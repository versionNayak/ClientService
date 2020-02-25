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

import com.finlabs.finexa.dto.BrokerageReportColumnDTO;
import com.finlabs.finexa.dto.BrokerageReportDTO;
import com.finlabs.finexa.dto.TransactionReportColumnDTO;
import com.finlabs.finexa.dto.TransactionReportDTO;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.BrokerageMasterBO;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.BrokerageMasterBORepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.repository.StagingFolioMasterBORepository;
import com.finlabs.finexa.repository.StagingInvestorMasterBORepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.util.FinexaUtil;

@Service("BrokerageReportsService")
@Transactional
public class BrokerageReportServiceImpl implements BrokerageReportService {

	@Autowired
	private BrokerageMasterBORepository brokerageMasterBORepository;

	@Autowired
	private MasterMFDailyNAVRepository masterMFDailyNAVRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private MasterMutualFundETFRepository etfRepository;

	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepo;
	
	@Autowired
	private StagingFolioMasterBORepository stagingFolioMasterBORepository;
	
	@Autowired
	private StagingInvestorMasterBORepository stagingInvestorMasterBORepository;

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public Map<String, List<BrokerageReportColumnDTO>> brokerageReport(BrokerageReportDTO brokerageReportDTO)
			throws RuntimeException, ParseException {

		// TODO Auto-generated method stub
		Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));

		String currentNAV = "";
		String currentValue = "";

		Date fromDate = brokerageReportDTO.getFromDate();
		Date toDate = brokerageReportDTO.getToDate();

		// Stores each distinct pair of Folio Number and Scheme Name as String and it's
		// list of transactions as List
		Map<String, List<BrokerageReportColumnDTO>> brokerageBasedOnFolioAndSchemeMap = new HashMap<String, List<BrokerageReportColumnDTO>>();
		List<BrokerageReportColumnDTO> brokerageList = new ArrayList<BrokerageReportColumnDTO>();
		List<String> clientMasterList = new ArrayList<String>();
		List<String> folioOfStagingList;
		List<String> folioOfBrokerageList;
		List<Integer> investorIdOfStagingInvestorList;
		List<Object[]> distinctFolioAndSchemeList;
		Map<String, String> mainFamilyPanMap = new HashMap<String, String>();
		Map<String, String> clientNamePANMap = new HashMap<String, String>();
		try {
			int clientId = brokerageReportDTO.getClientId();
			System.out.println("CLIENT ID: "+clientId);
			System.out.println("ADVISOR ID: "+brokerageReportDTO.getAdvisorId());

			ClientMaster cm = clientMasterRepository.findOne(clientId);

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
					clientNamePANMap.put(auxName.trim()+"-"+pan.trim(), name.trim());
				}

				List<ClientFamilyMember> familyMemberList = cm.getClientFamilyMembers();
				for (ClientFamilyMember obj : familyMemberList) {
					if(Arrays.asList(brokerageReportDTO.getFamilyMemberIdArr()).contains(obj.getId())) {
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
								clientNamePANMap.put(auxName.trim()+"-"+pan.trim(), auxName.trim());
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * for(Map.Entry<String, String> entryMap : mainFamilyPanMap.entrySet()) {
		 * System.out.println(entryMap.getKey() + " ----- " + entryMap.getValue()); }
		 */
		System.out.println(clientMasterList);
		for (Map.Entry<String,String> entry : clientNamePANMap.entrySet())  
            System.out.println("Key = " + entry.getKey() + 
                             ", Value = " + entry.getValue()); 
		try {

			for (String invName : clientMasterList) {
				try {
					String investorName = invName.substring(0, invName.indexOf('-'));
					String investorPan = invName.substring(invName.indexOf('-') + 1);					
					folioOfStagingList = new ArrayList<String>();
					folioOfBrokerageList = new ArrayList<String>();
					investorIdOfStagingInvestorList = new ArrayList<Integer>();
					distinctFolioAndSchemeList = new ArrayList<Object[]>();
					
					//Gets all the investor IDs by investor names and PANs with respect to advisor ID from stagingInvestorMasterBO
					investorIdOfStagingInvestorList = stagingInvestorMasterBORepository.getIdByAdvisorIdAndNameAndPAN(investorName, investorPan, brokerageReportDTO.getAdvisorId());
					System.out.println("SI:"+investorIdOfStagingInvestorList);
					
					//Gets all the folio numbers of the investor from stagingFolioMasterBO
					folioOfStagingList = stagingFolioMasterBORepository.getAllDistinctFolioById(investorIdOfStagingInvestorList.get(0));					
					System.out.println("SF:"+folioOfStagingList);
					
					//Gets all the folios of an investor from brokerageMasterBO
					folioOfBrokerageList = brokerageMasterBORepository.getDistinctFolioSetByInvestorNameAndTransactionDate("%"+investorName+"%", fromDate, toDate);
					System.out.println("FB:"+folioOfBrokerageList);

					//Retains the common between stagingFolioMasterBO and brokerageMasterBO
					folioOfBrokerageList.retainAll(folioOfStagingList);
					System.out.println("F:"+folioOfBrokerageList);

					// Returns the list of DISTINCT set of Folio Number and Scheme Name
					List<Object[]> distinctFolioSchemeList = brokerageMasterBORepository
							.getDistinctFolioSchemeRTASetByInvestorNameAndTransactionDate("%"+investorName+"%",
									fromDate, toDate);
					System.out.println("DFS:"+distinctFolioSchemeList);
					for (Object[] distinctFolioScheme : distinctFolioSchemeList) {
						System.out.println("Outer loop:"+distinctFolioScheme[0]);
						for (String folio : folioOfBrokerageList) {
							System.out.println("Inner Loop:"+folio);
							if ( distinctFolioScheme[0].equals(folio) ) {
								distinctFolioAndSchemeList.add(distinctFolioScheme);
							}
						}
					}
					for (Object[] distinctFolioScheme : distinctFolioAndSchemeList) {
					System.out.println((String) distinctFolioScheme[0]+(String) distinctFolioScheme[1]);
					}
					for (Object[] distinctFolioScheme : distinctFolioAndSchemeList) {
						try {
							String folioNo = (String) distinctFolioScheme[0];
							String rtaCode = (String) distinctFolioScheme[1];
							String brokerageType = "";
							int toBeConsideredFlag = 0;
							Double aum = 0.0, brokerage = 0.0;
							List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo
									.findByCamsCodeAndStatus(rtaCode, "NEW");
							if (isinMasterBO.size() > 0) {
								String isin = isinMasterBO.get(0).getIsin();
								MasterMutualFundETF mutualFundETF = etfRepository.findOne(isin);

								if (brokerageReportDTO.getSchemeName() != null
										&& brokerageReportDTO.getSchemeName() != "") {
									if (isin.equals(brokerageReportDTO.getSchemeName())) {
										toBeConsideredFlag = 1;
									}
								} else {
									toBeConsideredFlag = 1;
								}

								if (toBeConsideredFlag == 1) {

									// Returns the list of all transactions based on a set of Folio Number and
									// Scheme Name
									List<BrokerageMasterBO> brokerageBasedOnFolioAndSchemeList = brokerageMasterBORepository
											.findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeRTA(
													"%"+investorName+"%", folioNo, rtaCode, fromDate, toDate);
									System.out.println(brokerageBasedOnFolioAndSchemeList);
									BrokerageReportColumnDTO brokerageReportColumnDTO = new BrokerageReportColumnDTO();
									//brokerageList = new ArrayList<BrokerageReportColumnDTO>();

									brokerageReportColumnDTO
											.setFolioDetails(folioNo + "-" + mutualFundETF.getSchemeName());
									if (mainFamilyPanMap.containsKey(investorPan)) {
										brokerageReportColumnDTO.setClientDetails(
												mainFamilyPanMap.get(investorPan) + "-" + investorPan);
									} else {
										brokerageReportColumnDTO.setClientDetails(invName);
									}

									for (BrokerageMasterBO bkbo : brokerageBasedOnFolioAndSchemeList) {
										try {
											aum += Double.parseDouble(bkbo.getAmount());
											brokerage += Double.parseDouble(bkbo.getBrokerageAmount());
											brokerageType = bkbo.getBrokerageType();
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									// BrokerageReportColumnDTO brokerageReportColumn = new
									// BrokerageReportColumnDTO();
									brokerageReportColumnDTO.setFolioNo(folioNo);
									//brokerageReportColumnDTO.setClientDetails(investorName + " - " + investorPan);
									if (mainFamilyPanMap.containsKey(investorPan)) {
										//brokerageReportColumnDTO.setClientDetails(mainFamilyPanMap.get(investorPan) + "-" + investorPan);
										brokerageReportColumnDTO.setClientDetails(
												clientNamePANMap.get(invName) + "-" + investorPan);
									} else {
										brokerageReportColumnDTO.setClientDetails(invName);
									}
									brokerageReportColumnDTO.setFundName(mutualFundETF.getFundHouse());
									brokerageReportColumnDTO
											.setFolioDetails(folioNo + "-" + mutualFundETF.getSchemeName());
									brokerageReportColumnDTO.setSchemeName(mutualFundETF.getSchemeName());
									brokerageReportColumnDTO.setCurrentAUM(
											numberFormatter.format(Math.round(Double.parseDouble(aum.toString())))
													.replace("\u20B9", "")
													.substring(0, numberFormatter
															.format(Math.round(Double.parseDouble(aum.toString())))
															.replace("\u20B9", "").length() - 3));
									brokerageReportColumnDTO.setBrokType(brokerageType);
									//brokerageReportColumnDTO.setBrokerage(numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(Double.parseDouble(brokerage.toString()), 2))));
									brokerageReportColumnDTO.setBrokerage(
											numberFormatter.format(Math.round(Double.parseDouble(brokerage.toString())))
											.replace("\u20B9", "")
											.substring(0, numberFormatter
													.format(Math.round(Double.parseDouble(brokerage.toString())))
													.replace("\u20B9", "").length() - 3));
									brokerageList.add(brokerageReportColumnDTO);
								}
								//brokerageBasedOnFolioAndSchemeMap.put(brokerageReportDTO.getNameClient(), brokerageList);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					if (brokerageList.size() > 0) {
						brokerageBasedOnFolioAndSchemeMap.put(brokerageReportDTO.getNameClient(), brokerageList);
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println("MAP SIZE: "+transactionsBasedOnFolioAndSchemeMap.size());
		return brokerageBasedOnFolioAndSchemeMap;
	}

}