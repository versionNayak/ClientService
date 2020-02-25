package com.finlabs.finexa.dto;

import java.util.Date;


public class MasterMutualFundETFDTO {
	
	private String isin;
	private String amfiCode;
	private Integer assetClassID;
	private Integer benchmarkIndex;
	private String closeEndedFlag;
	private String exitLoadAndPeriod;
	private String fundHouse;
	private Integer fundManagerCode;
	private Float minInvestmentAmount;
	private String regularDirectFlag;
	private Date schemeEndDate;
	private Date schemeInceptionDate;
	private String schemeName;
	private String schemeOption;
	private Integer subAssetClassID;
	
	/*private String csvFileName;
	
	private List<String> headers;*/
	
	
	public MasterMutualFundETFDTO() {
		super();
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getAmfiCode() {
		return amfiCode;
	}

	public void setAmfiCode(String amfiCode) {
		this.amfiCode = amfiCode;
	}

	public Integer getBenchmarkIndex() {
		return benchmarkIndex;
	}

	public void setBenchmarkIndex(Integer benchmarkIndex) {
		this.benchmarkIndex = benchmarkIndex;
	}

	public String getCloseEndedFlag() {
		return closeEndedFlag;
	}

	public void setCloseEndedFlag(String closeEndedFlag) {
		this.closeEndedFlag = closeEndedFlag;
	}

	public String getExitLoadAndPeriod() {
		return exitLoadAndPeriod;
	}

	public void setExitLoadAndPeriod(String exitLoadAndPeriod) {
		this.exitLoadAndPeriod = exitLoadAndPeriod;
	}

	public String getFundHouse() {
		return fundHouse;
	}

	public void setFundHouse(String fundHouse) {
		this.fundHouse = fundHouse;
	}

	public Integer getFundManagerCode() {
		return fundManagerCode;
	}

	public void setFundManagerCode(Integer fundManagerCode) {
		this.fundManagerCode = fundManagerCode;
	}

	public Float getMinInvestmentAmount() {
		return minInvestmentAmount;
	}

	public void setMinInvestmentAmount(Float minInvestmentAmount) {
		this.minInvestmentAmount = minInvestmentAmount;
	}

	public String getRegularDirectFlag() {
		return regularDirectFlag;
	}

	public void setRegularDirectFlag(String regularDirectFlag) {
		this.regularDirectFlag = regularDirectFlag;
	}

	public Date getSchemeEndDate() {
		return schemeEndDate;
	}

	public void setSchemeEndDate(Date schemeEndDate) {
		this.schemeEndDate = schemeEndDate;
	}

	public Date getSchemeInceptionDate() {
		return schemeInceptionDate;
	}

	public void setSchemeInceptionDate(Date schemeInceptionDate) {
		this.schemeInceptionDate = schemeInceptionDate;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getSchemeOption() {
		return schemeOption;
	}

	public void setSchemeOption(String schemeOption) {
		this.schemeOption = schemeOption;
	}

	public Integer getAssetClassID() {
		return assetClassID;
	}

	public void setAssetClassID(Integer assetClassID) {
		this.assetClassID = assetClassID;
	}

	public Integer getSubAssetClassID() {
		return subAssetClassID;
	}

	public void setSubAssetClassID(Integer subAssetClassID) {
		this.subAssetClassID = subAssetClassID;
	}

	@Override
	public String toString() {
		return "MasterMutualFundETFDTO [isin=" + isin + ", amfiCode=" + amfiCode + ", assetClassID=" + assetClassID
				+ ", benchmarkIndex=" + benchmarkIndex + ", closeEndedFlag=" + closeEndedFlag + ", exitLoadAndPeriod="
				+ exitLoadAndPeriod + ", fundHouse=" + fundHouse + ", fundManagerCode=" + fundManagerCode
				+ ", minInvestmentAmount=" + minInvestmentAmount + ", regularDirectFlag=" + regularDirectFlag
				+ ", schemeEndDate=" + schemeEndDate + ", schemeInceptionDate=" + schemeInceptionDate + ", schemeName="
				+ schemeName + ", schemeOption=" + schemeOption + ", subAssetClassID=" + subAssetClassID + "]";
	}

	
	

}
