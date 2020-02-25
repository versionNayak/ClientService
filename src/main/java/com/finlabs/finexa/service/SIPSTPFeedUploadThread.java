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
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.finlabs.finexa.dto.SIPSTPMasterBODTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.BackOfficeUploadHistory;
import com.finlabs.finexa.model.LookupRTABO;
import com.finlabs.finexa.model.SIPSTPMasterBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.BackOfficeUploadHistoryRepository;
import com.finlabs.finexa.repository.LookupRTABORepository;
import com.finlabs.finexa.repository.LookupRTAMasterFileDetailsBORepository;
import com.finlabs.finexa.repository.RejectionMasterBORepository;
import com.finlabs.finexa.repository.SIPSTPMasterBORepository;
import com.finlabs.finexa.util.FinexaBOColumnConstant;
import com.finlabs.finexa.util.FinexaUtil;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

@Component

@Scope("prototype")

public class SIPSTPFeedUploadThread implements Runnable {

	private static Logger log = LoggerFactory.getLogger(RejectionMasterBOServiceImpl.class);

	@Autowired
	Mapper mapper;

	@Autowired
	LookupRTABORepository lookupRTABORepository;

	@Autowired
	AdvisorUserRepository advisorUserRepository;

	@Autowired
	BackOfficeUploadHistoryRepository uploadHistoryRepo;

	@Autowired
	SIPSTPMasterBORepository sIPSTPMasterBORepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(FeedUploadThread.class);
	private SIPSTPMasterBODTO sipSTPMasterBODTO;
	private UploadResponseDTO uploadResponseDTO;
	private BackOfficeUploadHistory backOfficeUploadHistory;

	private static final String RTA_FILE_TYPE_CAMS = "1";
	private static final String RTA_FILE_TYPE_KARVY = "2";
	private static final String RTA_FILE_TYPE_FRANKLIN = "3";
	private static final String RTA_FILE_TYPE_SUNDARAM = "4";
	private static final String FRANKLIN_ACTIVE_SIP = "25";
	private static final String FRANKLIN_CLOSED_SIP = "27";
	private static final String FRANKLIN_ACTIVE_STP = "26";
	private static final String FRANKLIN_CLOSED_STP = "28";
	private Properties prop = new Properties();
	private String rtaId;
	private Integer rta, file;
	private String fileName;
	private String FEED_PROPERTIES_FILE;
	private int rows = 1, columnHeaderRow = 0, noOfRejectedRecords = 0;
	private String rejectedRowNumbers = "";
	private Integer rejectedRowNumber = 0;
	DBFReader dbfReader;

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

	public void initialize(SIPSTPMasterBODTO sipSTPMasterBODTO, UploadResponseDTO uploadResponseDTO,
			BackOfficeUploadHistory backOfficeUploadHistory) {
		this.sipSTPMasterBODTO = sipSTPMasterBODTO;
		this.uploadResponseDTO = uploadResponseDTO;
		this.backOfficeUploadHistory = backOfficeUploadHistory;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {

			log.debug("RejectionMasterBOServiceImpl >> uploadRejectionMaster");
			rta = sipSTPMasterBODTO.getNameRTA();
			rtaId = rta.toString();
			file = sipSTPMasterBODTO.getNameFileType();
			fileName = file.toString();
			String name = sipSTPMasterBODTO.getNameSelectFile()[0].getOriginalFilename();
			if (name.contains(".dbf") || name.contains(".DBF")) {
				uploadResponseDTO = readDBFFeed(sipSTPMasterBODTO, uploadResponseDTO);
			} else {
				uploadResponseDTO = readSIPSTPExcelFeed(sipSTPMasterBODTO, uploadResponseDTO);
			}
			// readRejectionExcelFeed(rejectionMasterBODTO, uploadResponseDTO);

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
	private UploadResponseDTO readSIPSTPExcelFeed(SIPSTPMasterBODTO sIPSTPMasterBODTO,
			UploadResponseDTO uploadResponseDTO) throws IOException, InvalidFormatException, ParseException {

		// null pointer handling

		InputStream initialStream = sIPSTPMasterBODTO.getNameSelectFile()[0].getInputStream();

		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);

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
			FEED_PROPERTIES_FILE = "backOfficeProperties/sipSTPCAMS.properties";
			break;
		case RTA_FILE_TYPE_KARVY:
			FEED_PROPERTIES_FILE = "backOfficeProperties/sipSTPKarvyDBF.properties";
			break;

		case RTA_FILE_TYPE_FRANKLIN:
			if (fileName.equals(FRANKLIN_ACTIVE_SIP)) {
				FEED_PROPERTIES_FILE = "backOfficeProperties/sipActiveFranklin.properties";
				break;
			} else if (fileName.equals(FRANKLIN_CLOSED_SIP)) {
				FEED_PROPERTIES_FILE = "backOfficeProperties/sipClosedFranklin.properties";
				break;
			} else if (fileName.equals(FRANKLIN_ACTIVE_STP)) {
				FEED_PROPERTIES_FILE = "backOfficeProperties/stpActiveFranklin.properties";
				break;
			} else if (fileName.equals(FRANKLIN_CLOSED_STP)) {
				FEED_PROPERTIES_FILE = "backOfficeProperties/stpClosedFranklin.properties";
				break;
			}
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
		// lookupRTAMasterFileDetailsBORepository.findByFileCode("STPSIPM");

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
				// System.out.println(excelNameColNameMap);

			}

		}
		System.out.println(excelNameColNameMap);
		
