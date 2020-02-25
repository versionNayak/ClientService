package com.finlabs.finexa.repository;


import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finlabs.finexa.model.PurchaseOrderEntryParam;

public interface PurchaseOrderEntryParamRepository extends JpaRepository<PurchaseOrderEntryParam, BigInteger> {

	public List<PurchaseOrderEntryParam> getByClientCodeAndSaveModeAndPurchaseMode(String clientCode, String saveMode, String purchaseMode);
}
