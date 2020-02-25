package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;



public class ClientOtherAlternateAssetDTO {
	
	private int id;
	@NotNull(message = "financialAssetType must not be null")
	private byte financialAssetType;
	@NotNull(message = "fundDescription must not be null")
	private String fundDescription;
	@NotNull(message = "schemeName must not be null")
	private String schemeName;
	private byte assetTypeId;
	@NotNull(message = "totalInvestmentAmount must not be null")
	@DecimalMin(value="0.01", message="totalInvestmentAmount cannot be 0")
	private BigDecimal totalInvestmentAmount;
	@NotNull(message = "totalDrawdownAmount must not be null")
	@DecimalMin(value="0.01", message="totalDrawdownAmount cannot be 0")
	private BigDecimal totalDrawdownAmount;
	@NotNull(message = "outstandingAmount must not be null")
	@DecimalMin(value="0.01", message="outstandingAmount cannot be 0")
	private BigDecimal outstandingAmount;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	@NotNull(message = "investmentDate must not be null")
	private Date investmentDate;
	//@NotNull(message = "totalInterestReceived must not be null")
	//@DecimalMin(value="0.01", message="totalInterestReceived cannot be 0")
	private BigDecimal totalInterestReceived;
	//@NotNull(message = "totalPrincipalReceived must not be null")
	//@DecimalMin(value="0.01", message="totalPrincipalReceived cannot be 0")
	private BigDecimal totalPrincipalReceived;
	private String closeEndedFlag;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone="IST")
	//@NotNull(message = "maturityDate must not be null")
	private Date maturityDate;
	@NotNull(message = "currentMarketValue must not be null")
	@DecimalMin(value="0.01", message="currentMarketValue cannot be 0")
	private BigDecimal currentMarketValue;
	private int clientID;
	private int familyMemberID;
	private String financialAssetName;
	private String ownerName;
	
	public ClientOtherAlternateAssetDTO() {
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

	public String getFundDescription() {
		return fundDescription;
	}

	public void setFundDescription(String fundDescription) {
		this.fundDescription = fundDescription;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public byte getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(byte assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public BigDecimal getTotalInvestmentAmount() {
		return totalInvestmentAmount;
	}

	public void setTotalInvestmentAmount(BigDecimal totalInvestmentAmount) {
		this.totalInvestmentAmount = totalInvestmentAmount;
	}

	public BigDecimal getTotalDrawdownAmount() {
		return totalDrawdownAmount;
	}

	public void setTotalDrawdownAmount(BigDecimal totalDrawdownAmount) {
		this.totalDrawdownAmount = totalDrawdownAmount;
	}

	public BigDecimal getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(BigDecimal outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	public Date getInvestmentDate() {
		return investmentDate;
	}

	public void setInvestmentDate(Date investmentDate) {
		this.investmentDate = investmentDate;
	}

	public BigDecimal getTotalInterestReceived() {
		return totalInterestReceived;
	}

	public void setTotalInterestReceived(BigDecimal totalInterestReceived) {
		this.totalInterestReceived = totalInterestReceived;
	}

	public BigDecimal getTotalPrincipalReceived() {
		return totalPrincipalReceived;
	}

	public void setTotalPrincipalReceived(BigDecimal totalPrincipalReceived) {
		this.totalPrincipalReceived = totalPrincipalReceived;
	}

	public String getCloseEndedFlag() {
		return closeEndedFlag;
	}

	public void setCloseEndedFlag(String closeEndedFlag) {
		this.closeEndedFlag = closeEndedFlag;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public BigDecimal getCurrentMarketValue() {
		return currentMarketValue;
	}

	public void setCurrentMarketValue(BigDecimal currentMarketValue) {
		this.currentMarketValue = currentMarketValue;
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
		return "ClientOtherAlternateAssetDTO [id=" + id + ", financialAssetType=" + financialAssetType
				+ ", fundDescription=" + fundDescription + ", schemeName=" + schemeName + ", assetTypeId=" + assetTypeId
				+ ", totalInvestmentAmount=" + totalInvestmentAmount + ", totalDrawdownAmount=" + totalDrawdownAmount
				+ ", outstandingAmount=" + outstandingAmount + ", investmentDate=" + investmentDate
				+ ", totalInterestReceived=" + totalInterestReceived + ", totalPrincipalReceived="
				+ totalPrincipalReceived + ", closeEndedFlag=" + closeEndedFlag + ", maturityDate=" + maturityDate
				+ ", currentMarketValue=" + currentMarketValue + ", clientID=" + clientID + ", familyMemberID="
				+ familyMemberID + ", financialAssetName=" + financialAssetName + ", ownerName=" + ownerName + "]";
	}

	
	
	
}
