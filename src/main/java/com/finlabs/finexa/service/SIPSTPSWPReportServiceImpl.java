package com.finlabs.finexa.service;

import java.math.BigDecimal;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.SIPSTPSWPReportColumnDTO;
import com.finlabs.finexa.dto.SIPSTPSWPReportDTO;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SIPSTPMasterBO;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SIPSTPMasterBORepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;

@Service("SIPSTPSWPReportService")
@Transactional
public class SIPSTPSWPReportServiceImpl implements SIPSTPSWPReportService {

	private static Logger log = LoggerFactory.getLogger(SIPSTPSWPReportServiceImpl.class);
	
	@Autowired
	private SIPSTPMasterBORepository sipSTPMasterBORepository;

	@Autowired
	private MasterMutualFundETFRepository masterMutualFundETFRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepo;

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd-MM-yyyy");
	public Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));

	@SuppressWarnings("deprecation")
	@Override
	public Map<String, List<SIPSTPSWPReportColumnDTO>> sipSTPSWPReport(SIPSTPSWPReportDTO sipSTPSWPReportBODTO)
			throws RuntimeException, ParseException {
		// TODO Auto-generated method stub

		String scheme, folioNo, fundHouse, fundHouseName;
		Date fromDate, toDate;
		Date asOnDate = new Date();
		boolean sip = false, stp = false, swp = false, isSchemeSelected = false;
		List<Object[]> distinctSchemeNameList = new ArrayList<Object[]>();
		List<Object[]> distinctSchemeNameListSIP = new ArrayList<Object[]>();
		List<Object[]> distinctSchemeNameListSTP = new ArrayList<Object[]>();
		List<Object[]> distinctSchemeNameListSWP = new ArrayList<Object[]>();
		List<String> clientAlreadySearchedList = new ArrayList<>();
		List<SIPSTPMasterBO> transactionDetailsList = new ArrayList<SIPSTPMasterBO>();
		List<String> clientMasterList = new ArrayList<String>();
		Map<String, String> clientNamePANMap = new HashMap<String, String>();
		Map<String, List<SIPSTPSWPReportColumnDTO>> sipSTPSWPMap = new HashMap<String, List<SIPSTPSWPReportColumnDTO>>();

		if (sipSTPSWPReportBODTO.getReportType().contains(FinexaConstant.MF_REPORT_TYPE_SIP))
			sip = true;
		if (sipSTPSWPReportBODTO.getReportType().contains(FinexaConstant.MF_REPORT_TYPE_STP))
			stp = true;
		if (sipSTPSWPReportBODTO.getReportType().contains(FinexaConstant.MF_REPORT_TYPE_SWP))
			swp = true;

		fromDate = sipSTPSWPReportBODTO.getFromDate();
		toDate = sipSTPSWPReportBODTO.getToDate();

		Integer noOfInstallment = 0, index = 0;
		String installmentType;
		FinexaUtil finexaUtil = new FinexaUtil();

		int clientId = sipSTPSWPReportBODTO.getClientId();
		ClientMaster cm = clientMasterRepository.findOne(clientId);
		Map<String, String> mainFamilyPanMap = new HashMap<String, String>();

		if (cm != null) {
			// System.out.println(cm.getFirstName());
			// System.out.println(cm.getLastName());
			// System.out.println(cm.getMiddleName());

			String name = cm.getFirstName() + (cm.getMiddleName() == null ? "" : (" " + cm.getMiddleName()))
					+ (cm.getLastName() == null ? "" : " " + (cm.getLastName()));

			String pan = cm.getPan();
			clientMasterList.add(name + "-" + pan);
			mainFamilyPanMap.put(pan, name);
			clientNamePANMap.put(name.trim()+"-"+pan.trim(), name.trim());
			List<AuxillaryInvestorMaster> auxList = cm.getAuxillaryInvestorMasters();
			for (AuxillaryInvestorMaster obj : auxList) {
				String auxName = obj.getFirstName() + (obj.getMiddleName() == null ? "" : (" " + obj.getMiddleName()))
						+ (obj.getLastName() == null ? "" : (" " + obj.getLastName()));
				String pan1 = obj.getPan();
				if (!clientMasterList.contains(auxName + "-" + pan1)) {
					clientMasterList.add(auxName + "-" + pan1);
				}
				clientNamePANMap.put(auxName.trim()+"-"+pan1.trim(), name.trim());
			}
			List<ClientFamilyMember> familyMemberList = cm.getClientFamilyMembers();
			for (ClientFamilyMember obj : familyMemberList) {
				if (Arrays.asList(sipSTPSWPReportBODTO.getFamilyMemberIdArr()).contains(obj.getId())) {
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
		System.out.println("CLIENT MASTER: " + clientMasterList);
		System.out.println("clientMasterList size(): " + clientMasterList.size());
		for (Map.Entry<String,String> entry : clientNamePANMap.entrySet())  
            System.out.println("Key = " + entry.getKey() + 
                             ", Value = " + entry.getValue()); 
		System.out.println(sipSTPSWPReportBODTO.getSchemeName());
		if(sipSTPSWPReportBODTO.getSchemeName() != null && sipSTPSWPReportBODTO.getSchemeName().trim() != "" && !sipSTPSWPReportBODTO.getSchemeName().trim().isEmpty() && !sipSTPSWPReportBODTO.getSchemeName().equalsIgnoreCase("null")) {
			isSchemeSelected = true;
		}
		try {
			List<SIPSTPSWPReportColumnDTO> sipSTPSWPList = new ArrayList<SIPSTPSWPReportColumnDTO>();

			for (String clientMasterNamePan : clientMasterList) {
				try {
					if(clientAlreadySearchedList.size() > 0) {
						if(clientAlreadySearchedList.contains(clientMasterNamePan.toLowerCase())) {
							continue;
						} else {
							clientAlreadySearchedList.add(clientMasterNamePan.toLowerCase());
						}
					} else {
						clientAlreadySearchedList.add(clientMasterNamePan.toLowerCase());
					}
					// List<SIPSTPSWPReportColumnDTO> sipSTPSWPList = new
					// ArrayList<SIPSTPSWPReportColumnDTO>();
					String invName = clientMasterNamePan.substring(0, clientMasterNamePan.indexOf('-'));
					invName = invName.trim();
					String invPAN = clientMasterNamePan.substring(clientMasterNamePan.indexOf('-') + 1);
					invPAN = invPAN.trim();
					System.out.println("FROMDATE: " + fromDate);
					System.out.println("TODATE: " + toDate);
					// Gets list of distinct scheme names and folio number of a specific investor
					/*
					 * if(sip) { distinctSchemeNameListSIP = sipSTPMasterBORepository.
					 * getDistinctFolioSchemeSetByInvestorNameAndRegistrationDate(invName,invPAN,
					 * fromDate, toDate, "SIP");
					 * 
					 * if(distinctSchemeNameListSIP.size() > 0)
					 * distinctSchemeNameList.addAll(distinctSchemeNameListSIP); } if(stp) {
					 * distinctSchemeNameListSTP = sipSTPMasterBORepository
					 * .getDistinctFolioSchemeSetByInvestorNameAndRegistrationDate(invName,invPAN,
					 * fromDate, toDate, "STP");
					 * 
					 * if(distinctSchemeNameListSTP.size() > 0)
					 * distinctSchemeNameList.addAll(distinctSchemeNameListSTP); } if(swp) {
					 * distinctSchemeNameListSWP = sipSTPMasterBORepository
					 * .getDistinctFolioSchemeSetByInvestorNameAndRegistrationDate(invName,invPAN,
					 * fromDate, toDate, "SWP");
					 * 
					 * if(distinctSchemeNameListSWP.size() > 0)
					 * distinctSchemeNameList.addAll(distinctSchemeNameListSWP); }
					 */
					if (sip) {
						distinctSchemeNameListSIP = sipSTPMasterBORepository
								.getDistinctFolioSchemeSetByInvestorNameAndStartDate(invName, invPAN, fromDate, toDate,
										"SIP");

						if (distinctSchemeNameListSIP.size() > 0)
							distinctSchemeNameList.addAll(distinctSchemeNameListSIP);
					}
					if (stp) {
						distinctSchemeNameListSTP = sipSTPMasterBORepository
								.getDistinctFolioSchemeSetByInvestorNameAndStartDate(invName, invPAN, fromDate, toDate,
										"STP");

						if (distinctSchemeNameListSTP.size() > 0)
							distinctSchemeNameList.addAll(distinctSchemeNameListSTP);
					}
					if (swp) {
						distinctSchemeNameListSWP = sipSTPMasterBORepository
								.getDistinctFolioSchemeSetByInvestorNameAndStartDate(invName, invPAN, fromDate, toDate,
										"SWP");

						if (distinctSchemeNameListSWP.size() > 0)
							distinctSchemeNameList.addAll(distinctSchemeNameListSWP);
					}
					if (distinctSchemeNameList.size() > 0) {
						// Iterates to calculate on each set of scheme names and folio number of a
						// specific investor
						for (Object[] ob : distinctSchemeNameList) {
							try {

								folioNo = (String) ob[0];
								scheme = (String) ob[1];
								// index = scheme.indexOf(' ');
								// fundHouse = scheme.substring(0, index);
								// fundHouseName =
								// masterMutualFundETFRepository.getParticularFundHouseFromScheme(fundHouse +
								// "%");
								List<SchemeIsinMasterBO> schemeIsinMasterBOList = schemeIsinMasterBORepo
										.findByCamsCodeAndStatus(scheme, "NEW");
								if (schemeIsinMasterBOList.size() > 0) {
									MasterMutualFundETF masterMutualFundETF = masterMutualFundETFRepository
											.findByIsin(schemeIsinMasterBOList.get(0).getIsin());
									if (masterMutualFundETF != null) {
										if(isSchemeSelected == true) {
											if(!masterMutualFundETF.getIsin().equals(sipSTPSWPReportBODTO.getSchemeName())) 
												continue;
										}
										System.out.println("ISIN: " + schemeIsinMasterBOList.get(0).getIsin() + ","
												+ masterMutualFundETF.getIsin() + ","
												+ masterMutualFundETF.getSchemeName());
										transactionDetailsList = sipSTPMasterBORepository
												.findByNameFolioSchemeAndStartDate(invName, folioNo, scheme, fromDate,
														toDate);

										for (SIPSTPMasterBO transactionDetails : transactionDetailsList) {
											try {
												SIPSTPSWPReportColumnDTO sipSTPSWPReportColumnBODTO = new SIPSTPSWPReportColumnDTO();
												installmentType = transactionDetails.getFrequency();

												switch (installmentType.toUpperCase()) {
												/*
												 * case "OM": case "MONTHLY": noOfInstallment =
												 * finexaUtil.monthCalculator(transactionDetails.getFromDate(),
												 * transactionDetails.getToDate());
												 * System.out.println("Number of Installments: "+noOfInstallment);
												 * break;
												 */
												case "OM":
												case "MONTHLY":
													//FinexaUtil finexaUtil = new FinexaUtil();
													//noOfInstallment = (numberOfDaysBetweenTwoDate(transactionDetails.getToDate(), asOnDate)) / 30;
													noOfInstallment = finexaUtil.monthCalculator(transactionDetails.getToDate(), asOnDate);
													Calendar cal = Calendar.getInstance();
													int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
													System.out.println("DAY:"+dayOfMonth);
													if(dayOfMonth > 10) {
														noOfInstallment -= 1;
													}
													System.out.println("Number of Installments: " + noOfInstallment);
													break;
												case "WEEKLY":
													noOfInstallment = (numberOfDaysBetweenTwoDate(
															transactionDetails.getToDate(),
															asOnDate)) / 7;
													System.out.println("Number of Installments: " + noOfInstallment);
													break;
												case "QUARTERLY":
													noOfInstallment = (numberOfDaysBetweenTwoDate(
															transactionDetails.getToDate(),
															asOnDate)) / 90;
													System.out.println("Number of Installments: " + noOfInstallment);
													break;
												case "FORTNIGHTLY":
													noOfInstallment = (numberOfDaysBetweenTwoDate(
															transactionDetails.getToDate(),
															asOnDate)) / 14;
													System.out.println("Number of Installments: " + noOfInstallment);
													break;
												case "DAILY":
													noOfInstallment = numberOfDaysBetweenTwoDate(
															transactionDetails.getToDate(),
															asOnDate);
													System.out.println("Number of Installments: " + noOfInstallment);
													break;
												default:
													System.out.println(
															"INSTALLMENT TYPE IS NOT MATCHING WITH SWITCH CASE");
													break;

												}

												if (transactionDetails.getTerminationDate() == null) {
													sipSTPSWPReportColumnBODTO.setActiveInactive("Active");
												} else {
													// check if termination date > current date then inactive else
													// active
													sipSTPSWPReportColumnBODTO.setActiveInactive("Inactive");
												}

												if (transactionDetails.getTransactionType().equals("STP")) {
													if(transactionDetails.getTargetSchemeRTACode() != null 
														&& transactionDetails.getTargetSchemeRTACode().trim() != "" 
														&& !transactionDetails.getTargetSchemeRTACode().trim().isEmpty()) {
														List<SchemeIsinMasterBO> schemeISINBOList = schemeIsinMasterBORepo.findByCamsCodeAndStatus(transactionDetails.getTargetSchemeRTACode(), "NEW");
														if(schemeISINBOList != null && schemeISINBOList.size() > 0) {
															MasterMutualFundETF masterMFETF = masterMutualFundETFRepository.findByIsin(schemeISINBOList.get(0).getIsin());
															sipSTPSWPReportColumnBODTO.setTargetScheme(masterMFETF.getSchemeName());
														} else {
															log.debug("SCHEME ISIN MAPPING NOT FOUND FOR RTA CODE: "+transactionDetails.getTargetSchemeRTACode());
														}
													} else												
														sipSTPSWPReportColumnBODTO
															.setTargetScheme(transactionDetails.getTargetScheme());
												} else {
													sipSTPSWPReportColumnBODTO.setTargetScheme(" ");
												}

												sipSTPSWPReportColumnBODTO.setFolioNumber(folioNo);
												sipSTPSWPReportColumnBODTO
														.setFundName(masterMutualFundETF.getFundHouse());
												sipSTPSWPReportColumnBODTO.setClientDetails(clientNamePANMap.get(clientMasterNamePan) + " - " + invPAN);
												System.out.println("CLIENT DETAILS: "
														+ sipSTPSWPReportColumnBODTO.getClientDetails());
												sipSTPSWPReportColumnBODTO
														.setSchemeName(masterMutualFundETF.getSchemeName());
												System.out.println(sipSTPSWPReportColumnBODTO
														.getFolioDetails());
												sipSTPSWPReportColumnBODTO
														.setTransactionType(transactionDetails.getTransactionType());
												sipSTPSWPReportColumnBODTO.setFrequency(transactionDetails
														.getFrequency().toLowerCase().substring(0, 1).toUpperCase()
														+ transactionDetails.getFrequency().toLowerCase().substring(1,
																transactionDetails.getFrequency().length()));

												if (transactionDetails.getRegistrationDate() != null) {
													sipSTPSWPReportColumnBODTO.setRegistrationDate(formatterDisplay
															.format(transactionDetails.getRegistrationDate()));
												}

												sipSTPSWPReportColumnBODTO.setTransactionNumber(
														transactionDetails.getTransactionNumber());
												sipSTPSWPReportColumnBODTO.setFromDate(
														formatterDisplay.format(transactionDetails.getFromDate()));
												sipSTPSWPReportColumnBODTO.setToDate(
														formatterDisplay.format(transactionDetails.getToDate()));
												sipSTPSWPReportColumnBODTO.setAmount(numberFormatter
														.format(new BigDecimal(transactionDetails.getAmount()))
														.replace("\u20B9", "")
														.substring(0, numberFormatter
																.format(new BigDecimal(transactionDetails.getAmount()))
																.replace("\u20B9", "").length() - 3));
												//System.out.println("INS: "+noOfInstallment);
												// String installment =
												/*
												if(noOfInstallment < 0)
													noOfInstallment = 0;
												*/	
												if(asOnDate.after(transactionDetails.getToDate()) && noOfInstallment != 0) {
													sipSTPSWPReportColumnBODTO
													.setInstallmentsLeft("-"+(noOfInstallment).toString());
												} else {
													sipSTPSWPReportColumnBODTO
													.setInstallmentsLeft((noOfInstallment).toString());
												}										

												sipSTPSWPList.add(sipSTPSWPReportColumnBODTO);

											} catch (Exception e) {

												e.printStackTrace();
											}
										}
									}

								}

							} catch (Exception e) {

								e.printStackTrace();
							}
						}
					}
					/*
					 * if (sipSTPSWPList.size() > 0) sipSTPSWPMap.put(invName + " - " + invPAN,
					 * sipSTPSWPList);
					 */

				} catch (Exception e) {

					e.printStackTrace();
				}

			}
			if (sipSTPSWPList.size() > 0)
				sipSTPSWPMap.put("Key", sipSTPSWPList);
		} catch (Exception e) {

			e.printStackTrace();
		}
		System.out.println("BEFORE RETURN: ");
		System.out.println(sipSTPSWPMap);
		return sipSTPSWPMap;
	}

	int numberOfDaysBetweenTwoDate(Date startDate, Date endDate) {
		int daysBetween = 0;
		try {
			long difference = startDate.getTime() - endDate.getTime();
			daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
			/*
			 * You can also convert the milliseconds to days using this method float
			 * daysBetween = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
			 */
			System.out.println("Number of Days between dates: " + daysBetween);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return daysBetween;

	}

	
}
