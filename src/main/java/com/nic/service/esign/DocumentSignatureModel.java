package com.nic.service.esign;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

class DocumentSignatureModel {

	@JacksonXmlProperty(isAttribute = true)
	private String id;

	@JacksonXmlProperty(localName = "sigHashAlgorithm", isAttribute = true)
	private String signHashAlgorithm;

	@JacksonXmlProperty(isAttribute = true)
	private String error;

	@JacksonXmlText
	private String signedData;

	public DocumentSignatureModel() {
		super();
	}

	public DocumentSignatureModel(EsignTransactionEntity transactionDetails) {
		this.signedData = new String(transactionDetails.getSignedDocHash());
		this.error = transactionDetails.getDocSignError();
		this.signHashAlgorithm = transactionDetails.getSignHashAlgorithm();
	}

	public String getId() {
		return id;
	}

	public String getSignHashAlgorithm() {
		return signHashAlgorithm;
	}

	public String getError() {
		return error;
	}

	public String getSignedData() {
		return signedData;
	}

}
