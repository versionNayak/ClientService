package com.finlabs.finexa.service;



import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.model.ClientAccessRight;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.User;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.UserRepository;


@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
	/*@Autowired
	AdvisorUserRepository advisorUserRepository;*/
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	ClientMasterRepository clientMasterRepository;
	
	
	
	@Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 try {
			Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
			System.out.println("userdetails inn "+username);
			User user = userRepository.findByLoginUsernameAndActiveFlag(username, "Y");
			//System.out.println("user "+user);
			if(user != null) {
				if(user.getAdmin() != null && user.getAdmin().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_Admin"));
				
				if(user.getAdvisorAdmin() != null && user.getAdvisorAdmin().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_AdvisorAdmin"));
				
				if(user.getClientInfoView()!= null && user.getClientInfoView().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ClientInfoView"));
				
				if(user.getClientInfoAddEdit()!= null && user.getClientInfoAddEdit().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ClientInfoAddEdit"));
				
				if(user.getClientInfoDelete()!= null && user.getClientInfoDelete().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ClientInfoDelete"));
				
				if(user.getGoalPlanningView() != null && user.getGoalPlanningView().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_GoalPlanningView"));
				
				if(user.getGoalPlanningAddEdit() != null && user.getGoalPlanningAddEdit().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_GoalPlanningAddEdit"));
				
					
				if(user.getPortfolioManagementView() != null && user.getPortfolioManagementView().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_PortFolioManagementView"));
				
				if(user.getPortfolioManagementAddEdit() != null && user.getPortfolioManagementAddEdit().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_PortFolioManagementAddEdit"));
				
				if(user.getBudgetManagementView() != null && user.getBudgetManagementView().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_BudgetManagementView"));
				
				if(user.getFinancialPlanningView() != null && user.getFinancialPlanningView().equalsIgnoreCase("Y"))
					grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_FinancialPlanningView"));
				
				if(user.getFinancialPlanningAddEdit() != null && user.getFinancialPlanningAddEdit().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_FinancialPlanningAddEdit"));
				
				if(user.getClientRecordsView() != null && user.getClientRecordsView().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ClientRecordsView"));
				
				if(user.getMastersView() != null && user.getMastersView().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MastersView"));
				
				if(user.getMastersAddEdit() != null && user.getMastersAddEdit().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MastersAddEdit"));
				
				if(user.getMastersDelete() != null && user.getMastersDelete().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MastersDelete"));
				
				if(user.getUserManagementView() != null && user.getUserManagementView().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_UserManagementView"));
				
				if(user.getUserManagementAddEdit() != null && user.getUserManagementAddEdit().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_UserManagementAddEdit"));
				
				if(user.getUserManagementDelete() != null && user.getUserManagementDelete().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_UserManagementDelete"));
					
				if(user.getMfBackOfficeView() != null && user.getMfBackOfficeView().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MFBacKOfficeView"));
				
				if(user.getMfBackOfficeAddEdit() != null && user.getMfBackOfficeAddEdit().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MFBacKOfficeAddEdit"));
					
				if(user.getInvestView()!= null && user.getInvestView().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_InvestView"));
				
				if(user.getInvestAddEdit()!= null && user.getInvestAddEdit().equalsIgnoreCase("Y"))
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_InvestAddEdit"));

				return new org.springframework.security.core.userdetails.User(user.getLoginUsername(), user.getLoginPassword(), grantedAuthorities);
			}else{
		    ClientMaster clientMaster = clientMasterRepository.findByLoginUsernameAndActiveFlag(username, "Y");
		   // System.out.println("clientMaster "+clientMaster);
		    ClientAccessRight clientAccessRight = clientMaster.getClientAccessRight();
			if(clientMaster.getClient() != null && clientMaster.getClient().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_Client"));
			
			if(clientAccessRight != null) {
				
			if(clientAccessRight.getClientInfoView()!= null && clientAccessRight.getClientInfoView().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ClientInfoView"));
			
			if(clientAccessRight.getClientInfoAddEdit()!= null && clientAccessRight.getClientInfoAddEdit().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ClientInfoAddEdit"));
			
			if(clientAccessRight.getClientInfoDelete()!= null && clientAccessRight.getClientInfoDelete().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ClientInfoDelete"));
			
			if(clientAccessRight.getGoalPlanningView() != null && clientAccessRight.getGoalPlanningView().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_GoalPlanningView"));
			
			if(clientAccessRight.getGoalPlanningAddEdit() != null && clientAccessRight.getGoalPlanningAddEdit().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_GoalPlanningAddEdit"));
			
				
			if(clientAccessRight.getPortfolioManagementView() != null && clientAccessRight.getPortfolioManagementView().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_PortFolioManagementView"));
			
			if(clientAccessRight.getPortfolioManagementAddEdit() != null && clientAccessRight.getPortfolioManagementAddEdit().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_PortFolioManagementAddEdit"));
			
			if(clientAccessRight.getBudgetManagementView() != null && clientAccessRight.getBudgetManagementView().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_BudgetManagementView"));
			
			if(clientAccessRight.getFinancialPlanningView() != null && clientAccessRight.getFinancialPlanningView().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_FinancialPlanningView"));
			
			if(clientAccessRight.getFinancialPlanningAddEdit() != null && clientAccessRight.getFinancialPlanningAddEdit().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_FinancialPlanningAddEdit"));

			if(clientAccessRight.getMfBackOfficeView() != null && clientAccessRight.getMfBackOfficeView().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MFBacKOfficeView"));
			
			if(clientAccessRight.getInvestView()!= null && clientAccessRight.getInvestView().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_InvestView"));
			
			if(clientAccessRight.getInvestAddEdit()!= null && clientAccessRight.getInvestAddEdit().equalsIgnoreCase("Y"))
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_InvestAddEdit"));

			return new org.springframework.security.core.userdetails.User(clientMaster.getLoginUsername(), clientMaster.getLoginPassword(), grantedAuthorities);
		    
		      }
			}
		    
		  } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return  (UserDetails) new UsernameNotFoundException("Username: " + username + " not found");
    }
	
	
}
