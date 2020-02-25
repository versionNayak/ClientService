package com.finlabs.finexa.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.dto.AdvisorUserBulkUploadHistoryDTO;
import com.finlabs.finexa.dto.AdvisorUserLoginInfoDTO;
import com.finlabs.finexa.dto.ClientInfoDTO;
import com.finlabs.finexa.dto.ClientLoginInfoDTO;
import com.finlabs.finexa.dto.ClientMasterDTO;
import com.finlabs.finexa.dto.ClientUCCResultDTO;
import com.finlabs.finexa.dto.ConfirmPassDTO;
import com.finlabs.finexa.dto.DashBoardDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.LoginDTO;
import com.finlabs.finexa.dto.OTPObjectDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorForgotPassword;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientForgotPassword;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.model.UserClientRedis;
import com.finlabs.finexa.model.UserInRedis;
import com.finlabs.finexa.repository.AdvisorForgotPasswordRepository;
import com.finlabs.finexa.repository.AdvisorUserBulkUploadHistoryRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.ClientContactRepository;
import com.finlabs.finexa.repository.ClientForgotPasswordRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.AdvisorService;
import com.finlabs.finexa.service.ClientMasterService;
import com.finlabs.finexa.service.EmailService;
import com.finlabs.finexa.service.ViewUserManagmentService;
import com.finlabs.finexa.util.CacheInfoService;
import com.finlabs.finexa.util.Constants;
import com.finlabs.finexa.util.EmailUtil;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.RestClient;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@RestController
public class AdvisorUserController {
	private static Logger log = LoggerFactory.getLogger(AdvisorUserController.class);
	private String changePassURL = "";
	private String generatedURL = "";

	/*
	 * @Autowired public AdvisorUserController(@Value("${GENERATED.URL}") String
	 * generatedURL,
	 * 
	 * @Value("${CHANGE_PASS.URL}") String changePassURL) { this.generatedURL =
	 * generatedURL; this.changePassURL = changePassURL; }
	 */
	
