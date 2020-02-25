package com.finlabs.finexa.dto;

import java.math.BigDecimal;

public class MasterNAVHistoryDTO {
	
	private String isin;
	private String assetClass;
	private String camsCode;
	private BigDecimal dayFiveNAV;
	private BigDecimal dayFourNAV;
	private BigDecimal dayOneNAV;
	private BigDecimal daySevenNAV;
	private BigDecimal daySixNAV;
	private BigDecimal dayThreeNAV;
	private BigDecimal dayTwoNAV;
	private String schemeName;
	
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	public String getAssetClass() {
		return assetClass;
	}
	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}
	public String getCamsCode() {
		return camsCode;
	}
	public void setCamsCode(String camsCode) {
		this.camsCode = camsCode;
	}
	public BigDecimal getDayFiveNAV() {
		return dayFiveNAV;
	}
	public void setDayFiveNAV(BigDecimal dayFiveNAV) {
		this.dayFiveNAV = dayFiveNAV;
	}
	public BigDecimal getDayFourNAV() {
		return dayFourNAV;
	}
	public void setDayFourNAV(BigDecimal dayFourNAV) {
		this.dayFourNAV = dayFourNAV;
	}
	public BigDecimal getDayOneNAV() {
		return dayOneNAV;
	}
	public void setDayOneNAV(BigDecimal dayOneNAV) {
		this.dayOneNAV = dayOneNAV;
	}
	public BigDecimal getDaySevenNAV() {
		return daySevenNAV;
	}
	public void setDaySevenNAV(BigDecimal daySevenNAV) {
		this.daySevenNAV = daySevenNAV;
	}
	public BigDecimal getDaySixNAV() {
		return daySixNAV;
	}
	public void setDaySixNAV(BigDecimal daySixNAV) {
		this.daySixNAV = daySixNAV;
	}
	public BigDecimal getDayThreeNAV() {
		return dayThreeNAV;
	}
	public void setDayThreeNAV(BigDecimal dayThreeNAV) {
		this.dayThreeNAV = dayThreeNAV;
	}
	public BigDecimal getDayTwoNAV() {
		return dayTwoNAV;
	}
	public void setDayTwoNAV(BigDecimal dayTwoNAV) {
		this.dayTwoNAV = dayTwoNAV;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	@Override
	public String toString() {
		return "MasterNAVHistoryDTO [isin=" + isin + ", assetClass=" + assetClass + ", camsCode=" + camsCode
				+ ", dayFiveNAV=" + dayFiveNAV + ", dayFourNAV=" + dayFourNAV + ", dayOneNAV=" + dayOneNAV
				+ ", daySevenNAV=" + daySevenNAV + ", daySixNAV=" + daySixNAV + ", dayThreeNAV=" + dayThreeNAV
				+ ", dayTwoNAV=" + dayTwoNAV + ", schemeName=" + schemeName + "]";
	}
	
	
	
	
}
