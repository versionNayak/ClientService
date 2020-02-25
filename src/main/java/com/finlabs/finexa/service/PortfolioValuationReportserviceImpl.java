package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.PortfolioValuationReportColumnDTO;
import com.finlabs.finexa.dto.PortfolioValuationReportDTO;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.util.FinexaUtil;
import com.finlabs.finexa.util.XirrTest;

@Service("PortfolioValuationReportservice")
@Transactional
public class PortfolioValuationReportserviceImpl implements PortfolioValuationReportservice {

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;
	@Autowired
	private MasterMFDailyNAVRepository masterMFDailyNAVRepository;

	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepo;

	@Autowired
	private MasterMutualFundETFRepository etfRepository;

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd/MM/yyyy");
	private static final String NULL = "null";

	@Override
	public Map<String, List<PortfolioValuationReportColumnDTO>> portfolioValuationReport(
			PortfolioValuationReportDTO portfolioValuationReportDTO) throws RuntimeException, ParseException {

		Map<String, String> alreadyCountInReportMap = new HashMap<String, String>();
		Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));
		List<String> clientMasterList = new ArrayList<String>();
		Map<String, List<PortfolioValuationReportColumnDTO>> resultMap = new HashMap<String, List<PortfolioValuationReportColumnDTO>>();
		Map<String, String> mainNamePanMapforClient = new HashMap<String, String>();
		Map<String, String> mainNamePanMapforFamily = new HashMap<String, String>();
		List<String> auxiliaryClientList = new ArrayList<>();
		List<String> auxiliaryFamilyList = new ArrayList<>();

		Date asOnDate = portfolioValuationReportDTO.getAsOnDate();
		int clientId;
		clientId = portfolioValuationReportDTO.getClientId();
		ClientMaster cm = clientMasterRepository.findOne(clientId);

		if (cm != null) {
			String name = cm.getFirstName() + (cm.getMiddleName() == null ? "" : (" " + cm.getMiddleName()))
					+ (cm.getLastName() == null ? "" : " " + (cm.getLastName()));
			String pan = cm.getPan();
			if (Arrays.asList(portfolioValuationReportDTO.getClientId()).contains(clientId)) {
				clientMasterList.add(name + "-" + pan);
				mainNamePanMapforClient.put(pan + name, name);
				List<AuxillaryInvestorMaster> auxList = cm.getAuxillaryInvestorMasters();
				for (AuxillaryInvestorMaster obj : auxList) {
					String auxName = obj.getFirstName()
							+ (obj.getMiddleName() == null ? "" : (" " + obj.getMiddleName()))
							+ (obj.getLastName() == null ? "" : (" " + obj.getLastName()));
					String pan1 = obj.getPan();
					if (!clientMasterList.contains(auxName + "-" + pan1)) {
						clientMasterList.add(auxName + "-" + pan1);
						mainNamePanMapforClient.put(pan1 + auxName, name);
						auxiliaryClientList.add(auxName);
					}
				}
			}
			List<ClientFamilyMember> familyMemberList = cm.getClientFamilyMembers();
			for (ClientFamilyMember obj : familyMemberList) {
				if (Arrays.asList(portfolioValuationReportDTO.getFamilyMemberIdArr()).contains(obj.getId())) {
					String auxName = obj.getFirstName()
							+ (obj.getMiddleName() == null ? "" : (" " + obj.getMiddleName()))
							+ (obj.getLastName() == null ? "" : (" " + obj.getLastName()));
					String pan1 = obj.getPan();
					if (pan1 == null || pan1.equals("")) {
						if (!clientMasterList.contains(auxName + "-NA")) {
							clientMasterList.add(auxName + "-NA");
						}
					} else {
						if (!clientMasterList.contains(auxName + "-" + pan1)) {
							clientMasterList.add(auxName + "-" + pan1);
							mainNamePanMapforFamily.put(pan1 + auxName, auxName);
						}
					}

					List<AuxillaryFamilyMember> auxFamList = obj.getAuxillaryFamilyMembers();
					for (AuxillaryFamilyMember aux : auxFamList) {
						String auxNameFam = aux.getFirstName()
								+ (aux.getMiddleName() == null ? "" : (" " + aux.getMiddleName()))
								+ (aux.getLastName() == null ? "" : (" " + aux.getLastName()));
						String panFam = obj.getPan();
						if (panFam == null || panFam.equals("")) {
							if (!clientMasterList.contains(auxNameFam + "-NA")) {
								clientMasterList.add(auxNameFam + "-NA");
							}
						} else {
							if (!clientMasterList.contains(auxNameFam + "-" + panFam)) {
								clientMasterList.add(auxNameFam + "-" + panFam);
								mainNamePanMapforFamily.put(panFam + auxNameFam, auxName);
								auxiliaryFamilyList.add(auxNameFam);
							}
						}
					}
				}
			}
		}
		boolean isDividendFound = false;

		try {
			List<PortfolioValuationReportColumnDTO> portfolioValuationReportList = new ArrayList<PortfolioValuationReportColumnDTO>();
			Double totalForinvestmentSwitchIn = 0.0d;
			Double totalForredemptionsSwitchOut = 0.0d;
			Double totalForDividends = 0.0d;
			Double totalCostOfInvestment = 0.0d;
			Double totalForMarketValue = 0.0d;
			Double totalForXirr = 0.0d;
			Double totalCostXirr = 0.0d;
			int count = 0;
			int countForFamily = 0;
			int flag = 2;
			int flagForFamily = 2;
			 
			for (String clientMasterNamePan : clientMasterList) {
				try {

					String invName = clientMasterNamePan.substring(0, clientMasterNamePan.indexOf('-'));
					invName = invName.trim();
					String invPAN = clientMasterNamePan.substring(clientMasterNamePan.indexOf('-') + 1);
					invPAN = invPAN.trim();

					if (!auxiliaryClientList.contains(invName) && !auxiliaryFamilyList.contains(invName)) {

						totalForinvestmentSwitchIn = 0.0d;
						totalForredemptionsSwitchOut = 0.0d;
						totalForDividends = 0.0d;
						totalCostOfInvestment = 0.0d;
						totalForMarketValue = 0.0d;
						totalForXirr = 0.0d;
						totalCostXirr = 0.0d;

					}

					if (!mainNamePanMapforFamily.containsKey(invPAN + invName)) {
						count++;
					} else if (!mainNamePanMapforClient.containsKey(invPAN + invName)) {
						countForFamily++;
					}
					if (!alreadyCountInReportMap.containsKey(invName.toLowerCase() + invPAN)) {

						List<Object[]> distinctFolioSchemeRTASet = transactionMasterBORepository
								.getDistinctFolioSchemeRTASetByInvestorNameAndAsOnDate(invName, invPAN, asOnDate);
						double xirr = 0.0d;
						System.out.println("Inv Name PAn"+invName + invPAN);
						System.out.println("Distinct Scheme RTA "+distinctFolioSchemeRTASet.size());
						for (Object[] transactionMasterBO : distinctFolioSchemeRTASet) {

							try {
								List<Date> dateForCalculation = new ArrayList<>();
								List<Double> paymentsForCalculation = new ArrayList<Double>();

								int xirrCount = 0;
								String folioNo = (String) transactionMasterBO[0];
								String rtaCode = (String) transactionMasterBO[1];
								int toBeConsideredFlag = 0;
								Double investmentSwitchIn = 0.0d;
								Double redemptionsSwitchOut = 0.0d;
								Double dividends = 0.0d;
								Double presentNAV = 0.0d;
								Double marketValue = 0.0d;
								Double totalUnits = 0.0d;
								Double diffCost = 0.00;
								List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo
										.findByCamsCodeAndStatus(rtaCode, "NEW");

								PortfolioValuationReportColumnDTO PortfolioValuationReport = new PortfolioValuationReportColumnDTO();
								if (isinMasterBO.size() > 0) {
									String isin = isinMasterBO.get(0).getIsin();
									System.out.println("SchemeFolio"+rtaCode);
									if (portfolioValuationReportDTO.getSchemeName() != null
											&& portfolioValuationReportDTO.getSchemeName() != ""
											&& !portfolioValuationReportDTO.getSchemeName().equals(NULL)) {
										if (isin.equals(portfolioValuationReportDTO.getSchemeName())) {
											toBeConsideredFlag = 1;
										}
									} else {
										toBeConsideredFlag = 1;
									}

									if (toBeConsideredFlag == 1) {

										Date firstInvestmentDate = transactionMasterBORepository
												.findMinDateByInvestorNameAndFolioAndSchemeRTA(invName, invPAN, folioNo,
														rtaCode);

										MasterMutualFundETF mutualFundETF = etfRepository.findOne(isin);

										

										List<TransactionMasterBO> transactionsBasedOnFolioAndSchemeList = transactionMasterBORepository
												.findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeRTA(
														invName, folioNo, rtaCode, firstInvestmentDate, asOnDate);

										for (TransactionMasterBO tmbo : transactionsBasedOnFolioAndSchemeList) {

											try {
												xirrCount++;
												if (tmbo.getLookupTransactionRule() != null) {

													switch (tmbo.getLookupTransactionRule().getCode()) {
													case "P":
														investmentSwitchIn += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "R":
														redemptionsSwitchOut += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForredemptionsSwitchOut += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "RED":
														redemptionsSwitchOut += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForredemptionsSwitchOut += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "Redemption":
														redemptionsSwitchOut += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForredemptionsSwitchOut += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "REDR":
														redemptionsSwitchOut -= ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForredemptionsSwitchOut -= ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "SI":
														investmentSwitchIn += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn += (Double
																.parseDouble(tmbo.getTransAmt()));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;

													case "SWIN":

														investmentSwitchIn += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "SWINR":

														investmentSwitchIn -= ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn -= ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "STP In":
														investmentSwitchIn += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "STPI":
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														investmentSwitchIn += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														paymentsForCalculation
																.add(Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "STPIR":
														investmentSwitchIn -= ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn -= ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;

													case "STP O":
														redemptionsSwitchOut += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForredemptionsSwitchOut += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "STP Out":
														redemptionsSwitchOut += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForredemptionsSwitchOut += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
													case "STPOR":
														redemptionsSwitchOut -= ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForredemptionsSwitchOut -= ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "SO":
														redemptionsSwitchOut += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForredemptionsSwitchOut += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "SWOF":
														redemptionsSwitchOut += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForredemptionsSwitchOut += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "SWOFR":
														redemptionsSwitchOut -= ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForredemptionsSwitchOut -= ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "TI":
														investmentSwitchIn += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "TO":
														redemptionsSwitchOut += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForredemptionsSwitchOut += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "DR":
														dividends += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForDividends += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														/*
														 * paymentsForCalculation
														 * .add(Double.parseDouble(tmbo.getTransAmt()));
														 */
														break;
													case "DIR":
														dividends += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForDividends += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														/*
														 * paymentsForCalculation
														 * .add(Double.parseDouble(tmbo.getTransAmt()));
														 */
														break;
													case "Dividend Reinvestment":
														dividends += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForDividends += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														/*
														 * paymentsForCalculation
														 * .add(Double.parseDouble(tmbo.getTransAmt()));
														 */
														break;
													case "SIP":
														investmentSwitchIn += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());

														break;
													case "SIPR":
														investmentSwitchIn -= ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn -= ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());

														break;
													case "ADDPUR":

														investmentSwitchIn += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());

														break;
													case "Additional Purchase":
														investmentSwitchIn += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());

														break;
													case "New Purchase":
														investmentSwitchIn += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "NEWPUR":
														investmentSwitchIn += ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn += ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());

														break;
													case "ADDPURR":
														investmentSwitchIn -= ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn -= ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());

														break;
													case "NEWPURR":
														investmentSwitchIn -= ((Double.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalForinvestmentSwitchIn -= ((Double
																.parseDouble(tmbo.getNav())
																* (Double.parseDouble(tmbo.getTransUnits()))));
														totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
														paymentsForCalculation
																.add(-Double.parseDouble(tmbo.getTransAmt()));
														dateForCalculation.add(tmbo.getTransactionDate());
														break;
													case "DP":
														dividends += (Double.parseDouble(tmbo.getTransAmt()));
														totalForDividends += (Double.parseDouble(tmbo.getTransAmt()));

														/*
														 * paymentsForCalculation
														 * .add(-Double.parseDouble(tmbo.getTransAmt()));
														 */

														break;
													case "J":
														// no effect
														break;
													default:
														break;
													}
												}

												Date currentNAv = masterMFDailyNAVRepository.findMaxDate(isin,
														asOnDate);
												MasterMFDailyNAV currentNAV = masterMFDailyNAVRepository.findNAV(isin,
														currentNAv);

												presentNAV = (double) currentNAV.getNav();
												marketValue = (totalUnits * presentNAV);

											} catch (Exception e) {
												e.printStackTrace();
											}
										}
										System.out.println("Units" +FinexaUtil.roundOff(totalUnits, 2));
										if (Double.valueOf(FinexaUtil.roundOff(totalUnits, 2)) <= 0.00) {

											totalForinvestmentSwitchIn -= investmentSwitchIn;
											totalForredemptionsSwitchOut -= redemptionsSwitchOut;
											totalForDividends -= dividends;
											continue;
										}

										diffCost = investmentSwitchIn;

										paymentsForCalculation.add(-marketValue);
										dateForCalculation.add(asOnDate);
										double[] payments = new double[paymentsForCalculation.size()];
										Date[] date = new Date[dateForCalculation.size()];
										int i = 0;
										for (double payment : paymentsForCalculation) {
											int dummy = (int) (Math.round(payment));
											payments[i] = dummy;
											i++;
										}
										int j = 0;
										SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
										for (Date dates : dateForCalculation) {
											String dateString = sdf.format(dates);
											Date dt = sdf.parse(dateString);
											date[j] = dt;
											j++;
										}
										System.out.println(payments.toString());
										System.out.println(date.toString());
										XirrTest xirrCalculation = new XirrTest();

										xirr = xirrCalculation.findXirr(payments, date);

										/*******************************************
										 * New Requirement
										 ***********************/
										totalCostOfInvestment += diffCost;
										totalForMarketValue += marketValue;
										totalCostXirr += (diffCost * xirr);

										/********************
										 * New Requirement
										 **********************************************/

										if (mainNamePanMapforClient.containsKey(invPAN + invName)) {
											PortfolioValuationReport.setClientDetails(
													mainNamePanMapforClient.get(invPAN + invName) + "-" + invPAN);

										} else if (mainNamePanMapforFamily.containsKey(invPAN + invName)) {
											PortfolioValuationReport.setClientDetails(
													mainNamePanMapforFamily.get(invPAN + invName) + "-" + invPAN);

										}
										PortfolioValuationReport.setSchemeName(mutualFundETF.getSchemeName());
										System.out.println("Scheme Name"+ mutualFundETF.getSchemeName());
										PortfolioValuationReport.setFolioNo(folioNo);
										PortfolioValuationReport
												.setInvestmentSince(formatterDisplay.format(firstInvestmentDate));
										PortfolioValuationReport
												.setInvestmentSwitchIn(numberFormatter
														.format(Math.round(investmentSwitchIn)).replace("\u20B9", "")
														.substring(0, numberFormatter
																.format(new BigDecimal(Math.round(investmentSwitchIn)))
																.replace("\u20B9", "").length() - 3));
										System.out.println("InvestmentSwitchin "+investmentSwitchIn);
										PortfolioValuationReport.setRedemptionsSwitchOut(numberFormatter
												.format(Math.round(redemptionsSwitchOut)).replace("\u20B9", "")
												.substring(0, numberFormatter
														.format(new BigDecimal(Math.round(redemptionsSwitchOut)))
														.replace("\u20B9", "").length() - 3));
										System.out.println("Redemption "+redemptionsSwitchOut);
										PortfolioValuationReport.setDividends(numberFormatter
												.format(Math.round(dividends)).replace("\u20B9", "").substring(0,
														numberFormatter.format(new BigDecimal(Math.round(dividends)))
																.replace("\u20B9", "").length() - 3));

										PortfolioValuationReport.setUnits(numberFormatter
												.format(new BigDecimal(FinexaUtil.roundOff(totalUnits, 2)))
												.replace("\u20B9", ""));
										System.out.println("TotalUnits "+totalUnits);
										PortfolioValuationReport.setInvestmentCost(numberFormatter
												.format(Math.round(diffCost)).replace("\u20B9", "").substring(0,
														numberFormatter.format(new BigDecimal(Math.round(diffCost)))
																.replace("\u20B9", "").length() - 3));
										PortfolioValuationReport.setPresentNAV(numberFormatter
												.format(new BigDecimal(FinexaUtil.roundOff(presentNAV, 2)))
												.replace("\u20B9", ""));
										PortfolioValuationReport.setMarketValue(numberFormatter
												.format(Math.round(marketValue)).replace("\u20B9", "").substring(0,
														numberFormatter.format(new BigDecimal(Math.round(marketValue)))
																.replace("\u20B9", "").length() - 3));
										try {
											PortfolioValuationReport.setXirr(
													numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(xirr, 2)))
															.replace("\u20B9", ""));
										} catch (Exception e) {
											PortfolioValuationReport.setXirr("0.0");
										}

										isDividendFound = true;
										alreadyCountInReportMap.put(invName.toLowerCase() + invPAN, folioNo);
										portfolioValuationReportList.add(PortfolioValuationReport);
										

									}

								}

							} catch (Exception e) {
							}

						}

					}

					if (isDividendFound == true) {

						if (count == mainNamePanMapforClient.size()) {
							flag = 0;

							if (countForFamily == mainNamePanMapforFamily.size()) {
								flagForFamily = 0;
							}
						}
						if (flag == 0 || flagForFamily == 0) {

							totalForXirr += (totalCostXirr / totalCostOfInvestment);
							PortfolioValuationReportColumnDTO PortfolioValuationReportTotal = new PortfolioValuationReportColumnDTO();

							PortfolioValuationReportTotal.setSchemeName("Total: ");

							PortfolioValuationReportTotal.setInvestmentSwitchIn(String.valueOf(
									numberFormatter.format(Math.round(totalForinvestmentSwitchIn)).replace("\u20B9", "")
											.substring(0, numberFormatter
													.format(new BigDecimal(Math.round(totalForinvestmentSwitchIn)))
													.replace("\u20B9", "").length() - 3)));
							PortfolioValuationReportTotal
									.setRedemptionsSwitchOut(String.valueOf((numberFormatter
											.format(Math.round(totalForredemptionsSwitchOut)).replace("\u20B9", "")
											.substring(0, numberFormatter
													.format(new BigDecimal(Math.round(totalForredemptionsSwitchOut)))
													.replace("\u20B9", "").length() - 3))));
							PortfolioValuationReportTotal
									.setDividends(
											String.valueOf(((numberFormatter.format(Math.round(totalForDividends))
													.replace("\u20B9", "")
													.substring(0, numberFormatter
															.format(new BigDecimal(Math.round(totalForDividends)))
															.replace("\u20B9", "").length() - 3)))));

							PortfolioValuationReportTotal
									.setInvestmentCost(
											String.valueOf(((numberFormatter.format(Math.round(totalCostOfInvestment))
													.replace("\u20B9", "")
													.substring(0, numberFormatter
															.format(new BigDecimal(Math.round(totalCostOfInvestment)))
															.replace("\u20B9", "").length() - 3)))));
							PortfolioValuationReportTotal
									.setMarketValue(
											String.valueOf(((numberFormatter.format(Math.round(totalForMarketValue))
													.replace("\u20B9", "")
													.substring(0, numberFormatter
															.format(new BigDecimal(Math.round(totalForMarketValue)))
															.replace("\u20B9", "").length() - 3)))));

							PortfolioValuationReportTotal.setXirr(
									numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(totalForXirr, 2)))
											.replace("\u20B9", ""));

							portfolioValuationReportList.add(PortfolioValuationReportTotal);
							System.out.println("Total "+PortfolioValuationReportTotal);
							count = 0;
							countForFamily = 0;
							isDividendFound = false;

						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				if (portfolioValuationReportList.size() > 0 && portfolioValuationReportList != null) {
					resultMap.put("key", portfolioValuationReportList);
				}
				System.out.println("portfolioValuationReportList " + portfolioValuationReportList);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

}
