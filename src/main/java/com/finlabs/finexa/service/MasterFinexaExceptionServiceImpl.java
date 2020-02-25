package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientLifeInsuranceDTO;
import com.finlabs.finexa.dto.MasterFinexaExceptionDTO;
import com.finlabs.finexa.model.MasterFinexaException;
import com.finlabs.finexa.repository.MasterFinexaExceptionRepository;

@Service("MasterFinexaExceptionService")
@Transactional
public class MasterFinexaExceptionServiceImpl implements MasterFinexaExceptionService {

	private static Logger log = LoggerFactory.getLogger(MasterFinexaExceptionServiceImpl.class);

	@Autowired
	private Mapper mapper;
	
	@Autowired
	private MasterFinexaExceptionRepository masterFinexaExceptionRepository;

	public Map<String, String> findAllExceptions() {
		List<MasterFinexaException> listMasterFinexaException = masterFinexaExceptionRepository.findAll();
		Map<String, String> exceptionMap = listMasterFinexaException.stream()
				.collect(Collectors.toMap(mfe -> mfe.getErrorCode(), mfe -> mfe.getErrorDescription()));
		return exceptionMap;
	}
	
	@Override
	public MasterFinexaExceptionDTO save(MasterFinexaExceptionDTO masterFinexaExceptionDTO) {
		MasterFinexaException masterFinexaException = mapper.map(masterFinexaExceptionDTO, MasterFinexaException.class);
		masterFinexaException.setErrorCode(masterFinexaExceptionDTO.getErrorCode());
		masterFinexaException.setErrorDescription(masterFinexaExceptionDTO.getErrorDescription());
		
		masterFinexaException = masterFinexaExceptionRepository.save(masterFinexaException);
		return masterFinexaExceptionDTO;
	}

	@Override
	public MasterFinexaExceptionDTO update(MasterFinexaExceptionDTO masterFinexaExceptionDTO) {
		// TODO Auto-generated method stub
		MasterFinexaException masterFinexaException = mapper.map(masterFinexaExceptionDTO, MasterFinexaException.class);
		masterFinexaException.setErrorCode(masterFinexaExceptionDTO.getErrorCode());
		masterFinexaException.setErrorDescription(masterFinexaExceptionDTO.getErrorDescription());
		
		masterFinexaException = masterFinexaExceptionRepository.save(masterFinexaException);
		return masterFinexaExceptionDTO;
	}

	@Override
	public MasterFinexaExceptionDTO findById(int id) {
		// TODO Auto-generated method stub
		MasterFinexaException masterFinexaException = masterFinexaExceptionRepository.findOne(id);
		MasterFinexaExceptionDTO masterFinexaExceptionDTO = mapper.map(masterFinexaException, MasterFinexaExceptionDTO.class);
		masterFinexaExceptionDTO.setId(masterFinexaException.getId());
		masterFinexaExceptionDTO.setErrorCode(masterFinexaException.getErrorCode());
		masterFinexaExceptionDTO.setErrorDescription(masterFinexaException.getErrorDescription());
		return masterFinexaExceptionDTO;
	}

	@Override
	public List<MasterFinexaExceptionDTO> findAll() {
		// TODO Auto-generated method stub
		MasterFinexaExceptionDTO masterFinexaExceptionDTO;
		List<MasterFinexaException> listMasterFinexaException = masterFinexaExceptionRepository.findAll();
		
		List<MasterFinexaExceptionDTO> listDTO = new ArrayList<MasterFinexaExceptionDTO>();
		for (MasterFinexaException masterFinexaException : listMasterFinexaException) {
			masterFinexaExceptionDTO = mapper.map(masterFinexaException, MasterFinexaExceptionDTO.class);
			masterFinexaExceptionDTO.setId(masterFinexaException.getId());
			listDTO.add(masterFinexaExceptionDTO);
		}
		
		return listDTO;
	}

	
	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		if (id != 0) {
			masterFinexaExceptionRepository.delete(id);
			return 1;
		} else {
			return 0;
		}
	}
	
	
	

}