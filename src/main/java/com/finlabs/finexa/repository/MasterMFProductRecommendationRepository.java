package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.model.MasterMFProductRecommendation;

public interface MasterMFProductRecommendationRepository extends JpaRepository<MasterMFProductRecommendation, String> {
	
	//@Query("select mfProductReco from MasterMFProductRecommendation mfProductReco where mfProductReco.id.advisorUser.id=?1 Order By mfProductReco.lookupAssetSubClass.id")
	//public List<MasterMFProductRecommendation> getAllMFProductRecoSortBySubAssetClass(int advisorId);
	
	//@Query(value="select COUNT(advisorID) from masterMFProductRecommendation WHERE (CURDATE() BETWEEN timeframeStartDate and timeframeEndDate) and advisorID =?1", nativeQuery = true)
	//public int checkForExpriringOFMFMasters( int advisorId);

	//@Query(value ="select mfp.timeframeStartDate,mfp.timeframeEndDate from masterMFProductRecommendation  mfp  WHERE mfp.advisorID =?1  GROUP BY mfp.timeframeStartDate,mfp.timeframeEndDate", nativeQuery = true)
	//public Object[][] getMfProductRecoFundDetails(int advisorId);
	
	//@Query(value ="select etf.fundHouse from masterMutualFundETF etf LEFT JOIN masterMFProductRecommendation mfProReco on mfProReco.isin = etf.isin WHERE mfProReco.subAssetClassID=?2 and mfProReco.advisorID=?1 GROUP BY etf.fundHouse ORDER BY etf.fundHouse desc", nativeQuery = true)
	//public String[] getMfProductRecoFundDetails(int advisorId,int subAssetClassId);
	
	//@Query(value ="select count(subAssetClassID) as totalfund from masterMFProductRecommendation WHERE advisorID =?1 GROUP BY subAssetClassID ORDER BY totalfund DESC LIMIT 1;", nativeQuery = true)
	//public int getFundCount(int advisorId);

	//@Modifying
	//@Transactional
	//@Query(value = "DELETE FROM MasterMFProductRecommendation masterMF WHERE masterMF.id.advisorUser.id=?1")
	//public void deleteMasterMFProductRecoByAdviserUserID(int advisorID);

	//@Query("select mfProductReco from MasterMFProductRecommendation mfProductReco where mfProductReco.id.advisorUser.id= :advisorId Order By mfProductReco.lookupAssetSubClass.id")
	//public List<MasterMFProductRecommendation> getAllMFProductRecoSortBySubAssetClass(@Param("advisorId") int advisorId);
	@Query("select mfProductReco from MasterMFProductRecommendation mfProductReco where mfProductReco.id.advisorMaster.id= :advisorId Order By mfProductReco.lookupAssetSubClass.id")
	public List<MasterMFProductRecommendation> getAllMFProductRecoSortBySubAssetClass(@Param("advisorId") int advisorId);
	
	@Query("select COUNT(mf.id.advisorMaster.id) from MasterMFProductRecommendation mf WHERE (CURRENT_DATE() BETWEEN mf.timeframeStartDate and mf.timeframeEndDate) and mf.id.advisorMaster.id = :advisorId")
	public int checkForExpriringOFMFMasters(@Param("advisorId") int advisorId);

	@Query("select mfp.timeframeStartDate,mfp.timeframeEndDate from MasterMFProductRecommendation  mfp  WHERE mfp.id.advisorMaster.id = :advisorId  GROUP BY mfp.timeframeStartDate,mfp.timeframeEndDate")
	public Object[][] getMfProductRecoFundDetails(@Param("advisorId") int advisorId);
	
	//@Query("SELECT etf.fundHouse FROM MasterMFProductRecommendation  mfProReco LEFT JOIN mfProReco.id.masterMutualFundEtf etf  ON mfProReco.id.masterMutualFundEtf.isin = etf.isin WHERE mfProReco.lookupAssetSubClass.id= :subAssetClassId and mfProReco.id.advisorMaster.id= :advisorId GROUP BY etf.fundHouse ORDER BY etf.fundHouse desc")
	//public String[] getMfProductRecoFundDetails(@Param("advisorId") int advisorId,@Param("subAssetClassId") int subAssetClassId);
	
	
	@Query("SELECT etf.fundHouse FROM MasterMFProductRecommendation  mfProReco LEFT JOIN mfProReco.id.masterMutualFundEtf etf WHERE mfProReco.lookupAssetSubClass.id= :subAssetClassId and mfProReco.id.advisorMaster.id= :advisorId GROUP BY etf.fundHouse ORDER BY etf.fundHouse desc")
	public String[] getMfProductRecoFundDetails(@Param("advisorId") int advisorId, @Param("subAssetClassId") byte subAssetClassId);
	
	
	
	/*
	 * String FIND_PRODUCT_CLASS_ID = "SELECT pc.id FROM ProductClass pc"+
	 * " JOIN pc.ProductGroup pg " + " JOIN pg.Product p " +
	 * " JOIN p.ProductSub ps WHERE ps.id =:childProductSubId";'
	 * 
	 * WHERE mfProReco.id.masterMutualFundEtf.isin = etf.isin
	 */
	
	
	  @Query("select COUNT(mf.lookupAssetSubClass.id) as totalfund from MasterMFProductRecommendation mf WHERE mf.id.advisorMaster.id = :advisorId GROUP BY mf.lookupAssetSubClass.id ORDER BY totalfund DESC") 
	  public List<Long> getFundCount(@Param("advisorId") int advisorId);
	 

	

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM MasterMFProductRecommendation masterMF WHERE masterMF.id.advisorMaster.id=?1")
	public void deleteMasterMFProductRecoByAdviserUserID(int advisorID);
}
