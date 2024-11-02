package com.tikjuti.bus_ticket_booking.repository.Impl;

import com.tikjuti.bus_ticket_booking.repository.CustomPriceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import static jakarta.persistence.ParameterMode.IN;
import static jakarta.persistence.ParameterMode.OUT;

@Repository
public class CustomPriceRepositoryImpl implements CustomPriceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean checkPrice(String vehicleType, String route, String priceId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("checkPrice");
        query.registerStoredProcedureParameter("vehicleType", String.class, IN);
        query.registerStoredProcedureParameter("route", String.class, IN);
        query.registerStoredProcedureParameter("priceId", String.class, IN);
        query.setParameter("vehicleType", vehicleType);
        query.setParameter("route", route);
        query.setParameter("priceId", priceId);
        query.registerStoredProcedureParameter("isValid", Boolean.class, OUT);
        query.execute();

        return (Boolean) query.getOutputParameterValue("isValid");
    }
}
