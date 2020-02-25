/**
 * 
 */
package com.finlabs.finexa.util;

/**
 * @author vishwajeet, supratim
 *
 */
public class FinexaConstant {

	// Finexa Global Exception Constant //
	public static final String ERROR_PAGE_CODE = "EPC";
	public static final String ERROR_MESSAGE_CODE = "EMC";
	// Finexa Global Exception Constant //

	/***************** LookupRelationConstants ******************/
	public static final int LOOKUP_RELATION_SELF_ID = 0;
	public static final int LOOKUP_RELATION_SPOUSE_ID = 1;
	public static final int LOOKUP_RELATION_SON_ID = 2;
	public static final int LOOKUP_RELATION_DAUGHTER_ID = 3;
	public static final int LOOKUP_RELATION_FATHER_ID = 4;
	public static final int LOOKUP_RELATION_MOTHER_ID = 5;
	public static final int LOOKUP_RELATION_BROTHER_ID = 6;
	public static final int LOOKUP_RELATION_SISTER_ID = 7;
	public static final int LOOKUP_RELATION_OTHER_ID = 8;
	/***************** LookupRelationConstants ******************/
	
	public static final byte SUKANYA_DEPOSIT_TENURE = 15;
	
	public static final int INDIA_LOOKUP_COUNTRY_ID = 99;

	/*************** FamilyIncomeCatID Constant ***************/
	public static final byte LOOKUP_INCOME_SALARY_ID = 1;
	public static final byte LOOKUP_INCOME_BONOUS_VAR_ID = 2;
	public static final byte LOOKUP_INCOME_BUSSINESS_ID = 3;
	public static final byte LOOKUP_INCOME_PROFESSIONAL_ID = 4;
	public static final byte LOOKUP_INCOME_RENTAL_ID = 5;
	public static final byte LOOKUP_INCOME_PENSION_ID = 6;
	public static final byte LOOKUP_INCOME_OTHER_ID = 7;
	public static final byte LOOKUP_INCOME_TOTAL_ID = 8;
	/*************** FamilyIncomeCatID Constant ***************/

	/*************** lookupNonLifeInsuranceType Constant **********/
	public static final int LOOKUP_NON_LIFE_INSURANCE_GENERAL_ID = 1;
	public static final int LOOKUP_NON_LIFE_INSURANCE_HEALTH_ID = 2;
	/*************** lookupNonLifeInsuranceType Constant **********/

	/***************
	 * lookupNonLife >> General Insurance policy Type Constant
	 **********/
	public static final int LOOKUP_NON_LIFE_GENERAL_PERSONAL_ACCIDENT_ID = 2;
	/***************
	 * lookupNonLife >> General Insurance policy Type Constant
	 **********/

	/***************
	 * lookupNonLife >> health Insurance policy Type Constant
	 **********/
	public static final int LOOKUP_NON_LIFE_HEALTH_MEDICLAIM_ID = 1;
	public static final int LOOKUP_NON_LIFE_HEALTH_CRITICAL_ILLNESS_ID = 2;
	public static final int LOOKUP_NON_LIFE_HEALTH_PERMANENT_DISABILITY_ID = 3;
	/***************
	 * lookupNonLife >> health Insurance policy Type Constant
	 **********/

	/******************** Income Duration Constants ***********************/
	public static final byte INCOME_END_YEAR_LIFE_EXPECTANCY = 3;
	public static final String INCOME_END_YEAR_LIFE_EXPECTANCY_DESC = "Upto Life Expectancy";

	/********************
	 * Frequency Duration Constant
	 **************************/
	public static final byte FREQUENCY_ANNUAL_ID = 1;
	public static final String FREQUENCY_ANNUAL_DESC = "Annual";
	public static final byte FREQUENCY_ANNUAL_DISPLAY_ORDER = 6;
	public static final byte FREQUENCY_MONTHLY_ID = 12;
	public static final byte FREQUENCY_NA_ID = 13;
	public static final String FREQUENCY_MONTHLY_DESC = "Monthly";
	public static final byte FREQUENCY_MONTHLY_DISPLAY_ORDER = 1;

	/*******************
	 * Master Product Classification Constants
	 ********************/
	public static final byte MASTER_PRODUCT_CLASSIFICATION_ANNUITY_ID = 34;

	/******************* Lookup Annuity Type Constants ********************/
	public static final byte LOOKUP_ANNUITY_TYPE_EPS_ID = 6;

	// Exception Module for ClientLifeInsurance
	// public static final String CLIENT_LIFE_INSURANCE_MODULE =
	// "CLIENTINFO_LIFE_INSURANCE ";
	public static final String CLIENT_LIFE_INSURANCE_VIEW_CODE = "CLVC001";
	public static final String CLIENT_LIFE_INSURANCE_VIEW_DESC = "Failed to get Life Insurance";
	public static final String CLIENT_LIFE_POLICY_NUMBER_EXIST = "Policy Number Already Exists";
	public static final String CLIENT_LIFE_POLICY_NUMBER_AVAIL = "Policy Number Available";

	// Exception Module for ClientNonLifeInsurance
	public static final String CLIENT_NONLIFE_INSURANCE_MODULE = "CLIENTINFO_NONLIFE_INSURANCE ";
	public static final String CLIENT_NONLIFE_INSURANCE_VIEW_CODE = "CNLVC001";
	public static final String CLIENT_NONLIFE_INSURANCE_VIEW_DESC = "Failed to get Non Life Insurance";
	public static final String CLIENT_NONLIFE_POLICY_NUMBER_EXIST = "Policy Number Already Exists";
	public static final String CLIENT_NONLIFE_POLICY_NUMBER_AVAIL = "Policy Number Available";

	// Exception Module for ClientMutualFund
	public static final String CLIENT_MUTUALFUND_MODULE = "CLIENTINFO_MUTUAL_FUND";
	public static final String CLIENT_MUTUALFUND_VIEW_CODE = "CMFV001";
	public static final String CLIENT_MUTUALFUND_ADD_CODE = "CMFA001";
	public static final String CLIENT_MUTUALFUND_ADD_POPULATE_LIST_CODE = "CMFA002";
	// public static final String
	// CLIENT_MUTUALFUND_ADD_POPULATE_INTEREST_RATE_CODE = "CMFA003";
	public static final String CLIENT_MUTUALFUND_UPDATE_CODE = "CMFU001";
	public static final String CLIENT_MUTUALFUND_DELETE_CODE = "CMFD001";
	public static final String CLIENT_MUTUALFUND_EDIT_POPULATE_FORM_CODE = "CMFE001";
	public static final String CLIENT_MUTUALFUND_FUNDHOUSE_POPULATE_CODE = "CMFFH1";

	// Exception Module for ClientSmallSaving
	public static final String CLIENT_SMALL_SAVINGS_MODULE = "CLIENTINFO_SMALL_SAVINGS";
	public static final String CLIENT_SMALL_SAVINGS_DAUGHTER_CHECK = "CSS001";
	public static final String CLIENT_SMALL_SAVINGS_ADD_CODE = "CSS002";
	public static final String CLIENT_SMALL_SAVINGS_INTEREST_RATE = "Interest Rate does not exist for date specified";

	// For My Business
	public static final String ADMIN_DESC = "Admin";
	public static final int RETURN_VAL_ERROR_ROLE_MAPPING = 101;
	public static final int RETURN_VAL_ERROR_SUPERVISOR_MAPPING = 102;
	public static final int RETURN_VAL_ERROR_CLIENT_MAPPING = 103;
	public static final int RETURN_VAL_SUCCESS = 100;
	public static final int RETURN_VAL_ERROR = -1;

	// For Client Record
	public static final int ADDRESS_TYPE_OFFICE = 1;
	public static final int ADDRESS_TYPE_PERMANENT = 2;
	public static final int ADDRESS_TYPE_CORRESPONDENCE = 3;

	// Master names
	public static final String LOOKUP_ASSET_CLASS = "lookupAssetClass";
	public static final String MASTER_MUTUAL_FUND_ETF = "masterMutualFundETF";
	public static final String LOOKUP_ASSET_ALLOCATION = "Model Portfolio Allocation";
	public static final String LOOKUP_ASSET_ALLOCATION_CATEGORY = "Model Portfolio Master";
	public static final String LOOKUP_BUCKET_LOGIC = "Bucketing Logic";

