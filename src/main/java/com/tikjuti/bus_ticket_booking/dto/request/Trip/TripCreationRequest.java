package com.tikjuti.bus_ticket_booking.dto.request.Trip;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripCreationRequest {
    String departureDate;
    String departureTime;
    String arrivalDate;
    String arrivalTime;
    String vehicleId;
    String routeId;
}