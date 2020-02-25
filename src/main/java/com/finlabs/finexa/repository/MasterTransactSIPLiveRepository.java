package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterTransactBSEMFPhysicalScheme;
import com.finlabs.finexa.model.MasterTransactSIP;
import com.finlabs.finexa.model.MasterTransactSIPLive;

public interface MasterTransactSIPLiveRepository extends JpaRepository<MasterTransactSIPLive, Integer> {
	
	/*
	 * public List<MasterTransactSIP> findBySchemeCodeAndSipFrequency(String schemeCode, String sipFrequency);
	 * 
	 * */
	
	public MasterTransactSIPLive findBySchemeCodeAndSipFrequency(String schemeCode, String sipFrequency);
	
}
