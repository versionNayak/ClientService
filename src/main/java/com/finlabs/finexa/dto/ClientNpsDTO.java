package com.finlabs.finexa.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ClientNpsDTO {
	private int id;

	private Integer clientID;

	private Integer familyMemberID;
	@NotNull(message = "financialAssetType must not be null")
	private Byte financialAssetType;
	/*@NotNull(message = "npsCurrentBalance must not be null")
	@DecimalMin(value="0.01", message="npsCurrentBalance cannot be 0")*/
	private BigDecimal npsCurrentBalance;
	/*@NotNull(message = "employeeContribution must not be null")
	@DecimalMin(value="0.01", message="employeeContribution cannot be 0")*/
	private BigDecimal employeeContribution;
	/*@NotNull(message = "employeeContributionFrequency must not be null")
	@Min(value=1,message = "employeeContributionFrequency must not be 0")*/
	private Byte employeeContributionFrequency;
	/*@NotNull(message = "employerContribution must not be null")
	@DecimalMin(value="0.01", message="employerContribution cannot be 0")*/
	private BigDecimal employerContribution;
	/*@NotNull(message = "employerContributionFrequency must not be null")
	@Min(value=1,message = "employerContributionFrequency must not be 0")*/
	private Byte employerContributionFrequency;
	/*@NotNull(message = "expectedAnnualIncrease must not be null")
	@DecimalMin(value="0.01", message="expectedAnnualIncrease cannot be 0")*/
	private BigDecimal expectedAnnualIncrease;
/*	@NotNull(message = "planType must not be null")*/
	private Byte planType;
	/*@DecimalMin(value="0.01", message="assetClassEAllocation cannot be 0")*/
	private BigDecimal assetClassEAllocation;
	/*@DecimalMin(value="0.01", message="assetClassCAllocation cannot be 0")*/
	private BigDecimal assetClassCAllocation;
	/*@DecimalMin(value="0.01", message="assetClassGAllocation cannot be 0")*/
	private BigDecimal assetClassGAllocation;
	//@NotNull(message = "assetClassEReturns must not be null")
	//@DecimalMin(value="0.01", message="assetClassEReturns cannot be 0")
	private BigDecimal assetClassEReturns;
	//@NotNull(message = "assetClassCReturns must not be null")
	//@DecimalMin(value="0.01", message="assetClassCReturns cannot be 0")
	private BigDecimal assetClassCReturns;
	//@NotNull(message = "assetClassGReturns must not be null")
	//@DecimalMin(value="0.01", message="assetClassGReturns cannot be 0")
	private BigDecimal assetClassGReturns;
	//@NotNull(message = "autoPlanReturns must not be null")
	//@DecimalMin(value="0.01", message="autoPlanReturns cannot be 0")
	private BigDecimal autoPlanReturns;
	/*@NotNull(message = "employeeContributionUptoAge must not be null")
	@Min(value=1,message = "employeeContributionUptoAge must not be 0")*/
	private Byte employeeContributionUptoAge;
	/*@NotNull(message = "employerContributionUptoAge must not be null")
	@Min(value=1,message = "employerContributionUptoAge must not be 0")*/
	private Byte employerContributionUptoAge;
	private String firstName;
	private byte relationId;
	private String relationName;
	private String gender;

	public ClientNpsDTO() {
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Integer getClientID() {
		return clientID;
	}


	public void setClientID(Integer clientID) {
		this.clientID = clientID;
	}


	public Integer getFamilyMemberID() {
		return familyMemberID;
	}


	public void setFamilyMemberID(Integer familyMemberID) {
		this.familyMemberID = familyMemberID;
	}


	public Byte getFinancialAssetType() {
		return financialAssetType;
	}


	public void setFinancialAssetType(Byte financialAssetType) {
		this.financialAssetType = financialAssetType;
	}


	public BigDecimal getNpsCurrentBalance() {
		return npsCurrentBalance;
	}


	public void setNpsCurrentBalance(BigDecimal npsCurrentBalance) {
		this.npsCurrentBalance = npsCurrentBalance;
	}


	public BigDecimal getEmployeeContribution() {
		return employeeContribution;
	}


	public void setEmployeeContribution(BigDecimal employeeContribution) {
		this.employeeContribution = employeeContribution;
	}


	public Byte getEmployeeContributionFrequency() {
		return employeeContributionFrequency;
	}


	public void setEmployeeContributionFrequency(Byte employeeContributionFrequency) {
		this.employeeContributionFrequency = employeeContributionFrequency;
	}


	public BigDecimal getEmployerContribution() {
		return employerContribution;
	}


	public void setEmployerContribution(BigDecimal employerContribution) {
		this.employerContribution = employerContribution;
	}


	public Byte getEmployerContributionFrequency() {
		return employerContributionFrequency;
	}


	public void setEmployerContributionFrequency(Byte employerContributionFrequency) {
		this.employerContributionFrequency = employerContributionFrequency;
	}


	public BigDecimal getExpectedAnnualIncrease() {
		return expectedAnnualIncrease;
	}


	public void setExpectedAnnualIncrease(BigDecimal expectedAnnualIncrease) {
		this.expectedAnnualIncrease = expectedAnnualIncrease;
	}


	public Byte getPlanType() {
		return planType;
	}


	public void setPlanType(Byte planType) {
		this.planType = planType;
	}


	public BigDecimal getAssetClassEAllocation() {
		return assetClassEAllocation;
	}


	public void setAssetClassEAllocation(BigDecimal assetClassEAllocation) {
		this.assetClassEAllocation = assetClassEAllocation;
	}


	public BigDecimal getAssetClassCAllocation() {
		return assetClassCAllocation;
	}


	public void setAssetClassCAllocation(BigDecimal assetClassCAllocation) {
		this.assetClassCAllocation = assetClassCAllocation;
	}


	public BigDecimal getAssetClassGAllocation() {
		return assetClassGAllocation;
	}


	public void setAssetClassGAllocation(BigDecimal assetClassGAllocation) {
		this.assetClassGAllocation = assetClassGAllocation;
	}


	public BigDecimal getAssetClassEReturns() {
		return assetClassEReturns;
	}


	public void setAssetClassEReturns(BigDecimal assetClassEReturns) {
		this.assetClassEReturns = assetClassEReturns;
	}


	public BigDecimal getAssetClassCReturns() {
		return assetClassCReturns;
	}


	public void setAssetClassCReturns(BigDecimal assetClassCReturns) {
		this.assetClassCReturns = assetClassCReturns;
	}


	public BigDecimal getAssetClassGReturns() {
		return assetClassGReturns;
	}


	public void setAssetClassGReturns(BigDecimal assetClassGReturns) {
		this.assetClassGReturns = assetClassGReturns;
	}


	public BigDecimal getAutoPlanReturns() {
		return autoPlanReturns;
	}


	public void setAutoPlanReturns(BigDecimal autoPlanReturns) {
		this.autoPlanReturns = autoPlanReturns;
	}


	public Byte getEmployeeContributionUptoAge() {
		return employeeContributionUptoAge;
	}


	public void setEmployeeContributionUptoAge(Byte employeeContributionUptoAge) {
		this.employeeContributionUptoAge = employeeContributionUptoAge;
	}


	public Byte getEmployerContributionUptoAge() {
		return employerContributionUptoAge;
	}


	public void setEmployerContributionUptoAge(Byte employerContributionUptoAge) {
		this.employerContributionUptoAge = employerContributionUptoAge;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public byte getRelationId() {
		return relationId;
	}


	public void setRelationId(byte relationId) {
		this.relationId = relationId;
	}


	public String getRelationName() {
		return relationName;
	}


	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	@Override
	public String toString() {
		return "ClientNpsDTO [id=" + id + ", clientID=" + clientID + ", familyMemberID=" + familyMemberID
				+ ", financialAssetType=" + financialAssetType + ", npsCurrentBalance=" + npsCurrentBalance
				+ ", employeeContribution=" + employeeContribution + ", employeeContributionFrequency="
				+ employeeContributionFrequency + ", employerContribution=" + employerContribution
				+ ", employerContributionFrequency=" + employerContributionFrequency + ", expectedAnnualIncrease="
				+ expectedAnnualIncrease + ", planType=" + planType + ", assetClassEAllocation=" + assetClassEAllocation
				+ ", assetClassCAllocation=" + assetClassCAllocation + ", assetClassGAllocation="
				+ assetClassGAllocation + ", assetClassEReturns=" + assetClassEReturns + ", assetClassCReturns="
				+ assetClassCReturns + ", assetClassGReturns=" + assetClassGReturns + ", autoPlanReturns="
				+ autoPlanReturns + ", employeeContributionUptoAge=" + employeeContributionUptoAge
				+ ", employerContributionUptoAge=" + employerContributionUptoAge + ", firstName=" + firstName
				+ ", relationId=" + relationId + ", relationName=" + relationName + ", gender=" + gender + "]";
	}





	

}