		switch (rtaId) {

		case RTA_FILE_TYPE_CAMS:
			List<String> camsMandatoryFeedList = new ArrayList<>();
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_SCHEME_RTA_CODE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_SCHEME_NAME);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_FOLIO_NUMBER);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_INVESTOR_NAME);
			//camsMandatoryFeedList.add(FinexaBOColumnConstant.AUM_CAMS_EXCEL_SUB_BROKER_CODE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TRANSACTION_TYPE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TRANSACTION_NUMBER);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_AMOUNT);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_FROM_DATE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TERMINATION_DATE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_FREQUENCY);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TARGET_SCHEME);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_REGISTRATION_DATE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_INVESTOR_PAN);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TO_DATE);
			
			for (Map.Entry<String, String> obj : excelNameColNameMap.entrySet()) {
				String key = obj.getKey();
				String value = obj.getValue();
				if (value.equals("nu") && camsMandatoryFeedList.contains(key)) { // implies mandatory column null
					uploadResponseDTO.setPrimaryKeyNotFound(true);
					System.out.println(key+" Address Null");
					uploadResponseDTO.setStatus(false);
					return uploadResponseDTO;
				}
			}
			break;
		case RTA_FILE_TYPE_KARVY:
			List<String> karvyMandatoryFeedList = new ArrayList<>();
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_SCHEME_RTA_CODE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_SCHEME_NAME);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_FOLIO_NUMBER);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_INVESTOR_NAME);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TRANSACTION_TYPE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TRANSACTION_NUMBER);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_AMOUNT);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_FROM_DATE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TERMINATION_DATE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_FREQUENCY);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TARGET_SCHEME);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_REGISTRATION_DATE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_INVESTOR_PAN);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TO_DATE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_SCHEME_NAME1);		
			
			for (Map.Entry<String, String> obj : excelNameColNameMap.entrySet()) {
				String key = obj.getKey();
				String value = obj.getValue();
				if (value.equals("nu") && karvyMandatoryFeedList.contains(key)) { // implies mandatory column null
					if (key.equals(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_SCHEME_NAME) || key.equals(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_SCHEME_NAME1)) {
						continue;
					} else if (key.equals(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TRANSACTION_NUMBER) || key.equals(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TRANSACTION_NUMBER1)) {
						continue;
					} else{
						uploadResponseDTO.setPrimaryKeyNotFound(true);
						System.out.println(key+" Address Null");
						uploadResponseDTO.setStatus(false);
						return uploadResponseDTO;
					}					
				}
			}
			break;
		case FRANKLIN_ACTIVE_SIP:
			List<String> franklinActiveSIPMandatoryFeedList = new ArrayList<>();
			franklinActiveSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_SCHEME_RTA_CODE);
			franklinActiveSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_SCHEME_NAME);
			franklinActiveSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_FOLIO_NUMBER);
			franklinActiveSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_INVESTOR_NAME);			
			franklinActiveSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_TRANSACTION_NUMBER);			
			franklinActiveSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_AMOUNT);
			franklinActiveSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_FROM_DATE);
			franklinActiveSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_TO_DATE);
			franklinActiveSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_TERMINATION_DATE);
			franklinActiveSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_FREQUENCY);
			franklinActiveSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_REGISTRATION_DATE);
			franklinActiveSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_INVESTOR_PAN);
							
			for (Map.Entry<String, String> obj : excelNameColNameMap.entrySet()) {
				String key = obj.getKey();
				String value = obj.getValue();
				if (value.equals("nu") && franklinActiveSIPMandatoryFeedList.contains(key)) { // implies mandatory column null
					uploadResponseDTO.setPrimaryKeyNotFound(true);
					System.out.println(key+" Address Null");
					uploadResponseDTO.setStatus(false);
					return uploadResponseDTO;
				}
			}
			break;
			
		case FRANKLIN_CLOSED_SIP:
			List<String> franklinClosedSIPMandatoryFeedList = new ArrayList<>();
			
			franklinClosedSIPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_REJECT_TRANSACTION_NUMBER);			
			
							
			for (Map.Entry<String, String> obj : excelNameColNameMap.entrySet()) {
				String key = obj.getKey();
				String value = obj.getValue();
				if (value.equals("nu") && franklinClosedSIPMandatoryFeedList.contains(key)) { // implies mandatory column null
					uploadResponseDTO.setPrimaryKeyNotFound(true);
					System.out.println(key+" Address Null");
					uploadResponseDTO.setStatus(false);
					return uploadResponseDTO;
				}
			}
			break;
			
			
			
			
		
			
			
		case FRANKLIN_ACTIVE_STP:
			List<String> franklinActiveSTPMandatoryFeedList = new ArrayList<>();
			
			franklinActiveSTPMandatoryFeedList.add(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_SCHEME_NAME);
			franklinActiveSTPMandatoryFeedList.add(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_FOLIO_NUMBER);
			franklinActiveSTPMandatoryFeedList.add(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_INVESTOR_NAME);
			franklinActiveSTPMandatoryFeedList.add(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_TRANSACTION_NUMBER);
			franklinActiveSTPMandatoryFeedList.add(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_FROM_DATE);
			franklinActiveSTPMandatoryFeedList.add(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_TO_DATE);
			franklinActiveSTPMandatoryFeedList.add(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_FREQUENCY);
			franklinActiveSTPMandatoryFeedList.add(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_TARGET_SCHEME);
			franklinActiveSTPMandatoryFeedList.add(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_TRANSACTION_NUMBER);
			
			
			
							
			for (Map.Entry<String, String> obj : excelNameColNameMap.entrySet()) {
				String key = obj.getKey();
				String value = obj.getValue();
				if (value.equals("nu") && franklinActiveSTPMandatoryFeedList.contains(key)) { // implies mandatory column null
					uploadResponseDTO.setPrimaryKeyNotFound(true);
					System.out.println(key+" Address Null");
					uploadResponseDTO.setStatus(false);
					return uploadResponseDTO;
				}
			}
			break;
			
		case FRANKLIN_CLOSED_STP:
			List<String> franklinClosedSTPMandatoryFeedList = new ArrayList<>();
			
			
			franklinClosedSTPMandatoryFeedList.add(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_TRANSACTION_NUMBER);
			
			
			
							
			for (Map.Entry<String, String> obj : excelNameColNameMap.entrySet()) {
				String key = obj.getKey();
				String value = obj.getValue();
				if (value.equals("nu") && franklinClosedSTPMandatoryFeedList.contains(key)) { // implies mandatory column null
					uploadResponseDTO.setPrimaryKeyNotFound(true);
					System.out.println(key+" Address Null");
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
		
		
		
/*
		for (String columnAddress : excelNameColNameMap.values())
			if (columnAddress.equals("nu")) {
				uploadResponseDTO.setPrimaryKeyNotFound(true);
				System.out.println("Excel Column Address Null");
				uploadResponseDTO.setStatus(false);
				return uploadResponseDTO;
			}*/
		List<SIPSTPMasterBO> sIPSTPMasterBOList = new ArrayList<SIPSTPMasterBO>();

		// Empty the list each time after inserting 20 records
		int batchCount = 0;

		// System.out.println("AdvisorID: " + sIPSTPMasterBODTO.getAdvisorId());

		// try-catch
		AdvisorUser advisorUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
		// System.out.println("Inside readExcel method");
		System.out.println("advisorUser" + advisorUser);
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
						
						if (key.equals("transactionNumber") || key.equals("folioNumber")) {
							Cell cell = row.getCell(
									org.apache.poi.ss.util.CellReference.convertColStringToIndex(columnAddress));
							String data = cell.toString();
							if (!data.contains("/") && !data.matches("[A-Za-z0-9]+")) {							

								Double doubleData = Double.parseDouble(data);

								BigDecimal bigDecimaldata = new BigDecimal(doubleData.toString());

								Long longData = bigDecimaldata.longValueExact();

								dataOfCell = longData.toString();

							} else {
								
								dataOfCell = cell.toString();
							}
						} else {
							Cell cell = row.getCell(
									org.apache.poi.ss.util.CellReference.convertColStringToIndex(columnAddress));
							dataOfCell = cell.toString();
						}
					}
					feed.put(propertyKey, dataOfCell);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (batchCount == 5) {
				System.out.println("LIST SIZE: "+sIPSTPMasterBOList.size());
				uploadResponseDTO = saveSIPSTPData(sIPSTPMasterBOList, uploadResponseDTO);
				sIPSTPMasterBOList = new ArrayList<SIPSTPMasterBO>();
				batchCount = 0;

				switch (rtaId) {
				case RTA_FILE_TYPE_CAMS:
					sIPSTPMasterBOList = populateSIPSTPCAMSModelList(feed, sIPSTPMasterBODTO, sIPSTPMasterBOList,
							advisorUser);
					break;
				case RTA_FILE_TYPE_KARVY:
					sIPSTPMasterBOList = populateSIPSTPKarvyModelList(feed, sIPSTPMasterBODTO, sIPSTPMasterBOList,
							advisorUser);
					break;

				case RTA_FILE_TYPE_FRANKLIN:
					if (fileName.equals(FRANKLIN_ACTIVE_SIP)) {
						sIPSTPMasterBOList = populateSIPActiveFranklinModelList(feed, sIPSTPMasterBODTO,
								sIPSTPMasterBOList, advisorUser);
						break;
					} else if (fileName.equals(FRANKLIN_CLOSED_SIP)) {
						sIPSTPMasterBOList = populateSIPClosedFranklinModelList(feed, sIPSTPMasterBODTO,
								sIPSTPMasterBOList, advisorUser);
						break;
					} else if (fileName.equals(FRANKLIN_ACTIVE_STP)) {
						sIPSTPMasterBOList = populateSTPActiveFranklinModelList(feed, sIPSTPMasterBODTO,
								sIPSTPMasterBOList, advisorUser);
						break;
					} else if (fileName.equals(FRANKLIN_CLOSED_STP)) {
						sIPSTPMasterBOList = populateSTPClosedFranklinModelList(feed, sIPSTPMasterBODTO,
								sIPSTPMasterBOList, advisorUser);
						break;
					}
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
					sIPSTPMasterBOList = populateSIPSTPCAMSModelList(feed, sIPSTPMasterBODTO, sIPSTPMasterBOList,
							advisorUser);
					break;
				case RTA_FILE_TYPE_KARVY:
					sIPSTPMasterBOList = populateSIPSTPKarvyModelList(feed, sIPSTPMasterBODTO, sIPSTPMasterBOList,
							advisorUser);
					break;

				case RTA_FILE_TYPE_FRANKLIN:
					if (fileName.equals(FRANKLIN_ACTIVE_SIP)) {
						sIPSTPMasterBOList = populateSIPActiveFranklinModelList(feed, sIPSTPMasterBODTO,
								sIPSTPMasterBOList, advisorUser);
						break;
					} else if (fileName.equals(FRANKLIN_CLOSED_SIP)) {
						sIPSTPMasterBOList = populateSIPClosedFranklinModelList(feed, sIPSTPMasterBODTO,
								sIPSTPMasterBOList, advisorUser);
						break;
					} else if (fileName.equals(FRANKLIN_ACTIVE_STP)) {
						sIPSTPMasterBOList = populateSTPActiveFranklinModelList(feed, sIPSTPMasterBODTO,
								sIPSTPMasterBOList, advisorUser);
						break;
					} else if (fileName.equals(FRANKLIN_CLOSED_STP)) {
						sIPSTPMasterBOList = populateSTPClosedFranklinModelList(feed, sIPSTPMasterBODTO,
								sIPSTPMasterBOList, advisorUser);
						break;
					}
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
			System.out.println("LAST LIST SIZE: "+sIPSTPMasterBOList.size());
			uploadResponseDTO = saveSIPSTPData(sIPSTPMasterBOList, uploadResponseDTO);
			allRowsInserted = true;

		}
		targetFile.delete();
		workbook.close();
		
		uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		return uploadResponseDTO;

	}

	private List<SIPSTPMasterBO> populateSIPSTPCAMSModelList(Map<String, String> feedData,
			SIPSTPMasterBODTO sIPSTPMasterBODTO, List<SIPSTPMasterBO> sIPSTPMasterBOList, AdvisorUser advisorUser)
			throws ParseException {
		// TODO Auto-generated method stub

		try {
			SIPSTPMasterBO sIPSTPMasterBO = mapper.map(sIPSTPMasterBODTO, SIPSTPMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			sIPSTPMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_SCHEME_RTA_CODE));
			sIPSTPMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_SCHEME_NAME));
			sIPSTPMasterBO.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_FOLIO_NUMBER)));
			sIPSTPMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_INVESTOR_NAME));
			sIPSTPMasterBO.setTransactionType(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TRANSACTION_TYPE));
			sIPSTPMasterBO
					.setTransactionNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TRANSACTION_NUMBER)));
			sIPSTPMasterBO.setAmount(formatAmount(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_AMOUNT)));
			sIPSTPMasterBO.setFromDate(finexaUtil.formatDate(
					finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_FROM_DATE))));
			sIPSTPMasterBO.setToDate(finexaUtil.formatDate(
					finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TO_DATE))));
			sIPSTPMasterBO.setTerminationDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TERMINATION_DATE))));
			sIPSTPMasterBO.setFrequency(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_FREQUENCY));
			// sIPSTPMasterBO.setPeriodDay(feedData.get("PERIOD_DAY"));
			// sIPSTPMasterBO.setInvestorMin(feedData.get("INV_IIN"));
			// sIPSTPMasterBO.setPaymentMode(feedData.get("PAYMENT_MO"));
			sIPSTPMasterBO.setTargetScheme(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TARGET_SCHEME));
			sIPSTPMasterBO.setRegistrationDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_REGISTRATION_DATE))));
			sIPSTPMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_SUB_BROKER_CODE));
			/*
			 * sIPSTPMasterBO.setRemarks(feedData.get("REMARKS"));
			 * sIPSTPMasterBO.setTopUpFrequency(feedData.get("TOP_UP_FRQ"));
			 * sIPSTPMasterBO.setTopUpAmount(feedData.get("TOP_UP_AMT"));
			 * sIPSTPMasterBO.setAccountType(feedData.get("AC_TYPE"));
			 * sIPSTPMasterBO.setAccountName(feedData.get("BANK"));
			 * sIPSTPMasterBO.setBranch(feedData.get("BRANCH"));
			 * sIPSTPMasterBO.setInstrumentNumber(feedData.get("INSTRM_NO"));
			 * sIPSTPMasterBO.setChequeMICR(feedData.get("CHEQ_MICR_"));
			 * sIPSTPMasterBO.setAccountHolder(feedData.get("AC_HOLDER_"));
			 */
			sIPSTPMasterBO.setInvestorPAN(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_INVESTOR_PAN));
			// sIPSTPMasterBO.setTopUpPercentage(feedData.get("TOP_UP_PER"));
			// sIPSTPMasterBO.setArnEmpCode(feedData.get("EUIN"));
			// sIPSTPMasterBO.setSubBrokerARN(feedData.get("SUB_ARN_CO"));
			sIPSTPMasterBO.setLocation(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_LOCATION_CATEGORY));
			// sIPSTPMasterBO.setSchemeCode(feedData.get("SCHEME_COD"));
			sIPSTPMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_AMC_CODE));
			// sIPSTPMasterBO.setUserCode(feedData.get("USER_CODE"));
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");
			sIPSTPMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
			sIPSTPMasterBO.setAdvisorUser(advUser);
			// sIPSTPMasterBO.setId(investorMasterBOPK);
			// String transNo =
			// feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TRANSACTION_NUMBER).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TRANSACTION_NUMBER).toString().trim() != null
					&& !feedData.get(FinexaBOColumnConstant.SIPSTP_CAMS_EXCEL_TRANSACTION_NUMBER).toString().trim()
							.isEmpty()) {
				sIPSTPMasterBOList.add(sIPSTPMasterBO);
				isRecordValid = true;
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return sIPSTPMasterBOList;

	}

	private List<SIPSTPMasterBO> populateSIPSTPKarvyModelList(Map<String, String> feedData,
			SIPSTPMasterBODTO sIPSTPMasterBODTO, List<SIPSTPMasterBO> sIPSTPMasterBOList, AdvisorUser advisorUser)
			throws ParseException {
		// TODO Auto-generated method stub

		try {
			SIPSTPMasterBO sIPSTPMasterBO = mapper.map(sIPSTPMasterBODTO, SIPSTPMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			sIPSTPMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_SCHEME_RTA_CODE));
			sIPSTPMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_SCHEME_NAME));
			sIPSTPMasterBO.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_FOLIO_NUMBER)));
			sIPSTPMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_INVESTOR_NAME));
			sIPSTPMasterBO.setTransactionType(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TRANSACTION_TYPE));
			sIPSTPMasterBO
					.setTransactionNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TRANSACTION_NUMBER)));
			sIPSTPMasterBO.setAmount(formatAmount(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_AMOUNT)));
			sIPSTPMasterBO.setFromDate(finexaUtil.formatDate(
					finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_FROM_DATE))));
			sIPSTPMasterBO.setToDate(finexaUtil.formatDate(
					finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TO_DATE))));
			sIPSTPMasterBO.setTerminationDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TERMINATION_DATE))));
			sIPSTPMasterBO.setFrequency(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_FREQUENCY));
			/*
			 * sIPSTPMasterBO.setPeriodDay(feedData.get(""));
			 * sIPSTPMasterBO.setInvestorMin(feedData.get(""));
			 * sIPSTPMasterBO.setPaymentMode(feedData.get(""));
			 */
			sIPSTPMasterBO.setTargetScheme(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TARGET_SCHEME));
			sIPSTPMasterBO.setRegistrationDate(finexaUtil.formatDate(finexaUtil
					.formatStringDate(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_REGISTRATION_DATE))));
			// System.out.println(feedData.get("RegistrationDate"));
			// System.out.println(finexaUtil.formatDate(feedData.get("RegistrationDate")));
			sIPSTPMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_SUB_BROKER_CODE));
			sIPSTPMasterBO.setInvestorPAN(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_INVESTOR_PAN));
			// sIPSTPMasterBO.setTopUpFrequency(feedData.get("LOCATION"));
			/*
			 * sIPSTPMasterBO.setTopUpAmount(feedData.get(""));
			 * sIPSTPMasterBO.setAccountType(feedData.get(""));
			 * sIPSTPMasterBO.setAccountName(feedData.get(""));
			 * sIPSTPMasterBO.setBranch(feedData.get(""));
			 * sIPSTPMasterBO.setInstrumentNumber(feedData.get(""));
			 * sIPSTPMasterBO.setChequeMICR(feedData.get(""));
			 * sIPSTPMasterBO.setAccountHolder(feedData.get(""));
			 * sIPSTPMasterBO.setRemarks(feedData.get(""));
			 * sIPSTPMasterBO.setTopUpPercentage(feedData.get(""));
			 * sIPSTPMasterBO.setArnEmpCode(feedData.get(""));
			 * sIPSTPMasterBO.setSubBrokerARN(feedData.get(""));
			 * 
			 * sIPSTPMasterBO.setSchemeCode(feedData.get(""));
			 */
			sIPSTPMasterBO.setLocation(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_LOCATION_CATEGORY));
			sIPSTPMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_AMC_CODE));
			// sIPSTPMasterBO.setUserCode(feedData.get(""));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			sIPSTPMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
			sIPSTPMasterBO.setAdvisorUser(advUser);
			// sIPSTPMasterBO.setId(investorMasterBOPK);
			String transNo = feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TRANSACTION_NUMBER).toString()
					.trim();
			if (feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TRANSACTION_NUMBER).toString().trim() != null
					&& !feedData.get(FinexaBOColumnConstant.SIPSTP_KARVY_EXCEL_TRANSACTION_NUMBER).toString().trim()
							.isEmpty()) {
				sIPSTPMasterBOList.add(sIPSTPMasterBO);
				isRecordValid = true;
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return sIPSTPMasterBOList;

	}

	private List<SIPSTPMasterBO> populateSTPActiveFranklinModelList(Map<String, String> feedData,
			SIPSTPMasterBODTO sIPSTPMasterBODTO, List<SIPSTPMasterBO> sIPSTPMasterBOList, AdvisorUser advisorUser)
			throws ParseException {
		// TODO Auto-generated method stub

		try {
			SIPSTPMasterBO sIPSTPMasterBO = mapper.map(sIPSTPMasterBODTO, SIPSTPMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			// sIPSTPMasterBO.setSchemeRTACode(feedData.get(""));
			sIPSTPMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_SCHEME_NAME));
			sIPSTPMasterBO.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_FOLIO_NUMBER)));
			sIPSTPMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_INVESTOR_NAME));
			sIPSTPMasterBO.setTransactionType("STP");
			sIPSTPMasterBO
					.setTransactionNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_TRANSACTION_NUMBER)));
			sIPSTPMasterBO.setAmount(formatAmount(feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_AMOUNT)));
			sIPSTPMasterBO.setFromDate(
					finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_FROM_DATE))));
			sIPSTPMasterBO
					.setToDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_TO_DATE))));
			// sIPSTPMasterBO.setTerminationDate(finexaUtil.formatDate(feedData.get("")));
			sIPSTPMasterBO.setFrequency(feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_FREQUENCY));
			/*
			 * sIPSTPMasterBO.setPeriodDay(feedData.get(""));
			 * sIPSTPMasterBO.setInvestorMin(feedData.get(""));
			 * sIPSTPMasterBO.setPaymentMode(feedData.get(""));
			 */
			sIPSTPMasterBO.setTargetScheme(feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_TARGET_SCHEME));
			/*
			 * sIPSTPMasterBO.setRegistrationDate(finexaUtil.formatDate(feedData.get("")));
			 * sIPSTPMasterBO.setSubBrokerCode(feedData.get(""));
			 * sIPSTPMasterBO.setRemarks(feedData.get(""));
			 * sIPSTPMasterBO.setTopUpFrequency(feedData.get(""));
			 * sIPSTPMasterBO.setTopUpAmount(feedData.get(""));
			 * sIPSTPMasterBO.setAccountType(feedData.get(""));
			 * sIPSTPMasterBO.setAccountName(feedData.get(""));
			 * sIPSTPMasterBO.setBranch(feedData.get(""));
			 * sIPSTPMasterBO.setInstrumentNumber(feedData.get(""));
			 * sIPSTPMasterBO.setChequeMICR(feedData.get(""));
			 * sIPSTPMasterBO.setAccountHolder(feedData.get(""));
			 * sIPSTPMasterBO.setInvestorPAN(feedData.get(""));
			 * sIPSTPMasterBO.setTopUpPercentage(feedData.get(""));
			 * sIPSTPMasterBO.setArnEmpCode(feedData.get(""));
			 * sIPSTPMasterBO.setSubBrokerARN(feedData.get(""));
			 * sIPSTPMasterBO.setLocation(feedData.get(""));
			 * sIPSTPMasterBO.setSchemeCode(feedData.get(""));
			 * sIPSTPMasterBO.setAmcCode(feedData.get(""));
			 * sIPSTPMasterBO.setUserCode(feedData.get(""));
			 */
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			sIPSTPMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
			sIPSTPMasterBO.setAdvisorUser(advUser);
			// sIPSTPMasterBO.setId(investorMasterBOPK);
			//String transNo = feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_TRANSACTION_NUMBER).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_TRANSACTION_NUMBER).toString().trim() != null && !feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_TRANSACTION_NUMBER).toString().trim().isEmpty()) {
				sIPSTPMasterBOList.add(sIPSTPMasterBO);
				isRecordValid = true;
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return sIPSTPMasterBOList;

	}

	private List<SIPSTPMasterBO> populateSIPActiveFranklinModelList(Map<String, String> feedData,
			SIPSTPMasterBODTO sIPSTPMasterBODTO, List<SIPSTPMasterBO> sIPSTPMasterBOList, AdvisorUser advisorUser)
			throws ParseException {
		// TODO Auto-generated method stub

		try {
			SIPSTPMasterBO sIPSTPMasterBO = mapper.map(sIPSTPMasterBODTO, SIPSTPMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			sIPSTPMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_SCHEME_RTA_CODE));
			sIPSTPMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_SCHEME_NAME));
			sIPSTPMasterBO.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_FOLIO_NUMBER)));
			sIPSTPMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_INVESTOR_NAME));
			sIPSTPMasterBO.setTransactionType("SIP");
			sIPSTPMasterBO
					.setTransactionNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_TRANSACTION_NUMBER)));
			sIPSTPMasterBO.setAmount(formatAmount(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_AMOUNT)));
			sIPSTPMasterBO.setFromDate(
					finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_FROM_DATE))));
			sIPSTPMasterBO
					.setToDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_TO_DATE))));
			sIPSTPMasterBO.setTerminationDate(
					finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_TERMINATION_DATE))));
			sIPSTPMasterBO.setFrequency(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_FREQUENCY));
			/*
			 * sIPSTPMasterBO.setPeriodDay(feedData.get(""));
			 * sIPSTPMasterBO.setInvestorMin(feedData.get(""));
			 * sIPSTPMasterBO.setPaymentMode(feedData.get(""));
			 */
			sIPSTPMasterBO.setTargetScheme(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_TARGET_SCHEME));
			sIPSTPMasterBO.setRegistrationDate(
					finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_REGISTRATION_DATE))));
			sIPSTPMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_SUB_BROKER_CODE));
			/*
			 * sIPSTPMasterBO.setRemarks(feedData.get(""));
			 * sIPSTPMasterBO.setTopUpFrequency(feedData.get(""));
			 * sIPSTPMasterBO.setTopUpAmount(feedData.get(""));
			 * sIPSTPMasterBO.setAccountType(feedData.get(""));
			 */
			sIPSTPMasterBO.setAccountName(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_ACOUNT));
			/*
			 * sIPSTPMasterBO.setBranch(feedData.get(""));
			 * sIPSTPMasterBO.setInstrumentNumber(feedData.get(""));
			 * sIPSTPMasterBO.setChequeMICR(feedData.get(""));
			 * sIPSTPMasterBO.setAccountHolder(feedData.get(""));
			 */
			sIPSTPMasterBO.setInvestorPAN(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_INVESTOR_PAN));
			// sIPSTPMasterBO.setTopUpPercentage(feedData.get(""));
			sIPSTPMasterBO.setArnEmpCode(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_ARN_CODE));
			/*
			 * sIPSTPMasterBO.setSubBrokerARN(feedData.get(""));
			 * sIPSTPMasterBO.setLocation(feedData.get(""));
			 * sIPSTPMasterBO.setSchemeCode(feedData.get(""));
			 * sIPSTPMasterBO.setAmcCode(feedData.get(""));
			 * sIPSTPMasterBO.setUserCode(feedData.get(""));
			 */
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			sIPSTPMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
			sIPSTPMasterBO.setAdvisorUser(advUser);
			// sIPSTPMasterBO.setId(investorMasterBOPK);
			//String transNo = feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_TRANSACTION_NUMBER).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_TRANSACTION_NUMBER).toString().trim() != null && !feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_TRANSACTION_NUMBER).toString().trim().isEmpty()) {
				sIPSTPMasterBOList.add(sIPSTPMasterBO);
				isRecordValid = true;
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return sIPSTPMasterBOList;

	}

	private List<SIPSTPMasterBO> populateSIPClosedFranklinModelList(Map<String, String> feedData,
			SIPSTPMasterBODTO sIPSTPMasterBODTO, List<SIPSTPMasterBO> sIPSTPMasterBOList, AdvisorUser advisorUser)
			throws ParseException {
		// TODO Auto-generated method stub

		try {
			SIPSTPMasterBO sIPSTPMasterBO = mapper.map(sIPSTPMasterBODTO, SIPSTPMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			sIPSTPMasterBO.setTransactionNumber(formatNumbers(
					feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_REJECT_TRANSACTION_NUMBER)));
			sIPSTPMasterBO.setRemarks(feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_REJECT_REMARKS));
			sIPSTPMasterBO.setTransactionType("SIP");
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			sIPSTPMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
			sIPSTPMasterBO.setAdvisorUser(advUser);
			// sIPSTPMasterBO.setId(investorMasterBOPK);
			//String transNo = feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_REJECT_TRANSACTION_NUMBER).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_REJECT_TRANSACTION_NUMBER).toString().trim() != null && !feedData.get(FinexaBOColumnConstant.SIP_FRANKLIN_EXCEL_REJECT_TRANSACTION_NUMBER).toString().trim().isEmpty()) {
				sIPSTPMasterBOList.add(sIPSTPMasterBO);
				isRecordValid = true;
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return sIPSTPMasterBOList;

	}

	private List<SIPSTPMasterBO> populateSTPClosedFranklinModelList(Map<String, String> feedData,
			SIPSTPMasterBODTO sIPSTPMasterBODTO, List<SIPSTPMasterBO> sIPSTPMasterBOList, AdvisorUser advisorUser)
			throws ParseException {
		// TODO Auto-generated method stub

		try {
			SIPSTPMasterBO sIPSTPMasterBO = mapper.map(sIPSTPMasterBODTO, SIPSTPMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			sIPSTPMasterBO.setTransactionNumber(formatNumbers(
					feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_REJECT_TRANSACTION_NUMBER)));
			sIPSTPMasterBO.setRemarks(feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_REJECT_REMARKS));
			sIPSTPMasterBO.setTransactionType("STP");
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			sIPSTPMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
			sIPSTPMasterBO.setAdvisorUser(advUser);
			// sIPSTPMasterBO.setId(investorMasterBOPK);
			//String transNo = feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_REJECT_TRANSACTION_NUMBER).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_REJECT_TRANSACTION_NUMBER).toString().trim() != null && !feedData.get(FinexaBOColumnConstant.STP_FRANKLIN_EXCEL_REJECT_TRANSACTION_NUMBER).toString().trim().isEmpty()) {
				sIPSTPMasterBOList.add(sIPSTPMasterBO);
				isRecordValid = true;
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return sIPSTPMasterBOList;

	}

	private UploadResponseDTO readDBFFeed(SIPSTPMasterBODTO sIPSTPMasterBODTO, UploadResponseDTO uploadResponseDTO)
			throws IOException, InvalidFormatException, ParseException {

		InputStream initialStream = sIPSTPMasterBODTO.getNameSelectFile()[0].getInputStream();
		InputStream initialStream2 = sIPSTPMasterBODTO.getNameSelectFile()[0].getInputStream();
		dbfReader = new DBFReader(initialStream);
		boolean headerFound = false, firstRow;
		int numberOfFields;
		String headerPropFile, key, columnPropFile;
		Properties prop1 = new Properties();
		Object[] rowObjects1, rowObjects2;
		int batchCount = 0, counter = 0, headerRowCount = 0, row, numberOfRecords = 0, numberOfRecordRunning = 0;
		List<SIPSTPMasterBO> sipSTPMasterBOList = new ArrayList<SIPSTPMasterBO>();
		List<String> fileColNames = new ArrayList<String>();
		List<String> propColNames = new ArrayList<String>();
		Map<String, String> dataMap = new HashMap<>();
		Map<String, String> sipSTPFeedMap = new HashMap<>();

		// Number of columns in the DBF file
		numberOfFields = dbfReader.getFieldCount();
		numberOfRecords = dbfReader.getRecordCount();
		headerPropFile = "backOfficeProperties/sipSTPDBF.properties";
		ClassLoader classLoader1 = getClass().getClassLoader();
		FileReader fileReader1 = new FileReader(classLoader1.getResource(headerPropFile).getFile());
		prop1.load(fileReader1);
		if (Integer.parseInt(rtaId) > 2)
			key = fileName;
		else
			key = rtaId;
		rowObjects1 = dbfReader.nextRecord();
		firstRow = false;
		while (dbfReader.nextRecord() != null) {
			headerRowCount++;
			if (firstRow == false) {
				for (int i = 0; i < numberOfFields; i++) {
					DBFField field = dbfReader.getField(i);
					String fileColName = field.getName();
					String propColName = prop1.getProperty(key);
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
			sipSTPFeedMap.put(i.toString(), fileColname);
		}

		prop1 = new Properties();
		switch (key) {

		case "2":
			columnPropFile = "backOfficeProperties/sipSTPKarvyDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case "25":
			columnPropFile = "backOfficeProperties/sipActiveFranklinDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case "26":
			columnPropFile = "backOfficeProperties/stpActiveFranklinDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case "27":
			columnPropFile = "backOfficeProperties/sipClosedFranklinDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		default:
			break;

		}

		for (Map.Entry<String, String> entry : sipSTPFeedMap.entrySet()) {
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

		AdvisorUser advisorUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
		dbfReader = new DBFReader(initialStream2);
		row = 0;
		rowObjects2 = dbfReader.nextRecord();
		rejectedRowNumber = headerRowCount;
		// Starts reading after the header row
		while (rowObjects2 != null) {
			if (++row <= headerRowCount)
				continue;
			rejectedRowNumber++;
			numberOfRecordRunning++;

			for (int columnIndex = 0; columnIndex < numberOfFields; columnIndex++) {
				Integer fieldIndex = columnIndex;
				dataMap.put(sipSTPFeedMap.get(fieldIndex.toString()), String.valueOf(rowObjects2[columnIndex]).trim());

			}

			if (batchCount == 20) {
				System.out.println("sipSTPMasterBOList size() when batchCount is 20: " + sipSTPMasterBOList.size());
				uploadResponseDTO = saveSIPSTPData(sipSTPMasterBOList, uploadResponseDTO);
				sipSTPMasterBOList = new ArrayList<SIPSTPMasterBO>();
				batchCount = 0;
				counter++;
				System.out.println("Counter displayed" + counter);
				System.out.println("*********************************");

				switch (rtaId) {

				case RTA_FILE_TYPE_CAMS:
					sipSTPMasterBOList = populateDBFCAMSModelList(dataMap, sIPSTPMasterBODTO, sipSTPMasterBOList,
							advisorUser);
					break;

				case RTA_FILE_TYPE_KARVY:
					sipSTPMasterBOList = populateDBFKARVYModelList(dataMap, sIPSTPMasterBODTO, sipSTPMasterBOList,
							advisorUser);
					break;

				case RTA_FILE_TYPE_FRANKLIN:
					if (fileName.equals(FRANKLIN_ACTIVE_SIP)) {
						sipSTPMasterBOList = populateDBFSIPActiveFranklinModelList(dataMap, sIPSTPMasterBODTO,
								sipSTPMasterBOList, advisorUser);
						break;
					} else if (fileName.equals(FRANKLIN_CLOSED_SIP)) {
						sipSTPMasterBOList = populateDBFSIPClosedFranklinModelList(dataMap, sIPSTPMasterBODTO,
								sipSTPMasterBOList, advisorUser);
						break;
					} else if (fileName.equals(FRANKLIN_ACTIVE_STP)) {
						sipSTPMasterBOList = populateDBFSTPActiveFranklinModelList(dataMap, sIPSTPMasterBODTO,
								sipSTPMasterBOList, advisorUser);
						break;
					} else if (fileName.equals(FRANKLIN_CLOSED_STP)) {
						sipSTPMasterBOList = populateDBFSTPClosedFranklinModelList(dataMap, sIPSTPMasterBODTO,
								sipSTPMasterBOList, advisorUser);
						break;
					}
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
					sipSTPMasterBOList = populateDBFCAMSModelList(dataMap, sIPSTPMasterBODTO, sipSTPMasterBOList,
							advisorUser);
					break;

				case RTA_FILE_TYPE_KARVY:
					sipSTPMasterBOList = populateDBFKARVYModelList(dataMap, sIPSTPMasterBODTO, sipSTPMasterBOList,
							advisorUser);
					break;

				case RTA_FILE_TYPE_FRANKLIN:
					if (fileName.equals(FRANKLIN_ACTIVE_SIP)) {
						sipSTPMasterBOList = populateDBFSIPActiveFranklinModelList(dataMap, sIPSTPMasterBODTO,
								sipSTPMasterBOList, advisorUser);
						break;
					} else if (fileName.equals(FRANKLIN_CLOSED_SIP)) {
						sipSTPMasterBOList = populateDBFSIPClosedFranklinModelList(dataMap, sIPSTPMasterBODTO,
								sipSTPMasterBOList, advisorUser);
						break;
					} else if (fileName.equals(FRANKLIN_ACTIVE_STP)) {
						sipSTPMasterBOList = populateDBFSTPActiveFranklinModelList(dataMap, sIPSTPMasterBODTO,
								sipSTPMasterBOList, advisorUser);
						break;
					} else if (fileName.equals(FRANKLIN_CLOSED_STP)) {
						sipSTPMasterBOList = populateDBFSTPClosedFranklinModelList(dataMap, sIPSTPMasterBODTO,
								sipSTPMasterBOList, advisorUser);
						break;
					}
					break;
				default:
					break;

				}

				// System.out.println("transactionMasterBOList size(): " +
				// transactionMasterBOList.size());

				batchCount++;
			}
			if(numberOfRecordRunning == numberOfRecords)
				break;
			rowObjects2 = dbfReader.nextRecord();
		}
		if (batchCount > 0) {
			System.out.println("sipSTPMasterBOList size() before last: " + sipSTPMasterBOList.size());
			uploadResponseDTO = saveSIPSTPData(sipSTPMasterBOList, uploadResponseDTO);
		}
		// System.out.println(dataMap);

		uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		return uploadResponseDTO;

	}

	private List<SIPSTPMasterBO> populateDBFCAMSModelList(Map<String, String> feedData,
			SIPSTPMasterBODTO sIPSTPMasterBODTO, List<SIPSTPMasterBO> sipSTPMasterBOList, AdvisorUser advisorUser)
			throws ParseException {
		// TODO Auto-generated method stub

		try {
			SIPSTPMasterBO sIPSTPMasterBO = mapper.map(sIPSTPMasterBODTO, SIPSTPMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			sIPSTPMasterBO.setSchemeRTACode(feedData.get("PRODUCT"));
			sIPSTPMasterBO.setSchemeName(feedData.get("SCHEME"));
			sIPSTPMasterBO.setFolioNumber(formatNumbers(feedData.get("FOLIO_NO")));
			sIPSTPMasterBO.setInvestorName(feedData.get("INV_NAME"));
			sIPSTPMasterBO.setTransactionType(feedData.get("AUT_TRNTYP"));
			sIPSTPMasterBO.setTransactionNumber(formatNumbers(feedData.get("AUTO_TRNO")));
			sIPSTPMasterBO.setAmount(formatAmount(feedData.get("AUTO_AMOUN")));
			sIPSTPMasterBO.setFromDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("FROM_DATE"))));
			sIPSTPMasterBO.setToDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("TO_DATE"))));
			sIPSTPMasterBO
					.setTerminationDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("CEASE_DATE"))));
			sIPSTPMasterBO.setFrequency(feedData.get("PERIODICIT"));
			sIPSTPMasterBO.setPeriodDay(feedData.get("PERIOD_DAY"));
			sIPSTPMasterBO.setInvestorMin(feedData.get("INV_IIN"));
			sIPSTPMasterBO.setPaymentMode(feedData.get("PAYMENT_MO"));
			sIPSTPMasterBO.setTargetScheme(feedData.get("TARGET_SCH"));
			sIPSTPMasterBO
					.setRegistrationDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("REG_DATE"))));
			sIPSTPMasterBO.setSubBrokerCode(feedData.get("SUBBROKER"));
			sIPSTPMasterBO.setRemarks(feedData.get("REMARKS"));
			sIPSTPMasterBO.setTopUpFrequency(feedData.get("TOP_UP_FRQ"));
			sIPSTPMasterBO.setTopUpAmount(feedData.get("TOP_UP_AMT"));
			sIPSTPMasterBO.setAccountType(feedData.get("AC_TYPE"));
			sIPSTPMasterBO.setAccountName(feedData.get("BANK"));
			sIPSTPMasterBO.setBranch(feedData.get("BRANCH"));
			sIPSTPMasterBO.setInstrumentNumber(feedData.get("INSTRM_NO"));
			sIPSTPMasterBO.setChequeMICR(feedData.get("CHEQ_MICR_"));
			sIPSTPMasterBO.setAccountHolder(feedData.get("AC_HOLDER_"));
			sIPSTPMasterBO.setInvestorPAN(feedData.get("PAN"));
			sIPSTPMasterBO.setTopUpPercentage(feedData.get("TOP_UP_PER"));
			sIPSTPMasterBO.setArnEmpCode(feedData.get("EUIN"));
			sIPSTPMasterBO.setSubBrokerARN(feedData.get("SUB_ARN_CO"));
			sIPSTPMasterBO.setLocation(feedData.get("TER_LOCATI"));
			sIPSTPMasterBO.setSchemeCode(feedData.get("SCHEME_COD"));
			sIPSTPMasterBO.setAmcCode(feedData.get("AMC_CODE"));
			sIPSTPMasterBO.setUserCode(feedData.get("USER_CODE"));
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");
			sIPSTPMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
			sIPSTPMasterBO.setAdvisorUser(advUser);
			// sIPSTPMasterBO.setId(investorMasterBOPK);
			String transNo = feedData.get("AUTO_TRNO").toString().trim();
			if (transNo != null && !transNo.isEmpty()) {
				sipSTPMasterBOList.add(sIPSTPMasterBO);
				isRecordValid = true;
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return sipSTPMasterBOList;

	}

	private List<SIPSTPMasterBO> populateDBFKARVYModelList(Map<String, String> feedData,
			SIPSTPMasterBODTO sIPSTPMasterBODTO, List<SIPSTPMasterBO> sIPSTPMasterBOList, AdvisorUser advisorUser)
			throws ParseException {
		// TODO Auto-generated method stub

		try {
			SIPSTPMasterBO sIPSTPMasterBO = mapper.map(sIPSTPMasterBODTO, SIPSTPMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			sIPSTPMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_SCHEME_RTA_CODE));
			sIPSTPMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_SCHEME_NAME));
			sIPSTPMasterBO.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_FOLIO_NUMBER)));
			sIPSTPMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_INVESTOR_NAME));
			sIPSTPMasterBO.setTransactionType(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_TRANSACTION_TYPE));
			sIPSTPMasterBO
					.setTransactionNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_TRANSACTION_NUMBER)));
			sIPSTPMasterBO.setAmount(formatAmount(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_AMOUNT)));
			sIPSTPMasterBO.setFromDate(utilToSQLDate(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_FROM_DATE)));
			sIPSTPMasterBO.setToDate(utilToSQLDate(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_TO_DATE)));
			sIPSTPMasterBO.setTerminationDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_TERMINATION_DATE)));
			sIPSTPMasterBO.setFrequency(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_FREQUENCY));
			/*
			 * sIPSTPMasterBO.setPeriodDay(feedData.get(""));
			 * sIPSTPMasterBO.setInvestorMin(feedData.get(""));
			 * sIPSTPMasterBO.setPaymentMode(feedData.get(""));
			 */
			sIPSTPMasterBO.setTargetScheme(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_TARGET_SCHEME));
			sIPSTPMasterBO.setRegistrationDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_REGISTRATION_DATE)));
			// System.out.println(feedData.get("RegistrationDate"));
			// System.out.println(finexaUtil.formatDate(feedData.get("RegistrationDate")));
			sIPSTPMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_SUB_BROKER_CODE));
			sIPSTPMasterBO.setInvestorPAN(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_INVESTOR_PAN));
			// sIPSTPMasterBO.setTopUpFrequency(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_LOCATION_CATEGORY));
			/*
			 * sIPSTPMasterBO.setTopUpAmount(feedData.get(""));
			 * sIPSTPMasterBO.setAccountType(feedData.get(""));
			 * sIPSTPMasterBO.setAccountName(feedData.get(""));
			 * sIPSTPMasterBO.setBranch(feedData.get(""));
			 * sIPSTPMasterBO.setInstrumentNumber(feedData.get(""));
			 * sIPSTPMasterBO.setChequeMICR(feedData.get(""));
			 * sIPSTPMasterBO.setAccountHolder(feedData.get(""));
			 * sIPSTPMasterBO.setRemarks(feedData.get(""));
			 * sIPSTPMasterBO.setTopUpPercentage(feedData.get(""));
			 * sIPSTPMasterBO.setArnEmpCode(feedData.get(""));
			 * sIPSTPMasterBO.setSubBrokerARN(feedData.get(""));
			 * sIPSTPMasterBO.setLocation(feedData.get(""));
			 * sIPSTPMasterBO.setSchemeCode(feedData.get(""));
			 */
			sIPSTPMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_AMC_CODE));
			// sIPSTPMasterBO.setUserCode(feedData.get(""));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			sIPSTPMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
			sIPSTPMasterBO.setAdvisorUser(advUser);
			// sIPSTPMasterBO.setId(investorMasterBOPK);
			String transNo = feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_KARVY_TRANSACTION_NUMBER).toString().trim();
			if (transNo != null && !transNo.isEmpty()) {
				sIPSTPMasterBOList.add(sIPSTPMasterBO);
				isRecordValid = true;
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return sIPSTPMasterBOList;

	}

	private List<SIPSTPMasterBO> populateDBFSTPActiveFranklinModelList(Map<String, String> feedData,
			SIPSTPMasterBODTO sIPSTPMasterBODTO, List<SIPSTPMasterBO> sIPSTPMasterBOList, AdvisorUser advisorUser)
			throws ParseException {
		// TODO Auto-generated method stub

		try {
			SIPSTPMasterBO sIPSTPMasterBO = mapper.map(sIPSTPMasterBODTO, SIPSTPMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			sIPSTPMasterBO.setSchemeRTACode(
					feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_SCHEME_RTA_CODE));
			sIPSTPMasterBO
					.setSchemeName(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_SCHEME_NAME));
			sIPSTPMasterBO
					.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_FOLIO_NUMBER)));
			sIPSTPMasterBO
					.setInvestorName(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_INVESTOR_NAME));
			sIPSTPMasterBO.setTransactionType("STP");
			sIPSTPMasterBO.setTransactionNumber(formatNumbers(
					feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_TRANSACTION_NUMBER)));
			sIPSTPMasterBO.setAmount(formatAmount(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_AMOUNT)));
			sIPSTPMasterBO.setFromDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_FROM_DATE)));
			sIPSTPMasterBO.setToDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_TO_DATE)));
			// sIPSTPMasterBO.setTerminationDate(finexaUtil.formatDate(feedData.get("")));
			sIPSTPMasterBO.setFrequency(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_FREQUENCY));
			/*
			 * sIPSTPMasterBO.setPeriodDay(feedData.get(""));
			 * sIPSTPMasterBO.setInvestorMin(feedData.get(""));
			 * sIPSTPMasterBO.setPaymentMode(feedData.get(""));
			 */
			sIPSTPMasterBO
					.setTargetScheme(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TARGET_SCHEME));
			System.out.println("RegistrationDate: "
					+ feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_REGISTRATION_DATE));

			sIPSTPMasterBO.setRegistrationDate(finexaUtil.formatDate(
					feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_REGISTRATION_DATE)));
			sIPSTPMasterBO
					.setInvestorPAN(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_INVESTOR_PAN));
			sIPSTPMasterBO.setAmcCode(feedData.get("Franklin Mutual Fund"));
			/*
			 * 
			 * sIPSTPMasterBO.setSubBrokerCode(feedData.get(""));
			 * sIPSTPMasterBO.setRemarks(feedData.get(""));
			 * sIPSTPMasterBO.setTopUpFrequency(feedData.get(""));
			 * sIPSTPMasterBO.setTopUpAmount(feedData.get(""));
			 * sIPSTPMasterBO.setAccountType(feedData.get(""));
			 * sIPSTPMasterBO.setAccountName(feedData.get(""));
			 * sIPSTPMasterBO.setBranch(feedData.get(""));
			 * sIPSTPMasterBO.setInstrumentNumber(feedData.get(""));
			 * sIPSTPMasterBO.setChequeMICR(feedData.get(""));
			 * sIPSTPMasterBO.setAccountHolder(feedData.get(""));
			 * 
			 * sIPSTPMasterBO.setTopUpPercentage(feedData.get(""));
			 * sIPSTPMasterBO.setArnEmpCode(feedData.get(""));
			 * sIPSTPMasterBO.setSubBrokerARN(feedData.get(""));
			 * sIPSTPMasterBO.setLocation(feedData.get(""));
			 * sIPSTPMasterBO.setSchemeCode(feedData.get(""));
			 * 
			 * sIPSTPMasterBO.setUserCode(feedData.get(""));
			 */
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			sIPSTPMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
			sIPSTPMasterBO.setAdvisorUser(advUser);
			// sIPSTPMasterBO.setId(investorMasterBOPK);
			String transNo = feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_STP_TRANSACTION_NUMBER)
					.toString().trim();
			if (transNo != null && !transNo.isEmpty()) {
				sIPSTPMasterBOList.add(sIPSTPMasterBO);
				isRecordValid = true;
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return sIPSTPMasterBOList;

	}

	private List<SIPSTPMasterBO> populateDBFSIPActiveFranklinModelList(Map<String, String> feedData,
			SIPSTPMasterBODTO sIPSTPMasterBODTO, List<SIPSTPMasterBO> sIPSTPMasterBOList, AdvisorUser advisorUser)
			throws ParseException {
		// TODO Auto-generated method stub

		try {
			SIPSTPMasterBO sIPSTPMasterBO = mapper.map(sIPSTPMasterBODTO, SIPSTPMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			sIPSTPMasterBO.setSchemeRTACode(
					feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_SCHEME_RTA_CODE));
			sIPSTPMasterBO
					.setSchemeName(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_SCHEME_NAME));
			sIPSTPMasterBO
					.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_FOLIO_NUMBER)));
			sIPSTPMasterBO
					.setInvestorName(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_INVESTOR_NAME));
			sIPSTPMasterBO.setTransactionType("SIP");
			sIPSTPMasterBO.setTransactionNumber(formatNumbers(
					feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TRANSACTION_NUMBER)));
			sIPSTPMasterBO.setAmount(formatAmount(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_AMOUNT)));
			sIPSTPMasterBO.setFromDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_FROM_DATE)));
			sIPSTPMasterBO.setToDate(
					utilToSQLDate(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TO_DATE)));
			sIPSTPMasterBO.setTerminationDate(finexaUtil
					.formatDate(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TERMINATION_DATE)));
			sIPSTPMasterBO.setFrequency(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_FREQUENCY));
			/*
			 * sIPSTPMasterBO.setPeriodDay(feedData.get(""));
			 * sIPSTPMasterBO.setInvestorMin(feedData.get(""));
			 * sIPSTPMasterBO.setPaymentMode(feedData.get(""));
			 */
			// sIPSTPMasterBO.setTargetScheme(feedData.get(FinexaBOColumnConstant.));
			sIPSTPMasterBO.setRegistrationDate(utilToSQLDate(
					feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_REGISTRATION_DATE)));
			sIPSTPMasterBO.setSubBrokerCode(
					feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_SUB_BROKER_CODE));
			/*
			 * sIPSTPMasterBO.setRemarks(feedData.get(""));
			 * sIPSTPMasterBO.setTopUpFrequency(feedData.get(""));
			 * sIPSTPMasterBO.setTopUpAmount(feedData.get(""));
			 * sIPSTPMasterBO.setAccountType(feedData.get(""));
			 */
			// sIPSTPMasterBO.setAccountName(feedData.get(FinexaBOColumnConstant.));
			/*
			 * sIPSTPMasterBO.setBranch(feedData.get(""));
			 * sIPSTPMasterBO.setInstrumentNumber(feedData.get(""));
			 * sIPSTPMasterBO.setChequeMICR(feedData.get(""));
			 * sIPSTPMasterBO.setAccountHolder(feedData.get(""));
			 */
			sIPSTPMasterBO
					.setInvestorPAN(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_INVESTOR_PAN));
			// sIPSTPMasterBO.setTopUpPercentage(feedData.get(""));
			// sIPSTPMasterBO.setArnEmpCode(feedData.get(FinexaBOColumnConstant.));
			sIPSTPMasterBO.setAmcCode("Franklin Mutual Fund");
			sIPSTPMasterBO
					.setLocation(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_LOCATION_CATEGORY));
			/*
			 * sIPSTPMasterBO.setSubBrokerARN(feedData.get(""));
			 * sIPSTPMasterBO.setLocation(feedData.get(""));
			 * sIPSTPMasterBO.setSchemeCode(feedData.get(""));
			 * 
			 * sIPSTPMasterBO.setUserCode(feedData.get(""));
			 */
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			sIPSTPMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
			sIPSTPMasterBO.setAdvisorUser(advUser);
			// sIPSTPMasterBO.setId(investorMasterBOPK);
			String transNo = feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TRANSACTION_NUMBER)
					.toString().trim();
			if (transNo != null && !transNo.isEmpty()) {
				sIPSTPMasterBOList.add(sIPSTPMasterBO);
				isRecordValid = true;
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return sIPSTPMasterBOList;

	}

	private List<SIPSTPMasterBO> populateDBFSIPClosedFranklinModelList(Map<String, String> feedData,
			SIPSTPMasterBODTO sIPSTPMasterBODTO, List<SIPSTPMasterBO> sIPSTPMasterBOList, AdvisorUser advisorUser)
			throws ParseException {
		// TODO Auto-generated method stub

		try {
			SIPSTPMasterBO sIPSTPMasterBO = mapper.map(sIPSTPMasterBODTO, SIPSTPMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			sIPSTPMasterBO.setTransactionNumber(formatNumbers(
					feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_CLOSED_SIP_TRANSACTION_NUMBER)));
			sIPSTPMasterBO.setRemarks(feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_CLOSED_SIP_REMARKS));
			sIPSTPMasterBO.setTransactionType("SIP");
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			sIPSTPMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
			sIPSTPMasterBO.setAdvisorUser(advUser);
			// sIPSTPMasterBO.setId(investorMasterBOPK);
			String transNo = feedData.get(FinexaBOColumnConstant.SIPSTP_DBF_FRANKLIN_CLOSED_SIP_REMARKS).toString()
					.trim();
			if (transNo != null && !transNo.isEmpty()) {
				sIPSTPMasterBOList.add(sIPSTPMasterBO);
				isRecordValid = true;
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return sIPSTPMasterBOList;

	}

	private List<SIPSTPMasterBO> populateDBFSTPClosedFranklinModelList(Map<String, String> feedData,
			SIPSTPMasterBODTO sIPSTPMasterBODTO, List<SIPSTPMasterBO> sIPSTPMasterBOList, AdvisorUser advisorUser)
			throws ParseException {
		// TODO Auto-generated method stub

		try {
			SIPSTPMasterBO sIPSTPMasterBO = mapper.map(sIPSTPMasterBODTO, SIPSTPMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			sIPSTPMasterBO.setTransactionNumber(formatNumbers(feedData.get("SI NO")));
			sIPSTPMasterBO.setRemarks(feedData.get("Stop_Reason"));
			sIPSTPMasterBO.setTransactionType("STP");
			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			sIPSTPMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(sIPSTPMasterBODTO.getAdvisorId());
			sIPSTPMasterBO.setAdvisorUser(advUser);
			// sIPSTPMasterBO.setId(investorMasterBOPK);
			String transNo = feedData.get("SI NO").toString().trim();
			if (transNo != null && !transNo.isEmpty()) {
				sIPSTPMasterBOList.add(sIPSTPMasterBO);
				isRecordValid = true;
			}
			if (isRecordValid == false) {
				rejectedRowNumbers = rejectedRowNumbers+rejectedRowNumber.toString()+",";
				noOfRejectedRecords++;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return sIPSTPMasterBOList;

	}

	private synchronized UploadResponseDTO saveSIPSTPData(List<SIPSTPMasterBO> sIPSTPMasterBODTOList,
			UploadResponseDTO uploadResponseDTO) {
		// TODO Auto-generated method stub

		try {
			if (sIPSTPMasterBODTOList != null && sIPSTPMasterBODTOList.size() > 0) {
				sIPSTPMasterBORepository.save(sIPSTPMasterBODTOList);

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

		if (date != null && !date.isEmpty() && !date.contains("null") && date.trim() != "") {
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