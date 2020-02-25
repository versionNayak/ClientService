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

import com.finlabs.finexa.dto.ClientPreciousMetalDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientPreciousMetalService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientPreciousMetalController {

	private static Logger log = LoggerFactory.getLogger(ClientPreciousMetalController.class);

	@Autowired
	ClientPreciousMetalService clientPreciousMetalService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientPreciousMetal", method = RequestMethod.POST)
	public ResponseEntity<?> createClientPreciousMetal(
			@Valid @RequestBody ClientPreciousMetalDTO clientPreciousMetalDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientPreciousMetalDTO = clientPreciousMetalService.save(clientPreciousMetalDTO, request);
				return new ResponseEntity<ClientPreciousMetalDTO>(clientPreciousMetalDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_AI_PRECIOUS_METALS_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_PRECIOUS_METALS_MODULE,
						FinexaConstant.CLIENT_AI_PRECIOUS_METALS_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}

		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientPreciousMetal", method = RequestMethod.POST)
	public ResponseEntity<?> editClientPreciousMetal(@Valid @RequestBody ClientPreciousMetalDTO clientPreciousMetalDTO,
			Errors errors, HttpServletRequest request) throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientPreciousMetalDTO = clientPreciousMetalService.update(clientPreciousMetalDTO, request);
				return new ResponseEntity<ClientPreciousMetalDTO>(clientPreciousMetalDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_AI_PRECIOUS_METALS_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_PRECIOUS_METALS_MODULE,
						FinexaConstant.CLIENT_AI_PRECIOUS_METALS_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientPreciousMetal/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {

		try {
			ClientPreciousMetalDTO clientPreciousMetalDTO = clientPreciousMetalService.findById(id);
			return new ResponseEntity<ClientPreciousMetalDTO>(clientPreciousMetalDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_AI_PRECIOUS_METALS_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_PRECIOUS_METALS_MODULE,
					FinexaConstant.CLIENT_AI_PRECIOUS_METALS_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientPreciousMetalsList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientPreciousMetalDTO> clientPreciousMetalDTOList = clientPreciousMetalService.findAll();

		return new ResponseEntity<List<ClientPreciousMetalDTO>>(clientPreciousMetalDTOList, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientPreciousMetalsDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientPreciousMetalService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_AI_PRECIOUS_METALS_DELETE_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_PRECIOUS_METALS_MODULE,
					FinexaConstant.CLIENT_AI_PRECIOUS_METALS_DELETE_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientPreciousMetal/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientPreciousMetalDTO> clientPreciousMetalDTOList = clientPreciousMetalService
					.findByClientId(clientId);
			return new ResponseEntity<List<ClientPreciousMetalDTO>>(clientPreciousMetalDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_AI_PRECIOUS_METALS_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_PRECIOUS_METALS_MODULE,
					FinexaConstant.CLIENT_AI_PRECIOUS_METALS_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

}
