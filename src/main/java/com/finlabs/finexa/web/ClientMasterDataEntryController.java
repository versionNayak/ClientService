package com.finlabs.finexa.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.ClientInfoDTO;
import com.finlabs.finexa.dto.InvestorMasterSearchDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.service.AUMReportBOService;
import com.finlabs.finexa.service.AdvisorService;
import com.finlabs.finexa.service.ClientMasterDataEntryService;
import com.ibm.icu.text.SimpleDateFormat;

@RestController
public class ClientMasterDataEntryController {
	
	@Autowired
	private ClientMasterDataEntryService clientMasterDataEntryService;
	
	@Autowired
	AdvisorService advisorService;
	
	@Autowired
	AUMReportBOService aumReportBOService;
	
	@RequestMapping(value = "/investorListByNamePan", method = RequestMethod.GET)
	public ResponseEntity<?> investorListByNamePan(@RequestParam(value = "name") String name, 
			@RequestParam(value = "pan") String pan) throws FinexaBussinessException {
		try {
			List<InvestorMasterSearchDTO> listInvestorName = clientMasterDataEntryService.getInvestorDetailsByNamePan(name, pan);
			return new ResponseEntity<List<InvestorMasterSearchDTO>>(listInvestorName, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO_GINL", "Failed to get Investor Name Or Client Name List.", rexp);
		}
	}
	
	@RequestMapping(value = "/stagingInvestorListByNamePan", method = RequestMethod.GET)
	public ResponseEntity<?> StagingInvestorListByNamePan(@RequestParam(value = "advisorId") int advisorId,
									@RequestParam(value = "name") String name, @RequestParam(value = "pan") String pan) throws FinexaBussinessException {
		try {
			List<InvestorMasterSearchDTO> listInvestorName = clientMasterDataEntryService.getStagingInvestorDetailsByNamePan(advisorId, name, pan);
			return new ResponseEntity<List<InvestorMasterSearchDTO>>(listInvestorName, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO_GINL", "Failed to get Investor Name Or Client Name List.", rexp);
		}
	}
	
	@RequestMapping(value = "/createClient", method = RequestMethod.POST)
	public ResponseEntity<?> createClient(@Valid @RequestBody List<InvestorMasterSearchDTO> investorSearchDTO,HttpServletRequest request) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<InvestorMasterSearchDTO> resultDTOList = clientMasterDataEntryService.createClient(investorSearchDTO);
			for(InvestorMasterSearchDTO investorMasterSearchDTO:investorSearchDTO) {
				advisorService.deletetAUMCacheMap(investorMasterSearchDTO.getAdvisorUser(),request);
			}
			
			return new ResponseEntity<List<InvestorMasterSearchDTO>>(resultDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-CC", "Failed to Create Client", rexp);
		}
	}
	
	@RequestMapping(value = "/familyListByNamePan", method = RequestMethod.GET)
	public ResponseEntity<?> familyListByNamePan(@RequestParam(value = "name") String name,
			@RequestParam(value = "advisorId") int advisorId) throws FinexaBussinessException {
		try {
			List<ClientInfoDTO> listInvestorName = clientMasterDataEntryService.getFamilyDetailsByNamePan(name, advisorId);
			return new ResponseEntity<List<ClientInfoDTO>>(listInvestorName, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO_GINL", "Failed to get Investor Name Or Client Name List.", rexp);
		}
	}
	
	@RequestMapping(value = "/createFamily", method = RequestMethod.POST)
	public ResponseEntity<?> createFamily(@Valid @RequestBody List<InvestorMasterSearchDTO> investorSearchDTO) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<InvestorMasterSearchDTO> resultDTOList = clientMasterDataEntryService.createFamily(investorSearchDTO);
			return new ResponseEntity<List<InvestorMasterSearchDTO>>(resultDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("Create Family", "500", "Failed to Create Family Member", rexp);
		}
	}
	
	@RequestMapping(value = "/checkIfFamilyExists/{clientId}/{pan}", method = RequestMethod.GET)
	public ResponseEntity<?> checkIfFamilyExists(@PathVariable int clientId, @PathVariable String pan) throws FinexaBussinessException {
		try {
			boolean msg = clientMasterDataEntryService.checkIfFamilyExists(clientId, pan);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-CFE", "Failed to check existing Family Member", e);
		}
	}
	
	/*@RequestMapping(value = "/autoCreateStagingInvestor", method = RequestMethod.GET)
	public ResponseEntity<?> autoCreateStagingInvestor(@RequestParam(value = "advisorId") int advisorId) throws FinexaBussinessException {
		try {
			String msg = clientMasterDataEntryService.generateAutoClient(advisorId);
			return new ResponseEntity<String>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-CFE", "Failed to Auto Create Client", e);
		}
	}*/
	
	/*@RequestMapping(value = "/aumTest/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> aumTest(@PathVariable(value = "advisorId") int advisorId) throws FinexaBussinessException {
		try {
			String asOnDate = "2019-07-22";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = null;
			try {
				dt = sdf.parse(asOnDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Double msg = aumReportBOService.getTotalClientAUMFromTransaction(advisorId, dt);
			return new ResponseEntity<Double>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-CFE", "Failed to Auto Create Client", e);
		}
	}*/
	/*@RequestMapping(value = "/autoCreationTest/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> autoCreationTest(@PathVariable(value = "advisorId") int advisorId) throws FinexaBussinessException {
		try {
			Map <String, List<InvestorMasterSearchDTO>> panDTOMap = new HashMap<>();
			//panDTOMap = clientMasterDataEntryService.generatePANDTOMap(advisorId);
			panDTOMap = clientMasterDataEntryService.generateOptimizedPANDTOMap(advisorId);
			return new ResponseEntity<Map <String, List<InvestorMasterSearchDTO>>>(panDTOMap, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-CFE", "Failed to Auto Create Client", e);
		}
	}
	@RequestMapping(value = "/autoCreationTest2/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> autoCreationTest2(@PathVariable(value = "advisorId") int advisorId) throws FinexaBussinessException {
		try {
			List<InvestorMasterSearchDTO> panDTOList = new ArrayList<>();
			panDTOList = clientMasterDataEntryService.createClientAutomatically(advisorId);
			return new ResponseEntity<List<InvestorMasterSearchDTO>>(panDTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-CFE", "Failed to Auto Create Client", e);
		}
	}*/

}