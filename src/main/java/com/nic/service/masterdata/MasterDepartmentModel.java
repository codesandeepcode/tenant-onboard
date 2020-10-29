package com.nic.service.masterdata;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

class MasterDepartmentModel implements RowMapper<MasterDepartmentModel> {

	private Integer id;

	private String name;

	public MasterDepartmentModel() {
		super();
	}

	private MasterDepartmentModel(ResultSet rs) throws SQLException {
		this.id = rs.getInt(1);
		this.name = rs.getString(2);
	}

	@Override
	public MasterDepartmentModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new MasterDepartmentModel(rs);
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
