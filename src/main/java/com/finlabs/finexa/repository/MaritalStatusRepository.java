package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupMaritalStatus;

public interface MaritalStatusRepository extends JpaRepository<LookupMaritalStatus, Byte>{
	LookupMaritalStatus findById(byte id);
	LookupMaritalStatus findByDescription(String description);
	
}
