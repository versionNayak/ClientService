package com.finlabs.finexa.repository;



import org.springframework.data.jpa.repository.JpaRepository;


import com.finlabs.finexa.model.LookupEducationalQualification;

public interface EducationalQualificationRepository extends JpaRepository<LookupEducationalQualification, Byte>{

	LookupEducationalQualification findById(byte id);
	LookupEducationalQualification findByDescription(String description);
}
