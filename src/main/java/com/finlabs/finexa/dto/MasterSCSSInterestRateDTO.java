package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;


public class MasterSCSSInterestRateDTO {
	
	private int id;
    private Date depositDateFrom;
    private Date depositDateTo;
    private BigDecimal interestRate;
	
    public MasterSCSSInterestRateDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDepositDateFrom() {
		return depositDateFrom;
	}

	public void setDepositDateFrom(Date depositDateFrom) {
		this.depositDateFrom = depositDateFrom;
	}

	public Date getDepositDateTo() {
		return depositDateTo;
	}

	public void setDepositDateTo(Date depositDateTo) {
		this.depositDateTo = depositDateTo;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
    
    

}
