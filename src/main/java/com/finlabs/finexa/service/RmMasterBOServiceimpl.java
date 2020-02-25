package com.finlabs.finexa.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.dto.AdvisorUserDTO;
import com.finlabs.finexa.dto.BranchMasterDetailsBODTO;
import com.finlabs.finexa.dto.RmMasterBODTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserRoleMapping;
import com.finlabs.finexa.model.AdvisorUserSupervisorMapping;
import com.finlabs.finexa.model.BranchMasterDetailsBO;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.LookupTransactBSEAccessMode;
import com.finlabs.finexa.model.RmMasterBO;
import com.finlabs.finexa.model.User;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.AdvisorRoleRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserRoleMappingRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.BranchMasterDetailsBORepository;
import com.finlabs.finexa.repository.ClientContactRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.LookupCountryRepository;
import com.finlabs.finexa.repository.LookupTransactBSEAccessModeRepository;
import com.finlabs.finexa.repository.RmMasterRepositoryBO;
import com.finlabs.finexa.repository.UserRepository;

@Service("RmMasterBOService")
@Transactional
public class RmMasterBOServiceimpl implements RmMasterBOService {

	@Autowired
	AdvisorUserRepository advisorUserRepository;

	@Autowired
	BranchMasterDetailsBORepository branchMasterDetailsBORepository;

	@Autowired
	Mapper mapper;

	@Autowired
	AdvisorRoleRepository advisorRoleRepository;

	@Autowired
	RmMasterRepositoryBO rmMasterBORepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AdvisorUserRoleMappingRepository advisorUserRoleMappingRepo;

	@Autowired
	AdvisorMasterRepository advisorMasterRepository;

	@Autowired
	AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepo;

	@Autowired
	AdvisorUserRoleMappingRepository advisorUserRoleMappingRepository;

	@Autowired
	LookupTransactBSEAccessModeRepository lookupTransactBSEAccessMode;
	
	@Autowired
	LookupCountryRepository lookupCountryRepository;
	
	@Autowired
	ClientMasterRepository clientMasterRepository;
	
	@Autowired
	ClientContactRepository clientContactRepository;

	public static final String RELATION_MANAGER = "Relationship Manager";
	public static final String BRANCH_MANAGER = "Branch Manager";
	public static final String COUNTRY_CODE = "99";

	@Override
	@Transactional(rollbackOn = CustomFinexaException.class)
	public RmMasterBODTO save(RmMasterBODTO rmMasterBODTO) throws RuntimeException, CustomFinexaException {

		try {

			User userEntry = new User();
			AdvisorUser advisorUser = advisorUserRepository.findOne(rmMasterBODTO.getAdvisorID());
			userEntry.setAdvisorMaster(advisorUser.getAdvisorMaster());

			System.out.println(advisorUser.getAdvisorMaster().getId() + " --- " + RELATION_MANAGER);

			AdvisorRole advisorRole = advisorRoleRepository
					.findByAdvisorMasterAndRoleDescription(advisorUser.getAdvisorMaster(), RELATION_MANAGER);
			userEntry.setAdvisorRole(advisorRole);

			userEntry.setLoginUsername(rmMasterBODTO.getRmEmailID());

			String[] splited = rmMasterBODTO.getRmName().split("\\s+");
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
			userEntry.setClientInfoView(advisorUser.getUser().getClientInfoView());
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
			// userEntry.setBseMFView(advisorUser.getUser().getBseMFView());
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
				advUser.setEmployeeCode(rmMasterBODTO.getRmEmployeeCode());
				advUser.setPhoneNo(rmMasterBODTO.getRmMobileNumber());
				advUser.setFirstName(firstName);
				advUser.setMiddleName(middleName);
				/*
				 * advUser.setGoalPlanning("Y"); advUser.setPortfolioManagement("Y");
				 * advUser.setFinancialPlanning("Y"); advUser.setBudgetManagement("Y");
				 */
				LookupTransactBSEAccessMode LookupTransactBSEAccessMode = lookupTransactBSEAccessMode.findOne((byte) 2);
				advUser.setLookupTransactBseaccessMode(LookupTransactBSEAccessMode);
				advUser.setLastName(lastName);
				advUser.setUser(userEntry);
				advUser.setActiveFlag("Y");
				advUser.setLastLoginTime(new Date()); // setting lastLoginTime as current date
				advUser.setLoggedInFlag("N");
				
				//Set branch master as advisorUser City
				AdvisorUser advisorUserSupervisor=advisorUserRepository.findById(rmMasterBODTO.getRmBranch());
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

				AdvisorUser branchHead = advisorUserRepository.findOne(rmMasterBODTO.getRmBranch());
				userSuperMapping.setAdvisorUser2(branchHead);
				userSuperMapping.setEffectiveFromDate(new Date());
				userSuperMapping = advisorUserSupervisorMappingRepo.save(userSuperMapping);

			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			rmMasterBODTO.setErrorString("Unable To save Due To " + e.getMessage());
		}
		return rmMasterBODTO;

	}

