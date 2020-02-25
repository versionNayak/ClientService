package com.finlabs.finexa.repository;


import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientSTPOrderRegistration;
import com.finlabs.finexa.model.ClientSWPOrderRegistration;
import com.finlabs.finexa.model.PurchaseOrderEntryParam;
import com.finlabs.finexa.model.XsipOrderEntryParam;

public interface ClientSTPRepository extends JpaRepository<ClientSTPOrderRegistration, BigInteger> {

	public List<ClientSTPOrderRegistration> getByClientCodeAndSaveModeAndPurchaseMode(String clientCode, 
			String saveMode, String purchaseMode);
}
