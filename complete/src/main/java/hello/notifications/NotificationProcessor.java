package hello.notifications;

import com.bnpparibas.bddf.jms.sender.domain.EventData;

import java.util.List;

public interface NotificationProcessor {

	void processNotification(EventData event);

	SecureMessageBNPNotification secureMessageByCustomerId(EventData event, String customerId, List<String> listsVariable);

	SMSNotification smsByCustomerId(EventData event, String customerId, List<String> listsVariable);

}
