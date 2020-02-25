package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupCountry;

public interface CountryRepository extends JpaRepository<LookupCountry, Integer>{

	LookupCountry findByName(String contents);
}
