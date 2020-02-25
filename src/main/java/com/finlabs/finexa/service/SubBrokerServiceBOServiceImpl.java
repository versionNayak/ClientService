package com.finlabs.finexa.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.finlabs.finexa.dto.AdvisorUserDTO;
import com.finlabs.finexa.dto.BranchMasterDetailsBODTO;
import com.finlabs.finexa.dto.RmMasterBODTO;
import com.finlabs.finexa.dto.SubBrokerMasterDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.AdvisorProductReco;
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserRoleMapping;
import com.finlabs.finexa.model.AdvisorUserSupervisorMapping;
import com.finlabs.finexa.model.BranchMasterDetailsBO;
import com.finlabs.finexa.model.InvestorBranchMasterBO;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.LookupTransactBSEAccessMode;
import com.finlabs.finexa.model.SubBrokerMasterBO;
import com.finlabs.finexa.model.User;
import com.finlabs.finexa.repository.AdvisorRoleRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserRoleMappingRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.InvestorBranchMasterBORepository;
import com.finlabs.finexa.repository.LookupCountryRepository;
import com.finlabs.finexa.repository.LookupTransactBSEAccessModeRepository;
import com.finlabs.finexa.repository.SubBrokerMasterBORepository;
import com.finlabs.finexa.repository.UserRepository;
import com.finlabs.finexa.repository.BranchMasterDetailsBORepository;

@Service("SubBrokerMasterBOService")
@Transactional

public class SubBrokerServiceBOServiceImpl implements SubBrokerMasterBOService {

	@Autowired
	Mapper mapper;
	
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	
	@Autowired
	SubBrokerMasterBORepository subBrokerMasterBORepository;
	
	@Autowired
	AdvisorRoleRepository advisorRoleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	LookupTransactBSEAccessModeRepository lookupTransactBSEAccessMode;
	
	@Autowired
	AdvisorUserRoleMappingRepository advisorUserRoleMappingRepo;
	
	@Autowired
	BranchMasterDetailsBORepository branchMasterDetailsBORepository;
	
	@Autowired
	AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository;

	@Autowired
	AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepo;
	
	@Autowired
	LookupCountryRepository lookupCountryRepository;
	
	public static final String RELATION_MANAGER = "Sub Broker";
	public static final String BRANCH_MANAGER = "Branch Manager";
	public static final String COUNTRY_CODE = "99";
	
