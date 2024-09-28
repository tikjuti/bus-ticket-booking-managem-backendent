package com.tikjuti.bus_ticket_booking.dto.request.EmployeeType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeTypeCreationRequest {
    String nameEmployeeType;
}
