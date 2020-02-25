package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.FinexaExceptionHandlingDTO;

public interface FinexaExceptionHandlingService {
	
	public FinexaExceptionHandlingDTO save(FinexaExceptionHandlingDTO finexaExceptionHandlingDTO);
	
	public FinexaExceptionHandlingDTO update(FinexaExceptionHandlingDTO finexaExceptionHandlingDTO);
	
	public FinexaExceptionHandlingDTO findById(int id);
	
	public List<FinexaExceptionHandlingDTO> findAll();
	
	public int delete(int id);
}
