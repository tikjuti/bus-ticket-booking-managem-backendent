package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface CustomEmployeeRepository {
    List<Employee> findByEmployeeTypeEmployeeTypeName(String employeeTypeName);
}
