package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface SeatRepository extends JpaRepository<Seat, String>,
        JpaSpecificationExecutor<Seat>, CustomSeatRepository {

    Boolean checkPosition(String p_vehicle_id,  String p_seat_id, String p_new_position);

    Set<Seat> findByVehicleId(String vehicleId);

}
