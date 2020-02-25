package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientAccessRight;
import com.finlabs.finexa.model.ClientMaster;

public interface ClientAccessRightsRepository extends JpaRepository<ClientAccessRight, Integer> {

	public ClientAccessRight findByClientMaster(ClientMaster clientMaster);
}
