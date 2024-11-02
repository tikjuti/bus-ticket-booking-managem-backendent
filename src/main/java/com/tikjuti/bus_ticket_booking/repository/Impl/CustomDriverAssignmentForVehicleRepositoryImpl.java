package com.tikjuti.bus_ticket_booking.repository.Impl;

import com.tikjuti.bus_ticket_booking.repository.CustomDriverAssignmentForVehicleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import static jakarta.persistence.ParameterMode.IN;
import static jakarta.persistence.ParameterMode.OUT;

@Repository
public class CustomDriverAssignmentForVehicleRepositoryImpl implements CustomDriverAssignmentForVehicleRepository {

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
}
