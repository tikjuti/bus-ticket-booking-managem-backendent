package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Seat;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface CustomVehicleRepository {

    Set<Seat> findSeatsByVehicleId(String vehicleId);
    List<Vehicle> findByVehicleType(String vehicleTypeId);
    List<Vehicle> getUnassignedVehicles();
}
