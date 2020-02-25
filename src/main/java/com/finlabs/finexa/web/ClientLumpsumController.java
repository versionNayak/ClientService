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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ClientLumpsumDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.AdvisorService;
import com.finlabs.finexa.service.ClientLumpsumService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientLumpsumController {
	private static Logger log = LoggerFactory.getLogger(ClientLumpsumController.class);

	@Autowired
	ClientLumpsumService clientLumpsumService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientLumpsum", method = RequestMethod.POST)
	public ResponseEntity<?> create(@Valid @RequestBody ClientLumpsumDTO clientLumpsumDTO, Errors errors,  HttpServletRequest request)
			throws FinexaBussinessException {
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
				try {
					ClientLumpsumDTO retDTO = clientLumpsumService.save(clientLumpsumDTO, request);
					return new ResponseEntity<ClientLumpsumDTO>(retDTO, HttpStatus.OK);
				} catch (RuntimeException rexp) {
					FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
							.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
					FinexaExceptionHandling exception = finexaExceptionHandlingRepository
							.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
									FinexaConstant.CLIENT_LUMPSUM_INFLOW_ADD_ERROR);
					throw new FinexaBussinessException(FinexaConstant.CLIENT_LUMPSUM_INFLOW_MODULE,
							FinexaConstant.CLIENT_LUMPSUM_INFLOW_ADD_ERROR, 
							exception != null ? exception.getErrorMessage() : "", rexp);
				}
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientLumpsum", method = RequestMethod.POST)
	public ResponseEntity<?> edit(@Valid @RequestBody ClientLumpsumDTO clientLumpsumDTO, Errors errors,  HttpServletRequest request)
			throws FinexaBussinessException {
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
				try {
					ClientLumpsumDTO retDTO = clientLumpsumService.update(clientLumpsumDTO, request);
					return new ResponseEntity<ClientLumpsumDTO>(retDTO, HttpStatus.OK);
				} catch (RuntimeException rexp) {
					FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
							.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
					FinexaExceptionHandling exception = finexaExceptionHandlingRepository
							.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
									FinexaConstant.CLIENT_LUMPSUM_INFLOW_UPDATE_ERROR);
					throw new FinexaBussinessException(FinexaConstant.CLIENT_LUMPSUM_INFLOW_MODULE,
							FinexaConstant.CLIENT_LUMPSUM_INFLOW_UPDATE_ERROR, 
							exception != null ? exception.getErrorMessage() : "", rexp);
				}
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientLumpsum", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@RequestParam int id) throws FinexaBussinessException {
		try {
			ClientLumpsumDTO clientLumpsumDTO = clientLumpsumService.findById(id);
			log.debug("clientLumpsumDTO: " + clientLumpsumDTO);
			return new ResponseEntity<ClientLumpsumDTO>(clientLumpsumDTO, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_LUMPSUM_INFLOW_GET_EDIT_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LUMPSUM_INFLOW_MODULE,
					FinexaConstant.CLIENT_LUMPSUM_INFLOW_GET_EDIT_DATA_ERROR, 
					exception != null ? exception.getErrorMessage() : "", rexp);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientLumpsumList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientLumpsumDTO> clientLumpsumDTOList = clientLumpsumService.findAll();

		return new ResponseEntity<List<ClientLumpsumDTO>>(clientLumpsumDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientLumpsumDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {
		try {
			int i = clientLumpsumService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_LUMPSUM_INFLOW_DELETE_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LUMPSUM_INFLOW_MODULE,
					FinexaConstant.CLIENT_LUMPSUM_INFLOW_DELETE_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", rexp);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientLumpsum/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {
		try {
			List<ClientLumpsumDTO> clientLumpsumDTOList = clientLumpsumService.findByClientId(clientId);
			return new ResponseEntity<List<ClientLumpsumDTO>>(clientLumpsumDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_LUMPSUM_INFLOW_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LUMPSUM_INFLOW_MODULE,
					FinexaConstant.CLIENT_LUMPSUM_INFLOW_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", rexp);
		}
	}

}
