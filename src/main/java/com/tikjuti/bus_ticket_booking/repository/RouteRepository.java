package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface RouteRepository extends JpaRepository<Route, String>,
        JpaSpecificationExecutor<Route>, CustomRouteRepository {

    boolean existsById(String id);

    Boolean checkRoute(String departureLocation, String arrivalLocation, String departurePoint,
            String arrivalPoint
    );

}
