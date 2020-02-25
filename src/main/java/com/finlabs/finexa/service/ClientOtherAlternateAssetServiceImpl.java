package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientOtherAlternateAssetDTO;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientOtherAlternateAsset;
import com.finlabs.finexa.model.LookupAlternateInvestmentsAssetType;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.AlternateInvestmentsAssetTypeRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientOtherAlternateAssetRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;

@Service("ClientOtherAlternateAssetService")
@Transactional
public class ClientOtherAlternateAssetServiceImpl implements ClientOtherAlternateAssetService {

	private static Logger log = LoggerFactory.getLogger(ClientOtherAlternateAssetServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private ClientOtherAlternateAssetRepository clientOtherAlternateAssetRepository;

	@Autowired
	private AlternateInvestmentsAssetTypeRepository aiAssetTypeRepository;

	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;

	@Override
	public ClientOtherAlternateAssetDTO save(ClientOtherAlternateAssetDTO clientOtherAlternateAssetDTO)
			throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientOtherAlternateAssetDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository
					.findOne(clientOtherAlternateAssetDTO.getFamilyMemberID());
			ClientOtherAlternateAsset clientOtherAlternateAsset = mapper.map(clientOtherAlternateAssetDTO,
					ClientOtherAlternateAsset.class);
			clientOtherAlternateAsset.setClientMaster(cm);
			clientOtherAlternateAsset.setClientFamilyMember(cfm);
			log.debug("ClientId: " + clientOtherAlternateAssetDTO.getClientID());
			log.debug("Id: " + clientOtherAlternateAssetDTO.getId());

			LookupAlternateInvestmentsAssetType aiAssetType = aiAssetTypeRepository
					.findOne(clientOtherAlternateAssetDTO.getAssetTypeId());
			clientOtherAlternateAsset.setLookupAlternateInvestmentsAssetType(aiAssetType);

			MasterProductClassification master = masterProductClassificationRepository
					.findOne(clientOtherAlternateAssetDTO.getFinancialAssetType());
			clientOtherAlternateAsset.setMasterProductClassification(master);

			clientOtherAlternateAsset = clientOtherAlternateAssetRepository.save(clientOtherAlternateAsset);

			clientOtherAlternateAssetDTO = mapper.map(clientOtherAlternateAsset, ClientOtherAlternateAssetDTO.class);
			clientOtherAlternateAssetDTO.setClientID(clientOtherAlternateAsset.getClientMaster().getId());
			clientOtherAlternateAssetDTO
					.setFinancialAssetType(clientOtherAlternateAsset.getMasterProductClassification().getId());
			log.debug("Id " + clientOtherAlternateAssetDTO.getId() + " clientId"
					+ clientOtherAlternateAssetDTO.getClientID());

			return clientOtherAlternateAssetDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientOtherAlternateAssetDTO update(ClientOtherAlternateAssetDTO clientOtherAlternateAssetDTO)
			throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientOtherAlternateAssetDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository
					.findOne(clientOtherAlternateAssetDTO.getFamilyMemberID());
			ClientOtherAlternateAsset clientOtherAlternateAsset = mapper.map(clientOtherAlternateAssetDTO,
					ClientOtherAlternateAsset.class);
			clientOtherAlternateAsset.setClientMaster(cm);
			clientOtherAlternateAsset.setClientFamilyMember(cfm);
			log.debug("ClientId: " + clientOtherAlternateAssetDTO.getClientID());
			log.debug("Id: " + clientOtherAlternateAssetDTO.getId());

			LookupAlternateInvestmentsAssetType aiAssetType = aiAssetTypeRepository
					.findOne(clientOtherAlternateAssetDTO.getAssetTypeId());
			clientOtherAlternateAsset.setLookupAlternateInvestmentsAssetType(aiAssetType);

			MasterProductClassification master = masterProductClassificationRepository
					.findOne(clientOtherAlternateAssetDTO.getFinancialAssetType());
			clientOtherAlternateAsset.setMasterProductClassification(master);

			clientOtherAlternateAsset = clientOtherAlternateAssetRepository.save(clientOtherAlternateAsset);

			clientOtherAlternateAssetDTO = mapper.map(clientOtherAlternateAsset, ClientOtherAlternateAssetDTO.class);
			clientOtherAlternateAssetDTO.setClientID(clientOtherAlternateAsset.getClientMaster().getId());
			clientOtherAlternateAssetDTO
					.setFinancialAssetType(clientOtherAlternateAsset.getMasterProductClassification().getId());
			log.debug("Id " + clientOtherAlternateAssetDTO.getId() + " clientId"
					+ clientOtherAlternateAssetDTO.getClientID());

			return clientOtherAlternateAssetDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientOtherAlternateAssetDTO> findByClientId(int clientId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			List<ClientOtherAlternateAssetDTO> listDTO = new ArrayList<ClientOtherAlternateAssetDTO>();
			for (ClientOtherAlternateAsset clientOtherAlternateAsset : cm.getClientOtherAlternateAssets()) {
				ClientOtherAlternateAssetDTO dto = mapper.map(clientOtherAlternateAsset,
						ClientOtherAlternateAssetDTO.class);
				dto.setClientID(clientId);
				dto.setOwnerName(clientOtherAlternateAsset.getClientMaster().getFirstName() + " "
						+ clientOtherAlternateAsset.getClientMaster().getLastName());
				MasterProductClassification mpc = masterProductClassificationRepository
						.findOne(clientOtherAlternateAsset.getMasterProductClassification().getId());
				dto.setFinancialAssetName(mpc.getProductName());
				dto.setFundDescription(clientOtherAlternateAsset.getFundDescription());
				listDTO.add(dto);
			}
			return listDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException {
		// TODO Auto-generated method stub

		try {
			clientOtherAlternateAssetRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public ClientOtherAlternateAssetDTO findById(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientOtherAlternateAssetDTO clientOtherAlternateAssetDTO = mapper
					.map(clientOtherAlternateAssetRepository.findOne(id), ClientOtherAlternateAssetDTO.class);
			clientOtherAlternateAssetDTO
					.setClientID(clientOtherAlternateAssetRepository.findOne(id).getClientMaster().getId());
			clientOtherAlternateAssetDTO
					.setFamilyMemberID(clientOtherAlternateAssetRepository.findOne(id).getClientFamilyMember().getId());
			clientOtherAlternateAssetDTO.setFinancialAssetType(
					clientOtherAlternateAssetRepository.findOne(id).getMasterProductClassification().getId());
			clientOtherAlternateAssetDTO
					.setFundDescription(clientOtherAlternateAssetRepository.findOne(id).getFundDescription());

			return clientOtherAlternateAssetDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientOtherAlternateAssetDTO> findAll() {
		// TODO Auto-generated method stub
		ClientOtherAlternateAssetDTO ClientOtherAlternateAssetDTO;
		List<ClientOtherAlternateAsset> listOtherAlternateAsset = clientOtherAlternateAssetRepository.findAll();

		List<ClientOtherAlternateAssetDTO> listDTO = new ArrayList<ClientOtherAlternateAssetDTO>();
		for (ClientOtherAlternateAsset clientOtherAlternateAsset : listOtherAlternateAsset) {
			ClientOtherAlternateAssetDTO = mapper.map(clientOtherAlternateAsset, ClientOtherAlternateAssetDTO.class);
			ClientOtherAlternateAssetDTO.setClientID(clientOtherAlternateAsset.getClientMaster().getId());
			listDTO.add(ClientOtherAlternateAssetDTO);
		}
		return listDTO;
	}

}
