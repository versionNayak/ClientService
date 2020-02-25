package com.finlabs.finexa.dto;



public class ClientUCCResultDTO {
	
	boolean status;
	
	int statusCode;
	
	String message;
	
	String optionalParam;
	
	String optionalParamCKYC;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getOptionalParam() {
		return optionalParam;
	}

	public void setOptionalParam(String optionalParam) {
		this.optionalParam = optionalParam;
	}

	public String getOptionalParamCKYC() {
		return optionalParamCKYC;
	}

	public void setOptionalParamCKYC(String optionalParamCKYC) {
		this.optionalParamCKYC = optionalParamCKYC;
	}
	
}
