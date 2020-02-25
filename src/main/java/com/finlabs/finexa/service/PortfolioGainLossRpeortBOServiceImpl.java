package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.text.Format;
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

import com.finlabs.finexa.dto.PortfolioGainLossReportColumnDTO;
import com.finlabs.finexa.dto.PortfolioGainLossReportDTO;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.resources.model.GainCalculator;
import com.finlabs.finexa.resources.service.GainCalculatorBOService;
import com.finlabs.finexa.util.FinexaUtil;

@Service("PortfolioGainLossRpeortBOService")
@Transactional
public class PortfolioGainLossRpeortBOServiceImpl implements PortfolioGainLossRpeortBOService {

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private MasterMutualFundETFRepository masterMutualFundETFRepository;

	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepo;

    @Autowired
    private MasterMFDailyNAVRepository masterMFDailyNAVRepository;
    
	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd/MM/yyyy");
	Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));

	@Override
	public Map<String, List<PortfolioGainLossReportColumnDTO>> portfolioGainLossReport(
			PortfolioGainLossReportDTO portfolioGainLossReportDTO) {
		// TODO Auto-generated method stub
		Map<String, String> alreadyCountInReportMap = new HashMap<String, String>();
		GainCalculatorBOService gc = new GainCalculatorBOService();
		Date asOnDate, investmentSince;
		String rtaCode, folioNo, fundHouse, fundHouseName, selectedScheme;
		List<String> clientMasterList = new ArrayList<String>();
		List<Object[]> distinctFolioSchemeRTAList;
		Map<String, String> clientNamePANMap = new HashMap<String, String>();
		int size=0;
		Map<String, List<PortfolioGainLossReportColumnDTO>> portfolioGainLossMap = new HashMap<String, List<PortfolioGainLossReportColumnDTO>>();
		List<PortfolioGainLossReportColumnDTO> portfolioGainLossList = new ArrayList<PortfolioGainLossReportColumnDTO>();
		try {
			int clientId = portfolioGainLossReportDTO.getClientId();
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			Map<String, String> mainFamilyPanMap = new HashMap<String, String>();

			if (cm != null) {
				String name = cm.getFirstName() + (cm.getMiddleName() == null ? "" : (" " + cm.getMiddleName()))
						+ (cm.getLastName() == null ? "" : " " + (cm.getLastName()));
				String pan = cm.getPan();
				clientMasterList.add(name + "-" + pan);
				mainFamilyPanMap.put(pan, name);
				clientNamePANMap.put(name.trim()+"-"+pan.trim(), name.trim());
				List<AuxillaryInvestorMaster> auxList = cm.getAuxillaryInvestorMasters();
				for (AuxillaryInvestorMaster obj : auxList) {
					String auxName = obj.getFirstName()
							+ (obj.getMiddleName() == null ? "" : (" " + obj.getMiddleName()))
							+ (obj.getLastName() == null ? "" : (" " + obj.getLastName()));
					String pan1 = obj.getPan();
					if (!clientMasterList.contains(auxName + "-" + pan1)) {
						clientMasterList.add(auxName + "-" + pan1);
					}
					clientNamePANMap.put(auxName.trim()+"-"+pan1.trim(), name.trim());
				}
				List<ClientFamilyMember> familyMemberList = cm.getClientFamilyMembers();
				for (ClientFamilyMember obj : familyMemberList) {
					if (Arrays.asList(portfolioGainLossReportDTO.getFamilyMemberIdArr()).contains(obj.getId())) {
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
								clientNamePANMap.put(auxName.trim()+"-"+pan1.trim(), auxName.trim());
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
									clientNamePANMap.put(auxNameFam.trim()+"-"+panFam.trim(), auxName.trim());
								}
							}
						}
					}
				}
			}
			System.out.println("clientMasterList size(): " + clientMasterList.size());
			System.out.println(clientMasterList);
			for (Map.Entry<String,String> entry : clientNamePANMap.entrySet())  
	            System.out.println("Key = " + entry.getKey() + 
	                             ", Value = " + entry.getValue()); 
			System.out.println(portfolioGainLossReportDTO.getSchemeName());
			asOnDate = portfolioGainLossReportDTO.getAsOnDate();
			/**************************************************************************************************************************************/
			for (String invName : clientMasterList) {
				try {
					double realisedXIRR = 0.0d, unrealisedXIRR = 0.0d, totalXIRR = 0.0d;

					String investorName = invName.substring(0, invName.indexOf('-'));
					String investorPAN = invName.substring(invName.indexOf('-') + 1);
					// System.out.println(investorName+investorPAN);

					// Fetches distinct folio numbers of a specific investor within a specified
					// duration
					if (!alreadyCountInReportMap.containsKey(investorName.toLowerCase() + investorPAN)) {
					distinctFolioSchemeRTAList = new ArrayList<Object[]>();
					distinctFolioSchemeRTAList = transactionMasterBORepository
							.getDistinctFolioSchemeSetByInvestorNamePANAndAsOnTransactionDate(investorName, investorPAN,
									asOnDate);
					for(Object[] folioScheme : distinctFolioSchemeRTAList) {
						int toBeConsideredFlag = 0;
						try {
							folioNo = (String) folioScheme[0];
							rtaCode = (String) folioScheme[1];
							/*
							if(!folioNo.trim().equals("7779593122")) {
								System.out.println(rtaCode);
								continue;
							}
							/*
							if(rtaCode.equals("K154"))
								System.out.println("FOLIO: " + folioNo + " " + "RTA CODE: " + rtaCode);
							*/
							System.out.println("FolioNo" + folioNo);
							System.out.println("SchemeRTACode" + rtaCode);
							if (folioNo.equals("2109828286") && rtaCode.equals("120USIG")) {
								System.out.println("Here...");
							}
							List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo
									.findByCamsCodeAndStatus(rtaCode, "NEW");
							if (portfolioGainLossReportDTO.getSchemeName() != null
									&& portfolioGainLossReportDTO.getSchemeName() != ""
									&& !portfolioGainLossReportDTO.getSchemeName().equalsIgnoreCase("null")) {
								selectedScheme = portfolioGainLossReportDTO.getSchemeName();
								/*
								List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo
										.findByCamsCodeAndStatus(rtaCode, "NEW");
								*/
								// System.out.println("SCHEME ISIN: " + isinMasterBO.get(0).getIsin() + " " +
								// "SELECTED RTA CODE: " + selectedScheme);
								String isinNumber = "";
								if(isinMasterBO.size() > 0) {
									isinNumber = isinMasterBO.get(0).getIsin();
								} else {
									System.out.println("SchemeIsin Master No Record found for rtaCode " + rtaCode);
									continue;
								}
								
								if (isinNumber.equals(portfolioGainLossReportDTO.getSchemeName())) {
									toBeConsideredFlag = 1;
								} else {

								}
							} else {
								toBeConsideredFlag = 1;
							}

							if (toBeConsideredFlag == 1) {
								/*
								List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo
										.findByCamsCodeAndStatus(rtaCode, "NEW");
								*/		
								if (isinMasterBO != null && isinMasterBO.size() > 0) {
									double unrealisedGainLoss = 0.0, unrealisedUnits = 0.0, realisedCostOfInvestment = 0.0, balanceUnits = 0.0, currentValue = 0.0, 
											totalGainLoss = 0.0, unrealisedCostOfInvestment = 0.00, redeemedValue = 0.00, realisedGainLoss = 0.0, currentNav = 0.0,
											residualUnits = 0.0, marketValue = 0.0, navOfUnrealisedUnits = 0.0;
									float nav = 0;
									String isin = isinMasterBO.get(0).getIsin();
									/*
									List<TransactionMasterBO> transactionDateList = transactionMasterBORepository
											.findTransactiondetailsByInvestorNameAndAsOnDateAndFolioAndSchemeRTA(
													investorName, investorPAN, folioNo, rtaCode, asOnDate);
									*/
									
									investmentSince = transactionMasterBORepository.findMinDateByInvestorNameAndFolioAndSchemeRTA(investorName, investorPAN, folioNo, rtaCode);
									
									MasterMutualFundETF etf = masterMutualFundETFRepository.findOne(isin);
									fundHouseName = etf.getFundHouse();
									PortfolioGainLossReportColumnDTO portfolioGainLossReportColumnDTO = new PortfolioGainLossReportColumnDTO();
									/****************************************/
									Date maxDateForNAV = masterMFDailyNAVRepository.findMaxDate(isin,
											asOnDate);
									MasterMFDailyNAV dailyNav = masterMFDailyNAVRepository.findNAV(isin,
											maxDateForNAV);

									//presentNAV = (double) currentNAV.getNav();
									/****************************************/
									//MasterMFDailyNAV dailyNav = masterMFDailyNAVRepository.findTopByIdMasterMutualFundEtfIsinOrderByIdDateDesc(isin);
									if(dailyNav != null)
										//currentNav = (double)dailyNav.getNav();
										nav = dailyNav.getNav();
									else
										System.out.println("NAV NOT FOUND FOR SCHEMERTACODE: "+rtaCode+", AND ISIN: "+isin);
									// Stores the list of results by gain calculator
									List<GainCalculator> list = gc.getTransationDetails(investorName, folioNo, rtaCode,
											investmentSince, asOnDate);

									// Iterates to search over list of results of gain calculator, to find out
									// positive sell quantity

									if (list.size() == 0) {
										continue;
									}
									double[] payments = null;
									Date[] date = null;
									for (GainCalculator gainCalculator : list) {
										/*
										switch(gainCalculator.getTransactionType()) {
											case "Addition":
												balanceUnits += gainCalculator.getBuyQuantity();
												break;
											case "Subtraction":
												balanceUnits -= gainCalculator.getSellQuantity();
												break;
										}
										*/
										/*
										if(gainCalculator.getResidualBuyQty() > 0) {
											unrealisedUnits = gainCalculator.getResidualBuyQty();
											navOfUnrealisedUnits = gainCalculator.getRate();
										}
										*/
										if(gainCalculator.getSellQuantity() > 0) {
											unrealisedCostOfInvestment -= gainCalculator.getDifferentialCost();
											//residualUnits -= gainCalculator.getResidualBuyQty();
										} else {
											unrealisedCostOfInvestment += gainCalculator.getDifferentialCost();
											
										}
										
										residualUnits += gainCalculator.getResidualBuyQty();
										if (gainCalculator.getSellQuantity() > 0) {
											
											realisedCostOfInvestment += gainCalculator.getDifferentialCost();
											redeemedValue += gainCalculator.getTransAmt();
											balanceUnits -= gainCalculator.getSellQuantity();
										} else if (gainCalculator.getBuyQuantity() > 0){
											
											balanceUnits += gainCalculator.getBuyQuantity();
										}
										/*
										if (gainCalculator.getCumBuyCost() > 0) {
											unrealisedCumBuyCost = gainCalculator.getCumBuyCost();
	        							}
										*/ 
										realisedXIRR = gainCalculator.getXirr();
										unrealisedXIRR = gainCalculator.getXirrUnrealized();
										totalXIRR = gainCalculator.getXirrTotal();
										
										 									
											
									}
									
									
									
									if (balanceUnits > 0) {

	        							//currentValue = balanceUnits * currentNav;
	        							currentValue = balanceUnits * nav;
									}
									System.out.println(unrealisedCostOfInvestment+","+residualUnits);
									realisedGainLoss = redeemedValue - realisedCostOfInvestment;
									//unrealisedCostOfInvestment = unrealisedCumBuyCost - realisedCostOfInvestment;
									//unrealisedCostOfInvestment = unrealisedUnits * navOfUnrealisedUnits;
									unrealisedGainLoss = currentValue - unrealisedCostOfInvestment;
									
									marketValue = residualUnits * nav;
									System.out.println(folioNo+"-"+rtaCode);
									System.out.println("IMPORTANT:"+residualUnits+","+nav+","+dailyNav.getId().getDate()+","+dailyNav.getNav());
										
									totalGainLoss = realisedGainLoss + unrealisedGainLoss;
									System.out.println(realisedGainLoss+","+unrealisedGainLoss+","+totalGainLoss);
									portfolioGainLossReportColumnDTO.setUnrealizedGainLossAmount(numberFormatter.format(Math.round(unrealisedGainLoss)).replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(unrealisedGainLoss))).replace("\u20B9", "").length() - 3));
									portfolioGainLossReportColumnDTO.setUnrealizedInvestmentCost(numberFormatter.format(Math.round(unrealisedCostOfInvestment)).replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(unrealisedCostOfInvestment))).replace("\u20B9", "").length() - 3));
									portfolioGainLossReportColumnDTO
											.setRealizedInvestmentCost(numberFormatter.format(Math.round(realisedCostOfInvestment)).replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(realisedCostOfInvestment))).replace("\u20B9", "").length() - 3));
									portfolioGainLossReportColumnDTO
											.setRealizedGainLossAmount(numberFormatter.format(Math.round(realisedGainLoss)).replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(realisedGainLoss))).replace("\u20B9", "").length() - 3));
									//portfolioGainLossReportColumnDTO.setRealizedXirr(numberFormatter.format(Math.round(realisedXIRR)).replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(realisedXIRR))).replace("\u20B9", "").length() - 3));
									
									portfolioGainLossReportColumnDTO.setTotalGainLossAmount(numberFormatter.format(Math.round(totalGainLoss)).replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(totalGainLoss))).replace("\u20B9", "").length() - 3));
									
									portfolioGainLossReportColumnDTO.setInvestmentSince(formatterDisplay.format(investmentSince));
									portfolioGainLossReportColumnDTO.setMarketValue(numberFormatter.format(Math.round(marketValue)).replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(marketValue))).replace("\u20B9", "").length() - 3));
									portfolioGainLossReportColumnDTO.setSchemeName(etf.getSchemeName());
									portfolioGainLossReportColumnDTO.setFolioNo(folioNo);
									
									if (mainFamilyPanMap.containsKey(investorPAN)) {
										/*
										portfolioGainLossReportColumnDTO.setClientDetails(
												mainFamilyPanMap.get(investorPAN) + "-" + investorPAN);
										*/	
										String namePAN = investorName.trim()+"-"+investorPAN;
										portfolioGainLossReportColumnDTO.setClientDetails(
												clientNamePANMap.get(namePAN) + "-" + investorPAN);
									} else {
										portfolioGainLossReportColumnDTO.setClientDetails(investorName);
									}
									//System.out.println("XIRR "+xirr);
									try {
										portfolioGainLossReportColumnDTO.setTotalXirr(numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(totalXIRR, 3))).replace("\u20B9", ""));
									}catch(Exception e) {
										portfolioGainLossReportColumnDTO.setTotalXirr("0.00");
									}
									try {
										portfolioGainLossReportColumnDTO.setUnrealizedXirr(numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(unrealisedXIRR, 3))).replace("\u20B9", ""));
									}catch(Exception e) {
										portfolioGainLossReportColumnDTO.setUnrealizedXirr("0.00");
									}
									try {
										portfolioGainLossReportColumnDTO.setRealizedXirr(numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(realisedXIRR, 3))).replace("\u20B9", ""));
									}catch(Exception e) {
										portfolioGainLossReportColumnDTO.setRealizedXirr("0.00");
									}
							
									alreadyCountInReportMap.put(investorName.toLowerCase() + investorPAN, folioNo);
									
									portfolioGainLossList.add(portfolioGainLossReportColumnDTO);
								} else {
									System.out.println("SchemeIsin Master No Record found for rtaCode " + rtaCode);
								}
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

			if (portfolioGainLossList.size() > 0) {
				// Set of all clients details
				portfolioGainLossMap.put(portfolioGainLossReportDTO.getNameClient(), portfolioGainLossList);
			}
			/**************************************************************************************************************************************/
		} catch (Exception e) {
			e.printStackTrace();
		}

		return portfolioGainLossMap;
	}

}
