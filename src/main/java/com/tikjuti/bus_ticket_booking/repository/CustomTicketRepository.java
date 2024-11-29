package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Ticket;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomTicketRepository {

    Boolean checkIsEmployeeBooking(String employeeId);
    List<Object[]> findTripsByUser(String departureLocation, String arrivalLocation, String departureDate);
    int findAvailableSeatsByVehicleId(String vehicleId);
    int findTicketPrice(String vehicleId, String routeId);
    Optional<Ticket> findTicketByTicketIdAndPhone(String ticketId, String phone);
    List<Ticket> findByTripIdsAndEmailNotSent(List<String> tripIds);
    List<Object[]> countTicketsByDay(LocalDate startDate, LocalDate endDate);
    List<Object[]> countTicketsByMonth(LocalDate startDate, LocalDate endDate);
    List<Object[]> countTicketsByYear(LocalDate startDate, LocalDate endDate);

    List<Object[]> countTicketsByRoute(LocalDate startDate, LocalDate endDate);

    List<Object[]> countRevenueByDay(LocalDate startDate, LocalDate endDate);
    List<Object[]> countRevenueByMonth(LocalDate startDate, LocalDate endDate);
    List<Object[]> countRevenueByYear(LocalDate startDate, LocalDate endDate);
    List<Object[]> countRevenueByRoute(LocalDate startDate, LocalDate endDate);
    List<Object[]> countRevenueByVehicleType(LocalDate startDate, LocalDate endDate);
}
