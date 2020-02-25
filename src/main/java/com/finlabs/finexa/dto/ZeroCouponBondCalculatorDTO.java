package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ZeroCouponBondCalculatorDTO {
	
	@NotNull(message="deposit should not be null")
	private double deposit; 
	@NotNull(message="rateOfInterest should not be null")
	private double rateOfInterest;
	@NotNull(message="tenureType should not be null")
	private String tenureType;
	@NotNull(message="tenure should not be null")
	private int tenure; 
	@NotNull(message="interestFrequency should not be null")
	private int interestFrequency; 
	@NotNull(message="depositDate should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date depositDate;
	@NotNull(message="noOfBonds should not be null")
	private int noOfBonds; 
	@NotNull(message="bondFaceValue should not be null")
	private double bondFaceValue;
	
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
	public int getNoOfBonds() {
		return noOfBonds;
	}
	public void setNoOfBonds(int noOfBonds) {
		this.noOfBonds = noOfBonds;
	}
	public double getBondFaceValue() {
		return bondFaceValue;
	}
	public void setBondFaceValue(double bondFaceValue) {
		this.bondFaceValue = bondFaceValue;
	}
	
	@Override
	public String toString() {
		return "ZeroCouponBondCalculatorDTO [deposit=" + deposit + ", rateOfInterest=" + rateOfInterest
				+ ", tenureType=" + tenureType + ", tenure=" + tenure + ", interestFrequency=" + interestFrequency
				+ ", depositDate=" + depositDate + ", noOfBonds=" + noOfBonds + ", bondFaceValue=" + bondFaceValue
				+ "]";
	}
	
}
