package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.RiskProfileMasterDTO;
import com.finlabs.finexa.dto.RiskProfileQuestionnaireDTO;
import com.finlabs.finexa.model.RiskProfileQuestionnaire;


public interface AdvisorRiskProfileService {
	//////
	public void saveRiskprofileName(List<RiskProfileMasterDTO> riskProfileMasterList) throws RuntimeException;

	public void saveQuestion(List<RiskProfileQuestionnaireDTO> riskProfileMasterList) throws RuntimeException;

	public List<RiskProfileMasterDTO> getRiskProfileNameList(int advisorID) throws RuntimeException;

	public List<RiskProfileQuestionnaireDTO> getAllQuestionWithResponse(int advisorID) throws RuntimeException;

	public void DeleteAllRiskProfile(int advisorID);

	public List<RiskProfileQuestionnaire> autosaveQuestion(List<RiskProfileQuestionnaireDTO> RiskProfileQuestionnaireDTOList) throws RuntimeException;

}
