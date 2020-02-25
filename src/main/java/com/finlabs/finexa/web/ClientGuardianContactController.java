package com.finlabs.finexa.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ClientGuardianContactDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientGuardianContactService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientGuardianContactController {
	private static Logger log = LoggerFactory.getLogger(ClientGuardianContactController.class);

	@Autowired
	ClientGuardianContactService ClientGuardianContactService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/UpdateClientGuardianContactInfo", method = RequestMethod.POST)
	public ResponseEntity<?> createCtContactInfo(@RequestBody ClientGuardianContactDTO ClientGuardianContactDTO)
			throws FinexaBussinessException {
		try {
			ClientGuardianContactDTO = ClientGuardianContactService.save(ClientGuardianContactDTO);
			return new ResponseEntity<ClientGuardianContactDTO>(ClientGuardianContactDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GUARDIAN_CONTACT_UPDATE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GUARDIAN_CONTACT_MODULE,
					FinexaConstant.CLIENT_GUARDIAN_CONTACT_UPDATE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	// unused
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/ClientGuardianContactInfo", method = RequestMethod.PUT)
	public ResponseEntity<?> editCtContactInfo(@RequestBody ClientGuardianContactDTO ClientGuardianContactDTO) {
		ClientGuardianContactDTO = ClientGuardianContactService.update(ClientGuardianContactDTO);
		return new ResponseEntity<ClientGuardianContactDTO>(ClientGuardianContactDTO, HttpStatus.OK);

	}

	//
	// unused
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/ClientGuardianContactInfo/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) {
		ClientGuardianContactDTO ClientGuardianContactDTO = ClientGuardianContactService.findById(id);
		if (ClientGuardianContactDTO != null) {
			return new ResponseEntity<ClientGuardianContactDTO>(ClientGuardianContactDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Client not found ", HttpStatus.OK);
		}
	}

	// unused
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/ClientGuardianContactInfoList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientGuardianContactDTO> ClientGuardianContactDTOList = ClientGuardianContactService.findAll();

		return new ResponseEntity<List<ClientGuardianContactDTO>>(ClientGuardianContactDTOList, HttpStatus.OK);

	}

	// unused
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/ClientGuardianContactInfo/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable int id) {

		int i = ClientGuardianContactService.delete(id);
		return new ResponseEntity<Integer>(i, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/ClientGuardianContactInfo/guardian/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByguardianId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			ClientGuardianContactDTO ClientGuardianContactDTO = ClientGuardianContactService.findByClientID(clientId);
			return new ResponseEntity<ClientGuardianContactDTO>(ClientGuardianContactDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GUARDIAN_CONTACT_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GUARDIAN_CONTACT_MODULE,
					FinexaConstant.CLIENT_GUARDIAN_CONTACT_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "ClientGuardianContactInfo/uniquePincode", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniquePincode(@RequestParam("pincode") int pincode,
			@RequestParam("stateId") int stateId) throws FinexaBussinessException {
		try {
			boolean msg = ClientGuardianContactService.checkUniquePincode(pincode, stateId);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GUARDIAN_CONTACT_CHECK_UNIQUE_PINCODE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GUARDIAN_CONTACT_MODULE,
					FinexaConstant.CLIENT_GUARDIAN_CONTACT_CHECK_UNIQUE_PINCODE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

}
