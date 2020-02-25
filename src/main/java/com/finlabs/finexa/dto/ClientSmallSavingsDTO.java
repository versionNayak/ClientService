package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientSmallSavingsDTO {

	private int id;
	@NotNull(message = "financialAssetType must not be null")
	private byte financialAssetType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	@NotNull(message = "startDate must not be null")
	private Date startDate;
	@NotNull(message = "investmentAmount must not be null")
	@DecimalMin(value = "0.01", message = "investmentAmount cannot be 0")
	private BigDecimal investmentAmount;
	@NotNull(message = "interestRate must not be null")
	@DecimalMin(value = "0.01", message = "interestRate cannot be 0")
	private BigDecimal interestRate;
	private byte compoundingFrequency;
	private byte compoundingFrequencySelect;
	private byte depositFrequency;
	private byte depositTenure;
	private byte interestPayoutFrequency;
	private byte interestPayoutFrequencySelect;
	private byte kvpTenureMonth;
	private byte kvpTenureYear;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date maturityDate;
	private byte maturityTenure;
	private int clientID;
	private int familyMemberID;
	private String financialAssetName;
	private String ownerName;

	public ClientSmallSavingsDTO() {
		super();
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public BigDecimal getInvestmentAmount() {
		return investmentAmount;
	}

	public void setInvestmentAmount(BigDecimal investmentAmount) {
		this.investmentAmount = investmentAmount;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public byte getCompoundingFrequency() {
		return compoundingFrequency;
	}

	public void setCompoundingFrequency(byte compoundingFrequency) {
		this.compoundingFrequency = compoundingFrequency;
	}

	public byte getDepositFrequency() {
		return depositFrequency;
	}

	public void setDepositFrequency(byte depositFrequency) {
		this.depositFrequency = depositFrequency;
	}

	public byte getDepositTenure() {
		return depositTenure;
	}

	public void setDepositTenure(byte depositTenure) {
		this.depositTenure = depositTenure;
	}

	public byte getInterestPayoutFrequency() {
		return interestPayoutFrequency;
	}

	public void setInterestPayoutFrequency(byte interestPayoutFrequency) {
		this.interestPayoutFrequency = interestPayoutFrequency;
	}

	public byte getKvpTenureMonth() {
		return kvpTenureMonth;
	}

	public void setKvpTenureMonth(byte kvpTenureMonth) {
		this.kvpTenureMonth = kvpTenureMonth;
	}

	public byte getKvpTenureYear() {
		return kvpTenureYear;
	}

	public void setKvpTenureYear(byte kvpTenureYear) {
		this.kvpTenureYear = kvpTenureYear;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public byte getMaturityTenure() {
		return maturityTenure;
	}

	public void setMaturityTenure(byte maturityTenure) {
		this.maturityTenure = maturityTenure;
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

	public byte getCompoundingFrequencySelect() {
		return compoundingFrequencySelect;
	}

	public void setCompoundingFrequencySelect(byte compoundingFrequencySelect) {
		this.compoundingFrequencySelect = compoundingFrequencySelect;
	}
	

	public byte getInterestPayoutFrequencySelect() {
		return interestPayoutFrequencySelect;
	}

	public void setInterestPayoutFrequencySelect(byte interestPayoutFrequencySelect) {
		this.interestPayoutFrequencySelect = interestPayoutFrequencySelect;
	}

	@Override
	public String toString() {
		return "ClientSmallSavingsDTO [id=" + id + ", financialAssetType=" + financialAssetType + ", startDate="
				+ startDate + ", investmentAmount=" + investmentAmount + ", interestRate=" + interestRate
				+ ", compoundingFrequency=" + compoundingFrequency + ", compoundingFrequencySelect="
				+ compoundingFrequencySelect + ", depositFrequency=" + depositFrequency + ", depositTenure="
				+ depositTenure + ", interestPayoutFrequency=" + interestPayoutFrequency
				+ ", interestPayoutFrequencySelect=" + interestPayoutFrequencySelect + ", kvpTenureMonth="
				+ kvpTenureMonth + ", kvpTenureYear=" + kvpTenureYear + ", maturityDate=" + maturityDate
				+ ", maturityTenure=" + maturityTenure + ", clientID=" + clientID + ", familyMemberID=" + familyMemberID
				+ ", financialAssetName=" + financialAssetName + ", ownerName=" + ownerName + "]";
	}

	
}