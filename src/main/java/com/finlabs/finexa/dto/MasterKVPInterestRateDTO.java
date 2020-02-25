package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;

public class MasterKVPInterestRateDTO {
	
	private int id;
    private Date depositFromDate;
    private Date depositToDate;
    private BigDecimal interestRate;
    /*private BigDecimal investmentPeriodInYears;*/
	
    public MasterKVPInterestRateDTO() {
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

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	/*public BigDecimal getInvestmentPeriodInYears() {
		return investmentPeriodInYears;
	}

	public void setInvestmentPeriodInYears(BigDecimal investmentPeriodInYears) {
		this.investmentPeriodInYears = investmentPeriodInYears;
	}*/
    
    

}
