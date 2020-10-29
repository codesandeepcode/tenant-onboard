package com.nic.service.employeedetails;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "run.with.external-service-api", havingValue = "false")
class MockExternalApiServiceImpl implements ExternalApiService {

	private EmployeeDetail getHog() throws IllegalAccessException {
		EmployeeDetail emp = new EmployeeDetail();

		FieldUtils.writeDeclaredField(emp, "employeeCode", 1104, true);
		FieldUtils.writeDeclaredField(emp, "employeeTitle", "Shri", true);
		FieldUtils.writeDeclaredField(emp, "employeeName", "Pawan Kumar Joshi", true);
		FieldUtils.writeDeclaredField(emp, "employeeDesignation", "Scientist-G", true);
		FieldUtils.writeDeclaredField(emp, "emailId", "pawan.joshi@nic.in", true);
		FieldUtils.writeDeclaredField(emp, "mobileNo", "9818662315", true);
		FieldUtils.writeDeclaredField(emp, "landlineNo", "11-24305268", true);
		FieldUtils.writeDeclaredField(emp, "placeOfPosting", "UI-UX, SQG, Software Checker Group, Management Group",
				true);
		FieldUtils.writeDeclaredField(emp, "hogCode", 1104, true);
		FieldUtils.writeDeclaredField(emp, "hodCode", 1104, true);
		FieldUtils.writeDeclaredField(emp, "officeAddress", "NIC HQS, NEW DELHI, 110003", true);

		return emp;
	}

	private EmployeeDetail getHod() throws IllegalAccessException {
		EmployeeDetail emp = new EmployeeDetail();

		FieldUtils.writeDeclaredField(emp, "employeeCode", 3629, true);
		FieldUtils.writeDeclaredField(emp, "employeeTitle", "Shri", true);
		FieldUtils.writeDeclaredField(emp, "employeeName", "G. Mayil Muthu Kumaran", true);
		FieldUtils.writeDeclaredField(emp, "employeeDesignation", "Scientist-G", true);
		FieldUtils.writeDeclaredField(emp, "emailId", "muthu@nic.in", true);
		FieldUtils.writeDeclaredField(emp, "mobileNo", "9810119461", true);
		FieldUtils.writeDeclaredField(emp, "landlineNo", "11-24305748 ", true);
		FieldUtils.writeDeclaredField(emp, "placeOfPosting", "NIC", true);
		FieldUtils.writeDeclaredField(emp, "officeAddress", "A4B3, II, A, NIC, NEW, 110003", true);
		FieldUtils.writeDeclaredField(emp, "hogCode", 1104, true);
		FieldUtils.writeDeclaredField(emp, "hodCode", 3629, true);

		return emp;
	}

	private EmployeeDetail getTechAdmin1() throws IllegalAccessException {
		EmployeeDetail emp = new EmployeeDetail();

		FieldUtils.writeDeclaredField(emp, "employeeCode", 5516, true);
		FieldUtils.writeDeclaredField(emp, "employeeTitle", "Mrs", true);
		FieldUtils.writeDeclaredField(emp, "employeeName", "Pooja Singh", true);
		FieldUtils.writeDeclaredField(emp, "employeeDesignation", "Scientist-D", true);
		FieldUtils.writeDeclaredField(emp, "emailId", "singhpooja@nic.in", true);
		FieldUtils.writeDeclaredField(emp, "mobileNo", "9810273875", true);
		FieldUtils.writeDeclaredField(emp, "landlineNo", "11-24305294", true);
		FieldUtils.writeDeclaredField(emp, "placeOfPosting", "NIC", true);
		FieldUtils.writeDeclaredField(emp, "hogCode", 1104, true);
		FieldUtils.writeDeclaredField(emp, "hodCode", 3629, true);
		FieldUtils.writeDeclaredField(emp, "officeAddress", "A1B4, III, III, NIC HQS, , 110003", true);

		return emp;
	}

	private EmployeeDetail getTechAdmin2() throws IllegalAccessException {
		EmployeeDetail emp = new EmployeeDetail();

		FieldUtils.writeDeclaredField(emp, "employeeCode", 7102, true);
		FieldUtils.writeDeclaredField(emp, "employeeTitle", "Shri", true);
		FieldUtils.writeDeclaredField(emp, "employeeName", "Sandeep Yadav", true);
		FieldUtils.writeDeclaredField(emp, "employeeDesignation", "Scientist-B", true);
		FieldUtils.writeDeclaredField(emp, "emailId", "yadav.sandeep95@nic.in", true);
		FieldUtils.writeDeclaredField(emp, "mobileNo", "7891239272 ", true);
		FieldUtils.writeDeclaredField(emp, "placeOfPosting", "NIC", true);
		FieldUtils.writeDeclaredField(emp, "officeAddress", "SQG LAB, NIC HQ, 110003", true);
		FieldUtils.writeDeclaredField(emp, "hogCode", 1104, true);
		FieldUtils.writeDeclaredField(emp, "hodCode", 3629, true);

		return emp;
	}

	@Override
	public EmployeeDetail fetchEmployeeDetails(Integer employeeCode) {
		try {
			switch (employeeCode) {
			case 1104:
				return getHog();
			case 3629:
				return getHod();
			case 5516:
				return getTechAdmin1();
			case 7102:
				return getTechAdmin2();
			default:
				return null;
			}
		} catch (IllegalAccessException e) {
			return null;
		}
	}

}
