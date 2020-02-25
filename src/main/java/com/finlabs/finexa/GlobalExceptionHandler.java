package com.finlabs.finexa;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;

import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.util.FinexaConstant;

@ControllerAdvice  
@RestController
public class GlobalExceptionHandler {
	
/*	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<?> handleDataAccessException(Exception ex) {
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setErrorCode("DataAccessException");
		errorDTO.setErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorDTO>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}*/
	
	@ExceptionHandler(value = { FinexaBussinessException.class })
	protected ResponseEntity<ErrorDTO> handleException(FinexaBussinessException finexaBussExp,
			WebRequest request) {
		FinexaBussinessException.logFinexaBusinessException(finexaBussExp);
		ErrorDTO errorResponse = new ErrorDTO();
		errorResponse.setErrorCode(finexaBussExp.getErrorCode());
		errorResponse.setErrorMessage(finexaBussExp.getErrorDescription());
		return new ResponseEntity<ErrorDTO>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { CustomFinexaException.class })
	protected ResponseEntity<ErrorDTO> handleCustomeException(CustomFinexaException finexaBussExp,
			WebRequest request) {
		CustomFinexaException.logCustomFinexaBusinessException(finexaBussExp);
		ErrorDTO errorResponse = new ErrorDTO();
		errorResponse.setErrorCode(FinexaConstant.ERROR_MESSAGE_CODE);
		errorResponse.setErrorMessage(finexaBussExp.getErrorMsg());
		return new ResponseEntity<ErrorDTO>(errorResponse, HttpStatus.OK);
	}
	
	@ExceptionHandler(MultipartException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
	public ResponseEntity<ErrorDTO> multipartExceptionHandler(MultipartException e)
	{
	    Throwable th = e.getCause();
	    if( th instanceof IllegalStateException )
	    {
	        Throwable cause = th.getCause();
	        if(cause instanceof  SizeLimitExceededException)
	        {
	            SizeLimitExceededException ex = (SizeLimitExceededException) cause;
	            ErrorDTO errorResponse = new ErrorDTO();
	            errorResponse.setErrorCode(String.valueOf(HttpStatus.PAYLOAD_TOO_LARGE.value()));
	            errorResponse.setErrorMessage("Total size of file(s) should not be more than " + (int)(ex.getPermittedSize()/Math.pow(2, 20)) + " MB");
	            return new ResponseEntity<ErrorDTO>(errorResponse, HttpStatus.OK);
	        }
	    }
	    ErrorDTO errorResponse = new ErrorDTO();
        errorResponse.setErrorCode(String.valueOf(HttpStatus.PAYLOAD_TOO_LARGE.value()));
	    return new ResponseEntity<ErrorDTO>(errorResponse, HttpStatus.OK);
	}
	
/*	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException(Exception ex) {
		ErrorDTO errorResponse = new ErrorDTO();
		errorResponse.setErrorCode("Exception");
		errorResponse.setErrorMessage(ex.getMessage());
		//FinexaBussinessException.logFinexaBusinessException(finexaBussExp);
		return new ResponseEntity<ErrorDTO>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}*/
	
}

