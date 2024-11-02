package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.DriverAssignmentForTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverAssignmentForTripRepository extends JpaRepository<DriverAssignmentForTrip, String>,
        JpaSpecificationExecutor<DriverAssignmentForTrip>, CustomDriverAssignmentForTripRepository {

    boolean existsById(String id);

    Boolean checkIsDriver(String employeeId);
    Boolean canDriverOperateVehicle(String tripId, String employeeId);
    Boolean checkDriverAssignmentForTripExists(String tripId, String employeeId,
                                               String driverAssignmentForTripId
    );
}
