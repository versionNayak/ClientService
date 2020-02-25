package com.finlabs.finexa.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finlabs.finexa.resources.model.SimpleLoanCalLookup;
import com.finlabs.finexa.resources.model.SimpleLoanCalculator;

public class SimpleLoanEMIBasedCalculatorDTO {
	
	@NotNull(message = "loan_original_flag should be Y")
	private String loan_original_flag;
	@NotNull(message = "interestRate should not be null")
	private double interestRate;
	@NotNull(message = "loanAmount should not be null")
	private double loanAmount; 
	@NotNull(message = "loanAmount should not be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date loanStartDate; 
	@NotNull(message = "emiAmount should not be null")
	private double emiAmount; 
	@NotNull(message = "loanTenure should not be null")
	private int loanTenure;
	
	private List<SimpleLoanCalLookup> simpleLoanCalLookup;
	
	private SimpleLoanCalculator simpleLoanCal;

	public String getLoan_original_flag() {
		return loan_original_flag;
	}

	public void setLoan_original_flag(String loan_original_flag) {
		this.loan_original_flag = loan_original_flag;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Date getLoanStartDate() {
		return loanStartDate;
	}

	public void setLoanStartDate(Date loanStartDate) {
		this.loanStartDate = loanStartDate;
	}

	public double getEmiAmount() {
		return emiAmount;
	}

	public void setEmiAmount(double emiAmount) {
		this.emiAmount = emiAmount;
	}

	public int getLoanTenure() {
		return loanTenure;
	}

	public void setLoanTenure(int loanTenure) {
		this.loanTenure = loanTenure;
	}

	public List<SimpleLoanCalLookup> getSimpleLoanCalLookup() {
		return simpleLoanCalLookup;
	}

	public void setSimpleLoanCalLookup(List<SimpleLoanCalLookup> simpleLoanCalLookup) {
		this.simpleLoanCalLookup = simpleLoanCalLookup;
	}

	public SimpleLoanCalculator getSimpleLoanCal() {
		return simpleLoanCal;
	}

	public void setSimpleLoanCal(SimpleLoanCalculator simpleLoanCal) {
		this.simpleLoanCal = simpleLoanCal;
	}

	@Override
	public String toString() {
		return "SimpleLoanEMIBasedCalculatorDTO [loan_original_flag=" + loan_original_flag + ", interestRate="
				+ interestRate + ", loanAmount=" + loanAmount + ", loanStartDate=" + loanStartDate + ", emiAmount="
				+ emiAmount + ", loanTenure=" + loanTenure + ", simpleLoanCalLookup=" + simpleLoanCalLookup
				+ ", simpleLoanCal=" + simpleLoanCal + "]";
	}
	
	
	

}
