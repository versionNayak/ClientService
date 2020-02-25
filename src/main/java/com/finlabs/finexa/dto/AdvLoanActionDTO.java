package com.finlabs.finexa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AdvLoanActionDTO {
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private String actionDate ;
	private String action;
	private String prepaymentAmount;
	private String newInterestRate ;
	private String changeInEmi ;
	private String changeInTenure ;
	public String getActionDate() {
		return actionDate;
	}
	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getPrepaymentAmount() {
		return prepaymentAmount;
	}
	public void setPrepaymentAmount(String prepaymentAmount) {
		this.prepaymentAmount = prepaymentAmount;
	}
	public String getNewInterestRate() {
		return newInterestRate;
	}
	public void setNewInterestRate(String newInterestRate) {
		this.newInterestRate = newInterestRate;
	}
	public String getChangeInEmi() {
		return changeInEmi;
	}
	public void setChangeInEmi(String changeInEmi) {
		this.changeInEmi = changeInEmi;
	}
	public String getChangeInTenure() {
		return changeInTenure;
	}
	public void setChangeInTenure(String changeInTenure) {
		this.changeInTenure = changeInTenure;
	}
	
	
}
