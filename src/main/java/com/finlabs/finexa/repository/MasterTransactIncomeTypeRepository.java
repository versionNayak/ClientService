package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.MasterTransactDivPayMode;
import com.finlabs.finexa.model.MasterTransactFatcaAddressType;
import com.finlabs.finexa.model.MasterTransactIncome;

public interface MasterTransactIncomeTypeRepository extends JpaRepository<MasterTransactIncome, String> {

	MasterTransactIncome findByincome(String details);

}
