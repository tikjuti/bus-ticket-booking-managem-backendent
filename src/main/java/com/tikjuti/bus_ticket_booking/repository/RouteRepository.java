package com.tikjuti.bus_ticket_booking.repository;

import com.tikjuti.bus_ticket_booking.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
    boolean existsById(String id);

    @Procedure(name = "checkRoute")
    Boolean checkRoute(
            @Param("departureLocation") String departureLocation,
            @Param("arrivalLocation") String arrivalLocation,
            @Param("departurePoint") String departurePoint,
            @Param("arrivalPoint") String arrivalPoint,
            @Param("distance") int distance,
            @Param("duration") int duration
    );

}
