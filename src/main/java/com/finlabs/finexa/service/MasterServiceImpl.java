package com.finlabs.finexa.service;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

/*import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;*/
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.finlabs.finexa.dto.FinexaBusinessModuleDTO;
import com.finlabs.finexa.dto.FinexaBusinessSubmoduleDTO;
import com.finlabs.finexa.dto.FundSchemeIsinDTO;
import com.finlabs.finexa.dto.LookupAssetClassDTO;
import com.finlabs.finexa.dto.LookupAssetSubClassDTO;
import com.finlabs.finexa.dto.LookupTransactKYCTypeDTO;
import com.finlabs.finexa.dto.MasterAIFDTO;
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
import com.finlabs.finexa.dto.MasterPMSDTO;
import com.finlabs.finexa.dto.MasterPincodeDTO;
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
import com.finlabs.finexa.model.FinexaBusinessFunction;
import com.finlabs.finexa.model.FinexaBusinessModule;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.LookupAssetClass;
import com.finlabs.finexa.model.LookupAssetSubClass;
import com.finlabs.finexa.model.LookupRTAFileName;
import com.finlabs.finexa.model.LookupRTAMasterFileDetailsBO;
import com.finlabs.finexa.model.LookupTransactKYCType;
import com.finlabs.finexa.model.MasterAPYMonthlyPensionCorpus;
import com.finlabs.finexa.model.MasterCash;
import com.finlabs.finexa.model.MasterDirectEquity;
import com.finlabs.finexa.model.MasterEquityDailyPrice;
import com.finlabs.finexa.model.MasterFileMappingBO;
import com.finlabs.finexa.model.MasterFundManager;
import com.finlabs.finexa.model.MasterIndexName;
import com.finlabs.finexa.model.MasterInsurancePolicy;
import com.finlabs.finexa.model.MasterKVPInterestRate;
import com.finlabs.finexa.model.MasterKVPCompoundingFrequency;
import com.finlabs.finexa.model.MasterKVPTerm;
import com.finlabs.finexa.model.MasterMFProductRecommendation;
import com.finlabs.finexa.model.MasterMFRating;
import com.finlabs.finexa.model.MasterMFSector;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.MasterNSCInterestRate;
import com.finlabs.finexa.model.MasterPOMI;
import com.finlabs.finexa.model.MasterPORD;
import com.finlabs.finexa.model.MasterPincode;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.model.MasterProductType;
import com.finlabs.finexa.model.MasterSCSSInterestRate;
import com.finlabs.finexa.model.MasterSectorBenchmarkMapping;
import com.finlabs.finexa.model.MasterState;
import com.finlabs.finexa.model.MasterSubAssetClassReturn;
import com.finlabs.finexa.model.MasterSukanyaSamriddhiInterestRate;
import com.finlabs.finexa.model.MasterTransactAccountType;
import com.finlabs.finexa.model.MasterTransactAddressType;
import com.finlabs.finexa.model.MasterTransactBSEMFPhysicalScheme;
import com.finlabs.finexa.model.MasterTransactBSEMFPhysicalSchemeLive;
import com.finlabs.finexa.model.MasterTransactCommunicationMode;
import com.finlabs.finexa.model.MasterTransactCountryNationality;
import com.finlabs.finexa.model.MasterTransactDivPayMode;
import com.finlabs.finexa.model.MasterTransactFatcaAddressType;
import com.finlabs.finexa.model.MasterTransactIdentificationType;
import com.finlabs.finexa.model.MasterTransactIncome;
import com.finlabs.finexa.model.MasterTransactOccupationCode;
import com.finlabs.finexa.model.MasterTransactSourceOfWealth;
import com.finlabs.finexa.model.MasterTransactStateCode;
import com.finlabs.finexa.model.MasterTransactTaxStatus;
import com.finlabs.finexa.repository.FinexaBusinessFunctionRepository;
import com.finlabs.finexa.repository.FinexaBusinessModuleRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.LookupAssetClassRepository;
import com.finlabs.finexa.repository.LookupAssetSubClassRepository;
import com.finlabs.finexa.repository.LookupRTAFileNameRepository;
import com.finlabs.finexa.repository.LookupRTAMasterFileDetailsBORepository;
import com.finlabs.finexa.repository.MasterAPYMonthlyPensionCorpusRepository;
import com.finlabs.finexa.repository.MasterCashRepository;
import com.finlabs.finexa.repository.MasterDirectEquityRepository;
import com.finlabs.finexa.repository.MasterEquityDailyPriceRepository;
import com.finlabs.finexa.repository.MasterFileMappingBORepository;
import com.finlabs.finexa.repository.MasterFundManagerRepository;
import com.finlabs.finexa.repository.MasterIndexNameRepository;
import com.finlabs.finexa.repository.MasterInsurancePolicyRepository;
import com.finlabs.finexa.repository.MasterKVPInterestRateRepository;
import com.finlabs.finexa.repository.MasterKVPCompoundingFrequencyRepository;
import com.finlabs.finexa.repository.MasterKVPTermRepository;
import com.finlabs.finexa.repository.MasterMFProductRecommendationRepository;
import com.finlabs.finexa.repository.MasterMFRatingRepository;
import com.finlabs.finexa.repository.MasterMfSectorRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.MasterNSCInterestRateRepository;
import com.finlabs.finexa.repository.MasterPOMISRepository;
import com.finlabs.finexa.repository.MasterPORDRepository;
import com.finlabs.finexa.repository.MasterPincodeRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.repository.MasterProductTypeRepository;
import com.finlabs.finexa.repository.MasterSCSSInterestRateRepository;
import com.finlabs.finexa.repository.MasterSectorBenchmarkMappingRepository;
import com.finlabs.finexa.repository.MasterStateRepository;
import com.finlabs.finexa.repository.MasterSubAssetClassReturnRepository;
import com.finlabs.finexa.repository.MasterSukanyaSamriddhiInterestRateRepository;
import com.finlabs.finexa.repository.MasterTablesUploadHistoryRepository;
import com.finlabs.finexa.repository.MasterTransactAccountTypeRepository;
import com.finlabs.finexa.repository.MasterTransactBSEMFPhysicalRepository;
import com.finlabs.finexa.repository.MasterTransactBSEMFPhysicalRepositoryLive;
import com.finlabs.finexa.repository.MasterTransactCommunicationModeRepository;
import com.finlabs.finexa.repository.MasterTransactCountryNationalityRepository;
import com.finlabs.finexa.repository.MasterTransactDivPayModeRepository;
import com.finlabs.finexa.repository.MasterTransactFatcaAddressTypeRepository;
import com.finlabs.finexa.repository.MasterTransactIdentificationTypeRepository;
import com.finlabs.finexa.repository.MasterTransactIncomeTypeRepository;
import com.finlabs.finexa.repository.MasterTransactOccupationCodeRepository;
import com.finlabs.finexa.repository.MasterTransactSourceOfWealthRepository;
import com.finlabs.finexa.repository.MasterTransactStateCodeRepository;
import com.finlabs.finexa.repository.MasterTransactTaxStatusRepository;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.MFTransactConstant;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Service("MasterService")
@Transactional
public class MasterServiceImpl implements MasterService {

	private static Logger log = LoggerFactory.getLogger(MasterServiceImpl.class);

	@Autowired
	private MasterMutualFundETFRepository masterMFETFRepository;
	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;
	@Autowired
	private MasterCashRepository masterCashRepository;
	@Autowired
	private MasterDirectEquityRepository masterDirectEquityRepository;
	@Autowired
	private MasterEquityDailyPriceRepository masterEquityDailyPriceRepository;
	@Autowired
	private MasterMfSectorRepository masterMfSectorRepository;
	@Autowired
	private MasterNSCInterestRateRepository masterNSCInterestRateRepository;
	@Autowired
	private MasterKVPInterestRateRepository masterKVPInterestRateRepository;
	@Autowired
	private MasterKVPCompoundingFrequencyRepository masterKVPCompoundingFrequencyRepository;
	@Autowired
	private MasterKVPTermRepository masterKVPTermRepository;
	@Autowired
	private MasterPOMISRepository masterPOMISRepository;
	@Autowired
	private MasterSCSSInterestRateRepository masterSCSSInterestRateRepository;
	@Autowired
	private MasterPORDRepository masterPORDRepository;
	@Autowired
	private MasterSukanyaSamriddhiInterestRateRepository masterSukanyaInterestRateRepository;
	@Autowired
	private MasterInsurancePolicyRepository masterInsurancePolicyRepository;
	@Autowired
	private LookupAssetClassRepository lookupAssetRepository;
	@Autowired
	private LookupAssetSubClassRepository lookupAssetSubClassrepository;
	@Autowired
	private MasterFundManagerRepository masterFundManagerRepository;
	@Autowired
	private MasterIndexNameRepository masterIndexNameRepository;
	@Autowired
	private MasterStateRepository masterStateRepository;
	@Autowired
	private MasterPincodeRepository masterPincodeRepository;
	@Autowired
	private FinexaBusinessFunctionRepository finexaBusinessFunctionRepository;
	@Autowired
	private MasterSubAssetClassReturnRepository masterSubAssetClassReturnRepository;
	@Autowired
	private MasterSectorBenchmarkMappingRepository masterSectorBenchmarkMappingRepository;
	@Autowired
	private MasterProductTypeRepository masterProductTypeRepository;
	@Autowired
	private MasterMFRatingRepository masterMFRatingRepository;
	@Autowired
	private MasterMFProductRecommendationRepository masterMFProductRecommendationRepository;
	@Autowired
	private MasterTablesUploadHistoryRepository masterTablesUploadHistoryRepository;
	@Autowired
	private FinexaBusinessModuleRepository finexaBusinessModuleRepository;
	@Autowired
	private FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	private MasterAPYMonthlyPensionCorpusRepository masterAPYMonthlyPensionCorpusRepository;
	
	@Autowired
	private MasterTransactAccountTypeRepository masterTransactAccountTypeRepository;
	
	@Autowired
	private MasterTransactTaxStatusRepository masterTransactTaxStatusRepository;
	
	@Autowired
	private MasterTransactStateCodeRepository masterTransactStateCodeRepository;
	
	@Autowired
	private MasterTransactFatcaAddressTypeRepository masterTransactFatcaAddressTypeRepository;
	
	@Autowired
	private MasterTransactOccupationCodeRepository masterTransactOccupationCodeRepository;
	
	@Autowired
	private MasterTransactDivPayModeRepository masterTransactDivPayModeRepository;
	
	@Autowired
	private MasterTransactCommunicationModeRepository masterTransactCommunicationModeRepository;
	
	@Autowired
	private MasterTransactCountryNationalityRepository masterTransactCountryNationalityRepository;
	
	@Autowired
	private MasterTransactIncomeTypeRepository masterTransactIncomeTypeRepository;
	
	@Autowired
	private MasterTransactSourceOfWealthRepository masterTransactSourceOfWealthRepository;
	
	@Autowired
	private MasterTransactIdentificationTypeRepository masterTransactIdentificationTypeRepository;
	
	@Autowired
	private MasterTransactBSEMFPhysicalRepository masterTransactBSEMFPhysicalRepository;
	
	@Autowired
	private MasterTransactBSEMFPhysicalRepositoryLive masterTransactBSEMFPhysicalRepositoryLive;
	
	@Autowired	
	private MasterFileMappingBORepository masterFileMappingBORepository;
	
	@Autowired
	private LookupRTAMasterFileDetailsBORepository lookupRTAMasterFileDetailsBORepository;
	
	@Autowired
	private LookupRTAFileNameRepository lookupRTAFileNameRepository;
	
	@Autowired
	Mapper mapper;
	
