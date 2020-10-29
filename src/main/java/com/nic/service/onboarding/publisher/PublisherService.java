package com.nic.service.onboarding.publisher;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nic.service.esign.pdf.GovtApplicationModel;
import com.nic.service.esign.pdf.OthersApplicationModel;
import com.nic.service.utils.OrganisationType;

@Service("onBoardingBusinessService")
public class PublisherService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final PublisherRepository repository;

	private final Long applicationTimeLimit;

	private final ObjectWriter writer;

	public PublisherService(PublisherRepository repository, Environment environment, ObjectMapper mapper) {
		this.repository = repository;
		this.writer = mapper.writer();

		this.applicationTimeLimit = Long
				.parseLong(environment.getRequiredProperty("publisher.application.max-time-limit"));
		log.debug("Lifetime of application before digital signing is set as {} day/s", this.applicationTimeLimit);
	}

	String saveNicApplication(NicApplicationFormModel application) {
		if (log.isDebugEnabled()) {
			try {
				log.debug("Application for NIC organisation - {}", writer.writeValueAsString(application));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return repository.save(application);
	}

	String saveGovernmentApplication(GovernmentApplicationFormModel application) {
		if (log.isDebugEnabled()) {
			try {
				log.debug("Application for Government organisation - {}", writer.writeValueAsString(application));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return repository.save(application);
	}

	String saveOthersApplication(OthersApplicationFormModel application) {
		if (log.isDebugEnabled()) {
			try {
				log.debug("Application for Others organisation - {}", writer.writeValueAsString(application));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return repository.save(application);
	}

	/**
	 * @param domainName
	 * @return TRUE if domain name is already registered in database <br />
	 *         FALSE if domain name is not registered in database i.e., is a unique
	 *         value
	 */
	boolean isDomainNameRegisteredInDatabase(String domainName) {
		log.debug("Check whether domain name - {} exists or not", domainName);
		return repository.isDomainNameAlreadyRegistered(domainName);
	}

	/**
	 * @param applicationReferenceNo Valid application reference number
	 * @return TRUE if application already exists, i.e., is registered in database
	 *         <br />
	 *         FALSE if application is not registered in database
	 */
	public boolean doesApplicationExists(String applicationReferenceNo) {
		log.debug("Check whether application with ref no {} exists", applicationReferenceNo);
		return repository.getApplicationReferenceId(applicationReferenceNo).isPresent();
	}

	public String getApplicationReferenceNumber(Integer applicationReferenceId) {
		log.debug("Fetch application reference no for given id - {}", applicationReferenceId);

		return repository.getApplicationReferenceNumber(applicationReferenceId)
				.orElseThrow(() -> new IllegalArgumentException(
						"Invalid application reference id provided - " + applicationReferenceId));
	}

	public Integer getApplicationReferenceId(String applicationReferenceNo) {
		log.debug("Fetch application reference id for given no - {}", applicationReferenceNo);

		return repository.getApplicationReferenceId(applicationReferenceNo)
				.orElseThrow(() -> new IllegalArgumentException(
						"Invalid application reference number provided - " + applicationReferenceNo));
	}

	public NicApplicationFormModel fetchApplicationModelForNic(String applicationReferenceNo) {
		Integer applicationId = getApplicationReferenceId(applicationReferenceNo);

		return repository.fetchNicApplicationModel(applicationId).orElseThrow(
				() -> new IllegalArgumentException("Application reference no provided is not a NIC application!"));
	}

	public GovtApplicationModel fetchApplicationModelForGovt(String applicationReferenceNo) {
		Integer applicationId = getApplicationReferenceId(applicationReferenceNo);

		return repository.fetchGovtApplicationModel(applicationId).orElseThrow(() -> new IllegalArgumentException(
				"Application reference no provided is not a Government application!"));
	}

	public OthersApplicationModel fetchApplicationModelForOthers(String applicationReferenceNo) {
		Integer applicationId = getApplicationReferenceId(applicationReferenceNo);

		return repository.fetchOthersApplicationModel(applicationId).orElseThrow(
				() -> new IllegalArgumentException("Application reference no provided is not a Others application"));
	}

	/**
	 * @param applicationReferenceNo Valid application reference number
	 * @return TRUE if application exceeded max time-limit for e-signing <br />
	 *         FALSE if application is within time-limit for e-signing
	 */
	public boolean hasApplicationExceedTimeLimit(String applicationReferenceNo) {
		Integer applicationId = getApplicationReferenceId(applicationReferenceNo);

		LocalDateTime creationDate = repository.getApplicationDateOfCreation(applicationId)
				.orElseThrow(() -> new RuntimeException(
						"Unexpected exception! Cannot retrieve application creation date from given application id "
								+ applicationId));
		LocalDate maxTimeOfApplication = creationDate.toLocalDate().plusDays(applicationTimeLimit);

		return (LocalDate.now().compareTo(maxTimeOfApplication) > 0) ? true : false;
	}

	/**
	 * @param applicationNo Valid application reference number
	 * @return Type of organization (NIC, GOVT or OTHERS) which the application is
	 *         registered under
	 */
	public OrganisationType determineOrganisationType(String applicationNo) {
		Integer applicationId = getApplicationReferenceId(applicationNo);

		if (repository.checkOrganisationTypeOfApplication(OrganisationType.NIC, applicationId))
			return OrganisationType.NIC;

		if (repository.checkOrganisationTypeOfApplication(OrganisationType.GOVT, applicationId))
			return OrganisationType.GOVT;

		if (repository.checkOrganisationTypeOfApplication(OrganisationType.OTHERS, applicationId))
			return OrganisationType.OTHERS;

		log.error(
				"Possible data insertion issues! Cannot determine organisation type with given application id {} and application no {}",
				applicationId, applicationNo);
		throw new RuntimeException(
				"Unexpected exception! Cannot determine organisation type of application no provided - "
						+ applicationNo);
	}

}
