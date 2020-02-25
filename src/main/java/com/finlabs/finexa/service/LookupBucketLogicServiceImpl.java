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
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.LookupAssetAllocationCategory;
import com.finlabs.finexa.model.LookupBucketLogic;
import com.finlabs.finexa.model.LookupGoalHorizonBucket;
import com.finlabs.finexa.model.LookupRiskProfile;
import com.finlabs.finexa.model.MasterTablesUploadHistory;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.LookupAssetAllocationCategoryRepository;
import com.finlabs.finexa.repository.LookupBucketLogicRepository;
import com.finlabs.finexa.repository.LookupGoalHorizonBucketRepository;
import com.finlabs.finexa.repository.LookupRiskProfileRepository;
import com.finlabs.finexa.repository.MasterTablesUploadHistoryRepository;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Service("LookupBucketLogicService")
@Transactional
public class LookupBucketLogicServiceImpl implements LookupBucketLogicService {

	private static Logger log = LoggerFactory.getLogger(LookupBucketLogicServiceImpl.class);

	@Autowired
	Mapper mapper;
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	@Autowired
	LookupBucketLogicRepository lookupBucketLogicRepository;
	@Autowired
	LookupRiskProfileRepository lookupRiskProfileRepository;
	@Autowired
	LookupGoalHorizonBucketRepository lookupGoalHorizonBucketRepository;
	@Autowired
	LookupAssetAllocationCategoryRepository lookupAssetAllocationCategoryRepository;
	@Autowired
	MasterTablesUploadHistoryRepository masterTablesUploadHistoryRepository;