	@Override
	public List<MasterMutualFundETFDTO> getAllMasterMutualFundETFDTO() throws RuntimeException {
		log.debug("MasterServiceImpl >> getAllMasterMutualFundETFDTO()");
		log.debug("masterMFETFRepository >> findAll()");
		List<MasterMutualFundETF> masterMFETFList = masterMFETFRepository.findAll();
		log.debug("masterMFETFRepository << findAll()");
		List<MasterMutualFundETFDTO> mutualETFDTOList = new ArrayList<MasterMutualFundETFDTO>();
		for (MasterMutualFundETF masterMFETFObj : masterMFETFList) {
			mutualETFDTOList.add(mapper.map(masterMFETFObj, MasterMutualFundETFDTO.class));
		}
		log.debug("MasterServiceImpl << getAllMasterMutualFundETFDTO()");
		return mutualETFDTOList;
	}

	@Override
	public List<LookupAssetSubClassDTO> getAllSubAssetClassDTO() {
		List<LookupAssetSubClass> lookupAssetSubClassList = lookupAssetSubClassrepository.findAll();

		List<LookupAssetSubClassDTO> lookupAssetSubClassDTOList = new ArrayList<LookupAssetSubClassDTO>();
		for (LookupAssetSubClass obj : lookupAssetSubClassList) {
			lookupAssetSubClassDTOList.add(mapper.map(obj, LookupAssetSubClassDTO.class));
		}

		return lookupAssetSubClassDTOList;
	}

	@Override
	public List<MasterAPYMonthlyPensionCorpusDTO> getAllMasterAPYMonthlyPensionCorpusDTO() {
		// TODO Auto-generated method stub
		List<MasterAPYMonthlyPensionCorpus> masterAPYMonthlyPensionCorpusList = masterAPYMonthlyPensionCorpusRepository
				.findAll();

		List<MasterAPYMonthlyPensionCorpusDTO> masterAPYMonthlyPensionCorpusDTOList = new ArrayList<MasterAPYMonthlyPensionCorpusDTO>();
		for (MasterAPYMonthlyPensionCorpus obj : masterAPYMonthlyPensionCorpusList) {
			masterAPYMonthlyPensionCorpusDTOList.add(mapper.map(obj, MasterAPYMonthlyPensionCorpusDTO.class));
		}
		return masterAPYMonthlyPensionCorpusDTOList;
	}


	@Override
	public List<MasterStateDTO> getAllMasterStateDTO() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterState> masterStateList = masterStateRepository.findAll();

			List<MasterStateDTO> listState = new ArrayList<MasterStateDTO>();
			for (MasterState masterStateObj : masterStateList) {
				listState.add(mapper.map(masterStateObj, MasterStateDTO.class));
			}
			return listState;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	/*
	 * @Override public List<MasterPincodeDTO> getAllMasterPincodeDTO() { // TODO
	 * Auto-generated method stub List<MasterPincode> masterPincodeList =
	 * masterPincodeRepository.findAll();
	 * 
	 * List<MasterPincodeDTO> listPincode = new ArrayList<MasterPincodeDTO>(); for
	 * (MasterPincode masterPincodeObj : masterPincodeList) {
	 * listPincode.add(mapper.map(masterPincodeObj, MasterPincodeDTO.class)); }
	 * return listPincode; }
	 */

	@Override
	public List<MasterProductClassificationDTO> getAllMasterProductClassificationDTO() {
		// TODO Auto-generated method stub
		List<MasterProductClassification> masterProductClassificationList = masterProductClassificationRepository
				.findAll();

		List<MasterProductClassificationDTO> listProductClassification = new ArrayList<MasterProductClassificationDTO>();
		for (MasterProductClassification masterProductClassificationObj : masterProductClassificationList) {
			listProductClassification
					.add(mapper.map(masterProductClassificationObj, MasterProductClassificationDTO.class));
		}
		return listProductClassification;
	}

	@Override
	public List<MasterCashDTO> getAllMasterCashDTO() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterCash> masterCashList = masterCashRepository.findAll();
			List<MasterCashDTO> listCash = new ArrayList<MasterCashDTO>();
			for (MasterCash masterCash : masterCashList) {
				listCash.add(mapper.map(masterCash, MasterCashDTO.class));
			}
			return listCash;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MasterDirectEquityDTO> getAllMasterDirectEquityDTO() {
		// TODO Auto-generated method stub
		List<MasterDirectEquity> masterEquityList = masterDirectEquityRepository.findAll();

		List<MasterDirectEquityDTO> listEquity = new ArrayList<MasterDirectEquityDTO>();
		for (MasterDirectEquity masterDirectEquity : masterEquityList) {
			listEquity.add(mapper.map(masterDirectEquity, MasterDirectEquityDTO.class));
		}
		return listEquity;
	}

	@Override
	public List<MasterEquityDailyPriceDTO> getAllMasterEquityDailyPriceDTO() {

		List<MasterEquityDailyPrice> masterEquityDailyPriceList = masterEquityDailyPriceRepository.findAll();
		List<MasterEquityDailyPriceDTO> listEquityDailyPrice = new ArrayList<MasterEquityDailyPriceDTO>();

		for (MasterEquityDailyPrice masterEquityDailyPrice : masterEquityDailyPriceList) {
			listEquityDailyPrice.add(mapper.map(masterEquityDailyPrice, MasterEquityDailyPriceDTO.class));
		}
		return listEquityDailyPrice;
	}

	@Override
	public List<MasterMfSectorDTO> getAllMasterMfSectorDTO() {
		// TODO Auto-generated method stub
		List<MasterMFSector> masterMFSectorList = masterMfSectorRepository.findAll();

		List<MasterMfSectorDTO> listMFSectorList = new ArrayList<MasterMfSectorDTO>();
		for (MasterMFSector masterMFSector : masterMFSectorList) {
			listMFSectorList.add(mapper.map(masterMFSector, MasterMfSectorDTO.class));
		}
		return listMFSectorList;
	}

	@Override
	public List<FundSchemeIsinDTO> getSchemesFromFund(String fundHouse, String status) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			log.debug("Name = " + fundHouse);
			log.debug("Status = " + status);
			List<Object[]> list = masterMFETFRepository.findForSecondDropdown(fundHouse);
			List<FundSchemeIsinDTO> isinAndSchemes = new ArrayList<FundSchemeIsinDTO>();
			for (Object obj[] : list) {
				FundSchemeIsinDTO fundSchemeIsinDTO = new FundSchemeIsinDTO();
				String series = ((String)obj[16]);
				String schemeName = ((String)obj[2]);
				String descriptiveSchemeName = schemeName + "-" + series;
				
				fundSchemeIsinDTO.setIsin((String)obj[0]);
				fundSchemeIsinDTO.setSchemeName(schemeName);
				fundSchemeIsinDTO.setDescriptiveSchemeName(descriptiveSchemeName);
				fundSchemeIsinDTO.setAssetClassId((Byte)obj[4]);
				fundSchemeIsinDTO.setSubAssetClassId((Byte)obj[5]);
				//FundSchemeIsinDTO fundSchemeIsinDTO = mapper.map(masterMutualFundETF, FundSchemeIsinDTO.class);
				isinAndSchemes.add(fundSchemeIsinDTO);
			}
			log.debug("List : " + isinAndSchemes);
			// for sorting alphabetically
			isinAndSchemes.sort(Comparator.comparing(FundSchemeIsinDTO::getSchemeName));
			return isinAndSchemes;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<FundSchemeIsinDTO> getSchemesFromFund(String fundHouse, String status, Pageable pageable) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			log.debug("Name = " + fundHouse);
			log.debug("Status = " + status);
			List<Object[]> list = masterMFETFRepository.findForSecondDropdown(fundHouse, pageable);
			List<FundSchemeIsinDTO> isinAndSchemes = new ArrayList<FundSchemeIsinDTO>();
			for (Object obj[] : list) {
				FundSchemeIsinDTO fundSchemeIsinDTO = new FundSchemeIsinDTO();
				String series = ((String)obj[16]);
				String schemeName = ((String)obj[2]);
				String descriptiveSchemeName = schemeName + "-" + series;
				
				fundSchemeIsinDTO.setIsin((String)obj[0]);
				fundSchemeIsinDTO.setSchemeName(schemeName);
				fundSchemeIsinDTO.setDescriptiveSchemeName(descriptiveSchemeName);
				fundSchemeIsinDTO.setAssetClassId((Byte)obj[4]);
				fundSchemeIsinDTO.setSubAssetClassId((Byte)obj[5]);
				//FundSchemeIsinDTO fundSchemeIsinDTO = mapper.map(masterMutualFundETF, FundSchemeIsinDTO.class);
				isinAndSchemes.add(fundSchemeIsinDTO);
			}
			log.debug("List : " + isinAndSchemes);
			// for sorting alphabetically
			isinAndSchemes.sort(Comparator.comparing(FundSchemeIsinDTO::getSchemeName));
			return isinAndSchemes;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	
	/*@Override
	public List<FundSchemeIsinDTO> getSchemeFromFund(String fundHouse,String status) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			log.debug("Name = " + fundHouse);
			log.debug("Status = " + status);
			List<Object[]> list = masterMFETFRepository.findForSecondDropdown(fundHouse);
			List<FundSchemeIsinDTO> isinAndSchemes = new ArrayList<FundSchemeIsinDTO>();
			for (Object obj[] : list) {
				FundSchemeIsinDTO fundSchemeIsinDTO = new FundSchemeIsinDTO();
				String series = ((String)obj[16]);
				String schemeName = ((String)obj[2]);
				String descriptiveSchemeName = schemeName + "-" + series;
				
				fundSchemeIsinDTO.setIsin((String)obj[0]);
				fundSchemeIsinDTO.setSchemeName(schemeName);
				fundSchemeIsinDTO.setDescriptiveSchemeName(descriptiveSchemeName);
				fundSchemeIsinDTO.setAssetClassId((Byte)obj[4]);
				fundSchemeIsinDTO.setSubAssetClassId((Byte)obj[5]);
				
				//FundSchemeIsinDTO fundSchemeIsinDTO = mapper.map(masterMutualFundETF, FundSchemeIsinDTO.class);
				isinAndSchemes.add(fundSchemeIsinDTO);
			}
			log.debug("List : " + isinAndSchemes);
			// for sorting alphabetically
			isinAndSchemes.sort(Comparator.comparing(FundSchemeIsinDTO::getSchemeName));
			return isinAndSchemes;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}*/

	@Override
	public List<FundSchemeIsinDTO> getSchemesFromSelectedFund(String fundHouse, byte subAssetClassID,String status) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
/*			List<MasterMutualFundETF> list = masterMFETFRepository.findForSecondDropdown(fundHouse);
			List<FundSchemeIsinDTO> isinAndSchemes = new ArrayList<FundSchemeIsinDTO>();
			for (MasterMutualFundETF masterMutualFundETF : list) {
				FundSchemeIsinDTO fundSchemeIsinDTO = mapper.map(masterMutualFundETF, FundSchemeIsinDTO.class);
				if (masterMutualFundETF.getLookupAssetSubClass() != null) {
					fundSchemeIsinDTO.setSubAssetClassId(masterMutualFundETF.getLookupAssetSubClass().getId());
					isinAndSchemes.add(fundSchemeIsinDTO);
				}
			}*/
			//System.out.println("subAssetClassID "+subAssetClassID);
			List<Object[]> list = null;
			list = masterMFETFRepository.findForSecondDropdownList(fundHouse,subAssetClassID);
			List<FundSchemeIsinDTO> isinAndSchemes = new ArrayList<FundSchemeIsinDTO>();
			for (Object obj[] : list) {
				FundSchemeIsinDTO fundSchemeIsinDTO = new FundSchemeIsinDTO();
				String series = ((String)obj[16]);
				String schemeName = ((String)obj[2]);
				String descriptiveSchemeName = schemeName + "-" + series;
				
				fundSchemeIsinDTO.setIsin((String)obj[0]);
				fundSchemeIsinDTO.setSchemeName(schemeName);
				fundSchemeIsinDTO.setDescriptiveSchemeName(descriptiveSchemeName);
				
				fundSchemeIsinDTO.setAssetClassId((Byte)obj[4]);
				fundSchemeIsinDTO.setSubAssetClassId((Byte)obj[5]);
				//System.out.println("fundSchemeIsinDTO.setSubAssetClassId "+fundSchemeIsinDTO.getSubAssetClassId());
				
				//FundSchemeIsinDTO fundSchemeIsinDTO = mapper.map(masterMutualFundETF, FundSchemeIsinDTO.class);
				isinAndSchemes.add(fundSchemeIsinDTO);
			}

