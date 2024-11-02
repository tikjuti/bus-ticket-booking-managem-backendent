package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface PriceRepository extends JpaRepository<Price, String>,
        JpaSpecificationExecutor<Price>, CustomPriceRepository {

    boolean existsById(String id);

    Boolean checkPrice(String vehicleType, String route, String priceId);

}
