package com.finlabs.finexa.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.finlabs.finexa.dto.AccessRightDTO;
import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.dto.AdvisorRoleSubmoduleMappingDTO;
import com.finlabs.finexa.dto.Answer;
import com.finlabs.finexa.dto.ClientCashDTO;
import com.finlabs.finexa.dto.ClientEquityDTO;
import com.finlabs.finexa.dto.ClientExpenseDTO;
import com.finlabs.finexa.dto.ClientExportCsvDTO;
import com.finlabs.finexa.dto.ClientFamilyIncomeDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientFixedIncomeDTO;
import com.finlabs.finexa.dto.ClientFloaterCoverDTO;
import com.finlabs.finexa.dto.ClientGoalDTO;
import com.finlabs.finexa.dto.ClientInfo;
import com.finlabs.finexa.dto.ClientLifeInsuranceDTO;
import com.finlabs.finexa.dto.ClientLoanDTO;
import com.finlabs.finexa.dto.ClientLumpsumDTO;
import com.finlabs.finexa.dto.ClientMasterDTO;
import com.finlabs.finexa.dto.ClientMutualFundDTO;
import com.finlabs.finexa.dto.ClientNonlifeInsuranceDTO;
import com.finlabs.finexa.dto.ClientPpfDTO;
import com.finlabs.finexa.dto.ClientRealEstateDTO;
import com.finlabs.finexa.dto.ClientRiskProfileResponseDTO;
import com.finlabs.finexa.dto.ClientSmallSavingsDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.FinexaBusinessModuleDTO;
import com.finlabs.finexa.dto.FinexaBusinessSubmoduleDTO;
import com.finlabs.finexa.dto.FormDataDTO;
import com.finlabs.finexa.dto.ManagePasswordDTO;
import com.finlabs.finexa.dto.MasterMFProductRecoDTO;
import com.finlabs.finexa.dto.QuestionAnswers;
import com.finlabs.finexa.dto.RiskProfileMasterDTO;
import com.finlabs.finexa.dto.RiskProfileQuestionnaireDTO;
import com.finlabs.finexa.dto.RiskProfileResponseBasedScoreDTO;
import com.finlabs.finexa.dto.RoleDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.dto.UserDTO;
import com.finlabs.finexa.dto.UserDeleteReturnDTO;
import com.finlabs.finexa.dto.UserHierarchyMappingDTO;
import com.finlabs.finexa.dto.UserRoleCreationDTO;
import com.finlabs.finexa.dto.UserRoleReMappingDTO;
import com.finlabs.finexa.dto.ViewUserManagmentDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorRoleSubmoduleMapping;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserLoginInfo;
import com.finlabs.finexa.model.AdvisorUserRoleMapping;
import com.finlabs.finexa.model.AdvisorUserSupervisorMapping;
import com.finlabs.finexa.model.ClientAccessRight;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientFamilyIncome;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.FinexaBusinessModule;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.LookupEducationalQualification;
import com.finlabs.finexa.model.LookupEmploymentType;
import com.finlabs.finexa.model.LookupFrequency;
import com.finlabs.finexa.model.LookupIncomeExpenseDuration;
import com.finlabs.finexa.model.LookupMaritalStatus;
import com.finlabs.finexa.model.LookupMonth;
import com.finlabs.finexa.model.LookupResidentType;
import com.finlabs.finexa.model.LookupRole;
import com.finlabs.finexa.model.LookupTransactBSEAccessMode;
import com.finlabs.finexa.model.RiskProfileQuestionnaire;
import com.finlabs.finexa.model.User;
import com.finlabs.finexa.model.UserRole;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.AdvisorRoleRepository;
import com.finlabs.finexa.repository.AdvisorRoleSubmoduleMappingRepository;
import com.finlabs.finexa.repository.AdvisorUserLoginInfoRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserRoleMappingRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.ClientAccessRightsRepository;
import com.finlabs.finexa.repository.ClientContactRepository;
import com.finlabs.finexa.repository.ClientFamilyIncomeRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.CountryRepository;
import com.finlabs.finexa.repository.EducationalQualificationRepository;
import com.finlabs.finexa.repository.EmploymentTypeRepository;
import com.finlabs.finexa.repository.FinexaBusinessModuleRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.repository.FrequencyRepository;
import com.finlabs.finexa.repository.IncomeExpenseDurationRepository;
import com.finlabs.finexa.repository.LookupCountryRepository;
import com.finlabs.finexa.repository.LookupRelationshipRepository;
import com.finlabs.finexa.repository.LookupRoleRepository;
import com.finlabs.finexa.repository.LookupTransactBSEAccessModeRepository;
import com.finlabs.finexa.repository.MaritalStatusRepository;
import com.finlabs.finexa.repository.MonthRepository;
import com.finlabs.finexa.repository.ResidentTypeRepository;
import com.finlabs.finexa.repository.UserRepository;
import com.finlabs.finexa.repository.UserRoleRepository;
import com.finlabs.finexa.util.EmailUtil;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;
import com.finlabs.finexa.util.RandomGenerate;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Service("ViewUserManagmentService")
@Transactional

public class ViewUserManagmentServiceImpl implements ViewUserManagmentService {

	private static Logger log = LoggerFactory.getLogger(ViewUserManagmentServiceImpl.class);
	public static final String ORGANIZATION_FLAG_YES = "Y";
	public static final String ORGANIZATION_FLAG_NO = "N";

	public static final String MODULE_ACCCESS_YES = "Y";
	public static final String MODULE_ACCCESS_NO = "N";

	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Autowired
	private Mapper mapper;

	@Autowired
	private AdvisorMasterRepository advisorMasterRepository;
	
	@Autowired
	private ClientAccessRightsRepository clientAccessRightsRepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private AdvisorUserRoleMappingRepository advisorRoleMappingRepository;
	
	@Autowired
	private LookupRoleRepository lookupRoleRepository;
	
	@Autowired
	private AdvisorUserLoginInfoRepository advisorUserLoginInfoRepository;
	
	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private AdvisorRoleRepository advisorRoleRepository;

	@Autowired
	private LookupCountryRepository lookupCountryRepository;
	@Autowired
	private FinexaBusinessModuleRepository businessModuleRepo;

	@Autowired
	private FinexaBusinessSubmoduleRepository businessSubModuleRepo;

	@Autowired
	private AdvisorRoleSubmoduleMappingRepository advisorRoleSubmoduleMappingRepository;

	@Autowired
	private AdvisorUserRoleMappingRepository advisorUserRoleMappingRepository;

	@Autowired
	private AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository;

	@Autowired
	private MaritalStatusRepository maritalStatusRepository;

	@Autowired
	private EducationalQualificationRepository eduQualRepo;

	@Autowired
	private ResidentTypeRepository resTyprRepo;

	@Autowired
	private EmploymentTypeRepository empTypeRepo;

	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private ClientMasterRepository clientMasterRepo;

	@Autowired
	private LookupRelationshipRepository relationRepo;

	@Autowired
	private ClientFamilyMemberRepository familyMemRepo;

	@Autowired
	private ClientContactRepository clientContactRepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LookupTransactBSEAccessModeRepository lookupTransactBSEAccessModeRepository;

	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	AdvisorRiskProfileService advisorRiskProfileService;

	@Autowired
	MasterMFProductRecoService masterMFProductRecoService;

	@Autowired
	ClientMasterService clientMasterService;

	@Autowired
	ClientContactService clientContactService;

	@Autowired
	ClientFamilyMemberService clientFamilyMemberService;	
	
	@Autowired
	ClientFamilyIncomeService clientFamilyIncomeService;
	
	@Autowired
	ClientExpenseService clientExpenseService;
	
	@Autowired
	ClientGoalService clientGoalService;
	
	@Autowired
	ClientNonLifeInsuranceService clientNonLifeInsuranceService;
	
	@Autowired
	ClientLifeInsuranceService clientLifeInsuranceService;
	
	@Autowired
	ClientLoanService clientLoanService;
	
	@Autowired
	ClientFundService clientFundService;
	
	@Autowired
	ClientEquityService clientEquityService;
	
	@Autowired
	ClientFixedIncomeService clientFixedIncomeService;
	
	@Autowired
	ClientPPFService clientPPFService;
	
	@Autowired
	ClientRealEstateService clientRealEstateService;
	
	@Autowired
	ClientSmallSavingsService clientSmallSavingsService;

	@Autowired
	ClientCashService clientCashService;
	
	@Autowired
	ClientLumpsumService clientLumpsumService;
	
	@Autowired
	ClientRiskProfileService clientRiskProfileService;
	
	@Autowired
	private FrequencyRepository frequencyRepository;

	@Autowired
	private MonthRepository monthRepository;

	@Autowired
	private IncomeExpenseDurationRepository incomeExpenseDurationRepository;
	
	@Autowired
	private ClientFamilyIncomeRepository cientFamilyIncomeRepository;
	
	@Autowired
	private LookupRelationshipRepository lookupRelationshipRepository;
	

	@SuppressWarnings("null")
	@Override
	@Transactional(rollbackOn = CustomFinexaException.class)
	public UserDTO save(int loggedUserId, UserDTO userDTO, String serviceIP)
			throws RuntimeException, JsonParseException, JsonMappingException, IOException {
		AdvisorMaster mas = null;
		ObjectMapper m = new ObjectMapper();
		try {
			AdvisorMaster au = mapper.map(userDTO, AdvisorMaster.class);

			if ((userDTO.getOrganizationFlag()).equals("N")) {
				au.setOrgFlag(userDTO.getOrganizationFlag());
				au.setOrgName(userDTO.getOrganizationName());
				au.setDistributorCode(userDTO.getDisributorCode());
				au.setAutoCreateClient("Y");
				mas = advisorMasterRepository.save(au);
				
			} else {
			/*	if (userDTO.getOrganizationName() != null) {*/
					mas = advisorMasterRepository.findByOrgName(userDTO.getOrganizationName());
					if (mas == null) {
						au.setOrgFlag(userDTO.getOrganizationFlag());
						au.setOrgName(userDTO.getOrganizationName());
						au.setDistributorCode(userDTO.getDisributorCode());
						au.setAutoCreateClient("Y");
						mas = advisorMasterRepository.save(au);
					}
			}
			/* } */

			/*
			 * AdvisorUser advUser = advisorUserRepository.findOne(userDTO.getAdvisorID());
			 * AdvisorMaster au = advUser.getAdvisorMaster();
			 * au.setOrgFlag(userDTO.getOrganizationFlag());
			 * au.setOrgName(userDTO.getOrganizationName());
			 */

			/*
			 * User newUser = userRepository.findOne(userDTO.getMasterID()); AdvisorMaster
			 * am = newUser.getAdvisorMaster();
			 */

			/*
			 * User usr = userRepository.findOne(userDTO.getRoleId()); AdvisorRole role =
			 * usr.getAdvisorRole();
			 */

			User user = mapper.map(userDTO, User.class);
			AdvisorUser advUser = mapper.map(userDTO, AdvisorUser.class);
			
			if ((userDTO.getFinlabsAdminRole()).equals("Y")) {
				user.setAdvisorAdmin("Y");
			} 
			user.setUserManagementView("N");
			user.setClientInfoView("Y");
			user.setAdmin("N");
			
			int roleID = userDTO.getRoleId();
			//LookupRole lookupRole = lookupRoleRepository.findById((byte) roleID);
			AdvisorRole advisorRole = advisorRoleRepository.findOne(roleID);
			String roleDescription = advisorRole.getRoleDescription();
			advisorRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(mas, roleDescription);
			LookupRole lookupRole = null;
			if (advisorRole == null) {
				advisorRole = new AdvisorRole();
				advisorRole.setAdvisorMaster(mas);
				advisorRole.setRoleDescription(roleDescription);
			    lookupRole = lookupRoleRepository.findByDescription(advisorRole.getRoleDescription());
				LookupRole supervisorRole = lookupRole.getSupervisorID();
				if(supervisorRole == null) {
					advisorRole.setSupervisorRoleID(null);
				}else {
					advisorRole.setSupervisorRoleID((int) supervisorRole.getId());
				}
				advisorRole = advisorRoleRepository.save(advisorRole);
			}
				
			user.setAdvisorRole(advisorRole);

			LookupCountry lookupCountry = lookupCountryRepository.findOne(userDTO.getCountryId());
			/*
			 * if (userDTO.getUserId() > 0) { advUser.setId(au.getId()); user.setId((int)
			 * (userDTO.getUserId())); }
			 */
			if (mas == null) {
				user.setAdvisorMaster(au);
				advUser.setAdvisorMaster(au);
			} else {
				user.setAdvisorMaster(mas);
				advUser.setAdvisorMaster(mas);
			}

			advUser.setLookupCountry(lookupCountry);
			LookupTransactBSEAccessMode lookupTransactBseaccessMode = lookupTransactBSEAccessModeRepository
					.findOne((byte) 2);
			if (userDTO.getGender().equals("M")) {
				advUser.setSalutation("Mr");
			} else {
				advUser.setSalutation("Ms");
			}
			advUser.setBirthDate(userDTO.getBirthDate());
			advUser.setGender(userDTO.getGender());
			advUser.setLookupTransactBseaccessMode(lookupTransactBseaccessMode);
			advUser.setLoginUsername(userDTO.getEmailID());
			advUser.setLoginPassword(userDTO.getFirstName().trim());
			advUser.setPhoneCountryCode(lookupCountry.getPhonecode().toString());
			advUser.setActiveFlag("Y");
			advUser.setLastLoginTime(new Date());
			advUser.setGender(userDTO.getGender());
			/*
			 * advUser.setBudgetManagement("Y"); advUser.setGoalPlanning("Y");
			 * advUser.setPortfolioManagement("Y"); advUser.setFinancialPlanning("Y");
			 */
			
            //If Finlabs India's admin create a user then that particular user's role will be assigned to advisor admin//
		
			
			user.setActiveFlag("Y");
			user.setLoginUsername(userDTO.getEmailID());
			user.setLoginPassword(userDTO.getFirstName());
			user.setCreatedOn(new Date());
			user = userRepository.save(user);

			advUser.setUser(user);
			advUser.setLoggedInFlag("N");
			advUser = advisorUserRepository.save(advUser);
			
			//send email
			AdvisorUser loggedUser = advisorUserRepository.findById(loggedUserId);
			if (loggedUser.getUser().getAdmin() != null && loggedUser.getUser().getAdmin().equalsIgnoreCase("Y")) {
				
				StringBuilder sb = new StringBuilder();
				sb.append("Dear ");
				sb.append(advUser.getFirstName() + " " + advUser.getLastName() + ",\n");
				sb.append("\n");
				sb.append("Welcome to Finexa , the financial advisor's ERP\n\n"
						+ "Please find below your login credentials : \n"
						+ "User ID : ");
				sb.append(advUser.getEmailID() + "\n");
				sb.append("Password : ");
				sb.append(advUser.getLoginPassword() + "\n");
				sb.append("URL : " + serviceIP + "\n\n");
				sb.append("We can be reached during the regular business hours on the following contacts :\n" + 
						  "Phone: +91-022-62360605\n" + 
						  "Email: finexahelp@finlabsindia.com\n\n" +
						  "Relationship Manager: Shantanu Rathore\n" + 
						  "Email Id: shantanu.r@finlabsindia.com\n" + 
						  "Ph. No. +919986909709\n\n" +
						  "Regards,\nFinexa Admin.\n\n");
				List<String> toList = new ArrayList<String>();
				toList.add(advUser.getEmailID());

				EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_WELCOME_MESSAGE, sb.toString());
				
			} else {
				if (loggedUser.getUser().getAdvisorAdmin() != null && loggedUser.getUser().getAdvisorAdmin().equalsIgnoreCase("Y")) {
					
					StringBuilder sb = new StringBuilder();
					sb.append("Dear ");
					sb.append(advUser.getFirstName() + " " + advUser.getLastName() + ",\n");
					sb.append("\n");
					sb.append("Welcome to Finexa , the financial advisor's ERP\n\n"
							+ "Please find below your login credentials : \n"
							+ "User ID : ");
					sb.append(advUser.getEmailID() + "\n");
					sb.append("Password : ");
					sb.append(advUser.getLoginPassword() + "\n");
					sb.append("URL : " + serviceIP + "\n\n");
					sb.append("We can be reached on the following contacts :\n" + 
							  "Phone: "+ loggedUser.getPhoneNo() +"\n" + 
							  "Email: "+ loggedUser.getEmailID() +"\n\n" +
							  "Regards,\n" + loggedUser.getFirstName() + " " + loggedUser.getLastName() + ".\n\n");
					List<String> toList = new ArrayList<String>();
					toList.add(advUser.getEmailID());

					EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_WELCOME_MESSAGE, sb.toString());
					
				} 
			}
			
			// int advId = advisorUser.getId();
			// saving user role mapping

			/***********************
			 * check if exists
			 ****************************/
			AdvisorUserRoleMapping advUserRoleMapping = new AdvisorUserRoleMapping();

			if (userDTO.getRoleId() != null) {
				AdvisorUserRoleMapping existingAdvisorUserRoleMapping = advisorRoleMappingRepository
						.checkIfExists(userDTO.getRoleId(), (int) userDTO.getUserID());
				if (existingAdvisorUserRoleMapping != null) {
					advUserRoleMapping.setId(existingAdvisorUserRoleMapping.getId());
				}
			}

			advUserRoleMapping.setAdvisorUser(advUser);
			//AdvisorRole advRole = advisorRoleRepository.findOne(userDTO.getRoleId());
			advUserRoleMapping.setAdvisorRole(advisorRole);
			Date dt = new Date();
			advUserRoleMapping.setEffectiveFromDate(dt);

			advisorRoleMappingRepository.save(advUserRoleMapping);

			// new security code start

			InputStream resource = null;
			List<RiskProfileQuestionnaire> riskProfileQuestionnaireList = null;
			int masterId;
			if (userDTO.getRiskProfileCreation() == 1) {
				
				List<RiskProfileMasterDTO> riskProfileMasterList = new ArrayList<RiskProfileMasterDTO>();

				for (int i = 1; i <= 10; i++) {
					RiskProfileMasterDTO riskProfileMasterDTO = new RiskProfileMasterDTO();
					if (mas == null) {
						riskProfileMasterDTO.setAdvisorID(au.getId());
					} else {
						riskProfileMasterDTO.setAdvisorID(mas.getId());
					}
					riskProfileMasterDTO.setName("" + i);
					riskProfileMasterList.add(riskProfileMasterDTO);
				}
				advisorRiskProfileService.saveRiskprofileName(riskProfileMasterList);
				resource = FinexaUtil.class.getClassLoader().getResourceAsStream("RiskProfileQuestion.json");

				QuestionAnswers[] qas = m.readValue(resource, com.finlabs.finexa.dto.QuestionAnswers[].class);
				List<RiskProfileQuestionnaireDTO> riskProfileQuestionnaireDTOList = new ArrayList<RiskProfileQuestionnaireDTO>();
				for (QuestionAnswers qa : qas) {
					RiskProfileQuestionnaireDTO riskProfileQuestionnaireDTO = new RiskProfileQuestionnaireDTO();
					riskProfileQuestionnaireDTO.setQuestion(qa.question);
					if (mas == null) {
						riskProfileQuestionnaireDTO.setAdvisorId(au.getId());
					} else {
						riskProfileQuestionnaireDTO.setAdvisorId(mas.getId());
					}

					List<RiskProfileResponseBasedScoreDTO> riskProfileResponseBasedScoreDTOList = new ArrayList<RiskProfileResponseBasedScoreDTO>();
					int l = 0;
					for (Answer a : qa.answers) {
						RiskProfileResponseBasedScoreDTO riskProfileResponseBasedScoreDTO = new RiskProfileResponseBasedScoreDTO();
						riskProfileResponseBasedScoreDTO.setResponseID(++l);
						riskProfileResponseBasedScoreDTO.setResponseText(a.answer);
						riskProfileResponseBasedScoreDTO.setScore(a.score);

						riskProfileResponseBasedScoreDTOList.add(riskProfileResponseBasedScoreDTO);
					}
					riskProfileQuestionnaireDTO
							.setRiskProfileResponseBasedScoresDTO(riskProfileResponseBasedScoreDTOList);
					riskProfileQuestionnaireDTOList.add(riskProfileQuestionnaireDTO);
				}
				riskProfileQuestionnaireList= advisorRiskProfileService.autosaveQuestion(riskProfileQuestionnaireDTOList);
			}

