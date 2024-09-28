package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.Route.RouteCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Route.RouteUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.RouteResponse;
import com.tikjuti.bus_ticket_booking.entity.Route;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.RouteMapper;
import com.tikjuti.bus_ticket_booking.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RouteMapper routeMapper;

    public Route createRoute(RouteCreationRequest request)
    {
        Boolean exitsRoute = routeRepository.checkRoute(
                request.getDepartureLocation(),
                request.getArrivalLocation(),
                request.getDeparturePoint(),
                request.getArrivalPoint(),
                request.getDistance(),
                request.getDuration()
        );
        if(!exitsRoute)
            throw new AppException(ErrorCode.ROUTE_EXISTED);

        Route route = routeMapper.toRoute(request);

        return routeRepository
                .save(route);
    }

    public List<Route> getRoutes() {return routeRepository.findAll();}

    public RouteResponse getRoute(String routeId)
    {
        return routeMapper
                .toRouteResponse(routeRepository.findById(routeId)
                        .orElseThrow(() -> new RuntimeException("Route not found")));
    }

    public RouteResponse updateRoute(RouteUpdateRequest request, String routeId)
    {
        Route route = routeRepository
                .findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        Boolean exitsRoute = routeRepository.checkRoute(
                request.getDepartureLocation(),
                request.getArrivalLocation(),
                request.getDeparturePoint(),
                request.getArrivalPoint(),
                request.getDistance(),
                request.getDuration()
        );
        if(!exitsRoute)
            throw new AppException(ErrorCode.ROUTE_EXISTED);

        routeMapper.updateRoute(route, request);

        return routeMapper
                .toRouteResponse(routeRepository.save(route));
    }

    public RouteResponse patchUpdateRoute(RouteUpdateRequest request, String routeId) {
        Route route = routeRepository
                .findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        Boolean existsRoute = routeRepository.checkRoute(
                request.getDepartureLocation() != null ? request.getDepartureLocation() : route.getDepartureLocation(),
                request.getArrivalLocation() != null ? request.getArrivalLocation() : route.getArrivalLocation(),
                request.getDeparturePoint() != null ? request.getDeparturePoint() : route.getDeparturePoint(),
                request.getArrivalPoint() != null ? request.getArrivalPoint() : route.getArrivalPoint(),
                request.getDistance() != 0 ? request.getDistance() : route.getDistance(),
                request.getDuration() != 0 ? request.getDuration() : route.getDuration()
        );

        if (!existsRoute) {
            throw new AppException(ErrorCode.ROUTE_EXISTED);
        }

        if (request.getDepartureLocation() != null) {
            route.setDepartureLocation(request.getDepartureLocation());
        }

        if (request.getArrivalLocation() != null) {
            route.setArrivalLocation(request.getArrivalLocation());
        }

        if (request.getDeparturePoint() != null) {
            route.setDeparturePoint(request.getDeparturePoint());
        }

        if (request.getArrivalPoint() != null) {
            route.setArrivalPoint(request.getArrivalPoint());
        }

        if (request.getDistance() != 0 && request.getDistance() > 0) {
            route.setDistance(request.getDistance());
        }

        if (request.getDuration() != 0 && request.getDuration() > 0) {
            route.setDuration(request.getDuration());
        }

        Route updatedRoute = routeRepository.save(route);

        return routeMapper.toRouteResponse(updatedRoute);
    }


    public void deleteRoute(String routeId) {
        routeRepository.findById(routeId)
                .map(route -> {
                    routeRepository.delete(route);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Route not found for ID: " + routeId));
    }
}
