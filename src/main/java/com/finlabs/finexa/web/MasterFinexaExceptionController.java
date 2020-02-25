package com.finlabs.finexa.web;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.MasterFinexaExceptionDTO;
import com.finlabs.finexa.service.MasterFinexaExceptionService;

@RestController
public class MasterFinexaExceptionController {
	
	private static Logger log = LoggerFactory.getLogger(MasterFinexaExceptionController.class);

	@Autowired
	MasterFinexaExceptionService masterFinexaExceptionService;
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value="/createMasterFinexaExceptions", method=RequestMethod.POST)
	public ResponseEntity<?> createMasterFinexaException(@RequestBody MasterFinexaExceptionDTO masterFinexaExceptionDTO) {
		
		masterFinexaExceptionDTO = masterFinexaExceptionService.save(masterFinexaExceptionDTO);
		
		return new ResponseEntity<MasterFinexaExceptionDTO>(masterFinexaExceptionDTO , HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value="/editMasterFinexaExceptions", method=RequestMethod.POST)
	public ResponseEntity<?> editMasterFinexaException(@RequestBody MasterFinexaExceptionDTO masterFinexaExceptionDTO) {
		masterFinexaExceptionDTO = masterFinexaExceptionService.update(masterFinexaExceptionDTO);
		
		return new ResponseEntity<MasterFinexaExceptionDTO>(masterFinexaExceptionDTO , HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value="/masterFinexaException", method=RequestMethod.GET)
	public ResponseEntity<?> findById(@RequestParam int id) {
		MasterFinexaExceptionDTO masterFinexaExceptionDTO = masterFinexaExceptionService.findById(id);
		
		if (masterFinexaExceptionDTO != null) {
			return new ResponseEntity<MasterFinexaExceptionDTO>(masterFinexaExceptionDTO , HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Data not found", HttpStatus.OK);
		}
	}
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value="/masterFinexaExceptionsDelete/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id){
		int i = masterFinexaExceptionService.delete(id);
		return new ResponseEntity<Integer>(i, HttpStatus.OK);
	}
	
	
}
