package com.finlabs.finexa.service;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.dto.AdvisorForgotPasswordDTO;
import com.finlabs.finexa.dto.AdvisorUserBulkUploadHistoryDTO;
import com.finlabs.finexa.dto.AdvisorUserLoginInfoDTO;
import com.finlabs.finexa.dto.ClientForgotPasswordDTO;
import com.finlabs.finexa.dto.ClientInfoDTO;
import com.finlabs.finexa.dto.ClientLoginInfoDTO;
import com.finlabs.finexa.dto.ClientMasterDTO;
import com.finlabs.finexa.dto.ClientUCCResultDTO;
import com.finlabs.finexa.dto.DashBoardDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.LoginDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.dto.UserDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.AdvisorForgotPassword;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserBulkUploadHistory;
import com.finlabs.finexa.model.AdvisorUserLoginInfo;
import com.finlabs.finexa.model.AdvisorUserRoleMapping;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientForgotPassword;
import com.finlabs.finexa.model.ClientLoginInfo;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.LookupTransactBSEAccessMode;
import com.finlabs.finexa.model.User;
import com.finlabs.finexa.model.UserClientRedis;
import com.finlabs.finexa.repository.AdvisorForgotPasswordRepository;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.AdvisorRoleRepository;
import com.finlabs.finexa.repository.AdvisorUserBulkUploadHistoryRepository;
import com.finlabs.finexa.repository.AdvisorUserLoginInfoRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserRoleMappingRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.ClientContactRepository;
import com.finlabs.finexa.repository.ClientForgotPasswordRepository;
import com.finlabs.finexa.repository.ClientLoginInfoRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.repository.LookupCountryRepository;
import com.finlabs.finexa.repository.LookupRoleRepository;
import com.finlabs.finexa.repository.LookupTransactBSEAccessModeRepository;
import com.finlabs.finexa.repository.UserRepository;
import com.finlabs.finexa.transact.MFUPLOADSoapService;
import com.finlabs.finexa.util.CacheInfoService;
import com.finlabs.finexa.util.Constants;
import com.finlabs.finexa.util.EmailUtil;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;
import com.finlabs.finexa.util.MFTransactConstant;
import com.finlabs.finexa.util.OffsetBasedPageRequest;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Service("AdvisorService")
@Transactional
public class AdvisorServiceImpl implements AdvisorService {
	private static Logger log = LoggerFactory.getLogger(AdvisorServiceImpl.class);

	@Autowired
	Mapper mapper;
	
	@Autowired
	AdvisorForgotPasswordRepository advisorForgorPasswordRepository;
	
	@Autowired
	ClientForgotPasswordRepository clientForgotPasswordRepository;

	@Autowired
	AdvisorMasterRepository advisorMasterRepository;

	@Autowired
	AdvisorUserRepository advisorUserRepository;

	@Autowired
	LookupCountryRepository lookupCountryRepository;

	@Autowired
	AdvisorRoleRepository advisorRoleRepository;

	@Autowired
	AdvisorUserRoleMappingRepository advisorUserRoleMappingRepository;

	@Autowired
	AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository;
	// @Autowired
	// private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	AdvisorForgotPasswordRepository advisorForgotPasswordRepository;
	@Autowired
	AdvisorUserLoginInfoRepository advisorUserLoginInfoRepository;

	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;

	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

	@Autowired
	EmailService emailService;

	@Autowired
	ClientMasterRepository clientMasterRepository;

	
	@Autowired
	LookupTransactBSEAccessModeRepository lookupTransactBSEAccessModeRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleService roleService;

	@Autowired
	ClientContactRepository clientContactRepository;
	
	@Autowired			
	LookupRoleRepository lookupRoleRepository;
	
	@Autowired			
	ClientLoginInfoRepository clientLoginInfoRepository;
	
	@Autowired
	AdvisorUserBulkUploadHistoryRepository advisorUserBulkUploadHistoryRepository;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	private CacheInfoService cacheInfoService;
	

	public static final int STATUS_USER_NOT_REGISTERED = 101;
	public static final int STATUS_USER_DATA_NOT_SAVED = 201;
	public static final int STATUS_ADVISOR_REGISTERED = 100;

	@Override
	public void AdvisorForgotPasswordDTOsave(AdvisorForgotPasswordDTO forgotDTO) {
		// TODO Auto-generated method stub
		AdvisorForgotPassword advForgotPass = new AdvisorForgotPassword();
		AdvisorUser advUser = new AdvisorUser();
		advUser.setId(forgotDTO.getAdvisorID());
		advForgotPass.setAdvisorUser(advUser);
		advForgotPass.setTimestamp(forgotDTO.getTimestamp());
		advForgotPass.setUuid(forgotDTO.getUuid());
		advisorForgorPasswordRepository.save(advForgotPass);

	}
	
	@Override
	public void clientForgotPasswordDTOsave(ClientForgotPasswordDTO forgotDTO) {
		// TODO Auto-generated method stub
		ClientForgotPassword clientForgotPass = new ClientForgotPassword();
		System.out.println("clientMaster id in save : " + forgotDTO.getClientId());
		ClientMaster client = clientMasterRepository.findOne(forgotDTO.getClientId());
		System.out.println("clientMaster id after fetch : " + client.getId());
		clientForgotPass.setClientMaster(client);
		clientForgotPass.setTimestamp(forgotDTO.getTimestamp());
		clientForgotPass.setUuid(forgotDTO.getUuid());
		clientForgotPasswordRepository.save(clientForgotPass);

	}

	@Override
	public AdvisorDTO save(AdvisorDTO advisorDTO) {
		LookupCountry country = lookupCountryRepository.findOne(advisorDTO.getLookupCountryId());
		AdvisorMaster advMaster = mapper.map(advisorDTO, AdvisorMaster.class);
		AdvisorUser advUser = mapper.map(advisorDTO, AdvisorUser.class);
		advUser.setBirthDate(advisorDTO.getBirthDate());
		advUser.setLoginUsername(advisorDTO.getEmailID());
		// TODO: Make password encrypted
		// log.debug("encrypted Password : " +
		// bCryptPasswordEncoder.encode(advisorDTO.getLoginPassword()));
		// advUser.setLoginPassword(bCryptPasswordEncoder.encode(advisorDTO.getLoginPassword()));
		advUser.setLookupCountry(country);
		advUser.setPhoneCountryCode("+" + country.getPhonecode().toString());
		advUser.setLastLoginTime(new Date());
		AdvisorMaster retAM = advisorMasterRepository.save(advMaster);
		advUser.setAdvisorMaster(retAM);
		AdvisorUser retAU = advisorUserRepository.save(advUser);
		AdvisorRole advRole = new AdvisorRole();
		advRole.setAdvisorMaster(advMaster);
		advRole.setRoleDescription("Admin");
		advisorRoleRepository.save(advRole);

		AdvisorUserRoleMapping advisorUserRoleMapping = new AdvisorUserRoleMapping();
		advisorUserRoleMapping.setAdvisorRole(advRole);
		advisorUserRoleMapping.setAdvisorUser(retAU);
		advisorUserRoleMapping.setEffectiveFromDate(new Date());
		advisorUserRoleMappingRepository.save(advisorUserRoleMapping);

		AdvisorDTO advDTO = mapper.map(retAM, AdvisorDTO.class);
		mapper.map(retAU, advDTO);
		return advDTO;
	}

	/*
	 * @Override public AdvisorDTO findByEmailIdAndPassword(String emailId, String
	 * password) { // TODO Auto-generated method stub AdvisorUser advUser =
	 * advisorUserRepository.findByEmailIDAndLoginPassword(emailId, password); if
	 * (advUser != null) { AdvisorDTO advDTO =
	 * mapper.map(advUser.getAdvisorMaster(), AdvisorDTO.class); mapper.map(advUser,
	 * advDTO);
	 * 
	 * advDTO.setLookupCountryId(advUser.getLookupCountry().getId());
	 * advDTO.setAdvisorMasterId(advUser.getAdvisorMaster().getId());
	 * 
	 * return advDTO; } else { return null; } }
	 */
	String encodedToken;

	@Override

