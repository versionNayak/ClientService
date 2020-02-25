package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.LookupAlternateInvestmentsAssetTypeDTO;
import com.finlabs.finexa.dto.LookupAnnuityTypeDTO;
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
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserRoleMapping;
import com.finlabs.finexa.model.ClientARNMapping;
import com.finlabs.finexa.model.LookupAlternateInvestmentsAssetType;
import com.finlabs.finexa.model.LookupAnnuityType;
import com.finlabs.finexa.model.LookupAssetClass;
import com.finlabs.finexa.model.LookupAssetSubClass;
import com.finlabs.finexa.model.LookupBondType;
import com.finlabs.finexa.model.LookupCashBalanceType;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.LookupEducationalQualification;
import com.finlabs.finexa.model.LookupEmploymentType;
import com.finlabs.finexa.model.LookupFixedDepositType;
import com.finlabs.finexa.model.LookupFrequency;
import com.finlabs.finexa.model.LookupFundInvestmentMode;
import com.finlabs.finexa.model.LookupFundType;
import com.finlabs.finexa.model.LookupGoalCorpusUtilizationFrequency;
import com.finlabs.finexa.model.LookupGoalType;
import com.finlabs.finexa.model.LookupIncomeExpenseDuration;
import com.finlabs.finexa.model.LookupInsurancePolicyType;
import com.finlabs.finexa.model.LookupInsuranceType;
import com.finlabs.finexa.model.LookupLoanCategory;
import com.finlabs.finexa.model.LookupMaritalStatus;
import com.finlabs.finexa.model.LookupMonth;
import com.finlabs.finexa.model.LookupRTABO;
import com.finlabs.finexa.model.LookupRTAFileName;
import com.finlabs.finexa.model.LookupRealEstateType;
import com.finlabs.finexa.model.LookupRelation;
import com.finlabs.finexa.model.LookupResidentType;
import com.finlabs.finexa.model.LookupRole;
import com.finlabs.finexa.model.LookupTansactHoldingType;
import com.finlabs.finexa.model.LookupTransactKYCType;
import com.finlabs.finexa.model.LookupTransactUCCClientDefaultDP;
import com.finlabs.finexa.model.LookupTransactUCCClientType;
import com.finlabs.finexa.model.MasterFileMappingBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserRoleMappingRepository;
import com.finlabs.finexa.repository.AlternateInvestmentsAssetTypeRepository;
import com.finlabs.finexa.repository.CashBalanceTypeRepository;
import com.finlabs.finexa.repository.ClientARNMappingRepository;
import com.finlabs.finexa.repository.CountryRepository;
import com.finlabs.finexa.repository.EducationalQualificationRepository;
import com.finlabs.finexa.repository.EmploymentTypeRepository;
import com.finlabs.finexa.repository.FrequencyRepository;
import com.finlabs.finexa.repository.FundInvestmentModeRepository;
import com.finlabs.finexa.repository.FundTypeRepository;
import com.finlabs.finexa.repository.GoalFrequencyRepository;
import com.finlabs.finexa.repository.GoalTypeRepository;
import com.finlabs.finexa.repository.IncomeExpenseDurationRepository;
import com.finlabs.finexa.repository.LoanCategoryRepository;
import com.finlabs.finexa.repository.LookupAnnuityTypeRepository;
import com.finlabs.finexa.repository.LookupAssetClassRepository;
import com.finlabs.finexa.repository.LookupAssetSubClassRepository;
import com.finlabs.finexa.repository.LookupBondTypeRepository;
import com.finlabs.finexa.repository.LookupFixedDepositTypeRepository;
import com.finlabs.finexa.repository.LookupInsurancePolicyTypeRepository;
import com.finlabs.finexa.repository.LookupInsuranceTypeRepository;
import com.finlabs.finexa.repository.LookupRTABORepository;
import com.finlabs.finexa.repository.LookupRTAFileNameRepository;
import com.finlabs.finexa.repository.LookupRelationshipRepository;
import com.finlabs.finexa.repository.LookupRoleRepository;
import com.finlabs.finexa.repository.LookupTransactHoldingTypeRepository;
import com.finlabs.finexa.repository.LookupTransactKYCTypeRepository;
import com.finlabs.finexa.repository.LookupTransactUCCClientDefaultDPRepository;
import com.finlabs.finexa.repository.LookupTransactUCCClientTypeRepository;
import com.finlabs.finexa.repository.MaritalStatusRepository;
import com.finlabs.finexa.repository.MonthRepository;
import com.finlabs.finexa.repository.RealEsateTypeRepository;
import com.finlabs.finexa.repository.ResidentTypeRepository;

