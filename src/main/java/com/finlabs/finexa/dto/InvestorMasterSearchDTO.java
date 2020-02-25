package com.finlabs.finexa.dto;

public class InvestorMasterSearchDTO {
	
	private int advisorUser;
	
	private String investorPan;
	
	private String investorName;
	
	private String investorFolioNo;
	
	private String investorDOB;
	
	private String investorGender;
	
	private String investorMaritalStatus;
	
	private String investorAdhaar;
	
	private String investorAddressLine1;
	
	private String investorAdressLine2;
	
	private String investorAddressLine3;
	
	private String investorCity;
	
	private String investorState;
	
	private String investorPinCode;
	
	private String investorPinCountry;

	private boolean isFamilyHead;
	
	private String investorEmail;
	
	private String investorMobile;
	
	private String distributorARNCode;
	
	private int clientId;
	
	private int relationId;
	
	private int accountStatus;

	public String getInvestorPan() {
		return investorPan;
	}

	public void setInvestorPan(String investorPan) {
		this.investorPan = investorPan;
	}

	public String getInvestorName() {
		return investorName;
	}

	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}

	public String getInvestorFolioNo() {
		return investorFolioNo;
	}

	public void setInvestorFolioNo(String investorFolioNo) {
		this.investorFolioNo = investorFolioNo;
	}

	public String getInvestorDOB() {
		return investorDOB;
	}

	public void setInvestorDOB(String investorDOB) {
		this.investorDOB = investorDOB;
	}

	public String getInvestorGender() {
		return investorGender;
	}

	public void setInvestorGender(String investorGender) {
		this.investorGender = investorGender;
	}

	public String getInvestorMaritalStatus() {
		return investorMaritalStatus;
	}

	public void setInvestorMaritalStatus(String investorMaritalStatus) {
		this.investorMaritalStatus = investorMaritalStatus;
	}

	public String getInvestorAdhaar() {
		return investorAdhaar;
	}

	public void setInvestorAdhaar(String investorAdhaar) {
		this.investorAdhaar = investorAdhaar;
	}

	public String getInvestorAddressLine1() {
		return investorAddressLine1;
	}

	public void setInvestorAddressLine1(String investorAddressLine1) {
		this.investorAddressLine1 = investorAddressLine1;
	}

	public String getInvestorAdressLine2() {
		return investorAdressLine2;
	}

	public void setInvestorAdressLine2(String investorAdressLine2) {
		this.investorAdressLine2 = investorAdressLine2;
	}

	public String getInvestorAddressLine3() {
		return investorAddressLine3;
	}

	public void setInvestorAddressLine3(String investorAddressLine3) {
		this.investorAddressLine3 = investorAddressLine3;
	}

	public String getInvestorCity() {
		return investorCity;
	}

	public void setInvestorCity(String investorCity) {
		this.investorCity = investorCity;
	}

	public String getInvestorState() {
		return investorState;
	}

	public void setInvestorState(String investorState) {
		this.investorState = investorState;
	}

	public String getInvestorPinCode() {
		return investorPinCode;
	}

	public void setInvestorPinCode(String investorPinCode) {
		this.investorPinCode = investorPinCode;
	}

	public String getInvestorPinCountry() {
		return investorPinCountry;
	}

	public void setInvestorPinCountry(String investorPinCountry) {
		this.investorPinCountry = investorPinCountry;
	}

	public boolean isFamilyHead() {
		return isFamilyHead;
	}

	public void setFamilyHead(boolean isFamilyHead) {
		this.isFamilyHead = isFamilyHead;
	}

	public int getAdvisorUser() {
		return advisorUser;
	}

	public void setAdvisorUser(int advisorUser) {
		this.advisorUser = advisorUser;
	}

	public String getInvestorEmail() {
		return investorEmail;
	}

	public void setInvestorEmail(String investorEmail) {
		this.investorEmail = investorEmail;
	}

	public String getInvestorMobile() {
		return investorMobile;
	}

	public void setInvestorMobile(String investorMobile) {
		this.investorMobile = investorMobile;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getRelationId() {
		return relationId;
	}

	public void setRelationId(int relationId) {
		this.relationId = relationId;
	}

	
	public int getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(int accountStatus) {
		this.accountStatus = accountStatus;
	}

	
	public String getDistributorARNCode() {
		return distributorARNCode;
	}

	public void setDistributorARNCode(String distributorARNCode) {
		this.distributorARNCode = distributorARNCode;
	}

	@Override
	public String toString() {
		return "InvestorMasterSearchDTO [advisorUser=" + advisorUser + ", investorPan=" + investorPan
				+ ", investorName=" + investorName + ", investorFolioNo=" + investorFolioNo + ", investorDOB="
				+ investorDOB + ", investorGender=" + investorGender + ", investorMaritalStatus="
				+ investorMaritalStatus + ", investorAdhaar=" + investorAdhaar + ", investorAddressLine1="
				+ investorAddressLine1 + ", investorAdressLine2=" + investorAdressLine2 + ", investorAddressLine3="
				+ investorAddressLine3 + ", investorCity=" + investorCity + ", investorState=" + investorState
				+ ", investorPinCode=" + investorPinCode + ", investorPinCountry=" + investorPinCountry
				+ ", isFamilyHead=" + isFamilyHead + ", investorEmail=" + investorEmail + ", investorMobile="
				+ investorMobile + ", distributorARNCode=" + distributorARNCode + ", clientId=" + clientId
				+ ", relationId=" + relationId + ", accountStatus=" + accountStatus + "]";
	}

	
	
	
	
}
