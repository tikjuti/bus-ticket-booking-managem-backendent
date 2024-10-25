package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.DriverAssignmentForVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverAssignmentForVehicleRepository extends
        JpaRepository<DriverAssignmentForVehicle, String>,
        JpaSpecificationExecutor<DriverAssignmentForVehicle> {

    boolean existsById(String id);

    @Procedure(name = "CheckDriverAssignmentForVehicleExists")
    Boolean checkDriverAssignmentForVehicleExists(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("vehicleId") String arrivalDate,
            @Param("employeeId") String arrivalTime,
            @Param("driverAssignmentForVehicleId") String driverAssignmentForVehicleId
    );

    @Procedure(name = "checkIsDriver")
    Boolean checkIsDriver(
            @Param("employeeId") String employeeId
    );
}
