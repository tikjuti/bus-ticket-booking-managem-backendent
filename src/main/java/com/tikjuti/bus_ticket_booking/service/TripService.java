package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.Trip.FindTripsUserRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Trip.TripCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Trip.TripUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.RouteResponse;
import com.tikjuti.bus_ticket_booking.dto.response.TripResponse;
import com.tikjuti.bus_ticket_booking.dto.response.TripUserResponse;
import com.tikjuti.bus_ticket_booking.entity.*;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.RouteMapper;
import com.tikjuti.bus_ticket_booking.mapper.TripMapper;
import com.tikjuti.bus_ticket_booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



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

    public List<Trip> getTrips() {return tripRepository.findAll();}

    public TripResponse getTrip(String tripId)
    {
        return tripMapper.toTripResponse(tripRepository
                .findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found")));
    }

    @Transactional
    public List<TripUserResponse> findTripsByUser(FindTripsUserRequest request) {
        List<Object[]> trips = tripRepository.findTripsByUser(
                request.getDepartureLocation(), request.getArrivalLocation(), request.getDepartureDate());

        List<TripUserResponse> tripUserResponses = new ArrayList<>();

        for (Object[] trip : trips) {
            TripUserResponse tripUserResponse = new TripUserResponse();

            java.sql.Date departureDateSql = (Date) trip[1];
            Time departureTimeSql = (Time) trip[2];
            java.sql.Date arrivalDateSql = (Date) trip[3];
            Time arrivalTimeSql = (Time) trip[4];

            LocalDate departureDate = departureDateSql.toLocalDate();
            LocalTime departureTime = departureTimeSql.toLocalTime();
            LocalDate arrivalDate = arrivalDateSql.toLocalDate();
            LocalTime arrivalTime = arrivalTimeSql.toLocalTime();
            
            tripUserResponse.setTripId((String) trip[0]);
            tripUserResponse.setDepartureDate(departureDate);
            tripUserResponse.setDepartureTime(departureTime);
            tripUserResponse.setArrivalDate(arrivalDate);
            tripUserResponse.setArrivalTime(arrivalTime);

            Vehicle vehicle = vehicleRepository
                    .findById((String) trip[6])
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));

            tripUserResponse.setVehicleName(vehicle.getVehicleName());

            Route route = new Route();
            route.setId((String) trip[7]);
            route.setArrivalLocation((String) trip[8]);
            route.setArrivalPoint((String) trip[9]);
            route.setDepartureLocation((String) trip[10]);
            route.setDeparturePoint((String) trip[11]);
            route.setDistance((Integer) trip[12]);
            route.setDuration((Integer) trip[13]);

            RouteResponse routeResponse = routeMapper.toRouteResponse(route);

            tripUserResponse.setRoute(routeResponse);

            tripUserResponse.setTicketPrice(
                    tripRepository.findTicketPrice(
                            (String) trip[6],
                            route.getId()));
            tripUserResponse.setAvailableSeats(
                    tripRepository.findAvailableSeatsByVehicleId(
                            (String) trip[6]));

            tripUserResponses.add(tripUserResponse);
        }

        return tripUserResponses;
    }

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

    public TripResponse patchUpdateTrip(TripUpdateRequest request, String tripId) {
        Trip trip = tripRepository
                .findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository
                    .findById(request.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
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

    public void deleteTrip(String tripId) {
        tripRepository.findById(tripId)
                .map(trip -> {
                    tripRepository.delete(trip);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Trip not found for ID: " + tripId));
    }
}
