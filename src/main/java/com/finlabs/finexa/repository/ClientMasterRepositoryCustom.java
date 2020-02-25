package com.finlabs.finexa.repository;

import java.util.List;

import com.finlabs.finexa.dto.ClientSearchDTO;
import com.finlabs.finexa.model.ClientMaster;

public interface ClientMasterRepositoryCustom {
	List<ClientMaster> searchClient(ClientSearchDTO searchObj);
}
