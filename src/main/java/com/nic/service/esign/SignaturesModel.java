package com.nic.service.esign;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

class SignaturesModel {

	@JacksonXmlProperty(localName = "DocSignature")
	private DocumentSignatureModel docSignObject;

	public SignaturesModel() {
		super();
	}

	public SignaturesModel(EsignTransactionEntity transactionDetails) {
		this.docSignObject = new DocumentSignatureModel(transactionDetails);
	}

	public DocumentSignatureModel getDocSignObject() {
		return docSignObject;
	}

}
