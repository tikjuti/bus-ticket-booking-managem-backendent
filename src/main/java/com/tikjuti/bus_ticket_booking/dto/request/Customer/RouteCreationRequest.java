package com.tikjuti.bus_ticket_booking.dto.request.Customer;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteCreationRequest {
    String departureLocation;
    String arrivalLocation;
    String departurePoint;
    String arrivalPoint;
    @Min(value = 1, message = "DISTANCE_INVALID")
    int distance;
    @Min(value = 1, message = "DURATION_INVALID")
    int duration;
}
