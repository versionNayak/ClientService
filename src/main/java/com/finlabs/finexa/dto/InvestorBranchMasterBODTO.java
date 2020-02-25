package com.finlabs.finexa.dto;

public class InvestorBranchMasterBODTO {

	private String branchCode;

	private String branchAddress;

	private String branchCity;

	private String branchHead;

	private int branchMobileNo;

	private String branchName;

	private int branchPhoneNo;

	private int branchPinCode;

	private String branchState;
	
	private int advisorId;
	
	private int id;

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public String getBranchCity() {
		return branchCity;
	}

	public void setBranchCity(String branchCity) {
		this.branchCity = branchCity;
	}

	public String getBranchHead() {
		return branchHead;
	}

	public void setBranchHead(String branchHead) {
		this.branchHead = branchHead;
	}

	public int getBranchMobileNo() {
		return branchMobileNo;
	}

	public void setBranchMobileNo(int branchMobileNo) {
		this.branchMobileNo = branchMobileNo;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public int getBranchPhoneNo() {
		return branchPhoneNo;
	}

	public void setBranchPhoneNo(int branchPhoneNo) {
		this.branchPhoneNo = branchPhoneNo;
	}

	public int getBranchPinCode() {
		return branchPinCode;
	}

	public void setBranchPinCode(int branchPinCode) {
		this.branchPinCode = branchPinCode;
	}

	public String getBranchState() {
		return branchState;
	}

	public void setBranchState(String branchState) {
		this.branchState = branchState;
	}

	public int getAdvisorId() {
		return advisorId;
	}
	
	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "AdvisorDTO [code=" + branchCode + ", branchName=" + branchName + ", city=" + branchCity + ", branchAddress=" + branchAddress
				+ ", branchHead=" + branchHead + ", advisorId=" + advisorId + ", branchState=" + branchState
				+ ", branchPinCode=" + branchPinCode + ", branchPhoneNo=" + branchPhoneNo + ", branchMobileNo=" + branchMobileNo + "]";
	}
}
