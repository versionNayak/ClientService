/**
 * 
 */
package com.finlabs.finexa.util;

/**
 * @author Debolina
 *
 */
public class BSEConstant {
	
	public static final String MFAPI_FATCA_UPLOAD = "01";
	public static final String MFAPI_UCC_MFD = "02";
	public static final String MFAPI_PAYMENT_GATEWAY = "03";
	public static final String MFAPI_CHANGE_PASSWORD = "04";
	public static final String MFAPI_UCC_MFI = "05";
	public static final String MFAPI_MANDATE_REGISTRATION = "06";
	public static final String MFAPI_STP_REGISTRATION = "07";
	public static final String MFAPI_SWP_REGISTRATION = "08";
	public static final String MFAPI_CLIENT_ORDER_PAYMENT_STATUS = "11";
	public static final String MFAPI_CLIENT_REDEMPTION_SMS_AUTHENTICATION = "12";
	public static final String MFAPI_CLIENT_CKYC_UPLOAD = "13";
	public static final String MFAPI_MANDATE_STATUS = "14";
	public static final String MFAPI_SYSTEMATIC_PLAN_AUTHENTICATION = "15";
	
	
	public static final String SOAP_URL_MF_ORDER_ENTRY = "http://bsestarmfdemo.bseindia.com/MFOrderEntry/MFOrder.svc";
	public static final String SOAP_ACTION_MF_ORDER_ENTRY_PASSWORD = "http://bsestarmf.in/MFOrderEntry/getPassword";
	public static final String SOAP_ACTION_MF_ORDER_ENTRY_LUMPSUM = "http://bsestarmf.in/MFOrderEntry/orderEntryParam";
	public static final String SOAP_ACTION_MF_ORDER_ENTRY_SIP = "http://bsestarmf.in/MFOrderEntry/xsipOrderEntryParam";
	public static final String SOAP_ACTION_MF_ORDER_ENTRY_SWITCH = "http://bsestarmf.in/MFOrderEntry/switchOrderEntryParam";

	public static final String SOAP_URL_MF_API_UPLOAD = "http://bsestarmfdemo.bseindia.com/MFUploadService/MFUploadService.svc/Basic";
	public static final String SOAP_ACTION_MF_API_UPLOAD_PASSWORD  = "http://bsestarmfdemo.bseindia.com/2016/01/IMFUploadService/getPassword";
	public static final String SOAP_ACTION_MF_API_UPLOAD_REQUEST  = "http://bsestarmfdemo.bseindia.com/2016/01/IMFUploadService/MFAPI";

	
	public static final String SOAP_URL_MF_FILE_UPLOAD = "http://bsestarmfdemo.bseindia.com/StarMFFileUploadService/StarMFFileUploadService.svc/Basic";
	public static final String SOAP_ACTION_MF_FILE_UPLOAD_PASSWORD = "http://tempuri.org/IStarMFFileUploadService/GetPassword";
	public static final String SOAP_ACTION_MF_FILE_UPLOAD_AOF = "http://tempuri.org/IStarMFFileUploadService/UploadFile";
	public static final String SOAP_ACTION_MF_FILE_UPLOAD_NACH = "http://tempuri.org/IStarMFFileUploadService/UploadMandateScanFile";
	
	
	/****************************LIVE URLs *****************************************/
	
	public static final String SOAP_URL_MF_ORDER_ENTRY_LIVE = "http://www.bsestarmf.in/MFOrderEntry/MFOrder.svc";
	public static final String SOAP_ACTION_MF_ORDER_ENTRY_PASSWORD_LIVE = "http://bsestarmf.in/MFOrderEntry/getPassword";
	public static final String SOAP_ACTION_MF_ORDER_ENTRY_LUMPSUM_LIVE = "http://bsestarmf.in/MFOrderEntry/orderEntryParam";
	public static final String SOAP_ACTION_MF_ORDER_ENTRY_SIP_LIVE = "http://bsestarmf.in/MFOrderEntry/xsipOrderEntryParam";
	public static final String SOAP_ACTION_MF_ORDER_ENTRY_SWITCH_LIVE = "http://bsestarmf.in/MFOrderEntry/switchOrderEntryParam";

