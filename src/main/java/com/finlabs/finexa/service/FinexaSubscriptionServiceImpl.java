package com.finlabs.finexa.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.dozer.MappingException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.FinexaSubscriptionDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.FinexaBusinessModule;
import com.finlabs.finexa.model.FinexaSubscription;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.FinexaBusinessModuleRepository;
import com.finlabs.finexa.repository.FinexaSubscriptionRepository;

@Service("FinexaSubscriptionService")
@Transactional
public class FinexaSubscriptionServiceImpl implements FinexaSubscriptionService{

private static Logger log = LoggerFactory.getLogger(FinexaSubscriptionServiceImpl.class);
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private FinexaSubscriptionRepository finexaSubscriptonRepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;	
	
	@Autowired
	FinexaBusinessModuleRepository finexaBusinessModuleRepository;

	@Override
	public FinexaSubscriptionDTO save(FinexaSubscriptionDTO finexaSubscriptionDTO) throws RuntimeException{
		try {
			log.debug("in save");
			 for(String moduleCode : finexaSubscriptionDTO.getModules()){
				 AdvisorUser advUser = advisorUserRepository.findOne(finexaSubscriptionDTO.getAdvisorId());
				 FinexaSubscription finexaSubscription = mapper.map(finexaSubscriptionDTO, FinexaSubscription.class);
				 finexaSubscription.setAdvisorMaster(advUser.getAdvisorMaster());

				 FinexaBusinessModule fbm = finexaBusinessModuleRepository.findByCode(moduleCode);
				 finexaSubscription.setFinexaBusinessModule(fbm);
				 finexaSubscription.setDateOfSubscription(new Date());
				 finexaSubscriptonRepository.save(finexaSubscription);
			 }
			 return finexaSubscriptionDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<FinexaSubscriptionDTO> getAllSubscriptionByAdvisor(int advisorId) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			AdvisorUser advUser = advisorUserRepository.findOne(advisorId);
			List<FinexaSubscription> list = finexaSubscriptonRepository.findByAdvisorMaster(advUser.getAdvisorMaster());
			List<FinexaSubscriptionDTO> dtoList = new ArrayList<FinexaSubscriptionDTO>();
			for(FinexaSubscription obj : list){
				FinexaSubscriptionDTO dto = mapper.map(obj, FinexaSubscriptionDTO.class);
				dto.setAdvisorId(advisorId);
				FinexaBusinessModule fbm = obj.getFinexaBusinessModule();
				List<String> modules = new ArrayList<String>();
				modules.add(fbm.getCode());
				dto.setModules(modules);

				dto.setModuleName(fbm.getDescription());
				DateTime dt = new DateTime(obj.getDateOfSubscription());
				DateTime expDate = dt.plusYears(obj.getSubscriptionPeriod());
				dto.setDateOfExpairy(expDate.toDate());
				dtoList.add(dto);
			}
			
			
			return dtoList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public FinexaSubscriptionDTO getSubscriptionById(int subId) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			FinexaSubscription finexaSubscription = finexaSubscriptonRepository.findOne(subId);
			
			FinexaSubscriptionDTO dto = mapper.map(finexaSubscription, FinexaSubscriptionDTO.class);
			dto.setAdvisorId(finexaSubscription.getAdvisorMaster().getId());
			List<String> modules = new ArrayList<String>();
			modules.add(finexaSubscription.getFinexaBusinessModule().getCode());
			dto.setModules(modules);
			dto.setModuleName(finexaSubscription.getFinexaBusinessModule().getDescription());
			DateTime dt = new DateTime(finexaSubscription.getDateOfSubscription());
			DateTime expDate = dt.plusYears(finexaSubscription.getSubscriptionPeriod());
			dto.setDateOfExpairy(expDate.toDate());
			return dto;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	
	
}
