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

import com.finlabs.finexa.dto.ClientExpenseDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientExpenseService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientExpenseController {
	private static Logger log = LoggerFactory.getLogger(ClientExpenseController.class);

	@Autowired
	ClientExpenseService clientExpenseService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "expense/", method = RequestMethod.POST)
	public ResponseEntity<?> save(@Valid @RequestBody List<ClientExpenseDTO> clientExpenseList, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException, CustomFinexaException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientExpenseService.save(clientExpenseList, request);
				return new ResponseEntity<List<ClientExpenseDTO>>(clientExpenseList, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_EXPENSE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_EXPENSE_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_EXPENSE_MODULE,
						FinexaConstant.CLIENT_EXPENSE_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "editExpense/", method = RequestMethod.POST)
	public ResponseEntity<?> update(@Valid @RequestBody List<ClientExpenseDTO> clientExpenseList, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException, CustomFinexaException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientExpenseService.update(clientExpenseList, request);
				return new ResponseEntity<List<ClientExpenseDTO>>(clientExpenseList, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_EXPENSE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_EXPENSE_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_EXPENSE_MODULE,
						FinexaConstant.CLIENT_EXPENSE_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "expense/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllExpense(@PathVariable int clientId) throws FinexaBussinessException {
		try {
			ClientExpenseDTO clientExpenseDTO = clientExpenseService.getAllExpense(clientId);
			if (clientExpenseDTO == null) {
				clientExpenseDTO = new ClientExpenseDTO();
				clientExpenseDTO.setExpenseAmount((double) 0);
			}
			return new ResponseEntity<ClientExpenseDTO>(clientExpenseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_EXPENSE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_EXPENSE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_EXPENSE_MODULE,
					FinexaConstant.CLIENT_EXPENSE_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "AllExpense/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> viewAllExpense(@PathVariable int clientId) throws FinexaBussinessException {
		try {
			List<ClientExpenseDTO> clientExpenseDTOList = clientExpenseService.viewAllExpense(clientId);
			return new ResponseEntity<List<ClientExpenseDTO>>(clientExpenseDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_EXPENSE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_EXPENSE_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_EXPENSE_MODULE,
					FinexaConstant.CLIENT_EXPENSE_GET_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "delete/{expenseID}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteClientExpense(@PathVariable int expenseID) throws FinexaBussinessException {
		try {
			int i = clientExpenseService.deleteClientExpense(expenseID);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException exp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_EXPENSE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_EXPENSE_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_EXPENSE_MODULE,
					FinexaConstant.CLIENT_EXPENSE_DELETE_ERROR, exception != null ? exception.getErrorMessage() : "", exp);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "getExpenceUptoRetaiment/{clientID}", method = RequestMethod.GET)
	public boolean getClientExpenceUptoRetairment(@PathVariable int clientID) throws CustomFinexaException {
		return clientExpenseService.getExpenceByClientID(clientID);
	}

}