	// File Type
	public static final String FILE_TYPE_EXCEL = "excel";
	public static final String FILE_TYPE_CSV = "csv";
	public static final String FILE_TYPE_PDF = "pdf";

	// OTP
	public static final String OTP_API_KEY = "9346581b-880d-11e7-94da-0200cd936042";

	/*****************************************************************************************************************/
	//public static final String FROM_EMAIL = "finlabsindia@gmail.com";
	/*************Email is changed********************/
	public static final String FROM_EMAIL = "finexasupport@finlabsindia.com";
	//public static final String FROM_EMAIL_PASSWORD = "SottiJomkaloPassword!";
	public static final String FROM_EMAIL_PASSWORD = "finlabs@123";
	public static final String FINEXA_PASSWORD_RESET = "Finexa Password Reset";
	public static final String FINEXA_PASSWORD_UPDATE = "Updation of email ID in Finexa Client Portal";
	//public static final String FINEXA_CLIENT_PASSWORD_GENERATE = "Finexa Credentials";
	public static final String FINEXA_CLIENT_PASSWORD_GENERATE = "Welcome to Finexa Client Portal";
	public static final String FINEXA_USER_CREATION = "Finexa User Creation";
	public static final String FINEXA_WELCOME_MESSAGE = "Welcome to Finexa";
	
	
	// ModifiedExceptionHandlingConstants
	// My Client - Client Information : Portfolio
	// Client Cash
	public static final String CLIENT_CASH_MODULE = "Client Cash";
	public static final String CLIENT_CASH_ADD_ERROR = "MCCIP-CASH01";
	public static final String CLIENT_CASH_GET_EDIT_DATA_ERROR = "MCCIP-CASH02";
	public static final String CLIENT_CASH_UPDATE_ERROR = "MCCIP-CASH03";
	public static final String CLIENT_CASH_VIEW_ERROR = "MCCIP-CASH04";
	public static final String CLIENT_CASH_DELETE_DATA_ERROR = "MCCIP-CASH05";
	// Master Cash
	public static final String MASTER_CASH_MODULE = "Master Cash";
	public static final String MASTER_CASH_VIEW_ERROR = "MCCIP-BANKNAME";
	/*****************************************************************************************************************/
	// Lumpsum Inflow
	public static final String CLIENT_LUMPSUM_INFLOW_MODULE = "Client Lumpsum Inflow";
	public static final String CLIENT_LUMPSUM_INFLOW_ADD_ERROR = "MCCIP-LUMPSUM01";
	public static final String CLIENT_LUMPSUM_INFLOW_GET_EDIT_DATA_ERROR = "MCCIP-LUMPSUM02";
	public static final String CLIENT_LUMPSUM_INFLOW_UPDATE_ERROR = "MCCIP-LUMPSUM03";
	public static final String CLIENT_LUMPSUM_INFLOW_VIEW_ERROR = "MCCIP-LUMPSUM04";
	public static final String CLIENT_LUMPSUM_INFLOW_DELETE_DATA_ERROR = "MCCIP-LUMPSUM05";
	/*****************************************************************************************************************/
	// Alternate Investments
	public static final String CLIENT_AI_MODULE = "Alternate Investments";
	public static final String CLIENT_AI_VIEW_ERROR = "MCCIP-AIVIEW";
	// Alternate Investments - Real Estate
	public static final String CLIENT_AI_REAL_ESTATE_MODULE = "Client Real Estate";
	public static final String CLIENT_AI_REAL_ESTATE_ADD_ERROR = "MCCIP-AIRE01";
	// Lookup Alternate Investments Asset Type
	public static final String LOOKUP_AI_ASSET_TYPE_MODULE = "Lookup Alternate Investments Asset Type";
	public static final String LOOKUP_AI_ASSET_TYPE_VIEW_ERROR = "MCCIP-AIAT";
	// Lookup Real Estate Type
	public static final String LOOKUP_REAL_ESTATE_TYPE_MODULE = "Lookup Real Estate Type";
	public static final String LOOKUP_REAL_ESTATE_TYPE_VIEW_ERROR = "MCCIP-AIRE02";
	public static final String CLIENT_AI_REAL_ESTATE_GET_DATA_ERROR = "MCCIP-AIRE03";
	public static final String CLIENT_AI_REAL_ESTATE_UPDATE_ERROR = "MCCIP-AIRE04";
	public static final String CLIENT_AI_REAL_ESTATE_DELETE_DATA_ERROR = "MCCIP-AIRE05";
	public static final String CLIENT_AI_REAL_ESTATE_VIEW_ERROR = "MCCIP-AIRE06";
	// Alternate Investment - Precious Metals
	public static final String CLIENT_AI_PRECIOUS_METALS_MODULE = "Client Precious Metals";
	public static final String CLIENT_AI_PRECIOUS_METALS_ADD_ERROR = "MCCIP-AIPM01";
	public static final String CLIENT_AI_PRECIOUS_METALS_GET_DATA_ERROR = "MCCIP-AIPM02";
	public static final String CLIENT_AI_PRECIOUS_METALS_UPDATE_ERROR = "MCCIP-AIPM03";
	public static final String CLIENT_AI_PRECIOUS_METALS_DELETE_DATA_ERROR = "MCCIP-AIPM04";
	public static final String CLIENT_AI_PRECIOUS_METALS_VIEW_ERROR = "MCCIP-AIPM05";
	// Alternate Investment - Vehicles
	public static final String CLIENT_AI_VEHICLES_MODULE = "Client Vehicles";
	public static final String CLIENT_AI_VEHICLES_ADD_ERROR = "MCCIP-AIV01";
	public static final String CLIENT_AI_VEHICLES_GET_DATA_ERROR = "MCCIP-AIV02";
	public static final String CLIENT_AI_VEHICLES_UPDATE_ERROR = "MCCIP-AIV03";
	public static final String CLIENT_AI_VEHICLES_DELETE_DATA_ERROR = "MCCIP-AIV04";
	public static final String CLIENT_AI_VEHICLES_VIEW_ERROR = "MCCIP-AIV05";
	// Alternate Investment - Others(RE/PE/VCF/AIF Fund) / Other Alternate Asset
	public static final String CLIENT_AI_OTHERS_MODULE = "Client Other Alternate Asset";
	public static final String CLIENT_AI_OTHERS_ADD_ERROR = "MCCIP-AIO01";
	public static final String CLIENT_AI_OTHERS_GET_DATA_ERROR = "MCCIP-AIO02";
	public static final String CLIENT_AI_OTHERS_UPDATE_ERROR = "MCCIP-AIO03";
	public static final String CLIENT_AI_OTHERS_DELETE_DATA_ERROR = "MCCIP-AIO04";
	public static final String CLIENT_AI_OTHERS_VIEW_ERROR = "MCCIP-AIO05";
	// Alternate Investment - Structured Products
	public static final String CLIENT_AI_SP_MODULE = "Client Structured Products";
	public static final String CLIENT_AI_SP_ADD_ERROR = "MCCIP-AISP01";
	public static final String CLIENT_AI_SP_GET_DATA_ERROR = "MCCIP-AISP02";
	public static final String CLIENT_AI_SP_UPDATE_ERROR = "MCCIP-AISP03";
	public static final String CLIENT_AI_SP_DELETE_DATA_ERROR = "MCCIP-AISP04";
	public static final String CLIENT_AI_SP_VIEW_ERROR = "MCCIP-AISP05";

	/*****************************************************************************************************************/

