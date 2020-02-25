package com.finlabs.finexa.dto;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FinexaSubscriptionDTO {
	private int id;
	
	private int clientNumber;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date dateOfSubscription;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date dateOfExpairy;
	
	
	private String promoCode;
	
	private BigDecimal subscriptionAmount;
      
	private int subscriptionPeriod;

	private int userNumber;
	
	private int advisorId;
	
	private List<String> modules ;
	
	private String moduleName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(int clientNumber) {
		this.clientNumber = clientNumber;
	}

	public Date getDateOfSubscription() {
		return dateOfSubscription;
	}

	public void setDateOfSubscription(Date dateOfSubscription) {
		this.dateOfSubscription = dateOfSubscription;
	}

	public Date getDateOfExpairy() {
		return dateOfExpairy;
	}

	public void setDateOfExpairy(Date dateOfExpairy) {
		this.dateOfExpairy = dateOfExpairy;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public BigDecimal getSubscriptionAmount() {
		return subscriptionAmount;
	}

	public void setSubscriptionAmount(BigDecimal subscriptionAmount) {
		this.subscriptionAmount = subscriptionAmount;
	}

	public int getSubscriptionPeriod() {
		return subscriptionPeriod;
	}

	public void setSubscriptionPeriod(int subscriptionPeriod) {
		this.subscriptionPeriod = subscriptionPeriod;
	}

	public int getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(int userNumber) {
		this.userNumber = userNumber;
	}

	public int getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}

	public List<String> getModules() {
		return modules;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Override
	public String toString() {
		return "FinexaSubscriptionDTO [id=" + id + ", clientNumber=" + clientNumber + ", dateOfSubscription="
				+ dateOfSubscription + ", dateOfExpairy=" + dateOfExpairy + ", promoCode=" + promoCode
				+ ", subscriptionAmount=" + subscriptionAmount + ", subscriptionPeriod=" + subscriptionPeriod
				+ ", userNumber=" + userNumber + ", advisorId=" + advisorId + ", modules=" + modules + ", moduleName="
				+ moduleName + "]";
	}
	

	
}
