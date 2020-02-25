package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.MasterCash;

public interface MasterCashRepository extends JpaRepository<MasterCash, Integer>{

	//List<MasterCash> findByNameIgnoreCaseContaining(String searchString);
	
}