	// Retirement Oriented Schemes
	public static final String CLIENT_ROS_MODULE = "Retirement Oriented Schemes";
	public static final String CLIENT_ROS_VIEW_ERROR = "MCCIP-ROSVIEW";
	// Retirement Oriented Schemes - EPF
	public static final String CLIENT_ROS_EPF_MODULE = "Client EPF";
	public static final String CLIENT_ROS_EPF_ADD_ERROR = "MCCIP-ROSEPF01";
	// Master EPF Interest Rate
	public static final String MASTER_EPF_INTEREST_RATE_MODULE = "Master EPF Interest Rate";
	public static final String MASTER_EPF_INTEREST_RATE_VIEW_ERROR = "MCCIP-ROSEPF02";
	// Master Income Growth Rate
	public static final String MASTER_INCOME_GROWTH_RATE_MODULE = "Master Income Growth Rate";
	public static final String EPF_MASTER_INCOME_GROWTH_RATE_VIEW_ERROR = "MCCIP-ROSEPF03";
	public static final String CLIENT_ROS_EPF_GET_DATA_ERROR = "MCCIP-ROSEPF04";
	public static final String CLIENT_ROS_EPF_UPDATE_ERROR = "MCCIP-ROSEPF05";
	public static final String CLIENT_ROS_EPF_DELETE_DATA_ERROR = "MCCIP-ROSEPF06";
	public static final String CLIENT_ROS_EPF_VIEW_ERROR = "MCCIP-ROSEPF07";
	// Retirement Oriented Schemes - PPF
	public static final String CLIENT_ROS_PPF_MODULE = "Client PPF";
	public static final String CLIENT_ROS_PPF_ADD_ERROR = "MCCIP-ROSPPF01";
	// Master PPF Interest Rate
	public static final String MASTER_PPF_INTEREST_RATE_MODULE = "Master PPF Interest Rate";
	public static final String MASTER_PPF_INTEREST_RATE_VIEW_ERROR = "MCCIP-ROSPPF02";
	public static final String CLIENT_ROS_PPF_GET_DATA_ERROR = "MCCIP-ROSPPF03";
	public static final String CLIENT_ROS_PPF_UPDATE_ERROR = "MCCIP-ROSPPF04";
	public static final String CLIENT_ROS_PPF_DELETE_DATA_ERROR = "MCCIP-ROSPPF05";
	public static final String CLIENT_ROS_PPF_VIEW_ERROR = "MCCIP-ROSPPF06";
	public static final String CLIENT_ROS_CHECK_PPF_EXIST_ERROR = "MCCIP-ROSPPF07";
	public static final String CLIENT_ROS_CALCULATE_PPF_MATURITY_DATE_ERROR = "MCCIP-ROSPPF08";
	// Retirement Oriented Schemes - NPS
	public static final String CLIENT_ROS_NPS_MODULE = "Client NPS";
	public static final String CLIENT_ROS_NPS_ADD_ERROR = "MCCIP-ROSNPS01";
	// Lookup NPS Plan Type
	public static final String LOOKUP_NPS_PLAN_TYPE_MODULE = "Lookup NPS Plan Type";
	public static final String LOOKUP_NPS_PLAN_TYPE_VIEW_ERROR = "MCCIP-ROSNPS02";
	// Master Income Growth Rate
	public static final String NPS_MASTER_INCOME_GROWTH_RATE_VIEW_ERROR = "MCCIP-ROSNPS03";
	// Master NPS Asset Class Expected Return
	public static final String MASTER_NPS_ASSET_CLASS_EXPECTED_RETURN_MODULE = "Master NPS Asset Class Expected Return";
	public static final String MASTER_NPS_ASSET_CLASS_EXPECTED_RETURN_VIEW_ERROR = "MCCIP-ROSNPS04";
	public static final String CLIENT_ROS_NPS_GET_DATA_ERROR = "MCCIP-ROSNPS05";
	public static final String CLIENT_ROS_NPS_UPDATE_ERROR = "MCCIP-ROSNPS06";
	public static final String CLIENT_ROS_NPS_DELETE_DATA_ERROR = "MCCIP-ROSNPS07";
	public static final String CLIENT_ROS_NPS_VIEW_ERROR = "MCCIP-ROSNPS08";
	public static final String CLIENT_ROS_CHECK_NPS_EXIST_ERROR = "MCCIP-ROSNPS09";
	// Retirement Oriented Schemes - Annuity
	public static final String CLIENT_ROS_ANNUITY_MODULE = "Client Annuity";
	public static final String CLIENT_ROS_ANNUITY_ADD_ERROR = "MCCIP-ROSA01";
	// Lookup Annuity Type
	public static final String LOOKUP_ANNUITY_TYPE_MODULE = "Lookup Annuity Type";
	public static final String LOOKUP_ANNUITY_VIEW_ERROR = "MCCIP-ROSA02";
	public static final String CLIENT_ROS_ANNUITY_GET_DATA_ERROR = "MCCIP-ROSA03";
	public static final String CLIENT_ROS_ANNUITY_UPDATE_ERROR = "MCCIP-ROSA04";
	public static final String CLIENT_ROS_ANNUITY_DELETE_DATA_ERROR = "MCCIP-ROSA05";
	public static final String CLIENT_ROS_ANNUITY_VIEW_ERROR = "MCCIP-ROSA06";
	// Retirement Oriented Schemes - Atal Pension Yojana
	public static final String CLIENT_ROS_APY_MODULE = "Client Atal Pension Yojana";
	public static final String CLIENT_ROS_APY_ADD_ERROR = "MCCIP-ROSAPY01";
	public static final String CLIENT_ROS_APY_GET_DATA_ERROR = "MCCIP-ROSAPY02";
	public static final String CLIENT_ROS_APY_UPDATE_ERROR = "MCCIP-ROSAPY03";
	public static final String CLIENT_ROS_APY_DELETE_ERROR = "MCCIP-ROSAPY04";
	public static final String CLIENT_ROS_APY_VIEW_ERROR = "MCCIP-ROSAPY05";
	public static final String CLIENT_ROS_CHECK_APY_EXIST_ERROR = "MCCIP-ROSAPY06";
	/*****************************************************************************************************************/

	// Small Saving Schemes
	public static final String CLIENT_SSS_MODULE = "Client Small Savings";
	public static final String CLIENT_SSS_ADD_ERROR = "MCCIP-SS01";
	// Master NSC Interest Rate
	public static final String MASTER_NSC_INTEREST_RATE_MODULE = "Master NSC Interest Rate";
	public static final String MASTER_NSC_INTEREST_RATE_VIEW_ERROR = "MCCIP-SS02";
	// Master KVP Interest Rate
	public static final String MASTER_KVP_INTEREST_RATE_MODULE = "Master KVP Interest Rate";
	public static final String MASTER_KVP_INTEREST_RATE_VIEW_ERROR = "MCCIP-SS03";
	// Master POMIS
	public static final String MASTER_POMIS_MODULE = "Master POMIS";
	public static final String MASTER_POMIS_VIEW_ERROR = "MCCIP-SS04";
	// Master SCSS
	public static final String MASTER_SCSS_INTEREST_RATE_MODULE = "Master SCSS Interest Rate";
	public static final String MASTER_SCSS_INTEREST_RATE_VIEW_ERROR = "MCCIP-SS05";
	// Master PORD
	public static final String MASTER_PORD_MODULE = "Master PORD";
	public static final String MASTER_PORD_VIEW_ERROR = "MCCIP-SS06";
	// Master Sukanya Samriddhi Interest Rate
	public static final String MASTER_SUKANYA_SAMRIDDHI_INTEREST_RATE_MODULE = "Master Sukanya Samriddhi Interest Rate";
	public static final String MASTER_SUKANYA_SAMRIDDHI_INTEREST_RATE_VIEW_ERROR = "MCCIP-SS07";
	// Master KVP Compounding Frequency
	public static final String MASTER_KVP_COMPOUNDING_FREQUENCY_MODULE = "Master KVP Compounding Frequency";
	public static final String MASTER_KVP_COMPOUNDING_FREQUENCY_VIEW_ERROR = "MCCIP-SS08";
	// Master KVP Term
	public static final String MASTER_KVP_TERM_MODULE = "Master KVP Term";
	public static final String MASTER_KVP_TERM_VIEW_ERROR = "MCCIP-SS09";
	public static final String CLIENT_SSS_GET_DATA_ERROR = "MCCIP-SS10";
	public static final String CLIENT_SSS_UPDATE_ERROR = "MCCIP-SS11";
	public static final String CLIENT_SSS_VIEW_ERROR = "MCCIP-SS12";
	public static final String CLIENT_SSS_DELETE_DATA_ERROR = "MCCIP-SS13";

	/*****************************************************************************************************************/