@Service("LookupService")
@Transactional
public class LookupServiceImpl implements LookupService {

	private static Logger log = LoggerFactory.getLogger(LookupServiceImpl.class);

	@Autowired
	private EducationalQualificationRepository educationalQualificationRepository;
	@Autowired
	private EmploymentTypeRepository employmentTypeRepository;
	@Autowired
	private ResidentTypeRepository residentTypeRepository;
	@Autowired
	private MaritalStatusRepository maritalStatusRepository;
	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private LookupRelationshipRepository relationsRepository;
	@Autowired
	private FundTypeRepository fundTypeRepository;
	@Autowired
	private FundInvestmentModeRepository fundInvestmentModeRepository;
	@Autowired
	private GoalTypeRepository goalTypeRepository;
	@Autowired
	private FrequencyRepository frequencyRepository;
	@Autowired
	private GoalFrequencyRepository goalFrequencyRepository;
	@Autowired
	private LoanCategoryRepository loanCategoryRepository;
	@Autowired
	private AlternateInvestmentsAssetTypeRepository alternateInvestmentsAssetTypeRepository;
	@Autowired
	private RealEsateTypeRepository realEstateTypeRepository;
	@Autowired
	private CashBalanceTypeRepository cashBalanceTypeRepository;
	@Autowired
	private LookupInsurancePolicyTypeRepository insurancePolicyTypeRepository;
	@Autowired
	private LookupAssetClassRepository assetClassRepository;
	@Autowired
	private LookupAssetSubClassRepository lookupAssetSubClassRepository;
	@Autowired
	private MonthRepository monthRepository;
	@Autowired
	private IncomeExpenseDurationRepository incomeExpenseDurationRepository;
	@Autowired
	private LookupBondTypeRepository lookupBondTypeRepository;
	@Autowired
	private LookupFixedDepositTypeRepository lookupFixedDepositTypeRepository;
	@Autowired
	private LookupAnnuityTypeRepository lookupAnnuityTypeRepository;
	@Autowired
	private LookupTransactHoldingTypeRepository lookupTransactHoldingTypeRepository;
	@Autowired
	private LookupTransactKYCTypeRepository lookupTransactKYCTypeRepository;
	@Autowired
	Mapper mapper;
	@Autowired
	private LookupInsuranceTypeRepository lookupInsuranceTypeRepository;
	@Autowired
	private LookupTransactUCCClientDefaultDPRepository lookupTransactUCCClientDefaultDPRepository;
	@Autowired
	private LookupTransactUCCClientTypeRepository lookupTransactUCCClientTypeRepository;
	@Autowired
	private LookupRoleRepository lookupRoleRepository;
	@Autowired
	private LookupRTABORepository lookupRTABORepository;
	@Autowired
	private LookupRTAFileNameRepository lookupRTAFileNameRepository;
	@Autowired
	private AdvisorUserRoleMappingRepository advisorUserRoleMappingRepository;
	@Autowired
	private AdvisorUserRepository advisorUserRepository;
	@Autowired
	ClientARNMappingRepository clientARNMappingRepository;
	

