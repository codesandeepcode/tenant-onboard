package com.nic.service.employeedetails;

interface ExternalApiService {

	/**
	 * Return EmployeeDetail model when given the employee code
	 */
	EmployeeDetail fetchEmployeeDetails(Integer employeeCode);

}