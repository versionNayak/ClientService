package com.finlabs.finexa.web;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.BankNameDTO;
import com.finlabs.finexa.dto.CartOrderStatusDTO;
import com.finlabs.finexa.dto.ClientFatcaDTO;
import com.finlabs.finexa.dto.ClientRedeemDTO;
import com.finlabs.finexa.dto.ClientSTPDTO;
import com.finlabs.finexa.dto.ClientSWPDTO;
import com.finlabs.finexa.dto.ClientSwitchOrderEntryParamDTO;
import com.finlabs.finexa.dto.ClientTransactAOFDTO;
import com.finlabs.finexa.dto.ClientTransactNachDTO;
import com.finlabs.finexa.dto.ClientUCCDetailsDTO;
import com.finlabs.finexa.dto.ClientUCCDraftModeDTO;
import com.finlabs.finexa.dto.ClientUCCResultDTO;
import com.finlabs.finexa.dto.ExistingClientUCCDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.InvestDTO;
import com.finlabs.finexa.dto.LumpsumCartDTO;
import com.finlabs.finexa.dto.MandateDTO;
import com.finlabs.finexa.dto.MasterBankNameIFSCCodeDTO;
import com.finlabs.finexa.dto.MasterTransactMandateDTO;
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
import com.finlabs.finexa.service.ClientTransactService;
import com.finlabs.finexa.service.ClientTransactUploadService;
import com.itextpdf.io.IOException;

@RestController
public class ClientTransactController {

	
	public static final int DOC_TYPE_AOF = 1;
	public static final int DOC_TYPE_NACH = 2;
	
	private static Logger log = LoggerFactory.getLogger(ClientTransactController.class);

	/*
	 * @Resource(name = "exceptionmap") private Map<String, String>
	 * exceptionmap;
	 */
	@Autowired
	ClientTransactService clientTransactService;
	
	@Autowired
	ClientTransactUploadService clientTransactUploadService;
	
