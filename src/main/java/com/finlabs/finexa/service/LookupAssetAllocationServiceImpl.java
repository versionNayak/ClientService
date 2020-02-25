package com.finlabs.finexa.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
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
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.LookupAssetAllocation;
import com.finlabs.finexa.model.LookupAssetAllocationCategory;
import com.finlabs.finexa.model.LookupAssetSubClass;
import com.finlabs.finexa.model.MasterTablesUploadHistory;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.LookupAssetAllocationCategoryRepository;
import com.finlabs.finexa.repository.LookupAssetAllocationRepository;
import com.finlabs.finexa.repository.LookupAssetSubClassRepository;
import com.finlabs.finexa.repository.MasterTablesUploadHistoryRepository;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Service("LookupAssetAllocationService")
@Transactional
public class LookupAssetAllocationServiceImpl implements LookupAssetAllocationService {

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private static Logger log = LoggerFactory.getLogger(LookupAssetAllocationServiceImpl.class);

	@Autowired
	Mapper mapper;
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	@Autowired
	LookupAssetAllocationRepository lookupAssetAllocationRepository;
	@Autowired
	LookupAssetAllocationCategoryRepository lookupAssetAllocationCategoryRepository;
	@Autowired
	LookupAssetSubClassRepository lookupAssetSubClassRepository;
	@Autowired
	MasterTablesUploadHistoryRepository masterTablesUploadHistoryRepository;