	// Deposit/Bonds(Fixed Income)
	public static final String CLIENT_FIXED_INCOME_MODULE = "Client Fixed Income";
	public static final String CLIENT_FIXED_INCOME_ADD_ERROR = "MCCIP-FI01";
	// Lookup Fixed Deposit Type
	public static final String LOOKUP_FIXED_DEPOSIT_TYPE_MODULE = "Lookup Fixed Deposit Type";
	public static final String LOOKUP_FIXED_DEPOSIT_TYPE_VIEW_ERROR = "MCCIP-FI02";
	// Lookup Bond Type
	public static final String LOOKUP_BOND_TYPE_MODULE = "Lookup Bond Type";
	public static final String LOOKUP_BOND_TYPE_VIEW_ERROR = "MCCIP-FI03";
	public static final String CLIENT_FIXED_INCOME_GET_DATA_ERROR = "MCCIP-FI04";
	public static final String CLIENT_FIXED_INCOME_UPDATE_ERROR = "MCCIP-FI05";
	public static final String CLIENT_FIXED_INCOME_VIEW_ERROR = "MCCIP-FI06";
	public static final String CLIENT_FIXED_INCOME_DELETE_ERROR = "MCCIP-FI07";

	/*****************************************************************************************************************/

	// Direct Equity
	public static final String CLIENT_DIRECT_EQUITY_MODULE = "Client Equity";
	public static final String CLIENT_DIRECT_EQUITY_ADD_ERROR = "MCCIP-DE01";
	// Master Direct Equity
	public static final String MASTER_DIRECT_EQUITY_MODULE = "Master Direct Equity";
	public static final String MASTER_DIRECT_EQUITY_ADD_ERROR = "MCCIP-DE02";
	public static final String CLIENT_DIRECT_EQUITY_GET_DATA_ERROR = "MCCIP-DE03";
	public static final String CLIENT_DIRECT_EQUITY_UPDATE_ERROR = "MCCIP-DE04";
	public static final String CLIENT_DIRECT_EQUITY_VIEW_ERROR = "MCCIP-DE05";
	public static final String CLIENT_DIRECT_EQUITY_DELETE_ERROR = "MCCIP-DE06";

	/*****************************************************************************************************************/

	// MF-ETF-PMS(Funds)
	public static final String CLIENT_FUND_MODULE = "Client Mutual Fund";
	public static final String CLIENT_FUND_ADD_ERROR = "MCCIP-F01";
	// Master Mutual Fund ETF
	public static final String MASTER_MUTUAL_FUND_ETF_MODULE = "Master Mutual Fund ETF";
	public static final String GET_FUND_HOUSE_ERROR = "MCCIP-F02";
	public static final String GET_SCHEME_NAME_FROM_FUND_HOUSE_ERROR = "MCCIP-F03";
	public static final String GET_ALL_INFO_FROM_SCHEME_NAME_ERROR = "MCCIP-F11";
	// Lookup Asset Class
	public static final String LOOKUP_ASSET_CLASS_MODULE = "Lookup Fund Category";
	public static final String GET_FUND_CATEGORY_ERROR = "MCCIP-F04";
	// Lookup Fund Investment Mode
	public static final String LOOKUP_FUND_INVESTMENT_MODE_MODULE = "Lookup Fund Investment Mode";
	public static final String GET_FUND_INVESTMENT_MODE_ERROR = "MCCIP-F05";
	// Master MF Daily NAV
	public static final String MASTER_MF_DAILY_NAV_MODULE = "Master MF Daily NAV";
	public static final String GET_NAV_ERROR = "MCCIP-F06";
	public static final String CLIENT_FUND_GET_DATA_ERROR = "MCCIP-F07";
	public static final String CLIENT_FUND_UPDATE_ERROR = "MCCIP-F08";
	public static final String CLIENT_FUND_VIEW_ERROR = "MCCIP-F09";
	public static final String CLIENT_FUND_DELETE_ERROR = "MCCIP-F10";
	// Master PMS (Master PM in app code)
	public static final String MASTER_PMS_MODULE = "Master PMS";
	public static final String GET_PROVIDER_NAME_ERROR = "MCCIP-F11";
	public static final String GET_SCHEME_NAME_FROM_PROVIDER_NAME_ERROR = "MCCIP-F12";

	/*****************************************************************************************************************/

	// My Client - Client Information : Loans & Liabilities
	public static final String CLIENT_LOANS_MODULE = "Client Loan";
	public static final String CLIENT_LOANS_ADD_ERROR = "MCCIL-L01";
	// Lookup Loan Category
	public static final String LOOKUP_LOAN_CATEGORY_MODULE = "Lookup Loan Category";
	public static final String GET_LOAN_CATEGORY_ERROR = "MCCIL-L02";
	// Master Loan Provider
	public static final String MASTER_LOAN_PROVIDER_MODULE = "Master Loan Provider";
	public static final String GET_LOAN_PROVIDER_ERROR = "MCCIL-L03";
	public static final String CLIENT_LOANS_GET_LOAN_END_DATE_EMI_ERROR = "MCCIL-L04";
	public static final String CLIENT_LOANS_GET_LOAN_END_DATE_NON_EMI_ERROR = "MCCIL-L05";
	public static final String CLIENT_LOANS_GET_DATA_ERROR = "MCCIL-L06";
	public static final String CLIENT_LOANS_UPDATE_ERROR = "MCCIL-L07";
	public static final String CLIENT_LOANS_VIEW_ERROR = "MCCIL-L08";
	public static final String CLIENT_LOANS_DELETE_ERROR = "MCCIL-L09";

	/*****************************************************************************************************************/

	// My Client - Client Information : Insurance
	// Client Life Insurance
	public static final String CLIENT_LIFE_INSURANCE_MODULE = "Client Life Insurance";
	public static final String CLIENT_LIFE_INSURANCE_ADD_ERROR = "MCCIIn-LI01";
	// Master Insurance Company Name
	public static final String MASTER_INSURANCE_COMPANY_NAME_MODULE = "Master Insurance Company Name";
	public static final String GET_LIFE_INSURANCE_COMPANY_NAME_LIST_ERROR = "MCCIIn-LI02";
	// Lookup Insurance Policy Type
	public static final String LOOKUP_INSURANCE_POLICY_TYPE_MODULE = "Lookup Insurance Policy Type";
	public static final String GET_LIFE_INSURANCE_POLICY_TYPE_LIST_ERROR = "MCCIIn-LI03";
	public static final String CLIENT_LIFE_INSURANCE_CHECK_POLICY_NUMBER = "MCCIIn-LI04";
	public static final String CLIENT_LIFE_INSURANCE_GET_DATA_ERROR = "MCCIIn-LI05";
	public static final String CLIENT_LIFE_INSURANCE_UPDATE_ERROR = "MCCIIn-LI06";
	public static final String CLIENT_LIFE_INSURANCE_VIEW_ERROR = "MCCIIn-LI07";
	public static final String CLIENT_LIFE_INSURANCE_DELETE_ERROR = "MCCIIn-LI08";

	/*****************************************************************************************************************/

	// Client Non Life Insurance
	public static final String CLIENT_NON_LIFE_INSURANCE_MODULE = "Client Non Life Insurance";
	public static final String CLIENT_NON_LIFE_INSURANCE_ADD_ERROR = "MCCIIn-NLI01";
	// Master Insurance Company Name
	public static final String GET_NON_LIFE_INSURANCE_COMPANY_NAME_LIST_ERROR = "MCCIIn-NLI02";
	// Lookup Insurance Policy Type
	public static final String GET_NON_LIFE_INSURANCE_POLICY_TYPE_LIST_ERROR = "MCCIIn-NLI03";
	public static final String CLIENT_NON_LIFE_INSURANCE_CHECK_POLICY_TYPE_LIST_ERROR = "MCCIIn-NLI04";
	public static final String CLIENT_NON_LIFE_INSURANCE_GET_DATA_ERROR = "MCCIIn-NLI05";
	public static final String CLIENT_NON_LIFE_INSURANCE_UPDATE_ERROR = "MCCIIn-NLI06";
	public static final String CLIENT_NON_LIFE_INSURANCE_VIEW_ERROR = "MCCIIn-NLI07";
	public static final String CLIENT_NON_LIFE_INSURANCE_DELETE_ERROR = "MCCIIn-NLI08";

