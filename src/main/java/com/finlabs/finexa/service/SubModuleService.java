package com.finlabs.finexa.service;

import com.finlabs.finexa.dto.SubModuleDTO;

public interface SubModuleService {
	SubModuleDTO save(SubModuleDTO subModuleDTO);
	SubModuleDTO update(SubModuleDTO subModuleDTO);
	int delete(long id);
	SubModuleDTO findById(long id);
	
}
