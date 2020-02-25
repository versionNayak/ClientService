package com.finlabs.finexa.web;

import java.util.List;
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

import com.finlabs.finexa.dto.ClientLoanDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientLoanService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientLoanController {

	private static Logger log = LoggerFactory.getLogger(ClientLoanController.class);

	@Autowired
	ClientLoanService clientLoanService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientLoan", method = RequestMethod.POST)
	public ResponseEntity<?> createClientLoan(@Valid @RequestBody ClientLoanDTO clientLoanDTO, Errors errors)
			throws FinexaBussinessException {

		log.debug("ClientLoanController createClientLoan(): " + clientLoanDTO);
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientLoanDTO = clientLoanService.save(clientLoanDTO);
				return new ResponseEntity<ClientLoanDTO>(clientLoanDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_LOANS);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_LOANS_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_LOANS_MODULE,
						FinexaConstant.CLIENT_LOANS_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientLoan", method = RequestMethod.POST)
	public ResponseEntity<?> editClientLoan(@Valid @RequestBody ClientLoanDTO clientLoanDTO, Errors errors)
			throws FinexaBussinessException {

		log.debug("ClientLoanController editClientLoan(): " + clientLoanDTO);
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientLoanDTO = clientLoanService.update(clientLoanDTO);
				return new ResponseEntity<ClientLoanDTO>(clientLoanDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_LOANS);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_LOANS_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_LOANS_MODULE,
						FinexaConstant.CLIENT_LOANS_UPDATE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientLoan", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@RequestParam int loanId) throws FinexaBussinessException {
		try {
			ClientLoanDTO clientLoanDTO = clientLoanService.findById(loanId);
			return new ResponseEntity<ClientLoanDTO>(clientLoanDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_LOANS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_LOANS_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LOANS_MODULE,
					FinexaConstant.CLIENT_LOANS_GET_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientLoansList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientLoanDTO> clientLoanDTOList = clientLoanService.findAll();

		return new ResponseEntity<List<ClientLoanDTO>>(clientLoanDTOList, HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientLoanDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientLoanService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_LOANS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_LOANS_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LOANS_MODULE,
					FinexaConstant.CLIENT_LOANS_DELETE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientLoan/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientLoanDTO> clientLoanDTOList = clientLoanService.findByClientId(clientId);
			return new ResponseEntity<List<ClientLoanDTO>>(clientLoanDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_LOANS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_LOANS_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LOANS_MODULE,
					FinexaConstant.CLIENT_LOANS_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/loanEndDateEMI", method = RequestMethod.POST)
	public ResponseEntity<?> findLoanEndDateEMI(@RequestBody ClientLoanDTO clientLoanDTO)
			throws FinexaBussinessException {
		try {
			ClientLoanDTO dto = clientLoanService.findLoanEndDateEMI(clientLoanDTO);
			return new ResponseEntity<ClientLoanDTO>(dto, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_LOANS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_LOANS_GET_LOAN_END_DATE_EMI_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LOANS_MODULE,
					FinexaConstant.CLIENT_LOANS_GET_LOAN_END_DATE_EMI_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/loanEndDateNONEMI", method = RequestMethod.POST)
	public ResponseEntity<?> findLoanEndDateNONEMI(@RequestBody ClientLoanDTO clientLoanDTO)
			throws FinexaBussinessException {
		try {
			ClientLoanDTO dto = clientLoanService.findLoanEndDateNONEMI(clientLoanDTO);
			return new ResponseEntity<ClientLoanDTO>(dto, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_LOANS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_LOANS_GET_LOAN_END_DATE_NON_EMI_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LOANS_MODULE,
					FinexaConstant.CLIENT_LOANS_GET_LOAN_END_DATE_NON_EMI_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

}
