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

import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientLifeExpDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.LifeExpectancyDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientLifeExpectancyService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class LifeExpectencyController {
	private static Logger log = LoggerFactory.getLogger(LifeExpectencyController.class);

	@Autowired
	ClientLifeExpectancyService clientLifeExpService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/viewLifeExpList/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> viewList(@PathVariable int id) throws FinexaBussinessException {
		try {
			List<ClientLifeExpDTO> lifeExpDTOList = clientLifeExpService.viewLifeExpList(id);
			return new ResponseEntity<List<ClientLifeExpDTO>>(lifeExpDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_LIFE_EXPECTANCY_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_EXPECTANCY_MODULE,
					FinexaConstant.CLIENT_LIFE_EXPECTANCY_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getLifeExpDetails/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getLifeExpDetails(@PathVariable int id)
			throws FinexaBussinessException, CustomFinexaException {
		try {
			LifeExpectancyDTO lifeExpectancyDTO = clientLifeExpService.findByMemberId(id);
			return new ResponseEntity<LifeExpectancyDTO>(lifeExpectancyDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_LIFE_EXPECTANCY_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_EXPECTANCY_MODULE,
					FinexaConstant.CLIENT_LIFE_EXPECTANCY_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/calculateLifeExpectancy", method = RequestMethod.POST)
	public ResponseEntity<?> calculateLifeExpectancy(@RequestBody LifeExpectancyDTO lifeExpDTO)
			throws FinexaBussinessException, CustomFinexaException {

		try {
			
			lifeExpDTO = clientLifeExpService.calculateLifeExp(lifeExpDTO);
			return new ResponseEntity<LifeExpectancyDTO>(lifeExpDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CALCULATE_LIFE_EXPECTANCY_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_EXPECTANCY_MODULE,
					FinexaConstant.CALCULATE_LIFE_EXPECTANCY_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','ClientInfoAddEdit')")
	@RequestMapping(value = "/reCalculateLifeExpectancyForClient", method = RequestMethod.POST)
	public ResponseEntity<?> reCalculateLifeExpectancy(@RequestBody LifeExpectancyDTO lifeExpDTO)
			throws RuntimeException, CustomFinexaException, FinexaBussinessException {

		try {
			lifeExpDTO = clientLifeExpService.reCalculateLifeExpectancyForClient(lifeExpDTO);

			return new ResponseEntity<LifeExpectancyDTO>(lifeExpDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.RE_CALCULATE_LIFE_EXPECTANCY_FOR_CLIENT_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_EXPECTANCY_MODULE,
					FinexaConstant.RE_CALCULATE_LIFE_EXPECTANCY_FOR_CLIENT_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','ClientInfoAddEdit')")
	@RequestMapping(value = "/reCalculateLifeExpectancyForFamilyMember", method = RequestMethod.POST)
	public ResponseEntity<?> reCalculateLifeExpectancy(@RequestBody ClientFamilyMemberDTO clientFamilyMemberDTO)
			throws RuntimeException, CustomFinexaException, FinexaBussinessException {

		try {

			LifeExpectancyDTO lifeExpDTO = clientLifeExpService
					.reCalculateLifeExpectancyForFamilyMember(clientFamilyMemberDTO);

			return new ResponseEntity<LifeExpectancyDTO>(lifeExpDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.RE_CALCULATE_LIFE_EXPECTANCY_FOR_FAMILY_MEMBER_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_EXPECTANCY_MODULE,
					FinexaConstant.RE_CALCULATE_LIFE_EXPECTANCY_FOR_FAMILY_MEMBER_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/saveLifeExp", method = RequestMethod.POST)
	public ResponseEntity<?> saveLifeExp(@Valid @RequestBody LifeExpectancyDTO lifeExpDTO, Errors errors) throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("validationError");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				lifeExpDTO = clientLifeExpService.save(lifeExpDTO);
				return new ResponseEntity<LifeExpectancyDTO>(lifeExpDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.SAVE_LIFE_EXPECTANCY_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_EXPECTANCY_MODULE,
						FinexaConstant.SAVE_LIFE_EXPECTANCY_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}
		
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/updateLifeExp", method = RequestMethod.POST)
	public ResponseEntity<?> updateLifeExp(@Valid @RequestBody LifeExpectancyDTO lifeExpDTO, Errors errors) throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("validationError");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				lifeExpDTO = clientLifeExpService.update(lifeExpDTO);
				return new ResponseEntity<LifeExpectancyDTO>(lifeExpDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_LIFE_EXPECTANCY_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_EXPECTANCY_MODULE,
						FinexaConstant.CLIENT_LIFE_EXPECTANCY_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}

		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getFamilyMemberListByLifeExp/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getFamilyMemberListByLifeExp(@PathVariable int id) throws FinexaBussinessException {
		try {
			List<ClientFamilyMemberDTO> familyMemberDTOList = clientLifeExpService.getFamilyMemberByLifeExpectancy(id);
			return new ResponseEntity<List<ClientFamilyMemberDTO>>(familyMemberDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_FAMILY_MEMBER_BY_LIFE_EXPECTANCY_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_EXPECTANCY_MODULE,
					FinexaConstant.GET_FAMILY_MEMBER_BY_LIFE_EXPECTANCY_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/deleteClientLifeExpectancy/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteClientLifeExpectancy(@PathVariable int id) throws FinexaBussinessException {
		try {
			List<ClientLifeExpDTO> lifeExpDTOList = clientLifeExpService.deleteLifeExp(id);
			return new ResponseEntity<List<ClientLifeExpDTO>>(lifeExpDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_LIFE_EXPECTANCY_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_EXPECTANCY_MODULE,
					FinexaConstant.CLIENT_LIFE_EXPECTANCY_DELETE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

}
