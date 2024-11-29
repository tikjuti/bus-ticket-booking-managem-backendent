package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, String>,
        JpaSpecificationExecutor<Ticket>, CustomTicketRepository {
    boolean existsById(String id);

    Boolean checkIsEmployeeBooking(String employeeId);

    List<Object[]> findTripsByUser(String departureLocation, String arrivalLocation,String departureDate);

    int findAvailableSeatsByVehicleId(String vehicleId);

    int findTicketPrice(String vehicleId, String routeId);

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
