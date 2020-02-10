package hello.notifications;

import com.bnpparibas.bddf.plr.domain.event.EventBMMHandler;
import com.ibm.msg.client.wmq.WMQConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 *
 * @author b51007
 *
 */
@Component
public class MessageBMMSender implements EventBMMHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageBMMSender.class);

	@Value("${jms.mq.queue-notification}")
	private String destination;

	private final JmsTemplate jmsTemplate;

	public MessageBMMSender(JmsTemplate jmsTemplate) {

		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * Send notification
	 *
	 */

	@Override
	public void sendMessage(Notification notification) {
		this.jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				LOGGER.debug("getIdEmetteurMessage: {} : getIdTypeMessage: {} : getLibelleCampagne: {} : length message{} " + notification.getIdEmetteurMessage(), notification.getIdTypeMessage(), notification.getLibelleCampagne(),
						notification.getNotificationMessage().length());
				Message msg = session.createTextMessage(notification.getNotificationMessage());
				msg.setIntProperty(WMQConstants.JMS_IBM_CHARACTER_SET, 37);
				return msg;
			}
		});

	}

}
