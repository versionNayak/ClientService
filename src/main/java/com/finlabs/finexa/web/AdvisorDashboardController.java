package com.finlabs.finexa.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import com.finlabs.finexa.dto.ClientGoalDTO;
import com.finlabs.finexa.dto.ClientMeetingDTO;
import com.finlabs.finexa.dto.ClientTaskDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.ClientMeetingService;
import com.finlabs.finexa.service.ClientTaskService;
import com.finlabs.finexa.util.ExcelClientUtility;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class AdvisorDashboardController {
	
	private static Logger log = LoggerFactory.getLogger(AdvisorDashboardController.class);
	
	@Autowired
	ClientMeetingService clientMeetingService;
	@Autowired
	ClientTaskService clientTaskService;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientMeeting", method = RequestMethod.POST)
	public ResponseEntity<?> createClientMeeting(@Valid @RequestBody ClientMeetingDTO clientMeetingDTO, Errors errors)
			throws FinexaBussinessException {
		log.debug("clientCashDTO: " + clientMeetingDTO);
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientMeetingDTO = clientMeetingService.save(clientMeetingDTO);
				return new ResponseEntity<ClientMeetingDTO>(clientMeetingDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CASH_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE,
						FinexaConstant.CLIENT_CASH_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoDelete')")
	@RequestMapping(value = "/clientMeeting/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientMeetingService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_NON_LIFE_INSURANCE_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_NON_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_NON_LIFE_INSURANCE_DELETE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoView')")
	@RequestMapping(value = "/clientMeeting/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findByuserID(@PathVariable int id) throws FinexaBussinessException {
         
		try {
			ClientMeetingDTO clientMeetingDTO;
			clientMeetingDTO= clientMeetingService.findByMeetingId(id);
			return new ResponseEntity<ClientMeetingDTO>(clientMeetingDTO, HttpStatus.OK);
		} catch (Exception e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_NON_LIFE_INSURANCE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_NON_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_NON_LIFE_INSURANCE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoView')")
	@RequestMapping(value = "/clientMeetingList/user/{id}/{timePeriod}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllclientMeetingList(@PathVariable int id,@PathVariable int timePeriod) throws FinexaBussinessException {
		//System.out.println("id "+id);
		//System.out.println("timePeriod "+timePeriod);
			List<ClientMeetingDTO> clientMeetingDTOList = null;
			try {
				clientMeetingDTOList = clientMeetingService.findAllClientMeetingByUserID(id,timePeriod);
			}catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CASH_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE,
						FinexaConstant.CLIENT_CASH_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}

		return new ResponseEntity<List<ClientMeetingDTO>>(clientMeetingDTOList,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoView')")
	@RequestMapping(value = "/clientMeetingListDownloadForAdvisor/{userID}/{timePeriod}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllGoal(@PathVariable int userID,@PathVariable int timePeriod) throws FinexaBussinessException, IOException {
		
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		byte excelByte[];
		HttpHeaders header;
		try {
			List<ClientMeetingDTO> clientMeetingDTOList = clientMeetingService.findAllClientMeetingByUserID(userID,timePeriod);
			ClassLoader loader = getClass().getClassLoader();
			  
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}
       
			workbook = ExcelClientUtility.writeExcelMeetingOutputDataForAdvisor(file, clientMeetingDTOList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
		    header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

		    excelByte = bos.toByteArray();
			//System.out.println("excelByte.length "+excelByte.length);
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_GOAL_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE, FinexaConstant.CLIENT_GOAL_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
			
			
		}
		return returner;
	}
	
	///Client Task
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientTask", method = RequestMethod.POST)
	public ResponseEntity<?> createClientTask(@Valid @RequestBody ClientTaskDTO clientTaskDTO, Errors errors)
			throws FinexaBussinessException {
		
		/*ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);*/
       // } else {
			try {
				clientTaskDTO = clientTaskService.save(clientTaskDTO);
				return new ResponseEntity<ClientTaskDTO>(clientTaskDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CASH_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE,
						FinexaConstant.CLIENT_CASH_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
			}
		//}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoDelete')")
	@RequestMapping(value = "/clientTask/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteTask(@PathVariable int id) throws FinexaBussinessException {

		try {
			int i = clientTaskService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_NON_LIFE_INSURANCE_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_NON_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_NON_LIFE_INSURANCE_DELETE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoView')")
	@RequestMapping(value = "/clientTask/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findByTaskUserID(@PathVariable int id) throws FinexaBussinessException {
         
		try {
			ClientTaskDTO clientTaskDTO;
			clientTaskDTO= clientTaskService.findBytaskId(id);
			return new ResponseEntity<ClientTaskDTO>(clientTaskDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_NON_LIFE_INSURANCE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_NON_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_NON_LIFE_INSURANCE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoView')")
	@RequestMapping(value = "/clientTaskList/user/{id}/{timePeriod}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllclientTaskList(@PathVariable int id,@PathVariable int timePeriod) throws FinexaBussinessException {
		//System.out.println("id "+id);
		//System.out.println("timePeriod "+timePeriod);
		List<ClientTaskDTO> clientTaskDTOList = new ArrayList<ClientTaskDTO>();
		try {
			clientTaskDTOList = clientTaskService.findAllClientTaskByUserID(id,timePeriod);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CASH_ADD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE,
					FinexaConstant.CLIENT_CASH_ADD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
		return new ResponseEntity<List<ClientTaskDTO>>(clientTaskDTOList,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoView')")
	@RequestMapping(value = "/clientTaskListDownloadForAdvisor/{userID}/{timePeriod}", method = RequestMethod.GET)
	public ResponseEntity<?> clientTaskListDownloadForAdvisor(@PathVariable int userID,@PathVariable int timePeriod) throws FinexaBussinessException, IOException {
		
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		byte excelByte[];
		HttpHeaders header;
		try {
			List<ClientTaskDTO> clientTaskDTOList = clientTaskService.findAllClientTaskByUserID(userID,timePeriod);
			ClassLoader loader = getClass().getClassLoader();
			  
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}
       
			workbook = ExcelClientUtility.writeExcelTaskOutputDataForAdvisor(file, clientTaskDTOList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
		    header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

		    excelByte = bos.toByteArray();
			//System.out.println("excelByte.length "+excelByte.length);
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_GOAL_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_GOAL_MODULE, FinexaConstant.CLIENT_GOAL_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
			
			
		}
		return returner;
	}
	
}
