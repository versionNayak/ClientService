package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	User findByLoginUsername(String loginUsername);
	
	User findByLoginUsernameAndActiveFlag(String loginUsername, String activeFlag);

    User findByLoginUsernameAndLoginPassword(String username, String password);
    
    List<User> findByAdvisorRole(AdvisorRole role);

	List<User> findByAdvisorMasterAndAdvisorRole(AdvisorMaster master, AdvisorRole role);

	long countByAdvisorAdmin(String string);

	long countByAdvisorMasterAndAdvisorAdmin(AdvisorMaster advisorMaster, String string);

	Page<User> findByAdvisorAdmin(String string, Pageable pageable);
    
    
}
