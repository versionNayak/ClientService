package com.finlabs.finexa.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.AumMasterBODTO;
import com.finlabs.finexa.dto.BrokerageMasterBODTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.BrokerageMasterBO;
import com.finlabs.finexa.model.BrokerageMasterBOPK;
import com.finlabs.finexa.model.InvestorMasterBO;
import com.finlabs.finexa.model.LookupRTABO;
import com.finlabs.finexa.model.LookupRTAMasterFileDetailsBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.BrokerageMasterBORepository;
import com.finlabs.finexa.repository.LookupRTABORepository;
import com.finlabs.finexa.repository.LookupRTAMasterFileDetailsBORepository;
import com.finlabs.finexa.repository.LookupTransactionRuleRepository;
import com.finlabs.finexa.util.FinexaUtil;
import com.ibm.icu.text.DecimalFormat;

@Service("BrokerageMasterBOService")
public class BrokerageMasterBOServiceImpl implements BrokerageMasterBOService {

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

	private static final String RTA_FILE_TYPE_CAMS = "1";
	private static final String RTA_FILE_TYPE_KARVY = "2";
	private static final String RTA_FILE_TYPE_FRANKLIN = "3";
	private static final String RTA_FILE_TYPE_SUNDARAM = "4";
	private DecimalFormat decimalFormat = new DecimalFormat("#");
	private Properties prop = new Properties();
	private String rtaId;
	private String FEED_PROPERTIES_FILE;
	private int rows = 1, columnHeaderRow = 0;
	private int camsFeedNumber;
	// private List<Object[]> transNumTransDateProcessDateList = new
	// ArrayList<Object[]>();
	// private Map<String, BrokerageMasterBO> transNumTransDateProcessDateMap = new
	// HashMap<String, BrokerageMasterBO> ();

	@Override
	public UploadResponseDTO uploadBrokerageMaster(BrokerageMasterBODTO brokerageMasterBODTO,
			UploadResponseDTO uploadResponseDTO)
			throws RuntimeException, IOException, InvalidFormatException, ParseException {
		// TODO Auto-generated method stub

		try {

			//rtaId = brokerageMasterBODTO.getNameRTA();
			readBrokerageExcelFeed(brokerageMasterBODTO, uploadResponseDTO);

		} catch (RuntimeException e) {

			throw new RuntimeException(e);
		}

		return uploadResponseDTO;
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
			if (brokerageMasterBODTO.getNameSelectFile()[0].getOriginalFilename().contains("22")) {
				FEED_PROPERTIES_FILE = "backOfficeProperties/brokerageCAMS6.properties";
				camsFeedNumber = 6;
				break;
			} else if (brokerageMasterBODTO.getNameSelectFile()[0].getOriginalFilename().contains("53")) {
				FEED_PROPERTIES_FILE = "backOfficeProperties/brokerageCAMS51.properties";
				camsFeedNumber = 51;
				break;
			} else if (brokerageMasterBODTO.getNameSelectFile()[0].getOriginalFilename().contains("53")) {
				FEED_PROPERTIES_FILE = "backOfficeProperties/brokerageCAMS70.properties";
				camsFeedNumber = 70;
				break;
			} else if (brokerageMasterBODTO.getNameSelectFile()[0].getOriginalFilename().contains("53")) {
				FEED_PROPERTIES_FILE = "backOfficeProperties/brokerageCAMS71.properties";
				camsFeedNumber = 71;
				break;
			} else if (brokerageMasterBODTO.getNameSelectFile()[0].getOriginalFilename().contains("53")) {
				FEED_PROPERTIES_FILE = "backOfficeProperties/brokerageCAMS77.properties";
				camsFeedNumber = 77;
				break;
			}
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
		LookupRTAMasterFileDetailsBO lookupRTAMasterFileDetailsBO = lookupRTAMasterFileDetailsBORepository
				.findByFileCode("BROKM");

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

		// try-catch
		AdvisorUser advisorUser = advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());

		/*
		 * transNumTransDateProcessDateList = brokerageMasterBORepository
		 * .getdistinctTransNumberTransDateAndProcessDateSet(brokerageMasterBODTO.
		 * getAdvisorId());
		 */
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
				uploadResponseDTO = saveBrokerageData(brokerageMasterBOList, uploadResponseDTO);
				brokerageMasterBOList = new ArrayList<BrokerageMasterBO>();
				batchCount = 0;

