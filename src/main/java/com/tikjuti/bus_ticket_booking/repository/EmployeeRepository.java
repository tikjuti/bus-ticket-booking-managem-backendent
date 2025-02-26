package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>, JpaSpecificationExecutor<Employee>, CustomEmployeeRepository {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByNationalIDNumber(String nationalIDNumber);

    Optional<Employee> findByEmail(String email);
    List<Employee> findByEmployeeTypeEmployeeTypeName(String employeeTypeName);

    boolean existsById(String id);
}
