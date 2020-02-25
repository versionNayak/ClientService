package com.finlabs.finexa.dto;

public class ConfigurationAutoCreateClientDTO{
	private int advisorId;
	public int getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}

	private String autoCreateClient;
	
	
	

	public String getAutoCreateClient() {
		return autoCreateClient;
	}

	public void setAutoCreateClient(String autoCreateClient) {
		this.autoCreateClient = autoCreateClient;
	}
}
