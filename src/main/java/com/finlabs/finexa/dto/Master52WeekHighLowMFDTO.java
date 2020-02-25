package com.finlabs.finexa.dto;

public class Master52WeekHighLowMFDTO {
	
	private String amfiCode;
    private String action;
    private double nav52WeekHigh;
    private double nav52WeekLow;
    private String schemeName;
	
    public Master52WeekHighLowMFDTO() {
		super();
	}

	public String getAmfiCode() {
		return amfiCode;
	}

	public void setAmfiCode(String amfiCode) {
		this.amfiCode = amfiCode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public double getNav52WeekHigh() {
		return nav52WeekHigh;
	}

	public void setNav52WeekHigh(double nav52WeekHigh) {
		this.nav52WeekHigh = nav52WeekHigh;
	}

	public double getNav52WeekLow() {
		return nav52WeekLow;
	}

	public void setNav52WeekLow(double nav52WeekLow) {
		this.nav52WeekLow = nav52WeekLow;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
    
    

}
