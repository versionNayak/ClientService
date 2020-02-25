package com.finlabs.finexa.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.finlabs.finexa.model.ClientMeeting;

public interface ClientMeetingRepository extends JpaRepository<ClientMeeting, Integer>,JpaSpecificationExecutor<ClientMeeting> {

	
}
