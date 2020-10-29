package com.nic.service.onboarding.publisher;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

class OthersApplicationFormModel {

	static class Office {

		private String name;

		private String state;

		private String address;

		public String getName() {
			return name;
		}

		public String getState() {
			return state;
		}

		public String getAddress() {
			return address;
		}

	}

	static class ProjectHead {

		private String code;

		private String name;

		private String designation;

		private String emailId;

		private String mobileNo;

		private String landlineNo;

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getDesignation() {
			return designation;
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

	}

	private Office office;

	private ProjectHead projectHead;

	@JsonProperty("technicalHead")
	private List<TechnicalHeadDetailsModel> technicalHeadList;

	private String domainName;

	private String projectName;

	public Office getOffice() {
		return office;
	}

	public ProjectHead getProjectHead() {
		return projectHead;
	}

	public List<TechnicalHeadDetailsModel> getTechnicalHeadList() {
		return technicalHeadList;
	}

	public String getDomainName() {
		return domainName;
	}

	public String getProjectName() {
		return projectName;
	}

}
