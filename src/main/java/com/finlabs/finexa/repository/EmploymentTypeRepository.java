package com.finlabs.finexa.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupEmploymentType;



public interface EmploymentTypeRepository extends JpaRepository<LookupEmploymentType, Byte>{

	LookupEmploymentType findById(Byte id);
	LookupEmploymentType findByDescription(String description);
}
