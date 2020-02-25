package com.finlabs.finexa.web;

import java.io.IOException;
import java.util.List;
 
import javax.servlet.http.HttpServletResponse;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.finlabs.finexa.dto.LookupAssetAllocationCategoryDTO;
import com.finlabs.finexa.dto.LookupAssetAllocationDTO;
import com.finlabs.finexa.dto.LookupAssetClassDTO;
import com.finlabs.finexa.dto.LookupAssetSubClassDTO;
import com.finlabs.finexa.dto.LookupBucketLogicDTO;
import com.finlabs.finexa.dto.Master52WeekHighLowMFDTO;
import com.finlabs.finexa.dto.MasterAssetClassReturnDTO;
import com.finlabs.finexa.dto.MasterDirectEquityDTO;
import com.finlabs.finexa.dto.MasterDirectEquityMarketCapDTO;
import com.finlabs.finexa.dto.MasterEquityDailyPriceDTO;
import com.finlabs.finexa.dto.MasterEquityMarketCapClassificationDTO;
import com.finlabs.finexa.dto.MasterExpectedHistoricalReturnDTO;
import com.finlabs.finexa.dto.MasterIndexDailyNAVDTO;
import com.finlabs.finexa.dto.MasterIndexNameDTO;
import com.finlabs.finexa.dto.MasterMFCategoryDTO;
import com.finlabs.finexa.dto.MasterMFDailyNAVDTO;
import com.finlabs.finexa.dto.MasterMFHoldingDTO;
import com.finlabs.finexa.dto.MasterMFHoldingMaturityDTO;
import com.finlabs.finexa.dto.MasterMFHoldingRatingWiseDTO;
import com.finlabs.finexa.dto.MasterMFHoldingSectorWiseDTO;
import com.finlabs.finexa.dto.MasterMFMaturityDTO;
import com.finlabs.finexa.dto.MasterMFMaturityProfileDTO;
import com.finlabs.finexa.dto.MasterMFProductRecommendationDTO;
import com.finlabs.finexa.dto.MasterMFRatingDTO;
import com.finlabs.finexa.dto.MasterMfSectorDTO;
import com.finlabs.finexa.dto.MasterMutualFundETFDTO;
import com.finlabs.finexa.dto.MasterProductClassificationDTO;
import com.finlabs.finexa.dto.MasterProductTypeDTO;
import com.finlabs.finexa.dto.MasterSectorBenchmarkMappingDTO;
import com.finlabs.finexa.dto.MasterSubAssetClassReturnDTO;
import com.finlabs.finexa.model.LookupAssetAllocation;
import com.finlabs.finexa.model.LookupAssetAllocationCategory;
import com.finlabs.finexa.model.LookupAssetClass;
import com.finlabs.finexa.model.LookupAssetSubClass;
import com.finlabs.finexa.model.LookupBucketLogic;
import com.finlabs.finexa.model.Master52WeekHighLowMF;
import com.finlabs.finexa.model.MasterAssetClassReturn;
import com.finlabs.finexa.model.MasterDirectEquity;
import com.finlabs.finexa.model.MasterDirectEquityMarketCap;
import com.finlabs.finexa.model.MasterEquityDailyPrice;
import com.finlabs.finexa.model.MasterEquityMarketCapClassification;
import com.finlabs.finexa.model.MasterExpectedHistoricalReturn;
import com.finlabs.finexa.model.MasterIndexDailyNAV;
import com.finlabs.finexa.model.MasterIndexName;
import com.finlabs.finexa.model.MasterMFCategory;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMFHolding;
import com.finlabs.finexa.model.MasterMFHoldingMaturity;
import com.finlabs.finexa.model.MasterMFHoldingRatingWise;
import com.finlabs.finexa.model.MasterMFHoldingSectorWise;
import com.finlabs.finexa.model.MasterMFMaturity;
import com.finlabs.finexa.model.MasterMFMaturityProfile;
import com.finlabs.finexa.model.MasterMFProductRecommendation;
import com.finlabs.finexa.model.MasterMFRating;
import com.finlabs.finexa.model.MasterMFSector;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.model.MasterProductType;
import com.finlabs.finexa.model.MasterSectorBenchmarkMapping;
import com.finlabs.finexa.model.MasterSubAssetClassReturn;
import com.finlabs.finexa.repository.LookupAssetAllocationCategoryRepository;
import com.finlabs.finexa.repository.LookupAssetAllocationRepository;
import com.finlabs.finexa.repository.LookupAssetClassRepository;
import com.finlabs.finexa.repository.LookupAssetSubClassRepository;
import com.finlabs.finexa.repository.LookupBucketLogicRepository;
import com.finlabs.finexa.repository.Master52WeekHighLowMFRepository;
import com.finlabs.finexa.repository.MasterAssetClassReturnRepository;

