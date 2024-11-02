package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Seat;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface CustomVehicleRepository {

    Set<Seat> findSeatsByVehicleId(String vehicleId);
}
