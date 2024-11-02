package com.tikjuti.bus_ticket_booking.repository;

import org.springframework.stereotype.Repository;


@Repository
public interface CustomPriceRepository {
    Boolean checkPrice(String vehicleType, String route, String priceId);
}
