package com.finlabs.finexa.dto;

import java.util.ArrayList;
import java.util.List;

public class UploadResponseDTO {
	
	private boolean status;
	private boolean primaryKeyNotFound;
	private int rejectedRecords;
	private String message;
	
	public int getRejectedRecords() {
		return rejectedRecords;
	}

	public void setRejectedRecords(int rejectedRecords) {
		this.rejectedRecords = rejectedRecords;
	}

	private List<String> errors = new ArrayList<String>() ;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public boolean isPrimaryKeyNotFound() {
		return primaryKeyNotFound;
	}

	public void setPrimaryKeyNotFound(boolean primaryKeyNotFound) {
		this.primaryKeyNotFound = primaryKeyNotFound;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
