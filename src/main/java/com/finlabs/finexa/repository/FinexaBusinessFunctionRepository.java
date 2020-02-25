package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.FinexaBusinessFunction;

public interface FinexaBusinessFunctionRepository extends JpaRepository<FinexaBusinessFunction, Integer>{

	List<FinexaBusinessFunction> findByFunction(String function);
	List<FinexaBusinessFunction> findBySubFunction(String subFunction);
	List<FinexaBusinessFunction> findByEvent(String event);
	FinexaBusinessFunction findByFunctionAndEvent(String function, String event);
	FinexaBusinessFunction findByFunctionAndSubEvent(String function, String subEvent);
	
	
}
