package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SukanyaSamriddhiSchemeCalculatorDTO {
	
	@NotNull(message="deposit should not be null")
	private double deposit; 
	private String tenureType;
	@NotNull(message="paymenttenure should not be null")
	private int paymenttenure;
	@NotNull(message="rdDepositFreq should not be null")
	private int rdDepositFreq; 
	@NotNull(message="compoundingFreq should not be null")
	private int compoundingFreq; 
	@NotNull(message="depositDate should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date depositDate; 
	@NotNull(message="maturitytenure should not be null")
	private int maturitytenure;
	
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}
	public int getPaymenttenure() {
		return paymenttenure;
	}
	public void setPaymenttenure(int paymenttenure) {
		this.paymenttenure = paymenttenure;
	}
	public int getRdDepositFreq() {
		return rdDepositFreq;
	}
	public void setRdDepositFreq(int rdDepositFreq) {
		this.rdDepositFreq = rdDepositFreq;
	}
	public int getCompoundingFreq() {
		return compoundingFreq;
	}
	public void setCompoundingFreq(int compoundingFreq) {
		this.compoundingFreq = compoundingFreq;
	}
	public Date getDepositDate() {
		return depositDate;
	}
	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}
	public int getMaturitytenure() {
		return maturitytenure;
	}
	public void setMaturitytenure(int maturitytenure) {
		this.maturitytenure = maturitytenure;
	}
	
	public String getTenureType() {
		return tenureType;
	}
	public void setTenureType(String tenureType) {
		this.tenureType = tenureType;
	}
	
	@Override
	public String toString() {
		return "SukanyaSamriddhiSchemeCalculatorDTO [deposit=" + deposit + ", tenureType=" + tenureType
				+ ", paymenttenure=" + paymenttenure + ", rdDepositFreq=" + rdDepositFreq + ", compoundingFreq="
				+ compoundingFreq + ", depositDate=" + depositDate + ", maturitytenure=" + maturitytenure + "]";
	}
	
	
	
	

}
