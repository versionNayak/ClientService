package com.finlabs.finexa.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.FamilyAttributeMasterDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.FamilyAttributeMasterService;

@RestController
public class FamilyAttributeMasterController {
	
	@Autowired
	FamilyAttributeMasterService familyAttributeMasterService;
	
	@PreAuthorize("hasAnyRole('Admin','MFBacKOfficeAddEdit')")
	@RequestMapping(value="/createFamilyAttribute", method=RequestMethod.POST)
	public ResponseEntity<?> createFamilyAttribute(@RequestBody FamilyAttributeMasterDTO familyAttributeMasterDTO) throws CustomFinexaException, FinexaBussinessException {
		
		try {
			familyAttributeMasterDTO = familyAttributeMasterService.save(familyAttributeMasterDTO);
			return new ResponseEntity<FamilyAttributeMasterDTO>(familyAttributeMasterDTO , HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-FAM01", "Failed to save Family Attribute Master details.", e);
		}
		
	}

}
