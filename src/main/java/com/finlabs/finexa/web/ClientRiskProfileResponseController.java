package com.finlabs.finexa.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.finlabs.finexa.dto.ClientMasterDTO;
import com.finlabs.finexa.dto.ClientRiskProfileResponseDTO;
import com.finlabs.finexa.dto.RiskProfileQuestionnaireDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.AdvisorService;
import com.finlabs.finexa.service.ClientRiskProfileService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientRiskProfileResponseController {
	private static Logger log = LoggerFactory.getLogger(ClientRealEstateController.class);
   ////
	@Autowired
	private ClientRiskProfileService clientRiskProfileService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	AdvisorService advisorService;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getQuestionList/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> getQuestionList(@PathVariable int advisorId) throws FinexaBussinessException {

		try {
			List<RiskProfileQuestionnaireDTO> QuestionDTOList = clientRiskProfileService.getQuestionList(advisorId);
			return new ResponseEntity<List<RiskProfileQuestionnaireDTO>>(QuestionDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_MASTERS_RISK_PROFILE_MASTER);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_GET_QUESTIONS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_MODULE,
					FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_GET_QUESTIONS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody List<ClientRiskProfileResponseDTO> ClientRiskProfileResponseDTOList,  HttpServletRequest request)
			throws FinexaBussinessException, CustomFinexaException {
		try {
	
			Integer clientId = null;
			Integer advisorId = null;

			for (ClientRiskProfileResponseDTO clientRiskProfileResponseDTO : ClientRiskProfileResponseDTOList) {
				clientId = clientRiskProfileResponseDTO.getClientId();
				advisorId=clientRiskProfileResponseDTO.getAdvisorId();
				break;
			}

			clientRiskProfileService.disableRiskResponseData(clientId);

			for (ClientRiskProfileResponseDTO clientRiskProfileResponseDTO : ClientRiskProfileResponseDTOList) {
				clientRiskProfileService.save(clientRiskProfileResponseDTO);
			}
			clientRiskProfileService.saveScore(advisorId,clientId, request);
			
			return new ResponseEntity<List<ClientRiskProfileResponseDTO>>(ClientRiskProfileResponseDTOList,
					HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_RISK_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_CLIENT_RISK_PROFILE_UPDATE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_CLIENT_RISK_PROFILE_RESPONSE_MODULE,
					FinexaConstant.MY_CLIENT_RISK_PROFILE_UPDATE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	// unused
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<?> update(@RequestBody ClientRiskProfileResponseDTO ClientRiskProfileResponseDTO) {
		clientRiskProfileService.save(ClientRiskProfileResponseDTO);

		return new ResponseEntity<ClientRiskProfileResponseDTO>(ClientRiskProfileResponseDTO, HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int clientId) {

		int i = clientRiskProfileService.delete(clientId);

		return new ResponseEntity<Integer>(i, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('Admin','MastersAddEdit')")
	@RequestMapping(value = "/deleteByadvisorID/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteByAdvisorId(@PathVariable int advisorID) throws FinexaBussinessException {

		try {
			clientRiskProfileService.deleteByAdvisorId(advisorID);
			return new ResponseEntity<Integer>(1, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_RISK_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_CLIENT_RISK_PROFILE_RESPONSE_MODULE,
					FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_DELETE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getQuestionAnswerForClient/{clientID}/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> getQuestionAnswerForClient(@PathVariable int clientID, @PathVariable int advisorID)
			throws FinexaBussinessException {

		try {
			List<RiskProfileQuestionnaireDTO> QuestionDTOList = clientRiskProfileService
					.getQuestionAnswerForClient(clientID, advisorID);

			return new ResponseEntity<List<RiskProfileQuestionnaireDTO>>(QuestionDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_RISK_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_RISK_PROFILE_QUESTIONS_ANSWERS_FOR_CLIENT_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_CLIENT_RISK_PROFILE_RESPONSE_MODULE,
					FinexaConstant.GET_RISK_PROFILE_QUESTIONS_ANSWERS_FOR_CLIENT_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getRiskProfile/{clientID}/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> getRiskProfile(@PathVariable int clientID, @PathVariable int advisorID)
			throws FinexaBussinessException {

		try {
			ClientMasterDTO clientMasterDTO = clientRiskProfileService.getRiskProfile(clientID, advisorID);

			return new ResponseEntity<ClientMasterDTO>(clientMasterDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_RISK_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.VIEW_RISK_PROFILE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_CLIENT_RISK_PROFILE_RESPONSE_MODULE,
					FinexaConstant.VIEW_RISK_PROFILE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getRiskProfileScore/{clientID}", method = RequestMethod.GET)
	public ResponseEntity<?> getRiskProfileScoreByClientId(@PathVariable int clientID) {

		ClientMasterDTO clientMasterDTO = clientRiskProfileService.getRiskProfileScorByClientId(clientID);
		return new ResponseEntity<ClientMasterDTO>(clientMasterDTO, HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('Admin','MastersAddEdit')")
	@RequestMapping(value = "/getScoreforAllClient/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> getScoreforAllClient(@PathVariable int advisorID) throws FinexaBussinessException {

		try {
			clientRiskProfileService.getScoreforAllClient(advisorID);
			return new ResponseEntity<Integer>(1, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_RISK_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_GET_SCORE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_CLIENT_RISK_PROFILE_RESPONSE_MODULE,
					FinexaConstant.MY_BUSINESS_RISK_PROFILE_MASTER_GET_SCORE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

}