	/*****************************************************************************************************************/

	// My Client - Client Information : Goals
	// Client Goal
	public static final String CLIENT_GOAL_MODULE = "Client Goal";
	public static final String CLIENT_GOAL_ADD_ERROR = "MCCIG-G01";
	public static final String CLIENT_GOAL_GET_GOAL_PRIORITY_ERROR = "MCCIG-G02";
	// Lookup Goal Corpus Utilization Frequency
	public static final String LOOKUP_GOAL_CORPUS_UTILIZATION_FREQUENCY_MODULE = "Lookup Goal Corpus Utilization Frequency";
	public static final String GET_GOAL_CORPUS_UTILIZATION_FREQUENCY_ERROR = "MCCIG-G03";
	public static final String CLIENT_GOAL_GET_LIFE_EXPECTANCY_ERROR = "MCCIG-G04";
	public static final String CLIENT_GOAL_GET_DATE_ERROR = "MCCIG-G05";
	public static final String CLIENT_GOAL_GET_AGE_ERROR = "MCCIG-G06";
	public static final String CLIENT_GOAL_GET_SUM_OF_AGE_AND_RETIREMENT_AGE_ERROR = "MCCIG-G07";
	public static final String CLIENT_GOAL_GET_INFLATION_RATE_ERROR = "MCCIG-G08";
	public static final String CLIENT_GOAL_GET_CORPUS_GOAL_START_ERROR = "MCCIG-G09";
	public static final String CLIENT_GOAL_GET_DATA_ERROR = "MCCIG-G10";
	public static final String CLIENT_GOAL_UPDATE_ERROR = "MCCIG-G11";
	public static final String CLIENT_GOAL_VIEW_ERROR = "MCCIG-G12";
	public static final String CLIENT_GOAL_DELETE_ERROR = "MCCIG-G13";
	public static final String CLIENT_GOAL_VIEW_GOAL_PRIORITY_ERROR = "MCCIG-G14";
	public static final String CLIENT_DASHBOARD_GET_UPCOMING_GOAL = "MCCIG-G15";
	public static final String CLIENT_GOAL_CHECK_IF_RETIREMENT_GOAL_EXISTS_ERROR = "MCCIG-G16";

	/*****************************************************************************************************************/

	// My Client - Client Information : Income
	// Client Family Income
	public static final String CLIENT_FAMILY_INCOME_MODULE = "Client Family Income";
	public static final String CLIENT_FAMILY_INCOME_ADD_ERROR = "MCCII-FI01";
	// Lookup Month
	public static final String LOOKUP_MONTH_MODULE = "Lookup Month";
	public static final String GET_ALL_INCOME_EXPENSE_MONTHS_ERROR = "MCCIIE-MONTH";
	// Lookup Income Expense Duration
	public static final String LOOKUP_INCOME_EXPENSE_DURATION_MODULE = "Lookup Income Expense Duration";
	public static final String GET_ALL_INCOME_EXPENSE_YEARS_ERROR = "MCCIIE-YEAR";
	public static final String CHECK_IF_INCOME_EXISTS_ERROR = "MCCII-FI02";
	public static final String CLIENT_FAMILY_INCOME_GET_DATA_ERROR = "MCCII-FI03";
	public static final String CLIENT_FAMILY_INCOME_UPDATE_ERROR = "MCCII-FI04";
	public static final String CLIENT_FAMILY_INCOME_VIEW_ERROR = "MCCII-FI05";
	public static final String CLIENT_FAMILY_INCOME_DELETE_ERROR = "MCCII-FI06";

	/*****************************************************************************************************************/

	// Client Expense
	public static final String CLIENT_EXPENSE_MODULE = "Client Expense";
	public static final String CLIENT_EXPENSE_ADD_ERROR = "MCCIE-HE01";
	public static final String CLIENT_EXPENSE_GET_DATA_ERROR = "MCCIE-HE02";
	public static final String CLIENT_EXPENSE_UPDATE_ERROR = "MCCIE-HE03";
	public static final String CLIENT_EXPENSE_VIEW_ERROR = "MCCIE-HE04";
	public static final String CLIENT_EXPENSE_DELETE_ERROR = "MCCIE-HE05";

	/*****************************************************************************************************************/

	// My Client - Client Information : Personal Profile
	// Personal Information/Client Master
	public static final String PERSONAL_INFORMATION_MODULE = "Personal Information/Client Master";
	public static final String PERSONAL_INFORMATION_ADD_ERROR = "MCCI-ADDCLIENT";
	// Lookup Marital Status
	public static final String LOOKUP_MARITAL_STATUS_MODULE = "Lookup Marital Status";
	public static final String GET_MARITAL_STATUS_ERROR = "MCCIPP-PI02";
	// Lookup Educational Qualification
	public static final String LOOKUP_EDUCATIONAL_QUALIFICATION_MODULE = "Lookup Educational Qualification";
	public static final String GET_EDUCATIONAL_QUALIFICATION_ERROR = "MCCIPP-PI03";
	// Lookup Employment Type
	public static final String LOOKUP_EMPLOYMENT_TYPE_MODULE = "Lookup Employment Type";
	public static final String GET_EMPLOYMENT_TYPE_ERROR = "MCCIPP-PI04";
	// Lookup Resident Type
	public static final String LOOKUP_RESIDENT_TYPE_MODULE = "Lookup Resident Type";
	public static final String GET_RESIDENT_TYPE_ERROR = "MCCIPP-PI05";
	// Lookup Country
	public static final String LOOKUP_COUNTRY_MODULE = "Lookup Country";
	public static final String GET_COUNTRY_ERROR = "MCCIPP-PI06";
	public static final String PERSONAL_INFORMATION_GET_DATA_ERROR = "MCCIPP-PI01";
	public static final String PERSONAL_INFORMATION_UPDATE_ERROR = "MCCI-UPDATECLIENT";
	public static final String PERSONAL_INFORMATION_EXISTING_CLIENT_LIST_ERROR = "MCCI-VIEWCLIENT";
	public static final String PERSONAL_INFORMATION_CHECK_UNIQUE_PAN_ERROR = "MCCIPP-PI07";
	public static final String PERSONAL_INFORMATION_CHECK_UNIQUE_AADHAR_ERROR = "MCCIPP-PI08";
	public static final String PERSONAL_INFORMATION_CHECK_IF_PAN_EXISTS_ERROR = "MCCIPP-PI09";
	public static final String PERSONAL_INFORMATION_CHECK_IF_AADHAR_EXISTS_ERROR = "MCCIPP-PI10";
	public static final String PERSONAL_INFORMATION_DELETE_ERROR = "MCCIPP-PI11";
	public static final String PERSONAL_INFORMATION_SEARCH_ERROR = "MCCIPP-PI12";
	/*****************************************************************************************************************/
	// Client Guardian
	public static final String CLIENT_GUARDIAN_MODULE = "Client Guardian";
	public static final String CLIENT_GUARDIAN_ADD_ERROR = "MCCIPP-PIG01";
	public static final String CLIENT_GUARDIAN_GET_DATA_ERROR = "MCCIPP-PIG02";
	public static final String CLIENT_GUARDIAN_ALSO_GUARDIAN_CONTACT_DELETE_ERROR = "MCCIPP-PIG03";
	/*****************************************************************************************************************/
	// Client Guardian Contact
	public static final String CLIENT_GUARDIAN_CONTACT_MODULE = "Client Guardian Contact";
	public static final String CLIENT_GUARDIAN_CONTACT_ADD_ERROR = "MCCIPP-PIGC01";
	public static final String CLIENT_GUARDIAN_CONTACT_UPDATE_ERROR = "MCCIPP-PIGC02";
	public static final String CLIENT_GUARDIAN_CONTACT_GET_DATA_ERROR = "MCCIPP-PIGC03";
	public static final String CLIENT_GUARDIAN_CONTACT_CHECK_UNIQUE_PINCODE_ERROR = "MCCIPP-PIGC04";

