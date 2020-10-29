package com.nic.service.onboarding.publisher;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.nic.service.masterdata.MasterStateEntity;

@Entity
@Table(name = "others_office_details")
class OthersOfficeEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8157005168310151802L;

	@Id
	@OneToOne
	@JoinColumn(name = "applicationId")
	private PublisherApplicationEntity application;

	@OneToOne
	@JoinColumn(name = "stateId")
	private MasterStateEntity state;

	private String companyName;

	private String companyAddress;

	private Short active;

	public PublisherApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(PublisherApplicationEntity application) {
		this.application = application;
	}

	public MasterStateEntity getState() {
		return state;
	}

	public void setState(MasterStateEntity state) {
		this.state = state;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public Short getActive() {
		return active;
	}

	public void setActive(Short active) {
		this.active = active;
	}

}
