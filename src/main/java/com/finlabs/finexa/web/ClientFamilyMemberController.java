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

import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.AdvisorService;
import com.finlabs.finexa.service.ClientFamilyMemberService;
import com.finlabs.finexa.service.LookupService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientFamilyMemberController {

	private static Logger log = LoggerFactory.getLogger(ClientFamilyMemberController.class);
	@Autowired
	ClientFamilyMemberService clientFamilyMemberService;
	@Autowired
	LookupService lookupDaoService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	AdvisorService advisorService;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/clientFamilyMember", method = RequestMethod.POST)
	public ResponseEntity<?> createCtFamilyMember(@Valid @RequestBody ClientFamilyMemberDTO clientFamilyMemberDTO,
			Errors errors, HttpServletRequest request) throws FinexaBussinessException, CustomFinexaException {
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("validationError");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientFamilyMemberDTO = clientFamilyMemberService.save(clientFamilyMemberDTO, request);
				return new ResponseEntity<ClientFamilyMemberDTO>(clientFamilyMemberDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_FAMILY_MEMBER_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_MEMBER_MODULE,
						FinexaConstant.CLIENT_FAMILY_MEMBER_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientFamilyMember", method = RequestMethod.POST)
	public ResponseEntity<?> updateCtFamilyMember(@Valid @RequestBody ClientFamilyMemberDTO clientFamilyMemberDTO,
			Errors errors, HttpServletRequest request) throws FinexaBussinessException, CustomFinexaException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("validationError");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientFamilyMemberDTO = clientFamilyMemberService.update(clientFamilyMemberDTO, request);
				return new ResponseEntity<ClientFamilyMemberDTO>(clientFamilyMemberDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_FAMILY_MEMBER_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_MEMBER_MODULE,
						FinexaConstant.CLIENT_FAMILY_MEMBER_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/clientFamilyMemberEdit", method = RequestMethod.PUT)
	public ResponseEntity<?> editCtFamilyMember(@RequestBody ClientFamilyMemberDTO clientFamilyMemberDTO,  HttpServletRequest request) throws RuntimeException, CustomFinexaException {
		clientFamilyMemberDTO = clientFamilyMemberService.update(clientFamilyMemberDTO, request);
		return new ResponseEntity<ClientFamilyMemberDTO>(clientFamilyMemberDTO, HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientFamilyMember/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {

		try {
			ClientFamilyMemberDTO clientFamilyMemberDTO = clientFamilyMemberService.findById(id);
			return new ResponseEntity<ClientFamilyMemberDTO>(clientFamilyMemberDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_FAMILY_MEMBER_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_MEMBER_MODULE,
					FinexaConstant.CLIENT_FAMILY_MEMBER_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/existAssetOrLoan/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> existAssetOrLoan(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientFamilyMemberService.existassetOrLoan(id);
			log.debug("i " + i);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_FAMILY_MEMBER_CHECK_IF_MEMBER_HAS_LOANS_ASSETS);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_MEMBER_MODULE,
					FinexaConstant.CLIENT_FAMILY_MEMBER_CHECK_IF_MEMBER_HAS_LOANS_ASSETS,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientFamilyMemberList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientFamilyMemberDTO> clientFamilyMemberDTOList = clientFamilyMemberService.findAll();

		return new ResponseEntity<List<ClientFamilyMemberDTO>>(clientFamilyMemberDTOList, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/deleteClientFamilyMember/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {
		try {
			int i = clientFamilyMemberService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_FAMILY_MEMBER_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_MEMBER_MODULE,
					FinexaConstant.CLIENT_FAMILY_MEMBER_DELETE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientFamilyMember/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientFamilyMemberDTO> clientFamilyMemberDTOList = clientFamilyMemberService.findByClientId(clientId);
			return new ResponseEntity<List<ClientFamilyMemberDTO>>(clientFamilyMemberDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_FAMILY_MEMBER_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_MEMBER_MODULE,
					FinexaConstant.CLIENT_FAMILY_MEMBER_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/familyMember/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findClientByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			ClientFamilyMemberDTO clientFamilyMemberDTOList = clientFamilyMemberService.findClientByClientId(clientId);
			return new ResponseEntity<ClientFamilyMemberDTO>(clientFamilyMemberDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_FAMILY_MEMBER_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_MEMBER_MODULE,
					FinexaConstant.CLIENT_FAMILY_MEMBER_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	// Don't edit this method as it is used in global family member image logic.
	/* vishwajeet */
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientFamilyMemberImageByClient/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> getClientFamilyMemberImageByClient(@PathVariable int clientId)
			throws FinexaBussinessException {

		try {
			List<ClientFamilyMemberDTO> clientFamilyMemberDTOList = clientFamilyMemberService
					.getClientFamilyMemberImageByClient(clientId);
			return new ResponseEntity<List<ClientFamilyMemberDTO>>(clientFamilyMemberDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GLOBAL_FAMILY_MEMBER_ICON_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FAMILY_MEMBER_MODULE,
					FinexaConstant.GLOBAL_FAMILY_MEMBER_ICON_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

}
