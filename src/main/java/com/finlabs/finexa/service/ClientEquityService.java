package com.finlabs.finexa.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientEquityDTO;
import com.finlabs.finexa.dto.MasterDirectEquityDTO;



public interface ClientEquityService {

	/********** For Equity/ International Equity and ESOP *************/
					/*** for save and update *********/
	public ClientEquityDTO save(ClientEquityDTO clientEquityDTO, HttpServletRequest request) throws RuntimeException;
	
					/********* For Listing ************/
	public List<ClientEquityDTO> findAll();
	
		/********** get particular Equity by Client Id ***********/
	public List<ClientEquityDTO> findByClientId(int clientId) throws RuntimeException;
	
		/********* get Equity By Id ******************/
	public ClientEquityDTO findById(int id) throws RuntimeException;
	
	/**********For deleting *******************/
	public int delete(int id) throws RuntimeException;
	
	public ClientEquityDTO update(ClientEquityDTO clientEquityDTO, HttpServletRequest request) throws RuntimeException;
	
	/********* For Listing ************/
	public List<MasterDirectEquityDTO> securityNameList() throws RuntimeException;

    /********* For calculating from EquityProductCalculator *********/
	public ClientEquityDTO calculateEquityCurrentValue(ClientEquityDTO clientEquityDTO) throws RuntimeException;

	public ClientEquityDTO getClosingPrice(ClientEquityDTO clientEquityDTO)throws RuntimeException;

	public void autoSave(ClientEquityDTO clientEquityDTO) throws RuntimeException;
	
}
