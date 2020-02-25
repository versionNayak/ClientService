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

import com.finlabs.finexa.dto.ClientRealEstateDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientRealEstateService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientRealEstateController {

	private static Logger log = LoggerFactory.getLogger(ClientRealEstateController.class);

	@Autowired
	ClientRealEstateService clientRealEstateService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientRealEstate", method = RequestMethod.POST)
	public ResponseEntity<?> createClientRealEstate(@Valid @RequestBody ClientRealEstateDTO clientRealEstateDTO,
			Errors errors, HttpServletRequest request) throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientRealEstateDTO = clientRealEstateService.save(clientRealEstateDTO, request);
				return new ResponseEntity<ClientRealEstateDTO>(clientRealEstateDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_AI_REAL_ESTATE_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_REAL_ESTATE_MODULE,
						FinexaConstant.CLIENT_AI_REAL_ESTATE_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientRealEstate", method = RequestMethod.POST)
	public ResponseEntity<?> editClientRealEstate(@Valid @RequestBody ClientRealEstateDTO clientRealEstateDTO,
			Errors errors, HttpServletRequest request) throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientRealEstateDTO = clientRealEstateService.update(clientRealEstateDTO, request);
				return new ResponseEntity<ClientRealEstateDTO>(clientRealEstateDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_AI_REAL_ESTATE_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_REAL_ESTATE_MODULE,
						FinexaConstant.CLIENT_AI_REAL_ESTATE_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientRealEsate/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {

		try {
			ClientRealEstateDTO clientRealEstateDTO = clientRealEstateService.findById(id);
			return new ResponseEntity<ClientRealEstateDTO>(clientRealEstateDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_AI_REAL_ESTATE_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_REAL_ESTATE_MODULE,
					FinexaConstant.CLIENT_AI_REAL_ESTATE_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientRealEsatesList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientRealEstateDTO> clientRealEstateDTOList = clientRealEstateService.findAll();

		return new ResponseEntity<List<ClientRealEstateDTO>>(clientRealEstateDTOList, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientRealEstateDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientRealEstateService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_AI_REAL_ESTATE_DELETE_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_REAL_ESTATE_MODULE,
					FinexaConstant.CLIENT_AI_REAL_ESTATE_DELETE_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientRealEstate/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientRealEstateDTO> clientRealEstateDTOList = clientRealEstateService.findByClientId(clientId);
			return new ResponseEntity<List<ClientRealEstateDTO>>(clientRealEstateDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_AI_REAL_ESTATE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_REAL_ESTATE_MODULE,
					FinexaConstant.CLIENT_AI_REAL_ESTATE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

}
