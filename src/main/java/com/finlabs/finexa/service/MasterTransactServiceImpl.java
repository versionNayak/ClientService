package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.MasterTransactSipDTO;
import com.finlabs.finexa.dto.MasterTransactSipLiveDTO;
import com.finlabs.finexa.exception.CustomFinexaException;

import com.finlabs.finexa.model.MasterTransactSIP;
import com.finlabs.finexa.model.MasterTransactSIPLive;

import com.finlabs.finexa.repository.MasterTransactSIPLiveRepository;
import com.finlabs.finexa.repository.MasterTransactSIPRepository;

import com.finlabs.finexa.util.FinexaConstant;


@Service("MasterTransactService")
@Transactional
public class MasterTransactServiceImpl implements MasterTransactService {

	private static Logger log = LoggerFactory.getLogger(MasterTransactServiceImpl.class);

	@Autowired
	private MasterTransactSIPRepository masterTransactSIPRepository;
	
	@Autowired
	private MasterTransactSIPLiveRepository masterTransactSIPLiveRepository;
	
	
	@Autowired
	Mapper mapper;
	
	/*
	@Override
	public List<MasterTransactSipDTO> getAllSIPRecords(String schemeCode,String sipFrequency) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterTransactSIP> masterTransactSipList = masterTransactSIPRepository.findBySchemeCodeAndSipFrequency(schemeCode, sipFrequency);
			
			List<MasterTransactSipDTO> transactSipDtoList = new ArrayList<MasterTransactSipDTO>();
			
			for (MasterTransactSIP masterTransactSipObj : masterTransactSipList) {
				transactSipDtoList.add(mapper.map(masterTransactSipObj, MasterTransactSipDTO.class));
			}
			
			
			return transactSipDtoList;
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	*/
	
	@Override
	public MasterTransactSipDTO getAllSIPRecords(String schemeCode,String sipFrequency) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			MasterTransactSIP masterTransactSipList = masterTransactSIPRepository.findBySchemeCodeAndSipFrequency(schemeCode, sipFrequency);
			
			MasterTransactSipDTO transactSipDtoList = new MasterTransactSipDTO();
			
			if (masterTransactSipList != null) {
				transactSipDtoList = mapper.map(masterTransactSipList, MasterTransactSipDTO.class);
			}
			
			/*for (MasterTransactSIP masterTransactSipObj : masterTransactSipList) {
				transactSipDtoList.add(mapper.map(masterTransactSipObj, MasterTransactSipDTO.class));
			}*/
			
			
			return transactSipDtoList;
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public MasterTransactSipLiveDTO getAllSIPLiveRecords(String schemeCode,String sipFrequency) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			MasterTransactSIPLive masterTransactSipLiveList = masterTransactSIPLiveRepository.findBySchemeCodeAndSipFrequency(schemeCode, sipFrequency);
			
			MasterTransactSipLiveDTO masterTransactSipLiveDTOList = new MasterTransactSipLiveDTO();
			
			/*for (MasterTransactSIP masterTransactSipObj : masterTransactSipList) {
				transactSipDtoList.add(mapper.map(masterTransactSipObj, MasterTransactSipDTO.class));
			}*/
			
			if (masterTransactSipLiveList != null) {
				masterTransactSipLiveDTOList = mapper.map(masterTransactSipLiveList, MasterTransactSipLiveDTO.class);
			}
			
			return masterTransactSipLiveDTOList;
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	
	
}
