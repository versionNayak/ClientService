package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BankFDTDRPCDTO {

	@NotNull(message = "Deposit amount should not be Null")
	private double amountDeposited;
	@NotNull(message = "Rate of Interest amount should not be Null")
	private double rateOfInterest;
	@NotNull(message = "Tenure type should be D(Days) or Y(Years)")
	private String tenureType;
	@NotNull(message = "Tenure should be number of Days/Years")
	private int tenure;
	@NotNull(message = "Interest Frequency should not be Null")
	private int interestFrequency;
	@NotNull(message = "Deposit date should not be Null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date depositDate;

	private int serialNo;
	private String referenceDate;
	private String referenceMonth;
	private String financialYear;
	private double interestReceived;
	private double totalInterestReceived;
	private long daysToMaturity;

	public double getRateOfInterest() {
		return rateOfInterest;
	}

	public void setRateOfInterest(double rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}

	public String getTenureType() {
		return tenureType;
	}

	public void setTenureType(String tenureType) {
		this.tenureType = tenureType;
	}

	public int getTenure() {
		return tenure;
	}

	public void setTenure(int tenure) {
		this.tenure = tenure;
	}

	public int getInterestFrequency() {
		return interestFrequency;
	}

	public void setInterestFrequency(int interestFrequency) {
		this.interestFrequency = interestFrequency;
	}

	public Date getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(String referenceDate) {
		this.referenceDate = referenceDate;
	}

	public String getReferenceMonth() {
		return referenceMonth;
	}

	public void setReferenceMonth(String referenceMonth) {
		this.referenceMonth = referenceMonth;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public double getAmountDeposited() {
		return amountDeposited;
	}

	public void setAmountDeposited(double amountDeposited) {
		this.amountDeposited = amountDeposited;
	}

	public double getInterestReceived() {
		return interestReceived;
	}

	public void setInterestReceived(double interestReceived) {
		this.interestReceived = interestReceived;
	}

	public double getTotalInterestReceived() {
		return totalInterestReceived;
	}

	public void setTotalInterestReceived(double totalInterestReceived) {
		this.totalInterestReceived = totalInterestReceived;
	}

	public long getDaysToMaturity() {
		return daysToMaturity;
	}

	public void setDaysToMaturity(long daysToMaturity) {
		this.daysToMaturity = daysToMaturity;
	}

	@Override
	public String toString() {
		return "BankFDTDRPCDTO [amountDeposited=" + amountDeposited + ", rateOfInterest=" + rateOfInterest
				+ ", tenureType=" + tenureType + ", tenure=" + tenure + ", interestFrequency=" + interestFrequency
				+ ", depositDate=" + depositDate + ", serialNo=" + serialNo + ", referenceDate=" + referenceDate
				+ ", referenceMonth=" + referenceMonth + ", financialYear=" + financialYear + ", interestReceived="
				+ interestReceived + ", totalInterestReceived=" + totalInterestReceived + ", daysToMaturity="
				+ daysToMaturity + "]";
	}

}
