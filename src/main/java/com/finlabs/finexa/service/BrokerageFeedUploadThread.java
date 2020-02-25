package com.finlabs.finexa.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.AumMasterBODTO;
import com.finlabs.finexa.dto.BrokerageMasterBODTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AumMasterBO;
import com.finlabs.finexa.model.AumMasterBOPK;
import com.finlabs.finexa.model.BackOfficeUploadHistory;
import com.finlabs.finexa.model.BrokerageMasterBO;
import com.finlabs.finexa.model.BrokerageMasterBOPK;
import com.finlabs.finexa.model.InvestorMasterBO;
import com.finlabs.finexa.model.LookupRTABO;
import com.finlabs.finexa.model.LookupRTAMasterFileDetailsBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.BackOfficeUploadHistoryRepository;
import com.finlabs.finexa.repository.BrokerageMasterBORepository;
import com.finlabs.finexa.repository.LookupRTABORepository;
import com.finlabs.finexa.repository.LookupRTAMasterFileDetailsBORepository;
import com.finlabs.finexa.repository.LookupTransactionRuleRepository;
import com.finlabs.finexa.util.FinexaBOColumnConstant;
import com.finlabs.finexa.util.FinexaUtil;
import com.ibm.icu.text.DecimalFormat;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

@Component

@Scope("prototype")

public class BrokerageFeedUploadThread implements Runnable {

	@Autowired
	private Mapper mapper;

	@Autowired
	private LookupRTABORepository lookupRTABORepository;

	@Autowired
	private LookupRTAMasterFileDetailsBORepository lookupRTAMasterFileDetailsBORepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private BrokerageMasterBORepository brokerageMasterBORepository;

	@Autowired
	private BackOfficeUploadHistoryRepository uploadHistoryRepo;

	private static final String RTA_FILE_TYPE_CAMS = "1";
	private static final String RTA_FILE_TYPE_KARVY = "2";
	private static final String RTA_FILE_TYPE_FRANKLIN = "3";
	private static final String RTA_FILE_TYPE_SUNDARAM = "4";
	private DecimalFormat decimalFormat = new DecimalFormat("#");
	private Properties prop = new Properties();
	private String rtaId;
	private Integer rta;
	private String FEED_PROPERTIES_FILE;
	private int rows = 1, columnHeaderRow = 0, noOfRejectedRecords = 0;
	private BrokerageMasterBODTO brokerageMasterBODTO;
	private UploadResponseDTO uploadResponseDTO;
	private BackOfficeUploadHistory backOfficeUploadHistory;
	private DBFReader dbfReader;
	private String rejectedRowNumbers = "";
	private Integer rejectedRowNumber = 0;
	public static final String STATUS_COMPLETED = "COMPLETED";
	public static final String STATUS_REJECTED = "REJECTED";
	public static final String RECORD_REJECTION_MESSAGE = "Transaction Date or Process Date or Transaction Number or all of the three fields were missing in each record. Row numbers are: ";
	public static final String NOT_REJECTED_MESSAGE = "All records are successfully uploaded.";
	public static final String FILE_REJECTION_MESSAGE = "One or more columns were not found in file.";
	public static final String STATUS_NOT_APPLICABLE = "NOT APPLICABLE";
	public static final String FILE_NOT_PROCESSED_MESSAGE = "Could not process the file.";
	// private List<Object[]> transNumTransDateProcessDateList = new
	// ArrayList<Object[]>();
	// private Map<String, BrokerageMasterBO> transNumTransDateProcessDateMap = new
	// HashMap<String, BrokerageMasterBO> ();
	private boolean allRowsInserted = false;

	public void initialize(BrokerageMasterBODTO brokerageMasterBODTO, UploadResponseDTO uploadResponseDTO,
			BackOfficeUploadHistory backOfficeUploadHistory) {
		this.brokerageMasterBODTO = brokerageMasterBODTO;
		this.uploadResponseDTO = uploadResponseDTO;
		this.backOfficeUploadHistory = backOfficeUploadHistory;
	}

