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
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.FinexaSubscriptionDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.FinexaSubscriptionService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class FinexaSubscriptionController {
	private static Logger log = LoggerFactory.getLogger(FinexaSubscriptionController.class);

	@Autowired
	FinexaSubscriptionService finexaSubscriptionService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;

	@PreAuthorize("hasAnyRole('Admin')")
	@RequestMapping(value = "/createSubscription", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody FinexaSubscriptionDTO finexaSubscriptionDTO)
			throws FinexaBussinessException {

		try {
			FinexaSubscriptionDTO retDTO = finexaSubscriptionService.save(finexaSubscriptionDTO);
			return new ResponseEntity<FinexaSubscriptionDTO>(retDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_FINEXA_SUBSCRIPTION_SUBSCRIBE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_FINEXA_SUBSCRIPTION_ADD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_FINEXA_SUBSCRIPTION_MODULE,
					FinexaConstant.MY_BUSINESS_FINEXA_SUBSCRIPTION_ADD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@RequestMapping(value = "/subscriptionList/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllSubscriptionsByAdvId(@PathVariable int advisorId) throws FinexaBussinessException {
		try {
			List<FinexaSubscriptionDTO> dtoList = finexaSubscriptionService.getAllSubscriptionByAdvisor(advisorId);
			return new ResponseEntity<List<FinexaSubscriptionDTO>>(dtoList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_FINEXA_SUBSCRIPTION_SUBSCRIPTION_HISTORY);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_FINEXA_SUBSCRIPTION_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_FINEXA_SUBSCRIPTION_MODULE,
					FinexaConstant.MY_BUSINESS_FINEXA_SUBSCRIPTION_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@RequestMapping(value = "/subscription/{subId}", method = RequestMethod.GET)
	public ResponseEntity<?> findSubscriptionById(@PathVariable int subId) throws FinexaBussinessException {
		try {
			FinexaSubscriptionDTO dto = finexaSubscriptionService.getSubscriptionById(subId);
			return new ResponseEntity<FinexaSubscriptionDTO>(dto, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_FINEXA_SUBSCRIPTION_SUBSCRIPTION_HISTORY);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_FINEXA_SUBSCRIPTION_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_FINEXA_SUBSCRIPTION_MODULE,
					FinexaConstant.MY_BUSINESS_FINEXA_SUBSCRIPTION_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
}