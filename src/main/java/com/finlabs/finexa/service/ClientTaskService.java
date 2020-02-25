package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.ClientTaskDTO;


public interface ClientTaskService {
	public ClientTaskDTO save(ClientTaskDTO clientTaskDTO);

	int delete(int TaskId) throws RuntimeException;

	public List<ClientTaskDTO> findAllClientTaskByUserID(int userId, int value) throws RuntimeException;

	public ClientTaskDTO findBytaskId(int taskId) throws RuntimeException;

}
