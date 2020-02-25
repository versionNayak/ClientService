package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finlabs.finexa.model.LookupRole;

public interface LookupRoleRepository extends JpaRepository<LookupRole, Integer>{

	LookupRole findByDescription(String supervisorRole);

	LookupRole findById(byte roleId);

	LookupRole findBySupervisorID(LookupRole supervisorID);
	
}
