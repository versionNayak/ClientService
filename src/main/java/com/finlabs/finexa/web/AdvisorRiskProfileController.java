package com.finlabs.finexa.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.RiskProfileMasterDTO;
import com.finlabs.finexa.dto.RiskProfileQuestionnaireDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.AdvisorRiskProfileService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class AdvisorRiskProfileController {
  // // //
	private static Logger log = LoggerFactory.getLogger(AdvisorRiskProfileController.class);

	@Autowired
	AdvisorRiskProfileService advisorRiskProfileService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

	@PreAuthorize("hasAnyRole('Admin','MastersAddEdit')")
	@RequestMapping(value = "/createRiskprofileName", method = RequestMethod.POST)
	public ResponseEntity<?> createRiskProfileMaster(@RequestBody List<RiskProfileMasterDTO> riskProfileMasterList)
			throws FinexaBussinessException {
		try {
			advisorRiskProfileService.saveRiskprofileName(riskProfileMasterList);
			return new ResponseEntity<List<RiskProfileMasterDTO>>(riskProfileMasterList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_MASTERS_RISK_PROFILE_MASTER);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_ADD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_MODULE,
					FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_ADD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	@PreAuthorize("hasAnyRole('Admin','MastersAddEdit')")
	@RequestMapping(value = "/saveQuestionList", method = RequestMethod.POST)
	public ResponseEntity<?> saveQuestionList(
			@RequestBody List<RiskProfileQuestionnaireDTO> RiskProfileQuestionnaireDTOList)
			throws FinexaBussinessException {

		try {
			advisorRiskProfileService.saveQuestion(RiskProfileQuestionnaireDTOList);
			return new ResponseEntity<List<RiskProfileQuestionnaireDTO>>(RiskProfileQuestionnaireDTOList,
					HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_MASTERS_RISK_PROFILE_MASTER);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_ADD_QUESTIONS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_MODULE,
					FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_ADD_QUESTIONS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	@PreAuthorize("hasAnyRole('Admin','MastersView')")
	@RequestMapping(value = "/getRiskProfileNameList/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> getRiskProfileNameList(@PathVariable int advisorID) throws FinexaBussinessException {

		try {
			List<RiskProfileMasterDTO> RiskProfileMasterDTOList = advisorRiskProfileService
					.getRiskProfileNameList(advisorID);
			return new ResponseEntity<List<RiskProfileMasterDTO>>(RiskProfileMasterDTOList, HttpStatus.OK);
			} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_MASTERS_RISK_PROFILE_MASTER);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_MODULE,
					FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	@PreAuthorize("hasAnyRole('Admin','MastersView')")
	@RequestMapping(value = "/getAllQuestionWithResponse/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllQuestionWithResponse(@PathVariable int advisorID) throws FinexaBussinessException {
		try {
			List<RiskProfileQuestionnaireDTO> RiskProfileQuestionnaireDTOList = advisorRiskProfileService
					.getAllQuestionWithResponse(advisorID);
			return new ResponseEntity<List<RiskProfileQuestionnaireDTO>>(RiskProfileQuestionnaireDTOList,
					HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_MASTERS_RISK_PROFILE_MASTER);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_VIEW_QUESTIONS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_MODULE,
					FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_VIEW_QUESTIONS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','MastersDelete')")
	@RequestMapping(value = "/deleteAllRiskProfile/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> DeleteAllRiskProfile(@PathVariable int advisorID) {
		advisorRiskProfileService.DeleteAllRiskProfile(advisorID);
		

		return new ResponseEntity<Integer>(1, HttpStatus.OK);
	}
	
	
}