	public static final String SOAP_URL_MF_API_UPLOAD_LIVE = "http://www.bsestarmf.in/StarMFWebService/StarMFWebService.svc/Basic";
	public static final String SOAP_ACTION_MF_API_UPLOAD_PASSWORD_LIVE  = "http://www.bsestarmf.in/2016/01/IStarMFWebService/getPassword";
	public static final String SOAP_ACTION_MF_API_UPLOAD_REQUEST_LIVE  = "http://www.bsestarmf.in/2016/01/IStarMFWebService/MFAPI";

	public static final String SOAP_URL_MF_FILE_UPLOAD_LIVE = "http://www.bsestarmf.in/StarMFFileUploadService/StarMFFileUploadService.svc/Basic";
	public static final String SOAP_ACTION_MF_FILE_UPLOAD_PASSWORD_LIVE = "http://tempuri.org/IStarMFFileUploadService/GetPassword";
	public static final String SOAP_ACTION_MF_FILE_UPLOAD_AOF_LIVE = "http://tempuri.org/IStarMFFileUploadService/UploadFile";
	public static final String SOAP_ACTION_MF_FILE_UPLOAD_NACH_LIVE= "http://tempuri.org/IStarMFFileUploadService/UploadMandateScanFile";

	
	
	public static final int BSE_STATUS_CODE_SUCCESS = 100;
//	public static final int BSE_ORDER_CODE_SUCCESS = 100;
	
	public static final int BSE_ORDER_PLACED_SUCCESS = 0;
	public static final int BSE_ORDER_PLACED_FAILURE = 1;
	
	public static final int BSE_STATUS_CODE_FAILURE = 101;
	
	
	// XML Files
	public static final String MFAPI_PASSWORD_REQUEST = "bseXmlRequests/getPasswordRequestMFAPI.xml";
	public static final String MFAPI_PASSWORD_RESPONSE = "bseXmlRequests/getPasswordResponseMFAPI.xml";
	public static final String MFAPI_REQUEST = "bseXmlRequests/getMFAPIRequest.xml";
	public static final String MFAPI_RESPONSE = "bseXmlRequests/getMFAPIResponse.xml";
	
	public static final String MFORDERENTRY_PASSWORD_REQUEST = "bseXmlRequests/mfOrderEntryPasswordRequest.xml";
	public static final String MFORDERENTRY_RESPONSE = "bseXmlRequests/mfOrderEntryResponse.xml";
	public static final String MFORDERENTRY_LUMPSUM_REQUEST = "bseXmlRequests/mfOrderEntryLumpsumRequest.xml";
	public static final String MFORDERENTRY_SIP_REQUEST = "bseXmlRequests/mfOrderEntrySipRequest.xml";
	public static final String MFORDERENTRY_SWITCH_REQUEST = "bseXmlRequests/mfOrderEntrySwitchRequest.xml";
	
	public static final String MF_FILE_UPLOAD_PASSWORD_REQUEST = "bseXmlRequests/mfUploadFilePasswordRequest.xml";
	public static final String MF_FILE_UPLOAD_AOF_REQUEST = "bseXmlRequests/mfUploadFileAOFRequest.xml";
	public static final String MF_FILE_UPLOAD_NACH_REQUEST = "bseXmlRequests/mfUploadFileNachRequest.xml";
	public static final String MF_UPLOAD_FILE_RESPONSE = "bseXmlRequests/mfUploadFileResponse.xml";
	
	/***************** Soap Envelope Attribute Value ***************************/
	public static final String UPLOAD_SERVICE_ATTRIBUTE_VALUE = "http://bsestarmfdemo.bseindia.com/2016/01/";
	public static final String UPLOAD_SERVICE_ATTRIBUTE_VALUE_LIVE = "http://www.bsestarmf.in/2016/01/";
	
	// This url is same for Demo and live
	public static final String ORDER_ENTRY_SERVICE_ATTRIBUTE_VALUE = "http://bsestarmf.in/";
	public static final String ORDER_ENTRY_SERVICE_ATTRIBUTE_VALUE_LIVE = "http://bsestarmf.in/";

	
	
}
