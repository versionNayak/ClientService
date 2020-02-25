package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

    public class ClientFixedIncomeDTO {
	
	private int id;	
	private Integer clientID;	
	private Integer familyMemberID;	
	private String ownerName;
	@NotNull(message = "financialAssetType must not be null")
	private byte financialAssetType;
	private String financialAssetTypeName;
	/*@NotNull(message = "bankIssuerId must not be null")*/
	/*@Min(value=1,message = "bankIssuerId must not be 0")
	private Integer bankIssuerId;	
    */	
	private String bankIssuerName;
	private byte fixedDepositType;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	@NotNull(message = "investmentDepositDate must not be null")
	private Date investmentDepositDate;	
	@NotNull(message = "amount must not be null")
	@DecimalMin(value="0.01", message="amount cannot be 0")
	private BigDecimal amount;	
	//@NotNull(message = "interestCouponRate must not be null")
	//@DecimalMin(value="0.01", message="interestCouponRate cannot be 0")
	private BigDecimal interestCouponRate;	
	private byte recurringDepositFrequency;	
	private byte compoundingFrequency;	
	@NotNull(message = "tenureYearsDays must not be null")
	private String tenureYearsDays;	
	private Integer tenureD;
	private Integer tenure;	
	private Integer tenureRDMonths;	
	private byte payoutFrequency;	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	//@NotNull(message = "maturityDate must not be null")
	private Date maturityDate;	
	private Integer bondPurchased;	
	private BigDecimal bondFaceValue;	
	private byte bondType;	
	private BigDecimal bondCurrentYield;	
	private Integer createdBy;	
	private Timestamp createdOn;	
	private Integer lastUpdateddBy;	
	private Timestamp lastUpdatedOn;

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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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

	

	/*public Integer getBankIssuerId() {
		return bankIssuerId;
	}

	public void setBankIssuerId(Integer bankIssuerId) {
		this.bankIssuerId = bankIssuerId;
	}*/

	public String getBankIssuerName() {
		return bankIssuerName;
	}

	public void setBankIssuerName(String bankIssuerName) {
		this.bankIssuerName = bankIssuerName;
	}

	public byte getFixedDepositType() {
		return fixedDepositType;
	}

	public void setFixedDepositType(byte fixedDepositType) {
		this.fixedDepositType = fixedDepositType;
	}

	public Date getInvestmentDepositDate() {
		return investmentDepositDate;
	}

	public void setInvestmentDepositDate(Date investmentDepositDate) {
		this.investmentDepositDate = investmentDepositDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	
	public BigDecimal getInterestCouponRate() {
		return interestCouponRate;
	}

	public void setInterestCouponRate(BigDecimal interestCouponRate) {
		this.interestCouponRate = interestCouponRate;
	}

	public String getTenureYearsDays() {
		return tenureYearsDays;
	}

	public void setTenureYearsDays(String tenureYearsDays) {
		this.tenureYearsDays = tenureYearsDays;
	}

	public Integer getTenure() {
		return tenure;
	}

	public void setTenure(Integer tenure) {
		this.tenure = tenure;
	}

	public Integer getTenureRDMonths() {
		return tenureRDMonths;
	}

	public void setTenureRDMonths(Integer tenureRDMonths) {
		this.tenureRDMonths = tenureRDMonths;
	}

	

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public Integer getBondPurchased() {
		return bondPurchased;
	}

	public void setBondPurchased(Integer bondPurchased) {
		this.bondPurchased = bondPurchased;
	}

	public BigDecimal getBondFaceValue() {
		return bondFaceValue;
	}

	public void setBondFaceValue(BigDecimal bondFaceValue) {
		this.bondFaceValue = bondFaceValue;
	}

	public byte getBondType() {
		return bondType;
	}

	public void setBondType(byte bondType) {
		this.bondType = bondType;
	}

	public BigDecimal getBondCurrentYield() {
		return bondCurrentYield;
	}

	public void setBondCurrentYield(BigDecimal bondCurrentYield) {
		this.bondCurrentYield = bondCurrentYield;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getLastUpdateddBy() {
		return lastUpdateddBy;
	}

	public void setLastUpdateddBy(Integer lastUpdateddBy) {
		this.lastUpdateddBy = lastUpdateddBy;
	}

	public Timestamp getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public byte getRecurringDepositFrequency() {
		return recurringDepositFrequency;
	}

	public void setRecurringDepositFrequency(byte recurringDepositFrequency) {
		this.recurringDepositFrequency = recurringDepositFrequency;
	}

	public byte getCompoundingFrequency() {
		return compoundingFrequency;
	}

	public void setCompoundingFrequency(byte compoundingFrequency) {
		this.compoundingFrequency = compoundingFrequency;
	}

	public byte getPayoutFrequency() {
		return payoutFrequency;
	}

	public void setPayoutFrequency(byte payoutFrequency) {
		this.payoutFrequency = payoutFrequency;
	}

	public Integer getTenureD() {
		return tenureD;
	}

	public void setTenureD(Integer tenureD) {
		this.tenureD = tenureD;
	}

	

	

	
	
}