package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;


import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientAnnuityDTO {
//new
	private int id;
	//@DecimalMin(value="0.01", message="annuityRate cannot be 0")
	private BigDecimal annuityRate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	//@NotNull(message = "annuityStartDate must not be null")
	private Date annuityStartDate;
	@NotNull(message = "financialAssetType must not be null")
	@Min(value=1,message = "financialAssetType must not be 0")
	private Byte financialAssetType;
	@NotNull(message = "annuityType must not be null")
	@Min(value=1,message = "annuityType must not be 0")
	private Byte annuityType;
	private BigDecimal growthRate;
	private Byte payoutFrequency;
	@DecimalMin(value="0.01", message="pensionableCorpus cannot be 0")
	private BigDecimal pensionableCorpus;
	private int clientID;
	@DecimalMin(value="0.01", message="annuityMonthlyBasicDA cannot be 0")
	private BigDecimal annuityMonthlyBasicDA;
	@Min(value=1,message = "annuityServiceYears must not be 0")
	private Byte annuityServiceYears;
	private BigDecimal annuityAnnualContributionIncrease;
	private int clientEPFID;
	private String firstName;
	private byte relationId;
	private String relationName;
	private String gender;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getAnnuityRate() {
		return annuityRate;
	}

	public void setAnnuityRate(BigDecimal annuityRate) {
		this.annuityRate = annuityRate;
	}

	public Date getAnnuityStartDate() {
		return annuityStartDate;
	}

	public void setAnnuityStartDate(Date annuityStartDate) {
		this.annuityStartDate = annuityStartDate;
	}

	public Byte getFinancialAssetType() {
		return financialAssetType;
	}

	public void setFinancialAssetType(Byte financialAssetType) {
		this.financialAssetType = financialAssetType;
	}

	public Byte getAnnuityType() {
		return annuityType;
	}

	public void setAnnuityType(Byte annuityType) {
		this.annuityType = annuityType;
	}

	public BigDecimal getGrowthRate() {
		return growthRate;
	}

	public void setGrowthRate(BigDecimal growthRate) {
		this.growthRate = growthRate;
	}

	public Byte getPayoutFrequency() {
		return payoutFrequency;
	}

	public void setPayoutFrequency(Byte payoutFrequency) {
		this.payoutFrequency = payoutFrequency;
	}

	public BigDecimal getPensionableCorpus() {
		return pensionableCorpus;
	}

	public void setPensionableCorpus(BigDecimal pensionableCorpus) {
		this.pensionableCorpus = pensionableCorpus;
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

	public int getClientEPFID() {
		return clientEPFID;
	}

	public void setClientEPFID(int clientEPFID) {
		this.clientEPFID = clientEPFID;
	}

	public BigDecimal getAnnuityMonthlyBasicDA() {
		return annuityMonthlyBasicDA;
	}

	public void setAnnuityMonthlyBasicDA(BigDecimal annuityMonthlyBasicDA) {
		this.annuityMonthlyBasicDA = annuityMonthlyBasicDA;
	}

	public Byte getAnnuityServiceYears() {
		return annuityServiceYears;
	}

	public void setAnnuityServiceYears(Byte annuityServiceYears) {
		this.annuityServiceYears = annuityServiceYears;
	}

	public BigDecimal getAnnuityAnnualContributionIncrease() {
		return annuityAnnualContributionIncrease;
	}

	public void setAnnuityAnnualContributionIncrease(BigDecimal annuityAnnualContributionIncrease) {
		this.annuityAnnualContributionIncrease = annuityAnnualContributionIncrease;
	}

	private int familyMemberID;

	public ClientAnnuityDTO() {
	}

}