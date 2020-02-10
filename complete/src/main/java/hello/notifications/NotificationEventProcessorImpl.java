package hello.notifications;

import com.bnpparibas.bddf.domain.ddd.DDD;
import com.bnpparibas.bddf.jms.converter.ConverterUtil;
import com.bnpparibas.bddf.jms.sender.domain.EventData;
import com.bnpparibas.bddf.plr.domain.borrower.PotentialBorrower;
import com.bnpparibas.bddf.plr.domain.borrower.PotentialBorrowerRepository;
import com.bnpparibas.bddf.plr.domain.event.HomeEventType;
import com.bnpparibas.bddf.plr.domain.notification.common.Utilities;
import com.bnpparibas.bddf.plr.domain.notification.enumerations.ConstantNotification;
import com.bnpparibas.bddf.plr.domain.propertyloan.request.Profile;
import com.bnpparibas.bddf.plr.domain.propertyloan.request.PropertyLoanRequest;
import com.bnpparibas.bddf.plr.domain.propertyloan.request.PropertyLoanRequestRepository;
import com.bnpparibas.bddf.plr.infrastructure.notifications.NotificationToSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@DDD.RepositoryImpl
@Repository
public class NotificationEventProcessorImpl implements NotificationProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationEventProcessorImpl.class);
	private final PotentialBorrowerRepository potentialBorrowerRepository;
	private final NotificationToSend notificationToSend;

	private final PropertyLoanRequestRepository propertyLoanRequestRepository;

	public NotificationEventProcessorImpl(PotentialBorrowerRepository potentialBorrowerRepository, NotificationToSend notificationToSend, PropertyLoanRequestRepository propertyLoanRequestRepository) {
		super();
		this.potentialBorrowerRepository = potentialBorrowerRepository;
		this.notificationToSend = notificationToSend;
		this.propertyLoanRequestRepository = propertyLoanRequestRepository;
	}

	private void sendNotification(Notification notification) {
		LOGGER.info(" send notification [{}]  ", notification);
		notificationToSend.sendNotification(notification);
	}

	@Override
	public void processNotification(EventData event) {
		final HomeEventType eventType = HomeEventType.findByCode(event.getHeaderData().getNomenclatureRef());
		sendSMS(event, eventType);
		sendSecureMessage(event, eventType);

	}

	/**
	 *
	 * @param event
	 *
	 * @param eventType
	 */
	private void sendSecureMessage(EventData event, HomeEventType eventType) {

		List<String> listVariables = listsVariableMessage(event, eventType);
		final List<SecureMessageBNPNotification> secureMessage = getSecureMessageBNP(event, listVariables);
		secureMessage.stream().forEach(message -> sendNotification(message));
	}

	/**
	 *
	 * @param event
	 *
	 * @param eventType
	 */
	private void sendSMS(EventData event, HomeEventType eventType) {
		List<String> listVariables = listsVariableMessage(event, eventType);
		List<SMSNotification> listSMS = getSms(event, listVariables);
		listSMS.stream().forEach(sms -> {
			final String phone = sms.getTelephone();
			final boolean phoneValid = Utilities.isPhoneFrenchValid(phone);
			if (phoneValid) {
				Utilities.phoneFormatBMM(sms, phone);
				sendNotification(sms);
				LOGGER.info("sms  sent because : phone number [{}]  valid", phone);
			} else {
				LOGGER.error("sms not sent because : phone number [{}] is not valid", phone);
			}
		});
	}

	/**
	 *
	 * @param event
	 *
	 * @param listsVariable
	 *
	 * @return List<SMSNotification>
	 */
	private List<SMSNotification> getSms(EventData event, List<String> listsVariable) {
		final List<String> borrowersIds = borrowersIdsFromEvent(event);
		return borrowersIds.stream().map(id -> smsByCustomerId(event, id, listsVariable)).collect(Collectors.toList());
	}

	/**
	 *
	 * @param event
	 *            Event description
	 *
	 * @param customerId
	 *            customerId description
	 *
	 * @param listsVariable
	 *            listsVariable description
	 *
	 * @return SMSNotification
	 */
	@Override
	public SMSNotification smsByCustomerId(EventData event, String customerId, List<String> listsVariable) {
		SMSNotification sms = new SMSNotification();
		sms.setIdRP(Long.valueOf(customerId));
		sms = (SMSNotification) Utilities.variablesMessage(sms, listsVariable);
		List<Profile> profiles = Optional.ofNullable(getProfiles(event)).orElse(Collections.emptyList());
		Profile profile = profiles.stream().filter(b -> customerId.equals(b.getCustomerId())).findFirst().orElse(null);
		if (Optional.ofNullable(profile).isPresent()) {
			Optional<String> mobile = Optional.ofNullable(profile.getMobile());
			Optional<String> fix = Optional.ofNullable(profile.getPhoneNumber());
			if (!mobile.orElse("").isEmpty()) {
				LOGGER.info("*** mobile number retrieved in property loan request** ");
				setPhoneFromProfileOrPotentielBorrower(sms, profile.getMobile());
			} else if (mobile.orElse("").isEmpty() && fix.isPresent()) {
				LOGGER.error("*** the client does not wish to receive sms ***");
			} else if (mobile.orElse("").isEmpty() && fix.orElse("").isEmpty()) {
				LOGGER.info("**** the PLR ​​does not come from the online form ,the default initialization performed at simulation validation in DéfiImmo (event 6U )***");
				updateProfileWithPhones(event, customerId, sms);
			}

		} else {
			LOGGER.info("***** mobile phone to search in event*****");
			updateProfileWithPhones(event, customerId, sms);
		}

		return sms;
	}

	/**
	 *
	 *
	 * @param event
	 *            event description
	 *
	 * @param listsVariable
	 *            listsVariable description
	 *
	 * @return List<SecureMessageBNPNotification>
	 */
	private List<SecureMessageBNPNotification> getSecureMessageBNP(EventData event, List<String> listsVariable) {
		final List<String> borrowersIds = borrowersIdsFromEvent(event);
		return borrowersIds.stream().map(id -> secureMessageByCustomerId(event, id, listsVariable)).collect(Collectors.toList());
	}

	/**
	 *
	 * @param event
	 *            event description
	 *
	 * @param customerId
	 *            customerId description
	 *
	 * @param listsVariable
	 *            listsVariable description
	 *
	 * @return SecureMessageBNPNotification
	 */
	@Override
	public SecureMessageBNPNotification secureMessageByCustomerId(EventData event, String customerId, List<String> listsVariable) {
		SecureMessageBNPNotification secureMessage = new SecureMessageBNPNotification();
		secureMessage.setIdRP(Long.valueOf(customerId));
		final String idTypeMessage = Utilities.getTypeMessageLabelCampagne(event).getIdTypeMessage();
		secureMessage.setIdTypeMessage(idTypeMessage);
		final String libelleCampagne = Utilities.getTypeMessageLabelCampagne(event).getLibelleCampagne();
		secureMessage.setLibelleCampagne(libelleCampagne);
		secureMessage = (SecureMessageBNPNotification) Utilities.variablesMessage(secureMessage, listsVariable);

		return secureMessage;
	}

	/**
	 *
	 * @param event
	 *            event description
	 *
	 * @param eventType
	 *            eventType description
	 */
	private List<String> listsVariableMessage(EventData event, HomeEventType eventType) {
		final List<String> listVariables;
		final XMLGregorianCalendar dateTimeRef = event.getHeaderData().getDateTimeRef();
		final int days = 20;
		String dateOffre = ConverterUtil.fromXMLGregorianCalendarToZonedDateTime(dateTimeRef).minusDays(days).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		switch (eventType) {
		case FIN_DE_VALIDITE_OFFRE_DE_PRET:
			listVariables = Arrays.asList(ConstantNotification.VARIABLE_BNP.getLibelle(), dateOffre);
			break;
		default:
			listVariables = Arrays.asList(ConstantNotification.VARIABLE_BNP.getLibelle());
		}
		return listVariables;
	}

	/**
	 *
	 * @param event
	 *            event description
	 *
	 * @return Lists idCustomers from event
	 */
	private List<String> borrowersIdsFromEvent(EventData event) {
		LOGGER.info("get borrowers Ids from event");
		return event.getHeaderData().getBusinessContextEvent().getConsumers();

	}

	/**
	 *
	 * @param event
	 * @return list Customers from Event
	 */
	private List<PotentialBorrower> getCustomers(EventData event) {
		LOGGER.info("get customers from ope goal");
		final HashSet<String> listIdCustomers = new HashSet<>(borrowersIdsFromEvent(event));
		final Optional<List<PotentialBorrower>> customers = Optional.ofNullable(potentialBorrowerRepository.getPotentialBorrowers(listIdCustomers));
		return customers.orElse(Collections.emptyList());

	}

	private void setPhoneFromProfileOrPotentielBorrower(AbstractMessage sms, String phone) {
		Utilities.parsingPhone(sms, phone);
	}

	/**
	 *
	 * @param event
	 *            event description
	 *
	 * @return List<Profile> from database
	 */
	private List<Profile> getProfiles(EventData event) {
		LOGGER.info("get profiles from data base");
		final String plrId = event.getHeaderData().getConceptEvent().getIdentifiantConcept();
		final Optional<PropertyLoanRequest> plr = Optional.ofNullable(propertyLoanRequestRepository.findPlr(plrId));
		if (plr.isPresent()) {
			return propertyLoanRequestRepository.findPlr(plrId).getProfiles();
		} else {
			return Collections.emptyList();
		}

	}

	/**
	 *
	 * @param event
	 *            event description
	 *
	 * @param customerId
	 *            customerId description
	 *
	 * @param sms
	 *            sms description
	 */
	private void updateProfileWithPhones(EventData event, String customerId, SMSNotification sms) {
		LOGGER.info(" set phone borrower");
		List<PotentialBorrower> borrowers = Optional.ofNullable(getCustomers(event)).orElse(Collections.emptyList());
		final Optional<PotentialBorrower> borrower = borrowers.stream().filter(b -> customerId.equals(b.getCustomerId())).findFirst();
		borrower.ifPresent(borr -> setPhoneFromProfileOrPotentielBorrower(sms, borr.getMobile()));
	}
}
