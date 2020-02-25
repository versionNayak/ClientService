package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.AumReportColumnDTO;
import com.finlabs.finexa.dto.TransactionReportColumnDTO;
import com.finlabs.finexa.model.AumMasterBO;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.AumMasterBORepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.InvestMasterBORepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.util.FinexaUtil;

@Service("AUMReportService")
@Transactional
public class AUMReportBOServiceImpl implements AUMReportBOService{


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
	private InvestMasterBORepository investMasterBORepository;
	
	@Autowired
	private AumMasterBORepository aumMasterBORepository;

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public double getTotalClientAUMFromTransaction(int advisorID, Date asOfDate) throws RuntimeException {
		// TODO Auto-generated method stub

		Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));

		String currentNAV = "";
		String currentValue = "";
		double currentAUM = 0.0;
		double totalAUM = 0.0;
		Date asOnDate = asOfDate; // always spool the results from current date
		Map<String, List<TransactionReportColumnDTO>> transactionsBasedOnFolioAndSchemeMap = new HashMap<String, List<TransactionReportColumnDTO>>();
		List<TransactionReportColumnDTO> transactionList = new ArrayList<TransactionReportColumnDTO>();
		List<String> clientMasterList = new ArrayList<String>();
		Map<String,String> rtaCodeIsinMap = new HashMap<String, String>();

		List<Object[]> clientNamePANList = investMasterBORepository.findAllDistinctInvestorNameAndPAN(advisorID);

		for (Object[] clientNamePAN : clientNamePANList) {
			String invPAN = (String) clientNamePAN[0];
			String invName = (String) clientNamePAN[1];
			clientMasterList.add(invName + '-' + invPAN);
		}
		System.out.println(clientMasterList + "MasterList Size : " + clientMasterList.size());

		List<String> rtaCodeList = transactionMasterBORepository.getDistinctRTACodeByAdvisor(advisorID);
		for (String obj : rtaCodeList) {
			List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo.findByCamsCodeAndStatus(obj,
					"NEW");
			if (!rtaCodeIsinMap.containsKey(obj) && isinMasterBO.size() > 0) {
				String isin = isinMasterBO.get(0).getIsin();
				rtaCodeIsinMap.put(obj, isin);
			}
		}
		
		try {

			for (String invName : clientMasterList) {

				double clientWiseAUM = 0.0;
				String investorName = invName.substring(0, invName.indexOf('-'));
				String investorPan = invName.substring(invName.indexOf('-') + 1);

				// Returns the list of DISTINCT set of Folio Number and Scheme Name
				List<Object[]> distinctFolioSchemeRTASet = transactionMasterBORepository
						.getDistinctFolioSchemeSetByInvestorNamePANAndAsOnDate(investorName, investorPan, asOnDate);

				for (Object[] transactionMasterBO : distinctFolioSchemeRTASet) {
					String folioNo = (String) transactionMasterBO[0];
					String rtaCode = (String) transactionMasterBO[1];

					
					if (rtaCodeIsinMap.containsKey(rtaCode)) {
						//String isin = isinMasterBO.get(0).getIsin();
						//MasterMutualFundETF mutualFundETF = etfRepository.findOne(isin);

						double runningTotal = 0.0d;
						double transUnits = 0.0d;

						Date firstdate = transactionMasterBORepository
								.getFirstdateOfTransaction(investorName, investorPan, folioNo, rtaCode).get(0);
						// System.out.println("FIRST DATE:"+firstdate);
						// Returns the list of all transactions based on a set of Folio Number and
						// Scheme Name
						List<TransactionMasterBO> transactionsBasedOnFolioAndSchemeList = transactionMasterBORepository
								.findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeRTA(
										investorName, folioNo, rtaCode, firstdate, asOnDate);

						for (TransactionMasterBO tmbo : transactionsBasedOnFolioAndSchemeList) {
							try {
								/*
								 * TransactionReportColumnDTO transactions = new TransactionReportColumnDTO();
								 * 
								 * transactions.setFolioNo(tmbo.getFolioNo());
								 * transactions.setFolioDetails(folioNo + "-" + mutualFundETF.getSchemeName());
								 * transactions.setTransAmt(numberFormatter
								 * .format(Math.round(Double.parseDouble(tmbo.getTransAmt())))
								 * .replace("\u20B9", "").substring(0,
								 * numberFormatter.format(Math.round(Double.parseDouble(tmbo.getTransAmt()))).
								 * replace("\u20B9", "").length() - 3));
								 * transactions.setTransactionDate(formatterDisplay.format(tmbo.
								 * getTransactionDate()));
								 * 
								 * if(tmbo.getLookupTransactionRule() != null) {
								 * transactions.setTransactionType(tmbo.getLookupTransactionRule().
								 * getDescription()); } if (tmbo.getNav() != null && !tmbo.getNav().isEmpty() &&
								 * !tmbo.getNav().equals("null")) { transactions.setNav(numberFormatter
								 * .format(new BigDecimal(FinexaUtil.roundOff(Double.parseDouble(tmbo.getNav()),
								 * 2))) .replace("\u20B9", "")); } transactions.setTransUnits(numberFormatter
								 * .format(FinexaUtil.roundOff(Double.parseDouble(tmbo.getTransUnits()), 2))
								 * .replace("\u20B9", ""));
								 */
								transUnits = Double.parseDouble(tmbo.getTransUnits());

								if (tmbo.getLookupTransactionRule() != null) {
									switch (tmbo.getLookupTransactionRule().getCode()) {
									case "P":
										runningTotal += transUnits;
										/*
										 * transactions.setRunningTotal(
										 * numberFormatter.format(Math.round(runningTotal)) .replace("\u20B9",
										 * "").substring(0, numberFormatter.format(new
										 * BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
										 */
										break;
									case "R":
										runningTotal -= transUnits;
										/*
										 * transactions.setRunningTotal(
										 * numberFormatter.format(Math.round(runningTotal)) .replace("\u20B9",
										 * "").substring(0, numberFormatter.format(new
										 * BigDecimal(Math.round(runningTotal))).replace("\u20B9", "").length() - 3));
										 */break;
									case "SI":
										runningTotal += transUnits;
										/*
										 * transactions.setRunningTotal(
										 * numberFormatter.format(Math.round(runningTotal)).replace("\u20B9", "")
										 * .substring(0, numberFormatter .format(new
										 * BigDecimal(Math.round(runningTotal))) .replace("\u20B9", "").length() - 3));
										 */break;
									case "SO":
										runningTotal -= transUnits;
										/*
										 * transactions.setRunningTotal(
										 * numberFormatter.format(Math.round(runningTotal)).replace("\u20B9", "")
										 * .substring(0, numberFormatter .format(new
										 * BigDecimal(Math.round(runningTotal))) .replace("\u20B9", "").length() - 3));
										 */break;
									case "TI":
										runningTotal += transUnits;
										/*
										 * transactions.setRunningTotal(
										 * numberFormatter.format(Math.round(runningTotal)).replace("\u20B9", "")
										 * .substring(0, numberFormatter .format(new
										 * BigDecimal(Math.round(runningTotal))) .replace("\u20B9", "").length() - 3));
										 */break;
									case "TO":
										runningTotal -= transUnits;
										/*
										 * transactions.setRunningTotal(
										 * numberFormatter.format(Math.round(runningTotal)).replace("\u20B9", "")
										 * .substring(0, numberFormatter .format(new
										 * BigDecimal(Math.round(runningTotal))) .replace("\u20B9", "").length() - 3));
										 */break;
									case "DR":
									case "DIR":
									case "Dividend Reinvestment":
										runningTotal += transUnits;
										/*
										 * transactions.setRunningTotal(
										 * numberFormatter.format(Math.round(runningTotal)).replace("\u20B9", "")
										 * .substring(0, numberFormatter .format(new
										 * BigDecimal(Math.round(runningTotal))) .replace("\u20B9", "").length() - 3));
										 */break;
									case "SIP":
										runningTotal += transUnits;
										/*
										 * transactions.setRunningTotal(
										 * numberFormatter.format(Math.round(runningTotal)).replace("\u20B9", "")
										 * .substring(0, numberFormatter .format(new
										 * BigDecimal(Math.round(runningTotal))) .replace("\u20B9", "").length() - 3));
										 */break;
									case "ADDPUR":
										runningTotal += transUnits;
										/*
										 * transactions.setRunningTotal(
										 * numberFormatter.format(Math.round(runningTotal)).replace("\u20B9", "")
										 * .substring(0, numberFormatter .format(new
										 * BigDecimal(Math.round(runningTotal))) .replace("\u20B9", "").length() - 3));
										 */break;
									case "DP":
									case "J":
										// no effect
										/*
										 * transactions.setRunningTotal(
										 * numberFormatter.format(Math.round(runningTotal)).replace("\u20B9", "")
										 * .substring(0, numberFormatter .format(new
										 * BigDecimal(Math.round(runningTotal))) .replace("\u20B9", "").length() - 3));
										 */break;
									default:
										break;
									}
								}

								/*
								if (dailyNAV != null) {
									currentNAV = String.valueOf(FinexaUtil.roundOff((double) dailyNAV.getNav(), 2));
								} else {
									currentNAV = String.valueOf(FinexaUtil.roundOff(0, 2));
								}
									
								if (tmbo.getLookupTransactionRule() != null) {
									if (tmbo.getLookupTransactionRule().getCode().equals("DP")) {
										transactions.setNav(" ");
										transactions.setTransUnits("0");
									}
								}
								*/
								//transactionList.add(transactions);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						/*
						currentValue = numberFormatter.format(Math.round(runningTotal * Double.parseDouble(currentNAV)))
								.replace("\u20B9", "").substring(0,
										numberFormatter
												.format(Math.round(runningTotal * Double.parseDouble(currentNAV)))
												.replace("\u20B9", "").length() - 3);
						*/

						Date maxDate = masterMFDailyNAVRepository.findMaxDate(rtaCodeIsinMap.get(rtaCode), asOfDate);
						MasterMFDailyNAV dailyNAV = masterMFDailyNAVRepository.findNAV(rtaCodeIsinMap.get(rtaCode), maxDate);
						if (dailyNAV != null) {
							currentNAV = String.valueOf(FinexaUtil.roundOff((double) dailyNAV.getNav(), 2));
							currentAUM = runningTotal * Double.parseDouble(currentNAV);
						}
						
						/*
						TransactionReportColumnDTO closingTransactions = new TransactionReportColumnDTO();
						closingTransactions.setTransactionType("Closing Balance");
						// closingTransactions.setTransactionDate(formatterDisplay.format(toDate));
						closingTransactions.setFolioNo(folioNo);
						closingTransactions.setTransAmt(currentValue);
						closingTransactions.setNav(currentNAV);
						/*
						 * if (mainFamilyPanMap.containsKey(investorPan)) {
						 * closingTransactions.setClientDetails(mainFamilyPanMap.get(investorPan) + "-"
						 * + investorPan); } else { closingTransactions.setClientDetails(invName); }
						 
						closingTransactions
								.setRunningTotal(numberFormatter.format(Math.round(runningTotal)).replace("\u20B9", "")
										.substring(0, numberFormatter.format(new BigDecimal(Math.round(runningTotal)))
												.replace("\u20B9", "").length() - 3));
						closingTransactions.setFolioDetails(folioNo + "-" + mutualFundETF.getSchemeName());
						transactionList.add(closingTransactions);
						*/
						clientWiseAUM += currentAUM;

					}

				}
				System.out.println(investorName+", "+investorPan+", "+clientWiseAUM);
				if (transactionList.size() > 0) {
					// Set of all clients details
					transactionsBasedOnFolioAndSchemeMap.put("", transactionList);
				}

				totalAUM += clientWiseAUM;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println("MAP SIZE: "+transactionsBasedOnFolioAndSchemeMap.size());
		System.out.println(totalAUM);
		return totalAUM;

	}


	@Override
	public double getTotalClientAUMFromAumTable(int advisorID, Date asOfDate) throws RuntimeException {
		// TODO Auto-generated method stub
		
		String total = "";
		List<AumMasterBO> aumList = null;
		List<AumMasterBO> aumListToremove = new ArrayList<AumMasterBO>();
		Map<String,String> rtaCodeIsinMap = new HashMap<String, String>();
		double totalAUM = 0.0, totalAUMOfAllClients = 0.0;
		
		aumList = aumMasterBORepository.findAllByReportDateAndAdvisor(asOfDate, advisorID); 
		total = aumMasterBORepository.getTotalCurrentValueByAdvisor(asOfDate, advisorID);
		totalAUMOfAllClients = Double.parseDouble(total);
			int i = 0;
			for (AumMasterBO aumTemp : aumList) {
				try {
					
					for (int j = i+1; j < aumList.size(); j++) {
						if (aumTemp.getId().getFolioNumber() != null && aumTemp.getId().getSchemertacode() != null && aumTemp.getId().getReportDate() != null) {
						
							if (aumTemp.getId().getFolioNumber().equals(aumList.get(j).getId().getFolioNumber()) && 
									aumTemp.getId().getSchemertacode().equals(aumList.get(j).getId().getSchemertacode())								
									&& aumTemp.getId().getReportDate().before(aumList.get(j).getId().getReportDate())) {
								aumListToremove.add(aumTemp);								
							}
								
						}
					
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				i++;
			}
			if(aumListToremove.size() > 0)
				aumList.removeAll(aumListToremove);
			//System.out.println("AFTER"+aumList.size());
			if (aumList != null && aumList.size() > 0) {
				for (AumMasterBO aum : aumList) {
					
					try {
						float nav = 0;
						if(aum.getUnitBalance().equals(String.valueOf(0.0))) {
							System.out.println("Balance"+aum.getUnitBalance());
							continue;
						}
					//	System.out.println(aum.getSchemeName());
						//AumReportColumnDTO item = new AumReportColumnDTO();
						List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo.findByCamsCodeAndStatus(aum.getId().getSchemertacode(),"NEW");
						if (isinMasterBO != null && isinMasterBO.size() > 0) {
							String isinForEtf = isinMasterBO.get(0).getIsin();
							MasterMutualFundETF	etf = etfRepository.findOne(isinForEtf);
							/*
							fundHouseName = etf.getFundHouse();
							item.setFundName(fundHouseName);
							String folioNo = aum.getId().getFolioNumber();
							if(folioNo.contains("E")) {
								folioNo = folioNo.substring(0, folioNo.indexOf(".")) + folioNo.substring(folioNo.indexOf(".") + 1); 
								folioNo = folioNo.substring(0, folioNo.length() - 3);
							}
							/*
							item.setFolioNo(folioNo);
							item.setSchemeName(etf.getSchemeName());
							
							item.setUnits(numberFormatter.format(FinexaUtil.roundOff(Double.parseDouble(aum.getUnitBalance()), 2)).replace("\u20B9", ""));
							*/
							Date maxDate = masterMFDailyNAVRepository.findMaxDate(isinForEtf, asOfDate);
							MasterMFDailyNAV mfDailyNav = masterMFDailyNAVRepository.findNAV(isinForEtf, maxDate);
							
							if (maxDate != null) {
								//item.setCurrentNav(String.valueOf(FinexaUtil.roundOff(mfDailyNav.getNav(), 2)));
								//System.out.println("NAV:"+mfDailyNav.getNav());
								nav = mfDailyNav.getNav();
							} else {
								//item.setCurrentNav(String.valueOf(FinexaUtil.roundOff(Double.parseDouble(aum.getNav()), 2)));
								nav = Float.parseFloat(aum.getNav());
							}
							double currentVal = 0.0;
							if(aum.getCurrentValue() != null) {
								//System.out.println(aum.getUnitBalance()+"*"+nav);
								currentVal = Double.parseDouble(aum.getUnitBalance()) * nav;
								//item.setCurrentValue(String.valueOf(numberFormatter.format(Math.round(currentVal)).replace("\u20B9", "")).substring(0,  String.valueOf(numberFormatter.format(Math.round(currentVal)).replace("\u20B9", "")).length() - 3));
							} 
							totalAUM += currentVal;
							/*
							if(mainFamilyPanMap.containsKey(investorPan)) {
								item.setClientDetails(mainFamilyPanMap.get(investorPan) + "-" + investorPan);
							} else {
								item.setClientDetails(invName);
							}
							*/
							//item.setTotal(String.valueOf(numberFormatter.format(Math.round(Float.parseFloat(total))).replace("\u20B9", "")).substring(0, String.valueOf(numberFormatter.format(Math.round(Float.parseFloat(total))).replace("\u20B9", "")).length() - 3));
							//System.out.println(item.getSchemeName());
							//aumReportList.add(item);
						} else {
							//System.out.println("No RTA co isin mapping found for" + aum.getId().getSchemertacode());
						}
						
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						throw new RuntimeException(e);
					}
				}
				
			}
			return totalAUMOfAllClients;
	}

		

}
