package hello.notifications;

import com.bnpparibas.bddf.plr.domain.notification.enumerations.CanauxDestinatairesEnum;
import com.bnpparibas.bddf.plr.domain.notification.enumerations.IdentifiantEmetteurMessage;
import com.bnpparibas.bddf.plr.domain.notification.enumerations.LibelleCampagne;

/**
 * @author b51007
 *
 */

public class SecureMessageBNPNotification extends AbstractMessage implements Notification {

	private final String idEmetteurMessage = String.format("%-25s", IdentifiantEmetteurMessage.PL.getCode());
	private String idTypeMessage = String.format("%-10s", ' ');
	private final String canauxDestinatairesPush = String.format("%-2s", CanauxDestinatairesEnum.MESSAGE_SECUR_BNP.getCode());
	private final String canalDestinataire = String.format("%-2s", CanauxDestinatairesEnum.MESSAGE_SECUR_BNP.getCode());
	private String libelleCampagne = String.format("%-50s", LibelleCampagne.OFFRE_EMISE.getLibelle());

	public SecureMessageBNPNotification() {

	}

	@Override
	public String getIdEmetteurMessage() {

		return idEmetteurMessage;
	}

	@Override
	public String getCanauxDestinatairesPush() {
		return canauxDestinatairesPush;
	}

	@Override
	public String getCanalDestinataire() {
		return canalDestinataire;
	}

	@Override
	public String getIdTypeMessage() {

		return idTypeMessage;
	}

	@Override
	public String getLibelleCampagne() {

		return libelleCampagne;
	}

	public void setIdTypeMessage(String idTypeMessage) {
		this.idTypeMessage = String.format("%-10s", idTypeMessage);
	}

	public void setLibelleCampagne(String libelleCampagne) {
		this.libelleCampagne = String.format("%-50s", libelleCampagne);
	}

}