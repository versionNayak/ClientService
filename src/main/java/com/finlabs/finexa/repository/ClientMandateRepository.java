package com.finlabs.finexa.repository;

import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientFatcaReport;
import com.finlabs.finexa.model.ClientMandateRegistration;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientUCCDetail;
import com.finlabs.finexa.model.ClientUCCDetailsDraftMode;

public interface ClientMandateRepository extends JpaRepository<ClientMandateRegistration, String> {

	List<ClientMandateRegistration> findByclientMaster(ClientMaster master);
	
	public List<ClientMandateRegistration> getByClientCodeAndSaveMode(String clientCode, String saveMode);
	
	public ClientMandateRegistration findByMandateId(String mandateId);
	
	public List<ClientMandateRegistration> findByClientCodeAndMandateTypeAndSaveMode(String clientCode, String mandateType, String saveMode);

}
