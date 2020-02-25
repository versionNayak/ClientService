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

import com.finlabs.finexa.dto.ClientGuardianDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientGuardianService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientGuardianController {
	private static Logger log = LoggerFactory.getLogger(ClientGuardianController.class);

	@Autowired
	ClientGuardianService ClientGuardianService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/ClientGuardianInfo", method = RequestMethod.POST)
	public ResponseEntity<?> createCtContactInfo(@Valid @RequestBody ClientGuardianDTO ClientGuardianDTO, Errors errors)
			throws FinexaBussinessException {
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientGuardianDTO = ClientGuardianService.save(ClientGuardianDTO);
				return new ResponseEntity<ClientGuardianDTO>(ClientGuardianDTO, HttpStatus.OK);
			} catch (Exception e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_GUARDIAN_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_GUARDIAN_MODULE,
						FinexaConstant.CLIENT_GUARDIAN_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}

	// unused
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/ClientGuardianInfo", method = RequestMethod.PUT)
	public ResponseEntity<?> editCtContactInfo(@RequestBody ClientGuardianDTO ClientGuardianDTO) {
		ClientGuardianDTO = ClientGuardianService.update(ClientGuardianDTO);
		return new ResponseEntity<ClientGuardianDTO>(ClientGuardianDTO, HttpStatus.OK);

	}

	// unused
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/ClientGuardianInfo/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) {
		ClientGuardianDTO clientGuardianDTO = ClientGuardianService.findById(id);
		log.debug("birth date = " + clientGuardianDTO.getBirthDate());
		return new ResponseEntity<ClientGuardianDTO>(clientGuardianDTO, HttpStatus.OK);

	}

	// unused
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/ClientGuardianInfoList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientGuardianDTO> ClientGuardianDTOList = ClientGuardianService.findAll();

		return new ResponseEntity<List<ClientGuardianDTO>>(ClientGuardianDTOList, HttpStatus.OK);

	}

	// unused
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/ClientGuardianInfo/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable int id) {

		int i = ClientGuardianService.delete(id);
		return new ResponseEntity<Integer>(i, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/ClientGuardianInfo/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {
		try {
			ClientGuardianDTO clientGuardianDTO = ClientGuardianService.findByClientId(clientId);
			if (clientGuardianDTO != null)
				return new ResponseEntity<ClientGuardianDTO>(clientGuardianDTO, HttpStatus.OK);
			else {
				clientGuardianDTO = new ClientGuardianDTO();
				clientGuardianDTO.setId(0);
				return new ResponseEntity<ClientGuardianDTO>(clientGuardianDTO, HttpStatus.OK);
			}
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GUARDIAN_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GUARDIAN_MODULE,
					FinexaConstant.CLIENT_GUARDIAN_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/ClientGuardianandGuardianContactDelete/{guardianId}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteGuardianandGuardianContactDelete(@PathVariable int guardianId)
			throws FinexaBussinessException {
		try {
			int i = ClientGuardianService.deleteGuardianandGuardianContact(guardianId);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GUARDIAN_ALSO_GUARDIAN_CONTACT_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GUARDIAN_MODULE,
					FinexaConstant.CLIENT_GUARDIAN_ALSO_GUARDIAN_CONTACT_DELETE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
}
