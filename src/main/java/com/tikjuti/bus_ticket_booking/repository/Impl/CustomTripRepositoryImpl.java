package com.tikjuti.bus_ticket_booking.repository.Impl;

import com.tikjuti.bus_ticket_booking.repository.CustomTripRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

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
}
