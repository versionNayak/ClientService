package com.finlabs.finexa.service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.finlabs.finexa.dto.AumReportColumnDTO;
import com.finlabs.finexa.dto.AumReportDTO;
import com.finlabs.finexa.model.AumMasterBO;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.repository.AumMasterBORepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.util.FinexaUtil;

@Service("BackOfficeReportsService")
@Transactional
public class BackOfficeReportsServiceImpl implements BackOfficeReportsService {

	private static Logger log = LoggerFactory.getLogger(BackOfficeReportsServiceImpl.class);

	@Autowired
	private AumMasterBORepository aumMasterBORepository;
	
	@Autowired
	private MasterMutualFundETFRepository masterMutualFundETFRepository;
	
	@Autowired
	private ClientMasterRepository clientMasterRepository;
	
	@Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepository;
	
	@Autowired
	private MasterMFDailyNAVRepository masterMFDailyNavRepo;
	
	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd-MM-yyyy");
	public static Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));
	
	@Override
	public Map<String,List<AumReportColumnDTO>> aumReport(AumReportDTO aumReportDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		String fundHouseName = null;
		
		Map<String,List<AumReportColumnDTO>> result = new HashMap<String, List<AumReportColumnDTO>>();
		List<String> clientMasterList = new ArrayList<String>(); 
		int clientId = aumReportDTO.getClientId(); 
		ClientMaster cm = clientMasterRepository.findOne(clientId); 
		Map<String,String> mainFamilyPanMap = new HashMap<String, String>();
		Map<String,String> clientMasterMap = new HashMap<String, String>();
		Map<String,Integer> clientFamilyFlagMap = new HashMap<String, Integer>();
		Map<String,Double> totalCurrentValueAndPANMap = new HashMap<String, Double>();
		
		if (cm != null) { 
			String name = cm.getFirstName() + (cm.getMiddleName() == null ? "": (" " + cm.getMiddleName())) + (cm.getLastName() == null? "" : " " + (cm.getLastName()));
			String pan = cm.getPan(); clientMasterList.add(name + "-" + pan);
			clientFamilyFlagMap.put(name + "-" + pan, cm.getId());
			mainFamilyPanMap.put(pan, name);
			List<AuxillaryInvestorMaster> auxList = cm.getAuxillaryInvestorMasters(); 
			for (AuxillaryInvestorMaster obj : auxList) 
			{ 
				String auxName = obj.getFirstName() + (obj.getMiddleName() == null ? "": (" " +obj.getMiddleName())) + (obj.getLastName() == null? "" : (" " + obj.getLastName())); 
				String pan1 = obj.getPan(); 
				if (!clientMasterList.contains(auxName + "-" + pan1)) {
					clientMasterList.add(auxName + "-" + pan1); 
					clientFamilyFlagMap.put(auxName + "-" + pan1, obj.getClientMaster().getId());
				}
			}
			List<ClientFamilyMember> familyMemberList = cm.getClientFamilyMembers(); 
			for (ClientFamilyMember obj : familyMemberList) { 
				if(Arrays.asList(aumReportDTO.getFamilyMemberIdArr()).contains(obj.getId())) {
					String auxName = obj.getFirstName() + (obj.getMiddleName() == null ? "": (" " + obj.getMiddleName())) + (obj.getLastName() == null? "" : (" " + obj.getLastName())); 
					String pan1 = obj.getPan();
					if (pan1 == null || pan1.equals("")) 
					{ 
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
						String auxNameFam = aux.getFirstName() + (aux.getMiddleName() == null ? "": (" " + aux.getMiddleName())) + (aux.getLastName() == null? "" : (" " + aux.getLastName())); 
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
		List<AumReportColumnDTO> aumReportList = new ArrayList<AumReportColumnDTO>();
		for (String invName : clientMasterList) {
			String investorName = invName.substring(0, invName.indexOf('-'));
			String investorPan = invName.substring(invName.indexOf('-')+1);
			if(clientMasterMap.containsKey(investorName.toLowerCase())) {
				if(clientMasterMap.get(investorName.toLowerCase()).equals(investorPan)) {
					continue;
				} else {
					clientMasterMap.put(investorName.toLowerCase(), investorPan);
				}
			} else {
				clientMasterMap.put(investorName.toLowerCase(), investorPan);
			}
			String isin = aumReportDTO.getSchemeName();
			List<AumMasterBO> aumList = null;
			List<AumMasterBO> aumListToremove = new ArrayList<AumMasterBO>();
			Double totalCurrentValue = 0.0;
			if (!totalCurrentValueAndPANMap.isEmpty())
				if(totalCurrentValueAndPANMap.containsKey(clientFamilyFlagMap.get(investorName+ "-" + investorPan).toString()))
					totalCurrentValue = Double.parseDouble(totalCurrentValueAndPANMap.get(clientFamilyFlagMap.get(investorName+ "-" + investorPan).toString()).toString());
				
			//String total = "";
			if (isin == null || isin.isEmpty()) {
				// Consider all the schemes
				aumList = aumMasterBORepository.findAllByReportDateAndInvestorName(aumReportDTO.getAsOnDate(), investorName); 
				//total = aumMasterBORepository.getTotalCurrentValue1(aumReportDTO.getAsOnDate(), investorName);
			} else {
				// select for specific isin
				SchemeIsinMasterBO isinMasterBO = schemeIsinMasterBORepository.findOne(isin);
				aumList = aumMasterBORepository.findAllByReportDateAndInvestorNameAndRTACode(aumReportDTO.getAsOnDate(), investorName, isinMasterBO.getCamsCode()); 
				//total = aumMasterBORepository.getTotalCurrentValue2(aumReportDTO.getAsOnDate(), investorName, isinMasterBO.getCamsCode());
			}
			//System.out.println("BEFORE"+aumList.size());
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
					//System.out.println(aum.getSchemeName());
						AumReportColumnDTO item = new AumReportColumnDTO();
						List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepository.findByCamsCodeAndStatus(aum.getId().getSchemertacode(),"NEW");
						if (isinMasterBO != null && isinMasterBO.size() > 0) {
							String isinForEtf = isinMasterBO.get(0).getIsin();
							MasterMutualFundETF	etf = masterMutualFundETFRepository.findOne(isinForEtf);
							fundHouseName = etf.getFundHouse();
							item.setFundName(fundHouseName);
							String folioNo = aum.getId().getFolioNumber();
							if(folioNo.contains("E")) {
								folioNo = folioNo.substring(0, folioNo.indexOf(".")) + folioNo.substring(folioNo.indexOf(".") + 1); 
								folioNo = folioNo.substring(0, folioNo.length() - 3);
							}
							item.setFolioNo(folioNo);
							item.setSchemeName(etf.getSchemeName());
							
							item.setUnits(numberFormatter.format(FinexaUtil.roundOff(Double.parseDouble(aum.getUnitBalance()), 2)).replace("\u20B9", ""));
							
							Date maxDate = masterMFDailyNavRepo.findMaxDate(isinForEtf, aumReportDTO.getAsOnDate());
							MasterMFDailyNAV mfDailyNav = masterMFDailyNavRepo.findNAV(isinForEtf, maxDate);
							
							if (maxDate != null) {
								item.setCurrentNav(String.valueOf(FinexaUtil.roundOff(mfDailyNav.getNav(), 2)));
								//System.out.println("NAV:"+mfDailyNav.getNav());
								nav = mfDailyNav.getNav();
							} else {
								item.setCurrentNav(String.valueOf(FinexaUtil.roundOff(Double.parseDouble(aum.getNav()), 2)));
								nav = Float.parseFloat(aum.getNav());
							}
							
							if(aum.getCurrentValue() != null) {
								//System.out.println(aum.getUnitBalance()+"*"+nav);
								double currentVal = Double.parseDouble(aum.getUnitBalance()) * nav;
								totalCurrentValue += currentVal;
								totalCurrentValueAndPANMap.put(clientFamilyFlagMap.get(investorName+ "-" + investorPan).toString(), totalCurrentValue);
								item.setCurrentValue(String.valueOf(numberFormatter.format(Math.round(currentVal)).replace("\u20B9", "")).substring(0,  String.valueOf(numberFormatter.format(Math.round(currentVal)).replace("\u20B9", "")).length() - 3));
							} 
							
							if(mainFamilyPanMap.containsKey(investorPan)) {
								item.setClientDetails(mainFamilyPanMap.get(investorPan) + "-" + investorPan);

							} else {
								item.setClientDetails(invName);
							}
							
							item.setTotal(String.valueOf(numberFormatter.format(Math.round(Float.parseFloat(totalCurrentValueAndPANMap.get(clientFamilyFlagMap.get(investorName+ "-" + investorPan).toString()).toString()))).replace("\u20B9", "")).substring(0, String.valueOf(numberFormatter.format(Math.round(Float.parseFloat(totalCurrentValueAndPANMap.get(clientFamilyFlagMap.get(investorName+ "-" + investorPan).toString()).toString()))).replace("\u20B9", "")).length() - 3));
							//System.out.println(item.getSchemeName());
							aumReportList.add(item);
						} else {
							//System.out.println("No RTA co isin mapping found for" + aum.getId().getSchemertacode());
						}
						
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						throw new RuntimeException(e);
					}
				}
				
			}
		}
		
		if (aumReportList.size() > 0) {
			result.put(aumReportDTO.getNameClient(), aumReportList);
		}
		
		return result;
	}

	
}
