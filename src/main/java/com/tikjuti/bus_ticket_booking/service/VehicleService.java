package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.Utils.QueryableExtensions;
import com.tikjuti.bus_ticket_booking.dto.request.Vehicle.VehicleCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Vehicle.VehicleQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Vehicle.VehicleUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.SeatResponse;
import com.tikjuti.bus_ticket_booking.dto.response.VehicleResponse;
import com.tikjuti.bus_ticket_booking.entity.Seat;
import com.tikjuti.bus_ticket_booking.entity.Trip;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import com.tikjuti.bus_ticket_booking.entity.VehicleType;
import com.tikjuti.bus_ticket_booking.enums.SeatStatus;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.SeatMapper;
import com.tikjuti.bus_ticket_booking.mapper.VehicleMapper;
import com.tikjuti.bus_ticket_booking.repository.SeatRepository;
import com.tikjuti.bus_ticket_booking.repository.VehicleRepository;
import com.tikjuti.bus_ticket_booking.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    private SeatMapper seatMapper;

    @PreAuthorize("hasRole('ADMIN')")
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

        return savedVehicle;
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public PaginatedResult<Vehicle> getVehicles(VehicleQueryRequest queryRequest) {
        Map<String, Object> filterParams = new HashMap<>();

        if (queryRequest.getId() != null)
            filterParams.put("id", queryRequest.getId());

        if (queryRequest.getSeatCount() != null)
            filterParams.put("seatCount", queryRequest.getSeatCount());

        if (queryRequest.getLicensePlate() != null)
            filterParams.put("licensePlate", queryRequest.getLicensePlate());

        if (queryRequest.getVehicleName() != null)
            filterParams.put("vehicleName", queryRequest.getVehicleName());

        if (queryRequest.getColor() != null)
            filterParams.put("color", queryRequest.getColor());

        if (queryRequest.getStatus() != null)
            filterParams.put("status", queryRequest.getStatus());

        Specification<Vehicle> spec = Specification.where(
                        QueryableExtensions.<Vehicle>applyIncludes(queryRequest.getIncludes()))
                .and(QueryableExtensions.applyFilters(filterParams))
                .and(QueryableExtensions.applySorting(queryRequest.getSort()));

        return QueryableExtensions.applyPagination(
                vehicleRepository, spec, queryRequest.getPage(), queryRequest.getPageSize());
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public List<Vehicle> getUnassignedVehicles() {
        return vehicleRepository.getUnassignedVehicles();
    }

    public VehicleResponse getVehicle(String vehicleId)
    {
        VehicleResponse vehicleResponse = vehicleMapper.toVehicleResponse(vehicleRepository
                .findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found")));

        Set<SeatResponse> seats = seatMapper.toListSeatResponse(vehicleRepository
                .findSeatsByVehicleId(vehicleId));

        vehicleResponse.setSeats(seats);
        return vehicleResponse;
    }

    public List<Vehicle> getVehiclesByType(String vehicleTypeId) {
        return vehicleRepository.findByVehicleType(vehicleTypeId);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public VehicleResponse updateVehicle(VehicleUpdateRequest request, String vehicleId)
    {
        Vehicle vehicle = vehicleRepository
                .findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        VehicleType vehicleType = vehicleTypeRepository
                .findById(request.getVehicleType())
                .orElseThrow(() -> new RuntimeException("Vehicle type not found"));
        vehicle.setVehicleType(vehicleType);
        vehicle.setStatus(request.getStatus().name());
        vehicle.setVehicleName(request.getVehicleName());
        vehicle.setColor(request.getColor());

        return vehicleMapper
                .toVehicleResponse(vehicleRepository.save(vehicle));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public VehicleResponse patchUpdateRoute(VehicleUpdateRequest request, String vehicleId) {
        Vehicle vehicle = vehicleRepository
                .findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (request.getVehicleType() != null) {
            VehicleType vehicleType = vehicleTypeRepository
                    .findById(request.getVehicleType())
                    .orElseThrow(() -> new RuntimeException("Vehicle type not found"));
            vehicle.setVehicleType(vehicleType);
        }

        if (request.getVehicleName() != null) {
            vehicle.setVehicleName(request.getVehicleName());
        }

        if (request.getColor() != null) {
            vehicle.setColor(request.getColor());
        }

        if (request.getStatus() != null) {
            vehicle.setStatus(request.getStatus().name());
        }

        return vehicleMapper.toVehicleResponse(vehicleRepository.save(vehicle));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteVehicle(String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found for ID: " + vehicleId));

        vehicle.getSeats().clear();
        vehicleRepository.delete(vehicle);
    }

}
