package com.finlabs.finexa.dto;

public class ClientRiskProfileResponseDTO {
	private int id;
	private int responseID;
	private int clientId;
	private int questionId;
	private int advisorId;
	private int questioncount;
	private int score;
	private String activeFlag = "Y";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getResponseID() {
		return responseID;
	}

	public void setResponseID(int responseID) {
		this.responseID = responseID;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}

	public int getQuestioncount() {
		return questioncount;
	}

	public void setQuestioncount(int questioncount) {
		this.questioncount = questioncount;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

}
