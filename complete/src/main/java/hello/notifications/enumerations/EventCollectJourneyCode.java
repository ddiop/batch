package hello.notifications.enumerations;

public enum EventCollectJourneyCode {

	SOLVABILITE("100000", "Journey code pour solvabilité"), COMPLETUDE("100001", "Journey code pour complétude");
	private String code;
	private String libelle;

	private EventCollectJourneyCode(final String code, final String libelle) {

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
