package com.finlabs.finexa.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ClientRecordsController {
	@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	@RequestMapping(value = "/clientRecord/orgs/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> getOrgs(@PathVariable int advisorId){
		List<String> orgs = new ArrayList<String>();
	
		
		return new ResponseEntity<List<String>>(orgs , HttpStatus.OK);
		
	}    
	
	
}