	public void sendVerificationLink(String emailID, String verficationLink) {
		AdvisorUser user = advisorUserRepository.findByEmailID(emailID);
		AdvisorForgotPasswordDTO forgotDTO = new AdvisorForgotPasswordDTO();
		double timestamp;
		try {
			// String encodedToken = encodedUserId(user.getId());
			encodedToken = generateUUID();
			String urlToSend = verficationLink + encodedToken;
			forgotDTO.setAdvisorID(user.getId());
			timestamp = System.currentTimeMillis();
			forgotDTO.setTimestamp(timestamp);
			forgotDTO.setUuid(encodedToken);

			StringBuilder sb = new StringBuilder();
			sb.append("Dear ");
			sb.append(user.getFirstName() + " " + user.getLastName() + ",\n");
			sb.append("\n");
			sb.append("Please click on this link to change your password: \n");
			sb.append("\n");

			sb.append("This link is valid for only 5 minutes \n");

			sb.append(urlToSend + "\n");
			List<String> toList = new ArrayList<String>();
			toList.add(user.getEmailID());
			sb.append("Administrator , Finexa Application \n\n");
			EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_PASSWORD_RESET, sb.toString());
			//emailService.sendSimpleMessage(user.getEmailID(), "Finexa Password Reset", sb.toString());

			// user.setExpirationDate(new
			// SimpleDateFormat("yyyy-MM-dd").parse(FinexaUtil.getDate(System.currentTimeMillis())));
			AdvisorForgotPasswordDTOsave(forgotDTO);
			// advisorUserRepository.save(user);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	@Override
	public void sendVerificationLinkForClient(String emailID, String verficationLink) {
		ClientContact clientContact = clientContactRepository.findByEmailID(emailID);
		ClientForgotPasswordDTO forgotDTO = new ClientForgotPasswordDTO();
		double timestamp;
		try {
			// String encodedToken = encodedUserId(user.getId());
			encodedToken = generateUUID();
			String urlToSend = verficationLink + encodedToken;
			forgotDTO.setClientId(clientContact.getClientMaster().getId());
			timestamp = System.currentTimeMillis();
			forgotDTO.setTimestamp(timestamp);
			forgotDTO.setUuid(encodedToken);

			StringBuilder sb = new StringBuilder();
			sb.append("Dear ");
			sb.append(clientContact.getClientMaster().getFirstName() + " " + clientContact.getClientMaster().getLastName() + ",\n");
			sb.append("\n");
			sb.append("Please click on this link to change your password: \n");
			sb.append("\n");

			sb.append("This link is valid for only 5 minutes \n");

			sb.append(urlToSend + "\n");
			List<String> toList = new ArrayList<String>();
			toList.add(clientContact.getEmailID());
			sb.append("Administrator , Finexa Application \n\n");
			
			clientForgotPasswordDTOsave(forgotDTO);
			
			EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_PASSWORD_RESET, sb.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Boolean changePasswordByEmailId(String password, String emailId) {
		// TODO Auto-generated method stub
		AdvisorUser advUser = null;
		advUser = advisorUserRepository.findByEmailID(emailId);
		if (advUser != null) {
			advUser.setLoginPassword(password);
			advUser.getUser().setLoginPassword(password);
			advUser.setLastLoginTime(new Date());
			advisorUserRepository.save(advUser);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}

	}

	@Override
	public AdvisorDTO findByUsernameAndPassword(String username, String password) {
		// TODO Auto-generated method stub
		AdvisorUser advUser = advisorUserRepository.findByLoginUsernameAndLoginPassword(username, password);
		if (advUser != null) {
			if (advUser.getActiveFlag().equals("Y")) {
				AdvisorDTO advDTO = mapper.map(advUser.getAdvisorMaster(), AdvisorDTO.class);
				mapper.map(advUser, advDTO);
				// advDTO.setLookupCountryId(advUser.getLookupCountry().getId());
				advDTO.setAdvisorMasterId(advUser.getAdvisorMaster().getId());
				advDTO.setEmailID(advUser.getEmailID());
				advDTO.setLastLoginTime(advUser.getLastLoginTime());
				 /// role set
				advDTO.setBudgetManagementView(advUser.getUser().getBudgetManagementView());
				advDTO.setGoalPlanningView(advUser.getUser().getGoalPlanningView());
				advDTO.setGoalPlanningAddEdit(advUser.getUser().getGoalPlanningAddEdit());
				advDTO.setPortfolioManagementView(advUser.getUser().getPortfolioManagementView());
				advDTO.setPortfolioManagementAddEdit(advUser.getUser().getPortfolioManagementAddEdit());
				advDTO.setFinancialPlanningView(advUser.getUser().getFinancialPlanningView());
				advDTO.setFinancialPlanningAddEdit(advUser.getUser().getFinancialPlanningAddEdit());
				advDTO.setAdvisorAdmin(advUser.getUser().getAdvisorAdmin());
				advDTO.setAdmin(advUser.getUser().getAdmin());
				advDTO.setBseAccessMode("" + advUser.getLookupTransactBseaccessMode().getId());
				advDTO.setBseAccessMode("" + advUser.getLookupTransactBseaccessMode().getId());
				return advDTO;
			}
		}
		return null;

	}
	//for client login
	@Override
	public ClientInfoDTO findByUsernameAndPasswordForClient(String username, String password) {
		// TODO Auto-generated method stub
		ClientMaster cm = clientMasterRepository.findByLoginUsernameAndLoginPassword(username, password);
		if (cm != null) {
			if (cm.getActiveFlag().equals("Y")) {
				ClientInfoDTO cmDTO = mapper.map(cm.getAdvisorUser(), ClientInfoDTO.class);
				mapper.map(cm, cmDTO);
				// advDTO.setLookupCountryId(advUser.getLookupCountry().getId());
				cmDTO.setId(cm.getId());
				cmDTO.setName(cm.getFirstName() + " " + (cm.getMiddleName() == null ? " " : cm.getMiddleName()) + " " + cm.getLastName());
				cmDTO.setLastLoginTime(cm.getLastLoginTime());
				List<ClientContact> l = cm.getClientContacts();
				cmDTO.setRetirementStatus(cm.getRetiredFlag());
				if (l.size() > 0) {
					ClientContact c = l.get(0);
					cmDTO.setEmailId(c.getEmailID());
					cmDTO.setMobile(c.getMobile());
				}
				cmDTO.setUserID(cm.getAdvisorUser().getId());
				return cmDTO;
			}
		}
		return null;

	}

	@Override
	public List<AdvisorDTO> findAllUsersForOrg(int advisorMasterId) {
		// TODO Auto-generated method stub
		List<AdvisorDTO> retDTOList = new ArrayList<AdvisorDTO>();
		AdvisorMaster advisorMaster = advisorMasterRepository.findOne(advisorMasterId);
		List<AdvisorUser> advUserList = advisorMaster.getAdvisorUsers();
		for (AdvisorUser au : advUserList) {

			retDTOList.add(mapper.map(au, AdvisorDTO.class));

		}
		return retDTOList;
	}

	@Override
	@Transactional(rollbackOn = CustomFinexaException.class)
	public WritableWorkbook downloadUserTemplate(HttpServletResponse response, int advisorId)
			throws CustomFinexaException, IOException, RowsExceededException, WriteException, RuntimeException {

		// log.debug("advisorId in downloadUserTemplate service: " + advisorId);
		try {
			String fileName = "AdvisorUserUploadingTemplate.xls";
			WritableWorkbook writableWorkbook = null;

			response.setContentType("application/vnd.ms-excel"); //

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

			WritableSheet usercreationTemplate = writableWorkbook.createSheet("UserCreationTemplate", 0);
			
			AdvisorUser advUser = advisorUserRepository.findOne(advisorId);
			AdvisorMaster advMaster = advUser.getAdvisorMaster();
			List<AdvisorRole> roles = advMaster.getAdvisorRoles();

			System.out.println("admin ID :"+ advUser.getUser().getId());
			System.out.println("admin name :"+ advUser.getUser().getLoginUsername());
			System.out.println("admin role :"+ advUser.getUser().getAdmin());
			
			if(advUser.getUser().getAdmin().equalsIgnoreCase("y")) {
				
				addUserTemplateHeader(usercreationTemplate);
				// writeUserTemplateData(usercreationTemplate);
				
				WritableSheet organisation = writableWorkbook.createSheet("Organiation", 1);
				addOrganisationHeader(organisation);
				addOrganisationData(organisation, advMaster);
			
			} else {
				addUserTemplateHeaderForAdvisorAdmin(usercreationTemplate);
				
				WritableSheet role = writableWorkbook.createSheet("Roles", 2);
				addRoleHeader(role);
				addRoleData(role, roles);
			}

			WritableSheet country = writableWorkbook.createSheet("Country", 3);
			addCountryHeader(country);
			addCountryData(country);

			writableWorkbook.write();
			writableWorkbook.close();

			return writableWorkbook;

		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}

	}

	private void addRoleData(WritableSheet role, List<AdvisorRole> roles)
			throws RowsExceededException, WriteException, CustomFinexaException {

		try {
			log.debug("size of role List: " + roles.size());
			int i = 1;
			for (AdvisorRole advisorRoleObj : roles) {
				role.addCell(new Label(0, i, Integer.toString(advisorRoleObj.getId())));
				role.addCell(new Label(1, i, advisorRoleObj.getRoleDescription()));
				i++;
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException(FinexaConstant.BULK_UPLOAD_MODULE, "Nothing Specific",
					"Failed to add Role Data in excel sheet");
		}

	}

	private void addRoleHeader(WritableSheet sheet)
			throws RowsExceededException, WriteException, CustomFinexaException {
		// TODO Auto-generated method stub
		try {
			sheet.addCell(new Label(0, 0, "ID"));
			sheet.addCell(new Label(1, 0, "Role Name"));
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException(FinexaConstant.BULK_UPLOAD_MODULE, "Nothing Specific",
					"Failed to add Role Header in excel sheet");
		}

	}

	private void addOrganisationData(WritableSheet sheet, AdvisorMaster advMaster)
			throws RowsExceededException, WriteException, CustomFinexaException {
		// TODO Auto-generated method stub
		try {
			int i =1;
			List <AdvisorMaster> advisorMaster = advisorMasterRepository.findAll();
			
			for (AdvisorMaster master : advisorMaster) {
				sheet.addCell(new Label(0, i, Integer.toString(master.getId())));
				sheet.addCell(new Label(1, i, master.getOrgName()));
				sheet.addCell(new Label(2, i, master.getDistributorCode()));
				i++;
			}
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException(FinexaConstant.BULK_UPLOAD_MODULE, "Nothing Specific",
					"Failed to add Organization data in excel sheet");
		}

	}

	private void addCountryData(WritableSheet sheet)
			throws RuntimeException, RowsExceededException, WriteException, CustomFinexaException {
		// TODO Auto-generated method stub
		try {
			int i = 1;
			for (LookupCountry country : lookupCountryRepository.findAll()) {
				sheet.addCell(new Label(0, i, Integer.toString(country.getId())));
				sheet.addCell(new Label(1, i, country.getName()));
				i++;
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException(FinexaConstant.BULK_UPLOAD_MODULE, "Nothing Specific",
					"Failed to add Country data in excel sheet");
		}

	}

	private void addCountryHeader(WritableSheet sheet)
			throws RowsExceededException, WriteException, CustomFinexaException {
		// TODO Auto-generated method stub
		try {
			sheet.addCell(new Label(0, 0, "ID"));
			sheet.addCell(new Label(1, 0, "Country Name"));
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException(FinexaConstant.BULK_UPLOAD_MODULE, "Nothing Specific",
					"Failed to add Country Header in excel sheet");
		}

	}

	private void addOrganisationHeader(WritableSheet sheet)
			throws RowsExceededException, WriteException, CustomFinexaException {
		// TODO Auto-generated method stub
		try {
			sheet.addCell(new Label(0, 0, "ID"));
			sheet.addCell(new Label(1, 0, "Organisation Name"));
			sheet.addCell(new Label(2, 0, "Distributor Code"));
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException(FinexaConstant.BULK_UPLOAD_MODULE, "Nothing Specific",
					"Failed to add Organization Header in excel sheet");
		}

	}

	private void addUserTemplateHeader(WritableSheet sheet)
			throws RowsExceededException, WriteException, CustomFinexaException {
		// create header row

		try {
			
			sheet.addCell(new Label(0, 0, "System ID"));
			sheet.addCell(new Label(1, 0, "First Name"));
			sheet.addCell(new Label(2, 0, "Last Name"));
			sheet.addCell(new Label(3, 0, "Gender"));
			sheet.addCell(new Label(4, 0, "Email ID"));
			sheet.addCell(new Label(5, 0, "Mobile Number"));
			sheet.addCell(new Label(6, 0, "Country"));
			sheet.addCell(new Label(7, 0, "State"));
			sheet.addCell(new Label(8, 0, "City"));
			sheet.addCell(new Label(9, 0, "Employee Code"));
			sheet.addCell(new Label(10, 0, "Organization Flag"));
			sheet.addCell(new Label(11, 0, "Organization Name"));
			sheet.addCell(new Label(12, 0, "Distributor Code"));
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException(FinexaConstant.BULK_UPLOAD_MODULE, "Nothing Specific",
					"Failed to add User Template Header in excel sheet");
		}

	}
	
	private void addUserTemplateHeaderForAdvisorAdmin(WritableSheet sheet)
			throws RowsExceededException, WriteException, CustomFinexaException {
		// create header row

		try {
			
			sheet.addCell(new Label(0, 0, "System ID"));
			sheet.addCell(new Label(1, 0, "First Name"));
			sheet.addCell(new Label(2, 0, "Last Name"));
			sheet.addCell(new Label(3, 0, "Gender"));
			sheet.addCell(new Label(4, 0, "Email ID"));
			sheet.addCell(new Label(5, 0, "Mobile Number"));
			sheet.addCell(new Label(6, 0, "Country"));
			sheet.addCell(new Label(7, 0, "State"));
			sheet.addCell(new Label(8, 0, "City"));
			sheet.addCell(new Label(9, 0, "Employee Code"));
			sheet.addCell(new Label(10, 0, "Role"));
			sheet.addCell(new Label(11, 0, "Supervisor Role"));

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException(FinexaConstant.BULK_UPLOAD_MODULE, "Nothing Specific",
					"Failed to add User Template Header in excel sheet");
		}

	}
	
	@Override
	@Transactional(rollbackOn = CustomFinexaException.class)
	public UploadResponseDTO uploadBulkUsers(int advisorID, FileuploadDTO fileuploadDTO)
			throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub

		try {
			log.debug("inside uploadBulkUsers service impl");
			Workbook workbook = null;
			UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
			try {
				AdvisorUser advisorUser = advisorUserRepository.findOne(advisorID);
				AdvisorUserBulkUploadHistory advisorUserBulkUploadHistory = advisorUserBulkUploadHistoryRepository.
						findByAdvisorUserAndStatus(advisorUser, "Running");
				if (advisorUserBulkUploadHistory != null) {
					uploadResponseDTO.getErrors().add("Another Bulk Upload is in progress. Please wait untill complete...");
				} else {
					AdvisorUserBulkUploadHistory newAdvisorUserBulkUploadHistoryEntry = new AdvisorUserBulkUploadHistory();
					newAdvisorUserBulkUploadHistoryEntry.setAdvisorUser(advisorUser);
					newAdvisorUserBulkUploadHistoryEntry.setFileName(fileuploadDTO.getFilename());
					newAdvisorUserBulkUploadHistoryEntry.setStatus("Running");
					newAdvisorUserBulkUploadHistoryEntry.setStartTime(new Date());
					newAdvisorUserBulkUploadHistoryEntry = advisorUserBulkUploadHistoryRepository.save(newAdvisorUserBulkUploadHistoryEntry);
					
					UploadResponseDTO uploadResponseDTO1 = new UploadResponseDTO();
					
					if (newAdvisorUserBulkUploadHistoryEntry != null) {
						ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 5, Long.MAX_VALUE,
		                                 TimeUnit.NANOSECONDS, new ArrayBlockingQueue<Runnable>(3));
						AdvisorBulkUploadThread thread = applicationContext.getBean(AdvisorBulkUploadThread.class);
						thread.initialize(fileuploadDTO, uploadResponseDTO1, newAdvisorUserBulkUploadHistoryEntry);
						pool.execute(thread);

						uploadResponseDTO.setMessage("Bulk Upload started");
						pool.shutdown();
					} else {
						uploadResponseDTO.getErrors().add("Bulk Upload error");
					}
				}
				return uploadResponseDTO;

//				workbook = Workbook.getWorkbook(fileuploadDTO.getFile()[0].getInputStream());
//				Sheet sheet = workbook.getSheet(0);
//				// saveData(sheet, fileuploadDTO.getAdvisorUserId());
//				//AdvisorUser advisorUser = advisorUserRepository.findOne(fileuploadDTO.getAdvisorUserId());
//				//System.out.println("ID : " + advisorUser.getId());
//				//System.out.println("admin? : " + advisorUser.getUser().getAdmin());
//				if(advisorUser.getUser().getAdmin().equalsIgnoreCase("y")) {
//					if (validate(sheet, uploadResponseDTO)) {
//						boolean status = saveData(sheet, fileuploadDTO.getAdvisorUserId());
//						if (status == false) {
//							//UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
//							uploadResponseDTO.getErrors().add("This combination of Organization Name and Distributor Code already exists.<br>");
//						}
//					}
//				} else {
//					if (validateSheetForAdvisorUser(sheet, uploadResponseDTO)) {
//						AdvisorMaster advisorMaster = advisorUserRepository.findOne(advisorID).getAdvisorMaster();
//						boolean status = saveDataForAdvisorUser(advisorMaster, sheet, fileuploadDTO.getAdvisorUserId());
//						if (status == false) {
//							//UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
//							uploadResponseDTO.getErrors().add("This Employee Code already exists.<br>");
//						}
//					}
//				}

			} finally {
				if (workbook != null) {
					workbook.close();
				}
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		//return uploadResponseDTO;
	}

//	private boolean saveData(Sheet sheet, int advisorId) throws RuntimeException, CustomFinexaException {
//		// TODO Auto-generated method stub
//		boolean ret = true;
//		AdvisorRole advRole;
//		// AdvisorUser au = advisorUserRepository.findOne(advisorId);
//		try {
//			for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
//
//
//				// advisorMaster save
//				AdvisorMaster advMaster;
//				try {
//					
//					advMaster = advisorMasterRepository.findByOrgNameAndDistributorCode
//							(sheet.getCell(11, rownum).getContents(), sheet.getCell(12, rownum).getContents());
//					if(advMaster == null) {
//						advMaster = new AdvisorMaster();
//						advMaster.setOrgName(sheet.getCell(11, rownum).getContents());
//						advMaster.setDistributorCode(sheet.getCell(12, rownum).getContents());
//						advMaster.setOrgFlag(sheet.getCell(10, rownum).getContents().toUpperCase());
//						advMaster.setAutoCreateClient("Y");
//						advMaster = advisorMasterRepository.save(advMaster);
//					
//					
//					// avisorRole save
//					
//					String roleName = "Admin";
//					//String supRoleName = null;
//
//					System.out.println("master: " + advMaster.getId());
//
//					advRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(advMaster, roleName);
//					if (advRole == null) {
//						
//						advRole = new AdvisorRole();
//						advRole.setRoleDescription(roleName);
//						advRole.setSupervisorRoleID(null);
//						advRole.setAdvisorMaster(advMaster);
//						
//						advRole = advisorRoleRepository.save(advRole);
//					}
//
//					// user save
//					User user;
//					User savedUser;
//					
//					user = new User();
//					user.setAdvisorMaster(advMaster);
//					user.setLoginUsername(sheet.getCell(4, rownum).getContents());
//					user.setLoginPassword(sheet.getCell(1, rownum).getContents());
//					user.setActiveFlag("Y");
//					user.setAdvisorRole(advRole);
//					user.setAdmin("N");
//					user.setAdvisorAdmin("Y");
//					user.setClientInfoView("Y");
//					user.setClientInfoAddEdit("Y");
//					user.setClientInfoDelete("Y");
//					if(advMaster.getOrgFlag().equalsIgnoreCase("y")) {
//						user.setUserManagementView("Y");
//						user.setUserManagementAddEdit("Y");
//						user.setUserManagementDelete("Y");
//					} else {
//						user.setUserManagementView("N");
//						user.setUserManagementAddEdit("Y");
//						user.setUserManagementDelete("Y");
//					}
//					
//					SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//					Date date = new Date(System.currentTimeMillis());  
//					//System.out.println(formatter.format(date));  
//					user.setCreatedOn(date);
//					
//					savedUser = userRepository.save(user);
//
//					// AdvisorUser save
//					AdvisorUser advUser;
//					AdvisorUser savedAdvUser;
//
//					advUser = new AdvisorUser();
//					advUser.setAdvisorMaster(advMaster); //save masterID
//					advUser.setFirstName(sheet.getCell(1, rownum).getContents());
//					advUser.setLastName(sheet.getCell(2, rownum).getContents());
//					advUser.setGender(sheet.getCell(3, rownum).getContents().toUpperCase());
//					advUser.setEmailID(sheet.getCell(4, rownum).getContents());
//					advUser.setPhoneNo(new BigInteger(sheet.getCell(5, rownum).getContents()));
//					advUser.setState(sheet.getCell(7, rownum).getContents());
//					advUser.setCity(sheet.getCell(8, rownum).getContents());
//					advUser.setLoginUsername(sheet.getCell(4, rownum).getContents());
//					advUser.setLoginPassword(sheet.getCell(1, rownum).getContents());
//					advUser.setEmployeeCode(sheet.getCell(9, rownum).getContents());
//					advUser.setActiveFlag("Y");
//					//advUser.setLastLoginTime(new Date());
//					advUser.setLoggedInFlag("N");
//					
//					advUser.setLookupCountry(
//							lookupCountryRepository.findByName(sheet.getCell(6, rownum).getContents().toUpperCase()));
//					
//					advUser.setLookupTransactBseaccessMode(lookupTransactBSEAccessModeRepository
//							.findOne((byte) 1));
//					
//					LookupCountry lookupCountry = lookupCountryRepository.findByName(sheet.getCell(6, rownum).getContents().toUpperCase());
//					advUser.setPhoneCountryCode(lookupCountry.getPhonecode().toString());
//
//					advUser.setUser(savedUser);
//
//					savedAdvUser = advisorUserRepository.save(advUser);
//
//					// save userROLE MAPPING
//
//					AdvisorUserRoleMapping userRoleMap = new AdvisorUserRoleMapping();
//					userRoleMap.setAdvisorRole(advRole);
//					userRoleMap.setAdvisorUser(savedAdvUser);
//					Calendar cal = Calendar.getInstance();
//					userRoleMap.setEffectiveFromDate(cal.getTime());
//					advisorUserRoleMappingRepository.save(userRoleMap);
//
//					// Advisoruser supervisor mapping
//
//					/*String supervisorName = sheet.getCell(12, rownum).getContents();
//
//					String[] supervisorFirstName = supervisorName.trim().split("\\s+");
//
////					AdvisorUser supervisorUser = advisorUserRepository
////							.findByFirstNameAndLastName(supervisorFirstName[0], supervisorFirstName[1]);
//
////					AdvisorUser supervisorUser = advisorUserRepository
////							.findByIdAndRole(sheet.getCell(12, rownum).getContents(), advSupRole);
//					
//					User supervisorUser1 = userRepository
//							.findByIdAndAdvisorRole(sheet.getCell(12, rownum).getContents(), advSupRole);
//					
//					AdvisorUser supervisorUser = supervisorUser1.getAdvisorUser();
//					
//					System.out.println("supervisor :" + supervisorUser.getFirstName() + "<br><br>");
////					if(supervisorUser.getUser().getAdvisorRole() == advSupRole ) {
////						
////					}
//						
//					AdvisorUserSupervisorMapping advisorUserSupervisorMapping = new AdvisorUserSupervisorMapping();
//					advisorUserSupervisorMapping.setAdvisorUser1(advUser);
//					advisorUserSupervisorMapping.setAdvisorUser2(supervisorUser);
//					Calendar c = Calendar.getInstance();
//					advisorUserSupervisorMapping.setEffectiveFromDate(c.getTime());
//					advisorUserSupervisorMappingRepository.save(advisorUserSupervisorMapping);*/
//					} else {
//						ret = false;
//						
//					}
//
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					// throw new CustomFinexaException("Advisor Role", "Nothing Specific", "Failed
//					// to save data to Advisor Role");
//
//				}
//
//			}
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//			throw new RuntimeException(e);
//		}
//		return ret;
//	}
//
//	private boolean saveDataForAdvisorUser(AdvisorMaster advisorMaster, Sheet sheet, int advisorId) throws RuntimeException, CustomFinexaException {
//		// TODO Auto-generated method stub
//		boolean ret = true;
//		AdvisorRole advRole;
//		// AdvisorUser au = advisorUserRepository.findOne(advisorId);
//		try {
//			for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
//
//
//				// advisorMaster save
//				AdvisorMaster advMaster;
//				try {
//					//AdvisorMaster advisorMaster = new AdvisorMaster();
//					
//					advMaster = advisorMaster;
//					//check for unique empCode
//					List<AdvisorUser> emp = advisorUserRepository.
//							findByAdvisorMasterAndEmployeeCode(advMaster, sheet.getCell(9, rownum).getContents());
//					//user is created only if empCode does not exist in database
//					if(emp == null || emp.isEmpty()) {
//						String roleName = sheet.getCell(10, rownum).getContents();
//						String supRoleName = null;
//						if (!roleName.equalsIgnoreCase("Admin")) {
//							
//							supRoleName = sheet.getCell(11, rownum).getContents();
//						}
//						// supervisorRole
//	
//						//AdvisorRole advSupRole1 = advisorRoleRepository.findByRoleDescription(supRoleName);
//						System.out.println("master: " + advMaster.getId());
//						if (supRoleName != null) {
//							AdvisorRole advSupRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(advMaster, supRoleName);
//							if (advSupRole == null) {
//								advSupRole = new AdvisorRole();
//								advSupRole.setRoleDescription(supRoleName);
//								if(supRoleName.equalsIgnoreCase("admin")) {
//									advSupRole.setSupervisorRoleID(null);
//								} else {
//									advSupRole.setSupervisorRoleID(advSupRole.getId());
//								}
//								advSupRole.setAdvisorMaster(advMaster);
//								advSupRole = advisorRoleRepository.save(advSupRole);
//							}
//						}
//						
//	
//						advRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(advMaster, roleName);
//						if (advRole == null) {
//							advRole = new AdvisorRole();
//							advRole.setRoleDescription(roleName);
//							if (supRoleName != null) {
//								advRole.setSupervisorRoleID((int) lookupRoleRepository.findByDescription(supRoleName).getId());
//							} else {
//								advRole.setSupervisorRoleID(null);
//							}
//							advRole.setAdvisorMaster(advMaster);
//							advRole = advisorRoleRepository.save(advRole);
//						}
//	
//						
//	
//						// AdvisorUser save
//	
//						// user
//						User user;
//						User savedUser;
//						AdvisorUser advUser;
//						AdvisorUser savedAdvUser;
//	
//						user = new User();
//						user.setAdvisorMaster(advMaster);
//						user.setLoginUsername(sheet.getCell(4, rownum).getContents());
//						user.setLoginPassword(sheet.getCell(1, rownum).getContents());
//						user.setActiveFlag("Y");
//						user.setAdvisorRole(advRole);
//						user.setAdmin("N");
//						user.setAdvisorAdmin("N");
//						user.setClientInfoView("Y");
//						user.setClientInfoAddEdit("Y");
//						user.setClientInfoDelete("Y");
//						
//						SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//						Date date = new Date(System.currentTimeMillis());  
//						System.out.println(formatter.format(date));  
//						
//						user.setCreatedOn(date);
//						
//						savedUser = userRepository.save(user);
//	
//						advUser = new AdvisorUser();
//						advUser.setAdvisorMaster(advMaster); //save masterID
//						advUser.setFirstName(sheet.getCell(1, rownum).getContents());
//						advUser.setLastName(sheet.getCell(2, rownum).getContents());
//						advUser.setGender(sheet.getCell(3, rownum).getContents().toUpperCase());
//						advUser.setEmailID(sheet.getCell(4, rownum).getContents());
//						advUser.setPhoneNo(new BigInteger(sheet.getCell(5, rownum).getContents()));
//						advUser.setState(sheet.getCell(7, rownum).getContents());
//						advUser.setCity(sheet.getCell(8, rownum).getContents());
//						advUser.setLoginUsername(sheet.getCell(4, rownum).getContents());
//						advUser.setLoginPassword(sheet.getCell(1, rownum).getContents());
//						advUser.setEmployeeCode(sheet.getCell(9, rownum).getContents());
//						advUser.setActiveFlag("Y");
//						//advUser.setLastLoginTime(new Date());
//						advUser.setLoggedInFlag("N");
//						
//						advUser.setLookupCountry(
//								lookupCountryRepository.findByName(sheet.getCell(6, rownum).getContents().toUpperCase()));
//						
//						advUser.setLookupTransactBseaccessMode(lookupTransactBSEAccessModeRepository
//								.findOne((byte) 1));
//						
//						LookupCountry lookupCountry = lookupCountryRepository.findByName(sheet.getCell(6, rownum).getContents().toUpperCase());
//						advUser.setPhoneCountryCode(lookupCountry.getPhonecode().toString());
//	
//						advUser.setUser(savedUser);
//	
//						savedAdvUser = advisorUserRepository.save(advUser);
//	
//						// save userROLE MAPPING
//	
//						AdvisorUserRoleMapping userRoleMap = new AdvisorUserRoleMapping();
//						userRoleMap.setAdvisorRole(advRole);
//						userRoleMap.setAdvisorUser(savedAdvUser);
//						Calendar cal = Calendar.getInstance();
//						userRoleMap.setEffectiveFromDate(cal.getTime());
//						advisorUserRoleMappingRepository.save(userRoleMap);
//	
//						// Advisoruser supervisor mapping
//	
//						/*String supervisorName = sheet.getCell(12, rownum).getContents();
//	
//						String[] supervisorFirstName = supervisorName.trim().split("\\s+");
//	
//	//					AdvisorUser supervisorUser = advisorUserRepository
//	//							.findByFirstNameAndLastName(supervisorFirstName[0], supervisorFirstName[1]);
//	
//	//					AdvisorUser supervisorUser = advisorUserRepository
//	//							.findByIdAndRole(sheet.getCell(12, rownum).getContents(), advSupRole);
//						
//						User supervisorUser1 = userRepository
//								.findByIdAndAdvisorRole(sheet.getCell(12, rownum).getContents(), advSupRole);
//						
//						AdvisorUser supervisorUser = supervisorUser1.getAdvisorUser();
//						
//						System.out.println("supervisor :" + supervisorUser.getFirstName() + "<br><br>");
//	//					if(supervisorUser.getUser().getAdvisorRole() == advSupRole ) {
//	//						
//	//					}
//							
//						AdvisorUserSupervisorMapping advisorUserSupervisorMapping = new AdvisorUserSupervisorMapping();
//						advisorUserSupervisorMapping.setAdvisorUser1(advUser);
//						advisorUserSupervisorMapping.setAdvisorUser2(supervisorUser);
//						Calendar c = Calendar.getInstance();
//						advisorUserSupervisorMapping.setEffectiveFromDate(c.getTime());
//						advisorUserSupervisorMappingRepository.save(advisorUserSupervisorMapping);*/
//					} else {
//						System.out.println("existing users list size: " + emp.size());
//						for(AdvisorUser a : emp) {
//							System.out.println("existing users id: " + a.getId());
//						}
//						ret = false;
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					// throw new CustomFinexaException("Advisor Role", "Nothing Specific", "Failed
//					// to save data to Advisor Role");
//
//				}
//
//			}
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//			throw new RuntimeException(e);
//		}
//		return ret;
//	}
//	
//	private boolean validate(Sheet sheet, UploadResponseDTO uploadResponseDTO) {
//		// TODO Auto-generated method stub
//
//		log.debug("inside validate sheet");
//		if (sheet.getRows() < 2) {
//			uploadResponseDTO.getErrors().add("No Data in Excel sheet<br>");
//		}
//
//		for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
//
//			int displayRow = rownum + 1;
//			// systemId
//			if (StringUtils.isEmpty(sheet.getCell(0, rownum).getContents())) {
//				log.debug("System Id is empty");
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", A ) :" + "System Id should not be Empty<br>");
//			}
//			// firstName
//			if (StringUtils.isEmpty(sheet.getCell(1, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", B) :" + "First Name should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(1, rownum).getContents())) {
//					if (!StringUtils.isAlpha(sheet.getCell(1, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", B) :" + "First Name should contain alphabets<br>");
//					}
//				}
//			}
//			// lastName
//			if (StringUtils.isEmpty(sheet.getCell(2, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", C) :" + "Last Name should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(2, rownum).getContents())) {
//					if (!StringUtils.isAlpha(sheet.getCell(2, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", C) :" + "Last Name should contain alphabets<br>");
//					}
//				}
//			}
//			
//			//Gender
//			if (StringUtils.isEmpty(sheet.getCell(3, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D) :" + "Gender should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(3, rownum).getContents())) {
//					if (!StringUtils.isAlpha(sheet.getCell(3, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", D) :" + "Gender should contain alphabets<br>");
//					}
//					String gender = sheet.getCell(3, rownum).getContents();
//					//System.out.println("role: " + role);
//					if(!gender.equalsIgnoreCase("M") && !gender.equalsIgnoreCase("F")) {
//						uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", L) :" + "Gender should be M or F<br>");
//					}
//				}
//			}
//			
//			// emailID
//			if (StringUtils.isEmpty(sheet.getCell(4, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", E) :" + "Email ID should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(4, rownum).getContents())) {
//					log.debug("email check: " + checkUniqueEmail(sheet.getCell(4, rownum).getContents()));
//					if (checkUniqueEmail(sheet.getCell(4, rownum).getContents()) == false) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", E) :" + "Email ID should be unique<br>");
//					}
//				}
//			}
//			// MobileNo.
//			if (StringUtils.isEmpty(sheet.getCell(5, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", F) :" + "Mobile No. should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(5, rownum).getContents())) {
//					//log.debug("mobile check: " + checkUniqueMobile(sheet.getCell(5, rownum).getContents()));
//					if (StringUtils.isNumeric(sheet.getCell(5, rownum).getContents()) == false) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", F) :" + "Mobile number should contain only numbers<br>");
//					} else {
//					
//						if(sheet.getCell(5, rownum).getContents().length() != 10) {
//							uploadResponseDTO.getErrors()
//							.add("Cell (" + displayRow + ", F) :" + "Mobile number should be of exactly 10 digits<br>");
//						}
//						
//						if (checkUniqueMobile(sheet.getCell(5, rownum).getContents()) == false) {
//							uploadResponseDTO.getErrors()
//									.add("Cell (" + displayRow + ", F) :" + "Mobile number should be unique<br>");
//						}
//					}
//					
//				}
//			}
//			// countryName
//			if (StringUtils.isEmpty(sheet.getCell(6, rownum).getContents())) {
//				uploadResponseDTO.getErrors()
//						.add("Cell (" + displayRow + ", G) :" + "Country Name should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(6, rownum).getContents())) {
//					if (!StringUtils.isAlphaSpace(sheet.getCell(6, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", G) :" + "Country Name should contain alphabets<br>");
//					}
//				}
//			}
//			// state
//			if (StringUtils.isEmpty(sheet.getCell(7, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", H) :" + "State should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(7, rownum).getContents())) {
//					if (!StringUtils.isAlphaSpace(sheet.getCell(7, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", H) :" + "State should contain alphabets<br>");
//					}
//				}
//			}
//			// city
//			if (StringUtils.isEmpty(sheet.getCell(8, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", I) :" + "City should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(8, rownum).getContents())) {
//					if (!StringUtils.isAlphaSpace(sheet.getCell(8, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", I) :" + "City should contain alphabets<br>");
//					}
//				}
//			}
//			
//			// employeeCode
//			if (StringUtils.isEmpty(sheet.getCell(9, rownum).getContents())) {
//				uploadResponseDTO.getErrors()
//						.add("Cell (" + displayRow + ", J) :" + "Employee Code should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(9, rownum).getContents())) {
//					if (!StringUtils.isAlphanumeric(sheet.getCell(9, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", J) :" + "Employee Code is not valid<br>");
//					}
//				}
//			}
//			// orgFlag
//			if (StringUtils.isEmpty(sheet.getCell(10, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", K) :" + "Organization Flag value should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(10, rownum).getContents())) {
//					if (!StringUtils.isAlpha(sheet.getCell(10, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", K) :" + "Organization Flag should be Y or N<br>");
//					}
//					String orgFlag = sheet.getCell(10, rownum).getContents();
//					//System.out.println("role: " + role);
//					if(!orgFlag.equalsIgnoreCase("Y") && !orgFlag.equalsIgnoreCase("N")) {
//						uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", K) :" + "Organization Flag should be Y or N<br>");
//					}
//				}
//			}
//			// organizationName
//			if (sheet.getCell(10, rownum).getContents().equalsIgnoreCase("y")) {
//				if (StringUtils.isEmpty(sheet.getCell(11, rownum).getContents())) {
//					uploadResponseDTO.getErrors()
//							.add("Cell (" + displayRow + ", L) :" + "Organization Name should not be Empty when Organization Flag is Y<br>");
//				}
//			}
//			if (StringUtils.isNotEmpty(sheet.getCell(11, rownum).getContents())) {
//				if (!StringUtils.isAlphaSpace(sheet.getCell(11, rownum).getContents())) {
//					uploadResponseDTO.getErrors()
//							.add("Cell (" + displayRow + ", L) :" + "Organization Name should contain alphabets<br>");
//				} 
//				
//				String orgName = sheet.getCell(11, rownum).getContents();
//				AdvisorMaster advMaster = advisorMasterRepository.findByOrgName(orgName);
//				if(advMaster != null) {
//					uploadResponseDTO.getErrors()
//					.add("Cell (" + displayRow + ", L) :" + "This Organization Name already exists<br>");
//				}
//			}
//			
//			// distributorCode
//			if (StringUtils.isEmpty(sheet.getCell(12, rownum).getContents())) {
//				uploadResponseDTO.getErrors()
//						.add("Cell (" + displayRow + ", M) :" + "Distributor Code should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(12, rownum).getContents())) {
//					//if (!StringUtils.isAlphanumeric(sheet.getCell(12, rownum).getContents())) {
//					if (!sheet.getCell(12, rownum).getContents().matches("^ARN-\\d+") 
//							&& !sheet.getCell(12, rownum).getContents().matches("^INA\\d+")) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", M) :" + "Distributor Code is not valid.<br>"
//										+ "For example, Distributor Code should be in one of these formats :<br>"
//										+ "INA000001142, "
//										+ "ARN-121074");
//					}
//					String distCode = sheet.getCell(12, rownum).getContents();
//					AdvisorMaster advMaster = advisorMasterRepository.findByDistributorCode(distCode);
//					if(advMaster != null) {
//						uploadResponseDTO.getErrors()
//						.add("Cell (" + displayRow + ", M) :" + "This Distributor Code already exists<br>");
//					}
//				}
//			}
//			
//
//		}
//
//		return (uploadResponseDTO.getErrors().size() > 0) ? false : true;
//	}
//	
//	private boolean validateSheetForAdvisorUser(Sheet sheet, UploadResponseDTO uploadResponseDTO) {
//		// TODO Auto-generated method stub
//
//		log.debug("inside validate sheet");
//		if (sheet.getRows() < 2) {
//			uploadResponseDTO.getErrors().add("No Data in Excel sheet<br>");
//		}
//
//		for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
//
//			int displayRow = rownum + 1;
//			// systemId
//			if (StringUtils.isEmpty(sheet.getCell(0, rownum).getContents())) {
//				log.debug("System Id is empty");
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", A ) :" + "System Id should not be Empty<br>");
//			}
//			// firstName
//			if (StringUtils.isEmpty(sheet.getCell(1, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", B) :" + "First Name should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(1, rownum).getContents())) {
//					if (!StringUtils.isAlpha(sheet.getCell(1, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", B) :" + "First Name should contain alphabets<br>");
//					}
//				}
//			}
//			// lastName
//			if (StringUtils.isEmpty(sheet.getCell(2, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", C) :" + "Last Name should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(2, rownum).getContents())) {
//					if (!StringUtils.isAlpha(sheet.getCell(2, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", C) :" + "Last Name should contain alphabets<br>");
//					}
//				}
//			}
//			
//			//Gender
//			if (StringUtils.isEmpty(sheet.getCell(3, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D) :" + "Gender should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(3, rownum).getContents())) {
//					if (!StringUtils.isAlpha(sheet.getCell(3, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", D) :" + "Gender should contain alphabets<br>");
//					}
//					String gender = sheet.getCell(3, rownum).getContents();
//					//System.out.println("role: " + role);
//					if(!gender.equalsIgnoreCase("M") && !gender.equalsIgnoreCase("F")) {
//						uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D) :" + "Gender should be M or F<br>");
//					}
//				}
//			}
//			
//			// emailID
//			if (StringUtils.isEmpty(sheet.getCell(4, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", E) :" + "Email ID should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(4, rownum).getContents())) {
//					log.debug("email check: " + checkUniqueEmail(sheet.getCell(4, rownum).getContents()));
//					if (checkUniqueEmail(sheet.getCell(4, rownum).getContents()) == false) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", E) :" + "Email ID should be unique<br>");
//					}
//				}
//			}
//			// MobileNo.
//			if (StringUtils.isEmpty(sheet.getCell(5, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", F) :" + "Mobile No. should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(5, rownum).getContents())) {
//					//log.debug("mobile check: " + checkUniqueMobile(sheet.getCell(5, rownum).getContents()));
//					if (StringUtils.isNumeric(sheet.getCell(5, rownum).getContents()) == false) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", F) :" + "Mobile number should contain only numbers<br>");
//					} else {
//					
//						if(sheet.getCell(5, rownum).getContents().length() != 10) {
//							uploadResponseDTO.getErrors()
//							.add("Cell (" + displayRow + ", F) :" + "Mobile number should be of exactly 10 digits<br>");
//						}
//						
//						if (checkUniqueMobile(sheet.getCell(5, rownum).getContents()) == false) {
//							uploadResponseDTO.getErrors()
//									.add("Cell (" + displayRow + ", F) :" + "Mobile number should be unique<br>");
//						}
//					}
//					
//				}
//			}
//			// countryName
//			if (StringUtils.isEmpty(sheet.getCell(6, rownum).getContents())) {
//				uploadResponseDTO.getErrors()
//						.add("Cell (" + displayRow + ", G) :" + "Country Name should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(6, rownum).getContents())) {
//					if (!StringUtils.isAlphaSpace(sheet.getCell(6, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", G) :" + "Country Name should contain alphabets<br>");
//					}
//				}
//			}
//			// state
//			if (StringUtils.isEmpty(sheet.getCell(7, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", H) :" + "State should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(7, rownum).getContents())) {
//					if (!StringUtils.isAlphaSpace(sheet.getCell(7, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", H) :" + "State should contain alphabets<br>");
//					}
//				}
//			}
//			// city
//			if (StringUtils.isEmpty(sheet.getCell(8, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", I) :" + "City should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(8, rownum).getContents())) {
//					if (!StringUtils.isAlphaSpace(sheet.getCell(8, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", I) :" + "City should contain alphabets<br>");
//					}
//				}
//			}
//			
//			// employeeCode
//			if (StringUtils.isEmpty(sheet.getCell(9, rownum).getContents())) {
//				uploadResponseDTO.getErrors()
//						.add("Cell (" + displayRow + ", J) :" + "Employee Code should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(9, rownum).getContents())) {
//					if (!StringUtils.isAlphanumeric(sheet.getCell(9, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", J) :" + "Employee Code is not valid<br>");
//					}
//				}
//			}
//			// roleName
//			if (StringUtils.isEmpty(sheet.getCell(10, rownum).getContents())) {
//				uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", K) :" + "Role Name should not be Empty<br>");
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(10, rownum).getContents())) {
//					if (!StringUtils.isAlphaSpace(sheet.getCell(10, rownum).getContents())) {
//						uploadResponseDTO.getErrors()
//								.add("Cell (" + displayRow + ", K) :" + "Role Name should contain alphabets<br>");
//					}
//					String role = sheet.getCell(10, rownum).getContents();
//					System.out.println("role: " + role);
//					if(!role.equalsIgnoreCase("Admin") && !role.equalsIgnoreCase("Head") && !role.equalsIgnoreCase("Branch Manager") 
//							&& !role.equalsIgnoreCase("Relationship Manager") && !role.equalsIgnoreCase("Sub Broker Manager")) {
//						uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", K) :" + "Role Name should be one of these : <br>"
//								+ "Admin,<br>"
//								+ "Head,<br>"
//								+ "Branch Manager,<br>"
//								+ "Relationship Manager,<br>"
//								+ "Sub Broker Manager,<br>");
//					}
//				}
//			}
//			// SupervisorRole
//			if (!(sheet.getCell(10, rownum).getContents().equalsIgnoreCase("Admin"))) {
//				if (StringUtils.isEmpty(sheet.getCell(11, rownum).getContents())) {
//					uploadResponseDTO.getErrors()
//							.add("Cell (" + displayRow + ", L) :" + "Supervisor Role should not be Empty<br>");
//				} else {
//					if (StringUtils.isNotEmpty(sheet.getCell(11, rownum).getContents())) {
//						if (!StringUtils.isAlphaSpace(sheet.getCell(11, rownum).getContents())) {
//							uploadResponseDTO.getErrors()
//									.add("Cell (" + displayRow + ", L) :" + "Supervisor Role should contain alphabets<br>");
//						}
//					}
//					String supRole = sheet.getCell(11, rownum).getContents();
//					System.out.println("supRole: " + supRole);
//					if(!supRole.equalsIgnoreCase("Admin") && !supRole.equalsIgnoreCase("Head") && !supRole.equalsIgnoreCase("Branch Manager") 
//							&& !supRole.equalsIgnoreCase("Relationship Manager") && !supRole.equalsIgnoreCase("Sub Broker Manager")) {
//						uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", L) :" + "Supervisor Role Name should be one of these : <br>"
//								+ "Admin,<br>"
//								+ "Head,<br>"
//								+ "Branch Manager,<br>"
//								+ "Relationship Manager,<br>"
//								+ "Sub Broker Manager,<br>");
//					}
//				}
//			} else {
//				if (StringUtils.isNotEmpty(sheet.getCell(11, rownum).getContents())) {
//					uploadResponseDTO.getErrors()
//					.add("Cell (" + displayRow + ", L) :" + "Supervisor Role should be empty if role is Admin<br>");
//				}
//			}
//
//		}
//
//		return (uploadResponseDTO.getErrors().size() > 0) ? false : true;
//	}

	// Modification done for new table User Table
	@Override
	public AdvisorUser resetPassword(int advisorId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			AdvisorUser advUser = advisorUserRepository.findOne(advisorId);
			String resetPass = "FinexaPass";
			User user = advUser.getUser();
			user.setLoginPassword(resetPass);
			user = userRepository.save(user);
			advUser.setLoginPassword(resetPass);
			advUser.setLastLoginTime(new Date());
			AdvisorUser adUser = advisorUserRepository.save(advUser);
			return adUser;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public AdvisorUser resetPasswordIndex(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			AdvisorUser advUser = advisorUserRepository.findOne(userId);
			String resetPass = "FinexaPass";
			advUser.setLoginPassword(resetPass);
			advUser.setLastLoginTime(null);
			AdvisorUser retAdv = advisorUserRepository.save(advUser);
			return retAdv;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean checkUniqueEmail(String email) {
		boolean status;
		AdvisorUser au = advisorUserRepository.findByEmailID(email);
		if (au != null) {
			status = false;
		} else {
			ClientContact clientContact = clientContactRepository.findByEmailID(email);
			if (clientContact != null) {
				status = false;
			} else {
				status = true;
			}
		}
		//return (au != null) ? false : true;
		return status;
	}
	
	@Override
	public boolean checkUniqueMobile(String mobileNo) {
		// TODO Auto-generated method stub
		AdvisorUser au = advisorUserRepository.findByPhoneNo(new BigInteger(mobileNo));
		return (au != null) ? false : true;
	}

	/*
	 * @Override public boolean checkUniqueOrganization(String organization) {
	 * AdvisorMaster am = advisorMasterRepository.find }
	 */

	/*
	 * @Override public AdvisorDTO findByLoginUsernameAndLoginPassword(String
	 * username, String password) { AdvisorUser advUser =
	 * advisorUserRepository.findByLoginUsernameAndLoginPassword(username,
	 * password); AdvisorDTO advDTO = mapper.map(advUser, AdvisorDTO.class);
	 * advDTO.setLastLoginTime(advUser.getLastLoginTime()); return advDTO; }
	 */

	@Override
	public AdvisorDTO login(LoginDTO loginDTO) {
		// TODO Auto-generated method stub
		AdvisorUser au = advisorUserRepository.findByLoginUsernameAndLoginPassword(loginDTO.getLoginUsername(),
				loginDTO.getLoginPassword());
		if (au != null) {
			au.setLastLoginTime(new Date());
			au.setLoggedInFlag("Y");
			AdvisorUserLoginInfo adUlInfo = new AdvisorUserLoginInfo();
			adUlInfo.setAdvisorUser(au);
			adUlInfo.setLoginTime(new Timestamp(System.currentTimeMillis()));
			adUlInfo.setToken(loginDTO.getToken());
			advisorUserLoginInfoRepository.save(adUlInfo);

			AdvisorUser advUser = advisorUserRepository.save(au);
			AdvisorDTO dto = mapper.map(au, AdvisorDTO.class);
			// set more attribute like role access rights etc
			dto.setOrgName(au.getAdvisorMaster().getOrgName());
			dto.setDistributorCode(au.getAdvisorMaster().getDistributorCode());
			dto.setAdvisorMasterId(au.getAdvisorMaster().getId());

			
			dto.setClientInfoView(advUser.getUser().getClientInfoView());
			dto.setClientInfoAddEdit(advUser.getUser().getClientInfoAddEdit());
			dto.setClientInfoDelete(advUser.getUser().getClientInfoDelete());
			dto.setBudgetManagementView(advUser.getUser().getBudgetManagementView());
			dto.setGoalPlanningView(advUser.getUser().getGoalPlanningView());
			dto.setGoalPlanningAddEdit(advUser.getUser().getGoalPlanningAddEdit());
			dto.setPortfolioManagementView(advUser.getUser().getPortfolioManagementView());
			dto.setPortfolioManagementAddEdit(advUser.getUser().getPortfolioManagementAddEdit());
			dto.setFinancialPlanningView(advUser.getUser().getFinancialPlanningView());
			dto.setFinancialPlanningAddEdit(advUser.getUser().getFinancialPlanningAddEdit());
			dto.setAdvisorAdmin(advUser.getUser().getAdvisorAdmin());
			dto.setAdmin(advUser.getUser().getAdmin());
			dto.setBseAccessMode("" + au.getLookupTransactBseaccessMode().getId());
			dto.setInvestView(advUser.getUser().getInvestView());
			dto.setInvestAddEdit(advUser.getUser().getInvestAddEdit());
			dto.setMastersView(advUser.getUser().getMastersView());
			dto.setMastersAddEdit(advUser.getUser().getMastersAddEdit());
			dto.setMastersDelete(advUser.getUser().getMastersDelete());
			dto.setMfBackOfficeView(advUser.getUser().getMfBackOfficeView());
			dto.setMfBackOfficeAddEdit(advUser.getUser().getMfBackOfficeAddEdit());
			dto.setClientRecordsView(advUser.getUser().getClientRecordsView());
			dto.setUserManagementView(advUser.getUser().getUserManagementView());
			dto.setUserManagementAddEdit(advUser.getUser().getUserManagementAddEdit());
			dto.setUserManagementDelete(advUser.getUser().getUserManagementDelete());
			dto.setAdvisorID(advUser.getId());
			/*
			 * if(au.getAdvisorMaster().getOrgAddressLine1() != null &&
			 * !au.getAdvisorMaster().getOrgAddressLine1().isEmpty()) {
			 * dto.setOrgAddressLine1(au.getAdvisorMaster().getOrgAddressLine1()); }
			 * if(au.getAdvisorMaster().getOrgAddressLine2() != null &&
			 * !au.getAdvisorMaster().getOrgAddressLine2().isEmpty()) {
			 * dto.setOrgAddressLine2(au.getAdvisorMaster().getOrgAddressLine2()); }
			 * if(au.getAdvisorMaster().getOrgAddressLine3() != null &&
			 * !au.getAdvisorMaster().getOrgAddressLine3().isEmpty()) {
			 * dto.setOrgAddressLine3(au.getAdvisorMaster().getOrgAddressLine3()); }
			 * if(au.getAdvisorMaster().getOrgContactDetails() != null) {
			 * dto.setOrgContactDetails(au.getAdvisorMaster().getOrgContactDetails()); }
			 */
			UserDTO userDTO = new UserDTO();
			System.out.println("user id " + advUser.getUser().getId());
			userDTO.setId(advUser.getUser().getId());
			dto.setUserDTO(userDTO);

			if (au.getUser().getAdmin() == null || au.getUser().getAdmin().equalsIgnoreCase("N")) {
				dto.setRole("Advisor");
			} else if (au.getUser().getAdmin().equalsIgnoreCase("Y")) {
				dto.setRole("Admin");
			}

			return dto;
		} else {
			return null;
		}
	}
	
	@Override
	public ClientMasterDTO loginClient(LoginDTO loginDTO) {
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findByLoginUsernameAndLoginPassword(loginDTO.getLoginUsername(),
					loginDTO.getLoginPassword());
			if (cm != null) {
				cm.setLastLoginTime(new Date());
				cm.setLoggedInFlag("Y");
				ClientLoginInfo adUlInfo = new ClientLoginInfo();
				adUlInfo.setClientMaster(cm);
				adUlInfo.setLoginTime(new Timestamp(System.currentTimeMillis()));
				adUlInfo.setToken(loginDTO.getToken());
				clientLoginInfoRepository.save(adUlInfo);
				cm.setFinexaUser("Y");
				ClientMaster clientmas = clientMasterRepository.save(cm);
				ClientMasterDTO dto = mapper.map(cm, ClientMasterDTO.class);
				dto.setClientInfoView(clientmas.getClientAccessRight().getClientInfoView());
				dto.setClientInfoAddEdit(clientmas.getClientAccessRight().getClientInfoAddEdit());
				dto.setClientInfoDelete(clientmas.getClientAccessRight().getClientInfoDelete());
				dto.setBudgetManagementView(clientmas.getClientAccessRight().getBudgetManagementView());
				dto.setGoalPlanningView(clientmas.getClientAccessRight().getGoalPlanningView());
				dto.setGoalPlanningAddEdit(clientmas.getClientAccessRight().getGoalPlanningAddEdit());
				dto.setPortfolioManagementView(clientmas.getClientAccessRight().getPortfolioManagementView());
				dto.setPortfolioManagementAddEdit(clientmas.getClientAccessRight().getPortfolioManagementAddEdit());
				dto.setFinancialPlanningView(clientmas.getClientAccessRight().getFinancialPlanningView());
				dto.setFinancialPlanningAddEdit(clientmas.getClientAccessRight().getFinancialPlanningAddEdit());
				dto.setInvestView(clientmas.getClientAccessRight().getInvestView());
				dto.setInvestAddEdit(clientmas.getClientAccessRight().getInvestAddEdit());
				dto.setMfBackOfficeView(clientmas.getClientAccessRight().getMfBackOfficeView());
				dto.setLoginUserName(clientmas.getLoginUsername());
				dto.setName(cm.getFirstName() + " " + (cm.getMiddleName() == null ? " " : cm.getMiddleName()) + " " + cm.getLastName());
				List<ClientContact> l = cm.getClientContacts();
				dto.setRetirementStatus(cm.getRetiredFlag());
				if (l.size() > 0) {
					ClientContact c = l.get(0);
					dto.setEmailID(c.getEmailID());
					dto.setMobile(c.getMobile());
				}
				dto.setUserId(cm.getAdvisorUser().getId());
				dto.setAdvisorMasterID(cm.getAdvisorUser().getAdvisorMaster().getId());
				if (cm.getClient() != null || cm.getClient().equalsIgnoreCase("Y")) {
					dto.setRole("Client");
				} 

				return dto;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/*
	 * @Override public AdvisorDTO logout(int advisorUserId) { // TODO
	 * Auto-generated method stub
	 * 
	 * AdvisorUser au = advisorUserRepository.findOne(advisorUserId); if (au !=
	 * null) { au.setLastLogoutTime(new Date()); // List<AdvisorUserLoginInfo>
	 * auliList = //
	 * advisorUserLoginInfoRepository.findByAdvisorUserAndLogoutTimeIsNull(au);
	 * 
	 * List<AdvisorUserLoginInfo> aulList = advisorUserLoginInfoRepository
	 * .findByAdvisorUserOrderByLoginTimeDesc(au);
	 * log.debug("Get 0 List Login Time: " + aulList.get(0).getLoginTime());
	 * log.debug("Get 0 List Logout Time: " + aulList.get(0).getLogoutTime());
	 * aulList.get(0).setLogoutTime(new Timestamp(System.currentTimeMillis()));
	 * advisorUserLoginInfoRepository.save(aulList.get(0));
	 * advisorUserRepository.save(au); AdvisorDTO dto = mapper.map(au,
	 * AdvisorDTO.class); return dto; } else { return null; } }
	 */
	
	/*
	 * @Override public AdvisorDTO logout(String token, int advisorUserId) { // TODO
	 * Auto-generated method stub
	 * 
	 * AdvisorUser au = advisorUserRepository.findOne(advisorUserId); if (au !=
	 * null) { au.setLastLogoutTime(new Date()); // List<AdvisorUserLoginInfo>
	 * auliList = //
	 * advisorUserLoginInfoRepository.findByAdvisorUserAndLogoutTimeIsNull(au);
	 * 
	 * AdvisorUserLoginInfo aul = advisorUserLoginInfoRepository.findByToken(token);
	 * log.debug("Get 0 List Login Time: " + aul.getLoginTime());
	 * log.debug("Get 0 List Logout Time: " + aul.getLogoutTime());
	 * 
	 * aul.setLogoutTime(new Timestamp(System.currentTimeMillis()));
	 * advisorUserLoginInfoRepository.save(aul); advisorUserRepository.save(au);
	 * AdvisorDTO dto = mapper.map(au, AdvisorDTO.class); return dto; } else {
	 * return null; } }
	 */

	@Override
	public AdvisorDTO logout(String token, int advisorUserId) {
		// TODO Auto-generated method stub

		AdvisorUser au = advisorUserRepository.findOne(advisorUserId);
		if (au != null) {
			au.setLastLogoutTime(new Date());
			au.setLoggedInFlag("N");
			System.out.println("token "+token);
			AdvisorUserLoginInfo aul = advisorUserLoginInfoRepository.findByToken(token);
			aul.setLogoutTime(new Timestamp(System.currentTimeMillis()));
			advisorUserLoginInfoRepository.save(aul);
			advisorUserRepository.save(au);
			AdvisorDTO dto = mapper.map(au, AdvisorDTO.class);
			return dto;
		} else {
			return null;
		}
	}
	@Override
	public ClientMasterDTO logoutFromRedis(String token) {
		if(token != null && !token.isEmpty()) {
			AdvisorUserLoginInfo aul = advisorUserLoginInfoRepository.findByToken(token);
			if (aul != null) {	
				logoutRedisForUser(token,aul);
			}else {
			ClientLoginInfo cli = clientLoginInfoRepository.findByToken(token);
			if (cli != null) {
				logoutRedisForClient(token,cli);
		    }
		  }
		}
		return null;
	}
	//logout for client
	@Override
	public ClientMasterDTO logoutForClient(String token, int clientID) {
		// TODO Auto-generated method stub

		ClientMaster cm = clientMasterRepository.findOne(clientID);
		if (cm != null) {
			cm.setLastLogoutTime(new Date());
			cm.setLoggedInFlag("N");
			ClientLoginInfo cli = clientLoginInfoRepository.findByToken(token);
			cli.setLogoutTime(new Timestamp(System.currentTimeMillis()));
			clientLoginInfoRepository.save(cli);
			cm.setFinexaUser("Y");
			clientMasterRepository.save(cm);
			ClientMasterDTO dto = mapper.map(cm, ClientMasterDTO.class);
			return dto;
		} else {
			return null;
		}
	}

	@Override
	public AdvisorDTO logoutRedisForUser(String token, AdvisorUserLoginInfo advisorUserLoginInfo) {
		// List<AdvisorUserLoginInfo> auliList =
		// advisorUserLoginInfoRepository.findByAdvisorUserAndLogoutTimeIsNull(au);
		
			try {
				AdvisorUser au = advisorUserRepository.findOne(advisorUserLoginInfo.getAdvisorUser().getId());
				if (au != null) {
					au.setLastLogoutTime(new Date());
					au.setLoggedInFlag("N");
					advisorUserLoginInfo.setLogoutTime(new Timestamp(System.currentTimeMillis()));
					advisorUserLoginInfo = advisorUserLoginInfoRepository.save(advisorUserLoginInfo);
					System.out.println("advisorUserLoginInfo intime"+advisorUserLoginInfo.getLoginTime());
					System.out.println("advisorUserLoginInfo outtime"+advisorUserLoginInfo.getLogoutTime());
					advisorUserRepository.save(au);
					AdvisorDTO dto = mapper.map(au, AdvisorDTO.class);
					
					cacheInfoService.deleteTokenCacheMap(token);
					UserClientRedis userClientRedis = cacheInfoService.getClientCacheMap(token, au.getId());
					 if (userClientRedis != null) {
						cacheInfoService.deleteClientCacheMap(userClientRedis.getId(), token);
					}
					return dto;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	
	//For client
	@Override
	public ClientMasterDTO logoutRedisForClient(String token,ClientLoginInfo clientLoginInfo) {

			try {
				ClientMaster cm = clientMasterRepository.findOne(clientLoginInfo.getClientMaster().getId());
				if (cm != null) {
					cm.setLastLogoutTime(new Date());
					cm.setLoggedInFlag("N");
					clientLoginInfo.setLogoutTime(new Timestamp(System.currentTimeMillis()));
					clientLoginInfoRepository.save(clientLoginInfo);
					cm.setFinexaUser("Y");
					clientMasterRepository.save(cm);
					ClientMasterDTO dto = mapper.map(cm, ClientMasterDTO.class);
					
					cacheInfoService.deleteTokenCacheMap(token);
					UserClientRedis userClientRedis = cacheInfoService.getClientCacheMap(token, cm.getId());
					 if (userClientRedis != null) {
						cacheInfoService.deleteClientCacheMap(userClientRedis.getId(), token);
					}
					return dto;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}
	
	@Override
	public List<AdvisorUserLoginInfoDTO> findByLoggedInId(int loggedId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			AdvisorUser au = advisorUserRepository.findOne(loggedId);
			List<AdvisorUserLoginInfoDTO> listDTO = new ArrayList<AdvisorUserLoginInfoDTO>();
			for (AdvisorUserLoginInfo obj : au.getAdvisorUserLoginInfos()) {
				AdvisorUserLoginInfoDTO dto = mapper.map(obj, AdvisorUserLoginInfoDTO.class);
				dto.setLoggedInUserName(obj.getAdvisorUser().getFirstName() + " " + obj.getAdvisorUser().getLastName());
				long loginTime = obj.getLoginTime().getTime();
				Date loginTimeDate = new Date(loginTime);
				dto.setLoginTime(loginTimeDate);
				if (obj.getLogoutTime() != null) {
					long logoutTime = obj.getLogoutTime().getTime();
					Date logoutTimeDate = new Date(logoutTime);
					dto.setLogoutTime(logoutTimeDate);
				}

				listDTO.add(dto);
			}
			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
		//pagination==========================
		@Override
		public List<AdvisorUserLoginInfoDTO> findByLoggedInIdWithPagination(int loggedId, Pageable pageable) {
			
			AdvisorUser au = advisorUserRepository.findOne(loggedId);
			List<AdvisorUserLoginInfoDTO> listDTO = new ArrayList<AdvisorUserLoginInfoDTO>();
			List<AdvisorUserLoginInfo> advisorUserLoginInfos = advisorUserLoginInfoRepository.findByAdvisorUserOrderByLoginTimeDesc(au, pageable);
			System.out.println("lst size = " + advisorUserLoginInfos.size());
			for (AdvisorUserLoginInfo obj : advisorUserLoginInfos) {
				AdvisorUserLoginInfoDTO dto = mapper.map(obj, AdvisorUserLoginInfoDTO.class);
				dto.setLoggedInUserName(obj.getAdvisorUser().getFirstName() + " " + obj.getAdvisorUser().getLastName());
				long loginTime = obj.getLoginTime().getTime();
				Date loginTimeDate = new Date(loginTime);
				dto.setLoginTime(loginTimeDate);
				if (obj.getLogoutTime() != null) {
					long logoutTime = obj.getLogoutTime().getTime();
					Date logoutTimeDate = new Date(logoutTime);
					dto.setLogoutTime(logoutTimeDate);
				}

				listDTO.add(dto);
			}
			return listDTO;
		
		}
		
		@Override
		public int getLoggedInHistoryCount(int loggedId) {
			AdvisorUser au = advisorUserRepository.findOne(loggedId);
			long loggedInHistoryCount = advisorUserLoginInfoRepository.countByAdvisorUser(au);
			System.out.println("count = " + loggedInHistoryCount);
			return (int) loggedInHistoryCount;
		}
		//======================================

	@Override
	public void saveAdvisorUserLogo(MultipartFile uploadfile, int id) throws IOException {

		AdvisorUser adUser = advisorUserRepository.getOne(id);

		adUser.setLogo(uploadfile.getBytes());
		advisorUserRepository.save(adUser);

	}

	@Override
	public byte[] getAdvisorLogo(int id) throws IOException {

		AdvisorUser adUser = advisorUserRepository.getOne(id);

		return adUser.getLogo();
	}

	@Override
	public boolean checkEmailExists(String email) throws RuntimeException {
		try {
			boolean flag = false;
			AdvisorUser listAdvisorUser = advisorUserRepository.findByEmailID(email);
			if (listAdvisorUser != null) {
				flag = true;
			}

			System.out.println("flag: " + flag);
			return flag;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}
	
	//for client Forgot email checking
	@Override
	public boolean checkEmailExistsForClient(String email) throws RuntimeException {
		try {
			boolean flag = false;
			ClientContact clientContact = clientContactRepository.findByEmailID(email);
			if (clientContact != null) {
				flag = true;
			}

			System.out.println("flag: " + flag);
			return flag;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean checkPasswordExists(String email, String password) throws RuntimeException {
		try {
			boolean flag = false;
			AdvisorUser listAdvisorUser = advisorUserRepository.findByEmailID(email);
			if (listAdvisorUser != null) {
				if (listAdvisorUser.getLoginPassword().toLowerCase().equals(password.toLowerCase())) {
					flag = true;
				}
			}
			return flag;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean checkUserExist(String email) throws RuntimeException {
		try {
			boolean flag = false;
			AdvisorUser listAdvisorUser = advisorUserRepository.findByEmailID(email);
			if (listAdvisorUser != null) {
				System.out.print(listAdvisorUser.getActiveFlag());
				if (listAdvisorUser.getActiveFlag().toLowerCase().equals("y")) {
					flag = true;
				}
			}

			System.out.println("flag: " + flag);
			return flag;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String encodedUserId(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		String inputUserId = String.valueOf(userId);

		String encodedUserIdLevel1 = Base64.getEncoder().encodeToString(inputUserId.getBytes());
		String encodedUserIdLevel2 = Base64.getEncoder().encodeToString(encodedUserIdLevel1.getBytes());
		String encodedUserIdLevel3 = Base64.getEncoder().encodeToString(encodedUserIdLevel2.getBytes());

		System.out.println("inputUserId: " + inputUserId + " encodedUserId: " + encodedUserIdLevel3);

		return encodedUserIdLevel3;
	}

	@Override
	public String decodedUserId(String encodedUserId) throws RuntimeException {
		// TODO Auto-generated method stub

		// decrypting third level
		byte[] decodedUserIdLevel3ByteArr = Base64.getDecoder().decode(encodedUserId);
		String decodedUserIdLevel3 = new String(decodedUserIdLevel3ByteArr);

		// decrypting second level
		byte[] decodedUserIdLevel2ByteArr = Base64.getDecoder().decode(decodedUserIdLevel3);
		String decodedUserIdLevel2 = new String(decodedUserIdLevel2ByteArr);

		// decrypting first level
		byte[] decodedUserIdLevel1ByteArr = Base64.getDecoder().decode(decodedUserIdLevel2);
		String decodedUserIdLevel1 = new String(decodedUserIdLevel1ByteArr);

		return decodedUserIdLevel1;
	}

	@Override
	public String validateForgotUserPassword(String encodeID) {
		// TODO Auto-generated method stub
		String userID = decodedUserId(encodeID);

		return null;
	}

	@Override
	public String verifiyUserLink(String encodedUserId, double timestamp) throws RuntimeException {
		String message = null;
		String userID = encodedUserId;
		System.out.println(userID);
		// AdvisorUser adUser = advisorUserRepository.findOne(Integer.parseInt(userID));
		AdvisorForgotPassword adForgot = advisorForgotPasswordRepository.findByUUID(userID);
		if (adForgot != null) {
			//System.out.println("UUID-" + adForgot.getUuid());
			double uUIDsentTimestamp = advisorForgotPasswordRepository.getTimestampforUUID(encodedUserId);
			//System.out.println("timestamp user UUID sent time-" + uUIDsentTimestamp);
			//System.out.println("timestamp currently" + timestamp);
			double uUIDTimeout = 300000;// timeout time
			if ((adForgot != null) && (timestamp <= (uUIDsentTimestamp + uUIDTimeout))) {
				message = "valid";
			} else {
				message = "invalid link";
			}
		} else {
			message = "invalid link";
		}
		return message;

	}
	
	@Override
	public String verifiyUserLinkforClient(String encodedUserId, double timestamp) throws RuntimeException {
		String message = null;
		String userID = encodedUserId;
		//System.out.println(userID);
		// AdvisorUser adUser = advisorUserRepository.findOne(Integer.parseInt(userID));
		ClientForgotPassword forgot = clientForgotPasswordRepository.findByUUID(userID);
		if (forgot != null) {
			//System.out.println("UUID-" + forgot.getUuid());
			double uUIDsentTimestamp = clientForgotPasswordRepository.getTimestampforUUID(encodedUserId);
			//System.out.println("timestamp user UUID sent time-" + uUIDsentTimestamp);
			//System.out.println("timestamp currently" + timestamp);
			double uUIDTimeout = 300000;// timeout time
			if ((forgot != null) && (timestamp <= (uUIDsentTimestamp + uUIDTimeout))) {
				message = "valid";
			} else {
				message = "invalid link";
			}
		} else {
			message = "invalid link";
		}
		return message;
	}

	@Override
	public boolean changePass(String userID, String password, String dateTime) {
		// TODO Auto-generated method stub
		String userIDdecodeded = decodedUserId(userID);
		Date date = StringToDate(dateTime);
		return false;
	}

	public Date StringToDate(String s) {

		Date result = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result = dateFormat.parse(s);
		}

		catch (ParseException e) {
			e.printStackTrace();

		}
		return result;
	}

	@Override
	public String generateUUID() {
		// TODO Auto-generated method stub
		String uuid = UUID.randomUUID().toString().replace("-", "");
		System.out.println("uuid = " + uuid);
		return uuid;

	}

	@Override
	public boolean advisor_change_Pass(String uuid, String password) {
		// TODO Auto-generated method stub
		int advisor_id = advisorForgotPasswordRepository.getAdvisorId(uuid);
		AdvisorUser advUser = advisorUserRepository.findOne(advisor_id);
		System.out.println(advisor_id);
		System.out.println(password);

		advisorUserRepository.updatepassword(advisor_id, password);
		User user = advUser.getUser();
		user.setLoginPassword(password);
		user = userRepository.save(user);
		advisorForgotPasswordRepository.deleteRow(uuid);
		return true;
	}
	
	@Override
	public boolean client_change_Pass(ClientForgotPassword clientForgotPassword, String password) {
		ClientMaster clientMaster = clientMasterRepository.findOne(clientForgotPassword.getClientMaster().getId());

		clientMaster.setLoginPassword(password);
		clientMaster.setFinexaUser("Y");
		clientMasterRepository.save(clientMaster);
		
		clientForgotPasswordRepository.delete(clientForgotPassword);
		return true;
	}

	@Override
	public ClientUCCResultDTO registerBSEcredentials(String username, String memberId, String password, int advisorUserID, int mode) {

		// TODO Auto-generated method stub
		ClientUCCResultDTO resultDto = new ClientUCCResultDTO();
		try {
			if(username.equals("NULL")) {
				advisorUserRepository.deleteBSEDetails(advisorUserID);
				resultDto.setStatus(true);
				resultDto.setMessage(MFTransactConstant.ADVISOR_DATA_DELETED);
				resultDto.setStatusCode(MFTransactConstant.STATUS_CODE_SUCCESS);
			} else {
				MFUPLOADSoapService soapClient = new MFUPLOADSoapService();
				resultDto = soapClient.authenticateMFUploadService(username, memberId, password, "8A655",mode);
				if (resultDto.isStatus()) {
					resultDto.setStatusCode(MFTransactConstant.STATUS_CODE_SUCCESS);
					 String key = "Bar12345Bar12345"; // 128 bit key
			            // Create key and cipher
			            SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
			            Cipher cipher = Cipher.getInstance("AES");
			            // encrypt the text
			            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			            byte[] encrypted = cipher.doFinal(password.getBytes());

			            /*StringBuilder sb = new StringBuilder();
			            for (byte b: encrypted) {
			                sb.append((char)b);
			            }
			            // the encrypted String
			            String enc = sb.toString();
			            System.out.println("encrypted:" + enc);*/
			            AdvisorUser adv = advisorUserRepository.findOne(advisorUserID);
			            adv.setBseUsername(username);
			            adv.setBseMemberId(memberId);
			            adv.setBsePassword(encrypted);
			            LookupTransactBSEAccessMode lookupTransactBSEAccessMode = lookupTransactBSEAccessModeRepository.findOne((byte)mode);
			           if (lookupTransactBSEAccessMode != null) {
			        	   adv.setLookupTransactBseaccessMode(lookupTransactBSEAccessMode); 
			           }
					advisorUserRepository.save(adv);
				} else {
					resultDto.setStatusCode(STATUS_USER_NOT_REGISTERED);
				}
			} 
			
		} catch (Exception e) {
			log.debug("Error while updating bse details" + e.getMessage());
		}
		return resultDto;
	
		
		
	}
	
//	@Override
//	public List<AdvisorDTO> findAll() {
//		AdvisorDTO advisorDTO;
//		List<AdvisorUser> listUser = advisorUserRepository.findAll();
//		System.out.println("advisor list size :" + listUser.size());
//		List<AdvisorDTO> listDTO = new ArrayList<AdvisorDTO>();
//		for (AdvisorUser advisorUser : listUser) {
//
//			if (advisorUser.getUser().getAdmin() == null || advisorUser.getUser().getAdmin().equalsIgnoreCase("N")) {
//				advisorDTO = mapper.map(advisorUser, AdvisorDTO.class);
//				// userDTO.setClientId(user.getClientMaster().getId());
//				User user = advisorUser.getUser();
//				advisorDTO.setRole(user.getAdvisorRole().getRoleDescription());
//				advisorDTO.setUserId(user.getId());
//				listDTO.add(advisorDTO);
//			}
//
//		}
//
//		return listDTO;
//	}
	
	@Override
	public List<AdvisorDTO> findAllAdvisorAdmin() {
		AdvisorDTO advisorDTO;
		List<AdvisorUser> listUser = advisorUserRepository.findAll();
		System.out.println("advisor list size :" + listUser.size());
		List<AdvisorDTO> listDTO = new ArrayList<AdvisorDTO>();
		for (AdvisorUser advisorUser : listUser) {

			if (advisorUser.getUser().getAdmin() == null || advisorUser.getUser().getAdmin().equalsIgnoreCase("N")) {
				if (advisorUser.getUser().getAdvisorAdmin() != null && advisorUser.getUser().getAdvisorAdmin().equalsIgnoreCase("Y")) {
					advisorDTO = mapper.map(advisorUser, AdvisorDTO.class);
					// userDTO.setClientId(user.getClientMaster().getId());
					User user = advisorUser.getUser();
					advisorDTO.setRole(user.getAdvisorRole().getRoleDescription());
					advisorDTO.setUserId(user.getId());
					advisorDTO.setEmailID(user.getLoginUsername());
					listDTO.add(advisorDTO);
				}
			}

		}

		return listDTO;
	}
	
	//pagination==========================
	@Override
	public List<AdvisorDTO> findAllWithPagination(Pageable pageable) {
		//AdvisorDTO advisorDTO = new AdvisorDTO();
		//Page<AdvisorUser> listUser = advisorUserRepository.findAll(pageable);
		Page<User> users = userRepository.findByAdvisorAdmin("Y", pageable);
		//System.out.println("advisor list size :" + users.getSize());
		List<AdvisorDTO> listDTO = new ArrayList<AdvisorDTO>();
		for (User user : users) {

//			if (advisorUser.getUser().getAdmin() == null || advisorUser.getUser().getAdmin().equalsIgnoreCase("N")) {
//				if (advisorUser.getUser().getAdvisorAdmin() != null && advisorUser.getUser().getAdvisorAdmin().equalsIgnoreCase("Y")) {
//					advisorDTO = mapper.map(advisorUser, AdvisorDTO.class);
//					// userDTO.setClientId(user.getClientMaster().getId());
//					User user = advisorUser.getUser();
//					advisorDTO.setRole(user.getAdvisorRole().getRoleDescription());
//					advisorDTO.setAdvisorAdmin(user.getAdvisorAdmin());
//					advisorDTO.setUserId(user.getId());
//					System.out.println("name : " + advisorDTO.getLoginUsername());
//					listDTO.add(advisorDTO);
//				}
//			}
			AdvisorDTO advisorDTO = new AdvisorDTO();
			
			advisorDTO.setFirstName(user.getAdvisorUser().getFirstName());
			advisorDTO.setLastName(user.getAdvisorUser().getLastName());
			advisorDTO.setEmailID(user.getLoginUsername());
			advisorDTO.setActiveFlag(user.getActiveFlag());
			advisorDTO.setRole(user.getAdvisorRole().getRoleDescription());
			advisorDTO.setAdvisorAdmin(user.getAdvisorAdmin());
			advisorDTO.setUserId(user.getId());
			//System.out.println("name : " + advisorDTO.getLoginUsername());
			listDTO.add(advisorDTO);
		}

		return listDTO;
	}
	
	@Override
	public List<AdvisorDTO> findAllSelf(Pageable pageable) {
		AdvisorDTO advisorDTO;
		List<AdvisorDTO> listDTO = null;
		try {
			List<AdvisorMaster> listUser = advisorMasterRepository.findByOrgFlag("N", pageable);
			List<AdvisorUser> advisorUserList = new ArrayList<AdvisorUser>();
			for (AdvisorMaster advisorMaster : listUser) {
				
				System.out.println(advisorMaster.getId());
				// advisorUserList.add((AdvisorUser)
				// advisorUserRepository.findByAdvisorMaster(advisorMaster.getId()));
				advisorUserList.addAll(advisorMaster.getAdvisorUsers());
			}

			listDTO = new ArrayList<AdvisorDTO>();
			for (AdvisorUser advisorUser : advisorUserList) {
				if (advisorUser.getUser().getAdmin() == null
						|| advisorUser.getUser().getAdmin().equalsIgnoreCase("N")) {
					advisorDTO = mapper.map(advisorUser, AdvisorDTO.class);
					advisorDTO.setAdvisorUserName(advisorUser.getFirstName() + " "
							+ (advisorUser.getMiddleName() == null ? " " : advisorUser.getMiddleName()) + " "
							+ advisorUser.getLastName());
					// userDTO.setClientId(user.getClientMaster().getId());
					advisorDTO.setBudgetManagementView(advisorUser.getUser().getBudgetManagementView());
					advisorDTO.setGoalPlanningView(advisorUser.getUser().getGoalPlanningView());
					advisorDTO.setGoalPlanningAddEdit(advisorUser.getUser().getGoalPlanningAddEdit());
					advisorDTO.setPortfolioManagementView(advisorUser.getUser().getPortfolioManagementView());
					advisorDTO.setPortfolioManagementAddEdit(advisorUser.getUser().getPortfolioManagementAddEdit());
					advisorDTO.setFinancialPlanningView(advisorUser.getUser().getFinancialPlanningView());
					advisorDTO.setFinancialPlanningAddEdit(advisorUser.getUser().getFinancialPlanningAddEdit());
					advisorDTO.setAdvisorAdmin(advisorUser.getUser().getAdvisorAdmin());
					
					System.out.println("ID = " + advisorUser.getUser().getId() + " AdvisorAdmin self = " + advisorUser.getUser().getAdvisorAdmin());
					
					listDTO.add(advisorDTO);
				}

			}
		} catch (MappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listDTO;
	}
	
	@Override
	public List<AdvisorDTO> findAllOrg(int advisorid, Pageable pageable) {
		AdvisorDTO advisorDTO;
		List<AdvisorDTO> listDTO = null;
		try {
			AdvisorMaster master = advisorMasterRepository.findById(advisorid);
			//List<AdvisorUser> advisorUserList = new ArrayList<AdvisorUser>();
			//advisorUserList.addAll(master.getAdvisorUsers());
			
			List<AdvisorUser> advisorUserList = advisorUserRepository.findByAdvisorMaster(master, pageable);

			listDTO = new ArrayList<AdvisorDTO>();
			for (AdvisorUser advisorUser : advisorUserList) {
				if (advisorUser.getUser().getAdmin() == null
						|| advisorUser.getUser().getAdmin().equalsIgnoreCase("N")) {
					advisorDTO = mapper.map(advisorUser, AdvisorDTO.class);
					// userDTO.setClientId(user.getClientMaster().getId());
					advisorDTO.setBudgetManagementView(advisorUser.getUser().getBudgetManagementView());
					advisorDTO.setGoalPlanningView(advisorUser.getUser().getGoalPlanningView());
					advisorDTO.setGoalPlanningAddEdit(advisorUser.getUser().getGoalPlanningAddEdit());
					advisorDTO.setPortfolioManagementView(advisorUser.getUser().getPortfolioManagementView());
					advisorDTO.setPortfolioManagementAddEdit(advisorUser.getUser().getPortfolioManagementAddEdit());
					advisorDTO.setFinancialPlanningView(advisorUser.getUser().getFinancialPlanningView());
					advisorDTO.setFinancialPlanningAddEdit(advisorUser.getUser().getFinancialPlanningAddEdit());
					advisorDTO.setAdvisorAdmin(advisorUser.getUser().getAdvisorAdmin());
					
					System.out.println("ID = " + advisorUser.getUser().getId() + " AdvisorAdmin org = " + advisorUser.getUser().getAdvisorAdmin());
					
					listDTO.add(advisorDTO);
				}
			}
		} catch (MappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listDTO;
	}
	
	@Override
	public int getAdvisorCount() {
		//long advisorCount = advisorUserRepository.count()-1;
		long advisorCount = userRepository.countByAdvisorAdmin("Y");

		return (int) advisorCount;
	}
	
	@Override
	public List<AdvisorDTO> searchMatchingEntries(String matchString) {
		
		List<AdvisorDTO> advisorDTOs = new ArrayList<>();
		List<AdvisorUser> advisorUsers = advisorUserRepository.findByfirstNameContainingIgnoreCase(matchString);
		
		for (AdvisorUser obj : advisorUsers) {
			AdvisorDTO advisorDTO = new AdvisorDTO();
			mapper.map(obj, advisorDTO);
			advisorDTOs.add(advisorDTO);
		}

		return advisorDTOs;
	}
	
	@Override
	public int getSelfAdvisorCount() {
		//long advisorCount = advisorUserRepository.count()-1;
		long advisorCount = advisorMasterRepository.countByOrgFlag("N");

		return (int) advisorCount;
	}
	
	@Override
	public int getOrgAdvisorCount(int masterId) {
		//long advisorCount = advisorUserRepository.count()-1;
		AdvisorMaster advisorMaster = advisorMasterRepository.findById(masterId);
		long advisorCount = userRepository.countByAdvisorMasterAndAdvisorAdmin(advisorMaster, "Y");

		return (int) advisorCount;
	}
	//======================================
	
	@Override
	public List<UserDTO> findAllHierarchyList(int advisorID) {
		List<UserDTO> userDTOList = null;
		FinexaUtil finexaUtil = null;
		try {
			AdvisorUser advUser = advisorUserRepository.findOne(advisorID);
			finexaUtil = new FinexaUtil();
			userDTOList = finexaUtil.findAllHierarchyList(advUser,advisorUserSupervisorMappingRepository);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userDTOList;
	}
	//======================================
	//no extra checking needed because UM is only given Advisor Admin
	@Override
	public int checkIfUserExistUnderLoggedInAdvisorAdmin(int advisorID) {
		AdvisorUser advUser;
		AdvisorMaster advisorMaster;
		List<AdvisorUser> advisorUsers = null;
		try {
			advUser = advisorUserRepository.findOne(advisorID);
			advisorMaster = advUser.getAdvisorMaster();
			advisorUsers = advisorUserRepository.findByAdvisorMaster(advisorMaster);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return advisorUsers.size();
	}
	
	@Override
	public List<AdvisorDTO> findAllMaster() {
		AdvisorDTO advisorDTO;
		List<AdvisorMaster> listUser = advisorMasterRepository.findAll();

		List<AdvisorDTO> listDTO = new ArrayList<AdvisorDTO>();
		for (AdvisorMaster advisorUser : listUser) {
			advisorDTO = mapper.map(advisorUser, AdvisorDTO.class);
			// userDTO.setClientId(user.getClientMaster().getId());
			listDTO.add(advisorDTO);
		}

		return listDTO;
	}

	
	
	
	@Override
	public List<AdvisorDTO> findSelf() {
		AdvisorDTO advisorDTO;
		List<AdvisorDTO> listDTO = null;
		try {
			List<AdvisorMaster> listUser = advisorMasterRepository.findByOrgFlag("N");
			List<User> advisorUserList = new ArrayList<User>();
			for (AdvisorMaster advisorMaster : listUser) {
				//System.out.println(advisorMaster.getId());
				advisorUserList.addAll(advisorMaster.getUsers());
			}
			listDTO = new ArrayList<AdvisorDTO>();
			for (User user : advisorUserList) {
			advisorDTO = mapper.map(user, AdvisorDTO.class);
			advisorDTO.setAdvisorUserName(user.getAdvisorUser().getFirstName() + " "
					+ (user.getAdvisorUser().getMiddleName() == null ? " " : user.getAdvisorUser().getMiddleName()) + " "
					+ user.getAdvisorUser().getLastName());
			advisorDTO.setLoginUsername(user.getAdvisorUser().getLoginUsername());
			listDTO.add(advisorDTO);
			}
		    }catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		 return listDTO;
	}
	
	@Override
	public List<AdvisorDTO> findAllSelf() {
		AdvisorDTO advisorDTO;
		List<AdvisorDTO> listDTO = null;
		try {
			List<AdvisorMaster> listUser = advisorMasterRepository.findByOrgFlag("N");
			List<AdvisorUser> advisorUserList = new ArrayList<AdvisorUser>();
			for (AdvisorMaster advisorMaster : listUser) {
				
				System.out.println(advisorMaster.getId());
				// advisorUserList.add((AdvisorUser)
				// advisorUserRepository.findByAdvisorMaster(advisorMaster.getId()));
				advisorUserList.addAll(advisorMaster.getAdvisorUsers());
			}

			listDTO = new ArrayList<AdvisorDTO>();
			for (AdvisorUser advisorUser : advisorUserList) {
				if (advisorUser.getUser().getAdmin() == null
						|| advisorUser.getUser().getAdmin().equalsIgnoreCase("N")) {
					advisorDTO = mapper.map(advisorUser, AdvisorDTO.class);
					advisorDTO.setAdvisorUserName(advisorUser.getFirstName() + " "
							+ (advisorUser.getMiddleName() == null ? " " : advisorUser.getMiddleName()) + " "
							+ advisorUser.getLastName());
					// userDTO.setClientId(user.getClientMaster().getId());
					advisorDTO.setBudgetManagementView(advisorUser.getUser().getBudgetManagementView());
					advisorDTO.setGoalPlanningView(advisorUser.getUser().getGoalPlanningView());
					advisorDTO.setGoalPlanningAddEdit(advisorUser.getUser().getGoalPlanningAddEdit());
					advisorDTO.setPortfolioManagementView(advisorUser.getUser().getPortfolioManagementView());
					advisorDTO.setPortfolioManagementAddEdit(advisorUser.getUser().getPortfolioManagementAddEdit());
					advisorDTO.setFinancialPlanningView(advisorUser.getUser().getFinancialPlanningView());
					advisorDTO.setFinancialPlanningAddEdit(advisorUser.getUser().getFinancialPlanningAddEdit());
					advisorDTO.setAdvisorAdmin(advisorUser.getUser().getAdvisorAdmin());
					
					System.out.println("ID = " + advisorUser.getUser().getId() + " AdvisorAdmin self = " + advisorUser.getUser().getAdvisorAdmin());
					
					listDTO.add(advisorDTO);
				}

			}
		} catch (MappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listDTO;
	}

	/*
	 * @Override public List<AdvisorDTO> findAllOrgName() { AdvisorDTO advisorDTO;
	 * List<AdvisorDTO> listDTO = null; try { List<AdvisorMaster> listMaster =
	 * advisorMasterRepository.findByOrgFlag("Y"); } catch (MappingException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * return listDTO; }
	 */

	@Override
	public List<AdvisorDTO> findAllOrgName() {
		AdvisorDTO advisorDTO;
		List<AdvisorMaster> listMaster = advisorMasterRepository.findByOrgFlag("Y");

		List<AdvisorDTO> listDTO = new ArrayList<AdvisorDTO>();
		for (AdvisorMaster advisorMaster : listMaster) {
			advisorDTO = mapper.map(advisorMaster, AdvisorDTO.class);
			listDTO.add(advisorDTO);
		}

		return listDTO;
	}

	@Override
	public List<AdvisorDTO> findAllOrg(int advisorid) {
		AdvisorDTO advisorDTO;
		List<AdvisorDTO> listDTO = null;
		try {
			AdvisorMaster master = advisorMasterRepository.findById(advisorid);
			List<AdvisorUser> advisorUserList = new ArrayList<AdvisorUser>();

			// advisorUserList.add((AdvisorUser)
			// advisorUserRepository.findByAdvisorMaster(advisorMaster.getId()));
			advisorUserList.addAll(master.getAdvisorUsers());

			listDTO = new ArrayList<AdvisorDTO>();
			for (AdvisorUser advisorUser : advisorUserList) {
				if (advisorUser.getUser().getAdmin() == null
						|| advisorUser.getUser().getAdmin().equalsIgnoreCase("N")) {
					advisorDTO = mapper.map(advisorUser, AdvisorDTO.class);
					// userDTO.setClientId(user.getClientMaster().getId());
					advisorDTO.setBudgetManagementView(advisorUser.getUser().getBudgetManagementView());
					advisorDTO.setGoalPlanningView(advisorUser.getUser().getGoalPlanningView());
					advisorDTO.setGoalPlanningAddEdit(advisorUser.getUser().getGoalPlanningAddEdit());
					advisorDTO.setPortfolioManagementView(advisorUser.getUser().getPortfolioManagementView());
					advisorDTO.setPortfolioManagementAddEdit(advisorUser.getUser().getPortfolioManagementAddEdit());
					advisorDTO.setFinancialPlanningView(advisorUser.getUser().getFinancialPlanningView());
					advisorDTO.setFinancialPlanningAddEdit(advisorUser.getUser().getFinancialPlanningAddEdit());
					advisorDTO.setAdvisorAdmin(advisorUser.getUser().getAdvisorAdmin());
					
					System.out.println("ID = " + advisorUser.getUser().getId() + " AdvisorAdmin org = " + advisorUser.getUser().getAdvisorAdmin());
					
					listDTO.add(advisorDTO);
				}
			}
		} catch (MappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listDTO;
	}

	@Override
	public int deactivateActiveFlag(int id) {
		// TODO Auto-generated method stub
		//AdvisorUser advisorUser = advisorUserRepository.findOne(id);
		List <AdvisorUser> advisorUsers = advisorUserRepository.findOne(id).getAdvisorMaster().getAdvisorUsers();
		// advisorUser = mapper.map(advisorDTO, AdvisorUser.class);
		if (advisorUsers != null ) {
			for (AdvisorUser advisorUser : advisorUsers) {
				advisorUser.setActiveFlag("N");
				advisorUser = advisorUserRepository.save(advisorUser);

				User user = userRepository.findByLoginUsernameAndLoginPassword(advisorUser.getLoginUsername(),
						advisorUser.getLoginPassword());
				user.setActiveFlag("N");
				user = userRepository.save(user);
			}
		}
		return 1;
	}

	@Override
	public int activateActiveFlag(int id) { // TODO Auto-generated
		AdvisorUser advisorUser = advisorUserRepository.findOne(id);
		// advisorUser = mapper.map(advisorDTO, AdvisorUser.class);
		advisorUser.setActiveFlag("Y");
		advisorUser = advisorUserRepository.save(advisorUser);

		User user = userRepository.findByLoginUsernameAndLoginPassword(advisorUser.getLoginUsername(),
				advisorUser.getLoginPassword());
		user.setActiveFlag("Y");
		user = userRepository.save(user);

		return 1;
	}


	@Override
	public DashBoardDTO getClientByOffset(int advisorId, UserClientRedis userDetailsDTO, String token, int userID) {
		System.out.println("view on demand ");
		List<Integer> idlist;
		int totalClient = 0;
		List<ClientMaster> clientMasterListTotal = null;
		List<ClientMaster> clientMasterListonDemand = null;
		List<ClientMaster> ClientMasterListTotalRedis = null;
		int offset = 0;
		int limit = 0;
		String disableFlag = "N";
		final int cons = 100;
		////List<ClientInfoDTO> listDTO = null;
		DashBoardDTO dashboardDto = null;
		int endFlag = 0;
		try {
			dashboardDto = new DashBoardDTO();
			if(userDetailsDTO == null) {
				userDetailsDTO = new UserClientRedis();
			}
			idlist = new ArrayList<Integer>();
			// total Client
			totalClient = userDetailsDTO.getTotalClient();
			// Parent
			AdvisorUser advisorUser = advisorUserRepository.findOne(advisorId);
		    idlist = (new FinexaUtil()).findAllUserHierarchy(advisorUser, advisorUserSupervisorMappingRepository);
			if (userDetailsDTO.getTotalClient() == 0) {
			
				clientMasterListTotal = clientMasterRepository.findByAdvisorIds(idlist,"Y");
				userDetailsDTO.setTotalClient(clientMasterListTotal.size());
				limit = cons;
				userDetailsDTO.setOffset(offset+limit);
				/////dashboardDto.setCientInfoDTOTotalList(listDTO);
			} else {
				offset = userDetailsDTO.getOffset();
				limit = cons;
				userDetailsDTO.setOffset(offset+limit);
			}
			ClientMasterListTotalRedis = userDetailsDTO.getClientmastersTotalRedis();
			if(ClientMasterListTotalRedis == null) {
				ClientMasterListTotalRedis = new ArrayList<ClientMaster>();
			}
			
			if(ClientMasterListTotalRedis.size() == userDetailsDTO.getTotalClient()) {
				endFlag = 1;
			}
			//ondemand
			if(ClientMasterListTotalRedis.size() != userDetailsDTO.getTotalClient())
			{
			Pageable pageable = new OffsetBasedPageRequest(offset, limit);
			if(userDetailsDTO.getTotalClient() > cons) {
		    clientMasterListonDemand = clientMasterRepository.findByAdvisorIds(idlist, "Y", pageable);
			}else {
			clientMasterListonDemand = 	clientMasterListTotal;
			}
			
			userDetailsDTO.setClientmasters(clientMasterListonDemand);
			//Final List after retrieve
			ClientMasterListTotalRedis.addAll(clientMasterListonDemand);
			userDetailsDTO.setClientmastersTotalRedis(ClientMasterListTotalRedis);
			//////dashboardDto.setCientInfoDTOOnDemandListSize(clientMasterListonDemand.size());
			}
			if (ClientMasterListTotalRedis.size() == userDetailsDTO.getTotalClient()) {
				// remainingClient =userDetailsDTO.getTotalClient() - userDetailsDTO.getFinalClientmasterlist().size();
				disableFlag = "Y";
			} else {
				disableFlag = "N";
			}
			dashboardDto.setDisableFlag(disableFlag);
			userDetailsDTO.setId(userID);
			if(endFlag != 1) {
			cacheInfoService.addClientCacheMap(token, userDetailsDTO);
			}
			////userDetailsDTO = cacheInfoService.getClientCacheMap(token, userID);
			dashboardDto.setTotalClientSize(userDetailsDTO.getTotalClient());
			////dashboardDto.setStoredRedisId(userDetailsDTO.getId()); 
			////dashboardDto.setLoggedID(advisorId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dashboardDto;
	}
	
	@Override
	public void deletetAUMCacheMap(int advisorUserId, HttpServletRequest request) {
		String header = request.getHeader(Constants.HEADER_STRING);
		String token = cacheInfoService.getToken(header);
		cacheInfoService.deleteClientCacheMap(advisorUserId,token);			
	}
	
	@Override
	public AdvisorUserLoginInfoDTO checkLoggedInOrNot(String username) throws RuntimeException {
		AdvisorUserLoginInfo advisorUserLoginInfo;
		AdvisorUserLoginInfoDTO advisorUserLoginInfoDTO = new AdvisorUserLoginInfoDTO();
		try {
			AdvisorUser advisorUser = advisorUserRepository.findByLoginUsername(username);
			if(advisorUser != null) {
			if(advisorUser.getLoggedInFlag().equalsIgnoreCase("N")) {
				advisorUserLoginInfoDTO.setLoggedInflag("N");
				return advisorUserLoginInfoDTO;
			}else {
				advisorUserLoginInfoDTO.setLoggedInflag("Y");
				advisorUserLoginInfo = advisorUserLoginInfoRepository.findTopByAdvisorUserOrderByLoginTimeDesc(advisorUser);
				advisorUserLoginInfoDTO.setToken(advisorUserLoginInfo.getToken());
				return advisorUserLoginInfoDTO;
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return advisorUserLoginInfoDTO;
	}
	
	@Override
	public ClientLoginInfoDTO checkLoggedInOrNotForClient(String username) throws RuntimeException {
		ClientLoginInfo clientLoginInfo;
		ClientLoginInfoDTO clientLoginInfoDTO = new ClientLoginInfoDTO();
		try {
			ClientMaster clientMaster = clientMasterRepository.findByLoginUsername(username);
			if(clientMaster != null) {
			if(clientMaster.getLoggedInFlag().equalsIgnoreCase("N")) {
				clientLoginInfoDTO.setLoggedInflag("N");
				return clientLoginInfoDTO;
			}else {
				clientLoginInfoDTO.setLoggedInflag("Y");
				clientLoginInfo = clientLoginInfoRepository.findTopByClientMasterOrderByLoginTimeDesc(clientMaster);
				clientLoginInfoDTO.setToken(clientLoginInfo.getToken());
				return clientLoginInfoDTO;
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientLoginInfoDTO;
	}

	@Override
	public List<AdvisorDTO> getAllActiveUsersByUnderAdvisorAdmin(int advisorID) throws RuntimeException {
		List<AdvisorDTO> activeUsers = new ArrayList<AdvisorDTO>();
		try {
			List<AdvisorUser> advisorUsers = advisorUserRepository.findOne(advisorID).getAdvisorMaster().getAdvisorUsers();
			
			for (AdvisorUser user : advisorUsers) {
				if(user.getUser().getAdvisorAdmin() == null || user.getUser().getAdvisorAdmin().equalsIgnoreCase("N")) {
					if (user.getActiveFlag().equalsIgnoreCase("Y")) {
						AdvisorDTO dto = mapper.map(user, AdvisorDTO.class);
						activeUsers.add(dto);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return activeUsers;
	}

	@Override
	public List<AdvisorDTO> getAllInactiveUsersByUnderAdvisorAdmin(int advisorID) throws RuntimeException {
		List<AdvisorDTO> activeUsers = new ArrayList<AdvisorDTO>();
		try {
			List<AdvisorUser> advisorUsers = advisorUserRepository.findOne(advisorID).getAdvisorMaster().getAdvisorUsers();
			
			for (AdvisorUser user : advisorUsers) {
				if(user.getUser().getAdvisorAdmin() == null || user.getUser().getAdvisorAdmin().equalsIgnoreCase("N")) {
					if (user.getActiveFlag().equalsIgnoreCase("N")) {
						AdvisorDTO dto = mapper.map(user, AdvisorDTO.class);
						activeUsers.add(dto);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return activeUsers;
	}

	@Override
	public int changeUserActiveStatus(int[] advisorIDs) {
		//System.out.println("length " + advisorIDs.length);
		for (int id : advisorIDs) {
			AdvisorUser advisorUser = advisorUserRepository.findById(id);
			if (advisorUser.getActiveFlag().equalsIgnoreCase("Y")) {
				advisorUser.setActiveFlag("N");
				advisorUser.getUser().setActiveFlag("N");
			} else if (advisorUser.getActiveFlag().equalsIgnoreCase("N")) {
				advisorUser.setActiveFlag("Y");
				advisorUser.getUser().setActiveFlag("Y");
			}
			advisorUserRepository.save(advisorUser);
		}
		
		return 1;
	}

	@Override
	public AdvisorDTO storePassword(AdvisorDTO advisorDTO, HttpServletRequest httpServletRequest)
			throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			AdvisorUser advUser = advisorUserRepository.findOne(advisorDTO.getId());
			AdvisorMaster advMaster = advUser.getAdvisorMaster();
			
			advMaster.setCamsPassword(advisorDTO.getCamsPassword());
			advMaster.setKarvyPassword(advisorDTO.getKarvyPassword());
			advMaster.setFranklinPassword(advisorDTO.getFranklinPassword());
			advMaster.setSundaramPassword(advisorDTO.getSundaramPassword());
			advMaster = advisorMasterRepository.save(advMaster);
			return advisorDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<AdvisorDTO> getAllDistributors() throws RuntimeException {
		// TODO Auto-generated method stub
		List<AdvisorUser> advUserList = advisorUserRepository.findAll();
		List<AdvisorDTO> advDTOList = new ArrayList<AdvisorDTO>();
		for(AdvisorUser advUserObj : advUserList) {
			advDTOList.add(mapper.map(advUserObj, AdvisorDTO.class));
		}
		return advDTOList;
	}

	@Override
	public int getLogCount(int userID) {
		// TODO Auto-generated method stub
		int count = advisorUserBulkUploadHistoryRepository.countByAdvisorUser(advisorUserRepository.findOne(userID));
		return count;
	}

	@Override
	public List<AdvisorUserBulkUploadHistoryDTO> bulkUploadLog(int userID, Pageable pageable) {
		// TODO Auto-generated method stub
		List<AdvisorUserBulkUploadHistoryDTO> advisorUserBulkUploadHistoryDTOs = new ArrayList<>();
		List<AdvisorUserBulkUploadHistory> histories = advisorUserBulkUploadHistoryRepository.
				findByAdvisorUser(advisorUserRepository.findOne(userID), pageable);
		//List<AdvisorUserBulkUploadHistory> histories = advisorUserBulkUploadHistoryRepository.
		//		findByStatusIgnoreCaseAndAdvisorUser("completed", advisorUserRepository.findOne(userID), pageable);
		for (AdvisorUserBulkUploadHistory obj : histories) {
			AdvisorUserBulkUploadHistoryDTO dto = mapper.map(obj, AdvisorUserBulkUploadHistoryDTO.class);
			dto.setAdvisorUserID(obj.getAdvisorUser().getId());
			advisorUserBulkUploadHistoryDTOs.add(dto);
		}
		return advisorUserBulkUploadHistoryDTOs;
	}

	@Override
	public AdvisorUserBulkUploadHistoryDTO getLogDetails(int logDetailsId) {
		// TODO Auto-generated method stub
		AdvisorUserBulkUploadHistoryDTO dto = null;
		AdvisorUserBulkUploadHistory bulkUploadHistory = advisorUserBulkUploadHistoryRepository.findOne(logDetailsId);
		if (bulkUploadHistory != null) {
			dto = mapper.map(bulkUploadHistory, AdvisorUserBulkUploadHistoryDTO.class);
		}
		return dto;
	}

	@Override
	public AdvisorUserBulkUploadHistoryDTO getRunningProcess(int userID) {
		// TODO Auto-generated method stub
		AdvisorUserBulkUploadHistoryDTO dto = new AdvisorUserBulkUploadHistoryDTO();
		try {
			AdvisorUserBulkUploadHistory bulkUploadHistory = advisorUserBulkUploadHistoryRepository
					.findByAdvisorUserAndStatusIgnoreCase(advisorUserRepository.findOne(userID), "running");
			if (bulkUploadHistory != null) {
				dto = mapper.map(bulkUploadHistory, AdvisorUserBulkUploadHistoryDTO.class);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dto;
	}

}