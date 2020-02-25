package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientNonlifeInsuranceDTO {

	private int id;
	// Bug Fix CIUAT-271 Company Name and Policy Type foreign keys changed to point to respective master tables in DB
	//private int companyId;
	private Integer companyNameID;
	private String insuranceType;
	private List<Integer> checkedFamilyMemberID=new ArrayList<>();
	//private int policyNameID;
	private String policyName;
	//End Bug Fix CIUAT-271	
	@NotNull(message = "insurancePolicyTypeID must not be null")
	@Min(value=1,message = "insurancePolicyTypeID must not be 0")
	private byte insurancePolicyTypeID;
	private String lookupPolicyTypeDesc;	
	private String otherPolicyType;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	@NotNull(message = "policyEndDate must not be null")
	private Date policyEndDate;
	@NotNull(message = "policyNumber must not be null")
	private String policyNumber;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	@NotNull(message = "policyStartDate must not be null")
	private Date policyStartDate;
	@NotNull(message = "premiumAmount must not be null")
	@DecimalMin(value="0.01", message="premiumAmount cannot be 0")
	private BigDecimal premiumAmount;
	@NotNull(message = "sumInsured must not be null")
	@DecimalMin(value="0.01", message="sumInsured cannot be 0")
	private BigDecimal sumInsured;
	private int familyMemberID;
	private int clientID;
	//private int checkedFamilyMemberID;
	@NotNull(message = "insuranceTypeID must not be null")
	@Min(value=1,message = "insuranceTypeID must not be 0")
	private byte insuranceTypeID;
	// private String policyTypeFlag;
	private String ownerName;
	private String insuranceCompanyName;
	private String coverFlag;
	private String clientName;

	public ClientNonlifeInsuranceDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Date getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(Date policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
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

	public BigDecimal getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(BigDecimal sumInsured) {
		this.sumInsured = sumInsured;
	}

	public int getFamilyMemberID() {
		return familyMemberID;
	}

	public void setFamilyMemberID(int familyMemberID) {
		this.familyMemberID = familyMemberID;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
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

	public byte getInsuranceTypeID() {
		return insuranceTypeID;
	}
	
	public void setInsuranceTypeID(byte insuranceTypeID) {
		this.insuranceTypeID = insuranceTypeID;
	}
	
	//Bug Fix CIUAT-271
	
	/*
	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	*/
	public Integer getCompanyNameID() {
		return companyNameID;
	}

	public void setCompanyNameID(Integer companyNameID) {
		this.companyNameID = companyNameID;
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
	//End Bug Fix CIUAT-271

	public String getLookupPolicyTypeDesc() {
		return lookupPolicyTypeDesc;
	}

	public void setLookupPolicyTypeDesc(String lookupPolicyTypeDesc) {
		this.lookupPolicyTypeDesc = lookupPolicyTypeDesc;
	}

	public String getCoverFlag() {
		return coverFlag;
	}

	public void setCoverFlag(String coverFlag) {
		this.coverFlag = coverFlag;
	}

	public List<Integer> getCheckedFamilyMemberID() {
		return checkedFamilyMemberID;
	}

	public void setCheckedFamilyMemberID(List<Integer> checkedFamilyMemberID) {
		this.checkedFamilyMemberID = checkedFamilyMemberID;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}


	
	
}