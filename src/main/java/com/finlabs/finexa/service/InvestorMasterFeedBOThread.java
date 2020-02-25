package com.finlabs.finexa.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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

import com.finlabs.finexa.dto.InvestorMasterBODTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.BackOfficeUploadHistory;
import com.finlabs.finexa.model.InvestorMasterBO;
import com.finlabs.finexa.model.InvestorMasterBOPK;
import com.finlabs.finexa.model.LookupRTABO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.BackOfficeUploadHistoryRepository;
import com.finlabs.finexa.repository.InvestMasterBORepository;
import com.finlabs.finexa.repository.LookupRTABORepository;
import com.finlabs.finexa.repository.MasterDatabaseColumnNamesBORepository;
import com.finlabs.finexa.repository.MasterFeedColumnNamesBORepository;
import com.finlabs.finexa.util.FinexaBOColumnConstant;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

@Component

@Scope("prototype")

public class InvestorMasterFeedBOThread implements Runnable {

	private static Logger log = LoggerFactory.getLogger(InvestorMasterFeedBOThread.class);
	private InvestorMasterBODTO investorMasterBODTO;
	private UploadResponseDTO uploadResponseDTO;
	private BackOfficeUploadHistory backOfficeUploadHistory;

	private Integer rta;
	private String rejectedRowNumbers = "", rtaId = "";
	private Integer rejectedRowNumber = 0;
	private int rows = 1, columnHeaderRow = 0, noOfRejectedRecords = 0;
	private boolean allRowsInserted = false;
	private final int ROW_LENGTH = 255;
	private final Integer PRIMARY_KEY = 13;
	private final Integer ADULT_AGE = 18;
	private String autoCreationRespose;
	private Map<Integer, LookupRTABO> lookupRTABOMap = new HashMap<Integer, LookupRTABO>();
	DBFReader dbfReader;
	 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	 java.util.Date date=new java.util.Date();
	private final String CREATEDON= formatter.format(date);
	

	@Autowired
	private Mapper mapper;

	@Autowired
	private InvestMasterBORepository investMasterBORepository;

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

	@Autowired
	private ClientMasterDataEntryService clientMasterDataEntryService;

	public void initialize(InvestorMasterBODTO investorMasterBODTO, UploadResponseDTO uploadResponseDTO,
			BackOfficeUploadHistory backOfficeUploadHistory) {
		this.investorMasterBODTO = investorMasterBODTO;
		this.uploadResponseDTO = uploadResponseDTO;
		this.backOfficeUploadHistory = backOfficeUploadHistory;
	}

	@Override
	public void run() {
		
		// TODO Auto-generated method stub

		try {
			// System.out.println("Started Investor Feed");
			// log.debug("InvestorMasterBOServiceImpl >> uploadInvestorCAMSFeed");
			rta = investorMasterBODTO.getNameRTA();
			rtaId = rta.toString();
			String name = investorMasterBODTO.getNameSelectFile()[0].getOriginalFilename();
			log.debug("Investor Timestamp: "+CREATEDON);
			if (name.contains(FinexaBOColumnConstant.DBF_LOWER_CASE)
					|| name.contains(FinexaBOColumnConstant.DBF_UPPER_CASE)) {
				uploadResponseDTO = readDBFFeed(investorMasterBODTO, uploadResponseDTO);
				// System.out.println("EXCEL UPLOADED");
			} else {
				uploadResponseDTO = readExcelFeed(investorMasterBODTO, uploadResponseDTO);
			}

			if (uploadResponseDTO.isStatus()) {
				int reasonLength=rejectedRowNumbers.length()+(FinexaBOColumnConstant.INVESTOR_RECORD_REJECTION_MESSAGE).length();
				backOfficeUploadHistory.setStatus(FinexaBOColumnConstant.STATUS_COMPLETED);
				if (noOfRejectedRecords > 0) {

					if (reasonLength <= ROW_LENGTH) {

						//rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
						backOfficeUploadHistory.setReasonOfRejection(
								FinexaBOColumnConstant.INVESTOR_RECORD_REJECTION_MESSAGE + rejectedRowNumbers);

					} else {
						rejectedRowNumbers = rejectedRowNumbers.substring(0,
								rejectedRowNumbers.indexOf(',')) /* + rejectedRowNumber.toString() + "," */;

						backOfficeUploadHistory
								.setReasonOfRejection(FinexaBOColumnConstant.INVESTOR_RECORD_REJECTION_MESSAGE
										+ rejectedRowNumbers + FinexaBOColumnConstant.GENERAL_MESSAGE);
					}

					backOfficeUploadHistory.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_RUNNING);
				} else {
					backOfficeUploadHistory.setReasonOfRejection(FinexaBOColumnConstant.NOT_REJECTED_MESSAGE);
					backOfficeUploadHistory.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_RUNNING);
				}

			} else {
				backOfficeUploadHistory.setStatus(FinexaBOColumnConstant.STATUS_REJECTED);
				backOfficeUploadHistory.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_NOT_APPLICABLE);
				backOfficeUploadHistory.setReasonOfRejection(FinexaBOColumnConstant.FILE_REJECTION_MESSAGE);
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

