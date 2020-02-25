package com.finlabs.finexa.dto;


import java.util.Date;

public class MasterMFHoldingSectorWiseDTO {
	
	private Date date;
	private String amfiCode;
	private String ISIN;
	private String scheme;
	private byte sectorID;
	private double holding;
	
	public MasterMFHoldingSectorWiseDTO() {
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

	public String getISIN() {
		return ISIN;
	}

	public void setISIN(String iSIN) {
		ISIN = iSIN;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public byte getSectorID() {
		return sectorID;
	}

	public void setSectorID(byte sectorID) {
		this.sectorID = sectorID;
	}

	public double getHolding() {
		return holding;
	}

	public void setHolding(double holding) {
		this.holding = holding;
	}
	
	

}
