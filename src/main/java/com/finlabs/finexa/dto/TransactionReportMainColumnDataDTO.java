package com.finlabs.finexa.dto;

public class TransactionReportMainColumnDataDTO {

	private String folioDetails;
	private String clientDetails;
	private String transactionDate;
	private String transactionType;
	private String transAmt;
	private String nav;
	private String transUnits;
	private String runningTotal;
	
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
	
	@Override
	public String toString() {
		return "TransactionReportMainColumnDataDTO [folioDetails=" + folioDetails + ", clientDetails=" + clientDetails
				+ ", transactionDate=" + transactionDate + ", transactionType=" + transactionType + ", transAmt="
				+ transAmt + ", nav=" + nav + ", transUnits=" + transUnits + ", runningTotal=" + runningTotal + "]";
	}

}
