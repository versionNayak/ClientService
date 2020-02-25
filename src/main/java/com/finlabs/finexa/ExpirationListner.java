package com.finlabs.finexa;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import com.finlabs.finexa.dto.AdvisorDTO;
import com.finlabs.finexa.repository.AdvisorUserLoginInfoRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.service.AdvisorService;
import com.finlabs.finexa.service.AdvisorServiceImpl;


public class ExpirationListner implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(ExpirationListner.class);
    private  AdvisorService advisorService;  
    
	


	public ExpirationListner(AdvisorService advisorService) {
		this.advisorService = advisorService;
	}




	@Override
    public void onMessage(Message message, byte[] bytes) {
    	//System.out.println("onMessage");
        String key = "";
        key = new String(message.getBody());
        logger.debug("expired key: {}", key);
        //System.out.println("expired key: {}"+ key);
        String token = null;
        int index = key.indexOf("ey");
        //System.out.println("index "+index);
        if(index > 0) 
         token = key.substring(index);
        //AdvisorService advisorService = new AdvisorServiceImpl() ;
        advisorService.logoutFromRedis(token);
        
   

    }
    
    
}