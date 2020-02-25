/**
 * 
 */
package com.finlabs.finexa.util;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Smita Ghosh Chowdhury
 *
 */
public class FinexaBOColumnConstant {
	/****************************General Feed upload related messages********************************************************************************************/
	public static final String RTA_FILE_TYPE_CAMS = "1";
	public static final String RTA_FILE_TYPE_KARVY = "2";
	public static final String RTA_FILE_TYPE_FRANKLIN = "3";
	public static final String RTA_FILE_TYPE_SUNDARAM = "4";
	public static final Integer RTA_TYPE_CAMS = 1;
	public static final Integer RTA_TYPE_KARVY = 2;
	public static final Integer FRANKLIN_ACTIVE_SIP = 25;
	public static final Integer FRANKLIN_ACTIVE_STP = 26;
	public static final Integer FRANKLIN_CLOSED_SIP = 27;
	public static final Integer FRANKLIN_CLOSED_STP = 28;
	public static final Integer RTA_TYPE_FRANKLIN = 3;
	public static final Integer RTA_TYPE_SUNDARAM = 4;
	public static final Integer FEED_TYPE_INVM = 1;
	public static final Integer FEED_TYPE_TRM = 3;
	public static final Integer FEED_TYPE_SIPSTP = 4;
	public static final Integer FEED_TYPE_BROKM = 6;
	public static final Integer FEED_TYPE_REJM = 7;
	public static final Integer FEED_TYPE_AUMM = 8;
	public static final String RTA_CAMS = "CAMS";
	public static final String RTA_KARVY = "KARVY";
	public static final String RTA_FRANKLIN = "Franklin Templeton Investments";
	public static final String RTA_SUNDARAM = "Sundaram Mutual Fund";
	public static final String AMC_FRANKLIN = "Franklin Templeton Mutual Funds";
	public static final String AMC_SUNDARAM = "Sundaram Mutual Funds";
	public static final String CAMS_AUM22 = "22";
	public static final String CAMS_AUM53 = "53";
	public static final String STATUS_COMPLETED = "COMPLETED";
	public static final String STATUS_REJECTED = "REJECTED";
	public static final String STATUS_DONE = "DONE";
	public static final String STATUS_NOT_DONE = "NOT DONE";
	public static final String DBF_LOWER_CASE = ".dbf";
	public static final String DBF_UPPER_CASE = ".DBF";
	public static final String STATUS_NOT_APPLICABLE = "NOT APPLICABLE";
	public static final String STATUS_RUNNING = "RUNNING";
	public static final String FAILURE = "Failure";
	public static final String SUCCESS = "Success";
	public static final String BROKERAGE_RECORD_REJECTION_MESSAGE = "Transaction Date or Process Date or Transaction Number or all of the three fields were missing in each record. Row numbers are: ";
	public static final String AUM_RECORD_REJECTION_MESSAGE = "Investor Folio or Report Date or Scheme RTA Code or all of the three fields were missing in each record. Row numbers are: ";
	public static final String INVESTOR_RECORD_REJECTION_MESSAGE = "Investor Folio or PAN or both fields were missing in each record. Row numbers are: ";
	public static final String NOT_REJECTED_MESSAGE = "All records are successfully uploaded.";
	public static final String TRANSACTION_RECORD_REJECTION_MESSAGE = "Transaction Number or Transaction Type Code or both fields were missing in each record. Row numbers are: ";
	public static final String SIPSTP_RECORD_REJECTION_MESSAGE = "Transaction Number was missing in each record. Row numbers are: ";
	public static final String REJECTION_RECORD_REJECTION_MESSAGE = "Transaction Number field was missing in each record. Row numbers are: ";
	public static final String FILE_REJECTION_MESSAGE = "One or more columns were not found in file.";
	public static final String FILE_NOT_PROCESSED_MESSAGE = "Could not process the file.";
	public static final String GENERAL_MESSAGE = "Reason for rest rows are same as this.";
	public static final String FAILED_TO_COMPLETE = "Failed to Complete the Operation";
	public static final String UPLOAD_SUCCESSFUL = "File Upload Successfull";
	public static final String UPLOAD_FAILED = "File Upload Failed";
	/********************AUM Columns ***********************************************************************************************************************************************/
	/*********************CAMS WBR_22_Excel*************************/
	public static final String AUM_CAMS_EXCEL_SCHEME_RTA_CODE =	"product";
	//public static final String	AUM_CAMS_EXCEL_AMC_CODE =			
	public static final String	AUM_CAMS_EXCEL_FOLIO_NUMBER = "folio";
	public static final String	AUM_CAMS_EXCEL_SCHEME_NAME = "scheme_name";
	public static final String	AUM_CAMS_EXCEL_UNIT_BALANCE = "units";
	public static final String	AUM_CAMS_EXCEL_DISTRIBUTOR_ARN_CODE = "brok_dlr_code";
	public static final String	AUM_CAMS_EXCEL_SUB_BROKER_CODE = "ae_code";
	public static final String	AUM_CAMS_EXCEL_INVESTOR_NAME = "inv_name";
	//public static final String	AUM_CAMS_EXCEL_PINCODE =			
	public static final String	AUM_CAMS_EXCEL_CURRENT_VALUE = "closing_assets";
	public static final String	AUM_CAMS_EXCEL_NAV = "nav";
	public static final String	AUM_CAMS_EXCEL_REPORT_DATE = "asset_date";
	
	/*********************Karvy MFSD_243_Excel*************************/
	public static final String AUM_KARVY_EXCEL_SCHEME_RTA_CODE = "Product Code";
	public static final String	AUM_KARVY_EXCEL_AMC_CODE = "Fund";			
	public static final String	AUM_KARVY_EXCEL_FOLIO_NUMBER = "Folio Number";
	public static final String	AUM_KARVY_EXCEL_SCHEME_NAME = "Fund Description";
	public static final String	AUM_KARVY_EXCEL_UNIT_BALANCE = "Balance";
	public static final String	AUM_KARVY_EXCEL_DISTRIBUTOR_ARN_CODE = "Agent Code";
	public static final String	AUM_KARVY_EXCEL_SUB_BROKER_CODE = "Broker Code";
	public static final String	AUM_KARVY_EXCEL_INVESTOR_NAME = "Investor Name";
	public static final String	AUM_KARVY_EXCEL_PINCODE =	"Pincode";		
	public static final String	AUM_KARVY_EXCEL_CURRENT_VALUE = "AUM";
	public static final String	AUM_KARVY_EXCEL_NAV = "NAV";
	public static final String	AUM_KARVY_EXCEL_REPORT_DATE = "Report Date";	
	
	/*********************Franklin Client WIse AUM_Excel*************************/
	public static final String AUM_FRANKLIN_EXCEL_SCHEME_RTA_CODE =	"PRODCODE";
	//public static final String	AUM_FRANKLIN_EXCEL_AMC_CODE = "";			
	public static final String	AUM_FRANKLIN_EXCEL_FOLIO_NUMBER = "ACCNO";
	public static final String	AUM_FRANKLIN_EXCEL_SCHEME_NAME = "SCHEME";
	public static final String	AUM_FRANKLIN_EXCEL_UNIT_BALANCE = "UNITBALAN2";
	public static final String	AUM_FRANKLIN_EXCEL_DISTRIBUTOR_ARN_CODE = "BROKERCODE";
	public static final String	AUM_FRANKLIN_EXCEL_SUB_BROKER_CODE = "SUBBROKER0";
	public static final String	AUM_FRANKLIN_EXCEL_INVESTOR_NAME = "INVNAME";
	public static final String	AUM_FRANKLIN_EXCEL_PINCODE =	"INVPIN";		
	public static final String	AUM_FRANKLIN_EXCEL_CURRENT_VALUE = "CURVALUE";
	public static final String	AUM_FRANKLIN_EXCEL_NAV = "NAV";
	public static final String	AUM_FRANKLIN_EXCEL_REPORT_DATE = "REPDATE";

	/*XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX*/
	
	/*********************Franklin Client WIse AUM_DBF*************************/
	public static final String  AUM_FRANKLIN_DBF_SCHEME_RTA_CODE = "PRODCODE";
	//public static final String	AUM_FRANKLIN_DBF_AMC_CODE = "FUND";			
	public static final String	AUM_FRANKLIN_DBF_FOLIO_NUMBER = "ACCNO";
	public static final String	AUM_FRANKLIN_DBF_SCHEME_NAME = "SCHEME";
	public static final String	AUM_FRANKLIN_DBF_UNIT_BALANCE = "UNITBALAN2";
	public static final String	AUM_FRANKLIN_DBF_DISTRIBUTOR_ARN_CODE = "BROKERCODE";
	public static final String	AUM_FRANKLIN_DBF_SUB_BROKER_CODE = "SUBBROKER0";
	public static final String	AUM_FRANKLIN_DBF_INVESTOR_NAME = "INVNAME";
	public static final String	AUM_FRANKLIN_DBF_PINCODE =	"INVPIN";		
	public static final String	AUM_FRANKLIN_DBF_CURRENT_VALUE = "CURVALUE";
	public static final String	AUM_FRANKLIN_DBF_NAV = "NAV";
	public static final String	AUM_FRANKLIN_DBF_REPORT_DATE = "REPDATE";
	
	/*********************AUM_CAMS_DBF*************************/
	public static final String  AUM_CAMS_DBF_SCHEME_RTA_CODE = "PRODUCT";
	//public static final String	AUM_CAMS_DBF_AMC_CODE = "FUND";			
	public static final String	AUM_CAMS_DBF_FOLIO_NUMBER = "FOLIO";
	public static final String	AUM_CAMS_DBF_SCHEME_NAME = "SCHEME_NAM";
	public static final String	AUM_CAMS_DBF_UNIT_BALANCE = "UNITS";
	public static final String	AUM_CAMS_DBF_DISTRIBUTOR_ARN_CODE = "BROK_DLR_C";
	public static final String	AUM_CAMS_DBF_SUB_BROKER_CODE = "AE_CODE";
	public static final String	AUM_CAMS_DBF_INVESTOR_NAME = "INV_NAME";
	//public static final String	AUM_CAMS_DBF_PINCODE =	"INV_PIN";		
	public static final String	AUM_CAMS_DBF_CURRENT_VALUE = "CLOSING_AS";
	public static final String	AUM_CAMS_DBF_NAV = "NAV";
	public static final String	AUM_CAMS_DBF_REPORT_DATE = "ASSET_DATE";
	
	/*********************AUM_Karvy_DBF*************************/
	public static final String  AUM_KARVY_DBF_SCHEME_RTA_CODE = "PRCODE";
	public static final String	AUM_KARVY_DBF_AMC_CODE = "FUND";			
	public static final String	AUM_KARVY_DBF_FOLIO_NUMBER = "ACNO";
	public static final String	AUM_KARVY_DBF_SCHEME_NAME = "FUNDDESC";
	public static final String	AUM_KARVY_DBF_UNIT_BALANCE = "BALUNITS";
	public static final String	AUM_KARVY_DBF_DISTRIBUTOR_ARN_CODE = "BROKCODE";
	public static final String	AUM_KARVY_DBF_SUB_BROKER_CODE = "SBCODE";
	public static final String	AUM_KARVY_DBF_INVESTOR_NAME = "INVNAME";
	public static final String	AUM_KARVY_DBF_PINCODE =	"INV_PIN";		
	public static final String	AUM_KARVY_DBF_CURRENT_VALUE = "VALINV";
	public static final String	AUM_KARVY_DBF_NAV = "LNAV";
	public static final String	AUM_KARVY_DBF_REPORT_DATE = "CRDATE";
	/*********************AUM_Sundaram_DBF*************************/
	public static final String  AUM_SUNDARAM_DBF_SCHEME_RTA_CODE = "PRODCODE";
	public static final String	AUM_SUNDARAM_DBF_AMC_CODE = "FUND";			
	public static final String	AUM_SUNDARAM_DBF_FOLIO_NUMBER = "FOLIO";
	public static final String	AUM_SUNDARAM_DBF_SCHEME_NAME = "PRODNAME";
	public static final String	AUM_SUNDARAM_DBF_UNIT_BALANCE = "UNITS";
	public static final String	AUM_SUNDARAM_DBF_DISTRIBUTOR_ARN_CODE = "BROKCODE";
	//public static final String	AUM_Sundaram_DBF_SUB_BROKER_CODE = "SBCODE";
	public static final String	AUM_SUNDARAM_DBF_INVESTOR_NAME = "INVNAME";
	//public static final String	AUM_Sundaram_DBF_PINCODE =	"INV_PIN";		
	public static final String	AUM_SUNDARAM_DBF_CURRENT_VALUE = "CLGASSETS";
	public static final String	AUM_SUNDARAM_DBF_NAV = "NAV";
	public static final String	AUM_SUNDARAM_DBF_REPORT_DATE = "REPDATE";

	/********************SIPSTP Columns *****************************************************************************************************************************************/
	/*********************SIPSTP_Karvy_DBF*************************/
	public static final String	SIPSTP_DBF_KARVY_SCHEME_RTA_CODE = "PRODCODE";
	public static final String	SIPSTP_DBF_KARVY_SCHEME_NAME = "SCHEMENAME";
	public static final String	SIPSTP_DBF_KARVY_FOLIO_NUMBER = "FOLIO";
	public static final String	SIPSTP_DBF_KARVY_INVESTOR_NAME = "INVNAME";
	public static final String	SIPSTP_DBF_KARVY_TRANSACTION_TYPE = "TRTYPE";
	public static final String	SIPSTP_DBF_KARVY_TRANSACTION_NUMBER = "IHNO";
	public static final String	SIPSTP_DBF_KARVY_AMOUNT = "AMOUNT";
	public static final String	SIPSTP_DBF_KARVY_FROM_DATE = "STARTDATE";
	public static final String	SIPSTP_DBF_KARVY_TO_DATE = "ENDDATE";
	public static final String	SIPSTP_DBF_KARVY_TERMINATION_DATE = "TERMINATED";
	public static final String	SIPSTP_DBF_KARVY_FREQUENCY = "FREQUENCY";
	public static final String	SIPSTP_DBF_KARVY_TARGET_SCHEME = "TOSCHEME";
	public static final String	SIPSTP_DBF_KARVY_REGISTRATION_DATE = "REGDATE";
	public static final String	SIPSTP_DBF_KARVY_SUB_BROKER_CODE = "SUBBROKER";
	public static final String	SIPSTP_DBF_KARVY_INVESTOR_PAN = "PAN";
	public static final String	SIPSTP_DBF_KARVY_LOCATION_CATEGORY = "LOCATION";
	public static final String	SIPSTP_DBF_KARVY_AMC_CODE = "FUNDCODE";
	//public static final String	SIPSTP_DBF_KARVY_REMARKS = "";
	
	/*********************SIPSTP_CAMS_DBF*************************/
	public static final String	SIPSTP_DBF_CAMS_SCHEME_RTA_CODE = "";
	public static final String	SIPSTP_DBF_CAMS_SCHEME_NAME = "";
	public static final String	SIPSTP_DBF_CAMS_FOLIO_NUMBER = "";
	public static final String	SIPSTP_DBF_CAMS_INVESTOR_NAME = "";
	public static final String	SIPSTP_DBF_CAMS_TRANSACTION_TYPE = "";
	public static final String	SIPSTP_DBF_CAMS_TRANSACTION_NUMBER = "";
	public static final String	SIPSTP_DBF_CAMS_AMOUNT = "";
	public static final String	SIPSTP_DBF_CAMS_FROM_DATE = "";
	public static final String	SIPSTP_DBF_CAMS_TO_DATE = "";
	public static final String	SIPSTP_DBF_CAMS_TERMINATION_DATE = "";
	public static final String	SIPSTP_DBF_CAMS_FREQUENCY = "";
	public static final String	SIPSTP_DBF_CAMS_TARGET_SCHEME = "";
	public static final String	SIPSTP_DBF_CAMS_REGISTRATION_DATE = "";
	public static final String	SIPSTP_DBF_CAMS_SUB_BROKER_CODE = "";
	public static final String	SIPSTP_DBF_CAMS_INVESTOR_PAN = "";
	public static final String	SIPSTP_DBF_CAMS_LOCATION_CATEGORY = "";
	public static final String	SIPSTP_DBF_CAMS_AMC_CODE = "";
	//public static final String	SIPSTP_DBF_KARVY_REMARKS = "";
	
	/*********************SIP_Franklin_DBF*************************/
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_SCHEME_RTA_CODE = "PRODUCT_12";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_SCHEME_NAME = "SCHEME_NA3";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_FOLIO_NUMBER = "FOLIO_ID";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_INVESTOR_NAME = "INVESTOR_7";
	//public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TRANSACTION_TYPE = "TRTYPE";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TRANSACTION_NUMBER = "SIP_TXN_NO";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_AMOUNT = "AMOUNT";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_FROM_DATE = "START_DATE";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TO_DATE = "END_DATE";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TERMINATION_DATE = "SIP_CANC13";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_FREQUENCY = "FREQUENCY";
	//public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TARGET_SCHEME = "TOSCHEME";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_REGISTRATION_DATE = "SIP_REG_14";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_SUB_BROKER_CODE = "SUB_ARN";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_INVESTOR_PAN = "IT_PAN_NO";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_LOCATION_CATEGORY = "LOCATION16";
	//public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_AMC_CODE = "FUNDCODE";
	//public static final String	SIPSTP_DBF_KARVY_REMARKS = "";
	
	/*********************STP_Franklin_DBF*************************/
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_SCHEME_RTA_CODE = "PRODUCT_17";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_SCHEME_NAME = "SSCHEME_N4";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_FOLIO_NUMBER = "FOLIO_ID";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_INVESTOR_NAME = "INVESTOR12";
	//public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TRANSACTION_TYPE = "TRTYPE";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_TRANSACTION_NUMBER = "TRAN_NO";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_AMOUNT = "AMOUNT";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_FROM_DATE = "START_DATE";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_TO_DATE = "END_DATE";
	//public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TERMINATION_DATE = "SIP_CANC_DATE";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_FREQUENCY = "FREQUENCY";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_TARGET_SCHEME = "DSCHEME_N8";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_REGISTRATION_DATE = "STP_REG_18";
	//public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_SUB_BROKER_CODE = "SUB_ARN";
	public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_INVESTOR_PAN = "IT_PAN_NO";
	//public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_STP_LOCATION_CATEGORY = "LOCATION_16";
	//public static final String	SIPSTP_DBF_FRANKLIN_ACTIVE_SIP_AMC_CODE = "FUNDCODE";
	//public static final String	SIPSTP_DBF_KARVY_REMARKS = "";
	
