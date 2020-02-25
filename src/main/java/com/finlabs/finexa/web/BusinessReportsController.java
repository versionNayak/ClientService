package com.finlabs.finexa.web;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.finlabs.finexa.dto.AumReconciliationReportColumnDTO;
import com.finlabs.finexa.dto.AumReconciliationReportDTO;
import com.finlabs.finexa.dto.BusinessMISReportColumnDTO;
import com.finlabs.finexa.dto.BusinessMISReportDTO;
import com.finlabs.finexa.dto.BusinessTransactionReportDTO;
import com.finlabs.finexa.dto.InactiveClientReportDTO;
import com.finlabs.finexa.dto.TransactionMISDTO;
import com.finlabs.finexa.dto.TransactionMISInflowOutflowDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.service.InactiveClientReportService;
import com.finlabs.finexa.service.TransactionMISService;
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
public class BusinessReportsController {

	private static Logger log = LoggerFactory.getLogger(BusinessReportsController.class);

	private static final String CURRENT_PAGE_NUMBER = "${CURRENT_PAGE_NUMBER}";
	private static final String TOTAL_PAGE_NUMBER = "${TOTAL_PAGE_NUMBER}";

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private TransactionMISService transactionMISService;

	@Autowired
	private InactiveClientReportService inactiveClientReportService;

