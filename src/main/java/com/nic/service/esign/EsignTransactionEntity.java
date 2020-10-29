package com.nic.service.esign;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "esp_transaction_details")
class EsignTransactionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer applicationId;

	private String espVersion;

	private String transactionId;

	private String signingPerson;

	private Character ekycType;

	private Character authenticationMode;

	private String signType;

	private LocalDateTime requestTimestamp;

	private Character responseStatus;

	private String responseCode;

	private String responseErrorCode;

	private String responseErrorMessage;

	@Column(name = "response_x509_cert")
	private byte[] responseX509Cert;

	private byte[] signedDocHash;

	private String docSignError;

	private String signHashAlgorithm;

	private LocalDateTime responseTimestamp;

	@SuppressWarnings("unused")
	private EsignTransactionEntity() {
		super();
	}

	public EsignTransactionEntity(int applicationId, String signingPerson, ESignRequestModel requestModel) {
		this.applicationId = applicationId;
		this.signingPerson = signingPerson;

		this.espVersion = requestModel.getVersion();
		this.transactionId = requestModel.getTransactionId();
		this.ekycType = requestModel.geteKycIdType();
		this.authenticationMode = requestModel.getAuthenticationMode();
		this.signType = requestModel.getResponseSignatureType();
		this.requestTimestamp = LocalDateTime.parse(requestModel.getTimestamp(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	public void updateAuditDetails(ESignResponseModel responseModel) {
		this.responseStatus = responseModel.getStatus().charAt(0);
		this.responseCode = responseModel.getResponseCode();
		this.responseErrorCode = responseModel.getErrorCode();
		this.responseErrorMessage = responseModel.getErrorMessage();

		this.responseX509Cert = (responseModel.getUserCertificate() != null)
				? responseModel.getUserCertificate().getBytes()
				: null;

		this.signedDocHash = responseModel.returnSignedDocumentHash();
		this.docSignError = responseModel.returnDocumentError();
		this.signHashAlgorithm = responseModel.returnSignHashAlgorithm();

		this.responseTimestamp = (responseModel.getTimeStamp() != null)
				? LocalDateTime.parse(responseModel.getTimeStamp())
				: null;
	}

	public Integer getId() {
		return id;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public String getEspVersion() {
		return espVersion;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getSigningPerson() {
		return signingPerson;
	}

	public Character getEkycType() {
		return ekycType;
	}

	public Character getAuthenticationMode() {
		return authenticationMode;
	}

	public String getSignType() {
		return signType;
	}

	public LocalDateTime getRequestTimestamp() {
		return requestTimestamp;
	}

	public Character getResponseStatus() {
		return responseStatus;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public String getResponseErrorCode() {
		return responseErrorCode;
	}

	public String getResponseErrorMessage() {
		return responseErrorMessage;
	}

	public byte[] getResponseX509Cert() {
		return responseX509Cert;
	}

	public byte[] getSignedDocHash() {
		return signedDocHash;
	}

	public String getDocSignError() {
		return docSignError;
	}

	public String getSignHashAlgorithm() {
		return signHashAlgorithm;
	}

	public LocalDateTime getResponseTimestamp() {
		return responseTimestamp;
	}

}
