package com.finlabs.finexa.repository;

import java.math.BigInteger;
import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientUCCDetail;
import com.finlabs.finexa.model.ClientUCCDetailsDraftMode;

public interface ClientUCCDetailsDraftModeRepository extends JpaRepository<ClientUCCDetailsDraftMode, BigInteger> {

	ClientUCCDetailsDraftMode findByclientCode(String clientCode);
	
	public ClientUCCDetailsDraftMode getByClientCodeAndSaveMode(String clientCode, String saveMode);
}
