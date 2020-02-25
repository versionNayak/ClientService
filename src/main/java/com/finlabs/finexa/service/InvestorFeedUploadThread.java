package com.finlabs.finexa.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.Timestamp;
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
import com.finlabs.finexa.model.LookupRTAMasterFileDetailsBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.BackOfficeUploadHistoryRepository;
import com.finlabs.finexa.repository.InvestMasterBORepository;
import com.finlabs.finexa.repository.LookupRTABORepository;
import com.finlabs.finexa.repository.LookupRTAMasterFileDetailsBORepository;
import com.finlabs.finexa.repository.LookupTransactionRuleRepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.util.FinexaBOColumnConstant;
import com.finlabs.finexa.util.FinexaUtil;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

@Component

@Scope("prototype")

public class InvestorFeedUploadThread implements Runnable {

	@Autowired
	private Mapper mapper;

	@Autowired
	private InvestMasterBORepository investMasterBORepository;

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private LookupRTABORepository lookupRTABORepository;

	@Autowired
	private LookupTransactionRuleRepository lookupTransactionRuleRepository;

	@Autowired
	private LookupRTAMasterFileDetailsBORepository lookupRTAMasterFileDetailsBORepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	BackOfficeUploadHistoryRepository uploadHistoryRepo;

	@Autowired
	ClientMasterDataEntryService clientMasterDataEntryService;

	private static Logger log = LoggerFactory.getLogger(BackOfficeRTAFeedUploadServiceImpl.class);
	private static final Logger LOGGER = LoggerFactory.getLogger(FeedUploadThread.class);
	private InvestorMasterBODTO investorMasterBODTO;
	private UploadResponseDTO uploadResponseDTO;
	private BackOfficeUploadHistory backOfficeUploadHistory;
	private static final String RTA_FILE_TYPE_CAMS = "1";
	private static final String RTA_FILE_TYPE_KARVY = "2";
	private static final String RTA_FILE_TYPE_FRANKLIN = "3";
	private static final String RTA_FILE_TYPE_SUNDARAM = "4";
	private Properties prop = new Properties();
	private Integer rta, numberofPossibleColumns;
	private String rtaId;
	private String FEED_PROPERTIES_FILE;
	private int rows = 1, columnHeaderRow = 0, noOfRejectedRecords = 0;
	private List<Object[]> invPANFolioList = new ArrayList<Object[]>();
	private Map<String, InvestorMasterBO> invPANFolioMap = new HashMap<String, InvestorMasterBO>();
	private DBFField field = new DBFField();
	DBFReader dbfReader;
	private boolean isValueFound = false;
	public static final String STATUS_COMPLETED = "COMPLETED";
	public static final String STATUS_REJECTED = "REJECTED";

