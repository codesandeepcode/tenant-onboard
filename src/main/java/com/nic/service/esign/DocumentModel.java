package com.nic.service.esign;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

class DocumentModel {

	@JacksonXmlProperty(localName = "InputHash")
	private InputHashModel inputHash;

	DocumentModel(String documentInfo, byte[] fileInByteArray) {
		this.inputHash = new InputHashModel(documentInfo, fileInByteArray);
	}

	public InputHashModel getInputHash() {
		return inputHash;
	}

}
