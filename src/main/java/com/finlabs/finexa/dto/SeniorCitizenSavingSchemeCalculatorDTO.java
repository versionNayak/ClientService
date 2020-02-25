package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SeniorCitizenSavingSchemeCalculatorDTO {
	
	@NotNull(message="deposit should not be null")
	private double deposit; 
	@NotNull(message="years should not be null")
	private double years;
	@NotNull(message="interestFrequency should not be null")
	private int interestFrequency;
	@NotNull(message="depositDate should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date depositDate;
	
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}
	public double getYears() {
		return years;
	}
	public void setYears(double years) {
		this.years = years;
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
		return "SeniorCitizenSavingSchemeCalculatorDTO [deposit=" + deposit + ", years=" + years
				+ ", interestFrequency=" + interestFrequency + ", depositDate=" + depositDate + "]";
	}
	
	

}
