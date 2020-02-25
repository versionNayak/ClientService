package com.finlabs.finexa.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.finlabs.finexa.model.FinexaBusinessModule;

public interface FinexaBusinessModuleRepository extends JpaRepository<FinexaBusinessModule, Integer>{

	FinexaBusinessModule findByCode(String moduleCode);
	FinexaBusinessModule findByDescription(String moduleName);
}
