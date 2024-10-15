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
	USERNAME_EXISTED(400, "Username existed", HttpStatus.BAD_REQUEST),
	EMAIL_EXISTED(400, "Email existed", HttpStatus.BAD_REQUEST),
	PHONE_EXISTED(400, "Phone existed", HttpStatus.BAD_REQUEST),
	DRIVER_CAN_NOT_OPERATE_VEHICLE(400, "Driver can not operate vehicle", HttpStatus.BAD_REQUEST),
	DRIVER_ASSIGNMENT_FOR_TRIP_EXISTED(400, "Driver assignment for trip existed", HttpStatus.BAD_REQUEST),
	EMPLOYEE_NOT_A_DRIVER(400, "Employee is not a driver", HttpStatus.BAD_REQUEST),
	EMPLOYEE_NOT_A_BOOKING(400, "The employee is not making a booking", HttpStatus.BAD_REQUEST),
	NATIONAL_ID_NUMBER_EXISTED(400, "National ID number existed", HttpStatus.BAD_REQUEST),
	INVALID_KEY(400, "Invalid message key", HttpStatus.BAD_REQUEST),
	UNCATEGORIZED_EXCEPTION(500, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
	UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED(401, "You do not have permission", HttpStatus.FORBIDDEN),
	OTP_EXPIRED(404, "OTP expired", HttpStatus.EXPECTATION_FAILED),
	PASSWORD_NOT_MATCH(404, "Password not match", HttpStatus.BAD_REQUEST),

	INVALID_DATE_TIME(404, "Invalid date time", HttpStatus.BAD_REQUEST),
	INVALID_DATE(404, "Invalid date", HttpStatus.BAD_REQUEST),
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
