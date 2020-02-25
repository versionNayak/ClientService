package com.finlabs.finexa.service;

import java.util.List;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientLifeExpDTO;
import com.finlabs.finexa.dto.ClientLifeInsuranceDTO;
import com.finlabs.finexa.dto.LifeExpectancyDTO;
import com.finlabs.finexa.exception.CustomFinexaException;


public interface ClientLifeExpectancyService {
	
	public LifeExpectancyDTO save(LifeExpectancyDTO lifeExpectancyDTO) throws RuntimeException;
	
	public LifeExpectancyDTO update(LifeExpectancyDTO lifeExpectancyDTO) throws RuntimeException;
	
	public LifeExpectancyDTO findByMemberId(int memberId) throws RuntimeException, CustomFinexaException;
	
	public LifeExpectancyDTO calculateLifeExp(LifeExpectancyDTO lifeExpectancyDTO) throws RuntimeException, CustomFinexaException;

	public List <ClientLifeExpDTO> viewLifeExpList(Integer inClientId) throws RuntimeException;
	
	public List<ClientFamilyMemberDTO> getFamilyMemberByLifeExpectancy(int clientId) throws RuntimeException;
	
	public List<ClientLifeExpDTO> deleteLifeExp(int familyMemberId) throws RuntimeException;
	
	//public LifeExpectancyDTO reCalculateLifeExp(LifeExpectancyDTO lifeExpectancyDTO);
	public LifeExpectancyDTO reCalculateLifeExpectancyForClient(LifeExpectancyDTO lifeExpectancyDTO) throws RuntimeException, CustomFinexaException;
	public LifeExpectancyDTO reCalculateLifeExpectancyForFamilyMember(ClientFamilyMemberDTO clientFamilyMemberDTO) throws RuntimeException, CustomFinexaException;
	
}
