package com.finlabs.finexa.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.finlabs.finexa.dto.TransactionMasterBODTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.BackOfficeUploadHistory;
import com.finlabs.finexa.model.LookupRTABO;
import com.finlabs.finexa.model.LookupRTAMasterFileDetailsBO;
import com.finlabs.finexa.model.LookupTransactionRule;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.BackOfficeUploadHistoryRepository;
import com.finlabs.finexa.repository.LookupRTABORepository;
import com.finlabs.finexa.repository.LookupRTAMasterFileDetailsBORepository;
import com.finlabs.finexa.repository.LookupTransactionRuleRepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.util.FinexaBOColumnConstant;
import com.finlabs.finexa.util.FinexaUtil;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

@Component

@Scope("prototype")

public class TransactionFeedUploadThread implements Runnable {

	@Autowired
	private Mapper mapper;

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private LookupRTABORepository lookupRTABORepository;

	@Autowired
	private LookupTransactionRuleRepository lookupTransactionRuleRepository;

	@Autowired
	private LookupRTAMasterFileDetailsBORepository lookupRTAMasterFileDetailsBORepository;

	@Autowired
	BackOfficeUploadHistoryRepository uploadHistoryRepo;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;
	
	@Autowired
	private MasterMFDailyNAVRepository masterMFDailyNAVRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FeedUploadThread.class);
	private TransactionMasterBODTO transactionMasterBODTO;
	private UploadResponseDTO uploadResponseDTO;
	private BackOfficeUploadHistory backOfficeUploadHistory;
	private static final String RTA_FILE_TYPE_CAMS = "1";
	private static final String RTA_FILE_TYPE_KARVY = "2";
	private static final String RTA_FILE_TYPE_FRANKLIN = "3";
	private static final String RTA_FILE_TYPE_SUNDARAM = "4";
	private String TRANSACTION_FEED_PROPERTIES_FILE;
	private Properties prop = new Properties();
	private List<Double> duplicateTransNumber = new ArrayList();
	private Integer rta;
	private String rtaId;
	private int rows = 1, columnHeaderRow = 0, noOfRejectedRecords = 0;
	private DBFField field = new DBFField();
	DBFReader dbfReader;
	private String rejectedRowNumbers = "";
	private Integer rejectedRowNumber = 0;
	public static final String STATUS_COMPLETED = "COMPLETED";
	public static final String STATUS_REJECTED = "REJECTED";
	public static final String STATUS_NOT_APPLICABLE = "NOT APPLICABLE";
	public static final String STATUS_RUNNING = "RUNNING";
	public static final String FAILURE = "Failure";
	public static final String SUCCESS = "Success";
	public static final String RECORD_REJECTION_MESSAGE = "Transaction Number was missing in each record. Row numbers are: ";
	public static final String NOT_REJECTED_MESSAGE = "All records are successfully uploaded.";
	public static final String FILE_REJECTION_MESSAGE = "One or more columns were not found in file.";
	public static final String FILE_NOT_PROCESSED_MESSAGE = "Could not process the file.";
	private boolean allRowsInserted = false;
	

	public void initialize(TransactionMasterBODTO transactionMasterBODTO, UploadResponseDTO uploadResponseDTO,
			BackOfficeUploadHistory backOfficeUploadHistory) {
		this.transactionMasterBODTO = transactionMasterBODTO;
		this.uploadResponseDTO = uploadResponseDTO;
		this.backOfficeUploadHistory = backOfficeUploadHistory;
	}

	@Override
	public void run() {
		try {

			System.out.println("In Transaction Thread********************");
			rta = transactionMasterBODTO.getNameRTA();
			rtaId = rta.toString();
			String name = transactionMasterBODTO.getNameSelectFile()[0].getOriginalFilename();
			
			
			if (name.contains(".dbf") || name.contains(".DBF")) {
				uploadResponseDTO = readDBFFeed(transactionMasterBODTO, uploadResponseDTO);
			} else {
				uploadResponseDTO = readTransactionExcelFeed(transactionMasterBODTO, uploadResponseDTO);
			}
			if (uploadResponseDTO.isStatus()) {
				backOfficeUploadHistory.setStatus(STATUS_COMPLETED);
				if (noOfRejectedRecords > 0) {

					if (rejectedRowNumbers.length() <= 255) {

						rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
						backOfficeUploadHistory.setReasonOfRejection(RECORD_REJECTION_MESSAGE + rejectedRowNumbers);

					} else {
						rejectedRowNumbers = rejectedRowNumbers.substring(0, rejectedRowNumbers.indexOf(','))
								+ rejectedRowNumber.toString() + ",";

						backOfficeUploadHistory.setReasonOfRejection(RECORD_REJECTION_MESSAGE + rejectedRowNumbers
								+ "Reason for rest rows are same as this.");
					}

					backOfficeUploadHistory.setAutoClientCreationStatus(STATUS_NOT_APPLICABLE);
				} else {
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

		} catch (RuntimeException | InvalidFormatException | IOException | ParseException e) {
			if (allRowsInserted == true) {
				backOfficeUploadHistory.setStatus(STATUS_COMPLETED);
				backOfficeUploadHistory.setRejectedRecords(uploadResponseDTO.getRejectedRecords());
				backOfficeUploadHistory.setEndTime(new java.util.Date());
				backOfficeUploadHistory.setAutoClientCreationStatus(STATUS_NOT_APPLICABLE);
				backOfficeUploadHistory.setReasonOfRejection(RECORD_REJECTION_MESSAGE + rejectedRowNumbers);
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

		/*
		 * uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		 * noOfRejectedRecords = 0;
		 */
	}

	private UploadResponseDTO readTransactionExcelFeed(TransactionMasterBODTO transactionMasterBODTO,
			UploadResponseDTO uploadResponseDTO) throws IOException, InvalidFormatException, ParseException {
		// TODO Auto-generated method stub
		System.out.println("*********Job Submitted Successfully Excel *************");
		InputStream initialStream = transactionMasterBODTO.getNameSelectFile()[0].getInputStream();

		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);

		File directory = new File(System.getProperty("java.io.tmpdir"));
		File targetFile = File.createTempFile("WBR2_" + uuid, ".tmp", directory);

		java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		IOUtils.closeQuietly(initialStream);

		System.out.println("******************" + targetFile.getUsableSpace());
		// Workbook workbook = new XSSFWorkbook(targetFile);
		Workbook workbook = WorkbookFactory.create(targetFile);
		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		Map<String, String> feed = new HashMap<>();

		rows = sheet.getLastRowNum();

		// Iterating over Rows and Columns using Iterator
		Iterator<Row> rowIterator = sheet.rowIterator();

		switch (rtaId) {

		case RTA_FILE_TYPE_CAMS:
			TRANSACTION_FEED_PROPERTIES_FILE = "backOfficeProperties/transactionCAMS.properties";
			break;
		case RTA_FILE_TYPE_KARVY:
			TRANSACTION_FEED_PROPERTIES_FILE = "backOfficeProperties/transactionKarvy.properties";
			break;
		case RTA_FILE_TYPE_FRANKLIN:
			TRANSACTION_FEED_PROPERTIES_FILE = "backOfficeProperties/transactionFrankiln.properties";
			break;
		default:
			break;

		}

		ClassLoader classLoader = getClass().getClassLoader();
		FileReader fileReader = new FileReader(classLoader.getResource(TRANSACTION_FEED_PROPERTIES_FILE).getFile());
		prop.load(fileReader);
		LookupRTAMasterFileDetailsBO lookupRTAMasterFileDetailsBO = lookupRTAMasterFileDetailsBORepository
				.findByFileCode("TRM");

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
		System.out.println(excelNameColNameMap);
		switch (rtaId) {

		case RTA_FILE_TYPE_CAMS:
			List<String> camsMandatoryFeedList = new ArrayList<>();
			camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_NUMBER);
			// camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_AMC_CODE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_FOLIO_NUMBER);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_SCHEME_RTA_CODE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_SCHEME_NAME);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_INVESTOR_NAME);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_TYPE_CODE);
			// camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_MODE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_DATE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_NAV);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_UNITS_OF_THE_TRANSACTION);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_AMOUNT_OF_THE_TRANSACTION);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_DISTRIBUTOR_ARN_CODE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_REPORT_DATE);

			for (Map.Entry<String, String> obj : excelNameColNameMap.entrySet()) {
				String key = obj.getKey();
				String value = obj.getValue();
				if (value.equals("nu") && camsMandatoryFeedList.contains(key)) { // implies mandatory column null
					uploadResponseDTO.setPrimaryKeyNotFound(true);
					System.out.println(key + " Address Null");
					uploadResponseDTO.setStatus(false);
					return uploadResponseDTO;
				}
			}
			break;
		case RTA_FILE_TYPE_KARVY:
			List<String> karvyMandatoryFeedList = new ArrayList<>();
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_NUMBER);
			// karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_AMC_CODE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_FOLIO_NUMBER);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_SCHEME_RTA_CODE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_SCHEME_NAME);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_INVESTOR_NAME);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE_CODE);
			// karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_MODE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_DATE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_NAV);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_UNITS_OF_THE_TRANSACTION);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_AMOUNT_OF_THE_TRANSACTION);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_DISTRIBUTOR_ARN_CODE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_REPORT_DATE);

			for (Map.Entry<String, String> obj : excelNameColNameMap.entrySet()) {
				String key = obj.getKey();
				String value = obj.getValue();
				if (value.equals("nu") && karvyMandatoryFeedList.contains(key)) { // implies mandatory column null
					uploadResponseDTO.setPrimaryKeyNotFound(true);
					System.out.println(key + " Address Null");
					uploadResponseDTO.setStatus(false);
					return uploadResponseDTO;
				}
			}
			break;
		case RTA_FILE_TYPE_FRANKLIN:
			List<String> franklinMandatoryFeedList = new ArrayList<>();
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_NUMBER);
			// franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_AMC_CODE);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_FOLIO_NUMBER);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_SCHEME_RTA_CODE);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_SCHEME_NAME);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_INVESTOR_NAME);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE);
			// franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_MODE);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_DATE);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_NAV);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_UNITS_OF_THE_TRANSACTION);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_AMOUNT_OF_THE_TRANSACTION);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_DISTRIBUTOR_ARN_CODE);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_REPORT_DATE);
			for (Map.Entry<String, String> obj : excelNameColNameMap.entrySet()) {
				String key = obj.getKey();
				String value = obj.getValue();
				if (value.equals("nu") && franklinMandatoryFeedList.contains(key)) { // implies mandatory column null
					uploadResponseDTO.setPrimaryKeyNotFound(true);
					System.out.println(key + " Address Null");
					uploadResponseDTO.setStatus(false);
					return uploadResponseDTO;
				}
			}
			break;
		/*
		 * case RTA_FILE_TYPE_SUNDARAM : FEED_PROPERTIES_FILE =
		 * "backOfficeProperties/investorSundaram.properties"; break;
		 */
		default:
			break;
		}

		List<TransactionMasterBO> transactionMasterBOList = new ArrayList<TransactionMasterBO>();
		int batchCount = 0;
		int counter = 0;
		// System.out.println("AdvisorID: " + transactionMasterBODTO.getAdvisorID());

		AdvisorUser advisorUser = advisorUserRepository.findOne(transactionMasterBODTO.getAdvisorID());
		// System.out.println("Inside readExcel method");

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
						Cell cell = row
								.getCell(org.apache.poi.ss.util.CellReference.convertColStringToIndex(columnAddress));
						dataOfCell = cell.toString();
					}
					feed.put(propertyKey, dataOfCell);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (batchCount == 20) {
				System.out.println(
						"transactionMasterBOList size() when batchCount is 20: " + transactionMasterBOList.size());
				uploadResponseDTO = saveTransactionData(transactionMasterBOList, uploadResponseDTO);
				transactionMasterBOList = new ArrayList<TransactionMasterBO>();
				batchCount = 0;
				counter++;
				System.out.println("Counter displayed" + counter);
				System.out.println("*********************************");

				switch (rtaId) {

				case RTA_FILE_TYPE_CAMS:
					transactionMasterBOList = populateTransactionCAMSModelList(feed, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_KARVY:
					transactionMasterBOList = populateTransactionKARVYModelList(feed, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_FRANKLIN:
					transactionMasterBOList = populateTransactionFranklinModelList(feed, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;
				default:
					break;

				}

				// System.out.println("transactionMasterBOList size(): " +
				// transactionMasterBOList.size());

				batchCount++;

			} else {

				switch (rtaId) {

				case RTA_FILE_TYPE_CAMS:
					transactionMasterBOList = populateTransactionCAMSModelList(feed, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_KARVY:
					transactionMasterBOList = populateTransactionKARVYModelList(feed, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_FRANKLIN:
					transactionMasterBOList = populateTransactionFranklinModelList(feed, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;
				default:
					break;

				}

				// System.out.println("transactionMasterBOList size(): " +
				// transactionMasterBOList.size());

				batchCount++;
			}

		}

		if (batchCount > 0) {
			System.out.println("transactionMasterBOList size() before last: " + transactionMasterBOList.size());
			uploadResponseDTO = saveTransactionData(transactionMasterBOList, uploadResponseDTO);
			allRowsInserted = true;

		}
		targetFile.delete();
		workbook.close();
		uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		return uploadResponseDTO;

	}

	private List<TransactionMasterBO> populateTransactionCAMSModelList(Map<String, String> feedData,
			TransactionMasterBODTO transactionMasterBODTO, List<TransactionMasterBO> transactionMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			TransactionMasterBO transactionMasterBO = mapper.map(transactionMasterBODTO, TransactionMasterBO.class);
			boolean isRecordValid = false;

			FinexaUtil finexaUtil = new FinexaUtil();

			transactionMasterBO.setAdvisorUser(advisorUser);

			transactionMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_AMC_CODE));
			transactionMasterBO
					.setApplicationNo(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_APPLICATION_NUMBER));
			transactionMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_BANK_NAME));
			// transactionMasterBO.setBasicTDS(feedData.get("tax"));
			transactionMasterBO
					.setBrokerageAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_BROKERAGE_AMOUNT));
			transactionMasterBO.setBrokeragePercentage(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_BROKERAGE_PERCENTAGE));
			// transactionMasterBO.setCaInitiatedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("ca_initiated_date"))));
			// transactionMasterBO.setCgstAmt(feedData.get("cgst_amount"));
			transactionMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_DISTRIBUTOR_ARN_CODE));
			// transactionMasterBO.setDpId(feedData.get("dp_id"));
			// transactionMasterBO.setEligibleAmt(feedData.get("eligib_amt"));
			// transactionMasterBO.setEntryLoad(feedData.get("load"));
			transactionMasterBO.setEuin(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_EUIN));
			// transactionMasterBO.setEuinOpted(feedData.get("euin_opted"));
			transactionMasterBO.setEuinValidIndicator(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_EUIN_VALID_INDICATOR));
			// transactionMasterBO.setExchangeFlag(feedData.get("exchange_flag"));
			// transactionMasterBO.setExchDcFlag(feedData.get("exch_dc_flag"));
			String folio_number;
			if (!feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_FOLIO_NUMBER).contains("/")) {
				double folioNumberDouble = Double
						.parseDouble(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_FOLIO_NUMBER));
				folio_number = String.format("%.0f", folioNumberDouble);
			} else {
				folio_number = feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_FOLIO_NUMBER);
			}
			transactionMasterBO.setFolioNo(formatNumbers(folio_number));
			// transactionMasterBO.setForm15HDetails(feedData.get("te_15h"));
			// transactionMasterBO.setGstStateCode(feedData.get("gst_state_code"));
			// transactionMasterBO.setIgstAmt(feedData.get("igst_amount"));
			// transactionMasterBO.setInvestorAccountNo(feedData.get("ac_no"));
			transactionMasterBO.setInvestorId(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_INVESTOR_ID));
			// transactionMasterBO.setInvestorMin(feedData.get("inv_iin"));
			transactionMasterBO
					.setInvestorName(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_INVESTOR_NAME));
			transactionMasterBO
					.setInvestorPan(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_INVESTOR_PAN));
			// transactionMasterBO.setLocation(feedData.get("location"));
			// transactionMasterBO.setLocationCategory(feedData.get("ter_location"));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");
			transactionMasterBO.setLookupRtabo(lookupRtabo);

			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_TYPE_CODE).startsWith("P")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("P");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("R")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("R");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("SI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("SO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("TI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("TO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("DR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("J")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("J");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			}

			// transactionMasterBO.setMicrNo(feedData.get("micr_no"));
			// transactionMasterBO.setMicrRemarks(feedData.get("remarks"));
			// transactionMasterBO.setMultBrokerOption(feedData.get("mult_brok"));
			transactionMasterBO.setNav(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_NAV));
			// transactionMasterBO.setOldFolioNo(feedData.get("old_folio"));
			transactionMasterBO.setProcessDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_PROCESS_DATE))));
			// transactionMasterBO.setReinvestFlag(feedData.get("reinvest_flag"));
			transactionMasterBO.setReportDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_REPORT_DATE))));
			transactionMasterBO.setReportTime(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_REPORT_TIME));
			// transactionMasterBO.setReversalCode(feedData.get("reversal_code"));
			// transactionMasterBO.setScanRefNo(feedData.get("scanrefno"));
			transactionMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_SCHEME_NAME));
			transactionMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_SCHEME_RTA_CODE));
			// transactionMasterBO.setSchemeType(feedData.get("scheme_type"));
			// transactionMasterBO.setSequenceNo(feedData.get("seq_no"));
			// transactionMasterBO.setSgstAmt(feedData.get("sgst_amount"));
			// transactionMasterBO.setSipTransNo(feedData.get("siptrxnno"));
			// transactionMasterBO.setSourceBrokerCode(feedData.get("src_brk_code"));
			// transactionMasterBO.setSourceCode(feedData.get("usercode"));
			// transactionMasterBO.setSourceSerialNumber(feedData.get("usrtrxno"));
			transactionMasterBO
					.setSubBrokerARN(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_SUB_BROKER_ARN));
			transactionMasterBO
					.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_SUB_BROKER_CODE));
			// transactionMasterBO.setSwFlag(feedData.get("swflag"));
			// transactionMasterBO.setSysRegDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("sys_regn_date"))));
			// transactionMasterBO.setTargetSrcScheme(feedData.get("targ_src_scheme"));
			// transactionMasterBO.setTaxStatus(feedData.get("tax_status"));
			// transactionMasterBO.setTdsAmt(feedData.get("total_tax"));
			// transactionMasterBO.setTicobPostedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("ticob_posted_date"))));
			// transactionMasterBO.setTicobTransNo(feedData.get("ticob_trno"));
			// transactionMasterBO.setTicobTransType(feedData.get("ticob_trtype"));
			transactionMasterBO.setTransactionDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_DATE))));
			transactionMasterBO
					.setTransactionMode(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_MODE));
			double transactionNumberDouble = Double
					.parseDouble(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_NUMBER));
			/*
			 * if(duplicateTransNumber.contains(transactionNumberDouble))
			 * fw.write(transactionNumberDouble + ", "); else
			 * duplicateTransNumber.add(transactionNumberDouble);
			 */
			String transactionNumberAsString = String.format("%.0f", transactionNumberDouble);
			transactionMasterBO
					.setTransactionNumber(formatNumbers(String.valueOf(Long.parseLong(transactionNumberAsString))));
			transactionMasterBO.setTransactionStatus(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_STATUS));
			transactionMasterBO.setTransactionTax(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_SECURITY_TRANSACTION_TAX));
			transactionMasterBO.setTransAmt(formatAmount(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_AMOUNT_OF_THE_TRANSACTION)));
			transactionMasterBO
					.setTransCharges(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_CHARGES));
			// transactionMasterBO.setTransSrc(feedData.get("src_of_txn"));
			transactionMasterBO
					.setTransSuffix(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_SUFFIX));
			transactionMasterBO.setTransType(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANS_TYPE));
			transactionMasterBO.setTransUnits(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_UNITS_OF_THE_TRANSACTION));
			transactionMasterBO.setTxnType(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TXN_TYPE));
			// System.out.println("NO: "+
			// feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_NUMBER).toString().trim());
			// String transNo =
			// feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_NUMBER).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_NUMBER).toString().trim() != null
					&& !feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_CAMS_TRANSACTION_NUMBER).toString().trim()
							.isEmpty()) {
				transactionMasterBOList.add(transactionMasterBO);
				isRecordValid = true;
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return transactionMasterBOList;

	}

	private List<TransactionMasterBO> populateTransactionFranklinModelList(Map<String, String> feedData,
			TransactionMasterBODTO transactionMasterBODTO, List<TransactionMasterBO> transactionMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub
		try {
			TransactionMasterBO transactionMasterBO = mapper.map(transactionMasterBODTO, TransactionMasterBO.class);
			boolean isRecordValid = false;

			FinexaUtil finexaUtil = new FinexaUtil();

			transactionMasterBO.setAdvisorUser(advisorUser);

			transactionMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_AMC_CODE));
			transactionMasterBO.setApplicationNo(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_APPLICATION_NUMBER));
			transactionMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_BANK_NAME));
			// transactionMasterBO.setBasicTDS(feedData.get("tax"));
			transactionMasterBO
					.setBrokerageAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_BROKERAGE_AMOUNT));
			transactionMasterBO.setBrokeragePercentage(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_BROKERAGE_PERCENTAGE));
			// transactionMasterBO.setCaInitiatedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("ca_initiated_date"))));
			// transactionMasterBO.setCgstAmt(feedData.get("cgst_amount"));
			transactionMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_DISTRIBUTOR_ARN_CODE));
			// transactionMasterBO.setDpId(feedData.get("dp_id"));
			// transactionMasterBO.setEligibleAmt(feedData.get("eligib_amt"));
			// transactionMasterBO.setEntryLoad(feedData.get("load"));
			transactionMasterBO.setEuin(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_EUIN));
			// transactionMasterBO.setEuinOpted(feedData.get("euin_opted"));
			transactionMasterBO.setEuinValidIndicator(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_EUIN_VALID_INDICATOR));
			// transactionMasterBO.setExchangeFlag(feedData.get("exchange_flag"));
			// transactionMasterBO.setExchDcFlag(feedData.get("exch_dc_flag"));
			String folio_number;
			if (!feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_FOLIO_NUMBER).contains("/")) {
				double folioNumberDouble = Double
						.parseDouble(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_FOLIO_NUMBER));
				folio_number = String.format("%.0f", folioNumberDouble);
			} else {
				folio_number = feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_FOLIO_NUMBER);
			}
			transactionMasterBO.setFolioNo(formatNumbers(folio_number));
			// transactionMasterBO.setForm15HDetails(feedData.get("te_15h"));
			// transactionMasterBO.setGstStateCode(feedData.get("gst_state_code"));
			// transactionMasterBO.setIgstAmt(feedData.get("igst_amount"));
			// transactionMasterBO.setInvestorAccountNo(feedData.get("ac_no"));
			transactionMasterBO
					.setInvestorId(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_INVESTOR_ID));
			// transactionMasterBO.setInvestorMin(feedData.get("inv_iin"));
			transactionMasterBO
					.setInvestorName(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_INVESTOR_NAME));
			transactionMasterBO
					.setInvestorPan(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_INVESTOR_PAN));
			// transactionMasterBO.setLocation(feedData.get("location"));
			transactionMasterBO.setLocationCategory(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_LOCATION_CATEGORY));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			transactionMasterBO.setLookupRtabo(lookupRtabo);

			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("ADDPUR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("ADDPUR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("DIR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DIR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("NEWPUR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("NEWPUR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("RED")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("RED");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SIP")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SIP");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("STPI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STPI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("ADDPURR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("ADDPURR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("DP")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DP");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("NEWPURR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("NEWPURR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("STPO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STPO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("PUR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("PUR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("STP O")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STP O");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("REDR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("REDR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SIPR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SIPR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("STPIR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STPIR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("STPOR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STPOR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SWD")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWD");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SWIN")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWIN");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SWINR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWINR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SWOF")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWOF");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SWOFR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWOFR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("TI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("TO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			}

			// transactionMasterBO.setMicrNo(feedData.get("micr_no"));
			// transactionMasterBO.setMicrRemarks(feedData.get("remarks"));
			// transactionMasterBO.setMultBrokerOption(feedData.get("mult_brok"));
			transactionMasterBO.setNav(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_NAV));
			// transactionMasterBO.setOldFolioNo(feedData.get("old_folio"));
			transactionMasterBO.setProcessDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_PROCESS_DATE))));
			// transactionMasterBO.setReinvestFlag(feedData.get("reinvest_flag"));
			transactionMasterBO.setReportDate(
					finexaUtil.formatDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_REPORT_DATE)));
			transactionMasterBO
					.setReportTime(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_REPORT_TIME));
			// transactionMasterBO.setReversalCode(feedData.get("reversal_code"));
			// transactionMasterBO.setScanRefNo(feedData.get("scanrefno"));
			transactionMasterBO
					.setSchemeName(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_SCHEME_NAME));
			transactionMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_SCHEME_RTA_CODE));
			// transactionMasterBO.setSchemeType(feedData.get("scheme_type"));
			// transactionMasterBO.setSequenceNo(feedData.get("seq_no"));
			// transactionMasterBO.setSgstAmt(feedData.get("sgst_amount"));
			// transactionMasterBO.setSipTransNo(feedData.get("siptrxnno"));
			// transactionMasterBO.setSourceBrokerCode(feedData.get("src_brk_code"));
			// transactionMasterBO.setSourceCode(feedData.get("usercode"));
			// transactionMasterBO.setSourceSerialNumber(feedData.get("usrtrxno"));
			transactionMasterBO
					.setSubBrokerARN(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_SUB_BROKER_ARN));
			transactionMasterBO
					.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_SUB_BROKER_CODE));
			// transactionMasterBO.setSwFlag(feedData.get("swflag"));
			// transactionMasterBO.setSysRegDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("sys_regn_date"))));
			// transactionMasterBO.setTargetSrcScheme(feedData.get("targ_src_scheme"));
			// transactionMasterBO.setTaxStatus(feedData.get("tax_status"));
			transactionMasterBO.setTdsAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TDS_AMOUNT));
			// transactionMasterBO.setTicobPostedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("ticob_posted_date"))));
			// transactionMasterBO.setTicobTransNo(feedData.get("ticob_trno"));
			// transactionMasterBO.setTicobTransType(feedData.get("ticob_trtype"));
			transactionMasterBO.setTransactionDate(finexaUtil.formatDate(finexaUtil.formatStringDate(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_DATE))));
			transactionMasterBO.setTransactionMode(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_MODE));
			// double transactionNumberDouble = Double.parseDouble(feedData.get("TRXN_NO"));
			/*
			 * if(duplicateTransNumber.contains(transactionNumberDouble))
			 * fw.write(transactionNumberDouble + ", "); else
			 * duplicateTransNumber.add(transactionNumberDouble);
			 */
			// String transactionNumberAsString = String.format("%.0f",
			// transactionNumberDouble);
			transactionMasterBO.setTransactionNumber(
					formatNumbers(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_NUMBER))
							.toString());
			transactionMasterBO.setTransactionStatus(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_STATUS));
			// transactionMasterBO.setTransactionTax(feedData.get("stt"));
			transactionMasterBO.setTransAmt(formatAmount(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_AMOUNT_OF_THE_TRANSACTION)));
			transactionMasterBO.setTransCharges(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_CHARGES));
			// transactionMasterBO.setTransSrc(feedData.get("src_of_txn"));
			transactionMasterBO.setTransSuffix("");
			transactionMasterBO.setTransType(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TXN_TYPE));
			transactionMasterBO.setTransUnits(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_UNITS_OF_THE_TRANSACTION));
			// transactionMasterBO.setTxnType(feedData.get("trxn_type_flag"));
			// String transNo =
			// feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_NUMBER).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_NUMBER).toString()
					.trim() != null
					&& !feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_NUMBER).toString()
							.trim().isEmpty()) {
				transactionMasterBOList.add(transactionMasterBO);
				isRecordValid = true;
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return transactionMasterBOList;
	}

	private List<TransactionMasterBO> populateTransactionKARVYModelList(Map<String, String> feedData,
			TransactionMasterBODTO transactionMasterBODTO, List<TransactionMasterBO> transactionMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub
		try {
			TransactionMasterBO transactionMasterBO = mapper.map(transactionMasterBODTO, TransactionMasterBO.class);
			boolean isRecordValid = false;

			FinexaUtil finexaUtil = new FinexaUtil();

			transactionMasterBO.setAdvisorUser(advisorUser);

			transactionMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_AMC_CODE));
			transactionMasterBO
					.setApplicationNo(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_APPLICATION_NUMBER));
			transactionMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_BANK_NAME));
			// transactionMasterBO.setBasicTDS(feedData.get("tax"));
			transactionMasterBO
					.setBrokerageAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_BROKERAGE_AMOUNT));
			transactionMasterBO.setBrokeragePercentage(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_BROKERAGE_PERCENTAGE));
			/*
			 * transactionMasterBO.setCaInitiatedDate(feedData.get("ca_initiated_date"));
			 * transactionMasterBO.setCgstAmt(feedData.get("cgst_amount"));
			 */
			transactionMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_DISTRIBUTOR_ARN_CODE));
			/*
			 * transactionMasterBO.setDpId(feedData.get("dp_id"));
			 * transactionMasterBO.setEligibleAmt(feedData.get("eligib_amt"));
			 * transactionMasterBO.setEntryLoad(feedData.get("load"));
			 */
			transactionMasterBO.setEuin(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_EUIN));
			transactionMasterBO.setEuinValidIndicator(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_EUIN_VALID_INDICATOR));
			/*
			 * transactionMasterBO.setExchangeFlag(feedData.get("exchange_flag"));
			 * transactionMasterBO.setExchDcFlag(feedData.get("exch_dc_flag"));
			 */
			transactionMasterBO.setFolioNo(
					formatNumbers(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_FOLIO_NUMBER)));
			/*
			 * transactionMasterBO.setForm15HDetails(feedData.get("te_15h"));
			 * transactionMasterBO.setGstStateCode(feedData.get("gst_state_code"));
			 * transactionMasterBO.setIgstAmt(feedData.get("igst_amount"));
			 * transactionMasterBO.setInvestorAccountNo(feedData.get("ac_no"));
			 */
			transactionMasterBO.setInvestorId(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_INVESTOR_ID));
			// transactionMasterBO.setInvestorMin(feedData.get("inv_iin"));
			transactionMasterBO
					.setInvestorName(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_INVESTOR_NAME));
			transactionMasterBO
					.setInvestorPan(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_INVESTOR_PAN));
			/*
			 * transactionMasterBO.setLocation(feedData.get("location"));
			 */
			transactionMasterBO.setLocationCategory(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_LOCATION_CATEGORY));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			transactionMasterBO.setLookupRtabo(lookupRtabo);
			// System.out.println(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE_CODE));
			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE_CODE).startsWith("P")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("P");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("R")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("R");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("SI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("SO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("TI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("TO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("DR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("DP")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DP");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			}

			/*
			 * transactionMasterBO.setMicrNo(feedData.get("micr_no"));
			 * transactionMasterBO.setMicrRemarks(feedData.get("remarks"));
			 * transactionMasterBO.setMultBrokerOption(feedData.get("mult_brok"));
			 * transactionMasterBO.setOldFolioNo(feedData.get("old_folio"));
			 */
			transactionMasterBO.setNav(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_NAV));
			transactionMasterBO.setProcessDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_PROCESS_DATE))));
			// transactionMasterBO.setReinvestFlag(feedData.get("reinvest_flag"));
			transactionMasterBO.setReportDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_REPORT_DATE))));
			transactionMasterBO.setReportTime(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_REPORT_TIME));
			/*
			 * transactionMasterBO.setReversalCode(feedData.get("reversal_code"));
			 * transactionMasterBO.setScanRefNo(feedData.get("scanrefno"));
			 */
			transactionMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_SCHEME_NAME));
			transactionMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_SCHEME_RTA_CODE));
			/*
			 * transactionMasterBO.setSchemeType(feedData.get("scheme_type"));
			 * transactionMasterBO.setSequenceNo(feedData.get("seq_no"));
			 * transactionMasterBO.setSgstAmt(feedData.get("sgst_amount"));
			 * transactionMasterBO.setSipTransNo(feedData.get("siptrxnno"));
			 * transactionMasterBO.setSourceBrokerCode(feedData.get("src_brk_code"));
			 * transactionMasterBO.setSourceCode(feedData.get("usercode"));
			 * transactionMasterBO.setSourceSerialNumber(feedData.get("usrtrxno"));
			 */
			transactionMasterBO
					.setSubBrokerARN(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_SUB_BROKER_ARN));
			/*
			 * transactionMasterBO.setSubBrokerCode(feedData.get("subbrok"));
			 * transactionMasterBO.setSwFlag(feedData.get("swflag"));
			 * transactionMasterBO.setSysRegDate(feedData.get("sys_regn_date"));
			 * transactionMasterBO.setTargetSrcScheme(feedData.get("targ_src_scheme"));
			 * transactionMasterBO.setTaxStatus(feedData.get("tax_status"));
			 */
			transactionMasterBO.setTdsAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TDS_AMOUNT));
			/*
			 * transactionMasterBO.setTicobPostedDate(feedData.get("ticob_posted_date"));
			 * transactionMasterBO.setTicobTransNo(feedData.get("ticob_trno"));
			 * transactionMasterBO.setTicobTransType(feedData.get("ticob_trtype"));
			 */
			transactionMasterBO.setTransactionDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_DATE))));
			transactionMasterBO
					.setTransactionMode(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_MODE));
			double transactionNumberDouble = Double
					.parseDouble(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_NUMBER));
			String transactionNumberAsString = String.format("%.0f", transactionNumberDouble);
			transactionMasterBO
					.setTransactionNumber(formatNumbers(String.valueOf(Long.parseLong(transactionNumberAsString))));
			transactionMasterBO.setTransactionStatus(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_STATUS));
			transactionMasterBO.setTransactionTax(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_SECURITY_TRANSACTION_TAX));
			transactionMasterBO.setTransAmt(formatAmount(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_AMOUNT_OF_THE_TRANSACTION)));
			transactionMasterBO
					.setTransSuffix(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_SUFFIX));
			transactionMasterBO
					.setTransType(feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE));
			transactionMasterBO.setTransUnits(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_UNITS_OF_THE_TRANSACTION));
			// transactionMasterBO.setTxnType(feedData.get("trxn_type_flag"));
			// String transNo =
			// feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_NUMBER).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_NUMBER).toString()
					.trim() != null
					&& !feedData.get(FinexaBOColumnConstant.TRANSACTION_EXCEL_KARVY_TRANSACTION_NUMBER).toString()
							.trim().isEmpty()) {
				transactionMasterBOList.add(transactionMasterBO);
				isRecordValid = true;
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return transactionMasterBOList;

	}

	private UploadResponseDTO readDBFFeed(TransactionMasterBODTO transactionMasterBODTO,
			UploadResponseDTO uploadResponseDTO) throws IOException, InvalidFormatException, ParseException {

		InputStream initialStream = transactionMasterBODTO.getNameSelectFile()[0].getInputStream();
		InputStream initialStream2 = transactionMasterBODTO.getNameSelectFile()[0].getInputStream();
		dbfReader = new DBFReader(initialStream);
		boolean headerFound = false, firstRow;
		int numberOfFields;
		String headerPropFile, columnPropFile;
		Properties prop1 = new Properties();
		Object[] rowObjects1, rowObjects2;
		int batchCount = 0, counter = 0, headerRowCount = 0, row, numberOfRecords = 0, numberOfRecordRunning = 0;
		List<TransactionMasterBO> transactionMasterBOList = new ArrayList<TransactionMasterBO>();
		List<String> fileColNames = new ArrayList<String>();
		List<String> propColNames = new ArrayList<String>();
		Map<String, String> dataMap = new HashMap<>();
		Map<String, String> transFeedMap = new HashMap<>();

		// Number of columns in the DBF file
		numberOfFields = dbfReader.getFieldCount();
		numberOfRecords = dbfReader.getRecordCount();
		headerPropFile = "backOfficeProperties/transactionDBF.properties";
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
			transFeedMap.put(i.toString(), fileColname);
		}
		prop1 = new Properties();
		switch (rtaId) {

		case RTA_FILE_TYPE_CAMS:
			columnPropFile = "backOfficeProperties/transactionCAMSDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case RTA_FILE_TYPE_KARVY:
			columnPropFile = "backOfficeProperties/transactionKarvyDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case RTA_FILE_TYPE_FRANKLIN:
			columnPropFile = "backOfficeProperties/transactionFranklinDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case RTA_FILE_TYPE_SUNDARAM:
			columnPropFile = "backOfficeProperties/transactionSundaramDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		default:
			break;

		}

		for (Map.Entry<String, String> entry : transFeedMap.entrySet()) {
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

		AdvisorUser advisorUser = advisorUserRepository.findOne(transactionMasterBODTO.getAdvisorID());
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
				dataMap.put(transFeedMap.get(fieldIndex.toString()), String.valueOf(rowObjects2[columnIndex]).trim());
			}

			// }

			if (batchCount == 1000) {
				System.out.println(
						"transactionMasterBOList size() when batchCount is 20: " + transactionMasterBOList.size());
				uploadResponseDTO = saveTransactionData(transactionMasterBOList, uploadResponseDTO);
				transactionMasterBOList = new ArrayList<TransactionMasterBO>();
				batchCount = 0;
				counter++;
				System.out.println("Counter displayed" + counter);
				System.out.println("*********************************");

				switch (rtaId) {

				case RTA_FILE_TYPE_CAMS:
					transactionMasterBOList = populateDBFCAMSModelList(dataMap, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_KARVY:
					transactionMasterBOList = populateDBFKARVYModelList(dataMap, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_FRANKLIN:
					transactionMasterBOList = populateDBFFranklinModelList(dataMap, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_SUNDARAM:
					transactionMasterBOList = populateDBFSUNDARAMModelList(dataMap, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;
				default:
					break;

				}

				// System.out.println("transactionMasterBOList size(): " +
				// transactionMasterBOList.size());

				batchCount++;

			} else {

				switch (rtaId) {

				case RTA_FILE_TYPE_CAMS:
					transactionMasterBOList = populateDBFCAMSModelList(dataMap, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_KARVY:
					transactionMasterBOList = populateDBFKARVYModelList(dataMap, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_FRANKLIN:
					transactionMasterBOList = populateDBFFranklinModelList(dataMap, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_SUNDARAM:
					transactionMasterBOList = populateDBFSUNDARAMModelList(dataMap, transactionMasterBODTO,
							transactionMasterBOList, advisorUser);
					break;
				default:
					break;

				}

				// System.out.println("transactionMasterBOList size(): " +
				// transactionMasterBOList.size());

				batchCount++;
			}
			//System.out.println(rowObjects2.toString());
			if(numberOfRecordRunning == numberOfRecords)
				break;
			rowObjects2 = dbfReader.nextRecord();
		}
		if (batchCount > 0) {
			System.out.println("transactionMasterBOList size() before last: " + transactionMasterBOList.size());
			uploadResponseDTO = saveTransactionData(transactionMasterBOList, uploadResponseDTO);
		}
		// System.out.println(dataMap);

		uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		return uploadResponseDTO;

	}

	private List<TransactionMasterBO> populateDBFCAMSModelList(Map<String, String> feedData,
			TransactionMasterBODTO transactionMasterBODTO, List<TransactionMasterBO> transactionMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			TransactionMasterBO transactionMasterBO = mapper.map(transactionMasterBODTO, TransactionMasterBO.class);
			boolean isRecordValid = false;

			FinexaUtil finexaUtil = new FinexaUtil();

			transactionMasterBO.setAdvisorUser(advisorUser);

			transactionMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_AMC_CODE));
			transactionMasterBO
					.setApplicationNo(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_APPLICATION_NUMBER));
			transactionMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_BANK_NAME));
			// transactionMasterBO.setBasicTDS(feedData.get("tax"));
			transactionMasterBO
					.setBrokerageAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_BROKERAGE_AMOUNT));
			transactionMasterBO.setBrokeragePercentage(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_BROKERAGE_PERCENTAGE));
			// transactionMasterBO.setCaInitiatedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("ca_initiated_date"))));
			// transactionMasterBO.setCgstAmt(feedData.get("cgst_amount"));
			transactionMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_DISTRIBUTOR_ARN_CODE));
			// transactionMasterBO.setDpId(feedData.get("dp_id"));
			// transactionMasterBO.setEligibleAmt(feedData.get("eligib_amt"));
			// transactionMasterBO.setEntryLoad(feedData.get("load"));
			transactionMasterBO.setEuin(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_EUIN));
			// transactionMasterBO.setEuinOpted(feedData.get("euin_opted"));
			transactionMasterBO.setEuinValidIndicator(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_EUIN_VALID_INDICATOR));
			// transactionMasterBO.setExchangeFlag(feedData.get("exchange_flag"));
			// transactionMasterBO.setExchDcFlag(feedData.get("exch_dc_flag"));
			String folio_number;
			if (!feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_FOLIO_NUMBER).contains("/")) {
				double folioNumberDouble = Double
						.parseDouble(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_FOLIO_NUMBER));
				folio_number = String.format("%.0f", folioNumberDouble);
			} else {
				folio_number = feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_FOLIO_NUMBER);
			}
			transactionMasterBO.setFolioNo(formatNumbers(folio_number));
			// transactionMasterBO.setForm15HDetails(feedData.get("te_15h"));
			// transactionMasterBO.setGstStateCode(feedData.get("gst_state_code"));
			// transactionMasterBO.setIgstAmt(feedData.get("igst_amount"));
			// transactionMasterBO.setInvestorAccountNo(feedData.get("ac_no"));
			transactionMasterBO.setInvestorId(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_INVESTOR_ID));
			// transactionMasterBO.setInvestorMin(feedData.get("inv_iin"));
			transactionMasterBO
					.setInvestorName(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_INVESTOR_NAME));
			transactionMasterBO.setInvestorPan(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_INVESTOR_PAN));
			// transactionMasterBO.setLocation(feedData.get("location"));
			// transactionMasterBO.setLocationCategory(feedData.get("ter_location"));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");
			transactionMasterBO.setLookupRtabo(lookupRtabo);

			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_TYPE_CODE).startsWith("P")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("P");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("R")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("R");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("SI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("SO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("TI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("TO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("DR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("J")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("J");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			}else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_TYPE_CODE)
					.startsWith("DP")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DP");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			}

			// transactionMasterBO.setMicrNo(feedData.get("micr_no"));
			// transactionMasterBO.setMicrRemarks(feedData.get("remarks"));
			// transactionMasterBO.setMultBrokerOption(feedData.get("mult_brok"));
			transactionMasterBO.setNav(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_NAV));
			// transactionMasterBO.setOldFolioNo(feedData.get("old_folio"));
			transactionMasterBO.setProcessDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_PROCESS_DATE)));
			// transactionMasterBO.setReinvestFlag(feedData.get("reinvest_flag"));
			transactionMasterBO.setReportDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_REPORT_DATE)));
			transactionMasterBO.setReportTime(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_REPORT_TIME));
			// transactionMasterBO.setReversalCode(feedData.get("reversal_code"));
			// transactionMasterBO.setScanRefNo(feedData.get("scanrefno"));
			transactionMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_SCHEME_NAME));
			transactionMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_SCHEME_RTA_CODE));
			// transactionMasterBO.setSchemeType(feedData.get("scheme_type"));
			// transactionMasterBO.setSequenceNo(feedData.get("seq_no"));
			// transactionMasterBO.setSgstAmt(feedData.get("sgst_amount"));
			// transactionMasterBO.setSipTransNo(feedData.get("siptrxnno"));
			// transactionMasterBO.setSourceBrokerCode(feedData.get("src_brk_code"));
			// transactionMasterBO.setSourceCode(feedData.get("usercode"));
			// transactionMasterBO.setSourceSerialNumber(feedData.get("usrtrxno"));
			transactionMasterBO
					.setSubBrokerARN(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_SUB_BROKER_ARN));
			transactionMasterBO
					.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_SUB_BROKER_CODE));
			// transactionMasterBO.setSwFlag(feedData.get("swflag"));
			// transactionMasterBO.setSysRegDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("sys_regn_date"))));
			// transactionMasterBO.setTargetSrcScheme(feedData.get("targ_src_scheme"));
			// transactionMasterBO.setTaxStatus(feedData.get("tax_status"));
			// transactionMasterBO.setTdsAmt(feedData.get("total_tax"));
			// transactionMasterBO.setTicobPostedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("ticob_posted_date"))));
			// transactionMasterBO.setTicobTransNo(feedData.get("ticob_trno"));
			// transactionMasterBO.setTicobTransType(feedData.get("ticob_trtype"));
			transactionMasterBO.setTransactionDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_DATE)));
			transactionMasterBO
					.setTransactionMode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_MODE));
			double transactionNumberDouble = Double
					.parseDouble(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_NUMBER));
			/*
			 * if(duplicateTransNumber.contains(transactionNumberDouble))
			 * fw.write(transactionNumberDouble + ", "); else
			 * duplicateTransNumber.add(transactionNumberDouble);
			 */
			String transactionNumberAsString = String.format("%.0f", transactionNumberDouble);
			transactionMasterBO
					.setTransactionNumber(formatNumbers(String.valueOf(Long.parseLong(transactionNumberAsString))));
			transactionMasterBO
					.setTransactionStatus(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_STATUS));
			transactionMasterBO.setTransactionTax(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_SECURITY_TRANSACTION_TAX));
			transactionMasterBO.setTransAmt(
					formatAmount(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_AMOUNT_OF_THE_TRANSACTION)));
			transactionMasterBO
					.setTransCharges(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_CHARGES));
			// transactionMasterBO.setTransSrc(feedData.get("src_of_txn"));
			transactionMasterBO
					.setTransSuffix(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_SUFFIX));
			transactionMasterBO.setTransType(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANS_TYPE));
			transactionMasterBO
					.setTransUnits(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_UNITS_OF_THE_TRANSACTION));
			// transactionMasterBO.setTxnType(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TXN_TYPE));
			// String transNo =
			// feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_NUMBER).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_NUMBER).toString().trim() != null
					&& !feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_CAMS_TRANSACTION_NUMBER).toString().trim()
							.isEmpty()) {
				transactionMasterBOList.add(transactionMasterBO);
				isRecordValid = true;
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return transactionMasterBOList;

	}

	private List<TransactionMasterBO> populateDBFFranklinModelList(Map<String, String> feedData,
			TransactionMasterBODTO transactionMasterBODTO, List<TransactionMasterBO> transactionMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub
		try {
			TransactionMasterBO transactionMasterBO = mapper.map(transactionMasterBODTO, TransactionMasterBO.class);
			boolean isRecordValid = false;

			FinexaUtil finexaUtil = new FinexaUtil();

			transactionMasterBO.setAdvisorUser(advisorUser);

			transactionMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_AMC_CODE));
			transactionMasterBO
					.setApplicationNo(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_APPLICATION_NUMBER));
			transactionMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_BANK_NAME));
			// transactionMasterBO.setBasicTDS(feedData.get("tax"));
			transactionMasterBO
					.setBrokerageAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_BROKERAGE_AMOUNT));
			transactionMasterBO.setBrokeragePercentage(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_BROKERAGE_PERCENTAGE));
			// transactionMasterBO.setCaInitiatedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("ca_initiated_date"))));
			// transactionMasterBO.setCgstAmt(feedData.get("cgst_amount"));
			transactionMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_DISTRIBUTOR_ARN_CODE));
			// transactionMasterBO.setDpId(feedData.get("dp_id"));
			// transactionMasterBO.setEligibleAmt(feedData.get("eligib_amt"));
			// transactionMasterBO.setEntryLoad(feedData.get("load"));
			transactionMasterBO.setEuin(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_EUIN));
			// transactionMasterBO.setEuinOpted(feedData.get("euin_opted"));
			transactionMasterBO.setEuinValidIndicator(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_EUIN_VALID_INDICATOR));
			// transactionMasterBO.setExchangeFlag(feedData.get("exchange_flag"));
			// transactionMasterBO.setExchDcFlag(feedData.get("exch_dc_flag"));
			String folio_number;
			if (!feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_FOLIO_NUMBER).contains("/")) {
				double folioNumberDouble = Double
						.parseDouble(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_FOLIO_NUMBER));
				folio_number = String.format("%.0f", folioNumberDouble);
			} else {
				folio_number = feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_FOLIO_NUMBER);
			}
			transactionMasterBO.setFolioNo(formatNumbers(folio_number));
			// transactionMasterBO.setForm15HDetails(feedData.get("te_15h"));
			// transactionMasterBO.setGstStateCode(feedData.get("gst_state_code"));
			// transactionMasterBO.setIgstAmt(feedData.get("igst_amount"));
			// transactionMasterBO.setInvestorAccountNo(feedData.get("ac_no"));
			transactionMasterBO
					.setInvestorId(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_INVESTOR_ID));
			// transactionMasterBO.setInvestorMin(feedData.get("inv_iin"));
			transactionMasterBO
					.setInvestorName(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_INVESTOR_NAME));
			transactionMasterBO
					.setInvestorPan(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_INVESTOR_PAN));
			// transactionMasterBO.setLocation(feedData.get("location"));
			transactionMasterBO.setLocationCategory(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_LOCATION_CATEGORY));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			transactionMasterBO.setLookupRtabo(lookupRtabo);

			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("ADDPUR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("ADDPUR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("DIR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DIR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("NEWPUR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("NEWPUR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("RED")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("RED");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SIP")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SIP");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("STPI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STPI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("ADDPURR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("ADDPURR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("DP")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DP");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("NEWPURR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("NEWPURR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("STPO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STPO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("PUR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("PUR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("STP O")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STP O");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("REDR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("REDR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SIPR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SIPR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("STPIR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STPIR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("STPOR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("STPOR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SWD")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWD");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SWIN")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWIN");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SWINR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWINR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SWOF")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWOF");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("SWOFR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SWOFR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("TI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE)
					.startsWith("TO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			}

			// transactionMasterBO.setMicrNo(feedData.get("micr_no"));
			// transactionMasterBO.setMicrRemarks(feedData.get("remarks"));
			// transactionMasterBO.setMultBrokerOption(feedData.get("mult_brok"));
			transactionMasterBO.setNav(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_NAV));
			// transactionMasterBO.setOldFolioNo(feedData.get("old_folio"));
			transactionMasterBO.setProcessDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_PROCESS_DATE)));
			// transactionMasterBO.setReinvestFlag(feedData.get("reinvest_flag"));
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date uDate = new java.util.Date();
			if (!feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_REPORT_DATE).trim().isEmpty()
					&& !feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_REPORT_DATE).trim().contains("")
					&& !feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_REPORT_DATE).trim()
							.contains("null")) {
				uDate = formatter.parse(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_REPORT_DATE));
				transactionMasterBO.setReportDate(utilToSQLDate(uDate.toString()));
			}
			transactionMasterBO
					.setReportTime(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_REPORT_TIME));
			// transactionMasterBO.setReversalCode(feedData.get("reversal_code"));
			// transactionMasterBO.setScanRefNo(feedData.get("scanrefno"));
			transactionMasterBO
					.setSchemeName(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_SCHEME_NAME));
			transactionMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_SCHEME_RTA_CODE));
			// transactionMasterBO.setSchemeType(feedData.get("scheme_type"));
			// transactionMasterBO.setSequenceNo(feedData.get("seq_no"));
			// transactionMasterBO.setSgstAmt(feedData.get("sgst_amount"));
			// transactionMasterBO.setSipTransNo(feedData.get("siptrxnno"));
			// transactionMasterBO.setSourceBrokerCode(feedData.get("src_brk_code"));
			// transactionMasterBO.setSourceCode(feedData.get("usercode"));
			// transactionMasterBO.setSourceSerialNumber(feedData.get("usrtrxno"));
			transactionMasterBO
					.setSubBrokerARN(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_SUB_BROKER_ARN));
			transactionMasterBO
					.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_SUB_BROKER_CODE));
			// transactionMasterBO.setSwFlag(feedData.get("swflag"));
			// transactionMasterBO.setSysRegDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("sys_regn_date"))));
			// transactionMasterBO.setTargetSrcScheme(feedData.get("targ_src_scheme"));
			// transactionMasterBO.setTaxStatus(feedData.get("tax_status"));
			transactionMasterBO.setTdsAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TDS_AMOUNT));
			// transactionMasterBO.setTicobPostedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("ticob_posted_date"))));
			// transactionMasterBO.setTicobTransNo(feedData.get("ticob_trno"));
			// transactionMasterBO.setTicobTransType(feedData.get("ticob_trtype"));
			transactionMasterBO.setTransactionDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_DATE)));
			transactionMasterBO
					.setTransactionMode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_MODE));
			// double transactionNumberDouble = Double.parseDouble(feedData.get("TRXN_NO"));
			/*
			 * if(duplicateTransNumber.contains(transactionNumberDouble))
			 * fw.write(transactionNumberDouble + ", "); else
			 * duplicateTransNumber.add(transactionNumberDouble);
			 */
			// String transactionNumberAsString = String.format("%.0f",
			// transactionNumberDouble);
			transactionMasterBO.setTransactionNumber(
					formatNumbers(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_NUMBER)));
			transactionMasterBO.setTransactionStatus(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_STATUS));
			// transactionMasterBO.setTransactionTax(feedData.get("stt"));
			transactionMasterBO.setTransAmt(formatAmount(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_AMOUNT_OF_THE_TRANSACTION)));
			transactionMasterBO
					.setTransCharges(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_CHARGES));
			// transactionMasterBO.setTransSrc(feedData.get("src_of_txn"));
			transactionMasterBO.setTransSuffix("");
			transactionMasterBO.setTransType(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TXN_TYPE));
			transactionMasterBO.setTransUnits(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_UNITS_OF_THE_TRANSACTION));
			// transactionMasterBO.setTxnType(feedData.get("trxn_type_flag"));
			// String transNo =
			// feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_NUMBER).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_NUMBER).toString()
					.trim() != null
					&& !feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_FRANKLIN_TRANSACTION_NUMBER).toString()
							.trim().isEmpty()) {
				transactionMasterBOList.add(transactionMasterBO);
				isRecordValid = true;
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return transactionMasterBOList;
	}

	private List<TransactionMasterBO> populateDBFKARVYModelList(Map<String, String> feedData,
			TransactionMasterBODTO transactionMasterBODTO, List<TransactionMasterBO> transactionMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub
		try {
			TransactionMasterBO transactionMasterBO = mapper.map(transactionMasterBODTO, TransactionMasterBO.class);
			boolean isRecordValid = false;

			FinexaUtil finexaUtil = new FinexaUtil();

			transactionMasterBO.setAdvisorUser(advisorUser);

			transactionMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_AMC_CODE));
			transactionMasterBO
					.setApplicationNo(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_APPLICATION_NUMBER));
			transactionMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_BANK_NAME));
			// transactionMasterBO.setBasicTDS(feedData.get("tax"));
			transactionMasterBO
					.setBrokerageAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_BROKERAGE_AMOUNT));
			transactionMasterBO.setBrokeragePercentage(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_BROKERAGE_PERCENTAGE));
			/*
			 * transactionMasterBO.setCaInitiatedDate(feedData.get("ca_initiated_date"));
			 * transactionMasterBO.setCgstAmt(feedData.get("cgst_amount"));
			 */
			transactionMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_DISTRIBUTOR_ARN_CODE));
			/*
			 * transactionMasterBO.setDpId(feedData.get("dp_id"));
			 * transactionMasterBO.setEligibleAmt(feedData.get("eligib_amt"));
			 * transactionMasterBO.setEntryLoad(feedData.get("load"));
			 */
			transactionMasterBO.setEuin(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_EUIN));
			transactionMasterBO.setEuinValidIndicator(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_EUIN_VALID_INDICATOR));
			/*
			 * transactionMasterBO.setExchangeFlag(feedData.get("exchange_flag"));
			 * transactionMasterBO.setExchDcFlag(feedData.get("exch_dc_flag"));
			 */
			/*
			 * if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_FOLIO_NUMBER).
			 * contains("/")) {
			 * transactionMasterBO.setFolioNo(feedData.get(FinexaBOColumnConstant.
			 * TRANSACTION_DBF_KARVY_FOLIO_NUMBER)); } else {
			 * 
			 * BigDecimal bigDecimaldata = new BigDecimal(
			 * feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_FOLIO_NUMBER));
			 * 
			 * Long longData = bigDecimaldata.longValueExact();
			 * 
			 * String stringDataOfCell = longData.toString();
			 * transactionMasterBO.setFolioNo(stringDataOfCell); }
			 */
			transactionMasterBO
					.setFolioNo(formatNumbers(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_FOLIO_NUMBER)));
			/*
			 * transactionMasterBO.setForm15HDetails(feedData.get("te_15h"));
			 * transactionMasterBO.setGstStateCode(feedData.get("gst_state_code"));
			 * transactionMasterBO.setIgstAmt(feedData.get("igst_amount"));
			 * transactionMasterBO.setInvestorAccountNo(feedData.get("ac_no"));
			 */
			transactionMasterBO.setInvestorId(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_INVESTOR_ID));
			// transactionMasterBO.setInvestorMin(feedData.get("inv_iin"));
			transactionMasterBO
					.setInvestorName(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_INVESTOR_NAME));
			transactionMasterBO.setInvestorPan(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_INVESTOR_PAN));
			/*
			 * transactionMasterBO.setLocation(feedData.get("location"));
			 */
			transactionMasterBO
					.setLocationCategory(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_LOCATION_CATEGORY));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			transactionMasterBO.setLookupRtabo(lookupRtabo);

			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_TYPE_CODE).startsWith("P")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("P");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("R")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("R");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("SI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("SO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("TI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("TO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("DR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_TYPE_CODE)
					.startsWith("DP")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DP");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			}

			/*
			 * transactionMasterBO.setMicrNo(feedData.get("micr_no"));
			 * transactionMasterBO.setMicrRemarks(feedData.get("remarks"));
			 * transactionMasterBO.setMultBrokerOption(feedData.get("mult_brok"));
			 * transactionMasterBO.setOldFolioNo(feedData.get("old_folio"));
			 */
			transactionMasterBO.setNav(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_NAV));
			transactionMasterBO.setProcessDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_PROCESS_DATE)));
			// transactionMasterBO.setReinvestFlag(feedData.get("reinvest_flag"));
			transactionMasterBO.setReportDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_REPORT_DATE)));
			transactionMasterBO.setReportTime(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_REPORT_TIME));
			/*
			 * transactionMasterBO.setReversalCode(feedData.get("reversal_code"));
			 * transactionMasterBO.setScanRefNo(feedData.get("scanrefno"));
			 */
			transactionMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_SCHEME_NAME));
			transactionMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_SCHEME_RTA_CODE));
			/*
			 * transactionMasterBO.setSchemeType(feedData.get("scheme_type"));
			 * transactionMasterBO.setSequenceNo(feedData.get("seq_no"));
			 * transactionMasterBO.setSgstAmt(feedData.get("sgst_amount"));
			 * transactionMasterBO.setSipTransNo(feedData.get("siptrxnno"));
			 * transactionMasterBO.setSourceBrokerCode(feedData.get("src_brk_code"));
			 * transactionMasterBO.setSourceCode(feedData.get("usercode"));
			 * transactionMasterBO.setSourceSerialNumber(feedData.get("usrtrxno"));
			 */
			transactionMasterBO
					.setSubBrokerARN(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_SUB_BROKER_ARN));
			/*
			 * transactionMasterBO.setSubBrokerCode(feedData.get("subbrok"));
			 * transactionMasterBO.setSwFlag(feedData.get("swflag"));
			 * transactionMasterBO.setSysRegDate(feedData.get("sys_regn_date"));
			 * transactionMasterBO.setTargetSrcScheme(feedData.get("targ_src_scheme"));
			 * transactionMasterBO.setTaxStatus(feedData.get("tax_status"));
			 */
			transactionMasterBO.setTdsAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TDS_AMOUNT));
			/*
			 * transactionMasterBO.setTicobPostedDate(feedData.get("ticob_posted_date"));
			 * transactionMasterBO.setTicobTransNo(feedData.get("ticob_trno"));
			 * transactionMasterBO.setTicobTransType(feedData.get("ticob_trtype"));
			 */
			transactionMasterBO.setTransactionDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_DATE)));
			transactionMasterBO
					.setTransactionMode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_MODE));
			double transactionNumberDouble = Double
					.parseDouble(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_NUMBER));
			String transactionNumberAsString = String.format("%.0f", transactionNumberDouble);
			transactionMasterBO
					.setTransactionNumber(formatNumbers(String.valueOf(Long.parseLong(transactionNumberAsString))));
			transactionMasterBO.setTransactionStatus(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_STATUS));
			transactionMasterBO.setTransactionTax(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_SECURITY_TRANSACTION_TAX));
			transactionMasterBO.setTransAmt(
					formatAmount(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_AMOUNT_OF_THE_TRANSACTION)));
			transactionMasterBO
					.setTransCharges(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_CHARGES));
			// transactionMasterBO.setTransSrc(feedData.get("src_of_txn"));
			transactionMasterBO
					.setTransSuffix(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_SUFFIX));
			// transactionMasterBO.setTransType(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_TYPE));
			transactionMasterBO
					.setTransUnits(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_UNITS_OF_THE_TRANSACTION));
			// transactionMasterBO.setTxnType(feedData.get("trxn_type_flag"));
			// String transNo =
			// feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_NUMBER).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_NUMBER).toString().trim() != null
					&& !feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_NUMBER).toString().trim()
							.isEmpty()) {
				transactionMasterBOList.add(transactionMasterBO);
				isRecordValid = true;
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return transactionMasterBOList;

	}

	private List<TransactionMasterBO> populateDBFSUNDARAMModelList(Map<String, String> feedData,
			TransactionMasterBODTO transactionMasterBODTO, List<TransactionMasterBO> transactionMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub
		try {
			TransactionMasterBO transactionMasterBO = mapper.map(transactionMasterBODTO, TransactionMasterBO.class);
			boolean isRecordValid = false;

			FinexaUtil finexaUtil = new FinexaUtil();

			transactionMasterBO.setAdvisorUser(advisorUser);

			transactionMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_AMC_CODE));
			transactionMasterBO
					.setApplicationNo(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_APPLICATION_NUMBER));
			// transactionMasterBO.setBasicTDS(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TDS_AMOUNT));
			transactionMasterBO
					.setBrokerageAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_BROKERAGE_AMOUNT));
			transactionMasterBO.setBrokeragePercentage(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_BROKERAGE_PERCENTAGE));
			transactionMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_DISTRIBUTOR_ARN_CODE));
			transactionMasterBO.setEuin(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_EUIN));
			transactionMasterBO.setFolioNo(
					formatNumbers(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_FOLIO_NUMBER)));
			transactionMasterBO
					.setInvestorId(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_INVESTOR_ID));
			// transactionMasterBO.setInvestorMin(feedData.get("inv_iin"));
			transactionMasterBO
					.setInvestorName(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_INVESTOR_NAME));
			transactionMasterBO
					.setInvestorPan(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_INVESTOR_PAN));
			/*
			 * transactionMasterBO.setLocation(feedData.get("location"));
			 */
			transactionMasterBO.setLocationCategory(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_LOCATION_CATEGORY));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Sundaram Mutual Fund");
			transactionMasterBO.setLookupRtabo(lookupRtabo);

			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TXN_TYPE).startsWith("P")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("P");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TXN_TYPE).startsWith("R")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("R");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TXN_TYPE).startsWith("SI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TXN_TYPE).startsWith("SO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("SO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TXN_TYPE).startsWith("TI")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TI");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TXN_TYPE).startsWith("TO")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("TO");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TXN_TYPE).startsWith("DR")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DR");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			} else if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TXN_TYPE).startsWith("DP")) {
				LookupTransactionRule lookupTransactionRule = lookupTransactionRuleRepository.findOne("DP");
				transactionMasterBO.setLookupTransactionRule(lookupTransactionRule);
			}

			/*
			 * transactionMasterBO.setMicrNo(feedData.get("micr_no"));
			 * transactionMasterBO.setMicrRemarks(feedData.get("remarks"));
			 * transactionMasterBO.setMultBrokerOption(feedData.get("mult_brok"));
			 * transactionMasterBO.setOldFolioNo(feedData.get("old_folio"));
			 */
			transactionMasterBO.setNav(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_NAV));
			transactionMasterBO.setProcessDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_PROCESS_DATE)));
			// transactionMasterBO.setReinvestFlag(feedData.get("reinvest_flag"));
			transactionMasterBO.setReportDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_REPORT_DATE)));
			transactionMasterBO
					.setReportTime(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_REPORT_TIME));
			transactionMasterBO
					.setSchemeName(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_SCHEME_NAME));
			transactionMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_SCHEME_NAME));

			transactionMasterBO
					.setSubBrokerARN(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_SUB_BROKER_ARN));

			transactionMasterBO
					.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_SUB_BROKER_CODE));

			transactionMasterBO.setTdsAmt(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TDS_AMOUNT));
			/*
			 * transactionMasterBO.setTicobPostedDate(feedData.get("ticob_posted_date"));
			 * transactionMasterBO.setTicobTransNo(feedData.get("ticob_trno"));
			 * transactionMasterBO.setTicobTransType(feedData.get("ticob_trtype"));
			 */
			transactionMasterBO.setTransactionDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TRANSACTION_DATE)));
			transactionMasterBO
					.setTransactionMode(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TRANSACTION_MODE));
			double transactionNumberDouble = Double
					.parseDouble(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TRANSACTION_NUMBER));
			String transactionNumberAsString = String.format("%.0f", transactionNumberDouble);
			transactionMasterBO
					.setTransactionNumber(formatNumbers(String.valueOf(Long.parseLong(transactionNumberAsString))));
			transactionMasterBO.setTransactionStatus(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TRANSACTION_STATUS));
			transactionMasterBO.setTransactionTax(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_SECURITY_TRANSACTION_TAX));
			transactionMasterBO.setTransAmt(formatAmount(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAMS_AMOUNT_OF_THE_TRANSACTION)));
			transactionMasterBO
					.setTransCharges(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TRANSACTION_CHARGES));
			// transactionMasterBO.setTransSrc(feedData.get("src_of_txn"));
			transactionMasterBO
					.setTransSuffix(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TRANSACTION_SUFFIX));
			transactionMasterBO
					.setTransType(feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TRANSACTION_TYPE_CODE));
			transactionMasterBO.setTransUnits(
					feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_UNITS_OF_THE_TRANSACTION));

			// String transNo =
			// feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_KARVY_TRANSACTION_NUMBER).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TRANSACTION_NUMBER).toString()
					.trim() != null
					&& !feedData.get(FinexaBOColumnConstant.TRANSACTION_DBF_SUNDARAM_TRANSACTION_NUMBER).toString().trim()
							.isEmpty()) {
				transactionMasterBOList.add(transactionMasterBO);
				isRecordValid = true;
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return transactionMasterBOList;

	}

	private synchronized UploadResponseDTO saveTransactionData(List<TransactionMasterBO> transactionMasterBODTOList,
			UploadResponseDTO uploadResponseDTO) {
		// TODO Auto-generated method stub

		try {
			System.out.println("Uploading*******************");
			if (transactionMasterBODTOList != null && transactionMasterBODTOList.size() > 0) {
				transactionMasterBORepository.save(transactionMasterBODTOList);
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