	@Override
	public List<LookupEducationalQualificationDTO> getAllEducationalQualification() throws RuntimeException {
		try {
			List<LookupEducationalQualification> LookupEducationalQualifications = educationalQualificationRepository
					.findAll();

			List<LookupEducationalQualificationDTO> listDTO = new ArrayList<LookupEducationalQualificationDTO>();
			for (LookupEducationalQualification lookupEducationalQualification : LookupEducationalQualifications) {
				listDTO.add(mapper.map(lookupEducationalQualification, LookupEducationalQualificationDTO.class));
			}

			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupEmploymentTypeDTO> getAllEmploymentType() throws RuntimeException {
		try {
			List<LookupEmploymentType> LookupEmploymentTypes = employmentTypeRepository.findAll();

			List<LookupEmploymentTypeDTO> listDTO = new ArrayList<LookupEmploymentTypeDTO>();
			for (LookupEmploymentType lookupEmploymentType : LookupEmploymentTypes) {
				listDTO.add(mapper.map(lookupEmploymentType, LookupEmploymentTypeDTO.class));
			}

			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupResidentTypeDTO> getAllResidentType() throws RuntimeException {
		try {
			List<LookupResidentType> lookupResidentTypes = residentTypeRepository.findAll();

			List<LookupResidentTypeDTO> listDTO = new ArrayList<LookupResidentTypeDTO>();
			for (LookupResidentType lookupResidentType : lookupResidentTypes) {
				listDTO.add(mapper.map(lookupResidentType, LookupResidentTypeDTO.class));
			}

			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupCountryDTO> getAllCountry() throws RuntimeException {
		try {
			List<LookupCountry> lookupCountrys = countryRepository.findAll();

			List<LookupCountryDTO> listDTO = new ArrayList<LookupCountryDTO>();
			for (LookupCountry lookupCountry : lookupCountrys) {
				listDTO.add(mapper.map(lookupCountry, LookupCountryDTO.class));
			}

			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupRelationsDTO> getAllRelations() {
		List<LookupRelation> lookupRelations = relationsRepository.findAll();

		List<LookupRelationsDTO> listDTO = new ArrayList<LookupRelationsDTO>();
		for (LookupRelation lookupRelation : lookupRelations) {
			listDTO.add(mapper.map(lookupRelation, LookupRelationsDTO.class));
		}

		return listDTO;
	}

	@Override
	public List<LookupMaritalStatusDTO> getAllMartialStatus() throws RuntimeException {
		try {
			List<LookupMaritalStatus> lookupMaritalStatuses = maritalStatusRepository.findAll();

			List<LookupMaritalStatusDTO> listDTO = new ArrayList<LookupMaritalStatusDTO>();
			for (LookupMaritalStatus lookupMaritalStatus : lookupMaritalStatuses) {
				listDTO.add(mapper.map(lookupMaritalStatus, LookupMaritalStatusDTO.class));
			}

			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupAssetClassDTO> getAllFundCategory() throws RuntimeException {
		try {
			// List<LookupFundCategory> lookupFundCategoryList =
			// fundCategoryRepository.findAll();
			// List<LookupAssetClass> lookupFundCategoryList =
			// fundCategoryRepository.findAll();
			List<LookupAssetClass> lookupFundCategoryList = assetClassRepository.findAll();

			// List<LookupFundCategoryDTO> fundDTO = new
			// ArrayList<LookupFundCategoryDTO>();
			// for (LookupFundCategory lookupFund : lookupFundCategoryList) {
			List<LookupAssetClassDTO> fundCategoryList = new ArrayList<LookupAssetClassDTO>();
			for (LookupAssetClass lookupFundCategory : lookupFundCategoryList) {
				fundCategoryList.add(mapper.map(lookupFundCategory, LookupAssetClassDTO.class));
			}

			return fundCategoryList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupFundTypeDTO> getAllFundType() {
		List<LookupFundType> fundTypeList = fundTypeRepository.findAll();

		List<LookupFundTypeDTO> fundTypeDTOList = new ArrayList<LookupFundTypeDTO>();
		for (LookupFundType lookupFundType : fundTypeList) {
			fundTypeDTOList.add(mapper.map(lookupFundType, LookupFundTypeDTO.class));
		}

		return fundTypeDTOList;
	}

	@Override
	public List<LookupFundInvestmentModeDTO> getAllFundInvestmentMode() throws RuntimeException {
		try {
			List<LookupFundInvestmentMode> fundInvestmentModeList = fundInvestmentModeRepository.findAll();

			List<LookupFundInvestmentModeDTO> fundInvestmentModeDTOList = new ArrayList<LookupFundInvestmentModeDTO>();
			for (LookupFundInvestmentMode lookupInvestmentMode : fundInvestmentModeList) {
				fundInvestmentModeDTOList.add(mapper.map(lookupInvestmentMode, LookupFundInvestmentModeDTO.class));
			}

			return fundInvestmentModeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupGoalTypeDTO> getAllGoalType() {
		List<LookupGoalType> goalTypeList = goalTypeRepository.findAll();

		List<LookupGoalTypeDTO> goalTypeDTOList = new ArrayList<LookupGoalTypeDTO>();
		for (LookupGoalType lookupGoalType : goalTypeList) {
			goalTypeDTOList.add(mapper.map(lookupGoalType, LookupGoalTypeDTO.class));
		}

		return goalTypeDTOList;
	}

	@Override
	public List<LookupFrequencyDTO> getAllFrequency() throws RuntimeException {
		try {
			List<LookupFrequency> frequencyList = frequencyRepository.findAllByOrderBydisplayOrderAsc();

			List<LookupFrequencyDTO> frequencyDTOList = new ArrayList<LookupFrequencyDTO>();
			for (LookupFrequency lookupFrequency : frequencyList) {
				frequencyDTOList.add(mapper.map(lookupFrequency, LookupFrequencyDTO.class));
			}

			return frequencyDTOList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupGoalFrequencyDTO> getAllGoalFrequency() throws RuntimeException {
		try {
			List<LookupGoalCorpusUtilizationFrequency> goalFrequencyList = goalFrequencyRepository.findAll();

			List<LookupGoalFrequencyDTO> goalFrequencyDTOList = new ArrayList<LookupGoalFrequencyDTO>();
			for (LookupGoalCorpusUtilizationFrequency lookupGoalCorpusUtilizationFrequency : goalFrequencyList) {
				goalFrequencyDTOList
						.add(mapper.map(lookupGoalCorpusUtilizationFrequency, LookupGoalFrequencyDTO.class));
			}

			return goalFrequencyDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupLoanCategoryDTO> getAllLoanCategory() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupLoanCategory> loanCategoryList = loanCategoryRepository.findAll();

			List<LookupLoanCategoryDTO> loanCategoryDTOList = new ArrayList<LookupLoanCategoryDTO>();
			for (LookupLoanCategory lookupLoanCategory : loanCategoryList) {
				loanCategoryDTOList.add(mapper.map(lookupLoanCategory, LookupLoanCategoryDTO.class));
			}
			return loanCategoryDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupAlternateInvestmentsAssetTypeDTO> getAllAssetType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupAlternateInvestmentsAssetType> aiAssetTypeList = alternateInvestmentsAssetTypeRepository
					.findAll();

			List<LookupAlternateInvestmentsAssetTypeDTO> aiAssetTypeDTOList = new ArrayList<LookupAlternateInvestmentsAssetTypeDTO>();
			for (LookupAlternateInvestmentsAssetType lookupAIAssetType : aiAssetTypeList) {
				aiAssetTypeDTOList.add(mapper.map(lookupAIAssetType, LookupAlternateInvestmentsAssetTypeDTO.class));
			}

			return aiAssetTypeDTOList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupRealEstateTypeDTO> getAllRealEstateType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupRealEstateType> reTypeList = realEstateTypeRepository.findAll();

			List<LookupRealEstateTypeDTO> reTypeDTOList = new ArrayList<LookupRealEstateTypeDTO>();
			for (LookupRealEstateType lookupREType : reTypeList) {
				reTypeDTOList.add(mapper.map(lookupREType, LookupRealEstateTypeDTO.class));
			}
			return reTypeDTOList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupCashBalanceTypeDTO> getAllCashBalanceType() {
		// TODO Auto-generated method stub
		List<LookupCashBalanceType> cashBalanceTypeList = cashBalanceTypeRepository.findAll();

		List<LookupCashBalanceTypeDTO> cashBalanceTypeDTOList = new ArrayList<LookupCashBalanceTypeDTO>();
		for (LookupCashBalanceType lookupCashBalanceType : cashBalanceTypeList) {
			cashBalanceTypeDTOList.add(mapper.map(lookupCashBalanceType, LookupCashBalanceTypeDTO.class));
		}
		return cashBalanceTypeDTOList;
	}

	@Override
	public List<LookupLifeInsurancePolicyTypeDTO> getAllLifeInsurancePolicyType() {
		// TODO Auto-generated method stub
		// Byte insId = 1;

		List<LookupInsurancePolicyType> lifeInsurancePolicyTypeList = insurancePolicyTypeRepository.findAll();

		List<LookupLifeInsurancePolicyTypeDTO> lifeInsurancePolicyTypeDTOList = new ArrayList<LookupLifeInsurancePolicyTypeDTO>();

		for (LookupInsurancePolicyType lookupLifeInsurancePolicy : lifeInsurancePolicyTypeList) {
			lifeInsurancePolicyTypeDTOList
					.add(mapper.map(lookupLifeInsurancePolicy, LookupLifeInsurancePolicyTypeDTO.class));
		}

		return lifeInsurancePolicyTypeDTOList;
	}

	@Override
	public List<LookupMonthDTO> getAllMonth() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupMonth> monthList = monthRepository.findAllByOrderBydisplayOrderAsc();

			List<LookupMonthDTO> lookupMonthDTOList = new ArrayList<LookupMonthDTO>();
			for (LookupMonth lookupMonth : monthList) {
				lookupMonthDTOList.add(mapper.map(lookupMonth, LookupMonthDTO.class));
			}
			return lookupMonthDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupIncomeExpenseDurationDTO> getAllYear() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupIncomeExpenseDuration> yearList = incomeExpenseDurationRepository.findAll();

			List<LookupIncomeExpenseDurationDTO> lookupYearDTOList = new ArrayList<LookupIncomeExpenseDurationDTO>();

			for (LookupIncomeExpenseDuration lookupIncomeExpenseDuration : yearList) {
				lookupYearDTOList.add(mapper.map(lookupIncomeExpenseDuration, LookupIncomeExpenseDurationDTO.class));
			}
			return lookupYearDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupBondTypeDTO> getAllBondType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupBondType> BondTypeList = lookupBondTypeRepository.findAll();

			List<LookupBondTypeDTO> lookupBondTypeListDTOList = new ArrayList<LookupBondTypeDTO>();

			for (LookupBondType lookupBondType : BondTypeList) {
				lookupBondTypeListDTOList.add(mapper.map(lookupBondType, LookupBondTypeDTO.class));
			}
			return lookupBondTypeListDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupFixedDepositTypeDTO> getAllFixedDepositType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupFixedDepositType> fixedDepositTypeList = lookupFixedDepositTypeRepository.findAll();

			List<LookupFixedDepositTypeDTO> lookupFixedDepositTypeList = new ArrayList<LookupFixedDepositTypeDTO>();

			for (LookupFixedDepositType lookupFixedDepositType : fixedDepositTypeList) {
				lookupFixedDepositTypeList.add(mapper.map(lookupFixedDepositType, LookupFixedDepositTypeDTO.class));
			}
			return lookupFixedDepositTypeList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupAnnuityTypeDTO> getAllAnnuityType() {
		// TODO Auto-generated method stub
		List<LookupAnnuityType> annuityTypeList = lookupAnnuityTypeRepository.findAll();

		List<LookupAnnuityTypeDTO> lookupAnnuityList = new ArrayList<LookupAnnuityTypeDTO>();

		for (LookupAnnuityType lookupAnnuityType : annuityTypeList) {
			lookupAnnuityList.add(mapper.map(lookupAnnuityType, LookupAnnuityTypeDTO.class));
		}
		return lookupAnnuityList;
	}

	public List<LookupInsuranceTypeDTO> getAllInsuranceType() {
		List<LookupInsuranceType> lookupInsuranceTypeList = lookupInsuranceTypeRepository.findAll();

		List<LookupInsuranceTypeDTO> LookupInsuranceTypeDTOList = new ArrayList<LookupInsuranceTypeDTO>();
		for (LookupInsuranceType lookupInsuranceType : lookupInsuranceTypeList) {
			LookupInsuranceTypeDTOList.add(mapper.map(lookupInsuranceType, LookupInsuranceTypeDTO.class));
		}
		return LookupInsuranceTypeDTOList;

	}

	@Override
	public List<LookupAssetSubClassDTO> getSubAssetByAssetForFunds(LookupAssetClass assetClass) {
		// TODO Auto-generated method stub
		List<LookupAssetSubClass> lookupAssetSubClassList = lookupAssetSubClassRepository.findByLookupAssetClass(assetClass);
		List<LookupAssetSubClassDTO> dtoList = new ArrayList<LookupAssetSubClassDTO>();
		for (LookupAssetSubClass obj : lookupAssetSubClassList) {
			dtoList.add(mapper.map(obj, LookupAssetSubClassDTO.class));
		}
		return dtoList;
	}
	
	@Override
	public List<LookupTransactHoldingTypeDTO> getAllHoldingType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupTansactHoldingType> lookupTansactHoldingTypeModelList = lookupTransactHoldingTypeRepository.findAll();

			List<LookupTransactHoldingTypeDTO> lookupTransactHoldingTypeDTO = new ArrayList<LookupTransactHoldingTypeDTO>();

			for (LookupTansactHoldingType lookupTansactHoldingType : lookupTansactHoldingTypeModelList) {
				lookupTransactHoldingTypeDTO.add(mapper.map(lookupTansactHoldingType, LookupTransactHoldingTypeDTO.class));
			}
			return lookupTransactHoldingTypeDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupTransactKYCTypeDTO> getAllTransactKYCType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupTransactKYCType> lookupTransactKYCTypeModelList = lookupTransactKYCTypeRepository.findAll();

			List<LookupTransactKYCTypeDTO> lookupTransactKYCTypeDTO = new ArrayList<LookupTransactKYCTypeDTO>();

			for (LookupTransactKYCType lookupTransactKYCType : lookupTransactKYCTypeModelList) {
				lookupTransactKYCTypeDTO.add(mapper.map(lookupTransactKYCType, LookupTransactKYCTypeDTO.class));
			}
			return lookupTransactKYCTypeDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<LookupTransactUCCClientDefaultDPDTO> getAllTransactUCCClientDefaultDP() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupTransactUCCClientDefaultDP> lookupTransactUCCClientDefaultDPList = lookupTransactUCCClientDefaultDPRepository.findAll();

			List<LookupTransactUCCClientDefaultDPDTO> lookupTransactUCCClientDefaultDPDTOList = new ArrayList<LookupTransactUCCClientDefaultDPDTO>();

			for (LookupTransactUCCClientDefaultDP lookupTransactUCCClientDefaultDP : lookupTransactUCCClientDefaultDPList) {
				lookupTransactUCCClientDefaultDPDTOList.add(mapper.map(lookupTransactUCCClientDefaultDP, LookupTransactUCCClientDefaultDPDTO.class));
			}
			return lookupTransactUCCClientDefaultDPDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<LookupTransactUCCClientTypeDTO> getAllTransactUCCClientType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupTransactUCCClientType> lookupTransactUCCClientTypeList = lookupTransactUCCClientTypeRepository.findAll();

			List<LookupTransactUCCClientTypeDTO> lookupTransactUCCClientTypeDTOList = new ArrayList<LookupTransactUCCClientTypeDTO>();

			for (LookupTransactUCCClientType lookupTransactUCCClientType : lookupTransactUCCClientTypeList) {
				lookupTransactUCCClientTypeDTOList.add(mapper.map(lookupTransactUCCClientType, LookupTransactUCCClientTypeDTO.class));
			}
			return lookupTransactUCCClientTypeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
@Override
	public List<LookupRTABODTO> getAllRTABOType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupRTABO> lookupRTABOList = lookupRTABORepository.findAll();

			List<LookupRTABODTO> lookupRTABODTOList = new ArrayList<LookupRTABODTO>();

			for (LookupRTABO lookupRTABO : lookupRTABOList) {
				lookupRTABODTOList.add(mapper.map(lookupRTABO, LookupRTABODTO.class));
			}
			return lookupRTABODTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<LookupRTAFileNameDTO> getAllFileName() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupRTAFileName> lookupRTAFileNameList = lookupRTAFileNameRepository.findAll();

			List<LookupRTAFileNameDTO> lookupRTAFileNameDTOList = new ArrayList<LookupRTAFileNameDTO>();

			for (LookupRTAFileName lookupRTAFileNameOObj : lookupRTAFileNameList) {
				lookupRTAFileNameDTOList.add(mapper.map(lookupRTAFileNameOObj, LookupRTAFileNameDTO.class));
			}
			return lookupRTAFileNameDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupRoleDTO> getAllRoles() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupRole> roleList = lookupRoleRepository.findAll();
			List<LookupRoleDTO> lookupRoleDTOList = new ArrayList<LookupRoleDTO>();
			for (LookupRole lookupRole : roleList) {
				LookupRoleDTO  lookupRoleDTO = mapper.map(lookupRole, LookupRoleDTO.class);
				System.out.println("lookupRole.getSupervisorID() "+lookupRole.getSupervisorID());
				if(lookupRole.getSupervisorID() != null) {
				lookupRoleDTO.setSupervisorId(lookupRole.getSupervisorID().getId());
				lookupRoleDTO.setSupervisor(lookupRole.getSupervisorID().getDescription());
				}
				lookupRoleDTOList.add(lookupRoleDTO);
			}
			return lookupRoleDTOList;
		} /*catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}*/
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	@Override
	public List<LookupRoleDTO> getAllRolesAvailable(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			
			AdvisorUserRoleMapping advisorUserRoleMapping = advisorUserRoleMappingRepository
					.findByAdvisorUser(advisorUserRepository.findOne(userId));
			//System.out.println("advisorUserRoleMapping ID: " + advisorUserRoleMapping.getId() + "\n==================\n");
			AdvisorRole loggedAdvisorRole = advisorUserRoleMapping.getAdvisorRole();
			
			List<LookupRole> roleList = lookupRoleRepository.findAll();
			List<LookupRoleDTO> lookupRoleDTOList = new ArrayList<LookupRoleDTO>();
			
			for (LookupRole lookupRole : roleList) {
				
				
				LookupRoleDTO  lookupRoleDTO = mapper.map(lookupRole, LookupRoleDTO.class);
				System.out.println("lookupRole.getSupervisorID() "+lookupRole.getSupervisorID());
				if(lookupRole.getSupervisorID() != null) {
				lookupRoleDTO.setSupervisorId(lookupRole.getSupervisorID().getId());
				lookupRoleDTO.setSupervisor(lookupRole.getSupervisorID().getDescription());
				}
				lookupRoleDTOList.add(lookupRoleDTO);
			}
			return lookupRoleDTOList;
		} /*catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}*/
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	@Override
	public List<TransactionMISDTO> getAllARNByAdvisorId(int advisorId) {
		AdvisorUser advisorUser=advisorUserRepository.findById(advisorId);
		List<TransactionMISDTO>arnList=new ArrayList<>();
		List<ClientARNMapping> listOfARN=clientARNMappingRepository.findByAdvisorUser(advisorUser);
		TransactionMISDTO transactionMISDTO=new TransactionMISDTO();
		for(ClientARNMapping arn:listOfARN) {
			
			transactionMISDTO.setArn(arn.getArn());
			transactionMISDTO.setId(arn.getId());
			
		}
		arnList.add(transactionMISDTO);
		
		return arnList;
	}

}
