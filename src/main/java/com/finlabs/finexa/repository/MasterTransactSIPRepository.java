package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterTransactBSEMFPhysicalScheme;
import com.finlabs.finexa.model.MasterTransactSIP;

public interface MasterTransactSIPRepository extends JpaRepository<MasterTransactSIP, Integer> {
	
	/*
	 * public List<MasterTransactSIP> findBySchemeCodeAndSipFrequency(String schemeCode, String sipFrequency);
	 * 
	 * */
	
	public MasterTransactSIP findBySchemeCodeAndSipFrequency(String schemeCode, String sipFrequency);
	
}
