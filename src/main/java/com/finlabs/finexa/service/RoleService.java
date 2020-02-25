package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.RoleDTO;
import com.finlabs.finexa.dto.UserDTO;

public interface RoleService {
	List<RoleDTO> getAllRoles();
	RoleDTO save(RoleDTO roleDTO);
	RoleDTO update(RoleDTO roleDTO);
	int delete(long id);
	RoleDTO findById(long id);
	UserDTO findUserByAdvisorId(int id);
}
