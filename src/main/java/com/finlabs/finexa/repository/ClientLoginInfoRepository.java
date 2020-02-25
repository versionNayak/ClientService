package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoginInfo;
import com.finlabs.finexa.model.ClientMaster;



public interface ClientLoginInfoRepository extends JpaRepository<ClientLoginInfo, Integer> {

	ClientLoginInfo findByToken(String token);
	ClientLoginInfo findTopByClientMasterOrderByLoginTimeDesc(ClientMaster cm);
		
}


