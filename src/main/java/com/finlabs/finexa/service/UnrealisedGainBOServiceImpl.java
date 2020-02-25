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

import com.finlabs.finexa.dto.CurrentHoldingReportColumnDTO;
import com.finlabs.finexa.dto.CurrentHoldingReportDTO;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.InvestorMasterBO;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.SchemeIsinMasterBO;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.InvestMasterBORepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.SchemeIsinMasterBORepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;
import com.finlabs.finexa.resources.model.GainCalculator;
import com.finlabs.finexa.resources.service.GainCalculatorBOService;
import com.finlabs.finexa.util.FinexaUtil;

@Service("UnrealisedGainBOService")
@Transactional
public class UnrealisedGainBOServiceImpl implements UnrealisedGainBOService{
    
	@Autowired
    private TransactionMasterBORepository transactionMasterBORepository;

    @Autowired
    private MasterMutualFundETFRepository masterMutualFundETFRepository;
    
    @Autowired
    private MasterMFDailyNAVRepository masterMFDailyNAVRepository;
    
    @Autowired
	private InvestMasterBORepository investorMasterRepo;
    
    @Autowired
	private SchemeIsinMasterBORepository schemeIsinMasterBORepo;
	
	@Autowired
	private ClientMasterRepository clientMasterRepository;
	
