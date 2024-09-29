package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.Vehicle.VehicleCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.response.VehicleResponse;
import com.tikjuti.bus_ticket_booking.entity.Seat;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import com.tikjuti.bus_ticket_booking.entity.VehicleType;
import com.tikjuti.bus_ticket_booking.enums.SeatStatus;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.VehicleMapper;
import com.tikjuti.bus_ticket_booking.repository.SeatRepository;
import com.tikjuti.bus_ticket_booking.repository.VehicleRepository;
import com.tikjuti.bus_ticket_booking.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private VehicleMapper vehicleMapper;

    public Vehicle  createVehicle (VehicleCreationRequest request)
    {
        if (vehicleRepository.existsByLicensePlate(request.getLicensePlate()))
            throw new AppException(ErrorCode.VEHICLE_EXISTED);

        Vehicle vehicle = new Vehicle();

        vehicle.setSeatCount(request.getSeatCount());
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setVehicleName(request.getVehicleName());
        vehicle.setColor(request.getColor());
        vehicle.setStatus(request.getStatus().name());

        VehicleType vehicleType = vehicleTypeRepository
                .findById(request.getVehicleType())
                .orElseThrow(() -> new RuntimeException("VehicleType not found"));
        vehicle.setVehicleType(vehicleType);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        Set<Seat> seats = new HashSet<>();
        for (int i = 1; i <= request.getSeatCount(); i++) {
            Seat seat = new Seat();
            seat.setPosition(i + "");
            seat.setStatus(SeatStatus.AVAILABLE + "");
            seat.setVehicle(savedVehicle);
            seats.add(seat);
        }

        seatRepository.saveAll(seats);
        savedVehicle.setSeats(seats);

        return savedVehicle;
    }

    public List<Vehicle> getVehicles() {return vehicleRepository.findAll();}

    public VehicleResponse getVehicle(String vehicleId)
    {
        Vehicle vehicle = vehicleRepository.findByIdWithSeats(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        System.out.println("Vehicle ID: " + vehicle.getId());
        System.out.println("Vehicle Name: " + vehicle.getVehicleName());
        System.out.println("Seats Count: " + vehicle.getSeats().size());
        vehicle.getSeats().forEach(seat -> {
            System.out.println("Seat ID: " + seat.getId());
            System.out.println("Seat Position: " + seat.getPosition());
            System.out.println("Seat Status: " + seat.getStatus());
        });
        return vehicleMapper.toVehicleResponse(vehicle);
    }

//    public RouteResponse updateRoute(RouteUpdateRequest request, String routeId)
//    {
//        Route route = routeRepository
//                .findById(routeId)
//                .orElseThrow(() -> new RuntimeException("Route not found"));
//
//        Boolean exitsRoute = routeRepository.checkRoute(
//                request.getDepartureLocation(),
//                request.getArrivalLocation(),
//                request.getDeparturePoint(),
//                request.getArrivalPoint(),
//                request.getDistance(),
//                request.getDuration()
//        );
//        if(!exitsRoute)
//            throw new AppException(ErrorCode.ROUTE_EXISTED);
//
//        routeMapper.updateRoute(route, request);
//
//        return routeMapper
//                .toRouteResponse(routeRepository.save(route));
//    }
//
//    public RouteResponse patchUpdateRoute(RouteUpdateRequest request, String routeId) {
//        Route route = routeRepository
//                .findById(routeId)
//                .orElseThrow(() -> new RuntimeException("Route not found"));
//
//        Boolean existsRoute = routeRepository.checkRoute(
//                request.getDepartureLocation() != null ? request.getDepartureLocation() : route.getDepartureLocation(),
//                request.getArrivalLocation() != null ? request.getArrivalLocation() : route.getArrivalLocation(),
//                request.getDeparturePoint() != null ? request.getDeparturePoint() : route.getDeparturePoint(),
//                request.getArrivalPoint() != null ? request.getArrivalPoint() : route.getArrivalPoint(),
//                request.getDistance() != 0 ? request.getDistance() : route.getDistance(),
//                request.getDuration() != 0 ? request.getDuration() : route.getDuration()
//        );
//
//        if (!existsRoute) {
//            throw new AppException(ErrorCode.ROUTE_EXISTED);
//        }
//
//        if (request.getDepartureLocation() != null) {
//            route.setDepartureLocation(request.getDepartureLocation());
//        }
//
//        if (request.getArrivalLocation() != null) {
//            route.setArrivalLocation(request.getArrivalLocation());
//        }
//
//        if (request.getDeparturePoint() != null) {
//            route.setDeparturePoint(request.getDeparturePoint());
//        }
//
//        if (request.getArrivalPoint() != null) {
//            route.setArrivalPoint(request.getArrivalPoint());
//        }
//
//        if (request.getDistance() != 0 && request.getDistance() > 0) {
//            route.setDistance(request.getDistance());
//        }
//
//        if (request.getDuration() != 0 && request.getDuration() > 0) {
//            route.setDuration(request.getDuration());
//        }
//
//        Route updatedRoute = routeRepository.save(route);
//
//        return routeMapper.toRouteResponse(updatedRoute);
//    }
//
//
//    public void deleteRoute(String routeId) {
//        routeRepository.findById(routeId)
//                .map(route -> {
//                    routeRepository.delete(route);
//                    return true;
//                })
//                .orElseThrow(() -> new RuntimeException("Route not found for ID: " + routeId));
//    }
}
