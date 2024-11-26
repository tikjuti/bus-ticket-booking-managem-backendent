package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface CustomTicketRepository {

    Boolean checkIsEmployeeBooking(String employeeId);
    List<Object[]> findTripsByUser(String departureLocation, String arrivalLocation, String departureDate);
    int findAvailableSeatsByVehicleId(String vehicleId);
    int findTicketPrice(String vehicleId, String routeId);
    Optional<Ticket> findTicketByTicketIdAndPhone(String ticketId, String phone);
    List<Ticket> findByTripIdsAndEmailNotSent(List<String> tripIds);
}
