package com.finlabs.finexa.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.ClientGoal;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupGoalType;


public interface ClientGoalRepository extends JpaRepository<ClientGoal, Integer> {
	//@Query(value="select description from clientGoal WHERE  goalType=8 and description ='Retirement Goal' and clientid=?1 limit 1",nativeQuery=true)
   // @Query(value="select count(*) from clientGoal WHERE clientid=?1 and goalType = 8",nativeQuery=true)
	//  public int getRetirementGoal(int clientID);
	
	public Long countByClientMasterAndLookupGoalType(ClientMaster clientMaster, LookupGoalType lookupGoalType);
	 
	
//	@Query(value ="SELECT * FROM clientLifeInsurance WHERE endDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY) AND clientID=?1 ORDER BY lockedUptoDate", nativeQuery = true)
//	 public List<ClientLifeInsurance> getLockedUptoDateForNext1Week(int clientID);
//	
//	 @Query(value ="SELECT * FROM clientGoal WHERE startMonthYear BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY) AND clientID=?1", nativeQuery = true)
//	 public List<ClientGoal> getUpcomingGoalsForNext1Week(int clientID);
//	 
//	
//	 @Query(value ="SELECT * FROM clientGoal WHERE startMonthYear BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 1 MONTH) AND clientID=?1", nativeQuery = true)
//	 public List<ClientGoal> getUpcomingGoalsNext1Month(int clientID);
//	 
//
//	 @Query(value ="SELECT * FROM clientGoal WHERE startMonthYear BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 3 MONTH) AND clientID=?1", nativeQuery = true)
//	 public List<ClientGoal> getUpcomingGoalsForNext3Month(int clientID);
//	 
//	
//	 @Query(value ="SELECT * FROM clientGoal WHERE startMonthYear BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 6 MONTH) AND clientID=?1", nativeQuery = true)
//	 public List<ClientGoal> getUpcomingGoalsForNext6Month(int clientID);
//	 
//	 
//	 @Query(value ="SELECT * FROM clientGoal WHERE startMonthYear BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 1 YEAR) AND clientID=?1", nativeQuery = true)
//	 public List<ClientGoal> getUpcomingGoalsForNext1YEAR(int clientID);
//	 
//	 @Query(value ="SELECT * FROM clientGoal WHERE startMonthYear BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 2 WEEK) AND clientID=?1", nativeQuery = true)
//	 public List<ClientGoal> getUpcomingGoalsForNext1Fortnight(int clientID);
	 
	 
	@Procedure(procedureName = "procGoalPlanningGetLumpsumInvestment")
	public BigDecimal getLumpsumInvestmentForGoalPlanning(@Param("inClientId") Integer inClientId, @Param("inGoalId") Integer inGoalId, @Param("inGlideNonGlideFlag") String inGlideNonGlideFlag);

	@Procedure(procedureName = "procGoalPlanningGetSipInvestment")
	public BigDecimal getSipInvestmentForGoalPlanning(@Param("inClientId") Integer inClientId, @Param("inGoalId") Integer inGoalId, @Param("inGlideNonGlideFlag") String inGlideNonGlideFlag);

	@Procedure(procedureName = "procGoalPlanningCorpusReqdAtGoalStartOutput")
	public BigDecimal getCorpusReqdAtGoalStartOutput(@Param("inClientId") Integer inClientId, @Param("inGoalId") Integer inGoalId);

	//@Query(value="SELECT * FROM clientGoal WHERE clientID = ?1 AND goalType = ?2", nativeQuery = true)
	//public List<ClientGoal> getRetirementGoalForClient(int clientID, int goalType);	
	
	public List<ClientGoal> findByClientMasterAndLookupGoalType(ClientMaster clientMaster, LookupGoalType lookupGoalType);

	@Modifying
	@Query("UPDATE ClientGoal g SET g.postRetirementAnnualExpense = :expense WHERE g.clientMaster.id = :masterId AND g.lookupGoalType.id = :goalTypeId")
	void updateRetirementGoalExpense(@Param("goalTypeId") Byte goalTypeId, @Param("masterId") int masterId, @Param("expense") Double expense);
	
	@Modifying
	@Query("UPDATE ClientGoal g SET g.startMonthYear = :startMonthYear WHERE g.clientMaster.id = :masterId AND g.lookupGoalType.id = 8")
	void updateGoalStartMonthYear(@Param("startMonthYear") String startMonthYear, @Param("masterId") int masterId);

	//public List<ClientGoal> findByClientMasterAndStartMonthYear(ClientMaster clientMaster, String date);
	public List<ClientGoal> findByClientMasterAndStartMonthYearBefore(ClientMaster clientMaster, String date);
	
}
