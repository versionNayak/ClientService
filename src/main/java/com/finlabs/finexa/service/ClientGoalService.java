package com.finlabs.finexa.service;

import java.util.Date;
import java.util.List;

import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientGoalDTO;
import com.finlabs.finexa.dto.MasterGoalInflationRateDTO;
import com.finlabs.finexa.dto.MasterSubAssetClassReturnDTO;
import com.finlabs.finexa.model.ClientExpense;
import com.finlabs.finexa.model.ClientMaster;


public interface ClientGoalService {
	public ClientGoalDTO save(ClientGoalDTO clientGoalDTO) throws RuntimeException;
	
	public int delete(int id) throws RuntimeException;
	
	public ClientGoalDTO findById(int id) throws RuntimeException;
	
	public List<ClientGoalDTO> findByClientId(int clientId) throws RuntimeException;

	public int getMaxPriority(int clientId) throws RuntimeException;

	public ClientFamilyMemberDTO getDate(int clientId) throws RuntimeException;

	public int getAge(int familymenberId) throws RuntimeException;

	public int getLifeExpectency(int clientId) throws RuntimeException;

	public MasterGoalInflationRateDTO getInflationRate(byte goalTypeId, Date date) throws RuntimeException;

	public MasterSubAssetClassReturnDTO getCorpusPostGoalStart() throws RuntimeException;

	public ClientGoalDTO update(ClientGoalDTO clientGoalDTO) throws RuntimeException;
	
	public ClientGoalDTO UpdatePriority(ClientGoalDTO clientGoalDTO) throws RuntimeException;
	
	public List<ClientGoalDTO> fetchExpiredGoals(int clientId) throws RuntimeException;
	
	public ClientGoalDTO getAgePlusRetirementAge(int familyMemberId) throws RuntimeException;
	
	public List<ClientGoalDTO> findAllUpcomingGoalsByClientID(int userId,int value) throws RuntimeException; 
	
	public List<ClientGoalDTO> checkIfRetirementGoalPresentForAll(Integer clientId, Integer goalType) throws RuntimeException;
	
	public void updateClientGoal(ClientExpense clientExpense);
	
	public boolean checkIfRetirementGoalExists(int clientid);
	
	public void updateGoalStartMonthYear(ClientMaster clientMaster);

	public ClientGoalDTO autoSave(ClientGoalDTO clientGoalDTO) throws RuntimeException;
	
}
