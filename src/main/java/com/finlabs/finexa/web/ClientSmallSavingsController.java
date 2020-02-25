package com.finlabs.finexa.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
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

import com.finlabs.finexa.dto.ClientSmallSavingsDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientSmallSavingsService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientSmallSavingsController {

	private static Logger log = LoggerFactory.getLogger(ClientSmallSavingsController.class);
	/*
	 * @Resource(name = "exceptionmap") private Map<String, String>
	 * exceptionmap;
	 */
	@Autowired
	ClientSmallSavingsService clientSmallSavingsService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientSmallSaving", method = RequestMethod.POST)
	public ResponseEntity<?> createClientSmallSaving(@Valid @RequestBody ClientSmallSavingsDTO clientSSDTO,
			Errors errors, HttpServletRequest request) throws CustomFinexaException, FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientSSDTO = clientSmallSavingsService.save(clientSSDTO, request);
				return new ResponseEntity<ClientSmallSavingsDTO>(clientSSDTO, HttpStatus.OK);
			} catch (RuntimeException rexp) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_SSS_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_SSS_MODULE,
						FinexaConstant.CLIENT_SSS_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", rexp);
			}
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientSmallSaving", method = RequestMethod.POST)
	public ResponseEntity<?> editClientSmallSaving(@Valid @RequestBody ClientSmallSavingsDTO clientSmallSavingsDTO,
			Errors errors, HttpServletRequest request) throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientSmallSavingsDTO = clientSmallSavingsService.update(clientSmallSavingsDTO, request);
				return new ResponseEntity<ClientSmallSavingsDTO>(clientSmallSavingsDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_SSS_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_SSS_MODULE,
						FinexaConstant.CLIENT_SSS_UPDATE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientSmallSaving", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@RequestParam int id) throws FinexaBussinessException {
		try {
			ClientSmallSavingsDTO clientSmallSavingsDTO = clientSmallSavingsService.findById(id);
			return new ResponseEntity<ClientSmallSavingsDTO>(clientSmallSavingsDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_SSS_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_SSS_MODULE,
					FinexaConstant.CLIENT_SSS_GET_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientSmallSavingsList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientSmallSavingsDTO> clientSmallSavingDTOList = clientSmallSavingsService.findAll();

		return new ResponseEntity<List<ClientSmallSavingsDTO>>(clientSmallSavingDTOList, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientSmallSavingDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientSmallSavingsService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_SSS_DELETE_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_SSS_MODULE,
					FinexaConstant.CLIENT_SSS_DELETE_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientSmallSaving/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientSmallSavingsDTO> clientSmallSavingsDTOList = clientSmallSavingsService.findByClientId(clientId);
			return new ResponseEntity<List<ClientSmallSavingsDTO>>(clientSmallSavingsDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_SSS_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_SSS_MODULE, FinexaConstant.CLIENT_SSS_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
}
