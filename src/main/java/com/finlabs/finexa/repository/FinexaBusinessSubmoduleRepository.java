package com.finlabs.finexa.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.FinexaBusinessSubmodule;

public interface FinexaBusinessSubmoduleRepository extends JpaRepository<FinexaBusinessSubmodule, Integer>{

	FinexaBusinessSubmodule findByCode(String code);
	FinexaBusinessSubmodule findByDescription(String description);
	List<FinexaBusinessSubmodule> findByFinexaBusinessModule(int moduleID);
}
