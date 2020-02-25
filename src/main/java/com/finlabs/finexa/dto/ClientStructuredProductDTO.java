package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientStructuredProductDTO {
	private int id;
	private int clientID;
	private int familyMemberID;
	@NotNull(message = "financialAssetType must not be null")
	private byte financialAssetType;
	private byte createdBy;
	private Timestamp createdOn;
	@NotNull(message = "currentValue must not be null")
	@DecimalMin(value="0.01", message="currentValue cannot be 0")
	private BigDecimal currentValue;
	@NotNull(message = "spDescription must not be null")
	private String spDescription;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	@NotNull(message = "investmentStartDate must not be null")
	private Date investmentStartDate;
	private byte lastUpdateddBy;
	private Timestamp lastUpdatedOn;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	//@NotNull(message = "maturityDate must not be null")
	private Date maturityDate;
	//@NotNull(message = "maturityValue must not be null")
	@DecimalMin(value="0.01", message="maturityValue cannot be 0")
	private BigDecimal maturityValue;
	@NotNull(message = "investmentValue must not be null")
	@DecimalMin(value="0.01", message="investmentValue cannot be 0")
	private BigDecimal investmentValue;
	private String financialAssetName;
	private String ownerName;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public byte getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(byte createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}
	public BigDecimal getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}
	
	public String getSpDescription() {
		return spDescription;
	}
	public void setSpDescription(String spDescription) {
		this.spDescription = spDescription;
	}
	
	public Date getInvestmentStartDate() {
		return investmentStartDate;
	}
	public void setInvestmentStartDate(Date investmentStartDate) {
		this.investmentStartDate = investmentStartDate;
	}
	public byte getLastUpdateddBy() {
		return lastUpdateddBy;
	}
	public void setLastUpdateddBy(byte lastUpdateddBy) {
		this.lastUpdateddBy = lastUpdateddBy;
	}
	public Timestamp getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	public Date getMaturityDate() {
		return maturityDate;
	}
	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}
	public BigDecimal getMaturityValue() {
		return maturityValue;
	}
	public void setMaturityValue(BigDecimal maturityValue) {
		this.maturityValue = maturityValue;
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
	public byte getFinancialAssetType() {
		return financialAssetType;
	}
	public void setFinancialAssetType(byte financialAssetType) {
		this.financialAssetType = financialAssetType;
	}
	public BigDecimal getInvestmentValue() {
		return investmentValue;
	}
	public void setInvestmentValue(BigDecimal investmentValue) {
		this.investmentValue = investmentValue;
	}
	
	public String getFinancialAssetName() {
		return financialAssetName;
	}
	public void setFinancialAssetName(String financialAssetName) {
		this.financialAssetName = financialAssetName;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	@Override
	public String toString() {
		return "ClientStructuredProductDTO [id=" + id + ", clientID=" + clientID + ", familyMemberID=" + familyMemberID
				+ ", financialAssetType=" + financialAssetType + ", createdBy=" + createdBy + ", createdOn=" + createdOn
				+ ", currentValue=" + currentValue + ", spDescription=" + spDescription + ", investmentStartDate="
				+ investmentStartDate + ", lastUpdateddBy=" + lastUpdateddBy + ", lastUpdatedOn=" + lastUpdatedOn
				+ ", maturityDate=" + maturityDate + ", maturityValue=" + maturityValue + ", investmentValue="
				+ investmentValue + ", financialAssetName=" + financialAssetName + ", ownerName=" + ownerName + "]";
	}

	
	
	
}
