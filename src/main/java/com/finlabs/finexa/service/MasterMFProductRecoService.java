package com.finlabs.finexa.service;

import java.io.IOException;
import java.util.List;

import com.finlabs.finexa.dto.MasterMFProductRecoDTO;
import com.finlabs.finexa.dto.MasterProductRecommendationDTO;


public interface MasterMFProductRecoService {

	public List<MasterProductRecommendationDTO> getAllMasterMFProductRecommendation(int advisorId);
	
	public List<MasterProductRecommendationDTO> getAllMasterMutualFundETF();
	
	public List<MasterProductRecommendationDTO> getAllAssetSubAssetClasses();
	
	public byte[] downloadAllMFProductRecommendation (int advisorId) throws IOException ;
	
	public MasterProductRecommendationDTO saveMasterMfProductReco(int advisorId,List<String> isiList,String fromDate,String endDate);
	
	public Object[] getMfProductRecoFundDetails(int advisorId);
	
	public String[] getMfProductRecoSelectedFundDetails(int advisorId,byte subAssetClassId);
	
	public MasterProductRecommendationDTO editMasterMfProductReco(int advisorId,List<String> isiList,String fromDate,String endDate);

	public void saveMasterMfProductReco(List<MasterMFProductRecoDTO> masterMFProductRecommendationList);

	public MasterProductRecommendationDTO saveMasterMfProductRecommendation(int advisorId, List<String> isinList,
			String fromDate, String endDate);
}
