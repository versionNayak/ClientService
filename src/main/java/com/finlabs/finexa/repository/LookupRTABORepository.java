package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupRTABO;

public interface LookupRTABORepository extends JpaRepository<LookupRTABO, Integer> {
	
	public LookupRTABO findByName(String rtaName);
	
	

}
