package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.math.BigInteger;
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

import com.finlabs.finexa.dto.AumReportColumnDTO;
import com.finlabs.finexa.dto.AumReportDTO;
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

@Service("AUMTransactionReportBOService")
@Transactional
public class AUMTransactionReportBOServiceImpl implements AUMTransactionReportBOService {

	@Autowired
	private TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	private MasterMutualFundETFRepository masterMutualFundETFRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepository;

	@Autowired
	private MasterMFDailyNAVRepository masterMFDailyNAVRepository;

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd-MM-yyyy");
	public static Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));

	private static final String NULL = "null";

	@Override
	public Map<String, List<AumReportColumnDTO>> aumTransactionReport(AumReportDTO aumReportDTO)
			throws RuntimeException {
		// TODO Auto-generated method stub

		List<String> clientMasterList = new ArrayList<String>();
		Map<String, String> mainFamilyPanMap = new HashMap<String, String>();
		Map<String, String> clientMasterMap = new HashMap<String, String>();
		Map<String, Integer> clientFamilyFlagMap = new HashMap<String, Integer>();
		Map<String, Double> currentValueMap = new HashMap<String, Double>();
		Map<String,List<AumReportColumnDTO>> aumReportColumnDTOMap = new HashMap<String, List<AumReportColumnDTO>>();
		int clientId = aumReportDTO.getClientId();
		ClientMaster cm = clientMasterRepository.findOne(clientId);

		if (cm != null) {
			String name = cm.getFirstName() + (cm.getMiddleName() == null ? "" : (" " + cm.getMiddleName()))
					+ (cm.getLastName() == null ? "" : " " + (cm.getLastName()));
			String pan = cm.getPan();
			clientMasterList.add(name + "-" + pan);
			clientFamilyFlagMap.put(name + "-" + pan, cm.getId());
			mainFamilyPanMap.put(pan, name);
			List<AuxillaryInvestorMaster> auxList = cm.getAuxillaryInvestorMasters();
			for (AuxillaryInvestorMaster obj : auxList) {
				String auxName = obj.getFirstName() + (obj.getMiddleName() == null ? "" : (" " + obj.getMiddleName()))
						+ (obj.getLastName() == null ? "" : (" " + obj.getLastName()));
				String pan1 = obj.getPan();
				if (!clientMasterList.contains(auxName + "-" + pan1)) {
					clientMasterList.add(auxName + "-" + pan1);
					clientFamilyFlagMap.put(auxName + "-" + pan1, obj.getClientMaster().getId());
				}
			}
			List<ClientFamilyMember> familyMemberList = cm.getClientFamilyMembers();
			for (ClientFamilyMember obj : familyMemberList) {
				if (Arrays.asList(aumReportDTO.getFamilyMemberIdArr()).contains(obj.getId())) {
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
							clientFamilyFlagMap.put(auxName + "-" + pan1, obj.getId());
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
								clientFamilyFlagMap.put(auxNameFam + "-" + panFam, aux.getClientFamilyMember().getId());

							}
						}
					}
				}
			}
		}

		System.out.println(clientMasterList);
		List<AumReportColumnDTO> aumReportColumnDTOList = new ArrayList<AumReportColumnDTO>();

		for (String invName : clientMasterList) {
			try {
				String investorName = invName.substring(0, invName.indexOf('-'));
				String investorPAN = invName.substring(invName.indexOf('-') + 1);
				Double totalAUM = 0.0;
				if (clientMasterMap.containsKey(investorName.toLowerCase())) {
					if (clientMasterMap.get(investorName.toLowerCase()).equals(investorPAN)) {
						continue;
					} else {
						clientMasterMap.put(investorName.toLowerCase(), investorPAN);
					}
				} else {
					clientMasterMap.put(investorName.toLowerCase(), investorPAN);
				}

				// Returns the list of DISTINCT set of Folio Number and Scheme Name
				List<Object[]> distinctFolioSchemeRTASet = transactionMasterBORepository
						.getDistinctFolioSchemeSetByInvestorNamePANAndAsOnTransactionDate(investorName, investorPAN,
								aumReportDTO.getAsOnDate());

				for (Object[] distinctFolioSchemeRTA : distinctFolioSchemeRTASet) {
					try {
						String folioNo = (String) distinctFolioSchemeRTA[0];
						String rtaCode = (String) distinctFolioSchemeRTA[1];

						Double currentUnits = 0.0, currentValue = 0.0, zero = 0.0;
						Float currentNAV = 0.0f;
						int toBeConsideredFlag = 0;

						AumReportColumnDTO aumReportColumnDTO = new AumReportColumnDTO();

						List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepository
								.findByCamsCodeAndStatus(rtaCode, "NEW");
						if (isinMasterBO.size() > 0) {
							String isin = isinMasterBO.get(0).getIsin();
							MasterMutualFundETF masterMutualFundETF = masterMutualFundETFRepository.findOne(isin);

							aumReportColumnDTO.setFundName(masterMutualFundETF.getFundHouse());
							aumReportColumnDTO.setFolioNo(folioNo);
							aumReportColumnDTO.setSchemeName(masterMutualFundETF.getSchemeName());

							if (aumReportDTO.getSchemeName() != null && aumReportDTO.getSchemeName() != ""
									&& !aumReportDTO.getSchemeName().equals(NULL)) {
								if (isin.equals(aumReportDTO.getSchemeName())) {
									toBeConsideredFlag = 1;
								}
							} else {
								toBeConsideredFlag = 1;
							}

							if (toBeConsideredFlag == 1) {

							}

							List<TransactionMasterBO> transactionsBasedOnFolioAndSchemeList = transactionMasterBORepository
									.findTransactiondetailsByInvestorNameAndAsOnDateAndFolioAndSchemeRTA(investorName,
											investorPAN, folioNo, rtaCode, aumReportDTO.getAsOnDate());

							for (TransactionMasterBO transactionsBasedOnFolioAndScheme : transactionsBasedOnFolioAndSchemeList) {
								try {
									if (transactionsBasedOnFolioAndScheme.getLookupTransactionRule().getCode() != null) {
										
										switch (transactionsBasedOnFolioAndScheme.getLookupTransactionRule()
												.getCode()) {

										case "Additional Purchase":
										case "ADDPUR":
										case "DIR":
										case "Dividend Reinvestment":
										case "DR":
										case "New Purchase":
										case "NEWPUR":
										case "P":
										case "REDR":
										case "SI":
										case "SIP":
										case "STP In":
										case "STPI":
										case "STPOR":
										case "SWIN":
										case "SWOFR":
										case "TI":
										case "TMI":
											currentUnits += Double
													.parseDouble(transactionsBasedOnFolioAndScheme.getTransUnits());
											break;

										case "ADDPURR":
										case "NEWPURR":
										case "R":
										case "RED":
										case "Redemption":
										case "SIPR":
										case "SO":
										case "STP O":
										case "STP Out":
										case "STPIR":
										case "SWD":
										case "SWINR":
										case "SWOF":
										case "TMO":
										case "TO":
											currentUnits -= Double
													.parseDouble(transactionsBasedOnFolioAndScheme.getTransUnits());
											break;

										case "All others":
										case "DP":
										case "J":
											break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								//System.out.println(currentUnits);
							}

							Date maxDate = masterMFDailyNAVRepository.findMaxDate(isin, aumReportDTO.getAsOnDate());
							MasterMFDailyNAV masterMFDailyNAV = masterMFDailyNAVRepository.findNAV(isin, maxDate);
							currentNAV = masterMFDailyNAV.getNav();
							currentValue = currentUnits * currentNAV;
							if (currentValueMap.get(mainFamilyPanMap.get(investorPAN) + "-" + investorPAN) != null)
								totalAUM = currentValueMap.get(mainFamilyPanMap.get(investorPAN) + "-" + investorPAN);
							totalAUM += currentValue;
							//System.out.println("TOTAL AUM:"+totalAUM);
							System.out.println("RTA:"+rtaCode+", CURRENT VALUE:"+currentValue + ","+ numberFormatter.format(FinexaUtil.roundOff(currentValue, 2)).replace("\u20B9", ""));
							aumReportColumnDTO.setUnits(numberFormatter.format(FinexaUtil.roundOff(currentUnits, 2)).replace("\u20B9", ""));
							aumReportColumnDTO.setCurrentValue(numberFormatter.format(FinexaUtil.roundOff(currentValue, 2)).replace("\u20B9", ""));
							aumReportColumnDTO.setCurrentNav(numberFormatter.format(FinexaUtil.roundOff(currentNAV, 2)).replace("\u20B9", ""));
							if (mainFamilyPanMap.containsKey(investorPAN)) {
								aumReportColumnDTO
										.setClientDetails(mainFamilyPanMap.get(investorPAN) + "-" + investorPAN);
								currentValueMap.put(mainFamilyPanMap.get(investorPAN) + "-" + investorPAN, totalAUM);
							} else {
								aumReportColumnDTO.setClientDetails(investorName);
							}
							aumReportColumnDTO.setTotal(numberFormatter.format(FinexaUtil.roundOff(totalAUM, 2)).replace("\u20B9", ""));
							BigDecimal bigDecimaldata = new BigDecimal(BigDecimal.valueOf(currentUnits).toPlainString());
							/*
							BigDecimal bigDecimaldata = new BigDecimal(BigDecimal.valueOf(currentUnits).toPlainString());
							Long longData = bigDecimaldata.longValueExact();
							*/
							//Long longData = 0l;
							//BigDecimal bigDecimaldata = new BigDecimal(currentUnits.toString());
							//	longData = bigDecimaldata.longValueExact();
							//System.out.println(currentUnits+"BIG DECIMAL*******************************************:"+bigDecimaldata.floatValue()+","+bigDecimaldata.toBigInteger());
							long lonVal = bigDecimaldata.longValue();
							
							if (lonVal > 0) {
								aumReportColumnDTOList.add(aumReportColumnDTO);
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
		if (aumReportColumnDTOList.size() > 0) {
			aumReportColumnDTOMap.put(aumReportDTO.getNameClient(), aumReportColumnDTOList);
		}
		return aumReportColumnDTOMap;
	}
	
}