	@RequestMapping(value = "/generateAumReconciliationReport", method = RequestMethod.GET)
	public ResponseEntity<?> generateAumReconciliationReport(@RequestParam(value = "distributorId") int distributorId,
			@RequestParam(value = "asOnDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date asOnDate,
			HttpServletResponse response) throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String asOnDateInput = "";

			AumReconciliationReportDTO inputDTO = new AumReconciliationReportDTO();
			inputDTO.setDistributorId(distributorId);

			Calendar c1 = Calendar.getInstance();
			asOnDateInput = FinexaUtil.getProperDateInput(c1, asOnDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setAsOnDate(formatter.parse(asOnDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			AdvisorUser au = advisorUserRepository.findOne(distributorId);
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

			Map<String, List<AumReconciliationReportColumnDTO>> inputMap = new HashMap<String, List<AumReconciliationReportColumnDTO>>();
			List<AumReconciliationReportColumnDTO> inputList = new ArrayList<AumReconciliationReportColumnDTO>();
			AumReconciliationReportColumnDTO dto = new AumReconciliationReportColumnDTO();
			dto.setAmcInvestor("First Amc Investor");
			dto.setFinexaInvestor("First Finexa Investor");
			dto.setFundName("First Fund");
			dto.setFolioNo("First Folio No.");
			dto.setAmcAum("First Amc Aum");
			dto.setFinexaAum("First Finexa Aum");
			dto.setAumDifference("First Aum Difference");
			dto.setAmcUnits("First Amc Units");
			dto.setFinexaUnits("First Finexa Units");
			dto.setUnitDifference("First Unit Difference");
			dto.setTransDate("First Trans Date");
			dto.setFolioFreezeDate("First Folio Freeze Date");
			inputList.add(dto);

			dto = new AumReconciliationReportColumnDTO();
			dto.setAmcInvestor("Second Amc Investor");
			dto.setFinexaInvestor("Second Finexa Investor");
			dto.setFundName("Second Fund");
			dto.setFolioNo("Second Folio No.");
			dto.setAmcAum("Second Amc Aum");
			dto.setFinexaAum("Second Finexa Aum");
			dto.setAumDifference("Second Aum Difference");
			dto.setAmcUnits("Second Amc Units");
			dto.setFinexaUnits("Second Finexa Units");
			dto.setUnitDifference("Second Unit Difference");
			dto.setTransDate("Second Trans Date");
			dto.setFolioFreezeDate("Second Folio Freeze Date");
			inputList.add(dto);

			inputMap.put("Some Key", inputList);

			inputDTO.setInputMap(inputMap);

			returnStatus = generateAumReconciliationReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("Business Reports", "AUMR",
					"Failed to generate Aum Reconciliation Report.", e);
		}

	}

	private String generateAumReconciliationReport(AumReconciliationReportDTO aumReconciliationReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";
		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"AumReconciliationReport.html\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (aumReconciliationReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(aumReconciliationReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (aumReconciliationReportDTO.getInputMap() != null
					&& aumReconciliationReportDTO.getInputMap().size() > 0) {
				for (Map.Entry<String, List<AumReconciliationReportColumnDTO>> dataSourceMapEntry : aumReconciliationReportDTO
						.getInputMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:businessReportsJrxml/aumReconciliationReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();

					parameters.put("logo", logoImage);
					parameters.put("distributorName", aumReconciliationReportDTO.getDistributorName());
					parameters.put("distributorEmail", aumReconciliationReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", aumReconciliationReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(aumReconciliationReportDTO.getAsOnDate()));
					parameters.put("AumReconciliationDataSource", jrBeanCollectionDataSource);

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
				// SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/AumReconciliationReport.html"));
				exporterHTML.setExporterOutput(new SimpleHtmlExporterOutput(
						"/home/supratim/DummyMasterLatestWorkspace/FinexaWeb/src/main/webapp/MyBusiness/resources/AumReconciliationReport.html"));
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

	@RequestMapping(value = "/exportAumReconciliationReport", method = RequestMethod.GET)
	public ResponseEntity<?> exportAumReconciliationReport(@RequestParam(value = "distributorId") int distributorId,
			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "asOnDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date asOnDate,
			HttpServletResponse response) throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String asOnDateInput = "";

			AumReconciliationReportDTO inputDTO = new AumReconciliationReportDTO();
			inputDTO.setDistributorId(distributorId);

			Calendar c1 = Calendar.getInstance();
			asOnDateInput = FinexaUtil.getProperDateInput(c1, asOnDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setAsOnDate(formatter.parse(asOnDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			AdvisorUser au = advisorUserRepository.findOne(distributorId);
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

			Map<String, List<AumReconciliationReportColumnDTO>> inputMap = new HashMap<String, List<AumReconciliationReportColumnDTO>>();
			List<AumReconciliationReportColumnDTO> inputList = new ArrayList<AumReconciliationReportColumnDTO>();
			AumReconciliationReportColumnDTO dto = new AumReconciliationReportColumnDTO();
			dto.setAmcInvestor("First Amc Investor");
			dto.setFinexaInvestor("First Finexa Investor");
			dto.setFundName("First Fund");
			dto.setFolioNo("First Folio No.");
			dto.setAmcAum("First Amc Aum");
			dto.setFinexaAum("First Finexa Aum");
			dto.setAumDifference("First Aum Difference");
			dto.setAmcUnits("First Amc Units");
			dto.setFinexaUnits("First Finexa Units");
			dto.setUnitDifference("First Unit Difference");
			dto.setTransDate("First Trans Date");
			dto.setFolioFreezeDate("First Folio Freeze Date");
			inputList.add(dto);

			dto = new AumReconciliationReportColumnDTO();
			dto.setAmcInvestor("Second Amc Investor");
			dto.setFinexaInvestor("Second Finexa Investor");
			dto.setFundName("Second Fund");
			dto.setFolioNo("Second Folio No.");
			dto.setAmcAum("Second Amc Aum");
			dto.setFinexaAum("Second Finexa Aum");
			dto.setAumDifference("Second Aum Difference");
			dto.setAmcUnits("Second Amc Units");
			dto.setFinexaUnits("Second Finexa Units");
			dto.setUnitDifference("Second Unit Difference");
			dto.setTransDate("Second Trans Date");
			dto.setFolioFreezeDate("Second Folio Freeze Date");
			inputList.add(dto);

			inputMap.put("Some Key", inputList);

			inputDTO.setInputMap(inputMap);

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportAumReconciliationReportExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportAumReconciliationReportPDF(inputDTO, response);
			}

			returnStatus = generateAumReconciliationReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("Business Reports", "AUMR",
					"Failed to export Aum Reconciliation Report.", e);
		}
	}

	private String exportAumReconciliationReportPDF(AumReconciliationReportDTO aumReconciliationReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";
		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"AumReconciliationReport.pdf\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (aumReconciliationReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(aumReconciliationReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (aumReconciliationReportDTO.getInputMap() != null
					&& aumReconciliationReportDTO.getInputMap().size() > 0) {
				for (Map.Entry<String, List<AumReconciliationReportColumnDTO>> dataSourceMapEntry : aumReconciliationReportDTO
						.getInputMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:businessReportsJrxml/aumReconciliationReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();

					parameters.put("logo", logoImage);
					parameters.put("distributorName", aumReconciliationReportDTO.getDistributorName());
					parameters.put("distributorEmail", aumReconciliationReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", aumReconciliationReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(aumReconciliationReportDTO.getAsOnDate()));
					parameters.put("AumReconciliationDataSource", jrBeanCollectionDataSource);

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
						+ System.getProperty("file.separator") + "AumReconciliationReport.pdf"));
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

	private String exportAumReconciliationReportExcel(AumReconciliationReportDTO aumReconciliationReportDTO,
			HttpServletResponse response) throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";
		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"AumReconciliationReport.xls\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (aumReconciliationReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(aumReconciliationReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (aumReconciliationReportDTO.getInputMap() != null
					&& aumReconciliationReportDTO.getInputMap().size() > 0) {
				for (Map.Entry<String, List<AumReconciliationReportColumnDTO>> dataSourceMapEntry : aumReconciliationReportDTO
						.getInputMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:businessReportsJrxml/aumReconciliationReport.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();

					parameters.put("logo", logoImage);
					parameters.put("distributorName", aumReconciliationReportDTO.getDistributorName());
					parameters.put("distributorEmail", aumReconciliationReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", aumReconciliationReportDTO.getDistributorMobile());
					parameters.put("asOnDate", formatter.format(aumReconciliationReportDTO.getAsOnDate()));
					parameters.put("AumReconciliationDataSource", jrBeanCollectionDataSource);

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
						+ System.getProperty("file.separator") + "AumReconciliationReport.xls"));
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

	@RequestMapping(value = "/generateBusinessMISReport", method = RequestMethod.GET)
	public ResponseEntity<?> generateBusinessMISReport(@RequestParam(value = "distributorId") int distributorId,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "options") String optionType, HttpServletResponse response)
			throws IOException, JRException, FinexaBussinessException {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			BusinessMISReportDTO inputDTO = new BusinessMISReportDTO();
			inputDTO.setDistributorId(distributorId);

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

			AdvisorUser au = advisorUserRepository.findOne(distributorId);
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

			inputDTO.setOptions(optionType);

			returnStatus = generateBusinessMISReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("Business Reports", "AUMR",
					"Failed to generate Aum Reconciliation Report.", e);
		}

	}

	private String generateBusinessMISReport(BusinessMISReportDTO businessMISReportDTO, HttpServletResponse response)
			throws IOException, JRException {
		// TODO Auto-generated method stub
		String returnStatus = "";
		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"BusinessMISReport.html\""));

			List<JasperPrint> jasperPrintList = new ArrayList<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			BufferedImage logoImage = null;
			if (businessMISReportDTO.getLogo() != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(businessMISReportDTO.getLogo());
				logoImage = ImageIO.read(bis);
			} else {
				logoImage = ImageIO.read(
						new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
			}

			if (businessMISReportDTO.getInputMap() != null && businessMISReportDTO.getInputMap().size() > 0) {
				// if(businessMISReportDTO.getOptions())
				for (Map.Entry<String, List<BusinessMISReportColumnDTO>> dataSourceMapEntry : businessMISReportDTO
						.getInputMap().entrySet()) {
					System.out.println(
							dataSourceMapEntry.getKey() + " ------ " + dataSourceMapEntry.getValue().toString());
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
							dataSourceMapEntry.getValue());
					String path = resourceLoader
							.getResource("classpath:businessReportsJrxml/businessMISReportByFundHouse.jrxml").getURI()
							.getPath();
					JasperReport jasperReport = JasperCompileManager.compileReport(path);
					Map<String, Object> parameters = new HashMap<>();

					parameters.put("logo", logoImage);
					parameters.put("distributorName", businessMISReportDTO.getDistributorName());
					parameters.put("distributorEmail", businessMISReportDTO.getDistributorEmail());
					parameters.put("distributorMobile", businessMISReportDTO.getDistributorMobile());
					parameters.put("fromDate", formatter.format(businessMISReportDTO.getFromDate()));
					parameters.put("toDate", formatter.format(businessMISReportDTO.getToDate()));
					parameters.put("BusinessMISDataSource", jrBeanCollectionDataSource);

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
				// SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/AumReconciliationReport.html"));
				exporterHTML.setExporterOutput(new SimpleHtmlExporterOutput(
						"/home/supratim/DummyMasterLatestWorkspace/FinexaWeb/src/main/webapp/MyBusiness/resources/BusinessMISReport.html"));
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

	/*
	 * @RequestMapping(value = "/generateBusinessTransactionReport", method =
	 * RequestMethod.GET) public ResponseEntity<?>
	 * generateBusinessTransactionReport(
	 * 
	 * @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM") Date
	 * fromDate,
	 * 
	 * @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM") Date
	 * toDate,
	 * 
	 * @RequestParam(value = "arn") String arn,
	 * 
	 * @RequestParam(value = "advisorID") int advisorID, HttpServletResponse
	 * response) throws IOException, JRException, FinexaBussinessException { try {
	 * 
	 * String returnStatus = ""; String fromDateInput = ""; String toDateInput = "";
	 * 
	 * TransactionMISDTO inputDTO = new TransactionMISDTO();
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * AdvisorUser au = advisorUserRepository.findOne(advisorID); if (au != null) {
	 * 
	 * if (au.getLogo() != null) { inputDTO.setLogo(au.getLogo()); }
	 * 
	 * inputDTO.setDistributorName(au.getAdvisorMaster().getOrgName());
	 * 
	 * inputDTO.setDistributorAddress((au.getCity() == null ? " " : au.getCity()) +
	 * ", " + (au.getState() == null ? " " : au.getState()) + ", " +
	 * (au.getLookupCountry().getNicename() == null ? " " :
	 * au.getLookupCountry().getNicename())); inputDTO.setDistributorContactDetails(
	 * (au.getPhoneCountryCode() == null ? " " : au.getPhoneCountryCode()) + " " +
	 * (au.getPhoneNo() == null ? " " : au.getPhoneNo()));
	 * 
	 * inputDTO.setDistributorEmail(au.getEmailID());
	 * inputDTO.setDistributorMobile("" + au.getPhoneNo());
	 * 
	 * }
	 * 
	 * 
	 * inputDTO=transactionMISService.getInflowOutflowList(advisorID, fromDate,
	 * toDate, arn);
	 * 
	 * returnStatus = generateTransactionReportNew(inputDTO, response); return new
	 * ResponseEntity<String>(returnStatus, HttpStatus.OK); } catch
	 * (RuntimeException e) { // TODO Auto-generated catch block throw new
	 * FinexaBussinessException("MF-BackOffice", "MFBO-TR01",
	 * "Failed to export Transaction Report New.", e); } catch (ParseException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); } }
	 */
	@RequestMapping(value = "/exportBusinessTransactionReport", method = RequestMethod.GET)
	public ResponseEntity<?> exportBusinessTransactionReport(

			@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM") Date toDate,
			@RequestParam(value = "arn") String arn, @RequestParam(value = "advisorID") int advisorID,
			HttpServletResponse response) throws Exception {

		try {

			String returnStatus = "";
			String fromDateInput = "";
			String toDateInput = "";

			BusinessTransactionReportDTO inputDTO = new BusinessTransactionReportDTO();
			Calendar c1 = Calendar.getInstance();
			fromDateInput = FinexaUtil.getProperDateInput(c1, fromDate);

			Calendar c2 = Calendar.getInstance();
			toDateInput = FinexaUtil.getProperDateInput(c2, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromDateInput));
				inputDTO.setToDate(formatter.parse(toDateInput));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inputDTO.setMainDataSource(transactionMISService.getInflowOutflowList(advisorID, fromDate, toDate, arn));
			inputDTO.setAdvisorID(advisorID);
			
			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) { 
				returnStatus = exportBusinessTransactionReportExcel(inputDTO, response); 
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportBusinessTransactionReportPDF(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-TR01", "Failed to export Transaction Report New.",
					e);
		}

	}

	/*
	 * private String
	 * exportBusinessTransactionReportExcel(BusinessTransactionReportDTO
	 * transactionReportDTO, HttpServletResponse response) throws IOException,
	 * JRException { // TODO Auto-generated method stub String returnStatus = "";
	 * 
	 * try {
	 * 
	 * response.setContentType("application/x-download");
	 * response.setHeader("Content-Disposition",
	 * String.format("attachment; filename=\"TransactionReportNew.xls\""));
	 * 
	 * List<JasperPrint> jasperPrintList = new ArrayList<>(); SimpleDateFormat
	 * formatter = new SimpleDateFormat("dd/MM/yyyy");
	 * 
	 * BufferedImage logoImage = null; if (transactionReportDTO.getLogo() != null) {
	 * ByteArrayInputStream bis = new
	 * ByteArrayInputStream(transactionReportDTO.getLogo()); logoImage =
	 * ImageIO.read(bis); } else { logoImage = ImageIO.read(new
	 * File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().
	 * getPath())); }
	 * 
	 * Map<String, Object> parameters = new HashMap<>(); parameters.put("logo",
	 * logoImage); parameters.put("distributorName",
	 * transactionReportDTO.getDistributorName());
	 * parameters.put("distributorEmail",
	 * transactionReportDTO.getDistributorEmail());
	 * parameters.put("distributorMobile",
	 * transactionReportDTO.getDistributorMobile()); parameters.put("fromDate",
	 * formatter.format(transactionReportDTO.getFromDate()));
	 * parameters.put("toDate", formatter.format(transactionReportDTO.getToDate()));
	 * parameters.put("fundHouse", transactionReportDTO.getFundHouse());
	 * parameters.put("familyName", transactionReportDTO.getFamilyName());
	 * parameters.put("emailAddress", transactionReportDTO.getClientEmail());
	 * parameters.put("mobileNo", transactionReportDTO.getClientMobile());
	 * parameters.put("clientName", transactionReportDTO.getNameClient());
	 * parameters.put("pan", transactionReportDTO.getClientPAN());
	 * 
	 * if(transactionReportDTO.getDataSourceList() != null &&
	 * transactionReportDTO.getDataSourceList().size() > 0) {
	 * 
	 * List<TransactionReportDetailedDTOSecondOption> summaryDataSource = new
	 * ArrayList<TransactionReportDetailedDTOSecondOption>();
	 * for(TransactionReportDetailedDTOSecondOption obj :
	 * transactionReportDTO.getDataSourceList()) { summaryDataSource = new
	 * ArrayList<TransactionReportDetailedDTOSecondOption>();
	 * summaryDataSource.add(obj); JRBeanCollectionDataSource
	 * jrBeanCollectionDataSourceTransactionSummary = new
	 * JRBeanCollectionDataSource(summaryDataSource); String
	 * pathTransactionSummarySR = resourceLoader.getResource(
	 * "classpath:backOfficeReportsJrxml/transactionSummarySR.jrxml").getURI().
	 * getPath(); JasperReport jasperReportTransactionSummarySR =
	 * JasperCompileManager.compileReport(pathTransactionSummarySR);
	 * parameters.put("TransSummaryDataSource",
	 * jrBeanCollectionDataSourceTransactionSummary);
	 * parameters.put("jasperReportTransactionSummarySR",
	 * jasperReportTransactionSummarySR);
	 * 
	 * if(obj.getMainReportMap() != null && obj.getMainReportMap().size() > 0) {
	 * for(Map.Entry<String, List<TransactionReportColumnDTO>> mainReportMapEntry :
	 * obj.getMainReportMap().entrySet()) { JRBeanCollectionDataSource
	 * jrBeanCollectionDataSourceTransactionMain = new
	 * JRBeanCollectionDataSource(mainReportMapEntry.getValue()); String
	 * pathTransactionMainSR = resourceLoader.getResource(
	 * "classpath:backOfficeReportsJrxml/transactionMainTableSR.jrxml").getURI().
	 * getPath(); JasperReport jasperReportTransactionMainSR =
	 * JasperCompileManager.compileReport(pathTransactionMainSR);
	 * parameters.put("TransMainDataSource",
	 * jrBeanCollectionDataSourceTransactionMain);
	 * parameters.put("jasperReportTransactionMainSR",
	 * jasperReportTransactionMainSR); }
	 * 
	 * String path = resourceLoader.getResource(
	 * "classpath:backOfficeReportsJrxml/transactionReportNew.jrxml").getURI().
	 * getPath(); JasperReport jasperReport =
	 * JasperCompileManager.compileReport(path);
	 * 
	 * JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
	 * parameters, new JREmptyDataSource()); jasperPrintList.add(jasperPrint);
	 * 
	 * } }
	 * 
	 * // First loop on all reports to get total page number int totalPageNumber =
	 * 0; for (JasperPrint jp : jasperPrintList) { totalPageNumber +=
	 * jp.getPages().size(); }
	 * 
	 * // Second loop all reports to replace our markers with current and total
	 * number int currentPage = 1; for (JasperPrint jp : jasperPrintList) {
	 * List<JRPrintPage> pages = jp.getPages(); // Loop all pages of report for
	 * (JRPrintPage jpp : pages) { List<JRPrintElement> elements =
	 * jpp.getElements(); // Loop all elements on page for (JRPrintElement jpe :
	 * elements) { // Check if text element if (jpe instanceof JRPrintText) {
	 * JRPrintText jpt = (JRPrintText) jpe; // Check if current page marker if
	 * (CURRENT_PAGE_NUMBER.equals(jpt.getValue())) { jpt.setText("Page " +
	 * currentPage + " of"); // Replace marker continue; } // Check if total page
	 * marker if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())) { jpt.setText(" " +
	 * totalPageNumber); // Replace marker } } } currentPage++; } }
	 * 
	 * JRXlsExporter xlsExporter = new JRXlsExporter();
	 * 
	 * xlsExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList)
	 * ); xlsExporter.setExporterOutput(new
	 * SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir") +
	 * System.getProperty("file.separator") + "TransactionReportNew.xls"));
	 * SimpleXlsReportConfiguration xlsReportConfiguration = new
	 * SimpleXlsReportConfiguration();
	 * xlsReportConfiguration.setOnePagePerSheet(false);
	 * xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
	 * xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
	 * xlsReportConfiguration.setWrapText(true);
	 * xlsReportConfiguration.setFontSizeFixEnabled(false);
	 * xlsReportConfiguration.setDetectCellType(true);
	 * xlsReportConfiguration.setWhitePageBackground(false);
	 * xlsExporter.setConfiguration(xlsReportConfiguration);
	 * 
	 * xlsExporter.exportReport();
	 * 
	 * returnStatus = "Sucess";
	 * 
	 * } else { returnStatus = "No Data"; }
	 * 
	 * } catch (RuntimeException e) { // TODO Auto-generated catch block
	 * returnStatus = "Failure"; e.printStackTrace(); }
	 * 
	 * return returnStatus;
	 * 
	 * }
	 */
	
	private String exportBusinessTransactionReportPDF(BusinessTransactionReportDTO inputDTO,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	private String exportBusinessTransactionReportExcel(BusinessTransactionReportDTO inputDTO,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		TransactionMISDTO  transactionMISDTO = transactionMISService.getInflowOutflowList(inputDTO.getAdvisorID(), inputDTO.getFromDate(), inputDTO.getToDate(), inputDTO.getArn());
		
		Workbook newWorkbook = WorkbookFactory.create(new File("/ClientService/src/main/resources/businessReportsInExcelFormat/TransactionMISBusinessReport.xls"));
		Sheet sheet = newWorkbook.getSheetAt(0);
		Row row;
		int rowID = 0;		
		Cell cell; 
		row = sheet.createRow(rowID++);
		
		//Beginning of first row of "Monthly MIS Summary for Month - 2019"
		int cellID = 7;
		for(int i = 0; i < 5; i++) {						
			cell = row.createCell(cellID++);
			cellID++;
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 7,11));
		cell = row.getCell(7);
		cell.setCellValue(transactionMISDTO.getMonthlySummaryDetails());
		//End of first row of "Monthly MIS Summary for Month - 2019"
		
		//Beginning of content of "Monthly MIS Summary for Month - 2019"
		for(int i = 0; i < 3; i++) {
			row = sheet.createRow(rowID++);
			cellID = 7;	
			for(int j = 0; j < 3; j++) {
				cell = row.createCell(cellID++);
			}
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 7, 9));
			cell = row.getCell(7);
			if(i == 0)
				cell.setCellValue(transactionMISDTO.getTotalMonthlyInflowRow());
			else if(i == 1)
				cell.setCellValue(transactionMISDTO.getTotalMonthlyOutflowRow());
			else if(i == 1)
				cell.setCellValue(transactionMISDTO.getNetMonthlyInflowRow());
			
			for(int k = 0; k < 2; k++) {
				cell = row.createCell(cellID++);
			}
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 10, 11));
			cell = row.getCell(10);
			if(i == 0)
				cell.setCellValue(transactionMISDTO.getTotalMonthlyInflow());
			else if(i == 1)
				cell.setCellValue(transactionMISDTO.getTotalMonthlyOutflow());
			else if(i == 1)
				cell.setCellValue(transactionMISDTO.getNetMonthlyInflow());
		}
		//End of content of "Monthly MIS Summary for Month - 2019"
		rowID = 4;
		if(transactionMISDTO.getInflowList().size() > 1) {
     	  
     	   row = sheet.createRow(rowID++);
     	   row = sheet.createRow(rowID++);
     	   cell = row.createCell(7);
     	   cell.setCellValue("Monthly Inflow (Subscriptions)"); 
     	   //cell.setCellStyle(style);
     	   cell = row.createCell(8);
     	   cell = row.createCell(9);
     	   cell = row.createCell(10);
     	   sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 7,10));
     	   
