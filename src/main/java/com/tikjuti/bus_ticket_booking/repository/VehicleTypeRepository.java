package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, String>,
        JpaSpecificationExecutor<VehicleType> {
    boolean existsByVehicleTypeName(String nameEmployeeType);

    boolean existsById(String id);

    Optional<VehicleType> findByVehicleTypeNameAndId(String nameEmployeeType, String id);
}
