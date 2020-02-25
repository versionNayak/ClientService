package com.finlabs.finexa.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.util.IOUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.finlabs.finexa.dto.AumMasterBODTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AumMasterBO;
import com.finlabs.finexa.model.AumMasterBOPK;
import com.finlabs.finexa.model.BackOfficeUploadHistory;
import com.finlabs.finexa.model.LookupRTABO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AumMasterBORepository;
import com.finlabs.finexa.repository.BackOfficeUploadHistoryRepository;
import com.finlabs.finexa.repository.LookupRTABORepository;
import com.finlabs.finexa.repository.MasterDatabaseColumnNamesBORepository;
import com.finlabs.finexa.repository.MasterFeedColumnNamesBORepository;
import com.finlabs.finexa.util.FinexaBOColumnConstant;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

@Component

@Scope("prototype")

public class AUMMasterFeedBOThread implements Runnable {

	private static Logger log = LoggerFactory.getLogger(AUMMasterFeedBOThread.class);
	private AumMasterBODTO aumMasterBODTO;
	private UploadResponseDTO uploadResponseDTO;
	private BackOfficeUploadHistory backOfficeUploadHistory;

	private Integer rta;
	private String rejectedRowNumbers = "";
	private Integer rejectedRowNumber = 0;
	private int rows = 1, columnHeaderRow = 0, noOfRejectedRecords = 0;
	private boolean allRowsInserted = false;
	private final int ROW_LENGTH = 255;
	private final Integer PRIMARY_KEY = 8;
	private Map<Integer, LookupRTABO> lookupRTABOMap = new HashMap<Integer, LookupRTABO>();
	DBFReader dbfReader;

	@Autowired
	private Mapper mapper;

	@Autowired
	private AumMasterBORepository aumMasterBORepository;

	@Autowired
	private LookupRTABORepository lookupRTABORepository;

	@Autowired
	private BackOfficeUploadHistoryRepository uploadHistoryRepo;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private MasterDatabaseColumnNamesBORepository masterDatabaseColumnNamesBORepository;

	@Autowired
	private MasterFeedColumnNamesBORepository masterFeedColumnNamesBORepository;

	public void initialize(AumMasterBODTO aumMasterBODTO, UploadResponseDTO uploadResponseDTO,
			BackOfficeUploadHistory backOfficeUploadHistory) {
		this.aumMasterBODTO = aumMasterBODTO;
		this.uploadResponseDTO = uploadResponseDTO;
		this.backOfficeUploadHistory = backOfficeUploadHistory;
	}

