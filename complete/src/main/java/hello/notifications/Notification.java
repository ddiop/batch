package hello.notifications;

public interface Notification {
	public String getNotificationMessage();

	public String getIdEmetteurMessage();

	public String getIdTypeMessage();

	public String getCanauxDestinatairesPush();

	public String getCanalDestinataire();

	public String getLibelleCampagne();

}