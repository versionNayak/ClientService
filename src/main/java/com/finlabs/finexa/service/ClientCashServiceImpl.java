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

import com.finlabs.finexa.dto.ClientCashDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.ClientCash;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterCash;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.ClientCashRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.MasterCashRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.util.FinexaConstant;

@Service("ClientCashService")
@Transactional
public class ClientCashServiceImpl implements ClientCashService {

	private static Logger log = LoggerFactory.getLogger(ClientCashServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientCashRepository clientCashRepository;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private MasterCashRepository masterCashRepository;

	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;
	
	@Autowired
	AdvisorService advisorService;

	@Override
	public ClientCashDTO save(ClientCashDTO clientCashDTO, HttpServletRequest request) throws RuntimeException {
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientCashDTO.getClientId());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientCashDTO.getFamilyMemberId());
			ClientCash clientCash = mapper.map(clientCashDTO, ClientCash.class);
			clientCash.setClientMaster(cm);
			clientCash.setClientFamilyMember(cfm);
			log.debug("Add Cash: Bank Details: " + clientCashDTO.getBankID());
			if (clientCashDTO.getBankID() != null) {
				MasterCash masterCash = masterCashRepository.findOne(clientCashDTO.getBankID());
				clientCash.setMasterCash(masterCash);
			}
			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientCashDTO.getCashBalanceTypeId());
			clientCash.setMasterProductClassification(masterProductClassification);
			clientCash = clientCashRepository.save(clientCash);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientCashDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	@Override
	public void autoSave(ClientCashDTO clientCashDTO) throws RuntimeException {
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientCashDTO.getClientId());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientCashDTO.getFamilyMemberId());
			ClientCash clientCash = mapper.map(clientCashDTO, ClientCash.class);
			clientCash.setClientMaster(cm);
			clientCash.setClientFamilyMember(cfm);
			log.debug("Add Cash: Bank Details: " + clientCashDTO.getBankID());
			if (clientCashDTO.getBankID() != null) {
				MasterCash masterCash = masterCashRepository.findOne(clientCashDTO.getBankID());
				clientCash.setMasterCash(masterCash);
			}
			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientCashDTO.getCashBalanceTypeId());
			clientCash.setMasterProductClassification(masterProductClassification);
			clientCash = clientCashRepository.save(clientCash);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	@Override
	public ClientCashDTO update(ClientCashDTO clientCashDTO, HttpServletRequest request) throws RuntimeException {
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientCashDTO.getClientId());
			ClientCash clientCash = mapper.map(clientCashDTO, ClientCash.class);
			clientCash.setClientMaster(cm);
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientCashDTO.getFamilyMemberId());
			clientCash.setClientFamilyMember(cfm);

			// MasterCash masterCash =
			// masterCashRepository.findOne(clientCashDTO.getBankID());
			log.debug("Edit Cash: Bank Details: " + clientCashDTO.getBankID());
			// log.debug("Bank Details clientCash:
			// "+clientCash.getMasterCash().getId());
			if (clientCashDTO.getBankID() != null) {
				MasterCash masterCash = masterCashRepository.findOne(clientCashDTO.getBankID());
				clientCash.setMasterCash(masterCash);
			}

			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientCashDTO.getCashBalanceTypeId());
			clientCash.setMasterProductClassification(masterProductClassification);
			clientCash = clientCashRepository.save(clientCash);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientCashDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientCashDTO findById(int id) throws RuntimeException {
		try {
			ClientCash clientCash = clientCashRepository.findOne(id);
			ClientCashDTO clientCashDTO = mapper.map(clientCash, ClientCashDTO.class);
			clientCashDTO.setClientId(clientCash.getClientMaster().getId());
			clientCashDTO.setFamilyMemberId(clientCash.getClientFamilyMember().getId());
			if (clientCash.getMasterCash() != null) {
				clientCashDTO.setBankID(clientCash.getMasterCash().getId());
			} else
				log.debug("Edit Cash: Bank ID is null");
			clientCashDTO.setCashBalanceTypeId(clientCash.getMasterProductClassification().getId());
			return clientCashDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientCashDTO> findAll() {
		ClientCashDTO ClientCashDTO;
		List<ClientCash> listClientcash = clientCashRepository.findAll();

		List<ClientCashDTO> listDTO = new ArrayList<ClientCashDTO>();
		for (ClientCash clientCash : listClientcash) {
			ClientCashDTO = mapper.map(clientCash, ClientCashDTO.class);
			ClientCashDTO.setClientId(clientCash.getClientMaster().getId());
			listDTO.add(ClientCashDTO);
		}

		return listDTO;
	}

	@Override
	public List<ClientCashDTO> findByClientId(int clientId) throws RuntimeException, CustomFinexaException {
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			List<ClientCashDTO> listDTO = new ArrayList<ClientCashDTO>();
			for (ClientCash clientCash : cm.getClientCashs()) {
				ClientCashDTO dto = mapper.map(clientCash, ClientCashDTO.class);
				dto.setClientId(clientId);
				dto.setOwnerName(clientCash.getClientFamilyMember().getFirstName() + " "
						+ (clientCash.getClientFamilyMember().getMiddleName() == null ? " "
								: clientCash.getClientFamilyMember().getMiddleName())
						+ " " + clientCash.getClientFamilyMember().getLastName());
				try {
					if (clientCash.getMasterCash() != null) {
						dto.setBankName(clientCash.getMasterCash().getName());
					} else
						log.debug("View Cash: Bank ID is null");
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					throw new CustomFinexaException(FinexaConstant.MASTER_CASH_MODULE, FinexaConstant.MASTER_CASH_VIEW_ERROR,
							"Failed to show record cause cannot get Bank Name.");
				}
				dto.setCashBalanceTypeName(clientCash.getMasterProductClassification().getProductName());
				listDTO.add(dto);
			}
			return listDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException {

		try {
			clientCashRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}
}
