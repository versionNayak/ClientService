package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.finlabs.finexa.model.LookupMonth;

public interface MonthRepository extends JpaRepository<LookupMonth, Byte>{
	@Query("SELECT l FROM LookupMonth l ORDER BY l.displayOrder ASC")	 
    public List<LookupMonth> findAllByOrderBydisplayOrderAsc();
}
