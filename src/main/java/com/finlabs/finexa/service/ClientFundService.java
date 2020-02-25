package com.finlabs.finexa.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientMutualFundDTO;

public interface ClientFundService {

	/********** For Mutual Fund/ ETF/ PMS / AIF-Cat III *************/
	public ClientMutualFundDTO save(ClientMutualFundDTO clientMutualFundDTO, HttpServletRequest request) throws RuntimeException;

	public List<ClientMutualFundDTO> findAll();

	public ClientMutualFundDTO update(ClientMutualFundDTO clientMutualFundDTO, HttpServletRequest request) throws RuntimeException;

	public LinkedList<ClientMutualFundDTO> findByClientId(int clientId) throws RuntimeException;

	public int deleteClientMutualFund(int id) throws RuntimeException;

	public ClientMutualFundDTO findById(int id) throws RuntimeException;

	public Double getNAV(Date investmentDate, String isin) throws RuntimeException;

	public Double getLatestNAV(String isin) throws RuntimeException;

	public void autoSave(ClientMutualFundDTO clientMutualFundDTO) throws RuntimeException;
	
}
