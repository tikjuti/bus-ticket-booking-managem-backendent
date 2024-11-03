package com.tikjuti.bus_ticket_booking.dto.request.Employee;

import com.tikjuti.bus_ticket_booking.enums.EmployeeStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeCreationRequest {
    String employeeName;
    String gender;
    String address;
    String phone;
    String email;
    String dob;
    String nationalIDNumber;
    EmployeeStatus status = EmployeeStatus.ACTIVE;
    String employeeTypeId;
}
