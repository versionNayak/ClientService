package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientNpsDTO;
import com.finlabs.finexa.dto.ClientNPSAssetClassReturnDTO;
import com.finlabs.finexa.dto.LookupNPSPlanTypeDTO;


public interface ClientNPSService {

	public ClientNpsDTO save(ClientNpsDTO clientNPSDTO, HttpServletRequest request) throws RuntimeException;

	public List<ClientNpsDTO> findByClientId(int clientId);

	public int delete(int id);

	public ClientNpsDTO findById(int id);

	public ClientNpsDTO update(ClientNpsDTO clientNPSDTO, HttpServletRequest request);

	public List<ClientNpsDTO> findAll();
	
	public List<LookupNPSPlanTypeDTO> findAllNPSType() throws RuntimeException;

	public double getCAGRByIncomeType(int incomeType);

	public ClientNPSAssetClassReturnDTO getAssetClassReturn();

	//public List<ClientFamilyMemberDTO> checkIfNpsPresentForAll(int clientId);

	boolean checkIfNpsPresent(Integer clientId);

	List<ClientFamilyMemberDTO> checkIfNpsPresentForAll(Integer clientId);

	

}
