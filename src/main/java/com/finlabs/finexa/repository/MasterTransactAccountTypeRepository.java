package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.MasterTransactAccountType;
import com.finlabs.finexa.model.MasterTransactOccupationCode;

public interface MasterTransactAccountTypeRepository extends JpaRepository<MasterTransactAccountType, String> {

	MasterTransactAccountType findBydetails(String details);
}
