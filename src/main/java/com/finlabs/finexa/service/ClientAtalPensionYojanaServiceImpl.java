package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientAtalPensionYojanaDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.model.ClientAtalPensionYojana;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupFrequency;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.ClientAtalPensionYojanaRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.FrequencyRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.util.FinexaUtil;


@Service("ClientAtalPensionYojanaService")
@Transactional
public class ClientAtalPensionYojanaServiceImpl implements ClientAtalPensionYojanaService {
	private static Logger log = LoggerFactory.getLogger(ClientAtalPensionYojanaServiceImpl.class);
	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private ClientAtalPensionYojanaRepository clientATPRepository;
	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;

	@Autowired
	private FrequencyRepository frequencyRepository;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;
	
	@Autowired
	AdvisorService advisorService;

	@Override
	public ClientAtalPensionYojanaDTO save(ClientAtalPensionYojanaDTO clientATPDTO, HttpServletRequest request) throws RuntimeException{
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientATPDTO.getClientID());

			ClientAtalPensionYojana clientATP = mapper.map(clientATPDTO, ClientAtalPensionYojana.class);
			clientATP.setClientMaster(cm);
			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientATPDTO.getFinancialAssetType());
			clientATP.setMasterProductClassification(masterProductClassification);
			
			ClientFamilyMember fam = clientFamilyMemberRepository.findOne(clientATPDTO.getFamilyMemberID());
			clientATP.setClientFamilyMember(fam);
			LookupFrequency lookupFrequency = frequencyRepository.findOne(clientATPDTO.getInvestmentFrequency());
			clientATP.setLookupFrequency(lookupFrequency);
			clientATP = clientATPRepository.save(clientATP);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientATPDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientAtalPensionYojanaDTO update(ClientAtalPensionYojanaDTO clientATPDTO, HttpServletRequest request) throws RuntimeException{

		try {
			
			ClientMaster cm = clientMasterRepository.findOne(clientATPDTO.getClientID());

			ClientAtalPensionYojana clientATP = mapper.map(clientATPDTO, ClientAtalPensionYojana.class);
			
			clientATP.setClientMaster(cm);
			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientATPDTO.getFinancialAssetType());
			clientATP.setMasterProductClassification(masterProductClassification);
			
			ClientFamilyMember fam = clientFamilyMemberRepository.findOne(clientATPDTO.getFamilyMemberID());
			clientATP.setClientFamilyMember(fam);
			LookupFrequency lookupFrequency = frequencyRepository.findOne(clientATPDTO.getInvestmentFrequency());
			clientATP.setLookupFrequency(lookupFrequency);
			
			clientATP = clientATPRepository.save(clientATP);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);

			return clientATPDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientAtalPensionYojanaDTO> findAll() {
		// TODO Auto-generated method stub
		ClientAtalPensionYojanaDTO ClientAtalPensionYojanaDTO;
		List<ClientAtalPensionYojana> listClientATP = clientATPRepository.findAll();

		List<ClientAtalPensionYojanaDTO> listDTO = new ArrayList<ClientAtalPensionYojanaDTO>();
		for (ClientAtalPensionYojana clientATP : listClientATP) {
			ClientAtalPensionYojanaDTO = mapper.map(clientATP, ClientAtalPensionYojanaDTO.class);
			ClientAtalPensionYojanaDTO.setClientID(clientATP.getClientMaster().getId());
			listDTO.add(ClientAtalPensionYojanaDTO);
		}

		return listDTO;
	}

	@Override
	public List<ClientAtalPensionYojanaDTO> findByClientId(int clientId) throws RuntimeException{

		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			ClientAtalPensionYojanaDTO clientATPDTO;
			List<ClientAtalPensionYojana> clientATPList = clientMaster.getClientAtalPensionYojanas();
			List<ClientAtalPensionYojanaDTO> clientATPDTOList = new ArrayList<ClientAtalPensionYojanaDTO>();

			for (ClientAtalPensionYojana obj : clientATPList) {

				clientATPDTO = mapper.map(obj, ClientAtalPensionYojanaDTO.class);
				clientATPDTO.setClientID(obj.getClientMaster().getId());
				clientATPDTOList.add(clientATPDTO);
			}

			return clientATPDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException {

		try {
			clientATPRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public ClientAtalPensionYojanaDTO findById(int id) throws RuntimeException {

		try {
			ClientAtalPensionYojana clientAPY = clientATPRepository.findOne(id);
			ClientAtalPensionYojanaDTO clientATPDTO = mapper.map(clientAPY, ClientAtalPensionYojanaDTO.class);
			clientATPDTO.setClientID(clientATPRepository.findOne(id).getClientMaster().getId());
			clientATPDTO.setFamilyMemberID(clientAPY.getClientFamilyMember().getId());
			clientATPDTO.setFirstName(clientAPY.getClientFamilyMember().getFirstName());
			clientATPDTO.setRelationId(clientAPY.getClientFamilyMember().getLookupRelation().getId());
			clientATPDTO.setRelationName(clientAPY.getClientFamilyMember().getLookupRelation().getDescription());
			clientATPDTO.setGender(FinexaUtil.getGender(clientAPY.getClientFamilyMember().getClientMaster(),
					clientAPY.getClientFamilyMember().getLookupRelation()));
			clientATPDTO.setFinancialAssetType(clientAPY.getMasterProductClassification().getId());
			clientATPDTO.setInvestmentFrequency(clientAPY.getLookupFrequency().getId());
			return clientATPDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean checkIfApyPresent(Integer clientId) {
		ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

		List<ClientAtalPensionYojana> ClientApysList = clientMaster.getClientAtalPensionYojanas();
		if (ClientApysList.size() > 0)
			return true;
		else
			return false;
	}

	@Override
	public List<ClientFamilyMemberDTO> checkIfApyPresentForAll(Integer clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			List<ClientFamilyMember> ClientFamilyMembers = clientMaster.getClientFamilyMembers();

			List<ClientAtalPensionYojana> ClientApys = clientMaster.getClientAtalPensionYojanas();

			List<ClientFamilyMember> ClientFamilyMembers1 = new ArrayList<ClientFamilyMember>();

			for (ClientAtalPensionYojana cf : ClientApys) {

				ClientFamilyMembers1.add(cf.getClientFamilyMember());
			}

			ClientFamilyMembers.removeAll(ClientFamilyMembers1);

			for (ClientFamilyMember cf : ClientFamilyMembers) {

				log.debug("not income member " + cf.getId());
			}

			List<ClientFamilyMemberDTO> cfmDtoList = new ArrayList<ClientFamilyMemberDTO>();

			for (ClientFamilyMember clientFamilyMember : ClientFamilyMembers) {
				ClientFamilyMemberDTO clientFamilyMemberDTO = mapper.map(clientFamilyMember,
						ClientFamilyMemberDTO.class);

				if (clientFamilyMember.getLookupRelation().getId() == 0) {
					clientFamilyMemberDTO.setGender(clientMaster.getGender());
				}
				if (clientFamilyMember.getLookupRelation().getId() == 1) {
					if (clientMaster.getGender().equals("M")) {
						clientFamilyMemberDTO.setGender("F");
					} else {
						clientFamilyMemberDTO.setGender("M");
					}
				}
				clientFamilyMemberDTO.setRelationName(clientFamilyMember.getLookupRelation().getDescription());
				clientFamilyMemberDTO.setRelationID(clientFamilyMember.getLookupRelation().getId());

				cfmDtoList.add(clientFamilyMemberDTO);
			}
			Collections.sort(cfmDtoList, new Comparator<ClientFamilyMemberDTO>() {

				@Override
				public int compare(ClientFamilyMemberDTO relation1, ClientFamilyMemberDTO relation2) {
					// TODO Auto-generated method stub
					return relation1.getRelationID().compareTo(relation2.getRelationID());
				}
			});

			return cfmDtoList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}

	}

}
