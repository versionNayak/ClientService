package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupCountry;

public interface LookupCountryRepository extends JpaRepository<LookupCountry, Integer> {

	LookupCountry findByName(String contents);
	LookupCountry findById(int id);

}
