package com.finlabs.finexa.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.finlabs.finexa.dto.AdvisorUserLoginInfoDTO;
import com.finlabs.finexa.dto.ClientGoalDTO;
import com.finlabs.finexa.dto.ClientMeetingDTO;
import com.finlabs.finexa.dto.ClientTaskDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.MasterNAVHistoryDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.AccordMonitoringService;
import com.finlabs.finexa.service.ClientMeetingService;
import com.finlabs.finexa.service.ClientTaskService;
import com.finlabs.finexa.util.ExcelClientUtility;
import com.finlabs.finexa.util.FinexaConstant;


@RestController
public class AccordMonitoringController {
	
	@Autowired
	AccordMonitoringService accordMonitoringService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;	
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "getSchemeListNoNavData", method = RequestMethod.GET)
	public ResponseEntity<?> schemeListNoNavData() throws FinexaBussinessException {
		try {
			List<MasterNAVHistoryDTO> masterNAVHistoryDTOList = accordMonitoringService.findSchemeNoDetailsList();
			return new ResponseEntity<List<MasterNAVHistoryDTO>>(masterNAVHistoryDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
//			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
//					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGERMENT_LOGGED_IN_HISTORY);
//			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
//					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
//							FinexaConstant.MY_BUSINESS_LOGGING_HISTORY_VIEW_ERROR);
//			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_LOGGING_HISTORY_MODULE,
//					FinexaConstant.MY_BUSINESS_LOGGING_HISTORY_VIEW_ERROR,
//					exception != null ? exception.getErrorMessage() : "", e);
			return null;
			
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "getSchemeListNoNavData/{nextFetch}", method = RequestMethod.GET)
	public ResponseEntity<?> schemeListNoNavData(@PathVariable int nextFetch) throws FinexaBussinessException {
		try {
			Pageable pageable = new PageRequest(nextFetch, 1000);
			List<MasterNAVHistoryDTO> masterNAVHistoryDTOList = accordMonitoringService.findSchemeNoDetailsList(pageable);
			return new ResponseEntity<List<MasterNAVHistoryDTO>>(masterNAVHistoryDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
//			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
//					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGERMENT_LOGGED_IN_HISTORY);
//			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
//					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
//							FinexaConstant.MY_BUSINESS_LOGGING_HISTORY_VIEW_ERROR);
//			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_LOGGING_HISTORY_MODULE,
//					FinexaConstant.MY_BUSINESS_LOGGING_HISTORY_VIEW_ERROR,
//					exception != null ? exception.getErrorMessage() : "", e);
			return null;
			
		}
		
	}
	
	//@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/NoNAVData/downloadExcel", method = RequestMethod.GET)
	public ResponseEntity<?> downloadClientTemplate(HttpServletResponse response)
			throws IOException, FinexaBussinessException {
		try {
//			if (fileType.equalsIgnoreCase(FinexaConstant.FILE_TYPE_CSV)) {
//				viewUserManagmentService.downloadClientTemplateCSVForImport(response);
//			} else if (fileType.equalsIgnoreCase(FinexaConstant.FILE_TYPE_EXCEL)) {
//				viewUserManagmentService.downloadClientTemplateExcel(response);
//			}
			
			accordMonitoringService.downloadNoNAVDataExcel(response);
			
			return null;
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_CLIENT_RECORDS_IMPORT_EXPORT_CLIENT);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_IMPORT_CLIENT_RECORD_TEMPLATE_DOWNLOAD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_IMPORT_EXPORT_CLIENT_RECORDS_MODULE,
					FinexaConstant.MY_BUSINESS_IMPORT_CLIENT_RECORD_TEMPLATE_DOWNLOAD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}	
	
	
	
}
