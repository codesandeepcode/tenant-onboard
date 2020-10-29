package com.nic.service.onboarding.publisher;

class ResponseStatus {

	private String referenceNo;

	private String message;

	public ResponseStatus(String referenceNo) {
		if (referenceNo == null || referenceNo.trim().length() == 0)
			throw new RuntimeException("Application form not saved");

		this.referenceNo = referenceNo;
		this.message = "Application is saved and generated reference number is " + this.referenceNo;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public String getMessage() {
		return message;
	}

}
