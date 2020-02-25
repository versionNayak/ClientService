package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientVehicleDTO;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientVehicle;
import com.finlabs.finexa.model.LookupAlternateInvestmentsAssetType;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.AlternateInvestmentsAssetTypeRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientVehicleRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;

@Service("ClientVehicleService")
@Transactional
public class ClientVehicleServiceImpl implements ClientVehicleService {

	private static Logger log = LoggerFactory.getLogger(ClientVehicleServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private ClientVehicleRepository clientVehicleRepository;

	@Autowired
	private AlternateInvestmentsAssetTypeRepository aiAssetTypeRepository;

	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;

	@Override
	public ClientVehicleDTO save(ClientVehicleDTO clientVehicleDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientVehicleDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientVehicleDTO.getFamilyMemberID());
			ClientVehicle clientVehicle = mapper.map(clientVehicleDTO, ClientVehicle.class);
			clientVehicle.setClientMaster(cm);
			clientVehicle.setClientFamilyMember(cfm);
			log.debug("ClientId: " + clientVehicleDTO.getClientID());
			log.debug("Id: " + clientVehicleDTO.getId());

			LookupAlternateInvestmentsAssetType aiAssetType = aiAssetTypeRepository
					.findOne(clientVehicleDTO.getAssetTypeId());
			clientVehicle.setLookupAlternateInvestmentsAssetType(aiAssetType);

			MasterProductClassification master = masterProductClassificationRepository
					.findOne(clientVehicleDTO.getFinancialAssetType());
			clientVehicle.setMasterProductClassification(master);

			clientVehicle.setDescription(clientVehicleDTO.getvDescription());

			clientVehicle = clientVehicleRepository.save(clientVehicle);

			clientVehicleDTO = mapper.map(clientVehicle, ClientVehicleDTO.class);
			clientVehicleDTO.setClientID(clientVehicle.getClientMaster().getId());
			clientVehicleDTO.setAssetTypeId(clientVehicle.getLookupAlternateInvestmentsAssetType().getId());
			clientVehicleDTO.setFinancialAssetType(clientVehicle.getMasterProductClassification().getId());
			log.debug("Id " + clientVehicleDTO.getId() + " clientId" + clientVehicleDTO.getClientID());

			return clientVehicleDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientVehicleDTO update(ClientVehicleDTO clientVehicleDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientVehicleDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientVehicleDTO.getFamilyMemberID());
			ClientVehicle clientVehicle = mapper.map(clientVehicleDTO, ClientVehicle.class);
			clientVehicle.setClientMaster(cm);
			clientVehicle.setClientFamilyMember(cfm);
			log.debug("ClientId: " + clientVehicleDTO.getClientID());
			log.debug("Id: " + clientVehicleDTO.getId());

			LookupAlternateInvestmentsAssetType aiAssetType = aiAssetTypeRepository
					.findOne(clientVehicleDTO.getAssetTypeId());
			clientVehicle.setLookupAlternateInvestmentsAssetType(aiAssetType);

			MasterProductClassification master = masterProductClassificationRepository
					.findOne(clientVehicleDTO.getFinancialAssetType());
			clientVehicle.setMasterProductClassification(master);

			clientVehicle.setDescription(clientVehicleDTO.getvDescription());

			clientVehicle = clientVehicleRepository.save(clientVehicle);

			clientVehicleDTO = mapper.map(clientVehicle, ClientVehicleDTO.class);
			clientVehicleDTO.setClientID(clientVehicle.getClientMaster().getId());
			clientVehicleDTO.setAssetTypeId(clientVehicle.getLookupAlternateInvestmentsAssetType().getId());
			clientVehicleDTO.setFinancialAssetType(clientVehicle.getMasterProductClassification().getId());
			log.debug("Id " + clientVehicleDTO.getId() + " clientId" + clientVehicleDTO.getClientID());

			return clientVehicleDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientVehicleDTO> findByClientId(int clientId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			List<ClientVehicleDTO> listDTO = new ArrayList<ClientVehicleDTO>();
			for (ClientVehicle clientVehicle : cm.getClientVehicles()) {
				ClientVehicleDTO dto = mapper.map(clientVehicle, ClientVehicleDTO.class);
				dto.setClientID(clientId);
				dto.setOwnerName(clientVehicle.getClientMaster().getFirstName() + " "
						+ clientVehicle.getClientMaster().getLastName());
				MasterProductClassification mpc = masterProductClassificationRepository
						.findOne(clientVehicle.getMasterProductClassification().getId());
				dto.setFinancialAssetName(mpc.getProductName());
				dto.setvDescription(clientVehicle.getDescription());
				LookupAlternateInvestmentsAssetType assetType = aiAssetTypeRepository
						.findOne(clientVehicle.getLookupAlternateInvestmentsAssetType().getId());
				dto.setAssetTypeName(assetType.getDescription());
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
			clientVehicleRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public ClientVehicleDTO findById(int id) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			ClientVehicleDTO clientVehicleDTO = mapper.map(clientVehicleRepository.findOne(id), ClientVehicleDTO.class);
			clientVehicleDTO.setClientID(clientVehicleRepository.findOne(id).getClientMaster().getId());
			clientVehicleDTO.setFamilyMemberID(clientVehicleRepository.findOne(id).getClientFamilyMember().getId());
			clientVehicleDTO
					.setFinancialAssetType(clientVehicleRepository.findOne(id).getMasterProductClassification().getId());
			clientVehicleDTO
					.setAssetTypeId(clientVehicleRepository.findOne(id).getLookupAlternateInvestmentsAssetType().getId());
			clientVehicleDTO.setCurrentValue(clientVehicleRepository.findOne(id).getCurrentValue());
			clientVehicleDTO.setvDescription(clientVehicleRepository.findOne(id).getDescription());
			return clientVehicleDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientVehicleDTO> findAll() {
		// TODO Auto-generated method stub
		ClientVehicleDTO ClientVehicleDTO;
		List<ClientVehicle> listVehicle = clientVehicleRepository.findAll();

		List<ClientVehicleDTO> listDTO = new ArrayList<ClientVehicleDTO>();
		for (ClientVehicle clientVehicle : listVehicle) {
			ClientVehicleDTO = mapper.map(clientVehicle, ClientVehicleDTO.class);
			ClientVehicleDTO.setClientID(clientVehicle.getClientMaster().getId());
			listDTO.add(ClientVehicleDTO);
		}
		return listDTO;
	}

}
