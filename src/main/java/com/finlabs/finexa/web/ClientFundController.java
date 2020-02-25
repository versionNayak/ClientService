package com.finlabs.finexa.web;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ClientMutualFundDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientFundService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientFundController {

	private static Logger log = LoggerFactory.getLogger(ClientFundController.class);

	@Autowired
	ClientFundService clientFundService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientFund", method = RequestMethod.POST)
	public ResponseEntity<?> createClientFund(@Valid @RequestBody ClientMutualFundDTO clientMutualFundDTO,
			Errors errors,  HttpServletRequest request) throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientMutualFundDTO = clientFundService.save(clientMutualFundDTO, request);
				return new ResponseEntity<ClientMutualFundDTO>(clientMutualFundDTO, HttpStatus.OK);
			} catch (RuntimeException rexp) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_FUND_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_FUND_MODULE,
						FinexaConstant.CLIENT_FUND_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", rexp);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientFund", method = RequestMethod.POST)
	public ResponseEntity<?> editClientFund(@Valid @RequestBody ClientMutualFundDTO clientMutualFundDTO, Errors errors,  HttpServletRequest request)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientMutualFundDTO = clientFundService.update(clientMutualFundDTO, request);
				return new ResponseEntity<ClientMutualFundDTO>(clientMutualFundDTO, HttpStatus.OK);
			} catch (RuntimeException rexp) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_FUND_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_FUND_MODULE,
						FinexaConstant.CLIENT_FUND_UPDATE_ERROR, exception != null ? exception.getErrorMessage() : "", rexp);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientFund", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@RequestParam int id) throws FinexaBussinessException {
		try {
			ClientMutualFundDTO clientMutualFundDTO = clientFundService.findById(id);
			return new ResponseEntity<ClientMutualFundDTO>(clientMutualFundDTO, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_FUND_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FUND_MODULE,
					FinexaConstant.CLIENT_FUND_GET_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", rexp);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientFundsList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {

		List<ClientMutualFundDTO> clientMutualFundDTOList = clientFundService.findAll();
		return new ResponseEntity<List<ClientMutualFundDTO>>(clientMutualFundDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientFundDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteClientMutualFund(@PathVariable int id) throws FinexaBussinessException {
		try {
			int i = clientFundService.deleteClientMutualFund(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_FUND_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FUND_MODULE,
					FinexaConstant.CLIENT_FUND_DELETE_ERROR, exception != null ? exception.getErrorMessage() : "", rexp);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientFund/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {
		try {
			LinkedList<ClientMutualFundDTO> clientMutualFundDTOList = clientFundService.findByClientId(clientId);
			
			return new ResponseEntity<LinkedList<ClientMutualFundDTO>>(clientMutualFundDTOList, HttpStatus.OK);
		} catch (RuntimeException runtimeExp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_FUND_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FUND_MODULE, FinexaConstant.CLIENT_FUND_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", runtimeExp);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getNAV", method = RequestMethod.POST)
	public ResponseEntity<?> getNAV(@RequestBody ClientMutualFundDTO clientMutualFundDTO)
			throws FinexaBussinessException {
		try {
			double mfNAV = clientFundService.getNAV(clientMutualFundDTO.getInvestmentStartDate(),
					clientMutualFundDTO.getIsin());
			return new ResponseEntity<Double>(mfNAV, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_NAV_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_FUND_MODULE, FinexaConstant.GET_NAV_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getLatestNAV/{isin}", method = RequestMethod.GET)
	public ResponseEntity<?> getLatestNAV(@PathVariable("isin") String isin) throws FinexaBussinessException{
		try {
            Double mfNAV = clientFundService.getLatestNAV(isin);
			return new ResponseEntity<Double>(mfNAV, HttpStatus.OK);
		} catch (RuntimeException e) {
            FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
                    .findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
            FinexaExceptionHandling exception = finexaExceptionHandlingRepository
                    .findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_NAV_ERROR);
            throw new FinexaBussinessException(FinexaConstant.CLIENT_FUND_MODULE, FinexaConstant.GET_NAV_ERROR,
                    exception != null ? exception.getErrorMessage() : "", e);
		}
	}
}
