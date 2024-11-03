package com.tikjuti.bus_ticket_booking.dto.request.Vehicle;

import com.tikjuti.bus_ticket_booking.enums.VehicleStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleCreationRequest {
    int seatCount;
    String licensePlate;
    String vehicleName;
    String color;
    VehicleStatus status = VehicleStatus.ACTIVE;
    String vehicleType;
}
