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

import com.finlabs.finexa.dto.ClientUCCResultDTO;
import com.finlabs.finexa.util.BSEConstant;
import com.finlabs.finexa.util.MFTransactConstant;
public class MFUPLOADSoapService {

	
	public boolean authenticAdvisor(String userName, String memberId, String password) {

		try {
			System.out.println("**********************In Soap Cient Main **************************");
			//			URL url = this.getClass().getClassLoader().getResource("request.xml");
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("request.xml").getFile());
			//			File file = new File(url.toURI());
			System.out.println("file " + file.getAbsolutePath());
			//            String filepath = "src/main/resources/request.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);

			// Get the root element
			Node soapEnvelope = doc.getFirstChild();

			// Get the staff element , it may not working if tag has spaces, or
			// whatever weird characters in front...it's better to use
			// getElementsByTagName() to get it directly.
			// Node staff = company.getFirstChild();

			// Get the staff element by tag name directly
			Node staff = doc.getElementsByTagName("bses:UserId").item(0);
			System.out.println(staff.getNodeName());
			staff.setTextContent(userName);

			Node bsePassword = doc.getElementsByTagName("bses:Password").item(0);
			System.out.println(bsePassword.getNodeName());
			bsePassword.setTextContent(password);

			Node bsememberId = doc.getElementsByTagName("bses:PassKey").item(0);
			System.out.println(bsememberId.getNodeName());
			bsememberId.setTextContent(memberId);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			System.out.println("Done");

		} catch (Exception e) {
			System.out.println("Exception" + e.getMessage());
		}

		try {
			String SOAPUrl = "http://bsestarmfdemo.bseindia.com/MFOrderEntry/MFOrder.svc";
			ClassLoader classLoader = getClass().getClassLoader();
			File xmlFile2Send = new File(classLoader.getResource("request.xml").getFile());
			//			URL xmlFile2Send = this.getClass().getClassLoader().getResource("request.xml");
			//  String xmlFile2Send = "src/main/resources/request.xml";

			String SOAPAction = BSEConstant.SOAP_URL_MF_ORDER_ENTRY;

			// Create the connection where we're going to send the file.
			URL url = new URL(SOAPUrl);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) connection;

			// Open the input file. After we copy it to a byte array, we can see
			// how big it is so that we can set the HTTP Cotent-Length
			// property. (See complete e-mail below for more on this.)

			FileInputStream fin = new FileInputStream(xmlFile2Send);

			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			// Copy the SOAP file to the open connection.
			copy(fin,bout);
			fin.close();

			byte[] b = bout.toByteArray();

