package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface TripRepository extends JpaRepository<Trip, String>, JpaSpecificationExecutor<Trip>,
                CustomTripRepository{

    boolean existsById(String id);

    Boolean checkVehicleAssignmentExists(String departureDate, String departureTime, String arrivalDate,
            String arrivalTime, String vehicleId, String tripId
    );

}
