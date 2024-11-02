package com.tikjuti.bus_ticket_booking.repository;

import java.util.List;

public interface CustomTicketRepository {

    Boolean checkIsEmployeeBooking(String employeeId);
    List<Object[]> findTripsByUser(String departureLocation, String arrivalLocation, String departureDate);
    int findAvailableSeatsByVehicleId(String vehicleId);
    int findTicketPrice(String vehicleId, String routeId);
}
