package com.tikjuti.bus_ticket_booking.repository;

import org.springframework.stereotype.Repository;


@Repository
public interface CustomTripRepository {

    Boolean checkVehicleAssignmentExists(String departureDate, String departureTime, String arrivalDate,
            String arrivalTime, String vehicleId, String tripId
    );
    
}
