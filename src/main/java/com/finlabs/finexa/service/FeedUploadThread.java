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
import com.finlabs.finexa.repository.LookupRTAMasterFileDetailsBORepository;
import com.finlabs.finexa.repository.LookupTransactionRuleRepository;
import com.finlabs.finexa.util.FinexaBOColumnConstant;
import com.finlabs.finexa.util.FinexaUtil;
import com.ibm.icu.text.DecimalFormat;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

@Component

@Scope("prototype")

public class FeedUploadThread implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeedUploadThread.class);
	private AumMasterBODTO aumMasterBODTO;
	private UploadResponseDTO uploadResponseDTO;
	private BackOfficeUploadHistory backOfficeUploadHistory;
	private static final String RTA_FILE_TYPE_CAMS = "1";
	private static final String RTA_FILE_TYPE_KARVY = "2";
	private static final String RTA_FILE_TYPE_FRANKLIN = "3";
	private static final String RTA_FILE_TYPE_SUNDARAM = "4";
	private static final String CAMS_AUM22 = "22";
	private static final String CAMS_AUM53 = "53";
	public static final String STATUS_COMPLETED = "COMPLETED";
	public static final String STATUS_REJECTED = "REJECTED";
	
	public static final String STATUS_NOT_APPLICABLE = "NOT APPLICABLE";
	public static final String STATUS_RUNNING = "RUNNING";
	public static final String FAILURE = "Failure";
	public static final String SUCCESS = "Success";
	public static final String RECORD_REJECTION_MESSAGE = "Investor Folio or Report Date or Scheme RTA Code or all of the three fields were missing in each record. Row numbers are: ";
	public static final String NOT_REJECTED_MESSAGE = "All records are successfully uploaded.";
	public static final String FILE_REJECTION_MESSAGE = "One or more columns were not found in file.";
	public static final String FILE_NOT_PROCESSED_MESSAGE = "Could not process the file.";
	private DecimalFormat decimalFormat = new DecimalFormat("#");
	private Properties prop = new Properties();
	private Integer rta;
	private String rtaId;
	private String FEED_PROPERTIES_FILE;
	private String rejectedRowNumbers = "";
	private Integer rejectedRowNumber = 0;
	private int rows = 1, columnHeaderRow = 0, noOfRejectedRecords = 0, i = 0;
	private int camsFeedNumber;
	private DBFField field = new DBFField();
	private boolean allRowsInserted = false;
	DBFReader dbfReader;
	@Autowired
	private Mapper mapper;

	@Autowired
	private AumMasterBORepository aumMasterBORepository;

	@Autowired
	private LookupRTABORepository lookupRTABORepository;

	@Autowired
	private LookupTransactionRuleRepository lookupTransactionRuleRepository;

	@Autowired
	private LookupRTAMasterFileDetailsBORepository lookupRTAMasterFileDetailsBORepository;

	@Autowired
	BackOfficeUploadHistoryRepository uploadHistoryRepo;

	@Autowired
	AdvisorUserRepository advisorUserRepository;

	public void initialize(AumMasterBODTO aumMasterBODTO, UploadResponseDTO uploadResponseDTO,
			BackOfficeUploadHistory backOfficeUploadHistory) {
		this.aumMasterBODTO = aumMasterBODTO;
		this.uploadResponseDTO = uploadResponseDTO;
		this.backOfficeUploadHistory = backOfficeUploadHistory;
	}

	@Override
	public void run() {
		try {
			// log.debug("InvestorMasterBOServiceImpl >> uploadInvestorCAMSFeed");

			rta = aumMasterBODTO.getNameRTA();
			rtaId = rta.toString();
			String name = aumMasterBODTO.getNameSelectFile()[0].getOriginalFilename();
			if (name.contains(".dbf") || name.contains(".DBF")) {
				uploadResponseDTO = readDBFFeed(aumMasterBODTO, uploadResponseDTO);
			} else {
				uploadResponseDTO = readAUMExcelFeed(aumMasterBODTO, uploadResponseDTO);
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
			System.out.println("AUM Upload Finished");
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
	private UploadResponseDTO readAUMExcelFeed(AumMasterBODTO aumMasterBODTO, UploadResponseDTO uploadResponseDTO)
			throws IOException, InvalidFormatException, ParseException {

		// null pointer handling

		InputStream initialStream = aumMasterBODTO.getNameSelectFile()[0].getInputStream();

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
		System.out.println("Rows" + rows);

		// Iterating over Rows and Columns using Iterator
		Iterator<Row> rowIterator = sheet.rowIterator();
		// System.out.println("FileName:
		// "+aumMasterBODTO.getNameSelectFile()[0].getOriginalFilename());

		switch (rtaId) {

		case RTA_FILE_TYPE_CAMS:
			if (aumMasterBODTO.getNameSelectFile()[0].getOriginalFilename().contains(CAMS_AUM22)) {
				FEED_PROPERTIES_FILE = "backOfficeProperties/aumCAMS22.properties";
				camsFeedNumber = 22;
				break;
			} else if (aumMasterBODTO.getNameSelectFile()[0].getOriginalFilename().contains(CAMS_AUM53)) {
				FEED_PROPERTIES_FILE = "backOfficeProperties/aumCAMS53.properties";
				camsFeedNumber = 53;
				break;
			}
			break;
		case RTA_FILE_TYPE_KARVY:
			FEED_PROPERTIES_FILE = "backOfficeProperties/aumKarvy.properties";
			break;
		case RTA_FILE_TYPE_FRANKLIN:
			FEED_PROPERTIES_FILE = "backOfficeProperties/aumFranklin.properties";
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
		// LookupRTAMasterFileDetailsBO lookupRTAMasterFileDetailsBO =
		// lookupRTAMasterFileDetailsBORepository.findByFileCode("AUMM");

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
			camsMandatoryFeedList.add(FinexaBOColumnConstant.AUM_CAMS_EXCEL_FOLIO_NUMBER);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.AUM_CAMS_EXCEL_SCHEME_RTA_CODE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.AUM_CAMS_EXCEL_REPORT_DATE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.AUM_CAMS_EXCEL_UNIT_BALANCE);
			//camsMandatoryFeedList.add(FinexaBOColumnConstant.AUM_CAMS_EXCEL_SUB_BROKER_CODE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.AUM_CAMS_EXCEL_SCHEME_NAME);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.AUM_CAMS_EXCEL_NAV);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.AUM_CAMS_EXCEL_INVESTOR_NAME);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.AUM_CAMS_EXCEL_DISTRIBUTOR_ARN_CODE);
			camsMandatoryFeedList.add(FinexaBOColumnConstant.AUM_CAMS_EXCEL_CURRENT_VALUE);
			
			
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
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.AUM_KARVY_EXCEL_FOLIO_NUMBER);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.AUM_KARVY_EXCEL_SCHEME_RTA_CODE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.AUM_KARVY_EXCEL_REPORT_DATE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.AUM_KARVY_EXCEL_UNIT_BALANCE);
			//karvyMandatoryFeedList.add(FinexaBOColumnConstant.AUM_KARVY_EXCEL_SUB_BROKER_CODE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.AUM_KARVY_EXCEL_SCHEME_NAME);
		//	karvyMandatoryFeedList.add(FinexaBOColumnConstant.AUM_KARVY_EXCEL_PINCODE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.AUM_KARVY_EXCEL_NAV);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.AUM_KARVY_EXCEL_INVESTOR_NAME);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.AUM_KARVY_EXCEL_DISTRIBUTOR_ARN_CODE);
			karvyMandatoryFeedList.add(FinexaBOColumnConstant.AUM_KARVY_EXCEL_CURRENT_VALUE);
		//	karvyMandatoryFeedList.add(FinexaBOColumnConstant.AUM_KARVY_EXCEL_AMC_CODE);
		
			
			
			for (Map.Entry<String, String> obj : excelNameColNameMap.entrySet()) {
				String key = obj.getKey();
				String value = obj.getValue();
				if (value.equals("nu") && karvyMandatoryFeedList.contains(key)) { // implies mandatory column null
					uploadResponseDTO.setPrimaryKeyNotFound(true);
					System.out.println(key+" Address Null");
					uploadResponseDTO.setStatus(false);
					return uploadResponseDTO;
				}
			}
			break;
		case RTA_FILE_TYPE_FRANKLIN:
			List<String> franklinMandatoryFeedList = new ArrayList<>();
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_FOLIO_NUMBER);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_SCHEME_RTA_CODE);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_REPORT_DATE);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_UNIT_BALANCE);
			//franklinMandatoryFeedList.add(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_SUB_BROKER_CODE);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_SCHEME_NAME);
			//franklinMandatoryFeedList.add(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_PINCODE);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_NAV);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_INVESTOR_NAME);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_DISTRIBUTOR_ARN_CODE);
			franklinMandatoryFeedList.add(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_CURRENT_VALUE);
			
				
			for (Map.Entry<String, String> obj : excelNameColNameMap.entrySet()) {
				String key = obj.getKey();
				String value = obj.getValue();
				if (value.equals("nu") && franklinMandatoryFeedList.contains(key)) { // implies mandatory column null
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
		
		
		
	/*	for (String columnAddress : excelNameColNameMap.values())
			if (columnAddress.equals("nu")) {
				uploadResponseDTO.setPrimaryKeyNotFound(true);
				System.out.println("Excel Column Address Null");
				uploadResponseDTO.setStatus(false);
				return uploadResponseDTO;
			}
*/
		List<AumMasterBO> aumMasterBOList = new ArrayList<AumMasterBO>();

		// Empty the list each time after inserting 20 records
		int batchCount = 0;

		// System.out.println("AdvisorID: " + aumMasterBODTO.getAdvisorId());

		// try-catch
		AdvisorUser advisorUser = advisorUserRepository.findOne(aumMasterBODTO.getAdvisorId());
		// System.out.println("Inside readExcel method");

		while (rowIterator.hasNext()) {

			// This row variable is used to iterate through each row of excel sheet
			Row row = rowIterator.next();
			rejectedRowNumber = row.getRowNum();
			/*if (isEmpty(row)) {
				continue;
			}*/
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
				i++;
				if(i == 3) {
					System.out.println("3");
				}
				System.out.println("LIST size() when batchCount is 20: " + aumMasterBOList.size());
				uploadResponseDTO = saveAumData(aumMasterBOList, uploadResponseDTO);
				aumMasterBOList = new ArrayList<AumMasterBO>();
				batchCount = 0;

				switch (rtaId) {
				case RTA_FILE_TYPE_CAMS:
					if (camsFeedNumber == 22) {
						aumMasterBOList = populateAumCAMS22ModelList(feed, aumMasterBODTO, aumMasterBOList,
								advisorUser);
						break;
					} else if (camsFeedNumber == 53) {
						aumMasterBOList = populateAumCAMS53ModelList(feed, aumMasterBODTO, aumMasterBOList,
								advisorUser);
						break;
					}
					break;
				case RTA_FILE_TYPE_KARVY:
					aumMasterBOList = populateAumKarvyModelList(feed, aumMasterBODTO, aumMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_FRANKLIN:
					aumMasterBOList = populateAumFranklinModelList(feed, aumMasterBODTO, aumMasterBOList, advisorUser);
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
					if (camsFeedNumber == 22) {
						aumMasterBOList = populateAumCAMS22ModelList(feed, aumMasterBODTO, aumMasterBOList,
								advisorUser);
						break;
					} else if (camsFeedNumber == 53) {
						aumMasterBOList = populateAumCAMS53ModelList(feed, aumMasterBODTO, aumMasterBOList,
								advisorUser);
						break;
					}
					break;
				case RTA_FILE_TYPE_KARVY:
					aumMasterBOList = populateAumKarvyModelList(feed, aumMasterBODTO, aumMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_FRANKLIN:
					aumMasterBOList = populateAumFranklinModelList(feed, aumMasterBODTO, aumMasterBOList, advisorUser);
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
			System.out.println("LIST size() when LAST batchCount is 20: " + aumMasterBOList.size());
			uploadResponseDTO = saveAumData(aumMasterBOList, uploadResponseDTO);
			allRowsInserted = true;
		}
		targetFile.delete();
		workbook.close();
		uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		return uploadResponseDTO;

	}

	/*private boolean isEmpty(Row row) {
		// TODO Auto-generated method stub
		boolean toBeConsidered = false;
		Iterator<Cell> cellList = row.cellIterator();
		
		return toBeConsidered;
	}*/

	private List<AumMasterBO> populateAumCAMS22ModelList(Map<String, String> feedData, AumMasterBODTO aumMasterBODTO,
			List<AumMasterBO> aumMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordValid = false;
			AumMasterBO aumMasterBO = mapper.map(aumMasterBODTO, AumMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			AumMasterBOPK aumMasterBOPK = new AumMasterBOPK();
			aumMasterBOPK.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_FOLIO_NUMBER)));
			aumMasterBOPK.setSchemertacode(feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_SCHEME_RTA_CODE));
			aumMasterBOPK.setReportDate(finexaUtil.formatDate(
					finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_REPORT_DATE))));
			aumMasterBO.setUnitBalance(feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_UNIT_BALANCE));
			aumMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_SUB_BROKER_CODE));
			aumMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_SCHEME_NAME));
			// aumMasterBO.setPinCode(feedData.get("Pincode"));
			aumMasterBO.setNav(feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_NAV));
			aumMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_INVESTOR_NAME));
			aumMasterBO.setDistributorarncode(feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_DISTRIBUTOR_ARN_CODE));
			aumMasterBO.setCurrentValue(feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_CURRENT_VALUE));
			// aumMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");
			aumMasterBO.setLookupRtabo(lookupRtabo);
			aumMasterBO.setAdvisorUser(advisorUser);
			aumMasterBO.setId(aumMasterBOPK);
			// java.sql.Date reportDate = new
			// java.sql.Date(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_REPORT_DATE))).getTime());
			// String repdate = reportDate.toString().trim();
			// String folio =
			// feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_FOLIO_NUMBER).toString().trim();
			// String schemeRTACode =
			// feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_SCHEME_RTA_CODE).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_REPORT_DATE) != null
					&& !feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_REPORT_DATE).isEmpty()
					&& feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_REPORT_DATE) != "") {
				if (feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_FOLIO_NUMBER).toString().trim() != null
						&& !feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_FOLIO_NUMBER).toString().trim().isEmpty()
						&& feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_FOLIO_NUMBER).toString().trim() != "") {
					if (feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_SCHEME_RTA_CODE).toString().trim() != null
							&& !feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_SCHEME_RTA_CODE).toString().trim()
									.isEmpty()
							&& feedData.get(FinexaBOColumnConstant.AUM_CAMS_EXCEL_SCHEME_RTA_CODE).toString()
									.trim() != "") {
						aumMasterBOList.add(aumMasterBO);
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

		return aumMasterBOList;

	}

	private List<AumMasterBO> populateAumCAMS53ModelList(Map<String, String> feedData, AumMasterBODTO aumMasterBODTO,
			List<AumMasterBO> aumMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			AumMasterBO aumMasterBO = mapper.map(aumMasterBODTO, AumMasterBO.class);
			System.out.println("Model reference created");
			boolean isRecordValid = false;
			FinexaUtil finexaUtil = new FinexaUtil();
			AumMasterBOPK aumMasterBOPK = new AumMasterBOPK();
			aumMasterBOPK.setFolioNumber(feedData.get("foliochk"));
			aumMasterBOPK.setSchemertacode(feedData.get("product"));
			aumMasterBOPK.setReportDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("bal_date"))));
			/*
			 * aumMasterBO.setUnitBalance(feedData.get("units"));
			 * aumMasterBO.setSubBrokerCode(feedData.get("ae_code"));
			 * aumMasterBO.setSchemeName(feedData.get("scheme_name"));
			 * aumMasterBO.setPinCode(feedData.get("Pincode"));
			 * aumMasterBO.setNav(feedData.get("nav"));
			 * aumMasterBO.setInvestorName(feedData.get("inv_name"));
			 */
			aumMasterBO.setDistributorarncode(feedData.get("brok_dlr_code"));
			aumMasterBO.setCurrentValue(feedData.get("rupee_bal"));
			aumMasterBO.setAmcCode(feedData.get("amc_code"));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");
			aumMasterBO.setLookupRtabo(lookupRtabo);
			aumMasterBO.setAdvisorUser(advisorUser);
			aumMasterBO.setId(aumMasterBOPK);
			java.sql.Date reportDate = new java.sql.Date(
					finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("bal_date"))).getTime());
			String repdate = reportDate.toString().trim();
			String folio = feedData.get("foliochk").toString().trim();
			String schemeRTACode = feedData.get("product").toString().trim();
			if (repdate != null && !repdate.isEmpty()) {
				if (folio != null && !folio.isEmpty()) {
					if (schemeRTACode != null && !schemeRTACode.isEmpty()) {
						aumMasterBOList.add(aumMasterBO);
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

		return aumMasterBOList;

	}

	private List<AumMasterBO> populateAumKarvyModelList(Map<String, String> feedData, AumMasterBODTO aumMasterBODTO,
			List<AumMasterBO> aumMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordValid = false;
			AumMasterBO aumMasterBO = mapper.map(aumMasterBODTO, AumMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();

			AumMasterBOPK aumMasterBOPK = new AumMasterBOPK();
			System.out.println(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_FOLIO_NUMBER));
			aumMasterBOPK.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_FOLIO_NUMBER)));
			System.out.println(aumMasterBOPK.getFolioNumber());
			aumMasterBOPK.setSchemertacode(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_SCHEME_RTA_CODE));
			// System.out.println("REPORT DATE: "+feedData.get("Report Date"));
			aumMasterBOPK.setReportDate(
					finexaUtil.formatDate(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_REPORT_DATE)));
			aumMasterBO.setUnitBalance(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_UNIT_BALANCE));
			aumMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_SUB_BROKER_CODE));
			aumMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_SCHEME_NAME));
			aumMasterBO.setPinCode(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_PINCODE));
			aumMasterBO.setNav(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_NAV));
			aumMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_INVESTOR_NAME));
			aumMasterBO
					.setDistributorarncode(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_DISTRIBUTOR_ARN_CODE));
			aumMasterBO.setCurrentValue(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_CURRENT_VALUE));
			aumMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_AMC_CODE));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			aumMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(aumMasterBODTO.getAdvisorId());
			aumMasterBO.setAdvisorUser(advUser);
			aumMasterBO.setId(aumMasterBOPK);
			/*
			 * System.out.println("FEED " + feedData.get("Report Date"));
			 * System.out.println("FORMAT " +
			 * finexaUtil.formatDate(feedData.get("Report Date")));
			 * System.out.println("MODEL " + aumMasterBOPK.getReportDate());
			 */
			// java.sql.Date reportDate = new
			// java.sql.Date(finexaUtil.formatDate(feedData.get("Report Date")).getTime());
			// String repdate = aumMasterBOPK.getReportDate().toString().trim();
			// String folio =
			// feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_FOLIO_NUMBER).toString().trim();
			// String schemeRTACode =
			// feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_SCHEME_RTA_CODE).toString().trim();
			// System.out.println("REPO:
			// "+feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_REPORT_DATE));
			if (feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_REPORT_DATE).toString().trim() != null
					&& !feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_REPORT_DATE).toString().trim().isEmpty()
					&& feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_REPORT_DATE).toString().trim() != "") {
				if (feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_FOLIO_NUMBER).toString().trim() != null
						&& !feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_FOLIO_NUMBER).toString().trim()
								.isEmpty()) {
					if (feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_SCHEME_RTA_CODE).toString().trim() != null
							&& !feedData.get(FinexaBOColumnConstant.AUM_KARVY_EXCEL_SCHEME_RTA_CODE).toString().trim()
									.isEmpty()) {
						aumMasterBOList.add(aumMasterBO);
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

		return aumMasterBOList;

	}

	private List<AumMasterBO> populateAumFranklinModelList(Map<String, String> feedData, AumMasterBODTO aumMasterBODTO,
			List<AumMasterBO> aumMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordValid = false;
			AumMasterBO aumMasterBO = mapper.map(aumMasterBODTO, AumMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();

			AumMasterBOPK aumMasterBOPK = new AumMasterBOPK();
			aumMasterBOPK.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_FOLIO_NUMBER)));
			aumMasterBOPK.setSchemertacode(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_SCHEME_RTA_CODE));
			aumMasterBOPK.setReportDate(finexaUtil.formatDate(
					finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_REPORT_DATE))));
			aumMasterBO.setUnitBalance(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_UNIT_BALANCE));
			aumMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_SUB_BROKER_CODE));
			aumMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_SCHEME_NAME));
			aumMasterBO.setPinCode(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_PINCODE));
			aumMasterBO.setNav(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_NAV));
			aumMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_INVESTOR_NAME));
			aumMasterBO.setDistributorarncode(
					feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_DISTRIBUTOR_ARN_CODE));
			aumMasterBO.setCurrentValue(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_CURRENT_VALUE));
			aumMasterBO.setAmcCode("Franklin Templeton Mutual Funds");

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			aumMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(aumMasterBODTO.getAdvisorId());
			aumMasterBO.setAdvisorUser(advUser);
			aumMasterBO.setId(aumMasterBOPK);
			// java.sql.Date reportDate = new
			// java.sql.Date(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_REPORT_DATE))).getTime());
			// String repdate = reportDate.toString().trim();
			// String folio =
			// feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_FOLIO_NUMBER).toString().trim();
			// String schemeRTACode =
			// feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_SCHEME_RTA_CODE).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_REPORT_DATE) != null
					&& !feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_REPORT_DATE).isEmpty()
					&& feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_REPORT_DATE) != "") {
				if (feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_FOLIO_NUMBER).toString().trim() != null
						&& !feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_FOLIO_NUMBER).toString().trim()
								.isEmpty()
						&& feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_FOLIO_NUMBER).toString()
								.trim() != "") {
					if (feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_SCHEME_RTA_CODE).toString()
							.trim() != null
							&& !feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_SCHEME_RTA_CODE).toString()
									.trim().isEmpty()
							&& feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_EXCEL_SCHEME_RTA_CODE).toString()
									.trim() != "") {
						aumMasterBOList.add(aumMasterBO);
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

		return aumMasterBOList;

	}

	private UploadResponseDTO readDBFFeed(AumMasterBODTO aumMasterBODTO, UploadResponseDTO uploadResponseDTO)
			throws IOException, InvalidFormatException, ParseException {

		InputStream initialStream = aumMasterBODTO.getNameSelectFile()[0].getInputStream();
		InputStream initialStream2 = aumMasterBODTO.getNameSelectFile()[0].getInputStream();
		dbfReader = new DBFReader(initialStream);
		boolean headerFound = false, firstRow;
		int numberOfFields;
		String headerPropFile, columnPropFile;
		Properties prop1 = new Properties();
		Object[] rowObjects1, rowObjects2;
		int batchCount = 0, counter = 0, headerRowCount = 0, row, numberOfRecords = 0, numberOfRecordRunning = 0;
		List<AumMasterBO> aumMasterBOList = new ArrayList<AumMasterBO>();
		List<String> fileColNames = new ArrayList<String>();
		List<String> propColNames = new ArrayList<String>();
		Map<String, String> dataMap = new HashMap<>();
		Map<String, String> aumFeedMap = new HashMap<>();

		// Number of columns in the DBF file
		numberOfFields = dbfReader.getFieldCount();
		numberOfRecords = dbfReader.getRecordCount();

		headerPropFile = "backOfficeProperties/aumDBF.properties";
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
			aumFeedMap.put(i.toString(), fileColname);
		}
		prop1 = new Properties();
		switch (rtaId) {

		case RTA_FILE_TYPE_CAMS:
			columnPropFile = "backOfficeProperties/aumCAMSDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case RTA_FILE_TYPE_KARVY:
			columnPropFile = "backOfficeProperties/aumKarvyDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case RTA_FILE_TYPE_FRANKLIN:
			columnPropFile = "backOfficeProperties/aumFranklinDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;
		case RTA_FILE_TYPE_SUNDARAM:
			columnPropFile = "backOfficeProperties/aumSundaramDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		default:
			break;

		}

		for (Map.Entry<String, String> entry : aumFeedMap.entrySet()) {
			fileColNames.add(entry.getValue());
		}

		for (String columnHeader : prop1.stringPropertyNames()) {
			propColNames.add(prop1.getProperty(columnHeader));
			System.out.println(prop1.getProperty(columnHeader));
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

		AdvisorUser advisorUser = advisorUserRepository.findOne(aumMasterBODTO.getAdvisorId());

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
				dataMap.put(aumFeedMap.get(fieldIndex.toString()), String.valueOf(rowObjects2[columnIndex]).trim());

			}

			if (batchCount == 10) {
				System.out.println("aumMasterBOList size() when batchCount is 20: " + aumMasterBOList.size());
				uploadResponseDTO = saveAumData(aumMasterBOList, uploadResponseDTO);
				aumMasterBOList = new ArrayList<AumMasterBO>();
				batchCount = 0;
				counter++;
				System.out.println("Counter displayed" + counter);
				System.out.println("*********************************");

				switch (rtaId) {

				case RTA_FILE_TYPE_CAMS:
					aumMasterBOList = populateDBFCAMSModelList(dataMap, aumMasterBODTO, aumMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_KARVY:
					aumMasterBOList = populateDBFKARVYModelList(dataMap, aumMasterBODTO, aumMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_FRANKLIN:
					aumMasterBOList = populateDBFFranklinModelList(dataMap, aumMasterBODTO, aumMasterBOList,
							advisorUser);
					break;
				case RTA_FILE_TYPE_SUNDARAM:
					aumMasterBOList = populateDBFSundaramModelList(dataMap, aumMasterBODTO, aumMasterBOList,
							advisorUser);
					break;
				default:
					break;

				}

				batchCount++;

			} else {

				switch (rtaId) {
				case RTA_FILE_TYPE_CAMS:
					aumMasterBOList = populateDBFCAMSModelList(dataMap, aumMasterBODTO, aumMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_KARVY:
					aumMasterBOList = populateDBFKARVYModelList(dataMap, aumMasterBODTO, aumMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_FRANKLIN:
					aumMasterBOList = populateDBFFranklinModelList(dataMap, aumMasterBODTO, aumMasterBOList,
							advisorUser);
					break;
				case RTA_FILE_TYPE_SUNDARAM:
					aumMasterBOList = populateDBFSundaramModelList(dataMap, aumMasterBODTO, aumMasterBOList,
							advisorUser);
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
			System.out.println("aumMasterBOList size() before last: " + aumMasterBOList.size());
			uploadResponseDTO = saveAumData(aumMasterBOList, uploadResponseDTO);
		}
		// System.out.println(dataMap);

		uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		return uploadResponseDTO;

	}

	private List<AumMasterBO> populateDBFCAMSModelList(Map<String, String> feedData, AumMasterBODTO aumMasterBODTO,
			List<AumMasterBO> aumMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordValid = false;
			AumMasterBO aumMasterBO = mapper.map(aumMasterBODTO, AumMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			AumMasterBOPK aumMasterBOPK = new AumMasterBOPK();
			aumMasterBOPK.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_FOLIO_NUMBER)));
			aumMasterBOPK.setSchemertacode(feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_SCHEME_RTA_CODE));
			aumMasterBOPK.setReportDate(finexaUtil.formatDate(
					finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_REPORT_DATE))));
			aumMasterBO.setUnitBalance(feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_UNIT_BALANCE));
			aumMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_SUB_BROKER_CODE));
			aumMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_SCHEME_NAME));
			// aumMasterBO.setPinCode(feedData.get("Pincode"));
			aumMasterBO.setNav(feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_NAV));
			aumMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_INVESTOR_NAME));
			aumMasterBO.setDistributorarncode(feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_DISTRIBUTOR_ARN_CODE));
			aumMasterBO.setCurrentValue(feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_CURRENT_VALUE));
			// aumMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");
			aumMasterBO.setLookupRtabo(lookupRtabo);
			aumMasterBO.setAdvisorUser(advisorUser);
			aumMasterBO.setId(aumMasterBOPK);

			// String repdate = reportDate.toString().trim();
			// String folio =
			// feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_FOLIO_NUMBER).toString().trim();
			// String schemeRTACode =
			// feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_SCHEME_RTA_CODE).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_REPORT_DATE).toString().trim() != null
					&& !feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_REPORT_DATE).toString().trim().isEmpty()) {
				if (feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_FOLIO_NUMBER).toString().trim() != null
						&& !feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_FOLIO_NUMBER).toString().trim()
								.isEmpty()) {
					if (feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_SCHEME_RTA_CODE).toString().trim() != null
							&& !feedData.get(FinexaBOColumnConstant.AUM_CAMS_DBF_SCHEME_RTA_CODE).toString().trim()
									.isEmpty()) {
						aumMasterBOList.add(aumMasterBO);
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

		return aumMasterBOList;

	}

	private List<AumMasterBO> populateDBFKARVYModelList(Map<String, String> feedData, AumMasterBODTO aumMasterBODTO,
			List<AumMasterBO> aumMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordValid = false;
			AumMasterBO aumMasterBO = mapper.map(aumMasterBODTO, AumMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			AumMasterBOPK aumMasterBOPK = new AumMasterBOPK();
			
			aumMasterBOPK.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_FOLIO_NUMBER)));
			aumMasterBOPK.setSchemertacode(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_SCHEME_RTA_CODE));
			aumMasterBOPK.setReportDate(
					finexaUtil.formatDate(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_REPORT_DATE)));
			aumMasterBO.setUnitBalance(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_UNIT_BALANCE));
			aumMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_SUB_BROKER_CODE));
			aumMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_SCHEME_NAME));
			aumMasterBO.setPinCode(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_PINCODE));
			aumMasterBO.setNav(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_NAV));
			aumMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_INVESTOR_NAME));
			aumMasterBO.setDistributorarncode(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_DISTRIBUTOR_ARN_CODE));
			aumMasterBO.setCurrentValue(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_CURRENT_VALUE));
			aumMasterBO.setAmcCode(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_AMC_CODE));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			aumMasterBO.setLookupRtabo(lookupRtabo);
			aumMasterBO.setAdvisorUser(advisorUser);
			aumMasterBO.setId(aumMasterBOPK);
			// java.sql.Date reportDate = new
			// java.sql.Date(finexaUtil.formatDate(feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_REPORT_DATE)).getTime());
			// String repdate = reportDate.toString().trim();
			// String folio =
			// feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_FOLIO_NUMBER).toString().trim();
			// String schemeRTACode =
			// feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_SCHEME_RTA_CODE).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_REPORT_DATE).toString().trim() != null
					&& !feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_REPORT_DATE).toString().trim().isEmpty()) {
				if (feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_FOLIO_NUMBER).toString().trim() != null
						&& !feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_FOLIO_NUMBER).toString().trim()
								.isEmpty()) {
					if (feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_SCHEME_RTA_CODE).toString().trim() != null
							&& !feedData.get(FinexaBOColumnConstant.AUM_KARVY_DBF_SCHEME_RTA_CODE).toString().trim()
									.isEmpty()) {
						aumMasterBOList.add(aumMasterBO);
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

		return aumMasterBOList;

	}

	private List<AumMasterBO> populateDBFFranklinModelList(Map<String, String> feedData, AumMasterBODTO aumMasterBODTO,
			List<AumMasterBO> aumMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordValid = false;
			AumMasterBO aumMasterBO = mapper.map(aumMasterBODTO, AumMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			AumMasterBOPK aumMasterBOPK = new AumMasterBOPK();
			aumMasterBOPK.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_FOLIO_NUMBER)));
			aumMasterBOPK.setSchemertacode(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_SCHEME_RTA_CODE));
			aumMasterBOPK
					.setReportDate(utilToSQLDate(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_REPORT_DATE)));
			aumMasterBO.setUnitBalance(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_UNIT_BALANCE));
			aumMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_SUB_BROKER_CODE));
			aumMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_SCHEME_NAME));
			aumMasterBO.setPinCode(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_PINCODE));
			aumMasterBO.setNav(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_NAV));
			aumMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_INVESTOR_NAME));
			aumMasterBO
					.setDistributorarncode(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_DISTRIBUTOR_ARN_CODE));
			aumMasterBO.setCurrentValue(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_CURRENT_VALUE));
			aumMasterBO.setAmcCode("Franklin Templeton Mutual Funds");

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			aumMasterBO.setLookupRtabo(lookupRtabo);
			aumMasterBO.setAdvisorUser(advisorUser);
			aumMasterBO.setId(aumMasterBOPK);
			// .sql.Date reportDate =
			// utilToSQLDate(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_REPORT_DATE));
			// String repdate = reportDate.toString().trim();
			// String folio =
			// feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_FOLIO_NUMBER).toString().trim();
			// String schemeRTACode =
			// feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_SCHEME_RTA_CODE).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_REPORT_DATE).toString().trim() != null
					&& !feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_REPORT_DATE).toString().trim().isEmpty()) {
				if (feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_FOLIO_NUMBER).toString().trim() != null
						&& !feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_FOLIO_NUMBER).toString().trim()
								.isEmpty()) {
					if (feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_SCHEME_RTA_CODE).toString().trim() != null
							&& !feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_SCHEME_RTA_CODE).toString().trim()
									.isEmpty()) {
						aumMasterBOList.add(aumMasterBO);
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

		return aumMasterBOList;

	}
	
	private List<AumMasterBO> populateDBFSundaramModelList(Map<String, String> feedData, AumMasterBODTO aumMasterBODTO,
			List<AumMasterBO> aumMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			boolean isRecordValid = false;
			AumMasterBO aumMasterBO = mapper.map(aumMasterBODTO, AumMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			AumMasterBOPK aumMasterBOPK = new AumMasterBOPK();
			aumMasterBOPK.setFolioNumber(formatNumbers(feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_FOLIO_NUMBER)));
			aumMasterBOPK.setSchemertacode(feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_SCHEME_RTA_CODE));
			System.out.println(feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_REPORT_DATE));
			aumMasterBOPK
					.setReportDate(finexaUtil.formatDate(finexaUtil.formatStringDate((feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_REPORT_DATE)))));
			aumMasterBO.setUnitBalance(feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_UNIT_BALANCE));
			//aumMasterBO.setSubBrokerCode(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_SUB_BROKER_CODE));
			aumMasterBO.setSchemeName(feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_SCHEME_NAME));
			//aumMasterBO.setPinCode(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_PINCODE));
			aumMasterBO.setNav(feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_NAV));
			aumMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_INVESTOR_NAME));
			aumMasterBO
					.setDistributorarncode(feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_DISTRIBUTOR_ARN_CODE));
			aumMasterBO.setCurrentValue(feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_CURRENT_VALUE));
			aumMasterBO.setAmcCode("Sundaram Mutual Funds");

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Sundaram Mutual Fund");
			aumMasterBO.setLookupRtabo(lookupRtabo);
			aumMasterBO.setAdvisorUser(advisorUser);
			aumMasterBO.setId(aumMasterBOPK);
			// .sql.Date reportDate =
			// utilToSQLDate(feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_REPORT_DATE));
			// String repdate = reportDate.toString().trim();
			// String folio =
			// feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_FOLIO_NUMBER).toString().trim();
			// String schemeRTACode =
			// feedData.get(FinexaBOColumnConstant.AUM_FRANKLIN_DBF_SCHEME_RTA_CODE).toString().trim();
			if (feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_REPORT_DATE).toString().trim() != null
					&& !feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_REPORT_DATE).toString().trim().isEmpty()) {
				if (feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_FOLIO_NUMBER).toString().trim() != null
						&& !feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_FOLIO_NUMBER).toString().trim()
								.isEmpty()) {
					if (feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_SCHEME_RTA_CODE).toString().trim() != null
							&& !feedData.get(FinexaBOColumnConstant.AUM_SUNDARAM_DBF_SCHEME_RTA_CODE).toString().trim()
									.isEmpty()) {
						aumMasterBOList.add(aumMasterBO);
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

		return aumMasterBOList;

	}

	private synchronized UploadResponseDTO saveAumData(List<AumMasterBO> aumMasterBODTOList,
			UploadResponseDTO uploadResponseDTO) {
		// TODO Auto-generated method stub
		System.out.println("Uploading AUM");
		try {
			if (aumMasterBODTOList != null && aumMasterBODTOList.size() > 0) {
				aumMasterBORepository.save(aumMasterBODTOList);
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
		String columnHeader = "schemertacode";
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