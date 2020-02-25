package com.finlabs.finexa.dto;

public class DividendReportColumnDTO {
	
	private String folioNo;
	private String fundName;
	private String folioDetails;
	private String transactionDate;
	private String transactionType;
	private String transAmt;
	private String transUnits;
	private String dividendPerUnit;
	private String clientDetails;	
	
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
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
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
	public String getTransUnits() {
		return transUnits;
	}
	public void setTransUnits(String transUnits) {
		this.transUnits = transUnits;
	}
	public String getDividendPerUnit() {
		return dividendPerUnit;
	}
	public void setDividendPerUnit(String dividendPerUnit) {
		this.dividendPerUnit = dividendPerUnit;
	}
	@Override
	public String toString() {
		return "DividendReportColumnDTO [folioNo=" + folioNo + ", fundName=" + fundName + ", folioDetails="
				+ folioDetails + ", transactionDate=" + transactionDate + ", transactionType=" + transactionType
				+ ", transAmt=" + transAmt + ", transUnits=" + transUnits + ", dividendPerUnit=" + dividendPerUnit
				+ ", clientDetails=" + clientDetails + "]";
	}
	
	

}
