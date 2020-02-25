package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.FinexaExceptionHandlingDTO;
import com.finlabs.finexa.model.FinexaBusinessFunction;
import com.finlabs.finexa.model.FinexaBusinessModule;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.FinexaBusinessFunctionRepository;
import com.finlabs.finexa.repository.FinexaBusinessModuleRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;

@Service("FinexaExceptionHandlingService")
@Transactional
public class FinexaExceptionHandlingServiceImpl implements FinexaExceptionHandlingService {

	private static Logger log = LoggerFactory.getLogger(FinexaExceptionHandlingServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	
	@Autowired
	private FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@Autowired
	private FinexaBusinessModuleRepository finexaBusinessModuleRepository;
	
	@Autowired
	private FinexaBusinessFunctionRepository finexaBusinessFunctionRepository;

	@Override
	public FinexaExceptionHandlingDTO save(FinexaExceptionHandlingDTO finexaExceptionHandlingDTO) {
		// TODO Auto-generated method stub
		FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository.findOne(finexaExceptionHandlingDTO.getSubModuleID());
		FinexaExceptionHandling finexaExceptionHandling = mapper.map(finexaExceptionHandlingDTO, FinexaExceptionHandling.class);
		finexaExceptionHandling.setFinexaBusinessSubmodule(subModule);
		FinexaBusinessFunction function1 = finexaBusinessFunctionRepository.findByFunctionAndSubEvent(finexaExceptionHandlingDTO.getFunctionName(), finexaExceptionHandlingDTO.getFunctionSubEvent());
		if (function1 != null) {
			finexaExceptionHandling.setFinexaBusinessFunction(function1);
		} else {
			FinexaBusinessFunction function2 = finexaBusinessFunctionRepository.findByFunctionAndEvent(finexaExceptionHandlingDTO.getFunctionName(), finexaExceptionHandlingDTO.getFunctionEvent());
			finexaExceptionHandling.setFinexaBusinessFunction(function2);
		}
		finexaExceptionHandling.setErrorCode(finexaExceptionHandlingDTO.getErrorCode());
		finexaExceptionHandling.setErrorMessage(finexaExceptionHandlingDTO.getErrorMessage());
		finexaExceptionHandling = finexaExceptionHandlingRepository.save(finexaExceptionHandling);
		return finexaExceptionHandlingDTO;
	}

	@Override
	public FinexaExceptionHandlingDTO update(FinexaExceptionHandlingDTO finexaExceptionHandlingDTO) {
		// TODO Auto-generated method stub
		FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository.findOne(finexaExceptionHandlingDTO.getSubModuleID());
		FinexaExceptionHandling finexaExceptionHandling = mapper.map(finexaExceptionHandlingDTO, FinexaExceptionHandling.class);
		finexaExceptionHandling.setFinexaBusinessSubmodule(subModule);
		FinexaBusinessFunction function1 = finexaBusinessFunctionRepository.findByFunctionAndSubEvent(finexaExceptionHandlingDTO.getFunctionName(), finexaExceptionHandlingDTO.getFunctionSubEvent());
		if (function1 != null) {
			finexaExceptionHandling.setFinexaBusinessFunction(function1);
		} else {
			FinexaBusinessFunction function2 = finexaBusinessFunctionRepository.findByFunctionAndEvent(finexaExceptionHandlingDTO.getFunctionName(), finexaExceptionHandlingDTO.getFunctionEvent());
			finexaExceptionHandling.setFinexaBusinessFunction(function2);
		}
		finexaExceptionHandling.setErrorCode(finexaExceptionHandlingDTO.getErrorCode());
		finexaExceptionHandling.setErrorMessage(finexaExceptionHandlingDTO.getErrorMessage());
		finexaExceptionHandling = finexaExceptionHandlingRepository.save(finexaExceptionHandling);
		return finexaExceptionHandlingDTO;
	}

	@Override
	public FinexaExceptionHandlingDTO findById(int id) {
		// TODO Auto-generated method stub
		FinexaExceptionHandling finexaExceptionHandling = finexaExceptionHandlingRepository.findOne(id);
		FinexaExceptionHandlingDTO finexaExceptionHandlingDTO = mapper.map(finexaExceptionHandling,
				FinexaExceptionHandlingDTO.class);
		finexaExceptionHandlingDTO.setSubModuleID(finexaExceptionHandling.getFinexaBusinessSubmodule().getId());
		finexaExceptionHandlingDTO.setFunctionID(finexaExceptionHandling.getFinexaBusinessFunction().getId());
		finexaExceptionHandlingDTO.setFunctionName(finexaExceptionHandling.getFinexaBusinessFunction().getFunction());
		finexaExceptionHandlingDTO.setSubFunctionName(finexaExceptionHandling.getFinexaBusinessFunction().getSubFunction());
		finexaExceptionHandlingDTO.setFunctionEvent(finexaExceptionHandling.getFinexaBusinessFunction().getEvent());
		finexaExceptionHandlingDTO.setFunctionSubEvent(finexaExceptionHandling.getFinexaBusinessFunction().getSubEvent());
		finexaExceptionHandlingDTO.setSubModuleName(finexaExceptionHandling.getFinexaBusinessSubmodule().getDescription());
		finexaExceptionHandlingDTO.setModuleName(finexaExceptionHandling.getFinexaBusinessSubmodule().getFinexaBusinessModule().getDescription());
		log.debug("finexaExceptionHandlingDTO: " + finexaExceptionHandlingDTO);
		return finexaExceptionHandlingDTO;
	}

	@Override
	public List<FinexaExceptionHandlingDTO> findAll() {
		// TODO Auto-generated method stub
		FinexaExceptionHandlingDTO finexaExceptionHandlingDTO;
		List<FinexaExceptionHandling> listFinexaExceptionHandling = finexaExceptionHandlingRepository.findAll();

		List<FinexaExceptionHandlingDTO> listDTO = new ArrayList<FinexaExceptionHandlingDTO>();
		for (FinexaExceptionHandling finexaExceptionHandling : listFinexaExceptionHandling) {
			finexaExceptionHandlingDTO = mapper.map(finexaExceptionHandling, FinexaExceptionHandlingDTO.class);
			finexaExceptionHandlingDTO.setId(finexaExceptionHandling.getId());
			finexaExceptionHandlingDTO.setSubModuleID(finexaExceptionHandling.getFinexaBusinessSubmodule().getId());
			finexaExceptionHandlingDTO.setFunctionID(finexaExceptionHandling.getFinexaBusinessFunction().getId());
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository.findOne(finexaExceptionHandling.getFinexaBusinessSubmodule().getId());
			finexaExceptionHandlingDTO.setSubModuleName(subModule.getDescription());
			FinexaBusinessFunction function = finexaBusinessFunctionRepository.findOne(finexaExceptionHandling.getFinexaBusinessFunction().getId());
			finexaExceptionHandlingDTO.setFunctionName(function.getFunction());
			if (function.getSubFunction() != null) {
				finexaExceptionHandlingDTO.setSubFunctionName(function.getSubFunction());
			}
			finexaExceptionHandlingDTO.setFunctionEvent(function.getEvent());
			if (function.getSubEvent() != null) {
				finexaExceptionHandlingDTO.setFunctionSubEvent(function.getSubEvent());
			}
			FinexaBusinessModule module = finexaBusinessModuleRepository.findOne(subModule.getFinexaBusinessModule().getId());
			finexaExceptionHandlingDTO.setModuleName(module.getDescription());
			listDTO.add(finexaExceptionHandlingDTO);
		}

		return listDTO;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		if (id != 0) {
			finexaExceptionHandlingRepository.delete(id);
			return 1;
		} else {
			return 0;
		}
	}

}
