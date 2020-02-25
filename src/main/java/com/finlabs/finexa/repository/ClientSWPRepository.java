package com.finlabs.finexa.repository;


import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientSTPOrderRegistration;
import com.finlabs.finexa.model.ClientSWPOrderRegistration;

public interface ClientSWPRepository extends JpaRepository<ClientSWPOrderRegistration, BigInteger> {

	public List<ClientSWPOrderRegistration> getByClientCodeAndSaveModeAndPurchaseMode(String clientCode, 
			String saveMode, String purchaseMode);
}
