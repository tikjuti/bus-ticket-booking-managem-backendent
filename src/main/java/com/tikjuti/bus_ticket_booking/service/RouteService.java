package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.Utils.QueryableExtensions;
import com.tikjuti.bus_ticket_booking.dto.request.Route.RouteCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Route.RouteQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Route.RouteUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.RouteResponse;
import com.tikjuti.bus_ticket_booking.entity.Route;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.RouteMapper;
import com.tikjuti.bus_ticket_booking.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RouteMapper routeMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public Route createRoute(RouteCreationRequest request)
    {
        Boolean exitsRoute = routeRepository.checkRoute(
                request.getDepartureLocation(),
                request.getArrivalLocation(),
                request.getDeparturePoint(),
                request.getArrivalPoint()
        );
        if(!exitsRoute)
            throw new AppException(ErrorCode.ROUTE_EXISTED);

        Route route = routeMapper.toRoute(request);

        return routeRepository
                .save(route);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public PaginatedResult<Route> getRoutes(RouteQueryRequest queryRequest) {
        Map<String, Object> filterParams = new HashMap<>();

        if (queryRequest.getId() != null)
            filterParams.put("id", queryRequest.getId());

        if (queryRequest.getDepartureLocation() != null)
            filterParams.put("departureLocation", queryRequest.getDepartureLocation());

        if (queryRequest.getArrivalLocation() != null)
            filterParams.put("arrivalLocation", queryRequest.getArrivalLocation());

        if (queryRequest.getDeparturePoint() != null)
            filterParams.put("departurePoint", queryRequest.getDeparturePoint());

        if (queryRequest.getArrivalPoint() != null)
            filterParams.put("arrivalPoint", queryRequest.getArrivalPoint());

        if (queryRequest.getDistance() != null)
            filterParams.put("distance", queryRequest.getDistance());

        if (queryRequest.getDuration() != null)
            filterParams.put("duration", queryRequest.getDuration());

        Specification<Route> spec = Specification.where(
                        QueryableExtensions.<Route>applyIncludes(queryRequest.getIncludes()))
                .and(QueryableExtensions.applyFilters(filterParams))
                .and(QueryableExtensions.applySorting(queryRequest.getSort()));

        return QueryableExtensions.applyPagination(
                routeRepository, spec, queryRequest.getPage(), queryRequest.getPageSize());
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public RouteResponse getRoute(String routeId)
    {
        return routeMapper
                .toRouteResponse(routeRepository.findById(routeId)
                        .orElseThrow(() -> new RuntimeException("Route not found")));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public RouteResponse updateRoute(RouteUpdateRequest request, String routeId)
    {
        Route route = routeRepository
                .findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        Boolean exitsRoute = routeRepository.checkRoute(
                request.getDepartureLocation(),
                request.getArrivalLocation(),
                request.getDeparturePoint(),
                request.getArrivalPoint()
        );
        if(!exitsRoute)
            throw new AppException(ErrorCode.ROUTE_EXISTED);

        routeMapper.updateRoute(route, request);

        return routeMapper
                .toRouteResponse(routeRepository.save(route));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public RouteResponse patchUpdateRoute(RouteUpdateRequest request, String routeId) {
        Route route = routeRepository
                .findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        Boolean existsRoute = routeRepository.checkRoute(
                request.getDepartureLocation() != null ? request.getDepartureLocation() : route.getDepartureLocation(),
                request.getArrivalLocation() != null ? request.getArrivalLocation() : route.getArrivalLocation(),
                request.getDeparturePoint() != null ? request.getDeparturePoint() : route.getDeparturePoint(),
                request.getArrivalPoint() != null ? request.getArrivalPoint() : route.getArrivalPoint()
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

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRoute(String routeId) {
        routeRepository.findById(routeId)
                .map(route -> {
                    routeRepository.delete(route);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Route not found for ID: " + routeId));
    }
}