	/*********************CLOSED SIP_Franklin_DBF*************************/
	public static final String	SIPSTP_DBF_FRANKLIN_CLOSED_SIP_TRANSACTION_NUMBER = "SNO";
	public static final String	SIPSTP_DBF_FRANKLIN_CLOSED_SIP_REMARKS = "STOP_REAS6";

	/*XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX*/
	
	/*********************SIPSTP_CAMS_EXCEL*************************/
	public static final String SIPSTP_CAMS_EXCEL_SCHEME_RTA_CODE = "PRODUCT";
	public static final String SIPSTP_CAMS_EXCEL_SCHEME_NAME = "SCHEME";
	public static final String SIPSTP_CAMS_EXCEL_FOLIO_NUMBER = "FOLIO_NO";
	public static final String SIPSTP_CAMS_EXCEL_INVESTOR_NAME = "INV_NAME";
	public static final String SIPSTP_CAMS_EXCEL_TRANSACTION_TYPE ="AUT_TRNTYP";
	public static final String SIPSTP_CAMS_EXCEL_TRANSACTION_NUMBER ="AUTO_TRNO";
	public static final String SIPSTP_CAMS_EXCEL_AMOUNT ="AUTO_AMOUN";
	public static final String SIPSTP_CAMS_EXCEL_FROM_DATE ="FROM_DATE";
	public static final String SIPSTP_CAMS_EXCEL_TO_DATE ="TO_DATE";
	public static final String SIPSTP_CAMS_EXCEL_TERMINATION_DATE = "CEASE_DATE";
	public static final String SIPSTP_CAMS_EXCEL_FREQUENCY="PERIODICIT";
	public static final String SIPSTP_CAMS_EXCEL_TARGET_SCHEME="TARGET_SCH";
	public static final String SIPSTP_CAMS_EXCEL_REGISTRATION_DATE="REG_DATE";
	public static final String SIPSTP_CAMS_EXCEL_SUB_BROKER_CODE="SUBBROKER";
	public static final String SIPSTP_CAMS_EXCEL_INVESTOR_PAN="PAN";
	public static final String SIPSTP_CAMS_EXCEL_LOCATION_CATEGORY="TER_LOCATI";
	public static final String SIPSTP_CAMS_EXCEL_AMC_CODE="AMC_CODE";
	
	/*********************SIPSTP_Karvy_EXCEL*************************/
	public static final String SIPSTP_KARVY_EXCEL_SCHEME_RTA_CODE = "PRODCODE";
	public static final String SIPSTP_KARVY_EXCEL_SCHEME_NAME = "SCHEMENAME";
	public static final String SIPSTP_KARVY_EXCEL_FOLIO_NUMBER = "FOLIO";
	public static final String SIPSTP_KARVY_EXCEL_INVESTOR_NAME = "INVNAME";
	public static final String SIPSTP_KARVY_EXCEL_TRANSACTION_TYPE ="TRTYPE";
	public static final String SIPSTP_KARVY_EXCEL_TRANSACTION_NUMBER ="IHNO";
	public static final String SIPSTP_KARVY_EXCEL_AMOUNT ="AMOUNT";
	public static final String SIPSTP_KARVY_EXCEL_FROM_DATE ="STARTDATE";
	public static final String SIPSTP_KARVY_EXCEL_TO_DATE ="ENDDATE";
	public static final String SIPSTP_KARVY_EXCEL_TERMINATION_DATE = "TERMINATED";
	public static final String SIPSTP_KARVY_EXCEL_FREQUENCY="FREQUENCY";
	public static final String SIPSTP_KARVY_EXCEL_TARGET_SCHEME="TOSCHEME";
	public static final String SIPSTP_KARVY_EXCEL_REGISTRATION_DATE="REGDATE";
	public static final String SIPSTP_KARVY_EXCEL_SUB_BROKER_CODE="SUBBROKER";
	public static final String SIPSTP_KARVY_EXCEL_INVESTOR_PAN="PAN";
	public static final String SIPSTP_KARVY_EXCEL_LOCATION_CATEGORY="LOCATION";
	public static final String SIPSTP_KARVY_EXCEL_AMC_CODE="FUNDCODE";
	
	/*********************SIP_Franklin_EXCEL*************************/
	public static final String SIP_FRANKLIN_EXCEL_SCHEME_RTA_CODE = "PRODUCT_CODE";
	public static final String SIP_FRANKLIN_EXCEL_SCHEME_NAME = "SCHEME_NAME";
	public static final String SIP_FRANKLIN_EXCEL_FOLIO_NUMBER = "FOLIO_ID";
	public static final String SIP_FRANKLIN_EXCEL_INVESTOR_NAME = "INVESTOR_NAME";
	public static final String SIP_FRANKLIN_EXCEL_ACOUNT ="BANK_NAME";
	public static final String SIP_FRANKLIN_EXCEL_TRANSACTION_NUMBER ="SIP_TXN_NO";
	public static final String SIP_FRANKLIN_EXCEL_AMOUNT ="AMOUNT";
	public static final String SIP_FRANKLIN_EXCEL_FROM_DATE ="START_DATE";
	public static final String SIP_FRANKLIN_EXCEL_TO_DATE ="END_DATE";
	public static final String SIP_FRANKLIN_EXCEL_TERMINATION_DATE = "SIP_CANC_DATE";
	public static final String SIP_FRANKLIN_EXCEL_FREQUENCY="FREQUENCY";
	public static final String SIP_FRANKLIN_EXCEL_TARGET_SCHEME="DSCHEME_NAME";
	public static final String SIP_FRANKLIN_EXCEL_REGISTRATION_DATE="SIP_REG_DATE";
	public static final String SIP_FRANKLIN_EXCEL_SUB_BROKER_CODE="SUB_ARN";
	public static final String SIP_FRANKLIN_EXCEL_INVESTOR_PAN="IT_PAN_NO";
	public static final String SIP_FRANKLIN_EXCEL_LOCATION_CATEGORY="LOCATION_FLAG";
	public static final String SIP_FRANKLIN_EXCEL_ARN_CODE="EUIN";
	
	/*********************STP_Franklin_EXCEL*************************/
	public static final String STP_FRANKLIN_EXCEL_SCHEME_NAME = "SSCHEME_NAME";
	public static final String STP_FRANKLIN_EXCEL_FOLIO_NUMBER = "FOLIO_ID";
	public static final String STP_FRANKLIN_EXCEL_INVESTOR_NAME = "INVESTOR_NAME";
	public static final String STP_FRANKLIN_EXCEL_TRANSACTION_NUMBER ="TRAN_NO";
	public static final String STP_FRANKLIN_EXCEL_AMOUNT ="AMOUNT";
	public static final String STP_FRANKLIN_EXCEL_FROM_DATE ="START_DATE";
	public static final String STP_FRANKLIN_EXCEL_TO_DATE ="END_DATE";
	public static final String STP_FRANKLIN_EXCEL_FREQUENCY="FREQUENCY";
	public static final String STP_FRANKLIN_EXCEL_TARGET_SCHEME="DSCHEME_NAME";

	/*********************CLOSED SIP_Franklin_EXCEL*************************/
	public static final String SIP_FRANKLIN_EXCEL_REJECT_TRANSACTION_NUMBER="SNO";
	public static final String SIP_FRANKLIN_EXCEL_REJECT_REMARKS="Stop_Reason";

	/*********************CLOSED STP_Franklin_EXCEL*************************/
	public static final String STP_FRANKLIN_EXCEL_REJECT_TRANSACTION_NUMBER="SI NO";
	public static final String STP_FRANKLIN_EXCEL_REJECT_REMARKS="Stop_Reason";
	/********************Transaction Columns ***************************************************************************************************************************************/
	/*********************CAMS WBR_2_Excel*************************/
	public static final String	TRANSACTION_EXCEL_CAMS_AMC_CODE	 = 	"amc_code";
	public static final String	TRANSACTION_EXCEL_CAMS_FOLIO_NUMBER = 	"folio_no";
	public static final String	TRANSACTION_EXCEL_CAMS_SCHEME_RTA_CODE	 = 	"prodcode";
	public static final String	TRANSACTION_EXCEL_CAMS_SCHEME_NAME	 = 	"scheme";
	public static final String	TRANSACTION_EXCEL_CAMS_INVESTOR_NAME	 = 	"inv_name";
	public static final String	TRANSACTION_EXCEL_CAMS_TRANSACTION_TYPE_CODE	 = 	"trxntype";
	public static final String	TRANSACTION_EXCEL_CAMS_TRANSACTION_NUMBER	 = 	"trxnno";
	public static final String	TRANSACTION_EXCEL_CAMS_TRANSACTION_MODE	 = 	"trxnmode";
	public static final String	TRANSACTION_EXCEL_CAMS_TRANSACTION_STATUS	 = 	"trxnstat";
	public static final String	TRANSACTION_EXCEL_CAMS_TRANSACTION_DATE	 = 	"traddate";
	public static final String	TRANSACTION_EXCEL_CAMS_PROCESS_DATE	 = 	"postdate";
	public static final String	TRANSACTION_EXCEL_CAMS_NAV	 = 	"purprice";
	public static final String	TRANSACTION_EXCEL_CAMS_UNITS_OF_THE_TRANSACTION	 = 	"units";
	public static final String	TRANSACTION_EXCEL_CAMS_AMOUNT_OF_THE_TRANSACTION	 = 	"amount";
	public static final String	TRANSACTION_EXCEL_CAMS_DISTRIBUTOR_ARN_CODE	 = 	"brokcode";
	public static final String	TRANSACTION_EXCEL_CAMS_SUB_BROKER_CODE	 = 	"subbrok";
	public static final String	TRANSACTION_EXCEL_CAMS_BROKERAGE_PERCENTAGE	 = 	"brokperc";
	public static final String	TRANSACTION_EXCEL_CAMS_BROKERAGE_AMOUNT	 = 	"brokcomm";
	public static final String	TRANSACTION_EXCEL_CAMS_INVESTOR_ID	 = 	"altfolio";
	public static final String	TRANSACTION_EXCEL_CAMS_REPORT_DATE	 = 	"rep_date";
	public static final String	TRANSACTION_EXCEL_CAMS_REPORT_TIME	 = 	"time1";
	public static final String	TRANSACTION_EXCEL_CAMS_APPLICATION_NUMBER	 = 	"application_no";
	public static final String	TRANSACTION_EXCEL_CAMS_TRANS_TYPE	 = 	"trxn_nature";
	public static final String	TRANSACTION_EXCEL_CAMS_TDS_AMOUNT	 = 	"total_tax";
	public static final String	TRANSACTION_EXCEL_CAMS_SECURITY_TRANSACTION_TAX	 = 	"stt";
	public static final String	TRANSACTION_EXCEL_CAMS_INVESTOR_PAN	 = 	"pan";
	public static final String	TRANSACTION_EXCEL_CAMS_TXN_TYPE	 = 	"trxn_type_flag";
	public static final String	TRANSACTION_EXCEL_CAMS_TRANSACTION_CHARGES	 = 	"trxn_charges";
	public static final String	TRANSACTION_EXCEL_CAMS_TRANSACTION_SUFFIX	 = 	"trxn_suffix";
	public static final String	TRANSACTION_EXCEL_CAMS_LOCATION_CATEGORY	 = 	"ter_location";
	public static final String	TRANSACTION_EXCEL_CAMS_EUIN	 = 	"euin";
	public static final String	TRANSACTION_EXCEL_CAMS_EUIN_VALID_INDICATOR	 = 	"euin_valid";
	public static final String	TRANSACTION_EXCEL_CAMS_SUB_BROKER_ARN	 = 	"sub_brk_arn";
	public static final String	TRANSACTION_EXCEL_CAMS_BANK_NAME	 = 	"bank_name";
	
	/*********************Karvy MFSD201_Excel*************************/
	public static final String	TRANSACTION_EXCEL_KARVY_AMC_CODE	 = 	"Fund";
	public static final String	TRANSACTION_EXCEL_KARVY_FOLIO_NUMBER	 = 	"Folio Number";
	public static final String	TRANSACTION_EXCEL_KARVY_SCHEME_RTA_CODE	 = 	"Product Code";
	public static final String	TRANSACTION_EXCEL_KARVY_SCHEME_NAME	 = 	"Fund Description";
	public static final String	TRANSACTION_EXCEL_KARVY_INVESTOR_NAME	 = 	"Investor Name";
	public static final String	TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE_CODE	 = 	"Transaction Flag";
	public static final String	TRANSACTION_EXCEL_KARVY_TRANSACTION_NUMBER	 = 	"Transaction Number";
	public static final String	TRANSACTION_EXCEL_KARVY_TRANSACTION_MODE	 = 	"Transaction Mode";
	public static final String	TRANSACTION_EXCEL_KARVY_TRANSACTION_STATUS	 = 	"Transaction Status";
	public static final String	TRANSACTION_EXCEL_KARVY_TRANSACTION_DATE	 = 	"Transaction Date";
	public static final String	TRANSACTION_EXCEL_KARVY_PROCESS_DATE	 = 	"Process Date";
	public static final String	TRANSACTION_EXCEL_KARVY_NAV	 = 	"Price";
	public static final String	TRANSACTION_EXCEL_KARVY_UNITS_OF_THE_TRANSACTION	 = 	"Units";
	public static final String	TRANSACTION_EXCEL_KARVY_AMOUNT_OF_THE_TRANSACTION	 = 	"Amount";
	public static final String	TRANSACTION_EXCEL_KARVY_DISTRIBUTOR_ARN_CODE	 = 	"Agent Code";
	//public static final String	TRANSACTION_EXCEL_KARVY_SUB_BROKER_CODE	 = 		";
	public static final String	TRANSACTION_EXCEL_KARVY_BROKERAGE_PERCENTAGE	 = 	"Brokerage Percentage";
	public static final String	TRANSACTION_EXCEL_KARVY_BROKERAGE_AMOUNT	 = 	"Commission";
	public static final String	TRANSACTION_EXCEL_KARVY_INVESTOR_ID	 = 	"Investor ID";
	public static final String	TRANSACTION_EXCEL_KARVY_REPORT_DATE	 = 	"Report Date";
	public static final String	TRANSACTION_EXCEL_KARVY_REPORT_TIME	 = 	"Report Time";
	public static final String	TRANSACTION_EXCEL_KARVY_APPLICATION_NUMBER	 = 	"Application Number";
	//public static final String	TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE	 = 		";
	public static final String	TRANSACTION_EXCEL_KARVY_TDS_AMOUNT	 = 	"TDSAmount	";
	public static final String	TRANSACTION_EXCEL_KARVY_SECURITY_TRANSACTION_TAX	 = 	"STT";
	public static final String	TRANSACTION_EXCEL_KARVY_INVESTOR_PAN	 = 	"PAN1";
	public static final String	TRANSACTION_EXCEL_KARVY_TRANSACTION_TYPE	 = 	"Transaction Type";
	public static final String	TRANSACTION_EXCEL_KARVY_TRANSACTION_CHARGES	 = 	"TrCharges";
	public static final String	TRANSACTION_EXCEL_KARVY_TRANSACTION_SUFFIX	 = 	"ToProductCode";
	public static final String	TRANSACTION_EXCEL_KARVY_LOCATION_CATEGORY	 = 	"CityCategory";
	public static final String	TRANSACTION_EXCEL_KARVY_EUIN	 = 	"EUIN";
	public static final String	TRANSACTION_EXCEL_KARVY_EUIN_VALID_INDICATOR	 = 	"EUIN Valid Indicator";
	public static final String	TRANSACTION_EXCEL_KARVY_SUB_BROKER_ARN	 = 	"Sub-Broker Code";
	public static final String	TRANSACTION_EXCEL_KARVY_BANK_NAME	 = 	"Instrument Bank";
	
	/*********************Franklin Transaction_Excel*************************/
	public static final String	TRANSACTION_EXCEL_FRANKLIN_AMC_CODE	 = "COMP_CODE";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_FOLIO_NUMBER = "FOLIO_NO";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_SCHEME_RTA_CODE	 = "SCHEME_CO0";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_SCHEME_NAME	 = "SCHEME_NA1";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_INVESTOR_NAME	 = "INVESTOR_2";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_TYPE_CODE	 = "TRXN_TYPE";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_NUMBER	 = "TRXN_NO";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_MODE	 = "TRXN_MODE";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_STATUS	 = "TRXN_STAT";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_DATE	 = "TRXN_DATE";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_PROCESS_DATE	 = "POSTDT_DA3";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_NAV	 = "NAV";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_UNITS_OF_THE_TRANSACTION	 = "UNITS";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_AMOUNT_OF_THE_TRANSACTION	 = "AMOUNT";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_DISTRIBUTOR_ARN_CODE	 = "BROK_CODE";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_SUB_BROKER_CODE	 = "SUB_BROKE5";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_BROKERAGE_PERCENTAGE	 = "BROK_PERC";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_BROKERAGE_AMOUNT	 = "BROK_COMM";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_INVESTOR_ID	 = "INVEST_ID";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_REPORT_DATE	 = "CREA_DATE";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_REPORT_TIME	 = "CREA_TIME";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_APPLICATION_NUMBER	 = "APPL_NO";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_TRANS_TYPE	 = "TRXN_TYPE";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_TDS_AMOUNT	 = "TDS_AMOUNT";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_SECURITY_TRANSACTION_TAX	 = "STT";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_INVESTOR_PAN	 = "IT_PAN_NO1";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_TXN_TYPE	 = "TRXN_TYPE";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_CHARGES	 = "TXN_CHG";
	//public static final String	TRANSACTION_EXCEL_FRANKLIN_TRANSACTION_SUFFIX	 = "Will keep it blank";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_LOCATION_CATEGORY	 = "B15_T15_34";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_EUIN	 = "EUIN";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_EUIN_VALID_INDICATOR	 = "EUIN_INV33";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_SUB_BROKER_ARN	 = "SUB_BROK32";
	public static final String	TRANSACTION_EXCEL_FRANKLIN_BANK_NAME	 = "PBANK_NAME";
	
	/*XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	/*********************Franklin Transaction_DBF*************************/
	