	private void addLookupAssetAllocationExcelOutputHeader(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		// create header row
		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "assetAllocationCategory"));
		sheet.addCell(new Label(2, 0, "assetSubClass"));
		sheet.addCell(new Label(3, 0, "weightage"));
		sheet.addCell(new Label(4, 0, "fromDate"));
		sheet.addCell(new Label(5, 0, "toDate"));
	}

	@Override
	public WritableWorkbook downloadLookupAssetAllocationTemplate(String masterName, HttpServletResponse response)
			throws RuntimeException, IOException, RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		try {
			log.debug("masterName in downloadLookupAssetAllocationTemplate: " + masterName);

			String fileName = masterName + "Template" + ".xls";
			WritableWorkbook writableWorkbook = null;

			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addLookupAssetAllocationExcelOutputHeader(excelOutputsheet);

			WritableSheet assetAllocationCategory = writableWorkbook.createSheet("Asset Allocation Category", 1);
			addAssetAllocationCategoryHeader(assetAllocationCategory);
			addAssetAllocationCategoryData(assetAllocationCategory);

			WritableSheet assetSubClass = writableWorkbook.createSheet("Asset Sub Class", 2);
			addAssetSubClassHeader(assetSubClass);
			addAssetSubClassData(assetSubClass);

			writableWorkbook.write();
			writableWorkbook.close();

			return writableWorkbook;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private void addAssetAllocationCategoryHeader(WritableSheet sheet) throws RowsExceededException, WriteException {
		// create header row
		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "description"));

	}

	private void addAssetAllocationCategoryData(WritableSheet sheet) throws RowsExceededException, WriteException {
		// create header row
		int i = 1;
		for (LookupAssetAllocationCategory obj : lookupAssetAllocationCategoryRepository.findAll()) {
			sheet.addCell(new Label(0, i, "" + obj.getId()));
			sheet.addCell(new Label(1, i, obj.getDescription()));
			i++;
		}

	}

	private void addAssetSubClassHeader(WritableSheet sheet) throws RowsExceededException, WriteException {
		// create header row
		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "assetClass"));
		sheet.addCell(new Label(2, 0, "description"));

	}

	private void addAssetSubClassData(WritableSheet sheet) throws RowsExceededException, WriteException {
		// create header row
		int i = 1;
		for (LookupAssetSubClass obj : lookupAssetSubClassRepository.findAll()) {
			sheet.addCell(new Label(0, i, "" + obj.getId()));
			sheet.addCell(new Label(1, i, "" + obj.getLookupAssetClass().getId()));
			sheet.addCell(new Label(2, i, obj.getDescription()));
			i++;
		}

	}

	@Override
	public WritableWorkbook downloadLookupAssetAllocation(String masterName, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String fileName = masterName + ".xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addLookupAssetAllocationExcelOutputHeader(excelOutputsheet);
			// creating body
			List<LookupAssetAllocation> lookupAssetAllocationList = lookupAssetAllocationRepository.findAll();
			addLookupAssetAllocationExcelOutputBody(lookupAssetAllocationList, excelOutputsheet);

			WritableSheet assetAllocationCategory = writableWorkbook.createSheet("Asset Allocation Category", 1);
			addAssetAllocationCategoryHeader(assetAllocationCategory);
			addAssetAllocationCategoryData(assetAllocationCategory);

			WritableSheet assetSubClass = writableWorkbook.createSheet("Asset Sub Class", 2);
			addAssetSubClassHeader(assetSubClass);
			addAssetSubClassData(assetSubClass);

			writableWorkbook.write();
			writableWorkbook.close();

		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}
		return writableWorkbook;
	}

	private void addLookupAssetAllocationExcelOutputBody(List<LookupAssetAllocation> lookupAssetAllocationList,
			WritableSheet sheet) throws RowsExceededException, WriteException {
		// creating body
		int rowIndex = 1;// because header row is already added
		if (lookupAssetAllocationList != null && lookupAssetAllocationList.size() > 0) {
			for (int index = 0; index < lookupAssetAllocationList.size(); index++) {
				LookupAssetAllocation obj = lookupAssetAllocationList.get(index);
				sheet.addCell(new Label(0, rowIndex, "" + obj.getId()));
				sheet.addCell(new Label(1, rowIndex, obj.getLookupAssetAllocationCategory().getDescription()));
				sheet.addCell(new Label(2, rowIndex, obj.getLookupAssetSubClass().getDescription()));
				sheet.addCell(new Label(3, rowIndex, "" + obj.getWeightage()));
				sheet.addCell(new Label(4, rowIndex, "" + obj.getFromDate()));
				sheet.addCell(new Label(5, rowIndex, "" + obj.getToDate()));
				rowIndex++;
			}
		}
	}

	@Override
	public UploadResponseDTO uploadLookupAssetAllocation(FileuploadDTO fileUploadDTO)
			throws RuntimeException, BiffException, IOException {
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
			// Sheet sheet = null;
			if (validateLookupAssetAllocation(sheet, resDTO)) {
				// save
				for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
					LookupAssetAllocation lookupAssetAllocation = new LookupAssetAllocation();
					lookupAssetAllocation.setId(Integer.parseInt(sheet.getCell(0, rownum).getContents()));
					lookupAssetAllocation.setLookupAssetAllocationCategory(lookupAssetAllocationCategoryRepository
							.findByDescription(sheet.getCell(1, rownum).getContents().toUpperCase()));
					lookupAssetAllocation.setLookupAssetSubClass(
							lookupAssetSubClassRepository.findByDescription(sheet.getCell(2, rownum).getContents()));
					lookupAssetAllocation.setWeightage(new BigDecimal(sheet.getCell(3, rownum).getContents()));
					
					String fromdate = sheet.getCell(4, rownum).getContents();
					long fromdateLong = new SimpleDateFormat("dd/MM/yyyy").parse(fromdate, new ParsePosition(0)).getTime();
					java.sql.Date dbFromDate = new java.sql.Date(fromdateLong);
					System.out.println(dbFromDate);
					lookupAssetAllocation.setFromDate(dbFromDate);
					
					String todate = sheet.getCell(5, rownum).getContents();
					long todateLong = new SimpleDateFormat("dd/MM/yyyy").parse(todate, new ParsePosition(0)).getTime();
					java.sql.Date dbToDate = new java.sql.Date(todateLong);
					System.out.println(dbToDate);
					lookupAssetAllocation.setToDate(dbToDate);
				
					LookupAssetAllocation existingRecordsCheck = lookupAssetAllocationRepository
							.findOne(Integer.parseInt(sheet.getCell(0, rownum).getContents()));
					if (existingRecordsCheck != null) {
						lookupAssetAllocation.setId(existingRecordsCheck.getId());
						lookupAssetAllocation.setLookupAssetAllocationCategory(existingRecordsCheck.getLookupAssetAllocationCategory());
						lookupAssetAllocation.setLookupAssetSubClass(existingRecordsCheck.getLookupAssetSubClass());
						lookupAssetAllocation.setWeightage(existingRecordsCheck.getWeightage());
						lookupAssetAllocation.setFromDate(existingRecordsCheck.getFromDate());
						lookupAssetAllocation.setToDate(existingRecordsCheck.getToDate());
					}
					lookupAssetAllocationRepository.save(lookupAssetAllocation);
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

	private boolean validateLookupAssetAllocation(Sheet sheet, UploadResponseDTO uploadResponseDTO) {

		log.debug("inside validateLookupAssetAllocation sheet");
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
			// assetAllocationCategory
			if (StringUtils.isEmpty(sheet.getCell(1, rownum).getContents())) {
				uploadResponseDTO.getErrors()
						.add("Cell (" + displayRow + ", B ) : " + "assetAllocationCategory should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(1, rownum).getContents())) {
					if (!StringUtils.isAlphanumeric(sheet.getCell(1, rownum).getContents())) {
						uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", B ) : "
								+ "assetAllocationCategory should contain alphabets and numbers");
					} else {
						if (StringUtils.isAlphanumeric(sheet.getCell(1, rownum).getContents())) {
							LookupAssetAllocationCategory lookupAssetAllocationCategory = lookupAssetAllocationCategoryRepository
									.findByDescription(sheet.getCell(1, rownum).getContents());
							if (lookupAssetAllocationCategory == null) {
								uploadResponseDTO.getErrors().add(
										"Cell (" + displayRow + ", B ) : " + "assetAllocationCategory is not valid");
							}
						}
					}
				}
			}
			// assetSubClass
			if (StringUtils.isEmpty(sheet.getCell(2, rownum).getContents())) {
				uploadResponseDTO.getErrors()
						.add("Cell (" + displayRow + ", C ) : " + "assetSubClass should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(2, rownum).getContents())) {
					if (!StringUtils.isAsciiPrintable(sheet.getCell(2, rownum).getContents())) {
						uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", C ) : "
								+ "assetSubClass may contain alphabets, characters and numbers");
					} else {
						if (StringUtils.isAsciiPrintable(sheet.getCell(2, rownum).getContents())) {
							LookupAssetSubClass lookupAssetSubClass = lookupAssetSubClassRepository
									.findByDescription(sheet.getCell(2, rownum).getContents());
							if (lookupAssetSubClass == null) {
								uploadResponseDTO.getErrors()
										.add("Cell (" + displayRow + ", C ) : " + "assetSubClass is not valid");
							}
						}
					}
				}
			}
			// weightage
			if (StringUtils.isEmpty(sheet.getCell(3, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D ) : " + "weightage should not be Empty");
			} else {
				if (StringUtils.isAlpha(sheet.getCell(3, rownum).getContents())
						|| StringUtils.isAlphanumeric(sheet.getCell(3, rownum).getContents())
						|| StringUtils.isAlphaSpace(sheet.getCell(3, rownum).getContents())
						|| StringUtils.isAlphanumericSpace(sheet.getCell(3, rownum).getContents())) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D ) : " + "weightage must be decimal");
				}
			}
			/*// fromDate
			if (StringUtils.isEmpty(sheet.getCell(4, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", E ) : " + "fromDate should not be Empty");
			} else {
				if (!StringUtils.containsAny(sheet.getCell(4, rownum).getContents(), "-")
						|| StringUtils.isAlphaSpace(sheet.getCell(4, rownum).getContents())
						|| StringUtils.isAlphanumericSpace(sheet.getCell(4, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", E ) : " + "fromDate must be of yyyy-MM-dd format");
				}
			}
			// toDate
			if (StringUtils.isEmpty(sheet.getCell(5, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", F ) : " + "toDate should not be Empty");
			} else {
				if (!StringUtils.containsAny(sheet.getCell(5, rownum).getContents(), "-")
						|| StringUtils.isAlphaSpace(sheet.getCell(5, rownum).getContents())
						|| StringUtils.isAlphanumericSpace(sheet.getCell(5, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", F ) : " + "toDate must be of yyyy-MM-dd format");
				}
			}*/

		}

		log.debug("error size : " + uploadResponseDTO.getErrors().size());
		return (uploadResponseDTO.getErrors().size() > 0) ? false : true;

	}

	@Override
	public void downloadLookupAssetAllocationTemplateCSV(HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		try {
			String csvFileName = "Model Portfolio Allocation Template.csv";

			response.setContentType("text/csv");

			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
			response.setHeader(headerKey, headerValue);

			// uses the Super CSV API to generate CSV data from the model data
			ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

			String[] header = { "id", "assetAllocationCategory", "assetSubClass", "weightage", "fromDate", "toDate" };

			csvWriter.writeHeader(header);
			csvWriter.close();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public UploadResponseDTO uploadLookupAssetAllocationCSV(FileuploadDTO fileuploadDTO)
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
				LookupAssetAllocation lookupAssetAllocation = new LookupAssetAllocation();
				String[] fields = line.split(cvsSplitBy);
				// ---- set all other properties and then save the clientinfo as
				// csv below
				lookupAssetAllocation.setId(Integer.valueOf(fields[0]));
				lookupAssetAllocation.setLookupAssetAllocationCategory(
						lookupAssetAllocationCategoryRepository.findByDescription(fields[1].toUpperCase()));
				lookupAssetAllocation
						.setLookupAssetSubClass(lookupAssetSubClassRepository.findByDescription(fields[2]));
				lookupAssetAllocation.setWeightage(new BigDecimal(fields[3]));
				Date fromdate;
				Date todate;
				try {
					fromdate = (Date) formatter.parse(fields[4]);
					lookupAssetAllocation.setFromDate(fromdate);
					todate = (Date) formatter.parse(fields[5]);
					lookupAssetAllocation.setToDate(todate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LookupAssetAllocation existingRecordsCheck = lookupAssetAllocationRepository
						.findOne(Integer.parseInt(fields[0]));
				if (existingRecordsCheck != null) {
					lookupAssetAllocation.setId(existingRecordsCheck.getId());
				}
				lookupAssetAllocationRepository.save(lookupAssetAllocation);
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
