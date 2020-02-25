package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EPF2CalculatorDTO {

	@NotNull(message = "clientDOB should not be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date clientDOB;
	@NotNull(message = "currEPFBal should not be null")
	private double currEPFBal;
	@NotNull(message = "currEPSBal should not be null")
	private double currEPSBal;
	@NotNull(message = "monthlyBasicDA should not be null")
	private double monthlyBasicDA;
	@NotNull(message = "expectedIncreaseSal should not be null")
	private double expectedIncreaseSal;
	@NotNull(message = "contributionUptoAge should not be null")
	private int contributionUptoAge;
	@NotNull(message = "withdrawalAgeUpto should not be null")
	private int withdrawalAgeUpto;
	@NotNull(message = "increaseMonth should not be null")
	private String increaseMonth;

	public Date getClientDOB() {
		return clientDOB;
	}

	public void setClientDOB(Date clientDOB) {
		this.clientDOB = clientDOB;
	}

	public double getCurrEPFBal() {
		return currEPFBal;
	}

	public void setCurrEPFBal(double currEPFBal) {
		this.currEPFBal = currEPFBal;
	}

	public double getCurrEPSBal() {
		return currEPSBal;
	}

	public void setCurrEPSBal(double currEPSBal) {
		this.currEPSBal = currEPSBal;
	}

	public double getMonthlyBasicDA() {
		return monthlyBasicDA;
	}

	public void setMonthlyBasicDA(double monthlyBasicDA) {
		this.monthlyBasicDA = monthlyBasicDA;
	}

	public double getExpectedIncreaseSal() {
		return expectedIncreaseSal;
	}

	public void setExpectedIncreaseSal(double expectedIncreaseSal) {
		this.expectedIncreaseSal = expectedIncreaseSal;
	}

	public int getContributionUptoAge() {
		return contributionUptoAge;
	}

	public void setContributionUptoAge(int contributionUptoAge) {
		this.contributionUptoAge = contributionUptoAge;
	}

	public String getIncreaseMonth() {
		return increaseMonth;
	}

	public void setIncreaseMonth(String increaseMonth) {
		this.increaseMonth = increaseMonth;
	}

	public int getWithdrawalAgeUpto() {
		return withdrawalAgeUpto;
	}

	public void setWithdrawalAgeUpto(int withdrawalAgeUpto) {
		this.withdrawalAgeUpto = withdrawalAgeUpto;
	}

	@Override
	public String toString() {
		return "EPF2CalculatorDTO [clientDOB=" + clientDOB + ", currEPFBal=" + currEPFBal + ", currEPSBal=" + currEPSBal
				+ ", monthlyBasicDA=" + monthlyBasicDA + ", expectedIncreaseSal=" + expectedIncreaseSal
				+ ", contributionUptoAge=" + contributionUptoAge + ", withdrawalAgeUpto=" + withdrawalAgeUpto
				+ ", increaseMonth=" + increaseMonth + "]";
	}

}
