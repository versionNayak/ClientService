package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AtalPensionYojanaCalculatorDTO {
	
	@NotNull(message="clientDOB should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date clientDOB;
	@NotNull(message="contrFreq should not be null")
	private int contrFreq;
	@NotNull(message="monthlyPenReq should not be null")
	private double monthlyPenReq;
	@NotNull(message="retirementAge should not be null")
	private int retirementAge; 
	@NotNull(message="contrStartDate should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date contrStartDate; 
	@NotNull(message="lifeExptenYear should not be null")
	private int lifeExptenYear;
	
	public Date getClientDOB() {
		return clientDOB;
	}
	public void setClientDOB(Date clientDOB) {
		this.clientDOB = clientDOB;
	}
	public int getContrFreq() {
		return contrFreq;
	}
	public void setContrFreq(int contrFreq) {
		this.contrFreq = contrFreq;
	}
	public double getMonthlyPenReq() {
		return monthlyPenReq;
	}
	public void setMonthlyPenReq(double monthlyPenReq) {
		this.monthlyPenReq = monthlyPenReq;
	}
	public int getRetirementAge() {
		return retirementAge;
	}
	public void setRetirementAge(int retirementAge) {
		this.retirementAge = retirementAge;
	}
	public Date getContrStartDate() {
		return contrStartDate;
	}
	public void setContrStartDate(Date contrStartDate) {
		this.contrStartDate = contrStartDate;
	}
	public int getLifeExptenYear() {
		return lifeExptenYear;
	}
	public void setLifeExptenYear(int lifeExptenYear) {
		this.lifeExptenYear = lifeExptenYear;
	}
	
	@Override
	public String toString() {
		return "AtalPensionYojanaCalculatorDTO [clientDOB=" + clientDOB + ", contrFreq=" + contrFreq
				+ ", monthlyPenReq=" + monthlyPenReq + ", retirementAge=" + retirementAge + ", contrStartDate="
				+ contrStartDate + ", lifeExptenYear=" + lifeExptenYear + "]";
	}
	
}
