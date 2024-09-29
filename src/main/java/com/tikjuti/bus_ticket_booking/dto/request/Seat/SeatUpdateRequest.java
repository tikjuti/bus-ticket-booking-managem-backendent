package com.tikjuti.bus_ticket_booking.dto.request.Seat;

import com.tikjuti.bus_ticket_booking.enums.SeatStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatUpdateRequest {
    SeatStatus status;
    String position;
    String vehicle;
}
