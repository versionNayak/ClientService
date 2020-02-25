package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientMasterDTO;
import com.finlabs.finexa.dto.ClientRiskProfileResponseDTO;
import com.finlabs.finexa.dto.RiskProfileQuestionnaireDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.ClientRiskProfileResponse;

public interface ClientRiskProfileService {

	public List<RiskProfileQuestionnaireDTO> getQuestionList(int advisorid) throws RuntimeException;

	public ClientRiskProfileResponseDTO save(ClientRiskProfileResponseDTO clientRiskProfileResponseDTO)
			throws RuntimeException;

	public int delete(int id);

	// public List<ClientRiskProfileResponseDTO> getQuestionAnswerForclient(int
	// clientId);

	// public ClientRiskProfileResponseDTO getQuestionAnswerById(int id);

	public ClientMasterDTO getRiskProfile(int clientID, int advisorID) throws RuntimeException;

	public void disableRiskResponseData(Integer clientId) throws RuntimeException, CustomFinexaException;

	public List<RiskProfileQuestionnaireDTO> getQuestionAnswerForClient(int clientId, int advisorId)
			throws RuntimeException;

	public ClientMasterDTO getRiskProfileScorByClientId(int clientID);

	public void deleteByAdvisorId(int clientId) throws RuntimeException;

	public void getScoreforAllClient(int advisorID) throws RuntimeException;

	public void saveScore(int advisorId, int clientId,  HttpServletRequest request);

	public List<Integer> getQuestions(int advisorid) throws RuntimeException;

	public ClientRiskProfileResponse autoSave(ClientRiskProfileResponseDTO clientRiskProfileResponseDTO)
			throws RuntimeException;

	public void autoSaveScore(int masterId, int clientId);
}
