package com.tikjuti.bus_ticket_booking.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeTypeResponse {
    String id;
    String nameEmployeeType;
}
