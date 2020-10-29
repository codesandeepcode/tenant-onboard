package com.nic.service.employeedetails;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Employees")
class EmployeeWrapper {

	@JacksonXmlProperty(localName = "Detail")
	private EmployeeDetail employeeDetails;

	public EmployeeDetail getEmployeeDetails() {
		return employeeDetails;
	}

}
