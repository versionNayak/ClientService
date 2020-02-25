package com.finlabs.finexa.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;

import com.finlabs.finexa.dto.ClientFixedIncomeDTO;
import com.finlabs.finexa.dto.ClientInfoDTO;
import com.finlabs.finexa.dto.ClientLoginInfoDTO;
import com.finlabs.finexa.dto.ClientMasterDTO;
import com.finlabs.finexa.dto.SearchClientDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.ClientMaster;

import jxl.write.WritableWorkbook;


public interface ClientMasterService {

	ClientMasterDTO save(ClientMasterDTO clientMasterDTO) throws RuntimeException, CustomFinexaException;
	
	ClientMasterDTO update(ClientMasterDTO clientMasterDTO) throws RuntimeException, CustomFinexaException;
	
	List<ClientInfoDTO> findAll();

	List<ClientInfoDTO> findAllClientByUserID(int userId) throws RuntimeException;

	ClientMasterDTO find(int clientId) throws RuntimeException, CustomFinexaException;
	
	boolean isUniqueEmailId(String emailId);

	boolean isUniquePan(String pan) throws RuntimeException;

	boolean isUniqueAadhar(String aadhar) throws RuntimeException;

	ClientMaster deActivate(int clientId, HttpServletRequest request);

	List<ClientInfoDTO> searchClient(SearchClientDTO searchClientDTO) throws RuntimeException;

	boolean checkPanExists(String email, int clientId);

	boolean checkAadharExists(long aadhar, int clientId) throws RuntimeException;

	List<String> getAllOrganiations() throws RuntimeException;
	
	WritableWorkbook downloadClientTemplate(HttpServletResponse response);

	List<ClientInfoDTO> findAllClientDashBoardByUserID(int userId);

	int findAllAddedClientByUserID(int userId, int value);

	List<ClientInfoDTO> findClientBirthdayByUserID(int userId,int value) throws ParseException, RuntimeException, CustomFinexaException;

	List<ClientFixedIncomeDTO> getAssetForMaturityRenewal();

	List<ClientInfoDTO> searchClientBusiness(SearchClientDTO searchClientDTO) throws RuntimeException;

	List<ClientInfoDTO> writeExcelMasterListForAdvisor(int userId, int value)
			throws ParseException, RuntimeException, CustomFinexaException;

	List<ClientInfoDTO> findAllDashBoardByUserAndTimePeriod(int userId, int timePeriod);

	ClientMaster autoSave(ClientMasterDTO clientMasterDTO) throws RuntimeException, CustomFinexaException;

	List<ClientInfoDTO> findAllClientByUserHierarchy(int userId) throws RuntimeException;

	boolean isUniquePanForFixedAdvisor(String pan, int advisorID);

	boolean isUniqueAadharForFixedAdvisor(String aadhar, int advisorID);

	ClientLoginInfoDTO checkLoggedInOrNot(String username) throws RuntimeException;

	List<String> getAllOrganiations(int advisorID) throws RuntimeException;

	List<ClientInfoDTO> findAllClientByUserHierarchy(int userId, Pageable pagebale) throws RuntimeException;

	List<ClientInfoDTO> searchClientDynamicallyByUserHierarchy(int userId, String matchString) throws RuntimeException;

	boolean checkIfRelationAlreadyExists(int userId, byte relationID);

	boolean groupFamilyMembers(int clientId1, int clientId2, byte relationID, String otherRelation);

	boolean checkIfFamilypresent(int clientID);

	List<ClientInfoDTO> searchClientByEmailDynamicallyByUserHierarchy(int userId, String matchString)
			throws RuntimeException;

	boolean checkPasswordExists(String email, String password) throws RuntimeException;
	boolean changePasswordByEmailId(String password, String emailId);

	
	
}
