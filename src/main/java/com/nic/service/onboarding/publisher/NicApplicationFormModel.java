package com.nic.service.onboarding.publisher;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NicApplicationFormModel {

	public static class PersonalDetails {

		private String code;

		private String name;

		private String designation;

		private String address;

		private String department;

		private String emailId;

		private String mobileNo;

		private String landlineNo;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDesignation() {
			return designation;
		}

		public void setDesignation(String designation) {
			this.designation = designation;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public String getEmailId() {
			return emailId;
		}

		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}

		public String getMobileNo() {
			return mobileNo;
		}

		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}

		public String getLandlineNo() {
			return landlineNo;
		}

		public void setLandlineNo(String landlineNo) {
			this.landlineNo = landlineNo;
		}

	}

	@JsonProperty("groupName")
	private String group;

	private PersonalDetails hog;

	private PersonalDetails hod;

	private PersonalDetails techAdmin1;

	private PersonalDetails techAdmin2;

	@JsonProperty("domainName")
	private String domain;

	@JsonProperty("projectName")
	private String project;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public PersonalDetails getHog() {
		return hog;
	}

	public void setHog(PersonalDetails hog) {
		this.hog = hog;
	}

	public PersonalDetails getHod() {
		return hod;
	}

	public void setHod(PersonalDetails hod) {
		this.hod = hod;
	}

	public PersonalDetails getTechAdmin1() {
		return techAdmin1;
	}

	public void setTechAdmin1(PersonalDetails techAdmin1) {
		this.techAdmin1 = techAdmin1;
	}

	public PersonalDetails getTechAdmin2() {
		return techAdmin2;
	}

	public void setTechAdmin2(PersonalDetails techAdmin2) {
		this.techAdmin2 = techAdmin2;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

}
