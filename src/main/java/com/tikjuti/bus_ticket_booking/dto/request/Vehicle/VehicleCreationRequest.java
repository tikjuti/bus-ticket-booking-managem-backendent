package com.tikjuti.bus_ticket_booking.dto.request.Vehicle;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    public enum Status {
        ACTIVE,
        INACTIVE,
        MAINTENANCE,
        OUT_OF_SERVICE
    }
    int seatCount;
    String licensePlate;
    String vehicleName;
    String color;
    Status status;
    String vehicleType;
}
