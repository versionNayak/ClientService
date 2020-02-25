package com.finlabs.finexa.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ClientNpsDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.LookupNPSPlanTypeDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientNPSAssetClassReturnDTO;

import com.finlabs.finexa.service.ClientNPSService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientNpsController {
	private static Logger log = LoggerFactory.getLogger(ClientNpsController.class);

	@Autowired
	ClientNPSService clientNPSService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientNPS", method = RequestMethod.POST)
	public ResponseEntity<?> createClientNPS(@Valid @RequestBody ClientNpsDTO clientNpDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientNpsDTO retDTO = clientNPSService.save(clientNpDTO, request);
				return new ResponseEntity<ClientNpsDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_NPS_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_NPS_MODULE,
						FinexaConstant.CLIENT_ROS_NPS_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientNPS", method = RequestMethod.POST)
	public ResponseEntity<?> editClientNPS(@Valid @RequestBody ClientNpsDTO clientNpDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientNpsDTO retDTO = clientNPSService.update(clientNpDTO, request);
				return new ResponseEntity<ClientNpsDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_ROS_NPS_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_NPS_MODULE,
						FinexaConstant.CLIENT_ROS_NPS_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "clientNPS/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {

		try {
			ClientNpsDTO clientNpDTO = clientNPSService.findById(id);
			return new ResponseEntity<ClientNpsDTO>(clientNpDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_NPS_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_NPS_MODULE,
					FinexaConstant.CLIENT_ROS_NPS_GET_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientNPSList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientNpsDTO> clientNpDTOList = clientNPSService.findAll();

		return new ResponseEntity<List<ClientNpsDTO>>(clientNpDTOList, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientNPSDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {
		try {
			int i = clientNPSService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_ROS_NPS_DELETE_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_NPS_MODULE,
					FinexaConstant.CLIENT_ROS_NPS_DELETE_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}


	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientNPS/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientNpsDTO> clientNpDTOList = clientNPSService.findByClientId(clientId);
			return new ResponseEntity<List<ClientNpsDTO>>(clientNpDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_NPS_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_NPS_MODULE,
					FinexaConstant.CLIENT_ROS_NPS_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientNPS/getAllPlanType", method = RequestMethod.GET)
	public ResponseEntity<?> getAllPlanType() throws FinexaBussinessException {

		try {
			List<LookupNPSPlanTypeDTO> lookupNPSPlanTypeDTOList = clientNPSService.findAllNPSType();
			return new ResponseEntity<List<LookupNPSPlanTypeDTO>>(lookupNPSPlanTypeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.LOOKUP_NPS_PLAN_TYPE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_NPS_PLAN_TYPE_MODULE,
					FinexaConstant.LOOKUP_NPS_PLAN_TYPE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientNPS/getSalaryCAGR", method = RequestMethod.GET)
	public ResponseEntity<?> findCagr() throws FinexaBussinessException {
		try {
			int incomeType = 1; // Salary
			double cagr = clientNPSService.getCAGRByIncomeType(incomeType);
			return new ResponseEntity<Double>(cagr, HttpStatus.OK);
		} catch (Exception e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.NPS_MASTER_INCOME_GROWTH_RATE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_INCOME_GROWTH_RATE_MODULE,
					FinexaConstant.NPS_MASTER_INCOME_GROWTH_RATE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientNPS/getAssetClassReturn", method = RequestMethod.GET)
	public ResponseEntity<?> getAssetClassReturn() throws FinexaBussinessException {
		try {
			ClientNPSAssetClassReturnDTO retDTO = clientNPSService.getAssetClassReturn();
			return new ResponseEntity<ClientNPSAssetClassReturnDTO>(retDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MASTER_NPS_ASSET_CLASS_EXPECTED_RETURN_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_NPS_ASSET_CLASS_EXPECTED_RETURN_MODULE,
					FinexaConstant.MASTER_NPS_ASSET_CLASS_EXPECTED_RETURN_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/checkIfNpsPresentForAll/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<List<ClientFamilyMemberDTO>> checkIfNpsPresentForAll(@PathVariable int clientId)
			throws FinexaBussinessException {
		try {
			log.debug("client id" + clientId);
			List<ClientFamilyMemberDTO> clientNPSDTOList = clientNPSService.checkIfNpsPresentForAll(clientId);
			return new ResponseEntity<List<ClientFamilyMemberDTO>>(clientNPSDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_ROS_CHECK_NPS_EXIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_NPS_MODULE,
					FinexaConstant.CLIENT_ROS_CHECK_NPS_EXIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
}