			if (userDTO.getProductRecoCreation() == 1) {
				resource = FinexaUtil.class.getClassLoader().getResourceAsStream("ProductReco.json");
				MasterMFProductRecoDTO[] productReco = m.readValue(resource, MasterMFProductRecoDTO[].class);

				List<String> isinList = new ArrayList<String>();
				int i = 0;
				String startDate = "";
				String endDate = "";
				for (MasterMFProductRecoDTO ob : productReco) {
					if (i == 0) {
						startDate = ob.getStartDate();
						endDate = ob.getEndDate();
					}
					isinList.add(ob.getIsin());
				}
				
				if (mas == null) {
					masterId = au.getId();
				} else {
					masterId = mas.getId();
				}

				masterMFProductRecoService.saveMasterMfProductRecommendation(masterId, isinList, startDate, endDate);
			}
			// client creation
			if (userDTO.getClientCreation() == 1) {
				resource = FinexaUtil.class.getClassLoader().getResourceAsStream("ClientCreation.json");
				List<Integer> questionIDs = new ArrayList<Integer>();
				for(RiskProfileQuestionnaire riskProfileQuestionnaire : riskProfileQuestionnaireList) {
						questionIDs.add(riskProfileQuestionnaire.getId());
                 }
				
				//int clientId = generateDemoLogin(advUser.getId(), resource, questionIDs); // Calling authenticateUser
																							// function
				int clientID = generateDemoLogin(advUser.getId(), resource, questionIDs);
				
				if (mas == null) {
					masterId = au.getId();
				} else {
					masterId = mas.getId();
				}
				userDTO.setMasterID(masterId);
				userDTO.setClientID(clientID);
			}
			// end security code
			return userDTO;
		} catch (RuntimeException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	
	
	@Override
	public UserDTO update(UserDTO userDTO) throws RuntimeException {

		try {
			User user = userRepository.findOne(userDTO.getUserID());
			//AdvisorMaster am = user.getAdvisorMaster();
			AdvisorUser advUser = user.getAdvisorUser();
			
			boolean sendEmail;
			if (advUser.getEmailID().equals(userDTO.getEmailID()) && advUser.getLoginPassword().equals(userDTO.getFirstName())) {
				sendEmail = false;
			} else {
				sendEmail = true;
			}
			
			//user.setAdvisorMaster(am);
			//user.setAdvisorUser(advUser);
			
			LookupCountry lookupCountry = lookupCountryRepository.findOne(userDTO.getCountryId());
			/*
			 * if (userDTO.getUserID() > 0) { advisorUser.setId((int)
			 * (userDTO.getUserID())); }
			 */
			advUser.setLookupCountry(lookupCountry);

			if (userDTO.getGender().equals("M")) {
				advUser.setSalutation("Mr");
			} else {
				advUser.setSalutation("Ms");
			}
			advUser.setBirthDate(userDTO.getBirthDate());
			advUser.setGender(userDTO.getGender());
			advUser.setFirstName(userDTO.getFirstName());
			advUser.setLastName(userDTO.getLastName());
			advUser.setLoginUsername(userDTO.getEmailID());
			advUser.setEmailID(userDTO.getEmailID());
			advUser.setLoginPassword(userDTO.getFirstName());
			advUser.setPhoneCountryCode(lookupCountry.getPhonecode().toString());
			advUser.setActiveFlag("Y");
			advUser.setPhoneNo(userDTO.getPhoneNo());
			advUser.setCity(userDTO.getCity());
			advUser.setState(userDTO.getState());
			advUser.setGender(userDTO.getGender());
			/*
			 * advUser.setBudgetManagement("Y");
			 * advUser.setGoalPlanning("Y");
			 * advUser.setPortfolioManagement("Y");
			 * advUser.setFinancialPlanning("Y");
			 */
			LookupTransactBSEAccessMode lookupTransactBseaccessMode = lookupTransactBSEAccessModeRepository.findOne((byte) 2);
			advUser.setLookupTransactBseaccessMode(lookupTransactBseaccessMode);
			user.setActiveFlag("Y");
			user.setLoginUsername(userDTO.getEmailID());
			user.setLoginPassword(userDTO.getFirstName());
			user.setCreatedOn(new Date());
			user = userRepository.save(user);

			advUser.setUser(user);
			
			AdvisorUser	advisorUser = advisorUserRepository.save(advUser);
			// int advId = advisorUser.getId();
			// saving user role mapping

			/***********************
			 * check if exists  update
			 ****************************/
			//AdvisorUserRoleMapping advUserRoleMapping = new AdvisorUserRoleMapping();

			if (userDTO.getRoleId() != null) {
				System.out.println("userDTO.getRoleId() "+userDTO.getRoleId());
				System.out.println("userDTO.userDTO.getUserID()() "+userDTO.getAdvisorID());
				AdvisorUserRoleMapping advUserRoleMapping = advisorRoleMappingRepository
						.checkIfExists(userDTO.getRoleId(), (int) advUser.getId());
				/*
				 * if (existingAdvisorUserRoleMapping != null) {
				 * advUserRoleMapping.setId(existingAdvisorUserRoleMapping.getId()); }
				 */
				System.out.println("advUserRoleMapping "+advUserRoleMapping);
				advUserRoleMapping.setAdvisorUser(advUser);
				AdvisorRole advRole = advisorRoleRepository.findOne(userDTO.getRoleId());
				advUserRoleMapping.setAdvisorRole(advRole);
				Date dt = new Date();
				advUserRoleMapping.setEffectiveFromDate(dt);
				advisorRoleMappingRepository.save(advUserRoleMapping);
			}

			if (sendEmail == true) {
				StringBuilder sb = new StringBuilder();
				sb.append("Dear ");
				sb.append(advUser.getFirstName() + " " + advUser.getLastName() + ",\n");
				sb.append("\n");
				sb.append("Your Credentials for Finexa Application has been changed to:\n"
						+ "Username : " + advisorUser.getLoginUsername() + "\nPassword : ");
				sb.append(advUser.getLoginPassword() + "\n\n");
				sb.append("Administrator , Finexa Application \n\n");
				List<String> toList = new ArrayList<String>();
				toList.add(advisorUser.getEmailID());

				EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_PASSWORD_RESET, sb.toString());
			}

			return userDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public UserDTO update(int userId, UserDTO userDTO) throws RuntimeException {

		try {
			User user = userRepository.findOne(userDTO.getUserID());
			//AdvisorMaster am = user.getAdvisorMaster();
			AdvisorUser advUser = user.getAdvisorUser();
			
			boolean sendEmail;
			if (advUser.getEmailID().equals(userDTO.getEmailID()) && advUser.getLoginPassword().equals(userDTO.getFirstName())) {
				sendEmail = false;
			} else {
				sendEmail = true;
			}
			
			//user.setAdvisorMaster(am);
			//user.setAdvisorUser(advUser);
			
			LookupCountry lookupCountry = lookupCountryRepository.findOne(userDTO.getCountryId());
			/*
			 * if (userDTO.getUserID() > 0) { advisorUser.setId((int)
			 * (userDTO.getUserID())); }
			 */
			advUser.setLookupCountry(lookupCountry);

			if (userDTO.getGender().equals("M")) {
				advUser.setSalutation("Mr");
			} else {
				advUser.setSalutation("Ms");
			}
			advUser.setBirthDate(userDTO.getBirthDate());
			advUser.setGender(userDTO.getGender());
			advUser.setFirstName(userDTO.getFirstName());
			advUser.setLastName(userDTO.getLastName());
			advUser.setLoginUsername(userDTO.getEmailID());
			advUser.setEmailID(userDTO.getEmailID());
			advUser.setLoginPassword(userDTO.getFirstName());
			advUser.setPhoneCountryCode(lookupCountry.getPhonecode().toString());
			advUser.setActiveFlag("Y");
			advUser.setPhoneNo(userDTO.getPhoneNo());
			advUser.setCity(userDTO.getCity());
			advUser.setState(userDTO.getState());
			advUser.setGender(userDTO.getGender());
			/*
			 * advUser.setBudgetManagement("Y");
			 * advUser.setGoalPlanning("Y");
			 * advUser.setPortfolioManagement("Y");
			 * advUser.setFinancialPlanning("Y");
			 */
			LookupTransactBSEAccessMode lookupTransactBseaccessMode = lookupTransactBSEAccessModeRepository.findOne((byte) 2);
			advUser.setLookupTransactBseaccessMode(lookupTransactBseaccessMode);
			user.setActiveFlag("Y");
			user.setLoginUsername(userDTO.getEmailID());
			user.setLoginPassword(userDTO.getFirstName());
			user.setCreatedOn(new Date());
			user = userRepository.save(user);

			advUser.setUser(user);
			
			AdvisorUser	advisorUser = advisorUserRepository.save(advUser);
			// int advId = advisorUser.getId();
			// saving user role mapping

			/***********************
			 * check if exists  update
			 ****************************/
			//AdvisorUserRoleMapping advUserRoleMapping = new AdvisorUserRoleMapping();

			if (userDTO.getRoleId() != null) {
				System.out.println("userDTO.getRoleId() "+userDTO.getRoleId());
				System.out.println("userDTO.userDTO.getUserID()() "+userDTO.getAdvisorID());
				AdvisorUserRoleMapping advUserRoleMapping = advisorRoleMappingRepository
						.checkIfExists(userDTO.getRoleId(), (int) advUser.getId());
				/*
				 * if (existingAdvisorUserRoleMapping != null) {
				 * advUserRoleMapping.setId(existingAdvisorUserRoleMapping.getId()); }
				 */
				System.out.println("advUserRoleMapping "+advUserRoleMapping);
				advUserRoleMapping.setAdvisorUser(advUser);
				AdvisorRole advRole = advisorRoleRepository.findOne(userDTO.getRoleId());
				advUserRoleMapping.setAdvisorRole(advRole);
				Date dt = new Date();
				advUserRoleMapping.setEffectiveFromDate(dt);
				advisorRoleMappingRepository.save(advUserRoleMapping);
			}

			if (sendEmail == true) {
				String URLForEmail = "";
				Properties prop = new Properties();
				InputStream input = null;

				try {
				    input = new FileInputStream("/home/forgot_password.properties");
				    prop.load(input);
				    URLForEmail = prop.getProperty("URLForEmail");
				} catch (IOException ex) {
				    ex.printStackTrace();
				}
				
				AdvisorUser adv = advisorUserRepository.findOne(userId);
				if (adv.getUser().getAdmin() != null && adv.getUser().getAdmin().equalsIgnoreCase("Y")) {
					StringBuilder sb = new StringBuilder();
					sb.append("Dear ");
					sb.append(advUser.getFirstName() + " " + advUser.getLastName() + ",\n");
					sb.append("\nGreeting from Finlabs India Pvt Ltd.\n\n");
					sb.append("Please find below your new login credentials to access the portal:\n"
							+ "Username : " + advUser.getLoginUsername() + "\nPassword : ");
					sb.append(advUser.getLoginPassword() + "\nURL : " + URLForEmail + "\n\n");
					sb.append("In case of any queries, feel free to reach on " + adv.getEmailID() + "\n\n");
					sb.append("Regards,\nFinlabs Administrator.");
					List<String> toList = new ArrayList<String>();
					toList.add(advUser.getEmailID());

					EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, 
							toList, FinexaConstant.FINEXA_PASSWORD_RESET, sb.toString());
				} else if (adv.getUser().getAdvisorAdmin() != null && adv.getUser().getAdvisorAdmin().equalsIgnoreCase("Y")) {
					StringBuilder sb = new StringBuilder();
					sb.append("Dear ");
					sb.append(advUser.getFirstName() + " " + advUser.getLastName() + ",\n");
					sb.append("\nGreeting from " + adv.getFirstName() + " " + adv.getLastName() + "\n\n");
					sb.append("Please find below your new login credentials to access the portal:\n"
							+ "Username : " + advUser.getLoginUsername() + "\nPassword : ");
					sb.append(advUser.getLoginPassword() + "\nURL : " + URLForEmail + "\n\n");
					sb.append("In case of any queries, feel free to reach on " + adv.getEmailID() + " and " 
							+ adv.getPhoneNo() + "\n\n");
					sb.append("Regards,\n" + adv.getFirstName() + " " + adv.getLastName());
					List<String> toList = new ArrayList<String>();
					toList.add(advUser.getEmailID());

					EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_PASSWORD_UPDATE, sb.toString());
				}
				
			}

			return userDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/*
	 * @Override public UserDTO update(UserDTO userDTO) throws RuntimeException {
	 * 
	 * try { AdvisorMaster au = mapper.map(userDTO, AdvisorMaster.class);
	 * 
	 * if ((userDTO.getOrganizationFlag()).equals("N")) {
	 * au.setOrgFlag(userDTO.getOrganizationFlag());
	 * au.setOrgName(userDTO.getOrganizationName());
	 * au.setDistributorCode(userDTO.getDisributorCode()); au =
	 * advisorMasterRepository.save(au); } else { if (userDTO.getOrganizationName()
	 * != null) { AdvisorMaster mas =
	 * advisorMasterRepository.findByOrgName(userDTO.getOrganizationName()); if (mas
	 * == null) { au.setOrgFlag(userDTO.getOrganizationFlag());
	 * au.setOrgName(userDTO.getOrganizationName()); au =
	 * advisorMasterRepository.save(au); } } }
	 * 
	 * 
	 * AdvisorUser advUser = advisorUserRepository.findOne(userDTO.getAdvisorID());
	 * AdvisorMaster au = advUser.getAdvisorMaster();
	 * au.setOrgFlag(userDTO.getOrganizationFlag());
	 * au.setOrgName(userDTO.getOrganizationName());
	 * 
	 * 
	 * 
	 * User newUser = userRepository.findOne(userDTO.getMasterID()); AdvisorMaster
	 * am = newUser.getAdvisorMaster();
	 * 
	 * 
	 * 
	 * User usr = userRepository.findOne(userDTO.getRoleId()); AdvisorRole role =
	 * usr.getAdvisorRole();
	 * 
	 * 
	 * User user = mapper.map(userDTO, User.class); AdvisorUser advUser =
	 * mapper.map(userDTO, AdvisorUser.class);
	 * 
	 * AdvisorRole advisorRole = advisorRoleRepository.findOne(userDTO.getRoleId());
	 * user.setAdvisorRole(advisorRole);
	 * 
	 * LookupCountry lookupCountry =
	 * lookupCountryRepository.findOne(userDTO.getCountryId());
	 * 
	 * if (userDTO.getUserId() > 0) { advUser.setId(au.getId()); user.setId((int)
	 * (userDTO.getUserId())); }
	 * 
	 * user.setAdvisorMaster(au); advUser.setAdvisorMaster(au);
	 * advUser.setLookupCountry(lookupCountry); LookupTransactBSEAccessMode
	 * lookupTransactBseaccessMode = lookupTransactBSEAccessModeRepository
	 * .findOne((byte) 2); if (userDTO.getGender() == "M") {
	 * advUser.setSalutation("Mr"); } else { advUser.setSalutation("Ms"); }
	 * advUser.setBirthDate(userDTO.getBirthDate());
	 * advUser.setGender(userDTO.getGender());
	 * advUser.setLookupTransactBseaccessMode(lookupTransactBseaccessMode);
	 * advUser.setLoginUsername(userDTO.getEmailID());
	 * advUser.setLoginPassword(userDTO.getFirstName());
	 * advUser.setPhoneCountryCode(lookupCountry.getPhonecode().toString());
	 * advUser.setActiveFlag("Y"); advUser.setBudgetManagement("Y");
	 * advUser.setGoalPlanning("Y"); advUser.setPortfolioManagement("Y");
	 * advUser.setFinancialPlanning("Y"); AdvisorMaster master =
	 * advisorMasterRepository.findOne(userDTO.getMasterID());
	 * advUser.setAdvisorMaster(master); user.setAdvisorMaster(master);
	 * user.setActiveFlag("Y"); user.setLoginUsername(userDTO.getEmailID());
	 * user.setLoginPassword(userDTO.getFirstName()); user.setCreatedOn(new Date());
	 * user = userRepository.save(user); advUser.setUser(user);
	 * 
	 * advUser = advisorUserRepository.save(advUser); // int advId =
	 * advisorUser.getId(); // saving user role mapping
	 * 
	 *//***********************
		 * check if exists
		 ****************************//*
										 * AdvisorUserRoleMapping advUserRoleMapping = new AdvisorUserRoleMapping();
										 * 
										 * if (userDTO.getRoleId() != null) { AdvisorUserRoleMapping
										 * existingAdvisorUserRoleMapping = advisorRoleMappingRepository
										 * .checkIfExists(userDTO.getRoleId(), (int) userDTO.getUserID()); if
										 * (existingAdvisorUserRoleMapping != null) {
										 * advUserRoleMapping.setId(existingAdvisorUserRoleMapping.getId()); } }
										 * 
										 * advUserRoleMapping.setAdvisorUser(advUser); AdvisorRole advRole =
										 * advisorRoleRepository.findOne(userDTO.getRoleId());
										 * advUserRoleMapping.setAdvisorRole(advRole); Date dt = new Date();
										 * advUserRoleMapping.setEffectiveFromDate(dt);
										 * 
										 * advisorRoleMappingRepository.save(advUserRoleMapping);
										 * 
										 * return userDTO; } catch (RuntimeException e) { // TODO Auto-generated catch
										 * block throw new RuntimeException(e); } }
										 */

	@Override
	public List<ViewUserManagmentDTO> getAllUserList(int userId) throws RuntimeException {
		try {
			List<ViewUserManagmentDTO> viewUserManagmentDTOList = new ArrayList<>();
			AdvisorMaster advisorMaster = advisorUserRepository.findOne(userId).getAdvisorMaster();
			List<AdvisorUser> viewUserManagmentList = advisorMaster.getAdvisorUsers();

			for (AdvisorUser obj : viewUserManagmentList) {
				
				//------check if advisor admin----//
				//author Arghya
				if (obj.getUser().getAdvisorAdmin() ==  null || obj.getUser().getAdvisorAdmin().equalsIgnoreCase("n")) {
					/*
					 * try { if(obj.getUser().getId() == 0) continue; }
					 * catch(EntityNotFoundException e) { continue; }
					 */
					
					ViewUserManagmentDTO viewUserManagmentDTO = new ViewUserManagmentDTO();
					viewUserManagmentDTO.setId(obj.getId());
					// populating Name
					viewUserManagmentDTO.setUserName(obj.getFirstName() + " "
							+ (obj.getMiddleName() == null ? " " : obj.getMiddleName()) + " " + obj.getLastName());
					// populating Location
					viewUserManagmentDTO.setUserLocation(obj.getCity());
					
					viewUserManagmentDTO.setUserID(obj.getUser().getId());
					
					viewUserManagmentDTO.setEmailID(obj.getEmailID());
					
					viewUserManagmentDTO.setActiveFlag(obj.getActiveFlag());
					
					viewUserManagmentDTO.setFirstName(obj.getFirstName());
					
					viewUserManagmentDTO.setLastName(obj.getLastName());
					
					viewUserManagmentDTO.setRole(obj.getUser().getAdvisorRole().getRoleDescription());
					/*****************************
					 * 0 is default index
					 *******************************/
					if (obj.getAdvisorUserRoleMappings().size() > 0) {
						AdvisorUserRoleMapping userRoleMapping = obj.getAdvisorUserRoleMappings().get(0);
						viewUserManagmentDTO.setUserRole(userRoleMapping.getAdvisorRole().getRoleDescription());
						viewUserManagmentDTO.setUserRoleId(userRoleMapping.getAdvisorRole().getId());
						AdvisorRole advRole = userRoleMapping.getAdvisorRole();
						if (advRole.getSupervisorRoleID() != null) {
							viewUserManagmentDTO.setSupervisorRoleId(advRole.getSupervisorRoleID());
							int superVisorRoleID = advRole.getSupervisorRoleID();
							LookupRole supervisorRole = lookupRoleRepository.findById((byte)superVisorRoleID);
							viewUserManagmentDTO.setSupervisorRoleName(supervisorRole.getDescription());
							
							/*
							 * viewUserManagmentDTO.setSupervisorRoleName(
							 * advisorRoleRepository.findOne(advRole.getSupervisorRoleID()).
							 * getRoleDescription());
							 */
						}else {
							viewUserManagmentDTO.setSupervisorRoleName("Not Applicable");
						}
					}
	
					viewUserManagmentDTOList.add(viewUserManagmentDTO);
				}
			}
			return viewUserManagmentDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserDeleteReturnDTO deleteUser(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		User user = null;
		try {
			UserDeleteReturnDTO userDeleteReturnDTO = new UserDeleteReturnDTO();
			int returnInt = FinexaConstant.RETURN_VAL_ERROR;
			int flag = 0;
			if (id > 0) {
				 user = userRepository.findOne(id);
				// Check Whether this user has any Supervisor Mappings
				List<AdvisorUserSupervisorMapping> advisorUserSupervisorMappings = advisorUserSupervisorMappingRepository
						.getAllSupervisorWithSupervisorId(user.getAdvisorUser().getId());
				try {
					if (advisorUserSupervisorMappings != null && advisorUserSupervisorMappings.size() > 0) {
						returnInt = FinexaConstant.RETURN_VAL_ERROR_SUPERVISOR_MAPPING;
						flag = 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
					returnInt = FinexaConstant.RETURN_VAL_ERROR;
				}
				// check whether this user has any clients
				List<ClientMaster> masterList = clientMasterRepo
						.findByAdvisorUserAndActiveFlag(advisorUserRepository.findOne(user.getAdvisorUser().getId()), "Y");
				try {
					if (masterList != null && masterList.size() > 0) {
						returnInt = FinexaConstant.RETURN_VAL_ERROR_CLIENT_MAPPING;
						flag = 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
					returnInt = FinexaConstant.RETURN_VAL_ERROR;
				}
				if (flag == 0) {
					// delete supervisor mappings
					List<AdvisorUserSupervisorMapping> advisorUserSupervisorMappingList = advisorUserSupervisorMappingRepository
							.getAllSupervisorWithUserId(user.getAdvisorUser().getId());
					try {
						if (advisorUserSupervisorMappingList != null && advisorUserSupervisorMappingList.size() > 0) {
							for (AdvisorUserSupervisorMapping obj : advisorUserSupervisorMappingList) {
								advisorUserSupervisorMappingRepository.delete(obj.getId());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						returnInt = FinexaConstant.RETURN_VAL_ERROR;
					}
					// first delete all role mappings
					List<AdvisorUserRoleMapping> advisorUserRoleMappingList = advisorRoleMappingRepository
							.getAllUserRoleWithUserId(user.getAdvisorUser().getId());
					try {
						if (advisorUserRoleMappingList != null && advisorUserRoleMappingList.size() > 0) {
							for (AdvisorUserRoleMapping obj : advisorUserRoleMappingList) {
								advisorRoleMappingRepository.delete(obj.getId());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						returnInt = FinexaConstant.RETURN_VAL_ERROR;
					}
					// finally delete the user
					try {
						//first delete login info
						List<AdvisorUserLoginInfo> loginInfos = advisorUserLoginInfoRepository.findByAdvisorUser(user.getAdvisorUser());
						if (loginInfos.size() > 0) {
							for (AdvisorUserLoginInfo obj : loginInfos) {
								advisorUserLoginInfoRepository.delete(obj);
							}
						}
						advisorUserRepository.delete(user.getAdvisorUser().getId());
						//advisorUserLoginInfoRepository.delete(user.getAdvisorUser().getId());
						userRepository.delete(id);
						returnInt = FinexaConstant.RETURN_VAL_SUCCESS;
					} catch (Exception e) {
						e.printStackTrace();
						returnInt = FinexaConstant.RETURN_VAL_ERROR;
					}
				}
			}
			userDeleteReturnDTO.setUserId(user.getAdvisorUser().getId());
			userDeleteReturnDTO.setReturnStatus(returnInt);
			return userDeleteReturnDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserDeleteReturnDTO deleteRole(int roleId) throws RuntimeException {
		// TODO Auto-generated method stub

		UserDeleteReturnDTO userDeleteReturnDTO = new UserDeleteReturnDTO();
		int returnInt = FinexaConstant.RETURN_VAL_ERROR;
		int flag = 0;
		if (roleId > 0) {
			// Check Whether this user has any Role Mappings
			List<AdvisorUserRoleMapping> advisorUserRoleMappingList = advisorRoleMappingRepository
					.getAllUserRoleWithRoleId(roleId);
			try {
				if (advisorUserRoleMappingList != null && advisorUserRoleMappingList.size() > 0) {
					returnInt = FinexaConstant.RETURN_VAL_ERROR_ROLE_MAPPING;
					flag = 1;
				}
			} catch (Exception e) {
				returnInt = FinexaConstant.RETURN_VAL_ERROR;
			}
			// Check Whether this user has any Supervisor Mappings
			List<AdvisorRole> supervisorMappings = advisorRoleRepository.findBySupervisorRoleID(roleId);
			try {
				if (supervisorMappings != null && supervisorMappings.size() > 0) {
					returnInt = FinexaConstant.RETURN_VAL_ERROR_SUPERVISOR_MAPPING;
					flag = 1;
				}
			} catch (Exception e) {
				returnInt = FinexaConstant.RETURN_VAL_ERROR;
			}
			if (flag == 0) {
				// if no mapping Present then delete the user
				try {
					advisorRoleRepository.delete(roleId);
					returnInt = FinexaConstant.RETURN_VAL_SUCCESS;
				} catch (RuntimeException e) {
					throw new RuntimeException(e);
				}
			}
		}
		userDeleteReturnDTO.setRoleId(roleId);
		userDeleteReturnDTO.setReturnStatus(returnInt);
		return userDeleteReturnDTO;

	}

	@Override
	public UserDTO getExistingUser(int userId) throws RuntimeException {
		try {
			User user = userRepository.findOne(userId);
			AdvisorRole advisorRole =user.getAdvisorRole();
			AdvisorUser advisorUser = advisorUserRepository.findByUser(user);
			//AdvisorRole advisorRole = advisorUser.getAdvisorUserRoleMappings().get(0).getAdvisorRole();
			UserDTO userDTO = mapper.map(advisorUser, UserDTO.class);
			userDTO.setAdvisorID(advisorUser.getAdvisorMaster().getId());
			userDTO.setCountryId(advisorUser.getLookupCountry().getId());
			userDTO.setCountryName(advisorUser.getLookupCountry().getName());
			advisorUser.setBirthDate(new Date());
			// userDTO.setBirthDate(advisorUser.getBirthDate());
			userDTO.setGender(advisorUser.getGender());
			userDTO.setLoginUsername(advisorUser.getLoginUsername());
			userDTO.setPhoneCountryCode(advisorUser.getPhoneCountryCode());
			userDTO.setPassword(advisorUser.getLoginPassword());
			userDTO.setSalutation(advisorUser.getSalutation());
			userDTO.setDisributorCode(advisorUser.getAdvisorMaster().getDistributorCode());
			userDTO.setOrganisationName(advisorUser.getAdvisorMaster().getOrgName());
			userDTO.setRoleId(advisorRole.getId());
			userDTO.setUserID(advisorUser.getUser().getId());
			userDTO.setOrganizationFlag(advisorUser.getAdvisorMaster().getOrgFlag());
			userDTO.setEmployeeCode(advisorUser.getEmployeeCode());
			return userDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<UserRoleCreationDTO> getAllUserRoleSuperVisorRole(int userId) throws RuntimeException {
		try {
			List<UserRoleCreationDTO> viewUserRoleCreationDTOList = new ArrayList<>();
			AdvisorMaster advisorMaster = advisorUserRepository.findOne(userId).getAdvisorMaster();
			List<AdvisorRole> viewUserRoleList = advisorMaster.getAdvisorRoles();
			for (AdvisorRole obj : viewUserRoleList) {
				UserRoleCreationDTO user = new UserRoleCreationDTO();
				user.setUserRoleId(obj.getId());
				user.setUserRole(obj.getRoleDescription());
				if (obj.getSupervisorRoleID() != null) {
					user.setSupervisorRoleID(obj.getSupervisorRoleID());
					int superVisorRoleID = user.getSupervisorRoleID();
					LookupRole supervisorRole = lookupRoleRepository.findById((byte)superVisorRoleID);
					user.setSupervisorRole(supervisorRole.getDescription());
				}
				viewUserRoleCreationDTOList.add(user);

			}
			return viewUserRoleCreationDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	/*
	 * @Override public List<UserRoleCreationDTO> getAllUserRoleCreationList(int
	 * userId) {
	 * 
	 * AdvisorMaster advisorMaster =
	 * advisorUserRepository.findOne(userId).getAdvisorMaster(); List<Object[]>
	 * userRoleCreationObjectList = advisorMasterRepository
	 * .getAllUserRoleWithSupervisorRole(advisorMaster.getId()); //
	 * System.out.println(userRoleCreationObjectList); List<UserRoleCreationDTO>
	 * userRoleCreationDTOList = new ArrayList<>(); for (Object[] obj :
	 * userRoleCreationObjectList) { UserRoleCreationDTO user = new
	 * UserRoleCreationDTO(); user.setUserId((int) obj[0]); user.setUserRoleId((int)
	 * obj[2]); user.setUserRole((String) obj[3]); user.setSupervisorRoleID((int)
	 * obj[5]); user.setSupervisorRole((String) obj[6]);
	 * user.setAdvisorId(advisorMaster.getId()); userRoleCreationDTOList.add(user);
	 * } return userRoleCreationDTOList; }
	 */
	
	@Override
	public List<UserRoleCreationDTO> getAllUserRoleCreationList(int userId) {

		AdvisorMaster advisorMaster = advisorUserRepository.findOne(userId).getAdvisorMaster();
		List<Object[]> userRoleCreationObjectList = null;
		FinexaUtil finexaUtil = null;
		finexaUtil = new FinexaUtil();
		userRoleCreationObjectList = finexaUtil.getAllUserRoleWithSupervisorRole(advisorMaster.getId(),advisorMasterRepository);
		/*
		 * List<Object[]> userRoleCreationObjectList = advisorMasterRepository
		 * .getAllUserRoleWithSupervisorRole(advisorMaster.getId());
		 */
		// System.out.println(userRoleCreationObjectList);
		List<UserRoleCreationDTO> userRoleCreationDTOList = new ArrayList<>();
		UserRoleCreationDTO user = null;
		for (Object[] obj : userRoleCreationObjectList) {
			if(obj[5] != null && (int) obj[5] != 0 && obj[4] != null ) {
			 user = new UserRoleCreationDTO();
				/*
				 * System.out.println("_________________________________ ");
				 * System.out.println("setUserId "+(int) obj[0]);
				 * System.out.println("setUsername "+(String) obj[1]);
				 * System.out.println("UserRoleId "+(int) obj[2]);
				 * System.out.println("UserRole "+(String) obj[3]);
				 * System.out.println("Supervisorname "+(String) obj[4]);
				 * System.out.println("SupervisorRoleID "+(int) obj[5]);
				 * System.out.println("SupervisorRole "+(String) obj[6]);
				 * 
				 * System.out.println("_________________________________ ");
				 */
			user.setUserId((int) obj[0]);
			user.setUserName((String) obj[1]);
			user.setUserRoleId((int) obj[2]);
			user.setUserRole((String) obj[3]);
			user.setSupervisorName((String) obj[4]);
			user.setSupervisorRoleID((int) obj[5]);
			user.setSupervisorRole((String) obj[6]);
			user.setAdvisorId(advisorMaster.getId());
			userRoleCreationDTOList.add(user);
			}
		}
		return userRoleCreationDTOList;
	}
	
	@Override
	public List<UserRoleReMappingDTO> getAllUserRoleRemapping(int userId) throws RuntimeException {
		try {
			AdvisorMaster advisorMaster = advisorUserRepository.findOne(userId).getAdvisorMaster();
			List<AdvisorUser> userList = advisorMaster.getAdvisorUsers();
			List<UserRoleReMappingDTO> listDTO = new ArrayList<UserRoleReMappingDTO>();
			// System.out.println("userList "+userList.size());
			for (AdvisorUser obj : userList) {
				UserRoleReMappingDTO userRoleReMappingDTO = new UserRoleReMappingDTO();
				// System.out.println("bj.getAdvisorUserRoleMappings()
				// "+obj.getAdvisorUserRoleMappings().size());
				AdvisorUserRoleMapping roleMapping = obj.getAdvisorUserRoleMappings().get(0);
				AdvisorRole advRole = roleMapping.getAdvisorRole();
				userRoleReMappingDTO.setRoleID(advRole.getId());
				userRoleReMappingDTO.setUserRole(advRole.getRoleDescription());
				userRoleReMappingDTO.setUserName(obj.getFirstName() + " "
						+ (obj.getMiddleName() == null ? " " : obj.getMiddleName()) + " " + obj.getLastName());
				userRoleReMappingDTO.setUserID(obj.getId());
				userRoleReMappingDTO.setEffectiveFromDate(roleMapping.getEffectiveFromDate());
				userRoleReMappingDTO.setAdvisorId(advisorMaster.getId());
				 listDTO.add(userRoleReMappingDTO);
			}
			return listDTO;
		} catch (RuntimeException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserRoleReMappingDTO saveRoleRemapping(UserRoleReMappingDTO userRoleReMappingDTO) throws RuntimeException {

		try {
			
			AdvisorMaster au = advisorUserRepository.findOne(userRoleReMappingDTO.getAdvisorId()).getAdvisorMaster();
			AdvisorUser advisorUser = new AdvisorUser();
			advisorUser.setAdvisorMaster(au);
			AdvisorUserRoleMapping advUserRoleMapping = new AdvisorUserRoleMapping();
			advisorUser = advisorUserRepository.findOne(userRoleReMappingDTO.getUserID());
			advUserRoleMapping.setId(advisorUser.getAdvisorUserRoleMappings().get(0).getId());
			advUserRoleMapping.setAdvisorUser(advisorUser);
			AdvisorRole advRole = advisorRoleRepository.findOne(userRoleReMappingDTO.getRoleID());
			advUserRoleMapping.setAdvisorRole(advRole);
			advUserRoleMapping.setEffectiveFromDate(userRoleReMappingDTO.getEffectiveFromDate());
			advisorRoleMappingRepository.save(advUserRoleMapping);
			
			
			AdvisorUser advUser = advisorUserRepository.findOne(userRoleReMappingDTO.getUserID());
			User user = userRepository.findOne(advUser.getUser().getId());
			System.out.println("user" + user.getId());
			System.out.println("advRole" + advRole.getId());
			user.setAdvisorRole(advRole);
			userRepository.save(user);
			
			
			return userRoleReMappingDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserRoleReMappingDTO updateRoleRemapping(UserRoleReMappingDTO userRoleReMappingDTO) throws RuntimeException {

		try {
			AdvisorMaster au = advisorUserRepository.findOne(userRoleReMappingDTO.getAdvisorId()).getAdvisorMaster();
			AdvisorUser advisorUser = new AdvisorUser();
			advisorUser.setAdvisorMaster(au);
			AdvisorUserRoleMapping advUserRoleMapping = new AdvisorUserRoleMapping();
			advisorUser = advisorUserRepository.findOne(userRoleReMappingDTO.getUserID());
			advUserRoleMapping.setId(advisorUser.getAdvisorUserRoleMappings().get(0).getId());
			advUserRoleMapping.setAdvisorUser(advisorUser);
			AdvisorRole advRole = advisorRoleRepository.findOne(userRoleReMappingDTO.getRoleID());
			advUserRoleMapping.setAdvisorRole(advRole);
			advUserRoleMapping.setEffectiveFromDate(userRoleReMappingDTO.getEffectiveFromDate());
			advisorRoleMappingRepository.save(advUserRoleMapping);
			
			AdvisorUser advUser = advisorUserRepository.findOne(userRoleReMappingDTO.getUserID());
			User user = userRepository.findOne(advUser.getUser().getId());
			System.out.println("user" + user.getId());
			System.out.println("advRole" + advRole.getId());
			user.setAdvisorRole(advRole);
			userRepository.save(user);
			
			return userRoleReMappingDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<UserHierarchyMappingDTO> getOtherSupervisorsWithSameRole(int userId, int roleId)
			throws RuntimeException {
		try {
			AdvisorMaster advisorMaster = advisorUserRepository.findOne(userId).getAdvisorMaster();
			List<Object[]> userRoleCreationObjectList = advisorMasterRepository
					.getOtherSupervisorsWithSameRole(advisorMaster.getId(), roleId);
			// System.out.println(userRoleCreationObjectList);
			List<UserHierarchyMappingDTO> userHierarchyMappingDTOList = new ArrayList<>();
			for (Object[] obj : userRoleCreationObjectList) {
				UserHierarchyMappingDTO user = new UserHierarchyMappingDTO();
				user.setUserId((int) obj[0]);
				user.setSupervisorId((int) obj[0]);
				//System.out.println(""+);
				user.setSupervisorName((String) obj[1]);
				userHierarchyMappingDTOList.add(user);
			}
			for(UserHierarchyMappingDTO dto : userHierarchyMappingDTOList) {
				System.out.println(dto.toString());
			}
			return userHierarchyMappingDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	// for user Management purpose

	/*
	 * @Override public List<UserDTO> getUsersByOrgAndRole(int orgName, int roleId)
	 * throws RuntimeException { try { AdvisorMaster advisorMaster =
	 * advisorRoleRepository.findOne(roleId).getAdvisorMaster(); List<Object[]>
	 * userObjectList =
	 * advisorMasterRepository.getUsersByOrgAndRole(advisorMaster.getId(), roleId);
	 * List<UserDTO> userDTOList = new ArrayList<>(); for (Object[] obj :
	 * userObjectList) { UserDTO user = new UserDTO(); user.setUserName((String)
	 * obj[1]); userDTOList.add(user); } return userDTOList; } catch
	 * (RuntimeException e) { // TODO Auto-generated catch bloc throw new
	 * RuntimeException(e); }
	 * 
	 * }
	 */

	@Override
	public List<AdvisorDTO> getAllUserForSupervisorRole(int supRoleId) throws RuntimeException {

		try {
			AdvisorRole supRole = advisorRoleRepository.findOne(supRoleId);
			List<AdvisorDTO> userList = new ArrayList<AdvisorDTO>();
			List<AdvisorUserRoleMapping> list = advisorUserRoleMappingRepository.findByAdvisorRole(supRole);
			for (AdvisorUserRoleMapping mapping : list) {
				AdvisorUser user = mapping.getAdvisorUser();
				AdvisorDTO userDTO = mapper.map(user, AdvisorDTO.class);
				userList.add(userDTO);
			}
			return userList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	
	
	  @Override public List<UserHierarchyMappingDTO> getAllHierarchiesByAdvisorId(int userId) throws RuntimeException { 
		  try {
	  AdvisorMaster advisorMaster = advisorUserRepository.findOne(userId).getAdvisorMaster(); 
		List<Object[]> userRoleCreationObjectList = null;
		FinexaUtil finexaUtil = null;
		finexaUtil = new FinexaUtil();
		userRoleCreationObjectList = finexaUtil.getAllUserRoleWithSupervisorRole(advisorMaster.getId(),advisorMasterRepository);
		List<UserHierarchyMappingDTO> userHierarchyMappingDTOList = new ArrayList<>(); 
		UserHierarchyMappingDTO user = null;
	 // List<Object[]> userRoleCreationObjectList = advisorMasterRepository.getAllUserRoleWithSupervisorRole(advisorMaster.getId());
	    for (Object[] obj :userRoleCreationObjectList) { 
	         if (obj[5] != null && (int) obj[5] != 0) {
	           user = new UserHierarchyMappingDTO();
	           user.setUserId((int) obj[0]);
	           user.setUserName((String) obj[1]);
	           user.setUserRoleId((int) obj[2]); 
	           user.setUserRole((String) obj[3]);
	           user.setSupervisorName((String) obj[4]); 
	           user.setSupervisorId((int) obj[5]);
	           user.setSupervisorRole((String) obj[6]);
	           userHierarchyMappingDTOList.add(user); 
	       } 
	  } 
	  return userHierarchyMappingDTOList; 
	      }catch (RuntimeException e) { // TOD Auto-generated catch block 
		  throw new RuntimeException(e); 
		  } 
	  }
	


	
	//For access rights and user creation
	@Override
	public List<RoleDTO> getAllExistingRolesForUserCreation(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<RoleDTO> roleDTOList = new ArrayList<>();
			AdvisorMaster advisorMaster = advisorUserRepository.findOne(userId).getAdvisorMaster();
			//System.out.println("advisor master ID: " + advisorMaster.getId() + "\n==================\n");
			AdvisorUserRoleMapping advisorUserRoleMapping = advisorUserRoleMappingRepository
					.findByAdvisorUser(advisorUserRepository.findOne(userId));
			//System.out.println("advisorUserRoleMapping ID: " + advisorUserRoleMapping.getId() + "\n==================\n");
			AdvisorRole loggedAdvisorRole = advisorUserRoleMapping.getAdvisorRole();
			//System.out.println("loggedAdvisorRole ID: " + loggedAdvisorRole.getId() + "\n==================\n");
			if (advisorMaster != null) {
				List<AdvisorRole> advisorRoleList = advisorMaster.getAdvisorRoles();
				for (AdvisorRole obj : advisorRoleList) {
					
					//System.out.println("advisorRoleList ID: " + obj.getId() + "\n==================\n");
					//System.out.println("advisorRoleList supervisor ID: " + obj.getSupervisorRoleID() + "\n==================\n");
					//System.out.println("loggedAdvisorRole supervisor ID: " + loggedAdvisorRole.getSupervisorRoleID() + "\n==================\n");
					
					if(loggedAdvisorRole.getSupervisorRoleID() == null) {
						RoleDTO roleDTO = mapper.map(obj, RoleDTO.class);
						if(roleDTO.getRoleDescription().equalsIgnoreCase("admin")) {
							continue;
						} else {
						roleDTOList.add(roleDTO);
						}
					} else if(obj.getSupervisorRoleID()!= null && obj.getSupervisorRoleID() > loggedAdvisorRole.getSupervisorRoleID()) {
						RoleDTO roleDTO = mapper.map(obj, RoleDTO.class);
						roleDTOList.add(roleDTO);
					}
				}
			}
			return roleDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	//For user edit
		@Override
		public RoleDTO getExistingRoleForUserEdit(int roleId) throws RuntimeException {
			// TODO Auto-generated method stub
			try {
				RoleDTO roleDTO;
				AdvisorRole loggedAdvisorRole = advisorRoleRepository.findOne(roleId);
				roleDTO = mapper.map(loggedAdvisorRole, RoleDTO.class);
				return roleDTO;
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}
	
	//For edit user creation

	@Override
	public List<RoleDTO> getAllExistingRoles(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<RoleDTO> roleDTOList = new ArrayList<>();
			AdvisorMaster advisorMaster = advisorUserRepository.findOne(userId).getAdvisorMaster();
			List<AdvisorRole> advisorRoleList= advisorMaster.getAdvisorRoles();
			//System.out.println("advisor master ID: " + advisorMaster.getId() + "\n==================\n");
			
			
			AdvisorUserRoleMapping advisorUserRoleMapping = advisorUserRoleMappingRepository
					.findByAdvisorUser(advisorUserRepository.findOne(userId));
			 
			//System.out.println("advisorUserRoleMapping ID: " + advisorUserRoleMapping.getId() + "\n==================\n");
			AdvisorRole loggedAdvisorRole = advisorUserRoleMapping.getAdvisorRole();
			//System.out.println("loggedAdvisorRole ID: " + loggedAdvisorRole.getId() + "\n==================\n");
			if (advisorMaster != null) {
				for (AdvisorRole obj : advisorRoleList) {
					
					//System.out.println("advisorRoleList ID: " + obj.getId() + "\n==================\n");
					//System.out.println("advisorRoleList supervisor ID: " + obj.getSupervisorRoleID() + "\n==================\n");
					//System.out.println("loggedAdvisorRole supervisor ID: " + loggedAdvisorRole.getSupervisorRoleID() + "\n==================\n");
				
					if(loggedAdvisorRole.getSupervisorRoleID() == null) {
						RoleDTO roleDTO = mapper.map(obj, RoleDTO.class);
						if(roleDTO.getRoleDescription().equalsIgnoreCase("Admin")) {
							roleDTO.setRoleDescription(obj.getRoleDescription());
							roleDTOList.add(roleDTO);
							continue;
						} else {
						roleDTOList.add(roleDTO);
						}
					} else if(obj.getSupervisorRoleID()!= null && obj.getSupervisorRoleID() > loggedAdvisorRole.getSupervisorRoleID()) {
						System.out.println("45435345 ");
						RoleDTO roleDTO = mapper.map(obj, RoleDTO.class);
						roleDTOList.add(roleDTO);
					}
				}
			}
			return roleDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<FinexaBusinessModuleDTO> getAllBusinessModules() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<FinexaBusinessModuleDTO> businessModuleDTOList = new ArrayList<>();
			List<FinexaBusinessModule> finexaBusinessModuleList = businessModuleRepo.findAll();
			for (FinexaBusinessModule obj : finexaBusinessModuleList) {
				FinexaBusinessModuleDTO finexaBusinessModuleDTO = mapper.map(obj, FinexaBusinessModuleDTO.class);
				businessModuleDTOList.add(finexaBusinessModuleDTO);
			}
			return businessModuleDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<FinexaBusinessSubmoduleDTO> getAllBusinessSubModules(int moduleId, int roleId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<FinexaBusinessSubmoduleDTO> businessSubModuleDTOList = new ArrayList<>();
			FinexaBusinessModule finexaBusinessModule = businessModuleRepo.findOne(0);
			List<FinexaBusinessSubmodule> finexaBusinessModuleList = finexaBusinessModule.getFinexaBusinessSubmodules();
			for (FinexaBusinessSubmodule obj : finexaBusinessModuleList) {
				FinexaBusinessSubmoduleDTO finexaBusinessSubModuleDTO = mapper.map(obj,
						FinexaBusinessSubmoduleDTO.class);
				finexaBusinessSubModuleDTO.setFinexaBusinessModule(
						mapper.map(businessModuleRepo.findOne(moduleId), FinexaBusinessModuleDTO.class));
				AdvisorRole advRole = advisorRoleRepository.findOne(roleId);
				List<AdvisorRoleSubmoduleMapping> advisorUserRoleMappingList = advRole
						.getAdvisorRoleSubmoduleMappings();
				List<AdvisorRoleSubmoduleMappingDTO> advisorRoleSubmoduleMappingDTOList = new ArrayList<AdvisorRoleSubmoduleMappingDTO>();
				for (AdvisorRoleSubmoduleMapping obj1 : advisorUserRoleMappingList) {
					AdvisorRoleSubmoduleMappingDTO advisorRoleSubmoduleMappingDTO = mapper.map(obj1,
							AdvisorRoleSubmoduleMappingDTO.class);
					advisorRoleSubmoduleMappingDTO.setRoleID(obj1.getAdvisorRole().getId());
					advisorRoleSubmoduleMappingDTO.setSubModuleID(obj1.getFinexaBusinessSubmodule().getId());
					advisorRoleSubmoduleMappingDTOList.add(advisorRoleSubmoduleMappingDTO);
				}
				finexaBusinessSubModuleDTO.setAdvisorRoleSubmoduleMappings(advisorRoleSubmoduleMappingDTOList);
				businessSubModuleDTOList.add(finexaBusinessSubModuleDTO);
			}
			return businessSubModuleDTOList;
		} catch (RuntimeException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserRoleCreationDTO saveRole(UserRoleCreationDTO userRoleCreationDTO) throws RuntimeException {
		try {
			AdvisorUser advUser = advisorUserRepository.findOne(userRoleCreationDTO.getAdvisorId());
			AdvisorMaster au = advUser.getAdvisorMaster();
			/*
			 * AdvisorRole advisorRole =
			 * advisorRoleRepository.findOne(userRoleCreationDTO.getUserRoleId()); if
			 * (advisorRole == null) { advisorRole = new AdvisorRole(); }
			 */
			AdvisorRole advisorRole = new AdvisorRole();
			advisorRole.setAdvisorMaster(au);
			advisorRole.setRoleDescription(userRoleCreationDTO.getUserRole());
			if(userRoleCreationDTO.getSupervisorRole().equals("Not Applicable")) {
			    advisorRole.setSupervisorRoleID(null);
			}else {
				LookupRole lookupRole = lookupRoleRepository.findByDescription(userRoleCreationDTO.getSupervisorRole());
				//System.out.println("lookupRole" +lookupRole);
				advisorRole.setSupervisorRoleID((int)lookupRole.getId());
			}
			
			// AdvisorUserSupervisorMapping advisorUserSupervisorMapping = new
			// AdvisorUserSupervisorMapping();
			advisorRole = advisorRoleRepository.save(advisorRole);
			/*
			 * int superId = advisorRole.getId(); // role is not reporting to any supervisor
			 * if (userRoleCreationDTO.getSupervisorRoleID() == 0) {
			 * advisorRole.setSupervisorRoleID(superId); advisorRole =
			 * advisorRoleRepository.save(advisorRole); }
			 */
			 
			return userRoleCreationDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserRoleCreationDTO updateRole(UserRoleCreationDTO userRoleCreationDTO) throws RuntimeException {
		try {
			AdvisorUser advUser = advisorUserRepository.findOne(userRoleCreationDTO.getAdvisorId());
			AdvisorMaster au = advUser.getAdvisorMaster();
			/*
			 * AdvisorRole advisorRole =
			 * advisorRoleRepository.findOne(userRoleCreationDTO.getUserRoleId()); if
			 * (advisorRole == null) { advisorRole = new AdvisorRole(); }
			 */
			
			LookupRole lookupRole = lookupRoleRepository.findByDescription(userRoleCreationDTO.getSupervisorRole());
			System.out.println("lookupRole" +lookupRole);
			AdvisorRole advisorRole = new AdvisorRole();
			advisorRole.setAdvisorMaster(au);
			advisorRole.setRoleDescription(userRoleCreationDTO.getUserRole());
			if(userRoleCreationDTO.getSupervisorRole().equals("Not Applicable")) {
			advisorRole.setSupervisorRoleID(0);
			}
			advisorRole.setSupervisorRoleID((int)lookupRole.getId());
			// AdvisorUserSupervisorMapping advisorUserSupervisorMapping = new
			// AdvisorUserSupervisorMapping();
			advisorRole = advisorRoleRepository.save(advisorRole);
			/*
			 * int superId = advisorRole.getId(); // role is not reporting to any supervisor
			 * if (userRoleCreationDTO.getSupervisorRoleID() == 0) {
			 * advisorRole.setSupervisorRoleID(superId); advisorRole =
			 * advisorRoleRepository.save(advisorRole); }
			 */
			 
			return userRoleCreationDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(rollbackOn = CustomFinexaException.class)
	public void saveRoleSubModuleMapping(AdvisorRoleSubmoduleMappingDTO advisorRoleSubmoduleMappingDTO)
			throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		try {
			AdvisorRoleSubmoduleMapping advisorRoleSubmoduleMapping = advisorRoleSubmoduleMappingRepository
					.checkIfExists(advisorRoleSubmoduleMappingDTO.getRoleID(),
							advisorRoleSubmoduleMappingDTO.getSubModuleID());
			if (advisorRoleSubmoduleMapping != null) {
				// only update
				try {
					AdvisorRoleSubmoduleMapping obj = new AdvisorRoleSubmoduleMapping();
					obj.setId(advisorRoleSubmoduleMapping.getId());
					AdvisorRole advRole = advisorRoleRepository.findOne(advisorRoleSubmoduleMappingDTO.getRoleID());
					FinexaBusinessSubmodule finexaBusinessSubmodule = businessSubModuleRepo
							.findOne(advisorRoleSubmoduleMappingDTO.getSubModuleID());
					obj.setAdvisorRole(advRole);
					obj.setFinexaBusinessSubmodule(finexaBusinessSubmodule);
					if (advisorRoleSubmoduleMappingDTO.getViewAllowedFlag() != null) {
						obj.setViewAllowedFlag(advisorRoleSubmoduleMappingDTO.getViewAllowedFlag());
					} else {
						obj.setViewAllowedFlag(advisorRoleSubmoduleMapping.getViewAllowedFlag());
					}
					if (advisorRoleSubmoduleMappingDTO.getAddAllowedFlag() != null) {
						obj.setAddAllowedFlag(advisorRoleSubmoduleMappingDTO.getAddAllowedFlag());
					} else {
						obj.setAddAllowedFlag(advisorRoleSubmoduleMapping.getAddAllowedFlag());
					}
					if (advisorRoleSubmoduleMappingDTO.getEditAllowedFlag() != null) {
						obj.setEditAllowedFlag(advisorRoleSubmoduleMappingDTO.getEditAllowedFlag());
					} else {
						obj.setEditAllowedFlag(advisorRoleSubmoduleMapping.getEditAllowedFlag());
					}
					if (advisorRoleSubmoduleMappingDTO.getDeleteAllowedFlag() != null) {
						obj.setDeleteAllowedFlag(advisorRoleSubmoduleMappingDTO.getDeleteAllowedFlag());
					} else {
						obj.setDeleteAllowedFlag(advisorRoleSubmoduleMapping.getDeleteAllowedFlag());
					}
					advisorRoleSubmoduleMappingRepository.save(obj);
				} catch (RuntimeException e) {
					FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
							.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_ACCESS_RIGHTS);
					FinexaExceptionHandling exception = finexaExceptionHandlingRepository
							.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
									FinexaConstant.MY_BUSINESS_ACCESS_RIGHTS_UPDATE_ERROR);
					throw new CustomFinexaException(FinexaConstant.MY_BUSINESS_ACCESS_RIGHTS_MODULE,
							FinexaConstant.MY_BUSINESS_ACCESS_RIGHTS_UPDATE_ERROR,
							exception != null ? exception.getErrorMessage() : "");
				}

			} else {
				// create new Entry
				try {
					AdvisorRoleSubmoduleMapping obj = new AdvisorRoleSubmoduleMapping();
					AdvisorRole advRole = advisorRoleRepository.findOne(advisorRoleSubmoduleMappingDTO.getRoleID());
					FinexaBusinessSubmodule finexaBusinessSubmodule = businessSubModuleRepo
							.findOne(advisorRoleSubmoduleMappingDTO.getSubModuleID());
					obj.setAdvisorRole(advRole);
					obj.setFinexaBusinessSubmodule(finexaBusinessSubmodule);
					if (advisorRoleSubmoduleMappingDTO.getViewAllowedFlag() != null) {
						obj.setViewAllowedFlag(advisorRoleSubmoduleMappingDTO.getViewAllowedFlag());
					} else {
						obj.setViewAllowedFlag("N");
					}
					if (advisorRoleSubmoduleMappingDTO.getAddAllowedFlag() != null) {
						obj.setAddAllowedFlag(advisorRoleSubmoduleMappingDTO.getAddAllowedFlag());
					} else {
						obj.setAddAllowedFlag("N");
					}
					if (advisorRoleSubmoduleMappingDTO.getEditAllowedFlag() != null) {
						obj.setEditAllowedFlag(advisorRoleSubmoduleMappingDTO.getEditAllowedFlag());
					} else {
						obj.setEditAllowedFlag("N");
					}
					if (advisorRoleSubmoduleMappingDTO.getDeleteAllowedFlag() != null) {
						obj.setDeleteAllowedFlag(advisorRoleSubmoduleMappingDTO.getDeleteAllowedFlag());
					} else {
						obj.setDeleteAllowedFlag("N");
					}
					advisorRoleSubmoduleMappingRepository.save(obj);
				} catch (RuntimeException e) {
					FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
							.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_ACCESS_RIGHTS);
					FinexaExceptionHandling exception = finexaExceptionHandlingRepository
							.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
									FinexaConstant.MY_BUSINESS_ACCESS_RIGHTS_ADD_ERROR);
					throw new CustomFinexaException(FinexaConstant.MY_BUSINESS_ACCESS_RIGHTS_MODULE,
							FinexaConstant.MY_BUSINESS_ACCESS_RIGHTS_ADD_ERROR,
							exception != null ? exception.getErrorMessage() : "");
				}

			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	
	@Override
	public int delete(int advisorId) throws RuntimeException {

		try {
			AdvisorUser advisorUser = advisorUserRepository.findOne(advisorId);
		List<AdvisorUserSupervisorMapping> advisorUserSupervisorMappingList =advisorUser.getAdvisorUserSupervisorMappings1();
		if(advisorUserSupervisorMappingList != null && !advisorUserSupervisorMappingList.isEmpty()) {
		int	id = advisorUserSupervisorMappingList.get(0).getId();
			advisorUserSupervisorMappingRepository.delete(id);
		}
			
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}
	
	@Override
	public boolean checkEmailExists(String email, int userId) throws RuntimeException {
		// List<AdvisorUser> listUser = advisorUserRepository.findAll();
		try {
			boolean status;
			log.debug("email: " + email);
			log.debug("userId: " + userId);
			ClientContact clientContact = clientContactRepo.findByEmailID(email);
			if (clientContact == null) {
				List<AdvisorUser> listUser = advisorUserRepository.findByEmailIDAndIdNot(email, 
						advisorUserRepository.findByUser(userRepository.findOne(userId)).getId());
				log.debug("List size : " + listUser.size());
				if (listUser.size() > 0) {
					status = false;
				} else {
					status = true;
				}
			} else {
				status = false;
			}
			return status;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean checkMobileExists(BigInteger mobile, int userId) throws RuntimeException {
		try {
			/*
			 * List<AdvisorUser> listUserMobile = advisorUserRepository.findAll();
			 * 
			 * boolean flag = true; for (AdvisorUser advisorUser : listUserMobile) { if
			 * (advisorUser.getId() !=
			 * advisorUserRepository.findByUser(userRepository.findOne(userId)).getId()) {
			 * if (advisorUser.getPhoneNo().equals(mobile)) { flag = false; break; } } }
			 * 
			 * return flag;
			 */
			
			
			boolean status;
			
			List<AdvisorUser> listUser = advisorUserRepository.findByPhoneNoAndIdNot(mobile,
					advisorUserRepository.findByUser(userRepository.findOne(userId)).getId());
			log.debug("List size : " + listUser.size());
			if (listUser.size() > 0) {
				status = false;
			} else {
				status = true;
			}
			
			return status;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean checkEmployeeCodeExists(String employeeCd, int userId) throws RuntimeException {
		try {
			List<AdvisorUser> listEmployeeCode = advisorUserRepository.findAll();

			boolean flag = true;
			for (AdvisorUser advisorUser : listEmployeeCode) {
				if (advisorUser.getId() != userId) {
					if (advisorUser.getEmployeeCode().equals(employeeCd)) {
						flag = false;
						break;
					}
				}
			}
			return flag;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public String checkRoleExists(String role, int advisorId) throws RuntimeException {
		try {
			List<AdvisorRole> listRoles = advisorRoleRepository.findAll();

			int flag = 0;
			String msg = "";
			for (AdvisorRole advisorRole : listRoles) {
				if (advisorRole.getAdvisorMaster().getId() != advisorId) {
					if (advisorRole.getRoleDescription().equals(role)) {
						flag = 1;
						break;
					}
				}
			}
			if (flag == 1) {
				msg = "role exists";
			}

			return msg;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	/*
	 * @Override public boolean checkUniqueRole(String role) throws RuntimeException
	 * { // TODO Auto-generated method stub try { AdvisorRole ar =
	 * advisorRoleRepository.findByRoleDescription(role); return (ar != null) ?
	 * false : true; } catch (RuntimeException e) { // TODO Auto-generated catch
	 * block throw new RuntimeException(e); } }
	 */

	@Override
	public boolean checkUniqueEmployeeCode(String employeeCd) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			AdvisorUser EC = advisorUserRepository.findByEmployeeCode(employeeCd);
			return (EC != null) ? false : true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean checkUniqueEmail(String email) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			boolean status;
			AdvisorUser au = advisorUserRepository.findByEmailID(email);
			if (au != null) {
				status = false;
			} else {
				ClientContact clientContact = clientContactRepo.findByEmailID(email);
				if (clientContact != null) {
					status = false;
				} else {
					status = true;
				}
			}
			//return (au != null) ? false : true;
			return status;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean checkUniqueMobile(BigInteger mobile) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			AdvisorUser CHKMP = advisorUserRepository.findByPhoneNo(mobile);
			return (CHKMP != null) ? false : true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	/*********************** for manage password ****************************/
	@Override
	public List<ManagePasswordDTO> getAllUsersToManagePassword(int userId) throws RuntimeException {
		try {
			List<ManagePasswordDTO> viewManagePasswordDTOList = new ArrayList<>();
			User user = advisorUserRepository.findOne(userId).getUser();
			AdvisorMaster advisorMaster = user.getAdvisorMaster();
			List<AdvisorUser> viewUserList = advisorMaster.getAdvisorUsers();
			if (viewUserList != null && !viewUserList.isEmpty()) {
				for (AdvisorUser obj : viewUserList) {
					ManagePasswordDTO managePasswordDTO = new ManagePasswordDTO();
					AdvisorUserRoleMapping userRoleMapping = new AdvisorUserRoleMapping();
					if (obj.getAdvisorUserRoleMappings() != null && !obj.getAdvisorUserRoleMappings().isEmpty()) {
						userRoleMapping = obj.getAdvisorUserRoleMappings().get(0);
					}
					managePasswordDTO.setId(obj.getId());
					managePasswordDTO.setRoleId(userRoleMapping.getAdvisorRole().getId());
					managePasswordDTO.setUserName(obj.getFirstName() + " "
							+ (obj.getMiddleName() == null ? " " : obj.getMiddleName()) + " " + obj.getLastName());
					managePasswordDTO.setRoleDescription(userRoleMapping.getAdvisorRole().getRoleDescription());
					managePasswordDTO.setUserId(obj.getLoginUsername());
					managePasswordDTO.setUserID(obj.getUser().getId());
					viewManagePasswordDTOList.add(managePasswordDTO);
				}
			}
			return viewManagePasswordDTOList;
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserHierarchyMappingDTO saveUserHierarchy(UserHierarchyMappingDTO userHierarchyMappingDTO)
			throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			/*
			 * AdvisorUserSupervisorMapping advisorUserSupervisorMapping =
			 * advisorUserSupervisorMappingRepository
			 * .getAllSupervisorWithUserId(userHierarchyMappingDTO.getUserId()).get(0);
			 */

//			if (advisorUserSupervisorMapping == null) {
			AdvisorUserSupervisorMapping advisorUserSupervisorMapping = new AdvisorUserSupervisorMapping();
			// }

			advisorUserSupervisorMapping
					.setAdvisorUser1(advisorUserRepository.findOne(userHierarchyMappingDTO.getUserId()));
			/*
			 * advisorUserSupervisorMapping
			 * .setAdvisorUser2(advisorUserRepository.findOne(userHierarchyMappingDTO.
			 * getSupervisorId()));
			 */
			User user = userRepository.findOne(userHierarchyMappingDTO. getSupervisorId());
			advisorUserSupervisorMapping.setAdvisorUser2(user.getAdvisorUser());
			
			advisorUserSupervisorMapping.setEffectiveFromDate(userHierarchyMappingDTO.getEffectiveDate());
			advisorUserSupervisorMapping = advisorUserSupervisorMappingRepository.save(advisorUserSupervisorMapping);
			userHierarchyMappingDTO = mapper.map(advisorUserSupervisorMapping, UserHierarchyMappingDTO.class);
			return userHierarchyMappingDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserHierarchyMappingDTO updateUserHierarchy(UserHierarchyMappingDTO userHierarchyMappingDTO)
			throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			AdvisorUserSupervisorMapping advisorUserSupervisorMapping = advisorUserSupervisorMappingRepository
					.getAllSupervisorWithUserId(userHierarchyMappingDTO.getUserId()).get(0);

			if (advisorUserSupervisorMapping == null) {
				advisorUserSupervisorMapping = new AdvisorUserSupervisorMapping();
			}

			advisorUserSupervisorMapping
					.setAdvisorUser1(advisorUserRepository.findOne(userHierarchyMappingDTO.getUserId()));
			AdvisorUser user = advisorUserRepository.findOne(userHierarchyMappingDTO.getSupervisorId());
			advisorUserSupervisorMapping.setAdvisorUser2(user);
			advisorUserSupervisorMapping.setEffectiveFromDate(userHierarchyMappingDTO.getEffectiveDate());
			advisorUserSupervisorMapping = advisorUserSupervisorMappingRepository.save(advisorUserSupervisorMapping);
			userHierarchyMappingDTO = mapper.map(advisorUserSupervisorMapping, UserHierarchyMappingDTO.class);
			return userHierarchyMappingDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserHierarchyMappingDTO getUserHierarchy(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			UserHierarchyMappingDTO userHierarchyMappingDTO = new UserHierarchyMappingDTO();
			AdvisorUserSupervisorMapping advisorUserSupervisorMapping = advisorUserSupervisorMappingRepository
					.getAllSupervisorWithUserId(userId).get(0);

			userHierarchyMappingDTO.setUserId(advisorUserSupervisorMapping.getAdvisorUser1().getId());
			userHierarchyMappingDTO.setUserName(advisorUserSupervisorMapping.getAdvisorUser1().getFirstName());
			userHierarchyMappingDTO.setSupervisorId(advisorUserSupervisorMapping.getAdvisorUser2().getId());
			userHierarchyMappingDTO.setSupervisorName(advisorUserSupervisorMapping.getAdvisorUser2().getFirstName());
			AdvisorRole userRole = advisorUserRepository.findOne(userId).getAdvisorUserRoleMappings().get(0)
					.getAdvisorRole();
			AdvisorUser advUser = advisorUserRepository
					.findOne(advisorUserSupervisorMapping.getAdvisorUser2().getId());
			User user = advUser.getUser();
			userHierarchyMappingDTO.setUserRole(userRole.getRoleDescription());
			userHierarchyMappingDTO.setUserRoleId(userRole.getId());
			userHierarchyMappingDTO.setSupervisorRole(user.getAdvisorRole().getRoleDescription());
			userHierarchyMappingDTO.setSupervisorRoleID(user.getAdvisorRole().getId());

			String date = formatter.format(advisorUserSupervisorMapping.getEffectiveFromDate());
			try {
				userHierarchyMappingDTO.setEffectiveDate(formatter.parse(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return userHierarchyMappingDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public UserHierarchyMappingDTO getUserSpecific(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			UserHierarchyMappingDTO userHierarchyMappingDTO = new UserHierarchyMappingDTO();
			AdvisorUserSupervisorMapping advisorUserSupervisorMapping =advisorUserSupervisorMappingRepository.findByAdvisorUser1(userId);
			AdvisorUser advisor = advisorUserSupervisorMapping.getAdvisorUser2();
			userHierarchyMappingDTO.setSupervisorId(advisor.getId());
			userHierarchyMappingDTO.setUserName(advisor.getFirstName() + " "
					+ (advisor.getMiddleName() == null ? " " : advisor.getMiddleName()) + " " + advisor.getLastName());
			return userHierarchyMappingDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	/*************************** Client Record *******************************/
	private void addMaritalStatusHeader(WritableSheet sheet) throws RowsExceededException, WriteException {

		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "description"));

	}

	private void addMaritalStatusData(WritableSheet sheet) throws RowsExceededException, WriteException {

		int i = 1;
		for (LookupMaritalStatus obj : maritalStatusRepository.findAll()) {
			sheet.addCell(new Label(0, i, "" + obj.getId()));
			sheet.addCell(new Label(1, i, obj.getDescription()));
			i++;
		}

	}

	private void addEducationalQualificationsHeader(WritableSheet sheet) throws RowsExceededException, WriteException {

		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "description"));

	}

	private void addEducationalQualificationsData(WritableSheet sheet) throws RowsExceededException, WriteException {

		int i = 1;
		for (LookupEducationalQualification obj : eduQualRepo.findAll()) {
			sheet.addCell(new Label(0, i, "" + obj.getId()));
			sheet.addCell(new Label(1, i, obj.getDescription()));
			i++;
		}

	}

	private void addEmploymentTypeHeader(WritableSheet sheet) throws RowsExceededException, WriteException {

		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "description"));

	}

	private void addEmploymentTypeData(WritableSheet sheet) throws RowsExceededException, WriteException {

		int i = 1;
		for (LookupEmploymentType obj : empTypeRepo.findAll()) {
			sheet.addCell(new Label(0, i, "" + obj.getId()));
			sheet.addCell(new Label(1, i, obj.getDescription()));
			i++;
		}
	}

	private void addResidentTypeHeader(WritableSheet sheet) throws RowsExceededException, WriteException {

		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "description"));

	}

	private void addResidentTypeData(WritableSheet sheet) throws RowsExceededException, WriteException {

		int i = 1;
		for (LookupResidentType obj : resTyprRepo.findAll()) {
			sheet.addCell(new Label(0, i, "" + obj.getId()));
			sheet.addCell(new Label(1, i, obj.getDescription()));
			i++;
		}
	}

	private void addCountryHeader(WritableSheet sheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		sheet.addCell(new Label(0, 0, "ID"));
		sheet.addCell(new Label(1, 0, "Country Name"));
	}

	private void addCountryData(WritableSheet sheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		int i = 1;
		for (LookupCountry country : lookupCountryRepository.findAll()) {
			sheet.addCell(new Label(0, i, Integer.toString(country.getId())));
			sheet.addCell(new Label(1, i, country.getName()));
			i++;
		}

	}

	@Override
	public void downloadClientTemplateCSVForImport(HttpServletResponse response) throws IOException, RuntimeException {
		try {
			String csvFileName = "importClientTemplate.csv";

			response.setContentType("text/csv");

			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
			response.setHeader(headerKey, headerValue);

			// uses the Super CSV API to generate CSV data from the model data
			ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

			String[] header = { "First Name*", "Middle Name", "Last Name*", "Birth Date*", "Gender*", "Pan*", "Adhaar*",
					"Marital Status*",

					"Other Marital Status", "Educational Qualification", "Other Educational Qualification",
					"Employment Type", "Other Employment Type", "Organization Name", "Current Designation",
					"Resident Type*", "Other Resident Type", "Country Of Residence*",

					"Retired(Y/N)*", "Retirement Age*", "Email Address*", "Alternate Email Address", "Mobile Number*",
					"Emergency Number*", "Address Line 1*", "Address Line 2", "Address Line 3", "City*", "State*",
					"Pin Code*", "Country*", "Address Type*", "Annual Income", "Tobacco User (Y/N)", "BMI is between 18 and 25 (Y/N)",
					"History of Critical Illness (Y/N)", "Normal Blood Pressure (Y/N)"};

			csvWriter.writeHeader(header);

			csvWriter.close();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public WritableWorkbook downloadClientTemplateExcel(HttpServletResponse response) {
		String fileName = "ClientUserUploadingTemplate.xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addExcelOutputHeader(excelOutputsheet);

			WritableSheet maritalStatus = writableWorkbook.createSheet("Marital Status", 1);
			addMaritalStatusHeader(maritalStatus);
			addMaritalStatusData(maritalStatus);

			WritableSheet eduQualf = writableWorkbook.createSheet("Educational Qualifications", 2);
			addEducationalQualificationsHeader(eduQualf);
			addEducationalQualificationsData(eduQualf);

			WritableSheet empType = writableWorkbook.createSheet("Employment Type", 3);
			addEmploymentTypeHeader(empType);
			addEmploymentTypeData(empType);

			WritableSheet resType = writableWorkbook.createSheet("Resident Type", 4);
			addResidentTypeHeader(resType);
			addResidentTypeData(resType);

			WritableSheet country = writableWorkbook.createSheet("Country", 5);
			addCountryHeader(country);
			addCountryData(country);

			writableWorkbook.write();
			writableWorkbook.close();

		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}

		return writableWorkbook;
	}

	private void addExcelOutputHeader(WritableSheet sheet) throws RowsExceededException, WriteException {
		// create header row
		// sheet.addCell(new Label(0, 0, "Salutation*"));
		sheet.addCell(new Label(0, 0, "First Name*"));
		sheet.addCell(new Label(1, 0, "Middle Name"));
		sheet.addCell(new Label(2, 0, "Last Name*"));
		sheet.addCell(new Label(3, 0, "Birth Date*"));
		sheet.addCell(new Label(4, 0, "Gender*"));
		sheet.addCell(new Label(5, 0, "Pan*"));
		sheet.addCell(new Label(6, 0, "Adhaar*"));
		sheet.addCell(new Label(7, 0, "Marital Status*"));
		sheet.addCell(new Label(8, 0, "Other Marital Status"));
		sheet.addCell(new Label(9, 0, "Educational Qualification"));
		sheet.addCell(new Label(10, 0, "Other Educational Qualification"));
		sheet.addCell(new Label(11, 0, "Employment Type"));
		sheet.addCell(new Label(12, 0, "Other Employment Type"));
		sheet.addCell(new Label(13, 0, "Organization Name"));
		sheet.addCell(new Label(14, 0, "Current Designation"));
		sheet.addCell(new Label(15, 0, "Resident Type*"));
		sheet.addCell(new Label(16, 0, "Other Resident Type"));
		sheet.addCell(new Label(17, 0, "Country Of Residence*"));
		sheet.addCell(new Label(18, 0, "Retired(Y/N)*"));
		sheet.addCell(new Label(19, 0, "Retirement Age*"));
		sheet.addCell(new Label(20, 0, "Email Address*"));
		sheet.addCell(new Label(21, 0, "Alternate Email Address"));
		sheet.addCell(new Label(22, 0, "Mobile Number*"));
		sheet.addCell(new Label(23, 0, "Emergency Number*"));
		sheet.addCell(new Label(24, 0, "Address Line 1*"));
		sheet.addCell(new Label(25, 0, "Address Line 2"));
		sheet.addCell(new Label(26, 0, "Address Line 3"));
		sheet.addCell(new Label(27, 0, "City*"));
		sheet.addCell(new Label(28, 0, "State*"));
		sheet.addCell(new Label(29, 0, "Pin Code*"));
		sheet.addCell(new Label(30, 0, "Country*"));
		sheet.addCell(new Label(31, 0, "Address Type*"));
		sheet.addCell(new Label(32, 0, "Anuual Income"));
		sheet.addCell(new Label(33, 0, "Tobacco User (Y/N)"));
		sheet.addCell(new Label(34, 0, "BMI is between 18 and 25 (Y/N)"));
		sheet.addCell(new Label(35, 0, "History of Critical Illness (Y/N)"));
		sheet.addCell(new Label(36, 0, "Normal Blood Pressure (Y/N)"));

	}

	@Override
	public List<ClientExportCsvDTO> exportClientByUser(FormDataDTO formDataDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<ClientExportCsvDTO> clientExportCsvDTOList = new ArrayList<>();
			AdvisorUser advisorUser = advisorUserRepository.findOne(formDataDTO.getAdvUserId());
			List<ClientMaster> clientMasterList = clientMasterRepo.findByAdvisorUserAndActiveFlag(advisorUser, "Y");
			log.debug("size of clientMasterList: " + clientMasterList.size());
			for (ClientMaster obj : clientMasterList) {
				ClientExportCsvDTO clientExportCsvDTO = mapper.map(obj, ClientExportCsvDTO.class);
				log.debug("size of contacts: " + obj.getClientContacts().size());
				// clientExportCsvDTO.setUserName(advisorUser.getFirstName() +
				// advisorUser.getLastName());
				LookupMaritalStatus maritalStatus = (obj.getLookupMaritalStatus().getId() > 0)
						? maritalStatusRepository.findOne(obj.getLookupMaritalStatus().getId())
						: null;
				clientExportCsvDTO.setMaritalStatus(maritalStatus.getDescription());
				if (obj.getLookupEducationalQualification() != null) {
					LookupEducationalQualification eduQualf = (obj.getLookupEducationalQualification().getId() > 0)
							? eduQualRepo.findOne(obj.getLookupEducationalQualification().getId())
							: null;
					clientExportCsvDTO.setEducationalQualification(eduQualf.getDescription());
				}
				if (obj.getLookupEmploymentType() != null) {
					LookupEmploymentType empType = (obj.getLookupEmploymentType().getId() > 0)
							? empTypeRepo.findOne(obj.getLookupEmploymentType().getId())
							: null;
					clientExportCsvDTO.setEmploymentType(empType.getDescription());
				}

				LookupResidentType resType = (obj.getLookupResidentType().getId() > 0)
						? resTyprRepo.findOne(obj.getLookupResidentType().getId())
						: null;
				clientExportCsvDTO.setResidentType(resType.getDescription());

				LookupCountry countryOfRes = (obj.getLookupCountry().getId() > 0)
						? countryRepo.findOne(obj.getLookupCountry().getId())
						: null;
				clientExportCsvDTO.setCountryOfResidence(countryOfRes.getName());

				clientExportCsvDTO.setOrganisationName(obj.getOrgName());
				clientExportCsvDTO.setCurrentDesignation(obj.getCurrDesignation());
				
				// show tobacco and other flags
				List<ClientFamilyMember> clientFamilyMembers = obj.getClientFamilyMembers();
				for (ClientFamilyMember ob : clientFamilyMembers) {
					if (ob.getLookupRelation() == lookupRelationshipRepository.findById((byte) 0)) {
						clientExportCsvDTO.setIsTobaccoUser(ob.getIsTobaccoUser());
						clientExportCsvDTO.setIsNormalBP(ob.getHasNormalBP());
						clientExportCsvDTO.setIsNormalBMI(ob.getIsProperBMI());
						clientExportCsvDTO.setHistoryOfCriticalIllness(ob.getHasDiseaseHistory());
					}
				}
				
				// show income
				List<ClientFamilyIncome> clientFamilyIncomes = obj.getClientFamilyIncomes();
				for (ClientFamilyIncome ob : clientFamilyIncomes) {
					if (ob.getClientFamilyMember().getLookupRelation() == lookupRelationshipRepository.findById((byte) 0)) {
						BigDecimal annualIncome = ob.getIncomeAmount();
						clientExportCsvDTO.setAnnualIncome(clientExportCsvDTO.getAnnualIncome() + (annualIncome.floatValue() * 12));
					}
				}
				
				if (obj.getClientContacts().size() > 0) {
					ClientContact contact = obj.getClientContacts().get(0);
					if (contact.getOfficeAddressLine1() != null && !contact.getOfficeAddressLine1().isEmpty()) {
						clientExportCsvDTO.setAddressLine1(contact.getOfficeAddressLine1());
						clientExportCsvDTO.setAddressLine2(contact.getOfficeAddressLine2());
						clientExportCsvDTO.setAddressLine3(contact.getOfficeAddressLine3());
						clientExportCsvDTO.setEmailAddress(contact.getEmailID());
						clientExportCsvDTO.setAlternateEmailAddress(contact.getAlternateEmail());
						clientExportCsvDTO.setMobileNumber(contact.getMobile());
						clientExportCsvDTO.setEmergencyContact(contact.getEmergencyContact());
						clientExportCsvDTO.setCity(contact.getOfficeCity());
						clientExportCsvDTO.setState(contact.getOfficeState());
						clientExportCsvDTO.setPinCode(contact.getOfficePincode());
						clientExportCsvDTO.setAddressType("Office");
						if (contact.getLookupCountry1() != null) {
							clientExportCsvDTO.setCountry(contact.getLookupCountry1().getName());
						}

					}
					if (contact.getPermanentAddressLine1() != null && !contact.getPermanentAddressLine1().isEmpty()) {
						clientExportCsvDTO.setAddressLine1(contact.getPermanentAddressLine1());
						clientExportCsvDTO.setAddressLine2(contact.getPermanentAddressLine2());
						clientExportCsvDTO.setAddressLine3(contact.getPermanentAddressLine3());
						clientExportCsvDTO.setEmailAddress(contact.getEmailID());
						clientExportCsvDTO.setAlternateEmailAddress(contact.getAlternateEmail());
						clientExportCsvDTO.setMobileNumber(contact.getMobile());
						clientExportCsvDTO.setEmergencyContact(contact.getEmergencyContact());
						clientExportCsvDTO.setCity(contact.getPermanentCity());
						clientExportCsvDTO.setState(contact.getPermanentState());
						clientExportCsvDTO.setPinCode(contact.getPermanentPincode());
						clientExportCsvDTO.setAddressType("Permanent");
						if (contact.getLookupCountry2() != null) {
							clientExportCsvDTO.setCountry(contact.getLookupCountry2().getName());
						}

					}
					if (contact.getCorrespondenceAddressLine1() != null
							&& !contact.getCorrespondenceAddressLine1().isEmpty()) {
						clientExportCsvDTO.setAddressLine1(contact.getCorrespondenceAddressLine1());
						clientExportCsvDTO.setAddressLine2(contact.getCorrespondenceAddressLine2());
						clientExportCsvDTO.setAddressLine3(contact.getCorrespondenceAddressLine3());
						clientExportCsvDTO.setEmailAddress(contact.getEmailID());
						clientExportCsvDTO.setAlternateEmailAddress(contact.getAlternateEmail());
						clientExportCsvDTO.setMobileNumber(contact.getMobile());
						clientExportCsvDTO.setEmergencyContact(contact.getEmergencyContact());
						clientExportCsvDTO.setCity(contact.getCorrespondenceCity());
						clientExportCsvDTO.setState(contact.getCorrespondenceState());
						clientExportCsvDTO.setPinCode(contact.getCorrespondencePincode());
						clientExportCsvDTO.setAddressType("Correspondence");
						if (contact.getLookupCountry3() != null) {
							clientExportCsvDTO.setCountry(contact.getLookupCountry3().getName());
						}

					}
				}

				clientExportCsvDTOList.add(clientExportCsvDTO);
			}

			return clientExportCsvDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	private void exportClientByUserCsv(FormDataDTO formDataDTO, HttpServletResponse response) throws IOException {
		String csvFileName = "importClientTemplate.csv";

		response.setContentType("text/csv");

		// creates mock data
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
		response.setHeader(headerKey, headerValue);

		// uses the Super CSV API to generate CSV data from the model data
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		AdvisorUser advisorUser = advisorUserRepository.findOne(formDataDTO.getAdvUserId());
		List<ClientMaster> clientMasterList = clientMasterRepo.findByAdvisorUserAndActiveFlag(advisorUser, "Y");

		String[] header = { "salutation", "firstName", "middleName", "lastName", "birthDate", "gender", "pan", "aadhar",
				"maritalStatus",

				"otherMaritalStatus", "educationalQualification", "otherEducationalQualification", "employmentType",
				"otherEmploymentType", "organisationName", "currentDesignation", "residentType", "otherResidentType",
				"countryOfResidence",

				"retiredFlag", "retirementAge", "emailAddress", "alternateEmailAddress", "mobileNumber",
				"emergencyContact", "addressLine1", "addressLine2", "addressLine3", "city", "state", "pinCode",
				"country", "addressType" };

		csvWriter.writeHeader(header);

		for (ClientMaster obj : clientMasterList) {
			ClientExportCsvDTO clientExportCsvDTO = mapper.map(obj, ClientExportCsvDTO.class);
			ClientContact contact = obj.getClientContacts().get(0);
			if (contact.getOfficeAddressLine1() != null && !contact.getOfficeAddressLine1().isEmpty()) {
				clientExportCsvDTO.setAddressLine1(contact.getOfficeAddressLine1());
				clientExportCsvDTO.setAddressLine2(contact.getOfficeAddressLine2());
				clientExportCsvDTO.setAddressLine3(contact.getOfficeAddressLine3());
				clientExportCsvDTO.setEmailAddress(contact.getEmailID());
				clientExportCsvDTO.setAlternateEmailAddress(contact.getAlternateEmail());
				clientExportCsvDTO.setMobileNumber(contact.getMobile());
				clientExportCsvDTO.setEmergencyContact(contact.getEmergencyContact());
				clientExportCsvDTO.setCity(contact.getOfficeCity());
				clientExportCsvDTO.setState(contact.getOfficeState());
				clientExportCsvDTO.setPinCode(contact.getOfficePincode());
				clientExportCsvDTO.setAddressType("Office");
				if (contact.getLookupCountry1() != null) {
					clientExportCsvDTO.setCountry(contact.getLookupCountry1().getName());
				}

			}
			if (contact.getPermanentAddressLine1() != null && !contact.getPermanentAddressLine1().isEmpty()) {
				clientExportCsvDTO.setAddressLine1(contact.getPermanentAddressLine1());
				clientExportCsvDTO.setAddressLine2(contact.getPermanentAddressLine2());
				clientExportCsvDTO.setAddressLine3(contact.getPermanentAddressLine3());
				clientExportCsvDTO.setEmailAddress(contact.getEmailID());
				clientExportCsvDTO.setAlternateEmailAddress(contact.getAlternateEmail());
				clientExportCsvDTO.setMobileNumber(contact.getMobile());
				clientExportCsvDTO.setEmergencyContact(contact.getEmergencyContact());
				clientExportCsvDTO.setCity(contact.getPermanentCity());
				clientExportCsvDTO.setState(contact.getPermanentState());
				clientExportCsvDTO.setPinCode(contact.getPermanentPincode());
				clientExportCsvDTO.setAddressType("Permanent");
				if (contact.getLookupCountry2() != null) {
					clientExportCsvDTO.setCountry(contact.getLookupCountry2().getName());
				}

			}
			if (contact.getCorrespondenceAddressLine1() != null && !contact.getCorrespondenceAddressLine1().isEmpty()) {
				clientExportCsvDTO.setAddressLine1(contact.getCorrespondenceAddressLine1());
				clientExportCsvDTO.setAddressLine2(contact.getCorrespondenceAddressLine2());
				clientExportCsvDTO.setAddressLine3(contact.getCorrespondenceAddressLine3());
				clientExportCsvDTO.setEmailAddress(contact.getEmailID());
				clientExportCsvDTO.setAlternateEmailAddress(contact.getAlternateEmail());
				clientExportCsvDTO.setMobileNumber(contact.getMobile());
				clientExportCsvDTO.setEmergencyContact(contact.getEmergencyContact());
				clientExportCsvDTO.setCity(contact.getCorrespondenceCity());
				clientExportCsvDTO.setState(contact.getCorrespondenceState());
				clientExportCsvDTO.setPinCode(contact.getCorrespondencePincode());
				clientExportCsvDTO.setAddressType("Correspondence");
				if (contact.getLookupCountry3() != null) {
					clientExportCsvDTO.setCountry(contact.getLookupCountry3().getName());
				}

			}

			log.debug("clientExportCsvDTO : " + clientExportCsvDTO);
			csvWriter.write(clientExportCsvDTO, header);
		}

		csvWriter.close();

	}

	private void exportClientByUserExcel(FormDataDTO formDataDTO, HttpServletResponse response) {
		// TODO Auto-generated method stub
		AdvisorUser advisorUser = advisorUserRepository.findOne(formDataDTO.getAdvUserId());
		String name = advisorUser.getFirstName();
		String fileName = "ClientListOfUser" + name + ".xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addExcelOutputHeader(excelOutputsheet);
			// writeExcelOutputData(excelOutputsheet);
			List<ClientMaster> clientMasterList = clientMasterRepo.findByAdvisorUserAndActiveFlag(advisorUser, "Y");
			addExcelOutputBody(clientMasterList, excelOutputsheet);
			writableWorkbook.write();
			writableWorkbook.close();

		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}

	}

	private void addExcelOutputBody(List<ClientMaster> clientMasterList, WritableSheet sheet)
			throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		// creating body
		int rowIndex = 1;// because header row is already added
		if (clientMasterList != null && clientMasterList.size() > 0) {
			for (int index = 0; index < clientMasterList.size(); index++) {
				ClientMaster obj = clientMasterList.get(index);
				List<ClientContact> clientContactList = obj.getClientContacts();
				for (ClientContact contact : clientContactList) {

					sheet.addCell(new Label(0, rowIndex, obj.getFirstName()));
					sheet.addCell(new Label(1, rowIndex, obj.getMiddleName()));
					sheet.addCell(new Label(2, rowIndex, obj.getLastName()));
					sheet.addCell(new Label(3, rowIndex, FinexaUtil.dateString(obj.getBirthDate())));
					sheet.addCell(new Label(4, rowIndex, obj.getGender()));
					sheet.addCell(new Label(5, rowIndex, obj.getPan()));
					sheet.addCell(new Label(6, rowIndex, "" + obj.getAadhar()));
					sheet.addCell(new Label(7, rowIndex, "" + obj.getLookupMaritalStatus().getDescription()));
					sheet.addCell(new Label(8, rowIndex, obj.getOtherMaritalStatus()));
					if (obj.getLookupEducationalQualification() != null) {
						sheet.addCell(
								new Label(9, rowIndex, "" + obj.getLookupEducationalQualification().getDescription()));
					}
					sheet.addCell(new Label(10, rowIndex, obj.getOtherEduQualification()));

					if (obj.getLookupEmploymentType() != null) {
						sheet.addCell(new Label(11, rowIndex, "" + obj.getLookupEmploymentType().getDescription()));
					}
					sheet.addCell(new Label(12, rowIndex, obj.getOtherEmploymentType()));
					sheet.addCell(new Label(13, rowIndex, obj.getOrgName()));
					sheet.addCell(new Label(14, rowIndex, obj.getCurrDesignation()));
					if (obj.getLookupResidentType() != null) {
						sheet.addCell(new Label(15, rowIndex, "" + obj.getLookupResidentType().getDescription()));
					}

					sheet.addCell(new Label(16, rowIndex, obj.getOtherResidentType()));
					if (obj.getLookupCountry() != null) {
						sheet.addCell(new Label(17, rowIndex, "" + obj.getLookupCountry().getName()));
					}
					sheet.addCell(new Label(18, rowIndex, obj.getRetiredFlag()));
					sheet.addCell(new Label(19, rowIndex, "" + obj.getRetirementAge()));

					sheet.addCell(new Label(20, rowIndex, contact.getEmailID()));
					sheet.addCell(new Label(21, rowIndex, contact.getAlternateEmail()));
					sheet.addCell(new Label(22, rowIndex, "" + contact.getMobile()));
					sheet.addCell(new Label(23, rowIndex, "" + contact.getEmergencyContact().intValue()));

					if (contact.getOfficeAddressLine1() != null && !contact.getOfficeAddressLine1().isEmpty()) {
						sheet.addCell(new Label(31, rowIndex, "Office"));
						sheet.addCell(new Label(24, rowIndex, contact.getOfficeAddressLine1()));
						sheet.addCell(new Label(25, rowIndex, contact.getOfficeAddressLine2()));
						sheet.addCell(new Label(26, rowIndex, contact.getOfficeAddressLine3()));
						sheet.addCell(new Label(27, rowIndex, contact.getOfficeCity()));
						sheet.addCell(new Label(28, rowIndex, contact.getOfficeState()));
						sheet.addCell(new Label(29, rowIndex, "" + contact.getOfficePincode()));
						if (contact.getLookupCountry1() != null) {
							sheet.addCell(new Label(30, rowIndex, "" + contact.getLookupCountry1().getName()));
						}

					}
					if (contact.getPermanentAddressLine1() != null && !contact.getPermanentAddressLine1().isEmpty()) {
						sheet.addCell(new Label(31, rowIndex, "Permanent"));
						sheet.addCell(new Label(24, rowIndex, contact.getPermanentAddressLine1()));
						sheet.addCell(new Label(25, rowIndex, contact.getPermanentAddressLine2()));
						sheet.addCell(new Label(26, rowIndex, contact.getPermanentAddressLine3()));
						sheet.addCell(new Label(27, rowIndex, contact.getPermanentCity()));
						sheet.addCell(new Label(28, rowIndex, contact.getPermanentState()));
						sheet.addCell(new Label(29, rowIndex, "" + contact.getPermanentPincode()));
						if (contact.getLookupCountry2() != null) {
							sheet.addCell(new Label(30, rowIndex, "" + contact.getLookupCountry2().getName()));
						}

					}
					if (contact.getCorrespondenceAddressLine1() != null
							&& !contact.getCorrespondenceAddressLine1().isEmpty()) {
						sheet.addCell(new Label(31, rowIndex, "Correspondence"));
						sheet.addCell(new Label(24, rowIndex, contact.getCorrespondenceAddressLine1()));
						sheet.addCell(new Label(25, rowIndex, contact.getCorrespondenceAddressLine2()));
						sheet.addCell(new Label(26, rowIndex, contact.getCorrespondenceAddressLine3()));
						sheet.addCell(new Label(27, rowIndex, contact.getCorrespondenceCity()));
						sheet.addCell(new Label(28, rowIndex, contact.getCorrespondenceState()));
						sheet.addCell(new Label(29, rowIndex, "" + contact.getCorrespondencePincode()));
						if (contact.getLookupCountry3() != null) {
							sheet.addCell(new Label(30, rowIndex, "" + contact.getLookupCountry3().getName()));
						}

					}
					rowIndex++;
				}

			}

		}
	}

	@Override
	public UploadResponseDTO uploadClientByUser(FileuploadDTO fileuploadDTO, UploadResponseDTO uploadResponseDTO)
			throws RuntimeException, CustomFinexaException {

		try {
			if (fileuploadDTO.getFile()[0].getOriginalFilename().contains(".csv")) {
				// call csv upload
				uploadResponseDTO = uploadClientByUserCsvFile(fileuploadDTO, uploadResponseDTO);

			} else if (fileuploadDTO.getFile()[0].getOriginalFilename().contains(".xls")) {
				// call excel upload
				uploadResponseDTO = uploadClientByUserExcelFile(fileuploadDTO, uploadResponseDTO);
			}
			return uploadResponseDTO;
		} //catch (RuntimeException e) {
		catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Transactional(rollbackOn = CustomFinexaException.class)
	private UploadResponseDTO uploadClientByUserCsvFile(FileuploadDTO fileuploadDTO,
			UploadResponseDTO uploadResponseDTO) throws RuntimeException, CustomFinexaException {

		try {
			String line = "";
			int rowNum = 0;
			String cvsSplitBy = ",";
			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream()))) {
				while ((line = br.readLine()) != null) {
					if (rowNum == 0) {
						rowNum++;
						continue;
					}
					ClientMaster clientMaster = new ClientMaster();
					String[] fields = line.split(cvsSplitBy);
					// ---- set all other properties and then save the
					// clientinfo as
					// csv below

					String retiredFlag;
					try {
						clientMaster.setAdvisorUser(advisorUserRepository.findOne(fileuploadDTO.getAdvisorUserId()));
						clientMaster.setSalutation(FinexaUtil.getSalutation(fields[4],
								maritalStatusRepository.findByDescription(fields[7])));
						clientMaster.setFirstName(fields[0]);
						String generatedString = RandomStringUtils.randomAlphabetic(6);
						clientMaster.setLoginPassword(generatedString);
						clientMaster.setMiddleName(fields[1]);
						clientMaster.setLastName(fields[2]);
						clientMaster.setBirthDate(FinexaUtil.parseDate(fields[3]));
						clientMaster.setGender(fields[4].toUpperCase());
						clientMaster.setPan(fields[5]);
						// BigInteger value = new BigInteger(fields[6]);
						clientMaster.setAadhar(fields[6]);
						clientMaster.setLookupMaritalStatus(maritalStatusRepository.findByDescription(fields[7]));
						clientMaster.setOtherMaritalStatus(fields[8]);
						clientMaster.setLookupEducationalQualification(eduQualRepo.findByDescription(fields[9]));
						clientMaster.setOtherEduQualification(fields[10]);
						clientMaster.setLookupEmploymentType(empTypeRepo.findByDescription(fields[11]));
						clientMaster.setOtherEmploymentType(fields[12]);
						clientMaster.setOrgName(fields[13]);
						clientMaster.setCurrDesignation(fields[14]);
						clientMaster.setLookupResidentType(resTyprRepo.findByDescription(fields[15]));

						clientMaster.setOtherResidentType(fields[16]);
						clientMaster.setLookupCountry(countryRepo.findByName(fields[17].toUpperCase()));
						retiredFlag = fields[18];
						clientMaster.setRetiredFlag(retiredFlag);
						clientMaster.setRetirementAge(Byte.valueOf((fields[19])));
						clientMaster.setActiveFlag("Y");
						clientMaster.setClient("Y");
						clientMaster.setCreatedOn(new Date());
						clientMaster.setLastLoginTime(new Date());
						clientMaster.setFinexaUser("Y");
						ClientMaster existingCm = clientMasterRepo.findByPan(fields[5]);
						if (existingCm != null) {
							clientMaster.setId(existingCm.getId());
						}
						clientMaster = clientMasterRepo.save(clientMaster);
						
						// client access rights
						ClientAccessRight clientAccessRight = new ClientAccessRight();
						clientAccessRight.setClientMaster(clientMaster);			
						clientAccessRight.setClientInfoView("Y");
						clientAccessRight.setClientInfoAddEdit("N");
						clientAccessRight.setClientInfoDelete("N");
						clientAccessRight.setBudgetManagementView("N");
						clientAccessRight.setGoalPlanningView("N");
						clientAccessRight.setGoalPlanningAddEdit("N");
						clientAccessRight.setPortfolioManagementView("N");
						clientAccessRight.setPortfolioManagementAddEdit("N");
						clientAccessRight.setFinancialPlanningView("N");
						clientAccessRight.setFinancialPlanningAddEdit("N");
						clientAccessRight.setInvestView("N");
						clientAccessRight.setInvestAddEdit("N");
						clientAccessRight.setMfBackOfficeView("N");
						clientAccessRight.setCreatedOn(new Date());
						clientAccessRightsRepository.save(clientAccessRight);
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						throw new CustomFinexaException("Client Master", "Nothing Specific",
								"Failed to save Client Personal Information.");
					}
					// saving self record in Client Family Member
					try {
						ClientFamilyMember cfm = new ClientFamilyMember();
						cfm.setClientMaster(clientMaster);
						cfm.setFirstName(fields[0]);
						cfm.setMiddleName(fields[1]);
						cfm.setLastName(fields[2]);
						cfm.setLookupRelation(relationRepo.findOne((byte) 0));
						cfm.setBirthDate(FinexaUtil.parseDate(fields[3]));
						cfm.setPan(fields[5]);
						cfm.setAadhar(new BigInteger(fields[6]));

						cfm.setRetiredFlag(retiredFlag);
						cfm.setDependentFlag("N");
						cfm.setRetirementAge(Byte.valueOf(fields[19]));
						cfm.setHasDiseaseHistory(fields[35]);
						cfm.setIsProperBMI(fields[34]);
						cfm.setIsTobaccoUser(fields[33]);
						cfm.setHasNormalBP(fields[36]);

						ClientFamilyMember existingCFM = familyMemRepo.findBypan(fields[5]);
						if (existingCFM != null) {
							cfm.setId(existingCFM.getId());
						}
						cfm = familyMemRepo.save(cfm);
						
						//save income
						ClientFamilyIncome clientFamilyIncome = new ClientFamilyIncome();
						
						float incomeAmount = Float.parseFloat(fields[32]);
						System.out.println("income = " + incomeAmount);
						
						LookupFrequency lookupFrequency = frequencyRepository.findOne(FinexaConstant.FREQUENCY_MONTHLY_ID);
						LookupMonth lookupMonth = monthRepository.findOne(FinexaConstant.FREQUENCY_NA_ID);
						LookupIncomeExpenseDuration lookupIncomeExpenseDuration = 
								incomeExpenseDurationRepository.findOne(FinexaConstant.INCOME_END_YEAR_LIFE_EXPECTANCY);
						
						clientFamilyIncome.setClientMaster(clientMaster);
						clientFamilyIncome.setClientFamilyMember(cfm);
						clientFamilyIncome.setIncomeType((byte) 8);
						clientFamilyIncome.setIncomeAmount(new BigDecimal(incomeAmount  / FinexaConstant.FREQUENCY_MONTHLY_ID));
						System.out.println("icome / 12 = " + BigDecimal.valueOf(incomeAmount  / FinexaConstant.FREQUENCY_MONTHLY_ID));
						//System.out.println("====================" + new BigDecimal(incomeAmount  / FinexaConstant.FREQUENCY_MONTHLY_ID).setScale(2));
						clientFamilyIncome.setLookupFrequency(lookupFrequency);
						clientFamilyIncome.setLookupMonth(lookupMonth);
						clientFamilyIncome.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);
						clientFamilyIncome.setCreatedOn(new Timestamp(new Date().getTime()));
						
						clientFamilyIncome = cientFamilyIncomeRepository.save(clientFamilyIncome);
						
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						throw new CustomFinexaException("Client Family Member", "Nothing Specific",
								"Failed to save Client Family Member Information.");
					}
					// saving Client Contact
					try {
						ClientContact clientContact = new ClientContact();
						clientContact.setClientMaster(clientMaster);
						if (fields[31].equalsIgnoreCase("Permanent")) {
							clientContact.setPermanentAddressLine1(fields[24]);
							clientContact.setPermanentAddressLine2(fields[25]);
							clientContact.setPermanentAddressLine3(fields[26]);
							clientContact.setPermanentCity(fields[27]);
							clientContact.setPermanentState(fields[28]);
							clientContact.setPermanentPincode(Integer.parseInt(fields[29]));
							clientContact.setLookupCountry2((countryRepo.findByName(fields[30].toUpperCase())));

						} else if (fields[31].equalsIgnoreCase("Correspondence")) {

							clientContact.setCorrespondenceAddressLine1(fields[24]);
							clientContact.setCorrespondenceAddressLine2(fields[25]);
							clientContact.setCorrespondenceAddressLine3(fields[26]);
							clientContact.setCorrespondenceCity(fields[27]);
							clientContact.setCorrespondenceState(fields[28]);
							clientContact.setCorrespondencePincode(Integer.parseInt(fields[29]));
							clientContact.setLookupCountry3((countryRepo.findByName(fields[30].toUpperCase())));

						} else if (fields[31].equalsIgnoreCase("Office")) {

							clientContact.setOfficeAddressLine1(fields[24]);
							clientContact.setOfficeAddressLine2(fields[25]);
							clientContact.setOfficeAddressLine3(fields[26]);
							clientContact.setOfficeCity(fields[27]);
							clientContact.setOfficeState(fields[28]);
							clientContact.setOfficePincode(Integer.parseInt(fields[29]));
							clientContact.setLookupCountry1((countryRepo.findByName(fields[30].toUpperCase())));
						}
						clientContact.setEmailID(fields[20]);
						clientContact.setCountryCode(fields[30]);
						clientContact.setAlternateEmail(fields[21]);
						clientContact.setEmergencyContact(new BigInteger(fields[23]));
						clientContact.setMobile(new BigInteger(fields[22]));
						clientContact = clientContactRepo.save(clientContact);
						
						clientMaster.setLoginUsername(clientContact.getEmailID());
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						throw new CustomFinexaException("Client Contact", "Nothing Specific",
								"Failed to save Client Contact Details.");
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return uploadResponseDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Transactional(rollbackOn = CustomFinexaException.class)
	private UploadResponseDTO uploadClientByUserExcelFile(FileuploadDTO fileuploadDTO,
			UploadResponseDTO uploadResponseDTO) throws RuntimeException, CustomFinexaException {

		try {
			Workbook workbook = null;
			try {

				workbook = Workbook.getWorkbook(fileuploadDTO.getFile()[0].getInputStream());
				Sheet sheet = workbook.getSheet(0);
				for (int rownum = 1; rownum < sheet.getRows(); rownum++) {

					ClientMaster clientMaster = new ClientMaster();
					// saving in clientMaster
					String retiredFlag;
					try {
						clientMaster.setAdvisorUser(advisorUserRepository.findOne(fileuploadDTO.getAdvisorUserId()));
						clientMaster.setSalutation(FinexaUtil.getSalutation(sheet.getCell(4, rownum).getContents(),
								maritalStatusRepository.findByDescription(sheet.getCell(7, rownum).getContents())));
						clientMaster.setFirstName(sheet.getCell(0, rownum).getContents().trim());
						String generatedString = RandomStringUtils.randomAlphabetic(6);
						clientMaster.setLoginPassword(generatedString);
						clientMaster.setMiddleName(sheet.getCell(1, rownum).getContents().trim());
						clientMaster.setLastName(sheet.getCell(2, rownum).getContents().trim());
						//System.out.println("birthdate " + (sheet.getCell(3, rownum).getContents()));
						//System.out.println("birthdate with FinexaUtil " + FinexaUtil.parseDate(sheet.getCell(3, rownum).getContents()));
						clientMaster.setBirthDate(FinexaUtil.parseDate(sheet.getCell(3, rownum).getContents()));
						clientMaster.setGender(sheet.getCell(4, rownum).getContents());
						clientMaster.setPan(sheet.getCell(5, rownum).getContents());
						clientMaster.setAadhar(sheet.getCell(6, rownum).getContents());
						clientMaster.setLookupMaritalStatus(
								maritalStatusRepository.findByDescription(sheet.getCell(7, rownum).getContents()));
						clientMaster.setOtherMaritalStatus(sheet.getCell(8, rownum).getContents());
						clientMaster.setLookupEducationalQualification(
								eduQualRepo.findByDescription(sheet.getCell(9, rownum).getContents()));
						clientMaster.setOtherEduQualification(sheet.getCell(10, rownum).getContents());
						clientMaster.setLookupEmploymentType(
								empTypeRepo.findByDescription(sheet.getCell(11, rownum).getContents()));
						clientMaster.setOtherEmploymentType(sheet.getCell(12, rownum).getContents());
						clientMaster.setOrgName(sheet.getCell(13, rownum).getContents());
						clientMaster.setCurrDesignation(sheet.getCell(14, rownum).getContents());
						clientMaster.setLookupResidentType(
								resTyprRepo.findByDescription(sheet.getCell(15, rownum).getContents()));

						clientMaster.setOtherResidentType(sheet.getCell(16, rownum).getContents());
						clientMaster.setLookupCountry(
								countryRepo.findByName(sheet.getCell(17, rownum).getContents().toUpperCase()));
						retiredFlag = sheet.getCell(18, rownum).getContents();
						clientMaster.setRetiredFlag(retiredFlag);
						clientMaster.setRetirementAge(Byte.valueOf(sheet.getCell(19, rownum).getContents()));
						clientMaster.setActiveFlag("Y");
						clientMaster.setClient("Y");
						clientMaster.setCreatedOn(new Date());
						clientMaster.setLastLoginTime(new Date());
						clientMaster.setFinexaUser("Y");
						ClientMaster existingCm = clientMasterRepo.findByPan(sheet.getCell(5, rownum).getContents());
						if (existingCm != null) {
							clientMaster.setId(existingCm.getId());
						}
						clientMaster = clientMasterRepo.save(clientMaster);
						
						// client access rights
						ClientAccessRight clientAccessRight = new ClientAccessRight();
						clientAccessRight.setClientMaster(clientMaster);			
						clientAccessRight.setClientInfoView("Y");
						clientAccessRight.setClientInfoAddEdit("N");
						clientAccessRight.setClientInfoDelete("N");
						clientAccessRight.setBudgetManagementView("N");
						clientAccessRight.setGoalPlanningView("N");
						clientAccessRight.setGoalPlanningAddEdit("N");
						clientAccessRight.setPortfolioManagementView("N");
						clientAccessRight.setPortfolioManagementAddEdit("N");
						clientAccessRight.setFinancialPlanningView("N");
						clientAccessRight.setFinancialPlanningAddEdit("N");
						clientAccessRight.setInvestView("N");
						clientAccessRight.setInvestAddEdit("N");
						clientAccessRight.setMfBackOfficeView("N");
						clientAccessRight.setCreatedOn(new Date());
						clientAccessRightsRepository.save(clientAccessRight);
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						throw new CustomFinexaException("Client Master", "Nothing Specific",
								"Failed to save Client Personal Information.");
					}

					// saving self record in Client Family Member
					try {
						ClientFamilyMember cfm = new ClientFamilyMember();
						cfm.setClientMaster(clientMaster);
						cfm.setFirstName(sheet.getCell(0, rownum).getContents());
						cfm.setMiddleName(sheet.getCell(1, rownum).getContents());
						cfm.setLastName(sheet.getCell(2, rownum).getContents());
						cfm.setLookupRelation(relationRepo.findOne((byte) 0));
						cfm.setBirthDate(FinexaUtil.parseDate(sheet.getCell(3, rownum).getContents()));
						cfm.setPan(sheet.getCell(5, rownum).getContents());
						cfm.setAadhar(new BigInteger(sheet.getCell(6, rownum).getContents()));

						cfm.setRetiredFlag(retiredFlag);
						cfm.setDependentFlag("N");
						cfm.setRetirementAge(Byte.valueOf(sheet.getCell(19, rownum).getContents()));
						cfm.setHasDiseaseHistory(sheet.getCell(35, rownum).getContents());
						cfm.setIsProperBMI(sheet.getCell(34, rownum).getContents());
						cfm.setIsTobaccoUser(sheet.getCell(33, rownum).getContents());
						cfm.setHasNormalBP(sheet.getCell(36, rownum).getContents());

						ClientFamilyMember existingCFM = familyMemRepo
								.findBypan(sheet.getCell(5, rownum).getContents());
						if (existingCFM != null) {
							cfm.setId(existingCFM.getId());
						}
						cfm = familyMemRepo.save(cfm);
						
						//save income
						ClientFamilyIncome clientFamilyIncome = new ClientFamilyIncome();
						
						float incomeAmount = Float.parseFloat(sheet.getCell(32, rownum).getContents());
						
						LookupFrequency lookupFrequency = frequencyRepository.findOne(FinexaConstant.FREQUENCY_MONTHLY_ID);
						LookupMonth lookupMonth = monthRepository.findOne(FinexaConstant.FREQUENCY_NA_ID);
						LookupIncomeExpenseDuration lookupIncomeExpenseDuration = 
								incomeExpenseDurationRepository.findOne(FinexaConstant.INCOME_END_YEAR_LIFE_EXPECTANCY);
						
						clientFamilyIncome.setClientMaster(clientMaster);
						clientFamilyIncome.setClientFamilyMember(cfm);
						clientFamilyIncome.setIncomeType((byte) 8);
						clientFamilyIncome.setIncomeAmount(new BigDecimal(incomeAmount  / FinexaConstant.FREQUENCY_MONTHLY_ID));
						System.out.println("====================" + BigDecimal.valueOf(incomeAmount  / FinexaConstant.FREQUENCY_MONTHLY_ID));
						//System.out.println("====================" + new BigDecimal(incomeAmount  / FinexaConstant.FREQUENCY_MONTHLY_ID).setScale(2));
						clientFamilyIncome.setLookupFrequency(lookupFrequency);
						clientFamilyIncome.setLookupMonth(lookupMonth);
						clientFamilyIncome.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);
						clientFamilyIncome.setCreatedOn(new Timestamp(new Date().getTime()));
						
						clientFamilyIncome = cientFamilyIncomeRepository.save(clientFamilyIncome);
						
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						throw new CustomFinexaException("Client Family Member", "Nothing Specific",
								"Failed to save Client Family Member Information.");
					}

					// saving Client Contact
					try {
						ClientContact clientContact = new ClientContact();
						clientContact.setClientMaster(clientMaster);
						if (sheet.getCell(31, rownum).getContents().equalsIgnoreCase("Permanent")) {
							clientContact.setPermanentAddressLine1(sheet.getCell(24, rownum).getContents());
							clientContact.setPermanentAddressLine2(sheet.getCell(25, rownum).getContents());
							clientContact.setPermanentAddressLine3(sheet.getCell(26, rownum).getContents());
							clientContact.setPermanentCity(sheet.getCell(27, rownum).getContents());
							clientContact.setPermanentState(sheet.getCell(28, rownum).getContents());
							clientContact
									.setPermanentPincode(Integer.parseInt(sheet.getCell(29, rownum).getContents()));
							clientContact.setLookupCountry2(
									(countryRepo.findByName(sheet.getCell(17, rownum).getContents())));

						} else if (sheet.getCell(31, rownum).getContents().equalsIgnoreCase("Correspondence")) {

							clientContact.setCorrespondenceAddressLine1(sheet.getCell(24, rownum).getContents());
							clientContact.setCorrespondenceAddressLine2(sheet.getCell(25, rownum).getContents());
							clientContact.setCorrespondenceAddressLine3(sheet.getCell(26, rownum).getContents());
							clientContact.setCorrespondenceCity(sheet.getCell(27, rownum).getContents());
							clientContact.setCorrespondenceState(sheet.getCell(28, rownum).getContents());
							clientContact.setCorrespondencePincode(
									Integer.parseInt(sheet.getCell(29, rownum).getContents()));
							clientContact.setLookupCountry3(
									(countryRepo.findByName(sheet.getCell(17, rownum).getContents())));

						} else if (sheet.getCell(31, rownum).getContents().equalsIgnoreCase("Office")) {

							clientContact.setOfficeAddressLine1(sheet.getCell(24, rownum).getContents());
							clientContact.setOfficeAddressLine2(sheet.getCell(25, rownum).getContents());
							clientContact.setOfficeAddressLine3(sheet.getCell(26, rownum).getContents());
							clientContact.setOfficeCity(sheet.getCell(27, rownum).getContents());
							clientContact.setOfficeState(sheet.getCell(28, rownum).getContents());
							clientContact.setOfficePincode(Integer.parseInt(sheet.getCell(29, rownum).getContents()));
							clientContact.setLookupCountry1(
									(countryRepo.findByName(sheet.getCell(17, rownum).getContents())));
						}
						clientContact.setEmailID(sheet.getCell(20, rownum).getContents());
						clientContact.setCountryCode(sheet.getCell(17, rownum).getContents());
						clientContact.setAlternateEmail(sheet.getCell(21, rownum).getContents());
						clientContact.setEmergencyContact(new BigInteger(sheet.getCell(23, rownum).getContents()));
						clientContact.setMobile(new BigInteger(sheet.getCell(22, rownum).getContents()));
						clientContact = clientContactRepo.save(clientContact);
						
						// save client login username
						clientMaster.setLoginUsername(clientContact.getEmailID());
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						throw new CustomFinexaException("Client Contact", "Nothing Specific",
								"Failed to save Client Contact Details.");
					}

				}
			} catch (IOException e) {
				uploadResponseDTO.getErrors().add(e.getMessage());
				e.printStackTrace();
			} catch (BiffException e) {
				uploadResponseDTO.getErrors().add(e.getMessage());
				e.printStackTrace();
			} finally {
				if (workbook != null) {
					workbook.close();
				}
			}

			return uploadResponseDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ViewUserManagmentDTO findUserName(int selectedUserId) {
		// TODO Auto-generated method stub
		ViewUserManagmentDTO userDTO = new ViewUserManagmentDTO();
		AdvisorUser user = advisorUserRepository.findOne(selectedUserId);
		userDTO.setUserName(user.getFirstName() + user.getLastName());
		return userDTO;
	}

	// get selected modules for Finlabs admin for Access Rights
	@Override
	public List<AccessRightDTO> findModuleByAdvisorId(int advisorID) throws RuntimeException {
		AccessRightDTO accessRightDTO = null;
		List<AccessRightDTO> accessRightDTOList = new ArrayList<AccessRightDTO>();
		try {
			AdvisorUser advUser = advisorUserRepository.findOne(advisorID);
			User user = advUser.getUser();
			accessRightDTO = new AccessRightDTO();
			
			if ((user.getClientInfoView() != null && user.getClientInfoView().equalsIgnoreCase(MODULE_ACCCESS_YES)) || (user.getClientInfoAddEdit() != null && user.getClientInfoAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES) ) || (user.getClientInfoDelete()!= null && user.getClientInfoDelete().equalsIgnoreCase(MODULE_ACCCESS_YES))) {
				//roleList.add("ClientInfo");
				accessRightDTO = new AccessRightDTO();
				accessRightDTO.setAccessRight("ClientInfo");
				if ((user.getClientInfoView() != null && user.getClientInfoView().equalsIgnoreCase(MODULE_ACCCESS_YES))) {
					accessRightDTO.setViewAllowedFlag("Y");
				}
				if (user.getClientInfoAddEdit() != null && user.getClientInfoAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					accessRightDTO.setAddeditAllowedFlag("Y");
				}
				if (user.getClientInfoDelete()!= null && user.getClientInfoDelete().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					accessRightDTO.setDeleteAllowedFlag("Y");
				}
				accessRightDTOList.add(accessRightDTO);
			}

			if (user.getBudgetManagementView() != null && user.getBudgetManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
				//roleList.add("BudgetManagement");
				accessRightDTO = new AccessRightDTO();
				accessRightDTO.setAccessRight("BudgetManagement");
				accessRightDTOList.add(accessRightDTO);
				
			}

			if ((user.getGoalPlanningView() != null && user.getGoalPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)) || (user.getClientInfoAddEdit() != null && user.getGoalPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES) )) {
				//roleList.add("GoalPlanning");
				accessRightDTO = new AccessRightDTO();
				accessRightDTO.setAccessRight("GoalPlanning");
				
				if (user.getGoalPlanningView() != null && user.getGoalPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					accessRightDTO.setViewAllowedFlag("Y");
				}
				if (user.getClientInfoAddEdit() != null && user.getGoalPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					accessRightDTO.setAddeditAllowedFlag("Y");
				}
				accessRightDTOList.add(accessRightDTO);
			}

			if ((user.getPortfolioManagementView() != null && user.getPortfolioManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES) || (user.getPortfolioManagementAddEdit() != null && user.getPortfolioManagementAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)))) {
				//roleList.add("PortfolioManagement");
				accessRightDTO = new AccessRightDTO();
				accessRightDTO.setAccessRight("PortfolioManagement");
				
				if (user.getPortfolioManagementView() != null && user.getPortfolioManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					accessRightDTO.setViewAllowedFlag("Y");
				}
				if (user.getPortfolioManagementAddEdit() != null && user.getPortfolioManagementAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					accessRightDTO.setAddeditAllowedFlag("Y");
				}
				accessRightDTOList.add(accessRightDTO);
			}

			if ((user.getFinancialPlanningView() != null && user.getFinancialPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)  || ((user.getFinancialPlanningAddEdit() != null && user.getFinancialPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES))))){
				//roleList.add("FinancialPlanning");
				accessRightDTO = new AccessRightDTO();
				accessRightDTO.setAccessRight("FinancialPlanning");
				
				if (user.getFinancialPlanningView() != null && user.getFinancialPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)){
					accessRightDTO.setViewAllowedFlag("Y");
				}
				if (user.getFinancialPlanningAddEdit() != null && user.getFinancialPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)){
					accessRightDTO.setAddeditAllowedFlag("Y");
				}
				accessRightDTOList.add(accessRightDTO);
			}

//			if (user.getUserManagementView() != null && user.getUserManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES) || (user.getUserManagementAddEdit() != null && user.getUserManagementAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) || (user.getUserManagementDelete() != null && user.getUserManagementDelete().equalsIgnoreCase(MODULE_ACCCESS_YES))){
//				//roleList.add("UserManagement");
//				accessRightDTO = new AccessRightDTO();
//				accessRightDTO.setAccessRight("UserManagement");
//				
//				if (user.getUserManagementView() != null && user.getUserManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES)){
//					accessRightDTO.setViewAllowedFlag("Y");
//				}
//				if (user.getUserManagementAddEdit() != null && user.getUserManagementAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)){
//					accessRightDTO.setAddeditAllowedFlag("Y");
//				}
//				if (user.getUserManagementDelete() != null && user.getUserManagementDelete().equalsIgnoreCase(MODULE_ACCCESS_YES)){
//					accessRightDTO.setDeleteAllowedFlag("Y");
//				}
//				accessRightDTOList.add(accessRightDTO);
//			}

			if (user.getClientRecordsView() != null && user.getClientRecordsView().equalsIgnoreCase(MODULE_ACCCESS_YES)){
				//roleList.add("ClientRecords");
				accessRightDTO = new AccessRightDTO();
				accessRightDTO.setAccessRight("ClientRecords");
				accessRightDTOList.add(accessRightDTO);
			}

//			if ((user.getMastersView() != null && user.getMastersView().equalsIgnoreCase(MODULE_ACCCESS_YES)) || ((user.getMastersAddEdit() != null && user.getMastersAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES))) || ((user.getMastersDelete() != null && user.getMastersDelete().equalsIgnoreCase(MODULE_ACCCESS_YES)))) {
//				//roleList.add("Masters");
//				accessRightDTO = new AccessRightDTO();
//				accessRightDTO.setAccessRight("Masters");
//				
//				if (user.getMastersView() != null && user.getMastersView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
//					accessRightDTO.setViewAllowedFlag("Y");
//				}
//				if (user.getMastersAddEdit() != null && user.getMastersAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
//					accessRightDTO.setAddeditAllowedFlag("Y");
//				}
//				if (user.getMastersDelete() != null && user.getMastersDelete().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
//					accessRightDTO.setDeleteAllowedFlag("Y");
//				}
//				accessRightDTOList.add(accessRightDTO);
//			}

			if ((user.getMfBackOfficeView() != null && user.getMfBackOfficeView().equalsIgnoreCase(MODULE_ACCCESS_YES)) || (user.getMfBackOfficeAddEdit() != null && user.getMfBackOfficeAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES))) {
				//roleList.add("MFBackOffice");
				accessRightDTO = new AccessRightDTO();
				accessRightDTO.setAccessRight("MFBackOffice");
				
				if (user.getMfBackOfficeView() != null && user.getMfBackOfficeView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
				accessRightDTO.setViewAllowedFlag("Y");
				}
				if (user.getMfBackOfficeAddEdit() != null && user.getMfBackOfficeAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
				accessRightDTO.setAddeditAllowedFlag("Y");
				}
				accessRightDTOList.add(accessRightDTO);
			}

			/*
			 * if (user.getBseMFView() != null &&
			 * user.getBseMFView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
			 * //roleList.add("BSEMF"); accessRightDTO = new AccessRightDTO();
			 * accessRightDTO.setAccessRight("BSEMF");
			 * 
			 * if (user.getBseMFView() != null &&
			 * user.getBseMFView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
			 * accessRightDTO.setViewAllowedFlag("Y"); }
			 * accessRightDTOList.add(accessRightDTO); }
			 */

			if ((user.getInvestView() != null && user.getInvestView().equalsIgnoreCase(MODULE_ACCCESS_YES) || (user.getInvestAddEdit() != null && user.getInvestAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)))) {
				//roleList.add("Invest");
				accessRightDTO = new AccessRightDTO();
				accessRightDTO.setAccessRight("Invest");
				
				if (user.getInvestView() != null && user.getInvestView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					accessRightDTO.setViewAllowedFlag("Y");
				}
				if (user.getInvestAddEdit() != null && user.getInvestAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					accessRightDTO.setAddeditAllowedFlag("Y");
				}
				accessRightDTOList.add(accessRightDTO);
			}
			//accessRightDTO.setAccessRights(roleList);
			return accessRightDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	//for client portal access rights
		@Override
		public List<AccessRightDTO> findModuleByClient(int userID) throws RuntimeException {
			AccessRightDTO accessRightDTO = null;
			List<AccessRightDTO> accessRightDTOList = new ArrayList<AccessRightDTO>();
			try {
				AdvisorUser advUser = advisorUserRepository.findOne(userID);
				User user = advUser.getUser();

				if ((user.getClientInfoView() != null && user.getClientInfoView().equalsIgnoreCase(MODULE_ACCCESS_YES)) || (user.getClientInfoAddEdit() != null && user.getClientInfoAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES) ) || (user.getClientInfoDelete()!= null && user.getClientInfoDelete().equalsIgnoreCase(MODULE_ACCCESS_YES))) {
					//roleList.add("ClientInfo");
					accessRightDTO = new AccessRightDTO();
					accessRightDTO.setAccessRight("ClientInfo");
					if ((user.getClientInfoView() != null && user.getClientInfoView().equalsIgnoreCase(MODULE_ACCCESS_YES))) {
						accessRightDTO.setViewAllowedFlag("Y");
					}
					if (user.getClientInfoAddEdit() != null && user.getClientInfoAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
						accessRightDTO.setAddeditAllowedFlag("Y");
					}
					if (user.getClientInfoDelete()!= null && user.getClientInfoDelete().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
						accessRightDTO.setDeleteAllowedFlag("Y");
					}
					accessRightDTOList.add(accessRightDTO);
				}

				if (user.getBudgetManagementView() != null && user.getBudgetManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					//roleList.add("BudgetManagement");
					accessRightDTO = new AccessRightDTO();
					accessRightDTO.setAccessRight("BudgetManagement");
					accessRightDTOList.add(accessRightDTO);
					
				}

				if ((user.getGoalPlanningView() != null && user.getGoalPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)) || (user.getGoalPlanningAddEdit() != null && user.getGoalPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES) )) {
					//roleList.add("GoalPlanning");
					accessRightDTO = new AccessRightDTO();
					accessRightDTO.setAccessRight("GoalPlanning");
					
					if (user.getGoalPlanningView() != null && user.getGoalPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
						accessRightDTO.setViewAllowedFlag("Y");
					}
					if (user.getGoalPlanningAddEdit() != null && user.getGoalPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
						accessRightDTO.setAddeditAllowedFlag("Y");
					}
					accessRightDTOList.add(accessRightDTO);
				}

				if ((user.getPortfolioManagementView() != null && user.getPortfolioManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES) || (user.getPortfolioManagementAddEdit() != null && user.getPortfolioManagementAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)))) {
					//roleList.add("PortfolioManagement");
					accessRightDTO = new AccessRightDTO();
					accessRightDTO.setAccessRight("PortfolioManagement");
					
					if (user.getPortfolioManagementView() != null && user.getPortfolioManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
						accessRightDTO.setViewAllowedFlag("Y");
					}
					if (user.getPortfolioManagementAddEdit() != null && user.getPortfolioManagementAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
						accessRightDTO.setAddeditAllowedFlag("Y");
					}
					accessRightDTOList.add(accessRightDTO);
				}

				if ((user.getFinancialPlanningView() != null && user.getFinancialPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)  || ((user.getFinancialPlanningAddEdit() != null && user.getFinancialPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES))))){
					//roleList.add("FinancialPlanning");
					accessRightDTO = new AccessRightDTO();
					accessRightDTO.setAccessRight("FinancialPlanning");
					
					if (user.getFinancialPlanningView() != null && user.getFinancialPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)){
						accessRightDTO.setViewAllowedFlag("Y");
					}
					if (user.getFinancialPlanningAddEdit() != null && user.getFinancialPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)){
						accessRightDTO.setAddeditAllowedFlag("Y");
					}
					accessRightDTOList.add(accessRightDTO);
				}

				if ((user.getMfBackOfficeView() != null && user.getMfBackOfficeView().equalsIgnoreCase(MODULE_ACCCESS_YES))) {
					//roleList.add("MFBackOffice");
					accessRightDTO = new AccessRightDTO();
					accessRightDTO.setAccessRight("MFBackOffice");
					
					if (user.getMfBackOfficeView() != null && user.getMfBackOfficeView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					accessRightDTO.setViewAllowedFlag("Y");
					}
					
					accessRightDTOList.add(accessRightDTO);
				}

				if ((user.getInvestView() != null && user.getInvestView().equalsIgnoreCase(MODULE_ACCCESS_YES) || (user.getInvestAddEdit() != null && user.getInvestAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)))) {
					//roleList.add("Invest");
					accessRightDTO = new AccessRightDTO();
					accessRightDTO.setAccessRight("Invest");
					
					if (user.getInvestView() != null && user.getInvestView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
						accessRightDTO.setViewAllowedFlag("Y");
					}
					if (user.getInvestAddEdit() != null && user.getInvestAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
						accessRightDTO.setAddeditAllowedFlag("Y");
					}
					accessRightDTOList.add(accessRightDTO);
				}
				//accessRightDTO.setAccessRights(roleList);
				return accessRightDTOList;
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}
		
	//For Client's own details
		@Override
		public AccessRightDTO findModuleForClient(int clientID) throws RuntimeException {
			    AccessRightDTO accessRightDTO = null;
			try {
				ClientMaster clientMaster = clientMasterRepository.findOne(clientID);
				ClientAccessRight clientAccessRight = clientMaster.getClientAccessRight();
				
				
				if(clientAccessRight != null) {
				List<String> roleList = new ArrayList<String>();
			    accessRightDTO = new AccessRightDTO();
				if ((clientAccessRight.getClientInfoView() != null && clientAccessRight.getClientInfoView().equalsIgnoreCase(MODULE_ACCCESS_YES))) {
					roleList.add("ClientInfoView");
				}
				if (clientAccessRight.getClientInfoAddEdit() != null && clientAccessRight.getClientInfoAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("ClientInfoAddEdit");
				}
				if (clientAccessRight.getClientInfoDelete()!= null && clientAccessRight.getClientInfoDelete().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("ClientInfoDelete");
				}

				if (clientAccessRight.getBudgetManagementView() != null && clientAccessRight.getBudgetManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("BudgetManagementView");
				}

				if (clientAccessRight.getGoalPlanningView() != null && clientAccessRight.getGoalPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("GoalPlanningView");
				}
				if (clientAccessRight.getGoalPlanningAddEdit() != null && clientAccessRight.getGoalPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("GoalPlanningAddEdit");
				}

				if (clientAccessRight.getPortfolioManagementView() != null && clientAccessRight.getPortfolioManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("PortfolioManagementView");
				}
				if (clientAccessRight.getPortfolioManagementAddEdit() != null && clientAccessRight.getPortfolioManagementAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("PortfolioManagementAddEdit");
				}

				if (clientAccessRight.getFinancialPlanningView() != null && clientAccessRight.getFinancialPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)){
					roleList.add("FinancialPlanningView");
				}
				if (clientAccessRight.getFinancialPlanningAddEdit() != null && clientAccessRight.getFinancialPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)){
					roleList.add("FinancialPlanningAddEdit");
				}

				if (clientAccessRight.getMfBackOfficeView() != null && clientAccessRight.getMfBackOfficeView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("MFBackOfficeView");
				}

				if (clientAccessRight.getInvestView() != null && clientAccessRight.getInvestView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("InvestView");
				}
				if (clientAccessRight.getInvestAddEdit() != null && clientAccessRight.getInvestAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("InvestAddEdit");
				}
				accessRightDTO.setAccessRights(roleList);
				}
				return accessRightDTO;
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}


	
	//For advisor user
	@Override
	public UserDTO findModuleByUserId(int userID) throws RuntimeException {

		try {
			
			User user = userRepository.findOne(userID);
			UserDTO userDTO = mapper.map(user, UserDTO.class);
			List<String> roleList = new ArrayList<String>();

			/*
			 * if (user.getAdvisorAdmin() != null &&
			 * user.getAdvisorAdmin().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
			 * roleList.add("AdvisorAdmin"); }
			 */

			if ((user.getClientInfoView() != null && user.getClientInfoView().equalsIgnoreCase(MODULE_ACCCESS_YES)) || (user.getClientInfoAddEdit() != null && user.getClientInfoAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES) ) || (user.getClientInfoDelete()!= null && user.getClientInfoDelete().equalsIgnoreCase(MODULE_ACCCESS_YES))) {
				roleList.add("ClientInfo");
			}

			if (user.getBudgetManagementView() != null && user.getBudgetManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
				roleList.add("BudgetManagement");
			}

			if ((user.getGoalPlanningView() != null && user.getGoalPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)) || (user.getClientInfoAddEdit() != null && user.getGoalPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES) )) {
				roleList.add("GoalPlanning");
			}

			if ((user.getPortfolioManagementView() != null && user.getPortfolioManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES) && (user.getPortfolioManagementAddEdit() != null && user.getPortfolioManagementAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)))) {
				roleList.add("PortfolioManagement");
			}

			if ((user.getFinancialPlanningView() != null && user.getFinancialPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)  && ((user.getFinancialPlanningAddEdit() != null && user.getFinancialPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES))))){
				roleList.add("FinancialPlanning");
			}

			if (user.getUserManagementView() != null && user.getUserManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES) || (user.getUserManagementAddEdit() != null && user.getUserManagementAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) || (user.getUserManagementDelete() != null && user.getUserManagementDelete().equalsIgnoreCase(MODULE_ACCCESS_YES))){
				roleList.add("UserManagement");
			}

			if ((user.getClientRecordsView() != null && user.getClientRecordsView().equalsIgnoreCase(MODULE_ACCCESS_YES))) {
				roleList.add("ClientRecords");
			}

			if ((user.getMastersView() != null && user.getMastersView().equalsIgnoreCase(MODULE_ACCCESS_YES)) || ((user.getMastersAddEdit() != null && user.getMastersAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES))) || ((user.getMastersDelete() != null && user.getMastersDelete().equalsIgnoreCase(MODULE_ACCCESS_YES)))) {
				roleList.add("Masters");
			}

			if ((user.getMfBackOfficeView() != null && user.getMfBackOfficeView().equalsIgnoreCase(MODULE_ACCCESS_YES)) || (user.getMfBackOfficeAddEdit() != null && user.getMfBackOfficeAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES))) {
				roleList.add("MFBackOffice");
			}

			/*
			 * if (user.getBseMFView() != null &&
			 * user.getBseMFView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
			 * roleList.add("BSEMF"); }
			 */
			if ((user.getInvestView() != null && user.getInvestView().equalsIgnoreCase(MODULE_ACCCESS_YES) || (user.getInvestAddEdit() != null && user.getInvestAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)))) {
				roleList.add("Invest");
			}
			userDTO.setRoles(roleList);
			return userDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	
	
	
	// get User and role for advisor admin
	@Override
	public List<UserDTO> findUserAndRoleByUserId(int userId) throws RuntimeException {
		List<UserDTO> userDTOList = new ArrayList<UserDTO>();
		User user = userRepository.findOne(userId);
		AdvisorUser au = user.getAdvisorUser();
		List<String> listRoles = new ArrayList<String>();
		List<AdvisorUserRoleMapping> roleMappings = au.getAdvisorUserRoleMappings();
		for (AdvisorUserRoleMapping advisorUserRoleMapping : roleMappings) {
			UserDTO userDTO = new UserDTO();
			userDTO.setUserName(advisorUserRoleMapping.getAdvisorUser().getFirstName() + " "
					+ (advisorUserRoleMapping.getAdvisorUser().getMiddleName() == null ? " "
							: advisorUserRoleMapping.getAdvisorUser().getMiddleName())
					+ " " + advisorUserRoleMapping.getAdvisorUser().getLastName());
			listRoles.add(advisorUserRoleMapping.getAdvisorRole().getRoleDescription());
			userDTO.setRoles(listRoles);
			userDTOList.add(userDTO);
		}
		return userDTOList;
	}

	@Override
	public List<UserDTO> getAllUserForSelectedRole(int masterID, int roleId) throws RuntimeException {
		List<UserDTO> userDTOList = new ArrayList<UserDTO>();
		UserDTO userdto = null;
		try {
			AdvisorMaster master = advisorMasterRepository.findOne(masterID);
			AdvisorRole role = advisorRoleRepository.findOne(roleId);
			List<User> userList = userRepository.findByAdvisorMasterAndAdvisorRole(master, role);
			for (User user : userList) {
				 userdto = new UserDTO();
				
//				  System.out.println("user "+user); 
//				  System.out.println("user.getId() "+user.getId());
//				  System.out.println("user.getAdvisorUser() "+user.getAdvisorUser());
//				  System.out.println("user.getAdvisorUser().getId() "+user.getAdvisorUser().getId());
				 
				userdto.setId(user.getAdvisorUser().getId());
				userdto.setUserID(user.getId());
				userdto.setLoginUsername(user.getAdvisorUser().getLoginUsername());
				userdto.setUserName(user.getAdvisorUser().getFirstName() + " "
						+ (user.getAdvisorUser().getMiddleName() == null ? " " : user.getAdvisorUser().getMiddleName())
						+ " " + user.getAdvisorUser().getLastName());
				userDTOList.add(userdto);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return userDTOList;

	}
	
	public String getExistingRoleForOrg(int masterID, int roleID) throws RuntimeException {
		
		 try {
			 int flag = 0;
			 String msg = "";
			 AdvisorMaster advisorMaster = advisorMasterRepository.findOne(masterID);
			 AdvisorRole role = advisorRoleRepository.findOne(roleID);
		     LookupRole lookupRole = lookupRoleRepository.findById((byte) role.getId());
		     String roleDescription = lookupRole.getDescription();
		     AdvisorRole advisorRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription( advisorMaster,roleDescription);
		       if(advisorRole != null) {
					  flag = 1;
					  }
					if (flag == 1) {
						msg = "role exists";
					}
					return msg;

			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}
	
	@Override
	public boolean checkUniqueRole(int masterID, String role) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			AdvisorMaster advisorMaster = advisorMasterRepository.findOne(masterID);
			AdvisorRole ar = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(advisorMaster,role);
			return (ar != null) ? false : true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean checkSupervisor(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			AdvisorUser advUser = advisorUserRepository.findOne(userId);
	        List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList = advUser.getAdvisorUserSupervisorMappings2();
	        return (!advisorUserSupervisorParentList.isEmpty() && advisorUserSupervisorParentList != null) ? false : true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean checkRoleUser(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			AdvisorUser advUser = advisorUserRepository.findOne(userId);
	        List<AdvisorUserSupervisorMapping> advisorUserSupervisorList = advUser.getAdvisorUserSupervisorMappings1();
	        return (!advisorUserSupervisorList.isEmpty() && advisorUserSupervisorList != null) ? false : true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean checkSupervisorMapping(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			AdvisorUser advUser = advisorUserRepository.findOne(userId);
	        List<AdvisorUserSupervisorMapping> advisorUserSupervisorMappingList = advUser.getAdvisorUserSupervisorMappings1();
	        return (!advisorUserSupervisorMappingList.isEmpty() && advisorUserSupervisorMappingList != null) ? false : true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * @Override public UserDTO findUserAndRoleByOrgName(int orgName) throws
	 * RuntimeException { // TODO Auto-generated method stub AdvisorMaster am =
	 * advisorMasterRepository.findOne(orgName); UserDTO userDTO = new UserDTO();
	 * List<AdvisorRole> roleList = am.getAdvisorRoles(); List<AdvisorRole>
	 * listRoles = new ArrayList<AdvisorRole>(); List<AdvisorUser> listUsers = new
	 * ArrayList<AdvisorUser>(); for(AdvisorRole role : roleList) {
	 * listRoles.add(role); userDTO.setAdvisorRoles(listRoles);
	 * 
	 * List<User> securityUsers = userRepository.findByAdvisorRole(role);
	 * 
	 * for(User user : securityUsers) { if(user.getAvisorUser() != null) {
	 * listUsers.add(user.getAvisorUser().getFirstName() + " " +
	 * (user.getAvisorUser().getMiddleName() == null ? " " :
	 * user.getAvisorUser().getMiddleName()) + " " +
	 * user.getAvisorUser().getLastName());
	 * 
	 * listUsers.add(user.getAvisorUser()); userDTO.setAdvisorUsers(listUsers); } }
	 * 
	 * }
	 * 
	 * return userDTO; }
	 */

	// get all modules for Finlabs admin for Access Rights
		@Override
		public List<AccessRightDTO> getAllModules(String orgFlag) throws RuntimeException {
			// TODO Auto-generated method stub
			List<AccessRightDTO> accessRightDTOList = new ArrayList<>();
			AccessRightDTO accessRightDTO = null;
			try {
				
				List<UserRole> finexaBusinessModuleList = userRoleRepository.findAll();
				for (UserRole obj : finexaBusinessModuleList) {
					accessRightDTO = new AccessRightDTO();
					accessRightDTO.setId(obj.getId());
					accessRightDTO.setAccessRight(obj.getRoleDescription());
					if(orgFlag.equalsIgnoreCase("N")){
					 if((!accessRightDTO.getAccessRight().equals("UserManagement")) && (!accessRightDTO.getAccessRight().equals("Masters")))
					accessRightDTOList.add(accessRightDTO);
					}else {
					accessRightDTOList.add(accessRightDTO);	
					}
				}
				return accessRightDTOList;
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}


	@Override
	public UserDTO accessRights(UserDTO userDTO) throws RuntimeException {
		try {

			//AdvisorMaster master = advisorMasterRepository.findOne(userDTO.getMasterID());
			User user = userRepository.findOne(userDTO.getId());
			
			//AdvisorRole role = advisorRoleRepository.findOne(userDTO.getRoleId());
			//User usr = userRepository.findOne(userDTO.getId());
			// if (userDTO.getOrgFlag().equals(ORGANIZATION_FLAG_YES)) {
			//User user = mapper.map(userDTO, User.class);
			//user.setAdvisorRole(role);
			//user.setAdvisorMaster(master);
			user = getAccessRights(user, userDTO);
			userRepository.save(user);

			// This is for organization with hierarchy of Users
			/* 
			 * List<User> userList =
			 * userRepository.findByAdvisorMasterAndAdvisorRole(master, role); for (User u :
			 * userList) { if (advUser.getUser().getId() == u.getId()) { u =
			 * getAccessRights(u,userDTO); // update the record userRepository.save(u); } }
			 */
			/*
			 * } else { // For Self List<User> userList = userRepository.findAll(); for
			 * (User u : userList) { if (advUser.getUser().getId() == u.getId()) { u =
			 * getAccessRights(u,userDTO); // update the record userRepository.save(u);
			 * 
			 * } } }
			 */

			return userDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public UserDTO accessRightsForClient(UserDTO userDTO) throws RuntimeException {
		try {

			//AdvisorMaster master = advisorMasterRepository.findOne(userDTO.getMasterID());
			ClientMaster cm = clientMasterRepository.findOne(userDTO.getClientID());
		    ClientAccessRight clientAccessRights = cm.getClientAccessRight();
			System.out.println(clientAccessRights);
			if(clientAccessRights == null) {
				clientAccessRights = new ClientAccessRight();
				clientAccessRights.setClientMaster(cm);
			}else {
				System.out.println(clientAccessRights.getId());
			}
		    clientAccessRights = getAccessRightsForClient(clientAccessRights, userDTO);
		    clientAccessRightsRepository.save(clientAccessRights);

			// This is for organization with hierarchy of Users
			/* 
			 * List<User> userList =
			 * userRepository.findByAdvisorMasterAndAdvisorRole(master, role); for (User u :
			 * userList) { if (advUser.getUser().getId() == u.getId()) { u =
			 * getAccessRights(u,userDTO); // update the record userRepository.save(u); } }
			 */
			/*
			 * } else { // For Self List<User> userList = userRepository.findAll(); for
			 * (User u : userList) { if (advUser.getUser().getId() == u.getId()) { u =
			 * getAccessRights(u,userDTO); // update the record userRepository.save(u);
			 * 
			 * } } }
			 */

			return userDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	// For Advisor Admin role access
	@Override
	public UserDTO advisorAdminAccessRights(UserDTO userDTO) throws RuntimeException {
		try {

			//AdvisorMaster master = advisorMasterRepository.findOne(userDTO.getMasterID());
			//AdvisorRole role = advisorRoleRepository.findOne(userDTO.getRoleId());
			User usr = userRepository.findOne(userDTO.getId());
			//User user = mapper.map(userDTO, User.class);
			//user.setAdvisorRole(role);
			//user.setAdvisorMaster(master);
			User user = getAccessRights(usr, userDTO);
			userRepository.save(user);
			// This is for organization with hierarchy of Users
			/*
			 * List<User> userList =
			 * userRepository.findByAdvisorMasterAndAdvisorRole(master, role); for (User u :
			 * userList) { if (advUser.getUser().getId() == u.getId()) { u =
			 * getAccessRights(u,userDTO); // update the record userRepository.save(u); } }
			 */

			return userDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private User getAccessRights(User u, UserDTO userDTO) {
		// TODO Auto-generated method stub
		/*
		 * if (userDTO.getAdvisorAdmin() != null &&
		 * userDTO.getAdvisorAdmin().equals(MODULE_ACCCESS_YES)) {
		 * u.setAdvisorAdmin(MODULE_ACCCESS_YES); } else {
		 * u.setAdvisorAdmin(MODULE_ACCCESS_NO); }
		 */
		if (userDTO.getClientInfoView() != null && userDTO.getClientInfoView().equals(MODULE_ACCCESS_YES)) {
			u.setClientInfoView(MODULE_ACCCESS_YES);
		} else {
			u.setClientInfoView(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getClientInfoAddEdit() != null && userDTO.getClientInfoAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setClientInfoAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setClientInfoAddEdit(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getClientInfoDelete() != null && userDTO.getClientInfoDelete().equals(MODULE_ACCCESS_YES)) {
			u.setClientInfoDelete(MODULE_ACCCESS_YES);
		} else {
			u.setClientInfoDelete(MODULE_ACCCESS_NO);
		}

		if (userDTO.getBudgetManagementView() != null && userDTO.getBudgetManagementView().equals(MODULE_ACCCESS_YES)) {
			u.setBudgetManagementView(MODULE_ACCCESS_YES);
		} else {
			u.setBudgetManagementView(MODULE_ACCCESS_NO);
		}

		if (userDTO.getGoalPlanningView() != null && userDTO.getGoalPlanningView().equals(MODULE_ACCCESS_YES)) {
			u.setGoalPlanningView(MODULE_ACCCESS_YES);
		} else {
			u.setGoalPlanningView(MODULE_ACCCESS_NO);
		}

		if (userDTO.getGoalPlanningAddEdit() != null && userDTO.getGoalPlanningAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setGoalPlanningAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setGoalPlanningAddEdit(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getPortfolioManagementView() != null && userDTO.getPortfolioManagementView().equals(MODULE_ACCCESS_YES)) {
			u.setPortfolioManagementView(MODULE_ACCCESS_YES);
		} else {
			u.setPortfolioManagementView(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getPortfolioManagementAddEdit() != null && userDTO.getPortfolioManagementAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setPortfolioManagementAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setPortfolioManagementAddEdit(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getFinancialPlanningView() != null && userDTO.getFinancialPlanningView().equals(MODULE_ACCCESS_YES)) {
			u.setFinancialPlanningView(MODULE_ACCCESS_YES);
		} else {
			u.setFinancialPlanningView(MODULE_ACCCESS_NO);
		}

		if (userDTO.getFinancialPlanningAddEdit() != null && userDTO.getFinancialPlanningAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setFinancialPlanningAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setFinancialPlanningAddEdit(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getUserManagementView() != null && userDTO.getUserManagementView().equals(MODULE_ACCCESS_YES)) {
			u.setUserManagementView(MODULE_ACCCESS_YES);
		} else {
			u.setUserManagementView(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getUserManagementAddEdit() != null && userDTO.getUserManagementAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setUserManagementAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setUserManagementAddEdit(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getUserManagementDelete() != null && userDTO.getUserManagementDelete().equals(MODULE_ACCCESS_YES)) {
			u.setUserManagementDelete(MODULE_ACCCESS_YES);
		} else {
			u.setUserManagementDelete(MODULE_ACCCESS_NO);
		}

		if (userDTO.getClientRecordsView() != null && userDTO.getClientRecordsView().equals(MODULE_ACCCESS_YES)) {
			u.setClientRecordsView(MODULE_ACCCESS_YES);
		} else {
			u.setClientRecordsView(MODULE_ACCCESS_NO);
		}

		if (userDTO.getMastersView() != null && userDTO.getMastersView().equals(MODULE_ACCCESS_YES)) {
			u.setMastersView(MODULE_ACCCESS_YES);
		} else {
			u.setMastersView(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getMastersAddEdit() != null && userDTO.getMastersAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setMastersAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setMastersAddEdit(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getMastersDelete() != null && userDTO.getMastersDelete().equals(MODULE_ACCCESS_YES)) {
			u.setMastersDelete(MODULE_ACCCESS_YES);
		} else {
			u.setMastersDelete(MODULE_ACCCESS_NO);
		}

		/*
		 * if (userDTO.getBseMFView() != null &&
		 * userDTO.getBseMFView().equals(MODULE_ACCCESS_YES)) {
		 * u.setBseMFView(MODULE_ACCCESS_YES); } else {
		 * u.setBseMFView(MODULE_ACCCESS_NO); }
		 */

		if (userDTO.getMfBackOfficeView() != null && userDTO.getMfBackOfficeView().equals(MODULE_ACCCESS_YES)) {
			u.setMfBackOfficeView(MODULE_ACCCESS_YES);
		} else {
			u.setMfBackOfficeView(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getMfBackOfficeAddEdit() != null && userDTO.getMfBackOfficeAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setMfBackOfficeAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setMfBackOfficeAddEdit(MODULE_ACCCESS_NO);
		}

		if (userDTO.getInvestView() != null && userDTO.getInvestView().equals(MODULE_ACCCESS_YES)) {
			u.setInvestView(MODULE_ACCCESS_YES);
		} else {
			u.setInvestView(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getInvestAddEdit()!= null && userDTO.getInvestAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setInvestAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setInvestAddEdit(MODULE_ACCCESS_NO);
		}
		
		return u;
	}
	
	
	private ClientAccessRight getAccessRightsForClient(ClientAccessRight u, UserDTO userDTO) {
		// TODO Auto-generated method stub
		/*
		 * if (userDTO.getAdvisorAdmin() != null &&
		 * userDTO.getAdvisorAdmin().equals(MODULE_ACCCESS_YES)) {
		 * u.setAdvisorAdmin(MODULE_ACCCESS_YES); } else {
		 * u.setAdvisorAdmin(MODULE_ACCCESS_NO); }
		 */
		if (userDTO.getClientInfoView() != null && userDTO.getClientInfoView().equals(MODULE_ACCCESS_YES)) {
			u.setClientInfoView(MODULE_ACCCESS_YES);
		} else {
			u.setClientInfoView(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getClientInfoAddEdit() != null && userDTO.getClientInfoAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setClientInfoAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setClientInfoAddEdit(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getClientInfoDelete() != null && userDTO.getClientInfoDelete().equals(MODULE_ACCCESS_YES)) {
			u.setClientInfoDelete(MODULE_ACCCESS_YES);
		} else {
			u.setClientInfoDelete(MODULE_ACCCESS_NO);
		}

		if (userDTO.getBudgetManagementView() != null && userDTO.getBudgetManagementView().equals(MODULE_ACCCESS_YES)) {
			u.setBudgetManagementView(MODULE_ACCCESS_YES);
		} else {
			u.setBudgetManagementView(MODULE_ACCCESS_NO);
		}

		if (userDTO.getGoalPlanningView() != null && userDTO.getGoalPlanningView().equals(MODULE_ACCCESS_YES)) {
			u.setGoalPlanningView(MODULE_ACCCESS_YES);
		} else {
			u.setGoalPlanningView(MODULE_ACCCESS_NO);
		}

		if (userDTO.getGoalPlanningAddEdit() != null && userDTO.getGoalPlanningAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setGoalPlanningAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setGoalPlanningAddEdit(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getPortfolioManagementView() != null && userDTO.getPortfolioManagementView().equals(MODULE_ACCCESS_YES)) {
			u.setPortfolioManagementView(MODULE_ACCCESS_YES);
		} else {
			u.setPortfolioManagementView(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getPortfolioManagementAddEdit() != null && userDTO.getPortfolioManagementAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setPortfolioManagementAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setPortfolioManagementAddEdit(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getFinancialPlanningView() != null && userDTO.getFinancialPlanningView().equals(MODULE_ACCCESS_YES)) {
			u.setFinancialPlanningView(MODULE_ACCCESS_YES);
		} else {
			u.setFinancialPlanningView(MODULE_ACCCESS_NO);
		}

		if (userDTO.getFinancialPlanningAddEdit() != null && userDTO.getFinancialPlanningAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setFinancialPlanningAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setFinancialPlanningAddEdit(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getMfBackOfficeView() != null && userDTO.getMfBackOfficeView().equals(MODULE_ACCCESS_YES)) {
			u.setMfBackOfficeView(MODULE_ACCCESS_YES);
		} else {
			u.setMfBackOfficeView(MODULE_ACCCESS_NO);
		}

		if (userDTO.getInvestView() != null && userDTO.getInvestView().equals(MODULE_ACCCESS_YES)) {
			u.setInvestView(MODULE_ACCCESS_YES);
		} else {
			u.setInvestView(MODULE_ACCCESS_NO);
		}
		
		if (userDTO.getInvestAddEdit()!= null && userDTO.getInvestAddEdit().equals(MODULE_ACCCESS_YES)) {
			u.setInvestAddEdit(MODULE_ACCCESS_YES);
		} else {
			u.setInvestAddEdit(MODULE_ACCCESS_NO);
		}
		
		return u;
	}



	// for user management work
	@Override
	public UserDTO getAllRolesForSelectedOrg(int masterID) throws RuntimeException {
		   UserDTO userDTO = null;
		try {
			AdvisorMaster master = advisorMasterRepository.findOne(masterID);
			List<AdvisorRole> list = master.getAdvisorRoles();
			for (AdvisorRole advisorRole : list) {
				if(advisorRole.getRoleDescription().equalsIgnoreCase("Admin")) {
				userDTO = new UserDTO();
				userDTO.setRoleId(advisorRole.getId());
				userDTO.setRoleDescription(advisorRole.getRoleDescription());
				break;
				}
			}
			return userDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	public int generateDemoLogin(int userID, InputStream resource, List<Integer> questionID) {

		int k, clientID = 0;
		try {
			System.out.println("UserID " + userID);
			ObjectMapper m = new ObjectMapper();
			ClientInfo clientInfo = m.readValue(resource, ClientInfo.class);

			// insert clientMaster
			clientInfo.clientMaster.get(0).setUserId(userID);
			String pan = RandomGenerate.getPan();
			clientInfo.clientMaster.get(0).setPan(pan);
			long aadhar = RandomGenerate.getAadhar();
			clientInfo.clientMaster.get(0).setAadhar(aadhar + "");
			//clientInfo.clientMaster.get(0).setCreatedOn(new Date());
			ClientMaster clientMaster = clientMasterService.autoSave(clientInfo.clientMaster.get(0));
			System.out.println("clientid " + clientMaster.getId());

			// insert clientContact
			clientInfo.clientContact.get(0).setClientId(clientMaster.getId());
			ClientContact clientContact = clientContactService.autoSave(clientInfo.clientContact.get(0));
			
			//set login username and password
			clientMaster.setLoginUsername(clientContact.getEmailID());
			String generatedString = RandomStringUtils.randomAlphabetic(6);
			clientMaster.setLoginPassword(generatedString);
			clientMaster.setCreatedOn(new Date());
			
			//set access rights
			ClientAccessRight clientAccessRight = new ClientAccessRight();
			clientAccessRight.setClientMaster(clientMaster);
			clientAccessRight.setClientInfoView("Y");
			clientAccessRight.setClientInfoAddEdit("N");
			clientAccessRight.setClientInfoDelete("N");
			clientAccessRight.setBudgetManagementView("N");
			clientAccessRight.setGoalPlanningView("N");
			clientAccessRight.setGoalPlanningAddEdit("N");
			clientAccessRight.setPortfolioManagementView("N");
			clientAccessRight.setPortfolioManagementAddEdit("N");
			clientAccessRight.setFinancialPlanningView("N");
			clientAccessRight.setFinancialPlanningAddEdit("N");
			clientAccessRight.setInvestView("N");
			clientAccessRight.setInvestAddEdit("N");
			clientAccessRight.setMfBackOfficeView("N");
			clientAccessRight.setCreatedOn(new Date());
			clientAccessRightsRepository.save(clientAccessRight);

			// insert clientFamily
			int i = 0;
			ClientFamilyMemberDTO[] clientFamilyMemberDTO = new ClientFamilyMemberDTO[2];
			for (ClientFamilyMemberDTO cf : clientInfo.clientFamilyMember) {
				cf.setClientID(clientMaster.getId());
				clientFamilyMemberDTO[i] = clientFamilyMemberService.autoSave(cf);
				System.out.println("memberID " + clientFamilyMemberDTO[i].getId());
				i++;
			}

			//insert family income
			
			//int fi=0;
			k = 1;
			//ClientFamilyIncomeDTO[] clientFamilyIncomeDTO = new ClientFamilyIncomeDTO[3];
			for(ClientFamilyIncomeDTO cf:clientInfo.clientFamilyIncome) {
				
				cf.setClientId(clientMaster.getId());
				cf.setFamilyMemberId(clientFamilyMemberDTO[k].getId());
				//cf.setFamilyMemberId(clientMasterDTO.getFamilyMemberId());
				clientFamilyIncomeService.autoSave(cf);
				//fi++;
			}

			//insert client expense

			//int expenseIndex=0;
			//ClientExpenseDTO[] clientExpenseDTO = new ClientExpenseDTO[3];
			for(ClientExpenseDTO cf:clientInfo.clientExpense) {
				
				cf.setClientId(clientMaster.getId());
				clientExpenseService.autoSave(cf);
				//expenseIndex++;
			}

			//insert client Goal
			i=0;
			ClientGoalDTO[] clientGoalDTO = new ClientGoalDTO[3];
			for(ClientGoalDTO cf:clientInfo.clientGoal) {
				
				cf.setClientId(clientMaster.getId());
				cf.setClientFamilyMemberId(clientFamilyMemberDTO[k].getId());
				clientGoalDTO[i] = clientGoalService.save(cf);
				i++;
			}

			//insert client NonLife
			i=0;
			ClientNonlifeInsuranceDTO[] clientNonlifeInsuranceDTO = new ClientNonlifeInsuranceDTO[2];
			for(ClientNonlifeInsuranceDTO cf:clientInfo.clientNonLifeInsurance) {
				
				cf.setClientID(clientMaster.getId());
				cf.setFamilyMemberID(clientFamilyMemberDTO[k].getId());
				
				ClientFloaterCoverDTO clientFloaterCoverDTO = new ClientFloaterCoverDTO();
				List<Integer> checkedFamilyMemberId = new ArrayList<Integer>();
				for( ClientFamilyMemberDTO id : clientFamilyMemberDTO) {
					checkedFamilyMemberId.add(id.getId());
				}
				clientFloaterCoverDTO.setCheckedFamilyMemberID(checkedFamilyMemberId);
				
				clientNonlifeInsuranceDTO[i] = clientNonLifeInsuranceService.autoSave(cf, clientFloaterCoverDTO);
				i++;
			}
			//insert client Life
			i=0;
			ClientLifeInsuranceDTO[] clientLifeInsuranceDTO = new ClientLifeInsuranceDTO[1];
			for(ClientLifeInsuranceDTO cf:clientInfo.clientLifeInsurance) {
				
				cf.setClientID(clientMaster.getId());
				cf.setFamilyMemberID(clientFamilyMemberDTO[k].getId());
				clientLifeInsuranceDTO[i] = clientLifeInsuranceService.autoSave(cf);
				i++;
			}

			//insert client Loan
			i=0;
			ClientLoanDTO[] clientLoanDTO = new ClientLoanDTO[3];
			for(ClientLoanDTO cf:clientInfo.clientLoan) {
				cf.setClientID(clientMaster.getId());
				cf.setFamilyMemberId(clientFamilyMemberDTO[k].getId());
				clientLoanDTO[i] = clientLoanService.autoSave(cf);
				i++;
			}

			//insert client MF
			//i=0;
			ClientMutualFundDTO[] clientMutualFundDTO = new ClientMutualFundDTO[1];
			for(ClientMutualFundDTO cf:clientInfo.clientMutualFund) {
				cf.setClientID(clientMaster.getId());
				cf.setFamilyMemberId(clientFamilyMemberDTO[k].getId());
				clientFundService.autoSave(cf);
				//i++;
			}

			//insert client equity
			//i=0;
			//ClientEquityDTO[] clientEquityDTO = new ClientEquityDTO[1];
			for(ClientEquityDTO cf:clientInfo.clientEquity) {
				cf.setClientID(clientMaster.getId());
				cf.setFamilyMemberId(clientFamilyMemberDTO[k].getId());
				clientEquityService.autoSave(cf);
				//i++;
			}
			
			//insert clientFixedIncome
			//i=0;
			//ClientFixedIncomeDTO[] clientFixedIncomeDTO = new ClientFixedIncomeDTO[1];
			for(ClientFixedIncomeDTO cf:clientInfo.clientFixedIncome) {
				cf.setClientID(clientMaster.getId());
				cf.setFamilyMemberID(clientFamilyMemberDTO[k].getId());
				clientFixedIncomeService.autoSave(cf);
				//i++;
			}
			
			//insert client PPF
			//i=0;
			//ClientPpfDTO[] clientPpfDTO = new ClientPpfDTO[1];
			for(ClientPpfDTO cf:clientInfo.clientPPF) {
				cf.setClientID(clientMaster.getId());
				cf.setFamilyMemberID(clientFamilyMemberDTO[k].getId());
				clientPPFService.autoSave(cf);
				//i++;
			}

			//insert client Real Estate
			//i=0;
			//ClientRealEstateDTO[] clientRealEstateDTO = new ClientRealEstateDTO[1];
			for(ClientRealEstateDTO cf:clientInfo.clientRealEstate) {
				cf.setClientID(clientMaster.getId());
				cf.setFamilyMemberID(clientFamilyMemberDTO[k].getId());
				clientRealEstateService.autoSave(cf);
				//i++;
			}
			
			//insert client small savings
			//i=0;
			//ClientSmallSavingsDTO[] clientSmallSavingsDTO = new ClientSmallSavingsDTO[1];
			for(ClientSmallSavingsDTO cf:clientInfo.clientSmallSaving) {
				cf.setClientID(clientMaster.getId());
				cf.setFamilyMemberID(clientFamilyMemberDTO[k].getId());
				 clientSmallSavingsService.autoSave(cf);
				//i++;
			}

			//insert client Cash
			//i=0;
			//ClientCashDTO[] clientCashDTO = new ClientCashDTO[1];
			for(ClientCashDTO cf:clientInfo.clientCash) {
				cf.setClientId(clientMaster.getId());
				cf.setFamilyMemberId(clientFamilyMemberDTO[k].getId());
				clientCashService.autoSave(cf);
				//i++;
			}

			//insert  clientLumpsumInflow
			//i=0;
			//ClientLumpsumDTO[] clientLumpsumDTO = new ClientLumpsumDTO[1];
			for(ClientLumpsumDTO cf:clientInfo.clientLumpsumInflow) {
				cf.setClientId(clientMaster.getId());
				clientLumpsumService.autoSave(cf);
				//i++;
			}
			
			//insert clientRiskProfileResponse
			//i=0;
			//ClientRiskProfileResponseDTO[] clientRiskProfileResponseDTO = new ClientRiskProfileResponseDTO[12];
			int q = 0;
			for(ClientRiskProfileResponseDTO cf:clientInfo.clientRiskProfileResponse) {

				cf.setClientId(clientMaster.getId());
				System.out.println("questionID.get(q) "+questionID.get(q));
			    cf.setQuestionId(questionID.get(q));
				clientRiskProfileService.autoSave(cf);
				q++;
			}
			clientID = clientMaster.getId();
			//System.out.println("ClientID*******************" + clientMasterDTO.getId());

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return clientID;
	}


	@Override
	public List<UserDTO> getAllUserForSelectedSupervisorRole(int masterID, String roleDescription) throws RuntimeException {
		List<UserDTO> userDTOList = new ArrayList<UserDTO>();
		UserDTO userdto = new UserDTO();
		try {
			System.out.println("ROLE" +roleDescription);
			if(roleDescription.equals("Not Applicable")) {
				
			}else {
				AdvisorMaster master = advisorMasterRepository.findOne(masterID);
				AdvisorRole role= advisorRoleRepository.findByAdvisorMasterAndRoleDescription(master,roleDescription);
				
				List<User> userList = role.getUser();
				for (User user : userList) {
					userdto = mapper.map(user, UserDTO.class);
					/*
					 * System.out.println(user); System.out.println(user.getId());
					 * System.out.println(user.getAvisorUser());
					 * System.out.println(user.getAvisorUser().getId());
					 */
					userdto.setId(user.getAdvisorUser().getId());
					userdto.setUserID(user.getId());
					userdto.setUserName(user.getAdvisorUser().getFirstName() + " "
							+ (user.getAdvisorUser().getMiddleName() == null ? " " : user.getAdvisorUser().getMiddleName())
							+ " " + user.getAdvisorUser().getLastName());
					userDTOList.add(userdto);
				}
			}
            
		} catch (RuntimeException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return userDTOList;

	}
	
	
	
	//------------------------------------------------------------------------
	
		@Override
		public boolean checkUniqueOrganisationName(String orgName) throws RuntimeException {
			// TODO Auto-generated method stub
			try {
				System.out.println("orgName " + orgName);
				AdvisorMaster ad = advisorMasterRepository.findByOrgName(orgName);
				System.out.println("ad " + ad);
				return (ad != null) ? false : true;
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}



		@Override
		public List<AdvisorUser> checkUniqueEmpCodeForFixedMaster(String orgName, String distCode, String empCode) {
			// TODO Auto-generated method stub
			List <AdvisorUser> advisorUser = null;
			AdvisorMaster mas = advisorMasterRepository.findByOrgNameAndDistributorCode(orgName, distCode);
			System.out.println(mas);
			if(mas != null) {
				advisorUser = advisorUserRepository.findByAdvisorMasterAndEmployeeCode(mas, empCode);
				System.out.println("advisor list length :" + advisorUser.size());
				for(AdvisorUser a : advisorUser) {
					System.out.println("advisor Id :" + a.getId());
					System.out.println("advisor Name :" + a.getFirstName() + " " + a.getLastName());
					System.out.println("advisor Emp Code :" + a.getEmployeeCode());
				}
			}
			return advisorUser;
		}
		@Override
		public List<AdvisorDTO> checkUniqueEmpCodeForFixedmaster(String orgName, String distCode, String empCode) {
			// TODO Auto-generated method stub
			List <AdvisorUser> advisorUser;
			List <AdvisorDTO> advisorDTOList = new ArrayList<AdvisorDTO>();
			AdvisorMaster mas = advisorMasterRepository.findByOrgNameAndDistributorCode(orgName, distCode);
			System.out.println(mas);
			if(mas != null) {
				advisorUser = advisorUserRepository.findByAdvisorMasterAndEmployeeCode(mas, empCode);
				
				AdvisorDTO advisorDTO;
				System.out.println("advisor list length :" + advisorUser.size());
				for(AdvisorUser a : advisorUser) {
					advisorDTO = mapper.map(advisorUser, AdvisorDTO.class);
					advisorDTOList.add(advisorDTO);
					
				/*
				 * System.out.println("advisor Id :" + a.getId());
				 * System.out.println("advisor Name :" + a.getFirstName() + " " +
				 * a.getLastName()); System.out.println("advisor Emp Code :" +
				 * a.getEmployeeCode());
				 */
				}
			}
			return advisorDTOList;
		}
		//For advisor user
		@Override
		public AccessRightDTO findExistingModuleByUserId(int userID) throws RuntimeException {

			try {
				
				User user = userRepository.findOne(userID);
				//UserDTO userDTO = mapper.map(user, UserDTO.class);
				AccessRightDTO accessRightDTO = new AccessRightDTO();
				List<String> roleList = new ArrayList<String>();

				if ((user.getClientInfoView() != null && user.getClientInfoView().equalsIgnoreCase(MODULE_ACCCESS_YES))) {
					roleList.add("ClientInfoView");
				}
				if (user.getClientInfoAddEdit() != null && user.getClientInfoAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("ClientInfoAddEdit");
				}
				if (user.getClientInfoDelete()!= null && user.getClientInfoDelete().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("ClientInfoDelete");
				}

				if (user.getBudgetManagementView() != null && user.getBudgetManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("BudgetManagementView");
				}

				if (user.getGoalPlanningView() != null && user.getGoalPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("GoalPlanningView");
				}
				if (user.getGoalPlanningAddEdit() != null && user.getGoalPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("GoalPlanningAddEdit");
				}

				if (user.getPortfolioManagementView() != null && user.getPortfolioManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("PortfolioManagementView");
				}
				if (user.getPortfolioManagementAddEdit() != null && user.getPortfolioManagementAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("PortfolioManagementAddEdit");
				}

				if (user.getFinancialPlanningView() != null && user.getFinancialPlanningView().equalsIgnoreCase(MODULE_ACCCESS_YES)){
					roleList.add("FinancialPlanningView");
				}
				if (user.getFinancialPlanningAddEdit() != null && user.getFinancialPlanningAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)){
					roleList.add("FinancialPlanningAddEdit");
				}

				if (user.getUserManagementView() != null && user.getUserManagementView().equalsIgnoreCase(MODULE_ACCCESS_YES)){
					roleList.add("UserManagementView");
				}
				if (user.getUserManagementAddEdit() != null && user.getUserManagementAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)){
					roleList.add("UserManagementAddEdit");
				}
				if (user.getUserManagementDelete() != null && user.getUserManagementDelete().equalsIgnoreCase(MODULE_ACCCESS_YES)){
					roleList.add("UserManagementDelete");
				}

				if (user.getClientRecordsView() != null && user.getClientRecordsView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("ClientRecordsView");
				}

				if (user.getMastersView() != null && user.getMastersView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("MastersView");
				}
				if (user.getMastersAddEdit() != null && user.getMastersAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("MastersAddEdit");
				}
				if (user.getMastersDelete() != null && user.getMastersDelete().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("MastersDelete");
				}

				if (user.getMfBackOfficeView() != null && user.getMfBackOfficeView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("MFBackOfficeView");
				}
				if (user.getMfBackOfficeAddEdit() != null && user.getMfBackOfficeAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("MFBackOfficeAddEdit");
				}

			/*
			 * if (user.getBseMFView() != null &&
			 * user.getBseMFView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
			 * roleList.add("BSEMFView"); }
			 */

				if (user.getInvestView() != null && user.getInvestView().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("InvestView");
				}
				if (user.getInvestAddEdit() != null && user.getInvestAddEdit().equalsIgnoreCase(MODULE_ACCCESS_YES)) {
					roleList.add("InvestAddEdit");
				}
				//userDTO.setRoles(roleList);
				accessRightDTO.setAccessRights(roleList);
				return accessRightDTO;
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}



		@Override
		public boolean checkUniqueDistributorCode(String distributorCode) throws RuntimeException {
			// TODO Auto-generated method stub
						try {
							//System.out.println("distributorCode " + distributorCode);
							AdvisorMaster ad = advisorMasterRepository.findByDistributorCode(distributorCode);
							//System.out.println("ad " + ad);
							return (ad != null) ? false : true;
						} catch (RuntimeException e) {
							// TODO Auto-generated catch block
							throw new RuntimeException(e);
						}
		}



		@Override
		public List<ViewUserManagmentDTO> getUnsupervisedUserList(int userId) throws RuntimeException {
			try {
				List<ViewUserManagmentDTO> viewUserManagmentDTOList = new ArrayList<>();
				AdvisorMaster advisorMaster = advisorUserRepository.findOne(userId).getAdvisorMaster();
				List<AdvisorUser> viewUserManagmentList = advisorMaster.getAdvisorUsers();

				for (AdvisorUser obj : viewUserManagmentList) {
					
					if (obj.getId() != userId) {
					/*
					 * try { if(obj.getUser().getId() == 0) continue; }
					 * catch(EntityNotFoundException e) { continue; }
					 */
						//------check if already supervised----//
						//author Arghya
						AdvisorUserSupervisorMapping advisorUserSupervisorMapping = advisorUserSupervisorMappingRepository.findByAdvisorUser1(obj.getId());
						
						if (advisorUserSupervisorMapping == null) {
						
							//------check if advisor admin----//
							//author Arghya
							if (obj.getUser().getAdvisorAdmin() ==  null || obj.getUser().getAdvisorAdmin().equalsIgnoreCase("n")) {
							
								ViewUserManagmentDTO viewUserManagmentDTO = new ViewUserManagmentDTO();
								viewUserManagmentDTO.setId(obj.getId());
								// populating Name
								viewUserManagmentDTO.setUserName(obj.getFirstName() + " "
										+ (obj.getMiddleName() == null ? " " : obj.getMiddleName()) + " " + obj.getLastName());
								// populating Location
								viewUserManagmentDTO.setUserLocation(obj.getCity());
								
								viewUserManagmentDTO.setUserID(obj.getUser().getId());
								
								viewUserManagmentDTO.setEmailID(obj.getEmailID());
								/*****************************
								 * 0 is default index
								 *******************************/
								if (obj.getAdvisorUserRoleMappings().size() > 0) {
									AdvisorUserRoleMapping userRoleMapping = obj.getAdvisorUserRoleMappings().get(0);
									viewUserManagmentDTO.setUserRole(userRoleMapping.getAdvisorRole().getRoleDescription());
									viewUserManagmentDTO.setUserRoleId(userRoleMapping.getAdvisorRole().getId());
									AdvisorRole advRole = userRoleMapping.getAdvisorRole();
									if (advRole.getSupervisorRoleID() != null) {
										viewUserManagmentDTO.setSupervisorRoleId(advRole.getSupervisorRoleID());
										int superVisorRoleID = advRole.getSupervisorRoleID();
										LookupRole supervisorRole = lookupRoleRepository.findById((byte)superVisorRoleID);
										viewUserManagmentDTO.setSupervisorRoleName(supervisorRole.getDescription());
										
										/*
										 * viewUserManagmentDTO.setSupervisorRoleName(
										 * advisorRoleRepository.findOne(advRole.getSupervisorRoleID()).
										 * getRoleDescription());
										 */
									}else {
										viewUserManagmentDTO.setSupervisorRoleName("Not Applicable");
									}
								}
				
								viewUserManagmentDTOList.add(viewUserManagmentDTO);
							}
						}
					}
				}
				return viewUserManagmentDTOList;
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}



		@Override
		public List<ViewUserManagmentDTO> getAllUsersForClientContact(int userId) throws RuntimeException {
			try {
				List<ViewUserManagmentDTO> viewUserManagmentDTOList = new ArrayList<>();
				AdvisorMaster advisorMaster = advisorUserRepository.findOne(userId).getAdvisorMaster();
				List<AdvisorUser> viewUserManagmentList = advisorMaster.getAdvisorUsers();

				for (AdvisorUser obj : viewUserManagmentList) {

					ViewUserManagmentDTO viewUserManagmentDTO = new ViewUserManagmentDTO();
					viewUserManagmentDTO.setId(obj.getId());
					// populating Name
					viewUserManagmentDTO.setUserName(obj.getFirstName() + " "
							+ (obj.getMiddleName() == null ? " " : obj.getMiddleName()) + " " + obj.getLastName());
					// populating Location
					viewUserManagmentDTO.setUserLocation(obj.getCity());

					viewUserManagmentDTO.setUserID(obj.getUser().getId());

					viewUserManagmentDTO.setEmailID(obj.getEmailID());
					/*****************************
					 * 0 is default index
					 *******************************/
					if (obj.getAdvisorUserRoleMappings().size() > 0) {
						AdvisorUserRoleMapping userRoleMapping = obj.getAdvisorUserRoleMappings().get(0);
						viewUserManagmentDTO.setUserRole(userRoleMapping.getAdvisorRole().getRoleDescription());
						viewUserManagmentDTO.setUserRoleId(userRoleMapping.getAdvisorRole().getId());
						AdvisorRole advRole = userRoleMapping.getAdvisorRole();
						if (advRole.getSupervisorRoleID() != null) {
							viewUserManagmentDTO.setSupervisorRoleId(advRole.getSupervisorRoleID());
							int superVisorRoleID = advRole.getSupervisorRoleID();
							LookupRole supervisorRole = lookupRoleRepository.findById((byte) superVisorRoleID);
							viewUserManagmentDTO.setSupervisorRoleName(supervisorRole.getDescription());

							/*
							 * viewUserManagmentDTO.setSupervisorRoleName(
							 * advisorRoleRepository.findOne(advRole.getSupervisorRoleID()).
							 * getRoleDescription());
							 */
						} else {
							viewUserManagmentDTO.setSupervisorRoleName("Not Applicable");
						}
					}

					viewUserManagmentDTOList.add(viewUserManagmentDTO);

					// }
				}
				return viewUserManagmentDTOList;
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}

}