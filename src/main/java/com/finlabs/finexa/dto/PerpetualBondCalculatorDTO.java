package com.finlabs.finexa.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finlabs.finexa.resources.model.PerpetualBond;
import com.finlabs.finexa.resources.model.PerpetualBondLookup;

public class PerpetualBondCalculatorDTO {

	@NotNull(message = "tenureType should not be null")
	private String tenureType;
	@NotNull(message = "investmentDate should not be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date investmentDate;
	@NotNull(message = "tenure should not be null")
	private String tenure;
	@NotNull(message = "coupounPayoutFrequency should not be null")
	private int coupounPayoutFrequency;
	@NotNull(message = "interestCouponRate should not be null")
	private double interestCouponRate;
	@NotNull(message = "bondFaceValue should not be null")
	private double bondFaceValue;
	@NotNull(message = "numberOfBondsPurchased should not be null")
	private int numberOfBondsPurchased;
	@NotNull(message = "currentYield should not be null")
	private double currentYield;

	private List<PerpetualBondLookup> perpetualBondLookup;

	private PerpetualBond perpetualBondCal;

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

	public List<PerpetualBondLookup> getPerpetualBondLookup() {
		return perpetualBondLookup;
	}

	public void setPerpetualBondLookup(List<PerpetualBondLookup> perpetualBondLookup) {
		this.perpetualBondLookup = perpetualBondLookup;
	}

	public PerpetualBond getPerpetualBondCal() {
		return perpetualBondCal;
	}

	public void setPerpetualBondCal(PerpetualBond perpetualBondCal) {
		this.perpetualBondCal = perpetualBondCal;
	}

	@Override
	public String toString() {
		return "PerpetualBondCalculatorDTO [tenureType=" + tenureType + ", investmentDate=" + investmentDate
				+ ", tenure=" + tenure + ", coupounPayoutFrequency=" + coupounPayoutFrequency + ", interestCouponRate="
				+ interestCouponRate + ", bondFaceValue=" + bondFaceValue + ", numberOfBondsPurchased="
				+ numberOfBondsPurchased + ", currentYield=" + currentYield + ", perpetualBondLookup="
				+ perpetualBondLookup + ", perpetualBondCal=" + perpetualBondCal + "]";
	}

	

}