				switch (rtaId) {
				/*
				 * case RTA_FILE_TYPE_CAMS: if (camsFeedNumber == 6) { brokerageMasterBOList =
				 * populateBrokerageCAMS6ModelList(feed, brokerageMasterBODTO,
				 * brokerageMasterBOList, advisorUser); break; } else if (camsFeedNumber == 51)
				 * { brokerageMasterBOList = populateBrokerageCAMS51ModelList(feed,
				 * brokerageMasterBODTO, brokerageMasterBOList, advisorUser); break; } else if
				 * (camsFeedNumber == 70) { brokerageMasterBOList =
				 * populateBrokerageCAMS70ModelList(feed, brokerageMasterBODTO,
				 * brokerageMasterBOList, advisorUser); break; } else if (camsFeedNumber == 71)
				 * { brokerageMasterBOList = populateBrokerageCAMS71ModelList(feed,
				 * brokerageMasterBODTO, brokerageMasterBOList, advisorUser); break; } else if
				 * (camsFeedNumber == 77) { brokerageMasterBOList =
				 * populateBrokerageCAMS77ModelList(feed, brokerageMasterBODTO,
				 * brokerageMasterBOList, advisorUser); break; } break;
				 */
				case RTA_FILE_TYPE_KARVY:
					brokerageMasterBOList = populateBrokerageKarvyModelList(feed, brokerageMasterBODTO,
							brokerageMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_FRANKLIN:
					brokerageMasterBOList = populateBrokerageFranklinModelList(feed, brokerageMasterBODTO,
							brokerageMasterBOList, advisorUser);
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
				/*
				 * case RTA_FILE_TYPE_CAMS: if (camsFeedNumber == 6) { brokerageMasterBOList =
				 * populateBrokerageCAMS6ModelList(feed, brokerageMasterBODTO,
				 * brokerageMasterBOList, advisorUser); break; } else if (camsFeedNumber == 51)
				 * { brokerageMasterBOList = populateBrokerageCAMS51ModelList(feed,
				 * brokerageMasterBODTO, brokerageMasterBOList, advisorUser); break; } else if
				 * (camsFeedNumber == 70) { brokerageMasterBOList =
				 * populateBrokerageCAMS70ModelList(feed, brokerageMasterBODTO,
				 * brokerageMasterBOList, advisorUser); break; } else if (camsFeedNumber == 71)
				 * { brokerageMasterBOList = populateBrokerageCAMS71ModelList(feed,
				 * brokerageMasterBODTO, brokerageMasterBOList, advisorUser); break; } else if
				 * (camsFeedNumber == 77) { brokerageMasterBOList =
				 * populateBrokerageCAMS77ModelList(feed, brokerageMasterBODTO,
				 * brokerageMasterBOList, advisorUser); break; } break;
				 */
				case RTA_FILE_TYPE_KARVY:
					brokerageMasterBOList = populateBrokerageKarvyModelList(feed, brokerageMasterBODTO,
							brokerageMasterBOList, advisorUser);
					break;
				case RTA_FILE_TYPE_FRANKLIN:
					brokerageMasterBOList = populateBrokerageFranklinModelList(feed, brokerageMasterBODTO,
							brokerageMasterBOList, advisorUser);
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
			uploadResponseDTO = saveBrokerageData(brokerageMasterBOList, uploadResponseDTO);
		}

		targetFile.delete();
		return uploadResponseDTO;

	}

