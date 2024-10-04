package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.Trip.TripCreationRequest;
import com.tikjuti.bus_ticket_booking.entity.*;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.SeatMapper;
import com.tikjuti.bus_ticket_booking.mapper.VehicleMapper;
import com.tikjuti.bus_ticket_booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private SeatMapper seatMapper;

    public Trip createTrip (TripCreationRequest request)
    {

//        Kiểm tra ngày đi và ngày đến có hợp lệ không
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

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

//    public List<Vehicle> getVehicles() {return vehicleRepository.findAll();}
//
//    public VehicleResponse getVehicle(String vehicleId)
//    {
//        VehicleResponse vehicleResponse = vehicleMapper.toVehicleResponse(vehicleRepository
//                .findById(vehicleId)
//                .orElseThrow(() -> new RuntimeException("Vehicle not found")));
//
//        Set<SeatResponse> seats = seatMapper.toListSeatResponse(vehicleRepository
//                .findSeatsByVehicleId(vehicleId));
//
//        vehicleResponse.setSeats(seats);
//        return vehicleResponse;
//    }
//
//    public VehicleResponse updateVehicle(VehicleUpdateRequest request, String vehicleId)
//    {
//        Vehicle vehicle = vehicleRepository
//                .findById(vehicleId)
//                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
//
//        vehicleMapper.toUpdateVehicleResponse(vehicle);
//
//        VehicleType vehicleType = vehicleTypeRepository
//                .findById(request.getVehicleType())
//                .orElseThrow(() -> new RuntimeException("Vehicle type not found"));
//        vehicle.setVehicleType(vehicleType);
//
//        return vehicleMapper
//                .toVehicleResponse(vehicleRepository.save(vehicle));
//    }
//    public VehicleResponse patchUpdateRoute(VehicleUpdateRequest request, String vehicleId) {
//        Vehicle vehicle = vehicleRepository
//                .findById(vehicleId)
//                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
//
//        if (request.getVehicleType() != null) {
//            VehicleType vehicleType = vehicleTypeRepository
//                    .findById(request.getVehicleType())
//                    .orElseThrow(() -> new RuntimeException("Vehicle type not found"));
//            vehicle.setVehicleType(vehicleType);
//        }
//
//        if (request.getVehicleName() != null) {
//            vehicle.setVehicleName(request.getVehicleName());
//        }
//
//        if (request.getColor() != null) {
//            vehicle.setColor(request.getColor());
//        }
//
//        if (request.getStatus() != null) {
//            vehicle.setStatus(request.getStatus().name());
//        }
//
//        return vehicleMapper.toVehicleResponse(vehicleRepository.save(vehicle));
//    }
//
//
//    public void deleteVehicle(String vehicleId) {
//        vehicleRepository.findById(vehicleId)
//                .map(vehicle -> {
//                    vehicleRepository.delete(vehicle);
//                    return true;
//                })
//                .orElseThrow(() -> new RuntimeException("Vehicle not found for ID: " + vehicleId));
//    }
}
