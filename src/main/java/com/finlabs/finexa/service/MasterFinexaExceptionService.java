package com.finlabs.finexa.service;

import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.MasterFinexaExceptionDTO;

public interface MasterFinexaExceptionService {

	public Map<String, String> findAllExceptions();

	public MasterFinexaExceptionDTO save(MasterFinexaExceptionDTO masterFinexaExceptionDTO);

	public MasterFinexaExceptionDTO update(MasterFinexaExceptionDTO masterFinexaExceptionDTO);
	
	public MasterFinexaExceptionDTO findById(int id);
	
	public List<MasterFinexaExceptionDTO> findAll();
	
	public int delete(int id);
	
}