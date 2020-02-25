package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.AccessRightDTO;
import com.finlabs.finexa.model.AdvisorRoleSubmoduleMapping;

public interface AccessRightService {

	List<AdvisorRoleSubmoduleMapping> findAccessRights(int roleId, int userId);
	
	AccessRightDTO save(AccessRightDTO accessRightDTO);
}
