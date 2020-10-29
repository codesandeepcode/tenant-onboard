package com.nic.service.employeedetails;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.nic.service.SpringBootTestWrapper;

class IntegrationTest extends SpringBootTestWrapper {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testFetchNicEmployeeDetails_ValidHog() throws Exception {
		this.mockMvc.perform(get("/api/nic/1104/personaldetails").param("type", "hog"))
				.andExpect(handler().methodName("fetchNicEmployeeDetails")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.employeeCode", is(equalTo(1104))));
	}

	@Test
	void testFetchNicEmployeeDetails_InvalidHog() throws Exception {
		this.mockMvc.perform(get("/api/nic/3629/personaldetails").param("type", "hog"))
				.andExpect(handler().methodName("fetchNicEmployeeDetails")).andExpect(status().isBadRequest());
	}

}
