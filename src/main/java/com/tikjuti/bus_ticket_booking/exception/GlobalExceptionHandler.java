package com.tikjuti.bus_ticket_booking.exception;

import com.tikjuti.bus_ticket_booking.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = Exception.class)
	ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception)
	{
		ApiResponse apiResponse = new ApiResponse();

		apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
		apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

		return ResponseEntity
				.badRequest()
				.body(apiResponse);
	}

	@ExceptionHandler(value = RuntimeException.class)
	ResponseEntity<ApiResponse> hanlingNotFoundException(RuntimeException exception)
	{
		ApiResponse apiResponse = new ApiResponse();

		apiResponse.setMessage(exception.getMessage());
		apiResponse.setCode(HttpStatus.NOT_FOUND.value());

		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(apiResponse);
	}
	
	@ExceptionHandler(value = AppException.class)
	ResponseEntity<ApiResponse> handlingAppException(AppException exception)
	{
		ErrorCode errorCode = exception.getErrorCode();
		ApiResponse apiResponse = new ApiResponse();
		
		apiResponse.setCode(errorCode.getCode());
		apiResponse.setMessage(errorCode.getMessage());
		
		return ResponseEntity
				.status(errorCode.getStatusCode())
				.body(apiResponse);
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception)
	{
		String enumKey = exception.getFieldError().getDefaultMessage();

		ErrorCode errorCode = ErrorCode.INVALID_KEY;

		try {
			errorCode = ErrorCode.valueOf(enumKey);
		} catch (Exception e) {

		}

		ApiResponse apiResponse = new ApiResponse();
		
		apiResponse.setCode(errorCode.getCode());
		apiResponse.setMessage(errorCode.getMessage());
		
		return ResponseEntity
				.status(errorCode.getStatusCode())
				.body(apiResponse);
	}
}
