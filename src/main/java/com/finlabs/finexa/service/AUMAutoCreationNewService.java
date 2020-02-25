package com.finlabs.finexa.service;

import java.util.Map;

public interface AUMAutoCreationNewService {
	
	public void createAutoAUM (int advisorID, Map<String, String> clientPANMap, Map<String, String> clientFolioNameMap) ;

}
