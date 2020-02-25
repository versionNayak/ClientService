package com.finlabs.finexa.dto;

public class MasterFinexaExceptionDTO {
	
	private int id;
    private String errorCode;
    private String errorDescription;
	
    public MasterFinexaExceptionDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

    
    
    
}
