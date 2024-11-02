package com.tikjuti.bus_ticket_booking.repository.Impl;

import com.tikjuti.bus_ticket_booking.repository.CustomPriceRepository;
import com.tikjuti.bus_ticket_booking.repository.CustomRouteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import static jakarta.persistence.ParameterMode.IN;
import static jakarta.persistence.ParameterMode.OUT;

@Repository
public class CustomRouteRepositoryImpl implements CustomRouteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean checkRoute(String departureLocation, String arrivalLocation,
                              String departurePoint, String arrivalPoint) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("checkRoute");
        query.registerStoredProcedureParameter("departureLocation", String.class, IN);
        query.registerStoredProcedureParameter("arrivalLocation", String.class, IN);
        query.registerStoredProcedureParameter("departurePoint", String.class, IN);
        query.registerStoredProcedureParameter("arrivalPoint", String.class, IN);
        query.setParameter("departureLocation", departureLocation);
        query.setParameter("arrivalLocation", arrivalLocation);
        query.setParameter("departurePoint", departurePoint);
        query.setParameter("arrivalPoint", arrivalPoint);
        query.registerStoredProcedureParameter("isValid", Boolean.class, OUT);
        query.execute();

        return (Boolean) query.getOutputParameterValue("isValid");
    }
}
