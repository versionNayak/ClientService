package com.finlabs.finexa.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.StagingInvestorMasterBO;

public interface StagingInvestorMasterBORepository extends JpaRepository<StagingInvestorMasterBO, Integer>,JpaSpecificationExecutor<StagingInvestorMasterBO>{

	public StagingInvestorMasterBO findByAdvisoruserAndInvestorPANAndInvestorName(AdvisorUser advUser, String investorPAN, String investorName);

	@Query("select distinct stBO.investorPAN, stBO.investorName from StagingInvestorMasterBO stBO where stBO.advisoruser.id = :advisorId")
	public List<Object[]> findAllDistinctInvestorNameAndPAN(@Param("advisorId") int advisorId);
	
	@Query("select distinct stBO.investorPAN, stBO.investorName from StagingInvestorMasterBO stBO where stBO.advisoruser.id = :advisorId and stBO.createdOn=:createdOn")
	public List<Object[]> findAllDistinctInvestorNameAndPANByAdvisorIdAndCreatedOn(@Param("advisorId") int advisorId,@Param("createdOn") Timestamp createdOn);
	
	
	@Query("select distinct stBO from StagingInvestorMasterBO stBO where stBO.advisoruser.id = :advisorId and stBO.investorName like :investorName and stBO.investorPAN = :investorPAN")
	public List<StagingInvestorMasterBO> findDistinctByAdvisoruserAndInvestorPANAndInvestorName(@Param("advisorId") int advisorId, @Param("investorName") String investorName, @Param("investorPAN") String investorPAN);
	
	@Query("select distinct stBO from StagingInvestorMasterBO stBO where stBO.advisoruser.id = :advisorId and stBO.investorPAN = :investorPAN")
	public List<StagingInvestorMasterBO> findDistinctByAdvisoruserAndInvestorPAN(@Param("advisorId") int advisorId, @Param("investorPAN") String investorPAN);

	@Query("select distinct stBO from StagingInvestorMasterBO stBO where stBO.advisoruser.id = :advisorId and stBO.investorName like :investorName")
	public List<StagingInvestorMasterBO> findDistinctByAdvisoruserAndInvestorName(@Param("advisorId") int advisorId, @Param("investorName") String investorName);
	
	@Query("SELECT siBO.id FROM StagingInvestorMasterBO siBO WHERE siBO.investorName = :investorName AND siBO.investorPAN = :investorPAN AND siBO.advisoruser.id = :advisorID")
	public List<Integer> getIdByAdvisorIdAndNameAndPAN(@Param("investorName") String investorName, @Param("investorPAN") String investorPAN, @Param("advisorID") int advisorID);

	public List<StagingInvestorMasterBO> findDistinctByInvestorPAN(@Param("investorPAN") String investorPAN);
	
	public List<StagingInvestorMasterBO> findByAdvisoruser(AdvisorUser advUser);
	
	public List<StagingInvestorMasterBO> findByAdvisoruserAndCreatedOn(AdvisorUser advUser,String createdOn);
	
	public StagingInvestorMasterBO findByInvestorName(String investorName);
	
	public StagingInvestorMasterBO findByIdAndInvestorNameAndAdvisoruser(int id, String investorName, AdvisorUser advUser);
}
