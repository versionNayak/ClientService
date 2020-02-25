package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.ClientInfoDTO;
import com.finlabs.finexa.dto.InvestorBranchMasterBODTO;
import com.finlabs.finexa.dto.RmMasterBODTO;
import com.finlabs.finexa.dto.ViewClientUCCDetailsDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.InvestorBranchMasterBO;
import com.finlabs.finexa.model.RmMasterBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.InvestorBranchMasterBORepository;

@Service("InvestorBranchMasterBOService")
@Transactional
public class InvestorBranchMasterBOServiceImpl implements InvestorBranchMasterBOService {

	@Autowired
	Mapper mapper;
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	@Autowired
	InvestorBranchMasterBORepository investorBranchMasterBORepository;
	
	@Override
	public InvestorBranchMasterBODTO save(InvestorBranchMasterBODTO investorBranchMasterBODTO) {
		InvestorBranchMasterBO investorBranchMasterBO = mapper.map(investorBranchMasterBODTO, InvestorBranchMasterBO.class);
		AdvisorUser advisorUser = advisorUserRepository.findOne(investorBranchMasterBODTO.getAdvisorId());
		investorBranchMasterBO.setAdvisorUser(advisorUser);
		investorBranchMasterBO.setBranchAddress(investorBranchMasterBODTO.getBranchAddress());
		investorBranchMasterBO.setBranchCity(investorBranchMasterBODTO.getBranchCity());
		investorBranchMasterBO.setBranchCode(investorBranchMasterBODTO.getBranchCode());
		investorBranchMasterBO.setBranchHead(investorBranchMasterBODTO.getBranchHead());
		investorBranchMasterBO.setBranchMobileNo(investorBranchMasterBODTO.getBranchMobileNo());
		investorBranchMasterBO.setBranchName(investorBranchMasterBODTO.getBranchName());
		investorBranchMasterBO.setBranchPhoneNo(investorBranchMasterBODTO.getBranchPhoneNo());
		investorBranchMasterBO.setBranchPinCode(investorBranchMasterBODTO.getBranchPinCode());
		investorBranchMasterBO.setBranchState(investorBranchMasterBODTO.getBranchState());
		investorBranchMasterBO = investorBranchMasterBORepository.save(investorBranchMasterBO);
		return investorBranchMasterBODTO;
		
	}
	
//	
//	@Override
//	public List<InvestorBranchMasterBODTO> getAllMFBackOfficeBranch() {
//		// TODO Auto-generated method stub
//		List<InvestorBranchMasterBO> investorBranchMasterBOList = investorBranchMasterBORepository.findAll();
//		//log.debug("clientMaster list size = " + list.size());
//		List<InvestorBranchMasterBODTO> investorBranchMasterBODTOList = new ArrayList<InvestorBranchMasterBODTO>();
//		for (InvestorBranchMasterBO investorBranchMasterBO : investorBranchMasterBOList) {
//			
//			InvestorBranchMasterBODTO investorBranchMasterBODTO = mapper.map(investorBranchMasterBO, InvestorBranchMasterBODTO.class);
//			
//			investorBranchMasterBODTOList.add(investorBranchMasterBODTO);
//		}
//		return investorBranchMasterBODTOList;
//	}
//	
	
	/*@Override
	public List<InvestorBranchMasterBODTO> getAllMFBackOfficeBranchByAdvisorId(int advisorId) {
		// TODO Auto-generated method stub
		AdvisorUser advisorUser = advisorUserRepository.findOne(advisorId);
		List<InvestorBranchMasterBO> investorBranchMasterBOList = investorBranchMasterBORepository.findByAdvisorUser(advisorUser);
		List<InvestorBranchMasterBODTO> investorBranchMasterBODTOList = new ArrayList<InvestorBranchMasterBODTO>();
		for (InvestorBranchMasterBO investorBranchMasterBO : investorBranchMasterBOList) {
			
			InvestorBranchMasterBODTO investorBranchMasterBODTO = mapper.map(investorBranchMasterBO, InvestorBranchMasterBODTO.class);
			
			investorBranchMasterBODTOList.add(investorBranchMasterBODTO);
		}
		return investorBranchMasterBODTOList;
	}*/

	@Override
	public InvestorBranchMasterBODTO findByBranchMasterId(int branchMasterId) {
		// TODO Auto-generated method stub
		
		try {
			
			InvestorBranchMasterBO investorBranchMasterBO = investorBranchMasterBORepository.findOne(branchMasterId);
			//InvestorBranchMasterBO investorBranchMasterBO = investorBranchMasterBORepository.findByBranchCode(branchMasterCode);
			InvestorBranchMasterBODTO investorBranchMasterBODTO = mapper.map(investorBranchMasterBO, InvestorBranchMasterBODTO.class);
			
			return investorBranchMasterBODTO;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public int deleteBranchDetailsByBranchId(int branchId) {
		// TODO Auto-generated method stub
		
		try {
			investorBranchMasterBORepository.delete(branchId);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<InvestorBranchMasterBODTO> getMFBackOfficeBranchNameAndIdByAdvisorId(int advisorId) {
		// TODO Auto-generated method stub
		AdvisorUser advisorUser = advisorUserRepository.findOne(advisorId);
		List<InvestorBranchMasterBO> investorBranchMasterBOList = investorBranchMasterBORepository.findBranchNameAndIdByAdvisorUser(advisorUser);
		List<InvestorBranchMasterBODTO> investorBranchMasterBODTOList = new ArrayList<InvestorBranchMasterBODTO>();
		for (InvestorBranchMasterBO investorBranchMasterBO : investorBranchMasterBOList) {
			
			InvestorBranchMasterBODTO investorBranchMasterBODTO = mapper.map(investorBranchMasterBO, InvestorBranchMasterBODTO.class);
			
			investorBranchMasterBODTOList.add(investorBranchMasterBODTO);
		}
		return investorBranchMasterBODTOList;
	}

	@Override
	public List<InvestorBranchMasterBODTO> getAllMFBackOfficeBranchByAdvisorId(int advisorId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
