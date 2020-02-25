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

import org.apache.poi.hssf.util.CellReference;
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
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.AumMasterBODTO;
import com.finlabs.finexa.dto.RejectionMasterBODTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AumMasterBO;
import com.finlabs.finexa.model.AumMasterBOPK;
import com.finlabs.finexa.model.LookupRTABO;
import com.finlabs.finexa.model.LookupRTAMasterFileDetailsBO;
import com.finlabs.finexa.model.RejectionMasterBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.LookupRTABORepository;
import com.finlabs.finexa.repository.LookupRTAMasterFileDetailsBORepository;
import com.finlabs.finexa.repository.RejectionMasterBORepository;
import com.finlabs.finexa.util.FinexaBOColumnConstant;
import com.finlabs.finexa.util.FinexaUtil;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

@Component

@Scope("prototype")

@Service("RejectionMasterBOService")
public class RejectionMasterBOServiceImpl implements RejectionMasterBOService {

	private static Logger log = LoggerFactory.getLogger(RejectionMasterBOServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private RejectionMasterBORepository rejectionMasterBORepository;

	@Autowired
	private LookupRTABORepository lookupRTABORepository;

	@Autowired
	private LookupRTAMasterFileDetailsBORepository lookupRTAMasterFileDetailsBORepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	private static final String RTA_FILE_TYPE_CAMS = "1";
	private static final String RTA_FILE_TYPE_KARVY = "2";
	private static final String RTA_FILE_TYPE_FRANKLIN = "3";
	private static final String RTA_FILE_TYPE_SUNDARAM = "4";
	private Properties prop = new Properties();
	private String rtaId;
	private Integer rta;
	private String FEED_PROPERTIES_FILE;
	private int rows = 1, columnHeaderRow = 0,noOfRejectedRecords = 0;
	
	DBFReader dbfReader;

	/**
	 * Method where Rejection Master Feed File is extracted and the data is inserted
	 * to MySQL database
	 * 
	 * @throws ParseException
	 *
	 * @Author Smita Ghosh Chowdhury
	 */
	@Override
	public UploadResponseDTO uploadRejectionMaster(RejectionMasterBODTO rejectionMasterBODTO,
			UploadResponseDTO uploadResponseDTO)
			throws RuntimeException, IOException, InvalidFormatException, ParseException {

		try {

			log.debug("RejectionMasterBOServiceImpl >> uploadRejectionMaster");
			rta = rejectionMasterBODTO.getNameRTA();
			rtaId = rta.toString();
			
			String name = rejectionMasterBODTO.getNameSelectFile()[0].getOriginalFilename();
			if (name.contains(".dbf") || name.contains(".DBF")) {
				uploadResponseDTO = readDBFFeed(rejectionMasterBODTO, uploadResponseDTO);
			} else {
				uploadResponseDTO = readRejectionExcelFeed(rejectionMasterBODTO, uploadResponseDTO);
			}
			//readRejectionExcelFeed(rejectionMasterBODTO, uploadResponseDTO);

		} catch (RuntimeException e) {

			throw new RuntimeException(e);
		}
		
		uploadResponseDTO.setRejectedRecords(noOfRejectedRecords);
		noOfRejectedRecords = 0;
		return uploadResponseDTO;
	}

	/**
	 * Method to read from Excel File using Apache POI
	 *
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws ParseException
	 */
	private UploadResponseDTO readRejectionExcelFeed(RejectionMasterBODTO rejectionMasterBODTO,
			UploadResponseDTO uploadResponseDTO) throws IOException, InvalidFormatException, ParseException {

		// null pointer handling

		InputStream initialStream = rejectionMasterBODTO.getNameSelectFile()[0].getInputStream();

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
			FEED_PROPERTIES_FILE = "backOfficeProperties/rejectionCAMS.properties";
			break;
		case RTA_FILE_TYPE_KARVY:
			FEED_PROPERTIES_FILE = "backOfficeProperties/rejectionKarvy.properties";
			break;
		case RTA_FILE_TYPE_FRANKLIN:
			FEED_PROPERTIES_FILE = "backOfficeProperties/rejectionFranklin.properties";
			;
			break;
		/*
		 * case RTA_FILE_TYPE_SUNDARAM : FEED_PROPERTIES_FILE =
		 * "backOfficeProperties/rejectionSundaram.properties"; break;
		 */
		default:
			break;
		}

		ClassLoader classLoader = getClass().getClassLoader();
		FileReader fileReader = new FileReader(classLoader.getResource(FEED_PROPERTIES_FILE).getFile());
		prop.load(fileReader);
		LookupRTAMasterFileDetailsBO lookupRTAMasterFileDetailsBO = lookupRTAMasterFileDetailsBORepository
				.findByFileCode("REJM");

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
				//System.out.println(excelNameColNameMap);
				for (String columnAddress : excelNameColNameMap.values()) {
					//System.out.println("BM: "+columnAddress);
					if (columnAddress.equals("nu")) {
						//System.out.println(columnAddress);
						uploadResponseDTO.setPrimaryKeyNotFound(true);
						return uploadResponseDTO;
				}
				}	

		List<RejectionMasterBO> rejectionMasterBOList = new ArrayList<RejectionMasterBO>();
		int batchCount = 0;
		
		AdvisorUser advisorUser = advisorUserRepository.findOne(rejectionMasterBODTO.getAdvisorId());
		
		while (rowIterator.hasNext()) {

			// This row variable is used to iterate through each row of excel sheet
			Row row = rowIterator.next();

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
				uploadResponseDTO = saveRejectionData(rejectionMasterBOList, uploadResponseDTO);
				rejectionMasterBOList = new ArrayList<RejectionMasterBO>();
				batchCount = 0;

				switch (rtaId) {
				case RTA_FILE_TYPE_CAMS:
					rejectionMasterBOList = populateRejectionCAMSModelList(feed, rejectionMasterBODTO,
							rejectionMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_KARVY:
					rejectionMasterBOList = populateRejectionKarvyModelList(feed, rejectionMasterBODTO,
							rejectionMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_FRANKLIN:
					rejectionMasterBOList = populateRejectionFranklinModelList(feed, rejectionMasterBODTO,
							rejectionMasterBOList, advisorUser);
					break;
				/*
				 * case RTA_FILE_TYPE_SUNDARAM : rejectionMasterBOList =
				 * populateRejectionCAMSModelList(feed,
				 * rejectionMasterBODTO,rejectionMasterBOList, advisorUser); break;
				 */
				default:
					break;
				}

				batchCount++;
			} else {

				switch (rtaId) {
				case RTA_FILE_TYPE_CAMS:
					rejectionMasterBOList = populateRejectionCAMSModelList(feed, rejectionMasterBODTO,
							rejectionMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_KARVY:
					rejectionMasterBOList = populateRejectionKarvyModelList(feed, rejectionMasterBODTO,
							rejectionMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_FRANKLIN:
					rejectionMasterBOList = populateRejectionFranklinModelList(feed, rejectionMasterBODTO,
							rejectionMasterBOList, advisorUser);
					break;
				/*
				 * case RTA_FILE_TYPE_SUNDARAM : rejectionMasterBOList =
				 * populateRejectionCAMSModelList(feed,
				 * rejectionMasterBODTO,rejectionMasterBOList, advisorUser); break;
				 */
				default:
					break;
				}

				batchCount++;
			}

		}

		if (batchCount > 0) {
			uploadResponseDTO = saveRejectionData(rejectionMasterBOList, uploadResponseDTO);
			// rejectionMasterBOList.clear();
		}

		targetFile.delete();
		return uploadResponseDTO;

	}

	private List<RejectionMasterBO> populateRejectionCAMSModelList(Map<String, String> feedData,
			RejectionMasterBODTO rejectionMasterBODTO, List<RejectionMasterBO> rejectionMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub
		
		try {
			RejectionMasterBO rejectionMasterBO = mapper.map(rejectionMasterBODTO, RejectionMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			//rejectionMasterBO.setTransactedLocation(feedData.get("location"));
			//rejectionMasterBO.setApplicationNumber(feedData.get("application_no"));
			rejectionMasterBO.setFolioNumber(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_FOLIO_NUMBER));
			/*
			rejectionMasterBO.setAddress1(feedData.get("invest_ad1"));
			rejectionMasterBO.setAddress2(feedData.get("invest_ad2"));
			rejectionMasterBO.setCity(feedData.get("investor_city"));
			rejectionMasterBO.setPincode(feedData.get("investor_pincode"));
			rejectionMasterBO.setPhoneOffice(feedData.get("phone_off"));
			rejectionMasterBO.setPhoneResidence(feedData.get("phone_res"));
			rejectionMasterBO.setEmail(feedData.get("investor_email"));
			rejectionMasterBO.setDistributorCode(feedData.get("broker_name"));
			*/
			rejectionMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_SCHEME_RTA_CODE));
			rejectionMasterBO.setAmount(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_AMOUNT));
			rejectionMasterBO.setRemarks(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_REMARKS));
			//rejectionMasterBO.setLocationCode(feedData.get("location_code"));
			//rejectionMasterBO.setUserCode(feedData.get("user_code"));
			if(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_TRANSACTION_NUMBER).contains("/")) {
				rejectionMasterBO.setTransactionNumber(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_TRANSACTION_NUMBER));
			} else {

				BigDecimal bigDecimaldata = new BigDecimal(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_TRANSACTION_NUMBER));
				
				Long longData = bigDecimaldata.longValueExact();
				
				String stringDataOfCell = longData.toString();
				rejectionMasterBO.setTransactionNumber(stringDataOfCell);
			}
			//rejectionMasterBO.setTransactionNumber(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_TRANSACTION_NUMBER));
			rejectionMasterBO.setSchemeCode(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_SCHEME_CODE));
			rejectionMasterBO.setTransactionType(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_TRANSACTION_TYPE));
			//rejectionMasterBO.setPostedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("posted_date"))));
			//rejectionMasterBO.setTradeDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("trade_date_time"))));
			rejectionMasterBO
					.setTransactionDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_TRANSACTION_DATE))));
			rejectionMasterBO.setInstrumentNumber(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_INSTRUMENT_NUMBER));
			//rejectionMasterBO.setRejectAt(feedData.get("reject_at"));
			//rejectionMasterBO.setEuin(feedData.get("euin"));
			rejectionMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_INVESTOR_NAME));
			//rejectionMasterBO.setAddress3(feedData.get("invest_ad3"));
			rejectionMasterBO.setPan(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_PAN));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");
			rejectionMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(rejectionMasterBODTO.getAdvisorId());
			rejectionMasterBO.setAdvisorUser(advUser);
			String transNo = feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_CAMS_TRANSACTION_NUMBER).trim();
			if (transNo != null && !transNo.isEmpty()) {
				rejectionMasterBOList.add(rejectionMasterBO);
				isRecordValid = true;

			}
			if (isRecordValid == false)
				noOfRejectedRecords++;

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return rejectionMasterBOList;

	}

	private List<RejectionMasterBO> populateRejectionKarvyModelList(Map<String, String> feedData,
			RejectionMasterBODTO rejectionMasterBODTO, List<RejectionMasterBO> rejectionMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			RejectionMasterBO rejectionMasterBO = mapper.map(rejectionMasterBODTO, RejectionMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();

			boolean isRecordValid = false;

			rejectionMasterBO.setFolioNumber(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_FOLIO_NUMBER));
			rejectionMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_SCHEME_RTA_CODE));
			rejectionMasterBO.setAmount(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_AMOUNT));
			rejectionMasterBO.setRemarks(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_REMARKS));
			if(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_TRANSACTION_NUMBER).contains("/")) {
				rejectionMasterBO.setTransactionNumber(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_TRANSACTION_NUMBER));
			} else {

				BigDecimal bigDecimaldata = new BigDecimal(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_TRANSACTION_NUMBER));
				
				Long longData = bigDecimaldata.longValueExact();
				
				String stringDataOfCell = longData.toString();
				rejectionMasterBO.setTransactionNumber(stringDataOfCell);
			}
			//rejectionMasterBO.setTransactionNumber(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_TRANSACTION_NUMBER));
			rejectionMasterBO.setSchemeCode(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_SCHEME_CODE));
			rejectionMasterBO.setTransactionType(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_TRANSACTION_TYPE));
			rejectionMasterBO
					.setPostedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_POSTED_DATE))));
			rejectionMasterBO
					.setTradeDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_TRANSACTION_DATE))));
			rejectionMasterBO.setInstrumentNumber(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_INSTRUMENT_NUMBER));
			rejectionMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_INVESTOR_NAME));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			rejectionMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(rejectionMasterBODTO.getAdvisorId());
			rejectionMasterBO.setAdvisorUser(advUser);
			String transNo = feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_KARVY_TRANSACTION_NUMBER).trim();
			if (transNo != null && !transNo.isEmpty()) {
				rejectionMasterBOList.add(rejectionMasterBO);
				isRecordValid = true;

			}
			if (isRecordValid == false)
				noOfRejectedRecords++;

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return rejectionMasterBOList;

	}

	private List<RejectionMasterBO> populateRejectionFranklinModelList(Map<String, String> feedData,
			RejectionMasterBODTO rejectionMasterBODTO, List<RejectionMasterBO> rejectionMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			RejectionMasterBO rejectionMasterBO = mapper.map(rejectionMasterBODTO, RejectionMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			rejectionMasterBO.setApplicationNumber(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_APPLICATION_NUMBER));
			rejectionMasterBO.setFolioNumber(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_FOLIO_NUMBER));
			rejectionMasterBO.setAddress1(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_ADDRESS1));
			rejectionMasterBO.setAddress2(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_ADDRESS2));
			rejectionMasterBO.setCity(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_CITY));
			rejectionMasterBO.setPincode(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_PIN));
			rejectionMasterBO.setPhoneResidence(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_MOBILE));
			rejectionMasterBO.setEmail(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_EMAIL));
			rejectionMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_SCHEME_RTA_CODE));
			rejectionMasterBO.setAmount(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_AMOUNT));
			rejectionMasterBO.setRemarks(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_REMARKS));
			rejectionMasterBO.setUserCode(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_BRANCH));
			
			rejectionMasterBO.setTransactionNumber(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_TRANSACTION_NUMBER));
			rejectionMasterBO.setSchemeCode(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_SCHEME_CODE));
			rejectionMasterBO.setTransactionType(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_TRANSACTION_TYPE));
			rejectionMasterBO.setTransactionDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_TRANSACTION_DATE))));
			rejectionMasterBO.setInstrumentNumber(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_INSTRUMENT_NUMBER));
			rejectionMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_INVESTOR_NAME));
			rejectionMasterBO.setAddress3(feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_ADDRESS3));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			rejectionMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(rejectionMasterBODTO.getAdvisorId());
			rejectionMasterBO.setAdvisorUser(advUser);
			String transNo = feedData.get(FinexaBOColumnConstant.REJECTION_EXCEL_FRANKLIN_TRANSACTION_NUMBER).trim();
			if (transNo != null && !transNo.isEmpty()) {
				rejectionMasterBOList.add(rejectionMasterBO);
				isRecordValid = true;

			}
			if (isRecordValid == false)
				noOfRejectedRecords++;

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return rejectionMasterBOList;

	}
	
	
	private UploadResponseDTO readDBFFeed(RejectionMasterBODTO rejectionMasterBODTO, UploadResponseDTO uploadResponseDTO)
			throws IOException, InvalidFormatException, ParseException {

		InputStream initialStream = rejectionMasterBODTO.getNameSelectFile()[0].getInputStream();
		InputStream initialStream2 = rejectionMasterBODTO.getNameSelectFile()[0].getInputStream();
		dbfReader = new DBFReader(initialStream);
		boolean headerFound = false, firstRow;
		int numberOfFields;
		String headerPropFile, key, columnPropFile;
		Properties prop1 = new Properties();
		Object[] rowObjects1, rowObjects2;
		int batchCount = 0, counter = 0, headerRowCount = 0, row;
		List<RejectionMasterBO> rejectionMasterBOList = new ArrayList<RejectionMasterBO>();
		List<String> fileColNames = new ArrayList<String>();
		List<String> propColNames = new ArrayList<String>();
		Map<String, String> dataMap = new HashMap<>();
		Map<String, String> rejectionMasterFeedMap = new HashMap<>();
		
		// Number of columns in the DBF file
		numberOfFields = dbfReader.getFieldCount();

		headerPropFile = "backOfficeProperties/rejectionMasterDBF.properties";
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
			rejectionMasterFeedMap.put(i.toString(), fileColname);
		}
		prop1 = new Properties();
		switch (rtaId) {

		case "1":
			columnPropFile = "backOfficeProperties/rejectionCAMSDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case "2":
			columnPropFile = "backOfficeProperties/rejectionKarvyDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		case "3":
			columnPropFile = "backOfficeProperties/rejectionFranklinDBF.properties";
			classLoader1 = getClass().getClassLoader();
			fileReader1 = new FileReader(classLoader1.getResource(columnPropFile).getFile());
			prop1.load(fileReader1);
			break;

		default:
			break;

		}

		for (Map.Entry<String, String> entry : rejectionMasterFeedMap.entrySet()) {
			fileColNames.add(entry.getValue());
		}

		for (String columnHeader : prop1.stringPropertyNames()) {
			propColNames.add(prop1.getProperty(columnHeader));
		}
		
		fileColNames.retainAll(propColNames);

		if (fileColNames.size() != propColNames.size()) {

			uploadResponseDTO.setPrimaryKeyNotFound(true);
			return uploadResponseDTO;

		}

		AdvisorUser advisorUser = advisorUserRepository.findOne(rejectionMasterBODTO.getAdvisorId());

		dbfReader = new DBFReader(initialStream2);
		// Starts reading after the header row
		row = 0;
		rowObjects2 = dbfReader.nextRecord();
		while (dbfReader.nextRecord() != null) {
			if (++row <= headerRowCount)
				continue;
			
			for (int columnIndex = 0; columnIndex < numberOfFields; columnIndex++) {
				Integer fieldIndex = columnIndex;
				dataMap.put(rejectionMasterFeedMap.get(fieldIndex.toString()), String.valueOf(rowObjects2[columnIndex]).trim());
				
			}

			if (batchCount == 20) {
				System.out.println("rejectionMasterBOList size() when batchCount is 20: " + rejectionMasterBOList.size());
				uploadResponseDTO = saveRejectionData(rejectionMasterBOList, uploadResponseDTO);
				rejectionMasterBOList = new ArrayList<RejectionMasterBO>();
				batchCount = 0;
				counter++;
				System.out.println("Counter displayed" + counter);
				System.out.println("*********************************");

				switch (rtaId) {

				case RTA_FILE_TYPE_CAMS:
					rejectionMasterBOList = populateDBFCAMSModelList(dataMap, rejectionMasterBODTO, rejectionMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_KARVY:
					rejectionMasterBOList = populateDBFKARVYModelList(dataMap, rejectionMasterBODTO, rejectionMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_FRANKLIN:
					rejectionMasterBOList = populateDBFFranklinModelList(dataMap, rejectionMasterBODTO, rejectionMasterBOList,
							advisorUser);
					break;
				default:
					break;

				}

				batchCount++;

			} else {

				switch (rtaId) {
				case RTA_FILE_TYPE_CAMS:
					rejectionMasterBOList = populateDBFCAMSModelList(dataMap, rejectionMasterBODTO, rejectionMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_KARVY:
					rejectionMasterBOList = populateDBFKARVYModelList(dataMap, rejectionMasterBODTO, rejectionMasterBOList, advisorUser);
					break;

				case RTA_FILE_TYPE_FRANKLIN:
					rejectionMasterBOList = populateDBFFranklinModelList(dataMap, rejectionMasterBODTO, rejectionMasterBOList,
							advisorUser);
					break;
				default:
					break;

				}

				batchCount++;
				rowObjects2 = dbfReader.nextRecord();
			}

		}
		if (batchCount > 0) {
			System.out.println("rejectionMasterBOList size() before last: " + rejectionMasterBOList.size());
			uploadResponseDTO = saveRejectionData(rejectionMasterBOList, uploadResponseDTO);
		}
		// System.out.println(dataMap);

		return uploadResponseDTO;

	}
	
	private List<RejectionMasterBO> populateDBFCAMSModelList(Map<String, String> feedData, RejectionMasterBODTO rejectionMasterBODTO,
			List<RejectionMasterBO> rejectionMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			RejectionMasterBO rejectionMasterBO = mapper.map(rejectionMasterBODTO, RejectionMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;
			//rejectionMasterBO.setTransactedLocation(feedData.get("location"));
			//rejectionMasterBO.setApplicationNumber(feedData.get("application_no"));
			rejectionMasterBO.setFolioNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_FOLIO_NUMBER));
			/*
			rejectionMasterBO.setAddress1(feedData.get("invest_ad1"));
			rejectionMasterBO.setAddress2(feedData.get("invest_ad2"));
			rejectionMasterBO.setCity(feedData.get("investor_city"));
			rejectionMasterBO.setPincode(feedData.get("investor_pincode"));
			rejectionMasterBO.setPhoneOffice(feedData.get("phone_off"));
			rejectionMasterBO.setPhoneResidence(feedData.get("phone_res"));
			rejectionMasterBO.setEmail(feedData.get("investor_email"));
			rejectionMasterBO.setDistributorCode(feedData.get("broker_name"));
			*/
			rejectionMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_SCHEME_RTA_CODE));
			rejectionMasterBO.setAmount(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_AMOUNT));
			rejectionMasterBO.setRemarks(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_REMARKS));
			//rejectionMasterBO.setLocationCode(feedData.get("location_code"));
			//rejectionMasterBO.setUserCode(feedData.get("user_code"));
			if(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_TRANSACTION_NUMBER).contains("/")) {
				rejectionMasterBO.setTransactionNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_TRANSACTION_NUMBER));
			} else {

				BigDecimal bigDecimaldata = new BigDecimal(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_TRANSACTION_NUMBER));
				
				Long longData = bigDecimaldata.longValueExact();
				
				String stringDataOfCell = longData.toString();
				rejectionMasterBO.setTransactionNumber(stringDataOfCell);
			}
			//rejectionMasterBO.setTransactionNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_TRANSACTION_NUMBER));
			rejectionMasterBO.setSchemeCode(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_SCHEME_CODE));
			rejectionMasterBO.setTransactionType(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_TRANSACTION_TYPE));
			//rejectionMasterBO.setPostedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("posted_date"))));
			//rejectionMasterBO.setTradeDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("trade_date_time"))));
			rejectionMasterBO
					.setTransactionDate(utilToSQLDate(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_TRANSACTION_DATE)));
			rejectionMasterBO.setInstrumentNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_INSTRUMENT_NUMBER));
			//rejectionMasterBO.setRejectAt(feedData.get("reject_at"));
			//rejectionMasterBO.setEuin(feedData.get("euin"));
			rejectionMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_INVESTOR_NAME));
			//rejectionMasterBO.setAddress3(feedData.get("invest_ad3"));
			//rejectionMasterBO.setPan(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_PAN));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("CAMS");
			rejectionMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(rejectionMasterBODTO.getAdvisorId());
			rejectionMasterBO.setAdvisorUser(advUser);
			String transNo = feedData.get(FinexaBOColumnConstant.REJECTION_DBF_CAMS_TRANSACTION_NUMBER).trim();
			if (transNo != null && !transNo.isEmpty()) {
				rejectionMasterBOList.add(rejectionMasterBO);
				isRecordValid = true;

			}
			if (isRecordValid == false)
				noOfRejectedRecords++;

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return rejectionMasterBOList;

	}
	
	
	
	private List<RejectionMasterBO> populateDBFKARVYModelList(Map<String, String> feedData, RejectionMasterBODTO rejectionMasterBODTO,
			List<RejectionMasterBO> rejectionMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			RejectionMasterBO rejectionMasterBO = mapper.map(rejectionMasterBODTO, RejectionMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();

			boolean isRecordValid = false;

			//rejectionMasterBO.setFolioNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_FOLIO_NUMBER));
			if(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_FOLIO_NUMBER).contains("/")) {
				rejectionMasterBO.setFolioNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_FOLIO_NUMBER));
			} else {

				BigDecimal bigDecimaldata = new BigDecimal(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_FOLIO_NUMBER));
				
				Long longData = bigDecimaldata.longValueExact();
				
				String stringDataOfCell = longData.toString();
				rejectionMasterBO.setFolioNumber(stringDataOfCell);
			}
			rejectionMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_SCHEME_RTA_CODE));
			rejectionMasterBO.setAmount(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_AMOUNT));
			rejectionMasterBO.setRemarks(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_REMARKS));
			if(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_TRANSACTION_NUMBER).contains("/")) {
				rejectionMasterBO.setTransactionNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_TRANSACTION_NUMBER));
			} else {

				BigDecimal bigDecimaldata = new BigDecimal(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_TRANSACTION_NUMBER));
				
				Long longData = bigDecimaldata.longValueExact();
				
				String stringDataOfCell = longData.toString();
				rejectionMasterBO.setTransactionNumber(stringDataOfCell);
			}
			//rejectionMasterBO.setTransactionNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_TRANSACTION_NUMBER));
			rejectionMasterBO.setSchemeCode(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_SCHEME_CODE));
			rejectionMasterBO.setTransactionType(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_TRANSACTION_TYPE));
			//rejectionMasterBO.setPostedDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_POSTED_DATE))));
			rejectionMasterBO
					.setTradeDate(utilToSQLDate(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_TRANSACTION_DATE)));
			rejectionMasterBO.setInstrumentNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_INSTRUMENT_NUMBER));
			rejectionMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_INVESTOR_NAME));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			rejectionMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(rejectionMasterBODTO.getAdvisorId());
			rejectionMasterBO.setAdvisorUser(advUser);
			String transNo = feedData.get(FinexaBOColumnConstant.REJECTION_DBF_KARVY_TRANSACTION_NUMBER).trim();
			if (transNo != null && !transNo.isEmpty()) {
				rejectionMasterBOList.add(rejectionMasterBO);
				isRecordValid = true;

			}
			if (isRecordValid == false)
				noOfRejectedRecords++;

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return rejectionMasterBOList;

	}
	
	
	
	private List<RejectionMasterBO> populateDBFFranklinModelList(Map<String, String> feedData, RejectionMasterBODTO rejectionMasterBODTO,
			List<RejectionMasterBO> rejectionMasterBOList, AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			RejectionMasterBO rejectionMasterBO = mapper.map(rejectionMasterBODTO, RejectionMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			boolean isRecordValid = false;

			//rejectionMasterBO.setApplicationNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_APPLICATION_NUMBER));
			/*
			if(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_FOLIO_NUMBER).contains("/")) {
				rejectionMasterBO.setFolioNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_FOLIO_NUMBER));
			} else {

				BigDecimal bigDecimaldata = new BigDecimal(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_FOLIO_NUMBER));
				
				Long longData = bigDecimaldata.longValueExact();
				
				String stringDataOfCell = longData.toString();
				rejectionMasterBO.setFolioNumber(stringDataOfCell);
			}
			*/
			rejectionMasterBO.setFolioNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_FOLIO_NUMBER));
			//rejectionMasterBO.setAddress1(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_ADDRESS1));
			//rejectionMasterBO.setAddress2(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_ADDRESS2));
			//rejectionMasterBO.setCity(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_CITY));
			//rejectionMasterBO.setPincode(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_PIN));
			//rejectionMasterBO.setPhoneResidence(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_MOBILE));
			//rejectionMasterBO.setEmail(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_EMAIL));
			rejectionMasterBO.setSchemeRTACode(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_SCHEME_RTA_CODE));
			rejectionMasterBO.setAmount(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_AMOUNT));
			rejectionMasterBO.setRemarks(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_REMARKS));
			//rejectionMasterBO.setUserCode(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_BRANCH));
			
			rejectionMasterBO.setTransactionNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_TRANSACTION_NUMBER));
			rejectionMasterBO.setSchemeCode(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_SCHEME_CODE));
			rejectionMasterBO.setTransactionType(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_TRANSACTION_TYPE));
			//System.out.println("DATE:  "+feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_TRANSACTION_DATE));
			
			rejectionMasterBO.setTransactionDate(utilToSQLDate(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_TRANSACTION_DATE)));
			rejectionMasterBO.setInstrumentNumber(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_INSTRUMENT_NUMBER));
			rejectionMasterBO.setInvestorName(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_INVESTOR_NAME));
			//rejectionMasterBO.setAddress3(feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_ADDRESS3));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			rejectionMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(rejectionMasterBODTO.getAdvisorId());
			rejectionMasterBO.setAdvisorUser(advUser);
			String transNo = feedData.get(FinexaBOColumnConstant.REJECTION_DBF_FRANKLIN_TRANSACTION_NUMBER).trim();
			if (transNo != null && !transNo.isEmpty()) {
				rejectionMasterBOList.add(rejectionMasterBO);
				isRecordValid = true;

			}
			if (isRecordValid == false)
				noOfRejectedRecords++;

		} catch (RuntimeException e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}

		return rejectionMasterBOList;

	}

	private synchronized UploadResponseDTO saveRejectionData(List<RejectionMasterBO> rejectionMasterBODTOList,
			UploadResponseDTO uploadResponseDTO) {
		// TODO Auto-generated method stub

		try {
			if (rejectionMasterBODTOList != null && rejectionMasterBODTOList.size() > 0) {
				rejectionMasterBORepository.save(rejectionMasterBODTOList);

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
		if (!(date == null) && !date.trim().isEmpty() && !date.trim().contains("null")) {
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

}
