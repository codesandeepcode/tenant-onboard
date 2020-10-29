package com.nic.service.onboarding.publisher;

import java.util.Locale;

import org.springframework.test.util.ReflectionTestUtils;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.nic.service.onboarding.publisher.NicApplicationFormModel.PersonalDetails;

public class NicApplicationFormModelObjectMother {

	private static Faker faker = new Faker(new Locale("en", "IND"));

	private static FakeValuesService fakerService = new FakeValuesService(new Locale("en", "IND"), new RandomService());

	public static NicApplicationFormModelObjectMother.Builder defaultApplication() {
		String projectName = faker.company().name();
		projectName = projectName.substring(0, Math.min(30, projectName.length()));

		return builder().withGroup(faker.commerce().productName())
				.withDomain(faker.regexify("[a-z]{9}\\.(nic|gov)\\.in")).withProject(projectName)
				.withHog(defaultPersonal().build()).withHod(defaultPersonal().build())
				.withTechAdmin1(defaultPersonal().build()).withTechAdmin2(defaultPersonal().build());
	}

	public static NicApplicationFormModelObjectMother.PersonalDetailsBuilder defaultPersonal() {
		NicApplicationFormModelObjectMother.PersonalDetailsBuilder builder = personalBuilder()
				.withCode(fakerService.numerify("####")).withName(faker.name().fullName())
				.withDesignation(faker.job().position()).withAddress(faker.address().fullAddress())
				.withDepartment("NIC").withEmailId(faker.internet().emailAddress())
				.withMobileNo(fakerService.regexify("[789][0-9]{9}"));

		if (faker.bool().bool())
			builder.withLandlineNo(faker.phoneNumber().phoneNumber());

		return builder;
	}

	public static NicApplicationFormModelObjectMother.PersonalDetailsBuilder personalBuilder() {
		return new NicApplicationFormModelObjectMother.PersonalDetailsBuilder();
	}

	static class PersonalDetailsBuilder {

		private PersonalDetails model;

		private Builder builder;

		public PersonalDetailsBuilder() {
			model = new PersonalDetails();
		}

		public PersonalDetailsBuilder withCode(String code) {
			ReflectionTestUtils.setField(model, "code", code);
			return this;
		}

		public PersonalDetailsBuilder withName(String name) {
			ReflectionTestUtils.setField(model, "name", name);
			return this;
		}

		public PersonalDetailsBuilder withDesignation(String designation) {
			ReflectionTestUtils.setField(model, "designation", designation);
			return this;
		}

		public PersonalDetailsBuilder withAddress(String address) {
			ReflectionTestUtils.setField(model, "address", address);
			return this;
		}

		public PersonalDetailsBuilder withDepartment(String department) {
			ReflectionTestUtils.setField(model, "department", department);
			return this;
		}

		public PersonalDetailsBuilder withEmailId(String emailId) {
			ReflectionTestUtils.setField(model, "emailId", emailId);
			return this;
		}

		public PersonalDetailsBuilder withMobileNo(String mobileNo) {
			ReflectionTestUtils.setField(model, "mobileNo", mobileNo);
			return this;
		}

		public PersonalDetailsBuilder withLandlineNo(String landlineNo) {
			ReflectionTestUtils.setField(model, "landlineNo", landlineNo);
			return this;
		}

		public PersonalDetails build() {
			return model;
		}

		public Builder addToHog() {
			builder.withHog(build());
			return builder;
		}

		public Builder addToHod() {
			builder.withHod(build());
			return builder;
		}

		public Builder addToTechAdmin1() {
			builder.withTechAdmin1(build());
			return builder;
		}

		public Builder addToTechAdmin2() {
			builder.withTechAdmin2(build());
			return builder;
		}

	}

	public static Builder builder() {
		return new NicApplicationFormModelObjectMother.Builder();
	}

	static class Builder {

		private NicApplicationFormModel model;

		public Builder() {
			model = new NicApplicationFormModel();
		}

		public Builder withGroup(String group) {
			ReflectionTestUtils.setField(model, "group", group);
			return this;
		}

		public Builder withHog(PersonalDetails hog) {
			ReflectionTestUtils.setField(model, "hog", hog);
			return this;
		}

		public Builder withHod(PersonalDetails hod) {
			ReflectionTestUtils.setField(model, "hod", hod);
			return this;
		}

		public Builder withTechAdmin1(PersonalDetails techAdmin1) {
			ReflectionTestUtils.setField(model, "techAdmin1", techAdmin1);
			return this;
		}

		public Builder withTechAdmin2(PersonalDetails techAdmin2) {
			ReflectionTestUtils.setField(model, "techAdmin2", techAdmin2);
			return this;
		}

		public Builder withDomain(String domain) {
			ReflectionTestUtils.setField(model, "domain", domain);
			return this;
		}

		public Builder withProject(String project) {
			ReflectionTestUtils.setField(model, "project", project);
			return this;
		}

		public PersonalDetailsBuilder addPersonal() {
			PersonalDetailsBuilder builder = new PersonalDetailsBuilder();
			builder.builder = this;
			return builder;
		}

		public NicApplicationFormModel build() {
			return model;
		}

	}

}
