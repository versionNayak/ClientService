package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.MasterTransactAccountType;
import com.finlabs.finexa.model.MasterTransactClientUCCDetail;

public interface MasterTransactClientUCCDetailsRepository extends JpaRepository<MasterTransactClientUCCDetail, String> {

	public List<MasterTransactClientUCCDetail> findByFirstApplicantPan(String pan);
}
