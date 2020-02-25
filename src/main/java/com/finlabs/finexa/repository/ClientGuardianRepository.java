package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finlabs.finexa.model.ClientGuardian;

public interface ClientGuardianRepository extends JpaRepository<ClientGuardian, Integer> {
	//List<ClientGuardian> findByClientMasterClientId(long clientId);

}

