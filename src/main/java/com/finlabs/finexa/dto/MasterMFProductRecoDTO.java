package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



/**
 * The persistent class for the masterMFProductRecommendation database table.
 * 
 */

public class MasterMFProductRecoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private int advisorID;
	private Integer amfiCode;
	private String schemeName;
	private String isin;
	private String startDate;
	private String endDate;
	private int assetClassID;
	private int subAssetClassID;

	

	public String getIsin() {
		return isin;
	}


	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public int getAdvisorID() {
		return advisorID;
	}


	public void setAdvisorID(int advisorID) {
		this.advisorID = advisorID;
	}


	public Integer getAmfiCode() {
		return amfiCode;
	}


	public void setAmfiCode(Integer amfiCode) {
		this.amfiCode = amfiCode;
	}


	public String getSchemeName() {
		return schemeName;
	}


	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}


	

	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public int getAssetClassID() {
		return assetClassID;
	}


	public void setAssetClassID(int assetClassID) {
		this.assetClassID = assetClassID;
	}


	public int getSubAssetClassID() {
		return subAssetClassID;
	}


	public void setSubAssetClassID(int subAssetClassID) {
		this.subAssetClassID = subAssetClassID;
	}


	



}