package com.nic.service.onboarding.publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.test.util.ReflectionTestUtils;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.nic.service.onboarding.publisher.GovernmentApplicationFormModel.Office;
import com.nic.service.onboarding.publisher.GovernmentApplicationFormModel.ProjectHead;
import com.nic.service.utils.GovernmentType;

class GovernmentApplicationFormModelObjectMother {

	private static Faker faker = new Faker(new Locale("en", "IND"));

	private static FakeValuesService fakerService = new FakeValuesService(new Locale("en", "IND"), new RandomService());

	public static GovernmentApplicationFormModelObjectMother.Builder defaultApplication(boolean full) {
		String projectName = faker.company().name();
		projectName = projectName.substring(0, Math.min(30, projectName.length()));

		Builder builder = builder()
				.withGovernmentType(GovernmentType.getById(faker.random().nextInt(1, 2).shortValue()))
				.withOffice(defaultOffice().build()).withProjectHead(defaultProjectHead().build())
				.withDomainName(faker.regexify("[a-z]{9}\\.(nic|gov)\\.in")).withProjectName(projectName);

		int count = full ? 3 : faker.random().nextInt(1, 2);
		List<TechnicalHeadDetailsModel> list = new ArrayList<>();
		while (count != 0) {
			list.add(TechnicalHeadDetailsModelObjectMother.defaultTechnical().build());
			count -= 1;
		}

		builder.withTechnicalHeadList(list);
		return builder;
	}

	public static OfficeBuilder defaultOffice() {
		return new OfficeBuilder().withDepartment(Integer.toString(faker.random().nextInt(1, 4)))
				.withState(String.format("%02d", faker.random().nextInt(1, 35)))
				.withCategory(Integer.toString(faker.random().nextInt(1, 8))).withName(faker.company().name())
				.withAddress(faker.address().fullAddress());
	}

	public static ProjectHeadBuilder defaultProjectHead() {
		ProjectHeadBuilder builder = new ProjectHeadBuilder().withCode(fakerService.regexify("[A-Z0-9]{9}"))
				.withName(faker.name().fullName()).withDesignation(faker.job().position())
				.withGroup(faker.commerce().productName()).withEmailId(faker.internet().emailAddress())
				.withMobileNo(fakerService.regexify("[789][0-9]{9}"));

		if (faker.bool().bool())
			builder.withLandlineNo(faker.phoneNumber().phoneNumber());

		return builder;
	}

	static class OfficeBuilder {

		private Office model;

		private Builder builder;

		public OfficeBuilder() {
			model = new Office();
		}

		public OfficeBuilder withDepartment(String department) {
			ReflectionTestUtils.setField(model, "department", department);
			return this;
		}

		public OfficeBuilder withState(String state) {
			ReflectionTestUtils.setField(model, "state", state);
			return this;
		}

		public OfficeBuilder withCategory(String category) {
			ReflectionTestUtils.setField(model, "category", category);
			return this;
		}

		public OfficeBuilder withName(String name) {
			ReflectionTestUtils.setField(model, "name", name);
			return this;
		}

		public OfficeBuilder withAddress(String address) {
			ReflectionTestUtils.setField(model, "address", address);
			return this;
		}

		public Office build() {
			return model;
		}

		public Builder add() {
			builder.withOffice(build());
			return builder;
		}

	}

	static class ProjectHeadBuilder {

		private ProjectHead model;

		private Builder builder;

		public ProjectHeadBuilder() {
			model = new ProjectHead();
		}

		public ProjectHeadBuilder withCode(String code) {
			ReflectionTestUtils.setField(model, "code", code);
			return this;
		}

		public ProjectHeadBuilder withName(String name) {
			ReflectionTestUtils.setField(model, "name", name);
			return this;
		}

		public ProjectHeadBuilder withDesignation(String designation) {
			ReflectionTestUtils.setField(model, "designation", designation);
			return this;
		}

		public ProjectHeadBuilder withGroup(String group) {
			ReflectionTestUtils.setField(model, "group", group);
			return this;
		}

		public ProjectHeadBuilder withEmailId(String emailId) {
			ReflectionTestUtils.setField(model, "emailId", emailId);
			return this;
		}

		public ProjectHeadBuilder withMobileNo(String mobileNo) {
			ReflectionTestUtils.setField(model, "mobileNo", mobileNo);
			return this;
		}

		public ProjectHeadBuilder withLandlineNo(String landlineNo) {
			ReflectionTestUtils.setField(model, "landlineNo", landlineNo);
			return this;
		}

		public ProjectHead build() {
			return model;
		}

		public Builder add() {
			builder.withProjectHead(build());
			return builder;
		}

	}

	public static GovernmentApplicationFormModelObjectMother.Builder builder() {
		return new GovernmentApplicationFormModelObjectMother.Builder();
	}

	static class Builder {

		private GovernmentApplicationFormModel model;

		public Builder() {
			model = new GovernmentApplicationFormModel();
		}

		public Builder withGovernmentType(GovernmentType governmentType) {
			ReflectionTestUtils.setField(model, "governmentType", governmentType);
			return this;
		}

		public Builder withOffice(Office office) {
			ReflectionTestUtils.setField(model, "office", office);
			return this;
		}

		public Builder withProjectHead(ProjectHead projectHead) {
			ReflectionTestUtils.setField(model, "projectHead", projectHead);
			return this;
		}

		public Builder withTechnicalHeadList(List<TechnicalHeadDetailsModel> technicalHeadList) {
			ReflectionTestUtils.setField(model, "technicalHeadList", technicalHeadList);
			return this;
		}

		public Builder withDomainName(String domainName) {
			ReflectionTestUtils.setField(model, "domainName", domainName);
			return this;
		}

		public Builder withProjectName(String projectName) {
			ReflectionTestUtils.setField(model, "projectName", projectName);
			return this;
		}

		public ProjectHeadBuilder addProjectHead() {
			ProjectHeadBuilder builder = new ProjectHeadBuilder();
			builder.builder = this;
			return builder;
		}

		public OfficeBuilder addOffice() {
			OfficeBuilder builder = new OfficeBuilder();
			builder.builder = this;
			return builder;
		}

		public GovernmentApplicationFormModel build() {
			return model;
		}

	}

}
