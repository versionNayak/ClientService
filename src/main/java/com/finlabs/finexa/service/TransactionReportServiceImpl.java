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

import com.finlabs.finexa.dto.TransactionReportColumnDTO;
import com.finlabs.finexa.dto.TransactionReportDTO;
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

@Service("TransactionReportsService")
@Transactional
public class TransactionReportServiceImpl implements TransactionReportService {

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

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd-MM-yyyy");
	private List<Object[]> isinMaxDateList = new ArrayList<Object[]>();
	private Map<String, Date> isinMaxDateMap = new HashMap<String, Date>();
	private List<String> alreadyCountInReport = new ArrayList<String>();
	private static final String NULL = "null";
	@Override
	public Map<String, List<TransactionReportColumnDTO>> transactionReport(TransactionReportDTO transactionReportDTO)
			throws RuntimeException, ParseException {
		
		// TODO Auto-generated method stub
		Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));

		String currentNAV = "";
		String currentValue = "";

		Date fromDate = transactionReportDTO.getFromDate();
		Date toDate = transactionReportDTO.getToDate();

		// Stores each distinct pair of Folio Number and Scheme Name as String and it's
		// list of transactions as List
		Map<String, List<TransactionReportColumnDTO>> transactionsBasedOnFolioAndSchemeMap = new HashMap<String, List<TransactionReportColumnDTO>>();
		List<TransactionReportColumnDTO> transactionList = new ArrayList<TransactionReportColumnDTO>();

		int clientId = transactionReportDTO.getClientId();
		ClientMaster cm = clientMasterRepository.findOne(clientId);
		List<String> clientMasterList = new ArrayList<String>();
		Map<String, String> mainFamilyPanMap = new HashMap<String, String>();

		if (cm != null) {
			
			String name = cm.getFirstName() + (cm.getMiddleName() == null ? "" : (" " + cm.getMiddleName()))
					+ (cm.getLastName() == null ? "" : " " + (cm.getLastName()));
			String pan = cm.getPan();
			clientMasterList.add(name + "-" + pan);
			mainFamilyPanMap.put(pan, name);
			List<AuxillaryInvestorMaster> auxList = cm.getAuxillaryInvestorMasters();
			for (AuxillaryInvestorMaster obj : auxList) {
				String auxName = obj.getFirstName() + (obj.getMiddleName() == null ? "" : (" " + obj.getMiddleName()))
						+ (obj.getLastName() == null ? "" : (" " + obj.getLastName()));
				String pan1 = obj.getPan();
				if (!clientMasterList.contains(auxName + "-" + pan1)) {
					clientMasterList.add(auxName + "-" + pan1);
				}
			}
			
			List<ClientFamilyMember> familyMemberList = cm.getClientFamilyMembers();
			for (ClientFamilyMember obj : familyMemberList) {
				if(Arrays.asList(transactionReportDTO.getFamilyMemberIdArr()).contains(obj.getId())) {
					String auxName = obj.getFirstName() + (obj.getMiddleName() == null ? "" : (" " + obj.getMiddleName()))
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
							}
						}
					}
				}
			}
		}

		
		/*for(Map.Entry<String, String> entryMap : mainFamilyPanMap.entrySet()) {
			System.out.println(entryMap.getKey() + " ----- " + entryMap.getValue());
		}*/
		//System.out.println(clientMasterList);
		//Strores ISIN with it's Max date
		isinMaxDateList = masterMFDailyNAVRepository.findDistinctIsinMaxDateList();
		
		for(Object[] isinMaxDate : isinMaxDateList) {
			isinMaxDateMap.put(isinMaxDate[0].toString(), (Date)isinMaxDate[1]);
		}
		
		try {

			for (String invName : clientMasterList) {
				
				String investorName = invName.substring(0, invName.indexOf('-'));
				String investorPan = invName.substring(invName.indexOf('-') + 1);
				
				if(!alreadyCountInReport.contains(investorName.toLowerCase()+investorPan)) {
				// Returns the list of DISTINCT set of Folio Number and Scheme Name
				List<Object[]> distinctFolioSchemeRTASet = transactionMasterBORepository
						.getDistinctFolioSchemeRTASetByInvestorNameAndTransactionDate(investorName, fromDate, toDate);

				for (Object[] transactionMasterBO : distinctFolioSchemeRTASet) {
					String folioNo = (String) transactionMasterBO[0];
					String rtaCode = (String) transactionMasterBO[1];
					
					int toBeConsideredFlag = 0;
					boolean isOpeningBalanceFound = false;
					List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo.findByCamsCodeAndStatus(rtaCode,"NEW");
					if(isinMasterBO.size() > 0) {
						String isin = isinMasterBO.get(0).getIsin();
						MasterMutualFundETF mutualFundETF = etfRepository.findOne(isin);
						

						if (transactionReportDTO.getSchemeName() != null && 
								transactionReportDTO.getSchemeName() != "" && !transactionReportDTO.getSchemeName().equals(NULL)) {
							if (isin.equals(transactionReportDTO.getSchemeName())) {
								toBeConsideredFlag = 1;
							}
						} else {
							toBeConsideredFlag = 1;
						}
						
						if (toBeConsideredFlag == 1) {

							double runningTotal = 0.0d;
							double transUnits = 0.0d;
							int count = 0, counter = 0;
							
							if (rtaCode.equals("D104"))
								System.out.println(rtaCode);

							// Returns the list of all transactions based on a set of Folio Number and
							// Scheme Name
							List<TransactionMasterBO> transactionsBasedOnFolioAndSchemeList = transactionMasterBORepository
									.findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeRTA(investorName,
											folioNo, rtaCode, fromDate, toDate);
							List<TransactionMasterBO> openingBalance = transactionMasterBORepository.getOpeningBalance(investorName, folioNo, rtaCode, investorPan, fromDate);
							System.out.println("OP BALANCE SIZE:"+openingBalance.size());
							double opBalance = 0.0d;

							TransactionReportColumnDTO openingTransactions = new TransactionReportColumnDTO();
							openingTransactions.setTransactionType("Opening Balance");
							openingTransactions.setTransactionDate(formatterDisplay.format(fromDate));
							openingTransactions.setFolioNo(folioNo);
							if (openingBalance.size() > 0) {
								for (TransactionMasterBO transactionMaster : openingBalance) {
									/*
									if (transactionMaster.getTransactionDate().equals(fromDate) || transactionMaster.getTransactionDate().after(fromDate)) {
										isOpeningBalanceFound = false;
										break;
									}
									*/
									//isOpeningBalanceFound = true;
								
										if(transactionMaster.getLookupTransactionRule() != null) {
											switch (transactionMaster.getLookupTransactionRule().getCode()) {
											case "P":
												opBalance += Double.parseDouble(transactionMaster.getTransUnits());
												break;
											case "R":
												opBalance -= Double.parseDouble(transactionMaster.getTransUnits());
												break;
											case "SI":
												opBalance += Double.parseDouble(transactionMaster.getTransUnits());
												break;
											case "SO":
												opBalance -= Double.parseDouble(transactionMaster.getTransUnits());
												break;
											case "TI":
												opBalance += Double.parseDouble(transactionMaster.getTransUnits());
												break;
											case "TO":
												opBalance -= Double.parseDouble(transactionMaster.getTransUnits());
												break;
											case "DR":
											case "DIR":
											case "Dividend Reinvestment":
												opBalance += Double.parseDouble(transactionMaster.getTransUnits());
												break;
											case "SIP":
												opBalance += Double.parseDouble(transactionMaster.getTransUnits());
												break;
											case "ADDPUR":
												opBalance += Double.parseDouble(transactionMaster.getTransUnits());
												break;
											case "DP":
											case "J":
												break;
											default:
												break;
											}
										//}
									}
									
								} 
								//if (isOpeningBalanceFound == true)
									openingTransactions.setRunningTotal(numberFormatter.format(Math.round(opBalance)).replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(opBalance))).replace("\u20B9", "").length() - 3));
								
							} else {
								openingTransactions.setRunningTotal(String.valueOf(0));
							}
							if (openingTransactions.getRunningTotal() == null)	
								openingTransactions.setRunningTotal(String.valueOf(0));
							// Handling of more than one opening balance
							/*
							if (openingBalance != null && !openingBalance.isEmpty()) {
								for (String op : openingBalance) {
									opBalance += Double.parseDouble(op);
								}
								openingTransactions.setRunningTotal(numberFormatter
										.format(Math.round(opBalance)).replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(opBalance))).replace("\u20B9", "").length() - 3));
							} else {
								openingTransactions.setRunningTotal(String.valueOf(0));
							}
							*/
							openingTransactions.setFolioDetails(folioNo + "-" + mutualFundETF.getSchemeName());
							if (mainFamilyPanMap.containsKey(investorPan)) {
								openingTransactions.setClientDetails(mainFamilyPanMap.get(investorPan) + "-" + investorPan);
							} else {
								openingTransactions.setClientDetails(invName);
							}
							transactionList.add(openingTransactions);
							runningTotal = opBalance;
							for (TransactionMasterBO tmbo : transactionsBasedOnFolioAndSchemeList) {
								try {
									TransactionReportColumnDTO transactions = new TransactionReportColumnDTO();
									transactions.setFolioNo(tmbo.getFolioNo());
									transactions.setFolioDetails(folioNo + "-" + mutualFundETF.getSchemeName());
									transactions.setTransAmt(numberFormatter
											.format(Math.round(Double.parseDouble(tmbo.getTransAmt())))
											.replace("\u20B9", "").substring(0, numberFormatter.format(Math.round(Double.parseDouble(tmbo.getTransAmt()))).replace("\u20B9", "").length() - 3));
									transactions.setTransactionDate(formatterDisplay.format(tmbo.getTransactionDate()));
									transactions.setTransactionType(tmbo.getLookupTransactionRule().getDescription());
									if (tmbo.getNav() != null && !tmbo.getNav().isEmpty()) {
										transactions.setNav(numberFormatter
												.format(new BigDecimal(FinexaUtil.roundOff(Double.parseDouble(tmbo.getNav()), 2)))
												.replace("\u20B9", ""));
									}
									transactions.setTransUnits(numberFormatter
											.format(FinexaUtil.roundOff(Double.parseDouble(tmbo.getTransUnits()), 2))
											.replace("\u20B9", ""));
									transactions.setFromDate(formatterDisplay.format(fromDate));
									transactions.setToDate(formatterDisplay.format(toDate));

									if (mainFamilyPanMap.containsKey(investorPan)) {
										transactions.setClientDetails(mainFamilyPanMap.get(investorPan) + "-" + investorPan);
									} else {
										transactions.setClientDetails(invName);
									}

									transUnits = Double.parseDouble(tmbo.getTransUnits());
									/*
									 * transUnits = FinexaUtil.roundOff(Double.parseDouble(tmbo.getTransUnits()), 2);
									if (count == 0) {
										transactions.setRunningTotal(
												numberFormatter.format(Math.round(transUnits))
														.replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(transUnits))).replace("\u20B9", "").length() - 3));
										runningTotal = transUnits;
										count++;
									} else {*/
										
										if(tmbo.getLookupTransactionRule() != null) {
											switch (tmbo.getLookupTransactionRule().getCode()) {
											case "P":
												runningTotal += transUnits;
												transactions.setRunningTotal(
														numberFormatter.format(Math.round(runningTotal))
																.replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
												break;
											case "R":
												runningTotal -= transUnits;
												transactions.setRunningTotal(
														numberFormatter.format(Math.round(runningTotal))
																.replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
												break;
											case "SI":
												runningTotal += transUnits;
												transactions.setRunningTotal(
														numberFormatter.format(Math.round(runningTotal))
																.replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
												break;
											case "SO":
												runningTotal -= transUnits;
												transactions.setRunningTotal(
														numberFormatter.format(Math.round(runningTotal))
																.replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
												break;
											case "TI":
												runningTotal += transUnits;
												transactions.setRunningTotal(
														numberFormatter.format(Math.round(runningTotal))
																.replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
												break;
											case "TO":
												runningTotal -= transUnits;
												transactions.setRunningTotal(
														numberFormatter.format(Math.round(runningTotal))
																.replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
												break;
											case "DR":
											case "DIR":
											case "Dividend Reinvestment":
												runningTotal += transUnits;
												transactions.setRunningTotal(
														numberFormatter.format(Math.round(runningTotal))
																.replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
												break;
											case "SIP":
												runningTotal += transUnits;
												transactions.setRunningTotal(
														numberFormatter.format(Math.round(runningTotal))
																.replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
												break;
											case "ADDPUR":
												runningTotal += transUnits;
												transactions.setRunningTotal(
														numberFormatter.format(Math.round(runningTotal))
																.replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
												break;
											case "DP":
											case "J":
												// no effect
												transactions.setRunningTotal(
														numberFormatter.format(Math.round(runningTotal))
																.replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
												break;
											default:
												break;
											}
										}
									//}
									/*
									currentValue = numberFormatter
											.format(new BigDecimal(
													FinexaUtil.roundOff(runningTotal * Double.parseDouble(tmbo.getNav()), 3)))
											.replace("\u20B9", "");
									*/
									/*
									 * if (tmbo.getSchemeRTACode() != null && !tmbo.getSchemeRTACode().isEmpty()) {
									 * List<SchemeIsinMasterBO> isinList = schemeIsinMasterBORepo
									 * .findByCamsCodeAndStatus(tmbo.getSchemeRTACode(), "New"); if (isinList.size()
									 * > 0 && isinList != null) { for (SchemeIsinMasterBO asim : isinList) { String
									 * isinFo = asim.getIsin(); // System.out.println("isin: " + isin);
									 * MasterMFDailyNAV dailyNAV = masterMFDailyNAVRepository.findLatestNAV(isinFo);
									 * if (dailyNAV != null) { currentNAV = String.valueOf(FinexaUtil.roundOff(
									 * Double.parseDouble(String.valueOf(dailyNAV.getNav())), 2)); } else {
									 * currentNAV = String
									 * .valueOf(FinexaUtil.roundOff(Double.parseDouble(tmbo.getNav()), 2)); } } }
									 * else { currentNAV = String
									 * .valueOf(FinexaUtil.roundOff(Double.parseDouble(tmbo.getNav()), 2)); } } else
									 * { currentNAV =
									 * String.valueOf(FinexaUtil.roundOff(Double.parseDouble(tmbo.getNav()), 2)); }
									 */
									/*
									List<MasterMFDailyNAV> dailyNAVList = masterMFDailyNAVRepository.findNAVValue(isin);
									MasterMFDailyNAV dailyNAV = dailyNAVList.get(0);
									*/
									MasterMFDailyNAV dailyNAV = masterMFDailyNAVRepository.findNAV(isin, isinMaxDateMap.get(isin));
									if (dailyNAV != null) {
										currentNAV = String.valueOf(FinexaUtil.roundOff((double)dailyNAV.getNav(), 2));
									} else {
										currentNAV = String.valueOf(FinexaUtil.roundOff(0, 2));
									}
									
									
									if(tmbo.getLookupTransactionRule() != null) {
										if(tmbo.getLookupTransactionRule().getCode().equals("DP")) {
											transactions.setNav(" ");
											transactions.setTransUnits("0");
										}
									}
									
									transactionList.add(transactions);

								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							currentValue = numberFormatter.format(Math.round(runningTotal * Double.parseDouble(currentNAV))).replace("\u20B9", "").
									substring(0, numberFormatter.format(Math.round(runningTotal * Double.parseDouble(currentNAV))).replace("\u20B9", "").length() - 3);

							TransactionReportColumnDTO closingTransactions = new TransactionReportColumnDTO();
							closingTransactions.setTransactionType("Closing Balance");
							closingTransactions.setTransactionDate(formatterDisplay.format(toDate));
							closingTransactions.setFolioNo(folioNo);
							closingTransactions.setTransAmt(currentValue);
							closingTransactions.setNav(currentNAV);
							if (mainFamilyPanMap.containsKey(investorPan)) {
								closingTransactions.setClientDetails(mainFamilyPanMap.get(investorPan) + "-" + investorPan);
							} else {
								closingTransactions.setClientDetails(invName);
							}
							closingTransactions.setRunningTotal(numberFormatter
									.format(Math.round(runningTotal)).replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
							closingTransactions.setFolioDetails(folioNo + "-" + mutualFundETF.getSchemeName());
							transactionList.add(closingTransactions);
							alreadyCountInReport.add(investorName.toLowerCase()+investorPan);
						}
						
					}
					
				}
				if (transactionList.size() > 0) {
					// Set of all clients details
					transactionsBasedOnFolioAndSchemeMap.put(transactionReportDTO.getNameClient(), transactionList);
				}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println("MAP SIZE: "+transactionsBasedOnFolioAndSchemeMap.size());
		return transactionsBasedOnFolioAndSchemeMap;
	}

}