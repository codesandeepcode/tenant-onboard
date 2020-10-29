package com.nic.service.masterdata;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "master_state")
public class MasterStateEntity {

	@Id
	private String stateCode;

	private String stateNameEn;

	private String stateNameLl;

	private Short active;

	@SuppressWarnings("unused")
	private MasterStateEntity() {
		super();
	}

	public MasterStateEntity(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public String getStateNameEn() {
		return stateNameEn;
	}

	public String getStateNameLl() {
		return stateNameLl;
	}

	public Short getActive() {
		return active;
	}

}
