package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.finlabs.finexa.model.MasterProductClassification;
import java.lang.String;
import java.util.List;

public interface MasterProductClassificationRepository extends JpaRepository<MasterProductClassification, Byte>{

	     List<MasterProductClassification> findByLockedInFlag(String lockedinflag);
	     MasterProductClassification findByProductName(String productName);
}
