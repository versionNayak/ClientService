package com.finlabs.finexa.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.CurrentHoldingReportColumnDTO;
import com.finlabs.finexa.dto.CurrentHoldingReportDTO;
import com.finlabs.finexa.dto.DividendReportColumnDTO;
import com.finlabs.finexa.dto.DividendReportDTO;
import com.finlabs.finexa.dto.SIPSTPSWPReportColumnDTO;
import com.finlabs.finexa.dto.SIPSTPSWPReportDTO;
import com.finlabs.finexa.dto.TransactionReportColumnDTO;
import com.finlabs.finexa.dto.TransactionReportDTO;
import com.finlabs.finexa.service.DividendReportBOService;
import com.finlabs.finexa.service.RealisedGainBOService;
import com.finlabs.finexa.service.SIPSTPSWPReportService;
//import com.finlabs.finexa.service.MasterTestingService;
import com.finlabs.finexa.service.TransactionReportService;

@RestController
public class TransactionReportController {
	
	@Autowired 
	private TransactionReportService transactionReportService;
	
	@Autowired 
	private DividendReportBOService dividendReportBOService;
	
	@Autowired
	private RealisedGainBOService realisedGainBOService;
	
	@Autowired
	private SIPSTPSWPReportService sipSTPSWPReportService;
	
	/*@Autowired
	private MasterTestingService masterTestingService;*/
	
	@RequestMapping(value = "/transactionReport", method = RequestMethod.GET)
	public ResponseEntity<?> showReport() {
		try {
			TransactionReportDTO transactionReportDTO = new TransactionReportDTO();
			Map<String, List<TransactionReportColumnDTO>> transactionMap = transactionReportService.transactionReport(transactionReportDTO); 
			return new ResponseEntity<Map<String, List<TransactionReportColumnDTO>>>(transactionMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;		
		
	}
	
	@RequestMapping(value = "/dividendReport", method = RequestMethod.GET)
	public ResponseEntity<?> showDividendReport() {
		try {
			DividendReportDTO dividendReportDTO = new DividendReportDTO();
			Map<String, List<DividendReportColumnDTO>> dividendMap = dividendReportBOService.dividendReport(dividendReportDTO); 
			return new ResponseEntity<Map<String, List<DividendReportColumnDTO>>>(dividendMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;		
		
	}
	
	@RequestMapping(value = "/realisedGainReport", method = RequestMethod.GET)
	public ResponseEntity<?> showRealisedGainReport() {
		try {
			CurrentHoldingReportDTO currentHoldingReportDTO = new CurrentHoldingReportDTO();
			Map<String, List<CurrentHoldingReportColumnDTO>> realisedGainMap = realisedGainBOService.realisedGainReport(currentHoldingReportDTO); 
			return new ResponseEntity<Map<String, List<CurrentHoldingReportColumnDTO>>>(realisedGainMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;		
		
	}
	
	@RequestMapping(value = "/sipSTPReport", method = RequestMethod.GET)
	public ResponseEntity<?> showSIPSTPSWPReport() {
		try {
			SIPSTPSWPReportDTO sipSTPSWPReportDTO = new SIPSTPSWPReportDTO();
			Map<String, List<SIPSTPSWPReportColumnDTO>> sipSTPSWPMap = sipSTPSWPReportService.sipSTPSWPReport(sipSTPSWPReportDTO); 
			return new ResponseEntity<Map<String, List<SIPSTPSWPReportColumnDTO>>>(sipSTPSWPMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;		
		
	}
	
	/*@RequestMapping(value = "/masterTesting", method = RequestMethod.GET)
	public ResponseEntity<?> testMaster() {
		try {
			List<String> resultList = masterTestingService.testRejection();
			return new ResponseEntity<List<String>>(resultList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}*/	

}