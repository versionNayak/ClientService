package com.finlabs.finexa.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.AdvisorUserDTO;
import com.finlabs.finexa.dto.BranchMasterDetailsBODTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.service.BranchMasterDetailsBOService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class BranchMasterDetailsBOController {

	@Autowired
	BranchMasterDetailsBOService branchMasterDetailsBOService;
	
	@RequestMapping(value = "/addBranchMaster", method = RequestMethod.POST)
	public ResponseEntity<?> addBranchMaster(@RequestParam int advisorUserId,@Valid @RequestBody BranchMasterDetailsBODTO branchMasterDetailsBODTO)
			throws FinexaBussinessException {
		try {
			branchMasterDetailsBODTO = branchMasterDetailsBOService.save(branchMasterDetailsBODTO,advisorUserId);
			return new ResponseEntity<BranchMasterDetailsBODTO>(branchMasterDetailsBODTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
	}
	
	@RequestMapping(value = "/updateBranchMaster", method = RequestMethod.POST)
	public ResponseEntity<?> updateBranchMaster(@Valid @RequestBody BranchMasterDetailsBODTO branchMasterDetailsBODTO)
			throws FinexaBussinessException {
		try {
			branchMasterDetailsBODTO = branchMasterDetailsBOService.update(branchMasterDetailsBODTO);
			return new ResponseEntity<BranchMasterDetailsBODTO>(branchMasterDetailsBODTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
	}
	
	
	@RequestMapping(value = "/getAllMFBackOfficeBranchByAdvisorId/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllMFBackOfficeBranchByAdvisorId(@PathVariable int advisorId) throws FinexaBussinessException {
		
		try {
			List<BranchMasterDetailsBODTO> branchMasterDetailsBODTOList = branchMasterDetailsBOService.getAllMFBackOfficeBranchByAdvisorId(advisorId);
			return new ResponseEntity<List<BranchMasterDetailsBODTO>>(branchMasterDetailsBODTOList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
		
	}
	
	@RequestMapping(value = "/getBranchDetailByBranchId", method = RequestMethod.GET)
	public ResponseEntity<?> getBranchDetailByBranchId(@RequestParam int branchMasterId) throws FinexaBussinessException {

		try {
			BranchMasterDetailsBODTO branchMasterDetailsBODTO = branchMasterDetailsBOService.findByBranchMasterId(branchMasterId);
			return new ResponseEntity<BranchMasterDetailsBODTO>(branchMasterDetailsBODTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
	}
	
	@RequestMapping(value = "/branchMasterBODelete/{branchId}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int branchId) throws FinexaBussinessException {
		try {
			int i = branchMasterDetailsBOService.deleteBranchDetailsByBranchId(branchId);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/getBranchManagerList/{advisorUserId}", method = RequestMethod.GET)
	public ResponseEntity<?> getMFBackOfficeAdvisorMasterName(@PathVariable int advisorUserId) throws FinexaBussinessException {
		
		try {
			List<AdvisorUserDTO> userList = branchMasterDetailsBOService.getBranchManagerUsersName(advisorUserId);
			return new ResponseEntity<List<AdvisorUserDTO> >(userList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("MF-BackOffice","MFBO-BM05","Failed to get Branch Name in RM Master.", e);
		}
		
	}
	
	@PreAuthorize("hasAnyRole('AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkIfBranchExists/{branchHeadId}", method = RequestMethod.GET)
	public ResponseEntity<?> checkIfBranchWithSameBranchHeadExists(@PathVariable int branchHeadId) throws FinexaBussinessException {
		
		try {
			boolean checkFlag = branchMasterDetailsBOService.checkIfBranchWithSameBranchHeadExists(branchHeadId);
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-BM06", "Failed to check if branch with same branch head exists or not.", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkIfBranchExistsForBranchMaster/{branchHeadId}/{branchId}", method = RequestMethod.GET)
	public ResponseEntity<?> checkIfBranchExistsForBranchMaster(@PathVariable int branchHeadId, @PathVariable int branchId) throws FinexaBussinessException {
		
		try {
			boolean checkFlag = branchMasterDetailsBOService.checkIfBranchWithSameBranchHeadExistsForEdit(branchHeadId,branchId);
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-BM06", "Failed to check if branch with same branch head exists or not.", e);
		}
	}
	
	
	
	@PreAuthorize("hasAnyRole('AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkUniqueBranchCode/{branchCode}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniqueBranchCode(@PathVariable int userId,@PathVariable String branchCode)
			throws FinexaBussinessException {
		// log.debug("inside checkEmployeeCodeExists");
		try {
			boolean msg = branchMasterDetailsBOService.checkUniqueBranchCode(userId, branchCode);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkExistingBranchCode/{branchID}/{branchCode}", method = RequestMethod.GET)
	public ResponseEntity<?> checkExistingBranchCode(@PathVariable int branchID,@PathVariable String branchCode)
			throws FinexaBussinessException {
		// log.debug("inside checkEmployeeCodeExists");
		try {
			boolean msg = branchMasterDetailsBOService.checkExistingBranchCode(branchID, branchCode);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkIfBranchMasterExists/{branchHeadID}", method = RequestMethod.GET)
	public ResponseEntity<?> checkIfBranchMasterExists(@PathVariable int branchHeadID) throws FinexaBussinessException {
		
		try {
			boolean checkFlag = branchMasterDetailsBOService.checkIfBranchMasterExists(branchHeadID);
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkIfBranchMasterisAssigned/{branchBranchMasterID}", method = RequestMethod.GET)
	public ResponseEntity<?> checkIfBranchMasterisAssigned(@PathVariable int branchBranchMasterID) throws FinexaBussinessException {
		
		try {
			boolean checkFlag = branchMasterDetailsBOService.checkIfBranchMasterisAssigned(branchBranchMasterID);
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkUniquePhoneNumber/{phoneNumber}", method = RequestMethod.GET)
	public ResponseEntity<?> checkUniquePhoneNumber(@PathVariable String phoneNumber)
			throws FinexaBussinessException {
		// log.debug("inside checkEmployeeCodeExists");
		try {
			boolean msg = branchMasterDetailsBOService.checkUniqueMobileNumber(phoneNumber);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
	}
	@PreAuthorize("hasAnyRole('AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkExistingPhoneNumber/{branchBranchMasterID}/{phoneNumber}", method = RequestMethod.GET)
	public ResponseEntity<?> checkExistingPhoneNumber(@PathVariable int branchBranchMasterID,@PathVariable String phoneNumber)
			throws FinexaBussinessException {
		// log.debug("inside checkEmployeeCodeExists");
		try {
			boolean msg = branchMasterDetailsBOService.checkExistingPhoneNumber(branchBranchMasterID,phoneNumber);
			return new ResponseEntity<Boolean>(msg, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
	}
		
		
}