package com.finlabs.finexa.dto;

public class FinexaExceptionHandlingDTO {

	private int id;
	private int subModuleID;
	private int functionID;
	private String functionName;
	private String subFunctionName;
	private String functionEvent;
	private String functionSubEvent;
	private String errorCode;
	private String errorMessage;
	private String moduleName;
	private String subModuleName;

	public FinexaExceptionHandlingDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSubModuleID() {
		return subModuleID;
	}

	public void setSubModuleID(int subModuleID) {
		this.subModuleID = subModuleID;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getSubModuleName() {
		return subModuleName;
	}

	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName;
	}

	public int getFunctionID() {
		return functionID;
	}

	public void setFunctionID(int functionID) {
		this.functionID = functionID;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionEvent() {
		return functionEvent;
	}

	public void setFunctionEvent(String functionEvent) {
		this.functionEvent = functionEvent;
	}

	public String getFunctionSubEvent() {
		return functionSubEvent;
	}

	public void setFunctionSubEvent(String functionSubEvent) {
		this.functionSubEvent = functionSubEvent;
	}

	public String getSubFunctionName() {
		return subFunctionName;
	}

	public void setSubFunctionName(String subFunctionName) {
		this.subFunctionName = subFunctionName;
	}

	@Override
	public String toString() {
		return "FinexaExceptionHandlingDTO [id=" + id + ", subModuleID=" + subModuleID + ", functionID=" + functionID
				+ ", functionName=" + functionName + ", subFunctionName=" + subFunctionName + ", functionEvent="
				+ functionEvent + ", functionSubEvent=" + functionSubEvent + ", errorCode=" + errorCode
				+ ", errorMessage=" + errorMessage + ", moduleName=" + moduleName + ", subModuleName=" + subModuleName
				+ "]";
	}

}
