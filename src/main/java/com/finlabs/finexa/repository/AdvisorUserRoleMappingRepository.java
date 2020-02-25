package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserRoleMapping;

public interface AdvisorUserRoleMappingRepository extends JpaRepository<AdvisorUserRoleMapping, Integer>{

	@Query("SELECT advRoleMap FROM  AdvisorUserRoleMapping advRoleMap WHERE advRoleMap.advisorUser.id=:userId AND "
			+ "advRoleMap.advisorRole.id=:roleId")
	public AdvisorUserRoleMapping checkIfExists(@Param("roleId") int roleId, @Param("userId") int userId);
	
	@Query("SELECT advUserRole FROM AdvisorUserRoleMapping advUserRole WHERE advUserRole.advisorUser.id=:userId ")
	public List<AdvisorUserRoleMapping> getAllUserRoleWithUserId(@Param("userId") int userId);
	
	
	@Query("SELECT advRole FROM AdvisorUserRoleMapping advRole WHERE advRole.advisorRole.id=:roleId ")
	public List<AdvisorUserRoleMapping> getAllUserRoleWithRoleId(@Param("roleId") int roleId);
	
	List<AdvisorUserRoleMapping> findByAdvisorRole(AdvisorRole role);
	
	@Query("SELECT advisorUserRole FROM AdvisorUserRoleMapping advisorUserRole WHERE advisorUserRole.advisorUser.id=:id")
	public AdvisorUserRoleMapping findByuserId(@Param("id") int id);

	public AdvisorUserRoleMapping findByAdvisorUser(AdvisorUser advisorUserId);

}
