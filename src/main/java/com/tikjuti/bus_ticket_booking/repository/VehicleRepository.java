package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Seat;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String>, JpaSpecificationExecutor<Vehicle> {
    boolean existsById(String id);

    boolean existsByLicensePlate(String licensePlate);

    @Query("SELECT s FROM Seat s WHERE s.vehicle.id = :vehicleId")
    Set<Seat> findSeatsByVehicleId(@Param("vehicleId") String vehicleId);
}
