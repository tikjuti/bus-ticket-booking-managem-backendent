package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;


@Repository
public interface TripRepository extends JpaRepository<Trip, String> {
    boolean existsById(String id);

    @Procedure(name = "CheckVehicleAssignmentExists")
    Boolean checkVehicleAssignmentExists(
            @Param("departureDate") String departureDate,
            @Param("departureTime") String departureTime,
            @Param("arrivalDate") String arrivalDate,
            @Param("arrivalTime") String arrivalTime,
            @Param("vehicleId") String vehicleId,
            @Param("tripId") String tripId
    );

    @Procedure(name = "CheckTripExists")
    Boolean checkTripExists(
            @Param("departureDate") String departureDate,
            @Param("departureTime") String departureTime,
            @Param("arrivalDate") String arrivalDate,
            @Param("arrivalTime") String arrivalTime,
            @Param("vehicleId") String vehicleId,
            @Param("routeId") String routeId,
            @Param("tripId") String tripId
    );

    @Procedure(procedureName = "FindTripsUser")
    List<Object[]> findTripsByUser(
            @Param("departureLocation") String departureLocation,
            @Param("arrivalLocation") String arrivalLocation,
            @Param("departureDate") String departureDate
    );

    @Query(value = "SELECT GetAvailableSeats(:vehicleId)", nativeQuery = true)
    int findAvailableSeatsByVehicleId(@Param("vehicleId") String vehicleId);

    @Query(value = "SELECT GetTicketPrice(:vehicleId, :routeId)", nativeQuery = true)
    int findTicketPrice(@Param("vehicleId") String vehicleId, @Param("routeId") String routeId);

}
