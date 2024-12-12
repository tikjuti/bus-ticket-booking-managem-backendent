package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.Utils.QueryableExtensions;
import com.tikjuti.bus_ticket_booking.dto.request.Trip.TripCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Trip.TripQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Trip.TripUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.TripResponse;
import com.tikjuti.bus_ticket_booking.entity.*;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.RouteMapper;
import com.tikjuti.bus_ticket_booking.mapper.TripMapper;
import com.tikjuti.bus_ticket_booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private TripMapper tripMapper;
    @Autowired
    private RouteMapper routeMapper;


    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @PreAuthorize("hasRole('ADMIN')")
    public Trip createTrip (TripCreationRequest request)
    {

//        Kiểm tra ngày đi và ngày đến có hợp lệ không

        // Phân tích chuỗi thành LocalDate và LocalTime
        LocalDate departureDate = LocalDate.parse(request.getDepartureDate().trim(), dateFormatter);
        LocalTime departureTime = LocalTime.parse(request.getDepartureTime().trim(), timeFormatter);

        LocalDate arrivalDate = LocalDate.parse(request.getArrivalDate().trim(), dateFormatter);
        LocalTime arrivalTime = LocalTime.parse(request.getArrivalTime().trim(), timeFormatter);

        // Kết hợp LocalDate và LocalTime thành LocalDateTime
        LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);
        LocalDateTime arrivalDateTime = LocalDateTime.of(arrivalDate, arrivalTime);

        if (!departureDateTime.isBefore(arrivalDateTime)) {
            throw new AppException(ErrorCode.INVALID_DATE_TIME);
        }

