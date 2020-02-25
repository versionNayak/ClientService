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

import com.finlabs.finexa.dto.ClientAtalPensionYojanaDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientAtalPensionYojanaService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientAtalPensionYojanaController {
	private static Logger log = LoggerFactory.getLogger(ClientAnnuityController.class);

	@Autowired
	ClientAtalPensionYojanaService clientAtalPensionYojanaService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientAtalPensionYojana", method = RequestMethod.POST)
	public ResponseEntity<?> createClientAtalPensionYojana(
			@Valid @RequestBody ClientAtalPensionYojanaDTO clientAtalPensionYojanaDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientAtalPensionYojanaDTO retDTO = clientAtalPensionYojanaService.save(clientAtalPensionYojanaDTO, request);
				return new ResponseEntity<ClientAtalPensionYojanaDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_APY_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_APY_MODULE,
						FinexaConstant.CLIENT_ROS_APY_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientAtalPensionYojana", method = RequestMethod.POST)
	public ResponseEntity<?> editClientAtalPensionYojana(
			@Valid @RequestBody ClientAtalPensionYojanaDTO clientAtalPensionYojanaDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientAtalPensionYojanaDTO retDTO = clientAtalPensionYojanaService.save(clientAtalPensionYojanaDTO, request);
				return new ResponseEntity<ClientAtalPensionYojanaDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_ROS_APY_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_APY_MODULE,
						FinexaConstant.CLIENT_ROS_APY_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientAtalPensionYojana/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {
		try {
			ClientAtalPensionYojanaDTO clientAtalPensionYojanaDTO = clientAtalPensionYojanaService.findById(id);
			return new ResponseEntity<ClientAtalPensionYojanaDTO>(clientAtalPensionYojanaDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_APY_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_APY_MODULE,
					FinexaConstant.CLIENT_ROS_APY_GET_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientAtalPensionYojanaList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientAtalPensionYojanaDTO> clientAtalPensionYojanaDTOList = clientAtalPensionYojanaService.findAll();

		return new ResponseEntity<List<ClientAtalPensionYojanaDTO>>(clientAtalPensionYojanaDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientAtalPensionYojanaDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientAtalPensionYojanaService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_APY_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_APY_MODULE,
					FinexaConstant.CLIENT_ROS_APY_DELETE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientAtalPensionYojana/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientAtalPensionYojanaDTO> clientAtalPensionYojanaDTOList = clientAtalPensionYojanaService
					.findByClientId(clientId);
			return new ResponseEntity<List<ClientAtalPensionYojanaDTO>>(clientAtalPensionYojanaDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_APY_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_APY_MODULE,
					FinexaConstant.CLIENT_ROS_APY_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/checkIfApyPresentForAll/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<List<ClientFamilyMemberDTO>> checkIfApyPresentForAll(@PathVariable int clientId)
			throws FinexaBussinessException {
		try {
			log.debug("client id" + clientId);
			List<ClientFamilyMemberDTO> clientAPYDTOList = clientAtalPensionYojanaService
					.checkIfApyPresentForAll(clientId);
			return new ResponseEntity<List<ClientFamilyMemberDTO>>(clientAPYDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_ROS_CHECK_APY_EXIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_APY_MODULE,
					FinexaConstant.CLIENT_ROS_CHECK_APY_EXIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

}
