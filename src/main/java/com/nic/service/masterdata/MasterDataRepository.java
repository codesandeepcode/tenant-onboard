package com.nic.service.masterdata;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class MasterDataRepository {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final JdbcTemplate jdbcTemplate;

	MasterDataRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<MasterStateModel> fetchMasterStateList() {
		log.debug("Returning master data for State");

		String sql = "SELECT state_code, state_name_en FROM master_state";
		return jdbcTemplate.query(sql, new MasterStateModel());
	}

	public List<MasterOfficeCategoryModel> fetchMasterOfficeCategoryList() {
		log.debug("Returning master data for Office category");

		String sql = "SELECT id, name FROM master_office_category";
		return jdbcTemplate.query(sql, new MasterOfficeCategoryModel());
	}

	public Optional<List<MasterDepartmentModel>> fetchMasterDepartmentList(String type) {
		log.debug("Returning master data from Department for type {}", type);

		String sql;
		if (StringUtils.equalsIgnoreCase(type, "central")) {
			sql = "SELECT id, name FROM master_department WHERE central=TRUE ORDER BY 1";
		} else if (StringUtils.equalsIgnoreCase(type, "state")) {
			sql = "SELECT id, name FROM master_department WHERE state=TRUE ORDER BY 1";
		} else {
			return Optional.empty();
		}

		return Optional.of(jdbcTemplate.query(sql, new MasterDepartmentModel()));
	}

}
