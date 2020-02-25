package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientGuardianContactDTO;
import com.finlabs.finexa.model.ClientGuardian;
import com.finlabs.finexa.model.ClientGuardianContact;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterPincode;
import com.finlabs.finexa.model.MasterState;
import com.finlabs.finexa.repository.ClientGuardianContactRepository;
import com.finlabs.finexa.repository.ClientGuardianRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.LookupCountryRepository;
import com.finlabs.finexa.repository.MasterPincodeRepository;
import com.finlabs.finexa.repository.MasterStateRepository;

@Service("ClientGuardianContactService")
@Transactional
public class ClientGuardianContactServiceImpl implements ClientGuardianContactService {

	private static Logger log = LoggerFactory.getLogger(ClientGuardianContactServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientGuardianContactRepository clientGuardianContactRepository;

	@Autowired
	private ClientGuardianRepository clientGuardianRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private LookupCountryRepository lookupCountryRepository;

	@Autowired
	private MasterStateRepository masterStateRepository;

	@Autowired
	private MasterPincodeRepository masterPincodeRepository;

	@Override
	public ClientGuardianContactDTO save(ClientGuardianContactDTO clientGuardianContactDTO) throws RuntimeException{
		  try {
			  log.debug("in save");
			  ClientGuardian clientGuardian = clientGuardianRepository.findOne(clientGuardianContactDTO.getGuardianID());
			  ClientGuardianContact ClientGuardianContact = mapper.map(clientGuardianContactDTO, ClientGuardianContact.class);


		  ClientGuardianContact.setClientGuardian(clientGuardian);
		  ClientGuardianContact.setLookupCountry1(lookupCountryRepository.findOne(clientGuardianContactDTO.getLookupOfficeCountryId()));
		  if(clientGuardianContactDTO.getAddress1DropId()!=0){
				MasterState state = masterStateRepository.findOne(clientGuardianContactDTO.getAddress1DropId());
				ClientGuardianContact.setOfficeState(state.getState());
				}else{
					ClientGuardianContact.setOfficeState(clientGuardianContactDTO.getOfficeState());	
				}
		  
		  ClientGuardianContact.setLookupCountry2(lookupCountryRepository.findOne(clientGuardianContactDTO.getLookupPermanentCountryId()));
		  
			if(clientGuardianContactDTO.getAddress2DropId()!=0){
				MasterState state1 = masterStateRepository.findOne(clientGuardianContactDTO.getAddress2DropId());
				ClientGuardianContact.setPermanentState(state1.getState());
				}else{
					ClientGuardianContact.setPermanentState(clientGuardianContactDTO.getPermanentState());	
				}
			
		  ClientGuardianContact.setLookupCountry3(lookupCountryRepository.findOne(clientGuardianContactDTO.getLookupCorrespondenceCountryId()));
		  if(clientGuardianContactDTO.getAddress3DropId()!=0){
				MasterState state2 = masterStateRepository.findOne(clientGuardianContactDTO.getAddress3DropId());
				ClientGuardianContact.setCorrespondenceState(state2.getState());
				}else{
					ClientGuardianContact.setCorrespondenceState(clientGuardianContactDTO.getCorrespondenceState());	
				}
		  
	   	  ClientGuardianContact clientGuardianContact=clientGuardianContactRepository.save(ClientGuardianContact);
		  
		  ClientGuardianContactDTO retDTO= mapper.map(clientGuardianContact, ClientGuardianContactDTO.class);
		  retDTO.setGuardianID(clientGuardianContact.getClientGuardian().getId());
		  
		  return retDTO;

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public ClientGuardianContactDTO update(ClientGuardianContactDTO clientGuardianContactDTO) {
		ClientGuardian clientGuardian = clientGuardianRepository.findOne(clientGuardianContactDTO.getGuardianID());

		ClientGuardianContact clientGuardianContact = mapper.map(clientGuardianContactDTO, ClientGuardianContact.class);

		clientGuardianContact.setClientGuardian(clientGuardian);

		ClientGuardianContact retModel = clientGuardianContactRepository.save(clientGuardianContact);

		ClientGuardianContactDTO retDTO = mapper.map(retModel, ClientGuardianContactDTO.class);
		retDTO.setGuardianID(retModel.getClientGuardian().getId());

		return retDTO;
	}

	@Override
	public ClientGuardianContactDTO findByClientID(int clientId) throws RuntimeException{
		
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			ClientGuardian clientGuardian = clientMaster.getClientGuardians().get(0);
			
			//List<ClientGuardianContactDTO> listDTO=new ArrayList<ClientGuardianContactDTO>();
			
			/*for(ClientGuardianContact ClientGuardianContact:clientGuardian.getClientGuardianContacts()){
				ClientGuardianContactDTO retDTO =mapper.map(ClientGuardianContact, ClientGuardianContactDTO.class);
				retDTO.setGuardianID(ClientGuardianContact.getClientGuardian().getId());
				listDTO.add(retDTO);
			}*/
			ClientGuardianContact clientGuardianContact = clientGuardian.getClientGuardianContacts().get(0);
			
			ClientGuardianContactDTO retDTO =mapper.map(clientGuardianContact, ClientGuardianContactDTO.class);
			int oCountryId = (clientGuardianContact.getLookupCountry1() != null) ? clientGuardianContact.getLookupCountry1().getId() : 0;
			int pCountryId = (clientGuardianContact.getLookupCountry2() != null)?clientGuardianContact.getLookupCountry2().getId():0;
			int cCountryId = (clientGuardianContact.getLookupCountry3() !=null) ?clientGuardianContact.getLookupCountry3().getId() :0;
			
			//log.debug("look 1 "+oCountryId);
			//log.debug("look 2 "+pCountryId);
			//log.debug("look 3 "+cCountryId);
			
			if(oCountryId==99){
				MasterState state = masterStateRepository.findByState(clientGuardianContact.getOfficeState());
				retDTO.setAddress1DropId(state.getId());
			}
			if(pCountryId==99){
				MasterState state1 = masterStateRepository.findByState(clientGuardianContact.getPermanentState());
			    if( state1 != null && state1.getId() > 0){
			    	retDTO.setAddress2DropId(state1.getId());
			    }
			}
		        if(cCountryId==99){
				MasterState state2 = masterStateRepository.findByState(clientGuardianContact.getCorrespondenceState());
				retDTO.setAddress3DropId(state2.getId());
			}
			retDTO.setLookupOfficeCountryId(oCountryId);
			retDTO.setLookupPermanentCountryId(pCountryId);
			retDTO.setLookupCorrespondenceCountryId(cCountryId);
			
			retDTO.setGuardianID(clientGuardianContact.getClientGuardian().getId());
			
			return retDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public int delete(int id) {
		if (id != 0) {
			clientGuardianContactRepository.delete(id);
			return 1;
		} else {
			return 0;
		}

	}

	@Override
	public ClientGuardianContactDTO findById(int id) {
		ClientGuardianContactDTO ClientGuardianContactDTO = mapper.map(clientGuardianContactRepository.findOne(id),
				ClientGuardianContactDTO.class);
		ClientGuardianContactDTO.setGuardianID(clientGuardianContactRepository.findOne(id).getClientGuardian().getId());
		MasterState state = masterStateRepository
				.findByState(clientGuardianContactRepository.findOne(id).getOfficeState());
		ClientGuardianContactDTO.setAddress1DropId(state.getId());

		MasterState state1 = masterStateRepository
				.findByState(clientGuardianContactRepository.findOne(id).getPermanentState());
		ClientGuardianContactDTO.setAddress2DropId(state1.getId());

		MasterState state2 = masterStateRepository
				.findByState(clientGuardianContactRepository.findOne(id).getCorrespondenceState());
		ClientGuardianContactDTO.setAddress3DropId(state2.getId());
		return ClientGuardianContactDTO;
	}

	@Override
	public List<ClientGuardianContactDTO> findAll() {
		ClientGuardianContactDTO ClientGuardianContactDTO;
		List<ClientGuardianContact> listClientGuardianContact = clientGuardianContactRepository.findAll();

		List<ClientGuardianContactDTO> listDTO = new ArrayList<ClientGuardianContactDTO>();
		for (ClientGuardianContact clientGuardianContact : listClientGuardianContact) {
			ClientGuardianContactDTO = mapper.map(clientGuardianContact, ClientGuardianContactDTO.class);
			int oCountryId = (clientGuardianContact.getLookupCountry1() != null)
					? clientGuardianContact.getLookupCountry1().getId() : 0;
			int pCountryId = (clientGuardianContact.getLookupCountry2() != null)
					? clientGuardianContact.getLookupCountry2().getId() : 0;
			int cCountryId = (clientGuardianContact.getLookupCountry3() != null)
					? clientGuardianContact.getLookupCountry3().getId() : 0;
			ClientGuardianContactDTO.setLookupOfficeCountryId(oCountryId);
			ClientGuardianContactDTO.setLookupPermanentCountryId(pCountryId);
			ClientGuardianContactDTO.setLookupCorrespondenceCountryId(cCountryId);
			ClientGuardianContactDTO.setGuardianID(clientGuardianContact.getClientGuardian().getId());
			listDTO.add(ClientGuardianContactDTO);
		}

		return listDTO;
	}

	@Override
	public boolean checkUniquePincode(int pincode, int stateId) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			int cc = 0;
			MasterState masterState = masterStateRepository.findOne(stateId);

			for (MasterPincode masterPincode : masterState.getMasterPincodes()) {
				if (masterPincode.getPincode() == pincode) {
					cc = 1;
					break;
				} else {
					cc = 0;
				}
			}

			log.debug("cc " + cc);
			return (cc != 0) ? false : true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
}
