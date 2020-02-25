package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.ClientMutualFundDTO;
import com.finlabs.finexa.dto.PortfolioValuationReportColumnDTO;
import com.finlabs.finexa.dto.PortfolioValuationReportDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientMutualFund;
import com.finlabs.finexa.model.LookupFundCategory;
import com.finlabs.finexa.model.LookupFundInvestmentMode;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.model.StagingFolioMasterBO;
import com.finlabs.finexa.model.StagingInvestorMasterBO;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AuxillaryFamilyMemberRepository;
import com.finlabs.finexa.repository.AuxillaryMasterRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientMutualFundRepository;
import com.finlabs.finexa.repository.LookupFundCategoryRepository;
import com.finlabs.finexa.repository.LookupFundInvestmentModeRepository;
import com.finlabs.finexa.repository.LookupRelationshipRepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.repository.StagingFolioMasterBORepository;
import com.finlabs.finexa.repository.StagingInvestorMasterBORepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.resources.model.GainCalculator;
import com.finlabs.finexa.resources.service.GainCalculatorBOService;
import com.finlabs.finexa.util.FinexaUtil;
import com.finlabs.finexa.util.XirrTest;

@Service("AUMAutoCreationNewService")
@Transactional
public class AUMAutoCreationNewServiceImpl implements AUMAutoCreationNewService {

	private static Logger log = LoggerFactory.getLogger(AUMAutoCreationNewServiceImpl.class);

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private MasterMFDailyNAVRepository masterMFDailyNAVRepository;

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepo;

	@Autowired
	private AuxillaryMasterRepository auxillaryMasterRepository;

	@Autowired
	private MasterMutualFundETFRepository masterMutualFundETFRepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;

	@Autowired
	private ClientMutualFundRepository clientMutualFundRepository;

	@Autowired
	private StagingInvestorMasterBORepository stagingInvestorMasterBORepository;

	@Autowired
	private StagingFolioMasterBORepository stagingFolioMasterBORepository;

	@Autowired
	private LookupRelationshipRepository lookupRelationshipRepository;

	@Autowired
	private LookupFundCategoryRepository lookupFundCategoryRepository;

	@Autowired
	private LookupFundInvestmentModeRepository lookupFundInvestmentModeRepository;

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd-MM-yyyy");
	public static final String MUTUAL_FUNDS = "Mutual Funds";
	public static final String YES = "Y";
	public static final String NO = "N";
	private static DecimalFormat df = new DecimalFormat("0.00");
	// List<Object[]> isinMaxDateList = new ArrayList<Object[]>();
	// Map<String, Date> isinMaxDateMap = new HashMap<String, Date>();

