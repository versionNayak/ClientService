package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PostOfficeRDCalculatorDTO {
	
	@NotNull(message="deposit should not be null")
	private double deposit; 
	@NotNull(message="yearsDays should not be null")
	private int yearsDays; 
	@NotNull(message="rdDepositFreq should not be null")
	private int rdDepositFreq;
	@NotNull(message="compundingFreq should not be null")
	private int compundingFreq; 
	@NotNull(message="depositDate should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date depositDate;
	
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}
	public int getYearsDays() {
		return yearsDays;
	}
	public void setYearsDays(int yearsDays) {
		this.yearsDays = yearsDays;
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
		return "PostOfficeRDCalculatorDTO [deposit=" + deposit + ", yearsDays=" + yearsDays + ", rdDepositFreq="
				+ rdDepositFreq + ", compundingFreq=" + compundingFreq + ", depositDate=" + depositDate + "]";
	}
	
	
}
