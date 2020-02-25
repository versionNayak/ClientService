package com.finlabs.finexa.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.InvestorMasterBOService;

@RestController
public class InvestorMasterBOController {
	
	@Autowired
	private InvestorMasterBOService investMasterBOService;
	
	@PreAuthorize("hasAnyRole('Admin','MFBacKOfficeView')")
	@RequestMapping(value = "/investorNameList", method = RequestMethod.GET)
	public ResponseEntity<?> findAllFundHouseList(@RequestParam(value = "advisorID") int advisorID) throws FinexaBussinessException {
		try {
			List<String> listInvestorName = investMasterBOService.getAllInvestorNameList(advisorID);
			return new ResponseEntity<List<String>>(listInvestorName, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO_GINL", "Failed to get Investor Name Or Client Name List.", rexp);
		}

	}

}
