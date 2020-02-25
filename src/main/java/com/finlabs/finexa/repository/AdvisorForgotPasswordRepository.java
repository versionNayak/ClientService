package com.finlabs.finexa.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorForgotPassword;

public interface AdvisorForgotPasswordRepository extends JpaRepository<AdvisorForgotPassword,Integer>
{
	
	AdvisorForgotPassword findByUUID(String uuid);
	
	@Query("select adv.timestamp from AdvisorForgotPassword adv WHERE adv.uUID= :uuid")
	public double getTimestampforUUID(@Param("uuid") String uuid);
	
	@Query("select adv.advisorUser.id from AdvisorForgotPassword adv WHERE adv.uUID= :uuid")
	public int getAdvisorId(@Param("uuid") String uuid);
	
	@Modifying
	@Query(value="DELETE from AdvisorForgotPassword adv WHERE adv.uUID= :uuid")
	public void deleteRow(@Param("uuid") String uuid);
}