import com.finlabs.finexa.repository.MasterDirectEquityRepository;
import com.finlabs.finexa.repository.MasterEquityDailyPriceRepository;
import com.finlabs.finexa.repository.MasterEquityMarketCapClassificationRepository;
import com.finlabs.finexa.repository.MasterExpectedHistoricalReturnRepository;
import com.finlabs.finexa.repository.MasterIndexDailyNAVRepository;
import com.finlabs.finexa.repository.MasterIndexNameRepository;
import com.finlabs.finexa.repository.MasterMFCategoryRepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMFHoldingMaturityRepository;
import com.finlabs.finexa.repository.MasterMFHoldingRatingWiseRepository;
import com.finlabs.finexa.repository.MasterMFHoldingRepository;
import com.finlabs.finexa.repository.MasterMFHoldingSectorWiseRepository;
import com.finlabs.finexa.repository.MasterMFMaturityProfileRepository;
import com.finlabs.finexa.repository.MasterMFMaturityRepository;
import com.finlabs.finexa.repository.MasterMFProductRecommendationRepository;
import com.finlabs.finexa.repository.MasterMFRatingRepository;
import com.finlabs.finexa.repository.MasterMfSectorRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.repository.MasterProductTypeRepository;
import com.finlabs.finexa.repository.MasterSectorBenchmarkMappingRepository;
import com.finlabs.finexa.repository.MasterSubAssetClassReturnRepository;
import com.finlabs.finexa.service.MasterService;


 
@Controller
public class CSVFileDownloadController {/*
	private static Logger log = LoggerFactory.getLogger(CSVFileDownloadController.class);
	
	@Autowired
	Mapper mapper;

	@Autowired
	MasterService masterService;
	
	@Autowired
	private LookupAssetClassRepository lookupAssetClassRepository;
	@Autowired
	private MasterMutualFundETFRepository masterMutualFundETFRepository;
	@Autowired
	private MasterSubAssetClassReturnRepository masterSubAssetClassReturnRepository;
	@Autowired
	private MasterSectorBenchmarkMappingRepository masterSectorBenchmarkMappingRepository;
	@Autowired
	private MasterProductTypeRepository masterProductTypeRepository;
	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;
	@Autowired
	private MasterMfSectorRepository masterMfSectorRepository;
	@Autowired
	private MasterMFRatingRepository masterMFRatingRepository;
	@Autowired
	private MasterMFProductRecommendationRepository masterMFProductRecommendationrepository;
	@Autowired
	private MasterMFMaturityProfileRepository masterMFMaturityProfileRepository;
	@Autowired
	private MasterMFMaturityRepository masterMFMaturityRepository;
	@Autowired
	private MasterMFHoldingSectorWiseRepository masterMFHoldingSectorWiseRepository;
	@Autowired
	private MasterMFHoldingRatingWiseRepository masterMFHoldingRatingWiseRepository;
	@Autowired
	private MasterMFHoldingMaturityRepository masterMFHoldingMaturityRepository;
	@Autowired
	private MasterMFHoldingRepository masterMFHoldingRepository;
	@Autowired
	private MasterMFDailyNAVRepository masterMFDailyNAVRepository;
	@Autowired
	private MasterMFCategoryRepository masterMFCategoryRepository;
	@Autowired
	private MasterIndexNameRepository masterIndexNameRepository;
	@Autowired
	private MasterIndexDailyNAVRepository masterIndexDailyNAVRepository;
	@Autowired
	private MasterExpectedHistoricalReturnRepository masterExpectedHistoricalReturnRepository;
	@Autowired
    private MasterEquityMarketCapClassificationRepository masterEquityMarketCapClassificationRepository;
	@Autowired
	private MasterEquityDailyPriceRepository masterEquityDailyPriceRepository;
	@Autowired
	private MasterDirectEquityMarketCapRepository masterDirectEquityMarketCapRepository;
	@Autowired
	private MasterDirectEquityRepository masterDirectEquityRepository;
	@Autowired
	private MasterAssetClassReturnRepository masterAssetClassReturnRepository;
	@Autowired
	private Master52WeekHighLowMFRepository master52WeekHighLowMFRepository;
	@Autowired 
	private LookupAssetSubClassRepository lookupAssetSubClassRepository;
	@Autowired
	private LookupAssetAllocationCategoryRepository lookupAssetAllocationCategoryRepository;
	@Autowired
	private LookupAssetAllocationRepository lookupAssetAllocationRepository;
	@Autowired
	private LookupBucketLogicRepository lookupBucketLogicRepository;

	@RequestMapping(value = "/downloadMasterTemplateCSV")
    public void downloadMasterTemplateCSV(HttpServletResponse response) throws IOException {

        String csvFileName = "masterTemplateFormat.csv";
 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "column1", "column2", "column3", "...", "..." };
 
        csvWriter.writeHeader(header);
        csvWriter.close();
    }
	
    @RequestMapping(value = "/downloadMasterMutualFundETFCSV")
    public void downloadMasterMutualFundETFCSV(HttpServletResponse response) throws IOException {

        String csvFileName = "masterMutualFundETF.csv";
 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterMutualFundETF> masterMFETFList = masterMutualFundETFRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "fundHouse", "schemeName", "amfiCode", "isin",
                "assetClassID", "subAssetClassID", "schemeOption", "closeEndedFlag", "SchemeInceptionDate", 
                "regularDirectFlag", "schemeEndDate", "exitLoadAndPeriod", "minInvestmentAmount", "fundManagerCode", 
                "benchmarkIndex" };
 
        csvWriter.writeHeader(header);
        
        for (MasterMutualFundETF masterMutualFundETFObj : masterMFETFList) {
        	MasterMutualFundETFDTO  masterMutualFundETFDTO = mapper.map(masterMutualFundETFObj, MasterMutualFundETFDTO.class);
        	
        	masterMutualFundETFDTO.setAssetClassID((int)masterMutualFundETFObj.getLookupAssetClass().getId());
        	masterMutualFundETFDTO.setSubAssetClassID((int)masterMutualFundETFObj.getLookupAssetSubClass().getId());
        	masterMutualFundETFDTO.setFundManagerCode(masterMutualFundETFObj.getMasterFundManager().getManagerCode());
        	masterMutualFundETFDTO.setBenchmarkIndex(masterMutualFundETFObj.getMasterIndexName().getId());
        	
        	csvWriter.write(masterMutualFundETFDTO, header);
        }
 
        csvWriter.close();
    }
    
    @RequestMapping(value = "/downloadMasterSubAssetClassReturnCSV")
    public void downloadMasterSubAssetClassReturnCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterSubAssetClassReturn.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterSubAssetClassReturn> masterSubAssetClassReturnList = masterSubAssetClassReturnRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "lookupAssetSubClassId", "mlr", "bcr",
                "wcr", "riskStdDev", "fromDate", "toDate" };
 
        csvWriter.writeHeader(header);
        
        for (MasterSubAssetClassReturn masterSubAssetClassReturnObj : masterSubAssetClassReturnList) {
        	MasterSubAssetClassReturnDTO  masterSubAssetClassReturnDTO = mapper.map(masterSubAssetClassReturnObj, MasterSubAssetClassReturnDTO.class);
        	masterSubAssetClassReturnDTO.setId(masterSubAssetClassReturnObj.getId());
        	masterSubAssetClassReturnDTO.setLookupAssetSubClassId(masterSubAssetClassReturnObj.getLookupAssetSubClass().getId());
        	log.debug("MasterSubAssetClassReturnDTO: " + masterSubAssetClassReturnDTO);
        	csvWriter.write(masterSubAssetClassReturnDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterSectorBenchmarkMappingCSV")
    public void downloadMasterSectorBenchmarkMappingCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterSectorBenchmarkMapping.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterSectorBenchmarkMapping> masterSectorBenchmarkMappingList = masterSectorBenchmarkMappingRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "sectorID", "benchmarkIndex" };
 
        csvWriter.writeHeader(header);
        
        for (MasterSectorBenchmarkMapping masterSectorBenchmarkMappingObj : masterSectorBenchmarkMappingList) {
        	MasterSectorBenchmarkMappingDTO  masterSectorBenchmarkMappingDTO = mapper.map(masterSectorBenchmarkMappingObj, MasterSectorBenchmarkMappingDTO.class);
        	masterSectorBenchmarkMappingDTO.setSectorID(masterSectorBenchmarkMappingObj.getMasterMfsector().getId());
        	masterSectorBenchmarkMappingDTO.setBenchmarkIndex(masterSectorBenchmarkMappingObj.getMasterIndexName().getId());
       // 	log.debug("MasterSubAssetClassReturnDTO: " + masterSubAssetClassReturnDTO);
        	csvWriter.write(masterSectorBenchmarkMappingDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterProductTypeCSV")
    public void downloadMasterProductTypeCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterProductType.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterProductType> masterProductTypeList = masterProductTypeRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "productType" };
 
        csvWriter.writeHeader(header);
        
        for (MasterProductType masterProductTypeObj : masterProductTypeList) {
        	MasterProductTypeDTO  masterProductTypeDTO = mapper.map(masterProductTypeObj, MasterProductTypeDTO.class);
        
      
        	csvWriter.write(masterProductTypeDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterProductClassificationCSV")
    public void downloadMasterProductClassificationCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterProductClassification.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterProductClassification> masterProductClassificationList = masterProductClassificationRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "productName", "assetClass", "subAssetClass", "productType", 
        		"maturityFlag", "lockedInFlag", "investmentAssetsFlag", "marketLinkedFlag" };
 
        csvWriter.writeHeader(header);
        
        for (MasterProductClassification masterProductClassificationObj : masterProductClassificationList) {
        	MasterProductClassificationDTO  masterProductClassificationDTO = mapper.map(masterProductClassificationObj, MasterProductClassificationDTO.class);
        	
        	if (masterProductClassificationObj.getLookupAssetClass() != null){
        		masterProductClassificationDTO.setAssetClass(masterProductClassificationObj.getLookupAssetClass().getId());
        	}
        	if (masterProductClassificationObj.getLookupAssetSubClass() != null) {
        		masterProductClassificationDTO.setSubAssetClass(masterProductClassificationObj.getLookupAssetSubClass().getId());
        	}
        	masterProductClassificationDTO.setProductType(masterProductClassificationObj.getMasterProductType().getId());
        	csvWriter.write(masterProductClassificationDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterMfSectorCSV")
    public void downloadMasterMfSectorCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterMFSector.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterMFSector> masterMFSectorList = masterMfSectorRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "sector" };
 
        csvWriter.writeHeader(header);
        
        for (MasterMFSector masterMFSectorObj : masterMFSectorList) {
        	MasterMfSectorDTO  masterMfSectorDTO = mapper.map(masterMFSectorObj, MasterMfSectorDTO.class);
        	csvWriter.write(masterMfSectorDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterMfRatingCSV")
    public void downloadMasterMfRatingCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterMFRating.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterMFRating> masterMFRatingList = masterMFRatingRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "rating" };
 
        csvWriter.writeHeader(header);
        
        for (MasterMFRating masterMFRatingObj : masterMFRatingList) {
        	MasterMFRatingDTO  masterMFRatingDTO = mapper.map(masterMFRatingObj, MasterMFRatingDTO.class);
        	csvWriter.write(masterMFRatingDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterMFProductRecommendationCSV")
    public void downloadMasterMFProductRecommendationCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterMFProductRecommendation.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterMFProductRecommendation> masterMasterMFProductRecommendationList = masterMFProductRecommendationrepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "advisorID", "amfiCode", "isin", "schemeName", "timeframeStartDate", "timeframeEndDate" };
 
        csvWriter.writeHeader(header);
        
        for (MasterMFProductRecommendation masterMasterMFProductRecommendationObj : masterMasterMFProductRecommendationList) {
        	MasterMFProductRecommendationDTO  masterMFProductRecommendationDTO = mapper.map(masterMasterMFProductRecommendationObj, MasterMFProductRecommendationDTO.class);
        	
        	masterMFProductRecommendationDTO.setAdvisorID(masterMasterMFProductRecommendationObj.getAdvisorMaster().getId());
        	masterMFProductRecommendationDTO.setAmfiCode(masterMasterMFProductRecommendationObj.getMasterMutualFundEtf().getAmfiCode());
        	masterMFProductRecommendationDTO.setTimeframeEndDate(masterMasterMFProductRecommendationObj.getId().getTimeframeEndDate());
        	csvWriter.write(masterMFProductRecommendationDTO, header);
        }
 
        csvWriter.close();
    	
    }
  
    @RequestMapping(value = "/downloadMasterMFMaturityProfileCSV")
    public void downloadMasterMFMaturityProfileCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterMFMaturityProfile.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterMFMaturityProfile> masterMFMaturityProfileList = masterMFMaturityProfileRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "date", "schemeName", "amfiCode", "isin", "maturityID", "allocation" };
 
        csvWriter.writeHeader(header);
        
        for (MasterMFMaturityProfile masterMasterMFMaturityProfileObj : masterMFMaturityProfileList) {
        	MasterMFMaturityProfileDTO  masterMFMaturityProfileDTO = mapper.map(masterMasterMFMaturityProfileObj, MasterMFMaturityProfileDTO.class);
        	
        	masterMFMaturityProfileDTO.setDate(masterMasterMFMaturityProfileObj.getId().getDate());
        	masterMFMaturityProfileDTO.setAmfiCode(masterMasterMFMaturityProfileObj.getMasterMutualFundEtf().getAmfiCode());
        	masterMFMaturityProfileDTO.setMaturityID(masterMasterMFMaturityProfileObj.getMasterMfmaturity().getId());
        	csvWriter.write(masterMFMaturityProfileDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterMFMaturityCSV")
    public void downloadMasterMFMaturityCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterMFMaturity.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterMFMaturity> masterMFMaturityList = masterMFMaturityRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "maturity" };
 
        csvWriter.writeHeader(header);
        
        for (MasterMFMaturity masterMFMaturityObj : masterMFMaturityList) {
        	MasterMFMaturityDTO  masterMFMaturityDTO = mapper.map(masterMFMaturityObj, MasterMFMaturityDTO.class);
        	csvWriter.write(masterMFMaturityDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterMFHoldingSectorWiseCSV")
    public void downloadMasterMFHoldingSectorWiseCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterMFHoldingSectorWise.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterMFHoldingSectorWise> masterMFHoldingSectorWiseList = masterMFHoldingSectorWiseRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "date", "amfiCode", "ISIN", "scheme", "sectorID", "holding" };
 
        csvWriter.writeHeader(header);
        
        for (MasterMFHoldingSectorWise masterMFHoldingSectorWiseObj : masterMFHoldingSectorWiseList) {
        	MasterMFHoldingSectorWiseDTO  masterMFHoldingSectorWiseDTO = mapper.map(masterMFHoldingSectorWiseObj, MasterMFHoldingSectorWiseDTO.class);
        	
        	masterMFHoldingSectorWiseDTO.setDate(masterMFHoldingSectorWiseObj.getId().getDate());
        	masterMFHoldingSectorWiseDTO.setAmfiCode(masterMFHoldingSectorWiseObj.getMasterMutualFundEtf().getAmfiCode());
        	masterMFHoldingSectorWiseDTO.setISIN(masterMFHoldingSectorWiseObj.getIsin());
        	masterMFHoldingSectorWiseDTO.setSectorID(masterMFHoldingSectorWiseObj.getMasterMfsector().getId());
        	csvWriter.write(masterMFHoldingSectorWiseDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterMFHoldingRatingWiseCSV")
    public void downloadMasterMFHoldingRatingWiseCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterMFHoldingRatingWise.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterMFHoldingRatingWise> masterMFHoldingRatingWiseList = masterMFHoldingRatingWiseRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "date", "amfiCode", "isin", "schemeName", "ratingID", "holding" };
 
        csvWriter.writeHeader(header);
        
        for (MasterMFHoldingRatingWise masterMFHoldingRatingWiseObj : masterMFHoldingRatingWiseList) {
        	MasterMFHoldingRatingWiseDTO  masterMFHoldingRatingWiseDTO = mapper.map(masterMFHoldingRatingWiseObj, MasterMFHoldingRatingWiseDTO.class);
        	
        	masterMFHoldingRatingWiseDTO.setDate(masterMFHoldingRatingWiseObj.getId().getDate());
        	masterMFHoldingRatingWiseDTO.setAmfiCode(masterMFHoldingRatingWiseObj.getMasterMutualFundEtf().getAmfiCode());
        	masterMFHoldingRatingWiseDTO.setIsin(masterMFHoldingRatingWiseObj.getIsin());
        	masterMFHoldingRatingWiseDTO.setRatingID(masterMFHoldingRatingWiseObj.getMasterMfrating().getId());
        	csvWriter.write(masterMFHoldingRatingWiseDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterMFHoldingMaturityCSV")
    public void downloadMasterMFHoldingMaturityCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterMFHoldingMaturity.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterMFHoldingMaturity> masterMFHoldingMaturityList = masterMFHoldingMaturityRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "amfiCode", "schemeName", "averageMaturityPeriod", "asOfDate", "ytm", "duration",
        		"action" };
 
        csvWriter.writeHeader(header);
        
        for (MasterMFHoldingMaturity masterMFHoldingMaturityObj : masterMFHoldingMaturityList) {
        	MasterMFHoldingMaturityDTO  masterMFHoldingMaturityDTO = mapper.map(masterMFHoldingMaturityObj, MasterMFHoldingMaturityDTO.class);
        	
        	masterMFHoldingMaturityDTO.setAmfiCode(masterMFHoldingMaturityObj.getMasterMutualFundEtf().getAmfiCode());
        	masterMFHoldingMaturityDTO.setAsOfDate(masterMFHoldingMaturityObj.getId().getAsOfDate());
        	csvWriter.write(masterMFHoldingMaturityDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterMFHoldingCSV")
    public void downloadMasterMFHoldingCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterMFHolding.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterMFHolding> masterMFHoldingList = masterMFHoldingRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "amfiCode", "companyCode", "assetTypeCode", "assetDate", "assetValue", "numOfShares",
        		"assetPercentage", "couponRate", "maturity", "creditRatingCode", "action" };
 
        csvWriter.writeHeader(header);
        
        for (MasterMFHolding masterMFHoldingObj : masterMFHoldingList) {
        	MasterMFHoldingDTO  masterMFHoldingDTO = mapper.map(masterMFHoldingObj, MasterMFHoldingDTO.class);
        	
        	masterMFHoldingDTO.setAmfiCode(masterMFHoldingObj.getMasterMutualFundEtf().getAmfiCode());
        	masterMFHoldingDTO.setCompanyCode(masterMFHoldingObj.getId().getCompanyCode());
        	masterMFHoldingDTO.setAssetDate(masterMFHoldingObj.getId().getAssetDate());
        	csvWriter.write(masterMFHoldingDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterMFDailyNAVCSV")
    public void downloadMasterMFDailyNAVCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterMFDailyNAV.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterMFDailyNAV> masterMFDailyNAVList = masterMFDailyNAVRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "amfiCode", "isin", "schemeName", "date", "nav" };
 
        csvWriter.writeHeader(header);
        
        for (MasterMFDailyNAV masterMFDailyNAVObj : masterMFDailyNAVList) {
        	MasterMFDailyNAVDTO  masterMFDailyNAVDTO = mapper.map(masterMFDailyNAVObj, MasterMFDailyNAVDTO.class);
        	
        	masterMFDailyNAVDTO.setId(masterMFDailyNAVObj.getId());
        	csvWriter.write(masterMFDailyNAVDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterMFCategoryCSV")
    public void downloadMasterMFCategoryCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterMFCategory.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterMFCategory> masterMFCategoryList = masterMFCategoryRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "description" };
 
        csvWriter.writeHeader(header);
        
        for (MasterMFCategory masterMFCategoryObj : masterMFCategoryList) {
        	MasterMFCategoryDTO  masterMFCategoryDTO = mapper.map(masterMFCategoryObj, MasterMFCategoryDTO.class);
        	
        	masterMFCategoryDTO.setId(masterMFCategoryObj.getId());
        	csvWriter.write(masterMFCategoryDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterIndexNameCSV")
    public void downloadMasterIndexNameCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterIndexName.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterIndexName> masterIndexNameList = masterIndexNameRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "name" };
 
        csvWriter.writeHeader(header);
        
        for (MasterIndexName masterIndexNameObj : masterIndexNameList) {
        	MasterIndexNameDTO  masterIndexNameDTO = mapper.map(masterIndexNameObj, MasterIndexNameDTO.class);
        	
        	masterIndexNameDTO.setId(masterIndexNameObj.getId());
        	csvWriter.write(masterIndexNameDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterIndexDailyNAVCSV")
    public void downloadMasterIndexDailyNAVCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterIndexDailyNAV.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterIndexDailyNAV> masterIndexDailyNAVList = masterIndexDailyNAVRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "indexName", "date", "nav" };
 
        csvWriter.writeHeader(header);
        
        for (MasterIndexDailyNAV masterIndexDailyNAVObj : masterIndexDailyNAVList) {
        	MasterIndexDailyNAVDTO  masterIndexDailyNAVDTO = mapper.map(masterIndexDailyNAVObj, MasterIndexDailyNAVDTO.class);
        	
        	masterIndexDailyNAVDTO.setId(masterIndexDailyNAVObj.getId());
        	masterIndexDailyNAVDTO.setIndexName(masterIndexDailyNAVObj.getMasterIndexName().getId());
        	csvWriter.write(masterIndexDailyNAVDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterExpectedHistoricalReturnCSV")
    public void downloadMasterExpectedHistoricalReturnCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterExpectedHistoricalReturn.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterExpectedHistoricalReturn> masterIndexDailyNAVList = masterExpectedHistoricalReturnRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "subAssetClassName", "assetClassName", "year", "returnRate" };
 
        csvWriter.writeHeader(header);
        
        for (MasterExpectedHistoricalReturn masterExpectedHistoricalReturnObj : masterIndexDailyNAVList) {
        	MasterExpectedHistoricalReturnDTO  masterExpectedHistoricalReturnDTO = mapper.map(masterExpectedHistoricalReturnObj, MasterExpectedHistoricalReturnDTO.class);
        	
        	masterExpectedHistoricalReturnDTO.setId(masterExpectedHistoricalReturnObj.getId());
        	masterExpectedHistoricalReturnDTO.setAssetClassName(masterExpectedHistoricalReturnObj.getLookupAssetClass().getId());
        	masterExpectedHistoricalReturnDTO.setSubAssetClassName(masterExpectedHistoricalReturnObj.getLookupAssetSubClass().getId());
        	csvWriter.write(masterExpectedHistoricalReturnDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterEquityMarketCapClassificationCSV")
    public void downloadMasterEquityMarketCapClassificationCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterEquityMarketCapClassification.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterEquityMarketCapClassification> masterEquityMarketCapClassificationList = masterEquityMarketCapClassificationRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "marketcapCategory", "rangeStart", "rangeEnd" };
 
        csvWriter.writeHeader(header);
        
        for (MasterEquityMarketCapClassification masterEquityMarketCapClassificationObj : masterEquityMarketCapClassificationList) {
        	MasterEquityMarketCapClassificationDTO  masterEquityMarketCapClassificationDTO = mapper.map(masterEquityMarketCapClassificationObj, MasterEquityMarketCapClassificationDTO.class);
        	
        	masterEquityMarketCapClassificationDTO.setId(masterEquityMarketCapClassificationObj.getId());
        	csvWriter.write(masterEquityMarketCapClassificationDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterEquityDailyPriceCSV")
    public void downloadMasterEquityDailyPriceCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterEquityDailyPrice.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterEquityDailyPrice> masterEquityDailyPriceList = masterEquityDailyPriceRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "isin", "stockName", "date", "closingPrice" };
 
        csvWriter.writeHeader(header);
        
        for (MasterEquityDailyPrice masterEquityDailyPriceObj : masterEquityDailyPriceList) {
        	MasterEquityDailyPriceDTO  masterEquityDailyPriceDTO = mapper.map(masterEquityDailyPriceObj, MasterEquityDailyPriceDTO.class);
        	
        	masterEquityDailyPriceDTO.setIsin(masterEquityDailyPriceObj.getId().getIsin());
        	masterEquityDailyPriceDTO.setDate(masterEquityDailyPriceObj.getId().getDate());
        	csvWriter.write(masterEquityDailyPriceDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterDirectEquityMarketCapCSV")
    public void downloadMasterDirectEquityMarketCapCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterDirectEquityMarketCap.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterDirectEquityMarketCap> masterDirectEquityMarketCapList = masterDirectEquityMarketCapRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "ISIN", "stockName", "marketcap", "p_e", "p_b", "divYield", "price52WeekHigh",
        		"price52WeekLow", "dailyTradedVolume", "lastClosingPrice", "classificationID" };
 
        csvWriter.writeHeader(header);
        
        for (MasterDirectEquityMarketCap masterDirectEquityMarketCapObj : masterDirectEquityMarketCapList) {
        	MasterDirectEquityMarketCapDTO  masterDirectEquityMarketCapDTO = mapper.map(masterDirectEquityMarketCapObj, MasterDirectEquityMarketCapDTO.class);
        	
        	masterDirectEquityMarketCapDTO.setISIN(masterDirectEquityMarketCapObj.getIsin());
        	masterDirectEquityMarketCapDTO.setClassificationID(masterDirectEquityMarketCapObj.getMasterEquityMarketCapClassification().getId());
        //	log.debug("P/E: " + masterDirectEquityMarketCapObj.getP_e());
        	csvWriter.write(masterDirectEquityMarketCapDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterDirectEquityCSV")
    public void downloadMasterDirectEquityCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterDirectEquity.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterDirectEquity> masterDirectEquityList = masterDirectEquityRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "isin", "stockName", "nseCode", "bseCode", "sectorID" };
 
        csvWriter.writeHeader(header);
        
        for (MasterDirectEquity masterDirectEquityObj : masterDirectEquityList) {
        	MasterDirectEquityDTO  masterDirectEquityDTO = mapper.map(masterDirectEquityObj, MasterDirectEquityDTO.class);
        	
        	masterDirectEquityDTO.setIsin(masterDirectEquityObj.getIsin());
        	masterDirectEquityDTO.setSectorID(masterDirectEquityObj.getMasterMfsector().getId());
        	csvWriter.write(masterDirectEquityDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMasterAssetClassReturnCSV")
    public void downloadMasterAssetClassReturnCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "masterAssetClassReturn.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<MasterAssetClassReturn> masterAssetClassReturnList = masterAssetClassReturnRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "assetClass", "mlr", "bcr", "wcr", "riskStdDev", "fromDate", "toDate" };
 
        csvWriter.writeHeader(header);
        
        for (MasterAssetClassReturn masterAssetClassReturnObj : masterAssetClassReturnList) {
        	MasterAssetClassReturnDTO  masterAssetClassReturnDTO = mapper.map(masterAssetClassReturnObj, MasterAssetClassReturnDTO.class);
        	
        	masterAssetClassReturnDTO.setId(masterAssetClassReturnObj.getId());
        	masterAssetClassReturnDTO.setAssetClass(masterAssetClassReturnObj.getLookupAssetClass().getId());
        	csvWriter.write(masterAssetClassReturnDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadMaster52WeekHighLowMFCSV")
    public void downloadMaster52WeekHighLowMFCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "master52WeekHighLowMF.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<Master52WeekHighLowMF> master52WeekHighLowMFList = master52WeekHighLowMFRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "amfiCode", "schemeName", "nav52WeekLow", "nav52WeekHigh", "action" };
 
        csvWriter.writeHeader(header);
        
        for (Master52WeekHighLowMF master52WeekHighLowMFObj : master52WeekHighLowMFList) {
        	Master52WeekHighLowMFDTO  master52WeekHighLowMFDTO = mapper.map(master52WeekHighLowMFObj, Master52WeekHighLowMFDTO.class);
        	
        	master52WeekHighLowMFDTO.setAmfiCode(master52WeekHighLowMFObj.getMasterMutualFundEtf().getAmfiCode());
        	csvWriter.write(master52WeekHighLowMFDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadLookupAssetSubClassCSV")
    public void downloadLookupAssetSubClassCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "lookupAssetSubClass.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<LookupAssetSubClass> lookupAssetSubClassList = lookupAssetSubClassRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "assetClass", "description" };
 
        csvWriter.writeHeader(header);
        
        for (LookupAssetSubClass lookupAssetSubClassObj : lookupAssetSubClassList) {
        	LookupAssetSubClassDTO  lookupAssetSubClassDTO = mapper.map(lookupAssetSubClassObj, LookupAssetSubClassDTO.class);
        	
        	lookupAssetSubClassDTO.setId(lookupAssetSubClassObj.getId());
        	lookupAssetSubClassDTO.setAssetClass(lookupAssetSubClassObj.getLookupAssetClass().getId());
        	csvWriter.write(lookupAssetSubClassDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadLookupAssetClassCSV")
    public void downloadLookupAssetClassCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "lookupAssetClass.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<LookupAssetClass> lookupAssetClassList = lookupAssetClassRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "description", "displayOrder" };
 
        csvWriter.writeHeader(header);
        
        for (LookupAssetClass lookupAssetClassObj : lookupAssetClassList) {
        	LookupAssetClassDTO  lookupAssetClassDTO = mapper.map(lookupAssetClassObj, LookupAssetClassDTO.class);
        	
        	lookupAssetClassDTO.setId(lookupAssetClassObj.getId());
        	csvWriter.write(lookupAssetClassDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadLookupAssetAllocationCategoryCSV")
    public void downloadLookupAssetAllocationCategoryCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "lookupAssetAllocationCategory.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<LookupAssetAllocationCategory> lookupAssetAllocationCategoryList = lookupAssetAllocationCategoryRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "description" };
 
        csvWriter.writeHeader(header);
        
        for (LookupAssetAllocationCategory obj : lookupAssetAllocationCategoryList) {
        	LookupAssetAllocationCategoryDTO  lookupAssetAllocationCategoryDTO = mapper.map(obj, LookupAssetAllocationCategoryDTO.class);
        	
        	lookupAssetAllocationCategoryDTO.setId(obj.getId());
        	csvWriter.write(lookupAssetAllocationCategoryDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadLookupAssetAllocationCSV")
    public void downloadLookupAssetAllocationCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "lookupAssetAllocation.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<LookupAssetAllocation> lookupAssetAllocationList = lookupAssetAllocationRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "assetAllocationCategory", "assetSubClass", "weightage" };
 
        csvWriter.writeHeader(header);
        
        for (LookupAssetAllocation obj : lookupAssetAllocationList) {
        	LookupAssetAllocationDTO  lookupAssetAllocationDTO = mapper.map(obj, LookupAssetAllocationDTO.class);
        	
        	lookupAssetAllocationDTO.setId(obj.getId());
        	lookupAssetAllocationDTO.setAssetAllocationCategory(obj.getLookupAssetAllocationCategory().getId());
        	lookupAssetAllocationDTO.setAssetSubClass(obj.getLookupAssetSubClass().getId());
        	csvWriter.write(lookupAssetAllocationDTO, header);
        }
 
        csvWriter.close();
    	
    }
    
    @RequestMapping(value = "/downloadLookupBucketLogicCSV")
    public void downloadLookupBucketLogicCSV(HttpServletResponse response) throws IOException {
    	String csvFileName = "lookupBucketLogic.csv";
    	 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
        List<LookupBucketLogic> lookupBucketLogicList = lookupBucketLogicRepository.findAll();
       
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        String[] header = { "id", "riskProfile", "goalHorizonBucket", "assetAllocationCategory" };
 
        csvWriter.writeHeader(header);
        
        for (LookupBucketLogic obj : lookupBucketLogicList) {
        	LookupBucketLogicDTO  lookupBucketLogicDTO = mapper.map(obj, LookupBucketLogicDTO.class);
        	
        	lookupBucketLogicDTO.setId(obj.getId());
        	lookupBucketLogicDTO.setRiskProfile(obj.getLookupRiskProfile().getId());
        	lookupBucketLogicDTO.setGoalHorizonBucket(obj.getLookupGoalHorizonBucket().getId());
        	lookupBucketLogicDTO.setAssetAllocationCategory(obj.getLookupAssetAllocationCategory().getId());
        	csvWriter.write(lookupBucketLogicDTO, header);
        }
 
        csvWriter.close();
    	
    }





*/}
