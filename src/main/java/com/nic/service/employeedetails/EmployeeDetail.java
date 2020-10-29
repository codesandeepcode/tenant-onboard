package com.nic.service.employeedetails;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

class EmployeeDetail {

	@JacksonXmlProperty(localName = "emp_code")
	private Integer employeeCode;

	@JacksonXmlProperty(localName = "emp_title")
	private String employeeTitle;

	@JacksonXmlProperty(localName = "emp_name")
	private String employeeName;

	@JacksonXmlProperty(localName = "emp_desig")
	private String employeeDesignation;

	private String emailId;

	@JacksonXmlProperty(localName = "mobile")
	private String mobileNo;

	// not available in code
	@JacksonXmlProperty(localName = "contactno")
	private String landlineNo;

	private String placeOfPosting;

	private Integer hogCode;

	private Integer hodCode;

	@JacksonXmlProperty(localName = "officeaddress")
	private String officeAddress;

	public Integer getEmployeeCode() {
		return employeeCode;
	}

	public String getEmployeeTitle() {
		return employeeTitle;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public String getEmployeeDesignation() {
		return employeeDesignation;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public String getLandlineNo() {
		return landlineNo;
	}

	public String getPlaceOfPosting() {
		return placeOfPosting;
	}

	public Integer getHogCode() {
		return hogCode;
	}

	public Integer getHodCode() {
		return hodCode;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

}
