package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.MasterTransactCommunicationMode;
import com.finlabs.finexa.model.MasterTransactDivPayMode;

public interface MasterTransactDivPayModeRepository extends JpaRepository<MasterTransactDivPayMode, String> {

	MasterTransactDivPayMode findBydetails(String details);

}
