package com.nic.service.masterdata;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "master_office_category")
public class MasterOfficeCategoryEntity {

	@Id
	private Integer id;

	private String name;

	private Short active;

	@SuppressWarnings("unused")
	private MasterOfficeCategoryEntity() {
		super();
	}

	public MasterOfficeCategoryEntity(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Short getActive() {
		return active;
	}

}
