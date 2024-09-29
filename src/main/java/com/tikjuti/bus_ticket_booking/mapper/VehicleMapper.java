package com.tikjuti.bus_ticket_booking.mapper;

import com.tikjuti.bus_ticket_booking.dto.request.Account.AccountCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Account.AccountUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Vehicle.VehicleCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Vehicle.VehicleUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.AccountResponse;
import com.tikjuti.bus_ticket_booking.dto.response.SeatResponse;
import com.tikjuti.bus_ticket_booking.dto.response.VehicleResponse;
import com.tikjuti.bus_ticket_booking.entity.Account;
import com.tikjuti.bus_ticket_booking.entity.Seat;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Named("basic")
    VehicleResponse toVehicleResponse(Vehicle vehicle);

    SeatResponse toSeatResponse(Seat seat);

    default Set<SeatResponse> mapSeats(Set<Seat> seats) {
        return seats.stream()
                .map(this::toSeatResponse)
                .collect(Collectors.toSet());
    }

    @Named("withSeats")
    default VehicleResponse toVehicleResponseWithSeats(Vehicle vehicle) {
        VehicleResponse response = toVehicleResponse(vehicle);
        response.setSeats(mapSeats(vehicle.getSeats()));
        return response;
    }

}
