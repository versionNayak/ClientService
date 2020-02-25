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
import com.finlabs.finexa.model.MasterTablesUploadHistory;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.LookupAssetClassRepository;
import com.finlabs.finexa.repository.MasterTablesUploadHistoryRepository;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Service("LookupAssetClassService")
@Transactional
public class LookupAssetClassServiceImpl implements LookupAssetClassService {
	
	private static Logger log = LoggerFactory.getLogger(LookupAssetClassServiceImpl.class);
	
	@Autowired
	Mapper mapper;
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	@Autowired
	LookupAssetClassRepository lookupAssetClassRepository;
	@Autowired
	MasterTablesUploadHistoryRepository masterTablesUploadHistoryRepository;
	
	@Override
	public WritableWorkbook downloadLookupAssetClassTemplate(String masterName, HttpServletResponse response) {
		
		String fileName = masterName+"Template"+".xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			
			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());
			
			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addLookupAssetClassExcelOutputHeader(excelOutputsheet);
			writableWorkbook.write();
			writableWorkbook.close(); 
			
		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}
		return writableWorkbook;
		
	}
	
	private void addLookupAssetClassExcelOutputHeader(WritableSheet sheet) throws RowsExceededException, WriteException {
		// create header row
		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "description"));
		sheet.addCell(new Label(2, 0, "displayOrder"));
	}
	
	@Override
	public WritableWorkbook downloadLookupAssetClass(String masterName, HttpServletResponse response) {
		
		log.debug("masterName in downloadLookupAssetClass: " + masterName);
		
		String fileName = masterName+".xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			
			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());
			
			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addLookupAssetClassExcelOutputHeader(excelOutputsheet);
			//creating body
			List<LookupAssetClass> lookupAssetClassList = lookupAssetClassRepository.findAll();
			addLookupAssetClassExcelOutputBody(lookupAssetClassList,excelOutputsheet);
			writableWorkbook.write();
			writableWorkbook.close(); 
		
		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}
		return writableWorkbook;
	}
	
	private void addLookupAssetClassExcelOutputBody(List<LookupAssetClass> lookupAssetClassList, WritableSheet sheet) throws RowsExceededException, WriteException {
		
		// creating body
		int rowIndex = 1;// because header row is already added
		if (lookupAssetClassList != null && lookupAssetClassList.size() > 0) {
			for (int index = 0; index < lookupAssetClassList.size(); index ++) {
				LookupAssetClass obj = lookupAssetClassList.get(index);
				sheet.addCell(new Label(0, rowIndex, ""+obj.getId()));
				sheet.addCell(new Label(1, rowIndex, obj.getDescription()));
				sheet.addCell(new Label(2, rowIndex, ""+obj.getDisplayOrder()));
				rowIndex ++;
			}
			
		}
		
	}

	@Override
	public UploadResponseDTO uploadLookupAssetClass(FileuploadDTO fileUploadDTO) {
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
			if (validateLookupAssetClass(sheet, resDTO)){
				//save
			//	log.debug("validate: " + validateLookupAssetClass(sheet, resDTO));
				for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
					LookupAssetClass lookupAssetClass = new LookupAssetClass();
					lookupAssetClass.setId(Byte.parseByte(sheet.getCell(0, rownum).getContents()));
				    lookupAssetClass.setDescription(sheet.getCell(1, rownum).getContents());
					lookupAssetClass.setDisplayOrder(Integer.parseInt(sheet.getCell(2, rownum).getContents()));
					LookupAssetClass existingRecordsCheck = lookupAssetClassRepository.findOne(Byte.parseByte(sheet.getCell(0, rownum).getContents()));
					if (existingRecordsCheck != null) {
						lookupAssetClass.setId(existingRecordsCheck.getId());
					}
					lookupAssetClassRepository.save(lookupAssetClass);
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
	
	private boolean validateLookupAssetClass(Sheet sheet, UploadResponseDTO uploadResponseDTO) {
		log.debug("inside validateLookupAssetClass sheet");
		//log.debug("total rows: " + sheet.getRows());
		if (sheet.getRows() <2){
			uploadResponseDTO.getErrors().add("No Data in Excel sheet");
		}
		
		for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
			
			int displayRow =  rownum+1;
			//ID
			if (StringUtils.isEmpty(sheet.getCell(0, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", A ) : " + "ID should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(0, rownum).getContents())){
					if (!StringUtils.isNumeric(sheet.getCell(0, rownum).getContents())){
						uploadResponseDTO.getErrors().add("Cell ("+displayRow +", A) :" + "ID should be numeric");
					}
				}
			}
			//description
			if (StringUtils.isEmpty(sheet.getCell(1, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", B ) : " + "description should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(1, rownum).getContents())){
					if (!StringUtils.isAlphaSpace(sheet.getCell(1, rownum).getContents())){
						uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", B ) : " + "description should contain alphabets");
					}
				}
			}
			//displayOrder
			if (StringUtils.isEmpty(sheet.getCell(2, rownum).getContents())){
				uploadResponseDTO.getErrors().add("Cell ("+ displayRow +", C ) : " + "displayOrder should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(2, rownum).getContents())){
					if (StringUtils.isNotEmpty(sheet.getCell(2, rownum).getContents())){
						if (!StringUtils.isNumeric(sheet.getCell(2, rownum).getContents())){
							uploadResponseDTO.getErrors().add("Cell ("+displayRow +", C) : " + "displayOrder should be numeric");
						} else {
							if (StringUtils.isNumeric(sheet.getCell(2, rownum).getContents()) && rownum != 1) {
								if (Integer.valueOf(sheet.getCell(2, rownum).getContents().trim()) != (Integer.valueOf(sheet.getCell(2, rownum-1).getContents().trim()))+1) {
									uploadResponseDTO.getErrors().add("Cell ("+displayRow +", C) : " + "displayOrder should be unique");
								}
							}
						}
					}
				}
			}
			
			
		}
		
		log.debug("error size : " +uploadResponseDTO.getErrors().size());
		return (uploadResponseDTO.getErrors().size() >0) ? false:true;
	}

}
