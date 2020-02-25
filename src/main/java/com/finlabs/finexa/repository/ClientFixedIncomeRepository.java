package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finlabs.finexa.model.ClientFixedIncome;
import com.finlabs.finexa.model.ClientMutualFund;
@Repository
public interface ClientFixedIncomeRepository extends JpaRepository<ClientFixedIncome, Integer> {
}
