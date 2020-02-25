package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorRoleSubmoduleMapping;
import com.finlabs.finexa.model.AdvisorUserRoleMapping;
import com.finlabs.finexa.model.LookupRole;
import com.finlabs.finexa.util.FinexaConstant;

public interface AdvisorRoleRepository extends JpaRepository<AdvisorRole, Integer>{

	AdvisorRole findByRoleDescription(String roleDescription);
	List<AdvisorRole> findBySupervisorRoleID(int roleId);
	/*@Query("SELECT advisorUR FROM AdvisorRole advisorUR WHERE advisorUR.supervisorRoleID=:roleId")
	public List<AdvisorRole> getAllSupervisorWithRoleId(@Param("roleId") int roleId);*/
	/**/
	@Query("select max(u.id) from AdvisorRole u")
    int getMaxId();
	//List<AdvisorRole> findByAdvisorId(AdvisorMaster master);
	List<AdvisorRole> findByAdvisorMaster(AdvisorMaster master);
	AdvisorRole findByAdvisorMasterAndRoleDescription(AdvisorMaster advMaster, String advisorRole);
	AdvisorRole findByAdvisorMasterAndSupervisorRoleID(AdvisorMaster master, int roleId);
	

	

	
}
