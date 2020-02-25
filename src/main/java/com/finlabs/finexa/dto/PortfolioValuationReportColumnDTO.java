package com.finlabs.finexa.dto;

public class PortfolioValuationReportColumnDTO {
	
	private String clientDetails;
	private String schemeName;
	private String folioNo;
	private String investmentSince;
	private String investmentSwitchIn;
	private String redemptionsSwitchOut;
	private String dividends;
	private String units;
	private String investmentCost;
	private String presentNAV;
	private String marketValue;
	private String xirr;
	private String total;
	
	private String folioDetails;
	
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	
	public String getFolioNo() {
		return folioNo;
	}
	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}
	
	public String getInvestmentSince() {
		return investmentSince;
	}
	public void setInvestmentSince(String investmentSince) {
		this.investmentSince = investmentSince;
	}
	
	public String getInvestmentSwitchIn() {
		return investmentSwitchIn;
	}
	public void setInvestmentSwitchIn(String investmentSwitchIn) {
		this.investmentSwitchIn = investmentSwitchIn;
	}
	
	public String getRedemptionsSwitchOut() {
		return redemptionsSwitchOut;
	}
	public void setRedemptionsSwitchOut(String redemptionsSwitchOut) {
		this.redemptionsSwitchOut = redemptionsSwitchOut;
	}
	
	public String getDividends() {
		return dividends;
	}
	public void setDividends(String dividends) {
		this.dividends = dividends;
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
	
	public String getPresentNAV() {
		return presentNAV;
	}
	public void setPresentNAV(String presentNAV) {
		this.presentNAV = presentNAV;
	}
	
	public String getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(String marketValue) {
		this.marketValue = marketValue;
	}
	
	public String getXirr() {
		return xirr;
	}
	public void setXirr(String xirr) {
		this.xirr = xirr;
	}
	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	
	public String getClientDetails() {
		return clientDetails;
	}
	public void setClientDetails(String clientDetails) {
		this.clientDetails = clientDetails;
	}
	
	public String getFolioDetails() {
		return folioDetails;
	}
	public void setFolioDetails(String folioDetails) {
		this.folioDetails = folioDetails;
	}
	
	@Override
	public String toString() {
		return "PortfolioValuationReportColumnDTO [schemeName=" + schemeName + ", folioNo=" + folioNo
				+ ", investmentSince=" + investmentSince + ", investmentSwitchIn=" + investmentSwitchIn
				+ ", redemptionsSwitchOut=" + redemptionsSwitchOut + ", dividends=" + dividends + ", units=" + units
				+ ", investmentCost=" + investmentCost + ", presentNAV=" + presentNAV + ", marketValue=" + marketValue
				+ ", xirr=" + xirr + ", total=" + total + ", clientDetails=" + clientDetails + ", folioDetails="
				+ folioDetails + "]";
	}
	
	
}