	/*****************************************************************************************************************/
	// Client Family Member
	public static final String CLIENT_FAMILY_MEMBER_MODULE = "Client Family Member";
	public static final String CLIENT_FAMILY_MEMBER_ADD_ERROR = "MCCIPP-FI01";
	public static final String CLIENT_FAMILY_MEMBER_UPDATE_ERROR = "MCCIPP-FI02";
	public static final String CLIENT_FAMILY_MEMBER_GET_DATA_ERROR = "MCCIPP-FI03";
	public static final String CLIENT_FAMILY_MEMBER_CHECK_IF_MEMBER_HAS_LOANS_ASSETS = "MCCIPP-FI04";
	public static final String CLIENT_FAMILY_MEMBER_VIEW_ERROR = "MCCIPP-FI05";
	public static final String CLIENT_FAMILY_MEMBER_DELETE_ERROR = "MCCIPP-FI06";
	public static final String GLOBAL_FAMILY_MEMBER_ICON_ERROR = "MCCIPP-FI07";
	/*****************************************************************************************************************/
	// Client Contact Details
	public static final String CLIENT_CONTACT_MODULE = "Client Contact";
	public static final String CLIENT_CONTACT_ADD_ERROR = "MCCIPP-CD01";
	public static final String CLIENT_CONTACT_UPDATE_ERROR = "MCCIPP-CD02";
	public static final String CLIENT_CONTACT_GET_DATA_ERROR = "MCCIPP-CD03";
	public static final String CLIENT_CONTACT_CHECK_EMAIL_EXISTS_ERROR = "MCCIPP-CD04";
	public static final String CLIENT_CONTACT_CHECK_MOBILENO_EXISTS_ERROR = "MCCIPP-CD05";
	public static final String CLIENT_CONTACT_CHECK_UNIQUE_EMAIL_ERROR = "MCCIPP-CD06";
	public static final String CLIENT_CONTACT_CHECK_UNIQUE_MOBILENO_ERROR = "MCCIPP-CD07";
	//Master State
	public static final String MASTER_STATE_MODULE = "Master State";
	public static final String CLIENT_CONTACT_GET_STATE_DROPDOWN_ERROR = "MCCIPP-CD08";
	public static final String CLIENT_CONTACT_GET_CHECK_UNIQUE_PINCODE = "MCCIPP-UNIQUEPINCODE";

	/*****************************************************************************************************************/
	// Client Life Expectancy
	public static final String CLIENT_LIFE_EXPECTANCY_MODULE = "Client Life Expectancy";
	public static final String GET_FAMILY_MEMBER_BY_LIFE_EXPECTANCY_ERROR = "MCCIPP-LE01";
	public static final String CALCULATE_LIFE_EXPECTANCY_ERROR = "MCCIPP-LE02";
	public static final String SAVE_LIFE_EXPECTANCY_ERROR = "MCCIPP-LE03";
	public static final String CLIENT_LIFE_EXPECTANCY_VIEW_ERROR = "MCCIPP-LE04";
	public static final String CLIENT_LIFE_EXPECTANCY_GET_DATA_ERROR = "MCCIPP-LE05";
	public static final String CLIENT_LIFE_EXPECTANCY_UPDATE_ERROR = "MCCIPP-LE06";
	public static final String CLIENT_LIFE_EXPECTANCY_DELETE_ERROR = "MCCIPP-LE07";
	public static final String RE_CALCULATE_LIFE_EXPECTANCY_FOR_CLIENT_ERROR = "MCCIPP-LE08";
	public static final String RE_CALCULATE_LIFE_EXPECTANCY_FOR_FAMILY_MEMBER_ERROR = "MCCIPP-LE09";

	/*****************************************************************************************************************/
	// Lookup Frequency
	public static final String LOOKUP_FREQUENCY_MODULE = "Lookup Frequency";
	public static final String LOOKUP_FREQUENCY_VIEW_ERROR = "MCCI-FREQUENCY";

	/*****************************************************************************************************************/

	// My Client Client Information - Risk Profile:
	// Client Risk Profile Response
	public static final String MY_CLIENT_RISK_PROFILE_RESPONSE_MODULE = "Client Risk Profile Response";
	public static final String VIEW_RISK_PROFILE_ERROR = "MCCIRP-RP01";
	public static final String GET_RISK_PROFILE_QUESTIONS_ANSWERS_FOR_CLIENT_ERROR = "MCCIRP-RP02";
	public static final String MY_CLIENT_RISK_PROFILE_UPDATE_ERROR = "MCCIRP-RP03";

	/*****************************************************************************************************************/

	// My Business User Management - User Creation
	// Advisor User - Create User
	public static final String MY_BUSINESS_USER_CREATION_MODULE = "Advisor User - Create User";
	public static final String VIEW_USER_LIST_ERROR = "MBUMUC-CU01";
	public static final String DELETE_USER_RECORD_ERROR = "MBUMUC-CU02";
	// Advisor Role
	public static final String GET_ALL_EXISTING_ROLES_ERROR = "MBUMUC-EXISTINGROLES";
	public static final String MY_BUSINESS_USER_CREATION_ADD_ERROR = "MBUMUC-CU03";
	public static final String MY_BUSINESS_USER_CREATION_GET_DATA_ERROR = "MBUMUC-CU04";
	public static final String MY_BUSINESS_USER_CREATION_UPDATE_ERROR = "MBUMUC-CU05";
	public static final String ADD_EDIT_USER_CHECK_EMAIL_EXISTS_ERROR = "MBUMUC-CU06";
	public static final String ADD_EDIT_USER_CHECK_MOBILE_EXISTS_ERROR = "MBUMUC-CU07";
	public static final String ADD_USER_CHECK_UNIQUE_EMAIL_ERROR = "MBUMUC-CU08";
	public static final String ADD_USER_CHECK_UNIQUE_MOBILENO_ERROR = "MBUMUC-CU09";
	public static final String ADD_EDIT_USER_CHECK_EMPLOYEE_CODE_EXISTS_ERROR = "MBUMUC-CU10";
	public static final String ADD_USER_CHECK_UNIQUE_EMPLOYEE_CODE_ERROR = "MBUMUC-CU11";

	// Bulk Upload
	public static final String BULK_UPLOAD_MODULE = "Advisor User - Bulk Upload";
	public static final String DOWNLOAD_USER_TEMPLATE_ERROR = "MBUMUC-BU01";
	public static final String UPLOAD_BULK_USERS_ERROR = "MBUMUC-BU02";
	/*****************************************************************************************************************/

	// Manage Password
	public static final String MY_BUSINESS_MANAGE_PASSWORD_MODULE = "Manage Password";
	public static final String MY_BUSINESS_MANAGE_PASSWORD_VIEW_USERS_ERROR = "MBUMMP-MP01";
	public static final String MY_BUSINESS_MANAGE_PASSWORD_RESET_PASSWORD_ERROR = "MBUMMP-MP02";
	/*****************************************************************************************************************/

	// Advisor Role/ Supervisor Role - User Role Creation
	public static final String MY_BUSINESS_USER_ROLE_CREATION_MODULE = "Advisor Role/ Supervisor Role - User Role Creation";
	public static final String MY_BUSINESS_USER_ROLE_CREATION_VIEW_ERROR = "MBUMURC-URC01";
	public static final String MY_BUSINESS_USER_ROLE_CREATION_DELETE_ERROR = "MBUMURC-URC02";
	public static final String MY_BUSINESS_USER_ROLE_CREATION_UPDATE_ERROR = "MBUMURC-URC03";
	public static final String MY_BUSINESS_USER_ROLE_CREATION_ADD_ERROR = "MBUMURC-URC04";
	public static final String MY_BUSINESS_USER_ROLE_CHECK_UNIQUE_ROLE_ERROR = "MBUMURC-URC05";
	/*****************************************************************************************************************/

	// Advisor User Role ReMapping
	public static final String MY_BUSINESS_USER_ROLE_REMAPPING_MODULE = "Advisor User Role ReMapping";
	public static final String MY_BUISNESS_USER_ROLE_REMAPPING_VIEW_ERROR = "MBUMURR-URR01";
	public static final String MY_BUSINESS_USER_ROLE_REMAPPING_UPDATE_ERROR = "MBUMURR-URR02";
	public static final String MY_BUSINESS_USER_ROLE_REMAPPING_ADD_ERROR = "MBUMURR-URR03";
	/*****************************************************************************************************************/

