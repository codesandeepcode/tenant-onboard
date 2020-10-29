package com.nic.service.utils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GovernmentType {

	CENTRAL(1, "central", "Central"), STATE(2, "state", "State");

	private short id;

	private String label;

	private String name;

	private GovernmentType(int id, String label, String name) {
		this.id = (short) id;
		this.label = label;
		this.name = name;
	}

	public short getId() {
		return id;
	}

	public static GovernmentType getById(short id) {
		for (GovernmentType g : values()) {
			if (g.getId() == id)
				return g;
		}

		throw new IllegalArgumentException("Invalid id provided - " + id);
	}

	@JsonValue
	public String getLabel() {
		return label;
	}

	public String getName() {
		return name;
	}

}
