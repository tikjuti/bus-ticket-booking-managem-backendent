package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.dto.response.VehicleResponse;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    boolean existsById(String id);

    boolean existsByLicensePlate(String licensePlate);

    @Query("SELECT v FROM Vehicle v LEFT JOIN FETCH v.seats s WHERE v.id = :vehicleId")
    Optional<Vehicle> findByIdWithSeats(@Param("vehicleId") String vehicleId);

}
