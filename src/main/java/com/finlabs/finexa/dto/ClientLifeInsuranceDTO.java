package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientLifeInsuranceDTO {
	private int id;
	private int companyNameID;
	//private byte insuranceTypeID;
	private String insuranceType;
	@NotNull(message = "insurancePolicyTypeID must not be null")
	@Min(value=1,message = "insurancePolicyTypeID must not be 0")
	private byte insurancePolicyTypeID;
	private String lookupPolicyTypeDesc;
	private String otherPolicyType;
	//private int policyNameID;
	private String policyName;
	@NotNull(message = "policyNumber cannot be null")
	private String policyNumber;
	@NotNull(message = "sumInsured must not be null")
	@DecimalMin(value="0.01", message="sumInsured cannot be 0")
	private BigDecimal sumInsured;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	@NotNull(message = "policyStartDate cannot be null")
	private Date policyStartDate;
	@NotNull(message = "premiumAmount must not be null")
	@DecimalMin(value="0.01", message="premiumAmount cannot be 0")
	private BigDecimal premiumAmount;
	@NotNull(message = "premiumFrequency must not be null")
	@Min(value=1,message = "premiumFrequency must not be 0")
	private byte premiumFrequency;
	@NotNull(message = "policyTenure must not be null")
	@Min(value=1,message = "policyTenure must not be 0")
	private byte policyTenure;
	@NotNull(message = "premiumTenure must not be null")
	@Min(value=1,message = "premiumTenure must not be 0")
	private byte premiumTenure;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date endDate;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date lockedUptoDate;
	private BigDecimal currentUnitBalance;
	private int clientID;
	private int familyMemberID;
	private String ownerName;
	private String insuranceCompanyName;
	private String clientName;
	
	public ClientLifeInsuranceDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCompanyNameID() {
		return companyNameID;
	}

	public void setCompanyNameID(int companyNameID) {
		this.companyNameID = companyNameID;
	}

	public byte getInsurancePolicyTypeID() {
		return insurancePolicyTypeID;
	}

	public void setInsurancePolicyTypeID(byte insurancePolicyTypeID) {
		this.insurancePolicyTypeID = insurancePolicyTypeID;
	}

	public String getOtherPolicyType() {
		return otherPolicyType;
	}

	public void setOtherPolicyType(String otherPolicyType) {
		this.otherPolicyType = otherPolicyType;
	}

	/*
	public int getPolicyNameID() {
		return policyNameID;
	}

	public void setPolicyNameID(int policyNameID) {
		this.policyNameID = policyNameID;
	}
	*/

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	
	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public BigDecimal getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(BigDecimal sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Date getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public BigDecimal getPremiumAmount() {
		return premiumAmount;
	}

	public void setPremiumAmount(BigDecimal premiumAmount) {
		this.premiumAmount = premiumAmount;
	}

	public byte getPremiumFrequency() {
		return premiumFrequency;
	}

	public void setPremiumFrequency(byte premiumFrequency) {
		this.premiumFrequency = premiumFrequency;
	}

	public byte getPolicyTenure() {
		return policyTenure;
	}

	public void setPolicyTenure(byte policyTenure) {
		this.policyTenure = policyTenure;
	}

	public byte getPremiumTenure() {
		return premiumTenure;
	}

	public void setPremiumTenure(byte premiumTenure) {
		this.premiumTenure = premiumTenure;
	}

	public Date getLockedUptoDate() {
		return lockedUptoDate;
	}

	public void setLockedUptoDate(Date lockedUptoDate) {
		this.lockedUptoDate = lockedUptoDate;
	}

	public BigDecimal getCurrentUnitBalance() {
		return currentUnitBalance;
	}

	public void setCurrentUnitBalance(BigDecimal currentUnitBalance) {
		this.currentUnitBalance = currentUnitBalance;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public int getFamilyMemberID() {
		return familyMemberID;
	}

	public void setFamilyMemberID(int familyMemberID) {
		this.familyMemberID = familyMemberID;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getInsuranceCompanyName() {
		return insuranceCompanyName;
	}

	public void setInsuranceCompanyName(String insuranceCompanyName) {
		this.insuranceCompanyName = insuranceCompanyName;
	}

	/*
	public byte getInsuranceTypeID() {
		return insuranceTypeID;
	}

	public void setInsuranceTypeID(byte insuranceTypeID) {
		this.insuranceTypeID = insuranceTypeID;
	}
	*/


	public String getLookupPolicyTypeDesc() {
		return lookupPolicyTypeDesc;
	}

	public void setLookupPolicyTypeDesc(String lookupPolicyTypeDesc) {
		this.lookupPolicyTypeDesc = lookupPolicyTypeDesc;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	
	

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "ClientLifeInsuranceDTO [id=" + id + ", companyNameID=" + companyNameID + ", insuranceType="
				+ insuranceType + ", insurancePolicyTypeID=" + insurancePolicyTypeID + ", lookupPolicyTypeDesc="
				+ lookupPolicyTypeDesc + ", otherPolicyType=" + otherPolicyType + ", policyName=" + policyName
				+ ", policyNumber=" + policyNumber + ", sumInsured=" + sumInsured + ", policyStartDate="
				+ policyStartDate + ", premiumAmount=" + premiumAmount + ", premiumFrequency=" + premiumFrequency
				+ ", policyTenure=" + policyTenure + ", premiumTenure=" + premiumTenure + ", endDate=" + endDate
				+ ", lockedUptoDate=" + lockedUptoDate + ", currentUnitBalance=" + currentUnitBalance + ", clientID="
				+ clientID + ", familyMemberID=" + familyMemberID + ", ownerName=" + ownerName
				+ ", insuranceCompanyName=" + insuranceCompanyName + ", clientName=" + clientName + "]";
	}

	

	
	
	
	

}