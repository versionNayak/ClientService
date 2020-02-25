package com.finlabs.finexa.service;


import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientFamilyIncomeDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientLifeExpDTO;
import com.finlabs.finexa.dto.LifeExpectancyDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterAgeMortalityRate;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.LookupRelationshipRepository;
import com.finlabs.finexa.repository.MasterAgeMortalityRateRepository;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;

@Service("ClientLifeExpectancyService")
@Transactional
public class ClientLifeExpectancyServiceImpl implements ClientLifeExpectancyService{
	private static Logger log = LoggerFactory.getLogger(ClientLifeExpectancyService.class);
	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private ClientFamilyIncomeService clientFamilyIncomeService;
	
	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private MasterAgeMortalityRateRepository masterAgeMortRepo;
	
	@Autowired
	LookupRelationshipRepository lookupRelationshipRepository;
	
	@Autowired
	ClientGoalService  ClientGoalService;

	@Override
	public LifeExpectancyDTO save(LifeExpectancyDTO lifeExpectancyDTO) throws RuntimeException{
		try {
			int memberId = lifeExpectancyDTO.getFamilyMemberId();
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(memberId);

			if (lifeExpectancyDTO.getIsProperBMI().equalsIgnoreCase("on")) {
				clientFamilyMember.setIsProperBMI("Y");
			} else {
				clientFamilyMember.setIsProperBMI("N");
			}

			if (lifeExpectancyDTO.getHasNormalBP().equalsIgnoreCase("on")) {
				clientFamilyMember.setHasNormalBP("Y");
			} else {
				clientFamilyMember.setHasNormalBP("N");
			}


			if (lifeExpectancyDTO.getIsTobaccoUser().equalsIgnoreCase("on")) {
				clientFamilyMember.setIsTobaccoUser("Y");
			} else {
				clientFamilyMember.setIsTobaccoUser("N");
			}


			if (lifeExpectancyDTO.getHasDiseaseHistory().equalsIgnoreCase("on")) {
				clientFamilyMember.setHasDiseaseHistory("Y");
			} else {
				clientFamilyMember.setHasDiseaseHistory("N");
			}
			clientFamilyMember.setId(memberId);
			clientFamilyMember.setLifeExpectancy(lifeExpectancyDTO.getTotalLifeExpectancy());
			ClientFamilyMember member = clientFamilyMemberRepository.save(clientFamilyMember);
			log.debug("lifeExpectancyDTO.getAnnualIncome() "+lifeExpectancyDTO.getAnnualIncome());
			
			log.debug("member.getClientFamilyIncomes().size() "+member.getClientFamilyIncomes().size());
			
			if(member.getClientFamilyIncomes().size()==0){
			clientFamilyIncomeService.saveFamilyIncomeAfterChangeInLifeExp(lifeExpectancyDTO);
			}
			if(member.getClientFamilyIncomes().size()!=0){
				log.debug("member.getClientFamilyIncomes().get(0).getIncomeAmount() "+member.getClientFamilyIncomes().get(0).getIncomeAmount());
				if((member.getClientFamilyIncomes().get(0).getIncomeAmount().doubleValue()==0.00) || (member.getClientFamilyIncomes().get(0).getIncomeAmount().longValue()==0)){
					clientFamilyIncomeService.saveFamilyIncomeAfterChangeInLifeExp(lifeExpectancyDTO);
				}
			}
			return lifeExpectancyDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public LifeExpectancyDTO update(LifeExpectancyDTO lifeExpectancyDTO) throws RuntimeException{
		try {
			int memberId = lifeExpectancyDTO.getFamilyMemberId();
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(memberId);

			if (lifeExpectancyDTO.getIsProperBMI().equalsIgnoreCase("on")) {
				clientFamilyMember.setIsProperBMI("Y");
			} else {
				clientFamilyMember.setIsProperBMI("N");
			}

			if (lifeExpectancyDTO.getHasNormalBP().equalsIgnoreCase("on")) {
				clientFamilyMember.setHasNormalBP("Y");
			} else {
				clientFamilyMember.setHasNormalBP("N");
			}


			if (lifeExpectancyDTO.getIsTobaccoUser().equalsIgnoreCase("on")) {
				clientFamilyMember.setIsTobaccoUser("Y");
			} else {
				clientFamilyMember.setIsTobaccoUser("N");
			}


			if (lifeExpectancyDTO.getHasDiseaseHistory().equalsIgnoreCase("on")) {
				clientFamilyMember.setHasDiseaseHistory("Y");
			} else {
				clientFamilyMember.setHasDiseaseHistory("N");
			}
			clientFamilyMember.setId(memberId);
			clientFamilyMember.setLifeExpectancy(lifeExpectancyDTO.getTotalLifeExpectancy());
			ClientFamilyMember member = clientFamilyMemberRepository.save(clientFamilyMember);
			log.debug("lifeExpectancyDTO.getAnnualIncome() "+lifeExpectancyDTO.getAnnualIncome());
			
			log.debug("member.getClientFamilyIncomes().size() "+member.getClientFamilyIncomes().size());
			
			if(member.getClientFamilyIncomes().size()==0){
			clientFamilyIncomeService.saveFamilyIncomeAfterChangeInLifeExp(lifeExpectancyDTO);
			}
			if(member.getClientFamilyIncomes().size()!=0){
				log.debug("member.getClientFamilyIncomes().get(0).getIncomeAmount() "+member.getClientFamilyIncomes().get(0).getIncomeAmount());
				if((member.getClientFamilyIncomes().get(0).getIncomeAmount().doubleValue()==0.00) || (member.getClientFamilyIncomes().get(0).getIncomeAmount().longValue()==0)){
					clientFamilyIncomeService.saveFamilyIncomeAfterChangeInLifeExp(lifeExpectancyDTO);
				}
			}
			return lifeExpectancyDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public LifeExpectancyDTO findByMemberId(int memberId) throws RuntimeException, CustomFinexaException{
		try {
			ClientFamilyMember clientFamilyMember=clientFamilyMemberRepository.findOne(memberId);
			ClientFamilyMemberDTO clientFamilyMemberDTO = mapper.map(clientFamilyMember, ClientFamilyMemberDTO.class);
			clientFamilyMemberDTO.setClientID(clientFamilyMember.getClientMaster().getId());
			clientFamilyMemberDTO.setRelationID(clientFamilyMember.getLookupRelation().getId());
			
			LifeExpectancyDTO lifeExpectancyDTO;
			
			lifeExpectancyDTO = reCalculateLifeExpectancyForFamilyMember(clientFamilyMemberDTO);
			
			lifeExpectancyDTO.setFamilyMemberId(clientFamilyMemberDTO.getId());
			lifeExpectancyDTO.setId(clientFamilyMemberDTO.getClientID());
			//clientFamilyMember.setLookupRelation(lookupRelationshipRepository.findOne(clientFamilyMemberDTO.getRelationID()));

			
			ClientMaster cm = clientMasterRepository.findOne(clientFamilyMemberDTO.getClientID());
			String gender=FinexaUtil.getGender(cm, clientFamilyMember.getLookupRelation());
			lifeExpectancyDTO.setGender(gender);
			lifeExpectancyDTO.setBirthDate(clientFamilyMemberDTO.getBirthDate());
			
			List<ClientFamilyIncomeDTO> lstIncome=clientFamilyIncomeService.getAllIncomeForFamilyMember(clientFamilyMemberDTO.getClientID(), clientFamilyMemberDTO.getId());
			if (lstIncome.size()> 0) {
				int s=lstIncome.size();
			  lifeExpectancyDTO.setAnnualIncome(lstIncome.get(s-1).getTotal().doubleValue());
				
			}
			log.debug("Annual Income in edit mode: " + lifeExpectancyDTO.getAnnualIncome());
			
			
			
			lifeExpectancyDTO.setHasDiseaseHistory(clientFamilyMember.getHasDiseaseHistory());
			lifeExpectancyDTO.setHasNormalBP(clientFamilyMember.getHasNormalBP());
			lifeExpectancyDTO.setIsProperBMI(clientFamilyMember.getIsProperBMI());
			lifeExpectancyDTO.setIsTobaccoUser(clientFamilyMember.getIsTobaccoUser());
			
			
			
			//after 
			
			/*ClientFamilyMember clientFamilyMember=clientFamilyMemberRepository.findOne(memberId);
			LifeExpectancyDTO lifeExpectancyDTO=new LifeExpectancyDTO();
			lifeExpectancyDTO.setFamilyMemberId(clientFamilyMember.getId());
			lifeExpectancyDTO.setId(clientFamilyMember.getClientMaster().getId());
			lifeExpectancyDTO.setBirthDate(clientFamilyMember.getBirthDate());
			
			List<ClientFamilyIncomeDTO> lstIncome=clientFamilyIncomeService.getAllIncomeForFamilyMember(clientFamilyMember.getClientMaster().getId(), clientFamilyMember.getId());
			if (lstIncome.size()> 0) {
				int s=lstIncome.size();
			  lifeExpectancyDTO.setAnnualIncome(lstIncome.get(s-1).getTotal().doubleValue());
				
			}
			
			ClientMaster cm = clientMasterRepository.findOne(clientFamilyMember.getClientMaster().getId());
			String gender=FinexaUtil.getGender(cm, clientFamilyMember.getLookupRelation());
			lifeExpectancyDTO.setGender(gender);
			

			
			log.debug("clientFamilyMember.getIsTobaccoUser() "+clientFamilyMember.getIsTobaccoUser());
			log.debug("clientFamilyMember.getIsProperBMI() "+clientFamilyMember.getIsProperBMI());
			log.debug("clientFamilyMember.getHasDiseaseHistory() "+clientFamilyMember.getHasDiseaseHistory());
			log.debug("clientFamilyMember.getHasNormalBP() "+clientFamilyMember.getHasNormalBP());
			
			lifeExpectancyDTO.setHasDiseaseHistory(clientFamilyMember.getHasDiseaseHistory());
			lifeExpectancyDTO.setHasNormalBP(clientFamilyMember.getHasNormalBP());
			lifeExpectancyDTO.setIsProperBMI(clientFamilyMember.getIsProperBMI());
			lifeExpectancyDTO.setIsTobaccoUser(clientFamilyMember.getIsTobaccoUser());
			
			LifeExpectancyDTO lifeExpectancyDTO1=calculateLifeExp(lifeExpectancyDTO);
			lifeExpectancyDTO.setFutureLifeExpectancy(lifeExpectancyDTO1.getFutureLifeExpectancy());
			lifeExpectancyDTO.setTotalLifeExpectancy(lifeExpectancyDTO1.getTotalLifeExpectancy());
			*/
			return lifeExpectancyDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public LifeExpectancyDTO calculateLifeExp(LifeExpectancyDTO lifeExpectancyDTO) throws RuntimeException, CustomFinexaException{
		// TODO Auto-generated method stub
		try {
			if(lifeExpectancyDTO.getFamilyMemberId()!=0){
			//int familyMemberId = lifeExpectancyDTO.getFamilyMemberId();
			//log.debug("----familyMemberId----" + familyMemberId);
			}
			//Date clientDob = lifeExpectancyDTO.getBirthDate();
			//log.debug("----clientDob----" + clientDob);
			String clientGender = lifeExpectancyDTO.getGender();
			clientGender = clientGender.trim();
			if(clientGender.equalsIgnoreCase("Female")) {
				clientGender = "Female";
			} else {
				clientGender = "Male";
			}
			//log.debug("----clientGender----" + clientGender);

			double clientIncome = lifeExpectancyDTO.getAnnualIncome();
			//log.debug("----clientIncome----" + clientIncome);
			String isTobaccoUser = lifeExpectancyDTO.getIsTobaccoUser();
			//log.debug("----isTobaccoUser----" + isTobaccoUser);
			String isNormalBMI = lifeExpectancyDTO.getIsProperBMI();
			//log.debug("----isNormalBMI----" + isNormalBMI);
			String hasDiseaseHistory = lifeExpectancyDTO.getHasDiseaseHistory();
			//log.debug("----hasDiseaseHistory----" + hasDiseaseHistory);
			String hasBloodPressure = lifeExpectancyDTO.getHasNormalBP();
			//log.debug("----hasBloodPressure----" + hasBloodPressure);


			// Calculating Health Status

			String healthStatus = "Average";
			int noOfYes = 0;
			if (hasBloodPressure.equalsIgnoreCase("on") || hasBloodPressure.equalsIgnoreCase("Y")) {
				noOfYes++;
			}
			if (isNormalBMI.equalsIgnoreCase("on") || isNormalBMI.equalsIgnoreCase("Y")) {
				noOfYes++;
			}
			if (hasDiseaseHistory.equalsIgnoreCase("on") || hasDiseaseHistory.equalsIgnoreCase("Y")) {
				noOfYes++;
			}
			if (noOfYes == 3) {
				healthStatus = "Perfectly Fit";
			} else if (noOfYes == 0) {
				healthStatus = "Poor";
			}

			//log.debug("---healthStatus -----" + healthStatus);

			// Calculating Income Mortality Weight
			int imwp = 0;

			if (clientIncome >= 0 && clientIncome <= 250000) {
				imwp = 160;
			} else if (clientIncome > 250000 && clientIncome <= 500000) {
				imwp = 100;
			} else if (clientIncome > 500000 && clientIncome <= 1000000) {
				imwp = 80;
			} else if (clientIncome > 1000000 && clientIncome <= 2500000) {
				imwp = 60;
			} else if (clientIncome > 2500000) {
				imwp = 40;
			}

			//log.debug("---Income Mortality Weight -----" + imwp);

			// Calculating Gender Mortality Weight
			int gmwp = 0;
			//log.debug("----clientGender----" + clientGender);
			if (clientGender.equalsIgnoreCase("Male")) {
				gmwp = 100;
			} else if (clientGender.equalsIgnoreCase("Female")) {
				gmwp = 80;
			}
			//log.debug("---Gender Mortality Weight -----" + gmwp);
			// Calculating Tobacco Mortality Weight
			int tmwp = 0;
			if (clientGender.equalsIgnoreCase("Male")) {
				if (isTobaccoUser.equalsIgnoreCase("on") || isTobaccoUser.equalsIgnoreCase("Y")) {
					tmwp = 150;
				} else if (isTobaccoUser.equalsIgnoreCase("off") || isTobaccoUser.equalsIgnoreCase("N")) {
					tmwp = 100;
				}
				//log.debug("---Male Tobacco Mortality Weight -----" + tmwp);
			}

			// Male Non Smoker
			int mnsmwp = 0;
			if (clientGender.equalsIgnoreCase("Male")) {

				if (isTobaccoUser.equalsIgnoreCase("on") || isTobaccoUser.equalsIgnoreCase("Y")) {
					mnsmwp = 100;
				} else {
					if (healthStatus.equalsIgnoreCase("Perfectly Fit")) {
						mnsmwp = 85;
					} else if (healthStatus.equalsIgnoreCase("Average")) {
						mnsmwp = 95;
					} else if (healthStatus.equalsIgnoreCase("Poor")) {
						mnsmwp = 120;
					}
				}

				//log.debug("---Male Non Smoker Mortality Weight -----" + mnsmwp);
			}
			// Female

			int fmwp = 0;
			if (clientGender.equalsIgnoreCase("Female")) {
				if (healthStatus.equalsIgnoreCase("Perfectly Fit")) {
					fmwp = 85;
				} else if (healthStatus.equalsIgnoreCase("Average")) {
					fmwp = 100;
				} else if (healthStatus.equalsIgnoreCase("Poor")) {
					fmwp = 120;
				}
				//log.debug("---Female Mortality Weight -----" + fmwp);

			}

			// Calculating applied mortality
			double resultantMortalityDouble = 0;
			if (clientGender.equalsIgnoreCase("Male")) {
				resultantMortalityDouble =
						((imwp / 100.0) * (gmwp / 100.0) * (tmwp / 100.0) * (mnsmwp / 100.0)) * 100;
				// log.debug("---Male Result Mortality Weight Double -----" +
				// resultantMortalityDouble);
			} else if (clientGender.equalsIgnoreCase("Female")) {
				resultantMortalityDouble = ((imwp / 100.0) * (gmwp / 100.0) * (fmwp / 100.0)) * 100;
				// log.debug("---Female Result Mortality Weight Double -----" +
				// resultantMortalityDouble);
			}

			//int resultantMortalityInt = (int) Math.floor(resultantMortalityDouble);
			//log.debug("---Result Mortality Weight Int -----" + resultantMortalityInt);

			// Getting value from database
			Hashtable<Integer, Double> ageMortalityTable = getAgeMortalityTable();
			/*
			 * for (int i = 0; i < ageMortalityTable.size(); i++) { log.debug("Value of " + i +
			 * " is " + ageMortalityTable.get(i)) ; }
			 */
			// Calulating the current life in years

			//int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			// log.debug("LastIndex of /" + clientDob.lastIndexOf("/"));
			// log.debug("Value---" + clientDob.substring(clientDob.lastIndexOf("/") + 1).trim()
			// );
			/*Calendar cal = Calendar.getInstance();
			cal.setTime(clientDob);
			int dobYear = cal.get(Calendar.YEAR);*/
			//log.debug("lifeExpectancyDTO.getFamilyMemberId() "+lifeExpectancyDTO.getFamilyMemberId());
			int currentLifeYear=getAgeForLifeExpectancy(lifeExpectancyDTO);
			
			//log.debug("dobYear----" + currentLifeYear);
			//log.debug("currentYear----" + currentYear);
			//log.debug("currentLifeYear----" + currentLifeYear);
			Hashtable<Integer, Double> agelxTable = new Hashtable<Integer, Double>();
			double startingLx = 1.0;
			agelxTable.put(0, startingLx);
			for (int i = 1; i < ageMortalityTable.size(); i++) {
				double lx = agelxTable.get(i - 1) - ((resultantMortalityDouble / 100.0)
						* (ageMortalityTable.get(i)) * agelxTable.get(i - 1));
				// log.debug("lx for " + i + " --*******" + lx);
				agelxTable.put(i, lx);
			}
			double futureLifeExpDouble = 0.0;
			for (int i = currentLifeYear; i < ageMortalityTable.size(); i++) {
				futureLifeExpDouble = futureLifeExpDouble + agelxTable.get(i);
			}
			 //log.debug("Future Life Exp Double" + futureLifeExpDouble);
			int futureLifeExpInt = (int) Math.round(futureLifeExpDouble);
			//log.debug("Future Life Exp Int" + futureLifeExpInt);
			//log.debug("totalLifeExp before "+(currentLifeYear + futureLifeExpDouble));
			Byte totalLifeExp =  (byte) Math.round(currentLifeYear + futureLifeExpDouble);
			//log.debug("Total_Life_Expectancy after" + totalLifeExp);

			LifeExpectancyDTO outputDTO = new LifeExpectancyDTO();
			outputDTO.setFutureLifeExpectancy(futureLifeExpInt);
			outputDTO.setTotalLifeExpectancy(totalLifeExp);
			
			

			return outputDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	private Hashtable<Integer, Double> getAgeMortalityTable() throws RuntimeException, CustomFinexaException{
		// TODO Auto-generated method stub
		try {
			Hashtable<Integer, Double> ageMortalityTable = new Hashtable<Integer, Double>();
			List<MasterAgeMortalityRate> mortalityRateList = masterAgeMortRepo.findAll();
			if (mortalityRateList != null && !mortalityRateList.isEmpty()) {
				for (MasterAgeMortalityRate obj : mortalityRateList) {
					ageMortalityTable.put(obj.getAge(), obj.getMortalityRate());
					//ageMortalityTable.put(null, null);
				}
			}
			return ageMortalityTable;
		} catch (RuntimeException e) {
			throw new CustomFinexaException(FinexaConstant.CLIENT_LIFE_EXPECTANCY_MODULE, "Nothing Specific", 
					"Failed to Get Age Mortality Table");
		}
	}

	public int getAgeForLifeExpectancy(LifeExpectancyDTO lifeExpectancyDTO) throws CustomFinexaException {
		//log.debug("f id "+lifeExpectancyDTO.getFamilyMemberId());
		//ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(familymenberId);
		try {
			Date dob1 = lifeExpectancyDTO.getBirthDate();

			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dob = formatter.format(dob1);

			// TAKE SUBSTRINGS OF THE DOB SO SPLIT OUT YEAR, MONTH AND DAY
			// INTO SEPERATE VARIABLES
			int yearDOB = Integer.parseInt(dob.substring(0, 4));
			int monthDOB = Integer.parseInt(dob.substring(5, 7));
			int dayDOB = Integer.parseInt(dob.substring(8, 10));

			// CALCULATE THE CURRENT YEAR, MONTH AND DAY
			// INTO SEPERATE VARIABLES
			DateFormat dateFormat = new SimpleDateFormat("yyyy");
			java.util.Date date = new java.util.Date();
			int thisYear = Integer.parseInt(dateFormat.format(date));

			dateFormat = new SimpleDateFormat("MM");
			date = new java.util.Date();
			int thisMonth = Integer.parseInt(dateFormat.format(date));

			dateFormat = new SimpleDateFormat("dd");
			date = new java.util.Date();
			int thisDay = Integer.parseInt(dateFormat.format(date));

			// CREATE AN AGE VARIABLE TO HOLD THE CALCULATED AGE
			// TO START WILL SET THE AGE EQUEL TO THE CURRENT YEAR MINUS THE YEAR
			// OF THE DOB
			int age = thisYear - yearDOB;

			// IF THE CURRENT MONTH IS LESS THAN THE DOB MONTH
			// THEN REDUCE THE DOB BY 1 AS THEY HAVE NOT HAD THEIR
			// BIRTHDAY YET THIS YEAR
			if (thisMonth < monthDOB) {
				age = age - 1;
			}

			// IF THE MONTH IN THE DOB IS EQUAL TO THE CURRENT MONTH
			// THEN CHECK THE DAY TO FIND OUT IF THEY HAVE HAD THEIR
			// BIRTHDAY YET. IF THE CURRENT DAY IS LESS THAN THE DAY OF THE DOB
			// THEN REDUCE THE DOB BY 1 AS THEY HAVE NOT HAD THEIR
			// BIRTHDAY YET THIS YEAR
			if (thisMonth == monthDOB && thisDay < dayDOB) {
				age = age - 1;
			}
			return age;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException(FinexaConstant.CLIENT_LIFE_EXPECTANCY_MODULE, "Nothing Specific", 
					"Failed to Get Age for Life Expectancy");
		}
	}
	
	@Override
	public List<ClientLifeExpDTO> viewLifeExpList(Integer inClientId) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			List<ClientLifeExpDTO> clientLifeExpDTOList = new ArrayList<>();
		//	List<ClientFamilyMemberDTO> familymemberDTOList = new ArrayList<>();
			ClientMaster clientMaster = clientMasterRepository.findOne(inClientId);
			List<ClientFamilyMember> familymemberList = clientMaster.getClientFamilyMembers();

			for (ClientFamilyMember obj : familymemberList) {

				if (obj.getLifeExpectancy() != null) {
					ClientLifeExpDTO clientLifeExpDTO = new ClientLifeExpDTO();
					String name = obj.getFirstName();
					if (obj.getMiddleName() != null && !obj.getMiddleName().equals("")) {
						name = name + " " + obj.getMiddleName();
					}
					name = name + " " + obj.getLastName();
					clientLifeExpDTO.setMemberId(obj.getId());
					clientLifeExpDTO.setRelationId(obj.getLookupRelation().getId());
					if (obj.getLookupRelation().getId() == 0) {
						clientLifeExpDTO.setMemberGender(clientMaster.getGender());
					} else if (obj.getLookupRelation().getId() == 1) {
						if(clientMaster.getGender().equals("M")) {
							clientLifeExpDTO.setMemberGender("F");
						} else {
							clientLifeExpDTO.setMemberGender("M");
						}
					}
					clientLifeExpDTO.setMemberFirstName(obj.getFirstName());
					clientLifeExpDTO.setMemberName(name);
					clientLifeExpDTO.setMemberRelation(obj.getLookupRelation().getDescription());
					clientLifeExpDTO.setLifeExp(obj.getLifeExpectancy());
					clientLifeExpDTO.setRelationName(obj.getLookupRelation().getDescription());
					clientLifeExpDTOList.add(clientLifeExpDTO);
				}
			}
			return clientLifeExpDTOList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientFamilyMemberDTO> getFamilyMemberByLifeExpectancy(int clientId) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			List<ClientFamilyMemberDTO> familyMemberDTOList = new ArrayList<>();
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			List<ClientFamilyMember> familyMemberList = clientMaster.getClientFamilyMembers();
			for(ClientFamilyMember obj : familyMemberList) {
				if (obj.getLifeExpectancy() == null) {
					ClientFamilyMemberDTO famDTO = mapper.map(obj, ClientFamilyMemberDTO.class);
					if (obj.getLookupRelation().getId() == 0) {
						famDTO.setGender(clientMaster.getGender());
					} else if (obj.getLookupRelation().getId() == 1) {
						if(clientMaster.getGender().equals("M")) {
							famDTO.setGender("F");
						} else {
							famDTO.setGender("M");
						}
					}
					famDTO.setRelationID(obj.getLookupRelation().getId());
					famDTO.setRelationName(obj.getLookupRelation().getDescription());
					famDTO.setClientID(obj.getClientMaster().getId());
					familyMemberDTOList.add(famDTO);
				} else {
					if (obj.getLifeExpectancy() != null && obj.getLifeExpectancy() == 0) {
						ClientFamilyMemberDTO famDTO = mapper.map(obj, ClientFamilyMemberDTO.class);
						famDTO.setRelationID(obj.getLookupRelation().getId());
						if (obj.getLookupRelation().getId() == 0) {
							famDTO.setGender(clientMaster.getGender());
						} else if (obj.getLookupRelation().getId() == 1) {
							if(clientMaster.getGender().equals('M')) {
								famDTO.setGender("F");
							} else {
								famDTO.setGender("M");
							}
						}
						famDTO.setRelationName(obj.getLookupRelation().getDescription());
						famDTO.setClientID(obj.getClientMaster().getId());
						familyMemberDTOList.add(famDTO);
					}
				}
			}
			return familyMemberDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientLifeExpDTO> deleteLifeExp(int familyMemberId) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			ClientFamilyMember familyMember = clientFamilyMemberRepository.findOne(familyMemberId);
			ClientMaster clientMaster = familyMember.getClientMaster();

			// delete life Exp ie set life exp to null
			familyMember.setLifeExpectancy(null);
			familyMember.setHasDiseaseHistory("Y");
			familyMember.setHasNormalBP("Y");
			familyMember.setIsProperBMI("Y");
			familyMember.setIsTobaccoUser("N");
			clientFamilyMemberRepository.save(familyMember);

			List<ClientLifeExpDTO> lifeExpDTOList = viewLifeExpList(clientMaster.getId());
			return lifeExpDTOList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public LifeExpectancyDTO reCalculateLifeExpectancyForFamilyMember(ClientFamilyMemberDTO clientFamilyMemberDTO) throws RuntimeException, CustomFinexaException {
        
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientFamilyMemberDTO.getClientID());
			ClientFamilyMember clientFamilyMember=clientFamilyMemberRepository.findOne(clientFamilyMemberDTO.getId());
			
			LifeExpectancyDTO lifeExpectancyDTO=new LifeExpectancyDTO();
			lifeExpectancyDTO.setFamilyMemberId(clientFamilyMemberDTO.getId());
			lifeExpectancyDTO.setId(clientFamilyMemberDTO.getClientID());
			//	clientFamilyMember.setLookupRelation(lookupRelationshipRepository.findOne(clientFamilyMemberDTO.getRelationID()));

			String gender=FinexaUtil.getGender(cm, clientFamilyMember.getLookupRelation());
			lifeExpectancyDTO.setGender(gender);
			lifeExpectancyDTO.setBirthDate(clientFamilyMemberDTO.getBirthDate());
			
			List<ClientFamilyIncomeDTO> lstIncome=clientFamilyIncomeService.getAllIncomeForFamilyMember(clientFamilyMemberDTO.getClientID(), clientFamilyMemberDTO.getId());
			if (lstIncome.size()> 0) {
				int s=lstIncome.size();
			  lifeExpectancyDTO.setAnnualIncome(lstIncome.get(s-1).getTotal().doubleValue());
			 
			}
			//log.debug("Annual Income re: " + lifeExpectancyDTO.getAnnualIncome());
			
			
			
			lifeExpectancyDTO.setHasDiseaseHistory(clientFamilyMember.getHasDiseaseHistory());
			lifeExpectancyDTO.setHasNormalBP(clientFamilyMember.getHasNormalBP());
			lifeExpectancyDTO.setIsProperBMI(clientFamilyMember.getIsProperBMI());
			lifeExpectancyDTO.setIsTobaccoUser(clientFamilyMember.getIsTobaccoUser());
			
			//log.debug("HasDiseaseHistory(): " + lifeExpectancyDTO.getHasDiseaseHistory());
			//log.debug("HasNormalBP: " + lifeExpectancyDTO.getHasNormalBP());
			//log.debug("IsProperBMI(): " + lifeExpectancyDTO.getIsProperBMI());
			//log.debug("IsTobaccoUser: " + lifeExpectancyDTO.getIsTobaccoUser());

			
			LifeExpectancyDTO lifeExpDTO = calculateLifeExp(lifeExpectancyDTO);
			
   
			return lifeExpDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public LifeExpectancyDTO reCalculateLifeExpectancyForClient(LifeExpectancyDTO lifeExpectancyDTO) throws RuntimeException, CustomFinexaException {
        
		try {
			ClientFamilyMember clientFamilyMember=clientFamilyMemberRepository.findOne(lifeExpectancyDTO.getFamilyMemberId());
			
			
			if(lifeExpectancyDTO.getGender().equalsIgnoreCase("F")){
				lifeExpectancyDTO.setGender("Female");
			}
			if(lifeExpectancyDTO.getGender().equalsIgnoreCase("M")){
				lifeExpectancyDTO.setGender("Male");
			}
			List<ClientFamilyIncomeDTO> lstIncome=clientFamilyIncomeService.getAllIncomeForFamilyMember(lifeExpectancyDTO.getId(), lifeExpectancyDTO.getFamilyMemberId());
			if (lstIncome.size()> 0) {
				int s=lstIncome.size();
			  lifeExpectancyDTO.setAnnualIncome(lstIncome.get(s-1).getTotal().doubleValue());
			 
			}
			log.debug("Annual Income re: " + lifeExpectancyDTO.getAnnualIncome());
			
			
			
			lifeExpectancyDTO.setHasDiseaseHistory(clientFamilyMember.getHasDiseaseHistory());
			lifeExpectancyDTO.setHasNormalBP(clientFamilyMember.getHasNormalBP());
			lifeExpectancyDTO.setIsProperBMI(clientFamilyMember.getIsProperBMI());
			lifeExpectancyDTO.setIsTobaccoUser(clientFamilyMember.getIsTobaccoUser());
			
			LifeExpectancyDTO lifeExpDTO = calculateLifeExp(lifeExpectancyDTO);
			

			return lifeExpDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	
	


}

/*@Override
public LifeExpectancyDTO calculateLifeExp(LifeExpectancyDTO lifeExpectancyDTO) {
	// TODO Auto-generated method stub
	if(lifeExpectancyDTO.getFamilyMemberId()!=0){
	int familyMemberId = lifeExpectancyDTO.getFamilyMemberId();
	log.debug("----familyMemberId----" + familyMemberId);
	}
	Date clientDob = lifeExpectancyDTO.getBirthDate();
	log.debug("----clientDob----" + clientDob);
	String clientGender = lifeExpectancyDTO.getGender();
	clientGender = clientGender.trim();
	if(clientGender.equalsIgnoreCase("Female")) {
		clientGender = "Female";
	} else {
		clientGender = "Male";
	}
	log.debug("----clientGender----" + clientGender);

	double clientIncome = lifeExpectancyDTO.getAnnualIncome();
	log.debug("----clientIncome----" + clientIncome);
	String isTobaccoUser = lifeExpectancyDTO.getIsTobaccoUser();
	log.debug("----isTobaccoUser----" + isTobaccoUser);
	String isNormalBMI = lifeExpectancyDTO.getIsProperBMI();
	log.debug("----isNormalBMI----" + isNormalBMI);
	String hasDiseaseHistory = lifeExpectancyDTO.getHasDiseaseHistory();
	log.debug("----hasDiseaseHistory----" + hasDiseaseHistory);
	String hasBloodPressure = lifeExpectancyDTO.getHasNormalBP();
	log.debug("----hasBloodPressure----" + hasBloodPressure);


	// Calculating Health Status

	String healthStatus = "Average";
	int noOfYes = 0;
	if (hasBloodPressure.equalsIgnoreCase("on") || hasBloodPressure.equalsIgnoreCase("Y")) {
		noOfYes++;
	}
	if (isNormalBMI.equalsIgnoreCase("on") || isNormalBMI.equalsIgnoreCase("Y")) {
		noOfYes++;
	}
	if (hasDiseaseHistory.equalsIgnoreCase("on") || hasDiseaseHistory.equalsIgnoreCase("Y")) {
		noOfYes++;
	}
	if (noOfYes == 3) {
		healthStatus = "Perfectly Fit";
	} else if (noOfYes == 0) {
		healthStatus = "Poor";
	}

	log.debug("---healthStatus -----" + healthStatus);

	// Calculating Income Mortality Weight
	int imwp = 0;

	if (clientIncome >= 0 && clientIncome <= 250000) {
		imwp = 160;
	} else if (clientIncome > 250000 && clientIncome <= 500000) {
		imwp = 100;
	} else if (clientIncome > 500000 && clientIncome <= 1000000) {
		imwp = 80;
	} else if (clientIncome > 1000000 && clientIncome <= 2500000) {
		imwp = 60;
	} else if (clientIncome > 2500000) {
		imwp = 40;
	}

	log.debug("---Income Mortality Weight -----" + imwp);

	// Calculating Gender Mortality Weight
	int gmwp = 0;
	log.debug("----clientGender----" + clientGender);
	if (clientGender.equalsIgnoreCase("Male")) {
		gmwp = 100;
	} else if (clientGender.equalsIgnoreCase("Female")) {
		gmwp = 80;
	}
	log.debug("---Gender Mortality Weight -----" + gmwp);
	// Calculating Tobacco Mortality Weight
	int tmwp = 0;
	if (clientGender.equalsIgnoreCase("Male")) {
		if (isTobaccoUser.equalsIgnoreCase("on") || isTobaccoUser.equalsIgnoreCase("Y")) {
			tmwp = 150;
		} else if (isTobaccoUser.equalsIgnoreCase("off") || isTobaccoUser.equalsIgnoreCase("N")) {
			tmwp = 100;
		}
		log.debug("---Male Tobacco Mortality Weight -----" + tmwp);
	}

	// Male Non Smoker
	int mnsmwp = 0;
	if (clientGender.equalsIgnoreCase("Male")) {

		if (isTobaccoUser.equalsIgnoreCase("on")) {
			mnsmwp = 100;
		} else {
			if (healthStatus.equalsIgnoreCase("Perfectly Fit")) {
				mnsmwp = 85;
			} else if (healthStatus.equalsIgnoreCase("Average")) {
				mnsmwp = 95;
			} else if (healthStatus.equalsIgnoreCase("Poor")) {
				mnsmwp = 120;
			}
		}

		log.debug("---Male Non Smoker Mortality Weight -----" + mnsmwp);
	}
	// Female

	int fmwp = 0;
	if (clientGender.equalsIgnoreCase("Female")) {
		if (healthStatus.equalsIgnoreCase("Perfectly Fit")) {
			fmwp = 85;
		} else if (healthStatus.equalsIgnoreCase("Average")) {
			fmwp = 100;
		} else if (healthStatus.equalsIgnoreCase("Poor")) {
			fmwp = 120;
		}
		log.debug("---Female Mortality Weight -----" + fmwp);

	}

	// Calculating applied mortality
	double resultantMortalityDouble = 0;
	if (clientGender.equalsIgnoreCase("Male")) {
		resultantMortalityDouble =
				((imwp / 100.0) * (gmwp / 100.0) * (tmwp / 100.0) * (mnsmwp / 100.0)) * 100;
		// log.debug("---Male Result Mortality Weight Double -----" +
		// resultantMortalityDouble);
	} else if (clientGender.equalsIgnoreCase("Female")) {
		resultantMortalityDouble = ((imwp / 100.0) * (gmwp / 100.0) * (fmwp / 100.0)) * 100;
		// log.debug("---Female Result Mortality Weight Double -----" +
		// resultantMortalityDouble);
	}

	int resultantMortalityInt = (int) Math.floor(resultantMortalityDouble);
	log.debug("---Result Mortality Weight Int -----" + resultantMortalityInt);

	// Getting value from database
	Hashtable<Integer, Double> ageMortalityTable = getAgeMortalityTable();
	
	 * for (int i = 0; i < ageMortalityTable.size(); i++) { log.debug("Value of " + i +
	 * " is " + ageMortalityTable.get(i)) ; }
	 
	// Calulating the current life in years

	int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	// log.debug("LastIndex of /" + clientDob.lastIndexOf("/"));
	// log.debug("Value---" + clientDob.substring(clientDob.lastIndexOf("/") + 1).trim()
	// );
	Calendar cal = Calendar.getInstance();
	cal.setTime(clientDob);
	int dobYear = cal.get(Calendar.YEAR);
    
	log.debug("dobYear----" + dobYear);
	log.debug("currentYear----" + currentYear);
	int currentLifeYear = currentYear - dobYear;
	log.debug("currentLifeYear----" + currentLifeYear);
	Hashtable<Integer, Double> agelxTable = new Hashtable<Integer, Double>();
	double startingLx = 1.0;
	agelxTable.put(0, startingLx);
	for (int i = 1; i < ageMortalityTable.size(); i++) {
		double lx = agelxTable.get(i - 1) - ((resultantMortalityDouble / 100.0)
				* (ageMortalityTable.get(i)) * agelxTable.get(i - 1));
		// log.debug("lx for " + i + " --*******" + lx);
		agelxTable.put(i, lx);
	}
	double futureLifeExpDouble = 0.0;
	for (int i = currentLifeYear; i < ageMortalityTable.size(); i++) {
		futureLifeExpDouble = futureLifeExpDouble + agelxTable.get(i);
	}
	 log.debug("Future Life Exp Double" + futureLifeExpDouble);
	int futureLifeExpInt = (int) Math.round(futureLifeExpDouble);
	log.debug("Future Life Exp Int" + futureLifeExpInt);

	int totalLifeExp =  (int) Math.round(currentLifeYear + futureLifeExpDouble);
	log.debug("Total_Life_Expectancy" + totalLifeExp);

	LifeExpectancyDTO outputDTO = new LifeExpectancyDTO();
	outputDTO.setFutureLifeExpectancy(futureLifeExpInt);
	outputDTO.setTotalLifeExpectancy(totalLifeExp);
	
	

	return outputDTO;

}*/
/*@Override
public LifeExpectancyDTO findByMemberId(int memberId) {
	LifeExpectancyDTO lifeExpDTO = new LifeExpectancyDTO();
	log.debug("memberId "+memberId);
	// getting the particular family member
	ClientFamilyMember clientFamilyMember=clientFamilyMemberRepository.findOne(memberId);
	log.debug("clientfam "+clientFamilyMember.getId());
	log.debug("clientMasterRepository.findOne(clientId) "+clientFamilyMember.getClientMaster().getId());
//	ClientFamilyMemberDTO clientFamilyMemberDTO=mapper.map(clientFamilyMember, ClientFamilyMemberDTO.class);
	int clientId = clientFamilyMember.getClientMaster().getId();
	
	// getting the client for family member for getting the gender
	ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
	ClientMasterDTO clientMasterDTO = mapper.map(clientMaster, ClientMasterDTO.class);
	String genderOfClient = "";
	if (clientMaster != null) {
		genderOfClient = clientMasterDTO.getGender();
	}
	int relation = -1;
	if (clientFamilyMember.getLookupRelation().getId() != null) {
		relation = clientFamilyMember.getLookupRelation().getId();
	}
	String genderOfMember = "";
	if (relation >= 0 && !genderOfClient.equals("")) {
		switch (relation) {
		case 0:
			if(genderOfClient.equals("M")) {
				genderOfMember = "Male";
			} else {
				genderOfMember = "Female";
			}
			break;
		case 1:
			if(genderOfClient.equals("M")) {
				genderOfMember = "Female";
			} else {
				genderOfMember = "Male";
			}
			break;
		case 2:
			genderOfMember = "Male";
			break;
		case 3:
			genderOfMember = "Female";
			break;
		case 4:
			genderOfMember = "Male";
			break;
		case 5:
			genderOfMember = "Female";
			break;
		case 6:
			genderOfMember = "Male";
			break;
		case 7:
			genderOfMember = "Female";
			break;
		case 8:
			genderOfMember = "Male";
			break;
		default:
			break;
		}
	}

	// getting annual income of memberId
	List<ClientFamilyIncome> clientFamilyIncomeList = clientFamilyMember.getClientFamilyIncomes();
	double income = 0.0;
	for (ClientFamilyIncome obj : clientFamilyIncomeList) {
		if (obj != null && obj.getIncomeAmount() != null) {
			income = income + obj.getIncomeAmount().doubleValue()*obj.getLookupFrequency().getId();
		}
	}
	java.sql.Timestamp sq = new java.sql.Timestamp(clientFamilyMember.getBirthDate().getTime());
	
	log.debug("date "+clientFamilyMember.getBirthDate());
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String bdate = df.format(clientFamilyMember.getBirthDate());
		String year1 = bdate.substring(0,4);
		String month = bdate.substring(5,7);
		String day = bdate.substring(8,10);
		
	
		String bDate=day+"/"+month+"/"+year1;
		Date bDate1 = null;
		log.debug("clientDate "+bDate);
		try {
			 df = new SimpleDateFormat("dd/MM/yyyy");
			bDate1 = df.parse(bDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("clientDate1 "+bDate1);
	
	lifeExpDTO.setBirthDate(bDate1);
	lifeExpDTO.setAnnualIncome(income);
	lifeExpDTO.setGender(genderOfMember);
	lifeExpDTO.setFamilyMemberId(memberId);
	lifeExpDTO.setId(clientId);
	lifeExpDTO.setIsTobaccoUser(clientFamilyMember.getIsTobaccoUser());
	lifeExpDTO.setHasNormalBP(clientFamilyMember.getHasNormalBP());
	lifeExpDTO.setHasDiseaseHistory(clientFamilyMember.getHasDiseaseHistory());
	lifeExpDTO.setIsProperBMI(clientFamilyMember.getIsProperBMI());

	if (clientFamilyMember.getLifeExpectancy() != null) {
		lifeExpDTO.setTotalLifeExpectancy(clientFamilyMember.getLifeExpectancy());
		Calendar cal = Calendar.getInstance();
		cal.setTime(clientFamilyMember.getBirthDate());
		int lifeExpYear = clientFamilyMember.getLifeExpectancy() + cal.get(Calendar.YEAR);
		cal = Calendar.getInstance();
		int futureLifeExp = lifeExpYear - cal.get(Calendar.YEAR);
		lifeExpDTO.setFutureLifeExpectancy(futureLifeExp);
	}
	return lifeExpDTO;
}
*/