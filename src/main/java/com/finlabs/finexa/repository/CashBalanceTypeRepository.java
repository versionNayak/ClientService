package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupCashBalanceType;

public interface CashBalanceTypeRepository extends JpaRepository<LookupCashBalanceType, Integer> {

}
