package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class KisanVikasPatraDTO {
	
	@NotNull(message="deposit should not be null")
	private double deposit;
	@NotNull(message="depositDate should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date depositDate;
	
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}
	public Date getDepositDate() {
		return depositDate;
	}
	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}
	
	@Override
	public String toString() {
		return "KisanVikasPatraDTO [deposit=" + deposit + ", depositDate=" + depositDate + "]";
	}
	
	

}
