package com.nic.service.onboarding.publisher;

import java.util.Locale;

import org.springframework.test.util.ReflectionTestUtils;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

class TechnicalHeadDetailsModelObjectMother {

	private static Faker faker = new Faker(new Locale("en", "IND"));

	private static FakeValuesService fakerService = new FakeValuesService(new Locale("en", "IND"), new RandomService());

	public static Builder defaultTechnical() {
		Builder builder = builder().withCode(fakerService.regexify("[A-Z0-9]{9}")).withName(faker.name().fullName())
				.withDesignation(faker.job().position()).withEmailId(faker.internet().emailAddress())
				.withMobileNo(fakerService.regexify("[789][0-9]{9}"));

		if (faker.bool().bool())
			builder.withLandlineNo(faker.phoneNumber().phoneNumber());

		return builder;
	}

	public static Builder builder() {
		return new TechnicalHeadDetailsModelObjectMother.Builder();
	}

	static class Builder {

		private TechnicalHeadDetailsModel model;

		public Builder() {
			model = new TechnicalHeadDetailsModel();
		}

		public Builder withCode(String code) {
			ReflectionTestUtils.setField(model, "code", code);
			return this;
		}

		public Builder withName(String name) {
			ReflectionTestUtils.setField(model, "name", name);
			return this;
		}

		public Builder withDesignation(String designation) {
			ReflectionTestUtils.setField(model, "designation", designation);
			return this;
		}

		public Builder withEmailId(String emailId) {
			ReflectionTestUtils.setField(model, "emailId", emailId);
			return this;
		}

		public Builder withMobileNo(String mobileNo) {
			ReflectionTestUtils.setField(model, "mobileNo", mobileNo);
			return this;
		}

		public Builder withLandlineNo(String landlineNo) {
			ReflectionTestUtils.setField(model, "landlineNo", landlineNo);
			return this;
		}

		public TechnicalHeadDetailsModel build() {
			return model;
		}

	}

}
