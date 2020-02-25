package com.finlabs.finexa.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientLifeInsuranceDTO;
import com.finlabs.finexa.dto.ClientMeetingDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientGoal;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientMeeting;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.ClientMeetingRepository;
import com.finlabs.finexa.util.FinexaUtil;

@Service("ClientMeetingService")
@Transactional
public class ClientMeetingServiceIMPL implements ClientMeetingService {
	private static Logger log = LoggerFactory.getLogger(ClientMeetingService.class);

	@Autowired
	private Mapper mapper;
	@Autowired
	private AdvisorUserRepository advisorUserRepository;
	@Autowired
	private ClientMeetingRepository clientMeetingRepository;
	@Autowired
	private AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository;


	@Override
	public ClientMeetingDTO save(ClientMeetingDTO clientMeetingDTO) {
		try {

			ClientMeeting clientMeeting = mapper.map(clientMeetingDTO, ClientMeeting.class);
			clientMeeting.setAdvisorUser(advisorUserRepository.findOne(clientMeetingDTO.getUserID()));
			if (clientMeetingDTO.getId() != 0) {
				ClientMeeting cm = clientMeetingRepository.findOne(clientMeetingDTO.getId());
				clientMeeting.setClientType(cm.getClientType());
			}
			clientMeeting = clientMeetingRepository.save(clientMeeting);
			System.out.println("clientMeeting " + clientMeeting);
			return clientMeetingDTO;
		} catch (RuntimeException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientMeetingDTO findByMeetingId(int meetingId) {
		ClientMeeting clientMeeting;
		ClientMeetingDTO clientMeetingDTO = null;
		try {
			clientMeeting = clientMeetingRepository.findOne(meetingId);
			clientMeetingDTO = mapper.map(clientMeeting, ClientMeetingDTO.class);

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return clientMeetingDTO;
	}

	/*
	 * @Override public List<ClientMeetingDTO> findAllClientMeetingByUserID(int
	 * userId) throws RuntimeException { // TODO Auto-generated method stub try {
	 * AdvisorUser user = advisorUserRepository.findOne(userId);
	 * List<ClientMeetingDTO> listDTO = new ArrayList<ClientMeetingDTO>();
	 * List<ClientMeeting> list = user.getClientMeetings(); if (list.size() > 0) {
	 * for (ClientMeeting clientMeeting : list) { ClientMeetingDTO clientMeetingDTO
	 * = mapper.map(clientMeeting, ClientMeetingDTO.class);
	 * 
	 * listDTO.add(clientMeetingDTO);
	 * 
	 * } } listDTO.sort(Comparator.comparing(ClientMeetingDTO::getMeetingDate));
	 * return listDTO; } catch (RuntimeException e) { // TODO Auto-generated catch
	 * block throw new RuntimeException(); }
	 * 
	 * }
	 */

	@SuppressWarnings("null")
	@Override
	public List<ClientMeetingDTO> findAllClientMeetingByUserID(int userId, int value) throws RuntimeException {
		// TODO Auto-generated method stub
		List<ClientMeetingDTO> listDTO = new ArrayList<ClientMeetingDTO>();
		Date utilTODate = null;
		Date utilFromDate = null;
		List<ClientMeeting> clientMeetingList = null;
		List<ClientMeeting> clientMeetingListTotal = null;
		List<ClientMeeting> list = new ArrayList<ClientMeeting>();
		AdvisorUser user = null;
		try {
			Calendar cal = null;
			cal = Calendar.getInstance();

			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);

			utilFromDate = cal.getTime();

			
			// ==== get client List =======
			FinexaUtil finexaUtil = new FinexaUtil();
			user = advisorUserRepository.findOne(userId);
			List<Integer> ids = finexaUtil.findAllUserHierarchy(user, advisorUserSupervisorMappingRepository);
		    clientMeetingListTotal = new ArrayList<ClientMeeting>();
			for (Integer userid : ids) {
				user = advisorUserRepository.findOne(userid);
				clientMeetingList = user.getClientMeetings();
				clientMeetingListTotal.addAll(clientMeetingList);
			}
			// ===== end ========================

			// clientMeetingList = user.getClientMeetings();
			System.out.println("value " + value);

			if (value == 1) {
				cal.add(Calendar.DATE, 7);
			}
			if (value == 2) {
				cal.add(Calendar.DATE, 14);
			}
			if (value == 3) {
				cal.add(Calendar.MONTH, 1);
			}
			if (value == 4) {
				cal.add(Calendar.MONTH, 3);
			}
			if (value == 5) {
				cal.add(Calendar.MONTH, 6);
			}
			if (value == 6) {
				cal.add(Calendar.YEAR, 1);
			}
			
			utilTODate = cal.getTime();
			//System.out.println("utilTODate " + utilTODate);
			for (ClientMeeting clientMeeting : clientMeetingListTotal) {
				// for(ClientMeeting clientMeeting:clientMeetingList) {
				Date meetingDate = clientMeeting.getMeetingDate();

				if ((meetingDate.after(utilFromDate)) && meetingDate.before(utilTODate)
						|| meetingDate.equals(utilFromDate) || meetingDate.equals(utilTODate)) {

					list.add(clientMeeting);
				}
			}
			for (ClientMeeting clientMeeting : list) {
				ClientMeetingDTO clientMeetingDTO = mapper.map(clientMeeting, ClientMeetingDTO.class);
				listDTO.add(clientMeetingDTO);
			}
			Collections.sort(listDTO, new Comparator<ClientMeetingDTO>() {
				public int compare(ClientMeetingDTO o1, ClientMeetingDTO o2) {
					return (o1.getMeetingDate()).compareTo(o2.getMeetingDate());
				}
			});
		} catch (RuntimeException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

		return listDTO;
	}

	@Override
	public int delete(int meetingId) throws RuntimeException {

		try {
			clientMeetingRepository.delete(meetingId);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

}
