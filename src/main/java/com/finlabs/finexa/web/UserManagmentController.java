/*package com.finlabs.finexa.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ClientLifeExpDTO;
import com.finlabs.finexa.dto.UserDTO;
import com.finlabs.finexa.dto.ViewUserManagmentDTO;
import com.finlabs.finexa.service.ClientLifeExpectancyService;
import com.finlabs.finexa.service.ViewUserManagmentService;

@RestController
public class UserManagmentController {
private static Logger log = LoggerFactory.getLogger(UserManagmentController.class);
	@Autowired
	ViewUserManagmentService viewUserManagmentService;

	@RequestMapping(value = "/getUserList/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> viewList(@PathVariable int id) {
		List<ViewUserManagmentDTO> uList = viewUserManagmentService.getAllUserList(id);

		if (uList != null) {
			return new ResponseEntity<List<ViewUserManagmentDTO>>(uList, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Users Not Found", HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getUserById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserById(@PathVariable int id) {
		UserDTO userDTO = viewUserManagmentService.findById(id);

		if (userDTO != null) {
			return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User Not Found", HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteUser(@PathVariable int id) {
		int deleteFlag = viewUserManagmentService.delete(id);

		if (deleteFlag >=0 ) {
			return new ResponseEntity<Integer>(deleteFlag, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User Not Found", HttpStatus.OK);
		}
	}


}
 */
package com.finlabs.finexa.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import com.finlabs.finexa.dto.ClientExportCsvDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.AccessRightDTO;
import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.dto.FormDataDTO;
import com.finlabs.finexa.dto.ManagePasswordDTO;
import com.finlabs.finexa.dto.RoleDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.dto.UserDTO;
import com.finlabs.finexa.dto.UserDeleteReturnDTO;
import com.finlabs.finexa.dto.UserHierarchyMappingDTO;
import com.finlabs.finexa.dto.UserRoleCreationDTO;
import com.finlabs.finexa.dto.UserRoleReMappingDTO;
import com.finlabs.finexa.dto.ViewUserManagmentDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.LookupEducationalQualification;
import com.finlabs.finexa.model.LookupEmploymentType;
import com.finlabs.finexa.model.LookupMaritalStatus;
import com.finlabs.finexa.model.LookupResidentType;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.ClientContactRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.EducationalQualificationRepository;
import com.finlabs.finexa.repository.EmploymentTypeRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.repository.LookupCountryRepository;
import com.finlabs.finexa.repository.MaritalStatusRepository;
import com.finlabs.finexa.repository.ResidentTypeRepository;
import com.finlabs.finexa.service.AdvisorService;
import com.finlabs.finexa.service.ClientRiskProfileService;
import com.finlabs.finexa.service.ViewUserManagmentService;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

@RestController
public class UserManagmentController {
	private static Logger log = LoggerFactory.getLogger(UserManagmentController.class);

	@Autowired
	ViewUserManagmentService viewUserManagmentService;
	@Autowired
	ClientMasterRepository clientMasterRepository;
	@Autowired
	MaritalStatusRepository maritalStatusRepository;
	@Autowired
	EducationalQualificationRepository educationalQualificationRepository;
	@Autowired
	EmploymentTypeRepository employmentTypeRepository;
	@Autowired
	ResidentTypeRepository residentTypeRepository;
	@Autowired
	LookupCountryRepository countryRepository;
	@Autowired
	ClientContactRepository clientContactRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	@Autowired
	AdvisorMasterRepository advisorMasterRepository;
	@Autowired
    ClientRiskProfileService clientRiskProfileService;
    @Autowired
    AdvisorService advisorService;

