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

import com.finlabs.finexa.dto.DividendReportColumnDTO;
import com.finlabs.finexa.dto.DividendReportDTO;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.util.FinexaUtil;

@Service("DividendReportsService")
@Transactional
public class DividendReportBOServiceImpl implements DividendReportBOService {

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private MasterMutualFundETFRepository masterMutualFundETFRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd-MM-yyyy");
	

	@Override
	public Map<String, List<DividendReportColumnDTO>> dividendReport(DividendReportDTO dividendReportDTO)
			throws RuntimeException, ParseException {
		// TODO Auto-generated method stub

		double divPerUnit;
		String fundHouse, fundHouseName;
		int clientId;
		// String invName = dividendReportDTO.getNameClient();

		Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));

		Date fromDate = dividendReportDTO.getFromDate();
		Date toDate = dividendReportDTO.getToDate();

		List<String> clientMasterList = new ArrayList<String>();
		// Stores each distinct pair of Folio Number and Scheme Name as String and it's
		// list of transactions as List
		Map<String, List<DividendReportColumnDTO>> dividendFolioAndSchemeMap = new HashMap<String, List<DividendReportColumnDTO>>();

		// Returns the list of DISTINCT set of Folio Number and Scheme Name
		List<Object[]> distinctFolioSchemeList = new ArrayList<Object[]>();

		clientId = dividendReportDTO.getClientId();

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
				if(Arrays.asList(dividendReportDTO.getFamilyMemberIdArr()).contains(obj.getId())) {
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
		System.out.println("CLIENT MASTER: " + clientMasterList);
		System.out.println("clientMasterList size(): " + clientMasterList.size());

		try {
			List<DividendReportColumnDTO> dividendTransactionList = new ArrayList<DividendReportColumnDTO>();
			for (String clientMasterNamePan : clientMasterList) {
				try {
					String invName = clientMasterNamePan.substring(0, clientMasterNamePan.indexOf('-'));
					invName = invName.trim();
					String invPAN = clientMasterNamePan.substring(clientMasterNamePan.indexOf('-') + 1);
					invPAN = invPAN.trim();
					System.out.println("FROMDATE: " + fromDate);
					System.out.println("TODATE: " + toDate);
					distinctFolioSchemeList = transactionMasterBORepository
							.getDistinctFolioSchemeSetByInvestorNamePANAndTransactionDate(invName, invPAN, fromDate,
									toDate);

					for (Object[] distinctFolioScheme : distinctFolioSchemeList) {
						try {

							String folioNo = (String) distinctFolioScheme[0];
							String schemeName = (String) distinctFolioScheme[1];

							// Returns the list of all transactions based on a set of Folio Number and
							// Scheme Name
							List<TransactionMasterBO> transactionsBasedOnFolioAndSchemeList = transactionMasterBORepository
									.findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeAndTransType(
											invName, folioNo, schemeName, fromDate, toDate, "DP");

							// Stores list of transactions of each distinct pair of Folio Number and Scheme
							// Name
							// List<DividendReportColumnDTO> transactionList = new
							// ArrayList<DividendReportColumnDTO>();

							for (TransactionMasterBO tmbo : transactionsBasedOnFolioAndSchemeList) {
								try {
									DividendReportColumnDTO transactions = new DividendReportColumnDTO();

									transactions.setFolioNo(tmbo.getFolioNo());
									int i = schemeName.indexOf(' ');
									fundHouse = schemeName.substring(0, i);
									fundHouseName = masterMutualFundETFRepository
											.getParticularFundHouseFromScheme(fundHouse + "%");
									transactions.setFundName(fundHouseName);
									transactions.setFolioDetails(folioNo + " - " + schemeName);
									transactions.setTransAmt(numberFormatter
											.format(new BigDecimal(
													FinexaUtil.roundOff(Double.parseDouble(tmbo.getTransAmt()), 3)))
											.replace("\u20B9", "").substring(0, numberFormatter
											.format(new BigDecimal(
													FinexaUtil.roundOff(Double.parseDouble(tmbo.getTransAmt()), 3)))
											.replace("\u20B9", "").length() - 3));
									transactions.setTransactionDate(formatterDisplay.format(tmbo.getTransactionDate()));
									transactions.setTransactionType(tmbo.getLookupTransactionRule().getDescription());
									transactions.setTransUnits(numberFormatter
											.format(FinexaUtil.roundOff(Double.parseDouble(tmbo.getTransUnits()), 3))
											.replace("\u20B9", ""));
									divPerUnit = (Double.parseDouble(tmbo.getTransAmt())
											/ Double.parseDouble(tmbo.getTransUnits()));
									transactions.setDividendPerUnit(
											numberFormatter.format(FinexaUtil.roundOff(divPerUnit, 3)).replace("\u20B9", ""));
									transactions.setClientDetails(invName+" - "+invPAN);
									dividendTransactionList.add(transactions);
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
			}
			
			if (dividendTransactionList != null && dividendTransactionList.size() > 0) {
				dividendFolioAndSchemeMap.put("Some Key", dividendTransactionList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println("MAP SIZE: "+transactionsBasedOnFolioAndSchemeMap.size());
		return dividendFolioAndSchemeMap;
	}

}