			// Set the appropriate HTTP parameters.
			httpConn.setRequestProperty( "Content-Length",
					String.valueOf( b.length ) );
			httpConn.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
			httpConn.setRequestProperty("SOAPAction",SOAPAction);
			httpConn.setRequestMethod( "POST" );
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);

			// Everything's set up; send the XML that was read in to b.
			OutputStream out = httpConn.getOutputStream();
			out.write( b );    
			out.close();

			// Read the response and write it to standard out.

			/*InputStreamReader isr =
					new InputStreamReader(httpConn.getInputStream());
			BufferedReader in = new BufferedReader(isr);

			String inputLine;

			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);

			in.close();
*/
			File targetFile = new File(classLoader.getResource("response.xml").getFile());
			byte[] buffer = new byte[httpConn.getInputStream().available()];
			httpConn.getInputStream().read(buffer);

			OutputStream outStream = new FileOutputStream(targetFile);
			outStream.write(buffer);
			outStream.close();

			try {
				//	            String filepath = "src/main/resources/response.xml";
				DocumentBuilderFactory docFactory1 = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder1 = docFactory1.newDocumentBuilder();
				Document doc1 = docBuilder1.parse(targetFile);

				// Get the root element
				Node soapEnvelope = doc1.getFirstChild();

				// Get the staff element , it may not working if tag has spaces, or
				// whatever weird characters in front...it's better to use
				// getElementsByTagName() to get it directly.
				// Node staff = company.getFirstChild();

				// Get the staff element by tag name directly
				Node staff = doc1.getElementsByTagName("getPasswordResult").item(0);
				String response = staff.getTextContent();
				//	            System.out.println(staff.getTextContent());
				if(response.substring(0, 3).equals("100")) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {

			}
		} catch (Exception e) {

		}
		return false;
	}


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
	
	public ClientUCCResultDTO authenticateMFUploadService (String userID, String memberId, String password, String passKey, int accessMode) {
		
		String xmlFileName = BSEConstant.MFAPI_PASSWORD_REQUEST;
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
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_API_UPLOAD);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_API_UPLOAD_PASSWORD);
				
				nodeList.getNamedItem(MFTransactConstant.UPLOAD_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.UPLOAD_SERVICE_ATTRIBUTE_VALUE);
				
			} else if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE){
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_API_UPLOAD_LIVE);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_API_UPLOAD_PASSWORD_LIVE);
				
				nodeList.getNamedItem(MFTransactConstant.UPLOAD_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.UPLOAD_SERVICE_ATTRIBUTE_VALUE_LIVE);
			}
			
			
			Node userId = doc.getElementsByTagName("ns:UserId").item(0);
			userId.setTextContent(userID);

			Node memberIdNode = doc.getElementsByTagName("ns:MemberId").item(0);
			memberIdNode.setTextContent(memberId);

			Node passwordNode = doc.getElementsByTagName("ns:Password").item(0);
			passwordNode.setTextContent(password);
			
			Node passKeyNode = doc.getElementsByTagName("ns:PassKey").item(0);
			passKeyNode.setTextContent(passKey);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			String soapUrl = BSEConstant.SOAP_URL_MF_API_UPLOAD;
			String SOAPAction = BSEConstant.SOAP_ACTION_MF_API_UPLOAD_PASSWORD;
			if(accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				soapUrl = BSEConstant.SOAP_URL_MF_API_UPLOAD_LIVE;
				SOAPAction = BSEConstant.SOAP_ACTION_MF_API_UPLOAD_PASSWORD_LIVE;
			}
			
			boolean status = fireAPIandWritetoFile(soapUrl,SOAPAction,BSEConstant.MFAPI_PASSWORD_REQUEST);
			
			if (status == false ) {
				clientUCCResultDTO.setStatus(false);
				clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
				clientUCCResultDTO.setMessage("Http Connection Error");
			} else {
				try {
					File targetFile = new File(classLoader.getResource(BSEConstant.MFAPI_PASSWORD_RESPONSE).getFile());
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
	/**
	 * This method takes url and action as parameter, fire the request and write to a file
	 * @param soapUrlMfApiUpload
	 * @param sOAPAction
	 * @param mfapiPasswordResponse
	 * @return
	 */
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
			File targetFile = new File(classLoader.getResource(BSEConstant.MFAPI_PASSWORD_RESPONSE).getFile());
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


	public ClientUCCResultDTO fireMFAPIRequest(String flag, String userId, String encryptedPassword,String pipeSeparatedString, int accessMode) {
		
		String xmlFileName = BSEConstant.MFAPI_REQUEST;
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
			
			if (accessMode == MFTransactConstant.BSE_ACCESS_DEMO_MODE) {
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_API_UPLOAD);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_API_UPLOAD_REQUEST);
				
				nodeList.getNamedItem(MFTransactConstant.UPLOAD_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.UPLOAD_SERVICE_ATTRIBUTE_VALUE);
				
			} else if (accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE){
				
				Node wsaTo = doc.getElementsByTagName(MFTransactConstant.WSA_TO).item(0);
				wsaTo.setTextContent(BSEConstant.SOAP_URL_MF_API_UPLOAD_LIVE);
				
				Node wsaAction = doc.getElementsByTagName(MFTransactConstant.WSA_ACTION).item(0);
				wsaAction.setTextContent(BSEConstant.SOAP_ACTION_MF_API_UPLOAD_REQUEST_LIVE);
				
				nodeList.getNamedItem(MFTransactConstant.UPLOAD_SERVICE_ATTRIBUTE).setNodeValue(BSEConstant.UPLOAD_SERVICE_ATTRIBUTE_VALUE_LIVE);
			}

			Node nsflag = doc.getElementsByTagName("ns:Flag").item(0);
			nsflag.setTextContent(""+flag);

			Node nsUserId = doc.getElementsByTagName("ns:UserId").item(0);
			nsUserId.setTextContent(userId);

			Node encryptedPasswordNode = doc.getElementsByTagName("ns:EncryptedPassword").item(0);
			encryptedPasswordNode.setTextContent(encryptedPassword);
			
			Node param = doc.getElementsByTagName("ns:param").item(0);
			param.setTextContent(pipeSeparatedString);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			String soapUrl = BSEConstant.SOAP_URL_MF_API_UPLOAD;
			String SOAPAction = BSEConstant.SOAP_ACTION_MF_API_UPLOAD_REQUEST;
			if(accessMode == MFTransactConstant.BSE_ACCESS_LIVE_MODE) {
				soapUrl = BSEConstant.SOAP_URL_MF_API_UPLOAD_LIVE;
				SOAPAction = BSEConstant.SOAP_ACTION_MF_API_UPLOAD_REQUEST_LIVE;
			}
			
			boolean status = fireAPIandWritetoFile(soapUrl,SOAPAction,BSEConstant.MFAPI_REQUEST);
			
			if (status == false ) {
				clientUCCResultDTO.setStatus(false);
				clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_FAILURE);
				clientUCCResultDTO.setMessage("Http Connection Error");
			} else {
				try {
					File targetFile = new File(classLoader.getResource(BSEConstant.MFAPI_PASSWORD_RESPONSE).getFile());
					DocumentBuilderFactory docFactory1 = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder1 = docFactory1.newDocumentBuilder();
					Document doc1 = docBuilder1.parse(targetFile);
					Node soapEnvelope = doc1.getFirstChild();
					Node staff = doc1.getElementsByTagName("MFAPIResult").item(0);
					String response = staff.getTextContent();
					if(response.substring(0, 3).equals(""+BSEConstant.BSE_STATUS_CODE_SUCCESS)) {
						ccFiringStatusResult = response.substring(4);
						clientUCCResultDTO.setStatus(true);
						clientUCCResultDTO.setStatusCode(BSEConstant.BSE_STATUS_CODE_SUCCESS);
						clientUCCResultDTO.setMessage(ccFiringStatusResult);
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
	
}

