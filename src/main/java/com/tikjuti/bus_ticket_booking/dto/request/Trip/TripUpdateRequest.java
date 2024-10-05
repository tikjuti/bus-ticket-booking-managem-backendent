package com.tikjuti.bus_ticket_booking.dto.request.Trip;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripUpdateRequest {
    String departureDate;
    String departureTime;
    String arrivalDate;
    String arrivalTime;
    String vehicleId;
    String routeId;
}
