package com.finlabs.finexa.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dozer.Mapper;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.logging.NeedsLocalLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.finlabs.finexa.dto.ClientTransactAOFDTO;
import com.finlabs.finexa.dto.ClientTransactNachDTO;
import com.finlabs.finexa.dto.ClientUCCResultDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.MasterTransactMandateDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientMandateRegistration;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientTransactAOFDetail;
import com.finlabs.finexa.model.ClientTransactMandateDetail;
import com.finlabs.finexa.model.ClientUCCDetail;
import com.finlabs.finexa.model.LookupTansactHoldingType;
import com.finlabs.finexa.model.LookupTransactUCCClientDefaultDP;
import com.finlabs.finexa.model.LookupTransactUCCClientType;
import com.finlabs.finexa.model.MasterTransactAccountType;
import com.finlabs.finexa.model.MasterTransactClientUCCDetail;
import com.finlabs.finexa.model.MasterTransactCommunicationMode;
import com.finlabs.finexa.model.MasterTransactDivPayMode;

import com.finlabs.finexa.model.MasterTransactMandate;
import com.finlabs.finexa.model.MasterTransactOccupationCode;
import com.finlabs.finexa.model.MasterTransactStateCode;
import com.finlabs.finexa.model.MasterTransactTaxStatus;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.ClientMandateRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientTransactAOFDetailsRepository;
import com.finlabs.finexa.repository.ClientTransactNachMandateDetailsRepository;
import com.finlabs.finexa.repository.ClientUCCRepository;
import com.finlabs.finexa.repository.LookupTransactHoldingTypeRepository;
import com.finlabs.finexa.repository.LookupTransactKYCTypeRepository;
import com.finlabs.finexa.repository.LookupTransactUCCClientDefaultDPRepository;
import com.finlabs.finexa.repository.LookupTransactUCCClientTypeRepository;
import com.finlabs.finexa.repository.MasterTransactAccountTypeRepository;
import com.finlabs.finexa.repository.MasterTransactClientUCCDetailsRepository;
import com.finlabs.finexa.repository.MasterTransactCommunicationModeRepository;
import com.finlabs.finexa.repository.MasterTransactDivPayModeRepository;
import com.finlabs.finexa.repository.MasterTransactMandateRepository;
import com.finlabs.finexa.repository.MasterTransactOccupationCodeRepository;
import com.finlabs.finexa.repository.MasterTransactStateCodeRepository;
import com.finlabs.finexa.repository.MasterTransactTaxStatusRepository;
import com.finlabs.finexa.resources.util.FinexaConstant;
import com.finlabs.finexa.transact.MFFileUploadService;
import com.finlabs.finexa.transact.MFUPLOADSoapService;
import com.finlabs.finexa.util.BSEConstant;
import com.finlabs.finexa.util.MFTransactConstant;
import com.ibm.icu.text.SimpleDateFormat;
import com.itextpdf.io.codec.Base64.InputStream;

import jxl.CellType;

@Service("ClientTransactUploadService")
@Transactional
public class ClientTransactUploadServiceImpl implements ClientTransactUploadService {

	public static final String ISIP_STRING = "ISP";
	public static final String XSIP_STRING = "XSP";
	public static final String EMANDATE_STRING = "E-MANDATE";

	@Autowired
	private Mapper mapper;

	@Autowired
	private MasterTransactClientUCCDetailsRepository masterTransactClientUCCDetailsRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private ClientUCCRepository clientUCCRepository;

	@Autowired
	private LookupTransactHoldingTypeRepository lookupTransactHoldingTypeRepository;

	@Autowired
	private MasterTransactTaxStatusRepository masterTransactTaxStatusRepository;

	@Autowired
	private MasterTransactOccupationCodeRepository masterTransactOccupationCodeRepository;

	@Autowired
	private LookupTransactUCCClientTypeRepository lookupTransactUCCClientTypeRepository;

	@Autowired
	private LookupTransactUCCClientDefaultDPRepository LookupTransactUCCClientDefaultDPRepository;

	@Autowired
	private MasterTransactAccountTypeRepository masterTransactAccountTypeRepository;

	@Autowired
	private MasterTransactCommunicationModeRepository masterTransactCommunicationModeRepository;

	@Autowired
	private MasterTransactDivPayModeRepository masterTransactDivPayModeRepository;

	@Autowired
	private MasterTransactStateCodeRepository masterTransactStateCodeRepository;

	@Autowired
	private ClientTransactAOFDetailsRepository clientTransactAOFDetailsRepository;

	@Autowired
	private ClientTransactNachMandateDetailsRepository clientTransactNachMandateDetailsRepository;

	@Autowired
	private LookupTransactKYCTypeRepository lookupTransactKTCTypeRepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private MasterTransactMandateRepository masterTransactMandateRepository;

	@Autowired
	private ClientMandateRepository clientMandateRepository;

	public static final String STRING_VALUE = "1";
	public static final String NUMERIC_VALUE = "0";

	@SuppressWarnings("deprecation")
	@Override
	@Transactional(rollbackOn = CustomFinexaException.class)
	public UploadResponseDTO uploadBulkUsers(FileuploadDTO fileuploadDTO)
			throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();

		List<String> errorList = new ArrayList<>();
		int masterCount = 0;
		int clientCount = 0;
		try {

			File tempFile = File.createTempFile("ClientMaster", ".xlsx");
			tempFile.deleteOnExit();
			FileOutputStream out = new FileOutputStream(tempFile);
			IOUtils.copy(fileuploadDTO.getFile()[0].getInputStream(), out);

			FileInputStream excelFile = new FileInputStream(tempFile);
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			Row firstRow = iterator.next();
			Iterator<Cell> firstRowIterator = firstRow.iterator();

			Cell firstCell = firstRowIterator.next();
			String strFirstCell = new String();
			if (firstCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
				System.out.println("firstCell" + firstCell.getNumericCellValue());
				strFirstCell = "";
			} else if (firstCell.getCellTypeEnum().equals(STRING_VALUE)) {
				System.out.println("firstCell" + firstCell.getStringCellValue());
				strFirstCell = firstCell.getStringCellValue();
			}

			if ((strFirstCell.trim()).equals(MFTransactConstant.CLIENT_CODE_HEADING)) {

				// System.out.println("Inside");

				while (iterator.hasNext()) {
					MasterTransactClientUCCDetail masterTransactClientUCCDetail = null;
					ClientUCCDetail clientUCCDetail = null;
					String clientCode = "";
					Row currentRow = iterator.next();
					Iterator<Cell> cellIterator = currentRow.iterator();

					/* setClientCode */
					Cell currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						clientCode = "" + currentCell.getStringCellValue();
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						int clientCodeInInt = (int) currentCell.getNumericCellValue();
						clientCode = "" + clientCodeInInt;
					}

					masterTransactClientUCCDetail = masterTransactClientUCCDetailsRepository.findOne(clientCode);
					if (masterTransactClientUCCDetail == null) {
						masterTransactClientUCCDetail = new MasterTransactClientUCCDetail();
						masterTransactClientUCCDetail.setClientCode(clientCode);
					}

					clientUCCDetail = clientUCCRepository.findOne(clientCode);
					if (clientUCCDetail == null) {
						clientUCCDetail = new ClientUCCDetail();
						clientUCCDetail.setClientCode(clientCode);
					}

					/* setClientHolding */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientHolding(currentCell.getStringCellValue());
						LookupTansactHoldingType LookupTansactHoldingType = lookupTransactHoldingTypeRepository
								.findBydescription(currentCell.getStringCellValue());
						clientUCCDetail.setLookupTansactHoldingType(LookupTansactHoldingType);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientHolding("" + currentCell.getNumericCellValue());
						LookupTansactHoldingType LookupTansactHoldingType = lookupTransactHoldingTypeRepository
								.findBydescription(currentCell.getStringCellValue());
						clientUCCDetail.setLookupTansactHoldingType(LookupTansactHoldingType);
					}

					/* setTaxStatus */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setTaxStatus(currentCell.getStringCellValue());
						MasterTransactTaxStatus masterTransactTaxStatus = masterTransactTaxStatusRepository
								.findBytaxStatus(currentCell.getStringCellValue());
						clientUCCDetail.setMasterTransactTaxStatus(masterTransactTaxStatus);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setTaxStatus("" + currentCell.getNumericCellValue());
						MasterTransactTaxStatus masterTransactTaxStatus = masterTransactTaxStatusRepository
								.findBytaxStatus("" + currentCell.getNumericCellValue());
						clientUCCDetail.setMasterTransactTaxStatus(masterTransactTaxStatus);
					}

