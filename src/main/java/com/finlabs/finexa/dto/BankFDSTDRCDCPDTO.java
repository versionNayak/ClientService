package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BankFDSTDRCDCPDTO {
	
	@NotNull(message="Deposit amount should not be Null")
	private double depositAmount; 
	@NotNull(message="Deposit amount should not be Null")
	private double annualInterestRate; 
	@NotNull(message="Deposit amount should not be Null")
	private String tenureType; 
	@NotNull(message="Deposit amount should not be Null")
	private int tenure;
	@NotNull(message="Deposit amount should not be Null")
	private int compoundingFrequency; 
	@NotNull(message="Deposit amount should not be Null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private	Date depositDate;
	
	public double getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}
	public double getAnnualInterestRate() {
		return annualInterestRate;
	}
	public void setAnnualInterestRate(double annualInterestRate) {
		this.annualInterestRate = annualInterestRate;
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
	public int getCompoundingFrequency() {
		return compoundingFrequency;
	}
	public void setCompoundingFrequency(int compoundingFrequency) {
		this.compoundingFrequency = compoundingFrequency;
	}
	public Date getDepositDate() {
		return depositDate;
	}
	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}
	
	@Override
	public String toString() {
		return "BankFDSTDRCDCPDTO [depositAmount=" + depositAmount + ", annualInterestRate=" + annualInterestRate
				+ ", tenureType=" + tenureType + ", tenure=" + tenure + ", compoundingFrequency=" + compoundingFrequency
				+ ", depositDate=" + depositDate + "]";
	}
	
	

}
