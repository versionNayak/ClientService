package com.finlabs.finexa.web;

import java.io.Serializable;
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

import com.finlabs.finexa.dto.ClientContactDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientContactService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientContactController{

	private static Logger log = LoggerFactory.getLogger(ClientContactController.class);

	@Autowired
	ClientContactService clientContactService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/updateClientContactInfo", method = RequestMethod.POST)
	public ResponseEntity<?> createCtContactInfo(@Valid @RequestBody ClientContactDTO clientContactDTO, Errors errors)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("validationError");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientContactDTO retDTO = clientContactService.save(clientContactDTO);
				return new ResponseEntity<ClientContactDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_CONTACT_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_CONTACT_MODULE,
						FinexaConstant.CLIENT_CONTACT_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/updateClientContactInfo/{userId}", method = RequestMethod.POST)
	public ResponseEntity<?> createCtContactInfo(@PathVariable int userId, @Valid @RequestBody ClientContactDTO clientContactDTO, Errors errors)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("validationError");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientContactDTO retDTO = clientContactService.save(userId, clientContactDTO);
				return new ResponseEntity<ClientContactDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_CONTACT_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_CONTACT_MODULE,
						FinexaConstant.CLIENT_CONTACT_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	// unused
	@RequestMapping(value = "/clientContactInfo", method = RequestMethod.PUT)
	public ResponseEntity<?> editCtContactInfo(@RequestBody ClientContactDTO clientContactDTO) {
		clientContactDTO = clientContactService.update(clientContactDTO);
		return new ResponseEntity<ClientContactDTO>(clientContactDTO, HttpStatus.OK);

	}

	//
	// unused
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientContactInfo/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) {
		ClientContactDTO clientContactDTO = clientContactService.findById(id);

		if (clientContactDTO != null) {
			return new ResponseEntity<ClientContactDTO>(clientContactDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Client not found ", HttpStatus.OK);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	// unused
	@RequestMapping(value = "/clientContactInfoList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientContactDTO> clientContactDTOList = clientContactService.findAll();

		return new ResponseEntity<List<ClientContactDTO>>(clientContactDTOList, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	// unused
	@RequestMapping(value = "/clientContactInfo/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable int id) {

		int i = clientContactService.delete(id);
		return new ResponseEntity<Integer>(i, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientContactInfo/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientContactDTO> clientContactDTOList = clientContactService.findByClientId(clientId);
			return new ResponseEntity<ClientContactDTO>(clientContactDTOList.get(0), HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CONTACT_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_CONTACT_MODULE,
					FinexaConstant.CLIENT_CONTACT_GET_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "clientContactInfo/existEmail/{email}/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> checkEmailExists(@PathVariable String email, @PathVariable int clientId)
			throws FinexaBussinessException {
		try {

			boolean msg = clientContactService.checkEmailExists(email, clientId);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_CONTACT_CHECK_EMAIL_EXISTS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_CONTACT_MODULE,
					FinexaConstant.CLIENT_CONTACT_CHECK_EMAIL_EXISTS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "clientContactInfo/existMobile/{clientId}/{mobile}", method = RequestMethod.GET)
	public ResponseEntity<?> checkMobileExists(@PathVariable long mobile, @PathVariable int clientId)
			throws FinexaBussinessException {
		try {
			
			boolean msg = clientContactService.checkMobileExists(mobile, clientId);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_CONTACT_CHECK_MOBILENO_EXISTS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_CONTACT_MODULE,
					FinexaConstant.CLIENT_CONTACT_CHECK_MOBILENO_EXISTS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "clientContactInfo/uniqueEmail", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniqueEmail(@RequestParam("email") String email) throws FinexaBussinessException {
		try {
			boolean msg = clientContactService.checkUniqueEmail(email);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_CONTACT_CHECK_UNIQUE_EMAIL_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_CONTACT_MODULE,
					FinexaConstant.CLIENT_CONTACT_CHECK_UNIQUE_EMAIL_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "clientContactInfo/uniquePincode", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniquePincode(@RequestParam("pincode") int pincode,
			@RequestParam("stateId") int stateId) throws FinexaBussinessException {
		try {
			boolean msg = clientContactService.checkUniquePincode(pincode, stateId);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_CONTACT_GET_CHECK_UNIQUE_PINCODE);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_CONTACT_MODULE,
					FinexaConstant.CLIENT_CONTACT_GET_CHECK_UNIQUE_PINCODE,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "clientContactInfo/uniqueMobile", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniqueMobile(@RequestParam("mobile") long mobile) throws FinexaBussinessException {
		try {
			boolean msg = clientContactService.checkUniqueMobile(mobile);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_CONTACT_CHECK_UNIQUE_MOBILENO_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_CONTACT_MODULE,
					FinexaConstant.CLIENT_CONTACT_CHECK_UNIQUE_MOBILENO_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	/*
	 * @RequestMapping(value = "/aa", method = RequestMethod.GET) public
	 * ResponseEntity<?> aa() { String msg = "ssadsa"; return new
	 * ResponseEntity<String>(msg, HttpStatus.OK); }
	 */

	// My Business - ContactManagement
	@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	@RequestMapping(value = "/clients/cities/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllCities(@PathVariable int advisorID) throws FinexaBussinessException {
		try {
		
			List<String> list = clientContactService.findAllCities(advisorID);
			return new ResponseEntity<List<String>>(list, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_CLIENT_RECORDS_CLIENT_CONTACT_MANAGEMENT);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_CITIES_OF_CLIENTS_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_CLIENT_CONTACT_MANAGEMENT_MODULE,
					FinexaConstant.GET_CITIES_OF_CLIENTS_LIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
}