	/***********************
	 * For User Creation
	 ***********************************/
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/checkUniqueEmpCodeForFixedmaster/{orgName}/{distCode}/{empCode}", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniqueEmpCodeForFixedmaster
				(@PathVariable String orgName, @PathVariable String distCode, @PathVariable String empCode) 
						throws FinexaBussinessException {
		try {
			List<AdvisorDTO> advisorUser = viewUserManagmentService.checkUniqueEmpCodeForFixedmaster(orgName, distCode, empCode);
//			AdvisorMaster mas = advisorMasterRepository.findByOrgNameAndDistributorCode(orgName, distCode);
//			System.out.println(mas);
//			if(mas != null) {
//				advisorUser = advisorUserRepository.findByAdvisorMasterAndEmployeeCode(mas, empCode);
//				System.out.println("advisor list length :" + advisorUser.size());
//				for(AdvisorUser a : advisorUser) {
//					System.out.println("advisor Id :" + a.getId());
//					System.out.println("advisor Name :" + a.getFirstName() + " " + a.getLastName());
//					System.out.println("advisor Emp Code :" + a.getEmployeeCode());
//				}
//			}
			return new ResponseEntity<List<AdvisorDTO>>(advisorUser, HttpStatus.OK);
		} /*catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.VIEW_USER_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.VIEW_USER_LIST_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}*/
		
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getUserList/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserList(@PathVariable int id) throws FinexaBussinessException {
		try {
			List<ViewUserManagmentDTO> uList = viewUserManagmentService.getAllUserList(id);
			return new ResponseEntity<List<ViewUserManagmentDTO>>(uList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.VIEW_USER_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.VIEW_USER_LIST_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin', 'UserManagement')")
	@RequestMapping(value = "/getUnsupervisedUserList/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUnsupervisedUserList(@PathVariable int id) throws FinexaBussinessException {
		try {
			List<ViewUserManagmentDTO> uList = viewUserManagmentService.getUnsupervisedUserList(id);
			return new ResponseEntity<List<ViewUserManagmentDTO>>(uList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.VIEW_USER_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.VIEW_USER_LIST_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin', 'UserManagement')")
	@RequestMapping(value = "/getAllUsersForClientContact/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllUsersForClientContact(@PathVariable int id) throws FinexaBussinessException {
		try {
			List<ViewUserManagmentDTO> uList = viewUserManagmentService.getAllUsersForClientContact(id);
			return new ResponseEntity<List<ViewUserManagmentDTO>>(uList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.VIEW_USER_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.VIEW_USER_LIST_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getUserAndRoleList/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserAndRoleList(@PathVariable int id) throws FinexaBussinessException {
		try {
			List<UserDTO> uList = viewUserManagmentService.findUserAndRoleByUserId(id);
			return new ResponseEntity<List<UserDTO>>(uList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.VIEW_USER_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.VIEW_USER_LIST_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/supervisorMappingDelete/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int advisorId) throws FinexaBussinessException {
		try {
			int i = viewUserManagmentService.delete(advisorId);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CASH_DELETE_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE,
					FinexaConstant.CLIENT_CASH_DELETE_DATA_ERROR, exception != null ? exception.getErrorMessage() : "", rexp);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllUserForSelectedSupervisorRole", method = RequestMethod.GET)
	public ResponseEntity<?> getAllUserForSelectedSupervisorRole(@RequestParam("masterID") int masterID,@RequestParam("roleDescription") String roleDescription)
			throws FinexaBussinessException {

		try {
			List<UserDTO> userList = viewUserManagmentService.getAllUserForSelectedSupervisorRole(masterID,roleDescription);
			return new ResponseEntity<List<UserDTO>>(userList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_HIERARCHY_MAPPING);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_ADVISOR_USER_WITH_SUPERVISOR_ROLE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_MODULE,
					FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_ADVISOR_USER_WITH_SUPERVISOR_ROLE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@Transactional(rollbackOn = CustomFinexaException.class)
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestParam("serviceIP") String serviceIP, @RequestParam("loggedUser") int loggedUserId, 
			@Valid @RequestBody UserDTO userDTO, Errors errors)
			throws FinexaBussinessException, JsonParseException, JsonMappingException, IOException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				userDTO = viewUserManagmentService.save(loggedUserId, userDTO, serviceIP);
				return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.MY_BUSINESS_USER_CREATION_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
						FinexaConstant.MY_BUSINESS_USER_CREATION_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/saveScore/{masterId}/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> saveScore(@PathVariable int masterId,@PathVariable int clientId)
			throws FinexaBussinessException, JsonParseException, JsonMappingException, IOException {

		
			try {
				UserDTO userDTO = new UserDTO();
				clientRiskProfileService.autoSaveScore(masterId,clientId);
				return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.MY_BUSINESS_USER_CREATION_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
						FinexaConstant.MY_BUSINESS_USER_CREATION_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		
	}
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDTO, Errors errors)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				userDTO = viewUserManagmentService.update(userDTO);
				return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.MY_BUSINESS_USER_CREATION_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
						FinexaConstant.MY_BUSINESS_USER_CREATION_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/updateUser/{userId}", method = RequestMethod.POST)
	public ResponseEntity<?> updateUser(@PathVariable int userId, @Valid @RequestBody UserDTO userDTO, Errors errors)
			throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				userDTO = viewUserManagmentService.update(userId, userDTO);
				return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.MY_BUSINESS_USER_CREATION_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
						FinexaConstant.MY_BUSINESS_USER_CREATION_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/userById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> userById(@PathVariable int id) throws FinexaBussinessException {
		try {
			UserDTO userDTO = viewUserManagmentService.getExistingUser(id);
			return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_USER_CREATION_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.MY_BUSINESS_USER_CREATION_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteUser(@PathVariable int id) throws FinexaBussinessException {
		try {
			UserDeleteReturnDTO userDeleteReturnDTO = viewUserManagmentService.deleteUser(id);
			return new ResponseEntity<UserDeleteReturnDTO>(userDeleteReturnDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.DELETE_USER_RECORD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.DELETE_USER_RECORD_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	/***********************
	 * For User Role
	 *************************************/
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/createRole", method = RequestMethod.POST)
	public ResponseEntity<?> createRole(@RequestBody UserRoleCreationDTO userRoleCreationDTO)
			throws FinexaBussinessException {

		try {
			userRoleCreationDTO = viewUserManagmentService.saveRole(userRoleCreationDTO);
			return new ResponseEntity<UserRoleCreationDTO>(userRoleCreationDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_ADD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_MODULE,
					FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_ADD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/updateRole", method = RequestMethod.POST)
	public ResponseEntity<?> updateRole(@RequestBody UserRoleCreationDTO userRoleCreationDTO)
			throws FinexaBussinessException {

		try {
			userRoleCreationDTO = viewUserManagmentService.updateRole(userRoleCreationDTO);
			return new ResponseEntity<UserRoleCreationDTO>(userRoleCreationDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_UPDATE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_MODULE,
					FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_UPDATE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllExistingRoleSupervisorMapping/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllExistingRoleSupervisorMapping(@PathVariable int advisorId)
			throws FinexaBussinessException {
		try {
			List<UserRoleCreationDTO> userRoleDTOList = viewUserManagmentService
					.getAllUserRoleSuperVisorRole(advisorId);
			return new ResponseEntity<List<UserRoleCreationDTO>>(userRoleDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_MODULE,
					FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/deleteRole/{roleId}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteRole(@PathVariable int roleId) throws FinexaBussinessException {
		try {
			UserDeleteReturnDTO userDeleteReturnDTO = viewUserManagmentService.deleteRole(roleId);
			return new ResponseEntity<UserDeleteReturnDTO>(userDeleteReturnDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_MODULE,
					FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_DELETE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	/**************************************
	 * For Role DropDown
	 * 
	 * 
	 *********************************/
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllExistingRoles/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllExistingRoles(@PathVariable int advisorId) throws FinexaBussinessException {
		try {
			List<RoleDTO> roleDTOList = viewUserManagmentService.getAllExistingRoles(advisorId);
			return new ResponseEntity<List<RoleDTO>>(roleDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_ALL_EXISTING_ROLES_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.GET_ALL_EXISTING_ROLES_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllExistingRolesForUserCreation/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllExistingRolesForUserCreation(@PathVariable int advisorId) throws FinexaBussinessException {
		try {
			List<RoleDTO> roleDTOList = viewUserManagmentService.getAllExistingRolesForUserCreation(advisorId);
			return new ResponseEntity<List<RoleDTO>>(roleDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_ALL_EXISTING_ROLES_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.GET_ALL_EXISTING_ROLES_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getExistingRoleForUserEdit/{roleId}", method = RequestMethod.GET)
	public ResponseEntity<?> getExistingRoleForUserEdit(@PathVariable int roleId) throws FinexaBussinessException {
		try {
			RoleDTO roleDTO = viewUserManagmentService.getExistingRoleForUserEdit(roleId);
			return new ResponseEntity<RoleDTO>(roleDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.GET_ALL_EXISTING_ROLES_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.GET_ALL_EXISTING_ROLES_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	/**************************
	 * User Role Remapping
	 *
	 **********************************/
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/createUserRoleRemapping", method = RequestMethod.POST)
	public ResponseEntity<?> createUserRoleRemapping(@Valid @RequestBody UserRoleReMappingDTO userRoleReMappingDTO,
			Errors errors) throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				userRoleReMappingDTO = viewUserManagmentService.saveRoleRemapping(userRoleReMappingDTO);
				return new ResponseEntity<UserRoleReMappingDTO>(userRoleReMappingDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_REMAPPING);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.MY_BUSINESS_USER_ROLE_REMAPPING_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_ROLE_REMAPPING_MODULE,
						FinexaConstant.MY_BUSINESS_USER_ROLE_REMAPPING_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/updateUserRoleRemapping", method = RequestMethod.POST)
	public ResponseEntity<?> updateUserRoleRemapping(@Valid @RequestBody UserRoleReMappingDTO userRoleReMappingDTO,
			Errors errors) throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				userRoleReMappingDTO = viewUserManagmentService.updateRoleRemapping(userRoleReMappingDTO);
				return new ResponseEntity<UserRoleReMappingDTO>(userRoleReMappingDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_REMAPPING);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.MY_BUSINESS_USER_ROLE_REMAPPING_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_ROLE_REMAPPING_MODULE,
						FinexaConstant.MY_BUSINESS_USER_ROLE_REMAPPING_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}

		}
	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getUserRoleRemapping/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserRoleReMappingList(@PathVariable int advisorId) throws FinexaBussinessException {
		try {
			List<UserRoleReMappingDTO> uRoleReMappingList = viewUserManagmentService.getAllUserRoleRemapping(advisorId);
			return new ResponseEntity<List<UserRoleReMappingDTO>>(uRoleReMappingList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_REMAPPING);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUISNESS_USER_ROLE_REMAPPING_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_ROLE_REMAPPING_MODULE,
					FinexaConstant.MY_BUISNESS_USER_ROLE_REMAPPING_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	/***********************
	 * For User Role Supervisor Mapping
	 *************************************/
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getExistingUserSupervisorRoleMapping/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> getExstingUserRoleList(@PathVariable int advisorId) {
		List<UserRoleCreationDTO> userRoleCreationDTOList = viewUserManagmentService
				.getAllUserRoleCreationList(advisorId);

		if (userRoleCreationDTOList != null) {
			return new ResponseEntity<List<UserRoleCreationDTO>>(userRoleCreationDTOList, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User Roles Not Found", HttpStatus.OK);
		}
	}

	/*******************************
	 * For Hierarchy Mapping
	 * 
	 * 
	 ********************************/
	
	 @PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	 @RequestMapping(value = "/getUserHierarchyMapping/{advisorId}", method = RequestMethod.GET) 
	  public ResponseEntity<?> getUserHierarchyMapping(@PathVariable int advisorId) throws FinexaBussinessException {
		  try { List<UserHierarchyMappingDTO>
	  userHierarchyMappingDTOList = viewUserManagmentService.getAllHierarchiesByAdvisorId(advisorId); 
		  return new ResponseEntity<List<UserHierarchyMappingDTO>>(userHierarchyMappingDTOList,HttpStatus.OK); 
		     } catch (RuntimeException e){ 
		    	 FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
	                    .findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_HIERARCHY_MAPPING);
	             FinexaExceptionHandling exception = finexaExceptionHandlingRepository
	                    .findByFinexaBusinessSubmoduleAndErrorCode(subModule,
	            FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_VIEW_ERROR); 
	             throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_MODULE,FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_VIEW_ERROR,
	            		 exception != null ? exception.getErrorMessage() : "", e); 
	             }
	  }
	 
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getSupervisorsWithSameRole/{advisorId}/{roleId}", method = RequestMethod.GET)
	public ResponseEntity<?> getSupervisorsWithSameRole(@PathVariable int advisorId,
			@PathVariable int roleId) throws FinexaBussinessException {
		try {
			List<UserHierarchyMappingDTO> userHierarchyMappingDTOList = viewUserManagmentService
					.getOtherSupervisorsWithSameRole(advisorId, roleId);
			return new ResponseEntity<List<UserHierarchyMappingDTO>>(userHierarchyMappingDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_HIERARCHY_MAPPING);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_SUPERVISOR_WITH_SAME_ROLE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_MODULE,
					FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_SUPERVISOR_WITH_SAME_ROLE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	/*
	 * @PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	 * 
	 * @RequestMapping(value = "/getUsersByOrgAndRole", method = RequestMethod.GET)
	 * public ResponseEntity<?> getUsersByOrgAndRole(@RequestParam("orgName") int
	 * orgName,
	 * 
	 * @RequestParam("roleId") int roleId) throws FinexaBussinessException { try {
	 * List<UserDTO> userMappingDTOList = viewUserManagmentService
	 * .getUsersByOrgAndRole(orgName, roleId); return new
	 * ResponseEntity<List<UserDTO>>(userMappingDTOList, HttpStatus.OK); } catch
	 * (RuntimeException e) { FinexaBusinessSubmodule subModule =
	 * finexaBusinessSubmoduleRepository
	 * .findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_HIERARCHY_MAPPING);
	 * FinexaExceptionHandling exception = finexaExceptionHandlingRepository
	 * .findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.
	 * MY_BUSINESS_HIERARCHY_MAPPING_GET_SUPERVISOR_WITH_SAME_ROLE_ERROR); throw new
	 * FinexaBussinessException(FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_MODULE,
	 * FinexaConstant.
	 * MY_BUSINESS_HIERARCHY_MAPPING_GET_SUPERVISOR_WITH_SAME_ROLE_ERROR, exception
	 * != null ? exception.getErrorMessage() : "", e); }
	 * 
	 * }
	 */
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAdvisorUsersWithSupervisorRole", method = RequestMethod.GET)
	public ResponseEntity<?> getAdvisorUsersWithSupervisorRole(@RequestParam("supRoleId") int supRoleId)
			throws FinexaBussinessException {

		try {
			log.debug("supRoleId: " + supRoleId);
			List<AdvisorDTO> listUser = viewUserManagmentService.getAllUserForSupervisorRole(supRoleId);
			return new ResponseEntity<List<AdvisorDTO>>(listUser, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_HIERARCHY_MAPPING);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_ADVISOR_USER_WITH_SUPERVISOR_ROLE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_MODULE,
					FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_ADVISOR_USER_WITH_SUPERVISOR_ROLE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	
	//new User Management Implementation
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllUserForSelectedRole", method = RequestMethod.GET)
	public ResponseEntity<?> getAllUserForSelectedRole(@RequestParam("masterID") int masterID,@RequestParam("roleId") int roleId)
			throws FinexaBussinessException {

		try {
			log.debug("roleId: " + roleId);
			List<UserDTO> userList = viewUserManagmentService.getAllUserForSelectedRole(masterID,roleId);
			return new ResponseEntity<List<UserDTO>>(userList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_HIERARCHY_MAPPING);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_ADVISOR_USER_WITH_SUPERVISOR_ROLE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_MODULE,
					FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_ADVISOR_USER_WITH_SUPERVISOR_ROLE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllRolesForSelectedOrg", method = RequestMethod.GET)
	public ResponseEntity<?> getAllRolesForSelectedOrg(@RequestParam("masterID") int masterID)
			throws FinexaBussinessException {

		try {
			//log.debug("roleId: " + masterID);
			UserDTO userDTO = viewUserManagmentService.getAllRolesForSelectedOrg(masterID);
			return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_HIERARCHY_MAPPING);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_ADVISOR_USER_WITH_SUPERVISOR_ROLE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_MODULE,
					FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_ADVISOR_USER_WITH_SUPERVISOR_ROLE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/saveUserHierarchy", method = RequestMethod.POST)
	public ResponseEntity<?> saveUserHierarchy(@Valid @RequestBody UserHierarchyMappingDTO userHierarchyMappingDTO,
			Errors errors) throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				userHierarchyMappingDTO = viewUserManagmentService.saveUserHierarchy(userHierarchyMappingDTO);
				return new ResponseEntity<UserHierarchyMappingDTO>(userHierarchyMappingDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_HIERARCHY_MAPPING);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_ADD_ERROR);
				throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_MODULE,
						FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_ADD_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/updateUserHierarchy", method = RequestMethod.POST)
	public ResponseEntity<?> updateUserHierarchy(@Valid @RequestBody UserHierarchyMappingDTO userHierarchyMappingDTO,
			Errors errors) throws FinexaBussinessException {

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		} else {
			try {
				userHierarchyMappingDTO = viewUserManagmentService.updateUserHierarchy(userHierarchyMappingDTO);
				return new ResponseEntity<UserHierarchyMappingDTO>(userHierarchyMappingDTO, HttpStatus.OK);
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_HIERARCHY_MAPPING);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_UPDATE_ERROR);
				throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_MODULE,
						FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_UPDATE_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getUserHierarchy/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> userUserHierarchy(@PathVariable int userId) throws FinexaBussinessException {
		try {
			UserHierarchyMappingDTO userHierarchyMappingDTO = viewUserManagmentService.getUserHierarchy(userId);
			return new ResponseEntity<UserHierarchyMappingDTO>(userHierarchyMappingDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_HIERARCHY_MAPPING);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_MODULE,
					FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getUserSpecific/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> newUserHierarchy(@PathVariable int userId) throws FinexaBussinessException {
		try {
			UserHierarchyMappingDTO userHierarchyMappingDTO = viewUserManagmentService.getUserSpecific(userId);
			return new ResponseEntity<UserHierarchyMappingDTO>(userHierarchyMappingDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_HIERARCHY_MAPPING);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_DATA_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_MODULE,
					FinexaConstant.MY_BUSINESS_HIERARCHY_MAPPING_GET_DATA_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	
	/********************************
	 * For Access Rights
	 * 
	 * 
	 ***********************************/
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllModules/{orgFlag}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllModules(@PathVariable String orgFlag) throws FinexaBussinessException {
		try {
			List<AccessRightDTO> accessRightDTO = viewUserManagmentService.getAllModules(orgFlag);
			return new ResponseEntity<List<AccessRightDTO>>(accessRightDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_ACCESS_RIGHTS);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.My_BUSINESS_ACCESS_RIGHTS_GET_ALL_MODULES_LIST_ERROR);
			throw new FinexaBussinessException(FinexaConstant.FINEXA_BUSINESS_MODULE,
					FinexaConstant.My_BUSINESS_ACCESS_RIGHTS_GET_ALL_MODULES_LIST_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	 

	/*
	 * @RequestMapping(value = "/getAllSubModules", method = RequestMethod.GET)
	 * public ResponseEntity<?> getAllSubModules(@RequestParam("moduleId") int
	 * moduleId,
	 * 
	 * @RequestParam("roleId") int roleId) throws FinexaBussinessException { try {
	 * List<FinexaBusinessSubmoduleDTO> finexaBusinessSubmoduleDTOList =
	 * viewUserManagmentService .getAllBusinessSubModules(moduleId, roleId);
	 * 
	 * return new ResponseEntity<List<FinexaBusinessSubmoduleDTO>>(
	 * finexaBusinessSubmoduleDTOList, HttpStatus.OK); } catch (RuntimeException e)
	 * { FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
	 * .findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_ACCESS_RIGHTS);
	 * FinexaExceptionHandling exception = finexaExceptionHandlingRepository
	 * .findByFinexaBusinessSubmoduleAndErrorCode(subModule,
	 * FinexaConstant.MY_BUSINESS_ACCESS_RIGHTS_GET_ALL_SUBMODULES_ERROR); throw new
	 * FinexaBussinessException(FinexaConstant.FINEXA_BUSINESS_SUBMODULE,
	 * FinexaConstant.MY_BUSINESS_ACCESS_RIGHTS_GET_ALL_SUBMODULES_ERROR, exception
	 * != null ? exception.getErrorMessage() : "", e); }
	 * 
	 * }
	 */

	/*
	 * @RequestMapping(value = "/saveRoleSubmoduleMapping", method =
	 * RequestMethod.POST) public ResponseEntity<?> saveRoleSubmoduleMapping(
	 * 
	 * @Valid @RequestBody AdvisorRoleSubmoduleMappingDTO
	 * advisorRoleSubmoduleMappingDTO, Errors errors) throws
	 * FinexaBussinessException, CustomFinexaException {
	 * 
	 * ErrorDTO result = new ErrorDTO(); if (errors.hasErrors()) {
	 * result.setErrorCode("VALIDATION_ERROR"); result.setErrorMessage(
	 * errors.getAllErrors().stream().map(x ->
	 * x.getDefaultMessage()).collect(Collectors.joining(","))); return
	 * ResponseEntity.badRequest().body(result); } else { try {
	 * viewUserManagmentService.saveRoleSubModuleMapping(
	 * advisorRoleSubmoduleMappingDTO); return new ResponseEntity<Integer>(1,
	 * HttpStatus.OK); } catch (RuntimeException e) { FinexaBusinessSubmodule
	 * subModule = finexaBusinessSubmoduleRepository
	 * .findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_ACCESS_RIGHTS);
	 * FinexaExceptionHandling exception = finexaExceptionHandlingRepository
	 * .findByFinexaBusinessSubmoduleAndErrorCode(subModule,
	 * FinexaConstant.MY_BUSINESS_ACCESS_RIGHTS_ADD_ERROR); throw new
	 * FinexaBussinessException(FinexaConstant.MY_BUSINESS_ACCESS_RIGHTS_MODULE,
	 * FinexaConstant.MY_BUSINESS_ACCESS_RIGHTS_ADD_ERROR, exception != null ?
	 * exception.getErrorMessage() : "", e); } } }
	 */
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/userCreation/existEmail/{email}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> checkEmailExists(@PathVariable String email, @PathVariable int userId)
			throws FinexaBussinessException {
		try {
			log.debug("email in controller: " + email);
			log.debug("userId in controller: " + userId);
			boolean msg = viewUserManagmentService.checkEmailExists(email, userId);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.ADD_EDIT_USER_CHECK_EMAIL_EXISTS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.ADD_EDIT_USER_CHECK_EMAIL_EXISTS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/userCreation/{userId}/existMobile/{mobile}", method = RequestMethod.GET)
	public ResponseEntity<?> checkMobileExists(@PathVariable BigInteger mobile, @PathVariable int userId)
			throws FinexaBussinessException {
		try {
			boolean msg = viewUserManagmentService.checkMobileExists(mobile, userId);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.ADD_EDIT_USER_CHECK_MOBILE_EXISTS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.ADD_EDIT_USER_CHECK_MOBILE_EXISTS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/userCreation/uniqueEmail", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniqueEmail(@RequestParam("email") String email) throws FinexaBussinessException {
		try {
			boolean msg = viewUserManagmentService.checkUniqueEmail(email);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.ADD_USER_CHECK_UNIQUE_EMAIL_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.ADD_USER_CHECK_UNIQUE_EMAIL_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/userCreation/uniqueMobile", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniqueMobile(@RequestParam("mobile") BigInteger mobile)
			throws FinexaBussinessException {
		try {
			boolean msg = viewUserManagmentService.checkUniqueMobile(mobile);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.ADD_USER_CHECK_UNIQUE_MOBILENO_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.ADD_USER_CHECK_UNIQUE_MOBILENO_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/userCreation/{userId}/existEmployee/{employeeCd}", method = RequestMethod.GET)
	public ResponseEntity<?> checkEmployeeCodeExists(@PathVariable String employeeCd, @PathVariable int userId)
			throws FinexaBussinessException {
		// log.debug("inside checkEmployeeCodeExists");
		try {
			boolean msg = viewUserManagmentService.checkEmployeeCodeExists(employeeCd, userId);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.ADD_EDIT_USER_CHECK_EMPLOYEE_CODE_EXISTS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.ADD_EDIT_USER_CHECK_EMPLOYEE_CODE_EXISTS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/userCreation/uniqueEmployee", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniqueEmployeeCode(@RequestParam("employeeCd") String employeeCd)
			throws FinexaBussinessException {
		try {
			boolean msg = viewUserManagmentService.checkUniqueEmployeeCode(employeeCd);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.ADD_USER_CHECK_UNIQUE_EMPLOYEE_CODE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
					FinexaConstant.ADD_USER_CHECK_UNIQUE_EMPLOYEE_CODE_ERROR,
					exception != null ? exception.getErrorMessage() : "");
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/userCreation/{advisorId}/{role}", method = RequestMethod.GET)
	public ResponseEntity<?> checkRoleExists(String role, int advisorId) {
		String msg = viewUserManagmentService.checkRoleExists(role, advisorId);
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/userCreation", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniqueRole(@RequestParam("masterID") int masterID,@RequestParam("roleDescription") String role) throws FinexaBussinessException {
		try {
			boolean msg = viewUserManagmentService.checkUniqueRole(masterID,role);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_USER_ROLE_CHECK_UNIQUE_ROLE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_MODULE,
					FinexaConstant.MY_BUSINESS_USER_ROLE_CHECK_UNIQUE_ROLE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/supervisorChecking", method = RequestMethod.GET)
	public ResponseEntity<?> checkSupervisor(@RequestParam("userID") int userId) throws FinexaBussinessException {
		try {
			boolean msg = viewUserManagmentService.checkSupervisor(userId);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_USER_ROLE_CHECK_UNIQUE_ROLE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_MODULE,
					FinexaConstant.MY_BUSINESS_USER_ROLE_CHECK_UNIQUE_ROLE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/userChecking", method = RequestMethod.GET)
	public ResponseEntity<?> checkRoleUser(@RequestParam("userId") int userId) throws FinexaBussinessException {
		try {
			boolean msg = viewUserManagmentService.checkRoleUser(userId);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_USER_ROLE_CHECK_UNIQUE_ROLE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_MODULE,
					FinexaConstant.MY_BUSINESS_USER_ROLE_CHECK_UNIQUE_ROLE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	/***********************
	 * For Unique check in hierarchy mapping
	 * 
	 * 
	 **********************************/
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/checkSupervisorMapping", method = RequestMethod.GET)
	public ResponseEntity<?> checkSupervisorMapping(@RequestParam("userID") int userId) throws FinexaBussinessException {
		try {
			boolean msg = viewUserManagmentService.checkSupervisorMapping(userId);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_CREATION);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_USER_ROLE_CHECK_UNIQUE_ROLE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_ROLE_CREATION_MODULE,
					FinexaConstant.MY_BUSINESS_USER_ROLE_CHECK_UNIQUE_ROLE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	/***********************
	 * For Manage Password List
	 * 
	 * 
	 **********************************/
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllUsersToManagePassword/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUsersToManagePassword(@PathVariable int id) throws FinexaBussinessException {
		try {
			List<ManagePasswordDTO> uList = viewUserManagmentService.getAllUsersToManagePassword(id);
			return new ResponseEntity<List<ManagePasswordDTO>>(uList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGERMENT_MANAGE_PASSWORD);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_VIEW_USERS_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_MODULE,
					FinexaConstant.MY_BUSINESS_MANAGE_PASSWORD_VIEW_USERS_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	/************************************
	 * Client Record
	 *
	 * 
	 * 
	 *******************************************/
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientRecordsView')")
	@RequestMapping(value = "/clientRecord/{fileType}/downloadClientTemplate", method = RequestMethod.GET)
	public ResponseEntity<?> downloadClientTemplate(@PathVariable String fileType, HttpServletResponse response)
			throws IOException, FinexaBussinessException {
		try {
			if (fileType.equalsIgnoreCase(FinexaConstant.FILE_TYPE_CSV)) {
				viewUserManagmentService.downloadClientTemplateCSVForImport(response);
			} else if (fileType.equalsIgnoreCase(FinexaConstant.FILE_TYPE_EXCEL)) {
				viewUserManagmentService.downloadClientTemplateExcel(response);
			}

			return null;
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_CLIENT_RECORDS_IMPORT_EXPORT_CLIENT);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_IMPORT_CLIENT_RECORD_TEMPLATE_DOWNLOAD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_IMPORT_EXPORT_CLIENT_RECORDS_MODULE,
					FinexaConstant.MY_BUSINESS_IMPORT_CLIENT_RECORD_TEMPLATE_DOWNLOAD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientRecordsView')")
	@RequestMapping(value = "/downloadClientRecordTemplateCSV")
	public void downloadClientRecordTemplateCSV(HttpServletResponse response) throws IOException {

		String csvFileName = "clientRecordTemplateFormat.csv";

		response.setContentType("text/csv");

		// creates mock data
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
		response.setHeader(headerKey, headerValue);

		// uses the Super CSV API to generate CSV data from the model data
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] header = { "column1", "column2", "column3", "...", "..." };

		csvWriter.writeHeader(header);
		csvWriter.close();
	}
	
	 @PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientRecordsView')")
	@RequestMapping(value = "/clientRecord/uploadClientsByUsers", method = RequestMethod.POST)
	public ResponseEntity<?> uploadClientsByUsers(@ModelAttribute FileuploadDTO fileuploadDTO, HttpServletRequest request)
			throws FinexaBussinessException, CustomFinexaException {
		 UploadResponseDTO uploadResponseDTO = null;
		try {
			log.debug("fileuploadDTO " + fileuploadDTO);

			log.debug("FileName : " + fileuploadDTO.getFile()[0].getOriginalFilename());
			 uploadResponseDTO = new UploadResponseDTO();
			// validate the uploading file and return error else call service
			if (fileuploadDTO.getFile()[0].getOriginalFilename().contains(".xls")) {
				if (validUploadFileExcel(fileuploadDTO, uploadResponseDTO)) {
					uploadResponseDTO = viewUserManagmentService.uploadClientByUser(fileuploadDTO, uploadResponseDTO);
				}
			}
			if (fileuploadDTO.getFile()[0].getOriginalFilename().contains(".csv")) {
				if (validUploadFileCsv(fileuploadDTO, uploadResponseDTO)) {
					uploadResponseDTO = viewUserManagmentService.uploadClientByUser(fileuploadDTO, uploadResponseDTO);
				}
			}
			advisorService.deletetAUMCacheMap(fileuploadDTO.getLoggedUserID(),request);
			
			return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_CLIENT_RECORDS_IMPORT_EXPORT_CLIENT);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_IMPORT_CLIENT_RECORD_UPLOAD_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_IMPORT_EXPORT_CLIENT_RECORDS_MODULE,
					FinexaConstant.MY_BUSINESS_IMPORT_CLIENT_RECORD_UPLOAD_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	private boolean validUploadFileCsv(FileuploadDTO fileuploadDTO, UploadResponseDTO uploadResponseDTO) {

		log.debug("inside validUploadFileCsv import sheet");

		String line = "";
		long rowNum = 0;
		String cvsSplitBy = ",";

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream()))) {

			if (new BufferedReader(new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream())).lines()
					.count() < 2) {
				uploadResponseDTO.getErrors().add("No Data in CSV file");
			}

			while ((line = br.readLine()) != null) {

				if (rowNum == 0) {
					rowNum++;
					continue;
				}
				rowNum++;

				long displayRow = rowNum;
				String[] fields = line.split(cvsSplitBy);
				log.debug("fields length: " + fields.length);

				// firstName
				if (StringUtils.isEmpty(fields[0])) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", A) : " + "First Name should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(fields[0])) {
						if (!StringUtils.isAlpha(fields[0])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", A) : " + "First Name should contain alphabets");
						}
					}
				}
				// middleName
				if (fields.length < 38) {
					if (!fields[1].isEmpty()) {
						if (!StringUtils.isAlpha(fields[1])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", B) : " + "Middle Name should contain alphabets");
						}
					}
				}
				// lastName
				if (fields.length < 38) {
					if (fields[2].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", C) : " + "Last Name should not be Empty");
					} else {
						if (!StringUtils.isAlpha(fields[2])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", C) : " + "Last Name should contain alphabets");
						}
					}
				}
				// dateOfBirth
				if (fields.length < 38) {
					if (fields[3].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", D) : " + "Date of Birth should not be Empty");
					} else {
						Date dob = FinexaUtil.parseDate(fields[3]);
						if (dob == null) {
							uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", D) : " + "Date of Birth should be in dd-mm-yyyy format");
						}
					}
				}
				// gender
				if (fields.length < 38) {
					if (fields[4].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", E) : " + "Gender should not be Empty");
					} else {
						if (!StringUtils.equalsIgnoreCase(fields[4], "M")
								&& !StringUtils.equalsIgnoreCase(fields[4], "F")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", E) : " + "Gender should be either M or F");
						}
					}
				}
				// pan
				if (fields.length < 38) {
					if (fields[5].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", F) : " + "Pan should not be Empty");
					} else {
						System.out.println("PAN : " + fields[5]);
						if (!StringUtils.isAlphanumeric(fields[5])) {
							uploadResponseDTO.getErrors().add(
									"Cell (" + displayRow + ", F) : " + "Pan should contain alphabets and numbers");
						} else {
							if (StringUtils.isAlphanumeric(fields[5])) {
								if (isUniquePan(fields[5]) == false) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", F) : " + "Pan should be unique");
								}
							}
						}
					}
				}
				// adhaar
				String adhaar =(fields[6]);
				if (fields.length < 38) {
					if (fields[6].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", G) : " + "Adhaar should not be Empty");
					} else {
						System.out.println("aadhar : " + fields[6]);
						if (!StringUtils.isNumeric(fields[6])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", G) : " + "Adhaar should contain numbers");
						} else {
							if (StringUtils.isNumeric(fields[6])) {
								if (isUniqueAadharCheck(adhaar) == false) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", G) : " + "Adhaar should be unique");
								} else {
									if (isUniqueAadharCheck(adhaar) == true) {
										if (StringUtils.length(fields[6]) != 12) {
											uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", G) : "
													+ "Adhaar should contain 12 digits");
										}
									}
								}
							}
						}
					}
				}
				// MaritalStatus
				if (fields.length < 38) {
					if (fields[7].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", H) : " + "Marital Status should not be Empty");
					} else {
						if (!StringUtils.isAlpha(fields[7])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", H) : " + "Marital Status should contain alphabets");
						} else {
							if (StringUtils.isAlpha(fields[7])) {
								LookupMaritalStatus lookupMaritalStatus = maritalStatusRepository
										.findByDescription(fields[7]);
								;
								if (lookupMaritalStatus == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", H) : " + "Marital Status not valid");
								}
							}
						}
					}
				}
				// otherMaritalStatus
				if (fields.length < 38) {
					if (StringUtils.equalsIgnoreCase(fields[7], "Other")) {
						if (fields[8].isEmpty()) {
							uploadResponseDTO.getErrors().add(
									"Cell (" + displayRow + ", I) : " + "Other Marital Status should not be Empty");
						} else {
							if (!StringUtils.isAlpha(fields[8])) {
								uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", I) : "
										+ "Other Marital Status should contain alphabets");
							}
						}
					}
				}
				// educationalQualification
				if (fields.length < 38) {
					if (fields[9].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", H) : " + "Educational Qualification should not be Empty");
					} else {
						if (!StringUtils.isAlphaSpace(fields[9])) {
							uploadResponseDTO.getErrors().add(
									"Cell (" + displayRow + ", J) : " + "Educational Qualification should be in alphabets");
						} else {
							if (!fields[9].isEmpty()) {
								LookupEducationalQualification lookupEducationalQualification = educationalQualificationRepository
										.findByDescription(fields[9]);
								if (lookupEducationalQualification == null) {
									uploadResponseDTO.getErrors().add(
											"Cell (" + displayRow + ", J) : " + "Educational Qualification is not valid");
								}
							}
						}
					}
				}
				// otherEducationalQualification
				if (fields.length < 38) {
					if (StringUtils.equalsIgnoreCase(fields[9], "Other")) {
						if (fields.length < 11) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", K) : "
									+ "Other Educational Qualifications should not be Empty");
						} else {
							if (!StringUtils.isAlpha(fields[10])) {
								uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", K) : "
										+ "Other Educational Qualifications should contain alphabets");
							}
						}
					}
				}
				// employmentType
				if (fields.length < 38) {
					if (fields[11].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", H) : " + "Employment Type should not be Empty");
					} else {
						if (!StringUtils.isAlphaSpace(fields[11])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", L) : " + "Employment Type should be alphabets");
						} else {
							if (!fields[11].isEmpty()) {
								LookupEmploymentType lookupEmploymentType = employmentTypeRepository
										.findByDescription(fields[11]);
								if (lookupEmploymentType == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", L) : " + "Employment Type is not valid");
								}
							}
						}
					}
				}
				// otherEmploymentType
				if (fields.length < 38) {
					if (StringUtils.equalsIgnoreCase(fields[11], "Other")) {
						if (fields[12].isEmpty()) {
							uploadResponseDTO.getErrors().add(
									"Cell (" + displayRow + ", M) : " + "Other Employment Type should not be Empty");
						} else {
							if (!StringUtils.isAlpha(fields[12])) {
								uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", M) : "
										+ "Other Employment Type should contain alphabets");
							}
						}
					}
				}
				// organisationName
				if (fields.length < 38) {
					if (!StringUtils.isAlphaSpace(fields[13])) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", N) : " + "Organisation Name should contain alphabets");
					}
				}
				// currentDesignation
				if (fields.length < 38) {
					if (!StringUtils.isAlphaSpace(fields[14])) {
						uploadResponseDTO.getErrors().add(
								"Cell (" + displayRow + ", O) : " + "Current Designation should contain alphabets");
					}
				}
				// residentType
				if (fields.length < 38) {
					if (fields.length < 16) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", P) : " + "Resident Type should not be Empty");
					} else {
						if (!StringUtils.isAlphaSpace(fields[15])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", P) : " + "Resident Type should contain alphabets");
						} else {
							if (StringUtils.isAlphaSpace(fields[15])) {
								LookupResidentType lookupResidentType = residentTypeRepository
										.findByDescription(fields[15]);
								if (lookupResidentType == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", P) : " + "Resident Type is not valid");
								}
							}
						}
					}
				}
				
				//OtherResidentType
				if (fields.length < 38) {
					if (StringUtils.equalsIgnoreCase(fields[15], "Other")) {
						if (fields[16].isEmpty()) {
							uploadResponseDTO.getErrors().add(
									"Cell (" + displayRow + ", Q) : " + "Other Resident Type should not be Empty");
						} else {
							if (!StringUtils.isAlpha(fields[16])) {
								uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", Q) : "
										+ "Other Resident Type should contain alphabets");
							}
						}
					}
				}
				
				// countryOfResidence
				if (fields.length < 38) {
					if (fields.length < 18) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", R) : " + "Country of Residence should not be Empty");
					} else {
						if (!StringUtils.isAlphaSpace(fields[17])) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", R) : "
									+ "Country of Residence should contain alphabets");
						} else {
							if (StringUtils.isAlphaSpace(fields[17])) {
								LookupCountry lookupCountry = countryRepository.findByName(fields[17]);
								if (lookupCountry == null) {
									uploadResponseDTO.getErrors().add(
											"Cell (" + displayRow + ", R) : " + "Country of Residence is not valid");
								}
							}
						}
					}
				}
				// retired
				if (fields.length < 38) {
					if (fields.length < 19) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", S) : " + "Retired should not be Empty");
					} else {
						if (!StringUtils.containsOnly(fields[18], "Y") && !StringUtils.containsOnly(fields[18], "N")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", S) : " + "Retired should be either Y or N");
						}
					}
				}
				// retirementAge
				if (fields.length < 38) {
					if (fields.length < 20) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", T) : " + "Retirement Age should not be Empty");
					} else {
						if (!StringUtils.isNumeric(fields[19])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", T) : " + "Retirement Age should be a number");
						}
					}
				}
				// emailAddress
				if (fields.length < 38) {
					if (fields.length < 21) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", U) : " + "Email Address should not be Empty");
					} else {
						EmailValidator validateEmail = new EmailValidator();
						boolean check = validateEmail.isValid(fields[20], null);
						if (check == false) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", U) : " + "Email Address not valid");
						} else {
							if (isUniqueEmailId(fields[20]) == false) {
								uploadResponseDTO.getErrors()
										.add("Cell (" + displayRow + ", U) : " + "Email Address should be unique");
							}
						}
					}
				}
				// alternateEmail
				if (fields.length < 38) {
					if (fields.length > 21) {
						EmailValidator validateAlternateEmail = new EmailValidator();
						boolean check = validateAlternateEmail.isValid(fields[21], null);
						if (check == false) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", V) : " + "Alternate Email Address not valid");
						} else {
							if (isUniqueEmailId(fields[21]) == false) {
								uploadResponseDTO.getErrors().add(
										"Cell (" + displayRow + ", V) : " + "Alternate Email Address should be unique");
							}
						}
					}
				}
				// mobileNumber
				if (fields.length < 38) {
					if (fields.length < 23) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", W) : " + "Mobile Number should not be Empty");
					} else {
						if (!StringUtils.isNumeric(fields[22])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", W) : " + "Mobile Number should contain numbers");
						} else {
							if (StringUtils.isNumeric(fields[22])) {
								if (StringUtils.length(fields[22]) != 10) {
									uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", W) : "
											+ "Mobile Number should contain 10 digits");
								} else {
									if (checkUniqueMobile(Long.parseLong(fields[22])) == false) {
										uploadResponseDTO.getErrors()
												.add("Cell (" + displayRow + ", W) : " + "Mobile Number not unique");
									}
								}
							}
						}
					}
				}
				// emergencyNumber
				if (fields.length < 38) {
					if (fields.length < 24) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", X) : " + "Emergency Number should not be Empty");
					} else {
						if (!StringUtils.isNumeric(fields[23])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", X) : " + "Emergency Number should contain numbers");
						} else {
							if (StringUtils.isNumeric(fields[23])) {
								if (StringUtils.length(fields[23]) != 10) {
									uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", X) : "
											+ "Emergency Number should contain 10 digits");
								} else {
									if (checkUniqueEmergencyMobile(Long.parseLong(fields[23])) == false) {
										uploadResponseDTO.getErrors()
												.add("Cell (" + displayRow + ", X) : " + "Emergency Number not unique");
									}
								}
							}
						}
					}
				}
				// addressLine1
				if (fields.length < 38) {
					if (fields.length < 25) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", Y) : " + "Address Line 1 should not be Empty");
					}
				}
				// addressLine2
				// addressLine3
				// city
				if (fields.length < 38) {
					if (fields.length < 28) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", AB) : " + "City should not be Empty");
					} else {
						if (!StringUtils.isAlphaSpace(fields[27])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AB) : " + "City should contain alphabets");
						}
					}
				}
				// state
				if (fields.length < 38) {
					if (fields.length < 29) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", AC) : " + "State should not be Empty");
					} else {
						if (!StringUtils.isAlphaSpace(fields[28])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AC) : " + "State should contain alphabets");
						}
					}
				}
				// pinCode
				if (fields.length < 38) {
					if (fields[29].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", AD) : " + "Pin Code should not be Empty");
					} else {
						if (!StringUtils.isNumeric(fields[29])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AD) : " + "Pin Code should contain numbers");
						} else {
							if (StringUtils.isNumeric(fields[29])) {
								if (StringUtils.length(fields[29]) > 10) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", AD) : " + "Pin Code not valid");
								}
							}
						}
					}
				}
				// country
				if (fields.length < 38) {
					if (fields.length < 31) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", AE) : " + "Country should not be Empty");
					} else {
						if (!StringUtils.isAlphaSpace(fields[30])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AE) : " + "Country should be contain alphabets");
						} else {
							if (StringUtils.isAlphaSpace(fields[30])) {
								LookupCountry lookupCountry = countryRepository.findByName(fields[30]);
								if (lookupCountry == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", AE) : " + "Country is not valid");
								}
							}
						}
					}
				}
				// addressType
				if (fields.length < 38) {
					if (fields.length < 32) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", AF) : " + "Address Type should not be Empty");
					}

					if (fields.length == 32) {
						if (!StringUtils.equalsIgnoreCase(fields[31], "Office")
								&& !StringUtils.equalsIgnoreCase(fields[31], "Permanent")
								&& !StringUtils.equalsIgnoreCase(fields[31], "Correspondence")) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", AF) : "
									+ "Address Type must be either Office, Permanent or Correspondence");
						}
					}

				}
				// annualIncome
				if (fields.length < 38) {
					if (fields[32].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", AG) : " + "Annual Income should not be Empty");
					} else {
						if (!StringUtils.isNumeric(fields[32])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", S) : " + "Annual Income should only contain numbers");
						}
					}
				}
				// tobaccoUser
				if (fields.length < 38) {
					if (fields[33].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", AH) : " + "Tobacco User Flag should not be Empty");
					} else {
						if (!StringUtils.containsOnly(fields[33], "Y") && !StringUtils.containsOnly(fields[33], "N")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AH) : " + "Tobacco User Flag should be either Y or N");
						}
					}
				}
				// BMI
				if (fields.length < 38) {
					if (fields[34].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", AI) : " + "BMI Flag should not be Empty");
					} else {
						if (!StringUtils.containsOnly(fields[34], "Y") && !StringUtils.containsOnly(fields[34], "N")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AI) : " + "BMI Flag should be either Y or N");
						}
					}
				}
				// IllnessHistory
				if (fields.length < 38) {
					if (fields[35].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", AJ) : " + "History of Critical Illness Flag should not be Empty");
					} else {
						if (!StringUtils.containsOnly(fields[35], "Y") && !StringUtils.containsOnly(fields[35], "N")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AJ) : " + "History of Critical Illness Flag should be either Y or N");
						}
					}
				}
				// normalBP
				if (fields.length < 38) {
					if (fields[36].isEmpty()) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", AK) : " + "Normal Blood Pressure should not be Empty");
					} else {
						if (!StringUtils.containsOnly(fields[36], "Y") && !StringUtils.containsOnly(fields[36], "N")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AK) : " + "Normal Blood Pressure should be either Y or N");
						}
					}
				}

				continue;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		log.debug("error size : " + uploadResponseDTO.getErrors().size());
		return (uploadResponseDTO.getErrors().size() > 0) ? false : true;
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	private boolean validUploadFileExcel(FileuploadDTO fileuploadDTO, UploadResponseDTO uploadResponseDTO) {
		// TODO validate the fileuploadDTO
		Workbook workbook = null;
		Sheet sheet = null;
		try {
			workbook = Workbook.getWorkbook(fileuploadDTO.getFile()[0].getInputStream());
			sheet = workbook.getSheet(0);
			log.debug("inside validate sheet");
			if (sheet.getRows() < 2) {
				uploadResponseDTO.getErrors().add("No Data in Excel sheet");
			}

			for (int rownum = 1; rownum < sheet.getRows(); rownum++) {

				int displayRow = rownum + 1;
				/*
				 * //salutation if (StringUtils.isEmpty(sheet.getCell(0,
				 * rownum).getContents())){
				 * uploadResponseDTO.getErrors().add("Cell ("+ displayRow
				 * +", A ) : " + "salutation should not be Empty"); }
				 */
				// firstName
				if (StringUtils.isEmpty(sheet.getCell(0, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", A) : " + "First Name should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(0, rownum).getContents())) {
						if (!StringUtils.isAlpha(sheet.getCell(0, rownum).getContents().trim())) {
						//if (!StringUtils.isAlpha("Ronit")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", A) : " + "First Name should contain alphabets");
						}
					}
				}
				// middleName
				if (StringUtils.isNotEmpty(sheet.getCell(1, rownum).getContents())) {
					if (!StringUtils.isAlpha(sheet.getCell(1, rownum).getContents().trim())) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", B) : " + "Middle Name should contain alphabets");
					}
				}
				// lastName
				if (StringUtils.isEmpty(sheet.getCell(2, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", C) : " + "Last Name should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(2, rownum).getContents())) {
						if (!StringUtils.isAlpha(sheet.getCell(2, rownum).getContents().trim())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", C) : " + "Last Name should contain alphabets");
						}
					}
				}
				// dateOfBirth
				if (StringUtils.isEmpty(sheet.getCell(3, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", D) : " + "Date of Birth should not be Empty");
				} else {
					Date dob = FinexaUtil.parseDate(sheet.getCell(3, rownum).getContents());
					if (dob == null) {
						uploadResponseDTO.getErrors()
						.add("Cell (" + displayRow + ", D) : " + "Date of Birth should be in dd-mm-yyyy format");
					}
				}
				// gender
				if (StringUtils.isEmpty(sheet.getCell(4, rownum).getContents())) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", E) : " + "Gender should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(4, rownum).getContents())) {
						if (!StringUtils.equalsIgnoreCase(sheet.getCell(4, rownum).getContents(), "M")
								&& !StringUtils.equalsIgnoreCase(sheet.getCell(4, rownum).getContents(), "F")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", E) : " + "Gender should be either M or F");
						}
					}
				}
				// pan
				if (StringUtils.isEmpty(sheet.getCell(5, rownum).getContents())) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", F) : " + "Pan should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(5, rownum).getContents())) {
						if (!StringUtils.isAlphanumeric(sheet.getCell(5, rownum).getContents())) {
							uploadResponseDTO.getErrors().add(
									"Cell (" + displayRow + ", F) : " + "Pan should contain alphabets and numbers");
						} else {
							if (StringUtils.isAlphanumeric(sheet.getCell(5, rownum).getContents())) {
								if (isUniquePan(sheet.getCell(5, rownum).getContents()) == false) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", F) : " + "Pan should be unique");
								}
							}
						}
					}
				}
				// adhaar
				if (StringUtils.isEmpty(sheet.getCell(6, rownum).getContents())) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", G) : " + "Adhaar should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(6, rownum).getContents())) {
						if (!StringUtils.isNumeric(sheet.getCell(6, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", G) : " + "Adhaar should contain numbers");
						} else {
							if (StringUtils.isNumeric(sheet.getCell(6, rownum).getContents())) {
								//if (isUniqueAadhar(new BigInteger(sheet.getCell(6, rownum).getContents())) == false) {
								if (isUniqueAadharCheck((sheet.getCell(6, rownum).getContents())) == false) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", G) : " + "Adhaar should be unique");
								} else {
									if (isUniqueAadharCheck((sheet.getCell(6, rownum).getContents())) == true) {
										if (StringUtils.length(sheet.getCell(6, rownum).getContents()) != 12) {
											uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", G) : "
													+ "Adhaar should contain 12 digits");
										}
									}
								}
							}
						}
					}
				}
				// MaritalStatus
				if (StringUtils.isEmpty(sheet.getCell(7, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", H) : " + "Marital Status should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(7, rownum).getContents())) {
						if (!StringUtils.isAlpha(sheet.getCell(7, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", H) : " + "Marital Status should contain alphabets");
						} else {
							if (StringUtils.isAlpha(sheet.getCell(7, rownum).getContents())) {
								LookupMaritalStatus lookupMaritalStatus = maritalStatusRepository
										.findByDescription(sheet.getCell(7, rownum).getContents());
								;
								if (lookupMaritalStatus == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", H) : " + "Marital Status not valid");
								}
							}
						}
					}
				}
				// otherMaritalStatus
				if (StringUtils.equalsIgnoreCase(sheet.getCell(7, rownum).getContents(), "Other")) {
					if (StringUtils.isEmpty(sheet.getCell(8, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", I) : " + "Other Marital Status should not be Empty");
					} else {
						if (StringUtils.isNotEmpty(sheet.getCell(8, rownum).getContents())) {
							if (!StringUtils.isAlpha(sheet.getCell(8, rownum).getContents())) {
								uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", I) : "
										+ "Other Marital Status should contain alphabets");
							}
						}
					}
				}
				// educationalQualification
				if (StringUtils.isEmpty(sheet.getCell(9, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", H) : " + "Educational Qualification should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(9, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(9, rownum).getContents())) {
							uploadResponseDTO.getErrors().add(
									"Cell (" + displayRow + ", J) : " + "Educational Qualification should be in alphabets");
						} else {
							if (StringUtils.isAlphaSpace(sheet.getCell(9, rownum).getContents())) {
								LookupEducationalQualification lookupEducationalQualification = educationalQualificationRepository
										.findByDescription(sheet.getCell(9, rownum).getContents());
								if (lookupEducationalQualification == null) {
									uploadResponseDTO.getErrors().add(
											"Cell (" + displayRow + ", J) : " + "Educational Qualification is not valid");
								}
							}
						}
					}
				}
				// otherEducationalQualification
				if (StringUtils.equalsIgnoreCase(sheet.getCell(9, rownum).getContents(), "Other")) {
					if (StringUtils.isEmpty(sheet.getCell(10, rownum).getContents())) {
						uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", K) : "
								+ "Other Educational Qualifications should not be Empty");
					}
				}
				// employmentType
				if (StringUtils.isEmpty(sheet.getCell(11, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", H) : " + "Educational Qualification should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(11, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(11, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", L) : " + "Employment Type should be alphabets");
						} else {
							if (StringUtils.isAlphaSpace(sheet.getCell(11, rownum).getContents())) {
								LookupEmploymentType lookupEmploymentType = employmentTypeRepository
										.findByDescription(sheet.getCell(11, rownum).getContents());
								if (lookupEmploymentType == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", L) : " + "Employment Type is not valid");
								}
							}
						}
					}
				}
				// otherEmploymentType
				if (StringUtils.equalsIgnoreCase(sheet.getCell(11, rownum).getContents(), "Other")) {
					if (StringUtils.isEmpty(sheet.getCell(12, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", M) : " + "Other Employment Type should not be Empty");
					}
				}
				// organisationName
				if (StringUtils.isNotEmpty(sheet.getCell(13, rownum).getContents())) {
					if (!StringUtils.isAlphaSpace(sheet.getCell(13, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", N) : " + "Organisation Name should contain alphabets");
					}
				}
				// currentDesignation
				if (StringUtils.isNotEmpty(sheet.getCell(14, rownum).getContents())) {
					if (!StringUtils.isAlphaSpace(sheet.getCell(14, rownum).getContents())) {
						uploadResponseDTO.getErrors().add(
								"Cell (" + displayRow + ", O) : " + "Current Designation should contain alphabets");
					}
				}
				// residentType
				if (StringUtils.isEmpty(sheet.getCell(15, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", P) : " + "Resident Type should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(15, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(15, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", P) : " + "Resident Type should contain alphabets");
						} else {
							if (StringUtils.isAlphaSpace(sheet.getCell(15, rownum).getContents())) {
								LookupResidentType lookupResidentType = residentTypeRepository
										.findByDescription(sheet.getCell(15, rownum).getContents());
								if (lookupResidentType == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", P) : " + "Resident Type is not valid");
								}
							}
						}
					}
				}
				
				
				// otherResidentType
				if (StringUtils.equalsIgnoreCase(sheet.getCell(15, rownum).getContents(), "Other")) {
					if (StringUtils.isEmpty(sheet.getCell(16, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", Q) : " + "Other Resident Type should not be Empty");
					}
				}
				/*
				 * //otherResidentType if
				 * (StringUtils.containsOnly(sheet.getCell(16,
				 * rownum).getContents(), "5")){ if
				 * (StringUtils.isEmpty(sheet.getCell(17,
				 * rownum).getContents())){
				 * uploadResponseDTO.getErrors().add("Cell ("+displayRow
				 * +", R) : " + "Other Resident Type should not be Empty"); } }
				 */
				// countryOfResidence
				if (StringUtils.isEmpty(sheet.getCell(17, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", R) : " + "Country of Residence should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(17, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(17, rownum).getContents())) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", R) : "
									+ "Country of Residence should contain alphabets");
						} else {
							if (StringUtils.isAlphaSpace(sheet.getCell(17, rownum).getContents())) {
								LookupCountry lookupCountry = countryRepository
										.findByName(sheet.getCell(17, rownum).getContents());
								if (lookupCountry == null) {
									uploadResponseDTO.getErrors().add(
											"Cell (" + displayRow + ", R) : " + "Country of Residence is not valid");
								}
							}
						}
					}
				}
				// retired
				if (StringUtils.isEmpty(sheet.getCell(18, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", S) : " + "Retired should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(18, rownum).getContents())) {
						if (!StringUtils.containsOnly(sheet.getCell(18, rownum).getContents(), "Y")
								&& !StringUtils.containsOnly(sheet.getCell(18, rownum).getContents(), "N")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", S) : " + "Retired should be either Y or N");
						}
					}
				}
				// retirementAge
				if (StringUtils.isEmpty(sheet.getCell(19, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", T) : " + "Retirement Age should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(19, rownum).getContents())) {
						if (!StringUtils.isNumeric(sheet.getCell(19, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", T) : " + "Retirement Age should be a number");
						}
					}
				}
				// emailAddress
				if (StringUtils.isEmpty(sheet.getCell(20, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", U) : " + "Email Address should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(20, rownum).getContents())) {
						EmailValidator validateEmail = new EmailValidator();
						boolean check = validateEmail.isValid(sheet.getCell(20, rownum).getContents(), null);
						if (check == false) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", U) : " + "Email Address not valid");
						} else {
							if (isUniqueEmailId(sheet.getCell(20, rownum).getContents()) == false) {
								uploadResponseDTO.getErrors()
										.add("Cell (" + displayRow + ", U) : " + "Email Address should be unique");
							}
						}
					}
				}
				// alternateEmail
				if (StringUtils.isNotEmpty(sheet.getCell(21, rownum).getContents())) {
					EmailValidator validateAlternateEmail = new EmailValidator();
					boolean check = validateAlternateEmail.isValid(sheet.getCell(21, rownum).getContents(), null);
					if (check == false) {
						uploadResponseDTO.getErrors()
								.add("Cell (" + displayRow + ", V) : " + "Alternate Email Address not valid");
					} else {
						if (isUniqueEmailId(sheet.getCell(21, rownum).getContents()) == false) {
							uploadResponseDTO.getErrors().add(
									"Cell (" + displayRow + ", V) : " + "Alternate Email Address should be unique");
						}
					}
				}
				// mobileNumber
				if (StringUtils.isEmpty(sheet.getCell(22, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", W) : " + "Mobile Number should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(22, rownum).getContents())) {
						if (!StringUtils.isNumeric(sheet.getCell(22, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", W) : " + "Mobile Number should contain numbers");
						} else {
							if (StringUtils.isNumeric(sheet.getCell(22, rownum).getContents())) {
								if (StringUtils.length(sheet.getCell(22, rownum).getContents()) != 10) {
									uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", W) : "
											+ "Mobile Number should contain 10 digits");
								} else {
									if (checkUniqueMobile(
											Long.parseLong(sheet.getCell(22, rownum).getContents())) == false) {
										uploadResponseDTO.getErrors()
												.add("Cell (" + displayRow + ", W) : " + "Mobile Number not unique");
									}
								}
							}
						}
					}
				}
				// emergencyNumber
				if (StringUtils.isEmpty(sheet.getCell(23, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", X) : " + "Emergency Number should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(23, rownum).getContents())) {
						if (!StringUtils.isNumeric(sheet.getCell(23, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", X) : " + "Emergency Number should contain numbers");
						} else {
							if (StringUtils.isNumeric(sheet.getCell(23, rownum).getContents())) {
								if (StringUtils.length(sheet.getCell(23, rownum).getContents()) != 10) {
									uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", X) : "
											+ "Emergency Number should contain 10 digits");
								} else {
									if (checkUniqueEmergencyMobile(
											Long.parseLong(sheet.getCell(23, rownum).getContents())) == false) {
										uploadResponseDTO.getErrors()
												.add("Cell (" + displayRow + ", X) : " + "Emergency Number not unique");
									}
								}
							}
						}
					}
				}
				// addressLine1
				if (StringUtils.isEmpty(sheet.getCell(24, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", Y) : " + "Address Line 1 should not be Empty");
				}
				// addressLine2
				// addressLine3
				// city
				if (StringUtils.isEmpty(sheet.getCell(27, rownum).getContents())) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", AB) : " + "City should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(27, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(27, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AB) : " + "City should contain alphabets");
						}
					}
				}
				// state
				if (StringUtils.isEmpty(sheet.getCell(28, rownum).getContents())) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", AC) : " + "State should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(28, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(28, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AC) : " + "State should contain alphabets");
						}
					}
				}
				// pinCode
				if (StringUtils.isEmpty(sheet.getCell(29, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", AD) : " + "Pin Code should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(29, rownum).getContents())) {
						if (!StringUtils.isNumeric(sheet.getCell(29, rownum).getContents())) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", AD) : "
									+ "Pin Code should contain numbers/alphabets or both");
						} else {
							if (StringUtils.isNumeric(sheet.getCell(29, rownum).getContents())) {
								if (StringUtils.length(sheet.getCell(29, rownum).getContents()) > 10) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", AD) : " + "Pin Code not valid");
								}
							}
						}
					}
				}
				// country
				if (StringUtils.isEmpty(sheet.getCell(30, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", AE) : " + "Country should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(30, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(30, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AE) : " + "Country should be contain alphabets");
						} else {
							if (StringUtils.isAlphaSpace(sheet.getCell(30, rownum).getContents())) {
								LookupCountry lookupCountry = countryRepository
										.findByName(sheet.getCell(30, rownum).getContents());
								if (lookupCountry == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", AE) : " + "Country is not valid");
								}
							}
						}
					}
				}
				// addressType
				if (StringUtils.isEmpty(sheet.getCell(31, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", AF) : " + "Address Type should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(31, rownum).getContents())) {
						if (!StringUtils.equalsIgnoreCase(sheet.getCell(31, rownum).getContents(), "Office")
								&& !StringUtils.equalsIgnoreCase(sheet.getCell(31, rownum).getContents(), "Permanent")
								&& !StringUtils.equalsIgnoreCase(sheet.getCell(31, rownum).getContents(),
										"Correspondence")) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", AF) : "
									+ "Address Type must be either Office, Permanent or Correspondence");
						}
					}
				}
				// annualIncome
				if (StringUtils.isEmpty(sheet.getCell(32, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", AG) : " + "Annual Income should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(32, rownum).getContents())) {
						if (!StringUtils.isNumeric(sheet.getCell(32, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AG) : " + "Annual Income should contain only numbers");
						}
					}
				}
				// tobaccoUser
				if (StringUtils.isEmpty(sheet.getCell(33, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", AH) : " + "Tobacco User Flag should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(33, rownum).getContents())) {
						if (!StringUtils.containsOnly(sheet.getCell(33, rownum).getContents(), "Y")
								&& !StringUtils.containsOnly(sheet.getCell(33, rownum).getContents(), "N")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AH) : " + "Tobacco User Flag should be either Y or N");
						}
					}
				}
				// BMI
				if (StringUtils.isEmpty(sheet.getCell(34, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", AI) : " + "Tobacco User Flag should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(34, rownum).getContents())) {
						if (!StringUtils.containsOnly(sheet.getCell(34, rownum).getContents(), "Y")
								&& !StringUtils.containsOnly(sheet.getCell(34, rownum).getContents(), "N")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AI) : " + "Tobacco User Flag should be either Y or N");
						}
					}
				}
				// criticalIllness
				if (StringUtils.isEmpty(sheet.getCell(35, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", AJ) : " + "Critical Illness Flag should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(35, rownum).getContents())) {
						if (!StringUtils.containsOnly(sheet.getCell(35, rownum).getContents(), "Y")
								&& !StringUtils.containsOnly(sheet.getCell(35, rownum).getContents(), "N")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AJ) : " + "Critical Illness Flag should be either Y or N");
						}
					}
				}
				// bloodPressure
				if (StringUtils.isEmpty(sheet.getCell(36, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", AK) : " + "Blood Pressure Flag should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(36, rownum).getContents())) {
						if (!StringUtils.containsOnly(sheet.getCell(36, rownum).getContents(), "Y")
								&& !StringUtils.containsOnly(sheet.getCell(36, rownum).getContents(), "N")) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", AK) : " + "Blood Pressure Flag should be either Y or N");
						}
					}
				}
			}

		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.debug("error size : " + uploadResponseDTO.getErrors().size());
		return (uploadResponseDTO.getErrors().size() > 0) ? false : true;
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoAddEdit')")
	public boolean isUniquePan(String pan) {
		ClientMaster cm = clientMasterRepository.findByPan(pan);
		return (cm != null) ? false : true;
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoAddEdit')")
	public boolean isUniqueAadhar(BigInteger aadhar) {
		//ClientMaster cm = clientMasterRepository.findByAadhar(aadhar);
		//return (cm != null) ? false : true;
		return false;
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoAddEdit')")
	public boolean isUniqueAadharCheck(String aadhar) {
		ClientMaster cm = clientMasterRepository.findByAadhar(aadhar);
		return (cm != null) ? false : true;
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoAddEdit')")
	public boolean isUniqueEmailId(String emailId) {
		boolean status;
		ClientContact cc = clientContactRepository.findByEmailID(emailId);
		if (cc != null) {
			status = false;
		} else {
			AdvisorUser advisorUser = advisorUserRepository.findByEmailID(emailId);
			if (advisorUser != null) {
				status = false;
			} else {
				status = true;
			}
		}
		//return (cc != null) ? false : true;
		return status;
	}
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoAddEdit')")
	public boolean checkUniqueMobile(long mobile) {
		// TODO Auto-generated method stub
		List<ClientMaster> listClientMaster = clientMasterRepository.getByActiveFlag("Y");
		int cc = 0;
		for (ClientMaster clientMaster : listClientMaster) {
			for (ClientContact clientContact : clientMaster.getClientContacts()) {
				// clientContact = clientContactRepository.findByEmailID(email);
				if (clientContact.getMobile().longValue() == mobile) {
					cc = 1;
					break;
				}

			}
		}

		return (cc != 0) ? false : true;
	}
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfoAddEdit')")
	public boolean checkUniqueEmergencyMobile(long emergencyMobile) {
		// TODO Auto-generated method stub
		List<ClientMaster> listClientMaster = clientMasterRepository.getByActiveFlag("Y");
		int cc = 0;
		for (ClientMaster clientMaster : listClientMaster) {
			for (ClientContact clientContact : clientMaster.getClientContacts()) {
				// clientContact = clientContactRepository.findByEmailID(email);
				if (clientContact.getEmergencyContact().longValue() == emergencyMobile) {
					cc = 1;
					break;
				}

			}
		}

		return (cc != 0) ? false : true;
	}
	
	 @PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientRecordsView')")
	@RequestMapping(value = "/clientRecord/exportClientByUser", method = RequestMethod.POST)
	public ResponseEntity<?> exportClientByUser(@RequestBody FormDataDTO formDataDTO) throws FinexaBussinessException {
		try {
			log.debug("formDataDTO : " + formDataDTO);
			List<ClientExportCsvDTO> list = viewUserManagmentService.exportClientByUser(formDataDTO);
			return new ResponseEntity<List<ClientExportCsvDTO>>(list, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_CLIENT_RECORDS_IMPORT_EXPORT_CLIENT);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_EXPORT_CLIENT_RECORD_EXPORT_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_IMPORT_EXPORT_CLIENT_RECORDS_MODULE,
					FinexaConstant.MY_BUSINESS_EXPORT_CLIENT_RECORD_EXPORT_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	 @PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientRecordsView')")
	@RequestMapping(value = "/clientRecord/getUserName/{selectedUserId}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserName(@PathVariable int selectedUserId) {
		ViewUserManagmentDTO user = viewUserManagmentService.findUserName(selectedUserId);
		return new ResponseEntity<ViewUserManagmentDTO>(user, HttpStatus.OK);
	}
	
	/*
	 * @RequestMapping(value = "getUserAndRoleByOrgName/{orgName}", method =
	 * RequestMethod.GET) public ResponseEntity<?>
	 * getUserAndRoleByOrgName(@PathVariable int orgName) {
	 * 
	 * UserDTO user = viewUserManagmentService.findUserAndRoleByOrgName(orgName);
	 * return new ResponseEntity<UserDTO>(user, HttpStatus.OK);
	 * 
	 * }
	 */
	// Saving Access Rights
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/saveAccessRights", method = RequestMethod.POST)
	public ResponseEntity<?> saveAccessRights(@Valid @RequestBody UserDTO userDTO) throws FinexaBussinessException, CustomFinexaException {
				try {
					userDTO = viewUserManagmentService.accessRights(userDTO);
					return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
				} catch (RuntimeException e) {
					FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
							.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
					FinexaExceptionHandling exception = finexaExceptionHandlingRepository
							.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CASH_UPDATE_ERROR);
					throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE,
							FinexaConstant.CLIENT_CASH_UPDATE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
				}
			}
	
	//For client access Rights
	  @PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientRecordsView')")
		@RequestMapping(value = "/saveAccessRightsForClient", method = RequestMethod.POST)
		public ResponseEntity<?> saveAccessRightsForClient(@Valid @RequestBody UserDTO userDTO) throws FinexaBussinessException, CustomFinexaException {
					try {
						userDTO = viewUserManagmentService.accessRightsForClient(userDTO);
						return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
					} catch (RuntimeException e) {
						FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
								.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
						FinexaExceptionHandling exception = finexaExceptionHandlingRepository
								.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CASH_UPDATE_ERROR);
						throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE,
								FinexaConstant.CLIENT_CASH_UPDATE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
					}
				}
	
    @PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/advisorAdminAccessRights", method = RequestMethod.POST)
	public ResponseEntity<?> advisorAdminAccessRights(@Valid @RequestBody UserDTO userDTO) throws FinexaBussinessException, CustomFinexaException {
				try {
					userDTO = viewUserManagmentService.advisorAdminAccessRights(userDTO);
					return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
				} catch (RuntimeException e) {
					FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
							.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
					FinexaExceptionHandling exception = finexaExceptionHandlingRepository
							.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_CASH_UPDATE_ERROR);
					throw new FinexaBussinessException(FinexaConstant.CLIENT_CASH_MODULE,
							FinexaConstant.CLIENT_CASH_UPDATE_ERROR, exception != null ? exception.getErrorMessage() : "", e);
				}
			}

		/*
		 * try { System.out.println(userDTO.size()); //List<ClientUCCResultDTO>
		 * resultDTOList = clientTransactService.invest(investDTO); return new
		 * ResponseEntity<List<UserDTO>>(userDTO, HttpStatus.OK); } catch
		 * (RuntimeException rexp) { throw new
		 * FinexaBussinessException("ClientTransact", "500",
		 * "Failed to get Orders From Cart"); }
		 */
    @PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	 @RequestMapping(value = "/findModuleByAdvisorId/{advisorID}", method = RequestMethod.GET) 
	 public ResponseEntity<?>findModuleByAdvisorId(@PathVariable int advisorID) throws FinexaBussinessException { 
		 try { 
		 List<AccessRightDTO> accessRightDTOList =viewUserManagmentService.findModuleByAdvisorId(advisorID); 
		 return new ResponseEntity<List<AccessRightDTO>>(accessRightDTOList, HttpStatus.OK); 
		 } catch (RuntimeException e){ 
			 FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
	         FinexaExceptionHandling exception = finexaExceptionHandlingRepository.findByFinexaBusinessSubmoduleAndErrorCode(subModule,FinexaConstant.MY_BUSINESS_USER_CREATION_GET_DATA_ERROR); 
	         throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
	      FinexaConstant.MY_BUSINESS_USER_CREATION_GET_DATA_ERROR, exception != null ?exception.getErrorMessage() : "", e); 
	      }
	 
	 }
	 
	 ///For client portal
    @PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientRecordsView')")
	 @RequestMapping(value = "/findModuleByClient/{userID}", method = RequestMethod.GET) 
	 public ResponseEntity<?>findModuleByClient(@PathVariable int userID) throws FinexaBussinessException { 
		 try { List<AccessRightDTO> accessRightDTOList =viewUserManagmentService.findModuleByClient(userID); 
		 return new ResponseEntity<List<AccessRightDTO>>(accessRightDTOList, HttpStatus.OK); } catch (RuntimeException e){ 
			 FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
	         FinexaExceptionHandling exception = finexaExceptionHandlingRepository.findByFinexaBusinessSubmoduleAndErrorCode(subModule,FinexaConstant.MY_BUSINESS_USER_CREATION_GET_DATA_ERROR); 
	 throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
	      FinexaConstant.MY_BUSINESS_USER_CREATION_GET_DATA_ERROR, exception != null ?exception.getErrorMessage() : "", e); 
	      }
	 
	 }
	 
	 ///For client portal
     @PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientRecordsView')")
	 @RequestMapping(value = "/findModuleForClient/{clientID}", method = RequestMethod.GET) 
	 public ResponseEntity<?>findModuleForClient(@PathVariable int clientID) throws FinexaBussinessException { 
		 try { 
			 AccessRightDTO accessRightDTO =viewUserManagmentService.findModuleForClient(clientID); 
		     return new ResponseEntity<AccessRightDTO>(accessRightDTO, HttpStatus.OK); } catch (RuntimeException e){ 
			 FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
	         FinexaExceptionHandling exception = finexaExceptionHandlingRepository.findByFinexaBusinessSubmoduleAndErrorCode(subModule,FinexaConstant.MY_BUSINESS_USER_CREATION_GET_DATA_ERROR); 
	 throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
	      FinexaConstant.MY_BUSINESS_USER_CREATION_GET_DATA_ERROR, exception != null ?exception.getErrorMessage() : "", e); 
	      }
	 
	 }
	 
     @PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	 @RequestMapping(value = "/findModuleByUserId/{userID}", method = RequestMethod.GET) 
	 public ResponseEntity<?>findModuleByUserId(@PathVariable int userID) throws FinexaBussinessException { 
		 try { 
			 UserDTO userDTO =viewUserManagmentService.findModuleByUserId(userID); 
		     return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK); } catch (RuntimeException e){ 
			 FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
	         FinexaExceptionHandling exception = finexaExceptionHandlingRepository.findByFinexaBusinessSubmoduleAndErrorCode(subModule,FinexaConstant.MY_BUSINESS_USER_CREATION_GET_DATA_ERROR); 
	 throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
	      FinexaConstant.MY_BUSINESS_USER_CREATION_GET_DATA_ERROR, exception != null ?exception.getErrorMessage() : "", e); 
	      }
	 
	 }
	 
	     @PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
		 @RequestMapping(value = "/findExistingModuleByUserId/{userID}", method = RequestMethod.GET) 
		 public ResponseEntity<?>findExistingModuleByUserId(@PathVariable int userID) throws FinexaBussinessException { 
			 try { 
				 AccessRightDTO accessRightDTO =viewUserManagmentService.findExistingModuleByUserId(userID); 
			     return new ResponseEntity<AccessRightDTO>(accessRightDTO, HttpStatus.OK); } catch (RuntimeException e){ 
				 FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository.findByCode(FinexaConstant.MY_BUSINESS_USER_MANAGEMENT_USER_CREATION);
		         FinexaExceptionHandling exception = finexaExceptionHandlingRepository.findByFinexaBusinessSubmoduleAndErrorCode(subModule,FinexaConstant.MY_BUSINESS_USER_CREATION_GET_DATA_ERROR); 
		 throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_USER_CREATION_MODULE,
		      FinexaConstant.MY_BUSINESS_USER_CREATION_GET_DATA_ERROR, exception != null ?exception.getErrorMessage() : "", e); 
		      }
		 
		 }
	 
