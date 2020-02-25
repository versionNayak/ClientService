package com.finlabs.finexa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AumMasterBO;
import com.finlabs.finexa.model.SIPSTPMasterBO;
import com.finlabs.finexa.model.TransactionMasterBO;

public interface SIPSTPMasterBORepository extends JpaRepository<SIPSTPMasterBO, String>{

	@Query("SELECT sipSTP FROM SIPSTPMasterBO sipSTP WHERE sipSTP.fromDate = :fromDate AND sipSTP.toDate = :toDate AND sipSTP.investorName = :invName AND sipSTP.investorPAN = :invPAN")
	public List<SIPSTPMasterBO> findAllByDateAndInvNameAndPAN(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("invName") String invName, @Param("invPAN") String invPAN);
	
	@Query("SELECT distinct sipSTP.folioNumber, sipSTP.schemeRTACode FROM SIPSTPMasterBO sipSTP WHERE sipSTP.investorName = :invName AND sipSTP.investorPAN = :invPAN AND sipSTP.transactionType = :transType AND sipSTP.registrationDate between :fromDate AND :toDate") 
	public List<Object[]> getDistinctFolioSchemeSetByInvestorNameAndRegistrationDate (@Param("invName") String invName, @Param("invPAN") String invPAN, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("transType") String transType);
		
	@Query("SELECT sipSTP FROM SIPSTPMasterBO sipSTP WHERE sipSTP.folioNumber = :folioNumber AND sipSTP.schemeRTACode = :schemeName AND sipSTP.investorName = :invName AND sipSTP.registrationDate between :fromDate AND :toDate")
	public List<SIPSTPMasterBO> findByNameFolioSchemeFromToDate(@Param("invName") String invName, @Param("folioNumber") String folioNumber, @Param("schemeName") String schemeName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query("SELECT distinct sipSTP.folioNumber FROM SIPSTPMasterBO sipSTP WHERE sipSTP.investorName = :invName AND sipSTP.investorPAN = :invPAN AND sipSTP.transactionType = :transType AND sipSTP.registrationDate between :fromDate AND :toDate") 
	public List<String> findDistinctFolioNumberByInvestorNameAndInvestorPANAndRegistrationDate(@Param("invName") String invName, @Param("invPAN") String invPAN, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("transType") String transType);
	
	@Query("SELECT distinct sipSTP.schemeRTACode FROM SIPSTPMasterBO sipSTP WHERE sipSTP.investorName = :invName AND sipSTP.investorPAN = :invPAN AND sipSTP.folioNumber = :folioNo AND sipSTP.transactionType = :transType AND sipSTP.registrationDate between :fromDate AND :toDate")
	public List<String> getDistinctRTACodeByFolioNoAndDate (@Param("folioNo") String folioNo, @Param("invName") String invName, @Param("invPAN") String invPAN, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("transType") String transType);
	
	@Query("SELECT distinct sipSTP.folioNumber, sipSTP.schemeRTACode FROM SIPSTPMasterBO sipSTP WHERE sipSTP.investorName = :invName AND sipSTP.investorPAN = :invPAN AND sipSTP.transactionType = :transType AND sipSTP.fromDate BETWEEN :fromDate AND :toDate") 
	public List<Object[]> getDistinctFolioSchemeSetByInvestorNameAndStartDate (@Param("invName") String invName, @Param("invPAN") String invPAN, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("transType") String transType);
	
	@Query("SELECT sipSTP FROM SIPSTPMasterBO sipSTP WHERE sipSTP.folioNumber = :folioNumber AND sipSTP.schemeRTACode = :schemeName AND sipSTP.investorName = :invName AND sipSTP.fromDate BETWEEN :fromDate AND :toDate")
	public List<SIPSTPMasterBO> findByNameFolioSchemeAndStartDate(@Param("invName") String invName, @Param("folioNumber") String folioNumber, @Param("schemeName") String schemeName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	
}
