package com.finlabs.finexa.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ClientROSDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientROSService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientROSController {
	
	private static Logger log = LoggerFactory.getLogger(LifeExpectencyController.class);

	@Autowired
	ClientROSService clientROSService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/viewROSList/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> viewList(@PathVariable int id) throws FinexaBussinessException {

		try {
			List<ClientROSDTO> clientROSDTOList = clientROSService.viewClientROSList(id);
			return new ResponseEntity<List<ClientROSDTO>>(clientROSDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_MODULE, FinexaConstant.CLIENT_ROS_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

}
