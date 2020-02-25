package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PONSCCalculatorDTO {
	
	@NotNull(message = "deposit should not be null")
	private double deposit; 
	@NotNull(message = "tenure should not be null")
	private int tenure; 
	@NotNull(message = "interestFrequency should not be null")
	private int interestFrequency; 
	@NotNull(message = "depositDate should not be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date depositDate;
	
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
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
	
	
	@Override
	public String toString() {
		return "PONSCCalculatorDTO [deposit=" + deposit + ", tenure=" + tenure + ", interestFrequency="
				+ interestFrequency + ", depositDate=" + depositDate + "]";
	}
	
	

}
