package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorRoleSubmoduleMapping;
import com.finlabs.finexa.model.AdvisorUser;

public interface AdvisorRoleSubmoduleMappingRepository extends JpaRepository<AdvisorRoleSubmoduleMapping,Integer>{

	
	@Query("SELECT advRoleSub FROM  AdvisorRoleSubmoduleMapping advRoleSub WHERE advRoleSub.advisorRole.id=:roleId AND "
			+ "advRoleSub.finexaBusinessSubmodule.id=:submoduleId")
	public AdvisorRoleSubmoduleMapping checkIfExists(@Param("roleId") int roleId, @Param("submoduleId") int submoduleId);
	
	
}
