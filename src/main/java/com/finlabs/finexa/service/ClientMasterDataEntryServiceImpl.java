package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import com.finlabs.finexa.dto.ClientInfoDTO;
import com.finlabs.finexa.dto.InvestorMasterSearchDTO;
import com.finlabs.finexa.dto.LifeExpectancyDTO;
import com.finlabs.finexa.dto.SearchClientDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientARNMapping;
import com.finlabs.finexa.model.ClientAccessRight;
import com.finlabs.finexa.model.ClientCash;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientFamilyIncome;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.InvestorMasterBO;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.LookupIncomeExpenseDuration;
import com.finlabs.finexa.model.LookupMaritalStatus;
import com.finlabs.finexa.model.LookupRelation;
import com.finlabs.finexa.model.LookupResidentType;
import com.finlabs.finexa.model.StagingFolioMasterBO;
import com.finlabs.finexa.model.StagingInvestorMasterBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AuxillaryFamilyMemberRepository;
import com.finlabs.finexa.repository.AuxillaryMasterRepository;
import com.finlabs.finexa.repository.ClientARNMappingRepository;
import com.finlabs.finexa.repository.ClientAccessRightsRepository;
import com.finlabs.finexa.repository.ClientContactRepository;
import com.finlabs.finexa.repository.ClientFamilyIncomeRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.EmploymentTypeRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.repository.FrequencyRepository;
import com.finlabs.finexa.repository.IncomeExpenseDurationRepository;
import com.finlabs.finexa.repository.InvestMasterBORepository;
import com.finlabs.finexa.repository.LookupCountryRepository;
import com.finlabs.finexa.repository.LookupRelationshipRepository;
import com.finlabs.finexa.repository.LookupResidentTypeRepository;
import com.finlabs.finexa.repository.MaritalStatusRepository;
import com.finlabs.finexa.repository.MonthRepository;
import com.finlabs.finexa.repository.ResidentTypeRepository;
import com.finlabs.finexa.repository.StagingFolioMasterBORepository;
import com.finlabs.finexa.repository.StagingInvestorMasterBORepository;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;
import com.finlabs.finexa.util.InvestorMasterSpecification;

@Service("ClientMasterDataEntryService")
@Transactional
public class ClientMasterDataEntryServiceImpl implements ClientMasterDataEntryService {

