package com.tikjuti.bus_ticket_booking.dto.request.Vehicle;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleUpdateRequest {
    int seatCount;
    String licensePlate;
    String vehicleName;
    String color;
    String status;
    String vehicleType;
}
