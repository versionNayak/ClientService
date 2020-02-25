package com.finlabs.finexa.dto;


import java.math.BigDecimal;



public class ClientNPSAssetClassReturnDTO {
	private BigDecimal assetClassEReturn;
	private BigDecimal assetClassCReturn;	
	private BigDecimal assetClassGReturn;	
	private BigDecimal autoPlanReturn;

	public ClientNPSAssetClassReturnDTO() {
	}

	public BigDecimal getAssetClassEReturn() {
		return assetClassEReturn;
	}

	public void setAssetClassEReturn(BigDecimal assetClassEReturn) {
		this.assetClassEReturn = assetClassEReturn;
	}

	public BigDecimal getAssetClassCReturn() {
		return assetClassCReturn;
	}

	public void setAssetClassCReturn(BigDecimal assetClassCReturn) {
		this.assetClassCReturn = assetClassCReturn;
	}

	public BigDecimal getAssetClassGReturn() {
		return assetClassGReturn;
	}

	public void setAssetClassGReturn(BigDecimal assetClassGReturn) {
		this.assetClassGReturn = assetClassGReturn;
	}

	public BigDecimal getAutoPlanReturn() {
		return autoPlanReturn;
	}

	public void setAutoPlanReturn(BigDecimal autoPlanReturn) {
		this.autoPlanReturn = autoPlanReturn;
	}

}