	public List<RmMasterBODTO> findByAdvisorId(int advisorID) throws RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		// List<RmMasterBODTO> listDTO = new ArrayList<RmMasterBODTO>();
		List<RmMasterBODTO> advList = new ArrayList<RmMasterBODTO>();
		try {

			AdvisorUser adv = advisorUserRepository.findOne(advisorID);

			AdvisorUser advisorUser = new AdvisorUser();
			// get the RelationManager role of AdvisorUser
			AdvisorRole advisorRole = advisorRoleRepository
					.findByAdvisorMasterAndRoleDescription(adv.getAdvisorMaster(), RELATION_MANAGER);
			// find Role Mappings with the above role Id

			List<AdvisorUserRoleMapping> advisoruserRoleMappingList = advisorUserRoleMappingRepo
					.findByAdvisorRole(advisorRole);
			for (AdvisorUserRoleMapping obj : advisoruserRoleMappingList) {

				List<AdvisorUser> advList1 = new ArrayList<AdvisorUser>();
				advList1.add(obj.getAdvisorUser());
				for (AdvisorUser ad : advList1) {
					// int id=ad.getAdvisorMaster().getId();
					List<AdvisorUserSupervisorMapping> advisorUserSupervisorMapping = advisorUserSupervisorMappingRepo
							.getAllSupervisorWithUserId(ad.getId());
					for (AdvisorUserSupervisorMapping advisorUserSupervisorMappingid : advisorUserSupervisorMapping) {
						RmMasterBODTO rmMasterBODTO = mapper.map(advisorUserSupervisorMapping, RmMasterBODTO.class);
						if (ad.getId() == advisorUserSupervisorMappingid.getAdvisorUser1().getId()) {

							rmMasterBODTO = mapper.map(advisorUser, RmMasterBODTO.class);
							rmMasterBODTO
									.setRmName(ad.getFirstName() + " " + ad.getMiddleName() + " " + ad.getLastName());
							rmMasterBODTO.setRmMobileNumber(ad.getPhoneNo());
							rmMasterBODTO.setRmEmailID(ad.getEmailID());
							rmMasterBODTO.setRmEmployeeCode(ad.getEmployeeCode());
							rmMasterBODTO.setAdvisorID(advisorID);
							// rmMasterBODTO.setID(ad.getUser().getId());
							rmMasterBODTO.setID(ad.getId());
							rmMasterBODTO.setSupervisorId(advisorUserSupervisorMappingid.getAdvisorUser2().getId());

							rmMasterBODTO.setRmBranch(advisorUserSupervisorMappingid.getAdvisorUser2().getId());

							rmMasterBODTO.setRmMasterId(ad.getId());
							BranchMasterDetailsBO branchMasterDetailsBO = branchMasterDetailsBORepository
									.findByAdvisorUser(advisorUserSupervisorMappingid.getAdvisorUser2());
							// System.out.println("branchName " + branchMasterDetailsBO.getBranchName());
							rmMasterBODTO.setRmBranchName(branchMasterDetailsBO.getBranchName());
							advList.add(rmMasterBODTO);
							// advList.add(rmMasterBODTO);
						}
					}

				}

			}
			// }

		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		return advList;

	}

