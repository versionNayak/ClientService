package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientAnnuity;
import com.finlabs.finexa.model.ClientEquity;
import com.finlabs.finexa.model.ClientMutualFund;

public interface ClientEquityRepository extends JpaRepository<ClientEquity, Integer> {
}
