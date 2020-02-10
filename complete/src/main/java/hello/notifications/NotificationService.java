package hello.notifications;

import com.bnpparibas.bddf.domain.ddd.DDD;
import com.bnpparibas.bddf.jms.sender.domain.EventData;

/**
 *
 */
@DDD.ApplicationService
public interface NotificationService {

	SMSNotification getNotificationMessage(EventData event);

}
