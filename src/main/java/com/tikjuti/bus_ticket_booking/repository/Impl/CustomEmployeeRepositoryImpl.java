package com.tikjuti.bus_ticket_booking.repository.Impl;

import com.tikjuti.bus_ticket_booking.entity.Employee;
import com.tikjuti.bus_ticket_booking.entity.Seat;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import com.tikjuti.bus_ticket_booking.repository.CustomEmployeeRepository;
import com.tikjuti.bus_ticket_booking.repository.CustomVehicleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Employee> findByEmployeeTypeEmployeeTypeName(String employeeTypeName) {
        String jpql = "SELECT e FROM Employee e WHERE e.employeeType.nameEmployeeType LIKE :employeeTypeName";
        return entityManager.createQuery(jpql, Employee.class)
                .setParameter("employeeTypeName", "%" + employeeTypeName + "%")
                .getResultList();
    }
}
