package com.tikjuti.bus_ticket_booking.repository.Impl;

import com.tikjuti.bus_ticket_booking.entity.Seat;
import com.tikjuti.bus_ticket_booking.entity.Trip;
import com.tikjuti.bus_ticket_booking.entity.Vehicle;
import com.tikjuti.bus_ticket_booking.repository.CustomVehicleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class CustomVehicleRepositoryImpl implements CustomVehicleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Set<Seat> findSeatsByVehicleId(String vehicleId) {
        String jpql = "SELECT s FROM Seat s WHERE s.vehicle.id = :vehicleId";
        return new HashSet<>(entityManager.createQuery(jpql, Seat.class)
                .setParameter("vehicleId", vehicleId)
                .getResultList());
    }

    @Override
    public List<Vehicle> findByVehicleType(String vehicleTypeId) {
        String jpql = "SELECT v FROM Vehicle v WHERE v.vehicleType.id = :vehicleTypeId";
        return entityManager.createQuery(jpql, Vehicle.class)
                .setParameter("vehicleTypeId", vehicleTypeId)
                .getResultList();
    }

    @Override
    public List<Vehicle> getUnassignedVehicles() {
        String jpql = "SELECT v FROM Vehicle v WHERE v.id NOT IN (SELECT a.vehicle.id FROM DriverAssignmentForVehicle a)";
        return entityManager.createQuery(jpql, Vehicle.class)
                .getResultList();
    }
}
