package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupRTAFileName;


public interface LookupRTAFileNameRepository extends JpaRepository<LookupRTAFileName, Integer> {
	
	public LookupRTAFileName findByName(String rtaFileName);
}
