package hello.notifications.enumerations;

public enum ConstantNotification {

	LENGTH("10000", "taille maximale du message"), TOP_FACTURABLE("2", "Top facturabe"), NBRES_VARIABLES_MESSAGE_DEFAULT("01", "nombres de variables dans le message"), VARIABLE_BNP("1", "BNP Paribas");

	private String code;
	private String libelle;

	private ConstantNotification(String code, String libelle) {

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
