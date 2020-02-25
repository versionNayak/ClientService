package com.finlabs.finexa.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AumMasterBO;

public interface AumMasterBORepository extends JpaRepository<AumMasterBO, String> {
	
	
	@Query("SELECT aum.id.schemertacode FROM AumMasterBO aum WHERE aum.id.schemertacode LIKE :amc")
	public List<String> getSchemeRTACodesContainingAMCCode(@Param("amc") String amc);
	
	@Query("SELECT aum FROM AumMasterBO aum WHERE aum.id.reportDate <= :reportDate AND aum.investorName = :invName order by aum.id.reportDate")
	public List<AumMasterBO> findAllByReportDateAndInvestorName(@Param("reportDate") Date reportDate, @Param("invName") String invName);

	@Query("SELECT aum FROM AumMasterBO aum WHERE aum.id.reportDate <= :reportDate AND aum.investorName = :invName and aum.id.schemertacode = :rtaCode order by aum.id.reportDate")
	public List<AumMasterBO> findAllByReportDateAndInvestorNameAndRTACode(@Param("reportDate") Date reportDate, @Param("invName") String invName, @Param("rtaCode") String rtaCode);

	@Query(value = "Select SUM(aum.currentValue) from AumMasterBO aum WHERE aum.id.reportDate <= :reportDate AND aum.investorName = :invName order by aum.id.reportDate")
	public String getTotalCurrentValue1(@Param("reportDate") Date reportDate, @Param("invName") String invName);
	
	@Query(value = "Select SUM(aum.currentValue) from AumMasterBO aum WHERE aum.id.reportDate <= :reportDate AND aum.investorName = :invName and aum.id.schemertacode = :rtaCode order by aum.id.reportDate")
	public String getTotalCurrentValue2(@Param("reportDate") Date reportDate, @Param("invName") String invName, @Param("rtaCode") String rtaCode);
	
	@Query("SELECT DISTINCT aum.id.schemertacode FROM AumMasterBO aum where aum.advisorUser.id= :advisorId")
	public List<String> findAllDistinctRTACode(@Param("advisorId") int advisorId);	

	@Query("SELECT aum FROM AumMasterBO aum WHERE aum.id.reportDate <= :reportDate AND aum.advisorUser.id= :advisorId order by aum.id.reportDate")
	public List<AumMasterBO> findAllByReportDateAndAdvisor(@Param("reportDate") Date reportDate, @Param("advisorId") int advisorId);

	@Query(value = "Select SUM(aum.currentValue) from AumMasterBO aum WHERE aum.id.reportDate <= :reportDate AND aum.advisorUser.id= :advisorId order by aum.id.reportDate")
	public String getTotalCurrentValueByAdvisor(@Param("reportDate") Date reportDate, @Param("advisorId") int advisorId);
}
