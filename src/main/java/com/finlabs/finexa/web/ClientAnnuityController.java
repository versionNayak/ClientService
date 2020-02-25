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

import com.finlabs.finexa.dto.ClientAnnuityDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.LookupAnnuityTypeDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.AdvisorService;
import com.finlabs.finexa.service.ClientAnnuityService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientAnnuityController {
	private static Logger log = LoggerFactory.getLogger(ClientAnnuityController.class);
	@Autowired
	ClientAnnuityService clientAnnuityService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createclientAnnuity", method = RequestMethod.POST)
	public ResponseEntity<?> createclientAnnuity(@Valid @RequestBody ClientAnnuityDTO clientAnnuityDTO, Errors errors,HttpServletRequest request)
			throws FinexaBussinessException {
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientAnnuityDTO retDTO = clientAnnuityService.save(clientAnnuityDTO,request);
				
				return new ResponseEntity<ClientAnnuityDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_ROS_ANNUITY_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_ANNUITY_MODULE,
						FinexaConstant.CLIENT_ROS_ANNUITY_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editclientAnnuity", method = RequestMethod.POST)
	public ResponseEntity<?> editclientAnnuity(@Valid @RequestBody ClientAnnuityDTO clientAnnuityDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {

		log.debug("ClientAnnuityController editclientAnnuity(): " + clientAnnuityDTO);
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientAnnuityDTO retDTO = clientAnnuityService.update(clientAnnuityDTO, request);
				return new ResponseEntity<ClientAnnuityDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_ROS_ANNUITY_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_ANNUITY_MODULE,
						FinexaConstant.CLIENT_ROS_ANNUITY_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientAnnuity/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {
		try {
			ClientAnnuityDTO clientAnnuityDTO = clientAnnuityService.findById(id);
			return new ResponseEntity<ClientAnnuityDTO>(clientAnnuityDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_ROS_ANNUITY_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_ANNUITY_MODULE,
					FinexaConstant.CLIENT_ROS_ANNUITY_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientAnnuityList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientAnnuityDTO> clientAnnuityDTOList = clientAnnuityService.findAll();

		return new ResponseEntity<List<ClientAnnuityDTO>>(clientAnnuityDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientAnnuityDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientAnnuityService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_ROS_ANNUITY_DELETE_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_ANNUITY_MODULE,
					FinexaConstant.CLIENT_ROS_ANNUITY_DELETE_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientAnnuity/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientAnnuityDTO> clientAnnuityDTOList = clientAnnuityService.findByClientId(clientId);
			for(ClientAnnuityDTO xDTO : clientAnnuityDTOList) {
				System.out.println("Annuity Typ:e " + xDTO.getAnnuityType());
			}
			return new ResponseEntity<List<ClientAnnuityDTO>>(clientAnnuityDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_ANNUITY_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_ANNUITY_MODULE,
					FinexaConstant.CLIENT_ROS_ANNUITY_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllAnnuityType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllAnnuityList() throws FinexaBussinessException {

		try {
			List<LookupAnnuityTypeDTO> clientAnnuityDTOList = clientAnnuityService.getAllAnnuityTypes();
			return new ResponseEntity<List<LookupAnnuityTypeDTO>>(clientAnnuityDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.LOOKUP_ANNUITY_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_ANNUITY_TYPE_MODULE,
					FinexaConstant.LOOKUP_ANNUITY_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "checkIfAnnuityPresentForAll/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<List<ClientFamilyMemberDTO>> checkIfAnnuityPresentForAll(@PathVariable int clientId)
			throws FinexaBussinessException {
		try {
			log.debug("client id" + clientId);
			List<ClientFamilyMemberDTO> dtoList = clientAnnuityService.checkIfAnnuityPresentForAll(clientId);
			return new ResponseEntity<List<ClientFamilyMemberDTO>>(dtoList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_ANNUITY_MODULE, 
						"Some Error Code", "Failed to check if Annuity is present for all.");
		}
	}

}
