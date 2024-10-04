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
	PAYMENT_METHOD_EXISTED(400, "Payment method existed", HttpStatus.BAD_REQUEST),
	VEHICLE_TYPE_EXISTED(400, "Vehicle type existed", HttpStatus.BAD_REQUEST),
	ROUTE_EXISTED(400, "Route existed", HttpStatus.BAD_REQUEST),
	CUSTOMER_EXISTED(400, "Customer existed", HttpStatus.BAD_REQUEST),
	ACCOUNT_EXISTED(400, "Account existed", HttpStatus.BAD_REQUEST),
	VEHICLE_EXISTED(400, "Vehicle existed", HttpStatus.BAD_REQUEST),
	POSITION_EXISTED(400, "Position existed", HttpStatus.BAD_REQUEST),
	PRICE_EXISTED(400, "Price existed", HttpStatus.BAD_REQUEST),
	INVALID_KEY(400, "Invalid message key", HttpStatus.BAD_REQUEST),
	UNCATEGORIZED_EXCEPTION(500, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

	INVALID_DATE_TIME(404, "Invalid date time", HttpStatus.BAD_REQUEST),
	VEHICLE_ASSIGNMENT_EXISTED(404, "Vehicle assignment exists", HttpStatus.BAD_REQUEST),
	DURATION_INVALID(404, "Duration must be greater than 0", HttpStatus.BAD_REQUEST),
	DISTANCE_INVALID(404, "Distance must be greater than 0", HttpStatus.BAD_REQUEST),
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
