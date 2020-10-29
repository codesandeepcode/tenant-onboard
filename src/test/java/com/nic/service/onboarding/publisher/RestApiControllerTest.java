package com.nic.service.onboarding.publisher;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest({ RestApiController.class })
class RestApiControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private PublisherService service;

	// you may create more methods to test validations of request body

	@Test
	final void testProcessRequestForNic_ValidRequestBody() throws IOException, Exception {
		when(service.saveNicApplication(ArgumentMatchers.any(NicApplicationFormModel.class))).thenReturn("NAPIX/20002");

		File jsonFile = new File(getClass().getResource("publisher_application_nic.json").getPath());
		mvc.perform(post("/api/onboard/publisher/nic").contentType(MediaType.APPLICATION_JSON)
				.content(Files.readAllBytes(Paths.get(jsonFile.getPath())))).andExpect(status().isCreated())
				.andExpect(handler().methodName("processRequestForNic"))
				.andExpect(jsonPath("$.referenceNo", is(equalTo("NAPIX/20002"))));
	}

	@Test
	final void testProcessRequestForNic_RequestBodyNotProvided() throws Exception {
		mvc.perform(post("/api/onboard/publisher/nic")).andExpect(status().isBadRequest())
				.andExpect(handler().methodName("processRequestForNic"))
				.andExpect(jsonPath("$.errors", hasItem(containsString("Request body is required/missing"))));
	}

	@Test
	final void testProcessRequestForNic_ExceptionThrownDuringProcessing() throws IOException, Exception {
		when(service.saveNicApplication(ArgumentMatchers.any(NicApplicationFormModel.class)))
				.thenThrow(new RuntimeException());

		File jsonFile = new File(getClass().getResource("publisher_application_nic.json").getPath());
		mvc.perform(post("/api/onboard/publisher/nic").contentType(MediaType.APPLICATION_JSON)
				.content(Files.readAllBytes(Paths.get(jsonFile.getPath())))).andExpect(status().isInternalServerError())
				.andExpect(handler().methodName("processRequestForNic"));
	}

	@Test
	final void processRequestForGovt_ValidRequestBody() throws IOException, Exception {
		when(service.saveGovernmentApplication(ArgumentMatchers.any(GovernmentApplicationFormModel.class)))
				.thenReturn("NAPIX/20198");

		String requestBody = mapper
				.writeValueAsString(GovernmentApplicationFormModelObjectMother.defaultApplication(false).build());
		mvc.perform(post("/api/onboard/publisher/govt").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated()).andExpect(handler().methodName("processRequestForGovt"))
				.andExpect(jsonPath("$.referenceNo", is(equalTo("NAPIX/20198"))));
	}

	@Test
	final void processRequestForGovt_RequestBodyNotProvided() throws Exception {
		mvc.perform(post("/api/onboard/publisher/govt")).andExpect(status().isBadRequest())
				.andExpect(handler().methodName("processRequestForGovt"))
				.andExpect(jsonPath("$.errors", hasItem(containsString("Request body is required/missing"))));
	}

	@Test
	final void processRequestForGovt_ExceptionThrownDuringProcessing() throws Exception {
		when(service.saveGovernmentApplication(ArgumentMatchers.any(GovernmentApplicationFormModel.class)))
				.thenThrow(new RuntimeException());

		String requestBody = mapper
				.writeValueAsString(GovernmentApplicationFormModelObjectMother.defaultApplication(true).build());
		mvc.perform(post("/api/onboard/publisher/govt").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isInternalServerError()).andExpect(handler().methodName("processRequestForGovt"));
	}

	@Test
	final void testProcessRequestForOthers_ValidRequestBody() throws IOException, Exception {
		when(service.saveOthersApplication(ArgumentMatchers.any(OthersApplicationFormModel.class)))
				.thenReturn("NAPIX/20124");

		String requestBody = mapper
				.writeValueAsString(OthersApplicationFormModelObjectMother.defaultApplication(false).build());
		mvc.perform(post("/api/onboard/publisher/other").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated()).andExpect(handler().methodName("processRequestForOthers"))
				.andExpect(jsonPath("$.referenceNo", is(equalTo("NAPIX/20124"))));
	}

	@Test
	final void testProcessRequestForOthers_RequestBodyNotProvided() throws Exception {
		mvc.perform(post("/api/onboard/publisher/other")).andExpect(status().isBadRequest())
				.andExpect(handler().methodName("processRequestForOthers"))
				.andExpect(jsonPath("$.errors", hasItem(containsString("Request body is required/missing"))));
	}

	@Test
	final void testProcessRequestForOthers_ExceptionThrownDuringProcess() throws IOException, Exception {
		when(service.saveOthersApplication(ArgumentMatchers.any(OthersApplicationFormModel.class)))
				.thenThrow(RuntimeException.class);

		String requestBody = mapper
				.writeValueAsString(OthersApplicationFormModelObjectMother.defaultApplication(true).build());
		mvc.perform(post("/api/onboard/publisher/other").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isInternalServerError()).andExpect(handler().methodName("processRequestForOthers"));
	}

	@Test
	void testCanDomainNameBeUsed_NoDomainNameProvided() throws Exception {
		mvc.perform(get("/api/onboard/verify/does-not-exist")).andExpect(status().isBadRequest());
	}

	@Test
	void testCanDomainNameBeUsed_IncorrectFormattedDomainNameProvided() throws Exception {
		mvc.perform(get("/api/onboard/verify/does-not-exist").param("domain-name", "asdg2736dhajs"))
				.andExpect(handler().methodName("canDomainNameBeUsed")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem("Invalid domain name provided!")));

		verify(service, times(0)).isDomainNameRegisteredInDatabase(anyString());
	}

	@Test
	void testCanDomainNameBeUsed_DomainNameDoesNotExists() throws Exception {
		when(service.isDomainNameRegisteredInDatabase("napix.nic.in")).thenReturn(false);

		mvc.perform(get("/api/onboard/verify/does-not-exist").param("domain-name", "napix.nic.in"))
				.andExpect(handler().methodName("canDomainNameBeUsed")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", equalTo("Domain 'napix.nic.in' can be used")));

		verify(service).isDomainNameRegisteredInDatabase("napix.nic.in");
	}

	@Test
	void testCanDomainNameBeUsed_DomainNameDoesExists() throws Exception {
		when(service.isDomainNameRegisteredInDatabase("testnapix.nic.in")).thenReturn(true);

		mvc.perform(get("/api/onboard/verify/does-not-exist").param("domain-name", "testnapix.nic.in"))
				.andExpect(handler().methodName("canDomainNameBeUsed")).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errors", hasItem("Domain 'testnapix.nic.in' already exist!")));

		verify(service).isDomainNameRegisteredInDatabase("testnapix.nic.in");
	}

}
