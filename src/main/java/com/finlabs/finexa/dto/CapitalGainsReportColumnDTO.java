package com.finlabs.finexa.dto;

public class CapitalGainsReportColumnDTO {
	
	private String folioNo;
	private String clientDetails;
	private String folioDetails;
	private String transactionType1; //Redemption / Switch-out
	private String transactionDate1; //Redemption / Switch-out
	private String transactionUnits1; //Redemption / Switch-out
	private String navPerUnit1; //Redemption / Switch-out
	private String transAmt1; //Redemption / Switch-out
	private String transactionType2; //Corresponding Units in Purchase / Switch-in / Div. Reinvested
	private String transactionDate2; //Corresponding Units in Purchase / Switch-in / Div. Reinvested
	private String transactionUnits2; //Corresponding Units in Purchase / Switch-in / Div. Reinvested
	private String transAmt2; //Corresponding Units in Purchase / Switch-in / Div. Reinvested
	private String navPerUnit2; //Corresponding Units in Purchase / Switch-in / Div. Reinvested
	private String jan312018Units;
	private String jan312018NAV;
	private String jan312018MarketValue;
	private String shortTerm;
	private String longTermWithIndexation;
	private String longTermWithoutIndexation;
	
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
	public String getTransactionType1() {
		return transactionType1;
	}
	public void setTransactionType1(String transactionType1) {
		this.transactionType1 = transactionType1;
	}
	public String getTransactionDate1() {
		return transactionDate1;
	}
	public void setTransactionDate1(String transactionDate1) {
		this.transactionDate1 = transactionDate1;
	}
	public String getTransactionUnits1() {
		return transactionUnits1;
	}
	public void setTransactionUnits1(String transactionUnits1) {
		this.transactionUnits1 = transactionUnits1;
	}
	public String getNavPerUnit1() {
		return navPerUnit1;
	}
	public void setNavPerUnit1(String navPerUnit1) {
		this.navPerUnit1 = navPerUnit1;
	}
	public String getTransAmt1() {
		return transAmt1;
	}
	public void setTransAmt1(String transAmt1) {
		this.transAmt1 = transAmt1;
	}
	public String getTransactionType2() {
		return transactionType2;
	}
	public void setTransactionType2(String transactionType2) {
		this.transactionType2 = transactionType2;
	}
	public String getTransactionDate2() {
		return transactionDate2;
	}
	public void setTransactionDate2(String transactionDate2) {
		this.transactionDate2 = transactionDate2;
	}
	public String getTransactionUnits2() {
		return transactionUnits2;
	}
	public void setTransactionUnits2(String transactionUnits2) {
		this.transactionUnits2 = transactionUnits2;
	}
	public String getTransAmt2() {
		return transAmt2;
	}
	public void setTransAmt2(String transAmt2) {
		this.transAmt2 = transAmt2;
	}
	public String getNavPerUnit2() {
		return navPerUnit2;
	}
	public void setNavPerUnit2(String navPerUnit2) {
		this.navPerUnit2 = navPerUnit2;
	}
	public String getJan312018Units() {
		return jan312018Units;
	}
	public void setJan312018Units(String jan312018Units) {
		this.jan312018Units = jan312018Units;
	}
	public String getJan312018NAV() {
		return jan312018NAV;
	}
	public void setJan312018NAV(String jan312018nav) {
		jan312018NAV = jan312018nav;
	}
	public String getJan312018MarketValue() {
		return jan312018MarketValue;
	}
	public void setJan312018MarketValue(String jan312018MarketValue) {
		this.jan312018MarketValue = jan312018MarketValue;
	}
	public String getShortTerm() {
		return shortTerm;
	}
	public void setShortTerm(String shortTerm) {
		this.shortTerm = shortTerm;
	}
	public String getLongTermWithIndexation() {
		return longTermWithIndexation;
	}
	public void setLongTermWithIndexation(String longTermWithIndexation) {
		this.longTermWithIndexation = longTermWithIndexation;
	}
	public String getLongTermWithoutIndexation() {
		return longTermWithoutIndexation;
	}
	public void setLongTermWithoutIndexation(String longTermWithoutIndexation) {
		this.longTermWithoutIndexation = longTermWithoutIndexation;
	}
	

	@Override
	public String toString() {
		return "PortfolioGainLossReportColumnDTO [folioNo=" + folioNo + ", clientDetails=" + clientDetails
				+ ", folioDetails=" + folioDetails + ", transactionType1=" + transactionType1 + ", transactionDate1="
				+ transactionDate1 + ", transactionUnits1=" + transactionUnits1 + ", navPerUnit1=" + navPerUnit1
				+ ", transAmt1=" + transAmt1 + ", transactionType2=" + transactionType2 + ", transactionDate2="
				+ transactionDate2 + ", transactionUnits2=" + transactionUnits2 + ", transAmt2=" + transAmt2
				+ ", navPerUnit2=" + navPerUnit2 + ", jan312018Units=" + jan312018Units + ", jan312018NAV="
				+ jan312018NAV + ", jan312018MarketValue=" + jan312018MarketValue + ", shortTerm=" + shortTerm
				+ ", longTermWithIndexation=" + longTermWithIndexation + ", longTermWithoutIndexation="
				+ longTermWithoutIndexation + "]";
	}
	public String getClientDetails() {
		return clientDetails;
	}
	public void setClientDetails(String clientDetails) {
		this.clientDetails = clientDetails;
	}
	
	

}
