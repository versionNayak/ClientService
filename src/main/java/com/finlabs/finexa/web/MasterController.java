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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.finlabs.finexa.dto.ClientFixedIncomeDTO;
import com.finlabs.finexa.dto.ClientSmallSavingsDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.FinexaBusinessSubmoduleDTO;
import com.finlabs.finexa.dto.FundSchemeIsinDTO;
import com.finlabs.finexa.dto.LookupAssetSubClassDTO;
import com.finlabs.finexa.dto.LookupTransactKYCTypeDTO;
import com.finlabs.finexa.dto.MasterAIFDTO;
import com.finlabs.finexa.dto.MasterAPYMonthlyPensionCorpusDTO;
import com.finlabs.finexa.dto.MasterCashDTO;
import com.finlabs.finexa.dto.MasterDirectEquityDTO;
import com.finlabs.finexa.dto.MasterDirectEquityForPCDTO;
import com.finlabs.finexa.dto.MasterEquityDailyPriceDTO;
import com.finlabs.finexa.dto.MasterFileMappingBODTO;
import com.finlabs.finexa.dto.MasterInsurancePolicyDTO;
import com.finlabs.finexa.dto.MasterKVPTermDTO;
import com.finlabs.finexa.dto.MasterMutualFundETFDTO;
import com.finlabs.finexa.dto.MasterPMSDTO;
import com.finlabs.finexa.dto.MasterProductClassificationDTO;
import com.finlabs.finexa.dto.MasterStateDTO;
import com.finlabs.finexa.dto.MasterTablesUploadHistoryDTO;
import com.finlabs.finexa.dto.MasterTransactAccountTypeDTO;
import com.finlabs.finexa.dto.MasterTransactBSEMFDematSchemeDTO;
import com.finlabs.finexa.dto.MasterTransactBSEMFPhysicalSchemeDTO;
import com.finlabs.finexa.dto.MasterTransactCommunicationModeDTO;
import com.finlabs.finexa.dto.MasterTransactCountryNationalityDTO;
import com.finlabs.finexa.dto.MasterTransactDivPayModeDTO;
import com.finlabs.finexa.dto.MasterTransactFatcaAddressTypeDTO;
import com.finlabs.finexa.dto.MasterTransactIdentificationTypeDTO;
import com.finlabs.finexa.dto.MasterTransactIncomeTypeDTO;
import com.finlabs.finexa.dto.MasterTransactOccupationCodeDTO;
import com.finlabs.finexa.dto.MasterTransactSourceOfWealthDTO;
import com.finlabs.finexa.dto.MasterTransactStateCodeDTO;
import com.finlabs.finexa.dto.MasterTransactTaxStatusTypeDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.dto.ViewClientUCCDetailsDTO;
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
import com.finlabs.finexa.util.FinexaConstant;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@RestController
public class MasterController {
	private static Logger log = LoggerFactory.getLogger(MasterController.class);
	@Resource(name = "exceptionmap")
	private Map<String, String> exceptionmap;
	@Autowired
	MasterService masterService;
	@Autowired
	MasterUploadHistoryService masterUploadHistoryService;
	@Autowired
	LookupAssetClassService lookupAssetClassService;
	@Autowired
	MasterMutualFundETFService masterMutualFundETFService;
	@Autowired
	LookupAssetAllocationCategoryService lookupAssetAllocationCategoryService;
	@Autowired
	LookupAssetAllocationService lookupAssetAllocationService;
	@Autowired
	LookupBucketLogicService lookupBucketLogicService;
	@Autowired
	LookupAssetAllocationCategoryRepository lookupAssetAllocationCategoryRepository;
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

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMasterMFETF", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterMFETF() throws FinexaBussinessException {
		try {
			List<MasterMutualFundETFDTO> masterMFETFDTOList = masterService.getAllMasterMutualFundETFDTO();
			return new ResponseEntity<List<MasterMutualFundETFDTO>>(masterMFETFDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException(FinexaConstant.MASTER_MUTUAL_FUND_ETF_MODULE,
					"Not Present At the moment", "Failed to show all.");
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMasterAPYMonthlyPensionCorpus", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterAPYMonthlyPensionCorpus() {
		List<MasterAPYMonthlyPensionCorpusDTO> masterAPYMonthlyPensionCorpusDTOList = masterService
				.getAllMasterAPYMonthlyPensionCorpusDTO();
		return new ResponseEntity<List<MasterAPYMonthlyPensionCorpusDTO>>(masterAPYMonthlyPensionCorpusDTOList,
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMasterMFETFByScheme/{schemeName}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterMFETFByScheme(@PathVariable String schemeName)
			throws FinexaBussinessException {

		try {
			List<MasterMutualFundETFDTO> list = masterService.getAllFromSchemes(schemeName);
			return new ResponseEntity<List<MasterMutualFundETFDTO>>(list, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_ALL_INFO_FROM_SCHEME_NAME_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_MUTUAL_FUND_ETF_MODULE,
					FinexaConstant.GET_ALL_INFO_FROM_SCHEME_NAME_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMasterMFETFByIsin/{isin}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterMFETFByIsin(@PathVariable String isin)
			throws FinexaBussinessException {

		try {
			List<MasterMutualFundETFDTO> list = masterService.getAllFromIsins(isin);
			return new ResponseEntity<List<MasterMutualFundETFDTO>>(list, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_ALL_INFO_FROM_SCHEME_NAME_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_MUTUAL_FUND_ETF_MODULE,
					FinexaConstant.GET_ALL_INFO_FROM_SCHEME_NAME_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMasterEquityDailyPriceByStockName/{stockName}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterEquityDailyPriceByStockName(@PathVariable String stockName) {

		List<MasterEquityDailyPriceDTO> list = masterService.getAllFromStockNames(stockName);
		return new ResponseEntity<List<MasterEquityDailyPriceDTO>>(list, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/EquitySearch/{stockName}", method = RequestMethod.GET)
	public ResponseEntity<?> equitySearchByStockName(@PathVariable String stockName) {

		List<MasterDirectEquityForPCDTO> list = masterService.equitySearchByStockName(stockName);
		return new ResponseEntity<List<MasterDirectEquityForPCDTO>>(list, HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMasterEquityDailyPriceByIsin/{stockName}/{isin}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterEquityDailyPriceByIsin(@PathVariable String stockName,
			@PathVariable String isin) {

		List<MasterEquityDailyPriceDTO> list = masterService.getAllFromStockNameAndIsin(stockName, isin);
		return new ResponseEntity<List<MasterEquityDailyPriceDTO>>(list, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/SchemeFromFund/{fundHouse}", method = RequestMethod.GET)
	public ResponseEntity<?> findSchemeFromFund(@PathVariable String fundHouse, String status) throws FinexaBussinessException {

		try {
			status = "Active";
			List<FundSchemeIsinDTO> listofSchemesIsin = masterService.getSchemesFromFund(fundHouse, status);
			return new ResponseEntity<List<FundSchemeIsinDTO>>(listofSchemesIsin, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_SCHEME_NAME_FROM_FUND_HOUSE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_MUTUAL_FUND_ETF_MODULE,
					FinexaConstant.GET_SCHEME_NAME_FROM_FUND_HOUSE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/SchemeFromFundwithPagination/{fundHouse}/{nextFetch}", method = RequestMethod.GET)
	public ResponseEntity<?> findSchemeFromFund(@PathVariable String fundHouse,@PathVariable int nextFetch) throws FinexaBussinessException {
		
		try {
			String status = "Active";
			Pageable pageable = new PageRequest(nextFetch, 10);
			List<FundSchemeIsinDTO> listofSchemesIsin = masterService.getSchemesFromFund(fundHouse, status, pageable);
			return new ResponseEntity<List<FundSchemeIsinDTO>>(listofSchemesIsin, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_SCHEME_NAME_FROM_FUND_HOUSE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_MUTUAL_FUND_ETF_MODULE,
					FinexaConstant.GET_SCHEME_NAME_FROM_FUND_HOUSE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/SchemeFromFundMatchString/{fundHouse}/{matchString}", method = RequestMethod.GET)
	public ResponseEntity<?> findSchemeFromFundMatchString(@PathVariable String fundHouse,@PathVariable String matchString) throws FinexaBussinessException {

		try {
			String status = "Active";
			List<FundSchemeIsinDTO> listofSchemesIsin = masterService.getSchemesFromFund(fundHouse, status, matchString);
			return new ResponseEntity<List<FundSchemeIsinDTO>>(listofSchemesIsin, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_SCHEME_NAME_FROM_FUND_HOUSE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_MUTUAL_FUND_ETF_MODULE,
					FinexaConstant.GET_SCHEME_NAME_FROM_FUND_HOUSE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getSchemeFromFund/{fundHouse}/{subAssetClassID}", method = RequestMethod.GET)
	public ResponseEntity<?> getSchemeFromFund(@PathVariable String fundHouse, @PathVariable byte subAssetClassID, String status) throws FinexaBussinessException {

		try {
			status = "Active";
			List<FundSchemeIsinDTO> listofSchemesIsin = masterService.getSchemesFromSelectedFund(fundHouse, subAssetClassID, status);
			return new ResponseEntity<List<FundSchemeIsinDTO>>(listofSchemesIsin, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_SCHEME_NAME_FROM_FUND_HOUSE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_MUTUAL_FUND_ETF_MODULE,
					FinexaConstant.GET_SCHEME_NAME_FROM_FUND_HOUSE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin')")
	@RequestMapping(value = "/subModuleFromModule", method = RequestMethod.GET)
	public ResponseEntity<?> findSubModuleFromModule(@RequestParam("moduleName") String moduleName) {
	 
		 List<FinexaBusinessSubmoduleDTO> listofSubModules = masterService.getSubModuleFromModule(moduleName); 
		 return new  ResponseEntity<List<FinexaBusinessSubmoduleDTO>>(listofSubModules, HttpStatus.OK); 
	}
	 
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllSubAssetClass", method = RequestMethod.GET)
	public ResponseEntity<?> findAllSubAssetClass() {

		List<LookupAssetSubClassDTO> lookupAssetSubClassDTOList = masterService.getAllLookupAssetSubClassDTO();
		return new ResponseEntity<List<LookupAssetSubClassDTO>>(lookupAssetSubClassDTOList, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMasterState", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterState() throws FinexaBussinessException {

		try {
			List<MasterStateDTO> masterStateDTOList = masterService.getAllMasterStateDTO();
			return new ResponseEntity<List<MasterStateDTO>>(masterStateDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.CLIENT_CONTACT_GET_STATE_DROPDOWN_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_STATE_MODULE,
					FinexaConstant.CLIENT_CONTACT_GET_STATE_DROPDOWN_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMasterCash", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterCash() throws FinexaBussinessException {

		try {
			List<MasterCashDTO> masterCashDTOList = masterService.getAllMasterCashDTO();
			return new ResponseEntity<List<MasterCashDTO>>(masterCashDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.MASTER_CASH_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_CASH_MODULE, FinexaConstant.MASTER_CASH_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMasterProductClassification", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterProductClassification() {

		List<MasterProductClassificationDTO> masterProductClassificationDTOList = masterService
				.getAllMasterProductClassificationDTO();
		return new ResponseEntity<List<MasterProductClassificationDTO>>(masterProductClassificationDTOList,
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMasterDirectEquity", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterDirectEquity() {

		List<MasterDirectEquityDTO> masterDirectEquityDTOList = masterService.getAllMasterDirectEquityDTO();
		return new ResponseEntity<List<MasterDirectEquityDTO>>(masterDirectEquityDTOList, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllIsinForEquityCalculator", method = RequestMethod.GET)
	public ResponseEntity<?> findAllIsinForEquityCalculator() {

		List<String> listIsin = masterService.getAllIsinFromMasterDirectEquity();
		return new ResponseEntity<List<String>>(listIsin, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMasterEquityDailyPrice", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterEquityDailyPrice() {

		List<MasterEquityDailyPriceDTO> masterEquityDailyPriceDTOList = masterService.getAllMasterEquityDailyPriceDTO();
		return new ResponseEntity<List<MasterEquityDailyPriceDTO>>(masterEquityDailyPriceDTOList, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/fundHouseList", method = RequestMethod.GET)
	public ResponseEntity<?> findAllFundHouseList(String status) throws FinexaBussinessException {
		try {
			status = "Active";
			List<String> listFundHouse = masterService.getAllFundHouseList(status);
			return new ResponseEntity<List<String>>(listFundHouse, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_FUND_HOUSE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_MUTUAL_FUND_ETF_MODULE,
					FinexaConstant.GET_FUND_HOUSE_ERROR, exception != null ? exception.getErrorMessage() : "", rexp);
		}

	}
	
	//commented code for pagination, don't remove
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/fundHouseListWithPagination/{nextFetch}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllFundHouseListWithPagination(@PathVariable int nextFetch, String status) throws FinexaBussinessException {
		try {
			System.out.println("nextFetch "+ nextFetch);
			Pageable pageable = new PageRequest(nextFetch, 10);
			
			status = "Active";
			List<String> listFundHouse = masterService.getAllFundHouseList(pageable, status);
			return new ResponseEntity<List<String>>(listFundHouse, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_FUND_HOUSE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_MUTUAL_FUND_ETF_MODULE,
					FinexaConstant.GET_FUND_HOUSE_ERROR, exception != null ? exception.getErrorMessage() : "", rexp);
		}

	}
	
//-------------------------------------------------------------------------------------------------------------------
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/searchHouseListDynamically/{matchString}", method = RequestMethod.GET)
	public ResponseEntity<?> searchHouseListDynamically(@PathVariable String matchString) throws FinexaBussinessException {
		try {
			
			String status = "Active";
			List<String> listFundHouse = masterService.getAllFundHouseList(status, matchString);
			return new ResponseEntity<List<String>>(listFundHouse, HttpStatus.OK);
			
			
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
	
//------------------------------------------------------------------------------------------------------------------	
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/fundHouseListForSubAsset/{subClassAssetId}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllFundHouseListBySubAsset(@PathVariable byte subClassAssetId) throws FinexaBussinessException {
		try {
			List<String> listFundHouse = masterService.getAllFundHouseListBySubAsset(subClassAssetId);
			return new ResponseEntity<List<String>>(listFundHouse, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_FUND_HOUSE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_MUTUAL_FUND_ETF_MODULE,
					FinexaConstant.GET_FUND_HOUSE_ERROR, exception != null ? exception.getErrorMessage() : "", rexp);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/subAssetClassList", method = RequestMethod.GET)
	public ResponseEntity<?> findAllSubAssetClassList() {

		List<String> listSubAssetClass = masterService.getAllDescriptionList();
		return new ResponseEntity<List<String>>(listSubAssetClass, HttpStatus.OK);

	}

	 @PreAuthorize("hasAnyRole('Admin')")
	 @RequestMapping(value = "/finexaModuleList", method = RequestMethod.GET)
	 public ResponseEntity<?> findAllFinexaModuleList() {
	  
		 List<String> listModules = masterService.getAllModuleDescriptionList();
		 return new ResponseEntity<List<String>>(listModules, HttpStatus.OK); 
	 }
	 
	 @PreAuthorize("hasAnyRole('Admin')")
	 @RequestMapping(value = "/functionFromSubmodule", method = RequestMethod.GET)
	 public ResponseEntity<?> findFunctionFromSubmodule(@RequestParam("subModuleID") int subModuleID) {
	 
		 List<String> listFunctions = masterService.getFunctionFromSubmodule(subModuleID); 
		 return new ResponseEntity<List<String>>(listFunctions, HttpStatus.OK); 
	 }
	 
	
	@PreAuthorize("hasAnyRole('Admin')")
	@RequestMapping(value = "/eventFromSubFunction", method = RequestMethod.GET)
	public ResponseEntity<?> findEventFromSubFunction(@RequestParam("subFunction") String subFunction) {

		List<String> listEvents = masterService.getEventFromSubFunction(subFunction);
		return new ResponseEntity<List<String>>(listEvents, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin')")
	@RequestMapping(value = "/subEventFromEvent", method = RequestMethod.GET)
	public ResponseEntity<?> findSubEventByEvent(@RequestParam("event") String event) {

		List<String> listSubEvents = masterService.getSubEventFromEvent(event);
		return new ResponseEntity<List<String>>(listSubEvents, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin')")
	@RequestMapping(value = "/subFunctionFromFunction", method = RequestMethod.GET)
	public ResponseEntity<?> findSubFunctionByFunction(@RequestParam("function") String function) {

		List<String> listSubFunctions = masterService.getSubFunctionFromFunction(function);
		return new ResponseEntity<List<String>>(listSubFunctions, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getNSCInterestRateFromStartDate", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterNSCInterestRate()
			throws FinexaBussinessException, CustomFinexaException {
		try {
			BigDecimal masterNSC = masterService.getNSCInterestRateFromStartDate(new Date());
			log.debug("getNSCInterestRateFromStartDate Controller >> Interest Rate Found");
			return new ResponseEntity<BigDecimal>(masterNSC, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MASTER_NSC_INTEREST_RATE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_NSC_INTEREST_RATE_MODULE,
					FinexaConstant.MASTER_NSC_INTEREST_RATE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", rexp);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getKVPInterestRateFromStartDate", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterKVPInterestRate()
			throws FinexaBussinessException, CustomFinexaException {

		try {
			BigDecimal masterKVP = masterService.getKVPInterestRateFromStartDate(new Date());
			return new ResponseEntity<BigDecimal>(masterKVP, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MASTER_KVP_INTEREST_RATE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_KVP_INTEREST_RATE_MODULE,
					FinexaConstant.MASTER_KVP_INTEREST_RATE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getKVPCompoundingFrequencyFromStartDate", method = RequestMethod.POST)
	public ResponseEntity<?> findAllMasterKVPCompoundingFrequency(@RequestBody ClientSmallSavingsDTO clientSSDTO)
			throws FinexaBussinessException, CustomFinexaException {

		try {
			Integer masterKVP = masterService.getKVPCompoundingFrequencyFromStartDate(clientSSDTO.getStartDate());
			return new ResponseEntity<Integer>(masterKVP, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MASTER_KVP_COMPOUNDING_FREQUENCY_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_KVP_COMPOUNDING_FREQUENCY_MODULE,
					FinexaConstant.MASTER_KVP_COMPOUNDING_FREQUENCY_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getKVPTermFromStartDate", method = RequestMethod.POST)
	public ResponseEntity<?> findAllMasterKVPTerm(@RequestBody ClientSmallSavingsDTO clientSSDTO)
			throws FinexaBussinessException, CustomFinexaException {

		try {
			MasterKVPTermDTO masterKVP = masterService.getKVPTermFromStartDate(clientSSDTO.getStartDate());
			return new ResponseEntity<MasterKVPTermDTO>(masterKVP, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.MASTER_KVP_TERM_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_KVP_TERM_MODULE,
					FinexaConstant.MASTER_KVP_TERM_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getPOMISInterestRateFromStartDate", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterPOMISInterestRate()
			throws FinexaBussinessException, CustomFinexaException {

		try {
			BigDecimal masterPOMIS = masterService.getPOMISInterestRateFromStartDate(new Date());
			return new ResponseEntity<BigDecimal>(masterPOMIS, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.MASTER_POMIS_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_POMIS_MODULE,
					FinexaConstant.MASTER_POMIS_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getSCSSInterestRateFromStartDate", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterSCSSInterestRate()
			throws FinexaBussinessException, CustomFinexaException {

		try {
			BigDecimal masterSCSS = masterService.getSCSSInterestRateFromStartDate(new Date());
			return new ResponseEntity<BigDecimal>(masterSCSS, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MASTER_SCSS_INTEREST_RATE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_SCSS_INTEREST_RATE_MODULE,
					FinexaConstant.MASTER_SCSS_INTEREST_RATE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getPORDInterestRateFromStartDate", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterPORDInterestRate()
			throws RuntimeException, CustomFinexaException, FinexaBussinessException {

		try {
			BigDecimal masterPORD = masterService.getPORDInterestRateFromStartDate(new Date());
			return new ResponseEntity<BigDecimal>(masterPORD, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.MASTER_PORD_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_PORD_MODULE, FinexaConstant.MASTER_PORD_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getSukanyaInterestRateFromStartDate", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMasterSukanyaInterstRateFromStartDate()
			throws RuntimeException, CustomFinexaException, FinexaBussinessException {

		try {
			BigDecimal masterSukanya = masterService.getSukanyaInterestRateFromStartDate(new Date());
			return new ResponseEntity<BigDecimal>(masterSukanya, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MASTER_SUKANYA_SAMRIDDHI_INTEREST_RATE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_SUKANYA_SAMRIDDHI_INTEREST_RATE_MODULE,
					FinexaConstant.MASTER_SUKANYA_SAMRIDDHI_INTEREST_RATE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/CloseEndedFlagFromSchemes/{schemeName}", method = RequestMethod.GET)
	public ResponseEntity<?> findCloseEndedFlagsFromSchemes(@PathVariable String schemeName) {

		Set<String> listofFlags = masterService.getCloseEndedFlagFromScheme(schemeName);
		return new ResponseEntity<Set<String>>(listofFlags, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/SecurityNameFromIsin/{isin}", method = RequestMethod.GET)
	public ResponseEntity<?> findSecurityNameByIsin(@PathVariable String isin) {

		Set<String> listofNames = masterService.getStockNameFromIsin(isin);
		return new ResponseEntity<Set<String>>(listofNames, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/insuranceCompanyByType", method = RequestMethod.GET)
	public ResponseEntity<?> getinsuranceCompanyByType(@RequestParam byte insTypeid,
			@RequestParam byte insurancePolicyTypeId) {

		List<MasterInsurancePolicyDTO> listMasterInsurancePolicyDTO = masterService.getInsuranceCompanyByType(insTypeid,
				insurancePolicyTypeId);
		return new ResponseEntity<List<MasterInsurancePolicyDTO>>(listMasterInsurancePolicyDTO, HttpStatus.OK);

	}

	/*
	 * @RequestMapping(value="/master/createUploadMaster",
	 * method=RequestMethod.POST) public ResponseEntity<?>
	 * createUploadMaster(@RequestBody MasterTablesUploadHistoryDTO
	 * masterTablesUploadHistoryDTO) {
	 * 
	 * log.debug("MasterController createUploadMaster(): " +
	 * masterTablesUploadHistoryDTO);
	 * 
	 * masterTablesUploadHistoryDTO =
	 * masterService.save(masterTablesUploadHistoryDTO);
	 * 
	 * return new ResponseEntity<MasterTablesUploadHistoryDTO>(
	 * masterTablesUploadHistoryDTO, HttpStatus.OK); }
	 */

	@PreAuthorize("hasAnyRole('Admin','mastersDelete')")
	@RequestMapping(value = "/deleteUploadedMaster/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteUploadedMaster(@PathVariable int id) {

		int i = masterService.delete(id);
		return new ResponseEntity<Integer>(i, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','MastersAddEdit')")
	@RequestMapping(value = "/masters/{masterName}/download", method = RequestMethod.GET)
	public ResponseEntity<?> downloadMaster(@PathVariable String masterName, HttpServletResponse response) {
		log.debug("masterName in downloadMaster: " + masterName);
		if (masterName.equals(FinexaConstant.LOOKUP_ASSET_CLASS)) {
			lookupAssetClassService.downloadLookupAssetClass(masterName, response);
		} else if (masterName.equals(FinexaConstant.MASTER_MUTUAL_FUND_ETF)) {
			masterMutualFundETFService.downloadMasterMutualFundETF(masterName, response);
		} else if (masterName.equals(FinexaConstant.LOOKUP_ASSET_ALLOCATION_CATEGORY)) {
			lookupAssetAllocationCategoryService.downloadLookupAssetAllocationCategory(masterName, response);
		} else if (masterName.equals(FinexaConstant.LOOKUP_ASSET_ALLOCATION)) {
			lookupAssetAllocationService.downloadLookupAssetAllocation(masterName, response);
		} else if (masterName.equals(FinexaConstant.LOOKUP_BUCKET_LOGIC)) {
			lookupBucketLogicService.downloadLookupBucketLogic(masterName, response);
		}
		return null;
	}

	@PreAuthorize("hasAnyRole('Admin','MastersAddEdit')")
	@RequestMapping(value = "/masters/uploadHistory/{loggedId}", method = RequestMethod.GET)
	public ResponseEntity<?> uploadHistory(@PathVariable int loggedId) throws FinexaBussinessException {
		try {
			List<MasterTablesUploadHistoryDTO> masterTablesUploadHistoryDTOList = masterUploadHistoryService
					.findByLoggedInId(loggedId);
			return new ResponseEntity<List<MasterTablesUploadHistoryDTO>>(masterTablesUploadHistoryDTOList,
					HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_MASTERS_OTHER_MASTERS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_MODULE,
					FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','MastersAddEdit')")
	@RequestMapping(value = "/masters/{masterName}/upload", method = RequestMethod.POST)
	public ResponseEntity<?> uploadMaster(@PathVariable String masterName, @ModelAttribute FileuploadDTO fileuploadDTO)
			throws FinexaBussinessException, BiffException, IOException {
		log.debug("masterName in uploadMaster: " + masterName);
		log.debug("original file name in uploadMaster: " + fileuploadDTO.getFile()[0].getOriginalFilename());
		String masterNameExtxls = masterName + ".xls";
		String masterNameExtcsv = masterName + ".csv";
		log.debug("masterNameExtxls: " + masterNameExtxls);
		log.debug("masterNameExtcsv: " + masterNameExtcsv);
		UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
		if (fileuploadDTO.getFile()[0].getOriginalFilename().contains(".xls")) {
			/*
			 * //lookupAssetClass if
			 * (masterNameExtxls.equals(fileuploadDTO.getFile()[0].
			 * getOriginalFilename())){ uploadResponseDTO =
			 * lookupAssetClassService.uploadLookupAssetClass(fileuploadDTO); }
			 * else { uploadResponseDTO.getErrors().add("File mismatch"); }
			 * //masterMutualFundETF if
			 * (masterNameExtxls.equals(fileuploadDTO.getFile()[0].
			 * getOriginalFilename())){ uploadResponseDTO =
			 * masterMutualFundETFService.uploadMasterMutualFundETF(
			 * fileuploadDTO); } else {
			 * uploadResponseDTO.getErrors().add("File mismatch"); }
			 */
			try {
				if (masterNameExtxls.equals(fileuploadDTO.getFile()[0].getOriginalFilename())) {
					if (masterName.equals(FinexaConstant.LOOKUP_ASSET_ALLOCATION_CATEGORY)) {
						uploadResponseDTO = lookupAssetAllocationCategoryService
								.uploadLookupAssetAllocationCategory(fileuploadDTO);
					} else if (masterName.equals(FinexaConstant.LOOKUP_ASSET_ALLOCATION)) {
						uploadResponseDTO = lookupAssetAllocationService.uploadLookupAssetAllocation(fileuploadDTO);
					} else if (masterName.equals(FinexaConstant.LOOKUP_BUCKET_LOGIC)) {
						uploadResponseDTO = lookupBucketLogicService.uploadLookupBucketLogic(fileuploadDTO);
					}
				} else {
					uploadResponseDTO.getErrors().add("File mismatch");
				}
				return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
			} catch (RuntimeException | BiffException | IOException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_BUSINESS_MASTERS_OTHER_MASTERS);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_UPLOAD_EXCEL_ERROR);
				throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_MODULE,
						FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_UPLOAD_EXCEL_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}

		} else if (fileuploadDTO.getFile()[0].getOriginalFilename().contains(".csv")) {
			try {
				if (masterNameExtcsv.equals(fileuploadDTO.getFile()[0].getOriginalFilename())) {
					if (masterName.equals(FinexaConstant.LOOKUP_ASSET_ALLOCATION_CATEGORY)) {
						if (validateLookupAssetAllocationCategoryCSV(fileuploadDTO, uploadResponseDTO)) {
							uploadResponseDTO = lookupAssetAllocationCategoryService
									.uploadLookupAssetAllocationCategoryCSV(fileuploadDTO);
						}
					} else if (masterName.equals(FinexaConstant.LOOKUP_ASSET_ALLOCATION)) {
						if (validateLookupAssetAllocationCSV(fileuploadDTO, uploadResponseDTO)) {
							uploadResponseDTO = lookupAssetAllocationService
									.uploadLookupAssetAllocationCSV(fileuploadDTO);
						}
					} else if (masterName.equals(FinexaConstant.LOOKUP_BUCKET_LOGIC)) {
						if (validateLookupBucketLogicCSV(fileuploadDTO, uploadResponseDTO)) {
							uploadResponseDTO = lookupBucketLogicService.uploadLookupBucketLogicCSV(fileuploadDTO);
						}
					}

				} else {
					uploadResponseDTO.getErrors().add("File mismatch");
				}
				return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_BUSINESS_MASTERS_OTHER_MASTERS);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_UPLOAD_CSV_ERROR);
				throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_MODULE,
						FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_UPLOAD_CSV_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		} else {
			uploadResponseDTO.getErrors().add("File Format Mismatch");
		}

		return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
	}

	//@PreAuthorize("hasAnyRole('Admin','Masters')")
	private boolean validateLookupAssetAllocationCategoryCSV(FileuploadDTO fileuploadDTO,
			UploadResponseDTO uploadResponseDTO) {

		log.debug("inside validateLookupAssetAllocationCategory csv");
		String line = "";
		long rowNum = 0;
		String cvsSplitBy = ",";
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream()))) {

			if (new BufferedReader(new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream())).lines()
					.count() < 2) {
				uploadResponseDTO.getErrors().add("No Data in CSV file");
			}

			while ((line = br.readLine()) != null) {
				if (rowNum == 0) {
					rowNum++;
					continue;
				}
				rowNum++;
				// log.debug("rowNum= : " + rowNum);
				long displayRow = rowNum;

				String[] fields = line.split(cvsSplitBy);
				log.debug("fields: " + fields.length);
				log.debug("fields[0]: " + fields[0]);
				// ID
				if (StringUtils.isEmpty(fields[0])) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", A ) : " + "ID should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(fields[0])) {
						if (!StringUtils.isNumeric(fields[0])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", A) : " + "ID should be numeric");
						}
					}
				}
				// description
				if (fields[1].isEmpty() == true) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", B ) : " + "description should not be Empty");
				} else {
					// log.debug("fields[1].length(): " +fields[1].length());
					if (fields.length == 2 && fields[1].isEmpty() == false) {
						log.debug("fields[1]: " + fields[1]);
						log.debug("isAlphanumericSpace: " + StringUtils.isAlphanumericSpace(fields[1]));
						if (!StringUtils.isAlphanumericSpace(fields[1]) || StringUtils.isNumeric(fields[1])) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", B ) : "
									+ "description should contain alphabets and numbers");
						}
					}
				}
				continue;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		log.debug("error size : " + uploadResponseDTO.getErrors().size());
		return (uploadResponseDTO.getErrors().size() > 0) ? false : true;

	}
	
	//@PreAuthorize("hasAnyRole('Admin','Masters')")
	private boolean validateLookupAssetAllocationCSV(FileuploadDTO fileuploadDTO, UploadResponseDTO uploadResponseDTO) {

		log.debug("inside validateLookupAssetAllocationCSV csv");
		String line = "";
		long rowNum = 0;
		String cvsSplitBy = ",";
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream()))) {

			if (new BufferedReader(new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream())).lines()
					.count() < 2) {
				uploadResponseDTO.getErrors().add("No Data in CSV file");
			}

			while ((line = br.readLine()) != null) {
				if (rowNum == 0) {
					rowNum++;
					continue;
				}
				rowNum++;
				// log.debug("rowNum= : " + rowNum);
				long displayRow = rowNum;
				String[] fields = line.split(cvsSplitBy);
				log.debug("fields: " + fields.length);
				log.debug("fields[0]: " + fields[0]);
				// ID
				if (StringUtils.isEmpty(fields[0])) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", A ) : " + "ID should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(fields[0])) {
						if (!StringUtils.isNumeric(fields[0])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", A) : " + "ID should be numeric");
						}
					}
				}
				// assetAllocationCategory
				if (fields[1].isEmpty() == true) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", B ) : " + "assetAllocationCategory should not be Empty");
				} else {
					log.debug("fields[1].isEmpty(): " + fields[1].isEmpty());
					if (fields.length == 4 && fields[1].isEmpty() == false) {
						if (!StringUtils.isAlphanumeric(fields[1]) || StringUtils.isNumeric(fields[1])
								|| StringUtils.isNumericSpace(fields[1])) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", B ) : "
									+ "assetAllocationCategory should contain alphabets and numbers");
						} else {
							if (StringUtils.isAlphanumeric(fields[1]) && !StringUtils.isNumeric(fields[1])
									&& !StringUtils.isNumericSpace(fields[1])) {
								LookupAssetAllocationCategory lookupAssetAllocationCategory = lookupAssetAllocationCategoryRepository
										.findByDescription(fields[1]);
								if (lookupAssetAllocationCategory == null) {
									uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", B ) : "
											+ "assetAllocationCategory is not valid");
								}
							}
						}
					}
				}
				// assetSubClass
				if (fields[2].isEmpty() == true) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", C ) : " + "assetSubClass should not be Empty");
				} else {
					if (fields.length == 4 && fields[2].isEmpty() == false) {
						if (!StringUtils.isAsciiPrintable(fields[2])) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", C ) : "
									+ "assetSubClass may contain alphabets, characters and numbers");
						} else {
							if (StringUtils.isAsciiPrintable(fields[2])) {
								LookupAssetSubClass lookupAssetSubClass = lookupAssetSubClassRepository
										.findByDescription(fields[2]);
								if (lookupAssetSubClass == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", C ) : " + "assetSubClass is not valid");
								}
							}
						}
					}
				}
				// weightage
				if (fields[3].isEmpty() == true) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", D ) : " + "weightage should not be Empty");
				} else {
					if (StringUtils.isAlpha(fields[3]) || StringUtils.isAlphanumeric(fields[3])
							|| StringUtils.isAlphaSpace(fields[3]) || StringUtils.isAlphanumericSpace(fields[3])) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", D ) : " + "weightage must be decimal");
					}
				}
				// fromDate
				if (fields[4].isEmpty() == true) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", E ) : " + "fromDate should not be Empty");
				} else {
					if (!StringUtils.containsAny(fields[4], "-") || StringUtils.isAlphaSpace(fields[4])
							|| StringUtils.isAlphanumericSpace(fields[4])) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", E ) : " + "fromDate must be of yyyy-MM-dd format");
					}
				}
				// toDate
				if (fields[5].isEmpty() == true) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", F ) : " + "toDate should not be Empty");
				} else {
					if (!StringUtils.containsAny(fields[5], "-") || StringUtils.isAlphaSpace(fields[5])
							|| StringUtils.isAlphanumericSpace(fields[5])) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", F ) : " + "toDate must be of yyyy-MM-dd format");
					}
				}

				continue;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		log.debug("error size : " + uploadResponseDTO.getErrors().size());
		return (uploadResponseDTO.getErrors().size() > 0) ? false : true;
	}
	
	//@PreAuthorize("hasAnyRole('Admin','Masters')")
	private boolean validateLookupBucketLogicCSV(FileuploadDTO fileuploadDTO, UploadResponseDTO uploadResponseDTO) {

		log.debug("inside validateLookupBucketLogicCSV csv");
		String line = "";
		long rowNum = 0;
		String cvsSplitBy = ",";
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream()))) {

			if (new BufferedReader(new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream())).lines()
					.count() < 2) {
				uploadResponseDTO.getErrors().add("No Data in CSV file");
			}

			while ((line = br.readLine()) != null) {
				if (rowNum == 0) {
					rowNum++;
					continue;
				}
				rowNum++;
				// log.debug("rowNum= : " + rowNum);
				long displayRow = rowNum;
				String[] fields = line.split(cvsSplitBy);
				log.debug("fields: " + fields.length);
				log.debug("fields[0]: " + fields[0]);
				// ID
				if (StringUtils.isEmpty(fields[0])) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", A ) : " + "ID should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(fields[0])) {
						if (!StringUtils.isNumeric(fields[0])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", A) :" + "ID should be numeric");
						}
					}
				}
				// riskProfile
				if (fields[1].isEmpty() == true) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", B ) : " + "riskProfile should not be Empty");
				} else {
					if (fields.length == 4 && fields[1].isEmpty() == false) {
						if (!StringUtils.isAlphaSpace(fields[1]) || StringUtils.isNumeric(fields[1])
								|| StringUtils.isNumericSpace(fields[1])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", B ) : " + "riskProfile should contain alphabets");
						} else {
							if (StringUtils.isAlphaSpace(fields[1]) && !StringUtils.isNumeric(fields[1])
									&& !StringUtils.isNumericSpace(fields[1])) {
								LookupRiskProfile lookupRiskProfile = lookupRiskProfileRepository
										.findByDescription(fields[1]);
								if (lookupRiskProfile == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", B ) : " + "riskProfile is not valid");
								}
							}
						}
					}
				}
				// goalHorizonBucket
				if (fields[2].isEmpty() == true) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", C ) : " + "goalHorizonBucket should not be Empty");
				} else {
					if (fields[2].isEmpty() == false) {
						if (!StringUtils.isNumeric(fields[2])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", C ) : " + "goalHorizonBucket should be in numbers");
						} else {
							/*if (StringUtils.isNumeric(fields[2])) {
								LookupGoalHorizonBucket lookupGoalHorizonBucket = lookupGoalHorizonBucketRepository
										.findById(Integer.parseInt(fields[2]));
								if (lookupGoalHorizonBucket == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", C ) : " + "goalHorizonBucket is not valid");
								}
							}*/
						}
					}
				}
				// assetAllocationCategory
				if (fields[3].isEmpty() == true) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", D ) : " + "assetAllocationCategory should not be Empty");
				} else {
					if (fields[3].isEmpty() == false) {
						if (!StringUtils.isAlphanumeric(fields[3]) || StringUtils.isNumeric(fields[3])
								|| StringUtils.isNumericSpace(fields[3])) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D ) : "
									+ "assetAllocationCategory should contain alphabets and numbers");
						} else {
							if (StringUtils.isAlphanumeric(fields[3]) && !StringUtils.isNumeric(fields[3])
									&& !StringUtils.isNumericSpace(fields[3])) {
								LookupAssetAllocationCategory ookupAssetAllocationCategory = lookupAssetAllocationCategoryRepository
										.findByDescription(fields[3].toUpperCase());
								if (ookupAssetAllocationCategory == null) {
									uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D ) : "
											+ "assetAllocationCategory is not valid");
								}
							}
						}
					}
				}

				continue;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		log.debug("error size : " + uploadResponseDTO.getErrors().size());
		return (uploadResponseDTO.getErrors().size() > 0) ? false : true;
	}

	@PreAuthorize("hasAnyRole('Admin','MastersView')")
	@RequestMapping(value = "/masters/{masterName}/{fileType}/downloadTemplate", method = RequestMethod.GET)
	public ResponseEntity<?> downloadMasterTemplate(@PathVariable String masterName, @PathVariable String fileType,
			HttpServletResponse response) throws IOException, FinexaBussinessException, CustomFinexaException {

		log.debug("masterName in downloadMasterTemplate: " + masterName);
		if (fileType.equalsIgnoreCase(FinexaConstant.FILE_TYPE_EXCEL)) {
			try {
				if (masterName.equals(FinexaConstant.LOOKUP_ASSET_CLASS)) {
					lookupAssetClassService.downloadLookupAssetClassTemplate(masterName, response);
				} else if (masterName.equals(FinexaConstant.MASTER_MUTUAL_FUND_ETF)) {
					masterMutualFundETFService.downloadMasterMutualFundETFTemplate(masterName, response);
				} else if (masterName.equals(FinexaConstant.LOOKUP_ASSET_ALLOCATION_CATEGORY)) {
					log.debug(masterName);
					lookupAssetAllocationCategoryService.downloadLookupAssetAllocationCategoryTemplate(masterName,
							response);
				} else if (masterName.equals(FinexaConstant.LOOKUP_ASSET_ALLOCATION)) {
					log.debug(masterName);
					lookupAssetAllocationService.downloadLookupAssetAllocationTemplate(masterName, response);
				} else if (masterName.equals(FinexaConstant.LOOKUP_BUCKET_LOGIC)) {
					log.debug(masterName);
					lookupBucketLogicService.downloadLookupBucketLogicTemplate(masterName, response);
				}
			} catch (RuntimeException | IOException | WriteException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_BUSINESS_MASTERS_OTHER_MASTERS);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_DOWNLOAD_EXCEL_TEMPLATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_MODULE,
						FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_DOWNLOAD_EXCEL_TEMPLATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		} else if (fileType.equalsIgnoreCase(FinexaConstant.FILE_TYPE_CSV)) {
			try {
				if (masterName.equals(FinexaConstant.LOOKUP_ASSET_ALLOCATION_CATEGORY)) {
					log.debug(masterName);
					lookupAssetAllocationCategoryService.downloadLookupAssetAllocationCategoryTemplateCSV(response);
				} else if (masterName.equals(FinexaConstant.LOOKUP_ASSET_ALLOCATION)) {
					log.debug(masterName);
					lookupAssetAllocationService.downloadLookupAssetAllocationTemplateCSV(response);
				} else if (masterName.equals(FinexaConstant.LOOKUP_BUCKET_LOGIC)) {
					log.debug(masterName);
					lookupBucketLogicService.downloadLookupBucketLogicTemplateCSV(response);
				}
			} catch (RuntimeException | IOException e) {
				// TODO Auto-generated catch block
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_BUSINESS_MASTERS_OTHER_MASTERS);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_DOWNLOAD_CSV_TEMPLATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_MODULE,
						FinexaConstant.MY_BUSINESS_UPLOAD_MASTERS_DOWNLOAD_CSV_TEMPLATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}

		return null;
	}
	
	@PreAuthorize("hasAnyRole('Admin','MastersView')")
	@RequestMapping(value = "/masters/{errorInput}/downloadErrorLog/{selectedMaster}", method = RequestMethod.GET)
	public ResponseEntity<?> downloadErrorLog(@PathVariable String errorInput, @PathVariable String selectedMaster, HttpServletResponse response) {
		masterService.downloadErrorLog(errorInput, selectedMaster, response);
		return null;
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getAssetForMaturityRenewal", method = RequestMethod.GET)
	public ResponseEntity<?> getAssetForMaturityRenewal() throws ParseException {

		// int i=111;
		List<MasterProductClassificationDTO> masterProductClassificationDTO = masterService
				.getAssetForMaturityRenewal();
		return new ResponseEntity<List<MasterProductClassificationDTO>>(masterProductClassificationDTO, HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactAccountType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllTransactAccountType() throws FinexaBussinessException {

		try {
			List<MasterTransactAccountTypeDTO> masterTransactAccountTypeDTOList = masterService.getAllTransactAccountType();
			return new ResponseEntity<List<MasterTransactAccountTypeDTO>>(masterTransactAccountTypeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All the Account Types " +e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactTaxStatusType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllTransactTaxStatusType() throws FinexaBussinessException {

		try {
			List<MasterTransactTaxStatusTypeDTO> masterTransactTaxStatusTypeDTOList = masterService.getAllTransactTaxStatusType();
			return new ResponseEntity<List<MasterTransactTaxStatusTypeDTO>>(masterTransactTaxStatusTypeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All the Account Types " +e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactStateCode", method = RequestMethod.GET)
	public ResponseEntity<?> findAllTransactStateCode() throws FinexaBussinessException {

		try {
			List<MasterTransactStateCodeDTO> masterTransactStateCodeDTOList = masterService.getAllTransactStateCode();
			return new ResponseEntity<List<MasterTransactStateCodeDTO>>(masterTransactStateCodeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All the State Code " +e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactOccupationCode", method = RequestMethod.GET)
	public ResponseEntity<?> findAllTransactOccupationCode() throws FinexaBussinessException {

		try {
			List<MasterTransactOccupationCodeDTO> masterTransactOccupationCodeDTOList = masterService.getAllTransactOccupationCode();
			return new ResponseEntity<List<MasterTransactOccupationCodeDTO>>(masterTransactOccupationCodeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All the Occupation Code " +e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactDivPayMode", method = RequestMethod.GET)
	public ResponseEntity<?> findAllTransactDivPayMode() throws FinexaBussinessException {

		try {
			List<MasterTransactDivPayModeDTO> masterTransactDivPayModeDTOList = masterService.getAllTransactDivPayMode();
			return new ResponseEntity<List<MasterTransactDivPayModeDTO>>(masterTransactDivPayModeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All the DivPay Mode " +e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactCommunicationMode", method = RequestMethod.GET)
	public ResponseEntity<?> findAllTransactCommunicationMode() throws FinexaBussinessException {

		try {
			List<MasterTransactCommunicationModeDTO> masterTransactCommunicationModeDTOList = masterService.getAllTransactCommunicationMode();
			return new ResponseEntity<List<MasterTransactCommunicationModeDTO>>(masterTransactCommunicationModeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All the Communication Mode " +e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactLastAddressType", method = RequestMethod.GET)
	public ResponseEntity<?> AllTransactLastAddressType() throws FinexaBussinessException {

		try {
			List<MasterTransactFatcaAddressTypeDTO> masterTransactFatcaAddressTypeDTO = masterService.getAllTransactAddressType();
			return new ResponseEntity<List<MasterTransactFatcaAddressTypeDTO>>(masterTransactFatcaAddressTypeDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All the Address Type " +e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactIncomeType", method = RequestMethod.GET)
	public ResponseEntity<?> AllTransactIncomeType() throws FinexaBussinessException {

		try {
			List<MasterTransactIncomeTypeDTO> masterTransactIncomeTypeDTO = masterService.getAllTransactIncomeSlabType();
			return new ResponseEntity<List<MasterTransactIncomeTypeDTO>>(masterTransactIncomeTypeDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All Income Type " +e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactCountryNationality", method = RequestMethod.GET)
	public ResponseEntity<?> AllTransactCountryNationality() throws FinexaBussinessException {

		try {
			List<MasterTransactCountryNationalityDTO> masterTransactCountryNationalityDTO = masterService.getAllTransactCountryNationality();
			return new ResponseEntity<List<MasterTransactCountryNationalityDTO>>(masterTransactCountryNationalityDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All the Country Nationality " +e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactSourceOfWealth", method = RequestMethod.GET)
	public ResponseEntity<?> AllTransactSourceOfWealth() throws FinexaBussinessException {

		try {
			List<MasterTransactSourceOfWealthDTO> masterTransactSourceOfWealthDTO = masterService.getAllTransactSourceOfWealth();
			return new ResponseEntity<List<MasterTransactSourceOfWealthDTO>>(masterTransactSourceOfWealthDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve Source Of Wealth " +e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactIdentificationType", method = RequestMethod.GET)
	public ResponseEntity<?> AllTransactIdentificationType() throws FinexaBussinessException {
		
		try {
			List<MasterTransactIdentificationTypeDTO> masterTransactIdentificationTypeDTOList = masterService.getAllTransactIdentificationTypes();
			return new ResponseEntity<List<MasterTransactIdentificationTypeDTO>>(masterTransactIdentificationTypeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve Identification Type " +e.getMessage());
		}
		
	}
	
	/*@RequestMapping(value = "/AllTransactBSEMFDematSchemes", method = RequestMethod.GET)
	public ResponseEntity<?> AllTransactBSEMFDematSchemes() throws FinexaBussinessException {
		
		try {
			List<MasterTransactBSEMFDematSchemeDTO> masterTransactBSEMFDematSchemeDTOList = masterService.getAllBSEMFDematSchemes();
			return new ResponseEntity<List<MasterTransactBSEMFDematSchemeDTO>>(masterTransactBSEMFDematSchemeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve Transact BSE MF Demat Schemes " +e.getMessage());
		}
		
	}*/
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactBSEMFPhysicalSchemeTypes/{accessMode}", method = RequestMethod.GET)
	public ResponseEntity<?> AllTransactBSEMFPhysicalSchemeTypes(@PathVariable int accessMode) throws FinexaBussinessException {
		
		try {
			List<String> schemeTypes = masterService.getAllBSEMFPhysicalSchemeTypes(accessMode);
			return new ResponseEntity<List<String>>(schemeTypes, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve Transact BSE MF Demat Schemes Types" +e.getMessage());
		}
		
	}
	
	/*@RequestMapping(value = "/AllTransactBSEMFPhysicalSchemes", method = RequestMethod.GET)
	public ResponseEntity<?> AllTransactBSEMFPhysicalSchemes() throws FinexaBussinessException {
		
		try {
			List<MasterTransactBSEMFPhysicalSchemeDTO> masterTransactBSEMFPhysicalSchemeDTOList = masterService.getAllBSEMFPhysicalSchemes();
			return new ResponseEntity<List<MasterTransactBSEMFPhysicalSchemeDTO>>(masterTransactBSEMFPhysicalSchemeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve Transact BSE MF Physical Schemes " +e.getMessage());
		}
		
	}*/
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/getPhysicalAmcName/{accessMode}", method = RequestMethod.GET)
	public ResponseEntity<?> getPhysicalAmcName(@PathVariable int accessMode) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<String> masterTransactBSEMFDematSchemeDTOList = masterService.getAllBSEMFPhysicalAmc(accessMode);
			return new ResponseEntity<List<String>>(masterTransactBSEMFDematSchemeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve Transact BSE MF Demat Schemes " +e.getMessage());
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/getPhysicalSchemeNamesByFilter/{amcCode}/{schemeType}/{mode}/{accessMode}/{transactionMode}", method = RequestMethod.GET)
	public ResponseEntity<?> getPhysicalSchemeNamesByFilter(@PathVariable String amcCode, @PathVariable String schemeType, @PathVariable String mode, @PathVariable int accessMode,
			@PathVariable String transactionMode) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<MasterTransactBSEMFPhysicalSchemeDTO> masterTransactBSEMFPhysicalSchemeDTO = masterService.getPhysicalSchemeNamesByFilter(amcCode, schemeType, mode, accessMode, transactionMode);
			return new ResponseEntity<List<MasterTransactBSEMFPhysicalSchemeDTO>>(masterTransactBSEMFPhysicalSchemeDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve Transact BSE MF Demat Schemes " +e.getMessage());
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/getPhysicalSchemeByUniqueNo/{uniqueNo}/{accessMode}", method = RequestMethod.GET)
	public ResponseEntity<?> getPhysicalSchemeByUniqueNo(@PathVariable String uniqueNo, @PathVariable int accessMode) throws FinexaBussinessException, CustomFinexaException {
		try {
			MasterTransactBSEMFPhysicalSchemeDTO physicalDTO = masterService.getPhysicalSchemeDetails(uniqueNo, accessMode);
			return new ResponseEntity<MasterTransactBSEMFPhysicalSchemeDTO>(physicalDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve Transact BSE MF Physical Schemes " +e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getStateByCode/{stateCode}", method = RequestMethod.GET)
	public ResponseEntity<?> getStateByCode(@PathVariable String stateCode) throws FinexaBussinessException {
		String stateName = new String();
		try {
			stateName = masterService.getStateByCode(stateCode);
			return new ResponseEntity<String>(stateName, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("ccCreation","500","Failed to Retrieve State Name " +e.getMessage());
		}
	}
	
	// For fetching the File Name for back Office based on "page" and "RTA"
	@PreAuthorize("hasAnyRole('Admin','MFBacKOfficeView')")
	@RequestMapping(value = "/getSpecificFileNameByRTAAndFileCode/{rtaId}/{fileCode}", method = RequestMethod.GET)
	public ResponseEntity<?> getFileNameByRTAAndPage(@PathVariable int rtaId, @PathVariable String fileCode) throws FinexaBussinessException {
		try {
			List<MasterFileMappingBODTO> masterFileMappingBODTO = masterService.getSpecificFileName(rtaId, fileCode);
			return new ResponseEntity<List<MasterFileMappingBODTO>>(masterFileMappingBODTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("MF-Transact","500","Failed to Retrieve Transact BSE MF Demat Schemes " +e.getMessage());
		}
	}
	
	// For fetching the File Extension for back Office based on "File Name"
	@PreAuthorize("hasAnyRole('Admin','MFBacKOfficeView')")
	@RequestMapping(value = "/getFileExtensionByFileName/{fileId}", method = RequestMethod.GET)
	public ResponseEntity<?> getFileExtensionByFileName(@PathVariable int fileId) throws FinexaBussinessException {
		try {
			MasterFileMappingBODTO masterFileMappingBODTO = masterService.getFileExtensionByFileName(fileId);
			return new ResponseEntity<MasterFileMappingBODTO>(masterFileMappingBODTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("MF-Transact Back Office","500","Failed to Retrieve Extension of File " +e.getMessage());
		}
	}
	
}
