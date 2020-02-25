package com.finlabs.finexa.dto;

public class FundSchemeIsinDTO {

	private String descriptiveSchemeName;
	private String schemeName;
	private String isin;
	private byte subAssetClassId;
	private byte assetClassId;

	public FundSchemeIsinDTO() {
		super();
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public byte getSubAssetClassId() {
		return subAssetClassId;
	}

	public void setSubAssetClassId(byte subAssetClassId) {
		this.subAssetClassId = subAssetClassId;
	}

	@Override
	public String toString() {
		return "FundSchemeIsinDTO [schemeName=" + schemeName + ", isin=" + isin + "]";
	}

	public String getDescriptiveSchemeName() {
		return descriptiveSchemeName;
	}

	public void setDescriptiveSchemeName(String descriptiveSchemeName) {
		this.descriptiveSchemeName = descriptiveSchemeName;
	}

	public byte getAssetClassId() {
		return assetClassId;
	}

	public void setAssetClassId(byte assetClassId) {
		this.assetClassId = assetClassId;
	}

}
