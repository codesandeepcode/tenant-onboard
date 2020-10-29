package com.nic.service.masterdata;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

class MasterStateModel implements RowMapper<MasterStateModel> {

	private String code;

	private String name;

	public MasterStateModel() {
		super();
	}

	public MasterStateModel(ResultSet rs) throws SQLException {
		this.code = rs.getString(1);
		this.name = rs.getString(2);
	}

	@Override
	public MasterStateModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new MasterStateModel(rs);
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

}