	public static final String	TRANSACTION_DBF_FRANKLIN_AMC_CODE	 = "COMP_CODE";
	public static final String	TRANSACTION_DBF_FRANKLIN_FOLIO_NUMBER =	 "FOLIO_NO";
	public static final String	TRANSACTION_DBF_FRANKLIN_SCHEME_RTA_CODE	 = "SCHEME_CO0";
	public static final String	TRANSACTION_DBF_FRANKLIN_SCHEME_NAME	 = "SCHEME_NA1";
	public static final String	TRANSACTION_DBF_FRANKLIN_INVESTOR_NAME	 = "INVESTOR_2";
	public static final String	TRANSACTION_DBF_FRANKLIN_TRANSACTION_TYPE_CODE	 = "TRXN_TYPE";
	public static final String	TRANSACTION_DBF_FRANKLIN_TRANSACTION_NUMBER	 = "TRXN_NO";
	public static final String	TRANSACTION_DBF_FRANKLIN_TRANSACTION_MODE	 = "TRXN_MODE";
	public static final String	TRANSACTION_DBF_FRANKLIN_TRANSACTION_STATUS	 = "TRXN_STAT";
	public static final String	TRANSACTION_DBF_FRANKLIN_TRANSACTION_DATE	 = "TRXN_DATE";
	public static final String	TRANSACTION_DBF_FRANKLIN_PROCESS_DATE	 = "POSTDT_DA3";
	public static final String	TRANSACTION_DBF_FRANKLIN_NAV	 = "NAV";
	public static final String	TRANSACTION_DBF_FRANKLIN_UNITS_OF_THE_TRANSACTION	 = "UNITS";
	public static final String	TRANSACTION_DBF_FRANKLIN_AMOUNT_OF_THE_TRANSACTION	 = "AMOUNT";
	public static final String	TRANSACTION_DBF_FRANKLIN_DISTRIBUTOR_ARN_CODE	 = "BROK_CODE";
	public static final String	TRANSACTION_DBF_FRANKLIN_SUB_BROKER_CODE	 = "SUB_BROKE5";
	public static final String	TRANSACTION_DBF_FRANKLIN_BROKERAGE_PERCENTAGE	 = "BROK_PERC";
	public static final String	TRANSACTION_DBF_FRANKLIN_BROKERAGE_AMOUNT	 = "BROK_COMM";
	public static final String	TRANSACTION_DBF_FRANKLIN_INVESTOR_ID	 = "INVEST_ID";
	public static final String	TRANSACTION_DBF_FRANKLIN_REPORT_DATE	 = "CREA_DATE";
	public static final String	TRANSACTION_DBF_FRANKLIN_REPORT_TIME	 = "CREA_TIME";
	public static final String	TRANSACTION_DBF_FRANKLIN_APPLICATION_NUMBER	 = "APPL_NO";
	public static final String	TRANSACTION_DBF_FRANKLIN_TRANS_TYPE	 = "TRXN_TYPE";
	public static final String	TRANSACTION_DBF_FRANKLIN_TDS_AMOUNT	 = "TDS_AMOUNT";
	public static final String	TRANSACTION_DBF_FRANKLIN_SECURITY_TRANSACTION_TAX	 = "STT";
	public static final String	TRANSACTION_DBF_FRANKLIN_INVESTOR_PAN	 = "IT_PAN_NO1";
	public static final String	TRANSACTION_DBF_FRANKLIN_TXN_TYPE	 = "TRXN_TYPE";
	public static final String	TRANSACTION_DBF_FRANKLIN_TRANSACTION_CHARGES	 = "TXN_CHG";
	//public static final String	TRANSACTION_DBF_FRANKLIN_TRANSACTION_SUFFIX	 = "	Will keep it blank	";
	public static final String	TRANSACTION_DBF_FRANKLIN_LOCATION_CATEGORY	 = "B15_T15_34";
	public static final String	TRANSACTION_DBF_FRANKLIN_EUIN	 = "EUIN";
	public static final String	TRANSACTION_DBF_FRANKLIN_EUIN_VALID_INDICATOR	 = "EUIN_INV33";
	public static final String	TRANSACTION_DBF_FRANKLIN_SUB_BROKER_ARN	 = "SUB_BROK32";
	public static final String	TRANSACTION_DBF_FRANKLIN_BANK_NAME	 = "PBANK_NAME";
	
	/*********************KARVY Transaction_DBF*************************/
	public static final String	TRANSACTION_DBF_KARVY_AMC_CODE	 = "TD_FUND";
	public static final String	TRANSACTION_DBF_KARVY_FOLIO_NUMBER = "TD_ACNO";
	public static final String	TRANSACTION_DBF_KARVY_SCHEME_RTA_CODE	 = "FMCODE";
	public static final String	TRANSACTION_DBF_KARVY_SCHEME_NAME	 = "FUNDDESC";
	public static final String	TRANSACTION_DBF_KARVY_INVESTOR_NAME	 = "INVNAME";
	public static final String	TRANSACTION_DBF_KARVY_TRANSACTION_TYPE_CODE	 = "TRFLAG";
	public static final String	TRANSACTION_DBF_KARVY_TRANSACTION_NUMBER	 = "TD_TRNO";
	public static final String	TRANSACTION_DBF_KARVY_TRANSACTION_MODE	 = "TRNMODE";
	public static final String	TRANSACTION_DBF_KARVY_TRANSACTION_STATUS	 = "TRNSTAT";
	public static final String	TRANSACTION_DBF_KARVY_TRANSACTION_DATE	 = "TD_TRDT";
	public static final String	TRANSACTION_DBF_KARVY_PROCESS_DATE	 = "TD_PRDT";
	public static final String	TRANSACTION_DBF_KARVY_NAV	 = "TD_POP";
	public static final String	TRANSACTION_DBF_KARVY_UNITS_OF_THE_TRANSACTION	 = "TD_UNITS";
	public static final String	TRANSACTION_DBF_KARVY_AMOUNT_OF_THE_TRANSACTION	 = "TD_AMT";
	public static final String	TRANSACTION_DBF_KARVY_DISTRIBUTOR_ARN_CODE	 = "TD_AGENT";
	//public static final String	TRANSACTION_DBF_KARVY_SUB_BROKER_CODE	 = "		";
	public static final String	TRANSACTION_DBF_KARVY_BROKERAGE_PERCENTAGE	 = "BROKPER";
	public static final String	TRANSACTION_DBF_KARVY_BROKERAGE_AMOUNT	 = "BROKCOMM";
	public static final String	TRANSACTION_DBF_KARVY_INVESTOR_ID	 = "INVID";
	public static final String	TRANSACTION_DBF_KARVY_REPORT_DATE	 = "CRDATE";
	public static final String	TRANSACTION_DBF_KARVY_REPORT_TIME	 = "CRTIME";
	public static final String	TRANSACTION_DBF_KARVY_APPLICATION_NUMBER	 = "TD_APPNO";
	//public static final String	TRANSACTION_DBF_KARVY_TRANS_TYPE	 = "		";
	public static final String	TRANSACTION_DBF_KARVY_TDS_AMOUNT	 = "TDSAMOUNT";
	public static final String	TRANSACTION_DBF_KARVY_SECURITY_TRANSACTION_TAX	 = "STT";
	public static final String	TRANSACTION_DBF_KARVY_INVESTOR_PAN	 = "PAN1";
	public static final String	TRANSACTION_DBF_KARVY_TXN_TYPE	 = "TD_TRTYPE";
	public static final String	TRANSACTION_DBF_KARVY_TRANSACTION_CHARGES	 = "TRCHARGES";
	public static final String	TRANSACTION_DBF_KARVY_TRANSACTION_SUFFIX	 = "PRCODE1";
	public static final String	TRANSACTION_DBF_KARVY_LOCATION_CATEGORY	 = "CITYCATEG5";
	public static final String	TRANSACTION_DBF_KARVY_EUIN	 = "EUIN";
	public static final String	TRANSACTION_DBF_KARVY_EUIN_VALID_INDICATOR	 = "EVALID";
	public static final String	TRANSACTION_DBF_KARVY_SUB_BROKER_ARN	 = "TD_BROKER";
	public static final String	TRANSACTION_DBF_KARVY_BANK_NAME	 = "CHQBANK";
	
	/*********************CAMS Transaction_DBF*************************/
	public static final String	TRANSACTION_DBF_CAMS_AMC_CODE	 = "AMC_CODE";
	public static final String	TRANSACTION_DBF_CAMS_FOLIO_NUMBER =	 "FOLIO_NO";
	public static final String	TRANSACTION_DBF_CAMS_SCHEME_RTA_CODE	 = "PRODCODE";
	public static final String	TRANSACTION_DBF_CAMS_SCHEME_NAME	 = "SCHEME";
	public static final String	TRANSACTION_DBF_CAMS_INVESTOR_NAME	 = "INV_NAME";
	public static final String	TRANSACTION_DBF_CAMS_TRANSACTION_TYPE_CODE	 = "TRXNTYPE";
	public static final String	TRANSACTION_DBF_CAMS_TRANSACTION_NUMBER	 = "TRXNNO";
	public static final String	TRANSACTION_DBF_CAMS_TRANSACTION_MODE	 = "TRXNMODE";
	public static final String	TRANSACTION_DBF_CAMS_TRANSACTION_STATUS	 = "TRXNSTAT";
	public static final String	TRANSACTION_DBF_CAMS_TRANSACTION_DATE	 = "TRADDATE";
	public static final String	TRANSACTION_DBF_CAMS_PROCESS_DATE	 = "POSTDATE";
	public static final String	TRANSACTION_DBF_CAMS_NAV	 = "PURPRICE";
	public static final String	TRANSACTION_DBF_CAMS_UNITS_OF_THE_TRANSACTION	 = "UNITS";
	public static final String	TRANSACTION_DBF_CAMS_AMOUNT_OF_THE_TRANSACTION	 = "AMOUNT";
	public static final String	TRANSACTION_DBF_CAMS_DISTRIBUTOR_ARN_CODE	 = "BROKCODE";
	public static final String	TRANSACTION_DBF_CAMS_SUB_BROKER_CODE	 = "SUBBROK";
	public static final String	TRANSACTION_DBF_CAMS_BROKERAGE_PERCENTAGE	 = "BROKPERC";
	public static final String	TRANSACTION_DBF_CAMS_BROKERAGE_AMOUNT	 = "BROKCOMM";
	public static final String	TRANSACTION_DBF_CAMS_INVESTOR_ID	 = "ALTFOLIO";
	public static final String	TRANSACTION_DBF_CAMS_REPORT_DATE	 = "REP_DATE";
	public static final String	TRANSACTION_DBF_CAMS_REPORT_TIME	 = "TIME1";
	public static final String	TRANSACTION_DBF_CAMS_APPLICATION_NUMBER	 = "APPLICATIO";
	public static final String	TRANSACTION_DBF_CAMS_TRANS_TYPE	 = "TRXN_NATUR";
	//public static final String	TRANSACTION_DBF_CAMS_TDS_AMOUNT	 = "		";
	public static final String	TRANSACTION_DBF_CAMS_SECURITY_TRANSACTION_TAX	 = "STT";
	public static final String	TRANSACTION_DBF_CAMS_INVESTOR_PAN	 = "PAN";
	//public static final String	TRANSACTION_DBF_CAMS_TXN_TYPE	 = "		";
	public static final String	TRANSACTION_DBF_CAMS_TRANSACTION_CHARGES	 = "TRXN_CHARG";
	public static final String	TRANSACTION_DBF_CAMS_TRANSACTION_SUFFIX	 = "TRXN_SUFFI";
	public static final String	TRANSACTION_DBF_CAMS_LOCATION_CATEGORY	 = "TER_LOCATI";
	public static final String	TRANSACTION_DBF_CAMS_EUIN	 = "EUIN";
	public static final String	TRANSACTION_DBF_CAMS_EUIN_VALID_INDICATOR	 = "EUIN_VALID";
	public static final String	TRANSACTION_DBF_CAMS_SUB_BROKER_ARN	 = "SUB_BRK_AR";
	public static final String	TRANSACTION_DBF_CAMS_BANK_NAME	 = "BANK_NAME";
	
	/********************* Transaction_DBF*************************/
	public static final String	TRANSACTION_DBF_SUNDARAM_AMC_CODE	 = "AMC_CODE";
	public static final String	TRANSACTION_DBF_SUNDARAM_FOLIO_NUMBER =	 "FOLIO_NO";
	public static final String	TRANSACTION_DBF_SUNDARAM_SCHEME_RTA_CODE	 = "PRODCODE";
	public static final String	TRANSACTION_DBF_SUNDARAM_SCHEME_NAME	 = "SCHEME";
	public static final String	TRANSACTION_DBF_SUNDARAM_INVESTOR_NAME	 = "INV_NAME";
	public static final String	TRANSACTION_DBF_SUNDARAM_TRANSACTION_TYPE_CODE	 = "TRXNTYPE";
	public static final String	TRANSACTION_DBF_SUNDARAM_TRANSACTION_NUMBER	 = "TRXNNO";
	public static final String	TRANSACTION_DBF_SUNDARAM_TRANSACTION_MODE	 = "TRXNMODE";
	public static final String	TRANSACTION_DBF_SUNDARAM_TRANSACTION_STATUS	 = "TRXNSTAT";
	public static final String	TRANSACTION_DBF_SUNDARAM_TRANSACTION_DATE	 = "TRADDATE";
	public static final String	TRANSACTION_DBF_SUNDARAM_PROCESS_DATE	 = "POSTDATE";
	public static final String	TRANSACTION_DBF_SUNDARAM_NAV	 = "PURPRICE";
	public static final String	TRANSACTION_DBF_SUNDARAM_UNITS_OF_THE_TRANSACTION	 = "UNITS";
	public static final String	TRANSACTION_DBF_SUNDARAMS_AMOUNT_OF_THE_TRANSACTION	 = "AMOUNT";
	public static final String	TRANSACTION_DBF_SUNDARAM_DISTRIBUTOR_ARN_CODE	 = "BROKCODE";
	public static final String	TRANSACTION_DBF_SUNDARAM_SUB_BROKER_CODE	 = "SUBBROK";
	public static final String	TRANSACTION_DBF_SUNDARAM_BROKERAGE_PERCENTAGE	 = "BROKPERC";
	public static final String	TRANSACTION_DBF_SUNDARAM_BROKERAGE_AMOUNT	 = "BROKCOMM";
	public static final String	TRANSACTION_DBF_SUNDARAM_INVESTOR_ID	 = "ALTFOLIO";
	public static final String	TRANSACTION_DBF_SUNDARAM_REPORT_DATE	 = "REP_DATE";
	public static final String	TRANSACTION_DBF_SUNDARAM_REPORT_TIME	 = "TIME";
	public static final String	TRANSACTION_DBF_SUNDARAM_APPLICATION_NUMBER	 = "APPLICATIO";
	public static final String	TRANSACTION_DBF_SUNDARAM_TRANS_TYPE	 = "TRXN_NATUR";
	public static final String	TRANSACTION_DBF_SUNDARAM_TDS_AMOUNT	 = "TOTAL_TAX";
	public static final String	TRANSACTION_DBF_SUNDARAM_SECURITY_TRANSACTION_TAX	 = "STT";
	public static final String	TRANSACTION_DBF_SUNDARAM_INVESTOR_PAN	 = "PAN";
	public static final String	TRANSACTION_DBF_SUNDARAM_TXN_TYPE	 = "TRXNTYPE";
	public static final String	TRANSACTION_DBF_SUNDARAM_TRANSACTION_CHARGES	 = "TRXNCHGS";
	public static final String	TRANSACTION_DBF_SUNDARAM_TRANSACTION_SUFFIX	 = "TXN_SOURCE";
	public static final String	TRANSACTION_DBF_SUNDARAM_LOCATION_CATEGORY	 = "SEBICITY";
	public static final String	TRANSACTION_DBF_SUNDARAM_EUIN	 = "EUIN";
	//public static final String	TRANSACTION_DBF_SUNDARAM_EUIN_VALID_INDICATOR	 = "EUIN_VALID";
	public static final String	TRANSACTION_DBF_SUNDARAM_SUB_BROKER_ARN	 = "SUBBROK";
	//public static final String	TRANSACTION_DBF_SUNDARAM_BANK_NAME	 = "BANK_NAME";
	
	/********************Investor Columns ***************************************************************************************************************************************/
	/*********************CAMS Investor_EXCEL*************************/
	public static final String	INVESTOR_EXCEL_CAMS_FOLIO_NUMBER	 = "FOLIOCHK";
	public static final String	INVESTOR_EXCEL_CAMS_INVESTOR_NAME	 = "INV_NAME";
	public static final String	INVESTOR_EXCEL_CAMS_ADDRESS_LINE_1	 = "ADDRESS1";
	public static final String	INVESTOR_EXCEL_CAMS_ADDRESS_LINE_2	 = "ADDRESS2";
	public static final String	INVESTOR_EXCEL_CAMS_ADDRESS_LINE_3	 = "ADDRESS3";
	public static final String	INVESTOR_EXCEL_CAMS_CITY	 = "CITY";
	public static final String	INVESTOR_EXCEL_CAMS_PINCODE	 = "PINCODE";
	public static final String	INVESTOR_EXCEL_CAMS_SCHEME_RTA_CODE	 = "PRODUCT";
	public static final String	INVESTOR_EXCEL_CAMS_SCHEME_NAME	 = "SCH_NAME";
	public static final String	INVESTOR_EXCEL_CAMS_BALANCE_DATE	 = "REP_DATE";
	public static final String	INVESTOR_EXCEL_CAMS_UNIT_BALANCE	 = "CLOS_BAL";
	public static final String	INVESTOR_EXCEL_CAMS_RUPEE_BALANCE	 = "RUPEE_BAL";
	public static final String	INVESTOR_EXCEL_CAMS_JOINT_NAME1	 = "JNT_NAME1";
	public static final String	INVESTOR_EXCEL_CAMS_JOINT_NAME2	 = "JNT_NAME2";
	public static final String	INVESTOR_EXCEL_CAMS_PHONE_OFFICE	 = "PHONE_OFF";
	public static final String	INVESTOR_EXCEL_CAMS_PHONE_RESIDENCE	 = "PHONE_RES";
	public static final String	INVESTOR_EXCEL_CAMS_EMAIL	 = "EMAIL";
	//public static final String	INVESTOR_EXCEL_CAMS_MODE_OF_HOLDING	 = "HOLDING_NA";
	public static final String	INVESTOR_EXCEL_CAMS_MODE_OF_HOLDING	 = "holdingMode";

	public static final String	INVESTOR_EXCEL_CAMS_UIN	 = "UIN_NO";
	public static final String	INVESTOR_EXCEL_CAMS_INVESTOR_PAN	 = "PAN_NO";
	public static final String	INVESTOR_EXCEL_CAMS_JOINT1_PAN	 = "JOINT1_PAN";
	public static final String	INVESTOR_EXCEL_CAMS_JOINT2_PAN	 = "JOINT2_PAN";
	public static final String	INVESTOR_EXCEL_CAMS_GUARDIAN_PAN	 = "GUARD_PAN";
	public static final String	INVESTOR_EXCEL_CAMS_TAX_STATUS	 = "TAX_STATUS";
	//public static final String	INVESTOR_EXCEL_CAMS_DISTRIBUTOR_ARN_CODE	 = "BROKER_COD";
	public static final String	INVESTOR_EXCEL_CAMS_DISTRIBUTOR_ARN_CODE	 = "brokerDealerCode";

