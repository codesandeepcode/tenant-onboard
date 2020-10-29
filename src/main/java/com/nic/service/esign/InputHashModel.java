package com.nic.service.esign;

import org.apache.commons.codec.digest.DigestUtils;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

class InputHashModel {

	@JacksonXmlProperty(isAttribute = true)
	private short id = 1;

	@JacksonXmlProperty(isAttribute = true)
	private String hashAlgorithm = "SHA256";

	@JacksonXmlProperty(localName = "docInfo", isAttribute = true)
	private String documentInformation;

	@JacksonXmlText
	private String documentHash;

	InputHashModel(String documentInfo, byte[] fileInByteArray) {
		this.documentInformation = documentInfo;
		this.documentHash = DigestUtils.sha256Hex(fileInByteArray);
	}

	public short getId() {
		return id;
	}

	public String getHashAlgorithm() {
		return hashAlgorithm;
	}

	public String getDocumentInformation() {
		return documentInformation;
	}

	public String getDocumentHash() {
		return documentHash;
	}

}
