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
import java.util.Date;
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

import com.finlabs.finexa.dto.ClientTransactAOFDTO;
import com.finlabs.finexa.dto.ClientTransactNACHMandateDTO;
import com.finlabs.finexa.dto.ClientTransactNachDTO;
import com.finlabs.finexa.dto.ClientUCCResultDTO;
import com.finlabs.finexa.model.ClientTransactAOFDetail;
import com.finlabs.finexa.model.ClientTransactMandateDetail;
import com.finlabs.finexa.model.PurchaseOrderEntryParam;
import com.finlabs.finexa.model.XsipOrderEntryParam;
import com.finlabs.finexa.util.BSEConstant;
import com.finlabs.finexa.util.MFTransactConstant;
public class MFFileUploadService {

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
	
	public ClientUCCResultDTO authenticateMFFileUploadService (String memberId, String password, String userId, int accessMode) {
		
		String xmlFileName = BSEConstant.MF_FILE_UPLOAD_PASSWORD_REQUEST;
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String encryptedPassword = "";
		try {
			
			// write to file 
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(xmlFileName).getFile());
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			// selection between Demo and Live
			if (accessMode == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_FILE_UPLOAD);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_FILE_UPLOAD_PASSWORD);
				
				
			} else if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE){
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_FILE_UPLOAD_LIVE);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_FILE_UPLOAD_PASSWORD_LIVE);
				
			}
			Node memberIdNode = doc.getElementsByTagName("star:MemberId").item(0);
			memberIdNode.setTextContent(memberId);

			Node passwordNode = doc.getElementsByTagName("star:Password").item(0);
			passwordNode.setTextContent(password);

			Node userIdNode = doc.getElementsByTagName("star:UserId").item(0);
			userIdNode.setTextContent(userId);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			String soapUrl = BSEConstant.SOAP_URL_MF_FILE_UPLOAD;
			String SOAPAction = BSEConstant.SOAP_ACTION_MF_FILE_UPLOAD_PASSWORD;
			if(accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				soapUrl = BSEConstant.SOAP_URL_MF_FILE_UPLOAD_LIVE;
				SOAPAction = BSEConstant.SOAP_ACTION_MF_FILE_UPLOAD_PASSWORD_LIVE;
			}
			boolean status = fireAPIandWritetoFile(soapUrl,SOAPAction,BSEConstant.MF_FILE_UPLOAD_PASSWORD_REQUEST);
			
			if (status == false ) {
				clientUCCResultDTO.setStatus(false);
				clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
				clientUCCResultDTO.setMessage("Http Connection Error");
			} else {
				try {
					File targetFile = new File(classLoader.getResource(BSEConstant.MF_UPLOAD_FILE_RESPONSE).getFile());
					DocumentBuilderFactory docFactory1 = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder1 = docFactory1.newDocumentBuilder();
					Document doc1 = docBuilder1.parse(targetFile);
					Node soapEnvelope = doc1.getFirstChild();
					Node staff = doc1.getElementsByTagName("b:Status").item(0);
					String response = staff.getTextContent();
					Node responseNode = doc1.getElementsByTagName("b:ResponseString").item(0);
					if(response.equals(""+BSEConstant.BSE_STATUS_CODE_SUCCESS)) {
						encryptedPassword = responseNode.getTextContent();
						clientUCCResultDTO.setStatus(true);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_SUCCESS);
						clientUCCResultDTO.setMessage(encryptedPassword);
					} else {
						clientUCCResultDTO.setStatus(false);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
						clientUCCResultDTO.setMessage(responseNode.getTextContent());
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
			FileInputStream fin = new FileInputStream(xmlFile2Send);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			copy(fin,bout);
			fin.close();

			byte[] b = bout.toByteArray();

			// Set the appropriate HTTP parameters.
			httpConn.setRequestProperty( "Content-Length",
					String.valueOf( b.length ) );
			httpConn.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
			httpConn.setConnectTimeout(MFTransactConstant.TIME_OUT);
			httpConn.setRequestProperty("SOAPAction",sOAPAction);
			httpConn.setRequestMethod( "POST" );
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);

			// Everything's set up; send the XML that was read in to b.
			OutputStream out = httpConn.getOutputStream();
			out.write( b );    
			out.close();
			File targetFile = new File(classLoader.getResource(BSEConstant.MF_UPLOAD_FILE_RESPONSE).getFile());
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


	public ClientUCCResultDTO fireMFAOFFileUploadRequest(ClientTransactAOFDTO clientTransactAOFDTO, ClientTransactAOFDetail clientTransactAOFDetail) {
		
		String xmlFileName = BSEConstant.MF_FILE_UPLOAD_AOF_REQUEST;
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String ccFiringStatusResult = "";
		try {
			
			// write to file 
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(xmlFileName).getFile());
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			// selection between Demo and Live
			if (clientTransactAOFDetail.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_FILE_UPLOAD);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_FILE_UPLOAD_AOF);
				
				
			} else if (clientTransactAOFDetail.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE){
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_FILE_UPLOAD_LIVE);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_FILE_UPLOAD_AOF_LIVE);
				
			}
			
			Node ccFlag = doc.getElementsByTagName("star:ClientCode").item(0);
			ccFlag.setTextContent(clientTransactAOFDetail.getClientCode());

			Node docType = doc.getElementsByTagName("star:DocumentType").item(0);
			docType.setTextContent("Nrm");

			Node encryptedPasswordNode = doc.getElementsByTagName("star:EncryptedPassword").item(0);
			encryptedPasswordNode.setTextContent(clientTransactAOFDTO.getEncryptedPassword());
			
			Node fileName = doc.getElementsByTagName("star:FileName").item(0);
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			String date = sdf.format(new Date());
			String fileNameAOF = clientTransactAOFDetail.getAdvisorUser().getBseMemberId() + clientTransactAOFDetail.getClientCode() + date + ".tiff"; 
			fileName.setTextContent(fileNameAOF);
			
			Node filler1 = doc.getElementsByTagName("star:Filler1").item(0);
			filler1.setTextContent("");
			
			Node filler2 = doc.getElementsByTagName("star:Filler2").item(0);
			filler2.setTextContent("");
			
			Node flagNode = doc.getElementsByTagName("star:Flag").item(0);
			flagNode.setTextContent("Ucc");
			
			Node memberCode = doc.getElementsByTagName("star:MemberCode").item(0);
			memberCode.setTextContent(clientTransactAOFDetail.getAdvisorUser().getBseMemberId());
			
			Node userId = doc.getElementsByTagName("star:UserId").item(0);
			userId.setTextContent(clientTransactAOFDetail.getAdvisorUser().getBseUsername());
			
			Node fileBytes = doc.getElementsByTagName("star:pFileBytes").item(0);
			fileBytes.setTextContent(clientTransactAOFDTO.getFileString());
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			
			String soapUrl = BSEConstant.SOAP_URL_MF_FILE_UPLOAD;
			String SOAPAction = BSEConstant.SOAP_ACTION_MF_FILE_UPLOAD_AOF;
			
			if(clientTransactAOFDetail.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				soapUrl = BSEConstant.SOAP_URL_MF_FILE_UPLOAD_LIVE;
				SOAPAction = BSEConstant.SOAP_ACTION_MF_FILE_UPLOAD_AOF_LIVE;
			}
			boolean status = fireAPIandWritetoFile(soapUrl,SOAPAction,BSEConstant.MF_FILE_UPLOAD_AOF_REQUEST);
			
			if (status == false ) {
				clientUCCResultDTO.setStatus(false);
				clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
				clientUCCResultDTO.setMessage("Http Connection Error");
			} else {
				try {
					File targetFile = new File(classLoader.getResource(BSEConstant.MF_UPLOAD_FILE_RESPONSE).getFile());
					DocumentBuilderFactory docFactory1 = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder1 = docFactory1.newDocumentBuilder();
					Document doc1 = docBuilder1.parse(targetFile);
					Node soapEnvelope = doc1.getFirstChild();
					Node staff = doc1.getElementsByTagName("b:Status").item(0);
					String response = staff.getTextContent();
					Node responseNode = doc1.getElementsByTagName("b:ResponseString").item(0);
					if(response.equals(""+BSEConstant.BSE_STATUS_CODE_SUCCESS)) {
						ccFiringStatusResult = responseNode.getTextContent();
						clientUCCResultDTO.setStatus(true);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_SUCCESS);
						clientUCCResultDTO.setMessage(ccFiringStatusResult);
					} else {
						clientUCCResultDTO.setStatus(false);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
						clientUCCResultDTO.setMessage(responseNode.getTextContent());
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
	
public ClientUCCResultDTO fireMFNACHFileUploadRequest(ClientTransactNachDTO clientTransactNACHMandateDTO, ClientTransactMandateDetail clientTransactMandateDetail) {
		
		String xmlFileName = BSEConstant.MF_FILE_UPLOAD_NACH_REQUEST;
		ClientUCCResultDTO clientUCCResultDTO = new ClientUCCResultDTO();
		String ccFiringStatusResult = "";
		try {
			
			// write to file 
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(xmlFileName).getFile());
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			// selection between Demo and Live
			if (clientTransactMandateDetail.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_FILE_UPLOAD);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_FILE_UPLOAD_NACH);
				
				
			} else if (clientTransactMandateDetail.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE){
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_FILE_UPLOAD_LIVE);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_FILE_UPLOAD_NACH_LIVE);
				
			}
			
			Node ccFlag = doc.getElementsByTagName("star:ClientCode").item(0);
			ccFlag.setTextContent(clientTransactNACHMandateDTO.getClientCode());

			Node docType = doc.getElementsByTagName("star:EncryptedPassword").item(0);
			docType.setTextContent(clientTransactNACHMandateDTO.getEncryptedPassword());

			Node encryptedPasswordNode = doc.getElementsByTagName("star:Filler1").item(0);
			encryptedPasswordNode.setTextContent(MFTransactConstant.NACH_FORM_UTILITY_CODE);
			
			Node fileName = doc.getElementsByTagName("star:Filler2").item(0);
			fileName.setTextContent(MFTransactConstant.NACH_FORM_AGENCY_NAME);
			
			Node filler1 = doc.getElementsByTagName("star:Flag").item(0);
			filler1.setTextContent(BSEConstant.MFAPI_UCC_MFD);
			
			
			String nachFileName = clientTransactNACHMandateDTO.getFile()[0].getOriginalFilename();
			
			if(nachFileName.endsWith(MFTransactConstant.NACH_FORM_FILE_FORMAT_TIFF)) {
				Node flagNode = doc.getElementsByTagName("star:ImageType").item(0);
				flagNode.setTextContent(MFTransactConstant.NACH_FORM_FILE_FORMAT_TIFF_VAL);
				
				Node filler2 = doc.getElementsByTagName("star:ImageName").item(0);
				filler2.setTextContent(clientTransactNACHMandateDTO.getMandateId() + MFTransactConstant.NACH_FORM_FILE_FORMAT_TIFF);
				
			} else if (nachFileName.endsWith(MFTransactConstant.NACH_FORM_FILE_FORMAT_JPEG)) {
				Node flagNode = doc.getElementsByTagName("star:ImageType").item(0);
				flagNode.setTextContent(MFTransactConstant.NACH_FORM_FILE_FORMAT_JPEG_VAL);
				
				Node filler2 = doc.getElementsByTagName("star:ImageName").item(0);
				filler2.setTextContent(clientTransactNACHMandateDTO.getMandateId() + MFTransactConstant.NACH_FORM_FILE_FORMAT_JPEG);
				
			}
			
			
			Node memberCode = doc.getElementsByTagName("star:MandateId").item(0);
			memberCode.setTextContent(clientTransactMandateDetail.getMandateId());
			
			Node userId = doc.getElementsByTagName("star:MandateType").item(0);
			userId.setTextContent(MFTransactConstant.NACH_FORM_MANDATE_TYPE_XSIP);
			
			Node memberId = doc.getElementsByTagName("star:MemberCode").item(0);
			memberId.setTextContent(clientTransactMandateDetail.getAdvisorUser().getBseMemberId());
			
			Node fileBytes = doc.getElementsByTagName("star:pFileBytes").item(0);
			fileBytes.setTextContent(clientTransactNACHMandateDTO.getFileString());
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			String soapUrl = BSEConstant.SOAP_URL_MF_FILE_UPLOAD;
			String SOAPAction = BSEConstant.SOAP_ACTION_MF_FILE_UPLOAD_NACH;
			
			if(clientTransactMandateDetail.getAdvisorUser().getLookupTransactBseaccessMode().getId() == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				soapUrl = BSEConstant.SOAP_URL_MF_FILE_UPLOAD_LIVE;
				SOAPAction = BSEConstant.SOAP_ACTION_MF_FILE_UPLOAD_NACH_LIVE;
			}
			
			boolean status = fireAPIandWritetoFile(soapUrl,SOAPAction,BSEConstant.MF_FILE_UPLOAD_NACH_REQUEST);
			
			if (status == false ) {
				clientUCCResultDTO.setStatus(false);
				clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
				clientUCCResultDTO.setMessage("Http Connection Error");
			} else {
				try {
					File targetFile = new File(classLoader.getResource(BSEConstant.MF_UPLOAD_FILE_RESPONSE).getFile());
					DocumentBuilderFactory docFactory1 = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder1 = docFactory1.newDocumentBuilder();
					Document doc1 = docBuilder1.parse(targetFile);
					Node soapEnvelope = doc1.getFirstChild();
					Node staff = doc1.getElementsByTagName("b:Status").item(0);
					String response = staff.getTextContent();
					Node responseNode = doc1.getElementsByTagName("b:ResponseString").item(0);
					String responseString = responseNode.getTextContent();
					if(response.equals(""+BSEConstant.BSE_STATUS_CODE_SUCCESS)) {
						clientUCCResultDTO.setStatus(true);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_SUCCESS);
						clientUCCResultDTO.setMessage(responseString);
					} else {
						clientUCCResultDTO.setStatus(false);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
						clientUCCResultDTO.setMessage(responseString);
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
	
	
}

