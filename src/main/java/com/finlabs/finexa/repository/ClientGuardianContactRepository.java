package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.finlabs.finexa.model.ClientGuardianContact;

public interface ClientGuardianContactRepository extends JpaRepository<ClientGuardianContact, Integer> {
	//List<ClientGuardianContact> findByClientGuardianGuardianID(int guardianId);
}
