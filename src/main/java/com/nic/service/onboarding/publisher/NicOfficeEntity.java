package com.nic.service.onboarding.publisher;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "nic_office_details")
class NicOfficeEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3570276755131116331L;

	@Id
	@OneToOne
	@JoinColumn(name = "applicationId")
	private PublisherApplicationEntity application;

	private String groupName;

	private Short active;

	public PublisherApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(PublisherApplicationEntity application) {
		this.application = application;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Short getActive() {
		return active;
	}

	public void setActive(Short active) {
		this.active = active;
	}

}
