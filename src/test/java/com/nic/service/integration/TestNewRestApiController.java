package com.nic.service.integration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.nic.service.SpringBootTestWrapper;

class TestNewRestApiController extends SpringBootTestWrapper {

	@Autowired
	private MockMvc mvc;

	@Test
	final void testFetchEmployeeDetailsForNicIncorrectParam() throws Exception {
		mvc.perform(get("/api/nic/kdsf3748/personaldetails")).andExpect(status().isBadRequest());
	}

	@Test
	final void testFetchEmployeeDetailsForNicValidParam() throws Exception {
		mvc.perform(get("/api/nic/1104/personaldetails").param("type", "hog")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json(
						"{\"employeeCode\":1104,\"employeeName\":\"Pawan Kumar Joshi\",\"mobileNo\":\"9818662315\",\"employeeDesignation\":\"Scientist-G\"}"));
	}

	@Test
	final void testGetMasterStateList() throws Exception {
		mvc.perform(get("/api/master/states")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[?(@.code == 19)].name", hasItem("WEST BENGAL")));
	}

	@Test
	final void testGetMasterOfficeCategoryList() throws Exception {
		mvc.perform(get("/api/master/officecategories")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$[?(@.id == 3)].name",
						hasItem("Ministries/Departments and Offices of the State Govt")));
	}

	@Test
	final void testGetMasterDepartmentListNoParam() throws Exception {
		mvc.perform(get("/api/master/departments")).andExpect(status().isNotFound());
	}

	@Test
	final void testGetMasterDepartmentListInvalidParam() throws Exception {
		mvc.perform(get("/api/master/departments/hfskdjf")).andExpect(status().isBadRequest());
	}

	@Test
	final void testGetMasterDepartmentListParamCentral() throws Exception {
		mvc.perform(get("/api/master/departments/central")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[?(@.id == 2)].name", hasItem("Department of Atomic Energy")));
	}

	@Test
	final void testGetMasterDepartmentListParamState() throws Exception {
		mvc.perform(get("/api/master/departments/state")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[?(@.id == 4)].name", hasItem("Ministry of Coal")));
	}

	@Test
	final void testGetMasterStateWithGivenIdInvalidParam() throws Exception {
		mvc.perform(get("/api/master/states/3746")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem(containsString("Illegal data provided!"))));
	}

	@Test
	final void testGetMasterStateWithGivenIdValidParam() throws Exception {
		mvc.perform(get("/api/master/states/09")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is(equalTo("UTTAR PRADESH"))));
	}

	@Test
	final void testGetMasterOfficeCategoryWithGivenIdInvalidParam() throws Exception {
		mvc.perform(get("/api/master/officecategories/7423")).andExpect(status().isNotFound());
	}

	@Test
	final void testGetMasterOfficeCategoryWithGivenIdInvalidParamWrongType() throws Exception {
		mvc.perform(get("/api/master/officecategories/7423das79")).andExpect(status().isBadRequest());
	}

	@Test
	final void testGetMasterOfficeCategoryWithGivenIdValidParam() throws Exception {
		mvc.perform(get("/api/master/officecategories/3")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is(equalTo("Ministries/Departments and Offices of the State Govt"))));
	}

	@Test
	final void testGetMasterDepartmentWithGivenIdInvalidParam() throws Exception {
		mvc.perform(get("/api/master/departments/central/3847")).andExpect(status().isNotFound());
	}

	@Test
	final void testGetMasterDepartmentWithGivenIdInvalidParamWrongCategory() throws Exception {
		mvc.perform(get("/api/master/departments/centrals/3847")).andExpect(status().isBadRequest());
	}

	@Test
	final void testGetMasterDepartmentWithGivenIdInvalidParamWrongType() throws Exception {
		mvc.perform(get("/api/master/departments/central/38jshd263")).andExpect(status().isBadRequest());
	}

	@Test
	final void testGetMasterDepartmentWithGivenIdValidParam() throws Exception {
		mvc.perform(get("/api/master/departments/state/4")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is(equalTo("Ministry of Coal"))));
	}

}