	@Autowired
	private MasterMFDailyNAVRepository mfdailyNAVRepo;
    
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd/MM/yyyy");
    Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));

    @Override
    public Map<String, List<CurrentHoldingReportColumnDTO>> unrealisedGainReport(CurrentHoldingReportDTO currentHoldingReportDTO) throws RuntimeException, ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        // TODO Auto-generated method stub
        
        String rtaCode, folioNo, fundHouse, fundHouseName;
        Date fromDate, asOnDate;
        boolean flag = false;
        Integer schemeCode;
        double buyQty = 0.00, sellQty = 0.00;
        GainCalculatorBOService gc = new GainCalculatorBOService();
        List<String> clientMasterList = new ArrayList<String>(); 
        List<String> distinctSchemeNameList = new ArrayList<String>();   
        List<Object[]> folioSchemeNameList = new ArrayList<Object[]>();
        List <Date> transactionDateList = new ArrayList<Date>();
        List<TransactionMasterBO> transactionDetailsList = new ArrayList<TransactionMasterBO>();
       
        Map<String, List<CurrentHoldingReportColumnDTO>> unrealisedGainMap = new HashMap<String, List<CurrentHoldingReportColumnDTO>>();
  
		int clientId = currentHoldingReportDTO.getClientId(); 
		ClientMaster cm = clientMasterRepository.findOne(clientId); 
		Map<String,String> mainFamilyPanMap = new HashMap<String, String>();
		if (cm != null) { 
			String name = cm.getFirstName() + (cm.getMiddleName() == null ? "": (" " + cm.getMiddleName())) + (cm.getLastName() == null? "" : " " + (cm.getLastName()));
			String pan = cm.getPan();
			clientMasterList.add(name + "-" + pan);
			mainFamilyPanMap.put(pan, name);
			List<AuxillaryInvestorMaster> auxList = cm.getAuxillaryInvestorMasters(); 
			for (AuxillaryInvestorMaster obj : auxList) 
			{ 
				String auxName = obj.getFirstName() + (obj.getMiddleName() == null ? "": (" " +obj.getMiddleName())) + (obj.getLastName() == null? "" : (" " + obj.getLastName())); 
				String pan1 = obj.getPan(); 
				if (!clientMasterList.contains(auxName + "-" + pan1)) {
					clientMasterList.add(auxName + "-" + pan1); 
				}
			}
			List<ClientFamilyMember> familyMemberList = cm.getClientFamilyMembers(); 
			for (ClientFamilyMember obj : familyMemberList) { 
				if(Arrays.asList(currentHoldingReportDTO.getFamilyMemberIdArr()).contains(obj.getId())) {
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
						}
					}
					
					List<AuxillaryFamilyMember> auxFamList = obj.getAuxillaryFamilyMembers();
					for (AuxillaryFamilyMember aux : auxFamList) {
						String auxNameFam = aux.getFirstName() + (aux.getMiddleName() == null ? "": (" " + aux.getMiddleName())) + (aux.getLastName() == null? "" : (" " + aux.getLastName())); 
						String panFam = obj.getPan(); 
						if (panFam == null || panFam.equals("")) 
						{ 
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
        
        
        try {
        	
        	List<CurrentHoldingReportColumnDTO> unrealisedGainList = new ArrayList<CurrentHoldingReportColumnDTO>();
        	
        	for (String invName : clientMasterList) {
            	
        		String investorName = invName.substring(0, invName.indexOf('-'));
				String investorPan = invName.substring(invName.indexOf('-')+1);
    	        transactionDateList = transactionMasterBORepository.getFromDateByInvestorName(investorName, investorPan);        
    	        fromDate = transactionDateList.get(0); 
    	        asOnDate = currentHoldingReportDTO.getAsOnDate();
    		
    			List<String> transactionMasterBO = transactionMasterBORepository.getDistinctFolioNoByInvestorName(investorName, investorPan);
	
    			for (String objList : transactionMasterBO) {
    				
    				// Gets list of distinct scheme names and folio number of a specific investor
    				distinctSchemeNameList = transactionMasterBORepository
    						.getDistinctRTACodeByFolioNoAndDate(objList, investorName, investorPan, fromDate, asOnDate);
    				for (String ob : distinctSchemeNameList) {
    					Object[] objectList = new Object[2];
    					objectList[0] = (String)objList;
    					objectList[1] = ob;
    					folioSchemeNameList.add(objectList);
    				}
    				
    				
    			}
                
                //Iterates to calculate on each set of scheme names and folio number of a specific investor 
                for (Object[] ob : folioSchemeNameList) {
                	int toBeConsideredFlag = 0;
                    try {
                        
                    	folioNo = (String) ob[0];
    					rtaCode = (String) ob[1];
    					if (currentHoldingReportDTO.getSchemeName() != null && 
    							currentHoldingReportDTO.getSchemeName() != "") {
							List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo.findByCamsCodeAndStatus(rtaCode,"NEW");
							String camsCode = isinMasterBO.get(0).getCamsCode();
    						if (camsCode == rtaCode) {
    							toBeConsideredFlag = 1;
    						} else {
    							
    						}
    					} else {
    						toBeConsideredFlag = 1;
    					}
                    	
    					if (toBeConsideredFlag == 1) {

							List<SchemeIsinMasterBO> isinMasterBO = schemeIsinMasterBORepo.findByCamsCodeAndStatus(rtaCode,"NEW");
    						
    						if (isinMasterBO != null && isinMasterBO.size() > 0) {
    	  						
    							String isin = isinMasterBO.get(0).getIsin();
    							MasterMutualFundETF etf = masterMutualFundETFRepository.findOne(isin);
								fundHouseName = etf.getFundHouse();

        						CurrentHoldingReportColumnDTO currentHoldingReportColumnDTO = new CurrentHoldingReportColumnDTO();


        						//double transUnits = 0.00, investmentCost = 0.00, redeemedValue =0.00, currentValue = 0.00;
        						double balanceUnits = 0.00, investmentCost = 0.00, currentValue = 0.00, diffCost = 0.00;
        						Double currentNav = 0.00;double cumBuyCost = 0.0, dividendEarned = 0.00;
        						double dividendPayout = 0.0;
        						MasterMFDailyNAV dailyNav = masterMFDailyNAVRepository.findTopByIdMasterMutualFundEtfIsinOrderByIdDateDesc(isin);
        						currentNav = (double)dailyNav.getAdjNav();
        						

        						//Stores the list of results by gain calculator
        						List<GainCalculator> list = gc.getTransationDetails(investorName, folioNo, rtaCode, fromDate, asOnDate);
        						//System.out.println("REALISED GAIN Inside Service: "+list);

        						//Iterates to search over list of results of gain calculator, to find out positive sell quantity
        						for (GainCalculator gainCalculator : list) {                          

        							flag = true;
        							buyQty += gainCalculator.getBuyQuantity();
        							sellQty += gainCalculator.getSellQuantity();
        							dividendEarned += gainCalculator.getDividendReinvestUnrealized();
        							balanceUnits = gainCalculator.getBalanceQuantity();
        							if (gainCalculator.getSellQuantity() > 0) {
        								diffCost += gainCalculator.getDifferentialCost();
        							}
        							if (gainCalculator.getCumBuyCost() > 0) {
        								cumBuyCost = gainCalculator.getCumBuyCost();
        							}
        							dividendPayout += gainCalculator.getDividendPayoutUnrealized();
        							
        						}    
    							investmentCost = cumBuyCost - diffCost;                                                                                  

        						if (/*sellQty < buyQty*/FinexaUtil.roundOff(balanceUnits, 3) > 0) {

        							currentValue = balanceUnits * currentNav;
        							currentHoldingReportColumnDTO.setUnits(numberFormatter.format(new BigDecimal(FinexaUtil.roundOff(balanceUnits, 3))).replace("\u20B9", ""));
        							currentHoldingReportColumnDTO.setInvestmentCost(numberFormatter.format(new BigDecimal(Math.round(investmentCost))).replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(investmentCost))).replace("\u20B9", "").length() - 3));
        							currentHoldingReportColumnDTO.setCurrentValue(numberFormatter.format(new BigDecimal(Math.round(currentValue))).replace("\u20B9", "").substring(0, numberFormatter.format(new BigDecimal(Math.round(currentValue))).replace("\u20B9", "").length() - 3));
        							currentHoldingReportColumnDTO.setUnrealizedGainLoss(numberFormatter.format(Math.round(currentValue - investmentCost)).replace("\u20B9", "").substring(0,  numberFormatter.format(Math.round(currentValue - investmentCost)).replace("\u20B9", "").length() - 3));
        							currentHoldingReportColumnDTO.setDividendEarned(numberFormatter
											.format(new BigDecimal(FinexaUtil.roundOff(dividendEarned, 3))).replace("\u20B9", "").substring(0,  numberFormatter.format(Math.round(dividendEarned)).replace("\u20B9", "").length() - 3));
        							currentHoldingReportColumnDTO.setDividendPayout(numberFormatter.format(Math.round(dividendPayout)).replace("\u20B9", "").substring(0,  numberFormatter.format(Math.round(dividendPayout)).replace("\u20B9", "").length() - 3));
        							currentHoldingReportColumnDTO.setSchemeName(etf.getSchemeName());
        							currentHoldingReportColumnDTO.setFolioNo(folioNo);
        							currentHoldingReportColumnDTO.setFundName(fundHouseName);
        							if(mainFamilyPanMap.containsKey(investorPan)) {
        								
										currentHoldingReportColumnDTO.setClientDetails(mainFamilyPanMap.get(investorPan) + "-"+investorPan);
									} else {
										currentHoldingReportColumnDTO.setClientDetails(invName);
									}

        							//Set of one Mutual fund scheme's calculated values
        							unrealisedGainList.add(currentHoldingReportColumnDTO);                          

        						}
    						}
  
    					}
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }            

                }
            }
        	
        	//Set of all clients details
        	if(unrealisedGainList.size() > 0) {
        		 unrealisedGainMap.put(currentHoldingReportDTO.getNameClient(), unrealisedGainList);
        	}
           
        } catch (Exception e) {
            
            e.printStackTrace();
        }
                
        return unrealisedGainMap;
        
    }      



}