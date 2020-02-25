package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.MasterTablesUploadHistoryDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.MasterTablesUploadHistory;
import com.finlabs.finexa.repository.AdvisorUserRepository;

@Service("MasterUploadHistoryService")
@Transactional
public class MasterUploadHistoryServiceImpl implements MasterUploadHistoryService {
	
	private static Logger log = LoggerFactory.getLogger(MasterUploadHistoryServiceImpl.class);

	@Autowired
	Mapper mapper;
	
	@Autowired
	private AdvisorUserRepository advisorUserRepository;
	
	@Override
	public List<MasterTablesUploadHistoryDTO> findByLoggedInId(int loggedId) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			log.debug("inside findByLoggedInId");
			AdvisorUser au = advisorUserRepository.findOne(loggedId);
			List<MasterTablesUploadHistoryDTO> listDTO = new ArrayList<MasterTablesUploadHistoryDTO>();
			for (MasterTablesUploadHistory masterTablesUploadHistory : au.getMasterTablesUploadHistories()) {
				MasterTablesUploadHistoryDTO dto = mapper.map(masterTablesUploadHistory, MasterTablesUploadHistoryDTO.class);
				dto.setUploadedBy(loggedId);
				dto.setUploadedByName(masterTablesUploadHistory.getAdvisorUser().getFirstName());
				dto.setUploadDate(masterTablesUploadHistory.getUploadDate());
				listDTO.add(dto);
			}
			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}
