package com.tikjuti.bus_ticket_booking.dto.request.Route;

import com.tikjuti.bus_ticket_booking.dto.request.Price.PriceUpdateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

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
