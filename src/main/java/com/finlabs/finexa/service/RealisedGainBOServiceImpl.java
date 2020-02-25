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

import org.glassfish.pfl.dynamic.codegen.impl.ExpressionFactory.NewObjExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.CurrentHoldingReportColumnDTO;
import com.finlabs.finexa.dto.CurrentHoldingReportDTO;
import com.finlabs.finexa.dto.TransactionMasterBODTO;
import com.finlabs.finexa.dto.TransactionReportDTO;
import com.finlabs.finexa.model.AumMasterBO;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.InvestorBranchMasterBO;
import com.finlabs.finexa.model.InvestorMasterBO;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.InvestMasterBORepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.resources.model.GainCalculator;
import com.finlabs.finexa.resources.service.GainCalculatorBOService;
import com.finlabs.finexa.util.FinexaUtil;

@Service("RealisedGainBOService")
@Transactional
public class RealisedGainBOServiceImpl implements RealisedGainBOService {

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private MasterMutualFundETFRepository masterMutualFundETFRepository;

	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepo;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private InvestMasterBORepository investorMasterRepo;

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd/MM/yyyy");
	Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));

	@Override
	public Map<String, List<CurrentHoldingReportColumnDTO>> realisedGainReport(
			CurrentHoldingReportDTO currentHoldingReportDTO) throws RuntimeException, ParseException,
			InstantiationException, IllegalAccessException, ClassNotFoundException {

		String rtaCode, folioNo, fundHouse, fundHouseName, selectedScheme;
		rtaCode = "";
		Date fromDate, toDate, firstDate;
		// boolean flag = false;
		GainCalculatorBOService gc = new GainCalculatorBOService();
		List<String> clientMasterList = new ArrayList<String>();
		List<String> distinctSchemeNameList = new ArrayList<String>();
		List<Object[]> folioSchemeNameList = new ArrayList<Object[]>();
		List<Date> transactionDateList = new ArrayList<Date>();
		// List<TransactionMasterBO> transactionDetailsList = new
		// ArrayList<TransactionMasterBO>();
		Map<String, List<CurrentHoldingReportColumnDTO>> realisedGainMap = new HashMap<String, List<CurrentHoldingReportColumnDTO>>();

		int clientId = currentHoldingReportDTO.getClientId();
		ClientMaster cm = clientMasterRepository.findOne(clientId);
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
				if(Arrays.asList(currentHoldingReportDTO.getFamilyMemberIdArr()).contains(obj.getId())) {
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

		System.out.println("clientMasterList size(): " + clientMasterList.size());
		System.out.println(clientMasterList);

		fromDate = currentHoldingReportDTO.getFromDate();
		toDate = currentHoldingReportDTO.getToDate();

		try {

			List<CurrentHoldingReportColumnDTO> realisedGainList = new ArrayList<CurrentHoldingReportColumnDTO>();

			for (String invName : clientMasterList) {
				try {
					
					String investorName = invName.substring(0, invName.indexOf('-'));
					String investorPan = invName.substring(invName.indexOf('-') + 1);
					folioSchemeNameList = new ArrayList<Object[]>();
					System.out.println(investorName+investorPan);
					// Fetches distinct folio numbers of a specific investor within a specified
					// duration
					
					List<String> folioList = transactionMasterBORepository
							.getDistinctFolioNoByInvestorNameFromAndToDate(investorName, investorPan, fromDate, toDate);

					for (String folio : folioList) {
						try {
							// Gets list of distinct scheme names and folio number of a specific investor
							distinctSchemeNameList = transactionMasterBORepository
									.getDistinctRTACodeByFolioNoAndDate(folio, investorName, investorPan, fromDate, toDate);
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
					
					//folioSchemeNameList = transactionMasterBORepository.getDistinctFolioSchemeSetByInvestorNamePANAndTransactionDate(investorName, investorPan, fromDate, toDate);
					
					// Iterates to calculate on each set of scheme names and folio number of a
					// specific investor
					for (Object[] fS : folioSchemeNameList) {
						System.out.println("FOLIO: "+fS[0]+ " , SCHEME: "+ fS[1]);
					}
					for (Object[] folioScheme : folioSchemeNameList) {
						int toBeConsideredFlag = 0;
						try {
							folioNo = (String) folioScheme[0];
							rtaCode = (String) folioScheme[1];
							System.out.println("FOLIO: " + folioNo + " " + "RTA CODE: " + rtaCode);
							if (rtaCode.equals("D98")) {
								System.out.println(rtaCode+investorName);
							}

							if (currentHoldingReportDTO.getSchemeName() != null
									&& currentHoldingReportDTO.getSchemeName() != "") {
								selectedScheme = currentHoldingReportDTO.getSchemeName();
								List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo
										.findByCamsCodeAndStatus(rtaCode, "NEW");
								System.out.println("SCHEME ISIN: " + isinMasterBO.get(0).getIsin() + " "
										+ "SELECTED RTA CODE: " + selectedScheme);
								String isinNumber = isinMasterBO.get(0).getIsin();

								if (isinNumber.equals(currentHoldingReportDTO.getSchemeName())) {
									toBeConsideredFlag = 1;
								} else {

								}
							} else {
								toBeConsideredFlag = 1;
							}

							if (toBeConsideredFlag == 1) {

								List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo
										.findByCamsCodeAndStatus(rtaCode, "NEW");
								if (isinMasterBO != null && isinMasterBO.size() > 0) {

									String isin = isinMasterBO.get(0).getIsin();

									transactionDateList = transactionMasterBORepository
											.getFromDateByInvestorNamePANFolioScheme(investorName, investorPan, folioNo,
													rtaCode);
									System.out.println(investorName+", "+investorPan+", "+folioNo+", "+rtaCode+", "+transactionDateList.size());
									firstDate = transactionDateList.get(0);
									System.out.println("ISIN: "+isin);
									MasterMutualFundETF etf = masterMutualFundETFRepository.findOne(isin);
									fundHouseName = etf.getFundHouse();
									CurrentHoldingReportColumnDTO currentHoldingReportColumnDTO = new CurrentHoldingReportColumnDTO();

									double transUnits = 0.00, investmentCost = 0.00, redeemedValue = 0.00,
											capitalGain = 0.0;
									double dividendEarned = 0.00;
									/*
									 * transactionDetailsList = transactionMasterBORepository.
									 * findTransactionBasedOnTransCodeAndInvestorAndFromToDate (invName, scheme,
									 * fromDate, toDate, "R", "SO");
									 */

									// Stores the list of results by gain calculator
									List<GainCalculator> list = gc.getTransationDetails(investorName, folioNo, rtaCode,
											firstDate, toDate);

									// Iterates to search over list of results of gain calculator, to find out
									// positive sell quantity
									
									System.out.println("GAIN CALCULATOR LIST:");
									System.out.println(list);
									if(list.size() == 0) {
										continue;
									}
									for (GainCalculator gainCalculator : list) {

										if (gainCalculator.getSellQuantity() > 0) {
											// flag = true;
											transUnits += gainCalculator.getSellQuantity();
											System.out.println(
													"INVESTMENT COST: " + gainCalculator.getDifferentialCost());
											investmentCost += gainCalculator.getDifferentialCost();
											redeemedValue += gainCalculator.getTransAmt();
											System.out.println("REEDEMED COST: " + gainCalculator.getTransAmt());
											capitalGain += gainCalculator.getCapitalGain();
											dividendEarned += gainCalculator.getDividendReinvestRealized();
											// System.out.println("CAPITAL GAIN in DTO of SERVICE:
											// "+gainCalculator.getCapitalGain());
										}
									}
									
									/*
									 * //Handling of no positive sell quantity throughout the list if (flag ==
									 * false) {
									 * 
									 * transUnits = 0.0; investmentCost = 0.0; redeemedValue = 0.0;
									 * currentHoldingReportColumnDTO.setRealizedGainLoss("0.0");
									 * 
									 * 
									 * for (GainCalculator gainCalculator : list) {
									 * 
									 * transUnits += gainCalculator.getSellQuantity(); investmentCost += (transUnits
									 * * gainCalculator.getRate()); currentValue += (transUnits * currentNav);
									 * redeemedValue = 0.00;
									 * 
									 * }
									 * 
									 * currentHoldingReportColumnDTO.setRealizedGainLoss(numberFormatter.format(
									 * currentValue - investmentCost).replace("\u20B9", ""));
									 * //System.out.println("CAPITAL GAIN in DTO of SERVICE: "+gainCalculator.
									 * getCapitalGain()); }
									 */

									if (transUnits > 0) {
										currentHoldingReportColumnDTO.setUnits(numberFormatter
												.format(new BigDecimal(FinexaUtil.roundOff(transUnits, 3)))
												.replace("\u20B9", ""));
										currentHoldingReportColumnDTO.setInvestmentCost(numberFormatter
												.format(new BigDecimal(FinexaUtil.roundOff(investmentCost, 3)))
												.replace("\u20B9", ""));
										currentHoldingReportColumnDTO.setRedeemedValue(numberFormatter
												.format(new BigDecimal(FinexaUtil.roundOff(redeemedValue, 3)))
												.replace("\u20B9", ""));
										currentHoldingReportColumnDTO.setDividendEarned(numberFormatter
												.format(new BigDecimal(FinexaUtil.roundOff(dividendEarned, 3)))
												.replace("\u20B9", ""));
										currentHoldingReportColumnDTO.setSchemeName(etf.getSchemeName());
										currentHoldingReportColumnDTO.setFolioNo(folioNo);
										currentHoldingReportColumnDTO.setFundName(fundHouseName);
										System.out.println("REEDEMED VALUE: " + redeemedValue);
										System.out.println("Cost of inv VALUE: " + investmentCost);
										System.out.println("GAIN/LOSS: " + (redeemedValue - investmentCost));
										currentHoldingReportColumnDTO.setRealizedGainLoss(numberFormatter
												.format(new BigDecimal(
														FinexaUtil.roundOff((redeemedValue - investmentCost), 3)))
												.replace("\u20B9", ""));

										// numberFormatter.format(capitalGain).replace("\u20B9", "")

										// Set of one Mutual fund scheme's calculated values
										currentHoldingReportColumnDTO
												.setFolioDetails(folioNo + " - " + etf.getSchemeName());

										if (mainFamilyPanMap.containsKey(investorPan)) {

											currentHoldingReportColumnDTO.setClientDetails(
													mainFamilyPanMap.get(investorPan) + "-" + investorPan);
										} else {
											currentHoldingReportColumnDTO.setClientDetails(invName);
										}
										realisedGainList.add(currentHoldingReportColumnDTO);
									}
								} else {
									System.out.println("SchemeIsin Master No Record found for rtaCode " + rtaCode);
								}
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (realisedGainList.size() > 0) {
				// Set of all clients details
				realisedGainMap.put(currentHoldingReportDTO.getNameClient(), realisedGainList);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return realisedGainMap;

	}

}
