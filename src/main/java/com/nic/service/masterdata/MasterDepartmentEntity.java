package com.nic.service.masterdata;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "master_department")
public class MasterDepartmentEntity {

	@Id
	private Integer id;

	private String name;

	private Boolean central;

	private Boolean state;

	private Short active;

	@SuppressWarnings("unused")
	private MasterDepartmentEntity() {
		super();
	}

	public MasterDepartmentEntity(Integer departmentId) {
		this.id = departmentId;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Boolean getCentral() {
		return central;
	}

	public Boolean getState() {
		return state;
	}

	public Short getActive() {
		return active;
	}

}
