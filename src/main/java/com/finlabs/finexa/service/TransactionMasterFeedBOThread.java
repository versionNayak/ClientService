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
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.TransactionMasterBODTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.BackOfficeUploadHistory;
import com.finlabs.finexa.model.LookupRTABO;
import com.finlabs.finexa.model.LookupTransactionRule;
import com.finlabs.finexa.model.StagingFolioMasterBO;
import com.finlabs.finexa.model.StagingInvestorMasterBO;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.BackOfficeUploadHistoryRepository;
import com.finlabs.finexa.repository.LookupRTABORepository;
import com.finlabs.finexa.repository.LookupTransactionRuleRepository;
import com.finlabs.finexa.repository.MasterDatabaseColumnNamesBORepository;
import com.finlabs.finexa.repository.MasterFeedColumnNamesBORepository;
import com.finlabs.finexa.repository.StagingFolioMasterBORepository;
import com.finlabs.finexa.repository.StagingInvestorMasterBORepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.util.FinexaBOColumnConstant;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

@Component

@Scope("prototype")

public class TransactionMasterFeedBOThread implements Runnable {

	private static Logger log = LoggerFactory.getLogger(TransactionMasterFeedBOThread.class);
	private TransactionMasterBODTO transactionMasterBODTO;
	private UploadResponseDTO uploadResponseDTO;
	private BackOfficeUploadHistory backOfficeUploadHistory;

	private Integer rta;
	private String rejectedRowNumbers = "";
	private Integer rejectedRowNumber = 0;
	private int rows = 1, columnHeaderRow = 0, noOfRejectedRecords = 0;
	private boolean allRowsInserted = false;
	private final int ROW_LENGTH = 255;
	private final Integer PRIMARY_KEY = 106;
	private Map<Integer, LookupRTABO> lookupRTABOMap = new HashMap<Integer, LookupRTABO>();
	private Map<String, String> clientNamePANMap = new HashMap<>();
	private Map<String, String> clientFolioNameMap = new HashMap<>();
	private Map<String, TransactionMasterBO> transactionNumberAndMasterBOMap;
	DBFReader dbfReader;

	@Autowired
	private Mapper mapper;

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private LookupRTABORepository lookupRTABORepository;

	@Autowired
	private LookupTransactionRuleRepository lookupTransactionRuleRepository;

	@Autowired
	private BackOfficeUploadHistoryRepository uploadHistoryRepo;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private MasterDatabaseColumnNamesBORepository masterDatabaseColumnNamesBORepository;

	@Autowired
	private MasterFeedColumnNamesBORepository masterFeedColumnNamesBORepository;

	@Autowired
	private AUMAutoCreationNewService aumAutoCreationNewService;

	@Autowired
	private StagingInvestorMasterBORepository stagingInvestorMasterBORepository;

	@Autowired
	private StagingFolioMasterBORepository stagingFolioMasterBORepository;

	public void initialize(TransactionMasterBODTO transactionMasterBODTO, UploadResponseDTO uploadResponseDTO,
			BackOfficeUploadHistory backOfficeUploadHistory) {
		this.transactionMasterBODTO = transactionMasterBODTO;
		this.uploadResponseDTO = uploadResponseDTO;
		this.backOfficeUploadHistory = backOfficeUploadHistory;
	}