	// Hierarchy Mapping
	public static final String MY_BUSINESS_HIERARCHY_MAPPING_MODULE = "Hierarchy Mapping";
	public static final String MY_BUSINESS_HIERARCHY_MAPPING_VIEW_ERROR = "MBUMHM-HM01";
	public static final String MY_BUSINESS_HIERARCHY_MAPPING_GET_SUPERVISOR_WITH_SAME_ROLE_ERROR = "MBUMHM-HM02";
	public static final String MY_BUSINESS_HIERARCHY_MAPPING_GET_DATA_ERROR = "MBUMHM-HM03";
	public static final String MY_BUSINESS_HIERARCHY_MAPPING_UPDATE_ERROR = "MBUMHM-HM04";
	public static final String MY_BUSINESS_HIERARCHY_MAPPING_ADD_ERROR = "MBUMHM-HM05";
	public static final String MY_BUSINESS_HIERARCHY_MAPPING_GET_ADVISOR_USER_WITH_SUPERVISOR_ROLE_ERROR = "MBUMHM-HM06";
	/*****************************************************************************************************************/

	// Access Rights
	public static final String MY_BUSINESS_ACCESS_RIGHTS_MODULE = "Access Rights";
	// Finexa Business Submodule
	public static final String FINEXA_BUSINESS_SUBMODULE = "Finexa Business Submodule";
	public static final String MY_BUSINESS_ACCESS_RIGHTS_GET_ALL_SUBMODULES_ERROR = "MBUMAR-AR01";
	// Finexa Business Module
	public static final String FINEXA_BUSINESS_MODULE = "Finexa Business Module";
	public static final String My_BUSINESS_ACCESS_RIGHTS_GET_ALL_MODULES_LIST_ERROR = "MBUMAR-AR02";
	public static final String MY_BUSINESS_ACCESS_RIGHTS_ADD_ERROR = "MBUMAR-AR03";
	public static final String MY_BUSINESS_ACCESS_RIGHTS_UPDATE_ERROR = "MBUMAR-AR04";
	/*****************************************************************************************************************/

	// Logged-In History
	public static final String MY_BUSINESS_LOGGING_HISTORY_MODULE = "Logging History";
	public static final String MY_BUSINESS_LOGGING_HISTORY_VIEW_ERROR = "MBUMLIH-LIH01";
	/*****************************************************************************************************************/

	// My Business - Client Records
	// Client Contact Management
	public static final String MY_BUSINESS_CLIENT_CONTACT_MANAGEMENT_MODULE = "Client Contact Management";
	public static final String GET_ORGANIZATIONS_OF_CLIENTS_LIST_ERROR = "MBCRCCM-CCM01";
	public static final String GET_CITIES_OF_CLIENTS_LIST_ERROR = "MBCRCCM-CCM02";
	public static final String SEARCH_CLIENT_MASTER_RECORDS = "MBCRCCM-CCM03";
	/*****************************************************************************************************************/

	// Import/Export Client Records
	public static final String MY_BUSINESS_IMPORT_EXPORT_CLIENT_RECORDS_MODULE = "Import/Export Client Records";
	public static final String MY_BUSINESS_IMPORT_CLIENT_RECORD_TEMPLATE_DOWNLOAD_ERROR = "MBCRIEC-ICR01";
	public static final String MY_BUSINESS_IMPORT_CLIENT_RECORD_UPLOAD_ERROR = "MBCRIEC-ICR02";
	public static final String MY_BUSINESS_EXPORT_CLIENT_RECORD_EXPORT_ERROR = "MBCRIEC-ECR01";
	/*****************************************************************************************************************/
	
	//Client Mapping
	//Client Remapping
	public static final String MY_BUSINESS_CLIENT_REMAPPING_MODULE = "Client Remapping";
	public static final String MY_BUSINESS_CLIENT_REMAPPING_VIEW_ERROR = "MBCRCM-CM01";
	public static final String MY_BUSINESS_CLIENT_REMAPPING_REMAP_CLIENT_ERROR = "MBCRCM-CM02";
	public static final String MY_BUSINESS_CLIENT_REMAPPING_DOWNLOAD_TEMPLATE_ERROR = "MBCRCM-CM03";
	public static final String MY_BUSINESS_CLIENT_REMAPPING_UPLOAD_CLIENT_REMAPPING = "MBCRCM-CM04";
	/*****************************************************************************************************************/
	
	//My Business - Masters
	//Other Masters > Upload Masters
	public static final String MY_BUSINESS_UPLOAD_MASTERS_MODULE = "Upload Master Data";
	public static final String MY_BUSINESS_UPLOAD_MASTERS_VIEW_ERROR = "MBMOM-UM01";
	public static final String MY_BUSINESS_UPLOAD_MASTERS_DOWNLOAD_CSV_TEMPLATE_ERROR = "MBMOM-UM02";
	public static final String MY_BUSINESS_UPLOAD_MASTERS_DOWNLOAD_EXCEL_TEMPLATE_ERROR = "MBMOM-UM03";
	public static final String MY_BUSINESS_UPLOAD_MASTERS_UPLOAD_CSV_ERROR = "MBMOM-UM04";
	public static final String MY_BUSINESS_UPLOAD_MASTERS_UPLOAD_EXCEL_ERROR = "MBMOM-UM05";
	/*****************************************************************************************************************/
	
	//Risk Profile Master
	public static final String MY_BUSINESS_RISK_PROFILE_MASTER_MODULE = "Risk Profile Master";
	public static final String MY_BUSINESS_RISK_PROFILE_MASTER_VIEW_ERROR = "MBMRPM-RPM01";
	public static final String MY_BUSINESS_RISK_PROFILE_MASTER_VIEW_QUESTIONS_ERROR = "MBMRPM-RPM02";
	public static final String MY_BUSINESS_RISK_PROFILE_MASTER_GET_QUESTIONS_ERROR = "MBMRPM-RPM03";
	public static final String MY_BUSINESS_RISK_PROFILE_MASTER_ADD_ERROR = "MBMRPM-RPM04";
	public static final String MY_BUSINESS_RISK_PROFILE_MASTER_ADD_QUESTIONS_ERROR = "MBMRPM-RPM05";
	public static final String MY_BUSINESS_RISK_PROFILE_MASTER_DELETE_ERROR = "MBMRPM-RPM06";
	public static final String MY_BUSINESS_RISK_PROFILE_MASTER_GET_SCORE_ERROR = "MBMRPM-RPM07";
	/*****************************************************************************************************************/
	
	//Finexa Subscription
	//subscription history
	public static final String MY_BUSINESS_FINEXA_SUBSCRIPTION_MODULE = "Finexa Subscription";
	public static final String MY_BUSINESS_FINEXA_SUBSCRIPTION_VIEW_ERROR = "MBFSSH-SH01";
	public static final String MY_BUSINESS_FINEXA_SUBSCRIPTION_GET_DATA_ERROR = "MBFSSH-SH02";
	//subscribe online
	public static final String MY_BUSINESS_FINEXA_SUBSCRIPTION_ADD_ERROR = "MBFSS-SO01";
	

	// SubModuleCode
	public static final String MY_CLIENT_PORTFOLIO = "MCCIP";
	public static final String MY_CLIENT_CLIENT_INFORMATION_LOANS = "MCCIL";
	public static final String MY_CLIENT_CLIENT_INFORMATION_INSURANCE = "MCCIIn";
	public static final String MY_CLIENT_CLIENT_INFORMATION_GOALS = "MCCIG";
	public static final String MY_CLIENT_CLIENT_INFORMATION_INCOME = "MCCII";
	public static final String MY_CLIENT_CLIENT_INFORMATION_EXPENSE = "MCCIE";
	public static final String MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE = "MCCIPP";
	public static final String MY_CLIENT_CLIENT_INFORMATION_RISK_PROFILE = "MCCIRP";

