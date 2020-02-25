package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class MasterGoalInflationRateDTO {
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date fromDate;
	private BigDecimal inflationRate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date toDate;
	private int GoalType;
	public int getId() {
		return id;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public BigDecimal getInflationRate() {
		return inflationRate;
	}
	public Date getToDate() {
		return toDate;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public void setInflationRate(BigDecimal inflationRate) {
		this.inflationRate = inflationRate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public int getGoalType() {
		return GoalType;
	}
	public void setGoalType(int goalType) {
		GoalType = goalType;
	}
	
	
	
	

}
