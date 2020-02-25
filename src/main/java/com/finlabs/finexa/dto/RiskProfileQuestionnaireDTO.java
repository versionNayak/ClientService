package com.finlabs.finexa.dto;


import java.util.List;

public class RiskProfileQuestionnaireDTO {
	private int id;
	private String question;
	int clientId;
	private int advisorId;
	private List<RiskProfileResponseBasedScoreDTO> riskProfileResponseBasedScoresDTO;
	private ClientRiskProfileResponseDTO clientRiskProfileResponsesDTO;
	private int questionCount;
	private int totalNumberQuestion;
	
	
	
	
	public ClientRiskProfileResponseDTO getClientRiskProfileResponsesDTO() {
		return clientRiskProfileResponsesDTO;
	}
	public void setClientRiskProfileResponsesDTO(ClientRiskProfileResponseDTO clientRiskProfileResponsesDTO) {
		this.clientRiskProfileResponsesDTO = clientRiskProfileResponsesDTO;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getAdvisorId() {
		return advisorId;
	}
	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}
	public List<RiskProfileResponseBasedScoreDTO> getRiskProfileResponseBasedScoresDTO() {
		return riskProfileResponseBasedScoresDTO;
	}
	public void setRiskProfileResponseBasedScoresDTO(
			List<RiskProfileResponseBasedScoreDTO> riskProfileResponseBasedScoresDTO) {
		this.riskProfileResponseBasedScoresDTO = riskProfileResponseBasedScoresDTO;
	}
	public int getQuestionCount() {
		return questionCount;
	}
	public int getTotalNumberQuestion() {
		return totalNumberQuestion;
	}
	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}
	public void setTotalNumberQuestion(int totalNumberQuestion) {
		this.totalNumberQuestion = totalNumberQuestion;
	}
	
	

	
}
