package com.finlabs.finexa.web;

import java.util.List;

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

import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.dto.AdvisorUserDTO;
import com.finlabs.finexa.dto.RmMasterBODTO;

import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.RmMasterBOService;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class RmMasterBOController {
	
	@Autowired
	public RmMasterBOService rmMasterBOService;
	
	@Autowired
	public FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@Autowired
	public FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	
	@PreAuthorize("hasRole('MFBacKOfficeAddEdit')")
	@RequestMapping(value="/addRmMaster", method=RequestMethod.POST)
	public ResponseEntity<?> createRmMaster(@RequestBody RmMasterBODTO rmMasterBODTO) throws CustomFinexaException, FinexaBussinessException{
		
		try {
			rmMasterBODTO = rmMasterBOService.save(rmMasterBODTO);
			return new ResponseEntity<RmMasterBODTO>(rmMasterBODTO , HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-RM01", "Failed to save RM details", e);
		}
		
	}
	
	@PreAuthorize("hasRole('MFBacKOfficeAddEdit')")
	@RequestMapping(value="/updateRmMaster", method=RequestMethod.POST)
	public ResponseEntity<?> updateRmMaster(@RequestBody RmMasterBODTO rmMasterBODTO)
			throws FinexaBussinessException, CustomFinexaException {
		
		try {
			rmMasterBODTO = rmMasterBOService.update(rmMasterBODTO);
			return new ResponseEntity<RmMasterBODTO>(rmMasterBODTO , HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MF_BACK_OFFICE_RELATIONSHIP_MANAGER);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MF_BACK_OFFICE_RM_MANAGER_UPDATE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MF_BACK_OFFICE,
					FinexaConstant.MF_BACK_OFFICE_RM_MANAGER_UPDATE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
		
	}
	
	@PreAuthorize("hasRole('MFBacKOfficeView')")
	@RequestMapping(value = "/getRmMaster/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> findByAdvisorId(@PathVariable int advisorID) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<RmMasterBODTO> rmMasterDTOList = rmMasterBOService.findByAdvisorId(advisorID);
			return new ResponseEntity<List<RmMasterBODTO>>(rmMasterDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MF_BACK_OFFICE_RELATIONSHIP_MANAGER);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.MF_BACK_OFFICE_RM_MANAGER_GET_BY_ADVISOR_ID_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MF_BACK_OFFICE, FinexaConstant.MF_BACK_OFFICE_RM_MANAGER_GET_BY_ADVISOR_ID_ERROR,
					exception != null ? exception.getErrorMessage() : "", rexp);
		}
	}
	
	@PreAuthorize("hasRole('MFBacKOfficeView')")
	@RequestMapping(value = "/rmMaster", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@RequestParam int id) throws FinexaBussinessException {
		try {
			RmMasterBODTO rmMasterDTO = rmMasterBOService.findById(id);
			return new ResponseEntity<RmMasterBODTO>(rmMasterDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MF_BACK_OFFICE_RELATIONSHIP_MANAGER);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MF_BACK_OFFICE_RM_MANAGER_FETCH_A_RM_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MF_BACK_OFFICE,
					FinexaConstant.MF_BACK_OFFICE_RM_MANAGER_FETCH_A_RM_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('MFBacKOfficeAddEdit','UserManagementDelete')")
	@RequestMapping(value = "/rmMasterBODelete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable int id) throws FinexaBussinessException {
		try {
			int i = rmMasterBOService.delete(id);
			return new ResponseEntity<Integer>(i, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MF_BACK_OFFICE_RELATIONSHIP_MANAGER);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.MF_BACK_OFFICE_RM_MANAGER_DELETE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MF_BACK_OFFICE,
					FinexaConstant.MF_BACK_OFFICE_RM_MANAGER_DELETE_ERROR, exception != null ? exception.getErrorMessage() : "", rexp);
		}
	}
	
	@PreAuthorize("hasRole('MFBacKOfficeView')")
	@RequestMapping(value = "/getRMNameList/{branchId}", method = RequestMethod.GET)
	public ResponseEntity<?> getRMNameList(@PathVariable int branchId) throws FinexaBussinessException {
		
		try {
			List<AdvisorUserDTO> userList = rmMasterBOService.getRelationshipManagerUsersNameForParticularBranch(branchId);
			return new ResponseEntity<List<AdvisorUserDTO> >(userList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("MF-BackOffice","MFBO-RM05","Failed to get Relationship Manager Name in Family Attribute Master.", e);
		}
		
	}
	
	@PreAuthorize("hasAnyRole('AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkIfRmRoleExists/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> checkIfRmRoleExists(@PathVariable int advisorID) throws FinexaBussinessException {
		
		try {
			boolean checkFlag = rmMasterBOService.checkIfRmManagerRoleExists(advisorID);
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw e;
			
		}
	}
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkUniqueEmpCodeForAdvisorMaster/{advisorID}/{employeeCode}", method = RequestMethod.GET)
	public ResponseEntity<?>checkUniqueEmpCodeForAdvisorMaster
				(@PathVariable int advisorID,@PathVariable String employeeCode) 
						throws FinexaBussinessException {
		try {
			boolean checkFlag = rmMasterBOService.checkUniqueEmpCodeForAdvisorMaster(advisorID,employeeCode);		
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
		
		

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkExistingEmpCodeForAdvisorMaster/{advisorID}/{UserID}/{employeeCode}", method = RequestMethod.GET)
	public ResponseEntity<?>checkExistingEmpCodeForAdvisorMaster
				(@PathVariable int advisorID,@PathVariable int UserID,@PathVariable String employeeCode) 
						throws FinexaBussinessException {
		try {
			boolean checkFlag = rmMasterBOService.checkExistingEmpCodeForAdvisorMaster(advisorID,UserID,employeeCode);		
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
				

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkUniqueEmailForAdvisorMaster/{email}/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?>checkUniqueEmailForAdvisorMaster
				(@PathVariable String email,@PathVariable int advisorID) 
						throws FinexaBussinessException {
		try {
			boolean checkFlag = rmMasterBOService.checkUniqueEmailForAdvisorMaster(email,advisorID);		
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
		
		

	}
	
	
	

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkExistsEmailForAdvisorMaster/{email}/{advisorID}/{UserID}", method = RequestMethod.GET)
	public ResponseEntity<?>checkExistsEmailForAdvisorMaster
				(@PathVariable String email,@PathVariable int advisorID,@PathVariable int UserID) 
						throws FinexaBussinessException {
		try {
			boolean checkFlag = rmMasterBOService.checkExistsEmailForAdvisorMaster(email,advisorID,UserID);		
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
		
		

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkRmOrSbIsAssigned/{UserID}", method = RequestMethod.GET)
	public ResponseEntity<?>checkRmOrSbIsAssigned
				(@PathVariable int UserID)
						throws FinexaBussinessException {
		try {
			boolean checkFlag = rmMasterBOService.checkRmOrSbIsAssigned(UserID);		
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
		
		

	}
	
	

	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkUniqueMobileNumber/{mobileNo}", method = RequestMethod.GET)
	public ResponseEntity<?>checkUniqueMobileNumber
				(@PathVariable String mobileNo) 
						throws FinexaBussinessException {
		try {
			boolean checkFlag = rmMasterBOService.checkUniqueMobileNumber(mobileNo);		
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
		
		

	}
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkExistingMobileNumber/{userID}/{mobileNo}", method = RequestMethod.GET)
	public ResponseEntity<?>checkExistingMobileNumber
				(@PathVariable String mobileNo, @PathVariable int userID) 
						throws FinexaBussinessException {
		try {
			boolean checkFlag = rmMasterBOService.checkExistingMobileNumber(mobileNo,userID);		
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","", e);
		}
		
		

	}
	
	


}
