package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientMaster;

public interface AuxillaryMasterRepository extends JpaRepository<AuxillaryInvestorMaster, Integer> {

    List<AuxillaryInvestorMaster> findByActiveFlag(String flag);

	//List<AuxillaryInvestorMaster> findByAdvisorUserAndActiveFlag(AdvisorUser user, String flag);
	
	AuxillaryInvestorMaster findByPan(String pan);
	
	List<AuxillaryInvestorMaster> getByPanAndActiveFlag(String pan, String activeflag);
	
	List<AuxillaryInvestorMaster> findAllByClientMaster(ClientMaster clientMaster);
	
	List<AuxillaryInvestorMaster> getByPanAndActiveFlagAndUserID(String pan, String activeflag, int userID);
	
	List<AuxillaryInvestorMaster> getByPanAndActiveFlagAndClientMaster(String pan, String activeflag, ClientMaster clientMaster);
}

