package com.finlabs.finexa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorProductReco;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientGoal;
import com.finlabs.finexa.model.ClientMaster;


public interface AdvisorProductRecoRepository extends JpaRepository<AdvisorProductReco, Integer>{
	
	List<AdvisorProductReco> findByAdvisorUserAndClientMasterAndClientGoalAndModule
	    (AdvisorUser advisorUser,ClientMaster clientMaster, ClientGoal clientGoal,String module);
	
	List<AdvisorProductReco> findByAdvisorUserAndClientMasterAndModule
    (AdvisorUser advisorUser,ClientMaster clientMaster,String module);

	@Query("select max(adv.recoSaveDate) from AdvisorProductReco adv where adv.advisorUser.id = :advisorID and adv.clientMaster.id = :clientID "
			+ "and adv.clientGoal.id = :goalID and adv.module= :module")
			public Date getMaxDateOfSavedProductRecoGP(@Param("advisorID") int advisorID, @Param("clientID") int clientID, @Param("goalID") int goalID, @Param("module") String module);
	
	@Query("select adv.productPlan from AdvisorProductReco adv where adv.advisorUser.id = :advisorID and adv.clientMaster.id = :clientID "
			+ "and adv.clientGoal.id = :goalID and adv.module= :module and adv.recoSaveDate = :recoSaveDate")
			public String getLastSavedProductPlan(@Param("advisorID") int advisorID, @Param("clientID") int clientID, @Param("goalID") int goalID, @Param("module") String module,  @Param("recoSaveDate") Date recoSaveDate);
	
	@Query("select max(adv.recoSaveDate) from AdvisorProductReco adv where adv.advisorUser.id = :advisorID and adv.clientMaster.id = :clientID "
			+ "and adv.module= :module")
			public Date getMaxDateOfSavedProductRecoPM(@Param("advisorID") int advisorID, @Param("clientID") int clientID, @Param("module") String module);
	
	@Query("select adv.productPlan from AdvisorProductReco adv where adv.advisorUser.id = :advisorID and adv.clientMaster.id = :clientID "
			+ "and adv.module= :module and adv.recoSaveDate = :recoSaveDate")
			public String getLastSavedProductPlanPM(@Param("advisorID") int advisorID, @Param("clientID") int clientID, @Param("module") String module, @Param("recoSaveDate") Date recoSaveDate);
	
	AdvisorProductReco findByAdvisorUserAndClientMasterAndClientGoalAndModuleAndRecoSaveDate
    (AdvisorUser advisorUser,ClientMaster clientMaster, ClientGoal clientGoal,String module, String recoSaveDate);

	List<AdvisorProductReco> findByClientGoal(ClientGoal clientGoal);
	
}
