package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.dto.AdvisorUserDTO;
import com.finlabs.finexa.dto.BranchMasterDetailsBODTO;
import com.finlabs.finexa.dto.ConfigurationAutoCreateClientDTO;
import com.finlabs.finexa.dto.RmMasterBODTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;

public interface ConfigurationAutoClientCreateService {
	
	ConfigurationAutoCreateClientDTO update (ConfigurationAutoCreateClientDTO configurationAutoCreateClientDTO) throws RuntimeException, CustomFinexaException;

	
	
		
}
