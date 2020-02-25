package com.finlabs.finexa.transact;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.finlabs.finexa.dto.ClientRedeemDTO;
import com.finlabs.finexa.dto.ClientSwitchOrderEntryParamDTO;
import com.finlabs.finexa.dto.ClientUCCResultDTO;
import com.finlabs.finexa.dto.PurchaseOrderEntryParamDTO;
import com.finlabs.finexa.dto.SIPOrderEntryParamDTO;
import com.finlabs.finexa.model.ClientSwitchOrderEntryParam;
import com.finlabs.finexa.model.ClientTransactRedeemOrder;
import com.finlabs.finexa.model.PurchaseOrderEntryParam;
import com.finlabs.finexa.model.XsipOrderEntryParam;
import com.finlabs.finexa.util.BSEConstant;
import com.finlabs.finexa.util.MFTransactConstant;
public class MFOrderEntryService {

	public static void copy(InputStream in, OutputStream out) 
			throws IOException {

		// do not allow other threads to read from the
		// input or write to the output while copying is
		// taking place

		synchronized (in) {
			synchronized (out) {

				byte[] buffer = new byte[256];
				while (true) {
					int bytesRead = in.read(buffer);
					if (bytesRead == -1) break;
					out.write(buffer, 0, bytesRead);
				}
			}
		}	

	}
	
	public ClientUCCResultDTO authenticateMFOrderEntryService (String userID, String memberId, String password, int accessMode) {
		
		String xmlFileName = BSEConstant.MFORDERENTRY_PASSWORD_REQUEST;
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String encryptedPassword = "";
		try {
			
			// write to file 
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(xmlFileName).getFile());
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			Node soapEnvelope1 = doc.getElementsByTagName(MFTransactConstant.SOAP_ENVELOPE_TAG).item(0);
			NamedNodeMap nodeList = soapEnvelope1.getAttributes();
			// selection between Demo and Live
			
			if (accessMode == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_ORDER_ENTRY);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_PASSWORD);
				
