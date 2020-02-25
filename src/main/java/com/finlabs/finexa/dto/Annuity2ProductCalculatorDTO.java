package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Annuity2ProductCalculatorDTO {
	
	@NotNull(message="clientDOB should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date clientDOB;
	@NotNull(message="pensionableCorpus should not be null")
	private double pensionableCorpus;
	@NotNull(message="annuityRate should not be null")
	private double annuityRate; 
	@NotNull(message="lifeExpectancySelf should not be null")
	private double lifeExpectancySelf; 
	@NotNull(message="lifeExpectancySpouse should not be null")
	private double lifeExpectancySpouse;
	@NotNull(message="annuityPayoutFreq should not be null")
	private int annuityPayoutFreq;
	@NotNull(message="annuityStartDate should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date annuityStartDate; 
	@NotNull(message="annuityType should not be null")
	private int annuityType; 
	@NotNull(message="annuityGrowthRate should not be null")
	private double annuityGrowthRate; 
//	@NotNull(message="noOfYearsService should not be null")
	private int noOfYearsService;
	
	public Date getClientDOB() {
		return clientDOB;
	}
	public void setClientDOB(Date clientDOB) {
		this.clientDOB = clientDOB;
	}
	public double getPensionableCorpus() {
		return pensionableCorpus;
	}
	public void setPensionableCorpus(double pensionableCorpus) {
		this.pensionableCorpus = pensionableCorpus;
	}
	public double getAnnuityRate() {
		return annuityRate;
	}
	public void setAnnuityRate(double annuityRate) {
		this.annuityRate = annuityRate;
	}
	public double getLifeExpectancySelf() {
		return lifeExpectancySelf;
	}
	public void setLifeExpectancySelf(double lifeExpectancySelf) {
		this.lifeExpectancySelf = lifeExpectancySelf;
	}
	public double getLifeExpectancySpouse() {
		return lifeExpectancySpouse;
	}
	public void setLifeExpectancySpouse(double lifeExpectancySpouse) {
		this.lifeExpectancySpouse = lifeExpectancySpouse;
	}
	public int getAnnuityPayoutFreq() {
		return annuityPayoutFreq;
	}
	public void setAnnuityPayoutFreq(int annuityPayoutFreq) {
		this.annuityPayoutFreq = annuityPayoutFreq;
	}
	public Date getAnnuityStartDate() {
		return annuityStartDate;
	}
	public void setAnnuityStartDate(Date annuityStartDate) {
		this.annuityStartDate = annuityStartDate;
	}
	public int getAnnuityType() {
		return annuityType;
	}
	public void setAnnuityType(int annuityType) {
		this.annuityType = annuityType;
	}
	public double getAnnuityGrowthRate() {
		return annuityGrowthRate;
	}
	public void setAnnuityGrowthRate(double annuityGrowthRate) {
		this.annuityGrowthRate = annuityGrowthRate;
	}
	public int getNoOfYearsService() {
		return noOfYearsService;
	}
	public void setNoOfYearsService(int noOfYearsService) {
		this.noOfYearsService = noOfYearsService;
	}
	
	@Override
	public String toString() {
		return "Annuity2ProductCalculatorDTO [clientDOB=" + clientDOB + ", pensionableCorpus=" + pensionableCorpus
				+ ", annuityRate=" + annuityRate + ", lifeExpectancySelf=" + lifeExpectancySelf
				+ ", lifeExpectancySpouse=" + lifeExpectancySpouse + ", annuityPayoutFreq=" + annuityPayoutFreq
				+ ", annuityStartDate=" + annuityStartDate + ", annuityType=" + annuityType + ", annuityGrowthRate="
				+ annuityGrowthRate + ", noOfYearsService=" + noOfYearsService + "]";
	}
	
	

}
