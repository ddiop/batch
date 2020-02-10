package hello.notifications;

import com.bnpparibas.bddf.plr.domain.notification.enumerations.CanauxDestinatairesEnum;
import com.bnpparibas.bddf.plr.domain.notification.enumerations.IdentifiantEmetteurMessage;
import com.bnpparibas.bddf.plr.domain.notification.enumerations.IdentifiantTypeMessageEnum;
import com.bnpparibas.bddf.plr.domain.notification.enumerations.LibelleCampagne;

/**
 * @author b51007
 *
 */

public class SMSNotification extends AbstractMessage implements Notification {

	private final String idEmetteurMessage = String.format("%-25s", IdentifiantEmetteurMessage.PL.getCode());
	private final String idTypeMessage = String.format("%-10s", IdentifiantTypeMessageEnum.SMSHOM01.getCode());
	private final String canauxDestinatairesPush = String.format("%-2s", CanauxDestinatairesEnum.SMS.getCode());
	private final String canalDestinataire = String.format("%-2s", CanauxDestinatairesEnum.SMS.getCode());
	private final String libelleCampagne = String.format("%-50s", LibelleCampagne.SMS_ALERTE.getLibelle());

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

}