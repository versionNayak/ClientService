package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.MasterTransactOccupationCode;
import com.finlabs.finexa.model.MasterTransactTaxStatus;

public interface MasterTransactOccupationCodeRepository extends JpaRepository<MasterTransactOccupationCode, String> {

	MasterTransactOccupationCode findBydetails(String details);
}
