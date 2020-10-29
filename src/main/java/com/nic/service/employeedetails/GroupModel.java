package com.nic.service.employeedetails;

import java.util.ArrayList;
import java.util.List;

class GroupModel {

	private String id;

	private String name;

	public GroupModel(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public static List<GroupModel> list(String[] groups) {
		List<GroupModel> list = new ArrayList<>(groups.length);
		for (String group : groups) {
			list.add(new GroupModel(group, group));
		}

		return list;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
