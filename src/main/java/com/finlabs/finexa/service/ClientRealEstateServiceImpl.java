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

import com.finlabs.finexa.dto.ClientRealEstateDTO;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientRealEstate;
import com.finlabs.finexa.model.LookupAlternateInvestmentsAssetType;
import com.finlabs.finexa.model.LookupFrequency;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.AlternateInvestmentsAssetTypeRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientRealEstateRepository;
import com.finlabs.finexa.repository.FrequencyRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;



@Service("ClientRealEstateService")
@Transactional
public class ClientRealEstateServiceImpl implements ClientRealEstateService{

	private static Logger log = LoggerFactory.getLogger(ClientRealEstateServiceImpl.class);
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private ClientMasterRepository clientMasterRepository;
	
	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;
	
	@Autowired
	private ClientRealEstateRepository clientRealEsateRepository;
	
	@Autowired
	private AlternateInvestmentsAssetTypeRepository aiAssetTypeRepository;
	
	@Autowired
	private FrequencyRepository frequencyRepository;
	
	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;
	
	@Autowired
	AdvisorService advisorService;
	
	@Override
	public ClientRealEstateDTO save(ClientRealEstateDTO clientRealEstateDTO, HttpServletRequest request) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientRealEstateDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientRealEstateDTO.getFamilyMemberID());
			ClientRealEstate clientRealEstate = mapper.map(clientRealEstateDTO, ClientRealEstate.class);
			clientRealEstate.setClientMaster(cm);
			clientRealEstate.setClientFamilyMember(cfm);
			log.debug("ClientId: " + clientRealEstateDTO.getClientID());
			log.debug("Id: " + clientRealEstateDTO.getId());
			
			LookupAlternateInvestmentsAssetType aiAssetType = aiAssetTypeRepository.findOne(clientRealEstateDTO.getAssetTypeId());
			clientRealEstate.setLookupAlternateInvestmentsAssetType(aiAssetType);
			
			LookupFrequency frequency = frequencyRepository.findOne(clientRealEstateDTO.getRentalFrequency());
			clientRealEstate.setLookupFrequency(frequency);
			
			MasterProductClassification master = masterProductClassificationRepository.findOne(clientRealEstateDTO.getFinancialAssetType());
			clientRealEstate.setMasterProductClassification(master);
			
			
			log.debug("description: " + clientRealEstate.getDescription());

			clientRealEstate = clientRealEsateRepository.save(clientRealEstate);
			
			clientRealEstateDTO = mapper.map(clientRealEstate, ClientRealEstateDTO.class);
			clientRealEstateDTO.setClientID(clientRealEstate.getClientMaster().getId());
			clientRealEstateDTO.setAssetTypeId(clientRealEstate.getLookupAlternateInvestmentsAssetType().getId());
			clientRealEstateDTO.setFinancialAssetType(clientRealEstate.getMasterProductClassification().getId());
			//	clientRealEstateDTO.setRentalFrequency(clientRealEstate.getLookupFrequency().getId());
			log.debug("Id "+clientRealEstateDTO.getId()+" clientId"+clientRealEstateDTO.getClientID());
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientRealEstateDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientRealEstateDTO> findByClientId(int clientId) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			List<ClientRealEstateDTO> listDTO = new ArrayList<ClientRealEstateDTO>();
			for (ClientRealEstate clientRealEstate : cm.getClientRealEstates()) {
				ClientRealEstateDTO dto = mapper.map(clientRealEstate, ClientRealEstateDTO.class);
				dto.setClientID(clientId);
				dto.setOwnerName(clientRealEstate.getClientMaster().getFirstName() + " " + clientRealEstate.getClientMaster().getLastName());
				MasterProductClassification mpc = masterProductClassificationRepository.findOne(clientRealEstate.getMasterProductClassification().getId());
				dto.setFinancialAssetName(mpc.getProductName());
				dto.setFinancialAssetType(mpc.getId());
				log.debug("description in findbyclientId: " + clientRealEstate.getDescription());
				dto.setDescription(clientRealEstate.getDescription());
				LookupAlternateInvestmentsAssetType aiAssetType = aiAssetTypeRepository.findOne(clientRealEstate.getLookupAlternateInvestmentsAssetType().getId());
				dto.setAssetTypeName(aiAssetType.getDescription());
				listDTO.add(dto);
			}
			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException{
		// TODO Auto-generated method stub
		
			try {
				clientRealEsateRepository.delete(id);
				return 1;
			} catch (RuntimeException e) {
				throw new RuntimeException(e);
			}
		
	}

	@Override
	public ClientRealEstateDTO findById(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientRealEstate clientRealEstate = clientRealEsateRepository.findOne(id);
			ClientRealEstateDTO clientRealEstateDTO = mapper.map(clientRealEsateRepository.findOne(id), ClientRealEstateDTO.class);
			clientRealEstateDTO.setClientID(clientRealEsateRepository.findOne(id).getClientMaster().getId());
			clientRealEstateDTO.setFamilyMemberID(clientRealEstate.getClientFamilyMember().getId());
			clientRealEstateDTO.setFinancialAssetType(clientRealEstate.getMasterProductClassification().getId());
			clientRealEstateDTO.setAssetTypeId(clientRealEstate.getLookupAlternateInvestmentsAssetType().getId());
			clientRealEstateDTO.setDescription(clientRealEstate.getDescription());
			if(clientRealEstateDTO.getAssetTypeId() == 1) {
				if (clientRealEstate.getLookupFrequency() != null) {
					clientRealEstateDTO.setRentalFrequency(clientRealEstate.getLookupFrequency().getId());
				}
			}
			return clientRealEstateDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientRealEstateDTO update(ClientRealEstateDTO clientRealEstateDTO, HttpServletRequest request) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientRealEstateDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientRealEstateDTO.getFamilyMemberID());
			ClientRealEstate clientRealEstate = mapper.map(clientRealEstateDTO, ClientRealEstate.class);
			clientRealEstate.setClientMaster(cm);
			clientRealEstate.setClientFamilyMember(cfm);
			log.debug("ClientId: " + clientRealEstateDTO.getClientID());
			log.debug("Id: " + clientRealEstateDTO.getId());
			
			LookupAlternateInvestmentsAssetType aiAssetType = aiAssetTypeRepository.findOne(clientRealEstateDTO.getAssetTypeId());
			clientRealEstate.setLookupAlternateInvestmentsAssetType(aiAssetType);
			
			LookupFrequency frequency = frequencyRepository.findOne(clientRealEstateDTO.getRentalFrequency());
			clientRealEstate.setLookupFrequency(frequency);
			
			MasterProductClassification master = masterProductClassificationRepository.findOne(clientRealEstateDTO.getFinancialAssetType());
			clientRealEstate.setMasterProductClassification(master);
			
			
			log.debug("description: " + clientRealEstate.getDescription());

			clientRealEstate = clientRealEsateRepository.save(clientRealEstate);
			
			clientRealEstateDTO = mapper.map(clientRealEstate, ClientRealEstateDTO.class);
			clientRealEstateDTO.setClientID(clientRealEstate.getClientMaster().getId());
			clientRealEstateDTO.setAssetTypeId(clientRealEstate.getLookupAlternateInvestmentsAssetType().getId());
			clientRealEstateDTO.setFinancialAssetType(clientRealEstate.getMasterProductClassification().getId());
			//	clientRealEstateDTO.setRentalFrequency(clientRealEstate.getLookupFrequency().getId());
			log.debug("Id "+clientRealEstateDTO.getId()+" clientId"+clientRealEstateDTO.getClientID());
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientRealEstateDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public List<ClientRealEstateDTO> findAll() {
		// TODO Auto-generated method stub
		ClientRealEstateDTO ClientRealEstateDTO;
		List<ClientRealEstate> listRealEstate = clientRealEsateRepository.findAll();
		
		List<ClientRealEstateDTO> listDTO = new ArrayList<ClientRealEstateDTO>();
		for (ClientRealEstate clientRealEstate : listRealEstate) {
			ClientRealEstateDTO = mapper.map(clientRealEstate, ClientRealEstateDTO.class);
			ClientRealEstateDTO.setClientID(clientRealEstate.getClientMaster().getId());
			listDTO.add(ClientRealEstateDTO);
		}
		return listDTO;
	}
	
	@Override
	public void autoSave(ClientRealEstateDTO clientRealEstateDTO) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientRealEstateDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientRealEstateDTO.getFamilyMemberID());
			ClientRealEstate clientRealEstate = mapper.map(clientRealEstateDTO, ClientRealEstate.class);
			clientRealEstate.setClientMaster(cm);
			clientRealEstate.setClientFamilyMember(cfm);
			log.debug("ClientId: " + clientRealEstateDTO.getClientID());
			log.debug("Id: " + clientRealEstateDTO.getId());
			
			LookupAlternateInvestmentsAssetType aiAssetType = aiAssetTypeRepository.findOne(clientRealEstateDTO.getAssetTypeId());
			clientRealEstate.setLookupAlternateInvestmentsAssetType(aiAssetType);
			
			LookupFrequency frequency = frequencyRepository.findOne(clientRealEstateDTO.getRentalFrequency());
			clientRealEstate.setLookupFrequency(frequency);
			
			MasterProductClassification master = masterProductClassificationRepository.findOne(clientRealEstateDTO.getFinancialAssetType());
			clientRealEstate.setMasterProductClassification(master);
			
			
			log.debug("description: " + clientRealEstate.getDescription());

			clientRealEstate = clientRealEsateRepository.save(clientRealEstate);
			
			
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

}