					/* setOccupation */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setOccupation(currentCell.getStringCellValue());
						MasterTransactOccupationCode masterTransactOccupationCode = masterTransactOccupationCodeRepository
								.findBydetails(currentCell.getStringCellValue());
						clientUCCDetail.setMasterTransactOccupationCode(masterTransactOccupationCode);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setOccupation("" + currentCell.getNumericCellValue());
						MasterTransactOccupationCode masterTransactOccupationCode = masterTransactOccupationCodeRepository
								.findBydetails("" + currentCell.getNumericCellValue());
						clientUCCDetail.setMasterTransactOccupationCode(masterTransactOccupationCode);
					}

					/* setFirstApplicantName */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setFirstApplicantName(currentCell.getStringCellValue());
						clientUCCDetail.setClientAppName1(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setFirstApplicantName("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientAppName1("" + currentCell.getNumericCellValue());
					}

					/* setSecondApplicantName */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setSecondApplicantName(currentCell.getStringCellValue());
						clientUCCDetail.setClientAppName2(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setSecondApplicantName("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientAppName2("" + currentCell.getNumericCellValue());
					}

					/* setThirdApplicantName */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setThirdApplicantName(currentCell.getStringCellValue());
						clientUCCDetail.setClientAppName3(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setThirdApplicantName("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientAppName3("" + currentCell.getNumericCellValue());
					}

					/* setFirstApplicantDOB */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setFirstApplicantDOB(currentCell.getStringCellValue());
						clientUCCDetail.setClientDOB(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setFirstApplicantDOB("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientDOB("" + currentCell.getNumericCellValue());
					}

					/* setFirstAppGender */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setFirstAppGender(currentCell.getStringCellValue());
						if (currentCell.getStringCellValue().equals("MALE")) {
							clientUCCDetail.setClientGender("M");
						} else {
							clientUCCDetail.setClientGender("F");
						}

					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setFirstAppGender("" + currentCell.getNumericCellValue());
						if (("" + currentCell.getNumericCellValue()).equals("MALE")) {
							clientUCCDetail.setClientGender("M");
						} else {
							clientUCCDetail.setClientGender("F");
						}
					}

					/* setClientGuardian */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientGuardian(currentCell.getStringCellValue());
						clientUCCDetail.setClientGuardian(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientGuardian("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientGuardian("" + currentCell.getNumericCellValue());
					}

					String pan = "";
					/* setFirstApplicantPan */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						pan = currentCell.getStringCellValue();
						masterTransactClientUCCDetail.setFirstApplicantPan(currentCell.getStringCellValue());
						clientUCCDetail.setClientPan(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setFirstApplicantPan("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientPan("" + currentCell.getNumericCellValue());
					}

					/* setClientNominee */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientNominee(currentCell.getStringCellValue());
						clientUCCDetail.setClientNominee(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientNominee("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientNominee("" + currentCell.getNumericCellValue());
					}

					/* setClientNomineeRelation */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientNomineeRelation(currentCell.getStringCellValue());
						clientUCCDetail.setClientNomineeRelation(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientNomineeRelation("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientNomineeRelation(currentCell.getStringCellValue());
					}

					/* setGuardianPan */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setGuardianPan((currentCell.getStringCellValue()));
						clientUCCDetail.setClientGuardianPan(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setGuardianPan("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientGuardianPan("" + currentCell.getNumericCellValue());

					}

					/* setClientType */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientType((currentCell.getStringCellValue()));
						LookupTransactUCCClientType lookupTransactUCCClientType = lookupTransactUCCClientTypeRepository
								.findBydescription(currentCell.getStringCellValue());
						clientUCCDetail.setLookupTransactUccclientType(lookupTransactUCCClientType);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientType("" + currentCell.getNumericCellValue());
						LookupTransactUCCClientType lookupTransactUCCClientType = lookupTransactUCCClientTypeRepository
								.findBydescription("" + currentCell.getNumericCellValue());
						clientUCCDetail.setLookupTransactUccclientType(lookupTransactUCCClientType);
					}

					/* setClientDefaultDP */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientDefaultDP((currentCell.getStringCellValue()));
						LookupTransactUCCClientDefaultDP lookupTransactUCCClientDefaultDP = LookupTransactUCCClientDefaultDPRepository
								.findBydescription(currentCell.getStringCellValue());
						clientUCCDetail.setLookupTransactUccclientDefaultDp(lookupTransactUCCClientDefaultDP);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientDefaultDP("" + currentCell.getNumericCellValue());
						LookupTransactUCCClientDefaultDP lookupTransactUCCClientDefaultDP = LookupTransactUCCClientDefaultDPRepository
								.findBydescription("" + currentCell.getNumericCellValue());
						clientUCCDetail.setLookupTransactUccclientDefaultDp(lookupTransactUCCClientDefaultDP);
					}

					/* setCdsldpid */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setCdsldpid((currentCell.getStringCellValue()));
						clientUCCDetail.setClientCDSLDPID(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setCdsldpid("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientCDSLDPID("" + currentCell.getNumericCellValue());
					}

					/* setCdslcltid */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setCdslcltid((currentCell.getStringCellValue()));
						clientUCCDetail.setClientCDSLCLTID(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						long cdslcltid = (long) currentCell.getNumericCellValue();
						masterTransactClientUCCDetail.setCdslcltid("" + cdslcltid);
						clientUCCDetail.setClientCDSLCLTID("" + cdslcltid);
					}

					/* setNsdldpid */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setNsdldpid((currentCell.getStringCellValue()));
						clientUCCDetail.setClientNSDLDPID(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setNsdldpid("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientNSDLDPID("" + currentCell.getNumericCellValue());
					}

					/* setNsdlcltid */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setNsdlcltid((currentCell.getStringCellValue()));
						clientUCCDetail.setClientNSDLCLTID(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						long nsdlclTid = (long) currentCell.getNumericCellValue();
						masterTransactClientUCCDetail.setNsdlcltid("" + nsdlclTid);
						clientUCCDetail.setClientNSDLCLTID("" + nsdlclTid);
					}

					/* setAccType1 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAccType1((currentCell.getStringCellValue()));
						MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository
								.findBydetails(currentCell.getStringCellValue());
						if (masterTransactAccountType == null) {
							masterTransactAccountType = masterTransactAccountTypeRepository
									.findBydetails("Savings Bank");
						}
						clientUCCDetail.setMasterTransactAccountType1(masterTransactAccountType);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setAccType1("" + currentCell.getNumericCellValue());
						MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository
								.findBydetails("" + currentCell.getNumericCellValue());
						clientUCCDetail.setMasterTransactAccountType1(masterTransactAccountType);
					}

					/* setAccNumber1 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAccNumber1((currentCell.getStringCellValue()));
						clientUCCDetail.setClientAccNo1(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						long accNumber = (long) currentCell.getNumericCellValue();
						masterTransactClientUCCDetail.setAccNumber1("" + accNumber);
						clientUCCDetail.setClientAccNo1("" + accNumber);
					}

					/* setClientMicrNo1 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientMicrNo1((currentCell.getStringCellValue()));
						clientUCCDetail.setClientMicrNo1(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						long micrNo = (long) currentCell.getNumericCellValue();
//						System.out.println("micrNo" + micrNo);
						masterTransactClientUCCDetail.setClientMicrNo1("" + micrNo);
						clientUCCDetail.setClientMicrNo1("" + micrNo);
					}

					/* setNeftIfscCode1 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setNeftIfscCode1(currentCell.getStringCellValue());
						clientUCCDetail.setClientIfscCode1(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setNeftIfscCode1("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientIfscCode1("" + currentCell.getNumericCellValue());
					}

					/* setBankName1 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setBankName1(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setBankName1("" + currentCell.getNumericCellValue());
					}

					/* setBankBranch1 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setBankBranch1(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setBankBranch1("" + currentCell.getNumericCellValue());
					}

					/* setDefaultBankFlag1 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setDefaultBankFlag1(currentCell.getStringCellValue());
						clientUCCDetail.setDefaultBankFlag1(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setDefaultBankFlag1("" + currentCell.getNumericCellValue());
						clientUCCDetail.setDefaultBankFlag1("" + currentCell.getNumericCellValue());
					}

					/* setAccType2 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAccType2((currentCell.getStringCellValue()));
						MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository
								.findBydetails(currentCell.getStringCellValue());
						clientUCCDetail.setMasterTransactAccountType2(masterTransactAccountType);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setAccType2("" + currentCell.getNumericCellValue());
						MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository
								.findBydetails("" + currentCell.getNumericCellValue());
						clientUCCDetail.setMasterTransactAccountType2(masterTransactAccountType);

					}

					/* setAccNumber2 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAccNumber2((currentCell.getStringCellValue()));
						clientUCCDetail.setClientAccNo2(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						long accNumber = (long) currentCell.getNumericCellValue();
						masterTransactClientUCCDetail.setAccNumber2("" + accNumber);
						clientUCCDetail.setClientAccNo2("" + accNumber);
					}

					/* setClientMicrNo2 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientMicrNo2((currentCell.getStringCellValue()));
						clientUCCDetail.setClientMicrNo2(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientMicrNo2("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientMicrNo2("" + currentCell.getNumericCellValue());
					}

					/* setNeftIfscCode2 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setNeftIfscCode2(currentCell.getStringCellValue());
						clientUCCDetail.setClientIfscCode2(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setNeftIfscCode2("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientIfscCode2("" + currentCell.getNumericCellValue());
					}

					/* setDefaultBankFlag2 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setDefaultBankFlag2(currentCell.getStringCellValue());
						clientUCCDetail.setDefaultBankFlag2(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setDefaultBankFlag2("" + currentCell.getNumericCellValue());
						clientUCCDetail.setDefaultBankFlag2("" + currentCell.getNumericCellValue());
					}

					/* setBankName2 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setBankName2(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setBankName2("" + currentCell.getNumericCellValue());
					}

					/* setBankBranch2 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setBankBranch2(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setBankBranch2("" + currentCell.getNumericCellValue());
					}

					/* setAccType3 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAccType3((currentCell.getStringCellValue()));
						MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository
								.findBydetails(currentCell.getStringCellValue());
						clientUCCDetail.setMasterTransactAccountType3(masterTransactAccountType);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setAccType3("" + currentCell.getNumericCellValue());
						MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository
								.findBydetails(currentCell.getStringCellValue());
						clientUCCDetail.setMasterTransactAccountType3(masterTransactAccountType);
					}

					/* setAccNumber3 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAccNumber3((currentCell.getStringCellValue()));
						clientUCCDetail.setClientAccNo3(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						long accNumber = (long) currentCell.getNumericCellValue();
						masterTransactClientUCCDetail.setAccNumber3("" + accNumber);
						clientUCCDetail.setClientAccNo3("" + accNumber);
					}

					/* setClientMicrNo3 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientMicrNo3((currentCell.getStringCellValue()));
						clientUCCDetail.setClientMicrNo3(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientMicrNo3("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientMicrNo3("" + currentCell.getNumericCellValue());
					}

					/* setNeftIfscCode3 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setNeftIfscCode3(currentCell.getStringCellValue());
						clientUCCDetail.setClientIfscCode3(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setNeftIfscCode3("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientIfscCode3("" + currentCell.getNumericCellValue());
					}

					/* setDefaultBankFlag3 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setDefaultBankFlag3(currentCell.getStringCellValue());
						clientUCCDetail.setDefaultBankFlag3(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setDefaultBankFlag3("" + currentCell.getNumericCellValue());
						clientUCCDetail.setDefaultBankFlag3(currentCell.getStringCellValue());
					}

					/* setBankName3 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setBankName3(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setBankName3("" + currentCell.getNumericCellValue());
					}

					/* setBankBranch3 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setBankBranch3(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setBankBranch3("" + currentCell.getNumericCellValue());
					}

					/* setAccType4 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAccType4((currentCell.getStringCellValue()));
						MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository
								.findBydetails(currentCell.getStringCellValue());
						clientUCCDetail.setMasterTransactAccountType4(masterTransactAccountType);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setAccType4("" + currentCell.getNumericCellValue());
						MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository
								.findBydetails(currentCell.getStringCellValue());
						clientUCCDetail.setMasterTransactAccountType4(masterTransactAccountType);
					}

					/* setAccNumber4 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAccNumber4((currentCell.getStringCellValue()));
						clientUCCDetail.setClientAccNo4(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						long accNumber = (long) currentCell.getNumericCellValue();
						masterTransactClientUCCDetail.setAccNumber4("" + accNumber);
						clientUCCDetail.setClientAccNo4("" + accNumber);
					}

					/* setClientMicrNo4 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientMicrNo4((currentCell.getStringCellValue()));
						clientUCCDetail.setClientMicrNo4(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientMicrNo4("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientMicrNo4("" + currentCell.getNumericCellValue());
					}

					/* setNeftIfscCode4 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setNeftIfscCode4(currentCell.getStringCellValue());
						clientUCCDetail.setClientIfscCode4(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setNeftIfscCode4("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientIfscCode4("" + currentCell.getNumericCellValue());
					}

					/* setDefaultBankFlag4 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setDefaultBankFlag4(currentCell.getStringCellValue());
						clientUCCDetail.setDefaultBankFlag4(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setDefaultBankFlag4("" + currentCell.getNumericCellValue());
						clientUCCDetail.setDefaultBankFlag4("" + currentCell.getNumericCellValue());
					}

					/* setBankName4 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setBankName4(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setBankName4("" + currentCell.getNumericCellValue());
					}

					/* setBankBranch4 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setBankBranch4(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setBankBranch4("" + currentCell.getNumericCellValue());
					}

					/* setAccType5 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAccType5((currentCell.getStringCellValue()));
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setAccType5("" + currentCell.getNumericCellValue());
					}

					/* setAccNumber5 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAccNumber5((currentCell.getStringCellValue()));
						MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository
								.findBydetails(currentCell.getStringCellValue());
						clientUCCDetail.setMasterTransactAccountType2(masterTransactAccountType);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						long accNumber = (long) currentCell.getNumericCellValue();
						masterTransactClientUCCDetail.setAccNumber5("" + accNumber);
						clientUCCDetail.setClientAccNo5("" + accNumber);
					}

					/* setClientMicrNo5 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientMicrNo5((currentCell.getStringCellValue()));
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientMicrNo5("" + currentCell.getNumericCellValue());
					}

					/* setNeftIfscCode5 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setNeftIfscCode5(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setNeftIfscCode5("" + currentCell.getNumericCellValue());
					}

					/* setBankName5 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setBankName5(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setBankName5("" + currentCell.getNumericCellValue());
					}

					/* setBankBranch5 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setBankBranch5(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setBankBranch5("" + currentCell.getNumericCellValue());
					}

					/* setDefaultBankFlag5 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setDefaultBankFlag5(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setDefaultBankFlag5("" + currentCell.getNumericCellValue());
					}
					currentCell = cellIterator.next();
					/* setAdd1 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAdd1(currentCell.getStringCellValue());
						clientUCCDetail.setClientAdd1(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setAdd1("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientAdd1("" + currentCell.getNumericCellValue());
					}

					/* setAdd2 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAdd2(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setAdd2("" + currentCell.getNumericCellValue());
					}

					/* setAdd3 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setAdd3(currentCell.getStringCellValue());

					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setAdd3("" + currentCell.getNumericCellValue());
					}

					/* setCity */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setCity(currentCell.getStringCellValue());
						clientUCCDetail.setClientCity(currentCell.getStringCellValue());

					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setCity("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientCity("" + currentCell.getNumericCellValue());

					}

					/* setClientState */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientState(currentCell.getStringCellValue());
						MasterTransactStateCode stateCode = masterTransactStateCodeRepository
								.findBydetails(currentCell.getStringCellValue());
						clientUCCDetail.setMasterTransactStateCode(stateCode);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientState("" + currentCell.getNumericCellValue());
						MasterTransactStateCode stateCode = masterTransactStateCodeRepository
								.findBydetails("" + currentCell.getNumericCellValue());
						clientUCCDetail.setMasterTransactStateCode(stateCode);
					}

					/* setPinCode */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setPinCode(currentCell.getStringCellValue());
						clientUCCDetail.setClientPincode(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						long pinCode = (long) currentCell.getNumericCellValue();
						masterTransactClientUCCDetail.setPinCode("" + pinCode);
						clientUCCDetail.setClientPincode("" + pinCode);
					}

					/* setCountry */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setCountry(currentCell.getStringCellValue());
						clientUCCDetail.setClientCountry(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setCountry("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientCountry("" + currentCell.getNumericCellValue());
					}

					/* setResiPhone */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setResiPhone(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setResiPhone("" + currentCell.getNumericCellValue());
					}

					/* setResiFax */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setResiFax(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setResiFax("" + currentCell.getNumericCellValue());
					}

					/* setOfficephone */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setOfficephone(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setOfficephone("" + currentCell.getNumericCellValue());
					}

					/* setClientOfficeFax */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientOfficeFax(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientOfficeFax("" + currentCell.getNumericCellValue());
					}

					/* setClientEmail */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setClientEmail(currentCell.getStringCellValue());
						clientUCCDetail.setClientEmail(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setClientEmail("" + currentCell.getNumericCellValue());
						clientUCCDetail.setClientEmail("" + currentCell.getNumericCellValue());

					}

					/* setCommMode */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setCommMode(currentCell.getStringCellValue());
						MasterTransactCommunicationMode masterTransactCommunicationMode = masterTransactCommunicationModeRepository
								.findBydetails(currentCell.getStringCellValue());
						if (masterTransactCommunicationMode == null) {
							masterTransactCommunicationMode = masterTransactCommunicationModeRepository
									.findBydetails("Electronic");
						}
						clientUCCDetail.setMasterTransactCommunicationMode(masterTransactCommunicationMode);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setCommMode("" + currentCell.getNumericCellValue());
						MasterTransactCommunicationMode masterTransactCommunicationMode = masterTransactCommunicationModeRepository
								.findBydetails("" + currentCell.getNumericCellValue());
						clientUCCDetail.setMasterTransactCommunicationMode(masterTransactCommunicationMode);
					}

					/* setDivPayMode */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setDivPayMode(currentCell.getStringCellValue());
						MasterTransactDivPayMode masterTransactDivPayMode = masterTransactDivPayModeRepository
								.findBydetails(currentCell.getStringCellValue());
						clientUCCDetail.setMasterTransactDivPayMode(masterTransactDivPayMode);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setDivPayMode("" + currentCell.getNumericCellValue());
						MasterTransactDivPayMode masterTransactDivPayMode = masterTransactDivPayModeRepository
								.findBydetails("" + currentCell.getNumericCellValue());
						clientUCCDetail.setMasterTransactDivPayMode(masterTransactDivPayMode);
					}

					/* setSecondPan */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setSecondAppName1(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setSecondAppName1("" + currentCell.getNumericCellValue());
					}

					/* setThirdAppPan */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setThirdAppPan(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setThirdAppPan("" + currentCell.getNumericCellValue());
					}

					/* setMapinNo */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setMapinNo(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setMapinNo("" + currentCell.getNumericCellValue());
					}

					/* setForAdd1 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setForAdd1(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setForAdd1("" + currentCell.getNumericCellValue());
					}

					/* setForAdd2 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setForAdd2(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setForAdd2("" + currentCell.getNumericCellValue());
					}

					/* setForAdd3 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setForAdd3(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setForAdd3("" + currentCell.getNumericCellValue());
					}

					/* setForCity */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setForCity(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setForCity("" + currentCell.getNumericCellValue());
					}

					/* setForPinCode */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setForPinCode(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setForPinCode("" + currentCell.getNumericCellValue());
					}

					/* setForState */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setForState(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setForState("" + currentCell.getNumericCellValue());
					}

					/* setForCountry */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setForCountry(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setForCountry("" + currentCell.getNumericCellValue());
					}

					/* setForResiPhone */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setForResiPhone(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setForResiPhone("" + currentCell.getNumericCellValue());
					}

					/* setForResiFax */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setForResiFax(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setForResiFax("" + currentCell.getNumericCellValue());
					}

					/* setForOffPhone */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setForOffPhone(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setForOffPhone("" + currentCell.getNumericCellValue());
					}

					/* setForOffFax */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setForOffFax(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setForOffFax("" + currentCell.getNumericCellValue());
					}

					/* setMobile */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setMobile(currentCell.getStringCellValue());
						clientUCCDetail.setCmMobile(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						long mobile = (long) currentCell.getNumericCellValue();
						masterTransactClientUCCDetail.setMobile("" + mobile);
						clientUCCDetail.setCmMobile("" + mobile);
					}

					/* setCkyc */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setCkyc(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setCkyc("" + currentCell.getNumericCellValue());
					}

					/* setKycType1 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setKycType1(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setKycType1("" + currentCell.getNumericCellValue());
					}

					/* setKycType2 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setKycType2(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setKycType2("" + currentCell.getNumericCellValue());
					}

					/* setKycType3 */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setKycType3(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setKycType3("" + currentCell.getNumericCellValue());
					}

					/* setKycTypeGuardian */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setKycTypeGuardian(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setKycTypeGuardian("" + currentCell.getNumericCellValue());
					}

					/* set_stHolderCkyc */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.set_stHolderCkyc(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.set_stHolderCkyc("" + currentCell.getNumericCellValue());
					}

					/* set_ndHolderCkyc */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.set_ndHolderCkyc(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.set_ndHolderCkyc("" + currentCell.getNumericCellValue());
					}

					/* set_rdHolderCkyc */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.set_rdHolderCkyc(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.set_rdHolderCkyc("" + currentCell.getNumericCellValue());
					}

					/* setGuardianCKYC */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setGuardianCKYC(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setGuardianCKYC("" + currentCell.getNumericCellValue());
					}

					/* setJointHolder1DOB */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setJointHolder1DOB(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setJointHolder1DOB("" + currentCell.getNumericCellValue());
					}

					/* setJointHolder2DOB */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setJointHolder2DOB(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setJointHolder2DOB("" + currentCell.getNumericCellValue());
					}

					/* setGuardianCKYCDOB */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setGuardianCKYCDOB(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setGuardianCKYCDOB("" + currentCell.getNumericCellValue());
					}

					/* setDealer */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setDealer(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setDealer("" + currentCell.getNumericCellValue());
					}

					/* setBranch */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setBranch(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setBranch("" + currentCell.getNumericCellValue());
					}

					/* setCreatedBy */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setCreatedBy(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setCreatedBy("" + currentCell.getNumericCellValue());
					}

					/* setCreatedAt */
					currentCell = cellIterator.next();
//					System.out.println("currentCell" + currentCell) ;
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
						String dateString = currentCell.getStringCellValue();
						Date newDate = new Date();
						try {
							newDate = format.parse(dateString);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						masterTransactClientUCCDetail.setCreatedAt(newDate);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getDateCellValue());
						/*
						 * SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); Date dt = new
						 * Date(); try { dt = sdf.parse(currentCell.getStringCellValue()); } catch
						 * (ParseException e) { // TODO Auto-generated catch block e.printStackTrace();
						 * }
						 */
						masterTransactClientUCCDetail.setCreatedAt(currentCell.getDateCellValue());
					}

					/* setLastModifiedBy */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactClientUCCDetail.setLastModifiedBy(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactClientUCCDetail.setLastModifiedBy("" + currentCell.getNumericCellValue());
					}

					/* setLastModifiedAt */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
//						System.out.println("currentCell" + currentCell.getStringCellValue());
						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
						String dateString = currentCell.getStringCellValue();
						Date newDate = new Date();
						try {
							newDate = format.parse(dateString);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						masterTransactClientUCCDetail.setLastModifiedAt(newDate);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
//						System.out.println("currentCell" + currentCell.getDateCellValue());
						masterTransactClientUCCDetail.setLastModifiedAt(currentCell.getDateCellValue());
					}
					try {
						masterTransactClientUCCDetail = masterTransactClientUCCDetailsRepository
								.save(masterTransactClientUCCDetail);
						masterCount++;
					} catch (Exception e) {
//						System.out.println("Exceptione" + e);
					}
					ClientMaster cm = clientMasterRepository.getByPanAndActiveFlag(pan, "Y");
					if (cm != null) {

						AdvisorUser advisorUser = advisorUserRepository.findOne(fileuploadDTO.getAdvisorUserId());
						clientUCCDetail.setAdvisorUser(advisorUser);
						clientUCCDetail.setClientMaster(cm);
						clientUCCRepository.save(clientUCCDetail);
						clientCount++;
					}
					try {
						masterTransactClientUCCDetail = masterTransactClientUCCDetailsRepository
								.save(masterTransactClientUCCDetail);
					} catch (Exception e) {
//						System.out.println("Exception" + e);
					}

				}

			} else {
				// System.out.println("Incorrect file / Incorrect format of data uploaded");
				throw new Exception("Incorrect file / Incorrect format of data uploaded");

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			uploadResponseDTO.setStatus(false);
			errorList.add(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			uploadResponseDTO.setStatus(false);
			errorList.add(e.getMessage());
		} catch (Exception e) {
			uploadResponseDTO.setStatus(false);
			errorList.add(e.getMessage());
		}
		uploadResponseDTO.setStatus(true);
		errorList.add("Master Records Uploaded successfully : " + masterCount);
		errorList.add("Client Records Updated : " + clientCount);
		uploadResponseDTO.setErrors(errorList);
		return uploadResponseDTO;

	}

	private String decrypt(byte[] bsePassword) {
		// TODO Auto-generated method stub
		String decrypted = "";
		try {
			String key = "Bar12345Bar12345"; // 128 bit key
			// Create key and cipher
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");

			// String enc = advisorUser.getBsePassword();
			byte[] bb = bsePassword;

			// decrypt the text
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			decrypted = new String(cipher.doFinal(bb));
			System.err.println("decrypted:" + decrypted);
		} catch (Exception e) {
		}

		return decrypted;

	}

	private String autoGeneratePassKey() {
		// TODO Auto-generated method stub
		String passKey = "8A655"; // For Test Purposes
		return passKey;
	}

	@Override
	public ClientUCCResultDTO uploadAOF(ClientTransactAOFDTO fileuploadDTO)
			throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		try {
			java.io.InputStream inputStream = fileuploadDTO.getFile()[0].getInputStream();
			byte[] bytes = IOUtils.toByteArray(inputStream);
//			System.out.println("bytes" + bytes);
			String base64Encoded = Base64.getEncoder().encodeToString(bytes);
//			System.out.println(base64Encoded);
			fileuploadDTO.setFileString(base64Encoded);

			ClientTransactAOFDetail clientTransactAOFDetail = mapper.map(fileuploadDTO, ClientTransactAOFDetail.class);
			String clientCode = fileuploadDTO.getClientCode();
			ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(fileuploadDTO.getClientCode());

			if (clientUCCDetail != null) {
				clientTransactAOFDetail.setAdvisorUser(clientUCCDetail.getAdvisorUser());
				clientTransactAOFDetail.setClientMaster(clientUCCDetail.getClientMaster());
				clientTransactAOFDetail.setAofForm(bytes);
				ClientTransactAOFDetail alreadySavedAOF = clientTransactAOFDetailsRepository
						.getByClientCodeAndSaveMode(clientCode, FinexaConstant.DRAFT_MODE);
				if (alreadySavedAOF != null) {
					clientTransactAOFDetail.setId(alreadySavedAOF.getId());
					clientTransactAOFDetail.setLastUpdatedBy(clientTransactAOFDetail.getAdvisorUser().getBseMemberId());
				} else {
					clientTransactAOFDetail.setCreatedOn(new Date());
					clientTransactAOFDetail.setCreatedBy(clientTransactAOFDetail.getAdvisorUser().getBseMemberId());
				}
				clientTransactAOFDetail.setSaveMode(FinexaConstant.DRAFT_MODE);
				clientTransactAOFDetail.setClientCode(clientCode);
				clientTransactAOFDetail = clientTransactAOFDetailsRepository.save(clientTransactAOFDetail);
				if (clientTransactAOFDetail != null) {

					String bseUserId = clientTransactAOFDetail.getAdvisorUser().getBseUsername();
					String bseMemberId = clientTransactAOFDetail.getAdvisorUser().getBseMemberId();
					String bsePassword = decrypt(clientTransactAOFDetail.getAdvisorUser().getBsePassword());
					String passKey = autoGeneratePassKey();

					MFFileUploadService mfFileUploadService = new MFFileUploadService();
					clientUCCResultDTO = mfFileUploadService.authenticateMFFileUploadService(bseMemberId, bsePassword,
							bseUserId,
							clientTransactAOFDetail.getAdvisorUser().getLookupTransactBseaccessMode().getId());

					if (clientUCCResultDTO.isStatus()) {
						// Bse call to authenticate password is successful,
						// Now upload the mandateDetails
						fileuploadDTO.setEncryptedPassword(clientUCCResultDTO.getMessage());
						clientUCCResultDTO = mfFileUploadService.fireMFAOFFileUploadRequest(fileuploadDTO,
								clientTransactAOFDetail);
						clientTransactAOFDetail.setBseResponse(clientUCCResultDTO.getMessage());
						clientTransactAOFDetail.setBseResponseCode("" + clientUCCResultDTO.getStatusCode());
						if (clientUCCResultDTO.isStatus()) {
							// ccCreation with BSE Successful. Save the result in table
							alreadySavedAOF = clientTransactAOFDetailsRepository.getByClientCodeAndSaveMode(
									clientTransactAOFDetail.getClientCode(), FinexaConstant.DRAFT_MODE);
							if (alreadySavedAOF != null) {
								clientTransactAOFDetail.setId(alreadySavedAOF.getId());
							}
							// get Mandate Id
							/*
							 * int selectedIndex = clientUCCResultDTO.getMessage().indexOf('|'); String
							 * response = clientUCCResultDTO.getMessage().substring(0, selectedIndex);
							 * String mandateId = clientUCCResultDTO.getMessage().substring((selectedIndex +
							 * 1), clientUCCResultDTO.getMessage().length()); if (mandateId != null &&
							 * !mandateId.equals("")) { clientMandateRegistration.setBseResponse("" +
							 * clientUCCResultDTO.getStatusCode());
							 * clientMandateRegistration.setBseResponse(response);
							 * clientMandateRegistration.setMandateId(mandateId); }
							 */
							/*
							 * if (array.length > 1) { String response = array[0]; String mandateId =
							 * array[1];
							 * 
							 * }
							 */
							// set in save mode

							clientTransactAOFDetail.setSaveMode(FinexaConstant.SUCCESS_MODE);
							clientTransactAOFDetail = clientTransactAOFDetailsRepository.save(clientTransactAOFDetail);
						} else {
							// set in error mode
							clientTransactAOFDetail.setSaveMode(FinexaConstant.ERROR_MODE);
							clientTransactAOFDetail = clientTransactAOFDetailsRepository.save(clientTransactAOFDetail);

						}
					}

				}
			}

		} catch (Exception e) {
		}
		return clientUCCResultDTO;
	}

	// Nach Upload Service Impl

	@Override
	public ClientUCCResultDTO uploadNach(ClientTransactNachDTO fileuploadDTO)
			throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		try {
			java.io.InputStream inputStream = fileuploadDTO.getFile()[0].getInputStream();
			byte[] bytes = IOUtils.toByteArray(inputStream);
//			System.out.println("bytes" + bytes);
			String base64Encoded = Base64.getEncoder().encodeToString(bytes);
			System.out.println(base64Encoded);
			fileuploadDTO.setFileString(base64Encoded);

			ClientTransactMandateDetail clientTransactMandateDetail = mapper.map(fileuploadDTO,
					ClientTransactMandateDetail.class);
			String clientCode = fileuploadDTO.getClientCode();
			ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(fileuploadDTO.getClientCode());

			if (clientUCCDetail != null) {
				clientTransactMandateDetail.setAdvisorUser(clientUCCDetail.getAdvisorUser());
				clientTransactMandateDetail.setClientMaster(clientUCCDetail.getClientMaster());
				clientTransactMandateDetail.setNachMandateForm(bytes);
				ClientTransactMandateDetail alreadySavedMandate = clientTransactNachMandateDetailsRepository
						.getByClientCodeAndSaveMode(clientCode, FinexaConstant.DRAFT_MODE);
				if (alreadySavedMandate != null) {
					clientTransactMandateDetail.setId(alreadySavedMandate.getId());
					clientTransactMandateDetail.setLastUpdatedBy(alreadySavedMandate.getAdvisorUser().getBseMemberId());
				} else {
					clientTransactMandateDetail.setCreatedOn(new Date());
					clientTransactMandateDetail
							.setCreatedBy(clientTransactMandateDetail.getAdvisorUser().getBseMemberId());
				}
				clientTransactMandateDetail.setSaveMode(FinexaConstant.DRAFT_MODE);
				clientTransactMandateDetail.setClientCode(clientCode);
				clientTransactMandateDetail = clientTransactNachMandateDetailsRepository
						.save(clientTransactMandateDetail);
				if (clientTransactMandateDetail != null) {

					String bseUserId = clientTransactMandateDetail.getAdvisorUser().getBseUsername();
					String bseMemberId = clientTransactMandateDetail.getAdvisorUser().getBseMemberId();
					String bsePassword = decrypt(clientTransactMandateDetail.getAdvisorUser().getBsePassword());
					String passKey = autoGeneratePassKey();

					MFFileUploadService mfFileUploadService = new MFFileUploadService();
					clientUCCResultDTO = mfFileUploadService.authenticateMFFileUploadService(bseMemberId, bsePassword,
							bseUserId,
							clientTransactMandateDetail.getAdvisorUser().getLookupTransactBseaccessMode().getId());

					if (clientUCCResultDTO.isStatus()) {
						// Bse call to authenticate password is successful,
						// Now upload the mandateDetails
						fileuploadDTO.setEncryptedPassword(clientUCCResultDTO.getMessage());
						clientUCCResultDTO = mfFileUploadService.fireMFNACHFileUploadRequest(fileuploadDTO,
								clientTransactMandateDetail);
						clientTransactMandateDetail.setBseResponse(clientUCCResultDTO.getMessage());
						clientTransactMandateDetail.setBseResponseCode("" + clientUCCResultDTO.getStatusCode());
						if (clientUCCResultDTO.isStatus()) {
							// ccCreation with BSE Successful. Save the result in table
							alreadySavedMandate = clientTransactNachMandateDetailsRepository
									.getByClientCodeAndSaveMode(clientCode, FinexaConstant.DRAFT_MODE);
							if (alreadySavedMandate != null) {
								clientTransactMandateDetail.setId(alreadySavedMandate.getId());
							}
							// get Mandate Id
							/*
							 * int selectedIndex = clientUCCResultDTO.getMessage().indexOf('|'); String
							 * response = clientUCCResultDTO.getMessage().substring(0, selectedIndex);
							 * String mandateId = clientUCCResultDTO.getMessage().substring((selectedIndex +
							 * 1), clientUCCResultDTO.getMessage().length()); if (mandateId != null &&
							 * !mandateId.equals("")) { clientMandateRegistration.setBseResponse("" +
							 * clientUCCResultDTO.getStatusCode());
							 * clientMandateRegistration.setBseResponse(response);
							 * clientMandateRegistration.setMandateId(mandateId); }
							 */
							/*
							 * if (array.length > 1) { String response = array[0]; String mandateId =
							 * array[1];
							 * 
							 * }
							 */
							// set in save mode

							clientTransactMandateDetail.setSaveMode(FinexaConstant.SUCCESS_MODE);
							clientTransactMandateDetail = clientTransactNachMandateDetailsRepository
									.save(clientTransactMandateDetail);
						} else {
							// set in error mode
							clientTransactMandateDetail.setSaveMode(FinexaConstant.ERROR_MODE);
							clientTransactMandateDetail = clientTransactNachMandateDetailsRepository
									.save(clientTransactMandateDetail);

						}
					}

				}
			}

		} catch (Exception e) {
		}
		return clientUCCResultDTO;
	}

	// -------------- Mandate Upload Service Implementation ----------------------

	@Override
	public UploadResponseDTO uploadBulkMandateDetails(MasterTransactMandateDTO fileuploadDTO)
			throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		// return null;

		// TODO Auto-generated method stub
		UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();

		List<String> errorList = new ArrayList<>();
		int mandateCount = 0;
		int clientRecordCount = 0;
		// int clientCount = 0;
		try {

			File tempFile = File.createTempFile("MandateReport", ".xlsx");
			tempFile.deleteOnExit();
			FileOutputStream out = new FileOutputStream(tempFile);
			IOUtils.copy(fileuploadDTO.getFile()[0].getInputStream(), out);

			FileInputStream excelFile = new FileInputStream(tempFile);
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			Row firstRow = iterator.next();
			Iterator<Cell> firstRowIterator = firstRow.iterator();

			// ---------------
			Cell firstCell = firstRowIterator.next();
			String strFirstCell = new String();
			if (firstCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
				System.out.println("firstCell" + firstCell.getNumericCellValue());
				strFirstCell = "";
			} else if (firstCell.getCellTypeEnum().equals(STRING_VALUE)) {
				System.out.println("firstCell" + firstCell.getStringCellValue());
				strFirstCell = firstCell.getStringCellValue();
			}

			if ((strFirstCell.trim()).equals(MFTransactConstant.MANDATE_CODE_HEADING)) {

				// System.out.println("Inside");

				while (iterator.hasNext()) {
					MasterTransactMandate masterTransactMandate = null;
					ClientMandateRegistration clientMandateReg = null;
					Row currentRow = iterator.next();
					Iterator<Cell> cellIterator = currentRow.iterator();

					/* 1. setMandateCode */
					Cell currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						int mandateCode = (int) (currentCell.getNumericCellValue());
						masterTransactMandate = masterTransactMandateRepository.findOne("" + mandateCode);
						if (masterTransactMandate == null) {
							masterTransactMandate = new MasterTransactMandate();
							masterTransactMandate.setMandateCode("" + mandateCode);
						}
					} else if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate = masterTransactMandateRepository
								.findOne("" + currentCell.getStringCellValue());
						if (masterTransactMandate == null) {
							masterTransactMandate = new MasterTransactMandate();
							masterTransactMandate.setMandateCode("" + currentCell.getStringCellValue());
						}
						// masterTransactMandate.setMandateCode(""+currentCell.getNumericCellValue());
					}

					/* 2. setClientCode */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate.setClientCode(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						int clientCodeInInt = (int) currentCell.getNumericCellValue();
						masterTransactMandate.setClientCode("" + clientCodeInInt);
					}
					if (masterTransactMandate.getClientCode() != null) {
						// find if UCC exists in Finexa
						ClientUCCDetail clientUCCDetail = clientUCCRepository
								.findOne(masterTransactMandate.getClientCode());
						if (clientUCCDetail != null) {
							// UCC is present
							clientMandateReg = clientMandateRepository
									.findByMandateId(masterTransactMandate.getMandateCode());
							if (clientMandateReg == null) {
								clientMandateReg = new ClientMandateRegistration();
								clientMandateReg.setClientMaster(clientUCCDetail.getClientMaster());
								clientMandateReg.setAdvisorUser(clientUCCDetail.getAdvisorUser());
								clientMandateReg.setClientCode(clientUCCDetail.getClientCode());
							}
						}
					}

					/* 3. setClientName */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate.setClientName(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactMandate.setClientName("" + currentCell.getNumericCellValue());
					}

					/* 4. setMemberCode */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate.setMemberCode(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactMandate.setMemberCode("" + currentCell.getNumericCellValue());
					}

					/* 5. setBankName */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate.setBankName(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactMandate.setBankName("" + currentCell.getNumericCellValue());
					}

					/* 6. setBankBranch */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate.setBankBranch(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactMandate.setBankBranch("" + currentCell.getNumericCellValue());
					}

					/* 7. setAmount */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate.setAmount(new Double(currentCell.getStringCellValue()));
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactMandate.setAmount(currentCell.getNumericCellValue());
					}
					if (clientMandateReg != null) {
						clientMandateReg.setAmount("" + (int) masterTransactMandate.getAmount());
					}
					/* 8. setRegnDate */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());

						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
						Date dt = new Date();
						try {
							dt = sdf.parse(currentCell.getStringCellValue());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						masterTransactMandate.setRegnDate(dt);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getDateCellValue());
						masterTransactMandate.setRegnDate(currentCell.getDateCellValue());
					}

					/* 9. setStatus */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate.setStatus(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactMandate.setStatus("" + currentCell.getNumericCellValue());
					}

					/* 10. setUmrnNo */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate.setUmrnNo(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactMandate.setUmrnNo("" + currentCell.getNumericCellValue());
					}

					/* 11. setRemarks */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate.setRemarks(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactMandate.setRemarks("" + currentCell.getNumericCellValue());
					}

					/* 12. setApprovedDate */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
						String dateString = currentCell.getStringCellValue();
						Date newDate = new Date();
						try {
							newDate = format.parse(dateString);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						masterTransactMandate.setApprovedDate(newDate);

					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getDateCellValue());
						masterTransactMandate.setApprovedDate(currentCell.getDateCellValue());
					}

					/* 13. setBankAccountNo */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate.setBankAccountNo(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						long accNo = (long) currentCell.getNumericCellValue();
						masterTransactMandate.setBankAccountNo("" + accNo);
					}
					if (clientMandateReg != null) {
						clientMandateReg.setAccountNo(masterTransactMandate.getBankAccountNo());
					}

					/* 14. setMandateCollectionType */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate.setMandateCollectionType(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactMandate.setMandateCollectionType("" + currentCell.getNumericCellValue());
					}

					/* 15. setMandateType */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						System.out.println("currentCell" + currentCell.getStringCellValue());
						masterTransactMandate.setMandateType(currentCell.getStringCellValue());
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getNumericCellValue());
						masterTransactMandate.setMandateType("" + currentCell.getNumericCellValue());
					}
					if (clientMandateReg != null) {
						if (masterTransactMandate.getMandateType().equals(ISIP_STRING)) {
							clientMandateReg.setMandateType("I");
						} else if (masterTransactMandate.getMandateType().equals(XSIP_STRING)) {
							clientMandateReg.setMandateType("X");
						} else if (masterTransactMandate.getMandateType().equals(EMANDATE_STRING)) {
							clientMandateReg.setMandateType("E");
						}

					}
					/* 16. setDateOfUpload */
					currentCell = cellIterator.next();
					if (currentCell.getCellTypeEnum().equals(STRING_VALUE)) {
						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
						String dateString = currentCell.getStringCellValue();
						Date newDate = new Date();
						try {
							newDate = format.parse(dateString);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						/*
						 * DateTime dt = new DateTime(); DateTimeFormatter fmt =
						 * DateTimeFormat.forPattern("MMMM, yyyy"); String str = fmt.print(dt);
						 */

						masterTransactMandate.setDateOfUpload(newDate);
					} else if (currentCell.getCellTypeEnum().equals(NUMERIC_VALUE)) {
						System.out.println("currentCell" + currentCell.getDateCellValue());
						masterTransactMandate.setDateOfUpload(currentCell.getDateCellValue());
					}
					// masterTransactMandate =
					// masterTransactMandateRepository.save(masterTransactMandate);
					if (clientMandateReg != null) {
						MasterTransactAccountType accType = masterTransactAccountTypeRepository.findOne("SB");
						clientMandateReg.setMasterTransactAccountType(accType);

						// No Fields available in Excel, so uploading the default date
						clientMandateReg.setStartDate(new Date());
						clientMandateReg.setEndDate(new Date());
						clientMandateReg.setSaveMode(FinexaConstant.SUCCESS_MODE);
						clientMandateReg.setCreatedAt(new Date());
						clientMandateReg.setMandateId(masterTransactMandate.getMandateCode());
						clientMandateReg.setBseResponse("Uploaded From Excel");
					}
					try {
						masterTransactMandate = masterTransactMandateRepository.save(masterTransactMandate);
						mandateCount++;
					} catch (Exception e) {
						System.out.println("Exceptione" + e);
					}
					try {
						clientMandateReg = clientMandateRepository.save(clientMandateReg);
						clientRecordCount++;
					} catch (Exception e) {
						System.out.println("Exceptione" + e);
					}

				}

			} else {
				// System.out.println("Incorrect file / Incorrect format of data uploaded");
				throw new Exception("Incorrect file / Incorrect format of data uploaded");
			}
			// -----------------------------

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			uploadResponseDTO.setStatus(false);
			errorList.add(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			uploadResponseDTO.setStatus(false);
			errorList.add(e.getMessage());
		} catch (Exception e) {
			uploadResponseDTO.setStatus(false);
			errorList.add(e.getMessage());
		}

		uploadResponseDTO.setStatus(true);
		errorList.add("Mandate Records Uploaded successfully : " + mandateCount);
		errorList.add("Client Records Updated : " + clientRecordCount);
		uploadResponseDTO.setErrors(errorList);
		return uploadResponseDTO;

	}
	// ------------------------------------------------------

	// --------------- Fatca Details Upload -------------

	/*
	 * public UploadResponseDTO uploadBulkFatcaDetails(MasterTransactFatcaDTO
	 * fileuploadDTO) throws RuntimeException, CustomFinexaException { // TODO
	 * Auto-generated method stub UploadResponseDTO uploadResponseDTO = new
	 * UploadResponseDTO();
	 * 
	 * List<String> errorList = new ArrayList<>(); int fatcaCount = 0; //int
	 * clientCount = 0; try {
	 * 
	 * File tempFile = File.createTempFile("FatcaTemp", ".xlsx");
	 * tempFile.deleteOnExit(); FileOutputStream out = new
	 * FileOutputStream(tempFile);
	 * IOUtils.copy(fileuploadDTO.getFile()[0].getInputStream(), out);
	 * 
	 * FileInputStream excelFile = new FileInputStream(tempFile); Workbook workbook
	 * = new XSSFWorkbook(excelFile); Sheet datatypeSheet = workbook.getSheetAt(0);
	 * Iterator<Row> iterator = datatypeSheet.iterator(); Row firstRow =
	 * iterator.next(); Iterator<Cell> firstRowIterator = firstRow.iterator(); while
	 * (iterator.hasNext()) { MasterTransactFatca masterTransactFatca = new
	 * MasterTransactFatca(); //ClientUCCDetail clientUCCDetail = null; String
	 * clientCode = ""; Row currentRow = iterator.next(); Iterator<Cell>
	 * cellIterator = currentRow.iterator();
	 * 
	 * //currentCell = cellIterator.next();
	 * 
	 * 1. 1. setId (alias name for SRN No. in the excel) Cell currentCell =
	 * cellIterator.next(); if (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setId(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setId(""+currentCell.getNumericCellValue()); }
	 * 
	 * 38. 2. setPanRP currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setPanRP(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setPanRP(""+currentCell.getNumericCellValue()); }
	 * 
	 * 39. 3. setPekrn currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setPekrn(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setPekrn(""+currentCell.getNumericCellValue()); }
	 * 
	 * 26. 4. setInvName currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setInvName(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setInvName(""+currentCell.getNumericCellValue()); }
	 * 
	 * 10. 5. setDob currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); Date dt = new
	 * Date(); try{ dt = sdf.parse(currentCell.getStringCellValue()); }
	 * catch(ParseException e){ // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * masterTransactFatca.setDob(dt);
	 * 
	 * } else if (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getDateCellValue());
	 * masterTransactFatca.setDob(currentCell.getDateCellValue()); }
	 * 
	 * 17. 6. setFrName currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setFrName(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setFrName(""+currentCell.getNumericCellValue()); }
	 * 
	 * 44. 7. setSpName currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setSpName(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setSpName(""+currentCell.getNumericCellValue()); }
	 * 
	 * 51. 8. setTaxStatus currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setTaxStatus(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setTaxStatus(""+currentCell.getNumericCellValue()); }
	 * 
	 * 9. 9. setDataSrc currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setDataSrc(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setDataSrc(""+currentCell.getNumericCellValue()); }
	 * 
	 * 4. 10. setAddrType currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setAddrType(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setAddrType(""+currentCell.getNumericCellValue()); }
	 * 
	 * 41. 11. setPoBirInc currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setPoBirInc(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setPoBirInc(""+currentCell.getNumericCellValue()); }
	 * 
	 * 5. 12. setCoBirInc currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setCoBirInc(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setCoBirInc(""+currentCell.getNumericCellValue()); }
	 * 
	 * 47. 13. setTaxRes1 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setTaxRes1(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setTaxRes1(""+currentCell.getNumericCellValue()); }
	 * 
	 * 52. 14. setTpin1 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setTpin1(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setTpin1(""+currentCell.getNumericCellValue()); }
	 * 
	 * 21. 15. setId1Type currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setId1Type(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setId1Type(""+currentCell.getNumericCellValue()); }
	 * 
	 * 48. 16. setTaxRes2 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setTaxRes2(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setTaxRes2(""+currentCell.getNumericCellValue()); }
	 * 
	 * 53. 17. setTpin2 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setTpin2(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setTpin2(""+currentCell.getNumericCellValue()); }
	 * 
	 * 22. 18. setId2Type currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setId2Type(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setId2Type(""+currentCell.getNumericCellValue()); }
	 * 
	 * 49. 19. setTaxRes3 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setTaxRes3(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setTaxRes3(""+currentCell.getNumericCellValue()); }
	 * 
	 * 54. 20. setTpin3 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setTpin3(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setTpin3(""+currentCell.getNumericCellValue()); }
	 * 
	 * 23. 21. setId3Type currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setId3Type(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setId3Type(""+currentCell.getNumericCellValue()); }
	 * 
	 * 50. 22. setTaxRes4 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setTaxRes4(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setTaxRes4(""+currentCell.getNumericCellValue()); }
	 * 
	 * 55. 23. setTpin4 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setTpin4(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setTpin4(""+currentCell.getNumericCellValue()); }
	 * 
	 * 24. 24. setId4Type currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setId4Type(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setId4Type(""+currentCell.getNumericCellValue()); }
	 * 
	 * 46. 25. setSrceWealt currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setSrceWealt(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setSrceWealt(""+currentCell.getNumericCellValue()); }
	 * 
	 * 6. 26. setCorpServs currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setCorpServs(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setCorpServs(""+currentCell.getNumericCellValue()); }
	 * 
	 * 25. 27. setIncSlab currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setIncSlab(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setIncSlab(""+currentCell.getNumericCellValue()); }
	 * 
	 * 32. 28. setNetWorth currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setNetWorth(new
	 * BigInteger(currentCell.getStringCellValue())); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setNetWorth(new
	 * BigInteger(""+currentCell.getNumericCellValue())); }
	 * 
	 * 35. 29. setNwDate currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * 
	 * SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); Date dt = new
	 * Date(); try{ dt = sdf.parse(currentCell.getStringCellValue()); }
	 * catch(ParseException e){ // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * masterTransactFatca.setNwDate(dt); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getDateCellValue());
	 * masterTransactFatca.setNwDate(currentCell.getDateCellValue()); }
	 * 
	 * 40. 30. setPepFlag currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setPepFlag(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setPepFlag(""+currentCell.getNumericCellValue()); }
	 * 
	 * 36. 31. setOccCode currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setOccCode(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setOccCode(""+currentCell.getNumericCellValue()); }
	 * 
	 * 37. 32. setOccType currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setOccType(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setOccType(""+currentCell.getNumericCellValue()); }
	 * 
	 * 12. 33. setExempCode currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setExempCode(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setExempCode(""+currentCell.getNumericCellValue()); }
	 * 
	 * 13. 34. setFfiDrnfe currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setFfiDrnfe(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setFfiDrnfe(""+currentCell.getNumericCellValue()); }
	 * 
	 * 20. 35. setGiinNo currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setGiinNo(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setGiinNo(""+currentCell.getNumericCellValue()); }
	 * 
	 * 45. 36. setSprEntity currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setSprEntity(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setSprEntity(""+currentCell.getNumericCellValue()); }
	 * 
	 * 19. 37. setGiinNa currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setGiinNa(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setGiinNa(""+currentCell.getNumericCellValue()); }
	 * 
	 * 18. 38. setGiinExemc currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setGiinExemc(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setGiinExemc(""+currentCell.getNumericCellValue()); }
	 * 
	 * 34. 39. setNffeCatg currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setNffeCatg(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setNffeCatg(""+currentCell.getNumericCellValue()); }
	 * 
	 * 3. 40. setActNfeSc currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setActNfeSc(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setActNfeSc(""+currentCell.getNumericCellValue()); }
	 * 
	 * 31. 41. setNatureBus currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setNatureBus(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setNatureBus(""+currentCell.getNumericCellValue()); }
	 * 
	 * 42. 42. setRelListed currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setRelListed(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setRelListed(""+currentCell.getNumericCellValue()); }
	 * 
	 * 11. 43. setExchName currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setExchName(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setExchName(""+currentCell.getNumericCellValue()); }
	 * 
	 * 60. 44. setUboAppl currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboAppl(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboAppl(""+currentCell.getNumericCellValue()); }
	 * 
	 * 64. 45. setUboCount currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboCount(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboCount(""+currentCell.getNumericCellValue()); }
	 * 
	 * 73. 46. setUboName currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboName(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboName(""+currentCell.getNumericCellValue()); }
	 * 
	 * 77. 47. setUboPan currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboPan(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboPan(""+currentCell.getNumericCellValue()); }
	 * 
	 * 74. 48. setUboNation currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboNation(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboNation(""+currentCell.getNumericCellValue()); }
	 * 
	 * 56. 49. setUboAdd1 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboAdd1(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboAdd1(""+currentCell.getNumericCellValue()); }
	 * 
	 * 57. 50. setUboAdd2 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboAdd2(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboAdd2(""+currentCell.getNumericCellValue()); }
	 * 
	 * 58. 51. setUboAdd3 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboAdd3(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboAdd3(""+currentCell.getNumericCellValue()); }
	 * 
	 * 61. 52. setUboCity currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboCity(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboCity(""+currentCell.getNumericCellValue()); }
	 * 
	 * 78. 53. setUboPin currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboPin(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboPin(""+currentCell.getNumericCellValue()); }
	 * 
	 * 79. 54. setUboState currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboState(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboState(""+currentCell.getNumericCellValue()); }
	 * 
	 * 62. 55. setUboCntry currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboCntry(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboCntry(""+currentCell.getNumericCellValue()); }
	 * 
	 * 59. 56. setUboAddTy currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboAddTy(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboAddTy(""+currentCell.getNumericCellValue()); }
	 * 
	 * 65. 57. setUboCtr currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboCtr(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboCtr(""+currentCell.getNumericCellValue()); }
	 * 
	 * 81. 58. setUboTin currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboTin(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboTin(""+currentCell.getNumericCellValue()); }
	 * 
	 * 71. 59. setUboIdTy currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboIdTy(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboIdTy(""+currentCell.getNumericCellValue()); }
	 * 
	 * 63. 60. setUboCob currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboCob(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboCob(""+currentCell.getNumericCellValue()); }
	 * 
	 * 67. 61. setUboDob currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * 
	 * SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); Date dt = new
	 * Date(); try{ dt = sdf.parse(currentCell.getStringCellValue()); }
	 * catch(ParseException e){ // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * masterTransactFatca.setUboDob(dt); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getDateCellValue());
	 * masterTransactFatca.setUboDob(currentCell.getDateCellValue()); }
	 * 
	 * 69. 62. setUboGender currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboGender(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboGender(""+currentCell.getNumericCellValue()); }
	 * 
	 * 68. 63. setUboFrNam currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboFrNam(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboFrNam(""+currentCell.getNumericCellValue()); }
	 * 
	 * 75. 64. setUboOcc currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboOcc(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboOcc(""+currentCell.getNumericCellValue()); }
	 * 
	 * 76. 65. setUboOccTy currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboOccTy(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboOccTy(""+currentCell.getNumericCellValue()); }
	 * 
	 * 80. 66. setUboTel currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboTel(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboTel(""+currentCell.getNumericCellValue()); }
	 * 
	 * 72. 67. setUboMobile currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboMobile(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboMobile(""+currentCell.getNumericCellValue()); }
	 * 
	 * 00. 68. setUboCode currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboCode(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboCode(""+currentCell.getNumericCellValue()); }
	 * 
	 * 70. 69. setUboHolPc currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboHolPc(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboHolPc(""+currentCell.getNumericCellValue()); }
	 * 
	 * 43. 70. setSdfFlag currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setSdfFlag(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setSdfFlag(""+currentCell.getNumericCellValue()); }
	 * 
	 * 66. 71. setUboDf currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setUboDf(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setUboDf(""+currentCell.getNumericCellValue()); }
	 * 
	 * 2. 72. setAadhaarRp currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setAadhaarRp(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setAadhaarRp(""+currentCell.getNumericCellValue()); }
	 * 
	 * 33. 73. setNewChange currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setNewChange(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setNewChange(""+currentCell.getNumericCellValue()); }
	 * 
	 * 27. 74. setLogName currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setLogName(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setLogName(""+currentCell.getNumericCellValue()); }
	 * 
	 * 15. 75. setFiller1 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setFiller1(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setFiller1(""+currentCell.getNumericCellValue()); }
	 * 
	 * 16. 76. setFiller2 currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setFiller2(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setFiller2(""+currentCell.getNumericCellValue()); }
	 * 
	 * 28. 77. setMemberId currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setMemberId(Integer.parseInt(currentCell.
	 * getStringCellValue())); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setMemberId(Integer.parseInt(""+currentCell.
	 * getNumericCellValue())); }
	 * 
	 * 14. 78. setFileName currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setFileName(currentCell.getStringCellValue()); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setFileName(""+currentCell.getNumericCellValue()); }
	 * 
	 * 8. 79. setCreatedId currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setCreatedId(Integer.parseInt(currentCell.
	 * getStringCellValue())); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setCreatedId(Integer.parseInt(""+currentCell.
	 * getNumericCellValue())); }
	 * 
	 * 7. 80. setCreatedDateTime currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * 
	 * SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); Date dt = new
	 * Date(); try{ dt = sdf.parse(currentCell.getStringCellValue()); }
	 * catch(ParseException e){ // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * masterTransactFatca.setCreatedDateTime(dt); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getDateCellValue());
	 * masterTransactFatca.setCreatedDateTime(currentCell.getDateCellValue()); }
	 * 
	 * 30. 81. setModifiedBy currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * masterTransactFatca.setModifiedBy(currentCell.getStringCellValue()); } else
	 * if (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getNumericCellValue());
	 * masterTransactFatca.setModifiedBy(""+currentCell.getNumericCellValue()); }
	 * 
	 * 29. 82. setModifiedAt currentCell = cellIterator.next(); if
	 * (currentCell.getCellType().equals(STRING_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getStringCellValue());
	 * 
	 * SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); Date dt = new
	 * Date(); try{ dt = sdf.parse(currentCell.getStringCellValue()); }
	 * catch(ParseException e){ // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * masterTransactFatca.setModifiedAt(dt); } else if
	 * (currentCell.getCellType().equals(NUMERIC_VALUE)) {
	 * System.out.println("currentCell" + currentCell.getDateCellValue());
	 * masterTransactFatca.setModifiedAt(currentCell.getDateCellValue()); }
	 * 
	 * 
	 * 
	 * try { masterTransactFatca =
	 * masterTransactFatcaRepository.save(masterTransactFatca); fatcaCount ++; }
	 * catch (Exception e) { System.out.println("Exceptione" + e); }
	 * 
	 * 
	 * } } catch (FileNotFoundException e) { e.printStackTrace();
	 * uploadResponseDTO.setStatus(false); errorList.add(e.getMessage()); } catch
	 * (IOException e) { e.printStackTrace(); uploadResponseDTO.setStatus(false);
	 * errorList.add(e.getMessage()); } catch (Exception e) {
	 * uploadResponseDTO.setStatus(false); errorList.add(e.getMessage()); }
	 * uploadResponseDTO.setStatus(true);
	 * errorList.add("Fatca Records successfully Uploaded" + fatcaCount);
	 * //errorList.add("Client Records Transfered" + clientCount);
	 * uploadResponseDTO.setErrors(errorList); return uploadResponseDTO;
	 * 
	 * }
	 */

	// ----------------------------

}
