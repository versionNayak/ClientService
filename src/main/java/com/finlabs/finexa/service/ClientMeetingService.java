package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.ClientInfoDTO;
import com.finlabs.finexa.dto.ClientMeetingDTO;


public interface ClientMeetingService {
	
	public ClientMeetingDTO save(ClientMeetingDTO clientMeetingDTO);
	
	public ClientMeetingDTO findByMeetingId(int meetingId) throws RuntimeException;

	int delete(int meetingId) throws RuntimeException;

	public List<ClientMeetingDTO> findAllClientMeetingByUserID(int userId, int value) throws RuntimeException;

}
