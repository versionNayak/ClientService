package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupResidentType;

public interface ResidentTypeRepository extends JpaRepository<LookupResidentType, Byte>{
	
	LookupResidentType findById(byte id);
	LookupResidentType findByDescription(String description);  

	

}
