package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;

import com.finlabs.finexa.dto.FinexaBusinessSubmoduleDTO;
import com.finlabs.finexa.dto.FundSchemeIsinDTO;
import com.finlabs.finexa.dto.LookupAssetClassDTO;
import com.finlabs.finexa.dto.LookupAssetSubClassDTO;
import com.finlabs.finexa.dto.MasterAPYMonthlyPensionCorpusDTO;
import com.finlabs.finexa.dto.MasterCashDTO;
import com.finlabs.finexa.dto.MasterDirectEquityDTO;
import com.finlabs.finexa.dto.MasterDirectEquityForPCDTO;
import com.finlabs.finexa.dto.MasterEquityDailyPriceDTO;
import com.finlabs.finexa.dto.MasterFileMappingBODTO;
import com.finlabs.finexa.dto.MasterFundManagerDTO;
import com.finlabs.finexa.dto.MasterIndexNameDTO;
import com.finlabs.finexa.dto.MasterInsurancePolicyDTO;
import com.finlabs.finexa.dto.MasterKVPTermDTO;
import com.finlabs.finexa.dto.MasterMFProductRecommendationDTO;
import com.finlabs.finexa.dto.MasterMFRatingDTO;
import com.finlabs.finexa.dto.MasterMfSectorDTO;
import com.finlabs.finexa.dto.MasterMutualFundETFDTO;
import com.finlabs.finexa.dto.MasterProductClassificationDTO;
import com.finlabs.finexa.dto.MasterProductTypeDTO;
import com.finlabs.finexa.dto.MasterSectorBenchmarkMappingDTO;
import com.finlabs.finexa.dto.MasterStateDTO;
import com.finlabs.finexa.dto.MasterSubAssetClassReturnDTO;
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
import com.finlabs.finexa.exception.CustomFinexaException;

import jxl.write.WritableWorkbook;

public interface MasterService {

	public List<MasterMutualFundETFDTO> getAllMasterMutualFundETFDTO() throws RuntimeException;

	public List<MasterAPYMonthlyPensionCorpusDTO> getAllMasterAPYMonthlyPensionCorpusDTO();

	public List<LookupAssetClassDTO> getAllLookupAssetClassDTO();

	public List<LookupAssetSubClassDTO> getAllLookupAssetSubClassDTO();

	public List<MasterFundManagerDTO> getAllMasterFundManagerDTO();

	public List<MasterIndexNameDTO> getAllMasterIndexNameDTO();

	public List<MasterSubAssetClassReturnDTO> getAllMasterSubAssetClassReturnDTO();

	public List<MasterSectorBenchmarkMappingDTO> getAllMasterSectorBenchmarkMappingDTO();

	public List<MasterProductTypeDTO> getAllMasterProductTypeDTO();

	public List<MasterMFRatingDTO> getAllMasterMFRatingDTO();

	public List<MasterMFProductRecommendationDTO> getAllMasterMFProductRecommendationDTO();

	public List<LookupAssetSubClassDTO> getAllSubAssetClassDTO();

	public List<MasterProductClassificationDTO> getAllMasterProductClassificationDTO();

	public List<MasterMfSectorDTO> getAllMasterMfSectorDTO();

	public List<MasterCashDTO> getAllMasterCashDTO() throws RuntimeException;

	// public List<MasterCashDTO> getAllFromName(String name) throws
	// RuntimeException;

	public List<MasterDirectEquityDTO> getAllMasterDirectEquityDTO();

	public List<MasterEquityDailyPriceDTO> getAllMasterEquityDailyPriceDTO();

	public List<FundSchemeIsinDTO> getSchemesFromFund(String fundHouse, String status) throws RuntimeException;
	
	public List<FundSchemeIsinDTO> getSchemesFromFund(String fundHouse, String status, String matchString) throws RuntimeException;
	
	public List<FundSchemeIsinDTO> getSchemesFromFund(String fundHouse, String status, Pageable pageable) throws RuntimeException;

	public List<FundSchemeIsinDTO> getSchemesFromSelectedFund(String fundHouse, byte subAssetClassID,String status) throws RuntimeException;

	public List<MasterMutualFundETFDTO> getAllFromSchemes(String schemeName) throws RuntimeException;

	public List<MasterMutualFundETFDTO> getAllFromIsins(String isin) throws RuntimeException;

	public List<MasterEquityDailyPriceDTO> getAllFromStockNames(String stockName) throws RuntimeException;

	public List<MasterDirectEquityForPCDTO> equitySearchByStockName(String stockName) throws RuntimeException;

	public List<MasterEquityDailyPriceDTO> getAllFromStockNameAndIsin(String stockName, String isin)
			throws RuntimeException;

	public List<String> getAllFundHouseList(String status) throws RuntimeException;

