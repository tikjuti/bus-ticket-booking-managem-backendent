package com.tikjuti.bus_ticket_booking.dto.request.DriverAssignmentForVehicle;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverAssignmentForVehicleUpdateRequest {
    String startDate;
    String endDate;
    String vehicleId;
    String employeeId;
}
