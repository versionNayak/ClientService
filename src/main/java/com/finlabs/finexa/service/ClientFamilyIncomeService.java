package com.finlabs.finexa.service;



import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientFamilyIncomeDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.LifeExpectancyDTO;
import com.finlabs.finexa.exception.CustomFinexaException;



public interface ClientFamilyIncomeService{
	public void save(List<ClientFamilyIncomeDTO> clientFamilyIncomeList, HttpServletRequest request) throws RuntimeException, CustomFinexaException;
	public void update(List<ClientFamilyIncomeDTO> clientFamilyIncomeList, HttpServletRequest request) throws RuntimeException, CustomFinexaException;
    public List <ClientFamilyIncomeDTO> getAllFamilyIncome(Integer inClientId) throws RuntimeException;
	public List<ClientFamilyIncomeDTO> getAllIncomeForFamilyMember(Integer clientId, Integer familyMemberId) throws RuntimeException;
//	boolean checkIfIndividualIncomeHeadPresent(Integer clientId, Integer familyMemberId, Integer incomeType);
//	public boolean checkIfTotalIncomeHeadPresent(Integer inClientId, Integer familyMemberId, Integer incomeType);
	public List<ClientFamilyMemberDTO> checkIfIncomePresentForAll(Integer clientId) throws RuntimeException;
	public boolean checkIfIncomePresent(Integer clientId) throws RuntimeException;
	void deleteIncomeForMember(Integer clientId, Integer familyMemberId, HttpServletRequest request) throws RuntimeException, CustomFinexaException;
	void delete(Integer id);
	public void saveFamilyIncomeAfterChangeInLifeExp(LifeExpectancyDTO lifeExpectancyDTO) throws RuntimeException;
	public boolean getIncomeByClientID(Integer cliendID) throws CustomFinexaException;
	public ClientFamilyIncomeDTO autoSave(ClientFamilyIncomeDTO clientFamilyIncomeDTO) throws RuntimeException, CustomFinexaException;

	
	
}