	public static final String	INVESTOR_EXCEL_CAMS_SUBBROKER_DEALER_CODE	 = "SUBBROKER";
	public static final String	INVESTOR_EXCEL_CAMS_REINV_FLAG	 = "REINV_FLAG";
	public static final String	INVESTOR_EXCEL_CAMS_BANK_NAME	 = "BANK_NAME";
	public static final String	INVESTOR_EXCEL_CAMS_BANK_BRANCH	 = "BRANCH";
	public static final String	INVESTOR_EXCEL_CAMS_BANK_AC_TYPE	 = "AC_TYPE";
	public static final String	INVESTOR_EXCEL_CAMS_BANK_AC_NO	 = "AC_NO";
	public static final String	INVESTOR_EXCEL_CAMS_BANK_ADDRESS_1	 = "B_ADDRESS1	";
	public static final String	INVESTOR_EXCEL_CAMS_BANK_ADDRESS_2	 = "B_ADDRESS2	";
	public static final String	INVESTOR_EXCEL_CAMS_BANK_ADDRESS_3	 = "B_ADDRESS3";
	public static final String	INVESTOR_EXCEL_CAMS_BANK_CITY	 = "B_CITY";
	public static final String	INVESTOR_EXCEL_CAMS_BANK_PINCODE	 = "B_PINCODE";
	public static final String	INVESTOR_EXCEL_CAMS_INVESTOR_DATE_OF_BIRTH	 = "INV_DOB";
	public static final String	INVESTOR_EXCEL_CAMS_MOBILE_NUMBER	 = "MOBILE_NO";
	public static final String	INVESTOR_EXCEL_CAMS_OCCUPATION	 = "OCCUPATION";
	public static final String	INVESTOR_EXCEL_CAMS_INVESTOR_MIN	 = "INV_IIN";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_NAME	 = "NOM_NAME";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_RELATION	 = "RELATION";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_ADDRESS1	 = "NOM_ADDR1";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_ADDRESS2	 = "NOM_ADDR2";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_ADDRESS3	 = "NOM_ADDR3";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_CITY	 = "NOM_CITY";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_STATE	 = "NOM_STATE";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_PINCODE	 = "NOM_PINCOD";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_PHONE_OFFICE	 = "NOM_PH_OFF";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_PHONE_RESIDENCE	 = "NOM_PH_RES";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_EMAIL	 = "NOM_EMAIL";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_PERCENTAGE	 = "NOM_PERCEN";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_NAME	 = "NOM2_NAME";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_RELATION	 = "NOM2_RELAT";
	public static final String	INVESTOR_EXCEL_CAMS_INVESTOR_EXCEL_CAMS_NOMINEE2_ADDRESS1	 = "NOM2_ADDR1";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_ADDRESS2	 = "NOM2_ADDR2";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_ADDRESS3	 = "NOM2_ADDR3";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_CITY	 = "NOM2_CITY";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_STATE	 = "NOM2_STATE";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_PINCODE	 = "NOM2_PINCO";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_PHONE_OFFICE	 = "NOM2_PH_OF";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_PHONE_RESIDENCE	 = "NOM2_PH_RE";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_EMAIL	 = "NOM2_EMAIL";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_PERCENTAGE	 = "NOM2_PERCE";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_NAME	 = "NOM3_NAME";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_RELATION	 = "NOM3_RELAT";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_ADDRESS1	 = "NOM3_ADDR1";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_ADDRESS2	 = "NOM3_ADDR2";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_ADDRESS3	 = "NOM3_ADDR3";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_CITY	 = "NOM3_CITY";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_STATE	 = "NOM3_STATE";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_PINCODE	 = "NOM3_PINCO";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_PHONE_OFFICE	 = "NOM3_PH_OF";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_PHONE_RESIDENCE	 = "NOM3_PH_RE";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_EMAIL	 = "NOM3_EMAIL";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_PERCENTAGE	 = "NOM3_PERCE";
	public static final String	INVESTOR_EXCEL_CAMS_IFSC_CODE	 = "IFSC_CODE";
	public static final String	INVESTOR_EXCEL_CAMS_DP_ID	 = "DP_ID";
	public static final String	INVESTOR_EXCEL_CAMS_DEMAT	 = "DEMAT";
	public static final String	INVESTOR_EXCEL_CAMS_NAME_OF_GUARDIAN	 = "GUARD_NAME";
	public static final String	INVESTOR_EXCEL_CAMS_BROKER_DEALER_CODE	 = "BROKCODE";
	public static final String	INVESTOR_EXCEL_CAMS_FOLIO_CREATE_DATE	 = "FOLIO_DATE";
	public static final String	INVESTOR_EXCEL_CAMS_INVESTOR_AADHAAR_NUMBER	 = "AADHAAR";
	public static final String	INVESTOR_EXCEL_CAMS_FIRST_HOLDER_CKYC	 = "FH_CKYC_NO";
	public static final String	INVESTOR_EXCEL_CAMS_JOINT_HOLDER1_CKYC	 = "JH1_CKYC";
	public static final String	INVESTOR_EXCEL_CAMS_JOINT_HOLDER2_CKYC	 = "JH2_CKYC";
	public static final String	INVESTOR_EXCEL_CAMS_GUARDIAN_CKYC	 = "G_CKYC_NO";
	public static final String	INVESTOR_EXCEL_CAMS_AMC_CODE	 = "AMC_CODE";
	
	/*********************KARVY Investor_EXCEL*************************/
	public static final String	INVESTOR_EXCEL_KARVY_FOLIO_NUMBER	 = "Folio";
	public static final String	INVESTOR_EXCEL_KARVY_INVESTOR_NAME	 = "Investor Name";
	public static final String	INVESTOR_EXCEL_KARVY_ADDRESS_LINE_1	 = "Address #1";
	public static final String	INVESTOR_EXCEL_KARVY_ADDRESS_LINE_2	 = "Address #2";
	public static final String	INVESTOR_EXCEL_KARVY_ADDRESS_LINE_3	 = "Address #3";
	public static final String	INVESTOR_EXCEL_KARVY_CITY	 = "City";
	public static final String	INVESTOR_EXCEL_KARVY_PINCODE	 = "Pincode";
	public static final String	INVESTOR_EXCEL_KARVY_SCHEME_RTA_CODE	 = "Product Code";
	public static final String	INVESTOR_EXCEL_KARVY_SCHEME_NAME	 = "Fund Description";
	public static final String	INVESTOR_EXCEL_KARVY_BALANCE_DATE	 = "		";
	//public static final String	INVESTOR_EXCEL_KARVY_UNIT_BALANCE	 = "		";
	//public static final String	INVESTOR_EXCEL_KARVY_RUPEE_BALANCE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_JOINT_NAME1	 = "Joint Name 1";
	public static final String	INVESTOR_EXCEL_KARVY_JOINT_NAME2	 = "Joint Name 2";
	public static final String	INVESTOR_EXCEL_KARVY_PHONE_OFFICE	 = "Phone Office";
	public static final String	INVESTOR_EXCEL_KARVY_PHONE_RESIDENCE	 = "Phone Res#1";
	public static final String	INVESTOR_EXCEL_KARVY_EMAIL	 = "Email";
	//public static final String	INVESTOR_EXCEL_KARVY_MODE_OF_HOLDING	 = "Mode of Holding Description	";
	//public static final String	INVESTOR_EXCEL_KARVY_UIN	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_INVESTOR_PAN	 = "PAN Number";
	public static final String	INVESTOR_EXCEL_KARVY_JOINT1_PAN	 = "PAN2";
	public static final String	INVESTOR_EXCEL_KARVY_JOINT2_PAN	 = "PAN3";
	public static final String	INVESTOR_EXCEL_KARVY_GUARDIAN_PAN	 = "GuardPanNo";
	public static final String	INVESTOR_EXCEL_KARVY_TAX_STATUS	 = "Tax Status";
	public static final String	INVESTOR_EXCEL_KARVY_DISTRIBUTOR_ARN_CODE	 = "Broker Code";
	//public static final String	INVESTOR_EXCEL_KARVY_SUBBROKER_DEALER_CODE	 = "		";
	//public static final String	INVESTOR_EXCEL_KARVY_REINV_FLAG	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_BANK_NAME	 = "Bank Name";
	public static final String	INVESTOR_EXCEL_KARVY_BANK_BRANCH	 = "Branch";
	public static final String	INVESTOR_EXCEL_KARVY_BANK_AC_TYPE	 = "Account Type";
	public static final String	INVESTOR_EXCEL_KARVY_BANK_AC_NO	 = "BankAccno";
	public static final String	INVESTOR_EXCEL_KARVY_BANK_ADDRESS_1	 = "Bank Address #1";
	public static final String	INVESTOR_EXCEL_KARVY_BANK_ADDRESS_2	 = "Bank Address #2";
	public static final String	INVESTOR_EXCEL_KARVY_BANK_ADDRESS_3	 = "Bank Address #3";
	public static final String	INVESTOR_EXCEL_KARVY_BANK_CITY	 = "Bank City";
	//public static final String	INVESTOR_EXCEL_KARVY_BANK_PINCODE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_INVESTOR_DATE_OF_BIRTH	 = "Date of Birth";
	public static final String	INVESTOR_EXCEL_KARVY_MOBILE_NUMBER	 = "mobileNumber";
	//public static final String	INVESTOR_EXCEL_KARVY_OCCUPATION	 = "		";
	//public static final String	INVESTOR_EXCEL_KARVY_INVESTOR_MIN	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE_NAME	 = "Nominee";
	/*
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE_RELATION	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE_ADDRESS1	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE_ADDRESS2	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE_ADDRESS3	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE_CITY	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE_STATE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE_PINCODE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE_PHONE_OFFICE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE_PHONE_RESIDENCE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE_EMAIL	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE_PERCENTAGE	 = "		";
	*/
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE2_NAME	 = "Nominee2";
	/*
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE2_RELATION	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_INVESTOR_EXCEL_KARVY_NOMINEE2_ADDRESS1	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE2_ADDRESS2	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE2_ADDRESS3	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE2_CITY	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE2_STATE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE2_PINCODE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE2_PHONE_OFFICE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE2_PHONE_RESIDENCE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE2_EMAIL	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE2_PERCENTAGE	 = "		";
	*/
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE3_NAME	 = "Nominee3";
	/*
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE3_RELATION	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE3_ADDRESS1	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE3_ADDRESS2	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE3_ADDRESS3	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE3_CITY	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE3_STATE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE3_PINCODE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE3_PHONE_OFFICE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE3_PHONE_RESIDENCE	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE3_EMAIL	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NOMINEE3_PERCENTAGE	 = "		";
	*/
	public static final String	INVESTOR_EXCEL_KARVY_IFSC_CODE	 = "IFSC code";
	public static final String	INVESTOR_EXCEL_KARVY_DP_ID	 = "DPID";
	//public static final String	INVESTOR_EXCEL_KARVY_DEMAT	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_NAME_OF_GUARDIAN	 = "GuardianName";
	public static final String	INVESTOR_EXCEL_KARVY_BROKER_DEALER_CODE	 = "Broker Code";
	//public static final String	INVESTOR_EXCEL_KARVY_FOLIO_CREATE_DATE	 = "		";
	//public static final String	INVESTOR_EXCEL_KARVY_INVESTOR_AADHAAR_NUMBER	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_FIRST_HOLDER_CKYC	 = "Kyc1Flag";
	public static final String	INVESTOR_EXCEL_KARVY_JOINT_HOLDER1_CKYC	 = "Kyc2Flag";
	public static final String	INVESTOR_EXCEL_KARVY_JOINT_HOLDER2_CKYC	 = "Kyc3Flag";
	//public static final String	INVESTOR_EXCEL_KARVY_GUARDIAN_CKYC	 = "		";
	public static final String	INVESTOR_EXCEL_KARVY_AMC_CODE	 = "Fund";
	
	/*********************FRANKLIN Investor_EXCEL*************************/
	public static final String	INVESTOR_EXCEL_FRANKLIN_FOLIO_NUMBER	 = "FOLIO_NO";
	public static final String	INVESTOR_EXCEL_FRANKLIN_INVESTOR_NAME	 = "INV_NAME";
	public static final String	INVESTOR_EXCEL_FRANKLIN_ADDRESS_LINE_1	 = "ADDRESS1";
	public static final String	INVESTOR_EXCEL_FRANKLIN_ADDRESS_LINE_2	 = "ADDRESS2";
	public static final String	INVESTOR_EXCEL_FRANKLIN_ADDRESS_LINE_3	 = "ADDRESS3";
	public static final String	INVESTOR_EXCEL_FRANKLIN_CITY	 = "CITY";
	public static final String	INVESTOR_EXCEL_FRANKLIN_PINCODE	 = "PINCODE";
	/*
	public static final String	INVESTOR_EXCEL_FRANKLIN_SCHEME_RTA_CODE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_SCHEME_NAME	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_BALANCE_DATE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_UNIT_BALANCE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_RUPEE_BALANCE	 = "		";
	*/
	public static final String	INVESTOR_EXCEL_FRANKLIN_JOINT_NAME1	 = "JOINT_NAM1";
	public static final String	INVESTOR_EXCEL_FRANKLIN_JOINT_NAME2	 = "JOINT_NAM2";
	public static final String	INVESTOR_EXCEL_FRANKLIN_PHONE_OFFICE	 = "PHONE_OFF";
	public static final String	INVESTOR_EXCEL_FRANKLIN_PHONE_RESIDENCE	 = "PHONE_RES";
	public static final String	INVESTOR_EXCEL_FRANKLIN_EMAIL	 = "EMAIL";
	public static final String	INVESTOR_EXCEL_FRANKLIN_MODE_OF_HOLDING	 = "HOLDING_T6";
	//public static final String	INVESTOR_EXCEL_FRANKLIN_UIN	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_INVESTOR_PAN	 = "PANNO1";
	public static final String	INVESTOR_EXCEL_FRANKLIN_JOINT1_PAN	 = "PANNO2";
	public static final String	INVESTOR_EXCEL_FRANKLIN_JOINT2_PAN	 = "PANNO3";
	public static final String	INVESTOR_EXCEL_FRANKLIN_GUARDIAN_PAN	 = "GUARDIAN21";
	public static final String	INVESTOR_EXCEL_FRANKLIN_TAX_STATUS	 = "TAX_STATUS";
	public static final String	INVESTOR_EXCEL_FRANKLIN_DISTRIBUTOR_ARN_CODE	 = "BROK_CODE";
	public static final String	INVESTOR_EXCEL_FRANKLIN_SUBBROKER_DEALER_CODE	 = "SUB_BROK18";
	//public static final String	INVESTOR_EXCEL_FRANKLIN_REINV_FLAG	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_BANK_NAME	 = "PBANK_NAME";
	public static final String	INVESTOR_EXCEL_FRANKLIN_BANK_BRANCH	 = "BRANCH_N12";
	public static final String	INVESTOR_EXCEL_FRANKLIN_BANK_AC_TYPE	 = "ACCOUNT_11";
	public static final String	INVESTOR_EXCEL_FRANKLIN_BANK_AC_NO	 = "PERSONAL_9";
	public static final String	INVESTOR_EXCEL_FRANKLIN_BANK_ADDRESS_1	 = "B_ADDRESS1";
	public static final String	INVESTOR_EXCEL_FRANKLIN_BANK_ADDRESS_2	 = "B_ADDRESS2";
	public static final String	INVESTOR_EXCEL_FRANKLIN_BANK_ADDRESS_3	 = "B_ADDRESS3";
	public static final String	INVESTOR_EXCEL_FRANKLIN_BANK_CITY	 = "B_CITY";
	public static final String	INVESTOR_EXCEL_FRANKLIN_BANK_PINCODE	 = "B_PINCODE";
	public static final String	INVESTOR_EXCEL_FRANKLIN_INVESTOR_DATE_OF_BIRTH	 = "D_BIRTH";
	public static final String	INVESTOR_EXCEL_FRANKLIN_MOBILE_NUMBER	 = "PHONE_RES1";
	//public static final String	INVESTOR_EXCEL_FRANKLIN_OCCUPATION	 = "		";
	//public static final String	INVESTOR_EXCEL_FRANKLIN_INVESTOR_MIN	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE_NAME	 = "NOMINEE1";
	/*
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE_RELATION	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE_ADDRESS1	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE_ADDRESS2	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE_ADDRESS3	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE_CITY	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE_STATE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE_PINCODE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE_PHONE_OFFICE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE_PHONE_RESIDENCE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE_EMAIL	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE_PERCENTAGE	 = "		";
	*/
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE2_NAME	 = "NOMINEE2";
	/*
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE2_RELATION	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_INVESTOR_EXCEL_FRANKLIN_NOMINEE2_ADDRESS1	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE2_ADDRESS2	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE2_ADDRESS3	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE2_CITY	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE2_STATE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE2_PINCODE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE2_PHONE_OFFICE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE2_PHONE_RESIDENCE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE2_EMAIL	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE2_PERCENTAGE	 = "		";
	*/
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE3_NAME	 = "NOMINEE3";
	/*
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE3_RELATION	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE3_ADDRESS1	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE3_ADDRESS2	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE3_ADDRESS3	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE3_CITY	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE3_STATE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE3_PINCODE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE3_PHONE_OFFICE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE3_PHONE_RESIDENCE	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE3_EMAIL	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NOMINEE3_PERCENTAGE	 = "		";
	*/
	public static final String	INVESTOR_EXCEL_FRANKLIN_IFSC_CODE	 = "IFSC_CODE";
	public static final String	INVESTOR_EXCEL_FRANKLIN_DP_ID	 = "DP_ID";
	public static final String	INVESTOR_EXCEL_FRANKLIN_DEMAT	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_NAME_OF_GUARDIAN	 = "GUARDIAN20";
	public static final String	INVESTOR_EXCEL_FRANKLIN_BROKER_DEALER_CODE	 = "BROK_CODE";
	public static final String	INVESTOR_EXCEL_FRANKLIN_FOLIO_CREATE_DATE	 = "CREA_DATE";
	//public static final String	INVESTOR_EXCEL_FRANKLIN_INVESTOR_AADHAAR_NUMBER	 = "		";
	public static final String	INVESTOR_EXCEL_FRANKLIN_FIRST_HOLDER_CKYC	 = "CKYC_ID";
	public static final String	INVESTOR_EXCEL_FRANKLIN_JOINT_HOLDER1_CKYC	 = "H1_CKYC_ID";
	public static final String	INVESTOR_EXCEL_FRANKLIN_JOINT_HOLDER2_CKYC	 = "H2_CKYC_ID";
	public static final String	INVESTOR_EXCEL_FRANKLIN_GUARDIAN_CKYC	 = "G_CKYC_ID";
	public static final String	INVESTOR_EXCEL_FRANKLIN_AMC_CODE	 = "COMP_CODE";
	
