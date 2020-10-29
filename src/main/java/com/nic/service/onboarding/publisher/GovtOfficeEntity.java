package com.nic.service.onboarding.publisher;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.nic.service.masterdata.MasterDepartmentEntity;
import com.nic.service.masterdata.MasterGovernmentTypeEntity;
import com.nic.service.masterdata.MasterOfficeCategoryEntity;
import com.nic.service.masterdata.MasterStateEntity;

@Entity
@Table(name = "govt_office_details")
class GovtOfficeEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5591838315177385824L;

	@Id
	@OneToOne
	@JoinColumn(name = "applicationId")
	private PublisherApplicationEntity application;

	@OneToOne
	@JoinColumn(name = "govtTypeId")
	private MasterGovernmentTypeEntity govtType;

	@OneToOne
	@JoinColumn(name = "departmentId")
	private MasterDepartmentEntity department;

	@OneToOne
	@JoinColumn(name = "stateId")
	private MasterStateEntity state;

	@OneToOne
	@JoinColumn(name = "officeCategoryId")
	private MasterOfficeCategoryEntity officeCategory;

	private String officeName;

	private String officeAddress;

	private String groupName;

	private Short active;

	public PublisherApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(PublisherApplicationEntity application) {
		this.application = application;
	}

	public MasterGovernmentTypeEntity getGovtType() {
		return govtType;
	}

	public void setGovtType(MasterGovernmentTypeEntity govtType) {
		this.govtType = govtType;
	}

	public MasterDepartmentEntity getDepartment() {
		return department;
	}

	public void setDepartment(MasterDepartmentEntity department) {
		this.department = department;
	}

	public MasterStateEntity getState() {
		return state;
	}

	public void setState(MasterStateEntity state) {
		this.state = state;
	}

	public MasterOfficeCategoryEntity getOfficeCategory() {
		return officeCategory;
	}

	public void setOfficeCategory(MasterOfficeCategoryEntity officeCategory) {
		this.officeCategory = officeCategory;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
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
