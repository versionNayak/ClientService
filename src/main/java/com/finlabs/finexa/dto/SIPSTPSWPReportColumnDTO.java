package com.finlabs.finexa.dto;

public class SIPSTPSWPReportColumnDTO {
	
	private String clientDetails;
	private String fundName;
	private String folioNumber;
	private String folioDetails;
	private String schemeName;
	private String transactionType;
	private String frequency;
	private String registrationDate;
	private String transactionNumber;
	private String fromDate;
	private String toDate;
	private String targetScheme;
	private String amount;	
	private String installmentsLeft;
	private String activeInactive;
	
	public String getClientDetails() {
		return clientDetails;
	}
	public void setClientDetails(String clientDetails) {
		this.clientDetails = clientDetails;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getFolioNumber() {
		return folioNumber;
	}
	public void setFolioNumber(String folioNumber) {
		this.folioNumber = folioNumber;
	}
	public String getFolioDetails() {
		return folioDetails;
	}
	public void setFolioDetails(String folioDetails) {
		this.folioDetails = folioDetails;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	public String getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
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
	public String getTargetScheme() {
		return targetScheme;
	}
	public void setTargetScheme(String targetScheme) {
		this.targetScheme = targetScheme;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getInstallmentsLeft() {
		return installmentsLeft;
	}
	public void setInstallmentsLeft(String installmentsLeft) {
		this.installmentsLeft = installmentsLeft;
	}
	public String getActiveInactive() {
		return activeInactive;
	}
	public void setActiveInactive(String activeInactive) {
		this.activeInactive = activeInactive;
	}
	
	@Override
	public String toString() {
		return "SIPSTPSWPReportColumnDTO [clientDetails=" + clientDetails + ", fundName=" + fundName + ", folioNumber="
				+ folioNumber + ", folioDetails=" + folioDetails + ", schemeName=" + schemeName + ", transactionType="
				+ transactionType + ", frequency=" + frequency + ", registrationDate=" + registrationDate
				+ ", transactionNumber=" + transactionNumber + ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", targetScheme=" + targetScheme + ", amount=" + amount + ", installmentsLeft=" + installmentsLeft
				+ ", activeInactive=" + activeInactive + "]";
	}
	
	
}
