package com.finlabs.finexa.service;

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
import com.finlabs.finexa.dto.ClientTaskDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientTask;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.ClientTaskRepository;
import com.finlabs.finexa.util.FinexaUtil;

@Service("ClientTaskService")
@Transactional
public class ClientTaskServiceIMPL implements ClientTaskService {

	private static Logger log = LoggerFactory.getLogger(ClientTaskService.class);

	@Autowired
	private Mapper mapper;
	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private ClientTaskRepository clientTaskRepository;
	
	@Autowired
	private AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository;

	@Override
	public ClientTaskDTO save(ClientTaskDTO ClientTaskDTO) {
		try {

			ClientTask clientTask = mapper.map(ClientTaskDTO, ClientTask.class);
			clientTask.setAdvisorUser(advisorUserRepository.findOne(ClientTaskDTO.getUserID()));
			if (ClientTaskDTO.getId() != 0) {
				ClientTask ct = clientTaskRepository.findOne(ClientTaskDTO.getId());
				clientTask.setClientType(ct.getClientType());
			}
			clientTask = clientTaskRepository.save(clientTask);

			return ClientTaskDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientTaskDTO findBytaskId(int taskId) throws RuntimeException {
		try {
			ClientTask clientTask = clientTaskRepository.findOne(taskId);

			ClientTaskDTO ClientTaskDTO = mapper.map(clientTask, ClientTaskDTO.class);
			return ClientTaskDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * @Override public List<ClientTaskDTO> findAllClientTaskByUserID(int userId)
	 * throws RuntimeException { // TODO Auto-generated method stub try {
	 * AdvisorUser user = advisorUserRepository.findOne(userId); List<ClientTaskDTO>
	 * listDTO = new ArrayList<ClientTaskDTO>(); List<ClientTask> list =
	 * user.getClientTasks(); if (list.size() > 0) { for (ClientTask clientTask :
	 * list) { ClientTaskDTO ClientTaskDTO = mapper.map(clientTask,
	 * ClientTaskDTO.class);
	 * 
	 * listDTO.add(ClientTaskDTO);
	 * 
	 * } } listDTO.sort(Comparator.comparing(ClientTaskDTO::getTaskDate)); return
	 * listDTO; } catch (RuntimeException e) { // TODO Auto-generated catch block
	 * throw new RuntimeException(); }
	 * 
	 * }
	 */

	@Override
	public List<ClientTaskDTO> findAllClientTaskByUserID(int userId, int value) throws RuntimeException {
		// TODO Auto-generated method stub
		List<ClientTaskDTO> listDTO = new ArrayList<ClientTaskDTO>();
		Date utilFromDate = null;
		Date utilTODate = null;
		List<ClientTask> clientTaskDTOList;
		List<ClientTask> clientTaskDTOListTotal;
		List<ClientTask> list = new ArrayList<ClientTask>();
		try {
			Calendar cal = null;
			cal = Calendar.getInstance();

			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);

			utilFromDate = cal.getTime();
			if (value == 1) {
				cal.add(Calendar.DATE, 7);
				utilTODate = cal.getTime();
			}

			if (value == 2) {
				cal.add(Calendar.DATE, 14);
				utilTODate = cal.getTime();
			}

			if (value == 3) {
				cal.add(Calendar.MONTH, 1);
				utilTODate = cal.getTime();
			}

			if (value == 4) {
				cal.add(Calendar.MONTH, 3);
				utilTODate = cal.getTime();
			}
			if (value == 5) {
				cal.add(Calendar.MONTH, 6);
				utilTODate = cal.getTime();
			}
			if (value == 6) {
				cal.add(Calendar.YEAR, 1);
				utilTODate = cal.getTime();
			}

			AdvisorUser user = advisorUserRepository.findOne(userId);
			// ==== getUser List =======
			FinexaUtil finexaUtil = new FinexaUtil();
			user = advisorUserRepository.findOne(userId);
			List<Integer> ids = finexaUtil.findAllUserHierarchy(user, advisorUserSupervisorMappingRepository);
			clientTaskDTOListTotal = new ArrayList<ClientTask>();
			for (Integer userid : ids) {
				user = advisorUserRepository.findOne(userid);
				clientTaskDTOList = user.getClientTask();
				clientTaskDTOListTotal.addAll(clientTaskDTOList);
			}
			// ===== end ========================
            //old code
			//List<ClientTask> clientTaskList = user.getClientTask();
			//System.out.println("value " + value);
			for (ClientTask clientTask : clientTaskDTOListTotal) {
			//for (ClientTask clientTask : clientTaskList) {
				Date taskDate = clientTask.getTaskDate();
				//System.out.println("util Date wwww" + utilTODate);

				if ((taskDate.after(utilFromDate)) && taskDate.before(utilTODate) || taskDate.equals(utilFromDate)
						|| taskDate.equals(utilTODate)) {

					list.add(clientTask);
				}
			}

			for (ClientTask clientTask : list) {
				ClientTaskDTO clientTaskDTO = mapper.map(clientTask, ClientTaskDTO.class);
				listDTO.add(clientTaskDTO);
			}
			Collections.sort(listDTO, new Comparator<ClientTaskDTO>() {
				public int compare(ClientTaskDTO o1, ClientTaskDTO o2) {
					return (o1.getTaskDate()).compareTo(o2.getTaskDate());
				}
			});
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

		return listDTO;
	}

	@Override
	public int delete(int taskId) throws RuntimeException {

		try {
			clientTaskRepository.delete(taskId);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

}
