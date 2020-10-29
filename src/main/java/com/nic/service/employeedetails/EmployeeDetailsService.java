package com.nic.service.employeedetails;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
class EmployeeDetailsService {

	private final ExternalApiService apiService;

	EmployeeDetailsService(ExternalApiService apiService) {
		this.apiService = apiService;
	}

	// TODO: Consider replacing return type into Optional type (avoid need to throw
	// exception) .. how will you handle returning error messaging?
	public EmployeeDetail getEmployeeDetails(Integer employeeCode, String type) {
		if (employeeCode == null || StringUtils.isBlank(type))
			throw new IllegalArgumentException("Invalid parameters provided");

		EmployeeDetail empDetails = apiService.fetchEmployeeDetails(employeeCode);

		if (empDetails == null || empDetails.getEmployeeCode() == null)
			throw new IllegalArgumentException("Invalid employee code!");

		switch (type) {
		case "hog":
			if (!empDetails.getHogCode().equals(employeeCode))
				throw new IllegalArgumentException("Given Employee code does not belong to HoG category");

			break;
		case "hod":
			if (!empDetails.getHodCode().equals(employeeCode) || empDetails.getHogCode().equals(employeeCode))
				throw new IllegalArgumentException("Given Employee code does not belong to HoD category");

			break;
		case "other-one":
			if (empDetails.getHogCode().equals(employeeCode))
				throw new IllegalArgumentException("Employee code belong to HoG category");

			break;
		case "other-two":
			if (empDetails.getHogCode().equals(employeeCode))
				throw new IllegalArgumentException("Employee code belong to HoG category");

			if (empDetails.getHodCode().equals(employeeCode))
				throw new IllegalArgumentException("Employee code belong to Hod category");

			break;
		default:
			throw new IllegalArgumentException(
					"Invalid type provided - valid one is 'hog', 'hod', 'other-one', 'other-two'");
		}

		return empDetails;
	}

	public List<GroupModel> getGroupList(Integer employeeCode) {
		if (employeeCode == null)
			throw new IllegalArgumentException("Please provide valid employee code");

		EmployeeDetail empDetails = getEmployeeDetails(employeeCode, "hog");
		String[] splitted = StringUtils.split(empDetails.getPlaceOfPosting(), ",");
		return GroupModel.list(splitted);
	}

}
