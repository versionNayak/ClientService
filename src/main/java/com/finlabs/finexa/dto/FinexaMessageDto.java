package com.finlabs.finexa.dto;

import java.util.List;

public class FinexaMessageDto {

	private String code;
	private String message;
	
	private List<ErrorDTO> errorList;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ErrorDTO> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<ErrorDTO> errorList) {
		this.errorList = errorList;
	}
	
	

}
