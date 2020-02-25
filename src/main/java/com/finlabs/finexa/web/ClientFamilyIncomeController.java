package com.finlabs.finexa.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.finlabs.finexa.dto.ClientFamilyIncomeDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientFamilyIncomeService;
import com.finlabs.finexa.util.FinexaConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ClientFamilyIncomeController {

	private static Logger log = LoggerFactory.getLogger(ClientFamilyIncomeController.class);

	@Autowired
	ClientFamilyIncomeService clientFamilyIncomeService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "familyIncome/", method = RequestMethod.POST)
	public ResponseEntity<?> save(@Valid @RequestBody List<ClientFamilyIncomeDTO> clientFamilyIncomeList, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException, CustomFinexaException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientFamilyIncomeService.save(clientFamilyIncomeList, request);
				return new ResponseEntity<List<ClientFamilyIncomeDTO>>(clientFamilyIncomeList, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INCOME);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_FAMILY_INCOME_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_INCOME_MODULE,
						FinexaConstant.CLIENT_FAMILY_INCOME_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "editFamilyIncome/", method = RequestMethod.POST)
	public ResponseEntity<?> update(@Valid @RequestBody List<ClientFamilyIncomeDTO> clientFamilyIncomeList,
			Errors errors, HttpServletRequest request) throws FinexaBussinessException, CustomFinexaException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientFamilyIncomeService.update(clientFamilyIncomeList, request);
				return new ResponseEntity<List<ClientFamilyIncomeDTO>>(clientFamilyIncomeList, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INCOME);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_FAMILY_INCOME_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_INCOME_MODULE,
						FinexaConstant.CLIENT_FAMILY_INCOME_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "familyIncome/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<List<ClientFamilyIncomeDTO>> getAllfamilyIncome(@PathVariable int clientId)
			throws FinexaBussinessException {
		try {
			List<ClientFamilyIncomeDTO> clientFamilyIncomeDTOList = clientFamilyIncomeService
					.getAllFamilyIncome(clientId);

			return new ResponseEntity<List<ClientFamilyIncomeDTO>>(clientFamilyIncomeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INCOME);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_FAMILY_INCOME_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_INCOME_MODULE,
					FinexaConstant.CLIENT_FAMILY_INCOME_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "familyIncome/{clientId}/{familyMemberId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllIncomeForFamilyMember(@PathVariable int clientId, @PathVariable int familyMemberId)
			throws FinexaBussinessException {

		try {
			List<ClientFamilyIncomeDTO> clientFamilyIncomeDTOList = clientFamilyIncomeService
					.getAllIncomeForFamilyMember(clientId, familyMemberId);
			return new ResponseEntity<List<ClientFamilyIncomeDTO>>(clientFamilyIncomeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INCOME);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_FAMILY_INCOME_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_INCOME_MODULE,
					FinexaConstant.CLIENT_FAMILY_INCOME_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "delete/{clientId}/{memberId}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteIncomeForMember(@PathVariable int clientId, @PathVariable int memberId, HttpServletRequest request)
			throws FinexaBussinessException, CustomFinexaException {
		try {
			clientFamilyIncomeService.deleteIncomeForMember(clientId, memberId, request);
			ClientFamilyIncomeDTO ct = new ClientFamilyIncomeDTO();
			return new ResponseEntity<ClientFamilyIncomeDTO>(ct, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INCOME);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_FAMILY_INCOME_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_INCOME_MODULE,
					FinexaConstant.CLIENT_FAMILY_INCOME_DELETE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "checkIfIncomePresentForAll/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<List<ClientFamilyMemberDTO>> checkIfIncomePresentForAll(@PathVariable int clientId)
			throws FinexaBussinessException {
		try {
			List<ClientFamilyMemberDTO> ClientFamilyMemberDTOList = clientFamilyIncomeService
					.checkIfIncomePresentForAll(clientId);

			return new ResponseEntity<List<ClientFamilyMemberDTO>>(ClientFamilyMemberDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INCOME);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CHECK_IF_INCOME_EXISTS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_INCOME_MODULE,
					FinexaConstant.CHECK_IF_INCOME_EXISTS_ERROR, exception != null ? exception.getErrorMessage() : "",
					e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "getIncomeUptoRetaiment/{clientID}", method = RequestMethod.GET)
	public boolean getClientIncomeUptoRetairment(@PathVariable int clientID) throws CustomFinexaException {
		return clientFamilyIncomeService.getIncomeByClientID(clientID);
	}


}
