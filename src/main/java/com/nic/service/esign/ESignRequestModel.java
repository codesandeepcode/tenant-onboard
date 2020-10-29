package com.nic.service.esign;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.itextpdf.text.DocumentException;

@JacksonXmlRootElement(localName = "Esign")
class ESignRequestModel {

	@JsonIgnore
	private static final DateTimeFormatter TXN_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

	@JacksonXmlProperty(localName = "ver", isAttribute = true)
	private String version = "2.1";

	@JacksonXmlProperty(localName = "sc", isAttribute = true)
	private Character signConsent;

	@JacksonXmlProperty(localName = "ts", isAttribute = true)
	private String timestamp;

	@JacksonXmlProperty(localName = "txn", isAttribute = true)
	private String transactionId;

	@JacksonXmlProperty(localName = "ekycId", isAttribute = true)
	private String eKycId;

	@JacksonXmlProperty(localName = "ekycIdType", isAttribute = true)
	private Character eKycIdType = 'A';

	@JacksonXmlProperty(localName = "aspId", isAttribute = true)
	private String aspId = "TNIC-001";

	@JacksonXmlProperty(localName = "AuthMode", isAttribute = true)
	private Character authenticationMode = '1'; // For OTP only;

	@JacksonXmlProperty(localName = "responseSigType", isAttribute = true)
	private String responseSignatureType = "pkcs7"; // PKCS7 (CMS) signature type for now;

	@JacksonXmlProperty(localName = "responseUrl", isAttribute = true)
	private String responseUrl;

	@JacksonXmlProperty(localName = "Docs")
	private DocumentModel documentBody;

	public ESignRequestModel(String redirectUrl, String documentInfo, byte[] fileInByteArray, Integer transactionCount)
			throws IOException, DocumentException {
		this.signConsent = 'Y';
		this.eKycId = "";
		this.responseUrl = redirectUrl;

		// IST - Indian STandard Time
		ZonedDateTime instantTime = ZonedDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("IST")));

		this.timestamp = instantTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		this.transactionId = String.join("-", "999-BHARATAPI", instantTime.format(TXN_TIME_FORMAT),
				String.format("%06d", transactionCount));

		this.documentBody = new DocumentModel(documentInfo, fileInByteArray);
	}

	public String getVersion() {
		return version;
	}

	public Character getSignConsent() {
		return signConsent;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String geteKycId() {
		return eKycId;
	}

	public Character geteKycIdType() {
		return eKycIdType;
	}

	public String getAspId() {
		return aspId;
	}

	public Character getAuthenticationMode() {
		return authenticationMode;
	}

	public String getResponseSignatureType() {
		return responseSignatureType;
	}

	public String getResponseUrl() {
		return responseUrl;
	}

	public DocumentModel getDocumentBody() {
		return documentBody;
	}

}
