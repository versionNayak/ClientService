package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.FinexaImpactAnalysisDTO;
import com.finlabs.finexa.model.FinexaImpactAnalysis;
import com.finlabs.finexa.repository.FinexaImpactAnalysisRepository;

@Service("FinexaImpactAnalysisService")
@Transactional
public class FinexaImpactAnalysisServiceImpl implements FinexaImpactAnalysisService {

	private static Logger log = LoggerFactory.getLogger(FinexaImpactAnalysisServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private FinexaImpactAnalysisRepository finexaImpactAnalysisRepository;

	@Override
	public FinexaImpactAnalysisDTO save(FinexaImpactAnalysisDTO finexaImpactAnalysisDTO) {
		// TODO Auto-generated method stub
		FinexaImpactAnalysis finexaImpactAnalysis = mapper.map(finexaImpactAnalysisDTO, FinexaImpactAnalysis.class);
		finexaImpactAnalysis.setChanges(finexaImpactAnalysisDTO.getChanges());
		finexaImpactAnalysis.setController(finexaImpactAnalysisDTO.getController());
		finexaImpactAnalysis.setImpact(finexaImpactAnalysisDTO.getImpact());
		finexaImpactAnalysis.setMasterTableName(finexaImpactAnalysisDTO.getMasterTableName());
		finexaImpactAnalysis.setMethod(finexaImpactAnalysisDTO.getMethod());
		finexaImpactAnalysis.setModuleName(finexaImpactAnalysisDTO.getModuleName());
		finexaImpactAnalysis.setServiceImpl(finexaImpactAnalysisDTO.getServiceImpl());
		finexaImpactAnalysis.setSubModuleName(finexaImpactAnalysisDTO.getSubModuleName());
		finexaImpactAnalysis.setRepository(finexaImpactAnalysisDTO.getRepository());
		finexaImpactAnalysis = finexaImpactAnalysisRepository.save(finexaImpactAnalysis);
		return finexaImpactAnalysisDTO;
	}

	@Override
	public FinexaImpactAnalysisDTO update(FinexaImpactAnalysisDTO finexaImpactAnalysisDTO) {
		// TODO Auto-generated method stub
		FinexaImpactAnalysis finexaImpactAnalysis = mapper.map(finexaImpactAnalysisDTO, FinexaImpactAnalysis.class);
		finexaImpactAnalysis.setChanges(finexaImpactAnalysisDTO.getChanges());
		finexaImpactAnalysis.setController(finexaImpactAnalysisDTO.getController());
		finexaImpactAnalysis.setImpact(finexaImpactAnalysisDTO.getImpact());
		finexaImpactAnalysis.setMasterTableName(finexaImpactAnalysisDTO.getMasterTableName());
		finexaImpactAnalysis.setMethod(finexaImpactAnalysisDTO.getMethod());
		finexaImpactAnalysis.setModuleName(finexaImpactAnalysisDTO.getModuleName());
		finexaImpactAnalysis.setServiceImpl(finexaImpactAnalysisDTO.getServiceImpl());
		finexaImpactAnalysis.setSubModuleName(finexaImpactAnalysisDTO.getSubModuleName());
		finexaImpactAnalysis.setRepository(finexaImpactAnalysisDTO.getRepository());
		finexaImpactAnalysis = finexaImpactAnalysisRepository.save(finexaImpactAnalysis);
		return finexaImpactAnalysisDTO;
	}

	@Override
	public FinexaImpactAnalysisDTO findById(int id) {
		// TODO Auto-generated method stub
		FinexaImpactAnalysis finexaImpactAnalysis = finexaImpactAnalysisRepository.findOne(id);
		FinexaImpactAnalysisDTO finexaImpactAnalysisDTO = mapper.map(finexaImpactAnalysis, FinexaImpactAnalysisDTO.class);
		finexaImpactAnalysisDTO.setChanges(finexaImpactAnalysis.getChanges());
		finexaImpactAnalysisDTO.setController(finexaImpactAnalysis.getController());
		finexaImpactAnalysisDTO.setImpact(finexaImpactAnalysis.getImpact());
		finexaImpactAnalysisDTO.setMasterTableName(finexaImpactAnalysis.getMasterTableName());
		finexaImpactAnalysisDTO.setMethod(finexaImpactAnalysis.getMethod());
		finexaImpactAnalysisDTO.setModuleName(finexaImpactAnalysis.getModuleName());
		finexaImpactAnalysisDTO.setServiceImpl(finexaImpactAnalysis.getServiceImpl());
		finexaImpactAnalysisDTO.setSubModuleName(finexaImpactAnalysis.getSubModuleName());
		finexaImpactAnalysisDTO.setRepository(finexaImpactAnalysis.getRepository());
		return finexaImpactAnalysisDTO;
	}

	@Override
	public List<FinexaImpactAnalysisDTO> findAll() {
		// TODO Auto-generated method stub
		FinexaImpactAnalysisDTO finexaImpactAnalysisDTO;
		List<FinexaImpactAnalysis> listFinexaImpactAnalysis = finexaImpactAnalysisRepository.findAll();

		List<FinexaImpactAnalysisDTO> listDTO = new ArrayList<FinexaImpactAnalysisDTO>();
		for (FinexaImpactAnalysis finexaImpactAnalysis : listFinexaImpactAnalysis) {
			finexaImpactAnalysisDTO = mapper.map(finexaImpactAnalysis, FinexaImpactAnalysisDTO.class);
			finexaImpactAnalysisDTO.setId(finexaImpactAnalysis.getId());
			finexaImpactAnalysisDTO.setChanges(finexaImpactAnalysis.getChanges());
			finexaImpactAnalysisDTO.setController(finexaImpactAnalysis.getController());
			finexaImpactAnalysisDTO.setImpact(finexaImpactAnalysis.getImpact());
			finexaImpactAnalysisDTO.setMasterTableName(finexaImpactAnalysis.getMasterTableName());
			finexaImpactAnalysisDTO.setMethod(finexaImpactAnalysis.getMethod());
			finexaImpactAnalysisDTO.setModuleName(finexaImpactAnalysis.getModuleName());
			finexaImpactAnalysisDTO.setSubModuleName(finexaImpactAnalysis.getSubModuleName());
			finexaImpactAnalysisDTO.setServiceImpl(finexaImpactAnalysis.getServiceImpl());
			finexaImpactAnalysisDTO.setRepository(finexaImpactAnalysis.getRepository());
			listDTO.add(finexaImpactAnalysisDTO);
		}

		return listDTO;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
