package com.nic.service.employeedetails;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.test.util.ReflectionTestUtils;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

public class EmployeeDetailObjectMother {

	private static Faker faker = new Faker(new Locale("en", "IND"));
	private static FakeValuesService fakerService = new FakeValuesService(new Locale("en", "IND"), new RandomService());

	public static EmployeeDetailObjectMother.Builder aHogUser() {
		Integer empCode = Integer.parseInt(fakerService.numerify("1###"));

		return builder().withEmployeeCode(empCode).withEmployeeTitle(faker.name().prefix())
				.withEmployeeName(faker.name().fullName()).withEmployeeDesignation("Scientist-G")
				.withEmailId(faker.internet().emailAddress()).withMobileNo(faker.phoneNumber().phoneNumber())
				.withLandlineNo(faker.phoneNumber().cellPhone()).withPlaceOfPosting(placeOfPosting())
				.withHodCode(empCode).withHogCode(empCode).withOfficeAddress(faker.address().fullAddress());
	}

	public static EmployeeDetailObjectMother.Builder aHodUser() {
		Integer empCode = Integer.parseInt(fakerService.numerify("3###"));

		return builder().withEmployeeCode(empCode).withEmployeeTitle(faker.name().prefix())
				.withEmployeeName(faker.name().fullName()).withEmployeeDesignation("Scientist-D")
				.withEmailId(faker.internet().emailAddress()).withMobileNo(faker.phoneNumber().phoneNumber())
				.withLandlineNo(faker.phoneNumber().cellPhone()).withPlaceOfPosting(placeOfPosting())
				.withHodCode(empCode).withHogCode(Integer.parseInt(fakerService.numerify("1###")))
				.withOfficeAddress(faker.address().fullAddress());
	}

	public static EmployeeDetailObjectMother.Builder aTechUser() {
		return builder().withEmployeeCode(Integer.parseInt(fakerService.regexify("[5-9][0-9]{3}")))
				.withEmployeeTitle(faker.name().prefix()).withEmployeeName(faker.name().fullName())
				.withEmployeeDesignation(faker.regexify("Scientist-[E-H]")).withEmailId(faker.internet().emailAddress())
				.withMobileNo(faker.phoneNumber().phoneNumber()).withLandlineNo(faker.phoneNumber().cellPhone())
				.withPlaceOfPosting(placeOfPosting()).withHodCode(Integer.parseInt(fakerService.numerify("3###")))
				.withHogCode(Integer.parseInt(fakerService.numerify("1###")))
				.withOfficeAddress(faker.address().fullAddress());
	}

	public static List<GroupModel> getGroupList() {
		return GroupModel.list(StringUtils.split(placeOfPosting(), ','));
	}

	// TODO: to replace it with random values later
	public static String placeOfPosting() {
		return "User Experience Design And Technology (Uxdt), Open Source Technology Development,Qa And Api Infra. Mgmt.,Dopt And Doppw,Cem Kochi,Data Modelling Mining Warehousing";
	}

	public static Builder builder() {
		return new EmployeeDetailObjectMother.Builder();
	}

	public static class Builder {

		private EmployeeDetail emp;

		public Builder() {
			emp = new EmployeeDetail();
		}

		public Builder withEmployeeCode(Integer employeeCode) {
			ReflectionTestUtils.setField(emp, "employeeCode", employeeCode);
			return this;
		}

		public Builder withEmployeeTitle(String employeeTitle) {
			ReflectionTestUtils.setField(emp, "employeeTitle", employeeTitle);
			return this;
		}

		public Builder withEmployeeName(String employeeName) {
			ReflectionTestUtils.setField(emp, "employeeName", employeeName);
			return this;
		}

		public Builder withEmployeeDesignation(String employeeDesignation) {
			ReflectionTestUtils.setField(emp, "employeeDesignation", employeeDesignation);
			return this;
		}

		public Builder withEmailId(String emailId) {
			ReflectionTestUtils.setField(emp, "emailId", emailId);
			return this;
		}

		public Builder withMobileNo(String mobileNo) {
			ReflectionTestUtils.setField(emp, "mobileNo", mobileNo);
			return this;
		}

		public Builder withLandlineNo(String landlineNo) {
			ReflectionTestUtils.setField(emp, "landlineNo", landlineNo);
			return this;
		}

		public Builder withPlaceOfPosting(String placeOfPosting) {
			ReflectionTestUtils.setField(emp, "placeOfPosting", placeOfPosting);
			return this;
		}

		public Builder withHogCode(Integer hogCode) {
			ReflectionTestUtils.setField(emp, "hogCode", hogCode);
			return this;
		}

		public Builder withHodCode(Integer hodCode) {
			ReflectionTestUtils.setField(emp, "hodCode", hodCode);
			return this;
		}

		public Builder withOfficeAddress(String officeAddress) {
			ReflectionTestUtils.setField(emp, "officeAddress", officeAddress);
			return this;
		}

		public EmployeeDetail build() {
			return emp;
		}

	}

}
