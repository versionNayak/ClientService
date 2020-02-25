package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.ClientExpense;

import java.util.List;

public interface ClientExpenseRepository extends JpaRepository<ClientExpense, Integer> {
	@Modifying
	@Query(value="delete from ClientExpense cfi where cfi.clientMaster.id=?1 and cfi.expenseType!=?2")
	public void deleteIndividiualExpense(Integer clientID,Byte inIncomeType);
	
	/*@Modifying
	@Query(value="delete from ClientExpense cfi where cfi.clientMaster.id=?1")
	public int deleteClientExpense(Integer clientID);*/

	@Modifying
	@Query(value="delete from ClientExpense cfi where cfi.clientMaster.id=?1 and cfi.expenseType=?2")
	public void deleteTotalExpense(Integer clientID,Byte inIncomeType);

	@Query("SELECT exp.lookupIncomeExpenseDuration.description FROM ClientExpense exp where exp.clientMaster.id = :clientID")
	public List<Integer> fetchExpenseTypeByClientID (@Param("clientID") Integer clientID);

}
