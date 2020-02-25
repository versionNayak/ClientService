package com.finlabs.finexa.dto;


import java.util.Date;



public class MasterKVPTermDTO {
	
	private int id;
    private Date depositFromDate;
    private Date depositToDate;
	private int termMonths;
	private int termYears;
	
    public MasterKVPTermDTO() {
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

	public int getTermMonths() {
		return this.termMonths;
	}

	public void setTermMonths(int termMonths) {
		this.termMonths = termMonths;
	}

	public int getTermYears() {
		return this.termYears;
	}

	public void setTermYears(int termYears) {
		this.termYears = termYears;
	}
    
    

}
