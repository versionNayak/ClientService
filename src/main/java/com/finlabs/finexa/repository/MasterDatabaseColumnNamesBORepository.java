package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.MasterDatabaseColumnNamesBO;

public interface MasterDatabaseColumnNamesBORepository  extends JpaRepository<MasterDatabaseColumnNamesBO, Integer> {
	
	@Query("SELECT mdcb.id, mdcb.columnName FROM MasterDatabaseColumnNamesBO mdcb WHERE mdcb.lookupRtamasterFileDetailsBo.id = :feedTypeID")
	public List<Object []> getIDAndDatabaseColumnNames(@Param("feedTypeID") Integer feedTypeID);

}
