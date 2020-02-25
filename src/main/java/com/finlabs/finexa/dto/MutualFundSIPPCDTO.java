package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MutualFundSIPPCDTO {

	@NotNull(message = "sipAmount should not be Null")
	private double sipAmount;
	@NotNull(message = "amfiCode should not be Null")
	private Integer amfiCode;
	@NotNull(message = "sipStartDate should not be Null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date sipStartDate;
	@NotNull(message = "sipInstallment should not be Null")
	private int sipInstallment;
	@NotNull(message = "sipFrequency should not be Null")
	private int sipFrequency;
	@NotNull(message = "isin should not be Null")
	private String isin;

	public double getSipAmount() {
		return sipAmount;
	}

	public void setSipAmount(double sipAmount) {
		this.sipAmount = sipAmount;
	}

	public Integer getAmfiCode() {
		return amfiCode;
	}

	public void setAmfiCode(Integer amfiCode) {
		this.amfiCode = amfiCode;
	}

	public Date getSipStartDate() {
		return sipStartDate;
	}

	public void setSipStartDate(Date sipStartDate) {
		this.sipStartDate = sipStartDate;
	}

	public int getSipInstallment() {
		return sipInstallment;
	}

	public void setSipInstallment(int sipInstallment) {
		this.sipInstallment = sipInstallment;
	}

	public int getSipFrequency() {
		return sipFrequency;
	}

	public void setSipFrequency(int sipFrequency) {
		this.sipFrequency = sipFrequency;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	@Override
	public String toString() {
		return "MutualFundSIPPCDTO [sipAmount=" + sipAmount + ", amfiCode=" + amfiCode + ", sipStartDate="
				+ sipStartDate + ", sipInstallment=" + sipInstallment + ", sipFrequency=" + sipFrequency + "]";
	}

}
