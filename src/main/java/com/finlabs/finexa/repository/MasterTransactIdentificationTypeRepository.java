package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.MasterTransactIdentificationType;

public interface MasterTransactIdentificationTypeRepository extends JpaRepository<MasterTransactIdentificationType, String> {

	MasterTransactIdentificationTypeRepository findBydetails(String details);
}
