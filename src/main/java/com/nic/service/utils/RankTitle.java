package com.nic.service.utils;

public enum RankTitle {

	HEAD_OF_GROUP(1), HEAD_OF_DIVISION(2), TECHNICAL_ADMIN(3), PROJECT_HEAD(4), TECHNICAL_HEAD(5);

	private Short id;

	private RankTitle(int id) {
		this.id = (short) id;
	}

	public Short getId() {
		return id;
	}

	public boolean equalTo(Integer id) {
		return this.id == id.shortValue();
	}

	public boolean equalTo(Short id) {
		return this.id == id.shortValue();
	}

}
