package com.finlabs.finexa.dto;

public class InactiveClientReportColumnDTO {
	
	private String clientName;
	private String fundName;
	private String folioNo;
	private String schemeName;
	private String units;
	private String currentNAV;
	private String currentValue;
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
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
	public String getCurrentNAV() {
		return currentNAV;
	}
	public void setCurrentNAV(String currentNAV) {
		this.currentNAV = currentNAV;
	}
	public String getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
	@Override
	public String toString() {
		return "InactiveClientReportColumnDTO [clientName=" + clientName + ", fundName=" + fundName + ", folioNo="
				+ folioNo + ", schemeName=" + schemeName + ", units=" + units + ", currentNAV=" + currentNAV
				+ ", currentValue=" + currentValue + "]";
	}
	
		
}
