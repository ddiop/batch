package hello.notifications;

import com.bnpparibas.bddf.jms.sender.domain.EventData;
import com.bnpparibas.bddf.plr.domain.event.HomeEventType;
import com.bnpparibas.bddf.plr.domain.notification.enumerations.IdentifiantTypeMessageEnum;
import com.bnpparibas.bddf.plr.domain.notification.enumerations.LibelleCampagne;
import com.google.common.base.Strings;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public final class Utilities {
	private Utilities() {

	}

	public static boolean isPhoneFrenchValid(String phone) {
		if (!Strings.isNullOrEmpty(phone)) {
			return phone.matches("(0|\\+33|0033)[6-7][0-9]{8}");
		}
		return false;
	}

	public static void parsingPhone(AbstractMessage notification, String phone) {
		Optional<String> mobile = Optional.ofNullable(phone);
		mobile.ifPresent(mobil -> {
			boolean isPlus = mobil.startsWith("+");
			String finalMobile = mobil;
			if (isPlus && notification instanceof SMSNotification) {
				finalMobile = finalMobile.replace("+", "00");
			}

			notification.setTelephone(finalMobile);
		});

	}

	public static void phoneFormatBMM(SMSNotification message, String phone) {
		final StringJoiner join = new StringJoiner("");
		final int taillePhone = phone.length();
		final String substringPhone = phone.substring(taillePhone - 9, taillePhone);
		final String phoneFormatBMM = join.add("0").add(substringPhone).toString();
		message.setTelephone(phoneFormatBMM);
	}

	public static TypeMessageLabelCampagne getTypeMessageLabelCampagne(EventData event) {
		HomeEventType eventType = HomeEventType.findByCode(event.getHeaderData().getNomenclatureRef());
		EnumMap<HomeEventType, TypeMessageLabelCampagne> map = new EnumMap<>(HomeEventType.class);
		map.put(HomeEventType.DEMANDE_DE_PRET_TRANSMIS, new TypeMessageLabelCampagne(IdentifiantTypeMessageEnum.MSHOM001.getCode(), LibelleCampagne.ENVOI_APAC.getLibelle()));
		map.put(HomeEventType.OFFRE_DE_PRET_EMISE, new TypeMessageLabelCampagne(IdentifiantTypeMessageEnum.MSHOM002.getCode(), LibelleCampagne.OFFRE_EMISE.getLibelle()));
		map.put(HomeEventType.OFFRE_DE_PRET_DISPONIBLE, new TypeMessageLabelCampagne(IdentifiantTypeMessageEnum.MSHOM003.getCode(), LibelleCampagne.DELAI_REFLEXION_ECOULE.getLibelle()));
		map.put(HomeEventType.FIN_DE_VALIDITE_OFFRE_DE_PRET, new TypeMessageLabelCampagne(IdentifiantTypeMessageEnum.MSHOM004.getCode(), LibelleCampagne.RELANCE_ACCEPTATION.getLibelle()));
		map.put(HomeEventType.DEBLOCAGE_DE_FONDS_TRANSMIS, new TypeMessageLabelCampagne(IdentifiantTypeMessageEnum.MSHOM005.getCode(), LibelleCampagne.APPEL_DE_FONDS.getLibelle()));
		return map.get(eventType);
	}

	public static AbstractMessage variablesMessage(AbstractMessage message, List<String> listsVariable) {
		message.setNbresVariablesMessage((long) listsVariable.size());
		message.setVariablesMessage(listsVariable);
		return message;

	}

}
