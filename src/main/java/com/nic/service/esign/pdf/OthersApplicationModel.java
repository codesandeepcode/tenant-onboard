package com.nic.service.esign.pdf;

import java.util.List;

public class OthersApplicationModel {

	public static class Office {

		private String name;

		private String state;

		private String address;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

	}

	public static class ProjectHead {

		private String code;

		private String name;

		private String designation;

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

	public static class TechnicalHead {

		private String code;

		private String name;

		private String designation;

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

	private Office office;

	private ProjectHead projectHead;

	private List<TechnicalHead> technicalHead;

	private String domainName;

	private String projectName;

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public ProjectHead getProjectHead() {
		return projectHead;
	}

	public void setProjectHead(ProjectHead projectHead) {
		this.projectHead = projectHead;
	}

	public List<TechnicalHead> getTechnicalHead() {
		return technicalHead;
	}

	public void setTechnicalHead(List<TechnicalHead> technicalHead) {
		this.technicalHead = technicalHead;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
