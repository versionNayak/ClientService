package com.finlabs.finexa.dto;

import java.math.BigInteger;

public class BranchMasterDetailsBODTO {

	private String branchCode;

	private String branchAddress;

	private String branchCity;

	private int branchHeadId;

	private BigInteger branchMobileNo;
	
	private String branchName;

	private BigInteger branchPhoneNo;

	private int branchPinCode;

	private String branchState;
	
	private String orgName;
	
	private String branchHeadName;
	
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

	public int getBranchHeadId() {
		return branchHeadId;
	}

	public void setBranchHeadId(int branchHeadId) {
		this.branchHeadId = branchHeadId;
	}

	public BigInteger getBranchMobileNo() {
		return branchMobileNo;
	}

	public void setBranchMobileNo(BigInteger branchMobileNo) {
		this.branchMobileNo = branchMobileNo;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public BigInteger getBranchPhoneNo() {
		return branchPhoneNo;
	}

	public void setBranchPhoneNo(BigInteger branchPhoneNo) {
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

		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public String getBranchHeadName() {
		return branchHeadName;
	}

	public void setBranchHeadName(String branchHeadName) {
		this.branchHeadName = branchHeadName;
	}

	@Override
	public String toString() {
		return "BranchMasterDetailsBODTO [branchCode=" + branchCode + ", branchAddress=" + branchAddress
				+ ", branchCity=" + branchCity + ", branchHeadId=" + branchHeadId + ", branchMobileNo=" + branchMobileNo
				+ ", branchName=" + branchName + ", branchPhoneNo=" + branchPhoneNo + ", branchPinCode=" + branchPinCode
				+ ", branchState=" + branchState + ", orgName=" + orgName + ", branchHeadName=" + branchHeadName
				+ ", id=" + id + "]";
	}
	
	
	
}
