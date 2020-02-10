package hello.notifications.enumerations;

public enum LibelleCampagne {

	SMS_ALERTE("1", "Home - SMS Alerte"), OFFRE_EMISE("2", "Home - Offre emise"), ENVOI_APAC("3", "Home - Envoi APAC"), RELANCE_ACCEPTATION("4", "Home - Relance acceptation"), DELAI_REFLEXION_ECOULE("5",
			"Home - Delai reflexion ecoule"), APPEL_DE_FONDS("5", "Home - Appel de fonds");
	private String code;
	private String libelle;

	private LibelleCampagne(String code, String libelle) {

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
