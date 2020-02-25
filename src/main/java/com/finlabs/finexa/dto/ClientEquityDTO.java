package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientEquityDTO {

	private int id;

	private int clientID;

	private byte createdBy;

	private String ownerName;

	private Timestamp createdOn;
	@NotNull(message = "currentMarketValue must not be null")
	@DecimalMin(value = "0.01", message = "currentMarketValue cannot be 0")
	private BigDecimal currentMarketValue;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date esopVestingDate;

	private int familyMemberId;

	private BigDecimal investmentAmount;

	private String isin;

	private byte lastUpdateddBy;

	private Timestamp lastUpdatedOn;
	@NotNull(message = "listedFlag must not be null")
	private String listedFlag;
	@NotNull(message = "financialAssetType must not be null")
	private byte financialAssetType;

	private String financialAssetTypeName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	@NotNull(message = "purchaseDate must not be null")
	private Date purchaseDate;
	
	private Integer quantity;

	private String unlistedStockNameText;

	private String unlistedStockNameList;

	private String errorMessage;

	public ClientEquityDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public byte getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(byte createdBy) {
		this.createdBy = createdBy;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public BigDecimal getCurrentMarketValue() {
		return currentMarketValue;
	}

	public void setCurrentMarketValue(BigDecimal currentMarketValue) {
		this.currentMarketValue = currentMarketValue;
	}

	public Date getEsopVestingDate() {
		return esopVestingDate;
	}

	public void setEsopVestingDate(Date esopVestingDate) {
		this.esopVestingDate = esopVestingDate;
	}

	public int getFamilyMemberId() {
		return familyMemberId;
	}

	public void setFamilyMemberId(int familyMemberId) {
		this.familyMemberId = familyMemberId;
	}

	public BigDecimal getInvestmentAmount() {
		return investmentAmount;
	}

	public void setInvestmentAmount(BigDecimal investmentAmount) {
		this.investmentAmount = investmentAmount;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
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

	public String getListedFlag() {
		return listedFlag;
	}

	public void setListedFlag(String listedFlag) {
		this.listedFlag = listedFlag;
	}

	public byte getFinancialAssetType() {
		return financialAssetType;
	}

	public void setFinancialAssetType(byte financialAssetType) {
		this.financialAssetType = financialAssetType;
	}

	public String getFinancialAssetTypeName() {
		return financialAssetTypeName;
	}

	public void setFinancialAssetTypeName(String financialAssetTypeName) {
		this.financialAssetTypeName = financialAssetTypeName;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getUnlistedStockNameText() {
		return unlistedStockNameText;
	}

	public void setUnlistedStockNameText(String unlistedStockNameText) {
		this.unlistedStockNameText = unlistedStockNameText;
	}

	public String getUnlistedStockNameList() {
		return unlistedStockNameList;
	}

	public void setUnlistedStockNameList(String unlistedStockNameList) {
		this.unlistedStockNameList = unlistedStockNameList;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "ClientEquityDTO [id=" + id + ", clientID=" + clientID + ", createdBy=" + createdBy + ", ownerName="
				+ ownerName + ", createdOn=" + createdOn + ", currentMarketValue=" + currentMarketValue
				+ ", esopVestingDate=" + esopVestingDate + ", familyMemberId=" + familyMemberId + ", investmentAmount="
				+ investmentAmount + ", isin=" + isin + ", lastUpdateddBy=" + lastUpdateddBy + ", lastUpdatedOn="
				+ lastUpdatedOn + ", listedFlag=" + listedFlag + ", financialAssetType=" + financialAssetType
				+ ", financialAssetTypeName=" + financialAssetTypeName + ", purchaseDate=" + purchaseDate
				+ ", quantity=" + quantity + ", unlistedStockNameText=" + unlistedStockNameText
				+ ", unlistedStockNameList=" + unlistedStockNameList + ", errorMessage=" + errorMessage + "]";
	}

}