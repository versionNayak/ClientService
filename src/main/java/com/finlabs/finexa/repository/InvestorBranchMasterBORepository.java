package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.InvestorBranchMasterBO;

public interface InvestorBranchMasterBORepository extends JpaRepository<InvestorBranchMasterBO, Integer>{
	
	List<InvestorBranchMasterBO> findByAdvisorUser(AdvisorUser advisorUser);

	List<InvestorBranchMasterBO> findBranchNameAndIdByAdvisorUser(AdvisorUser advisorUser);

	//InvestorBranchMasterBO findByBranchCode(String branchMasterCode);

	//void deleteByBranchCode(String branchCode);
	
	//InvestorBranchMasterBO findOne(int id);
	
	
}