	@Autowired
	private SessionFactory sessionfactory;


	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/authenticateAdvisor", method = RequestMethod.GET)
	public ResponseEntity<?> authenticateAdvisor(@RequestParam(value = "advisorUserId") int advisorUserId, HttpServletRequest request) throws FinexaBussinessException {
		Session session = null;
		try {
			session = sessionfactory.openSession();
			request.getSession().setAttribute("cart","1");
			ClientUCCResultDTO statusCode = clientTransactService.authenticateAdvisor(advisorUserId,session);
			return new ResponseEntity<ClientUCCResultDTO>(statusCode, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/storeCartInSession", method = RequestMethod.GET)
	public String testMestod(HttpServletRequest request){
	   request.getSession().setAttribute("cart","1");
	   return "testJsp";
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/getCartInSession", method = RequestMethod.GET)
	public String getCartInSession(HttpServletRequest request){
	   String parameters = (String) request.getSession().getAttribute("cart");
	   return parameters;
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/getClientByUCC/{clientCode}", method = RequestMethod.GET)
	public ResponseEntity<?> getDetailsByUCC(@PathVariable String clientCode) throws FinexaBussinessException, CustomFinexaException {
		try {
			ClientUCCDetailsDTO clientUCCDetailsDTO = clientTransactService.getClientByUCC(clientCode);
			return new ResponseEntity<ClientUCCDetailsDTO>(clientUCCDetailsDTO, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		}
	}

	/*@RequestMapping(value = "/saveAndUploadCCCreation", method = RequestMethod.GET)
	public ResponseEntity<?> saveAndUploadCCCreation(@RequestParam(value = "advisorId") int advisorId,
			@RequestParam(value = "clientHolding") Object clientHolding,
			@RequestParam(value = "clientPan") Object clientPan, 
			@RequestParam(value = "clientBank") Object clientBank,
			@RequestParam(value = "clientContact") Object clientContact,
			@RequestParam(value = "clientTax") Object clientTax) throws FinexaBussinessException {
		
		try {
			ClientUCCResultDTO clientUCCResultDTO = clientTransactService.saveAndUploadClientUCC(clientHolding, 
					clientPan, clientBank, clientContact, clientTax, advisorId);
			return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		}
	}*/
	
	/*********************Map Existing BSEClients with Finexa Clients ***************************/
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/saveUCCDraftMode", method = RequestMethod.POST)
	public ResponseEntity<?> saveUCCDraftMode(@Valid @RequestBody ClientUCCDraftModeDTO clientUCCDraftModeDTO)
			throws FinexaBussinessException {
		log.debug("clientUCCDraftModeDTO: " + clientUCCDraftModeDTO);
		try {
			ClientUCCResultDTO  clientUCCResultDTO = clientTransactService.saveAndUploadClientUCC(clientUCCDraftModeDTO);
			return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","");
			}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/fireUCCAPI", method = RequestMethod.GET)
	public ResponseEntity<?> fireUCCAPI(@RequestParam(value = "lastSavedId") int lastSavedId, 
			@RequestParam(value = "clientCode") String clientCode) throws FinexaBussinessException {
		Session session = null;
		try {
			session = sessionfactory.openSession();
			ClientUCCResultDTO  clientUCCResultDTO = clientTransactService.callUCCService(lastSavedId, clientCode);
			return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/editPDF", method = RequestMethod.GET)
	public ResponseEntity<?> editPDF(@RequestParam(value = "clientCode") String clientCode, 
			@RequestParam(value = "docType") int docType, @RequestParam(value = "mandateId") String mandateId) throws FinexaBussinessException {
		Session session = null;
		try {
			session = sessionfactory.openSession();
			boolean statusCode = clientTransactService.editAOFPDF(clientCode, session, docType, mandateId);
			return new ResponseEntity<Boolean>(statusCode, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/downloadAOFForm", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<?> downloadAOFForm(HttpServletResponse response,
			@RequestParam(value = "clientCode") String clientCode, @RequestParam(value = "docType") int docType) {
		log.info("ClientIncomeController >>> Entering downloadExcelOutputExl() ");
		ResponseEntity<?> returner = null;
		Session session = null;
		String fileToBeDownloaded = "";
		if (docType == DOC_TYPE_AOF) {
			fileToBeDownloaded = "NewAOF.pdf";
		} else {
			fileToBeDownloaded = "NACHTemplateUpdated.pdf";
		}
		
		try {
			session = sessionfactory.openSession();
			ClassLoader loader = getClass().getClassLoader();
			File file = null;
			if (loader.getResource(fileToBeDownloaded).getFile() != null) {
				file = new File(loader.getResource(fileToBeDownloaded).getFile());
			} else {
				throw new FinexaBussinessException("MF-Transact", "500", "Download Failed");
			}

			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/pdf"));

			int length = (int)file.length();
			byte[] bytes = new byte[length];
			try {
				BufferedInputStream reader = new BufferedInputStream(new 
						FileInputStream(file));
				reader.read(bytes, 0, length);
				System.out.println(reader);
				// setFile(bytes);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			header.setContentLength(bytes.length);
			returner = new ResponseEntity<byte[]>(bytes, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("MF-Transact", "500", "Download Failed",exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		}
		log.info("MF-Transact <<< Exiting downloadPDF() ");
		return returner;
	}

	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/clientTransact/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByClientCode(@PathVariable int clientId) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<ViewClientUCCDetailsDTO> viewClientUCCDetailsDTOList = clientTransactService.getExistingUCC(clientId);
			return new ResponseEntity<List<ViewClientUCCDetailsDTO>>(viewClientUCCDetailsDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
					throw new FinexaBussinessException("ClientTransact", "500", "Failed to get UCC Details");
		}
	}
	
	/*********************Map Existing BSEClients with Finexa Clients ***************************/
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/validateExistingClient", method = RequestMethod.POST)
	public ResponseEntity<?> createClientCash(@Valid @RequestBody ExistingClientUCCDTO existingClientUCCDTO)
			throws FinexaBussinessException {
		log.debug("clientTransactDTO: " + existingClientUCCDTO);
		try {
			ClientUCCResultDTO  clientUCCResultDTO = clientTransactService.validateExistingUCC(existingClientUCCDTO);
			return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","");
			}
	
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/clientTransact/uploadBulkUCC", method = RequestMethod.POST)
    public ResponseEntity<?> uploadBulkUsers(@ModelAttribute FileuploadDTO fileuploadDTO)
            throws FinexaBussinessException, CustomFinexaException {
        try {
            UploadResponseDTO uploadResponseDTO = clientTransactUploadService.uploadBulkUsers(fileuploadDTO);
            return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
           throw new FinexaBussinessException("", "", "");
        }
    }
	
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/clientTransact/uploadAOF", method = RequestMethod.POST)
    public ResponseEntity<?> uploadAOF(@ModelAttribute ClientTransactAOFDTO fileuploadDTO)
            throws FinexaBussinessException, CustomFinexaException {
        try {
            ClientUCCResultDTO clientUCCResultDTO = clientTransactUploadService.uploadAOF(fileuploadDTO);
            return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
           throw new FinexaBussinessException("", "", "");
        }

    }
	
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/clientTransact/uploadNach", method = RequestMethod.POST)
    public ResponseEntity<?> uploadNach(@ModelAttribute ClientTransactNachDTO fileuploadDTO)
            throws FinexaBussinessException, CustomFinexaException {
        try {
            ClientUCCResultDTO clientUCCResultDTO = clientTransactUploadService.uploadNach(fileuploadDTO);
            return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
           throw new FinexaBussinessException("", "", "");
        }

    }
	
	/*********************Fatca Upload ***************************/
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/validateAndSaveClientFatca", method = RequestMethod.POST)
	public ResponseEntity<?> validateAndSaveClientFatca(@Valid @RequestBody ClientFatcaDTO clientFatcaDTO)
			throws FinexaBussinessException {
	
		try {
			ClientUCCResultDTO  clientUCCResultDTO = clientTransactService.validateAndSaveClientFatca(clientFatcaDTO);
			return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","");
			}
	
	}
	
	/*********************Mandate Registration ***************************/
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/validateAndSaveMandate", method = RequestMethod.POST)
	public ResponseEntity<?> validateAndSaveMandate(@Valid @RequestBody MandateDTO mandateDTO)
			throws FinexaBussinessException {
		try {
			ClientUCCResultDTO  clientUCCResultDTO = clientTransactService.validateAndSaveClientMandate(mandateDTO);
			return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","");
			}
	
	}
	
	/*********************Purchase Order Registration ***************************/
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/registerPurchaseOrder", method = RequestMethod.POST)
	public ResponseEntity<?> registerPurchaseOrder(@Valid @RequestBody PurchaseOrderEntryParamDTO purchaseOrderEntryParamDTO)
			throws FinexaBussinessException {
		try {
			ClientUCCResultDTO  clientUCCResultDTO = clientTransactService.registerPurchaseOrder(purchaseOrderEntryParamDTO);
			return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","");
			}
	
	}
	
	/*********************Purchase Order Registration ***************************/
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/redeemOrder", method = RequestMethod.POST)
	public ResponseEntity<?> redeemOrder(@Valid @RequestBody ClientRedeemDTO clientRedeemDTO)
			throws FinexaBussinessException {
		try {
			ClientUCCResultDTO  clientUCCResultDTO = clientTransactService.redeemPurchaseOrder(clientRedeemDTO);
			return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","");
			}
	
	}
	
	/*********************Purchase Order Registration ***************************/
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/registerSIPOrder", method = RequestMethod.POST)
	public ResponseEntity<?> registerSIPOrder(@Valid @RequestBody SIPOrderEntryParamDTO sipOrderEntryParamDTO)
			throws FinexaBussinessException {
		try {
			ClientUCCResultDTO  clientUCCResultDTO = clientTransactService.registerSIPOrder(sipOrderEntryParamDTO);
			return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","");
			}
	}
	
	/*********************Switch Order Registration ***************************/
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/registerSwitchOrder", method = RequestMethod.POST)
	public ResponseEntity<?> registerSwitchOrder(@Valid @RequestBody ClientSwitchOrderEntryParamDTO clientSwitchOrderEntryParamDTO)
			throws FinexaBussinessException {
		try {
			ClientUCCResultDTO  clientUCCResultDTO = clientTransactService.registerSwitchOrder(clientSwitchOrderEntryParamDTO);
			return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","");
			}
	
	}
	
	/*******************SWP Registration ***************************/
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/registerAndSaveSWP", method = RequestMethod.POST)
	public ResponseEntity<?> registerAndSaveSWP(@Valid @RequestBody ClientSWPDTO clientSWPDTO)
			throws FinexaBussinessException {
		try {
			ClientUCCResultDTO  clientUCCResultDTO = clientTransactService.registerSwpOrder(clientSWPDTO);
			return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","");
			}
	
	}
	/*******************STP Registration ***************************/
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/registerAndSaveSTP", method = RequestMethod.POST)
	public ResponseEntity<?> registerAndSaveSTP(@Valid @RequestBody ClientSTPDTO clientSTPDTO)
			throws FinexaBussinessException {
		try {
			ClientUCCResultDTO  clientUCCResultDTO = clientTransactService.registerStpOrder(clientSTPDTO);
			return new ResponseEntity<ClientUCCResultDTO>(clientUCCResultDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new FinexaBussinessException("","","");
			}
	
	}

	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/viewLumpsumOrders/{clientCode}", method = RequestMethod.GET)
	public ResponseEntity<?> viewLumpsumOrders(@PathVariable String clientCode) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<LumpsumCartDTO> lumpsumCartDTOList = clientTransactService.getLumpsumOrdersFromCart(clientCode);
			return new ResponseEntity<List<LumpsumCartDTO>>(lumpsumCartDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Lumpsum Orders");
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/viewSIPOrders/{clientCode}", method = RequestMethod.GET)
	public ResponseEntity<?> viewSIPOrders(@PathVariable String clientCode) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<SIPCartDTO> sipCartDTOList = clientTransactService.getSIPOrdersFromCart(clientCode);
			return new ResponseEntity<List<SIPCartDTO>>(sipCartDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Sip Orders");
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/viewSwitchOrders/{clientCode}", method = RequestMethod.GET)
	public ResponseEntity<?> viewSwitchOrders(@PathVariable String clientCode) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<SwitchCartDTO> switchCartDTOList = clientTransactService.getSwitchOrdersFromCart(clientCode);
			return new ResponseEntity<List<SwitchCartDTO>>(switchCartDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Switch Orders");
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/viewSTPOrders/{clientCode}", method = RequestMethod.GET)
	public ResponseEntity<?> viewSTPOrders(@PathVariable String clientCode) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<STPCartDTO> stpCartDTOList = clientTransactService.getSTPOrdersFromCart(clientCode);
			return new ResponseEntity<List<STPCartDTO>>(stpCartDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Lumpsum Orders");
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/viewSWPOrders/{clientCode}", method = RequestMethod.GET)
	public ResponseEntity<?> viewSWPOrders(@PathVariable String clientCode) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<SWPCartDTO> swpCartDTOList = clientTransactService.getSWPOrdersFromCart(clientCode);
			return new ResponseEntity<List<SWPCartDTO>>(swpCartDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Lumpsum Orders");
		}
	}
	
	@PreAuthorize("hasAnyRole('AdvisorAdmin','GoalPlanningView','PortFolioManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getLastSavedProductReco/{advisorId}/{clientId}/{goalId}/{module}/{date}", method = RequestMethod.GET)
	public ResponseEntity<?> getLastSavedProductReco(@PathVariable int advisorId, @PathVariable int clientId, 
			@PathVariable int goalId, @PathVariable String module, @PathVariable String date) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<ProductRecommendationTransactDTO> productDTOList = clientTransactService.
					getLastSavedProductRecommendation(advisorId, clientId, goalId, module,date);
			return new ResponseEntity<List<ProductRecommendationTransactDTO>>(productDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Last Saved product Reco");
		}
	}
	
	@PreAuthorize("hasAnyRole('AdvisorAdmin','GoalPlanningView','PortFolioManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getLastSavedProductRecoPM/{advisorId}/{clientId}/{date}/{module}", method = RequestMethod.GET)
	public ResponseEntity<?> getLastSavedProductReco(@PathVariable int advisorId, @PathVariable int clientId, @PathVariable String date,
			@PathVariable String module) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<ProductRecommendationTransactDTO> productDTOList = clientTransactService.
					getLastSavedProductRecommendationPM(advisorId, clientId, date, module);
			return new ResponseEntity<List<ProductRecommendationTransactDTO>>(productDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Last Saved product Reco");
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/getOrdersInCart/{clientCode}", method = RequestMethod.GET)
	public ResponseEntity<?> getOrdersInCart(@PathVariable String clientCode) throws FinexaBussinessException, CustomFinexaException {
		try {
			ClientUCCResultDTO resultDTO = clientTransactService.getOrdersFromCart(clientCode);
			return new ResponseEntity<ClientUCCResultDTO>(resultDTO, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Orders From Cart");
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/placeLumpsumOrdersFromCart/{lumpsumOrders}", method = RequestMethod.GET)
	public ResponseEntity<?> placeLumpsumOrdersFromCart(@PathVariable int[] lumpsumOrders) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<CartOrderStatusDTO> cartStatusDTO = clientTransactService.placeLumpsumOrdersOfCart(lumpsumOrders);
			return new ResponseEntity<List<CartOrderStatusDTO>>(cartStatusDTO, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to Place Lumpsum Cart Orders");
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/placeSipOrdersFromCart/{sipOrders}", method = RequestMethod.GET)
	public ResponseEntity<?> placeSipOrdersFromCart(@PathVariable int[] sipOrders) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<CartOrderStatusDTO> cartStatusDTO = clientTransactService.placeSipOrdersOfCart(sipOrders);
			return new ResponseEntity<List<CartOrderStatusDTO>>(cartStatusDTO, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Sip Orders");
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/placeSwitchOrdersFromCart/{switchOrders}", method = RequestMethod.GET)
	public ResponseEntity<?> placeSwitchOrdersFromCart(@PathVariable int[] switchOrders) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<CartOrderStatusDTO> cartStatusDTO = clientTransactService.placeSwitchOrdersOfCart(switchOrders);
			return new ResponseEntity<List<CartOrderStatusDTO>>(cartStatusDTO, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Orders From Cart");
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/placeStpOrdersFromCart/{stpOrders}", method = RequestMethod.GET)
	public ResponseEntity<?> placeStpOrdersFromCart(@PathVariable int[] stpOrders) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<CartOrderStatusDTO> cartStatusDTO = clientTransactService.placeSipOrdersOfCart(stpOrders);
			return new ResponseEntity<List<CartOrderStatusDTO>>(cartStatusDTO, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Orders From Cart");
		}
	}
	
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/placeSWPOrdersFromCart/{swpOrders}", method = RequestMethod.GET)
	public ResponseEntity<?> placeSWPOrdersFromCart(@PathVariable int[] swpOrders) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<CartOrderStatusDTO> cartStatusDTO = clientTransactService.placeSwpOrdersOfCart(swpOrders);
			return new ResponseEntity<List<CartOrderStatusDTO>>(cartStatusDTO, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Orders From Cart");
		}
	}
	@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
	@RequestMapping(value = "/invest", method = RequestMethod.POST)
	public ResponseEntity<?> invest(@Valid @RequestBody List<InvestDTO> investDTO) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<ClientUCCResultDTO> resultDTOList = clientTransactService.invest(investDTO);
			return new ResponseEntity<List<ClientUCCResultDTO>>(resultDTOList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Orders From Cart");
		}
	}
			
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/getBankDetailsByIFSC/{strIfsc}", method = RequestMethod.GET)
	public ResponseEntity<?> getBankDetailsByIFSC(@PathVariable String strIfsc) throws FinexaBussinessException, CustomFinexaException {
		try {
			MasterBankNameIFSCCodeDTO  masterBankNameIFSCCodeDTO = clientTransactService.getBankDetailsByIFSC(strIfsc);
			return new ResponseEntity<MasterBankNameIFSCCodeDTO>(masterBankNameIFSCCodeDTO, HttpStatus.OK);
		} catch (RuntimeException rexp) {
					throw new FinexaBussinessException("ClientTransact Controller", "500", "Failed to get Bank Details");
		}
	}		
	@PreAuthorize("hasAnyRole('Admin','InvestView')")
	@RequestMapping(value = "/viewBankList/{clientCode}", method = RequestMethod.GET)
	public ResponseEntity<?> viewBankList(@PathVariable String clientCode) throws FinexaBussinessException, CustomFinexaException {
		try {
			List<BankNameDTO> bankList = clientTransactService.getBankList(clientCode);
			return new ResponseEntity<List<BankNameDTO>>(bankList, HttpStatus.OK);
		} catch (RuntimeException rexp) {
			throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Lumpsum Orders");
		}
	}
	
	//--------------Controller part Mandate Upload----------------------
		@PreAuthorize("hasAnyRole('Admin','InvestAddEdit')")
		@RequestMapping(value = "/clientTransact/uploadBulkMandate", method = RequestMethod.POST)
	    public ResponseEntity<?> uploadBulkMandate(@ModelAttribute MasterTransactMandateDTO masterTransactMandateDTO)
	            throws FinexaBussinessException, CustomFinexaException {
	        try {
	            UploadResponseDTO uploadResponseDTO = clientTransactUploadService.uploadBulkMandateDetails(masterTransactMandateDTO);
	            return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
	        } catch (RuntimeException e) {
	           throw new FinexaBussinessException("", "", "");
	        }
	        
	    }

		//-------------- End of Controller Part----------------------
		
	//------------------Get Mandate Ids ---------------------------//
		@PreAuthorize("hasAnyRole('Admin','InvestView')")
		@RequestMapping(value = "/getMandateId/{clientCode}/{mandateType}", method = RequestMethod.GET)
		public ResponseEntity<?> findByClientCode(@PathVariable String clientCode, @PathVariable String mandateType) throws FinexaBussinessException, CustomFinexaException {
			try {
				List<String> mandateIdList = clientTransactService.getAllMandateIDByType(mandateType, clientCode);
				return new ResponseEntity<List<String>>(mandateIdList, HttpStatus.OK);
			} catch (RuntimeException rexp) {
				throw new FinexaBussinessException("ClientTransact", "500", "Failed to get Mandate Id");
			}
		}
		
		//-------------End of Mandate Ids--------------------------//
		
		//------------------Get CKYC Details based on UCC for Edit Mode ---------------------------//
		
		@RequestMapping(value = "/getClientCKYCByUCC/{clientCode}", method = RequestMethod.GET)
		public ResponseEntity<?> getClientCKYCByUCC(@PathVariable String clientCode) throws FinexaBussinessException, CustomFinexaException {
			try {
				ClientUCCDraftModeDTO clientUCCDraftModeDTO = clientTransactService.getClientCKYCByUCC(clientCode);
				return new ResponseEntity<ClientUCCDraftModeDTO>(clientUCCDraftModeDTO, HttpStatus.OK);
			} catch (RuntimeException rexp) {
				throw new FinexaBussinessException("ClientTransact", "500", "Failed to get CKYC details");
			}
		}
				
	   //-------------End of Mandate Ids--------------------------//
		

}
