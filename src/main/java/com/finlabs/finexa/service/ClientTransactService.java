package com.finlabs.finexa.service;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.dto.BankNameDTO;
import com.finlabs.finexa.dto.CartOrderStatusDTO;
import com.finlabs.finexa.dto.ClientFatcaDTO;
import com.finlabs.finexa.dto.ClientRedeemDTO;
import com.finlabs.finexa.dto.ClientSTPDTO;
import com.finlabs.finexa.dto.ClientSWPDTO;
import com.finlabs.finexa.dto.ClientSwitchOrderEntryParamDTO;
import com.finlabs.finexa.dto.ClientUCCDetailsDTO;
import com.finlabs.finexa.dto.ClientUCCDraftModeDTO;
import com.finlabs.finexa.dto.ClientUCCResultDTO;
import com.finlabs.finexa.dto.ExistingClientUCCDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.InvestDTO;
import com.finlabs.finexa.dto.LumpsumCartDTO;
import com.finlabs.finexa.dto.MandateDTO;
import com.finlabs.finexa.dto.MasterBankNameIFSCCodeDTO;
import com.finlabs.finexa.dto.ProductRecommendationTransactDTO;
import com.finlabs.finexa.dto.PurchaseOrderEntryParamDTO;
import com.finlabs.finexa.dto.SIPCartDTO;
import com.finlabs.finexa.dto.SIPOrderEntryParamDTO;
import com.finlabs.finexa.dto.STPCartDTO;
import com.finlabs.finexa.dto.SWPCartDTO;
import com.finlabs.finexa.dto.SwitchCartDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.dto.ViewClientUCCDetailsDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.ClientUCCDetailsDraftMode;
import com.finlabs.finexa.model.PurchaseOrderEntryParam;
import com.finlabs.finexa.repository.ViewriskprofilesumminmaxscoreRepository;

public interface ClientTransactService {

	public ClientUCCResultDTO authenticateAdvisor(int advisorId, Session session) throws FinexaBussinessException;

	public boolean editAOFPDF(String clientCode, Session session, int docType, String mandateId)
			throws FinexaBussinessException;

	public ClientUCCResultDTO saveAndUploadClientUCC(ClientUCCDraftModeDTO clientUCCDraftModeDTO)
			throws FinexaBussinessException;

	public ClientUCCResultDTO callUCCService(int lastSavedId, String clientCode) throws FinexaBussinessException;

	public List<ViewClientUCCDetailsDTO> getExistingUCC(int clientId) throws FinexaBussinessException;

	public ClientUCCResultDTO validateExistingUCC(ExistingClientUCCDTO existingClientUCCDTO)
			throws FinexaBussinessException;

	public ClientUCCResultDTO validateAndSaveClientFatca(ClientFatcaDTO clientFatcaDTO) throws FinexaBussinessException;

	public ClientUCCResultDTO validateAndSaveClientMandate(MandateDTO mandateDTO) throws FinexaBussinessException;

	public ClientUCCResultDTO registerPurchaseOrder(PurchaseOrderEntryParamDTO purchaseOrderEntryParamDTO)
			throws FinexaBussinessException;

	public ClientUCCResultDTO redeemPurchaseOrder(ClientRedeemDTO redeemDTO) throws FinexaBussinessException;

	public ClientUCCResultDTO registerSIPOrder(SIPOrderEntryParamDTO sipOrderEntryParamDTO)
			throws FinexaBussinessException;

	public ClientUCCResultDTO registerSwitchOrder(ClientSwitchOrderEntryParamDTO clientSwitchOrderEntryParamDTO)
			throws FinexaBussinessException;

	public ClientUCCResultDTO registerSwpOrder(ClientSWPDTO clientSWPDTO) throws FinexaBussinessException;

	public ClientUCCResultDTO registerStpOrder(ClientSTPDTO clientSTPDTO) throws FinexaBussinessException;

	public ClientUCCDetailsDTO getClientByUCC(String clientCode) throws FinexaBussinessException;

	public ClientUCCResultDTO getOrdersFromCart(String clientCode) throws FinexaBussinessException;

	public List<LumpsumCartDTO> getLumpsumOrdersFromCart(String clientCode) throws FinexaBussinessException;

	public List<SIPCartDTO> getSIPOrdersFromCart(String clientCode) throws FinexaBussinessException;

	public List<SwitchCartDTO> getSwitchOrdersFromCart(String clientCode) throws FinexaBussinessException;

	public List<SWPCartDTO> getSWPOrdersFromCart(String clientCode) throws FinexaBussinessException;

	public List<STPCartDTO> getSTPOrdersFromCart(String clientCode) throws FinexaBussinessException;

	/****************** Plan & Invest **********************/
	public List<ProductRecommendationTransactDTO> getLastSavedProductRecommendation(int advsiorId, int clientId,
			int goalId, String module, String date) throws FinexaBussinessException;

	public List<ProductRecommendationTransactDTO> getLastSavedProductRecommendationPM(int advsiorId, int clientId,
			String date, String module) throws FinexaBussinessException;

	public List<ClientUCCResultDTO> invest(List<InvestDTO> investDTOList) throws FinexaBussinessException;

	public MasterBankNameIFSCCodeDTO getBankDetailsByIFSC(String ifsc) throws FinexaBussinessException;

	public List<BankNameDTO> getBankList(String clientCode) throws FinexaBussinessException;

	public List<CartOrderStatusDTO> placeLumpsumOrdersOfCart(int[] lumpsumOrders) throws FinexaBussinessException;

	public List<CartOrderStatusDTO> placeSipOrdersOfCart(int[] sipOrders) throws FinexaBussinessException;

	public List<CartOrderStatusDTO> placeSwitchOrdersOfCart(int[] switchOrders) throws FinexaBussinessException;

	public List<CartOrderStatusDTO> placeStpOrdersOfCart(int[] stpOrders) throws FinexaBussinessException;
	
	public ClientUCCDraftModeDTO getClientCKYCByUCC(String clientCode) throws FinexaBussinessException;

	public List<CartOrderStatusDTO> placeSwpOrdersOfCart(int[] swpOrders) throws FinexaBussinessException;

	public List<String> getAllMandateIDByType(String mandateType, String clientCode) throws FinexaBussinessException;
	
}