	@Override
	public void run() {
		try {
			// log.debug("InvestorMasterBOServiceImpl >> uploadInvestorCAMSFeed");
			//
			rta = brokerageMasterBODTO.getNameRTA();
			rtaId = rta.toString();
			String name = brokerageMasterBODTO.getNameSelectFile()[0].getOriginalFilename();
			if (name.contains(".dbf") || name.contains(".DBF")) {
				uploadResponseDTO = readDBFFeed(brokerageMasterBODTO, uploadResponseDTO);
			} else {
				uploadResponseDTO = readBrokerageExcelFeed(brokerageMasterBODTO, uploadResponseDTO);
			}
			if (uploadResponseDTO.isStatus()) {
				backOfficeUploadHistory.setStatus(STATUS_COMPLETED);
				if (noOfRejectedRecords > 0) {
					
					if(rejectedRowNumbers.length()<=255) {
						
						
						rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
						backOfficeUploadHistory.setReasonOfRejection(RECORD_REJECTION_MESSAGE+rejectedRowNumbers);
						
						}else {
							rejectedRowNumbers = rejectedRowNumbers.substring(0,rejectedRowNumbers.indexOf(','))+rejectedRowNumber.toString()+",";
						
							backOfficeUploadHistory.setReasonOfRejection(RECORD_REJECTION_MESSAGE+rejectedRowNumbers+"Reason for rest rows are same as this.");
						}
					
					backOfficeUploadHistory.setAutoClientCreationStatus(STATUS_NOT_APPLICABLE);
				}
				else {
					backOfficeUploadHistory.setReasonOfRejection(NOT_REJECTED_MESSAGE);
					backOfficeUploadHistory.setAutoClientCreationStatus(STATUS_NOT_APPLICABLE);
				}
				
			} else {
				backOfficeUploadHistory.setStatus(STATUS_REJECTED);
				backOfficeUploadHistory.setReasonOfRejection(FILE_REJECTION_MESSAGE);
				backOfficeUploadHistory.setAutoClientCreationStatus(STATUS_NOT_APPLICABLE);
			}
			backOfficeUploadHistory.setRejectedRecords(uploadResponseDTO.getRejectedRecords());
			backOfficeUploadHistory.setEndTime(new java.util.Date());
			backOfficeUploadHistory = uploadHistoryRepo.save(backOfficeUploadHistory);

			if (backOfficeUploadHistory == null) {
				uploadResponseDTO.setMessage("Failed to Complete the Operation");
				uploadResponseDTO.setStatus(false);
			} else {
				if (uploadResponseDTO.isStatus()) {
					uploadResponseDTO.setMessage("File Upload Successfull");
				} else {
					uploadResponseDTO.setMessage("File Upload Failed");
				}
			}
			System.out.println("Brokerage Upload Finished");
		} catch (RuntimeException | InvalidFormatException | IOException | ParseException e) {
			if (allRowsInserted == true) {
				backOfficeUploadHistory.setStatus(STATUS_COMPLETED);
				backOfficeUploadHistory.setRejectedRecords(uploadResponseDTO.getRejectedRecords());
				backOfficeUploadHistory.setEndTime(new java.util.Date());
				backOfficeUploadHistory.setAutoClientCreationStatus(STATUS_NOT_APPLICABLE);
				backOfficeUploadHistory.setReasonOfRejection(RECORD_REJECTION_MESSAGE+rejectedRowNumbers);
				backOfficeUploadHistory = uploadHistoryRepo.save(backOfficeUploadHistory);
			} else {
				backOfficeUploadHistory.setStatus(STATUS_REJECTED);
				backOfficeUploadHistory.setRejectedRecords(0);
				backOfficeUploadHistory.setEndTime(new java.util.Date());
				backOfficeUploadHistory.setAutoClientCreationStatus(STATUS_NOT_APPLICABLE);
				backOfficeUploadHistory.setReasonOfRejection(FILE_NOT_PROCESSED_MESSAGE);
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
	private UploadResponseDTO readBrokerageExcelFeed(BrokerageMasterBODTO brokerageMasterBODTO,
			UploadResponseDTO uploadResponseDTO) throws IOException, InvalidFormatException, ParseException {
		try {

			// null pointer handling

			InputStream initialStream = brokerageMasterBODTO.getNameSelectFile()[0].getInputStream();

			String uuid = UUID.randomUUID().toString();
			System.out.println(uuid);

			File directory = new File(System.getProperty("java.io.tmpdir"));
			File targetFile = File.createTempFile("WBR6_" + uuid, ".tmp", directory);

			java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

			IOUtils.closeQuietly(initialStream);

			Workbook workbook = WorkbookFactory.create(targetFile);

			// Getting the Sheet at index zero
			Sheet sheet = workbook.getSheetAt(0);

			Map<String, String> feed = new HashMap<>();

			rows = sheet.getLastRowNum();

			// Iterating over Rows and Columns using Iterator
			Iterator<Row> rowIterator = sheet.rowIterator();

			switch (rtaId) {

			case RTA_FILE_TYPE_CAMS:
				FEED_PROPERTIES_FILE = "backOfficeProperties/brokerageCAMS.properties";
				break;

			case RTA_FILE_TYPE_KARVY:
				FEED_PROPERTIES_FILE = "backOfficeProperties/brokerageKarvy.properties";
				break;
			case RTA_FILE_TYPE_FRANKLIN:
				FEED_PROPERTIES_FILE = "backOfficeProperties/brokerageFranklin.properties";
				;
				break;
			/*
			 * case RTA_FILE_TYPE_SUNDARAM : FEED_PROPERTIES_FILE =
			 * "backOfficeProperties/aumSundaram.properties"; break;
			 */
			default:
				break;
			}

			ClassLoader classLoader = getClass().getClassLoader();
			FileReader fileReader = new FileReader(classLoader.getResource(FEED_PROPERTIES_FILE).getFile());
			prop.load(fileReader);

			// To get the actual column header
			columnHeaderRow = getColumnHeaderRowNum(sheet);

			Map<String, String> excelNameColNameMap = new HashMap<String, String>();

			for (String columnHeader : prop.stringPropertyNames()) {
				String value = prop.getProperty(columnHeader);
				String columnAddress = null;
				CellAddress addressOfCell = null;

				// Create a DataFormatter to format and get each cell's value as String
				DataFormatter dataFormatter = new DataFormatter();

				Iterator<Row> rowIterator1 = sheet.rowIterator();

				while (rowIterator1.hasNext()) {
					Row row1 = rowIterator1.next();

					// To reach the actual column header
					if (row1.getRowNum() != columnHeaderRow)
						continue;

					// To iterate over the columns of the current row
					Iterator<Cell> cellIterator1 = row1.cellIterator();

					while (cellIterator1.hasNext()) {
						Cell cell1 = cellIterator1.next();
						String cellValue1 = dataFormatter.formatCellValue(cell1);
						if (cellValue1.equalsIgnoreCase(value)) {
							addressOfCell = cell1.getAddress();
							break;
						}
					}

					if (("" + addressOfCell).length() < 3) {
						columnAddress = ("" + addressOfCell).substring(0, 1);
					} else
						columnAddress = ("" + addressOfCell).substring(0, 2);

					excelNameColNameMap.put(value, columnAddress);

				}

			}

			List<BrokerageMasterBO> brokerageMasterBOList = new ArrayList<BrokerageMasterBO>();

			// Empty the list each time after inserting 20 records
			int batchCount = 0;

			AdvisorUser advisorUser = advisorUserRepository.findOne(backOfficeUploadHistory.getAdvisorUser().getId());

			while (rowIterator.hasNext()) {

				// This row variable is used to iterate through each row of excel sheet
				Row row = rowIterator.next();
				rejectedRowNumber = row.getRowNum();
				// To reach the column header row
				if (row.getRowNum() < columnHeaderRow + 1) {
					continue;
				}

				// Row number is greater than column header row number
				try {
					for (String key : prop.stringPropertyNames()) {
						String propertyKey = prop.getProperty(key);
						String columnAddress = excelNameColNameMap.get(propertyKey);
						String dataOfCell = "";
						if (row.getCell(
								org.apache.poi.ss.util.CellReference.convertColStringToIndex(columnAddress)) != null) {
							Cell cell = row.getCell(
									org.apache.poi.ss.util.CellReference.convertColStringToIndex(columnAddress));
							dataOfCell = cell.toString();
						}
						feed.put(propertyKey, dataOfCell);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (batchCount == 20) {
					System.out.println("LIST SIZE: " + brokerageMasterBOList.size());
					uploadResponseDTO = saveBrokerageData(brokerageMasterBOList, uploadResponseDTO);
					brokerageMasterBOList = new ArrayList<BrokerageMasterBO>();
					batchCount = 0;

					switch (rtaId) {

					case RTA_FILE_TYPE_CAMS:
						brokerageMasterBOList = populateBrokerageCAMSModelList(feed, brokerageMasterBODTO,
								brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser());
						break;

					case RTA_FILE_TYPE_KARVY:
						brokerageMasterBOList = populateBrokerageKarvyModelList(feed, brokerageMasterBODTO,
								brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser());
						break;
					case RTA_FILE_TYPE_FRANKLIN:
						brokerageMasterBOList = populateBrokerageFranklinModelList(feed, brokerageMasterBODTO,
								brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser());
						break;
					/*
					 * case RTA_FILE_TYPE_SUNDARAM : investorMasterBOList =
					 * populateInvestorCAMSModelList(feed, investorMasterBODTO,investorMasterBOList,
					 * advisorUser); break;
					 */
					default:
						break;
					}

					batchCount++;

				} else {

					switch (rtaId) {

					case RTA_FILE_TYPE_CAMS:
						brokerageMasterBOList = populateBrokerageCAMSModelList(feed, brokerageMasterBODTO,
								brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser());
						break;

					case RTA_FILE_TYPE_KARVY:
						brokerageMasterBOList = populateBrokerageKarvyModelList(feed, brokerageMasterBODTO,
								brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser());
						break;
					case RTA_FILE_TYPE_FRANKLIN:
						brokerageMasterBOList = populateBrokerageFranklinModelList(feed, brokerageMasterBODTO,
								brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser());
						break;
					/*
					 * case RTA_FILE_TYPE_SUNDARAM : investorMasterBOList =
					 * populateInvestorCAMSModelList(feed, investorMasterBODTO,investorMasterBOList,
					 * advisorUser); break;
					 */
					default:
						break;
					}

					batchCount++;
				}

			}

			if (batchCount > 0) {
				System.out.println("LIST SIZE AT LAST: " + brokerageMasterBOList.size());
				uploadResponseDTO = saveBrokerageData(brokerageMasterBOList, uploadResponseDTO);
				allRowsInserted = true;

			}

			targetFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uploadResponseDTO;

	}

	private List<BrokerageMasterBO> populateBrokerageCAMSModelList(Map<String, String> feedData,
			BrokerageMasterBODTO brokerageMasterBODTO, List<BrokerageMasterBO> brokerageMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO, BrokerageMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;
			BrokerageMasterBOPK brokerageMasterBOPK = new BrokerageMasterBOPK();
			brokerageMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_SCHEME_RTA_CODE));
			// brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
			brokerageMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_AMC_CODE));
			brokerageMasterBO.setFolioNumber(
					formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_FOLIO_NUMBER)).toString());
			brokerageMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_INVESTOR_NAME));
			brokerageMasterBO
					.setTransactionType(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_TRANSACTION_TYPE));
			brokerageMasterBO.setFromDate(finexaUtil.formatDate(
					finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_FROM_DATE))));
			brokerageMasterBO.setToDate(finexaUtil.formatDate(
					finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_TO_DATE))));
			brokerageMasterBOPK.setTransactionDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_TRANSACTION_DATE))));
			brokerageMasterBOPK.setProcessDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_PROCESS_DATE))));
			// brokerageMasterBO.setAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_BROKERAGE_AMOUNT));
			brokerageMasterBO.setBrokeragePercentage(
					feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_BROKERAGE_PERCENTAGE));
			brokerageMasterBO.setBrokerageAmount(
					formatAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_BROKERAGE_AMOUNT))
							.toString());
			brokerageMasterBO
					.setBrokerageType(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_BROKERAGE_TYPE));
			brokerageMasterBOPK.setTransactionNumber(
					formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_TRANSACTION_NUMBER))
							.toString());

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");
			brokerageMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(backOfficeUploadHistory.getAdvisorUser().getId());
			brokerageMasterBO.setAdvisorUser(advUser);
			brokerageMasterBO.setId(brokerageMasterBOPK);

			if (feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_TRANSACTION_NUMBER) != null
					&& !feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_TRANSACTION_NUMBER).isEmpty()) {
				if (feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_TRANSACTION_DATE) != null
						&& !feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_TRANSACTION_DATE).isEmpty()) {
					if (feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_PROCESS_DATE) != null
							&& !feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_CAMS_PROCESS_DATE).isEmpty()) {
						brokerageMasterBOList.add(brokerageMasterBO);
						isRecordValid = true;
					}
				}
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return brokerageMasterBOList;

	}

	private List<BrokerageMasterBO> populateBrokerageKarvyModelList(Map<String, String> feedData,
			BrokerageMasterBODTO brokerageMasterBODTO, List<BrokerageMasterBO> brokerageMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			// BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO,
			// BrokerageMasterBO.class);
			BrokerageMasterBO brokerageMasterBO = new BrokerageMasterBO();
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;
			BrokerageMasterBOPK brokerageMasterBOPK = new BrokerageMasterBOPK();
			
			  brokerageMasterBO .setSchemeRTACode(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_SCHEME_RTA_CODE));
			  brokerageMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_SCHEME_NAME));
			  brokerageMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_AMC_CODE));
			  brokerageMasterBO.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_FOLIO_NUMBER)).toString());
			  brokerageMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_INVESTOR_NAME)); 
			  brokerageMasterBO.setTransactionType(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_TRANSACTION_TYPE));
			  brokerageMasterBO.setFromDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_FROM_DATE))));
			  brokerageMasterBO.setToDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_TO_DATE))));
			 
			  brokerageMasterBOPK.setTransactionDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_TRANSACTION_DATE))));
			  brokerageMasterBOPK.setProcessDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_PROCESS_DATE))));
			
			  brokerageMasterBO.setAmount(formatAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_AMOUNT)).toString());
			  brokerageMasterBO.setBrokeragePercentage(formatAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_BROKERAGE_PERCENTAGE)).toString());
			  brokerageMasterBO.setBrokerageAmount(formatAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_BROKERAGE_AMOUNT)).toString()); 
			  brokerageMasterBO.setBrokerageType(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_BROKERAGE_TYPE));
			 
			  brokerageMasterBOPK.setTransactionNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_TRANSACTION_NUMBER)).toString());

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			brokerageMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(backOfficeUploadHistory.getAdvisorUser().getId());
			brokerageMasterBO.setAdvisorUser(advUser);
			brokerageMasterBO.setId(brokerageMasterBOPK);

			if (feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_TRANSACTION_NUMBER) != null
					&& !feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_TRANSACTION_NUMBER).isEmpty()) {
				if (feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_TRANSACTION_DATE) != null
						&& !feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_TRANSACTION_DATE).isEmpty()) {
					if (feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_PROCESS_DATE) != null
							&& !feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_KARVY_PROCESS_DATE).isEmpty()) {
						brokerageMasterBOList.add(brokerageMasterBO);
						isRecordValid = true;
					}
				}
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return brokerageMasterBOList;

	}

	private List<BrokerageMasterBO> populateBrokerageFranklinModelList(Map<String, String> feedData,
			BrokerageMasterBODTO brokerageMasterBODTO, List<BrokerageMasterBO> brokerageMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO, BrokerageMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;
			BrokerageMasterBOPK brokerageMasterBOPK = new BrokerageMasterBOPK();

			brokerageMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_SCHEME_RTA_CODE));
			// brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
			// brokerageMasterBO.setAmcCode(feedData.get("Fund"));
			brokerageMasterBO.setFolioNumber(
					formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_FOLIO_NUMBER))
							.toString());
			brokerageMasterBO
					.setInvestorName(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_INVESTOR_NAME));
			brokerageMasterBO
					.setTransactionType(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_TRANSACTION_TYPE));
			brokerageMasterBO.setFromDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_FROM_DATE))));
			brokerageMasterBO.setToDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_TO_DATE))));
			brokerageMasterBOPK.setTransactionDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_TRANSACTION_DATE))));
			brokerageMasterBOPK.setProcessDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_PROCESS_DATE))));
			brokerageMasterBO.setAmount(
					formatAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_AMOUNT)).toString());
			brokerageMasterBO.setBrokeragePercentage(
					formatAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_BROKERAGE_PERCENTAGE))
							.toString());
			brokerageMasterBO.setBrokerageAmount(
					formatAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_BROKERAGE_AMOUNT))
							.toString());
			brokerageMasterBO
					.setBrokerageType(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_BROKERAGE_TYPE));
			brokerageMasterBOPK.setTransactionNumber(
					formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_TRANSACTION_NUMBER))
							.toString());

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			brokerageMasterBO.setLookupRtabo(lookupRtabo);
			// AdvisorUser advUser =
			// advisorUserRepository.findOne(backOfficeUploadHistory.getAdvisorUser().getId());
			brokerageMasterBO.setAdvisorUser(backOfficeUploadHistory.getAdvisorUser());
			brokerageMasterBO.setId(brokerageMasterBOPK);

			if (feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_TRANSACTION_NUMBER) != null
					&& !feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_TRANSACTION_NUMBER).isEmpty()) {
				if (feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_TRANSACTION_DATE) != null
						&& !feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_TRANSACTION_DATE).isEmpty()) {
					if (feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_PROCESS_DATE) != null
							&& !feedData.get(FinexaBOColumnConstant.BROKERAGE_EXCEL_FRANKLIN_PROCESS_DATE).isEmpty()) {
						brokerageMasterBOList.add(brokerageMasterBO);
						isRecordValid = true;
					}
				}
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return brokerageMasterBOList;

	}

	private UploadResponseDTO readDBFFeed(BrokerageMasterBODTO brokerageMasterBODTO,
			UploadResponseDTO uploadResponseDTO) throws IOException, InvalidFormatException, ParseException {

		InputStream initialStream = brokerageMasterBODTO.getNameSelectFile()[0].getInputStream();
		InputStream initialStream2 = brokerageMasterBODTO.getNameSelectFile()[0].getInputStream();
		dbfReader = new DBFReader(initialStream);
		boolean headerFound = false, firstRow;
		int numberOfFields;
		String headerPropFile, columnPropFile;
		Properties prop1 = new Properties();
		Object[] rowObjects1, rowObjects2;
		int batchCount = 0, counter = 0, headerRowCount = 0, row, numberOfRecords = 0, numberOfRecordRunning = 0;
		List<BrokerageMasterBO> brokerageMasterBOList = new ArrayList<BrokerageMasterBO>();
		List<String> fileColNames = new ArrayList<String>();
		List<String> propColNames = new ArrayList<String>();
		Map<String, String> dataMap = new HashMap<>();
		Map<String, String> brokerageFeedMap = new HashMap<>();

		// Number of columns in the DBF file
		numberOfFields = dbfReader.getFieldCount();
		numberOfRecords = dbfReader.getRecordCount();
		headerPropFile = "backOfficeProperties/brokerageDBF.properties";
		ClassLoader classLoader1 = getClass().getClassLoader();
		FileReader fileReader1 = new FileReader(classLoader1.getResource(headerPropFile).getFile());
		prop1.load(fileReader1);
		rowObjects1 = dbfReader.nextRecord();
		firstRow = false;
		while (dbfReader.nextRecord() != null) {
			headerRowCount++;
			if (firstRow == false) {
				for (int i = 0; i < numberOfFields; i++) {
					DBFField field = dbfReader.getField(i);
					String fileColName = field.getName();
					String propColName = prop1.getProperty(rtaId);
					if (fileColName.equals(propColName)) {
						headerFound = true;
						break;
					}
				}

				firstRow = true;
			} else {

				for (int i = 0; i < numberOfFields; i++) {
					DBFField field = dbfReader.getField(i);
					String fileColName = field.getName();
					String propColName = String.valueOf(rowObjects1[i]).trim();
					if (fileColName.equals(propColName)) {
						headerFound = true;
						break;
					}
				}

				rowObjects1 = dbfReader.nextRecord();
			}
			if (headerFound == true)
				break;

		}

		for (Integer i = 0; i < numberOfFields; i++) {
			DBFField field = dbfReader.getField(i);
			String fileColname = field.getName();
			brokerageFeedMap.put(i.toString(), fileColname);
		}
		prop1 = new Properties();
		switch (rtaId) {

		case RTA_FILE_TYPE_CAMS:
			columnPropFile = "backOfficeProperties/brokerageCAMSDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case RTA_FILE_TYPE_KARVY:
			columnPropFile = "backOfficeProperties/brokerageKarvyDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case RTA_FILE_TYPE_FRANKLIN:
			columnPropFile = "backOfficeProperties/brokerageFranklinDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;
			
		case RTA_FILE_TYPE_SUNDARAM:
			columnPropFile = "backOfficeProperties/brokerageSundaramDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;
			
		default:
			break;

		}

		for (Map.Entry<String, String> entry : brokerageFeedMap.entrySet()) {
			fileColNames.add(entry.getValue());
		}

		for (String columnHeader : prop1.stringPropertyNames()) {
			propColNames.add(prop1.getProperty(columnHeader));
		}

		System.out.println(fileColNames.size() + " fileColNames: " + fileColNames);
		System.out.println(propColNames.size() + " propColNames: " + propColNames);

		fileColNames.retainAll(propColNames);
		System.out.println("After retainAll: " + fileColNames.size());

		if (fileColNames.size() != propColNames.size()) {

			propColNames.removeAll(fileColNames);
			uploadResponseDTO.setPrimaryKeyNotFound(true);
			System.out.println("DBF Column Address Null");
			System.out.println(propColNames + " NOT FOUND");
			uploadResponseDTO.setStatus(false);
			return uploadResponseDTO;

		}

		// AdvisorUser advisorUser =
		// advisorUserRepository.findOne(backOfficeUploadHistory.getAdvisorUser().getId());

		dbfReader = new DBFReader(initialStream2);
		// Starts reading after the header row
		row = 0;
		rowObjects2 = dbfReader.nextRecord();
		rejectedRowNumber = headerRowCount;
		while (rowObjects2 != null) {
			if (++row <= headerRowCount)
				continue;
			rejectedRowNumber++;
			numberOfRecordRunning++;

			for (int columnIndex = 0; columnIndex < numberOfFields; columnIndex++) {
				Integer fieldIndex = columnIndex;
				dataMap.put(brokerageFeedMap.get(fieldIndex.toString()),
						String.valueOf(rowObjects2[columnIndex]).trim());

			}

			if (batchCount == 20) {
				System.out
						.println("brokerageMasterBOList size() when batchCount is 20: " + brokerageMasterBOList.size());
				uploadResponseDTO = saveBrokerageData(brokerageMasterBOList, uploadResponseDTO);
				brokerageMasterBOList = new ArrayList<BrokerageMasterBO>();
				batchCount = 0;
				counter++;
				System.out.println("Counter displayed" + counter);
				System.out.println("*********************************");

				switch (rtaId) {
				
				case RTA_FILE_TYPE_CAMS: brokerageMasterBOList = populateDBFCAMSModelList(dataMap, brokerageMasterBODTO,
					brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser()); 
					break;
				 
				case RTA_FILE_TYPE_KARVY:
					brokerageMasterBOList = populateDBFKARVYModelList(dataMap, brokerageMasterBODTO,
							brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser());
					break;
				
				case RTA_FILE_TYPE_FRANKLIN: brokerageMasterBOList = populateDBFFRANKLINModelList(dataMap, brokerageMasterBODTO,
					brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser());
					break;
					
				case RTA_FILE_TYPE_SUNDARAM: brokerageMasterBOList = populateDBFSUNDARAMModelList(dataMap, brokerageMasterBODTO,
						brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser());
						break;	
				 
				default:
					break;

				}

				batchCount++;

			} else {

				switch (rtaId) {
				case RTA_FILE_TYPE_CAMS: brokerageMasterBOList = populateDBFCAMSModelList(dataMap, brokerageMasterBODTO,
						brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser()); 
						break;
					 
				case RTA_FILE_TYPE_KARVY:
					brokerageMasterBOList = populateDBFKARVYModelList(dataMap, brokerageMasterBODTO,
							brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser());
					break;
				
				  case RTA_FILE_TYPE_FRANKLIN: 
					  brokerageMasterBOList = populateDBFFRANKLINModelList(dataMap, brokerageMasterBODTO,
							brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser());
					  break;
					  
				  case RTA_FILE_TYPE_SUNDARAM: brokerageMasterBOList = populateDBFSUNDARAMModelList(dataMap, brokerageMasterBODTO,
							brokerageMasterBOList, backOfficeUploadHistory.getAdvisorUser());
							break;	
					 
				default:
					break;

				}

				batchCount++;
			}
			if(numberOfRecordRunning == numberOfRecords)
				break;
			rowObjects2 = dbfReader.nextRecord();

		}
		if (batchCount > 0) {
			System.out.println("brokerageMasterBOList size() before last: " + brokerageMasterBOList.size());
			uploadResponseDTO = saveBrokerageData(brokerageMasterBOList, uploadResponseDTO);
		}
		// System.out.println(dataMap);

		uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		return uploadResponseDTO;

	}

	
	private List<BrokerageMasterBO> populateDBFKARVYModelList(Map<String, String> feedData,
			BrokerageMasterBODTO brokerageMasterBODTO, List<BrokerageMasterBO> brokerageMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO, BrokerageMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;
			BrokerageMasterBOPK brokerageMasterBOPK = new BrokerageMasterBOPK();
			brokerageMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_SCHEME_RTA_CODE));
			brokerageMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_SCHEME_NAME));
			brokerageMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_AMC_CODE));
			brokerageMasterBO.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_FOLIO_NUMBER)));
			brokerageMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_INVESTOR_NAME));
			brokerageMasterBO
					.setTransactionType(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_TRANSACTION_TYPE));
			brokerageMasterBO.setFromDate(finexaUtil.formatDate(
					(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_FROM_DATE))));
			brokerageMasterBO.setToDate(finexaUtil.formatDate(
					(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_TO_DATE))));
			brokerageMasterBOPK.setTransactionDate(finexaUtil.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_TRANSACTION_DATE))));
			brokerageMasterBOPK.setProcessDate(finexaUtil.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_PROCESS_DATE))));
			brokerageMasterBO.setAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_AMOUNT));
			brokerageMasterBO.setBrokeragePercentage(
					feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_BROKERAGE_PERCENTAGE));
			brokerageMasterBO
					.setBrokerageAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_BROKERAGE_AMOUNT));
			brokerageMasterBO.setBrokerageType(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_BROKERAGE_TYPE));
			brokerageMasterBOPK
					.setTransactionNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_TRANSACTION_NUMBER)));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			brokerageMasterBO.setLookupRtabo(lookupRtabo);
			// AdvisorUser advUser =
			// advisorUserRepository.findOne(backOfficeUploadHistory.getAdvisorUser().getId());
			brokerageMasterBO.setAdvisorUser(backOfficeUploadHistory.getAdvisorUser());
			brokerageMasterBO.setId(brokerageMasterBOPK);
			/*
			java.sql.Date transDate = new java.sql.Date(finexaUtil
					.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_FROM_DATE)))
					.getTime());
			java.sql.Date procDate = new java.sql.Date(finexaUtil
					.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_PROCESS_DATE)))
					.getTime());
			
			String transctionDate = transDate.toString().trim();
			String processDate = procDate.toString().trim();
			String trnxNo = feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_TRANSACTION_NUMBER).trim();
			*/
			if (feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_FROM_DATE).trim() != null && !feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_FROM_DATE).trim().isEmpty()) {
				if (((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_PROCESS_DATE))).toString().trim() !=null && !((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_PROCESS_DATE))).toString().trim().isEmpty()) {
					if ((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_TRANSACTION_NUMBER).toString().trim() != null && !(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_TRANSACTION_NUMBER).toString().trim().isEmpty())))
							{
						brokerageMasterBOList.add(brokerageMasterBO);
						isRecordValid = true;
					}
				}
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return brokerageMasterBOList;

	}
	
	private List<BrokerageMasterBO> populateDBFCAMSModelList(Map<String, String> feedData,
			BrokerageMasterBODTO brokerageMasterBODTO, List<BrokerageMasterBO> brokerageMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO, BrokerageMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;
			BrokerageMasterBOPK brokerageMasterBOPK = new BrokerageMasterBOPK();
			brokerageMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_SCHEME_RTA_CODE));
			//brokerageMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_SCHEME_NAME));
			brokerageMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_AMC_CODE));
			brokerageMasterBO.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_FOLIO_NUMBER)));
			brokerageMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_INVESTOR_NAME));
			brokerageMasterBO
					.setTransactionType(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_TRANSACTION_TYPE));
			brokerageMasterBO.setFromDate(finexaUtil.formatDate(
					(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_FROM_DATE))));
			brokerageMasterBO.setToDate(finexaUtil.formatDate(
					(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_TO_DATE))));
			brokerageMasterBOPK.setTransactionDate(finexaUtil.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_TRANSACTION_DATE))));
			brokerageMasterBOPK.setProcessDate(finexaUtil.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_PROCESS_DATE))));
			brokerageMasterBO.setAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_AMOUNT));
			brokerageMasterBO.setBrokeragePercentage(
					feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_BROKERAGE_PERCENTAGE));
			brokerageMasterBO
					.setBrokerageAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_BROKERAGE_AMOUNT));
			brokerageMasterBO.setBrokerageType(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_BROKERAGE_TYPE));
			brokerageMasterBOPK
					.setTransactionNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_TRANSACTION_NUMBER)));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");
			brokerageMasterBO.setLookupRtabo(lookupRtabo);
			// AdvisorUser advUser =
			// advisorUserRepository.findOne(backOfficeUploadHistory.getAdvisorUser().getId());
			brokerageMasterBO.setAdvisorUser(backOfficeUploadHistory.getAdvisorUser());
			brokerageMasterBO.setId(brokerageMasterBOPK);
			/*
			java.sql.Date transDate = new java.sql.Date(finexaUtil
					.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_FROM_DATE)))
					.getTime());
			java.sql.Date procDate = new java.sql.Date(finexaUtil
					.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_PROCESS_DATE)))
					.getTime());
			
			String transctionDate = transDate.toString().trim();
			String processDate = procDate.toString().trim();
			String trnxNo = feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_TRANSACTION_NUMBER).trim();
			*/
			//System.out.println("BROKERAGE_DBF_CAMS_TRANSACTION_NUMBER"+feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_FROM_DATE));
			//if(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_FROM_DATE) != null && feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_PROCESS_DATE) != null && feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_TRANSACTION_NUMBER) != null) {
			
			if (feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_FROM_DATE).trim() != null && !feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_FROM_DATE).trim().isEmpty()) {
				if (((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_PROCESS_DATE))).toString().trim() !=null && !((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_PROCESS_DATE))).toString().trim().isEmpty()) {
					if ((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_TRANSACTION_NUMBER).toString().trim() != null && !(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_CAMS_TRANSACTION_NUMBER).toString().trim().isEmpty())))
							{
								brokerageMasterBOList.add(brokerageMasterBO);
								isRecordValid = true;
					}
				}
			//}
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return brokerageMasterBOList;

	}

	
	private List<BrokerageMasterBO> populateDBFFRANKLINModelList(Map<String, String> feedData,
			BrokerageMasterBODTO brokerageMasterBODTO, List<BrokerageMasterBO> brokerageMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO, BrokerageMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;
			BrokerageMasterBOPK brokerageMasterBOPK = new BrokerageMasterBOPK();
			brokerageMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_SCHEME_RTA_CODE));
		//	brokerageMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_SCHEME_NAME));
			//brokerageMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_AMC_CODE));
			brokerageMasterBO.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_FOLIO_NUMBER)));
			brokerageMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_INVESTOR_NAME));
			brokerageMasterBO
					.setTransactionType(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_TRANSACTION_TYPE));
			brokerageMasterBO.setFromDate(finexaUtil.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_FROM_DATE))));
			brokerageMasterBO.setToDate(finexaUtil.formatDate(
					(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_TO_DATE))));
			
			brokerageMasterBOPK.setTransactionDate(finexaUtil.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_TRANSACTION_DATE))));
			brokerageMasterBOPK.setProcessDate(finexaUtil.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_PROCESS_DATE))));
			brokerageMasterBO.setAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_AMOUNT));
			brokerageMasterBO.setBrokeragePercentage(
					feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_BROKERAGE_PERCENTAGE));
			brokerageMasterBO
					.setBrokerageAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_BROKERAGE_AMOUNT));
			brokerageMasterBO.setBrokerageType(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_BROKERAGE_TYPE));
			brokerageMasterBOPK
					.setTransactionNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_TRANSACTION_NUMBER)));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			brokerageMasterBO.setLookupRtabo(lookupRtabo);
			// AdvisorUser advUser =
			// advisorUserRepository.findOne(backOfficeUploadHistory.getAdvisorUser().getId());
			brokerageMasterBO.setAdvisorUser(backOfficeUploadHistory.getAdvisorUser());
			brokerageMasterBO.setId(brokerageMasterBOPK);
			/*
			java.sql.Date transDate = new java.sql.Date(finexaUtil
					.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_FROM_DATE)))
					.getTime());
			java.sql.Date procDate = new java.sql.Date(finexaUtil
					.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_PROCESS_DATE)))
					.getTime());
			
			String transctionDate = transDate.toString().trim();
			String processDate = procDate.toString().trim();
			String trnxNo = feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_TRANSACTION_NUMBER).trim();
			*/
			if (feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_FROM_DATE).trim() != null && !feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_FROM_DATE).trim().isEmpty()) {
				if (feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_TRANSACTION_NUMBER).trim() != null && !feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_TRANSACTION_NUMBER).trim().isEmpty()) {
					if ((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_PROCESS_DATE).toString().trim() != null && !(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_PROCESS_DATE).toString().trim().isEmpty()))) {
						brokerageMasterBOList.add(brokerageMasterBO);
						isRecordValid = true;
					}
				}
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return brokerageMasterBOList;

	}


	private List<BrokerageMasterBO> populateDBFSUNDARAMModelList(Map<String, String> feedData,
			BrokerageMasterBODTO brokerageMasterBODTO, List<BrokerageMasterBO> brokerageMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO, BrokerageMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;
			BrokerageMasterBOPK brokerageMasterBOPK = new BrokerageMasterBOPK();
			brokerageMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_SCHEME_RTA_CODE));
		//	brokerageMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_SCHEME_NAME));
			//brokerageMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_KARVY_AMC_CODE));
			brokerageMasterBO.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_FOLIO_NUMBER)));
			brokerageMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_INVESTOR_NAME));
			brokerageMasterBO
					.setTransactionType(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_TRANSACTION_TYPE));
			brokerageMasterBO.setFromDate(finexaUtil.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_FROM_DATE))));
			brokerageMasterBO.setToDate(finexaUtil.formatDate(
					(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_TO_DATE))));
			
			brokerageMasterBOPK.setTransactionDate(finexaUtil.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_TRANSACTION_DATE))));
			brokerageMasterBOPK.setProcessDate(finexaUtil.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_PROCESS_DATE))));
			brokerageMasterBO.setAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_AMOUNT));
			brokerageMasterBO.setBrokeragePercentage(
					feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_BROKERAGE_PERCENTAGE));
			brokerageMasterBO
					.setBrokerageAmount(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_BROKERAGE_AMOUNT));
			brokerageMasterBO.setBrokerageType(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_BROKERAGE_TYPE));
			brokerageMasterBOPK
					.setTransactionNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_TRANSACTION_NUMBER)));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Sundaram Mutual Fund");
			brokerageMasterBO.setLookupRtabo(lookupRtabo);
			// AdvisorUser advUser =
			// advisorUserRepository.findOne(backOfficeUploadHistory.getAdvisorUser().getId());
			brokerageMasterBO.setAdvisorUser(backOfficeUploadHistory.getAdvisorUser());
			brokerageMasterBO.setId(brokerageMasterBOPK);
			/*
			java.sql.Date transDate = new java.sql.Date(finexaUtil
					.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_FROM_DATE)))
					.getTime());
			java.sql.Date procDate = new java.sql.Date(finexaUtil
					.formatDate((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_PROCESS_DATE)))
					.getTime());
			
			String transctionDate = transDate.toString().trim();
			String processDate = procDate.toString().trim();
			String trnxNo = feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_FRANKLIN_TRANSACTION_NUMBER).trim();
			*/
			if (feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_FROM_DATE).trim() != null && !feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_FROM_DATE).trim().isEmpty()) {
				if (feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_TRANSACTION_NUMBER).trim() != null && !feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_TRANSACTION_NUMBER).trim().isEmpty()) {
					if ((feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_PROCESS_DATE).toString().trim() != null && !(feedData.get(FinexaBOColumnConstant.BROKERAGE_DBF_SUNDARAM_PROCESS_DATE).toString().trim().isEmpty()))) {
						brokerageMasterBOList.add(brokerageMasterBO);
						isRecordValid = true;
					}
				}
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return brokerageMasterBOList;

	}


	private synchronized UploadResponseDTO saveBrokerageData(List<BrokerageMasterBO> brokerageMasterBODTOList,
			UploadResponseDTO uploadResponseDTO) {
		// TODO Auto-generated method stub

		try {
			if (brokerageMasterBODTOList != null && brokerageMasterBODTOList.size() > 0) {
				brokerageMasterBORepository.save(brokerageMasterBODTOList);

			}

			uploadResponseDTO.setStatus(true);

		} catch (RuntimeException e) {
			uploadResponseDTO.setStatus(false);
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return uploadResponseDTO;

	}

	private int getColumnHeaderRowNum(Sheet sheet) {

		boolean found = false;
		String columnHeader = "transactionNumber";
		// for (String columnHeader : prop.stringPropertyNames()) {

		String value = prop.getProperty(columnHeader);
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
						if (cellValue1.equalsIgnoreCase(value)) {
							columnHeaderRow = row1.getRowNum();
							found = true;
							break;
						}
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
	}

	private String formatNumbers(String number) {
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
					
					Double doubleData = Double.parseDouble(number);

					BigDecimal bigDecimaldata = new BigDecimal(BigDecimal.valueOf(doubleData).toPlainString());

					number = bigDecimaldata.toString();

					return number;

				} else
					return number;
			}

		} else
			return null;
	}

	private String formatAmount(String number) {
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

	}
}