//---------------------------------------------------------------------	
	 
		@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
		@RequestMapping(value = "/userCreation/uniqueOrganisationName", method = RequestMethod.GET)
		public ResponseEntity<?> checkUniqueOrganisation(@RequestParam("orgName") String orgName) throws FinexaBussinessException {
			try {
				boolean msg = viewUserManagmentService.checkUniqueOrganisationName(orgName);
				return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
			} catch (RuntimeException e) {
				throw new FinexaBussinessException("","","", e);
			}
		}
	 
	 @PreAuthorize("hasAnyRole('Admin','UserManagement')")
		@RequestMapping(value = "/userCreation/uniqueDisributorCode", method = RequestMethod.GET)
		public ResponseEntity<?> checkDisributorCode(@RequestParam("distributorCode") String distributorCode) throws FinexaBussinessException {
			try {
				boolean msg = viewUserManagmentService.checkUniqueDistributorCode(distributorCode);
				return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
			} catch (RuntimeException e) {
				throw new FinexaBussinessException("","","", e);
			}
	}
	 
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllActiveUsersByUnderAdvisorAdmin/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllActiveUsersByUnderAdvisorAdmin(@PathVariable int advisorID)
			throws FinexaBussinessException {
		try {
			List<AdvisorDTO> advisorDTOs = advisorService.getAllActiveUsersByUnderAdvisorAdmin(advisorID);
			return new ResponseEntity<List<AdvisorDTO>>(advisorDTOs, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("", "", "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllInactiveUsersByUnderAdvisorAdmin/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllInactiveUsersByUnderAdvisorAdmin(@PathVariable int advisorID)
			throws FinexaBussinessException {
		try {
			List<AdvisorDTO> advisorDTOs = advisorService.getAllInactiveUsersByUnderAdvisorAdmin(advisorID);
			return new ResponseEntity<List<AdvisorDTO>>(advisorDTOs, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("", "", "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/changeUserActiveStatus", method = RequestMethod.POST)
	public ResponseEntity<?> changeUserActiveStatus(@RequestBody int[] advisorIDs)
			throws FinexaBussinessException {
		try {
			int status = advisorService.changeUserActiveStatus(advisorIDs);
			return new ResponseEntity<Integer>(status, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("", "", "", e);
		}
	}
	 
}




	
	


