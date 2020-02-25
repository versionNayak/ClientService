package com.finlabs.finexa.repository;


import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientSwitchOrderEntryParam;
import com.finlabs.finexa.model.PurchaseOrderEntryParam;
import com.finlabs.finexa.model.XsipOrderEntryParam;

public interface ClientSwitchOrderEntryParamRepository extends JpaRepository<ClientSwitchOrderEntryParam, BigInteger> {

	public List<ClientSwitchOrderEntryParam> getByClientCodeAndSaveModeAndPurchaseMode(String clientCode, String saveMode, String purchaseMode);
}
