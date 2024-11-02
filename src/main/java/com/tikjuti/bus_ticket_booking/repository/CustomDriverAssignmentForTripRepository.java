package com.tikjuti.bus_ticket_booking.repository;

public interface CustomDriverAssignmentForTripRepository {

    Boolean checkIsDriver(String employeeId);

    Boolean canDriverOperateVehicle(String tripId, String employeeId);

    Boolean checkDriverAssignmentForTripExists(String tripId, String employeeId,
                                               String driverAssignmentForTripId
    );

}
