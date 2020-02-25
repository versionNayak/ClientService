package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.FinexaImpactAnalysisDTO;

public interface FinexaImpactAnalysisService {
	
	public FinexaImpactAnalysisDTO save(FinexaImpactAnalysisDTO finexaImpactAnalysisDTO);
	
	public FinexaImpactAnalysisDTO update(FinexaImpactAnalysisDTO finexaImpactAnalysisDTO);
	
	public FinexaImpactAnalysisDTO findById(int id);
	
	public List<FinexaImpactAnalysisDTO> findAll();
	
	public int delete(int id);
	
}
