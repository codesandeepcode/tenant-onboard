package com.nic.service.onboarding.publisher;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nic.service.SpringBootTestWrapper;

class IntegrationTest extends SpringBootTestWrapper {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YY");

	@Test
	final void testProcessRequestForNic() throws IOException, Exception {
		File jsonFile = new File(getClass().getResource("publisher_application_nic.json").getPath());
		mvc.perform(post("/api/onboard/publisher/nic").contentType(MediaType.APPLICATION_JSON)
				.content(Files.readAllBytes(Paths.get(jsonFile.getPath())))).andExpect(status().isCreated())
				.andExpect(handler().methodName("processRequestForNic")).andExpect(jsonPath("$.referenceNo",
						containsString("NAPIX/".concat(formatter.format(LocalDateTime.now()).toUpperCase()))));
	}

	@Test
	final void testProcessRequestForNicNoTechAdmin2() throws IOException, Exception {
		String requestBody = mapper.writeValueAsString(
				NicApplicationFormModelObjectMother.defaultApplication().withTechAdmin2(null).build());

		mvc.perform(post("/api/onboard/publisher/nic").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated()).andExpect(handler().methodName("processRequestForNic"))
				.andExpect(jsonPath("$.referenceNo",
						containsString("NAPIX/".concat(formatter.format(LocalDateTime.now()).toUpperCase()))));
	}

	@RepeatedTest(10)
	final void testProcessRequestForGovt_FullApplication() throws IOException, Exception {
		String requestBody = mapper
				.writeValueAsString(GovernmentApplicationFormModelObjectMother.defaultApplication(true).build());

		mvc.perform(post("/api/onboard/publisher/govt").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated()).andExpect(handler().methodName("processRequestForGovt"))
				.andExpect(jsonPath("$.referenceNo",
						containsString("NAPIX/".concat(formatter.format(LocalDateTime.now()).toUpperCase()))));
	}

	@RepeatedTest(10)
	final void testProcessRequestForGovt_MinimumApplication() throws IOException, Exception {
		String requestBody = mapper
				.writeValueAsString(GovernmentApplicationFormModelObjectMother.defaultApplication(false).build());

		mvc.perform(post("/api/onboard/publisher/govt").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated()).andExpect(handler().methodName("processRequestForGovt"))
				.andExpect(jsonPath("$.referenceNo",
						containsString("NAPIX/".concat(formatter.format(LocalDateTime.now()).toUpperCase()))));
	}

	@RepeatedTest(10)
	final void testProcessRequestForOthers_FullApplication() throws IOException, Exception {
		String requestBody = mapper
				.writeValueAsString(OthersApplicationFormModelObjectMother.defaultApplication(true).build());

		mvc.perform(post("/api/onboard/publisher/other").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated()).andExpect(handler().methodName("processRequestForOthers"))
				.andExpect(jsonPath("$.referenceNo",
						containsString("NAPIX/".concat(formatter.format(LocalDateTime.now()).toUpperCase()))));
	}

	@RepeatedTest(10)
	final void testProcessRequestForOthers_MinimumApplication() throws IOException, Exception {
		String requestBody = mapper
				.writeValueAsString(OthersApplicationFormModelObjectMother.defaultApplication(false).build());

		mvc.perform(post("/api/onboard/publisher/other").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated()).andExpect(handler().methodName("processRequestForOthers"))
				.andExpect(jsonPath("$.referenceNo",
						containsString("NAPIX/".concat(formatter.format(LocalDateTime.now()).toUpperCase()))));
	}

	@Test
	final void testCanDomainNameBeUsed() throws Exception {
		mvc.perform(get("/api/onboard/verify/does-not-exist").param("domain-name", "freeapi.nic.in"))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(handler().methodName("canDomainNameBeUsed"))
				.andExpect(jsonPath("$.errors", hasItem(containsString("Domain 'freeapi.nic.in' already exist!"))));
	}

}
