package com.finlabs.finexa.service;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dozer.Mapper;
import org.springframework.data.domain.Pageable;
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
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserLoginInfo;
import com.finlabs.finexa.model.ClientForgotPassword;
import com.finlabs.finexa.model.ClientLoginInfo;
import com.finlabs.finexa.model.UserClientRedis;
import com.finlabs.finexa.repository.AdvisorUserLoginInfoRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.util.CacheInfoService;

import com.finlabs.finexa.repository.ClientLoginInfoRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

//import jxl.write.WritableWorkbook;

public interface AdvisorService {
	AdvisorDTO save(AdvisorDTO userDTO);
	public void AdvisorForgotPasswordDTOsave(AdvisorForgotPasswordDTO forgotDTO);
	AdvisorDTO findByUsernameAndPassword(String username, String password);
	Boolean changePasswordByEmailId(String password, String emailId);
	List<AdvisorDTO> findAllUsersForOrg(int advisorMasterId);
	public void sendVerificationLink(String email,String verfificationLink) throws RuntimeException;
	public String verifiyUserLink(String encodedUserId,double timestamp) throws RuntimeException;
	AdvisorUser resetPassword(int advisorId) throws RuntimeException;
	AdvisorUser resetPasswordIndex(int userId) throws RuntimeException;
	//UploadResponseDTO uploadBulkUsers(FileuploadDTO fileuploadDTO) throws RuntimeException, CustomFinexaException;
	WritableWorkbook downloadUserTemplate(HttpServletResponse response, int advisorId) throws RuntimeException, IOException, RowsExceededException, WriteException, CustomFinexaException;
	boolean checkUniqueEmail(String email);
    boolean checkEmailExists(String email) throws RuntimeException;
    boolean checkPasswordExists(String email, String password) throws RuntimeException;
	boolean checkUserExist(String email) throws RuntimeException;
	public List<AdvisorUserLoginInfoDTO> findByLoggedInId(int loggedId) throws RuntimeException;
	public boolean changePass(String userID,String password,String dateTime);
	public String validateForgotUserPassword(String encodeID);
	
	public void saveAdvisorUserLogo(MultipartFile uploadfile,int id) throws IOException;
	
	public byte[] getAdvisorLogo(int id) throws IOException;
	
	public String encodedUserId(int userId) throws RuntimeException;
	public String generateUUID();
	public String decodedUserId(String encodedUserId) throws RuntimeException;
	public boolean advisor_change_Pass(String uuid,String password);
	
	public ClientUCCResultDTO registerBSEcredentials(String username, String memberId, String password, int advisorUserID, int mode);
	public AdvisorDTO login(LoginDTO loginDTO);
	//List<AdvisorDTO> findAll();
	public int deactivateActiveFlag(int id);
	public int activateActiveFlag(int id);
	public List<AdvisorDTO> findAllSelf();
	public List<AdvisorDTO> findAllMaster();
	public List<AdvisorDTO> findAllOrg(int advisorid);
	public List<AdvisorDTO> findAllOrgName();
	public DashBoardDTO getClientByOffset(int advisorId, UserClientRedis userDetailsDTO, String token, int userID);
	public AdvisorDTO logout(String token, int advisorUserId);
	public List<AdvisorDTO> findSelf();
	public List<UserDTO> findAllHierarchyList(int advisorID);
	boolean checkUniqueMobile(String mobileNo);
	public UploadResponseDTO uploadBulkUsers(int advisorID, FileuploadDTO fileuploadDTO)
			throws RuntimeException, CustomFinexaException;
	//public WritableWorkbook downloadUserTemplateForAdvisorAdmin(HttpServletResponse response, int advisorId)
	//		throws CustomFinexaException, IOException, RowsExceededException, WriteException, RuntimeException;
	public void deletetAUMCacheMap(int advisorUserId, HttpServletRequest request);
	//AdvisorDTO logoutRedis(String token, AdvisorUserLoginInfoRepository advisorUserLoginInfoRepository,
	//		AdvisorUserRepository advisorUserRepository, Mapper mapper, CacheInfoService cacheInfoService);
	boolean checkEmailExistsForClient(String email) throws RuntimeException;
	public void sendVerificationLinkForClient(String emailID, String verficationLink);
	public ClientInfoDTO findByUsernameAndPasswordForClient(String username, String password);
	public AdvisorUserLoginInfoDTO checkLoggedInOrNot(String username) throws RuntimeException;
	public ClientMasterDTO loginClient(LoginDTO loginDTO);
	public ClientLoginInfoDTO checkLoggedInOrNotForClient(String username);
	public ClientMasterDTO logoutForClient(String token, int clientID);
	public ClientMasterDTO logoutRedisForClient(String token,ClientLoginInfo clientLoginInfo);
	public ClientMasterDTO logoutFromRedis(String token);
	public AdvisorDTO logoutRedisForUser(String token, AdvisorUserLoginInfo advisorUserLoginInfo);
	//pagination
	//public List<AdvisorDTO> findAll(Pageable pageable);
	public int getAdvisorCount();
	public List<AdvisorDTO> findAllWithPagination(Pageable pageable);
	public int checkIfUserExistUnderLoggedInAdvisorAdmin(int advisorID);
	public List<AdvisorUserLoginInfoDTO> findByLoggedInIdWithPagination(int loggedId, Pageable pageable);
	public int getLoggedInHistoryCount(int loggedId);
	public List<AdvisorDTO> findAllAdvisorAdmin();
	public int getSelfAdvisorCount();
	public List<AdvisorDTO> findAllSelf(Pageable pageable);
	public int getOrgAdvisorCount(int masterId);
	public List<AdvisorDTO> findAllOrg(int advisorid, Pageable pageable);
	public List<AdvisorDTO> getAllActiveUsersByUnderAdvisorAdmin(int advisorID) throws RuntimeException;
	public List<AdvisorDTO> getAllInactiveUsersByUnderAdvisorAdmin(int advisorID) throws RuntimeException;
	public int changeUserActiveStatus(int[] advisorIDs);
	
	public AdvisorDTO storePassword(AdvisorDTO advisorDTO, HttpServletRequest httpServletRequest) throws RuntimeException;
	public List<AdvisorDTO> searchMatchingEntries(String matchString);
	public void clientForgotPasswordDTOsave(ClientForgotPasswordDTO forgotDTO);
	public String verifiyUserLinkforClient(String encodedUserId, double timestamp) throws RuntimeException;
	boolean client_change_Pass(ClientForgotPassword clientForgotPassword, String password);
	public List<AdvisorDTO> getAllDistributors() throws RuntimeException;
	public int getLogCount(int userID);
	public List<AdvisorUserBulkUploadHistoryDTO> bulkUploadLog(int userID, Pageable pageable);
	public AdvisorUserBulkUploadHistoryDTO getLogDetails(int logDetailsId);
	public AdvisorUserBulkUploadHistoryDTO getRunningProcess(int userID);

}
