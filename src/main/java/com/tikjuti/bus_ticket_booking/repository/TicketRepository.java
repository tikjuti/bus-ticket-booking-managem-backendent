package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    boolean existsById(String id);

    @Procedure(name = "checkIsEmployeeBooking")
    Boolean checkIsEmployeeBooking(
            @Param("employeeId") String employeeId
    );
}
