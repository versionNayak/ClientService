package com.finlabs.finexa.service;

import java.io.File;
import java.math.BigInteger;
import java.security.Key;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finlabs.finexa.dto.BankNameDTO;
import com.finlabs.finexa.dto.CartOrderStatusDTO;
import com.finlabs.finexa.dto.ClientFatcaDTO;
import com.finlabs.finexa.dto.ClientRedeemDTO;
import com.finlabs.finexa.dto.ClientSTPDTO;
import com.finlabs.finexa.dto.ClientSWPDTO;
import com.finlabs.finexa.dto.ClientSwitchOrderEntryParamDTO;
import com.finlabs.finexa.dto.ClientUCCDetailsDTO;
import com.finlabs.finexa.dto.ClientUCCDraftModeDTO;
import com.finlabs.finexa.dto.ClientUCCResultDTO;
import com.finlabs.finexa.dto.ExistingClientUCCDTO;
import com.finlabs.finexa.dto.IFSCCodeDTO;
import com.finlabs.finexa.dto.InvestDTO;
import com.finlabs.finexa.dto.LumpsumCartDTO;
import com.finlabs.finexa.dto.MandateDTO;
import com.finlabs.finexa.dto.MasterBankNameIFSCCodeDTO;
import com.finlabs.finexa.dto.ProductRecommendationTransactDTO;
import com.finlabs.finexa.dto.PurchaseOrderEntryParamDTO;
import com.finlabs.finexa.dto.SIPCartDTO;
import com.finlabs.finexa.dto.SIPOrderEntryParamDTO;
import com.finlabs.finexa.dto.STPCartDTO;
import com.finlabs.finexa.dto.SWPCartDTO;
import com.finlabs.finexa.dto.SwitchCartDTO;
import com.finlabs.finexa.dto.ViewClientUCCDetailsDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorProductReco;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientCKYCDetail;
import com.finlabs.finexa.model.ClientFatcaReport;
import com.finlabs.finexa.model.ClientMandateRegistration;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientSTPOrderRegistration;
import com.finlabs.finexa.model.ClientSWPOrderRegistration;
import com.finlabs.finexa.model.ClientSwitchOrderEntryParam;
import com.finlabs.finexa.model.ClientTransactRedeemOrder;
import com.finlabs.finexa.model.ClientUCCDetail;
import com.finlabs.finexa.model.ClientUCCDetailsDraftMode;
import com.finlabs.finexa.model.LookupTansactHoldingType;
import com.finlabs.finexa.model.LookupTransactKYCType;
import com.finlabs.finexa.model.LookupTransactUCCClientDefaultDP;
import com.finlabs.finexa.model.LookupTransactUCCClientType;
import com.finlabs.finexa.model.MasterBankNameIFSCCode;
import com.finlabs.finexa.model.MasterTransactAccountType;
import com.finlabs.finexa.model.MasterTransactAddressType;
import com.finlabs.finexa.model.MasterTransactBSEMFPhysicalScheme;
import com.finlabs.finexa.model.MasterTransactBSEMFPhysicalSchemeLive;
import com.finlabs.finexa.model.MasterTransactClientUCCDetail;
import com.finlabs.finexa.model.MasterTransactCommunicationMode;
import com.finlabs.finexa.model.MasterTransactCountryNationality;
import com.finlabs.finexa.model.MasterTransactDivPayMode;
import com.finlabs.finexa.model.MasterTransactIdentificationType;
import com.finlabs.finexa.model.MasterTransactIncome;
import com.finlabs.finexa.model.MasterTransactOccupationCode;
import com.finlabs.finexa.model.MasterTransactSourceOfWealth;
import com.finlabs.finexa.model.MasterTransactStateCode;
import com.finlabs.finexa.model.MasterTransactTaxStatus;
import com.finlabs.finexa.model.PurchaseOrderEntryParam;
import com.finlabs.finexa.model.UniqueIdentifierNumberDateWise;
import com.finlabs.finexa.model.XsipOrderEntryParam;
import com.finlabs.finexa.repository.AdvisorProductRecoRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.ClientFatcaRepository;
import com.finlabs.finexa.repository.ClientKYCRepository;
import com.finlabs.finexa.repository.ClientMandateRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientSTPRepository;
import com.finlabs.finexa.repository.ClientSWPRepository;
import com.finlabs.finexa.repository.ClientSwitchOrderEntryParamRepository;
import com.finlabs.finexa.repository.ClientTransactDAO;
import com.finlabs.finexa.repository.ClientUCCDetailsDraftModeRepository;
import com.finlabs.finexa.repository.ClientUCCRepository;
import com.finlabs.finexa.repository.LookupTransactHoldingTypeRepository;
import com.finlabs.finexa.repository.LookupTransactKYCTypeRepository;
import com.finlabs.finexa.repository.LookupTransactUCCClientDefaultDPRepository;
import com.finlabs.finexa.repository.LookupTransactUCCClientTypeRepository;
import com.finlabs.finexa.repository.MasterBankNameIFSCCodeRepository;
import com.finlabs.finexa.repository.MasterTransactAccountTypeRepository;
import com.finlabs.finexa.repository.MasterTransactBSEMFPhysicalRepository;
import com.finlabs.finexa.repository.MasterTransactBSEMFPhysicalRepositoryLive;
import com.finlabs.finexa.repository.MasterTransactClientUCCDetailsRepository;
import com.finlabs.finexa.repository.MasterTransactCommunicationModeRepository;
import com.finlabs.finexa.repository.MasterTransactCountryNationalityRepository;
import com.finlabs.finexa.repository.MasterTransactDivPayModeRepository;
import com.finlabs.finexa.repository.MasterTransactFatcaAddressTypeRepository;
import com.finlabs.finexa.repository.MasterTransactIdentificationTypeRepository;
import com.finlabs.finexa.repository.MasterTransactIncomeTypeRepository;
import com.finlabs.finexa.repository.MasterTransactOccupationCodeRepository;
import com.finlabs.finexa.repository.MasterTransactSourceOfWealthRepository;
import com.finlabs.finexa.repository.MasterTransactStateCodeRepository;
import com.finlabs.finexa.repository.MasterTransactTaxStatusRepository;
import com.finlabs.finexa.repository.PurchaseOrderEntryParamRepository;
import com.finlabs.finexa.repository.RedeemPurchaseOrderRepository;
import com.finlabs.finexa.repository.SIPOrderEntryParamRepository;
import com.finlabs.finexa.repository.UniqueIdentifierRepository;
import com.finlabs.finexa.resources.util.FinexaConstant;
import com.finlabs.finexa.transact.MFOrderEntryService;
import com.finlabs.finexa.transact.MFUPLOADSoapService;
import com.finlabs.finexa.util.BSEConstant;
import com.finlabs.finexa.util.FinexaErrorCodes;
import com.finlabs.finexa.util.MFTransactConstant;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.taragana.utils.PDFReplacer;

@Service("ClientTransactService")
@Transactional
public class ClientTransactServiceImpl implements ClientTransactService {


	public static final int ROLLING_FREQUENCY = 1;

	@Autowired
	private Mapper mapper;

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
	private LookupTransactKYCTypeRepository lookupTransactKTCTypeRepository;

	@Autowired
	private ClientUCCRepository clientUCCRepository;

	@Autowired
	private ClientUCCDetailsDraftModeRepository clientUCCDetailsDraftModeRepository;

	@Autowired
	private ClientKYCRepository clientKYCRepository;

	@Autowired
	private MasterTransactClientUCCDetailsRepository masterTransactClientUCCDetailsRepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private ClientMandateRepository clientMandateRepository;

	@Autowired
	private ClientFatcaRepository clientFatcaRepository;

	@Autowired
	private MasterTransactIdentificationTypeRepository masterTransactIdentificationTypeRepository;

	@Autowired
	private MasterTransactFatcaAddressTypeRepository masterTransactFatcaAddressTypeRepository;

	@Autowired
	private MasterTransactCountryNationalityRepository masterTransactCountryNationalityRepository;

	@Autowired
	private MasterTransactSourceOfWealthRepository masterTransactSourceOfWealthRepository;

	@Autowired
	private MasterTransactIncomeTypeRepository masterTransactIncomeTypeRepository;
	
	@Autowired
	private MasterTransactBSEMFPhysicalRepository masterTransactBSEMFPhysicalRepository;
	
	@Autowired
	private MasterTransactBSEMFPhysicalRepositoryLive masterTransactBSEMFPhysicalRepositoryLive;

	@Autowired
	private PurchaseOrderEntryParamRepository purchaseOrderEntryParamRepository;

	@Autowired
	private SIPOrderEntryParamRepository sipOrderEntryParamRepository;

	@Autowired
	private ClientSwitchOrderEntryParamRepository clientSwitchOrderEntryParamRepository;

	@Autowired
	private ClientSWPRepository clientSWPRepository;

	@Autowired
	private ClientSTPRepository clientSTPRepository;

	@Autowired
	private RedeemPurchaseOrderRepository redeemRepository;

	@Autowired
	private AdvisorProductRecoRepository advisorProductRecoRepository;

	@Autowired
	private UniqueIdentifierRepository uniqueIdentifierNumberDateWiseRepository;
	
	@Autowired
	private LookupTransactKYCTypeRepository lookupTransactKYCTypeRepository;

	@Autowired
	private MasterBankNameIFSCCodeRepository masterBankNameIFSCCodeRepository;
	
	
	public static final int STATUS_USER_NOT_REGISTERED = 101;
	public static final int STATUS_USER_DATA_NOT_SAVED = 201;
	public static final int STATUS_ADVISOR_REGISTERED = 100;
	public static final String DATE_UNDEFINED = "NA";
	
	 private static final String[] tensNames = {
			    "",
			    " ten",
			    " twenty",
			    " thirty",
			    " forty",
			    " fifty",
			    " sixty",
			    " seventy",
			    " eighty",
			    " ninety"
			  };
	 private static final String[] numNames = {
			    "",
			    " one",
			    " two",
			    " three",
			    " four",
			    " five",
			    " six",
			    " seven",
			    " eight",
			    " nine",
			    " ten",
			    " eleven",
			    " twelve",
			    " thirteen",
			    " fourteen",
			    " fifteen",
			    " sixteen",
			    " seventeen",
			    " eighteen",
			    " nineteen"
			  };

	  public String convert(long number) {
		    // 0 to 999 999 999 999
		    if (number == 0) { return "zero"; }

		    String snumber = Long.toString(number);

		    // pad with "0"
		    String mask = "000000000000";
		    DecimalFormat df = new DecimalFormat(mask);
		    snumber = df.format(number);

		    // XXXnnnnnnnnn
		    int billions = Integer.parseInt(snumber.substring(0,3));
		    // nnnXXXnnnnnn
		    int millions  = Integer.parseInt(snumber.substring(3,6));
		    // nnnnnnXXXnnn
		    int hundredThousands = Integer.parseInt(snumber.substring(6,9));
		    // nnnnnnnnnXXX
		    int thousands = Integer.parseInt(snumber.substring(9,12));

		    String tradBillions;
		    switch (billions) {
		    case 0:
		      tradBillions = "";
		      break;
		    case 1 :
		      tradBillions = convertLessThanOneThousand(billions)
		      + " billion ";
		      break;
		    default :
		      tradBillions = convertLessThanOneThousand(billions)
		      + " billion ";
		    }
		    String result =  tradBillions;

		    String tradMillions;
		    switch (millions) {
		    case 0:
		      tradMillions = "";
		      break;
		    case 1 :
		      tradMillions = convertLessThanOneThousand(millions)
		         + " million ";
		      break;
		    default :
		      tradMillions = convertLessThanOneThousand(millions)
		         + " million ";
		    }
		    result =  result + tradMillions;

		    String tradHundredThousands;
		    switch (hundredThousands) {
		    case 0:
		      tradHundredThousands = "";
		      break;
		    case 1 :
		      tradHundredThousands = "one thousand ";
		      break;
		    default :
		      tradHundredThousands = convertLessThanOneThousand(hundredThousands)
		         + " thousand ";
		    }
		    result =  result + tradHundredThousands;

		    String tradThousand;
		    tradThousand = convertLessThanOneThousand(thousands);
		    result =  result + tradThousand;

		    // remove extra spaces!
		    return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
		  }
	  private String convertLessThanOneThousand(int number) {
		    String soFar;

		    if (number % 100 < 20){
		      soFar = numNames[number % 100];
		      number /= 100;
		    }
		    else {
		      soFar = numNames[number % 10];
		      number /= 10;

		      soFar = tensNames[number % 10] + soFar;
		      number /= 10;
		    }
		    if (number == 0) return soFar;
		    return numNames[number] + " hundred" + soFar;
		  }

	 
	@Override
	public ClientUCCResultDTO authenticateAdvisor(int advisorId, Session session) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		ClientUCCResultDTO resultDto = new ClientUCCResultDTO();
		ClientTransactDAO clientTransactDao = new ClientTransactDAOImpl();
		try {
			AdvisorUser advisorUser = clientTransactDao.getAdvisorById(advisorId,session);
			if (advisorUser != null) {
				if (advisorUser.getBseUsername() == null || advisorUser.getBsePassword() == null || 
						advisorUser.getBseMemberId() == null) {
					resultDto.setMessage(MFTransactConstant.ADVISOR_DATA_NOT_SAVED);
					resultDto.setStatus(false);
					resultDto.setStatusCode(STATUS_USER_DATA_NOT_SAVED);
				} else {
					String passKey = autoGeneratePassKey();
					String password = decrypt(advisorUser.getBsePassword());
					MFUPLOADSoapService soapClient = new MFUPLOADSoapService();
					 resultDto = soapClient.authenticateMFUploadService
							(advisorUser.getBseUsername(), advisorUser.getBseMemberId(), password, passKey, advisorUser.getLookupTransactBseaccessMode().getId());
					if (resultDto.isStatus()) {
						resultDto.setStatusCode(STATUS_ADVISOR_REGISTERED);
					} else {
						resultDto.setStatusCode(STATUS_USER_NOT_REGISTERED);
					}
				}
			}
		} catch (Exception e) {
			throw new FinexaBussinessException("Transact", "500", "Failed To Get AdvisorDetails");
		}

