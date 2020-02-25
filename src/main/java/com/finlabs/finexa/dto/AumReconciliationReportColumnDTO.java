package com.finlabs.finexa.dto;

public class AumReconciliationReportColumnDTO {
	
	private String amcInvestor;
	private String finexaInvestor;
	private String fundName;
	private String folioNo;
	private String amcAum;
	private String finexaAum;
	private String aumDifference;
	private String amcUnits;
	private String finexaUnits;
	private String unitDifference;
	private String transDate;
	private String folioFreezeDate;
	
	public String getAmcInvestor() {
		return amcInvestor;
	}
	public void setAmcInvestor(String amcInvestor) {
		this.amcInvestor = amcInvestor;
	}
	
	public String getFinexaInvestor() {
		return finexaInvestor;
	}
	public void setFinexaInvestor(String finexaInvestor) {
		this.finexaInvestor = finexaInvestor;
	}
	
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	
	public String getFolioNo() {
		return folioNo;
	}
	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}
	
	public String getAmcAum() {
		return amcAum;
	}
	public void setAmcAum(String amcAum) {
		this.amcAum = amcAum;
	}
	
	public String getFinexaAum() {
		return finexaAum;
	}
	public void setFinexaAum(String finexaAum) {
		this.finexaAum = finexaAum;
	}
	
	public String getAumDifference() {
		return aumDifference;
	}
	public void setAumDifference(String aumDifference) {
		this.aumDifference = aumDifference;
	}
	
	public String getAmcUnits() {
		return amcUnits;
	}
	public void setAmcUnits(String amcUnits) {
		this.amcUnits = amcUnits;
	}
	
	public String getFinexaUnits() {
		return finexaUnits;
	}
	public void setFinexaUnits(String finexaUnits) {
		this.finexaUnits = finexaUnits;
	}
	
	public String getUnitDifference() {
		return unitDifference;
	}
	public void setUnitDifference(String unitDifference) {
		this.unitDifference = unitDifference;
	}
	
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	
	public String getFolioFreezeDate() {
		return folioFreezeDate;
	}
	public void setFolioFreezeDate(String folioFreezeDate) {
		this.folioFreezeDate = folioFreezeDate;
	}
	
	@Override
	public String toString() {
		return "AumReconciliationReportColumnDTO [amcInvestor=" + amcInvestor + ", finexaInvestor=" + finexaInvestor
				+ ", fundName=" + fundName + ", folioNo=" + folioNo + ", amcAum=" + amcAum + ", finexaAum=" + finexaAum
				+ ", aumDifference=" + aumDifference + ", amcUnits=" + amcUnits + ", finexaUnits=" + finexaUnits
				+ ", unitDifference=" + unitDifference + ", transDate=" + transDate + ", folioFreezeDate="
				+ folioFreezeDate + "]";
	}
	
}
