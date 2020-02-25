package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserLoginInfo;

public interface AdvisorUserLoginInfoRepository extends JpaRepository<AdvisorUserLoginInfo, Integer>{

	List<AdvisorUserLoginInfo> findByAdvisorUserAndLogoutTimeIsNull(AdvisorUser au);

	List<AdvisorUserLoginInfo> findByAdvisorUser(AdvisorUser au);
	
	AdvisorUserLoginInfo findByToken(String token);

	List<AdvisorUserLoginInfo> findByAdvisorUserOrderByLoginTimeDesc(AdvisorUser au);
	
	AdvisorUserLoginInfo findTopByAdvisorUserOrderByLoginTimeDesc(AdvisorUser au);

	List<AdvisorUserLoginInfo> findByAdvisorUserOrderByLoginTimeDesc(AdvisorUser au, Pageable pageable);

	int countByAdvisorUser(AdvisorUser au);

}
