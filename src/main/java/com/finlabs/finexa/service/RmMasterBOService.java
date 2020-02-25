package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.dto.AdvisorUserDTO;
import com.finlabs.finexa.dto.BranchMasterDetailsBODTO;
import com.finlabs.finexa.dto.RmMasterBODTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;

public interface RmMasterBOService {
	
	RmMasterBODTO save (RmMasterBODTO rmMasterBODTO) throws RuntimeException, CustomFinexaException;

	List<RmMasterBODTO> findByAdvisorId(int advisorID) throws RuntimeException, CustomFinexaException;

	RmMasterBODTO findById(int id) throws RuntimeException;

	int delete(int id) throws RuntimeException;

	List<BranchMasterDetailsBODTO> getMFBackOfficeBranchNameAndIdByAdvisorId(int advisorId);

	RmMasterBODTO update(RmMasterBODTO rmMasterBODTO);

	List<AdvisorUserDTO> getRelationshipManagerUsersNameForParticularBranch(int branchId) throws RuntimeException;

	boolean checkIfRmManagerRoleExists(int advisorId);

	boolean checkUniqueEmpCodeForAdvisorMaster(int advisorID,String employeeCode);

	boolean checkExistingEmpCodeForAdvisorMaster(int advisorID,int userID, String employeeCode);

	boolean checkUniqueEmailForAdvisorMaster(String emailId,int advisorID );

	boolean checkExistsEmailForAdvisorMaster(String email,int advisorID,int UserID);

	boolean checkRmOrSbIsAssigned(int userID);

	boolean checkUniqueMobileNumber(String mobileNo);

	boolean checkExistingMobileNumber(String mobileNo,int userID);
	
		
}
