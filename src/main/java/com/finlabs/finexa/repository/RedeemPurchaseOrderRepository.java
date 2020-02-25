package com.finlabs.finexa.repository;


import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientTransactRedeemOrder;
import com.finlabs.finexa.model.PurchaseOrderEntryParam;

public interface RedeemPurchaseOrderRepository extends JpaRepository<ClientTransactRedeemOrder, BigInteger> {

	public List<ClientTransactRedeemOrder> getByClientCodeAndSaveMode(String clientCode, String saveMode);
}
