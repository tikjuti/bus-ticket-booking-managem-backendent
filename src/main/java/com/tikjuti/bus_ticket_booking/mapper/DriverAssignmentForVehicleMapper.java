package com.tikjuti.bus_ticket_booking.mapper;

import com.tikjuti.bus_ticket_booking.dto.response.DriverAssignmentForVehicleResponse;
import com.tikjuti.bus_ticket_booking.entity.DriverAssignmentForVehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverAssignmentForVehicleMapper {
    DriverAssignmentForVehicleResponse toDriverAssignmentForVehicleResponse(
            DriverAssignmentForVehicle driverAssignmentForVehicle);
}
