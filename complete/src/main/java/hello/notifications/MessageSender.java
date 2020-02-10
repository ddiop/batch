package hello.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

	@Value("${jms.mq.outbound-default-queue}")
	private String destination;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);

	@Autowired
	private final JmsTemplate jmsTemplate;

	public MessageSender(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void sendMessage(Object object, String nomenclatureRef) {

		LOGGER.debug("Invoking Send message " + object);

		// send Message
		jmsTemplate.convertAndSend(destination, object, new PLRMessagePostProcessor(nomenclatureRef));

	}

}
