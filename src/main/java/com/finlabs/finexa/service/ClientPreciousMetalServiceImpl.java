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

import com.finlabs.finexa.dto.ClientPreciousMetalDTO;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientPreciousMetal;
import com.finlabs.finexa.model.LookupAlternateInvestmentsAssetType;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.AlternateInvestmentsAssetTypeRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientPreciousMetalRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;

@Service("ClientPreciousMetalService")
@Transactional
public class ClientPreciousMetalServiceImpl implements ClientPreciousMetalService{

	private static Logger log = LoggerFactory.getLogger(ClientPreciousMetalServiceImpl.class);
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private ClientMasterRepository clientMasterRepository;
	
	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;
	
	@Autowired
	private ClientPreciousMetalRepository clientPreciousMetalRepository;
	
	@Autowired
	private AlternateInvestmentsAssetTypeRepository aiAssetTypeRepository;
	
    @Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;
	
    @Autowired
	AdvisorService advisorService;
    
	@Override
	public ClientPreciousMetalDTO save(ClientPreciousMetalDTO clientPreciousMetalDTO, HttpServletRequest request) throws RuntimeException{
		// TODO Auto-generated method stub
		
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientPreciousMetalDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientPreciousMetalDTO.getFamilyMemberID());
			ClientPreciousMetal clientPreciousMetal = mapper.map(clientPreciousMetalDTO, ClientPreciousMetal.class);
			clientPreciousMetal.setClientMaster(cm);
			clientPreciousMetal.setClientFamilyMember(cfm);
			log.debug("ClientId: " + clientPreciousMetalDTO.getClientID());
			log.debug("Id: " + clientPreciousMetalDTO.getId());
			
			LookupAlternateInvestmentsAssetType aiAssetType = aiAssetTypeRepository.findOne(clientPreciousMetalDTO.getAssetTypeId());
			clientPreciousMetal.setLookupAlternateInvestmentsAssetType(aiAssetType);
			
			MasterProductClassification master = masterProductClassificationRepository.findOne(clientPreciousMetalDTO.getFinancialAssetType());
			clientPreciousMetal.setMasterProductClassification(master);
			
			clientPreciousMetal.setDescription(clientPreciousMetalDTO.getPmDescription());
			
			clientPreciousMetal = clientPreciousMetalRepository.save(clientPreciousMetal);
			
