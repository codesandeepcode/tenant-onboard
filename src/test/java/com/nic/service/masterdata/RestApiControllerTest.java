package com.nic.service.masterdata;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({ RestApiController.class })
class RestApiControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MasterDataRepository repository;

	private void loadForMasterState() {
		List<MasterStateModel> stateList = new ArrayList<>(3);

		MasterStateModel state1 = new MasterStateModel();
		ReflectionTestUtils.setField(state1, "code", "01");
		ReflectionTestUtils.setField(state1, "name", "Kerala");

		MasterStateModel state2 = new MasterStateModel();
		ReflectionTestUtils.setField(state2, "code", "02");
		ReflectionTestUtils.setField(state2, "name", "Uttar Pradesh");

		MasterStateModel state3 = new MasterStateModel();
		ReflectionTestUtils.setField(state3, "code", "02");
		ReflectionTestUtils.setField(state3, "name", "Uttar Pradesh");

		stateList.add(state1);
		stateList.add(state2);
		stateList.add(state3);

		when(repository.fetchMasterStateList()).thenReturn(stateList);
	}

	@Test
	void testGetMasterStateList_DataProvidedInRepository() throws Exception {
		loadForMasterState();

		mvc.perform(get("/api/master/states")).andExpect(handler().methodName("getMasterStateList"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[?(@.code == '02')].name", hasItem("Uttar Pradesh")));
	}

	@Test
	void testGetMasterStateList_DataNotProvidedInRepository() throws Exception {
		when(repository.fetchMasterStateList()).thenReturn(new ArrayList<>());

		mvc.perform(get("/api/master/states")).andExpect(handler().methodName("getMasterStateList"))
				.andExpect(status().isOk()).andExpect(content().string("[]"));
	}

	@Test
	void testGetMasterStateList_ExactCountMatch() throws Exception {
		loadForMasterState();

		mvc.perform(get("/api/master/states")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$[*]", hasSize(3)));
	}

	@Test
	void testGetMasterStateList_ExceptionCausedInRepository() throws Exception {
		when(repository.fetchMasterStateList()).thenThrow(RuntimeException.class);

		mvc.perform(get("/api/master/states")).andExpect(status().isInternalServerError()).andExpect(
				jsonPath("$.errors", hasItem(containsStringIgnoringCase("Internal server exception occured"))));
	}

	@Test
	void testGetMasterStateWithGivenId_CorrectIdProvided() throws Exception {
		loadForMasterState();

		mvc.perform(get("/api/master/states/02")).andExpect(status().isOk())
				.andExpect(handler().methodName("getMasterStateWithGivenId"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", equalTo("Uttar Pradesh")));
	}

	@Test
	void testGetMasterStateWithGivenId_WrongIdProvided() throws Exception {
		mvc.perform(get("/api/master/states/10")).andExpect(status().isNotFound())
				.andExpect(handler().methodName("getMasterStateWithGivenId")).andExpect(jsonPath("$.errors",
						hasItem(containsStringIgnoringCase("Cannot find master state for given id - 10"))));
	}

	@Test
	void testGetMasterStateWithGivenId_InvalidIdProvided() throws Exception {
		mvc.perform(get("/api/master/states/10we22")).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errors", hasItem(containsStringIgnoringCase("Illegal data provided"))));
	}

	private void loadForOfficeCategory() {
		List<MasterOfficeCategoryModel> officeList = new ArrayList<>(2);

		MasterOfficeCategoryModel model1 = new MasterOfficeCategoryModel();
		ReflectionTestUtils.setField(model1, "id", 3);
		ReflectionTestUtils.setField(model1, "name", "State Office");

		MasterOfficeCategoryModel model2 = new MasterOfficeCategoryModel();
		ReflectionTestUtils.setField(model2, "id", 5);
		ReflectionTestUtils.setField(model2, "name", "Coal company");

		officeList.add(model1);
		officeList.add(model2);

		when(repository.fetchMasterOfficeCategoryList()).thenReturn(officeList);
	}

	@Test
	void testGetMasterOfficeCategoryList_DataProvidedInRepository() throws Exception {
		loadForOfficeCategory();

		mvc.perform(get("/api/master/officecategories")).andExpect(status().isOk())
				.andExpect(handler().methodName("getMasterOfficeCategoryList"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[?(@.id == 5)].name", hasItem("Coal company")));
	}

	@Test
	void testGetMasterOfficeCategoryList_DataNotProvidedInRepository() throws Exception {
		when(repository.fetchMasterOfficeCategoryList()).thenReturn(new ArrayList<>());

		mvc.perform(get("/api/master/officecategories")).andExpect(status().isOk())
				.andExpect(handler().methodName("getMasterOfficeCategoryList"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("[]"));
	}

	@Test
	void testGetMasterOfficeCategoryList_ExactCountMatch() throws Exception {
		loadForOfficeCategory();

		mvc.perform(get("/api/master/officecategories")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$[*]", hasSize(2)))
				.andExpect(handler().methodName("getMasterOfficeCategoryList"));
	}

	@Test
	void testGetMasterOfficeCategoryList_ExceptionThrownInRepository() throws Exception {
		when(repository.fetchMasterOfficeCategoryList()).thenThrow(new DataAccessException("Test exception") {
			private static final long serialVersionUID = 1L;
		});

		mvc.perform(get("/api/master/officecategories")).andExpect(status().isInternalServerError()).andExpect(
				jsonPath("$.errors", hasItem(containsStringIgnoringCase("Internal server exception occured"))));
	}

	@Test
	void testGetMasterOfficeCategory_GivenCorrectId() throws Exception {
		loadForOfficeCategory();

		mvc.perform(get("/api/master/officecategories/3")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", equalTo("State Office")))
				.andExpect(handler().methodName("getMasterOfficeCategoryWithGivenId"));
	}

	@Test
	void testGetMasterOfficeCategory_GivenWrongId() throws Exception {
		loadForOfficeCategory();

		mvc.perform(get("/api/master/officecategories/13")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors",
						hasItem(containsStringIgnoringCase("Cannot find master Office category for given id -13"))))
				.andExpect(handler().methodName("getMasterOfficeCategoryWithGivenId"));
	}

	@Test
	void testGetMasterOfficeCategory_InvalidWrongId() throws Exception {
		mvc.perform(get("/api/master/officecategories/1we21a")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem(containsStringIgnoringCase("Illegal data provided"))));
	}

	private void loadApiDataDepartmentList(String governmentType) {
		List<MasterDepartmentModel> list = new ArrayList<>(3);

		MasterDepartmentModel model1 = new MasterDepartmentModel();
		ReflectionTestUtils.setField(model1, "id", 4);
		ReflectionTestUtils.setField(model1, "name", "Central Dept");

		MasterDepartmentModel model2 = new MasterDepartmentModel();
		ReflectionTestUtils.setField(model2, "id", 10);
		ReflectionTestUtils.setField(model2, "name", "State Dept");

		MasterDepartmentModel model3 = new MasterDepartmentModel();
		ReflectionTestUtils.setField(model3, "id", 15);
		ReflectionTestUtils.setField(model3, "name", "Viillage Dept");

		list.add(model1);
		list.add(model2);
		list.add(model3);

		when(repository.fetchMasterDepartmentList(governmentType)).thenReturn(Optional.of(list));
	}

	@Test
	void testGetMasterDepartmentList_NoGovernmentTypeProvided() throws Exception {
		mvc.perform(get("/api/master/departments")).andExpect(status().isNotFound());
	}

	@Test
	void testGetMasterDepartmentList_CentralGovernmentTypeProvided() throws Exception {
		loadApiDataDepartmentList("central");

		mvc.perform(get("/api/master/departments/central")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(handler().methodName("getMasterDepartmentList"))
				.andExpect(jsonPath("$[?(@.id == 10)].name", hasItem("State Dept")))
				.andExpect(jsonPath("$.[*]", hasSize(3)));
	}

	@Test
	void testGetMasterDepartmentList_WrongGovernmentTypeProvided() throws Exception {
		mvc.perform(get("/api/master/departments/statess")).andExpect(status().isBadRequest())
				.andExpect(handler().methodName("getMasterDepartmentList")).andExpect(jsonPath("$.errors",
						hasItem(containsStringIgnoringCase("Only 'central' or 'state' value are allowed!"))));
	}

	@Test
	void testGetMasterDepartmentList_WrongGovernmentTypeProvided_EdgeCase() throws Exception {
		mvc.perform(get("/api/master/departments/centrals")).andExpect(status().isBadRequest())
				.andExpect(handler().methodName("getMasterDepartmentList")).andExpect(jsonPath("$.errors",
						hasItem(containsStringIgnoringCase("Only 'central' or 'state' value are allowed!"))));
	}

	void testGetMasterDepartmentList_StateGovernmentTypeProvided() throws Exception {
		loadApiDataDepartmentList("state");

		mvc.perform(get("/api/master/departments/state")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[?(@.id == 15)].name", hasItem("Viillage Dept")))
				.andExpect(jsonPath("$.[*]", hasSize(3)));
	}

	@Test
	void testGetMasterDepartmentWithGivenId_CentralGovernmentTypeProvidedValidId() throws Exception {
		loadApiDataDepartmentList("central");

		mvc.perform(get("/api/master/departments/central/10")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", equalTo("State Dept")))
				.andExpect(handler().methodName("getMasterDepartmentWithGivenId"));
	}

	@Test
	void testGetMasterDepartmentWithGivenId_StateGovernmentTypeProvidedValidId() throws Exception {
		loadApiDataDepartmentList("state");

		mvc.perform(get("/api/master/departments/state/10")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", equalTo("State Dept")))
				.andExpect(handler().methodName("getMasterDepartmentWithGivenId"));
	}

	@Test
	void testGetMasterDepartmentWithGivenId_InvalidGovernmentTypeProvidedValidId() throws Exception {
		mvc.perform(get("/api/master/departments/centralss/10")).andExpect(status().isBadRequest())
				.andExpect(handler().methodName("getMasterDepartmentWithGivenId")).andExpect(jsonPath("$.errors",
						hasItem(containsStringIgnoringCase("Only 'central' or 'state' value are allowed"))));
	}

	@Test
	void testGetMasterDepartmentWithGivenId_CentralGovernmentTypeProvidedInvalidId() throws Exception {
		loadApiDataDepartmentList("central");

		mvc.perform(get("/api/master/departments/central/12")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors",
						hasItem(containsStringIgnoringCase("Cannot find master Department type for given id - 12"))));
	}

	@Test
	void testGetMasterDepartmentWithGivenId_CentralGovernmentTypeIncorrectTypeId() throws Exception {
		mvc.perform(get("/api/master/departments/central/12we23")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors",
						hasItem(containsStringIgnoringCase("Illegal data provided! Should be integer only!"))));
	}

}
