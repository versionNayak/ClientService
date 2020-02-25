package com.finlabs.finexa.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ClientLifeInsuranceDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.FinexaMessageDto;
import com.finlabs.finexa.dto.MasterInsurancePolicyDTO;
import com.finlabs.finexa.dto.LookupLifeInsurancePolicyTypeDTO;
import com.finlabs.finexa.dto.MasterInsuranceCompanyNameDTO;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.model.LookupInsurancePolicyType;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.ClientLifeInsuranceService;
import com.finlabs.finexa.util.ExcelClientUtility;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class ClientLifeInsuranceController {

	private static Logger log = LoggerFactory.getLogger(ClientLifeInsuranceController.class);

	@Resource(name = "exceptionmap")
	private Map<String, String> exceptionmap;
	@Autowired
	ClientLifeInsuranceService clientLifeInsuranceService;
	LookupInsurancePolicyType lookupInsurancePolicyType;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/createClientLifeInsurance", method = RequestMethod.POST)
	public ResponseEntity<?> createClientLifeInsurance(
			@Valid @RequestBody ClientLifeInsuranceDTO clientLifeInsuranceDTO, Errors errors)
			throws FinexaBussinessException {

		log.debug("ClientLifeInsuranceService createClientLifeInsurance(): " + clientLifeInsuranceDTO);
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientLifeInsuranceDTO = clientLifeInsuranceService.save(clientLifeInsuranceDTO);
				return new ResponseEntity<ClientLifeInsuranceDTO>(clientLifeInsuranceDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_LIFE_INSURANCE_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
						FinexaConstant.CLIENT_LIFE_INSURANCE_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "",  e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value = "/editClienLifeInsurance", method = RequestMethod.POST)
	public ResponseEntity<?> editClientLifeInsurance(@Valid @RequestBody ClientLifeInsuranceDTO clientLifeInsuranceDTO,
			Errors errors) throws FinexaBussinessException {

		log.debug("ClientLifeInsuranceService editClientLifeInsurance(): " + clientLifeInsuranceDTO);
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				clientLifeInsuranceDTO = clientLifeInsuranceService.update(clientLifeInsuranceDTO);
				return new ResponseEntity<ClientLifeInsuranceDTO>(clientLifeInsuranceDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_LIFE_INSURANCE_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
						FinexaConstant.CLIENT_LIFE_INSURANCE_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientLifeInsurance/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable int id) throws FinexaBussinessException {
		try {
			ClientLifeInsuranceDTO clientLifeInsuranceDTO = clientLifeInsuranceService.findById(id);
			return new ResponseEntity<ClientLifeInsuranceDTO>(clientLifeInsuranceDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_LIFE_INSURANCE_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_LIFE_INSURANCE_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/deleteclientLifeInsurance/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteclientLifeInsurance(@PathVariable int id) throws FinexaBussinessException {
		try {
			int i = clientLifeInsuranceService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_LIFE_INSURANCE_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_LIFE_INSURANCE_DELETE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientLifeInsuranceList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList = clientLifeInsuranceService.findAll();

		return new ResponseEntity<List<ClientLifeInsuranceDTO>>(clientLifeInsuranceDTOList, HttpStatus.OK);
	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/LifeInsuranceCompanyList", method = RequestMethod.GET)
	public ResponseEntity<?> LifeInsuranceCompanyList() throws FinexaBussinessException {
		try {
			List<MasterInsuranceCompanyNameDTO> masterInsuranceCompanyNameList = clientLifeInsuranceService
					.getClientLifeInsuranceCompanyList();
			return new ResponseEntity<List<MasterInsuranceCompanyNameDTO>>(masterInsuranceCompanyNameList,
					HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_LIFE_INSURANCE_COMPANY_NAME_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_INSURANCE_COMPANY_NAME_MODULE,
					FinexaConstant.GET_LIFE_INSURANCE_COMPANY_NAME_LIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/LifeInsurancePolicyTypeList", method = RequestMethod.GET)
	public ResponseEntity<?> LifeInsurancePolicyTypeList() throws FinexaBussinessException {
		try {
			List<LookupLifeInsurancePolicyTypeDTO> lookupInsurancePolicyTypeList = clientLifeInsuranceService
					.getClientLifeInsurancePolicyTypeList();
			return new ResponseEntity<List<LookupLifeInsurancePolicyTypeDTO>>(lookupInsurancePolicyTypeList,
					HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_LIFE_INSURANCE_POLICY_TYPE_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_INSURANCE_POLICY_TYPE_MODULE,
					FinexaConstant.GET_LIFE_INSURANCE_POLICY_TYPE_LIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientLifeInsuranceCompanyPolicyTypeList", method = RequestMethod.GET)
	public ResponseEntity<?> clientLifeInsuranceCompanyPolicyTypeList(@RequestParam int companyId) {
		List<MasterInsurancePolicyDTO> masterInsurancePolicyDTOList = clientLifeInsuranceService
				.ClientLifeInsuranceCompanyPolicyList(companyId);

		return new ResponseEntity<List<MasterInsurancePolicyDTO>>(masterInsurancePolicyDTOList, HttpStatus.OK);
	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientLifeInsuranceCompanyPolicyName", method = RequestMethod.GET)
	public ResponseEntity<?> clientLifeInsuranceCompanyPolicyTypeList(@RequestParam int companyId,
			@RequestParam byte insurancePolicyTypeId) {
		MasterInsurancePolicyDTO masterInsurancePolicyDTO = clientLifeInsuranceService
				.ClientLifeInsuranceCompanyPolicyName(companyId, insurancePolicyTypeId);

		return new ResponseEntity<MasterInsurancePolicyDTO>(masterInsurancePolicyDTO, HttpStatus.OK);
	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/clientLifeInsurance/client/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientId(@PathVariable int clientId) throws FinexaBussinessException {
		try {
			log.debug("ClientLifeInsuranceController >> findByClientId()");
			List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList = clientLifeInsuranceService
					.findByClientId(clientId);
			log.debug("ClientLifeInsuranceController << findByClientId()");
			return new ResponseEntity<List<ClientLifeInsuranceDTO>>(clientLifeInsuranceDTOList, HttpStatus.OK);
		} catch (RuntimeException finexaBussExp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", finexaBussExp);
		}
	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/checkClientLifePolicyNumber", method = RequestMethod.GET)
	public ResponseEntity<?> findPolicyNumber(@RequestParam String policyNumber, @RequestParam int clientId)
			throws CustomFinexaException, FinexaBussinessException {
		try {
			log.debug("ClientLifeInsuranceController >> findByClientId()");
			FinexaMessageDto finexaMessageDto = clientLifeInsuranceService.checkAvailPolicyNumber(policyNumber,
					clientId);
			log.debug("ClientLifeInsuranceController << findByClientId()");
			return new ResponseEntity<FinexaMessageDto>(finexaMessageDto, HttpStatus.OK);
		} catch (RuntimeException finexaBussExp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_LIFE_INSURANCE_CHECK_POLICY_NUMBER);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_LIFE_INSURANCE_CHECK_POLICY_NUMBER,
					exception != null ? exception.getErrorMessage() : "", finexaBussExp);
		}
	}
	//client dashboard
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getLockedUptoDate", method = RequestMethod.GET)
	public ResponseEntity<?> getLockedUptoDate(@RequestParam int timePeriod, @RequestParam int clientId)
			throws CustomFinexaException, FinexaBussinessException {
		try {
			log.debug("ClientLifeInsuranceController >> findByClientId()");
			List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList = clientLifeInsuranceService.getLockedUptoDate(clientId, timePeriod);
			log.debug("ClientLifeInsuranceController << findByClientId()");
			return new ResponseEntity<List<ClientLifeInsuranceDTO>>(clientLifeInsuranceDTOList, HttpStatus.OK);
		} catch (RuntimeException finexaBussExp) {
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_CODE, FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_DESC,
					finexaBussExp);
		}
	}
	//advisor dashboard
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getLockedUptoDateForAdvisor", method = RequestMethod.GET)
	public ResponseEntity<?> getLockedUptoDateForAdvisor(@RequestParam int advisorUserID,@RequestParam int timePeriod)
			throws CustomFinexaException, FinexaBussinessException {
		try {
			log.debug("ClientLifeInsuranceController >> findByClientId()");
			List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList = clientLifeInsuranceService.getLockedUptoDateForAdvisor(advisorUserID, timePeriod);
			log.debug("ClientLifeInsuranceController << findByClientId()");
			return new ResponseEntity<List<ClientLifeInsuranceDTO>>(clientLifeInsuranceDTOList, HttpStatus.OK);
		} catch (RuntimeException finexaBussExp) {
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_CODE, FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_DESC,
					finexaBussExp);
		}
	}
	//client dashboard
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getLockedUptoDateTimePeriod", method = RequestMethod.GET)
	public ResponseEntity<?> getLockedUptoDateTimePeriod(@RequestParam int timePeriod, @RequestParam int clientId)
			throws CustomFinexaException, FinexaBussinessException {
		try {
			log.debug("ClientLifeInsuranceController >> findByClientId()");
			List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList = clientLifeInsuranceService.getLockedUptoDateTimePeriod(clientId, timePeriod);
			log.debug("ClientLifeInsuranceController << findByClientId()");
			return new ResponseEntity<List<ClientLifeInsuranceDTO>>(clientLifeInsuranceDTOList, HttpStatus.OK);
		} catch (RuntimeException finexaBussExp) {
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_CODE, FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_DESC,
					finexaBussExp);
		}
	}
	//client dashboard
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getLockedUptoDateTimePeriodDownload", method = RequestMethod.GET)
	public ResponseEntity<?> getLockedUptoDateTimePeriodDownload(@RequestParam int clientId, @RequestParam int timePeriod)
			throws CustomFinexaException, FinexaBussinessException {
		
		Session session = null;
		//log.info("ClientIncomeController >>> Entering downloadExcelOutputExl() ");
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		byte excelByte[];
		HttpHeaders header;
	
		try {
		
			List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList = clientLifeInsuranceService.getLockedUptoDateTimePeriod(clientId, timePeriod);
			
		    System.out.println("clientLifeInsuranceDTOList jjj"+clientLifeInsuranceDTOList.size());
			ClassLoader loader = getClass().getClassLoader();
		  
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}
       
			workbook = ExcelClientUtility.writeExcelOutputDataInsurance(file, clientLifeInsuranceDTOList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
		    header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

		    excelByte = bos.toByteArray();
			System.out.println("excelByte.length "+excelByte.length);
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			exp.printStackTrace();
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			//session.close();

		}
		return returner ;
	}
	//advisor dashboard
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getLockedUptoDateTimePeriodForAdvisor", method = RequestMethod.GET)
	public ResponseEntity<?> getLockedUptoDateTimePeriodForAdvisor(@RequestParam int advisorUserID,@RequestParam int timePeriod)
			throws CustomFinexaException, FinexaBussinessException {
		try {
			log.debug("ClientLifeInsuranceController >> findByClientId()");
			List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList = clientLifeInsuranceService.getLockedUptoDateTimePeriodForAdvisor(advisorUserID, timePeriod);
			log.debug("ClientLifeInsuranceController << findByClientId()");
			return new ResponseEntity<List<ClientLifeInsuranceDTO>>(clientLifeInsuranceDTOList, HttpStatus.OK);
		} catch (RuntimeException finexaBussExp) {
			throw new FinexaBussinessException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
					FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_CODE, FinexaConstant.CLIENT_LIFE_INSURANCE_VIEW_DESC,
					finexaBussExp);
		}
	}
	
	
	    @PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
		@RequestMapping(value = "/getLockedUptoDateTimePeriodForAdvisorDownload", method = RequestMethod.GET)
		public ResponseEntity<?> getLockedUptoDateTimePeriodForAdvisorDownload(@RequestParam int userID, @RequestParam int timePeriod)
				throws CustomFinexaException, FinexaBussinessException {
			
			Session session = null;
			//log.info("ClientIncomeController >>> Entering downloadExcelOutputExl() ");
			XSSFWorkbook workbook = null;
			ResponseEntity<?> returner = null;
			byte excelByte[];
			HttpHeaders header;
		
			try {
			
				List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList = clientLifeInsuranceService.getLockedUptoDateTimePeriodForAdvisor(userID, timePeriod);
			    System.out.println("clientLifeInsuranceDTOList jjj"+clientLifeInsuranceDTOList.size());
				ClassLoader loader = getClass().getClassLoader();
			  
				File file = null;
				if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
					file = new File(loader.getResource("Excel_Output.xlsx").getFile());
				} else {
					throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
				}
	       
				workbook = ExcelClientUtility.writeExcelOutputDataInsuranceForAdvisor(file, clientLifeInsuranceDTOList);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
			    header = new HttpHeaders();
				header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

			    excelByte = bos.toByteArray();
				System.out.println("excelByte.length "+excelByte.length);
				header.setContentLength(excelByte.length);
				returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
			} catch (Exception exp) {
				FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
						"Failed To download  , Please Try again later.", exp);
				FinexaBussinessException.logFinexaBusinessException(businessException);
				exp.printStackTrace();
				return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
			} finally {
				//session.close();

			}
			return returner ;
		}
}