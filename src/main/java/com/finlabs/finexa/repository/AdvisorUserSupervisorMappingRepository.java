package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserRoleMapping;
import com.finlabs.finexa.model.AdvisorUserSupervisorMapping;
import com.finlabs.finexa.model.User;

public interface AdvisorUserSupervisorMappingRepository extends JpaRepository<AdvisorUserSupervisorMapping, Integer>{
	@Query("SELECT advSupervisor FROM AdvisorUserSupervisorMapping advSupervisor WHERE advSupervisor.advisorUser1.id=:userId")
	public List<AdvisorUserSupervisorMapping> getAllSupervisorWithUserId(@Param("userId") int userId);
	
	@Query("SELECT advSupervisor FROM AdvisorUserSupervisorMapping advSupervisor WHERE advSupervisor.advisorUser2.id=:superId and advSupervisor.advisorUser1.id<>:superId")
	public List<AdvisorUserSupervisorMapping> getAllSupervisorWithSupervisorId(@Param("superId") int superId);
	
	public List<AdvisorUserSupervisorMapping> findByAdvisorUser2(AdvisorUser user);
	
	@Query("SELECT advSupervisor FROM AdvisorUserSupervisorMapping advSupervisor WHERE advSupervisor.advisorUser1.id=:id")
    public AdvisorUserSupervisorMapping findByAdvisorUser1(@Param("id") int id);
	
	public AdvisorUserSupervisorMapping findByAdvisorUser1(AdvisorUser advisorUserId);



}
