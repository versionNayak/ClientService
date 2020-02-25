package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.MasterTransactBSEMFPhysicalScheme;
import com.finlabs.finexa.model.MasterTransactBSEMFPhysicalSchemeLive;

public interface MasterTransactBSEMFPhysicalRepository extends JpaRepository<MasterTransactBSEMFPhysicalScheme, Integer> {
	
	  @Query("SELECT DISTINCT mt.schemeType FROM MasterTransactBSEMFPhysicalScheme mt") 
	  public List<String> getAllBSEDPhysicalSchemeTypes();
	 
	
	
	  @Query("SELECT DISTINCT mt.amcCode FROM MasterTransactBSEMFPhysicalScheme mt ORDER BY mt.amcCode ASC") 
	   public List<String> getAllAmcs();
	 
	//public List<MasterTransactBSEMFPhysicalScheme> findDistinctAmcCodeByOrderByAmcCodeAsc();
	
	
	/*public List<MasterTransactBSEMFPhysicalScheme> findByAmcCodeAndSchemeTypeAndPurchaseAllowedAndSchemePlanAndPurchaseTransactionModeOrPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String purchaseAllowedFlag, String schemePlan, String transactionModeOption1, String transactionModeOption2);
*/	
	
	/******************** Tried to fetch the scheme Type in Ascending Order ********************/
	
	//public List<String> findSchemeTypeDistinctBySchemeTypeOrderBySchemeTypeAsc();
	
	@Query("SELECT DISTINCT mt.schemeType FROM MasterTransactBSEMFPhysicalScheme mt ORDER BY mt.schemeType ASC")
	public List<String> getDistinctSchemeType();
	//public List<MasterTransactBSEMFPhysicalScheme>findDistinctSchemeTypeOrderBySchemeType();
	//public List<MasterTransactBSEMFPhysicalScheme>findDistinctSchemeTypeBySchemeTypeOrderBySchemeTypeAsc();
	
	//public List<MasterTransactBSEMFPhysicalScheme>findDistinctSchemeType();
	/************************************* End *************************************/
	
	
	public List<MasterTransactBSEMFPhysicalScheme> findByAmcCodeAndSchemeTypeAndPurchaseAllowedAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndPurchaseAllowedAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String purchaseAllowedFlag, String schemePlan, String transactionModeOption1, String amcCode1, 
			String schemeType1, String purchaseAllowedFlag1, String schemePlan1, String transactionModeOption2);
	
	public List<MasterTransactBSEMFPhysicalScheme> findByAmcCodeAndSchemeTypeAndSipFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndSipFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String sipFlag,String schemePlan,String transactionModeOption1, String amcCode1, 
			String schemeType1, String sipFlag1,String schemePlan1,String transactionModeOption2);
	
	public List<MasterTransactBSEMFPhysicalScheme> findByAmcCodeAndSchemeTypeAndSwpFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndSwpFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String swpFlag, String schemePlan,String transactionModeOption1, String amcCode1, 
			String schemeType1, String swpFlag1, String schemePlan1,String transactionModeOption2);
	
	public List<MasterTransactBSEMFPhysicalScheme> findByAmcCodeAndSchemeTypeAndStpFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndStpFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String stpFlag, String schemePlan,String transactionModeOption1, String amcCode1, 
			String schemeType1, String stpFlag1, String schemePlan1, String transactionModeOption2);
	
	public List<MasterTransactBSEMFPhysicalScheme> findByAmcCodeAndSchemeTypeAndSwitchFlagAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndSwitchFlagAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String switchFlag,String schemePlan, String transactionModeOption1, String amcCode1, 
			String schemeType1, String switchFlag1,String schemePlan1,String transactionModeOption2);
	
	public List<MasterTransactBSEMFPhysicalScheme> findByIsin(String isin);
	
	public List<MasterTransactBSEMFPhysicalScheme> findByIsinAndPurchaseTransactionModeOrIsinAndPurchaseTransactionMode(String isin, 
			String purchaseTransactionModeOption1, String isin1, String purchaseTransactionModeOption2);
	
	
	public List<MasterTransactBSEMFPhysicalScheme> findByAmcCodeAndSchemeTypeAndRedemptionAllowedAndSchemePlanAndPurchaseTransactionModeOrAmcCodeAndSchemeTypeAndRedemptionAllowedAndSchemePlanAndPurchaseTransactionModeOrderBySchemeNameAsc(String amcCode, 
			String schemeType, String redeemAllowed, String schemePlan, String transactionModeOption1,String amcCode1, 
			String schemeType1, String redeemAllowed1, String schemePlan1, String transactionModeOption2);
	
	
	
	
}
