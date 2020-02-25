package com.finlabs.finexa.service;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.CapitalGainsReportColumnDTO;
import com.finlabs.finexa.dto.CapitalGainsReportDTO;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterCostInflationIndex;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.MasterCostInflationIndexRepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.resources.model.GainCalculator;
import com.finlabs.finexa.resources.model.GainLossMapDTO;
import com.finlabs.finexa.resources.service.GainCalculatorBOService;
import com.finlabs.finexa.util.FinexaUtil;
import com.ibm.icu.math.BigDecimal;

@Service("CapitalGainsBOService")
@Transactional
public class CapitalGainBOServiceImpl implements CapitalGainsBOService {

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private MasterMutualFundETFRepository masterMutualFundETFRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private MasterCostInflationIndexRepository costIndexRepo;

	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepo;

	@Autowired
	private MasterMFDailyNAVRepository mfDailyNAVRepo;

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd/MM/yyyy");
	Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));
	private static final String NULL = "null";

	@Override
	public Map<String, List<CapitalGainsReportColumnDTO>> getCapitalGainsReport(
			CapitalGainsReportDTO capitalGainsReportDTO) throws RuntimeException, ParseException,
			InstantiationException, IllegalAccessException, ClassNotFoundException {

		Map<String, String> alreadyCountInReportMap = new HashMap<String, String>();
		String rtaCode, folioNo, fundHouse, fundHouseName, selectedScheme;
		Date fromDate, toDate, firstDate;
		List<String> clientMasterList = new ArrayList<String>();
		Map<String, String> clientNamePANMap = new HashMap<String, String>();
		List<String> distinctSchemeNameList = new ArrayList<String>();
		List<Date> transactionDateList = new ArrayList<Date>();
		Map<String, List<CapitalGainsReportColumnDTO>> portfolioGainLossReportDTOLocal = new HashMap<String, List<CapitalGainsReportColumnDTO>>();

		int clientId = capitalGainsReportDTO.getClientId();
		ClientMaster cm = clientMasterRepository.findOne(clientId);
		Map<String, String> mainFamilyPanMap = new HashMap<String, String>();

		if (cm != null) {
			String name = cm.getFirstName() + (cm.getMiddleName() == null ? "" : (" " + cm.getMiddleName()))
					+ (cm.getLastName() == null ? "" : " " + (cm.getLastName()));
			String pan = cm.getPan();
			mainFamilyPanMap.put(pan, name);
			clientMasterList.add(name + "-" + pan);
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
				if (Arrays.asList(capitalGainsReportDTO.getFamilyMemberIdArr()).contains(obj.getId())) {
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
		// clientMasterList.add("Bhupendra Padmakar Naik-ACAPN4111H");
		for (Map.Entry<String, String> entry : clientNamePANMap.entrySet())
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

		fromDate = capitalGainsReportDTO.getFromDate();
		toDate = capitalGainsReportDTO.getToDate();

		try {
			List<CapitalGainsReportColumnDTO> portfolioGainLossList = new ArrayList<CapitalGainsReportColumnDTO>();

			for (String invName : clientMasterList) {
				List<Object[]> folioSchemeNameList = new ArrayList<Object[]>();

				String investorName = invName.substring(0, invName.indexOf('-'));
				String investorPan = invName.substring(invName.indexOf('-') + 1);
				if (!alreadyCountInReportMap.containsKey(investorName.toLowerCase() + investorPan)) {
					List<String> transactionMasterBO = transactionMasterBORepository
							.getDistinctFolioNoByInvestorName(investorName, investorPan);

					for (String objList : transactionMasterBO) {

						// Gets list of distinct scheme names and folio number of a specific investor
						distinctSchemeNameList = transactionMasterBORepository.getDistinctRTACodeByFolioNoAndDate(
								objList, investorName, investorPan, fromDate, toDate);
						for (String ob : distinctSchemeNameList) {
							Object[] objectList = new Object[2];
							objectList[0] = objList;
							objectList[1] = ob;
							folioSchemeNameList.add(objectList);
						}

					}
				}
				// Iterates to calculate on each set of scheme names and folio number of a
				// specific investor
				for (Object[] ob : folioSchemeNameList) {
					int toBeConsideredFlag = 0;
					try {

						folioNo = (String) ob[0];
						rtaCode = (String) ob[1];
						List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo.findByCamsCodeAndStatus(rtaCode,
								"NEW");
						if (capitalGainsReportDTO.getSchemeName() != null && capitalGainsReportDTO.getSchemeName() != ""
								&& !capitalGainsReportDTO.getSchemeName().equals(NULL)) {
							selectedScheme = capitalGainsReportDTO.getSchemeName();
							String isinNumber = "";
							if (isinMasterBO.size() > 0) {
								isinNumber = isinMasterBO.get(0).getIsin();
							} else {
								System.out.println("SchemeIsin Master No Record found for rtaCode " + rtaCode);
								continue;
							}
							if (isinNumber.equals(capitalGainsReportDTO.getSchemeName())) {
								toBeConsideredFlag = 1;
							}

						} else {
							toBeConsideredFlag = 1;
						}

						if (toBeConsideredFlag == 1) {

							if (isinMasterBO != null && isinMasterBO.size() > 0) {
								String isin = isinMasterBO.get(0).getIsin();

								transactionDateList = transactionMasterBORepository
										.getFromDateByInvestorNamePANFolioScheme(investorName, investorPan, folioNo,
												rtaCode);
								System.out.println(investorName + ", " + investorPan + ", " + folioNo + ", " + rtaCode
										+ ", " + transactionDateList.size());
								firstDate = transactionDateList.get(0);
								System.out.println("ISIN: " + isin);

								// MasterMutualFundETF etf = masterMutualFundETFRepository.findOne(isin);
								// fundHouseName = etf.getFundHouse();
								GainCalculatorBOService gainCalculatorService = new GainCalculatorBOService();
								MasterMutualFundETF mmfETF = masterMutualFundETFRepository.findOne(isin);
								GainCalculator gainLossOutput = gainCalculatorService
										.getGainLossDetailsMap(investorName, folioNo, rtaCode, firstDate, toDate);
								SortedMap<Date, List<GainLossMapDTO>> gainLossMap = gainLossOutput.getGainLossMap();
								for (SortedMap.Entry<Date, List<GainLossMapDTO>> mapKey : gainLossMap.entrySet()) {
									List<GainLossMapDTO> gainLossMapDto = mapKey.getValue();
									int count = 0;
									for (GainLossMapDTO obj : gainLossMapDto) {

										if (obj.getSellTransationDate().after(fromDate)
												|| obj.getSellTransationDate().equals(fromDate)) {
											CapitalGainsReportColumnDTO portfolioDTO = new CapitalGainsReportColumnDTO();
											if (count == 0) {
												portfolioDTO.setTransactionType1(obj.getSellTransactionType());
												SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
												String date = dateFormat.format(obj.getSellTransationDate());
												portfolioDTO.setTransactionDate1("" + date);
												portfolioDTO.setTransactionUnits1(numberFormatter
														.format(FinexaUtil.roundOff(obj.getSoldUnits(), 2))
														.replace("\u20B9", ""));
												portfolioDTO.setNavPerUnit1(
														numberFormatter.format(FinexaUtil.roundOff(obj.getSoldNav(), 2))
																.replace("\u20B9", ""));
												portfolioDTO.setTransAmt1(numberFormatter
														.format(new BigDecimal(Math.round(obj.getSoldAmt())))
														.replace("\u20B9", "")
														.substring(0, numberFormatter
																.format(new BigDecimal(Math.round(obj.getSoldAmt())))
																.replace("\u20B9", "").length() - 3));
												count++;
											}

											portfolioDTO.setTransactionType2(obj.getPurchaseTransactionType());
											SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
											String date = dateFormat.format(obj.getPurchaseTransationDate());
											portfolioDTO.setTransactionDate2(date);
											portfolioDTO.setTransactionUnits2(numberFormatter
													.format(FinexaUtil.roundOff(obj.getPurchaseUnits(), 2))
													.replace("\u20B9", ""));
											portfolioDTO.setNavPerUnit2(
													numberFormatter.format(FinexaUtil.roundOff(obj.getPurchaseNav(), 2))
															.replace("\u20B9", ""));
											portfolioDTO.setTransAmt2(numberFormatter
													.format(new BigDecimal(Math.round(obj.getPurchaseAmt())))
													.replace("\u20B9", "")
													.substring(0, numberFormatter
															.format(new BigDecimal(Math.round(obj.getPurchaseAmt())))
															.replace("\u20B9", "").length() - 3));

											Calendar sDate = Calendar.getInstance();
											Calendar eDateCopy = Calendar.getInstance();
											Calendar eDate = Calendar.getInstance();
											sDate.setTime(obj.getSellTransationDate());
											eDateCopy.setTime(obj.getPurchaseTransationDate());
											eDate.setTime(obj.getPurchaseTransationDate());
											int difInMonths = 0;
											while (eDateCopy.before(sDate)) {
												eDateCopy.add(Calendar.MONTH, 1);
												if (eDateCopy.before(sDate))
													difInMonths++;
											}
											double sellPrice = obj.getSoldAmt();
											if (obj.getPurchaseUnits() < obj.getSoldUnits()) {
												sellPrice = obj.getPurchaseUnits() * obj.getSoldNav();
											}
											if (mmfETF.getLookupAssetClass().getId() == 3) {// Equity
												if (difInMonths >= 12) {
													// Grandfathering clause
													// if purchase is done before 1st February , 2018, then only
													// grandfathering will be applicable
													Calendar grandFatherCal = Calendar.getInstance();
													grandFatherCal.set(Calendar.YEAR, 2018);
													grandFatherCal.set(Calendar.MONTH, 1);
													grandFatherCal.set(Calendar.DAY_OF_MONTH, 1);
													if (eDate.before(grandFatherCal)) {
														// to find 31st Jan Nav
														Calendar lastJanVal = Calendar.getInstance();
														lastJanVal.set(Calendar.YEAR, 2018);
														lastJanVal.set(Calendar.MONTH, 0);
														lastJanVal.set(Calendar.DAY_OF_MONTH, 31);
														System.out.println(lastJanVal.getTime());
														MasterMFDailyNAV mfDailyNav = mfDailyNAVRepo.findNAV(isin,
																lastJanVal.getTime());
														if (mfDailyNav == null) {
															Date dateBefore31stJan = mfDailyNAVRepo.findMaxDate(isin,
																	lastJanVal.getTime());
															mfDailyNav = mfDailyNAVRepo.findNAV(isin,
																	dateBefore31stJan);
														}

														double fairMarketValue = 0.0; // hard coded if after all cases
																						// this is returned as null;
														double navVal = 10.0;
														if (mfDailyNav != null) {
															navVal = mfDailyNav.getNav();
														}
														fairMarketValue = (navVal*obj.getPurchaseUnits());

														double deemedCost = 0.0;
														if (sellPrice > fairMarketValue) {
															deemedCost = higherOf(obj.getPurchaseAmt(),
																	fairMarketValue);
														} else {
															deemedCost = higherOf(obj.getPurchaseAmt(), sellPrice);
														}

														double tax = sellPrice - deemedCost;
														portfolioDTO.setLongTermWithoutIndexation(
																numberFormatter.format(new BigDecimal(Math.round(tax)))
																		.replace("\u20B9", "")
																		.substring(0, numberFormatter
																				.format(new BigDecimal(Math.round(tax)))
																				.replace("\u20B9", "").length() - 3));

														portfolioDTO.setJan312018Units(numberFormatter
																.format(new BigDecimal(
																		FinexaUtil.roundOff(obj.getPurchaseUnits(), 2)))
																.replace("\u20B9", ""));
														portfolioDTO.setJan312018NAV(numberFormatter
																.format(new BigDecimal(
																		FinexaUtil.roundOff(navVal, 2)))
																.replace("\u20B9", ""));
														double marketValOn31stJan = obj.getPurchaseUnits()
																* navVal;
														portfolioDTO.setJan312018MarketValue(numberFormatter
																.format(new BigDecimal(Math.round(marketValOn31stJan)))
																.replace("\u20B9", "").substring(0,
																		numberFormatter
																				.format(new BigDecimal(
																						Math.round(marketValOn31stJan)))
																				.replace("\u20B9", "").length() - 3));

													} else {
														double tax = sellPrice - obj.getPurchaseAmt();
														portfolioDTO.setLongTermWithoutIndexation(
																numberFormatter.format(new BigDecimal(Math.round(tax)))
																		.replace("\u20B9", "")
																		.substring(0, numberFormatter
																				.format(new BigDecimal(Math.round(tax)))
																				.replace("\u20B9", "").length() - 3));
													}
												} else {
													double gainLoss = sellPrice - obj.getPurchaseAmt();
													portfolioDTO.setShortTerm(numberFormatter
															.format(new BigDecimal(Math.round(gainLoss)))
															.replace("\u20B9", "")
															.substring(0, numberFormatter
																	.format(new BigDecimal(Math.round(gainLoss)))
																	.replace("\u20B9", "").length() - 3));
												}
											} else {
												// For Non Equity
												// Find out Long or short term

												if (difInMonths >= 36) {

													// without indexation
													double gainLoss = sellPrice - obj.getPurchaseAmt();
													portfolioDTO.setLongTermWithoutIndexation(numberFormatter
															.format(new BigDecimal(Math.round(gainLoss)))
															.replace("\u20B9", "")
															.substring(0, numberFormatter
																	.format(new BigDecimal(Math.round(gainLoss)))
																	.replace("\u20B9", "").length() - 3));

													// with Indexation
													int sellYear = sDate.get(Calendar.YEAR);
													String sellFinancialYear = "";
													if (sDate.get(Calendar.MONTH) >= 3) {
														sellFinancialYear = sellYear + "-" + (sellYear + 1);
													} else {
														sellFinancialYear = (sellYear - 1) + "-" + (sellYear);
													}

													int purchaseYear = eDate.get(Calendar.YEAR);
													String purchaseFinancialYear = "";
													if (eDate.get(Calendar.MONTH) >= 3) {
														purchaseFinancialYear = purchaseYear + "-" + (purchaseYear + 1);
													} else {
														purchaseFinancialYear = (purchaseYear - 1) + "-"
																+ (purchaseYear);
													}
													MasterCostInflationIndex purchaseIndex = costIndexRepo
															.findByFinancialYear(purchaseFinancialYear);
													MasterCostInflationIndex soldIndex = costIndexRepo
															.findByFinancialYear(sellFinancialYear);

													if (purchaseIndex != null && soldIndex != null) {
														double costOfAquisition = (obj.getPurchaseAmt()
																* soldIndex.getCostInflationIndex())
																/ (double) purchaseIndex.getCostInflationIndex();
														double gainLossIndexed = sellPrice - costOfAquisition;
														portfolioDTO.setLongTermWithIndexation(numberFormatter
																.format(new BigDecimal(Math.round(gainLossIndexed)))
																.replace("\u20B9", "").substring(0,
																		numberFormatter
																				.format(new BigDecimal(
																						Math.round(gainLossIndexed)))
																				.replace("\u20B9", "").length() - 3));
													}

												} else {
													double gainLoss = sellPrice - obj.getPurchaseAmt();
													portfolioDTO.setShortTerm(numberFormatter
															.format(new BigDecimal(Math.round(gainLoss)))
															.replace("\u20B9", "")
															.substring(0, numberFormatter
																	.format(new BigDecimal(Math.round(gainLoss)))
																	.replace("\u20B9", "").length() - 3));
												}
											}

											if (mainFamilyPanMap.containsKey(investorPan)) {

												// portfolioDTO.setClientDetails(mainFamilyPanMap.get(investorPan) +
												// "-"+investorPan);
												portfolioDTO.setClientDetails(
														clientNamePANMap.get(invName) + "-" + investorPan);
											} else {
												portfolioDTO.setClientDetails(invName);
											}

											portfolioDTO.setFolioDetails(folioNo + "-" + mmfETF.getSchemeName());
											alreadyCountInReportMap.put(investorName.toLowerCase() + investorPan,
													folioNo);
											portfolioGainLossList.add(portfolioDTO);
										}

									}
								}
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				folioSchemeNameList.clear();
			}
			if (portfolioGainLossList.size() > 0) {
				portfolioGainLossReportDTOLocal.put(capitalGainsReportDTO.getNameClient(), portfolioGainLossList);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return portfolioGainLossReportDTOLocal;

	}

	private double higherOf(double amountOne, double amountTwo) {
		// TODO Auto-generated method stub
		double value = 0.0;

		if (amountOne > amountTwo) {
			value = amountOne;
		} else {
			value = amountTwo;
		}
		return value;
	}
}
