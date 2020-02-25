package com.finlabs.finexa.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientROSDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.ClientAnnuity;
import com.finlabs.finexa.model.ClientAtalPensionYojana;
import com.finlabs.finexa.model.ClientEPF;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientNPS;
import com.finlabs.finexa.model.ClientPPF;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.util.FinexaConstant;

@Service("ClientROSService")
@Transactional
public class ClientROSServiceImpl implements ClientROSService {
	
	private static Logger log = LoggerFactory.getLogger(ClientROSServiceImpl.class);
	
	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;

	@Autowired
	private FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;

	@Override
	public List<ClientROSDTO> viewClientROSList(int clientId) throws RuntimeException, FinexaBussinessException {
		// TODO Auto-generated method stub
		List<ClientROSDTO> clientROSDTOList = new ArrayList<>();
		ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

		if (clientMaster == null) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_MODULE, FinexaConstant.CLIENT_ROS_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "");
		}

		try {
			
			List<ClientPPF> clientPPFList = clientMaster.getClientPpfs();
			for (ClientPPF obj : clientPPFList) {
				ClientROSDTO clientROSDTO = new ClientROSDTO();
				clientROSDTO.setFinancialAssetType(obj.getMasterProductClassification().getId());
				ClientFamilyMember famMember = clientFamilyMemberRepository
						.findOne(obj.getClientFamilyMember().getId());
				String name = famMember.getFirstName();
				if (famMember.getMiddleName() == null || famMember.getMiddleName().equals("")) {
					name = name + " ";
				} else {
					name = name + famMember.getMiddleName() + " ";
				}
				name = name + famMember.getLastName();
				clientROSDTO.setOwnerName(name);
				clientROSDTO.setProductName(obj.getMasterProductClassification().getProductName());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				clientROSDTO.setStartValue(sdf.format(obj.getStartDate()));
				clientROSDTO.setId(obj.getId());
				clientROSDTOList.add(clientROSDTO);
			}
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_PPF_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MASTER_PPF_INTEREST_RATE_MODULE,
					FinexaConstant.CLIENT_ROS_PPF_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

		try {
			log.debug("EPF");
			List<ClientEPF> clientEPFList = clientMaster.getClientEpfs();
			for (ClientEPF obj : clientEPFList) {
				ClientROSDTO clientROSDTO = new ClientROSDTO();
				clientROSDTO.setFinancialAssetType(obj.getMasterProductClassification().getId());
				ClientFamilyMember famMember = clientFamilyMemberRepository
						.findOne(obj.getClientFamilyMember().getId());
				String name = famMember.getFirstName();
				if (famMember.getMiddleName() == null || famMember.getMiddleName().equals("")) {
					name = name + " ";
				} else {
					name = name + famMember.getMiddleName() + " ";
				}
				name = name + famMember.getLastName();
				clientROSDTO.setOwnerName(name);
				clientROSDTO.setProductName(obj.getMasterProductClassification().getProductName());
				clientROSDTO.setStartValue("NA");
				clientROSDTO.setId(obj.getId());
				clientROSDTOList.add(clientROSDTO);
			}
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_EPF_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_EPF_MODULE,
					FinexaConstant.CLIENT_ROS_EPF_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

		try {
			log.debug("NPS");
			List<ClientNPS> clientNPFList = clientMaster.getClientNps();
			for (ClientNPS obj : clientNPFList) {
				ClientROSDTO clientROSDTO = new ClientROSDTO();
				clientROSDTO.setFinancialAssetType(obj.getMasterProductClassification().getId());
				ClientFamilyMember famMember = clientFamilyMemberRepository
						.findOne(obj.getClientFamilyMember().getId());
				String name = famMember.getFirstName();
				if (famMember.getMiddleName() == null || famMember.getMiddleName().equals("")) {
					name = name + " ";
				} else {
					name = name + famMember.getMiddleName() + " ";
				}
				name = name + famMember.getLastName();
				clientROSDTO.setOwnerName(name);
				clientROSDTO.setProductName(obj.getMasterProductClassification().getProductName());
				clientROSDTO.setStartValue("NA");
				clientROSDTO.setId(obj.getId());
				clientROSDTOList.add(clientROSDTO);
			}
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_NPS_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_NPS_MODULE,
					FinexaConstant.CLIENT_ROS_NPS_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

		try {
			log.debug("Annuity");
			List<ClientAnnuity> clientAnnuityList = clientMaster.getClientAnnuities();
			for (ClientAnnuity obj : clientAnnuityList) {
				ClientROSDTO clientROSDTO = new ClientROSDTO();
				clientROSDTO.setFinancialAssetType(obj.getMasterProductClassification().getId());
				ClientFamilyMember famMember = clientFamilyMemberRepository
						.findOne(obj.getClientFamilyMember().getId());
				String name = famMember.getFirstName();
				if (famMember.getMiddleName() == null || famMember.getMiddleName().equals("")) {
					name = name + " ";
				} else {
					name = name + famMember.getMiddleName() + " ";
				}
				name = name + famMember.getLastName();
				clientROSDTO.setOwnerName(name);
				clientROSDTO.setProductName(obj.getMasterProductClassification().getProductName());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

				if (obj.getAnnuityStartDate() != null) {
					clientROSDTO.setStartValue(sdf.format(obj.getAnnuityStartDate()));
				}
				
				clientROSDTO.setId(obj.getId());
				if (obj.getClientEpf() != null) {
					clientROSDTO.setEpfId(obj.getClientEpf().getId());
				}
				clientROSDTO.setAnnuityType(obj.getLookupAnnuityType().getId());
				clientROSDTOList.add(clientROSDTO);
			}
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_ANNUITY_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_ANNUITY_MODULE,
					FinexaConstant.CLIENT_ROS_ANNUITY_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

		try {
			log.debug("Atal Pension Yojana");
			List<ClientAtalPensionYojana> clientAPYList = clientMaster.getClientAtalPensionYojanas();
			for (ClientAtalPensionYojana obj : clientAPYList) {
				ClientROSDTO clientROSDTO = new ClientROSDTO();
				clientROSDTO.setFinancialAssetType(obj.getMasterProductClassification().getId());
				ClientFamilyMember famMember = clientFamilyMemberRepository.findOne(obj.getClientFamilyMember().getId());
				String name = famMember.getFirstName();
				if (famMember.getMiddleName() == null || famMember.getMiddleName().equals("")) {
					name = name + " ";
				} else {
					name = name + famMember.getMiddleName() + " ";
				}
				name = name + famMember.getLastName();
				clientROSDTO.setOwnerName(name);
				clientROSDTO.setProductName(obj.getMasterProductClassification().getProductName());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

				clientROSDTO.setStartValue(sdf.format(obj.getApyStartDate()));
				clientROSDTO.setId(obj.getId());
				clientROSDTOList.add(clientROSDTO);
			}
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_ROS_APY_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.CLIENT_ROS_APY_MODULE,
					FinexaConstant.CLIENT_ROS_APY_VIEW_ERROR, exception != null ? exception.getErrorMessage() : "", e);
		}

		return clientROSDTOList;
	}

}
