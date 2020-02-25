package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.MasterFinexaException;

public interface MasterFinexaExceptionRepository extends JpaRepository<MasterFinexaException, Integer> {

}