			clientPreciousMetalDTO = mapper.map(clientPreciousMetal, ClientPreciousMetalDTO.class);
			clientPreciousMetalDTO.setClientID(clientPreciousMetal.getClientMaster().getId());
			clientPreciousMetalDTO.setAssetTypeId(clientPreciousMetal.getLookupAlternateInvestmentsAssetType().getId());
			clientPreciousMetalDTO.setFinancialAssetType(clientPreciousMetal.getMasterProductClassification().getId());
			log.debug("Id "+clientPreciousMetalDTO.getId()+" clientId"+clientPreciousMetalDTO.getClientID());
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientPreciousMetalDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientPreciousMetalDTO update(ClientPreciousMetalDTO clientPreciousMetalDTO, HttpServletRequest request) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientPreciousMetalDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientPreciousMetalDTO.getFamilyMemberID());
			ClientPreciousMetal clientPreciousMetal = mapper.map(clientPreciousMetalDTO, ClientPreciousMetal.class);
			clientPreciousMetal.setClientMaster(cm);
			clientPreciousMetal.setClientFamilyMember(cfm);
			log.debug("ClientId: " + clientPreciousMetalDTO.getClientID());
			log.debug("Id: " + clientPreciousMetalDTO.getId());
			
			LookupAlternateInvestmentsAssetType aiAssetType = aiAssetTypeRepository.findOne(clientPreciousMetalDTO.getAssetTypeId());
			clientPreciousMetal.setLookupAlternateInvestmentsAssetType(aiAssetType);
			
			MasterProductClassification master = masterProductClassificationRepository.findOne(clientPreciousMetalDTO.getFinancialAssetType());
			clientPreciousMetal.setMasterProductClassification(master);
			
			clientPreciousMetal.setDescription(clientPreciousMetalDTO.getPmDescription());
			
			clientPreciousMetal = clientPreciousMetalRepository.save(clientPreciousMetal);
			
			clientPreciousMetalDTO = mapper.map(clientPreciousMetal, ClientPreciousMetalDTO.class);
			clientPreciousMetalDTO.setClientID(clientPreciousMetal.getClientMaster().getId());
			clientPreciousMetalDTO.setAssetTypeId(clientPreciousMetal.getLookupAlternateInvestmentsAssetType().getId());
			clientPreciousMetalDTO.setFinancialAssetType(clientPreciousMetal.getMasterProductClassification().getId());
			log.debug("Id "+clientPreciousMetalDTO.getId()+" clientId"+clientPreciousMetalDTO.getClientID());
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientPreciousMetalDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public List<ClientPreciousMetalDTO> findByClientId(int clientId) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			List<ClientPreciousMetalDTO> listDTO = new ArrayList<ClientPreciousMetalDTO>();
			for (ClientPreciousMetal clientPreciousMetal : cm.getClientPreciousMetals()){
				ClientPreciousMetalDTO dto = mapper.map(clientPreciousMetal, ClientPreciousMetalDTO.class);
				dto.setClientID(clientId);
				dto.setOwnerName(clientPreciousMetal.getClientMaster().getFirstName() + " " + clientPreciousMetal.getClientMaster().getLastName());
				MasterProductClassification mpc = masterProductClassificationRepository.findOne(clientPreciousMetal.getMasterProductClassification().getId());
				dto.setFinancialAssetName(mpc.getProductName());
				dto.setFinancialAssetType(mpc.getId());
				dto.setPmDescription(clientPreciousMetal.getDescription());
				LookupAlternateInvestmentsAssetType assetType = aiAssetTypeRepository.findOne(clientPreciousMetal.getLookupAlternateInvestmentsAssetType().getId());
				dto.setAssetTypeName(assetType.getDescription());
				listDTO.add(dto);
			}
			return listDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException{
		// TODO Auto-generated method stub
			try {
				clientPreciousMetalRepository.delete(id);
				return 1;
			} catch (RuntimeException e) {
				throw new RuntimeException(e);
			}
		
	}

	@Override
	public ClientPreciousMetalDTO findById(int id) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			ClientPreciousMetalDTO clientPreciousMetalDTO = mapper.map(clientPreciousMetalRepository.findOne(id), ClientPreciousMetalDTO.class);
			clientPreciousMetalDTO.setClientID(clientPreciousMetalRepository.findOne(id).getClientMaster().getId());
			clientPreciousMetalDTO.setFamilyMemberID(clientPreciousMetalRepository.findOne(id).getClientFamilyMember().getId());
			clientPreciousMetalDTO.setFinancialAssetType(clientPreciousMetalRepository.findOne(id).getMasterProductClassification().getId());
			clientPreciousMetalDTO.setAssetTypeId(clientPreciousMetalRepository.findOne(id).getLookupAlternateInvestmentsAssetType().getId());
			clientPreciousMetalDTO.setPmDescription(clientPreciousMetalRepository.findOne(id).getDescription());
			return clientPreciousMetalDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientPreciousMetalDTO> findAll() {
		// TODO Auto-generated method stub
		ClientPreciousMetalDTO ClientPreciousMetalDTO;
		List<ClientPreciousMetal> listPreciousMetal = clientPreciousMetalRepository.findAll();
		
		List<ClientPreciousMetalDTO> listDTO = new ArrayList<ClientPreciousMetalDTO>();
		for (ClientPreciousMetal clientPreciousMetal : listPreciousMetal) {
			ClientPreciousMetalDTO = mapper.map(clientPreciousMetal, ClientPreciousMetalDTO.class);
			ClientPreciousMetalDTO.setClientID(clientPreciousMetal.getClientMaster().getId());
			listDTO.add(ClientPreciousMetalDTO);
		}
		return listDTO;
	}

}
