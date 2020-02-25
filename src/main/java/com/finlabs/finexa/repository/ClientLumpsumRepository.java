package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.finlabs.finexa.model.ClientLumpsumInflow;

public interface ClientLumpsumRepository  extends JpaRepository<ClientLumpsumInflow, Integer> {

}
