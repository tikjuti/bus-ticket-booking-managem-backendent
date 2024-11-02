package com.tikjuti.bus_ticket_booking.repository.Impl;

import com.tikjuti.bus_ticket_booking.repository.CustomDriverAssignmentForTripRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;
import static jakarta.persistence.ParameterMode.IN;
import static jakarta.persistence.ParameterMode.OUT;

@Repository
public class CustomDriverAssignmentForTripRepositoryImpl implements CustomDriverAssignmentForTripRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean checkIsDriver(String employeeId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("checkIsDriver");
        query.registerStoredProcedureParameter("employeeId", String.class, IN);
        query.setParameter("employeeId", employeeId);
        query.registerStoredProcedureParameter("isValid", Boolean.class, OUT);
        query.execute();

        return (Boolean) query.getOutputParameterValue("isValid");
    }

    @Override
    public Boolean canDriverOperateVehicle(String tripId, String employeeId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("canDriverOperateVehicle");
        query.registerStoredProcedureParameter("tripId", String.class, IN);
        query.registerStoredProcedureParameter("employeeId", String.class, IN);
        query.setParameter("tripId", tripId);
        query.setParameter("employeeId", employeeId);
        query.registerStoredProcedureParameter("isValid", Boolean.class, OUT);
        query.execute();

        return (Boolean) query.getOutputParameterValue("isValid");
    }

    @Override
    public Boolean checkDriverAssignmentForTripExists(String tripId, String employeeId, String driverAssignmentForTripId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("CheckDriverAssignmentForTripExists");
        query.registerStoredProcedureParameter("tripId", String.class, IN);
        query.registerStoredProcedureParameter("employeeId", String.class, IN);
        query.registerStoredProcedureParameter("driverAssignmentForTripId", String.class, IN);
        query.setParameter("tripId", tripId);
        query.setParameter("employeeId", employeeId);
        query.setParameter("driverAssignmentForTripId", driverAssignmentForTripId);
        query.registerStoredProcedureParameter("isValid", Boolean.class, OUT);
        query.execute();

        return (Boolean) query.getOutputParameterValue("isValid");
    }

}
