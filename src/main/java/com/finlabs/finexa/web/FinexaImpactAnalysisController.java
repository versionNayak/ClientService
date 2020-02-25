package com.finlabs.finexa.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.FinexaExceptionHandlingDTO;
import com.finlabs.finexa.dto.FinexaImpactAnalysisDTO;
import com.finlabs.finexa.service.FinexaImpactAnalysisService;

@RestController
public class FinexaImpactAnalysisController {

	private static Logger log = LoggerFactory.getLogger(FinexaImpactAnalysisController.class);

	@Autowired
	FinexaImpactAnalysisService finexaImpactAnalysisService;

	@PreAuthorize("hasAnyRole('Admin')")
	@RequestMapping(value = "/createFinexaImpactAnalysis", method = RequestMethod.POST)
	public ResponseEntity<?> createFinexaImpactAnalysis(@RequestBody FinexaImpactAnalysisDTO finexaImpactAnalysisDTO) {

		finexaImpactAnalysisDTO = finexaImpactAnalysisService.save(finexaImpactAnalysisDTO);
		return new ResponseEntity<FinexaImpactAnalysisDTO>(finexaImpactAnalysisDTO, HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('Admin')")
	@RequestMapping(value = "/editFinexaImpactAnalysis", method = RequestMethod.POST)
	public ResponseEntity<?> editFinexaImpactAnalysis(@RequestBody FinexaImpactAnalysisDTO finexaImpactAnalysisDTO) {

		finexaImpactAnalysisDTO = finexaImpactAnalysisService.update(finexaImpactAnalysisDTO);
		return new ResponseEntity<FinexaImpactAnalysisDTO>(finexaImpactAnalysisDTO, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@RequestMapping(value = "/finexaImpactAnalysis", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@RequestParam int id) {

		FinexaImpactAnalysisDTO finexaImpactAnalysisDTO = finexaImpactAnalysisService.findById(id);

		if (finexaImpactAnalysisDTO != null) {
			return new ResponseEntity<FinexaImpactAnalysisDTO>(finexaImpactAnalysisDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("No Impact present in database", HttpStatus.OK);
		}
	}

	@PreAuthorize("hasAnyRole('Admin')")
	@RequestMapping(value = "/finexaImpactAnalysisList", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {

		List<FinexaImpactAnalysisDTO> finexaImpactAnalysisDTOList = finexaImpactAnalysisService.findAll();
		return new ResponseEntity<List<FinexaImpactAnalysisDTO>>(finexaImpactAnalysisDTOList, HttpStatus.OK);
	}

}
