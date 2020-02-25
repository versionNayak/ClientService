package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
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

import com.finlabs.finexa.dto.InactiveClientReportColumnDTO;
import com.finlabs.finexa.dto.InactiveClientReportDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.resources.model.GainCalculator;
import com.finlabs.finexa.resources.service.GainCalculatorBOService;
import com.finlabs.finexa.util.FinexaBOColumnConstant;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;

@Service("BackOfficeInactiveClientService")
@Transactional
public class InactiveClientReportBOServiceImpl implements InactiveClientReportService {

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private MasterMutualFundETFRepository masterMutualFundETFRepository;

	@Autowired
	private MasterMFDailyNAVRepository masterMFDailyNAVRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepository;

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd-MM-yyyy");
	Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));
	List<String> clientNameListFromTransactionMaster = new ArrayList<String>();
	Map<String, List<TransactionMasterBO>> inactiveCleintNamePANFolioSchemeRTACodeMap = new HashMap<String, List<TransactionMasterBO>>();

	@Override
	public List<InactiveClientReportColumnDTO> inactiveClientReport(InactiveClientReportDTO inactiveClientReportDTO)
			throws RuntimeException, ParseException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		// TODO Auto-generated method stub

		String folioNo, fundHouseName, rtaCode;
		Date fromDate, toDate, transStartDate, today;
		boolean noPurchase = false, noSale = false, flag = false;
		int caseNumber = 0;
		GainCalculatorBOService gc = new GainCalculatorBOService();
		List<InactiveClientReportColumnDTO> inactiveClientDetailsList = new ArrayList<InactiveClientReportColumnDTO>();
		fromDate = inactiveClientReportDTO.getFromDate();
		toDate = inactiveClientReportDTO.getToDate();
		List<TransactionMasterBO> transactionMasterBOList = new ArrayList<>();
		today = new Date();

		if (inactiveClientReportDTO.getReportType().contains(FinexaConstant.MF_REPORT_IC_NO_PURCHASE))
			noPurchase = true;
		if (inactiveClientReportDTO.getReportType().contains(FinexaConstant.MF_REPORT_IC_NO_SALE))
			noSale = true;

		if (noPurchase == true && noSale == false)
			caseNumber = 1;
		else if (noPurchase == false && noSale == true)
			caseNumber = 2;
		else
			caseNumber = 3;

		try {

			List<String> clientNameListFromClientMaster = new ArrayList<String>();
			AdvisorUser advUser = advisorUserRepository.findOne(inactiveClientReportDTO.getAdvisorId());
			List<ClientMaster> cmList = clientMasterRepository.findByAdvisorUserAndActiveFlag(advUser, "Y");
			Map<String, String> mainFamilyPanMap = new HashMap<String, String>();
			for (ClientMaster clientMasterObj : cmList) {

				String name = clientMasterObj.getFirstName()
						+ (clientMasterObj.getMiddleName() == null ? "" : (" " + clientMasterObj.getMiddleName()))
						+ (clientMasterObj.getLastName() == null ? "" : " " + (clientMasterObj.getLastName()));
				String pan = clientMasterObj.getPan();
				clientNameListFromClientMaster.add(name + "-" + pan);
				mainFamilyPanMap.put(pan, name);
				List<AuxillaryInvestorMaster> auxList = clientMasterObj.getAuxillaryInvestorMasters();
				for (AuxillaryInvestorMaster obj : auxList) {
					String auxName = obj.getFirstName()
							+ (obj.getMiddleName() == null ? "" : (" " + obj.getMiddleName()))
							+ (obj.getLastName() == null ? "" : (" " + obj.getLastName()));
					String pan1 = obj.getPan();
					if (!clientNameListFromClientMaster.contains(auxName + "-" + pan1)) {
						clientNameListFromClientMaster.add(auxName + "-" + pan1);
					}
				}
				List<ClientFamilyMember> familyMemberList = clientMasterObj.getClientFamilyMembers();
				for (ClientFamilyMember obj : familyMemberList) {
					String auxName = obj.getFirstName()
							+ (obj.getMiddleName() == null ? "" : (" " + obj.getMiddleName()))
							+ (obj.getLastName() == null ? "" : (" " + obj.getLastName()));
					String pan1 = obj.getPan();
					if (pan1 == null || pan1.equals("")) {
						if (!clientNameListFromClientMaster.contains(auxName + "-NA")) {
							clientNameListFromClientMaster.add(auxName + "-NA");
						}
					} else {
						if (!clientNameListFromClientMaster.contains(auxName + "-" + pan1)) {
							clientNameListFromClientMaster.add(auxName + "-" + pan1);
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
							if (!clientNameListFromClientMaster.contains(auxNameFam + "-NA")) {
								clientNameListFromClientMaster.add(auxNameFam + "-NA");
							}
						} else {
							if (!clientNameListFromClientMaster.contains(auxNameFam + "-" + panFam)) {
								clientNameListFromClientMaster.add(auxNameFam + "-" + panFam);
							}
						}
					}

				}
			}

			transactionMasterBOList = transactionMasterBORepository.findByAdvisorUserOrderByTransactionDate(advUser);

			switch (caseNumber) {
			case 1:
				for (TransactionMasterBO tmbo : transactionMasterBOList) {
					if (FinexaBOColumnConstant.DIVIDEND_LIST.contains(tmbo.getLookupTransactionRule().getCode())) {
						continue;
					} else if (FinexaBOColumnConstant.PURCHASE_LIST
							.contains(tmbo.getLookupTransactionRule().getCode())) {
						if (tmbo.getTransactionDate().before(inactiveClientReportDTO.getFromDate())
								|| tmbo.getTransactionDate().after(inactiveClientReportDTO.getToDate())) {

							inactiveClientMapping(tmbo);
						} else {

							inactiveClientMapping(tmbo);
						}

					}
				}
				break;

			case 2:
				for (TransactionMasterBO tmbo : transactionMasterBOList) {
					if (FinexaBOColumnConstant.DIVIDEND_LIST.contains(tmbo.getLookupTransactionRule().getCode())) {
						continue;
					} else if (FinexaBOColumnConstant.SALE_LIST.contains(tmbo.getLookupTransactionRule().getCode())) {
						if (tmbo.getTransactionDate().before(inactiveClientReportDTO.getFromDate())
								|| tmbo.getTransactionDate().after(inactiveClientReportDTO.getToDate())) {

							inactiveClientMapping(tmbo);
						} else {

							inactiveClientMapping(tmbo);
						}

					}
				}
				break;

			case 3:
				for (TransactionMasterBO tmbo : transactionMasterBOList) {

					if (FinexaBOColumnConstant.PURCHASE_LIST.contains(tmbo.getLookupTransactionRule().getCode())
							|| FinexaBOColumnConstant.SALE_LIST.contains(tmbo.getLookupTransactionRule().getCode())) {
						if (tmbo.getTransactionDate().before(inactiveClientReportDTO.getFromDate())
								|| tmbo.getTransactionDate().after(inactiveClientReportDTO.getToDate())) {

							inactiveClientMapping(tmbo);
						}
					} else if (FinexaBOColumnConstant.DIVIDEND_LIST
							.contains(tmbo.getLookupTransactionRule().getCode())) {
						continue;
					}
				}
				break;
			}

			System.out.println(clientNameListFromClientMaster.size());
			System.out.println(clientNameListFromTransactionMaster.size());

			clientNameListFromClientMaster.retainAll(clientNameListFromTransactionMaster);
			System.out.println(clientNameListFromClientMaster.size());

			for (Map.Entry<String, List<TransactionMasterBO>> entry : inactiveCleintNamePANFolioSchemeRTACodeMap
					.entrySet()) {
				try {
					String value[] = entry.getKey().split("-");

					int toBeConsideredFlag = 0;

					folioNo = (String) value[2];
					rtaCode = (String) value[3];
					if (inactiveClientReportDTO.getSchemeName() != null
							&& inactiveClientReportDTO.getSchemeName() != "") {
						List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepository
								.findByCamsCodeAndStatus(rtaCode, "NEW");
						String camsCode = isinMasterBO.get(0).getCamsCode();
						if (camsCode == rtaCode) {
							toBeConsideredFlag = 1;
						}
					} else {
						toBeConsideredFlag = 1;
					}

					if (toBeConsideredFlag == 1) {

						List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepository
								.findByCamsCodeAndStatus(rtaCode, "NEW");

						if (isinMasterBO != null && isinMasterBO.size() > 0) {

							String isin = isinMasterBO.get(0).getIsin();
							MasterMutualFundETF etf = masterMutualFundETFRepository.findOne(isin);
							fundHouseName = etf.getFundHouse();

							InactiveClientReportColumnDTO inactiveClientReportColumnDTO = new InactiveClientReportColumnDTO();

							double balanceUnits = 0.00, currentValue = 0.00;
							Double currentNav = 0.00;

							MasterMFDailyNAV dailyNav = masterMFDailyNAVRepository
									.findTopByIdMasterMutualFundEtfIsinOrderByIdDateDesc(isin);
							currentNav = (double) dailyNav.getAdjNav();

							// Stores the list of results by gain calculator
							List<GainCalculator> list = gc.getTransationDetails(value[0], folioNo, rtaCode,
									entry.getValue().get(0).getTransactionDate(), today);

							for (GainCalculator gainCalculator : list) {

								flag = true;
								balanceUnits = gainCalculator.getBalanceQuantity();
								if (gainCalculator.getSellQuantity() > 0) {
								}
							}

							if (FinexaUtil.roundOff(balanceUnits, 3) > 0) {

								currentValue = balanceUnits * currentNav;

								inactiveClientReportColumnDTO.setFundName(fundHouseName);
								inactiveClientReportColumnDTO.setFolioNo(folioNo);
								inactiveClientReportColumnDTO.setSchemeName(etf.getSchemeName());
								inactiveClientReportColumnDTO.setUnits(
										numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(balanceUnits, 2)))
												.replace("\u20B9", ""));
								Double nav = FinexaUtil.roundOff(currentNav, 1);
								inactiveClientReportColumnDTO.setCurrentNAV(nav.toString());
								inactiveClientReportColumnDTO.setCurrentValue(numberFormatter
										.format(new BigDecimal(Math.round(currentValue))).replace("\u20B9", "")
										.substring(0, numberFormatter.format(new BigDecimal(Math.round(currentValue)))
												.replace("\u20B9", "").length() - 3));
								if (mainFamilyPanMap.containsKey(value[1])) {

									inactiveClientReportColumnDTO
											.setClientName(mainFamilyPanMap.get(value[1]) + "-" + value[1]);
								} else {
									inactiveClientReportColumnDTO.setClientName(value[0]);
								}
								inactiveClientReportColumnDTO.setClientName(value[0]);
								// Set of one Mutual fund scheme's calculated values
								inactiveClientDetailsList.add(inactiveClientReportColumnDTO);

							}
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		if (inactiveClientDetailsList.size() > 0) {
			return inactiveClientDetailsList;
		} else {
			inactiveClientDetailsList = new ArrayList<InactiveClientReportColumnDTO>();
			return null;
		}

	}

	void inactiveClientMapping(TransactionMasterBO tmbo) {
		String name = tmbo.getInvestorName().trim();
		String pan = tmbo.getInvestorPan().trim();
		String folio = tmbo.getFolioNo().trim();
		String schemeRTACode = tmbo.getSchemeRTACode().trim();
		String key = name + "-" + pan + "-" + folio + "-" + schemeRTACode;

		List<TransactionMasterBO> transMasterList;
		if (inactiveCleintNamePANFolioSchemeRTACodeMap.size() > 0) {
			transMasterList = inactiveCleintNamePANFolioSchemeRTACodeMap.get(key);
			if (transMasterList != null && transMasterList.size() > 0) {
				transMasterList.add(tmbo);
				clientNameListFromTransactionMaster.add(name + "-" + pan);
				inactiveCleintNamePANFolioSchemeRTACodeMap.put(key, transMasterList);
			} else {
				transMasterList = new ArrayList<TransactionMasterBO>();
				transMasterList.add(tmbo);
				clientNameListFromTransactionMaster.add(name + "-" + pan);
				inactiveCleintNamePANFolioSchemeRTACodeMap.put(key, transMasterList);
			}
		} else {
			transMasterList = new ArrayList<TransactionMasterBO>();
			transMasterList.add(tmbo);
			clientNameListFromTransactionMaster.add(name + "-" + pan);
			inactiveCleintNamePANFolioSchemeRTACodeMap.put(key, transMasterList);
		}
	}
}
