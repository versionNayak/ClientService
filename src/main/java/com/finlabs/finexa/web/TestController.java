package com.finlabs.finexa.web;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.EmailService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class TestController {
	
	private static Logger log = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	EmailService emailService;

	
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public ResponseEntity<?> serverStatus(){
		return new ResponseEntity<String>("Status : Server is up and Ready to serve " , HttpStatus.OK);
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity<?> serverTest() throws FinexaBussinessException{
		
		int age = 60;
		int remainder = 0;
		
		int result;
		result = age / remainder;
		
	/*	
		String name = null;
		if (name == null) {
			throw new FinexaBussinessException(FinexaConstant.CLIENT_MUTUALFUND_MODULE, FinexaConstant.CLIENT_MUTUALFUND_ADD_CODE, "Failed to add Funds");
		}*/
		return new ResponseEntity<String>("Testing Exceoption", HttpStatus.OK);
	}
	@RequestMapping(value = "/testmail", method = RequestMethod.GET)
	public void sendEmailWithoutTemplating() {
		
		emailService.sendSimpleMessage("susanta.s@finlabsindia.com", "Hello this is Subject", "This is body of the Text");

	}
	

	
}
