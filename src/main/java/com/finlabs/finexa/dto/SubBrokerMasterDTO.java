package com.finlabs.finexa.dto;

import java.math.BigInteger;

import javax.validation.constraints.NotNull;

import java.math.BigInteger;

public class SubBrokerMasterDTO {
	
	private int ID;
	private int subBrokerMasterID;
	private String sbName;
	private String sbEmailID;
	private String sbEmployeeCode;
	private BigInteger sbMobileNumber;
	private int sbBranch;
	private int advisorID;
	private String branchName;
	private int supervisorId;
	//private int id;


	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getSubBrokerMasterID() {
		return subBrokerMasterID;
	}
	public void setSubBrokerMasterID(int subBrokerMasterID) {
		this.subBrokerMasterID = subBrokerMasterID;
	}
	public String getSbName() {
		return sbName;
	}
	public void setSbName(String sbName) {
		this.sbName = sbName;
	}
	public String getSbEmailID() {
		return sbEmailID;
	}
	public void setSbEmailID(String sbEmailID) {
		this.sbEmailID = sbEmailID;
	}
	public String getSbEmployeeCode() {
		return sbEmployeeCode;
	}
	public void setSbEmployeeCode(String sbEmployeeCode) {
		this.sbEmployeeCode = sbEmployeeCode;
	}
	public BigInteger getSbMobileNumber() {
		return sbMobileNumber;
	}
	public void setSbMobileNumber(BigInteger sbMobileNumber) {
		this.sbMobileNumber = sbMobileNumber;
	}
	public int getSbBranch() {
		return sbBranch;
	}
	public void setSbBranch(int sbBranch) {
		this.sbBranch = sbBranch;
	}
	public int getAdvisorID() {
		return advisorID;
	}
	public void setAdvisorID(int advisorID) {
		this.advisorID = advisorID;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public int getSupervisorId() {
		return supervisorId;
	}
	public void setSupervisorId(int supervisorId) {
		this.supervisorId = supervisorId;
	}
	
	
	@Override
	public String toString() {
		return "SubBrokerMasterDTO [subBrokerMasterID=" + subBrokerMasterID + ", sbName=" + sbName + ", sbEmailID="
				+ sbEmailID + ", sbEmployeeCode=" + sbEmployeeCode + ", sbMobileNumber=" + sbMobileNumber
				+ ", sbBranch=" + sbBranch + ", advisorID=" + advisorID + ", supervisorId=" + supervisorId + ", branchName=" + branchName + "]";
	}

	
	
		
	
}

