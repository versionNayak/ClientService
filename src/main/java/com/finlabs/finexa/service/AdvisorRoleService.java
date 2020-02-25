package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.AdvisorRoleDTO;


public interface AdvisorRoleService {
	AdvisorRoleDTO save(AdvisorRoleDTO advisorRoleDTO);
	List<AdvisorRoleDTO> findRoles(Integer userId);
	

}
