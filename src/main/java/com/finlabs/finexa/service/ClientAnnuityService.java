package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientAnnuityDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.LookupAnnuityTypeDTO;
import com.finlabs.finexa.model.ClientEPF;


public interface ClientAnnuityService {

	public ClientAnnuityDTO save(ClientAnnuityDTO clientAnnuityDTO, HttpServletRequest request);

	public List<ClientAnnuityDTO> findByClientId(int clientId);

	public int delete(int id);
	
	//public int deleteSpecial(int epfId);

	public ClientAnnuityDTO findById(int id);

	public ClientAnnuityDTO update(ClientAnnuityDTO clientAnnuityDTO, HttpServletRequest request);

	public List<ClientAnnuityDTO> findAll();
	
	public List<LookupAnnuityTypeDTO> getAllAnnuityTypes();
	
	public void saveEPSAnnuity(ClientEPF clientEPF);
	
	public void updateEPSAnnuity(ClientEPF clientEPF);
	
	public List<ClientFamilyMemberDTO> checkIfAnnuityPresentForAll(Integer clientId) throws RuntimeException; 

}
