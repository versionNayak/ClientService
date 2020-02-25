package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.BranchMasterDetailsBO;

public interface BranchMasterDetailsBORepository extends JpaRepository<BranchMasterDetailsBO, Integer>{

	BranchMasterDetailsBO findByAdvisorUser(AdvisorUser advisorUser);
	
	List<BranchMasterDetailsBO> findAllByAdvisorUser(AdvisorUser advisorUser);

	BranchMasterDetailsBO findByadvisorUser(AdvisorUser advisorUser);

	BranchMasterDetailsBO findById(int branchBranchMasterID);
	
	//String findBranchNameByAdvisorUser(int branchHeadId);
		
}
