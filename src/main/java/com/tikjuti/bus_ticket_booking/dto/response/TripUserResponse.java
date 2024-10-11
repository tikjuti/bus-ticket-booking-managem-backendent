package com.tikjuti.bus_ticket_booking.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripUserResponse {
    String tripId;
    LocalDate departureDate;
    LocalTime departureTime;
    LocalDate arrivalDate;
    LocalTime arrivalTime;
    String vehicleName;

    RouteResponse route;

    int ticketPrice;
    int availableSeats;
}
