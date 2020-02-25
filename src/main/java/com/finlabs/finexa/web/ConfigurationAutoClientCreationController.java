package com.finlabs.finexa.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ConfigurationAutoCreateClientDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.ConfigurationAutoClientCreateService;

@RestController
public class ConfigurationAutoClientCreationController {
	
	@Autowired
	ConfigurationAutoClientCreateService configurationAutoClientCreateService;
   
	@PreAuthorize("hasRole('MFBacKOfficeAddEdit')")
	@RequestMapping(value = "/saveAutoClientOption", method = RequestMethod.POST)
	public ResponseEntity<?> saveAutoClientOption(@Valid @RequestBody ConfigurationAutoCreateClientDTO configurationAutoCreateClientDTO) throws FinexaBussinessException, CustomFinexaException {
		try {
			configurationAutoCreateClientDTO = configurationAutoClientCreateService.update(configurationAutoCreateClientDTO);
			return new ResponseEntity<ConfigurationAutoCreateClientDTO>(configurationAutoCreateClientDTO, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("","","", rexp);
		}
	}
}