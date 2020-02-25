package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BankRecurringDespositCalculatorDTO {

	@NotNull(message = "Deposit amount should not be Null")
	private double deposit;
	@NotNull(message = "rateOfInterest amount should not be Null")
	private double rateOfInterest;
	@NotNull(message = "year amount should not be Null")
	private int year;
	@NotNull(message = "month amount should not be Null")
	private int month;
	@NotNull(message = "rdDepositFreq amount should not be Null")
	private int rdDepositFreq;
	@NotNull(message = "compundingFreq amount should not be Null")
	private int compundingFreq;
	@NotNull(message = "depositDate amount should not be Null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date depositDate;

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public double getRateOfInterest() {
		return rateOfInterest;
	}

	public void setRateOfInterest(double rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getRdDepositFreq() {
		return rdDepositFreq;
	}

	public void setRdDepositFreq(int rdDepositFreq) {
		this.rdDepositFreq = rdDepositFreq;
	}

	public int getCompundingFreq() {
		return compundingFreq;
	}

	public void setCompundingFreq(int compundingFreq) {
		this.compundingFreq = compundingFreq;
	}

	public Date getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	@Override
	public String toString() {
		return "BankRecurringDespositCalculatorDTO [deposit=" + deposit + ", rateOfInterest=" + rateOfInterest
				+ ", year=" + year + ", month=" + month + ", rdDepositFreq=" + rdDepositFreq + ", compundingFreq="
				+ compundingFreq + ", depositDate=" + depositDate + "]";
	}

}
