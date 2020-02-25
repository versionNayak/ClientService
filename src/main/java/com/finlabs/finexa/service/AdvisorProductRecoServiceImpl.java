package com.finlabs.finexa.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.dozer.MappingException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.AdvisorProductRecoDTO;
import com.finlabs.finexa.model.AdvisorProductReco;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientGoal;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.repository.AdvisorProductRecoRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.ClientGoalRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.service.AdvisorProductRecoService;

@Service("AdvisorProductRecoService")
@Transactional
public class AdvisorProductRecoServiceImpl implements AdvisorProductRecoService {
	private static Logger log = LoggerFactory.getLogger(AdvisorProductRecoServiceImpl.class);
	
	@Autowired
	Mapper mapper;
	@Autowired
	AdvisorProductRecoRepository advisorProductRecoRepository;
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	@Autowired
	ClientMasterRepository clientMasterRepository;
	@Autowired
	ClientGoalRepository clientGoalRepository;
	/*@Override
	public Date save(AdvisorProductRecoDTO dto,Session session) throws FinexaBussinessException {
		AdvisorProductRecoRepository advisorProductRecoRepository = new AdvisorProductRecoDaoImpl();
		try {

			try {
				Date date = advisorProductRecoRepository.saveOrUpdate(dto,session);
				return date;
			} catch (Exception e) {
				return null;
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch bloc
			return null;
		}
	}

	@Override
	public List<AdvisorProductRecoDTO> getAllProductRecoByAdvisorId(int advisorID, Session session)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub
		AdvisorProductRecoRepository advisorProductRecoRepository = new AdvisorProductRecoDaoImpl();
         List<AdvisorProductReco> recolist = null;
         List<AdvisorProductRecoDTO> recoDtoList = new ArrayList<>();
		try {
			recolist = advisorProductRecoRepository.getAllProductRecoByAdvisorId(advisorID, session);
			for (AdvisorProductReco obj : recolist) {
				AdvisorProductRecoDTO dtoObj = new AdvisorProductRecoDTO();
				dtoObj.setAdviorID(obj.getAdvisorUser().getId());
				dtoObj.setDate(obj.getRecoSaveDate());
				dtoObj.setModule(obj.getModule());
				String dateInString = "";
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
				dateInString = sdf.format(obj.getRecoSaveDate());
				dtoObj.setDateString(dateInString);
				String s = new String(obj.getProductPlan());
				dtoObj.setProductPlan(s);
				recoDtoList.add(dtoObj);
			}
			
		} catch (Exception e) {
			throw new FinexaBussinessException("FP", "FPPRECO", "cannot get the productreco");
		}

		return recoDtoList;
	}*/
	/*@Override
	public Date save() {
		
		AdvisorProductRecoDTO advisorProductRecoDTO = mapper.map(advisorProductRecoDTO, AdvisorProductRecoDTO.class);
		advisorProductRecoRepository.save(advisorProductRecoDTO)
		return null;
	}*/
	@Override
	public List<AdvisorProductRecoDTO> getAllProductRecoByAdvisorId(int advisorID,int clientID, int goalID, String module) {
		
		AdvisorUser advisorUser = advisorUserRepository.findOne(advisorID);
		ClientMaster clientMaster = clientMasterRepository.findOne(clientID);
		List<AdvisorProductReco> advisorProductRecoList = null;
		if(module.equals("GP") || module.equals("FPGE")) {
		ClientGoal clientGoal = clientGoalRepository.findOne(goalID);
		advisorProductRecoList = advisorProductRecoRepository.findByAdvisorUserAndClientMasterAndClientGoalAndModule(advisorUser, clientMaster, clientGoal, module);
		}
		if(module.equals("PM") || module.equals("FP")) {
		advisorProductRecoList = advisorProductRecoRepository.findByAdvisorUserAndClientMasterAndModule(advisorUser, clientMaster,module);
		}
		List<AdvisorProductRecoDTO> advisorProductRecoDTOList = new ArrayList<AdvisorProductRecoDTO>();
		try {
			for (AdvisorProductReco advisorProductReco : advisorProductRecoList) {
				
				if((advisorProductReco.getModule().equals(module)) && (advisorProductReco.getAdvisorUser().getId() == advisorID) && (advisorProductReco.getClientMaster().getId() == clientID)) {
				AdvisorProductRecoDTO advisorProductRecoDTO = mapper.map(advisorProductReco, AdvisorProductRecoDTO.class);
				advisorProductRecoDTO.setAdviorID(advisorID);
				advisorProductRecoDTO.setClientID(clientID);
				if(module.equals("GP") || module.equals("FPGE")) {
				advisorProductRecoDTO.setGoalID(goalID);
				}
				advisorProductRecoDTO.setProductPlan("");
				advisorProductRecoDTOList .add(advisorProductRecoDTO);
				}
			}
			Collections.sort(advisorProductRecoDTOList, new Comparator<AdvisorProductRecoDTO>() {
				public int compare(AdvisorProductRecoDTO o1, AdvisorProductRecoDTO o2) {
					return (o1.getRecoSaveDate()).compareTo(o2.getRecoSaveDate());
				}
			});
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return advisorProductRecoDTOList;
	}
	@Override
	public AdvisorProductRecoDTO save(int advisorID, int clientID, int goalID, String module, String jsonData) {
		
		AdvisorProductRecoDTO advisorProductRecoDTO = new AdvisorProductRecoDTO();
		Date currentdate = new Date();
		//Date productRecodate;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			
			AdvisorUser advisorUser = advisorUserRepository.findOne(advisorID);
			ClientMaster clientMaster = clientMasterRepository.findOne(clientID);
			List<AdvisorProductReco> advisorProductRecoList = null;
			AdvisorProductReco advisorProductReco = null;
			
			if(module.equals("GP") || module.equals("FPGE")) {
				ClientGoal clientGoal = clientGoalRepository.findOne(goalID);
				advisorProductRecoList = advisorProductRecoRepository.findByAdvisorUserAndClientMasterAndClientGoalAndModule(advisorUser, clientMaster, clientGoal, module);
			}
				if(module.equals("PM") || module.equals("FP")) {
				advisorProductRecoList = advisorProductRecoRepository.findByAdvisorUserAndClientMasterAndModule(advisorUser, clientMaster,module);
			}
           
			for (AdvisorProductReco advisorProductOb : advisorProductRecoList) {
				
				if((sdf.format(advisorProductOb.getRecoSaveDate())).equals(sdf.format(currentdate))) {
					advisorProductRecoRepository.delete(advisorProductOb.getId());
					
					break;
				}
			}
			   
				advisorProductRecoDTO.setModule(module); 
				advisorProductRecoDTO.setProductPlan(jsonData);
				advisorProductRecoDTO.setRecoSaveDate(new Date());
			    advisorProductReco = mapper.map(advisorProductRecoDTO,AdvisorProductReco.class);
				advisorProductReco.setAdvisorUser(advisorUser);
				advisorProductReco.setClientMaster(clientMasterRepository.findOne(clientID));
				if(module.equals("GP") || module.equals("FPGE")) {
				advisorProductReco.setClientGoal(clientGoalRepository.findOne(goalID));
				}
			   advisorProductReco = advisorProductRecoRepository.save(advisorProductReco);
			   advisorProductRecoDTO = mapper.map(advisorProductReco,AdvisorProductRecoDTO.class);
			 //  advisorProductRecoDTO.setOption(flag);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	
		return advisorProductRecoDTO;
	}
	
}