	/*********************FRANKLIN Investor_DBF*************************/
	public static final String	INVESTOR_DBF_FRANKLIN_FOLIO_NUMBER	 = "FOLIO_NO";
	public static final String	INVESTOR_DBF_FRANKLIN_INVESTOR_NAME	 = "INV_NAME";
	public static final String	INVESTOR_DBF_FRANKLIN_ADDRESS_LINE_1	 = "ADDRESS1";
	public static final String	INVESTOR_DBF_FRANKLIN_ADDRESS_LINE_2	 = "ADDRESS2";
	public static final String	INVESTOR_DBF_FRANKLIN_ADDRESS_LINE_3	 = "ADDRESS3";
	public static final String	INVESTOR_DBF_FRANKLIN_CITY	 = "CITY";
	public static final String	INVESTOR_DBF_FRANKLIN_PINCODE	 = "PINCODE";
	/*
	public static final String	INVESTOR_DBF_FRANKLIN_SCHEME_RTA_CODE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_SCHEME_NAME	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_BALANCE_DATE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_UNIT_BALANCE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_RUPEE_BALANCE	 = "		";
	*/
	public static final String	INVESTOR_DBF_FRANKLIN_JOINT_NAME1	 = "JOINT_NAM1";
	public static final String	INVESTOR_DBF_FRANKLIN_JOINT_NAME2	 = "JOINT_NAM2";
	public static final String	INVESTOR_DBF_FRANKLIN_PHONE_OFFICE	 = "PHONE_OFF";
	public static final String	INVESTOR_DBF_FRANKLIN_PHONE_RESIDENCE	 = "PHONE_RES";
	public static final String	INVESTOR_DBF_FRANKLIN_EMAIL	 = "EMAIL";
	public static final String	INVESTOR_DBF_FRANKLIN_MODE_OF_HOLDING	 = "HOLDING_T6";
	//public static final String	INVESTOR_DBF_FRANKLIN_UIN	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_INVESTOR_PAN	 = "PANNO1";
	public static final String	INVESTOR_DBF_FRANKLIN_JOINT1_PAN	 = "PANNO2";
	public static final String	INVESTOR_DBF_FRANKLIN_JOINT2_PAN	 = "PANNO3";
	public static final String	INVESTOR_DBF_FRANKLIN_GUARDIAN_PAN	 = "GUARDIAN22";
	public static final String	INVESTOR_DBF_FRANKLIN_TAX_STATUS	 = "TAX_STATUS";
	public static final String	INVESTOR_DBF_FRANKLIN_DISTRIBUTOR_ARN_CODE	 = "BROK_CODE";
	public static final String	INVESTOR_DBF_FRANKLIN_SUBBROKER_DEALER_CODE	 = "SUB_BROK18";
	//public static final String	INVESTOR_DBF_FRANKLIN_REINV_FLAG	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_BANK_NAME	 = "PBANK_NAME";
	public static final String	INVESTOR_DBF_FRANKLIN_BANK_BRANCH	 = "BRANCH_N12";
	public static final String	INVESTOR_DBF_FRANKLIN_BANK_AC_TYPE	 = "ACCOUNT_11";
	public static final String	INVESTOR_DBF_FRANKLIN_BANK_AC_NO	 = "PERSONAL_9";
	public static final String	INVESTOR_DBF_FRANKLIN_BANK_ADDRESS_1	 = "B_ADDRESS1";
	public static final String	INVESTOR_DBF_FRANKLIN_BANK_ADDRESS_2	 = "B_ADDRESS2";
	public static final String	INVESTOR_DBF_FRANKLIN_BANK_ADDRESS_3	 = "B_ADDRESS3";
	public static final String	INVESTOR_DBF_FRANKLIN_BANK_CITY	 = "B_CITY";
	public static final String	INVESTOR_DBF_FRANKLIN_BANK_PINCODE	 = "B_PINCODE";
	public static final String	INVESTOR_DBF_FRANKLIN_INVESTOR_DATE_OF_BIRTH	 = "D_BIRTH";
	public static final String	INVESTOR_DBF_FRANKLIN_MOBILE_NUMBER	 = "PHONE_RES2";
	//public static final String	INVESTOR_DBF_FRANKLIN_OCCUPATION	 = "		";
	//public static final String	INVESTOR_DBF_FRANKLIN_INVESTOR_MIN	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE_NAME	 = "NOMINEE1";
	/*
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE_RELATION	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE_ADDRESS1	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE_ADDRESS2	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE_ADDRESS3	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE_CITY	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE_STATE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE_PINCODE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE_PHONE_OFFICE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE_PHONE_RESIDENCE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE_EMAIL	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE_PERCENTAGE	 = "		";
	*/
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE2_NAME	 = "NOMINEE2";
	/*
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE2_RELATION	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_INVESTOR_DBF_FRANKLIN_NOMINEE2_ADDRESS1	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE2_ADDRESS2	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE2_ADDRESS3	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE2_CITY	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE2_STATE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE2_PINCODE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE2_PHONE_OFFICE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE2_PHONE_RESIDENCE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE2_EMAIL	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE2_PERCENTAGE	 = "		";
	*/
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE3_NAME	 = "NOMINEE3";
	/*
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE3_RELATION	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE3_ADDRESS1	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE3_ADDRESS2	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE3_ADDRESS3	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE3_CITY	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE3_STATE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE3_PINCODE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE3_PHONE_OFFICE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE3_PHONE_RESIDENCE	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE3_EMAIL	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NOMINEE3_PERCENTAGE	 = "		";
	*/
	public static final String	INVESTOR_DBF_FRANKLIN_IFSC_CODE	 = "IFSC_CODE";
	public static final String	INVESTOR_DBF_FRANKLIN_DP_ID	 = "DP_ID";
	//public static final String	INVESTOR_DBF_FRANKLIN_DEMAT	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_NAME_OF_GUARDIAN	 = "GUARDIAN20";
	public static final String	INVESTOR_DBF_FRANKLIN_BROKER_DEALER_CODE	 = "BROK_CODE";
	public static final String	INVESTOR_DBF_FRANKLIN_FOLIO_CREATE_DATE	 = "CREA_DATE";
	//public static final String	INVESTOR_DBF_FRANKLIN_INVESTOR_AADHAAR_NUMBER	 = "		";
	public static final String	INVESTOR_DBF_FRANKLIN_FIRST_HOLDER_CKYC	 = "CKYC_ID";
	public static final String	INVESTOR_DBF_FRANKLIN_JOINT_HOLDER1_CKYC	 = "H1_CKYC_ID";
	public static final String	INVESTOR_DBF_FRANKLIN_JOINT_HOLDER2_CKYC	 = "H2_CKYC_ID";
	public static final String	INVESTOR_DBF_FRANKLIN_GUARDIAN_CKYC	 = "G_CKYC_ID";
	public static final String	INVESTOR_DBF_FRANKLIN_AMC_CODE	 = "COMP_CODE";
	
	/*********************CAMS Investor_DBF*************************/
	
	public static final String	INVESTOR_DBF_CAMS_FOLIO_NUMBER	 = "FOLIOCHK";
	public static final String	INVESTOR_DBF_CAMS_INVESTOR_NAME	 = "INV_NAME";
	public static final String	INVESTOR_DBF_CAMS_ADDRESS_LINE_1	 = "ADDRESS1";
	public static final String	INVESTOR_DBF_CAMS_ADDRESS_LINE_2	 = "ADDRESS2";
	public static final String	INVESTOR_DBF_CAMS_ADDRESS_LINE_3	 = "ADDRESS3";
	public static final String	INVESTOR_DBF_CAMS_CITY	 = "CITY";
	public static final String	INVESTOR_DBF_CAMS_PINCODE	 = "PINCODE";
	public static final String	INVESTOR_DBF_CAMS_SCHEME_RTA_CODE	 = "PRODUCT";
	public static final String	INVESTOR_DBF_CAMS_SCHEME_NAME	 = "SCH_NAME";
	public static final String	INVESTOR_DBF_CAMS_BALANCE_DATE	 = "REP_DATE";
	public static final String	INVESTOR_DBF_CAMS_UNIT_BALANCE	 = "CLOS_BAL";
	public static final String	INVESTOR_DBF_CAMS_RUPEE_BALANCE	 = "RUPEE_BAL";

	public static final String	INVESTOR_DBF_CAMS_JOINT_NAME1	 = "JNT_NAME1";
	public static final String	INVESTOR_DBF_CAMS_JOINT_NAME2	 = "JNT_NAME2";
	public static final String	INVESTOR_DBF_CAMS_PHONE_OFFICE	 = "PHONE_OFF";
	public static final String	INVESTOR_DBF_CAMS_PHONE_RESIDENCE	 = "PHONE_RES";
	public static final String	INVESTOR_DBF_CAMS_EMAIL	 = "EMAIL";
	public static final String	INVESTOR_DBF_CAMS_MODE_OF_HOLDING	 = "HOLDING_NA";
	public static final String	INVESTOR_DBF_CAMS_UIN	 = "UIN_NO";
	public static final String	INVESTOR_DBF_CAMS_INVESTOR_PAN	 = "PAN_NO";
	public static final String	INVESTOR_DBF_CAMS_JOINT1_PAN	 = "JOINT1_PAN";
	public static final String	INVESTOR_DBF_CAMS_JOINT2_PAN	 = "JOINT2_PAN";
	public static final String	INVESTOR_DBF_CAMS_GUARDIAN_PAN	 = "GUARD_PAN";
	public static final String	INVESTOR_DBF_CAMS_TAX_STATUS	 = "TAX_STATUS";
	public static final String	INVESTOR_DBF_CAMS_DISTRIBUTOR_ARN_CODE	 = "BROKER_COD";
	public static final String	INVESTOR_DBF_CAMS_SUBBROKER_DEALER_CODE	 = "SUBBROKER";
	public static final String	INVESTOR_DBF_CAMS_REINV_FLAG	 = "REINV_FLAG";
	public static final String	INVESTOR_DBF_CAMS_BANK_NAME	 = "BANK_NAME";
	public static final String	INVESTOR_DBF_CAMS_BANK_BRANCH	 = "BRANCH";
	public static final String	INVESTOR_DBF_CAMS_BANK_AC_TYPE	 = "AC_TYPE";
	public static final String	INVESTOR_DBF_CAMS_BANK_AC_NO	 = "AC_NO";
	public static final String	INVESTOR_DBF_CAMS_BANK_ADDRESS_1	 = "B_ADDRESS1";
	public static final String	INVESTOR_DBF_CAMS_BANK_ADDRESS_2	 = "B_ADDRESS2";
	public static final String	INVESTOR_DBF_CAMS_BANK_ADDRESS_3	 = "B_ADDRESS3";
	public static final String	INVESTOR_DBF_CAMS_BANK_CITY	 = "B_CITY";
	public static final String	INVESTOR_DBF_CAMS_BANK_PINCODE	 = "B_PINCODE";
	public static final String	INVESTOR_DBF_CAMS_INVESTOR_DATE_OF_BIRTH	 = "INV_DOB";
	public static final String	INVESTOR_DBF_CAMS_MOBILE_NUMBER	 = "MOBILE_NO";
	public static final String	INVESTOR_DBF_CAMS_OCCUPATION	 = "OCCUPATION";
	public static final String	INVESTOR_DBF_CAMS_INVESTOR_MIN	 = "INV_IIN";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_NAME	 = "NOM_NAME";
	
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_RELATION	 = "RELATION";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_ADDRESS1	 = "NOM_ADDR1";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_ADDRESS2	 = "NOM_ADDR2";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_ADDRESS3	 = "NOM_ADDR3";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_CITY	 = "NOM_CITY";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_STATE	 = "NOM_STATE";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_PINCODE	 = "NOM_PINCOD";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_PHONE_OFFICE	 = "NOM_PH_OFF";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_PHONE_RESIDENCE	 = "NOM_PH_RES";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_EMAIL	 = "NOM_EMAIL";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_PERCENTAGE	 = "NOM_PERCEN";
	
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_NAME	 = "NOM2_NAME";
	
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_RELATION	 = "NOM2_RELAT";
	public static final String	INVESTOR_DBF_CAMS_INVESTOR_DBF_FRANKLIN_NOMINEE2_ADDRESS1	 = "NOM2_ADDR1";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_ADDRESS2	 = "NOM2_ADDR2";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_ADDRESS3	 = "NOM2_ADDR3";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_CITY	 = "NOM2_CITY";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_STATE	 = "NOM2_STATE";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_PINCODE	 = "NOM2_PINCO";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_PHONE_OFFICE	 = "NOM2_PH_OF";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_PHONE_RESIDENCE	 = "NOM2_PH_RE";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_EMAIL	 = "NOM2_EMAIL";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_PERCENTAGE	 = "NOM2_PERCE";
	
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_NAME	 = "NOM3_NAME";
	
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_RELATION	 = "NOM3_RELAT";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_ADDRESS1	 = "NOM3_ADDR1";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_ADDRESS2	 = "NOM3_ADDR2";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_ADDRESS3	 = "NOM3_ADDR3";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_CITY	 = "NOM3_CITY";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_STATE	 = "NOM3_STATE";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_PINCODE	 = "NOM3_PINCO";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_PHONE_OFFICE	 = "NOM3_PH_OF";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_PHONE_RESIDENCE	 = "NOM3_PH_RE";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_EMAIL	 = "NOM3_EMAIL";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_PERCENTAGE	 = "NOM3_PERCE";
	
	public static final String	INVESTOR_DBF_CAMS_IFSC_CODE	 = "IFSC_CODE";
	public static final String	INVESTOR_DBF_CAMS_DP_ID	 = "DP_ID";
	public static final String	INVESTOR_DBF_CAMS_DEMAT	 = "DEMAT";
	public static final String	INVESTOR_DBF_CAMS_NAME_OF_GUARDIAN	 = "GUARD_NAME";
	public static final String	INVESTOR_DBF_CAMS_BROKER_DEALER_CODE	 = "BROKCODE";
	public static final String	INVESTOR_DBF_CAMS_FOLIO_CREATE_DATE	 = "FOLIO_DATE";
	public static final String	INVESTOR_DBF_CAMS_INVESTOR_AADHAAR_NUMBER	 = "AADHAAR";
	public static final String	INVESTOR_DBF_CAMS_FIRST_HOLDER_CKYC	 = "FH_CKYC_NO";
	public static final String	INVESTOR_DBF_CAMS_JOINT_HOLDER1_CKYC	 = "JH1_CKYC";
	public static final String	INVESTOR_DBF_CAMS_JOINT_HOLDER2_CKYC	 = "JH2_CKYC";
	public static final String	INVESTOR_DBF_CAMS_GUARDIAN_CKYC	 = "G_CKYC_NO";
	public static final String	INVESTOR_DBF_CAMS_AMC_CODE	 = "AMC_CODE";
	
	/*********************KARVY Investor_DBF*************************/
	
	public static final String	INVESTOR_DBF_KARVY_FOLIO_NUMBER	 = "ACNO";
	public static final String	INVESTOR_DBF_KARVY_INVESTOR_NAME	 = "INVNAME";
	public static final String	INVESTOR_DBF_KARVY_ADDRESS_LINE_1	 = "ADD1";
	public static final String	INVESTOR_DBF_KARVY_ADDRESS_LINE_2	 = "ADD2";
	public static final String	INVESTOR_DBF_KARVY_ADDRESS_LINE_3	 = "ADD3";
	public static final String	INVESTOR_DBF_KARVY_CITY	 = "CITY";
	public static final String	INVESTOR_DBF_KARVY_PINCODE	 = "PIN";
	public static final String	INVESTOR_DBF_KARVY_SCHEME_RTA_CODE	 = "PRCODE";
	public static final String	INVESTOR_DBF_KARVY_SCHEME_NAME	 = "FUNDDESC";
	/*public static final String	INVESTOR_DBF_KARVY_BALANCE_DATE	 = "REP_DATE";
	public static final String	INVESTOR_DBF_KARVY_UNIT_BALANCE	 = "CLOS_BAL";
	public static final String	INVESTOR_DBF_KARVY_RUPEE_BALANCE	 = "RUPEE_BAL";*/

	public static final String	INVESTOR_DBF_KARVY_JOINT_NAME1	 = "JTNAME1";
	public static final String	INVESTOR_DBF_KARVY_JOINT_NAME2	 = "JTNAME2";
	public static final String	INVESTOR_DBF_KARVY_PHONE_OFFICE	 = "OPHONE";
	public static final String	INVESTOR_DBF_KARVY_PHONE_RESIDENCE	 = "PH_RES1";
	public static final String	INVESTOR_DBF_KARVY_EMAIL	 = "EMAIL";
	public static final String	INVESTOR_DBF_KARVY_MODE_OF_HOLDING	 = "MODEOFHOLD";
	//public static final String	INVESTOR_DBF_KARVY_UIN	 = "UIN_NO";
	public static final String	INVESTOR_DBF_KARVY_INVESTOR_PAN	 = "PANGNO";
	public static final String	INVESTOR_DBF_KARVY_JOINT1_PAN	 = "PAN2";
	public static final String	INVESTOR_DBF_KARVY_JOINT2_PAN	 = "PAN3";
	public static final String	INVESTOR_DBF_KARVY_GUARDIAN_PAN	 = "GuardPanNo";
	public static final String	INVESTOR_DBF_KARVY_TAX_STATUS	 = "STATUS";
	public static final String	INVESTOR_DBF_KARVY_DISTRIBUTOR_ARN_CODE	 = "BROKCODE";
	/*public static final String	INVESTOR_DBF_KARVY_SUBBROKER_DEALER_CODE	 = "SUBBROKER";
	public static final String	INVESTOR_DBF_KARVY_REINV_FLAG	 = "REINV_FLAG";*/
	public static final String	INVESTOR_DBF_KARVY_BANK_NAME	 = "BNAME";
	public static final String	INVESTOR_DBF_KARVY_BANK_BRANCH	 = "BRANCH";
	public static final String	INVESTOR_DBF_KARVY_BANK_AC_TYPE	 = "BNKACTYPE";
	public static final String	INVESTOR_DBF_KARVY_BANK_AC_NO	 = "BNKACNO";
	public static final String	INVESTOR_DBF_KARVY_BANK_ADDRESS_1	 = "BADD1";
	public static final String	INVESTOR_DBF_KARVY_BANK_ADDRESS_2	 = "BADD2";
	public static final String	INVESTOR_DBF_KARVY_BANK_ADDRESS_3	 = "BADD3";
	public static final String	INVESTOR_DBF_KARVY_BANK_CITY	 = "BCITY";
	//public static final String	INVESTOR_DBF_KARVY_BANK_PINCODE	 = "B_PINCODE";
	public static final String	INVESTOR_DBF_KARVY_INVESTOR_DATE_OF_BIRTH	 = "DOB";
	public static final String	INVESTOR_DBF_KARVY_MOBILE_NUMBER	 = "MOBILE";
	/*public static final String	INVESTOR_DBF_KARVY_OCCUPATION	 = "OCCUPATION";
	public static final String	INVESTOR_DBF_KARVY_INVESTOR_MIN	 = "INV_IIN";*/
	public static final String	INVESTOR_DBF_KARVY_NOMINEE_NAME	 = "NOMINEE";
	
