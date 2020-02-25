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


import com.finlabs.finexa.dto.ClientStructuredProductDTO;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientStructuredProduct;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientStructuredProductRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;

@Service("ClientStructuredProductService")
@Transactional
public class ClientStructuredProductServiceImpl implements ClientStructuredProductService{

	private static Logger log = LoggerFactory.getLogger(ClientStructuredProductServiceImpl.class);
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private ClientStructuredProductRepository clientStructuredProductRepository;
	
	@Autowired
	private ClientMasterRepository clientMasterRepository;
	
	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;
	
	@Autowired
    private MasterProductClassificationRepository  masterProductClassificationRepository;

	@Autowired
	AdvisorService advisorService;

	@Override
	public ClientStructuredProductDTO save(ClientStructuredProductDTO clientStructuredProductDTO,  HttpServletRequest request) throws RuntimeException{
    try {
    	log.debug("in save");
		ClientMaster cm = clientMasterRepository.findOne(clientStructuredProductDTO.getClientID());
		ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientStructuredProductDTO.getFamilyMemberID());	
		ClientStructuredProduct clientStructuredProduct = mapper.map(clientStructuredProductDTO, ClientStructuredProduct.class);
		clientStructuredProduct.setClientMaster(cm);
		clientStructuredProduct.setClientFamilyMember(cfm);
			
		    MasterProductClassification masterProductClassification = masterProductClassificationRepository.findOne(clientStructuredProductDTO.getFinancialAssetType());
		    clientStructuredProduct.setMasterProductClassification(masterProductClassification);
		    
			clientStructuredProduct.setDescription(clientStructuredProductDTO.getSpDescription());
		    
		    clientStructuredProduct = clientStructuredProductRepository.save(clientStructuredProduct);
		    clientStructuredProductDTO = mapper.map(clientStructuredProduct, ClientStructuredProductDTO.class);
		    clientStructuredProductDTO.setClientID(clientStructuredProduct.getClientMaster().getId());
			clientStructuredProductDTO.setFinancialAssetType(clientStructuredProduct.getMasterProductClassification().getId());
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientStructuredProductDTO;
	} catch (RuntimeException e) {
		throw new RuntimeException(e);
	}
	}
	
	@Override
	public ClientStructuredProductDTO update(ClientStructuredProductDTO clientStructuredProductDTO, HttpServletRequest request) throws RuntimeException{
		 try {
			ClientMaster cm = clientMasterRepository.findOne(clientStructuredProductDTO.getClientID());
				ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientStructuredProductDTO.getFamilyMemberID());	
			    ClientStructuredProduct clientStructuredProduct = mapper.map(clientStructuredProductDTO, ClientStructuredProduct.class);
			    clientStructuredProduct.setClientMaster(cm);
				clientStructuredProduct.setClientFamilyMember(cfm);
					
				    MasterProductClassification masterProductClassification = masterProductClassificationRepository.findOne(clientStructuredProductDTO.getFinancialAssetType());
				    clientStructuredProduct.setMasterProductClassification(masterProductClassification);
				    
					clientStructuredProduct.setDescription(clientStructuredProductDTO.getSpDescription());
				    
				    clientStructuredProduct = clientStructuredProductRepository.save(clientStructuredProduct);
				    clientStructuredProductDTO = mapper.map(clientStructuredProduct, ClientStructuredProductDTO.class);
				    clientStructuredProductDTO.setClientID(clientStructuredProduct.getClientMaster().getId());
					clientStructuredProductDTO.setFinancialAssetType(clientStructuredProduct.getMasterProductClassification().getId());
					advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
					return clientStructuredProductDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}
	

	@Override
	public ClientStructuredProductDTO findById(int id) throws RuntimeException{
		try {
			ClientStructuredProductDTO clientStructuredProductDTO = mapper.map(clientStructuredProductRepository.findOne(id), ClientStructuredProductDTO.class);
			clientStructuredProductDTO.setClientID(clientStructuredProductRepository.findOne(id).getClientMaster().getId());
			clientStructuredProductDTO.setFamilyMemberID(clientStructuredProductRepository.findOne(id).getClientFamilyMember().getId());
			clientStructuredProductDTO.setSpDescription(clientStructuredProductRepository.findOne(id).getDescription());
			return clientStructuredProductDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<ClientStructuredProductDTO> findAll() {
		ClientStructuredProductDTO clientStructuredProductDTO;
		List<ClientStructuredProduct> listClientStructuredProduct = clientStructuredProductRepository.findAll();

		List<ClientStructuredProductDTO> listDTO = new ArrayList<ClientStructuredProductDTO>();
		for (ClientStructuredProduct clientStructuredProduct : listClientStructuredProduct) {
			clientStructuredProductDTO = mapper.map(clientStructuredProduct, ClientStructuredProductDTO.class);
			clientStructuredProductDTO.setClientID(clientStructuredProduct.getClientMaster().getId());
			listDTO.add(clientStructuredProductDTO);
		}

		return listDTO;
	}
	
	@Override
	public List<ClientStructuredProductDTO> findByClientId(int clientId) throws RuntimeException{
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			List<ClientStructuredProductDTO> listDTO = new ArrayList<ClientStructuredProductDTO>();
			for (ClientStructuredProduct clientStructuredProduct : cm.getClientStructuredProducts()) {
				ClientStructuredProductDTO dto= mapper.map(clientStructuredProduct, ClientStructuredProductDTO.class);
				dto.setClientID(clientId);
				dto.setOwnerName(clientStructuredProduct.getClientMaster().getFirstName() + " " + clientStructuredProduct.getClientMaster().getLastName());
				MasterProductClassification mpc = masterProductClassificationRepository.findOne(clientStructuredProduct.getMasterProductClassification().getId());
				dto.setFinancialAssetName(mpc.getProductName());
				dto.setSpDescription(clientStructuredProduct.getDescription());
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
		
			try {
				clientStructuredProductRepository.delete(id);
				return 1;
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		
	}
	
}
