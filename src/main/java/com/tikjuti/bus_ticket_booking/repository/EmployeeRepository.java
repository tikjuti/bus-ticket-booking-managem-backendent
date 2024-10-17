package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByNationalIDNumber(String nationalIDNumber);

    Optional<Employee> findByEmail(String email);

    boolean existsById(String id);
}
