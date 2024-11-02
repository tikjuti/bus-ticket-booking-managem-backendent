package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.DriverAssignmentForVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverAssignmentForVehicleRepository extends
        JpaRepository<DriverAssignmentForVehicle, String>,
        JpaSpecificationExecutor<DriverAssignmentForVehicle>,
        CustomDriverAssignmentForVehicleRepository{

    boolean existsById(String id);
    Boolean checkIsDriver(String employeeId);
}
