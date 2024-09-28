package com.tikjuti.bus_ticket_booking.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceResponse {
    String id;
    int ticketPrice;
    VehicleTypeResponse vehicleType;
    RouteResponse route;
}
