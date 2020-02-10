package hello.notifications.enumerations;

public enum IdentifiantEmetteurMessage {

	PL("PL", "idenifiant emetteur message sms");

	private String code;
	private String libelle;

	private IdentifiantEmetteurMessage(String code, String libelle) {

		this.libelle = libelle;
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public String getCode() {
		return code;
	}

}
