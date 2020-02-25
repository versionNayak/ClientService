package com.finlabs.finexa.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.web.multipart.MultipartFile;

import com.finlabs.finexa.dto.AdvisorRoleSubmoduleMappingDTO;
import com.finlabs.finexa.dto.ClientExportCsvDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.AccessRightDTO;
import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.dto.AdvisorRoleDTO;
import com.finlabs.finexa.dto.FinexaBusinessModuleDTO;
import com.finlabs.finexa.dto.FinexaBusinessSubmoduleDTO;
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
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.User;

import jxl.write.WritableWorkbook;



public interface ViewUserManagmentService {
	
	/*********************For User Creation ********************************/
	public List<ViewUserManagmentDTO> getAllUserList(int userId) throws RuntimeException;
	public UserDTO save(int loggedUserId, UserDTO userDTO, String serviceIP) throws RuntimeException, JsonParseException, JsonMappingException, IOException;
	public UserDTO update(UserDTO userDTO) throws RuntimeException;
	public UserDTO getExistingUser(int userId) throws RuntimeException;
	boolean checkEmailExists(String email, int userId) throws RuntimeException;
	boolean checkMobileExists(BigInteger mobile, int clientId) throws RuntimeException;
	boolean checkUniqueEmail(String email) throws RuntimeException;
	boolean checkUniqueMobile(BigInteger mobile) throws RuntimeException;
	boolean checkEmployeeCodeExists(String employeeCd, int userId) throws RuntimeException;
	boolean checkUniqueEmployeeCode(String employeeCd) throws RuntimeException;
	
	
	/********************** For User Role Creation ***************************/
	public List<UserRoleCreationDTO> getAllUserRoleSuperVisorRole(int userId) throws RuntimeException;
	public List<UserRoleCreationDTO> getAllUserRoleCreationList(int userId);
	String checkRoleExists(String role, int advisorId);
	String getExistingRoleForOrg(int masterID, int roleID);
	//boolean checkUniqueRole(String role) throws RuntimeException;
	
	/********************** For User & User Role Remapping *************************/
	public UserRoleCreationDTO saveRole(UserRoleCreationDTO userRoleCreationDTO) throws RuntimeException;
	public UserRoleCreationDTO updateRole(UserRoleCreationDTO userRoleCreationDTO) throws RuntimeException;

	public List<UserRoleReMappingDTO> getAllUserRoleRemapping(int userId) throws RuntimeException;
	public UserRoleReMappingDTO saveRoleRemapping(UserRoleReMappingDTO userRoleReMappingDTO) throws RuntimeException;
	public UserRoleReMappingDTO updateRoleRemapping(UserRoleReMappingDTO userRoleReMappingDTO) throws RuntimeException;
	
	/*********************** For User Role Hierarchy Mapping *************************/
	public List<UserHierarchyMappingDTO> getAllHierarchiesByAdvisorId(int userId) throws RuntimeException;
	public List<UserHierarchyMappingDTO> getOtherSupervisorsWithSameRole(int userId, int roleId) throws RuntimeException;
	public UserHierarchyMappingDTO saveUserHierarchy(UserHierarchyMappingDTO userHierarchyMappingDTO) throws RuntimeException;
	public UserHierarchyMappingDTO updateUserHierarchy(UserHierarchyMappingDTO userHierarchyMappingDTO) throws RuntimeException;
	public UserHierarchyMappingDTO getUserHierarchy(int userId) throws RuntimeException;
	public List<AdvisorDTO> getAllUserForSupervisorRole(int supRoleId) throws RuntimeException;
	
	
	/************************** For Access Rights ************************************/
	public List<RoleDTO> getAllExistingRoles(int userId) throws RuntimeException;
	public List<FinexaBusinessModuleDTO> getAllBusinessModules() throws RuntimeException;
	public List<FinexaBusinessSubmoduleDTO> getAllBusinessSubModules(int moduleId, int roleId) throws RuntimeException;
	public void saveRoleSubModuleMapping(AdvisorRoleSubmoduleMappingDTO advisorRoleSubmoduleMappingDTO) throws RuntimeException, CustomFinexaException;
	
	
	/************************** For Manage Password ************************************/
	public List<ManagePasswordDTO> getAllUsersToManagePassword(int userId) throws RuntimeException;
	
	
	
