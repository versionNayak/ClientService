package com.finlabs.finexa.service;

import java.util.List;


import com.finlabs.finexa.dto.MasterTransactSipDTO;
import com.finlabs.finexa.dto.MasterTransactSipLiveDTO;
import com.finlabs.finexa.exception.CustomFinexaException;


public interface MasterTransactService {
	
	/*
	public List<MasterTransactSipDTO> getAllSIPRecords(String schemeCode,String sipFrequency) throws RuntimeException;
	*/
	
	public MasterTransactSipDTO getAllSIPRecords(String schemeCode,String sipFrequency) throws RuntimeException;
	
	public MasterTransactSipLiveDTO getAllSIPLiveRecords(String schemeCode,String sipFrequency) throws RuntimeException;
	
	
}
