package com.finlabs.finexa.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.MasterTransactSipDTO;
import com.finlabs.finexa.dto.MasterTransactSipLiveDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;

import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.model.LookupAssetAllocationCategory;
import com.finlabs.finexa.model.LookupAssetSubClass;
import com.finlabs.finexa.model.LookupGoalHorizonBucket;
import com.finlabs.finexa.model.LookupRiskProfile;
import com.finlabs.finexa.model.MasterDirectEquity;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.model.MasterTransactCountryNationality;
import com.finlabs.finexa.model.MasterTransactSourceOfWealth;

import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.repository.LookupAssetAllocationCategoryRepository;
import com.finlabs.finexa.repository.LookupAssetSubClassRepository;
import com.finlabs.finexa.repository.LookupGoalHorizonBucketRepository;
import com.finlabs.finexa.repository.LookupRiskProfileRepository;

import com.finlabs.finexa.service.LookupAssetAllocationCategoryService;
import com.finlabs.finexa.service.LookupAssetAllocationService;
import com.finlabs.finexa.service.LookupAssetClassService;
import com.finlabs.finexa.service.LookupBucketLogicService;
import com.finlabs.finexa.service.MasterMutualFundETFService;
import com.finlabs.finexa.service.MasterService;
import com.finlabs.finexa.service.MasterUploadHistoryService;

import com.finlabs.finexa.service.MasterTransactService;

import com.finlabs.finexa.util.FinexaConstant;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@RestController
public class MasterTransactController {
	private static Logger log = LoggerFactory.getLogger(MasterController.class);
	@Resource(name = "exceptionmap")
	private Map<String, String> exceptionmap;
	
	@Autowired
	MasterTransactService masterTranactService;
	
/*
 * Pls Delete
 * 
	@Autowired
	LookupAssetSubClassRepository lookupAssetSubClassRepository;
	@Autowired
	LookupRiskProfileRepository lookupRiskProfileRepository;
	@Autowired
	LookupGoalHorizonBucketRepository lookupGoalHorizonBucketRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

*
*
*/	
	
//-------------------------------------------------------------------	
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	
	@RequestMapping(value = "/allSIPRecords/{schemeCode}/{sipFrequency}", method = RequestMethod.GET)
	public ResponseEntity<?> allSIPRecords(@PathVariable String schemeCode, @PathVariable String sipFrequency) throws FinexaBussinessException, CustomFinexaException {
		/*try {
			List<MasterTransactSipDTO> masterTransactSipDTO = masterTranactService.getAllSIPRecords(schemeCode, sipFrequency);
			return new ResponseEntity<List<MasterTransactSipDTO>>(masterTransactSipDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve Transact SIP details " +e.getMessage());
		}*/
		
		try {
			MasterTransactSipDTO masterTransactSipDTO = masterTranactService.getAllSIPRecords(schemeCode, sipFrequency);
			return new ResponseEntity<MasterTransactSipDTO>(masterTransactSipDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve Transact SIP details " +e.getMessage());
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/allSIPLiveRecords/{schemeCode}/{sipFrequency}", method = RequestMethod.GET)
	public ResponseEntity<?> allSIPLiveRecords(@PathVariable String schemeCode, @PathVariable String sipFrequency) throws FinexaBussinessException, CustomFinexaException {
		System.out.println("schemeCode "+schemeCode+" sipFrequency "+sipFrequency);
		try {
			MasterTransactSipLiveDTO masterTransactSipLiveDTO = masterTranactService.getAllSIPLiveRecords(schemeCode, sipFrequency);
			return new ResponseEntity<MasterTransactSipLiveDTO>(masterTransactSipLiveDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve Transact SIP Live details " +e.getMessage());
		}
	}
	
	
	
	
}
