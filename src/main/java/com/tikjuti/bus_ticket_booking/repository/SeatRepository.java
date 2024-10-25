package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SeatRepository extends JpaRepository<Seat, String>,
        JpaSpecificationExecutor<Seat> {
    @Procedure(name = "checkPosition")
    Boolean checkPosition(
            @Param("p_vehicle_id") String p_vehicle_id,
            @Param("p_seat_id") String p_seat_id,
            @Param("p_new_position") String p_new_position
    );
}
