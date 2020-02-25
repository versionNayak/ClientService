package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.ClientForgotPassword;

public interface ClientForgotPasswordRepository extends JpaRepository<ClientForgotPassword,Integer> {

	ClientForgotPassword findByUUID(String userID);

	@Query("select c.timestamp from ClientForgotPassword c WHERE c.uUID= :uuid")
	public double getTimestampforUUID(@Param("uuid") String uuid);
	
//	@Query("select adv.advisorUser.id from AdvisorForgotPassword adv WHERE adv.uUID= :uuid")
//	public int getAdvisorId(@Param("uuid") String uuid);
}
