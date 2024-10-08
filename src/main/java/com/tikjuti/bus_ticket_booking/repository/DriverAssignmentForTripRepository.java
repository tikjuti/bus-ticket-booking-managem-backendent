package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.DriverAssignmentForTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverAssignmentForTripRepository extends JpaRepository<DriverAssignmentForTrip, String> {

    boolean existsById(String id);
}
