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

import com.finlabs.finexa.dto.ClientCashDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientCashService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientCashController {

	private static Logger log = LoggerFactory.getLogger(ClientCashController.class);

	@Autowired
	ClientCashService clientCashService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientCash", method = RequestMethod.POST)
	public ResponseEntity<?> createClientCash(@Valid @RequestBody ClientCashDTO clientCashDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientCashDTO = clientCashService.save(clientCashDTO, request);
				return new ResponseEntity<ClientCashDTO>(clientCashDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CASH_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE,
						FinexaConstant.CLIENT_CASH_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientCash", method = RequestMethod.POST)
	public ResponseEntity<?> editClientCash(@Valid @RequestBody ClientCashDTO clientCashDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {
		
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientCashDTO = clientCashService.update(clientCashDTO, request);
				return new ResponseEntity<ClientCashDTO>(clientCashDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CASH_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE,
						FinexaConstant.CLIENT_CASH_UPDATE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientCash", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@RequestParam int id) throws FinexaBussinessException {

		try {
			ClientCashDTO clientCashDTO = clientCashService.findById(id);
			
			return new ResponseEntity<ClientCashDTO>(clientCashDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_CASH_GET_EDIT_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE,
					FinexaConstant.CLIENT_CASH_GET_EDIT_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientCashDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {
		try {
			int i = clientCashService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CASH_DELETE_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE,
					FinexaConstant.CLIENT_CASH_DELETE_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", rexp);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientCash/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<ClientCashDTO> clientCashDTOList = clientCashService.findByClientId(clientId);
			return new ResponseEntity<List<ClientCashDTO>>(clientCashDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CASH_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE, FinexaConstant.CLIENT_CASH_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", rexp);
		}
	}

}