			return isinAndSchemes;
		} catch (RuntimeException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	@Override
	public List<FinexaBusinessSubmoduleDTO> getSubModuleFromModule(String moduleName) {
		List<FinexaBusinessSubmoduleDTO> businessSubModuleDTOList = new ArrayList<>();
		FinexaBusinessModule finexaBusinessModule = finexaBusinessModuleRepository.findByDescription(moduleName);
		List<FinexaBusinessSubmodule> finexaBusinessModuleList = finexaBusinessModule.getFinexaBusinessSubmodules();
		for (FinexaBusinessSubmodule obj : finexaBusinessModuleList) {
			FinexaBusinessSubmoduleDTO finexaBusinessSubModuleDTO = mapper.map(obj, FinexaBusinessSubmoduleDTO.class);
			finexaBusinessSubModuleDTO.setFinexaBusinessModule(mapper
					.map(finexaBusinessModuleRepository.findByDescription(moduleName), FinexaBusinessModuleDTO.class));
			businessSubModuleDTOList.add(finexaBusinessSubModuleDTO);
		}
		return businessSubModuleDTOList;
	}

	@Override
	public List<String> getFunctionFromSubmodule(int subModuleID) {
		Set<String> businessFunctionDTOList = new HashSet<>();
		// List<FinexaBusinessFunctionDTO> businessFunctionDTOList = new
		// ArrayList<>();
		FinexaBusinessSubmodule finexaBusinessSubmodule = finexaBusinessSubmoduleRepository.findOne(subModuleID);
		List<FinexaBusinessFunction> finexaBusinessFunctionList = finexaBusinessSubmodule.getFinexaBusinessFunctions();
		for (FinexaBusinessFunction obj : finexaBusinessFunctionList) {
			/*
			 * FinexaBusinessFunctionDTO dto = mapper.map(obj,
			 * FinexaBusinessFunctionDTO.class); dto.setFinexaBusinessSubmodule(
			 * mapper.map(finexaBusinessSubmoduleRepository.findOne(subModuleID) ,
			 * FinexaBusinessSubmoduleDTO.class));
			 */
			// dto.setSubModuleID(obj.getFinexaBusinessSubmodule().getId());
			businessFunctionDTOList.add(obj.getFunction());
		}

		// return businessFunctionDTOList;
		return businessFunctionDTOList.stream().collect(Collectors.toList());
	}

	@Override
	public List<String> getEventFromSubFunction(String subFunction) {
		Set<String> uniqueSet = new HashSet<String>();
		List<FinexaBusinessFunction> list = finexaBusinessFunctionRepository.findBySubFunction(subFunction);
		for (FinexaBusinessFunction obj : list) {
			uniqueSet.add(obj.getEvent());
		}
		return uniqueSet.stream().collect(Collectors.toList());
	}

	@Override
	public List<String> getSubEventFromEvent(String event) {
		List<FinexaBusinessFunction> list = finexaBusinessFunctionRepository.findByEvent(event);
		List<String> listSubEvents = new ArrayList<String>();
		for (FinexaBusinessFunction obj : list) {
			listSubEvents.add(obj.getSubEvent());
		}
		return listSubEvents;
	}

	@Override
	public List<String> getSubFunctionFromFunction(String function) {
		// TODO Auto-generated method stub
		Set<String> uniqueSet = new HashSet<String>();
		List<FinexaBusinessFunction> list = finexaBusinessFunctionRepository.findByFunction(function);
		for (FinexaBusinessFunction obj : list) {
			uniqueSet.add(obj.getSubFunction());
		}
		return uniqueSet.stream().collect(Collectors.toList());
	}

	@Override
	public List<MasterMutualFundETFDTO> getAllFromSchemes(String searchString) throws RuntimeException {

		try {
			List<MasterMutualFundETF> list = masterMFETFRepository.findBySchemeNameIgnoreCaseContaining(searchString);
			List<MasterMutualFundETFDTO> listAll = new ArrayList<MasterMutualFundETFDTO>();
			for (MasterMutualFundETF masterMutualFundETF : list) {
				MasterMutualFundETFDTO masterMutualFundETFDTO = mapper.map(masterMutualFundETF,
						MasterMutualFundETFDTO.class);
				masterMutualFundETFDTO.setIsin(masterMutualFundETF.getIsin());
				masterMutualFundETFDTO
						.setAssetClassID(Integer.valueOf(masterMutualFundETF.getLookupAssetClass().getId()));
				masterMutualFundETFDTO
						.setSubAssetClassID(Integer.valueOf(masterMutualFundETF.getLookupAssetSubClass().getId()));
				masterMutualFundETFDTO.setBenchmarkIndex(masterMutualFundETF.getMasterIndexName().getId());
				masterMutualFundETFDTO.setFundManagerCode(masterMutualFundETF.getMasterFundManager().getManagerCode());
				listAll.add(masterMutualFundETFDTO);
			}

			return listAll;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MasterMutualFundETFDTO> getAllFromIsins(String searchString) throws RuntimeException {

		try {
			MasterMutualFundETF masterMutualFundETF = masterMFETFRepository.findOne(searchString);
			List<MasterMutualFundETFDTO> listAll = new ArrayList<MasterMutualFundETFDTO>();

			MasterMutualFundETFDTO masterMutualFundETFDTO = mapper.map(masterMutualFundETF,
					MasterMutualFundETFDTO.class);
			masterMutualFundETFDTO.setIsin(masterMutualFundETF.getIsin());
			masterMutualFundETFDTO.setAssetClassID(masterMutualFundETF.getLookupAssetClass() == null ?null: Integer.valueOf(masterMutualFundETF.getLookupAssetClass().getId()));
			masterMutualFundETFDTO
					.setSubAssetClassID(masterMutualFundETF.getLookupAssetSubClass() == null ?null : Integer.valueOf(masterMutualFundETF.getLookupAssetSubClass().getId()));
			masterMutualFundETFDTO.setBenchmarkIndex(masterMutualFundETF.getMasterIndexName().getId());
			masterMutualFundETFDTO.setFundManagerCode(masterMutualFundETF.getMasterFundManager().getManagerCode());
			listAll.add(masterMutualFundETFDTO);
			return listAll;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MasterEquityDailyPriceDTO> getAllFromStockNames(String stockName) throws RuntimeException {

		try {
			List<MasterEquityDailyPrice> list = masterEquityDailyPriceRepository
					.findByStockNameIgnoreCaseContaining(stockName);
			List<MasterEquityDailyPriceDTO> listAll = new ArrayList<MasterEquityDailyPriceDTO>();
			for (MasterEquityDailyPrice masterEquityDailyPrice : list) {
				MasterEquityDailyPriceDTO masterEquityDailyPriceDTO = mapper.map(masterEquityDailyPrice,
						MasterEquityDailyPriceDTO.class);
				masterEquityDailyPriceDTO.setStockName(masterEquityDailyPrice.getStockName());
				masterEquityDailyPriceDTO.setIsin(masterEquityDailyPrice.getId().getMasterDirectEquity().getIsin());
				masterEquityDailyPriceDTO.setDate(masterEquityDailyPrice.getId().getDate());
				masterEquityDailyPriceDTO.setClosingPrice(masterEquityDailyPrice.getClosingPrice());
				listAll.add(masterEquityDailyPriceDTO);
			}

			return listAll;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MasterDirectEquityForPCDTO> equitySearchByStockName(String stockName) throws RuntimeException {

		try {
			List<MasterDirectEquity> list = masterDirectEquityRepository.findByStockNameIgnoreCaseContaining(stockName);
			List<MasterDirectEquityForPCDTO> listforPC = new ArrayList<MasterDirectEquityForPCDTO>();
			for (MasterDirectEquity masterDirectEquity : list) {
				MasterDirectEquityForPCDTO masterDirectEquityForPCDTO = mapper.map(masterDirectEquity,
						MasterDirectEquityForPCDTO.class);
				masterDirectEquityForPCDTO.setIsin(masterDirectEquity.getIsin());
				masterDirectEquityForPCDTO.setStockName(masterDirectEquity.getStockName());
				listforPC.add(masterDirectEquityForPCDTO);
			}

			return listforPC;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<MasterEquityDailyPriceDTO> getAllFromStockNameAndIsin(String stockName, String isin)
			throws RuntimeException {

		try {
			MasterDirectEquity mDE = masterDirectEquityRepository
					.findByStockNameIgnoreCaseContainingAndIsinIgnoreCaseContaining(stockName, isin.toUpperCase());
			List<MasterEquityDailyPrice> list = masterEquityDailyPriceRepository.findByIsin(mDE.getIsin());
			List<MasterEquityDailyPriceDTO> listAll = new ArrayList<MasterEquityDailyPriceDTO>();
			for (MasterEquityDailyPrice masterEquityDailyPrice : list) {
				MasterEquityDailyPriceDTO masterEquityDailyPriceDTO = mapper.map(masterEquityDailyPrice,
						MasterEquityDailyPriceDTO.class);
				masterEquityDailyPriceDTO.setStockName(masterEquityDailyPrice.getStockName());
				masterEquityDailyPriceDTO.setIsin(masterEquityDailyPrice.getId().getMasterDirectEquity().getIsin());
				masterEquityDailyPriceDTO.setDate(masterEquityDailyPrice.getId().getDate());
				masterEquityDailyPriceDTO.setClosingPrice(masterEquityDailyPrice.getClosingPrice());
				listAll.add(masterEquityDailyPriceDTO);
			}

			return listAll;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	/*
	 * @Override public List<MasterCashDTO> getAllFromName(String name) throws
	 * RuntimeException {
	 * 
	 * List<MasterCash> list =
	 * masterCashRepository.findByNameIgnoreCaseContaining(name);
	 * List<MasterCashDTO> listAll = new ArrayList<MasterCashDTO>(); for (MasterCash
	 * masterCash : list) { MasterCashDTO masterCashDTO = mapper.map(masterCash,
	 * MasterCashDTO.class); masterCashDTO.setName(name); } }
	 */
	@Override
	public List<String> getAllFundHouseList(String status) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			log.debug("MasterServiceImpl >> getAllFundHouseList()");
			Set<String> uniqueSet = new HashSet<String>();
			log.debug("masterMFETFRepository >> findByStatus()");
			// List<MasterMutualFundETF> list = masterMFETFRepository.findAll();
			List<MasterMutualFundETF> list = masterMFETFRepository.findForDropdown();
			log.debug("masterMFETFRepository << findByStatus()");
			for (MasterMutualFundETF obj : list) {
				uniqueSet.add(obj.getFundHouse());
			}
			log.debug("MasterServiceImpl << getAllFundHouseList()");
			// for sorting alphabetically
			return uniqueSet.stream().sorted().collect(Collectors.toList());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	//commented code for pagination, don't remove
//	@Override
//	public List<String> getAllFundHouseList(Pageable pageable, String status) throws RuntimeException {
//		// TODO Auto-generated method stub
//		try {
//			log.debug("MasterServiceImpl >> getAllFundHouseList()");
//			Set<String> uniqueSet = new HashSet<String>();
//			log.debug("masterMFETFRepository >> findByStatus()");
//			// List<MasterMutualFundETF> list = masterMFETFRepository.findAll();
//			List<MasterMutualFundETF> list = masterMFETFRepository.findForDropdown(pageable);
//			log.debug("masterMFETFRepository << findByStatus()");
//			for (MasterMutualFundETF obj : list) {
//				System.out.println("obj.getFundHouse() " + obj.getFundHouse());
//				uniqueSet.add(obj.getFundHouse());
//			}
//			log.debug("MasterServiceImpl << getAllFundHouseList()");
//			// for sorting alphabetically
//			return uniqueSet.stream().sorted().collect(Collectors.toList());
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//			throw new RuntimeException(e);
//		}
//	}
	
	@Override
	public List<String> getAllFundHouseList(Pageable pageable, String status) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<String> uniqueSet = new ArrayList <String>();
			// List<MasterMutualFundETF> list = masterMFETFRepository.findAll();
			List<String> uniqueFundName = masterMFETFRepository.findForDropdown(pageable);
			for (String str : uniqueFundName) {
				System.out.println("obj.getFundHouse() " + str);
				uniqueSet.add(str);
			}
			// for sorting alphabetically
			//return uniqueSet.stream().sorted().collect(Collectors.toList());
			return uniqueSet;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	//--------------------------------------------------------------------
	@Override
	public List<String> getAllFundHouseList(String status, String matchString) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			log.debug("MasterServiceImpl >> getAllFundHouseList()");
			Set<String> uniqueSet = new HashSet<String>();
			log.debug("masterMFETFRepository >> findByStatus()");
			// List<MasterMutualFundETF> list = masterMFETFRepository.findAll();
			List<MasterMutualFundETF> list = masterMFETFRepository.findForDropdown("%"+matchString+"%");
			log.debug("masterMFETFRepository << findByStatus()");
			for (MasterMutualFundETF obj : list) {
				uniqueSet.add(obj.getFundHouse());
			}
			log.debug("MasterServiceImpl << getAllFundHouseList()");
			// for sorting alphabetically
			return uniqueSet.stream().sorted().collect(Collectors.toList());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	//--------------------------------------------------------------------
	
	
	@Override
	public List<String> getAllFundHouseListBySubAsset(byte subAssetClassId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			Set<String> uniqueSet = new HashSet<String>();
			log.debug("masterMFETFRepository >> findByStatusAndSubAssetClass()");
			// List<MasterMutualFundETF> list = masterMFETFRepository.findAll();
			List<MasterMutualFundETF> list = masterMFETFRepository.findFundHouseBySubAssetClassId(subAssetClassId);
			log.debug("masterMFETFRepository << findByStatus()");
			for (MasterMutualFundETF obj : list) {
				uniqueSet.add(obj.getFundHouse());
			}
			log.debug("MasterServiceImpl << getAllFundHouseList()");
			// for sorting alphabetically
			return uniqueSet.stream().sorted().collect(Collectors.toList());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<String> getAllIsinFromMasterDirectEquity() {

		List<String> set = new ArrayList<String>();
		List<MasterDirectEquity> list = masterDirectEquityRepository.findAll();
		for (MasterDirectEquity obj : list) {
			set.add(obj.getIsin());
		}
		return set;
	}

	@Override
	public List<String> getAllDescriptionList() {

		List<String> set = new ArrayList<String>();
		List<LookupAssetSubClass> list = lookupAssetSubClassrepository.findAll();
		for (LookupAssetSubClass obj : list) {
			set.add(obj.getDescription());
		}

		return set;
	}

	@Override
	public List<String> getAllModuleDescriptionList() {

		List<String> set = new ArrayList<String>();
		List<FinexaBusinessModule> list = finexaBusinessModuleRepository.findAll();
		for (FinexaBusinessModule obj : list) {
			set.add(obj.getDescription());
		}
		return set;
	}

	@Override
	public BigDecimal getNSCInterestRateFromStartDate(Date startDate) throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		try {
			BigDecimal displayInterestRate = null;
			Boolean checkInterestRate = false;
			List<MasterNSCInterestRate> masterNSCList = masterNSCInterestRateRepository.findAll();
			for (MasterNSCInterestRate masterNSC : masterNSCList) {
				/*
				 * if ((startDate.after(masterNSC.getPeriodStart()) &&
				 * startDate.before(masterNSC.getPeriodEnd())) ||
				 * startDate.equals(masterNSC.getPeriodStart()) ||
				 * startDate.equals(masterNSC.getPeriodEnd())) { displayInterestRate =
				 * masterNSC.getInterestRate(); checkInterestRate = true; }
				 */
				/*
				 * else { if (startDate.before(masterNSC.getPeriodStart())) { throw new
				 * CustomFinexaException(FinexaConstant.MASTER_NSC_INTEREST_RATE_MODULE,
				 * FinexaConstant.MASTER_NSC_INTEREST_RATE_VIEW_ERROR,
				 * "Interest Rate Not Found"); } }
				 */

				/*if (startDate.equals(masterNSC.getPeriodStart()) || startDate.equals(masterNSC.getPeriodEnd())) {
					displayInterestRate = masterNSC.getInterestRate();
					checkInterestRate = true;
				} else {*/
					if (startDate.after(masterNSC.getPeriodStart()) && startDate.before(masterNSC.getPeriodEnd())) {
						displayInterestRate = masterNSC.getInterestRate();
						checkInterestRate = true;
					}
			/*	}*/

				if (displayInterestRate == null) {
					displayInterestRate = masterNSCList.get((masterNSCList.size() - 1)).getInterestRate();
				}

				if (checkInterestRate) {
					break;
				}
			}
			return displayInterestRate;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<String> getCloseEndedFlagFromScheme(String schemeName) {
		// TODO Auto-generated method stub
		List<MasterMutualFundETF> list = masterMFETFRepository.findCloseEndedFlagBySchemeName(schemeName);
		Set<String> flags = new HashSet<String>();

		for (MasterMutualFundETF obj : list) {
			flags.add(obj.getCloseEndedFlag());
		}

		return flags;
	}

	@Override
	public BigDecimal getKVPInterestRateFromStartDate(Date startDate) throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		try {
			BigDecimal displayInterestRate = null;
			Boolean checkInterestRate = false;
			List<MasterKVPInterestRate> masterKVPList = masterKVPInterestRateRepository.findAll();
			for (MasterKVPInterestRate masterKVP : masterKVPList) {
				/*
				 * if ((startDate.after(masterKVP.getDepositFromDate()) &&
				 * startDate.before(masterKVP.getDepositToDate())) ||
				 * startDate.equals(masterKVP.getDepositFromDate()) ||
				 * startDate.equals(masterKVP.getDepositToDate())) { displayInterestRate =
				 * masterKVP.getInterestRate(); checkInterestRate = true; }
				 */
				/*
				 * else { if (startDate.before(masterKVP.getDepositFromDate())) { throw new
				 * CustomFinexaException(FinexaConstant.MASTER_KVP_INTEREST_RATE_MODULE,
				 * FinexaConstant.MASTER_KVP_INTEREST_RATE_VIEW_ERROR,
				 * "Interest Rate Not Found"); } }
				 */

				/*if (startDate.equals(masterKVP.getDepositFromDate())
						|| startDate.equals(masterKVP.getDepositToDate())) {
					displayInterestRate = masterKVP.getInterestRate();
					checkInterestRate = true;
				} else {*/
					if (startDate.after(masterKVP.getDepositFromDate())
							&& startDate.before(masterKVP.getDepositToDate())) {
						displayInterestRate = masterKVP.getInterestRate();
						checkInterestRate = true;
					}
				/*}*/

				if (displayInterestRate == null) {
					displayInterestRate = masterKVPList.get((masterKVPList.size() - 1)).getInterestRate();
				}

				if (checkInterestRate) {
					break;
				}
			}
			return displayInterestRate;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public Integer getKVPCompoundingFrequencyFromStartDate(Date startDate)
			throws RuntimeException, CustomFinexaException {
		try {
			Integer compoundingFrequency = 0;
			Boolean checkFrequency = false;
			List<MasterKVPCompoundingFrequency> masterKVPList = masterKVPCompoundingFrequencyRepository.findAll();
			for (MasterKVPCompoundingFrequency masterKVP : masterKVPList) {
				if ((startDate.after(masterKVP.getDepositFromDate()) && startDate.before(masterKVP.getDepositToDate()))
						|| startDate.equals(masterKVP.getDepositFromDate())
						|| startDate.equals(masterKVP.getDepositToDate())) {
					compoundingFrequency = masterKVP.getCompoundingFrequency();
					checkFrequency = true;
				} else {
					if (startDate.before(masterKVP.getDepositFromDate())) {
						throw new CustomFinexaException(FinexaConstant.MASTER_KVP_COMPOUNDING_FREQUENCY_MODULE,
								FinexaConstant.MASTER_KVP_COMPOUNDING_FREQUENCY_VIEW_ERROR,
								"Compouding Frequency Not Found");
					} else {
						if (startDate.after(masterKVP.getDepositToDate())) {
							compoundingFrequency = masterKVP.getCompoundingFrequency();
						}
					}
				}

				if (checkFrequency) {
					break;
				}

			}
			return compoundingFrequency;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public MasterKVPTermDTO getKVPTermFromStartDate(Date startDate) throws RuntimeException, CustomFinexaException {
		try {
			Integer termM = 0;
			Integer termY = 0;
			MasterKVPTermDTO masterKVPTermDTO = new MasterKVPTermDTO();
			Boolean checkTerm = false;
			List<MasterKVPTerm> masterKVPList = masterKVPTermRepository.findAll();
			for (MasterKVPTerm masterKVP : masterKVPList) {
				if ((startDate.after(masterKVP.getDepositFromDate()) && startDate.before(masterKVP.getDepositToDate()))
						|| startDate.equals(masterKVP.getDepositFromDate())
						|| startDate.equals(masterKVP.getDepositToDate())) {
					masterKVPTermDTO.setDepositFromDate(masterKVP.getDepositFromDate());
					masterKVPTermDTO.setDepositToDate(masterKVP.getDepositToDate());
					termM = masterKVP.getTermMonths();
					masterKVPTermDTO.setTermMonths(termM);
					termY = masterKVP.getTermYears();
					masterKVPTermDTO.setTermYears(termY);
					checkTerm = true;
				} else {
					if (startDate.before(masterKVP.getDepositFromDate())) {
						throw new CustomFinexaException(FinexaConstant.MASTER_KVP_TERM_MODULE,
								FinexaConstant.MASTER_KVP_TERM_VIEW_ERROR, "Term Not Found");
					} else {
						if (startDate.after(masterKVP.getDepositFromDate())) {
							termM = masterKVP.getTermMonths();
							masterKVPTermDTO.setTermMonths(termM);
							termY = masterKVP.getTermYears();
							masterKVPTermDTO.setTermYears(termY);
						}
					}
				}
				if (checkTerm) {
					break;
				}
			}
			return masterKVPTermDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public BigDecimal getPOMISInterestRateFromStartDate(Date startDate) throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		try {
			BigDecimal displayInterestRate = null;
			Boolean checkInterestRate = false;
			List<MasterPOMI> masteRPOMISList = masterPOMISRepository.findAll();
			for (MasterPOMI masterPOMIS : masteRPOMISList) {
				/*
				 * if ((startDate.after(masterPOMIS.getDepositDateFrom()) &&
				 * startDate.before(masterPOMIS.getDepositDateTo())) ||
				 * startDate.equals(masterPOMIS.getDepositDateFrom()) ||
				 * startDate.equals(masterPOMIS.getDepositDateTo())) { displayInterestRate =
				 * masterPOMIS.getInterestRate(); checkInterestRate = true; }
				 */
				/*
				 * else { if (startDate.before(masterPOMIS.getDepositDateFrom())) { throw new
				 * CustomFinexaException(FinexaConstant.MASTER_POMIS_MODULE,
				 * FinexaConstant.MASTER_POMIS_VIEW_ERROR, "Interest Rate Not Found"); } }
				 */

				/*if (startDate.equals(masterPOMIS.getDepositDateFrom())
						|| startDate.equals(masterPOMIS.getDepositDateTo())) {
					displayInterestRate = masterPOMIS.getInterestRate();
					checkInterestRate = true;
				} else {*/
					if (startDate.after(masterPOMIS.getDepositDateFrom())
							&& startDate.before(masterPOMIS.getDepositDateTo())) {
						displayInterestRate = masterPOMIS.getInterestRate();
						checkInterestRate = true;
					}
				/*}*/

				if (displayInterestRate == null) {
					displayInterestRate = masteRPOMISList.get((masteRPOMISList.size() - 1)).getInterestRate();
				}

				if (checkInterestRate) {
					break;
				}
			}
			return displayInterestRate;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public BigDecimal getSCSSInterestRateFromStartDate(Date startDate) throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		try {
			BigDecimal displayInterestRate = null;
			Boolean checkInterestRate = false;
			List<MasterSCSSInterestRate> masterSCSSList = masterSCSSInterestRateRepository.findAll();
			for (MasterSCSSInterestRate masterSCSS : masterSCSSList) {
				/*
				 * if ((startDate.after(masterSCSS.getDepositDateFrom()) &&
				 * startDate.before(masterSCSS.getDepositDateTo())) ||
				 * startDate.equals(masterSCSS.getDepositDateFrom()) ||
				 * startDate.equals(masterSCSS.getDepositDateTo())) { displayInterestRate =
				 * masterSCSS.getInterestRate(); checkInterestRate = true; }
				 */
				/*
				 * else { if (startDate.before(masterSCSS.getDepositDateFrom())) { throw new
				 * CustomFinexaException(FinexaConstant.MASTER_SCSS_INTEREST_RATE_MODULE,
				 * FinexaConstant.MASTER_SCSS_INTEREST_RATE_VIEW_ERROR,
				 * "Interest Rate Not Found"); } }
				 */

			/*	if (startDate.equals(masterSCSS.getDepositDateFrom())
						|| startDate.equals(masterSCSS.getDepositDateTo())) {
					displayInterestRate = masterSCSS.getInterestRate();
					checkInterestRate = true;
				} else {*/
					if (startDate.after(masterSCSS.getDepositDateFrom())
							&& startDate.before(masterSCSS.getDepositDateTo())) {
						displayInterestRate = masterSCSS.getInterestRate();
						checkInterestRate = true;
					}
				/*}*/

				if (displayInterestRate == null) {
					displayInterestRate = masterSCSSList.get((masterSCSSList.size() - 1)).getInterestRate();
				}

				if (checkInterestRate) {
					break;
				}
			}
			return displayInterestRate;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public BigDecimal getPORDInterestRateFromStartDate(Date startDate) throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		try {
			BigDecimal displayInterestRate = null;
			Boolean checkInterestRate = false;
			List<MasterPORD> masterPORDList = masterPORDRepository.findAll();
			for (MasterPORD masterPORD : masterPORDList) {
				/*
				 * if ((startDate.after(masterPORD.getDepositDateFrom()) &&
				 * startDate.before(masterPORD.getDepositDateTo())) ||
				 * startDate.equals(masterPORD.getDepositDateFrom()) ||
				 * startDate.equals(masterPORD.getDepositDateTo())) { displayInterestRate =
				 * masterPORD.getInterestRate(); checkInterestRate = true; }
				 */
				/*
				 * else { if (startDate.before(masterPORD.getDepositDateFrom())) { throw new
				 * CustomFinexaException(FinexaConstant.MASTER_PORD_MODULE,
				 * FinexaConstant.MASTER_PORD_VIEW_ERROR, "Interest Rate Not Found"); } }
				 */

				/*if (startDate.equals(masterPORD.getDepositDateFrom())
						|| startDate.equals(masterPORD.getDepositDateTo())) {
					displayInterestRate = masterPORD.getInterestRate();
					checkInterestRate = true;
				} else {*/
					if (startDate.after(masterPORD.getDepositDateFrom())
							&& startDate.before(masterPORD.getDepositDateTo())) {
						displayInterestRate = masterPORD.getInterestRate();
						checkInterestRate = true;
					}
				/*}*/

				if (displayInterestRate == null) {
					displayInterestRate = masterPORDList.get((masterPORDList.size() - 1)).getInterestRate();
				}

				if (checkInterestRate) {
					break;
				}
			}
			return displayInterestRate;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public BigDecimal getSukanyaInterestRateFromStartDate(Date startDate)
			throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		try {
			BigDecimal displayInterestRate = null;
			Boolean checkInterestRate = false;
			List<MasterSukanyaSamriddhiInterestRate> masterSukanyaList = masterSukanyaInterestRateRepository.findAll();
			for (MasterSukanyaSamriddhiInterestRate masterSukanya : masterSukanyaList) {
				/*
				 * if ((startDate.after(masterSukanya.getStartDate()) &&
				 * startDate.before(masterSukanya.getEndDate())) ||
				 * startDate.equals(masterSukanya.getStartDate()) ||
				 * startDate.equals(masterSukanya.getEndDate())) { displayInterestRate =
				 * masterSukanya.getInterestRate(); checkInterestRate = true; }
				 */
				/*
				 * else { if (startDate.before(masterSukanya.getStartDate())) { throw new
				 * CustomFinexaException(FinexaConstant.
				 * MASTER_SUKANYA_SAMRIDDHI_INTEREST_RATE_MODULE,
				 * FinexaConstant.MASTER_SUKANYA_SAMRIDDHI_INTEREST_RATE_VIEW_ERROR,
				 * "Interest Rate Not Found"); } }
				 */

				/*if (startDate.equals(masterSukanya.getStartDate()) || startDate.equals(masterSukanya.getEndDate())) {
					displayInterestRate = masterSukanya.getInterestRate();
					checkInterestRate = true;
				} else {*/
					if (startDate.after(masterSukanya.getStartDate())
							&& startDate.before(masterSukanya.getEndDate())) {
						displayInterestRate = masterSukanya.getInterestRate();
						checkInterestRate = true;
					}
				/*}*/

				if (displayInterestRate == null) {
					displayInterestRate = masterSukanyaList.get((masterSukanyaList.size() - 1)).getInterestRate();
				}

				if (checkInterestRate) {
					break;
				}
			}
			return displayInterestRate;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MasterInsurancePolicyDTO> getInsuranceCompanyByType(byte policyType, byte insurancePolicyType) {
		List<MasterInsurancePolicy> masterInsurancePolicyList = masterInsurancePolicyRepository
				.getInsuranceCompanyForPolicyId(policyType, insurancePolicyType);

		List<MasterInsurancePolicyDTO> masterInsurancePolicyDTOList = new ArrayList<MasterInsurancePolicyDTO>();

		for (MasterInsurancePolicy masterInsurancePolicy : masterInsurancePolicyList) {
			MasterInsurancePolicyDTO masterInsurancePolicyDTO = new MasterInsurancePolicyDTO();
			masterInsurancePolicyDTO.setId(masterInsurancePolicy.getMasterInsuranceCompanyName().getId());
			masterInsurancePolicyDTO
					.setCompanyName(masterInsurancePolicy.getMasterInsuranceCompanyName().getDescription());
			masterInsurancePolicyDTOList.add(masterInsurancePolicyDTO);
		}

		return masterInsurancePolicyDTOList;
	}

	@Override
	public Set<String> getStockNameFromIsin(String isin) {
		// TODO Auto-generated method stub
		List<MasterDirectEquity> stockName = masterDirectEquityRepository.findSecurityNameByIsin(isin);
		Set<String> flags = new HashSet<String>();

		for (MasterDirectEquity obj : stockName) {
			flags.add(obj.getStockName());
		}

		return flags;
	}

	/*
	 * @Override public boolean uploadMasterMutualFundETF(FileuploadDTO
	 * fileUploadDTO) { // TODO Auto-generated method stub MultipartFile file =
	 * fileUploadDTO.getFile()[0];
	 * 
	 * log.debug("file name : " + file.getName()); log.debug("file name : " +
	 * file.getOriginalFilename()); boolean success = true; jxl.Workbook workbook =
	 * null; try { workbook = jxl.Workbook.getWorkbook(file.getInputStream());
	 * jxl.Sheet sheet = workbook.getSheet(0); for (int rownum = 1; rownum <
	 * sheet.getRows(); rownum++) { MasterMutualFundETF masterMutualFundETF = new
	 * MasterMutualFundETF(); masterMutualFundETF.setFundHouse(sheet.getCell(0,
	 * rownum).getContents()); masterMutualFundETF.setSchemeName(sheet.getCell(1,
	 * rownum).getContents()); masterMutualFundETF.setAmfiCode(sheet.getCell(2,
	 * rownum).getContents()); masterMutualFundETF.setIsin(sheet.getCell(3,
	 * rownum).getContents()); LookupAssetClass lookupAssetClass =
	 * lookupAssetRepository.findById(Byte.parseByte(sheet.getCell(4,
	 * rownum).getContents())); if(lookupAssetClass==null){ try { throw new
	 * Exception("LookupAssetClass id missmatch in this Row"); } catch (Exception e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); } }
	 * masterMutualFundETF.setLookupAssetClass(lookupAssetClass);
	 * LookupAssetSubClass lookupAssetSubClass =
	 * lookupAssetSubClassrepository.findById(Byte.parseByte(sheet.getCell(5,
	 * rownum).getContents())); if(lookupAssetSubClass==null){ try { throw new
	 * Exception("LookupAssetSubClass id missmatch in this Row"); } catch (Exception
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } }
	 * masterMutualFundETF.setLookupAssetSubClass(lookupAssetSubClass);
	 * masterMutualFundETF.setSchemeOption(sheet.getCell(6, rownum).getContents());
	 * masterMutualFundETF.setCloseEndedFlag(sheet.getCell(7,
	 * rownum).getContents());
	 * masterMutualFundETF.setSchemeInceptionDate(FinexaUtil.parseDate(sheet.
	 * getCell(8, rownum).getContents()));
	 * masterMutualFundETF.setRegularDirectFlag(sheet.getCell(9,
	 * rownum).getContents());
	 * masterMutualFundETF.setSchemeEndDate(FinexaUtil.parseDate(sheet.getCell( 10,
	 * rownum).getContents()));
	 * masterMutualFundETF.setExitLoadAndPeriod(sheet.getCell(11,
	 * rownum).getContents());
	 * masterMutualFundETF.setMinInvestmentAmount(Short.parseShort(sheet.getCell
	 * (12, rownum).getContents())); MasterFundManager masterFundManager =
	 * masterFundManagerRepository.findByManagerCode(Integer.parseInt(sheet.
	 * getCell(13, rownum).getContents())); if(masterFundManager==null){ try { throw
	 * new Exception("MasterFundManager id missmatch in this Row"); } catch
	 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); } }
	 * masterMutualFundETF.setMasterFundManager(masterFundManager); MasterIndexName
	 * masterIndexName =
	 * masterIndexNameRepository.findById(Integer.parseInt(sheet.getCell(14,
	 * rownum).getContents())); if(masterIndexName==null){ try { throw new
	 * Exception("MasterIndexName id missmatch in this Row"); } catch (Exception e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); } }
	 * masterMutualFundETF.setMasterIndexName(masterIndexName); MasterMutualFundETF
	 * existingRecordsCheck =
	 * masterMutualFundETFRepository.findByAmfiCode(sheet.getCell(2,
	 * rownum).getContents()); if (existingRecordsCheck != null) {
	 * masterMutualFundETF.setAmfiCode(existingRecordsCheck.getAmfiCode()); }
	 * masterMutualFundETFRepository.save(masterMutualFundETF); }
	 * 
	 * } catch (IOException e) { success = false; e.printStackTrace(); } catch
	 * (BiffException e) { success = false; e.printStackTrace(); } finally { if
	 * (workbook != null) { workbook.close(); } } return success;
	 * 
	 * 
	 * }
	 */

	/*
	 * @Override public boolean uploadMasterProductType(FileuploadDTO fileUploadDTO)
	 * { MultipartFile file = fileUploadDTO.getFile()[0];
	 * 
	 * log.debug("file name : " + file.getName()); log.debug("file name : " +
	 * file.getOriginalFilename()); boolean success = true; jxl.Workbook workbook =
	 * null; try { workbook = jxl.Workbook.getWorkbook(file.getInputStream());
	 * jxl.Sheet sheet = workbook.getSheet(0); for (int rownum = 1; rownum <
	 * sheet.getRows(); rownum++) { MasterProductType masterProductType = new
	 * MasterProductType(); masterProductType.setId(Byte.parseByte(sheet.getCell(0,
	 * rownum).getContents())); masterProductType.setProductType(sheet.getCell(1,
	 * rownum).getContents()); MasterProductType existingRecordsCheck =
	 * masterProductTypeRepository.findOne(Byte.parseByte(sheet.getCell(0,
	 * rownum).getContents())); if (existingRecordsCheck != null) {
	 * masterProductType.setId(existingRecordsCheck.getId()); }
	 * masterProductTypeRepository.save(masterProductType); }
	 * 
	 * } catch (IOException e) { success = false; e.printStackTrace(); } catch
	 * (BiffException e) { success = false; e.printStackTrace(); } finally { if
	 * (workbook != null) { workbook.close(); } } return success; }
	 */

	/*
	 * @SuppressWarnings("static-access")
	 * 
	 * @Override public boolean uploadMasterProductType(FileuploadDTO fileUploadDTO)
	 * { // TODO Auto-generated method stub MultipartFile file =
	 * fileUploadDTO.getFile()[0]; log.debug("file name : " + file.getName());
	 * log.debug("file name : " + file.getOriginalFilename()); boolean success =
	 * true; org.apache.poi.ss.usermodel.Workbook workbook = null; try { int i = 0;
	 * workbook = new WorkbookFactory().create(file.getInputStream());
	 * org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0); Workbook
	 * workbook2 = new HSSFWorkbook(); Sheet sheet2 =
	 * workbook2.createSheet("masterProductType"); // Create backup Excel file with
	 * header sheet2.createRow(i);
	 * sheet2.getRow(i).createCell(0).setCellValue(sheet.getRow(0).getCell(0).
	 * getStringCellValue());
	 * sheet2.getRow(i).createCell(1).setCellValue(sheet.getRow(0).getCell(1).
	 * getStringCellValue());
	 * sheet2.getRow(i).createCell(2).setCellValue("Remarks"); // Read existing
	 * Excel file log.debug("physical number of rows: " +
	 * sheet.getPhysicalNumberOfRows()); int totalRows =
	 * sheet.getPhysicalNumberOfRows(); MasterTablesUploadHistoryDTO dto = new
	 * MasterTablesUploadHistoryDTO(); dto.setRecordsUploaded(totalRows); for (int
	 * rownum = 1; rownum < sheet.getPhysicalNumberOfRows(); rownum++) { try{
	 * MasterProductType masterProductType = new MasterProductType();
	 * log.debug("rownum: " + rownum); log.debug("cell id: " +
	 * Byte.valueOf(sheet.getRow(rownum).getCell(0).getStringCellValue()));
	 * log.debug("cell productType: " +
	 * sheet.getRow(rownum).getCell(1).getStringCellValue());
	 * masterProductType.setId(Byte.valueOf(sheet.getRow(rownum).getCell(0).
	 * getStringCellValue()));
	 * masterProductType.setProductType(sheet.getRow(rownum).getCell(1).
	 * getStringCellValue()); MasterProductType existingRecordsCheck =
	 * masterProductTypeRepository.findOne(Byte.parseByte(sheet.getRow(rownum).
	 * getCell(0).getStringCellValue())); if (existingRecordsCheck != null) {
	 * masterProductType.setId(existingRecordsCheck.getId()); }
	 * masterProductTypeRepository.save(masterProductType); } catch (Exception e) {
	 * i++; sheet2.createRow(i);
	 * sheet2.getRow(i).createCell(0).setCellValue(sheet.getRow(rownum).getCell(
	 * 0).getStringCellValue());
	 * sheet2.getRow(i).createCell(1).setCellValue(sheet.getRow(rownum).getCell(
	 * 1).getStringCellValue());
	 * sheet2.getRow(i).createCell(2).setCellValue(e.getMessage()); } } }catch
	 * (IOException e) { e.printStackTrace(); success = false; } catch
	 * (InvalidFormatException e1) { e1.printStackTrace(); success = false; } return
	 * success;
	 * 
	 * }
	 */

	@Override
	public List<LookupAssetClassDTO> getAllLookupAssetClassDTO() {
		List<LookupAssetClass> lookupAssetClassList = lookupAssetRepository.findAll();

		List<LookupAssetClassDTO> lookupAssetClassDTOList = new ArrayList<LookupAssetClassDTO>();
		for (LookupAssetClass lookupAssetClassObj : lookupAssetClassList) {
			lookupAssetClassDTOList.add(mapper.map(lookupAssetClassObj, LookupAssetClassDTO.class));
		}

		return lookupAssetClassDTOList;
	}

	@Override
	public List<LookupAssetSubClassDTO> getAllLookupAssetSubClassDTO() {
		// TODO Auto-generated method stub
		List<LookupAssetSubClass> lookupAssetSubClassList = lookupAssetSubClassrepository.findAll();

		List<LookupAssetSubClassDTO> lookupAssetSubClassDTOList = new ArrayList<LookupAssetSubClassDTO>();
		for (LookupAssetSubClass lookupAssetSubClassObj : lookupAssetSubClassList) {
			lookupAssetSubClassDTOList.add(mapper.map(lookupAssetSubClassObj, LookupAssetSubClassDTO.class));
		}
		return lookupAssetSubClassDTOList;
	}

	@Override
	public List<MasterFundManagerDTO> getAllMasterFundManagerDTO() {
		// TODO Auto-generated method stub
		List<MasterFundManager> masterFundManagerList = masterFundManagerRepository.findAll();

		List<MasterFundManagerDTO> masterFundManagerDTOList = new ArrayList<MasterFundManagerDTO>();
		for (MasterFundManager masterFundManagerObj : masterFundManagerList) {
			masterFundManagerDTOList.add(mapper.map(masterFundManagerObj, MasterFundManagerDTO.class));
		}

		return masterFundManagerDTOList;
	}

	@Override
	public List<MasterIndexNameDTO> getAllMasterIndexNameDTO() {
		// TODO Auto-generated method stub
		List<MasterIndexName> masterIndexNameList = masterIndexNameRepository.findAll();

		List<MasterIndexNameDTO> masterIndexNameDTOList = new ArrayList<MasterIndexNameDTO>();
		for (MasterIndexName masterIndexNameObj : masterIndexNameList) {
			masterIndexNameDTOList.add(mapper.map(masterIndexNameObj, MasterIndexNameDTO.class));
		}
		return masterIndexNameDTOList;
	}

	@Override
	public List<MasterSubAssetClassReturnDTO> getAllMasterSubAssetClassReturnDTO() {
		// TODO Auto-generated method stub
		List<MasterSubAssetClassReturn> masterSubAssetClassReturnList = masterSubAssetClassReturnRepository.findAll();

		List<MasterSubAssetClassReturnDTO> masterSubAssetClassReturnDTOList = new ArrayList<MasterSubAssetClassReturnDTO>();
		for (MasterSubAssetClassReturn masterSubAssetClassReturnObj : masterSubAssetClassReturnList) {
			masterSubAssetClassReturnDTOList
					.add(mapper.map(masterSubAssetClassReturnObj, MasterSubAssetClassReturnDTO.class));
		}
		return masterSubAssetClassReturnDTOList;
	}

	@Override
	public List<MasterSectorBenchmarkMappingDTO> getAllMasterSectorBenchmarkMappingDTO() {
		// TODO Auto-generated method stub
		List<MasterSectorBenchmarkMapping> masterSectorBenchmarkMappingList = masterSectorBenchmarkMappingRepository
				.findAll();

		List<MasterSectorBenchmarkMappingDTO> masterSectorBenchmarkMappingDTOList = new ArrayList<MasterSectorBenchmarkMappingDTO>();
		for (MasterSectorBenchmarkMapping masterSectorBenchmarkMappingObj : masterSectorBenchmarkMappingList) {
			masterSectorBenchmarkMappingDTOList
					.add(mapper.map(masterSectorBenchmarkMappingObj, MasterSectorBenchmarkMappingDTO.class));
		}
		return masterSectorBenchmarkMappingDTOList;
	}

	@Override
	public List<MasterProductTypeDTO> getAllMasterProductTypeDTO() {
		// TODO Auto-generated method stub
		List<MasterProductType> masterProductTypeList = masterProductTypeRepository.findAll();

		List<MasterProductTypeDTO> masterProductTypeDTOList = new ArrayList<MasterProductTypeDTO>();
		for (MasterProductType masterProductTypeObj : masterProductTypeList) {
			masterProductTypeDTOList.add(mapper.map(masterProductTypeObj, MasterProductTypeDTO.class));
		}
		return masterProductTypeDTOList;
	}

	@Override
	public List<MasterMFRatingDTO> getAllMasterMFRatingDTO() {
		// TODO Auto-generated method stub
		List<MasterMFRating> masterMFRatingList = masterMFRatingRepository.findAll();

		List<MasterMFRatingDTO> masterMFRatingDTOList = new ArrayList<MasterMFRatingDTO>();
		for (MasterMFRating masterMFRatingObj : masterMFRatingList) {
			masterMFRatingDTOList.add(mapper.map(masterMFRatingObj, MasterMFRatingDTO.class));
		}
		return masterMFRatingDTOList;
	}

	@Override
	public List<MasterMFProductRecommendationDTO> getAllMasterMFProductRecommendationDTO() {
		// TODO Auto-generated method stub
		List<MasterMFProductRecommendation> masterMFProductRecommendationList = masterMFProductRecommendationRepository
				.findAll();

		List<MasterMFProductRecommendationDTO> masterMFProductRecommendationDTOList = new ArrayList<MasterMFProductRecommendationDTO>();
		for (MasterMFProductRecommendation masterMFProductRecommendationObj : masterMFProductRecommendationList) {
			masterMFProductRecommendationDTOList
					.add(mapper.map(masterMFProductRecommendationObj, MasterMFProductRecommendationDTO.class));
		}
		return masterMFProductRecommendationDTOList;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		if (id != 0) {
			masterTablesUploadHistoryRepository.delete(id);
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public List<MasterProductClassificationDTO> getAssetForMaturityRenewal() {

		List<MasterProductClassificationDTO> MasterProductClassificationDTOList = new ArrayList<MasterProductClassificationDTO>();
		List<MasterProductClassification> MasterProductClassificationList = masterProductClassificationRepository
				.findByLockedInFlag("Y");

		for (MasterProductClassification masterProductClassification : MasterProductClassificationList) {
			int flag = 0;
			if (masterProductClassification.getId() == 2) {
				flag = 1;
			}

			if (masterProductClassification.getId() == 4) {
				flag = 1;
			}

			if (flag == 0)
				MasterProductClassificationDTOList
						.add(mapper.map(masterProductClassification, MasterProductClassificationDTO.class));

		}

		MasterProductClassificationDTO ob = new MasterProductClassificationDTO();
		ob.setId((byte) 19);
		ob.setProductName("Mutual Funds");
		ob.setProductType((byte) 1);
		ob.setInvestmentAssetsFlag("Y");
		ob.setMarketLinkedFlag("Y");

		MasterProductClassificationDTOList.add(ob);

		Collections.sort(MasterProductClassificationDTOList, new ProductNameComparator());

		return MasterProductClassificationDTOList;

	}

	private void addMasterUploadErrorLogExcelOutputHeader(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		// create header row
		sheet.addCell(new Label(0, 0, "Sr. No"));
		sheet.addCell(new Label(1, 0, "Error Log"));
	}

	@Override
	public WritableWorkbook downloadErrorLog(String input, String selectedMaster, HttpServletResponse response)
			throws RuntimeException {
		// TODO Auto-generated method stub

		String fileName = selectedMaster + " " + "Upload Error Log.xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addMasterUploadErrorLogExcelOutputHeader(excelOutputsheet);

			addMasterUploadErrorLogExcelOutputBody(input, excelOutputsheet);

			writableWorkbook.write();
			writableWorkbook.close();

		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}
		return writableWorkbook;
	}

	private void addMasterUploadErrorLogExcelOutputBody(String input, WritableSheet sheet)
			throws RowsExceededException, WriteException {
		// creating Body
		int rowIndex = 1;// because header row is already added
		if (input != null && input != "") {
			int i = 1;
			for (i = 1; i == rowIndex; i++) {
				sheet.addCell(new Label(0, rowIndex, "" + i));
				sheet.addCell(new Label(1, rowIndex, input));
				rowIndex++;
				i++;
			}
		}
	}

	class ProductNameComparator implements Comparator<MasterProductClassificationDTO> {
		@Override
		public int compare(MasterProductClassificationDTO obj1, MasterProductClassificationDTO obj2) {
			return obj1.getProductName().compareTo(obj2.getProductName());
		}
	}

	@Override
	public List<MasterTransactAccountTypeDTO> getAllTransactAccountType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterTransactAccountType> masterTransactAccountTypeList = masterTransactAccountTypeRepository.findAll();

			List<MasterTransactAccountTypeDTO> masterTransactAccountTypeDTOList = new ArrayList<MasterTransactAccountTypeDTO>();

			for (MasterTransactAccountType masterTransactAccountType : masterTransactAccountTypeList) {
				masterTransactAccountTypeDTOList.add(mapper.map(masterTransactAccountType, MasterTransactAccountTypeDTO.class));
			}
			return masterTransactAccountTypeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<MasterTransactTaxStatusTypeDTO> getAllTransactTaxStatusType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterTransactTaxStatus> masterTransactTaxStatusTypeList = masterTransactTaxStatusRepository.findAll();

			List<MasterTransactTaxStatusTypeDTO> masterTransactTaxStatusTypeDTOList = new ArrayList<MasterTransactTaxStatusTypeDTO>();

			for (MasterTransactTaxStatus masterTransactTaxStatusType : masterTransactTaxStatusTypeList) {
				masterTransactTaxStatusTypeDTOList.add(mapper.map(masterTransactTaxStatusType, MasterTransactTaxStatusTypeDTO.class));
			}
			return masterTransactTaxStatusTypeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<MasterTransactStateCodeDTO> getAllTransactStateCode() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterTransactStateCode> masterTransactStateCodeList = masterTransactStateCodeRepository.findAll();

			List<MasterTransactStateCodeDTO> masterTransactStateCodeDTOList = new ArrayList<MasterTransactStateCodeDTO>();
				
			for (MasterTransactStateCode masterTransactStateCode : masterTransactStateCodeList) {
				masterTransactStateCodeDTOList.add(mapper.map(masterTransactStateCode, MasterTransactStateCodeDTO.class));
			}
			return masterTransactStateCodeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}	
	
	@Override
	public List<MasterTransactFatcaAddressTypeDTO> getAllTransactAddressType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterTransactAddressType> masterTransactFatcaAddressTypeList = masterTransactFatcaAddressTypeRepository.findAll();

			List<MasterTransactFatcaAddressTypeDTO> masterTransactFatcaAddressTypeDTOList = new ArrayList<MasterTransactFatcaAddressTypeDTO>();
				
			for (MasterTransactAddressType obj : masterTransactFatcaAddressTypeList) {
				masterTransactFatcaAddressTypeDTOList.add(mapper.map(obj, MasterTransactFatcaAddressTypeDTO.class));
			}
			return masterTransactFatcaAddressTypeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}	

	
	@Override
	public List<MasterTransactOccupationCodeDTO> getAllTransactOccupationCode() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterTransactOccupationCode> masterTransactOccupationCodeList = masterTransactOccupationCodeRepository.findAll();

			List<MasterTransactOccupationCodeDTO> masterTransactOccupationCodeDTOList = new ArrayList<MasterTransactOccupationCodeDTO>();
			
			for (MasterTransactOccupationCode masterTransactOccupationCode : masterTransactOccupationCodeList) {
				masterTransactOccupationCodeDTOList.add(mapper.map(masterTransactOccupationCode, MasterTransactOccupationCodeDTO.class));
			}
			return masterTransactOccupationCodeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}	
	
	@Override
	public List<MasterTransactDivPayModeDTO> getAllTransactDivPayMode() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterTransactDivPayMode> masterTransactDivPayModeList = masterTransactDivPayModeRepository.findAll();

			List<MasterTransactDivPayModeDTO> masterTransactDivPayModeDTOList = new ArrayList<MasterTransactDivPayModeDTO>();
			
			for (MasterTransactDivPayMode masterTransactDivPayMode : masterTransactDivPayModeList) {
				masterTransactDivPayModeDTOList.add(mapper.map(masterTransactDivPayMode, MasterTransactDivPayModeDTO.class));
			}
			return masterTransactDivPayModeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<MasterTransactCommunicationModeDTO> getAllTransactCommunicationMode() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterTransactCommunicationMode> masterTransactCommunicationModeList = masterTransactCommunicationModeRepository.findAll();

			List<MasterTransactCommunicationModeDTO> masterTransactDivPayModeDTOList = new ArrayList<MasterTransactCommunicationModeDTO>();
			
			for (MasterTransactCommunicationMode masterTransactDivPayMode : masterTransactCommunicationModeList) {
				masterTransactDivPayModeDTOList.add(mapper.map(masterTransactDivPayMode, MasterTransactCommunicationModeDTO.class));
			}
			return masterTransactDivPayModeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MasterTransactCountryNationalityDTO> getAllTransactCountryNationality() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterTransactCountryNationality> masterTransactCountryNationalityList = masterTransactCountryNationalityRepository.findAll();

			List<MasterTransactCountryNationalityDTO> masterTransactCountryNationalityDTOList = new ArrayList<MasterTransactCountryNationalityDTO>();
				
			for (MasterTransactCountryNationality obj : masterTransactCountryNationalityList) {
				masterTransactCountryNationalityDTOList.add(mapper.map(obj, MasterTransactCountryNationalityDTO.class));
			}
			return masterTransactCountryNationalityDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MasterTransactIncomeTypeDTO> getAllTransactIncomeSlabType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterTransactIncome> masterTransactIncome = masterTransactIncomeTypeRepository.findAll();

			List<MasterTransactIncomeTypeDTO> masterTransactIncomeTypeDTOList = new ArrayList<MasterTransactIncomeTypeDTO>();
			
			for (MasterTransactIncome obj : masterTransactIncome) {
				masterTransactIncomeTypeDTOList.add(mapper.map(obj, MasterTransactIncomeTypeDTO.class));
			}
			return masterTransactIncomeTypeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MasterTransactSourceOfWealthDTO> getAllTransactSourceOfWealth() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterTransactSourceOfWealth> masterTransactSourceWealth = masterTransactSourceOfWealthRepository.findAll();

			List<MasterTransactSourceOfWealthDTO> masterTransactSourceOfWealthDTOList = new ArrayList<MasterTransactSourceOfWealthDTO>();
			
			for (MasterTransactSourceOfWealth obj : masterTransactSourceWealth) {
				masterTransactSourceOfWealthDTOList.add(mapper.map(obj, MasterTransactSourceOfWealthDTO.class));
			}
			return masterTransactSourceOfWealthDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MasterTransactIdentificationTypeDTO> getAllTransactIdentificationTypes() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterTransactIdentificationType> masterTransactIdentificationTypeList = masterTransactIdentificationTypeRepository.findAll();
			
			List<MasterTransactIdentificationTypeDTO> masterTransactIdentificationTypeDTOList = new ArrayList<MasterTransactIdentificationTypeDTO>();
			
			for (MasterTransactIdentificationType obj : masterTransactIdentificationTypeList) {
				masterTransactIdentificationTypeDTOList.add(mapper.map(obj, MasterTransactIdentificationTypeDTO.class));
			}
			return masterTransactIdentificationTypeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * @Override public List<String> getAllBSEMFPhysicalAmc(int accessMode) throws
	 * RuntimeException { // TODO Auto-generated method stub //return null;
	 * 
	 * try { List<String> amcList = null; if (accessMode ==
	 * MFTransactConstant.BSE_ACCESS_LIVE_MODE) { amcList =
	 * masterTransactBSEMFPhysicalRepositoryLive.getAllAmcs(); } else { amcList =
	 * masterTransactBSEMFPhysicalRepository.getAllAmcs(); } return amcList; } catch
	 * (RuntimeException e) { // TODO Auto-generated catch block throw new
	 * RuntimeException(e); }
	 * 
	 * }
	 */
	@Override
	public List<String> getAllBSEMFPhysicalAmc(int accessMode) throws RuntimeException {
		
		List<String> amcListNew = null;
	   
		
		try {
			
			amcListNew = new ArrayList<String>();
			if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				//amcListNew = masterTransactBSEMFPhysicalRepository.getAllAmcs();
				amcListNew = masterTransactBSEMFPhysicalRepositoryLive.getAllAmcs();
			} else {
				amcListNew = masterTransactBSEMFPhysicalRepository.getAllAmcs();
				
				}
			
			return amcListNew;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}

	
	@Override
	public List<MasterTransactBSEMFPhysicalSchemeDTO> getAllBSEMFPhysicalSchemes() throws RuntimeException {
		// TODO Auto-generated method stub
		//return null;
		
		try {
			List<MasterTransactBSEMFPhysicalScheme> masterTransactBSEMFPhysicalSchemeList = masterTransactBSEMFPhysicalRepository.findAll();
			
			List<MasterTransactBSEMFPhysicalSchemeDTO> masterTransactBSEMFPhysicalSchemeDTOList = new ArrayList<MasterTransactBSEMFPhysicalSchemeDTO>();
			
			for (MasterTransactBSEMFPhysicalScheme obj : masterTransactBSEMFPhysicalSchemeList) {
				masterTransactBSEMFPhysicalSchemeDTOList.add(mapper.map(obj, MasterTransactBSEMFPhysicalSchemeDTO.class));
			}
			return masterTransactBSEMFPhysicalSchemeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}
	
	
//	public List<String> getAllBSEMFPhysicalSchemeTypes(int accessMode) throws RuntimeException {
//		List<MasterTransactBSEMFPhysicalSchemeLive> schemeTypeList = null;
//		List<MasterTransactBSEMFPhysicalScheme> schemeTypeListNew = null;
//		List<String> list = null;
//		try {
//			list = new ArrayList<String>();
//			if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
//				schemeTypeList = masterTransactBSEMFPhysicalRepositoryLive.findDistinctSchemeTypeByOrderBySchemeTypeAsc();
//				for (MasterTransactBSEMFPhysicalSchemeLive obj : schemeTypeList) {
//					list.add(obj.getSchemeType());
//				}
//			} else {
//				schemeTypeListNew = masterTransactBSEMFPhysicalRepository.getDistinctSchemeType();
//				//schemeTypeListNew = masterTransactBSEMFPhysicalRepository.findDistinctSchemeType();
//				for (MasterTransactBSEMFPhysicalScheme obj : schemeTypeListNew) {
//					list.add(obj.getSchemeType());
//				}
//			}
//			
//			/*List<MasterTransactBSEMFDematSchemeDTO> masterTransactBSEMFDematSchemeDTOList = new ArrayList<MasterTransactBSEMFDematSchemeDTO>();
//			
//			for (MasterTransactBSEMFDematScheme obj : masterTransactBSEMFDematSchemeList) {
//				masterTransactBSEMFDematSchemeDTOList.add(mapper.map(obj, MasterTransactBSEMFDematSchemeDTO.class));
//			}*/
//			return list;
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//			throw new RuntimeException(e);
//		}
//		
//	}
//	
//	
	@Override
	public List<String> getAllBSEMFPhysicalSchemeTypes(int accessMode) throws RuntimeException {
		// TODO Auto-generated method stub
		//return null;
		List<String> schemeTypeList = null;
		try {
			if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				//schemeTypeList = masterTransactBSEMFPhysicalRepositoryLive.getDistinctSchemeType();
				schemeTypeList = masterTransactBSEMFPhysicalRepositoryLive.getDistinctSchemeType();
			} else {
				schemeTypeList = masterTransactBSEMFPhysicalRepository.getDistinctSchemeType();
			}
			
			/*List<MasterTransactBSEMFDematSchemeDTO> masterTransactBSEMFDematSchemeDTOList = new ArrayList<MasterTransactBSEMFDematSchemeDTO>();
			
			for (MasterTransactBSEMFDematScheme obj : masterTransactBSEMFDematSchemeList) {
				masterTransactBSEMFDematSchemeDTOList.add(mapper.map(obj, MasterTransactBSEMFDematSchemeDTO.class));
			}*/
			return schemeTypeList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}

	
	@Override
	public List<MasterTransactBSEMFPhysicalSchemeDTO> getPhysicalSchemeNamesByFilter(String amcCode, String schemeType, String mode, int accessMode, String transactionMode) throws RuntimeException {
		// TODO Auto-generated method stub
		try{
			List<MasterTransactBSEMFPhysicalScheme> masterTransactBSEMFPhysicalSchemeList = null;
			
			List<MasterTransactBSEMFPhysicalSchemeLive> masterTransactBSEMFPhysicalSchemeListLive = null;
			
			List<MasterTransactBSEMFPhysicalSchemeDTO> masterTransactBSEMFPhysicalSchemeDTOList = new ArrayList<MasterTransactBSEMFPhysicalSchemeDTO>();
			
			
			
			if (mode.equals(FinexaConstant.ORDER_LUMPSUM)) {
				
				if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
					masterTransactBSEMFPhysicalSchemeListLive = masterTransactBSEMFPhysicalRepositoryLive.
							findByAmcCodeAndSchemeTypeAndPurchaseAllowedAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndPurchaseAllowedAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, transactionMode);
				} else {
					/*masterTransactBSEMFPhysicalSchemeList = masterTransactBSEMFPhysicalRepository.
							findByAmcCodeAndSchemeTypeAndPurchaseAllowedAndSchemePlanAndPurchaseTransactionModeOrPurchaseTransactionModeOrderBySchemeNameAsc(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, transactionMode);*/
					
					masterTransactBSEMFPhysicalSchemeList = masterTransactBSEMFPhysicalRepository.
							findByAmcCodeAndSchemeTypeAndPurchaseAllowedAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndPurchaseAllowedAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, transactionMode);
				}
				
			} else if (mode.equals(FinexaConstant.ORDER_SIP)) {
				if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
					masterTransactBSEMFPhysicalSchemeListLive = masterTransactBSEMFPhysicalRepositoryLive.
							findByAmcCodeAndSchemeTypeAndSipFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndSipFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, transactionMode);
				} else {
					masterTransactBSEMFPhysicalSchemeList = masterTransactBSEMFPhysicalRepository.
							findByAmcCodeAndSchemeTypeAndSipFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndSipFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, transactionMode);
				}
			} else if (mode.equals(FinexaConstant.ORDER_SWITCH)) {
				if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
					masterTransactBSEMFPhysicalSchemeListLive = masterTransactBSEMFPhysicalRepositoryLive.
							findByAmcCodeAndSchemeTypeAndSwitchFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndSwitchFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, transactionMode);
				} else {
					masterTransactBSEMFPhysicalSchemeList = masterTransactBSEMFPhysicalRepository.
							findByAmcCodeAndSchemeTypeAndSwitchFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndSwitchFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, transactionMode);
				}
				

			} else if (mode.equals(FinexaConstant.ORDER_STP)) {
				if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
					masterTransactBSEMFPhysicalSchemeListLive = masterTransactBSEMFPhysicalRepositoryLive.
							findByAmcCodeAndSchemeTypeAndStpFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndStpFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, transactionMode);
				} else {
					masterTransactBSEMFPhysicalSchemeList = masterTransactBSEMFPhysicalRepository.
							findByAmcCodeAndSchemeTypeAndStpFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndStpFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, transactionMode);
				}
				
			} else if (mode.equals(FinexaConstant.ORDER_SWP)) {
				if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
					masterTransactBSEMFPhysicalSchemeListLive = masterTransactBSEMFPhysicalRepositoryLive.
							findByAmcCodeAndSchemeTypeAndSwitchFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndSwitchFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, transactionMode);
				} else {
					masterTransactBSEMFPhysicalSchemeList = masterTransactBSEMFPhysicalRepository.
							findByAmcCodeAndSchemeTypeAndSwitchFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndSwitchFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, transactionMode);
				}
				
			} else if (mode.equals(FinexaConstant.ORDER_REDEEM)) {
				if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
					masterTransactBSEMFPhysicalSchemeListLive = masterTransactBSEMFPhysicalRepositoryLive.
							findByAmcCodeAndSchemeTypeAndRedemptionAllowedAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndRedemptionAllowedAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, transactionMode);
				} else {
					masterTransactBSEMFPhysicalSchemeList = masterTransactBSEMFPhysicalRepository.
							findByAmcCodeAndSchemeTypeAndRedemptionAllowedAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndRedemptionAllowedAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc
							(amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, 
									FinexaConstant.ONLY_DEMAT_PHYSICAL, amcCode, schemeType, FinexaConstant.ALLOWED_FLAG_YES,MFTransactConstant.NORMAL_SCHEME_PLAN, transactionMode);
				}
				
			}
			if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				for (MasterTransactBSEMFPhysicalSchemeLive obj : masterTransactBSEMFPhysicalSchemeListLive) {
					masterTransactBSEMFPhysicalSchemeDTOList.add(mapper.map(obj, MasterTransactBSEMFPhysicalSchemeDTO.class));
				}
			} else {
				for (MasterTransactBSEMFPhysicalScheme obj : masterTransactBSEMFPhysicalSchemeList) {
					masterTransactBSEMFPhysicalSchemeDTOList.add(mapper.map(obj, MasterTransactBSEMFPhysicalSchemeDTO.class));
				}
			}
			
			
			
			return masterTransactBSEMFPhysicalSchemeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	
	@Override
	public MasterTransactBSEMFPhysicalSchemeDTO getPhysicalSchemeDetails(String uniqueNo, int accessMode) throws RuntimeException {
		// TODO Auto-generated method stub
		int uniqueRefNo = 0;
		MasterTransactBSEMFPhysicalSchemeDTO physicalDTO = null;
		try {
			uniqueRefNo = Integer.parseInt(uniqueNo);
			if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				MasterTransactBSEMFPhysicalSchemeLive physicalScheme = masterTransactBSEMFPhysicalRepositoryLive.findOne(uniqueRefNo);
				physicalDTO = mapper.map(physicalScheme, MasterTransactBSEMFPhysicalSchemeDTO.class);
			} else {
				MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(uniqueRefNo);
				physicalDTO = mapper.map(physicalScheme, MasterTransactBSEMFPhysicalSchemeDTO.class);
			}
			
			
			return physicalDTO;
			
		}catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MasterFileMappingBODTO> getSpecificFileName(int rtaId, String fileCode) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			
			LookupRTAMasterFileDetailsBO lookupRTAMasterFileDetailsBO = lookupRTAMasterFileDetailsBORepository.findByFileCode(fileCode);
			
			List<Object[]> masterFileMappingBOList = masterFileMappingBORepository.getAllDataByRTAAndFileName(rtaId, lookupRTAMasterFileDetailsBO.getId());
			List<MasterFileMappingBODTO> masterFileMappingBODTOList = new ArrayList<MasterFileMappingBODTO>();
			for (Object[] masterFileMappingBOObj : masterFileMappingBOList) {
				MasterFileMappingBODTO masterFileMappingBODTO = new MasterFileMappingBODTO();
				masterFileMappingBODTO.setId((Integer)masterFileMappingBOObj[0]);
				//masterFileMappingBODTO.setId((Integer)masterFileMappingBOObj[4]);
				masterFileMappingBODTO.setExtension((String)masterFileMappingBOObj[1]);
				LookupRTAFileName lookupRTAFileName = lookupRTAFileNameRepository.findOne((Integer)masterFileMappingBOObj[4]);
				masterFileMappingBODTO.setRtaFileName(lookupRTAFileName.getName());
				masterFileMappingBODTOList.add(masterFileMappingBODTO);
			}
			
			return masterFileMappingBODTOList;
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/*
	@Override
	public String getFileExtensionByFileName(int fileId) throws RuntimeException {
		// TODO Auto-generated method stub
		
		try {
			
			String fileExtension = new String();
			fileExtension = masterFileMappingBORepository.findExtensionById(fileId);
			System.out.println("fileExtension " + fileExtension);
			return fileExtension;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		
	}
	*/
	
	@Override
	public String getStateByCode(String stateCode) throws RuntimeException {
			
			String stateName = new String();
			try {
				
				//stateName = masterTransactStateCodeRepository.findDetailsByCode(stateCode);
				MasterTransactStateCode masterTransactStateCode = masterTransactStateCodeRepository.findDetailsByCode(stateCode);
				stateName = masterTransactStateCode.getDetails();
				System.out.println(stateName);
				return stateName;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
	}
	@Override
	public MasterFileMappingBODTO getFileExtensionByFileName(int fileId) throws RuntimeException {
		// TODO Auto-generated method stub
		
		MasterFileMappingBODTO masterFileMappingBODTO = new MasterFileMappingBODTO();
		MasterFileMappingBO masterFileMappingBO = new MasterFileMappingBO();
		try {
			masterFileMappingBO = masterFileMappingBORepository.findOne(fileId);
			masterFileMappingBODTO = mapper.map(masterFileMappingBO, MasterFileMappingBODTO.class);
			masterFileMappingBODTO.setRtaFileName(masterFileMappingBO.getLookupRtafileName().getName());
			//System.out.println("fileExtension " + masterFileMappingBODTO.getExtension());
			return masterFileMappingBODTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public List<FundSchemeIsinDTO> getSchemesFromFund(String fundHouse, String status, String matchString)
			throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			log.debug("Name = " + fundHouse);
			log.debug("Status = " + status);
			//List<Object[]> list = masterMFETFRepository.findForSecondDropdown(fundHouse, "%"+matchString+"%");
			List<Object[]> list = masterMFETFRepository.findForSecondDropdown("%"+matchString+"%", fundHouse);
			List<FundSchemeIsinDTO> isinAndSchemes = new ArrayList<FundSchemeIsinDTO>();
			for (Object obj[] : list) {
				FundSchemeIsinDTO fundSchemeIsinDTO = new FundSchemeIsinDTO();
				String series = ((String)obj[16]);
				String schemeName = ((String)obj[2]);
				String descriptiveSchemeName = schemeName + "-" + series;
				
				fundSchemeIsinDTO.setIsin((String)obj[0]);
				fundSchemeIsinDTO.setSchemeName(schemeName);
				fundSchemeIsinDTO.setDescriptiveSchemeName(descriptiveSchemeName);
				fundSchemeIsinDTO.setAssetClassId((Byte)obj[4]);
				fundSchemeIsinDTO.setSubAssetClassId((Byte)obj[5]);
				//FundSchemeIsinDTO fundSchemeIsinDTO = mapper.map(masterMutualFundETF, FundSchemeIsinDTO.class);
				isinAndSchemes.add(fundSchemeIsinDTO);
			}
			log.debug("List : " + isinAndSchemes);
			// for sorting alphabetically
			isinAndSchemes.sort(Comparator.comparing(FundSchemeIsinDTO::getSchemeName));
			return isinAndSchemes;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	
}
