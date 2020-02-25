package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.AdvisorUserDTO;
import com.finlabs.finexa.dto.BranchMasterDetailsBODTO;
import com.finlabs.finexa.exception.FinexaBussinessException;

public interface BranchMasterDetailsBOService {

	public List<AdvisorUserDTO> getBranchManagerUsers(int advisorUserId) throws FinexaBussinessException;
	
	public List<BranchMasterDetailsBODTO> getAllMFBackOfficeBranchByAdvisorId(int advisorId);
	
	public BranchMasterDetailsBODTO save(BranchMasterDetailsBODTO branchMasterDetailsBODTO, int advisorUserId);
	
	public BranchMasterDetailsBODTO findByBranchMasterId(int branchMasterId);
	
	public int deleteBranchDetailsByBranchId(int branchId);

	List<AdvisorUserDTO> getBranchManagerUsersName(int advisorUserId) throws FinexaBussinessException;

	public boolean checkIfBranchWithSameBranchHeadExists(int branchHeadId) throws RuntimeException;

	public boolean checkUniqueBranchCode(int userId, String branchCode);

	public boolean checkExistingBranchCode(int branchID, String branchCode);

	public BranchMasterDetailsBODTO update(BranchMasterDetailsBODTO branchMasterDetailsBODTO);

	public boolean checkIfBranchMasterExists(int branchHeadId);

	public boolean checkIfBranchMasterisAssigned(int branchHeadID);

	public boolean checkUniqueMobileNumber(String phoneNumber);

	public boolean checkExistingPhoneNumber(int branchBranchMasterID, String phoneNumber);

	public boolean checkIfBranchWithSameBranchHeadExistsForEdit(int branchHeadId,int branchId);

	
	
	
	
}
