package com.finlabs.finexa.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.finlabs.finexa.dto.AdvisorUserDTO;
import com.finlabs.finexa.dto.ClientCashDTO;
import com.finlabs.finexa.dto.SubBrokerMasterDTO;

public interface SubBrokerMasterBOService {

	public SubBrokerMasterDTO save(SubBrokerMasterDTO subBrokerMasterDTO) throws RuntimeException, IOException, InvalidFormatException, ParseException;

	public List<SubBrokerMasterDTO> findByAdvisorId(int advisorID);

	//SubBrokerMasterDTO finndById(int id) throws RuntimeException;

	public int delete(int id) throws RuntimeException;

	public SubBrokerMasterDTO findById(int id) throws RuntimeException;

	public SubBrokerMasterDTO update(SubBrokerMasterDTO subBrokerMasterDTO);

	public List<AdvisorUserDTO> getSubBrokerUsersNameForParticularBranch(int branchId) throws RuntimeException;

	public boolean checkIfSbRoleExists(int advisorID);
}


