package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupFundCategory;

public interface FundRepository extends JpaRepository<LookupFundCategory, Integer>{
}
