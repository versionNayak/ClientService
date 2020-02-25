package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.dto.UserDTO;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorUser;

public interface AdvisorMasterRepository extends JpaRepository<AdvisorMaster, Integer>{

//	@Query("SELECT advUser.id,advRole.roleDescription,adusm.advisorUser2.id,advSuperAdvisorRole.roleDescription FROM AdvisorUserSupervisorMapping adusm,AdvisorRole advSuperAdvisorRole LEFT JOIN adusm.advisorUser1 advUser LEFT JOIN advUser.advisorMaster advm LEFT JOIN advUser.advisorUserRoleMappings adRoleMap LEFT JOIN adRoleMap.advisorRole advRole  WHERE advm.id =:advisorId AND advSuperAdvisorRole.supervisorRoleID= adusm.advisorUser2.id ORDER BY advUser.id")
//	public List<Object[]> getAllUserRoleWithSupervisorRole(@Param("advisorId") int advisorId);
	/*
	 * @Query(value = "SELECT advUser.ID as userId, advUser.firstName as userName,"
	 * +
	 * "advUserRoleMapping.roleID as userRoleId,advRole.roleDescription as userRoleDesc,"
	 * +
	 * "advUser1.firstName as userSupervisorName,advUserSuperMap.supervisorID as userSupervisorId,"
	 * +
	 * "advRole1.roleDescription as userSupervisorRoleDesc FROM advisorUser advUser "
	 * + "left join advisorMaster advMaster on advMaster.ID = advUser.advisorID " +
	 * "left join advisorUserRoleMapping advUserRoleMapping on advUserRoleMapping.userID = advUser.ID "
	 * + "left join advisorRole advRole on advRole.ID=advUserRoleMapping.roleID " +
	 * "left join advisorUserSupervisorMapping advUserSuperMap on advUserSuperMap.userID = advUser.ID "
	 * +
	 * "left join advisorUserRoleMapping advRoleMap1 on advUserSuperMap.supervisorID = advRoleMap1.userID "
	 * + "left join advisorRole advRole1 on advRoleMap1.roleID = advRole1.ID " +
	 * "left join advisorUser advUser1 on advUser1.ID = advUserSuperMap.supervisorID "
	 * + "where advMaster.ID=?1", nativeQuery = true) public List<Object[]>
	 * getAllUserRoleWithSupervisorRole(int advisorId);
	 */
	
	@Query("select advUser.id as supervisorID, concat(advUser.firstName,' ',ifnull(advUser.middleName,''),' ',advUser.lastName) as supervisorName "
			+ "from AdvisorMaster advMaster "
			+ "left join advMaster.advisorRoles advRole "
			+ "left join advMaster.advisorUsers advUser "
			+ "left join advUser.advisorUserRoleMappings advUserRoleMap "
			+ "where advRole.id= :roleId and advMaster.id= :advisorId")
	public List<Object[]> getOtherSupervisorsWithSameRole(@Param("advisorId") int advisorId, @Param("roleId") int roleId);

	public AdvisorMaster findByOrgNameAndDistributorCode(String orgName, String distCode);

	public AdvisorMaster findByOrgName(String contents);
	
	public List<AdvisorMaster> findByOrgFlag(String orgFlag);

	public AdvisorMaster findById(int advisorId);
	
	/*
	 * @Query("select advMaster.id as orgName," + "advUser.firstName as userName " +
	 * "from AdvisorRole advRole " +
	 * "left join AdvisorUserRoleMapping advUserRoleMap on advUserRoleMap.advisorRole.id = advRole.id "
	 * +
	 * "left join AdvisorUser advUser on advUser.id = advUserRoleMap.advisorUser.id "
	 * +
	 * "left join AdvisorMaster advMaster on advMaster.id = advRole.advisorMaster.id  "
	 * + "where advRole.ID= :roleId and advMaster.id= :orgName") public
	 * List<Object[]> getUsersByOrgAndRole(@Param("orgName") int
	 * orgName, @Param("roleId") int roleId) throws RuntimeException;
	 */
//unused query	
	//@Query(value = "select advMaster.ID as orgName,"
		//	+ "advUser.firstName as userName "
		//	+ "from advisorRole advRole "
		//	+ "left join advisorUserRoleMapping advUserRoleMap on advUserRoleMap.roleID = advRole.ID "
		//	+ "left join advisorUser advUser on advUser.ID = advUserRoleMap.userID "
		//	+ "left join advisorMaster advMaster on advMaster.id = advRole.advisorID  "
		//	+ "where advRole.ID=?2 and advMaster.id=?1", nativeQuery = true)
	//public List<Object[]> getUsersByOrgAndRole(int orgName, int roleId) throws RuntimeException;
	
	//public List<AdvisorMaster> findByDistributorCode(String distCode);
	
	public AdvisorMaster findByDistributorCode(String distCode);

	public long countByOrgFlag(String orgFlag);

	public List<AdvisorMaster> findByOrgFlag(String orgFlag, Pageable pageable);

}
