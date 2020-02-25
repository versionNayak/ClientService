package com.finlabs.finexa.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorForgotPassword;
import com.finlabs.finexa.model.ClientFatcaReport;
import com.finlabs.finexa.model.ClientTransactAOFDetail;

public interface ClientTransactAOFDetailsRepository extends JpaRepository<ClientTransactAOFDetail,Integer>{
	
	public ClientTransactAOFDetail getByClientCodeAndSaveMode(String clientCode, String saveMode);
}
