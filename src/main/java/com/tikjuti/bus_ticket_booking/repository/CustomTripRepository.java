package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Trip;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface CustomTripRepository {

    Boolean checkVehicleAssignmentExists(String departureDate, String departureTime, String arrivalDate,
            String arrivalTime, String vehicleId, String tripId
    );

    Boolean checkVehicleIsACTIVE(String vehicleId);

    List<Trip> findCompletedTrips(LocalDate currentDate, LocalTime currentTime);

    List<Trip> findStartingTrips(LocalDate currentDate, LocalTime currentTime);
}
