package com.finlabs.finexa.web;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.finlabs.finexa.dto.SubBrokerMasterDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.SubBrokerMasterBOService;

@RestController
public class SubBrokerMasterBOController {
    private static Logger log = LoggerFactory.getLogger(SubBrokerMasterBOController.class);
   
    @Autowired
    SubBrokerMasterBOService subBrokerMasterBOService;
   
	@PreAuthorize("hasRole('MFBacKOfficeAddEdit')")
    @RequestMapping(value="/addSubBrokerMaster", method=RequestMethod.POST)
    public ResponseEntity<?> createSubBrokerMaster(@Valid @RequestBody SubBrokerMasterDTO subBrokerMasterDTO) throws InvalidFormatException, RuntimeException, IOException, ParseException{
        log.debug("RoleController.createSubBrokerMaster " + subBrokerMasterDTO);
       
        subBrokerMasterDTO = subBrokerMasterBOService.save(subBrokerMasterDTO);
       
        return new ResponseEntity<SubBrokerMasterDTO>(subBrokerMasterDTO , HttpStatus.OK);
       
    }
    
	@PreAuthorize("hasRole('MFBacKOfficeView')")
    @RequestMapping(value = "/getSbMaster/{advisorID}", method = RequestMethod.GET)
    public ResponseEntity<?> findByAdvisorId(@PathVariable int advisorID) throws Exception {
    	
    	try {
			//List<RmMasterBODTO> rmMasterDTOList = rmMasterBOService.findByAdvisorId(advisorID);
			//return new ResponseEntity<List<RmMasterBODTO>>(rmMasterDTOList, HttpStatus.OK);
    		
    		List<SubBrokerMasterDTO> subBrokerMasterDTOList = subBrokerMasterBOService.findByAdvisorId(advisorID);
        	return new ResponseEntity<List<SubBrokerMasterDTO>>(subBrokerMasterDTOList, HttpStatus.OK);
    		
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("","","", rexp);
		}
    	
    }
	
	@PreAuthorize("hasRole('MFBacKOfficeAddEdit')")
    @RequestMapping(value="/updateSbMaster", method=RequestMethod.POST)
    public ResponseEntity<?> updateSbMaster(@RequestBody SubBrokerMasterDTO subBrokerMasterDTO) throws Exception{
    	
    	//subBrokerMasterDTO = subBrokerMasterBOService.save(subBrokerMasterDTO);
    	subBrokerMasterDTO = subBrokerMasterBOService.update(subBrokerMasterDTO);
    	return new ResponseEntity<SubBrokerMasterDTO>(subBrokerMasterDTO , HttpStatus.OK);
    		
    }
    
    @PreAuthorize("hasRole('MFBacKOfficeView')")
    @RequestMapping(value = "/SBMaster", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@RequestParam int advisorUserId) throws Exception{
    	
    	SubBrokerMasterDTO subBrokerMasterDTO = subBrokerMasterBOService.findById(advisorUserId);
    	return new ResponseEntity<SubBrokerMasterDTO>(subBrokerMasterDTO, HttpStatus.OK);
    
    }
    
    @PreAuthorize("hasAnyRole('MFBacKOfficeAddEdit','UserManagementDelete')")
    @RequestMapping(value = "/SBMasterDelete/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> delete(@PathVariable int id) throws Exception{
  
    	int i = subBrokerMasterBOService.delete(id);
    	return new ResponseEntity<Integer>(i, HttpStatus.OK);
   
    }
    
    @PreAuthorize("hasRole('MFBacKOfficeView')")
    @RequestMapping(value = "/getSBNameList/{branchId}", method = RequestMethod.GET)
	public ResponseEntity<?> getSBNameList(@PathVariable int branchId) throws FinexaBussinessException {
		
		try {
			List<AdvisorUserDTO> userList = subBrokerMasterBOService.getSubBrokerUsersNameForParticularBranch(branchId);
			return new ResponseEntity<List<AdvisorUserDTO> >(userList, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("MF-BackOffice","MFBO-SB06","Failed to get Sub Broker Name in Family Attribute Master.", e);
		}
		
	}
    
    @PreAuthorize("hasAnyRole('AdvisorAdmin','MFBackOffice')")
	@RequestMapping(value = "/checkIfSbRoleExists/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> checkIfSbRoleExists(@PathVariable int advisorID) throws FinexaBussinessException {
		
		try {
			boolean checkFlag = subBrokerMasterBOService.checkIfSbRoleExists(advisorID);
			return new ResponseEntity<Boolean>(checkFlag, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw e;
			
		}
	}
    
    
}
    
    

  

	