	/*
	 * private List<BrokerageMasterBO> populateBrokerageCAMS6ModelList(Map<String,
	 * String> feedData, BrokerageMasterBODTO brokerageMasterBODTO,
	 * List<BrokerageMasterBO> brokerageMasterBOList, AdvisorUser advisorUser)
	 * throws ParseException { // TODO Auto-generated method stub
	 * 
	 * try { BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO,
	 * BrokerageMasterBO.class); FinexaUtil finexaUtil = new FinexaUtil();
	 * 
	 * boolean clientIsPresent = false; int id = 0; for (Object[]
	 * transNumTransDateProcessDate : transNumTransDateProcessDateList) if
	 * (transNumTransDateProcessDate[1].equals(feedData.get("Folio Number")) &&
	 * transNumTransDateProcessDate[2].equals(feedData.get("Product Code")) &&
	 * transNumTransDateProcessDate[3].equals(feedData.get("Report Date"))) {
	 * clientIsPresent = true; id = (int) transNumTransDateProcessDate[0]; break; }
	 * if (clientIsPresent == true) {
	 * 
	 * brokerageMasterBO.setId(id);
	 * brokerageMasterBO.setSchemeRTACode(feedData.get("Product Code"));
	 * brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
	 * brokerageMasterBO.setAmcCode(feedData.get("Fund"));
	 * brokerageMasterBO.setFolioNumber(feedData.get("Account Number"));
	 * brokerageMasterBO.setInvestorName(feedData.get("Investor Name"));
	 * brokerageMasterBO.setTransactionType(feedData.get("Transaction Description"))
	 * ;
	 * brokerageMasterBO.setFromDate(finexaUtil.formatDate(feedData.get("From Date")
	 * ));
	 * brokerageMasterBO.setToDate(finexaUtil.formatDate(feedData.get("To Date")));
	 * brokerageMasterBO.setTransactionDate(finexaUtil.formatDate(feedData.
	 * get("Transaction Date")));
	 * brokerageMasterBO.setProcessDate(finexaUtil.formatDate(feedData.
	 * get("Process Date")));
	 * brokerageMasterBO.setAmount(feedData.get("Amount (in Rs.)"));
	 * brokerageMasterBO.setBrokeragePercentage(feedData.get("Percentage (%)"));
	 * brokerageMasterBO.setBrokerageAmount(feedData.get("Brokerage (in Rs.)"));
	 * brokerageMasterBO.setBrokerageType(feedData.get("Brokerage Head"));
	 * brokerageMasterBO.setTransactionNumber(feedData.get("Transaction Number"));
	 * 
	 * LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
	 * brokerageMasterBO.setLookupRtabo(lookupRtabo); AdvisorUser advUser =
	 * advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
	 * brokerageMasterBO.setAdvisorUser(advUser);
	 * 
	 * if (feedData.get("Transaction Number") != null &&
	 * !feedData.get("Transaction Number").isEmpty()) { if
	 * (feedData.get("Transaction Date") != null &&
	 * !feedData.get("Transaction Date").isEmpty()) { if
	 * (feedData.get("Process Date") != null &&
	 * !feedData.get("Process Date").isEmpty()) {
	 * brokerageMasterBOList.add(brokerageMasterBO); } } } } else {
	 * 
	 * brokerageMasterBO.setSchemeRTACode(feedData.get("Product Code"));
	 * brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
	 * brokerageMasterBO.setAmcCode(feedData.get("Fund"));
	 * brokerageMasterBO.setFolioNumber(feedData.get("Account Number"));
	 * brokerageMasterBO.setInvestorName(feedData.get("Investor Name"));
	 * brokerageMasterBO.setTransactionType(feedData.get("Transaction Description"))
	 * ;
	 * brokerageMasterBO.setFromDate(finexaUtil.formatDate(feedData.get("From Date")
	 * ));
	 * brokerageMasterBO.setToDate(finexaUtil.formatDate(feedData.get("To Date")));
	 * brokerageMasterBO.setTransactionDate(finexaUtil.formatDate(feedData.
	 * get("Transaction Date")));
	 * brokerageMasterBO.setProcessDate(finexaUtil.formatDate(feedData.
	 * get("Process Date")));
	 * brokerageMasterBO.setAmount(feedData.get("Amount (in Rs.)"));
	 * brokerageMasterBO.setBrokeragePercentage(feedData.get("Percentage (%)"));
	 * brokerageMasterBO.setBrokerageAmount(feedData.get("Brokerage (in Rs.)"));
	 * brokerageMasterBO.setBrokerageType(feedData.get("Brokerage Head"));
	 * brokerageMasterBO.setTransactionNumber(feedData.get("Transaction Number"));
	 * 
	 * LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
	 * brokerageMasterBO.setLookupRtabo(lookupRtabo); AdvisorUser advUser =
	 * advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
	 * brokerageMasterBO.setAdvisorUser(advUser);
	 * 
	 * if (feedData.get("Transaction Number") != null &&
	 * !feedData.get("Transaction Number").isEmpty()) { if
	 * (feedData.get("Transaction Date") != null &&
	 * !feedData.get("Transaction Date").isEmpty()) { if
	 * (feedData.get("Process Date") != null &&
	 * !feedData.get("Process Date").isEmpty()) {
	 * brokerageMasterBOList.add(brokerageMasterBO); } } }
	 * 
	 * }
	 * 
	 * } catch (RuntimeException e) { e.printStackTrace(); // throw new
	 * RuntimeException(e); }
	 * 
	 * return brokerageMasterBOList;
	 * 
	 * }
	 * 
	 * private List<BrokerageMasterBO> populateBrokerageCAMS51ModelList(Map<String,
	 * String> feedData, BrokerageMasterBODTO brokerageMasterBODTO,
	 * List<BrokerageMasterBO> brokerageMasterBOList, AdvisorUser advisorUser)
	 * throws ParseException { // TODO Auto-generated method stub
	 * 
	 * try { BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO,
	 * BrokerageMasterBO.class); FinexaUtil finexaUtil = new FinexaUtil();
	 * 
	 * boolean clientIsPresent = false; int id = 0; for (Object[]
	 * transNumTransDateProcessDate : transNumTransDateProcessDateList) if
	 * (transNumTransDateProcessDate[1].equals(feedData.get("Folio Number")) &&
	 * transNumTransDateProcessDate[2].equals(feedData.get("Product Code")) &&
	 * transNumTransDateProcessDate[3].equals(feedData.get("Report Date"))) {
	 * clientIsPresent = true; id = (int) transNumTransDateProcessDate[0]; break; }
	 * if (clientIsPresent == true) {
	 * 
	 * brokerageMasterBO.setId(id);
	 * brokerageMasterBO.setSchemeRTACode(feedData.get("Product Code"));
	 * brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
	 * brokerageMasterBO.setAmcCode(feedData.get("Fund"));
	 * brokerageMasterBO.setFolioNumber(feedData.get("Account Number"));
	 * brokerageMasterBO.setInvestorName(feedData.get("Investor Name"));
	 * brokerageMasterBO.setTransactionType(feedData.get("Transaction Description"))
	 * ;
	 * brokerageMasterBO.setFromDate(finexaUtil.formatDate(feedData.get("From Date")
	 * ));
	 * brokerageMasterBO.setToDate(finexaUtil.formatDate(feedData.get("To Date")));
	 * brokerageMasterBO.setTransactionDate(finexaUtil.formatDate(feedData.
	 * get("Transaction Date")));
	 * brokerageMasterBO.setProcessDate(finexaUtil.formatDate(feedData.
	 * get("Process Date")));
	 * brokerageMasterBO.setAmount(feedData.get("Amount (in Rs.)"));
	 * brokerageMasterBO.setBrokeragePercentage(feedData.get("Percentage (%)"));
	 * brokerageMasterBO.setBrokerageAmount(feedData.get("Brokerage (in Rs.)"));
	 * brokerageMasterBO.setBrokerageType(feedData.get("Brokerage Head"));
	 * brokerageMasterBO.setTransactionNumber(feedData.get("Transaction Number"));
	 * 
	 * LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
	 * brokerageMasterBO.setLookupRtabo(lookupRtabo); AdvisorUser advUser =
	 * advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
	 * brokerageMasterBO.setAdvisorUser(advUser);
	 * 
	 * if (feedData.get("Transaction Number") != null &&
	 * !feedData.get("Transaction Number").isEmpty()) { if
	 * (feedData.get("Transaction Date") != null &&
	 * !feedData.get("Transaction Date").isEmpty()) { if
	 * (feedData.get("Process Date") != null &&
	 * !feedData.get("Process Date").isEmpty()) {
	 * brokerageMasterBOList.add(brokerageMasterBO); } } } } else {
	 * 
	 * brokerageMasterBO.setSchemeRTACode(feedData.get("Product Code"));
	 * brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
	 * brokerageMasterBO.setAmcCode(feedData.get("Fund"));
	 * brokerageMasterBO.setFolioNumber(feedData.get("Account Number"));
	 * brokerageMasterBO.setInvestorName(feedData.get("Investor Name"));
	 * brokerageMasterBO.setTransactionType(feedData.get("Transaction Description"))
	 * ;
	 * brokerageMasterBO.setFromDate(finexaUtil.formatDate(feedData.get("From Date")
	 * ));
	 * brokerageMasterBO.setToDate(finexaUtil.formatDate(feedData.get("To Date")));
	 * brokerageMasterBO.setTransactionDate(finexaUtil.formatDate(feedData.
	 * get("Transaction Date")));
	 * brokerageMasterBO.setProcessDate(finexaUtil.formatDate(feedData.
	 * get("Process Date")));
	 * brokerageMasterBO.setAmount(feedData.get("Amount (in Rs.)"));
	 * brokerageMasterBO.setBrokeragePercentage(feedData.get("Percentage (%)"));
	 * brokerageMasterBO.setBrokerageAmount(feedData.get("Brokerage (in Rs.)"));
	 * brokerageMasterBO.setBrokerageType(feedData.get("Brokerage Head"));
	 * brokerageMasterBO.setTransactionNumber(feedData.get("Transaction Number"));
	 * 
	 * LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
	 * brokerageMasterBO.setLookupRtabo(lookupRtabo); AdvisorUser advUser =
	 * advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
	 * brokerageMasterBO.setAdvisorUser(advUser);
	 * 
	 * if (feedData.get("Transaction Number") != null &&
	 * !feedData.get("Transaction Number").isEmpty()) { if
	 * (feedData.get("Transaction Date") != null &&
	 * !feedData.get("Transaction Date").isEmpty()) { if
	 * (feedData.get("Process Date") != null &&
	 * !feedData.get("Process Date").isEmpty()) {
	 * brokerageMasterBOList.add(brokerageMasterBO); } } }
	 * 
	 * }
	 * 
	 * } catch (RuntimeException e) { e.printStackTrace(); // throw new
	 * RuntimeException(e); }
	 * 
	 * return brokerageMasterBOList;
	 * 
	 * }
	 * 
	 * private List<BrokerageMasterBO> populateBrokerageCAMS70ModelList(Map<String,
	 * String> feedData, BrokerageMasterBODTO brokerageMasterBODTO,
	 * List<BrokerageMasterBO> brokerageMasterBOList, AdvisorUser advisorUser)
	 * throws ParseException { // TODO Auto-generated method stub
	 * 
	 * try { BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO,
	 * BrokerageMasterBO.class); FinexaUtil finexaUtil = new FinexaUtil();
	 * 
	 * boolean clientIsPresent = false; int id = 0; for (Object[]
	 * transNumTransDateProcessDate : transNumTransDateProcessDateList) if
	 * (transNumTransDateProcessDate[1].equals(feedData.get("Folio Number")) &&
	 * transNumTransDateProcessDate[2].equals(feedData.get("Product Code")) &&
	 * transNumTransDateProcessDate[3].equals(feedData.get("Report Date"))) {
	 * clientIsPresent = true; id = (int) transNumTransDateProcessDate[0]; break; }
	 * if (clientIsPresent == true) {
	 * 
	 * brokerageMasterBO.setId(id);
	 * brokerageMasterBO.setSchemeRTACode(feedData.get("Product Code"));
	 * brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
	 * brokerageMasterBO.setAmcCode(feedData.get("Fund"));
	 * brokerageMasterBO.setFolioNumber(feedData.get("Account Number"));
	 * brokerageMasterBO.setInvestorName(feedData.get("Investor Name"));
	 * brokerageMasterBO.setTransactionType(feedData.get("Transaction Description"))
	 * ;
	 * brokerageMasterBO.setFromDate(finexaUtil.formatDate(feedData.get("From Date")
	 * ));
	 * brokerageMasterBO.setToDate(finexaUtil.formatDate(feedData.get("To Date")));
	 * brokerageMasterBO.setTransactionDate(finexaUtil.formatDate(feedData.
	 * get("Transaction Date")));
	 * brokerageMasterBO.setProcessDate(finexaUtil.formatDate(feedData.
	 * get("Process Date")));
	 * brokerageMasterBO.setAmount(feedData.get("Amount (in Rs.)"));
	 * brokerageMasterBO.setBrokeragePercentage(feedData.get("Percentage (%)"));
	 * brokerageMasterBO.setBrokerageAmount(feedData.get("Brokerage (in Rs.)"));
	 * brokerageMasterBO.setBrokerageType(feedData.get("Brokerage Head"));
	 * brokerageMasterBO.setTransactionNumber(feedData.get("Transaction Number"));
	 * 
	 * LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
	 * brokerageMasterBO.setLookupRtabo(lookupRtabo); AdvisorUser advUser =
	 * advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
	 * brokerageMasterBO.setAdvisorUser(advUser);
	 * 
	 * if (feedData.get("Transaction Number") != null &&
	 * !feedData.get("Transaction Number").isEmpty()) { if
	 * (feedData.get("Transaction Date") != null &&
	 * !feedData.get("Transaction Date").isEmpty()) { if
	 * (feedData.get("Process Date") != null &&
	 * !feedData.get("Process Date").isEmpty()) {
	 * brokerageMasterBOList.add(brokerageMasterBO); } } } } else {
	 * 
	 * brokerageMasterBO.setSchemeRTACode(feedData.get("Product Code"));
	 * brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
	 * brokerageMasterBO.setAmcCode(feedData.get("Fund"));
	 * brokerageMasterBO.setFolioNumber(feedData.get("Account Number"));
	 * brokerageMasterBO.setInvestorName(feedData.get("Investor Name"));
	 * brokerageMasterBO.setTransactionType(feedData.get("Transaction Description"))
	 * ;
	 * brokerageMasterBO.setFromDate(finexaUtil.formatDate(feedData.get("From Date")
	 * ));
	 * brokerageMasterBO.setToDate(finexaUtil.formatDate(feedData.get("To Date")));
	 * brokerageMasterBO.setTransactionDate(finexaUtil.formatDate(feedData.
	 * get("Transaction Date")));
	 * brokerageMasterBO.setProcessDate(finexaUtil.formatDate(feedData.
	 * get("Process Date")));
	 * brokerageMasterBO.setAmount(feedData.get("Amount (in Rs.)"));
	 * brokerageMasterBO.setBrokeragePercentage(feedData.get("Percentage (%)"));
	 * brokerageMasterBO.setBrokerageAmount(feedData.get("Brokerage (in Rs.)"));
	 * brokerageMasterBO.setBrokerageType(feedData.get("Brokerage Head"));
	 * brokerageMasterBO.setTransactionNumber(feedData.get("Transaction Number"));
	 * 
	 * LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
	 * brokerageMasterBO.setLookupRtabo(lookupRtabo); AdvisorUser advUser =
	 * advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
	 * brokerageMasterBO.setAdvisorUser(advUser);
	 * 
	 * if (feedData.get("Transaction Number") != null &&
	 * !feedData.get("Transaction Number").isEmpty()) { if
	 * (feedData.get("Transaction Date") != null &&
	 * !feedData.get("Transaction Date").isEmpty()) { if
	 * (feedData.get("Process Date") != null &&
	 * !feedData.get("Process Date").isEmpty()) {
	 * brokerageMasterBOList.add(brokerageMasterBO); } } }
	 * 
	 * }
	 * 
	 * } catch (RuntimeException e) { e.printStackTrace(); // throw new
	 * RuntimeException(e); }
	 * 
	 * return brokerageMasterBOList;
	 * 
	 * }
	 * 
	 * private List<BrokerageMasterBO> populateBrokerageCAMS71ModelList(Map<String,
	 * String> feedData, BrokerageMasterBODTO brokerageMasterBODTO,
	 * List<BrokerageMasterBO> brokerageMasterBOList, AdvisorUser advisorUser)
	 * throws ParseException { // TODO Auto-generated method stub
	 * 
	 * try { BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO,
	 * BrokerageMasterBO.class); FinexaUtil finexaUtil = new FinexaUtil();
	 * 
	 * boolean clientIsPresent = false; int id = 0; for (Object[]
	 * transNumTransDateProcessDate : transNumTransDateProcessDateList) if
	 * (transNumTransDateProcessDate[1].equals(feedData.get("Folio Number")) &&
	 * transNumTransDateProcessDate[2].equals(feedData.get("Product Code")) &&
	 * transNumTransDateProcessDate[3].equals(feedData.get("Report Date"))) {
	 * clientIsPresent = true; id = (int) transNumTransDateProcessDate[0]; break; }
	 * if (clientIsPresent == true) {
	 * 
	 * brokerageMasterBO.setId(id);
	 * brokerageMasterBO.setSchemeRTACode(feedData.get("Product Code"));
	 * brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
	 * brokerageMasterBO.setAmcCode(feedData.get("Fund"));
	 * brokerageMasterBO.setFolioNumber(feedData.get("Account Number"));
	 * brokerageMasterBO.setInvestorName(feedData.get("Investor Name"));
	 * brokerageMasterBO.setTransactionType(feedData.get("Transaction Description"))
	 * ;
	 * brokerageMasterBO.setFromDate(finexaUtil.formatDate(feedData.get("From Date")
	 * ));
	 * brokerageMasterBO.setToDate(finexaUtil.formatDate(feedData.get("To Date")));
	 * brokerageMasterBO.setTransactionDate(finexaUtil.formatDate(feedData.
	 * get("Transaction Date")));
	 * brokerageMasterBO.setProcessDate(finexaUtil.formatDate(feedData.
	 * get("Process Date")));
	 * brokerageMasterBO.setAmount(feedData.get("Amount (in Rs.)"));
	 * brokerageMasterBO.setBrokeragePercentage(feedData.get("Percentage (%)"));
	 * brokerageMasterBO.setBrokerageAmount(feedData.get("Brokerage (in Rs.)"));
	 * brokerageMasterBO.setBrokerageType(feedData.get("Brokerage Head"));
	 * brokerageMasterBO.setTransactionNumber(feedData.get("Transaction Number"));
	 * 
	 * LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
	 * brokerageMasterBO.setLookupRtabo(lookupRtabo); AdvisorUser advUser =
	 * advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
	 * brokerageMasterBO.setAdvisorUser(advUser);
	 * 
	 * if (feedData.get("Transaction Number") != null &&
	 * !feedData.get("Transaction Number").isEmpty()) { if
	 * (feedData.get("Transaction Date") != null &&
	 * !feedData.get("Transaction Date").isEmpty()) { if
	 * (feedData.get("Process Date") != null &&
	 * !feedData.get("Process Date").isEmpty()) {
	 * brokerageMasterBOList.add(brokerageMasterBO); } } } } else {
	 * 
	 * brokerageMasterBO.setSchemeRTACode(feedData.get("Product Code"));
	 * brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
	 * brokerageMasterBO.setAmcCode(feedData.get("Fund"));
	 * brokerageMasterBO.setFolioNumber(feedData.get("Account Number"));
	 * brokerageMasterBO.setInvestorName(feedData.get("Investor Name"));
	 * brokerageMasterBO.setTransactionType(feedData.get("Transaction Description"))
	 * ;
	 * brokerageMasterBO.setFromDate(finexaUtil.formatDate(feedData.get("From Date")
	 * ));
	 * brokerageMasterBO.setToDate(finexaUtil.formatDate(feedData.get("To Date")));
	 * brokerageMasterBO.setTransactionDate(finexaUtil.formatDate(feedData.
	 * get("Transaction Date")));
	 * brokerageMasterBO.setProcessDate(finexaUtil.formatDate(feedData.
	 * get("Process Date")));
	 * brokerageMasterBO.setAmount(feedData.get("Amount (in Rs.)"));
	 * brokerageMasterBO.setBrokeragePercentage(feedData.get("Percentage (%)"));
	 * brokerageMasterBO.setBrokerageAmount(feedData.get("Brokerage (in Rs.)"));
	 * brokerageMasterBO.setBrokerageType(feedData.get("Brokerage Head"));
	 * brokerageMasterBO.setTransactionNumber(feedData.get("Transaction Number"));
	 * 
	 * LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
	 * brokerageMasterBO.setLookupRtabo(lookupRtabo); AdvisorUser advUser =
	 * advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
	 * brokerageMasterBO.setAdvisorUser(advUser);
	 * 
	 * if (feedData.get("Transaction Number") != null &&
	 * !feedData.get("Transaction Number").isEmpty()) { if
	 * (feedData.get("Transaction Date") != null &&
	 * !feedData.get("Transaction Date").isEmpty()) { if
	 * (feedData.get("Process Date") != null &&
	 * !feedData.get("Process Date").isEmpty()) {
	 * brokerageMasterBOList.add(brokerageMasterBO); } } }
	 * 
	 * }
	 * 
	 * } catch (RuntimeException e) { e.printStackTrace(); // throw new
	 * RuntimeException(e); }
	 * 
	 * return brokerageMasterBOList;
	 * 
	 * }
	 * 
	 * private List<BrokerageMasterBO> populateBrokerageCAMS77ModelList(Map<String,
	 * String> feedData, BrokerageMasterBODTO brokerageMasterBODTO,
	 * List<BrokerageMasterBO> brokerageMasterBOList, AdvisorUser advisorUser)
	 * throws ParseException { // TODO Auto-generated method stub
	 * 
	 * try { BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO,
	 * BrokerageMasterBO.class); FinexaUtil finexaUtil = new FinexaUtil();
	 * 
	 * boolean clientIsPresent = false; int id = 0; for (Object[]
	 * transNumTransDateProcessDate : transNumTransDateProcessDateList) if
	 * (transNumTransDateProcessDate[1].equals(feedData.get("Folio Number")) &&
	 * transNumTransDateProcessDate[2].equals(feedData.get("Product Code")) &&
	 * transNumTransDateProcessDate[3].equals(feedData.get("Report Date"))) {
	 * clientIsPresent = true; id = (int) transNumTransDateProcessDate[0]; break; }
	 * if (clientIsPresent == true) {
	 * 
	 * brokerageMasterBO.setId(id);
	 * brokerageMasterBO.setSchemeRTACode(feedData.get("Product Code"));
	 * brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
	 * brokerageMasterBO.setAmcCode(feedData.get("Fund"));
	 * brokerageMasterBO.setFolioNumber(feedData.get("Account Number"));
	 * brokerageMasterBO.setInvestorName(feedData.get("Investor Name"));
	 * brokerageMasterBO.setTransactionType(feedData.get("Transaction Description"))
	 * ;
	 * brokerageMasterBO.setFromDate(finexaUtil.formatDate(feedData.get("From Date")
	 * ));
	 * brokerageMasterBO.setToDate(finexaUtil.formatDate(feedData.get("To Date")));
	 * brokerageMasterBO.setTransactionDate(finexaUtil.formatDate(feedData.
	 * get("Transaction Date")));
	 * brokerageMasterBO.setProcessDate(finexaUtil.formatDate(feedData.
	 * get("Process Date")));
	 * brokerageMasterBO.setAmount(feedData.get("Amount (in Rs.)"));
	 * brokerageMasterBO.setBrokeragePercentage(feedData.get("Percentage (%)"));
	 * brokerageMasterBO.setBrokerageAmount(feedData.get("Brokerage (in Rs.)"));
	 * brokerageMasterBO.setBrokerageType(feedData.get("Brokerage Head"));
	 * brokerageMasterBO.setTransactionNumber(feedData.get("Transaction Number"));
	 * 
	 * LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
	 * brokerageMasterBO.setLookupRtabo(lookupRtabo); AdvisorUser advUser =
	 * advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
	 * brokerageMasterBO.setAdvisorUser(advUser);
	 * 
	 * if (feedData.get("Transaction Number") != null &&
	 * !feedData.get("Transaction Number").isEmpty()) { if
	 * (feedData.get("Transaction Date") != null &&
	 * !feedData.get("Transaction Date").isEmpty()) { if
	 * (feedData.get("Process Date") != null &&
	 * !feedData.get("Process Date").isEmpty()) {
	 * brokerageMasterBOList.add(brokerageMasterBO); } } } } else {
	 * 
	 * brokerageMasterBO.setSchemeRTACode(feedData.get("Product Code"));
	 * brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
	 * brokerageMasterBO.setAmcCode(feedData.get("Fund"));
	 * brokerageMasterBO.setFolioNumber(feedData.get("Account Number"));
	 * brokerageMasterBO.setInvestorName(feedData.get("Investor Name"));
	 * brokerageMasterBO.setTransactionType(feedData.get("Transaction Description"))
	 * ;
	 * brokerageMasterBO.setFromDate(finexaUtil.formatDate(feedData.get("From Date")
	 * ));
	 * brokerageMasterBO.setToDate(finexaUtil.formatDate(feedData.get("To Date")));
	 * brokerageMasterBO.setTransactionDate(finexaUtil.formatDate(feedData.
	 * get("Transaction Date")));
	 * brokerageMasterBO.setProcessDate(finexaUtil.formatDate(feedData.
	 * get("Process Date")));
	 * brokerageMasterBO.setAmount(feedData.get("Amount (in Rs.)"));
	 * brokerageMasterBO.setBrokeragePercentage(feedData.get("Percentage (%)"));
	 * brokerageMasterBO.setBrokerageAmount(feedData.get("Brokerage (in Rs.)"));
	 * brokerageMasterBO.setBrokerageType(feedData.get("Brokerage Head"));
	 * brokerageMasterBO.setTransactionNumber(feedData.get("Transaction Number"));
	 * 
	 * LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
	 * brokerageMasterBO.setLookupRtabo(lookupRtabo); AdvisorUser advUser =
	 * advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
	 * brokerageMasterBO.setAdvisorUser(advUser);
	 * 
	 * if (feedData.get("Transaction Number") != null &&
	 * !feedData.get("Transaction Number").isEmpty()) { if
	 * (feedData.get("Transaction Date") != null &&
	 * !feedData.get("Transaction Date").isEmpty()) { if
	 * (feedData.get("Process Date") != null &&
	 * !feedData.get("Process Date").isEmpty()) {
	 * brokerageMasterBOList.add(brokerageMasterBO); } } }
	 * 
	 * }
	 * 
	 * } catch (RuntimeException e) { e.printStackTrace(); // throw new
	 * RuntimeException(e); }
	 * 
	 * return brokerageMasterBOList;
	 * 
	 * }
	 */
	private List<BrokerageMasterBO> populateBrokerageKarvyModelList(Map<String, String> feedData,
			BrokerageMasterBODTO brokerageMasterBODTO, List<BrokerageMasterBO> brokerageMasterBOList,
			AdvisorUser advisorUser) throws ParseException {
		// TODO Auto-generated method stub

		try {
			BrokerageMasterBO brokerageMasterBO = mapper.map(brokerageMasterBODTO, BrokerageMasterBO.class);
			FinexaUtil finexaUtil = new FinexaUtil();
			/*
			 * boolean clientIsPresent = false; int id = 0; for (Object[]
			 * transNumTransDateProcessDate : transNumTransDateProcessDateList) if
			 * (transNumTransDateProcessDate[1].equals(feedData.get("Transaction Number"))
			 * && transNumTransDateProcessDate[2].equals(feedData.get("Transaction Date"))
			 * && transNumTransDateProcessDate[3].equals(feedData.get("Process Date"))) {
			 * clientIsPresent = true; id = (int) transNumTransDateProcessDate[0]; break; }
			 * if (clientIsPresent == true) {
			 * 
			 * brokerageMasterBO.setId(id);
			 */
			BrokerageMasterBOPK brokerageMasterBOPK = new BrokerageMasterBOPK();
			brokerageMasterBO.setSchemeRTACode(feedData.get("Product Code"));
			brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
			brokerageMasterBO.setAmcCode(feedData.get("Fund"));
			brokerageMasterBO.setFolioNumber(feedData.get("Account Number"));
			brokerageMasterBO.setInvestorName(feedData.get("Investor Name"));
			brokerageMasterBO.setTransactionType(feedData.get("Transaction Description"));
			brokerageMasterBO
					.setFromDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("From Date"))));
			brokerageMasterBO.setToDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("To Date"))));
			brokerageMasterBOPK.setTransactionDate(
					finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("Transaction Date"))));
			brokerageMasterBOPK
					.setProcessDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("Process Date"))));
			brokerageMasterBO.setAmount(feedData.get("Amount (in Rs.)"));
			brokerageMasterBO.setBrokeragePercentage(feedData.get("Percentage (%)"));
			brokerageMasterBO.setBrokerageAmount(feedData.get("Brokerage (in Rs.)"));
			brokerageMasterBO.setBrokerageType(feedData.get("Brokerage Head"));
			brokerageMasterBOPK.setTransactionNumber(feedData.get("Transaction Number"));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			brokerageMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
			brokerageMasterBO.setAdvisorUser(advUser);
			brokerageMasterBO.setId(brokerageMasterBOPK);
			java.sql.Date transDate = new java.sql.Date(
					finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("Transaction Date"))).getTime());
			java.sql.Date procDate = new java.sql.Date(
					finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("Process Date"))).getTime());
			String transctionDate = transDate.toString().trim();
			String processDate = procDate.toString().trim();
			String trnxNo = feedData.get("Transaction Number").trim();
			if (trnxNo != null && !trnxNo.isEmpty()) {
				if (transctionDate != null && !transctionDate.isEmpty()) {
					if (processDate != null && !processDate.isEmpty()) {
						brokerageMasterBOList.add(brokerageMasterBO);
					}
				}
			}
			/*
			 * } else {
			 * 
			 * brokerageMasterBO.setSchemeRTACode(feedData.get("Product Code"));
			 * brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
			 * brokerageMasterBO.setAmcCode(feedData.get("Fund"));
			 * brokerageMasterBO.setFolioNumber(feedData.get("Account Number"));
			 * brokerageMasterBO.setInvestorName(feedData.get("Investor Name"));
			 * brokerageMasterBO.setTransactionType(feedData.get("Transaction Description"))
			 * ; brokerageMasterBO
			 * .setFromDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.
			 * get("From Date")))); brokerageMasterBO
			 * .setToDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.
			 * get("To Date")))); brokerageMasterBO.setTransactionDate(
			 * finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.
			 * get("Transaction Date")))); brokerageMasterBO.setProcessDate(
			 * finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("Process Date"
			 * )))); brokerageMasterBO.setAmount(feedData.get("Amount (in Rs.)"));
			 * brokerageMasterBO.setBrokeragePercentage(feedData.get("Percentage (%)"));
			 * brokerageMasterBO.setBrokerageAmount(feedData.get("Brokerage (in Rs.)"));
			 * brokerageMasterBO.setBrokerageType(feedData.get("Brokerage Head"));
			 * brokerageMasterBO.setTransactionNumber(feedData.get("Transaction Number"));
			 * 
			 * LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Karvy");
			 * brokerageMasterBO.setLookupRtabo(lookupRtabo); AdvisorUser advUser =
			 * advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
			 * brokerageMasterBO.setAdvisorUser(advUser);
			 * 
			 * if (feedData.get("Transaction Number") != null &&
			 * !feedData.get("Transaction Number").isEmpty()) { if
			 * (feedData.get("Transaction Date") != null &&
			 * !feedData.get("Transaction Date").isEmpty()) { if
			 * (feedData.get("Process Date") != null &&
			 * !feedData.get("Process Date").isEmpty()) {
			 * brokerageMasterBOList.add(brokerageMasterBO); Date transDate = new
			 * java.sql.Date(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.
			 * get("Transaction Date"))).getTime()); Date procDate = new
			 * java.sql.Date(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.
			 * get("Process Date"))).getTime());
			 * transNumTransDateProcessDateMap.put(feedData.get("Transaction Number")+
			 * transDate+procDate, brokerageMasterBO); brokerageMasterBOList = new
			 * ArrayList<BrokerageMasterBO>(); for (Map.Entry<String,BrokerageMasterBO>
			 * entry : transNumTransDateProcessDateMap.entrySet())
			 * brokerageMasterBOList.add(entry.getValue()); } } }
			 * 
			 * }
			 */
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
			/*
			 * boolean clientIsPresent = false; int id = 0; for (Object[]
			 * transNumTransDateProcessDate : transNumTransDateProcessDateList) {
			 * 
			 * if (transNumTransDateProcessDate[1].equals(feedData.get("TXNNO")) &&
			 * transNumTransDateProcessDate[2] .equals(new
			 * java.sql.Date(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(
			 * "TXNDATE"))).getTime())) && transNumTransDateProcessDate[3] .equals(new
			 * java.sql.Date(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(
			 * "PRDATE"))).getTime()))) { clientIsPresent = true;
			 * 
			 * id = (int) transNumTransDateProcessDate[0]; break; } } if (clientIsPresent ==
			 * true) { brokerageMasterBO.setId(id);
			 */
			BrokerageMasterBOPK brokerageMasterBOPK = new BrokerageMasterBOPK();

			brokerageMasterBO.setSchemeRTACode(feedData.get("PRODCODE"));
			// brokerageMasterBO.setSchemeName(feedData.get("Fund Description"));
			// brokerageMasterBO.setAmcCode(feedData.get("Fund"));
			brokerageMasterBO.setFolioNumber(feedData.get("ACCOUNTNO"));
			brokerageMasterBO.setInvestorName(feedData.get("INVNAME"));
			brokerageMasterBO.setTransactionType(feedData.get("TXNTYPE"));
			brokerageMasterBO.setFromDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("FRDATE"))));
			brokerageMasterBO.setToDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("TODATE"))));
			brokerageMasterBOPK
					.setTransactionDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("TXNDATE"))));
			brokerageMasterBOPK
					.setProcessDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("PRDATE"))));
			brokerageMasterBO.setAmount(feedData.get("TXNAMOUNT"));
			brokerageMasterBO.setBrokeragePercentage(feedData.get("PER"));
			brokerageMasterBO.setBrokerageAmount(feedData.get("BROKERAGE"));
			brokerageMasterBO.setBrokerageType(feedData.get("REMARKS"));
			brokerageMasterBOPK.setTransactionNumber(feedData.get("TXNNO"));

			LookupRTABO lookupRtabo = lookupRTABORepository.findByName("Franklin Templeton Investments");
			brokerageMasterBO.setLookupRtabo(lookupRtabo);
			AdvisorUser advUser = advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
			brokerageMasterBO.setAdvisorUser(advUser);
			brokerageMasterBO.setId(brokerageMasterBOPK);
			java.sql.Date transDate = new java.sql.Date(
					finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("TXNDATE"))).getTime());
			java.sql.Date procDate = new java.sql.Date(
					finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("PRDATE"))).getTime());
			String transctionDate = transDate.toString().trim();
			String processDate = procDate.toString().trim();
			String trnxNo = feedData.get("TXNNO").trim();
			if (trnxNo != null && !trnxNo.isEmpty()) {
				if (transctionDate != null && !transctionDate.isEmpty()) {
					if (processDate != null && !processDate.isEmpty()) {
						brokerageMasterBOList.add(brokerageMasterBO);
					}
				}
			}

			/*
			 * } else {
			 * 
			 * brokerageMasterBO.setSchemeRTACode(feedData.get("PRODCODE")); //
			 * brokerageMasterBO.setSchemeName(feedData.get("Fund Description")); //
			 * brokerageMasterBO.setAmcCode(feedData.get("Fund"));
			 * brokerageMasterBO.setFolioNumber(feedData.get("ACCOUNTNO"));
			 * brokerageMasterBO.setInvestorName(feedData.get("INVNAME"));
			 * brokerageMasterBO.setTransactionType(feedData.get("TXNTYPE"));
			 * brokerageMasterBO
			 * .setFromDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(
			 * "FRDATE"))));
			 * brokerageMasterBO.setToDate(finexaUtil.formatDate(finexaUtil.formatStringDate
			 * (feedData.get("TODATE")))); brokerageMasterBO.setTransactionDate(
			 * finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get("TXNDATE"))));
			 * brokerageMasterBO
			 * .setProcessDate(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.
			 * get("PRDATE")))); brokerageMasterBO.setAmount(feedData.get("TXNAMOUNT"));
			 * brokerageMasterBO.setBrokeragePercentage(feedData.get("PER"));
			 * brokerageMasterBO.setBrokerageAmount(feedData.get("BROKERAGE"));
			 * brokerageMasterBO.setBrokerageType(feedData.get("REMARKS"));
			 * brokerageMasterBO.setTransactionNumber(feedData.get("TXNNO"));
			 * 
			 * LookupRTABO lookupRtabo =
			 * lookupRTABORepository.findByName("Franklin Templeton Investments");
			 * brokerageMasterBO.setLookupRtabo(lookupRtabo); AdvisorUser advUser =
			 * advisorUserRepository.findOne(brokerageMasterBODTO.getAdvisorId());
			 * brokerageMasterBO.setAdvisorUser(advUser);
			 * 
			 * if (feedData.get("TXNNO") != null && !feedData.get("TXNNO").isEmpty()) { if
			 * (feedData.get("TXNDATE") != null && !feedData.get("TXNDATE").isEmpty()) { if
			 * (feedData.get("PRDATE") != null && !feedData.get("PRDATE").isEmpty()) {
			 * brokerageMasterBOList.add(brokerageMasterBO); Date transDate = new
			 * java.sql.Date(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(
			 * "TXNDATE"))).getTime()); Date procDate = new
			 * java.sql.Date(finexaUtil.formatDate(finexaUtil.formatStringDate(feedData.get(
			 * "PRDATE"))).getTime());
			 * transNumTransDateProcessDateMap.put(feedData.get("TXNNO")+transDate+procDate,
			 * brokerageMasterBO); brokerageMasterBOList = new
			 * ArrayList<BrokerageMasterBO>(); for (Map.Entry<String,BrokerageMasterBO>
			 * entry : transNumTransDateProcessDateMap.entrySet())
			 * brokerageMasterBOList.add(entry.getValue()); } } } }
			 */
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

}
