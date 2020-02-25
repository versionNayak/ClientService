package com.finlabs.finexa.dto;


import java.util.Date;

public class MasterKVPCompoundingFrequencyDTO {
	
	private int id;
    private Date depositFromDate;
    private Date depositToDate;
	private int compoundingFrequency;
	
    public MasterKVPCompoundingFrequencyDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDepositFromDate() {
		return depositFromDate;
	}

	public void setDepositFromDate(Date depositFromDate) {
		this.depositFromDate = depositFromDate;
	}

	public Date getDepositToDate() {
		return depositToDate;
	}

	public void setDepositToDate(Date depositToDate) {
		this.depositToDate = depositToDate;
	}

	public int getCompoundingFrequency() {
		return this.compoundingFrequency;
	}

	public void setCompoundingFrequency(int compoundingFrequency) {
		this.compoundingFrequency = compoundingFrequency;
	}
    

}
