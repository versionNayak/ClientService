package com.finlabs.finexa.repository;

import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientUCCDetail;

public interface ClientUCCRepository extends JpaRepository<ClientUCCDetail, String> {

	List<ClientUCCDetail> findByclientMaster(ClientMaster master);
	
	ClientUCCDetail findByClientAppName1AndClientAppName2AndClientAppName3(String app1,
			String app2,String app3);
	
	List<ClientUCCDetail> findByAdvisorUserAndClientPanAndClientPan2AndClientPan3AndClientGuardianPan(AdvisorUser adv, String app1,
			String app2,String app3, String app4);
}