				nodeList.getNamedItem(MFTransactConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE_VALUE);
				
			} else if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE){
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_ORDER_ENTRY_LIVE);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_PASSWORD_LIVE);
				
				nodeList.getNamedItem(MFTransactConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE_VALUE_LIVE);
			}
			

			Node userId = doc.getElementsByTagName("bses:UserId").item(0);
			userId.setTextContent(userID);

			Node memberIdNode = doc.getElementsByTagName("bses:Password").item(0);
			memberIdNode.setTextContent(password);

			Node passwordNode = doc.getElementsByTagName("bses:PassKey").item(0);
			passwordNode.setTextContent(memberId);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			String soapUrl = BSEConstant.SOAP_URL_MF_ORDER_ENTRY;
			String soapAction = BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_PASSWORD;
			if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				soapUrl = BSEConstant.SOAP_URL_MF_ORDER_ENTRY_LIVE;
				soapAction = BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_PASSWORD_LIVE;
			}
			boolean status = fireAPIandWritetoFile(soapUrl,soapAction,BSEConstant.MFORDERENTRY_PASSWORD_REQUEST);
			
			if (status == false ) {
				clientUCCResultDTO.setStatus(false);
				clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
				clientUCCResultDTO.setMessage("Http Connection Error");
			} else {
				try {
					File targetFile = new File(classLoader.getResource(BSEConstant.MFORDERENTRY_RESPONSE).getFile());
					DocumentBuilderFactory docFactory1 = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder1 = docFactory1.newDocumentBuilder();
					Document doc1 = docBuilder1.parse(targetFile);
					Node soapEnvelope = doc1.getFirstChild();
					Node staff = doc1.getElementsByTagName("getPasswordResult").item(0);
					String response = staff.getTextContent();
					if(response.substring(0, 3).equals(""+BSEConstant.BSE_STATUS_CODE_SUCCESS)) {
						encryptedPassword = response.substring(4);
						clientUCCResultDTO.setStatus(true);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_SUCCESS);
						clientUCCResultDTO.setMessage(encryptedPassword);
					} else {
						clientUCCResultDTO.setStatus(false);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
						clientUCCResultDTO.setMessage(response.substring(4));
					}
				} catch (Exception e) {
					clientUCCResultDTO.setStatus(false);
					clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
					clientUCCResultDTO.setMessage(e.getMessage());
				}
			}
			
		} catch (Exception e){
			clientUCCResultDTO.setStatus(false);
			clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
			clientUCCResultDTO.setMessage(e.getMessage());
		}
		return clientUCCResultDTO;
	}
	
	private boolean fireAPIandWritetoFile(String soapUrlMfApiUpload, String sOAPAction, String mfapiPasswordResponse) {
		// TODO Auto-generated method stub
		
		try {
			
			ClassLoader classLoader = getClass().getClassLoader();
			File xmlFile2Send = new File(classLoader.getResource(mfapiPasswordResponse).getFile());
			// Create the connection where we're going to send the file.
			URL url = new URL(soapUrlMfApiUpload);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) connection;
			httpConn.setConnectTimeout(MFTransactConstant.TIME_OUT);
			FileInputStream fin = new FileInputStream(xmlFile2Send);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			copy(fin,bout);
			fin.close();

			byte[] b = bout.toByteArray();

			// Set the appropriate HTTP parameters.
			httpConn.setRequestProperty( "Content-Length",
					String.valueOf( b.length ) );
			httpConn.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
			httpConn.setRequestProperty("SOAPAction",sOAPAction);
			httpConn.setRequestMethod( "POST" );
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);

			// Everything's set up; send the XML that was read in to b.
			OutputStream out = httpConn.getOutputStream();
			out.write( b );    
			out.close();
			File targetFile = new File(classLoader.getResource(BSEConstant.MFORDERENTRY_RESPONSE).getFile());
			byte[] buffer = new byte[httpConn.getInputStream().available()];
			httpConn.getInputStream().read(buffer);

			OutputStream outStream = new FileOutputStream(targetFile);
			outStream.write(buffer);
			outStream.close();
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public ClientUCCResultDTO fireMFOrderEntryRedeemRequest(ClientTransactRedeemOrder clientTransactRedeemOrder, ClientRedeemDTO redeemDTO) {
		
		String xmlFileName = BSEConstant.MFORDERENTRY_LUMPSUM_REQUEST;
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String ccFiringStatusResult = "";
		try {

			// write to file 
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(xmlFileName).getFile());
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			Node soapEnvelope1 = doc.getElementsByTagName(MFTransactConstant.SOAP_ENVELOPE_TAG).item(0);
			NamedNodeMap nodeList = soapEnvelope1.getAttributes();
			// selection between Demo and Live
			int accessMode = clientTransactRedeemOrder.getAdvisorUser().getLookupTransactBseaccessMode().getId();
			if (accessMode == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_ORDER_ENTRY);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_LUMPSUM);
				
				nodeList.getNamedItem(MFTransactConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE_VALUE);
				
			} else if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE){
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_ORDER_ENTRY_LIVE);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_LUMPSUM_LIVE);
				
				nodeList.getNamedItem(MFTransactConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE_VALUE_LIVE);
			}
			
			
			Node nsflag = doc.getElementsByTagName("bses:TransCode").item(0);
			nsflag.setTextContent(clientTransactRedeemOrder.getTransactionCode() == null ? "" : (""+clientTransactRedeemOrder.getTransactionCode()));

			Node nsUserId = doc.getElementsByTagName("bses:TransNo").item(0);
			nsUserId.setTextContent(clientTransactRedeemOrder.getUrn());

			Node encryptedPasswordNode = doc.getElementsByTagName("bses:OrderId").item(0);
			encryptedPasswordNode.setTextContent(clientTransactRedeemOrder.getOrderID() ==  null ? "" : clientTransactRedeemOrder.getOrderID().toString());

			Node param = doc.getElementsByTagName("bses:UserID").item(0);
			param.setTextContent(clientTransactRedeemOrder.getAdvisorUser().getBseUsername());

			Node paramMember = doc.getElementsByTagName("bses:MemberId").item(0);
			paramMember.setTextContent(clientTransactRedeemOrder.getAdvisorUser().getBseMemberId());

			Node paramCC = doc.getElementsByTagName("bses:ClientCode").item(0);
			paramCC.setTextContent(clientTransactRedeemOrder.getClientCode());

			/*if (purchaseOrderEntryParam.getMasterTransactBsemfdematScheme() != null) {
							Node paramSchemeCode = doc.getElementsByTagName("bses:SchemeCd").item(0);
							paramSchemeCode.setTextContent(purchaseOrderEntryParam.getMasterTransactBsemfdematScheme().getAmcSchemeCode().toString());
						} else {
							Node paramSchemeCode = doc.getElementsByTagName("bses:SchemeCd").item(0);
							paramSchemeCode.setTextContent(purchaseOrderEntryParam.getMasterTransactBsemfphysicalScheme().getAmcSchemeCode().toString());
						}*/
			if (accessMode == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
				Node paramSchemeCode = doc.getElementsByTagName("bses:SchemeCd").item(0);
				paramSchemeCode.setTextContent(clientTransactRedeemOrder.getMasterTransactBsemfphysicalScheme().getSchemeCode());
			} else {
				Node paramSchemeCode = doc.getElementsByTagName("bses:SchemeCd").item(0);
				paramSchemeCode.setTextContent(clientTransactRedeemOrder.getMasterTransactBsemfphysicalSchemeLive().getSchemeCode());
			}
			
			


			Node paramBuySell = doc.getElementsByTagName("bses:BuySell").item(0);
			paramBuySell.setTextContent(clientTransactRedeemOrder.getBuySell());

			Node paramBuySellType = doc.getElementsByTagName("bses:BuySellType").item(0);
			paramBuySellType.setTextContent(clientTransactRedeemOrder.getBuySellType());

			Node paramDpTxn = doc.getElementsByTagName("bses:DPTxn").item(0);
			paramDpTxn.setTextContent(clientTransactRedeemOrder.getDpTxn());

			Node paramOrderVal = doc.getElementsByTagName("bses:OrderVal").item(0);
			paramOrderVal.setTextContent(clientTransactRedeemOrder.getAmount() == null ? "" : clientTransactRedeemOrder.getAmount().toString());

			Node paramQty = doc.getElementsByTagName("bses:Qty").item(0);
			paramQty.setTextContent(clientTransactRedeemOrder.getQty() == null ? "" : clientTransactRedeemOrder.getQty().toString());

			Node paramAllRedem = doc.getElementsByTagName("bses:AllRedeem").item(0);
			paramAllRedem.setTextContent(clientTransactRedeemOrder.getAllRedeem());

			Node paramFolioNo = doc.getElementsByTagName("bses:FolioNo").item(0);
			paramFolioNo.setTextContent(clientTransactRedeemOrder.getFolioNo());

			Node paramRemarks = doc.getElementsByTagName("bses:Remarks").item(0);
			paramRemarks.setTextContent(clientTransactRedeemOrder.getRemarks() == null ? "" : clientTransactRedeemOrder.getRemarks());

			Node paramKYCStatus = doc.getElementsByTagName("bses:KYCStatus").item(0);
			paramKYCStatus.setTextContent(clientTransactRedeemOrder.getKycStatus());

			Node paramRefNo = doc.getElementsByTagName("bses:RefNo").item(0);
			paramRefNo.setTextContent(clientTransactRedeemOrder.getRefNo() == null ? "" : clientTransactRedeemOrder.getRefNo());

			Node paramSubBrCode = doc.getElementsByTagName("bses:SubBrCode").item(0);
			paramSubBrCode.setTextContent(clientTransactRedeemOrder.getSubBrCode() == null ? "" : clientTransactRedeemOrder.getSubBrCode());

			Node paramEUIN = doc.getElementsByTagName("bses:EUIN").item(0);
			paramEUIN.setTextContent(clientTransactRedeemOrder.getEuin());

			Node paramEUINVal = doc.getElementsByTagName("bses:EUINVal").item(0);
			paramEUINVal.setTextContent(clientTransactRedeemOrder.getEuinFlag());

			Node paramMinRedeem = doc.getElementsByTagName("bses:MinRedeem").item(0);
			paramMinRedeem.setTextContent(clientTransactRedeemOrder.getMinRedeem());

			Node paramDPC = doc.getElementsByTagName("bses:DPC").item(0);
			paramDPC.setTextContent(clientTransactRedeemOrder.getDpc());


			Node paramIpAdd = doc.getElementsByTagName("bses:IPAdd").item(0);
			paramIpAdd.setTextContent(clientTransactRedeemOrder.getIpAdd() == null ? "" : clientTransactRedeemOrder.getIpAdd());

			Node paramPassword = doc.getElementsByTagName("bses:Password").item(0);
			paramPassword.setTextContent(redeemDTO.getEncryptedPassword());

			Node paramPassKey = doc.getElementsByTagName("bses:PassKey").item(0);
			paramPassKey.setTextContent(clientTransactRedeemOrder.getAdvisorUser().getBseMemberId());

			Node paramParam1 = doc.getElementsByTagName("bses:Parma1").item(0);
			paramParam1.setTextContent("");

			Node paramParam2 = doc.getElementsByTagName("bses:Param2").item(0);
			paramParam2.setTextContent("");

			Node paramParam3 = doc.getElementsByTagName("bses:Param3").item(0);
			paramParam3.setTextContent("");


			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			
			String soapUrl = BSEConstant.SOAP_URL_MF_ORDER_ENTRY;
			String SOAPAction = BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_LUMPSUM;
			if (clientTransactRedeemOrder.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				soapUrl = BSEConstant.SOAP_URL_MF_ORDER_ENTRY_LIVE;
				SOAPAction = BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_LUMPSUM_LIVE;
			}
			boolean status = fireAPIandWritetoFile(soapUrl,SOAPAction,BSEConstant.MFORDERENTRY_LUMPSUM_REQUEST);
			
			if (status == false ) {
				clientUCCResultDTO.setStatus(false);
				clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
				clientUCCResultDTO.setMessage("Http Connection Error");
			} else {
				try {
					File targetFile = new File(classLoader.getResource(BSEConstant.MFORDERENTRY_RESPONSE).getFile());
					DocumentBuilderFactory docFactory1 = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder1 = docFactory1.newDocumentBuilder();
					Document doc1 = docBuilder1.parse(targetFile);
					Node soapEnvelope = doc1.getFirstChild();
					Node staff = doc1.getElementsByTagName("orderEntryParamResult").item(0);
					String response = staff.getTextContent();
					String responseCode = response.substring(response.length()-1, response.length());
					if(responseCode.equals(""+BSEConstant.BSE_ORDER_PLACED_SUCCESS)) {
						ccFiringStatusResult = response;
						clientUCCResultDTO.setStatus(true);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_ORDER_PLACED_SUCCESS);
						clientUCCResultDTO.setMessage(ccFiringStatusResult);
					} else {
						clientUCCResultDTO.setStatus(false);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_ORDER_PLACED_FAILURE);
						clientUCCResultDTO.setMessage(response.substring(4));
					}
				} catch (Exception e) {
					clientUCCResultDTO.setStatus(false);
					clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
					clientUCCResultDTO.setMessage(e.getMessage());
				}
			}
		} catch (Exception e){
			clientUCCResultDTO.setStatus(false);
			clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
			clientUCCResultDTO.setMessage(e.getMessage());
		}
		return clientUCCResultDTO;
	}

	public ClientUCCResultDTO fireMFOrderEntryLumpsumRequest(PurchaseOrderEntryParam purchaseOrderEntryParam, PurchaseOrderEntryParamDTO purchaseDto) {
		
		String xmlFileName = BSEConstant.MFORDERENTRY_LUMPSUM_REQUEST;
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String ccFiringStatusResult = "";
		try {
			
			// write to file 
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(xmlFileName).getFile());
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			Node soapEnvelope1 = doc.getElementsByTagName(MFTransactConstant.SOAP_ENVELOPE_TAG).item(0);
			NamedNodeMap nodeList = soapEnvelope1.getAttributes();
			// selection between Demo and Live
			int accessMode = purchaseOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId();
			if (accessMode == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_ORDER_ENTRY);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_LUMPSUM);
				
				nodeList.getNamedItem(MFTransactConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE_VALUE);
				
			} else if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE){
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_ORDER_ENTRY_LIVE);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_LUMPSUM_LIVE);
				
				nodeList.getNamedItem(MFTransactConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE_VALUE_LIVE);
			}
			Node nsflag = doc.getElementsByTagName("bses:TransCode").item(0);
			nsflag.setTextContent(purchaseOrderEntryParam.getTransactionCode() == null ? "" : (""+purchaseOrderEntryParam.getTransactionCode()));

			Node nsUserId = doc.getElementsByTagName("bses:TransNo").item(0);
			nsUserId.setTextContent(purchaseOrderEntryParam.getUrn());

			Node encryptedPasswordNode = doc.getElementsByTagName("bses:OrderId").item(0);
			encryptedPasswordNode.setTextContent(purchaseOrderEntryParam.getOrderID() ==  null ? "" : purchaseOrderEntryParam.getOrderID().toString());
			
			Node param = doc.getElementsByTagName("bses:UserID").item(0);
			param.setTextContent(purchaseOrderEntryParam.getAdvisorUser().getBseUsername());
			
			Node paramMember = doc.getElementsByTagName("bses:MemberId").item(0);
			paramMember.setTextContent(purchaseOrderEntryParam.getAdvisorUser().getBseMemberId());
			
			Node paramCC = doc.getElementsByTagName("bses:ClientCode").item(0);
			paramCC.setTextContent(purchaseOrderEntryParam.getClientCode());
			
			/*if (purchaseOrderEntryParam.getMasterTransactBsemfdematScheme() != null) {
				Node paramSchemeCode = doc.getElementsByTagName("bses:SchemeCd").item(0);
				paramSchemeCode.setTextContent(purchaseOrderEntryParam.getMasterTransactBsemfdematScheme().getAmcSchemeCode().toString());
			} else {
				Node paramSchemeCode = doc.getElementsByTagName("bses:SchemeCd").item(0);
				paramSchemeCode.setTextContent(purchaseOrderEntryParam.getMasterTransactBsemfphysicalScheme().getAmcSchemeCode().toString());
			}*/
			System.out.println("purchaseOrder ID : "+ purchaseOrderEntryParam.getId());
			try {
				if (accessMode == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
					Node paramSchemeCode = doc.getElementsByTagName("bses:SchemeCd").item(0);
					System.out.println("MF Order : " + purchaseOrderEntryParam.getMasterTransactBsemfphysicalScheme().getSchemeCode());
					paramSchemeCode.setTextContent(purchaseOrderEntryParam.getMasterTransactBsemfphysicalScheme().getSchemeCode());
				} else {
					Node paramSchemeCode = doc.getElementsByTagName("bses:SchemeCd").item(0);
					paramSchemeCode.setTextContent(purchaseOrderEntryParam.getMasterTransactBsemfphysicalSchemeLive().getSchemeCode());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				clientUCCResultDTO.setStatus(false);
				clientUCCResultDTO.setStatusCode(BSEConstant.BSE_ORDER_PLACED_FAILURE);
				if (accessMode == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
					clientUCCResultDTO.setMessage("DEMO Advisor in BSE-MF cannot have LIVE Client    ");
				} else {
					clientUCCResultDTO.setMessage("LIVE Advisor in BSE-MF cannot have DEMO Client    ");
				}
				
				return clientUCCResultDTO;
			}
			
			Node paramBuySell = doc.getElementsByTagName("bses:BuySell").item(0);
			paramBuySell.setTextContent(purchaseOrderEntryParam.getBuySell());
			
			Node paramBuySellType = doc.getElementsByTagName("bses:BuySellType").item(0);
			paramBuySellType.setTextContent(purchaseOrderEntryParam.getBuySellType());
			
			Node paramDpTxn = doc.getElementsByTagName("bses:DPTxn").item(0);
			paramDpTxn.setTextContent(purchaseOrderEntryParam.getDpTxn());
			
			Node paramOrderVal = doc.getElementsByTagName("bses:OrderVal").item(0);
			paramOrderVal.setTextContent(purchaseOrderEntryParam.getAmount() == null ? "" : purchaseOrderEntryParam.getAmount().toString());
			
			Node paramQty = doc.getElementsByTagName("bses:Qty").item(0);
			paramQty.setTextContent(purchaseOrderEntryParam.getQty() == null ? "" : purchaseOrderEntryParam.getQty().toString());
			
			Node paramAllRedem = doc.getElementsByTagName("bses:AllRedeem").item(0);
			paramAllRedem.setTextContent(purchaseOrderEntryParam.getAllRedeem());
			
			Node paramFolioNo = doc.getElementsByTagName("bses:FolioNo").item(0);
			paramFolioNo.setTextContent(purchaseOrderEntryParam.getFolioNo());
			
			Node paramRemarks = doc.getElementsByTagName("bses:Remarks").item(0);
			paramRemarks.setTextContent(purchaseOrderEntryParam.getRemarks() == null ? "" : purchaseOrderEntryParam.getRemarks());
			
			Node paramKYCStatus = doc.getElementsByTagName("bses:KYCStatus").item(0);
			paramKYCStatus.setTextContent(purchaseOrderEntryParam.getKycStatus());
			
			Node paramRefNo = doc.getElementsByTagName("bses:RefNo").item(0);
			paramRefNo.setTextContent(purchaseOrderEntryParam.getRefNo() == null ? "" : purchaseOrderEntryParam.getRefNo());
			
			Node paramSubBrCode = doc.getElementsByTagName("bses:SubBrCode").item(0);
			paramSubBrCode.setTextContent(purchaseOrderEntryParam.getSubBrCode() == null ? "" : purchaseOrderEntryParam.getSubBrCode());
			
			Node paramEUIN = doc.getElementsByTagName("bses:EUIN").item(0);
			paramEUIN.setTextContent(purchaseOrderEntryParam.getEuin());
			
			Node paramEUINVal = doc.getElementsByTagName("bses:EUINVal").item(0);
			paramEUINVal.setTextContent(purchaseOrderEntryParam.getEuinFlag());
			
			Node paramMinRedeem = doc.getElementsByTagName("bses:MinRedeem").item(0);
			paramMinRedeem.setTextContent(purchaseOrderEntryParam.getMinRedeem());
			
			Node paramDPC = doc.getElementsByTagName("bses:DPC").item(0);
			paramDPC.setTextContent(purchaseOrderEntryParam.getDpc());
			
			
			Node paramIpAdd = doc.getElementsByTagName("bses:IPAdd").item(0);
			paramIpAdd.setTextContent(purchaseOrderEntryParam.getIpAdd() == null ? "" : purchaseOrderEntryParam.getIpAdd());
			
			Node paramPassword = doc.getElementsByTagName("bses:Password").item(0);
			paramPassword.setTextContent(purchaseDto.getEncryptedPassword());
			
			Node paramPassKey = doc.getElementsByTagName("bses:PassKey").item(0);
			paramPassKey.setTextContent(purchaseOrderEntryParam.getAdvisorUser().getBseMemberId());
			
			Node paramParam1 = doc.getElementsByTagName("bses:Parma1").item(0);
			paramParam1.setTextContent("");
			
			Node paramParam2 = doc.getElementsByTagName("bses:Param2").item(0);
			paramParam2.setTextContent("");
			
			Node paramParam3 = doc.getElementsByTagName("bses:Param3").item(0);
			paramParam3.setTextContent("");
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			String soapUrl = BSEConstant.SOAP_URL_MF_ORDER_ENTRY;
			String SOAPAction = BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_LUMPSUM;
			if (purchaseOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				soapUrl = BSEConstant.SOAP_URL_MF_ORDER_ENTRY_LIVE;
				SOAPAction = BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_LUMPSUM_LIVE;
			}
			boolean status = fireAPIandWritetoFile(soapUrl,SOAPAction,BSEConstant.MFORDERENTRY_LUMPSUM_REQUEST);
			
			if (status == false ) {
				clientUCCResultDTO.setStatus(false);
				clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
				clientUCCResultDTO.setMessage("Http Connection Error");
			} else {
				try {
					File targetFile = new File(classLoader.getResource(BSEConstant.MFORDERENTRY_RESPONSE).getFile());
					DocumentBuilderFactory docFactory1 = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder1 = docFactory1.newDocumentBuilder();
					Document doc1 = docBuilder1.parse(targetFile);
					Node soapEnvelope = doc1.getFirstChild();
					Node staff = doc1.getElementsByTagName("orderEntryParamResult").item(0);
					String response = staff.getTextContent();
					String responseCode = response.substring(response.length()-1, response.length());
					if(responseCode.equals(""+BSEConstant.BSE_ORDER_PLACED_SUCCESS)) {
						ccFiringStatusResult = response;
						clientUCCResultDTO.setStatus(true);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_ORDER_PLACED_SUCCESS);
						clientUCCResultDTO.setMessage(ccFiringStatusResult);
					} else {
						clientUCCResultDTO.setStatus(false);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_ORDER_PLACED_FAILURE);
						clientUCCResultDTO.setMessage(response.substring(4));
					}
				} catch (Exception e) {
					clientUCCResultDTO.setStatus(false);
					clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
					clientUCCResultDTO.setMessage(e.getMessage());
				}
			}
		} catch (Exception e){
			clientUCCResultDTO.setStatus(false);
			clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
			clientUCCResultDTO.setMessage(e.getMessage());
		}
		return clientUCCResultDTO;
	}
	
	//------------- Start of XSIP -------------------
	
	public ClientUCCResultDTO fireMFOrderEntryXSIPRequest(XsipOrderEntryParam xSipOrderEntryParam, SIPOrderEntryParamDTO orderDTO) {
		
		String xmlFileName = BSEConstant.MFORDERENTRY_SIP_REQUEST;
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String ccFiringStatusResult = "";
		try {
			
			// write to file 
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(xmlFileName).getFile());
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			Node soapEnvelope1 = doc.getElementsByTagName(MFTransactConstant.SOAP_ENVELOPE_TAG).item(0);
			NamedNodeMap nodeList = soapEnvelope1.getAttributes();
			// selection between Demo and Live
			int accessMode = xSipOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId();
			if (accessMode == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_ORDER_ENTRY);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_SIP);
				
				nodeList.getNamedItem(MFTransactConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE_VALUE);
				
			} else if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE){
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_ORDER_ENTRY_LIVE);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_SIP_LIVE);
				
				nodeList.getNamedItem(MFTransactConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE_VALUE_LIVE);
			}
			Node transCode = doc.getElementsByTagName("bses:TransactionCode").item(0);
			transCode.setTextContent(xSipOrderEntryParam.getTransactionCode());

			Node urn = doc.getElementsByTagName("bses:UniqueRefNo").item(0);
			urn.setTextContent(xSipOrderEntryParam.getUrn());

			if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				Node schemeCd = doc.getElementsByTagName("bses:SchemeCode").item(0);
				schemeCd.setTextContent(xSipOrderEntryParam.getMasterTransactBsemfphysicalSchemeLive().getSchemeCode());
			} else {
				Node schemeCd = doc.getElementsByTagName("bses:SchemeCode").item(0);
				schemeCd.setTextContent(xSipOrderEntryParam.getMasterTransactBsemfphysicalScheme().getSchemeCode());
			}
			
			Node memberCd = doc.getElementsByTagName("bses:MemberCode").item(0);
			memberCd.setTextContent(xSipOrderEntryParam.getAdvisorUser().getBseMemberId());
			
			Node clientCd = doc.getElementsByTagName("bses:ClientCode").item(0);
			clientCd.setTextContent(xSipOrderEntryParam.getClientCode());
			
			Node userId = doc.getElementsByTagName("bses:UserId").item(0);
			userId.setTextContent(xSipOrderEntryParam.getAdvisorUser().getBseUsername());
			
			Node refNo = doc.getElementsByTagName("bses:InternalRefNo").item(0);
			refNo.setTextContent("");
			
			Node transMode = doc.getElementsByTagName("bses:TransMode").item(0);
			transMode.setTextContent(xSipOrderEntryParam.getTransMode());
			
			Node dpTxnMode = doc.getElementsByTagName("bses:DpTxnMode").item(0);
			dpTxnMode.setTextContent(xSipOrderEntryParam.getDpTransactionMode());
			
			Node startDate = doc.getElementsByTagName("bses:StartDate").item(0);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String dateInString = sdf.format(xSipOrderEntryParam.getStartDate());
			startDate.setTextContent(""+dateInString);
			
			Node freqType = doc.getElementsByTagName("bses:FrequencyType").item(0);
			freqType.setTextContent(xSipOrderEntryParam.getFrequencyType());
			
			Node freqAllowed = doc.getElementsByTagName("bses:FrequencyAllowed").item(0);
			freqAllowed.setTextContent("" + xSipOrderEntryParam.getFrequencyAllowed());
			
			Node installmentAmount = doc.getElementsByTagName("bses:InstallmentAmount").item(0);
			installmentAmount.setTextContent(xSipOrderEntryParam.getInstallmentAmount().toString());
			
			Node noOfInstallment = doc.getElementsByTagName("bses:NoOfInstallment").item(0);
			noOfInstallment.setTextContent("" + xSipOrderEntryParam.getNoOfInstallment());
			
			Node remarks = doc.getElementsByTagName("bses:Remarks").item(0);
			remarks.setTextContent(xSipOrderEntryParam.getRemarks());
			
			Node folioNo = doc.getElementsByTagName("bses:FolioNo").item(0);
			folioNo.setTextContent(xSipOrderEntryParam.getFolioNo());
			
			Node firstOrderFlag = doc.getElementsByTagName("bses:FirstOrderFlag").item(0);
			firstOrderFlag.setTextContent(xSipOrderEntryParam.getFirstOrderFlag());
			
			Node brokerage = doc.getElementsByTagName("bses:Brokerage").item(0);
			if (xSipOrderEntryParam.getBrokerage() == null) {
				brokerage.setTextContent("");
			} else {
				brokerage.setTextContent("" + xSipOrderEntryParam.getBrokerage());
			}
			
			
			Node mandateId = doc.getElementsByTagName("bses:MandateID").item(0);
			if (orderDTO.getRegType().equals("I")) {
				mandateId.setTextContent("");
			} else {
				mandateId.setTextContent("" + xSipOrderEntryParam.getXsipMandateid());
			}
			
			
			Node subBrokerCode = doc.getElementsByTagName("bses:SubberCode").item(0);
			subBrokerCode.setTextContent(xSipOrderEntryParam.getSubBrCode());
			
			Node euin = doc.getElementsByTagName("bses:Euin").item(0);
			euin.setTextContent(xSipOrderEntryParam.getEuin());
			
			Node euinVal = doc.getElementsByTagName("bses:EuinVal").item(0);
			euinVal.setTextContent(xSipOrderEntryParam.getEuinFlag());
			
			Node dpc = doc.getElementsByTagName("bses:DPC").item(0);
			dpc.setTextContent(xSipOrderEntryParam.getDpc());
			
			Node xSipRegId = doc.getElementsByTagName("bses:XsipRegID").item(0);
			if (xSipOrderEntryParam.getXsipRegId() == 0) {
				xSipRegId.setTextContent("");
			} else {
				xSipRegId.setTextContent("" + xSipOrderEntryParam.getXsipRegId());
			}
			
			
			Node ipAdd = doc.getElementsByTagName("bses:IPAdd").item(0);
			ipAdd.setTextContent(xSipOrderEntryParam.getIpAdd());
			
			Node pwd = doc.getElementsByTagName("bses:Password").item(0);
			pwd.setTextContent(orderDTO.getEncryptedPassword());
			
			Node passkey = doc.getElementsByTagName("bses:PassKey").item(0);
			passkey.setTextContent("" + xSipOrderEntryParam.getAdvisorUser().getBseMemberId());
			
			Node param1 = doc.getElementsByTagName("bses:Param1").item(0);
			param1.setTextContent("");
			
			Node param2 = doc.getElementsByTagName("bses:Param2").item(0);
			if (orderDTO.getRegType().equals("I")) {
				param2.setTextContent("" + xSipOrderEntryParam.getXsipMandateid());
			} else {
				param2.setTextContent("");
			}
			
			Node param3 = doc.getElementsByTagName("bses:Param3").item(0);
			param3.setTextContent("");
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			String soapUrl = BSEConstant.SOAP_URL_MF_ORDER_ENTRY;
			String SOAPAction = BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_SIP;
			if (xSipOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				soapUrl = BSEConstant.SOAP_URL_MF_ORDER_ENTRY_LIVE;
				SOAPAction = BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_SIP_LIVE;
			}
			boolean status = fireAPIandWritetoFile(soapUrl,SOAPAction,BSEConstant.MFORDERENTRY_SIP_REQUEST);
			
			if (status == false ) {
				clientUCCResultDTO.setStatus(false);
				clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
				clientUCCResultDTO.setMessage("Http Connection Error");
			} else {
				try {
					File targetFile = new File(classLoader.getResource(BSEConstant.MFORDERENTRY_RESPONSE).getFile());
					DocumentBuilderFactory docFactory1 = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder1 = docFactory1.newDocumentBuilder();
					Document doc1 = docBuilder1.parse(targetFile);
					Node soapEnvelope = doc1.getFirstChild();
					Node staff = doc1.getElementsByTagName("xsipOrderEntryParamResult").item(0);
					String response = staff.getTextContent();
					String responseCode = response.substring(response.length()-1, response.length());
					if(responseCode.equals(""+BSEConstant.BSE_ORDER_PLACED_SUCCESS)) {
						ccFiringStatusResult = response;
						clientUCCResultDTO.setStatus(true);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_ORDER_PLACED_SUCCESS);
						clientUCCResultDTO.setMessage(ccFiringStatusResult);
					} else {
						clientUCCResultDTO.setStatus(false);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_ORDER_PLACED_FAILURE);
						clientUCCResultDTO.setMessage(response.substring(4));
					}
				} catch (Exception e) {
					clientUCCResultDTO.setStatus(false);
					clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
					clientUCCResultDTO.setMessage(e.getMessage());
				}
			}
		} catch (Exception e){
			clientUCCResultDTO.setStatus(false);
			clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
			clientUCCResultDTO.setMessage(e.getMessage());
		}
		return clientUCCResultDTO;
	}
	
	//------------- End of XSIP -------------------	
	
	
	//------------- Start of Switch -------------------
	
	public ClientUCCResultDTO fireMFOrderEntrySwitchRequest(ClientSwitchOrderEntryParam switchOrderEntryParam, 
			ClientSwitchOrderEntryParamDTO clientSwitchOrderEntryParamDTO) {
		
		String xmlFileName = BSEConstant.MFORDERENTRY_SWITCH_REQUEST;
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String ccFiringStatusResult = "";
		try {
			
			// write to file 
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(xmlFileName).getFile());
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			Node soapEnvelope1 = doc.getElementsByTagName(MFTransactConstant.SOAP_ENVELOPE_TAG).item(0);
			NamedNodeMap nodeList = soapEnvelope1.getAttributes();
			// selection between Demo and Live
			int accessMode = switchOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId();
			if (accessMode == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_ORDER_ENTRY);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_SWITCH);
				
				nodeList.getNamedItem(MFTransactConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE_VALUE);
				
			} else if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE){
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_ORDER_ENTRY_LIVE);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_SWITCH_LIVE);
				
				nodeList.getNamedItem(MFTransactConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.ORDER_ENTRY_SERVICE_ATTRIBUTE_VALUE_LIVE);
			}
			Node transCode = doc.getElementsByTagName("bses:TransCode").item(0);
			transCode.setTextContent(""+switchOrderEntryParam.getTransCode());

			Node transNo = doc.getElementsByTagName("bses:TransNo").item(0);
			transNo.setTextContent(switchOrderEntryParam.getUrn());

			Node orderId = doc.getElementsByTagName("bses:OrderId").item(0);
			orderId.setTextContent("");
			
			Node userId = doc.getElementsByTagName("bses:UserId").item(0);
			userId.setTextContent(switchOrderEntryParam.getAdvisorUser().getBseUsername());
			
			Node memberId = doc.getElementsByTagName("bses:MemberId").item(0);
			memberId.setTextContent(switchOrderEntryParam.getAdvisorUser().getBseMemberId());
			
			Node clientCode = doc.getElementsByTagName("bses:ClientCode").item(0);
			clientCode.setTextContent(switchOrderEntryParam.getClientCode());
			
			if (accessMode == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
				Node fromSchemeCode = doc.getElementsByTagName("bses:FromSchemeCd").item(0);
				fromSchemeCode.setTextContent(switchOrderEntryParam.getMasterTransactBsemfphysicalScheme1().getSchemeCode());
				
				Node toSchemeCocde = doc.getElementsByTagName("bses:ToSchemeCd").item(0);
				toSchemeCocde.setTextContent(switchOrderEntryParam.getMasterTransactBsemfphysicalScheme2().getSchemeCode());
			} else {
				Node fromSchemeCode = doc.getElementsByTagName("bses:FromSchemeCd").item(0);
				fromSchemeCode.setTextContent(switchOrderEntryParam.getMasterTransactBsemfphysicalSchemeLive().getSchemeCode());
				
				Node toSchemeCocde = doc.getElementsByTagName("bses:ToSchemeCd").item(0);
				toSchemeCocde.setTextContent(switchOrderEntryParam.getMasterTransactBsemfTophysicalSchemeLive().getSchemeCode());
			}
			
			
			
			/*if (switchOrderEntryParam.getDpTxn().equals("P")) {
				Node fromSchemeCode = doc.getElementsByTagName("bses:FromSchemeCd").item(0);
				fromSchemeCode.setTextContent(switchOrderEntryParam.getMasterTransactBsemfphysicalScheme1().getAmcSchemeCode());
				
				Node toSchemeCocde = doc.getElementsByTagName("bses:ToSchemeCd").item(0);
				toSchemeCocde.setTextContent(switchOrderEntryParam.getMasterTransactBsemfphysicalScheme2().getAmcSchemeCode());
			} else {
				Node fromSchemeCode = doc.getElementsByTagName("bses:FromSchemeCd").item(0);
				fromSchemeCode.setTextContent(switchOrderEntryParam.getMasterTransactBsemfdematScheme1().getAmcSchemeCode());
				
				Node toSchemeCocde = doc.getElementsByTagName("bses:ToSchemeCd").item(0);
				toSchemeCocde.setTextContent(switchOrderEntryParam.getMasterTransactBsemfdematScheme2().getAmcSchemeCode());
			}*/
			
			
			Node buySell = doc.getElementsByTagName("bses:BuySell").item(0);
			buySell.setTextContent(switchOrderEntryParam.getBuySell());
			
			Node buySellType = doc.getElementsByTagName("bses:BuySellType").item(0);
			buySellType.setTextContent(switchOrderEntryParam.getBuySellType());
			
			Node dpTxn = doc.getElementsByTagName("bses:DPTxn").item(0);
			dpTxn.setTextContent(switchOrderEntryParam.getDpTxn());
			
			Node orderVal = doc.getElementsByTagName("bses:OrderVal").item(0);
			orderVal.setTextContent(switchOrderEntryParam.getAmount() == null ? "" : switchOrderEntryParam.getAmount().toString());
			
			Node units = doc.getElementsByTagName("bses:SwitchUnits").item(0);
			units.setTextContent(switchOrderEntryParam.getUnits() == null ? "" : switchOrderEntryParam.getUnits().toString());
			
			Node unitsFlag = doc.getElementsByTagName("bses:AllUnitsFlag").item(0);
			unitsFlag.setTextContent(switchOrderEntryParam.getAllUnitsFlag());
			
			Node folioNo = doc.getElementsByTagName("bses:FolioNo").item(0);
			folioNo.setTextContent(switchOrderEntryParam.getFolioNo());
			
			Node remarks = doc.getElementsByTagName("bses:Remarks").item(0);
			remarks.setTextContent(switchOrderEntryParam.getRemarks() == null ? "" : switchOrderEntryParam.getRemarks());
			
			Node kycStatus = doc.getElementsByTagName("bses:KYCStatus").item(0);
			kycStatus.setTextContent(switchOrderEntryParam.getKycStatus());
			
			Node subBrCode = doc.getElementsByTagName("bses:SubBrCode").item(0);
			subBrCode.setTextContent(switchOrderEntryParam.getSubBrokerCode() == null ? "" : switchOrderEntryParam.getSubBrokerCode());
			
			Node euin = doc.getElementsByTagName("bses:EUIN").item(0);
			euin.setTextContent(switchOrderEntryParam.getEuin());
			
			Node euinVal = doc.getElementsByTagName("bses:EUINVal").item(0);
			euinVal.setTextContent(switchOrderEntryParam.getEuinFlag());
			
			Node minRedeem = doc.getElementsByTagName("bses:MinRedeem").item(0);
			minRedeem.setTextContent(switchOrderEntryParam.getMinRedeem() == null ? "" : switchOrderEntryParam.getMinRedeem());
			
			Node ipAdd = doc.getElementsByTagName("bses:IPAdd").item(0);
			ipAdd.setTextContent(switchOrderEntryParam.getIpAddress() == null ? "" : switchOrderEntryParam.getIpAddress());
			
			Node password = doc.getElementsByTagName("bses:Password").item(0);
			password.setTextContent(clientSwitchOrderEntryParamDTO.getEncryptedPassword());
			
			Node passKey = doc.getElementsByTagName("bses:PassKey").item(0);
			passKey.setTextContent(switchOrderEntryParam.getAdvisorUser().getBseMemberId());
			
			Node param1 = doc.getElementsByTagName("bses:Parma1").item(0);
			param1.setTextContent(switchOrderEntryParam.getParam1() == null ? "" : switchOrderEntryParam.getParam1());
			
			Node param2 = doc.getElementsByTagName("bses:Param2").item(0);
			param2.setTextContent(switchOrderEntryParam.getParam2() == null ? "" : switchOrderEntryParam.getParam2());
			
			Node param3 = doc.getElementsByTagName("bses:Param3").item(0);
			param3.setTextContent(switchOrderEntryParam.getParam3() == null ? "" : switchOrderEntryParam.getParam3());
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			String soapUrl = BSEConstant.SOAP_URL_MF_ORDER_ENTRY;
			String SOAPAction = BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_SWITCH;
			if (switchOrderEntryParam.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				soapUrl = BSEConstant.SOAP_URL_MF_ORDER_ENTRY_LIVE;
				SOAPAction = BSEConstant.SOAP_ACTION_MF_ORDER_ENTRY_SWITCH_LIVE;
			}
			boolean status = fireAPIandWritetoFile(soapUrl,SOAPAction,BSEConstant.MFORDERENTRY_SWITCH_REQUEST);
			
			if (status == false ) {
				clientUCCResultDTO.setStatus(false);
				clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
				clientUCCResultDTO.setMessage("Http Connection Error");
			} else {
				try {
					File targetFile = new File(classLoader.getResource(BSEConstant.MFORDERENTRY_RESPONSE).getFile());
					DocumentBuilderFactory docFactory1 = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder1 = docFactory1.newDocumentBuilder();
					Document doc1 = docBuilder1.parse(targetFile);
					Node soapEnvelope = doc1.getFirstChild();
					Node staff = doc1.getElementsByTagName("switchOrderEntryParamResult").item(0);
					String response = staff.getTextContent();
					String responseCode = response.substring(response.length()-1, response.length());
					if(responseCode.equals(""+BSEConstant.BSE_ORDER_PLACED_SUCCESS)) {
						ccFiringStatusResult = response;
						clientUCCResultDTO.setStatus(true);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_ORDER_PLACED_SUCCESS);
						clientUCCResultDTO.setMessage(ccFiringStatusResult);
					} else {
						clientUCCResultDTO.setStatus(false);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_ORDER_PLACED_FAILURE);
						clientUCCResultDTO.setMessage(response.substring(4));
					}
				} catch (Exception e) {
					clientUCCResultDTO.setStatus(false);
					clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
					clientUCCResultDTO.setMessage(e.getMessage());
				}
			}
		} catch (Exception e){
			clientUCCResultDTO.setStatus(false);
			clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
			clientUCCResultDTO.setMessage(e.getMessage());
		}
		return clientUCCResultDTO;
	}
	
	//------------- End of Switch -------------------	
	
	
	
	
}