	private static Logger log = LoggerFactory.getLogger(ClientMasterDataEntryServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientMasterService clientService;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private MaritalStatusRepository maritalStatusRepository;

	@Autowired
	private LookupCountryRepository lookupCountryRepository;

	@Autowired
	EmploymentTypeRepository employmentTypeRepository;

	@Autowired
	ResidentTypeRepository residentTypeRepository;

	@Autowired
	LookupRelationshipRepository lookupRelationshipRepository;

	@Autowired
	ClientContactRepository clientContactRepository;

	@Autowired
	private InvestMasterBORepository investorRepo;

	@Autowired
	private ClientLifeExpectancyService lifeExpectancyService;

	@Autowired
	private ClientFamilyIncomeRepository clientFamilyIncomeRepository;

	@Autowired
	private FrequencyRepository frequencyRepository;

	@Autowired
	private MonthRepository monthRepository;

	@Autowired
	private IncomeExpenseDurationRepository incomeExpenseDurationRepository;

	@Autowired
	private AuxillaryMasterRepository auxillaryMasterRepository;

	@Autowired
	private AuxillaryFamilyMemberRepository auxFamilyMemberRepo;

	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;

	@Autowired
	StagingInvestorMasterBORepository stagingInvestorMasterBORepository;

	@Autowired
	StagingFolioMasterBORepository stagingFolioMasterBORepository;

	@Autowired
	ClientAccessRightsRepository clientAccessRightsRepository;
	
	@Autowired
	private ClientARNMappingRepository clientARNMappingRepository;

	public static final String MobileNumberLength1 = "10";
	public static final String MobileNumberLength2 = "11";
	public static final String MobileNumberLength3 = "13";
	private final Integer ADULT_AGE = 18;
	private final int RELATION_ID = 0;
	private Integer clientID;
	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd/MM/yyyy");
	public List<ClientARNMapping> clientARNMappingList = new ArrayList<>();
	@Override
	public List<InvestorMasterSearchDTO> getInvestorDetailsByNamePan(String name, String pan)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub

		try {
			List<InvestorMasterSearchDTO> investorMasterSearchDTOList = new ArrayList<>();
			SearchClientDTO searchClientDTO = new SearchClientDTO();
			searchClientDTO.setSearchName(name);
			searchClientDTO.setSearchPan(pan);

			Specification<InvestorMasterBO> spec = InvestorMasterSpecification
					.findAllClientRecordsByCriteria(searchClientDTO);
			List<InvestorMasterBO> list = investorRepo.findAll(spec);
			log.debug("Search Result Size = " + list.size());
			for (InvestorMasterBO client : list) {
				InvestorMasterSearchDTO obj = new InvestorMasterSearchDTO();
				obj.setInvestorName(client.getInvestorName());
				obj.setInvestorPan(client.getId().getInvestorPAN());
				obj.setInvestorFolioNo(client.getId().getFolioNumber());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				if (client.getInvestorDOB() != null) {
					String dateInString = sdf.format(client.getInvestorDOB());
					obj.setInvestorDOB("" + dateInString);
				}
				obj.setInvestorAdhaar(client.getInvestorAdhaarNumber());
				obj.setInvestorAddressLine1(client.getAddressLine1());
				obj.setInvestorAdressLine2(client.getAddressLine2());
				obj.setInvestorAddressLine3(client.getAddressLine3());
				obj.setInvestorEmail(client.getEmail());
				obj.setInvestorMobile(client.getMobileNumber());
				obj.setInvestorCity(client.getCity());
				obj.setInvestorPinCode(client.getPincode());
				obj.setDistributorARNCode(client.getDistributorARNCode());
				investorMasterSearchDTOList.add(obj);
			}
			return investorMasterSearchDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<InvestorMasterSearchDTO> getStagingInvestorDetailsByNamePan(int advisorId, String name, String pan)
			throws FinexaBussinessException {
		String familyMemberName;
		String firstName;
		String middleName;
		String lastName;
		// TODO Auto-generated method stub
		byte relationID = 0;
		try {
			List<InvestorMasterSearchDTO> investorMasterSearchDTOList = new ArrayList<>();
			List<StagingInvestorMasterBO> list = new ArrayList<StagingInvestorMasterBO>();
			List<ClientMaster> allClientUnderAnAdvisorList = new ArrayList<ClientMaster>();
			List<ClientFamilyMember> allFamilyMemberUnderAnAdvisorList = new ArrayList<ClientFamilyMember>();
			List<ClientFamilyMember> familyMemberUnderAnAdvisorList = new ArrayList<ClientFamilyMember>();

			System.out.println("name: " + name + " pan: " + pan);

			AdvisorUser advUser = advisorUserRepository.findById(advisorId);
			allClientUnderAnAdvisorList = clientMasterRepository.findByAdvisorUser(advUser);
			if (allClientUnderAnAdvisorList.size() > 0) {

				for (ClientMaster clientMaster : allClientUnderAnAdvisorList) {
					try {

						familyMemberUnderAnAdvisorList = clientFamilyMemberRepository
								.findByClientIdAndRelationId(clientMaster.getId(), relationID);
						if (familyMemberUnderAnAdvisorList.size() > 0) {
							allFamilyMemberUnderAnAdvisorList.addAll(familyMemberUnderAnAdvisorList);
						}

					} catch (Exception e) {
						e.printStackTrace();

					}
				}
			}
			if (!name.isEmpty() && !pan.isEmpty()) {
				list = stagingInvestorMasterBORepository
						.findDistinctByAdvisoruserAndInvestorPANAndInvestorName(advisorId, "%" + name + "%", pan);
			} else if (name.isEmpty() && !pan.isEmpty()) {
				list = stagingInvestorMasterBORepository.findDistinctByAdvisoruserAndInvestorPAN(advisorId, pan);
			} else if (!name.isEmpty() && pan.isEmpty()) {
				list = stagingInvestorMasterBORepository.findDistinctByAdvisoruserAndInvestorName(advisorId,
						"%" + name + "%");
			}

			log.debug("Search Result Size = " + list.size());
			for (StagingInvestorMasterBO client : list) {
				try {
					InvestorMasterSearchDTO obj = new InvestorMasterSearchDTO();
					obj.setInvestorName(client.getInvestorName());
					obj.setInvestorPan(client.getInvestorPAN());
					// obj.setInvestorFolioNo(client..getFolioNumber());
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					if (client.getInvestorDOB() != null) {
						String dateInString = sdf.format(client.getInvestorDOB());
						obj.setInvestorDOB("" + dateInString);
					}

					obj.setInvestorAdhaar(client.getInvestorAadhar());
					obj.setInvestorAddressLine1(client.getAddressLine1());
					obj.setInvestorAdressLine2(client.getAddressLine2());
					obj.setInvestorAddressLine3(client.getAddressLine3());
					obj.setInvestorEmail(client.getEmail());
					obj.setInvestorMobile(client.getMobileNumber());
					obj.setInvestorCity(client.getCity());
					obj.setInvestorPinCode(client.getPinCode());
					obj.setDistributorARNCode(client.getDistributorARNCode());

					ClientMaster cm = clientMasterRepository.findByAdvisorIdAndPan(advUser.getId(),
							client.getInvestorPAN());
					obj.setAccountStatus(0);

					String clientName = (obj.getInvestorName()).replaceAll("\\s", "");
					System.out.println("Client Name" + clientName);

					for (ClientFamilyMember clientFamilyMember : allFamilyMemberUnderAnAdvisorList) {
						try {
							firstName = (clientFamilyMember.getFirstName()).trim();
							if ((StringUtils.isNotEmpty(clientFamilyMember.getMiddleName()))
									&& StringUtils.isNotEmpty(clientFamilyMember.getMiddleName())) {

								middleName = (clientFamilyMember.getMiddleName()).trim();
								lastName = (clientFamilyMember.getLastName()).trim();
								familyMemberName = firstName + middleName + lastName;

							} else if (StringUtils.isNotEmpty(clientFamilyMember.getMiddleName())
									&& clientFamilyMember.getLastName().isEmpty()) {
								middleName = (clientFamilyMember.getMiddleName()).trim();
								familyMemberName = firstName + middleName;
							}

							else {
								lastName = (clientFamilyMember.getLastName()).trim();
								familyMemberName = firstName + lastName;
							}
							familyMemberName = familyMemberName.replaceAll("\\s", "");
							if ((clientName.toLowerCase()).equals(familyMemberName.toLowerCase())) {
								obj.setAccountStatus(2);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (cm != null) {
						obj.setAccountStatus(1);
					} /*
						 * else { obj.setFinexaClient(false); }
						 */

					investorMasterSearchDTOList.add(obj);
				} catch (Exception e) {
					// e.printStackTrace();
					System.out.println(e);
				}
			}

			return investorMasterSearchDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	/*
	 * @Override public List<InvestorMasterSearchDTO>
	 * createClient(List<InvestorMasterSearchDTO> investDTOList) throws
	 * FinexaBussinessException {
	 * 
	 * try { if (investDTOList != null && investDTOList.size() > 0) {
	 * 
	 * String mobile = ""; String email = " "; String address1 = ""; String pinCode
	 * = ""; /* This part of code is done as in some records email is present while
	 * in some it is not. Ideally these are all the same person hence we are
	 * assuming any one as the correct email and mobile
	 * 
	 * for (InvestorMasterSearchDTO obj : investDTOList) { if
	 * (obj.getInvestorMobile() != null && !obj.getInvestorMobile().equals("")) {
	 * mobile = obj.getInvestorMobile(); } }
	 * 
	 * for (InvestorMasterSearchDTO obj : investDTOList) { if
	 * (obj.getInvestorEmail() != null && !obj.getInvestorEmail().equals("")) {
	 * email = obj.getInvestorEmail(); } } for (InvestorMasterSearchDTO obj :
	 * investDTOList) { if (obj.getInvestorAddressLine1() != null &&
	 * !obj.getInvestorAddressLine1().equals("")) { address1 =
	 * obj.getInvestorAddressLine1(); } }
	 * 
	 * for (InvestorMasterSearchDTO obj : investDTOList) { if
	 * ((obj.getInvestorPinCode()) != null && !obj.getInvestorPinCode().equals(""))
	 * { pinCode = (obj.getInvestorPinCode()); } else { pinCode = "0"; } }
	 * 
	 * ClientMaster cm = null; AdvisorUser advUser =
	 * advisorUserRepository.findOne(investDTOList.get(0).getAdvisorUser()); for
	 * (InvestorMasterSearchDTO obj : investDTOList) { if (obj.isFamilyHead()) { cm
	 * = clientMasterRepository.findByAdvisorIdAndPan(advUser.getId(),
	 * obj.getInvestorPan()); if (cm == null) { cm = new ClientMaster();
	 * cm.setAdvisorUser(advUser); SimpleDateFormat sdf = new
	 * SimpleDateFormat("dd/MM/yyyy"); Date dt = null; try { dt =
	 * sdf.parse(obj.getInvestorDOB()); } catch (ParseException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } cm.setBirthDate(dt); if
	 * (obj.getInvestorGender().equals("M")) { cm.setGender("M");
	 * cm.setSalutation("Mr"); } else { cm.setGender("F"); cm.setSalutation("Ms"); }
	 * String name[] = obj.getInvestorName().split(" "); if (name.length == 1) {
	 * cm.setFirstName(obj.getInvestorName()); } else if (name.length == 2) {
	 * cm.setFirstName(name[0]); cm.setLastName(name[1]); } else if (name.length ==
	 * 3) { cm.setFirstName(name[0]); cm.setMiddleName(name[1]);
	 * cm.setLastName(name[2]); } else { cm.setFirstName(name[0]);
	 * cm.setMiddleName(name[1]); cm.setLastName(name[2] + name[3]); }
	 * LookupMaritalStatus lm = maritalStatusRepository.findOne((byte) 1);
	 * cm.setLookupMaritalStatus(lm); cm.setRetiredFlag("N"); cm.setActiveFlag("Y");
	 * cm.setPan(obj.getInvestorPan()); cm.setCreatedOn(new Date());
	 * 
	 * String generatedString = RandomStringUtils.randomAlphabetic(6);
	 * cm.setLoginPassword(generatedString); cm.setLastLoginTime(new Date());
	 * cm.setClient("Y"); cm.setLoggedInFlag("N"); LookupResidentType lr =
	 * residentTypeRepository.findOne((byte) 1); cm.setLookupResidentType(lr); cm =
	 * clientMasterRepository.save(cm);
	 * 
	 * // Insert record in Client Access rights
	 * 
	 * ClientAccessRight clientAccessRight = new ClientAccessRight();
	 * clientAccessRight.setClientMaster(cm);
	 * clientAccessRight.setClientInfoView("Y");
	 * clientAccessRight.setClientInfoAddEdit("N");
	 * clientAccessRight.setClientInfoDelete("N");
	 * clientAccessRight.setBudgetManagementView("N");
	 * clientAccessRight.setPortfolioManagementView("N");
	 * clientAccessRight.setPortfolioManagementAddEdit("N");
	 * clientAccessRight.setGoalPlanningView("N");
	 * clientAccessRight.setGoalPlanningAddEdit("N");
	 * clientAccessRight.setFinancialPlanningView("N");
	 * clientAccessRight.setFinancialPlanningAddEdit("N");
	 * clientAccessRight.setInvestView("N");
	 * clientAccessRight.setInvestAddEdit("N");
	 * clientAccessRight.setMfBackOfficeView("N");
	 * clientAccessRight.setCreatedOn(new Date());
	 * 
	 * clientAccessRight = clientAccessRightsRepository.save(clientAccessRight);
	 * 
	 * long currentTime = System.currentTimeMillis(); // save contact details
	 * ClientContact contact = new ClientContact(); contact.setClientMaster(cm);
	 * contact.setPermanentAddressLine1(address1);
	 * contact.setPermanentAddressLine2(obj.getInvestorAdressLine2());
	 * contact.setPermanentAddressLine3(obj.getInvestorAddressLine3());
	 * contact.setPermanentCity(obj.getInvestorCity());
	 * contact.setPermanentState(obj.getInvestorState()); LookupCountry country =
	 * lookupCountryRepository.findByName(obj.getInvestorPinCountry()); if (country
	 * != null) { contact.setLookupCountry2(country); } else { country =
	 * lookupCountryRepository.findOne(FinexaConstant.INDIA_LOOKUP_COUNTRY_ID);
	 * contact.setLookupCountry2(country); }
	 * 
	 * System.out.println("Pincode" + formatNumbers(pinCode));
	 * contact.setPermanentPincode((Integer.parseInt(formatNumbers(pinCode))));
	 * 
	 * contact.setEmailID(email); contact.setCreatedOn(new Timestamp(currentTime));
	 * 
	 * if (!mobile.isEmpty()) { if (mobile.length() ==
	 * Integer.parseInt(MobileNumberLength2)) { String mobileToBeStored =
	 * mobile.substring(1); contact.setMobile(new BigInteger(mobileToBeStored));
	 * contact.setCountryCode("+91"); contact.setEmergencyContact(new BigInteger(""
	 * + mobileToBeStored)); } else if (mobile.length() ==
	 * Integer.parseInt(MobileNumberLength3)) { String mobileToBeStoredWithCode =
	 * mobile.substring(3); contact.setMobile(new
	 * BigInteger(mobileToBeStoredWithCode)); contact.setCountryCode("+91");
	 * contact.setEmergencyContact(new BigInteger("" + mobileToBeStoredWithCode)); }
	 * else if (mobile.length() == Integer.parseInt(MobileNumberLength1)) {
	 * contact.setCountryCode("+91"); contact.setMobile(new BigInteger(mobile));
	 * contact.setEmergencyContact(new BigInteger(mobile)); } } else {
	 * contact.setMobile(BigInteger.valueOf(FinexaUtil.generateRandomMobileNumber())
	 * ); contact.setCountryCode("+91"); contact.setEmergencyContact(
	 * BigInteger.valueOf(FinexaUtil.generateRandomMobileNumber())); }
	 * 
	 * contact = clientContactRepository.save(contact); cm.setLoginUsername(email);
	 * 
	 * // Saving FamilyMember ClientFamilyMember clientFamilyMember = new
	 * ClientFamilyMember(); clientFamilyMember.setClientMaster(cm);
	 * clientFamilyMember.setFirstName(cm.getFirstName());
	 * clientFamilyMember.setMiddleName(cm.getMiddleName());
	 * clientFamilyMember.setLastName(cm.getLastName());
	 * clientFamilyMember.setBirthDate(cm.getBirthDate());
	 * clientFamilyMember.setLookupRelation(lookupRelationshipRepository.findOne((
	 * byte) 0)); clientFamilyMember.setPan(cm.getPan());
	 * clientFamilyMember.setDependentFlag("N");
	 * clientFamilyMember.setIsTobaccoUser("N");
	 * clientFamilyMember.setIsProperBMI("Y");
	 * clientFamilyMember.setHasDiseaseHistory("N");
	 * clientFamilyMember.setHasNormalBP("Y");
	 * 
	 * LifeExpectancyDTO lifeExpectancyDTO = new LifeExpectancyDTO();
	 * lifeExpectancyDTO.setAnnualIncome(0);
	 * lifeExpectancyDTO.setBirthDate(cm.getBirthDate());
	 * lifeExpectancyDTO.setIsTobaccoUser("N");
	 * lifeExpectancyDTO.setIsProperBMI("Y");
	 * lifeExpectancyDTO.setHasDiseaseHistory("N");
	 * lifeExpectancyDTO.setHasNormalBP("Y"); if (cm.getGender().equals("M")) {
	 * lifeExpectancyDTO.setGender("Male"); } else {
	 * lifeExpectancyDTO.setGender("Female"); } try { lifeExpectancyDTO =
	 * lifeExpectancyService.calculateLifeExp(lifeExpectancyDTO); if
	 * (lifeExpectancyDTO.getTotalLifeExpectancy() != null) {
	 * clientFamilyMember.setLifeExpectancy(lifeExpectancyDTO.getTotalLifeExpectancy
	 * ()); } } catch (Exception exp) { clientFamilyMember.setLifeExpectancy((byte)
	 * 80); } clientFamilyMember.setCreatedOn(new Timestamp(currentTime));
	 * clientFamilyMember.setRetiredFlag("N");
	 * clientFamilyMember.setRetirementAge((byte) 60);
	 * clientFamilyMember.setIsFamilyHead("Y"); clientFamilyMember =
	 * clientFamilyMemberRepository.save(clientFamilyMember);
	 * 
	 * // Insert Record in Income Table
	 * 
	 * ClientFamilyIncome clientFamilyIncome = new ClientFamilyIncome();
	 * clientFamilyIncome.setClientMaster(cm);
	 * clientFamilyIncome.setClientFamilyMember(clientFamilyMember);
	 * clientFamilyIncome.setIncomeType((byte)
	 * FinexaConstant.LOOKUP_INCOME_TOTAL_ID);
	 * clientFamilyIncome.setIncomeAmount(new BigDecimal("" + 0.0));
	 * clientFamilyIncome.setLookupFrequency(
	 * frequencyRepository.findOne(FinexaConstant.FREQUENCY_MONTHLY_ID));
	 * clientFamilyIncome.setLookupMonth(monthRepository.findOne((byte) 13));
	 * LookupIncomeExpenseDuration lookupIncomeExpenseDuration =
	 * incomeExpenseDurationRepository
	 * .findOne(FinexaConstant.INCOME_END_YEAR_LIFE_EXPECTANCY);
	 * clientFamilyIncome.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration
	 * );
	 * 
	 * clientFamilyIncome.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration
	 * ); clientFamilyIncome.setCreatedOn(new Timestamp(currentTime));
	 * clientFamilyIncome = clientFamilyIncomeRepository.save(clientFamilyIncome);
	 * // Client creation complete } }
	 * 
	 * }
	 * 
	 * if (cm != null) { List<AuxillaryInvestorMaster> aim =
	 * auxillaryMasterRepository.getByPanAndActiveFlag(cm.getPan(),
	 * cm.getActiveFlag()); if (aim.size() > 0 && aim != null) { if
	 * (!aim.get(0).getPan().equals(cm.getPan())) { for (InvestorMasterSearchDTO obj
	 * : investDTOList) { if (!obj.isFamilyHead()) { // store In Auxillary Table
	 * AuxillaryInvestorMaster auxillaryMaster = new AuxillaryInvestorMaster();
	 * auxillaryMaster.setClientMaster(cm); cm.setAdvisorUser(advUser);
	 * SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); Date dt = null;
	 * try { dt = sdf.parse(obj.getInvestorDOB()); } catch (ParseException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * auxillaryMaster.setBirthDate(dt); if (obj.getInvestorGender().equals("M")) {
	 * auxillaryMaster.setGender("M"); auxillaryMaster.setSalutation("Mr"); } else {
	 * auxillaryMaster.setGender("F"); auxillaryMaster.setSalutation("Ms"); } String
	 * name[] = obj.getInvestorName().split(" "); if (name.length == 1) {
	 * auxillaryMaster.setFirstName(obj.getInvestorName()); } else if (name.length
	 * == 2) { auxillaryMaster.setFirstName(name[0]);
	 * auxillaryMaster.setLastName(name[1]); } else if (name.length == 3) {
	 * auxillaryMaster.setFirstName(name[0]);
	 * auxillaryMaster.setMiddleName(name[1]); auxillaryMaster.setLastName(name[2]);
	 * } else { cm.setFirstName(name[0]); cm.setMiddleName(name[1]);
	 * cm.setLastName(name[2] + name[3]); } auxillaryMaster.setMaritalStatus((byte)
	 * 1); auxillaryMaster.setUserID(cm.getAdvisorUser().getId());
	 * auxillaryMaster.setRetiredFlag(cm.getRetiredFlag());
	 * auxillaryMaster.setActiveFlag(cm.getActiveFlag()); if (obj.getInvestorPan()
	 * != null) { auxillaryMaster.setPan(obj.getInvestorPan()); } else {
	 * auxillaryMaster.setPan(cm.getPan()); }
	 * auxillaryMaster.setCreatedOn(cm.getCreatedOn()); auxillaryMaster =
	 * auxillaryMasterRepository.save(auxillaryMaster); } } } } else { for
	 * (InvestorMasterSearchDTO obj : investDTOList) { if (!obj.isFamilyHead()) { //
	 * store In Auxillary Table AuxillaryInvestorMaster auxillaryMaster = new
	 * AuxillaryInvestorMaster(); auxillaryMaster.setClientMaster(cm);
	 * cm.setAdvisorUser(advUser); SimpleDateFormat sdf = new
	 * SimpleDateFormat("dd/MM/yyyy"); Date dt = null; try { dt =
	 * sdf.parse(obj.getInvestorDOB()); } catch (ParseException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * auxillaryMaster.setBirthDate(dt); if (obj.getInvestorGender().equals("M")) {
	 * auxillaryMaster.setGender("M"); auxillaryMaster.setSalutation("Mr"); } else {
	 * auxillaryMaster.setGender("F"); auxillaryMaster.setSalutation("Ms"); } String
	 * name[] = obj.getInvestorName().split(" "); if (name.length == 1) {
	 * auxillaryMaster.setFirstName(obj.getInvestorName()); } else if (name.length
	 * == 2) { auxillaryMaster.setFirstName(name[0]);
	 * auxillaryMaster.setLastName(name[1]); } else if (name.length == 3) {
	 * auxillaryMaster.setFirstName(name[0]);
	 * auxillaryMaster.setMiddleName(name[1]); auxillaryMaster.setLastName(name[2]);
	 * } else { cm.setFirstName(name[0]); cm.setMiddleName(name[1]);
	 * cm.setLastName(name[2] + name[3]); } auxillaryMaster.setMaritalStatus((byte)
	 * 1); auxillaryMaster.setUserID(cm.getAdvisorUser().getId());
	 * auxillaryMaster.setRetiredFlag(cm.getRetiredFlag());
	 * auxillaryMaster.setActiveFlag(cm.getActiveFlag()); if (obj.getInvestorPan()
	 * != null) { auxillaryMaster.setPan(obj.getInvestorPan()); } else {
	 * auxillaryMaster.setPan(cm.getPan()); }
	 * auxillaryMaster.setCreatedOn(cm.getCreatedOn()); auxillaryMaster =
	 * auxillaryMasterRepository.save(auxillaryMaster); } } } } } // TODO
	 * Auto-generated method stub return investDTOList; } catch (RuntimeException e)
	 * { // TODO Auto-generated catch block throw new RuntimeException(e); } }
	 */

	@Override
	public List<InvestorMasterSearchDTO> createClient(List<InvestorMasterSearchDTO> investDTOList)
			throws FinexaBussinessException {

		try {
			
			Map<Integer, String> idAndNameMap;
			if (investDTOList != null && investDTOList.size() > 0) {

				String mobile = "";
				String email = "";
				String address1 = "";
				String pinCode = "";
				String clientName = "";
				// This part of code is done as in some records email is present while in some
				// it is not. Ideally these are all the same person hence we are assuming any
				// one as the correct email and mobile

				for (InvestorMasterSearchDTO obj : investDTOList) {
					if (obj.getInvestorMobile() != null && !obj.getInvestorMobile().equals("")) {
						mobile = obj.getInvestorMobile();
					}
				}

				for (InvestorMasterSearchDTO obj : investDTOList) {
					if (obj.getInvestorEmail() != null && !obj.getInvestorEmail().equals("")) {
						email = obj.getInvestorEmail();
					}
				}
				for (InvestorMasterSearchDTO obj : investDTOList) {
					if (obj.getInvestorAddressLine1() != null && !obj.getInvestorAddressLine1().equals("")) {
						address1 = obj.getInvestorAddressLine1();
					}
				}

				for (InvestorMasterSearchDTO obj : investDTOList) {
					if ((obj.getInvestorPinCode()) != null && !obj.getInvestorPinCode().equals("")
							&& obj.getInvestorPinCode().matches("[0-9]+")) {
						pinCode = (obj.getInvestorPinCode());
					} else {
						pinCode = "0";
					}
				}

				ClientMaster cm = null;
				AdvisorUser advUser = advisorUserRepository.findOne(investDTOList.get(0).getAdvisorUser());
				for (InvestorMasterSearchDTO obj : investDTOList) {
					if (obj.isFamilyHead()) {
						cm = clientMasterRepository.findByAdvisorIdAndPan(advUser.getId(), obj.getInvestorPan());
						if (cm == null) {
							cm = new ClientMaster();
							cm.setAdvisorUser(advUser);
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							Date dt = null;
							try {
								dt = sdf.parse(obj.getInvestorDOB());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							cm.setBirthDate(dt);
							if (obj.getInvestorGender().equals("M")) {
								cm.setGender("M");
								cm.setSalutation("Mr");
							} else {
								cm.setGender("F");
								cm.setSalutation("Ms");
							}
							String name[] = obj.getInvestorName().split(" ");
							for(int i = 0; i < name.length; i++ ) {
								if(name[i] != null) {
									if(name[i] == "" || name[i].isEmpty()) {
										name = (String[]) ArrayUtils.remove(name, i);
									}
								}
							}
							/*
							 * if(obj.getInvestorPan().equals("ADWPK2411L")) {
							 * System.out.println(name.length+","+obj.getInvestorPan()+""+obj.
							 * getInvestorName()); }
							 */
							if (name.length == 1) {
								cm.setFirstName(obj.getInvestorName().trim());
							} else if (name.length == 2) {
								cm.setFirstName(name[0]);
								cm.setLastName(name[1]);
							} else if (name.length == 3) {
								cm.setFirstName(name[0]);
								cm.setMiddleName(name[1]);
								cm.setLastName(name[2]);
							} else {
								cm.setFirstName(name[0]);
								cm.setMiddleName(name[1]);
								String lastName = name[2].trim();
								for (int count = 3; count < name.length; count++) {

									lastName = lastName + " " + name[count].trim();
								}
									cm.setLastName(lastName);
								

							}
							LookupMaritalStatus lm = maritalStatusRepository.findOne((byte) 1);
							cm.setLookupMaritalStatus(lm);
							cm.setRetiredFlag("N");
							cm.setActiveFlag("Y");
							cm.setPan(obj.getInvestorPan());
							cm.setCreatedOn(new Date());

							String generatedString = RandomStringUtils.randomAlphabetic(6);
							cm.setLoginUsername(email);
							cm.setLoginPassword(generatedString);
							cm.setLastLoginTime(new Date());
							cm.setClient("Y");
							cm.setLoggedInFlag("N");
							LookupResidentType lr = residentTypeRepository.findOne((byte) 1);
							cm.setLookupResidentType(lr);
							cm.setFinexaUser("N");
							cm = clientMasterRepository.save(cm);
							ClientARNMapping clientARNMapping = new ClientARNMapping();
							clientARNMapping.setClientMaster(cm);
							clientARNMapping.setAdvisorUser(advUser);
							clientARNMapping.setArn(obj.getDistributorARNCode());
							clientARNMappingList.add(clientARNMapping);
							clientID = cm.getId();
							// Insert record in Client Access rights

							ClientAccessRight clientAccessRight = new ClientAccessRight();
							clientAccessRight.setClientMaster(cm);
							clientAccessRight.setClientInfoView("Y");
							clientAccessRight.setClientInfoAddEdit("N");
							clientAccessRight.setClientInfoDelete("N");
							clientAccessRight.setBudgetManagementView("N");
							clientAccessRight.setPortfolioManagementView("N");
							clientAccessRight.setPortfolioManagementAddEdit("N");
							clientAccessRight.setGoalPlanningView("N");
							clientAccessRight.setGoalPlanningAddEdit("N");
							clientAccessRight.setFinancialPlanningView("N");
							clientAccessRight.setFinancialPlanningAddEdit("N");
							clientAccessRight.setInvestView("N");
							clientAccessRight.setInvestAddEdit("N");
							clientAccessRight.setMfBackOfficeView("N");
							clientAccessRight.setCreatedOn(new Date());

							clientAccessRight = clientAccessRightsRepository.save(clientAccessRight);

							long currentTime = System.currentTimeMillis();
							// save contact details
							ClientContact contact = new ClientContact();
							contact.setClientMaster(cm);
							contact.setPermanentAddressLine1(address1);
							contact.setPermanentAddressLine2(obj.getInvestorAdressLine2());
							contact.setPermanentAddressLine3(obj.getInvestorAddressLine3());
							contact.setPermanentCity(obj.getInvestorCity());
							contact.setPermanentState(obj.getInvestorState());
							/*
							 * LookupCountry country =
							 * lookupCountryRepository.findByName(obj.getInvestorPinCountry()); if (country
							 * != null) { contact.setLookupCountry2(country); } else { country =
							 * lookupCountryRepository.findOne(FinexaConstant.INDIA_LOOKUP_COUNTRY_ID);
							 * contact.setLookupCountry2(country); }
							 */
							LookupCountry country = lookupCountryRepository
									.findOne(FinexaConstant.INDIA_LOOKUP_COUNTRY_ID);
							contact.setLookupCountry2(country);

							// System.out.println("LOOK UP COUNTRY:"+contact.getLookupCountry2());
							/*
							 * if(obj.getInvestorPan().equals("ATZPB5785B")) {
							 * System.out.println(obj.getInvestorPan()+","+obj.getInvestorMobile()); }
							 * System.out.println("Pincode" + formatNumbers(pinCode));
							 */
							contact.setPermanentPincode((Integer.parseInt(formatNumbers(pinCode))));

							contact.setEmailID(email);
							contact.setCreatedOn(new Timestamp(currentTime));

							if (!mobile.isEmpty()) {
								if (mobile.length() == Integer.parseInt(MobileNumberLength2)) {
									String mobileToBeStored = mobile.substring(1);
									contact.setMobile(new BigInteger(mobileToBeStored));
									contact.setCountryCode("+91");
									contact.setEmergencyContact(new BigInteger("" + mobileToBeStored));
								} else if (mobile.length() == Integer.parseInt(MobileNumberLength3)) {
									String mobileToBeStoredWithCode = mobile.substring(3);
									contact.setMobile(new BigInteger(mobileToBeStoredWithCode));
									contact.setCountryCode("+91");
									contact.setEmergencyContact(new BigInteger("" + mobileToBeStoredWithCode));
								} else if (mobile.length() == Integer.parseInt(MobileNumberLength1)) {
									contact.setCountryCode("+91");
									contact.setMobile(new BigInteger(mobile));
									contact.setEmergencyContact(new BigInteger(mobile));
								} else {
									contact.setMobile(BigInteger.valueOf(FinexaUtil.generateRandomMobileNumber()));
									contact.setCountryCode("+91");
									contact.setEmergencyContact(
											BigInteger.valueOf(FinexaUtil.generateRandomMobileNumber()));
								}

							} else {
								contact.setMobile(BigInteger.valueOf(FinexaUtil.generateRandomMobileNumber()));
								contact.setCountryCode("+91");
								contact.setEmergencyContact(
										BigInteger.valueOf(FinexaUtil.generateRandomMobileNumber()));
							}

							contact = clientContactRepository.save(contact);
							cm.setLoginUsername(email);

							// Saving FamilyMember
							ClientFamilyMember clientFamilyMember = new ClientFamilyMember();
							clientFamilyMember.setClientMaster(cm);
							clientFamilyMember.setFirstName(cm.getFirstName());
							clientFamilyMember.setMiddleName(cm.getMiddleName());
							clientFamilyMember.setLastName(cm.getLastName());
							clientFamilyMember.setBirthDate(cm.getBirthDate());
							clientFamilyMember.setLookupRelation(lookupRelationshipRepository.findOne((byte) 0));
							clientFamilyMember.setPan(cm.getPan());
							clientFamilyMember.setDependentFlag("N");
							clientFamilyMember.setIsTobaccoUser("N");
							clientFamilyMember.setIsProperBMI("Y");
							clientFamilyMember.setHasDiseaseHistory("N");
							clientFamilyMember.setHasNormalBP("Y");

							LifeExpectancyDTO lifeExpectancyDTO = new LifeExpectancyDTO();
							lifeExpectancyDTO.setAnnualIncome(0);
							lifeExpectancyDTO.setBirthDate(cm.getBirthDate());
							lifeExpectancyDTO.setIsTobaccoUser("N");
							lifeExpectancyDTO.setIsProperBMI("Y");
							lifeExpectancyDTO.setHasDiseaseHistory("N");
							lifeExpectancyDTO.setHasNormalBP("Y");
							if (cm.getGender().equals("M")) {
								lifeExpectancyDTO.setGender("Male");
							} else {
								lifeExpectancyDTO.setGender("Female");
							}
							try {
								lifeExpectancyDTO = lifeExpectancyService.calculateLifeExp(lifeExpectancyDTO);
								if (lifeExpectancyDTO.getTotalLifeExpectancy() != null) {
									clientFamilyMember.setLifeExpectancy(lifeExpectancyDTO.getTotalLifeExpectancy());
								}
							} catch (Exception exp) {
								clientFamilyMember.setLifeExpectancy((byte) 80);
							}
							clientFamilyMember.setCreatedOn(new Timestamp(currentTime));
							clientFamilyMember.setRetiredFlag("N");
							clientFamilyMember.setRetirementAge((byte) 60);
							clientFamilyMember.setIsFamilyHead("Y");
							clientFamilyMember = clientFamilyMemberRepository.save(clientFamilyMember);

							// Insert Record in Income Table

							ClientFamilyIncome clientFamilyIncome = new ClientFamilyIncome();
							clientFamilyIncome.setClientMaster(cm);
							clientFamilyIncome.setClientFamilyMember(clientFamilyMember);
							clientFamilyIncome.setIncomeType((byte) FinexaConstant.LOOKUP_INCOME_TOTAL_ID);
							clientFamilyIncome.setIncomeAmount(new BigDecimal("" + 0.0));
							clientFamilyIncome.setLookupFrequency(
									frequencyRepository.findOne(FinexaConstant.FREQUENCY_MONTHLY_ID));
							clientFamilyIncome.setLookupMonth(monthRepository.findOne((byte) 13));
							LookupIncomeExpenseDuration lookupIncomeExpenseDuration = incomeExpenseDurationRepository
									.findOne(FinexaConstant.INCOME_END_YEAR_LIFE_EXPECTANCY);
							clientFamilyIncome.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);

							clientFamilyIncome.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);
							clientFamilyIncome.setCreatedOn(new Timestamp(currentTime));
							clientFamilyIncome = clientFamilyIncomeRepository.save(clientFamilyIncome);
							// Client creation complete
						} else if (cm != null) {
							if (cm.getFirstName() != null)
								clientName = cm.getFirstName();
							if (cm.getMiddleName() != null)
								clientName = clientName + " " + cm.getMiddleName();
							if (cm.getLastName() != null)
								clientName = clientName + " " + cm.getLastName();
							clientID = cm.getId();
							if (obj.getInvestorName().trim().equals(clientName)) {
								/************************************************************************/

								cm = new ClientMaster();
								cm.setId(clientID);
								cm.setAdvisorUser(advUser);
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date dt = null;
								try {
									dt = sdf.parse(obj.getInvestorDOB());
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cm.setBirthDate(dt);
								if (obj.getInvestorGender().equals("M")) {
									cm.setGender("M");
									cm.setSalutation("Mr");
								} else {
									cm.setGender("F");
									cm.setSalutation("Ms");
								}
								String name[] = obj.getInvestorName().split(" ");
								for(int i = 0; i < name.length; i++ ) {
									if(name[i] != null) {
										if(name[i] == "" || name[i].isEmpty()) {
											name = (String[]) ArrayUtils.remove(name, i);
										}
									}
								}
								if (name.length == 1) {
									cm.setFirstName(obj.getInvestorName());
								} else if (name.length == 2) {
									cm.setFirstName(name[0]);
									cm.setLastName(name[1]);
								} else if (name.length == 3) {
									cm.setFirstName(name[0]);
									cm.setMiddleName(name[1]);
									cm.setLastName(name[2]);
								} else {
									cm.setFirstName(name[0]);
									cm.setMiddleName(name[1]);
									String lastName = name[2].trim();
									for (int count = 3; count < name.length; count++) {

										lastName = lastName + " " + name[count].trim();
									}
										cm.setLastName(lastName);
									

								}
								LookupMaritalStatus lm = maritalStatusRepository.findOne((byte) 1);
								cm.setLookupMaritalStatus(lm);
								cm.setRetiredFlag("N");
								cm.setActiveFlag("Y");
								cm.setPan(obj.getInvestorPan());
								cm.setCreatedOn(new Date());

								String generatedString = RandomStringUtils.randomAlphabetic(6);
								cm.setLoginPassword(generatedString);
								cm.setLastLoginTime(new Date());
								cm.setClient("Y");
								cm.setLoggedInFlag("N");
								LookupResidentType lr = residentTypeRepository.findOne((byte) 1);
								cm.setLookupResidentType(lr);
								cm.setFinexaUser("N");
								cm = clientMasterRepository.save(cm);
								ClientARNMapping clientARNMapping = new ClientARNMapping();
								clientARNMapping.setClientMaster(cm);
								clientARNMapping.setAdvisorUser(advUser);
								clientARNMapping.setArn(obj.getDistributorARNCode());
								clientARNMappingList.add(clientARNMapping);
								clientID = cm.getId();
								// Insert record in Client Access rights

								ClientAccessRight clientAccessRight = new ClientAccessRight();
								ClientAccessRight clientAccess = new ClientAccessRight();
								clientAccessRight.setClientMaster(cm);
								clientAccessRight.setClientInfoView("Y");
								clientAccessRight.setClientInfoAddEdit("N");
								clientAccessRight.setClientInfoDelete("N");
								clientAccessRight.setBudgetManagementView("N");
								clientAccessRight.setPortfolioManagementView("N");
								clientAccessRight.setPortfolioManagementAddEdit("N");
								clientAccessRight.setGoalPlanningView("N");
								clientAccessRight.setGoalPlanningAddEdit("N");
								clientAccessRight.setFinancialPlanningView("N");
								clientAccessRight.setFinancialPlanningAddEdit("N");
								clientAccessRight.setInvestView("N");
								clientAccessRight.setInvestAddEdit("N");
								clientAccessRight.setMfBackOfficeView("N");
								clientAccessRight.setCreatedOn(new Date());

								clientAccess = clientAccessRightsRepository.findByClientMaster(cm);
								if (clientAccess != null)
									clientAccessRight.setId(clientAccess.getId());
								clientAccessRight = clientAccessRightsRepository.save(clientAccessRight);

								long currentTime = System.currentTimeMillis();
								// save contact details
								ClientContact contact = new ClientContact();
								ClientContact clientContact = new ClientContact();
								clientContact = clientContactRepository.findByClientMaster(cm);
								if (clientContact != null)
									contact.setId(clientContact.getId());
								contact.setClientMaster(cm);
								contact.setPermanentAddressLine1(address1);
								contact.setPermanentAddressLine2(obj.getInvestorAdressLine2());
								contact.setPermanentAddressLine3(obj.getInvestorAddressLine3());
								contact.setPermanentCity(obj.getInvestorCity());
								contact.setPermanentState(obj.getInvestorState());
								/*
								 * LookupCountry country =
								 * lookupCountryRepository.findByName(obj.getInvestorPinCountry()); if (country
								 * != null) { contact.setLookupCountry2(country); } else { country =
								 * lookupCountryRepository.findOne(FinexaConstant.INDIA_LOOKUP_COUNTRY_ID);
								 * contact.setLookupCountry2(country); }
								 */
								LookupCountry country = lookupCountryRepository
										.findOne(FinexaConstant.INDIA_LOOKUP_COUNTRY_ID);
								contact.setLookupCountry2(country);

								// System.out.println("Pincode" + formatNumbers(pinCode));
								contact.setPermanentPincode((Integer.parseInt(formatNumbers(pinCode))));

								contact.setEmailID(email);
								contact.setCreatedOn(new Timestamp(currentTime));

								if (!mobile.isEmpty()) {
									if (mobile.length() == Integer.parseInt(MobileNumberLength2)) {
										String mobileToBeStored = mobile.substring(1);
										contact.setMobile(new BigInteger(mobileToBeStored));
										contact.setCountryCode("+91");
										contact.setEmergencyContact(new BigInteger("" + mobileToBeStored));
									} else if (mobile.length() == Integer.parseInt(MobileNumberLength3)) {
										String mobileToBeStoredWithCode = mobile.substring(3);
										contact.setMobile(new BigInteger(mobileToBeStoredWithCode));
										contact.setCountryCode("+91");
										contact.setEmergencyContact(new BigInteger("" + mobileToBeStoredWithCode));
									} else if (mobile.length() == Integer.parseInt(MobileNumberLength1)) {
										contact.setCountryCode("+91");
										contact.setMobile(new BigInteger(mobile));
										contact.setEmergencyContact(new BigInteger(mobile));
									} else {
										contact.setMobile(BigInteger.valueOf(FinexaUtil.generateRandomMobileNumber()));
										contact.setCountryCode("+91");
										contact.setEmergencyContact(
												BigInteger.valueOf(FinexaUtil.generateRandomMobileNumber()));
									}
								} else {
									contact.setMobile(BigInteger.valueOf(FinexaUtil.generateRandomMobileNumber()));
									contact.setCountryCode("+91");
									contact.setEmergencyContact(
											BigInteger.valueOf(FinexaUtil.generateRandomMobileNumber()));
								}

								contact = clientContactRepository.save(contact);
								cm.setLoginUsername(email);

								// Saving FamilyMember
								ClientFamilyMember clientFamilyMember = new ClientFamilyMember();
								ClientFamilyMember clientFamily = new ClientFamilyMember();
								int selfRelation = 0;
								LookupRelation relationSelf = lookupRelationshipRepository.findOne((byte) selfRelation);
								clientFamily = clientFamilyMemberRepository.findByClientMasterAndLookupRelation(cm,
										relationSelf);
								if (clientFamily != null)
									clientFamilyMember.setId(clientFamily.getId());
								clientFamilyMember.setClientMaster(cm);
								clientFamilyMember.setFirstName(cm.getFirstName());
								clientFamilyMember.setMiddleName(cm.getMiddleName());
								clientFamilyMember.setLastName(cm.getLastName());
								clientFamilyMember.setBirthDate(cm.getBirthDate());
								clientFamilyMember.setLookupRelation(lookupRelationshipRepository.findOne((byte) 0));
								clientFamilyMember.setPan(cm.getPan());
								clientFamilyMember.setDependentFlag("N");
								clientFamilyMember.setIsTobaccoUser("N");
								clientFamilyMember.setIsProperBMI("Y");
								clientFamilyMember.setHasDiseaseHistory("N");
								clientFamilyMember.setHasNormalBP("Y");

								LifeExpectancyDTO lifeExpectancyDTO = new LifeExpectancyDTO();
								lifeExpectancyDTO.setAnnualIncome(0);
								lifeExpectancyDTO.setBirthDate(cm.getBirthDate());
								lifeExpectancyDTO.setIsTobaccoUser("N");
								lifeExpectancyDTO.setIsProperBMI("Y");
								lifeExpectancyDTO.setHasDiseaseHistory("N");
								lifeExpectancyDTO.setHasNormalBP("Y");
								if (cm.getGender().equals("M")) {
									lifeExpectancyDTO.setGender("Male");
								} else {
									lifeExpectancyDTO.setGender("Female");
								}
								try {
									lifeExpectancyDTO = lifeExpectancyService.calculateLifeExp(lifeExpectancyDTO);
									if (lifeExpectancyDTO.getTotalLifeExpectancy() != null) {
										clientFamilyMember
												.setLifeExpectancy(lifeExpectancyDTO.getTotalLifeExpectancy());
									}
								} catch (Exception exp) {
									clientFamilyMember.setLifeExpectancy((byte) 80);
								}
								clientFamilyMember.setCreatedOn(new Timestamp(currentTime));
								clientFamilyMember.setRetiredFlag("N");
								clientFamilyMember.setRetirementAge((byte) 60);
								clientFamilyMember.setIsFamilyHead("Y");
								clientFamilyMember = clientFamilyMemberRepository.save(clientFamilyMember);

								// Insert Record in Income Table

								ClientFamilyIncome clientFamilyIncome = new ClientFamilyIncome();
								/*
								 * ClientFamilyIncome cFamilyIncome = new ClientFamilyIncome(); cFamilyIncome =
								 * clientFamilyIncomeRepository .findByClientFamilyMember(clientFamilyMember);
								 * if (cFamilyIncome != null) clientFamilyIncome.setId(cFamilyIncome.getId());
								 */
								clientFamilyIncome.setClientMaster(cm);
								clientFamilyIncome.setClientFamilyMember(clientFamilyMember);
								clientFamilyIncome.setIncomeType((byte) FinexaConstant.LOOKUP_INCOME_TOTAL_ID);
								clientFamilyIncome.setIncomeAmount(new BigDecimal("" + 0.0));
								clientFamilyIncome.setLookupFrequency(
										frequencyRepository.findOne(FinexaConstant.FREQUENCY_MONTHLY_ID));
								clientFamilyIncome.setLookupMonth(monthRepository.findOne((byte) 13));
								LookupIncomeExpenseDuration lookupIncomeExpenseDuration = incomeExpenseDurationRepository
										.findOne(FinexaConstant.INCOME_END_YEAR_LIFE_EXPECTANCY);
								clientFamilyIncome.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);

								clientFamilyIncome.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);
								clientFamilyIncome.setCreatedOn(new Timestamp(currentTime));
								clientFamilyIncome = clientFamilyIncomeRepository.save(clientFamilyIncome);
								// Client creation complete

								/************************************************************************/
							}
						}
					}

				}
				if (cm != null && investDTOList.size() > 1) {
					List<AuxillaryInvestorMaster> aim = auxillaryMasterRepository
							.getByPanAndActiveFlagAndClientMaster(cm.getPan(), cm.getActiveFlag(), cm);
					for (InvestorMasterSearchDTO obj : investDTOList) {
						if (!obj.isFamilyHead()) {
							boolean isAuxiliaryFound = false;
							AuxillaryInvestorMaster auxillaryInvestorMaster = new AuxillaryInvestorMaster();
							for (AuxillaryInvestorMaster auxInvestorMaster : aim) {

								String nameOfAuxiliaryInvestor[] = null;
								if (obj.getInvestorName() != null && obj.getInvestorName().trim() != ""
										&& !obj.getInvestorName().trim().isEmpty()) {
									nameOfAuxiliaryInvestor = obj.getInvestorName().trim().split(" ");
								}
								if (nameOfAuxiliaryInvestor.length == 1 && auxInvestorMaster.getMiddleName() == null
										&& auxInvestorMaster.getLastName() == null) {
									if (auxInvestorMaster.getFirstName().trim()
											.equalsIgnoreCase(nameOfAuxiliaryInvestor[0])) {
										isAuxiliaryFound = true;
										auxillaryInvestorMaster = auxInvestorMaster;
										break;
									}
								} else if (nameOfAuxiliaryInvestor.length == 2
										&& auxInvestorMaster.getMiddleName() == null) {
									if (auxInvestorMaster.getFirstName().trim()
											.equalsIgnoreCase(nameOfAuxiliaryInvestor[0])
											&& (auxInvestorMaster.getLastName() != null
													&& auxInvestorMaster.getLastName().trim() != ""
													&& !auxInvestorMaster.getLastName().trim().isEmpty()
													&& auxInvestorMaster.getLastName().trim()
															.equalsIgnoreCase(nameOfAuxiliaryInvestor[1]))) {
										isAuxiliaryFound = true;
										auxillaryInvestorMaster = auxInvestorMaster;
										break;
									}
								} else if (nameOfAuxiliaryInvestor.length == 3) {
									if (auxInvestorMaster.getFirstName().trim()
											.equalsIgnoreCase(nameOfAuxiliaryInvestor[0])
											&& (auxInvestorMaster.getMiddleName() != null
													&& auxInvestorMaster.getMiddleName().trim() != ""
													&& !auxInvestorMaster.getMiddleName().trim().isEmpty()
													&& auxInvestorMaster.getMiddleName().trim()
															.equalsIgnoreCase(nameOfAuxiliaryInvestor[1]))
											&& (auxInvestorMaster.getLastName() != null
													&& auxInvestorMaster.getLastName().trim() != ""
													&& !auxInvestorMaster.getLastName().trim().isEmpty()
													&& auxInvestorMaster.getLastName().trim()
															.equalsIgnoreCase(nameOfAuxiliaryInvestor[2]))) {
										isAuxiliaryFound = true;
										auxillaryInvestorMaster = auxInvestorMaster;
										break;
									}

								} else if(nameOfAuxiliaryInvestor.length > 3) {									
									String lastName = nameOfAuxiliaryInvestor[2].trim();
									for (int counter = 3; counter < nameOfAuxiliaryInvestor.length; counter++) {
										lastName = lastName + " " + nameOfAuxiliaryInvestor[counter].trim();
									}
									if (auxInvestorMaster.getFirstName().trim()
											.equalsIgnoreCase(nameOfAuxiliaryInvestor[0])
											&& (auxInvestorMaster.getMiddleName() != null
													&& auxInvestorMaster.getMiddleName().trim() != ""
													&& !auxInvestorMaster.getMiddleName().trim().isEmpty()
													&& auxInvestorMaster.getMiddleName().trim()
															.equalsIgnoreCase(nameOfAuxiliaryInvestor[1]))
											&& auxInvestorMaster.getLastName().trim().equalsIgnoreCase(lastName)) {
										isAuxiliaryFound = true;
										auxillaryInvestorMaster = auxInvestorMaster;
										break;
									}
								}

							}
							if (isAuxiliaryFound == true) {

								// store In Auxillary Table
								// AuxillaryInvestorMaster auxillaryMaster = new AuxillaryInvestorMaster();
								auxillaryInvestorMaster.setClientMaster(cm);
								cm.setAdvisorUser(advUser);
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date dt = null;
								try {
									dt = sdf.parse(obj.getInvestorDOB());
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								auxillaryInvestorMaster.setBirthDate(dt);
								if (obj.getInvestorGender().equals("M")) {
									auxillaryInvestorMaster.setGender("M");
									auxillaryInvestorMaster.setSalutation("Mr");
								} else {
									auxillaryInvestorMaster.setGender("F");
									auxillaryInvestorMaster.setSalutation("Ms");
								}
								String name[] = obj.getInvestorName().split(" ");
								for(int i = 0; i < name.length; i++ ) {
									if(name[i] != null) {
										if(name[i] == "" || name[i].isEmpty()) {
											name = (String[]) ArrayUtils.remove(name, i);
										}
									}
								}
								if (name.length == 1) {
									auxillaryInvestorMaster.setFirstName(obj.getInvestorName());
								} else if (name.length == 2) {
									auxillaryInvestorMaster.setFirstName(name[0]);
									auxillaryInvestorMaster.setLastName(name[1]);
								} else if (name.length == 3) {
									auxillaryInvestorMaster.setFirstName(name[0]);
									auxillaryInvestorMaster.setMiddleName(name[1]);
									auxillaryInvestorMaster.setLastName(name[2]);
								} else {
									auxillaryInvestorMaster.setFirstName(name[0]);
									auxillaryInvestorMaster.setMiddleName(name[1]);
									String lastName = name[2].trim();
									for (int count = 3; count < name.length; count++) {

										lastName = lastName + " " + name[count].trim();
									}
										auxillaryInvestorMaster.setLastName(lastName);
									

								}
								auxillaryInvestorMaster.setMaritalStatus((byte) 1);
								auxillaryInvestorMaster.setUserID(cm.getAdvisorUser().getId());
								auxillaryInvestorMaster.setRetiredFlag(cm.getRetiredFlag());
								auxillaryInvestorMaster.setActiveFlag(cm.getActiveFlag());
								if (obj.getInvestorPan() != null) {
									auxillaryInvestorMaster.setPan(obj.getInvestorPan());
								} else {
									auxillaryInvestorMaster.setPan(cm.getPan());
								}
								auxillaryInvestorMaster.setCreatedOn(cm.getCreatedOn());
								auxillaryInvestorMaster.setId(auxillaryInvestorMaster.getId());
								auxillaryInvestorMaster = auxillaryMasterRepository.save(auxillaryInvestorMaster);

							} else {

								// store In Auxillary Table
								AuxillaryInvestorMaster auxillaryMaster = new AuxillaryInvestorMaster();
								auxillaryMaster.setClientMaster(cm);
								cm.setAdvisorUser(advUser);
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date dt = null;
								try {
									dt = sdf.parse(obj.getInvestorDOB());
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								auxillaryMaster.setBirthDate(dt);
								if (obj.getInvestorGender().equals("M")) {
									auxillaryMaster.setGender("M");
									auxillaryMaster.setSalutation("Mr");
								} else {
									auxillaryMaster.setGender("F");
									auxillaryMaster.setSalutation("Ms");
								}
								String name[] = obj.getInvestorName().split(" ");
								for(int i = 0; i < name.length; i++ ) {
									if(name[i] != null) {
										if(name[i] == "" || name[i].isEmpty()) {
											name = (String[]) ArrayUtils.remove(name, i);
										}
									}
								}
								if (name.length == 1) {
									auxillaryMaster.setFirstName(obj.getInvestorName());
								} else if (name.length == 2) {
									auxillaryMaster.setFirstName(name[0]);
									auxillaryMaster.setLastName(name[1]);
								} else if (name.length == 3) {
									auxillaryMaster.setFirstName(name[0]);
									auxillaryMaster.setMiddleName(name[1]);
									auxillaryMaster.setLastName(name[2]);
								} else {
									auxillaryMaster.setFirstName(name[0]);
									auxillaryMaster.setMiddleName(name[1]);
									String lastName = name[2].trim();
									for (int count = 3; count < name.length; count++) {

										lastName = lastName + " " + name[count].trim();
									}
										auxillaryInvestorMaster.setLastName(lastName);
									
								}
								auxillaryMaster.setMaritalStatus((byte) 1);
								auxillaryMaster.setUserID(cm.getAdvisorUser().getId());
								auxillaryMaster.setRetiredFlag(cm.getRetiredFlag());
								auxillaryMaster.setActiveFlag(cm.getActiveFlag());
								if (obj.getInvestorPan() != null) {
									auxillaryMaster.setPan(obj.getInvestorPan());
								} else {
									auxillaryMaster.setPan(cm.getPan());
								}
								auxillaryMaster.setCreatedOn(cm.getCreatedOn());
								auxillaryMaster = auxillaryMasterRepository.save(auxillaryMaster);

							}
						}
					}
				}
				/*
				 * if (cm != null) { List<AuxillaryInvestorMaster> aim =
				 * auxillaryMasterRepository .getByPanAndActiveFlagAndUserID(cm.getPan(),
				 * cm.getActiveFlag(), advUser.getId()); idAndNameMap = new HashMap<>();
				 * 
				 * if (aim.size() > 0 && aim != null) { for (AuxillaryInvestorMaster
				 * auxInvestorMaster : aim) { String auxilliaryInvestorName = ""; if
				 * (auxInvestorMaster.getFirstName() != null) auxilliaryInvestorName =
				 * auxInvestorMaster.getFirstName(); if (auxInvestorMaster.getMiddleName() !=
				 * null) auxilliaryInvestorName = auxilliaryInvestorName + " " +
				 * auxInvestorMaster.getMiddleName(); if (auxInvestorMaster.getLastName() !=
				 * null) auxilliaryInvestorName = auxilliaryInvestorName + " " +
				 * auxInvestorMaster.getLastName(); idAndNameMap.put(auxInvestorMaster.getId(),
				 * auxilliaryInvestorName); } for (InvestorMasterSearchDTO obj : investDTOList)
				 * { if (!obj.isFamilyHead()) {
				 * 
				 * int id = 0;
				 * 
				 * for (Map.Entry<Integer, String> entry : idAndNameMap.entrySet()) { if
				 * (entry.getValue().equals(obj.getInvestorName().trim())) { id =
				 * entry.getKey(); break; } } if (id != 0) { // store In Auxillary Table
				 * AuxillaryInvestorMaster auxillaryMaster = new AuxillaryInvestorMaster();
				 * auxillaryMaster.setClientMaster(cm); cm.setAdvisorUser(advUser);
				 * SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); Date dt = null;
				 * try { dt = sdf.parse(obj.getInvestorDOB()); } catch (ParseException e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); }
				 * auxillaryMaster.setBirthDate(dt); if (obj.getInvestorGender().equals("M")) {
				 * auxillaryMaster.setGender("M"); auxillaryMaster.setSalutation("Mr"); } else {
				 * auxillaryMaster.setGender("F"); auxillaryMaster.setSalutation("Ms"); } String
				 * name[] = obj.getInvestorName().split(" "); if (name.length == 1) {
				 * auxillaryMaster.setFirstName(obj.getInvestorName()); } else if (name.length
				 * == 2) { auxillaryMaster.setFirstName(name[0]);
				 * auxillaryMaster.setLastName(name[1]); } else if (name.length == 3) {
				 * auxillaryMaster.setFirstName(name[0]);
				 * auxillaryMaster.setMiddleName(name[1]); auxillaryMaster.setLastName(name[2]);
				 * } else { auxillaryMaster.setFirstName(name[0]);
				 * auxillaryMaster.setMiddleName(name[1]); auxillaryMaster.setLastName(name[2] +
				 * name[3]); } auxillaryMaster.setMaritalStatus((byte) 1);
				 * auxillaryMaster.setUserID(cm.getAdvisorUser().getId());
				 * auxillaryMaster.setRetiredFlag(cm.getRetiredFlag());
				 * auxillaryMaster.setActiveFlag(cm.getActiveFlag()); if (obj.getInvestorPan()
				 * != null) { auxillaryMaster.setPan(obj.getInvestorPan()); } else {
				 * auxillaryMaster.setPan(cm.getPan()); }
				 * auxillaryMaster.setCreatedOn(cm.getCreatedOn()); auxillaryMaster.setId(id);
				 * auxillaryMaster = auxillaryMasterRepository.save(auxillaryMaster); } else {
				 * // store In Auxillary Table AuxillaryInvestorMaster auxillaryMaster = new
				 * AuxillaryInvestorMaster(); auxillaryMaster.setClientMaster(cm);
				 * cm.setAdvisorUser(advUser); SimpleDateFormat sdf = new
				 * SimpleDateFormat("dd/MM/yyyy"); Date dt = null; try { dt =
				 * sdf.parse(obj.getInvestorDOB()); } catch (ParseException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 * auxillaryMaster.setBirthDate(dt); if (obj.getInvestorGender().equals("M")) {
				 * auxillaryMaster.setGender("M"); auxillaryMaster.setSalutation("Mr"); } else {
				 * auxillaryMaster.setGender("F"); auxillaryMaster.setSalutation("Ms"); } String
				 * name[] = obj.getInvestorName().split(" "); if (name.length == 1) {
				 * auxillaryMaster.setFirstName(obj.getInvestorName()); } else if (name.length
				 * == 2) { auxillaryMaster.setFirstName(name[0]);
				 * auxillaryMaster.setLastName(name[1]); } else if (name.length == 3) {
				 * auxillaryMaster.setFirstName(name[0]);
				 * auxillaryMaster.setMiddleName(name[1]); auxillaryMaster.setLastName(name[2]);
				 * } else { auxillaryMaster.setFirstName(name[0]);
				 * auxillaryMaster.setMiddleName(name[1]); auxillaryMaster.setLastName(name[2] +
				 * name[3]); } auxillaryMaster.setMaritalStatus((byte) 1);
				 * auxillaryMaster.setUserID(cm.getAdvisorUser().getId());
				 * auxillaryMaster.setRetiredFlag(cm.getRetiredFlag());
				 * auxillaryMaster.setActiveFlag(cm.getActiveFlag()); if (obj.getInvestorPan()
				 * != null) { auxillaryMaster.setPan(obj.getInvestorPan()); } else {
				 * auxillaryMaster.setPan(cm.getPan()); }
				 * auxillaryMaster.setCreatedOn(cm.getCreatedOn()); auxillaryMaster =
				 * auxillaryMasterRepository.save(auxillaryMaster); }
				 * 
				 * } } } else { for (InvestorMasterSearchDTO obj : investDTOList) { if
				 * (!obj.isFamilyHead()) { // store In Auxillary Table AuxillaryInvestorMaster
				 * auxillaryMaster = new AuxillaryInvestorMaster();
				 * auxillaryMaster.setClientMaster(cm); cm.setAdvisorUser(advUser);
				 * SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); Date dt = null;
				 * try { dt = sdf.parse(obj.getInvestorDOB()); } catch (ParseException e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); }
				 * auxillaryMaster.setBirthDate(dt); if (obj.getInvestorGender().equals("M")) {
				 * auxillaryMaster.setGender("M"); auxillaryMaster.setSalutation("Mr"); } else {
				 * auxillaryMaster.setGender("F"); auxillaryMaster.setSalutation("Ms"); } String
				 * name[] = obj.getInvestorName().split(" "); if (name.length == 1) {
				 * auxillaryMaster.setFirstName(obj.getInvestorName()); } else if (name.length
				 * == 2) { auxillaryMaster.setFirstName(name[0]);
				 * auxillaryMaster.setLastName(name[1]); } else if (name.length == 3) {
				 * auxillaryMaster.setFirstName(name[0]);
				 * auxillaryMaster.setMiddleName(name[1]); auxillaryMaster.setLastName(name[2]);
				 * } else { auxillaryMaster.setFirstName(name[0]);
				 * auxillaryMaster.setMiddleName(name[1]); auxillaryMaster.setLastName(name[2] +
				 * name[3]); } auxillaryMaster.setMaritalStatus((byte) 1);
				 * auxillaryMaster.setUserID(cm.getAdvisorUser().getId());
				 * auxillaryMaster.setRetiredFlag(cm.getRetiredFlag());
				 * auxillaryMaster.setActiveFlag(cm.getActiveFlag()); if (obj.getInvestorPan()
				 * != null) { auxillaryMaster.setPan(obj.getInvestorPan()); } else {
				 * auxillaryMaster.setPan(cm.getPan()); }
				 * auxillaryMaster.setCreatedOn(cm.getCreatedOn()); auxillaryMaster =
				 * auxillaryMasterRepository.save(auxillaryMaster); } } } }
				 */
			}
			// TODO Auto-generated method stub
			return investDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientInfoDTO> getFamilyDetailsByNamePan(String name, int advisorId) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		try {
			SearchClientDTO searchDTO = new SearchClientDTO();
			searchDTO.setSearchName(name);
			searchDTO.setSearchAadhar("");
			searchDTO.setSearchMobile("");
			searchDTO.setSearchPan("");
			searchDTO.setSearchEmail("");
			searchDTO.setAdvisorId("" + advisorId);
			List<ClientInfoDTO> infoList = clientService.searchClient(searchDTO);

			if (infoList == null || infoList.size() == 0) {
				searchDTO = new SearchClientDTO();
				searchDTO.setSearchPan(name);
				searchDTO.setSearchAadhar("");
				searchDTO.setSearchMobile("");
				searchDTO.setSearchName("");
				searchDTO.setSearchEmail("");
				searchDTO.setAdvisorId("" + advisorId);
				infoList = clientService.searchClient(searchDTO);

			}
			return infoList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	/*
	 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	 */
	@Override
	public List<InvestorMasterSearchDTO> createFamily(List<InvestorMasterSearchDTO> investDTOList)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub
		try {
			if (investDTOList != null && investDTOList.size() > 0) {
				ClientMaster cm = null;

				int selfRelation = 0;
				List<ClientFamilyMember> clientFamilyMemberList;
				for (InvestorMasterSearchDTO obj : investDTOList) {
					// Saving FamilyMember
					if (!obj.isFamilyHead()) {
						ClientFamilyMember clientFamilyMember = null;
						cm = clientMasterRepository.findOne(obj.getClientId());
						if (cm != null) {
							clientFamilyMemberList = clientFamilyMemberRepository
									.findByClientIdAndRelationId(cm.getId(), (byte) selfRelation);
							if (clientFamilyMemberList.size() > 0) { // Already clients present other than client
																		// himself

								// LookupRelation relationWithFamily =
								// lookupRelationshipRepository.findOne((byte) obj.getRelationId());

								/****************************/
								boolean isNameMatched = false;
								for (ClientFamilyMember cfm : clientFamilyMemberList) {
									String nameOfInvestorFamilyMember[] = null;
									if (obj.getInvestorName() != null && obj.getInvestorName().trim() != ""
											&& !obj.getInvestorName().trim().isEmpty()) {
										nameOfInvestorFamilyMember = obj.getInvestorName().trim().split(" ");
									}

									if (nameOfInvestorFamilyMember.length == 1 && cfm.getMiddleName() == null
											&& cfm.getLastName() == null) {
										if (cfm.getFirstName().trim().equalsIgnoreCase(nameOfInvestorFamilyMember[0])) {
											isNameMatched = true;
											clientFamilyMember = cfm;
											break;
										}
									} else if (nameOfInvestorFamilyMember.length == 2 && cfm.getMiddleName() == null) {
										if (cfm.getFirstName().trim().equalsIgnoreCase(nameOfInvestorFamilyMember[0])
												&& (cfm.getLastName() != null && cfm.getLastName().trim() != ""
														&& !cfm.getLastName().trim().isEmpty()
														&& cfm.getLastName().trim()
																.equalsIgnoreCase(nameOfInvestorFamilyMember[1]))) {
											isNameMatched = true;
											clientFamilyMember = cfm;
											break;
										}
									} else if (nameOfInvestorFamilyMember.length == 3) {
										if (cfm.getFirstName().trim().equalsIgnoreCase(nameOfInvestorFamilyMember[0])
												&& (cfm.getMiddleName() != null && cfm.getMiddleName().trim() != ""
														&& !cfm.getMiddleName().trim().isEmpty()
														&& cfm.getMiddleName().trim()
																.equalsIgnoreCase(nameOfInvestorFamilyMember[1]))
												&& (cfm.getLastName() != null && cfm.getLastName().trim() != ""
														&& !cfm.getLastName().trim().isEmpty()
														&& cfm.getLastName().trim()
																.equalsIgnoreCase(nameOfInvestorFamilyMember[2]))) {
											isNameMatched = true;
											clientFamilyMember = cfm;
											break;
										}

									} else if(nameOfInvestorFamilyMember.length > 3) {
										String lastName = nameOfInvestorFamilyMember[2].trim();
										for (int counter = 3; counter < nameOfInvestorFamilyMember.length; counter++) {
											lastName = lastName + " " + nameOfInvestorFamilyMember[counter];
										}
										if (cfm.getFirstName().trim().equalsIgnoreCase(nameOfInvestorFamilyMember[0])
												&& (cfm.getMiddleName() != null && cfm.getMiddleName().trim() != ""
														&& !cfm.getMiddleName().trim().isEmpty()
														&& cfm.getMiddleName().trim()
																.equalsIgnoreCase(nameOfInvestorFamilyMember[1]))
												&& (cfm.getLastName() != null && cfm.getLastName().trim() != ""
														&& !cfm.getLastName().trim().isEmpty()
														&& cfm.getLastName().trim().equalsIgnoreCase(lastName))) {
											isNameMatched = true;
											clientFamilyMember = cfm;
											break;
										}
									}
								}

								if (isNameMatched == true) { // Updation of existing record in client family member
									// clientFamilyMember = new ClientFamilyMember();
									clientFamilyMember.setClientMaster(cm);
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
									Date dt = null;
									try {
										dt = sdf.parse(obj.getInvestorDOB());
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									clientFamilyMember.setBirthDate(dt);

									String name[] = obj.getInvestorName().split(" ");
									for(int i = 0; i < name.length; i++ ) {
										if(name[i] != null) {
											if(name[i] == "" || name[i].isEmpty()) {
												name = (String[]) ArrayUtils.remove(name, i);
											}
										}
									}
									if (name.length == 1) {
										clientFamilyMember.setFirstName(obj.getInvestorName());
									} else if (name.length == 2) {
										clientFamilyMember.setFirstName(name[0]);
										clientFamilyMember.setLastName(name[1]);
									} else if (name.length == 3) {
										clientFamilyMember.setFirstName(name[0]);
										clientFamilyMember.setMiddleName(name[1]);
										clientFamilyMember.setLastName(name[2]);
									} else {
										clientFamilyMember.setFirstName(name[0]);
										clientFamilyMember.setMiddleName(name[1]);
										String lastName = name[2].trim();
										for (int count = 3; count < name.length; count++) {

											lastName = lastName + " " + name[count].trim();
										}
										clientFamilyMember.setLastName(lastName);

									}

									LookupRelation relation = lookupRelationshipRepository
											.findOne((byte) obj.getRelationId());
									clientFamilyMember.setLookupRelation(relation);
									clientFamilyMember.setPan(obj.getInvestorPan());
									clientFamilyMember.setDependentFlag("Y");
									clientFamilyMember.setIsTobaccoUser("N");
									clientFamilyMember.setIsProperBMI("Y");
									clientFamilyMember.setHasDiseaseHistory("N");
									clientFamilyMember.setHasNormalBP("Y");
									clientFamilyMember.setIsFamilyHead("N");
									LifeExpectancyDTO lifeExpectancyDTO = new LifeExpectancyDTO();
									lifeExpectancyDTO.setAnnualIncome(0);
									try {
										lifeExpectancyDTO.setBirthDate(sdf.parse(obj.getInvestorDOB()));
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									lifeExpectancyDTO.setIsTobaccoUser("N");
									lifeExpectancyDTO.setIsProperBMI("Y");
									lifeExpectancyDTO.setHasDiseaseHistory("N");
									lifeExpectancyDTO.setHasNormalBP("Y");
									String gender = "";
									switch (obj.getRelationId()) {
									case 1:
										if (cm.getGender().equals("M")) {
											gender = "F";
										} else {
											gender = "M";
										}
										break;
									case 2:
										gender = "M";
										break;
									case 3:
										gender = "F";
										break;
									case 4:
										gender = "M";
										break;
									case 5:
										gender = "F";
										break;
									case 6:
										gender = "M";
										break;
									case 7:
										gender = "F";
										break;

									default:
										break;
									}
									lifeExpectancyDTO.setGender(gender);

									try {
										lifeExpectancyDTO = lifeExpectancyService.calculateLifeExp(lifeExpectancyDTO);
										if (lifeExpectancyDTO.getTotalLifeExpectancy() != null) {
											clientFamilyMember
													.setLifeExpectancy(lifeExpectancyDTO.getTotalLifeExpectancy());
										}
									} catch (Exception exp) {
										clientFamilyMember.setLifeExpectancy((byte) 80);
									}
									long currentTime = System.currentTimeMillis();
									clientFamilyMember.setCreatedOn(new Timestamp(currentTime));
									clientFamilyMember.setRetiredFlag("N");
									clientFamilyMember.setRetirementAge((byte) 0);
									clientFamilyMember = clientFamilyMemberRepository.save(clientFamilyMember);
								} else {
									boolean isFoundInAuxilliary = false;
									AuxillaryFamilyMember auxFamilyMember = new AuxillaryFamilyMember();

									for (ClientFamilyMember cfm : clientFamilyMemberList) {
										auxFamilyMember = new AuxillaryFamilyMember();
										List<AuxillaryFamilyMember> auxFamilyMemberList = cfm
												.getAuxillaryFamilyMembers();
										if (auxFamilyMemberList != null && (auxFamilyMemberList.size() > 0)) {
											for (AuxillaryFamilyMember afm : auxFamilyMemberList) {
												String nameOfInvestorFamilyMember[] = null;
												if (obj.getInvestorName() != null && obj.getInvestorName().trim() != ""
														&& !obj.getInvestorName().trim().isEmpty()) {
													nameOfInvestorFamilyMember = obj.getInvestorName().trim()
															.split(" ");
												}
												if (nameOfInvestorFamilyMember.length == 1
														&& afm.getMiddleName() == null && afm.getLastName() == null) {
													if (afm.getFirstName().trim()
															.equalsIgnoreCase(nameOfInvestorFamilyMember[0])) {
														isFoundInAuxilliary = true;
														auxFamilyMember = afm;
														break;
													}
												} else if (nameOfInvestorFamilyMember.length == 2
														&& afm.getMiddleName() == null) {
													if (afm.getFirstName().trim()
															.equalsIgnoreCase(nameOfInvestorFamilyMember[0])
															&& (afm.getLastName() != null
																	&& afm.getLastName().trim() != ""
																	&& !afm.getLastName().isEmpty()
																	&& afm.getLastName().trim().equalsIgnoreCase(
																			nameOfInvestorFamilyMember[1]))) {
														isFoundInAuxilliary = true;
														auxFamilyMember = afm;
														break;
													}
												} else if (nameOfInvestorFamilyMember.length == 3) {
													if (afm.getFirstName().trim()
															.equalsIgnoreCase(nameOfInvestorFamilyMember[0])
															&& (afm.getMiddleName() != null
																	&& afm.getMiddleName().trim() != ""
																	&& !afm.getMiddleName().trim().isEmpty()
																	&& afm.getMiddleName().trim().equalsIgnoreCase(
																			nameOfInvestorFamilyMember[1]))
															&& (afm.getLastName() != null
																	&& afm.getLastName().trim() != ""
																	&& !afm.getLastName().trim().isEmpty()
																	&& afm.getLastName().trim().equalsIgnoreCase(
																			nameOfInvestorFamilyMember[2]))) {
														isFoundInAuxilliary = true;
														auxFamilyMember = afm;
														break;
													}

												} else if(nameOfInvestorFamilyMember.length > 3){
													String lastName = nameOfInvestorFamilyMember[2].trim();
													for (int counter = 3; counter < nameOfInvestorFamilyMember.length; counter++) {

														lastName = lastName + " "
																+ nameOfInvestorFamilyMember[counter].trim();

													}
													if (afm.getFirstName().trim()
															.equalsIgnoreCase(nameOfInvestorFamilyMember[0])
															&& (afm.getMiddleName() != null
																	&& afm.getMiddleName().trim() != ""
																	&& !afm.getMiddleName().trim().isEmpty()
																	&& afm.getMiddleName().trim().equalsIgnoreCase(
																			nameOfInvestorFamilyMember[1]))
															&& (afm.getLastName() != null
																	&& afm.getLastName().trim() != ""
																	&& !afm.getLastName().trim().isEmpty()
																	&& afm.getLastName().trim()
																			.equalsIgnoreCase(lastName))) {
														isFoundInAuxilliary = true;
														auxFamilyMember = afm;
														break;
													}
												}
											}
										}
										if (isFoundInAuxilliary == true) {
											break;
										}
									}

									if (isFoundInAuxilliary == true) { // Updation of existing data

										int self = 0;
										LookupRelation relation = lookupRelationshipRepository.findOne((byte) self);
										ClientFamilyMember clientFamily = clientFamilyMemberRepository
												.findByClientMasterAndLookupRelation(cm, relation);
										SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
										Date dt = null;
										try {
											dt = sdf.parse(obj.getInvestorDOB());
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										auxFamilyMember.setBirthDate(dt);

										String name[] = obj.getInvestorName().split(" ");
										for(int i = 0; i < name.length; i++ ) {
											if(name[i] != null) {
												if(name[i] == "" || name[i].isEmpty()) {
													name = (String[]) ArrayUtils.remove(name, i);
												}
											}
										}
										if (name.length == 1) {
											auxFamilyMember.setFirstName(obj.getInvestorName());
										} else if (name.length == 2) {
											auxFamilyMember.setFirstName(name[0]);
											auxFamilyMember.setLastName(name[1]);
										} else if (name.length == 3) {
											auxFamilyMember.setFirstName(name[0]);
											auxFamilyMember.setMiddleName(name[1]);
											auxFamilyMember.setLastName(name[2]);
										} else {
											auxFamilyMember.setFirstName(name[0]);
											auxFamilyMember.setMiddleName(name[1]);
											String lastName = name[2].trim();
											for (int count = 3; count < name.length; count++) {

												lastName = lastName + " " + name[count];
											}

											auxFamilyMember.setLastName(lastName);

										}
										auxFamilyMember.setRelationID(
												auxFamilyMember.getClientFamilyMember().getLookupRelation().getId());
										auxFamilyMember.setPan(obj.getInvestorPan());
										auxFamilyMember.setDependentFlag("Y");
										auxFamilyMember.setIsTobaccoUser("N");
										auxFamilyMember.setIsProperBMI("Y");
										auxFamilyMember.setHasDiseaseHistory("N");
										auxFamilyMember.setHasNormalBP("Y");
										auxFamilyMember.setIsFamilyHead("N");
										LifeExpectancyDTO lifeExpectancyDTO = new LifeExpectancyDTO();
										lifeExpectancyDTO.setAnnualIncome(0);
										try {
											lifeExpectancyDTO.setBirthDate(sdf.parse(obj.getInvestorDOB()));
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										lifeExpectancyDTO.setIsTobaccoUser("N");
										lifeExpectancyDTO.setIsProperBMI("Y");
										lifeExpectancyDTO.setHasDiseaseHistory("N");
										lifeExpectancyDTO.setHasNormalBP("Y");
										String gender = "";
										switch (obj.getRelationId()) {
										case 1:
											if (cm.getGender().equals("M")) {
												gender = "F";
											} else {
												gender = "M";
											}
											break;
										case 2:
											gender = "M";
											break;
										case 3:
											gender = "F";
											break;
										case 4:
											gender = "M";
											break;
										case 5:
											gender = "F";
											break;
										case 6:
											gender = "M";
											break;
										case 7:
											gender = "F";
											break;

										default:
											break;
										}
										lifeExpectancyDTO.setGender(gender);

										try {
											lifeExpectancyDTO = lifeExpectancyService
													.calculateLifeExp(lifeExpectancyDTO);
											if (lifeExpectancyDTO.getTotalLifeExpectancy() != null) {
												auxFamilyMember
														.setLifeExpectancy(lifeExpectancyDTO.getTotalLifeExpectancy());
											}
										} catch (Exception exp) {
											auxFamilyMember.setLifeExpectancy((byte) 80);
										}
										long currentTime = System.currentTimeMillis();
										auxFamilyMember.setCreatedOn(new Timestamp(currentTime));
										auxFamilyMember.setRetiredFlag("N");
										auxFamilyMember.setRetirementAge((byte) 60);
										auxFamilyMember = auxFamilyMemberRepo.save(auxFamilyMember);

									} else { // New entry in auxilliary family member

										AuxillaryFamilyMember familyMember = new AuxillaryFamilyMember();
										familyMember.setClientFamilyMember(clientFamilyMemberList.get(0));
										familyMember
												.setClientID(clientFamilyMemberList.get(0).getClientMaster().getId());
										SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
										Date dt = null;
										try {
											dt = sdf.parse(obj.getInvestorDOB());
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										familyMember.setBirthDate(dt);

										String name[] = obj.getInvestorName().split(" ");
										for(int i = 0; i < name.length; i++ ) {
											if(name[i] != null) {
												if(name[i] == "" || name[i].isEmpty()) {
													name = (String[]) ArrayUtils.remove(name, i);
												}
											}
										}
										if (name.length == 1) {
											familyMember.setFirstName(obj.getInvestorName());
										} else if (name.length == 2) {
											familyMember.setFirstName(name[0]);
											familyMember.setLastName(name[1]);
										} else if (name.length == 3) {
											familyMember.setFirstName(name[0]);
											familyMember.setMiddleName(name[1]);
											familyMember.setLastName(name[2]);
										} else {
											familyMember.setFirstName(name[0]);
											familyMember.setMiddleName(name[1]);
											String lastName = name[2].trim();
											for (int count = 3; count < name.length; count++) {

												lastName = lastName + " " + name[count].trim();
											}
											familyMember.setLastName(lastName);

										}
										familyMember.setRelationID(
												clientFamilyMemberList.get(0).getLookupRelation().getId());
										familyMember.setPan(obj.getInvestorPan());
										familyMember.setDependentFlag("Y");
										familyMember.setIsTobaccoUser("N");
										familyMember.setIsProperBMI("Y");
										familyMember.setHasDiseaseHistory("N");
										familyMember.setHasNormalBP("Y");
										familyMember.setIsFamilyHead("N");
										LifeExpectancyDTO lifeExpectancyDTO = new LifeExpectancyDTO();
										lifeExpectancyDTO.setAnnualIncome(0);
										try {
											lifeExpectancyDTO.setBirthDate(sdf.parse(obj.getInvestorDOB()));
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										lifeExpectancyDTO.setIsTobaccoUser("N");
										lifeExpectancyDTO.setIsProperBMI("Y");
										lifeExpectancyDTO.setHasDiseaseHistory("N");
										lifeExpectancyDTO.setHasNormalBP("Y");
										String gender = "";
										switch (obj.getRelationId()) {
										case 1:
											if (cm.getGender().equals("M")) {
												gender = "F";
											} else {
												gender = "M";
											}
											break;
										case 2:
											gender = "M";
											break;
										case 3:
											gender = "F";
											break;
										case 4:
											gender = "M";
											break;
										case 5:
											gender = "F";
											break;
										case 6:
											gender = "M";
											break;
										case 7:
											gender = "F";
											break;

										default:
											break;
										}
										lifeExpectancyDTO.setGender(gender);

										try {
											lifeExpectancyDTO = lifeExpectancyService
													.calculateLifeExp(lifeExpectancyDTO);
											if (lifeExpectancyDTO.getTotalLifeExpectancy() != null) {
												familyMember
														.setLifeExpectancy(lifeExpectancyDTO.getTotalLifeExpectancy());
											}
										} catch (Exception exp) {
											familyMember.setLifeExpectancy((byte) 80);
										}
										long currentTime = System.currentTimeMillis();
										familyMember.setCreatedOn(new Timestamp(currentTime));
										familyMember.setRetiredFlag("N");
										familyMember.setRetirementAge((byte) 60);
										familyMember = auxFamilyMemberRepo.save(familyMember);
									}

								}

							} else {// No family member exists other than client himself

								ClientFamilyMember newClientFamilyMember = new ClientFamilyMember();
								newClientFamilyMember.setClientMaster(cm);
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date dt = null;
								try {
									dt = sdf.parse(obj.getInvestorDOB());
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								newClientFamilyMember.setBirthDate(dt);

								String name[] = obj.getInvestorName().split(" ");
								for(int i = 0; i < name.length; i++ ) {
									if(name[i] != null) {
										if(name[i] == "" || name[i].isEmpty()) {
											name = (String[]) ArrayUtils.remove(name, i);
										}
									}
								}
								if (name.length == 1) {
									newClientFamilyMember.setFirstName(obj.getInvestorName());
								} else if (name.length == 2) {
									newClientFamilyMember.setFirstName(name[0]);
									newClientFamilyMember.setLastName(name[1]);
								} else if (name.length == 3) {
									newClientFamilyMember.setFirstName(name[0]);
									newClientFamilyMember.setMiddleName(name[1]);
									newClientFamilyMember.setLastName(name[2]);
								} else {
									newClientFamilyMember.setFirstName(name[0]);
									newClientFamilyMember.setMiddleName(name[1]);
									String lastName = name[2].trim();
									for (int count = 3; count < name.length; count++) {

										lastName = lastName + " " + name[count].trim();

									}
									newClientFamilyMember.setLastName(lastName);

								}

								LookupRelation relation = lookupRelationshipRepository
										.findOne((byte) obj.getRelationId());
								newClientFamilyMember.setLookupRelation(relation);
								newClientFamilyMember.setPan(obj.getInvestorPan());
								newClientFamilyMember.setDependentFlag("Y");
								newClientFamilyMember.setIsTobaccoUser("N");
								newClientFamilyMember.setIsProperBMI("Y");
								newClientFamilyMember.setHasDiseaseHistory("N");
								newClientFamilyMember.setHasNormalBP("Y");
								newClientFamilyMember.setIsFamilyHead("N");
								LifeExpectancyDTO lifeExpectancyDTO = new LifeExpectancyDTO();
								lifeExpectancyDTO.setAnnualIncome(0);
								try {
									lifeExpectancyDTO.setBirthDate(sdf.parse(obj.getInvestorDOB()));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								lifeExpectancyDTO.setIsTobaccoUser("N");
								lifeExpectancyDTO.setIsProperBMI("Y");
								lifeExpectancyDTO.setHasDiseaseHistory("N");
								lifeExpectancyDTO.setHasNormalBP("Y");
								String gender = "";
								switch (obj.getRelationId()) {
								case 1:
									if (cm.getGender().equals("M")) {
										gender = "F";
									} else {
										gender = "M";
									}
									break;
								case 2:
									gender = "M";
									break;
								case 3:
									gender = "F";
									break;
								case 4:
									gender = "M";
									break;
								case 5:
									gender = "F";
									break;
								case 6:
									gender = "M";
									break;
								case 7:
									gender = "F";
									break;

								default:
									break;
								}
								lifeExpectancyDTO.setGender(gender);

								try {
									lifeExpectancyDTO = lifeExpectancyService.calculateLifeExp(lifeExpectancyDTO);
									if (lifeExpectancyDTO.getTotalLifeExpectancy() != null) {
										newClientFamilyMember
												.setLifeExpectancy(lifeExpectancyDTO.getTotalLifeExpectancy());
									}
								} catch (Exception exp) {
									newClientFamilyMember.setLifeExpectancy((byte) 80);
								}
								long currentTime = System.currentTimeMillis();
								newClientFamilyMember.setCreatedOn(new Timestamp(currentTime));
								newClientFamilyMember.setRetiredFlag("N");
								newClientFamilyMember.setRetirementAge((byte) 0);
								newClientFamilyMember = clientFamilyMemberRepository.save(newClientFamilyMember);

							}
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return investDTOList;
	}

	/*
	 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	 */

	@Override
	public boolean checkIfFamilyExists(int clientId, String pan) throws RuntimeException {
		// TODO Auto-generated method stub
		try {

			boolean flag;
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			ClientFamilyMember cfm = clientFamilyMemberRepository.findBypan(cm.getPan());
			flag = true;
			if (cfm != null) {
				if (cfm.getPan().equals(pan)) {
					flag = false;
				}
			}

			return flag;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public String generateAutoClient(int advisorId, String createdOn) throws RuntimeException {
		// TODO Auto-generated method stub
		String returnStatus = "";
		Map<String, String> alreadyExistingCheckMap = new HashMap<String, String>();
		Map<String, String> alreadyPresentFolioMap = new HashMap<String, String>();
		Map<String, String> alreadyExistinStagingFolio = new HashMap<String, String>();

		/*
		 * if(createdOn.getNanos()>500) {
		 * createdOn.setSeconds(createdOn.getSeconds()+1); }
		 */
		// Timestamp timestamp =Timestamp.valueOf((new SimpleDateFormat("yyyy-MM-dd
		// hh:mm:ss").format(createdOn))).t;
		List<Object[]> distinctInvestorPANList = investorRepo
				.findAllDistinctInvestorNameAndPANByAdvisorIdAndCreatedOn(advisorId, createdOn);
		System.out.println("*********************************************************distinctInvestorPANList"
				+ distinctInvestorPANList.size());
		if (distinctInvestorPANList == null || distinctInvestorPANList.size() == 0) {
			returnStatus = "Sorry, no records available for auto upload";
		}
		List<Object[]> alreadyPresentList = stagingInvestorMasterBORepository
				.findAllDistinctInvestorNameAndPAN(advisorId);

		List<Object[]> alreadyPresentFolo = stagingFolioMasterBORepository.findAllDistinctFolioNumberAndInvestorId();

		for (Object[] objList : alreadyPresentFolo) {
			String invFolio = (String) objList[0];
			String invId = "" + (Integer) objList[1];
			alreadyPresentFolioMap.put(invFolio.trim() + invId, invId);

		}

		for (Object[] objList : alreadyPresentList) {
			String invPan = (String) objList[0];
			String invName = (String) objList[1];
			alreadyExistingCheckMap.put(invName.trim().toLowerCase() + invPan.trim(), invPan.trim());

		}

		AdvisorUser advUser = advisorUserRepository.findOne(advisorId);
		for (Object[] objList : distinctInvestorPANList) {
			try {
				StagingInvestorMasterBO simBO = null;

				String pan = (String) objList[0];
				// pan.trim();
				String name = ((String) objList[1]);
				// name.trim();
				System.out.println("NAme and PAN" + name + "" + pan);
				if (alreadyExistingCheckMap != null
						&& alreadyExistingCheckMap.containsKey((name.trim().toLowerCase() + pan.trim()))
						&& alreadyExistingCheckMap.get(name.trim().toLowerCase() + pan.trim()).equals(pan.trim())) {
					simBO = stagingInvestorMasterBORepository.findByAdvisoruserAndInvestorPANAndInvestorName(advUser,
							pan.trim(), name.trim());
				}
				List<InvestorMasterSearchDTO> searchList = getInvestorDetailsByNamePan(name, pan.trim());
				int count = 0;
				for (InvestorMasterSearchDTO investorMasterSearchDTO : searchList) {
					try {
						if (((investorMasterSearchDTO.getInvestorName()).trim().equalsIgnoreCase((name.trim())))) {
							if (simBO == null && count == 0) {
								System.out.println("Investor " + name + "pan" + pan + " newly created");
								simBO = new StagingInvestorMasterBO();
								simBO.setAdvisoruser(advUser);
								simBO.setInvestorName(name);
								simBO.setInvestorPAN(pan);
								simBO.setAddressLine1(investorMasterSearchDTO.getInvestorAddressLine1());
								simBO.setAddressLine2(investorMasterSearchDTO.getInvestorAdressLine2());
								simBO.setAddressLine3(investorMasterSearchDTO.getInvestorAddressLine3());
								simBO.setEmail(investorMasterSearchDTO.getInvestorEmail());
								simBO.setMobileNumber(investorMasterSearchDTO.getInvestorMobile());
								simBO.setCity(investorMasterSearchDTO.getInvestorCity());
								simBO.setPinCode(investorMasterSearchDTO.getInvestorPinCode());
								simBO.setDistributorARNCode(investorMasterSearchDTO.getDistributorARNCode());
								simBO.setCreatedOn(createdOn);
								log.debug("In StagingInvestor for the first time" + createdOn + name + pan);
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date dt = new Date();
								try {
									if (investorMasterSearchDTO.getInvestorDOB() != null) {
										dt = sdf.parse(investorMasterSearchDTO.getInvestorDOB());
									}
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								simBO.setInvestorDOB(dt);
								if (investorMasterSearchDTO.getInvestorAdhaar() != null
										&& !investorMasterSearchDTO.getInvestorAdhaar().isEmpty()
										&& investorMasterSearchDTO.getInvestorAdhaar() != "") {
									if (investorMasterSearchDTO.getInvestorAdhaar().length() == 12) {
										simBO.setInvestorAadhar(investorMasterSearchDTO.getInvestorAdhaar());
									}
								}
								simBO = stagingInvestorMasterBORepository.save(simBO);
								System.out.println("SI"+simBO.getId()+simBO.getDistributorARNCode());
								count++;
							} else {
								simBO.setId(simBO.getId());
								simBO.setAdvisoruser(advUser);
								simBO.setInvestorName(name);
								simBO.setInvestorPAN(pan);
								simBO.setAddressLine1(investorMasterSearchDTO.getInvestorAddressLine1());
								simBO.setAddressLine2(investorMasterSearchDTO.getInvestorAdressLine2());
								simBO.setAddressLine3(investorMasterSearchDTO.getInvestorAddressLine3());
								
								if( investorMasterSearchDTO.getInvestorEmail()!=  null && !investorMasterSearchDTO.getInvestorEmail().trim().isEmpty() && !investorMasterSearchDTO.getInvestorEmail().trim().equalsIgnoreCase("null") && investorMasterSearchDTO.getInvestorEmail().trim()!= "") {
									simBO.setEmail(investorMasterSearchDTO.getInvestorEmail());
								}
								if(investorMasterSearchDTO.getInvestorMobile()!=  null && !investorMasterSearchDTO.getInvestorMobile().trim().isEmpty() && !investorMasterSearchDTO.getInvestorMobile().trim().equalsIgnoreCase("null") && investorMasterSearchDTO.getInvestorMobile().trim()!= "") {
									simBO.setMobileNumber(investorMasterSearchDTO.getInvestorMobile());
								}
								
								simBO.setCity(investorMasterSearchDTO.getInvestorCity());
								simBO.setPinCode(investorMasterSearchDTO.getInvestorPinCode());
								simBO.setDistributorARNCode(investorMasterSearchDTO.getDistributorARNCode());
								simBO.setCreatedOn(createdOn);
								log.debug("In StagingInvestor for the Edit " + createdOn + name + pan);
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date dt = new Date();
								try {
									if (investorMasterSearchDTO.getInvestorDOB() != null) {
										dt = sdf.parse(investorMasterSearchDTO.getInvestorDOB());
									}
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								simBO.setInvestorDOB(dt);
								if (investorMasterSearchDTO.getInvestorAdhaar() != null
										&& !investorMasterSearchDTO.getInvestorAdhaar().isEmpty()
										&& investorMasterSearchDTO.getInvestorAdhaar() != "") {
									if (investorMasterSearchDTO.getInvestorAdhaar().length() == 12) {
										simBO.setInvestorAadhar(investorMasterSearchDTO.getInvestorAdhaar());
									}
								}
								
								simBO = stagingInvestorMasterBORepository.save(simBO);
								System.out.println("SI2"+simBO.getId()+simBO.getDistributorARNCode());
								// count++;
							}
						}
						StagingFolioMasterBO sfmBO = null;
						String keyCheck = investorMasterSearchDTO.getInvestorFolioNo() + simBO.getId();
						log.debug("folio" + investorMasterSearchDTO.getInvestorFolioNo());
						if (alreadyPresentFolioMap != null && !alreadyPresentFolioMap.isEmpty()
								&& investorMasterSearchDTO.getInvestorFolioNo() != null
								&& alreadyPresentFolioMap.containsKey(keyCheck)
								&& alreadyPresentFolioMap
										.get(investorMasterSearchDTO.getInvestorFolioNo() + simBO.getId())
										.equals(String.valueOf(simBO.getId()))) {
							sfmBO = stagingFolioMasterBORepository.findByStaginginvestormasterboAndInvestorFolio(simBO,
									investorMasterSearchDTO.getInvestorFolioNo());
						}
						String keyToBeSearched = name + pan + investorMasterSearchDTO.getInvestorFolioNo();
						if (sfmBO == null) {
							if (!alreadyExistinStagingFolio.containsKey(keyToBeSearched)) {
								sfmBO = new StagingFolioMasterBO();
								sfmBO.setInvestorFolio(investorMasterSearchDTO.getInvestorFolioNo());
								sfmBO.setStaginginvestormasterbo(simBO);
								stagingFolioMasterBORepository.save(sfmBO);
								/*
								 * System.out.println("Folio number+ ID newly created" +
								 * investorMasterSearchDTO.getInvestorFolioNo() + simBO.getId());
								 */
								alreadyExistinStagingFolio.put(keyToBeSearched,
										investorMasterSearchDTO.getInvestorFolioNo());

							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
					}

				}
				returnStatus = "Success";
			} catch (FinexaBussinessException e) {
				// TODO Auto-generated catch block
				returnStatus = "Failure";
				e.printStackTrace();
			}
		}
		List<InvestorMasterSearchDTO> investorMasterSearchDTOList = createClientAutomatically(advisorId, createdOn);
		if(investorMasterSearchDTOList.size() > 0) {
			List<ClientARNMapping> listOflientARNMapping = clientARNMappingRepository.save(clientARNMappingList);
			for(ClientARNMapping cARN : listOflientARNMapping) {
				System.out.println(cARN.getArn()+cARN.getClientMaster()+cARN.getId());
			}
		}
		return returnStatus;
	}

	private String formatNumbers(String number) {
		boolean flag = false;
		if (number != null && !number.isEmpty() && !number.contains("null") && number != "") {
			if (!number.contains("/") && !number.matches(".*[a-zA-Z]+.*") && !number.contains("-")) {

				if (number.contains(".")) {
					Double doubleData = Double.parseDouble(number);

					BigDecimal bigDecimaldata = new BigDecimal(doubleData.toString());

					Long longData = bigDecimaldata.longValueExact();

					number = longData.toString();

				}
				return number;
			} else {
				if (number.matches(".*[.Ee].*") || number.matches(".*[.Ee-].*")) {
					for (int i = 0; i < number.length(); i++) {
						char c = number.charAt(i);
						if ((c >= 'A' && c <= 'D') || (c >= 'F' && c <= 'Z') || (c >= 'a' && c <= 'd')
								|| (c >= 'f' && c <= 'z')) {
							flag = true;
							break;

						}
					}
					if (flag == false) {
						Double doubleData = Double.parseDouble(number);

						BigDecimal bigDecimaldata = new BigDecimal(BigDecimal.valueOf(doubleData).toPlainString());

						number = bigDecimaldata.toString();

					}
					return number;

				} else
					return number;
			}

		} else
			return null;
	}

	@Override
	public Map<String, List<InvestorMasterSearchDTO>> generatePANDTOMap(int advisorID) {

		Map<String, List<InvestorMasterSearchDTO>> panDTOMap = new HashMap<>();
		List<StagingInvestorMasterBO> stagingInvestorMasterBOList = new ArrayList<>();
		List<Object[]> distinctNamePANList;
		List<InvestorMasterSearchDTO> investorMasterSearchDTOList;
		try {

			distinctNamePANList = stagingInvestorMasterBORepository.findAllDistinctInvestorNameAndPAN(advisorID);
			for (Object[] distinctNamePAN : distinctNamePANList) {
				try {
					String invPAN = distinctNamePAN[0].toString();
					String invName = distinctNamePAN[1].toString();
					investorMasterSearchDTOList = new ArrayList<>();
					if (invPAN != null && !invPAN.trim().isEmpty() && invPAN.trim() != "" && invName != null
							&& !invName.trim().isEmpty() && invName.trim() != "") {
						stagingInvestorMasterBOList = stagingInvestorMasterBORepository
								.findDistinctByInvestorPAN(invPAN);

						for (StagingInvestorMasterBO stagingInvestorMasterBO : stagingInvestorMasterBOList) {
							try {

								InvestorMasterSearchDTO investorMasterSearchDTO = new InvestorMasterSearchDTO();
								// investorMasterSearchDTO = mapper.map(stagingInvestorMasterBO,
								// StagingInvestorMasterBO.class);

								investorMasterSearchDTO.setInvestorPan(stagingInvestorMasterBO.getInvestorPAN());
								investorMasterSearchDTO.setInvestorName(stagingInvestorMasterBO.getInvestorName());
								investorMasterSearchDTO.setInvestorDOB(
										formatterDisplay.format(stagingInvestorMasterBO.getInvestorDOB()));
								investorMasterSearchDTO.setInvestorAdhaar(stagingInvestorMasterBO.getInvestorAadhar());
								investorMasterSearchDTO
										.setAdvisorUser(stagingInvestorMasterBO.getAdvisoruser().getId());
								investorMasterSearchDTO
										.setInvestorAddressLine1(stagingInvestorMasterBO.getAddressLine1());
								;
								investorMasterSearchDTO
										.setInvestorAdressLine2(stagingInvestorMasterBO.getAddressLine2());
								investorMasterSearchDTO
										.setInvestorAddressLine3(stagingInvestorMasterBO.getAddressLine3());
								investorMasterSearchDTO.setInvestorEmail(stagingInvestorMasterBO.getEmail());
								investorMasterSearchDTO.setInvestorMobile(stagingInvestorMasterBO.getMobileNumber());
								investorMasterSearchDTO.setInvestorCity(stagingInvestorMasterBO.getCity());
								investorMasterSearchDTO.setInvestorPinCode(stagingInvestorMasterBO.getPinCode());
								investorMasterSearchDTO.setInvestorGender("F");
								investorMasterSearchDTO.setDistributorARNCode(stagingInvestorMasterBO.getDistributorARNCode());
								investorMasterSearchDTOList.add(investorMasterSearchDTO);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						panDTOMap.put(invPAN, investorMasterSearchDTOList);
					}
					/*
					 * for (Map.Entry<String, List<InvestorMasterSearchDTO>> entry :
					 * panDTOMap.entrySet()) { System.out.println("Key = " +
					 * entry.getKey()+"********************************************************");
					 * 
					 * for(InvestorMasterSearchDTO investorMasterSearchDTO : entry.getValue())
					 * System.out.println(investorMasterSearchDTO);
					 * 
					 * } /*if(invPAN != null && !invPAN.trim().isEmpty() && invPAN.trim() != "" &&
					 * invName != null && !invName.trim().isEmpty() && invName.trim() != "")
					 * stagingInvestorMasterBOList = stagingInvestorMasterBORepository.
					 * findDistinctByAdvisoruserAndInvestorPANAndInvestorName(advisorID, invName,
					 * invPAN); else continue; for(StagingInvestorMasterBO stagingInvestorMasterBO :
					 * stagingInvestorMasterBOList) { try {
					 * 
					 * InvestorMasterSearchDTO investorMasterSearchDTO = new
					 * InvestorMasterSearchDTO(); //investorMasterSearchDTO =
					 * mapper.map(stagingInvestorMasterBO, StagingInvestorMasterBO.class);
					 * 
					 * investorMasterSearchDTO.setInvestorPan(stagingInvestorMasterBO.getInvestorPAN
					 * ()); investorMasterSearchDTO.setInvestorName(stagingInvestorMasterBO.
					 * getInvestorName());
					 * investorMasterSearchDTO.setInvestorDOB(formatterDisplay.format(
					 * stagingInvestorMasterBO.getInvestorDOB()));
					 * investorMasterSearchDTO.setInvestorAdhaar(stagingInvestorMasterBO.
					 * getInvestorAadhar());
					 * investorMasterSearchDTO.setAdvisorUser(stagingInvestorMasterBO.getAdvisoruser
					 * ().getId());
					 * investorMasterSearchDTO.setInvestorAddressLine1(stagingInvestorMasterBO.
					 * getAddressLine1());;
					 * investorMasterSearchDTO.setInvestorAdressLine2(stagingInvestorMasterBO.
					 * getAddressLine2());
					 * investorMasterSearchDTO.setInvestorAddressLine3(stagingInvestorMasterBO.
					 * getAddressLine3());
					 * investorMasterSearchDTO.setInvestorEmail(stagingInvestorMasterBO.getEmail());
					 * investorMasterSearchDTO.setInvestorMobile(stagingInvestorMasterBO.
					 * getMobileNumber());
					 * investorMasterSearchDTO.setInvestorCity(stagingInvestorMasterBO.getCity());
					 * investorMasterSearchDTO.setInvestorPinCode(stagingInvestorMasterBO.getPinCode
					 * ());
					 * 
					 * investorMasterSearchDTOList.add(investorMasterSearchDTO); } catch (Exception
					 * e) { e.printStackTrace(); } } panDTOMap.put(invPAN,
					 * investorMasterSearchDTOList);
					 */
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(panDTOMap.size());
		return panDTOMap;

	}

	/*******************************************************************************************************************************/

	@Override
	public Map<String, List<InvestorMasterSearchDTO>> generateOptimizedPANDTOMap(int advisorID, String createdOn) {

		Map<String, List<InvestorMasterSearchDTO>> panDTOMap = new HashMap<>();
		List<StagingInvestorMasterBO> stagingInvestorMasterBOList = new ArrayList<>();
		List<InvestorMasterSearchDTO> investorMasterSearchDTOListByUniquePAN = new ArrayList<>();

		try {
			AdvisorUser advisorUser = advisorUserRepository.findById(advisorID);
			stagingInvestorMasterBOList = stagingInvestorMasterBORepository.findByAdvisoruserAndCreatedOn(advisorUser,
					createdOn);
			for (StagingInvestorMasterBO stagingInvestorMasterBO : stagingInvestorMasterBOList) {
				try {
					String pan = stagingInvestorMasterBO.getInvestorPAN();
					System.out.println("ARN"+stagingInvestorMasterBO.getInvestorName()+stagingInvestorMasterBO.getDistributorARNCode());
					if (!panDTOMap.isEmpty()) {
						if (panDTOMap.containsKey(pan)) {
							/*
							 * If the MAP contains the PAN then the list is fetched and new records added
							 * with it and the whole list gets put in it.
							 */
							investorMasterSearchDTOListByUniquePAN = new ArrayList<>();
							investorMasterSearchDTOListByUniquePAN = panDTOMap.get(pan);

							InvestorMasterSearchDTO investorMasterSearchDTO = new InvestorMasterSearchDTO();
							investorMasterSearchDTO = mapInvestormasterWithStaging(stagingInvestorMasterBO);

							investorMasterSearchDTOListByUniquePAN.add(investorMasterSearchDTO);
							panDTOMap.put(pan, investorMasterSearchDTOListByUniquePAN);
						} else {
							/*
							 * If the MAP does not contain the PAN then records are put in a new list and
							 * the list gets put in it.
							 */
							investorMasterSearchDTOListByUniquePAN = new ArrayList<>();
							InvestorMasterSearchDTO investorMasterSearchDTO = new InvestorMasterSearchDTO();

							investorMasterSearchDTO = mapInvestormasterWithStaging(stagingInvestorMasterBO);
							investorMasterSearchDTOListByUniquePAN.add(investorMasterSearchDTO);
							panDTOMap.put(pan, investorMasterSearchDTOListByUniquePAN);
						}
					} else {
						/* First record gets entered in the MAP when the MAP is empty. */
						investorMasterSearchDTOListByUniquePAN = new ArrayList<>();
						InvestorMasterSearchDTO investorMasterSearchDTO = new InvestorMasterSearchDTO();

						investorMasterSearchDTO = mapInvestormasterWithStaging(stagingInvestorMasterBO);
						investorMasterSearchDTOListByUniquePAN.add(investorMasterSearchDTO);
						panDTOMap.put(pan, investorMasterSearchDTOListByUniquePAN);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(panDTOMap.size());
		return panDTOMap;
	}

	private InvestorMasterSearchDTO mapInvestormasterWithStaging(StagingInvestorMasterBO stagingInvestorMasterBO) {
		InvestorMasterSearchDTO investorMasterSearchDTO = new InvestorMasterSearchDTO();
		try {

			investorMasterSearchDTO.setInvestorPan(stagingInvestorMasterBO.getInvestorPAN());
			investorMasterSearchDTO.setInvestorName(stagingInvestorMasterBO.getInvestorName());
			investorMasterSearchDTO.setDistributorARNCode(stagingInvestorMasterBO.getDistributorARNCode());
			investorMasterSearchDTO.setInvestorDOB(formatterDisplay.format(stagingInvestorMasterBO.getInvestorDOB()));
			investorMasterSearchDTO.setInvestorAdhaar(stagingInvestorMasterBO.getInvestorAadhar());
			investorMasterSearchDTO.setAdvisorUser(stagingInvestorMasterBO.getAdvisoruser().getId());
			investorMasterSearchDTO.setInvestorAddressLine1(stagingInvestorMasterBO.getAddressLine1());
			;
			investorMasterSearchDTO.setInvestorAdressLine2(stagingInvestorMasterBO.getAddressLine2());
			investorMasterSearchDTO.setInvestorAddressLine3(stagingInvestorMasterBO.getAddressLine3());
			investorMasterSearchDTO.setInvestorEmail(stagingInvestorMasterBO.getEmail());
			investorMasterSearchDTO.setInvestorMobile(stagingInvestorMasterBO.getMobileNumber());
			investorMasterSearchDTO.setInvestorCity(stagingInvestorMasterBO.getCity());
			investorMasterSearchDTO.setInvestorPinCode(stagingInvestorMasterBO.getPinCode());
			investorMasterSearchDTO.setInvestorGender("M");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return investorMasterSearchDTO;
	}

	/*******************************************************************************************************************************/

	@Override
	public List<InvestorMasterSearchDTO> createClientAutomatically(int advisorID, String createdOn) {
		// TODO Auto-generated method stub

		Map<String, List<InvestorMasterSearchDTO>> panInvestorDTOListMap = new HashMap<>();
		List<InvestorMasterSearchDTO> clientList, clientfamilyList;
		List<InvestorMasterSearchDTO> responseCreateClient = new ArrayList<>();
		List<InvestorMasterSearchDTO> responseCreateFamily = new ArrayList<>();
		List<InvestorMasterSearchDTO> response = new ArrayList<>();
		try {
			panInvestorDTOListMap = generateOptimizedPANDTOMap(advisorID, createdOn);

			List<String> clientEmailList = clientContactRepository.getEmail();
			List<BigInteger> clientMobileList = clientContactRepository.getMobile();
			List<String> advisorEmailList = advisorUserRepository.getAllEmailID();
			List<BigInteger> advisorMobileList = advisorUserRepository.getAllPhoneNo();
			clientEmailList.addAll(advisorEmailList);
			clientMobileList.addAll(advisorMobileList);
			int count = 0;
			for (Map.Entry<String, List<InvestorMasterSearchDTO>> panInvestorDTOList : panInvestorDTOListMap
					.entrySet()) {

				try {
					log.debug("CLIENT CREATION PROCESS FOR INVESTOR: "
							+ panInvestorDTOList.getValue().get(0).getInvestorName() + "-" + panInvestorDTOList.getKey()
							+ ", IS GOING ON..");
					int age = 0, i = 0, maxNameLength = -1, positionOfMaxLength = -1;
					List<Integer> positionsList = new ArrayList<>();
					clientList = new ArrayList<>();
					clientfamilyList = new ArrayList<>();
					InvestorMasterSearchDTO in = null, inSearch = null;
					for (InvestorMasterSearchDTO investorMasterSearchDTO : panInvestorDTOList.getValue()) {
						count++;
						try {
							if (investorMasterSearchDTO.getInvestorEmail() != null
									&& investorMasterSearchDTO.getInvestorEmail().toString().trim() != ""
									&& !investorMasterSearchDTO.getInvestorEmail().toString().trim().isEmpty()) {
								if (clientEmailList
										.contains(investorMasterSearchDTO.getInvestorEmail().toString().trim())) {
									investorMasterSearchDTO.setInvestorEmail(generateRandomUniqueEmail());
								}
							}
							if (investorMasterSearchDTO.getInvestorMobile() != null
									&& investorMasterSearchDTO.getInvestorMobile().toString().trim() != ""
									&& !investorMasterSearchDTO.getInvestorMobile().toString().trim().isEmpty()) {
								if (clientMobileList.contains(new BigInteger(
										investorMasterSearchDTO.getInvestorMobile().toString().trim()))) {
									investorMasterSearchDTO.setInvestorMobile(generateRandomUniqueMobile().toString());
								}
							}

							/*
							 * if(investorMasterSearchDTO.getInvestorPan().equals("AFWPD8802N") ||
							 * investorMasterSearchDTO.getInvestorPan().equals("AHDPK7558D") ||
							 * investorMasterSearchDTO.getInvestorPan().equals("ACAPN4111H")
							 * investorMasterSearchDTO.getInvestorPan().equals("ADWPK2411L")) {
							 * System.out.println(investorMasterSearchDTO); }
							 */

							Date dob = new SimpleDateFormat("dd/MM/yyyy")
									.parse(investorMasterSearchDTO.getInvestorDOB());
							age = ageCalculator(dob);
							/*
							 * if (age == 6 || age == 39) { System.out.println(investorMasterSearchDTO); }
							 */
							if (investorMasterSearchDTO.getInvestorName().contains("Huf")
									|| investorMasterSearchDTO.getInvestorName().contains("HUF")
									|| investorMasterSearchDTO.getInvestorName().contains("huf")) {
								investorMasterSearchDTO.setFamilyHead(false);
								investorMasterSearchDTO.setRelationId(2);
								clientfamilyList.add(investorMasterSearchDTO);
							} else if (age < ADULT_AGE && age != 0) {
								investorMasterSearchDTO.setFamilyHead(false);
								investorMasterSearchDTO.setRelationId(2);
								clientfamilyList.add(investorMasterSearchDTO);
							} else {
								if (investorMasterSearchDTO.getInvestorName().length() > maxNameLength) {
									maxNameLength = investorMasterSearchDTO.getInvestorName().length();
									positionOfMaxLength = i;
									// positionsList.add(i);
								}
								positionsList.add(i);
							}
							i++;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (positionsList.size() > 0)
						// positionsList.remove((int) positionOfMaxLength);
						positionsList.remove(Integer.valueOf(positionOfMaxLength));
					for (int counter = 0; counter < panInvestorDTOList.getValue().size(); counter++) {
						if (positionsList.contains(counter)) {
							panInvestorDTOList.getValue().get(counter).setFamilyHead(false);
							panInvestorDTOList.getValue().get(counter).setRelationId(2);
							clientList.add(panInvestorDTOList.getValue().get(counter));
						} else if (counter == positionOfMaxLength) {
							panInvestorDTOList.getValue().get(counter).setFamilyHead(true);
							clientList.add(panInvestorDTOList.getValue().get(counter));
							panInvestorDTOList.getValue().get(counter).setRelationId(0);
						}
					}

					if (clientList.size() > 0) {
						responseCreateClient = createClient(clientList);
						if (clientfamilyList.size() > 0) {
							for (InvestorMasterSearchDTO investorMasterSearchDTO : clientfamilyList) {
								investorMasterSearchDTO.setClientId(clientID);
							}
						}
					}
					if (clientList.size() == 0 && clientfamilyList.size() > 0) {
						ClientMaster clientMaster = null;
						clientMaster = clientMasterRepository.findByAdvisorIdAndPan(advisorID,
								clientfamilyList.get(0).getInvestorPan());
						if (clientMaster != null) {
							for (InvestorMasterSearchDTO imSearchDTO : clientfamilyList) {
								imSearchDTO.setClientId(clientMaster.getId());
							}
						} else {
							/*
							 * log.debug("GUARDIAN NOT FOUND, SO CLIENT CREATION PROCESS FOR INVESTOR: " +
							 * panInvestorDTOList.getValue().get(0).getInvestorName() + "-" +
							 * panInvestorDTOList.getKey() + ", IS SKIPPED..."); continue;
							 */
							log.debug("GUARDIAN NOT FOUND, SO PARENT CLIENT CREATION PROCESS FOR INVESTOR: "
									+ panInvestorDTOList.getValue().get(0).getInvestorName() + "-"
									+ panInvestorDTOList.getKey() + ", IS STARTED...");
							// System.out.println("BEFORE:" + clientfamilyList.get(0).getInvestorName());
							inSearch = clientfamilyList.get(0);
							in = new InvestorMasterSearchDTO();
							in = mapper.map(inSearch, InvestorMasterSearchDTO.class);
							in.setInvestorName(in.getInvestorName().trim() + " Parent");
							// in.setInvestorName(in.getInvestorName().trim() + " Parent");
							in.setFamilyHead(true);
							// System.out.println("AFTER in:" + in.getInvestorName());
							// System.out.println("AFTER cm:" + clientfamilyList.get(0).getInvestorName());
							in.setRelationId(0);
							// clientList = new ArrayList<>();
							clientList.add(in);
							responseCreateClient = createClient(clientList);
							for (InvestorMasterSearchDTO searchDTO : clientfamilyList) {
								searchDTO.setClientId(clientID);
								/*
								 * investorMasterSearchDTO.setFamilyHead(false);
								 * investorMasterSearchDTO.setRelationId(2);
								 */
							}
						}
					}
					responseCreateFamily = createFamily(clientfamilyList);
					response.addAll(responseCreateFamily);
					response.addAll(responseCreateClient);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			log.debug("****************************************For Client Createion " + count + " " + createdOn);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private int ageCalculator(java.util.Date dob) throws ParseException {
		try {
			// direct age calculation
			int age = 0;

			java.util.Date d = dob;
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int date = c.get(Calendar.DATE);
			LocalDate l1 = LocalDate.of(year, month, date);
			LocalDate now1 = LocalDate.now();
			Period diff1 = Period.between(l1, now1);
			age = diff1.getYears();
			// System.out.println("age:" + diff1.getYears() + "years");
			return age;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String generateRandomUniqueEmail() {
		String email = RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
		if (clientContactRepository.findByEmailID(email) != null) {
			generateRandomUniqueEmail();
		}
		return email;
	}

	public BigInteger generateRandomUniqueMobile() {
		// String randomMobile = 79393 + RandomStringUtils.randomNumeric(5);
		BigInteger mobile = new BigInteger(79393 + RandomStringUtils.randomNumeric(5));
		if (clientContactRepository.findByMobile(mobile) != null) {
			generateRandomUniqueMobile();
		}
		return mobile;
	}

}