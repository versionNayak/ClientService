package com.finlabs.finexa.dto;



public class RiskProfileResponseBasedScoreDTO {
	private int id;
	private int responseID;
	private String responseText;
	private int score;
	private int advisorID;
	private int questionID;
	public int getQuestionID() {
		return questionID;
	}
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}
	//private RiskProfileQuestionnaire riskProfileQuestionnaire;
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
	public String getResponseText() {
		return responseText;
	}
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getAdvisorID() {
		return advisorID;
	}
	public void setAdvisorID(int advisorID) {
		this.advisorID = advisorID;
	}
	
	
}
