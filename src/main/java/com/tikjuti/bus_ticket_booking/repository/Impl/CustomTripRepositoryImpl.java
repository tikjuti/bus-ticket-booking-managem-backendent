package com.tikjuti.bus_ticket_booking.repository.Impl;

import com.tikjuti.bus_ticket_booking.entity.Trip;
import com.tikjuti.bus_ticket_booking.repository.CustomTripRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static jakarta.persistence.ParameterMode.IN;
import static jakarta.persistence.ParameterMode.OUT;

@Repository
public class CustomTripRepositoryImpl implements CustomTripRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean checkVehicleAssignmentExists(String departureDate, String departureTime, String arrivalDate,
                                                String arrivalTime, String vehicleId, String tripId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("CheckVehicleAssignmentExists");
        query.registerStoredProcedureParameter("departureDate", String.class, IN);
        query.registerStoredProcedureParameter("departureTime", String.class, IN);
        query.registerStoredProcedureParameter("arrivalDate", String.class, IN);
        query.registerStoredProcedureParameter("arrivalTime", String.class, IN);
        query.registerStoredProcedureParameter("vehicleId", String.class, IN);
        query.registerStoredProcedureParameter("tripId", String.class, IN);
        query.setParameter("departureDate", departureDate);
        query.setParameter("departureTime", departureTime);
        query.setParameter("arrivalDate", arrivalDate);
        query.setParameter("arrivalTime", arrivalTime);
        query.setParameter("vehicleId", vehicleId);
        query.setParameter("tripId", tripId);
        query.registerStoredProcedureParameter("isValid", Boolean.class, OUT);
        query.execute();

        return (Boolean) query.getOutputParameterValue("isValid");
    }

    @Override
    public Boolean checkVehicleIsACTIVE(String vehicleId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("CheckVehicleIsACTIVE");
        query.registerStoredProcedureParameter("vehicleId", String.class, IN);
        query.setParameter("vehicleId", vehicleId);
        query.registerStoredProcedureParameter("isValid", Boolean.class, OUT);
        query.execute();

        return (Boolean) query.getOutputParameterValue("isValid");
    }

    @Override
    public List<Trip> findCompletedTrips(LocalDate currentDate, LocalTime currentTime) {
        String jpql = "SELECT t FROM Trip t WHERE t.arrivalDate < :currentDate OR (t.arrivalDate = :currentDate AND t.arrivalTime < :currentTime)" +
                "AND t.vehicle NOT IN (SELECT trip.vehicle FROM Trip trip WHERE trip.arrivalDate >= :currentDate AND trip.arrivalTime >= :currentTime)";

        TypedQuery<Trip> query = entityManager.createQuery(jpql, Trip.class);
        query.setParameter("currentDate", currentDate);
        query.setParameter("currentTime", currentTime);

        return query.getResultList();
    }

    @Override
    public List<Trip> findStartingTrips(LocalDate currentDate, LocalTime currentTime) {
        LocalDateTime currentDateTime = LocalDateTime.of(currentDate, currentTime);
        LocalDateTime sixHoursAfter = currentDateTime.plusHours(6);
        LocalDateTime twelveHoursAfter = currentDateTime.plusHours(12);

        LocalDate sixHoursAfterDate = sixHoursAfter.toLocalDate();
        LocalTime sixHoursAfterTime = sixHoursAfter.toLocalTime();
        LocalDate twelveHoursAfterDate = twelveHoursAfter.toLocalDate();
        LocalTime twelveHoursAfterTime = twelveHoursAfter.toLocalTime();

        String jpql = "SELECT t FROM Trip t WHERE " +
                "(t.departureDate > :sixHoursAfterDate OR " +
                "(t.departureDate = :sixHoursAfterDate AND t.departureTime > :sixHoursAfterTime)) " +
                "AND (t.departureDate < :twelveHoursAfterDate OR " +
                "(t.departureDate = :twelveHoursAfterDate AND t.departureTime <= :twelveHoursAfterTime))";

        TypedQuery<Trip> query = entityManager.createQuery(jpql, Trip.class);
        query.setParameter("sixHoursAfterDate", sixHoursAfterDate);
        query.setParameter("sixHoursAfterTime", sixHoursAfterTime);
        query.setParameter("twelveHoursAfterDate", twelveHoursAfterDate);
        query.setParameter("twelveHoursAfterTime", twelveHoursAfterTime);

        return query.getResultList();
    }
}
