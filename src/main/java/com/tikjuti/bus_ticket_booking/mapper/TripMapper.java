package com.tikjuti.bus_ticket_booking.mapper;

import com.tikjuti.bus_ticket_booking.dto.response.TripResponse;
import com.tikjuti.bus_ticket_booking.entity.Trip;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripMapper {

    TripResponse toTripResponse(Trip trip);

}