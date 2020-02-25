package com.finlabs.finexa.repository;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.InvestorMasterBO;

public interface InvestMasterBORepository extends JpaRepository<InvestorMasterBO, String>,JpaSpecificationExecutor<InvestorMasterBO>{
	
	public List<InvestorMasterBO> findByAdvisorUser(AdvisorUser advUser);
	
	public InvestorMasterBO findByInvestorNameAndAdvisorUser(String investorName, AdvisorUser advUser);
	
	public List<InvestorMasterBO> findAllByInvestorNameAndAdvisorUser(String investorName, AdvisorUser advUser);
	
	@Query("select distinct imBO.id.investorPAN, imBO.investorName from InvestorMasterBO imBO where imBO.advisorUser.id= :advisorId")
	public List<Object[]> findAllDistinctInvestorNameAndPAN(@Param("advisorId") int advisorId);
	
	@Query("select distinct imBO.id.investorPAN, imBO.investorName from InvestorMasterBO imBO where imBO.advisorUser.id= :advisorId and imBO.createdOn=:createdOn")
	public List<Object[]> findAllDistinctInvestorNameAndPANByAdvisorIdAndCreatedOn(@Param("advisorId") int advisorId,@Param("createdOn") String createdOn);
	
}
