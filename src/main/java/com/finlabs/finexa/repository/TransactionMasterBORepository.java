package com.finlabs.finexa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.TransactionMasterBO;


public interface TransactionMasterBORepository extends JpaRepository<TransactionMasterBO, String>{
	
	@Query("SELECT tmbo FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.transactionDate between :fromDate AND :toDate AND tmbo.schemeName LIKE :scheme")
	public List<TransactionMasterBO> findFilteredTransactionDetails(@Param("invName") String invName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("scheme") String scheme);
	
	@Query("SELECT distinct(tmbo.folioNo) FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.transactionDate between :fromDate AND :toDate")
	public List<String> getDistinctFolioNoByInvestorNameAndTransactionDate(@Param("invName") String invName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query("SELECT distinct(tmbo.schemeName) FROM TransactionMasterBO tmbo where tmbo.folioNo = :folioNo AND tmbo.transactionDate between :fromDate AND :toDate")
	public List<String> getDistinctSchemeNameByFolioNoAndTransactionDate(@Param("folioNo") String folioNo, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query("SELECT distinct(tmbo.folioNo) FROM TransactionMasterBO tmbo where tmbo.investorName = :invName AND tmbo.investorPan = :invPAN")
	public List<String> getDistinctFolioNoByInvestorName(@Param("invName") String invName, @Param("invPAN") String invPAN);

	@Query("SELECT distinct(tmbo.folioNo) FROM TransactionMasterBO tmbo where tmbo.investorName = :invName AND tmbo.investorPan = :invPAN AND tmbo.transactionDate between :fromDate AND :toDate")
	public List<String> getDistinctFolioNoByInvestorNameFromAndToDate(@Param("invName") String invName, @Param("invPAN") String invPAN, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query("SELECT tmbo FROM TransactionMasterBO tmbo WHERE tmbo.folioNo = :folioNo AND tmbo.investorName = :invName AND tmbo.transactionDate between :fromDate AND :toDate ORDER BY tmbo.transactionDate")
	public List<TransactionMasterBO> findFilteredData(@Param("folioNo") String folioNo, @Param("invName") String invName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query("SELECT tmbo FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.transactionDate between :fromDate AND :toDate GROUP BY tmbo.folioNo, tmbo.transactionNumber")
	public List<TransactionMasterBO> findFilteredDataSecond(@Param("invName") String invName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
		
	@Query("SELECT distinct tmbo.schemeRTACode FROM TransactionMasterBO tmbo WHERE tmbo.folioNo = :folioNo AND  tmbo.investorName = :invName AND tmbo.investorPan = :invPAN AND tmbo.transactionDate between :fromDate AND :toDate")
	public List<String> getDistinctRTACodeByFolioNoAndDate (@Param("folioNo") String folioNo, @Param("invName") String invName, @Param("invPAN") String invPAN, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query("SELECT distinct tmbo.folioNo, tmbo.schemeRTACode FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.transactionDate between :fromDate AND :toDate GROUP BY tmbo.folioNo, tmbo.schemeRTACode")
	public List<Object[]> getDistinctFolioSchemeRTASetByInvestorNameAndTransactionDate (@Param("invName") String invName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query("SELECT distinct tmbo.folioNo, tmbo.schemeName FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.investorPan = :invPAN AND tmbo.transactionDate between :fromDate AND :toDate GROUP BY tmbo.folioNo, tmbo.schemeName")
	public List<Object[]> getDistinctFolioSchemeSetByInvestorNamePANAndTransactionDate (@Param("invName") String invName, @Param("invPAN") String invPAN, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query("SELECT distinct tmbo.folioNo, tmbo.schemeRTACode FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.investorPan = :invPAN AND tmbo.transactionDate <= :asOnDate GROUP BY tmbo.folioNo, tmbo.schemeRTACode")
	public List<Object[]> getDistinctFolioSchemeSetByInvestorNamePANAndAsOnTransactionDate (@Param("invName") String invName, @Param("invPAN") String invPAN, @Param("asOnDate") Date asOnDate);

	
	@Query("SELECT distinct tmbo.folioNo, tmbo.schemeRTACode FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.investorPan = :invPAN AND tmbo.transactionDate <= :asOnDate GROUP BY tmbo.folioNo, tmbo.schemeRTACode")
	public List<Object[]> getDistinctFolioSchemeSetByInvestorNamePANAndAsOnDate (@Param("invName") String invName, @Param("invPAN") String invPAN, @Param("asOnDate") Date asOnDate);
	
	@Query("SELECT tmbo FROM TransactionMasterBO tmbo WHERE tmbo.folioNo = :folioNo AND tmbo.schemeRTACode = :rtaCode AND tmbo.investorName = :invName AND tmbo.transactionDate between :fromDate AND :toDate ORDER BY tmbo.transactionDate")
	public List<TransactionMasterBO> findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeRTA (@Param("invName") String invName, @Param("folioNo") String folioNo, @Param("rtaCode") String schemeName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query("SELECT tmbo FROM TransactionMasterBO tmbo WHERE tmbo.folioNo = :folioNo AND tmbo.schemeRTACode = :rtaCode AND tmbo.investorName = :invName AND tmbo.investorPan = :invPAN AND tmbo.transactionDate <= :asOnDate ORDER BY tmbo.transactionDate")
	public List<TransactionMasterBO> findTransactiondetailsByInvestorNameAndAsOnDateAndFolioAndSchemeRTA (@Param("invName") String invName, @Param("invPAN") String invPAN, @Param("folioNo") String folioNo, @Param("rtaCode") String rtaCode, @Param("asOnDate") Date asOnDate);
	
	
	@Query("SELECT tmbo FROM TransactionMasterBO tmbo WHERE tmbo.folioNo = :folioNo AND tmbo.schemeRTACode = :schemeRTACode AND tmbo.investorName = :invName AND tmbo.investorPan = :invPAN AND tmbo.transactionDate < :fromDate ORDER BY tmbo.transactionDate")
	public List<TransactionMasterBO> getOpeningBalance (@Param("invName") String invName, @Param("folioNo") String folioNo, @Param("schemeRTACode") String schemeRTACode, @Param("invPAN") String invPAN, @Param("fromDate") Date fromDate);
	
	@Query("SELECT tmbo.transactionDate FROM TransactionMasterBO tmbo WHERE tmbo.folioNo = :folioNo AND tmbo.schemeRTACode = :schemeRTACode AND tmbo.investorName = :invName AND tmbo.investorPan = :invPAN ORDER BY tmbo.transactionDate")
	public List<Date> getFirstdateOfTransaction (@Param("invName") String invName, @Param("invPAN") String invPAN, @Param("folioNo") String folioNo, @Param("schemeRTACode") String schemeRTACode);
	
	
	@Query("SELECT tmbo FROM TransactionMasterBO tmbo WHERE tmbo.folioNo = :folioNo AND tmbo.schemeName = :schemeName AND tmbo.investorName = :invName AND tmbo.transactionDate between :fromDate AND :toDate AND tmbo.lookupTransactionRule.code = :transTypeCode ORDER BY tmbo.transactionDate")
	public List<TransactionMasterBO> findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeAndTransType (@Param("invName") String invName, @Param("folioNo") String folioNo, @Param("schemeName") String schemeName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("transTypeCode") String transTypeCode);
	 
	@Query("SELECT distinct tmbo.schemeName FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.transactionDate between :fromDate AND :toDate GROUP BY tmbo.schemeName")
	public List<String> getDistinctSchemeNameByInvestorAndFromToDate (@Param("invName") String invName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	//Returns distinct name and pan set of investors with inactive purchase for a specific period
	@Query("SELECT distinct  tmbo.investorName, tmbo.investorPan FROM TransactionMasterBO tmbo WHERE tmbo.lookupTransactionRule.code  IN ('Additional Purchase','Dividend Reinvestment','DIR','DR', 'ADDPUR', 'New Purchase', 'NEWPUR', 'P','REDR', 'SI','STPOR', 'SIP', 'STP In', 'STPI','SWIN','SWOFR', 'TI','TMI') AND  tmbo.advisorUser.id= :advisorID AND tmbo.transactionDate between :fromDate AND :toDate  GROUP BY tmbo.investorName, tmbo.investorPan")
	public List<Object[]> getDistinctNamePANSetByTransactionDateForInactivePurchase (@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,@Param("advisorID") int advisorID);
	
	//Returns distinct name and pan set of investors with inactive sale for a specific period
	@Query("SELECT distinct  tmbo.investorName, tmbo.investorPan FROM TransactionMasterBO tmbo WHERE tmbo.lookupTransactionRule.code IN ('R', 'RED', 'Redemption','ADDPURR','NEWPURR', 'SO','SIPR', 'STP O','STPIR','STPO','SWD','SWINR','SWOF','TMO','TO', 'STP Out', 'TO') AND tmbo.advisorUser.id= :advisorID AND tmbo.transactionDate between :fromDate AND :toDate GROUP BY tmbo.investorName, tmbo.investorPan")
	public List<Object[]> getDistinctNamePANSetByTransactionDateForInactiveSale (@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,@Param("advisorID") int advisorID);
	
	//Returns investors with inactive purchase for a specific period
	@Query("SELECT tmbo FROM TransactionMasterBO tmbo WHERE tmbo.lookupTransactionRule.code NOT IN ('Additional Purchase', 'ADDPUR', 'New Purchase', 'NEWPUR', 'P', 'SI', 'SIP', 'STP In', 'STPI', 'TI') AND tmbo.transactionDate between :fromDate AND :toDate ORDER BY tmbo.transactionDate")
	public List<TransactionMasterBO> getInvestorWithInactivePurchase (@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	//Returns investors with inactive sale for a specific period
	@Query("SELECT tmbo FROM TransactionMasterBO tmbo WHERE tmbo.lookupTransactionRule.code NOT IN ('R', 'RED', 'Redemption', 'SO', 'STP O', 'STP Out', 'TO') AND tmbo.transactionDate between :fromDate AND :toDate ORDER BY tmbo.transactionDate")
	public List<TransactionMasterBO> getInvestorWithInactiveSale (@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	/*@Query("SELECT DISTINCT tmbo.folioNo, tmbo.schemeName from TransactionMasterBO tmbo WHERE tmbo IN (SELECT tmbo.folioNo, tmbo.schemeName, tmbo.investorName, tmbo.investorPan FROM TransactionMasterBO tmbo WHERE investorName = :invName AND investorPan = :invPan AND tmbo.transactionDate between :fromDate AND :toDate ORDER BY tmbo.transactionDate)")
	public List<Object[]> getDistictFolioSchemeByInvestorNameAndPAN(@Param("invName") String invName,
			@Param("invPan") String invPan, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);*/

	@Query("SELECT distinct tmbo.folioNo, tmbo.schemeName FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.investorPan = :invPan AND tmbo.transactionDate between :fromDate AND :toDate GROUP BY tmbo.folioNo, tmbo.schemeName")
	public List<Object []> getDistictFolioSchemeByInvestorNameAndPAN (@Param("invName") String invName, @Param("invPan") String invPan, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
		
	//Returns starting date of transaction for a specific investor
	@Query("SELECT tmbo.transactionDate FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.investorPan = :invPAN ORDER BY tmbo.transactionDate")
	public List<Date> getFromDateByInvestorName (@Param("invName") String invName, @Param("invPAN") String invPAN);
	
	//Returns starting date of transaction for a specific investor
	@Query("SELECT tmbo.transactionDate FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.investorPan = :invPAN AND tmbo.folioNo = :folioNumber AND tmbo.schemeRTACode = :schemeRTACode ORDER BY tmbo.transactionDate")
	public List<Date> getFromDateByInvestorNamePANFolioScheme (@Param("invName") String invName, @Param("invPAN") String invPAN, @Param("folioNumber") String folioNumber, @Param("schemeRTACode") String schemeRTACode);
		
	//Returns starting date of transaction for a specific investor
		@Query("SELECT tmbo.transactionDate FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.investorPan = :invPAN  ORDER BY tmbo.transactionDate")
		public List<Date> getFromDateByInvestorNameAndPan (@Param("invName") String invName, @Param("invPAN") String invPAN);
		
	
	/*
	 * @Query("SELECT tmbo FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.schemeName = :schemename AND tmbo.transactionDate between :fromDate AND :toDate AND tmbo.lookupTransactionRule.code in (:transTypeCode1, :transTypeCode2)  ORDER BY tmbo.transactionDate"
	 * ) public List<TransactionMasterBO>
	 * findTransactionBasedOnTransCodeAndInvestorAndFromToDate (@Param("invName")
	 * String invName, @Param("schemeName") String schemeName, @Param("fromDate")
	 * Date fromDate, @Param("toDate") Date toDate, @Param("transTypeCode1") String
	 * transTypeCode1, @Param("transTypeCode2") String transTypeCode2);
	 *//*
	 * @Query("SELECT tmbo.transUnits FROM TransactionMasterBO tmbo WHERE tmbo.folioNo = :folioNo AND tmbo.schemeName = :schemeName AND tmbo.investorName = :invName AND tmbo.transactionDate = :toDate"
	 * ) public List<Object> getClosingBalance (@Param("invName") String
	 * invName, @Param("folioNo") String folioNo, @Param("schemeName") String
	 * schemeName, @Param("toDate") Date toDate);
	 */
		
		
		/***********************New Requirements******************/
		@Query("SELECT distinct(tmbo.schemeRTACode) FROM TransactionMasterBO tmbo where tmbo.advisorUser.id = :advisorId")
		public List<String> getDistinctRTACodeByAdvisor(@Param("advisorId") int advisorId);

		@Query("SELECT MIN(tmbo.transactionDate) FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.investorPan = :invPAN AND tmbo.folioNo = :folioNumber AND tmbo.schemeRTACode = :schemeRTACode")
		public Date findMinDateByInvestorNameAndFolioAndSchemeRTA(@Param("invName") String invName,@Param("invPAN") String invPAN,@Param("folioNumber") String folioNumber, @Param("schemeRTACode") String schemeRTACode);

		@Query("SELECT tmbo FROM TransactionMasterBO tmbo WHERE tmbo.folioNo = :folioNo AND tmbo.schemeRTACode = :schemeRTACode AND tmbo.investorName = :invName AND tmbo.transactionDate between :fromDate AND :toDate AND tmbo.lookupTransactionRule.code = :transTypeCode ORDER BY tmbo.transactionDate")
		public List<TransactionMasterBO> findTransactiondetailsByInvestorNameAndTransactionDateAndFolioAndSchemeAndLikeTransType (@Param("invName") String invName, @Param("folioNo") String folioNo,  @Param("schemeRTACode") String schemeRTACode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("transTypeCode") String transTypeCode);

		@Query("SELECT distinct tmbo.folioNo, tmbo.schemeName FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.investorPan = :invPAN AND tmbo.transactionDate <= :asOnDate ORDER BY tmbo.transactionDate")
		public List<Object[]> getDistinctFolioSchemeSetByAsOnDate (@Param("invName") String invName,  @Param("invPAN") String invPAN, @Param("asOnDate") Date asOnDate);

		@Query("SELECT distinct tmbo.folioNo, tmbo.schemeRTACode FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.investorPan = :invPAN AND tmbo.transactionDate <= :asOnDate GROUP BY tmbo.folioNo, tmbo.schemeRTACode")
		public List<Object[]> getDistinctFolioSchemeRTASetByInvestorNameAndAsOnDate(@Param("invName") String invName,@Param("invPAN") String invPAN, @Param("asOnDate") Date asOnDate);
		
		@Query("SELECT MIN(tmbo.transactionDate) FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.investorPan = :invPAN")
		public Date findMinDateByInvestorNameAndPAN(@Param("invName") String invName,@Param("invPAN") String invPAN);
		
		public List<TransactionMasterBO> findByInvestorNameAndInvestorPanOrderByTransactionDate(String name,String pan);
		
		@Query("SELECT distinct(tmbo.folioNo) FROM TransactionMasterBO tmbo where tmbo.investorName like :invName")
		public List<String> getDistinctFolioNoByInvestor(@Param("invName") String invName);

		@Query("SELECT distinct tmbo.schemeRTACode FROM TransactionMasterBO tmbo WHERE tmbo.folioNo = :folioNo AND tmbo.investorName = :invName")
		public List<String> getDistinctRTACodeByFolioNo (@Param("folioNo") String folioNo, @Param("invName") String invName);
		
		@Query("SELECT MIN(tmbo.transactionDate) FROM TransactionMasterBO tmbo WHERE tmbo.investorName = :invName AND tmbo.folioNo = :folioNumber AND tmbo.schemeRTACode = :schemeRTACode")
		public Date findMinDateByInvestorAndFolioAndSchemeRTA(@Param("invName") String invName, @Param("folioNumber") String folioNumber, @Param("schemeRTACode") String schemeRTACode);
	
		@Query("SELECT tmbo FROM TransactionMasterBO tmbo WHERE tmbo.advisorUser.id = :advId AND tmbo.transactionDate between :fromDate AND :toDate")
		public List<TransactionMasterBO> findByAdvisorIdAndStartDateAndEndDate(@Param("advId") int advId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
		
		@Query("SELECT distinct tmbo.investorName, tmbo.investorPan FROM TransactionMasterBO tmbo WHERE tmbo.advisorUser.id = :advisorId")
		public List<Object []> getDistinctInvestorNameAndInvestorPanByAdvisorId(@Param("advisorId") int advisorId);
	
		public List<TransactionMasterBO> findByAdvisorUserOrderByTransactionDate(AdvisorUser advisorUser);
		
}