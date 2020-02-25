package com.finlabs.finexa.dto;

import java.math.BigInteger;

public class RmMasterBODTO {
	
	private int ID;
	private String rmName;
	private String rmEmailID;
	private String rmEmployeeCode;
	private BigInteger rmMobileNumber;
	private int advisorID;
	private String branchCode;
	private int rmBranch;
	private String errorString;
	private String rmBranchName;
	private int rmMasterId;
	private int supervisorId;
	
	public int getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(int supervisorId) {
		this.supervisorId = supervisorId;
	}

	public int getRmMasterId() {
		return rmMasterId;
	}

	public void setRmMasterId(int rmMasterId) {
		this.rmMasterId = rmMasterId;
	}

	public String getRmBranchName() {
		return rmBranchName;
	}

	public void setRmBranchName(String rmBranchName) {
		this.rmBranchName = rmBranchName;
	}

	public void setRmBranch(int rmBranch) {
		this.rmBranch = rmBranch;
	}

	public String getBranchCode() 
	  { return branchCode; 
	  } 
	  
	  public void setBranchCode(String branchCode) 
	  { this.branchCode = branchCode; 
	  }
	 
	
	
	/*
	 * public int getRmMasterID() { return rmMasterID; } public void
	 * setRmMasterID(int rmMasterID) { this.rmMasterID = rmMasterID; }
	 */
	public String getRmName() {
		return rmName;
	}
	public void setRmName(String rmName) {
		this.rmName = rmName;
	}
	public String getRmEmailID() {
		return rmEmailID;
	}
	public void setRmEmailID(String rmEmailID) {
		this.rmEmailID = rmEmailID;
	}
	public String getRmEmployeeCode() {
		return rmEmployeeCode;
	}
	public void setRmEmployeeCode(String rmEmployeeCode) {
		this.rmEmployeeCode = rmEmployeeCode;
	}
	public BigInteger getRmMobileNumber() {
		return rmMobileNumber;
	}
	public void setRmMobileNumber(BigInteger rmMobileNumber) {
		this.rmMobileNumber = rmMobileNumber;
	}

	
	
	public int getRmBranch() {
		return rmBranch;
	}

	public int getAdvisorID() {
		return advisorID;
	}
	public void setAdvisorID(int advisorID) {
		this.advisorID = advisorID;
	}
	
	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	@Override
	public String toString() {
		return "RmMasterBODTO [ID=" + ID + ", rmName=" + rmName + ", rmEmailID=" + rmEmailID + ", rmEmployeeCode="
				+ rmEmployeeCode + ", rmMobileNumber=" + rmMobileNumber + ", advisorID=" + advisorID + ", branchCode="
				+ branchCode + ", rmBranch=" + rmBranch + ", errorString=" + errorString + ", rmBranchName="
				+ rmBranchName + ", rmMasterId=" + rmMasterId + ", supervisorId=" + supervisorId + "]";
	}

	
	

}
