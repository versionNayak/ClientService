package com.finlabs.finexa.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.finlabs.finexa.model.ClientTask;

public interface ClientTaskRepository extends JpaRepository<ClientTask, Integer>,JpaSpecificationExecutor<ClientTask> {

}
