package com.finlabs.finexa.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import com.finlabs.finexa.dto.LookupCountryDTO;
import com.finlabs.finexa.dto.LookupEducationalQualificationDTO;
import com.finlabs.finexa.dto.LookupEmploymentTypeDTO;
import com.finlabs.finexa.dto.LookupMaritalStatusDTO;
import com.finlabs.finexa.dto.LookupResidentTypeDTO;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.LookupResidentType;
import com.finlabs.finexa.service.ClientMasterService;
import com.finlabs.finexa.service.LookupService;



public class ClientPersonalInfoController {
/*	
	private static Logger log = LoggerFactory.getLogger(ClientPersonalInfoController.class);
	
	@Autowired
	ClientMasterService clientPersonalInfoService;

	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value="/clientPersonalInfo", method=RequestMethod.POST)
	public ResponseEntity<?> createCtPersonalInfo(@RequestBody ClientPersonalInfoDTO clientPersonalInfoDTO){
		
		
		clientPersonalInfoDTO = clientPersonalInfoService.save(clientPersonalInfoDTO);
		
			return new ResponseEntity<ClientPersonalInfoDTO>(clientPersonalInfoDTO , HttpStatus.OK);
		}
	
		
	@PreAuthorize("hasAnyRole('Admin','ClientInfoAddEdit')")
	@RequestMapping(value="/clientPersonalInfo", method=RequestMethod.PUT)
	public ResponseEntity<?> editCtPersonalInfo(@RequestBody ClientPersonalInfoDTO clientPersonalInfoDTO){
		clientPersonalInfoDTO = clientPersonalInfoService.update(clientPersonalInfoDTO);
		return new ResponseEntity<ClientPersonalInfoDTO>(clientPersonalInfoDTO , HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value="/clientPersonalInfo/{id}", method=RequestMethod.GET)
	 public ResponseEntity<?> findById(@PathVariable long id){

		//log.debug("User for username and Password = " + emailId + "" + password);
		ClientPersonalInfoDTO clientPersonalInfoDTO = clientPersonalInfoService.findById(id);
		log.debug("birth date = " + clientPersonalInfoDTO.getBirthDate());
	
			
			return new ResponseEntity<ClientPersonalInfoDTO>(clientPersonalInfoDTO , HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value="/clientPersonalInfoList", method=RequestMethod.GET)
	 public ResponseEntity<?> findAll(){

		//log.debug("User for username and Password = " + emailId + "" + password);
		List<ClientPersonalInfoDTO> clientPersonalInfoDTOList = clientPersonalInfoService.findAll();
		
			log.debug("Num of Client = " + clientPersonalInfoDTOList.size());
		return new ResponseEntity<List<ClientPersonalInfoDTO>>(clientPersonalInfoDTOList , HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientInfoView')")
	@RequestMapping(value="/clientPersonalInfo/{id}", method=RequestMethod.DELETE)
	 public ResponseEntity<?> delete(@PathVariable long id){

		ClientPersonalInfoDTO clientPersonalInfoDTO = clientPersonalInfoService.findById(id);
		clientPersonalInfoDTO.setActiveFlag("N");
		
		ClientPersonalInfoDTO retUserDTO = clientPersonalInfoService.update(clientPersonalInfoDTO);
		return new ResponseEntity<ClientPersonalInfoDTO>(retUserDTO , HttpStatus.OK);
	}
	
	*/
	

	
}
