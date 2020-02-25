package com.finlabs.finexa.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientMaster;

public interface ClientMasterRepository
		extends JpaRepository<ClientMaster, Integer>, JpaSpecificationExecutor<ClientMaster> {

	List<ClientMaster> findByActiveFlag(String flag);

	List<ClientMaster> findByAdvisorUserAndActiveFlag(AdvisorUser user, String flag);

	ClientMaster findByPan(String pan);

	ClientMaster findByAadhar(BigInteger aadhar);

	ClientMaster findByAadhar(String aadhar);

	@Query("FROM ClientMaster As c where c.firstName like :firstName or c.middleName like :middleName or c.lastName like :lastName")
	List<ClientMaster> search(@Param("firstName") String firstName, @Param("middleName") String middleName,
			@Param("lastName") String lastName);

	List<ClientMaster> findByFirstNameLike(String firstName);

	List<ClientMaster> findByGenderLike(String gender);

	List<ClientMaster> findByRetiredFlagLike(String retiredFlag);

	@Query("SELECT cm FROM ClientMaster cm WHERE cm.lookupMaritalStatus.id=:maritalStatusId")
	public List<ClientMaster> findByMaritalStatus(@Param("maritalStatusId") int maritalStatusId);
     //unused (same code available in clientRecordserviceimpl with getAllUserNameWithClientNameByUserId())
	
	//@Query(value = "SELECT * FROM clientMaster where userID=?1 and firstName like CONCAT('%', ?2, '%') ", nativeQuery = true)
	//public List<ClientMaster> getAllClients(int userID, String firstName);

	@Query("SELECT cm FROM ClientMaster cm WHERE cm.lookupCountry.id=:countryId ")
	public List<ClientMaster> findByCountry(@Param("countryId") int countryId);

	//@Query(value = "select advUser.ID as userID, clientMas.ID as clientID, concat(clientMas.firstName,' ',ifnull(clientMas.middleName,''),' ',clientMas.lastName) as clientName, "
		//	+ "clientMas.countryOfResidence as Country, clientCont.emailID Email, clientCont.mobile Mobile, "
		//	+ "clientCont.permanentCity City, clientCont.permanentState State, "
		//	+ "clientCont.permanentAddressLine1 Address, clientMas.gender as Gender " + "FROM clientMaster clientMas "
		//	+ "left join clientContact  clientCont on  clientCont.clientID = clientMas.ID "
		//	+ "left join advisorUser  advUser on  advUser.ID = clientMas.userID "
		//	+ "where clientMas.ID=?1", nativeQuery = true)
	//public List<Object[]> getAllClientNameByClientId(int clientID);

	public List<ClientMaster> findByOrgNameIsNotNull();

	public ClientMaster getByFirstNameAndLastName(String string, String string2);

	public ClientMaster getByPanAndActiveFlag(String pan, String activeflag);

	public ClientMaster getByAadharAndActiveFlag(String aadhar, String activeflag);

	public List<ClientMaster> getByActiveFlag(String activeflag);

	/*
	 * @Query(value =
	 * "SELECT * FROM clientMaster WHERE createdOn  BETWEEN SYSDATE() - INTERVAL 7 DAY AND SYSDATE() AND userID=?1 AND activeFlag='Y'"
	 * , nativeQuery = true) public List<ClientMaster>
	 * getAddedClientsForLast1Week(int userId);
	 */

//	@Query(value = "SELECT * FROM clientMaster WHERE createdOn  BETWEEN SYSDATE() - INTERVAL 7 DAY AND SYSDATE() AND userID in :ids AND activeFlag='Y'", nativeQuery = true)
//	public List<ClientMaster> getAddedClientsForLast1Week(@Param("ids") List<Integer> ids);
//	
//	@Query(value = "SELECT * FROM clientMaster WHERE createdOn  BETWEEN SYSDATE() - INTERVAL 1 MONTH AND SYSDATE() AND userID in :ids AND activeFlag='Y'", nativeQuery = true)
//	public List<ClientMaster> getAddedClientsForLast1Month(@Param("ids") List<Integer> ids);
//
//	@Query(value = "SELECT * FROM clientMaster WHERE createdOn  BETWEEN SYSDATE() - INTERVAL 3 MONTH AND SYSDATE() AND userID in :ids AND activeFlag='Y'", nativeQuery = true)
//	public List<ClientMaster> getAddedClientsForLast3Month(@Param("ids") List<Integer> ids);
//
//	@Query(value = "SELECT * FROM clientMaster WHERE createdOn  BETWEEN SYSDATE() - INTERVAL 6 MONTH AND SYSDATE() AND userID in :ids AND activeFlag='Y'", nativeQuery = true)
//	public List<ClientMaster> getAddedClientsForLast6Month(@Param("ids") List<Integer> ids);
//
//	@Query(value = "SELECT * FROM clientMaster WHERE createdOn  BETWEEN SYSDATE() - INTERVAL 1 YEAR AND SYSDATE() AND userID in :ids AND activeFlag='Y'", nativeQuery = true)
//	public List<ClientMaster> getAddedClientsForLast1YEAR(@Param("ids") List<Integer> ids);
//
//	@Query(value = "SELECT * FROM clientMaster WHERE createdOn BETWEEN SYSDATE() - INTERVAL 14 DAY AND SYSDATE() AND CURDATE( ) AND userID in :ids AND activeFlag='Y'", nativeQuery = true)
//	public List<ClientMaster> getAddedClientsForLast1Fortnight(@Param("ids") List<Integer> ids);

//	@Query(value = "SELECT * FROM  clientMaster WHERE  DATE_ADD(birthDate,INTERVAL YEAR(CURDATE())-YEAR(birthDate) + IF(DAYOFYEAR(CURDATE()) > DAYOFYEAR(birthDate),1,0)YEAR)  BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 1 WEEK) AND userID in :ids AND activeFlag='Y'", nativeQuery = true)
//	public List<ClientMaster> getClientsBirthDayForNext1Week(@Param("ids") List<Integer> ids);

	
	/*
	 * @Query(value =
	 * "SELECT * FROM  clientMaster WHERE  DATE_ADD(birthDate,INTERVAL YEAR(CURDATE())-YEAR(birthDate) + IF(DAYOFYEAR(CURDATE()) > DAYOFYEAR(birthDate),1,0)YEAR)  BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 1 MONTH) AND userID=?1 AND activeFlag='Y'"
	 * , nativeQuery = true) public List<ClientMaster>
	 * getClientsBirthDayForNext1Month(int userID);
	 */
	
//	@Query(value = "SELECT * FROM  clientMaster WHERE  DATE_ADD(birthDate,INTERVAL YEAR(CURDATE())-YEAR(birthDate) + IF(DAYOFYEAR(CURDATE()) > DAYOFYEAR(birthDate),1,0)YEAR)  BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 1 MONTH) AND userID in :ids AND activeFlag='Y'", nativeQuery = true)
//	public List<ClientMaster> getClientsBirthDayForNext1Month(@Param("ids") List<Integer> ids);
//	 
//	@Query(value = "SELECT * FROM  clientMaster WHERE  DATE_ADD(birthDate,INTERVAL YEAR(CURDATE())-YEAR(birthDate) + IF(DAYOFYEAR(CURDATE()) > DAYOFYEAR(birthDate),1,0)YEAR)  BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 MONTH) AND userID in :ids AND activeFlag='Y'", nativeQuery = true)
//	public List<ClientMaster> getClientsBirthDayForNext3Month(@Param("ids") List<Integer> ids);
//
//	@Query(value = "SELECT * FROM  clientMaster WHERE  DATE_ADD(birthDate,INTERVAL YEAR(CURDATE())-YEAR(birthDate) + IF(DAYOFYEAR(CURDATE()) > DAYOFYEAR(birthDate),1,0)YEAR)  BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 6 MONTH) AND userID in :ids AND activeFlag='Y'", nativeQuery = true)
//	public List<ClientMaster> getClientsBirthDayForNext6Month(@Param("ids") List<Integer> ids);
//
//	@Query(value = "SELECT * FROM  clientMaster WHERE  DATE_ADD(birthDate,INTERVAL YEAR(CURDATE())-YEAR(birthDate) + IF(DAYOFYEAR(CURDATE()) > DAYOFYEAR(birthDate),1,0)YEAR)  BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 1 YEAR) AND userID in :ids AND activeFlag='Y'", nativeQuery = true)
//	public List<ClientMaster> getClientsBirthDayForNext1YEAR(@Param("ids") List<Integer> ids);
//
//	@Query(value = "SELECT * FROM  clientMaster WHERE  DATE_ADD(birthDate,INTERVAL YEAR(CURDATE())-YEAR(birthDate) + IF(DAYOFYEAR(CURDATE()) > DAYOFYEAR(birthDate),1,0)YEAR)  BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 2 WEEK) AND userID in :ids AND activeFlag='Y'", nativeQuery = true)
//	public List<ClientMaster> getClientsBirthDayNext1Fortnight(@Param("ids") List<Integer> ids);

	public List<ClientMaster> findByCreatedOnBetween(Date lastDate, Date curDate);
	
	/*
	 * @Query("SELECT c FROM ClientMaster c  WHERE c.activeFlag = :activeFlag AND c.advisorUser.id in :ids"
	 * ) List<ClientMaster> findByAdvisorIdsViewOnDemand(@Param("ids") List<Integer>
	 * ids,
	 * 
	 * @Param("activeFlag") String activeFlag, Pageable pageable);
	 * 
	 * @Query("SELECT c FROM ClientMaster c  WHERE c.activeFlag = :activeFlag AND c.advisorUser.id in :ids"
	 * ) List<ClientMaster> findByAdvisorIds(@Param("ids") List<Integer>
	 * ids, @Param("activeFlag") String activeFlag);
	 */
	
	@Query("select c FROM ClientMaster c  where c.advisorUser.id in :ids")
	List<ClientMaster> findByAdvisorIdsViewOnDemand(@Param("ids") List<Integer> ids,Pageable pageable);

	@Query("select c FROM ClientMaster c  where c.advisorUser.id in :ids and c.activeFlag = :activeFlag")
	List<ClientMaster> findByAdvisorIds(@Param("ids") List<Integer> ids, @Param("activeFlag") String activeFlag);
	
	/*
	 * @Query(value =
	 * "select * from clientMaster where userID in :ids and c.activeFlag = :activeFlag"
	 * ,nativeQuery = true) List<ClientMaster> findByAdvisorIds1(@Param("ids")
	 * List<Integer> ids, @Param("activeFlag") String activeFlag);
	 */
	/*
	 * @Query("select c FROM ClientMaster c  where c.advisorUser.id in :ids and c.activeFlag = :activeFlag"
	 * ) List<ClientMaster> findByAdvisorIds3(@Param("ids") List<Integer>
	 * id, @Param("activeFlag") String activeFlag);
	 * 
	 */
	@Query("select c FROM ClientMaster c  where c.advisorUser.id in :ids and c.activeFlag = :activeFlag")
	List<ClientMaster> findByAdvisorIds(@Param("ids") List<Integer> ids, @Param("activeFlag") String activeFlag, Pageable pageable);
	
	@Query("SELECT cm.id FROM ClientMaster cm WHERE cm.firstName = :firstName AND cm.pan = :invPAN AND cm.activeFlag = :activeFlag")
	public Integer findByNamePANAndFlag (@Param("firstName") String firstName, @Param("invPAN") String invPAN, @Param("activeFlag") String activeFlag);

	public ClientMaster findByPanAndAdvisorUserAndActiveFlag(String pan, AdvisorUser advisorUser, String string);

	public ClientMaster findByAadharAndAdvisorUserAndActiveFlag(String aadhar, AdvisorUser advisorUser, String string);
	 
	 ClientMaster findByLoginUsernameAndLoginPassword(String username, String password);
	
	ClientMaster findByLoginUsernameAndActiveFlag(String username, String flag);

	ClientMaster findByLoginUsername(String username);

	public List<ClientMaster> findByAdvisorUserAndOrgNameIsNotNull(AdvisorUser advisorUser);

	public List<ClientMaster> findByAdvisorUser(AdvisorUser advisorUser);
	
	@Query("SELECT c FROM ClientMaster c  where c.advisorUser.id = :advisorID and c.pan = :investorPAN")
	public ClientMaster findByAdvisorIdAndPan(@Param("advisorID") int advisorID, @Param("investorPAN") String investorPAN);

	List<ClientMaster> findByAdvisorUserAndFirstNameContainingIgnoreCase(AdvisorUser adv, String firstName);

	List<ClientMaster> findByAdvisorUserAndLastNameContainingIgnoreCase(AdvisorUser adv, String laststName);

//	@Query("select c FROM ClientMaster c  where c.advisorUser.id in ?1 and c.activeFlag = ?2 and "
//			+ "c.firstName like %?#{[2].toUpperCase()}% "
//			+ "or c.middleName like %?#{[3].toUpperCase()}% or c.lastName like %?#{[4].toUpperCase()}%")
//	List<ClientMaster> findByAdvisorIdsAndNameContaingIgnoreCase(List<Integer> idList, String activeFlag, String firstName, String middleName, 
//			String lastName);
	
	//@Query("select c FROM ClientMaster c  where c.advisorUser.id in ?1 and c.firstName like %?#{[1].toUpperCase()}%")
	//List<ClientMaster> findByAdvisorIdsAndNameContaingIgnoreCase(List<Integer> idList, String firstName);
	
	@Query("select c FROM ClientMaster c  where c.advisorUser.id in ?1 and "
			+ "(c.firstName like %?#{[1].toUpperCase()}% OR "
			+ "c.middleName like %?#{[1].toUpperCase()}% OR "
			+ "c.lastName like %?#{[1].toUpperCase()}%)")
	List<ClientMaster> findByAdvisorIdsAndNameContaingIgnoreCase(List<Integer> idList, String firstName);
	
	@Query("select c FROM ClientMaster c  where c.advisorUser.id in ?1 "
			+ "AND (c.firstName like %?#{[1].toUpperCase()}%) "
			+ "AND ((c.middleName like %?#{[2].toUpperCase()}%) "
			+ "OR (c.lastName like %?#{[2].toUpperCase()}%))")
	List<ClientMaster> findByAdvisorIdsAndNameContaingIgnoreCase(List<Integer> idList, String firstName, String secondName);
	
		
//	@Query("select c FROM ClientMaster c  where c.advisorUser.id in ?1 "
//			+ "AND (c.firstName like %?#{[1].toUpperCase()}%) "
//			+ "AND ((c.middleName like %?#{[2].toUpperCase()}% OR (c.lastName like %?#{[2].toUpperCase()}%)) "
//			+ "AND (c.lastName like %?#{[2].toUpperCase()}%) ")
//	List<ClientMaster> findByAdvisorIdsAndNameContaingIgnoreCase(List<Integer> idList, String firstName, String secondName, String thirdName);
	
	
	@Query("select c FROM ClientMaster c  where c.advisorUser.id in ?1 "
			+ "AND (c.firstName like %?#{[1].toUpperCase()}%) "
			+ "AND (c.middleName like %?#{[2].toUpperCase()}%) "
			+ "AND (c.lastName like %?#{[3].toUpperCase()}%)")
	List<ClientMaster> findByAdvisorIdsAndNameContaingIgnoreCase(List<Integer> idList, String firstName, String secondName, String thirdName);
	
	
//	@Query("select distinct mmfetf.isin,mmfetf.fundHouse,mmfetf.schemeName,mmfetf.amfiCode,"
//			+ "mmfetf.lookupAssetClass.id,mmfetf.lookupAssetSubClass.id,mmfetf.schemeOption, "
//			+ "mmfetf.closeEndedFlag, mmfetf.schemeInceptionDate,mmfetf.regularDirectFlag,"
//			+ "mmfetf.schemeEndDate, mmfetf.exitLoadAndPeriod, mmfetf.minInvestmentAmount, "
//			+ "mmfetf.masterFundManager.managerCode, mmfetf.masterIndexName.id, mmfetf.status ,asim.series "
//			+ "from MasterMutualFundETF mmfetf, AccordSchemeIsinMaster asim where "
//			+ "mmfetf.isin = asim.id.isin and mmfetf.status = 'Active' "
//			+ "and asim.status = 'NEW' and mmfetf.fundHouse = :fundHouse "
//			+ "and mmfetf.schemeName like :matchString ")
//	List<Object[]> findForSecondDropdown(@Param(value = "matchString") String matchString,@Param(value = "fundHouse") String fundHouse);

	@Query("select c FROM ClientMaster c  where c.advisorUser.id in ?1 and c.loginUsername like %?#{[1].toUpperCase()}%")
	List<ClientMaster> findByAdvisorIdsAndLoginUsernameContaingIgnoreCase(List<Integer> idList, String matchString);

	public List<ClientMaster> findByAdvisorUserAndFinexaUser(AdvisorUser advisorUser, String isFinexaUserOrNot);
	
	public List<ClientMaster> getByAdvisorUserAndActiveFlag(AdvisorUser advisorUser, String flag);
	ClientMaster findById(Integer clientId);
}
