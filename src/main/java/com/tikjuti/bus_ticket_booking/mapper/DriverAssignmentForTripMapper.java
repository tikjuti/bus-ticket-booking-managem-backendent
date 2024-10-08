package com.tikjuti.bus_ticket_booking.mapper;

import com.tikjuti.bus_ticket_booking.dto.response.DriverAssignmentForTripResponse;
import com.tikjuti.bus_ticket_booking.entity.DriverAssignmentForTrip;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverAssignmentForTripMapper {
    DriverAssignmentForTripResponse toDriverAssignmentForTripResponse(DriverAssignmentForTrip driverAssignmentForTrip);
}
