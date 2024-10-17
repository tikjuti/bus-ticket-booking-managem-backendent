package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PriceRepository extends JpaRepository<Price, String> {
    boolean existsById(String id);

    @Procedure(name = "checkPrice")
    Boolean checkPrice(
            @Param("vehicleType") String vehicleType,
            @Param("route") String route,
            @Param("priceId") String priceId
    );

}