	//-------------------- For saving the details of Sub broker master -----------------------------------
	@Override
	@Transactional(rollbackOn = CustomFinexaException.class)
	public SubBrokerMasterDTO save(SubBrokerMasterDTO subBrokerMasterDTO)
			throws RuntimeException, IOException, InvalidFormatException, ParseException {
		try {
			
			User userEntry = new User();
			AdvisorUser advisorUser = advisorUserRepository.findOne(subBrokerMasterDTO.getAdvisorID());
			userEntry.setAdvisorMaster(advisorUser.getAdvisorMaster());

			System.out.println(advisorUser.getAdvisorMaster().getId() + " --- " + RELATION_MANAGER);
			
			AdvisorRole advisorRole = advisorRoleRepository
					.findByAdvisorMasterAndRoleDescription(advisorUser.getAdvisorMaster(), RELATION_MANAGER);
			userEntry.setAdvisorRole(advisorRole);

			userEntry.setLoginUsername(subBrokerMasterDTO.getSbEmailID());

			String[] splited = subBrokerMasterDTO.getSbName().split("\\s+");
			String firstName = splited[0];
			String middleName = "";
			String lastName = "";
			if (splited.length == 3) {
				middleName = splited[1];
				lastName = splited[2];
			} else if (splited.length == 2) {
				lastName = splited[1];
			}
			String password = splited[0].toLowerCase();

			userEntry.setLoginPassword(password);
			userEntry.setAdmin("N");
			userEntry.setAdvisorAdmin("N");
			userEntry.setBudgetManagementView(advisorUser.getUser().getBudgetManagementView());
			userEntry.setGoalPlanningView(advisorUser.getUser().getGoalPlanningView());
			userEntry.setGoalPlanningAddEdit(advisorUser.getUser().getGoalPlanningAddEdit());
			userEntry.setPortfolioManagementView(advisorUser.getUser().getPortfolioManagementView());
			userEntry.setPortfolioManagementAddEdit(advisorUser.getUser().getPortfolioManagementAddEdit());
			userEntry.setFinancialPlanningView(advisorUser.getUser().getFinancialPlanningView());
			userEntry.setFinancialPlanningAddEdit(advisorUser.getUser().getFinancialPlanningAddEdit());
			userEntry.setInvestView(advisorUser.getUser().getInvestView());
			userEntry.setInvestAddEdit(advisorUser.getUser().getInvestView());
			userEntry.setMfBackOfficeView(advisorUser.getUser().getMfBackOfficeView());
			userEntry.setMfBackOfficeAddEdit(advisorUser.getUser().getMfBackOfficeAddEdit());
			userEntry.setUserManagementView("N");
			userEntry.setUserManagementAddEdit("N");
			userEntry.setUserManagementDelete("N");
			userEntry.setClientInfoView(advisorUser.getUser().getClientInfoView());
			userEntry.setClientInfoAddEdit(advisorUser.getUser().getClientInfoAddEdit());
			userEntry.setClientInfoDelete(advisorUser.getUser().getClientInfoDelete());
			userEntry.setMastersView(advisorUser.getUser().getMastersView());
			userEntry.setMastersAddEdit(advisorUser.getUser().getMastersAddEdit());
			userEntry.setMastersDelete(advisorUser.getUser().getMastersDelete());
			//userEntry.setBseMFView(advisorUser.getUser().getBseMFView());
			userEntry.setActiveFlag("Y");
			userEntry.setCreatedOn(new Date());
			userEntry = userRepository.save(userEntry);

			if (userEntry != null) {
				// save in AdvisorUser
				AdvisorUser advUser = new AdvisorUser();
				advUser.setAdvisorMaster(advisorUser.getAdvisorMaster());
				advUser.setLoginUsername(userEntry.getLoginUsername());
				advUser.setLoginPassword(userEntry.getLoginPassword());
				advUser.setEmailID(userEntry.getLoginUsername());
				advUser.setEmployeeCode(subBrokerMasterDTO.getSbEmployeeCode());
				advUser.setPhoneNo(subBrokerMasterDTO.getSbMobileNumber());
				advUser.setFirstName(firstName);
				advUser.setMiddleName(middleName);
				LookupTransactBSEAccessMode LookupTransactBSEAccessMode = lookupTransactBSEAccessMode.findOne((byte) 2);
				advUser.setLookupTransactBseaccessMode(LookupTransactBSEAccessMode);
				advUser.setLastName(lastName);
				advUser.setUser(userEntry);
				advUser.setActiveFlag("Y");
				advUser.setLastLoginTime(new Date());
				advUser.setLoggedInFlag("N");//setting lastLoginTime as current date
				
				//Set branch master as advisorUser City
				AdvisorUser advisorUserSupervisor=advisorUserRepository.findById(subBrokerMasterDTO.getSbBranch());
			    BranchMasterDetailsBO branchMasterDetailsBO=branchMasterDetailsBORepository.findByAdvisorUser(advisorUserSupervisor);
			    advUser.setCity(branchMasterDetailsBO.getBranchName());
			    LookupCountry lookupCountry=lookupCountryRepository.findById(Integer.parseInt(COUNTRY_CODE));
			    advUser.setLookupCountry(lookupCountry);
			    
				advUser = advisorUserRepository.save(advUser);

				// user Role Mapping
				AdvisorUserRoleMapping roleMapping = new AdvisorUserRoleMapping();
				roleMapping.setAdvisorUser(advUser);
				roleMapping.setAdvisorRole(advisorRole);
				roleMapping.setEffectiveFromDate(new Date());
				
				roleMapping = advisorUserRoleMappingRepo.save(roleMapping);

				// userSupervisorMapping
				AdvisorUserSupervisorMapping userSuperMapping = new AdvisorUserSupervisorMapping();
				userSuperMapping.setAdvisorUser1(advUser);

				AdvisorUser branchHead = advisorUserRepository.findOne(subBrokerMasterDTO.getSbBranch());
				userSuperMapping.setAdvisorUser2(branchHead);
				userSuperMapping.setEffectiveFromDate(new Date());
				userSuperMapping = advisorUserSupervisorMappingRepo.save(userSuperMapping);

			}
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		return subBrokerMasterDTO;
	}
//-------------------- END of saving the details of Sub broker master  -------------------------------------

/************************** For showing the list of Sub brokers along their Branch **************************/
	@Override
	public List<SubBrokerMasterDTO> findByAdvisorId(int advisorID) {
		// TODO Auto-generated method stub
		List<SubBrokerMasterDTO> advList = new ArrayList<SubBrokerMasterDTO>();
		try {

			AdvisorUser adv = advisorUserRepository.findOne(advisorID);

			AdvisorUser advisorUser = new AdvisorUser();
			// get the RelationManager role of AdvisorUser
			AdvisorRole advisorRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(adv.getAdvisorMaster(), RELATION_MANAGER);

			// find Role Mappings with the above role Id
			List<AdvisorUserRoleMapping> advisoruserRoleMappingList = advisorUserRoleMappingRepo.findByAdvisorRole(advisorRole);

			for (AdvisorUserRoleMapping obj : advisoruserRoleMappingList) {

				List<AdvisorUser> advList1 = new ArrayList<AdvisorUser>();
				advList1.add(obj.getAdvisorUser());
				for (AdvisorUser ad : advList1) {
					// int id=ad.getAdvisorMaster().getId();
					List<AdvisorUserSupervisorMapping> advisorUserSupervisorMapping = advisorUserSupervisorMappingRepo.getAllSupervisorWithUserId(ad.getId());

					for (AdvisorUserSupervisorMapping advisorUserSupervisorMappingid : advisorUserSupervisorMapping) {
						SubBrokerMasterDTO subBrokerMasterDTO = mapper.map(advisorUserSupervisorMapping,SubBrokerMasterDTO.class);
						if (ad.getId() == advisorUserSupervisorMappingid.getAdvisorUser1().getId()) {

							subBrokerMasterDTO = mapper.map(advisorUser, SubBrokerMasterDTO.class);
							subBrokerMasterDTO.setSbName(ad.getFirstName() + " " + ad.getMiddleName() + " " + ad.getLastName());
							subBrokerMasterDTO.setSbMobileNumber(ad.getPhoneNo());
							subBrokerMasterDTO.setSbEmailID(ad.getEmailID());
							subBrokerMasterDTO.setSbEmployeeCode(ad.getEmployeeCode());
							subBrokerMasterDTO.setAdvisorID(advisorID);
							subBrokerMasterDTO.setID(ad.getId());
							System.out.println("ID "+ad.getId());
							
							subBrokerMasterDTO.setSupervisorId(advisorUserSupervisorMappingid.getAdvisorUser2().getId());
							int brId = advisorUserSupervisorMappingid.getAdvisorUser2().getId();
							subBrokerMasterDTO.setSbBranch(brId);
							subBrokerMasterDTO.setSubBrokerMasterID(ad.getId());
							BranchMasterDetailsBO branchMasterDetailsBO = branchMasterDetailsBORepository.findByAdvisorUser(advisorUserSupervisorMappingid.getAdvisorUser2());
							//System.out.println("branchName " + branchMasterDetailsBO.getBranchName());
							subBrokerMasterDTO.setBranchName(branchMasterDetailsBO.getBranchName());
							

							// String branchName =
							// branchMasterDetailsBORepository.findBranchNameByBranchHead(advisorUserSupervisorMappingid.getAdvisorUser2().getId());
							// System.out.println("branchName " + branchMasterDetailsBO.getBranchName());
							// subBrokerMasterDTO.setBranchName(branchName);

							advList.add(subBrokerMasterDTO);
						}
					}

				}

			}

		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		return advList;
	}
/************************** End for showing the list of Sub brokers along their Branch **************************/

// ---------------------------  For fetching details for Edit  ---------------------------
	@Override
	 public SubBrokerMasterDTO findById(int id) throws RuntimeException{
		try {
			 AdvisorUser advisorUserBO = advisorUserRepository.findOne(id);
			 SubBrokerMasterDTO subBrokerMasterDTO = mapper.map(advisorUserBO, SubBrokerMasterDTO.class);
			 
			 subBrokerMasterDTO.setSbName(advisorUserBO.getFirstName());
			 subBrokerMasterDTO.setSbEmailID(advisorUserBO.getEmailID());
			 subBrokerMasterDTO.setSbEmployeeCode(advisorUserBO.getEmployeeCode());
			 subBrokerMasterDTO.setSbMobileNumber(advisorUserBO.getPhoneNo());
			 //subBrokerMasterDTO.setSubBrokerMasterID(advisorUserBO.g);
			 
			 //AdvisorUserSupervisorMapping advisorUserSupervisorMapping = advisorUserSupervisorMappingRepository.findByUserID(advisorUserBO.getId());
			 AdvisorUserSupervisorMapping advisorUserSupervisorMapping = advisorUserSupervisorMappingRepository.findByAdvisorUser1(id);
			 subBrokerMasterDTO.setSupervisorId(advisorUserSupervisorMapping.getAdvisorUser2().getId());
			 subBrokerMasterDTO.setSubBrokerMasterID(advisorUserSupervisorMapping.getAdvisorUser2().getId());
			 subBrokerMasterDTO.setSbBranch(advisorUserSupervisorMapping.getAdvisorUser2().getId());
			 
			 BranchMasterDetailsBO branchMasterDetailsBO = branchMasterDetailsBORepository
						.findByAdvisorUser(advisorUserSupervisorMapping.getAdvisorUser2());
			 
			//For fetching branch manager with branch master
				AdvisorUser adv = advisorUserRepository.findOne(id);
				AdvisorRole advisorRole = advisorRoleRepository
						.findByAdvisorMasterAndRoleDescription(adv.getAdvisorMaster(), BRANCH_MANAGER);
				// find Role Mappings with the above role Id

				List<AdvisorUserRoleMapping> advisoruserRoleMappingList = advisorUserRoleMappingRepo
						.findByAdvisorRole(advisorRole);
				for (AdvisorUserRoleMapping obj : advisoruserRoleMappingList) {

					List<AdvisorUser> advList1 = new ArrayList<AdvisorUser>();
					advList1.add(obj.getAdvisorUser());
					for (AdvisorUser ad : advList1) {
						List<AdvisorUserSupervisorMapping> advisorUserSupervisorMappinglist = advisorUserSupervisorMappingRepo
								.getAllSupervisorWithUserId(ad.getId());
						for (AdvisorUserSupervisorMapping advisorUserSupervisorMappingid : advisorUserSupervisorMappinglist) {
							subBrokerMasterDTO.setSupervisorId(advisorUserSupervisorMappingid.getAdvisorUser2().getId());
						}

					}
				}

			 
			 
			 subBrokerMasterDTO.setBranchName(branchMasterDetailsBO.getBranchName());
			 subBrokerMasterDTO.setSubBrokerMasterID(advisorUserBO.getUser().getId());
			 
			 return subBrokerMasterDTO;
			 
		 }catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	 }
// --------------------------- End of fetching details for Edit ---------------------------

	
/*************************************	Delete Details	*************************************/
	@Override
	public int delete(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			
			AdvisorUser advisorUser=advisorUserRepository.findOne(id);
			
			AdvisorUserSupervisorMapping advisorUserSupervisorMapping=advisorUserSupervisorMappingRepo.findByAdvisorUser1(id);
			advisorUserSupervisorMappingRepo.delete(advisorUserSupervisorMapping.getId());
			
			AdvisorUserRoleMapping advisorUserRoleMapping=advisorUserRoleMappingRepo.findByuserId(id);
			advisorUserRoleMappingRepo.delete(advisorUserRoleMapping.getId());
			
			advisorUserRepository.delete(id);
			
			userRepository.delete(advisorUser.getUser().getId());
			
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return 1;
	}
/***********************************	End of Delete Details	***********************************/
	
	
//-------------------- For saving the Updated details of Sub broker master -----------------------------------
	@Override
	public SubBrokerMasterDTO update(SubBrokerMasterDTO subBrokerMasterDTO) {
		try {
			//System.out.println("Advisor ID " + subBrokerMasterDTO.getAdvisorID());
			//System.out.println("SubBrokerMasterID " + subBrokerMasterDTO.getSubBrokerMasterID());
			
			User userEntry = new User();
			
			AdvisorUser advisorUserId = advisorUserRepository.findOne(subBrokerMasterDTO.getSubBrokerMasterID());
			userEntry.setId(advisorUserId.getUser().getId());
			
			AdvisorUser advisorUser = advisorUserRepository.findOne(subBrokerMasterDTO.getAdvisorID());
			userEntry.setAdvisorMaster(advisorUser.getAdvisorMaster());
			
			AdvisorRole advisorRole = advisorRoleRepository
					.findByAdvisorMasterAndRoleDescription(advisorUser.getAdvisorMaster(), RELATION_MANAGER);
			userEntry.setAdvisorRole(advisorRole);
			
			userEntry.setLoginUsername(subBrokerMasterDTO.getSbEmailID());

				String[] splited = subBrokerMasterDTO.getSbName().split("\\s+");
				String firstName = splited[0];
				String middleName = "";
				String lastName = "";
				if (splited.length == 3) {
					middleName = splited[1];
					lastName = splited[2];
				} else if (splited.length == 2) {
					lastName = splited[1];
				}
				String password = splited[0].toLowerCase();

				userEntry.setLoginPassword(password);
				userEntry.setAdmin("N");
				userEntry.setAdvisorAdmin("N");
				userEntry.setBudgetManagementView(advisorUser.getUser().getBudgetManagementView());
				userEntry.setGoalPlanningView(advisorUser.getUser().getGoalPlanningView());
				userEntry.setGoalPlanningAddEdit(advisorUser.getUser().getGoalPlanningAddEdit());
				userEntry.setPortfolioManagementView(advisorUser.getUser().getPortfolioManagementView());
				userEntry.setPortfolioManagementAddEdit(advisorUser.getUser().getPortfolioManagementAddEdit());
				userEntry.setFinancialPlanningView(advisorUser.getUser().getFinancialPlanningView());
				userEntry.setFinancialPlanningAddEdit(advisorUser.getUser().getFinancialPlanningAddEdit());
				userEntry.setInvestView(advisorUser.getUser().getInvestView());
				userEntry.setInvestAddEdit(advisorUser.getUser().getInvestView());
				userEntry.setMfBackOfficeView(advisorUser.getUser().getMfBackOfficeView());
				userEntry.setMfBackOfficeAddEdit(advisorUser.getUser().getMfBackOfficeAddEdit());
				userEntry.setUserManagementView("N");
				userEntry.setUserManagementAddEdit("N");
				userEntry.setUserManagementDelete("N");
				userEntry.setClientInfoView(advisorUser.getUser().getClientInfoView());
				userEntry.setMastersView(advisorUser.getUser().getMastersView());
				userEntry.setMastersAddEdit(advisorUser.getUser().getMastersAddEdit());
				userEntry.setMastersDelete(advisorUser.getUser().getMastersDelete());
				//userEntry.setBseMFView(advisorUser.getUser().getBseMFView());
				userEntry.setActiveFlag("Y");
				userEntry.setCreatedOn(new Date());
				userEntry = userRepository.save(userEntry);
				//System.out.println("saved in User Table ");

				if (userEntry != null) {
					// save in AdvisorUser
					AdvisorUser advUser = new AdvisorUser();
					advUser.setId(subBrokerMasterDTO.getSubBrokerMasterID());
					advUser.setAdvisorMaster(advisorUser.getAdvisorMaster());
					advUser.setLoginUsername(userEntry.getLoginUsername());
					advUser.setLoginPassword(userEntry.getLoginPassword());
					advUser.setEmailID(userEntry.getLoginUsername());
					advUser.setEmployeeCode(subBrokerMasterDTO.getSbEmployeeCode());
					advUser.setPhoneNo(subBrokerMasterDTO.getSbMobileNumber());
					advUser.setFirstName(firstName);
					advUser.setMiddleName(middleName);
					LookupTransactBSEAccessMode LookupTransactBSEAccessMode = lookupTransactBSEAccessMode.findOne((byte) 2);
					advUser.setLookupTransactBseaccessMode(LookupTransactBSEAccessMode);
					advUser.setLastName(lastName);
					advUser.setUser(userEntry);
					advUser.setActiveFlag("Y");
					advUser.setLastLoginTime(new Date()); //setting lastLoginTime as current date
					advUser.setLoggedInFlag("N");
					advUser = advisorUserRepository.save(advUser);
					//System.out.println("saved in AdvisorUser Table ");

					// user Role Mapping
					AdvisorUserRoleMapping roleMapping = new AdvisorUserRoleMapping();
					AdvisorUserRoleMapping advisorUserRoleMapping = advisorUserRoleMappingRepo.findByAdvisorUser(advisorUserId);
					roleMapping.setId(advisorUserRoleMapping.getId());
					roleMapping.setAdvisorUser(advUser);
					roleMapping.setAdvisorRole(advisorRole);
					roleMapping.setEffectiveFromDate(new Date());
					roleMapping = advisorUserRoleMappingRepo.save(roleMapping);
					//System.out.println("saved in AdvisorUserRole Table ");
					
					// userSupervisorMapping
					AdvisorUserSupervisorMapping userSuperMapping = new AdvisorUserSupervisorMapping();
					userSuperMapping.setAdvisorUser1(advUser);
					AdvisorUserSupervisorMapping AdvUserSuperMapping = advisorUserSupervisorMappingRepo.findByAdvisorUser1(advisorUserId);
					userSuperMapping.setId(AdvUserSuperMapping.getId());
					AdvisorUser branchHead = advisorUserRepository.findOne(subBrokerMasterDTO.getSbBranch());
					userSuperMapping.setAdvisorUser2(branchHead);
					userSuperMapping.setEffectiveFromDate(new Date());
					userSuperMapping = advisorUserSupervisorMappingRepo.save(userSuperMapping);
					//System.out.println("saved in AdvisorUserSupervisorMapping Table ");
					
				}
							
			} catch (RuntimeException e) {
				throw new RuntimeException(e);
			}
			
			return subBrokerMasterDTO;
			
		}
//-------------------- END of saving the Updated details of Sub broker master  -------------------------------------

	@Override
	public List<AdvisorUserDTO> getSubBrokerUsersNameForParticularBranch(int branchId) throws RuntimeException {
		// TODO Auto-generated method stub
		List<AdvisorUserDTO> advList = new ArrayList<AdvisorUserDTO>();
		try {
			if (branchId != 0) {
				AdvisorUser adv = advisorUserRepository.findOne(branchId);
				if (adv != null) {
					AdvisorRole advisorRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(adv.getAdvisorMaster(), RELATION_MANAGER);
					List<AdvisorUserRoleMapping> userRoleMapping = advisorUserRoleMappingRepo.findByAdvisorRole(advisorRole);
				
					for (AdvisorUserRoleMapping obj : userRoleMapping) {
						AdvisorUserDTO objDto = new AdvisorUserDTO();
						objDto.setId(obj.getAdvisorUser().getId());
						objDto.setFirstName(obj.getAdvisorUser().getFirstName());
						objDto.setMiddleName(obj.getAdvisorUser().getMiddleName());
						objDto.setLastName(obj.getAdvisorUser().getLastName());
						
						advList.add(objDto);
					
					}
					
					return advList;
				}
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		return advList;
	}

	@Override
	public boolean checkIfSbRoleExists(int advisorID) {
		boolean flag = true;
		AdvisorUser advisorUser = advisorUserRepository.findOne(advisorID);

		System.out.println(advisorUser.getAdvisorMaster().getId() + " --- " + RELATION_MANAGER);
		
		AdvisorRole advisorRole = advisorRoleRepository
				.findByAdvisorMasterAndRoleDescription(advisorUser.getAdvisorMaster(), RELATION_MANAGER);
		System.out.println("Role"+advisorRole);
		if(advisorRole == null || advisorRole.equals(null) ||advisorRole.equals("null") ) {
			flag=false;
		}
		return flag;
	}

}
