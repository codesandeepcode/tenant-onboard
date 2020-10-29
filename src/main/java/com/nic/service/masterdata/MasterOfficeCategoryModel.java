package com.nic.service.masterdata;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

class MasterOfficeCategoryModel implements RowMapper<MasterOfficeCategoryModel> {

	private Integer id;

	private String name;

	public MasterOfficeCategoryModel() {
		super();
	}

	public MasterOfficeCategoryModel(ResultSet rs) throws SQLException {
		this.id = rs.getInt(1);
		this.name = rs.getString(2);
	}

	@Override
	public MasterOfficeCategoryModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new MasterOfficeCategoryModel(rs);
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
