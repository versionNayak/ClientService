package com.finlabs.finexa.dto;

import java.util.Date;

public class MasterMFHoldingRatingWiseDTO {
	
	private Date date;
	private String amfiCode;
	private String isin;
	private String schemeName;
	private byte ratingID;
	private double holding;
	
	public MasterMFHoldingRatingWiseDTO() {
		super();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public byte getRatingID() {
		return ratingID;
	}

	public void setRatingID(byte ratingID) {
		this.ratingID = ratingID;
	}

	public double getHolding() {
		return holding;
	}

	public void setHolding(double holding) {
		this.holding = holding;
	}
	
	

}
