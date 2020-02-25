package com.finlabs.finexa.dto;

import java.util.List;

public class MasterProductRecommendationDTO {
	private String schemeName;
	private byte subAssetClassId;
	private String subAssetClass;
	private String assetClass;

	private String statusCode;
	private String statusMessage;

	private String isin;
	private int amfiCode;

	private int advisorId;

	private List<String> isinList;

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getSubAssetClass() {
		return subAssetClass;
	}

	public void setSubAssetClass(String subAssetClass) {
		this.subAssetClass = subAssetClass;
	}

	public String getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public int getAmfiCode() {
		return amfiCode;
	}

	public void setAmfiCode(int amfiCode) {
		this.amfiCode = amfiCode;
	}

	public int getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}

	public List<String> getIsinList() {
		return isinList;
	}

	public void setIsinList(List<String> isinList) {
		this.isinList = isinList;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public byte getSubAssetClassId() {
		return subAssetClassId;
	}

	public void setSubAssetClassId(byte subAssetClassId) {
		this.subAssetClassId = subAssetClassId;
	}

}
