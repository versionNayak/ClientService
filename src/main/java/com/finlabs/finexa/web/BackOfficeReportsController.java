package com.finlabs.finexa.web;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.AumReportColumnDTO;
import com.finlabs.finexa.dto.AumReportDTO;
import com.finlabs.finexa.dto.BrokerageReportColumnDTO;
import com.finlabs.finexa.dto.BrokerageReportDTO;
import com.finlabs.finexa.dto.CapitalGainsReportColumnDTO;
import com.finlabs.finexa.dto.CapitalGainsReportDTO;
import com.finlabs.finexa.dto.CurrentHoldingReportColumnDTO;
import com.finlabs.finexa.dto.CurrentHoldingReportDTO;
import com.finlabs.finexa.dto.DividendReportColumnDTO;
import com.finlabs.finexa.dto.DividendReportColumnNewDTO;
import com.finlabs.finexa.dto.DividendReportDTO;
import com.finlabs.finexa.dto.PortfolioGainLossReportColumnDTO;
import com.finlabs.finexa.dto.PortfolioGainLossReportDTO;
import com.finlabs.finexa.dto.PortfolioValuationReportColumnDTO;
import com.finlabs.finexa.dto.PortfolioValuationReportDTO;
import com.finlabs.finexa.dto.SIPSTPSWPReportColumnDTO;
import com.finlabs.finexa.dto.SIPSTPSWPReportDTO;
import com.finlabs.finexa.dto.TransactionReportColumnDTO;
import com.finlabs.finexa.dto.TransactionReportDTO;
import com.finlabs.finexa.dto.TransactionReportDetailedDTOSecondOption;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.service.AUMTransactionReportBOService;
import com.finlabs.finexa.service.AsynchronousService;
import com.finlabs.finexa.service.BrokerageReportService;
import com.finlabs.finexa.service.CapitalGainsBOService;
import com.finlabs.finexa.service.DividendReportBOService;
import com.finlabs.finexa.service.DividendReportBOServiceNew;
import com.finlabs.finexa.service.PortfolioGainLossRpeortBOService;
import com.finlabs.finexa.service.PortfolioValuationReportservice;
import com.finlabs.finexa.service.RealisedGainBOService;
import com.finlabs.finexa.service.SIPSTPSWPReportService;
import com.finlabs.finexa.service.TransactionReportService;
import com.finlabs.finexa.service.TransactionReportServiceNew;
import com.finlabs.finexa.service.UnrealisedGainBOService;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

@RestController
public class BackOfficeReportsController {

	// private static Logger log =
	// LoggerFactory.getLogger(BackOfficeReportsController.class);

	private static final String CURRENT_PAGE_NUMBER = "${CURRENT_PAGE_NUMBER}";
	private static final String TOTAL_PAGE_NUMBER = "${TOTAL_PAGE_NUMBER}";

	@Autowired
	private AUMTransactionReportBOService aumTransactionReportBOService;

	@Autowired
	private TransactionReportService transactionReportService;

	@Autowired
	private TransactionReportServiceNew transactionReportServiceNew;

	@Autowired
	private RealisedGainBOService realizedGainBOService;

	@Autowired
	private CapitalGainsBOService capitalGainsBOService;

	@Autowired
	private UnrealisedGainBOService unrealisedGainBOService;

	@Autowired
	private DividendReportBOService dividendReportBOService;

	@Autowired
	private DividendReportBOServiceNew dividendReportBOServiceNew;

	@Autowired
	private SIPSTPSWPReportService sipSTPSWPReportService;

	

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private BrokerageReportService brokerageReportService;

	@Autowired
	private PortfolioValuationReportservice portfolioValuationReportservice;

	@Autowired
	private PortfolioGainLossRpeortBOService portfolioGainLossRpeortBOService;

	@Autowired
	AsynchronousService async;

	@RequestMapping(value = "/generateAumReport", method = RequestMethod.GET)
	public ResponseEntity<?> generateAumReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "asOnDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date asOnDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "isin") String isin,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";

			String asOnDateInput = "";

