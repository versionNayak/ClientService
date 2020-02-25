package com.finlabs.finexa.repository;

import java.math.BigInteger;
import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientFatcaReport;
import com.finlabs.finexa.model.ClientMandateRegistration;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientUCCDetail;

public interface ClientFatcaRepository extends JpaRepository<ClientFatcaReport, BigInteger> {

	List<ClientFatcaReport> findByclientMaster(ClientMaster master);
	
	public ClientFatcaReport getByClientCodeAndSaveMode(String clientCode, String saveMode);

}
