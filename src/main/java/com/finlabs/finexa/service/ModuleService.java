package com.finlabs.finexa.service;

import com.finlabs.finexa.dto.ModuleDTO;

public interface ModuleService {
	ModuleDTO save(ModuleDTO moduleDTO);
	ModuleDTO update(ModuleDTO moduleDTO);
	int delete(long id);
	ModuleDTO findById(long id);
}
