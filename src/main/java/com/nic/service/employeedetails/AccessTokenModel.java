package com.nic.service.employeedetails;

class AccessTokenModel {

	public String accessToken;

	public String scope;

	public String tokenType;

	public Long expiresIn;

	public String getAccessToken() {
		return accessToken;
	}

	public String getScope() {
		return scope;
	}

	public String getTokenType() {
		return tokenType;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

}