			AumReportDTO inputDTO = new AumReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			asOnDateInput = FinexaUtil.getProperDateInput(c1, asOnDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setAsOnDate(formatter.parse(asOnDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(isin);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null || cm.getMiddleName().isEmpty()) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null || cfm.getMiddleName().isEmpty()) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setInputMap(aumTransactionReportBOService.aumTransactionReport(inputDTO));

			returnStatus = generateAumReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-AUMRHTML", "Failed to generate Aum Report.", e);
		}

	}

	private String generateAumReport(AumReportDTO aumReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";
		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"AumReport.html\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (aumReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(aumReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (aumReportDTO.getInputMap() != null && aumReportDTO.getInputMap().size() > 0) {
				for (Map.Entry<String, List<AumReportColumnDTO>> dataSourceMapEntry : aumReportDTO.getInputMap()
						.entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/aumReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();

					parameters.put("logo", logoImage);
					parameters.put("distributorName", aumReportDTO.getDistributorName());
					parameters.put("distributorEmail", aumReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", aumReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(aumReportDTO.getAsOnDate()));
					parameters.put("familyName", aumReportDTO.getFamilyName());
					parameters.put("emailAddress", aumReportDTO.getClientEmail());
					parameters.put("mobileNo", aumReportDTO.getClientMobile());
					parameters.put("clientName", aumReportDTO.getNameClient());
					parameters.put("pan", aumReportDTO.getClientPAN());
					parameters.put("AumDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporterHTML.setExporterOutput(
						new SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/AumReport.html"));
				// exporterHTML.setExporterOutput(new
				// SimpleHtmlExporterOutput("/home/supratim/MFBackOfficeJava11Workspace/FinexaWebMerged/src/main/webapp/MyBusiness/resources/AumReport.html"));
				exporterHTML.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;

	}

	@RequestMapping(value = "/aumExport", method = RequestMethod.GET)
	public ResponseEntity<?> createAumReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "asOnDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date asOnDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "isin") String isin,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";

			String asOnDateInput = "";

			AumReportDTO inputDTO = new AumReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			asOnDateInput = FinexaUtil.getProperDateInput(c1, asOnDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setAsOnDate(formatter.parse(asOnDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(isin);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null || cm.getMiddleName().isEmpty()) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null || cfm.getMiddleName().isEmpty()) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setInputMap(aumTransactionReportBOService.aumTransactionReport(inputDTO));

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportAumReportExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportAumReportPDF(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-AR01", "Failed to export Aum Report.", e);
		}

	}

	private String exportAumReportExcel(AumReportDTO aumReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"AumReport.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (aumReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(aumReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (aumReportDTO.getInputMap() != null && aumReportDTO.getInputMap().size() > 0) {
				for (Map.Entry<String, List<AumReportColumnDTO>> dataSourceMapEntry : aumReportDTO.getInputMap()
						.entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/excel/aumReportExcel.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", aumReportDTO.getDistributorName());
					parameters.put("distributorEmail", aumReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", aumReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(aumReportDTO.getAsOnDate()));
					parameters.put("familyName", aumReportDTO.getFamilyName());
					parameters.put("emailAddress", aumReportDTO.getClientEmail());
					parameters.put("mobileNo", aumReportDTO.getClientMobile());
					parameters.put("clientName", aumReportDTO.getNameClient());
					parameters.put("pan", aumReportDTO.getClientPAN());
					parameters.put("AumDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRXlsExporter xlsExporter = new JRXlsExporter(); // For .xls
				// JRXlsxExporter xlsxExporter = new JRXlsxExporter(); //For .xlsx

				xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
						System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "AumReport.xls"));
				SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
				xlsReportConfiguration.setOnePagePerSheet(false);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
				xlsReportConfiguration.setWrapText(true);
				xlsReportConfiguration.setFontSizeFixEnabled(true);
				xlsReportConfiguration.setDetectCellType(true);
				xlsReportConfiguration.setWhitePageBackground(false);
				xlsExporter.setConfiguration(xlsReportConfiguration);

				xlsExporter.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	private String exportAumReportPDF(AumReportDTO aumReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub

		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"AumReport.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (aumReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(aumReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (aumReportDTO.getInputMap() != null && aumReportDTO.getInputMap().size() > 0) {
				for (Map.Entry<String, List<AumReportColumnDTO>> dataSourceMapEntry : aumReportDTO.getInputMap()
						.entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/aumReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", aumReportDTO.getDistributorName());
					parameters.put("distributorEmail", aumReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", aumReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(aumReportDTO.getAsOnDate()));
					parameters.put("familyName", aumReportDTO.getFamilyName());
					parameters.put("emailAddress", aumReportDTO.getClientEmail());
					parameters.put("mobileNo", aumReportDTO.getClientMobile());
					parameters.put("clientName", aumReportDTO.getNameClient());
					parameters.put("pan", aumReportDTO.getClientPAN());
					parameters.put("AumDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
						System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "AumReport.pdf"));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				configuration.setCreatingBatchModeBookmarks(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/generateBrokerageReport", method = RequestMethod.GET)
	public ResponseEntity<?> generateBrokerageReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException, ParseException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			BrokerageReportDTO inputDTO = new BrokerageReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);
			inputDTO.setAdvisorId(advisorID);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// inputDTO.setFundHouse(fundHouse);
			// inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			// dummy replace after proper service is developed
			Map<String, List<BrokerageReportColumnDTO>> inputMap = new HashMap<String, List<BrokerageReportColumnDTO>>();
			List<BrokerageReportColumnDTO> inputList = new ArrayList<BrokerageReportColumnDTO>();
			BrokerageReportColumnDTO dataDTO = new BrokerageReportColumnDTO();
			// dataDTO.setFundName("Mirae Asset Mutual Fund");
			// dataDTO.setFolioNo("70410784852");
			// dataDTO.setSchemeName("Mirae Asset Emerging Bluechip Fund - Regular Plan
			// Growth");

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}

			}

			// dataDTO.setClientDetails(inputDTO.getNameClient() + "-" +
			// inputDTO.getClientPAN());
			// dataDTO.setCurrentAUM("2,500");
			// dataDTO.setBrokType("Annualized");
			// dataDTO.setBrokerage("9.92");
			// inputList.add(dataDTO);

			// inputMap.put("some key", inputList);

			inputDTO.setInputMap(brokerageReportService.brokerageReport(inputDTO));

			returnStatus = generateBrokerageReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-BRHTML", "Failed to generate Brokerage Report.",
					e);
		}

	}

	private String generateBrokerageReport(BrokerageReportDTO brokerageReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"BrokerageReport.html\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (brokerageReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(brokerageReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (brokerageReportDTO.getInputMap() != null && brokerageReportDTO.getInputMap().size() > 0) {
				for (Map.Entry<String, List<BrokerageReportColumnDTO>> dataSourceMapEntry : brokerageReportDTO
						.getInputMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/brokerageReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", brokerageReportDTO.getDistributorName());
					parameters.put("distributorEmail", brokerageReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", brokerageReportDTO.getDistributorMobile());
					parameters.put("clientName", brokerageReportDTO.getNameClient());
					parameters.put("pan", brokerageReportDTO.getClientPAN());
					parameters.put("emailAddress", brokerageReportDTO.getClientEmail());
					parameters.put("mobileNo", brokerageReportDTO.getClientMobile());
					parameters.put("fromDate", formatter.format(brokerageReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(brokerageReportDTO.getToDate()));
					parameters.put("BrokerageDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				// exporterHTML.setExporterOutput(new
				// SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/BrokerageReport.html"));
				exporterHTML.setExporterOutput(
						new SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/BrokerageReport.html"));
				exporterHTML.exportReport();

				returnStatus = "Success";
			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/brokerageExport", method = RequestMethod.GET)
	public ResponseEntity<?> createBrokerageReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException, ParseException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			BrokerageReportDTO inputDTO = new BrokerageReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);
			inputDTO.setAdvisorId(advisorID);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// inputDTO.setFundHouse(fundHouse);
			// inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			// dummy replace after proper service is developed
			Map<String, List<BrokerageReportColumnDTO>> inputMap = new HashMap<String, List<BrokerageReportColumnDTO>>();
			List<BrokerageReportColumnDTO> inputList = new ArrayList<BrokerageReportColumnDTO>();
			BrokerageReportColumnDTO dataDTO = new BrokerageReportColumnDTO();
			// dataDTO.setFundName("Mirae Asset Mutual Fund");
			// dataDTO.setFolioNo("70410784852");
			// dataDTO.setSchemeName("Mirae Asset Emerging Bluechip Fund - Regular Plan
			// Growth");

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}

			}

			// dataDTO.setClientDetails(inputDTO.getNameClient() + "-" +
			// inputDTO.getClientPAN());
			// dataDTO.setCurrentAUM("2,500");
			// dataDTO.setBrokType("Annualized");
			// dataDTO.setBrokerage("9.92");
			// inputList.add(dataDTO);

			// inputMap.put("some key", inputList);

			inputDTO.setInputMap(brokerageReportService.brokerageReport(inputDTO));

			System.out.println("REPORT FORMAT:" + reportFormat);
			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportBrokerageReportExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportBrokerageReportPDF(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-BR01", "Failed to export Brokerage Report.", e);
		}

	}

	private String exportBrokerageReportExcel(BrokerageReportDTO brokerageReportDTO, HttpServletResponse response)
			throws JRException, IOException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"BrokerageReport.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (brokerageReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(brokerageReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (brokerageReportDTO.getInputMap() != null && brokerageReportDTO.getInputMap().size() > 0) {
				for (Map.Entry<String, List<BrokerageReportColumnDTO>> dataSourceMapEntry : brokerageReportDTO
						.getInputMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/brokerageReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", brokerageReportDTO.getDistributorName());
					parameters.put("distributorEmail", brokerageReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", brokerageReportDTO.getDistributorMobile());
					parameters.put("distributorAddress", brokerageReportDTO.getDistributorAddress());
					parameters.put("distributorContactDetails", brokerageReportDTO.getDistributorContactDetails());
					parameters.put("fromDate", formatter.format(brokerageReportDTO.getFromDate()));
					parameters.put("clientName", brokerageReportDTO.getNameClient());
					parameters.put("pan", brokerageReportDTO.getClientPAN());
					parameters.put("emailAddress", brokerageReportDTO.getClientEmail());
					parameters.put("mobileNo", brokerageReportDTO.getClientMobile());
					parameters.put("toDate", formatter.format(brokerageReportDTO.getToDate()));
					parameters.put("BrokerageDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "BrokerageReport.xls"));
				SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
				xlsReportConfiguration.setOnePagePerSheet(false);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
				xlsReportConfiguration.setWrapText(true);
				xlsReportConfiguration.setFontSizeFixEnabled(false);
				xlsReportConfiguration.setDetectCellType(true);
				xlsReportConfiguration.setWhitePageBackground(false);
				xlsExporter.setConfiguration(xlsReportConfiguration);

				xlsExporter.exportReport();

				returnStatus = "Success";
			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	private String exportBrokerageReportPDF(BrokerageReportDTO brokerageReportDTO, HttpServletResponse response)
			throws JRException, IOException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"BrokerageReport.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (brokerageReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(brokerageReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (brokerageReportDTO.getInputMap() != null && brokerageReportDTO.getInputMap().size() > 0) {
				for (Map.Entry<String, List<BrokerageReportColumnDTO>> dataSourceMapEntry : brokerageReportDTO
						.getInputMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/brokerageReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", brokerageReportDTO.getDistributorName());
					parameters.put("distributorEmail", brokerageReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", brokerageReportDTO.getDistributorMobile());
					parameters.put("distributorAddress", brokerageReportDTO.getDistributorAddress());
					parameters.put("distributorContactDetails", brokerageReportDTO.getDistributorContactDetails());
					parameters.put("clientName", brokerageReportDTO.getNameClient());
					parameters.put("pan", brokerageReportDTO.getClientPAN());
					parameters.put("emailAddress", brokerageReportDTO.getClientEmail());
					parameters.put("mobileNo", brokerageReportDTO.getClientMobile());
					parameters.put("fromDate", formatter.format(brokerageReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(brokerageReportDTO.getToDate()));
					parameters.put("BrokerageDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "BrokerageReport.pdf"));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				configuration.setCreatingBatchModeBookmarks(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				returnStatus = "Success";
			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/generateTransactionReport", method = RequestMethod.GET)
	public ResponseEntity<?> generateTransactionReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "isin") String isin,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {
		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			TransactionReportDTO inputDTO = new TransactionReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(isin);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setFolioShemeMap(transactionReportService.transactionReport(inputDTO));

			returnStatus = generateTransactionReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException | ParseException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-TRHTML", "Failed to generate Transaction Report.",
					e);
		}
	}

	private String generateTransactionReport(TransactionReportDTO transactionReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"TransactionReport.html\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (transactionReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(transactionReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (transactionReportDTO.getFolioShemeMap() != null && transactionReportDTO.getFolioShemeMap().size() > 0) {
				for (Map.Entry<String, List<TransactionReportColumnDTO>> dataSourceMapEntry : transactionReportDTO
						.getFolioShemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/transactionReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", transactionReportDTO.getDistributorName());
					parameters.put("distributorEmail", transactionReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", transactionReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(transactionReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(transactionReportDTO.getToDate()));
					parameters.put("fundHouse", transactionReportDTO.getFundHouse());
					parameters.put("familyName", transactionReportDTO.getFamilyName());
					parameters.put("emailAddress", transactionReportDTO.getClientEmail());
					parameters.put("mobileNo", transactionReportDTO.getClientMobile());
					parameters.put("clientName", transactionReportDTO.getNameClient());
					parameters.put("pan", transactionReportDTO.getClientPAN());
					parameters.put("TransDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);

				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));

				exporterHTML.setExporterOutput(
						new SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/TransactionReport.html"));
				/*
				 * exporterHTML.setExporterOutput(new
				 * SimpleHtmlExporterOutput("/home/smita/Desktop/TransactionReport.html"));
				 */
				exporterHTML.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/transactionExport", method = RequestMethod.GET)
	public ResponseEntity<?> createTransactionReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			TransactionReportDTO inputDTO = new TransactionReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}

				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setFolioShemeMap(transactionReportService.transactionReport(inputDTO));

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportTransactionReportExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportTransactionReportPDF(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException | ParseException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-TR01", "Failed to export Transaction Report.", e);
		}

	}

	private String exportTransactionReportExcel(TransactionReportDTO transactionReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"TransactionReport.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (transactionReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(transactionReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (transactionReportDTO.getFolioShemeMap() != null && transactionReportDTO.getFolioShemeMap().size() > 0) {
				for (Map.Entry<String, List<TransactionReportColumnDTO>> dataSourceMapEntry : transactionReportDTO
						.getFolioShemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());

					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/transactionReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", transactionReportDTO.getDistributorName());
					parameters.put("distributorEmail", transactionReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", transactionReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(transactionReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(transactionReportDTO.getToDate()));
					parameters.put("fundHouse", transactionReportDTO.getFundHouse());
					parameters.put("familyName", transactionReportDTO.getFamilyName());
					parameters.put("emailAddress", transactionReportDTO.getClientEmail());
					parameters.put("mobileNo", transactionReportDTO.getClientMobile());
					parameters.put("clientName", transactionReportDTO.getNameClient());
					parameters.put("pan", transactionReportDTO.getClientPAN());
					parameters.put("TransDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);

				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "TransactionReport.xls"));
				SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
				xlsReportConfiguration.setOnePagePerSheet(false);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
				xlsReportConfiguration.setWrapText(true);
				xlsReportConfiguration.setFontSizeFixEnabled(false);
				xlsReportConfiguration.setDetectCellType(true);
				xlsReportConfiguration.setWhitePageBackground(false);
				xlsExporter.setConfiguration(xlsReportConfiguration);

				xlsExporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;

	}

	private String exportTransactionReportPDF(TransactionReportDTO transactionReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"TransactionReport.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (transactionReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(transactionReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (transactionReportDTO.getFolioShemeMap() != null && transactionReportDTO.getFolioShemeMap().size() > 0) {
				for (Map.Entry<String, List<TransactionReportColumnDTO>> dataSourceMapEntry : transactionReportDTO
						.getFolioShemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());

					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/transactionReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", transactionReportDTO.getDistributorName());
					parameters.put("distributorEmail", transactionReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", transactionReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(transactionReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(transactionReportDTO.getToDate()));
					parameters.put("fundHouse", transactionReportDTO.getFundHouse());
					parameters.put("familyName", transactionReportDTO.getFamilyName());
					parameters.put("emailAddress", transactionReportDTO.getClientEmail());
					parameters.put("mobileNo", transactionReportDTO.getClientMobile());
					parameters.put("clientName", transactionReportDTO.getNameClient());
					parameters.put("pan", transactionReportDTO.getClientPAN());
					parameters.put("TransDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);

				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "TransactionReport.pdf"));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				configuration.setCreatingBatchModeBookmarks(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;

	}

	@RequestMapping(value = "/generateTransactionReportNew", method = RequestMethod.GET)
	public ResponseEntity<?> generateTransactionReportNew(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			TransactionReportDTO inputDTO = new TransactionReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}

				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setDataSourceList(transactionReportServiceNew.transactionReport(inputDTO));

			returnStatus = generateTransactionReportNew(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-TR01", "Failed to export Transaction Report New.",
					e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<String>("NA", HttpStatus.OK);
	}

	private String generateTransactionReportNew(TransactionReportDTO transactionReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"TransactionReportNew.html\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (transactionReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(transactionReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("logo", logoImage);
			parameters.put("distributorName", transactionReportDTO.getDistributorName());
			parameters.put("distributorEmail", transactionReportDTO.getDistributorEmail());
			parameters.put("distributorMobile", transactionReportDTO.getDistributorMobile());
			parameters.put("fromDate", formatter.format(transactionReportDTO.getFromDate()));
			parameters.put("toDate", formatter.format(transactionReportDTO.getToDate()));
			parameters.put("fundHouse", transactionReportDTO.getFundHouse());
			parameters.put("familyName", transactionReportDTO.getFamilyName());
			parameters.put("emailAddress", transactionReportDTO.getClientEmail());
			parameters.put("mobileNo", transactionReportDTO.getClientMobile());
			parameters.put("clientName", transactionReportDTO.getNameClient());
			parameters.put("pan", transactionReportDTO.getClientPAN());

			if (transactionReportDTO.getDataSourceList() != null
					&& transactionReportDTO.getDataSourceList().size() > 0) {

				List<TransactionReportDetailedDTOSecondOption> summaryDataSource = new ArrayList<TransactionReportDetailedDTOSecondOption>();
				for (TransactionReportDetailedDTOSecondOption obj : transactionReportDTO.getDataSourceList()) {
					summaryDataSource = new ArrayList<TransactionReportDetailedDTOSecondOption>();
					summaryDataSource.add(obj);
					JRBeanCollectionDataSource jrBeanCollectionDataSourceTransactionSummary = new JRBeanCollectionDataSource(
							summaryDataSource);
					String pathTransactionSummarySR = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/transactionSummarySR.jrxml").getURI()
							.getPath();
					JasperReport jasperReportTransactionSummarySR = JasperCompileManager
							.compileReport(pathTransactionSummarySR);
					parameters.put("TransSummaryDataSource", jrBeanCollectionDataSourceTransactionSummary);
					parameters.put("jasperReportTransactionSummarySR", jasperReportTransactionSummarySR);

					if (obj.getMainReportMap() != null && obj.getMainReportMap().size() > 0) {
						for (Map.Entry<String, List<TransactionReportColumnDTO>> mainReportMapEntry : obj
								.getMainReportMap().entrySet()) {
							JRBeanCollectionDataSource jrBeanCollectionDataSourceTransactionMain = new JRBeanCollectionDataSource(
									mainReportMapEntry.getValue());
							String pathTransactionMainSR = resourceLoader
									.getResource("classpath:backOfficeReportsJrxml/transactionMainTableSR.jrxml")
									.getURI().getPath();
							JasperReport jasperReportTransactionMainSR = JasperCompileManager
									.compileReport(pathTransactionMainSR);
							parameters.put("TransMainDataSource", jrBeanCollectionDataSourceTransactionMain);
							parameters.put("jasperReportTransactionMainSR", jasperReportTransactionMainSR);
						}

						String path = resourceLoader
								.getResource("classpath:backOfficeReportsJrxml/transactionReportNew.jrxml").getURI()
								.getPath();
						JasperReport jasperReport = JasperCompileManager.compileReport(path);

						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
								new JREmptyDataSource());
						jasperPrintList.add(jasperPrint);

					}
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}

						currentPage++;
					}

				}

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporterHTML.setExporterOutput(
						new SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/TransactionReportNew.html"));
				// exporterHTML.setExporterOutput(new
				// SimpleHtmlExporterOutput("/home/debolina/Documents/DummyMasterWorkspace/FinexaWeb/src/main/webapp/MyBusiness/resources/TransactionReportNew.html"));
				exporterHTML.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/exportTransactionReportNew", method = RequestMethod.GET)
	public ResponseEntity<?> exportTransactionReportNew(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			TransactionReportDTO inputDTO = new TransactionReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}

				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setDataSourceList(transactionReportServiceNew.transactionReport(inputDTO));

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportTransactionReportNewExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportTransactionReportNewPDF(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-TR01", "Failed to export Transaction Report New.",
					e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<String>("NA", HttpStatus.OK);
	}

	private String exportTransactionReportNewExcel(TransactionReportDTO transactionReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"TransactionReportNew.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (transactionReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(transactionReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("logo", logoImage);
			parameters.put("distributorName", transactionReportDTO.getDistributorName());
			parameters.put("distributorEmail", transactionReportDTO.getDistributorEmail());
			parameters.put("distributorMobile", transactionReportDTO.getDistributorMobile());
			parameters.put("fromDate", formatter.format(transactionReportDTO.getFromDate()));
			parameters.put("toDate", formatter.format(transactionReportDTO.getToDate()));
			parameters.put("fundHouse", transactionReportDTO.getFundHouse());
			parameters.put("familyName", transactionReportDTO.getFamilyName());
			parameters.put("emailAddress", transactionReportDTO.getClientEmail());
			parameters.put("mobileNo", transactionReportDTO.getClientMobile());
			parameters.put("clientName", transactionReportDTO.getNameClient());
			parameters.put("pan", transactionReportDTO.getClientPAN());

			if (transactionReportDTO.getDataSourceList() != null
					&& transactionReportDTO.getDataSourceList().size() > 0) {

				List<TransactionReportDetailedDTOSecondOption> summaryDataSource = new ArrayList<TransactionReportDetailedDTOSecondOption>();
				for (TransactionReportDetailedDTOSecondOption obj : transactionReportDTO.getDataSourceList()) {
					summaryDataSource = new ArrayList<TransactionReportDetailedDTOSecondOption>();
					summaryDataSource.add(obj);
					JRBeanCollectionDataSource jrBeanCollectionDataSourceTransactionSummary = new JRBeanCollectionDataSource(
							summaryDataSource);
					String pathTransactionSummarySR = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/transactionSummarySR.jrxml").getURI()
							.getPath();
					JasperReport jasperReportTransactionSummarySR = JasperCompileManager
							.compileReport(pathTransactionSummarySR);
					parameters.put("TransSummaryDataSource", jrBeanCollectionDataSourceTransactionSummary);
					parameters.put("jasperReportTransactionSummarySR", jasperReportTransactionSummarySR);

					if (obj.getMainReportMap() != null && obj.getMainReportMap().size() > 0) {
						for (Map.Entry<String, List<TransactionReportColumnDTO>> mainReportMapEntry : obj
								.getMainReportMap().entrySet()) {
							JRBeanCollectionDataSource jrBeanCollectionDataSourceTransactionMain = new JRBeanCollectionDataSource(
									mainReportMapEntry.getValue());
							String pathTransactionMainSR = resourceLoader
									.getResource("classpath:backOfficeReportsJrxml/transactionMainTableSR.jrxml")
									.getURI().getPath();
							JasperReport jasperReportTransactionMainSR = JasperCompileManager
									.compileReport(pathTransactionMainSR);
							parameters.put("TransMainDataSource", jrBeanCollectionDataSourceTransactionMain);
							parameters.put("jasperReportTransactionMainSR", jasperReportTransactionMainSR);
						}

						String path = resourceLoader
								.getResource("classpath:backOfficeReportsJrxml/transactionReportNew.jrxml").getURI()
								.getPath();
						JasperReport jasperReport = JasperCompileManager.compileReport(path);

						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
								new JREmptyDataSource());
						jasperPrintList.add(jasperPrint);

					}
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "TransactionReportNew.xls"));
				SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
				xlsReportConfiguration.setOnePagePerSheet(false);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
				xlsReportConfiguration.setWrapText(true);
				xlsReportConfiguration.setFontSizeFixEnabled(false);
				xlsReportConfiguration.setDetectCellType(true);
				xlsReportConfiguration.setWhitePageBackground(false);
				xlsExporter.setConfiguration(xlsReportConfiguration);

				xlsExporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;

	}

	private String exportTransactionReportNewPDF(TransactionReportDTO transactionReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"TransactionReportNew.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (transactionReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(transactionReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("logo", logoImage);
			parameters.put("distributorName", transactionReportDTO.getDistributorName());
			parameters.put("distributorEmail", transactionReportDTO.getDistributorEmail());
			parameters.put("distributorMobile", transactionReportDTO.getDistributorMobile());
			parameters.put("fromDate", formatter.format(transactionReportDTO.getFromDate()));
			parameters.put("toDate", formatter.format(transactionReportDTO.getToDate()));
			parameters.put("fundHouse", transactionReportDTO.getFundHouse());
			parameters.put("familyName", transactionReportDTO.getFamilyName());
			parameters.put("emailAddress", transactionReportDTO.getClientEmail());
			parameters.put("mobileNo", transactionReportDTO.getClientMobile());
			parameters.put("clientName", transactionReportDTO.getNameClient());
			parameters.put("pan", transactionReportDTO.getClientPAN());

			if (transactionReportDTO.getDataSourceList() != null
					&& transactionReportDTO.getDataSourceList().size() > 0) {

				List<TransactionReportDetailedDTOSecondOption> summaryDataSource = new ArrayList<TransactionReportDetailedDTOSecondOption>();
				for (TransactionReportDetailedDTOSecondOption obj : transactionReportDTO.getDataSourceList()) {
					summaryDataSource = new ArrayList<TransactionReportDetailedDTOSecondOption>();
					summaryDataSource.add(obj);
					JRBeanCollectionDataSource jrBeanCollectionDataSourceTransactionSummary = new JRBeanCollectionDataSource(
							summaryDataSource);
					String pathTransactionSummarySR = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/transactionSummarySR.jrxml").getURI()
							.getPath();
					JasperReport jasperReportTransactionSummarySR = JasperCompileManager
							.compileReport(pathTransactionSummarySR);
					parameters.put("TransSummaryDataSource", jrBeanCollectionDataSourceTransactionSummary);
					parameters.put("jasperReportTransactionSummarySR", jasperReportTransactionSummarySR);

					if (obj.getMainReportMap() != null && obj.getMainReportMap().size() > 0) {
						for (Map.Entry<String, List<TransactionReportColumnDTO>> mainReportMapEntry : obj
								.getMainReportMap().entrySet()) {
							JRBeanCollectionDataSource jrBeanCollectionDataSourceTransactionMain = new JRBeanCollectionDataSource(
									mainReportMapEntry.getValue());
							String pathTransactionMainSR = resourceLoader
									.getResource("classpath:backOfficeReportsJrxml/transactionMainTableSR.jrxml")
									.getURI().getPath();
							JasperReport jasperReportTransactionMainSR = JasperCompileManager
									.compileReport(pathTransactionMainSR);
							parameters.put("TransMainDataSource", jrBeanCollectionDataSourceTransactionMain);
							parameters.put("jasperReportTransactionMainSR", jasperReportTransactionMainSR);
						}

						String path = resourceLoader
								.getResource("classpath:backOfficeReportsJrxml/transactionReportNew.jrxml").getURI()
								.getPath();
						JasperReport jasperReport = JasperCompileManager.compileReport(path);

						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
								new JREmptyDataSource());
						jasperPrintList.add(jasperPrint);

					}
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}

						currentPage++;
					}

				}

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "TransactionReportNew.pdf"));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				configuration.setCreatingBatchModeBookmarks(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;

	}

	@RequestMapping(value = "/generateRealizedGainReport", method = RequestMethod.GET)
	public ResponseEntity<?> generateRealizedGainReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			CurrentHoldingReportDTO inputDTO = new CurrentHoldingReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setSchemeMap(realizedGainBOService.realisedGainReport(inputDTO));

			System.out.println(inputDTO.getSchemeMap().keySet());
			System.out.println(inputDTO.getSchemeMap().entrySet());

			returnStatus = generateRealizedGainReport(inputDTO, response);

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException | InstantiationException | IllegalAccessException | ClassNotFoundException
				| ParseException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-RGRHTML",
					"Failed to generate Realized Gain Report", e);
		}

	}

	private String generateRealizedGainReport(CurrentHoldingReportDTO currentHoldingReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"RealizedGainReport.html\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (currentHoldingReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(currentHoldingReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (currentHoldingReportDTO.getSchemeMap() != null && currentHoldingReportDTO.getSchemeMap().size() > 0) {
				for (Map.Entry<String, List<CurrentHoldingReportColumnDTO>> dataSourceMapEntry : currentHoldingReportDTO
						.getSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/realizedGainReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", currentHoldingReportDTO.getDistributorName());
					parameters.put("distributorEmail", currentHoldingReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", currentHoldingReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(currentHoldingReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(currentHoldingReportDTO.getToDate()));
					parameters.put("familyName", currentHoldingReportDTO.getFamilyName());
					parameters.put("emailAddress", currentHoldingReportDTO.getClientEmail());
					parameters.put("mobileNo", currentHoldingReportDTO.getClientMobile());
					parameters.put("clientName", currentHoldingReportDTO.getNameClient());
					parameters.put("pan", currentHoldingReportDTO.getClientPAN());
					parameters.put("RealizedGainDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporterHTML.setExporterOutput(
						new SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/RealizedGainReport.html"));
				// exporterHTML.setExporterOutput(new
				// SimpleHtmlExporterOutput("/home/supratim/MFBackOfficeJava11Workspace/FinexaWebMerged/src/main/webapp/MyBusiness/resources/RealizedGainReport.html"));
				exporterHTML.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/realizedGainExport", method = RequestMethod.GET)
	public ResponseEntity<?> createRealizedGainReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {
		try {
			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			CurrentHoldingReportDTO inputDTO = new CurrentHoldingReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setSchemeMap(realizedGainBOService.realisedGainReport(inputDTO));

			System.out.println(inputDTO.getSchemeMap().keySet());
			System.out.println(inputDTO.getSchemeMap().entrySet());

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportRealizedGainReportExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportRealizedGainReportPDF(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException | InstantiationException | IllegalAccessException | ClassNotFoundException
				| ParseException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-RGR01", "Failed to export Realized Gain Report",
					e);
		}

	}

	private String exportRealizedGainReportExcel(CurrentHoldingReportDTO currentHoldingReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"RealizedGainReport.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (currentHoldingReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(currentHoldingReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			System.out.println(
					"currentHoldingReportDTO.getSchemeMap().size(): " + currentHoldingReportDTO.getSchemeMap().size());

			if (currentHoldingReportDTO.getSchemeMap() != null && currentHoldingReportDTO.getSchemeMap().size() > 0) {
				for (Map.Entry<String, List<CurrentHoldingReportColumnDTO>> dataSourceMapEntry : currentHoldingReportDTO
						.getSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/realizedGainReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", currentHoldingReportDTO.getDistributorName());
					parameters.put("distributorEmail", currentHoldingReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", currentHoldingReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(currentHoldingReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(currentHoldingReportDTO.getToDate()));
					parameters.put("familyName", currentHoldingReportDTO.getFamilyName());
					parameters.put("emailAddress", currentHoldingReportDTO.getClientEmail());
					parameters.put("mobileNo", currentHoldingReportDTO.getClientMobile());
					parameters.put("clientName", currentHoldingReportDTO.getNameClient());
					parameters.put("pan", currentHoldingReportDTO.getClientPAN());
					parameters.put("RealizedGainDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "RealizedGainReport.xls"));
				SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
				xlsReportConfiguration.setOnePagePerSheet(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
				xlsReportConfiguration.setDetectCellType(true);
				xlsReportConfiguration.setWhitePageBackground(false);
				xlsReportConfiguration.setIgnoreGraphics(false);
				xlsExporter.setConfiguration(xlsReportConfiguration);

				xlsExporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	private String exportRealizedGainReportPDF(CurrentHoldingReportDTO currentHoldingReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"RealizedGainReport.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (currentHoldingReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(currentHoldingReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (currentHoldingReportDTO.getSchemeMap() != null && currentHoldingReportDTO.getSchemeMap().size() > 0) {
				for (Map.Entry<String, List<CurrentHoldingReportColumnDTO>> dataSourceMapEntry : currentHoldingReportDTO
						.getSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/realizedGainReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", currentHoldingReportDTO.getDistributorName());
					parameters.put("distributorEmail", currentHoldingReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", currentHoldingReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(currentHoldingReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(currentHoldingReportDTO.getToDate()));
					parameters.put("familyName", currentHoldingReportDTO.getFamilyName());
					parameters.put("emailAddress", currentHoldingReportDTO.getClientEmail());
					parameters.put("mobileNo", currentHoldingReportDTO.getClientMobile());
					parameters.put("clientName", currentHoldingReportDTO.getNameClient());
					parameters.put("pan", currentHoldingReportDTO.getClientPAN());
					parameters.put("RealizedGainDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "RealizedGainReport.pdf"));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				configuration.setCreatingBatchModeBookmarks(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/generateUnealizedGainReport", method = RequestMethod.GET)
	public ResponseEntity<?> generateUnealizedGainReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "asOnDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date asOnDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {
		try {

			String returnStatus = "";
			String asOnDateInput = "";

			CurrentHoldingReportDTO inputDTO = new CurrentHoldingReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			asOnDateInput = FinexaUtil.getProperDateInput(c1, asOnDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setAsOnDate(formatter.parse(asOnDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setSchemeMap(unrealisedGainBOService.unrealisedGainReport(inputDTO));

			System.out.println(inputDTO.getSchemeMap().keySet());
			System.out.println(inputDTO.getSchemeMap().entrySet());

			returnStatus = generateUnrealizedGainReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException | InstantiationException | IllegalAccessException | ClassNotFoundException
				| ParseException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-URGR01",
					"Failed to export Unrealized Gain Report", e);
		}

	}

	private String generateUnrealizedGainReport(CurrentHoldingReportDTO currentHoldingReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"UnrealizedGainReport.html\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (currentHoldingReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(currentHoldingReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (currentHoldingReportDTO.getSchemeMap() != null && currentHoldingReportDTO.getSchemeMap().size() > 0) {
				for (Map.Entry<String, List<CurrentHoldingReportColumnDTO>> dataSourceMapEntry : currentHoldingReportDTO
						.getSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/unrealizedGainReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", currentHoldingReportDTO.getDistributorName());
					parameters.put("distributorEmail", currentHoldingReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", currentHoldingReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(currentHoldingReportDTO.getAsOnDate()));
					parameters.put("familyName", currentHoldingReportDTO.getFamilyName());
					parameters.put("emailAddress", currentHoldingReportDTO.getClientEmail());
					parameters.put("mobileNo", currentHoldingReportDTO.getClientMobile());
					parameters.put("clientName", currentHoldingReportDTO.getNameClient());
					parameters.put("pan", currentHoldingReportDTO.getClientPAN());
					parameters.put("UnrealizedGainDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporterHTML.setExporterOutput(
						new SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/UnrealizedGainReport.html"));
				// exporterHTML.setExporterOutput(new
				// SimpleHtmlExporterOutput("/home/supratim/MFBackOfficeJava11Workspace/FinexaWebMerged/src/main/webapp/MyBusiness/resources/UnrealizedGainReport.html"));
				exporterHTML.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/unrealizedGainExport", method = RequestMethod.GET)
	public ResponseEntity<?> createUnrealizedGainReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "asOnDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date asOnDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String asOnDateInput = "";

			CurrentHoldingReportDTO inputDTO = new CurrentHoldingReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			asOnDateInput = FinexaUtil.getProperDateInput(c1, asOnDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setAsOnDate(formatter.parse(asOnDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setSchemeMap(unrealisedGainBOService.unrealisedGainReport(inputDTO));

			System.out.println(inputDTO.getSchemeMap().keySet());
			System.out.println(inputDTO.getSchemeMap().entrySet());

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportUnrealizedGainReportExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportUnrealizedGainReportPDF(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException | InstantiationException | IllegalAccessException | ClassNotFoundException
				| ParseException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-URGR01",
					"Failed to export Unrealized Gain Report", e);
		}

	}

	private String exportUnrealizedGainReportExcel(CurrentHoldingReportDTO currentHoldingReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"UnrealizedGainReport.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (currentHoldingReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(currentHoldingReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (currentHoldingReportDTO.getSchemeMap() != null && currentHoldingReportDTO.getSchemeMap().size() > 0) {
				for (Map.Entry<String, List<CurrentHoldingReportColumnDTO>> dataSourceMapEntry : currentHoldingReportDTO
						.getSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/unrealizedGainReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", currentHoldingReportDTO.getDistributorName());
					parameters.put("distributorEmail", currentHoldingReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", currentHoldingReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(currentHoldingReportDTO.getAsOnDate()));
					parameters.put("familyName", currentHoldingReportDTO.getFamilyName());
					parameters.put("emailAddress", currentHoldingReportDTO.getClientEmail());
					parameters.put("mobileNo", currentHoldingReportDTO.getClientMobile());
					parameters.put("clientName", currentHoldingReportDTO.getNameClient());
					parameters.put("pan", currentHoldingReportDTO.getClientPAN());
					parameters.put("UnrealizedGainDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "UnrealizedGainReport.xls"));
				SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
				xlsReportConfiguration.setOnePagePerSheet(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
				xlsReportConfiguration.setDetectCellType(true);
				xlsReportConfiguration.setWhitePageBackground(false);
				xlsReportConfiguration.setIgnoreGraphics(false);
				xlsExporter.setConfiguration(xlsReportConfiguration);

				xlsExporter.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	private String exportUnrealizedGainReportPDF(CurrentHoldingReportDTO currentHoldingReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"UnrealizedGainReport.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (currentHoldingReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(currentHoldingReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (currentHoldingReportDTO.getSchemeMap() != null && currentHoldingReportDTO.getSchemeMap().size() > 0) {
				for (Map.Entry<String, List<CurrentHoldingReportColumnDTO>> dataSourceMapEntry : currentHoldingReportDTO
						.getSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/unrealizedGainReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", currentHoldingReportDTO.getDistributorName());
					parameters.put("distributorEmail", currentHoldingReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", currentHoldingReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(currentHoldingReportDTO.getAsOnDate()));
					parameters.put("familyName", currentHoldingReportDTO.getFamilyName());
					parameters.put("emailAddress", currentHoldingReportDTO.getClientEmail());
					parameters.put("mobileNo", currentHoldingReportDTO.getClientMobile());
					parameters.put("clientName", currentHoldingReportDTO.getNameClient());
					parameters.put("pan", currentHoldingReportDTO.getClientPAN());
					parameters.put("UnrealizedGainDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "UnrealizedGainReport.pdf"));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				configuration.setCreatingBatchModeBookmarks(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/generateDividendReport", method = RequestMethod.GET)
	public ResponseEntity<?> generateDividendReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			DividendReportDTO inputDTO = new DividendReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setFolioSchemeMap(dividendReportBOService.dividendReport(inputDTO));

			returnStatus = generateDividendReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException | ParseException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-DRHTML", "Failed to export Dividend Report.", e);
		}

	}

	private String generateDividendReport(DividendReportDTO dividendReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"DividendReport.html\""));

			BufferedImage logoImage = null;
			if (dividendReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(dividendReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			if (dividendReportDTO.getFolioSchemeMap() != null && dividendReportDTO.getFolioSchemeMap().size() > 0) {
				for (Map.Entry<String, List<DividendReportColumnDTO>> dataSourceMapEntry : dividendReportDTO
						.getFolioSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/dividendReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", dividendReportDTO.getDistributorName());
					parameters.put("distributorEmail", dividendReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", dividendReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(dividendReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(dividendReportDTO.getToDate()));
					parameters.put("fundHouse", dividendReportDTO.getFundHouse());
					parameters.put("familyName", dividendReportDTO.getFamilyName());
					parameters.put("emailAddress", dividendReportDTO.getClientEmail());
					parameters.put("mobileNo", dividendReportDTO.getClientMobile());
					parameters.put("clientName", dividendReportDTO.getNameClient());
					parameters.put("pan", dividendReportDTO.getClientPAN());
					parameters.put("DividendDataSource", jrBeanCollectionDataSource);
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporterHTML.setExporterOutput(
						new SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/DividendReport.html"));
				// exporterHTML.setExporterOutput(new
				// SimpleHtmlExporterOutput("/home/supratim/MFBackOfficeJava11Workspace/FinexaWebMerged/src/main/webapp/MyBusiness/resources/DividendReport.html"));
				exporterHTML.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/generateDividendReportNew", method = RequestMethod.GET)
	public ResponseEntity<?> generateDividendReportNew(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			DividendReportDTO inputDTO = new DividendReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setFolioSchemeMapNew(dividendReportBOServiceNew.dividendReport(inputDTO));

			returnStatus = generateDividendReportNew(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException | ParseException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-DRHTML", "Failed to export Dividend Report.", e);
		}

	}

	private String generateDividendReportNew(DividendReportDTO dividendReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"DividendReportNew.html\""));

			BufferedImage logoImage = null;
			if (dividendReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(dividendReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			if (dividendReportDTO.getFolioSchemeMapNew() != null
					&& dividendReportDTO.getFolioSchemeMapNew().size() > 0) {
				for (Map.Entry<String, List<DividendReportColumnNewDTO>> dataSourceMapEntry : dividendReportDTO
						.getFolioSchemeMapNew().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/dividendReportNew.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", dividendReportDTO.getDistributorName());
					parameters.put("distributorEmail", dividendReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", dividendReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(dividendReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(dividendReportDTO.getToDate()));
					parameters.put("fundHouse", dividendReportDTO.getFundHouse());
					parameters.put("familyName", dividendReportDTO.getFamilyName());
					parameters.put("emailAddress", dividendReportDTO.getClientEmail());
					parameters.put("mobileNo", dividendReportDTO.getClientMobile());
					parameters.put("clientName", dividendReportDTO.getNameClient());
					parameters.put("pan", dividendReportDTO.getClientPAN());
					parameters.put("DividendDataSource", jrBeanCollectionDataSource);
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporterHTML.setExporterOutput(
						new SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/DividendReportNew.html"));
				// exporterHTML.setExporterOutput(new
				// SimpleHtmlExporterOutput("/home/supratim/DummyMasterLatestWorkspace/FinexaWeb/src/main/webapp/MyBusiness/resources/DividendReportNew.html"));
				exporterHTML.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/dividendReportExport", method = RequestMethod.GET)
	public ResponseEntity<?> createDividendReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			DividendReportDTO inputDTO = new DividendReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setFolioSchemeMap(dividendReportBOService.dividendReport(inputDTO));

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportDividendReportExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportDividendReportPDF(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException | ParseException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-DR01", "Failed to export Dividend Report.", e);
		}
	}

	@RequestMapping(value = "/dividendReportExportNew", method = RequestMethod.GET)
	public ResponseEntity<?> createDividendReportNew(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			DividendReportDTO inputDTO = new DividendReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(schemeName);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setFolioSchemeMapNew(dividendReportBOServiceNew.dividendReport(inputDTO));

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportDividendReportExcelNew(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportDividendReportPDFNew(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException | ParseException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-DR01", "Failed to export Dividend Report.", e);
		}
	}

	private String exportDividendReportExcel(DividendReportDTO dividendReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"DividendReport.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			BufferedImage logoImage = null;
			if (dividendReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(dividendReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (dividendReportDTO.getFolioSchemeMap() != null && dividendReportDTO.getFolioSchemeMap().size() > 0) {
				for (Map.Entry<String, List<DividendReportColumnDTO>> dataSourceMapEntry : dividendReportDTO
						.getFolioSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/dividendReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", dividendReportDTO.getDistributorName());
					parameters.put("distributorEmail", dividendReportDTO.getDistributorAddress());
					parameters.put("distributorMobile", dividendReportDTO.getDistributorContactDetails());
					parameters.put("fromDate", formatter.format(dividendReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(dividendReportDTO.getToDate()));
					parameters.put("fundHouse", dividendReportDTO.getFundHouse());
					parameters.put("familyName", dividendReportDTO.getFamilyName());
					parameters.put("emailAddress", dividendReportDTO.getClientEmail());
					parameters.put("mobileNo", dividendReportDTO.getClientMobile());
					parameters.put("clientName", dividendReportDTO.getNameClient());
					parameters.put("pan", dividendReportDTO.getClientPAN());
					parameters.put("DividendDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "DividendReport.xls"));
				SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
				xlsReportConfiguration.setOnePagePerSheet(false);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
				xlsReportConfiguration.setWrapText(true);
				xlsReportConfiguration.setFontSizeFixEnabled(true);
				xlsReportConfiguration.setDetectCellType(true);
				xlsReportConfiguration.setWhitePageBackground(false);
				xlsExporter.setConfiguration(xlsReportConfiguration);

				xlsExporter.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	private String exportDividendReportExcelNew(DividendReportDTO dividendReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"DividendReportNew.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			BufferedImage logoImage = null;
			if (dividendReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(dividendReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (dividendReportDTO.getFolioSchemeMapNew() != null
					&& dividendReportDTO.getFolioSchemeMapNew().size() > 0) {
				for (Map.Entry<String, List<DividendReportColumnNewDTO>> dataSourceMapEntry : dividendReportDTO
						.getFolioSchemeMapNew().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/dividendReportNew.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", dividendReportDTO.getDistributorName());
					parameters.put("distributorEmail", dividendReportDTO.getDistributorAddress());
					parameters.put("distributorMobile", dividendReportDTO.getDistributorContactDetails());
					parameters.put("fromDate", formatter.format(dividendReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(dividendReportDTO.getToDate()));
					parameters.put("fundHouse", dividendReportDTO.getFundHouse());
					parameters.put("familyName", dividendReportDTO.getFamilyName());
					parameters.put("emailAddress", dividendReportDTO.getClientEmail());
					parameters.put("mobileNo", dividendReportDTO.getClientMobile());
					parameters.put("clientName", dividendReportDTO.getNameClient());
					parameters.put("pan", dividendReportDTO.getClientPAN());
					parameters.put("DividendDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "DividendReportNew.xls"));
				SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
				xlsReportConfiguration.setOnePagePerSheet(false);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
				xlsReportConfiguration.setWrapText(true);
				xlsReportConfiguration.setFontSizeFixEnabled(true);
				xlsReportConfiguration.setDetectCellType(true);
				xlsReportConfiguration.setWhitePageBackground(false);
				xlsExporter.setConfiguration(xlsReportConfiguration);

				xlsExporter.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	private String exportDividendReportPDF(DividendReportDTO dividendReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"DividendReport.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			BufferedImage logoImage = null;
			if (dividendReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(dividendReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (dividendReportDTO.getFolioSchemeMap() != null && dividendReportDTO.getFolioSchemeMap().size() > 0) {
				for (Map.Entry<String, List<DividendReportColumnDTO>> dataSourceMapEntry : dividendReportDTO
						.getFolioSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/dividendReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", dividendReportDTO.getDistributorName());
					parameters.put("distributorEmail", dividendReportDTO.getDistributorAddress());
					parameters.put("distributorMobile", dividendReportDTO.getDistributorContactDetails());
					parameters.put("fromDate", formatter.format(dividendReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(dividendReportDTO.getToDate()));
					parameters.put("fundHouse", dividendReportDTO.getFundHouse());
					parameters.put("familyName", dividendReportDTO.getFamilyName());
					parameters.put("emailAddress", dividendReportDTO.getClientEmail());
					parameters.put("mobileNo", dividendReportDTO.getClientMobile());
					parameters.put("clientName", dividendReportDTO.getNameClient());
					parameters.put("pan", dividendReportDTO.getClientPAN());
					parameters.put("DividendDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "DividendReport.pdf"));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				configuration.setCreatingBatchModeBookmarks(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	private String exportDividendReportPDFNew(DividendReportDTO dividendReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"DividendReportNew.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			BufferedImage logoImage = null;
			if (dividendReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(dividendReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (dividendReportDTO.getFolioSchemeMapNew() != null
					&& dividendReportDTO.getFolioSchemeMapNew().size() > 0) {
				for (Map.Entry<String, List<DividendReportColumnNewDTO>> dataSourceMapEntry : dividendReportDTO
						.getFolioSchemeMapNew().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/dividendReportNew.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", dividendReportDTO.getDistributorName());
					parameters.put("distributorEmail", dividendReportDTO.getDistributorAddress());
					parameters.put("distributorMobile", dividendReportDTO.getDistributorContactDetails());
					parameters.put("fromDate", formatter.format(dividendReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(dividendReportDTO.getToDate()));
					parameters.put("fundHouse", dividendReportDTO.getFundHouse());
					parameters.put("familyName", dividendReportDTO.getFamilyName());
					parameters.put("emailAddress", dividendReportDTO.getClientEmail());
					parameters.put("mobileNo", dividendReportDTO.getClientMobile());
					parameters.put("clientName", dividendReportDTO.getNameClient());
					parameters.put("pan", dividendReportDTO.getClientPAN());
					parameters.put("DividendDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "DividendReportNew.pdf"));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				configuration.setCreatingBatchModeBookmarks(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/generateCapitalGainsReport", method = RequestMethod.GET)
	public ResponseEntity<?> generateCapitalGainsReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String isin,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response) throws IOException,
			JRException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			CapitalGainsReportDTO inputDTO = new CapitalGainsReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(isin);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setFolioSchemeMap(capitalGainsBOService.getCapitalGainsReport(inputDTO));

			returnStatus = generateCapitalGainsReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<String>("Done", HttpStatus.OK);

	}

	private String generateCapitalGainsReport(CapitalGainsReportDTO capitalGainsReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"CapitalGainsReport.html\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (capitalGainsReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(capitalGainsReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (capitalGainsReportDTO.getFolioSchemeMap() != null
					&& capitalGainsReportDTO.getFolioSchemeMap().size() > 0) {
				for (Map.Entry<String, List<CapitalGainsReportColumnDTO>> dataSourceMapEntry : capitalGainsReportDTO
						.getFolioSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/capitalGainsReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", capitalGainsReportDTO.getDistributorName());
					parameters.put("distributorEmail", capitalGainsReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", capitalGainsReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(capitalGainsReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(capitalGainsReportDTO.getToDate()));
					parameters.put("fundHouse", capitalGainsReportDTO.getFundHouse());
					parameters.put("familyName", capitalGainsReportDTO.getFamilyName());
					parameters.put("emailAddress", capitalGainsReportDTO.getClientEmail());
					parameters.put("mobileNo", capitalGainsReportDTO.getClientMobile());
					parameters.put("clientName", capitalGainsReportDTO.getNameClient());
					parameters.put("pan", capitalGainsReportDTO.getClientPAN());
					parameters.put("CapitalGainsDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporterHTML.setExporterOutput(
						new SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/CapitalGainsReport.html"));
				// exporterHTML.setExporterOutput(new
				// SimpleHtmlExporterOutput("/home/debolina/Documents/DummyMasterWorkspace/FinexaWeb/src/main/webapp/MyBusiness/resources/CapitalGainsReport.html"));
				exporterHTML.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/exportCapitalGainsReport", method = RequestMethod.GET)
	public ResponseEntity<?> exportCapitalGainsReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String isin,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response) throws IOException,
			JRException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			CapitalGainsReportDTO inputDTO = new CapitalGainsReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(isin);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setFolioSchemeMap(capitalGainsBOService.getCapitalGainsReport(inputDTO));

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportCapitalGainsReportExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportCapitalGainsReportPDF(inputDTO, response);
			}
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<String>("Done", HttpStatus.OK);

	}

	private String exportCapitalGainsReportPDF(CapitalGainsReportDTO capitalGainsReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"CapitalGainsReport.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (capitalGainsReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(capitalGainsReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (capitalGainsReportDTO.getFolioSchemeMap() != null
					&& capitalGainsReportDTO.getFolioSchemeMap().size() > 0) {
				for (Map.Entry<String, List<CapitalGainsReportColumnDTO>> dataSourceMapEntry : capitalGainsReportDTO
						.getFolioSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/capitalGainsReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", capitalGainsReportDTO.getDistributorName());
					parameters.put("distributorEmail", capitalGainsReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", capitalGainsReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(capitalGainsReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(capitalGainsReportDTO.getToDate()));
					parameters.put("fundHouse", capitalGainsReportDTO.getFundHouse());
					parameters.put("familyName", capitalGainsReportDTO.getFamilyName());
					parameters.put("emailAddress", capitalGainsReportDTO.getClientEmail());
					parameters.put("mobileNo", capitalGainsReportDTO.getClientMobile());
					parameters.put("clientName", capitalGainsReportDTO.getNameClient());
					parameters.put("pan", capitalGainsReportDTO.getClientPAN());
					parameters.put("CapitalGainsDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "CapitalGainsReport.pdf"));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				configuration.setCreatingBatchModeBookmarks(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	private String exportCapitalGainsReportExcel(CapitalGainsReportDTO capitalGainsReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"CapitalGainsReport.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (capitalGainsReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(capitalGainsReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (capitalGainsReportDTO.getFolioSchemeMap() != null
					&& capitalGainsReportDTO.getFolioSchemeMap().size() > 0) {
				for (Map.Entry<String, List<CapitalGainsReportColumnDTO>> dataSourceMapEntry : capitalGainsReportDTO
						.getFolioSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/capitalGainsReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", capitalGainsReportDTO.getDistributorName());
					parameters.put("distributorEmail", capitalGainsReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", capitalGainsReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(capitalGainsReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(capitalGainsReportDTO.getToDate()));
					parameters.put("fundHouse", capitalGainsReportDTO.getFundHouse());
					parameters.put("familyName", capitalGainsReportDTO.getFamilyName());
					parameters.put("emailAddress", capitalGainsReportDTO.getClientEmail());
					parameters.put("mobileNo", capitalGainsReportDTO.getClientMobile());
					parameters.put("clientName", capitalGainsReportDTO.getNameClient());
					parameters.put("pan", capitalGainsReportDTO.getClientPAN());
					parameters.put("CapitalGainsDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "CapitalGainsReport.xls"));
				SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
				xlsReportConfiguration.setOnePagePerSheet(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
				xlsReportConfiguration.setDetectCellType(true);
				xlsReportConfiguration.setWhitePageBackground(false);
				xlsReportConfiguration.setIgnoreGraphics(false);
				xlsExporter.setConfiguration(xlsReportConfiguration);

				xlsExporter.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/generatePortfolioGainLossReport", method = RequestMethod.GET)
	public ResponseEntity<?> generatePortfolioGainLossReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "asOnDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date asOnDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String isin,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response) throws IOException,
			JRException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException {

		try {

			String returnStatus = "";
			String asOnDateInput = "";

			PortfolioGainLossReportDTO inputDTO = new PortfolioGainLossReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);
			inputDTO.setSchemeName(isin);
			Calendar c1 = Calendar.getInstance();
			asOnDateInput = FinexaUtil.getProperDateInput(c1, asOnDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setAsOnDate(formatter.parse(asOnDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(isin);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setSchemeMap(portfolioGainLossRpeortBOService.portfolioGainLossReport(inputDTO));

			returnStatus = generatePortfolioGainLossReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<String>("Done", HttpStatus.OK);

	}

	private String generatePortfolioGainLossReport(PortfolioGainLossReportDTO portfolioGainLossReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"PortfolioGainLossReport.html\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (portfolioGainLossReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(portfolioGainLossReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (portfolioGainLossReportDTO.getSchemeMap() != null
					&& portfolioGainLossReportDTO.getSchemeMap().size() > 0) {
				for (Map.Entry<String, List<PortfolioGainLossReportColumnDTO>> dataSourceMapEntry : portfolioGainLossReportDTO
						.getSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/portfolioGainLossReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", portfolioGainLossReportDTO.getDistributorName());
					parameters.put("distributorEmail", portfolioGainLossReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", portfolioGainLossReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(portfolioGainLossReportDTO.getAsOnDate()));
					parameters.put("fundHouse", portfolioGainLossReportDTO.getFundHouse());
					parameters.put("familyName", portfolioGainLossReportDTO.getFamilyName());
					parameters.put("emailAddress", portfolioGainLossReportDTO.getClientEmail());
					parameters.put("mobileNo", portfolioGainLossReportDTO.getClientMobile());
					parameters.put("clientName", portfolioGainLossReportDTO.getNameClient());
					parameters.put("pan", portfolioGainLossReportDTO.getClientPAN());
					parameters.put("PortfolioGainLossDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporterHTML.setExporterOutput(new SimpleHtmlExporterOutput(
						"/var/www/html/MyBusiness/resources/PortfolioGainLossReport.html"));
				// exporterHTML.setExporterOutput(new
				// SimpleHtmlExporterOutput("/home/debolina/Documents/DummyMasterWorkspace/FinexaWeb/src/main/webapp/MyBusiness/resources/PortfolioGainLossReport.html"));
				exporterHTML.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/generatePortfolioValuationReport", method = RequestMethod.GET)
	public ResponseEntity<?> generatePortfolioValuationReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "asOnDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date asOnDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String isin,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response) throws IOException,
			JRException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException {

		try {

			String returnStatus = "";
			String asOnDateInput = "";

			PortfolioValuationReportDTO inputDTO = new PortfolioValuationReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			asOnDateInput = FinexaUtil.getProperDateInput(c1, asOnDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setAsOnDate(formatter.parse(asOnDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(isin);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setFolioSchemeMap(portfolioValuationReportservice.portfolioValuationReport(inputDTO));

			returnStatus = generatePortfolioValuationReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<String>("Done", HttpStatus.OK);

	}

	private String generatePortfolioValuationReport(PortfolioValuationReportDTO portfolioValuationReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"PortfolioValuationReport.html\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (portfolioValuationReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(portfolioValuationReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (portfolioValuationReportDTO.getFolioSchemeMap() != null
					&& portfolioValuationReportDTO.getFolioSchemeMap().size() > 0) {
				for (Map.Entry<String, List<PortfolioValuationReportColumnDTO>> dataSourceMapEntry : portfolioValuationReportDTO
						.getFolioSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/portfolioValuationReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", portfolioValuationReportDTO.getDistributorName());
					parameters.put("distributorEmail", portfolioValuationReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", portfolioValuationReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(portfolioValuationReportDTO.getAsOnDate()));
					parameters.put("fundHouse", portfolioValuationReportDTO.getFundHouse());
					parameters.put("familyName", portfolioValuationReportDTO.getFamilyName());
					parameters.put("emailAddress", portfolioValuationReportDTO.getClientEmail());
					parameters.put("mobileNo", portfolioValuationReportDTO.getClientMobile());
					parameters.put("clientName", portfolioValuationReportDTO.getNameClient());
					parameters.put("pan", portfolioValuationReportDTO.getClientPAN());
					parameters.put("PortfolioValuationDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporterHTML.setExporterOutput(new SimpleHtmlExporterOutput(
						"/var/www/html/MyBusiness/resources/PortfolioValuationReport.html"));
				// exporterHTML.setExporterOutput(new
				// SimpleHtmlExporterOutput("/home/debolina/Documents/DummyMasterWorkspace/FinexaWeb/src/main/webapp/MyBusiness/resources/PortfolioValuationReport.html"));
				exporterHTML.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/portfolioValuationExport", method = RequestMethod.GET)
	public ResponseEntity<?> createPortfolioValuationReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "asOnDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date asOnDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String isin,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response) throws IOException,
			JRException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException {

		try {

			String returnStatus = "";
			String asOnDateInput = "";

			PortfolioValuationReportDTO inputDTO = new PortfolioValuationReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);

			Calendar c1 = Calendar.getInstance();
			asOnDateInput = FinexaUtil.getProperDateInput(c1, asOnDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setAsOnDate(formatter.parse(asOnDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);
			inputDTO.setSchemeName(isin);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setFolioSchemeMap(portfolioValuationReportservice.portfolioValuationReport(inputDTO));

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportPortfolioValuationReportExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportPortfolioValuationReportPDF(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<String>("Done", HttpStatus.OK);

	}

	private String exportPortfolioValuationReportExcel(PortfolioValuationReportDTO portfolioValuationReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"PortfolioValuationReport.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (portfolioValuationReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(portfolioValuationReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (portfolioValuationReportDTO.getFolioSchemeMap() != null
					&& portfolioValuationReportDTO.getFolioSchemeMap().size() > 0) {
				for (Map.Entry<String, List<PortfolioValuationReportColumnDTO>> dataSourceMapEntry : portfolioValuationReportDTO
						.getFolioSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/portfolioValuationReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", portfolioValuationReportDTO.getDistributorName());
					parameters.put("distributorEmail", portfolioValuationReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", portfolioValuationReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(portfolioValuationReportDTO.getAsOnDate()));
					parameters.put("fundHouse", portfolioValuationReportDTO.getFundHouse());
					parameters.put("familyName", portfolioValuationReportDTO.getFamilyName());
					parameters.put("emailAddress", portfolioValuationReportDTO.getClientEmail());
					parameters.put("mobileNo", portfolioValuationReportDTO.getClientMobile());
					parameters.put("clientName", portfolioValuationReportDTO.getNameClient());
					parameters.put("pan", portfolioValuationReportDTO.getClientPAN());
					parameters.put("PortfolioValuationDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "PortfolioValuationReport.xls"));
				SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
				xlsReportConfiguration.setOnePagePerSheet(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
				xlsReportConfiguration.setDetectCellType(true);
				xlsReportConfiguration.setWhitePageBackground(false);
				xlsReportConfiguration.setIgnoreGraphics(false);
				xlsExporter.setConfiguration(xlsReportConfiguration);

				xlsExporter.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	private String exportPortfolioValuationReportPDF(PortfolioValuationReportDTO portfolioValuationReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"PortfolioValuationReport.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (portfolioValuationReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(portfolioValuationReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (portfolioValuationReportDTO.getFolioSchemeMap() != null
					&& portfolioValuationReportDTO.getFolioSchemeMap().size() > 0) {
				for (Map.Entry<String, List<PortfolioValuationReportColumnDTO>> dataSourceMapEntry : portfolioValuationReportDTO
						.getFolioSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/portfolioValuationReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", portfolioValuationReportDTO.getDistributorName());
					parameters.put("distributorEmail", portfolioValuationReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", portfolioValuationReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(portfolioValuationReportDTO.getAsOnDate()));
					parameters.put("fundHouse", portfolioValuationReportDTO.getFundHouse());
					parameters.put("familyName", portfolioValuationReportDTO.getFamilyName());
					parameters.put("emailAddress", portfolioValuationReportDTO.getClientEmail());
					parameters.put("mobileNo", portfolioValuationReportDTO.getClientMobile());
					parameters.put("clientName", portfolioValuationReportDTO.getNameClient());
					parameters.put("pan", portfolioValuationReportDTO.getClientPAN());
					parameters.put("PortfolioValuationDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "PortfolioValuationReport.pdf"));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				configuration.setCreatingBatchModeBookmarks(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/portfolioGainLossExport", method = RequestMethod.GET)
	public ResponseEntity<?> createPortfolioGainLossReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "asOnDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date asOnDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "advisorID") int advisorID, HttpServletResponse response) throws IOException,
			JRException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException {

		try {

			String returnStatus = "";
			String asOnDateInput = "";

			PortfolioGainLossReportDTO inputDTO = new PortfolioGainLossReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);
			inputDTO.setSchemeName(schemeName);
			Calendar c1 = Calendar.getInstance();
			asOnDateInput = FinexaUtil.getProperDateInput(c1, asOnDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setAsOnDate(formatter.parse(asOnDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			Map<String, List<PortfolioGainLossReportColumnDTO>> inputMap = new HashMap<String, List<PortfolioGainLossReportColumnDTO>>();
			List<PortfolioGainLossReportColumnDTO> newDTOList = new ArrayList<PortfolioGainLossReportColumnDTO>();

			inputDTO.setSchemeMap(portfolioGainLossRpeortBOService.portfolioGainLossReport(inputDTO));

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportGainLossReportExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportGainLossReportPDF(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<String>("Done", HttpStatus.OK);
	}

	private String exportGainLossReportExcel(PortfolioGainLossReportDTO portfolioGainLossReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"PortfolioGainLossReport.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (portfolioGainLossReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(portfolioGainLossReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (portfolioGainLossReportDTO.getSchemeMap() != null
					&& portfolioGainLossReportDTO.getSchemeMap().size() > 0) {
				for (Map.Entry<String, List<PortfolioGainLossReportColumnDTO>> dataSourceMapEntry : portfolioGainLossReportDTO
						.getSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/portfolioGainLossReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", portfolioGainLossReportDTO.getDistributorName());
					parameters.put("distributorEmail", portfolioGainLossReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", portfolioGainLossReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(portfolioGainLossReportDTO.getAsOnDate()));
					parameters.put("fundHouse", portfolioGainLossReportDTO.getFundHouse());
					parameters.put("familyName", portfolioGainLossReportDTO.getFamilyName());
					parameters.put("emailAddress", portfolioGainLossReportDTO.getClientEmail());
					parameters.put("mobileNo", portfolioGainLossReportDTO.getClientMobile());
					parameters.put("clientName", portfolioGainLossReportDTO.getNameClient());
					parameters.put("pan", portfolioGainLossReportDTO.getClientPAN());
					parameters.put("PortfolioGainLossDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "PortfolioGainLossReport.xls"));
				SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
				xlsReportConfiguration.setOnePagePerSheet(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
				xlsReportConfiguration.setDetectCellType(true);
				xlsReportConfiguration.setWhitePageBackground(false);
				xlsReportConfiguration.setIgnoreGraphics(false);
				xlsExporter.setConfiguration(xlsReportConfiguration);

				xlsExporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	private String exportGainLossReportPDF(PortfolioGainLossReportDTO portfolioGainLossReportDTO,
			HttpServletResponse response) throws IOException, JRException {

		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"PortfolioGainLossReport.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (portfolioGainLossReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(portfolioGainLossReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (portfolioGainLossReportDTO.getSchemeMap() != null
					&& portfolioGainLossReportDTO.getSchemeMap().size() > 0) {
				for (Map.Entry<String, List<PortfolioGainLossReportColumnDTO>> dataSourceMapEntry : portfolioGainLossReportDTO
						.getSchemeMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:backOfficeReportsJrxml/portfolioGainLossReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", portfolioGainLossReportDTO.getDistributorName());
					parameters.put("distributorEmail", portfolioGainLossReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", portfolioGainLossReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(portfolioGainLossReportDTO.getAsOnDate()));
					parameters.put("fundHouse", portfolioGainLossReportDTO.getFundHouse());
					parameters.put("familyName", portfolioGainLossReportDTO.getFamilyName());
					parameters.put("emailAddress", portfolioGainLossReportDTO.getClientEmail());
					parameters.put("mobileNo", portfolioGainLossReportDTO.getClientMobile());
					parameters.put("clientName", portfolioGainLossReportDTO.getNameClient());
					parameters.put("pan", portfolioGainLossReportDTO.getClientPAN());
					parameters.put("PortfolioGainLossDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "PortfolioGainLossReport.pdf"));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				configuration.setCreatingBatchModeBookmarks(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;

	}

	@RequestMapping(value = "/generateSipStpSwpReport", method = RequestMethod.GET)
	public ResponseEntity<?> generateSipStpSwpReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "reportType") String reportType, @RequestParam(value = "advisorID") int advisorID,
			HttpServletResponse response) throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			SIPSTPSWPReportDTO inputDTO = new SIPSTPSWPReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);
			inputDTO.setReportType(reportType);
			inputDTO.setSchemeName(schemeName);
			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setSipStpSwpDataMap(sipSTPSWPReportService.sipSTPSWPReport(inputDTO));

			returnStatus = generateSipStpSwpReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException | ParseException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-SSSRHTML",
					"Failed to Generate Sip Stp Swp Report.", e);
		}

	}

	private String generateSipStpSwpReport(SIPSTPSWPReportDTO sipStpSwpReportDTO, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"SipStpSwpReport.html\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			BufferedImage logoImage = null;
			if (sipStpSwpReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(sipStpSwpReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (sipStpSwpReportDTO.getSipStpSwpDataMap() != null
					&& sipStpSwpReportDTO.getSipStpSwpDataMap().size() > 0) {
				for (Map.Entry<String, List<SIPSTPSWPReportColumnDTO>> dataSourceMapEntry : sipStpSwpReportDTO
						.getSipStpSwpDataMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/sipSTPSWPReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", sipStpSwpReportDTO.getDistributorName());
					parameters.put("distributorEmail", sipStpSwpReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", sipStpSwpReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(sipStpSwpReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(sipStpSwpReportDTO.getToDate()));
					parameters.put("fundHouse", sipStpSwpReportDTO.getFundHouse());
					parameters.put("familyName", sipStpSwpReportDTO.getFamilyName());
					parameters.put("emailAddress", sipStpSwpReportDTO.getClientEmail());
					parameters.put("mobileNo", sipStpSwpReportDTO.getClientMobile());
					parameters.put("clientName", sipStpSwpReportDTO.getNameClient());
					parameters.put("pan", sipStpSwpReportDTO.getClientPAN());
					parameters.put("SipStpSwpDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporterHTML.setExporterOutput(
						new SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/SipStpSwpReport.html"));
				// exporterHTML.setExporterOutput(new
				// SimpleHtmlExporterOutput("/home/supratim/MFBackOfficeJava11Workspace/FinexaWebMerged/src/main/webapp/MyBusiness/resources/SipStpSwpReport.html"));
				exporterHTML.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	@RequestMapping(value = "/sipStpSwpExport", method = RequestMethod.GET)
	public ResponseEntity<?> createSipStpSwpReport(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "familyMemberIdArr") Integer[] familyMemberIdArr,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "reportType") String reportType, @RequestParam(value = "advisorID") int advisorID,
			HttpServletResponse response) throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			SIPSTPSWPReportDTO inputDTO = new SIPSTPSWPReportDTO();
			inputDTO.setClientId(clientId);
			inputDTO.setFamilyMemberIdArr(familyMemberIdArr);
			inputDTO.setReportType(reportType);
			inputDTO.setSchemeName(schemeName);
			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setFundHouse(fundHouse);

			AdvisorUser au = advisorUserRepository.findOne(advisorID);
			if (au != null) {

				if (au.getLogo() != null) {
					inputDTO.setLogo(au.getLogo());
				}

				inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());

				inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) + ", "
						+ (au.getState() == null ? " " : au.getState()) + ", "
						+ (au.getLookupCountry().getNicename() == null ? " " : au.getLookupCountry().getNicename()));
				inputDTO.setDistributorContactDetails(
						(au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " "
								+ (au.getPhoneNo() == null ? " " : au.getPhoneNo()));

				inputDTO.setDistributorEmail(au.getEmailID());
				inputDTO.setDistributorMobile("" + au.getPhoneNo());

			}

			ClientMaster cm = clientMasterRepository.findOne(clientId);
			if (cm != null) {
				if (cm.getMiddleName() == null) {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getLastName());
				} else {
					inputDTO.setNameClient(cm.getFirstName() + " " + cm.getMiddleName() + " " + cm.getLastName());
				}

				inputDTO.setClientPAN(cm.getPan());

				List<ClientFamilyMember> cfmList = cm.getClientFamilyMembers();
				if (cfmList != null) {
					for (ClientFamilyMember cfm : cfmList) {
						if (cfm.getIsFamilyHead() != null) {
							if (cfm.getIsFamilyHead().equals("Y")) {
								if (cfm.getMiddleName() == null) {
									inputDTO.setFamilyName(cfm.getFirstName() + " " + cfm.getLastName());
								} else {
									inputDTO.setFamilyName(
											cfm.getFirstName() + " " + cfm.getMiddleName() + " " + cfm.getLastName());
								}
							}
						}
					}
				}

				List<ClientContact> contactList = cm.getClientContacts();
				if (contactList != null) {
					inputDTO.setClientEmail(contactList.get(0).getEmailID());
					inputDTO.setClientMobile(String.valueOf(contactList.get(0).getMobile()));
				}
			}

			inputDTO.setSipStpSwpDataMap(sipSTPSWPReportService.sipSTPSWPReport(inputDTO));

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportSipStpSwpReportExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportSipStpSwpReportPDF(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException | ParseException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-SSSR01", "Failed to Export Sip Stp Swp Report.",
					e);
		}
	}

	private String exportSipStpSwpReportExcel(SIPSTPSWPReportDTO sipStpSwpReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"SipStpSwpReport.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			BufferedImage logoImage = null;
			if (sipStpSwpReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(sipStpSwpReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (sipStpSwpReportDTO.getSipStpSwpDataMap() != null
					&& sipStpSwpReportDTO.getSipStpSwpDataMap().size() > 0) {
				for (Map.Entry<String, List<SIPSTPSWPReportColumnDTO>> dataSourceMapEntry : sipStpSwpReportDTO
						.getSipStpSwpDataMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/sipSTPSWPReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", sipStpSwpReportDTO.getDistributorName());
					parameters.put("distributorEmail", sipStpSwpReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", sipStpSwpReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(sipStpSwpReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(sipStpSwpReportDTO.getToDate()));
					parameters.put("fundHouse", sipStpSwpReportDTO.getFundHouse());
					parameters.put("familyName", sipStpSwpReportDTO.getFamilyName());
					parameters.put("emailAddress", sipStpSwpReportDTO.getClientEmail());
					parameters.put("mobileNo", sipStpSwpReportDTO.getClientMobile());
					parameters.put("clientName", sipStpSwpReportDTO.getNameClient());
					parameters.put("pan", sipStpSwpReportDTO.getClientPAN());
					parameters.put("SipStpSwpDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "SipStpSwpReport.xls"));
				SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
				xlsReportConfiguration.setOnePagePerSheet(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
				xlsReportConfiguration.setDetectCellType(true);
				xlsReportConfiguration.setWhitePageBackground(false);
				xlsReportConfiguration.setIgnoreGraphics(false);
				xlsExporter.setConfiguration(xlsReportConfiguration);

				xlsExporter.exportReport();

				returnStatus = "Success";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	private String exportSipStpSwpReportPDF(SIPSTPSWPReportDTO sipStpSwpReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"SipStpSwpReport.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			BufferedImage logoImage = null;
			if (sipStpSwpReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(sipStpSwpReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (sipStpSwpReportDTO.getSipStpSwpDataMap() != null
					&& sipStpSwpReportDTO.getSipStpSwpDataMap().size() > 0) {
				for (Map.Entry<String, List<SIPSTPSWPReportColumnDTO>> dataSourceMapEntry : sipStpSwpReportDTO
						.getSipStpSwpDataMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/sipSTPSWPReport.jrxml")
							.getURI().getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("logo", logoImage);
					parameters.put("distributorName", sipStpSwpReportDTO.getDistributorName());
					parameters.put("distributorEmail", sipStpSwpReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", sipStpSwpReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(sipStpSwpReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(sipStpSwpReportDTO.getToDate()));
					parameters.put("fundHouse", sipStpSwpReportDTO.getFundHouse());
					parameters.put("familyName", sipStpSwpReportDTO.getFamilyName());
					parameters.put("emailAddress", sipStpSwpReportDTO.getClientEmail());
					parameters.put("mobileNo", sipStpSwpReportDTO.getClientMobile());
					parameters.put("clientName", sipStpSwpReportDTO.getNameClient());
					parameters.put("pan", sipStpSwpReportDTO.getClientPAN());
					parameters.put("SipStpSwpDataSource", jrBeanCollectionDataSource);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
							new JREmptyDataSource());
					jasperPrintList.add(jasperPrint);
				}

				// First loop on all reports to get total page number
				int totalPageNumber = 0;
				for (JasperPrint jp : jasperPrintList) {
					totalPageNumber += jp.getPages().size();
				}

				// Second loop all reports to replace our markers with current and total number
				int currentPage = 1;
				for (JasperPrint jp : jasperPrintList) {
					List<JRPrintPage> pages = jp.getPages();
					// Loop all pages of report
					for (JRPrintPage jpp : pages) {
						List<JRPrintElement> elements = jpp.getElements();
						// Loop all elements on page
						for (JRPrintElement jpe : elements) {
							// Check if text element
							if (jpe instanceof JRPrintText) {
								JRPrintText jpt = (JRPrintText) jpe;
								// Check if current page marker
								if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText("Page " + currentPage + " of"); // Replace marker
									continue;
								}
								// Check if total page marker
								if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) {
									jpt.setText(" " + totalPageNumber); // Replace marker
								}
							}
						}
						currentPage++;
					}
				}

				JRPdfExporter exporter = new JRPdfExporter();

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "SipStpSwpReport.pdf"));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				configuration.setCreatingBatchModeBookmarks(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				returnStatus = "Sucess";

			} else {
				returnStatus = "No Data";
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}

		return returnStatus;
	}

	
	@SuppressWarnings("resource")
	@RequestMapping(value = "/downloadHandlerForReport/{reportName}/{reportFormat}", method = RequestMethod.GET)
	public ResponseEntity<?> downloadHandlerForReport(@PathVariable String reportName,
			@PathVariable String reportFormat, HttpServletResponse response) throws FinexaBussinessException {
		ResponseEntity<?> returner = null;
		try {
			File file = null;

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				if (reportName.contains(FinexaConstant.MF_REPORT_TYPE_SIP)
						|| reportName.contains(FinexaConstant.MF_REPORT_TYPE_STP)
						|| reportName.contains(FinexaConstant.MF_REPORT_TYPE_SWP)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "SipStpSwpReport.pdf");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_DIVIDEND)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "DividendReport.pdf");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_TRANSACTION)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "TransactionReport.pdf");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_AUM)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "AumReport.pdf");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_INACTIVE_CLIENTS)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "InactiveClientReport.pdf");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_PORTFOLIO_GAIN_LOSS)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "PortfolioGainLossReport.pdf");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_BROKERAGE)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "BrokerageReport.pdf");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_PORTFOLIO_GAIN_LOSS)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "PortfolioGainLossReport.pdf");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_PORTFOLIO_VALUATION)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "PortfolioValuationReport.pdf");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_TRANSACTION_NEW)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "TransactionReportNew.pdf");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_DIVIDEND_NEW)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "DividendReportNew.pdf");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_CAPITAL_GAINS)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "CapitalGainsReport.pdf");
				}

				HttpHeaders header = new HttpHeaders();
				header.setContentType(MediaType.parseMediaType("application/pdf"));

				int length = (int) file.length();
				byte[] bytes = new byte[length];
				try {
					BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
					reader.read(bytes, 0, length);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				header.setContentLength(bytes.length);
				returner = new ResponseEntity<byte[]>(bytes, header, HttpStatus.OK);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				if (reportName.contains(FinexaConstant.MF_REPORT_TYPE_SIP)
						|| reportName.contains(FinexaConstant.MF_REPORT_TYPE_STP)
						|| reportName.contains(FinexaConstant.MF_REPORT_TYPE_SWP)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "SipStpSwpReport.xls");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_DIVIDEND)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "DividendReport.xls");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_TRANSACTION)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "TransactionReport.xls");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_AUM)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "AumReport.xls");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_INACTIVE_CLIENTS)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "InactiveClientReport.xls");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_PORTFOLIO_GAIN_LOSS)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "PortfolioGainLossReport.xls");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_BROKERAGE)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "BrokerageReport.xls");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_PORTFOLIO_GAIN_LOSS)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "PortfolioGainLossReport.xls");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_PORTFOLIO_VALUATION)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "PortfolioValuationReport.xls");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_TRANSACTION_NEW)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "TransactionReportNew.xls");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_DIVIDEND_NEW)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "DividendReportNew.xls");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_CAPITAL_GAINS)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "CapitalGainsReport.xls");
				}

				HttpHeaders header = new HttpHeaders();
				header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

				int length = (int) file.length();
				byte[] bytes = new byte[length];
				try {
					BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
					reader.read(bytes, 0, length);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				header.setContentLength(bytes.length);
				returner = new ResponseEntity<byte[]>(bytes, header, HttpStatus.OK);
			}
		} catch (Exception exp) {
			if (reportName.contains(FinexaConstant.MF_REPORT_TYPE_SIP)
					|| reportName.contains(FinexaConstant.MF_REPORT_TYPE_STP)
					|| reportName.contains(FinexaConstant.MF_REPORT_TYPE_SWP)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "Sip Stp Swp Report Download Failed", exp);
			} else if (reportName.equals(FinexaConstant.MF_REPORT_DIVIDEND)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "Dividend Report Download Failed", exp);
			} else if (reportName.equals(FinexaConstant.MF_REPORT_TRANSACTION)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "Transaction Report Download Failed", exp);
			} else if (reportName.equals(FinexaConstant.MF_REPORT_AUM)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "AUM Report Download Failed", exp);
			} else if (reportName.equals(FinexaConstant.MF_REPORT_INACTIVE_CLIENTS)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "Inactive Client Report Download Failed",
						exp);
			} else if (reportName.equals(FinexaConstant.MF_REPORT_PORTFOLIO_GAIN_LOSS)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "Portfolio Gain Loss Report Download Failed",
						exp);
			} else if (reportName.equals(FinexaConstant.MF_REPORT_BROKERAGE)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "Brokerage MIS Report Download Failed", exp);
			} else if (reportName.equals(FinexaConstant.MF_REPORT_PORTFOLIO_GAIN_LOSS)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "Portfolio Gain Loss Report Download Failed",
						exp);
			} else if (reportName.equals(FinexaConstant.MF_REPORT_PORTFOLIO_VALUATION)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "Portfolio Valuation Report Download Failed",
						exp);
			} else if (reportName.equals(FinexaConstant.MF_REPORT_TRANSACTION_NEW)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "Transaction Report New Download Failed",
						exp);
			} else if (reportName.equals(FinexaConstant.MF_REPORT_DIVIDEND_NEW)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "Dividend Report New Download Failed", exp);
			} else if (reportName.equals(FinexaConstant.MF_REPORT_CAPITAL_GAINS)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "Capital Gains Report Download Failed", exp);
			}
		}

		return returner;
	}

}
