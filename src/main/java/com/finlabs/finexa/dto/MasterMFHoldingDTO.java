package com.finlabs.finexa.dto;

import java.util.Date;

public class MasterMFHoldingDTO {
	
	private String amfiCode;
	private short companyCode;
	private short assetTypeCode;
	private Date assetDate;
	private Integer assetValue;
	private Integer numOfShares;
	private double assetPercentage;
	private double couponRate;
	private String maturity;
	private byte creditRatingCode;
	private String action;
	
	public MasterMFHoldingDTO() {
		super();
	}

	public String getAmfiCode() {
		return amfiCode;
	}

	public void setAmfiCode(String amfiCode) {
		this.amfiCode = amfiCode;
	}

	public short getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(short companyCode) {
		this.companyCode = companyCode;
	}

	public short getAssetTypeCode() {
		return assetTypeCode;
	}

	public void setAssetTypeCode(short assetTypeCode) {
		this.assetTypeCode = assetTypeCode;
	}

	public Date getAssetDate() {
		return assetDate;
	}

	public void setAssetDate(Date assetDate) {
		this.assetDate = assetDate;
	}

	public Integer getAssetValue() {
		return assetValue;
	}

	public void setAssetValue(Integer assetValue) {
		this.assetValue = assetValue;
	}

	public Integer getNumOfShares() {
		return numOfShares;
	}

	public void setNumOfShares(Integer numOfShares) {
		this.numOfShares = numOfShares;
	}

	public double getAssetPercentage() {
		return assetPercentage;
	}

	public void setAssetPercentage(double assetPercentage) {
		this.assetPercentage = assetPercentage;
	}

	public double getCouponRate() {
		return couponRate;
	}

	public void setCouponRate(double couponRate) {
		this.couponRate = couponRate;
	}

	public String getMaturity() {
		return maturity;
	}

	public void setMaturity(String maturity) {
		this.maturity = maturity;
	}

	public byte getCreditRatingCode() {
		return creditRatingCode;
	}

	public void setCreditRatingCode(byte creditRatingCode) {
		this.creditRatingCode = creditRatingCode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	
	
	
}
