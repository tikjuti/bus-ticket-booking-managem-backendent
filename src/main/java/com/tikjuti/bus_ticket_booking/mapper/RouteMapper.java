package com.tikjuti.bus_ticket_booking.mapper;

import com.tikjuti.bus_ticket_booking.dto.request.Route.RouteCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Route.RouteUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.RouteResponse;
import com.tikjuti.bus_ticket_booking.entity.Route;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RouteMapper {
    Route toRoute(RouteCreationRequest request);

    RouteResponse toRouteResponse(Route route);

    void updateRoute(@MappingTarget Route route, RouteUpdateRequest request);
}
