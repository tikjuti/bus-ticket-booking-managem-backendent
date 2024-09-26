package com.tikjuti.bus_ticket_booking.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
	
	EMPLOYEE_TYPE_EXISTED(400, "Employee type existed", HttpStatus.BAD_REQUEST),
	INVALID_KEY(400, "Invalid message key", HttpStatus.BAD_REQUEST),
	UNCATEGORIZED_EXCEPTION(500, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
	USERNAME_INVALID(404, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
	PASSWORD_INVALID(404, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
	;
	
	int code;
	String message;
	HttpStatusCode statusCode;

	ErrorCode(int code, String message, HttpStatusCode statusCode) {
		this.code = code;
		this.message = message;
		this.statusCode = statusCode;
	}
}
