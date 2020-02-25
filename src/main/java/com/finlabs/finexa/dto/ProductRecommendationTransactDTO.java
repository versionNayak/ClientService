package com.finlabs.finexa.dto;

import java.util.Date;

public class ProductRecommendationTransactDTO {

	private String subAssetClass;

	private String subAssetAlloPerc;

	private String selectAll;

	private String[] productName;
	
	private String[] productIsin;
	
	private double[] productValue;

	private Double[] lumpsumAmt;

	private  int[] sipTennure;

	private Double[] sipAmount;

	public String getSubAssetClass() {
		return subAssetClass;
	}

	public void setSubAssetClass(String subAssetClass) {
		this.subAssetClass = subAssetClass;
	}

	public String getSubAssetAlloPerc() {
		return subAssetAlloPerc;
	}

	public void setSubAssetAlloPerc(String subAssetAlloPerc) {
		this.subAssetAlloPerc = subAssetAlloPerc;
	}

	public String getSelectAll() {
		return selectAll;
	}

	public void setSelectAll(String selectAll) {
		this.selectAll = selectAll;
	}

	public String[] getProductName() {
		return productName;
	}

	public void setProductName(String[] productName) {
		this.productName = productName;
	}


	public double[] getProductValue() {
		return productValue;
	}

	public void setProductValue(double[] productValue) {
		this.productValue = productValue;
	}

	public Double[] getLumpsumAmt() {
		return lumpsumAmt;
	}

	public void setLumpsumAmt(Double[] lumpsumAmt) {
		this.lumpsumAmt = lumpsumAmt;
	}

	public int[] getSipTennure() {
		return sipTennure;
	}

	public void setSipTennure(int[] sipTennure) {
		this.sipTennure = sipTennure;
	}

	public Double[] getSipAmount() {
		return sipAmount;
	}

	public void setSipAmount(Double[] sipAmount) {
		this.sipAmount = sipAmount;
	}

	public String[] getProductIsin() {
		return productIsin;
	}

	public void setProductIsin(String[] productIsin) {
		this.productIsin = productIsin;
	}

	
}
