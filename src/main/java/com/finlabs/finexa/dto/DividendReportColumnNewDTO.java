package com.finlabs.finexa.dto;

public class DividendReportColumnNewDTO {

	private String srNo;
	private String folioNo;
	private String schemeName;
	private String dividendPayout;
	private String dividendReinvestment;
	private String clientDetails;	
	private String totalForDP;	
	private String totalForDR;	
	
	public String getTotalForDR() {
		return totalForDR;
	}
	public void setTotalForDR(String totalForDR) {
		this.totalForDR = totalForDR;
	}
	public String getSrNo() {
		return srNo;
	}
	public void setSrNo(String srNo) {
		this.srNo = srNo;
	}
	
	public String getFolioNo() {
		return folioNo;
	}
	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}
	
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	
	public String getDividendPayout() {
		return dividendPayout;
	}
	public void setDividendPayout(String dividendPayout) {
		this.dividendPayout = dividendPayout;
	}
	
	public String getDividendReinvestment() {
		return dividendReinvestment;
	}
	public void setDividendReinvestment(String dividendReinvestment) {
		this.dividendReinvestment = dividendReinvestment;
	}
	
	public String getClientDetails() {
		return clientDetails;
	}
	public void setClientDetails(String clientDetails) {
		this.clientDetails = clientDetails;
	}
	
	@Override
	public String toString() {
		return "DividendReportColumnNewDTO [srNo=" + srNo + ", folioNo=" + folioNo + ", schemeName=" + schemeName
				+ ", dividendPayout=" + dividendPayout + ", dividendReinvestment=" + dividendReinvestment
				+ ", clientDetails=" + clientDetails + ", totalForDP=" + totalForDP + ", totalForDR=" + totalForDR
				+ "]";
	}
	public String getTotalForDP() {
		return totalForDP;
	}
	public void setTotalForDP(String totalForDP) {
		this.totalForDP = totalForDP;
	}
	
	
	
}
