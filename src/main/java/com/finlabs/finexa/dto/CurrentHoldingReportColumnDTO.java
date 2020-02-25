package com.finlabs.finexa.dto;

public class CurrentHoldingReportColumnDTO {
	
	private String fundName;
	private String folioNo;
	private String folioDetails;
	private String clientDetails;
	private String schemeName;
	private String units;
	private String investmentCost;
	private String redeemedValue;
	private String dividendEarned;
	private String realizedGainLoss;
	private String currentValue;
	private String unrealizedGainLoss;
	private String dividendPayout;
	
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
	public String getFolioDetails() {
		return folioDetails;
	}
	public void setFolioDetails(String folioDetails) {
		this.folioDetails = folioDetails;
	}
	public String getClientDetails() {
		return clientDetails;
	}
	public void setClientDetails(String clientDetails) {
		this.clientDetails = clientDetails;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getInvestmentCost() {
		return investmentCost;
	}
	public void setInvestmentCost(String investmentCost) {
		this.investmentCost = investmentCost;
	}
	public String getRedeemedValue() {
		return redeemedValue;
	}
	public void setRedeemedValue(String redeemedValue) {
		this.redeemedValue = redeemedValue;
	}
	public String getDividendEarned() {
		return dividendEarned;
	}
	public void setDividendEarned(String dividendEarned) {
		this.dividendEarned = dividendEarned;
	}
	public String getRealizedGainLoss() {
		return realizedGainLoss;
	}
	public void setRealizedGainLoss(String realizedGainLoss) {
		this.realizedGainLoss = realizedGainLoss;
	}
	public String getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
	public String getUnrealizedGainLoss() {
		return unrealizedGainLoss;
	}
	public void setUnrealizedGainLoss(String unrealizedGainLoss) {
		this.unrealizedGainLoss = unrealizedGainLoss;
	}
	
	@Override
	public String toString() {
		return "CurrentHoldingReportColumnDTO [fundName=" + fundName + ", folioNo=" + folioNo + ", folioDetails="
				+ folioDetails + ", clientDetails=" + clientDetails + ", schemeName=" + schemeName + ", units=" + units
				+ ", investmentCost=" + investmentCost + ", redeemedValue=" + redeemedValue + ", dividendEarned="
				+ dividendEarned + ", realizedGainLoss=" + realizedGainLoss + ", currentValue=" + currentValue
				+ ", unrealizedGainLoss=" + unrealizedGainLoss + "]";
	}
	public String getDividendPayout() {
		return dividendPayout;
	}
	public void setDividendPayout(String dividendPayout) {
		this.dividendPayout = dividendPayout;
	}
	
	

}
