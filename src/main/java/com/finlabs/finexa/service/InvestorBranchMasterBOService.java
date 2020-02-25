package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.BranchMasterDetailsBODTO;
import com.finlabs.finexa.dto.InvestorBranchMasterBODTO;

public interface InvestorBranchMasterBOService {
	
	public InvestorBranchMasterBODTO save(InvestorBranchMasterBODTO roleDTO);
	
	//public List<InvestorBranchMasterBODTO> getAllMFBackOfficeBranch();
	
	public List<InvestorBranchMasterBODTO> getAllMFBackOfficeBranchByAdvisorId(int advisorId);

	//public InvestorBranchMasterBODTO findByBranchMasterCode(String branchMasterCode);
	public InvestorBranchMasterBODTO findByBranchMasterId(int branchMasterId);
	
	
	//public int deleteBranchDetailsByBranchCode(String branchCode);
	public int deleteBranchDetailsByBranchId(int branchId);
	
	public List<InvestorBranchMasterBODTO> getMFBackOfficeBranchNameAndIdByAdvisorId(int advisorId);

	
	
}
