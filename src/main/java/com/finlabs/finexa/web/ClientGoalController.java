package com.finlabs.finexa.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientGoalDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.MasterGoalInflationRateDTO;
import com.finlabs.finexa.dto.MasterSubAssetClassReturnDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.ClientGoal;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;

import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientGoalService;
import com.finlabs.finexa.util.ExcelClientUtility;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientGoalController {
	private static Logger log = LoggerFactory.getLogger(ClientGoalController.class);

	@Autowired
	ClientGoalService clientGoalService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/clientGoal", method = RequestMethod.POST)
	public ResponseEntity<?> create(@Valid @RequestBody ClientGoalDTO clientGoalDTO, Errors errors)
			throws FinexaBussinessException {

		log.debug("ClientGoalController create(): " + clientGoalDTO);
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientGoalDTO retDTO = clientGoalService.save(clientGoalDTO);
				return new ResponseEntity<ClientGoalDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_GOAL_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
						FinexaConstant.CLIENT_GOAL_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClientGoal", method = RequestMethod.POST)
	public ResponseEntity<?> update(@Valid @RequestBody ClientGoalDTO clientGoalDTO, Errors errors)
			throws FinexaBussinessException {

		log.debug("ClientGoalController update(): " + clientGoalDTO);
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientGoalDTO retDTO = clientGoalService.update(clientGoalDTO);
				return new ResponseEntity<ClientGoalDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_GOAL_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
						FinexaConstant.CLIENT_GOAL_UPDATE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientGoalList", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody List<ClientGoalDTO> clientGoalDTOList)
			throws FinexaBussinessException {

		try {
			ClientGoalDTO retDTO = null;
			for (ClientGoalDTO clientGoalDTO : clientGoalDTOList) {
				retDTO = clientGoalService.UpdatePriority(clientGoalDTO);
			}
			return new ResponseEntity<ClientGoalDTO>(retDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GOAL_VIEW_GOAL_PRIORITY_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_GOAL_VIEW_GOAL_PRIORITY_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientGoalListDateModified/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> clientGoalListDateModified(@PathVariable int clientId)
			throws FinexaBussinessException {
		
		try {
			
			List<ClientGoalDTO> retDTO = clientGoalService.fetchExpiredGoals(clientId);
			return new ResponseEntity<List<ClientGoalDTO>>(retDTO, HttpStatus.OK);
			
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientGoal/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {
		try {
			ClientGoalDTO clientGoalDTO = clientGoalService.findById(id);
			return new ResponseEntity<ClientGoalDTO>(clientGoalDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_GOAL_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_GOAL_GET_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientGoalList/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findAll(@PathVariable int clientId) throws FinexaBussinessException {
		try {
			List<ClientGoalDTO> clientGoalDTOList = clientGoalService.findByClientId(clientId);
			return new ResponseEntity<List<ClientGoalDTO>>(clientGoalDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_GOAL_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE, FinexaConstant.CLIENT_GOAL_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientGoal/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientGoalService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_GOAL_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_GOAL_DELETE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientGoal/getMaxPriority/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> getMaxPriority(@PathVariable int clientId) throws FinexaBussinessException {
		try {
			int i = clientGoalService.getMaxPriority(clientId);
			ClientGoalDTO clientGoalDTO = new ClientGoalDTO();
			clientGoalDTO.setPriority((byte) i);
			return new ResponseEntity<ClientGoalDTO>(clientGoalDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GOAL_GET_GOAL_PRIORITY_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_GOAL_GET_GOAL_PRIORITY_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientGoal/getDate/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> getSpecificDate(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			ClientFamilyMemberDTO clientFamilyMemberDTO = clientGoalService.getDate(clientId);
			return new ResponseEntity<ClientFamilyMemberDTO>(clientFamilyMemberDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_GOAL_GET_DATE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_GOAL_GET_DATE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "clientGoal/getAge/{familymemberId}", method = RequestMethod.GET)
	public ResponseEntity<ClientGoalDTO> getAge(@PathVariable int familymemberId) throws FinexaBussinessException {

		try {
			int age = clientGoalService.getAge(familymemberId);
			ClientGoalDTO clientGoalDTO = new ClientGoalDTO();
			clientGoalDTO.setVar1(age);
			return new ResponseEntity<ClientGoalDTO>(clientGoalDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_GOAL_GET_AGE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_GOAL_GET_AGE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "clientGoal/getLifeExpectency/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<ClientGoalDTO> getLifeExpectency(@PathVariable int clientId) throws FinexaBussinessException {

		try {
			int lf = clientGoalService.getLifeExpectency(clientId);
			ClientGoalDTO clientGoalDTO = new ClientGoalDTO();
			clientGoalDTO.setVar1(lf);
			return new ResponseEntity<ClientGoalDTO>(clientGoalDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GOAL_GET_LIFE_EXPECTANCY_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_GOAL_GET_LIFE_EXPECTANCY_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientGoal/getAgePlusRetirementAge/{familyMemberId}", method = RequestMethod.GET)
	public ResponseEntity<ClientGoalDTO> getAgePlusRetirementAge(@PathVariable int familyMemberId)
			throws FinexaBussinessException {
		try {
			ClientGoalDTO clientGoalDTO = clientGoalService.getAgePlusRetirementAge(familyMemberId);
			return new ResponseEntity<ClientGoalDTO>(clientGoalDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GOAL_GET_SUM_OF_AGE_AND_RETIREMENT_AGE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_GOAL_GET_SUM_OF_AGE_AND_RETIREMENT_AGE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	/*gourab*/
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "clientGoal/checkIfRetirementGoalExists/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> retirementGoalexist(@PathVariable int clientId) throws FinexaBussinessException
	{
		try {
			boolean retexist=clientGoalService.checkIfRetirementGoalExists(clientId);
			
			return new ResponseEntity<Boolean>(retexist, HttpStatus.OK);
		}catch(RuntimeException e)
		{
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GOAL_GET_INFLATION_RATE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_GOAL_GET_INFLATION_RATE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	
		
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "clientGoal/getInflationRate/{goalTypeId}/{date}", method = RequestMethod.GET)
	public ResponseEntity<?> getInflationRate(@PathVariable byte goalTypeId, @PathVariable Date date) throws FinexaBussinessException {

		try {
			MasterGoalInflationRateDTO retDTO = clientGoalService.getInflationRate(goalTypeId, date);
			return new ResponseEntity<MasterGoalInflationRateDTO>(retDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GOAL_GET_INFLATION_RATE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_GOAL_GET_INFLATION_RATE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "clientGoal/getCorpusPostGoalStart", method = RequestMethod.GET)
	public ResponseEntity<?> getCorpusPostGoalStart() throws FinexaBussinessException {
		try {
			MasterSubAssetClassReturnDTO retDTO = clientGoalService.getCorpusPostGoalStart();
			return new ResponseEntity<MasterSubAssetClassReturnDTO>(retDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GOAL_GET_CORPUS_GOAL_START_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_GOAL_GET_CORPUS_GOAL_START_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/upcomingGoal/{userId}/{value}", method = RequestMethod.GET)
	public ResponseEntity<?> getupcomingGoal(@PathVariable int userId, @PathVariable int value)
			throws FinexaBussinessException {
		try {
			List<ClientGoalDTO> ClientGoalDTOList = clientGoalService.findAllUpcomingGoalsByClientID(userId, value);
			return new ResponseEntity<List<ClientGoalDTO>>(ClientGoalDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_DASHBOARD_GET_UPCOMING_GOAL);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_DASHBOARD_GET_UPCOMING_GOAL,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/checkIfRetirementGoalPresentForAll/{clientId}/{goalType}", method = RequestMethod.GET)
	public ResponseEntity<List<ClientGoalDTO>> checkIfRetirementGoalPresentForAll(@PathVariable int clientId, @PathVariable int goalType)
			throws FinexaBussinessException {
		try {
			//log.debug("client id" + clientId);
			List<ClientGoalDTO> clientGoalList = clientGoalService.checkIfRetirementGoalPresentForAll(clientId, goalType);
			return new ResponseEntity<List<ClientGoalDTO>>(clientGoalList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_GOAL_CHECK_IF_RETIREMENT_GOAL_EXISTS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE,
					FinexaConstant.CLIENT_GOAL_CHECK_IF_RETIREMENT_GOAL_EXISTS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientGoalListDownloadForAdvisor/{userID}/{timePeriod}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllGoal(@PathVariable int userID,@PathVariable int timePeriod) throws FinexaBussinessException, IOException {
		
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		byte excelByte[];
		HttpHeaders header;
		try {
			List<ClientGoalDTO> clientGoalDTOList = clientGoalService.findAllUpcomingGoalsByClientID(userID, timePeriod);
			ClassLoader loader = getClass().getClassLoader();
			  
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}
       
			workbook = ExcelClientUtility.writeExcelGoalOutputDataForAdvisor(file, clientGoalDTOList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
		    header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

		    excelByte = bos.toByteArray();
			System.out.println("excelByte.length "+excelByte.length);
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception e) {
			/*FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_GOAL_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE, FinexaConstant.CLIENT_GOAL_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);*/
			
			e.printStackTrace();
		}
		return returner;
	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientGoalListDownload/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllGoal(@PathVariable int clientId) throws FinexaBussinessException, IOException {
		
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		byte excelByte[];
		HttpHeaders header;
		try {
			List<ClientGoalDTO> clientGoalDTOList = clientGoalService.findByClientId(clientId);
			ClassLoader loader = getClass().getClassLoader();
			  
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}
       
			workbook = ExcelClientUtility.writeExcelOutputData(file, clientGoalDTOList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
		    header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

		    excelByte = bos.toByteArray();
			System.out.println("excelByte.length "+excelByte.length);
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception e) {
			/*FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_GOAL_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE, FinexaConstant.CLIENT_GOAL_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);*/
			
			e.printStackTrace();
		}
		return returner;
	}
	
}
