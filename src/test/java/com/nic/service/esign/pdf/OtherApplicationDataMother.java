package com.nic.service.esign.pdf;

import java.util.ArrayList;
import java.util.Locale;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.nic.service.esign.pdf.OthersApplicationModel.Office;
import com.nic.service.esign.pdf.OthersApplicationModel.ProjectHead;
import com.nic.service.esign.pdf.OthersApplicationModel.TechnicalHead;

public class OtherApplicationDataMother {

	private static Faker faker = new Faker(new Locale("en", "IND"));
	private static FakeValuesService fakerService = new FakeValuesService(new Locale("en", "IND"), new RandomService());

	public static OtherApplicationDataMother.Builder builder() {
		return new OtherApplicationDataMother.Builder();
	}

	public static OtherApplicationDataMother.Builder aDefaultApplication() {
		return builder().withOffice(aRandomOffice()).withProjectHead(aRandomProjectHead())
				.withTechnicalHead(aRandomTechHead()).withTechnicalHead(aRandomTechHead())
				.withDomainName(fakerService.letterify("????????.nic.in")).withProjectName(faker.company().name());
	}

	public static class Builder {

		private OthersApplicationModel app;

		public Builder() {
			app = new OthersApplicationModel();
		}

		public Builder withOffice(Office office) {
			this.app.setOffice(office);
			return this;
		}

		public Builder withProjectHead(ProjectHead projectHead) {
			this.app.setProjectHead(projectHead);
			return this;
		}

		public Builder withTechnicalHead(TechnicalHead technicalHead) {
			if (this.app.getTechnicalHead() == null)
				this.app.setTechnicalHead(new ArrayList<>());

			this.app.getTechnicalHead().add(technicalHead);
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

		public OthersApplicationModel build() {
			return app;
		}

	}

	public static OthersApplicationModel.Office aRandomOffice() {
		OthersApplicationModel.Office office = new OthersApplicationModel.Office();
		office.setName(faker.company().name());
		office.setState(faker.address().state());
		office.setAddress(faker.address().fullAddress());
		return office;
	}

	public static OthersApplicationModel.ProjectHead aRandomProjectHead() {
		OthersApplicationModel.ProjectHead projectHead = new OthersApplicationModel.ProjectHead();
		projectHead.setCode(fakerService.bothify("##??#?"));
		projectHead.setName(faker.name().fullName());
		projectHead.setDesignation(faker.company().profession());
		projectHead.setEmailId(faker.internet().emailAddress());
		projectHead.setMobileNo(faker.phoneNumber().phoneNumber());
		projectHead.setLandlineNo(faker.phoneNumber().cellPhone());
		return projectHead;
	}

	public static OthersApplicationModel.TechnicalHead aRandomTechHead() {
		OthersApplicationModel.TechnicalHead tech = new OthersApplicationModel.TechnicalHead();
		tech.setCode(fakerService.bothify("????##??#"));
		tech.setName(faker.name().fullName());
		tech.setDesignation(faker.company().profession());
		tech.setEmailId(faker.internet().emailAddress());
		tech.setMobileNo(faker.phoneNumber().phoneNumber());
		tech.setLandlineNo(faker.phoneNumber().phoneNumber());
		return tech;
	}

}
