package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.EmployeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, String>,
        JpaSpecificationExecutor<EmployeeType> {
    boolean existsByNameEmployeeType(String nameEmployeeType);

    boolean existsById(String id);
}
