package com.finlabs.finexa.service;

import java.util.List;

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
import com.finlabs.finexa.model.LookupAssetClass;

public interface LookupService {
	public List<LookupEducationalQualificationDTO> getAllEducationalQualification() throws RuntimeException;

	public List<LookupEmploymentTypeDTO> getAllEmploymentType() throws RuntimeException;

	public List<LookupResidentTypeDTO> getAllResidentType() throws RuntimeException;

	public List<LookupMaritalStatusDTO> getAllMartialStatus() throws RuntimeException;

	public List<LookupCountryDTO> getAllCountry();

	public List<LookupRelationsDTO> getAllRelations();

	public List<LookupAssetClassDTO> getAllFundCategory() throws RuntimeException;

	public List<LookupFundTypeDTO> getAllFundType();

	public List<LookupFundInvestmentModeDTO> getAllFundInvestmentMode() throws RuntimeException;

	public List<LookupGoalTypeDTO> getAllGoalType();

	public List<LookupFrequencyDTO> getAllFrequency() throws RuntimeException;

	public List<LookupGoalFrequencyDTO> getAllGoalFrequency() throws RuntimeException;

	public List<LookupLoanCategoryDTO> getAllLoanCategory() throws RuntimeException;

	public List<LookupAlternateInvestmentsAssetTypeDTO> getAllAssetType() throws RuntimeException;

	public List<LookupRealEstateTypeDTO> getAllRealEstateType() throws RuntimeException;

	public List<LookupCashBalanceTypeDTO> getAllCashBalanceType();

	public List<LookupLifeInsurancePolicyTypeDTO> getAllLifeInsurancePolicyType();

	public List<LookupMonthDTO> getAllMonth() throws RuntimeException;

	List<LookupIncomeExpenseDurationDTO> getAllYear() throws RuntimeException;

	public List<LookupBondTypeDTO> getAllBondType() throws RuntimeException;

	public List<LookupFixedDepositTypeDTO> getAllFixedDepositType() throws RuntimeException;

	public List<LookupTransactHoldingTypeDTO> getAllHoldingType() throws RuntimeException;
	
	public List<LookupTransactKYCTypeDTO> getAllTransactKYCType() throws RuntimeException;
	
	public List<LookupTransactUCCClientDefaultDPDTO> getAllTransactUCCClientDefaultDP() throws RuntimeException;
	
	public List<LookupTransactUCCClientTypeDTO> getAllTransactUCCClientType() throws RuntimeException;
	
	public List<LookupAnnuityTypeDTO> getAllAnnuityType();
	
	public List<LookupInsuranceTypeDTO> getAllInsuranceType();
	
	public List<LookupAssetSubClassDTO> getSubAssetByAssetForFunds(LookupAssetClass assetClass);
	
	public List<LookupRTABODTO> getAllRTABOType();
	
	public List<LookupRTAFileNameDTO> getAllFileName();

	public List<LookupRoleDTO> getAllRoles() throws RuntimeException;

	List<LookupRoleDTO> getAllRolesAvailable(int userId) throws RuntimeException;

	public List<TransactionMISDTO> getAllARNByAdvisorId(int advisorId);

}
