package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.dto.RoleDTO;
import com.finlabs.finexa.dto.UserDTO;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.User;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.AdvisorRoleRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	private static Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
	
	@Autowired
	Mapper mapper;
	@Autowired
	AdvisorMasterRepository advisorMasterRepository;
	@Autowired
	AdvisorRoleRepository advisorRoleRepository;
	@Autowired
	AdvisorUserRepository advisorUserRepository;


	@Override
	public RoleDTO save(RoleDTO roleDTO) {
		// TODO Auto-generated method stub
		
		AdvisorRole advRole = mapper.map(roleDTO, AdvisorRole.class);
		log.debug("advRole = " + advRole);
		
		AdvisorMaster advMaster = advisorMasterRepository.findOne(9);
		advRole.setAdvisorMaster(advMaster);
		AdvisorRole retRole = advisorRoleRepository.save(advRole);
		
		RoleDTO retDTO = mapper.map(advRole, RoleDTO.class);
	//	mapper.map(advMaster, retDTO);
		retDTO.setAdvisorID(advMaster.getId());
		return retDTO;
	}

	@Override
	public RoleDTO update(RoleDTO roleDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserDTO findUserByAdvisorId(int id) {
		AdvisorUser advisorUser = advisorUserRepository.findOne(id);
		User user = advisorUser.getUser();
		UserDTO userDTO = new UserDTO();
		userDTO.setAdmin(user.getAdmin());
		userDTO.setAdvisorAdmin(user.getAdvisorAdmin());
		userDTO.setClientInfoView(user.getClientInfoView());
		userDTO.setClientInfoAddEdit(user.getClientInfoAddEdit());
		userDTO.setClientInfoDelete(user.getClientInfoDelete());
		userDTO.setBudgetManagementView(user.getBudgetManagementView());
		userDTO.setGoalPlanningView(user.getGoalPlanningView());
		userDTO.setGoalPlanningAddEdit(user.getGoalPlanningAddEdit());
		userDTO.setPortfolioManagementView(user.getPortfolioManagementView());
		userDTO.setPortfolioManagementAddEdit(user.getPortfolioManagementAddEdit());
		userDTO.setFinancialPlanningView(user.getFinancialPlanningView());
		userDTO.setFinancialPlanningAddEdit(user.getFinancialPlanningAddEdit());
		userDTO.setUserManagementView(user.getUserManagementView());
		userDTO.setUserManagementAddEdit(user.getUserManagementAddEdit());
		userDTO.setUserManagementDelete(user.getUserManagementDelete());
		userDTO.setInvestView(user.getInvestView());
		userDTO.setInvestAddEdit(user.getInvestAddEdit());
		userDTO.setMastersView(user.getMastersView());
		userDTO.setMastersAddEdit(user.getMastersAddEdit());
		userDTO.setMastersDelete(user.getMastersDelete());
		userDTO.setClientRecordsView(user.getClientRecordsView());
		userDTO.setMfBackOfficeView(user.getMfBackOfficeView());
		userDTO.setMfBackOfficeAddEdit(user.getMfBackOfficeAddEdit());
		//userDTO.setBseMFView(user.getBseMFView());
		return userDTO;
	}

	@Override
	public List<RoleDTO> getAllRoles() {
		// TODO Auto-generated method stub
		List<AdvisorRole> listRoles = advisorRoleRepository.findAll();
		log.debug("listRoles: " + listRoles);
	
		List<RoleDTO> listDTO=new ArrayList<RoleDTO>();
		for(AdvisorRole advisorRole : listRoles){
			listDTO.add(mapper.map(advisorRole, RoleDTO.class));
		}
		
		return listDTO;
	}

	@Override
	public RoleDTO findById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