	/*public static final String	INVESTOR_DBF_KARVY_NOMINEE_RELATION	 = "RELATION";
	public static final String	INVESTOR_DBF_KARVY_NOMINEE_ADDRESS1	 = "NOM_ADDR1";
	public static final String	INVESTOR_DBF_KARVY_NOMINEE_ADDRESS2	 = "NOM_ADDR2";
	public static final String	INVESTOR_DBF_KARVY_NOMINEE_ADDRESS3	 = "NOM_ADDR3";
	public static final String	INVESTOR_DBF_KARVY_NOMINEE_CITY	 = "NOM_CITY";
	public static final String	INVESTOR_DBF_KARVY_NOMINEE_STATE	 = "NOM_STATE";
	public static final String	INVESTOR_DBF_KARVY_NOMINEE_PINCODE	 = "NOM_PINCOD";
	public static final String	INVESTOR_DBF_KARVY_NOMINEE_PHONE_OFFICE	 = "NOM_PH_OFF";*/
	/*public static final String	INVESTOR_DBF_CAMS_NOMINEE_PHONE_RESIDENCE	 = "NOM_PH_RES";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_EMAIL	 = "NOM_EMAIL";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE_PERCENTAGE	 = "NOM_PERCEN";*/
	
	public static final String	INVESTOR_DBF_KARVY_NOMINEE2_NAME	 = "NOMINEE2";
	
/*	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_RELATION	 = "NOM2_RELAT";
	public static final String	INVESTOR_DBF_CAMS_INVESTOR_DBF_FRANKLIN_NOMINEE2_ADDRESS1	 = "NOM2_ADDR1";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_ADDRESS2	 = "NOM2_ADDR2";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_ADDRESS3	 = "NOM2_ADDR3";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_CITY	 = "NOM2_CITY";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_STATE	 = "NOM2_STATE";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_PINCODE	 = "NOM2_PINCO";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_PHONE_OFFICE	 = "NOM2_PH_OF";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_PHONE_RESIDENCE	 = "NOM2_PH_RE";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_EMAIL	 = "NOM2_EMAIL";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE2_PERCENTAGE	 = "NOM2_PERCE";*/
	
	public static final String	INVESTOR_DBF_KARVY_NOMINEE3_NAME	 = "NOMINEE3";
	
	/*public static final String	INVESTOR_DBF_CAMS_NOMINEE3_RELATION	 = "NOM3_RELAT";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_ADDRESS1	 = "NOM3_ADDR1";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_ADDRESS2	 = "NOM3_ADDR2";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_ADDRESS3	 = "NOM3_ADDR3";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_CITY	 = "NOM3_CITY";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_STATE	 = "NOM3_STATE";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_PINCODE	 = "NOM3_PINCO";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_PHONE_OFFICE	 = "NOM3_PH_OF";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_PHONE_RESIDENCE	 = "NOM3_PH_RE";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_EMAIL	 = "NOM3_EMAIL";
	public static final String	INVESTOR_DBF_CAMS_NOMINEE3_PERCENTAGE	 = "NOM3_PERCE";*/
	
	public static final String	INVESTOR_DBF_KARVY_IFSC_CODE	 = "IFSC";
	public static final String	INVESTOR_DBF_KARVY_DP_ID	 = "DPID";
	//public static final String	INVESTOR_DBF_CAMS_DEMAT	 = "DEMAT";
	public static final String	INVESTOR_DBF_KARVY_NAME_OF_GUARDIAN	 = "GUARDIANN0";
	public static final String	INVESTOR_DBF_KARVY_BROKER_DEALER_CODE	 = "BROKCODE";
/*	public static final String	INVESTOR_DBF_CAMS_FOLIO_CREATE_DATE	 = "FOLIO_DATE";
	public static final String	INVESTOR_DBF_CAMS_INVESTOR_AADHAAR_NUMBER	 = "AADHAAR";*/
	public static final String	INVESTOR_DBF_KARVY_FIRST_HOLDER_CKYC	 = "KYC1FLAG";
	public static final String	INVESTOR_DBF_KARVY_JOINT_HOLDER1_CKYC	 = "KYC2FLAG";
	public static final String	INVESTOR_DBF_KARVY_JOINT_HOLDER2_CKYC	 = "KYC3FLAG";
	//public static final String	INVESTOR_DBF_CAMS_GUARDIAN_CKYC	 = "G_CKYC_NO";
	public static final String	INVESTOR_DBF_KARVY_AMC_CODE	 = "FUND";
	
	/********************SUNDARAM INVESTOR_DBF***********************/
	public static final String	INVESTOR_DBF_SUNDARAM_FOLIO_NUMBER	 = "FOLIO";
	public static final String	INVESTOR_DBF_SUNDARAM_INVESTOR_NAME	 = "INVNAME";
	public static final String	INVESTOR_DBF_SUNDARAM_ADDRESS_LINE_1	 = "ADDRESS1";
	public static final String	INVESTOR_DBF_SUNDARAM_ADDRESS_LINE_2	 = "ADDRESS2";
	public static final String	INVESTOR_DBF_SUNDARAM_ADDRESS_LINE_3	 = "ADDRESS3";
	public static final String	INVESTOR_DBF_SUNDARAM_CITY	 = "CITY";
	public static final String	INVESTOR_DBF_SUNDARAM_PINCODE	 = "PINCODE";
	/*
	public static final String	INVESTOR_DBF_SUNDARAM_SCHEME_RTA_CODE	 = "PRODCODE";
	public static final String	INVESTOR_DBF_SUNDARAM_SCHEME_NAME	 = "SCHEME";
	public static final String	INVESTOR_DBF_SUNDARAM_BALANCE_DATE	 = "BALDATE";
	public static final String	INVESTOR_DBF_SUNDARAM_UNIT_BALANCE	 = "UNIT_BAL";
	public static final String	INVESTOR_DBF_SUNDARAM_RUPEE_BALANCE	 = "RUPEE_BAL";
	*/
	public static final String	INVESTOR_DBF_SUNDARAM_JOINT_NAME1	 = "JOINTNAME1";
	public static final String	INVESTOR_DBF_SUNDARAM_JOINT_NAME2	 = "JOINTNAME2";
	public static final String	INVESTOR_DBF_SUNDARAM_PHONE_OFFICE	 = "PHOFF";
	public static final String	INVESTOR_DBF_SUNDARAM_PHONE_RESIDENCE	 = "PHRES";
	public static final String	INVESTOR_DBF_SUNDARAM_EMAIL	 = "EMAIL";
	public static final String	INVESTOR_DBF_SUNDARAM_MODE_OF_HOLDING	 = "HOLNAT";
	//public static final String	INVESTOR_DBF_SUNDARAM_UIN	 = "UIN";
	public static final String	INVESTOR_DBF_SUNDARAM_INVESTOR_PAN	 = "PAN";
	public static final String	INVESTOR_DBF_SUNDARAM_JOINT1_PAN	 = "JOINT1PAN";
	public static final String	INVESTOR_DBF_SUNDARAM_JOINT2_PAN	 = "JOINT2PAN";
	public static final String	INVESTOR_DBF_SUNDARAM_GUARDIAN_PAN	 = "GUARPAN";
	public static final String	INVESTOR_DBF_SUNDARAM_TAX_STATUS	 = "TAXSTAT";
	public static final String	INVESTOR_DBF_SUNDARAM_DISTRIBUTOR_ARN_CODE	 = "BROKCODE";
	public static final String	INVESTOR_DBF_SUNDARAM_SUBBROKER_DEALER_CODE	 = "SUBBRKCOD";
	//public static final String	INVESTOR_DBF_SUNDARAM_REINV_FLAG	 = "REINFLAG";
	public static final String	INVESTOR_DBF_SUNDARAM_BANK_NAME	 = "BANKNAME";
	public static final String	INVESTOR_DBF_SUNDARAM_BANK_BRANCH	 = "BANKBRA";
	//public static final String	INVESTOR_DBF_SUNDARAM_BANK_AC_TYPE	 = "ACCOUNT_11";
	public static final String	INVESTOR_DBF_SUNDARAM_BANK_AC_NO	 = "BANKACNO";
	/*
	public static final String	INVESTOR_DBF_SUNDARAM_BANK_ADDRESS_1	 = "B_ADDRESS1";
	public static final String	INVESTOR_DBF_SUNDARAM_BANK_ADDRESS_2	 = "B_ADDRESS2";
	public static final String	INVESTOR_DBF_SUNDARAM_BANK_ADDRESS_3	 = "B_ADDRESS3";
	public static final String	INVESTOR_DBF_SUNDARAM_BANK_CITY	 = "B_CITY";
	public static final String	INVESTOR_DBF_SUNDARAM_BANK_PINCODE	 = "B_PINCODE";
	*/
	public static final String	INVESTOR_DBF_SUNDARAM_INVESTOR_DATE_OF_BIRTH	 = "INVDOB";
	public static final String	INVESTOR_DBF_SUNDARAM_MOBILE_NUMBER	 = "INVMOBIL";
	//public static final String	INVESTOR_DBF_SUNDARAM_OCCUPATION	 = "		";
	//public static final String	INVESTOR_DBF_SUNDARAM_INVESTOR_MIN	 = "		";
	//public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE_NAME	 = "NOMINEE1";
	/*
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE_RELATION	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE_ADDRESS1	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE_ADDRESS2	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE_ADDRESS3	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE_CITY	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE_STATE	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE_PINCODE	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE_PHONE_OFFICE	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE_PHONE_RESIDENCE	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE_EMAIL	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE_PERCENTAGE	 = "		";
	
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE2_NAME	 = "NOMINEE2";
	
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE2_RELATION	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_INVESTOR_DBF_SUNDARAM_NOMINEE2_ADDRESS1	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE2_ADDRESS2	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE2_ADDRESS3	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE2_CITY	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE2_STATE	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE2_PINCODE	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE2_PHONE_OFFICE	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE2_PHONE_RESIDENCE	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE2_EMAIL	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE2_PERCENTAGE	 = "		";
	
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE3_NAME	 = "NOMINEE3";
	
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE3_RELATION	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE3_ADDRESS1	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE3_ADDRESS2	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE3_ADDRESS3	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE3_CITY	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE3_STATE	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE3_PINCODE	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE3_PHONE_OFFICE	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE3_PHONE_RESIDENCE	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE3_EMAIL	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NOMINEE3_PERCENTAGE	 = "		";
	
	public static final String	INVESTOR_DBF_SUNDARAM_IFSC_CODE	 = "IFSC_CODE";
	public static final String	INVESTOR_DBF_SUNDARAM_DP_ID	 = "DP_ID";
	public static final String	INVESTOR_DBF_SUNDARAM_DEMAT	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_NAME_OF_GUARDIAN	 = "GUARDIAN20";
	public static final String	INVESTOR_DBF_SUNDARAM_BROKER_DEALER_CODE	 = "BROK_CODE";
	public static final String	INVESTOR_DBF_SUNDARAM_FOLIO_CREATE_DATE	 = "CREA_DATE";
	public static final String	INVESTOR_DBF_SUNDARAM_INVESTOR_AADHAAR_NUMBER	 = "		";
	public static final String	INVESTOR_DBF_SUNDARAM_FIRST_HOLDER_CKYC	 = "CKYC_ID";
	public static final String	INVESTOR_DBF_SUNDARAM_JOINT_HOLDER1_CKYC	 = "H1_CKYC_ID";
	public static final String	INVESTOR_DBF_SUNDARAM_JOINT_HOLDER2_CKYC	 = "H2_CKYC_ID";
	public static final String	INVESTOR_DBF_SUNDARAM_GUARDIAN_CKYC	 = "G_CKYC_ID";
	public static final String	INVESTOR_DBF_SUNDARAM_AMC_CODE	 = "COMP_CODE";
	/********************REJECTION Columns ***************************************************************************************************************************************/

	/*********************CAMS REJECTION_DBF*************************/
	public static final String	REJECTION_DBF_CAMS_FOLIO_NUMBER	 = "FOLIO_NUMB";
	public static final String	REJECTION_DBF_CAMS_INVESTOR_NAME	 = "INVESTOR_N";
	public static final String	REJECTION_DBF_CAMS_SCHEME_RTA_CODE	 = "PRODUCT_CO";
	public static final String	REJECTION_DBF_CAMS_AMOUNT	 = "AMOUNT";
	public static final String	REJECTION_DBF_CAMS_REMARKS	 = "REMARKS";
	public static final String	REJECTION_DBF_CAMS_TRANSACTION_NUMBER	 = "USER_TRXNN";
	public static final String	REJECTION_DBF_CAMS_SCHEME_CODE	 = "SCHEME_COD";
	public static final String	REJECTION_DBF_CAMS_TRANSACTION_TYPE	 = "TRXN_TYPE";
	public static final String	REJECTION_DBF_CAMS_TRANSACTION_DATE	 = "ENTRY_DATE";
	public static final String	REJECTION_DBF_CAMS_INSTRUMENT_NUMBER	 = "INSTRM_NO";
	
	/*********************KARVY REJECTION_DBF*************************/
	
	public static final String	REJECTION_DBF_KARVY_FOLIO_NUMBER	 ="TD_ACNO";
	public static final String	REJECTION_DBF_KARVY_INVESTOR_NAME	 ="INVNAME";
	public static final String	REJECTION_DBF_KARVY_SCHEME_RTA_CODE	 ="FMCODE";
	public static final String	REJECTION_DBF_KARVY_AMOUNT	 ="TD_AMT";
	public static final String	REJECTION_DBF_KARVY_REMARKS	 ="NCTREMARKS";
	public static final String	REJECTION_DBF_KARVY_TRANSACTION_NUMBER	 ="TD_TRNO";
	public static final String	REJECTION_DBF_KARVY_SCHEME_CODE	 ="SCHPLN";
	public static final String	REJECTION_DBF_KARVY_TRANSACTION_TYPE	 ="TD_PURRED";
	public static final String	REJECTION_DBF_KARVY_TRANSACTION_DATE	 ="TD_TRDT";
	public static final String	REJECTION_DBF_KARVY_INSTRUMENT_NUMBER	 ="CHQNO";	
	
	/*********************FRANKLIN REJECTION_DBF*************************/
	public static final String	REJECTION_DBF_FRANKLIN_FOLIO_NUMBER	 ="FOLIO_ID";
	public static final String	REJECTION_DBF_FRANKLIN_INVESTOR_NAME	 ="CUSTOMER_3";
	public static final String	REJECTION_DBF_FRANKLIN_SCHEME_RTA_CODE	 ="PROD_CODE";
	public static final String	REJECTION_DBF_FRANKLIN_AMOUNT	 ="AMOUNT";
	public static final String	REJECTION_DBF_FRANKLIN_REMARKS	 ="REJECT_RE5";
	public static final String	REJECTION_DBF_FRANKLIN_TRANSACTION_NUMBER	 ="TXN_NO";
	public static final String	REJECTION_DBF_FRANKLIN_SCHEME_CODE	 ="SCHEME_C11";
	public static final String	REJECTION_DBF_FRANKLIN_TRANSACTION_TYPE	 ="TRAN_TYPE";
	public static final String	REJECTION_DBF_FRANKLIN_TRANSACTION_DATE	 ="TRANS_DATE";
	public static final String	REJECTION_DBF_FRANKLIN_INSTRUMENT_NUMBER	 ="CHEQUE_NO";

	/*********************CAMS REJECTION_EXCEL*************************/
	public static final String	REJECTION_EXCEL_CAMS_FOLIO_NUMBER	 ="folio_number";
	public static final String	REJECTION_EXCEL_CAMS_INVESTOR_NAME	 ="investor_name";
	public static final String	REJECTION_EXCEL_CAMS_SCHEME_RTA_CODE	 ="product_code";
	public static final String	REJECTION_EXCEL_CAMS_AMOUNT	 ="amount";
	public static final String	REJECTION_EXCEL_CAMS_REMARKS	 ="remarks";
	public static final String	REJECTION_EXCEL_CAMS_TRANSACTION_NUMBER	 ="user_trxnno";
	public static final String	REJECTION_EXCEL_CAMS_SCHEME_CODE	 ="scheme_code";
	public static final String	REJECTION_EXCEL_CAMS_TRANSACTION_TYPE	 ="trxn_type";
	public static final String	REJECTION_EXCEL_CAMS_TRANSACTION_DATE	 ="entry_date";
	public static final String	REJECTION_EXCEL_CAMS_INSTRUMENT_NUMBER	 ="instrm_no";
	public static final String	REJECTION_EXCEL_CAMS_PAN	 ="pan";

	
	/*********************KARVY REJECTION_EXCEL*************************/
	public static final String	REJECTION_EXCEL_KARVY_FOLIO_NUMBER	 ="Folio Number";
	public static final String	REJECTION_EXCEL_KARVY_INVESTOR_NAME	 ="Investor Name";
	public static final String	REJECTION_EXCEL_KARVY_SCHEME_RTA_CODE	 ="Product Code";
	public static final String	REJECTION_EXCEL_KARVY_AMOUNT	 ="Amount";
	public static final String	REJECTION_EXCEL_KARVY_REMARKS	 ="Remarks";
	public static final String	REJECTION_EXCEL_KARVY_TRANSACTION_NUMBER	 ="Transaction Number";
	public static final String	REJECTION_EXCEL_KARVY_SCHEME_CODE	 ="Scheme Code";
	public static final String	REJECTION_EXCEL_KARVY_TRANSACTION_TYPE	 ="Transaction Head";
	public static final String	REJECTION_EXCEL_KARVY_TRANSACTION_DATE	 ="Transaction Date";
	public static final String	REJECTION_EXCEL_KARVY_INSTRUMENT_NUMBER	 ="Instrument Number";
	public static final String	REJECTION_EXCEL_KARVY_POSTED_DATE	 = "Process Date";

	/*********************FRANKLIN REJECTION_EXCEL*************************/
	
