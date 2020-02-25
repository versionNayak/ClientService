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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.FinexaExceptionHandlingDTO;
import com.finlabs.finexa.service.FinexaExceptionHandlingService;

@RestController
public class FinexaExceptionHandlingController {

	private static Logger log = LoggerFactory.getLogger(FinexaExceptionHandlingController.class);
	
	@Autowired
	FinexaExceptionHandlingService finexaExceptionHandlingService;
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value="/createFinexaExceptionHandling", method=RequestMethod.POST)
	public ResponseEntity<?> createFinexaExceptionHandling(@RequestBody FinexaExceptionHandlingDTO finexaExceptionHandlingDTO){
		
		finexaExceptionHandlingDTO = finexaExceptionHandlingService.save(finexaExceptionHandlingDTO);
		return new ResponseEntity<FinexaExceptionHandlingDTO>(finexaExceptionHandlingDTO, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value="/editFinexaExceptionHandling", method=RequestMethod.POST)
	public ResponseEntity<?> editFinexaExceptionHandling(@RequestBody FinexaExceptionHandlingDTO finexaExceptionHandlingDTO){
		
		finexaExceptionHandlingDTO = finexaExceptionHandlingService.update(finexaExceptionHandlingDTO);
		return new ResponseEntity<FinexaExceptionHandlingDTO>(finexaExceptionHandlingDTO, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value="/finexaExceptionHandling", method=RequestMethod.GET)
	public ResponseEntity<?> findById(@RequestParam int id){
		
		FinexaExceptionHandlingDTO finexaExceptionHandlingDTO = finexaExceptionHandlingService.findById(id);
		
		if (finexaExceptionHandlingDTO != null) {
			return new ResponseEntity<FinexaExceptionHandlingDTO>(finexaExceptionHandlingDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("No Error Code present in database", HttpStatus.OK);
		}
	}
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value="/finexaExceptionHandlingList", method=RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<FinexaExceptionHandlingDTO> finexaExceptionHandlingDTOList = finexaExceptionHandlingService.findAll();
		
		return new ResponseEntity<List<FinexaExceptionHandlingDTO>>(finexaExceptionHandlingDTOList, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@RequestMapping(value = "/finexaExceptionDelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) {
		
		int i = finexaExceptionHandlingService.delete(id);
		return new ResponseEntity<Integer>(i, HttpStatus.OK);
		
	}
	
	
	
}
