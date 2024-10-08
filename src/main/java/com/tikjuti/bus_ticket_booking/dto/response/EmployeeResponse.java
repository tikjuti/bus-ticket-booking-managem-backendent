package com.tikjuti.bus_ticket_booking.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tikjuti.bus_ticket_booking.enums.EmployeeStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponse {
    String id;
    String employeeName;
    String gender;
    String address;
    String phone;
    String email;
    LocalDate dob;
    String nationalIDNumber;
    Timestamp createdAt;
    String status;
    EmployeeTypeResponse employeeType;
    AccountResponse account;
}
