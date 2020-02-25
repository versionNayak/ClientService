package com.finlabs.finexa.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PPFFixedAmountCalculatorDTO {

	private double deposit;
	private String tenureType;
	private int tenure;
	private int rdDepositFreq;
	private int compundingFreq;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date depositDate;
	private int extensionFlag;

	private double depositAmountExt;
	private String tenureTypeExt;
	private int depositFrequencyExt;
	private int compoundingFrequencyExt;
	private int termExt;
	private double currentBalance;

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public String getTenureType() {
		return tenureType;
	}

	public void setTenureType(String tenureType) {
		this.tenureType = tenureType;
	}

	public int getTenure() {
		return tenure;
	}

	public void setTenure(int tenure) {
		this.tenure = tenure;
	}

	public int getRdDepositFreq() {
		return rdDepositFreq;
	}

	public void setRdDepositFreq(int rdDepositFreq) {
		this.rdDepositFreq = rdDepositFreq;
	}

	public int getCompundingFreq() {
		return compundingFreq;
	}

	public void setCompundingFreq(int compundingFreq) {
		this.compundingFreq = compundingFreq;
	}

	public Date getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	public int getExtensionFlag() {
		return extensionFlag;
	}

	public void setExtensionFlag(int extensionFlag) {
		this.extensionFlag = extensionFlag;
	}

	public double getDepositAmountExt() {
		return depositAmountExt;
	}

	public void setDepositAmountExt(double depositAmountExt) {
		this.depositAmountExt = depositAmountExt;
	}

	public String getTenureTypeExt() {
		return tenureTypeExt;
	}

	public void setTenureTypeExt(String tenureTypeExt) {
		this.tenureTypeExt = tenureTypeExt;
	}

	public int getDepositFrequencyExt() {
		return depositFrequencyExt;
	}

	public void setDepositFrequencyExt(int depositFrequencyExt) {
		this.depositFrequencyExt = depositFrequencyExt;
	}

	public int getCompoundingFrequencyExt() {
		return compoundingFrequencyExt;
	}

	public void setCompoundingFrequencyExt(int compoundingFrequencyExt) {
		this.compoundingFrequencyExt = compoundingFrequencyExt;
	}

	public int getTermExt() {
		return termExt;
	}

	public void setTermExt(int termExt) {
		this.termExt = termExt;
	}
	

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	@Override
	public String toString() {
		return "PPFFixedAmountCalculatorDTO [deposit=" + deposit + ", tenureType=" + tenureType + ", tenure=" + tenure
				+ ", rdDepositFreq=" + rdDepositFreq + ", compundingFreq=" + compundingFreq + ", depositDate="
				+ depositDate + ", extensionFlag=" + extensionFlag + ", depositAmountExt=" + depositAmountExt
				+ ", tenureTypeExt=" + tenureTypeExt + ", depositFrequencyExt=" + depositFrequencyExt
				+ ", compoundingFrequencyExt=" + compoundingFrequencyExt + ", termExt=" + termExt + "]";
	}

	

}
