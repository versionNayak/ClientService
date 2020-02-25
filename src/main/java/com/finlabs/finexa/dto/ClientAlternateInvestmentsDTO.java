package com.finlabs.finexa.dto;

import java.math.BigDecimal;

public class ClientAlternateInvestmentsDTO {

	private int id;
	private int financialAssetType;
	private String financialAssetTypeName;
	private String ownerName;
	private String assetDescription;
	private String assetTypeName;
	private BigDecimal currentValue;

	public ClientAlternateInvestmentsDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFinancialAssetTypeName() {
		return financialAssetTypeName;
	}

	public void setFinancialAssetTypeName(String financialAssetTypeName) {
		this.financialAssetTypeName = financialAssetTypeName;
	}

	public String getAssetDescription() {
		return assetDescription;
	}

	public void setAssetDescription(String assetDescription) {
		this.assetDescription = assetDescription;
	}

	public String getAssetTypeName() {
		return assetTypeName;
	}

	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}

	public BigDecimal getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getFinancialAssetType() {
		return financialAssetType;
	}

	public void setFinancialAssetType(int financialAssetType) {
		this.financialAssetType = financialAssetType;
	}

	@Override
	public String toString() {
		return "ClientAlternateInvestmentsDTO [id=" + id + ", financialAssetType=" + financialAssetType
				+ ", financialAssetTypeName=" + financialAssetTypeName + ", ownerName=" + ownerName
				+ ", assetDescription=" + assetDescription + ", assetTypeName=" + assetTypeName + ", currentValue="
				+ currentValue + "]";
	}
	
	
	
}
