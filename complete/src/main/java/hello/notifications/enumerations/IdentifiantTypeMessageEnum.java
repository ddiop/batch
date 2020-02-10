package hello.notifications.enumerations;

public enum IdentifiantTypeMessageEnum {

	SMSHOM01("SMSHOM01", "message sms"), MSHOM001("MSHOM001", "son dossier de prêt est transmis pour étude"), MSHOM002("MSHOM002", "l'offre a été éditée"), MSHOM003("MSHOM003", "peux signer son offre"), MSHOM004("MSHOM004",
			"peux signer son offre"), MSHOM005("MSHOM005", "Etre informé que la banque a versé l’appel de fonds en ligne");
	private String code;
	private String libelle;

	private IdentifiantTypeMessageEnum(String code, String libelle) {

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
