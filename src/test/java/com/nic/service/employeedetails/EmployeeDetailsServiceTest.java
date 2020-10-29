package com.nic.service.employeedetails;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeDetailsServiceTest {

	@Mock
	private ExternalApiServiceImpl apiService;

	@InjectMocks
	private EmployeeDetailsService service;

	@Test
	void testGetEmployeeDetailsValidHogCode() {
		EmployeeDetail hog = EmployeeDetailObjectMother.aHogUser().build();
		when(apiService.fetchEmployeeDetails(hog.getEmployeeCode())).thenReturn(hog);

		assertTrue(service.getEmployeeDetails(hog.getEmployeeCode(), "hog").getEmployeeName()
				.equals(hog.getEmployeeName()));
		verify(apiService, times(1)).fetchEmployeeDetails(hog.getEmployeeCode());
	}

	@Test
	void testGetEmployeeDetailsInvalidHogCode() {
		EmployeeDetail hod = EmployeeDetailObjectMother.aHodUser().build();
		when(apiService.fetchEmployeeDetails(hod.getEmployeeCode())).thenReturn(hod);

		assertThrows(IllegalArgumentException.class, () -> service.getEmployeeDetails(hod.getEmployeeCode(), "hog"));
		verify(apiService, times(1)).fetchEmployeeDetails(hod.getEmployeeCode());
	}

	@Test
	void testGetEmployeeDetailsValidHodCode() {
		EmployeeDetail hod = EmployeeDetailObjectMother.aHodUser().build();
		when(apiService.fetchEmployeeDetails(hod.getEmployeeCode())).thenReturn(hod);

		assertTrue(service.getEmployeeDetails(hod.getEmployeeCode(), "hod").getEmployeeName()
				.equals(hod.getEmployeeName()));
		verify(apiService, times(1)).fetchEmployeeDetails(hod.getEmployeeCode());
	}

	@Test
	void testGetEmployeeDetailsInvalidOthersCode() {
		EmployeeDetail others = EmployeeDetailObjectMother.aTechUser().build();
		when(apiService.fetchEmployeeDetails(others.getEmployeeCode())).thenReturn(others);

		assertThrows(IllegalArgumentException.class, () -> service.getEmployeeDetails(others.getEmployeeCode(), "hog"));
		verify(apiService).fetchEmployeeDetails(others.getEmployeeCode());
	}

	@Test
	void testGetEmployeeDetailsValidOthersCode() {
		EmployeeDetail others = EmployeeDetailObjectMother.aTechUser().build();
		when(apiService.fetchEmployeeDetails(others.getEmployeeCode())).thenReturn(others);

		assertTrue(service.getEmployeeDetails(others.getEmployeeCode(), "other-one").getOfficeAddress()
				.equals(others.getOfficeAddress()));
	}

	@Test
	void testGetEmployeeDetailsInvalidType() {
		EmployeeDetail others = EmployeeDetailObjectMother.aTechUser().build();
		when(apiService.fetchEmployeeDetails(others.getEmployeeCode())).thenReturn(others);

		assertThrows(IllegalArgumentException.class,
				() -> service.getEmployeeDetails(others.getEmployeeCode(), "nothing"));
	}

	@Test
	void testGetEmployeeDetails_WrongEmployeeCodeValidType() {
		int code = 1024;
		EmployeeDetail invalid = EmployeeDetailObjectMother.builder().build();
		when(apiService.fetchEmployeeDetails(code)).thenReturn(invalid);

		assertThrows(IllegalArgumentException.class, () -> service.getEmployeeDetails(code, "hog"));
	}

	@Test
	void testGetEmployeeDetailsInvalidParams() {
		assertThrows(IllegalArgumentException.class, () -> service.getEmployeeDetails(3214, null));
		verify(apiService, times(0)).fetchEmployeeDetails(Mockito.anyInt());
	}

	@Test
	void testGetEmployeeDetails_ReturnedEmployeeDetailsIsNull() {
		when(apiService.fetchEmployeeDetails(2343)).thenReturn(null);

		Exception ex = assertThrows(IllegalArgumentException.class, () -> service.getEmployeeDetails(2343, "hog"));
		assertTrue(ex.getMessage().equals("Invalid employee code!"));
	}

	@Test
	void testGetGroupList_InvalidCode() {
		EmployeeDetail hod = EmployeeDetailObjectMother.aHodUser().build();
		when(apiService.fetchEmployeeDetails(hod.getEmployeeCode())).thenReturn(hod);

		assertThrows(IllegalArgumentException.class, () -> service.getGroupList(hod.getEmployeeCode()));
	}

	@Test
	void testGetGroupList_ValidCode() {
		EmployeeDetail hog = EmployeeDetailObjectMother.aHogUser().build();
		when(apiService.fetchEmployeeDetails(hog.getEmployeeCode())).thenReturn(hog);

		assertTrue(service.getGroupList(hog.getEmployeeCode()).size() == 6);
		verify(apiService).fetchEmployeeDetails(hog.getEmployeeCode());
	}

	@Test
	void testGetGroupList_NoParam() {
		assertThrows(IllegalArgumentException.class, () -> service.getGroupList(null));
	}

}