	private void addLookupBucketLogicExcelOutputHeader(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		// create header row
		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "riskProfile"));
		sheet.addCell(new Label(2, 0, "goalHorizonBucket"));
		sheet.addCell(new Label(3, 0, "assetAllocationCategory"));

	}

	@Override
	public WritableWorkbook downloadLookupBucketLogicTemplate(String masterName, HttpServletResponse response)
			throws RuntimeException, IOException, RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		try {
			log.debug("masterName in downloadLookupBucketLogicTemplate: " + masterName);

			String fileName = masterName + "Template" + ".xls";
			WritableWorkbook writableWorkbook = null;

			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addLookupBucketLogicExcelOutputHeader(excelOutputsheet);

			WritableSheet riskProfile = writableWorkbook.createSheet("Risk Profile", 1);
			addRiskProfileHeader(riskProfile);
			addRiskProfileData(riskProfile);

			WritableSheet goalHorizonBucket = writableWorkbook.createSheet("Goal Horizon Bucket", 2);
			addGoalHorizonBucketHeader(goalHorizonBucket);
			addGoalHorizonBucketData(goalHorizonBucket);

			WritableSheet assetAllocationCategory = writableWorkbook.createSheet("Asset Allocation Category", 3);
			addAssetAllocationCategoryHeader(assetAllocationCategory);
			addAssetAllocationCategoryData(assetAllocationCategory);

			writableWorkbook.write();
			writableWorkbook.close();

			return writableWorkbook;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private void addAssetAllocationCategoryData(WritableSheet sheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		int i = 1;
		for (LookupAssetAllocationCategory obj : lookupAssetAllocationCategoryRepository.findAll()) {
			sheet.addCell(new Label(0, i, "" + obj.getId()));
			sheet.addCell(new Label(1, i, obj.getDescription()));
			i++;
		}

	}

	private void addAssetAllocationCategoryHeader(WritableSheet sheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "description"));

	}

	private void addGoalHorizonBucketData(WritableSheet sheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		int i = 1;
		for (LookupGoalHorizonBucket obj : lookupGoalHorizonBucketRepository.findAll()) {
			sheet.addCell(new Label(0, i, "" + obj.getId()));
			sheet.addCell(new Label(1, i, obj.getBucketCode()));
			sheet.addCell(new Label(2, i, "" + obj.getLCMonthToGoal()));
			sheet.addCell(new Label(3, i, "" + obj.getUCMonthToGoal()));
			i++;
		}

	}

	private void addGoalHorizonBucketHeader(WritableSheet sheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "bucketCode"));
		sheet.addCell(new Label(2, 0, "LCMonthToGoal"));
		sheet.addCell(new Label(3, 0, "UCMonthToGoal"));

	}

	private void addRiskProfileData(WritableSheet sheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		int i = 1;
		for (LookupRiskProfile obj : lookupRiskProfileRepository.findAll()) {
			sheet.addCell(new Label(0, i, "" + obj.getId()));
			sheet.addCell(new Label(1, i, obj.getDescription()));
			i++;
		}

	}

	private void addRiskProfileHeader(WritableSheet sheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "description"));

	}

	@Override
	public WritableWorkbook downloadLookupBucketLogic(String masterName, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String fileName = masterName + ".xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addLookupBucketLogicExcelOutputHeader(excelOutputsheet);
			// creatingBody
			List<LookupBucketLogic> lookupBucketLogicList = lookupBucketLogicRepository.findAll();
			addLookupBucketLogicExcelOutputBody(lookupBucketLogicList, excelOutputsheet);

			WritableSheet riskProfile = writableWorkbook.createSheet("Risk Profile", 1);
			addRiskProfileHeader(riskProfile);
			addRiskProfileData(riskProfile);

			WritableSheet goalHorizonBucket = writableWorkbook.createSheet("Goal Horizon Bucket", 2);
			addGoalHorizonBucketHeader(goalHorizonBucket);
			addGoalHorizonBucketData(goalHorizonBucket);

			WritableSheet assetAllocationCategory = writableWorkbook.createSheet("Asset Allocation Category", 3);
			addAssetAllocationCategoryHeader(assetAllocationCategory);
			addAssetAllocationCategoryData(assetAllocationCategory);

			writableWorkbook.write();
			writableWorkbook.close();

		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}
		return writableWorkbook;
	}

	private void addLookupBucketLogicExcelOutputBody(List<LookupBucketLogic> lookupBucketLogicList, WritableSheet sheet)
			throws RowsExceededException, WriteException {
		// creating Body
		int rowIndex = 1;// because header row is already added
		if (lookupBucketLogicList != null && lookupBucketLogicList.size() > 0) {
			for (int index = 0; index < lookupBucketLogicList.size(); index++) {
				LookupBucketLogic obj = lookupBucketLogicList.get(index);
				sheet.addCell(new Label(0, rowIndex, "" + obj.getId()));
				sheet.addCell(new Label(1, rowIndex, obj.getLookupRiskProfile().getDescription()));
				sheet.addCell(new Label(2, rowIndex, "" + obj.getLookupGoalHorizonBucket().getId()));
				sheet.addCell(new Label(3, rowIndex, obj.getLookupAssetAllocationCategory().getDescription()));
				rowIndex++;
			}
		}

	}

	@Override
	public UploadResponseDTO uploadLookupBucketLogic(FileuploadDTO fileUploadDTO)
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
			if (validateLookupBucketLogic(sheet, resDTO)) {
				// save
				for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
					LookupBucketLogic lookupBucketLogic = new LookupBucketLogic();
					lookupBucketLogic.setId(Integer.parseInt(sheet.getCell(0, rownum).getContents()));
					lookupBucketLogic.setLookupRiskProfile(
							lookupRiskProfileRepository.findByDescription(sheet.getCell(1, rownum).getContents()));
					lookupBucketLogic.setLookupGoalHorizonBucket(lookupGoalHorizonBucketRepository
							.findById(Integer.parseInt(sheet.getCell(2, rownum).getContents())));
					lookupBucketLogic.setLookupAssetAllocationCategory(lookupAssetAllocationCategoryRepository
							.findByDescription(sheet.getCell(3, rownum).getContents().toUpperCase()));
					LookupBucketLogic existingRecordsCheck = lookupBucketLogicRepository
							.findOne(Integer.parseInt(sheet.getCell(0, rownum).getContents()));
					if (existingRecordsCheck != null) {
						lookupBucketLogic.setId(existingRecordsCheck.getId());
						lookupBucketLogic.setLookupRiskProfile(existingRecordsCheck.getLookupRiskProfile());
						lookupBucketLogic.setLookupGoalHorizonBucket(existingRecordsCheck.getLookupGoalHorizonBucket());
						lookupBucketLogic.setLookupAssetAllocationCategory(existingRecordsCheck.getLookupAssetAllocationCategory());
					}
					lookupBucketLogicRepository.save(lookupBucketLogic);
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
			// success = false;
			throw new RuntimeException(e);
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}

	}

	private boolean validateLookupBucketLogic(Sheet sheet, UploadResponseDTO uploadResponseDTO) {
		// TODO Auto-generated method stub
		log.debug("inside validateLookupBucketLogic sheet");
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
			// riskProfile
			if (StringUtils.isEmpty(sheet.getCell(1, rownum).getContents())) {
				uploadResponseDTO.getErrors()
						.add("Cell (" + displayRow + ", B ) : " + "riskProfile should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(1, rownum).getContents())) {
					if (!StringUtils.isAlphaSpace(sheet.getCell(1, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", B ) : " + "riskProfile should contain alphabets");
					} else {
						if (StringUtils.isAlphaSpace(sheet.getCell(1, rownum).getContents())) {
							LookupRiskProfile lookupRiskProfile = lookupRiskProfileRepository
									.findByDescription(sheet.getCell(1, rownum).getContents());
							if (lookupRiskProfile == null) {
								uploadResponseDTO.getErrors()
										.add("Cell (" + displayRow + ", B ) : " + "riskProfile is not valid");
							}
						}
					}
				}
			}
			// goalHorizonBucket
			if (StringUtils.isEmpty(sheet.getCell(2, rownum).getContents())) {
				uploadResponseDTO.getErrors()
						.add("Cell (" + displayRow + ", C ) : " + "goalHorizonBucket should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(2, rownum).getContents())) {
					if (!StringUtils.isNumeric(sheet.getCell(2, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", C ) : " + "goalHorizonBucket should be in numbers");
					} else {
						if (StringUtils.isNumeric(sheet.getCell(2, rownum).getContents())) {
							LookupGoalHorizonBucket lookupGoalHorizonBucket = lookupGoalHorizonBucketRepository
									.findById(Integer.parseInt(sheet.getCell(2, rownum).getContents()));
							if (lookupGoalHorizonBucket == null) {
								uploadResponseDTO.getErrors()
										.add("Cell (" + displayRow + ", C ) : " + "goalHorizonBucket is not valid");
							}
						}
					}
				}
			}
			// assetAllocationCategory
			if (StringUtils.isEmpty(sheet.getCell(3, rownum).getContents())) {
				uploadResponseDTO.getErrors()
						.add("Cell (" + displayRow + ", D ) : " + "assetAllocationCategory should not be Empty");
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(3, rownum).getContents())) {
					if (!StringUtils.isAlphanumeric(sheet.getCell(3, rownum).getContents())) {
						uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D ) : "
								+ "assetAllocationCategory should contain alphabets and numbers");
					} else {
						if (StringUtils.isAlphanumeric(sheet.getCell(3, rownum).getContents())) {
							LookupAssetAllocationCategory ookupAssetAllocationCategory = lookupAssetAllocationCategoryRepository
									.findByDescription(sheet.getCell(3, rownum).getContents());
							if (ookupAssetAllocationCategory == null) {
								uploadResponseDTO.getErrors().add(
										"Cell (" + displayRow + ", D ) : " + "assetAllocationCategory is not valid");
							}
						}
					}
				}
			}

		}

		log.debug("error size : " + uploadResponseDTO.getErrors().size());
		return (uploadResponseDTO.getErrors().size() > 0) ? false : true;
	}

	@Override
	public void downloadLookupBucketLogicTemplateCSV(HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		try {
			String csvFileName = "Bucketing Logic Template.csv";

			response.setContentType("text/csv");

			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
			response.setHeader(headerKey, headerValue);

			// uses the Super CSV API to generate CSV data from the model data
			ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

			String[] header = { "id", "riskProfile", "goalHorizonBucket", "assetAllocationCategory" };

			csvWriter.writeHeader(header);
			csvWriter.close();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public UploadResponseDTO uploadLookupBucketLogicCSV(FileuploadDTO fileuploadDTO)
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
				LookupBucketLogic lookupBucketLogic = new LookupBucketLogic();
				String[] fields = line.split(cvsSplitBy);
				// ---- set all other properties and then save the clientinfo as
				// csv below
				lookupBucketLogic.setId(Integer.valueOf(fields[0]));
				lookupBucketLogic.setLookupRiskProfile(lookupRiskProfileRepository.findByDescription(fields[1]));
				lookupBucketLogic.setLookupGoalHorizonBucket(
						lookupGoalHorizonBucketRepository.findById(Integer.parseInt(fields[2])));
				lookupBucketLogic.setLookupAssetAllocationCategory(
						lookupAssetAllocationCategoryRepository.findByDescription(fields[3].toUpperCase()));
				LookupBucketLogic existingRecordsCheck = lookupBucketLogicRepository
						.findOne(Integer.parseInt(fields[0]));
				if (existingRecordsCheck != null) {
					lookupBucketLogic.setId(existingRecordsCheck.getId());
				}
				lookupBucketLogicRepository.save(lookupBucketLogic);
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
