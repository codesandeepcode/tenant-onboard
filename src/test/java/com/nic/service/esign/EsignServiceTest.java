package com.nic.service.esign;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;

import com.github.javafaker.Faker;
import com.itextpdf.text.DocumentException;
import com.nic.service.esign.pdf.GovtApplicationDataMother;
import com.nic.service.esign.pdf.OtherApplicationDataMother;
import com.nic.service.onboarding.publisher.PublisherService;
import com.nic.service.utils.OrganisationType;

@ExtendWith(MockitoExtension.class)
class EsignServiceTest {

	@Mock
	private EsignRepository repository;

	@Mock
	private MappingJackson2XmlHttpMessageConverter xmlConverter;

	@Mock
	private PdfEsignService signService;

	@Mock
	private MockEnvironment environment;

	@Mock
	private PublisherService onBoardService;

	@InjectMocks
	private EsignService service;

	@Test
	void testGetSignedRequestInXml_InvalidApplicationNoForNic() throws IOException, DocumentException {
		when(onBoardService.fetchApplicationModelForNic("NAPIX/32154")).thenThrow(IllegalArgumentException.class);
		when(onBoardService.determineOrganisationType("NAPIX/32154")).thenReturn(OrganisationType.NIC);

		assertThrows(IllegalArgumentException.class, () -> service.getSignedRequestInXml("NAPIX/32154",
				new Faker().name().firstName(), new MockHttpServletRequest()));
	}

	@Test
	void testIsApplicationRefNumberValid_ApplicationNoIsInvalid() {
		when(onBoardService.doesApplicationExists("NAPIX/98765")).thenReturn(false);
		assertThat(service.isApplicationRefNumberValid("NAPIX/98765")).isPresent();
		assertThat(service.isApplicationRefNumberValid("NAPIX/98765")).contains("Application number does not exists");
	}

	@Test
	void testIsApplicationRefNumberValid_ApplicationNoIsValid_EsignAlreadyAvailable() {
		String appNo = "NAPIX/17263";

		when(onBoardService.doesApplicationExists(appNo)).thenReturn(true);
		when(onBoardService.getApplicationReferenceId(appNo)).thenReturn(1254);
		when(repository.fetchEsignResponse(1254)).thenReturn(Optional.of(new ESignResponseModel()));

		assertTrue(service.isApplicationRefNumberValid(appNo).isPresent());
		assertTrue(service.isApplicationRefNumberValid(appNo).get().length() > 10);
	}

	@Test
	void testIsApplicationRefNumberValid_ApplicationNoIsValid_EsignNotAvailable() {
		String appNo = "NAPIX/17263";

		when(onBoardService.doesApplicationExists(appNo)).thenReturn(true);
		when(onBoardService.getApplicationReferenceId(appNo)).thenReturn(45);
		when(repository.fetchEsignResponse(45)).thenReturn(Optional.empty());
		when(onBoardService.hasApplicationExceedTimeLimit(appNo)).thenReturn(false);

		assertFalse(service.isApplicationRefNumberValid(appNo).isPresent());
	}

	@Test
	void testIsApplicationRefNumberValid_ApplicationNoIsValid_TimeLimitExcceded() {
		String appNo = "NAPIX/17987";

		when(onBoardService.doesApplicationExists(appNo)).thenReturn(true);
		when(onBoardService.getApplicationReferenceId(appNo)).thenReturn(45);
		when(repository.fetchEsignResponse(45)).thenReturn(Optional.empty());
		when(onBoardService.hasApplicationExceedTimeLimit(appNo)).thenReturn(true);

		assertTrue(service.isApplicationRefNumberValid(appNo).isPresent());
	}

	@Test
	void testGetUnsignedPdfReport_ValidAppicationNoForGovt_ReportTypeGovt() {
		String appNo = "NAPIX/11654";

		when(onBoardService.fetchApplicationModelForGovt(appNo))
				.thenReturn(GovtApplicationDataMother.aDefaultApplication().build());
		when(onBoardService.determineOrganisationType(appNo)).thenReturn(OrganisationType.GOVT);

		assertTrue(service.getUnsignedPdfReport(appNo).length > 50);
	}

	@Test
	void testGetUnsignedPdfReport_ValidAppicationNoForGovt_ReportTypeOthers() {
		String appNo = "NAPIX/21456";

		when(onBoardService.fetchApplicationModelForOthers(appNo)).thenThrow(IllegalArgumentException.class);
		when(onBoardService.determineOrganisationType(appNo)).thenReturn(OrganisationType.OTHERS);

		assertThrows(IllegalArgumentException.class, () -> service.getUnsignedPdfReport(appNo));
	}

	@Test
	void testGetUnsignedPdfReport_ValidApplicationNoForOthers_ReportTypeOthers() {
		String appNo = "NAPIX/541232";

		when(onBoardService.fetchApplicationModelForOthers(appNo))
				.thenReturn(OtherApplicationDataMother.aDefaultApplication().build());
		when(onBoardService.determineOrganisationType(appNo)).thenReturn(OrganisationType.OTHERS);

		assertTrue(service.getUnsignedPdfReport(appNo).length > 50);
	}

	@Test
	void testGetUnsignedPdfReport_ValidApplicationNoForOthers_ReportTypeGovt() {
		String appNo = "NAPIX/32145";

		when(onBoardService.fetchApplicationModelForGovt(appNo)).thenThrow(IllegalArgumentException.class);
		when(onBoardService.determineOrganisationType(appNo)).thenReturn(OrganisationType.GOVT);

		assertThrows(IllegalArgumentException.class, () -> service.getUnsignedPdfReport(appNo));
	}

	@Test
	void testGetUnsignedPdfReport_ValidApplicationNoForOthers_ReportTypeNic() {
		String appNo = "NAPIX/32145";

		when(onBoardService.fetchApplicationModelForNic(appNo)).thenThrow(IllegalArgumentException.class);
		when(onBoardService.determineOrganisationType(appNo)).thenReturn(OrganisationType.NIC);

		assertThrows(IllegalArgumentException.class, () -> service.getUnsignedPdfReport(appNo));
	}

	@Test
	@Disabled
	void testGetSignedPdfReport() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled
	void testProcessESignResponse() {
		fail("Not yet implemented");
	}

}
