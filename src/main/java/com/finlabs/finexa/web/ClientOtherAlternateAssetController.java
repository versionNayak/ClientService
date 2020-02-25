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
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ClientOtherAlternateAssetDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientOtherAlternateAssetService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientOtherAlternateAssetController {

	private static Logger log = LoggerFactory.getLogger(ClientOtherAlternateAssetController.class);

	@Autowired
	ClientOtherAlternateAssetService clientOtherAlternateAssetService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientOtherAlternateAsset", method = RequestMethod.POST)
	public ResponseEntity<?> createClientOtherAlternateAsset(
			@Valid @RequestBody ClientOtherAlternateAssetDTO clientOtherAlternateAssetDTO, Errors errors)
			throws FinexaBussinessException {
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientOtherAlternateAssetDTO = clientOtherAlternateAssetService.save(clientOtherAlternateAssetDTO);
				return new ResponseEntity<ClientOtherAlternateAssetDTO>(clientOtherAlternateAssetDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_AI_OTHERS_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_OTHERS_MODULE,
						FinexaConstant.CLIENT_AI_OTHERS_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientOtherAlternateAsset", method = RequestMethod.POST)
	public ResponseEntity<?> editClientOtherAlternateAsset(
			@Valid @RequestBody ClientOtherAlternateAssetDTO clientOtherAlternateAssetDTO, Errors errors)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientOtherAlternateAssetDTO = clientOtherAlternateAssetService.update(clientOtherAlternateAssetDTO);
				return new ResponseEntity<ClientOtherAlternateAssetDTO>(clientOtherAlternateAssetDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_AI_OTHERS_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_OTHERS_MODULE,
						FinexaConstant.CLIENT_AI_OTHERS_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientOtherAlternateAsset/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {

		try {
			ClientOtherAlternateAssetDTO clientOtherAlternateAssetDTO = clientOtherAlternateAssetService.findById(id);
			return new ResponseEntity<ClientOtherAlternateAssetDTO>(clientOtherAlternateAssetDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_AI_OTHERS_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_OTHERS_MODULE,
					FinexaConstant.CLIENT_AI_OTHERS_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientOtherAlternateAssetsList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientOtherAlternateAssetDTO> clientOtherAlternateAssetDTOList = clientOtherAlternateAssetService
				.findAll();

		return new ResponseEntity<List<ClientOtherAlternateAssetDTO>>(clientOtherAlternateAssetDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientOtherAlternateAssetDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientOtherAlternateAssetService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_AI_OTHERS_DELETE_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_OTHERS_MODULE,
					FinexaConstant.CLIENT_AI_OTHERS_DELETE_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientOtherAlternateAsset/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientOtherAlternateAssetDTO> clientOtherAlternateAssetDTOList = clientOtherAlternateAssetService
					.findByClientId(clientId);
			return new ResponseEntity<List<ClientOtherAlternateAssetDTO>>(clientOtherAlternateAssetDTOList,
					HttpStatus.OK);
		 } catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_AI_OTHERS_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_OTHERS_MODULE,
					FinexaConstant.CLIENT_AI_OTHERS_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}

}
