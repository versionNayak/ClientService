package com.finlabs.finexa.service;

import java.util.List;
import java.util.Set;

import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.dto.ClientFloaterCoverDTO;
import com.finlabs.finexa.dto.ClientNonlifeInsuranceDTO;
import com.finlabs.finexa.dto.FinexaMessageDto;
import com.finlabs.finexa.dto.LookupHealthInsurancePolicyTypeDTO;
import com.finlabs.finexa.dto.LookupNonLifeInsurancePolicyTypeDTO;
import com.finlabs.finexa.dto.LookupNonlifeInsuranceTypeDTO;
import com.finlabs.finexa.dto.MasterInsurancePolicyDTO;
import com.finlabs.finexa.dto.MasterInsuranceCompanyNameDTO;


public interface ClientNonLifeInsuranceService {
	ClientNonlifeInsuranceDTO save(ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO) throws RuntimeException;
	ClientNonlifeInsuranceDTO update(ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO) throws RuntimeException;
	public int delete(int id) throws RuntimeException;
	public ClientNonlifeInsuranceDTO findById(int id) throws RuntimeException;	
	public ClientNonlifeInsuranceDTO findPolicyNumber(String policyNumber,int clientId);
	public FinexaMessageDto checkAvailPolicyNumber(String policyNumber, int clientId) throws CustomFinexaException,RuntimeException;
	public List<ClientNonlifeInsuranceDTO> findByClientId(int clientId) throws RuntimeException;
	List<ClientNonlifeInsuranceDTO> findAll();
	List<LookupNonlifeInsuranceTypeDTO> findAllNonInsuranceTypeList();
	public List<MasterInsuranceCompanyNameDTO> getClientInsuranceCompanyList() throws RuntimeException;	
	public Set<LookupNonLifeInsurancePolicyTypeDTO> findAllNonInsurancePolicyTypeList(byte insTypeId) throws RuntimeException;	
	List<LookupHealthInsurancePolicyTypeDTO> findAllHealthInsurancePolicyTypeList();	
	public MasterInsurancePolicyDTO ClientNonLifeInsuranceCompanyPolicyName(byte insTypeId,int companyId,
			byte insurancePolicyTypeId);
	public List<ClientNonlifeInsuranceDTO> getPolicyEndDate(int clientId, int timePeriod,int insuranceType);
	//ClientFloaterCoverDTO saveFloaterCover(ClientFloaterCoverDTO clientFloaterCoverDTO);	
	public List<ClientNonlifeInsuranceDTO> getPolicyEndDateForadvisor(int advisorUserID, int timePeriod, int insuranceType);
	public ClientNonlifeInsuranceDTO autoSave(ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO, ClientFloaterCoverDTO clientFloaterCoverDTO);
	public ClientFloaterCoverDTO saveFloaterCover(ClientFloaterCoverDTO clientFloaterCoverDTO) throws RuntimeException;
}