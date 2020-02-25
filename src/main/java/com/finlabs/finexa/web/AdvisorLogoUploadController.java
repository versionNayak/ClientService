package com.finlabs.finexa.web;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.finlabs.finexa.service.AdvisorService;

@RestController
public class AdvisorLogoUploadController {

	private final Logger logger = LoggerFactory.getLogger(AdvisorLogoUploadController.class);

	@Autowired
	AdvisorService advisorService;

	// Save the uploaded file to this folder
	public static final String GENERATED_PDF = System.getProperty("java.io.tmpdir") + File.separator
			+ "Certificate.pdf";
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/logo/upload", method = RequestMethod.POST)
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile, @RequestParam("id") int id) {

		logger.debug("Single file upload!");

		if (uploadfile.isEmpty()) {
			return new ResponseEntity("please select a file!", HttpStatus.OK);
		}

		try {

			advisorService.saveAdvisorUserLogo(uploadfile, id);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity("Successfully uploaded advisor logo ", new HttpHeaders(), HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','ClientInfo','Client')")
	@RequestMapping(value = "/getadvisor/logo/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> uploadFile(@PathVariable("id") int id) {

		logger.debug("Single file upload!");

		String logoString = "";

		try {

			byte[] logo = advisorService.getAdvisorLogo(id);
			// logoString = new String(logo);
			if (logo != null) {
				logoString = Base64.getEncoder().encodeToString(logo);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity(logoString, new HttpHeaders(), HttpStatus.OK);

	}

}