//        Kiểm tra có xe nào đã phân công trong khoảng thời gian này chưa

        Boolean checkVehicleAssignmentExists = tripRepository.checkVehicleAssignmentExists(
                request.getDepartureDate(),
                request.getDepartureTime(),
                request.getArrivalDate(),
                request.getArrivalTime(),
                request.getVehicleId(),
                null
        );

        if (checkVehicleAssignmentExists) {
            throw new AppException(ErrorCode.VEHICLE_ASSIGNMENT_EXISTED);
        }

        Vehicle vehicle = vehicleRepository
                .findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        boolean checkVehicleIsACTIVE = tripRepository.checkVehicleIsACTIVE(request.getVehicleId());

        if (checkVehicleIsACTIVE) {
            throw new AppException(ErrorCode.VEHICLE_NOT_ACTIVE);
        }

        Route route = routeRepository
                .findById(request.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found"));

        Trip trip = new Trip();

        trip.setDepartureDate(departureDate);
        trip.setDepartureTime(departureTime);
        trip.setArrivalDate(arrivalDate);
        trip.setArrivalTime(arrivalTime);
        trip.setVehicle(vehicle);
        trip.setRoute(route);

        return tripRepository.save(trip);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public PaginatedResult<Trip> getTrips(TripQueryRequest queryRequest) {
        Map<String, Object> filterParams = new HashMap<>();

        if (queryRequest.getId() != null)
            filterParams.put("id", queryRequest.getId());

        if (queryRequest.getDepartureDate() != null)
            filterParams.put("departureDate", queryRequest.getDepartureDate());

        if (queryRequest.getDepartureTime() != null)
            filterParams.put("departureTime", queryRequest.getDepartureTime());

        if (queryRequest.getArrivalDate() != null)
            filterParams.put("arrivalDate", queryRequest.getArrivalDate());

        if (queryRequest.getArrivalTime() != null)
            filterParams.put("arrivalTime", queryRequest.getArrivalTime());

        Specification<Trip> spec = Specification.where(
                        QueryableExtensions.<Trip>applyIncludes(queryRequest.getIncludes()))
                .and(QueryableExtensions.applyFilters(filterParams))
                .and(QueryableExtensions.applySorting(queryRequest.getSort()));

        return QueryableExtensions.applyPagination(
                tripRepository, spec, queryRequest.getPage(), queryRequest.getPageSize());
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public List<Trip> getUnassignedTrips() {
        return tripRepository.getUnassignedTrips();
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public TripResponse getTrip(String tripId)
    {
        return tripMapper.toTripResponse(tripRepository
                .findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found")));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public TripResponse updateTrip(TripUpdateRequest request, String tripId)
    {
        Trip trip = tripRepository
                .findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        // Phân tích chuỗi thành LocalDate và LocalTime
        LocalDate departureDate = LocalDate.parse(request.getDepartureDate().trim(), dateFormatter);
        LocalTime departureTime = LocalTime.parse(request.getDepartureTime().trim(), timeFormatter);

        LocalDate arrivalDate = LocalDate.parse(request.getArrivalDate().trim(), dateFormatter);
        LocalTime arrivalTime = LocalTime.parse(request.getArrivalTime().trim(), timeFormatter);

        // Kết hợp LocalDate và LocalTime thành LocalDateTime
        LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);
        LocalDateTime arrivalDateTime = LocalDateTime.of(arrivalDate, arrivalTime);

        if (!departureDateTime.isBefore(arrivalDateTime)) {
            throw new AppException(ErrorCode.INVALID_DATE_TIME);
        }

//        Kiểm tra có xe nào đã phân công trong khoảng thời gian này chưa

        Boolean checkVehicleAssignmentExists = tripRepository.checkVehicleAssignmentExists(
                request.getDepartureDate(),
                request.getDepartureTime(),
                request.getArrivalDate(),
                request.getArrivalTime(),
                request.getVehicleId(),
                tripId
        );

        if (checkVehicleAssignmentExists) {
            throw new AppException(ErrorCode.VEHICLE_ASSIGNMENT_EXISTED);
        }

        Vehicle vehicle = vehicleRepository
                .findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        boolean checkVehicleIsACTIVE = tripRepository.checkVehicleIsACTIVE(request.getVehicleId());

        if (checkVehicleIsACTIVE) {
            throw new AppException(ErrorCode.VEHICLE_NOT_ACTIVE);
        }

        Route route = routeRepository
                .findById(request.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found"));

        trip.setDepartureDate(departureDate);
        trip.setDepartureTime(departureTime);
        trip.setArrivalDate(arrivalDate);
        trip.setArrivalTime(arrivalTime);
        trip.setVehicle(vehicle);
        trip.setRoute(route);


        return tripMapper.toTripResponse(tripRepository.save(trip));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public TripResponse patchUpdateTrip(TripUpdateRequest request, String tripId) {
        Trip trip = tripRepository
                .findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository
                    .findById(request.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));

            boolean checkVehicleIsACTIVE = tripRepository.checkVehicleIsACTIVE(request.getVehicleId());

            if (checkVehicleIsACTIVE) {
                throw new AppException(ErrorCode.VEHICLE_NOT_ACTIVE);
            }
            trip.setVehicle(vehicle);
        }

        if (request.getRouteId() != null) {
            Route route = routeRepository
                    .findById(request.getRouteId())
                    .orElseThrow(() -> new RuntimeException("Route not found"));
            trip.setRoute(route);
        }

        LocalDate departureDate;
        LocalTime departureTime;
        LocalDate arrivalDate;
        LocalTime arrivalTime;
        String vehicleId;

        if (request.getDepartureDate() != null) {
            departureDate = LocalDate.parse(request.getDepartureDate().trim(), dateFormatter);
        } else departureDate = trip.getDepartureDate();
        if (request.getDepartureTime() != null) {
            departureTime = LocalTime.parse(request.getDepartureTime().trim(), timeFormatter);
        } else departureTime = trip.getDepartureTime();
        if (request.getArrivalDate() != null) {
            arrivalDate = LocalDate.parse(request.getArrivalDate().trim(), dateFormatter);
        } else arrivalDate = trip.getArrivalDate();
        if (request.getArrivalTime() != null) {
            arrivalTime = LocalTime.parse(request.getArrivalTime().trim(), timeFormatter);
        } else arrivalTime = trip.getArrivalTime();

        LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);
        LocalDateTime arrivalDateTime = LocalDateTime.of(arrivalDate, arrivalTime);

        if (!departureDateTime.isBefore(arrivalDateTime)) {
            throw new AppException(ErrorCode.INVALID_DATE_TIME);
        }

        if (request.getVehicleId() != null) {
            vehicleId = request.getVehicleId();
        } else vehicleId = trip.getVehicle().getId();

        Boolean checkVehicleAssignmentExists = tripRepository.checkVehicleAssignmentExists(
                departureDate.toString(),
                departureTime.toString(),
                arrivalDate.toString(),
                arrivalTime.toString(),
                vehicleId,
                tripId
        );

        if (checkVehicleAssignmentExists) {
            throw new AppException(ErrorCode.VEHICLE_ASSIGNMENT_EXISTED);
        }

        trip.setDepartureDate(departureDate);
        trip.setDepartureTime(departureTime);
        trip.setArrivalDate(arrivalDate);
        trip.setArrivalTime(arrivalTime);


        return tripMapper.toTripResponse(tripRepository.save(trip));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTrip(String tripId) {
        tripRepository.findById(tripId)
                .map(trip -> {
                    tripRepository.delete(trip);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Trip not found for ID: " + tripId));
    }
}
