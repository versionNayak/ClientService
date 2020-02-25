package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.UserSupervisorMappingDTO;

public interface UserSupervisorMapping {
	UserSupervisorMappingDTO save(UserSupervisorMappingDTO userSupervisorMappingDTO);
	List<UserSupervisorMappingDTO> findUserRoleMapping(int AdvisorId);
}
