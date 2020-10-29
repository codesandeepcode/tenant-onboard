package com.nic.service.utils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrganisationType {

	NIC("nic"), GOVT("government"), OTHERS("others");

	private String name;

	private OrganisationType(String name) {
		this.name = name;
	}

	@JsonValue
	public String getName() {
		return name;
	}

}