	@Override
	public RmMasterBODTO findById(int id) throws RuntimeException {
		// TODO Auto-generated method stub

		try {
			AdvisorUserSupervisorMapping advisorUserSupervisorMapping = advisorUserSupervisorMappingRepo
					.findByAdvisorUser1(id);
			AdvisorUser advisorUser = advisorUserRepository.findOne(id);
			RmMasterBODTO rmMasterBODTO = mapper.map(advisorUser, RmMasterBODTO.class);
			rmMasterBODTO.setRmName(advisorUser.getFirstName());
			rmMasterBODTO.setRmMobileNumber(advisorUser.getPhoneNo());
			rmMasterBODTO.setRmEmailID(advisorUser.getEmailID());
			rmMasterBODTO.setRmEmployeeCode(advisorUser.getEmployeeCode());
			rmMasterBODTO.setRmBranch(advisorUserSupervisorMapping.getAdvisorUser2().getId());
			BranchMasterDetailsBO branchMasterDetailsBO = branchMasterDetailsBORepository
					.findByAdvisorUser(advisorUserSupervisorMapping.getAdvisorUser2());
			rmMasterBODTO.setRmBranchName(branchMasterDetailsBO.getBranchName());
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
						rmMasterBODTO.setSupervisorId(advisorUserSupervisorMappingid.getAdvisorUser2().getId());
					}

				}
			}

			rmMasterBODTO.setRmMasterId(advisorUser.getUser().getId());
			return rmMasterBODTO;
			/*
			 * RmMasterBO rmMasterBO = rmMasterBORepository.findOne(id); RmMasterBODTO
			 * rmMasterBODTO = mapper.map(rmMasterBO, RmMasterBODTO.class);
			 * rmMasterBODTO.setRmName(rmMasterBO.getRmMasterName());
			 * rmMasterBODTO.setRmMobileNumber(rmMasterBO.getRmMasterMobileNumber());
			 * rmMasterBODTO.setRmEmailID(rmMasterBO.getRmMasterEmailID());
			 * rmMasterBODTO.setRmEmployeeCode(rmMasterBO.getRmMasterEmployeeCode());
			 * rmMasterBODTO.setRmBranch(rmMasterBO.getInvestorBranchMasterBo().getId());
			 * 
			 * return rmMasterBODTO;
			 */
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public int delete(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		try {

			AdvisorUser advisorUser = advisorUserRepository.findOne(id);

			AdvisorUserSupervisorMapping advisorUserSupervisorMapping = advisorUserSupervisorMappingRepo
					.findByAdvisorUser1(id);
			advisorUserSupervisorMappingRepo.delete(advisorUserSupervisorMapping.getId());

			AdvisorUserRoleMapping advisorUserRoleMapping = advisorUserRoleMappingRepo.findByuserId(id);
			advisorUserRoleMappingRepo.delete(advisorUserRoleMapping.getId());

			advisorUserRepository.delete(id);

			userRepository.delete(advisorUser.getUser().getId());

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return 1;
	}

	@Override
	public List<BranchMasterDetailsBODTO> getMFBackOfficeBranchNameAndIdByAdvisorId(int advisorId) {
		return null;

	}

	@Override
	public RmMasterBODTO update(RmMasterBODTO rmMasterBODTO) {
		try {
			User userEntry = new User();

			AdvisorUser advisorUserId = advisorUserRepository.findOne(rmMasterBODTO.getRmMasterId());
			userEntry.setId(advisorUserId.getUser().getId());

			AdvisorUser advisorUser = advisorUserRepository.findOne(rmMasterBODTO.getAdvisorID());
			userEntry.setAdvisorMaster(advisorUser.getAdvisorMaster());

			AdvisorRole advisorRole = advisorRoleRepository
					.findByAdvisorMasterAndRoleDescription(advisorUser.getAdvisorMaster(), RELATION_MANAGER);
			userEntry.setAdvisorRole(advisorRole);

			userEntry.setLoginUsername(rmMasterBODTO.getRmEmailID());

			String[] splited = rmMasterBODTO.getRmName().split("\\s+");
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
			// userEntry.setBseMFView(advisorUser.getUser().getBseMFView());
			userEntry.setActiveFlag("Y");
			userEntry.setCreatedOn(new Date());
			userEntry = userRepository.save(userEntry);
			// System.out.println("saved in User Table ");

			if (userEntry != null) {
				// save in AdvisorUser
				AdvisorUser advUser = new AdvisorUser();
				advUser.setId(rmMasterBODTO.getRmMasterId());
				advUser.setAdvisorMaster(advisorUser.getAdvisorMaster());
				advUser.setLoginUsername(userEntry.getLoginUsername());
				advUser.setLoginPassword(userEntry.getLoginPassword());
				advUser.setEmailID(userEntry.getLoginUsername());
				advUser.setEmployeeCode(rmMasterBODTO.getRmEmployeeCode());
				advUser.setPhoneNo(rmMasterBODTO.getRmMobileNumber());
				advUser.setFirstName(firstName);
				advUser.setMiddleName(middleName);
				LookupTransactBSEAccessMode LookupTransactBSEAccessMode = lookupTransactBSEAccessMode.findOne((byte) 2);
				advUser.setLookupTransactBseaccessMode(LookupTransactBSEAccessMode);
				advUser.setLastName(lastName);
				advUser.setUser(userEntry);
				advUser.setActiveFlag("Y");
				advUser.setLastLoginTime(new Date()); // setting lastLoginTime as current date
				advUser.setLoggedInFlag("N");
				advUser = advisorUserRepository.save(advUser);
				// System.out.println("saved in AdvisorUser Table ");

				// user Role Mapping
				AdvisorUserRoleMapping roleMapping = new AdvisorUserRoleMapping();
				AdvisorUserRoleMapping advisorUserRoleMapping = advisorUserRoleMappingRepo
						.findByAdvisorUser(advisorUserId);
				roleMapping.setId(advisorUserRoleMapping.getId());
				roleMapping.setAdvisorUser(advUser);
				roleMapping.setAdvisorRole(advisorRole);
				roleMapping.setEffectiveFromDate(new Date());
				roleMapping = advisorUserRoleMappingRepo.save(roleMapping);
				// System.out.println("saved in AdvisorUserRole Table ");

				// userSupervisorMapping
				AdvisorUserSupervisorMapping userSuperMapping = new AdvisorUserSupervisorMapping();
				userSuperMapping.setAdvisorUser1(advUser);
				AdvisorUserSupervisorMapping AdvUserSuperMapping = advisorUserSupervisorMappingRepo
						.findByAdvisorUser1(advisorUserId);
				userSuperMapping.setId(AdvUserSuperMapping.getId());
				AdvisorUser branchHead = advisorUserRepository.findOne(rmMasterBODTO.getRmBranch());
				userSuperMapping.setAdvisorUser2(branchHead);
				userSuperMapping.setEffectiveFromDate(new Date());
				userSuperMapping = advisorUserSupervisorMappingRepo.save(userSuperMapping);
				// System.out.println("saved in AdvisorUserSupervisorMapping Table ");

			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return rmMasterBODTO;

	}

	@Override
	public List<AdvisorUserDTO> getRelationshipManagerUsersNameForParticularBranch(int branchId)
			throws RuntimeException {
		// TODO Auto-generated method stub
		List<AdvisorUserDTO> advList = new ArrayList<AdvisorUserDTO>();
		try {
			if (branchId != 0) {
				AdvisorUser adv = advisorUserRepository.findOne(branchId);
				if (adv != null) {
					AdvisorRole advisorRole = advisorRoleRepository
							.findByAdvisorMasterAndRoleDescription(adv.getAdvisorMaster(), RELATION_MANAGER);
					List<AdvisorUserRoleMapping> userRoleMapping = advisorUserRoleMappingRepo
							.findByAdvisorRole(advisorRole);

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
	public boolean checkIfRmManagerRoleExists(int advisorID) {
		boolean flag = true;
		AdvisorUser advisorUser = advisorUserRepository.findOne(advisorID);

		System.out.println(advisorUser.getAdvisorMaster().getId() + " --- " + RELATION_MANAGER);

		AdvisorRole advisorRole = advisorRoleRepository
				.findByAdvisorMasterAndRoleDescription(advisorUser.getAdvisorMaster(), RELATION_MANAGER);
		System.out.println("Role" + advisorRole);
		if (advisorRole == null || advisorRole.equals(null) || advisorRole.equals("null")) {
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean checkUniqueEmpCodeForAdvisorMaster(int advisorID, String employeeCode) {

		boolean flag = true;
		AdvisorUser advisorUser = advisorUserRepository.findById(advisorID);
		List<AdvisorUser> advisorUserList = advisorUserRepository.findByAdvisorMaster(advisorUser.getAdvisorMaster());
		try {
			if (advisorUserList.size() > 0) {

				for (AdvisorUser empCode : advisorUserList) {
					if (empCode.getEmployeeCode()!=null && empCode.getEmployeeCode().trim()!="" && !empCode.getEmployeeCode().trim().isEmpty() ) {
						if (empCode.getEmployeeCode().equals(employeeCode)) {
							flag = false;
						}
				}
				}
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return flag;

	}

	@Override
	public boolean checkExistingEmpCodeForAdvisorMaster(int advisorID, int userID, String employeeCode) {
		boolean flag = true;
		AdvisorUser advisorUser = advisorUserRepository.findById(advisorID);
		List<AdvisorUser> advisorUserList = advisorUserRepository.findByAdvisorMaster(advisorUser.getAdvisorMaster());
		if (advisorUserList.size() > 0) {
			for (AdvisorUser user : advisorUserList) {
				if (user.getId() != userID) {
					if (user.getEmployeeCode()!=null && user.getEmployeeCode().trim()!="" && !user.getEmployeeCode().trim().isEmpty()) {
						if (user.getEmployeeCode().equals(employeeCode)) {
							flag = false;
						}
					}
				}
			}
		}

		return flag;

	}

	@Override
	public boolean checkUniqueEmailForAdvisorMaster(String email,int advisorID) {
		boolean flag = true;
		AdvisorUser advisorUser = advisorUserRepository.findById(advisorID);
		List<AdvisorUser> advisorUserList = advisorUserRepository.findByAdvisorMaster(advisorUser.getAdvisorMaster());
		try {
			if (advisorUserList.size() > 0) {

				for (AdvisorUser user : advisorUserList) {
					if (user.getEmailID()!=null && user.getEmailID().trim()!="" && !user.getEmailID().trim().isEmpty() ) {
						if (user.getEmailID().equals(email)) {
							flag = false;
						}
				}
				}
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return flag;
	}

	@Override
	public boolean checkExistsEmailForAdvisorMaster( String email,int advisorID,int UserID) {
		boolean flag = true;
		AdvisorUser advisorUser = advisorUserRepository.findById(advisorID);
		List<AdvisorUser> advisorUserList = advisorUserRepository.findByAdvisorMaster(advisorUser.getAdvisorMaster());
		try {
			if (advisorUserList.size() > 0) {

				for (AdvisorUser user : advisorUserList) {
					if (user.getId() != UserID) {
					if (user.getEmailID()!=null && user.getEmailID().trim()!="" && !user.getEmailID().trim().isEmpty() ) {
						if (user.getEmailID().equals(email)) {
							flag = false;
						}
				}
				}
				}
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return flag;

		
	}

	@Override
	public boolean checkRmOrSbIsAssigned( int userID) {
		boolean flag=true;
		AdvisorUser advisorUser=advisorUserRepository.findById(userID);
		List<ClientMaster> cm=clientMasterRepository.findByAdvisorUser(advisorUser);
	
		if(cm.size()==0) {
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean checkUniqueMobileNumber(String mobileNo) {
		boolean flag = true;
		List<BigInteger> mobileNumbersOfClients=clientContactRepository.getMobile();
		List<BigInteger> mobileNumbersOfAdvisors=advisorUserRepository.getAllPhoneNo();
		if (mobileNo!=null && mobileNo!="" && !mobileNo.trim().isEmpty() ) {
			if ((mobileNumbersOfClients.contains(BigInteger.valueOf(Long.parseLong(mobileNo)))) || mobileNumbersOfAdvisors.contains(BigInteger.valueOf(Long.parseLong(mobileNo)))) {
				flag = false;
			}
			
	}
		return flag;
	}

	@Override
	public boolean checkExistingMobileNumber(String mobileNo,int userID) {
		boolean flag = true ;
		List<BigInteger> mobileNumbersOfClients=clientContactRepository.getMobile();
		List<BigInteger> mobileNumbersOfAdvisors=advisorUserRepository.getAllPhoneNo();
		if (mobileNo!=null && mobileNo!="" && !mobileNo.trim().isEmpty() ) {
			if ((mobileNumbersOfClients.contains(BigInteger.valueOf(Long.parseLong(mobileNo)))) || mobileNumbersOfAdvisors.contains(BigInteger.valueOf(Long.parseLong(mobileNo)))) {
				AdvisorUser advisorUSer=advisorUserRepository.findByPhoneNo(BigInteger.valueOf(Long.parseLong(mobileNo)));
				if(advisorUSer.getId()!=userID) {
				flag = false;
				}
			}
			
	}
		return flag;
	}

}
