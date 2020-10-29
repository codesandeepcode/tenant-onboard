package com.nic.service.esign;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import org.springframework.test.util.ReflectionTestUtils;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

class ESignResponseModelObjectMother {

	private static Faker faker = new Faker(new Locale("en", "IND"));
	private static FakeValuesService fakerService = new FakeValuesService(new Locale("en", "IND"), new RandomService());

	static class DocumentSignatureModelObjectMother {

		public static Builder aNonErrorDocSign() {
			return builder().withId("1").withError("NA").withSignHashAlgorithm("SHA256")
					.withSignedData(faker.random().hex(500));
		}

		public static Builder aErrorDocSign() {
			return builder().withId("0").withError("There is errorin signing").withSignHashAlgorithm("SHA256");
		}

		public static Builder builder() {
			return new DocumentSignatureModelObjectMother.Builder();
		}

		public static class Builder {

			private DocumentSignatureModel model;

			public Builder() {
				model = new DocumentSignatureModel();
			}

			public Builder withId(String id) {
				ReflectionTestUtils.setField(model, "id", id);
				return this;
			}

			public Builder withSignHashAlgorithm(String signHashAlgorithm) {
				ReflectionTestUtils.setField(model, "signHashAlgorithm", signHashAlgorithm);
				return this;
			}

			public Builder withError(String error) {
				ReflectionTestUtils.setField(model, "error", error);
				return this;
			}

			public Builder withSignedData(String signedData) {
				ReflectionTestUtils.setField(model, "signedData", signedData);
				return this;
			}

			public DocumentSignatureModel build() {
				return model;
			}

		}
	}

	static class SignaturesModelObjectMother {

		public static Builder aNonErrorWrapper() {
			return builder().withDocSignObject(DocumentSignatureModelObjectMother.aNonErrorDocSign().build());
		}

		public static Builder builder() {
			return new SignaturesModelObjectMother.Builder();
		}

		public static class Builder {

			private SignaturesModel model;

			public Builder() {
				model = new SignaturesModel();
			}

			public Builder withDocSignObject(DocumentSignatureModel docSignObject) {
				ReflectionTestUtils.setField(this.model, "docSignObject", docSignObject);
				return this;
			}

			public SignaturesModel build() {
				return model;
			}

		}

	}

	public static Builder aNonErrorResponse() {
		return builder().withStatus("1")
				.withTimeStamp(LocalDateTime.now().minusMonths(faker.random().nextInt(1, 12))
						.minusYears(faker.random().nextInt(1, 20)).toString())
				.withTransactionId(fakerService.numerify("999-BHARATAPI-########-######-######"))
				.withResponseCode(UUID.randomUUID().toString()).withErrorCode("NA").withErrorMessage("NA")
				.withUserCertificate(faker.random().hex(1000))
				.withSignaturesObject(SignaturesModelObjectMother.aNonErrorWrapper().build());
	}

	public static Builder aErrorResponse() {
		return builder().withStatus("0")
				.withTimeStamp(LocalDateTime.now().minusMonths(faker.random().nextInt(1, 12))
						.minusYears(faker.random().nextInt(1, 20)).toString())
				.withTransactionId(fakerService.numerify("999-BHARATAPI-########-######-######"))
				.withResponseCode(UUID.randomUUID().toString()).withErrorCode(fakerService.numerify("ESP-###"))
				.withErrorMessage(faker.chuckNorris().fact());
	}

	public static Builder aReponseWithOnlyErrorCode() {
		return builder().withErrorCode(fakerService.numerify("ESP-###")).withTimeStamp(LocalDateTime.now()
				.minusMonths(faker.random().nextInt(1, 12)).minusYears(faker.random().nextInt(1, 20)).toString());
	}

	public static Builder builder() {
		return new ESignResponseModelObjectMother.Builder();
	}

	public static class Builder {

		private ESignResponseModel model;

		public Builder() {
			model = new ESignResponseModel();
		}

		public Builder withStatus(String status) {
			ReflectionTestUtils.setField(model, "status", status);
			return this;
		}

		public Builder withTimeStamp(String timeStamp) {
			ReflectionTestUtils.setField(model, "timeStamp", timeStamp);
			return this;
		}

		public Builder withTransactionId(String transactionId) {
			ReflectionTestUtils.setField(model, "transactionId", transactionId);
			return this;
		}

		public Builder withResponseCode(String responseCode) {
			ReflectionTestUtils.setField(model, "responseCode", responseCode);
			return this;
		}

		public Builder withErrorCode(String errorCode) {
			ReflectionTestUtils.setField(model, "errorCode", errorCode);
			return this;
		}

		public Builder withErrorMessage(String errorMessage) {
			ReflectionTestUtils.setField(model, "errorMessage", errorMessage);
			return this;
		}

		public Builder withUserCertificate(String userCertificate) {
			ReflectionTestUtils.setField(model, "userCertificate", userCertificate);
			return this;
		}

		public Builder withSignaturesObject(SignaturesModel signaturesObject) {
			ReflectionTestUtils.setField(model, "signaturesObject", signaturesObject);
			return this;
		}

		public ESignResponseModel build() {
			return model;
		}

	}

}
