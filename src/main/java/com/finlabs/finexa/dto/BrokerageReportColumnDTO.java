package com.finlabs.finexa.dto;

public class BrokerageReportColumnDTO {
	
	private String folioNo;
	private String clientDetails;
	private String fundName;
	private String folioDetails;
	private String schemeName;
	private String currentAUM;
	private String brokType;
	private String brokerage;
	
	public String getFolioNo() {
		return folioNo;
	}
	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}
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
	public String getCurrentAUM() {
		return currentAUM;
	}
	public void setCurrentAUM(String currentAUM) {
		this.currentAUM = currentAUM;
	}
	public String getBrokType() {
		return brokType;
	}
	public void setBrokType(String brokType) {
		this.brokType = brokType;
	}
	public String getBrokerage() {
		return brokerage;
	}
	public void setBrokerage(String brokerage) {
		this.brokerage = brokerage;
	}
	
	@Override
	public String toString() {
		return "BrokerageReportColumnDTO [folioNo=" + folioNo + ", clientDetails=" + clientDetails + ", fundName="
				+ fundName + ", folioDetails=" + folioDetails + ", schemeName=" + schemeName + ", currentAUM="
				+ currentAUM + ", brokType=" + brokType + ", brokerage=" + brokerage + "]";
	}
	
}
