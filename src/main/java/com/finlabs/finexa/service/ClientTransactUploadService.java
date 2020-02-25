package com.finlabs.finexa.service;

import com.finlabs.finexa.dto.ClientTransactAOFDTO;
import com.finlabs.finexa.dto.ClientTransactNachDTO;
import com.finlabs.finexa.dto.ClientUCCResultDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.MasterTransactMandateDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.exception.CustomFinexaException;

public interface ClientTransactUploadService {

	
	 public UploadResponseDTO uploadBulkUsers(FileuploadDTO fileuploadDTO) throws RuntimeException, CustomFinexaException;
	 
	 //Upload AOF form 
	 public ClientUCCResultDTO uploadAOF(ClientTransactAOFDTO fileuploadDTO) throws RuntimeException, CustomFinexaException;
	  
	  
	 //Upload Nach form 
	 public ClientUCCResultDTO uploadNach(ClientTransactNachDTO fileuploadDTO) throws RuntimeException, CustomFinexaException;
	  
	  
	 // ---------------- Mandate Transact ---------------- public
	 UploadResponseDTO uploadBulkMandateDetails(MasterTransactMandateDTO fileuploadDTO) throws RuntimeException, CustomFinexaException;
	  
	 // ---------------- Fatca Details Upload ---------------- //public
	 //UploadResponseDTO uploadBulkFatcaDetails(MasterTransactFatcaDTO fileuploadDTO) throws RuntimeException, CustomFinexaException;
	 
}
