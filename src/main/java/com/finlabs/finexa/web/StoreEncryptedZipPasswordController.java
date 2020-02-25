package com.finlabs.finexa.web;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.AdvisorService;

@RestController
public class StoreEncryptedZipPasswordController {

	private final Logger logger = LoggerFactory.getLogger(StoreEncryptedZipPasswordController.class);

	@Autowired
	AdvisorService advisorService;
	
	@RequestMapping(value = "/storePassword", method = RequestMethod.POST)
	public ResponseEntity<?> storePassword(@RequestBody AdvisorDTO advisorDTO, HttpServletRequest request)
			throws FinexaBussinessException {
		try {
			advisorDTO = advisorService.storePassword(advisorDTO, request);
			return new ResponseEntity<AdvisorDTO>(advisorDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("Masters",
					"", "Failed to store encrypted zip file password", e);
		}
	}
	
}