	public static final String	REJECTION_EXCEL_FRANKLIN_FOLIO_NUMBER	 ="Folio_ID";
	public static final String	REJECTION_EXCEL_FRANKLIN_INVESTOR_NAME	 ="Customer_Name";
	public static final String	REJECTION_EXCEL_FRANKLIN_SCHEME_RTA_CODE	 ="Prod_Code";
	public static final String	REJECTION_EXCEL_FRANKLIN_AMOUNT	 ="Amount";
	public static final String	REJECTION_EXCEL_FRANKLIN_REMARKS	 ="Reject_Reason";
	public static final String	REJECTION_EXCEL_FRANKLIN_TRANSACTION_NUMBER	 ="TXN_No";
	public static final String	REJECTION_EXCEL_FRANKLIN_SCHEME_CODE	 ="Scheme_Code";
	public static final String	REJECTION_EXCEL_FRANKLIN_TRANSACTION_TYPE	 ="Trans_Type";
	public static final String	REJECTION_EXCEL_FRANKLIN_TRANSACTION_DATE	 ="Trans_Date";
	public static final String	REJECTION_EXCEL_FRANKLIN_INSTRUMENT_NUMBER	 ="Cheque_No";
	public static final String	REJECTION_EXCEL_FRANKLIN_APPLICATION_NUMBER	 ="Application#";

	public static final String	REJECTION_EXCEL_FRANKLIN_ADDRESS1	 ="Address1";
	public static final String	REJECTION_EXCEL_FRANKLIN_ADDRESS2	 ="Address2";
	public static final String	REJECTION_EXCEL_FRANKLIN_ADDRESS3	 ="Address3";
	public static final String	REJECTION_EXCEL_FRANKLIN_CITY	 ="City";
	public static final String	REJECTION_EXCEL_FRANKLIN_PIN	 ="Pin_Code";
	public static final String	REJECTION_EXCEL_FRANKLIN_MOBILE	 ="Mobile_No";
	public static final String	REJECTION_EXCEL_FRANKLIN_EMAIL	 ="Email_Id";
	public static final String	REJECTION_EXCEL_FRANKLIN_BRANCH	 ="Branch_Name";


/********************BROKERAGE Columns ***************************************************************************************************************************************/
    /*********************CAMS BROKERAGE_EXCEL*************************/
    public static final String 	BROKERAGE_EXCEL_CAMS_SCHEME_RTA_CODE	 = "scheme_code";
    //public static final String 	BROKERAGE_EXCEL_CAMS_SCHEME_NAME	 = "		";
    public static final String 	BROKERAGE_EXCEL_CAMS_AMC_CODE	 = "amc_code";
    public static final String 	BROKERAGE_EXCEL_CAMS_FOLIO_NUMBER	 = "folio_no";
    public static final String 	BROKERAGE_EXCEL_CAMS_INVESTOR_NAME	 = "inv_name";
    public static final String 	BROKERAGE_EXCEL_CAMS_TRANSACTION_TYPE	 = "trxn_type";
    public static final String 	BROKERAGE_EXCEL_CAMS_FROM_DATE	 = "brkage_from";
    public static final String 	BROKERAGE_EXCEL_CAMS_TO_DATE	 = "brkage_to";
    //public static final String 	BROKERAGE_EXCEL_CAMS_AMOUNT	 = "		";
    public static final String 	BROKERAGE_EXCEL_CAMS_TRANSACTION_DATE	 = "trade_date_time";
    public static final String 	BROKERAGE_EXCEL_CAMS_PROCESS_DATE	 = "proc_date";
    public static final String 	BROKERAGE_EXCEL_CAMS_BROKERAGE_PERCENTAGE	 = "brkage_rate";
    public static final String 	BROKERAGE_EXCEL_CAMS_BROKERAGE_AMOUNT	 = "brkage_amt";
    public static final String 	BROKERAGE_EXCEL_CAMS_BROKERAGE_TYPE	 = "brkage_type";
    public static final String 	BROKERAGE_EXCEL_CAMS_TRANSACTION_NUMBER	 = "trxn_no";

    /*********************KARVY BROKERAGE_EXCEL*************************/

    public static final String	BROKERAGE_EXCEL_KARVY_SCHEME_RTA_CODE	 = "Product Code";
    public static final String	BROKERAGE_EXCEL_KARVY_SCHEME_NAME	 = "Fund Description";
    public static final String	BROKERAGE_EXCEL_KARVY_AMC_CODE	 = "Fund";
    public static final String	BROKERAGE_EXCEL_KARVY_FOLIO_NUMBER	 = "Account Number";
    public static final String	BROKERAGE_EXCEL_KARVY_INVESTOR_NAME	 = "Investor Name";
    public static final String	BROKERAGE_EXCEL_KARVY_TRANSACTION_TYPE	 = "Transaction Description";
    public static final String	BROKERAGE_EXCEL_KARVY_FROM_DATE	 = "From Date";
    public static final String	BROKERAGE_EXCEL_KARVY_TO_DATE	 = "To Date";
    public static final String	BROKERAGE_EXCEL_KARVY_AMOUNT	 = "Amount (in Rs.)";
    public static final String	BROKERAGE_EXCEL_KARVY_TRANSACTION_DATE	 = "Transaction Date";
    public static final String	BROKERAGE_EXCEL_KARVY_PROCESS_DATE	 = "Process Date";
    public static final String	BROKERAGE_EXCEL_KARVY_BROKERAGE_PERCENTAGE	 = "Percentage (%)";
    public static final String	BROKERAGE_EXCEL_KARVY_BROKERAGE_AMOUNT	 = "Brokerage (in Rs.)";
    public static final String	BROKERAGE_EXCEL_KARVY_BROKERAGE_TYPE	 = "Brokerage Head";
    public static final String	BROKERAGE_EXCEL_KARVY_TRANSACTION_NUMBER	 = "Transaction Number";

    /*********************FRANKLIN BROKERAGE_EXCEL*************************/

    public static final String	BROKERAGE_EXCEL_FRANKLIN_SCHEME_RTA_CODE	 = "PRODCODE";
    //public static final String	BROKERAGE_EXCEL_FRANKLIN_SCHEME_NAME	 = "		";
    //public static final String	BROKERAGE_EXCEL_FRANKLIN_AMC_CODE	 = "		";
    public static final String	BROKERAGE_EXCEL_FRANKLIN_FOLIO_NUMBER	 = "ACCOUNTNO";
    public static final String	BROKERAGE_EXCEL_FRANKLIN_INVESTOR_NAME	 = "INVNAME";
    public static final String	BROKERAGE_EXCEL_FRANKLIN_TRANSACTION_TYPE	 = "TXNTYPE";
    public static final String	BROKERAGE_EXCEL_FRANKLIN_FROM_DATE	 = "FRDATE";
    public static final String	BROKERAGE_EXCEL_FRANKLIN_TO_DATE	 = "TODATE";
    public static final String	BROKERAGE_EXCEL_FRANKLIN_AMOUNT	 = "TXNAMOUNT";
    public static final String	BROKERAGE_EXCEL_FRANKLIN_TRANSACTION_DATE	 = "TXNDATE";
    public static final String	BROKERAGE_EXCEL_FRANKLIN_PROCESS_DATE	 = "PRDATE";
    public static final String	BROKERAGE_EXCEL_FRANKLIN_BROKERAGE_PERCENTAGE	 = "PER";
    public static final String	BROKERAGE_EXCEL_FRANKLIN_BROKERAGE_AMOUNT	 = "BROKERAGE";
    public static final String	BROKERAGE_EXCEL_FRANKLIN_BROKERAGE_TYPE	 = "REMARKS";
    public static final String	BROKERAGE_EXCEL_FRANKLIN_TRANSACTION_NUMBER	 = "TXNNO";
	/*XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

	/*********************CAMS BROKERAGE_DBF*************************/
    
    public static final String	BROKERAGE_DBF_CAMS_SCHEME_RTA_CODE	 = "SCHEME_COD";
    //public static final String	BROKERAGE_DBF_CAMS_SCHEME_NAME	 = "FUNDDESC";
    public static final String	BROKERAGE_DBF_CAMS_AMC_CODE	 = "AMC_CODE";
    public static final String	BROKERAGE_DBF_CAMS_FOLIO_NUMBER	 = "FOLIO_NO";
    public static final String	BROKERAGE_DBF_CAMS_INVESTOR_NAME	 = "INV_NAME";
    public static final String	BROKERAGE_DBF_CAMS_TRANSACTION_TYPE	 = "TRXN_TYPE";
    public static final String	BROKERAGE_DBF_CAMS_FROM_DATE	 = "PROC_FROM_";
    public static final String	BROKERAGE_DBF_CAMS_TO_DATE	 = "PROC_TO_DA";
    public static final String	BROKERAGE_DBF_CAMS_AMOUNT	 = "PLOT_AMOUN";
    public static final String	BROKERAGE_DBF_CAMS_TRANSACTION_DATE	 = "TRADE_DATE";
    public static final String	BROKERAGE_DBF_CAMS_PROCESS_DATE	 = "PROC_DATE";
    public static final String	BROKERAGE_DBF_CAMS_BROKERAGE_PERCENTAGE	 = "BRKAGE_RAT";
    public static final String	BROKERAGE_DBF_CAMS_BROKERAGE_AMOUNT	 = "BRKAGE_AMT";
    public static final String	BROKERAGE_DBF_CAMS_BROKERAGE_TYPE	 = "CLW_TYPE";
    public static final String	BROKERAGE_DBF_CAMS_TRANSACTION_NUMBER	 = "TRXN_NO";
    
    /*********************SUNDARAM BROKERAGE_DBF*************************/
    
    public static final String	BROKERAGE_DBF_SUNDARAM_SCHEME_RTA_CODE	 = "PRODUCT";
    public static final String	BROKERAGE_DBF_SUNDARAM_SCHEME_NAME	 = "SCHNAME";
    public static final String	BROKERAGE_DBF_SUNDARAM_AMC_CODE	 = "AMCCODE";
    public static final String	BROKERAGE_DBF_SUNDARAM_FOLIO_NUMBER	 = "FOLIOCHK";
    public static final String	BROKERAGE_DBF_SUNDARAM_INVESTOR_NAME	 = "INVNAME";
    public static final String	BROKERAGE_DBF_SUNDARAM_TRANSACTION_TYPE	 = "TRXNTYPE";
    public static final String	BROKERAGE_DBF_SUNDARAM_FROM_DATE	 = "TFFROMDA";
    public static final String	BROKERAGE_DBF_SUNDARAM_TO_DATE	 = "TFTODATE";
    public static final String	BROKERAGE_DBF_SUNDARAM_AMOUNT	 = "AMOUNT";
    public static final String	BROKERAGE_DBF_SUNDARAM_TRANSACTION_DATE	 = "TRADATE";
    public static final String	BROKERAGE_DBF_SUNDARAM_PROCESS_DATE	 = "PROCDATE";
    public static final String	BROKERAGE_DBF_SUNDARAM_BROKERAGE_PERCENTAGE	 = "RATE";
    public static final String	BROKERAGE_DBF_SUNDARAM_BROKERAGE_AMOUNT	 = "FEEAMT";
    public static final String	BROKERAGE_DBF_SUNDARAM_BROKERAGE_TYPE	 = "FEETYPE";
    public static final String	BROKERAGE_DBF_SUNDARAM_TRANSACTION_NUMBER	 = "TRXNID";
    
    /*********************KARVY BROKERAGE_DBF*************************/

    public static final String	BROKERAGE_DBF_KARVY_SCHEME_RTA_CODE	 = "PRCODE";
    public static final String	BROKERAGE_DBF_KARVY_SCHEME_NAME	 = "FUNDDESC";
    public static final String	BROKERAGE_DBF_KARVY_AMC_CODE	 = "FUND";
    public static final String	BROKERAGE_DBF_KARVY_FOLIO_NUMBER	 = "ACCOUNTNO";
    public static final String	BROKERAGE_DBF_KARVY_INVESTOR_NAME	 = "INVESTORN0";
    public static final String	BROKERAGE_DBF_KARVY_TRANSACTION_TYPE	 = "TRANSACTI1";
    public static final String	BROKERAGE_DBF_KARVY_FROM_DATE	 = "FROMDATE";
    public static final String	BROKERAGE_DBF_KARVY_TO_DATE	 = "TODATE";
    public static final String	BROKERAGE_DBF_KARVY_AMOUNT	 = "AMOUNT";
    public static final String	BROKERAGE_DBF_KARVY_TRANSACTION_DATE	 = "TRANSACTI2";
    public static final String	BROKERAGE_DBF_KARVY_PROCESS_DATE	 = "PROCESSDA3";
    public static final String	BROKERAGE_DBF_KARVY_BROKERAGE_PERCENTAGE	 = "PERCENTAGE";
    public static final String	BROKERAGE_DBF_KARVY_BROKERAGE_AMOUNT	 = "BROKERAGE";
    public static final String	BROKERAGE_DBF_KARVY_BROKERAGE_TYPE	 = "BROKTYPE";
    public static final String	BROKERAGE_DBF_KARVY_TRANSACTION_NUMBER	 = "TRNO";

    /*********************FRANKLIN BROKERAGE_DBF*************************/
    public static final String	BROKERAGE_DBF_FRANKLIN_SCHEME_RTA_CODE	 = "PRODCODE";
    //public static final String	BROKERAGE_EXCEL_FRANKLIN_SCHEME_NAME	 = "		";
    //public static final String	BROKERAGE_EXCEL_FRANKLIN_AMC_CODE	 = "		";
    public static final String	BROKERAGE_DBF_FRANKLIN_FOLIO_NUMBER	 = "ACCOUNTNO";
    public static final String	BROKERAGE_DBF_FRANKLIN_INVESTOR_NAME	 = "INVNAME";
    public static final String	BROKERAGE_DBF_FRANKLIN_TRANSACTION_TYPE	 = "TXNTYPE";
    public static final String	BROKERAGE_DBF_FRANKLIN_FROM_DATE	 = "FRDATE";
    public static final String	BROKERAGE_DBF_FRANKLIN_TO_DATE	 = "TODATE";
    public static final String	BROKERAGE_DBF_FRANKLIN_AMOUNT	 = "TXNAMOUNT";
    public static final String	BROKERAGE_DBF_FRANKLIN_TRANSACTION_DATE	 = "TXNDATE";
    public static final String	BROKERAGE_DBF_FRANKLIN_PROCESS_DATE	 = "PRDATE";
    public static final String	BROKERAGE_DBF_FRANKLIN_BROKERAGE_PERCENTAGE	 = "PER";
    public static final String	BROKERAGE_DBF_FRANKLIN_BROKERAGE_AMOUNT	 = "BROKERAGE";
    public static final String	BROKERAGE_DBF_FRANKLIN_BROKERAGE_TYPE	 = "REMARKS";
    public static final String	BROKERAGE_DBF_FRANKLIN_TRANSACTION_NUMBER	 = "TXNNO";
    /********************************************************************************************************************************************************/
    
    /**********************AUM FEED*************************************/
    
    public static final String  AUM_SCHEME_RTA_CODE = "3";
	public static final String	AUM_AMC_CODE = "12";			
	public static final String	AUM_FOLIO_NUMBER = "8";
	public static final String	AUM_SCHEME_NAME = "4";
	public static final String	AUM_UNIT_BALANCE = "1";
	public static final String	AUM_DISTRIBUTOR_ARN_CODE = "9";
	public static final String	AUM_SUB_BROKER_CODE = "2";
	public static final String	AUM_INVESTOR_NAME = "7";
	public static final String	AUM_PINCODE =	"10";		
	public static final String	AUM_CURRENT_VALUE = "11";
	public static final String	AUM_NAV = "6";
	public static final String	AUM_REPORT_DATE = "5";
    
	/*********************INVESTOR FEED**********************************/
	
	public static final String	INVESTOR_FOLIO_NUMBER	 = "13";
	public static final String	INVESTOR_INVESTOR_NAME	 = "14";
	public static final String	INVESTOR_ADDRESS_LINE_1	 = "15";
	public static final String	INVESTOR_ADDRESS_LINE_2	 = "16";
	public static final String	INVESTOR_ADDRESS_LINE_3	 = "17";
	public static final String	INVESTOR_CITY	 = "18";
	public static final String	INVESTOR_PINCODE	 = "19";
	public static final String	INVESTOR_SCHEME_RTA_CODE	 = "22";
	public static final String	INVESTOR_SCHEME_NAME	 = "23";
	/*
	public static final String	INVESTOR_BALANCE_DATE	 = "29";
	public static final String	INVESTOR_UNIT_BALANCE	 = "30";
	public static final String	INVESTOR_RUPEE_BALANCE	 = "31";
	*/
	public static final String	INVESTOR_JOINT_NAME1	 = "20";
	public static final String	INVESTOR_JOINT_NAME2	 = "21";
	public static final String	INVESTOR_PHONE_OFFICE	 = "24";
	public static final String	INVESTOR_PHONE_RESIDENCE	 = "25";
	public static final String	INVESTOR_EMAIL	 = "26";
	public static final String	INVESTOR_MODE_OF_HOLDING	 = "27";
	public static final String	INVESTOR_UIN	 = "32";
	public static final String	INVESTOR_INVESTOR_PAN	 = "28";
	public static final String	INVESTOR_JOINT1_PAN	 = "33";
	public static final String	INVESTOR_JOINT2_PAN	 = "34";
	public static final String	INVESTOR_GUARDIAN_PAN	 = "35";
	public static final String	INVESTOR_TAX_STATUS	 = "36";
	public static final String	INVESTOR_DISTRIBUTOR_ARN_CODE	 = "37";
	/*
	public static final String	INVESTOR_EXCEL_CAMS_SUBBROKER_DEALER_CODE	 = "38";
	public static final String	INVESTOR_EXCEL_CAMS_REINV_FLAG	 = "39";
	*/
	public static final String	INVESTOR_BANK_NAME	 = "40";
	public static final String	INVESTOR_BANK_BRANCH	 = "41";
	public static final String	INVESTOR_BANK_AC_TYPE	 = "42";
	public static final String	INVESTOR_BANK_AC_NO	 = "43";
	public static final String	INVESTOR_BANK_ADDRESS_1	 = "44";
	public static final String	INVESTOR_BANK_ADDRESS_2	 = "45";
	public static final String	INVESTOR_BANK_ADDRESS_3	 = "46";
	public static final String	INVESTOR_BANK_CITY	 = "47";
	public static final String	INVESTOR_BANK_PINCODE	 = "48";
	public static final String	INVESTOR_INVESTOR_DATE_OF_BIRTH	 = "49";
	public static final String	INVESTOR_MOBILE_NUMBER	 = "50";
	public static final String	INVESTOR_OCCUPATION	 = "51";
	public static final String	INVESTOR_INVESTOR_MIN	 = "52";
	public static final String	INVESTOR_NOMINEE_NAME	 = "53";
	/*
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_RELATION	 = "54";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_ADDRESS1	 = "55";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_ADDRESS2	 = "56";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_ADDRESS3	 = "57";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_CITY	 = "58";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_STATE	 = "59";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_PINCODE	 = "60";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_PHONE_OFFICE	 = "61";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_PHONE_RESIDENCE	 = "62";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_EMAIL	 = "63";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE_PERCENTAGE	 = "64";
	*/
	public static final String	INVESTOR_NOMINEE2_NAME	 = "65";
	/*
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_RELATION	 = "66";
	public static final String	INVESTOR_EXCEL_CAMS_INVESTOR_EXCEL_CAMS_NOMINEE2_ADDRESS1	 = "68";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_ADDRESS2	 = "NOM2_ADDR2";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_ADDRESS3	 = "NOM2_ADDR3";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_CITY	 = "NOM2_CITY";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_STATE	 = "NOM2_STATE";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_PINCODE	 = "NOM2_PINCO";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_PHONE_OFFICE	 = "NOM2_PH_OF";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_PHONE_RESIDENCE	 = "NOM2_PH_RE";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_EMAIL	 = "NOM2_EMAIL";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE2_PERCENTAGE	 = "NOM2_PERCE";
	*/
	public static final String	INVESTOR_NOMINEE3_NAME	 = "77";
	/*
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_RELATION	 = "NOM3_RELAT";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_ADDRESS1	 = "NOM3_ADDR1";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_ADDRESS2	 = "NOM3_ADDR2";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_ADDRESS3	 = "NOM3_ADDR3";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_CITY	 = "NOM3_CITY";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_STATE	 = "NOM3_STATE";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_PINCODE	 = "NOM3_PINCO";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_PHONE_OFFICE	 = "NOM3_PH_OF";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_PHONE_RESIDENCE	 = "NOM3_PH_RE";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_EMAIL	 = "NOM3_EMAIL";
	public static final String	INVESTOR_EXCEL_CAMS_NOMINEE3_PERCENTAGE	 = "NOM3_PERCE";
	*/
	public static final String	INVESTOR_IFSC_CODE	 = "89";
	public static final String	INVESTOR_DP_ID	 = "90";
	public static final String	INVESTOR_DEMAT	 = "91";
	public static final String	INVESTOR_NAME_OF_GUARDIAN	 = "92";
	public static final String	INVESTOR_BROKER_DEALER_CODE	 = "93";
	public static final String	INVESTOR_FOLIO_CREATE_DATE	 = "94";
	public static final String	INVESTOR_INVESTOR_AADHAAR_NUMBER	 = "95";
	public static final String	INVESTOR_FIRST_HOLDER_CKYC	 = "97";
	public static final String	INVESTOR_JOINT_HOLDER1_CKYC	 = "98";
	public static final String	INVESTOR_JOINT_HOLDER2_CKYC	 = "99";
	public static final String	INVESTOR_GUARDIAN_CKYC	 = "100";
	public static final String	INVESTOR_AMC_CODE	 = "104";
	
