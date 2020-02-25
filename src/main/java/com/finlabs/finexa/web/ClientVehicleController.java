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

import com.finlabs.finexa.dto.ClientVehicleDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientVehicleService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientVehicleController {

	private static Logger log = LoggerFactory.getLogger(ClientVehicleController.class);

	@Autowired
	ClientVehicleService clientVehicleService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientVehicle", method = RequestMethod.POST)
	public ResponseEntity<?> createClientVehicle(@Valid @RequestBody ClientVehicleDTO clientVehicleDTO, Errors errors)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientVehicleDTO = clientVehicleService.save(clientVehicleDTO);
				return new ResponseEntity<ClientVehicleDTO>(clientVehicleDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_AI_VEHICLES_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_VEHICLES_MODULE,
						FinexaConstant.CLIENT_AI_VEHICLES_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientVehicle", method = RequestMethod.POST)
	public ResponseEntity<?> editClientVehicle(@Valid @RequestBody ClientVehicleDTO clientVehicleDTO, Errors errors)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientVehicleDTO = clientVehicleService.update(clientVehicleDTO);
				return new ResponseEntity<ClientVehicleDTO>(clientVehicleDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_AI_VEHICLES_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_VEHICLES_MODULE,
						FinexaConstant.CLIENT_AI_VEHICLES_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientVehicle/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {

		try {
			ClientVehicleDTO clientVehicleDTO = clientVehicleService.findById(id);
			return new ResponseEntity<ClientVehicleDTO>(clientVehicleDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_AI_VEHICLES_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_VEHICLES_MODULE,
					FinexaConstant.CLIENT_AI_VEHICLES_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientVehiclesList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientVehicleDTO> clientVehicleDTOList = clientVehicleService.findAll();

		return new ResponseEntity<List<ClientVehicleDTO>>(clientVehicleDTOList, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientVehicleDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientVehicleService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_AI_VEHICLES_DELETE_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_VEHICLES_MODULE,
					FinexaConstant.CLIENT_AI_VEHICLES_DELETE_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientVehicle/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientVehicleDTO> clientVehicleDTOList = clientVehicleService.findByClientId(clientId);
			return new ResponseEntity<List<ClientVehicleDTO>>(clientVehicleDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_AI_VEHICLES_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_AI_VEHICLES_MODULE,
					FinexaConstant.CLIENT_AI_VEHICLES_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

}