	public Map<String, ClientMutualFund> portfolioValuationReport(int advisorID, String investorName,
			String investorPAN) throws RuntimeException, ParseException {

		Map<String, ClientMutualFund> isinClientMutualFundMap = new HashMap<String, ClientMutualFund>();
		// List<ClientMutualFund> clientMutualFundList = new ArrayList<>();
		Date asOnDate = new Date();

		try {
			List<String> folioOfStagingList;
			List<String> folioOfTransactionList;
			List<String> notFoundRTAList = new ArrayList<String>();
			List<Integer> investorIdOfStagingInvestorList;
			List<String> distinctSchemeNameList;
			List<Object[]> folioSchemeNameList = new ArrayList<Object[]>();
			Double totalForinvestmentSwitchIn = 0.0d;
			Double totalForredemptionsSwitchOut = 0.0d;
			Double totalForDividends = 0.0d;

			// Gets all the investor IDs by investor names and PANs with respect to advisor
			// ID from stagingInvestorMasterBO
			investorIdOfStagingInvestorList = stagingInvestorMasterBORepository
					.getIdByAdvisorIdAndNameAndPAN(investorName, investorPAN, advisorID);
			// System.out.println("StagingInvestor:" + investorIdOfStagingInvestorList);

			// Gets all the folio numbers of the investor from stagingFolioMasterBO
			folioOfStagingList = stagingFolioMasterBORepository
					.getAllDistinctFolioById(investorIdOfStagingInvestorList.get(0));
			// System.out.println("StagingFolio:" + folioOfStagingList);

			// Gets all the folios of an investor from transactionMasterBO
			folioOfTransactionList = transactionMasterBORepository
					.getDistinctFolioNoByInvestor("%" + investorName + "%");
			// System.out.println("FolioOfTransaction:" + folioOfTransactionList);

			// Retains the common between stagingFolioMasterBO and transactionMasterBO
			folioOfTransactionList.retainAll(folioOfStagingList);
			// System.out.println("FolioInCommon:" + folioOfTransactionList);

			for (String folio : folioOfTransactionList) {
				try {
					// Gets list of distinct scheme names and folio number of a specific investor
					distinctSchemeNameList = transactionMasterBORepository.getDistinctRTACodeByFolioNo(folio,
							investorName);
					for (String schemeName : distinctSchemeNameList) {
						Object[] schemeFolio = new Object[2];
						schemeFolio[0] = folio;
						schemeFolio[1] = schemeName;
						folioSchemeNameList.add(schemeFolio);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			for (Object[] transactionMasterBO : folioSchemeNameList) {
				try {
					List<Date> dateForCalculation = new ArrayList<>();
					List<Double> paymentsForCalculation = new ArrayList<Double>();
					String folioNo = (String) transactionMasterBO[0];
					String rtaCode = (String) transactionMasterBO[1];
					if(notFoundRTAList.size() > 0) {
						if(notFoundRTAList.contains(rtaCode.trim())) {
							log.debug("SCHEME ISIN MAPPING NOT FOUND FOR RTA CODE: " + rtaCode);
							continue;
						}
					}
					Date firstInvestmentDate = transactionMasterBORepository
							.findMinDateByInvestorAndFolioAndSchemeRTA(investorName, folioNo, rtaCode);
					int toBeConsideredFlag = 1; // Not required

					Double investmentSwitchIn = 0.0d;
					Double redemptionsSwitchOut = 0.0d;
					Double dividends = 0.0d;
					Double presentNAV = 0.0d;
					Double marketValue = 0.0d;
					Double totalUnits = 0.0d;

					List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo.findByCamsCodeAndStatus(rtaCode,
							"NEW");
					ClientMutualFund clientMutualFund = new ClientMutualFund();

					if (isinMasterBO.size() > 0) {
						String isin = isinMasterBO.get(0).getIsin();
						MasterMutualFundETF masterMutualFundETF = masterMutualFundETFRepository.findByIsin(isin);
						if(masterMutualFundETF == null) {
							log.debug("MasterMutualFundETF NOT FOUND for ISIN: "+ isin +". So, this record is skipped.");
							continue;
						}
						if (toBeConsideredFlag == 1) {

							List<TransactionMasterBO> transactionsBasedOnFolioAndSchemeList = transactionMasterBORepository
									.findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeRTA(
											investorName, folioNo, rtaCode, firstInvestmentDate, asOnDate);
							clientMutualFund.setIsin(isin);
							clientMutualFund.setInvestmentStartDate(firstInvestmentDate);
							for (TransactionMasterBO tmbo : transactionsBasedOnFolioAndSchemeList) {

								try {

									dateForCalculation.add(tmbo.getTransactionDate());
									if (tmbo.getLookupTransactionRule() != null) {

										switch (tmbo.getLookupTransactionRule().getCode()) {
										case "P":
											investmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));

											break;
										case "R":
											redemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalForredemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "RED":
											redemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalForredemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "Redemption":
											redemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalForredemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "REDR":
											redemptionsSwitchOut -= (Double.parseDouble(tmbo.getTransAmt()));
											totalForredemptionsSwitchOut -= (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "SI":
											investmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));
											break;

										case "SWIN":

											investmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "SWINR":

											investmentSwitchIn -= (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn -= (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "STP In":
											investmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "STPI":
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											investmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "STPIR":
											investmentSwitchIn -= (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn -= (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));
											break;

										case "STP O":
											redemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalForredemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "STP Out":
											redemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalForredemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));
										case "STPOR":
											redemptionsSwitchOut -= (Double.parseDouble(tmbo.getTransAmt()));
											totalForredemptionsSwitchOut -= (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "SO":
											redemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalForredemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "SWOF":
											redemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalForredemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "SWOFR":
											redemptionsSwitchOut -= (Double.parseDouble(tmbo.getTransAmt()));
											totalForredemptionsSwitchOut -= (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "TI":
											investmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "TO":
											redemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalForredemptionsSwitchOut += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "DR":
											dividends += (Double.parseDouble(tmbo.getTransAmt()));
											totalForDividends = (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "DIR":
											dividends += (Double.parseDouble(tmbo.getTransAmt()));
											totalForDividends = (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "Dividend Reinvestment":
											dividends += (Double.parseDouble(tmbo.getTransAmt()));
											totalForDividends = (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "SIP":
											investmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));

											break;
										case "SIPR":
											investmentSwitchIn -= (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn -= (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));

											break;
										case "ADDPUR":

											investmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));

											break;
										case "Additional Purchase":
											investmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));

											break;
										case "New Purchase":
											investmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));
											break;
										case "NEWPUR":
											investmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn += (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits += (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(Double.parseDouble(tmbo.getTransAmt()));

											break;
										case "ADDPURR":
											investmentSwitchIn -= (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn -= (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));

											break;
										case "NEWPURR":
											investmentSwitchIn -= (Double.parseDouble(tmbo.getTransAmt()));
											totalForinvestmentSwitchIn -= (Double.parseDouble(tmbo.getTransAmt()));
											totalUnits -= (Double.parseDouble(tmbo.getTransUnits()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));

											break;
										case "DP":
											dividends += (Double.parseDouble(tmbo.getTransAmt()));
											totalForDividends += (Double.parseDouble(tmbo.getTransAmt()));
											paymentsForCalculation.add(-Double.parseDouble(tmbo.getTransAmt()));

											break;
										case "J":
											// no effect
											break;
										default:
											break;
										}
									}

								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							Date maxDate = masterMFDailyNAVRepository.findMaxDate(isin, asOnDate);
							
							MasterMFDailyNAV dailyNAV = masterMFDailyNAVRepository.findNAV(isin, maxDate);
							if (dailyNAV != null) {
								presentNAV = ((double) dailyNAV.getNav());
							} else {
								presentNAV = (FinexaUtil.roundOff(0, 2));
							}

							marketValue = (totalUnits * presentNAV);
							clientMutualFund.setFolioNumber(folioNo);
							BigDecimal bigDeci= new BigDecimal(investmentSwitchIn);
							bigDeci = bigDeci.setScale(2, RoundingMode.HALF_UP);
							clientMutualFund.setInvestmentAmount(bigDeci);
							BigDecimal bd = new BigDecimal(marketValue);
							bd = bd.setScale(2, RoundingMode.HALF_UP);
							// System.out.println("MARKET VALUE:"+bd);
							clientMutualFund.setCurrentMarketValue(bd);
							int totalUnit = totalUnits.intValue();
							//System.out.println("UNITS TU:"+totalUnit);
							if(totalUnit < 0)
								totalUnit = 0;
							clientMutualFund.setLumpsumUnitsPurchased(new BigDecimal(totalUnit));
							clientMutualFund.setMasterProductClassification(
									masterProductClassificationRepository.findByProductName(MUTUAL_FUNDS));
							//System.out.println("ISIN: "+isin);
							//System.out.println(masterMutualFundETF.getLookupAssetSubClass());
							if(masterMutualFundETF.getLookupAssetSubClass() != null)
								clientMutualFund.setLookupAssetSubClass(masterMutualFundETF.getLookupAssetSubClass());
							LookupFundInvestmentMode lfm = lookupFundInvestmentModeRepository.getOne((byte) 1);
							/*
							 * if (lfm != null) System.out.println(lfm.getId());
							 */
							clientMutualFund.setLookupFundInvestmentMode(lfm);

							/*
							 * This method of setting LookupFundCategory is wrong but it is done here like
							 * this because this approach is done in ClientFundServiceImpl
							 */
							LookupFundCategory lookupFundCategory = lookupFundCategoryRepository
									.findOne(masterMutualFundETF.getLookupAssetClass().getId());
							clientMutualFund.setLookupFundCategory(lookupFundCategory);

						}
					} else {
						log.debug("SCHEME ISIN MAPPING NOT FOUND FOR RTA CODE: " + rtaCode);
						notFoundRTAList.add(rtaCode.trim());
						continue;
					}

					if (isinClientMutualFundMap != null && !isinClientMutualFundMap.isEmpty()
							&& clientMutualFund.getIsin() != null && clientMutualFund.getIsin() != ""
							&& !clientMutualFund.getIsin().isEmpty()) {
						if (isinClientMutualFundMap.containsKey(
								clientMutualFund.getIsin().trim() + "-" + clientMutualFund.getFolioNumber().trim())) {
							ClientMutualFund clientMF = new ClientMutualFund();
							clientMF = isinClientMutualFundMap.get(clientMutualFund.getIsin());
							double investmentAmount = clientMF.getInvestmentAmount().doubleValue()
									+ clientMutualFund.getInvestmentAmount().doubleValue();
							if (clientMF.getInvestmentStartDate().after(clientMutualFund.getInvestmentStartDate())) {
								clientMF.setInvestmentStartDate(clientMutualFund.getInvestmentStartDate());
							}
							BigDecimal big= new BigDecimal(investmentAmount);
							big = big.setScale(2, RoundingMode.HALF_UP);
							double units = clientMF.getLumpsumUnitsPurchased().doubleValue()
									+ clientMutualFund.getLumpsumUnitsPurchased().doubleValue();
							double currentValue = clientMF.getCurrentMarketValue().doubleValue()
									+ clientMutualFund.getCurrentMarketValue().doubleValue();
							clientMF.setInvestmentAmount(big);
							int unit = (int) units;
							//System.out.println("UNITS:"+unit);
							if(unit < 0)
								unit = 0;
							clientMF.setLumpsumUnitsPurchased(new BigDecimal(unit));
							// System.out.println("ROUNDED OFF: "+Math.round(currentValue * 100.0) / 100.0);
							clientMF.setCurrentMarketValue(new BigDecimal(Math.round(currentValue * 100.0) / 100.0));
							isinClientMutualFundMap.put(
									clientMutualFund.getIsin().trim() + "-" + clientMutualFund.getFolioNumber().trim(),
									clientMF);
						} else if (clientMutualFund.getIsin() != null && clientMutualFund.getIsin() != ""
								&& !clientMutualFund.getIsin().isEmpty()) {
							isinClientMutualFundMap.put(
									clientMutualFund.getIsin().trim() + "-" + clientMutualFund.getFolioNumber().trim(),
									clientMutualFund);
						}
					} else if (clientMutualFund.getIsin() != null && clientMutualFund.getIsin() != ""
							&& !clientMutualFund.getIsin().isEmpty()) {
						isinClientMutualFundMap.put(
								clientMutualFund.getIsin().trim() + "-" + clientMutualFund.getFolioNumber().trim(),
								clientMutualFund);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return clientMutualFundList;
		return isinClientMutualFundMap;
	}

	@Override
	public void createAutoAUM(int advisorID, Map<String, String> clientNamePANMap,
			Map<String, String> clientFolioNameMap) {
		// TODO Auto-generated method stub0
		try {

			List<ClientMaster> clientMasterList = new ArrayList<ClientMaster>();
			List<AuxillaryInvestorMaster> auxillaryInvestorMasterList;
			List<ClientFamilyMember> clientFamilyMemberList;
			List<AuxillaryFamilyMember> auxillaryFamilyMemberList;
			// List<ClientMutualFund> generatedClientMutualFundList;
			// List<ClientMutualFund> generatedAllClientMutualFundList;
			Map<String, ClientMaster> clientMasterMap = new HashMap<>();
			Map<String, ClientMaster> clientMasterNamePANMap = new HashMap<>();
			Map<String, AuxillaryInvestorMaster> auxiliaryInvesorMasterNamePANMap = new HashMap<>();
			Map<String, ClientFamilyMember> clientFamilyNamePANMap = new HashMap<>();
			Map<String, AuxillaryFamilyMember> auxiliaryFamilyMasterNamePANMap = new HashMap<>();
			Map<String, ClientFamilyMember> clientFamilyMemberMap = new HashMap<>();
			Map<String, ClientMutualFund> clientIDFamilyIDISINClientMutualFundListMap = new HashMap<String, ClientMutualFund>();
			Map<Integer, StagingInvestorMasterBO> stagingInvestorMap = new HashMap<Integer, StagingInvestorMasterBO>();
			AdvisorUser advisorUser = advisorUserRepository.findById(advisorID);
			
			if (clientFolioNameMap.size() > 0) {
				
				List<StagingInvestorMasterBO> stagingInvestorList = stagingInvestorMasterBORepository
						.findByAdvisoruser(advisorUser);
				for (StagingInvestorMasterBO stagingInvestorMasterBO : stagingInvestorList) {
					try {
						stagingInvestorMap.put(stagingInvestorMasterBO.getId(), stagingInvestorMasterBO);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				for (Map.Entry<String, String> clientFolioName : clientFolioNameMap.entrySet()) {
					try {
						List<StagingFolioMasterBO> stagingFolioList = stagingFolioMasterBORepository
								.findByInvestorFolio(clientFolioName.getKey());
						for (StagingFolioMasterBO stagingFolioMasterBO : stagingFolioList) {
							try {
								if(stagingInvestorMap.containsKey(stagingFolioMasterBO.getStaginginvestormasterbo().getId())) {
									String iName = stagingInvestorMap.get(stagingFolioMasterBO.getStaginginvestormasterbo().getId()).getInvestorName();
									String iPAN = stagingInvestorMap.get(stagingFolioMasterBO.getStaginginvestormasterbo().getId()).getInvestorPAN();
									clientNamePANMap.put(iName.trim() + "-" + iPAN, "");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			if (clientNamePANMap.size() > 0) {
				// List <String> donePANList = new ArrayList<>();
				// isinMaxDateList = masterMFDailyNAVRepository.findDistinctIsinMaxDateList();
				/*
				 * for (Object[] isinMaxDate : isinMaxDateList) {
				 * isinMaxDateMap.put(isinMaxDate[0].toString(), (Date) isinMaxDate[1]); }
				 */
				clientMasterList = advisorUser.getClientMasters();
				for(ClientMaster clientMaster : clientMasterList) {
					try {								
						if (clientMaster != null) {
							String name = clientMaster.getFirstName().trim() + (clientMaster.getMiddleName() == null ? "" : (" " + clientMaster.getMiddleName().trim()))
									+ (clientMaster.getLastName() == null ? "" : " " + (clientMaster.getLastName().trim()));
							String pan = clientMaster.getPan();
							
							clientMasterNamePANMap.put(name.trim() + "-" + pan.trim(), clientMaster);
							auxillaryInvestorMasterList = new ArrayList<>();
							auxillaryInvestorMasterList = clientMaster.getAuxillaryInvestorMasters();
							if(auxillaryInvestorMasterList.size() > 0) {
								for (AuxillaryInvestorMaster auxillaryInvestorMaster : auxillaryInvestorMasterList) {
									try {
										String auxInvName = auxillaryInvestorMaster.getFirstName().trim() + (auxillaryInvestorMaster.getMiddleName() == null ? "" : (" " + auxillaryInvestorMaster.getMiddleName().trim()))
												+ (auxillaryInvestorMaster.getLastName() == null ? "" : " " + (auxillaryInvestorMaster.getLastName().trim()));
										String auxInvPAN = auxillaryInvestorMaster.getPan();
										auxiliaryInvesorMasterNamePANMap.put(auxInvName.trim() + "-" + auxInvPAN.trim(), auxillaryInvestorMaster);
										
									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}
							}
							clientFamilyMemberList = clientMaster.getClientFamilyMembers();
							if(clientFamilyMemberList.size() > 0) {
								for (ClientFamilyMember clientFamilyMember : clientFamilyMemberList) {
									try {
										String clientFamilyName = clientFamilyMember.getFirstName().trim() + (clientFamilyMember.getMiddleName() == null ? "" : (" " + clientFamilyMember.getMiddleName().trim()))
												+ (clientFamilyMember.getLastName() == null ? "" : " " + (clientFamilyMember.getLastName().trim()));
										String clientFamilyPAN = clientFamilyMember.getPan();
										clientFamilyNamePANMap.put(clientFamilyName.trim() + "-" + clientFamilyPAN.trim(), clientFamilyMember);
										auxillaryFamilyMemberList = clientFamilyMember.getAuxillaryFamilyMembers();
										
										if(auxillaryFamilyMemberList.size() > 0) {
											for (AuxillaryFamilyMember auxillaryFamilyMember : auxillaryFamilyMemberList) {
												try {
													String auxFamilyName = auxillaryFamilyMember.getFirstName().trim() + (auxillaryFamilyMember.getMiddleName() == null ? "" : (" " + auxillaryFamilyMember.getMiddleName().trim()))
															+ (auxillaryFamilyMember.getLastName() == null ? "" : " " + (auxillaryFamilyMember.getLastName().trim()));
													String auxFamilyPAN = auxillaryFamilyMember.getPan();
													auxiliaryFamilyMasterNamePANMap.put(auxFamilyName.trim() + "-" + auxFamilyPAN.trim(), auxillaryFamilyMember);
													
												} catch (Exception e) {
													e.printStackTrace();
												}
												
											}
										}
										
									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}
							}
							
						}	
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for (Map.Entry<String, String> clientNamePAN : clientNamePANMap.entrySet()) {
					try {
						String namePAN[] = clientNamePAN.getKey().split("-");
						String investorName = namePAN[0];
						String investorPAN = "";
						// System.out.println("LENGTH:" + namePAN.length);
						if (namePAN.length == 2) {
							if (namePAN[1] != null && !namePAN[1].isEmpty() && namePAN[1] != "")
								investorPAN = namePAN[1];
						} else {
							log.debug("Investor NAme or PAN not found");
							continue;
						}
						/*
						if (investorPAN == null || investorPAN == "" || investorPAN.isEmpty()) {
							StagingInvestorMasterBO stagingInvestor = stagingInvestorMasterBORepository
									.findByInvestorName(investorName.trim());
							investorPAN = stagingInvestor.getInvestorPAN();
						}
						*/
						boolean isClientFound = false;
						
						if(clientMasterNamePANMap.containsKey(clientNamePAN.getKey())) {
							isClientFound = true;
							clientNamePAN
									.setValue(
											String.valueOf(
													clientMasterNamePANMap.get(clientNamePAN.getKey()).getId() + "-"
															+ String.valueOf(clientFamilyNamePANMap.get(clientNamePAN.getKey()).getId())));	
						} else if (auxiliaryInvesorMasterNamePANMap.containsKey(clientNamePAN.getKey())){
							isClientFound = true;
							int clientFamilyID = 0;
							int clientID = auxiliaryInvesorMasterNamePANMap.get(clientNamePAN.getKey()).getClientMaster().getId();
							 for (Map.Entry<String,ClientFamilyMember> entry : clientFamilyNamePANMap.entrySet()) {
								 if(entry.getValue().getClientMaster().getId() == clientID) {
									 clientFamilyID = entry.getValue().getId();
								 }
							 }
						            
							clientNamePAN
									.setValue(
											String.valueOf(auxiliaryInvesorMasterNamePANMap.get(clientNamePAN.getKey()).getClientMaster().getId() + "-"
													+ String.valueOf(clientFamilyID)));
																
						} else if (clientFamilyNamePANMap.containsKey(clientNamePAN.getKey())){
							isClientFound = true;
							clientNamePAN.setValue(String
									.valueOf(clientFamilyNamePANMap.get(clientNamePAN.getKey()).getClientMaster().getId() + "-"
											+ String.valueOf(clientFamilyNamePANMap.get(clientNamePAN.getKey()).getId())));	
						} else if (auxiliaryFamilyMasterNamePANMap.containsKey(clientNamePAN.getKey())){
							isClientFound = true;
							clientNamePAN.setValue(String.valueOf(auxiliaryFamilyMasterNamePANMap.get(clientNamePAN.getKey()).getClientFamilyMember().getClientMaster().getId() + "-"
									+ String.valueOf(auxiliaryFamilyMasterNamePANMap.get(clientNamePAN.getKey()).getClientFamilyMember().getId())));
						}
						
						
						// generatedClientMutualFundList = new ArrayList<>();
						// Searched in Client Master
						/*
						clientMaster = clientMasterRepository.findByAdvisorIdAndPan(advisorID, investorPAN);
						if (clientMaster != null) {
							String nameOfInvestor[] = investorName.trim().split(" ");
							
							if (nameOfInvestor.length == 1 && clientMaster.getMiddleName() == null
									&& clientMaster.getLastName() == null) {
								if (clientMaster.getFirstName().trim().equalsIgnoreCase(nameOfInvestor[0])) {
									isClientFound = true;
									clientNamePAN
											.setValue(
													String.valueOf(
															clientMaster.getId() + "-"
																	+ String.valueOf(clientFamilyMemberRepository
																			.findByPanAndClientMasterAndLookupRelation(
																					investorPAN, clientMaster,
																					lookupRelationshipRepository
																							.getOne((byte) 0))
																			.getId())));
								}
							} else if (nameOfInvestor.length == 2 && clientMaster.getMiddleName() == null) {
								if (clientMaster.getFirstName().trim().equalsIgnoreCase(nameOfInvestor[0])

										&& (clientMaster.getLastName() != null && clientMaster.getLastName() != ""
												&& !clientMaster.getLastName().isEmpty() && clientMaster.getLastName()
														.trim().equalsIgnoreCase(nameOfInvestor[1]))) {
									isClientFound = true;
									clientNamePAN
											.setValue(
													String.valueOf(
															clientMaster.getId() + "-"
																	+ String.valueOf(clientFamilyMemberRepository
																			.findByPanAndClientMasterAndLookupRelation(
																					investorPAN, clientMaster,
																					lookupRelationshipRepository
																							.getOne((byte) 0))
																			.getId())));
								}
							} else if (nameOfInvestor.length == 3) {
								if (clientMaster.getFirstName().trim().equalsIgnoreCase(nameOfInvestor[0])

										&& (clientMaster.getMiddleName() != null && clientMaster.getMiddleName() != ""
												&& !clientMaster.getMiddleName().isEmpty()
												&& clientMaster.getMiddleName().trim()
														.equalsIgnoreCase(nameOfInvestor[1]))

										&& (clientMaster.getLastName() != null && clientMaster.getLastName() != ""
												&& !clientMaster.getLastName().isEmpty() && clientMaster.getLastName()
														.trim().equalsIgnoreCase(nameOfInvestor[2]))) {
									isClientFound = true;
									clientNamePAN
											.setValue(
													String.valueOf(
															clientMaster.getId() + "-"
																	+ String.valueOf(clientFamilyMemberRepository
																			.findByPanAndClientMasterAndLookupRelation(
																					investorPAN, clientMaster,
																					lookupRelationshipRepository
																							.getOne((byte) 0))
																			.getId())));
								}

							} else if(nameOfInvestor.length > 3) {
								
								String lastName = nameOfInvestor[2].trim();
								for (int counter = 3; counter < nameOfInvestor.length; counter++) {
									lastName = lastName + " " + nameOfInvestor[counter].trim();
								}
								if (clientMaster.getFirstName().trim().equalsIgnoreCase(nameOfInvestor[0])
										&& (clientMaster.getMiddleName() != null && clientMaster.getMiddleName() != ""
												&& !clientMaster.getMiddleName().isEmpty()
												&& clientMaster.getMiddleName().trim()
														.equalsIgnoreCase(nameOfInvestor[1]))
										&& (clientMaster.getLastName().trim().equalsIgnoreCase(lastName))) {
									isClientFound = true;
									clientNamePAN
											.setValue(
													String.valueOf(
															clientMaster.getId() + "-"
																	+ String.valueOf(clientFamilyMemberRepository
																			.findByPanAndClientMasterAndLookupRelation(
																					investorPAN, clientMaster,
																					lookupRelationshipRepository
																							.getOne((byte) 0))
																			.getId())));
								}
							}

						}
						
						// Searched in Auxiliary Investor Master
						
						if (isClientFound == false && clientMaster != null) {
							auxillaryInvestorMasterList = clientMaster.getAuxillaryInvestorMasters();
							for (AuxillaryInvestorMaster auxillaryInvestorMaster : auxillaryInvestorMasterList) {
								try {
									String nameOfInvestor[] = investorName.trim().split(" ");

									if (nameOfInvestor.length == 1 && auxillaryInvestorMaster.getMiddleName() == null
											&& auxillaryInvestorMaster.getLastName() == null) {
										if (auxillaryInvestorMaster.getFirstName().trim()
												.equalsIgnoreCase(nameOfInvestor[0])) {
											isClientFound = true;
											clientNamePAN
													.setValue(
															String.valueOf(clientMaster.getId() + "-"
																	+ String.valueOf(clientFamilyMemberRepository
																			.findByPanAndClientMasterAndLookupRelation(
																					investorPAN, clientMaster,
																					lookupRelationshipRepository
																							.getOne((byte) 0))
																			.getId())));
										}
									} else if (nameOfInvestor.length == 2
											&& auxillaryInvestorMaster.getMiddleName() == null) {
										if (auxillaryInvestorMaster.getFirstName().trim()
												.equalsIgnoreCase(nameOfInvestor[0])
												&& (auxillaryInvestorMaster.getLastName() != null
														&& auxillaryInvestorMaster.getLastName().trim() != ""
														&& !auxillaryInvestorMaster.getLastName().trim().isEmpty()
														&& auxillaryInvestorMaster.getLastName().trim()
																.equalsIgnoreCase(nameOfInvestor[1]))) {
											isClientFound = true;
											clientNamePAN
													.setValue(
															String.valueOf(clientMaster.getId() + "-"
																	+ String.valueOf(clientFamilyMemberRepository
																			.findByPanAndClientMasterAndLookupRelation(
																					investorPAN, clientMaster,
																					lookupRelationshipRepository
																							.getOne((byte) 0))
																			.getId())));
										}
									} else if (nameOfInvestor.length == 3) {
										//System.out.println(nameOfInvestor[0]+"^"+nameOfInvestor[1]+"^"+nameOfInvestor[2]+"^");
										if (auxillaryInvestorMaster.getFirstName().trim()
												.equalsIgnoreCase(nameOfInvestor[0])
												&& (auxillaryInvestorMaster.getMiddleName() != null
														&& auxillaryInvestorMaster.getMiddleName().trim() != ""
														&& !auxillaryInvestorMaster.getMiddleName().trim().isEmpty()
														&& auxillaryInvestorMaster.getMiddleName().trim()
																.equalsIgnoreCase(nameOfInvestor[1]))
												&& (auxillaryInvestorMaster.getLastName() != null
														&& auxillaryInvestorMaster.getLastName().trim() != ""
														&& !auxillaryInvestorMaster.getLastName().trim().isEmpty()
														&& auxillaryInvestorMaster.getLastName().trim()
																.equalsIgnoreCase(nameOfInvestor[2]))) {
											isClientFound = true;
											clientNamePAN
													.setValue(
															String.valueOf(clientMaster.getId() + "-"
																	+ String.valueOf(clientFamilyMemberRepository
																			.findByPanAndClientMasterAndLookupRelation(
																					investorPAN, clientMaster,
																					lookupRelationshipRepository
																							.getOne((byte) 0))
																			.getId())));
										}

									} else if(nameOfInvestor.length > 3) {
										String lastName = nameOfInvestor[2].trim();
										for (int counter = 3; counter < nameOfInvestor.length; counter++) {
											lastName = lastName + " " + nameOfInvestor[counter].trim();
										}
										if (auxillaryInvestorMaster.getFirstName().trim()
												.equalsIgnoreCase(nameOfInvestor[0])
												&& (auxillaryInvestorMaster.getMiddleName() != null
														&& auxillaryInvestorMaster.getMiddleName().trim() != ""
														&& !auxillaryInvestorMaster.getMiddleName().trim().isEmpty()
														&& auxillaryInvestorMaster.getMiddleName().trim()
																.equalsIgnoreCase(nameOfInvestor[1]))
												&& auxillaryInvestorMaster.getLastName().trim()
														.equalsIgnoreCase(lastName)) {
											isClientFound = true;
											clientNamePAN
													.setValue(
															String.valueOf(clientMaster.getId() + "-"
																	+ String.valueOf(clientFamilyMemberRepository
																			.findByPanAndClientMasterAndLookupRelation(
																					investorPAN, clientMaster,
																					lookupRelationshipRepository
																							.getOne((byte) 0))
																			.getId())));
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}
						// Searched in Client Family Master
						if (isClientFound == false) {
							List<ClientMaster> cmList = advisorUser.getClientMasters();

							for (ClientMaster cmL : cmList) {
								try {
									clientFamilyMemberList = cmL.getClientFamilyMembers();
									for (ClientFamilyMember clientFamilyMember : clientFamilyMemberList) {
										try {
											String nameOfInvestor[] = investorName.trim().split(" ");

											if (nameOfInvestor.length == 1 && clientFamilyMember.getMiddleName() == null
													&& clientFamilyMember.getLastName() == null) {
												if (clientFamilyMember.getFirstName().trim()
														.equalsIgnoreCase(nameOfInvestor[0])) {
													isClientFound = true;
													clientNamePAN.setValue(String
															.valueOf(clientFamilyMember.getClientMaster().getId() + "-"
																	+ String.valueOf(clientFamilyMember.getId())));
													break;
												}
											} else if (nameOfInvestor.length == 2
													&& clientFamilyMember.getMiddleName() == null) {
												if (clientFamilyMember.getFirstName().trim()
														.equalsIgnoreCase(nameOfInvestor[0])
														&& (clientFamilyMember.getLastName() != null
																&& clientFamilyMember.getLastName().trim() != ""
																&& !clientFamilyMember.getLastName().trim().isEmpty()
																&& clientFamilyMember.getLastName().trim()
																		.equalsIgnoreCase(nameOfInvestor[1]))) {
													isClientFound = true;
													clientNamePAN.setValue(String
															.valueOf(clientFamilyMember.getClientMaster().getId() + "-"
																	+ String.valueOf(clientFamilyMember.getId())));
													break;
												}
											} else if (nameOfInvestor.length == 3) {
												if (clientFamilyMember.getFirstName().trim()
														.equalsIgnoreCase(nameOfInvestor[0])
														&& (clientFamilyMember.getMiddleName() != null
																&& clientFamilyMember.getMiddleName().trim() != ""
																&& !clientFamilyMember.getMiddleName().trim().isEmpty()
																&& clientFamilyMember.getMiddleName().trim()
																		.equalsIgnoreCase(nameOfInvestor[1]))
														&& (clientFamilyMember.getLastName() != null
																&& clientFamilyMember.getLastName().trim() != ""
																&& !clientFamilyMember.getLastName().trim().isEmpty()
																&& clientFamilyMember.getLastName().trim()
																		.equalsIgnoreCase(nameOfInvestor[2]))) {
													isClientFound = true;
													clientNamePAN.setValue(String
															.valueOf(clientFamilyMember.getClientMaster().getId() + "-"
																	+ String.valueOf(clientFamilyMember.getId())));
													break;
												}

											} else if(nameOfInvestor.length > 3) {
												String lastName = nameOfInvestor[2].trim();
												for (int counter = 3; counter < nameOfInvestor.length; counter++) {
													lastName = lastName + " " + nameOfInvestor[counter].trim();
												}
												if (clientFamilyMember.getFirstName().trim()
														.equalsIgnoreCase(nameOfInvestor[0])
														&& (clientFamilyMember.getMiddleName() != null
																&& clientFamilyMember.getMiddleName().trim() != ""
																&& !clientFamilyMember.getMiddleName().trim().isEmpty()
																&& clientFamilyMember.getMiddleName().trim()
																		.equalsIgnoreCase(nameOfInvestor[1]))
														&& clientFamilyMember.getLastName().trim()
																.equalsIgnoreCase(lastName)) {
													isClientFound = true;
													clientNamePAN.setValue(String
															.valueOf(clientFamilyMember.getClientMaster().getId() + "-"
																	+ String.valueOf(clientFamilyMember.getId())));
													break;
												}
											}

											if (isClientFound == false) {

												auxillaryFamilyMemberList = clientFamilyMember
														.getAuxillaryFamilyMembers();
												for (AuxillaryFamilyMember auxillaryFamilyMember : auxillaryFamilyMemberList) {
													try {
														// String nameOfinvestor[] = investorName.trim().split(" ");

														if (nameOfInvestor.length == 1
																&& auxillaryFamilyMember.getMiddleName() == null
																&& auxillaryFamilyMember.getLastName() == null) {
															if (auxillaryFamilyMember.getFirstName().trim()
																	.equalsIgnoreCase(nameOfInvestor[0])) {
																isClientFound = true;
																clientNamePAN.setValue(String.valueOf(clientFamilyMember
																		.getClientMaster().getId() + "-"
																		+ String.valueOf(clientFamilyMember.getId())));
																break;
															}
														} else if (nameOfInvestor.length == 2
																&& auxillaryFamilyMember.getMiddleName() == null) {
															if (auxillaryFamilyMember.getFirstName().trim()
																	.equalsIgnoreCase(nameOfInvestor[0])
																	&& (auxillaryFamilyMember.getLastName() != null
																			&& auxillaryFamilyMember.getLastName() != ""
																			&& !auxillaryFamilyMember.getLastName()
																					.isEmpty()
																			&& auxillaryFamilyMember.getLastName()
																					.trim().equalsIgnoreCase(
																							nameOfInvestor[1]))) {
																isClientFound = true;
																clientNamePAN.setValue(String.valueOf(clientFamilyMember
																		.getClientMaster().getId() + "-"
																		+ String.valueOf(clientFamilyMember.getId())));
																break;
															}
														} else if (nameOfInvestor.length == 3) {
															if (auxillaryFamilyMember.getFirstName().trim()
																	.equalsIgnoreCase(nameOfInvestor[0])
																	&& (auxillaryFamilyMember.getMiddleName() != null
																			&& auxillaryFamilyMember
																					.getMiddleName() != ""
																			&& !auxillaryFamilyMember.getMiddleName()
																					.trim().isEmpty()
																			&& auxillaryFamilyMember.getMiddleName()
																					.trim().equalsIgnoreCase(
																							nameOfInvestor[1]))
																	&& (auxillaryFamilyMember.getLastName() != null
																			&& auxillaryFamilyMember.getLastName() != ""
																			&& !auxillaryFamilyMember.getLastName()
																					.isEmpty()
																			&& auxillaryFamilyMember.getLastName()
																					.trim().equalsIgnoreCase(
																							nameOfInvestor[2]))) {
																isClientFound = true;
																clientNamePAN.setValue(String.valueOf(clientFamilyMember
																		.getClientMaster().getId() + "-"
																		+ String.valueOf(clientFamilyMember.getId())));
																break;
															}

														} else if(nameOfInvestor.length > 3) {
															String lastName = nameOfInvestor[2].trim();
															for (int counter = 3; counter < nameOfInvestor.length; counter++) {
																lastName = lastName + " " + nameOfInvestor[counter].trim();
															}
															if (auxillaryFamilyMember.getFirstName().trim()
																	.equalsIgnoreCase(nameOfInvestor[0])
																	&& (auxillaryFamilyMember.getMiddleName() != null
																			&& auxillaryFamilyMember
																					.getMiddleName() != ""
																			&& !auxillaryFamilyMember.getMiddleName()
																					.trim().isEmpty()
																			&& auxillaryFamilyMember.getMiddleName()
																					.trim().equalsIgnoreCase(
																							nameOfInvestor[1]))
																	&& auxillaryFamilyMember.getLastName().trim()
																			.equalsIgnoreCase(lastName)) {
																isClientFound = true;
																clientNamePAN.setValue(String.valueOf(clientFamilyMember
																		.getClientMaster().getId() + "-"
																		+ String.valueOf(clientFamilyMember.getId())));
																break;
															}
														}
													} catch (Exception e) {
														e.printStackTrace();
													}
												}

											}

											if (isClientFound == true) {
												break;
											}

										} catch (Exception e) {
											e.printStackTrace();
										}
									}

									if (isClientFound == true) {
										break;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							}

						}
						*/
						if (isClientFound == true) {

							String clientIDAndFamilyID[] = clientNamePAN.getValue().split("-");
							int clientID = Integer.parseInt(clientIDAndFamilyID[0]);
							int familyID = Integer.parseInt(clientIDAndFamilyID[1]);

							ClientMaster clM = clientMasterRepository.getOne(clientID);
							ClientFamilyMember cFM = clientFamilyMemberRepository.getOne(familyID);

							if (!clientMasterMap.isEmpty()) {
								if (!clientMasterMap.containsKey(clientIDAndFamilyID[0])) {
									clientMasterMap.put(clientIDAndFamilyID[0], clM);
								}
							} else {
								clientMasterMap.put(clientIDAndFamilyID[0], clM);
							}
							if (!clientFamilyMemberMap.isEmpty()) {
								if (!clientFamilyMemberMap.containsKey(clientIDAndFamilyID[1])) {
									clientFamilyMemberMap.put(clientIDAndFamilyID[1], cFM);
								}
							} else {
								clientFamilyMemberMap.put(clientIDAndFamilyID[1], cFM);
							}
							String clientIDFamilyIDISINFolio = String.valueOf(clientID) + "-"
									+ String.valueOf(familyID);
							log.debug("AUM calculation started for: "+investorName+"-"+investorPAN);
							Map<String, ClientMutualFund> isinClientMutualFundMap = portfolioValuationReport(advisorID,
									investorName, investorPAN);

							for (Map.Entry<String, ClientMutualFund> isinClientMutualFund : isinClientMutualFundMap
									.entrySet()) {
								try {

									ClientMutualFund clientMF = isinClientMutualFund.getValue();
									Timestamp timestamp = new Timestamp(System.currentTimeMillis());
									clientMF.setClientMaster(clM);
									clientMF.setClientFamilyMember(cFM);
									clientMF.setCloseEndedFlag(NO);
									clientMF.setCreatedOn(timestamp);
									clientMF.setLastUpdatedOn(timestamp);
									clientMF.setBackOfficeEntry(YES);
									String isinFolio[] = isinClientMutualFund.getKey().split("-");
									clientIDFamilyIDISINFolio = String.valueOf(clientID) + "-"
											+ String.valueOf(familyID) + "-" + isinFolio[0] + "-" + isinFolio[1];

									if (clientIDFamilyIDISINClientMutualFundListMap != null
											&& !clientIDFamilyIDISINClientMutualFundListMap.isEmpty()) {
										if (clientIDFamilyIDISINClientMutualFundListMap
												.containsKey(clientIDFamilyIDISINFolio)) {
											ClientMutualFund clMF = new ClientMutualFund();
											clMF = clientIDFamilyIDISINClientMutualFundListMap
													.get(clientIDFamilyIDISINFolio);
											/*******************************************/
											double investmentAmount = clientMF.getInvestmentAmount().doubleValue()
													+ clMF.getInvestmentAmount().doubleValue();
											if (clientMF.getInvestmentStartDate()
													.after(clMF.getInvestmentStartDate())) {
												clientMF.setInvestmentStartDate(clMF.getInvestmentStartDate());
											}
											double units = clientMF.getLumpsumUnitsPurchased().doubleValue()
													+ clMF.getLumpsumUnitsPurchased().doubleValue();
											double currentValue = clientMF.getCurrentMarketValue().doubleValue()
													+ clMF.getCurrentMarketValue().doubleValue();
											clientMF.setInvestmentAmount(new BigDecimal(investmentAmount));
											int ut = (int) units;
											//System.out.println("UNITS ut: "+ ut );
											if(ut < 0)
												ut = 0;
											clientMF.setLumpsumUnitsPurchased(new BigDecimal(ut));
											// System.out.println("ROUNDED OFF: "+Math.round(currentValue * 100.0) /
											// 100.0);
											clientMF.setCurrentMarketValue(
													new BigDecimal(Math.round(currentValue * 100.0) / 100.0));
											/*******************************************/
											clientIDFamilyIDISINClientMutualFundListMap.put(clientIDFamilyIDISINFolio,
													clientMF);
										} else {
											clientIDFamilyIDISINClientMutualFundListMap.put(clientIDFamilyIDISINFolio,
													clientMF);
										}
									} else {
										clientIDFamilyIDISINClientMutualFundListMap.put(clientIDFamilyIDISINFolio,
												clientMF);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						} else
							log.debug("Investor " + investorName + "-" + investorPAN
									+ " NOT FOUND as client or family. So, automatic AUM calculation is stopped..");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// enter in client mutual fund table

				for (Map.Entry<String, ClientMutualFund> clientIDFamilyIDISINClientMutualFund : clientIDFamilyIDISINClientMutualFundListMap
						.entrySet()) {
					try {
						// System.out.println("Key = " +
						// clientIDFamilyIDISINClientMutualFundList.getKey() + ", Value = " +
						// clientIDFamilyIDISINClientMutualFundList.getValue());
						String clientIDFamilyIDISINClientMutualFundArray[] = clientIDFamilyIDISINClientMutualFund
								.getKey().split("-");

						ClientMutualFund clientMFund = clientMutualFundRepository
								.findByClientMasterAndClientFamilyMemberAndFolioNumberAndIsinAndBackOfficeEntry(
										clientMasterMap.get(clientIDFamilyIDISINClientMutualFundArray[0]),
										clientFamilyMemberMap.get(clientIDFamilyIDISINClientMutualFundArray[1]),
										clientIDFamilyIDISINClientMutualFundArray[3],
										clientIDFamilyIDISINClientMutualFundArray[2], YES);
						if (clientMFund != null) {
							//System.out.println("Started for Client ID: " + clientIDFamilyIDISINClientMutualFundArray[0]+ ", Client Family ID: " + clientIDFamilyIDISINClientMutualFundArray[1]+ " And ISIN: " + clientIDFamilyIDISINClientMutualFundArray[2] + "And Folio: "+ clientIDFamilyIDISINClientMutualFundArray[3]);
							clientIDFamilyIDISINClientMutualFund.getValue().setId(clientMFund.getId());
							clientMutualFundRepository.save(clientIDFamilyIDISINClientMutualFund.getValue());
							log.debug("Done for Client ID: " + clientIDFamilyIDISINClientMutualFundArray[0]
									+ ", Client Family ID: " + clientIDFamilyIDISINClientMutualFundArray[1]
									+ " And ISIN: " + clientIDFamilyIDISINClientMutualFundArray[2] + "And Folio: "
									+ clientIDFamilyIDISINClientMutualFundArray[3]);
						} else {
							// clientIDFamilyIDISINClientMutualFund.getValue().setBackOfficeEntry("Y");
							//System.out.println("Started for Client ID: " + clientIDFamilyIDISINClientMutualFundArray[0]+ ", Client Family ID: " + clientIDFamilyIDISINClientMutualFundArray[1]+ " And ISIN: " + clientIDFamilyIDISINClientMutualFundArray[2] + "And Folio: "+ clientIDFamilyIDISINClientMutualFundArray[3]);
							clientMutualFundRepository.save(clientIDFamilyIDISINClientMutualFund.getValue());
							log.debug("Done for Client ID: " + clientIDFamilyIDISINClientMutualFundArray[0]
									+ ", Client Family ID: " + clientIDFamilyIDISINClientMutualFundArray[1]
									+ " And ISIN: " + clientIDFamilyIDISINClientMutualFundArray[2] + "And Folio: "
									+ clientIDFamilyIDISINClientMutualFundArray[3]);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String formatNumbers(String number) {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
