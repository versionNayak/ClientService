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

import com.finlabs.finexa.dto.DividendReportColumnNewDTO;
import com.finlabs.finexa.dto.DividendReportDTO;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.util.FinexaUtil;

@Service("DividendReportBOServiceNew")
@Transactional
public class DividendReportBOServiceImplNew implements DividendReportBOServiceNew {

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepo;

	@Autowired
	private MasterMutualFundETFRepository etfRepository;

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public Map<String, List<DividendReportColumnNewDTO>> dividendReport(DividendReportDTO dividendReportDTO)
			throws RuntimeException, ParseException {
		System.out.println("Dividend service called..............");
		// TODO Auto-generated method stub

		Map<String, String> mainNamePanMapforClient = new HashMap<String, String>();
		Map<String, String> mainNamePanMapforFamily = new HashMap<String, String>();
		List<String> auxiliaryClientList = new ArrayList<>();
		List<String> auxiliaryFamilyList = new ArrayList<>();
		String selectedScheme;

		int clientId;
		int srNO = 0;
		Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));

		Date fromDate = dividendReportDTO.getFromDate();
		Date toDate = dividendReportDTO.getToDate();

		double dividendPayout, dividendReinvestment;
		List<String> clientMasterList = new ArrayList<String>();
		// Stores each distinct pair of Folio Number and Scheme Name as String and it's
		// list of transactions as List
		Map<String, List<DividendReportColumnNewDTO>> dividendFolioAndSchemeMap = new HashMap<String, List<DividendReportColumnNewDTO>>();

		System.out.println("MAP  " + dividendFolioAndSchemeMap.size());

		for (Map.Entry<String, List<DividendReportColumnNewDTO>> entry : dividendFolioAndSchemeMap.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}

		// Returns the list of DISTINCT set of Folio Number and Scheme Name
		List<Object[]> distinctFolioSchemeList = new ArrayList<Object[]>();

		clientId = dividendReportDTO.getClientId();

		ClientMaster cm = clientMasterRepository.findOne(clientId);
		Map<String, String> mainFamilyPanMap = new HashMap<String, String>();

		if (cm != null) {
			String name = cm.getFirstName() + (cm.getMiddleName() == null ? "" : (" " + cm.getMiddleName()))
					+ (cm.getLastName() == null ? "" : " " + (cm.getLastName()));
			String pan = cm.getPan();
			if (Arrays.asList(dividendReportDTO.getClientId()).contains(clientId)) {
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
				if (Arrays.asList(dividendReportDTO.getFamilyMemberIdArr()).contains(obj.getId())) {
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
		System.out.println("CLIENT MASTER: " + clientMasterList);
		System.out.println("clientMasterList size(): " + clientMasterList.size());

		// try {
		List<DividendReportColumnNewDTO> dividendTransactionList = new ArrayList<DividendReportColumnNewDTO>();
		List<Object[]> distinctFolioSchemeRTASet = new ArrayList<>();
		List<String> alreadyCountInReport = new ArrayList<String>();
		for (String clientMasterNamePan : clientMasterList) {
			try {
				String invName = clientMasterNamePan.substring(0, clientMasterNamePan.indexOf('-'));
				invName = invName.trim();
				String invPAN = clientMasterNamePan.substring(clientMasterNamePan.indexOf('-') + 1);
				invPAN = invPAN.trim();
				boolean isDividendFound = false;
				if (!alreadyCountInReport.contains(invName.trim().toLowerCase() + invPAN)) {
					distinctFolioSchemeRTASet = transactionMasterBORepository
							.getDistinctFolioSchemeRTASetByInvestorNameAndAsOnDate(invName, invPAN, toDate);

					Double totalForDP = 0.0d;
					Double totalForDR = 0.0d;
					for (Object[] transactionMasterBO : distinctFolioSchemeRTASet) {
						int toBeConsideredFlag = 0;

						String folioNo = (String) transactionMasterBO[0];
						String rtaCode = (String) transactionMasterBO[1];

						List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo.findByCamsCodeAndStatus(rtaCode,
								"NEW");
						if (dividendReportDTO.getSchemeName() != null && dividendReportDTO.getSchemeName() != ""
								&& !dividendReportDTO.getSchemeName().equalsIgnoreCase("null")) {
							selectedScheme = dividendReportDTO.getSchemeName();
							String isinNumber = "";
							if (isinMasterBO.size() > 0) {
								isinNumber = isinMasterBO.get(0).getIsin();

							} else {
								System.out.println("SchemeIsin Master No Record found for rtaCode " + rtaCode);
								continue;
							}

							if (isinNumber.equals(dividendReportDTO.getSchemeName())) {

								toBeConsideredFlag = 1;
							} else {

							}
						} else {
							toBeConsideredFlag = 1;
						}
						if (toBeConsideredFlag == 1) {

							List<TransactionMasterBO> transactionsBasedOnFolioAndSchemeList = transactionMasterBORepository
									.findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeAndLikeTransType(
											invName, folioNo, rtaCode, fromDate, toDate, "DP");
							List<TransactionMasterBO> transactionsBasedOnFolioAndSchemeListForDR = transactionMasterBORepository
									.findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeAndLikeTransType(
											invName, folioNo, rtaCode, fromDate, toDate, "DR");
							List<TransactionMasterBO> transactionsBasedOnFolioAndSchemeListForDIR = transactionMasterBORepository
									.findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeAndLikeTransType(
											invName, folioNo, rtaCode, fromDate, toDate, "DIR");
							List<TransactionMasterBO> transactionsBasedOnFolioAndSchemeListForDividendReinvested = transactionMasterBORepository
									.findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeAndLikeTransType(
											invName, folioNo, rtaCode, fromDate, toDate, "Dividend Reinvested");

							transactionsBasedOnFolioAndSchemeList.addAll(transactionsBasedOnFolioAndSchemeListForDR);
							transactionsBasedOnFolioAndSchemeList.addAll(transactionsBasedOnFolioAndSchemeListForDIR);
							transactionsBasedOnFolioAndSchemeList
									.addAll(transactionsBasedOnFolioAndSchemeListForDividendReinvested);

							DividendReportColumnNewDTO transactions = new DividendReportColumnNewDTO();
							dividendPayout = 0.0d;
							dividendReinvestment = 0.0d;

							if (transactionsBasedOnFolioAndSchemeList.size() > 0) {

								String isin = isinMasterBO.get(0).getIsin();
								MasterMutualFundETF mutualFundETF = etfRepository.findOne(isin);
								for (TransactionMasterBO tmbo : transactionsBasedOnFolioAndSchemeList) {
									try {
										System.out.println(
												"Folio No + schemeName" + tmbo.getFolioNo() + tmbo.getSchemeName());
										transactions.setFolioNo(tmbo.getFolioNo());
										transactions.setSchemeName(mutualFundETF.getSchemeName());
										if (tmbo.getLookupTransactionRule() != null) {
											switch (tmbo.getLookupTransactionRule().getCode()) {
											case "DP":
												dividendPayout += (Double.parseDouble(tmbo.getTransAmt()));
												totalForDP += (Double.parseDouble(tmbo.getTransAmt()));
												break;
											case "DIR":
												dividendReinvestment += (Double.parseDouble(tmbo.getTransAmt()));
												totalForDR += (Double.parseDouble(tmbo.getTransAmt()));
												break;
											case "Dividend Reinvestment":
												dividendReinvestment += (Double.parseDouble(tmbo.getTransAmt()));
												totalForDR += (Double.parseDouble(tmbo.getTransAmt()));
												break;
											case "DR":
												dividendReinvestment += (Double.parseDouble(tmbo.getTransAmt()));
												totalForDR += (Double.parseDouble(tmbo.getTransAmt()));
												break;
											}
										}

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								transactions
										.setDividendPayout(
												numberFormatter.format(Math.round(dividendPayout)).replace("\u20B9", "")
														.substring(0, numberFormatter
																.format(new BigDecimal(Math.round(dividendPayout)))
																.replace("\u20B9", "").length() - 3));
								transactions.setDividendReinvestment(
										numberFormatter.format(Math.round(dividendReinvestment)).replace("\u20B9", "")
												.substring(0, numberFormatter
														.format(new BigDecimal(Math.round(dividendReinvestment)))
														.replace("\u20B9", "").length() - 3));
								System.out.println("DP + DR" + String.valueOf(FinexaUtil.roundOff((dividendPayout), 2))
										+ String.valueOf(FinexaUtil.roundOff((dividendReinvestment), 2)));
								if (mainNamePanMapforClient.containsKey(invPAN + invName)) {
									transactions.setClientDetails(
											mainNamePanMapforClient.get(invPAN + invName) + "-" + invPAN);

								} else if (mainNamePanMapforFamily.containsKey(invPAN + invName)) {
									transactions.setClientDetails(
											mainNamePanMapforFamily.get(invPAN + invName) + "-" + invPAN);

								}
								srNO++;
								transactions.setSrNo(String.valueOf(srNO));
								dividendTransactionList.add(transactions);
								isDividendFound = true;
								alreadyCountInReport.add(invName.toLowerCase() + invPAN);

							}

						}
					}
					System.out.println(srNO + " " + dividendTransactionList.size());
					if (isDividendFound == true) {
						DividendReportColumnNewDTO totalTransactions = new DividendReportColumnNewDTO();
						totalTransactions.setSchemeName("Total: ");
						totalTransactions.setDividendPayout(
								String.valueOf(numberFormatter.format(Math.round(totalForDP)).replace("\u20B9", "")
										.substring(0, numberFormatter.format(new BigDecimal(Math.round(totalForDP)))
												.replace("\u20B9", "").length() - 3)));
						totalTransactions.setDividendReinvestment(
								String.valueOf(numberFormatter.format(Math.round(totalForDR)).replace("\u20B9", "")
										.substring(0, numberFormatter.format(new BigDecimal(Math.round(totalForDR)))
												.replace("\u20B9", "").length() - 3)));
						dividendTransactionList.add(totalTransactions);
					}

					if (dividendTransactionList != null && dividendTransactionList.size() > 0) {
						dividendFolioAndSchemeMap.put(dividendReportDTO.getNameClient(), dividendTransactionList);

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return dividendFolioAndSchemeMap;
	}

}
