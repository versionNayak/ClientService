package com.finlabs.finexa.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.LookupAssetClass;
import com.finlabs.finexa.model.LookupAssetSubClass;
import com.finlabs.finexa.model.MasterFundManager;
import com.finlabs.finexa.model.MasterIndexName;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.MasterTablesUploadHistory;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.LookupAssetClassRepository;
import com.finlabs.finexa.repository.LookupAssetSubClassRepository;
import com.finlabs.finexa.repository.MasterFundManagerRepository;
import com.finlabs.finexa.repository.MasterIndexNameRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.MasterTablesUploadHistoryRepository;
import com.finlabs.finexa.util.FinexaUtil;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Service("MasterMutualFundETFService")
@Transactional
public class MasterMutualFundETFServiceImpl implements MasterMutualFundETFService{
	
	private static Logger log = LoggerFactory.getLogger(MasterMutualFundETFServiceImpl.class);

	@Autowired
	Mapper mapper;
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	@Autowired
	LookupAssetClassRepository lookupAssetClassRepository;
	@Autowired
	LookupAssetSubClassRepository lookupAssetSubClassRepository;
	@Autowired
	MasterMutualFundETFRepository masterMutualFundETFRepository;
	@Autowired
	MasterFundManagerRepository masterFundManagerRepository;
	@Autowired
	MasterIndexNameRepository masterIndexNameRepository;
	@Autowired
	MasterTablesUploadHistoryRepository masterTablesUploadHistoryRepository;
	
