package com.finlabs.finexa.dto;

public class AumReportColumnDTO {
	
	private String fundName;
	private String folioNo;
	private String folioDetails;
	private String clientDetails;
	private String schemeName;
	private String units;
	private String currentValue;
	private String currentNav;
	private String total;
	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getCurrentNav() {
		return currentNav;
	}
	public void setCurrentNav(String currentNav) {
		this.currentNav = currentNav;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
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
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	
	public String getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
	@Override
	public String toString() {
		return "AumReportColumnDTO [fundName=" + fundName + ", folioNo=" + folioNo + ", folioDetails=" + folioDetails
				+ ", clientDetails=" + clientDetails + ", schemeName=" + schemeName + ", units=" + units
				+ ", currentValue=" + currentValue + ", currentNav=" + currentNav + "]";
	}

}
