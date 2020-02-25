package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.finlabs.finexa.model.LookupGoalType;

public interface GoalTypeRepository extends JpaRepository<LookupGoalType, Byte>{
	  
}