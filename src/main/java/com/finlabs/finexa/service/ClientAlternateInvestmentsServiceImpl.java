package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientAlternateInvestmentsDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientOtherAlternateAsset;
import com.finlabs.finexa.model.ClientPreciousMetal;
import com.finlabs.finexa.model.ClientRealEstate;
import com.finlabs.finexa.model.ClientStructuredProduct;
import com.finlabs.finexa.model.ClientVehicle;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.util.FinexaConstant;

@Service("ClientAlternateInvestmentsService")
@Transactional
public class ClientAlternateInvestmentsServiceImpl implements ClientAlternateInvestmentsService{

	private static Logger log = LoggerFactory.getLogger(ClientAlternateInvestmentsServiceImpl.class);

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;
	
	@Autowired
	private FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	
	@Autowired
	private FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@Override
	public List<ClientAlternateInvestmentsDTO> viewClientAlternateInvestmentsList(int clientId) throws RuntimeException, CustomFinexaException{
		// TODO Auto-generated method stub
		List<ClientAlternateInvestmentsDTO> clientAIDTOList = new ArrayList<>();
		ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
		
		if (clientMaster == null) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_AI_VIEW_ERROR);
			throw new CustomFinexaException(FinexaConstant.CLIENT_AI_MODULE, FinexaConstant.CLIENT_AI_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "");
		}
		
			try {
				log.debug("Real Estate");
				List<ClientRealEstate> clientRealEstateList = clientMaster.getClientRealEstates();
				for (ClientRealEstate obj : clientRealEstateList) {
					ClientAlternateInvestmentsDTO clientAIDTO = new ClientAlternateInvestmentsDTO();
					clientAIDTO.setFinancialAssetType(obj.getMasterProductClassification().getId());
					clientAIDTO.setFinancialAssetTypeName(obj.getMasterProductClassification().getProductName());
					ClientFamilyMember famMember = clientFamilyMemberRepository.findOne(obj.getClientFamilyMember().getId());
					String name = famMember.getFirstName();
					if (famMember.getMiddleName() == null || famMember.getMiddleName().equals("")) {
						name = name + " ";
					} else {
						name = name + famMember.getMiddleName() + " ";
					}
					name = name + famMember.getLastName();
					clientAIDTO.setOwnerName(name);
					clientAIDTO.setAssetDescription(obj.getDescription());
					try {
						clientAIDTO.setAssetTypeName(obj.getLookupAlternateInvestmentsAssetType().getDescription());
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						throw new CustomFinexaException(FinexaConstant.LOOKUP_AI_ASSET_TYPE_MODULE, FinexaConstant.LOOKUP_AI_ASSET_TYPE_VIEW_ERROR,
								"Failed to show Real Estate record cause cannot get Asset Type.");
					}
					clientAIDTO.setCurrentValue(obj.getCurrentValue());
					clientAIDTO.setId(obj.getId());
					clientAIDTOList.add(clientAIDTO);
				} 
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_AI_REAL_ESTATE_VIEW_ERROR);
				throw new CustomFinexaException(FinexaConstant.CLIENT_AI_REAL_ESTATE_MODULE, FinexaConstant.CLIENT_AI_REAL_ESTATE_VIEW_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
			
			try {
				log.debug("Precious Metals/Commodities");
				List<ClientPreciousMetal> clientPreciousMetalList = clientMaster.getClientPreciousMetals();
				for (ClientPreciousMetal obj : clientPreciousMetalList) {
					ClientAlternateInvestmentsDTO clientAIDTO = new ClientAlternateInvestmentsDTO();
					clientAIDTO.setFinancialAssetType(obj.getMasterProductClassification().getId());
					clientAIDTO.setFinancialAssetTypeName(obj.getMasterProductClassification().getProductName());
					ClientFamilyMember famMember = clientFamilyMemberRepository.findOne(obj.getClientFamilyMember().getId());
					String name = famMember.getFirstName();
					if (famMember.getMiddleName() == null || famMember.getMiddleName().equals("")) {
						name = name + " ";
					} else {
						name = name + famMember.getMiddleName() + " ";
					}
					name = name + famMember.getLastName();
					clientAIDTO.setOwnerName(name);
					clientAIDTO.setAssetDescription(obj.getDescription());
					try {
						clientAIDTO.setAssetTypeName(obj.getLookupAlternateInvestmentsAssetType().getDescription());
					} catch (RuntimeException e) {
						throw new CustomFinexaException(FinexaConstant.LOOKUP_AI_ASSET_TYPE_MODULE, FinexaConstant.LOOKUP_AI_ASSET_TYPE_VIEW_ERROR,
								"Failed to show Precious Metals/Commodities record cause cannot get Asset Type.");
					}
					clientAIDTO.setCurrentValue(obj.getCurrentValue());
					clientAIDTO.setId(obj.getId());
					clientAIDTOList.add(clientAIDTO);
				}
			} catch (RuntimeException e) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule, FinexaConstant.CLIENT_AI_PRECIOUS_METALS_VIEW_ERROR);
				throw new CustomFinexaException(FinexaConstant.CLIENT_AI_PRECIOUS_METALS_MODULE, FinexaConstant.CLIENT_AI_PRECIOUS_METALS_VIEW_ERROR,
						exception != null ? exception.getErrorMessage() : "", e);
			}
			
						log.debug("Vehicles");
						try {
							List<ClientVehicle> clientVehiclesList = clientMaster.getClientVehicles();
							for (ClientVehicle obj : clientVehiclesList) {
								ClientAlternateInvestmentsDTO clientAIDTO = new ClientAlternateInvestmentsDTO();
								clientAIDTO.setFinancialAssetType(obj.getMasterProductClassification().getId());
								clientAIDTO.setFinancialAssetTypeName(obj.getMasterProductClassification().getProductName());
								ClientFamilyMember famMember = clientFamilyMemberRepository.findOne(obj.getClientFamilyMember().getId());
								String name = famMember.getFirstName();
								if (famMember.getMiddleName() == null || famMember.getMiddleName().equals("")) {
									name = name + " ";
								} else {
									name = name + famMember.getMiddleName() + " ";
								}
								name = name + famMember.getLastName();
								clientAIDTO.setOwnerName(name);
								clientAIDTO.setAssetDescription(obj.getDescription());
								clientAIDTO.setAssetTypeName("NA");
								clientAIDTO.setCurrentValue(obj.getCurrentValue());
								clientAIDTO.setId(obj.getId());
								clientAIDTOList.add(clientAIDTO);
							}
						} catch (RuntimeException e) {
							FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
									.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
							FinexaExceptionHandling exception = finexaExceptionHandlingRepository
									.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
											FinexaConstant.CLIENT_AI_VEHICLES_VIEW_ERROR);
							throw new CustomFinexaException(FinexaConstant.CLIENT_AI_VEHICLES_MODULE,
									FinexaConstant.CLIENT_AI_VEHICLES_VIEW_ERROR,
									exception != null ? exception.getErrorMessage() : "", e);
						}
						
						try {
							log.debug("Others");
							List<ClientOtherAlternateAsset> clientOthersList = clientMaster.getClientOtherAlternateAssets();
							for (ClientOtherAlternateAsset obj : clientOthersList) {
								ClientAlternateInvestmentsDTO clientAIDTO = new ClientAlternateInvestmentsDTO();
								clientAIDTO.setFinancialAssetType(obj.getMasterProductClassification().getId());
								clientAIDTO.setFinancialAssetTypeName(obj.getMasterProductClassification().getProductName());
								ClientFamilyMember famMember = clientFamilyMemberRepository.findOne(obj.getClientFamilyMember().getId());
								String name = famMember.getFirstName();
								if (famMember.getMiddleName() == null || famMember.getMiddleName().equals("")) {
									name = name + " ";
								} else {
									name = name + famMember.getMiddleName() + " ";
								}
								name = name + famMember.getLastName();
								clientAIDTO.setOwnerName(name);
								clientAIDTO.setAssetDescription(obj.getFundDescription());
								clientAIDTO.setAssetTypeName("NA");
								clientAIDTO.setCurrentValue(obj.getCurrentMarketValue());
								clientAIDTO.setId(obj.getId());
								clientAIDTOList.add(clientAIDTO);
							}
						} catch (RuntimeException e) {
							FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
									.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
							FinexaExceptionHandling exception = finexaExceptionHandlingRepository
									.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
											FinexaConstant.CLIENT_AI_OTHERS_VIEW_ERROR);
							throw new CustomFinexaException(FinexaConstant.CLIENT_AI_OTHERS_MODULE,
									FinexaConstant.CLIENT_AI_OTHERS_VIEW_ERROR,
									exception != null ? exception.getErrorMessage() : "", e);
						}
						
						try {
							log.debug("Structured Product");
							List<ClientStructuredProduct> clientSPList = clientMaster.getClientStructuredProducts();
							for (ClientStructuredProduct obj : clientSPList) {
								ClientAlternateInvestmentsDTO clientAIDTO = new ClientAlternateInvestmentsDTO();
								clientAIDTO.setFinancialAssetType(obj.getMasterProductClassification().getId());
								clientAIDTO.setFinancialAssetTypeName(obj.getMasterProductClassification().getProductName());
								ClientFamilyMember famMember = clientFamilyMemberRepository.findOne(obj.getClientFamilyMember().getId());
								String name = famMember.getFirstName();
								if (famMember.getMiddleName() == null || famMember.getMiddleName().equals("")) {
									name = name + " ";
								} else {
									name = name + famMember.getMiddleName() + " ";
								}
								name = name + famMember.getLastName();
								clientAIDTO.setOwnerName(name);
								clientAIDTO.setAssetDescription(obj.getDescription());
								clientAIDTO.setAssetTypeName("NA");
								clientAIDTO.setCurrentValue(obj.getCurrentValue());
								clientAIDTO.setId(obj.getId());
								clientAIDTOList.add(clientAIDTO);
							}
						} catch (RuntimeException e) {
							FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
									.findByCode(FinexaConstant.MY_CLIENT_PORTFOLIO);
							FinexaExceptionHandling exception = finexaExceptionHandlingRepository
									.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
											FinexaConstant.CLIENT_AI_SP_VIEW_ERROR);
							throw new CustomFinexaException(FinexaConstant.CLIENT_AI_SP_MODULE,
									FinexaConstant.CLIENT_AI_SP_VIEW_ERROR,
									exception != null ? exception.getErrorMessage() : "", e);
						}
			
			return clientAIDTOList;
	}
}				
			
	
	
	
	


