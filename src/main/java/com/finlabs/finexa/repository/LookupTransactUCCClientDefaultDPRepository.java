package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.LookupTransactUCCClientDefaultDP;
import com.finlabs.finexa.model.LookupTransactUCCClientType;

public interface LookupTransactUCCClientDefaultDPRepository extends JpaRepository<LookupTransactUCCClientDefaultDP, Byte> {

	LookupTransactUCCClientDefaultDP findBydescription(String findBydescription);

}
