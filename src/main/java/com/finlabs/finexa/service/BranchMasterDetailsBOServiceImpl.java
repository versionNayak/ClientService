package com.finlabs.finexa.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.AdvisorUserDTO;
import com.finlabs.finexa.dto.BranchMasterDetailsBODTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserRoleMapping;
import com.finlabs.finexa.model.AdvisorUserSupervisorMapping;
import com.finlabs.finexa.model.BranchMasterDetailsBO;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.AdvisorRoleRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserRoleMappingRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.BranchMasterDetailsBORepository;
import com.finlabs.finexa.repository.UserRepository;

@Service("BranchMasterDetailsBOService")
@Transactional
public class BranchMasterDetailsBOServiceImpl implements BranchMasterDetailsBOService {

	@Autowired
	Mapper mapper;
	
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	
	@Autowired
	AdvisorUserRoleMappingRepository advisorUserRoleMappingRepository;
	
	@Autowired
	BranchMasterDetailsBORepository branchMasterDetailsBORepository;
	
	@Autowired
	AdvisorMasterRepository advisorMasterRepository;
	
	@Autowired
	AdvisorRoleRepository advisorRoleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository;
	
	public static final String BRANCH_MANAGER = "Branch Manager";
	
	@Override
	public BranchMasterDetailsBODTO save(BranchMasterDetailsBODTO branchMasterDetailsBODTO, int advisorUserId) {
		
		try {
			
			BranchMasterDetailsBO branchMasterDetailsBO = mapper.map(branchMasterDetailsBODTO, BranchMasterDetailsBO.class);
			AdvisorUser advisorUser = advisorUserRepository.findOne(branchMasterDetailsBODTO.getBranchHeadId());
			branchMasterDetailsBO.setAdvisorUser(advisorUser);
			
			branchMasterDetailsBO = branchMasterDetailsBORepository.save(branchMasterDetailsBO);
			
			return branchMasterDetailsBODTO;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}
			
	@Override
	public List<BranchMasterDetailsBODTO> getAllMFBackOfficeBranchByAdvisorId(int advisorId) {
		// TODO Auto-generated method stub
		try {
			List<BranchMasterDetailsBODTO> branchMasterDetailsBODTOList = new ArrayList<BranchMasterDetailsBODTO>();
			
			AdvisorUser advisorUser = advisorUserRepository.findOne(advisorId);
			
			AdvisorRole advisorRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(advisorUser.getAdvisorMaster(), BRANCH_MANAGER);				
			// find Role Mappings with the above role Id
			List<AdvisorUserRoleMapping> userRoleMapping = advisorUserRoleMappingRepository.findByAdvisorRole(advisorRole);
			
			for(AdvisorUserRoleMapping obj : userRoleMapping) {
				List<BranchMasterDetailsBO> branchMasterDetailsBOList = branchMasterDetailsBORepository.findAllByAdvisorUser(obj.getAdvisorUser());
				
				for (BranchMasterDetailsBO branchMasterDetailsBO : branchMasterDetailsBOList) {
					
					BranchMasterDetailsBODTO branchMasterDetailsBODTO = mapper.map(branchMasterDetailsBO, BranchMasterDetailsBODTO.class);
					
					AdvisorUser advisorObj = advisorUserRepository.findOne(branchMasterDetailsBO.getAdvisorUser().getId());
					//System.out.println("FirstName : " + advisorObj.getFirstName());
					String name = advisorObj.getFirstName();
					
					/*if ( !((advisorObj.getMiddleName()).equals("null")) || (advisorObj.getMiddleName() != null) || advisorObj.getMiddleName()!="") {
						System.out.println(advisorObj.getMiddleName().length());
						name = name + " " + advisorObj.getMiddleName();
					}*/
					/*
					if ( (advisorObj.getMiddleName() != null) || advisorObj.getMiddleName()!="") {
						//System.out.println(advisorObj.getMiddleName());
						name = name + " " + advisorObj.getMiddleName();
					}
					*/
					if (advisorObj.getLastName()!= null || advisorObj.getLastName()!="") {
						//System.out.println(advisorObj.getLastName());
						name = name + " " + advisorObj.getLastName();
					}
					
					branchMasterDetailsBODTO.setBranchHeadName(name);
					
					branchMasterDetailsBODTOList.add(branchMasterDetailsBODTO);
				}
			}
			
			
			return branchMasterDetailsBODTOList;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}

	
	@Override
	public BranchMasterDetailsBODTO findByBranchMasterId(int branchMasterId) {
		// TODO Auto-generated method stub
		
		try {
			
			BranchMasterDetailsBO branchMasterDetailsBO = branchMasterDetailsBORepository.findOne(branchMasterId);
			BranchMasterDetailsBODTO branchMasterDetailsBODTO = mapper.map(branchMasterDetailsBO, BranchMasterDetailsBODTO.class);
			branchMasterDetailsBODTO.setBranchHeadId(branchMasterDetailsBO.getAdvisorUser().getId());
			
			return branchMasterDetailsBODTO;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	@Override
	public int deleteBranchDetailsByBranchId(int branchId) {
		// TODO Auto-generated method stub
		
		try {
			branchMasterDetailsBORepository.delete(branchId);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	

	@Override
	public List<AdvisorUserDTO> getBranchManagerUsers(int advisorUserId) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<AdvisorUserDTO> advList = new ArrayList<AdvisorUserDTO>();
		if (advisorUserId != 0) {
			AdvisorUser adv = advisorUserRepository.findOne(advisorUserId);
			if (adv != null) {
				// get the branchManager role of AdvisorUser
				AdvisorRole advisorRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(adv.getAdvisorMaster(), BRANCH_MANAGER);				
				// find Role Mappings with the above role Id
				List<AdvisorUserRoleMapping> userRoleMapping = advisorUserRoleMappingRepository.findByAdvisorRole(advisorRole);
				
				for (AdvisorUserRoleMapping obj : userRoleMapping) {
					AdvisorUserDTO objDto = new AdvisorUserDTO();
					objDto.setId(obj.getAdvisorUser().getId());
					objDto.setFirstName(obj.getAdvisorUser().getFirstName());
					// find if this user is assigned to any branch
					
					BranchMasterDetailsBO branchDetails = branchMasterDetailsBORepository.findByAdvisorUser(obj.getAdvisorUser());
					if (branchDetails != null) {
						objDto.setBranchName(branchDetails.getBranchName());
					}
					
					advList.add(objDto);
				}
				
			}
		}
		
		return advList;
	}

	@Override
	public List<AdvisorUserDTO> getBranchManagerUsersName(int advisorUserId) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		List<AdvisorUserDTO> advList = new ArrayList<AdvisorUserDTO>();
		if (advisorUserId != 0) {
			AdvisorUser adv = advisorUserRepository.findOne(advisorUserId);
			if (adv != null) {
				// get the branchManager role of AdvisorUser
				AdvisorRole advisorRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(adv.getAdvisorMaster(), BRANCH_MANAGER);				
				// find Role Mappings with the above role Id
				List<AdvisorUserRoleMapping> userRoleMapping = advisorUserRoleMappingRepository.findByAdvisorRole(advisorRole);
				
				for (AdvisorUserRoleMapping obj : userRoleMapping) {
					
					AdvisorUserDTO objDto = new AdvisorUserDTO();
					objDto.setId(obj.getAdvisorUser().getId());
					objDto.setFirstName(obj.getAdvisorUser().getFirstName());
					objDto.setMiddleName(obj.getAdvisorUser().getMiddleName());
					objDto.setLastName(obj.getAdvisorUser().getLastName());
					
					BranchMasterDetailsBO branchMasterDetailsBO = branchMasterDetailsBORepository.findByAdvisorUser(obj.getAdvisorUser());
					if(branchMasterDetailsBO != null) {
						objDto.setBranchName(branchMasterDetailsBO.getBranchName());
					} else {
						objDto.setBranchName("NA");
					}
					
					advList.add(objDto);
					
				}
				
			}
		}
		
		return advList;
	}

	@Override
	public boolean checkIfBranchWithSameBranchHeadExists(int branchHeadId)
			throws RuntimeException {
		// TODO Auto-generated method stub
		boolean flag = true;
		if(branchHeadId != 0) {
			AdvisorUser advHeadBranchHead = advisorUserRepository.findOne(branchHeadId);
			if(advHeadBranchHead != null) {
				BranchMasterDetailsBO branchObj = branchMasterDetailsBORepository.findByAdvisorUser(advHeadBranchHead);
				if(branchObj != null) {
					if(branchObj.getAdvisorUser().getId() == branchHeadId) {
						flag = false;
					}
				}
			}
		}
		
		return flag;
	}

	@Override
	public boolean checkUniqueBranchCode(int userId, String branchCode) {
		try {
			List<BranchMasterDetailsBO> listBranchCode = branchMasterDetailsBORepository.findAll();

			boolean flag = true;
			for (BranchMasterDetailsBO branchMasterDetailsBO : listBranchCode) {
				
					if (branchMasterDetailsBO.getBranchCode().equals(branchCode)) {
						flag = false;
						break;
					}
				
			}
			return flag;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean checkExistingBranchCode(int branchID, String branchCode) {
		try {
			List<BranchMasterDetailsBO> listBranchCode = branchMasterDetailsBORepository.findAll();

			boolean flag = true;
			for (BranchMasterDetailsBO branchMasterDetailsBO : listBranchCode) {
				if(branchMasterDetailsBO.getId()!=branchID)
					if (branchMasterDetailsBO.getBranchCode().equals(branchCode)) {
						flag = false;
						break;
					}
				
			}
			return flag;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public BranchMasterDetailsBODTO update(BranchMasterDetailsBODTO branchMasterDetailsBODTO) {

		BranchMasterDetailsBO branchMasterDetailsBO = mapper.map(branchMasterDetailsBODTO, BranchMasterDetailsBO.class);
		// branchMasterDetailsBO = branchMasterDetailsBORepository.findOne(branchMasterDetailsBODTO.getId());
		branchMasterDetailsBO.setId(branchMasterDetailsBODTO.getId());
		AdvisorUser advisorUserId = advisorUserRepository.findOne(branchMasterDetailsBODTO.getBranchHeadId());
		branchMasterDetailsBO.setAdvisorUser(advisorUserId);
		branchMasterDetailsBO = branchMasterDetailsBORepository.save(branchMasterDetailsBO);
		
		
		return branchMasterDetailsBODTO;
		
	}

	@Override
	public boolean checkIfBranchMasterExists(int branchHeadId) {
		boolean flag=false;
		AdvisorUser advisorUser=advisorUserRepository.findById(branchHeadId);
		BranchMasterDetailsBO branchMasterDetailsBO=branchMasterDetailsBORepository.findByadvisorUser(advisorUser);
		if(branchMasterDetailsBO!=null) {
			flag=true;
		}
		return flag;
	}

	@Override
	public boolean checkIfBranchMasterisAssigned(int branchBranchMasterID) {
		boolean flag=true;
		BranchMasterDetailsBO branchMasterDetailsBO=branchMasterDetailsBORepository.findById(branchBranchMasterID);
		List<AdvisorUserSupervisorMapping> advisorUserSupervisorMapping=advisorUserSupervisorMappingRepository.findByAdvisorUser2(branchMasterDetailsBO.getAdvisorUser());
		if(advisorUserSupervisorMapping.size()==0) {
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean checkUniqueMobileNumber(String phoneNumber) {
		boolean flag = true;
		List<BranchMasterDetailsBO> branchMasterDetailsBOList=branchMasterDetailsBORepository.findAll();
		for(BranchMasterDetailsBO branchMasterDetailsBO:branchMasterDetailsBOList) {
			if((branchMasterDetailsBO.getBranchMobileNo().toString()).equals(String.valueOf(phoneNumber))||(branchMasterDetailsBO.getBranchPhoneNo()).toString().equals(String.valueOf(phoneNumber))) {
				flag=false;
			}
		}
		return flag;
	}

	@Override
	public boolean checkExistingPhoneNumber(int branchBranchMasterID, String phoneNumber) {
		boolean flag = true;
		List<BranchMasterDetailsBO> branchMasterDetailsBOList=branchMasterDetailsBORepository.findAll();
		for(BranchMasterDetailsBO branchMasterDetailsBO:branchMasterDetailsBOList) {
			if(branchBranchMasterID!=(branchMasterDetailsBO.getId()))
			if((branchMasterDetailsBO.getBranchMobileNo().toString()).equals(String.valueOf(phoneNumber))||(branchMasterDetailsBO.getBranchPhoneNo()).toString().equals(String.valueOf(phoneNumber))) {
				flag=false;
			}
		}
		return flag;
	}

	@Override
	public boolean checkIfBranchWithSameBranchHeadExistsForEdit(int branchHeadId,int branchId) {
		boolean flag = true;
		if(branchHeadId != 0) {
			AdvisorUser advHeadBranchHead = advisorUserRepository.findOne(branchHeadId);
			if(advHeadBranchHead != null) {
				BranchMasterDetailsBO branchObj = branchMasterDetailsBORepository.findByAdvisorUser(advHeadBranchHead);
				if(branchObj != null) {
					if(branchId!=branchObj.getId()) {
						if(branchObj.getAdvisorUser().getId() == branchHeadId) {
						flag = false;
						}
					}
				}
			}
		}
		
		return flag;
	}

	
			
}
