package com.finlabs.finexa.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.finlabs.finexa.dto.RoleDTO;
import com.finlabs.finexa.dto.UserDTO;
import com.finlabs.finexa.service.RoleService;

@RestController
public class RoleController {
	
	private static Logger log = LoggerFactory.getLogger(RoleController.class);
	
	
	@Autowired
	RoleService roleService;
	
	@PreAuthorize("hasAnyRole('Admin','UserManagementView','AdvisorAdmin')")
	@RequestMapping(value="/roles", method=RequestMethod.GET)
	public ResponseEntity<List<RoleDTO>> getAllRoles(){
		
		List<RoleDTO> rolesList = roleService.getAllRoles();
		
		return new ResponseEntity<List<RoleDTO>>(rolesList , HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','UserManagementAddEdit','AdvisorAdmin')")
	@RequestMapping(value="/userRole", method=RequestMethod.POST)
	public ResponseEntity<?> createRole(@RequestBody RoleDTO roleDTO){
		RoleDTO retDTO = roleService.save(roleDTO);
		
		return new ResponseEntity<RoleDTO>(retDTO , HttpStatus.OK);
		
	}
	
	
	@PreAuthorize("hasAnyRole('Admin','UserManagementView','AdvisorAdmin','ClientInfoView')")
	@RequestMapping(value="/user/{userId}", method=RequestMethod.GET)
	public ResponseEntity<?> findUser(@PathVariable Integer userId){
		UserDTO userDTO = roleService.findUserByAdvisorId(userId);
		//if (listDTOs.isEmpty()) throw new No();
		return new ResponseEntity<UserDTO>(userDTO , HttpStatus.OK);
	}
}
