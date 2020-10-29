package com.nic.service.onboarding.publisher;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "publisher_application_details")
class PublisherApplicationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer applicationId;

	private String applicationReferenceNo;

	private String domainName;

	private String projectName;

	private LocalDateTime creationDate;

	private String applicationRemarks;

	private Short applicationStatusId;

	private Short active;

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationReferenceNo() {
		return applicationReferenceNo;
	}

	public void setApplicationReferenceNo(String applicationReferenceNo) {
		this.applicationReferenceNo = applicationReferenceNo;
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

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getApplicationRemarks() {
		return applicationRemarks;
	}

	public void setApplicationRemarks(String applicationRemarks) {
		this.applicationRemarks = applicationRemarks;
	}

	public Short getApplicationStatusId() {
		return applicationStatusId;
	}

	public void setApplicationStatusId(Short applicationStatusId) {
		this.applicationStatusId = applicationStatusId;
	}

	public Short getActive() {
		return active;
	}

	public void setActive(Short active) {
		this.active = active;
	}

}
