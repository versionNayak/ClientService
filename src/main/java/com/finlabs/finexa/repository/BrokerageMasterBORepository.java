package com.finlabs.finexa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.BrokerageMasterBO;
import com.finlabs.finexa.model.TransactionMasterBO;

public interface BrokerageMasterBORepository extends JpaRepository<BrokerageMasterBO, String> {
	/*
	@Query("SELECT distinct bmbo.id, bmbo.transactionNumber, bmbo.transactionDate, bmbo.processDate FROM BrokerageMasterBO bmbo WHERE  advisorUser.id = :advisorId group by bmbo.id, bmbo.transactionNumber, bmbo.transactionDate, bmbo.processDate")
	public List<Object []> getdistinctTransNumberTransDateAndProcessDateSet (@Param ("advisorId") int advisorId); 
	
	@Query("SELECT bmbo.id FROM BrokerageMasterBO bmbo WHERE bmbo.transactionNumber = :trxnNo AND bmbo.transactionDate = :trxnDate AND bmbo.processDate = :processDate AND advisorUser.id = :advisorId")
	public int getId (@Param ("trxnDate") Date trxnDate, @Param ("processDate") Date processDate, @Param ("trxnNo") String trxnNo, @Param ("advisorId") int advisorId);
	*/
	@Query("SELECT distinct bkbo.folioNumber, bkbo.schemeRTACode FROM BrokerageMasterBO bkbo WHERE bkbo.investorName like :investorName AND bkbo.id.transactionDate between :fromDate AND :toDate GROUP BY bkbo.folioNumber, bkbo.schemeRTACode")
	public List<Object[]> getDistinctFolioSchemeRTASetByInvestorNameAndTransactionDate (@Param("investorName") String investorName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query("SELECT bkbo FROM BrokerageMasterBO bkbo WHERE bkbo.folioNumber = :folioNumber AND bkbo.schemeRTACode = :rtaCode AND bkbo.investorName like :investorName AND bkbo.id.transactionDate between :fromDate AND :toDate ORDER BY bkbo.id.transactionDate")
	public List<BrokerageMasterBO> findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeRTA (@Param("investorName") String investorName, @Param("folioNumber") String folioNo, @Param("rtaCode") String schemeName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query("SELECT distinct bkbo.folioNumber FROM BrokerageMasterBO bkbo WHERE bkbo.investorName like :investorName AND bkbo.id.transactionDate between :fromDate AND :toDate")
	public List<String> getDistinctFolioSetByInvestorNameAndTransactionDate (@Param("investorName") String investorName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
}
