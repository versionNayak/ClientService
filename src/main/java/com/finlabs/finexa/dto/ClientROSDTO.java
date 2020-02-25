package com.finlabs.finexa.dto;

public class ClientROSDTO {

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int financialAssetType;
	private String productName;
	private String ownerName;
	private String startValue;
	private double assetValue;
	private int epfId;
	private Byte annuityType;

	public int getFinancialAssetType() {
		return financialAssetType;
	}

	public void setFinancialAssetType(int financialAssetType) {
		this.financialAssetType = financialAssetType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getStartValue() {
		return startValue;
	}

	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}

	public double getAssetValue() {
		return assetValue;
	}

	public void setAssetValue(double assetValue) {
		this.assetValue = assetValue;
	}

	public int getEpfId() {
		return epfId;
	}

	public void setEpfId(int epfId) {
		this.epfId = epfId;
	}

	public Byte getAnnuityType() {
		return annuityType;
	}

	public void setAnnuityType(Byte annuityType) {
		this.annuityType = annuityType;
	}

}
