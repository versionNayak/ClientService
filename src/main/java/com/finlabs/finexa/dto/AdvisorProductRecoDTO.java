package com.finlabs.finexa.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;



public class AdvisorProductRecoDTO {
	
	private int id;
	private int adviorID;
	private int clientID;
	private int goalID;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date recoSaveDate;
	private String module;
	private String productPlan;
	private int option;
	
	public int getAdviorID() {
		return adviorID;
	}

	public void setAdviorID(int adviorID) {
		this.adviorID = adviorID;
	}
	
	public String getProductPlan() {
		return productPlan;
	}

	public void setProductPlan(String productPlan) {
		this.productPlan = productPlan;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getRecoSaveDate() {
		return recoSaveDate;
	}

	public void setRecoSaveDate(Date recoSaveDate) {
		this.recoSaveDate = recoSaveDate;
	}

	public int getOption() {
		return option;
	}

	public void setOption(int option) {
		this.option = option;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public int getGoalID() {
		return goalID;
	}

	public void setGoalID(int goalID) {
		this.goalID = goalID;
	}

	
}
