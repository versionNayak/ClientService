package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MutualFundLumpsumPCDTO {

	@NotNull(message = "amountInvested should not be Null")
	private double amountInvested;
	//@NotNull(message = "amfiCode should not be Null")
	private Integer amfiCode;
	@NotNull(message = "investedDate should not be Null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date investedDate;
	@NotNull(message = "unitPurchased should not be Null")
	private int unitPurchased;
	@NotNull(message = "isin should not be Null")
	private String isin;

	public double getAmountInvested() {
		return amountInvested;
	}

	public void setAmountInvested(double amountInvested) {
		this.amountInvested = amountInvested;
	}

	public Integer getAmfiCode() {
		return amfiCode;
	}

	public void setAmfiCode(Integer amfiCode) {
		this.amfiCode = amfiCode;
	}

	public Date getInvestedDate() {
		return investedDate;
	}

	public void setInvestedDate(Date investedDate) {
		this.investedDate = investedDate;
	}

	public int getUnitPurchased() {
		return unitPurchased;
	}

	public void setUnitPurchased(int unitPurchased) {
		this.unitPurchased = unitPurchased;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	@Override
	public String toString() {
		return "MutualFundLumpsumPCDTO [amountInvested=" + amountInvested + ", amfiCode=" + amfiCode + ", investedDate="
				+ investedDate + ", unitPurchased=" + unitPurchased + "]";
	}

}
