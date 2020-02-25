package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.MasterFeedColumnNamesBO;

public interface MasterFeedColumnNamesBORepository extends JpaRepository<MasterFeedColumnNamesBO, Integer> {

	@Query("SELECT mfcb.masterDatabaseColumnNamesBo.id, mfcb.feedFileColumnName FROM MasterFeedColumnNamesBO mfcb WHERE mfcb.lookupRtamasterFileDetailsBo.id = :feedTypeID")
	public List<Object []> getIDAndFeedColumnNames(@Param("feedTypeID") Integer feedTypeID);
	
}
