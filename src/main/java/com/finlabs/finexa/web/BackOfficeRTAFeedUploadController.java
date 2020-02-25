package com.finlabs.finexa.web;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.AumMasterBODTO;
import com.finlabs.finexa.dto.BrokerageMasterBODTO;
import com.finlabs.finexa.dto.InvestorMasterBODTO;
import com.finlabs.finexa.dto.RejectionMasterBODTO;
import com.finlabs.finexa.dto.SIPSTPMasterBODTO;
import com.finlabs.finexa.dto.TransactionMasterBODTO;
import com.finlabs.finexa.dto.UploadHistoryDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.AsynchronousService;
import com.finlabs.finexa.service.AumMasterBOService;
import com.finlabs.finexa.service.BackOfficeRTAFeedUploadService;
import com.finlabs.finexa.service.BrokerageMasterBOService;
import com.finlabs.finexa.service.RejectionMasterBOService;
import com.finlabs.finexa.service.SIPSTPMasterBOService;
import com.finlabs.finexa.service.TransactionMasterBOService;


@RestController
public class BackOfficeRTAFeedUploadController {
	
	@Autowired
	BackOfficeRTAFeedUploadService backOfficeRTAFeedUploadService;
	
	@Autowired
	RejectionMasterBOService rejectionMasterBOService;
	
	@Autowired
	SIPSTPMasterBOService sIPSTPMasterBOService;
	
	@Autowired
	TransactionMasterBOService transactionMasterBOService;
	
	@Autowired
	AumMasterBOService aumMasterBOService;

	@Autowired
	BrokerageMasterBOService brokerageMasterBOService;
	
	@Autowired
	AsynchronousService asynchronousService;
	
		
	@RequestMapping(value = "/uploadInvestorFeed", method = RequestMethod.POST)
    public ResponseEntity<?> uploadInvestorFeed(@ModelAttribute InvestorMasterBODTO investorMasterBODTO) 
    		throws InvalidFormatException, IOException, ParseException{
        try {
			UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
			uploadResponseDTO = asynchronousService.feedUploadInvestorAsynchronously(investorMasterBODTO, uploadResponseDTO);
			//uploadResponseDTO = backOfficeRTAFeedUploadService.uploadInvestorMaster(investorMasterBODTO, uploadResponseDTO);
			System.out.println("STATUS: "+uploadResponseDTO.isStatus());
            return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
        	throw new RuntimeException(e); 
        }
    }
	
	@RequestMapping(value = "/backOffice/uploadTransactionMaster", method = RequestMethod.POST)
	public ResponseEntity<?> uploadTransactionMaster(@ModelAttribute TransactionMasterBODTO transactionMasterBODTO)
			throws InvalidFormatException, IOException, ParseException, FinexaBussinessException {
		
		try {
			UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
			uploadResponseDTO = asynchronousService.feedUploadTransactionAsynchronously(transactionMasterBODTO, uploadResponseDTO);
			System.out.println("STATUS: "+uploadResponseDTO.isStatus());
			return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-DFU", "Failed to upload Transaction Daily Feed.", e);
		}
		
	}
	
	@RequestMapping(value = "/uploadRejectionFeed", method = RequestMethod.POST)
    public ResponseEntity<?> uploadRejectionFeed(@ModelAttribute RejectionMasterBODTO rejectionMasterBODTO) 
    		throws InvalidFormatException, IOException, ParseException{
        try {
			UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
			uploadResponseDTO = asynchronousService.feedUploadRejectionAsynchronously(rejectionMasterBODTO, uploadResponseDTO);
			System.out.println("STATUS: "+uploadResponseDTO.isStatus());
            return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
        	throw new RuntimeException(e); 
        }
    }
	
	@RequestMapping(value = "/uploadSIPSTPFeed", method = RequestMethod.POST)
    public ResponseEntity<?> uploadSIPSTPFeed(@ModelAttribute SIPSTPMasterBODTO sIPSTPMasterBODTO) 
    		throws InvalidFormatException, IOException, ParseException{
        try {
			UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
			uploadResponseDTO = asynchronousService.feedUploadSIPSTPAsynchronously(sIPSTPMasterBODTO, uploadResponseDTO);
            return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
        	throw new RuntimeException(e); 
        }
    }
	
	@RequestMapping(value = "/backOffice/uploadAumFeed", method = RequestMethod.POST)
	public ResponseEntity<?> uploadAumFeed(@ModelAttribute AumMasterBODTO aumMasterBODTO)
			throws InvalidFormatException, IOException, ParseException, FinexaBussinessException {
		
		try {
			UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
			uploadResponseDTO = asynchronousService.feedUploadAUMAsynchronously(aumMasterBODTO, uploadResponseDTO);
			System.out.println("STATUS: "+uploadResponseDTO.isStatus());
			//uploadResponseDTO = aumMasterBOService.uploadAumMaster(aumMasterBODTO, uploadResponseDTO);
			return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-DFU", "Failed to upload AUM Feed.", e);
		}
	}	

	@RequestMapping(value = "/uploadBrokerageFeed", method = RequestMethod.POST)
    public ResponseEntity<?> uploadBrokerageFeed(@ModelAttribute BrokerageMasterBODTO brokerageMasterBODTO) 
    		throws InvalidFormatException, IOException, ParseException{
        try {
			UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
			uploadResponseDTO = asynchronousService.feedUploadBrokerageAsynchronously(brokerageMasterBODTO, uploadResponseDTO);
            return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
        	throw new RuntimeException(e); 
        }
    }
	
	@RequestMapping(value = "/getFeedUploadHistory/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientCode(@PathVariable int advisorId) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<UploadHistoryDTO> viewUploadHistoryDTOs = backOfficeRTAFeedUploadService.getAllUploadHistory(advisorId);
			return new ResponseEntity<List<UploadHistoryDTO>>(viewUploadHistoryDTOs, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-DFU", "Failed to get Upload History.", rexp);
		}
	}
	
	//==================pagination=====================//
	@RequestMapping(value = "/getUploadHistoryCount/{advisorId}", method = RequestMethod.GET)
	public ResponseEntity<?> getUploadHistoryCount(@PathVariable int advisorId) throws FinexaBussinessException, CustomFinexaException {
		try {
			int count = backOfficeRTAFeedUploadService.getUploadHistoryCount(advisorId);
			return new ResponseEntity<Integer>(count, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-DFU", "Failed to get Upload History.", rexp);
		}
	}
	
	@RequestMapping(value = "/getFeedUploadHistoryWithPagination/{advisorId}/{nextFetch}", method = RequestMethod.GET)
	public ResponseEntity<?> getFeedUploadHistoryWithPagination(@PathVariable int advisorId, @PathVariable int nextFetch) throws FinexaBussinessException, CustomFinexaException {
		try {
			Pageable pageable = new PageRequest(nextFetch, 50);
			
			List<UploadHistoryDTO> viewUploadHistoryDTOs = backOfficeRTAFeedUploadService.getFeedUploadHistoryWithPagination(advisorId, pageable);
			return new ResponseEntity<List<UploadHistoryDTO>>(viewUploadHistoryDTOs, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("MF-BackOffice", "MFBO-DFU", "Failed to get Upload History.", rexp);
		}
	}
	//================================================//
	
	
}



	
	

	
	
	