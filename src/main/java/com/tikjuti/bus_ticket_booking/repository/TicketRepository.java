package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    boolean existsById(String id);

    @Procedure(name = "checkIsEmployeeBooking")
    Boolean checkIsEmployeeBooking(
            @Param("employeeId") String employeeId
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
