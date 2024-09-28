package com.tikjuti.bus_ticket_booking.dto.request.Price;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceCreationRequest {
    int ticketPrice;
    String vehicleTypeId;
    String routeId;
}
