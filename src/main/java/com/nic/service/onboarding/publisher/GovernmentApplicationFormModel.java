package com.nic.service.onboarding.publisher;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nic.service.utils.GovernmentType;

class GovernmentApplicationFormModel {

	static class Office {

		private String department;

		private String state;

		private String category;

		private String name;

		private String address;

		public String getDepartment() {
			return department;
		}

		public String getState() {
			return state;
		}

		public String getCategory() {
			return category;
		}

		public String getName() {
			return name;
		}

		public String getAddress() {
			return address;
		}

	}

	static class ProjectHead {

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

		public String getName() {
			return name;
		}

		public String getDesignation() {
			return designation;
		}

		public String getGroup() {
			return group;
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

	@JsonProperty("govtType")
	private GovernmentType governmentType;

	private Office office;

	private ProjectHead projectHead;

	@JsonProperty("technicalHead")
	private List<TechnicalHeadDetailsModel> technicalHeadList;

	private String domainName;

	private String projectName;

	public GovernmentType getGovernmentType() {
		return governmentType;
	}

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
