package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Seat;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface CustomSeatRepository {

    Boolean checkPosition(String p_vehicle_id, String p_seat_id,
                          String p_new_position
    );

    List<Seat> findSeatsFromCompletedTrips(LocalDate currentDate, LocalTime currentTime);

}
