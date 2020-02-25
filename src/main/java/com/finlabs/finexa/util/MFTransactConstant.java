/**
 * 
 */
package com.finlabs.finexa.util;

/**
 * @author Debolina
 *
 */
public class MFTransactConstant {
	
	/********************Finexa Status Error Messages****************************/
	public static final String RECORD_TRANSFERRED_FROM_MASTER_TO_CLIENT = "UCC Present in BSE Master.Record Successfully Transferred";
	public static final String UCC_ALREADY_PRESENT = "This UCC is already Present";
	public static final String PATTERN_HOLDING_PRESENT = "This Pattern Holding is already present";
	public static final String ADVISOR_DATA_NOT_SAVED = "Advisor Data Not Saved";
	public static final String ADVISOR_DATA_DELETED = "Advisor Data Successfully Dleted";
	
	/******************Finexa Status Code*************************/
	public static final int STATUS_CODE_SUCCESS = 200;
	
	/*******************HTTP Connection TimeOut ***********************/
	public static final int TIME_OUT = 5000;
	
	/****************String Constants Used In MF-Transact**********************/
	
	public static final String NACH_FORM_SPONSOR_BANK_CODE = "CITI000PIGW";
	
	public static final String NACH_FORM_UTILITY_CODE = "CITI00002000000037";
	
	public static final String NACH_FORM_AGENCY_NAME = "BILLDESK";
	
	public static final String NACH_FORM_EXCHANGE_NAME = "Bse Limited";
	
	public static final String NACH_FORM_MANDATE_TYPE_XSIP = "XSIP";
	
	public static final String NACH_FORM_FILE_FORMAT_TIFF = ".tiff";
	
	public static final String NACH_FORM_FILE_FORMAT_JPEG = ".jpg";
	
	public static final String NACH_FORM_FILE_FORMAT_TIFF_VAL = "tiff";
	
	public static final String NACH_FORM_FILE_FORMAT_JPEG_VAL = "jpg";
	
	public static final String NACH_FORM_DOT_SYMBOL = ".";
	
	public static final String MANDATE_CODE_HEADING = "MANDATE CODE";
	
	public static final String CLIENT_CODE_HEADING = "CLIENT CODE";
	
	/********************* BSE URL Access Mode *************************/
	
	public static final int BSE_ACCESS_LIVE_MODE = 1;
	public static final int BSE_ACCESS_DEMO_MODE = 2;
	
	
	/******************** BSE XML File Constants ***************************/
	public static final String WSA_TO = "wsa:To";
	public static final String WSA_ACTION = "wsa:Action";
	public static final String SOAP_ENVELOPE_TAG = "soap:Envelope";
	
	public static final String UPLOAD_SERVICE_ATTRIBUTE = "xmlns:ns";
	public static final String ORDER_ENTRY_SERVICE_ATTRIBUTE = "xmlns:bses";
	
	public static final String DIRECT_SCHEME_PLAN = "DIRECT";
	public static final String NORMAL_SCHEME_PLAN = "NORMAL";
	
	public static final String ONLY_PHYSICAL = "P";
	public static final String ONLY_DEMAT = "D";
	public static final String ONLY_DEMAT_PHYSICAL = "DP";
	
}
