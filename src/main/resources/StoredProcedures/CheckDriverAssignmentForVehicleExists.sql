CREATE PROCEDURE CheckDriverAssignmentForVehicleExists(
    IN startDate DATE,
    IN endDate DATE,
    IN vehicleId VARCHAR(255),
    IN employeeId VARCHAR(255),
    IN driverAssignmentForVehicleId VARCHAR(255),
    OUT isValid BIT
)
BEGIN
    DECLARE counts INT DEFAULT 0;
    
    SELECT COUNT(*) INTO counts
    FROM bus_ticket_booking.driver_assignment_for_vehicle 
    WHERE vehicle_id = vehicleId
	  AND employee_id = employeeId
      AND ((start_date <= endDate AND start_date >= startDate) 
			OR (end_date <= endDate AND end_date >= startDate))
      AND (driverAssignmentForVehicleId IS NULL OR id != driverAssignmentForVehicleId);

    -- Nếu số lượng lớn hơn 0, thì không hợp lệ (1)
    IF (counts > 0) THEN
        SET isValid = 1; -- Invalid
    ELSE
        SET isValid = 0; -- Valid
    END IF;

END;
