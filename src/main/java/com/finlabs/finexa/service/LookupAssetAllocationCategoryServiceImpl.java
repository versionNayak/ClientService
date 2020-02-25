package com.finlabs.finexa.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.LookupAssetAllocationCategory;
import com.finlabs.finexa.model.MasterTablesUploadHistory;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.LookupAssetAllocationCategoryRepository;
import com.finlabs.finexa.repository.MasterTablesUploadHistoryRepository;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Service("LookupAssetAllocationCategoryService")
@Transactional
public class LookupAssetAllocationCategoryServiceImpl implements LookupAssetAllocationCategoryService {

	private static Logger log = LoggerFactory.getLogger(LookupAssetAllocationCategoryServiceImpl.class);

	@Autowired
	Mapper mapper;
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	@Autowired
	LookupAssetAllocationCategoryRepository lookupAssetAllocationCategoryRepository;
	@Autowired
	MasterTablesUploadHistoryRepository masterTablesUploadHistoryRepository;

	private void addLookupAssetAllocationCategoryExcelOutputHeader(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		// create header row
		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "description"));

	}

	@Override
	public WritableWorkbook downloadLookupAssetAllocationCategoryTemplate(String masterName,
			HttpServletResponse response) throws RuntimeException, IOException, RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		try {
			log.debug("masterName in downloadLookupAssetAllocationCategoryTemplate: " + masterName);

			String fileName = masterName + "Template" + ".xls";
			WritableWorkbook writableWorkbook = null;

			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addLookupAssetAllocationCategoryExcelOutputHeader(excelOutputsheet);

			writableWorkbook.write();
			writableWorkbook.close();

			return writableWorkbook;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public WritableWorkbook downloadLookupAssetAllocationCategory(String masterName, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String fileName = masterName + ".xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addLookupAssetAllocationCategoryExcelOutputHeader(excelOutputsheet);
			// creating body
			List<LookupAssetAllocationCategory> lookupAssetAllocationCategoryList = lookupAssetAllocationCategoryRepository
					.findAll();
			addLookupAssetAllocationCategoryExcelOutputBody(lookupAssetAllocationCategoryList, excelOutputsheet);
			writableWorkbook.write();
			writableWorkbook.close();

		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}
		return writableWorkbook;
	}

	private void addLookupAssetAllocationCategoryExcelOutputBody(
			List<LookupAssetAllocationCategory> lookupAssetAllocationCategoryList, WritableSheet sheet)
			throws RowsExceededException, WriteException {
		// creating body
		int rowIndex = 1;// because header row is already added
		if (lookupAssetAllocationCategoryList != null && lookupAssetAllocationCategoryList.size() > 0) {
			for (int index = 0; index < lookupAssetAllocationCategoryList.size(); index++) {
				LookupAssetAllocationCategory obj = lookupAssetAllocationCategoryList.get(index);
				sheet.addCell(new Label(0, rowIndex, "" + obj.getId()));
				sheet.addCell(new Label(1, rowIndex, obj.getDescription()));
				rowIndex++;
			}
		}
	}

	@Override
	public UploadResponseDTO uploadLookupAssetAllocationCategory(FileuploadDTO fileUploadDTO) throws RuntimeException, BiffException, IOException {
		// TODO Auto-generated method stub
		Workbook workbook = null;
		try {
			int recordsUploaded = 0;
			UploadResponseDTO resDTO = new UploadResponseDTO();

			log.debug("file name : " + fileUploadDTO.getFile()[0].getName());
			log.debug("file name : " + fileUploadDTO.getFile()[0].getOriginalFilename());
			// boolean success = true;
			
			workbook = Workbook.getWorkbook(fileUploadDTO.getFile()[0].getInputStream());
			Sheet sheet = workbook.getSheet(0);
			//Sheet sheet = null;
			if (validateLookupAssetAllocationCategory(sheet, resDTO)) {
				// save
				for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
					LookupAssetAllocationCategory lookupAssetAllocationCategory = new LookupAssetAllocationCategory();
					lookupAssetAllocationCategory.setId(Integer.valueOf(sheet.getCell(0, rownum).getContents()));
					lookupAssetAllocationCategory.setDescription(sheet.getCell(1, rownum).getContents().toUpperCase());
					LookupAssetAllocationCategory existingRecordsCheck = lookupAssetAllocationCategoryRepository
							.findOne(Integer.parseInt(sheet.getCell(0, rownum).getContents()));
					if (existingRecordsCheck != null) {
						lookupAssetAllocationCategory.setId(existingRecordsCheck.getId());
						lookupAssetAllocationCategory.setDescription(existingRecordsCheck.getDescription());
					}
					lookupAssetAllocationCategoryRepository.save(lookupAssetAllocationCategory);
					recordsUploaded++;
				}

				AdvisorUser au = advisorUserRepository.findOne(fileUploadDTO.getUploadedBy());
				MasterTablesUploadHistory masterTablesUploadHistory = mapper.map(fileUploadDTO,
						MasterTablesUploadHistory.class);
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

			return resDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
	}

	private boolean validateLookupAssetAllocationCategory(Sheet sheet, UploadResponseDTO uploadResponseDTO) {

		log.debug("inside validateLookupAssetAllocationCategory sheet");
		// log.debug("total rows: " + sheet.getRows());
		if (sheet.getRows() < 2) {
			uploadResponseDTO.getErrors().add("No Data in Excel sheet");
		}

		for (int rownum = 1; rownum < sheet.getRows(); rownum++) {

			int displayRow = rownum + 1;
			// ID
			if (StringUtils.isEmpty(sheet.getCell(0, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", A ) : " + "ID should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(0, rownum).getContents())) {
					if (!StringUtils.isNumeric(sheet.getCell(0, rownum).getContents())) {
						uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", A) :" + "ID should be numeric");
					}
				}
			}
			// description
			if (StringUtils.isEmpty(sheet.getCell(1, rownum).getContents())) {
				uploadResponseDTO.getErrors()
						.add("Cell (" + displayRow + ", B ) : " + "description should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(1, rownum).getContents())) {
					if (!StringUtils.isAlphanumeric(sheet.getCell(1, rownum).getContents())) {
						uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", B ) : "
								+ "description should contain alphabets and numbers");
					}
				}
			}

		}

		log.debug("error size : " + uploadResponseDTO.getErrors().size());
		return (uploadResponseDTO.getErrors().size() > 0) ? false : true;
	}

	@Override
	public void downloadLookupAssetAllocationCategoryTemplateCSV(HttpServletResponse response)
			throws RuntimeException, IOException {
		// TODO Auto-generated method stub
		try {
			String csvFileName = "Model Portfolio Master Template.csv";

			response.setContentType("text/csv");

			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
			response.setHeader(headerKey, headerValue);

			// uses the Super CSV API to generate CSV data from the model data
			ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
			//ICsvBeanWriter csvWriter = null;
			String[] header = { "id", "description" };

			csvWriter.writeHeader(header);
			csvWriter.close();
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public UploadResponseDTO uploadLookupAssetAllocationCategoryCSV(FileuploadDTO fileuploadDTO)
			throws RuntimeException, IOException {

		int recordsUploaded = 0;
		UploadResponseDTO resDTO = new UploadResponseDTO();
		log.debug("file name : " + fileuploadDTO.getFile()[0].getName());
		log.debug("file name : " + fileuploadDTO.getFile()[0].getOriginalFilename());
		String line = "";
		int rowNum = 0;
		String cvsSplitBy = ",";
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream()))) {

			while ((line = br.readLine()) != null) {

				if (rowNum == 0) {
					rowNum++;
					continue;
				}

				LookupAssetAllocationCategory lookupAssetAllocationCategory = new LookupAssetAllocationCategory();
				String[] fields = line.split(cvsSplitBy);
				// ---- set all other properties and then save the clientinfo as
				// csv below
				lookupAssetAllocationCategory.setId(Integer.valueOf(fields[0]));
				lookupAssetAllocationCategory.setDescription(fields[1].toUpperCase());
				LookupAssetAllocationCategory existingRecordsCheck = lookupAssetAllocationCategoryRepository
						.findOne(Integer.parseInt(fields[0]));
				if (existingRecordsCheck != null) {
					lookupAssetAllocationCategory.setId(existingRecordsCheck.getId());
				}
				lookupAssetAllocationCategoryRepository.save(lookupAssetAllocationCategory);
				recordsUploaded++;

			}

			AdvisorUser au = advisorUserRepository.findOne(fileuploadDTO.getUploadedBy());
			MasterTablesUploadHistory masterTablesUploadHistory = mapper.map(fileuploadDTO,
					MasterTablesUploadHistory.class);
			masterTablesUploadHistory.setAdvisorUser(au);
			masterTablesUploadHistory.setUploadDate(new Date());

			fileuploadDTO.setRecordsUploaded(recordsUploaded);

			fileuploadDTO.setStatus("S");

			masterTablesUploadHistory.setRecordsUploaded(fileuploadDTO.getRecordsUploaded());
			masterTablesUploadHistory.setStatus(fileuploadDTO.getStatus());

			masterTablesUploadHistory = masterTablesUploadHistoryRepository.save(masterTablesUploadHistory);
			fileuploadDTO = mapper.map(masterTablesUploadHistory, FileuploadDTO.class);
			fileuploadDTO.setUploadedBy(masterTablesUploadHistory.getAdvisorUser().getId());

		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		return resDTO;
	}

}
