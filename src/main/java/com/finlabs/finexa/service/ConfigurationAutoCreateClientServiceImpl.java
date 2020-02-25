package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.dto.AdvisorUserDTO;
import com.finlabs.finexa.dto.BranchMasterDetailsBODTO;
import com.finlabs.finexa.dto.ConfigurationAutoCreateClientDTO;
import com.finlabs.finexa.dto.RmMasterBODTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserRoleMapping;
import com.finlabs.finexa.model.AdvisorUserSupervisorMapping;
import com.finlabs.finexa.model.BranchMasterDetailsBO;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.LookupTransactBSEAccessMode;
import com.finlabs.finexa.model.RmMasterBO;
import com.finlabs.finexa.model.User;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.AdvisorRoleRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserRoleMappingRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.BranchMasterDetailsBORepository;
import com.finlabs.finexa.repository.LookupCountryRepository;
import com.finlabs.finexa.repository.LookupTransactBSEAccessModeRepository;
import com.finlabs.finexa.repository.RmMasterRepositoryBO;
import com.finlabs.finexa.repository.UserRepository;

@Service("ConfigurationAutoClientCreateService")
@Transactional
public class ConfigurationAutoCreateClientServiceImpl implements ConfigurationAutoClientCreateService {
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	@Autowired
	AdvisorMasterRepository advisorMasterRepository;
	
	
	@Override
	@Transactional(rollbackOn = CustomFinexaException.class)
	public ConfigurationAutoCreateClientDTO update(ConfigurationAutoCreateClientDTO configurationAutoCreateClientDTO) {
		
		AdvisorUser advisorUserDetails=advisorUserRepository.findById(configurationAutoCreateClientDTO.getAdvisorId());
		AdvisorMaster advisorMasterDetails=advisorMasterRepository.findById(advisorUserDetails.getAdvisorMaster().getId());
		AdvisorMaster  advisorMaster=new AdvisorMaster();
		advisorMaster.setId(advisorMasterDetails.getId());
		if(configurationAutoCreateClientDTO.getAutoCreateClient().equals("Yes")) {
			advisorMaster.setAutoCreateClient("Y");
		}
		else
			advisorMaster.setAutoCreateClient("N");
		
	
		return configurationAutoCreateClientDTO;
	}
	
}
