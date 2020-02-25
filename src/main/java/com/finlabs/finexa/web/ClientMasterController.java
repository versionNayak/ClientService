package com.finlabs.finexa.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.dto.ClientCashDTO;
import com.finlabs.finexa.dto.ClientInfoDTO;
import com.finlabs.finexa.dto.ClientMasterDTO;
import com.finlabs.finexa.dto.ConfirmPassDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.SearchClientDTO;
import com.finlabs.finexa.dto.UserDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.model.User;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.AdvisorService;
import com.finlabs.finexa.service.ClientMasterService;
import com.finlabs.finexa.util.CacheInfoService;
import com.finlabs.finexa.util.Constants;
import com.finlabs.finexa.util.ExcelClientUtility;
import com.finlabs.finexa.util.ExcelUtility;
import com.finlabs.finexa.util.FinexaConstant;
import com.itextpdf.text.log.SysoCounter;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@RestController
@RequestMapping("/")
public class ClientMasterController {

	private static Logger log = LoggerFactory.getLogger(ClientMasterController.class);

	@Autowired
	ClientMasterService clientMasterService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	private CacheInfoService cacheInfoService;
	@Autowired
	AdvisorService advisorService;
	@Autowired
	private SessionFactory sessionfactory;

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "clientMaster", method = RequestMethod.POST)
	public ResponseEntity<?> createClientMaster(@Valid @RequestBody ClientMasterDTO clientMasterDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException, CustomFinexaException {
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("validationError");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientMasterDTO retDTO = clientMasterService.save(clientMasterDTO);
				advisorService.deletetAUMCacheMap(clientMasterDTO.getUserId(),request);
				return new ResponseEntity<ClientMasterDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.PERSONAL_INFORMATION_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
						FinexaConstant.PERSONAL_INFORMATION_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "editClientMaster/", method = RequestMethod.POST)
	public ResponseEntity<?> updateClientMaster(@Valid @RequestBody ClientMasterDTO clientMasterDTO, Errors errors, HttpServletRequest request)
			throws FinexaBussinessException, CustomFinexaException {
	
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("validationError");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				ClientMasterDTO retDTO = clientMasterService.update(clientMasterDTO);
				advisorService.deletetAUMCacheMap(clientMasterDTO.getUserId(),request);
				return new ResponseEntity<ClientMasterDTO>(retDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.PERSONAL_INFORMATION_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
						FinexaConstant.PERSONAL_INFORMATION_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "clientMaster/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> getClientMaster(@PathVariable int clientId)
			throws FinexaBussinessException, CustomFinexaException {
		try {
			ClientMasterDTO retDTO = clientMasterService.find(clientId);
			return new ResponseEntity<ClientMasterDTO>(retDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasRole('ClientInfoView')")
	@RequestMapping(value = "/clientMasterList/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllByUser(@PathVariable int userId) throws FinexaBussinessException {
		try {
			//List<ClientInfoDTO> clientInfoList = clientMasterService.findAllClientByUserID(userId);
			List<ClientInfoDTO> clientInfoList = clientMasterService.findAllClientByUserHierarchy(userId);
			return new ResponseEntity<List<ClientInfoDTO>>(clientInfoList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_EXISTING_CLIENT_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_EXISTING_CLIENT_LIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasRole('ClientInfoView')")
	@RequestMapping(value = "/clientMasterListWithpagination/{userId}/{nextFetch}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllByUserWithPagination(@PathVariable int userId, @PathVariable int nextFetch) throws FinexaBussinessException {
		try {
			Pageable pageable = new PageRequest(nextFetch, 10);
			
			//List<ClientInfoDTO> clientInfoList = clientMasterService.findAllClientByUserID(userId);
			List<ClientInfoDTO> clientInfoList = clientMasterService.findAllClientByUserHierarchy(userId, pageable);
			return new ResponseEntity<List<ClientInfoDTO>>(clientInfoList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_EXISTING_CLIENT_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_EXISTING_CLIENT_LIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasRole('ClientInfoView')")
	@RequestMapping(value = "/searchClientsDynamically/{userId}/{matchString}", method = RequestMethod.GET)
	public ResponseEntity<?> searchClientsDynamically(@PathVariable int userId, @PathVariable String matchString) throws FinexaBussinessException {
		try {
			
			//List<ClientInfoDTO> clientInfoList = clientMasterService.findAllClientByUserID(userId);
			List<ClientInfoDTO> clientInfoList = clientMasterService.searchClientDynamicallyByUserHierarchy(userId, matchString);
			return new ResponseEntity<List<ClientInfoDTO>>(clientInfoList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_EXISTING_CLIENT_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_EXISTING_CLIENT_LIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasRole('ClientInfoView')")
	@RequestMapping(value = "/searchClientsByEmailDynamically/{userId}/{matchString}", method = RequestMethod.GET)
	public ResponseEntity<?> searchClientsByEmailDynamically(@PathVariable int userId, @PathVariable String matchString) throws FinexaBussinessException {
		try {
			
			//List<ClientInfoDTO> clientInfoList = clientMasterService.findAllClientByUserID(userId);
			List<ClientInfoDTO> clientInfoList = clientMasterService.searchClientByEmailDynamicallyByUserHierarchy(userId, matchString);
			return new ResponseEntity<List<ClientInfoDTO>>(clientInfoList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_EXISTING_CLIENT_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_EXISTING_CLIENT_LIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	// dashboard
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientMasterListDashBoard/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllDashBoardByUser(@PathVariable int userId) {
		List<ClientInfoDTO> clientInfoList = null;
		try {
			clientInfoList = clientMasterService.findAllClientDashBoardByUserID(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<List<ClientInfoDTO>>(clientInfoList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientMasterListDashBoard/{userId}/{timePeriod}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllDashBoardByUserAndTimePeriod(@PathVariable int userId,@PathVariable int timePeriod) {
		List<ClientInfoDTO> clientInfoList = null;
		try {
			clientInfoList = clientMasterService.findAllDashBoardByUserAndTimePeriod(userId,timePeriod);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<List<ClientInfoDTO>>(clientInfoList, HttpStatus.OK);
	}

	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit','UserManagementAddEdit')")
	@RequestMapping(value = "/clientMaster/uniqueEmail", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniqueEmail(@RequestParam("email") String email) {
		boolean retValue = clientMasterService.isUniqueEmailId(email);
		return new ResponseEntity<Boolean>(retValue, HttpStatus.OK);
	}


	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit','UserManagementAddEdit')")
	@RequestMapping(value = "/clientMaster/uniquePan", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniquePan(@RequestParam("pan") String pan) throws FinexaBussinessException {
		try {
			boolean retValue = clientMasterService.isUniquePan(pan);
			return new ResponseEntity<Boolean>(retValue, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_CHECK_UNIQUE_PAN_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_CHECK_UNIQUE_PAN_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	//======unique PAN check respective to a particular Advisor========//
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/clientMaster/uniquePanForFixedAdvisor", method = RequestMethod.GET)
	//public ResponseEntity<?> checkUniquePan(@RequestParam("pan") String pan) throws FinexaBussinessException {
	public ResponseEntity<?> checkUniquePanForFixedAdvisor(@RequestParam("pan") String pan, @RequestParam("advisorID") int advisorID) throws FinexaBussinessException {
		try {
			log.debug("inside checkUniquePan");
			//boolean retValue = clientMasterService.isUniquePan(pan);
			boolean retValue = clientMasterService.isUniquePanForFixedAdvisor(pan, advisorID);
			return new ResponseEntity<Boolean>(retValue, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_CHECK_UNIQUE_PAN_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_CHECK_UNIQUE_PAN_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/clientMaster/uniqueAadhar", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniqueAadhar(@RequestParam("aadhar") String aadhar)
			throws FinexaBussinessException {
		try {
			
			boolean retValue = clientMasterService.isUniqueAadhar(aadhar);
			return new ResponseEntity<Boolean>(retValue, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_CHECK_UNIQUE_AADHAR_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_CHECK_UNIQUE_AADHAR_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	//======unique AADHAR check respective to a particular Advisor========//
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/clientMaster/uniqueAadharForFixedAdvisor", method = RequestMethod.GET)
	public ResponseEntity<?> uniqueAadharForFixedAdvisor(@RequestParam("aadhar") String aadhar, @RequestParam("advisorID") int advisorID)
			throws FinexaBussinessException {
		try {
			
			boolean retValue = clientMasterService.isUniqueAadharForFixedAdvisor(aadhar, advisorID);
			return new ResponseEntity<Boolean>(retValue, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_CHECK_UNIQUE_AADHAR_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_CHECK_UNIQUE_AADHAR_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/clientMaster/existPan/{pan}/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> checkPanExists(@PathVariable String pan, @PathVariable int clientId)
			throws FinexaBussinessException {

		try {
			boolean msg = clientMasterService.checkPanExists(pan, clientId);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_CHECK_IF_PAN_EXISTS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_CHECK_IF_PAN_EXISTS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/clientMaster/existAadhar/{clientId}/{aadhar}", method = RequestMethod.GET)
	public ResponseEntity<?> checkAadharExists(@PathVariable long aadhar, @PathVariable int clientId)
			throws FinexaBussinessException {
		try {
			boolean msg = clientMasterService.checkAadharExists(aadhar, clientId);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_CHECK_IF_AADHAR_EXISTS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_CHECK_IF_AADHAR_EXISTS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete')")
	@RequestMapping(value = "/clientMaster/delete", method = RequestMethod.GET)
	public ResponseEntity<?> decativateClient(@RequestParam("clientId") int clientId, HttpServletRequest request) throws FinexaBussinessException {
		try {
			//log.debug("client id to deactivate = " + clientId);
			ClientMaster retValue = clientMasterService.deActivate(clientId, request);
			return new ResponseEntity<ClientMaster>(retValue, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_DELETE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientMaster/search", method = RequestMethod.POST)
	public ResponseEntity<?> searchClient(@RequestBody SearchClientDTO searchClientDTO)
			throws FinexaBussinessException {
		try {
			List<ClientInfoDTO> list = clientMasterService.searchClient(searchClientDTO);
			return new ResponseEntity<List<ClientInfoDTO>>(list, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.PERSONAL_INFORMATION_SEARCH_ERROR);
			throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
					FinexaConstant.PERSONAL_INFORMATION_SEARCH_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	// MyBusiness Contact Management
	@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	@RequestMapping(value = "/clientMasterRecords/search", method = RequestMethod.POST)
	public ResponseEntity<?> searchClientRecords(@RequestBody SearchClientDTO searchClientDTO)
			throws FinexaBussinessException {
		try {
			List<ClientInfoDTO> list = clientMasterService.searchClientBusiness(searchClientDTO);
			return new ResponseEntity<List<ClientInfoDTO>>(list, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_CLIENT_RECORDS_CLIENT_CONTACT_MANAGEMENT);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.SEARCH_CLIENT_MASTER_RECORDS);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_CLIENT_CONTACT_MANAGEMENT_MODULE,
					FinexaConstant.SEARCH_CLIENT_MASTER_RECORDS, exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	@RequestMapping(value = "/clientMasterRecords/searchDownload", method = RequestMethod.GET)
	public ResponseEntity<?> searchClientRecordsDownload(@RequestParam String searchData,
			HttpServletResponse response) {
		SearchClientDTO searchClientDTO = new SearchClientDTO();
		try {
			searchClientDTO = new ObjectMapper().readValue(searchData, SearchClientDTO.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<ClientInfoDTO> list = clientMasterService.searchClient(searchClientDTO);

		String fileName = "FilteredClientRecords.xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());
			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addClientRecordsHeader(excelOutputsheet);
			addClientRecordsBody(list, excelOutputsheet);
			writableWorkbook.write();
			writableWorkbook.close();

		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}
		return null;
	}
	
	//@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	private void addClientRecordsBody(List<ClientInfoDTO> list, WritableSheet sheet)
			throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		int rowIndex = 1;// because header row is already added

		for (ClientInfoDTO client : list) {
			sheet.addCell(new Label(0, rowIndex, client.getFirstName()));
			sheet.addCell(new Label(1, rowIndex, client.getGender()));
			sheet.addCell(new Label(2, rowIndex, "" + client.getMobile()));
			sheet.addCell(new Label(3, rowIndex, client.getEmailId()));
			sheet.addCell(new Label(4, rowIndex, client.getAddress1()));
			sheet.addCell(new Label(5, rowIndex, client.getCity()));
			sheet.addCell(new Label(6, rowIndex, client.getState()));
			sheet.addCell(new Label(7, rowIndex, client.getCountry()));
			rowIndex++;
		}

	}
	//@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	private void addClientRecordsHeader(WritableSheet sheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub

		sheet.addCell(new Label(0, 0, "Client Name"));
		sheet.addCell(new Label(1, 0, "Gender"));
		sheet.addCell(new Label(2, 0, "Mobile Number"));
		sheet.addCell(new Label(3, 0, "Email ID"));
		sheet.addCell(new Label(4, 0, "Address"));
		sheet.addCell(new Label(5, 0, "City"));
		sheet.addCell(new Label(6, 0, "State"));
		sheet.addCell(new Label(7, 0, "Country"));

	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','ClientRecordsView')")
	@RequestMapping(value = "/clients/orgs/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllOrganizations(@PathVariable int advisorID) throws FinexaBussinessException {
		try {
			List<String> org = clientMasterService.getAllOrganiations(advisorID);
			return new ResponseEntity<List<String>>(org, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_CLIENT_RECORDS_CLIENT_CONTACT_MANAGEMENT);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_ORGANIZATIONS_OF_CLIENTS_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_CLIENT_CONTACT_MANAGEMENT_MODULE,
					FinexaConstant.GET_ORGANIZATIONS_OF_CLIENTS_LIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	//
	// Dashboard
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/addedClients/{userId}/{value}", method = RequestMethod.GET)
	public ResponseEntity<?> getAlladdedClients(@PathVariable int userId, @PathVariable int value) {
		int i = 0;
		try {
			i = clientMasterService.findAllAddedClientByUserID(userId, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Integer>(i, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/findClientBirthdayByUserID/{userId}/{value}", method = RequestMethod.GET)
	public ResponseEntity<?> findClientBirthdayByUserID(@PathVariable int userId, @PathVariable int value)
			throws ParseException, RuntimeException, CustomFinexaException {
		
		List<ClientInfoDTO> clientInfoDTO = new ArrayList<ClientInfoDTO>();
		try {
			clientInfoDTO = clientMasterService.findClientBirthdayByUserID(userId, value);
			return new ResponseEntity<List<ClientInfoDTO>>(clientInfoDTO, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<List<ClientInfoDTO>>(clientInfoDTO, HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientBirthdayDownloadForAdvisor/{userId}/{timePeriodbirthModal}", method = RequestMethod.GET)
	public ResponseEntity<?> clientBirthdayDownloadForAdvisor(@PathVariable int userId, @PathVariable int timePeriodbirthModal)
			throws ParseException, RuntimeException, CustomFinexaException {
				
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		byte excelByte[];
		HttpHeaders header;
		try {
			List<ClientInfoDTO> clientBirthdayList = clientMasterService.findClientBirthdayByUserID(userId, timePeriodbirthModal);
			ClassLoader loader = getClass().getClassLoader();
			  
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientBirthday", "111", "Download Failed");
			}
       
			workbook = ExcelClientUtility.writeExcelBirthdayOutputDataForAdvisor(file, clientBirthdayList);
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
	//

	/*
	 * 
	 * @RequestMapping(value="/clients/downloadTemplate",
	 * method=RequestMethod.GET) public ResponseEntity<?>
	 * downloadMasterTemplate(HttpServletResponse response) {
	 * clientMasterService.downloadClientTemplate(response);
	 * 
	 * return null; }
	 */
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clearClientCache", method = RequestMethod.GET)
public ResponseEntity<?> clearClientCache(@RequestParam int client, HttpServletRequest request) {

		
		String header = request.getHeader(Constants.HEADER_STRING);
		String token = cacheInfoService.getToken(header);
		cacheInfoService.deleteCache(FinexaConstant.CALCULATION_TYPE_CONSTANT+token, client);
		System.out.println("*****************Clear Cache*******************" + cacheInfoService.getCacheList(
				FinexaConstant.CALCULATION_TYPE_CONSTANT+token, String.valueOf(client),"NETSURPLUS-CAL"));
		
		System.out.println(cacheInfoService.getCacheMap(
					FinexaConstant.CALCULATION_TYPE_CONSTANT+token, String.valueOf(client),"FINANCIAL_EARMARKED_MAP"));
		
		return new ResponseEntity<String>("successfully deleted", HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/clientMasterListDownloadForAdvisor/{userID}", method = RequestMethod.GET)
	public ResponseEntity<?> clientMasterListDownloadForAdvisor(@PathVariable int userID) throws FinexaBussinessException, IOException {
		
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		byte excelByte[];
		HttpHeaders header;
		List<ClientInfoDTO> clientInfoList = null;
		try {
			clientInfoList = clientMasterService.findAllClientDashBoardByUserID(userID);
			ClassLoader loader = getClass().getClassLoader();
			  
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}
       
			workbook = ExcelClientUtility.writeExcelMasterDataForAdvisor(file, clientInfoList);
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
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/showUser", method = RequestMethod.GET)
	public ResponseEntity<?> showUser() {
		//List<AdvisorDTO> advisorDTOList = advisorService.findAll();
		List<AdvisorDTO> advisorDTOList = advisorService.findAllAdvisorAdmin();
		return new ResponseEntity<List<AdvisorDTO>>(advisorDTOList, HttpStatus.OK);
	}
	
	//pagination=================
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/showUserWithPagination/{nextFetch}", method = RequestMethod.GET)
	public ResponseEntity<?> showUserWithPagination(@PathVariable int nextFetch) {
		Pageable pageable = new PageRequest(nextFetch, 100);
		
		List<AdvisorDTO> advisorDTOList = advisorService.findAllWithPagination(pageable);
		return new ResponseEntity<List<AdvisorDTO>>(advisorDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/getAdvisorCount", method = RequestMethod.GET)
	public ResponseEntity<?> getAdvisorCount() {
		
		int advisorCount = advisorService.getAdvisorCount();
		return new ResponseEntity<Integer>(advisorCount, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/getSelfAdvisorCount", method = RequestMethod.GET)
	public ResponseEntity<?> getSelfAdvisorCount() {
		
		int selfAdvisorCount = advisorService.getSelfAdvisorCount();
		return new ResponseEntity<Integer>(selfAdvisorCount, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/getOrgAdvisorCount/{masterId}", method = RequestMethod.GET)
	public ResponseEntity<?> getOrgAdvisorCount(@PathVariable int masterId) {
		
		int selfAdvisorCount = advisorService.getOrgAdvisorCount(masterId);
		return new ResponseEntity<Integer>(selfAdvisorCount, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/showUserSelfWithPagination/{nextFetchSelf}", method = RequestMethod.GET)
	public ResponseEntity<?> showUserSelfWithPagination(@PathVariable int nextFetchSelf) {
		Pageable pageable = new PageRequest(nextFetchSelf, 100);
		
		List<AdvisorDTO> advisorDTOList = advisorService.findAllSelf(pageable);
		return new ResponseEntity<List<AdvisorDTO>>(advisorDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/showUserOrgWithPagination/{advisorid}/{nextFetchOrg}", method = RequestMethod.GET)
	public ResponseEntity<?> showUserOrgWithPagination(@PathVariable int advisorid, @PathVariable int nextFetchOrg) {
		Pageable pageable = new PageRequest(nextFetchOrg, 100);
		
		List<AdvisorDTO> advisorDTOList = advisorService.findAllOrg(advisorid, pageable);
		return new ResponseEntity<List<AdvisorDTO>>(advisorDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/searchMatchingEntries/{matchString}", method = RequestMethod.GET)
	public ResponseEntity<?> searchMatchingEntries(@PathVariable String matchString) {
		//Pageable pageable = new PageRequest(nextFetch, 100);
		
		List<AdvisorDTO> advisorDTOList = advisorService.searchMatchingEntries(matchString);
		return new ResponseEntity<List<AdvisorDTO>>(advisorDTOList, HttpStatus.OK);
	}
	//====================================
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/findAllHierarchyList/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> showUserHierarch(@PathVariable int advisorID) {
		List<UserDTO> userDTOList = advisorService.findAllHierarchyList(advisorID);
		return new ResponseEntity<List<UserDTO>>(userDTOList, HttpStatus.OK);
	}
	//=====================================
	//no extra checking needed because UM is only given Advisor Admin
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/checkIfUserExistUnderLoggedInAdvisorAdmin/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> checkIfUserExistUnderLoggedInAdvisorAdmin(@PathVariable int advisorID) {
		int count = advisorService.checkIfUserExistUnderLoggedInAdvisorAdmin(advisorID);
		return new ResponseEntity<Integer>(count, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/showMaster", method = RequestMethod.GET)
	public ResponseEntity<?> showMaster() {
		List<AdvisorDTO> advisorDTOList = advisorService.findAllMaster();
		return new ResponseEntity<List<AdvisorDTO>>(advisorDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/showUserOrg/{advisorid}", method = RequestMethod.GET)
	public ResponseEntity<?> showUserOrg(@PathVariable int advisorid) {
		List<AdvisorDTO> advisorDTOList = advisorService.findAllOrg(advisorid);
		return new ResponseEntity<List<AdvisorDTO>>(advisorDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/showUserSelf", method = RequestMethod.GET)
	public ResponseEntity<?> showUserSelf() {
		List<AdvisorDTO> advisorDTOList = advisorService.findAllSelf();
		return new ResponseEntity<List<AdvisorDTO>>(advisorDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','UserManagementView')")
	@RequestMapping(value = "/showSelf", method = RequestMethod.GET)
	public ResponseEntity<?> showSelf() {
		List<AdvisorDTO> userList = advisorService.findSelf();
		return new ResponseEntity<List<AdvisorDTO>>(userList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value = "/deactivateActiveFlag/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> deactivateActiveFlag(@PathVariable int id) {
		int status = advisorService.deactivateActiveFlag(id);
		return new ResponseEntity<Integer>(status, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value = "/activateActiveFlag/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> activateActiveFlag(@PathVariable int id) {
		int status = advisorService.activateActiveFlag(id);
		return new ResponseEntity<Integer>(status, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value = "/allOrgName", method = RequestMethod.GET)
	public ResponseEntity<?> findByOrgFlag() {
		List<AdvisorDTO> advisorDTOList = advisorService.findAllOrgName();
		return new ResponseEntity<List<AdvisorDTO>>(advisorDTOList, HttpStatus.OK);
	}
	/*@RequestMapping(value = "/deactivateActiveFlag", method = RequestMethod.GET)
	public ResponseEntity<?> deactivateActiveFlag(@PathVariable AdvisorDTO advisorDTO) {
		advisorDTO = advisorService.deactivateActiveFlag(advisorDTO);
		return new ResponseEntity<AdvisorDTO>(advisorDTO, HttpStatus.OK);
	}*/
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value = "/downloadReport/self", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public ResponseEntity<?> downloadExcelOutputSelf() {
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		Session session = null;
		try {
			//ClientIncomeService clientIncomeSeriveImpl = new ClientIncomeServiceImpl();
			session = sessionfactory.openSession();
			//ClientFamilyIncomeOutput incomeOutput = clientIncomeSeriveImpl.getCLientFamilyAllIncomes(clientId, mode,
			//		fpFlag, session);
			
			List<AdvisorDTO> advisorDTOList = advisorService.findAllSelf();
			
			ClassLoader loader = getClass().getClassLoader();
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}

			workbook = ExcelUtility.writeExcelOutputData(file, advisorDTOList);
			
						
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

			byte excelByte[] = bos.toByteArray();
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
		return returner;
	}
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value = "/downloadReport/org/{id}", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public ResponseEntity<?> downloadExcelOutputOrg(@PathVariable int id) {
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		Session session = null;
		try {
			//ClientIncomeService clientIncomeSeriveImpl = new ClientIncomeServiceImpl();
			session = sessionfactory.openSession();
			//ClientFamilyIncomeOutput incomeOutput = clientIncomeSeriveImpl.getCLientFamilyAllIncomes(clientId, mode,
			//		fpFlag, session);
			
			List<AdvisorDTO> advisorDTOList = advisorService.findAllOrg(id);
			
			ClassLoader loader = getClass().getClassLoader();
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}

			workbook = ExcelUtility.writeExcelOutputData(file, advisorDTOList);
			
						
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

			byte excelByte[] = bos.toByteArray();
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
		return returner;
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/checkIfRelationAlreadyExists/{userId}/{relationID}", method = RequestMethod.GET)
	public ResponseEntity<?> checkIfRelationAlreadyExists(@PathVariable int userId,@PathVariable byte relationID) {
		boolean status = false;
		try {
			status = clientMasterService.checkIfRelationAlreadyExists(userId,relationID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Boolean>(status, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/checkIfFamilypresent/{clientID}", method = RequestMethod.GET)
	public ResponseEntity<?> checkIfFamilypresent(@PathVariable int clientID) {
		boolean status = false;
		try {
			status = clientMasterService.checkIfFamilypresent(clientID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Boolean>(status, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit','AdvisorAdmin')")
	//@RequestMapping(value = "groupFamilyMembers/{userId}/{clientId1}/{clientId2}/{relationID}/{otherRelation}", method = RequestMethod.GET)
	@RequestMapping(value = "groupFamilyMembers", method = RequestMethod.GET)
	public ResponseEntity<?> groupFamilyMembers(
			@RequestParam(value = "userId") int userId, 
			@RequestParam(value = "clientId1") int clientId1, 
			@RequestParam(value = "clientId2") int clientId2, 
			@RequestParam(value = "relationID") byte relationID, 
			@RequestParam(value = "otherRelation") String otherRelation, 
//			Errors errors, 
			HttpServletRequest request)
			throws FinexaBussinessException, CustomFinexaException {
//		ErrorDTO result = new ErrorDTO();
//		if (errors.hasErrors()) {
//			result.setErrorCode("validationError");
//			result.setErrorMessage(
//					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
//			return ResponseEntity.badRequest().body(result);
//		} else {
			try {
				String otherRelationDesc = null;
				if (otherRelation != null && !(otherRelation.isEmpty())) {
					otherRelationDesc = otherRelation;
				}
				boolean status = clientMasterService.groupFamilyMembers(clientId1, clientId2, relationID, otherRelationDesc);
				advisorService.deletetAUMCacheMap(userId, request);
				return new ResponseEntity<Boolean>(status, HttpStatus.OK);
			} catch (RuntimeException e) {
				e.printStackTrace();
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.PERSONAL_INFORMATION_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.PERSONAL_INFORMATION_MODULE,
						FinexaConstant.PERSONAL_INFORMATION_ADD_ERROR, exception != null ? exception.getErrorMessage() : "",
						e);
			}
		//}
	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/changePasswordClient", method = RequestMethod.POST)
	public ResponseEntity<?> changePasswordClient(@RequestBody ConfirmPassDTO confirmPassDTO) {
		boolean flag = false;
		flag = clientMasterService.changePasswordByEmailId(confirmPassDTO.getPassword(),confirmPassDTO.getEmailId());
		if (!flag) {
			ErrorDTO err = new ErrorDTO("InvalidEmail", "The provided Email is not available in the system");
			return new ResponseEntity<>(err, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(flag, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/changePasswordClientBeforeLogin", method = RequestMethod.GET)
	public ResponseEntity<?> changePasswordClient(@RequestParam(value = "emailID") String emailID,@RequestParam(value = "password") String password) {
		boolean flag = false;
		flag = clientMasterService.changePasswordByEmailId(password, emailID);
		if (!flag) {
			ErrorDTO err = new ErrorDTO("InvalidEmail", "The provided Email is not available in the system");
			return new ResponseEntity<>(err, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(flag, HttpStatus.OK);
		}
	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/checkExistingClientPassword/{email}/{password}", method = RequestMethod.GET)
	public ResponseEntity<?> checkExistingPasswordClient(@PathVariable String email, @PathVariable String password)
			throws FinexaBussinessException {
		try {
			boolean exist = false;
			exist = clientMasterService.checkPasswordExists(email, password);
			System.out.println("exist "+exist);
		
			return new ResponseEntity<Boolean>(exist, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("", "", "");
		
		}
	}

}