			if ((backOfficeUploadHistory.getStatus()).equals(FinexaBOColumnConstant.STATUS_COMPLETED)) {
				try {
					System.out.println("Investor feed upload  "+CREATEDON);
					autoCreationRespose = clientMasterDataEntryService.generateAutoClient(investorMasterBODTO.getAdvisorId(),CREATEDON);
					//autoCreationRespose = "Success";
					if (autoCreationRespose.equals(FinexaBOColumnConstant.SUCCESS)) {

						backOfficeUploadHistory.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_DONE);

					} else if (autoCreationRespose.equals(FinexaBOColumnConstant.FAILURE)) {

						backOfficeUploadHistory.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_NOT_DONE);
						backOfficeUploadHistory.setReasonOfRejection(FinexaBOColumnConstant.STATUS_NOT_APPLICABLE);

					} else {
						backOfficeUploadHistory
								.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_NOT_APPLICABLE);
						backOfficeUploadHistory.setReasonOfRejection(FinexaBOColumnConstant.STATUS_NOT_APPLICABLE);

					}
					backOfficeUploadHistory = uploadHistoryRepo.save(backOfficeUploadHistory);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (RuntimeException | InvalidFormatException | IOException | ParseException e) {
			if (allRowsInserted == true) {
				backOfficeUploadHistory.setStatus(FinexaBOColumnConstant.STATUS_COMPLETED);
				backOfficeUploadHistory.setRejectedRecords(uploadResponseDTO.getRejectedRecords());
				backOfficeUploadHistory.setEndTime(new java.util.Date());
				backOfficeUploadHistory.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_NOT_APPLICABLE);
				backOfficeUploadHistory.setReasonOfRejection(
						FinexaBOColumnConstant.INVESTOR_RECORD_REJECTION_MESSAGE + rejectedRowNumbers);
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

		/*
		 * uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		 * noOfRejectedRecords = 0;
		 */

	}

	/**
	 * Method to read from Excel File using Apache POI
	 *
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws ParseException
	 */
	private UploadResponseDTO readExcelFeed(InvestorMasterBODTO investorMasterBODTO,
			UploadResponseDTO uploadResponseDTO) throws IOException, InvalidFormatException, ParseException {
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

			InputStream initialStream = investorMasterBODTO.getNameSelectFile()[0].getInputStream();
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
			databaseColumnIDAndNameList = masterDatabaseColumnNamesBORepository
					.getIDAndDatabaseColumnNames(FinexaBOColumnConstant.FEED_TYPE_INVM);

			// Fetching the the database column IDs and feed column names list based on the
			// type of feed file
			feedColumnNameList = masterFeedColumnNamesBORepository
					.getIDAndFeedColumnNames(FinexaBOColumnConstant.FEED_TYPE_INVM);

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
								//column = columnName;

								// Storing the database column ID as a key and corresponding feed column name as
								// the value.
								databaseColumnIDAndExcelFieldNameMap.put(dbColumnID, columnName.toLowerCase().trim());

								// Storing the feed column name as a key and it's address as value.
								excelColNameAndAddressMap.put(columnName.toLowerCase().trim(), excelNameColNameMap.get(columnName.toLowerCase().trim()));
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

			for (String dbCOlumnID : FinexaBOColumnConstant.INVESTOR_DB_COLUMN_ID_LIST) {
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

			List<InvestorMasterBO> investorMasterBOList = new ArrayList<InvestorMasterBO>();

			// Empty the list each time after inserting 20 records
			int batchCount = 0;

			// try-catch
			AdvisorUser advisorUser = advisorUserRepository.findOne(investorMasterBODTO.getAdvisorId());
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
								+ investorMasterBOList.size());
						uploadResponseDTO = saveModelData(investorMasterBOList, uploadResponseDTO);
						investorMasterBOList = new ArrayList<InvestorMasterBO>();
						batchCount = 0;

						investorMasterBOList = populateModelList(feed, investorMasterBODTO, investorMasterBOList,
								advisorUser);

						batchCount++;

					} else {

						investorMasterBOList = populateModelList(feed, investorMasterBODTO, investorMasterBOList,
								advisorUser);

						batchCount++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			if (batchCount > 0) {
				counter++;
				log.debug("LIST size() when LAST COUNTER is: " + counter + ", and batchCount is: "
						+ investorMasterBOList.size());
				uploadResponseDTO = saveModelData(investorMasterBOList, uploadResponseDTO);
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

	private List<InvestorMasterBO> populateModelList(Map<String, String> feedData,
			InvestorMasterBODTO investorMasterBODTO, List<InvestorMasterBO> investorMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		
		
		// TODO Auto-generated method stub

		try {
			String guardianPanCheck = null, pan = null, guardianPAN = null;
			boolean isRecordInvalid = false;
			int age = 0;

			if (feedData.get(FinexaBOColumnConstant.INVESTOR_FOLIO_NUMBER) == null
					|| feedData.get(FinexaBOColumnConstant.INVESTOR_FOLIO_NUMBER).toString().trim().isEmpty()
					|| feedData.get(FinexaBOColumnConstant.INVESTOR_FOLIO_NUMBER).toString().trim() == "") {
				isRecordInvalid = true;
			} else if (feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_PAN) == null
					|| feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_PAN).toString().trim().isEmpty()
					|| feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_PAN).toString().trim() == ""
					|| !feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_PAN).toString().trim().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
				
				if(feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN) != null
						&& !feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN).toString().trim().isEmpty()
						&& feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN).toString().trim() != ""
						&& feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN).toString().trim().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
					guardianPanCheck = feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN).toString()
							.trim();
					if(feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_DATE_OF_BIRTH) != null
						&& !feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_DATE_OF_BIRTH).toString().trim().isEmpty()
						&& feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_DATE_OF_BIRTH).toString().trim() != ""
						&& feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_DATE_OF_BIRTH).length() > 0) {
						age = ageCalculator(
								dateFormatter(feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_DATE_OF_BIRTH)));
					} else {
						age = 0;
					}
					
					if (age < ADULT_AGE) {
						if (guardianPanCheck == null || guardianPanCheck.isEmpty()
								|| !guardianPanCheck.matches(".*\\d.*")) {
							isRecordInvalid = true;

						}
				}
				
				} else
					isRecordInvalid = true;
			}

			if (isRecordInvalid == true) {
				Integer rowNum = rejectedRowNumber + 1;
				rejectedRowNumbers = rejectedRowNumbers + rowNum.toString() + ",";
				//log.debug("rejectedRowNumbers:"+rejectedRowNumbers);
				noOfRejectedRecords++;

				return investorMasterBOList;
			}

			InvestorMasterBO investorMasterBO = mapper.map(investorMasterBODTO, InvestorMasterBO.class);
			InvestorMasterBOPK investorMasterBOPK = new InvestorMasterBOPK();

			investorMasterBOPK
					.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_FOLIO_NUMBER)));
			investorMasterBO.setInvestorDOB(formatUtilDateToSQLDate(dateFormatter(feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_DATE_OF_BIRTH))));
			//System.out.println(feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_PAN)+","+feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN));
			if(feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_PAN) != null
					&& !feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_PAN).toString().trim().isEmpty()
					&& feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_PAN).toString().trim() != ""
					&& feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_PAN).toString().trim().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {			
			pan = feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_PAN).toString().trim();
			}
			if(feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN) != null
					&& !feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN).toString().trim().isEmpty()
					&& feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN).toString().trim() != ""
					&& feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN).toString().trim().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
			guardianPAN = feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN).toString().trim();
			}
			
			if (pan != null && !pan.isEmpty() && pan.matches(".*\\d.*")) {
				investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_PAN));
			} else {
				if (guardianPAN != null && !guardianPAN.isEmpty() && guardianPAN.matches(".*\\d.*")) {
					investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN));
				} else {
					log.debug("PAN NOT FOUND!! FOR INVESTOR AND GUARDIAN AS WELL!");
				}
			}
			
			// investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_PAN));
			investorMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_NAME).trim());
			investorMasterBO.setAddressLine1(feedData.get(FinexaBOColumnConstant.INVESTOR_ADDRESS_LINE_1));
			investorMasterBO.setAddressLine2(feedData.get(FinexaBOColumnConstant.INVESTOR_ADDRESS_LINE_2));
			investorMasterBO.setAddressLine3(feedData.get(FinexaBOColumnConstant.INVESTOR_ADDRESS_LINE_3));
			investorMasterBO.setCity(feedData.get(FinexaBOColumnConstant.INVESTOR_CITY));
			if(feedData.get(FinexaBOColumnConstant.INVESTOR_PINCODE).matches("[0-9]+"))
				investorMasterBO.setPincode(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_PINCODE)));
			else
				investorMasterBO.setPincode("0");
			investorMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.INVESTOR_SCHEME_RTA_CODE));
			investorMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.INVESTOR_SCHEME_NAME));
			/*
			 * investorMasterBO.setBalanceDate(finexaUtil.formatDate(finexaUtil
			 * .formatStringDate(feedData.get(FinexaBOColumnConstant.INVESTOR_BALANCE_DATE))
			 * ));
			 * 
			 * investorMasterBO.setUnitBalance(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_UNIT_BALANCE));
			 * investorMasterBO.setRupeeBalance(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_RUPEE_BALANCE));
			 */
			investorMasterBO.setJointName1(feedData.get(FinexaBOColumnConstant.INVESTOR_JOINT_NAME1));
			investorMasterBO.setJointName2(feedData.get(FinexaBOColumnConstant.INVESTOR_JOINT_NAME2));
			investorMasterBO.setPhoneOffice(feedData.get(FinexaBOColumnConstant.INVESTOR_PHONE_OFFICE));
			investorMasterBO.setPhoneResidence(feedData.get(FinexaBOColumnConstant.INVESTOR_PHONE_RESIDENCE));
			investorMasterBO.setEmail(feedData.get(FinexaBOColumnConstant.INVESTOR_EMAIL));
			investorMasterBO.setModeOfHolding(feedData.get(FinexaBOColumnConstant.INVESTOR_MODE_OF_HOLDING));
			investorMasterBO.setUin(feedData.get(FinexaBOColumnConstant.INVESTOR_UIN));
			investorMasterBO.setJoint1PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_JOINT1_PAN));
			investorMasterBO.setJoint2PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_JOINT2_PAN));
			investorMasterBO.setGuardianPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_PAN));

			investorMasterBO.setTaxStatus(feedData.get(FinexaBOColumnConstant.INVESTOR_TAX_STATUS));
			investorMasterBO.setDistributorARNCode(feedData.get(FinexaBOColumnConstant.INVESTOR_DISTRIBUTOR_ARN_CODE));
			/*
			 * investorMasterBO.setSubbrokerDealerCode(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_SUBBROKER_DEALER_CODE));
			 * investorMasterBO.setReinvFlag(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_REINV_FLAG));
			 */
			investorMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.INVESTOR_BANK_NAME));
			investorMasterBO.setBankBranch(feedData.get(FinexaBOColumnConstant.INVESTOR_BANK_BRANCH));
			investorMasterBO.setBankAccountType(feedData.get(FinexaBOColumnConstant.INVESTOR_BANK_AC_TYPE));
			investorMasterBO.setBankAccountNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_BANK_AC_NO));
			investorMasterBO.setBankAddress1(feedData.get(FinexaBOColumnConstant.INVESTOR_BANK_ADDRESS_1));
			investorMasterBO.setBankAddress2(feedData.get(FinexaBOColumnConstant.INVESTOR_BANK_ADDRESS_2));
			investorMasterBO.setBankAddress3(feedData.get(FinexaBOColumnConstant.INVESTOR_BANK_ADDRESS_3));
			investorMasterBO.setBankCity(feedData.get(FinexaBOColumnConstant.INVESTOR_BANK_CITY));

			investorMasterBO.setBankPincode(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_BANK_PINCODE)));
			String mobileNo = "";
			if(feedData.get(FinexaBOColumnConstant.INVESTOR_MOBILE_NUMBER) != null
					&& !feedData.get(FinexaBOColumnConstant.INVESTOR_MOBILE_NUMBER).toString().trim().isEmpty()
					&& feedData.get(FinexaBOColumnConstant.INVESTOR_MOBILE_NUMBER).toString().trim() != "") {
				mobileNo = feedData.get(FinexaBOColumnConstant.INVESTOR_MOBILE_NUMBER);
				if(mobileNo.contains(",")) {
					mobileNo = mobileNo.substring(0, mobileNo.indexOf(","));
				}
			}	
			investorMasterBO.setMobileNumber(mobileNo);
			investorMasterBO.setOccupation(feedData.get(FinexaBOColumnConstant.INVESTOR_OCCUPATION));
			investorMasterBO.setInvestorMin(feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_MIN));

			investorMasterBO.setNomineeName(feedData.get(FinexaBOColumnConstant.INVESTOR_NOMINEE_NAME));
			/*
			 * investorMasterBO .setNomineeRelation(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE_RELATION)); investorMasterBO
			 * .setNomineeAddress1(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE_ADDRESS1)); investorMasterBO
			 * .setNomineeAddress2(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE_ADDRESS2)); investorMasterBO
			 * .setNomineeAddress3(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE_ADDRESS3));
			 * investorMasterBO.setNomineeCity(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE_CITY));
			 * investorMasterBO.setNomineeState(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE_STATE)); investorMasterBO
			 * .setNomineePincode(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE_PINCODE)); investorMasterBO.setNomineePhoneOffice(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_NOMINEE_PHONE_OFFICE));
			 * investorMasterBO.setNomineePhoneResidence(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_NOMINEE_PHONE_RESIDENCE));
			 * investorMasterBO.setNomineeEmail(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE_EMAIL)); investorMasterBO
			 * .setNomineePercentage(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE_PERCENTAGE));
			 */
			investorMasterBO.setNominee2Name(feedData.get(FinexaBOColumnConstant.INVESTOR_NOMINEE2_NAME));
			/*
			 * investorMasterBO .setNominee2Relation(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE2_RELATION)); investorMasterBO.setNominee2Address1(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_NOMINEE2_ADDRESS1));
			 * investorMasterBO .setNominee2Address2(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE2_ADDRESS2)); investorMasterBO
			 * .setNominee2Address3(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE2_ADDRESS3));
			 * investorMasterBO.setNominee2City(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE2_CITY));
			 * investorMasterBO.setNominee2State(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE2_STATE)); investorMasterBO
			 * .setNominee2Pincode(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE2_PINCODE)); investorMasterBO.setNominee2PhoneOffice(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_NOMINEE2_PHONE_OFFICE));
			 * investorMasterBO.setNominee2PhoneResidence(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_NOMINEE2_PHONE_RESIDENCE));
			 * investorMasterBO.setNominee2Email(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE2_EMAIL)); investorMasterBO.setNominee2Percentage(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_NOMINEE2_PERCENTAGE));
			 */
			investorMasterBO.setNominee3Name(feedData.get(FinexaBOColumnConstant.INVESTOR_NOMINEE3_NAME));
			/*
			 * investorMasterBO .setNominee3Relation(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE3_RELATION)); investorMasterBO
			 * .setNominee3Address1(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE3_ADDRESS1)); investorMasterBO
			 * .setNominee3Address2(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE3_ADDRESS2)); investorMasterBO
			 * .setNominee3Address3(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE3_ADDRESS3));
			 * investorMasterBO.setNominee3City(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE3_CITY));
			 * investorMasterBO.setNominee3State(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE3_STATE)); investorMasterBO
			 * .setNominee3Pincode(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE3_PINCODE)); investorMasterBO.setNominee3PhoneOffice(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_NOMINEE3_PHONE_OFFICE));
			 * investorMasterBO.setNominee3PhoneResidence(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_NOMINEE3_PHONE_RESIDENCE));
			 * investorMasterBO.setNominee3Email(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_NOMINEE3_EMAIL)); investorMasterBO.setNominee3Percentage(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_NOMINEE3_PERCENTAGE));
			 */
			investorMasterBO.setIfscCode(feedData.get(FinexaBOColumnConstant.INVESTOR_IFSC_CODE));
			investorMasterBO.setDpId(feedData.get(FinexaBOColumnConstant.INVESTOR_DP_ID));
			investorMasterBO.setDemat(feedData.get(FinexaBOColumnConstant.INVESTOR_DEMAT));
			investorMasterBO.setGuardianName(feedData.get(FinexaBOColumnConstant.INVESTOR_NAME_OF_GUARDIAN));
			investorMasterBO.setBrokerDealerCode(feedData.get(FinexaBOColumnConstant.INVESTOR_BROKER_DEALER_CODE));
			investorMasterBO
					.setFolioCreateDate(dateFormatter(feedData.get(FinexaBOColumnConstant.INVESTOR_FOLIO_CREATE_DATE)));
			investorMasterBO
					.setInvestorAdhaarNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_INVESTOR_AADHAAR_NUMBER));
			// investorMasterBO.setTpaLinked(feedData.get("TPA_LINKED"));
			investorMasterBO.setFirstHolderCKYCNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_FIRST_HOLDER_CKYC));
			investorMasterBO
					.setJointHolder1CKYCNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_JOINT_HOLDER1_CKYC));
			investorMasterBO
					.setJointHolder2CKYCNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_JOINT_HOLDER2_CKYC));
			investorMasterBO.setGuardianCKYCNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_GUARDIAN_CKYC));
			/*
			 * investorMasterBO
			 * .setJointHolder1DOB(finexaUtil.formatDate(finexaUtil.formatStringDate(
			 * feedData.get("JH1_DOB")))); investorMasterBO
			 * .setJointHolder2DOB(finexaUtil.formatDate(finexaUtil.formatStringDate(
			 * feedData.get("JH2_DOB")))); investorMasterBO
			 * .setGuardianDOB(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.
			 * get("GUARDIAN_D"))));
			 */
			investorMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.INVESTOR_AMC_CODE));
			// investorMasterBO.setGstStateCode(feedData.get("GST_STATE_"));
			
			investorMasterBO.setCreatedOn(CREATEDON);
             
			if (investorMasterBODTO.getNameRTA() == FinexaBOColumnConstant.RTA_TYPE_FRANKLIN)
				investorMasterBO.setAmcCode(FinexaBOColumnConstant.AMC_FRANKLIN);
			else if (investorMasterBODTO.getNameRTA() == FinexaBOColumnConstant.RTA_TYPE_SUNDARAM)
				investorMasterBO.setAmcCode(FinexaBOColumnConstant.AMC_SUNDARAM);
			else
				investorMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.INVESTOR_AMC_CODE));

			investorMasterBO.setLookupRtabo(lookupRTABOMap.get(investorMasterBODTO.getNameRTA()));
			investorMasterBO.setAdvisorUser(advisorUser);
			investorMasterBO.setId(investorMasterBOPK);

			investorMasterBOList.add(investorMasterBO);
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return investorMasterBOList;

	}

	private UploadResponseDTO readDBFFeed(InvestorMasterBODTO investorMasterBODTO, UploadResponseDTO uploadResponseDTO)
			throws IOException, InvalidFormatException, ParseException {
		try {
			InputStream initialStream = investorMasterBODTO.getNameSelectFile()[0].getInputStream();
			InputStream initialStream2 = investorMasterBODTO.getNameSelectFile()[0].getInputStream();
			dbfReader = new DBFReader(initialStream);
			boolean headerFound = false, firstRow, isUniqueColumnNotFound = false;
			int numberOfFields;
			String column;
			Object[] rowObjects1, rowObjects2;
			int batchCount = 0, counter = 0, headerRowCount = 0, row, numberOfRecords = 0, numberOfRecordRunning = 0;
			List<InvestorMasterBO> investorMasterBOList = new ArrayList<InvestorMasterBO>();
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
			databaseColumnIDAndNameList = masterDatabaseColumnNamesBORepository
					.getIDAndDatabaseColumnNames(FinexaBOColumnConstant.FEED_TYPE_INVM);

			// Fetching the the database column IDs and feed column names list based on the
			// type of feed file
			feedColumnNameList = masterFeedColumnNamesBORepository
					.getIDAndFeedColumnNames(FinexaBOColumnConstant.FEED_TYPE_INVM);

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
					// System.out.println(dbColumnID + dbColumnName);
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
								//column = columnName;

								// Storing the database column ID as a key and corresponding feed column name as
								// the value.
								databaseColumnIDAndExcelFieldIDMap.put(dbColumnID, feedMap.get(columnName.toLowerCase().trim()));

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

			for (String dbCOlumnID : FinexaBOColumnConstant.INVESTOR_DB_COLUMN_ID_LIST) {
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

			AdvisorUser advisorUser = advisorUserRepository.findOne(investorMasterBODTO.getAdvisorId());

			dbfReader = new DBFReader(initialStream2);
			// Starts reading after the header row
			row = 0;
			rowObjects2 = dbfReader.nextRecord();
			rejectedRowNumber = headerRowCount;
			while (rowObjects2 != null) {
				try {
					if (++row <= headerRowCount)
						continue;
					boolean isRowNotBlank = false;
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
					/*
					for (Map.Entry<String,String> entry : databaseColumnIDAndDataMap.entrySet()) { 
			            
			            if(entry.getValue() != null || !entry.getValue().trim().isEmpty() || entry.getValue() != "") {
			            	isRowNotBlank = true;
			            	break;
			            }	
					} 
					*/
					if (batchCount == 100) {
						counter++;
						log.debug("investorMasterBOList size() when COUNTER is:" + counter + " batchCount is: "
								+ investorMasterBOList.size());
						uploadResponseDTO = saveModelData(investorMasterBOList, uploadResponseDTO);
						investorMasterBOList = new ArrayList<InvestorMasterBO>();
						batchCount = 0;

						//System.out.println("*********************************");

						investorMasterBOList = populateModelList(databaseColumnIDAndDataMap, investorMasterBODTO,
								investorMasterBOList, advisorUser);

						batchCount++;

					} else {

						investorMasterBOList = populateModelList(databaseColumnIDAndDataMap, investorMasterBODTO,
								investorMasterBOList, advisorUser);

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
				log.debug("investorMasterBOList size() before LAST: " + investorMasterBOList.size());
				uploadResponseDTO = saveModelData(investorMasterBOList, uploadResponseDTO);
			}
			// System.out.println(dataMap);

			uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uploadResponseDTO;

	}

	/*
	 * private List<InvestorMasterBO> populateDBFModelList(Map<String, String>
	 * feedData, InvestorMasterBODTO investorMasterBODTO, List<InvestorMasterBO>
	 * investorMasterBOList, AdvisorUser advisorUser) throws ParseException { //
	 * TODO Auto-generated method stub
	 * 
	 * try { boolean isRecordInvalid = false;
	 * 
	 * if (feedData.get(FinexaBOColumnConstant.AUM_REPORT_DATE) == null ||
	 * feedData.get(FinexaBOColumnConstant.AUM_REPORT_DATE).isEmpty() ||
	 * feedData.get(FinexaBOColumnConstant.AUM_REPORT_DATE) == "") { isRecordInvalid
	 * = true; } else if
	 * (feedData.get(FinexaBOColumnConstant.AUM_FOLIO_NUMBER).toString().trim() ==
	 * null ||
	 * feedData.get(FinexaBOColumnConstant.AUM_FOLIO_NUMBER).toString().trim().
	 * isEmpty() ||
	 * feedData.get(FinexaBOColumnConstant.AUM_FOLIO_NUMBER).toString().trim() ==
	 * "") { isRecordInvalid = true; } else if
	 * (feedData.get(FinexaBOColumnConstant.AUM_SCHEME_RTA_CODE).toString().trim()
	 * == null ||
	 * feedData.get(FinexaBOColumnConstant.AUM_SCHEME_RTA_CODE).toString().trim().
	 * isEmpty() ||
	 * feedData.get(FinexaBOColumnConstant.AUM_SCHEME_RTA_CODE).toString().trim() ==
	 * "") {
	 * 
	 * isRecordInvalid = true; }
	 * 
	 * if (isRecordInvalid == true) { Integer rowNum = rejectedRowNumber + 1;
	 * rejectedRowNumbers = rejectedRowNumbers + rowNum.toString() + ",";
	 * noOfRejectedRecords++;
	 * 
	 * return investorMasterBOList; }
	 * 
	 * AumMasterBO aumMasterBO = mapper.map(aumMasterBODTO, AumMasterBO.class);
	 * AumMasterBOPK aumMasterBOPK = new AumMasterBOPK();
	 * aumMasterBOPK.setFolioNumber(formatNumbers(feedData.get(
	 * FinexaBOColumnConstant.AUM_FOLIO_NUMBER)));
	 * aumMasterBOPK.setSchemertacode(feedData.get(FinexaBOColumnConstant.
	 * AUM_SCHEME_RTA_CODE));
	 * aumMasterBOPK.setReportDate(dateFormatter(feedData.get(FinexaBOColumnConstant
	 * .AUM_REPORT_DATE)));
	 * aumMasterBO.setUnitBalance(formatAmount(feedData.get(FinexaBOColumnConstant.
	 * AUM_UNIT_BALANCE)));
	 * aumMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.
	 * AUM_SUB_BROKER_CODE));
	 * aumMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.AUM_SCHEME_NAME
	 * )); aumMasterBO.setPinCode(formatNumbers(feedData.get(FinexaBOColumnConstant.
	 * AUM_PINCODE)));
	 * aumMasterBO.setNav(feedData.get(FinexaBOColumnConstant.AUM_NAV));
	 * aumMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.
	 * AUM_INVESTOR_NAME));
	 * aumMasterBO.setDistributorarncode(feedData.get(FinexaBOColumnConstant.
	 * AUM_DISTRIBUTOR_ARN_CODE));
	 * aumMasterBO.setCurrentValue(formatAmount(feedData.get(FinexaBOColumnConstant.
	 * AUM_CURRENT_VALUE)));
	 * 
	 * if(aumMasterBODTO.getNameRTA() == FinexaBOColumnConstant.RTA_TYPE_FRANKLIN)
	 * aumMasterBO.setAmcCode(FinexaBOColumnConstant.AMC_FRANKLIN); else
	 * if(aumMasterBODTO.getNameRTA() == FinexaBOColumnConstant.RTA_TYPE_SUNDARAM)
	 * aumMasterBO.setAmcCode(FinexaBOColumnConstant.AMC_SUNDARAM); else
	 * aumMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.AUM_AMC_CODE));
	 * 
	 * aumMasterBO.setLookupRtabo(lookupRTABOMap.get(aumMasterBODTO.getNameRTA()));
	 * aumMasterBO.setAdvisorUser(advisorUser); aumMasterBO.setId(aumMasterBOPK);
	 * 
	 * investorMasterBOList.add(aumMasterBO); } catch (RuntimeException e) {
	 * e.printStackTrace(); // throw new RuntimeException(e); }
	 * 
	 * return aumMasterBOList;
	 * 
	 * }
	 */
	private synchronized UploadResponseDTO saveModelData(List<InvestorMasterBO> investorMasterBOList,
			UploadResponseDTO uploadResponseDTO) {
		// TODO Auto-generated method stub
		// System.out.println("Uploading AUM");
		try {
			if (investorMasterBOList != null && investorMasterBOList.size() > 0) {
				investMasterBORepository.save(investorMasterBOList);
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

	private int ageCalculator(java.util.Date dob) throws ParseException {
		try {
			// direct age calculation
			int age = 0;

			java.util.Date d = dob;
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int date = c.get(Calendar.DATE);
			LocalDate l1 = LocalDate.of(year, month, date);
			LocalDate now1 = LocalDate.now();
			Period diff1 = Period.between(l1, now1);
			age = diff1.getYears();
			//System.out.println("age:" + diff1.getYears() + "years");
			return age;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
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
