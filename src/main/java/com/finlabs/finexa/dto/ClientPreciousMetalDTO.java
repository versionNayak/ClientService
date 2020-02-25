package com.finlabs.finexa.dto;


import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;


public class ClientPreciousMetalDTO {

	private int id;
	@NotNull(message = "financialAssetType must not be null")
	private byte financialAssetType;
	@NotNull(message = "assetTypeId must not be null")
	@Min(value=1,message = "assetTypeId must not be 0")
	private byte assetTypeId;
	@NotNull(message = "currentValue must not be null")
	@DecimalMin(value="0.01", message="currentValue cannot be 0")
    private BigDecimal currentValue;
	@NotNull(message = "pmDescription must not be null")
    private String pmDescription;
    private int clientID;
    private int familyMemberID;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	@NotNull(message = "investmentDate must not be null")
	private Date investmentDate;
	@NotNull(message = "investmentValue must not be null")
	@DecimalMin(value="0.01", message="investmentValue cannot be 0")
    private BigDecimal investmentValue;
    private String financialAssetName;
    private String ownerName;
    private String assetTypeName;

	public ClientPreciousMetalDTO() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public byte getFinancialAssetType() {
		return financialAssetType;
	}


	public void setFinancialAssetType(byte financialAssetType) {
		this.financialAssetType = financialAssetType;
	}


	public byte getAssetTypeId() {
		return assetTypeId;
	}


	public void setAssetTypeId(byte assetTypeId) {
		this.assetTypeId = assetTypeId;
	}


	public BigDecimal getCurrentValue() {
		return currentValue;
	}


	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}


	public String getPmDescription() {
		return pmDescription;
	}


	public void setPmDescription(String pmDescription) {
		this.pmDescription = pmDescription;
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


	public Date getInvestmentDate() {
		return investmentDate;
	}


	public void setInvestmentDate(Date investmentDate) {
		this.investmentDate = investmentDate;
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


	public String getAssetTypeName() {
		return assetTypeName;
	}


	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}


	@Override
	public String toString() {
		return "ClientPreciousMetalDTO [id=" + id + ", financialAssetType=" + financialAssetType + ", assetTypeId="
				+ assetTypeId + ", currentValue=" + currentValue + ", pmDescription=" + pmDescription + ", clientID="
				+ clientID + ", familyMemberID=" + familyMemberID + ", investmentDate=" + investmentDate
				+ ", investmentValue=" + investmentValue + ", financialAssetName=" + financialAssetName + ", ownerName="
				+ ownerName + ", assetTypeName=" + assetTypeName + "]";
	}


	


	


}