	public List<String> getAllFundHouseListBySubAsset(byte subAssetClassId) throws RuntimeException;

	public List<String> getAllIsinFromMasterDirectEquity();

	public List<String> getAllDescriptionList();

	public List<String> getAllModuleDescriptionList();

	public List<FinexaBusinessSubmoduleDTO> getSubModuleFromModule(String moduleName);

	public BigDecimal getNSCInterestRateFromStartDate(Date startDate) throws RuntimeException, CustomFinexaException;

	public BigDecimal getKVPInterestRateFromStartDate(Date startDate) throws RuntimeException, CustomFinexaException;

	public Integer getKVPCompoundingFrequencyFromStartDate(Date startDate)
			throws RuntimeException, CustomFinexaException;

	public MasterKVPTermDTO getKVPTermFromStartDate(Date startDate) throws RuntimeException, CustomFinexaException;

	public BigDecimal getPOMISInterestRateFromStartDate(Date startDate) throws RuntimeException, CustomFinexaException;

	public BigDecimal getSCSSInterestRateFromStartDate(Date startDate) throws RuntimeException, CustomFinexaException;

	public BigDecimal getPORDInterestRateFromStartDate(Date startDate) throws RuntimeException, CustomFinexaException;

	public BigDecimal getSukanyaInterestRateFromStartDate(Date startDate)
			throws RuntimeException, CustomFinexaException;

	public Set<String> getCloseEndedFlagFromScheme(String schemeName);

	public List<MasterInsurancePolicyDTO> getInsuranceCompanyByType(byte policyType, byte insurancePolicyType);

	// public List<MasterInsuranceCompanyName> getInsuranceCompanyName();

	public Set<String> getStockNameFromIsin(String isin);

	public int delete(int id);

	public List<String> getFunctionFromSubmodule(int subModuleID);

	public List<String> getEventFromSubFunction(String subFunction);

	public List<String> getSubEventFromEvent(String event);

	public List<String> getSubFunctionFromFunction(String function);

	public List<MasterProductClassificationDTO> getAssetForMaturityRenewal();

	public WritableWorkbook downloadErrorLog(String input, String selectedMaster, HttpServletResponse response)
			throws RuntimeException;

	List<MasterStateDTO> getAllMasterStateDTO() throws RuntimeException;

	public List<MasterTransactAccountTypeDTO> getAllTransactAccountType() throws RuntimeException;

	public List<MasterTransactTaxStatusTypeDTO> getAllTransactTaxStatusType() throws RuntimeException;

	public List<MasterTransactStateCodeDTO> getAllTransactStateCode() throws RuntimeException;

	public List<MasterTransactOccupationCodeDTO> getAllTransactOccupationCode() throws RuntimeException;

	public List<MasterTransactDivPayModeDTO> getAllTransactDivPayMode() throws RuntimeException;

	public List<MasterTransactCommunicationModeDTO> getAllTransactCommunicationMode() throws RuntimeException;

	public List<MasterTransactFatcaAddressTypeDTO> getAllTransactAddressType() throws RuntimeException;

	public List<MasterTransactCountryNationalityDTO> getAllTransactCountryNationality() throws RuntimeException;

	public List<MasterTransactIncomeTypeDTO> getAllTransactIncomeSlabType() throws RuntimeException;

	public List<MasterTransactSourceOfWealthDTO> getAllTransactSourceOfWealth() throws RuntimeException;
	/*public List<FundSchemeIsinDTO> getSchemeFromFund(String fundHouse,String status) throws RuntimeException;
	 */

	public List<MasterTransactIdentificationTypeDTO> getAllTransactIdentificationTypes() throws RuntimeException;

	public List<String> getAllBSEMFPhysicalSchemeTypes(int accessMode) throws RuntimeException;

	public List<String> getAllBSEMFPhysicalAmc(int accessMode) throws RuntimeException;

	public List<MasterTransactBSEMFPhysicalSchemeDTO> getPhysicalSchemeNamesByFilter(String amcCode, String schemeType, String mode, int accessMode, String transactionMode) throws RuntimeException;

	public MasterTransactBSEMFPhysicalSchemeDTO getPhysicalSchemeDetails(String uniqueNo, int accessMode) throws RuntimeException;

	public List<MasterTransactBSEMFPhysicalSchemeDTO> getAllBSEMFPhysicalSchemes() throws RuntimeException;

	public String getStateByCode(String stateCode) throws RuntimeException;
	
	public List<MasterFileMappingBODTO> getSpecificFileName(int rtaId, String fileCode) throws RuntimeException;
	
	public MasterFileMappingBODTO getFileExtensionByFileName(int fileId) throws RuntimeException;

	List<String> getAllFundHouseList(Pageable pageable, String status) throws RuntimeException;
	
	List<String> getAllFundHouseList(String status, String matchString) throws RuntimeException;


}