	@Override
	public void run() {
		try {

			rta = aumMasterBODTO.getNameRTA();
			rta.toString();
			String name = aumMasterBODTO.getNameSelectFile()[0].getOriginalFilename();
			if (name.contains(FinexaBOColumnConstant.DBF_LOWER_CASE) || name.contains(FinexaBOColumnConstant.DBF_UPPER_CASE)) {
				uploadResponseDTO = readDBFFeed(aumMasterBODTO, uploadResponseDTO);
			} else {
				uploadResponseDTO = readExcelFeed(aumMasterBODTO, uploadResponseDTO);
			}
			if (uploadResponseDTO.isStatus()) {
				
				int reasonLength=rejectedRowNumbers.length()+(FinexaBOColumnConstant.AUM_RECORD_REJECTION_MESSAGE).length();
				backOfficeUploadHistory.setStatus(FinexaBOColumnConstant.STATUS_COMPLETED);
				if (noOfRejectedRecords > 0) {

					if (reasonLength <= ROW_LENGTH) {

						//rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
						backOfficeUploadHistory.setReasonOfRejection(
								FinexaBOColumnConstant.AUM_RECORD_REJECTION_MESSAGE + rejectedRowNumbers);

					} else {
						rejectedRowNumbers = rejectedRowNumbers.substring(0, rejectedRowNumbers.indexOf(','))
						/* + rejectedRowNumber.toString() + "," */;

						backOfficeUploadHistory.setReasonOfRejection(FinexaBOColumnConstant.AUM_RECORD_REJECTION_MESSAGE
								+ rejectedRowNumbers + FinexaBOColumnConstant.GENERAL_MESSAGE);
					}

					backOfficeUploadHistory.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_NOT_APPLICABLE);
				} else {
					backOfficeUploadHistory.setReasonOfRejection(FinexaBOColumnConstant.NOT_REJECTED_MESSAGE);
					backOfficeUploadHistory.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_NOT_APPLICABLE);
				}

			} else {
				backOfficeUploadHistory.setStatus(FinexaBOColumnConstant.STATUS_REJECTED);
				backOfficeUploadHistory.setReasonOfRejection(FinexaBOColumnConstant.FILE_REJECTION_MESSAGE);
				backOfficeUploadHistory.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_NOT_APPLICABLE);
			}
			backOfficeUploadHistory.setRejectedRecords(uploadResponseDTO.getRejectedRecords());
			backOfficeUploadHistory.setEndTime(new java.util.Date());
			backOfficeUploadHistory = uploadHistoryRepo.save(backOfficeUploadHistory);

			if (backOfficeUploadHistory == null) {
				uploadResponseDTO.setMessage(FinexaBOColumnConstant.FAILED_TO_COMPLETE);
				uploadResponseDTO.setStatus(false);
			} else {
				if (uploadResponseDTO.isStatus()) {
					uploadResponseDTO.setMessage(FinexaBOColumnConstant.UPLOAD_SUCCESSFUL);
				} else {
					uploadResponseDTO.setMessage(FinexaBOColumnConstant.UPLOAD_FAILED);
				}
			}
			log.debug("AUM FEED UPLOADING FINISHED!");
		} catch (RuntimeException | InvalidFormatException | IOException | ParseException e) {
			if (allRowsInserted == true) {
				backOfficeUploadHistory.setStatus(FinexaBOColumnConstant.STATUS_COMPLETED);
				backOfficeUploadHistory.setRejectedRecords(uploadResponseDTO.getRejectedRecords());
				backOfficeUploadHistory.setEndTime(new java.util.Date());
				backOfficeUploadHistory.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_NOT_APPLICABLE);
				backOfficeUploadHistory
						.setReasonOfRejection(FinexaBOColumnConstant.AUM_RECORD_REJECTION_MESSAGE + rejectedRowNumbers);
				backOfficeUploadHistory = uploadHistoryRepo.save(backOfficeUploadHistory);
			} else {
				backOfficeUploadHistory.setStatus(FinexaBOColumnConstant.STATUS_REJECTED);
				backOfficeUploadHistory.setRejectedRecords(0);
				backOfficeUploadHistory.setEndTime(new java.util.Date());
				backOfficeUploadHistory.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_NOT_APPLICABLE);
				backOfficeUploadHistory.setReasonOfRejection(FinexaBOColumnConstant.FILE_NOT_PROCESSED_MESSAGE);
				backOfficeUploadHistory = uploadHistoryRepo.save(backOfficeUploadHistory);
			}

			throw new RuntimeException(e);
		}
	}

	/**
	 * Method to read from Excel File using Apache POI
	 *
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws ParseException
	 */
	private UploadResponseDTO readExcelFeed(AumMasterBODTO aumMasterBODTO, UploadResponseDTO uploadResponseDTO)
			throws IOException, InvalidFormatException, ParseException {
		try {
			// null pointer handling
			boolean isUniqueColumnNotFound = false;
			int counter = 0;
			String column = "";
			CellAddress addressOfCell = null;
			String columnAddress = null;
			List<Object[]> databaseColumnIDAndNameList;
			List<Object[]> feedColumnNameList;
			List<String> primaryKeyList = new ArrayList<String>();
			List<String> columnNameList = new ArrayList<String>();
			List<LookupRTABO> lookupRTABOList;
			Map<String, String> excelNameColNameMap = new HashMap<String, String>();
			Map<String, String> excelColNameAndAddressMap = new HashMap<String, String>();
			Map<String, String> distinctColumnNameAndIDMap = new HashMap<String, String>();
			Map<String, String> databaseColumnIDAndExcelFieldNameMap = new HashMap<String, String>();

			InputStream initialStream = aumMasterBODTO.getNameSelectFile()[0].getInputStream();
			String uuid = UUID.randomUUID().toString();
			//System.out.println(uuid);
			File directory = new File(System.getProperty("java.io.tmpdir"));
			File targetFile = File.createTempFile("WBR2_" + uuid, ".tmp", directory);
			java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			IOUtils.closeQuietly(initialStream);

			Workbook workbook = WorkbookFactory.create(targetFile);

			// Getting the Sheet at index zero
			Sheet sheet = workbook.getSheetAt(0);

			// Fetching the the database column IDs and names list based on the type of feed
			// file
			databaseColumnIDAndNameList = masterDatabaseColumnNamesBORepository.getIDAndDatabaseColumnNames(FinexaBOColumnConstant.FEED_TYPE_AUMM);

			// Fetching the the database column IDs and feed column names list based on the
			// type of feed file
			feedColumnNameList = masterFeedColumnNamesBORepository.getIDAndFeedColumnNames(FinexaBOColumnConstant.FEED_TYPE_AUMM);

			// Fetching all types of RTAs
			lookupRTABOList = lookupRTABORepository.findAll();

			// Storing RTA ID as a key and corresponding RTA model as the value
			for (LookupRTABO lookupRTABO : lookupRTABOList) {
				try {
					if (lookupRTABO.getName().equals(FinexaBOColumnConstant.RTA_CAMS)) {
						lookupRTABOMap.put(Integer.parseInt(FinexaBOColumnConstant.RTA_FILE_TYPE_CAMS), lookupRTABO);
					} else if (lookupRTABO.getName().equals(FinexaBOColumnConstant.RTA_KARVY)) {
						lookupRTABOMap.put(Integer.parseInt(FinexaBOColumnConstant.RTA_FILE_TYPE_KARVY), lookupRTABO);
					} else if (lookupRTABO.getName().equals(FinexaBOColumnConstant.RTA_FRANKLIN)) {
						lookupRTABOMap.put(Integer.parseInt(FinexaBOColumnConstant.RTA_FILE_TYPE_FRANKLIN),
								lookupRTABO);
					} else if (lookupRTABO.getName().equals(FinexaBOColumnConstant.RTA_SUNDARAM)) {
						lookupRTABOMap.put(Integer.parseInt(FinexaBOColumnConstant.RTA_FILE_TYPE_SUNDARAM),
								lookupRTABO);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (Object[] databaseColumnIDAndName : databaseColumnIDAndNameList) {
				try {

					// Storing database column ID as key and database column name as value
					distinctColumnNameAndIDMap.put(databaseColumnIDAndName[0].toString(),
							databaseColumnIDAndName[1].toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (Object[] feedColumnName : feedColumnNameList) {
				try {
					if (feedColumnName[0].toString().equals(PRIMARY_KEY.toString())) {

						// Generating a list of all possible feed column names for an unique field
						primaryKeyList.add(feedColumnName[1].toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Map<String, String> feed = new HashMap<>();

			// Storing the last row number of feed file
			rows = sheet.getLastRowNum();
			//System.out.println("Rows" + rows);

			// Iterating over Rows and Columns using Iterator
			Iterator<Row> rowIterator = sheet.rowIterator();

			// Getting the column header row number
			columnHeaderRow = getColumnHeaderRowNum(sheet, primaryKeyList);

			Iterator<Row> rowIterator1 = sheet.rowIterator();

			while (rowIterator1.hasNext()) {
				try {
					Row row1 = rowIterator1.next();

					// To reach the actual column header
					if (row1.getRowNum() != columnHeaderRow)
						continue;

					// To iterate over the columns of the current row
					Iterator<Cell> cellIterator1 = row1.cellIterator();

					while (cellIterator1.hasNext()) {
						try {
							Cell cell1 = cellIterator1.next();
							column = cell1.toString();
							addressOfCell = cell1.getAddress();

							if (("" + addressOfCell).length() < 3) {
								columnAddress = ("" + addressOfCell).substring(0, 1);
							} else
								columnAddress = ("" + addressOfCell).substring(0, 2);

							// Storing excel column name as a key and column address as the value
							excelNameColNameMap.put(column.toLowerCase().trim(), columnAddress);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (Map.Entry<String, String> distinctColumnNameAndID : distinctColumnNameAndIDMap.entrySet()) {
				try {
					String dbColumnID = distinctColumnNameAndID.getKey();
					String dbColumnName = distinctColumnNameAndID.getValue();
					//System.out.println(dbColumnID + dbColumnName);
					columnNameList = new ArrayList<String>();
					// Generating a list of all possible feed column name for each column of
					// database
					for (Object[] feedColumnName : feedColumnNameList) {
						try {
							if (feedColumnName[0].toString().equals(dbColumnID)) {
								columnNameList.add(feedColumnName[1].toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					for (String columnName : columnNameList) {
						try {
							/*
							 * If cell value of a column matches any of the field present in columnNameList,
							 * * then it breaks the loop and gets stored
							 */
							if (excelNameColNameMap.containsKey(columnName.toLowerCase().trim())) {
								column = columnName;

								// Storing the database column ID as a key and corresponding feed column name as
								// the value.
								databaseColumnIDAndExcelFieldNameMap.put(dbColumnID, column.toLowerCase().trim());

								// Storing the feed column name as a key and it's address as value.
								excelColNameAndAddressMap.put(column.toLowerCase().trim(), excelNameColNameMap.get(column.toLowerCase().trim()));
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			//System.out.println(excelColNameAndAddressMap);

			for (String dbCOlumnID : FinexaBOColumnConstant.AUM_DB_COLUMN_ID_LIST) {
				try {
					if (databaseColumnIDAndExcelFieldNameMap.containsKey(dbCOlumnID))
						continue;
					else {
						isUniqueColumnNotFound = true;
						log.debug("CORRESPONDING FEED COLUMN FOR THE  DATABASE COLUMN - " + dbCOlumnID
								+ ", IS NOT FOUND!!");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			if (isUniqueColumnNotFound == true) {
				uploadResponseDTO.setPrimaryKeyNotFound(true);
				uploadResponseDTO.setStatus(false);
				return uploadResponseDTO;
			}

			List<AumMasterBO> aumMasterBOList = new ArrayList<AumMasterBO>();

			// Empty the list each time after inserting 20 records
			int batchCount = 0;

			// try-catch
			AdvisorUser advisorUser = advisorUserRepository.findOne(aumMasterBODTO.getAdvisorId());
			// System.out.println("Inside readExcel method");

			while (rowIterator.hasNext()) {
				try {
					boolean isRowNotBlank = false;
					// This row variable is used to iterate through each row of excel sheet
					Row row = rowIterator.next();
					rejectedRowNumber = row.getRowNum();

					// To reach the column header row
					if (row.getRowNum() < columnHeaderRow + 1) {
						continue;
					}

					// Row number is greater than column header row number
					for (Map.Entry<String, String> distinctColumnNameAndID : distinctColumnNameAndIDMap.entrySet()) {
						try {
							String dbColID = distinctColumnNameAndID.getKey();
							if (excelColNameAndAddressMap
									.get(databaseColumnIDAndExcelFieldNameMap.get(dbColID)) != null) {
								columnAddress = excelColNameAndAddressMap
										.get(databaseColumnIDAndExcelFieldNameMap.get(dbColID));
								String dataOfCell = "";
								if (row.getCell(org.apache.poi.ss.util.CellReference
										.convertColStringToIndex(columnAddress)) != null) {
									Cell cell = row.getCell(org.apache.poi.ss.util.CellReference
											.convertColStringToIndex(columnAddress));
									dataOfCell = cell.toString();
								}
								feed.put(dbColID, dataOfCell);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					for (Map.Entry<String,String> entry : feed.entrySet()) { 
			            
			            if(entry.getValue() != null && !entry.getValue().trim().isEmpty() && entry.getValue() != "") {
			            	isRowNotBlank = true;
			            	break;
			            }	
					} 
					if (isRowNotBlank == false)
						continue;
					//System.out.println(feed);
					if (batchCount == 100) {
						counter++;
						log.debug("LIST size() when COUNTER is: " + counter + ", and batchCount is: "
								+ aumMasterBOList.size());
						uploadResponseDTO = saveModelData(aumMasterBOList, uploadResponseDTO);
						aumMasterBOList = new ArrayList<AumMasterBO>();
						batchCount = 0;

						aumMasterBOList = populateModelList(feed, aumMasterBODTO, aumMasterBOList, advisorUser);

						batchCount++;

					} else {

						aumMasterBOList = populateModelList(feed, aumMasterBODTO, aumMasterBOList, advisorUser);

						batchCount++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			if (batchCount > 0) {
				counter++;
				log.debug("LIST size() when LAST COUNTER is: " + counter + ", and batchCount is: "
						+ aumMasterBOList.size());
				uploadResponseDTO = saveModelData(aumMasterBOList, uploadResponseDTO);
				allRowsInserted = true;
			}
			targetFile.delete();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		finally {
			uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		}
		return uploadResponseDTO;
	}

	private List<AumMasterBO> populateModelList(Map<String, String> feedData, AumMasterBODTO aumMasterBODTO,
			List<AumMasterBO> aumMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordInvalid = false;

			if (feedData.get(FinexaBOColumnConstant.AUM_REPORT_DATE) == null
					|| feedData.get(FinexaBOColumnConstant.AUM_REPORT_DATE).isEmpty()
					|| feedData.get(FinexaBOColumnConstant.AUM_REPORT_DATE) == "") {
				isRecordInvalid = true;
			} else if (feedData.get(FinexaBOColumnConstant.AUM_FOLIO_NUMBER).toString().trim() == null
					|| feedData.get(FinexaBOColumnConstant.AUM_FOLIO_NUMBER).toString().trim().isEmpty()
					|| feedData.get(FinexaBOColumnConstant.AUM_FOLIO_NUMBER).toString().trim() == "") {
				isRecordInvalid = true;
			} else if (feedData.get(FinexaBOColumnConstant.AUM_SCHEME_RTA_CODE).toString().trim() == null
					|| feedData.get(FinexaBOColumnConstant.AUM_SCHEME_RTA_CODE).toString().trim().isEmpty()
					|| feedData.get(FinexaBOColumnConstant.AUM_SCHEME_RTA_CODE).toString().trim() == "") {

				isRecordInvalid = true;
			}

			if (isRecordInvalid == true) {
				Integer rowNum = rejectedRowNumber + 1;
				rejectedRowNumbers = rejectedRowNumbers + rowNum.toString() + ",";
				noOfRejectedRecords++;

				return aumMasterBOList;
			}

			AumMasterBO aumMasterBO = mapper.map(aumMasterBODTO, AumMasterBO.class);
			AumMasterBOPK aumMasterBOPK = new AumMasterBOPK();
			aumMasterBOPK.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.AUM_FOLIO_NUMBER)));
			aumMasterBOPK.setSchemertacode(feedData.get(FinexaBOColumnConstant.AUM_SCHEME_RTA_CODE));
			aumMasterBOPK.setReportDate(formatUtilDateToSQLDate(dateFormatter(feedData.get(FinexaBOColumnConstant.AUM_REPORT_DATE))));
			aumMasterBO.setUnitBalance(formatAmount(feedData.get(FinexaBOColumnConstant.AUM_UNIT_BALANCE)));
			aumMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.AUM_SUB_BROKER_CODE));
			aumMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.AUM_SCHEME_NAME));
			aumMasterBO.setPinCode(formatNumbers(feedData.get(FinexaBOColumnConstant.AUM_PINCODE)));
			aumMasterBO.setNav(feedData.get(FinexaBOColumnConstant.AUM_NAV));
			aumMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.AUM_INVESTOR_NAME));
			aumMasterBO.setDistributorarncode(feedData.get(FinexaBOColumnConstant.AUM_DISTRIBUTOR_ARN_CODE));
			aumMasterBO.setCurrentValue(formatAmount(feedData.get(FinexaBOColumnConstant.AUM_CURRENT_VALUE)));

			if(aumMasterBODTO.getNameRTA() == FinexaBOColumnConstant.RTA_TYPE_FRANKLIN)
				aumMasterBO.setAmcCode(FinexaBOColumnConstant.AMC_FRANKLIN);
			else if(aumMasterBODTO.getNameRTA() == FinexaBOColumnConstant.RTA_TYPE_SUNDARAM)
				aumMasterBO.setAmcCode(FinexaBOColumnConstant.AMC_SUNDARAM);
			else
				aumMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.AUM_AMC_CODE));

			aumMasterBO.setLookupRtabo(lookupRTABOMap.get(aumMasterBODTO.getNameRTA()));
			aumMasterBO.setAdvisorUser(advisorUser);
			aumMasterBO.setId(aumMasterBOPK);

			aumMasterBOList.add(aumMasterBO);
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return aumMasterBOList;

	}

	private UploadResponseDTO readDBFFeed(AumMasterBODTO aumMasterBODTO, UploadResponseDTO uploadResponseDTO)
			throws IOException, InvalidFormatException, ParseException {
		try {
			InputStream initialStream = aumMasterBODTO.getNameSelectFile()[0].getInputStream();
			InputStream initialStream2 = aumMasterBODTO.getNameSelectFile()[0].getInputStream();
			dbfReader = new DBFReader(initialStream);
			boolean headerFound = false, firstRow, isUniqueColumnNotFound = false;
			int numberOfFields;
			String column;
			Object[] rowObjects1, rowObjects2;
			int batchCount = 0, counter = 0, headerRowCount = 0, row, numberOfRecords = 0, numberOfRecordRunning = 0;
			List<AumMasterBO> aumMasterBOList = new ArrayList<AumMasterBO>();
			Map<Integer, String> dataMap = new HashMap<>();
			Map<String, String> feedMap = new HashMap<>();

			List<Object[]> databaseColumnIDAndNameList;
			List<Object[]> feedColumnNameList;
			List<String> primaryKeyList = new ArrayList<String>();
			List<String> columnNameList = new ArrayList<String>();
			List<LookupRTABO> lookupRTABOList;
			Map<String, String> databaseColumnNameAndIDMap = new HashMap<String, String>();
			Map<String, String> databaseColumnIDAndExcelFieldIDMap = new HashMap<String, String>();
			Map<String, String> databaseColumnIDAndDataMap = new HashMap<String, String>();

			// Fetching the the database column IDs and names list based on the type of feed
			// file
			databaseColumnIDAndNameList = masterDatabaseColumnNamesBORepository.getIDAndDatabaseColumnNames(FinexaBOColumnConstant.FEED_TYPE_AUMM);

			// Fetching the the database column IDs and feed column names list based on the
			// type of feed file
			feedColumnNameList = masterFeedColumnNamesBORepository.getIDAndFeedColumnNames(FinexaBOColumnConstant.FEED_TYPE_AUMM);

			// Fetching all types of RTAs
			lookupRTABOList = lookupRTABORepository.findAll();

			// Storing RTA ID as a key and corresponding RTA model as the value
			for (LookupRTABO lookupRTABO : lookupRTABOList) {
				try {
					if (lookupRTABO.getName().equals(FinexaBOColumnConstant.RTA_CAMS)) {
						lookupRTABOMap.put(Integer.parseInt(FinexaBOColumnConstant.RTA_FILE_TYPE_CAMS), lookupRTABO);
					} else if (lookupRTABO.getName().equals(FinexaBOColumnConstant.RTA_KARVY)) {
						lookupRTABOMap.put(Integer.parseInt(FinexaBOColumnConstant.RTA_FILE_TYPE_KARVY), lookupRTABO);
					} else if (lookupRTABO.getName().equals(FinexaBOColumnConstant.RTA_FRANKLIN)) {
						lookupRTABOMap.put(Integer.parseInt(FinexaBOColumnConstant.RTA_FILE_TYPE_FRANKLIN),
								lookupRTABO);
					} else if (lookupRTABO.getName().equals(FinexaBOColumnConstant.RTA_SUNDARAM)) {
						lookupRTABOMap.put(Integer.parseInt(FinexaBOColumnConstant.RTA_FILE_TYPE_SUNDARAM),
								lookupRTABO);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (Object[] databaseColumnIDAndName : databaseColumnIDAndNameList) {
				try {
					// Storing database column ID as key and database column name as value
					databaseColumnNameAndIDMap.put(databaseColumnIDAndName[0].toString(),
							databaseColumnIDAndName[1].toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (Object[] feedColumnName : feedColumnNameList) {
				try {
					if (feedColumnName[0].toString().equals(PRIMARY_KEY.toString())) {

						// Generating a list of all possible feed column names for an unique field
						primaryKeyList.add(feedColumnName[1].toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Number of columns in the DBF file
			numberOfFields = dbfReader.getFieldCount();
			numberOfRecords = dbfReader.getRecordCount();
/*
			rowObjects1 = dbfReader.nextRecord();
			firstRow = false;
			while (dbfReader.nextRecord() != null) {
				try {
					headerRowCount++;
					if (firstRow == false) {
						for (int i = 0; i < numberOfFields; i++) {
							DBFField field = dbfReader.getField(i);
							String fileColName = field.getName();
							for (String primaryKey : primaryKeyList) {
								if (fileColName.equalsIgnoreCase(primaryKey)) {
									headerFound = true;
									break;
								}
							}
							if (headerFound == true)
								break;
						}

						firstRow = true;
						if (headerFound == true)
							break;
					} else {

						for (int i = 0; i < numberOfFields; i++) {
							DBFField field = dbfReader.getField(i);
							String fileColName = field.getName();
							for (String primaryKey : primaryKeyList) {
								if (fileColName.equalsIgnoreCase(primaryKey)) {
									headerFound = true;
									break;
								}
							}
							if (headerFound == true)
								break;
						}

						rowObjects1 = dbfReader.nextRecord();
					}
					if (headerFound == true)
						break;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
*/
			int numberOfRecordRunning1 = 0;
			//rowObjects1 = dbfReader.nextRecord();
			firstRow = false;
			while (dbfReader.nextRecord() != null) {				
				try {					
					headerRowCount++;
					if (firstRow == false) {
						for (int i = 0; i < numberOfFields; i++) {
							DBFField field = dbfReader.getField(i);
							String fileColName = field.getName();
							for (String primaryKey : primaryKeyList) {
								if (fileColName.equalsIgnoreCase(primaryKey)) {
									headerFound = true;
									break;
								}
							}
							if (headerFound == true)
								break;
						}

						firstRow = true;
						if (headerFound == true)
							break;
					} else {
						rowObjects1 = dbfReader.nextRecord();
						for (int i = 0; i < numberOfFields; i++) {
							//DBFField field1 = dbfReader.getField(i);
							String fileColName1 = String.valueOf(rowObjects1[i]);
							System.out.print(fileColName1+",");
						}
						System.out.println();
						for (int i = 0; i < numberOfFields; i++) {
							String fileColName = String.valueOf(rowObjects1[i]);
							for (String primaryKey : primaryKeyList) {
								if (fileColName.equalsIgnoreCase(primaryKey)) {
									headerFound = true;
									break;
								}
							}
							if (headerFound == true)
								break;
						}

						numberOfRecordRunning1++;
						
					}
					if (headerFound == true)
						break;
					if (numberOfRecordRunning1 == numberOfRecords)
						break;
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			for (Integer i = 0; i < numberOfFields; i++) {
				DBFField field = dbfReader.getField(i);
				String fileColname = field.getName();
				feedMap.put(fileColname.toLowerCase().trim(), i.toString());
			}

			for (Map.Entry<String, String> databaseColumnNameAndID : databaseColumnNameAndIDMap.entrySet()) {
				try {
					String dbColumnID = databaseColumnNameAndID.getKey();
					String dbColumnName = databaseColumnNameAndID.getValue();
					//System.out.println(dbColumnID + dbColumnName);
					columnNameList = new ArrayList<String>();
					// Generating a list of all possible feed column name for each column of
					// database
					for (Object[] feedColumnName : feedColumnNameList) {
						try {
							if (feedColumnName[0].toString().equals(dbColumnID)) {
								columnNameList.add(feedColumnName[1].toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					for (String columnName : columnNameList) {
						try {
							/*
							 * If cell value of a column matches any of the field present in columnNameList,
							 * * then it breaks the loop and gets stored
							 */
							if (feedMap.containsKey(columnName.toLowerCase().trim())) {
								column = columnName;

								// Storing the database column ID as a key and corresponding feed column name as
								// the value.
								databaseColumnIDAndExcelFieldIDMap.put(dbColumnID, feedMap.get(column.toLowerCase().trim()));

								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			for (String dbCOlumnID : FinexaBOColumnConstant.AUM_DB_COLUMN_ID_LIST) {
				try {
					if (databaseColumnIDAndExcelFieldIDMap.containsKey(dbCOlumnID))
						continue;
					else {
						isUniqueColumnNotFound = true;
						log.debug("CORRESPONDING FEED COLUMN FOR THE  DATABASE COLUMN - " + dbCOlumnID
								+ ", IS NOT FOUND!!");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			if (isUniqueColumnNotFound == true) {
				uploadResponseDTO.setPrimaryKeyNotFound(true);
				uploadResponseDTO.setStatus(false);
				return uploadResponseDTO;
			}

			AdvisorUser advisorUser = advisorUserRepository.findOne(aumMasterBODTO.getAdvisorId());

			dbfReader = new DBFReader(initialStream2);
			// Starts reading after the header row
			row = 0;
			rowObjects2 = dbfReader.nextRecord();
			rejectedRowNumber = headerRowCount;
			while (rowObjects2 != null) {
				try {
					if (++row <= headerRowCount)
						continue;
					rejectedRowNumber++;
					numberOfRecordRunning++;
					for (int columnIndex = 0; columnIndex < numberOfFields; columnIndex++) {
						try {
							Integer fieldIndex = columnIndex;
							dataMap.put(fieldIndex, String.valueOf(rowObjects2[columnIndex]).trim());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					for (Map.Entry<String, String> databaseColumnIDAndExcelFieldID : databaseColumnIDAndExcelFieldIDMap
							.entrySet()) {
						try {
							String databaseColumnID = databaseColumnIDAndExcelFieldID.getKey();
							String feedColumnID = databaseColumnIDAndExcelFieldID.getValue();
							databaseColumnIDAndDataMap.put(databaseColumnID,
									dataMap.get(Integer.parseInt(feedColumnID)));
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					//System.out.println(databaseColumnIDAndDataMap);
					if (batchCount == 100) {
						counter++;
						log.debug("aumMasterBOList size() when COUNTER is:" + counter + " batchCount is: "
								+ aumMasterBOList.size());
						uploadResponseDTO = saveModelData(aumMasterBOList, uploadResponseDTO);
						aumMasterBOList = new ArrayList<AumMasterBO>();
						batchCount = 0;

						//System.out.println("*********************************");

						aumMasterBOList = populateModelList(databaseColumnIDAndDataMap, aumMasterBODTO,
								aumMasterBOList, advisorUser);

						batchCount++;

					} else {

						aumMasterBOList = populateModelList(databaseColumnIDAndDataMap, aumMasterBODTO,
								aumMasterBOList, advisorUser);

						batchCount++;
					}
					if (numberOfRecordRunning == numberOfRecords)
						break;
					rowObjects2 = dbfReader.nextRecord();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			if (batchCount > 0) {
				log.debug("aumMasterBOList size() before LAST: " + aumMasterBOList.size());
				uploadResponseDTO = saveModelData(aumMasterBOList, uploadResponseDTO);
			}
			// System.out.println(dataMap);

			uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uploadResponseDTO;

	}
/*
	private List<AumMasterBO> populateDBFModelList(Map<String, String> feedData, AumMasterBODTO aumMasterBODTO,
			List<AumMasterBO> aumMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordInvalid = false;

			if (feedData.get(FinexaBOColumnConstant.AUM_REPORT_DATE) == null
					|| feedData.get(FinexaBOColumnConstant.AUM_REPORT_DATE).isEmpty()
					|| feedData.get(FinexaBOColumnConstant.AUM_REPORT_DATE) == "") {
				isRecordInvalid = true;
			} else if (feedData.get(FinexaBOColumnConstant.AUM_FOLIO_NUMBER).toString().trim() == null
					|| feedData.get(FinexaBOColumnConstant.AUM_FOLIO_NUMBER).toString().trim().isEmpty()
					|| feedData.get(FinexaBOColumnConstant.AUM_FOLIO_NUMBER).toString().trim() == "") {
				isRecordInvalid = true;
			} else if (feedData.get(FinexaBOColumnConstant.AUM_SCHEME_RTA_CODE).toString().trim() == null
					|| feedData.get(FinexaBOColumnConstant.AUM_SCHEME_RTA_CODE).toString().trim().isEmpty()
					|| feedData.get(FinexaBOColumnConstant.AUM_SCHEME_RTA_CODE).toString().trim() == "") {

				isRecordInvalid = true;
			}

			if (isRecordInvalid == true) {
				Integer rowNum = rejectedRowNumber + 1;
				rejectedRowNumbers = rejectedRowNumbers + rowNum.toString() + ",";
				noOfRejectedRecords++;

				return aumMasterBOList;
			}

			AumMasterBO aumMasterBO = mapper.map(aumMasterBODTO, AumMasterBO.class);
			AumMasterBOPK aumMasterBOPK = new AumMasterBOPK();
			aumMasterBOPK.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.AUM_FOLIO_NUMBER)));
			aumMasterBOPK.setSchemertacode(feedData.get(FinexaBOColumnConstant.AUM_SCHEME_RTA_CODE));
			aumMasterBOPK.setReportDate(dateFormatter(feedData.get(FinexaBOColumnConstant.AUM_REPORT_DATE)));
			aumMasterBO.setUnitBalance(formatAmount(feedData.get(FinexaBOColumnConstant.AUM_UNIT_BALANCE)));
			aumMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.AUM_SUB_BROKER_CODE));
			aumMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.AUM_SCHEME_NAME));
			aumMasterBO.setPinCode(formatNumbers(feedData.get(FinexaBOColumnConstant.AUM_PINCODE)));
			aumMasterBO.setNav(feedData.get(FinexaBOColumnConstant.AUM_NAV));
			aumMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.AUM_INVESTOR_NAME));
			aumMasterBO.setDistributorarncode(feedData.get(FinexaBOColumnConstant.AUM_DISTRIBUTOR_ARN_CODE));
			aumMasterBO.setCurrentValue(formatAmount(feedData.get(FinexaBOColumnConstant.AUM_CURRENT_VALUE)));
			
			if(aumMasterBODTO.getNameRTA() == FinexaBOColumnConstant.RTA_TYPE_FRANKLIN)
				aumMasterBO.setAmcCode(FinexaBOColumnConstant.AMC_FRANKLIN);
			else if(aumMasterBODTO.getNameRTA() == FinexaBOColumnConstant.RTA_TYPE_SUNDARAM)
				aumMasterBO.setAmcCode(FinexaBOColumnConstant.AMC_SUNDARAM);
			else
				aumMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.AUM_AMC_CODE));

			aumMasterBO.setLookupRtabo(lookupRTABOMap.get(aumMasterBODTO.getNameRTA()));
			aumMasterBO.setAdvisorUser(advisorUser);
			aumMasterBO.setId(aumMasterBOPK);

			aumMasterBOList.add(aumMasterBO);
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return aumMasterBOList;

	}
*/
	private synchronized UploadResponseDTO saveModelData(List<AumMasterBO> aumMasterBOList,
			UploadResponseDTO uploadResponseDTO) {
		// TODO Auto-generated method stub
		//System.out.println("Uploading AUM");
		try {
			if (aumMasterBOList != null && aumMasterBOList.size() > 0) {
				aumMasterBORepository.save(aumMasterBOList);
				uploadResponseDTO.setStatus(true);
			}
		} catch (RuntimeException e) {
			uploadResponseDTO.setStatus(false);
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return uploadResponseDTO;

	}

	private Date utilToSQLDate(String date) {
		try {
			if (!(date == null) && !date.trim().isEmpty() && !date.trim().contains("null") && date.trim() != "") {
				SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
				java.util.Date uDate = new java.util.Date();
				try {
					uDate = formatter.parse(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// System.out.println("UDATE " + uDate);
				java.sql.Date sDate = new java.sql.Date(uDate.getTime());
				return sDate;
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private int getColumnHeaderRowNum(Sheet sheet, List<String> primaryKeyList) {
		try {
			boolean found = false;
			columnHeaderRow = 0;

			// Create a DataFormatter to format and get each cell's value as String
			DataFormatter dataFormatter = new DataFormatter();

			Iterator<Row> rowIterator1 = sheet.rowIterator();

			while (rowIterator1.hasNext()) {
				Row row1 = rowIterator1.next();

				// To iterate over the columns of the current row
				Iterator<Cell> cellIterator1 = row1.cellIterator();

				while (cellIterator1.hasNext()) {
					Cell cell1 = cellIterator1.next();
					if (cell1.toString() != null && !cell1.toString().isEmpty()) {
						try {
							String cellValue1 = dataFormatter.formatCellValue(cell1);
							for (String primaryKey : primaryKeyList) {
								if (cellValue1.equalsIgnoreCase(primaryKey)) {
									columnHeaderRow = row1.getRowNum();
									found = true;
									break;
								}
							}
							if (found == true)
								break;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if (found == true)
					break;

			}
			// break;

			// }
			return columnHeaderRow;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}

	private String formatNumbers(String number) {
		try {
			boolean flag = false;
			if (number != null && !number.isEmpty() && !number.contains("null") && number != "") {
				if (!number.contains("/") && !number.matches(".*[a-zA-Z]+.*") && !number.contains("-")) {

					if (number.contains(".")) {
						Double doubleData = Double.parseDouble(number);

						BigDecimal bigDecimaldata = new BigDecimal(doubleData.toString());
						
						Long longData = bigDecimaldata.longValueExact();

						number = longData.toString();

					}
					return number;
				} else {
					if (number.matches(".*[.Ee].*") || number.matches(".*[.Ee-].*")) {
						for (int i = 0; i < number.length(); i++) {
							char c = number.charAt(i);
							if ((c >= 'A' && c <= 'D') || (c >= 'F' && c <= 'Z') || (c >= 'a' && c <= 'd')
									|| (c >= 'f' && c <= 'z')) {
								flag = true;
								break;

							}
						}
						if (flag == false) {
							Double doubleData = Double.parseDouble(number);

							BigDecimal bigDecimaldata = new BigDecimal(BigDecimal.valueOf(doubleData).toPlainString());

							number = bigDecimaldata.toString();

						}
						return number;

					} else
						return number;
				}

			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String formatAmount(String number) {
		try {
			if (number != null && !number.isEmpty() && !number.contains("null") && number != "") {

				if (number.matches(".*[.Ee].*") || number.matches(".*[.Ee-].*")) {
					Double doubleData = Double.parseDouble(number);

					BigDecimal bigDecimaldata = new BigDecimal(BigDecimal.valueOf(doubleData).toPlainString());

					// Long longData = bigDecimaldata.longValueExact();

					number = bigDecimaldata.toString();

					return number;

				} else {

					return number;
				}
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private java.util.Date dateFormatter(String givenDate) {
		java.util.Date formattedDate = new java.util.Date();
		try {
			if (givenDate != null && givenDate.trim() != "" && !givenDate.trim().toLowerCase().contains("null")
					&& !givenDate.isEmpty()) {
				if (givenDate.toLowerCase().matches(".*[a-z].*") && !givenDate.toLowerCase().matches(".*[:].*")) {
					try {
						formattedDate = formatDate(formatStringDate(givenDate));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (!givenDate.toLowerCase().matches(".*[a-z].*")) {
					try {
						formattedDate = formatDate(givenDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (givenDate.toLowerCase().matches(".*[a-z].*") && givenDate.toLowerCase().matches(".*[:].*")) {
					formattedDate = utilToSQLDate(givenDate);
				}
			} else 
				return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formattedDate;
	}

	/**
	 * Method to convert a date in proper format For Back Office
	 * 
	 * @param rawDate
	 * @return
	 */
	private String formatStringDate(String rawDate) {
		try {
			String formattedDate, monthInWords, day, month = null, year;
			// System.out.println("Inside formatDate method");
			if (rawDate != null && !rawDate.isEmpty() && !rawDate.contains("null") && rawDate.trim() != "") {
				// System.out.println("Raw date: "+rawDate);
				String[] splited = rawDate.split("-");
				day = splited[0];
				monthInWords = splited[1];
				year = splited[2];
				// day = rawDate.substring(0, 2);
				// monthInWords = rawDate.substring(3, 6);
				switch (monthInWords.toLowerCase()) {
				case "jan":
					month = "01";
					break;
				case "feb":
					month = "02";
					break;
				case "mar":
					month = "03";
					break;
				case "apr":
					month = "04";
					break;
				case "may":
					month = "05";
					break;
				case "jun":
					month = "06";
					break;
				case "jul":
					month = "07";
					break;
				case "aug":
					month = "08";
					break;
				case "sep":
					month = "09";
					break;
				case "oct":
					month = "10";
					break;
				case "nov":
					month = "11";
					break;
				case "dec":
					month = "12";
					break;
				}

				formattedDate = day + "/" + month + "/" + year;
				// formattedDate = year + "-" + month + "-" + day;
				// System.out.println("Inside format method:"+formattedDate);
				return formattedDate;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private java.util.Date formatDate(String feedDate) throws ParseException {
		try {
			java.util.Date date = null;
			if ((feedDate != null && !feedDate.isEmpty() && !feedDate.contains("null") && feedDate.trim() != "")) {
				if ((!feedDate.contains("/") && !feedDate.contains("-"))) {
					DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
					Date date1 = (Date) formatter.parse(feedDate);

					Calendar cal = Calendar.getInstance();
					cal.setTime(date1);
					String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/"
							+ cal.get(Calendar.YEAR);
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
					date = sdf1.parse(formatedDate);
					return date;
				} else if(feedDate.contains("-")) {
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
					date = sdf1.parse(feedDate);

					return date;
				}else if(feedDate.contains("/")){
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
					date = sdf1.parse(feedDate);

					return date;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private java.sql.Date formatUtilDateToSQLDate(java.util.Date date) {
		java.sql.Date formattedDate = null;
		try {
		if (date != null) {			
				formattedDate = new java.sql.Date(date.getTime());				
			} else
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formattedDate;
	}
}
