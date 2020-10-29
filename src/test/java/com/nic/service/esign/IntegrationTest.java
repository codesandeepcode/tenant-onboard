package com.nic.service.esign;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.nic.service.SpringBootTestWrapper;

class IntegrationTest extends SpringBootTestWrapper {

	@Autowired
	private MockMvc mvc;

	@Test
	final void testDownloadPdfApplicationForm() throws Exception {
		MvcResult mvcResult = mvc
				.perform(get("/services/download-application-form").param("application-number", "NAPIX/20115"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_PDF))
				.andExpect(header().string("Content-Disposition", containsString("Application Ref No NAPIX/20115.pdf")))
				.andReturn();

		assertTrue(mvcResult.getResponse().getContentLength() > 20);
	}

	// test for : signed=true , that is, when e-signed succeedded

}
