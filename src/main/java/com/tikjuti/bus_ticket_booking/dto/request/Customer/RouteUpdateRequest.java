package com.tikjuti.bus_ticket_booking.dto.request.Customer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteUpdateRequest {
    String departureLocation;
    String arrivalLocation;
    String departurePoint;
    String arrivalPoint;
    int distance;
    int duration;
}
