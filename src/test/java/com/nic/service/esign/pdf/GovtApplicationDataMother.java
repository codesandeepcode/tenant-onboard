package com.nic.service.esign.pdf;

import java.util.ArrayList;
import java.util.Locale;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.nic.service.esign.pdf.GovtApplicationModel.Office;
import com.nic.service.esign.pdf.GovtApplicationModel.ProjectHead;
import com.nic.service.esign.pdf.GovtApplicationModel.TechnicalHead;
import com.nic.service.utils.GovernmentType;

public class GovtApplicationDataMother {

	private static Faker faker = new Faker(new Locale("en", "IND"));
	private static FakeValuesService fakerService = new FakeValuesService(new Locale("en", "IND"), new RandomService());

	/**
	 * A default government application (kind of)
	 */
	public static GovtApplicationDataMother.Builder aDefaultApplication() {
		return builder().withGovernmentType(GovernmentType.CENTRAL).withOffice(aDefaultOffice())
				.withProjectHead(aRandomProjectHead()).withTechnicalHead(aRandomTechnicalHead())
				.withTechnicalHead(aRandomTechnicalHead()).withDomainName(fakerService.letterify("?????.nic.in"))
				.withProjectName(faker.company().name());
	}

	public static GovtApplicationDataMother.Builder builder() {
		return new GovtApplicationDataMother.Builder();
	}

	public static class Builder {

		private GovtApplicationModel app;

		public Builder() {
			this.app = new GovtApplicationModel();
		}

		public Builder withGovernmentType(GovernmentType type) {
			this.app.setGovernmentType(type);
			return this;
		}

		public Builder withOffice(Office office) {
			this.app.setOffice(office);
			return this;
		}

		public Builder withProjectHead(ProjectHead projectHead) {
			this.app.setProjectHead(projectHead);
			return this;
		}

		public Builder withTechnicalHead(TechnicalHead testHead) {
			if (this.app.getTechnicalHeadList() == null) {
				this.app.setTechnicalHeadList(new ArrayList<>());
			}

			this.app.getTechnicalHeadList().add(testHead);
			return this;
		}

		public Builder withDomainName(String domainName) {
			this.app.setDomainName(domainName);
			return this;
		}

		public Builder withProjectName(String projectName) {
			this.app.setProjectName(projectName);
			return this;
		}

		public GovtApplicationModel build() {
			return this.app;
		}

	}

	// TODO: to complete
	public GovtApplicationModel.Office aRandomOffice() {
		GovtApplicationModel.Office office = new GovtApplicationModel.Office();
		// office.setDepartment(fakerService.);
		return office;
	}

	// TODO: add random
	public static GovtApplicationModel.Office aDefaultOffice() {
		GovtApplicationModel.Office office = new GovtApplicationModel.Office();
		office.setDepartment("Ministry of Agriculture");
		office.setState("HARYANA");
		office.setCategory("Statutory bodies fully funded by Center/State Govt with no internal revenue resources");
		office.setName("Food Affairs Department");
		office.setAddress(faker.address().fullAddress());
		return office;
	}

	public static GovtApplicationModel.ProjectHead aRandomProjectHead() {
		GovtApplicationModel.ProjectHead head = new GovtApplicationModel.ProjectHead();
		head.setCode(fakerService.bothify("????##??#"));
		head.setName(faker.name().fullName());
		head.setDesignation("Project Head");
		head.setGroup("Food Affairs Group");
		head.setEmailId(faker.internet().emailAddress());
		head.setMobileNo(faker.phoneNumber().phoneNumber());
		head.setLandlineNo(faker.phoneNumber().phoneNumber());
		return head;
	}

	public static GovtApplicationModel.TechnicalHead aRandomTechnicalHead() {
		GovtApplicationModel.TechnicalHead tech = new GovtApplicationModel.TechnicalHead();
		tech.setCode(fakerService.bothify("????##??#"));
		tech.setName(faker.name().fullName());
		tech.setDesignation("Technical Head");
		tech.setEmailId(faker.internet().emailAddress());
		tech.setMobileNo(faker.phoneNumber().phoneNumber());
		tech.setLandlineNo(faker.phoneNumber().phoneNumber());
		return tech;
	}

}
