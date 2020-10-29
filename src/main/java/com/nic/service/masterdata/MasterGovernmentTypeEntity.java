package com.nic.service.masterdata;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nic.service.utils.GovernmentType;

@Entity
@Table(name = "master_govt_type")
public class MasterGovernmentTypeEntity {

	@Id
	private Short id;

	private String name;

	private Short active;

	@SuppressWarnings("unused")
	private MasterGovernmentTypeEntity() {
		super();
	}

	public MasterGovernmentTypeEntity(GovernmentType type) {
		id = type.getId();
	}

	public Short getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Short getActive() {
		return active;
	}

}
