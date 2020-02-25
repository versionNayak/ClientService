package com.finlabs.finexa.web;

import java.util.Date;
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

import com.finlabs.finexa.dto.ClientEpfDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.AdvisorService;
import com.finlabs.finexa.service.ClientAnnuityService;
import com.finlabs.finexa.service.ClientEPFService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientEpfController {

	private static Logger log = LoggerFactory.getLogger(ClientEpfController.class);

	@Autowired
	ClientEPFService clientEPFService;
	@Autowired
	ClientAnnuityService clientAnnuityService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientEPF", method = RequestMethod.POST)
	public ResponseEntity<?> createClientEPF(@Valid @RequestBody ClientEpfDTO clientEpfDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientEpfDTO retDTO = clientEPFService.save(clientEpfDTO, request);
				return new ResponseEntity<ClientEpfDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_EPF_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_EPF_MODULE,
						FinexaConstant.CLIENT_ROS_EPF_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientEPF", method = RequestMethod.POST)
	public ResponseEntity<?> editClientEPF(@Valid @RequestBody ClientEpfDTO clientEpfDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientEpfDTO retDTO = clientEPFService.update(clientEpfDTO, request);
				return new ResponseEntity<ClientEpfDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_ROS_EPF_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_EPF_MODULE,
						FinexaConstant.CLIENT_ROS_EPF_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientEPF/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {

		try {
			ClientEpfDTO clientEpfDTO = clientEPFService.findById(id);
			return new ResponseEntity<ClientEpfDTO>(clientEpfDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_EPF_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_EPF_MODULE,
					FinexaConstant.CLIENT_ROS_EPF_GET_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientEPFList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientEpfDTO> clientEpfDTOList = clientEPFService.findAll();

		return new ResponseEntity<List<ClientEpfDTO>>(clientEpfDTOList, HttpStatus.OK);
	}

	/*
	 * @RequestMapping(value="/clientEPFDelete/{id}",
	 * method=RequestMethod.DELETE) public ResponseEntity<?>
	 * delete(@PathVariable int id){
	 * 
	 * int i = clientEPFService.delete(id); return new ResponseEntity<Integer>(i
	 * , HttpStatus.OK); }
	 */
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientEPFDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientEPFService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_ROS_EPF_DELETE_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_EPF_MODULE,
					FinexaConstant.CLIENT_ROS_EPF_DELETE_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientEPF/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientEpfDTO> clientEpfDTOList = clientEPFService.findByClientId(clientId);
			return new ResponseEntity<List<ClientEpfDTO>>(clientEpfDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_EPF_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_EPF_MODULE,
					FinexaConstant.CLIENT_ROS_EPF_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientEPF/getEPFInterestRate/{date}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable Date date) throws FinexaBussinessException {

		try {
			double interestRate = clientEPFService.getInterestRate(date);
			return new ResponseEntity<Double>(interestRate, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MASTER_EPF_INTEREST_RATE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_EPF_INTEREST_RATE_MODULE,
					FinexaConstant.MASTER_EPF_INTEREST_RATE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientEPF/getEPFCagr/{date}", method = RequestMethod.GET)
	public ResponseEntity<?> findCagr(@PathVariable Date date) throws FinexaBussinessException {
		try {
			double cagr = clientEPFService.getCagr(date);
			return new ResponseEntity<Double>(cagr, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.EPF_MASTER_INCOME_GROWTH_RATE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_INCOME_GROWTH_RATE_MODULE,
					FinexaConstant.EPF_MASTER_INCOME_GROWTH_RATE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/existAnnuity/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> existAnnuity(@PathVariable int id) {

		int i = clientEPFService.existAnnuity(id);

		log.debug("i " + i);
		return new ResponseEntity<Integer>(i, HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/checkIfEpfPresent/{clientId}", method = RequestMethod.GET)
	public int checkIfEpfPresent(@PathVariable int clientId)
	 {
		int i = clientEPFService.checkIfEpfPresent(clientId);
		return i;
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/checkIfEpfPresentForAll/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<List<ClientFamilyMemberDTO>> checkIfEpfPresentForAll(@PathVariable int clientId)
			throws FinexaBussinessException {
		try {
			log.debug("client id" + clientId);
			List<ClientFamilyMemberDTO> dtoList = clientEPFService.checkIfEpfPresentForAll(clientId);
			return new ResponseEntity<List<ClientFamilyMemberDTO>>(dtoList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_EPF_MODULE, 
					"Some Error Code", "Failed to check if EPF exists for all.", e);
		}
	}

	
}
