package com.nic.service.employeedetails;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RestApiController.class)
class RestApiControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private EmployeeDetailsService service;

	@Test
	void testFetchListOfNicGroupsWithHogEmployeeCode_ValidEmployeeCode() throws Exception {
		when(service.getGroupList(1104)).thenReturn(EmployeeDetailObjectMother.getGroupList());

		mvc.perform(get("/api/nic/1104/groups"))
				.andExpect(handler().methodName("fetchListOfNicGroupsWithHogEmployeeCode")).andExpect(status().isOk())
				.andExpect(jsonPath("$.[?(@.id=='Dopt And Doppw')].name", hasItem("Dopt And Doppw")))
				.andExpect(jsonPath("$.[*]", hasSize(6)));
		verify(service).getGroupList(1104);
	}

	@Test
	void testFetchNicGroupWithHoGEmployeeCode_InvalidEmployeeCode() throws Exception {
		when(service.getGroupList(2345)).thenThrow(new IllegalArgumentException("Invalid code provided"));

		mvc.perform(get("/api/nic/2345/groups"))
				.andExpect(handler().methodName("fetchListOfNicGroupsWithHogEmployeeCode"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors", hasItem("Invalid code provided")));
		verify(service).getGroupList(2345);
	}

	@Test
	void testFetchNicGroupWithHoGEmployeeCode_WrongInputCode() throws Exception {
		mvc.perform(get("/api/nic/234aa5/groups"))
				.andExpect(handler().methodName("fetchListOfNicGroupsWithHogEmployeeCode"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem("Employee Code should be of number type")));
		verify(service, times(0)).getGroupList(anyInt());
	}

	@Test
	void testFetchNicGroupWithHoGEmployeeCode_ValidParams() throws Exception {
		when(service.getGroupList(1104)).thenReturn(EmployeeDetailObjectMother.getGroupList());

		mvc.perform(get("/api/nic/1104/groups/Cem Kochi"))
				.andExpect(handler().methodName("fetchNicGroupWithHoGEmployeeCode")).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(equalTo("Cem Kochi"))));
		verify(service).getGroupList(1104);
	}

	@Test
	void testFetchNicGroupWithHoGEmployeeCode_InvalidEmployeeCodeAndGroupId() throws Exception {
		when(service.getGroupList(12321)).thenThrow(new IllegalArgumentException("Invalid employee code!"));

		mvc.perform(get("/api/nic/12321/groups/test"))
				.andExpect(handler().methodName("fetchNicGroupWithHoGEmployeeCode")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem("Invalid employee code!")));
	}

	@Test
	void testFetchNicGroupWithHoGEmployeeCode_ValidEmployeeIdInvalidGroupId() throws Exception {
		when(service.getGroupList(4561)).thenReturn(EmployeeDetailObjectMother.getGroupList());

		mvc.perform(get("/api/nic/4561/groups/check test"))
				.andExpect(handler().methodName("fetchNicGroupWithHoGEmployeeCode")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors", hasItem("Cannot find Group Name for given Group Id")));
		verify(service).getGroupList(4561);
	}

	@Test
	void testFetchNicGroupWithHoGEmployeeCode_InvalidDatatypeEmployeeIdInvalidGroupId() throws Exception {
		mvc.perform(get("/api/nic/456as23/groups/check test"))
				.andExpect(handler().methodName("fetchNicGroupWithHoGEmployeeCode")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem("Employee Code should be of number type")));
	}

	@Test
	void testFetchNicEmployeeDetails_ValidEmployeeCodeValidType() throws Exception {
		EmployeeDetail hog = EmployeeDetailObjectMother.aHogUser().build();
		when(service.getEmployeeDetails(hog.getEmployeeCode(), "hog")).thenReturn(hog);

		mvc.perform(get("/api/nic/" + hog.getEmployeeCode() + "/personaldetails").queryParam("type", "hog"))
				.andExpect(handler().methodName("fetchNicEmployeeDetails")).andExpect(status().isOk())
				.andExpect(jsonPath("$.emailId", is(equalTo(hog.getEmailId()))))
				.andExpect(jsonPath("$.officeAddress", is(equalTo(hog.getOfficeAddress()))));
		verify(service).getEmployeeDetails(hog.getEmployeeCode(), "hog");
	}

	@Test
	void testFetchNicEmployeeDetails_ValidEmployeeCodeWrongType() throws Exception {
		EmployeeDetail hog = EmployeeDetailObjectMother.aHogUser().build();
		when(service.getEmployeeDetails(hog.getEmployeeCode(), "hod"))
				.thenThrow(new IllegalArgumentException("Id belong to HOG group"));

		mvc.perform(get("/api/nic/" + hog.getEmployeeCode() + "/personaldetails").queryParam("type", "hod"))
				.andExpect(handler().methodName("fetchNicEmployeeDetails")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem("Id belong to HOG group")));
	}

	@Test
	void testFetchNicEmployeeDetails_ValidEmployeeCodeNoGivenType() throws Exception {
		mvc.perform(get("/api/nic/1234/personaldetails")).andExpect(handler().methodName("fetchNicEmployeeDetails"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testFetchNicEmployeeDetails_InvalidEmployeeCodeValidType() throws Exception {
		EmployeeDetail hog = EmployeeDetailObjectMother.aHogUser().build();
		when(service.getEmployeeDetails(hog.getEmployeeCode(), "hog"))
				.thenThrow(new IllegalArgumentException("Invalid employee code given"));

		mvc.perform(get("/api/nic/" + hog.getEmployeeCode() + "/personaldetails").param("type", "hog"))
				.andExpect(handler().methodName("fetchNicEmployeeDetails"))
				.andExpect(jsonPath("$.errors", hasItem("Invalid employee code given")));
		verify(service).getEmployeeDetails(hog.getEmployeeCode(), "hog");
	}

	@Test
	void testFetchNicEmployeeDetails_InvalidEmployeeCodeDataTypeValidType() throws Exception {
		mvc.perform(get("/api/nic/235we23/personaldetails").param("type", "hog")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem("Employee Code should be of number type")));
		verify(service, times(0)).getEmployeeDetails(anyInt(), anyString());
	}

	@Test
	void testFetchNicEmployeeDetails_ValidEmployeeCodeInvalidType() throws Exception {
		mvc.perform(get("/api/nic/235we23/personaldetails").param("type", "test")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors",
						hasItem("Request 'type' should be either 'hog', 'hod', 'other-one' or 'other-two'")));
	}

}
