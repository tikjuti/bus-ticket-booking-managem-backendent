package com.tikjuti.bus_ticket_booking.repository;

import org.springframework.stereotype.Repository;


@Repository
public interface CustomRouteRepository {

    Boolean checkRoute(String departureLocation, String arrivalLocation,
                       String departurePoint, String arrivalPoint
    );

}
