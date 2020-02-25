package com.finlabs.finexa.web;

import java.util.Date;
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

import com.finlabs.finexa.dto.ClientEquityDTO;
import com.finlabs.finexa.dto.ClientPpfDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.MasterDirectEquityDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientEquityService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientEquityController {

	private static Logger log = LoggerFactory.getLogger(ClientEquityController.class);

	@Autowired
	ClientEquityService clientEquityService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientEquity", method = RequestMethod.POST)
	public ResponseEntity<?> createClientEquity(@Valid @RequestBody ClientEquityDTO clientEquityDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientEquityDTO = clientEquityService.save(clientEquityDTO, request);
				return new ResponseEntity<ClientEquityDTO>(clientEquityDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_DIRECT_EQUITY_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_DIRECT_EQUITY_MODULE,
						FinexaConstant.CLIENT_DIRECT_EQUITY_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientEquity", method = RequestMethod.POST)
	public ResponseEntity<?> editClientEquity(@Valid @RequestBody ClientEquityDTO clientEquityDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException {
		
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientEquityDTO = clientEquityService.update(clientEquityDTO, request);
				return new ResponseEntity<ClientEquityDTO>(clientEquityDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_DIRECT_EQUITY_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_DIRECT_EQUITY_MODULE,
						FinexaConstant.CLIENT_DIRECT_EQUITY_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientEquity/{id}", method = RequestMethod.GET)
	// public ResponseEntity<?> findById(@RequestParam int id) {
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {
		try {
			ClientEquityDTO clientEquityDTO = clientEquityService.findById(id);
			return new ResponseEntity<ClientEquityDTO>(clientEquityDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_DIRECT_EQUITY_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_DIRECT_EQUITY_MODULE,
					FinexaConstant.CLIENT_DIRECT_EQUITY_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientEquityList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientEquityDTO> clientEquityDTOList = clientEquityService.findAll();

		return new ResponseEntity<List<ClientEquityDTO>>(clientEquityDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientEquityDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientEquityService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_DIRECT_EQUITY_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_DIRECT_EQUITY_MODULE,
					FinexaConstant.CLIENT_DIRECT_EQUITY_DELETE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientEquity/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			List<ClientEquityDTO> clientEquityDTOList = clientEquityService.findByClientId(clientId);
			return new ResponseEntity<List<ClientEquityDTO>>(clientEquityDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_DIRECT_EQUITY_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_DIRECT_EQUITY_MODULE,
					FinexaConstant.CLIENT_DIRECT_EQUITY_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/securityNameList", method = RequestMethod.GET)
	public ResponseEntity<?> securityNameList() throws FinexaBussinessException {
		try {
			List<MasterDirectEquityDTO> materDirectEqutyDTOlist = clientEquityService.securityNameList();
			return new ResponseEntity<List<MasterDirectEquityDTO>>(materDirectEqutyDTOlist, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MASTER_DIRECT_EQUITY_ADD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_DIRECT_EQUITY_MODULE,
					FinexaConstant.MASTER_DIRECT_EQUITY_ADD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/calculateEquityCurrentValue", method = RequestMethod.POST)
	public ResponseEntity<?> calculateEquityCurrentValue(@RequestBody ClientEquityDTO clientEquityDTO)
			throws FinexaBussinessException {
		try {
			ClientEquityDTO dto = clientEquityService.calculateEquityCurrentValue(clientEquityDTO);
			return new ResponseEntity<ClientEquityDTO>(dto, HttpStatus.OK);
		} catch (RuntimeException e) {
			/*FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_ROS_CALCULATE_PPF_MATURITY_DATE_ERROR);*/
			throw new FinexaBussinessException(FinexaConstant.CLIENT_DIRECT_EQUITY_MODULE,
					"Some Code",
					"Failed to Calculate Equity Current Value", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getClosingPriceESOP", method = RequestMethod.POST)
	public ResponseEntity<?> getClosingPriceESOP(@RequestBody ClientEquityDTO clientEquityDTO) throws FinexaBussinessException {
		try {
			ClientEquityDTO dto = clientEquityService.getClosingPrice(clientEquityDTO);
			return new ResponseEntity<ClientEquityDTO>(dto, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException(FinexaConstant.CLIENT_DIRECT_EQUITY_MODULE, "Some Code", 
					"Failed to get Closing Price for ESOP and calculate current market value", e);
		}
	}
}
