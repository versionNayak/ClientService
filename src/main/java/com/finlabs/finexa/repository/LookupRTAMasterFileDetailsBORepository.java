package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupRTAMasterFileDetailsBO;

public interface LookupRTAMasterFileDetailsBORepository extends JpaRepository<LookupRTAMasterFileDetailsBO, Integer> {

	LookupRTAMasterFileDetailsBO findByFileCode(String fileCode);

}
