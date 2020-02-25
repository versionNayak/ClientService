package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class NPSCalculatorDTO {
	
	@NotNull(message="clientDOB should not be Null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date clientDOB; 
	@NotNull(message="currNPSBal should not be Null")
	private double currNPSBal; 
	@NotNull(message="empolyeeCont should not be Null")
	private double empolyeeCont; 
	@NotNull(message="empolyeeContFreq should not be Null")
	private int empolyeeContFreq;
	@NotNull(message="empolyerCont should not be Null")
	private double empolyerCont; 
	@NotNull(message="empolyerContFreq should not be Null")
	private int empolyerContFreq; 
	@NotNull(message="assetClassEAll should not be Null")
	private double assetClassEAll; 
	@NotNull(message="assetClassCAll should not be Null")
	private double assetClassCAll;
	@NotNull(message="assetClassGAll should not be Null")
	private double assetClassGAll; 
	@NotNull(message="empolyeeContAge should not be Null")
	private int empolyeeContAge; 
	@NotNull(message="empolyerContAge should not be Null")
	private int empolyerContAge; 
	@NotNull(message="retirementAge should not be Null")
	private int retirementAge; 
	@NotNull(message="planType should not be Null")
	private int planType;
	
	public Date getClientDOB() {
		return clientDOB;
	}
	public void setClientDOB(Date clientDOB) {
		this.clientDOB = clientDOB;
	}
	public double getCurrNPSBal() {
		return currNPSBal;
	}
	public void setCurrNPSBal(double currNPSBal) {
		this.currNPSBal = currNPSBal;
	}
	public double getEmpolyeeCont() {
		return empolyeeCont;
	}
	public void setEmpolyeeCont(double empolyeeCont) {
		this.empolyeeCont = empolyeeCont;
	}
	public int getEmpolyeeContFreq() {
		return empolyeeContFreq;
	}
	public void setEmpolyeeContFreq(int empolyeeContFreq) {
		this.empolyeeContFreq = empolyeeContFreq;
	}
	public double getEmpolyerCont() {
		return empolyerCont;
	}
	public void setEmpolyerCont(double empolyerCont) {
		this.empolyerCont = empolyerCont;
	}
	public int getEmpolyerContFreq() {
		return empolyerContFreq;
	}
	public void setEmpolyerContFreq(int empolyerContFreq) {
		this.empolyerContFreq = empolyerContFreq;
	}
	public double getAssetClassEAll() {
		return assetClassEAll;
	}
	public void setAssetClassEAll(double assetClassEAll) {
		this.assetClassEAll = assetClassEAll;
	}
	public double getAssetClassCAll() {
		return assetClassCAll;
	}
	public void setAssetClassCAll(double assetClassCAll) {
		this.assetClassCAll = assetClassCAll;
	}
	public double getAssetClassGAll() {
		return assetClassGAll;
	}
	public void setAssetClassGAll(double assetClassGAll) {
		this.assetClassGAll = assetClassGAll;
	}
	public int getEmpolyeeContAge() {
		return empolyeeContAge;
	}
	public void setEmpolyeeContAge(int empolyeeContAge) {
		this.empolyeeContAge = empolyeeContAge;
	}
	public int getEmpolyerContAge() {
		return empolyerContAge;
	}
	public void setEmpolyerContAge(int empolyerContAge) {
		this.empolyerContAge = empolyerContAge;
	}
	public int getRetirementAge() {
		return retirementAge;
	}
	public void setRetirementAge(int retirementAge) {
		this.retirementAge = retirementAge;
	}
	public int getPlanType() {
		return planType;
	}
	public void setPlanType(int planType) {
		this.planType = planType;
	}
	
	@Override
	public String toString() {
		return "NPSCalculatorDTO [clientDOB=" + clientDOB + ", currNPSBal=" + currNPSBal + ", empolyeeCont="
				+ empolyeeCont + ", empolyeeContFreq=" + empolyeeContFreq + ", empolyerCont=" + empolyerCont
				+ ", empolyerContFreq=" + empolyerContFreq + ", assetClassEAll=" + assetClassEAll + ", assetClassCAll="
				+ assetClassCAll + ", assetClassGAll=" + assetClassGAll + ", empolyeeContAge=" + empolyeeContAge
				+ ", empolyerContAge=" + empolyerContAge + ", retirementAge=" + retirementAge + ", planType=" + planType
				+ "]";
	}
	
	

}
