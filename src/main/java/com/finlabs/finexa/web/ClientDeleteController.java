package com.finlabs.finexa.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.AdvisorUserLoginInfoDTO;
import com.finlabs.finexa.dto.ClientGoalDTO;
import com.finlabs.finexa.dto.ClientMeetingDTO;
import com.finlabs.finexa.dto.ClientTaskDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.MasterNAVHistoryDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.service.AccordMonitoringService;
import com.finlabs.finexa.service.ClientDeletionService;
import com.finlabs.finexa.service.ClientMeetingService;
import com.finlabs.finexa.service.ClientTaskService;
import com.finlabs.finexa.util.ExcelClientUtility;
import com.finlabs.finexa.util.FinexaConstant;


@RestController
public class ClientDeleteController {
	
	
	@Autowired
	private ClientDeletionService clientDeletionService;

		
	
	@PreAuthorize("hasAnyRole('Admin','AdvisorAdmin')")
	@RequestMapping(value = "deleteClientRecord/{advisorId}/{radioValue}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteClientRecord(@PathVariable int advisorId,@PathVariable String radioValue) throws FinexaBussinessException {
		try {
			String yes = "Y", no = "N";
			if(radioValue.equals("CM"))
				radioValue = yes;
			else if(radioValue.equals("BO"))
				radioValue = no;
			System.out.println("advisorId : "+advisorId +" radio " + radioValue);
			List<ClientMaster> clientMasterList = clientDeletionService.deleteClientByAdvisor(advisorId, radioValue);
			System.out.println("RETURNED TO CONTROLLER");
			for(ClientMaster ClientMaster : clientMasterList)
				System.out.println("First name: "+ClientMaster.getFirstName());
			return new ResponseEntity<List<ClientMaster>>(clientMasterList, HttpStatus.OK);
			//return new ResponseEntity<Integer>(1, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
		
	}
	
	
}
