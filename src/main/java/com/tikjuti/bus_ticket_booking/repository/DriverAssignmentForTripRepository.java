package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.DriverAssignmentForTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverAssignmentForTripRepository extends JpaRepository<DriverAssignmentForTrip, String> {

    boolean existsById(String id);

    @Procedure(name = "checkIsDriver")
    Boolean checkIsDriver(
            @Param("employeeId") String employeeId
    );

    @Procedure(name = "canDriverOperateVehicle")
    Boolean canDriverOperateVehicle(
            @Param("tripId") String tripId,
            @Param("employeeId") String employeeId
    );

    @Procedure(name = "CheckDriverAssignmentForTripExists")
    Boolean checkDriverAssignmentForTripExists(
            @Param("tripId") String tripId,
            @Param("employeeId") String employeeId,
            @Param("driverAssignmentForTripId") String driverAssignmentForTripId
    );

}
