package com.tikjuti.bus_ticket_booking.mapper;

import com.tikjuti.bus_ticket_booking.dto.response.SeatResponse;
import com.tikjuti.bus_ticket_booking.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    @Mapping(target = "vehicle", source = "vehicle")
    SeatResponse toSeatResponse(Seat seat);

    Set<SeatResponse> toListSeatResponse (Set<Seat> seats);

    @Mapping(target = "vehicle", source = "vehicle")
    Seat toSeat(SeatResponse seatResponse);
}
