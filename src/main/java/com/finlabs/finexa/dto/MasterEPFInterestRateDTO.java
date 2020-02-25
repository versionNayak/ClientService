package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;



public class MasterEPFInterestRateDTO {
	
	private int id;
    private Date fromDate;
    private BigDecimal interestRate;
    private Date toDate;
	
    public MasterEPFInterestRateDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
    
    

}
