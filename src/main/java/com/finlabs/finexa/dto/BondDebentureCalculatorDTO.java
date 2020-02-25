package com.finlabs.finexa.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finlabs.finexa.resources.model.BankBondDebenturesLookup;
import com.finlabs.finexa.resources.model.BondDebentures;

public class BondDebentureCalculatorDTO {

	@NotNull(message = "Tenure type should be D(Days) or Y(Years)")
	private String tenureType;
	@NotNull(message = "Investment date should not be Null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date investmentDate;
	@NotNull(message = "Tenure should be number of Days/Years")
	private String tenure;
	@NotNull(message = "Coupon Payout Frequency should not be Null")
	private int coupounPayoutFrequency;
	@NotNull(message = "Interest Coupon rate should not be Null")
	private double interestCouponRate;
	@NotNull(message = "Bond face value should not be Null")
	private double bondFaceValue;
	@NotNull(message = "Number of Bonds Purchased should not be Null")
	private int numberOfBondsPurchased;
	@NotNull(message = "Current yeild should not be Null")
	private double currentYield;

	private List<BankBondDebenturesLookup> bankBondDebenturesLookup;

	private BondDebentures bondDebCal;

	public String getTenureType() {
		return tenureType;
	}

	public void setTenureType(String tenureType) {
		this.tenureType = tenureType;
	}

	public Date getInvestmentDate() {
		return investmentDate;
	}

	public void setInvestmentDate(Date investmentDate) {
		this.investmentDate = investmentDate;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public int getCoupounPayoutFrequency() {
		return coupounPayoutFrequency;
	}

	public void setCoupounPayoutFrequency(int coupounPayoutFrequency) {
		this.coupounPayoutFrequency = coupounPayoutFrequency;
	}

	public double getInterestCouponRate() {
		return interestCouponRate;
	}

	public void setInterestCouponRate(double interestCouponRate) {
		this.interestCouponRate = interestCouponRate;
	}

	public double getBondFaceValue() {
		return bondFaceValue;
	}

	public void setBondFaceValue(double bondFaceValue) {
		this.bondFaceValue = bondFaceValue;
	}

	public int getNumberOfBondsPurchased() {
		return numberOfBondsPurchased;
	}

	public void setNumberOfBondsPurchased(int numberOfBondsPurchased) {
		this.numberOfBondsPurchased = numberOfBondsPurchased;
	}

	public double getCurrentYield() {
		return currentYield;
	}

	public void setCurrentYield(double currentYield) {
		this.currentYield = currentYield;
	}

	public List<BankBondDebenturesLookup> getBankBondDebenturesLookup() {
		return bankBondDebenturesLookup;
	}

	public void setBankBondDebenturesLookup(List<BankBondDebenturesLookup> bankBondDebenturesLookup) {
		this.bankBondDebenturesLookup = bankBondDebenturesLookup;
	}

	public BondDebentures getBondDebCal() {
		return bondDebCal;
	}

	public void setBondDebCal(BondDebentures bondDebCal) {
		this.bondDebCal = bondDebCal;
	}

	@Override
	public String toString() {
		return "BondDebentureCalculatorDTO [tenureType=" + tenureType + ", investmentDate=" + investmentDate
				+ ", tenure=" + tenure + ", coupounPayoutFrequency=" + coupounPayoutFrequency + ", interestCouponRate="
				+ interestCouponRate + ", bondFaceValue=" + bondFaceValue + ", numberOfBondsPurchased="
				+ numberOfBondsPurchased + ", currentYield=" + currentYield + "]";
	}

}
