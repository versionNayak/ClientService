package com.finlabs.finexa.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import com.finlabs.finexa.model.CacheInfoDTO;
import com.finlabs.finexa.model.UserClientRedis;
import com.finlabs.finexa.model.UserInRedis;

@Component
public class CacheInfoService {
	
	@Autowired
	private RedisTemplate<String, UserInRedis> redisTemplate1;
	@Autowired
	private RedisTemplate<String, CacheInfoDTO> redisTemplate;

	
	public Map<String,List<?>> getCacheMap(String typeConstant, String tokenId, String subTypeConstant) {
		CacheInfoDTO cacheObject = (CacheInfoDTO) redisTemplate.opsForHash().get(typeConstant, tokenId);
		Map<String, Map<String,List<?>>> calculatedMethodMap = null;
		Map<String,List<?>> unknownTypeMap = null;
		if (cacheObject != null) {
			calculatedMethodMap = cacheObject.getCalculatedMethodMap();

			if (calculatedMethodMap != null && calculatedMethodMap.get(subTypeConstant) != null) {
				unknownTypeMap = calculatedMethodMap.get(subTypeConstant);
			}
		}
		return unknownTypeMap;
	}
	
	public List<?> getCacheList(String typeConstant, String tokenId, String subTypeConstant) {
		CacheInfoDTO cacheObject = (CacheInfoDTO) redisTemplate.opsForHash().get(typeConstant, tokenId);
		Map<String, List<?>> calculatedMethodListMap = null;
		List<?> unknownTypeList = null;
		if (cacheObject != null) {
			calculatedMethodListMap = cacheObject.getCalculatedMethodListMap();

			if (calculatedMethodListMap.get(subTypeConstant) != null) {
				unknownTypeList = calculatedMethodListMap.get(subTypeConstant);
			}
		}
		return unknownTypeList;
	}

	
	  //   PortFolio redis cache
	  public void deleteCache(String typeConstant, int clientID) {
	  redisTemplate.opsForHash().delete(typeConstant,clientID);
	  redisTemplate.delete(typeConstant);
	  }
	 
	
	// ====================After LogIn (SessionStoreage)===========================

	public void addTokenCacheMap(String token, UserInRedis userDetailsDTO) {

		try {

			ValueOperations<String, UserInRedis> values = redisTemplate1.opsForValue();
			values.set(token, userDetailsDTO);
			redisTemplate1.expire(token, 3600, TimeUnit.SECONDS);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UserInRedis getTokenCacheMap(String header) {
		String token = getToken(header);

		if (header != null && header.startsWith(Constants.TOKEN_PREFIX)) {
			token = header.replace(Constants.TOKEN_PREFIX, "");

		}
		ValueOperations<String, UserInRedis> values = redisTemplate1.opsForValue();
		UserInRedis userDetailsDTO = (UserInRedis) values.get(token);
		System.out.println("advisorDTO from redis " + userDetailsDTO);
		if (userDetailsDTO != null) {
			System.out.println(
					"advisorDTO from redis " + userDetailsDTO.getUsername() + " " + userDetailsDTO.getEmailID());
		}
		System.out.println("token" + token);
		return userDetailsDTO;

	}

	public String getToken(String header) {
		String token = "";
		if (header != null && header.startsWith(Constants.TOKEN_PREFIX))
			token = header.replace(Constants.TOKEN_PREFIX, "");
		return token;
	}

	public void deleteTokenCacheMap(String tokenId) {
		try {
			redisTemplate1.delete(tokenId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	// ===================END====================================

	// ====================ViewOnDemand===========================

	public void addClientCacheMap(String tokenId, UserClientRedis userInRedis) {
		redisTemplate1.opsForHash().put(Integer.toString(userInRedis.getId()), tokenId, userInRedis);
		// redisTemplate1.expire(tokenId, 2400, TimeUnit.MILLISECONDS);
		System.out.println("advisorDTO " + userInRedis.getClientmastersTotalRedis().size());

	}

	public UserClientRedis getClientCacheMap(String tokenId, int id) {
		UserClientRedis userClientRedis = (UserClientRedis) redisTemplate1.opsForHash().get(Integer.toString(id),
				tokenId);	
		return userClientRedis;

	}

	public void deleteClientCacheMap(int id,String tokenId) {
		redisTemplate1.opsForHash().delete(Integer.toString(id), tokenId);

	}
	// ===================END====================================

}

/*
 * public void addCacheMap(String tokenId, AdvisorDTO advisorDTO) { advisorDTO =
 * (AdvisorDTO) redisTemplate.opsForHash().get(tokenId, advisorDTO.getId());
 * System.out.println("advisorDTO "+advisorDTO.getId()); //
 * System.out.println("advisorDTO "+advisorDTO.getId());
 * redisTemplate.opsForHash().put(tokenId, advisorDTO.getId(), advisorDTO);
 * advisorDTO = (AdvisorDTO) redisTemplate.opsForHash().get(tokenId,
 * advisorDTO.getId());
 * System.out.println("advisorDTO "+advisorDTO.getFirstName());
 * System.out.println("token"+tokenId); redisTemplate.expire(tokenId, 3600,
 * TimeUnit.MILLISECONDS); }
 */

