package com.tikjuti.bus_ticket_booking.repository;

import org.springframework.stereotype.Repository;


@Repository
public interface CustomSeatRepository {

    Boolean checkPosition(String p_vehicle_id, String p_seat_id,
                          String p_new_position
    );
}
