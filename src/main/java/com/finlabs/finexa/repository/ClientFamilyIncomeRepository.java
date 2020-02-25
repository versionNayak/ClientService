package com.finlabs.finexa.repository;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorUserRoleMapping;
import com.finlabs.finexa.model.ClientFamilyIncome;
import com.finlabs.finexa.model.ClientFamilyMember;

public interface ClientFamilyIncomeRepository extends JpaRepository<ClientFamilyIncome, Integer> {
	
	/*@Query("select sum(cfi.incomeAmount) from ClientFamilyIncome cfi where cfi.clientFamilyMember.id=?1 group by cfi.clientFamilyMember.id")
	public double getFamilyMemberIncome(int memberId);*/
	
	
	/*@Query("select new ClientFamilyIncome(sum(cfi.incomeAmount) as incomeAmount,cfi.clientFamilyMember)from ClientFamilyIncome cfi where cfi.clientMaster.id=?1 group by cfi.clientFamilyMember order by cfi.clientFamilyMember.id") 
	public List<ClientFamilyIncome> getAllFamilyIncome(int inClientId);*/
	
	/*@Query("select sum(cfi.incomeAmount) from ClientFamilyIncome cfi where cfi.clientmaster.id=?1 group by cfi.clientFamilyMember.id")
	public BigDecimal getAllFamilyMemberIncome(int clientId);*/
	
	
	
	@Modifying
	@Query(value="delete from ClientFamilyIncome cfi where cfi.clientMaster.id=?1 and cfi.clientFamilyMember.id=?2 and cfi.incomeType!=?3")
	public void deleteIndividiualIncome(Integer clientID,Integer memberID,Byte inIncomeType);
	
	@Modifying
	@Query(value="delete from ClientFamilyIncome cfi where cfi.clientMaster.id=?1 and cfi.clientFamilyMember.id=?2")
	public void deleteIncomeForMember(Integer clientID,Integer memberID);

	@Modifying
	@Query(value="delete from ClientFamilyIncome cfi where cfi.clientMaster.id=?1 and cfi.clientFamilyMember.id=?2 and cfi.incomeType=?3")
	public void deleteTotalIncome(Integer clientID,Integer memberID,Byte inIncomeType);
	
	@Query("SELECT cfi FROM  ClientFamilyIncome cfi WHERE cfi.clientFamilyMember.id=:memberId AND "
			+ "cfi.incomeType=:incomeType")
	public ClientFamilyIncome checkIfExists(@Param("memberId") int memberId, @Param("incomeType") byte incomeType);

	@Query("SELECT fam.lookupIncomeExpenseDuration.description  FROM ClientFamilyIncome fam where fam.clientMaster.id = :clientID AND fam.clientFamilyMember.id = :clientFamilyMemberID")
	public List<Integer> fetchExpenseTypeByClientIDAndFamilyMemberID (@Param("clientID") Integer clientID, @Param("clientFamilyMemberID") Integer clientFamilyMemberID);

	@Query("SELECT fam.id FROM ClientFamilyMember fam where fam.clientMaster.id = :clientID AND fam.lookupRelation.id=0")
	public int fetchMemberID (@Param("clientID") Integer clientID);
	
	public ClientFamilyIncome findByClientFamilyMember(ClientFamilyMember clientFamilyMember);

}