package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.MasterTransactAccountType;
import com.finlabs.finexa.model.MasterTransactCommunicationMode;

public interface MasterTransactCommunicationModeRepository extends JpaRepository<MasterTransactCommunicationMode, String> {

	MasterTransactCommunicationMode findBydetails(String details);
}
