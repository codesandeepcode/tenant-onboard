package com.nic.service.esign;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itextpdf.text.DocumentException;
import com.nic.service.esign.pdf.GovtApplicationModel;
import com.nic.service.esign.pdf.GovtApplicationReportInPdf;
import com.nic.service.esign.pdf.NicApplicationReportInPdf;
import com.nic.service.esign.pdf.OthersApplicationModel;
import com.nic.service.esign.pdf.OthersApplicationReportInPdf;
import com.nic.service.onboarding.publisher.NicApplicationFormModel;
import com.nic.service.onboarding.publisher.PublisherService;
import com.nic.service.utils.OrganisationType;
import com.nic.service.utils.Utils;

@Service("eSignBusinessService")
class EsignService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final XmlMapper xmlMapper;

	private final EsignRepository repository;

	private final PdfEsignService signService;

	private final PublisherService onBoardingService;

	private final String domainNameUrl;

	private final String subDirectoryUrl;

	private final String esignResponseUrl;

	EsignService(MappingJackson2XmlHttpMessageConverter xmlConverter, EsignRepository auditService,
			PublisherService onBoardingService, PdfEsignService pdfEsignService, Environment environment) {
		this.xmlMapper = (XmlMapper) xmlConverter.getObjectMapper();
		this.repository = auditService;
		this.signService = pdfEsignService;
		this.onBoardingService = onBoardingService;
		this.domainNameUrl = environment.getProperty("application.domain-name");
		this.subDirectoryUrl = environment.getProperty("application.sub-directory");
		this.esignResponseUrl = environment.getRequiredProperty("nic-esign.gateway.redirect-response-url");
	}

	String getSignedRequestInXml(String applicationRefNo, String signingPerson, HttpServletRequest request)
			throws IOException, DocumentException {
		log.debug("Generating signed xml request for application - {}", applicationRefNo);

		String redirectUrl = esignResponseUrl + "?rs=" + Utils.returnBaseUrl(request, domainNameUrl, subDirectoryUrl)
				+ "/services/esign/process/gateway-response";
		byte[] document = getUnsignedPdfReport(applicationRefNo);
		String docInfo = "Reference no : " + applicationRefNo;
		ESignRequestModel eSignRequestData = new ESignRequestModel(redirectUrl, docInfo, document,
				repository.getEsignTransactionSequenceNo());

		if (log.isDebugEnabled()) {
			try {
				log.debug("Pre-signed Request Xml : {}", xmlMapper.writeValueAsString(eSignRequestData));
			} catch (Exception e) {
				log.error("Error in generating output during debug {}", e);
			}
		}

		String signedXml = signService.signRequestInXml(eSignRequestData);

		// When signing is successful, then save the details
		repository.saveEsignTransactionDetails(onBoardingService.getApplicationReferenceId(applicationRefNo),
				signingPerson, eSignRequestData);
		log.debug("Esign details saved to database");

		return signedXml;
	}

	Optional<String> isApplicationRefNumberValid(String applicationRefNo) {
		// check if application no exists
		if (!onBoardingService.doesApplicationExists(applicationRefNo))
			return Optional.of("Application number does not exists");

		// check if esign is already done
		if (repository.fetchEsignResponse(onBoardingService.getApplicationReferenceId(applicationRefNo)).isPresent())
			return Optional.of("Application is already digitally signed");

		// check if application creation's date is more than 7 days
		if (onBoardingService.hasApplicationExceedTimeLimit(applicationRefNo))
			return Optional.of("Application has exceeded the time limit for esigning");

		return Optional.empty();
	}

	/**
	 * Generate new unsigned PDF report for a given organization type
	 */
	byte[] getUnsignedPdfReport(String applicationRefNo) {
		byte[] fileBytes = null;
		OrganisationType type = onBoardingService.determineOrganisationType(applicationRefNo);

		switch (type) {
		case NIC:
			NicApplicationFormModel nicData = onBoardingService.fetchApplicationModelForNic(applicationRefNo);
			fileBytes = NicApplicationReportInPdf.generateReport(applicationRefNo, nicData);
			break;
		case GOVT:
			GovtApplicationModel govtData = onBoardingService.fetchApplicationModelForGovt(applicationRefNo);
			fileBytes = GovtApplicationReportInPdf.generateReport(applicationRefNo, govtData);
			break;
		case OTHERS:
			OthersApplicationModel othersData = onBoardingService.fetchApplicationModelForOthers(applicationRefNo);
			fileBytes = OthersApplicationReportInPdf.generateReports(applicationRefNo, othersData);
			break;
		}

		return fileBytes;
	}

	/**
	 * Get new signed PDF report
	 */
	byte[] getSignedPdfReport(String applicationRefNo) {
		byte[] document = getUnsignedPdfReport(applicationRefNo);
		Integer applicationId = onBoardingService.getApplicationReferenceId(applicationRefNo);

		ESignResponseModel responseModel = repository.fetchEsignResponse(applicationId)
				.orElseThrow(() -> new IllegalArgumentException(
						"Cannot retrieve esign transactions details from given no -" + applicationRefNo));
		String signingPerson = repository.getSigningPerson(applicationId)
				.orElseThrow(() -> new IllegalArgumentException("Cannot retrieve signing person name!"));

		try {
			return signService.signPdfReport(document, signingPerson, responseModel);
		} catch (IOException | DocumentException e) {
			throw new RuntimeException("Cannot sign pdf report", e);
		}
	}

	/**
	 * This method will process the response from e-sign gateway & save its details
	 * to database
	 * 
	 * <p>
	 * Note that this method will throw IllegalArgumentException if valid
	 * transaction id cannot be retrieved from e-sign response
	 * 
	 * @param responseModel
	 * @return Application reference no of associated e-signed application
	 */
	String processESignResponse(ESignResponseModel responseModel) {
		if (StringUtils.isBlank(responseModel.getTransactionId()))
			throw new IllegalArgumentException("Transaction Id from esign gateway response cannot be retrieved");

		log.debug("Saving response from e-sign gateway");
		Integer applicationId = repository.updateEsignTransactionDetails(responseModel);
		return onBoardingService.getApplicationReferenceNumber(applicationId);
	}

}
