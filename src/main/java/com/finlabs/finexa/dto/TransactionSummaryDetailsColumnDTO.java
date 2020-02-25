package com.finlabs.finexa.dto;

public class TransactionSummaryDetailsColumnDTO {
	
	private String clientDetails;
	private String folioDetails;
	private String firstInvestmentDate;
	private String purchaseAndSIP;
	private String switchInAndSIP;
	private String dividendReinvestment;
	private String redemptionsAndSWP;
	private String switchOutAndSTPOut;
	private String dividentPayouts;
	private String unitsHeld;
	private String costOfInvestment;
	private String presentNAV;
	private String marketValue;
	private String xirr;
	
	
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
	
	
	
	public String getFirstInvestmentDate() {
		return firstInvestmentDate;
	}
	public void setFirstInvestmentDate(String firstInvestmentDate) {
		this.firstInvestmentDate = firstInvestmentDate;
	}
	public String getPurchaseAndSIP() {
		return purchaseAndSIP;
	}
	public void setPurchaseAndSIP(String purchaseAndSIP) {
		this.purchaseAndSIP = purchaseAndSIP;
	}
	public String getSwitchInAndSIP() {
		return switchInAndSIP;
	}
	public void setSwitchInAndSIP(String switchInAndSIP) {
		this.switchInAndSIP = switchInAndSIP;
	}
	public String getDividendReinvestment() {
		return dividendReinvestment;
	}
	public void setDividendReinvestment(String dividendReinvestment) {
		this.dividendReinvestment = dividendReinvestment;
	}
	public String getRedemptionsAndSWP() {
		return redemptionsAndSWP;
	}
	public void setRedemptionsAndSWP(String redemptionsAndSWP) {
		this.redemptionsAndSWP = redemptionsAndSWP;
	}
	public String getSwitchOutAndSTPOut() {
		return switchOutAndSTPOut;
	}
	public void setSwitchOutAndSTPOut(String switchOutAndSTPOut) {
		this.switchOutAndSTPOut = switchOutAndSTPOut;
	}
	public String getDividentPayouts() {
		return dividentPayouts;
	}
	public void setDividentPayouts(String dividentPayouts) {
		this.dividentPayouts = dividentPayouts;
	}
	public String getUnitsHeld() {
		return unitsHeld;
	}
	public void setUnitsHeld(String unitsHeld) {
		this.unitsHeld = unitsHeld;
	}
	public String getCostOfInvestment() {
		return costOfInvestment;
	}
	public void setCostOfInvestment(String costOfInvestment) {
		this.costOfInvestment = costOfInvestment;
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
	@Override
	public String toString() {
		return "TransactionSummaryDetailsColumnDTO [clientDetails=" + clientDetails + ", folioDetails=" + folioDetails
				+ ", firstInvestmentDate=" + firstInvestmentDate + ", purchaseAndSIP=" + purchaseAndSIP
				+ ", switchInAndSIP=" + switchInAndSIP + ", dividendReinvestment=" + dividendReinvestment
				+ ", redemptionsAndSWP=" + redemptionsAndSWP + ", switchOutAndSTPOut=" + switchOutAndSTPOut
				+ ", dividentPayouts=" + dividentPayouts + ", unitsHeld=" + unitsHeld + ", costOfInvestment="
				+ costOfInvestment + ", presentNAV=" + presentNAV + ", marketValue=" + marketValue + ", xirr=" + xirr
				+ "]";
	}
	
	
	
	
}
