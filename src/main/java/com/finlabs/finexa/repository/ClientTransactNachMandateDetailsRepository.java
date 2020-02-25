package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientTransactMandateDetail;

public interface ClientTransactNachMandateDetailsRepository extends JpaRepository<ClientTransactMandateDetail, Integer> {
	
	public ClientTransactMandateDetail getByClientCodeAndSaveMode(String clientCode, String saveMode);
}