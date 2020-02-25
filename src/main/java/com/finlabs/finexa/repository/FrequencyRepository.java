package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.LookupFrequency;



public interface FrequencyRepository extends JpaRepository<LookupFrequency, Byte>{
	@Query("SELECT l FROM LookupFrequency l ORDER BY l.displayOrder ASC")	 
    public List<LookupFrequency> findAllByOrderBydisplayOrderAsc();
}