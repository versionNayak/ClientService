package com.finlabs.finexa.dto;

import java.util.Date;

public class MasterMFHoldingMaturityDTO {
	
	private String amfiCode;
	private String schemeName;
	private double averageMaturityPeriod;
	private Date asOfDate;
	private double ytm;
	private double duration;
	private String action;
	
	public MasterMFHoldingMaturityDTO() {
		super();
	}

	public String getAmfiCode() {
		return amfiCode;
	}

	public void setAmfiCode(String amfiCode) {
		this.amfiCode = amfiCode;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public double getAverageMaturityPeriod() {
		return averageMaturityPeriod;
	}

	public void setAverageMaturityPeriod(double averageMaturityPeriod) {
		this.averageMaturityPeriod = averageMaturityPeriod;
	}

	public Date getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
	}

	public double getYtm() {
		return ytm;
	}

	public void setYtm(double ytm) {
		this.ytm = ytm;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	

}