	@Override
	public WritableWorkbook downloadMasterMutualFundETFTemplate(String masterName, HttpServletResponse response) {
		
		String fileName = masterName+"Template"+".xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			
			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());
			
			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addMasterMutualFundETFExcelOutputHeader(excelOutputsheet);
			writableWorkbook.write();
			writableWorkbook.close(); 
			
		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}
		return writableWorkbook;
	
	}
	
	private void addMasterMutualFundETFExcelOutputHeader(WritableSheet sheet) throws RowsExceededException, WriteException {
		// create header row
		sheet.addCell(new Label(0, 0, "fundHouse"));
		sheet.addCell(new Label(1, 0, "schemeName"));
		sheet.addCell(new Label(2, 0, "amfiCode"));
		sheet.addCell(new Label(3, 0, "isin"));
		sheet.addCell(new Label(4, 0, "assetClassID"));
		sheet.addCell(new Label(5, 0, "subAssetClassID"));
		sheet.addCell(new Label(6, 0, "schemeOption"));
		sheet.addCell(new Label(7, 0, "closeEndedFlag"));
		sheet.addCell(new Label(8, 0, "SchemeInceptionDate"));
		sheet.addCell(new Label(9, 0, "regularDirectFlag"));
		sheet.addCell(new Label(10, 0, "schemeEndDate"));
		sheet.addCell(new Label(11, 0, "exitLoadAndPeriod"));
		sheet.addCell(new Label(12, 0, "minInvestmentAmount"));
		sheet.addCell(new Label(13, 0, "fundManagerCode"));
		sheet.addCell(new Label(14, 0, "benchmarkIndex"));
	}
	
	@Override
	public WritableWorkbook downloadMasterMutualFundETF(String masterName, HttpServletResponse response) {
		// TODO Auto-generated method stub
		log.debug("masterName in downloadMasterMutualFundETF: " + masterName);
		
		String fileName = masterName+".xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			
			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());
			
			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addMasterMutualFundETFExcelOutputHeader(excelOutputsheet);
			//creating body
			List<MasterMutualFundETF> masterMutualFundETFList = masterMutualFundETFRepository.findAll();
			addMasterMutualFundETFExcelOutputBody(masterMutualFundETFList,excelOutputsheet);
			writableWorkbook.write();
			writableWorkbook.close();
			
		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}
		return writableWorkbook;
	}
	
	private void addMasterMutualFundETFExcelOutputBody(List<MasterMutualFundETF> masterMutualFundETFList, WritableSheet sheet) throws RowsExceededException, WriteException {
		
		// creating body
		int rowIndex = 1;// because header row is already added
		if (masterMutualFundETFList != null && masterMutualFundETFList.size() > 0) {
			for (int index = 0; index < masterMutualFundETFList.size(); index ++) {
				MasterMutualFundETF obj = masterMutualFundETFList.get(index);
				sheet.addCell(new Label(0, rowIndex, obj.getFundHouse()));
				sheet.addCell(new Label(1, rowIndex, obj.getSchemeName()));
				sheet.addCell(new Label(2, rowIndex, ""+obj.getAmfiCode()));
				sheet.addCell(new Label(3, rowIndex, obj.getIsin()));
				sheet.addCell(new Label(4, rowIndex, ""+obj.getLookupAssetClass().getId()));
				sheet.addCell(new Label(5, rowIndex, ""+obj.getLookupAssetSubClass().getId()));
				sheet.addCell(new Label(6, rowIndex, obj.getSchemeOption()));
				sheet.addCell(new Label(7, rowIndex, obj.getCloseEndedFlag()));
				sheet.addCell(new Label(8, rowIndex, ""+obj.getSchemeInceptionDate()));
				sheet.addCell(new Label(9, rowIndex, obj.getRegularDirectFlag()));
				sheet.addCell(new Label(10, rowIndex, ""+obj.getSchemeEndDate()));
				sheet.addCell(new Label(11, rowIndex, obj.getExitLoadAndPeriod()));
				sheet.addCell(new Label(12, rowIndex, ""+obj.getMinInvestmentAmount()));
				sheet.addCell(new Label(13, rowIndex, ""+obj.getMasterFundManager().getManagerCode()));
				sheet.addCell(new Label(14, rowIndex, ""+obj.getMasterIndexName().getId()));
				rowIndex ++;
			}
			
		}
	
	}

	@Override
	public UploadResponseDTO uploadMasterMutualFundETF(FileuploadDTO fileUploadDTO) {
		// TODO Auto-generated method stub
		int recordsUploaded = 0;
		UploadResponseDTO resDTO = new UploadResponseDTO();
		
		log.debug("file name : " + fileUploadDTO.getFile()[0].getName());
		log.debug("file name : " + fileUploadDTO.getFile()[0].getOriginalFilename());
		//boolean success = true;
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(fileUploadDTO.getFile()[0].getInputStream());
			Sheet sheet = workbook.getSheet(0);
			if (validateMasterMutualFundETF(sheet, resDTO)){
				//save
				for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
					MasterMutualFundETF masterMutualFundETF = new MasterMutualFundETF();
					masterMutualFundETF.setFundHouse(sheet.getCell(0, rownum).getContents());
					masterMutualFundETF.setSchemeName(sheet.getCell(1, rownum).getContents());
					masterMutualFundETF.setAmfiCode(Integer.parseInt(sheet.getCell(2, rownum).getContents()));
					masterMutualFundETF.setIsin(sheet.getCell(3, rownum).getContents());
					LookupAssetClass lookupAssetClass = lookupAssetClassRepository.findById(Byte.parseByte(sheet.getCell(4, rownum).getContents()));
					if(lookupAssetClass==null){
						try {
							throw new Exception("LookupAssetClass id missmatch in this Row");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					masterMutualFundETF.setLookupAssetClass(lookupAssetClass);
					LookupAssetSubClass lookupAssetSubClass = lookupAssetSubClassRepository.findById(Byte.parseByte(sheet.getCell(5, rownum).getContents()));
					if(lookupAssetSubClass==null){
						try {
							throw new Exception("LookupAssetSubClass id missmatch in this Row");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					masterMutualFundETF.setLookupAssetSubClass(lookupAssetSubClass);
					masterMutualFundETF.setSchemeOption(sheet.getCell(6, rownum).getContents());
					masterMutualFundETF.setCloseEndedFlag(sheet.getCell(7, rownum).getContents());
					masterMutualFundETF.setSchemeInceptionDate(FinexaUtil.parseDate(sheet.getCell(8, rownum).getContents()));
					masterMutualFundETF.setRegularDirectFlag(sheet.getCell(9, rownum).getContents());
					masterMutualFundETF.setSchemeEndDate(FinexaUtil.parseDate(sheet.getCell(10, rownum).getContents()));
					masterMutualFundETF.setExitLoadAndPeriod(sheet.getCell(11, rownum).getContents());
					masterMutualFundETF.setMinInvestmentAmount(Short.parseShort(sheet.getCell(12, rownum).getContents()));
					MasterFundManager masterFundManager = masterFundManagerRepository.findByManagerCode(Integer.parseInt(sheet.getCell(13, rownum).getContents()));
					if(masterFundManager==null){
						try {
							throw new Exception("MasterFundManager id missmatch in this Row");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					masterMutualFundETF.setMasterFundManager(masterFundManager);
					MasterIndexName masterIndexName = masterIndexNameRepository.findById(Integer.parseInt(sheet.getCell(14, rownum).getContents()));
					if(masterIndexName==null){
						try {
							throw new Exception("MasterIndexName id missmatch in this Row");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					masterMutualFundETF.setMasterIndexName(masterIndexName);
					MasterMutualFundETF existingRecordsCheck = masterMutualFundETFRepository.findByAmfiCode(Integer.parseInt(sheet.getCell(2, rownum).getContents()));
					if (existingRecordsCheck != null) {
						masterMutualFundETF.setAmfiCode(existingRecordsCheck.getAmfiCode());
					}
					masterMutualFundETFRepository.save(masterMutualFundETF);
					recordsUploaded++;
				}
				
				AdvisorUser au = advisorUserRepository.findOne(fileUploadDTO.getUploadedBy());
				MasterTablesUploadHistory masterTablesUploadHistory = mapper.map(fileUploadDTO, MasterTablesUploadHistory.class);
				masterTablesUploadHistory.setAdvisorUser(au);
				masterTablesUploadHistory.setUploadDate(new Date());
				
				fileUploadDTO.setRecordsUploaded(recordsUploaded);
				
				fileUploadDTO.setStatus("S");
					
				masterTablesUploadHistory.setRecordsUploaded(fileUploadDTO.getRecordsUploaded());
				masterTablesUploadHistory.setStatus(fileUploadDTO.getStatus());
				
				masterTablesUploadHistory = masterTablesUploadHistoryRepository.save(masterTablesUploadHistory);
				fileUploadDTO = mapper.map(masterTablesUploadHistory, FileuploadDTO.class);
				fileUploadDTO.setUploadedBy(masterTablesUploadHistory.getAdvisorUser().getId());
				
			}
			
			
		} catch (IOException e) {
			//success = false;
			e.printStackTrace();
		} catch (BiffException e) {
			//success = false;
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
		
		return resDTO;
	}
	
	private boolean validateMasterMutualFundETF(Sheet sheet, UploadResponseDTO uploadResponseDTO) {
		log.debug("inside validateLookupAssetClass sheet");
		
		if (sheet.getRows() <2){
			uploadResponseDTO.getErrors().add("No Data in Excel sheet");
		}
		
		for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
			
			int displayRow =  rownum+1;
			//fundHouse
			if (StringUtils.isEmpty(sheet.getCell(0, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", A ) : " + "fundHouse should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(0, rownum).getContents())){
					if (StringUtils.isNumeric(sheet.getCell(0, rownum).getContents())){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", A ) : " + "fundHouse should not contain only numbers");
					}
				}
			}
			//schemeName
			if (StringUtils.isEmpty(sheet.getCell(1, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", B ) : " + "schemeName should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(1, rownum).getContents())){
					if (StringUtils.isNumeric(sheet.getCell(1, rownum).getContents())){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", B ) : " + "schemeName should not contain only numbers");
					}
				}
			}
			//amfiCode
			if (StringUtils.isEmpty(sheet.getCell(2, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", C ) : " + "amfiCode should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(2, rownum).getContents())){
					if (!StringUtils.isAlphanumeric(sheet.getCell(2, rownum).getContents())){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", C ) : " + "amfiCode is not valid");
					}
				}
			}
			//isin
			if (StringUtils.isEmpty(sheet.getCell(3, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", D ) : " + "isin should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(3, rownum).getContents())){
					if (!StringUtils.isAlphanumeric(sheet.getCell(3, rownum).getContents())){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", D ) : " + "isin is not valid");
					}
				}
			}
			//assetClassID
			if (StringUtils.isEmpty(sheet.getCell(4, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", E ) : " + "assetClassID should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(4, rownum).getContents())){
					LookupAssetClass lookupAssetClass = lookupAssetClassRepository.findById(Byte.parseByte(sheet.getCell(4, rownum).getContents()));
					if(lookupAssetClass==null){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", E ) : " + "assetClassID missmatch in this Row");
					} 
					if (!StringUtils.isNumeric(sheet.getCell(4, rownum).getContents())){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", E ) : " + "assetClassID should contain number");
					}
				}
			}
			//subAssetClassID
			if (StringUtils.isEmpty(sheet.getCell(5, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", F ) : " + "subAssetClassID should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(5, rownum).getContents())){
					LookupAssetSubClass lookupAssetSubClass = lookupAssetSubClassRepository.findById(Byte.parseByte(sheet.getCell(5, rownum).getContents()));
					if(lookupAssetSubClass==null){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", F ) : " + "subAssetClassID missmatch in this Row");
					}
					if (!StringUtils.isNumeric(sheet.getCell(5, rownum).getContents())){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", F ) : " + "subAssetClassID should contain number");
					}
				}
			}
			//schemeOption
			if (StringUtils.isEmpty(sheet.getCell(6, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", G ) : " + "schemeOption should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(6, rownum).getContents())){
					if (!StringUtils.isAlpha(sheet.getCell(6, rownum).getContents())){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", G ) : " + "schemeOption should contain alphabets");
					}
				}
			}
			//closeEndedFlag
			if (StringUtils.isEmpty(sheet.getCell(7, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", H ) : " + "closeEndedFlag should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(7, rownum).getContents())){
					if (!StringUtils.equalsIgnoreCase(sheet.getCell(7, rownum).getContents(), "Open Ended") && !StringUtils.equalsIgnoreCase(sheet.getCell(7, rownum).getContents(), "Close Ended")){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", H ) : " + "closeEndedFlag should be either Open Ended or Close Ended");
					}
				}
			}
			//SchemeInceptionDate
			
			//regularDirectFlag
			if (StringUtils.isEmpty(sheet.getCell(9, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", J ) : " + "regularDirectFlag should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(9, rownum).getContents())){
					if (!StringUtils.equalsIgnoreCase(sheet.getCell(9, rownum).getContents(), "Regular") && !StringUtils.equalsIgnoreCase(sheet.getCell(9, rownum).getContents(), "Direct")){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", J ) : " + "regularDirectFlag should be either Regular or Direct");
					}
				}
			}
			//schemeEndDate
			
			//exitLoadAndPeriod
			if (StringUtils.isEmpty(sheet.getCell(11, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", L ) : " + "exitLoadAndPeriod should not be Empty");
			}
			//minInvestmentAmount
			if (StringUtils.isEmpty(sheet.getCell(12, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", M ) : " + "minInvestmentAmount should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(12, rownum).getContents())){
					if (!StringUtils.isNumeric(sheet.getCell(12, rownum).getContents())){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", M ) : " + "minInvestmentAmount should contain numbers");
					} else {
						if (StringUtils.isNumeric(sheet.getCell(12, rownum).getContents())){
							if (StringUtils.length(sheet.getCell(12, rownum).getContents()) > 6){
								uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", M ) : " + "minInvestmentAmount should'nt exceed 6 digits");
							}
						}
					}
				}
			}
			//fundManagerCode
			if (StringUtils.isEmpty(sheet.getCell(13, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", N ) : " + "fundManagerCode should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(13, rownum).getContents())){
					MasterFundManager masterFundManager = masterFundManagerRepository.findByManagerCode(Integer.parseInt(sheet.getCell(13, rownum).getContents()));
					if(masterFundManager==null){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", N ) : " + "fundManagerCode missmatch in this Row");
					}
					if (!StringUtils.isNumeric(sheet.getCell(13, rownum).getContents())){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", N ) : " + "fundManagerCode should contain number");
					}
				}
			}
			//benchmarkIndex
			if (StringUtils.isEmpty(sheet.getCell(14, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", O ) : " + "benchmarkIndex should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(14, rownum).getContents())){
					MasterIndexName masterIndexName = masterIndexNameRepository.findById(Integer.parseInt(sheet.getCell(14, rownum).getContents()));
					if(masterIndexName==null){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", O ) : " + "benchmarkIndex missmatch in this Row");
					}
					if (!StringUtils.isNumeric(sheet.getCell(14, rownum).getContents())){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", O ) : " + "benchmarkIndex should contain number");
					}
				}
			}
			
			
		}
		
		log.debug("error size : " +uploadResponseDTO.getErrors().size());
		return (uploadResponseDTO.getErrors().size() >0) ? false:true;
	}
		
	
		
}
