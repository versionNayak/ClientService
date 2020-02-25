package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;



public class MasterIncomeGrowthRateDTO {
	
	private int id;
    private BigDecimal cagr;
    private Date fromDate;
    private String incomeCategory;
    private Date toDate;
	
    public MasterIncomeGrowthRateDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getCagr() {
		return cagr;
	}

	public void setCagr(BigDecimal cagr) {
		this.cagr = cagr;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getIncomeCategory() {
		return incomeCategory;
	}

	public void setIncomeCategory(String incomeCategory) {
		this.incomeCategory = incomeCategory;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
    
    

}
