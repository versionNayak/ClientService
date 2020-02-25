package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.MasterFileMappingBO;


public interface MasterFileMappingBORepository extends JpaRepository<MasterFileMappingBO, Integer>{
	
	//List<MasterFileMappingBO> findByLookupRtaBO();
	
	@Query("select mfmbo.id,mfmbo.extension,mfmbo.lookupRtabo.id,mfmbo.lookupRtamasterFileDetailsBo.id,mfmbo.lookupRtafileName.id "
			+ "FROM MasterFileMappingBO mfmbo where mfmbo.lookupRtabo.id=?1 and mfmbo.lookupRtamasterFileDetailsBo.id = ?2")
	public List<Object[]> getAllDataByRTAAndFileName(int rtaId, int masterId);
	
	
	
}
