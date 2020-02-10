package hello.notifications;

import com.bnpparibas.bddf.plr.domain.notification.enumerations.ConstantNotification;

import java.text.NumberFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

public abstract class AbstractMessage implements Notification {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
	final NumberFormat numberFormater = NumberFormat.getNumberInstance();
	private String idTelematique = String.format("%10s", ' ');
	private String idRP = String.format("%-17d", 0);
	private String idSGI = String.format("%012d", 0);
	private String codeAgence = String.format("%05d", 0);
	private String telephone = String.format("%10s", ' ');
	private String partieEmailGauche = String.format("%-80s", ' ');
	private String partieEmailDroite = String.format("%-80s", ' ');
	private String referenceCampagne = String.format("%010d", 0);
	private String dateDebutValidite = String.format("%-10s", ' ');
	private String heureDebutValidite = String.format("%-8s", ' ');
	private String dateFinValidite = String.format("%-10s", ' ');
	private String heureFinValidite = String.format("%-8s", ' ');

	private String topFacturable = ConstantNotification.TOP_FACTURABLE.getCode();
	private String nbresVariablesMessage = ConstantNotification.NBRES_VARIABLES_MESSAGE_DEFAULT.getCode();

	private String variablesMessage = String.format("%-50s", ' ');

	final private String filler = String.format("%-4698s", ' ');

	public AbstractMessage() {
	}

	@Override
	public String getNotificationMessage() {
		StringJoiner join = new StringJoiner("");
		join.add(idTelematique);
		join.add(getIdEmetteurMessage());
		join.add(idRP);
		join.add(idSGI);
		join.add(codeAgence);
		join.add(getIdTypeMessage());
		join.add(getCanauxDestinatairesPush());
		join.add(telephone);
		join.add(partieEmailGauche);
		join.add(partieEmailDroite);
		join.add(referenceCampagne);
		join.add(getLibelleCampagne());
		join.add(dateDebutValidite);
		join.add(heureDebutValidite);
		join.add(dateFinValidite);
		join.add(heureFinValidite);
		join.add(getCanalDestinataire());
		join.add(topFacturable);
		join.add(nbresVariablesMessage);
		join.add(variablesMessage);
		join.add(filler);
		return join.toString();
	}

	public String getIdTelematique() {

		return idTelematique;
	}

	public void setIdTelematique(String idTelematique) {
		this.idTelematique = String.format("%10s", idTelematique);
	}

	public String getIdRP() {
		return idRP;
	}

	public void setIdRP(Long idRP) {
		this.idRP = String.format("%017d", idRP);
	}

	public String getIdSGI() {
		return idSGI;
	}

	public void setIdSGI(Long idSGI) {

		this.idSGI = String.format("%012d", idSGI);
	}

	public String getCodeAgence() {
		return codeAgence;
	}

	public void setCodeAgence(Long codeAgence) {
		this.codeAgence = String.format("%05d", codeAgence);
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = String.format("%10s", telephone);
	}

	public String getPartieEmailGauche() {
		return partieEmailGauche;
	}

	public void setPartieEmailGauche(String partieEmailGauche) {
		this.partieEmailGauche = String.format("%-80s", partieEmailGauche);
	}

	public String getPartieEmailDroite() {
		return partieEmailDroite;
	}

	public void setPartieEmailDroite(String partieEmailDroite) {
		this.partieEmailDroite = String.format("%-80s", partieEmailDroite);
	}

	public String getReferenceCampagne() {
		return referenceCampagne;
	}

	public void setReferenceCampagne(Long referenceCampagne) {
		this.referenceCampagne = String.format("%010d", referenceCampagne);
	}

	public String getDateDebutValidite() {
		return dateDebutValidite;
	}

	public void setDateDebutValidite(String dateDebutValidite) {
		this.dateDebutValidite = String.format("%-10s", dateDebutValidite);
	}

	public String getHeureDebutValidite() {
		return heureDebutValidite;
	}

	public void setHeureDebutValidite(String heureDebutValidite) {
		this.heureDebutValidite = String.format("%-8s", LocalTime.parse(heureDebutValidite).format(FORMATTER));
	}

	public String getDateFinValidite() {
		return dateFinValidite;
	}

	public void setDateFinValidite(String dateFinValidite) {
		this.dateFinValidite = String.format("%-10s", dateFinValidite);
	}

	public String getHeureFinValidite() {
		return heureFinValidite;
	}

	public void setHeureFinValidite(String heureFinValidite) {
		this.heureFinValidite = String.format("%-8s", LocalTime.parse(heureFinValidite).format(FORMATTER));
	}

	public String getTopFacturable() {
		return this.topFacturable;
	}

	public void setTopFacturable(int topFacturable) {
		this.topFacturable = String.format("%d", 2);

	}

	public String getNbresVariablesMessage() {
		return nbresVariablesMessage;
	}

	public void setNbresVariablesMessage(Long nbresVariablesMessage) {
		this.nbresVariablesMessage = String.format("%02d", nbresVariablesMessage);

	}

	public String getVariablesMessage() {
		return this.variablesMessage;
	}

	public void setVariablesMessage(List<String> var) {

		StringJoiner join = new StringJoiner("");
		int nbMessage = var.size();
		var.stream().forEach(v -> {
			join.add(String.format("%-50s", v));
		});

		for (int i = nbMessage; i < 99; i++) {
			join.add(String.format("%-50s", variablesMessage));

		}
		variablesMessage = join.toString();

	}

}