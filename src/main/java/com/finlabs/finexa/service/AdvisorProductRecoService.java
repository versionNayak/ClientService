package com.finlabs.finexa.service;


import java.util.List;
import com.finlabs.finexa.dto.AdvisorProductRecoDTO;

public interface AdvisorProductRecoService {
	public List<AdvisorProductRecoDTO> getAllProductRecoByAdvisorId(int advisorID,int clientID,int goalID, String module);
	public AdvisorProductRecoDTO save(int advisorId, int clientId, int goalID, String module, String string);

	

}
