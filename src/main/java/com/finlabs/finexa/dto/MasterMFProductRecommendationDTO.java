package com.finlabs.finexa.dto;

import java.util.Date;

public class MasterMFProductRecommendationDTO {
	
	private Integer advisorID;
	private String amfiCode;
	private String  isin;
	private String schemeName;
	private Date timeframeStartDate;
	private Date timeframeEndDate;
	
	public MasterMFProductRecommendationDTO() {
		super();
	}

	public Integer getAdvisorID() {
		return advisorID;
	}

	public void setAdvisorID(Integer advisorID) {
		this.advisorID = advisorID;
	}

	public String getAmfiCode() {
		return amfiCode;
	}

	public void setAmfiCode(String amfiCode) {
		this.amfiCode = amfiCode;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public Date getTimeframeStartDate() {
		return timeframeStartDate;
	}

	public void setTimeframeStartDate(Date timeframeStartDate) {
		this.timeframeStartDate = timeframeStartDate;
	}

	public Date getTimeframeEndDate() {
		return timeframeEndDate;
	}

	public void setTimeframeEndDate(Date timeframeEndDate) {
		this.timeframeEndDate = timeframeEndDate;
	}
	
	

}
