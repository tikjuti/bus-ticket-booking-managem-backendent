package com.tikjuti.bus_ticket_booking.repository.Impl;

import com.tikjuti.bus_ticket_booking.entity.Seat;
import com.tikjuti.bus_ticket_booking.repository.CustomSeatRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static jakarta.persistence.ParameterMode.IN;
import static jakarta.persistence.ParameterMode.OUT;

@Repository
public class CustomSeatRepositoryImpl implements CustomSeatRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean checkPosition(String p_vehicle_id, String p_seat_id,
                                 String p_new_position) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("checkPosition");
        query.registerStoredProcedureParameter("p_vehicle_id", String.class, IN);
        query.registerStoredProcedureParameter("p_seat_id", String.class, IN);
        query.registerStoredProcedureParameter("p_new_position", String.class, IN);
        query.setParameter("p_vehicle_id", p_vehicle_id);
        query.setParameter("p_seat_id", p_seat_id);
        query.setParameter("p_new_position", p_new_position);
        query.registerStoredProcedureParameter("p_is_exists", Boolean.class, OUT);
        query.execute();

        return (Boolean) query.getOutputParameterValue("p_is_exists");
    }

    @Override
    public List<Seat> findSeatsFromCompletedTrips(LocalDate currentDate, LocalTime currentTime) {
            String jpql = "SELECT s FROM Seat s " +
                    "WHERE s.vehicle IN (" +
                    "  SELECT t.vehicle FROM Trip t " +
                    "  WHERE NOT EXISTS (" +
                    "    SELECT 1 FROM Trip t2 " +
                    "    WHERE t2.vehicle = t.vehicle " +
                    "      AND (t2.arrivalDate > :currentDate " +
                    "           OR (t2.arrivalDate = :currentDate AND t2.arrivalTime >= :currentTime))" +
                    "  )" +
                    ")";

            TypedQuery<Seat> query = entityManager.createQuery(jpql, Seat.class);
            query.setParameter("currentDate", currentDate);
            query.setParameter("currentTime", currentTime);

            return query.getResultList();
    }


}
