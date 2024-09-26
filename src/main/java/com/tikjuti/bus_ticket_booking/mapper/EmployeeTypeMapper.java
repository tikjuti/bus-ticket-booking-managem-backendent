package com.tikjuti.bus_ticket_booking.mapper;

import com.tikjuti.bus_ticket_booking.dto.request.EmployeeTypeCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.EmployeeTypeUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.EmployeeTypeResponse;
import com.tikjuti.bus_ticket_booking.entity.EmployeeType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeTypeMapper {
    EmployeeType toEmployeeType(EmployeeTypeCreationRequest request);

    EmployeeTypeResponse toEmployeeTypeResponse(EmployeeType employeeType);

    void updateEmployeeType(@MappingTarget EmployeeType employeeType, EmployeeTypeUpdateRequest request);
}
