package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.TransactionReportColumnDTO;
import com.finlabs.finexa.dto.TransactionReportDTO;
import com.finlabs.finexa.dto.TransactionReportDetailedDTOSecondOption;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupTransactionRule;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.LookupTransactionRuleRepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.util.FinexaUtil;
import com.finlabs.finexa.util.XirrTest;

@Service("TransactionReportServiceNew")
@Transactional
public class TransactionReportServiceImplNew implements TransactionReportServiceNew {

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private MasterMFDailyNAVRepository masterMFDailyNAVRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private MasterMutualFundETFRepository etfRepository;

	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepo;
	
	@Autowired
	private LookupTransactionRuleRepository lookupTransactionRuleRepository;

	private static final int UNIT_FLAG_ZERO = 0;
	private static final int UNIT_FLAG_ONE = 1;
	private List<Date> dateForCalculation = new ArrayList<>();
	private List<Double> paymentsForCalculation = new ArrayList<Double>();
	private List<LookupTransactionRule> lookupTransactionRuleList = new ArrayList<LookupTransactionRule>();
	private List<String> lookupTransactionRuleCodeList = new ArrayList<String>();
	private Map<String, String> lookupTransactionRuleMap = new HashMap<>();
	
	@Override
	public List<TransactionReportDetailedDTOSecondOption> transactionReport(TransactionReportDTO transactionReportDTO)
			throws RuntimeException, ParseException {
		SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd/MM/yyyy");
		boolean isSchemeSelected = false;
		String rtaCodeName = "";
		System.out.println("Inside Report............." + transactionReportDTO.getSchemeName());
		List<String> alreadyCountInReport = new ArrayList<String>();

		List<TransactionReportDetailedDTOSecondOption> secondOptionList = new ArrayList<TransactionReportDetailedDTOSecondOption>();

		// TODO Auto-generated method stub
		Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));

		Date fromDate = transactionReportDTO.getFromDate();
		Date toDate = transactionReportDTO.getToDate();
		Map<String, String> clientNamePANMap = new HashMap<String, String>();

		int clientId = transactionReportDTO.getClientId();
		ClientMaster cm = clientMasterRepository.findOne(clientId);
		List<String> clientMasterList = new ArrayList<String>();
		Map<String, String> mainFamilyPanMap = new HashMap<String, String>();
		lookupTransactionRuleList = lookupTransactionRuleRepository.findAll();
		for(LookupTransactionRule lookupTransactionRule : lookupTransactionRuleList) {
			lookupTransactionRuleCodeList.add(lookupTransactionRule.getCode());
			lookupTransactionRuleMap.put(lookupTransactionRule.getCode().trim(), lookupTransactionRule.getDescription().trim());
		}
		if (cm != null) {

			String name = cm.getFirstName() + (cm.getMiddleName() == null ? "" : (" " + cm.getMiddleName()))
					+ (cm.getLastName() == null ? "" : " " + (cm.getLastName()));
			String pan = cm.getPan();
			clientMasterList.add(name + "-" + pan);
			mainFamilyPanMap.put(pan, name);
			clientNamePANMap.put(name.trim() + "-" + pan.trim(), name.trim());
			List<AuxillaryInvestorMaster> auxList = cm.getAuxillaryInvestorMasters();
			for (AuxillaryInvestorMaster obj : auxList) {
				String auxName = obj.getFirstName() + (obj.getMiddleName() == null ? "" : (" " + obj.getMiddleName()))
						+ (obj.getLastName() == null ? "" : (" " + obj.getLastName()));
				String pan1 = obj.getPan();
				if (!clientMasterList.contains(auxName + "-" + pan1)) {
					clientMasterList.add(auxName + "-" + pan1);
				}
				clientNamePANMap.put(auxName.trim() + "-" + pan1.trim(), name.trim());
			}

			List<ClientFamilyMember> familyMemberList = cm.getClientFamilyMembers();
			for (ClientFamilyMember obj : familyMemberList) {
				if (Arrays.asList(transactionReportDTO.getFamilyMemberIdArr()).contains(obj.getId())) {
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
							mainFamilyPanMap.put(pan1, auxName);
							clientNamePANMap.put(auxName.trim() + "-" + pan1.trim(), auxName.trim());
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
								clientNamePANMap.put(auxNameFam.trim() + "-" + panFam.trim(), auxName.trim());
							}
						}
					}
				}
			}
		}
		for (Map.Entry<String, String> entry : clientNamePANMap.entrySet())
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		if (transactionReportDTO.getSchemeName() != null
				&& !transactionReportDTO.getSchemeName().equalsIgnoreCase("null")) {
			List<SchemeIsinMasterBO> schemeIsinMasterBOList = schemeIsinMasterBORepo
					.findByIsinAndStatus(transactionReportDTO.getSchemeName(), "NEW");
			rtaCodeName = schemeIsinMasterBOList.get(0).getCamsCode();
			isSchemeSelected = true;
		}

		Map<String, String> rtaCodeIsinMap = new HashMap<String, String>();
		List<String> isinNotFoundList = new ArrayList<String>();
		try {

			for (String invName : clientMasterList) {
				try {
					// out.println("For Client" + invName);
					Map<String, List<TransactionReportColumnDTO>> transactionListHashMap = new ConcurrentHashMap<String, List<TransactionReportColumnDTO>>();

					List<String> folioSchemeRTACode = new ArrayList<String>();
					String investorName = invName.substring(0, invName.indexOf('-'));
					String investorPan = invName.substring(invName.indexOf('-') + 1);

					if (!alreadyCountInReport.contains(investorName.toLowerCase() + investorPan)) {
						// Returns the list of DISTINCT set of Folio Number and Scheme Name

						List<TransactionMasterBO> transMasterBOList = transactionMasterBORepository
								.findByInvestorNameAndInvestorPanOrderByTransactionDate(investorName, investorPan);

						System.out.println("**********************transMasterBOList" + transMasterBOList.size());
						// store All Data From Table and Prepare a hashmap

						for (TransactionMasterBO transObj : transMasterBOList) {
							try {
								System.out.println("Inside loop of transMasterBOList.............");
								if (isSchemeSelected == true) {
									System.out.println("Inside if 1.............");
									if (transObj.getSchemeRTACode() != null
											&& !transObj.getSchemeRTACode().equals(rtaCodeName)) {
										System.out.println("Inside if 2.............");
										continue;
									}
								}

								String folioNo = transObj.getFolioNo();
								String rtaCode = transObj.getSchemeRTACode();
								String jointNameForFolioNoAndRtaCode = folioNo;
								String jointNameForFolioNoAndRtaCodeCopy = folioNo + "-" + rtaCode;
								if (!folioSchemeRTACode.contains(jointNameForFolioNoAndRtaCodeCopy)) {
									System.out.println("Inside if 3.............");
									folioSchemeRTACode.add(jointNameForFolioNoAndRtaCodeCopy);
								}
								System.out.println("Outside if 3.............");
								if (isinNotFoundList.contains(rtaCode)) {
									System.out.println("Inside if 4.............");
									continue;
								}
								System.out.println("Outside if 4.............");
								// Obtain isin For this rtaCode
								if (rtaCodeIsinMap != null && !rtaCodeIsinMap.containsKey(rtaCode)) {
									System.out.println("Inside null checking of rtaCodeIsinMap.............");
									List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo
											.findByCamsCodeAndStatus(rtaCode, "NEW");
									System.out.println("After fetching SchemeIsinMasterBOList.............");
									if (isinMasterBO.size() > 0) {
										System.out.println("Inside if 5.............");
										String isin = isinMasterBO.get(0).getIsin();
										MasterMutualFundETF mutualFundETF = etfRepository.findOne(isin);
										if(mutualFundETF == null) {
											Log.debug("MASTER MUTUAL FUND ETF NOT FOUND FOR ISIN: "+isin);
											continue;
										}
										System.out
												.println(jointNameForFolioNoAndRtaCode + mutualFundETF.getSchemeName());
										jointNameForFolioNoAndRtaCode = jointNameForFolioNoAndRtaCode + "-"
												+ mutualFundETF.getSchemeName();

										if (!rtaCodeIsinMap.containsKey(rtaCode)) {
											System.out.println("Inside if 6.............");
											rtaCodeIsinMap.put(rtaCode, isin);
										}
									} else {
										System.out.println("Inside else 6.............");
										isinNotFoundList.add(rtaCode);
										continue;
									}
								} else {
									System.out.println("Inside else 5.............");
									MasterMutualFundETF mutualFundETF = etfRepository
											.findOne(rtaCodeIsinMap.get(rtaCode));
									jointNameForFolioNoAndRtaCode = jointNameForFolioNoAndRtaCode + "-"
											+ mutualFundETF.getSchemeName();
								}
								System.out.println("outside else 5.............");
								TransactionReportColumnDTO reportDTO = new TransactionReportColumnDTO();
								if (mainFamilyPanMap.containsKey(investorPan)) {
									reportDTO.setClientDetails(mainFamilyPanMap.get(investorPan) + "-" + investorPan);
									System.out.println("inside if 6.............");
								} else {
									System.out.println("inside else 6.............");
									reportDTO.setClientDetails(invName);
								}
								System.out.println("outside else 6.............");
								reportDTO.setFolioNo(folioNo);
								reportDTO.setSchemeRTACode(rtaCode);
								reportDTO.setFolioDetails(jointNameForFolioNoAndRtaCode);
								reportDTO.setTransDateInDateFormat(transObj.getTransactionDate());
								if(transObj.getTransactionDescription() != null) {
									if(lookupTransactionRuleCodeList.contains(transObj.getTransactionDescription())) {
										reportDTO.setTransactionType(lookupTransactionRuleMap.get(transObj.getTransactionDescription()));
									} else {
										reportDTO.setTransactionType(transObj.getTransactionDescription());
									}
								} else {
									reportDTO.setTransactionType(transObj.getLookupTransactionRule().getDescription());
								}
															
								reportDTO.setTransAmt(transObj.getTransAmt());
								reportDTO.setTransUnits(transObj.getTransUnits());
								reportDTO.setTransTypeCode(transObj.getLookupTransactionRule().getCode());
								reportDTO.setTransactionDate(formatterDisplay.format(transObj.getTransactionDate()));
								reportDTO.setNav(transObj.getNav());
								reportDTO.setRunningTotal("0.0");
								if (!transactionListHashMap.containsKey(jointNameForFolioNoAndRtaCode)) {
									System.out.println("inside if 7.............");
									List<TransactionReportColumnDTO> reportList = new ArrayList<TransactionReportColumnDTO>();
									reportList.add(reportDTO);
									transactionListHashMap.put(jointNameForFolioNoAndRtaCode, reportList);
								} else {
									System.out.println("inside else 7.............");
									List<TransactionReportColumnDTO> reportList = transactionListHashMap
											.get(jointNameForFolioNoAndRtaCode);
									reportList.add(reportDTO);
									transactionListHashMap.put(jointNameForFolioNoAndRtaCode, reportList);
								}
								alreadyCountInReport.add(investorName.toLowerCase().trim() + investorPan);
								System.out.println("outside else 6.............");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}
					for (Map.Entry<String, List<TransactionReportColumnDTO>> entry : transactionListHashMap
							.entrySet()) {
						try {
							System.out.println("transactionListHashMap.............");
							List<TransactionReportColumnDTO> reportList = entry.getValue();
							Comparator<TransactionReportColumnDTO> compareByDate = (TransactionReportColumnDTO o1,
									TransactionReportColumnDTO o2) -> o1.getTransDateInDateFormat()
											.compareTo(o2.getTransDateInDateFormat());
							Collections.sort(reportList, compareByDate);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// out.println(transactionListHashMap.size());
					// out.println(transactionListHashMap.keySet());
					// out.println(folioSchemeRTACode.toString());

					for (Map.Entry<String, List<TransactionReportColumnDTO>> entry : transactionListHashMap
							.entrySet()) {
						try {
							System.out.println("Inside Calculation List");

							TransactionReportDetailedDTOSecondOption mainDTOObj = new TransactionReportDetailedDTOSecondOption();
							Map<String, List<TransactionReportColumnDTO>> mainObjDTO = new ConcurrentHashMap<String, List<TransactionReportColumnDTO>>();

							Double purchaseAndSIP = 0.0d;
							Double switchInAndSTPIn = 0.0d;
							Double dividendReinvestment = 0.0d;
							Double redemptions = 0.0d;
							Double switchOutAndSTPOut = 0.0d;
							Double dividendPayout = 0.0d;
							String invStartDate = null;
							int firstTimeFlag = 0;
							double diffCost = 0.0d;
							double openingBalance = 0.0d, runningTotal = 0.0d, closingTotal = 0.0d;

							List<TransactionReportColumnDTO> manipulationList = new CopyOnWriteArrayList<TransactionReportColumnDTO>();
							List<TransactionReportColumnDTO> copyOfList = new ArrayList<TransactionReportColumnDTO>();
							manipulationList.addAll(entry.getValue());
							String folioNo = manipulationList.get(0).getFolioNo();
							String rtaCode = manipulationList.get(0).getSchemeRTACode();
							if(rtaCode.equals("120USIG")) {
								System.out.println(rtaCode);
							}
							// String clientDetails = manipulationList.get(0).getClientDetails();
							String clientDetails = clientNamePANMap.get(invName);
							String folioDetails = manipulationList.get(0).getFolioDetails();
							System.out.println("ClientDetails" + clientDetails);

							TransactionReportColumnDTO openingDTO = new TransactionReportColumnDTO();
							dateForCalculation.clear();
							paymentsForCalculation.clear();
							int copyOfListIndex = 0;
							int valueToBeConsidered = 0;
							for (int index = 0; index < manipulationList.size(); index++) {
								try {
									TransactionReportColumnDTO tmbo = manipulationList.get(index);
									if ((tmbo.getTransDateInDateFormat().after(fromDate)
											|| tmbo.getTransDateInDateFormat().equals(fromDate))
											&& (tmbo.getTransDateInDateFormat().before(toDate)
													|| tmbo.getTransDateInDateFormat().equals(toDate))) {
										copyOfList.add(manipulationList.get(index));
										if (invStartDate == null) {
											invStartDate = tmbo.getTransactionDate();
											firstTimeFlag = 1;
										}
										openingDTO.setFolioNo(folioNo);
										openingDTO.setSchemeRTACode(rtaCode);
										openingDTO.setTransactionType("Opening Balance");
										openingDTO.setTransactionDate(formatterDisplay.format(fromDate));
										openingDTO.setRunningTotal("" + round(openingBalance, 2));
										runningTotal = getCalculatedValFromTransactionRule(tmbo.getTransTypeCode(),
												tmbo.getTransUnits(), runningTotal, UNIT_FLAG_ZERO);
										// System.out.println("RUNNING TOTAL BEFORE: "+runningTotal);
										if (runningTotal <= 0) {
											runningTotal = 0.00;
										}
										// System.out.println("RUNNING TOTAL AFTER: "+runningTotal);
										closingTotal = runningTotal;
										copyOfList.get(copyOfListIndex).setRunningTotal("" + round(runningTotal, 2));
										if (!tmbo.getTransTypeCode().equals("DR")
												&& !tmbo.getTransTypeCode().equals("DIR")
												&& !tmbo.getTransTypeCode().equals("Dividend Reinvestment")) {
											dateForCalculation.add(tmbo.getTransDateInDateFormat());
										}
										getCalculatedValFromTransactionRule(tmbo.getTransTypeCode(), tmbo.getTransAmt(),
												0.0, UNIT_FLAG_ONE);
										purchaseAndSIP = getPurchasesAndSIP(tmbo.getTransTypeCode(),
												String.valueOf((Double.parseDouble(tmbo.getTransUnits())
														* (Double.parseDouble(tmbo.getNav())))),
												purchaseAndSIP);
										switchInAndSTPIn = getSwitchInsAndSTPIns(tmbo.getTransTypeCode(),
												String.valueOf((Double.parseDouble(tmbo.getTransUnits())
														* (Double.parseDouble(tmbo.getNav())))),
												switchInAndSTPIn);
										dividendReinvestment = getDividendReinvestment(tmbo.getTransTypeCode(),
												String.valueOf((Double.parseDouble(tmbo.getTransUnits())
														* (Double.parseDouble(tmbo.getNav())))),
												dividendReinvestment);
										redemptions = getRedemptionsAndSWP(tmbo.getTransTypeCode(),
												String.valueOf((Double.parseDouble(tmbo.getTransUnits())
														* (Double.parseDouble(tmbo.getNav())))),
												redemptions);
										switchOutAndSTPOut = getSwitchOutAndSTPOut(tmbo.getTransTypeCode(),
												String.valueOf((Double.parseDouble(tmbo.getTransUnits())
														* (Double.parseDouble(tmbo.getNav())))),
												switchOutAndSTPOut);
										dividendPayout = getDividendPayouts(tmbo.getTransTypeCode(), tmbo.getTransAmt(),
												dividendPayout);
										copyOfList.get(copyOfListIndex)
												.setNav(numberFormatter
														.format(new BigDecimal(FinexaUtil.roundOff(Double.parseDouble(
																copyOfList.get(copyOfListIndex).getNav()), 3)))
														.replace("\u20B9", ""));
										copyOfList.get(copyOfListIndex).setTransUnits("" + round(
												Double.parseDouble(copyOfList.get(copyOfListIndex).getTransUnits()),
												2));
										copyOfList.get(copyOfListIndex).setTransAmt(numberFormatter
												.format(Math.round(Double
														.parseDouble(copyOfList.get(copyOfListIndex).getTransAmt())))
												.replace("\u20B9", "")
												.substring(0, numberFormatter
														.format(new BigDecimal(Math.round(Double.parseDouble(
																copyOfList.get(copyOfListIndex).getTransAmt()))))
														.replace("\u20B9", "").length() - 3));
										copyOfListIndex++;

										valueToBeConsidered = 1;
									} else {
										if (firstTimeFlag == 0) {
											invStartDate = tmbo.getTransactionDate();
											firstTimeFlag = 1;
										}
										openingBalance = getCalculatedValFromTransactionRule(tmbo.getTransTypeCode(),
												tmbo.getTransUnits(), openingBalance, UNIT_FLAG_ZERO);
										runningTotal = openingBalance;
										// manipulationList.remove(tmbo);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if (valueToBeConsidered == 0) {
								continue;
							}
							// out.println("Opening Balance for "+ entry.getKey() + "-" + rtaCode + " is " +
							// openingBalance);
							// out.println("Running Total for "+ entry.getKey() + " is " + runningTotal);
							// out.println("Client Details" + manipulationList.get(0).getClientDetails());

							manipulationList.add(0, openingDTO);
							copyOfList.add(0, openingDTO);

							manipulationList.add(0, new TransactionReportColumnDTO());
							copyOfList.add(0, new TransactionReportColumnDTO());

							TransactionReportColumnDTO reportDTO = new TransactionReportColumnDTO();
							if (mainFamilyPanMap.containsKey(investorPan)) {
								reportDTO.setClientDetails(mainFamilyPanMap.get(investorPan) + "-" + investorPan);
							} else {
								reportDTO.setClientDetails(invName);
							}
							reportDTO.setFolioNo(folioNo);
							reportDTO.setSchemeRTACode(rtaCode);
							reportDTO.setFolioDetails(folioDetails);
							reportDTO.setTransactionDate(formatterDisplay.format(toDate));
							reportDTO.setTransactionType("Closing Balance");
							reportDTO.setTransUnits("" + round(closingTotal, 2));
							reportDTO.setRunningTotal("" + round(closingTotal, 2));
							System.out.println("CLOSING_TOTAL:" + closingTotal);

							/*
							 * List<GainCalculator> list = gc.getTransationDetails(investorName, folioNo,
							 * rtaCode, fromDate, toDate); for (GainCalculator gainCalculator : list) {
							 * diffCost += (gainCalculator.getBuyQuantity()) * (gainCalculator.getRate()); }
							 */

							diffCost = purchaseAndSIP + switchInAndSTPIn;

							String isin = rtaCodeIsinMap.get(rtaCode);
							Date maxdate = masterMFDailyNAVRepository.findMaxDate(isin, toDate);
							MasterMFDailyNAV dailyNav = masterMFDailyNAVRepository.findNAV(isin, maxdate);
							System.out.println(maxdate + "NAV:" + dailyNav.getNav() + toDate);
							double presentNav = 10.00;
							if (dailyNav != null) {
								presentNav = dailyNav.getNav();
								if (presentNav == 0.0) {
									// out.println("Nav Val zero for date " + isinMaxDateMap.get(isin));
									presentNav = 10.00;
								}
							} else {
								presentNav = 10.00;
							}
							double currentMarketVal = closingTotal * presentNav;

							reportDTO.setNav(numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(presentNav, 3)))
									.replace("\u20B9", ""));
							reportDTO
									.setTransAmt(
											numberFormatter.format(Math.round(currentMarketVal)).replace("\u20B9", "")
													.substring(0, numberFormatter
															.format(new BigDecimal(Math.round(currentMarketVal)))
															.replace("\u20B9", "").length() - 3));

							manipulationList.add(reportDTO);
							copyOfList.add(reportDTO);

							paymentsForCalculation.add(-currentMarketVal);
							dateForCalculation.add(toDate);
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
								try {
									String dateString = sdf.format(dates);
									Date dt = sdf.parse(dateString);
									date[j] = dt;
									j++;
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							System.out.println(paymentsForCalculation.toString());
							System.out.println(dateForCalculation.toString());
							XirrTest xirrCalculation = new XirrTest();

							double xirr = xirrCalculation.findXirr(payments, date);

							mainDTOObj.setFirstInvestmentDate(invStartDate);
							mainDTOObj.setClientDetails(clientDetails);
							mainDTOObj.setFolioDetails(folioDetails);
							System.out.println(manipulationList.get(0).getClientDetails());
							System.out.println(manipulationList.get(0).getFolioDetails());
							mainDTOObj.setPurchaseAndSIP(
									numberFormatter.format(Math.round(purchaseAndSIP)).replace("\u20B9", "").substring(
											0, numberFormatter.format(new BigDecimal(Math.round(purchaseAndSIP)))
													.replace("\u20B9", "").length() - 3));
							mainDTOObj
									.setSwitchInAndSIP(
											numberFormatter.format(Math.round(switchInAndSTPIn)).replace("\u20B9", "")
													.substring(0, numberFormatter
															.format(new BigDecimal(Math.round(switchInAndSTPIn)))
															.replace("\u20B9", "").length() - 3));
							mainDTOObj.setDividendReinvestment(numberFormatter.format(Math.round(dividendReinvestment))
									.replace("\u20B9", "").substring(0,
											numberFormatter.format(new BigDecimal(Math.round(dividendReinvestment)))
													.replace("\u20B9", "").length() - 3));
							mainDTOObj.setRedemptionsAndSWP(
									numberFormatter.format(Math.round(redemptions)).replace("\u20B9", "").substring(0,
											numberFormatter.format(new BigDecimal(Math.round(redemptions)))
													.replace("\u20B9", "").length() - 3));
							mainDTOObj
									.setSwitchOutAndSTPOut(
											numberFormatter.format(Math.round(switchOutAndSTPOut)).replace("\u20B9", "")
													.substring(0, numberFormatter
															.format(new BigDecimal(Math.round(switchOutAndSTPOut)))
															.replace("\u20B9", "").length() - 3));
							mainDTOObj.setDividentPayouts(
									numberFormatter.format(Math.round(dividendPayout)).replace("\u20B9", "").substring(
											0, numberFormatter.format(new BigDecimal(Math.round(dividendPayout)))
													.replace("\u20B9", "").length() - 3));
							mainDTOObj.setUnitsHeld(
									numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(closingTotal, 3)))
											.replace("\u20B9", ""));
							mainDTOObj.setCostOfInvestment(
									numberFormatter.format(Math.round(diffCost)).replace("\u20B9", "").substring(0,
											numberFormatter.format(new BigDecimal(Math.round(diffCost)))
													.replace("\u20B9", "").length() - 3));
							mainDTOObj.setPresentNAV(numberFormatter
									.format(new BigDecimal(FinexaUtil.roundOff(presentNav, 3))).replace("\u20B9", ""));
							mainDTOObj
									.setMarketValue(
											numberFormatter.format(Math.round(currentMarketVal)).replace("\u20B9", "")
													.substring(0, numberFormatter
															.format(new BigDecimal(Math.round(currentMarketVal)))
															.replace("\u20B9", "").length() - 3));
							try {
								mainDTOObj.setXirr(numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(xirr, 3)))
										.replace("\u20B9", ""));

							} catch (Exception e) {
								mainDTOObj.setXirr("0.0");
							}
							manipulationList.add(0, new TransactionReportColumnDTO());
							mainObjDTO.put(entry.getKey(), copyOfList);
							mainDTOObj.setMainReportMap(mainObjDTO);
							secondOptionList.add(mainDTOObj);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// out.close();
		// System.out.println("summary map size: " +
		// transactionReportDetailedDTO.getSummaryMap().size());
		for (TransactionReportDetailedDTOSecondOption obj : secondOptionList) {
			Map<String, List<TransactionReportColumnDTO>> mainReportMap = obj.getMainReportMap();
			for (Map.Entry<String, List<TransactionReportColumnDTO>> entry : mainReportMap.entrySet()) {
				System.out.println("For Key" + entry.getKey());
				for (TransactionReportColumnDTO obj2 : entry.getValue()) {
					System.out.println(obj2);
				}

			}

		}

		for (String invName : clientMasterList) {

			// out.println("For Client" + invName);
			Map<String, List<TransactionReportColumnDTO>> transactionListHashMap = new ConcurrentHashMap<String, List<TransactionReportColumnDTO>>();

			List<String> folioSchemeRTACode = new ArrayList<String>();
			String investorName = invName.substring(0, invName.indexOf('-'));
			String investorPan = invName.substring(invName.indexOf('-') + 1);

			if (!alreadyCountInReport.contains(investorName.toLowerCase() + investorPan)) {
				// Returns the list of DISTINCT set of Folio Number and Scheme Name

				List<TransactionMasterBO> transMasterBOList = transactionMasterBORepository
						.findByInvestorNameAndInvestorPanOrderByTransactionDate(investorName, investorPan);

				System.out.println("**********************transMasterBOList" + transMasterBOList.size());
				// store All Data From Table and Prepare a hashmap

				for (TransactionMasterBO transObj : transMasterBOList) {

					if (isSchemeSelected == true) {
						if (transObj.getSchemeRTACode() != null && !transObj.getSchemeRTACode().equals(rtaCodeName)) {
							continue;
						}
					}

					String folioNo = transObj.getFolioNo();
					String rtaCode = transObj.getSchemeRTACode();
					String jointNameForFolioNoAndRtaCode = folioNo;
					String jointNameForFolioNoAndRtaCodeCopy = folioNo + "-" + rtaCode;
					if (!folioSchemeRTACode.contains(jointNameForFolioNoAndRtaCodeCopy)) {
						folioSchemeRTACode.add(jointNameForFolioNoAndRtaCodeCopy);
					}
					if (isinNotFoundList.contains(rtaCode)) {
						continue;
					}
					// Obtain isin For this rtaCode
					if (rtaCodeIsinMap != null && !rtaCodeIsinMap.containsKey(rtaCode)) {
						List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo.findByCamsCodeAndStatus(rtaCode,
								"NEW");
						if (isinMasterBO.size() > 0) {
							String isin = isinMasterBO.get(0).getIsin();
							MasterMutualFundETF mutualFundETF = etfRepository.findOne(isin);
							jointNameForFolioNoAndRtaCode = jointNameForFolioNoAndRtaCode + "-"
									+ mutualFundETF.getSchemeName();

							if (!rtaCodeIsinMap.containsKey(rtaCode)) {
								rtaCodeIsinMap.put(rtaCode, isin);
							}
						} else {
							isinNotFoundList.add(rtaCode);
							continue;
						}
					} else {
						MasterMutualFundETF mutualFundETF = etfRepository.findOne(rtaCodeIsinMap.get(rtaCode));
						jointNameForFolioNoAndRtaCode = jointNameForFolioNoAndRtaCode + "-"
								+ mutualFundETF.getSchemeName();
					}
					TransactionReportColumnDTO reportDTO = new TransactionReportColumnDTO();
					if (mainFamilyPanMap.containsKey(investorPan)) {
						reportDTO.setClientDetails(mainFamilyPanMap.get(investorPan) + "-" + investorPan);
					} else {
						reportDTO.setClientDetails(invName);
					}
					reportDTO.setFolioNo(folioNo);
					reportDTO.setSchemeRTACode(rtaCode);
					reportDTO.setFolioDetails(jointNameForFolioNoAndRtaCode);
					reportDTO.setTransDateInDateFormat(transObj.getTransactionDate());
					reportDTO.setTransactionType(transObj.getLookupTransactionRule().getDescription());
					reportDTO.setTransAmt(transObj.getTransAmt());
					reportDTO.setTransUnits(transObj.getTransUnits());
					reportDTO.setTransTypeCode(transObj.getLookupTransactionRule().getCode());
					reportDTO.setTransactionDate(formatterDisplay.format(transObj.getTransactionDate()));
					reportDTO.setNav(transObj.getNav());
					reportDTO.setRunningTotal("0.0");
					if (!transactionListHashMap.containsKey(jointNameForFolioNoAndRtaCode)) {
						List<TransactionReportColumnDTO> reportList = new ArrayList<TransactionReportColumnDTO>();
						reportList.add(reportDTO);
						transactionListHashMap.put(jointNameForFolioNoAndRtaCode, reportList);
					} else {
						List<TransactionReportColumnDTO> reportList = transactionListHashMap
								.get(jointNameForFolioNoAndRtaCode);
						reportList.add(reportDTO);
						transactionListHashMap.put(jointNameForFolioNoAndRtaCode, reportList);
					}
					alreadyCountInReport.add(investorName.toLowerCase().trim() + investorPan);

				}

			}
			for (Map.Entry<String, List<TransactionReportColumnDTO>> entry : transactionListHashMap.entrySet()) {
				List<TransactionReportColumnDTO> reportList = entry.getValue();
				Comparator<TransactionReportColumnDTO> compareByDate = (TransactionReportColumnDTO o1,
						TransactionReportColumnDTO o2) -> o1.getTransDateInDateFormat()
								.compareTo(o2.getTransDateInDateFormat());
				Collections.sort(reportList, compareByDate);
			}
			// out.println(transactionListHashMap.size());
			// out.println(transactionListHashMap.keySet());
			// out.println(folioSchemeRTACode.toString());

			for (Map.Entry<String, List<TransactionReportColumnDTO>> entry : transactionListHashMap.entrySet()) {

				TransactionReportDetailedDTOSecondOption mainDTOObj = new TransactionReportDetailedDTOSecondOption();
				Map<String, List<TransactionReportColumnDTO>> mainObjDTO = new ConcurrentHashMap<String, List<TransactionReportColumnDTO>>();

				Double purchaseAndSIP = 0.0d;
				Double switchInAndSTPIn = 0.0d;
				Double dividendReinvestment = 0.0d;
				Double redemptions = 0.0d;
				Double switchOutAndSTPOut = 0.0d;
				Double dividendPayout = 0.0d;
				String invStartDate = null;
				int firstTimeFlag = 0;
				double diffCost = 0.0d;
				double openingBalance = 0.0d, runningTotal = 0.0d, closingTotal = 0.0d;

				List<TransactionReportColumnDTO> manipulationList = new CopyOnWriteArrayList<TransactionReportColumnDTO>();
				List<TransactionReportColumnDTO> copyOfList = new ArrayList<TransactionReportColumnDTO>();
				manipulationList.addAll(entry.getValue());
				String folioNo = manipulationList.get(0).getFolioNo();
				String rtaCode = manipulationList.get(0).getSchemeRTACode();
				// String clientDetails = manipulationList.get(0).getClientDetails();
				String clientDetails = clientNamePANMap.get(invName);
				String folioDetails = manipulationList.get(0).getFolioDetails();
				System.out.println("ClientDetails" + clientDetails);

				TransactionReportColumnDTO openingDTO = new TransactionReportColumnDTO();
				dateForCalculation.clear();
				paymentsForCalculation.clear();
				int copyOfListIndex = 0;
				int valueToBeConsidered = 0;
				for (int index = 0; index < manipulationList.size(); index++) {
					TransactionReportColumnDTO tmbo = manipulationList.get(index);
					if ((tmbo.getTransDateInDateFormat().after(fromDate)
							|| tmbo.getTransDateInDateFormat().equals(fromDate))
							&& (tmbo.getTransDateInDateFormat().before(toDate)
									|| tmbo.getTransDateInDateFormat().equals(toDate))) {
						copyOfList.add(manipulationList.get(index));
						if (invStartDate == null) {
							invStartDate = tmbo.getTransactionDate();
							firstTimeFlag = 1;
						}
						openingDTO.setFolioNo(folioNo);
						openingDTO.setSchemeRTACode(rtaCode);
						openingDTO.setTransactionType("Opening Balance");
						openingDTO.setTransactionDate(formatterDisplay.format(fromDate));
						openingDTO.setRunningTotal("" + round(openingBalance, 2));
						runningTotal = getCalculatedValFromTransactionRule(tmbo.getTransTypeCode(),
								tmbo.getTransUnits(), runningTotal, UNIT_FLAG_ZERO);
						// System.out.println("RUNNING TOTAL BEFORE: "+runningTotal);
						if (runningTotal <= 0) {
							runningTotal = 0.00;
						}
						// System.out.println("RUNNING TOTAL AFTER: "+runningTotal);
						closingTotal = runningTotal;
						copyOfList.get(copyOfListIndex).setRunningTotal("" + round(runningTotal, 2));
						if (!tmbo.getTransTypeCode().equals("DR") && !tmbo.getTransTypeCode().equals("DIR")
								&& !tmbo.getTransTypeCode().equals("Dividend Reinvestment")) {
							dateForCalculation.add(tmbo.getTransDateInDateFormat());
						}
						getCalculatedValFromTransactionRule(tmbo.getTransTypeCode(), tmbo.getTransAmt(), 0.0,
								UNIT_FLAG_ONE);
						purchaseAndSIP = getPurchasesAndSIP(tmbo.getTransTypeCode(), String.valueOf(
								(Double.parseDouble(tmbo.getTransUnits()) * (Double.parseDouble(tmbo.getNav())))),
								purchaseAndSIP);
						switchInAndSTPIn = getSwitchInsAndSTPIns(tmbo.getTransTypeCode(), String.valueOf(
								(Double.parseDouble(tmbo.getTransUnits()) * (Double.parseDouble(tmbo.getNav())))),
								switchInAndSTPIn);
						dividendReinvestment = getDividendReinvestment(tmbo.getTransTypeCode(), String.valueOf(
								(Double.parseDouble(tmbo.getTransUnits()) * (Double.parseDouble(tmbo.getNav())))),
								dividendReinvestment);
						redemptions = getRedemptionsAndSWP(tmbo.getTransTypeCode(), String.valueOf(
								(Double.parseDouble(tmbo.getTransUnits()) * (Double.parseDouble(tmbo.getNav())))),
								redemptions);
						switchOutAndSTPOut = getSwitchOutAndSTPOut(tmbo.getTransTypeCode(), String.valueOf(
								(Double.parseDouble(tmbo.getTransUnits()) * (Double.parseDouble(tmbo.getNav())))),
								switchOutAndSTPOut);
						dividendPayout = getDividendPayouts(tmbo.getTransTypeCode(), tmbo.getTransAmt(),
								dividendPayout);
						copyOfList.get(copyOfListIndex)
								.setNav(numberFormatter
										.format(new BigDecimal(FinexaUtil.roundOff(
												Double.parseDouble(copyOfList.get(copyOfListIndex).getNav()), 3)))
										.replace("\u20B9", ""));
						copyOfList.get(copyOfListIndex).setTransUnits(
								"" + round(Double.parseDouble(copyOfList.get(copyOfListIndex).getTransUnits()), 2));
						copyOfList.get(copyOfListIndex).setTransAmt(numberFormatter
								.format(Math.round(Double.parseDouble(copyOfList.get(copyOfListIndex).getTransAmt())))
								.replace("\u20B9", "")
								.substring(0, numberFormatter
										.format(new BigDecimal(Math.round(
												Double.parseDouble(copyOfList.get(copyOfListIndex).getTransAmt()))))
										.replace("\u20B9", "").length() - 3));
						copyOfListIndex++;

						valueToBeConsidered = 1;
					} else {
						if (firstTimeFlag == 0) {
							invStartDate = tmbo.getTransactionDate();
							firstTimeFlag = 1;
						}
						openingBalance = getCalculatedValFromTransactionRule(tmbo.getTransTypeCode(),
								tmbo.getTransUnits(), openingBalance, UNIT_FLAG_ZERO);
						runningTotal = openingBalance;
						// manipulationList.remove(tmbo);
					}
				}
				if (valueToBeConsidered == 0) {
					continue;
				}
				// out.println("Opening Balance for "+ entry.getKey() + "-" + rtaCode + " is " +
				// openingBalance);
				// out.println("Running Total for "+ entry.getKey() + " is " + runningTotal);
				// out.println("Client Details" + manipulationList.get(0).getClientDetails());

				manipulationList.add(0, openingDTO);
				copyOfList.add(0, openingDTO);

				manipulationList.add(0, new TransactionReportColumnDTO());
				copyOfList.add(0, new TransactionReportColumnDTO());

				TransactionReportColumnDTO reportDTO = new TransactionReportColumnDTO();
				if (mainFamilyPanMap.containsKey(investorPan)) {
					reportDTO.setClientDetails(mainFamilyPanMap.get(investorPan) + "-" + investorPan);
				} else {
					reportDTO.setClientDetails(invName);
				}
				reportDTO.setFolioNo(folioNo);
				reportDTO.setSchemeRTACode(rtaCode);
				reportDTO.setFolioDetails(folioDetails);
				reportDTO.setTransactionDate(formatterDisplay.format(toDate));
				reportDTO.setTransactionType("Closing Balance");
				reportDTO.setTransUnits("" + round(closingTotal, 2));
				reportDTO.setRunningTotal("" + round(closingTotal, 2));
				System.out.println("CLOSING_TOTAL:" + closingTotal);

				/*
				 * List<GainCalculator> list = gc.getTransationDetails(investorName, folioNo,
				 * rtaCode, fromDate, toDate); for (GainCalculator gainCalculator : list) {
				 * diffCost += (gainCalculator.getBuyQuantity()) * (gainCalculator.getRate()); }
				 */

				diffCost = purchaseAndSIP + switchInAndSTPIn;

				String isin = rtaCodeIsinMap.get(rtaCode);
				Date maxdate = masterMFDailyNAVRepository.findMaxDate(isin, toDate);
				MasterMFDailyNAV dailyNav = masterMFDailyNAVRepository.findNAV(isin, maxdate);
				System.out.println(maxdate + "NAV:" + dailyNav.getNav() + toDate);
				double presentNav = 10.00;
				if (dailyNav != null) {
					presentNav = dailyNav.getNav();
					if (presentNav == 0.0) {
						// out.println("Nav Val zero for date " + isinMaxDateMap.get(isin));
						presentNav = 10.00;
					}
				} else {
					presentNav = 10.00;
				}
				double currentMarketVal = closingTotal * presentNav;

				reportDTO.setNav(numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(presentNav, 3)))
						.replace("\u20B9", ""));
				reportDTO.setTransAmt(numberFormatter.format(Math.round(currentMarketVal)).replace("\u20B9", "")
						.substring(0, numberFormatter.format(new BigDecimal(Math.round(currentMarketVal)))
								.replace("\u20B9", "").length() - 3));

				manipulationList.add(reportDTO);
				copyOfList.add(reportDTO);

				paymentsForCalculation.add(-currentMarketVal);
				dateForCalculation.add(toDate);
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
				System.out.println(paymentsForCalculation.toString());
				System.out.println(dateForCalculation.toString());
				XirrTest xirrCalculation = new XirrTest();

				double xirr = xirrCalculation.findXirr(payments, date);

				mainDTOObj.setFirstInvestmentDate(invStartDate);
				mainDTOObj.setClientDetails(clientDetails);
				mainDTOObj.setFolioDetails(folioDetails);
				System.out.println(manipulationList.get(0).getClientDetails());
				System.out.println(manipulationList.get(0).getFolioDetails());
				mainDTOObj.setPurchaseAndSIP(numberFormatter.format(Math.round(purchaseAndSIP)).replace("\u20B9", "")
						.substring(0, numberFormatter.format(new BigDecimal(Math.round(purchaseAndSIP)))
								.replace("\u20B9", "").length() - 3));
				mainDTOObj.setSwitchInAndSIP(numberFormatter.format(Math.round(switchInAndSTPIn)).replace("\u20B9", "")
						.substring(0, numberFormatter.format(new BigDecimal(Math.round(switchInAndSTPIn)))
								.replace("\u20B9", "").length() - 3));
				mainDTOObj.setDividendReinvestment(
						numberFormatter.format(Math.round(dividendReinvestment)).replace("\u20B9", "").substring(0,
								numberFormatter.format(new BigDecimal(Math.round(dividendReinvestment)))
										.replace("\u20B9", "").length() - 3));
				mainDTOObj.setRedemptionsAndSWP(numberFormatter.format(Math.round(redemptions)).replace("\u20B9", "")
						.substring(0, numberFormatter.format(new BigDecimal(Math.round(redemptions)))
								.replace("\u20B9", "").length() - 3));
				mainDTOObj.setSwitchOutAndSTPOut(
						numberFormatter.format(Math.round(switchOutAndSTPOut)).replace("\u20B9", "").substring(0,
								numberFormatter.format(new BigDecimal(Math.round(switchOutAndSTPOut)))
										.replace("\u20B9", "").length() - 3));
				mainDTOObj.setDividentPayouts(numberFormatter.format(Math.round(dividendPayout)).replace("\u20B9", "")
						.substring(0, numberFormatter.format(new BigDecimal(Math.round(dividendPayout)))
								.replace("\u20B9", "").length() - 3));
				mainDTOObj.setUnitsHeld(numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(closingTotal, 3)))
						.replace("\u20B9", ""));
				mainDTOObj.setCostOfInvestment(
						numberFormatter.format(Math.round(diffCost)).replace("\u20B9", "").substring(0, numberFormatter
								.format(new BigDecimal(Math.round(diffCost))).replace("\u20B9", "").length() - 3));
				mainDTOObj.setPresentNAV(numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(presentNav, 3)))
						.replace("\u20B9", ""));
				mainDTOObj.setMarketValue(numberFormatter.format(Math.round(currentMarketVal)).replace("\u20B9", "")
						.substring(0, numberFormatter.format(new BigDecimal(Math.round(currentMarketVal)))
								.replace("\u20B9", "").length() - 3));
				try {
					mainDTOObj.setXirr(
							numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(xirr, 3))).replace("\u20B9", ""));

				} catch (Exception e) {
					mainDTOObj.setXirr("0.0");
				}
				manipulationList.add(0, new TransactionReportColumnDTO());
				mainObjDTO.put(entry.getKey(), copyOfList);
				mainDTOObj.setMainReportMap(mainObjDTO);
				secondOptionList.add(mainDTOObj);
			}

		}

		return secondOptionList;
	}

	public String round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		DecimalFormat df = new DecimalFormat("#.00");
		df.setMinimumIntegerDigits(1);
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		String valueString = df.format(value);
		// value = Double.parseDouble(valueString);
		return valueString;
	}

	private double getCalculatedValFromTransactionRule(String transactionType, String valToBeAdded, Double originalVal,
			int unitFlag) {
		// TODO Auto-generated method stub
		double newVal = originalVal;
		if (transactionType != null) {
			switch (transactionType) {
			case "P":
			case "REDR":
			case "SI":
			case "SWIN":
			case "STP In":
			case "STPI":
			case "STPOR":
			case "TI":
			case "DR":
			case "DIR":
			case "Dividend Reinvestment":
			case "New Purchase":
			case "NEWPUR":
			case "SIP":
			case "Additional Purchase":
			case "SWOFR":
			case "ADDPUR":
			case "TMI":
				newVal += Double.parseDouble(valToBeAdded);
				if (!transactionType.equals("DR") && !transactionType.equals("DIR")
						&& !transactionType.equals("Dividend Reinvestment")) {
					if (unitFlag == UNIT_FLAG_ONE) {
						paymentsForCalculation.add(Double.parseDouble(valToBeAdded));
					}
				}
				break;
			case "R":
			case "RED":
			case "Redemption":
			case "STPIR":
			case "STP O":
			case "STP Out":
			case "SWINR":
			case "SO":
			case "SWOF":
			case "TO":
			case "SIPR":
			case "ADDPURR":
			case "SWD":
			case "NEWPURR":
			case "TMO":
				newVal -= Double.parseDouble(valToBeAdded);
				if (unitFlag == UNIT_FLAG_ONE) {
					paymentsForCalculation.add(-Double.parseDouble(valToBeAdded));
				}
				// System.out.println("VALUE_TO_BE_ADDED:"+valToBeAdded);
				// System.out.println("NEW_VAL:"+newVal);
				break;
			case "DP":
			case "J":
			case "All others":
				// noEffect
				if (!transactionType.equals("DP")) {
					if (unitFlag == UNIT_FLAG_ONE) {
						paymentsForCalculation.add(-Double.parseDouble(valToBeAdded));
					}
				}
				break;
			default:
				break;
			}

		}
		return newVal;
	}

	private double getPurchasesAndSIP(String transactionType, String valToBeAdded, Double originalVal) {
		// TODO Auto-generated method stub
		double newVal = originalVal;
		if (transactionType != null) {
			switch (transactionType) {
			case "P":
			case "REDR":
			case "TI":
			case "New Purchase":
			case "NEWPUR":
			case "SIP":
			case "Additional Purchase":
			case "ADDPUR":
			case "TMI":
				newVal += Double.parseDouble(valToBeAdded);
				break;
			default:
				break;
			}

		}
		return newVal;
	}

	private double getSwitchInsAndSTPIns(String transactionType, String valToBeAdded, Double originalVal) {
		// TODO Auto-generated method stub
		double newVal = originalVal;
		if (transactionType != null) {
			switch (transactionType) {
			case "SI":
			case "SWIN":
			case "STP In":
			case "STPI":
			case "STPOR":
			case "SWOFR":
				newVal += Double.parseDouble(valToBeAdded);
				break;
			default:
				break;
			}

		}
		return newVal;
	}

	private double getDividendReinvestment(String transactionType, String valToBeAdded, Double originalVal) {
		// TODO Auto-generated method stub
		double newVal = originalVal;
		if (transactionType != null) {
			switch (transactionType) {
			case "DR":
			case "DIR":
			case "Dividend Reinvestment":
				newVal += Double.parseDouble(valToBeAdded);
				break;
			default:
				break;
			}

		}
		return newVal;
	}

	private double getRedemptionsAndSWP(String transactionType, String valToBeAdded, Double originalVal) {
		// TODO Auto-generated method stub
		double newVal = originalVal;
		if (transactionType != null) {
			switch (transactionType) {
			case "R":
			case "RED":
			case "Redemption":
			case "TO":
			case "SIPR":
			case "ADDPURR":
			case "SWD":
			case "NEWPURR":
			case "TMO":
				newVal += Double.parseDouble(valToBeAdded);
				break;
			default:
				break;
			}

		}
		return newVal;
	}

	private double getSwitchOutAndSTPOut(String transactionType, String valToBeAdded, Double originalVal) {
		// TODO Auto-generated method stub
		double newVal = originalVal;
		if (transactionType != null) {
			switch (transactionType) {
			case "STPIR":
			case "STP O":
			case "STP Out":
			case "SWINR":
			case "SO":
			case "SWOF":
			case "SIPR":
				newVal += Double.parseDouble(valToBeAdded);
				break;
			default:
				break;
			}

		}
		return newVal;
	}

	private double getDividendPayouts(String transactionType, String valToBeAdded, Double originalVal) {
		// TODO Auto-generated method stub
		double newVal = originalVal;
		if (transactionType != null) {
			switch (transactionType) {
			case "DP":
				newVal += Double.parseDouble(valToBeAdded);
				break;
			default:
				break;
			}

		}
		return newVal;
	}
}
