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

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.AumReconciliationReportColumnDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AumMasterBO;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientMutualFund;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AumMasterBORepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientMutualFundRepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.util.FinexaUtil;

@Service("AUMReconciliationBOService")
@Transactional
public class AUMReconciliationBOServiceImpl implements AUMReconciliationBOService {

	private static Logger log = LoggerFactory.getLogger(AUMReconciliationBOServiceImpl.class);

	@Autowired
	private AumMasterBORepository aumMasterBORepository;

	@Autowired
	private MasterMutualFundETFRepository masterMutualFundETFRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepository;

	@Autowired
	private MasterMFDailyNAVRepository masterMFDailyNAVRepository;

	@Autowired
	private ClientMutualFundRepository clientMutualFundRepository;
	
	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd-MM-yyyy");
	public static Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));
	public static final String YES = "Y";

	@Override
	public List<AumReconciliationReportColumnDTO> generateAUMReconciliationReport(int advisorID) {
		// TODO Auto-generated method stub
		try {
			List<ClientMutualFund> clientMutualFundList;
			Map<String, Double> clientFolioAUMMap;
			AdvisorUser advisorUser = advisorUserRepository.findById(advisorID);
			List<ClientMaster> clientMasterList = clientMasterRepository.getByAdvisorUserAndActiveFlag(advisorUser, YES);

			for (ClientMaster clientMaster : clientMasterList) {
				try {

					AumReconciliationReportColumnDTO aumReconciliationReportColumnDTO = new AumReconciliationReportColumnDTO();
					clientMutualFundList = new ArrayList<ClientMutualFund>();
					clientFolioAUMMap = new HashedMap();
					// Calculation of AUM from CLientMutualFund
					clientMutualFundList = clientMutualFundRepository.findByClientMasterAndBackOfficeEntry(clientMaster,
							YES);
					for (ClientMutualFund clientMutualFund : clientMutualFundList) {
						try {
							double unitsLeft = 0.0;
							if (!clientFolioAUMMap.isEmpty()) {
								if (clientFolioAUMMap.containsKey(clientMutualFund.getFolioNumber().trim() + "-"
										+ clientMutualFund.getIsin().trim())) {
									unitsLeft = clientFolioAUMMap.get(clientMutualFund.getFolioNumber().trim() + "-"
											+ clientMutualFund.getIsin().trim());
									unitsLeft += clientMutualFund.getLumpsumUnitsPurchased().doubleValue();
									clientFolioAUMMap.put(clientMutualFund.getFolioNumber().trim() + "-"
											+ clientMutualFund.getIsin().trim(), unitsLeft);
								} else {
									clientFolioAUMMap.put(
											clientMutualFund.getFolioNumber().trim() + "-"
													+ clientMutualFund.getIsin().trim(),
											clientMutualFund.getLumpsumUnitsPurchased().doubleValue());
								}
							} else {
								clientFolioAUMMap.put(
										clientMutualFund.getFolioNumber().trim() + "-"
												+ clientMutualFund.getIsin().trim(),
										clientMutualFund.getLumpsumUnitsPurchased().doubleValue());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					Map<String, Double> folioUnitsMap = new HashMap<String, Double>();
					List<String> unCommonFolioIsin = new ArrayList<String>();
					folioUnitsMap = aumReport(clientMaster.getId());

					if (clientFolioAUMMap.size() > 0 && folioUnitsMap.size() > 0) {
						for (Map.Entry<String, Double> entry : clientFolioAUMMap.entrySet()) {
							try {
								if (folioUnitsMap.containsKey(entry.getKey())) {
									String invName = clientMaster.getFirstName()
											+ (clientMaster.getMiddleName() == null ? ""
													: (" " + clientMaster.getMiddleName()))
											+ (clientMaster.getLastName() == null ? ""
													: " " + (clientMaster.getLastName()));
									String folioISIN[] = entry.getKey().trim().split("-");
									if (folioISIN[0] != null && folioISIN[1] != null) {
										MasterMutualFundETF masterMutualFundETF = masterMutualFundETFRepository
												.findByIsin(folioISIN[1]);
										if (masterMutualFundETF != null) {
											double unitsDifference = folioUnitsMap.get(entry.getKey())
													- entry.getValue();
											Date maxDate = masterMFDailyNAVRepository.findMaxDate(folioISIN[1],
													new Date());
											MasterMFDailyNAV masterMFDailyNAV = masterMFDailyNAVRepository
													.findNAV(folioISIN[1], maxDate);
											double aumDifference = (folioUnitsMap.get(entry.getKey())
													* masterMFDailyNAV.getNav())
													- (entry.getValue() * masterMFDailyNAV.getNav());
											double amcAUM = folioUnitsMap.get(entry.getKey())
													* masterMFDailyNAV.getNav();
											double finexaAUM = entry.getValue() * masterMFDailyNAV.getNav();
											aumReconciliationReportColumnDTO.setAmcInvestor(invName);
											aumReconciliationReportColumnDTO.setFinexaInvestor(invName);
											aumReconciliationReportColumnDTO
													.setFundName(masterMutualFundETF.getFundHouse());
											aumReconciliationReportColumnDTO.setFolioNo(folioISIN[0]);
											aumReconciliationReportColumnDTO.setAmcAum(numberFormatter
													.format(new BigDecimal(FinexaUtil.roundOff(amcAUM, 3)))
													.replace("\u20B9", ""));
											aumReconciliationReportColumnDTO.setFinexaAum(numberFormatter
													.format(new BigDecimal(FinexaUtil.roundOff(finexaAUM, 3)))
													.replace("\u20B9", ""));
											aumReconciliationReportColumnDTO.setAumDifference(numberFormatter
													.format(new BigDecimal(FinexaUtil.roundOff(aumDifference, 3)))
													.replace("\u20B9", ""));
											aumReconciliationReportColumnDTO.setAmcUnits(numberFormatter
													.format(new BigDecimal(
															FinexaUtil.roundOff(folioUnitsMap.get(entry.getKey()), 3)))
													.replace("\u20B9", ""));
											aumReconciliationReportColumnDTO.setFinexaUnits(numberFormatter
													.format(new BigDecimal(FinexaUtil.roundOff(entry.getValue(), 3)))
													.replace("\u20B9", ""));
											aumReconciliationReportColumnDTO.setUnitDifference(numberFormatter
													.format(new BigDecimal(FinexaUtil.roundOff(unitsDifference, 3)))
													.replace("\u20B9", ""));
											aumReconciliationReportColumnDTO.setTransDate("TRANS DATE");
											aumReconciliationReportColumnDTO.setFolioFreezeDate("FOLIO FREEZE DATE");
										} else {
											log.debug(
													"ISIN not found in MasterMutualFundETF for ISIN: " + folioISIN[1]);
											continue;
										}
									} else {
										log.debug("Folio or ISIN not found");
										continue;
									}

								} else {
									unCommonFolioIsin.add(entry.getKey());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						log.debug("AUM or Transaction data not found");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, Double> aumReport(int clientID) throws RuntimeException {
		// TODO Auto-generated method stub
		// String fundHouseName = null;

		// Map<String, List<AumReportColumnDTO>> result = new HashMap<String,
		// List<AumReportColumnDTO>>();
		List<String> clientMasterList = new ArrayList<String>();
		int clientId = clientID;
		ClientMaster cm = clientMasterRepository.findOne(clientId);
		Map<String, String> mainFamilyPanMap = new HashMap<String, String>();
		Map<String, String> clientMasterMap = new HashMap<String, String>();
		Map<String, Integer> clientFamilyFlagMap = new HashMap<String, Integer>();
		Map<String, Double> totalCurrentValueAndPANMap = new HashMap<String, Double>();
		Map<String, Double> folioUnitsMap = new HashMap<String, Double>();

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
				// if (Arrays.asList(aumReportDTO.getFamilyMemberIdArr()).contains(obj.getId()))
				// {
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
				// }
			}
		}

		System.out.println(clientMasterList);
		// List<AumReportColumnDTO> aumReportList = new ArrayList<AumReportColumnDTO>();
		for (String invName : clientMasterList) {
			String investorName = invName.substring(0, invName.indexOf('-'));
			String investorPan = invName.substring(invName.indexOf('-') + 1);
			if (clientMasterMap.containsKey(investorName.toLowerCase())) {
				if (clientMasterMap.get(investorName.toLowerCase()).equals(investorPan)) {
					continue;
				} else {
					clientMasterMap.put(investorName.toLowerCase(), investorPan);
				}
			} else {
				clientMasterMap.put(investorName.toLowerCase(), investorPan);
			}
			// String isin = aumReportDTO.getSchemeName();
			List<AumMasterBO> aumList = null;
			List<AumMasterBO> aumListToremove = new ArrayList<AumMasterBO>();
			if (!totalCurrentValueAndPANMap.isEmpty())
				if (totalCurrentValueAndPANMap
						.containsKey(clientFamilyFlagMap.get(investorName + "-" + investorPan).toString())) {
				}

			// String total = "";

			aumList = aumMasterBORepository.findAllByReportDateAndInvestorName(new Date(), investorName);
			// System.out.println("BEFORE"+aumList.size());
			int i = 0;
			for (AumMasterBO aumTemp : aumList) {
				try {

					for (int j = i + 1; j < aumList.size(); j++) {
						if (aumTemp.getId().getFolioNumber() != null && aumTemp.getId().getSchemertacode() != null
								&& aumTemp.getId().getReportDate() != null) {

							if (aumTemp.getId().getFolioNumber().equals(aumList.get(j).getId().getFolioNumber())
									&& aumTemp.getId().getSchemertacode()
											.equals(aumList.get(j).getId().getSchemertacode())
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
			if (aumListToremove.size() > 0)
				aumList.removeAll(aumListToremove);
			// System.out.println("AFTER"+aumList.size());
			if (aumList != null && aumList.size() > 0) {
				for (AumMasterBO aum : aumList) {
					try {
						double units = 0.0;
						List<SchemeIsinMasterBO> schemeIsinMasterBOList = schemeIsinMasterBORepository
								.findByCamsCodeAndStatus(aum.getId().getSchemertacode(), "NEW");
						if (schemeIsinMasterBOList != null && schemeIsinMasterBOList.size() > 0) {
							if (!folioUnitsMap.isEmpty()) {
								if (folioUnitsMap.containsKey(aum.getId().getFolioNumber().trim() + "-"
										+ schemeIsinMasterBOList.get(0).getIsin().trim())) {
									units = folioUnitsMap.get(aum.getId().getFolioNumber().trim() + "-"
											+ schemeIsinMasterBOList.get(0).getIsin().trim());
									units += Double.parseDouble(aum.getUnitBalance());
									folioUnitsMap.put(aum.getId().getFolioNumber().trim() + "-"
											+ schemeIsinMasterBOList.get(0).getIsin().trim(), units);
								} else {
									folioUnitsMap.put(
											aum.getId().getFolioNumber().trim() + "-"
													+ schemeIsinMasterBOList.get(0).getIsin().trim(),
											Double.parseDouble(aum.getUnitBalance()));
								}
							} else {
								folioUnitsMap.put(
										aum.getId().getFolioNumber().trim() + "-"
												+ schemeIsinMasterBOList.get(0).getIsin().trim(),
										Double.parseDouble(aum.getUnitBalance()));
							}
						}
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						throw new RuntimeException(e);
					}
				}

			}
		}

		return folioUnitsMap;
	}

}