	AdvisorUserController() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
		    input = new FileInputStream("/home/forgot_password.properties");
		    prop.load(input);
		    this.generatedURL = prop.getProperty("generatedURL");
		    this.changePassURL = prop.getProperty("changePassURL");
		} catch (IOException ex) {
		    ex.printStackTrace();
		} 
	}
	
	@Autowired
	AdvisorForgotPasswordRepository advisorForgotPasswordRepository;
	
	@Autowired
	ClientForgotPasswordRepository clientForgotPasswordRepository;
	
	@Autowired
	AdvisorService advisorService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	ClientMasterService clientMasterService;

	@Autowired
	ViewUserManagmentService viewUserManagmentService;

	@Autowired
	RestClient restClient;

	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

	@Autowired
	AdvisorUserRepository advisorUserRepository;

	@Autowired
	private CacheInfoService cacheInfoService;
	@Autowired
	private ClientContactRepository clientContactRepository;
	
	@Autowired
	AdvisorUserBulkUploadHistoryRepository advisorUserBulkUploadHistoryRepository;
	

	
	@RequestMapping(value = "/checkLoggedInOrNot", method = RequestMethod.GET)
	public ResponseEntity<?> checkLoggedInOrNot1(@RequestParam(value = "username") String username) {
		AdvisorUserLoginInfoDTO advisorUserLoginInfoDTO = advisorService.checkLoggedInOrNot(username);
		//AdvisorUserLoginInfoDTO advisorUserLoginInfoDTO = new AdvisorUserLoginInfoDTO();
		return new ResponseEntity<AdvisorUserLoginInfoDTO>(advisorUserLoginInfoDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/checkLoggedInOrNotWithLastToken", method = RequestMethod.GET)
	public ResponseEntity<?> checkLoggedInOrNot() {
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/checkLoggedInOrNotForClient", method = RequestMethod.GET)
	public ResponseEntity<?> checkLoggedInOrNotForClient1(@RequestParam(value = "username") String username) {
		ClientLoginInfoDTO clientLoginInfoDTO = advisorService.checkLoggedInOrNotForClient(username);
		//AdvisorUserLoginInfoDTO advisorUserLoginInfoDTO = new AdvisorUserLoginInfoDTO();
		return new ResponseEntity<ClientLoginInfoDTO>(clientLoginInfoDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/checkLoggedInOrNotWithLastTokenForClient", method = RequestMethod.GET)
	public ResponseEntity<?> checkLoggedInOrNotForClient() {
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> createAdvisor(@Valid @RequestBody AdvisorDTO advisorDTO, Errors errors) {
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}
		AdvisorDTO retDTO = advisorService.save(advisorDTO);
		return new ResponseEntity<AdvisorDTO>(retDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/forgotPassword/authenticateUser/{encodedUserId}", method = RequestMethod.GET)
	public String authenticateForgotPass(@PathVariable String encodedUserId, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("userCenter/loginPage");
		try {
			double userCurrentTimestamp = System.currentTimeMillis();
			String message = advisorService.verifiyUserLinkforClient(encodedUserId, userCurrentTimestamp);
			System.out.println("message : " + message);
			if (message.equals("valid")) {
				AdvisorUserController advisorUserController = new AdvisorUserController();
				response.sendRedirect(advisorUserController.changePassURL + "/changePassword.html?userType=" + encodedUserId);
			} else {
				message = advisorService.verifiyUserLink(encodedUserId, userCurrentTimestamp);
				System.out.println("message : " + message);
				if (message.equals("valid")) {
					AdvisorUserController advisorUserController = new AdvisorUserController();
					response.sendRedirect(advisorUserController.changePassURL + "/changePassword.html?userType=" + encodedUserId);
				}
			}

			return "";
		} catch (Exception e) {

			return "";
		}

	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoView')")
	@SuppressWarnings("unused")
	@RequestMapping(value = "/findLastLoginTime/{username}/{password}", method = RequestMethod.GET)
	public ResponseEntity<?> findLastLoginTime(@PathVariable String username, @PathVariable String password, HttpServletRequest request) {
//	@RequestMapping(value = "/findLastLoginTime", method = RequestMethod.GET)
//	public ResponseEntity<?> findLastLoginTime(@RequestParam("username") String username, 
//			@RequestParam("password") String password, HttpServletRequest request) {
		AdvisorDTO advDTO = null;
		try {
			advDTO = advisorService.findByUsernameAndPassword(username, password);

			String header = request.getHeader(Constants.HEADER_STRING);
			String token = cacheInfoService.getToken(header);
					UserInRedis userDetailsDTO = new UserInRedis();
			if (advDTO != null) {
			userDetailsDTO.setEmailID(advDTO.getEmailID());
			userDetailsDTO.setId(advDTO.getId());
			userDetailsDTO.setUsername(advDTO.getEmailID());
			cacheInfoService.addTokenCacheMap(token, userDetailsDTO);
			return new ResponseEntity<AdvisorDTO>(advDTO, HttpStatus.OK);
			} else {
			ErrorDTO err = new ErrorDTO("NOT_FOUND", "User not Found");
			return new ResponseEntity<ErrorDTO>(err, HttpStatus.OK);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	    @PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
		@RequestMapping(value = "/findLastLoginTimeForClient/{username}/{password}", method = RequestMethod.GET)
		public ResponseEntity<?> findLastLoginTimeForClient(@PathVariable String username, @PathVariable String password,
				HttpServletRequest request) {
	    	ClientInfoDTO masterDTO = advisorService.findByUsernameAndPasswordForClient(username, password);
			String header = request.getHeader(Constants.HEADER_STRING);
			String token = cacheInfoService.getToken(header);
			UserInRedis userDetailsDTO = new UserInRedis();
			if (masterDTO != null) {
			userDetailsDTO.setClientEmailID(masterDTO.getEmailId());
			userDetailsDTO.setClientID(masterDTO.getId());
			userDetailsDTO.setClientMobile(masterDTO.getMobile());
			userDetailsDTO.setClientRETIREMENT_STATUS(masterDTO.getRetirementStatus());
            cacheInfoService.addTokenCacheMap(token, userDetailsDTO);
			return new ResponseEntity<ClientInfoDTO>(masterDTO, HttpStatus.OK);
			} else {
			ErrorDTO err = new ErrorDTO("NOT_FOUND", "Client not Found");
			return new ResponseEntity<ErrorDTO>(err, HttpStatus.OK);
			}

		}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','AdvisorAdmin')")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> loginAdvisor(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
		String header = "";
		String token  = "";
		AdvisorDTO retDTO = null;
		try {
			 header = request.getHeader(Constants.HEADER_STRING);
			 token = cacheInfoService.getToken(header);
			loginDTO.setToken(token);
			UserInRedis userDetailsDTO = cacheInfoService.getTokenCacheMap(header);
			System.out.println("userDetailsDTO " + userDetailsDTO.getId() + "    " + userDetailsDTO.getEmailID());
			retDTO = advisorService.login(loginDTO);
			if (retDTO == null) {
				ErrorDTO err = new ErrorDTO("NOT_FOUND", "User not Found");
				return new ResponseEntity<ErrorDTO>(err, HttpStatus.OK);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<AdvisorDTO>(retDTO, HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/loginForClient", method = RequestMethod.POST)
	public ResponseEntity<?> loginClient(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
		String header = request.getHeader(Constants.HEADER_STRING);
		String token = cacheInfoService.getToken(header);
		loginDTO.setToken(token);
		ClientMasterDTO retDTO = advisorService.loginClient(loginDTO);
		if (retDTO == null) {
			ErrorDTO err = new ErrorDTO("NOT_FOUND", "Client not Found");
			return new ResponseEntity<ErrorDTO>(err, HttpStatus.OK);
		} else {

			return new ResponseEntity<ClientMasterDTO>(retDTO, HttpStatus.OK);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','AdvisorAdmin')")
	@RequestMapping(value = "/logoutFinexa", method = RequestMethod.GET)
	public ResponseEntity<?> logoutAdvisor(
			 @RequestParam("advisorUserId") int advisorUserId, HttpServletRequest request) {
		AdvisorDTO retDTO = null;
		String header = request.getHeader(Constants.HEADER_STRING);
		String token = cacheInfoService.getToken(header);
		
		UserInRedis userDetailsDTO = cacheInfoService.getTokenCacheMap(header);
	//	UserClientRedis userClientRedis = cacheInfoService.getClientCacheMap(token, advisorUserId);
		System.out.println("userDetailsDTO "+userDetailsDTO);
	//	System.out.println("userClientRedis "+userClientRedis);
		if (userDetailsDTO != null) {
	    retDTO = advisorService.logout(token, userDetailsDTO.getId());
	    cacheInfoService.deleteTokenCacheMap(token);
		}
		/*
		 * if (userClientRedis != null){
		 * cacheInfoService.deleteClientCacheMap(userClientRedis.getId(),token); }
		 */
		if (retDTO == null) {
			ErrorDTO err = new ErrorDTO("NOT_FOUND", "User not Found");
			return new ResponseEntity<ErrorDTO>(err, HttpStatus.OK);
		} else {
			// save AdvisorUserlogininfo, lastLogoutTime in AdvisorUser
			
			
			return new ResponseEntity<AdvisorDTO>(retDTO, HttpStatus.OK);
		}
	}
	
	
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView','AdvisorAdmin')")
	@RequestMapping(value = "/logoutFinexaForClient", method = RequestMethod.GET)
	public ResponseEntity<?> logoutClient(
			 @RequestParam("clientID") int clientID, HttpServletRequest request) {
		ClientMasterDTO retDTO = null;
		String header = request.getHeader(Constants.HEADER_STRING);
		String token = cacheInfoService.getToken(header);
		
		UserInRedis userDetailsDTO = cacheInfoService.getTokenCacheMap(header);
	//	UserClientRedis userClientRedis = cacheInfoService.getClientCacheMap(token, advisorUserId);
		System.out.println("userDetailsDTO "+userDetailsDTO);
	//	System.out.println("userClientRedis "+userClientRedis);
		if (userDetailsDTO != null) {
	    retDTO = advisorService.logoutForClient(token, userDetailsDTO.getClientID());
	    cacheInfoService.deleteTokenCacheMap(token);
		}
		/*
		 * if (userClientRedis != null){
		 * cacheInfoService.deleteClientCacheMap(userClientRedis.getId(),token); }
		 */
		if (retDTO == null) {
			ErrorDTO err = new ErrorDTO("NOT_FOUND", "Client not Found");
			return new ResponseEntity<ErrorDTO>(err, HttpStatus.OK);
		} else {
			// save AdvisorUserlogininfo, lastLogoutTime in AdvisorUser
			
			
			return new ResponseEntity<ClientMasterDTO>(retDTO, HttpStatus.OK);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/checkExistingUserPassword/{email}/{password}", method = RequestMethod.GET)
	public ResponseEntity<?> checkExistingUserPassword(@PathVariable String email, @PathVariable String password)
			throws FinexaBussinessException {
		boolean exist = false;
		boolean msg = false;
		try {
			msg = advisorService.checkPasswordExists(email, password);
			if (msg) {
				exist = advisorService.checkUserExist(email);
			}
			return new ResponseEntity<Boolean>(exist, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("", "", "");
		
		}
	}
	
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "/changeUserPassword", method = RequestMethod.POST)
	public ResponseEntity<?> changeUserPassword(@RequestBody ConfirmPassDTO confirmPassDTO) {
		boolean flag = advisorService.changePasswordByEmailId(confirmPassDTO.getPassword(),
				confirmPassDTO.getEmailId());
		if (!flag) {
			ErrorDTO err = new ErrorDTO("InvalidEmail", "The provided Email is not available in the system");
			return new ResponseEntity<>(err, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(flag, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/changePasswordadvisorBeforeLogin", method = RequestMethod.GET)
	public ResponseEntity<?> changePasswordadvisorBeforeLogin(@RequestParam(value = "emailID") String emailID,@RequestParam(value = "password") String password) {
		boolean flag = advisorService.changePasswordByEmailId(password, emailID);
		if (!flag) {
			ErrorDTO err = new ErrorDTO("InvalidEmail", "The provided Email is not available in the system");
			return new ResponseEntity<>(err, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(flag, HttpStatus.OK);
		}
	}


	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value = "/advisorUsers/advisorMaster/{advisorMasterId}", method = RequestMethod.GET)
	public ResponseEntity<?> findAllUsersForOrg(@PathVariable int advisorMasterId) {
		List<AdvisorDTO> list = advisorService.findAllUsersForOrg(advisorMasterId);
		return new ResponseEntity<List<AdvisorDTO>>(list, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value = "advisorUser/changePass/{userID}/{password}/{time}", method = RequestMethod.GET)
	public ResponseEntity<?> changePass(@PathVariable String userID, @PathVariable String password,
			@PathVariable String dateTime) throws FinexaBussinessException {
		try {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);

		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGERMENT_MANAGE_PASSWORD);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_MODULE,
					FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);

		}
	}

	/* gourab */
	/*
	 * @RequestMapping(value =
	 * "advisorUser/advisor_change_Pass/{userID}/{password}", method =
	 * RequestMethod.POST) public ResponseEntity<?>
	 * advisor_change_Pass(@PathVariable String uuid,@PathVariable String
	 * password)throws FinexaBussinessException { try { boolean
	 * f=advisorService.advisor_change_Pass(uuid,password); return new
	 * ResponseEntity<Boolean>(true, HttpStatus.OK);
	 * 
	 * } catch(RuntimeException e) { FinexaBusinessSubmodule subModule =
	 * finexaBusinessSubmoduleRepository
	 * .findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGERMENT_MANAGE_PASSWORD);
	 * FinexaExceptionHandling exception = finexaExceptionHandlingRepository
	 * .findByFinexaBusinessSubmoduleAndErrorCode(subModule,
	 * FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR); throw new
	 * FinexaBussinessException(FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_MODULE,
	 * FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR, exception !=
	 * null ? exception.getErrorMessage() : "", e);
	 * 
	 * } }
	 */
	
	@RequestMapping(value = "/advisor_change_Pass", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> advisor_change_Pass(@RequestParam(value = "uuid") String uuid,
			@RequestParam(value = "password") String password) throws FinexaBussinessException {
		log.info("ClientService >>> Entering changePassword ");
		try {
			boolean status = false;
			AdvisorForgotPassword advisorForgotPassword = advisorForgotPasswordRepository.findByUUID(uuid);
			if (advisorForgotPassword != null) {
				status = advisorService.advisor_change_Pass(uuid, password);
			} else {
				ClientForgotPassword clientForgotPassword = clientForgotPasswordRepository.findByUUID(uuid);
				if (clientForgotPassword != null) {
					status = advisorService.client_change_Pass(clientForgotPassword, password);
				}
			}
			return new ResponseEntity<Boolean>(status, HttpStatus.OK);

		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGERMENT_MANAGE_PASSWORD);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_MODULE,
					FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);

		}
	}

	
	@RequestMapping(value = "advisorUser/sendVerificationLink", method = RequestMethod.GET)
	public ResponseEntity<?> sendVerificationLink(@RequestParam("email") String email) throws FinexaBussinessException {
		try {
			boolean msg = advisorService.checkEmailExists(email);

			if (msg) {
				AdvisorUserController advisorUserController = new AdvisorUserController();
				advisorService.sendVerificationLink(email, advisorUserController.generatedURL);
			}
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.FINEXA_LOGIN);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.FINEXA_LOGIN_FORGOT_PASS_CHECK_EXISTIN_EMAIL_ERROR);
			throw new FinexaBussinessException(FinexaConstant.FINEXA_LOGIN_MODULE,
					FinexaConstant.FINEXA_LOGIN_FORGOT_PASS_CHECK_EXISTIN_EMAIL_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@RequestMapping(value = "clientMaster/sendVerificationLinkForClient", method = RequestMethod.GET)
	public ResponseEntity<?> sendVerificationLinkForClient(@RequestParam("email") String email) throws FinexaBussinessException {
		try {
			boolean msg = advisorService.checkEmailExistsForClient(email);

			if (msg) {
				AdvisorUserController advisorUserController = new AdvisorUserController();
				advisorService.sendVerificationLinkForClient(email, advisorUserController.generatedURL);
			}
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.FINEXA_LOGIN);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.FINEXA_LOGIN_FORGOT_PASS_CHECK_EXISTIN_EMAIL_ERROR);
			throw new FinexaBussinessException(FinexaConstant.FINEXA_LOGIN_MODULE,
					FinexaConstant.FINEXA_LOGIN_FORGOT_PASS_CHECK_EXISTIN_EMAIL_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','UserManagementAddEdit')")
	@RequestMapping(value = "/advisorUsers/{advisorId}/downloadUserTemplate", method = RequestMethod.GET)
	public ResponseEntity<?> downloadUserTemplate(@PathVariable int advisorId, HttpServletResponse response)
			throws FinexaBussinessException, IOException, RowsExceededException, WriteException, CustomFinexaException {
		try {
			advisorService.downloadUserTemplate(response, advisorId);
			return null;
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.DOWNLOAD_USER_TEMPLATE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.BULK_UPLOAD_MODULE,
					FinexaConstant.DOWNLOAD_USER_TEMPLATE_ERROR, exception != null ? exception.getErrorMessage() : "",
					e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','UserManagementAddEdit')")
	@RequestMapping(value = "/advisorUsers/{advisorId}/uploadBulkUsers", method = RequestMethod.POST)
	public ResponseEntity<?> uploadBulkUsers(@PathVariable int advisorId, @ModelAttribute FileuploadDTO fileuploadDTO)
			throws FinexaBussinessException, CustomFinexaException {
		try {
			UploadResponseDTO uploadResponseDTO = advisorService.uploadBulkUsers(advisorId, fileuploadDTO);
			return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.UPLOAD_BULK_USERS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.BULK_UPLOAD_MODULE,
					FinexaConstant.UPLOAD_BULK_USERS_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/resetPass/advisorUser/{advisorId}", method = RequestMethod.POST)
	public ResponseEntity<?> resetPassword(@PathVariable int advisorId) throws FinexaBussinessException {

		try {
			AdvisorUser user = advisorService.resetPassword(advisorId);
			StringBuilder sb = new StringBuilder();
			sb.append("Dear ");
			sb.append(user.getFirstName() + " " + user.getLastName() + ",\n");
			sb.append("\n");
			sb.append("Your Password for Finexa Application has been reset, new password is : ");
			sb.append(user.getLoginPassword() + "\n");
			sb.append("Administrator , Finexa Application \n\n");
			List<String> toList = new ArrayList<String>();
			toList.add(user.getEmailID());

			EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_PASSWORD_RESET, sb.toString());
			
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGERMENT_MANAGE_PASSWORD);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_MODULE,
					FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/resetPass/advisorUser/{advisorId}/{loggedUserId}", method = RequestMethod.POST)
	public ResponseEntity<?> resetPassword(@PathVariable int advisorId, @PathVariable int loggedUserId) throws FinexaBussinessException {

		try {
			AdvisorUser user = advisorService.resetPassword(advisorId);
			AdvisorUser loggeduser = advisorService.resetPassword(loggedUserId);
			if (loggeduser.getUser().getAdmin() != null && loggeduser.getUser().getAdmin().equalsIgnoreCase("Y")) {
				StringBuilder sb = new StringBuilder();
				sb.append("Dear ");
				sb.append(user.getFirstName() + " " + user.getLastName() + ",\n");
				sb.append("\n");
				sb.append("As requested your Password for Finexa application has been reset.\nNew Password : ");
				sb.append(user.getLoginPassword() + "\n\n");
				sb.append("Regards,\nFinexa Admin.");
				List<String> toList = new ArrayList<String>();
				toList.add(user.getEmailID());
	
				EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_PASSWORD_RESET, sb.toString());
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append("Dear ");
				sb.append(user.getFirstName() + " " + user.getLastName() + ",\n");
				sb.append("\n");
				sb.append("As requested your Password for Finexa application has been reset.\nNew Password : ");
				sb.append(user.getLoginPassword() + "\n\n");
				sb.append("Regards,\n");
				sb.append(loggeduser.getFirstName() + " " + loggeduser.getLastName());
				List<String> toList = new ArrayList<String>();
				toList.add(user.getEmailID());
	
				EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_PASSWORD_RESET, sb.toString());
			}
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGERMENT_MANAGE_PASSWORD);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_MODULE,
					FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoView')")
	@RequestMapping(value = "/generateClientCredential", method = RequestMethod.GET)
	public ResponseEntity<?> generateClientCredential(@RequestParam("email") String emailID,
			@RequestParam("serviceIP") String serviceIP,
			@RequestParam("loggedUser") int loggedUser) throws FinexaBussinessException {

		try {
			log.debug("Reset password called ... ");
			//System.out.println("email :" + emailID);
			AdvisorUser advisorUser = advisorUserRepository.findById(loggedUser);
			ClientContact clientContact =  clientContactRepository.findByEmailID(emailID);
			//System.out.println("client contact id : " + clientContact.getId());
			//System.out.println("client master id within clientContact: " + clientContact.getClientMaster().getId());
			ClientMaster clientMaster = clientContact.getClientMaster();
			//System.out.println("client master id : " + clientMaster.getId());
			StringBuilder sb = new StringBuilder();
			sb.append("Dear ");
			sb.append(clientMaster.getFirstName() + " " + clientMaster.getLastName() + ",\n\n");
			sb.append("Welcome to Finexa Client Portal\n" + 
					"Please find below your login credentials to access the portal:\n");
			//System.out.println("client master password : " + clientMaster.getLoginPassword());
			sb.append("User ID : " + clientContact.getEmailID() + "\nPassword : " + clientMaster.getLoginPassword());
			sb.append("\nURL : " + serviceIP + "\n\n");
			sb.append("In case of any queries, feel free to reach on " + advisorUser.getEmailID() + 
					" and " + advisorUser.getPhoneNo() + "\n\n");
			sb.append("Regards,\n" + advisorUser.getFirstName() + " " + advisorUser.getLastName());
			List<String> toList = new ArrayList<String>();
			toList.add(clientContact.getEmailID());
			EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_CLIENT_PASSWORD_GENERATE, sb.toString());
			
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		} catch (RuntimeException e) {
			e.printStackTrace();
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGERMENT_MANAGE_PASSWORD);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_MODULE,
					FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/advisorUser/resetPassword/{userId}", method = RequestMethod.POST)
	public ResponseEntity<?> generatePassResetLink(@PathVariable int userId) throws FinexaBussinessException {

		try {
			AdvisorUserController advisorUserController = new AdvisorUserController();
			String encodedToken = advisorService.encodedUserId(userId);
			String urlToSend = advisorUserController.generatedURL + encodedToken;

			AdvisorUser user = advisorUserRepository.findOne(userId);

			StringBuilder sb = new StringBuilder();
			sb.append("Dear ");
			sb.append(user.getFirstName() + " " + user.getLastName() + ",\n");
			sb.append("\n");
			sb.append("Please click on this link to reset your password: \n");
			sb.append(urlToSend + "\n");
			sb.append("Administrator , Finexa Application \n\n");

			emailService.sendSimpleMessage(user.getEmailID(), "Finexa Password Reset", sb.toString());

			/*
			 * user.setExpirationDate( new
			 * SimpleDateFormat("yyyy-MM-dd").parse(FinexaUtil.getDate(System.
			 * currentTimeMillis())));
			 */
			advisorUserRepository.save(user);

			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);

		} catch (RuntimeException e) {
			throw new FinexaBussinessException("", "", "");
		}

	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/resetPassIndex/advisorUser/{userId}", method = RequestMethod.POST)
	public ResponseEntity<?> resetPasswordIndex(@PathVariable int userId) throws FinexaBussinessException {

		try {
			log.debug("Reset password called ... ");

			AdvisorUser user = advisorService.resetPasswordIndex(userId);
			StringBuilder sb = new StringBuilder();
			sb.append("Dear ");
			sb.append(user.getFirstName() + " " + user.getLastName() + ",\n");
			sb.append("\n");
			sb.append("Your Password for Finexa Application has been reset, new password is : ");
			sb.append(user.getLoginPassword() + "\n");
			sb.append("Administrator , Finexa Application \n\n");

			emailService.sendSimpleMessage(user.getEmailID(), "Finexa Password Reset", sb.toString());
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGERMENT_MANAGE_PASSWORD);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_MODULE,
					FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	
	@RequestMapping(value = "advisorUser/existEmail", method = RequestMethod.GET)
	public ResponseEntity<?> checkEmailExists(@RequestParam("email") String email) throws FinexaBussinessException {
		try {
			System.out.println("email: " + email);
			boolean msg = advisorService.checkEmailExists(email);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.FINEXA_LOGIN);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.FINEXA_LOGIN_FORGOT_PASS_CHECK_EXISTIN_EMAIL_ERROR);
			throw new FinexaBussinessException(FinexaConstant.FINEXA_LOGIN_MODULE,
					FinexaConstant.FINEXA_LOGIN_FORGOT_PASS_CHECK_EXISTIN_EMAIL_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	//@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','UserManagementAddEdit', 'Client')")
	@RequestMapping(value = "clientMaster/existEmail", method = RequestMethod.GET)
	public ResponseEntity<?> checkEmailExistsForClient(@RequestParam("email") String email) throws FinexaBussinessException {
		try {
			System.out.println("email: " + email);
			boolean msg = advisorService.checkEmailExistsForClient(email);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.FINEXA_LOGIN);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.FINEXA_LOGIN_FORGOT_PASS_CHECK_EXISTIN_EMAIL_ERROR);
			throw new FinexaBussinessException(FinexaConstant.FINEXA_LOGIN_MODULE,
					FinexaConstant.FINEXA_LOGIN_FORGOT_PASS_CHECK_EXISTIN_EMAIL_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','UserManagementAddEdit')")
	@RequestMapping(value = "/advisorUser/uniqueEmail", method = RequestMethod.GET)
	public ResponseEntity<?> uniqueEmail(@RequestParam("fieldId") String fieldId,
			@RequestParam("fieldValue") String fieldValue) {
		boolean status = viewUserManagmentService.checkUniqueEmail(fieldValue);
		List<String> msgList = new ArrayList<String>();
		String errorMsg = (status) ? "Email is available " : "Email is already present , Please choose a different one";
		msgList.add(fieldId);
		msgList.add(Boolean.toString(status));
		msgList.add(errorMsg);
		return new ResponseEntity<List<String>>(msgList, HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','UserManagementAddEdit')")
	@RequestMapping(value = "/advisorUser/uniqueAdvisorEmail", method = RequestMethod.GET)
	public ResponseEntity<?> uniqueAdvisorEmail(@RequestParam("emailId") String emailId) throws FinexaBussinessException {
		
		try {
			boolean status = viewUserManagmentService.checkUniqueEmail(emailId);
			return new ResponseEntity<Boolean>(status, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MyBusiness_UserCreation", "MB-UC01", "Failed to check uniqueAdvisorEmail", e);
		}
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','UserManagementAddEdit')")
	@RequestMapping(value = "/advisorUser/existsAdvisorEmail/{emailId}/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> existsAdvisorEmail(@PathVariable String emailId, @PathVariable int advisorId) throws FinexaBussinessException {
		
		try {
			boolean status = viewUserManagmentService.checkEmailExists(emailId, advisorId);
			return new ResponseEntity<Boolean>(status, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MyBusiness_UserCreation", "MB-UC02", "Failed to check existsAdvisorEmail", e);
		}
		
	}
 
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value = "/sendOTP/{phoneNo}", method = RequestMethod.GET)
	public ResponseEntity<?> sendOTP(@PathVariable String phoneNo)
			throws JsonParseException, JsonMappingException, IOException {

		String url = "https://2factor.in/API/V1/" + FinexaConstant.OTP_API_KEY + "/SMS/+91" + phoneNo + "/AUTOGEN";

		String OtpReturn = restClient.get(url).toLowerCase();
		ObjectMapper mapper = new ObjectMapper();

		log.debug("OtpReturn: " + OtpReturn);

		// JSON from String to Object
		OTPObjectDTO obj = mapper.readValue(OtpReturn, OTPObjectDTO.class);

		return new ResponseEntity<OTPObjectDTO>(obj, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value = "/verifyOTP/{session}/{otp}", method = RequestMethod.GET)
	public ResponseEntity<?> verifyOTP(@PathVariable String session, @PathVariable String otp)
			throws JsonParseException, JsonMappingException, IOException {

		String url = "https://2factor.in/API/V1/" + FinexaConstant.OTP_API_KEY + "/SMS/VERIFY/" + session + "/" + otp;

		String OtpVerify = restClient.get(url).toLowerCase();
		ObjectMapper mapper = new ObjectMapper();

		log.debug("OtpVerify: " + OtpVerify);

		// JSON from String to Object
		OTPObjectDTO obj = mapper.readValue(OtpVerify, OTPObjectDTO.class);

		return new ResponseEntity<OTPObjectDTO>(obj, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getLoggingHistory/{loggedId}", method = RequestMethod.GET)
	public ResponseEntity<?> loggingHistory(@PathVariable int loggedId) throws FinexaBussinessException {
		try {
			List<AdvisorUserLoginInfoDTO> advisorUserLoginInfoDTOList = advisorService.findByLoggedInId(loggedId);
			return new ResponseEntity<List<AdvisorUserLoginInfoDTO>>(advisorUserLoginInfoDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGERMENT_LOGGED_IN_HISTORY);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_LOGGING_HISTORY_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_LOGGING_HISTORY_MODULE,
					FinexaConstant.MY_BUSINESS_LOGGING_HISTORY_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
		//pagination=================
		@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
		@RequestMapping(value = "/getLoggingHistoryWithPagination/{loggedId}/{nextFetch}", method = RequestMethod.GET)
		public ResponseEntity<?> getLoggingHistoryWithPagination(@PathVariable int loggedId, @PathVariable int nextFetch) {
			Pageable pageable = new PageRequest(nextFetch, 10);
			
			List<AdvisorUserLoginInfoDTO> advisorUserLoginInfoDTOList = advisorService.findByLoggedInIdWithPagination(loggedId, pageable);
			return new ResponseEntity<List<AdvisorUserLoginInfoDTO>>(advisorUserLoginInfoDTOList, HttpStatus.OK);
		}
		
		@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
		@RequestMapping(value = "/getLoggedInHistoryCount/{loggedId}", method = RequestMethod.GET)
		public ResponseEntity<?> getLoggedInHistoryCount(@PathVariable int loggedId) {
			
			int advisorCount = advisorService.getLoggedInHistoryCount(loggedId);
			return new ResponseEntity<Integer>(advisorCount, HttpStatus.OK);
		}
		//====================================
		
	@PreAuthorize("hasRole('InvestAddEdit')")
	@RequestMapping(value = "/registerBSEDetails", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> registerBSEDetails(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "memberCode") String memberCode, @RequestParam(value = "password") String password,
			@RequestParam(value = "advisorId") int advisorId,int mode) {
		log.info("AdvisorUserController >>> registerBSEDetails ");
		try {
			ClientUCCResultDTO status = advisorService.registerBSEcredentials(userName, memberCode, password, advisorId, mode);
			log.info("AdvisorUserRepository <<< Exiting registerBSEDetails() ");
			return new ResponseEntity<ClientUCCResultDTO>(status, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed to get Family Income List , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		}

	}

	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoView')")
	@RequestMapping(value = "/getClientByOffset/{loggedId}", method = RequestMethod.GET)
	public ResponseEntity<?> getClientByOffset(@PathVariable int loggedId, HttpServletRequest request)
			throws FinexaBussinessException {
		DashBoardDTO dashBoardDTO = null;
		try {
			String header = request.getHeader(Constants.HEADER_STRING);
			String token = cacheInfoService.getToken(header);
			UserClientRedis userDetailsDTO = cacheInfoService.getClientCacheMap(token, loggedId);
			System.out.println("userDetailsDTO " + userDetailsDTO);
			dashBoardDTO = advisorService.getClientByOffset(loggedId, userDetailsDTO, token, loggedId);

		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGERMENT_LOGGED_IN_HISTORY);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_LOGGING_HISTORY_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_LOGGING_HISTORY_MODULE,
					FinexaConstant.MY_BUSINESS_LOGGING_HISTORY_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<DashBoardDTO>(dashBoardDTO, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoDelete','AdvisorAdmin')")
	@RequestMapping(value = "/deletetAUMCacheMap", method = RequestMethod.GET)
	public ResponseEntity<?> deletetAUMCacheMap( @RequestParam("advisorUserId") int advisorUserId, HttpServletRequest request) {
		String header = request.getHeader(Constants.HEADER_STRING);
		String token = cacheInfoService.getToken(header);
		cacheInfoService.deleteClientCacheMap(advisorUserId,token);		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getDistributorName", method = RequestMethod.GET)
	public ResponseEntity<?> getDistributorName() throws FinexaBussinessException {
		try {
			List<AdvisorDTO> advDTOList = advisorService.getAllDistributors();
			return new ResponseEntity<List<AdvisorDTO>>(advDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Advisor User",
					"Not Present At the moment", "Failed to show all.");
		}
	}
	
	@RequestMapping(value = "/getLogCount/{userID}", method = RequestMethod.GET)
	public ResponseEntity<?> getLogCount(@PathVariable int userID) throws FinexaBussinessException {
		try {
			int count = advisorService.getLogCount(userID);
			return new ResponseEntity<Integer>(count, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Advisor User",
					"Not Present At the moment", "Failed to show all.");
		}
	}
	
	@RequestMapping(value = "/bulkUploadLog/{userID}/{nextFetch}", method = RequestMethod.GET)
	public ResponseEntity<?> bulkUploadLog(@PathVariable int userID, @PathVariable int nextFetch) throws FinexaBussinessException {
		try {
			Pageable pageable = new PageRequest(nextFetch, 10);
			List<AdvisorUserBulkUploadHistoryDTO> advBUDTOList = advisorService.bulkUploadLog(userID, pageable);
			return new ResponseEntity<List<AdvisorUserBulkUploadHistoryDTO>>(advBUDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Advisor User",
					"Not Present At the moment", "Failed to show all.");
		}
	}
	
	@RequestMapping(value = "/getLogDetails/{logDetailsId}", method = RequestMethod.GET)
	public ResponseEntity<?> getLogDetails(@PathVariable int logDetailsId) throws FinexaBussinessException {
		try {
			AdvisorUserBulkUploadHistoryDTO advBUDTO = advisorService.getLogDetails(logDetailsId);
			return new ResponseEntity<AdvisorUserBulkUploadHistoryDTO>(advBUDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Advisor User",
					"Not Present At the moment", "Failed to show all.");
		}
	}
	
	@RequestMapping(value = "/getRunningProcess/{userID}", method = RequestMethod.GET)
	public ResponseEntity<?> getRunningProcess(@PathVariable int userID) throws FinexaBussinessException {
		try {
			AdvisorUserBulkUploadHistoryDTO advBUDTO = advisorService.getRunningProcess(userID);
			return new ResponseEntity<AdvisorUserBulkUploadHistoryDTO>(advBUDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Advisor User",
					"Not Present At the moment", "Failed to show all.");
		}
	}

}