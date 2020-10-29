package com.nic.service.esign.pdf;

import java.util.List;

import com.nic.service.utils.GovernmentType;

public class GovtApplicationModel {

	public static class Office {

		private String department;

		private String state;

		private String category;

		private String name;

		private String address;

		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

		private String group;

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

		public String getGroup() {
			return group;
		}

		public void setGroup(String group) {
			this.group = group;
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

	private GovernmentType governmentType;

	private Office office;

	private ProjectHead projectHead;

	private List<TechnicalHead> technicalHeadList;

	private String domainName;

	private String projectName;

	public GovernmentType getGovernmentType() {
		return governmentType;
	}

	public void setGovernmentType(GovernmentType governmentType) {
		this.governmentType = governmentType;
	}

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

	public List<TechnicalHead> getTechnicalHeadList() {
		return technicalHeadList;
	}

	public void setTechnicalHeadList(List<TechnicalHead> technicalHeadList) {
		this.technicalHeadList = technicalHeadList;
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
