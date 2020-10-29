package com.nic.service.esign;

import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "EsignResp")
class ESignResponseModel {

	@JacksonXmlProperty(isAttribute = true)
	private String status;

	@JacksonXmlProperty(localName = "ts", isAttribute = true)
	private String timeStamp;

	@JacksonXmlProperty(localName = "txn", isAttribute = true)
	private String transactionId;

	@JacksonXmlProperty(localName = "resCode", isAttribute = true)
	private String responseCode;

	@JacksonXmlProperty(localName = "errCode", isAttribute = true)
	private String errorCode;

	@JacksonXmlProperty(localName = "errMsg", isAttribute = true)
	private String errorMessage;

	@JacksonXmlProperty(localName = "UserX509Certificate")
	private String userCertificate;

	@JacksonXmlProperty(localName = "Signatures")
	private SignaturesModel signaturesObject;

	public ESignResponseModel() {
		super();
	}

	public ESignResponseModel(EsignTransactionEntity transactionDetails) {
		this.status = transactionDetails.getResponseStatus().toString();
		this.timeStamp = transactionDetails.getResponseTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		this.transactionId = transactionDetails.getTransactionId();
		this.responseCode = transactionDetails.getResponseCode();
		this.errorCode = transactionDetails.getResponseErrorCode();
		this.errorMessage = transactionDetails.getResponseErrorMessage();
		this.userCertificate = new String(transactionDetails.getResponseX509Cert());

		this.signaturesObject = new SignaturesModel(transactionDetails);
	}

	public byte[] returnSignedDocumentHash() {
		return (signaturesObject != null) ? signaturesObject.getDocSignObject().getSignedData().getBytes() : null;
	}

	public String returnDocumentError() {
		return (signaturesObject != null) ? signaturesObject.getDocSignObject().getError() : null;
	}

	public String returnSignHashAlgorithm() {
		return (signaturesObject != null) ? signaturesObject.getDocSignObject().getSignHashAlgorithm() : null;
	}

	public String getStatus() {
		return status;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getUserCertificate() {
		return userCertificate;
	}

	public SignaturesModel getSignaturesObject() {
		return signaturesObject;
	}

}
