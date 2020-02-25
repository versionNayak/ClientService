package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;


public class MasterSukanyaSamriddhiInterestRateDTO {
	
	private int id;
    private Date endDate;
    private BigDecimal interestRate;
    private Date startDate;
	
    public MasterSukanyaSamriddhiInterestRateDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
    
    

}
