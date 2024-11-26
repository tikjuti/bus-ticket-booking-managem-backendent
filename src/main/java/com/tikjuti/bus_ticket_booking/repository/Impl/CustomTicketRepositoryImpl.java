package com.tikjuti.bus_ticket_booking.repository.Impl;

import com.tikjuti.bus_ticket_booking.entity.Ticket;
import com.tikjuti.bus_ticket_booking.repository.CustomTicketRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import static jakarta.persistence.ParameterMode.IN;
import static jakarta.persistence.ParameterMode.OUT;


import java.util.List;
import java.util.Optional;

public class CustomTicketRepositoryImpl implements CustomTicketRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean checkIsEmployeeBooking(String employeeId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("checkIsEmployeeBooking");
        query.registerStoredProcedureParameter("employeeId", String.class, IN);
        query.setParameter("employeeId", employeeId);
        query.registerStoredProcedureParameter("isValid", Boolean.class, OUT);
        query.execute();

        return (Boolean) query.getOutputParameterValue("isValid");
    }

    @Override
    public List<Object[]> findTripsByUser(String departureLocation, String arrivalLocation, String departureDate) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("FindTripsUser");
        query.registerStoredProcedureParameter("departureLocation", String.class, IN);
        query.registerStoredProcedureParameter("arrivalLocation", String.class, IN);
        query.registerStoredProcedureParameter("departureDate", String.class, IN);
        query.setParameter("departureLocation", departureLocation);
        query.setParameter("arrivalLocation", arrivalLocation);
        query.setParameter("departureDate", departureDate);
        query.execute();
        return query.getResultList();
    }

    @Override
    public int findAvailableSeatsByVehicleId(String vehicleId) {
        String sql = "SELECT GetAvailableSeats(:vehicleId)";
        return ((Number) entityManager.createNativeQuery(sql)
                .setParameter("vehicleId", vehicleId)
                .getSingleResult()).intValue();
    }

    @Override
    public int findTicketPrice(String vehicleId, String routeId) {
        String sql = "SELECT GetTicketPrice(:vehicleId, :routeId)";
        return ((Number) entityManager.createNativeQuery(sql)
                .setParameter("vehicleId", vehicleId)
                .setParameter("routeId", routeId)
                .getSingleResult()).intValue();
    }

    @Override
    public Optional<Ticket> findTicketByTicketIdAndPhone(String ticketId, String phone) {
        String jpql = "SELECT t FROM Ticket t WHERE t.id = :ticketId AND t.customer.phone = :phone";
        return entityManager.createQuery(jpql, Ticket.class)
                .setParameter("ticketId", ticketId)
                .setParameter("phone", phone)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Ticket> findByTripIdsAndEmailNotSent(List<String> tripIds) {
        String jpql = "SELECT t FROM Ticket t WHERE t.trip.id IN :tripIds AND t.isEmailSent = false";
        return entityManager.createQuery(jpql, Ticket.class)
                .setParameter("tripIds", tripIds)
                .getResultList();
    }
}
