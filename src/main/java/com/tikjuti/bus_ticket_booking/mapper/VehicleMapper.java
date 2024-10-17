package com.tikjuti.bus_ticket_booking.mapper;

import com.tikjuti.bus_ticket_booking.dto.response.VehicleResponse;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleResponse toVehicleResponse(Vehicle vehicle);

    @Mapping(target = "vehicleName", source = "vehicleName")
    @Mapping(target = "color", source = "color")
    @Mapping(target = "status", source = "status")
    VehicleResponse toUpdateVehicleResponse(Vehicle vehicle);

}