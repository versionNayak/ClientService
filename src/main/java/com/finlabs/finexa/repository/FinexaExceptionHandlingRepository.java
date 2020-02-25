package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;

public interface FinexaExceptionHandlingRepository extends JpaRepository<FinexaExceptionHandling, Integer>{
	
	//FinexaExceptionHandling findByModuleNameAndErrorCode(String moduleName, String errorCode);
	FinexaExceptionHandling findByFinexaBusinessSubmoduleAndErrorCode(FinexaBusinessSubmodule subModuleID, String errorCode);
	
}
