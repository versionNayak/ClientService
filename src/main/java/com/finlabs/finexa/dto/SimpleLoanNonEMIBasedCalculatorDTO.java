package com.finlabs.finexa.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finlabs.finexa.resources.model.SimpleLoanCalLookup;
import com.finlabs.finexa.resources.model.SimpleLoanCalculator;

public class SimpleLoanNonEMIBasedCalculatorDTO {
	
	@NotNull(message = "loanAmount should not be null")
	private double loanAmount;
	@NotNull(message = "interestRate should not be null")
	private double interestRate;
	@NotNull(message = "loanTenure should not be null")
	private int loanTenure;
	@NotNull(message = "interestPaymentFrequency should not be null")
	private int interestPaymentFrequency;
	@NotNull(message = "loanStartDate should not be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date loanStartDate;
	
	private List<SimpleLoanCalLookup> simpleLoanCalLookup;
	
	private SimpleLoanCalculator simpleLoanCal;

	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public int getLoanTenure() {
		return loanTenure;
	}

	public void setLoanTenure(int loanTenure) {
		this.loanTenure = loanTenure;
	}

	public int getInterestPaymentFrequency() {
		return interestPaymentFrequency;
	}

	public void setInterestPaymentFrequency(int interestPaymentFrequency) {
		this.interestPaymentFrequency = interestPaymentFrequency;
	}

	public Date getLoanStartDate() {
		return loanStartDate;
	}

	public void setLoanStartDate(Date loanStartDate) {
		this.loanStartDate = loanStartDate;
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
		return "SimpleLoanNonEMIBasedalculatorDTO [loanAmount=" + loanAmount + ", interestRate=" + interestRate
				+ ", loanTenure=" + loanTenure + ", interestPaymentFrequency=" + interestPaymentFrequency
				+ ", loanStartDate=" + loanStartDate + ", simpleLoanCalLookup=" + simpleLoanCalLookup
				+ ", simpleLoanCal=" + simpleLoanCal + "]";
	}
	
	
	
	

}
