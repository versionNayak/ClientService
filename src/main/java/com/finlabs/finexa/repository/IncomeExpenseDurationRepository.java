package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupIncomeExpenseDuration;

public interface IncomeExpenseDurationRepository extends JpaRepository<LookupIncomeExpenseDuration, Byte> {

}
