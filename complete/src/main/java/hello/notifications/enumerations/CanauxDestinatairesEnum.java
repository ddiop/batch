package hello.notifications.enumerations;

public enum CanauxDestinatairesEnum {
	SERVEUR_VOCAL_INTERACTIF("03", "Serveur Vocal Interactif"), MESSAGE_SECUR_BNP("04", "la messagerie sécurisée BNP Paribas"), EMAILS("53", "emails"), SMS("55", "Sms");

	private String code;
	private String libelle;

	private CanauxDestinatairesEnum(String code, String libelle) {

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
