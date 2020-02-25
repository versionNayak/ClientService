package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EquityCalculatorDTO {
	
	@NotNull(message="dateOfPurchased should not be Null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date dateOfPurchased;
	@NotNull(message="amountInvested should not be Null")
	private double amountInvested;
	@NotNull(message="noOfSharesPurchased should not be Null")
	private int noOfSharesPurchased;
	@NotNull(message="isin should not be Null")
	private String isin;
	
	public Date getDateOfPurchased() {
		return dateOfPurchased;
	}
	public void setDateOfPurchased(Date dateOfPurchased) {
		this.dateOfPurchased = dateOfPurchased;
	}
	public double getAmountInvested() {
		return amountInvested;
	}
	public void setAmountInvested(double amountInvested) {
		this.amountInvested = amountInvested;
	}
	public int getNoOfSharesPurchased() {
		return noOfSharesPurchased;
	}
	public void setNoOfSharesPurchased(int noOfSharesPurchased) {
		this.noOfSharesPurchased = noOfSharesPurchased;
	}
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	@Override
	public String toString() {
		return "EquityCalculatorDTO [dateOfPurchased=" + dateOfPurchased + ", amountInvested=" + amountInvested
				+ ", noOfSharesPurchased=" + noOfSharesPurchased + ", isin=" + isin + "]";
	}
	
	

}