	/***************************** For Deletion ******************************************/
	public UserDeleteReturnDTO deleteUser(int id) throws RuntimeException;
	UserDeleteReturnDTO deleteRole(int roleId) throws RuntimeException;
	
	/****************************** For Download Template **********************************/
	public WritableWorkbook downloadClientTemplateExcel(HttpServletResponse response) throws RuntimeException;
/*	public ErrorDTO uploadClientByUser(MultipartFile fileData,int userId);*/
	public List<ClientExportCsvDTO> exportClientByUser(FormDataDTO formDataDTO) throws RuntimeException;
	/***************************** Getting all organisations ******************************************/
	//public List<AdvisorDTO> getAllOrganisations(int userId);
	//List<AdvisorDTO> getAllOrganisations();
	public UploadResponseDTO uploadClientByUser(FileuploadDTO fileuploadDTO,UploadResponseDTO uploadResponseDTO) throws RuntimeException, CustomFinexaException;
	
	public void downloadClientTemplateCSVForImport(HttpServletResponse response) throws IOException, RuntimeException;
	
	public ViewUserManagmentDTO findUserName(int selectedUserId);
	public List<AccessRightDTO> findModuleByAdvisorId(int advisorID) throws RuntimeException;
	public List<UserDTO> findUserAndRoleByUserId(int userId) throws RuntimeException;
	//public UserDTO findUserAndRoleByOrgName(int orgName) throws RuntimeException;
	public List<AccessRightDTO> getAllModules(String orgFlag) throws RuntimeException;
	public List<UserDTO> getAllUserForSelectedRole(int masterID, int roleId) throws RuntimeException;
	//public List<UserDTO> getUsersByOrgAndRole(String orgName, int roleId) throws RuntimeException;
	public UserDTO accessRights(UserDTO userDTO) throws RuntimeException;
	//public List<UserDTO> getUsersByOrgAndRole(int orgName, int roleId) throws RuntimeException;
	UserDTO getAllRolesForSelectedOrg(int masterID) throws RuntimeException;
	public UserDTO advisorAdminAccessRights(UserDTO userDTO) throws RuntimeException;
	public int generateDemoLogin(int userID, InputStream resource, List<Integer> questionID);
	boolean checkUniqueOrganisationName(String orgName) throws RuntimeException;
	List<UserDTO> getAllUserForSelectedSupervisorRole(int masterID, String roleDescription) throws RuntimeException;
	boolean checkUniqueRole(int masterID,String role) throws RuntimeException;
	public List<RoleDTO> getAllExistingRolesForUserCreation(int userId) throws RuntimeException;
	UserHierarchyMappingDTO getUserSpecific(int userId) throws RuntimeException;
	boolean checkSupervisor(int userId) throws RuntimeException;
	UserDTO findModuleByUserId(int userID) throws RuntimeException;
	boolean checkSupervisorMapping(int userId) throws RuntimeException;
	int delete(int advisorId) throws RuntimeException;
	boolean checkRoleUser(int userId) throws RuntimeException;
	//void generateDemoLogin(int masterID, int userID, InputStream resource, List<Integer> questionID);
	public List<AdvisorUser> checkUniqueEmpCodeForFixedMaster(String orgName, String distCode, String empCode);
	public List<AdvisorDTO> checkUniqueEmpCodeForFixedmaster(String orgName, String distCode, String empCode);
	boolean checkUniqueDistributorCode(String distributorCode) throws RuntimeException;
	public List<ViewUserManagmentDTO> getUnsupervisedUserList(int id) throws RuntimeException;
	public List<ViewUserManagmentDTO> getAllUsersForClientContact(int userId) throws RuntimeException;
	public List<AccessRightDTO> findModuleByClient(int userID) throws RuntimeException;
	public AccessRightDTO findModuleForClient(int clientID) throws RuntimeException;
	//public List<UserDTO> getAllAccessRight() throws RuntimeException;
	public AccessRightDTO findExistingModuleByUserId(int userID) throws RuntimeException;
	public UserDTO accessRightsForClient(UserDTO userDTO) throws RuntimeException;
	public RoleDTO getExistingRoleForUserEdit(int roleId) throws RuntimeException;
	public UserDTO update(int userId, UserDTO userDTO);
	//public boolean checkMobileExists(BigInteger mobile, int userId);
	
}
