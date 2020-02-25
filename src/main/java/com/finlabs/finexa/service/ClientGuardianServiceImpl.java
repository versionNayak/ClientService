package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientGuardianDTO;
import com.finlabs.finexa.model.ClientGuardian;
import com.finlabs.finexa.model.ClientGuardianContact;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.repository.ClientGuardianContactRepository;
import com.finlabs.finexa.repository.ClientGuardianRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;

@Service("ClientGuardianService")
@Transactional
public class ClientGuardianServiceImpl implements ClientGuardianService {
	private static Logger log = LoggerFactory.getLogger(ClientGuardianServiceImpl.class);
	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientGuardianRepository clientGuardianRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private ClientGuardianContactRepository clientGuardianContactRepository;

	@Override
	public ClientGuardianDTO save(ClientGuardianDTO clientGuardianDTO) throws RuntimeException {
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientGuardianDTO.getClientID());
			ClientGuardian clientGuardian = mapper.map(clientGuardianDTO, ClientGuardian.class);
			clientGuardian.setClientMaster(cm);

			ClientGuardian model = clientGuardianRepository.save(clientGuardian);

			ClientGuardianDTO dto = mapper.map(model, ClientGuardianDTO.class);
			dto.setClientID(model.getClientMaster().getId());

			return dto;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientGuardianDTO update(ClientGuardianDTO clientGuardianDTO) {
		ClientMaster cm = clientMasterRepository.findOne(clientGuardianDTO.getClientID());
		ClientGuardian clientGuardian = mapper.map(clientGuardianDTO, ClientGuardian.class);
		clientGuardian.setClientMaster(cm);

		ClientGuardian model = clientGuardianRepository.save(clientGuardian);
		ClientGuardianDTO dto = mapper.map(model, ClientGuardianDTO.class);

		dto.setClientID(model.getClientMaster().getId());

		return clientGuardianDTO;
	}

	@Override
	public ClientGuardianDTO findByClientId(int clientId) throws RuntimeException {
		try {
			ClientGuardianDTO clientGuardianDTO = null;
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			if (clientMaster.getClientGuardians().size() != 0) {
				ClientGuardian clientGuardian = clientMaster.getClientGuardians().get(0);
				log.debug("id " + clientGuardian.getId());
				clientGuardianDTO = mapper.map(clientGuardian, ClientGuardianDTO.class);
				log.debug("id " + clientGuardianDTO.getId());
				log.debug("id " + clientGuardianDTO.getBirthDate());
				clientGuardianDTO.setClientID(clientId);
			}
			return clientGuardianDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) {
		if (id != 0) {
			clientGuardianRepository.delete(id);
			return 1;
		} else {
			return 0;
		}

	}

	@Override
	public int deleteGuardianandGuardianContact(int id) throws RuntimeException{

		try {
			// log.debug("id "+id);

			ClientGuardian clientGuardian = clientGuardianRepository.getOne(id);
			// log.debug("clientGuardian "+clientGuardian);
			// log.debug("clientGuardianContact
			// "+clientGuardian.getClientGuardianContacts().size());
			// log.debug("after delete ");
			for (ClientGuardianContact clientGuardianContact : clientGuardian.getClientGuardianContacts()) {

				clientGuardianContactRepository.delete(clientGuardianContact.getId());
				// log.debug("clientGuardianContact
				// "+clientGuardian.getClientGuardianContacts().size());
				clientGuardianRepository.delete(id);
				// log.debug("clientGuardian "+clientGuardian.getId());

			}

			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}

	}

	@Override
	public ClientGuardianDTO findById(int id) {
		ClientGuardianDTO ClientGuardianDTO = mapper.map(clientGuardianRepository.findOne(id), ClientGuardianDTO.class);
		ClientGuardianDTO.setClientID(clientGuardianRepository.findOne(id).getClientMaster().getId());
		return ClientGuardianDTO;
	}

	@Override
	public List<ClientGuardianDTO> findAll() {
		ClientGuardianDTO ClientGuardianDTO;
		List<ClientGuardian> listClientGuardian = clientGuardianRepository.findAll();

		List<ClientGuardianDTO> listDTO = new ArrayList<ClientGuardianDTO>();
		for (ClientGuardian ClientGuardian : listClientGuardian) {
			ClientGuardianDTO = mapper.map(ClientGuardian, ClientGuardianDTO.class);
			ClientGuardianDTO.setClientID(ClientGuardian.getClientMaster().getId());
			listDTO.add(ClientGuardianDTO);
		}

		return listDTO;
	}
}
