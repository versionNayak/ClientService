package com.finlabs.finexa.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finlabs.finexa.service.ClientLifeInsuranceServiceImpl;

public class CustomFinexaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String moduleName;
	private String errorCode;
	private String errorMsg;
	private Exception ex;

	public static final Logger LOGGER = LoggerFactory.getLogger(CustomFinexaException.class);

	public CustomFinexaException(String moduleName, String errorCode, String errorMsg) {
		this.moduleName = moduleName;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	public CustomFinexaException(String moduleName, String errorCode, String errorMsg, Exception ex) {
		this.moduleName = moduleName;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.ex = ex;
	}

	public String getModuleName() {
		return moduleName;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public Exception getEx() {
		return ex;
	}

	public void setEx(Exception ex) {
		this.ex = ex;
	}

	public static void logCustomFinexaBusinessException(CustomFinexaException ospEx) {
		if (null != ospEx) {
			Map<String, Object> errorMap = new HashMap<String, Object>();
			errorMap.put("Error Code", ospEx.getErrorCode());
			errorMap.put("Error Description", ospEx.getErrorMsg());
			StringBuffer sb = new StringBuffer();
			Set<Map.Entry<String, Object>> entrySet = errorMap.entrySet();
			sb.append("Module Name:" + ospEx.getModuleName() + "||");
			for (Entry<String, Object> entry : entrySet) {
				sb.append(entry.getKey() + ":" + entry.getValue() + "||");
			}
			LOGGER.error(sb.toString(), ospEx.getEx());
		}
	}

}