	public static final String STATUS_DONE = "DONE";
	public static final String STATUS_NOT_DONE = "NOT DONE";
	public static final String STATUS_NOT_APPLICABLE = "NOT APPLICABLE";
	public static final String STATUS_RUNNING = "RUNNING";
	public static final String FAILURE = "Failure";
	public static final String SUCCESS = "Success";
	public static final String RECORD_REJECTION_MESSAGE = "Investor Folio or PAN or both fields were missing in each record. Row numbers are: ";
	public static final String NOT_REJECTED_MESSAGE = "All records are successfully uploaded.";
	public static final String FILE_REJECTION_MESSAGE = "One or more columns were not found in file.";
	public static final String FILE_NOT_PROCESSED_MESSAGE = "Could not process the file.";
	private String rejectedRowNumbers = "";
	private Integer rejectedRowNumber = 0;
	private String autoCreationRespose;
	private boolean allRowsInserted = false;
	
	 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	 java.util.Date date=new java.util.Date();
	private final String CREATEDON= formatter.format(date);

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
			System.out.println("Started Investor Feed");
			log.debug("InvestorMasterBOServiceImpl >> uploadInvestorCAMSFeed");
			rta = investorMasterBODTO.getNameRTA();
			rtaId = rta.toString();
			String name = investorMasterBODTO.getNameSelectFile()[0].getOriginalFilename();
			if (name.contains(".dbf") || name.contains(".DBF")) {
				uploadResponseDTO = readDBFFeed(investorMasterBODTO, uploadResponseDTO);
				System.out.println("EXCEL UPLOADED");
			} else {
				uploadResponseDTO = readInvestorExcelFeed(investorMasterBODTO, uploadResponseDTO);
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
				backOfficeUploadHistory.setAutoClientCreationStatus(STATUS_NOT_APPLICABLE);
				backOfficeUploadHistory.setReasonOfRejection(FILE_REJECTION_MESSAGE);
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

			if ((backOfficeUploadHistory.getStatus()).equals(STATUS_COMPLETED)) {
				try {

					autoCreationRespose = clientMasterDataEntryService
							.generateAutoClient(investorMasterBODTO.getAdvisorId(),CREATEDON);
					if (autoCreationRespose.equals(SUCCESS)) {

						backOfficeUploadHistory.setAutoClientCreationStatus(STATUS_DONE);

					} else if (autoCreationRespose.equals(FAILURE)) {

						backOfficeUploadHistory.setAutoClientCreationStatus(STATUS_NOT_DONE);
						backOfficeUploadHistory.setReasonOfRejection(STATUS_NOT_APPLICABLE);

					} else {
						backOfficeUploadHistory.setAutoClientCreationStatus(STATUS_NOT_APPLICABLE);
						backOfficeUploadHistory.setReasonOfRejection(STATUS_NOT_APPLICABLE);

					}
					backOfficeUploadHistory = uploadHistoryRepo.save(backOfficeUploadHistory);
				} catch (Exception e) {
					e.printStackTrace();
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

	private UploadResponseDTO readInvestorExcelFeed(InvestorMasterBODTO investorMasterBODTO,
			UploadResponseDTO uploadResponseDTO) throws IOException, InvalidFormatException, ParseException {

		// null pointer handling

		InputStream initialStream = investorMasterBODTO.getNameSelectFile()[0].getInputStream();

		String uuid = UUID.randomUUID().toString();

		File directory = new File(System.getProperty("java.io.tmpdir"));
		File targetFile = File.createTempFile("WBR2_" + uuid, ".tmp", directory);

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
			FEED_PROPERTIES_FILE = "backOfficeProperties/investorCAMS.properties";
			break;
		case RTA_FILE_TYPE_KARVY:
			FEED_PROPERTIES_FILE = "backOfficeProperties/investorKarvy.properties";
			break;
		case RTA_FILE_TYPE_FRANKLIN:
			FEED_PROPERTIES_FILE = "backOfficeProperties/investorFranklin.properties";
			;
			break;
		/*
		 * case RTA_FILE_TYPE_SUNDARAM : FEED_PROPERTIES_FILE =
		 * "backOfficeProperties/investorSundaram.properties"; break;
		 */
		default:
			break;
		}

		ClassLoader classLoader = getClass().getClassLoader();
		FileReader fileReader = new FileReader(classLoader.getResource(FEED_PROPERTIES_FILE).getFile());
		prop.load(fileReader);
		// LookupRTAMasterFileDetailsBO lookupRTAMasterFileDetailsBO =
		// lookupRTAMasterFileDetailsBORepository.findByFileCode("INVM");

		// To get the actual column header
		columnHeaderRow = getColumnHeaderRowNum(sheet);

		Map<String, String> excelNameColNameMap = new HashMap<String, String>();
		Iterator<Row> rowIterator1 = sheet.rowIterator();
		while (rowIterator1.hasNext()) {
			Row row1 = rowIterator1.next();

			// To reach the actual column header
			if (row1.getRowNum() != columnHeaderRow)
				continue;
			for (String columnHeader : prop.stringPropertyNames()) {
				String value = prop.getProperty(columnHeader);
				String columnAddress = null;
				CellAddress addressOfCell = null;
				isValueFound = false;
				if (value.equals("2"))
					System.out.println(value);
				// Create a DataFormatter to format and get each cell's value as String
				DataFormatter dataFormatter = new DataFormatter();

				// To iterate over the columns of the current row
				Iterator<Cell> cellIterator1 = row1.cellIterator();

				while (cellIterator1.hasNext()) {
					Cell cell1 = cellIterator1.next();
					String cellValue1 = dataFormatter.formatCellValue(cell1);
					if (value.matches("[0-9]+")) {
						numberofPossibleColumns = Integer.parseInt(value);
						switch (rtaId) {

						case RTA_FILE_TYPE_CAMS:
							for (Integer iterator = 1; iterator <= numberofPossibleColumns; iterator++) {

								if (cellValue1.trim().equalsIgnoreCase(FinexaBOColumnConstant.investorCAMSColNameMap
										.get(columnHeader + iterator.toString()))) {
									addressOfCell = cell1.getAddress();
									value = columnHeader;
									isValueFound = true;
									break;
								}
							}
							break;
						case RTA_FILE_TYPE_KARVY:
							for (Integer iterator = 1; iterator <= numberofPossibleColumns; iterator++) {

								if (cellValue1.trim().equalsIgnoreCase(FinexaBOColumnConstant.investorKARVYColNameMap
										.get(columnHeader + iterator.toString()))) {
									addressOfCell = cell1.getAddress();
									value = columnHeader;
									isValueFound = true;
									break;
								}
							}
							break;
						/*
						 * case RTA_FILE_TYPE_FRANKLIN: for (Integer iterator = 1; iterator <=
						 * numberofPossibleColumns; iterator++) {
						 * 
						 * if
						 * (cellValue1.equalsIgnoreCase(FinexaBOColumnConstant.investorCAMSColNameMap.
						 * get(columnHeader + iterator.toString()))) { addressOfCell =
						 * cell1.getAddress(); value = columnHeader; isValueFound = true; break; } }
						 * break;
						 */
						default:
							break;
						}

						if (isValueFound == true)
							break;

					} else {
						if (cellValue1.equalsIgnoreCase(value)) {
							addressOfCell = cell1.getAddress();
							break;
						}
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
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_FOLIO_NUMBER);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_NAME);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_ADDRESS_LINE_1);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_CITY);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_PINCODE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_SCHEME_RTA_CODE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_SCHEME_NAME);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_EMAIL);
			// camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_MODE_OF_HOLDING);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_PAN);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_TAX_STATUS);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_DISTRIBUTOR_ARN_CODE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_DATE_OF_BIRTH);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_MOBILE_NUMBER);
			// camsMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_AMC_CODE);

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
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_FOLIO_NUMBER);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_INVESTOR_NAME);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_ADDRESS_LINE_1);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_CITY);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_PINCODE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_SCHEME_RTA_CODE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_SCHEME_NAME);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_EMAIL);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_INVESTOR_PAN);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_TAX_STATUS);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_DISTRIBUTOR_ARN_CODE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_INVESTOR_DATE_OF_BIRTH);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_MOBILE_NUMBER);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_MOBILE_NUMBER1);
			// karvyMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_AMC_CODE);

			for (Map.Entry<String, String> obj : excelNameColNameMap.entrySet()) {
				String key = obj.getKey();
				String value = obj.getValue();
				if (value.equals("nu") && karvyMandatoryFeedList.contains(key)) { // implies mandatory column null
					if (key.equals(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_MOBILE_NUMBER)
							|| key.equals(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_MOBILE_NUMBER1)) {
						continue;
					} else {
						uploadResponseDTO.setPrimaryKeyNotFound(true);
						System.out.println(key + " Address Null");
						uploadResponseDTO.setStatus(false);
						return uploadResponseDTO;
					}
				}
			}
			break;
		case RTA_FILE_TYPE_FRANKLIN:
			List<String> franklinMandatoryFeedList = new ArrayList<>();
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_FOLIO_NUMBER);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_INVESTOR_NAME);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_ADDRESS_LINE_1);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_CITY);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_PINCODE);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_EMAIL);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_INVESTOR_PAN);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_TAX_STATUS);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_DISTRIBUTOR_ARN_CODE);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_INVESTOR_DATE_OF_BIRTH);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_MOBILE_NUMBER);
			// franklinMandatoryFeedList.add(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_AMC_CODE);

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

		List<InvestorMasterBO> investorMasterBOList = new ArrayList<InvestorMasterBO>();

		// Empty the list each time after inserting 20 records
		int batchCount = 0;

		// try-catch
		AdvisorUser advisorUser = advisorUserRepository.findOne(investorMasterBODTO.getAdvisorId());

		// invPANFolioList =
		// investMasterBORepository.getdistinctTransNumberTransDateAndProcessDateSet(investorMasterBODTO.getAdvisorId());

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
					if (propertyKey.matches("[0-9]+")) {
						propertyKey = key;
					}
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
				System.out.println("LIST size() when batchCount is 20: " + investorMasterBOList.size());

				uploadResponseDTO = saveInvestorData(investorMasterBOList, uploadResponseDTO);
				investorMasterBOList = new ArrayList<InvestorMasterBO>();
				batchCount = 0;

				switch (rtaId) {
				case RTA_FILE_TYPE_CAMS:
					investorMasterBOList = populateInvestorCAMSModelList(feed, investorMasterBODTO,
							investorMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_KARVY:
					investorMasterBOList = populateInvestorKarvyModelList(feed, investorMasterBODTO,
							investorMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_FRANKLIN:
					investorMasterBOList = populateInvestorFranklinModelList(feed, investorMasterBODTO,
							investorMasterBOList, advisorUser);
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
					investorMasterBOList = populateInvestorCAMSModelList(feed, investorMasterBODTO,
							investorMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_KARVY:
					investorMasterBOList = populateInvestorKarvyModelList(feed, investorMasterBODTO,
							investorMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_FRANKLIN:
					investorMasterBOList = populateInvestorFranklinModelList(feed, investorMasterBODTO,
							investorMasterBOList, advisorUser);
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
			System.out.println("LIST size() when LAST batchCount is 20: " + investorMasterBOList.size());
			uploadResponseDTO = saveInvestorData(investorMasterBOList, uploadResponseDTO);
			allRowsInserted = true;

		}
		targetFile.delete();
		// workbook.close();
		uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		return uploadResponseDTO;

	}

	private List<InvestorMasterBO> populateInvestorCAMSModelList(Map<String, String> feedData,
			InvestorMasterBODTO investorMasterBODTO, List<InvestorMasterBO> investorMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub
		boolean isRecordValid = false;
		try {
			InvestorMasterBO investorMasterBO = mapper.map(investorMasterBODTO, InvestorMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();

			InvestorMasterBOPK investorMasterBOPK = new InvestorMasterBOPK();
			investorMasterBOPK.setFolioNumber(
					formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_FOLIO_NUMBER)));

			investorMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_NAME));
			investorMasterBO.setAddressLine1(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_ADDRESS_LINE_1));
			investorMasterBO.setAddressLine2(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_ADDRESS_LINE_2));
			investorMasterBO.setAddressLine3(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_ADDRESS_LINE_3));
			investorMasterBO.setCity(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_CITY));
			investorMasterBO.setPincode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_PINCODE));
			investorMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_SCHEME_RTA_CODE));

			investorMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_SCHEME_NAME));
			investorMasterBO.setBalanceDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_BALANCE_DATE))));

			investorMasterBO.setUnitBalance(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_UNIT_BALANCE));
			investorMasterBO.setRupeeBalance(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_RUPEE_BALANCE));

			investorMasterBO.setJointName1(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_JOINT_NAME1));
			investorMasterBO.setJointName2(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_JOINT_NAME2));
			investorMasterBO.setPhoneOffice(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_PHONE_OFFICE));
			investorMasterBO
					.setPhoneResidence(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_PHONE_RESIDENCE));
			investorMasterBO.setEmail(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_EMAIL));
			investorMasterBO.setModeOfHolding(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_MODE_OF_HOLDING));
			investorMasterBO.setUin(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_UIN));
			investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_PAN));
			investorMasterBO.setJoint1PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_JOINT1_PAN));
			investorMasterBO.setJoint2PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_JOINT2_PAN));
			investorMasterBO.setGuardianPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_GUARDIAN_PAN));
			String panCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_PAN)).toString().trim();
			String guardianPanCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_PAN)).toString().trim();
			if (panCheck != null && !panCheck.isEmpty() && panCheck.matches(".*\\d.*")) {
			} else {
				if (guardianPanCheck != null && !guardianPanCheck.isEmpty() && guardianPanCheck.matches(".*\\d.*")) {
					investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_GUARDIAN_PAN));

				}
			}
			investorMasterBO.setTaxStatus(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_TAX_STATUS));
			investorMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_DISTRIBUTOR_ARN_CODE));
			investorMasterBO.setSubbrokerDealerCode(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_SUBBROKER_DEALER_CODE));
			investorMasterBO.setReinvFlag(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_REINV_FLAG));
			investorMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_BANK_NAME));
			investorMasterBO.setBankBranch(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_BANK_BRANCH));
			investorMasterBO.setBankAccountType(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_BANK_AC_TYPE));
			investorMasterBO.setBankAccountNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_BANK_AC_NO));
			investorMasterBO.setBankAddress1(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_BANK_ADDRESS_1));
			investorMasterBO.setBankAddress2(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_BANK_ADDRESS_2));
			investorMasterBO.setBankAddress3(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_BANK_ADDRESS_3));
			investorMasterBO.setBankCity(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_BANK_CITY));
			investorMasterBO.setInvestorDOB(finexaUtil.formatDate(finexaUtil.formatStringDate(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_DATE_OF_BIRTH))));

			investorMasterBO.setBankPincode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_BANK_PINCODE));
			investorMasterBO.setMobileNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_MOBILE_NUMBER));
			investorMasterBO.setOccupation(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_OCCUPATION));
			investorMasterBO.setInvestorMin(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_MIN));

			investorMasterBO.setNomineeName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE_NAME));
			investorMasterBO
					.setNomineeRelation(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE_RELATION));
			investorMasterBO
					.setNomineeAddress1(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE_ADDRESS1));
			investorMasterBO
					.setNomineeAddress2(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE_ADDRESS2));
			investorMasterBO
					.setNomineeAddress3(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE_ADDRESS3));
			investorMasterBO.setNomineeCity(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE_CITY));
			investorMasterBO.setNomineeState(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE_STATE));
			investorMasterBO
					.setNomineePincode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE_PINCODE));
			investorMasterBO.setNomineePhoneOffice(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE_PHONE_OFFICE));
			investorMasterBO.setNomineePhoneResidence(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE_PHONE_RESIDENCE));
			investorMasterBO.setNomineeEmail(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE_EMAIL));
			investorMasterBO
					.setNomineePercentage(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE_PERCENTAGE));
			investorMasterBO.setNominee2Name(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE2_NAME));
			investorMasterBO
					.setNominee2Relation(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE2_RELATION));
			investorMasterBO.setNominee2Address1(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_EXCEL_CAMS_NOMINEE2_ADDRESS1));
			investorMasterBO
					.setNominee2Address2(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE2_ADDRESS2));
			investorMasterBO
					.setNominee2Address3(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE2_ADDRESS3));
			investorMasterBO.setNominee2City(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE2_CITY));
			investorMasterBO.setNominee2State(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE2_STATE));
			investorMasterBO
					.setNominee2Pincode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE2_PINCODE));
			investorMasterBO.setNominee2PhoneOffice(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE2_PHONE_OFFICE));
			investorMasterBO.setNominee2PhoneResidence(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE2_PHONE_RESIDENCE));
			investorMasterBO.setNominee2Email(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE2_EMAIL));
			investorMasterBO.setNominee2Percentage(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE2_PERCENTAGE));
			investorMasterBO.setNominee3Name(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE3_NAME));
			investorMasterBO
					.setNominee3Relation(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE3_RELATION));
			investorMasterBO
					.setNominee3Address1(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE3_ADDRESS1));
			investorMasterBO
					.setNominee3Address2(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE3_ADDRESS2));
			investorMasterBO
					.setNominee3Address3(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE3_ADDRESS3));
			investorMasterBO.setNominee3City(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE3_CITY));
			investorMasterBO.setNominee3State(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE3_STATE));
			investorMasterBO
					.setNominee3Pincode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE3_PINCODE));
			investorMasterBO.setNominee3PhoneOffice(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE3_PHONE_OFFICE));
			investorMasterBO.setNominee3PhoneResidence(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE3_PHONE_RESIDENCE));
			investorMasterBO.setNominee3Email(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE3_EMAIL));
			investorMasterBO.setNominee3Percentage(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NOMINEE3_PERCENTAGE));
			investorMasterBO.setIfscCode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_IFSC_CODE));
			investorMasterBO.setDpId(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_DP_ID));
			investorMasterBO.setDemat(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_DEMAT));
			investorMasterBO.setGuardianName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_NAME_OF_GUARDIAN));
			investorMasterBO
					.setBrokerDealerCode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_BROKER_DEALER_CODE));
			investorMasterBO.setFolioCreateDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_FOLIO_CREATE_DATE))));
			investorMasterBO.setInvestorAdhaarNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_AADHAAR_NUMBER));

			investorMasterBO.setTpaLinked(feedData.get("TPA_LINKED"));

			investorMasterBO.setFirstHolderCKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_FIRST_HOLDER_CKYC));
			investorMasterBO.setJointHolder1CKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_JOINT_HOLDER1_CKYC));
			investorMasterBO.setJointHolder2CKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_JOINT_HOLDER2_CKYC));
			investorMasterBO
					.setGuardianCKYCNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_GUARDIAN_CKYC));

			investorMasterBO
					.setJointHolder1DOB(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("JH1_DOB"))));
			investorMasterBO
					.setJointHolder2DOB(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("JH2_DOB"))));
			investorMasterBO
					.setGuardianDOB(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("GUARDIAN_D"))));

			investorMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_AMC_CODE));

			investorMasterBO.setGstStateCode(feedData.get("GST_STATE_"));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");

			investorMasterBO.setLookupRtabo(lookupRtabo);
			investorMasterBO.setAdvisorUser(advisorUser);
			investorMasterBO.setId(investorMasterBOPK);
			//String pan = (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_PAN)).toString().trim();
			String guardianPan = null;
			if (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_GUARDIAN_PAN) != null) {
				 guardianPan = (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_GUARDIAN_PAN)).toString().trim();
			}
			//String folioNo = (formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_FOLIO_NUMBER))).toString().trim();
			if ((feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_PAN)).toString().trim() != null && !(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_PAN)).toString().trim().isEmpty() && (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_INVESTOR_PAN)).toString().trim().matches(".*\\d.*")) {
				if ((formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_FOLIO_NUMBER))).toString().trim() != null && !(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_FOLIO_NUMBER))).toString().trim().isEmpty()) {
					investorMasterBOList.add(investorMasterBO);
					isRecordValid = true;
				}
			} else {
				if (guardianPan != null && !guardianPan.isEmpty() && guardianPan.matches(".*\\d.*")) {
					if ((formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_FOLIO_NUMBER))).toString().trim() != null && !(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_CAMS_FOLIO_NUMBER))).toString().trim().isEmpty()) {
						investorMasterBOList.add(investorMasterBO);
						isRecordValid = true;
					}
				}
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return investorMasterBOList;

	}

	private List<InvestorMasterBO> populateInvestorKarvyModelList(Map<String, String> feedData,
			InvestorMasterBODTO investorMasterBODTO, List<InvestorMasterBO> investorMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub
		boolean isRecordValid = false;
		try {
			InvestorMasterBO investorMasterBO = mapper.map(investorMasterBODTO, InvestorMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();

			InvestorMasterBOPK investorMasterBOPK = new InvestorMasterBOPK();
			investorMasterBOPK.setFolioNumber(
					formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_FOLIO_NUMBER)));
			investorMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_INVESTOR_NAME));
			investorMasterBO.setAddressLine1(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_ADDRESS_LINE_1));
			investorMasterBO.setAddressLine2(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_ADDRESS_LINE_2));
			investorMasterBO.setAddressLine3(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_ADDRESS_LINE_3));
			investorMasterBO.setCity(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_CITY));
			investorMasterBO.setPincode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_PINCODE));
			investorMasterBO
					.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_SCHEME_RTA_CODE));
			investorMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_SCHEME_NAME));

			investorMasterBO.setJointName1(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_JOINT_NAME1));
			investorMasterBO.setJointName2(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_JOINT_NAME2));
			investorMasterBO.setPhoneOffice(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_PHONE_OFFICE));
			investorMasterBO
					.setPhoneResidence(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_PHONE_RESIDENCE));
			investorMasterBO.setEmail(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_EMAIL));

			investorMasterBO.setModeOfHolding(feedData.get("Mode of Holding Description"));

			investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_INVESTOR_PAN));
			investorMasterBO.setJoint1PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_JOINT1_PAN));
			investorMasterBO.setJoint2PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_JOINT2_PAN));
			investorMasterBO.setGuardianPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_GUARDIAN_PAN));
			String panCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_INVESTOR_PAN)).toString().trim();
			String guardianPanCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_INVESTOR_PAN)).toString().trim();
			if (panCheck != null && !panCheck.isEmpty() && panCheck.matches(".*\\d.*")) {
			} else {
				if (guardianPanCheck != null && !guardianPanCheck.isEmpty() && guardianPanCheck.matches(".*\\d.*")) {
					investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_GUARDIAN_PAN));

				}
			}
			investorMasterBO.setTaxStatus(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_TAX_STATUS));
			investorMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_DISTRIBUTOR_ARN_CODE));

			investorMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_BANK_NAME));
			investorMasterBO.setBankBranch(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_BANK_BRANCH));
			investorMasterBO.setBankAccountType(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_BANK_AC_TYPE));
			investorMasterBO.setBankAccountNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_BANK_AC_NO));
			investorMasterBO.setBankAddress1(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_BANK_ADDRESS_1));
			investorMasterBO.setBankAddress2(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_BANK_ADDRESS_2));
			investorMasterBO.setBankAddress3(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_BANK_ADDRESS_3));
			investorMasterBO.setBankCity(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_BANK_CITY));
			investorMasterBO.setInvestorDOB(finexaUtil.formatDate(finexaUtil.formatStringDate(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_INVESTOR_DATE_OF_BIRTH))));
			investorMasterBO.setMobileNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_MOBILE_NUMBER));
			investorMasterBO.setNomineeName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_NOMINEE_NAME));

			investorMasterBO.setNominee2Name(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_NOMINEE2_NAME));
			investorMasterBO.setNominee3Name(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_NOMINEE3_NAME));

			investorMasterBO.setIfscCode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_IFSC_CODE));
			investorMasterBO.setDpId(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_DP_ID));
			investorMasterBO
					.setGuardianName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_NAME_OF_GUARDIAN));
			investorMasterBO
					.setBrokerDealerCode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_BROKER_DEALER_CODE));

			investorMasterBO.setFirstHolderCKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_FIRST_HOLDER_CKYC));
			investorMasterBO.setJointHolder1CKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_JOINT_HOLDER1_CKYC));
			investorMasterBO.setJointHolder2CKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_JOINT_HOLDER2_CKYC));
			investorMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_AMC_CODE));
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			investorMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(investorMasterBODTO.getAdvisorId());
			investorMasterBO.setAdvisorUser(advUser);
			investorMasterBO.setId(investorMasterBOPK);
			//String pan = (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_INVESTOR_PAN)).toString().trim();
			String guardianPan = null;
			if (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_GUARDIAN_PAN) != null) {
				 guardianPan = (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_GUARDIAN_PAN)).toString().trim();
			}
			//String folioNo = (formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_FOLIO_NUMBER))).toString().trim();
			if ((feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_INVESTOR_PAN)).toString().trim() != null && !(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_INVESTOR_PAN)).toString().trim().isEmpty() && (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_INVESTOR_PAN)).toString().trim().matches(".*\\d.*")) {
				if ((formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_FOLIO_NUMBER))).toString().trim() != null && !(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_FOLIO_NUMBER))).toString().trim().isEmpty()) {
					investorMasterBOList.add(investorMasterBO);
					isRecordValid = true;
				}
			}else {
				if (guardianPan != null && !guardianPan.isEmpty() && guardianPan.matches(".*\\d.*")) {
					if ((formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_FOLIO_NUMBER))).toString().trim() != null && !(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_KARVY_FOLIO_NUMBER))).toString().trim().isEmpty()) {
						investorMasterBOList.add(investorMasterBO);
						isRecordValid = true;
					}
				}
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return investorMasterBOList;

	}

	private List<InvestorMasterBO> populateInvestorFranklinModelList(Map<String, String> feedData,
			InvestorMasterBODTO investorMasterBODTO, List<InvestorMasterBO> investorMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub
		boolean isRecordValid = false;
		try {
			InvestorMasterBO investorMasterBO = mapper.map(investorMasterBODTO, InvestorMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();

			InvestorMasterBOPK investorMasterBOPK = new InvestorMasterBOPK();
			investorMasterBOPK.setFolioNumber(
					formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_FOLIO_NUMBER)));
			investorMasterBO
					.setInvestorName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_INVESTOR_NAME));
			investorMasterBO
					.setAddressLine1(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_ADDRESS_LINE_1));
			investorMasterBO
					.setAddressLine2(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_ADDRESS_LINE_2));
			investorMasterBO
					.setAddressLine3(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_ADDRESS_LINE_3));
			investorMasterBO.setCity(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_CITY));
			investorMasterBO.setPincode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_PINCODE));
			investorMasterBO.setJointName1(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_JOINT_NAME1));
			investorMasterBO.setJointName2(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_JOINT_NAME2));
			investorMasterBO.setPhoneOffice(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_PHONE_OFFICE));
			investorMasterBO
					.setPhoneResidence(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_PHONE_RESIDENCE));
			investorMasterBO.setEmail(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_EMAIL));
			investorMasterBO
					.setModeOfHolding(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_MODE_OF_HOLDING));
			investorMasterBOPK
					.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_INVESTOR_PAN));
			investorMasterBO.setJoint1PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_JOINT1_PAN));
			investorMasterBO.setJoint2PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_JOINT2_PAN));
			investorMasterBO.setGuardianPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_GUARDIAN_PAN));
			String panCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_INVESTOR_PAN)).toString().trim();
			String guardianPanCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_INVESTOR_PAN)).toString().trim();
			if (panCheck != null && !panCheck.isEmpty() && panCheck.matches(".*\\d.*")) {
			} else {
				if (guardianPanCheck != null && !guardianPanCheck.isEmpty() && guardianPanCheck.matches(".*\\d.*")) {
					investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_GUARDIAN_PAN));

				}
			}
			investorMasterBO.setTaxStatus(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_TAX_STATUS));
			investorMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_DISTRIBUTOR_ARN_CODE));
			investorMasterBO.setSubbrokerDealerCode(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_SUBBROKER_DEALER_CODE));
			investorMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_BANK_NAME));
			investorMasterBO.setBankBranch(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_BANK_BRANCH));
			investorMasterBO
					.setBankAccountType(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_BANK_AC_TYPE));
			investorMasterBO
					.setBankAccountNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_BANK_AC_NO));
			investorMasterBO
					.setBankAddress1(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_BANK_ADDRESS_1));
			investorMasterBO
					.setBankAddress2(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_BANK_ADDRESS_2));
			investorMasterBO
					.setBankAddress3(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_BANK_ADDRESS_3));
			investorMasterBO.setBankCity(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_BANK_CITY));
			investorMasterBO.setPincode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_BANK_PINCODE));
			investorMasterBO.setInvestorDOB(finexaUtil
					.formatDate(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_INVESTOR_DATE_OF_BIRTH)));
			investorMasterBO
					.setMobileNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_MOBILE_NUMBER));

			investorMasterBO.setNomineeName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_NOMINEE_NAME));
			investorMasterBO
					.setNominee2Name(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_NOMINEE2_NAME));
			investorMasterBO
					.setNominee3Name(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_NOMINEE3_NAME));
			investorMasterBO.setIfscCode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_IFSC_CODE));
			investorMasterBO.setDpId(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_DP_ID));
			investorMasterBO
					.setGuardianName(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_NAME_OF_GUARDIAN));
			investorMasterBO.setBrokerDealerCode(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_BROKER_DEALER_CODE));

			investorMasterBO.setFolioCreateDate(finexaUtil
					.formatDate(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_FOLIO_CREATE_DATE)));
			investorMasterBO.setFirstHolderCKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_FIRST_HOLDER_CKYC));
			investorMasterBO.setJointHolder1CKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_JOINT_HOLDER1_CKYC));
			investorMasterBO.setJointHolder2CKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_JOINT_HOLDER2_CKYC));
			investorMasterBO
					.setGuardianCKYCNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_GUARDIAN_CKYC));
			investorMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_AMC_CODE));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			investorMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(investorMasterBODTO.getAdvisorId());
			investorMasterBO.setAdvisorUser(advUser);
			investorMasterBO.setId(investorMasterBOPK);
			//String pan = (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_INVESTOR_PAN)).toString().trim();
			String guardianPan = null;
			if (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_GUARDIAN_PAN) != null) {
				guardianPan = (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_GUARDIAN_PAN)).toString().trim();
	
			}
			//String folioNo = (formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_FOLIO_NUMBER))).toString().trim();
			if ((feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_INVESTOR_PAN)).toString().trim() != null && !(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_INVESTOR_PAN)).toString().trim().isEmpty() && (feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_INVESTOR_PAN)).toString().trim().matches(".*\\d.*")) {
				if ((formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_FOLIO_NUMBER))).toString().trim() != null && !(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_FOLIO_NUMBER))).toString().trim().isEmpty()) {
					investorMasterBOList.add(investorMasterBO);
					isRecordValid = true;
				}
			} else {
				if (guardianPan != null && !guardianPan.isEmpty() && guardianPan.matches(".*\\d.*")) {
					if ((formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_FOLIO_NUMBER))).toString().trim() != null && !(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_EXCEL_FRANKLIN_FOLIO_NUMBER))).toString().trim().isEmpty()) {
						investorMasterBOList.add(investorMasterBO);
						isRecordValid = true;
					}
				}
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return investorMasterBOList;

	}

	private UploadResponseDTO readDBFFeed(InvestorMasterBODTO investorMasterBODTO, UploadResponseDTO uploadResponseDTO)
			throws IOException, InvalidFormatException, ParseException {

		InputStream initialStream = investorMasterBODTO.getNameSelectFile()[0].getInputStream();
		InputStream initialStream2 = investorMasterBODTO.getNameSelectFile()[0].getInputStream();
		dbfReader = new DBFReader(initialStream);
		boolean headerFound = false, firstRow;
		int numberOfFields;
		String headerPropFile, columnPropFile;
		Properties prop1 = new Properties();
		Object[] rowObjects1, rowObjects2;
		int batchCount = 0, counter = 0, headerRowCount = 0, row, numberOfRecords = 0, numberOfRecordRunning = 0;
		List<InvestorMasterBO> investorMasterBOList = new ArrayList<InvestorMasterBO>();
		List<String> fileColNames = new ArrayList<String>();
		List<String> propColNames = new ArrayList<String>();
		Map<String, String> dataMap = new HashMap<>();
		Map<String, String> investorFeedMap = new HashMap<>();

		// Number of columns in the DBF file
		numberOfFields = dbfReader.getFieldCount();
		numberOfRecords = dbfReader.getRecordCount();
		headerPropFile = "backOfficeProperties/investorDBF.properties";
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
			investorFeedMap.put(i.toString(), fileColname);
		}
		prop1 = new Properties();
		switch (rtaId) {

		case RTA_FILE_TYPE_CAMS:
			columnPropFile = "backOfficeProperties/investorCAMSDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case RTA_FILE_TYPE_KARVY:
			columnPropFile = "backOfficeProperties/investorKarvyDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case RTA_FILE_TYPE_FRANKLIN:
			columnPropFile = "backOfficeProperties/investorFranklinDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case RTA_FILE_TYPE_SUNDARAM:
			columnPropFile = "backOfficeProperties/investorSundaramDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		default:
			break;

		}

		for (Map.Entry<String, String> entry : investorFeedMap.entrySet()) {
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

		AdvisorUser advisorUser = advisorUserRepository.findOne(investorMasterBODTO.getAdvisorId());
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
				dataMap.put(investorFeedMap.get(fieldIndex.toString()),
						String.valueOf(rowObjects2[columnIndex]).trim());

			}

			if (batchCount == 20) {
				System.out.println("InvestorMasterBOList size() when batchCount is 20: " + investorMasterBOList.size());
				uploadResponseDTO = saveInvestorData(investorMasterBOList, uploadResponseDTO);
				investorMasterBOList = new ArrayList<InvestorMasterBO>();
				batchCount = 0;
				counter++;
				System.out.println("Counter displayed" + counter);
				System.out.println("*********************************");

				switch (rtaId) {

				case RTA_FILE_TYPE_CAMS:
					investorMasterBOList = populateDBFCAMSModelList(dataMap, investorMasterBODTO, investorMasterBOList,
							advisorUser);
					break;

				case RTA_FILE_TYPE_KARVY:
					investorMasterBOList = populateDBFKARVYModelList(dataMap, investorMasterBODTO, investorMasterBOList,
							advisorUser);
					break;

				case RTA_FILE_TYPE_FRANKLIN:
					investorMasterBOList = populateDBFFranklinModelList(dataMap, investorMasterBODTO,
							investorMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_SUNDARAM:
					investorMasterBOList = populateDBFSUNDARAMModelList(dataMap, investorMasterBODTO,
							investorMasterBOList, advisorUser);
					break;
				default:
					break;

				}

				batchCount++;

			} else {

				switch (rtaId) {
				case RTA_FILE_TYPE_CAMS:
					investorMasterBOList = populateDBFCAMSModelList(dataMap, investorMasterBODTO, investorMasterBOList,
							advisorUser);
					break;

				case RTA_FILE_TYPE_KARVY:
					investorMasterBOList = populateDBFKARVYModelList(dataMap, investorMasterBODTO, investorMasterBOList,
							advisorUser);
					break;

				case RTA_FILE_TYPE_FRANKLIN:
					investorMasterBOList = populateDBFFranklinModelList(dataMap, investorMasterBODTO,
							investorMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_SUNDARAM:
					investorMasterBOList = populateDBFSUNDARAMModelList(dataMap, investorMasterBODTO,
							investorMasterBOList, advisorUser);
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
			System.out.println("investorMasterBOList size() before last: " + investorMasterBOList.size());
			uploadResponseDTO = saveInvestorData(investorMasterBOList, uploadResponseDTO);
		}
		// System.out.println(dataMap);

		uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		return uploadResponseDTO;

	}

	private List<InvestorMasterBO> populateDBFCAMSModelList(Map<String, String> feedData,
			InvestorMasterBODTO investorMasterBODTO, List<InvestorMasterBO> investorMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordValid = false;
			InvestorMasterBO investorMasterBO = mapper.map(investorMasterBODTO, InvestorMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			InvestorMasterBOPK investorMasterBOPK = new InvestorMasterBOPK();
			investorMasterBOPK
					.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_FOLIO_NUMBER)));
			investorMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_NAME));
			investorMasterBO.setAddressLine1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_ADDRESS_LINE_1));
			investorMasterBO.setAddressLine2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_ADDRESS_LINE_2));
			investorMasterBO.setAddressLine3(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_ADDRESS_LINE_3));
			investorMasterBO.setCity(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_CITY));
			investorMasterBO.setPincode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_PINCODE));
			investorMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_SCHEME_RTA_CODE));
			investorMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_SCHEME_NAME));
			investorMasterBO
					.setBalanceDate(utilToSQLDate(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_BALANCE_DATE)));
			investorMasterBO.setUnitBalance(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_UNIT_BALANCE));
			investorMasterBO.setRupeeBalance(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_RUPEE_BALANCE));
			investorMasterBO.setJointName1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_JOINT_NAME1));
			investorMasterBO.setJointName2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_JOINT_NAME2));
			investorMasterBO.setPhoneOffice(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_PHONE_OFFICE));
			investorMasterBO.setPhoneResidence(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_PHONE_RESIDENCE));
			investorMasterBO.setEmail(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_EMAIL));
			investorMasterBO.setModeOfHolding(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_MODE_OF_HOLDING));
			investorMasterBO.setUin(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_UIN));
			investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_PAN));
			investorMasterBO.setJoint1PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_JOINT1_PAN));
			investorMasterBO.setJoint2PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_JOINT2_PAN));
			investorMasterBO.setGuardianPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_GUARDIAN_PAN));
			String panCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_PAN)).toString().trim();
			String guardianPanCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_PAN)).toString().trim();
			if (panCheck != null && !panCheck.isEmpty() && panCheck.matches(".*\\d.*")) {
			} else {
				if (guardianPanCheck != null && !guardianPanCheck.isEmpty() && guardianPanCheck.matches(".*\\d.*")) {
					investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_GUARDIAN_PAN));

				}
			}
			investorMasterBO.setTaxStatus(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_TAX_STATUS));
			investorMasterBO
					.setDistributorARNCode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_DISTRIBUTOR_ARN_CODE));
			investorMasterBO.setSubbrokerDealerCode(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_SUBBROKER_DEALER_CODE));
			investorMasterBO.setReinvFlag(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_REINV_FLAG));
			investorMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_BANK_NAME));
			investorMasterBO.setBankBranch(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_BANK_BRANCH));
			investorMasterBO.setBankAccountType(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_BANK_AC_TYPE));
			investorMasterBO.setBankAccountNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_BANK_AC_NO));
			investorMasterBO.setBankAddress1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_BANK_ADDRESS_1));
			investorMasterBO.setBankAddress2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_BANK_ADDRESS_2));
			investorMasterBO.setBankAddress3(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_BANK_ADDRESS_3));
			investorMasterBO.setBankCity(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_BANK_CITY));
			// System.out.println("DOB: " +
			// feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_DATE_OF_BIRTH));
			// if(!feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_DATE_OF_BIRTH).trim().isEmpty()
			// &&
			// !feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_DATE_OF_BIRTH).trim().isBlank()
			// &&
			// !feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_DATE_OF_BIRTH).trim().equals("null"))
			// {
			investorMasterBO.setInvestorDOB(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_DATE_OF_BIRTH)));
			// }

			investorMasterBO.setMobileNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_MOBILE_NUMBER));
			investorMasterBO.setOccupation(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_OCCUPATION));
			investorMasterBO.setInvestorMin(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_MIN));
			investorMasterBO.setNomineeName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE_NAME));
			investorMasterBO
					.setNomineeRelation(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE_RELATION));
			investorMasterBO
					.setNomineeAddress1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE_ADDRESS1));
			investorMasterBO
					.setNomineeAddress2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE_ADDRESS2));
			investorMasterBO
					.setNomineeAddress3(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE_ADDRESS3));
			investorMasterBO.setNomineeCity(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE_CITY));
			investorMasterBO.setNomineeState(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE_STATE));
			investorMasterBO.setNomineePincode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE_PINCODE));
			investorMasterBO
					.setNomineePhoneOffice(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE_PHONE_OFFICE));
			investorMasterBO.setNomineePhoneResidence(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE_PHONE_RESIDENCE));
			investorMasterBO.setNomineeEmail(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE_EMAIL));
			investorMasterBO
					.setNomineePercentage((feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE_PERCENTAGE)));
			investorMasterBO.setNominee2Name(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE2_NAME));
			investorMasterBO
					.setNominee2Relation(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE2_RELATION));
			investorMasterBO.setNominee2Address1(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_DBF_FRANKLIN_NOMINEE2_ADDRESS1));
			investorMasterBO
					.setNominee2Address2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE2_ADDRESS2));
			investorMasterBO
					.setNominee2Address3(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE2_ADDRESS3));
			investorMasterBO.setNominee2City(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE2_CITY));
			investorMasterBO.setNominee2State(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE2_STATE));
			investorMasterBO
					.setNominee2Pincode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE2_PINCODE));
			investorMasterBO.setNominee2PhoneOffice(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE2_PHONE_OFFICE));
			investorMasterBO.setNominee2PhoneResidence(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE2_PHONE_RESIDENCE));
			investorMasterBO.setNominee2Email(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE2_EMAIL));
			investorMasterBO
					.setNominee2Percentage(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE2_PERCENTAGE));
			investorMasterBO.setNominee3Name(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE3_NAME));
			investorMasterBO
					.setNominee3Relation(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE3_RELATION));
			investorMasterBO
					.setNominee3Address1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE3_ADDRESS1));
			investorMasterBO
					.setNominee3Address2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE3_ADDRESS2));
			investorMasterBO
					.setNominee3Address3(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE3_ADDRESS3));
			investorMasterBO.setNominee3City(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE3_CITY));
			investorMasterBO.setNominee3State(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE3_STATE));
			investorMasterBO
					.setNominee3Pincode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE3_PINCODE));
			investorMasterBO.setNominee3PhoneOffice(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE3_PHONE_OFFICE));
			investorMasterBO.setNominee3PhoneResidence(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE3_PHONE_RESIDENCE));
			investorMasterBO.setNominee3Email(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE3_EMAIL));
			investorMasterBO
					.setNominee3Percentage(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NOMINEE3_PERCENTAGE));
			investorMasterBO.setIfscCode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_IFSC_CODE));
			investorMasterBO.setDpId(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_DP_ID));
			investorMasterBO.setDemat(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_DEMAT));
			investorMasterBO.setGuardianName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_NAME_OF_GUARDIAN));
			investorMasterBO
					.setBrokerDealerCode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_BROKER_DEALER_CODE));
			investorMasterBO.setFolioCreateDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_FOLIO_CREATE_DATE)));
			investorMasterBO.setInvestorAdhaarNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_AADHAAR_NUMBER));
			// investorMasterBO.setTpaLinked(feedData.get("TPA_LINKED"));
			investorMasterBO
					.setFirstHolderCKYCNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_FIRST_HOLDER_CKYC));
			investorMasterBO.setJointHolder1CKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_JOINT_HOLDER1_CKYC));
			investorMasterBO.setJointHolder2CKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_JOINT_HOLDER2_CKYC));
			investorMasterBO
					.setGuardianCKYCNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_GUARDIAN_CKYC));
			/*
			 * investorMasterBO
			 * .setJointHolder1DOB(finexaUtil.formatDate(finexaUtil.formatStringDate(
			 * feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_FOLIO_NUMBER))));
			 * investorMasterBO
			 * .setJointHolder2DOB(finexaUtil.formatDate(finexaUtil.formatStringDate(
			 * feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_FOLIO_NUMBER))));
			 * investorMasterBO
			 * .setGuardianDOB(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.
			 * get(FinexaBOColumnConstant.AUM_CAMS_DBF_FOLIO_NUMBER))));
			 */
			investorMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_AMC_CODE));
			// investorMasterBO.setGstStateCode(feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_FOLIO_NUMBER));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");
			investorMasterBO.setLookupRtabo(lookupRtabo);
			investorMasterBO.setAdvisorUser(advisorUser);
			investorMasterBO.setId(investorMasterBOPK);

			//String pan = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_PAN)).toString().trim();
			String guardianPan = null;
			if (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_GUARDIAN_PAN) != null) {
				 guardianPan = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_GUARDIAN_PAN)).toString().trim();

			}
			//String folioNo = (formatNumbers(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_FOLIO_NUMBER))).toString().trim());
			if ((feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_PAN)).toString().trim() != null && !(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_PAN)).toString().trim().isEmpty() && (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_INVESTOR_PAN)).toString().trim().matches(".*\\d.*")) {
				if ((formatNumbers(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_FOLIO_NUMBER))).toString().trim()) != null && !(formatNumbers(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_FOLIO_NUMBER))).toString().trim()).isEmpty()) {
					investorMasterBOList.add(investorMasterBO);
					isRecordValid = true;
				}
			} else {
				if (guardianPan != null && !guardianPan.isEmpty() && guardianPan.matches(".*\\d.*")) {
					if ((formatNumbers(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_FOLIO_NUMBER))).toString().trim()) != null && !(formatNumbers(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_CAMS_FOLIO_NUMBER))).toString().trim()).isEmpty()) {
						investorMasterBOList.add(investorMasterBO);
						isRecordValid = true;
					}
				}
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return investorMasterBOList;

	}

	private List<InvestorMasterBO> populateDBFKARVYModelList(Map<String, String> feedData,
			InvestorMasterBODTO investorMasterBODTO, List<InvestorMasterBO> investorMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordValid = false;
			InvestorMasterBO investorMasterBO = mapper.map(investorMasterBODTO, InvestorMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			InvestorMasterBOPK investorMasterBOPK = new InvestorMasterBOPK();
			if (formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_FOLIO_NUMBER)).contains("/")) {
				investorMasterBOPK.setFolioNumber(
						formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_FOLIO_NUMBER)));
			} else {

				BigDecimal bigDecimaldata = new BigDecimal(
						feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_FOLIO_NUMBER));

				Long longData = bigDecimaldata.longValueExact();

				String stringDataOfCell = longData.toString();
				investorMasterBOPK.setFolioNumber(stringDataOfCell);
			}
			// investorMasterBOPK.setFolioNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_FOLIO_NUMBER));
			investorMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_INVESTOR_NAME));
			investorMasterBO.setAddressLine1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_ADDRESS_LINE_1));
			investorMasterBO.setAddressLine2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_ADDRESS_LINE_2));
			investorMasterBO.setAddressLine3(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_ADDRESS_LINE_3));
			investorMasterBO.setCity(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_CITY));
			// System.out.println("PIN:
			// "+feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_PINCODE));
			if (!((feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_PINCODE)) == null)
					&& feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_PINCODE) != ""
					&& !(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_PINCODE)).isEmpty()
					&& !(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_PINCODE)).contains("null")) {

				Double pin = Double.parseDouble(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_PINCODE));
				Integer intPin = pin.intValue();
				investorMasterBO.setPincode(intPin.toString());
			} else {
				investorMasterBO.setPincode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_PINCODE));
			}
			investorMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_SCHEME_RTA_CODE));
			investorMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_SCHEME_NAME));
			investorMasterBO.setJointName1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_JOINT_NAME1));
			investorMasterBO.setJointName2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_JOINT_NAME2));
			investorMasterBO.setPhoneOffice(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_PHONE_OFFICE));
			investorMasterBO.setPhoneResidence(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_PHONE_RESIDENCE));
			investorMasterBO.setEmail(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_EMAIL));
			investorMasterBO.setModeOfHolding(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_MODE_OF_HOLDING));
			investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_INVESTOR_PAN));
			investorMasterBO.setJoint1PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_JOINT1_PAN));
			investorMasterBO.setJoint2PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_JOINT2_PAN));
			investorMasterBO.setGuardianPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_GUARDIAN_PAN));
			String panCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_INVESTOR_PAN)).toString().trim();
			String guardianPanCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_INVESTOR_PAN)).toString().trim();
			if (panCheck != null && !panCheck.isEmpty() && panCheck.matches(".*\\d.*")) {
			} else {
				if (guardianPanCheck != null && !guardianPanCheck.isEmpty() && guardianPanCheck.matches(".*\\d.*")) {
					investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_GUARDIAN_PAN));

				}
			}
			investorMasterBO.setTaxStatus(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_TAX_STATUS));
			investorMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_DISTRIBUTOR_ARN_CODE));
			investorMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_BANK_NAME));
			investorMasterBO.setBankBranch(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_BANK_BRANCH));
			investorMasterBO.setBankAccountType(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_BANK_AC_TYPE));
			investorMasterBO.setBankAccountNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_BANK_AC_NO));
			investorMasterBO.setBankAddress1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_BANK_ADDRESS_1));
			investorMasterBO.setBankAddress2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_BANK_ADDRESS_2));
			investorMasterBO.setBankAddress3(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_BANK_ADDRESS_3));
			investorMasterBO.setBankCity(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_BANK_CITY));
			investorMasterBO.setInvestorDOB(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_INVESTOR_DATE_OF_BIRTH)));
			investorMasterBO.setMobileNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_MOBILE_NUMBER));
			investorMasterBO.setNomineeName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_NOMINEE_NAME));
			investorMasterBO.setNominee2Name(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_NOMINEE2_NAME));
			investorMasterBO.setNominee3Name(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_NOMINEE3_NAME));
			investorMasterBO.setIfscCode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_IFSC_CODE));
			investorMasterBO.setDpId(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_DP_ID));
			investorMasterBO.setGuardianName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_NAME_OF_GUARDIAN));
			investorMasterBO
					.setBrokerDealerCode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_BROKER_DEALER_CODE));
			investorMasterBO.setFirstHolderCKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_FIRST_HOLDER_CKYC));
			investorMasterBO.setJointHolder1CKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_JOINT_HOLDER1_CKYC));
			investorMasterBO.setJointHolder2CKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_JOINT_HOLDER2_CKYC));
			investorMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_AMC_CODE));
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			investorMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(investorMasterBODTO.getAdvisorId());
			investorMasterBO.setAdvisorUser(advUser);
			investorMasterBO.setId(investorMasterBOPK);

			//String pan = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_INVESTOR_PAN)).toString().trim();
			String guardianPan = null;
			if (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_GUARDIAN_PAN) != null) {
				guardianPan = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_GUARDIAN_PAN)).toString().trim();
			}
			//String folioNo = (formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_FOLIO_NUMBER))).toString().trim();
			if ((feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_INVESTOR_PAN)).toString().trim() != null && !(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_INVESTOR_PAN)).toString().trim().isEmpty() && (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_INVESTOR_PAN)).toString().trim().matches(".*\\d.*")) {
				if ((formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_FOLIO_NUMBER))).toString().trim() != null && !(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_FOLIO_NUMBER))).toString().trim().isEmpty()) {
					investorMasterBOList.add(investorMasterBO);

					isRecordValid = true;
				}
			} else {
				if (guardianPan != null && !guardianPan.isEmpty() && guardianPan.matches(".*\\d.*")) {
					if ((formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_FOLIO_NUMBER))).toString().trim() != null && !(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_KARVY_FOLIO_NUMBER))).toString().trim().isEmpty()) {
						investorMasterBOList.add(investorMasterBO);
						isRecordValid = true;
					}
				}
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return investorMasterBOList;

	}

	private List<InvestorMasterBO> populateDBFFranklinModelList(Map<String, String> feedData,
			InvestorMasterBODTO investorMasterBODTO, List<InvestorMasterBO> investorMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordValid = false;
			InvestorMasterBO investorMasterBO = mapper.map(investorMasterBODTO, InvestorMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			InvestorMasterBOPK investorMasterBOPK = new InvestorMasterBOPK();
			investorMasterBOPK.setFolioNumber(
					formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_FOLIO_NUMBER)));
			investorMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_NAME));
			investorMasterBO.setAddressLine1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_ADDRESS_LINE_1));
			investorMasterBO.setAddressLine2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_ADDRESS_LINE_2));
			investorMasterBO.setAddressLine3(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_ADDRESS_LINE_3));
			investorMasterBO.setCity(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_CITY));
			investorMasterBO.setPincode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_PINCODE));
			investorMasterBO.setJointName1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_JOINT_NAME1));
			investorMasterBO.setJointName2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_JOINT_NAME2));
			investorMasterBO.setPhoneOffice(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_PHONE_OFFICE));
			investorMasterBO
					.setPhoneResidence(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_PHONE_RESIDENCE));
			investorMasterBO.setEmail(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_EMAIL));
			investorMasterBO
					.setModeOfHolding(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_MODE_OF_HOLDING));
			investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_PAN));
			investorMasterBO.setJoint1PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_JOINT1_PAN));
			investorMasterBO.setJoint2PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_JOINT2_PAN));
			investorMasterBO.setGuardianPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_GUARDIAN_PAN));
			String panCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_PAN)).toString().trim();
			String guardianPanCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_PAN)).toString().trim();
			if (panCheck != null && !panCheck.isEmpty() && panCheck.matches(".*\\d.*")) {
			} else {
				if (guardianPanCheck != null && !guardianPanCheck.isEmpty() && guardianPanCheck.matches(".*\\d.*")) {
					investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_GUARDIAN_PAN));

				}
			}
			investorMasterBO.setTaxStatus(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_TAX_STATUS));
			investorMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_DISTRIBUTOR_ARN_CODE));
			investorMasterBO.setSubbrokerDealerCode(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_SUBBROKER_DEALER_CODE));
			investorMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_BANK_NAME));
			investorMasterBO.setBankBranch(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_BANK_BRANCH));
			investorMasterBO
					.setBankAccountType(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_BANK_AC_TYPE));
			investorMasterBO
					.setBankAccountNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_BANK_AC_NO));
			investorMasterBO.setBankAddress1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_BANK_ADDRESS_1));
			investorMasterBO.setBankAddress2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_BANK_ADDRESS_2));
			investorMasterBO.setBankAddress3(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_BANK_ADDRESS_3));
			investorMasterBO.setBankCity(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_BANK_CITY));
			investorMasterBO.setPincode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_BANK_PINCODE));
			// System.out.println("DOB:
			// "+feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_DATE_OF_BIRTH));
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date uDate = new java.util.Date();
			if (!feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_DATE_OF_BIRTH).trim().isEmpty()
					&& !feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_DATE_OF_BIRTH).trim()
							.contains("")
					&& !feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_DATE_OF_BIRTH).trim()
							.contains("null")) {
				uDate = formatter
						.parse(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_DATE_OF_BIRTH));
				investorMasterBO.setInvestorDOB(utilToSQLDate(uDate.toString()));
			}
			investorMasterBO.setMobileNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_MOBILE_NUMBER));
			investorMasterBO.setNomineeName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_NOMINEE_NAME));
			investorMasterBO.setNominee2Name(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_NOMINEE2_NAME));
			investorMasterBO.setNominee3Name(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_NOMINEE3_NAME));
			investorMasterBO.setIfscCode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_IFSC_CODE));
			investorMasterBO.setDpId(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_DP_ID));
			investorMasterBO
					.setGuardianName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_NAME_OF_GUARDIAN));
			investorMasterBO
					.setBrokerDealerCode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_BROKER_DEALER_CODE));
			investorMasterBO.setFolioCreateDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_FOLIO_CREATE_DATE)));
			investorMasterBO.setFirstHolderCKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_FIRST_HOLDER_CKYC));
			investorMasterBO.setJointHolder1CKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_JOINT_HOLDER1_CKYC));
			investorMasterBO.setJointHolder2CKYCNumber(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_JOINT_HOLDER2_CKYC));
			investorMasterBO
					.setGuardianCKYCNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_GUARDIAN_CKYC));
			investorMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_AMC_CODE));
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			investorMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(investorMasterBODTO.getAdvisorId());
			investorMasterBO.setAdvisorUser(advUser);
			investorMasterBO.setId(investorMasterBOPK);

			//String pan = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_PAN)).toString().trim();
			String guardianPan = null;
			if (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_GUARDIAN_PAN) != null) {
				guardianPan = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_GUARDIAN_PAN)).toString().trim();
			}			
			//String folioNo = (formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_FOLIO_NUMBER))).toString().trim();
			if ((feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_PAN)).toString().trim() != null && !(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_PAN)).toString().trim().isEmpty() && (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_INVESTOR_PAN)).toString().trim().matches(".*\\d.*")) {
				if ((formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_FOLIO_NUMBER))).toString().trim() != null && !(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_FOLIO_NUMBER))).toString().trim().isEmpty()) {
					investorMasterBOList.add(investorMasterBO);

					isRecordValid = true;
				}
			} else {
				if (guardianPan != null && !guardianPan.isEmpty() && guardianPan.matches(".*\\d.*")) {
					if ((formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_FOLIO_NUMBER))).toString().trim() != null && !(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_FOLIO_NUMBER))).toString().trim().isEmpty()) {
						investorMasterBOList.add(investorMasterBO);
						isRecordValid = true;
					}
				}
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return investorMasterBOList;

	}

	private List<InvestorMasterBO> populateDBFSUNDARAMModelList(Map<String, String> feedData,
			InvestorMasterBODTO investorMasterBODTO, List<InvestorMasterBO> investorMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordValid = false;
			InvestorMasterBO investorMasterBO = mapper.map(investorMasterBODTO, InvestorMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			InvestorMasterBOPK investorMasterBOPK = new InvestorMasterBOPK();
			investorMasterBOPK.setFolioNumber(
					formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_FOLIO_NUMBER)));
			investorMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_INVESTOR_NAME));
			investorMasterBO.setAddressLine1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_ADDRESS_LINE_1));
			investorMasterBO.setAddressLine2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_ADDRESS_LINE_2));
			investorMasterBO.setAddressLine3(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_ADDRESS_LINE_3));
			investorMasterBO.setCity(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_CITY));
			investorMasterBO.setPincode(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_PINCODE));
			investorMasterBO.setJointName1(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_JOINT_NAME1));
			investorMasterBO.setJointName2(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_JOINT_NAME2));
			investorMasterBO.setPhoneOffice(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_PHONE_OFFICE));
			investorMasterBO
					.setPhoneResidence(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_PHONE_RESIDENCE));
			investorMasterBO.setEmail(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_EMAIL));
			investorMasterBO
					.setModeOfHolding(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_MODE_OF_HOLDING));
			investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_INVESTOR_PAN));
			investorMasterBO.setJoint1PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_JOINT1_PAN));
			investorMasterBO.setJoint2PAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_JOINT2_PAN));
			investorMasterBO.setGuardianPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_GUARDIAN_PAN));
			String panCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_INVESTOR_PAN)).toString().trim();
			String guardianPanCheck = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_INVESTOR_PAN)).toString().trim();
			if (panCheck != null && !panCheck.isEmpty() && panCheck.matches(".*\\d.*")) {
			} else {
				if (guardianPanCheck != null && !guardianPanCheck.isEmpty() && guardianPanCheck.matches(".*\\d.*")) {
					investorMasterBOPK.setInvestorPAN(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_GUARDIAN_PAN));

				}
			}
			investorMasterBO.setTaxStatus(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_TAX_STATUS));
			investorMasterBO.setDistributorARNCode(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_DISTRIBUTOR_ARN_CODE));
			investorMasterBO.setSubbrokerDealerCode(
					feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_SUBBROKER_DEALER_CODE));
			investorMasterBO.setBankName(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_BANK_NAME));
			investorMasterBO.setBankBranch(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_BANK_BRANCH));
			/*
			 * investorMasterBO .setBankAccountType(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_SUNDARAM_BANK_AC_TYPE)); investorMasterBO
			 * .setBankAccountNumber(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_SUNDARAM_BANK_AC_NO));
			 * investorMasterBO.setBankAddress1(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_SUNDARAM_BANK_ADDRESS_1));
			 * investorMasterBO.setBankAddress2(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_SUNDARAM_BANK_ADDRESS_2));
			 * investorMasterBO.setBankAddress3(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_SUNDARAM_BANK_ADDRESS_3));
			 * investorMasterBO.setBankCity(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_SUNDARAM_BANK_CITY));
			 * investorMasterBO.setPincode(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_SUNDARAM_BANK_PINCODE)); // System.out.println("DOB: //
			 * "+feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_SUNDARAM_INVESTOR_DATE_OF_BIRTH));
			 */
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date uDate = new java.util.Date();
			if (!feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_INVESTOR_DATE_OF_BIRTH).trim().isEmpty()
					&& !feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_INVESTOR_DATE_OF_BIRTH).trim()
							.contains("")
					&& !feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_INVESTOR_DATE_OF_BIRTH).trim()
							.contains("null")) {
				uDate = formatter
						.parse(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_INVESTOR_DATE_OF_BIRTH));
				investorMasterBO.setInvestorDOB(utilToSQLDate(uDate.toString()));
			}
			investorMasterBO.setMobileNumber(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_MOBILE_NUMBER));
			/*
			 * investorMasterBO.setNomineeName(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_SUNDARAM_NOMINEE_NAME));
			 * investorMasterBO.setNominee2Name(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_FRANKLIN_NOMINEE2_NAME));
			 * investorMasterBO.setNominee3Name(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_FRANKLIN_NOMINEE3_NAME));
			 * investorMasterBO.setIfscCode(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_FRANKLIN_IFSC_CODE));
			 * investorMasterBO.setDpId(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_FRANKLIN_DP_ID)); investorMasterBO
			 * .setGuardianName(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_FRANKLIN_NAME_OF_GUARDIAN)); investorMasterBO
			 * .setBrokerDealerCode(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_FRANKLIN_BROKER_DEALER_CODE));
			 * investorMasterBO.setFolioCreateDate(
			 * utilToSQLDate(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_FRANKLIN_FOLIO_CREATE_DATE)));
			 * investorMasterBO.setFirstHolderCKYCNumber(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_FIRST_HOLDER_CKYC))
			 * ; investorMasterBO.setJointHolder1CKYCNumber(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_JOINT_HOLDER1_CKYC)
			 * ); investorMasterBO.setJointHolder2CKYCNumber(
			 * feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_FRANKLIN_JOINT_HOLDER2_CKYC)
			 * ); investorMasterBO
			 * .setGuardianCKYCNumber(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_FRANKLIN_GUARDIAN_CKYC));
			 * investorMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.
			 * INVESTOR_DBF_FRANKLIN_AMC_CODE));
			 */
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Sundaram Mutual Fund");
			investorMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(investorMasterBODTO.getAdvisorId());
			investorMasterBO.setAdvisorUser(advUser);
			investorMasterBO.setId(investorMasterBOPK);

			//String pan = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_INVESTOR_PAN)).toString().trim();
			String guardianPan = null;
			if (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_GUARDIAN_PAN) != null) {
				guardianPan = (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_GUARDIAN_PAN)).toString().trim();
			}
			//String folioNo = (formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_FOLIO_NUMBER))).toString().trim();
			if ((feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_INVESTOR_PAN)).toString().trim() != null && !(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_INVESTOR_PAN)).toString().trim().isEmpty() && (feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_INVESTOR_PAN)).toString().trim().matches(".*\\d.*")) {
				if ((formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_FOLIO_NUMBER))).toString().trim() != null && !(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_FOLIO_NUMBER))).toString().trim().isEmpty()) {
					investorMasterBOList.add(investorMasterBO);

					isRecordValid = true;
				}
			} else {
				if (guardianPan != null && !guardianPan.isEmpty() && guardianPan.matches(".*\\d.*")) {
					if ((formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_FOLIO_NUMBER))).toString().trim() != null && !(formatNumbers(feedData.get(FinexaBOColumnConstant.INVESTOR_DBF_SUNDARAM_FOLIO_NUMBER))).toString().trim().isEmpty()) {
						investorMasterBOList.add(investorMasterBO);

						isRecordValid = true;
					}
				}
			}

			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers + rejectedRowNumber.toString() + ",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return investorMasterBOList;

	}

	private synchronized UploadResponseDTO saveInvestorData(List<InvestorMasterBO> investorMasterBODTOList,
			UploadResponseDTO uploadResponseDTO) {
		// TODO Auto-generated method stub

		try {
			if (investorMasterBODTOList != null && investorMasterBODTOList.size() > 0) {
				investMasterBORepository.save(investorMasterBODTOList);

			}

			uploadResponseDTO.setStatus(true);

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
		String columnHeader = "folioNo";
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
}