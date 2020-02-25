package com.finlabs.finexa.web;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.MasterProductRecommendationDTO;
import com.finlabs.finexa.service.MasterMFProductRecoService;

@RestController
public class MasterMFProductRecoController {

	@Autowired
	MasterMFProductRecoService mastermfProductService;

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllProductRecommendations", method = RequestMethod.GET)
	public ResponseEntity<List<MasterProductRecommendationDTO>> getAllMasterMFProductRecommendation(
			@RequestParam int advisorId) {

		return new ResponseEntity<List<MasterProductRecommendationDTO>>(
				mastermfProductService.getAllMasterMFProductRecommendation(advisorId), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/downloadAllProductRecommendations", method = RequestMethod.GET)
	public ResponseEntity<?> downloadAllMasterMFProductRecommendation(@RequestParam int advisorId) throws IOException {

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

		byte excelByte[] = mastermfProductService.downloadAllMFProductRecommendation(advisorId);
		header.setContentLength(excelByte.length);
		return new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllMasterMutualFundETF", method = RequestMethod.GET)
	public ResponseEntity<List<MasterProductRecommendationDTO>> getAllMasterMutualFundETF() throws IOException {

		return new ResponseEntity<List<MasterProductRecommendationDTO>>(
				mastermfProductService.getAllMasterMutualFundETF(), HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/saveMasterMfProductReco", method = RequestMethod.POST)
	public ResponseEntity<MasterProductRecommendationDTO> saveMasterMfProductReco(@RequestParam int advisorId,
			@RequestParam List<String> isinList, @RequestParam String fromDate, @RequestParam String endDate) {

		MasterProductRecommendationDTO masterProductRecommendationDTO = mastermfProductService
				.saveMasterMfProductReco(advisorId, isinList, fromDate, endDate);

		return new ResponseEntity<MasterProductRecommendationDTO>(masterProductRecommendationDTO, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAllAssetSubAssetClasses", method = RequestMethod.GET)
	public ResponseEntity<List<MasterProductRecommendationDTO>> getAllAssetSubAssetClasses() throws IOException {

		return new ResponseEntity<List<MasterProductRecommendationDTO>>(
				mastermfProductService.getAllAssetSubAssetClasses(), HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','GoalPlanningView','PortFolioManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getAdvisorMFProductFundDetails", method = RequestMethod.GET)
	public ResponseEntity<?> getNumberOfFunds(@RequestParam int advisorId) {

		return new ResponseEntity<Object[]>(mastermfProductService.getMfProductRecoFundDetails(advisorId),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/getAdvisorMFProductSelectedFundDetails", method = RequestMethod.GET)
	public ResponseEntity<?> getAdvisorMFProductSelectedFundDetails(@RequestParam int advisorId,
			@RequestParam byte subAssetClassId) {

		return new ResponseEntity<String[]>(mastermfProductService.getMfProductRecoSelectedFundDetails(advisorId, subAssetClassId),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "/editMasterMfProductReco", method = RequestMethod.POST)
	public ResponseEntity<MasterProductRecommendationDTO> editMasterMfProductReco(@RequestParam int advisorId,
			@RequestParam List<String> isinList, @RequestParam String fromDate, @RequestParam String endDate) {

		MasterProductRecommendationDTO masterProductRecommendationDTO = mastermfProductService
				.editMasterMfProductReco(advisorId, isinList, fromDate, endDate);

		return new ResponseEntity<MasterProductRecommendationDTO>(masterProductRecommendationDTO, HttpStatus.OK);
	}
}