	public static final String MY_BUSINESS_USER_MANAGEMENT_USER_CREATION = "MBUMUC";
	public static final String MY_BUSINESS_USER_MANAGERMENT_MANAGE_PASSWORD = "MBUMMP";
	public static final String MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_CREATION = "MBUMURC";
	public static final String MY_BUSINESS_USER_MANAGEMENT_USER_ROLE_REMAPPING = "MBUMURR";
	public static final String MY_BUSINESS_USER_MANAGEMENT_HIERARCHY_MAPPING = "MBUMHM";
	public static final String MY_BUSINESS_USER_MANAGEMENT_ACCESS_RIGHTS = "MBUMAR";
	public static final String MY_BUSINESS_USER_MANAGERMENT_LOGGED_IN_HISTORY = "MBUMLIH";

	public static final String MY_BUSINESS_CLIENT_RECORDS_CLIENT_CONTACT_MANAGEMENT = "MBCRCCM";
	public static final String MY_BUSINESS_CLIENT_RECORDS_IMPORT_EXPORT_CLIENT = "MBCRIEC";
	public static final String MY_BUSINESS_CLIENT_RECORDS_CLIENT_MAPPING = "MBCRCM";
	
	public static final String MY_BUSINESS_MASTERS_OTHER_MASTERS = "MBMOM";
	public static final String MY_BUSINESS_MASTERS_RISK_PROFILE_MASTER = "MBMRPM";
	
	public static final String MY_BUSINESS_FINEXA_SUBSCRIPTION_SUBSCRIPTION_HISTORY = "MBFSSH";
	public static final String MY_BUSINESS_FINEXA_SUBSCRIPTION_SUBSCRIBE = "MBFSS";

	
	//Login
	public static final String FINEXA_LOGIN = "LOGINFP";
	public static final String FINEXA_LOGIN_FORGOT_PASS_CHECK_EXISTIN_EMAIL_ERROR = null;
	public static final String FINEXA_LOGIN_MODULE = null;
	
	/************MF Back Office Constants***********/
	public static final String MF_BACK_OFFICE = "MFBO";
	public static final String MF_BACK_OFFICE_RELATIONSHIP_MANAGER = "MFBORM";
	public static final String MF_BACK_OFFICE_RM_MANAGER_GET_BY_ADVISOR_ID_ERROR = "MFBO-RM01";
	public static final String MF_BACK_OFFICE_RM_MANAGER_FETCH_A_RM_ERROR = "MFBO-RM02";
	public static final String MF_BACK_OFFICE_RM_MANAGER_UPDATE_ERROR = "MFBO-RM03";
	public static final String MF_BACK_OFFICE_RM_MANAGER_DELETE_ERROR = "MFBO-RM04";
	public static final String MF_BACK_OFFICE_GET_RM_NAME_FOR_FAMILY_ATTR_MASTER_ERROR = "MFBO-RM05";
	
	
	// MCBPP
	// MCBI
	// MCBE
	// MCBL
	// MCBP
	// MCBIn
	// MCBBM
	// MCGPPP
	// MCGPG
	// MCGPRP
	// MCGPGP
	// MCPMPP
	// MCPML
	// MCPMP
	// MCPMRP
	// MCPMPM
	// MCFPPP
	// MCFPI
	// MCFPE
	// MCFPL
	// MCFPP
	// MCFPG
	// MCFPIn
	// MCFPRP
	// MCFPFP
	// MCFPPM
	// MCFPBM
	// MCFPGP
	// MCRPC
	// MBUMAR
	// MBCRCC
	// MBCRCM
	// MBCRIEC
	// MBMRPM
	// MBMPR
	// MBRCTC
	// MBRCRT
	// MBRSMR
	// MBRRBR
	// MBRSR
	// MBRCR
	// MBASH
	// MBASE
	// MBASC
	// MBORSET
	// MBFSSP
	// MBFSS
	// MBFSSH

	// Personal Profile
	// Income
	// Expenses
	// Loans
	// Portfolio
	// Goals
	// Insurance
	// Risk Profile
	// Personal Profile
	// Income
	// Expenses
	// Loans
	// Portfolio
	// Insurance
	// Budget Management
	// Personal Profile
	// Goals
	// Risk Profile
	// Goal Planning
	// Personal Profile
	// Loans
	// Portfolio
	// Risk Profile
	// Portfolio Management
	// Personal Profile
	// Income
	// Expenses
	// Loans
	// Portfolio
	// Goals
	// Insurance
	// Risk Profile
	// Financial Planning
	// Portfolio Management
	// Budget Management
	// Goal Planning
	// Product Calculators
	// User Creation
	// Manage Password
	// User Role Creation
	// User Role Mapping
	// Hierarchy Mapping
	// Access Rights
	// Client Contact Management
	// Client Mapping
	// Import/Export Clients
	// Risk Profile Master
	// Product Recommendation
	// Other Master
	// Template Customization
	// Recommendation Template
	// Sales Management Reports
	// Revenue Based Reports
	// Standardized Reports
	// Customise Report
	// Helpline
	// Email
	// Chat
	// Standard Email Template
	// Subscription Package
	// Subscribe
	// Subscription History
	
	public static final Byte GOAL_TYPE_RETIREMENT = 8;

	/******************************* Cache Constant **************************/
	public static final String CALCULATION_TYPE_CONSTANT = "CALCULATION";
	public static final String MASTER_TYPE_CONSTANT = "MASTER";
	
	/***********************Transact Module Related Constants*********************/
	public static final String ORDER_LUMPSUM = "lumpsum";
	public static final String ORDER_SIP = "sip";
	public static final String ORDER_SWP = "swp";
	public static final String ORDER_STP = "stp";
	public static final String ORDER_SWITCH = "switch";
	public static final String ORDER_REDEEM = "redeem";
	
	public static final String ALLOWED_FLAG_YES = "Y";
	public static final String ALLOWED_FLAG_NO = "N";
	
	public static final String ONLY_PHYSICAL = "P";
	public static final String ONLY_DEMAT_PHYSICAL = "DP";
	
	/***********************MF Back Office Report Names****************************/
	public static final String MF_REPORT_TYPE_SIP = "sip";
	public static final String MF_REPORT_TYPE_SWP = "swp";
	public static final String MF_REPORT_TYPE_STP = "stp";
	public static final String MF_REPORT_DIVIDEND = "dividend";
	public static final String MF_REPORT_DIVIDEND_NEW = "dividendNew";
	//public static final String MF_REPORT_CURRENT_HOLDING_REALIZED_GAIN = "realized";
	//public static final String MF_REPORT_CURRENT_HOLDING_UNREALIZED_GAIN = "unrealized";
	public static final String MF_REPORT_PORTFOLIO_GAIN_LOSS = "portfolioGainLoss";
	public static final String MF_REPORT_PORTFOLIO_VALUATION = "portfolioValuation";
	public static final String MF_REPORT_TRANSACTION = "transaction";
	public static final String MF_REPORT_TRANSACTION_NEW = "transactionNew";
	public static final String MF_REPORT_AUM = "aum";
	public static final String MF_REPORT_CAPITAL_GAINS = "capitalGains";
	public static final String MF_REPORT_INACTIVE_CLIENTS = "inactiveClient";
	public static final String MF_REPORT_BROKERAGE = "brokerage";
	public static final String MF_REPORT_IC_NO_PURCHASE = "purchase";
	public static final String MF_REPORT_IC_NO_SALE = "sale";
	
	/***********************Business MIS Report Constants**************************/
	public static final String BUSINESS_MIS_REPORT_OPTION_1 = "By Fund House";
	public static final String BUSINESS_MIS_REPORT_OPTION_2 = "By Scheme";
	public static final String BUSINESS_MIS_REPORT_OPTION_3 = "By Clients";
	public static final String BUSINESS_MIS_REPORT_OPTION_4 = "By Sub-Broker";
	public static final String BUSINESS_MIS_REPORT_OPTION_5 = "By RM";
	public static final String BUSINESS_MIS_REPORT_OPTION_6 = "By Fund Sub- asset Class";
	
	/************************Business Reports***************************************/
	public static final String BUSINESS_REPORT_AUM_RECONCILIATION = "aumReconciliation";
	public static final String BUSINESS_REPORT_BUSINESS_MIS = "businessMIS";
	public static final String BUSINESS_REPORT_TRANSACTION_SUMMARY = "transactionSummary";
	

}
