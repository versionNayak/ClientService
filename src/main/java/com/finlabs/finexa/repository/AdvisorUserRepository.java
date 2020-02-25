package com.finlabs.finexa.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.User;

public interface AdvisorUserRepository extends JpaRepository<AdvisorUser, Integer>{
	AdvisorUser findByEmailIDAndLoginPassword(String emailId,String password);
	//AdvisorUser forgotPasswordOfUser(String emailId);
    AdvisorUser findByEmailID(String emailID);
    AdvisorUser findByLoginUsername(String loginUsername);
    AdvisorUser findByLoginUsernameAndLoginPassword(String loginUsername,String loginPassword);
//	String findEmailID(String emailID);
	List<AdvisorUser> findByAdvisorMaster(AdvisorMaster advisorMaster);
	AdvisorUser findByPhoneNo(BigInteger phoneNo);
	AdvisorUser findByEmployeeCode(String employeeCode);
	
	/*
	 * @Query(value =
	 * "select advUser.ID as userID, concat(advUser.firstName,' ',ifnull(advUser.middleName,''),' ',advUser.lastName) as userName, "
	 * +
	 * "clientMas.ID as clientID, concat(clientMas.firstName,' ',ifnull(clientMas.middleName,''),' ',clientMas.lastName) as clientName, advUser.city as location "
	 * + "FROM clientMaster clientMas " +
	 * "left join advisorUser advUser on advUser.ID = clientMas.userID  " +
	 * "where advUser.ID=?1 and clientMas.firstName like CONCAT('%',?2,'%')",
	 * nativeQuery = true) public List<Object[]>
	 * getAllUserNameWithClientNameByUserId(int userId,String firstName);
	 */
			public AdvisorUser getByFirstNameAndLastName(String string, String string2);
			AdvisorUser findByFirstNameAndLastName(String fn, String ln);
			
			
			List<AdvisorUser> findByEmailIDAndIdNot(String emailID, int userId);
			@Modifying
			@Query("UPDATE AdvisorUser adv SET adv.loginPassword= :password where adv.id= :advisor_id")
			public void updatepassword(@Param("advisor_id") int advisor_id,@Param("password") String password);
			
	        // New Query added for MF transact
	
	        @Modifying
	        @Query("UPDATE AdvisorUser adv SET adv.bseUsername= :username, adv.bseMemberId= :memberCode, adv.bsePassword= :password WHERE adv.id= :advisorId") 
	        public void updateBSEDetails(@Param("username") String username, @Param("memberCode") String memberCode, @Param("password") byte[] password, @Param("advisorId") int advisorId);
	
			@Modifying
			@Query("UPDATE AdvisorUser adv SET adv.bseUsername=NULL, adv.bseMemberId=NULL, adv.bsePassword=NULL WHERE adv.id= :advisorId")
			public void deleteBSEDetails(@Param("advisorId") int advisorId);
			//AdvisorUser findByAdvisorId(int id);
			AdvisorUser findByAdvisorMaster(int id);
			AdvisorUser findByUser(User user);
			AdvisorUser findById(int id);
			List<AdvisorUser> findByAdvisorMasterAndEmployeeCode(AdvisorMaster advMaster, String empCode);
			
			//pagination
//			@Query("SELECT COUNT(a) FROM advisorUser a")
//		    int getAdvisorUserCount();
			Page<AdvisorUser> findAll(Pageable pageable);
			List<Object[]> findByIdAndFirstName(int userId, String firstName);
			List<AdvisorUser> findByAdvisorMaster(AdvisorMaster master, Pageable pageable);
			List<AdvisorUser> findByfirstNameContainingIgnoreCase(String matchString);
			List<AdvisorUser> findByPhoneNoAndIdNot(BigInteger mobile, int id);
			
			@Query("SELECT adv.phoneNo FROM AdvisorUser adv")
			List<BigInteger> getAllPhoneNo();
			
			@Query("SELECT adv.emailID FROM AdvisorUser adv")
			List<String> getAllEmailID();
			
}

