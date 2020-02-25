package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.MasterTransactDivPayMode;
import com.finlabs.finexa.model.MasterTransactStateCode;

public interface MasterTransactStateCodeRepository extends JpaRepository<MasterTransactStateCode, String> {

	MasterTransactStateCode findBydetails(String details);

	MasterTransactStateCode findDetailsByCode(String code);

}
