package com.tikjuti.bus_ticket_booking.mapper;

import com.tikjuti.bus_ticket_booking.dto.request.VehicleType.VehicleTypeCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.VehicleType.VehicleTypeUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.VehicleTypeResponse;
import com.tikjuti.bus_ticket_booking.entity.VehicleType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehicleTypeMapper {
    VehicleType toVehicleType(VehicleTypeCreationRequest request);

    VehicleTypeResponse toVehicleTypeResponse(VehicleType vehicleType);

    void updateVehicleType(@MappingTarget VehicleType vehicleType, VehicleTypeUpdateRequest request);
}
