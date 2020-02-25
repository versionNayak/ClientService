package com.finlabs.finexa.dto;

public class PortfolioGainLossReportColumnDTO {
	
	private String clientDetails;
	private String schemeName;
	private String folioNo;
	private String investmentSince;
	private String marketValue;
	private String realizedInvestmentCost;
	private String realizedGainLossAmount;
	private String realizedXirr;
	private String unrealizedInvestmentCost;
	private String unrealizedGainLossAmount;
	private String unrealizedXirr;
	private String totalGainLossAmount;
	private String totalXirr;
	
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
	public String getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(String marketValue) {
		this.marketValue = marketValue;
	}
	public String getRealizedInvestmentCost() {
		return realizedInvestmentCost;
	}
	public void setRealizedInvestmentCost(String realizedInvestmentCost) {
		this.realizedInvestmentCost = realizedInvestmentCost;
	}
	public String getRealizedGainLossAmount() {
		return realizedGainLossAmount;
	}
	public void setRealizedGainLossAmount(String realizedGainLossAmount) {
		this.realizedGainLossAmount = realizedGainLossAmount;
	}
	public String getRealizedXirr() {
		return realizedXirr;
	}
	public void setRealizedXirr(String realizedXirr) {
		this.realizedXirr = realizedXirr;
	}
	public String getUnrealizedInvestmentCost() {
		return unrealizedInvestmentCost;
	}
	public void setUnrealizedInvestmentCost(String unrealizedInvestmentCost) {
		this.unrealizedInvestmentCost = unrealizedInvestmentCost;
	}
	public String getUnrealizedGainLossAmount() {
		return unrealizedGainLossAmount;
	}
	public void setUnrealizedGainLossAmount(String unrealizedGainLossAmount) {
		this.unrealizedGainLossAmount = unrealizedGainLossAmount;
	}
	public String getUnrealizedXirr() {
		return unrealizedXirr;
	}
	public void setUnrealizedXirr(String unrealizedXirr) {
		this.unrealizedXirr = unrealizedXirr;
	}
	public String getTotalGainLossAmount() {
		return totalGainLossAmount;
	}
	public void setTotalGainLossAmount(String totalGainLossAmount) {
		this.totalGainLossAmount = totalGainLossAmount;
	}
	public String getTotalXirr() {
		return totalXirr;
	}
	public void setTotalXirr(String totalXirr) {
		this.totalXirr = totalXirr;
	}
	@Override
	public String toString() {
		return "PortfolioGainLossReportColumnDTO [clientDetails=" + clientDetails + ", schemeName=" + schemeName
				+ ", folioNo=" + folioNo + ", investmentSince=" + investmentSince + ", marketValue=" + marketValue
				+ ", realizedInvestmentCost=" + realizedInvestmentCost + ", realizedGainLossAmount="
				+ realizedGainLossAmount + ", realizedXirr=" + realizedXirr + ", unrealizedInvestmentCost="
				+ unrealizedInvestmentCost + ", unrealizedGainLossAmount=" + unrealizedGainLossAmount
				+ ", unrealizedXirr=" + unrealizedXirr + ", totalGainLossAmount=" + totalGainLossAmount + ", totalXirr="
				+ totalXirr + "]";
	}
	
	
	
	

}
