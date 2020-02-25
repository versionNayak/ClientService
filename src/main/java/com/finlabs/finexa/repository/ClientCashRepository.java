package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientCash;


public interface ClientCashRepository extends JpaRepository<ClientCash, Integer> {

//	List<ClientCash> findByClientMasterClientId(long clientId);
	
}
