package hello.notifications;

public class TypeMessageLabelCampagne {
	private final String libelleCampagne;
	private final String idTypeMessage;

	public TypeMessageLabelCampagne(String idTypeMessage, String libelleCampagne) {
		this.libelleCampagne = String.format("%-50s", libelleCampagne);
		this.idTypeMessage = String.format("%-10s", idTypeMessage);
	}

	public String getLibelleCampagne() {
		return String.format("%-50s", libelleCampagne);
	}

	public String getIdTypeMessage() {
		return String.format("%-10s", idTypeMessage);
	}

}
