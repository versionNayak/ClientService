package com.finlabs.finexa.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.MasterGoalInflationRate;

public interface MasterGoalInflationRateRepository extends JpaRepository<MasterGoalInflationRate, Integer>{
	
	@Query("SELECT mgir FROM MasterGoalInflationRate mgir WHERE mgir.lookupGoalType.id = :goalType AND :date BETWEEN mgir.fromDate AND mgir.toDate")
	MasterGoalInflationRate findByGoalType(@Param("goalType") byte goalType, @Param("date") Date date);
}
