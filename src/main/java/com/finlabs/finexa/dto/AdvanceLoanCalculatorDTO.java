package com.finlabs.finexa.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AdvanceLoanCalculatorDTO {
	
	private int loanType ;
	private double orignalLoanAmount ;
	private double annualInterestRate;
	private int loanTenure;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date startDateEMI ;
	
	List<AdvLoanActionDTO> actionList ;

	public int getLoanType() {
		return loanType;
	}

	public void setLoanType(int loanType) {
		this.loanType = loanType;
	}

	public double getOrignalLoanAmount() {
		return orignalLoanAmount;
	}

	public void setOrignalLoanAmount(double orignalLoanAmount) {
		this.orignalLoanAmount = orignalLoanAmount;
	}

	public double getAnnualInterestRate() {
		return annualInterestRate;
	}

	public void setAnnualInterestRate(double annualInterestRate) {
		this.annualInterestRate = annualInterestRate;
	}

	public int getLoanTenure() {
		return loanTenure;
	}

	public void setLoanTenure(int loanTenure) {
		this.loanTenure = loanTenure;
	}

	public Date getStartDateEMI() {
		return startDateEMI;
	}

	public void setStartDateEMI(Date startDateEMI) {
		this.startDateEMI = startDateEMI;
	}

	public List<AdvLoanActionDTO> getActionList() {
		return actionList;
	}

	public void setActionList(List<AdvLoanActionDTO> actionList) {
		this.actionList = actionList;
	}
	
	

}