	/*********************TRANSACTION FEED**********************************/
	
	public static final String	TRANSACTION_AMC_CODE	 = 	"107";
	public static final String	TRANSACTION_FOLIO_NUMBER = 	"108";
	public static final String	TRANSACTION_SCHEME_RTA_CODE	 = 	"109";
	public static final String	TRANSACTION_SCHEME_NAME	 = 	"110";
	public static final String	TRANSACTION_INVESTOR_NAME	 = 	"111";
	public static final String	TRANSACTION_TRANSACTION_TYPE_CODE	 = 	"112";
	public static final String	TRANSACTION_TRANSACTION_NUMBER	 = 	"106";
	public static final String	TRANSACTION_TRANSACTION_MODE	 = 	"113";
	public static final String	TRANSACTION_TRANSACTION_STATUS	 = 	"114";
	public static final String	TRANSACTION_TRANSACTION_DATE	 = 	"115";
	public static final String	TRANSACTION_PROCESS_DATE	 = 	"116";
	public static final String	TRANSACTION_NAV	 = 	"117";
	public static final String	TRANSACTION_UNITS_OF_THE_TRANSACTION	 = 	"118";
	public static final String	TRANSACTION_AMOUNT_OF_THE_TRANSACTION	 = 	"119";
	public static final String	TRANSACTION_DISTRIBUTOR_ARN_CODE	 = 	"120";
	public static final String	TRANSACTION_SUB_BROKER_CODE	 = 	"121";
	public static final String	TRANSACTION_BROKERAGE_PERCENTAGE	 = 	"122";
	public static final String	TRANSACTION_BROKERAGE_AMOUNT	 = 	"123";
	public static final String	TRANSACTION_INVESTOR_ID	 = 	"124";
	public static final String	TRANSACTION_REPORT_DATE	 = 	"125";
	public static final String	TRANSACTION_REPORT_TIME	 = 	"126";
	public static final String	TRANSACTION_APPLICATION_NUMBER	 = 	"127";
	public static final String	TRANSACTION_TRANS_TYPE	 = 	"128";
	public static final String	TRANSACTION_TDS_AMOUNT	 = 	"129";
	public static final String	TRANSACTION_SECURITY_TRANSACTION_TAX	 = 	"";
	public static final String	TRANSACTION_INVESTOR_PAN	 = 	"145";
	public static final String	TRANSACTION_TXN_TYPE	 = 	"148";
	public static final String	TRANSACTION_TRANSACTION_CHARGES	 = 	"153";
	public static final String	TRANSACTION_TRANSACTION_SUFFIX	 = 	"156";
	public static final String	TRANSACTION_LOCATION_CATEGORY	 = 	"158";
	public static final String	TRANSACTION_EUIN	 = 	"159";
	public static final String	TRANSACTION_EUIN_VALID_INDICATOR	 = 	"160";
	public static final String	TRANSACTION_SUB_BROKER_ARN	 = 	"162";
	public static final String	TRANSACTION_BANK_NAME	 = 	"167";
	public static final String	TRANSACTION_DESCRIPTION	 = 	"258";
	public static final String	TRANSACTION_DESCRIPTION_CODE	 = 	"259";
	
	/*********************SIP STP FEED**********************************/
	
	public static final String SIPSTP_SCHEME_RTA_CODE = "178";
	public static final String SIPSTP_SCHEME_NAME = "179";
	public static final String SIPSTP_FOLIO_NUMBER = "180";
	public static final String SIPSTP_INVESTOR_NAME = "181";
	public static final String SIPSTP_TRANSACTION_TYPE ="182";
	public static final String SIPSTP_TRANSACTION_NUMBER ="183";
	public static final String SIPSTP_AMOUNT ="184";
	public static final String SIPSTP_FROM_DATE ="185";
	public static final String SIPSTP_TO_DATE ="186";
	public static final String SIPSTP_TERMINATION_DATE = "187";
	public static final String SIPSTP_FREQUENCY="188";
	public static final String SIPSTP_TARGET_SCHEME="192";
	public static final String SIPSTP_REGISTRATION_DATE="193";
	public static final String SIPSTP_SUB_BROKER_CODE="194";
	public static final String SIPSTP_INVESTOR_PAN="204";
	public static final String SIPSTP_LOCATION_CATEGORY="208";
	public static final String SIPSTP_AMC_CODE="210";
	public static final String SIPSTP_TARGET_SCHEME_RTA_CODE="256";
	
    /******************************************************REJECTION FEED***************************************************************/
	public static final String	REJECTION_FOLIO_NUMBER	 ="215";
	public static final String	REJECTION_INVESTOR_NAME	 ="238";
	public static final String	REJECTION_SCHEME_RTA_CODE	 ="224";
	public static final String	REJECTION_AMOUNT	 ="225";
	public static final String	REJECTION_REMARKS	 ="226";
	public static final String	REJECTION_TRANSACTION_NUMBER	 ="229";
	public static final String	REJECTION_SCHEME_CODE	 ="230";
	public static final String	REJECTION_TRANSACTION_TYPE	 ="231";
	public static final String	REJECTION_TRANSACTION_DATE	 ="234";
	public static final String	REJECTION_INSTRUMENT_NUMBER	 ="235";
	public static final String	REJECTION_PAN	 ="240";
	
	/******************************************************BROKERAGE FEED***************************************************************/
	
	public static final String 	BROKERAGE_SCHEME_RTA_CODE	 = "241";
    public static final String 	BROKERAGE_SCHEME_NAME	 = "242";
    public static final String 	BROKERAGE_AMC_CODE	 = "243";
    public static final String 	BROKERAGE_FOLIO_NUMBER	 = "244";
    public static final String 	BROKERAGE_INVESTOR_NAME	 = "245";
    public static final String 	BROKERAGE_TRANSACTION_TYPE	 = "246";
    public static final String 	BROKERAGE_FROM_DATE	 = "247";
    public static final String 	BROKERAGE_TO_DATE	 = "248";
    public static final String 	BROKERAGE_AMOUNT	 = "249";
    public static final String 	BROKERAGE_TRANSACTION_DATE	 = "250";
    public static final String 	BROKERAGE_PROCESS_DATE	 = "251";
    public static final String 	BROKERAGE_BROKERAGE_PERCENTAGE	 = "252";
    public static final String 	BROKERAGE_BROKERAGE_AMOUNT	 = "253";
    public static final String 	BROKERAGE_BROKERAGE_TYPE	 = "254";
    public static final String 	BROKERAGE_TRANSACTION_NUMBER	 = "255";
	
    /****************************************************************************************************************************************************************/
    							/*MULTIPLE FIELDS REQUIRED FOR FILE REJECTION*/ 
	public static final String	INVESTOR_EXCEL_KARVY_MOBILE_NUMBER1	 = "Mobile";
	public static final String	SIPSTP_KARVY_EXCEL_SCHEME_NAME1 = "Scheme Name";
	public static final String SIPSTP_KARVY_EXCEL_TRANSACTION_NUMBER1 ="Ihno";
    /*************************************************************************************************************************/
    /**************************************** S T A T I C    M A P***********************************************************/
    
    public static final Map<String, String> investorCAMSColNameMap = new HashMap<String, String>();
    static {
    	investorCAMSColNameMap.put("holdingMode1", "HOLDING_NA");
    	investorCAMSColNameMap.put("holdingMode2", "holding_nature");
    	investorCAMSColNameMap.put("distributorARNCode1", "BROKER_COD");
    	investorCAMSColNameMap.put("distributorARNCode2", "broker_code");
    }
    public static final Map<String, String> investorKARVYColNameMap = new HashMap<String, String>();
    static {
    	investorKARVYColNameMap.put("mobileNumber1", "Mobile");
    	investorKARVYColNameMap.put("mobileNumber2", "Mobile Number");
    	
    }
    public static final Map<String, String> transactionSUNDARAMDBFColNameMap = new HashMap<String, String>();
    static {
    	investorKARVYColNameMap.put("mobileNumber1", "Mobile");
    	investorKARVYColNameMap.put("mobileNumber2", "Mobile Number");
    	
    }
    /**************************************** S T A T I C    L I S T***********************************************************/
    /******************************************************AUM***************************************************************/
    public static final List<String> AUM_DB_COLUMN_ID_LIST = new ArrayList<String>();
    static {
    	AUM_DB_COLUMN_ID_LIST.add("8");
    	AUM_DB_COLUMN_ID_LIST.add("3");
    	AUM_DB_COLUMN_ID_LIST.add("5");
    	AUM_DB_COLUMN_ID_LIST.add("1");
    	AUM_DB_COLUMN_ID_LIST.add("4");
    	AUM_DB_COLUMN_ID_LIST.add("6");
    	AUM_DB_COLUMN_ID_LIST.add("7");
    	AUM_DB_COLUMN_ID_LIST.add("9");
    	AUM_DB_COLUMN_ID_LIST.add("11");
    }
    /******************************************************INVESTOR***************************************************************/
    public static final List<String> INVESTOR_DB_COLUMN_ID_LIST = new ArrayList<String>();
    static {
    	INVESTOR_DB_COLUMN_ID_LIST.add("13");
    	INVESTOR_DB_COLUMN_ID_LIST.add("14");
    	INVESTOR_DB_COLUMN_ID_LIST.add("15");
    	INVESTOR_DB_COLUMN_ID_LIST.add("18");
    	INVESTOR_DB_COLUMN_ID_LIST.add("19");
    	INVESTOR_DB_COLUMN_ID_LIST.add("26");
    	INVESTOR_DB_COLUMN_ID_LIST.add("28");
    	INVESTOR_DB_COLUMN_ID_LIST.add("36");
    	INVESTOR_DB_COLUMN_ID_LIST.add("37");
    	INVESTOR_DB_COLUMN_ID_LIST.add("49");
    	INVESTOR_DB_COLUMN_ID_LIST.add("50");
    }
    /******************************************************TRANSACTION***************************************************************/
    public static final List<String> TRANSACTION_DB_COLUMN_ID_LIST = new ArrayList<String>();
    static {
    	TRANSACTION_DB_COLUMN_ID_LIST.add("106");
    	TRANSACTION_DB_COLUMN_ID_LIST.add("108");
    	TRANSACTION_DB_COLUMN_ID_LIST.add("109");
    	TRANSACTION_DB_COLUMN_ID_LIST.add("110");
    	TRANSACTION_DB_COLUMN_ID_LIST.add("111");
    	TRANSACTION_DB_COLUMN_ID_LIST.add("112");
    	TRANSACTION_DB_COLUMN_ID_LIST.add("115");
    	TRANSACTION_DB_COLUMN_ID_LIST.add("117");
    	TRANSACTION_DB_COLUMN_ID_LIST.add("118");
    	TRANSACTION_DB_COLUMN_ID_LIST.add("119");
    	TRANSACTION_DB_COLUMN_ID_LIST.add("120");
    	TRANSACTION_DB_COLUMN_ID_LIST.add("125");
    	TRANSACTION_DB_COLUMN_ID_LIST.add("258");
    }
    /******************************************************SIP STP***************************************************************/
    public static final List<String> SIPSTP_DB_COLUMN_ID_LIST = new ArrayList<String>();
    static {
    	SIPSTP_DB_COLUMN_ID_LIST.add("178");
    	SIPSTP_DB_COLUMN_ID_LIST.add("179");
    	SIPSTP_DB_COLUMN_ID_LIST.add("180");
    	SIPSTP_DB_COLUMN_ID_LIST.add("181");
    	SIPSTP_DB_COLUMN_ID_LIST.add("182");
    	SIPSTP_DB_COLUMN_ID_LIST.add("183");
    	SIPSTP_DB_COLUMN_ID_LIST.add("184");
    	SIPSTP_DB_COLUMN_ID_LIST.add("185");
    	SIPSTP_DB_COLUMN_ID_LIST.add("187");
    	SIPSTP_DB_COLUMN_ID_LIST.add("188");
    	SIPSTP_DB_COLUMN_ID_LIST.add("192");
    	SIPSTP_DB_COLUMN_ID_LIST.add("193");
    	SIPSTP_DB_COLUMN_ID_LIST.add("204");
    	SIPSTP_DB_COLUMN_ID_LIST.add("186");
    }
    public static final List<String> SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST = new ArrayList<String>();
    static {
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("178");
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("179");
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("180");
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("181");
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("183");
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("184");
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("185");
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("187");
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("188");
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("192");
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("193");
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("204");
    	SIPSTP_FRANKLIN_ACTIVE_DB_COLUMN_ID_LIST.add("186");
    }
    public static final List<String> SIPSTP_FRANKLIN_CLOSED_DB_COLUMN_ID_LIST = new ArrayList<String>();
    static {
    	//SIPSTP_FRANKLIN_CLOSED_DB_COLUMN_ID_LIST.add("182");
    	SIPSTP_FRANKLIN_CLOSED_DB_COLUMN_ID_LIST.add("183");    	
    }
    /******************************************************BROKERAGE***************************************************************/
    public static final List<String> BROKERAGE_DB_COLUMN_ID_LIST = new ArrayList<String>();
    static {
    	BROKERAGE_DB_COLUMN_ID_LIST.add("241");
    	//BROKERAGE_DB_COLUMN_ID_LIST.add("242");
    	BROKERAGE_DB_COLUMN_ID_LIST.add("244");
    	BROKERAGE_DB_COLUMN_ID_LIST.add("245");
    	BROKERAGE_DB_COLUMN_ID_LIST.add("249");
    	BROKERAGE_DB_COLUMN_ID_LIST.add("250");
    	BROKERAGE_DB_COLUMN_ID_LIST.add("253");
    	BROKERAGE_DB_COLUMN_ID_LIST.add("254");
    	BROKERAGE_DB_COLUMN_ID_LIST.add("251");
    	BROKERAGE_DB_COLUMN_ID_LIST.add("255");
    }
    
    /******************************************************REJECTION***************************************************************/
    public static final List<String> REJECTION_DB_COLUMN_ID_LIST = new ArrayList<String>();
    static {
    	REJECTION_DB_COLUMN_ID_LIST.add("229");
    }
    
    public static final List<String> PURCHASE_LIST = new ArrayList<String>();
    static {
    	PURCHASE_LIST.add("Additional Purchase");
    	PURCHASE_LIST.add("ADDPUR");
    	PURCHASE_LIST.add("New Purchase");
    	PURCHASE_LIST.add("NEWPUR");
    	PURCHASE_LIST.add("P");
    	PURCHASE_LIST.add("REDR");
    	PURCHASE_LIST.add("SIP");
    	PURCHASE_LIST.add("STP In");
    	PURCHASE_LIST.add("STPI");
    	PURCHASE_LIST.add("SWIN");
    	PURCHASE_LIST.add("TI");
    	PURCHASE_LIST.add("TMI");
    	PURCHASE_LIST.add("SI");
    	PURCHASE_LIST.add("STPOR");
    	PURCHASE_LIST.add("SWIN");
    	PURCHASE_LIST.add("SWOFR");    	
    }
    
    public static final List<String> SALE_LIST = new ArrayList<String>();
    static {
    	SALE_LIST.add("R");
    	SALE_LIST.add("RED");
    	SALE_LIST.add("Redemption");
    	SALE_LIST.add("SO");
    	SALE_LIST.add("STP O");
    	SALE_LIST.add("STP Out");
    	SALE_LIST.add("SWD");
    	SALE_LIST.add("SWOF");
    	SALE_LIST.add("TMO");
    	SALE_LIST.add("TO");
    	SALE_LIST.add("TI");
    	SALE_LIST.add("TMI");    	
    	SALE_LIST.add("ADDPURR");
    	SALE_LIST.add("NEWPURR");
    	SALE_LIST.add("SIPR");
    	SALE_LIST.add("STPIR");
    	SALE_LIST.add("STPO");
    	SALE_LIST.add("SWINR");
   }
    
    public static final List<String> DIVIDEND_LIST = new ArrayList<String>();
    static {
    	DIVIDEND_LIST.add("DIR");
    	DIVIDEND_LIST.add("Dividend Reinvestment");
    	DIVIDEND_LIST.add("DR");
    	DIVIDEND_LIST.add("DP");
    	DIVIDEND_LIST.add("No Effect");
    	DIVIDEND_LIST.add("J");
    }
    
} 