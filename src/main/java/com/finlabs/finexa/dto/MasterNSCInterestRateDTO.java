package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;

public class MasterNSCInterestRateDTO {
	
	private int id;
    private BigDecimal interestRate;
    private String issueId;
    private Date periodEnd;
    private Date periodStart;
	
    public MasterNSCInterestRateDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public Date getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(Date periodEnd) {
		this.periodEnd = periodEnd;
	}

	public Date getPeriodStart() {
		return periodStart;
	}

	public void setPeriodStart(Date periodStart) {
		this.periodStart = periodStart;
	}
    
    
}
