package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientLumpsumDTO;
import com.finlabs.finexa.model.ClientLumpsumInflow;
import com.finlabs.finexa.model.ClientMaster;

import com.finlabs.finexa.repository.ClientLumpsumRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;

@Service("ClientLumpsumService")
@Transactional
public class ClientLumpsumServiceImpl implements ClientLumpsumService {

	private static Logger log = LoggerFactory.getLogger(ClientLoanServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientLumpsumRepository clientLumpsumRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;
	
	@Autowired
	AdvisorService advisorService;

	@Override
	public ClientLumpsumDTO save(ClientLumpsumDTO clientLumpsumDTO, HttpServletRequest request) throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientLumpsumDTO.getClientId());
			ClientLumpsumInflow clientLumpsumInflow = mapper.map(clientLumpsumDTO, ClientLumpsumInflow.class);
			clientLumpsumInflow.setClientMaster(cm);
			clientLumpsumInflow = clientLumpsumRepository.save(clientLumpsumInflow);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientLumpsumDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientLumpsumDTO update(ClientLumpsumDTO clientLumpsumDTO, HttpServletRequest request) throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientLumpsumDTO.getClientId());
			ClientLumpsumInflow clientLumpsumInflow = mapper.map(clientLumpsumDTO, ClientLumpsumInflow.class);
			clientLumpsumInflow.setClientMaster(cm);
			clientLumpsumInflow = clientLumpsumRepository.save(clientLumpsumInflow);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientLumpsumDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientLumpsumDTO findById(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientLumpsumDTO clientLumpsumDTO = mapper.map(clientLumpsumRepository.findOne(id), ClientLumpsumDTO.class);
			clientLumpsumDTO.setClientId(clientLumpsumRepository.findOne(id).getClientMaster().getId());
			return clientLumpsumDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientLumpsumDTO> findAll() {
		// TODO Auto-generated method stub
		ClientLumpsumDTO ClientLumpsumDTO;
		List<ClientLumpsumInflow> listClientLumpsum = clientLumpsumRepository.findAll();

		List<ClientLumpsumDTO> listDTO = new ArrayList<ClientLumpsumDTO>();
		for (ClientLumpsumInflow clientLumpsumInflow : listClientLumpsum) {
			ClientLumpsumDTO = mapper.map(clientLumpsumInflow, ClientLumpsumDTO.class);
			ClientLumpsumDTO.setClientId(clientLumpsumInflow.getClientMaster().getId());
			listDTO.add(ClientLumpsumDTO);
		}

		return listDTO;
	}

	@Override
	public List<ClientLumpsumDTO> findByClientId(int clientId) throws RuntimeException {

		List<ClientLumpsumDTO> listDTO = null;
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			listDTO = new ArrayList<ClientLumpsumDTO>();
			for (ClientLumpsumInflow clientLumpsumInflow : cm.getClientLumpsumInflows()) {
				ClientLumpsumDTO dto = mapper.map(clientLumpsumInflow, ClientLumpsumDTO.class);
				dto.setClientId(clientId);
				dto.setOwnerName(clientLumpsumInflow.getClientMaster().getFirstName() + " "
						+ (clientLumpsumInflow.getClientMaster().getMiddleName() == null ? " "
								: clientLumpsumInflow.getClientMaster().getMiddleName())
						+ " " + clientLumpsumInflow.getClientMaster().getLastName());
				listDTO.add(dto);
				log.debug("date: " + dto.getExpectedInflowDate());
			}
			return listDTO;
		} catch (RuntimeException runtimeExp) {
			throw new RuntimeException(runtimeExp);
		}

	}

	@Override
	public int delete(int id) throws RuntimeException {

		try {
			clientLumpsumRepository.delete(id);
			return 1;
		} catch (RuntimeException runtimeExp) {
			throw new RuntimeException(runtimeExp);
		}

	}
	
	@Override
	public void autoSave(ClientLumpsumDTO clientLumpsumDTO) throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientLumpsumDTO.getClientId());
			ClientLumpsumInflow clientLumpsumInflow = mapper.map(clientLumpsumDTO, ClientLumpsumInflow.class);
			clientLumpsumInflow.setClientMaster(cm);
			clientLumpsumInflow = clientLumpsumRepository.save(clientLumpsumInflow);
			//return clientLumpsumDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}
