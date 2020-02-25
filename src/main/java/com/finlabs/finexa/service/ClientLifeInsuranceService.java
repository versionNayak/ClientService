package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.ClientLifeInsuranceDTO;
import com.finlabs.finexa.dto.FinexaMessageDto;
import com.finlabs.finexa.dto.MasterInsurancePolicyDTO;
import com.finlabs.finexa.dto.LookupLifeInsurancePolicyTypeDTO; 
import com.finlabs.finexa.dto.MasterInsuranceCompanyNameDTO;
import com.finlabs.finexa.exception.CustomFinexaException;

public interface ClientLifeInsuranceService {
	public ClientLifeInsuranceDTO save(ClientLifeInsuranceDTO clientLifeInsuranceDTO) throws RuntimeException;
	public ClientLifeInsuranceDTO update(ClientLifeInsuranceDTO clientLifeInsuranceDTO) throws RuntimeException;
	public ClientLifeInsuranceDTO findById(int id) throws RuntimeException;
	public List<ClientLifeInsuranceDTO> findAll();
	public List<ClientLifeInsuranceDTO> findByClientId(int clientId) throws RuntimeException;
	public int delete(int id) throws RuntimeException;	
	public List<MasterInsuranceCompanyNameDTO> getClientLifeInsuranceCompanyList() throws RuntimeException;
	public List<LookupLifeInsurancePolicyTypeDTO> getClientLifeInsurancePolicyTypeList() throws RuntimeException;
	public List<MasterInsurancePolicyDTO> ClientLifeInsuranceCompanyPolicyList(int companyid);	
	public MasterInsurancePolicyDTO ClientLifeInsuranceCompanyPolicyName(int companyId, byte insurancePolicyTypeId);
	//public void deleteClientLifeInsurance(int id) throws RuntimeException;
	public FinexaMessageDto checkAvailPolicyNumber(String policyNumber, int clientId) throws CustomFinexaException,RuntimeException;
	public List<ClientLifeInsuranceDTO> getLockedUptoDate(int clientId, int timePeriod);
	public List<ClientLifeInsuranceDTO> getLockedUptoDateTimePeriod(int clientId, int timePeriod);
	public List<ClientLifeInsuranceDTO> getLockedUptoDateForAdvisor(int advisorUserID, int timePeriod);
	public List<ClientLifeInsuranceDTO> getLockedUptoDateTimePeriodForAdvisor(int advisorUserID, int timePeriod);
	public ClientLifeInsuranceDTO autoSave(ClientLifeInsuranceDTO cf);
}