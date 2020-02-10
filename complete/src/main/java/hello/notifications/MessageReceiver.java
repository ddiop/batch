package hello.notifications;

import com.bnpparibas.bddf.aop.logging.LayerMarker;
import com.bnpparibas.bddf.errors.BusinessException;
import com.bnpparibas.bddf.jms.converter.XPathUtils;
import com.bnpparibas.bddf.plr.domain.event.HomeEventType;
import com.bnpparibas.bddf.plr.domain.service.error.PlrErrorConstants;
import com.bnpparibas.bddf.plr.infrastructure.jms.process.event.EventOperation;
import com.bnpparibas.bddf.plr.infrastructure.jms.process.event.ProcessEventAcceptingCommercialProposalOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@EnableJms
public class MessageReceiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiver.class);

	private static final String NOMENCLATURE_REF_HIERARCHY = "/Event/header/nomenclatureRef";

	private static final String APP_ORIGIN = "AppOrigin";

	private static final String NOMENCLATURE_REF = "nomenclatureRef";

	@Value("${template.plr.header.id:AP12243}")
	private String appId;

	@Autowired
	private List<EventOperation> eventOperations;

	@Autowired
	private ProcessEventAcceptingCommercialProposalOperation processEventAcceptingCommercialProposalOperation;

	@JmsListener(destination = "${jms.mq.inbound-default-queue}")
	public void receivedMessage(Message message) throws JMSException {
		try {
			initMDCForEvent(message);

			LOGGER.info("Reception of event with message ID {}", message.getJMSMessageID());
			LOGGER.debug("Unmarshalling du message : {}", message);

			/**
			 * This mechanism is temporary waiting that DFEvent Send a 6W event after receiving this event from plr app
			 */
			if (HomeEventType.ACCEPTED_COMERCIAL_PROPOSITION.getCode().equals(message.getStringProperty(NOMENCLATURE_REF))) {
				processEventAcceptingCommercialProposalOperation.processEvent((((TextMessage) message).getText()), HomeEventType.ACCEPTED_COMERCIAL_PROPOSITION);
			} else {
				nominalProcess(message);
			}

		} finally {
			// Tear down MDC data:
			// ( Important! Cleans up the ThreadLocal data again )
			MDC.clear();
		}
	}

	private void nominalProcess(Message message) throws JMSException {
		if (appId.equals(message.getStringProperty(APP_ORIGIN))) {
			LOGGER.debug("Ingore the PLR sending event {} ", message);
		} else {
			if (message instanceof TextMessage) {
				readTextMessageEvent(message);
			} else {
				LOGGER.error("The PLR manage only the TextMessage events format. The event has {} format", message.getClass());
			}
		}
	}

	private void readTextMessageEvent(Message message) throws JMSException {
		String xml = ((TextMessage) message).getText();
		LOGGER.debug("Event Message Content:\n {} ", xml);
		String xmlResult = Optional.ofNullable(xml)
									.map(str->str.replaceAll("^\\s+", ""))
									.map(str->str.replaceAll("\\s+$", ""))
									.orElseThrow(() -> new BusinessException(PlrErrorConstants.ERR_EVENT_INVALID_EVENT_FORMAT));
		HomeEventType eventType = HomeEventType.findByCode(XPathUtils.getElementValue(xmlResult, NOMENCLATURE_REF_HIERARCHY));
		Optional.ofNullable(eventType).orElseThrow(() -> new BusinessException(PlrErrorConstants.ERR_EVENT_INVALID_EVENT_FORMAT));

		List<HomeEventType> correctsEvents = new ArrayList<>();
		eventOperations.stream().filter(operation -> operation.isApplicable(eventType, message)).findFirst().ifPresent(operation -> {
			correctsEvents.add(eventType);
			operation.processEvent(xmlResult, eventType);
		});
		if (correctsEvents.isEmpty()) {
			throw new BusinessException(PlrErrorConstants.ERR_EVENT_INVALID_EVENT_FORMAT, eventType.getCode());
		}
	}

	/**
	 * Initialize MCD parameters for events
	 *
	 * @param message
	 *            the event Message
	 * @throws JMSException
	 *             Exception if event parameters does not exist
	 */
	private void initMDCForEvent(Message message) throws JMSException {
		MDC.put("HostName", getHostName());
		MDC.put("IP", message.getJMSMessageID());
		MDC.put("AppName", appId);
		MDC.put("JMSCorrelationID", message.getJMSCorrelationID());
		MDC.put("Marker", LayerMarker.INFRASTRUCTURE.getLabel());
		Optional.ofNullable(message.getJMSTimestamp()).ifPresent(timesTamp -> {
			Instant instant = Instant.ofEpochMilli(Long.valueOf(timesTamp));
			MDC.put("JMSTimeHandledMessage", String.valueOf(instant));
		});
	}

	private String getHostName() {
		try {
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (UnknownHostException ex) {
			LOGGER.error("Hostname can not be resolved: {}", ex);
		}
		return "";
	}
}
