package com.finlabs.finexa.dto;

import java.util.Date;

public class TransactionReportColumnDTO {
	
	private String folioNo;
	private String folioDetails;
	private String clientDetails;
	private String transactionDate;
	private String transactionType;
	private String transAmt;
	private String nav;
	private String transUnits;
	private String runningTotal;
	private String openingBalance;
	private String closingBalance;
	private String fromDate;
	private String toDate;
	private String currentValue;
	private double currentNav;
	private Date transDateInDateFormat;
	private String transTypeCode;
	private String schemeRTACode;
	public String getSchemeRTACode() {
		return schemeRTACode;
	}
	public void setSchemeRTACode(String schemeRTACode) {
		this.schemeRTACode = schemeRTACode;
	}
	public String getTransTypeCode() {
		return transTypeCode;
	}
	public void setTransTypeCode(String transTypeCode) {
		this.transTypeCode = transTypeCode;
	}
	public Date getTransDateInDateFormat() {
		return transDateInDateFormat;
	}
	public void setTransDateInDateFormat(Date transDateInDateFormat) {
		this.transDateInDateFormat = transDateInDateFormat;
	}
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
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getNav() {
		return nav;
	}
	public void setNav(String nav) {
		this.nav = nav;
	}
	public String getTransUnits() {
		return transUnits;
	}
	public void setTransUnits(String transUnits) {
		this.transUnits = transUnits;
	}
	public String getRunningTotal() {
		return runningTotal;
	}
	public void setRunningTotal(String runningTotal) {
		this.runningTotal = runningTotal;
	}
	public String getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(String openingBalance) {
		this.openingBalance = openingBalance;
	}
	public String getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(String closingBalance) {
		this.closingBalance = closingBalance;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
	public double getCurrentNav() {
		return currentNav;
	}
	public void setCurrentNav(double currentNav) {
		this.currentNav = currentNav;
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
		return "TransactionReportColumnDTO [folioNo=" + folioNo + ", folioDetails=" + folioDetails + ", clientDetails="
				+ clientDetails + ", transactionDate=" + transactionDate + ", transactionType=" + transactionType
				+ ", transAmt=" + transAmt + ", nav=" + nav + ", transUnits=" + transUnits + ", runningTotal="
				+ runningTotal + ", openingBalance=" + openingBalance + ", closingBalance=" + closingBalance
				+ ", fromDate=" + fromDate + ", toDate=" + toDate + ", currentValue=" + currentValue + ", currentNav="
				+ currentNav + ", firstInvestmentDate=" + firstInvestmentDate + ", purchaseAndSIP=" + purchaseAndSIP
				+ ", switchInAndSIP=" + switchInAndSIP + ", dividendReinvestment=" + dividendReinvestment
				+ ", redemptionsAndSWP=" + redemptionsAndSWP + ", switchOutAndSTPOut=" + switchOutAndSTPOut
				+ ", dividentPayouts=" + dividentPayouts + ", unitsHeld=" + unitsHeld + ", costOfInvestment="
				+ costOfInvestment + ", presentNAV=" + presentNAV + ", marketValue=" + marketValue + ", xirr=" + xirr
				+ "]";
	}
	
	


	
	
	
}
