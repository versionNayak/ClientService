package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.MasterTransactBSEMFPhysicalScheme;
import com.finlabs.finexa.model.MasterTransactBSEMFPhysicalSchemeLive;
import com.finlabs.finexa.util.MFTransactConstant;

public interface MasterTransactBSEMFPhysicalRepositoryLive extends JpaRepository<MasterTransactBSEMFPhysicalSchemeLive, Integer> {

	
	  @Query("SELECT DISTINCT mt.schemeType FROM MasterTransactBSEMFPhysicalSchemeLive mt") 
	   public List<String> getAllBSEDPhysicalSchemeTypes();
	 
	 
	//public List<MasterTransactBSEMFPhysicalSchemeLive>findDistinctBySchemeType();
	
	
	/*
	 * @Query(value =
	 * "SELECT distinct amcCode FROM masterTransactBSEMFPhysicalSchemeLive ORDER BY amcCode ASC"
	 * , nativeQuery = true) public List<String> getAllAmcs();
	 */
	  
	@Query("SELECT DISTINCT mt.amcCode FROM MasterTransactBSEMFPhysicalSchemeLive mt ORDER BY mt.amcCode ASC") 
	public List<String> getAllAmcs();
	//public List<MasterTransactBSEMFPhysicalSchemeLive>findDistinctAmcCodeByOrderByAmcCodeAsc();
	
	/******************** Tried to fetch the scheme Type in Ascending Order ********************/
	
	//public List<String> findSchemeTypeDistinctBySchemeTypeOrderBySchemeTypeAsc();
	
	//public List<MasterTransactBSEMFPhysicalScheme> getAllDistinctBySchemeTypeOrderBySchemeTypeAsc();
	
//	@Query("SELECT distinct schemeType FROM masterTransactBSEMFPhysicalScheme ORDER BY schemeType ASC")
//	public List<String> getDistinctSchemeType();
	@Query("SELECT DISTINCT mt.schemeType FROM MasterTransactBSEMFPhysicalSchemeLive mt ORDER BY mt.schemeType ASC")
	public List<String> getDistinctSchemeType();
	//public List<MasterTransactBSEMFPhysicalSchemeLive>findDistinctSchemeTypeByOrderBySchemeTypeAsc();
	
	/************************************* End *************************************/
	
	
	public List<MasterTransactBSEMFPhysicalSchemeLive> findByAmcCodeAndSchemeTypeAndPurchaseAllowedAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndPurchaseAllowedAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String purchaseAllowedFlag, String schemePlan, String transactionModeOption1, String amcCode1, 
			String schemeType1, String purchaseAllowedFlag1, String schemePlan1, String transactionModeOption2);
	
	public List<MasterTransactBSEMFPhysicalSchemeLive> findByAmcCodeAndSchemeTypeAndSipFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndSipFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String sipFlag,String schemePlan,String transactionModeOption1, String amcCode1, 
			String schemeType1, String sipFlag1,String schemePlan1,String transactionModeOption2);
	
	public List<MasterTransactBSEMFPhysicalSchemeLive> findByAmcCodeAndSchemeTypeAndSwpFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndSwpFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String swpFlag, String schemePlan,String transactionModeOption1, String amcCode1, 
			String schemeType1, String swpFlag1, String schemePlan1,String transactionModeOption2);
	
	public List<MasterTransactBSEMFPhysicalSchemeLive> findByAmcCodeAndSchemeTypeAndStpFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndStpFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String stpFlag, String schemePlan,String transactionModeOption1, String amcCode1, 
			String schemeType1, String stpFlag1, String schemePlan1, String transactionModeOption2);
	
	public List<MasterTransactBSEMFPhysicalSchemeLive> findByAmcCodeAndSchemeTypeAndSwitchFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndSwitchFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String switchFlag,String schemePlan, String transactionModeOption1, String amcCode1, 
			String schemeType1, String switchFlag1,String schemePlan1,String transactionModeOption2);
	
	public List<MasterTransactBSEMFPhysicalSchemeLive> findByIsin(String isin);
	
	public List<MasterTransactBSEMFPhysicalSchemeLive> findByIsinAndPurchaseTransactionModeOrIsinAndPurchaseTransactionMode(String isin, 
			String purchaseTransactionModeOption1, String isin1, String purchaseTransactionModeOption2);
	
	public List<MasterTransactBSEMFPhysicalSchemeLive> findByAmcCodeAndSchemeTypeAndRedemptionAllowedAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndRedemptionAllowedAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String redeemAllowed, String schemePlan, String transactionModeOption1,String amcCode1, 
			String schemeType1, String redeemAllowed1, String schemePlan1, String transactionModeOption2);
	
	
	
}
