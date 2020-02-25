package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.LookupTansactHoldingType;
import com.finlabs.finexa.model.MasterTransactTaxStatus;

public interface MasterTransactTaxStatusRepository extends JpaRepository<MasterTransactTaxStatus, String> {

	MasterTransactTaxStatus findBytaxStatus(String taxStatus);
}
