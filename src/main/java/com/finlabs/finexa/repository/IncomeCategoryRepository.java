package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupIncomeCategory;


public interface IncomeCategoryRepository extends JpaRepository<LookupIncomeCategory, Integer> {
	
	LookupIncomeCategory  findByDescriptionContaining(String description);

}