     	   //List<Column> monthlyTransactionList = new ArrayList<Column>();
     	   //monthlyTransactionList = entry.getValue();
     	   for (TransactionMISInflowOutflowDTO transactionMISInflowOutflowDTO : transactionMISDTO.getInflowList()) {
     		  row = sheet.createRow(rowID++);
     		   for(int counter = 3; counter <= 12; counter++) {
     			   switch(counter) {
     			   case 3: 
     				   cell = row.createCell(counter);
     				   cell.setCellValue(transactionMISInflowOutflowDTO.getSRNo());
     				   break;
     			   case 4: 
     				   cell = row.createCell(counter);
     				   cell.setCellValue(transactionMISInflowOutflowDTO.getInvestorName());
     				   break;
     			   case 5: 
     				   cell = row.createCell(counter);
     				   cell.setCellValue(transactionMISInflowOutflowDTO.getAmc());
     				   break;
     			   case 6: 
     				   cell = row.createCell(counter);
     				   cell.setCellValue(transactionMISInflowOutflowDTO.getSchemeName());
     				   break;
     			   case 7: 
     				   cell = row.createCell(counter);
     				   cell.setCellValue(transactionMISInflowOutflowDTO.getFolioNo());
     				   break;
     			   case 8: 
     				   cell = row.createCell(counter);
     				   cell.setCellValue(transactionMISInflowOutflowDTO.getTransType());
     				   break;
     			   case 9: 
     				   cell = row.createCell(counter);
     				   cell.setCellValue(transactionMISInflowOutflowDTO.getTransDate());
     				   break;
     			   case 10: 
     				   cell = row.createCell(counter);
     				   cell.setCellValue(transactionMISInflowOutflowDTO.getTransAmt());
     				   break;
     			   case 11: 
     				   cell = row.createCell(counter);
     				   cell.setCellValue(transactionMISInflowOutflowDTO.getNav());
     				   break;
     			   case 12: 
     				   cell = row.createCell(counter);
     				   cell.setCellValue(transactionMISInflowOutflowDTO.getUnits());
     				   break;
     				   
     			   }
     		   }
     	   }       
		}
		
		if(transactionMISDTO.getOutflowList().size() > 1) {
	     	  
	     	   row = sheet.createRow(rowID++);
	     	   row = sheet.createRow(rowID++);
	     	   cell = row.createCell(7);
	     	   cell.setCellValue("Monthly Inflow (Subscriptions)"); 
	     	   //cell.setCellStyle(style);
	     	   cell = row.createCell(8);
	     	   cell = row.createCell(9);
	     	   cell = row.createCell(10);
	     	   sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 7,10));
	     	   
	     	   //List<Column> monthlyTransactionList = new ArrayList<Column>();
	     	   //monthlyTransactionList = entry.getValue();
	     	   for (TransactionMISInflowOutflowDTO transactionMISInflowOutflowDTO : transactionMISDTO.getOutflowList()) {
	     		  row = sheet.createRow(rowID++);
	     		   for(int counter = 3; counter <= 12; counter++) {
	     			   switch(counter) {
	     			   case 3: 
	     				   cell = row.createCell(counter);
	     				   cell.setCellValue(transactionMISInflowOutflowDTO.getSRNo());
	     				   break;
	     			   case 4: 
	     				   cell = row.createCell(counter);
	     				   cell.setCellValue(transactionMISInflowOutflowDTO.getInvestorName());
	     				   break;
	     			   case 5: 
	     				   cell = row.createCell(counter);
	     				   cell.setCellValue(transactionMISInflowOutflowDTO.getAmc());
	     				   break;
	     			   case 6: 
	     				   cell = row.createCell(counter);
	     				   cell.setCellValue(transactionMISInflowOutflowDTO.getSchemeName());
	     				   break;
	     			   case 7: 
	     				   cell = row.createCell(counter);
	     				   cell.setCellValue(transactionMISInflowOutflowDTO.getFolioNo());
	     				   break;
	     			   case 8: 
	     				   cell = row.createCell(counter);
	     				   cell.setCellValue(transactionMISInflowOutflowDTO.getTransType());
	     				   break;
	     			   case 9: 
	     				   cell = row.createCell(counter);
	     				   cell.setCellValue(transactionMISInflowOutflowDTO.getTransDate());
	     				   break;
	     			   case 10: 
	     				   cell = row.createCell(counter);
	     				   cell.setCellValue(transactionMISInflowOutflowDTO.getTransAmt());
	     				   break;
	     			   case 11: 
	     				   cell = row.createCell(counter);
	     				   cell.setCellValue(transactionMISInflowOutflowDTO.getNav());
	     				   break;
	     			   case 12: 
	     				   cell = row.createCell(counter);
	     				   cell.setCellValue(transactionMISInflowOutflowDTO.getUnits());
	     				   break;
	     				   
	     			   }
	     		   }
	     	   }
	     	   
	        
			}
		
		FileOutputStream out = new FileOutputStream(new File("/home/smita/Desktop/TransactionMIS/Report.xlsx"));
	    newWorkbook.write(out);
	    out.close();
	    System.out.println("Report.xlsx written successfully");
	   
		
		return null;
	}


	@RequestMapping(value = "/generateInactiveClientReport", method = RequestMethod.GET)
	public ResponseEntity<?> generateInactiveClientReport(
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "reportType") String reportType, @RequestParam(value = "advisorID") int advisorID,
			HttpServletResponse response) throws IOException, JRException, FinexaBussinessException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException {

		try {

			String returnStatus = "";
			String fromdate = "";
			String todate = "";

			InactiveClientReportDTO inputDTO = new InactiveClientReportDTO();
			inputDTO.setAdvisorId(advisorID);
			Calendar c1 = Calendar.getInstance();
			fromdate = FinexaUtil.getProperDateInput(c1, fromDate);
			todate = FinexaUtil.getProperDateInput(c1, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromdate));
				inputDTO.setToDate(formatter.parse(todate));
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

			inputDTO.setReportType(reportType);

			inputDTO.setClientNameTransactionList(inactiveClientReportService.inactiveClientReport(inputDTO));

			returnStatus = generateInactiveClientReport(inputDTO, response);
			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-ICHTML",
					"Failed to Generate Inactive Client Report.", e);
		}

	}

	private String generateInactiveClientReport(InactiveClientReportDTO inactiveClientReportDTO,
			HttpServletResponse response) throws IOException, JRException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, ParseException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"InactiveClientReport.html\""));

			if (inactiveClientReportDTO.getClientNameTransactionList() != null
					&& inactiveClientReportDTO.getClientNameTransactionList().size() > 0) {
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
						inactiveClientReportDTO.getClientNameTransactionList());
				String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/inactiveClientReport.jrxml")
						.getURI().getPath();
				JasperReport jasperReport = JasperCompileManager.compileReport(path);
				Map<String, Object> parameters = new HashMap<>();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

				BufferedImage logoImage = null;
				if (inactiveClientReportDTO.getLogo() != null) {
					ByteArrayInputStream bis = new ByteArrayInputStream(inactiveClientReportDTO.getLogo());
					logoImage = ImageIO.read(bis);
				} else {
					logoImage = ImageIO.read(new File(
							resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
				}

				parameters.put("logo", logoImage);
				parameters.put("distributorName", inactiveClientReportDTO.getDistributorName());
				parameters.put("distributorEmail", inactiveClientReportDTO.getDistributorEmail());
				parameters.put("distributorMobile", inactiveClientReportDTO.getDistributorMobile());
				parameters.put("fromDate", formatter.format(inactiveClientReportDTO.getFromDate()));
				parameters.put("toDate", formatter.format(inactiveClientReportDTO.getToDate()));
				parameters.put("InactiveClientDataSource", dataSource);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						new JREmptyDataSource());

				List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
				jasperPrintList.add(jasperPrint);

				HtmlExporter exporterHTML = new HtmlExporter();
				exporterHTML.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporterHTML.setExporterOutput(
						new SimpleHtmlExporterOutput("/var/www/html/MyBusiness/resources/InactiveClientReport.html"));
				// exporterHTML.setExporterOutput(new
				// SimpleHtmlExporterOutput("/home/supratim/MFBackOfficeJava11Workspace/FinexaWebMerged/src/main/webapp/MyBusiness/resources/InactiveClientReport.html"));
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

	@RequestMapping(value = "/inactiveClientExport", method = RequestMethod.GET)
	public ResponseEntity<?> createInactiveClientReport(@RequestParam(value = "reportFormat") String reportFormat,
			@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(value = "fundHouse") String fundHouse, @RequestParam(value = "schemeName") String schemeName,
			@RequestParam(value = "reportType") String reportType, @RequestParam(value = "advisorID") int advisorID,
			HttpServletResponse response) throws IOException, JRException, FinexaBussinessException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException {

		try {

			String returnStatus = "";
			String fromdate = "";
			String todate = "";

			InactiveClientReportDTO inputDTO = new InactiveClientReportDTO();
			inputDTO.setAdvisorId(advisorID);
			Calendar c1 = Calendar.getInstance();
			fromdate = FinexaUtil.getProperDateInput(c1, fromDate);
			todate = FinexaUtil.getProperDateInput(c1, toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				inputDTO.setFromDate(formatter.parse(fromdate));
				inputDTO.setToDate(formatter.parse(todate));
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

			inputDTO.setReportType(reportType);

			inputDTO.setClientNameTransactionList(inactiveClientReportService.inactiveClientReport(inputDTO));

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				returnStatus = exportInactiveClientReportExcel(inputDTO, response);
			} else if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				returnStatus = exportInactiveClientReportPDF(inputDTO, response);
			}

			return new ResponseEntity<String>(returnStatus, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-IC01", "Failed to export Inactive Client Report.",
					e);
		}

	}

	private String exportInactiveClientReportExcel(InactiveClientReportDTO inactiveClientReportDTO,
			HttpServletResponse response) throws IOException, JRException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, ParseException {
		// TODO Auto-generated method stub
		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"InactiveClientReport.xls\""));
			if (inactiveClientReportDTO.getClientNameTransactionList() != null
					&& inactiveClientReportDTO.getClientNameTransactionList().size() > 0) {
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
						inactiveClientReportDTO.getClientNameTransactionList());
				String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/inactiveClientReport.jrxml")
						.getURI().getPath();
				JasperReport jasperReport = JasperCompileManager.compileReport(path);
				Map<String, Object> parameters = new HashMap<>();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

				BufferedImage logoImage = null;
				if (inactiveClientReportDTO.getLogo() != null) {
					ByteArrayInputStream bis = new ByteArrayInputStream(inactiveClientReportDTO.getLogo());
					logoImage = ImageIO.read(bis);
				} else {
					logoImage = ImageIO.read(new File(
							resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
				}

				parameters.put("logo", logoImage);
				parameters.put("distributorName", inactiveClientReportDTO.getDistributorName());
				parameters.put("distributorEmail", inactiveClientReportDTO.getDistributorEmail());
				parameters.put("distributorMobile", inactiveClientReportDTO.getDistributorMobile());
				parameters.put("fromDate", formatter.format(inactiveClientReportDTO.getFromDate()));
				parameters.put("toDate", formatter.format(inactiveClientReportDTO.getToDate()));
				parameters.put("InactiveClientDataSource", dataSource);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						new JREmptyDataSource());

				List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
				jasperPrintList.add(jasperPrint);

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "InactiveClientReport.xls"));
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

	private String exportInactiveClientReportPDF(InactiveClientReportDTO inactiveClientReportDTO,
			HttpServletResponse response) throws IOException, JRException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, ParseException {
		// TODO Auto-generated method stub

		String returnStatus = "";

		try {

			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"InactiveClientReport.pdf\""));

			// System.out.println(inactiveClientReportService.inactiveClientReport(inactiveClientReportDTO).size());
			if (inactiveClientReportDTO.getClientNameTransactionList() != null
					&& inactiveClientReportDTO.getClientNameTransactionList().size() > 0) {
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
						inactiveClientReportDTO.getClientNameTransactionList());
				String path = resourceLoader.getResource("classpath:backOfficeReportsJrxml/inactiveClientReport.jrxml")
						.getURI().getPath();
				JasperReport jasperReport = JasperCompileManager.compileReport(path);
				Map<String, Object> parameters = new HashMap<>();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

				BufferedImage logoImage = null;
				if (inactiveClientReportDTO.getLogo() != null) {
					ByteArrayInputStream bis = new ByteArrayInputStream(inactiveClientReportDTO.getLogo());
					logoImage = ImageIO.read(bis);
				} else {
					logoImage = ImageIO.read(new File(
							resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
				}

				parameters.put("logo", logoImage);
				parameters.put("distributorName", inactiveClientReportDTO.getDistributorName());
				parameters.put("distributorEmail", inactiveClientReportDTO.getDistributorEmail());
				parameters.put("distributorMobile", inactiveClientReportDTO.getDistributorMobile());
				parameters.put("fromDate", formatter.format(inactiveClientReportDTO.getFromDate()));
				parameters.put("toDate", formatter.format(inactiveClientReportDTO.getToDate()));
				parameters.put("InactiveClientDataSource", dataSource);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						new JREmptyDataSource());
				JRPdfExporter exporter = new JRPdfExporter();

				List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
				jasperPrintList.add(jasperPrint);

				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir")
						+ System.getProperty("file.separator") + "InactiveClientReport.pdf"));
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
	@RequestMapping(value = "/downloadHandlerForBusinessReports/{reportName}/{reportFormat}", method = RequestMethod.GET)
	public ResponseEntity<?> newDownloadHandlerForReport(@PathVariable String reportName,
			@PathVariable String reportFormat, HttpServletResponse response) throws FinexaBussinessException {
		ResponseEntity<?> returner = null;
		try {
			File file = null;

			if (reportFormat.equals(FinexaConstant.FILE_TYPE_PDF)) {
				if (reportName.contains(FinexaConstant.BUSINESS_REPORT_AUM_RECONCILIATION)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "AumReconciliationReport.pdf");
				} else if (reportName.contains(FinexaConstant.BUSINESS_REPORT_TRANSACTION_SUMMARY)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "BusinessTransactionReport.pdf");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_INACTIVE_CLIENTS)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "InactiveClientReport.pdf");
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
				if (reportName.equals(FinexaConstant.BUSINESS_REPORT_AUM_RECONCILIATION)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "AumReconciliationReport.xls");
				} else if (reportName.equals(FinexaConstant.MF_REPORT_INACTIVE_CLIENTS)) {
					file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
							+ "InactiveClientReport.xls");
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
			if (reportFormat.equals(FinexaConstant.FILE_TYPE_EXCEL)) {
				throw new FinexaBussinessException("MF-BackOffice", "500", "Aum Reconciliation Report Download Failed",
						exp);
			}
		}

		return returner;
	}

}
