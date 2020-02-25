package com.finlabs.finexa.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.LookupAlternateInvestmentsAssetTypeDTO;
import com.finlabs.finexa.dto.LookupAssetClassDTO;
import com.finlabs.finexa.dto.LookupAssetSubClassDTO;
import com.finlabs.finexa.dto.LookupBondTypeDTO;
import com.finlabs.finexa.dto.LookupCashBalanceTypeDTO;
import com.finlabs.finexa.dto.LookupCountryDTO;
import com.finlabs.finexa.dto.LookupEducationalQualificationDTO;
import com.finlabs.finexa.dto.LookupEmploymentTypeDTO;
import com.finlabs.finexa.dto.LookupFixedDepositTypeDTO;
import com.finlabs.finexa.dto.LookupFrequencyDTO;
import com.finlabs.finexa.dto.LookupFundInvestmentModeDTO;
import com.finlabs.finexa.dto.LookupFundTypeDTO;
import com.finlabs.finexa.dto.LookupGoalFrequencyDTO;
import com.finlabs.finexa.dto.LookupGoalTypeDTO;
import com.finlabs.finexa.dto.LookupIncomeExpenseDurationDTO;
import com.finlabs.finexa.dto.LookupInsuranceTypeDTO;
import com.finlabs.finexa.dto.LookupLifeInsurancePolicyTypeDTO;
import com.finlabs.finexa.dto.LookupLoanCategoryDTO;
import com.finlabs.finexa.dto.LookupMaritalStatusDTO;
import com.finlabs.finexa.dto.LookupMonthDTO;
import com.finlabs.finexa.dto.LookupRTABODTO;
import com.finlabs.finexa.dto.LookupRTAFileNameDTO;
import com.finlabs.finexa.dto.LookupRealEstateTypeDTO;
import com.finlabs.finexa.dto.LookupRelationsDTO;
import com.finlabs.finexa.dto.LookupResidentTypeDTO;
import com.finlabs.finexa.dto.LookupRoleDTO;
import com.finlabs.finexa.dto.LookupTransactHoldingTypeDTO;
import com.finlabs.finexa.dto.LookupTransactKYCTypeDTO;
import com.finlabs.finexa.dto.LookupTransactUCCClientDefaultDPDTO;
import com.finlabs.finexa.dto.LookupTransactUCCClientTypeDTO;
import com.finlabs.finexa.dto.TransactionMISDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.model.LookupAssetClass;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.LookupService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class LookupController {

	private static Logger log = LoggerFactory.getLogger(ClientPersonalInfoController.class);

	@Autowired
	LookupService lookupDaoService;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllEducationalQualification", method = RequestMethod.GET)
	public ResponseEntity<?> findAllEQ() throws FinexaBussinessException {

		try {
			List<LookupEducationalQualificationDTO> lookupEducationalQualificationDTOList = lookupDaoService
					.getAllEducationalQualification();
			return new ResponseEntity<List<LookupEducationalQualificationDTO>>(lookupEducationalQualificationDTOList,
					HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_EDUCATIONAL_QUALIFICATION_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_EDUCATIONAL_QUALIFICATION_MODULE,
					FinexaConstant.GET_EDUCATIONAL_QUALIFICATION_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllEmploymentType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllET() throws FinexaBussinessException {

		try {
			List<LookupEmploymentTypeDTO> lookupEmploymentTypeDTOList = lookupDaoService.getAllEmploymentType();
			return new ResponseEntity<List<LookupEmploymentTypeDTO>>(lookupEmploymentTypeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_EMPLOYMENT_TYPE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_EMPLOYMENT_TYPE_MODULE,
					FinexaConstant.GET_EMPLOYMENT_TYPE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllResidentType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllRT() throws FinexaBussinessException {

		try {
			List<LookupResidentTypeDTO> lookupEmploymentTypeDTOList = lookupDaoService.getAllResidentType();
			return new ResponseEntity<List<LookupResidentTypeDTO>>(lookupEmploymentTypeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_RESIDENT_TYPE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_RESIDENT_TYPE_MODULE,
					FinexaConstant.GET_RESIDENT_TYPE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMaritalStatus", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMS() throws FinexaBussinessException {

		try {
			List<LookupMaritalStatusDTO> lookupMaritalStatusDTOList = lookupDaoService.getAllMartialStatus();
			return new ResponseEntity<List<LookupMaritalStatusDTO>>(lookupMaritalStatusDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_MARITAL_STATUS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_MARITAL_STATUS_MODULE,
					FinexaConstant.GET_MARITAL_STATUS_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllCountries", method = RequestMethod.GET)
	public ResponseEntity<?> findAllCountries() throws FinexaBussinessException {

		try {
			List<LookupCountryDTO> lookupCountryList = lookupDaoService.getAllCountry();
			return new ResponseEntity<List<LookupCountryDTO>>(lookupCountryList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_COUNTRY_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_COUNTRY_MODULE, FinexaConstant.GET_COUNTRY_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllRelations", method = RequestMethod.GET)
	public ResponseEntity<?> findAllRelations() {

		List<LookupRelationsDTO> lookupRelationsDTOList = lookupDaoService.getAllRelations();
		return new ResponseEntity<List<LookupRelationsDTO>>(lookupRelationsDTOList, HttpStatus.OK);

	}

	/*************
	 * New Lookup added for fundCategory
	 * 
	 * 
	 ***********************/
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllFundCategory", method = RequestMethod.GET)
	public ResponseEntity<?> findAllFundCategory() throws FinexaBussinessException {
		try {
			List<LookupAssetClassDTO> lookupFundDTOList = lookupDaoService.getAllFundCategory();
			return new ResponseEntity<List<LookupAssetClassDTO>>(lookupFundDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_FUND_CATEGORY_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_ASSET_CLASS_MODULE,
					FinexaConstant.GET_FUND_CATEGORY_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/SubAssetByAssetForFunds/{assetClass}", method = RequestMethod.GET)
	public ResponseEntity<?> subAssetByAssetForFunds(@PathVariable LookupAssetClass assetClass) {
		List<LookupAssetSubClassDTO> lookupAssetSubClassDTOList = lookupDaoService.getSubAssetByAssetForFunds(assetClass);
		return new ResponseEntity<List<LookupAssetSubClassDTO>>(lookupAssetSubClassDTOList, HttpStatus.OK);
	}

	/************* New Lookup added for fundType ***********************/
	@RequestMapping(value = "/AllFundType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllFundType() {

		List<LookupFundTypeDTO> lookupFundTypeDTOList = lookupDaoService.getAllFundType();
		return new ResponseEntity<List<LookupFundTypeDTO>>(lookupFundTypeDTOList, HttpStatus.OK);

	}

	/*************
	 * New Lookup added for fundInvestmentMode
	 * 
	 * 
	 ***********************/
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllFundInvestmentMode", method = RequestMethod.GET)
	public ResponseEntity<?> findAllFundInvestmentMode() throws FinexaBussinessException {

		try {
			List<LookupFundInvestmentModeDTO> lookupFundInvestmentModeDTOList = lookupDaoService
					.getAllFundInvestmentMode();
			return new ResponseEntity<List<LookupFundInvestmentModeDTO>>(lookupFundInvestmentModeDTOList,
					HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_FUND_INVESTMENT_MODE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_FUND_INVESTMENT_MODE_MODULE,
					FinexaConstant.GET_FUND_INVESTMENT_MODE_ERROR, exception != null ? exception.getErrorMessage() : "",
					e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllGoalType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllGoalType() {

		List<LookupGoalTypeDTO> lookupGoalTypeDTOList = lookupDaoService.getAllGoalType();
		return new ResponseEntity<List<LookupGoalTypeDTO>>(lookupGoalTypeDTOList, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllFrequency", method = RequestMethod.GET)
	public ResponseEntity<?> findAllFrequency() throws FinexaBussinessException {

		try {
			List<LookupFrequencyDTO> lookupFrequencyDTOList = lookupDaoService.getAllFrequency();
			return new ResponseEntity<List<LookupFrequencyDTO>>(lookupFrequencyDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.LOOKUP_FREQUENCY_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_FREQUENCY_MODULE,
					FinexaConstant.LOOKUP_FREQUENCY_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "",
					e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllGoalFrequency", method = RequestMethod.GET)
	public ResponseEntity<?> findAllGoalFrequency() throws FinexaBussinessException {

		try {
			List<LookupGoalFrequencyDTO> lookupGoalFrequencyDTOList = lookupDaoService.getAllGoalFrequency();
			return new ResponseEntity<List<LookupGoalFrequencyDTO>>(lookupGoalFrequencyDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_GOALS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_GOAL_CORPUS_UTILIZATION_FREQUENCY_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_GOAL_CORPUS_UTILIZATION_FREQUENCY_MODULE,
					FinexaConstant.GET_GOAL_CORPUS_UTILIZATION_FREQUENCY_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllLoanCategory", method = RequestMethod.GET)
	public ResponseEntity<?> findAllLoanCategory() throws FinexaBussinessException {

		try {
			List<LookupLoanCategoryDTO> lookupLoanCategoryDTOList = lookupDaoService.getAllLoanCategory();
			return new ResponseEntity<List<LookupLoanCategoryDTO>>(lookupLoanCategoryDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_LOANS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_LOAN_CATEGORY_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_LOAN_CATEGORY_MODULE,
					FinexaConstant.GET_LOAN_CATEGORY_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllAlternateInvestmentsAssetType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllAssetType() throws FinexaBussinessException {

		try {
			List<LookupAlternateInvestmentsAssetTypeDTO> lookupAIAssetTypeDTOList = lookupDaoService.getAllAssetType();
			return new ResponseEntity<List<LookupAlternateInvestmentsAssetTypeDTO>>(lookupAIAssetTypeDTOList,
					HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.LOOKUP_AI_ASSET_TYPE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_AI_ASSET_TYPE_MODULE,
					FinexaConstant.LOOKUP_AI_ASSET_TYPE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllRealEstateType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllRealEstateType() throws FinexaBussinessException {

		try {
			List<LookupRealEstateTypeDTO> lookupRETypeDTOList = lookupDaoService.getAllRealEstateType();
			return new ResponseEntity<List<LookupRealEstateTypeDTO>>(lookupRETypeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.LOOKUP_REAL_ESTATE_TYPE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_REAL_ESTATE_TYPE_MODULE,
					FinexaConstant.LOOKUP_REAL_ESTATE_TYPE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllCashBalanceType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllCashBalanceType() {

		List<LookupCashBalanceTypeDTO> lookupCashBalanceTypeDTOList = lookupDaoService.getAllCashBalanceType();

		return new ResponseEntity<List<LookupCashBalanceTypeDTO>>(lookupCashBalanceTypeDTOList, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllLifeInsurancePolicyType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllLifeInsurancePolicyType() {

		List<LookupLifeInsurancePolicyTypeDTO> lookupLifeInsurancePolicyTypeDTOList = lookupDaoService
				.getAllLifeInsurancePolicyType();

		return new ResponseEntity<List<LookupLifeInsurancePolicyTypeDTO>>(lookupLifeInsurancePolicyTypeDTOList,
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllMonths", method = RequestMethod.GET)
	public ResponseEntity<?> findAllMonth() throws FinexaBussinessException {

		try {
			List<LookupMonthDTO> lookupMonthDTOList = lookupDaoService.getAllMonth();
			return new ResponseEntity<List<LookupMonthDTO>>(lookupMonthDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INCOME);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_ALL_INCOME_EXPENSE_MONTHS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_MONTH_MODULE,
					FinexaConstant.GET_ALL_INCOME_EXPENSE_MONTHS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllYears", method = RequestMethod.GET)
	public ResponseEntity<?> findAllYear() throws FinexaBussinessException {

		try {
			List<LookupIncomeExpenseDurationDTO> lookupYearDTOList = lookupDaoService.getAllYear();
			return new ResponseEntity<List<LookupIncomeExpenseDurationDTO>>(lookupYearDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INCOME);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_ALL_INCOME_EXPENSE_YEARS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_INCOME_EXPENSE_DURATION_MODULE,
					FinexaConstant.GET_ALL_INCOME_EXPENSE_YEARS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllBondType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllBondType() throws FinexaBussinessException {

		try {
			List<LookupBondTypeDTO> lookupBondTypeDTOList = lookupDaoService.getAllBondType();
			return new ResponseEntity<List<LookupBondTypeDTO>>(lookupBondTypeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.LOOKUP_BOND_TYPE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_BOND_TYPE_MODULE,
					FinexaConstant.LOOKUP_BOND_TYPE_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "",
					e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllFixedDepositType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllFixedDepositType() throws FinexaBussinessException {

		try {
			List<LookupFixedDepositTypeDTO> lookupFixedDepositTypeDTOList = lookupDaoService.getAllFixedDepositType();
			return new ResponseEntity<List<LookupFixedDepositTypeDTO>>(lookupFixedDepositTypeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.LOOKUP_FIXED_DEPOSIT_TYPE_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_FIXED_DEPOSIT_TYPE_MODULE,
					FinexaConstant.LOOKUP_FIXED_DEPOSIT_TYPE_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/AllInsuranceType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllInsuranceType() {

		List<LookupInsuranceTypeDTO> LookupInsuranceTypeDTOList = lookupDaoService.getAllInsuranceType();

		return new ResponseEntity<List<LookupInsuranceTypeDTO>>(LookupInsuranceTypeDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactHoldingType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllTransactHoldingType() throws FinexaBussinessException {

		try {
			List<LookupTransactHoldingTypeDTO> lookupTransactHoldingTypeDTOList = lookupDaoService.getAllHoldingType();
			return new ResponseEntity<List<LookupTransactHoldingTypeDTO>>(lookupTransactHoldingTypeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All the Holding Types " +e.getMessage());
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactKYCType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllTransactKYCType() throws FinexaBussinessException {

		try {
			List<LookupTransactKYCTypeDTO> lookupTransactKYCTypeDTOList = lookupDaoService.getAllTransactKYCType();
			return new ResponseEntity<List<LookupTransactKYCTypeDTO>>(lookupTransactKYCTypeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All the Holding Types " +e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactUCCClientDefaultDP", method = RequestMethod.GET)
	public ResponseEntity<?> findAllTransactUCCClientDefaultDP() throws FinexaBussinessException {

		try {
			List<LookupTransactUCCClientDefaultDPDTO> lookupTransactUCCClientDefaultDPDTOList = lookupDaoService.getAllTransactUCCClientDefaultDP();
			return new ResponseEntity<List<LookupTransactUCCClientDefaultDPDTO>>(lookupTransactUCCClientDefaultDPDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All the UCCClient Default DP " +e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/AllTransactUCCClientType", method = RequestMethod.GET)
	public ResponseEntity<?> findAllTransactUCCClientType() throws FinexaBussinessException {

		try {
			List<LookupTransactUCCClientTypeDTO> lookupTransactUCCClientTypeDTOList = lookupDaoService.getAllTransactUCCClientType();
			return new ResponseEntity<List<LookupTransactUCCClientTypeDTO>>(lookupTransactUCCClientTypeDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Transact","500","Failed to Retrieve All the UCCClient Types " +e.getMessage());
		}
		
	}
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/getAllRoles", method = RequestMethod.GET)
	public ResponseEntity<?> findAllRole() throws FinexaBussinessException {

		try {
			List<LookupRoleDTO> lookupRoleDTOList = lookupDaoService.getAllRoles();
			return new ResponseEntity<List<LookupRoleDTO>>(lookupRoleDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INCOME);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.GET_ALL_INCOME_EXPENSE_MONTHS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.LOOKUP_MONTH_MODULE,
					FinexaConstant.GET_ALL_INCOME_EXPENSE_MONTHS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
		
	}
	// For fetching RTA name for 'Back-Office' Project
	    @PreAuthorize("hasAnyRole('Admin','MFBacKOfficeView')")
		@RequestMapping(value = "/AllRTAType", method = RequestMethod.GET)
		public ResponseEntity<?> findAllRTAType() throws FinexaBussinessException {

			try {
				List<LookupRTABODTO> lookupRTABODTOList = lookupDaoService.getAllRTABOType();
				return new ResponseEntity<List<LookupRTABODTO>>(lookupRTABODTOList, HttpStatus.OK);
			} catch (RuntimeException e) {
				throw new FinexaBussinessException("MF-Back Office","500","Failed to Retrieve All the RTA " +e.getMessage());
			}
			
		}
		
	    @PreAuthorize("hasAnyRole('Admin','MFBacKOfficeView')")
		// For fetching File name for 'Back-Office' Project
		@RequestMapping(value = "/AllFileName", method = RequestMethod.GET)
		public ResponseEntity<?> findAllFileName() throws FinexaBussinessException {

			try {
				List<LookupRTAFileNameDTO> lookupRTAFileNameDTOList = lookupDaoService.getAllFileName();
				return new ResponseEntity<List<LookupRTAFileNameDTO>>(lookupRTAFileNameDTOList, HttpStatus.OK);
			} catch (RuntimeException e) {
				throw new FinexaBussinessException("MF-Back Office","500","Failed to Retrieve All the RTA " +e.getMessage());
			}
			
		}
	    
	    @PreAuthorize("hasAnyRole('Admin','MFBacKOfficeView')")
	    @RequestMapping(value = "/allARNbyadvisorId/{advisorId}", method = RequestMethod.GET)
		public ResponseEntity<?> allARNbyadvisorId(@PathVariable int advisorId) throws FinexaBussinessException {
								System.out.println("hello");		
			try {
				List<TransactionMISDTO> listInvestorName = lookupDaoService.getAllARNByAdvisorId(advisorId);
				return new ResponseEntity<List<TransactionMISDTO>>(listInvestorName, HttpStatus.OK);
			} catch (RuntimeException rexp) {
				throw new FinexaBussinessException("MF-BackOffice", "MFBO_GINL", "Failed to get Investor Name Or Client Name List.", rexp);
			}
		}
}
