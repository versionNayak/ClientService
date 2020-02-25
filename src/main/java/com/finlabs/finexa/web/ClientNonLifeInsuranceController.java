package com.finlabs.finexa.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ClientFloaterCoverDTO;
import com.finlabs.finexa.dto.ClientNonlifeInsuranceDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.FinexaMessageDto;
import com.finlabs.finexa.dto.LookupNonLifeInsurancePolicyTypeDTO;
import com.finlabs.finexa.dto.LookupNonlifeInsuranceTypeDTO;
import com.finlabs.finexa.dto.MasterInsurancePolicyDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.dto.MasterInsuranceCompanyNameDTO;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientNonLifeInsuranceService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientNonLifeInsuranceController {

	private static Logger log = LoggerFactory.getLogger(ClientNonLifeInsuranceController.class);

	@Autowired
	ClientNonLifeInsuranceService clientNonLifeInsuranceService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/saveClientNonLifeInsurance", method = RequestMethod.POST)
	public ResponseEntity<?> create(@Valid @RequestBody ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO,
			Errors errors) throws FinexaBussinessException {

		log.debug("ClientNonLifeInsuranceController create(): " + clientNonlifeInsuranceDTO.getCheckedFamilyMemberID().size());
	
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientNonlifeInsuranceDTO retDTO = clientNonLifeInsuranceService.save(clientNonlifeInsuranceDTO);
				return new ResponseEntity<ClientNonlifeInsuranceDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_NON_LIFE_INSURANCE_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_NON_LIFE_INSURANCE_MODULE,
						FinexaConstant.CLIENT_NON_LIFE_INSURANCE_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	/*@RequestMapping(value = "/saveFloaterCover", method = RequestMethod.POST)
	public ResponseEntity<?> saveFloaterCover(@RequestBody ClientFloaterCoverDTO clientFloaterCoverDTO) {
		
		clientNonLifeInsuranceService.saveFloaterCover(clientFloaterCoverDTO);
		return new ResponseEntity<ClientFloaterCoverDTO>(clientFloaterCoverDTO, HttpStatus.OK);
		
	}*/
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editclientNonLifeInsurance", method = RequestMethod.POST)
	public ResponseEntity<?> editclientNonLifeInsurance(
			@Valid @RequestBody ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO, Errors errors)
			throws FinexaBussinessException {

		log.debug("ClientNonLifeInsuranceController editclientNonLifeInsurance(): " + clientNonlifeInsuranceDTO);
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientNonlifeInsuranceDTO = clientNonLifeInsuranceService.update(clientNonlifeInsuranceDTO);
				return new ResponseEntity<ClientNonlifeInsuranceDTO>(clientNonlifeInsuranceDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_NON_LIFE_INSURANCE_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_NON_LIFE_INSURANCE_MODULE,
						FinexaConstant.CLIENT_NON_LIFE_INSURANCE_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientNonLifeInsuranceList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientNonlifeInsuranceDTO> clientNonLifeInsuranceDTOList = clientNonLifeInsuranceService.findAll();

		return new ResponseEntity<List<ClientNonlifeInsuranceDTO>>(clientNonLifeInsuranceDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientNonLifeInsurance/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {
		try {
			ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO = clientNonLifeInsuranceService.findById(id);
			return new ResponseEntity<ClientNonlifeInsuranceDTO>(clientNonlifeInsuranceDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_NON_LIFE_INSURANCE_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_NON_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_NON_LIFE_INSURANCE_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/checkClientNonLifePolicyNumber", method = RequestMethod.GET)
	public ResponseEntity<?> checkAvailPolicyNumber(@RequestParam String policyNumber, @RequestParam int clientId)
			throws CustomFinexaException, FinexaBussinessException {
		try {
			log.debug("ClientNonLifeInsuranceController >> findByClientId()");

			FinexaMessageDto finexaMessageDto = clientNonLifeInsuranceService.checkAvailPolicyNumber(policyNumber,
					clientId);
			log.debug("ClientNonLifeInsuranceController << findByClientId()");
			return new ResponseEntity<FinexaMessageDto>(finexaMessageDto, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_NON_LIFE_INSURANCE_CHECK_POLICY_TYPE_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_NON_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_NON_LIFE_INSURANCE_CHECK_POLICY_TYPE_LIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientNonLifeInsuranceDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientNonLifeInsuranceService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_NON_LIFE_INSURANCE_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_NON_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_NON_LIFE_INSURANCE_DELETE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientNonLifeInsurance/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientNonlifeInsuranceDTO> clientNonLifeInsuranceDTOList = clientNonLifeInsuranceService
					.findByClientId(clientId);
			return new ResponseEntity<List<ClientNonlifeInsuranceDTO>>(clientNonLifeInsuranceDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_NON_LIFE_INSURANCE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_NON_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_NON_LIFE_INSURANCE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientNonLifeInsuranceTypeList", method = RequestMethod.GET)
	public ResponseEntity<?> getAllNonLifeInsuranceTypeList() {
		List<LookupNonlifeInsuranceTypeDTO> lookupNonlifeInsuranceTypeDTOList = clientNonLifeInsuranceService
				.findAllNonInsuranceTypeList();

		return new ResponseEntity<List<LookupNonlifeInsuranceTypeDTO>>(lookupNonlifeInsuranceTypeDTOList,
				HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/NonInsurancePolicyTypeList", method = RequestMethod.GET)
	public ResponseEntity<?> NonInsurancePolicyTypeList(@RequestParam byte insTypeId) throws FinexaBussinessException {
		try {
			Set<LookupNonLifeInsurancePolicyTypeDTO> lookupNonInsurancePolicyTypeDTOList = clientNonLifeInsuranceService
					.findAllNonInsurancePolicyTypeList(insTypeId);
			return new ResponseEntity<Set<LookupNonLifeInsurancePolicyTypeDTO>>(lookupNonInsurancePolicyTypeDTOList,
					HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_NON_LIFE_INSURANCE_POLICY_TYPE_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_INSURANCE_POLICY_TYPE_MODULE,
					FinexaConstant.GET_NON_LIFE_INSURANCE_POLICY_TYPE_LIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientNonLifeInsuranceCompanyPolicyName", method = RequestMethod.GET)
	public ResponseEntity<?> clientLifeInsuranceCompanyPolicyTypeList(@RequestParam byte insTypeId,
			@RequestParam int companyId, @RequestParam byte insurancePolicyTypeId) {
		MasterInsurancePolicyDTO masterInsurancePolicyDTO = clientNonLifeInsuranceService
				.ClientNonLifeInsuranceCompanyPolicyName(insTypeId, companyId, insurancePolicyTypeId);

		return new ResponseEntity<MasterInsurancePolicyDTO>(masterInsurancePolicyDTO, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/InsuranceCompanyList", method = RequestMethod.GET)
	public ResponseEntity<?> InsuranceCompanyList() throws FinexaBussinessException {
		try {
			List<MasterInsuranceCompanyNameDTO> masterInsuranceCompanyNameList = clientNonLifeInsuranceService
					.getClientInsuranceCompanyList();
			return new ResponseEntity<List<MasterInsuranceCompanyNameDTO>>(masterInsuranceCompanyNameList,
					HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_NON_LIFE_INSURANCE_COMPANY_NAME_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_INSURANCE_COMPANY_NAME_MODULE,
					FinexaConstant.GET_NON_LIFE_INSURANCE_COMPANY_NAME_LIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getPolicyEndDate", method = RequestMethod.GET)
	public ResponseEntity<?> getPolicyEndDate(@RequestParam int timePeriod, @RequestParam int clientId,@RequestParam Byte insuranceType)
			throws CustomFinexaException, FinexaBussinessException {
		List<ClientNonlifeInsuranceDTO> clientLifeInsuranceDTOList = new ArrayList<ClientNonlifeInsuranceDTO>();
		try {
			log.debug("ClientLifeInsuranceController getPolicyEndDate");
			clientLifeInsuranceDTOList = clientNonLifeInsuranceService.getPolicyEndDate(clientId, timePeriod,insuranceType);
			log.debug("ClientLifeInsuranceController getPolicyEndDate "+clientLifeInsuranceDTOList.size());
			return new ResponseEntity<List<ClientNonlifeInsuranceDTO>>(clientLifeInsuranceDTOList, HttpStatus.OK);
		}catch (RuntimeException finexaBussExp) {
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_CODE, FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_DESC,
					finexaBussExp);
		}
	
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getPolicyEndDateForadvisor", method = RequestMethod.GET)
	public ResponseEntity<?> getPolicyEndDateForadvisor(@RequestParam int timePeriod, @RequestParam int advisorUserID,@RequestParam Byte insuranceType)
			throws CustomFinexaException, FinexaBussinessException {
		List<ClientNonlifeInsuranceDTO> clientLifeInsuranceDTOList = new ArrayList<ClientNonlifeInsuranceDTO>();
		try {
			log.debug("ClientLifeInsuranceController >> findByClientId()");
		    clientLifeInsuranceDTOList = clientNonLifeInsuranceService.getPolicyEndDateForadvisor(advisorUserID, timePeriod,insuranceType);
			log.debug("ClientLifeInsuranceController << findByClientId() "+clientLifeInsuranceDTOList.size());
			return new ResponseEntity<List<ClientNonlifeInsuranceDTO>>(clientLifeInsuranceDTOList, HttpStatus.OK);
		} catch (RuntimeException finexaBussExp) {
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_CODE, FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_DESC,
					finexaBussExp);
		}
	
	}
}