	@Override
	public void run() {
		try {

			rta = transactionMasterBODTO.getNameRTA();
			rta.toString();
			String name = transactionMasterBODTO.getNameSelectFile()[0].getOriginalFilename();
			if (name.contains(FinexaBOColumnConstant.DBF_LOWER_CASE)
					|| name.contains(FinexaBOColumnConstant.DBF_UPPER_CASE)) {
				uploadResponseDTO = readDBFFeed(transactionMasterBODTO, uploadResponseDTO);
			} else {
				uploadResponseDTO = readExcelFeed(transactionMasterBODTO, uploadResponseDTO);
			}
			if (uploadResponseDTO.isStatus()) {
				int reasonLength = rejectedRowNumbers.length()
						+ (FinexaBOColumnConstant.TRANSACTION_RECORD_REJECTION_MESSAGE).length();
				backOfficeUploadHistory.setStatus(FinexaBOColumnConstant.STATUS_COMPLETED);
				if (noOfRejectedRecords > 0) {
					System.out.println("Rejected Records:" + rejectedRowNumbers.length());
					if (reasonLength <= ROW_LENGTH) {
						System.out.println((FinexaBOColumnConstant.TRANSACTION_RECORD_REJECTION_MESSAGE).length());
						// rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
						backOfficeUploadHistory.setReasonOfRejection(
								FinexaBOColumnConstant.TRANSACTION_RECORD_REJECTION_MESSAGE + rejectedRowNumbers);

					} else {
						rejectedRowNumbers = rejectedRowNumbers.substring(0, rejectedRowNumbers.indexOf(','))
						/* + rejectedRowNumber.toString() + "," */;

						backOfficeUploadHistory
								.setReasonOfRejection(FinexaBOColumnConstant.TRANSACTION_RECORD_REJECTION_MESSAGE
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
			log.debug("TRANSACTION FEED UPLOADING FINISHED!");
			log.debug("Auto AUM calculation STARTED..");
			//aumAutoCreationNewService.createAutoAUM(transactionMasterBODTO.getAdvisorID(), clientNamePANMap, clientFolioNameMap);
			log.debug("Auto AUM calculation COMPLETED..");
		} catch (RuntimeException | InvalidFormatException | IOException | ParseException e) {
			if (allRowsInserted == true) {
				backOfficeUploadHistory.setStatus(FinexaBOColumnConstant.STATUS_COMPLETED);
				backOfficeUploadHistory.setRejectedRecords(uploadResponseDTO.getRejectedRecords());
				backOfficeUploadHistory.setEndTime(new java.util.Date());
				backOfficeUploadHistory.setAutoClientCreationStatus(FinexaBOColumnConstant.STATUS_NOT_APPLICABLE);
				backOfficeUploadHistory.setReasonOfRejection(
						FinexaBOColumnConstant.TRANSACTION_RECORD_REJECTION_MESSAGE + rejectedRowNumbers);
				backOfficeUploadHistory = uploadHistoryRepo.save(backOfficeUploadHistory);
				log.debug("TRANSACTION FEED UPLOADING FINISHED!");
				log.debug("Auto AUM calculation STARTED..");
				//aumAutoCreationNewService.createAutoAUM(transactionMasterBODTO.getAdvisorID(), clientNamePANMap, clientFolioNameMap);
				log.debug("Auto AUM calculation COMPLETED..");
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
	private UploadResponseDTO readExcelFeed(TransactionMasterBODTO transactionMasterBODTO,
			UploadResponseDTO uploadResponseDTO) throws IOException, InvalidFormatException, ParseException {
		try {
			// null pointer handling
			transactionNumberAndMasterBOMap = new HashMap<>();
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

			InputStream initialStream = transactionMasterBODTO.getNameSelectFile()[0].getInputStream();
			String uuid = UUID.randomUUID().toString();
			// System.out.println(uuid);
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
					.getIDAndDatabaseColumnNames(FinexaBOColumnConstant.FEED_TYPE_TRM);

			// Fetching the the database column IDs and feed column names list based on the
			// type of feed file
			feedColumnNameList = masterFeedColumnNamesBORepository
					.getIDAndFeedColumnNames(FinexaBOColumnConstant.FEED_TYPE_TRM);

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
			// System.out.println("Rows" + rows);

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
					if(dbColumnID.equals("259")) {
						System.out.println(dbColumnID + dbColumnName);	
					}
					
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
								excelColNameAndAddressMap.put(column.toLowerCase().trim(),
										excelNameColNameMap.get(column.toLowerCase().trim()));
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
			for (Map.Entry<String,String> entry : databaseColumnIDAndExcelFieldNameMap.entrySet())  
	            System.out.println("Key = " + entry.getKey() + 
	                             ", Value = " + entry.getValue()); 
			// System.out.println(excelColNameAndAddressMap);

			for (String dbCOlumnID : FinexaBOColumnConstant.TRANSACTION_DB_COLUMN_ID_LIST) {
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

			List<TransactionMasterBO> transactionMasterBOList = new ArrayList<TransactionMasterBO>();

			// Empty the list each time after inserting 20 records
			int batchCount = 0;

			// try-catch
			AdvisorUser advisorUser = advisorUserRepository.findOne(transactionMasterBODTO.getAdvisorID());
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
					for (Map.Entry<String, String> entry : feed.entrySet()) {

						if (entry.getValue() != null && !entry.getValue().trim().isEmpty() && entry.getValue() != "") {
							isRowNotBlank = true;
							break;
						}
					}
					if (isRowNotBlank == false)
						continue;
					// System.out.println(feed);
					if (batchCount == 100) {
						counter++;
						log.debug("LIST size() when COUNTER is: " + counter + ", and batchCount is: "
								+ transactionMasterBOList.size());
						uploadResponseDTO = saveModelData(transactionMasterBOList, uploadResponseDTO);
						transactionMasterBOList = new ArrayList<TransactionMasterBO>();
						batchCount = 0;

						transactionMasterBOList = populateModelList(feed, transactionMasterBODTO,
								transactionMasterBOList, advisorUser);

						batchCount++;

					} else {

						transactionMasterBOList = populateModelList(feed, transactionMasterBODTO,
								transactionMasterBOList, advisorUser);

						batchCount++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			if (batchCount > 0) {
				counter++;
				log.debug("LIST size() when LAST COUNTER is: " + counter + ", and batchCount is: "
						+ transactionMasterBOList.size());
				uploadResponseDTO = saveModelData(transactionMasterBOList, uploadResponseDTO);
				allRowsInserted = true;
			}
			targetFile.delete();
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		}
		return uploadResponseDTO;
	}

	private List<TransactionMasterBO> populateModelList(Map<String, String> feedData,
			TransactionMasterBODTO transactionMasterBODTO, List<TransactionMasterBO> transactionMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordInvalid = false;
			boolean isPANSet = false;
			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_NUMBER) == null
					|| feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_NUMBER).isEmpty()
					|| feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_NUMBER) == "") {
				isRecordInvalid = true;
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE) == null
					|| feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).trim().isEmpty()
					|| feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).trim() == "") {
				isRecordInvalid = true;
			}

			if (isRecordInvalid == true) {
				Integer rowNum = rejectedRowNumber + 1;
				rejectedRowNumbers = rejectedRowNumbers + rowNum.toString() + ",";
				noOfRejectedRecords++;

				return transactionMasterBOList;
			}

			TransactionMasterBO transactionMasterBO = mapper.map(transactionMasterBODTO, TransactionMasterBO.class);

			transactionMasterBO.setAdvisorUser(advisorUser);

			transactionMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_AMC_CODE));
			transactionMasterBO.setApplicationNo(feedData.get(FinexaBOColumnConstant.TRANSACTION_APPLICATION_NUMBER));
			transactionMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.TRANSACTION_BANK_NAME));
			// transactionMasterBO.setBasicTDS(feedData.get("tax"));
			transactionMasterBO.setBrokerageAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_BROKERAGE_AMOUNT));
			transactionMasterBO
					.setBrokeragePercentage(feedData.get(FinexaBOColumnConstant.TRANSACTION_BROKERAGE_PERCENTAGE));
			// transactionMasterBO.setCaInitiatedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("ca_initiated_date"))));
			// transactionMasterBO.setCgstAmt(feedData.get("cgst_amount"));
			transactionMasterBO
					.setDistributorARNCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DISTRIBUTOR_ARN_CODE));
			// transactionMasterBO.setDpId(feedData.get("dp_id"));
			// transactionMasterBO.setEligibleAmt(feedData.get("eligib_amt"));
			// transactionMasterBO.setEntryLoad(feedData.get("load"));
			transactionMasterBO.setEuin(feedData.get(FinexaBOColumnConstant.TRANSACTION_EUIN));
			// transactionMasterBO.setEuinOpted(feedData.get("euin_opted"));
			transactionMasterBO
					.setEuinValidIndicator(feedData.get(FinexaBOColumnConstant.TRANSACTION_EUIN_VALID_INDICATOR));
			// transactionMasterBO.setExchangeFlag(feedData.get("exchange_flag"));
			// transactionMasterBO.setExchDcFlag(feedData.get("exch_dc_flag"));
			String folio_number;
			if (!feedData.get(FinexaBOColumnConstant.TRANSACTION_FOLIO_NUMBER).contains("/")) {
				double folioNumberDouble = Double
						.parseDouble(feedData.get(FinexaBOColumnConstant.TRANSACTION_FOLIO_NUMBER));
				folio_number = String.format("%.0f", folioNumberDouble);
			} else {
				folio_number = feedData.get(FinexaBOColumnConstant.TRANSACTION_FOLIO_NUMBER);
			}
			transactionMasterBO.setFolioNo(formatNumbers(folio_number));
			// transactionMasterBO.setForm15HDetails(feedData.get("te_15h"));
			// transactionMasterBO.setGstStateCode(feedData.get("gst_state_code"));
			// transactionMasterBO.setIgstAmt(feedData.get("igst_amount"));
			// transactionMasterBO.setInvestorAccountNo(feedData.get("ac_no"));
			transactionMasterBO.setInvestorId(feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_ID));
			// transactionMasterBO.setInvestorMin(feedData.get("inv_iin"));
			transactionMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_NAME));
			transactionMasterBO.setInvestorPan(feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_PAN));
			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_PAN) != null
					&& !feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_PAN).trim().isEmpty()
					&& feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_PAN).trim() != "") {

				if (clientNamePANMap.size() > 0) {
					if (!clientNamePANMap.containsKey(
							feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_NAME).toString().trim())) {
						clientNamePANMap.put(feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_NAME).toString()
								.trim() + "-"
								+ feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_PAN).toString().trim(), "");
					}
				} else {

					clientNamePANMap.put(
							feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_NAME).toString().trim() + "-"
									+ feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_PAN).toString().trim(),
							"");
				}

			} else {
				/*****************
				 * Investor PAN issue is being handled here
				 ********************/

				List<StagingFolioMasterBO> stagingFolioMasterBOList = stagingFolioMasterBORepository
						.findByInvestorFolio(transactionMasterBO.getFolioNo());
				if (stagingFolioMasterBOList.size() > 0) {
					for (StagingFolioMasterBO stagingFolioMasterBO : stagingFolioMasterBOList) {
						try {

							StagingInvestorMasterBO stagingInvestorMasterBO = stagingFolioMasterBO
									.getStaginginvestormasterbo();
							if (stagingInvestorMasterBO != null
									&& stagingInvestorMasterBO.getAdvisoruser().getId() == advisorUser.getId()
									&& stagingInvestorMasterBO.getInvestorName()
											.equalsIgnoreCase(transactionMasterBO.getInvestorName().trim())) {

								transactionMasterBO.setInvestorPan(stagingInvestorMasterBO.getInvestorPAN().trim());
								isPANSet = true;
								/*****************************************************
								 * Code repeated
								 ****************/
								if (transactionMasterBO.getInvestorPan() != null
										&& !transactionMasterBO.getInvestorPan().trim()
												.isEmpty()
										&& transactionMasterBO.getInvestorPan() != "") {

									if (clientNamePANMap.size() > 0) {
										if (!clientNamePANMap.containsKey(
												feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_NAME)
														.toString().trim())) {
											clientNamePANMap.put(feedData
													.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_NAME).toString()
													.trim() + "-"
													+ transactionMasterBO.getInvestorPan()
															.toString().trim(),
													"");
										}
									} else {

										clientNamePANMap.put(feedData
												.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_NAME).toString().trim()
												+ "-" + transactionMasterBO.getInvestorPan()
														.toString().trim(),
												"");
									}

								}
								break;
								/**********************************************************************************/
							} else {
								log.debug("Investor: " + transactionMasterBO.getInvestorName()
										+ ", not found in Staging Investor.");
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					log.debug("No match found of the Folio: " + transactionMasterBO.getFolioNo());
				}

				/************************************/

			}
			// transactionMasterBO.setLocation(feedData.get("location"));
			// transactionMasterBO.setLocationCategory(feedData.get("ter_location"));
			if (isPANSet == false) {
				if (transactionMasterBO.getFolioNo() != null && transactionMasterBO.getFolioNo().trim() != ""
						&& !transactionMasterBO.getFolioNo().trim().isEmpty()) {
					clientFolioNameMap.put(transactionMasterBO.getFolioNo().trim(),
							feedData.get(FinexaBOColumnConstant.TRANSACTION_INVESTOR_NAME).toString().trim());
				}
			}
			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE)
					.startsWith("Additional Purchase")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository
						.findOne("Additional Purchase");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("ADDPURR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("ADDPURR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("ADDPUR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("ADDPUR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE)
					.startsWith("All others")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("All others");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("DIR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DIR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE)
					.startsWith("Dividend Reinvestment")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository
						.findOne("Dividend Reinvestment");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("DP")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DP");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("DR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("J")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("J");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE)
					.startsWith("New Purchase")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("New Purchase");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("NEWPURR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("NEWPURR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("NEWPUR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("NEWPUR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("P")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("P");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE)
					.startsWith("Redemption")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("Redemption");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("REDR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("REDR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("RED")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("RED");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("R")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("R");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("SIPR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SIPR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("SIP")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SIP");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("SI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("SO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("STP In")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STP In");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("STP Out")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STP Out");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("STP O")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STP O");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("STPO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STP O");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("STPIR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STPIR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("STPI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STPI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("STPOR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STPOR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("SWD")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWD");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("SWINR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWINR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("SWIN")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWIN");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("SWOFR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWOFR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("SWOF")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWOF");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("TI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("TO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("TMI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TMI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("TMO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TMO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE).startsWith("STPO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STPO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else {
				log.debug("A NEW TRANSACTION TYPE CODE FOUND: "
						+ feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_TYPE_CODE));
			}

			// transactionMasterBO.setMicrNo(feedData.get("micr_no"));
			// transactionMasterBO.setMicrRemarks(feedData.get("remarks"));
			// transactionMasterBO.setMultBrokerOption(feedData.get("mult_brok"));
			transactionMasterBO.setNav(feedData.get(FinexaBOColumnConstant.TRANSACTION_NAV));
			// transactionMasterBO.setOldFolioNo(feedData.get("old_folio"));
			transactionMasterBO.setProcessDate(formatUtilDateToSQLDate(
					dateFormatter(feedData.get(FinexaBOColumnConstant.TRANSACTION_PROCESS_DATE))));
			// transactionMasterBO.setReinvestFlag(feedData.get("reinvest_flag"));
			transactionMasterBO.setReportDate(formatUtilDateToSQLDate(
					dateFormatter(feedData.get(FinexaBOColumnConstant.TRANSACTION_REPORT_DATE))));
			transactionMasterBO.setReportTime(feedData.get(FinexaBOColumnConstant.TRANSACTION_REPORT_TIME));
			// transactionMasterBO.setReversalCode(feedData.get("reversal_code"));
			// transactionMasterBO.setScanRefNo(feedData.get("scanrefno"));
			transactionMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.TRANSACTION_SCHEME_NAME));
			transactionMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.TRANSACTION_SCHEME_RTA_CODE));
			// transactionMasterBO.setSchemeType(feedData.get("scheme_type"));
			// transactionMasterBO.setSequenceNo(feedData.get("seq_no"));
			// transactionMasterBO.setSgstAmt(feedData.get("sgst_amount"));
			// transactionMasterBO.setSipTransNo(feedData.get("siptrxnno"));
			// transactionMasterBO.setSourceBrokerCode(feedData.get("src_brk_code"));
			// transactionMasterBO.setSourceCode(feedData.get("usercode"));
			// transactionMasterBO.setSourceSerialNumber(feedData.get("usrtrxno"));
			transactionMasterBO.setSubBrokerARN(feedData.get(FinexaBOColumnConstant.TRANSACTION_SUB_BROKER_ARN));
			transactionMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_SUB_BROKER_CODE));
			// transactionMasterBO.setSwFlag(feedData.get("swflag"));
			// transactionMasterBO.setSysRegDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("sys_regn_date"))));
			// transactionMasterBO.setTargetSrcScheme(feedData.get("targ_src_scheme"));
			// transactionMasterBO.setTaxStatus(feedData.get("tax_status"));
			// transactionMasterBO.setTdsAmt(feedData.get("total_tax"));
			// transactionMasterBO.setTicobPostedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("ticob_posted_date"))));
			// transactionMasterBO.setTicobTransNo(feedData.get("ticob_trno"));
			// transactionMasterBO.setTicobTransType(feedData.get("ticob_trtype"));
			transactionMasterBO.setTransactionDate(formatUtilDateToSQLDate(
					dateFormatter(feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_DATE))));
			transactionMasterBO.setTransactionMode(feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_MODE));
			transactionMasterBO.setTransactionDescription(feedData.get(FinexaBOColumnConstant.TRANSACTION_DESCRIPTION));
			transactionMasterBO.setTransactionDescriptionCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DESCRIPTION_CODE));
			/*
			 * double transactionNumberDouble = Double
			 * .parseDouble(feedData.get(FinexaBOColumnConstant.
			 * TRANSACTION_TRANSACTION_NUMBER));
			 */
			/*
			 * if(duplicateTransNumber.contains(transactionNumberDouble))
			 * fw.write(transactionNumberDouble + ", "); else
			 * duplicateTransNumber.add(transactionNumberDouble);
			 */
			// String transactionNumberAsString = String.format("%.0f",
			// transactionNumberDouble);
			transactionMasterBO.setTransactionNumber(
					formatNumbers(String.valueOf(feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_NUMBER))));
			transactionMasterBO
					.setTransactionStatus(feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_STATUS));
			transactionMasterBO
					.setTransactionTax(feedData.get(FinexaBOColumnConstant.TRANSACTION_SECURITY_TRANSACTION_TAX));
			/*
			 * transactionMasterBO.setTransAmt(formatAmount(
			 * feedData.get(FinexaBOColumnConstant.TRANSACTION_AMOUNT_OF_THE_TRANSACTION)));
			 */
			transactionMasterBO.setTransCharges(feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_CHARGES));
			// transactionMasterBO.setTransSrc(feedData.get("src_of_txn"));
			transactionMasterBO.setTransSuffix(feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_SUFFIX));
			transactionMasterBO.setTransType(feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANS_TYPE));

			/*********************************************
			 * For New Requirement
			 *****************************/
			/*
			 * if(feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANS_TYPE).equals("R") ||
			 * feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANS_TYPE).equals(
			 * "Redemption") ||
			 * feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANS_TYPE).equals("RED")) {
			 */
			if(transactionNumberAndMasterBOMap.size() > 0) {
				if(transactionMasterBO.getTransactionNumber() != null) {
					if(transactionNumberAndMasterBOMap.containsKey(transactionMasterBO.getTransactionNumber().trim())) {
						if(transactionMasterBO.getTransactionDate().equals(transactionNumberAndMasterBOMap.get(transactionMasterBO.getTransactionNumber().trim()).getTransactionDate())) {
							
						}
					}
				}
				
					
			}
			if (String.valueOf(rta).equals(FinexaBOColumnConstant.RTA_FILE_TYPE_KARVY)) {
				// if(feedData.get(FinexaBOColumnConstant.TRANSACTION_TRANSACTION_MODE).equals("R"))
				// {

				transactionMasterBO.setTransUnits(
						feedData.get(FinexaBOColumnConstant.TRANSACTION_UNITS_OF_THE_TRANSACTION).replaceAll("-", ""));
				transactionMasterBO.setTransAmt(formatAmount(feedData
						.get(FinexaBOColumnConstant.TRANSACTION_AMOUNT_OF_THE_TRANSACTION).replaceAll("-", "")));
				// }
			} else {
				transactionMasterBO
						.setTransUnits(feedData.get(FinexaBOColumnConstant.TRANSACTION_UNITS_OF_THE_TRANSACTION));
				transactionMasterBO.setTransAmt(
						formatAmount(feedData.get(FinexaBOColumnConstant.TRANSACTION_AMOUNT_OF_THE_TRANSACTION)));
			}

			/*******************************************************************************************************/
			/*
			 * transactionMasterBO.setTransUnits(
			 * feedData.get(FinexaBOColumnConstant.TRANSACTION_UNITS_OF_THE_TRANSACTION));
			 */
			transactionMasterBO.setTxnType(feedData.get(FinexaBOColumnConstant.TRANSACTION_TXN_TYPE));

			if (transactionMasterBODTO.getNameRTA() == FinexaBOColumnConstant.RTA_TYPE_FRANKLIN)
				transactionMasterBO.setAmcCode(FinexaBOColumnConstant.AMC_FRANKLIN);
			else if (transactionMasterBODTO.getNameRTA() == FinexaBOColumnConstant.RTA_TYPE_SUNDARAM)
				transactionMasterBO.setAmcCode(FinexaBOColumnConstant.AMC_SUNDARAM);
			else
				transactionMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_AMC_CODE));

			transactionMasterBO.setLookupRtabo(lookupRTABOMap.get(transactionMasterBODTO.getNameRTA()));
			transactionMasterBO.setAdvisorUser(advisorUser);

			transactionMasterBOList.add(transactionMasterBO);
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return transactionMasterBOList;

	}

	private UploadResponseDTO readDBFFeed(TransactionMasterBODTO transactionMasterBODTO,
			UploadResponseDTO uploadResponseDTO) throws IOException, InvalidFormatException, ParseException {
		try {
			InputStream initialStream = transactionMasterBODTO.getNameSelectFile()[0].getInputStream();
			InputStream initialStream2 = transactionMasterBODTO.getNameSelectFile()[0].getInputStream();
			dbfReader = new DBFReader(initialStream);
			transactionNumberAndMasterBOMap = new HashMap<>();
			boolean headerFound = false, firstRow, isUniqueColumnNotFound = false;
			int numberOfFields;
			String column;
			Object[] rowObjects1, rowObjects2;
			int batchCount = 0, counter = 0, headerRowCount = 0, row, numberOfRecords = 0, numberOfRecordRunning = 0;
			List<TransactionMasterBO> transactionMasterBOList = new ArrayList<TransactionMasterBO>();
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
					.getIDAndDatabaseColumnNames(FinexaBOColumnConstant.FEED_TYPE_TRM);

			// Fetching the the database column IDs and feed column names list based on the
			// type of feed file
			feedColumnNameList = masterFeedColumnNamesBORepository
					.getIDAndFeedColumnNames(FinexaBOColumnConstant.FEED_TYPE_TRM);

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
			 * rowObjects1 = dbfReader.nextRecord(); firstRow = false; while
			 * (dbfReader.nextRecord() != null) { try { headerRowCount++; if (firstRow ==
			 * false) { for (int i = 0; i < numberOfFields; i++) { DBFField field =
			 * dbfReader.getField(i); String fileColName = field.getName(); for (String
			 * primaryKey : primaryKeyList) { if (fileColName.equalsIgnoreCase(primaryKey))
			 * { headerFound = true; break; } } if (headerFound == true) break; }
			 * 
			 * firstRow = true; if (headerFound == true) break; } else {
			 * 
			 * for (int i = 0; i < numberOfFields; i++) { DBFField field =
			 * dbfReader.getField(i); String fileColName = field.getName(); for (String
			 * primaryKey : primaryKeyList) { if (fileColName.equalsIgnoreCase(primaryKey))
			 * { headerFound = true; break; } } if (headerFound == true) break; }
			 * 
			 * rowObjects1 = dbfReader.nextRecord(); } if (headerFound == true) break; }
			 * catch (Exception e) { e.printStackTrace(); }
			 * 
			 * }
			 */
			int numberOfRecordRunning1 = 0;
			// rowObjects1 = dbfReader.nextRecord();
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
							// DBFField field1 = dbfReader.getField(i);
							String fileColName1 = String.valueOf(rowObjects1[i]);
							System.out.print(fileColName1 + ",");
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
								column = columnName;

								// Storing the database column ID as a key and corresponding feed column name as
								// the value.
								databaseColumnIDAndExcelFieldIDMap.put(dbColumnID,
										feedMap.get(column.toLowerCase().trim()));

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

			for (String dbCOlumnID : FinexaBOColumnConstant.TRANSACTION_DB_COLUMN_ID_LIST) {
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

			AdvisorUser advisorUser = advisorUserRepository.findOne(transactionMasterBODTO.getAdvisorID());

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
					// System.out.println(databaseColumnIDAndDataMap);
					if (batchCount == 100) {
						counter++;
						log.debug("transactionMasterBOList size() when COUNTER is:" + counter + " batchCount is: "
								+ transactionMasterBOList.size());
						uploadResponseDTO = saveModelData(transactionMasterBOList, uploadResponseDTO);
						transactionMasterBOList = new ArrayList<TransactionMasterBO>();
						batchCount = 0;

						// System.out.println("*********************************");

						transactionMasterBOList = populateModelList(databaseColumnIDAndDataMap, transactionMasterBODTO,
								transactionMasterBOList, advisorUser);

						batchCount++;

					} else {

						transactionMasterBOList = populateModelList(databaseColumnIDAndDataMap, transactionMasterBODTO,
								transactionMasterBOList, advisorUser);

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
				log.debug("transactionMasterBOList size() before LAST: " + transactionMasterBOList.size());
				uploadResponseDTO = saveModelData(transactionMasterBOList, uploadResponseDTO);
			}
			// System.out.println(dataMap);

			uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uploadResponseDTO;

	}

	private synchronized UploadResponseDTO saveModelData(List<TransactionMasterBO> transactionMasterBOList,
			UploadResponseDTO uploadResponseDTO) {
		// TODO Auto-generated method stub
		// System.out.println("Uploading transaction");
		try {
			if (transactionMasterBOList != null && transactionMasterBOList.size() > 0) {
				transactionMasterBORepository.save(transactionMasterBOList);
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
				} else if (feedDate.contains("-")) {
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
					date = sdf1.parse(feedDate);

					return date;
				} else if (feedDate.contains("/")) {
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
