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

import com.finlabs.finexa.dto.ClientStructuredProductDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientStructuredProductService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientStructuredProductController {

	private static Logger log = LoggerFactory.getLogger(ClientStructuredProductController.class);

	@Autowired
	ClientStructuredProductService clientStructuredProductService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientStructuredProduct", method = RequestMethod.POST)
	public ResponseEntity<?> createClientStructuredProduct(
			@Valid @RequestBody ClientStructuredProductDTO clientStructuredProductDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientStructuredProductDTO = clientStructuredProductService.save(clientStructuredProductDTO, request);
				return new ResponseEntity<ClientStructuredProductDTO>(clientStructuredProductDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_AI_SP_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_SP_MODULE,
						FinexaConstant.CLIENT_AI_SP_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientStructuredProduct", method = RequestMethod.POST)
	public ResponseEntity<?> editClientStructuredProduct(
			@Valid @RequestBody ClientStructuredProductDTO clientStructuredProductDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientStructuredProductDTO = clientStructuredProductService.update(clientStructuredProductDTO, request);
				return new ResponseEntity<ClientStructuredProductDTO>(clientStructuredProductDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_AI_SP_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_SP_MODULE,
						FinexaConstant.CLIENT_AI_SP_UPDATE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientStructuredProduct/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {

		try {
			ClientStructuredProductDTO clientStructuredProductDTO = clientStructuredProductService.findById(id);
			return new ResponseEntity<ClientStructuredProductDTO>(clientStructuredProductDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_AI_SP_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_SP_MODULE,
					FinexaConstant.CLIENT_AI_SP_GET_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientStructuredProductList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientStructuredProductDTO> clientStructuredProductDTOList = clientStructuredProductService.findAll();

		return new ResponseEntity<List<ClientStructuredProductDTO>>(clientStructuredProductDTOList, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientStructuredProductDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {
		try {
			int i = clientStructuredProductService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_AI_SP_DELETE_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_SP_MODULE,
					FinexaConstant.CLIENT_AI_SP_DELETE_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientStructuredProduct/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {
		try {
			List<ClientStructuredProductDTO> clientStructuredProductDTOList = clientStructuredProductService
					.findByClientId(clientId);
			return new ResponseEntity<List<ClientStructuredProductDTO>>(clientStructuredProductDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_AI_SP_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_SP_MODULE,
					FinexaConstant.CLIENT_AI_SP_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}

}
