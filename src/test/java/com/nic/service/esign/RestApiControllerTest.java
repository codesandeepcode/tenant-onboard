package com.nic.service.esign;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.nic.service.MappingConfiguration;

@WebMvcTest({ RestApiController.class })
@Import({ MappingConfiguration.class })
class RestApiControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private EsignService service;

	// ----- test for 'isProvidedApplicationNumberValid' method

	@Test
	final void testIsProvidedApplicationNumberValid_ProvidedApplicationNoIsNull() throws Exception {
		mvc.perform(get("/api/esign/check-validity")).andExpect(status().isBadRequest())
				.andExpect(handler().methodName("isProvidedApplicationNumberValid"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errors", hasItem(containsString("'application-number' is not present"))));
	}

	@Test
	final void testIsProvidedApplicationNumberValid_ProvidedApplicationNoIsBlank() throws Exception {
		mvc.perform(get("/api/esign/check-validity").param("application-number", "")).andExpect(status().isBadRequest())
				.andExpect(handler().methodName("isProvidedApplicationNumberValid"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errors", hasItem("Application number must be provided")));
	}

	@Test
	final void testIsProvidedApplicationNumberValid_ProvidedApplicationNoFormatIsWrong() throws Exception {
		mvc.perform(get("/api/esign/check-validity").param("application-number", "NAPIX-987455"))
				.andExpect(status().isBadRequest()).andExpect(handler().methodName("isProvidedApplicationNumberValid"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errors", hasItem("Application number's format is incorrect")));
	}

	@Test
	final void testIsProvidedApplicationNumberValid_ProvidedApplicationNoValidityWrong() throws Exception {
		when(service.isApplicationRefNumberValid("NAPIX/12345")).thenReturn(Optional.of("Validity is wrong"));

		mvc.perform(get("/api/esign/check-validity").param("application-number", "NAPIX/12345"))
				.andExpect(status().isBadRequest()).andExpect(handler().methodName("isProvidedApplicationNumberValid"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errors", hasItem("Validity is wrong")));
	}

	@Test
	final void testIsProvidedApplicationNumberValid_ProvidedApplicationNoValidityIsCorrect() throws Exception {
		when(service.isApplicationRefNumberValid("NAPIX/65432")).thenReturn(Optional.empty());

		mvc.perform(get("/api/esign/check-validity").param("application-number", "NAPIX/65432"))
				.andExpect(status().isOk()).andExpect(handler().methodName("isProvidedApplicationNumberValid"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.result", equalTo("Application no NAPIX/65432 is valid")));
	}

	@Test
	final void testIsProvidedApplicationNumberValid_ExceptionThrown() throws Exception {
		when(service.isApplicationRefNumberValid(ArgumentMatchers.anyString())).thenThrow(new RuntimeException());

		mvc.perform(get("/api/esign/check-validity").param("application-number", "NAPIX/65432"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(
						jsonPath("$.errors", hasItem("Internal server exception occured. Please retry after a while")))
				.andExpect(handler().methodName("isProvidedApplicationNumberValid"));
	}

	// ----- test for 'downloadPdfApplicationForm' method

	@Test
	final void testDownloadPdfApplicationForm_ApplicationNo_Valid() throws Exception {
		when(service.getUnsignedPdfReport("NAPIX/78945")).thenReturn("Random String".getBytes());

		mvc.perform(get("/services/download-application-form").param("application-number", "NAPIX/78945"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_PDF))
				.andExpect(content().bytes("Random String".getBytes())).andExpect(
						header().string("Content-Disposition", containsString("Application Ref No NAPIX/78945.pdf")));
	}

	@Test
	final void testDownloadPdfApplicationForm_ApplicationNo_InvalidFormat() throws Exception {
		mvc.perform(get("/services/download-application-form").param("application-number", "NAPIXS/789451")
				.param("signed", "false")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem("Application number's format is incorrect")));
	}

	@Test
	final void testDownloadPdfApplicationForm_ApplicationNo_NotProvided() throws Exception {
		mvc.perform(get("/services/download-application-form").param("signed", "false"))
				.andExpect(status().isBadRequest()).andExpect(
						jsonPath("$.errors", hasItem("Required String parameter 'application-number' is not present")));
	}

	@Test
	final void testDownloadPdfApplicationForm_SignedParam_NotProvided() throws Exception {
		when(service.getUnsignedPdfReport("NAPIX/78942")).thenReturn("Random String Number 2".getBytes());

		mvc.perform(get("/services/download-application-form").param("application-number", "NAPIX/78942"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_PDF))
				.andExpect(content().bytes("Random String Number 2".getBytes())).andExpect(
						header().string("Content-Disposition", containsString("Application Ref No NAPIX/78942.pdf")));
	}

	@Test
	final void testDownloadPdfApplicationForm_SignedParam_SetAsTrue() throws Exception {
		when(service.getSignedPdfReport("NAPIX/12345")).thenReturn("Random String Number 3".getBytes());

		mvc.perform(get("/services/download-application-form").param("application-number", "NAPIX/12345")
				.param("signed", "true")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_PDF))
				.andExpect(content().bytes("Random String Number 3".getBytes())).andExpect(header()
						.string("Content-Disposition", containsString("Application Ref No (Signed) NAPIX/12345.pdf")));
	}

	@Test
	final void testDownloadPdfApplicationForm_SignedParam_SetAsFalse() throws Exception {
		when(service.getUnsignedPdfReport("NAPIX/12115")).thenReturn("Random String 123".getBytes());

		mvc.perform(get("/services/download-application-form").param("application-number", "NAPIX/12115")
				.param("signed", "false")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_PDF))
				.andExpect(content().bytes("Random String 123".getBytes())).andExpect(
						header().string("Content-Disposition", containsString("Application Ref No NAPIX/12115.pdf")));
	}

	@Test
	final void testDownloadPdfApplicationForm_ExceptionThrown() throws Exception {
		when(service.getSignedPdfReport("NAPIX/12345")).thenThrow(RuntimeException.class);

		mvc.perform(get("/services/download-application-form").param("application-number", "NAPIX/12345")
				.param("signed", "true")).andExpect(status().isInternalServerError());
	}

}
