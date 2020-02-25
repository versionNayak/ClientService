package com.finlabs.finexa.dto;

public class AdvisorForgotPasswordDTO {
	private int id;
	private int clientId;
	private int advisorID;
	private String uuid;
	private Double timestamp;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getAdvisorID() {
		return advisorID;
	}
	public void setAdvisorID(int advisorID) {
		this.advisorID = advisorID;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Double getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Double timestamp) {
		this.timestamp = timestamp;
	}
	

}