		return resultDto;
	}
	private String decrypt(byte[] bsePassword) {
		// TODO Auto-generated method stub
		String decrypted = "";
		try {
			String key = "Bar12345Bar12345"; // 128 bit key
			// Create key and cipher
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");

			//			String enc = advisorUser.getBsePassword();
			byte[] bb = bsePassword;

			// decrypt the text
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			decrypted = new String(cipher.doFinal(bb));
			System.err.println("decrypted:" + decrypted);
		} catch (Exception e) {}

		return decrypted;

	}
	@Override
	public boolean editAOFPDF(String clientCode, Session session, int docType, String mandateId) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		boolean status = false;
		ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(clientCode);
		if (docType == FinexaConstant.DOCUMENT_TYPE_AOF) {
			try {
				if(clientUCCDetail != null) {
					PDFReplacer pdfReplacer = new PDFReplacer();
					Map<String,String> aofMap = new HashMap<String, String>();
					aofMap.put("name1", clientUCCDetail.getClientAppName1());
					aofMap.put("pan1", clientUCCDetail.getClientPan());
					aofMap.put("dob1", clientUCCDetail.getClientDOB());
					aofMap.put("address1", clientUCCDetail.getClientAdd1());
					aofMap.put("city1", clientUCCDetail.getClientCity());
					aofMap.put("pinCode1", clientUCCDetail.getClientPincode());
					aofMap.put("state1", clientUCCDetail.getMasterTransactStateCode().getDetails());
					aofMap.put("country1", clientUCCDetail.getClientCountry());
					aofMap.put("email", clientUCCDetail.getClientEmail());
					aofMap.put("mobile", clientUCCDetail.getCmMobile());
					aofMap.put("occDetails", clientUCCDetail.getMasterTransactOccupationCode().getDetails());
					aofMap.put("bankAccType", clientUCCDetail.getMasterTransactAccountType1().getDetails());
					aofMap.put("BankAccNo", clientUCCDetail.getClientAccNo1());
					aofMap.put("ifsc", clientUCCDetail.getClientIfscCode1());

					ClientCKYCDetail clientCKYCDetail = clientKYCRepository.findByClientCode(clientCode);
					if (clientCKYCDetail != null) {
						aofMap.put("kyc1", clientCKYCDetail.getCkycNumberFirst().toString());
					}
					ClassLoader loader = getClass().getClassLoader();
					File targetFile = null;
					File destinationFile = null;
					if (loader.getResource("AOFFormFresh.pdf").getFile() != null) {
						targetFile = new File(loader.getResource("AOFFormFresh.pdf").getFile());
//						targetFile = new File(loader.getResource("NewAOF1.pdf").getFile());
					}
					if (loader.getResource("NewAOF.pdf").getFile() != null) {
						destinationFile = new File(loader.getResource("NewAOF.pdf").getFile());
					}
					if (targetFile != null && destinationFile != null) {
						pdfReplacer.replaceAcroFieldsByName(targetFile.getAbsolutePath(), destinationFile.getAbsolutePath(), aofMap);
					}
					status = true;
				}

			} catch (Exception e) {

			}
		} else {
			
			ClientMandateRegistration mandate = clientMandateRepository.findByMandateId(mandateId);
			if (mandate != null) {
				PDFReplacer pdfReplacer = new PDFReplacer();
				pdfReplacer.showAcroFields("NACHMandateN.pdf");

				Map<String,String> nachMap = new HashMap<String, String>();
				nachMap.put("UMRN", "12345");
				nachMap.put("sponsorBankCode", MFTransactConstant.NACH_FORM_SPONSOR_BANK_CODE);
				nachMap.put("utilityCode", MFTransactConstant.NACH_FORM_UTILITY_CODE);
//				nachMap.put("bseLimited", MFTransactConstant.NACH_FORM_EXCHANGE_NAME);
				
				nachMap.put("bankAccountNumber", mandate.getAccountNo());
				nachMap.put("withBank", "");
				nachMap.put("amountOfRupeesWords", convert(Long.parseLong(mandate.getAmount())));
				nachMap.put("amount", mandate.getAmount());
				nachMap.put("mandateReferenceNo", mandateId);
				nachMap.put("phone", clientUCCDetail.getCmMobile());
				nachMap.put("uniqueClientCode", clientUCCDetail.getClientCode());
				nachMap.put("email",clientUCCDetail.getClientEmail());

				if (mandate.getIfscCode() == null) {
					nachMap.put("IFSC", "");
				} else {
					nachMap.put("IFSC", mandate.getIfscCode());
				}
				nachMap.put("MICR", "");
				
				SimpleDateFormat sdfMandate = new SimpleDateFormat("dd/MM/yyyy");
				String startDate = sdfMandate.format(mandate.getStartDate());
				String [] arr = startDate.split("/");
				String startDay = arr[0];
				if(startDay.length() == 1) {
					startDay = "0" + startDay;
				}
				String startMonth = arr[1];
				if (startMonth.length() == 1) {
					startMonth = "0" + startMonth;
				}
				String startYear = arr[2];
				String endDate = sdfMandate.format(mandate.getEndDate());
				String [] arrEnd = endDate.split("/");
				String endDay = arrEnd[0];
				if(endDay.length() == 1) {
					endDay = "0" + endDay;
				}
				String endMonth = arrEnd[1];
				if (endMonth.length() == 1) {
					endMonth = "0" + endMonth;
				}
				String endYear = arrEnd[2];
				
				String todayDate = sdfMandate.format(new Date());
				String [] arrToday = todayDate.split("/");
				String todayDay = arrToday[0];
				if(todayDay.length() == 1) {
					todayDay = "0" + todayDay;
				}
				String todayMonth = arrToday[1];
				if (todayMonth.length() == 1) {
					todayMonth = "0" + todayMonth;
				}
				String todayYear = arrToday[2];
				
				
				nachMap.put("fromD", startDay.substring(0,1));
				nachMap.put("fromDD", startDay.substring(1,2));
				nachMap.put("fromM", startMonth.substring(0, 1));
				nachMap.put("fromMM", startMonth.substring(1, 2));
				nachMap.put("fromY", startYear.substring(0,1));
				nachMap.put("fromYY", startYear.substring(1,2));
				nachMap.put("fromYYY", startYear.substring(2,3));
				nachMap.put("fromYYYY", startYear.substring(3,4));
				
				nachMap.put("toD", endDay.substring(0,1));
				nachMap.put("toDD", endDay.substring(1,2));
				nachMap.put("toM", endMonth.substring(0, 1));
				nachMap.put("toMM", endMonth.substring(1, 2));
				nachMap.put("toY", endYear.substring(0,1));
				nachMap.put("toYY", endYear.substring(1,2));
				nachMap.put("toYYY", endYear.substring(2,3));
				nachMap.put("toYYYY", endYear.substring(3,4));
				
				nachMap.put("D", todayDate.substring(0,1));
				nachMap.put("DD", todayDate.substring(1,2));
				nachMap.put("M", todayMonth.substring(0, 1));
				nachMap.put("MM", todayMonth.substring(1, 2));
				nachMap.put("Y", todayYear.substring(0,1));
				nachMap.put("YY", todayYear.substring(1,2));
				nachMap.put("YYY", todayYear.substring(2,3));
				nachMap.put("YYYY", todayYear.substring(3,4));

				ClassLoader loader = getClass().getClassLoader();
				File targetFile = null;
				File destinationFile = null;
				if (loader.getResource("NACHMandateN.pdf").getFile() != null) {
					targetFile = new File(loader.getResource("NACHMandateN.pdf").getFile());
				}
				if (loader.getResource("NACHTemplateUpdated.pdf").getFile() != null) {
					destinationFile = new File(loader.getResource("NACHTemplateUpdated.pdf").getFile());
				}
				if (targetFile != null && destinationFile != null) {
//					pdfReplacer.replaceTextInPDF("NACHMandateN.pdf", "NACHTemplateUpdated.pdf", nachMap);
					pdfReplacer.replaceAcroFieldsByName(targetFile.getAbsolutePath(), destinationFile.getAbsolutePath(), nachMap);
				}
				
				status = true;
			}
			
		}

		

		try {
			/*ClassLoader classLoader = getClass().getClassLoader();
			File sourceFile = new File(classLoader.getResource("SampleAOF.pdf").getFile());
			PdfReader sourcePDF = new PdfReader(sourceFile);



			PdfDocument pdfDoc = new PdfDocument(sourcePDF, destPDF);
			PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
			Map<String, PdfFormField> fields = form.getFormFields();
			fields.get("Broker/Agent Code ARN").setValue("Debolina Bhattacharjee");
			pdfDoc.close();*/

			/*File destFile = new File("NewAOF.pdf");
			PdfWriter destPDF = new PdfWriter(destFile);
			PdfDocument pdf = new PdfDocument(destPDF);    
			Document document = new Document(pdf, PageSize.A4.rotate());
			document.setMargins(20, 20, 20, 20);
			PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
			PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
			Table firstTable = new Table(new float[]{4, 1, 3, 4, 3, 3, 3, 3, 1});
			firstTable.setWidth(UnitValue.createPercentValue(100));
			firstTable.addHeaderCell(
					new Cell().add("Broker/Agent Code ARN:"));
			firstTable.addHeaderCell(
					new Cell().add(("")));

			firstTable.addHeaderCell(
					new Cell().add("Sub-Broker"));
			firstTable.addHeaderCell(
					new Cell().add(("")));

			firstTable.addHeaderCell(
					new Cell().add("EUIN:"));
			firstTable.addHeaderCell(
					new Cell().add(("")));
			//			document.add(firstTable);

			/*document.add(new Paragraph("Unit Folder Information"));
			Table secondTable = new Table(new float[]{4, 1, 3, 4, 3, 3, 3, 3, 1});
			secondTable.setWidth(UnitValue.createPercentValue(100));
			secondTable.addHeaderCell(
					new Cell().add("Name Of First Applicant"));
			secondTable.addHeaderCell(
					new Cell().add(("")));

			secondTable.addCell(
	                new Cell().add(new Paragraph("Pan Number")));

			document.add(secondTable);*/

			/*Table secondTable = new Table(new float[]{4, 1, 3, 4, 3, 3, 3, 3, 1});
			process(secondTable, "Broker/Agent Code ARN;Sub-Broker;EUIN;First Applicant Name;Pan Number; DOB;Name Of Guardian; Pan", bold, true);
			process(secondTable, "54689; ; ;Mihir Gala;XXXXXX;10-05-1984; ; ", font, false);
			document.add(secondTable);

			document.add(new Paragraph("Contact Details"));
			Table thirdTable = new Table(new float[]{4, 1, 3, 4, 3, 3, 3, 3, 1});
			process(thirdTable, "Address1;Address2;Address3;City;PinCode;State;Country;Email", bold, true);
			process(thirdTable, "LBS Road;Ghatkopar; ;Mumbai;400086;Maharashtra;India;mihirgala@gmail.com", font, false);
			document.add(thirdTable);
			document.close();*/


		} catch (Exception e) {}

		return status;
	}

	public void process(Table table, String line, PdfFont font, boolean isHeader) {
		StringTokenizer tokenizer = new StringTokenizer(line, ";");
		while (tokenizer.hasMoreTokens()) {
			if (isHeader) {
				table.addHeaderCell(
						new Cell().add(
								new Paragraph(tokenizer.nextToken()).setFont(font)));
			} else {
				table.addCell(
						new Cell().add(
								new Paragraph(tokenizer.nextToken()).setFont(font)));
			}
		}
	}
	@Override
	public ClientUCCResultDTO saveAndUploadClientUCC(ClientUCCDraftModeDTO clientUCCDraftModeDTO) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		ClientUCCResultDTO result = new ClientUCCResultDTO();
		ClientUCCDetailsDraftMode clientUCCDetailsDraftMode = null;
		// First Part save in database
		try {

			clientUCCDetailsDraftMode = clientUCCDetailsDraftModeRepository.
					getByClientCodeAndSaveMode(clientUCCDraftModeDTO.getClientCode(), FinexaConstant.DRAFT_MODE);
			if (clientUCCDetailsDraftMode == null) {

				// Success Mode should be kept for Audit Log
				/*clientUCCDetailsDraftMode = clientUCCDetailsDraftModeRepository.
						getByClientCodeAndSaveMode(clientUCCDraftModeDTO.getClientCode(), FinexaConstant.SUCCESS_MODE);*/

				/*if (clientUCCDetailsDraftMode == null) {

					
				}*/
				clientUCCDetailsDraftMode = new ClientUCCDetailsDraftMode();
				clientUCCDetailsDraftMode.setClientCode(clientUCCDraftModeDTO.getClientCode());
				clientUCCDetailsDraftMode.setCreatedAt(new Date());
			}

			AdvisorUser advisorUser = advisorUserRepository.findOne(clientUCCDraftModeDTO.getAdvisorId());
			if(clientUCCDetailsDraftMode.getAdvisorUser() == null) {
				clientUCCDetailsDraftMode.setAdvisorUser(advisorUser);
			}


			ClientMaster cm = clientMasterRepository.findOne(clientUCCDraftModeDTO.getClientId());
			if(clientUCCDetailsDraftMode.getClientMaster() == null) {
				clientUCCDetailsDraftMode.setClientMaster(cm);
			}

			if(clientUCCDetailsDraftMode.getClientHolding() == null) {
				clientUCCDetailsDraftMode.setClientHolding(clientUCCDraftModeDTO.getHolding() == null ? null : Byte.parseByte(clientUCCDraftModeDTO.getHolding()));
			}

			/*byte holdingType = Byte.parseByte(jsonObjClientHolding.get("holding").toString());
			LookupTansactHoldingType lookupTansactHoldingType = lookupTransactHoldingTypeRepository.findOne(holdingType);
			clientUCCDetailsDraftMode.setLookupTansactHoldingType(lookupTansactHoldingType);
			 */
			/*MasterTransactTaxStatus masterTransactTaxStatus = masterTransactTaxStatusRepository.findOne(jsonObjClientHolding.get("clientTaxStatus").toString());
			clientUCCDetailsDraftMode.setMasterTransactTaxStatus(masterTransactTaxStatus);
			 */
			if(clientUCCDetailsDraftMode.getClientTaxStatus() == null || clientUCCDetailsDraftMode.getClientTaxStatus().equals("")) {
				clientUCCDetailsDraftMode.setClientTaxStatus(clientUCCDraftModeDTO.getTaxStatus());
			}

			/*MasterTransactOccupationCode  masterTransactOccupationCode = masterTransactOccupationCodeRepository.findOne(jsonObjClientHolding.get("clientOccupation").toString()); 
			clientUCCDetailsDraftMode.setMasterTransactOccupationCode(masterTransactOccupationCode);*/

			if(clientUCCDetailsDraftMode.getClientOccupationCode() == null || clientUCCDetailsDraftMode.getClientOccupationCode().equals("")) {
				clientUCCDetailsDraftMode.setClientOccupationCode(clientUCCDraftModeDTO.getOccupation());
			}
			if(clientUCCDetailsDraftMode.getClientAppName1() == null || clientUCCDetailsDraftMode.getClientAppName1().equals("")) {
				clientUCCDetailsDraftMode.setClientAppName1(clientUCCDraftModeDTO.getClientAppli());
			}
			if(clientUCCDetailsDraftMode.getClientAppName2() == null || clientUCCDetailsDraftMode.getClientAppName2().equals("")) {
				clientUCCDetailsDraftMode.setClientAppName2(clientUCCDraftModeDTO.getClientsecondAppli());
			}
			if(clientUCCDetailsDraftMode.getClientAppName3() == null || clientUCCDetailsDraftMode.getClientAppName3().equals("")) {
				clientUCCDetailsDraftMode.setClientAppName3(clientUCCDraftModeDTO.getClientThirdAppli());
			}
			if(clientUCCDetailsDraftMode.getClientDOB() == null || clientUCCDetailsDraftMode.getClientDOB().equals("")) {
				clientUCCDetailsDraftMode.setClientDOB(clientUCCDraftModeDTO.getDateOfBirth());
			}
			if(clientUCCDetailsDraftMode.getClientGender() == null || clientUCCDetailsDraftMode.getClientGender().equals("")) {
				clientUCCDetailsDraftMode.setClientGender(clientUCCDraftModeDTO.getGender());
			}
			if(clientUCCDetailsDraftMode.getClientGuardian() == null || clientUCCDetailsDraftMode.getClientGuardian().equals("")) {
				clientUCCDetailsDraftMode.setClientGuardian(clientUCCDraftModeDTO.getGuardianName());
			}


			// PAN Details
			if(clientUCCDetailsDraftMode.getClientPan() == null || clientUCCDetailsDraftMode.getClientPan().equals("")) {
				clientUCCDetailsDraftMode.setClientPan(clientUCCDraftModeDTO.getFirstAppliPan());
			}
			if(clientUCCDetailsDraftMode.getClientPan2() == null || clientUCCDetailsDraftMode.getClientPan2().equals("")) {
				clientUCCDetailsDraftMode.setClientPan2(clientUCCDraftModeDTO.getSecondApplicantPAN());
			}
			if(clientUCCDetailsDraftMode.getClientPan3() == null || clientUCCDetailsDraftMode.getClientPan3().equals("")) {
				clientUCCDetailsDraftMode.setClientPan3(clientUCCDraftModeDTO.getThirdApplicantPAN());
			}
			if(clientUCCDetailsDraftMode.getClientGuardianPan() == null || clientUCCDetailsDraftMode.getClientGuardianPan().equals("")) {
				clientUCCDetailsDraftMode.setClientGuardianPan(clientUCCDraftModeDTO.getGuardianPAN());
			}

			// Type Details
			/*System.out.println("jsonClientTaxString" + jsonClientTaxString);
			org.json.simple.JSONObject jsonObjClientTax = (org.json.simple.JSONObject) parser.parse(jsonClientTaxString);
			byte clientType = Byte.parseByte((jsonObjClientTax.get("clientDepositoryDetails").toString()));

			 */			
			if (clientUCCDetailsDraftMode.getClientType() == null) {
				clientUCCDetailsDraftMode.setClientType(clientUCCDraftModeDTO.getDepositoryDetails() == null ? null : Byte.parseByte(clientUCCDraftModeDTO.getDepositoryDetails()));
			}

			/*LookupTransactUCCClientType lookupTransactUCCClientType = lookupTransactUCCClientTypeRepository.findOne(clientType);
			clientUCCDetailsDraftMode.setLookupTransactUccclientType(lookupTransactUCCClientType);
			 */

			if(clientUCCDetailsDraftMode.getClientDefaultDP() == null) {
				clientUCCDetailsDraftMode.setClientDefaultDP((clientUCCDraftModeDTO.getDepositoryName() == null || clientUCCDraftModeDTO.getDepositoryName().equals("")) ? null : Byte.parseByte(clientUCCDraftModeDTO.getDepositoryName()));
			}
			/*if (lookupTransactUCCClientType.getValue().equals("D")) { // for Demat
				LookupTransactUCCClientDefaultDP lookupTransactUCCClientDefaultDP = LookupTransactUCCClientDefaultDPRepository.findOne(Byte.parseByte(jsonObjClientTax.get("clientDepositoryDetails").toString()));
				clientUCCDetailsDraftMode.setLookupTransactUccclientDefaultDp(lookupTransactUCCClientDefaultDP);
			}*/

			/*MasterTransactDivPayMode masterTransactDivPayMode = masterTransactDivPayModeRepository.findOne(jsonObjClientTax.get("clientDivPayMode").toString());
			clientUCCDetailsDraftMode.setMasterTransactDivPayMode(masterTransactDivPayMode);*/
			if(clientUCCDetailsDraftMode.getClientDivPayMode() == null || clientUCCDetailsDraftMode.getClientDivPayMode().equals("")) {
				clientUCCDetailsDraftMode.setClientDivPayMode(clientUCCDraftModeDTO.getDividendPaymentMode());
			}

			/*MasterTransactCommunicationMode masterTransactCommunicationMode = masterTransactCommunicationModeRepository.findOne(jsonObjClientTax.get("clientCommunicationMode").toString());
			clientUCCDetailsDraftMode.setMasterTransactCommunicationMode(masterTransactCommunicationMode);*/
			if (clientUCCDetailsDraftMode.getClientCommMode() == null || clientUCCDetailsDraftMode.getClientCommMode().equals("")) {
				clientUCCDetailsDraftMode.setClientCommMode(clientUCCDraftModeDTO.getCommunicationMode());
			}

			// Bank Details

			/*MasterTransactAccountType masterTransactAccountType= masterTransactAccountTypeRepository.findOne(jsonObjClientBank.get("accountType").toString());
			System.out.println("masterTransactAccountType" + masterTransactAccountType.getCode());
			 */	
			if(clientUCCDetailsDraftMode.getClientAccType1() == null || clientUCCDetailsDraftMode.getClientAccType1().equals("")) {
				clientUCCDetailsDraftMode.setClientAccType1(clientUCCDraftModeDTO.getAccountType());
			}
			if(clientUCCDetailsDraftMode.getClientAccNo1() == null || clientUCCDetailsDraftMode.getClientAccNo1().equals("")) {
				clientUCCDetailsDraftMode.setClientAccNo1(clientUCCDraftModeDTO.getBankAccountNumber());
			}
			if(clientUCCDetailsDraftMode.getClientIfscCode1() == null || clientUCCDetailsDraftMode.getClientIfscCode1().equals("")) {
				clientUCCDetailsDraftMode.setClientIfscCode1(clientUCCDraftModeDTO.getIfsccode());
			}
			if(clientUCCDetailsDraftMode.getClientMicrNo1() == null || clientUCCDetailsDraftMode.getClientMicrNo1().equals("")) {
				clientUCCDetailsDraftMode.setClientMicrNo1(clientUCCDraftModeDTO.getMicrCode());
			}
			if (clientUCCDetailsDraftMode.getDefaultBankFlag1() == null || clientUCCDetailsDraftMode.getDefaultBankFlag1().equals("")) {
				clientUCCDetailsDraftMode.setDefaultBankFlag1(clientUCCDraftModeDTO.getDefaultBankFlag());
			}

			// Account 2
			if(clientUCCDetailsDraftMode.getClientAccType2() == null || clientUCCDetailsDraftMode.getClientAccType2().equals("")) {
				clientUCCDetailsDraftMode.setClientAccType2(clientUCCDraftModeDTO.getAccountType2());
			}
			if(clientUCCDetailsDraftMode.getClientAccNo2() == null || clientUCCDetailsDraftMode.getClientAccNo2().equals("")) {
				clientUCCDetailsDraftMode.setClientAccNo2(clientUCCDraftModeDTO.getBankAccountNumber2());
			}
			if(clientUCCDetailsDraftMode.getClientIfscCode2() == null || clientUCCDetailsDraftMode.getClientIfscCode2().equals("")) {
				clientUCCDetailsDraftMode.setClientIfscCode2(clientUCCDraftModeDTO.getIfsccode2());
			}
			if(clientUCCDetailsDraftMode.getClientMicrNo2() == null || clientUCCDetailsDraftMode.getClientMicrNo2().equals("")) {
				clientUCCDetailsDraftMode.setClientMicrNo2(clientUCCDraftModeDTO.getMicrCode2());
			}
			if (clientUCCDetailsDraftMode.getDefaultBankFlag2() == null || clientUCCDetailsDraftMode.getDefaultBankFlag2().equals("")) {
				clientUCCDetailsDraftMode.setDefaultBankFlag2(clientUCCDraftModeDTO.getDefaultBankFlag2());
			}

			// Account 3
			if(clientUCCDetailsDraftMode.getClientAccType3() == null || clientUCCDetailsDraftMode.getClientAccType3().equals("")) {
				clientUCCDetailsDraftMode.setClientAccType3(clientUCCDraftModeDTO.getAccountType3());
			}
			if(clientUCCDetailsDraftMode.getClientAccNo3() == null || clientUCCDetailsDraftMode.getClientAccNo3().equals("")) {
				clientUCCDetailsDraftMode.setClientAccNo3(clientUCCDraftModeDTO.getBankAccountNumber3());
			}
			if(clientUCCDetailsDraftMode.getClientIfscCode3() == null || clientUCCDetailsDraftMode.getClientIfscCode3().equals("")) {
				clientUCCDetailsDraftMode.setClientIfscCode3(clientUCCDraftModeDTO.getIfsccode3());
			}
			if(clientUCCDetailsDraftMode.getClientMicrNo3() == null || clientUCCDetailsDraftMode.getClientMicrNo3().equals("")) {
				clientUCCDetailsDraftMode.setClientMicrNo3(clientUCCDraftModeDTO.getMicrCode3());
			}
			if (clientUCCDetailsDraftMode.getDefaultBankFlag3() == null || clientUCCDetailsDraftMode.getDefaultBankFlag3().equals("")) {
				clientUCCDetailsDraftMode.setDefaultBankFlag3(clientUCCDraftModeDTO.getDefaultBankFlag3());
			}

			// Account 4
			if(clientUCCDetailsDraftMode.getClientAccType4() == null || clientUCCDetailsDraftMode.getClientAccType4().equals("")) {
				clientUCCDetailsDraftMode.setClientAccType4(clientUCCDraftModeDTO.getAccountType4());
			}
			if(clientUCCDetailsDraftMode.getClientAccNo4() == null || clientUCCDetailsDraftMode.getClientAccNo4().equals("")) {
				clientUCCDetailsDraftMode.setClientAccNo4(clientUCCDraftModeDTO.getBankAccountNumber4());
			}
			if(clientUCCDetailsDraftMode.getClientIfscCode4() == null || clientUCCDetailsDraftMode.getClientIfscCode4().equals("")) {
				clientUCCDetailsDraftMode.setClientIfscCode4(clientUCCDraftModeDTO.getIfsccode4());
			}
			if(clientUCCDetailsDraftMode.getClientMicrNo4() == null || clientUCCDetailsDraftMode.getClientMicrNo4().equals("")) {
				clientUCCDetailsDraftMode.setClientMicrNo4(clientUCCDraftModeDTO.getMicrCode4());
			}
			if (clientUCCDetailsDraftMode.getDefaultBankFlag4() == null || clientUCCDetailsDraftMode.getDefaultBankFlag4().equals("")) {
				clientUCCDetailsDraftMode.setDefaultBankFlag4(clientUCCDraftModeDTO.getDefaultBankFlag4());
			}

			// Account 5
			if(clientUCCDetailsDraftMode.getClientAccType5() == null || clientUCCDetailsDraftMode.getClientAccType5().equals("")) {
				clientUCCDetailsDraftMode.setClientAccType5(clientUCCDraftModeDTO.getAccountType5());
			}
			if(clientUCCDetailsDraftMode.getClientAccNo5() == null || clientUCCDetailsDraftMode.getClientAccNo5().equals("")) {
				clientUCCDetailsDraftMode.setClientAccNo5(clientUCCDraftModeDTO.getBankAccountNumber5());
			}
			if(clientUCCDetailsDraftMode.getClientIfscCode5() == null || clientUCCDetailsDraftMode.getClientIfscCode5().equals("")) {
				clientUCCDetailsDraftMode.setClientIfscCode5(clientUCCDraftModeDTO.getIfsccode5());
			}
			if(clientUCCDetailsDraftMode.getClientMicrNo5() == null || clientUCCDetailsDraftMode.getClientMicrNo5().equals("")) {
				clientUCCDetailsDraftMode.setClientMicrNo5(clientUCCDraftModeDTO.getMicrCode5());
			}
			if (clientUCCDetailsDraftMode.getDefaultBankFlag5() == null || clientUCCDetailsDraftMode.getDefaultBankFlag5().equals("")) {
				clientUCCDetailsDraftMode.setDefaultBankFlag5(clientUCCDraftModeDTO.getDefaultBankFlag5());
			}


			//Contact

			if(clientUCCDetailsDraftMode.getClientAdd1() == null || clientUCCDetailsDraftMode.getClientAdd1().equals("")) {
				clientUCCDetailsDraftMode.setClientAdd1(clientUCCDraftModeDTO.getAddress());
			}
			if(clientUCCDetailsDraftMode.getClientAdd2() == null || clientUCCDetailsDraftMode.getClientAdd2().equals("")) {
				clientUCCDetailsDraftMode.setClientAdd2(clientUCCDraftModeDTO.getAddress2());
			}
			if(clientUCCDetailsDraftMode.getClientAdd3() == null || clientUCCDetailsDraftMode.getClientAdd3().equals("")) {
				clientUCCDetailsDraftMode.setClientAdd3(clientUCCDraftModeDTO.getAddress3());
			}
			if (clientUCCDetailsDraftMode.getClientCity() == null || clientUCCDetailsDraftMode.getClientCity().equals("")) {
				clientUCCDetailsDraftMode.setClientCity(clientUCCDraftModeDTO.getCity());
			}
			/*String stateCode = jsonObjClientContact.get("clientState").toString();
			MasterTransactStateCode masterTransactStateCode = masterTransactStateCodeRepository.findOne(stateCode);
			 */

			if (clientUCCDetailsDraftMode.getClientState() == null || clientUCCDetailsDraftMode.getClientState().equals("")) {
				clientUCCDetailsDraftMode.setClientState(clientUCCDraftModeDTO.getState());
			}
			if(clientUCCDetailsDraftMode.getClientCountry() == null || clientUCCDetailsDraftMode.getClientCountry().equals("")) {
				clientUCCDetailsDraftMode.setClientCountry(clientUCCDraftModeDTO.getCountry());
			}
			if(clientUCCDetailsDraftMode.getClientPincode() == null || clientUCCDetailsDraftMode.getClientPincode().equals("")) {
				clientUCCDetailsDraftMode.setClientPincode(clientUCCDraftModeDTO.getPincode());
			}
			if(clientUCCDetailsDraftMode.getClientEmail() == null || clientUCCDetailsDraftMode.getClientEmail().equals("")) {
				clientUCCDetailsDraftMode.setClientEmail(clientUCCDraftModeDTO.getEmailId());
			}
			if (clientUCCDetailsDraftMode.getCmMobile() == null || clientUCCDetailsDraftMode.getCmMobile().equals("")) {
				clientUCCDetailsDraftMode.setCmMobile(clientUCCDraftModeDTO.getMobile());
			}
			clientUCCDetailsDraftMode.setSaveMode(FinexaConstant.DRAFT_MODE);


			clientUCCDetailsDraftMode = clientUCCDetailsDraftModeRepository.save(clientUCCDetailsDraftMode);
			if (clientUCCDetailsDraftMode != null) {
				result.setOptionalParam(clientUCCDetailsDraftMode.getId());
				result.setStatusCode(com.finlabs.finexa.util.FinexaConstant.RETURN_VAL_SUCCESS);
				result.setMessage("Successfully Saved");
				result.setStatus(true);
			} else {
				result.setStatusCode(com.finlabs.finexa.util.FinexaConstant.RETURN_VAL_ERROR);
				result.setStatus(false);
			}

			// store ckyc data in draft mode

			if(clientUCCDraftModeDTO.getcKYC() != null || clientUCCDraftModeDTO.getcKYC().equals("")) {
				ClientCKYCDetail clientCKYCDetail = null;

				clientCKYCDetail = clientKYCRepository.getByClientCodeAndSaveMode(clientUCCDraftModeDTO.getClientCode(), FinexaConstant.DRAFT_MODE);
				if (clientCKYCDetail == null) {
					clientCKYCDetail = clientKYCRepository.
							getByClientCodeAndSaveMode(clientUCCDraftModeDTO.getClientCode(), 
									FinexaConstant.SUCCESS_MODE);

					if (clientCKYCDetail == null) {
						clientCKYCDetail = new ClientCKYCDetail();
						clientCKYCDetail.setClientCode(clientUCCDetailsDraftMode.getClientCode());
						clientCKYCDetail.setAdvisorUser(advisorUser);
						clientCKYCDetail.setClientMaster(cm);
						clientCKYCDetail.setCreatedAt(new Date());
					}
				}

				if (clientCKYCDetail.getClientPan() == null || clientCKYCDetail.getClientPan().equals("")) {
					clientCKYCDetail.setClientPan((clientUCCDetailsDraftMode.getClientPan()));
				}

				BigInteger ckycNumberFirst = new BigInteger((clientUCCDraftModeDTO.getcKYCFirstApplicant()));
				clientCKYCDetail.setCkycNumberFirst(ckycNumberFirst);

				if (clientUCCDraftModeDTO.getcKYCSecondApplicant() != null && !clientUCCDraftModeDTO.getcKYCSecondApplicant().equals("")) {
					BigInteger ckycNumberSecond = new BigInteger((clientUCCDraftModeDTO.getcKYCSecondApplicant()));
					clientCKYCDetail.setCkycNumberSecond(ckycNumberSecond);
					clientCKYCDetail.setJointHolderDOB1(clientUCCDraftModeDTO.getJointHolderDOB1());
				}

				if (clientUCCDraftModeDTO.getcKYCThirdApplicant() != null && !clientUCCDraftModeDTO.getcKYCThirdApplicant().toString().equals("")) {
					BigInteger ckycNumberThird = new BigInteger((clientUCCDraftModeDTO.getcKYCThirdApplicant().toString()));
					clientCKYCDetail.setCkycNumberThird(ckycNumberThird);
					clientCKYCDetail.setJointHolderDOB2(clientUCCDraftModeDTO.getJointHolderDOB2());
				}
				if (clientUCCDraftModeDTO.getCkycGuardian() != null && !clientUCCDraftModeDTO.getCkycGuardian().toString().equals("")) {
					BigInteger ckycNumberGuardian = new BigInteger((clientUCCDraftModeDTO.getCkycGuardian().toString()));
					clientCKYCDetail.setCkycGuardian(ckycNumberGuardian);
					clientCKYCDetail.setGuardianDOB(clientUCCDraftModeDTO.getGuardianDOB());
				}

				if (clientUCCDraftModeDTO.getkYCFirstApplicant() != null && !clientUCCDraftModeDTO.getkYCFirstApplicant().equals("")) {
					byte kycTypeFirst = Byte.parseByte((clientUCCDraftModeDTO.getkYCFirstApplicant()));
					LookupTransactKYCType lookupTransactKYCType = lookupTransactKTCTypeRepository.findOne(kycTypeFirst);
					clientCKYCDetail.setLookupTransactKyctype1(lookupTransactKYCType);

				}

				if (clientUCCDraftModeDTO.getkYCTypeSecondApplicant() != null && !clientUCCDraftModeDTO.getkYCTypeSecondApplicant().equals("")) {
					byte kycTypeSecond = Byte.parseByte((clientUCCDraftModeDTO.getkYCTypeSecondApplicant().toString()));
					LookupTransactKYCType lookupTransactKYCTypeFirst = lookupTransactKTCTypeRepository.findOne(kycTypeSecond);
					clientCKYCDetail.setLookupTransactKyctype2(lookupTransactKYCTypeFirst);

				}
				if (clientUCCDraftModeDTO.getkYCThirdApplicant() != null && !clientUCCDraftModeDTO.getkYCThirdApplicant().equals("")) {
					byte kycTypeThird = Byte.parseByte((clientUCCDraftModeDTO.getkYCThirdApplicant().toString()));
					LookupTransactKYCType lookupTransactKYCTypeThird = lookupTransactKTCTypeRepository.findOne(kycTypeThird);
					clientCKYCDetail.setLookupTransactKyctype3(lookupTransactKYCTypeThird);
				}

				if (clientUCCDraftModeDTO.getKycTypeGuardian()!= null && !clientUCCDraftModeDTO.getKycTypeGuardian().equals("")) {
					byte kycTypeGuardian = Byte.parseByte((clientUCCDraftModeDTO.getKycTypeGuardian().toString()));
					LookupTransactKYCType lookupTransactKYCTypeGuardian = lookupTransactKTCTypeRepository.findOne(kycTypeGuardian);
					clientCKYCDetail.setLookupTransactKyctype4(lookupTransactKYCTypeGuardian);
				}

				clientCKYCDetail.setSaveMode(FinexaConstant.DRAFT_MODE);

				clientCKYCDetail = clientKYCRepository.save(clientCKYCDetail);
				if (clientCKYCDetail != null) {
					result.setOptionalParamCKYC(clientCKYCDetail.getId());
					result.setStatusCode(com.finlabs.finexa.util.FinexaConstant.RETURN_VAL_SUCCESS);
					result.setMessage("Successfully Saved");
					result.setStatus(true);
				} else {
					result.setStatusCode(com.finlabs.finexa.util.FinexaConstant.RETURN_VAL_ERROR);
					result.setStatus(false);
				}
			}

			//Fire BSE API
			{/*

				String pipeSeparatedString = "";
				pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getLookupTansactHoldingType().getValue(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getMasterTransactTaxStatus().getCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getMasterTransactOccupationCode().getCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientAppName1(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientAppName2(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientAppName3(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientDOB(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientGender(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientGuardian(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientPan(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientNominee(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientNomineeRelation(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientGuardianPan(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getLookupTransactUccclientType().getValue(), pipeSeparatedString);
				if (clientUCCDetail.getLookupTransactUccclientDefaultDp() == null) {
					pipeSeparatedString = joinPipeValues("", pipeSeparatedString);
				} else {
					pipeSeparatedString = joinPipeValues(clientUCCDetail.getLookupTransactUccclientDefaultDp().getValue(), pipeSeparatedString);

				}
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientCDSLDPID(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientCDSLCLTID(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientNSDLDPID(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientNSDLCLTID(), pipeSeparatedString);

				pipeSeparatedString = joinPipeValues(clientUCCDetail.getMasterTransactAccountType1().getCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientAccNo1(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientMicrNo1(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientIfscCode1(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getDefaultBankFlag1(), pipeSeparatedString);


				if (clientUCCDetail.getMasterTransactAccountType2() == null) {
					pipeSeparatedString = joinPipeValues("", pipeSeparatedString);
				} else {
					pipeSeparatedString = joinPipeValues(clientUCCDetail.getMasterTransactAccountType2().getCode(), pipeSeparatedString);

				}
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientAccNo2(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientMicrNo2(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientIfscCode2(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getDefaultBankFlag2(), pipeSeparatedString);

				if (clientUCCDetail.getMasterTransactAccountType3() == null) {
					pipeSeparatedString = joinPipeValues("", pipeSeparatedString);
				} else {
					pipeSeparatedString = joinPipeValues(clientUCCDetail.getMasterTransactAccountType3().getCode(), pipeSeparatedString);

				}
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientAccNo3(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientMicrNo3(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientIfscCode3(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getDefaultBankFlag3(), pipeSeparatedString);


				if (clientUCCDetail.getMasterTransactAccountType4() == null) {
					pipeSeparatedString = joinPipeValues("", pipeSeparatedString);
				} else {
					pipeSeparatedString = joinPipeValues(clientUCCDetail.getMasterTransactAccountType4().getCode(), pipeSeparatedString);

				}
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientAccNo4(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientMicrNo4(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientIfscCode4(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getDefaultBankFlag4(), pipeSeparatedString);

				if (clientUCCDetail.getMasterTransactAccountType5() == null) {
					pipeSeparatedString = joinPipeValues("", pipeSeparatedString);
				} else {
					pipeSeparatedString = joinPipeValues(clientUCCDetail.getMasterTransactAccountType5().getCode(), pipeSeparatedString);

				}
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientAccNo5(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientMicrNo5(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientIfscCode5(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getDefaultBankFlag5(), pipeSeparatedString);

				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientChequeName5(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientAdd1(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientAdd2(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientAdd3(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientCity(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getMasterTransactStateCode().getCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientPincode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientCountry(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientResiPhone(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientResiFax(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientOfficePhone(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientOfficeFax(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientEmail(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getMasterTransactCommunicationMode().getCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getMasterTransactDivPayMode().getCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientPan2(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getClientPan3(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getMapinNo(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getCmForAdd1(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getCmForAdd2(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getCmForAdd3(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getCmForCity(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getCmForPinCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getCmForState(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getCmForCountry(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getCmForResiPhone(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getCmForResiFax(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getCmForOffPhone(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getCmForOffFax(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientUCCDetail.getCmMobile(), pipeSeparatedString);

				System.out.println(pipeSeparatedString);
				pipeSeparatedString = pipeSeparatedString.substring(0,pipeSeparatedString.length()-1);
				System.out.println(pipeSeparatedString);
				MFUPLOADSoapService mFUPLOADSoapService = new MFUPLOADSoapService();

				String bseUserId = clientUCCDetail.getAdvisorUser().getBseUsername();
				String bseMemberId = clientUCCDetail.getAdvisorUser().getBseMemberId();
				String bsePassword = clientUCCDetail.getAdvisorUser().getBsePassword();
				String passKey = autoGeneratePassKey();

				ClientUCCResultDTO clientUCCResultDTO = mFUPLOADSoapService.authenticateMFUploadService
						(bseUserId, bseMemberId, bsePassword, passKey);

				if (clientUCCResultDTO.isStatus()) {
					// Bse call to authenticate password is successful,
					//Now upload the ccCreationDetails
					clientUCCResultDTO = mFUPLOADSoapService.fireMFAPIRequest(BSEConstant.MFAPI_UCC_MFD, bseUserId, clientUCCResultDTO.getMessage(), pipeSeparatedString);
					if (clientUCCResultDTO.isStatus()) {
						// ccCreation with BSE Successful. Save the result in table
						ClientUCCDetail clientUCCDetailAfterSave = clientUCCRepository.save(clientUCCDetail);
						if (clientUCCDetailAfterSave == null) {
							result.setStatus(false);
							result.setStatusCode(FinexaErrorCodes.ERROR_UCC_SAVE);
							result.setMessage("Unable to save record in Finexa");
						} else {
							// ccCreation successful. Now fire CKYC Details

							// Filling Up CKYC Details
							ClientCKYCDetail clientCKYCDetail = new ClientCKYCDetail();
							clientCKYCDetail.setClientCode(jsonObjClientHolding.get("clientCode").toString());
							clientCKYCDetail.setClientPan((jsonObjClientPan.get("clientPan").toString()));
							BigInteger ckycNumberFirst = new BigInteger((jsonObjClientTax.get("firstKycNumber").toString()));
							clientCKYCDetail.setCkycNumberFirst(ckycNumberFirst);

							if (jsonObjClientTax.get("secondKycNUmber") != null && !jsonObjClientTax.get("secondKycNUmber").toString().equals("")) {
								BigInteger ckycNumberSecond = new BigInteger((jsonObjClientTax.get("secondKycNUmber").toString()));
								clientCKYCDetail.setCkycNumberSecond(ckycNumberSecond);
							}

							if (jsonObjClientTax.get("thirdKycNUmber") != null && !jsonObjClientTax.get("thirdKycNUmber").toString().equals("")) {
								BigInteger ckycNumberSecond = new BigInteger((jsonObjClientTax.get("thirdKycNUmber").toString()));
								clientCKYCDetail.setCkycNumberThird(ckycNumberSecond);
							}

							byte kycTypeFirst = Byte.parseByte((jsonObjClientTax.get("firstKycStatus").toString()));
							LookupTransactKYCType lookupTransactKYCType = lookupTransactKTCTypeRepository.findOne(kycTypeFirst);
							clientCKYCDetail.setLookupTransactKyctype1(lookupTransactKYCType);

							if (jsonObjClientTax.get("secondKycStatus") != null && !jsonObjClientTax.get("secondKycStatus").toString().equals("")) {
								byte kycTypeSecond = Byte.parseByte((jsonObjClientTax.get("secondKycStatus").toString()));
								LookupTransactKYCType lookupTransactKYCTypeFirst = lookupTransactKTCTypeRepository.findOne(kycTypeSecond);
								clientCKYCDetail.setLookupTransactKyctype2(lookupTransactKYCTypeFirst);

							}

							if (jsonObjClientTax.get("thirdKycStatus") != null && !jsonObjClientTax.get("thirdKycStatus").toString().equals("")) {
								byte kycTypeThird = Byte.parseByte((jsonObjClientTax.get("thirdKycStatus").toString()));
								LookupTransactKYCType lookupTransactKYCTypeThird = lookupTransactKTCTypeRepository.findOne(kycTypeThird);
								clientCKYCDetail.setLookupTransactKyctype3(lookupTransactKYCTypeThird);
							}

							//fire CKYC Details 

							String pipeStringCKYC = "";
							pipeStringCKYC = joinPipeValues(clientCKYCDetail.getClientCode(), pipeStringCKYC);
							pipeStringCKYC = joinPipeValues(clientCKYCDetail.getClientPan(), pipeStringCKYC);
							pipeStringCKYC = joinPipeValues(""+clientCKYCDetail.getCkycNumberFirst(), pipeStringCKYC);
							pipeStringCKYC = joinPipeValues(""+clientCKYCDetail.getCkycNumberSecond(), pipeStringCKYC);
							pipeStringCKYC = joinPipeValues(""+clientCKYCDetail.getCkycNumberThird(), pipeStringCKYC);
							pipeStringCKYC = joinPipeValues(""+clientCKYCDetail.getCkycGuardian(), pipeStringCKYC);
							pipeStringCKYC = joinPipeValues(""+clientCKYCDetail.getJointHolderDOB1(), pipeStringCKYC);
							pipeStringCKYC = joinPipeValues(""+clientCKYCDetail.getJointHolderDOB2(), pipeStringCKYC);
							pipeStringCKYC = joinPipeValues(""+clientCKYCDetail.getGuardianDOB(), pipeStringCKYC);
							pipeStringCKYC = joinPipeValues(clientCKYCDetail.getLookupTransactKyctype1().getValue(), pipeStringCKYC);

							if (clientCKYCDetail.getLookupTransactKyctype2() == null) {
								pipeStringCKYC = joinPipeValues("", pipeStringCKYC);
							} else {
								pipeStringCKYC = joinPipeValues(clientCKYCDetail.getLookupTransactKyctype2().getValue(), pipeStringCKYC);

							}

							if (clientCKYCDetail.getLookupTransactKyctype3() == null) {
								pipeStringCKYC = joinPipeValues("", pipeStringCKYC);
							} else {
								pipeStringCKYC = joinPipeValues(clientCKYCDetail.getLookupTransactKyctype3().getValue(), pipeStringCKYC);

							}

							if (clientCKYCDetail.getLookupTransactKyctype4() == null) {
								pipeStringCKYC = joinPipeValues("", pipeStringCKYC);
							} else {
								pipeStringCKYC = joinPipeValues(clientCKYCDetail.getLookupTransactKyctype4().getValue(), pipeStringCKYC);

							}
							System.out.println("pipeStringCKYC" + pipeStringCKYC);
							pipeStringCKYC = pipeStringCKYC.substring(0, pipeStringCKYC.length() - 1);

							clientUCCResultDTO = mFUPLOADSoapService.authenticateMFUploadService
									(bseUserId, bseMemberId, bsePassword, passKey);

							if (clientUCCResultDTO.isStatus()) {
								// Bse call to authenticate password is successful,
								//Now upload the ccCreationDetails
								clientUCCResultDTO = mFUPLOADSoapService.fireMFAPIRequest(BSEConstant.MFAPI_CLIENT_CKYC_UPLOAD, bseUserId, clientUCCResultDTO.getMessage(), pipeStringCKYC);
								if (clientUCCResultDTO.isStatus()) {
									// ccCreation with BSE Successful. Save the result in table
									ClientCKYCDetail clientCKYCDetailAfterSave = clientKYCRepository.save(clientCKYCDetail);
									if (clientCKYCDetailAfterSave == null) {
										result.setStatus(false);
										result.setStatusCode(FinexaErrorCodes.ERROR_CKYC_SAVE);
										result.setMessage("Unable to save record in Finexa");
									}  else {
										return clientUCCResultDTO;
									}
								} else {
									return clientUCCResultDTO;
								}
							} else {
								return clientUCCResultDTO;
							}

						}
					} else {
						return clientUCCResultDTO;
					}
				} else {
					return clientUCCResultDTO;
				}
			 */}
		}catch (Exception e) {
			result.setStatusCode(com.finlabs.finexa.util.FinexaConstant.RETURN_VAL_ERROR);
			result.setStatus(false);
		}

		return result;

	}
	private String autoGeneratePassKey() {
		// TODO Auto-generated method stub
		String passKey = "8A655"; // For Test Purposes
		return passKey;
	}
	private String joinPipeValues(String value, String pipeString) {
		// TODO Auto-generated method stub
		if (value == null || value.equals("") || value.equals("null")) {
			pipeString = pipeString + "|";
		} else {
			pipeString = pipeString + value + "|";
		}
		return pipeString;
	}
	private String composePipeSeparatedString(ClientUCCDetail clientUCCDetailAfterSave) {
		// TODO Auto-generated method stub
		String pipeSeparated = "";

		return null;
	}
	@Override
	public List<ViewClientUCCDetailsDTO> getExistingUCC(int clientId) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<ViewClientUCCDetailsDTO> viewClientUCCDetailsDTOList = new ArrayList<>();
		try {

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			List<ClientUCCDetail> clientUCCDetailList = clientUCCRepository.findByclientMaster(cm);

			for (ClientUCCDetail obj : clientUCCDetailList) {
				ViewClientUCCDetailsDTO viewClientUCCDetailsDTO = mapper.map(obj, ViewClientUCCDetailsDTO.class);
				if (obj.getLookupTransactUccclientType().getValue().equals(FinexaConstant.PHYSICAL_MODE)) {
					viewClientUCCDetailsDTO.setPhysial(true);
				}else {
					viewClientUCCDetailsDTO.setPhysial(false);
				}
				ClientFatcaReport fatcaReport = clientFatcaRepository.getByClientCodeAndSaveMode(obj.getClientCode(), FinexaConstant.SUCCESS_MODE);
				if (fatcaReport != null) {
					viewClientUCCDetailsDTO.setFatcaStatus(true);
				} else {
					viewClientUCCDetailsDTO.setFatcaStatus(false);
				}

				List<ClientMandateRegistration> savedMandate = clientMandateRepository.getByClientCodeAndSaveMode(obj.getClientCode(), FinexaConstant.SUCCESS_MODE);
				if (savedMandate != null && savedMandate.size() > 0) {
					viewClientUCCDetailsDTO.setMandateStatus(true);
				} else {
					viewClientUCCDetailsDTO.setMandateStatus(false);
				}

				// get the AOF Status

				viewClientUCCDetailsDTOList.add(viewClientUCCDetailsDTO);
			}
			return viewClientUCCDetailsDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	@Override
	public ClientUCCResultDTO validateExistingUCC(ExistingClientUCCDTO existingClientUCCDTO)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		// check whether UCC is present in ClientUCCTable
		ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(existingClientUCCDTO.getClientCode());
		if (clientUCCDetail != null) {
			// this UCC is already present
			clientUCCResultDTO.setMessage(MFTransactConstant.UCC_ALREADY_PRESENT);
			clientUCCResultDTO.setStatusCode(MFTransactConstant.STATUS_CODE_SUCCESS);
			clientUCCResultDTO.setStatus(true);
		} else {
			// pattern of holding by PAN is already present or not
			
			if (existingClientUCCDTO.getAdvisorId() > 0) {
				AdvisorUser advUser = advisorUserRepository.findOne(existingClientUCCDTO.getAdvisorId());
				if (advUser != null) {
					if (advUser.getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {

						 if (existingClientUCCDTO.getFirstApplicantPan().equals("")) {
							 existingClientUCCDTO.setFirstApplicantPan(null);
						 }
						 if (existingClientUCCDTO.getSecondApplicantPan().equals("")) {
							 existingClientUCCDTO.setSecondApplicantPan(null);
						 }
						 if (existingClientUCCDTO.getThirdApplicantPan().equals("")) {
							 existingClientUCCDTO.setThirdApplicantPan(null);
						 }
						 if (existingClientUCCDTO.getGuardianPan().equals("")) {
							 existingClientUCCDTO.setGuardianPan(null);
						 }
						 	 
						 List<ClientUCCDetail> clientUCCDetailList = clientUCCRepository.findByAdvisorUserAndClientPanAndClientPan2AndClientPan3AndClientGuardianPan(
								 advUser, existingClientUCCDTO.getFirstApplicantPan(), existingClientUCCDTO.getSecondApplicantPan(), 
										 existingClientUCCDTO.getThirdApplicantPan(), existingClientUCCDTO.getGuardianPan());
						 
						 
						 if (clientUCCDetailList != null && clientUCCDetailList.size() > 0) {
							 clientUCCResultDTO.setMessage(MFTransactConstant.PATTERN_HOLDING_PRESENT);
							 clientUCCResultDTO.setStatusCode(MFTransactConstant.STATUS_CODE_SUCCESS);
							 clientUCCResultDTO.setStatus(true);
						 }
					}
				}
				
			}
		}
		/*if (clientUCCDetail == null) {
			// check whether record is present in Master File . If present transfer it to clientUCC Table
			if (existingClientUCCDTO.getFirstApplicantPan() != null) {
				try {
					List<MasterTransactClientUCCDetail> cmList = masterTransactClientUCCDetailsRepository.findByFirstApplicantPan(existingClientUCCDTO.getFirstApplicantPan());
					if (cmList != null && cmList.size() > 0) {
						// transfer first record from master to client
						clientUCCResultDTO.setMessage(MFTransactConstant.RECORD_TRANSFERRED_FROM_MASTER_TO_CLIENT);
						clientUCCResultDTO.setStatusCode(MFTransactConstant.STATUS_CODE_SUCCESS);
						clientUCCResultDTO.setStatus(true);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}*/
		return clientUCCResultDTO; 
	}
	@Override
	public ClientUCCResultDTO validateAndSaveClientFatca(ClientFatcaDTO clientFatcaDTO)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub

		ClientFatcaReport clientFatcaReport = mapper.map(clientFatcaDTO, ClientFatcaReport.class);
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String clientCode = clientFatcaDTO.getClientCode();
		ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(clientCode);

		if (clientUCCDetail != null) {
			clientFatcaReport.setAdvisorUser(clientUCCDetail.getAdvisorUser());
			clientFatcaReport.setClientMaster(clientUCCDetail.getClientMaster());

			MasterTransactIdentificationType masterTransactIdentificationType = masterTransactIdentificationTypeRepository.findOne(clientFatcaDTO.getId1Type());
			clientFatcaReport.setMasterTransactIdentificationType(masterTransactIdentificationType);

			MasterTransactTaxStatus masterTransactTaxStatus = masterTransactTaxStatusRepository.findOne(clientFatcaDTO.getTaxStatus());
			clientFatcaReport.setMasterTransactTaxStatus(masterTransactTaxStatus);

			MasterTransactAddressType addType = masterTransactFatcaAddressTypeRepository.findOne(clientFatcaDTO.getAddrType());
			clientFatcaReport.setMasterTransactAddressType(addType);

			MasterTransactCountryNationality countryNationality = masterTransactCountryNationalityRepository.findOne(clientFatcaDTO.getCoBirInc());
			clientFatcaReport.setMasterTransactCountryNationality1(countryNationality);


			MasterTransactCountryNationality countryNationality2 = masterTransactCountryNationalityRepository.findOne(clientFatcaDTO.getTaxRes1());
			clientFatcaReport.setMasterTransactCountryNationality2(countryNationality2);

			MasterTransactSourceOfWealth sourceOfWealth = masterTransactSourceOfWealthRepository.findOne(clientFatcaDTO.getSrceWealt());
			clientFatcaReport.setMasterTransactSourceOfWealth(sourceOfWealth);


			MasterTransactIncome income = masterTransactIncomeTypeRepository.findOne(clientFatcaDTO.getIncSlab());
			clientFatcaReport.setMasterTransactIncome(income);

			MasterTransactOccupationCode masterTransactOccupationCode = masterTransactOccupationCodeRepository.findOne(clientFatcaDTO.getOccCode());
			clientFatcaReport.setMasterTransactOccupationCode(masterTransactOccupationCode);

			if (clientFatcaDTO.getNewChange() == null || clientFatcaDTO.getNewChange().equals("")) {
				clientFatcaReport.setNewChange(null);
				clientFatcaDTO.setNewChange("N");
			}

			if (clientFatcaDTO.getUboDf() == null || clientFatcaDTO.getUboDf().equals("")) {
				clientFatcaReport.setUboDf(null);
				clientFatcaDTO.setUboDf("N");
			}

			if (clientFatcaDTO.getSdfFlag() == null || clientFatcaDTO.getSdfFlag().equals("")) {
				clientFatcaReport.setSdfFlag(null);

			}

			if (clientFatcaDTO.getUboAppl() == null || clientFatcaDTO.getUboAppl().equals("")) {
				clientFatcaReport.setUboAppl(null);
				clientFatcaDTO.setUboAppl("N");
			}
			ClientFatcaReport alreadySavedFatca = clientFatcaRepository.getByClientCodeAndSaveMode(clientFatcaReport.getClientCode(), FinexaConstant.SUCCESS_MODE);
			if (alreadySavedFatca != null) {
				clientFatcaReport.setId(alreadySavedFatca.getId());
				// set in save mode
			} else {
				alreadySavedFatca = clientFatcaRepository.getByClientCodeAndSaveMode(clientFatcaReport.getClientCode(), FinexaConstant.DRAFT_MODE);
				if (alreadySavedFatca != null) {
					clientFatcaReport.setId(alreadySavedFatca.getId());
					// set in save mode
				}
			}
			clientFatcaReport.setCreatedAt(new Date());
			clientFatcaReport.setSaveMode(FinexaConstant.DRAFT_MODE);

			try {
				clientFatcaReport = clientFatcaRepository.save(clientFatcaReport);
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
			}

			if (clientFatcaReport != null) {
				//composePipeSeparatedString

				String pipeSeparatedString = "";
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getPanRP(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getPekrn(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getInvName(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getDob(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getFrName(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getSpName(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getTaxStatus(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getDataSrc(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getAddrType(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getPoBirInc(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getCoBirInc(), pipeSeparatedString);
				// Tax Residency will be coutry of birth
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getCoBirInc(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getTpin1(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getId1Type(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getTaxRes2(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getTpin2(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getId2Type(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getTaxRes3(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getTpin3(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getId3Type(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getTaxRes4(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getTpin4(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getId4Type(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getSrceWealt(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getCorpServs(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getIncSlab(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getNetWorth(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getNwDate(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getPepFlag(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getOccCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getOccType(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getExempCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getFfiDrnfe(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getGiinNo(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getSprEntity(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getGiinNa(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getGiinExemc(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getNffeCatg(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getActNfeSc(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getNatureBus(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getRelListed(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getExchName(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboAppl(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboCount(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboName(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboPan(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboNation(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboAdd1(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboAdd2(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboAdd3(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboCity(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboPin(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboState(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboCntry(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboAddTy(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboCtr(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboTin(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboIdTy(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboCob(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboDob(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboGender(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboFrNam(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboOcc(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboOccTy(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboTel(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboMobile(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboHolPc(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getSdfFlag(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getUboDf(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getAadhaarRp(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getNewChange(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getLogName(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getFiller1(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientFatcaDTO.getFiller2(), pipeSeparatedString);

				System.out.println(pipeSeparatedString);
				pipeSeparatedString = pipeSeparatedString.substring(0,pipeSeparatedString.length()-1);
				System.out.println(pipeSeparatedString);

				String bseUserId = clientFatcaReport.getAdvisorUser().getBseUsername();
				String bseMemberId = clientFatcaReport.getAdvisorUser().getBseMemberId();
				String bsePassword = decrypt(clientFatcaReport.getAdvisorUser().getBsePassword());
				String passKey = autoGeneratePassKey();

				MFUPLOADSoapService mFUPLOADSoapService = new MFUPLOADSoapService();
				clientUCCResultDTO = mFUPLOADSoapService.authenticateMFUploadService
						(bseUserId, bseMemberId, bsePassword, passKey,clientFatcaReport.getAdvisorUser().getLookupTransactBseaccessMode().getId());

				if (clientUCCResultDTO.isStatus()) {
					// Bse call to authenticate password is successful,
					//Now upload the mandateDetails
					clientUCCResultDTO = mFUPLOADSoapService.fireMFAPIRequest(BSEConstant.MFAPI_FATCA_UPLOAD, bseUserId, clientUCCResultDTO.getMessage(), pipeSeparatedString, clientFatcaReport.getAdvisorUser().getLookupTransactBseaccessMode().getId());
					if (clientUCCResultDTO.isStatus()) {
						// ccCreation with BSE Successful. Save the result in table
						// set in save mode
						alreadySavedFatca = clientFatcaRepository.getByClientCodeAndSaveMode(clientFatcaReport.getClientCode(), FinexaConstant.DRAFT_MODE);
						if (alreadySavedFatca != null) {
							clientFatcaReport.setId(alreadySavedFatca.getId());
						}
						clientFatcaReport.setBseResponse(clientUCCResultDTO.getMessage());
						clientFatcaReport.setBseResponseCode("" + clientUCCResultDTO.getStatusCode());
						clientFatcaReport.setSaveMode(FinexaConstant.SUCCESS_MODE);
						clientFatcaReport = clientFatcaRepository.save(clientFatcaReport);
					} else {
						// set in error mode
						clientFatcaReport.setSaveMode(FinexaConstant.ERROR_MODE);
						clientFatcaReport = clientFatcaRepository.save(clientFatcaReport);
					}
				}
			}
		}
		return clientUCCResultDTO;

	}
	@Override
	public ClientUCCResultDTO validateAndSaveClientMandate(MandateDTO mandateDTO) throws FinexaBussinessException {
		// TODO Auto-generated method stub

		ClientMandateRegistration clientMandateRegistration = mapper.map(mandateDTO, ClientMandateRegistration.class);
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String clientCode = mandateDTO.getClientCode();
		ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(clientCode);

		if (clientUCCDetail != null) {
			clientMandateRegistration.setAdvisorUser(clientUCCDetail.getAdvisorUser());
			clientMandateRegistration.setClientMaster(clientUCCDetail.getClientMaster());
			String acType = "";
			String ifscCode = "";
			String micrCode = "";
			if (clientUCCDetail.getClientAccNo1() != null && clientUCCDetail.getClientAccNo1().equals(mandateDTO.getAccountNo())) {
				acType = clientUCCDetail.getMasterTransactAccountType1().getCode();
				clientMandateRegistration.setMasterTransactAccountType(clientUCCDetail.getMasterTransactAccountType1());
				clientMandateRegistration.setIfscCode(clientUCCDetail.getClientIfscCode1());
				clientMandateRegistration.setMicrCode(clientUCCDetail.getClientMicrNo1());
			}
			if (clientUCCDetail.getClientAccNo2() != null && clientUCCDetail.getClientAccNo2().equals(mandateDTO.getAccountNo())) {
				acType = clientUCCDetail.getMasterTransactAccountType1().getCode();
				clientMandateRegistration.setMasterTransactAccountType(clientUCCDetail.getMasterTransactAccountType2());
				clientMandateRegistration.setIfscCode(clientUCCDetail.getClientIfscCode2());
				clientMandateRegistration.setMicrCode(clientUCCDetail.getClientMicrNo2());
			}
			if (clientUCCDetail.getClientAccNo3() != null && clientUCCDetail.getClientAccNo3().equals(mandateDTO.getAccountNo())) {
				acType = clientUCCDetail.getMasterTransactAccountType1().getCode();
				clientMandateRegistration.setMasterTransactAccountType(clientUCCDetail.getMasterTransactAccountType3());
				clientMandateRegistration.setIfscCode(clientUCCDetail.getClientIfscCode3());
				clientMandateRegistration.setMicrCode(clientUCCDetail.getClientMicrNo3());
			}
			if (clientUCCDetail.getClientAccNo4() != null && clientUCCDetail.getClientAccNo4().equals(mandateDTO.getAccountNo())) {
				acType = clientUCCDetail.getMasterTransactAccountType1().getCode();
				clientMandateRegistration.setMasterTransactAccountType(clientUCCDetail.getMasterTransactAccountType4());
				clientMandateRegistration.setIfscCode(clientUCCDetail.getClientIfscCode1());
				clientMandateRegistration.setMicrCode(clientUCCDetail.getClientMicrNo1());
			}
			if (clientUCCDetail.getClientAccNo5() != null && clientUCCDetail.getClientAccNo5().equals(mandateDTO.getAccountNo())) {
				acType = clientUCCDetail.getMasterTransactAccountType1().getCode();
				clientMandateRegistration.setMasterTransactAccountType(clientUCCDetail.getMasterTransactAccountType5());
				clientMandateRegistration.setIfscCode(clientUCCDetail.getClientIfscCode5());
				clientMandateRegistration.setMicrCode(clientUCCDetail.getClientMicrNo5());
			}
			List<ClientMandateRegistration> alreadySavedMandate = clientMandateRepository.getByClientCodeAndSaveMode(mandateDTO.getClientCode(), FinexaConstant.DRAFT_MODE);
			if (alreadySavedMandate != null && alreadySavedMandate.size() > 0 && alreadySavedMandate.get(0) != null) {
				clientMandateRegistration.setId(alreadySavedMandate.get(0).getId());
			}
			clientMandateRegistration.setSaveMode(FinexaConstant.DRAFT_MODE);
			clientMandateRegistration.setCreatedAt(new Date());
			clientMandateRegistration = clientMandateRepository.save(clientMandateRegistration);
			if (clientMandateRegistration != null) {
				// compose pipe separated String
				String pipeSeparatedString = "";
				pipeSeparatedString = joinPipeValues(mandateDTO.getClientCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(mandateDTO.getAmount(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(mandateDTO.getMandateType(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(mandateDTO.getAccountNo(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(acType, pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientMandateRegistration.getIfscCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientMandateRegistration.getMicrCode(), pipeSeparatedString);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String startDate = sdf.format(mandateDTO.getStartDate());
				String endDate = sdf.format(mandateDTO.getEndDate());
				pipeSeparatedString = joinPipeValues(startDate, pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(endDate, pipeSeparatedString);

				System.out.println(pipeSeparatedString);
				pipeSeparatedString = pipeSeparatedString.substring(0,pipeSeparatedString.length()-1);
				System.out.println(pipeSeparatedString);

				String bseUserId = clientMandateRegistration.getAdvisorUser().getBseUsername();
				String bseMemberId = clientMandateRegistration.getAdvisorUser().getBseMemberId();
				String bsePassword = decrypt(clientMandateRegistration.getAdvisorUser().getBsePassword());
				String passKey = autoGeneratePassKey();

				MFUPLOADSoapService mFUPLOADSoapService = new MFUPLOADSoapService();
				clientUCCResultDTO = mFUPLOADSoapService.authenticateMFUploadService
						(bseUserId, bseMemberId, bsePassword, passKey, clientMandateRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId());

				if (clientUCCResultDTO.isStatus()) {
					// Bse call to authenticate password is successful,
					//Now upload the mandateDetails
					clientUCCResultDTO = mFUPLOADSoapService.fireMFAPIRequest(BSEConstant.MFAPI_MANDATE_REGISTRATION, bseUserId, clientUCCResultDTO.getMessage(), pipeSeparatedString, clientMandateRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId());
					if (clientUCCResultDTO.isStatus()) {
						// ccCreation with BSE Successful. Save the result in table
						alreadySavedMandate = clientMandateRepository.getByClientCodeAndSaveMode(mandateDTO.getClientCode(), FinexaConstant.DRAFT_MODE);
						if (alreadySavedMandate != null && alreadySavedMandate.size() > 0 && alreadySavedMandate.get(0) != null) {
							clientMandateRegistration.setId(alreadySavedMandate.get(0).getId());
						}
						//get Mandate Id
						int selectedIndex = clientUCCResultDTO.getMessage().indexOf('|');
						String response = clientUCCResultDTO.getMessage().substring(0, selectedIndex);
						String mandateId = clientUCCResultDTO.getMessage().substring((selectedIndex + 1), clientUCCResultDTO.getMessage().length());
						if (mandateId != null && !mandateId.equals("")) {
							clientMandateRegistration.setBseResponse("" + clientUCCResultDTO.getStatusCode());
							clientMandateRegistration.setBseResponse(response);
							clientMandateRegistration.setMandateId(mandateId);
						}
						/*if (array.length > 1) {
								String response = array[0];
								String mandateId = array[1];

							}*/
						// set in save mode
						clientMandateRegistration.setSaveMode(FinexaConstant.SUCCESS_MODE);
						ClientMandateRegistration clientMandateRegistrationAfterSave = clientMandateRepository.save(clientMandateRegistration);
					} else {
						// set in error mode
						clientMandateRegistration.setSaveMode(FinexaConstant.ERROR_MODE);
						ClientMandateRegistration clientMandateRegistrationAfterSave = clientMandateRepository.save(clientMandateRegistration);

					}
				}

			}
		}
		return clientUCCResultDTO;
	}

	@Override
	public ClientUCCResultDTO callUCCService(int lastSavedId, String clientCode)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub

		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		ClientUCCDetailsDraftMode clientUCCDetailsDraftMode = clientUCCDetailsDraftModeRepository.getByClientCodeAndSaveMode(clientCode, FinexaConstant.DRAFT_MODE);
		ClientUCCDetailsDTO clientUCCDetailsDTO = mapper.map(clientUCCDetailsDraftMode, ClientUCCDetailsDTO.class);
		if (clientUCCDetailsDraftMode != null) {
			try {
				{

					String pipeSeparatedString = "";
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientCode(), pipeSeparatedString);
					LookupTansactHoldingType lookupHolding = lookupTransactHoldingTypeRepository.findOne(clientUCCDetailsDraftMode.getClientHolding());
					pipeSeparatedString = joinPipeValues(lookupHolding.getValue(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientTaxStatus(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientOccupationCode(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAppName1(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAppName2(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAppName3(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientDOB(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientGender(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientGuardian(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientPan(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientNominee(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientNomineeRelation(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientGuardianPan(), pipeSeparatedString);
					LookupTransactUCCClientType lookupTransactUCCClientType = lookupTransactUCCClientTypeRepository.findOne(clientUCCDetailsDraftMode.getClientType());
					pipeSeparatedString = joinPipeValues(lookupTransactUCCClientType.getValue(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues((""+clientUCCDetailsDraftMode.getClientDefaultDP()), pipeSeparatedString);

					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientCDSLDPID(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientCDSLCLTID(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientNSDLDPID(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientNSDLCLTID(), pipeSeparatedString);

					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAccType1(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAccNo1(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientMicrNo1(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientIfscCode1(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getDefaultBankFlag1(), pipeSeparatedString);

					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAccType2(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAccNo2(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientMicrNo2(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientIfscCode2(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getDefaultBankFlag2(), pipeSeparatedString);

					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAccType3(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAccNo3(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientMicrNo3(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientIfscCode3(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getDefaultBankFlag3(), pipeSeparatedString);


					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAccType4(), pipeSeparatedString);

					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAccNo4(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientMicrNo4(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientIfscCode4(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getDefaultBankFlag4(), pipeSeparatedString);

					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAccType5(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAccNo5(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientMicrNo5(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientIfscCode5(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getDefaultBankFlag5(), pipeSeparatedString);

					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientChequeName5(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAdd1(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAdd2(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientAdd3(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientCity(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientState(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientPincode(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientCountry(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientResiPhone(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientResiFax(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientOfficePhone(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientOfficeFax(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientEmail(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientCommMode(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientDivPayMode(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientPan2(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getClientPan3(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getMapinNo(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getCmForAdd1(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getCmForAdd2(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getCmForAdd3(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getCmForCity(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getCmForPinCode(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getCmForState(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getCmForCountry(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getCmForResiPhone(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getCmForResiFax(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getCmForOffPhone(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getCmForOffFax(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientUCCDetailsDraftMode.getCmMobile(), pipeSeparatedString);

					System.out.println(pipeSeparatedString);
					pipeSeparatedString = pipeSeparatedString.substring(0,pipeSeparatedString.length()-1);
					System.out.println(pipeSeparatedString);
					MFUPLOADSoapService mFUPLOADSoapService = new MFUPLOADSoapService();

					String bseUserId = clientUCCDetailsDraftMode.getAdvisorUser().getBseUsername();
					String bseMemberId = clientUCCDetailsDraftMode.getAdvisorUser().getBseMemberId();
					String bsePassword = decrypt(clientUCCDetailsDraftMode.getAdvisorUser().getBsePassword());
					String passKey = autoGeneratePassKey();

					clientUCCResultDTO = mFUPLOADSoapService.authenticateMFUploadService
							(bseUserId, bseMemberId, bsePassword, passKey, clientUCCDetailsDraftMode.getAdvisorUser().getLookupTransactBseaccessMode().getId());

					if (clientUCCResultDTO.isStatus()) {
						// Bse call to authenticate password is successful,
						//Now upload the ccCreationDetails
						//							pipeSeparatedString = "C301|SI|01|01|Nimish Agrawal|||13/12/1979|M||AFNPA4076L||||P||||||SB|004104000092658||IBKL0000004|Y||||||||||||||||||||||Premier Road|Ghatkopar(W)||Mumbai|MA|400086|India|||||agrawalnimesh.na@gmail.com|P|01|||||||||||||||9820017218";
						clientUCCResultDTO = mFUPLOADSoapService.fireMFAPIRequest(BSEConstant.MFAPI_UCC_MFD, bseUserId, clientUCCResultDTO.getMessage(), pipeSeparatedString, clientUCCDetailsDraftMode.getAdvisorUser().getLookupTransactBseaccessMode().getId());
						if (clientUCCResultDTO.isStatus()) {
							ClientUCCDetail clientUCCDetail = mapper.map(clientUCCDetailsDTO, ClientUCCDetail.class);
							System.out.println(clientUCCDetail.getClientAppName1());
							if (clientUCCDetailsDraftMode.getClientHolding() != null) {
								LookupTansactHoldingType lookupTansactHoldingType = lookupTransactHoldingTypeRepository.findOne(clientUCCDetailsDraftMode.getClientHolding());
								clientUCCDetail.setLookupTansactHoldingType(lookupTansactHoldingType);
							}
							if(clientUCCDetailsDraftMode.getClientAccType1() != null) {
								MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository.findOne(clientUCCDetailsDraftMode.getClientAccType1());
								clientUCCDetail.setMasterTransactAccountType1(masterTransactAccountType);
							}
							if(clientUCCDetailsDraftMode.getClientAccType2() != null) {
								MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository.findOne(clientUCCDetailsDraftMode.getClientAccType2());
								clientUCCDetail.setMasterTransactAccountType2(masterTransactAccountType);
							}
							if(clientUCCDetailsDraftMode.getClientAccType3() != null) {
								MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository.findOne(clientUCCDetailsDraftMode.getClientAccType3());
								clientUCCDetail.setMasterTransactAccountType3(masterTransactAccountType);
							}
							if(clientUCCDetailsDraftMode.getClientAccType4() != null) {
								MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository.findOne(clientUCCDetailsDraftMode.getClientAccType4());
								clientUCCDetail.setMasterTransactAccountType4(masterTransactAccountType);
							}
							if(clientUCCDetailsDraftMode.getClientAccType5() != null) {
								MasterTransactAccountType masterTransactAccountType = masterTransactAccountTypeRepository.findOne(clientUCCDetailsDraftMode.getClientAccType5());
								clientUCCDetail.setMasterTransactAccountType5(masterTransactAccountType);
							}
							if(clientUCCDetailsDraftMode.getClientState() != null) {
								MasterTransactStateCode stateCode = masterTransactStateCodeRepository.findOne(clientUCCDetailsDraftMode.getClientState());
								clientUCCDetail.setMasterTransactStateCode(stateCode);
							}
							if(clientUCCDetailsDraftMode.getClientCommMode() != null) {
								MasterTransactCommunicationMode commMode = masterTransactCommunicationModeRepository.findOne(clientUCCDetailsDraftMode.getClientCommMode());
								clientUCCDetail.setMasterTransactCommunicationMode(commMode);
							}
							if(clientUCCDetailsDraftMode.getClientDivPayMode() != null) {
								MasterTransactDivPayMode divPayMode = masterTransactDivPayModeRepository.findOne(clientUCCDetailsDraftMode.getClientDivPayMode());
								clientUCCDetail.setMasterTransactDivPayMode(divPayMode);
							}
							if(clientUCCDetailsDraftMode.getClientTaxStatus() != null) {
								MasterTransactTaxStatus taxStatus = masterTransactTaxStatusRepository.findOne(clientUCCDetailsDraftMode.getClientTaxStatus());
								clientUCCDetail.setMasterTransactTaxStatus(taxStatus);
							}
							if(clientUCCDetailsDraftMode.getClientOccupationCode() != null) {
								MasterTransactOccupationCode occCode = masterTransactOccupationCodeRepository.findOne(clientUCCDetailsDraftMode.getClientOccupationCode());
								clientUCCDetail.setMasterTransactOccupationCode(occCode);
							}
							if(clientUCCDetailsDraftMode.getClientType() != null) {
								LookupTransactUCCClientType clientType = lookupTransactUCCClientTypeRepository.findOne(clientUCCDetailsDraftMode.getClientType());
								clientUCCDetail.setLookupTransactUccclientType(clientType);
							}
							if(clientUCCDetailsDraftMode.getClientDefaultDP() != null) {
								LookupTransactUCCClientDefaultDP lookupTransactUCCClientDefaultDP = LookupTransactUCCClientDefaultDPRepository.findOne(clientUCCDetailsDraftMode.getClientDefaultDP());
								clientUCCDetail.setLookupTransactUccclientDefaultDp(lookupTransactUCCClientDefaultDP);
							}
							clientUCCDetail.setAdvisorUser(clientUCCDetailsDraftMode.getAdvisorUser());
							clientUCCDetail.setClientMaster(clientUCCDetailsDraftMode.getClientMaster());

							try {
								clientUCCDetail = clientUCCRepository.save(clientUCCDetail);
								clientUCCDetailsDraftMode.setSaveMode(FinexaConstant.SUCCESS_MODE);
								clientUCCDetailsDraftMode = clientUCCDetailsDraftModeRepository.save(clientUCCDetailsDraftMode);
							}catch (Exception e) {
								System.out.println(e.getMessage());
							}

							ClientCKYCDetail ckycDetail = clientKYCRepository.getByClientCodeAndSaveMode(clientCode, FinexaConstant.DRAFT_MODE);
							if (ckycDetail != null) {
								String pipeStringCKYC = "";
								pipeStringCKYC = joinPipeValues(ckycDetail.getClientCode(), pipeStringCKYC);
								pipeStringCKYC = joinPipeValues(ckycDetail.getClientPan(), pipeStringCKYC);
								pipeStringCKYC = joinPipeValues(""+ckycDetail.getCkycNumberFirst(), pipeStringCKYC);
								pipeStringCKYC = joinPipeValues(""+ckycDetail.getCkycNumberSecond(), pipeStringCKYC);
								pipeStringCKYC = joinPipeValues(""+ckycDetail.getCkycNumberThird(), pipeStringCKYC);
								pipeStringCKYC = joinPipeValues(""+ckycDetail.getCkycGuardian(), pipeStringCKYC);
								SimpleDateFormat ckycFormat = new SimpleDateFormat("dd/MM/yyyy");
								String dob1 = "";
								if (!(ckycDetail.getJointHolderDOB1() == null)) {
									dob1 = ckycFormat.format(ckycDetail.getJointHolderDOB1());
								}
								String dob2 = "";
								if (!(ckycDetail.getJointHolderDOB2() == null )) {
									dob2 = ckycFormat.format(ckycDetail.getJointHolderDOB2());
								}
								String dobGuardian = "";
								if (!(ckycDetail.getGuardianDOB() == null)) {
									dobGuardian = ckycFormat.format(ckycDetail.getGuardianDOB());
								}
								pipeStringCKYC = joinPipeValues("" + dob1, pipeStringCKYC);
								pipeStringCKYC = joinPipeValues(""+dob2, pipeStringCKYC);
								pipeStringCKYC = joinPipeValues(""+dobGuardian, pipeStringCKYC);
								pipeStringCKYC = joinPipeValues(ckycDetail.getLookupTransactKyctype1().getValue(), pipeStringCKYC);

								if (ckycDetail.getLookupTransactKyctype2() == null) {
									pipeStringCKYC = joinPipeValues("", pipeStringCKYC);
								} else {
									pipeStringCKYC = joinPipeValues(ckycDetail.getLookupTransactKyctype2().getValue(), pipeStringCKYC);

								}

								if (ckycDetail.getLookupTransactKyctype3() == null) {
									pipeStringCKYC = joinPipeValues("", pipeStringCKYC);
								} else {
									pipeStringCKYC = joinPipeValues(ckycDetail.getLookupTransactKyctype3().getValue(), pipeStringCKYC);

								}

								if (ckycDetail.getLookupTransactKyctype4() == null) {
									pipeStringCKYC = joinPipeValues("", pipeStringCKYC);
								} else {
									pipeStringCKYC = joinPipeValues(ckycDetail.getLookupTransactKyctype4().getValue(), pipeStringCKYC);

								}
								System.out.println("pipeStringCKYC" + pipeStringCKYC);
								pipeStringCKYC = pipeStringCKYC.substring(0, pipeStringCKYC.length() - 1);

								clientUCCResultDTO = mFUPLOADSoapService.authenticateMFUploadService
										(bseUserId, bseMemberId, bsePassword, passKey, ckycDetail.getAdvisorUser().getLookupTransactBseaccessMode().getId());

								if (clientUCCResultDTO.isStatus()) {
									// Bse call to authenticate password is successful,
									//Now upload the ccCreationDetails
									clientUCCResultDTO = mFUPLOADSoapService.fireMFAPIRequest(BSEConstant.MFAPI_CLIENT_CKYC_UPLOAD, bseUserId, clientUCCResultDTO.getMessage(), pipeStringCKYC, ckycDetail.getAdvisorUser().getLookupTransactBseaccessMode().getId());
									if (clientUCCResultDTO.isStatus()) {
										// ccCreation with BSE Successful. Save the result in table
										ckycDetail.setSaveMode(FinexaConstant.SUCCESS_MODE);
										ClientCKYCDetail clientCKYCDetailAfterSave = clientKYCRepository.save(ckycDetail);
									}
								} else {
									int id = lastSavedId + 1;
									ckycDetail.setId(""+id);
									ckycDetail.setSaveMode(FinexaConstant.ERROR_MODE);
									ckycDetail = clientKYCRepository.save(ckycDetail);
									if (ckycDetail != null) {
										clientUCCResultDTO.setOptionalParamCKYC(ckycDetail.getId());
									} 
									
									return clientUCCResultDTO;
								}
							}
						} else {
							/*int id = lastSavedId + 1;
							clientUCCDetailsDraftMode.setId(""+id);*/
							clientUCCDetailsDraftMode.setSaveMode(FinexaConstant.ERROR_MODE);
							clientUCCDetailsDraftMode = clientUCCDetailsDraftModeRepository.save(clientUCCDetailsDraftMode);
							if (clientUCCDetailsDraftMode != null) {
								clientUCCResultDTO.setOptionalParam(clientUCCDetailsDraftMode.getId());
							} 
							return clientUCCResultDTO;
						}
					} else {
						return clientUCCResultDTO;
					}
				}
			} catch (Exception e) {}
		}
		return clientUCCResultDTO;
	}
	@Override
	public ClientUCCDetailsDTO getClientByUCC(String clientCode) throws FinexaBussinessException {
		// TODO Auto-generated method stub

		ClientUCCDetailsDTO clientUCCDetailsDTO = new ClientUCCDetailsDTO();
		ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(clientCode);
		if (clientUCCDetail != null) {
			clientUCCDetailsDTO = mapper.map(clientUCCDetail, ClientUCCDetailsDTO.class);
			
			// Transact Holding Type
			if(clientUCCDetail.getLookupTansactHoldingType()!=null){
				clientUCCDetailsDTO.setClientHolding(clientUCCDetail.getLookupTansactHoldingType().getId());
			}
			// Account Type of Client 1
			if(clientUCCDetail.getMasterTransactAccountType1()!=null){
				clientUCCDetailsDTO.setClientAccType1(clientUCCDetail.getMasterTransactAccountType1().getCode());
			}
			// Account Type of Client 2
			if(clientUCCDetail.getMasterTransactAccountType2()!=null){
				clientUCCDetailsDTO.setClientAccType2(clientUCCDetail.getMasterTransactAccountType2().getCode());
			}
			// Account Type of Client 3
			if(clientUCCDetail.getMasterTransactAccountType3()!=null){
				clientUCCDetailsDTO.setClientAccType3(clientUCCDetail.getMasterTransactAccountType3().getCode());
			}
			// Account Type of Client 4
			if(clientUCCDetail.getMasterTransactAccountType4()!=null){
				clientUCCDetailsDTO.setClientAccType4(clientUCCDetail.getMasterTransactAccountType4().getCode());
			}
			// Account Type of Client 5
			if(clientUCCDetail.getMasterTransactAccountType5()!=null){
				clientUCCDetailsDTO.setClientAccType5(clientUCCDetail.getMasterTransactAccountType5().getCode());
			}
			//State Code
			if(clientUCCDetail.getMasterTransactStateCode()!=null){
				clientUCCDetailsDTO.setClientState(clientUCCDetail.getMasterTransactStateCode().getCode());
			}
			// Communication mode
			if(clientUCCDetail.getMasterTransactCommunicationMode()!=null){
				clientUCCDetailsDTO.setClientCommMode(clientUCCDetail.getMasterTransactCommunicationMode().getCode());
			}
			// Div Payment Mode
			if(clientUCCDetail.getMasterTransactDivPayMode()!=null){
				clientUCCDetailsDTO.setClientDivPayMode(clientUCCDetail.getMasterTransactDivPayMode().getCode());
			}
			/*  Advisor ID is not present in DTO
			if(clientUCCDetail.getAdvisorUser()!=null){
				
			}*/
			/* Client Id is not present in the DTO
			if(clientUCCDetail.getClientMaster()!=null){
				clientUCCDetailsDTO.setClientId
			}*/
			// Tax Status
			if(clientUCCDetail.getMasterTransactTaxStatus()!=null){
				clientUCCDetailsDTO.setClientTaxStatus(clientUCCDetail.getMasterTransactTaxStatus().getCode());
			}
			// Occupation Code
			if(clientUCCDetail.getMasterTransactOccupationCode()!=null){
				clientUCCDetailsDTO.setClientOccupationCode(clientUCCDetail.getMasterTransactOccupationCode().getCode());
			}
			// Client Type
			if(clientUCCDetail.getLookupTransactUccclientType()!=null){
				clientUCCDetailsDTO.setClientType(clientUCCDetail.getLookupTransactUccclientType().getId());
			}
			// Client Default DP
			if(clientUCCDetail.getLookupTransactUccclientDefaultDp()!=null){
				clientUCCDetailsDTO.setClientDefaultDP(clientUCCDetail.getLookupTransactUccclientDefaultDp().getId());
			}
			
		}

		return clientUCCDetailsDTO;
	}
	@Override
	public ClientUCCResultDTO registerPurchaseOrder(PurchaseOrderEntryParamDTO purchaseOrderEntryParamDTO)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub
		
		PurchaseOrderEntryParam purchaseOrderEntryParam = mapper.map(purchaseOrderEntryParamDTO, PurchaseOrderEntryParam.class);
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String clientCode = purchaseOrderEntryParamDTO.getClientCode();
		ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(clientCode);
		
		if (clientUCCDetail != null) {
			purchaseOrderEntryParam.setAdvisorUser(clientUCCDetail.getAdvisorUser());
			purchaseOrderEntryParam.setClientMaster(clientUCCDetail.getClientMaster());
			if (purchaseOrderEntryParamDTO.getDpTxn().equals( FinexaConstant.PHYSICAL_MODE)) {
				
				if (purchaseOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
					int urnPhysical = (purchaseOrderEntryParamDTO.getUniqueSchemeCode());
					MasterTransactBSEMFPhysicalSchemeLive physicalSchemeLive = masterTransactBSEMFPhysicalRepositoryLive.findOne(urnPhysical);
					purchaseOrderEntryParam.setMasterTransactBsemfphysicalSchemeLive(physicalSchemeLive);
				} else {
					int urnPhysical = (purchaseOrderEntryParamDTO.getUniqueSchemeCode());
					MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
					purchaseOrderEntryParam.setMasterTransactBsemfphysicalScheme(physicalScheme);
				}
				
			} else {
				if (purchaseOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
					int urnPhysical = (purchaseOrderEntryParamDTO.getUniqueSchemeCode());
					MasterTransactBSEMFPhysicalSchemeLive physicalSchemeLive = masterTransactBSEMFPhysicalRepositoryLive.findOne(urnPhysical);
					purchaseOrderEntryParam.setMasterTransactBsemfphysicalSchemeLive(physicalSchemeLive);
				} else {
					int urnPhysical = (purchaseOrderEntryParamDTO.getUniqueSchemeCode());
					MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
					purchaseOrderEntryParam.setMasterTransactBsemfphysicalScheme(physicalScheme);
				}
			}

			/*List<PurchaseOrderEntryParam> alreadySavedOrders = purchaseOrderEntryParamRepository.
					getByClientCodeAndSaveModeAndPurchaseMode(purchaseOrderEntryParamDTO.getClientCode(), 
							FinexaConstant.DRAFT_MODE, purchaseOrderEntryParamDTO.getPurchaseMode());
			if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
				purchaseOrderEntryParam.setId(alreadySavedOrders.get(0).getId());
			}*/
			purchaseOrderEntryParam.setSaveMode(FinexaConstant.DRAFT_MODE);
			purchaseOrderEntryParam.setCreatedAt(new Date());
			//generate URN
			String urn = generateURN(purchaseOrderEntryParam.getAdvisorUser().getBseMemberId());
			purchaseOrderEntryParam.setUrn(urn);
			purchaseOrderEntryParam.setTransactionCode("NEW");
			try {
				purchaseOrderEntryParam = purchaseOrderEntryParamRepository.save(purchaseOrderEntryParam);
				if (purchaseOrderEntryParam != null) {
					if (purchaseOrderEntryParam.getPurchaseMode().equals(FinexaConstant.PURCHASE_MODE_BUY)) {
						clientUCCResultDTO = registerAndFirePurchaseOrder(purchaseOrderEntryParam);
					} else {
						// else it is in cart. Afterwards it will  be processed from cart
						clientUCCResultDTO.setStatusCode(200);
						clientUCCResultDTO.setMessage("Order Successfully Added To Cart");
						// calculate the total cart count
						int cartCount = calculateTotalCartCount(purchaseOrderEntryParam.getClientCode());
						clientUCCResultDTO.setOptionalParam(""+cartCount);
					}
				}
			} catch (Exception e) {
				clientUCCResultDTO.setStatusCode(500);
				clientUCCResultDTO.setMessage("Unable to save Record in database due to " + e.getMessage());
			}
		}
		return clientUCCResultDTO;
	}
	private int calculateTotalCartCount(String clientCode) {
		// TODO Auto-generated method stub
		int cartCount = 0;
		List<PurchaseOrderEntryParam> purchaseOrdersInCartList = purchaseOrderEntryParamRepository.
				getByClientCodeAndSaveModeAndPurchaseMode(clientCode, FinexaConstant.DRAFT_MODE, FinexaConstant.PURCHASE_MODE_CART);

		if (purchaseOrdersInCartList != null && purchaseOrdersInCartList.size() > 0) {
			cartCount = cartCount + purchaseOrdersInCartList.size();
		}

		List<XsipOrderEntryParam> xsipOrderList = sipOrderEntryParamRepository.
				getByClientCodeAndSaveModeAndPurchaseMode(clientCode, FinexaConstant.DRAFT_MODE, FinexaConstant.PURCHASE_MODE_CART);

		if (xsipOrderList != null && xsipOrderList.size() > 0) {
			cartCount = cartCount + xsipOrderList.size();
		}


		List<ClientSwitchOrderEntryParam> switchOrderList = clientSwitchOrderEntryParamRepository.
				getByClientCodeAndSaveModeAndPurchaseMode(clientCode, FinexaConstant.DRAFT_MODE, FinexaConstant.PURCHASE_MODE_CART);

		if (switchOrderList != null && switchOrderList.size() > 0) {
			cartCount = cartCount + switchOrderList.size();
		}


		List<ClientSTPOrderRegistration> stpOrderList = clientSTPRepository.
				getByClientCodeAndSaveModeAndPurchaseMode(clientCode, FinexaConstant.DRAFT_MODE, FinexaConstant.PURCHASE_MODE_CART);

		if (stpOrderList != null && stpOrderList.size() > 0) {
			cartCount = cartCount + stpOrderList.size();
		}

		List<ClientSWPOrderRegistration> swpOrderList = clientSWPRepository.
				getByClientCodeAndSaveModeAndPurchaseMode(clientCode, FinexaConstant.DRAFT_MODE, FinexaConstant.PURCHASE_MODE_CART);

		if (swpOrderList != null && swpOrderList.size() > 0) {
			cartCount = cartCount + swpOrderList.size();
		}

		return cartCount;
	}
	private ClientUCCResultDTO registerAndFirePurchaseOrder(PurchaseOrderEntryParam purchaseOrderEntryParam) {
		// TODO Auto-generated method stub
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		PurchaseOrderEntryParamDTO purchaseDto = new PurchaseOrderEntryParamDTO();
		if (purchaseOrderEntryParam != null) {

			String bseUserId = purchaseOrderEntryParam.getAdvisorUser().getBseUsername();
			String bseMemberId = purchaseOrderEntryParam.getAdvisorUser().getBseMemberId();
			String bsePassword = decrypt(purchaseOrderEntryParam.getAdvisorUser().getBsePassword());
			String passKey = autoGeneratePassKey();
			
			MFOrderEntryService mFOrderEntryService = new MFOrderEntryService();
			clientUCCResultDTO = mFOrderEntryService.authenticateMFOrderEntryService(bseUserId, bseMemberId, bsePassword, purchaseOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId());
			
			if (clientUCCResultDTO.isStatus()) {
				// Bse call to authenticate password is successful,
				//Now upload the mandateDetails
				purchaseDto.setPassKey(passKey);
				purchaseDto.setEncryptedPassword(clientUCCResultDTO.getMessage());
				System.out.println("clientUCCResultDTO.getMessage() " + clientUCCResultDTO.getMessage());
				clientUCCResultDTO = mFOrderEntryService.fireMFOrderEntryLumpsumRequest(purchaseOrderEntryParam,purchaseDto);
				purchaseOrderEntryParam.setBseResponse(clientUCCResultDTO.getMessage());
				System.out.println("BSE Resp " + clientUCCResultDTO.getMessage() + " length" + (clientUCCResultDTO.getMessage().length() - 2));
				String message = clientUCCResultDTO.getMessage().substring(0, (clientUCCResultDTO.getMessage().length() - 2));
				System.out.println("Str message " + message);
				int lastIndexOfPipe = message.lastIndexOf("|");
				System.out.println("lastIndexOfPipe " + lastIndexOfPipe);
				message = message.substring((lastIndexOfPipe + 1), message.length());
				clientUCCResultDTO.setMessage(message);
				purchaseOrderEntryParam.setBseResponseCode("" + clientUCCResultDTO.getStatusCode());
				if (clientUCCResultDTO.isStatus()) {
					// ccCreation with BSE Successful. Save the result in table
					/*List<PurchaseOrderEntryParam> alreadySavedOrders = purchaseOrderEntryParamRepository.
							getByClientCodeAndSaveModeAndPurchaseMode(purchaseOrderEntryParam.getClientCode(), 
									FinexaConstant.DRAFT_MODE, purchaseOrderEntryParam.getPurchaseMode());
					if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
						purchaseOrderEntryParam.setId(alreadySavedOrders.get(0).getId());
					}*/
					purchaseOrderEntryParam.setSaveMode(FinexaConstant.SUCCESS_MODE);
					purchaseOrderEntryParam = purchaseOrderEntryParamRepository.save(purchaseOrderEntryParam);
				} else {
					// set in error mode
					purchaseOrderEntryParam.setSaveMode(FinexaConstant.ERROR_MODE);
					purchaseOrderEntryParam = purchaseOrderEntryParamRepository.save(purchaseOrderEntryParam);

				}
			}
		}
		return clientUCCResultDTO;
	}
	private String generateURN(String clientCode) {
		// TODO Auto-generated method stub
		String urnInString = "";
		int urn = 0;
		Date todayDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		String dateString = dateFormat.format(todayDate);
		UniqueIdentifierNumberDateWise uniqueIdentifierNumberDateWise = uniqueIdentifierNumberDateWiseRepository.findOne(dateString);
		if (uniqueIdentifierNumberDateWise == null) {
			uniqueIdentifierNumberDateWise = new UniqueIdentifierNumberDateWise();
			uniqueIdentifierNumberDateWise.setDate(dateString);
			uniqueIdentifierNumberDateWise.setUrn(0);
		} else {
			urn = uniqueIdentifierNumberDateWise.getUrn();
			urn ++;
			uniqueIdentifierNumberDateWise.setUrn(urn);
		}
		try {
			uniqueIdentifierNumberDateWise = uniqueIdentifierNumberDateWiseRepository.save(uniqueIdentifierNumberDateWise);
			if (uniqueIdentifierNumberDateWise != null) {
				urn = uniqueIdentifierNumberDateWise.getUrn() + 1;
			}
		} catch (Exception e) {}
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
		Date dt = new Date();
		String dateInString = sdf.format(dt);
		urnInString = dateInString+clientCode+ urn;
		return urnInString;
	}
	@Override
	public ClientUCCResultDTO registerSIPOrder(SIPOrderEntryParamDTO sipOrderEntryParamDTO)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub

		XsipOrderEntryParam xsipOrderEntryParam = mapper.map(sipOrderEntryParamDTO, XsipOrderEntryParam.class);
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String clientCode = sipOrderEntryParamDTO.getClientCode();
		ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(clientCode);

		if (clientUCCDetail != null) {
			xsipOrderEntryParam.setAdvisorUser(clientUCCDetail.getAdvisorUser());
			xsipOrderEntryParam.setClientMaster(clientUCCDetail.getClientMaster());
			if (sipOrderEntryParamDTO.getDpTransactionMode().equals( FinexaConstant.PHYSICAL_MODE)) {
				/*int urnPhysical = (sipOrderEntryParamDTO.getUniqueSchemeCode());
				MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
				xsipOrderEntryParam.setMasterTransactBsemfphysicalScheme(physicalScheme);*/
				xsipOrderEntryParam.setTransMode(FinexaConstant.PHYSICAL_MODE);
			} else {
				/*int urnDemat = sipOrderEntryParamDTO.getUniqueSchemeCode();
				MasterTransactBSEMFDematScheme dematScheme = masterTransactBSEMFDematRepository.findOne(urnDemat);
				xsipOrderEntryParam.setMasterTransactBsemfdematScheme(dematScheme);*/
				xsipOrderEntryParam.setTransMode(FinexaConstant.DEMAT_MODE);
			}

			if (xsipOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				int urnPhysical = (sipOrderEntryParamDTO.getUniqueSchemeCode());
				MasterTransactBSEMFPhysicalSchemeLive physicalSchemeLive = masterTransactBSEMFPhysicalRepositoryLive.findOne(urnPhysical);
				xsipOrderEntryParam.setMasterTransactBsemfphysicalSchemeLive(physicalSchemeLive);
			} else {
				int urnPhysical = (sipOrderEntryParamDTO.getUniqueSchemeCode());
				MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
				xsipOrderEntryParam.setMasterTransactBsemfphysicalScheme(physicalScheme);
			}
			
			/*List<XsipOrderEntryParam> alreadySavedOrders = sipOrderEntryParamRepository.
					getByClientCodeAndSaveModeAndPurchaseMode(sipOrderEntryParamDTO.getClientCode(), FinexaConstant.DRAFT_MODE, FinexaConstant.PURCHASE_MODE_BUY);
			if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
				xsipOrderEntryParam.setId(alreadySavedOrders.get(0).getId());
			}*/
			xsipOrderEntryParam.setSaveMode(FinexaConstant.DRAFT_MODE);
			xsipOrderEntryParam.setCreatedAt(new Date());
			String urn = generateURN(xsipOrderEntryParam.getAdvisorUser().getBseMemberId());
			xsipOrderEntryParam.setUrn(urn);
			xsipOrderEntryParam.setTransactionCode("NEW");
			xsipOrderEntryParam.setFrequencyAllowed(ROLLING_FREQUENCY);
			try {
				xsipOrderEntryParam = sipOrderEntryParamRepository.save(xsipOrderEntryParam);
				if (xsipOrderEntryParam != null) {
					if (xsipOrderEntryParam.getPurchaseMode().equals(FinexaConstant.PURCHASE_MODE_BUY)) {
						clientUCCResultDTO = registerAndFireSIPOrder(xsipOrderEntryParam);
					} // else it is in cart. Afterwards it will  be processed from cart 
					else {
						clientUCCResultDTO.setStatusCode(200);
						clientUCCResultDTO.setMessage("Order Successfully Added To Cart"); 
						// calculate the total cart count
						int cartCount = calculateTotalCartCount(xsipOrderEntryParam.getClientCode());
						clientUCCResultDTO.setOptionalParam(""+cartCount);
					}
				}
			} catch (Exception e) {
				clientUCCResultDTO.setStatusCode(500);
				clientUCCResultDTO.setMessage("Unable to save Record in database due to " + e.getMessage());
			}
			xsipOrderEntryParam = sipOrderEntryParamRepository.save(xsipOrderEntryParam);
			if (xsipOrderEntryParam != null) {}
		}
		return clientUCCResultDTO;
	}
	private ClientUCCResultDTO registerAndFireSIPOrder(XsipOrderEntryParam xsipOrderEntryParam) {
		// TODO Auto-generated method stub

		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		SIPOrderEntryParamDTO sipOrderEntryParamDTO = new SIPOrderEntryParamDTO();
		String bseUserId = xsipOrderEntryParam.getAdvisorUser().getBseUsername();
		String bseMemberId = xsipOrderEntryParam.getAdvisorUser().getBseMemberId();
		String bsePassword = decrypt(xsipOrderEntryParam.getAdvisorUser().getBsePassword());
		String passKey = autoGeneratePassKey();

		MFOrderEntryService mFOrderEntryService = new MFOrderEntryService();
		clientUCCResultDTO = mFOrderEntryService.authenticateMFOrderEntryService(bseUserId, bseMemberId, bsePassword, xsipOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId());

		if (clientUCCResultDTO.isStatus()) {
			// Bse call to authenticate password is successful,
			//Now upload the mandateDetails
			sipOrderEntryParamDTO.setRegType(xsipOrderEntryParam.getRegType());
			sipOrderEntryParamDTO.setEncryptedPassword(clientUCCResultDTO.getMessage());
			clientUCCResultDTO = mFOrderEntryService.fireMFOrderEntryXSIPRequest(xsipOrderEntryParam,sipOrderEntryParamDTO);
			xsipOrderEntryParam.setBseResponse(clientUCCResultDTO.getMessage());
			String message = clientUCCResultDTO.getMessage().substring(0, (clientUCCResultDTO.getMessage().length() - 2));
			int lastIndexOfPipe = message.lastIndexOf("|");
			message = message.substring((lastIndexOfPipe + 1), message.length());
			clientUCCResultDTO.setMessage(message);
			xsipOrderEntryParam.setBseResponseCode("" + clientUCCResultDTO.getStatusCode());
			if (clientUCCResultDTO.isStatus()) {
				// ccCreation with BSE Successful. Save the result in table
				/*List<XsipOrderEntryParam> alreadySavedOrders = sipOrderEntryParamRepository.
						getByClientCodeAndSaveModeAndPurchaseMode(sipOrderEntryParamDTO.getClientCode(),
								FinexaConstant.DRAFT_MODE, FinexaConstant.PURCHASE_MODE_BUY);
				if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
					xsipOrderEntryParam.setId(alreadySavedOrders.get(0).getId());
				}*/
				xsipOrderEntryParam.setSaveMode(FinexaConstant.SUCCESS_MODE);
				xsipOrderEntryParam = sipOrderEntryParamRepository.save(xsipOrderEntryParam);
			} else {
				// set in error mode
				xsipOrderEntryParam.setSaveMode(FinexaConstant.ERROR_MODE);
				xsipOrderEntryParam = sipOrderEntryParamRepository.save(xsipOrderEntryParam);

			}
		}
		return clientUCCResultDTO;

	}
	@Override
	public ClientUCCResultDTO registerSwitchOrder(ClientSwitchOrderEntryParamDTO clientSwitchOrderEntryParamDTO)
			throws FinexaBussinessException {


		// TODO Auto-generated method stub

		ClientSwitchOrderEntryParam clientSwitchOrderEntryParam = mapper.map(clientSwitchOrderEntryParamDTO, ClientSwitchOrderEntryParam.class);
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String clientCode = clientSwitchOrderEntryParamDTO.getClientCode();
		ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(clientCode);

		if (clientUCCDetail != null) {
			clientSwitchOrderEntryParam.setAdvisorUser(clientUCCDetail.getAdvisorUser());
			clientSwitchOrderEntryParam.setClientMaster(clientUCCDetail.getClientMaster());
			
			if (clientSwitchOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				int urnPhysical = (clientSwitchOrderEntryParamDTO.getFromSchemeUniqueNo());
				MasterTransactBSEMFPhysicalSchemeLive physicalSchemeLive = masterTransactBSEMFPhysicalRepositoryLive.findOne(urnPhysical);
				clientSwitchOrderEntryParam.setMasterTransactBsemfphysicalSchemeLive(physicalSchemeLive);

				int urnToPhysical = (clientSwitchOrderEntryParamDTO.getToSchemeUniqueNo());
				MasterTransactBSEMFPhysicalSchemeLive toPhysicalSchemeLive = masterTransactBSEMFPhysicalRepositoryLive.findOne(urnToPhysical);
				clientSwitchOrderEntryParam.setMasterTransactBsemfTophysicalSchemeLive(toPhysicalSchemeLive);
			} else {
				int urnPhysical = (clientSwitchOrderEntryParamDTO.getFromSchemeUniqueNo());
				MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
				clientSwitchOrderEntryParam.setMasterTransactBsemfphysicalScheme1(physicalScheme);

				int urnToPhysical = (clientSwitchOrderEntryParamDTO.getToSchemeUniqueNo());
				MasterTransactBSEMFPhysicalScheme toPhysicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnToPhysical);
				clientSwitchOrderEntryParam.setMasterTransactBsemfphysicalScheme2(toPhysicalScheme);
			}
			


			/*if (clientSwitchOrderEntryParamDTO.getDpTxn().equals( FinexaConstant.PHYSICAL_MODE)) {
				int urnPhysical = (clientSwitchOrderEntryParamDTO.getFromSchemeUniqueNo());
				MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
				clientSwitchOrderEntryParam.setMasterTransactBsemfphysicalScheme1(physicalScheme);

				int urnToPhysical = (clientSwitchOrderEntryParamDTO.getFromSchemeUniqueNo());
				MasterTransactBSEMFPhysicalScheme toPhysicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnToPhysical);
				clientSwitchOrderEntryParam.setMasterTransactBsemfphysicalScheme2(toPhysicalScheme);

			} else {
				int urnDemat = (clientSwitchOrderEntryParamDTO.getFromSchemeUniqueNo());
				MasterTransactBSEMFDematScheme fromDematScheme = masterTransactBSEMFDematRepository.findOne(urnDemat);
				clientSwitchOrderEntryParam.setMasterTransactBsemfdematScheme1(fromDematScheme);

				int urnDematTo = (clientSwitchOrderEntryParamDTO.getToSchemeUniqueNo());
				MasterTransactBSEMFDematScheme toDematScheme = masterTransactBSEMFDematRepository.findOne(urnDematTo);
				clientSwitchOrderEntryParam.setMasterTransactBsemfdematScheme2(toDematScheme);


			}*/

			/*List<ClientSwitchOrderEntryParam> alreadySavedOrders = clientSwitchOrderEntryParamRepository.
					getByClientCodeAndSaveModeAndPurchaseMode(clientSwitchOrderEntryParamDTO.getClientCode(), 
							FinexaConstant.DRAFT_MODE, clientSwitchOrderEntryParamDTO.getPurchaseMode());
			if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
				clientSwitchOrderEntryParam.setId(alreadySavedOrders.get(0).getId());
			}*/
			clientSwitchOrderEntryParam.setSaveMode(FinexaConstant.DRAFT_MODE);
			clientSwitchOrderEntryParam.setCreatedAt(new Date());
			String urn = generateURN(clientSwitchOrderEntryParam.getAdvisorUser().getBseMemberId());
			clientSwitchOrderEntryParam.setUrn(urn);
			clientSwitchOrderEntryParam.setTransCode("NEW");
			clientSwitchOrderEntryParam.setCreatedBy("" + clientSwitchOrderEntryParam.getAdvisorUser().getBseMemberId());
			try {
				clientSwitchOrderEntryParam = clientSwitchOrderEntryParamRepository.save(clientSwitchOrderEntryParam);
				if (clientSwitchOrderEntryParam != null) {
					if (clientSwitchOrderEntryParam.getPurchaseMode().equals(FinexaConstant.PURCHASE_MODE_BUY)) {
						clientUCCResultDTO = registerAndFireSwitchOrder(clientSwitchOrderEntryParam);
					}else {
						// else it is in cart. Afterwards it will  be processed from cart
						clientUCCResultDTO.setStatusCode(200);
						clientUCCResultDTO.setMessage("Order Successfully Added To Cart");
						// calculate the total cart count
						int cartCount = calculateTotalCartCount(clientSwitchOrderEntryParam.getClientCode());
						clientUCCResultDTO.setOptionalParam(""+cartCount);
					}
				}
			} catch (Exception e) {
				clientUCCResultDTO.setStatusCode(500);
				clientUCCResultDTO.setMessage("Unable to save Record in database due to " + e.getMessage());
			}
		}
		return clientUCCResultDTO;


	}
	private ClientUCCResultDTO registerAndFireSwitchOrder(ClientSwitchOrderEntryParam clientSwitchOrderEntryParam) {
		// TODO Auto-generated method stub

		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		ClientSwitchOrderEntryParamDTO clientSwitchOrderEntryParamDTO = new ClientSwitchOrderEntryParamDTO();
		if (clientSwitchOrderEntryParam != null) {

			String bseUserId = clientSwitchOrderEntryParam.getAdvisorUser().getBseUsername();
			String bseMemberId = clientSwitchOrderEntryParam.getAdvisorUser().getBseMemberId();
			String bsePassword = decrypt(clientSwitchOrderEntryParam.getAdvisorUser().getBsePassword());
			String passKey = autoGeneratePassKey();

			MFOrderEntryService mFOrderEntryService = new MFOrderEntryService();
			clientUCCResultDTO = mFOrderEntryService.authenticateMFOrderEntryService(bseUserId, bseMemberId, bsePassword, clientSwitchOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId());

			if (clientUCCResultDTO.isStatus()) {
				// Bse call to authenticate password is successful,
				//Now upload the mandateDetails
				clientSwitchOrderEntryParamDTO.setPassKey(passKey);
				clientSwitchOrderEntryParamDTO.setEncryptedPassword(clientUCCResultDTO.getMessage());
				clientUCCResultDTO = mFOrderEntryService.fireMFOrderEntrySwitchRequest(clientSwitchOrderEntryParam, clientSwitchOrderEntryParamDTO);

				clientSwitchOrderEntryParam.setBseResponse(clientUCCResultDTO.getMessage());
				String message = clientUCCResultDTO.getMessage().substring(0, (clientUCCResultDTO.getMessage().length() - 2));
				int lastIndexOfPipe = message.lastIndexOf("|");
				message = message.substring((lastIndexOfPipe + 1), message.length());
				clientUCCResultDTO.setMessage(message);
				clientSwitchOrderEntryParam.setBseResponseCode("" + clientUCCResultDTO.getStatusCode());
				if (clientUCCResultDTO.isStatus()) {
					// ccCreation with BSE Successful. Save the result in table
					/*List<ClientSwitchOrderEntryParam> alreadySavedOrders = clientSwitchOrderEntryParamRepository.
							getByClientCodeAndSaveModeAndPurchaseMode(clientSwitchOrderEntryParam.getClientCode(), 
									FinexaConstant.DRAFT_MODE, clientSwitchOrderEntryParam.getPurchaseMode());
					if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
						clientSwitchOrderEntryParam.setId(alreadySavedOrders.get(0).getId());
					}*/
					clientSwitchOrderEntryParam.setSaveMode(FinexaConstant.SUCCESS_MODE);
					clientSwitchOrderEntryParam = clientSwitchOrderEntryParamRepository.save(clientSwitchOrderEntryParam);
				} else {
					// set in error mode
					clientSwitchOrderEntryParam.setSaveMode(FinexaConstant.ERROR_MODE);
					clientSwitchOrderEntryParam = clientSwitchOrderEntryParamRepository.save(clientSwitchOrderEntryParam);

				}
			}

		}
		return clientUCCResultDTO;
	}
	@Override
	public ClientUCCResultDTO registerSwpOrder(ClientSWPDTO clientSWPDTO) throws FinexaBussinessException {


		// TODO Auto-generated method stub

		ClientSWPOrderRegistration clientSWPSTPOrderRegistration = mapper.map(clientSWPDTO, ClientSWPOrderRegistration.class);
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String clientCode = clientSWPDTO.getClientCode();
		ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(clientCode);

		if (clientUCCDetail != null) {
			clientSWPSTPOrderRegistration.setAdvisorUser(clientUCCDetail.getAdvisorUser());
			clientSWPSTPOrderRegistration.setClientMaster(clientUCCDetail.getClientMaster());
			
			if (clientSWPSTPOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				int urnPhysical = (clientSWPDTO.getUniqueSchemeCode());
				MasterTransactBSEMFPhysicalSchemeLive physicalSchemeLive = masterTransactBSEMFPhysicalRepositoryLive.findOne(urnPhysical);
				clientSWPSTPOrderRegistration.setMasterTransactBsemfphysicalSchemeLive(physicalSchemeLive);
			} else {
				int urnPhysical = (clientSWPDTO.getUniqueSchemeCode());
				MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
				clientSWPSTPOrderRegistration.setMasterTransactBsemfphysicalScheme(physicalScheme);
			}
			if (!clientSWPSTPOrderRegistration.getTransactionMode().equals(FinexaConstant.PHYSICAL_MODE)) {
				clientSWPSTPOrderRegistration.setTransactionMode(FinexaConstant.DEMAT_MODE);
				clientSWPDTO.setTransactionMode(FinexaConstant.DEMAT_MODE);
			}
			
			/*int urnPhysical = (clientSWPDTO.getUniqueSchemeCode());
			MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
			clientSWPSTPOrderRegistration.setMasterTransactBsemfphysicalScheme(physicalScheme);*/
			/*if (clientSWPDTO.getTransactionMode().equals(FinexaConstant.PHYSICAL_MODE)) {
				int urnPhysical = (clientSWPDTO.getUniqueSchemeCode());
				MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
				clientSWPSTPOrderRegistration.setMasterTransactBsemfphysicalScheme(physicalScheme);
			} else {
				int urnDemat = (clientSWPDTO.getUniqueSchemeCode());
				MasterTransactBSEMFDematScheme fromDematScheme = masterTransactBSEMFDematRepository.findOne(urnDemat);
				clientSWPSTPOrderRegistration.setMasterTransactBsemfdematScheme(fromDematScheme);
			}*/

			/*List<ClientSWPOrderRegistration> alreadySavedOrders = clientSWPRepository.
					getByClientCodeAndSaveModeAndPurchaseMode(clientSWPDTO.getClientCode(), FinexaConstant.DRAFT_MODE, clientSWPDTO.getPurchaseMode());
			if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
				clientSWPSTPOrderRegistration.setId(alreadySavedOrders.get(0).getId());
			}*/
			clientSWPSTPOrderRegistration.setSaveMode(FinexaConstant.DRAFT_MODE);
			clientSWPSTPOrderRegistration.setCreatedAt(new Date());
			clientSWPSTPOrderRegistration = clientSWPRepository.save(clientSWPSTPOrderRegistration);
			if (clientSWPSTPOrderRegistration != null && clientSWPSTPOrderRegistration.getPurchaseMode().equals(FinexaConstant.PURCHASE_MODE_BUY)) {


				String pipeSeparatedString = "";
				pipeSeparatedString = joinPipeValues(clientSWPDTO.getClientCode(), pipeSeparatedString);
				
				String schemeCode = "";
				if (clientSWPSTPOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
					schemeCode = clientSWPSTPOrderRegistration.getMasterTransactBsemfphysicalSchemeLive().getSchemeCode();
				} else {
					schemeCode = clientSWPSTPOrderRegistration.getMasterTransactBsemfphysicalScheme().getSchemeCode();
				}
				
				pipeSeparatedString = joinPipeValues(schemeCode, pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientSWPDTO.getTransactionMode(), pipeSeparatedString);

				/*if (clientSWPDTO.getTransactionMode().equals( FinexaConstant.PHYSICAL_MODE)) {
					pipeSeparatedString = joinPipeValues(clientSWPSTPOrderRegistration.getMasterTransactBsemfphysicalScheme().getAmcSchemeCode(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientSWPDTO.getTransactionMode(), pipeSeparatedString);

				} else {
					pipeSeparatedString = joinPipeValues(clientSWPSTPOrderRegistration.getMasterTransactBsemfdematScheme().getAmcSchemeCode(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientSWPDTO.getTransactionMode(), pipeSeparatedString);
				}*/
				pipeSeparatedString = joinPipeValues(clientSWPDTO.getFolioNo(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues("", pipeSeparatedString);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String startDate = sdf.format(clientSWPDTO.getStartDate());

				pipeSeparatedString = joinPipeValues(startDate, pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(("" + clientSWPDTO.getNoOfWithDrawals()), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientSWPDTO.getFrequencyType(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientSWPDTO.getInstallmentAmount().toString(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientSWPDTO.getInstallmentUnit() == null ? "" :clientSWPDTO.getInstallmentUnit().toString(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientSWPDTO.getFirstOrderToday(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientSWPDTO.getSubBrokerCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientSWPDTO.getEuinDeclaration(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientSWPDTO.getEuinNumber(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientSWPDTO.getRemarks(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(clientSWPDTO.getSubBroker_ARN(), pipeSeparatedString);

				System.out.println(pipeSeparatedString);
				pipeSeparatedString = pipeSeparatedString.substring(0,pipeSeparatedString.length()-1);
				System.out.println(pipeSeparatedString);

				String bseUserId = clientSWPSTPOrderRegistration.getAdvisorUser().getBseUsername();
				String bseMemberId = clientSWPSTPOrderRegistration.getAdvisorUser().getBseMemberId();
				String bsePassword = decrypt(clientSWPSTPOrderRegistration.getAdvisorUser().getBsePassword());
				String passKey = autoGeneratePassKey();

				MFUPLOADSoapService mFUPLOADSoapService = new MFUPLOADSoapService();
				clientUCCResultDTO = mFUPLOADSoapService.authenticateMFUploadService
						(bseUserId, bseMemberId, bsePassword, passKey, clientSWPSTPOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId());

				if (clientUCCResultDTO.isStatus()) {
					// Bse call to authenticate password is successful,
					//Now upload the mandateDetails
					clientUCCResultDTO = mFUPLOADSoapService.fireMFAPIRequest(BSEConstant.MFAPI_SWP_REGISTRATION, bseUserId, clientUCCResultDTO.getMessage(), pipeSeparatedString, clientSWPSTPOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId());
					if (clientUCCResultDTO.isStatus()) {
						// ccCreation with BSE Successful. Save the result in table
						/*alreadySavedOrders = clientSWPRepository.
								getByClientCodeAndSaveModeAndPurchaseMode(clientSWPDTO.getClientCode(), FinexaConstant.DRAFT_MODE, 
										clientSWPSTPOrderRegistration.getPurchaseMode());
						if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
							clientSWPSTPOrderRegistration.setId(alreadySavedOrders.get(0).getId());
						}*/
						clientSWPSTPOrderRegistration.setBseResponse(clientUCCResultDTO.getMessage());
						clientSWPSTPOrderRegistration.setBseResponseCode("" +clientUCCResultDTO.getStatusCode());

						clientSWPSTPOrderRegistration.setSaveMode(FinexaConstant.SUCCESS_MODE);
						clientSWPSTPOrderRegistration = clientSWPRepository.save(clientSWPSTPOrderRegistration);
					} else {

						clientSWPSTPOrderRegistration.setBseResponse(clientUCCResultDTO.getMessage());
						clientSWPSTPOrderRegistration.setBseResponseCode("" +clientUCCResultDTO.getStatusCode());

						// set in error mode
						clientSWPSTPOrderRegistration.setSaveMode(FinexaConstant.ERROR_MODE);
						clientSWPSTPOrderRegistration = clientSWPRepository.save(clientSWPSTPOrderRegistration);
					}
				}


			} else {
				// else it is in cart. Afterwards it will  be processed from cart
				clientUCCResultDTO.setStatusCode(200);
				clientUCCResultDTO.setMessage("Order Successfully Added To Cart");
				// calculate the total cart count
				int cartCount = calculateTotalCartCount(clientSWPSTPOrderRegistration.getClientCode());
				clientUCCResultDTO.setOptionalParam(""+cartCount);
			}
		}
		return clientUCCResultDTO;


	}
	@Override
	public ClientUCCResultDTO registerStpOrder(ClientSTPDTO clientSTPDTO) throws FinexaBussinessException {
		// TODO Auto-generated method stub



		// TODO Auto-generated method stub

		ClientSTPOrderRegistration clientSTPOrderRegistration = mapper.map(clientSTPDTO, ClientSTPOrderRegistration.class);
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String clientCode = clientSTPDTO.getClientCode();
		ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(clientCode);

		if (clientUCCDetail != null) {
			clientSTPOrderRegistration.setAdvisorUser(clientUCCDetail.getAdvisorUser());
			clientSTPOrderRegistration.setClientMaster(clientUCCDetail.getClientMaster());
			
			if (clientSTPOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				int urnPhysical = (clientSTPDTO.getFromSchemeUniqueNo());
				MasterTransactBSEMFPhysicalSchemeLive physicalSchemeLive = masterTransactBSEMFPhysicalRepositoryLive.findOne(urnPhysical);
				clientSTPOrderRegistration.setMasterTransactBsemfphysicalSchemeLive(physicalSchemeLive);

				int urnToPhysical = (clientSTPDTO.getToSchemeUniqueNo());
				MasterTransactBSEMFPhysicalSchemeLive toPhysicalSchemeLive = masterTransactBSEMFPhysicalRepositoryLive.findOne(urnToPhysical);
				clientSTPOrderRegistration.setMasterTransactBsemfTophysicalSchemeLive(toPhysicalSchemeLive);
			} else {
				int urnPhysical = (clientSTPDTO.getFromSchemeUniqueNo());
				MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
				clientSTPOrderRegistration.setMasterTransactBsemfphysicalScheme1(physicalScheme);

				int urnToPhysical = (clientSTPDTO.getToSchemeUniqueNo());
				MasterTransactBSEMFPhysicalScheme toPhysicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnToPhysical);
				clientSTPOrderRegistration.setMasterTransactBsemfphysicalScheme2(toPhysicalScheme);
			}
			
			if (!clientSTPOrderRegistration.getTransactionMode().equals(FinexaConstant.PHYSICAL_MODE)) {
				clientSTPOrderRegistration.setTransactionMode(FinexaConstant.DEMAT_MODE);
				clientSTPDTO.setTransactionMode(FinexaConstant.DEMAT_MODE);
			}
			
			/*if (clientSTPDTO.getTransactionMode().equals( FinexaConstant.PHYSICAL_MODE)) {

				int urnPhysical = (clientSTPDTO.getFromSchemeUniqueNo());
				MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
				clientSTPOrderRegistration.setMasterTransactBsemfphysicalScheme1(physicalScheme);

				int urnToPhysical = (clientSTPDTO.getFromSchemeUniqueNo());
				MasterTransactBSEMFPhysicalScheme toPhysicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnToPhysical);
				clientSTPOrderRegistration.setMasterTransactBsemfphysicalScheme2(toPhysicalScheme);

			} else {

				int urnDemat = (clientSTPDTO.getFromSchemeUniqueNo());
				MasterTransactBSEMFDematScheme fromDematScheme = masterTransactBSEMFDematRepository.findOne(urnDemat);
				clientSTPOrderRegistration.setMasterTransactBsemfdematScheme1(fromDematScheme);

				int urnDematTo = (clientSTPDTO.getFromSchemeUniqueNo());
				MasterTransactBSEMFDematScheme toDematScheme = masterTransactBSEMFDematRepository.findOne(urnDematTo);
				clientSTPOrderRegistration.setMasterTransactBsemfdematScheme2(toDematScheme);
			}*/

			/*List<ClientSTPOrderRegistration> alreadySavedOrders = clientSTPRepository.
					getByClientCodeAndSaveModeAndPurchaseMode(clientSTPDTO.getClientCode(), FinexaConstant.DRAFT_MODE, 
							clientSTPOrderRegistration.getPurchaseMode());
			if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
				clientSTPOrderRegistration.setId(alreadySavedOrders.get(0).getId());
			}*/
			clientSTPOrderRegistration.setSaveMode(FinexaConstant.DRAFT_MODE);
			clientSTPOrderRegistration.setCreatedAt(new Date());
			try {
				clientSTPOrderRegistration = clientSTPRepository.save(clientSTPOrderRegistration);
				if (clientSTPOrderRegistration != null && 
						clientSTPOrderRegistration.getPurchaseMode().equals(FinexaConstant.PURCHASE_MODE_BUY)) {


					String pipeSeparatedString = "";
					pipeSeparatedString = joinPipeValues(clientSTPDTO.getClientCode(), pipeSeparatedString);
					String schemeCodeFrom = "";
					if (clientSTPOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
						schemeCodeFrom = clientSTPOrderRegistration.getMasterTransactBsemfphysicalSchemeLive().getSchemeCode();
					} else {
						schemeCodeFrom = clientSTPOrderRegistration.getMasterTransactBsemfphysicalScheme1().getSchemeCode();
					}
					
					String schemeCodeTo = "";
					if (clientSTPOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
						schemeCodeTo = clientSTPOrderRegistration.getMasterTransactBsemfTophysicalSchemeLive().getSchemeCode();
					} else {
						schemeCodeTo = clientSTPOrderRegistration.getMasterTransactBsemfphysicalScheme2().getSchemeCode();
					}
					
					
					pipeSeparatedString = joinPipeValues(schemeCodeFrom, pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(schemeCodeTo, pipeSeparatedString);

					/*if (clientSTPDTO.getTransactionMode().equals( FinexaConstant.PHYSICAL_MODE)) {

					}else if (clientSTPDTO.getTransactionMode().equals( FinexaConstant.DEMAT_MODE)) {
						pipeSeparatedString = joinPipeValues(clientSTPOrderRegistration.getMasterTransactBsemfdematScheme1().getAmcSchemeCode(), pipeSeparatedString);
						pipeSeparatedString = joinPipeValues(clientSTPOrderRegistration.getMasterTransactBsemfdematScheme2().getAmcSchemeCode(), pipeSeparatedString);

					}*/
					pipeSeparatedString = joinPipeValues(clientSTPDTO.getBuySellType(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientSTPDTO.getTransactionMode(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientSTPDTO.getFolioNo(), pipeSeparatedString);
					// Internal reference number to be left blank
					pipeSeparatedString = joinPipeValues("", pipeSeparatedString);
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String startDate = sdf.format(clientSTPDTO.getStartDate());

					pipeSeparatedString = joinPipeValues(startDate, pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientSTPDTO.getFrequencyType(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(("" + clientSTPDTO.getNoOfTransfers()), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientSTPDTO.getInstallmentAmount().toString(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientSTPDTO.getFirstOrderToday().toString(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientSTPDTO.getSubBrokerCode(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientSTPDTO.getEuinDeclaration(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientSTPDTO.getEuinNumber(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientSTPDTO.getRemarks(), pipeSeparatedString);
					pipeSeparatedString = joinPipeValues(clientSTPDTO.getSubBroker_ARN(), pipeSeparatedString);

					System.out.println(pipeSeparatedString);
					pipeSeparatedString = pipeSeparatedString.substring(0,pipeSeparatedString.length()-1);
					System.out.println(pipeSeparatedString);

					String bseUserId = clientSTPOrderRegistration.getAdvisorUser().getBseUsername();
					String bseMemberId = clientSTPOrderRegistration.getAdvisorUser().getBseMemberId();
					String bsePassword = decrypt(clientSTPOrderRegistration.getAdvisorUser().getBsePassword());
					String passKey = autoGeneratePassKey();

					MFUPLOADSoapService mFUPLOADSoapService = new MFUPLOADSoapService();
					clientUCCResultDTO = mFUPLOADSoapService.authenticateMFUploadService
							(bseUserId, bseMemberId, bsePassword, passKey, clientSTPOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId());

					if (clientUCCResultDTO.isStatus()) {
						// Bse call to authenticate password is successful,
						//Now upload the mandateDetails
						clientUCCResultDTO = mFUPLOADSoapService.fireMFAPIRequest(BSEConstant.MFAPI_STP_REGISTRATION, bseUserId, clientUCCResultDTO.getMessage(), pipeSeparatedString, clientSTPOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId());
						if (clientUCCResultDTO.isStatus()) {
							// ccCreation with BSE Successful. Save the result in table
							/*alreadySavedOrders = clientSTPRepository.
									getByClientCodeAndSaveModeAndPurchaseMode(clientSTPDTO.getClientCode(), FinexaConstant.DRAFT_MODE, 
											clientSTPOrderRegistration.getPurchaseMode());
							if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
								clientSTPOrderRegistration.setId(alreadySavedOrders.get(0).getId());
							}*/
							clientSTPOrderRegistration.setBseResponse(clientUCCResultDTO.getMessage());
							clientSTPOrderRegistration.setBseResponseCode("" +clientUCCResultDTO.getStatusCode());

							clientSTPOrderRegistration.setSaveMode(FinexaConstant.SUCCESS_MODE);
							clientSTPOrderRegistration = clientSTPRepository.save(clientSTPOrderRegistration);
						} else {
							// set in error mode
							clientSTPOrderRegistration.setSaveMode(FinexaConstant.ERROR_MODE);
							clientSTPOrderRegistration = clientSTPRepository.save(clientSTPOrderRegistration);
						}
					}

				} else {
					// else it is in cart. Afterwards it will  be processed from cart
					clientUCCResultDTO.setStatusCode(200);
					clientUCCResultDTO.setMessage("Order Successfully Added To Cart");

					// calculate the total cart count
					int cartCount = calculateTotalCartCount(clientSTPOrderRegistration.getClientCode());
					clientUCCResultDTO.setOptionalParam(""+cartCount);
				}
			} catch(Exception e) {}
		}
		return clientUCCResultDTO;

	}
	@Override
	public ClientUCCResultDTO redeemPurchaseOrder(ClientRedeemDTO redeemDTO) throws FinexaBussinessException {
		// TODO Auto-generated method stub

		ClientTransactRedeemOrder  clientTransactRedeemOrder = mapper.map(redeemDTO, ClientTransactRedeemOrder.class);
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String clientCode = redeemDTO.getClientCode();
		ClientUCCDetail clientUCCDetail = clientUCCRepository.findOne(clientCode);

		if (clientUCCDetail != null) {
			clientTransactRedeemOrder.setAdvisorUser(clientUCCDetail.getAdvisorUser());
			clientTransactRedeemOrder.setClientMaster(clientUCCDetail.getClientMaster());
			if (clientTransactRedeemOrder.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				int urnPhysical = (redeemDTO.getUniqueSchemeCode());
				MasterTransactBSEMFPhysicalSchemeLive physicalSchemeLive = masterTransactBSEMFPhysicalRepositoryLive.findOne(urnPhysical);
				clientTransactRedeemOrder.setMasterTransactBsemfphysicalSchemeLive(physicalSchemeLive);
			} else {
				int urnPhysical = (redeemDTO.getUniqueSchemeCode());
				MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
				clientTransactRedeemOrder.setMasterTransactBsemfphysicalScheme(physicalScheme);
			}
			/*if (redeemDTO.getDpTxn().equals( FinexaConstant.DEMAT_MODE)) {
				int urnDemat = (redeemDTO.getUniqueCodeDemat());
				MasterTransactBSEMFDematScheme dematScheme = masterTransactBSEMFDematRepository.findOne(urnDemat);
				clientTransactRedeemOrder.setMasterTransactBsemfdematScheme(dematScheme);
			} else {
				int urnPhysical = (redeemDTO.getUniqueCodePhysical());
				MasterTransactBSEMFPhysicalScheme physicalScheme = masterTransactBSEMFPhysicalRepository.findOne(urnPhysical);
				clientTransactRedeemOrder.setMasterTransactBsemfphysicalScheme(physicalScheme);
			}*/

			List<ClientTransactRedeemOrder> alreadySavedOrders = redeemRepository.getByClientCodeAndSaveMode(redeemDTO.getClientCode(), FinexaConstant.DRAFT_MODE);
			if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
				clientTransactRedeemOrder.setId(alreadySavedOrders.get(0).getId());
			}
			clientTransactRedeemOrder.setSaveMode(FinexaConstant.DRAFT_MODE);
			clientTransactRedeemOrder.setCreatedAt(new Date());
			String urn = generateURN(clientTransactRedeemOrder.getAdvisorUser().getBseMemberId());
			clientTransactRedeemOrder.setUrn(urn);
			clientTransactRedeemOrder.setTransactionCode("New");
			try {
				clientTransactRedeemOrder = redeemRepository.save(clientTransactRedeemOrder);
				if (clientTransactRedeemOrder != null) {
					if (clientTransactRedeemOrder.getPurchaseMode().equals(FinexaConstant.PURCHASE_MODE_BUY)) {
						clientUCCResultDTO = registerAndFireRedeemOrder(clientTransactRedeemOrder);
					}
				}
			} catch (Exception e) {
				clientUCCResultDTO.setStatusCode(500);
				clientUCCResultDTO.setMessage("Unable to save Record in database due to " + e.getMessage());
			}

			/*			clientTransactRedeemOrder = redeemRepository.save(clientTransactRedeemOrder);
			if (clientTransactRedeemOrder != null) {

				String bseUserId = clientTransactRedeemOrder.getAdvisorUser().getBseUsername();
				String bseMemberId = clientTransactRedeemOrder.getAdvisorUser().getBseMemberId();
				String bsePassword = decrypt(clientTransactRedeemOrder.getAdvisorUser().getBsePassword());
				String passKey = autoGeneratePassKey();

				MFOrderEntryService mFOrderEntryService = new MFOrderEntryService();
				clientUCCResultDTO = mFOrderEntryService.authenticateMFOrderEntryService(bseUserId, bseMemberId, bsePassword);

				if (clientUCCResultDTO.isStatus()) {
					// Bse call to authenticate password is successful,
					//Now upload the mandateDetails
					redeemDTO.setEncryptedPassword(clientUCCResultDTO.getMessage());
					clientUCCResultDTO = mFOrderEntryService.fireMFOrderEntryRedeemRequest(clientTransactRedeemOrder, redeemDTO);
					clientTransactRedeemOrder.setBseResponse(clientUCCResultDTO.getMessage());
					clientTransactRedeemOrder.setBseResponseCode("" + clientUCCResultDTO.getStatusCode());
					if (clientUCCResultDTO.isStatus()) {
						// ccCreation with BSE Successful. Save the result in table
						alreadySavedOrders = redeemRepository.getByClientCodeAndSaveMode(redeemDTO.getClientCode(), FinexaConstant.DRAFT_MODE);
						if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
							clientTransactRedeemOrder.setId(alreadySavedOrders.get(0).getId());
						}
						clientTransactRedeemOrder.setSaveMode(FinexaConstant.SUCCESS_MODE);
						clientTransactRedeemOrder = redeemRepository.save(clientTransactRedeemOrder);
					} else {
						// set in error mode
						clientTransactRedeemOrder.setSaveMode(FinexaConstant.ERROR_MODE);
						clientTransactRedeemOrder = redeemRepository.save(clientTransactRedeemOrder);

					}
				}

			}*/
		}
		return clientUCCResultDTO;
	}
	private ClientUCCResultDTO registerAndFireRedeemOrder(ClientTransactRedeemOrder clientTransactRedeemOrder) {
		// TODO Auto-generated method stub
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		ClientRedeemDTO redeemDTO = new ClientRedeemDTO();
		if (clientTransactRedeemOrder != null) {

			String bseUserId = clientTransactRedeemOrder.getAdvisorUser().getBseUsername();
			String bseMemberId = clientTransactRedeemOrder.getAdvisorUser().getBseMemberId();
			String bsePassword = decrypt(clientTransactRedeemOrder.getAdvisorUser().getBsePassword());
			String passKey = autoGeneratePassKey();

			MFOrderEntryService mFOrderEntryService = new MFOrderEntryService();
			clientUCCResultDTO = mFOrderEntryService.authenticateMFOrderEntryService(bseUserId, bseMemberId, bsePassword, clientTransactRedeemOrder.getAdvisorUser().getLookupTransactBseaccessMode().getId());

			if (clientUCCResultDTO.isStatus()) {
				// Bse call to authenticate password is successful,
				//Now upload the mandateDetails
				redeemDTO.setPassKey(passKey);
				redeemDTO.setEncryptedPassword(clientUCCResultDTO.getMessage());
				clientUCCResultDTO = mFOrderEntryService.fireMFOrderEntryRedeemRequest(clientTransactRedeemOrder, redeemDTO);
				clientTransactRedeemOrder.setBseResponse(clientUCCResultDTO.getMessage());
				String message = clientUCCResultDTO.getMessage().substring(0, (clientUCCResultDTO.getMessage().length() - 2));
				int lastIndexOfPipe = message.lastIndexOf("|");
				message = message.substring((lastIndexOfPipe + 1), message.length());
				clientUCCResultDTO.setMessage(message);
				clientTransactRedeemOrder.setBseResponseCode("" + clientUCCResultDTO.getStatusCode());
				if (clientUCCResultDTO.isStatus()) {
					// ccCreation with BSE Successful. Save the result in table
					/*List<PurchaseOrderEntryParam> alreadySavedOrders = purchaseOrderEntryParamRepository.
							getByClientCodeAndSaveModeAndPurchaseMode(purchaseOrderEntryParam.getClientCode(), 
									FinexaConstant.DRAFT_MODE, purchaseOrderEntryParam.getPurchaseMode());
					if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
						purchaseOrderEntryParam.setId(alreadySavedOrders.get(0).getId());
					}*/
					clientTransactRedeemOrder.setSaveMode(FinexaConstant.SUCCESS_MODE);
					clientTransactRedeemOrder = redeemRepository.save(clientTransactRedeemOrder);
				} else {
					// set in error mode
					clientTransactRedeemOrder.setSaveMode(FinexaConstant.ERROR_MODE);
					clientTransactRedeemOrder = redeemRepository.save(clientTransactRedeemOrder);

				}
			}
		}
		return clientUCCResultDTO;
	}
	@Override
	public List<LumpsumCartDTO> getLumpsumOrdersFromCart(String clientCode) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<LumpsumCartDTO> lumpsumOrdersDTOList = new ArrayList<>();
		List<PurchaseOrderEntryParam> purchaseOrders = purchaseOrderEntryParamRepository.
				getByClientCodeAndSaveModeAndPurchaseMode(clientCode, FinexaConstant.DRAFT_MODE, FinexaConstant.PURCHASE_MODE_CART);
		for (PurchaseOrderEntryParam obj : purchaseOrders) {

			LumpsumCartDTO cartObj = new LumpsumCartDTO();
			cartObj.setId(obj.getId());
			
			/******************************/
			if(obj.getMasterTransactBsemfphysicalScheme() != null){
				//Details from BSE Physical Scheme
				cartObj.setAmcName(obj.getMasterTransactBsemfphysicalScheme().getAmcCode());
				cartObj.setSchemeName(obj.getMasterTransactBsemfphysicalScheme().getSchemeName());
				cartObj.setSchemeType(obj.getMasterTransactBsemfphysicalScheme().getSchemeType());
			} else {
				//Details from BSE Physical Scheme Live
				cartObj.setAmcName(obj.getMasterTransactBsemfphysicalSchemeLive().getAmcCode());
				cartObj.setSchemeName(obj.getMasterTransactBsemfphysicalSchemeLive().getSchemeName());
				cartObj.setSchemeType(obj.getMasterTransactBsemfphysicalSchemeLive().getSchemeType());
			}
			/****************************/
			
			cartObj.setAmountInvested(obj.getAmount());
			cartObj.setUnits(obj.getQty());

			lumpsumOrdersDTOList.add(cartObj);
		}

		return lumpsumOrdersDTOList;
	}
	@Override
	public List<ProductRecommendationTransactDTO> getLastSavedProductRecommendation(int advsiorId, int clientId, int goalId, String module, String date) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		// =========== start Product recommendation=====================
		List<ProductRecommendationTransactDTO> productRecommendationDTOList = null;
		try {
			Date lastSavedDate = null;
			if (!date.equals(DATE_UNDEFINED)) {
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				lastSavedDate = sdf.parse(date);
			} else {
				lastSavedDate = advisorProductRecoRepository.getMaxDateOfSavedProductRecoGP(advsiorId, clientId, goalId, module);
			}
			if (lastSavedDate != null) {
				String productPlan = advisorProductRecoRepository.
						getLastSavedProductPlan(advsiorId, clientId, goalId, module,lastSavedDate);
				if (productPlan != "") {
					productRecommendationDTOList = new ObjectMapper().readValue(productPlan, new TypeReference<List<ProductRecommendationTransactDTO>>(){});
				}
			}
			return productRecommendationDTOList;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productRecommendationDTOList;
	}
	@Override
	public List<SIPCartDTO> getSIPOrdersFromCart(String clientCode) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<SIPCartDTO> sipOrdersDTOList = new ArrayList<>();
		List<XsipOrderEntryParam> sipOrders = sipOrderEntryParamRepository.
				getByClientCodeAndSaveModeAndPurchaseMode(clientCode, FinexaConstant.DRAFT_MODE, FinexaConstant.PURCHASE_MODE_CART);
		for (XsipOrderEntryParam obj : sipOrders) {

			SIPCartDTO cartObj = new SIPCartDTO();
			cartObj.setId(obj.getId());
			
			
			/******************************/
			if(obj.getMasterTransactBsemfphysicalScheme()!=null){
				//Details from BSE Physical Scheme
				cartObj.setAmcName(obj.getMasterTransactBsemfphysicalScheme().getAmcCode());
				cartObj.setFromSchemeName(obj.getMasterTransactBsemfphysicalScheme().getSchemeName());
				cartObj.setSchemeType(obj.getMasterTransactBsemfphysicalScheme().getSchemeType());
			} else {
				//Details from BSE Physical Scheme Live
				cartObj.setAmcName(obj.getMasterTransactBsemfphysicalSchemeLive().getAmcCode());
				cartObj.setFromSchemeName(obj.getMasterTransactBsemfphysicalSchemeLive().getSchemeName());
				cartObj.setSchemeType(obj.getMasterTransactBsemfphysicalSchemeLive().getSchemeType());
			}
			
			/******************************/
			
			cartObj.setRegType(obj.getRegType());
			cartObj.setInstallmentAmount(obj.getInstallmentAmount());
			cartObj.setNoOfInstallments(obj.getNoOfInstallment());
			cartObj.setFrequency(obj.getFrequencyType());

			sipOrdersDTOList.add(cartObj);
		}

		return sipOrdersDTOList;
	}
	@Override
	public List<SwitchCartDTO> getSwitchOrdersFromCart(String clientCode) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<SwitchCartDTO> switchOrdersDTOList = new ArrayList<>();
		List<ClientSwitchOrderEntryParam> switchOrders = clientSwitchOrderEntryParamRepository.
				getByClientCodeAndSaveModeAndPurchaseMode(clientCode, FinexaConstant.DRAFT_MODE, FinexaConstant.PURCHASE_MODE_CART);
		for (ClientSwitchOrderEntryParam obj : switchOrders) {

			SwitchCartDTO cartObj = new SwitchCartDTO();
			cartObj.setId(obj.getId());
			
			/*******************************/
			if(obj.getMasterTransactBsemfphysicalScheme1()!=null){
				//Details from BSE Physical Scheme 
				cartObj.setAmcName(obj.getMasterTransactBsemfphysicalScheme1().getAmcCode());
				cartObj.setFromSchemeName(obj.getMasterTransactBsemfphysicalScheme1().getSchemeName());
				cartObj.setFromSchemeType(obj.getMasterTransactBsemfphysicalScheme1().getSchemeType());

				cartObj.setToSchemeName(obj.getMasterTransactBsemfphysicalScheme2().getSchemeName());
				cartObj.setToSchemeType(obj.getMasterTransactBsemfphysicalScheme2().getSchemeType());
			} else {
				//Details from BSE Physical Scheme Live
				cartObj.setAmcName(obj.getMasterTransactBsemfphysicalSchemeLive().getAmcCode());
				cartObj.setFromSchemeName(obj.getMasterTransactBsemfphysicalSchemeLive().getSchemeName());
				cartObj.setFromSchemeType(obj.getMasterTransactBsemfphysicalSchemeLive().getSchemeType());

				cartObj.setToSchemeName(obj.getMasterTransactBsemfTophysicalSchemeLive().getSchemeName());
				cartObj.setToSchemeType(obj.getMasterTransactBsemfTophysicalSchemeLive().getSchemeType());
			}
			
			
			/******************************/
			
			cartObj.setAmountInvested(obj.getAmount());
			cartObj.setUnits(obj.getUnits());

			switchOrdersDTOList.add(cartObj);
		}

		return switchOrdersDTOList;
	}
	@Override
	public List<SWPCartDTO> getSWPOrdersFromCart(String clientCode) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<SWPCartDTO> swpOrdersDTOList = new ArrayList<>();
		List<ClientSWPOrderRegistration> swpOrders = clientSWPRepository.
				getByClientCodeAndSaveModeAndPurchaseMode(clientCode, FinexaConstant.DRAFT_MODE, FinexaConstant.PURCHASE_MODE_CART);
		for (ClientSWPOrderRegistration obj : swpOrders) {

			SWPCartDTO cartObj = new SWPCartDTO();
			cartObj.setId(obj.getId());
			
			
			/********************************/
			if(obj.getMasterTransactBsemfphysicalScheme()!=null){
				//Details from BSE Physical Scheme
				cartObj.setAmcName(obj.getMasterTransactBsemfphysicalScheme().getAmcCode());
				cartObj.setSchemeName(obj.getMasterTransactBsemfphysicalScheme().getSchemeName());
				cartObj.setSchemeType(obj.getMasterTransactBsemfphysicalScheme().getSchemeType());
			} else {
				//Details from BSE Physical Scheme Live
				cartObj.setAmcName(obj.getMasterTransactBsemfphysicalSchemeLive().getAmcCode());
				cartObj.setSchemeName(obj.getMasterTransactBsemfphysicalSchemeLive().getSchemeName());
				cartObj.setSchemeType(obj.getMasterTransactBsemfphysicalSchemeLive().getSchemeType());
			}
			/*******************************/
			
			
			cartObj.setWithdrawalAmt(obj.getInstallmentAmount());
			cartObj.setWithdrawalunits(obj.getInstallmentUnit());
			cartObj.setNoOfInstallment(obj.getNoOfWithDrawals());
			cartObj.setFrequencyType(obj.getFrequencyType());

			swpOrdersDTOList.add(cartObj);
		}

		return swpOrdersDTOList;
	}
	@Override
	public List<STPCartDTO> getSTPOrdersFromCart(String clientCode) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<STPCartDTO> stpOrdersDTOList = new ArrayList<>();
		List<ClientSTPOrderRegistration> stpOrders = clientSTPRepository.
				getByClientCodeAndSaveModeAndPurchaseMode(clientCode, FinexaConstant.DRAFT_MODE, FinexaConstant.PURCHASE_MODE_CART);
		for (ClientSTPOrderRegistration obj : stpOrders) {

			STPCartDTO cartObj = new STPCartDTO();
			cartObj.setId(obj.getId());
			
			
			/***********************************/
			if(obj.getMasterTransactBsemfphysicalScheme1() != null){
				//Details from BSE Physical Scheme
				cartObj.setAmcName(obj.getMasterTransactBsemfphysicalScheme1().getAmcCode());
				cartObj.setFromSchemeName(obj.getMasterTransactBsemfphysicalScheme1().getSchemeName());
				cartObj.setFromSchemeType(obj.getMasterTransactBsemfphysicalScheme1().getSchemeType());
				
				cartObj.setToSchemeName(obj.getMasterTransactBsemfphysicalScheme2().getSchemeName());
				cartObj.setToSchemeType(obj.getMasterTransactBsemfphysicalScheme2().getSchemeType());
			} else {
				//Details from BSE Physical Scheme Live
				cartObj.setAmcName(obj.getMasterTransactBsemfphysicalSchemeLive().getAmcCode());
				cartObj.setFromSchemeName(obj.getMasterTransactBsemfphysicalSchemeLive().getSchemeName());
				cartObj.setFromSchemeType(obj.getMasterTransactBsemfphysicalSchemeLive().getSchemeType());
				
				cartObj.setToSchemeName(obj.getMasterTransactBsemfTophysicalSchemeLive().getSchemeName());
				cartObj.setToSchemeType(obj.getMasterTransactBsemfTophysicalSchemeLive().getSchemeType());
			}
			
			/**********************************/
			
			cartObj.setNoOfInstallment(obj.getNoOfTransfers());
			cartObj.setTransferAmount(obj.getInstallmentAmount());
			cartObj.setFrequencyType(obj.getFrequencyType());

			//			cartObj.setUnits(obj.ge);

			stpOrdersDTOList.add(cartObj);
		}

		return stpOrdersDTOList;
	}
	@Override
	public ClientUCCResultDTO getOrdersFromCart(String clientCode) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		// calculate the total cart count
		try {
			int cartCount = calculateTotalCartCount(clientCode);
			clientUCCResultDTO.setOptionalParam("" + cartCount);
			clientUCCResultDTO.setStatusCode(200);
			clientUCCResultDTO.setStatus(true);
			clientUCCResultDTO.setMessage("Order Count Successful");
		} catch (Exception e) {
			clientUCCResultDTO.setOptionalParam("" + 0);
			clientUCCResultDTO.setStatusCode(500);
			clientUCCResultDTO.setStatus(false);
			clientUCCResultDTO.setMessage("Order Count Unsuccessful");
		}


		return clientUCCResultDTO;
	}
	@Override
	public List<ClientUCCResultDTO> invest(List<InvestDTO> investDTOList) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<ClientUCCResultDTO> uccResultDTOList = new ArrayList();
		if (investDTOList != null) {
			AdvisorUser advUser = advisorUserRepository.findOne(investDTOList.get(0).getAdvisorID());
			for (InvestDTO obj : investDTOList) {
				if (obj.getIdTransMode() != null && !obj.getIdTransMode().equals("")) {
					if (obj.getModeOfInvestment().equals("1")) {
						// Lumpsum
						ClientUCCResultDTO singleResult = new ClientUCCResultDTO();
						PurchaseOrderEntryParamDTO orderEntryDTO = new PurchaseOrderEntryParamDTO();
						orderEntryDTO.setClientCode(obj.getClientCode());
						orderEntryDTO.setAdvisorID(new BigInteger("" + obj.getAdvisorID()));
						orderEntryDTO.setClientID(new BigInteger("" + obj.getClientID()));
						// set the scheme code
						String transMode = obj.getIdTransMode();
						if (transMode.equals("N") || transMode.equals("C")) {
							transMode = MFTransactConstant.ONLY_DEMAT;
						} else {
							transMode = MFTransactConstant.ONLY_PHYSICAL;
						}
						if (advUser.getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
							List<MasterTransactBSEMFPhysicalScheme> bseSchemeList = masterTransactBSEMFPhysicalRepository.findByIsinAndPurchaseTransactionModeOrIsinAndPurchaseTransactionMode
									(obj.getProductIsin(), MFTransactConstant.ONLY_DEMAT_PHYSICAL, obj.getProductIsin(), transMode);
							if (bseSchemeList != null && bseSchemeList.size() > 0) {
								String suffix1 = "";
								String suffix2 = "";
								if (Integer.parseInt(obj.getLumpsumAmt()) > 200000) {
									suffix1 = suffix1 + "L0";
									suffix2 = suffix2 + "L1";
								}
								for (MasterTransactBSEMFPhysicalScheme scheme : bseSchemeList) {
									if (suffix1.length() > 0) {
										if (scheme.getSchemeCode().endsWith(suffix1) || scheme.getSchemeCode().endsWith(suffix2)) {
											orderEntryDTO.setUniqueSchemeCode(scheme.getUniqueNo());
											break;
										}
									} else {
										if (!scheme.getSchemeCode().endsWith("L0") && !scheme.getSchemeCode().endsWith("L1")) {
											orderEntryDTO.setUniqueSchemeCode(scheme.getUniqueNo());
											break;
										}
									}

								}
							} else {
								singleResult.setMessage("Scheme Not Found");
								singleResult.setStatus(false);	
								singleResult.setStatusCode(BSEConstant.BSE_ORDER_PLACED_FAILURE);
								singleResult.setOptionalParam("" + obj.getRowCount());
								uccResultDTOList.add(singleResult);
								continue;
							}
						} else {
							List<MasterTransactBSEMFPhysicalSchemeLive> bseSchemeList = masterTransactBSEMFPhysicalRepositoryLive.findByIsinAndPurchaseTransactionModeOrIsinAndPurchaseTransactionMode
									(obj.getProductIsin(), MFTransactConstant.ONLY_DEMAT_PHYSICAL, obj.getProductIsin(), transMode);
								if (bseSchemeList != null && bseSchemeList.size() > 0) {
								String suffix1 = "";
								String suffix2 = "";
								if (Integer.parseInt(obj.getLumpsumAmt()) > 200000) {
									suffix1 = suffix1 + "L0";
									suffix2 = suffix2 + "L1";
								}
								for (MasterTransactBSEMFPhysicalSchemeLive scheme : bseSchemeList) {
									if (suffix1.length() > 0) {
										if (scheme.getSchemeCode().endsWith(suffix1) || scheme.getSchemeCode().endsWith(suffix2)) {
											orderEntryDTO.setUniqueSchemeCode(scheme.getUniqueNo());
											break;
										}
									} else {
										if (!scheme.getSchemeCode().endsWith("L0") && !scheme.getSchemeCode().endsWith("L1")) {
											orderEntryDTO.setUniqueSchemeCode(scheme.getUniqueNo());
											break;
										}
									}

								}
							} else {
								singleResult.setMessage("Scheme Not Found");
								singleResult.setStatus(false);	
								singleResult.setStatusCode(BSEConstant.BSE_ORDER_PLACED_FAILURE);
								singleResult.setOptionalParam("" + obj.getRowCount());
								uccResultDTOList.add(singleResult);
								continue;
							}
						}
						
						orderEntryDTO.setAmount(new BigInteger(obj.getLumpsumAmt()));
						orderEntryDTO.setDpTxn(obj.getIdTransMode());
						orderEntryDTO.setBuySellType("Fresh");

						orderEntryDTO.setAllRedeem("N");
						orderEntryDTO.setMinRedeem("N");
						orderEntryDTO.setBuySell("P");
						orderEntryDTO.setKycStatus("Y");
						orderEntryDTO.setDpc("N");
						orderEntryDTO.setPurchaseMode("B");
						//orderEntryDTO.setFolioNo("12345");
						//orderEntryDTO.setEuin("E12345");
						orderEntryDTO.setEuinFlag("N");
						singleResult = registerPurchaseOrder(orderEntryDTO);
						singleResult.setOptionalParam("" + obj.getRowCount());
						uccResultDTOList.add(singleResult);

					} else {
						//SIP

						ClientUCCResultDTO singleResult = new ClientUCCResultDTO();
						SIPOrderEntryParamDTO sipDTO = new SIPOrderEntryParamDTO();
						sipDTO.setClientCode(obj.getClientCode());
						sipDTO.setAdvisorId(obj.getAdvisorID());
						sipDTO.setClientId(obj.getClientID());
						// set the scheme code
						String transMode = obj.getIdTransMode();
						if (transMode.equals("N") || transMode.equals("C")) {
							transMode = "D";
						} else {
							transMode = "P";
						}
						if (advUser.getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
							List<MasterTransactBSEMFPhysicalScheme> bseSchemeList = masterTransactBSEMFPhysicalRepository.findByIsinAndPurchaseTransactionModeOrIsinAndPurchaseTransactionMode
									(obj.getProductIsin(), MFTransactConstant.ONLY_DEMAT_PHYSICAL, obj.getProductIsin(), transMode);
								if (bseSchemeList != null && bseSchemeList.size() > 0) {
								String suffix1 = "";
								String suffix2 = "";
								if (Integer.parseInt(obj.getLumpsumAmt()) > 200000) {
									suffix1 = suffix1 + "L0";
									suffix2 = suffix2 + "L1";
								}
								for (MasterTransactBSEMFPhysicalScheme scheme : bseSchemeList) {
									if (suffix1.length() > 0) {
										if (scheme.getSchemeCode().endsWith(suffix1) || scheme.getSchemeCode().endsWith(suffix2)) {
											sipDTO.setUniqueSchemeCode(scheme.getUniqueNo());
											break;
										}
									} else {
										if (!scheme.getSchemeCode().endsWith("L0") && !scheme.getSchemeCode().endsWith("L1")) {
											sipDTO.setUniqueSchemeCode(scheme.getUniqueNo());
											break;
										}
									}

								}
							} else {
								singleResult.setMessage("Scheme Not Found");
								singleResult.setStatus(false);
								singleResult.setStatusCode(BSEConstant.BSE_ORDER_PLACED_FAILURE);
								singleResult.setOptionalParam("" + obj.getRowCount());
								uccResultDTOList.add(singleResult);
								continue;
							}
						} else {
							List<MasterTransactBSEMFPhysicalSchemeLive> bseSchemeList = masterTransactBSEMFPhysicalRepositoryLive.findByIsinAndPurchaseTransactionModeOrIsinAndPurchaseTransactionMode
									(obj.getProductIsin(), MFTransactConstant.ONLY_DEMAT_PHYSICAL, obj.getProductIsin(), transMode);
								if (bseSchemeList != null && bseSchemeList.size() > 0) {
								String suffix1 = "";
								String suffix2 = "";
								if (Integer.parseInt(obj.getLumpsumAmt()) > 200000) {
									suffix1 = suffix1 + "L0";
									suffix2 = suffix2 + "L1";
								}
								for (MasterTransactBSEMFPhysicalSchemeLive scheme : bseSchemeList) {
									if (suffix1.length() > 0) {
										if (scheme.getSchemeCode().endsWith(suffix1) || scheme.getSchemeCode().endsWith(suffix2)) {
											sipDTO.setUniqueSchemeCode(scheme.getUniqueNo());
											break;
										}
									} else {
										if (!scheme.getSchemeCode().endsWith("L0") && !scheme.getSchemeCode().endsWith("L1")) {
											sipDTO.setUniqueSchemeCode(scheme.getUniqueNo());
											break;
										}
									}

								}
							} else {
								singleResult.setMessage("Scheme Not Found");
								singleResult.setStatus(false);
								singleResult.setStatusCode(BSEConstant.BSE_ORDER_PLACED_FAILURE);
								singleResult.setOptionalParam("" + obj.getRowCount());
								uccResultDTOList.add(singleResult);
								continue;
							}
						}
						
						int sipAmt = (int)(Double.parseDouble(obj.getSipAmt()));
						sipDTO.setInstallmentAmount(new BigInteger("" + sipAmt));
						sipDTO.setTransMode(transMode);
						sipDTO.setDpTransactionMode((obj.getIdTransMode()));
						sipDTO.setFrequencyType(obj.getSipFreq());
						sipDTO.setNoOfInstallment(Long.parseLong(obj.getSipTenure()));
						sipDTO.setFirstOrderFlag("Y");
						sipDTO.setXsipMandateid(obj.getMandateType());
						sipDTO.setDpc("N");
						sipDTO.setPurchaseMode("B");
						sipDTO.setRegType(obj.getRegType());
						sipDTO.setStartDate(obj.getStartDate());
						//sipDTO.setFolioNo("12345");
						//sipDTO.setEuin("E12345");
						sipDTO.setEuinFlag("N");
						singleResult = registerSIPOrder(sipDTO);
						singleResult.setOptionalParam("" + obj.getRowCount());
						uccResultDTOList.add(singleResult);

					}
				}
			}
		}


		return uccResultDTOList;
	}

	@Override
	public MasterBankNameIFSCCodeDTO getBankDetailsByIFSC(String ifsc) throws FinexaBussinessException {
		// TODO Auto-generated method stub

		MasterBankNameIFSCCodeDTO masterBankNameIFSCCodeDTO = new MasterBankNameIFSCCodeDTO();

		try{
			MasterBankNameIFSCCode masterBankNameIFSCCode = masterBankNameIFSCCodeRepository.findByIfsc(ifsc);
			if (masterBankNameIFSCCode != null) {
				masterBankNameIFSCCodeDTO = mapper.map(masterBankNameIFSCCode, MasterBankNameIFSCCodeDTO.class);
			}
		}
		catch(Exception e){
			System.out.println("Exception in Controller : "+e);
		}


		return masterBankNameIFSCCodeDTO;

	}
	@Override
	public List<BankNameDTO> getBankList(String clientCode) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<BankNameDTO> bankList = new ArrayList<>();
		List<String> bankNameList = new ArrayList<>();
		List<String> ifscCodeTotal = new ArrayList<>();
		try {
			ClientUCCDetail uccDetail = clientUCCRepository.findOne(clientCode);
			BankNameDTO bankNameDTO = new BankNameDTO();
			List<IFSCCodeDTO> ifscList = new ArrayList<>();
			// Getting the first Bank Details
			if (uccDetail.getClientIfscCode1() != null) {
				MasterBankNameIFSCCode bankIfsc = masterBankNameIFSCCodeRepository.findByIfsc(uccDetail.getClientIfscCode1());

				if (bankIfsc != null) {
					bankNameList.add(bankIfsc.getBank());
					bankNameDTO.setBankName(bankIfsc.getBank());
					IFSCCodeDTO ifscDto = new IFSCCodeDTO();
					List<String> accNo = new ArrayList<>();
					accNo.add("" + uccDetail.getClientAccNo1());
					ifscDto.setAccountNo(accNo);
					ifscDto.setIfscCode(uccDetail.getClientIfscCode1());
					ifscCodeTotal.add(uccDetail.getClientIfscCode1());
					ifscList.add(ifscDto);
					bankNameDTO.setIfscCodeList(ifscList);
					bankList.add(bankNameDTO);
				}

			}

			// Getting the second Bank Details
			if (uccDetail.getClientIfscCode2() != null) {
				MasterBankNameIFSCCode bankIfsc = masterBankNameIFSCCodeRepository.findByIfsc(uccDetail.getClientIfscCode2());
				if (bankIfsc != null) {
					if (bankNameList.contains(bankIfsc.getBank())) {
						for (BankNameDTO obj : bankList) {
							if (obj.getBankName().equals(bankIfsc.getBank())) {
								List<IFSCCodeDTO> ifscDTOList = obj.getIfscCodeList();
								if (ifscDTOList != null) {
									int ifscFlag = 0;
									for (IFSCCodeDTO ifscDto : ifscDTOList) {
										if (ifscDto != null) {
											if (ifscCodeTotal.contains(ifscDto.getIfscCode())) {
												ifscFlag = 1;
												ifscDto.getAccountNo().add(uccDetail.getClientAccNo2());
											}
										}
									}
									if (ifscFlag == 0) {
										// 0 implies that IFSC Code is not present previously
										IFSCCodeDTO ifscDtoNew = new IFSCCodeDTO();
										List<String> accNo = new ArrayList<>();
										accNo.add("" + uccDetail.getClientAccNo2());
										ifscDtoNew.setAccountNo(accNo);
										ifscDtoNew.setIfscCode(uccDetail.getClientIfscCode2());
										ifscDTOList.add(ifscDtoNew);
									}
								}
							}
						}
					} else {
						bankNameList.add(bankIfsc.getBank());
						bankNameDTO.setBankName(bankIfsc.getBank());
						IFSCCodeDTO ifscDto = new IFSCCodeDTO();
						List<String> accNo = new ArrayList<>();
						accNo.add("" + uccDetail.getClientAccNo2());
						ifscDto.setAccountNo(accNo);
						ifscDto.setIfscCode(uccDetail.getClientIfscCode2());
						ifscList.add(ifscDto);
						bankNameDTO.setIfscCodeList(ifscList);
						bankList.add(bankNameDTO);
					}
				}
			}

			// Getting the third Bank Details
			if (uccDetail.getClientIfscCode3() != null) {
				MasterBankNameIFSCCode bankIfsc = masterBankNameIFSCCodeRepository.findByIfsc(uccDetail.getClientIfscCode3());
				if (bankIfsc != null) {
					if (bankNameList.contains(bankIfsc.getBank())) {
						for (BankNameDTO obj : bankList) {
							if (obj.getBankName().equals(bankIfsc.getBank())) {
								List<IFSCCodeDTO> ifscDTOList = obj.getIfscCodeList();
								if (ifscDTOList != null) {
									int ifscFlag = 0;
									for (IFSCCodeDTO ifscDto : ifscDTOList) {
										if (ifscDto != null) {
											if (ifscCodeTotal.contains(ifscDto.getIfscCode())) {
												ifscFlag = 1;
												ifscDto.getAccountNo().add(uccDetail.getClientAccNo3());
											}
										}
									}
									if (ifscFlag == 0) {
										// 0 implies that IFSC Code is not present previously
										IFSCCodeDTO ifscDtoNew = new IFSCCodeDTO();
										List<String> accNo = new ArrayList<>();
										accNo.add("" + uccDetail.getClientAccNo3());
										ifscDtoNew.setAccountNo(accNo);
										ifscDtoNew.setIfscCode(uccDetail.getClientIfscCode3());
										ifscDTOList.add(ifscDtoNew);
									}
								}
							}
						}
					} else {
						bankNameList.add(bankIfsc.getBank());
						bankNameDTO.setBankName(bankIfsc.getBank());
						IFSCCodeDTO ifscDto = new IFSCCodeDTO();
						List<String> accNo = new ArrayList<>();
						accNo.add("" + uccDetail.getClientAccNo3());
						ifscDto.setAccountNo(accNo);
						ifscDto.setIfscCode(uccDetail.getClientIfscCode3());
						ifscList.add(ifscDto);
						bankNameDTO.setIfscCodeList(ifscList);
						bankList.add(bankNameDTO);
					}
				}
			}

			// Getting the fourth Bank Details
			if (uccDetail.getClientIfscCode4() != null) {
				MasterBankNameIFSCCode bankIfsc = masterBankNameIFSCCodeRepository.findByIfsc(uccDetail.getClientIfscCode4());
				if (bankIfsc != null) {
					if (bankNameList.contains(bankIfsc.getBank())) {
						for (BankNameDTO obj : bankList) {
							if (obj.getBankName().equals(bankIfsc.getBank())) {
								List<IFSCCodeDTO> ifscDTOList = obj.getIfscCodeList();
								if (ifscDTOList != null) {
									int ifscFlag = 0;
									for (IFSCCodeDTO ifscDto : ifscDTOList) {
										if (ifscDto != null) {
											if (ifscCodeTotal.contains(ifscDto.getIfscCode())) {
												ifscFlag = 1;
												ifscDto.getAccountNo().add(uccDetail.getClientAccNo4());
											}
										}
									}
									if (ifscFlag == 0) {
										// 0 implies that IFSC Code is not present previously
										IFSCCodeDTO ifscDtoNew = new IFSCCodeDTO();
										List<String> accNo = new ArrayList<>();
										accNo.add("" + uccDetail.getClientAccNo4());
										ifscDtoNew.setAccountNo(accNo);
										ifscDtoNew.setIfscCode(uccDetail.getClientIfscCode4());
										ifscDTOList.add(ifscDtoNew);
									}
								}
							}
						}
					} else {
						bankNameList.add(bankIfsc.getBank());
						bankNameDTO.setBankName(bankIfsc.getBank());
						IFSCCodeDTO ifscDto = new IFSCCodeDTO();
						List<String> accNo = new ArrayList<>();
						accNo.add("" + uccDetail.getClientAccNo4());
						ifscDto.setAccountNo(accNo);
						ifscDto.setIfscCode(uccDetail.getClientIfscCode4());
						ifscList.add(ifscDto);
						bankNameDTO.setIfscCodeList(ifscList);
						bankList.add(bankNameDTO);
					}
				}
			}

			// Getting the fifth Bank Details
			if (uccDetail.getClientIfscCode5() != null) {
				MasterBankNameIFSCCode bankIfsc = masterBankNameIFSCCodeRepository.findByIfsc(uccDetail.getClientIfscCode5());
				if (bankIfsc != null) {
					if (bankNameList.contains(bankIfsc.getBank())) {
						for (BankNameDTO obj : bankList) {
							if (obj.getBankName().equals(bankIfsc.getBank())) {
								List<IFSCCodeDTO> ifscDTOList = obj.getIfscCodeList();
								if (ifscDTOList != null) {
									int ifscFlag = 0;
									for (IFSCCodeDTO ifscDto : ifscDTOList) {
										if (ifscDto != null) {
											if (ifscCodeTotal.contains(ifscDto.getIfscCode())) {
												ifscFlag = 1;
												ifscDto.getAccountNo().add(uccDetail.getClientAccNo5());
											}
										}
									}
									if (ifscFlag == 0) {
										// 0 implies that IFSC Code is not present previously
										IFSCCodeDTO ifscDtoNew = new IFSCCodeDTO();
										List<String> accNo = new ArrayList<>();
										accNo.add("" + uccDetail.getClientAccNo5());
										ifscDtoNew.setAccountNo(accNo);
										ifscDtoNew.setIfscCode(uccDetail.getClientIfscCode5());
										ifscDTOList.add(ifscDtoNew);
									}
								}
							}
						}
					} else {
						bankNameList.add(bankIfsc.getBank());
						bankNameDTO.setBankName(bankIfsc.getBank());
						IFSCCodeDTO ifscDto = new IFSCCodeDTO();
						List<String> accNo = new ArrayList<>();
						accNo.add("" + uccDetail.getClientAccNo5());
						ifscDto.setAccountNo(accNo);
						ifscDto.setIfscCode(uccDetail.getClientIfscCode5());
						ifscList.add(ifscDto);
						bankNameDTO.setIfscCodeList(ifscList);
						bankList.add(bankNameDTO);
					}
				}
			}

		} catch (Exception e) {

		}
		return bankList;
	}
	@Override
	public List<CartOrderStatusDTO> placeLumpsumOrdersOfCart(int[] lumpsumOrders) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<CartOrderStatusDTO> cartOrderStatusList = new ArrayList();
		for (int orderIndex = 0; orderIndex < lumpsumOrders.length; orderIndex ++) {
			try {
				PurchaseOrderEntryParam purchase = purchaseOrderEntryParamRepository.findOne(new BigInteger("" + lumpsumOrders[orderIndex]));
				ClientUCCResultDTO resultDTO = registerAndFirePurchaseOrder(purchase);
				purchase.setPurchaseMode("B");
				purchase = purchaseOrderEntryParamRepository.save(purchase);
				CartOrderStatusDTO obj = new CartOrderStatusDTO();
				obj.setId(purchase.getId().intValue());
				obj.setOrderStatus(resultDTO.getMessage());
				//if (resultDTO.isStatus()) {
					if (purchase.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
						obj.setSchemeName(purchase.getMasterTransactBsemfphysicalSchemeLive().getSchemeName());
						obj.setIsin(purchase.getMasterTransactBsemfphysicalSchemeLive().getIsin());
					} else {
						obj.setSchemeName(purchase.getMasterTransactBsemfphysicalScheme().getSchemeName());
						obj.setIsin(purchase.getMasterTransactBsemfphysicalScheme().getIsin());
					}
				//}
				
				cartOrderStatusList.add(obj);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return cartOrderStatusList;
	}
	@Override
	public List<CartOrderStatusDTO> placeSipOrdersOfCart(int[] sipOrders) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<CartOrderStatusDTO> cartOrderStatusList = new ArrayList();
		for (int orderIndex = 0; orderIndex < sipOrders.length; orderIndex ++) {
			try {
				XsipOrderEntryParam sipOrderEntry  = sipOrderEntryParamRepository.findOne(new BigInteger("" + sipOrders[orderIndex]));
				ClientUCCResultDTO resultDTO = registerAndFireSIPOrder(sipOrderEntry);
				sipOrderEntry.setPurchaseMode("B");
				sipOrderEntry = sipOrderEntryParamRepository.save(sipOrderEntry);
				CartOrderStatusDTO obj = new CartOrderStatusDTO();
				obj.setId(sipOrderEntry.getId().intValue());
				obj.setOrderStatus(resultDTO.getMessage());
				if (sipOrderEntry.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
					obj.setSchemeName(sipOrderEntry.getMasterTransactBsemfphysicalSchemeLive().getSchemeName());
					obj.setIsin(sipOrderEntry.getMasterTransactBsemfphysicalSchemeLive().getIsin());
				} else {
					obj.setSchemeName(sipOrderEntry.getMasterTransactBsemfphysicalScheme().getSchemeName());
					obj.setIsin(sipOrderEntry.getMasterTransactBsemfphysicalScheme().getIsin());
				}
				
				cartOrderStatusList.add(obj);

			} catch (Exception e) {

			}

		}
		return cartOrderStatusList;
	}
	@Override
	public List<CartOrderStatusDTO> placeSwitchOrdersOfCart(int[] switchOrders) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<CartOrderStatusDTO> cartOrderStatusList = new ArrayList();
		for (int orderIndex = 0; orderIndex < switchOrders.length; orderIndex ++) {
			try {
				ClientSwitchOrderEntryParam switchOrderEntryParam  = clientSwitchOrderEntryParamRepository.findOne(new BigInteger("" + switchOrders[orderIndex]));
				ClientUCCResultDTO resultDTO = registerAndFireSwitchOrder(switchOrderEntryParam);
				switchOrderEntryParam.setPurchaseMode("B");
				switchOrderEntryParam = clientSwitchOrderEntryParamRepository.save(switchOrderEntryParam);
				CartOrderStatusDTO obj = new CartOrderStatusDTO();
				obj.setId(switchOrderEntryParam.getId().intValue());
				obj.setOrderStatus(resultDTO.getMessage());
				if (switchOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
					obj.setSchemeName("From Scheme "+ switchOrderEntryParam.getMasterTransactBsemfphysicalSchemeLive().getSchemeName() + " To Scheme " + switchOrderEntryParam.getMasterTransactBsemfTophysicalSchemeLive().getSchemeName());
				} else {
					obj.setSchemeName("From Scheme "+ switchOrderEntryParam.getMasterTransactBsemfphysicalScheme1().getSchemeName() + " To Scheme " + switchOrderEntryParam.getMasterTransactBsemfphysicalScheme2().getSchemeName());
					//obj.setIsin(switchOrderEntryParam.getMasterTransactBsemfphysicalScheme().getIsin());
				}
				cartOrderStatusList.add(obj);

			} catch (Exception e) {

			}

		}
		return cartOrderStatusList;
	}
	@Override
	public List<CartOrderStatusDTO> placeStpOrdersOfCart(int[] stpOrders) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<CartOrderStatusDTO> cartOrderStatusList = new ArrayList();
		for (int orderIndex = 0; orderIndex < stpOrders.length; orderIndex ++) {
			try {
				ClientSTPOrderRegistration stpOrderRegistration  = clientSTPRepository.findOne(new BigInteger("" + stpOrders[orderIndex]));
				String pipeSeparatedString = "";
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getClientCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getMasterTransactBsemfphysicalScheme1().getSchemeCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getMasterTransactBsemfphysicalScheme2().getSchemeCode(), pipeSeparatedString);

				/*if (clientSTPDTO.getTransactionMode().equals( FinexaConstant.PHYSICAL_MODE)) {

					}else if (clientSTPDTO.getTransactionMode().equals( FinexaConstant.DEMAT_MODE)) {
						pipeSeparatedString = joinPipeValues(clientSTPOrderRegistration.getMasterTransactBsemfdematScheme1().getAmcSchemeCode(), pipeSeparatedString);
						pipeSeparatedString = joinPipeValues(clientSTPOrderRegistration.getMasterTransactBsemfdematScheme2().getAmcSchemeCode(), pipeSeparatedString);

					}*/
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getBuySellType(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getTransactionMode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getFolioNo(), pipeSeparatedString);
				// Internal reference number to be left blank
				pipeSeparatedString = joinPipeValues("", pipeSeparatedString);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String startDate = sdf.format(stpOrderRegistration.getStartDate());

				pipeSeparatedString = joinPipeValues(startDate, pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getFrequencyType(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(("" + stpOrderRegistration.getNoOfTransfers()), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getInstallmentAmount().toString(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getFirstOrderToday().toString(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getSubBrokerCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getEuinDeclaration(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getEuinNumber(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getRemarks(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(stpOrderRegistration.getSubBroker_ARN(), pipeSeparatedString);

				System.out.println(pipeSeparatedString);
				pipeSeparatedString = pipeSeparatedString.substring(0,pipeSeparatedString.length()-1);
				System.out.println(pipeSeparatedString);

				String bseUserId = stpOrderRegistration.getAdvisorUser().getBseUsername();
				String bseMemberId = stpOrderRegistration.getAdvisorUser().getBseMemberId();
				String bsePassword = decrypt(stpOrderRegistration.getAdvisorUser().getBsePassword());
				String passKey = autoGeneratePassKey();

				MFUPLOADSoapService mFUPLOADSoapService = new MFUPLOADSoapService();
				ClientUCCResultDTO clientUCCResultDTO = mFUPLOADSoapService.authenticateMFUploadService
						(bseUserId, bseMemberId, bsePassword, passKey, stpOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId());

				if (clientUCCResultDTO.isStatus()) {
					// Bse call to authenticate password is successful,
					//Now upload the mandateDetails
					clientUCCResultDTO = mFUPLOADSoapService.fireMFAPIRequest(BSEConstant.MFAPI_STP_REGISTRATION, bseUserId, clientUCCResultDTO.getMessage(), pipeSeparatedString, stpOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId());
					if (clientUCCResultDTO.isStatus()) {
						// ccCreation with BSE Successful. Save the result in table
						/*alreadySavedOrders = clientSTPRepository.
									getByClientCodeAndSaveModeAndPurchaseMode(clientSTPDTO.getClientCode(), FinexaConstant.DRAFT_MODE, 
											clientSTPOrderRegistration.getPurchaseMode());
							if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
								clientSTPOrderRegistration.setId(alreadySavedOrders.get(0).getId());
							}*/
						stpOrderRegistration.setBseResponse(clientUCCResultDTO.getMessage());
						stpOrderRegistration.setBseResponseCode("" +clientUCCResultDTO.getStatusCode());

						stpOrderRegistration.setSaveMode(FinexaConstant.SUCCESS_MODE);
						stpOrderRegistration.setPurchaseMode(FinexaConstant.PURCHASE_MODE_BUY);
						stpOrderRegistration = clientSTPRepository.save(stpOrderRegistration);
					} else {
						// set in error mode
						stpOrderRegistration.setSaveMode(FinexaConstant.ERROR_MODE);
						stpOrderRegistration = clientSTPRepository.save(stpOrderRegistration);
					}
				}

				CartOrderStatusDTO obj = new CartOrderStatusDTO();
				obj.setId(stpOrderRegistration.getId().intValue());
				obj.setOrderStatus(clientUCCResultDTO.getMessage());
				obj.setSchemeName("From Scheme "+ stpOrderRegistration.getMasterTransactBsemfphysicalScheme1().getSchemeName() + " To Scheme " + stpOrderRegistration.getMasterTransactBsemfphysicalScheme2().getSchemeName());
				//obj.setIsin(switchOrderEntryParam.getMasterTransactBsemfphysicalScheme().getIsin());
				cartOrderStatusList.add(obj);

			} catch (Exception e) {

			}

		}
		return cartOrderStatusList;
	}
	@Override
	public List<CartOrderStatusDTO> placeSwpOrdersOfCart(int[] swpOrders) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<CartOrderStatusDTO> cartOrderStatusList = new ArrayList();
		for (int orderIndex = 0; orderIndex < swpOrders.length; orderIndex ++) {
			try {
				ClientSWPOrderRegistration swpOrderRegistration  = clientSWPRepository.findOne(new BigInteger("" + swpOrders[orderIndex]));
				String pipeSeparatedString = "";
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getClientCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getMasterTransactBsemfphysicalScheme().getAmcSchemeCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getTransactionMode(), pipeSeparatedString);

				/*if (clientSWPDTO.getTransactionMode().equals( FinexaConstant.PHYSICAL_MODE)) {
						pipeSeparatedString = joinPipeValues(clientSWPSTPOrderRegistration.getMasterTransactBsemfphysicalScheme().getAmcSchemeCode(), pipeSeparatedString);
						pipeSeparatedString = joinPipeValues(clientSWPDTO.getTransactionMode(), pipeSeparatedString);

					} else {
						pipeSeparatedString = joinPipeValues(clientSWPSTPOrderRegistration.getMasterTransactBsemfdematScheme().getAmcSchemeCode(), pipeSeparatedString);
						pipeSeparatedString = joinPipeValues(clientSWPDTO.getTransactionMode(), pipeSeparatedString);
					}*/
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getFolioNo(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues("", pipeSeparatedString);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String startDate = sdf.format(swpOrderRegistration.getStartDate());

				pipeSeparatedString = joinPipeValues(startDate, pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(("" + swpOrderRegistration.getNoOfWithDrawals()), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getFrequencyType(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getInstallmentAmount().toString(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getInstallmentUnit() == null ? "" :swpOrderRegistration.getInstallmentUnit().toString(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getFirstOrderToday(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getSubBrokerCode(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getEuinDeclaration(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getEuinNumber(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getRemarks(), pipeSeparatedString);
				pipeSeparatedString = joinPipeValues(swpOrderRegistration.getSubBroker_ARN(), pipeSeparatedString);

				System.out.println(pipeSeparatedString);
				pipeSeparatedString = pipeSeparatedString.substring(0,pipeSeparatedString.length()-1);
				System.out.println(pipeSeparatedString);

				String bseUserId = swpOrderRegistration.getAdvisorUser().getBseUsername();
				String bseMemberId = swpOrderRegistration.getAdvisorUser().getBseMemberId();
				String bsePassword = decrypt(swpOrderRegistration.getAdvisorUser().getBsePassword());
				String passKey = autoGeneratePassKey();

				MFUPLOADSoapService mFUPLOADSoapService = new MFUPLOADSoapService();
				ClientUCCResultDTO clientUCCResultDTO = mFUPLOADSoapService.authenticateMFUploadService
						(bseUserId, bseMemberId, bsePassword, passKey, swpOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId());

				if (clientUCCResultDTO.isStatus()) {
					// Bse call to authenticate password is successful,
					//Now upload the mandateDetails
					clientUCCResultDTO = mFUPLOADSoapService.fireMFAPIRequest(BSEConstant.MFAPI_SWP_REGISTRATION, bseUserId, clientUCCResultDTO.getMessage(), pipeSeparatedString, swpOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId());
					if (clientUCCResultDTO.isStatus()) {
						// ccCreation with BSE Successful. Save the result in table
						/*alreadySavedOrders = clientSWPRepository.
									getByClientCodeAndSaveModeAndPurchaseMode(clientSWPDTO.getClientCode(), FinexaConstant.DRAFT_MODE, 
											clientSWPSTPOrderRegistration.getPurchaseMode());
							if (alreadySavedOrders != null && alreadySavedOrders.size() > 0 && alreadySavedOrders.get(0) != null) {
								clientSWPSTPOrderRegistration.setId(alreadySavedOrders.get(0).getId());
							}*/
						swpOrderRegistration.setBseResponse(clientUCCResultDTO.getMessage());
						swpOrderRegistration.setBseResponseCode("" +clientUCCResultDTO.getStatusCode());

						swpOrderRegistration.setSaveMode(FinexaConstant.SUCCESS_MODE);
						swpOrderRegistration.setPurchaseMode(FinexaConstant.PURCHASE_MODE_BUY);
						swpOrderRegistration = clientSWPRepository.save(swpOrderRegistration);

					}
				}

				CartOrderStatusDTO obj = new CartOrderStatusDTO();
				obj.setId(swpOrderRegistration.getId().intValue());
				obj.setOrderStatus(clientUCCResultDTO.getMessage());
				if (swpOrderRegistration.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
					obj.setSchemeName(swpOrderRegistration.getMasterTransactBsemfphysicalSchemeLive().getSchemeName());
				} else {
					obj.setSchemeName(swpOrderRegistration.getMasterTransactBsemfphysicalScheme().getSchemeName());
				}
				
				//obj.setIsin(switchOrderEntryParam.getMasterTransactBsemfphysicalScheme().getIsin());
				cartOrderStatusList.add(obj);
			} catch (Exception e) {

			}

		}
		return cartOrderStatusList;
	}
	@Override
	public List<ProductRecommendationTransactDTO> getLastSavedProductRecommendationPM(int advsiorId, int clientId, String date, String module)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub
		// =========== start Product recommendation=====================
		List<ProductRecommendationTransactDTO> productRecommendationDTOList = null;
		try {
			Date lastSavedDate = null;
			if (!date.equals(DATE_UNDEFINED)) {
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				lastSavedDate = sdf.parse(date);
			} else {
				lastSavedDate = advisorProductRecoRepository.getMaxDateOfSavedProductRecoPM(advsiorId, clientId, module);
			}
			if (lastSavedDate != null) {
				String productPlan = advisorProductRecoRepository.
						getLastSavedProductPlanPM(advsiorId, clientId, module,lastSavedDate);
				if (productPlan != "") {
					productRecommendationDTOList = new ObjectMapper().readValue(productPlan, new TypeReference<List<ProductRecommendationTransactDTO>>(){});
				}
			}
			return productRecommendationDTOList;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productRecommendationDTOList;
	}
	@Override
	public List<String> getAllMandateIDByType(String mandateType, String clientCode) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<String> mandateIdList = new ArrayList<>();
		try {
			List<ClientMandateRegistration> mandateDTOList = null;
			if (mandateType.equals(FinexaConstant.MANDATE_TYPE_ISIP) || mandateType.equals(FinexaConstant.MANDATE_TYPE_XSIP)) {
				mandateDTOList = clientMandateRepository.findByClientCodeAndMandateTypeAndSaveMode(clientCode, mandateType, FinexaConstant.SUCCESS_MODE);
			} else {
				mandateDTOList = clientMandateRepository.findByClientCodeAndMandateTypeAndSaveMode(clientCode, mandateType, FinexaConstant.SUCCESS_MODE);
			}
			if (mandateDTOList != null) {
				for (ClientMandateRegistration obj : mandateDTOList) {
					mandateIdList.add(obj.getMandateId());
				}
			}
			
		} catch (Exception exp) {
			throw new FinexaBussinessException("Mandate ID", "", "Failed to Get Mandate Ids");
		}
		return mandateIdList;
	}
	
	@Override
	public ClientUCCDraftModeDTO getClientCKYCByUCC(String clientCode) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		ClientUCCDraftModeDTO clientUCCDraftModeDTO = new ClientUCCDraftModeDTO();
		try {
			
			ClientCKYCDetail clientCKYCDetails = clientKYCRepository.findByClientCode(clientCode);
			if (clientCKYCDetails != null) {
				clientUCCDraftModeDTO = mapper.map(clientCKYCDetails, ClientUCCDraftModeDTO.class);

				String value = new String();
				
				if (clientCKYCDetails.getLookupTransactKyctype1() != null) {
					value = "" + clientCKYCDetails.getLookupTransactKyctype1().getId();
					clientUCCDraftModeDTO.setkYCFirstApplicant(value);
				}
				
				if (clientCKYCDetails.getLookupTransactKyctype2() != null) {
					value = "" + clientCKYCDetails.getLookupTransactKyctype2().getId();
					clientUCCDraftModeDTO.setkYCTypeSecondApplicant(value);
				}
				
				if (clientCKYCDetails.getLookupTransactKyctype3() != null) {
					value = "" + clientCKYCDetails.getLookupTransactKyctype3().getId();
					clientUCCDraftModeDTO.setkYCThirdApplicant(value);
				}
				
				if (clientCKYCDetails.getLookupTransactKyctype4() != null) {
					value = "" + clientCKYCDetails.getLookupTransactKyctype4().getId();
					clientUCCDraftModeDTO.setKycTypeGuardian(value);
				}
			
			}
			
			//clientUCCDraftModeDTO.setcKYCFirstApplicant(clientCKYCDetails.getCkycNumberFirst().toString());
			//System.out.println(clientUCCDraftModeDTO.getcKYCFirstApplicant());
			//LookupTransactKYCType lookupTransactKYCType = clientCKYCDetails.getCkycNumberFirst().toString();
			
			
			/*
			 * LookupTransactKYCType lookupTransactKYCTypeObj =
			 * lookupTransactKYCTypeRepository.findOne(clientCKYCDetails.
			 * getLookupTransactKyctype1()); clientUCCDraftModeDTO =
			 * mapper.map(lookupTransactKYCTypeObj, ClientUCCDraftModeDTO.class);
			 * clientUCCDraftModeDTO.setcKYCFirstApplicant(lookupTransactKYCTypeObj.
			 * getClientCkycdetails1().toString());
			 */
			
			
		} catch (Exception e) {
			throw new FinexaBussinessException("MF-Transact", "500", "Failed to Get CKYC details");
		}
		return clientUCCDraftModeDTO;
	}
	

	
}
