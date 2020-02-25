package com.finlabs.finexa.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.ClientInfoDTO;
import com.finlabs.finexa.dto.InvestorMasterSearchDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;

public interface ClientMasterDataEntryService {
	
	List<InvestorMasterSearchDTO> getInvestorDetailsByNamePan(String name, String pan) throws FinexaBussinessException;
	
	List<InvestorMasterSearchDTO> getStagingInvestorDetailsByNamePan(int advisorId, String name, String pan) throws FinexaBussinessException;
	
	List<ClientInfoDTO> getFamilyDetailsByNamePan(String name, int advisorId) throws FinexaBussinessException;

	public List<InvestorMasterSearchDTO> createClient(List<InvestorMasterSearchDTO> investDTOList) throws FinexaBussinessException;
	
	public List<InvestorMasterSearchDTO> createFamily(List<InvestorMasterSearchDTO> investDTOList) throws FinexaBussinessException;

	boolean checkIfFamilyExists(int clientId, String pan) throws RuntimeException;
	
	String generateAutoClient(int advisorId,String createdOn) throws RuntimeException;
	
	public Map <String, List<InvestorMasterSearchDTO>> generatePANDTOMap (int advisorID);
	
	public List<InvestorMasterSearchDTO> createClientAutomatically (int advisorID,String createdOn);
	
	public Map <String, List<InvestorMasterSearchDTO>> generateOptimizedPANDTOMap (int advisorID,String createdOn);

}
