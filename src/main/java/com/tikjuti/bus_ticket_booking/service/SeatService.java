package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.Seat.SeatUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.SeatResponse;
import com.tikjuti.bus_ticket_booking.entity.Seat;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.SeatMapper;
import com.tikjuti.bus_ticket_booking.repository.SeatRepository;
import com.tikjuti.bus_ticket_booking.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private SeatMapper seatMapper;

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public Set<SeatResponse> getSeatBuyVehicleId(String vehicleId) {
        Set<Seat> seats = seatRepository.findByVehicleId(vehicleId);

        return seatMapper.toListSeatResponse(seats);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public SeatResponse updateSeat(SeatUpdateRequest request, String seatId)
    {
        Seat seat = seatRepository
                .findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        Boolean existsPosition = seatRepository.checkPosition(
                request.getVehicle(),
                seatId,
                request.getPosition()
        );
        if(existsPosition)
            throw new AppException(ErrorCode.POSITION_EXISTED);

        seat.setPosition(request.getPosition());
        seat.setStatus(request.getStatus().name());

        Vehicle vehicle = vehicleRepository
                .findById(request.getVehicle())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        seat.setVehicle(vehicle);

        return seatMapper
                .toSeatResponse(seatRepository.save(seat));
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public SeatResponse patchUpdateSeat(SeatUpdateRequest request, String seatId) {
        Seat seat = seatRepository
                .findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        Boolean existsPosition = seatRepository.checkPosition(
                request.getVehicle() != null ? request.getVehicle() : seat.getVehicle().getId(),
                seatId,
                request.getPosition() != null ? request.getPosition() : seat.getPosition()
        );

        if (existsPosition) {
            throw new AppException(ErrorCode.POSITION_EXISTED);
        }

        if (request.getVehicle() != null) {
            Vehicle vehicle = vehicleRepository
                    .findById(request.getVehicle())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
            seat.setVehicle(vehicle);
        }

        if (request.getPosition() != null) {
            seat.setPosition(request.getPosition());
        }

        if (request.getStatus() != null) {
            seat.setStatus(request.getStatus().name());
        }


        Seat updatedSeat = seatRepository.save(seat);

        return seatMapper.toSeatResponse(updatedSeat);
    }

}
